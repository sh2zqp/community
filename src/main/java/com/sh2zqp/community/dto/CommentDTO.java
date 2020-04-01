package com.sh2zqp.community.dto;

import com.sh2zqp.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private User user;
}
