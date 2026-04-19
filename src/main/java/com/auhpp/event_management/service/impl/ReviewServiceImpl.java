package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.AttendeeStatus;
import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.dto.request.ReviewCreateRequest;
import com.auhpp.event_management.dto.request.ReviewSearchRequest;
import com.auhpp.event_management.dto.request.ReviewUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.RatingBreakdownResponse;
import com.auhpp.event_management.dto.response.ReviewResponse;
import com.auhpp.event_management.dto.response.ReviewStatsResponse;
import com.auhpp.event_management.entity.Attendee;
import com.auhpp.event_management.entity.EventSession;
import com.auhpp.event_management.entity.Review;
import com.auhpp.event_management.entity.ReviewImage;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.ReviewMapper;
import com.auhpp.event_management.repository.AttendeeRepository;
import com.auhpp.event_management.repository.EventSessionRepository;
import com.auhpp.event_management.repository.ReviewImageRepository;
import com.auhpp.event_management.repository.ReviewRepository;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    EventSessionRepository eventSessionRepository;
    AttendeeRepository attendeeRepository;
    CloudinaryService cloudinaryService;
    ReviewImageRepository reviewImageRepository;

    @Override
    @Transactional
    public ReviewResponse createReview(ReviewCreateRequest request) {
        Review review = new Review();
        EventSession eventSession = eventSessionRepository.findById(request.getEventSessionId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (!eventSession.isExpired()) {
            throw new AppException(ErrorCode.NOT_EXPIRED_EVENT_SESSION);
        }
        Attendee attendee = attendeeRepository.findById(request.getAttendeeId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (attendee.getStatus() != AttendeeStatus.CHECKED_IN) {
            throw new AppException(ErrorCode.CHECKED_IN_TICKET);
        }
        review.setAttendee(attendee);
        review.setEventSession(eventSession);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        reviewRepository.save(review);

        // handle image
        if (!request.getFiles().isEmpty()) {
            if (request.getFiles().size() > 5) {
                throw new AppException(ErrorCode.INVALID_PARAMS);
            }
            List<ReviewImage> reviewImages = new ArrayList<>();
            for (MultipartFile file : request.getFiles()) {

                Map<String, Object> uploadResult = cloudinaryService.uploadFile(file,
                        FolderName.REVIEW.getValue() + review.getId(), true);
                String publicId = (String) uploadResult.get("public_id");
                String imageUrl = (String) uploadResult.get("secure_url");
                reviewImages.add(
                        ReviewImage.builder()
                                .imagePublicId(publicId)
                                .imageUrl(imageUrl)
                                .review(review)
                                .build()
                );
            }
            review.setReviewImages(reviewImages);

            reviewRepository.save(review);
        }
        return reviewMapper.toReviewResponse(review);
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(Long id, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (review.getEditCount() == 1 || ChronoUnit.DAYS.between(review.getCreatedAt(), LocalDateTime.now()) > 7) {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_UPDATE);
        }
        reviewMapper.updateReview(request, review);
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            if (request.getFiles().size() > 5) {
                throw new AppException(ErrorCode.INVALID_PARAMS);
            }
            List<ReviewImage> reviewImages = new ArrayList<>();
            for (MultipartFile file : request.getFiles()) {
                Map<String, Object> uploadResult = cloudinaryService.uploadFile(file,
                        FolderName.REVIEW.getValue() + review.getId(), true);
                String publicId = (String) uploadResult.get("public_id");
                String imageUrl = (String) uploadResult.get("secure_url");
                reviewImages.add(
                        ReviewImage.builder()
                                .imagePublicId(publicId)
                                .imageUrl(imageUrl)
                                .review(review)
                                .build()
                );
            }
            reviewImageRepository.saveAll(reviewImages);
        }
        if (request.getDeleteImageIds() != null && !request.getDeleteImageIds().isEmpty()) {
            if (request.getDeleteImageIds().size() > 5) {
                throw new AppException(ErrorCode.INVALID_PARAMS);
            }
            for (Long imageId : request.getDeleteImageIds()) {
                ReviewImage reviewImage = reviewImageRepository.findById(imageId).orElseThrow(
                        () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
                );
                reviewImageRepository.deleteById(imageId);
                cloudinaryService.deleteFile(reviewImage.getImagePublicId());
            }
        }
        review.setEditCount(review.getEditCount() + 1);
        reviewRepository.save(review);
        return reviewMapper.toReviewResponse(review);
    }

    @Override
    public PageResponse<ReviewResponse> getReviews(ReviewSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Review> pageData = reviewRepository.filterReview(request.getEventSessionId(),
                request.getUserId(), request.getAttendeeId(), request.getRating(), request.getEmail(), pageable);
        List<ReviewResponse> responses = pageData.getContent().stream().map(
                reviewMapper::toReviewResponse
        ).toList();
        return PageResponse.<ReviewResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public ReviewStatsResponse getReviewStats(Long eventSessionId) {
        List<Object[]> ratingCounts = reviewRepository.countReviewsByRating(
                null, eventSessionId, null, null, null);
        long totalReview = 0;
        long totalScore = 0;

        long[] starCounts = new long[6];
        for (Object[] row : ratingCounts) {
            int star = (Integer) row[0];
            long count = (Long) row[1];
            starCounts[star] = count;

            totalReview += count;
            totalScore += (star * count);
        }

        double averageRating = totalReview > 0 ? ((double) totalScore / totalReview) : 0.0;

        List<RatingBreakdownResponse> breakdownList = new ArrayList<>();
        for (int i = 5; i >= 1; i--) {
            long count = starCounts[i];
            double percent = totalReview > 0 ? ((double) count / totalReview) * 100 : 0.0;

            breakdownList.add(RatingBreakdownResponse.builder()
                    .count(count)
                    .percent(Math.round(percent * 10.0) / 10.0)
                    .stars(i)
                    .build());
        }
        return ReviewStatsResponse.builder()
                .averageRating(Math.round(averageRating * 10.0) / 10.0)
                .totalReviews(totalReview)
                .breakdown(breakdownList)
                .build();
    }
}
