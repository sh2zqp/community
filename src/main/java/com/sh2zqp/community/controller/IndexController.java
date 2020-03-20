package com.sh2zqp.community.controller;

import com.sh2zqp.community.dto.PageDisplayDTO;
import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.User;
import com.sh2zqp.community.service.QuestionService;
import com.sh2zqp.community.utils.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user = CommunityUtil.getSessionUser(request, userMapper);
        if (user != null) {
            request.getSession().setAttribute("user", user);
        }

        PageDisplayDTO pageDisplayDTO = questionService.list(page, size);
        model.addAttribute("pageDisplayDTO", pageDisplayDTO);

        return "index";
    }
}
