package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.dto.request.TagCreateRequest;
import com.auhpp.event_management.dto.request.TagSearchRequest;
import com.auhpp.event_management.dto.request.TagUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.TagResponse;
import com.auhpp.event_management.entity.Tag;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.TagMapper;
import com.auhpp.event_management.repository.TagRepository;
import com.auhpp.event_management.service.TagService;
import com.auhpp.event_management.util.SlugUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    TagRepository tagRepository;
    TagMapper tagMapper;

    @Override
    @Transactional
    public TagResponse create(TagCreateRequest request) {
        // mapper
        Tag tag = tagMapper.toTag(request);

        // handle slug
        String slug = SlugUtils.toSlug(request.getName());
        tag.setSlug(slug);

        // check exists tag
        Optional<Tag> optionalTag = tagRepository.findBySlug(slug);
        if (optionalTag.isPresent()) {
            throw new AppException(ErrorCode.RESOURCE_EXISTS);
        }

        tagRepository.save(tag);
        return tagMapper.toTagResponse(tag);
    }

    @Override
    @Transactional
    public TagResponse update(Long id, TagUpdateRequest request) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        // handle slug
        String slug = SlugUtils.toSlug(request.getName());
        tag.setSlug(slug);

        // check exists tag
        Optional<Tag> optionalTag = tagRepository.findBySlug(slug);
        if (optionalTag.isPresent()) {
            throw new AppException(ErrorCode.RESOURCE_EXISTS);
        }

        tagRepository.save(tag);
        return tagMapper.toTagResponse(tag);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (tag.getEventTags() != null && !tag.getEventTags().isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_DELETE);
        }
        tagRepository.deleteById(id);
    }

    @Override
    public PageResponse<TagResponse> filter(TagSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Tag> pageData = tagRepository.filter(request.getType(), request.getName(), pageable);
        List<TagResponse> responses = pageData.getContent().stream().map(
                tagMapper::toTagResponse
        ).toList();
        return PageResponse.<TagResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public List<TagResponse> getAll(TagSearchRequest request) {
        List<Tag> tags = tagRepository.findAllByType(request.getType(), request.getName(), request.getEventId());
        return tags.stream().map(tagMapper::toTagResponse).toList();
    }
}
