package com.sh2zqp.community.mapper;

import com.sh2zqp.community.model.Question;

public interface QuestionExtendMapper {
    void incView(Question record);
    void incComment(Question record);
}