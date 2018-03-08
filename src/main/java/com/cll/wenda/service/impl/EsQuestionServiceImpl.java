package com.cll.wenda.service.impl;

import com.cll.wenda.model.User;
import com.cll.wenda.model.es.EsQuestion;
import com.cll.wenda.repository.EsQuestionRepository;
import com.cll.wenda.service.EsQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/11/19
 */
@Service
public class EsQuestionServiceImpl implements EsQuestionService {


    private EsQuestionRepository repository;


    @Autowired
    protected EsQuestionServiceImpl(EsQuestionRepository repository){
        this.repository=repository;
    }

    @Override
    public void remove(String id) {
        repository.delete(id);
    }

    @Override
    public EsQuestion update(EsQuestion esQuestion) {
        return repository.save(esQuestion);
    }

    @Override
    public EsQuestion getEsQuestionByQuestionId(Integer questionId) {
        return repository.findByQueId(questionId);
    }

    @Override
    public Page<EsQuestion> listNewestEsQuestion(String keyword, int pageNo, int pageSize) {
        Page<EsQuestion> page;
        Sort sort = new Sort(Sort.Direction.DESC, "quePubtime");
        Pageable pageable = new PageRequest(pageNo-1, pageSize, sort);
        page = repository.findEsQuestionsByQueTitleContainingOrQueContentContainingOrQueAuthorContaining(
                keyword, keyword, keyword, pageable
        );
        return page;
    }

    @Override
    public Page<EsQuestion> listHotestEsQuestion(String keyword, int pageNo, int pageSize) {
        Page<EsQuestion> page;
        Sort sort = new Sort(Sort.Direction.DESC, "queReadSize", "queAtt", "queAns", "quePubtime");
        Pageable pageable = new PageRequest(pageNo-1, pageSize, sort);

        page = repository.findEsQuestionsByQueTitleContainingOrQueContentContainingOrQueAuthorContaining(
                keyword, keyword, keyword, pageable
        );
        return page;
    }

    @Override
    public Page<EsQuestion> listEsQuestion(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<User> listTop12Users() {
        return null;
    }

    @Override
    public Page<EsQuestion> listHotSwiper(int pageNo, int pageSize) {
        Page<EsQuestion> page;
        Sort sort = new Sort(Sort.Direction.DESC, "queReadSize", "queAtt", "queAns", "quePubtime");
        Pageable pageable = new PageRequest(pageNo-1, pageSize, sort);
        page = repository.findByQuePicStartingWith("http",pageable);
        return page;
    }
}
