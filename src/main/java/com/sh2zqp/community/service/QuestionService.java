package com.sh2zqp.community.service;

import com.sh2zqp.community.dto.PageDisplayDTO;
import com.sh2zqp.community.dto.QuestionDTO;
import com.sh2zqp.community.exception.CustomizeErrorCode;
import com.sh2zqp.community.exception.CustomizeException;
import com.sh2zqp.community.mapper.QuestionExtendMapper;
import com.sh2zqp.community.mapper.QuestionMapper;
import com.sh2zqp.community.mapper.UserMapper;
import com.sh2zqp.community.model.Question;
import com.sh2zqp.community.model.QuestionExample;
import com.sh2zqp.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
    @Autowired
    private QuestionExtendMapper questionExtendMapper;


    public PageDisplayDTO list(Integer page, Integer size) {
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());  // 数据库中的总的问题条数
        Integer totalPage = totalCount%size == 0 ? totalCount/size : totalCount/size + 1;  // 总的可以分的页数
        // 非法处理
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        int offset = size*(page-1);  // 数据库分页查询偏移量
        // 全部查询
        QuestionExample questionExample = new QuestionExample();
        RowBounds rowBounds = new RowBounds(offset, size);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample, rowBounds);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageDisplayDTO pageDisplayDTO = new PageDisplayDTO();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDisplayDTO.setQuestionDTOS(questionDTOS);
        pageDisplayDTO.setPageDisplayDTO(totalCount, totalPage, page);  // 设置页面的一些分页显示数据

        return pageDisplayDTO;
    }

    public PageDisplayDTO list(Integer page, Integer size, Long userId) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        Integer totalPage = totalCount%size == 0 ? totalCount/size : totalCount/size + 1;  // 总的可以分的页数
        // 非法处理
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        Integer offset = size*(page-1);  // 数据库分页查询偏移量
        // 添加userId查询条件
        QuestionExample questionExampleByUserId = new QuestionExample();
        questionExampleByUserId.createCriteria().andCreatorEqualTo(userId);
        RowBounds rowBounds = new RowBounds(offset, size);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExampleByUserId, rowBounds);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageDisplayDTO pageDisplayDTO = new PageDisplayDTO();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDisplayDTO.setQuestionDTOS(questionDTOS);
        pageDisplayDTO.setPageDisplayDTO(totalCount, totalPage, page);  // 设置页面的一些分页显示数据

        return pageDisplayDTO;
    }

    public QuestionDTO getQuestionById(Long id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        Question questionById = questionMapper.selectByPrimaryKey(question.getId());
        if (questionById == null) {
            // 创建
            questionMapper.insert(question);
        } else {
            // 更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if (update != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question recordQuestion = new Question();
        recordQuestion.setId(id);
        questionExtendMapper.incView(recordQuestion);
    }
}
