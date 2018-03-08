package com.cll.wenda.service;

import com.cll.wenda.model.User;
import com.cll.wenda.model.es.EsQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/11/19
 */
public interface EsQuestionService {


    /**
     * 删除EsQuestion
     * @param id
     */
    void remove(String id);

    /**
     * 跟新EsQuestion
     * @param esQuestion
     * @return
     */
    EsQuestion update(EsQuestion esQuestion);

    /**
     * 根据questionId删除EsQuestion
     * @param questionId
     * @return
     */
    EsQuestion getEsQuestionByQuestionId(Integer questionId);

    /**
     * 最新问题列表，分页
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<EsQuestion> listNewestEsQuestion(String keyword, int pageNo, int pageSize);

    /**
     * 最热问题列表，分页
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<EsQuestion> listHotestEsQuestion(String keyword,int pageNo, int pageSize);

    /**
     * 问题列表，分页
     * @param pageable
     * @return
     */
    Page<EsQuestion> listEsQuestion(Pageable pageable);

    /**
     * 最热前12用户
     * @return
     */
    List<User> listTop12Users();


    Page<EsQuestion> listHotSwiper(int pageNo,int pageSize);

}
