package com.sh2zqp.community.service;

import com.sh2zqp.community.dto.PageDisplayDTO;
import com.sh2zqp.community.dto.QuestionDTO;
import com.sh2zqp.community.mapper.QuestionMapper;
import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.Question;
import com.sh2zqp.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;


    public PageDisplayDTO list(Integer page, Integer size) {
        Integer totalCount = questionMapper.count();  // 数据库中的总的问题条数
        Integer totalPage = totalCount/size == 0 ? totalCount/size : totalCount/size + 1;  // 总的可以分的页数
        // 非法处理
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        Integer offset = size*(page-1);  // 数据库分页查询偏移量
        List<Question> questions = questions = questionMapper.list(offset, size); // 全部查询
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageDisplayDTO pageDisplayDTO = new PageDisplayDTO();
        for (Question question : questions) {
            User user = userMapper.findUserById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDisplayDTO.setQuestionDTOS(questionDTOS);
        pageDisplayDTO.sePageDisplayDTO(totalCount, totalPage, page);  // 设置页面的一些分页显示数据

        return pageDisplayDTO;
    }

    public PageDisplayDTO list(Integer page, Integer size, Integer userId) {
        Integer totalCount = questionMapper.countByUserId(userId);
        Integer totalPage = totalCount/size == 0 ? totalCount/size : totalCount/size + 1;  // 总的可以分的页数
        // 非法处理
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        Integer offset = size*(page-1);  // 数据库分页查询偏移量
        List<Question>  questions = questionMapper.listByUserId(userId, offset, size);  // 添加userId查询条件
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageDisplayDTO pageDisplayDTO = new PageDisplayDTO();
        for (Question question : questions) {
            User user = userMapper.findUserById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDisplayDTO.setQuestionDTOS(questionDTOS);
        pageDisplayDTO.sePageDisplayDTO(totalCount, totalPage, page);  // 设置页面的一些分页显示数据

        return pageDisplayDTO;
    }
}
