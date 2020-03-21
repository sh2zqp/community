package com.sh2zqp.community.controller;

import com.sh2zqp.community.dto.AccessTokenDTO;
import com.sh2zqp.community.dto.GithubUser;
import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.User;
import com.sh2zqp.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response,
                           RedirectAttributes attributes) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);

        GithubUser githubUser;
        if (accessToken != null) {
            githubUser = githubProvider.getUser(accessToken);
        } else {
            // 获取Token失败
            githubUser = null;
            attributes.addAttribute("error", "获取Token失败");
        }

        if (githubUser != null) {
            // 从数据库中判断用户是否已经存在
            if (!githubProvider.githubUserIsExists(userMapper.countUser(String.valueOf(githubUser.getId())))) {
                // 登录成功，写session和cookie
                User user = new User();
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setAccountId(String.valueOf(githubUser.getId()));
                user.setName(githubUser.getName());
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                user.setBio(githubUser.getBio());
                user.setAvatarUrl(githubUser.getAvatarUrl());
                userMapper.insert(user);
                response.addCookie(new Cookie("token", token));
            } else {
                attributes.addAttribute("error", "该用户已经注册");
            }
        } else {
            // 登录失败，重新登录
            attributes.addAttribute("error", "获取用户失败");
        }
        return "redirect:/";
    }
}
