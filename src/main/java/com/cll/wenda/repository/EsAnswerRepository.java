package com.cll.wenda.repository;

import com.cll.wenda.model.es.EsAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author chenliangliang
 * @date: 2017/11/20
 */
@Repository
public interface EsAnswerRepository extends ElasticsearchRepository<EsAnswer,String> {

    Page<EsAnswer> findByAnsContentContainingOrAnsAuthorContaining(String content, String username, Pageable pageable);

    EsAnswer findByAnsId(Integer answerId);

    Page<EsAnswer> findByAnsPicStartingWith(String picStart,Pageable pageable);

}
