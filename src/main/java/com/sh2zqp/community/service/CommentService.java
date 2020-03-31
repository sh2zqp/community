package com.sh2zqp.community.service;

import com.sh2zqp.community.enums.CommentTypeEnum;
import com.sh2zqp.community.exception.CustomizeErrorCode;
import com.sh2zqp.community.exception.CustomizeException;
import com.sh2zqp.community.mapper.CommentMapper;
import com.sh2zqp.community.mapper.QuestionExtendMapper;
import com.sh2zqp.community.mapper.QuestionMapper;
import com.sh2zqp.community.model.Comment;
import com.sh2zqp.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtendMapper questionExtendMapper;

    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExists(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType().equals(CommentTypeEnum.QUESTION.getType())) {
            // 回复问题（一级评论）
            Question parentQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (parentQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.REPLY_QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            parentQuestion.setCommentCount(1); // 新增
            questionExtendMapper.incComment(parentQuestion);  // 新增一条评论
        } else if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            // 回复评论（二级评论）
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (parentComment == null) {
                throw new CustomizeException(CustomizeErrorCode.REPLY_COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }
    }
}
