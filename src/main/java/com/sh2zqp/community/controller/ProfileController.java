package com.sh2zqp.community.controller;

import com.sh2zqp.community.dto.PageDisplayDTO;
import com.sh2zqp.community.model.User;
import com.sh2zqp.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          HttpServletRequest request,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {

        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            request.getSession().setAttribute("user", user);
        } else {
            return "redirect:/";  // 如果无用户登录, 直接返回主页面，不能访问此页面
        }

        PageDisplayDTO pageDisplayDTO = questionService.list(page, size, user.getId()); // 根据userId查询问题数
        model.addAttribute("pageDisplayDTO", pageDisplayDTO);

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        return "profile";
    }
}
