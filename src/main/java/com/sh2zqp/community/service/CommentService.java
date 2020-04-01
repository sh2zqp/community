package com.sh2zqp.community.service;

import com.sh2zqp.community.dto.CommentDTO;
import com.sh2zqp.community.enums.CommentTypeEnum;
import com.sh2zqp.community.exception.CustomizeErrorCode;
import com.sh2zqp.community.exception.CustomizeException;
import com.sh2zqp.community.mapper.CommentMapper;
import com.sh2zqp.community.mapper.QuestionExtendMapper;
import com.sh2zqp.community.mapper.QuestionMapper;
import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtendMapper questionExtendMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional
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

    public List<CommentDTO> listByQuestionId(Long questionId) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(questionId)
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        // 获取去重的评论人
        List<Long> userIds = comments.stream().map(Comment::getCommentator).distinct().collect(Collectors.toList());

        // 获取评论人并转为map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOList = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());


        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            Long commentator = comment.getCommentator();
            User user = userMapper.selectByPrimaryKey(commentator);
            commentDTO.setUser(user);
            commentDTOS.add(commentDTO);
        }

        return commentDTOList;
    }
}
