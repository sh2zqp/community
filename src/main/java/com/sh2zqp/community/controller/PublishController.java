package com.sh2zqp.community.controller;

import com.sh2zqp.community.mapper.QuestionMapper;
import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.Question;
import com.sh2zqp.community.model.User;
import com.sh2zqp.community.utils.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            HttpServletRequest request,
            Model model) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);

        if (title == null || title.equals("")) {
            model.addAttribute("error", "问题标题不能为空");
            return "publish";   // 有异常回到publish页面
        }
        if (description == null || description.equals("")) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";   // 有异常回到publish页面
        }
        if (tag == null || tag.equals("")) {
            model.addAttribute("error", "问题标签不能为空");
            return "publish";   // 有异常回到publish页面
        }

        User user = CommunityUtil.getSessionUser(request, userMapper);
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";   // 有异常回到publish页面
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);

        return "redirect:/";   // 无异常返回首页
    }
}