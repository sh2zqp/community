package com.sh2zqp.community.controller;

import com.sh2zqp.community.dto.QuestionDTO;
import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.User;
import com.sh2zqp.community.service.QuestionService;
import com.sh2zqp.community.utils.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        User user = CommunityUtil.getSessionUser(request, userMapper);
        if (user != null) {
            request.getSession().setAttribute("user", user);
        }

        List<QuestionDTO> questions = questionService.list();
        model.addAttribute("questions", questions);

        return "index";
    }
}
