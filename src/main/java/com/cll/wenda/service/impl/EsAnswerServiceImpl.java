package com.cll.wenda.service.impl;

import com.cll.wenda.model.es.EsAnswer;
import com.cll.wenda.repository.EsAnswerRepository;
import com.cll.wenda.service.EsAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author chenliangliang
 * @date: 2017/11/22
 */
@Service
public class EsAnswerServiceImpl implements EsAnswerService {

    private EsAnswerRepository repository;

    @Autowired
    protected EsAnswerServiceImpl(EsAnswerRepository repository){
        this.repository=repository;
    }


    @Override
    public void remove(EsAnswer esAnswer) {
        repository.delete(esAnswer);
    }

    @Override
    public EsAnswer update(EsAnswer esAnswer) {
        return repository.save(esAnswer);
    }

    @Override
    public EsAnswer getEsAnswerByQid(Integer answerId) {
        return repository.findByAnsId(answerId);
    }

    @Override
    public Page<EsAnswer> listNewestEsAnswers(String keyword, int pageNo, int pageSize) {
        Page<EsAnswer> page;
        Sort sort = new Sort(Sort.Direction.DESC,  "ansPubtime");
        Pageable pageable = new PageRequest(pageNo-1, pageSize, sort);

        page = repository.findByAnsContentContainingOrAnsAuthorContaining(
                keyword, keyword, pageable
        );
        return page;
    }

    @Override
    public Page<EsAnswer> listHotestEsAnswers(String keyword, int pageNo, int pageSize) {
        Page<EsAnswer> page;
        Sort sort = new Sort(Sort.Direction.DESC,  "ansCollect","ansDz","ansCmt");
        Pageable pageable = new PageRequest(pageNo-1, pageSize, sort);

        page = repository.findByAnsContentContainingOrAnsAuthorContaining(
                keyword, keyword, pageable
        );
        return page;
    }

    @Override
    public Page<EsAnswer> listEsAnswers(Pageable pageable) {
        return repository.findAll(pageable);
    }


    @Override
    public Page<EsAnswer> listHotSwiper(int pageNo, int pageSize) {
        Page<EsAnswer> page;
        Sort sort = new Sort(Sort.Direction.DESC,  "ansCollect","ansDz","ansCmt");
        Pageable pageable = new PageRequest(pageNo-1, pageSize, sort);

        page = repository.findByAnsPicStartingWith("http",pageable);
        return page;
    }
}
