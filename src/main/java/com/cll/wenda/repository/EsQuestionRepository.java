package com.cll.wenda.repository;

import com.cll.wenda.model.es.EsQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author chenliangliang
 * @date: 2017/11/19
 */
public interface EsQuestionRepository extends ElasticsearchRepository<EsQuestion,String> {

    /**
     *模糊查询
     * @param title
     * @param content
     * @param username
     * @param pageable
     * @return
     */
    Page<EsQuestion> findEsQuestionsByQueTitleContainingOrQueContentContainingOrQueAuthorContaining(String title, String content, String username, Pageable pageable);


    /**
     *
     * @param questionId
     * @return
     */
    EsQuestion findByQueId(Integer questionId);

    /**
     * 首页轮播图
     * @param pageable
     * @param picStart
     * @return
     */
    Page<EsQuestion> findByQuePicStartingWith(String picStart,Pageable pageable);

}
