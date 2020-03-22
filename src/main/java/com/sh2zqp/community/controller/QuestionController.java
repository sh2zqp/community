package com.sh2zqp.community.controller;

import com.sh2zqp.community.dto.QuestionDTO;
import com.sh2zqp.community.model.Question;
import com.sh2zqp.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model) {
        QuestionDTO questionDTO = questionService.getQuestion(id);
        model.addAttribute("questionDTO", questionDTO);
        return "question";
    }
}