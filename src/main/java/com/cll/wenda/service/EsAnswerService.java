package com.cll.wenda.service;

import com.cll.wenda.model.es.EsAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author chenliangliang
 * @date: 2017/11/22
 */
public interface EsAnswerService {

    /**
     * 移除elasticsearch中的回答
     * @param esAnswer
     */
    void remove(EsAnswer esAnswer);


    /**
     * 跟新elasticsearch中的回答
     * @param esAnswer
     * @return
     */
    EsAnswer update(EsAnswer esAnswer);

    /**
     * 根据ID查询回答
     * @param answerId
     * @return
     */
    EsAnswer getEsAnswerByQid(Integer answerId);

    /**
     * 最新回答列表，分页
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<EsAnswer> listNewestEsAnswers(String keyword,int pageNo,int pageSize);


    /**
     * 最热回答列表，分页
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<EsAnswer> listHotestEsAnswers(String keyword,int pageNo,int pageSize);

    /**
     * 问题列表，分页
     * @param pageable
     * @return
     */
    Page<EsAnswer> listEsAnswers(Pageable pageable);

    /**
     * 首页轮播图
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<EsAnswer> listHotSwiper(int pageNo,int pageSize);

}
