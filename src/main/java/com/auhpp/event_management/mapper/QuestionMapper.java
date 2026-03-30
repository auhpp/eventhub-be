package com.auhpp.event_management.mapper;

import com.auhpp.event_management.dto.response.QuestionResponse;
import com.auhpp.event_management.entity.Question;
import com.auhpp.event_management.repository.UpvoteQuestionRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public abstract class QuestionMapper {

    @Autowired
    protected UpvoteQuestionRepository upvoteQuestionRepository;

    public abstract QuestionResponse toQuestionResponse(Question question, @Context Long currentUserId);

    @AfterMapping
    protected void customizeMapping(Question question, @MappingTarget QuestionResponse response, @Context Long currentUserId) {
        if (question.getHasAnonymous()) {
            response.setAppUser(null);
        }
        response.setUpvoteCount(question.getUpvoteQuestions() == null ? 0 : question.getUpvoteQuestions().size());
        if (currentUserId != null && question.getId() != null) {
            boolean isUpvote = upvoteQuestionRepository.existsByAppUserIdAndQuestionId(currentUserId, question.getId());
            response.setUpVoted(isUpvote);
        } else {
            response.setUpVoted(false);
        }
        response.setActionUserId(currentUserId);
        response.setEventSessionId(question.getEventSession().getId());
    }
}
