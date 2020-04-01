package com.sh2zqp.community.controller;

import com.sh2zqp.community.dto.CommentDTO;
import com.sh2zqp.community.dto.QuestionDTO;
import com.sh2zqp.community.model.Question;
import com.sh2zqp.community.service.CommentService;
import com.sh2zqp.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        List<CommentDTO> commentDTOS = commentService.listByQuestionId(id);
        questionService.incView(id); // 新增浏览+1
        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("commentDTOs", commentDTOS);
        return "question";
    }
}
