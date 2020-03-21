package com.sh2zqp.community.controller;

import com.sh2zqp.community.dto.PageDisplayDTO;
import com.sh2zqp.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {

        PageDisplayDTO pageDisplayDTO = questionService.list(page, size); // 全部查询
        model.addAttribute("pageDisplayDTO", pageDisplayDTO);

        return "index";
    }
}
