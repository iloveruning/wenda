package com.cll.wenda.controller;

import com.cll.wenda.mapper.KeywordMapper;
import com.cll.wenda.model.Banner;
import com.cll.wenda.model.Question;
import com.cll.wenda.model.Result;
import com.cll.wenda.model.es.EsAnswer;
import com.cll.wenda.model.es.EsQuestion;
import com.cll.wenda.model.es.Search;
import com.cll.wenda.service.BannerService;
import com.cll.wenda.service.EsAnswerService;
import com.cll.wenda.service.EsQuestionService;
import com.cll.wenda.service.QuestionService;
import com.cll.wenda.utils.ResultUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenliangliang
 * @date: 2017/11/18
 */
@RestController
@RequestMapping(value = "/index")
public class MainController {


    private EsQuestionService esQuestionService;

    private QuestionService questionService;

    private EsAnswerService esAnswerService;

    private KeywordMapper keywordMapper;

    private ThreadPoolTaskExecutor executor;

    private BannerService bannerService;


    @Autowired
    protected MainController(EsQuestionService esQuestionService, QuestionService questionService,
                             EsAnswerService esAnswerService, KeywordMapper keywordMapper, ThreadPoolTaskExecutor executor,
                             BannerService bannerService) {
        this.esQuestionService = esQuestionService;
        this.questionService = questionService;
        this.esAnswerService = esAnswerService;
        this.keywordMapper = keywordMapper;
        this.executor = executor;
        this.bannerService=bannerService;
    }

    @PostMapping("/search")
    public ResponseEntity<Result> search(@RequestBody @Valid Search search,
                                         BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(ResultUtil.failResult(result.getAllErrors().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String keyword = search.getKeyword();
        String type = search.getType();
        String sort = search.getSort();
        Integer pageNo = search.getPageNo();
        Integer pageSize = search.getPageSize();

        if (pageNo <= 0) {
            pageNo = 1;
        }

        if (pageSize <= 0) {
            pageSize = 4;
        }

        try {
            Map<String, Object> map;
            switch (type) {
                case "que":
                    map = getQuestionSearchResult(sort, keyword, pageNo, pageSize);
                    break;

                case "ans":
                    map = getAnswerSearchResult(sort, keyword, pageNo, pageSize);
                    break;

                case "all":
                    map = new HashMap<>(2);
                    map.put("questions", getQuestionSearchResult(sort, keyword, pageNo, pageSize));
                    map.put("answers", getAnswerSearchResult(sort, keyword, pageNo, pageSize));
                    break;
                default:
                    map = getQuestionSearchResult(sort, keyword, pageNo, pageSize);
                    break;
            }

            executor.execute(() -> {

                if (keywordMapper.isExist(keyword) > 0) {
                    keywordMapper.updateKeywordCounts(keyword);
                } else {
                    keywordMapper.save(keyword);
                }

            });

            return new ResponseEntity<>(ResultUtil.successResult("OK", map), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("查询失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("/{openid}")
    @Cacheable(value = "index", key = "'pageOnLoad_openid_'+#openid")
    public ResponseEntity<Result> pageOnLoad(@PathVariable(value = "openid", required = false) String openid,
                                             HttpServletRequest request) {

        try {
            Map<String, Object> res = new HashMap<>(3);
            Integer uid=(Integer)request.getAttribute("uid");
           List<Banner> swiper=bannerService.listNewestBanner();

            res.put("swiper", swiper);
            PageInfo<Question> topic = questionService.getHotTopic(1, 4);

            res.put("hotTopic", topic.getList());

            PageInfo<Question> questions = questionService.getHotQuestion(uid,1, 12);

            res.put("question", questions.getList());

            return new ResponseEntity<>(ResultUtil.successResult("OK", res), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            removeCache(openid);
            return new ResponseEntity<>(ResultUtil.failResult("数据加载失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @CacheEvict(value = "index", key = "'pageOnLoad_openid_'+#openid")
    public void removeCache(String openid) {
        System.out.println("清除首页数据缓存");
    }


    private Map<String, Object> getQuestionSearchResult(String sort, String keyword, int pageNo, int pageSize) {
        Page<EsQuestion> questions;
        if ("hot".equalsIgnoreCase(sort)) {
            questions = esQuestionService.listHotestEsQuestion(keyword, pageNo, pageSize);
        } else {
            questions = esQuestionService.listNewestEsQuestion(keyword, pageNo, pageSize);
        }
        Map<String, Object> queMap = new HashMap<>(4);
        queMap.put("totalPages", questions.getTotalPages());
        queMap.put("totalElements", questions.getTotalElements());
        queMap.put("page", pageNo);
        queMap.put("data", questions.getContent());
        return queMap;
    }

    private Map<String, Object> getAnswerSearchResult(String sort, String keyword, int pageNo, int pageSize) {
        Page<EsAnswer> answers;
        if ("hot".equalsIgnoreCase(sort)) {
            answers = esAnswerService.listHotestEsAnswers(keyword, pageNo, pageSize);
        } else {
            answers = esAnswerService.listNewestEsAnswers(keyword, pageNo, pageSize);
        }
        Map<String, Object> ansMap = new HashMap<>(4);
        ansMap.put("totalPages", answers.getTotalPages());
        ansMap.put("totalElements", answers.getTotalElements());
        ansMap.put("page", pageNo);
        ansMap.put("data", answers.getContent());
        return ansMap;
    }


    @PostMapping("/keyword")
    public ResponseEntity<Result> getKeywords(@RequestBody Map<String, Object> map) {

        try {
            String kwd = "%" + map.get("keyword") + "%";
            System.out.println(kwd);
            List<String> res = keywordMapper.blurQuery(kwd);
            return new ResponseEntity<>(ResultUtil.successResult("OK", res), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("获取搜索关键字失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("/keyword/hot")
    public ResponseEntity<Result> getHotKeywords() {
        try {
            List<String> res = keywordMapper.findHotKeywords();
            return new ResponseEntity<>(ResultUtil.successResult("OK", res), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("获取热门关键字失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 更多热门话题
     * @param pageNo
     * @return
     */
    @GetMapping("/hotTopic/{pageNo}")
    public ResponseEntity<Result> getHotTopic(@PathVariable(value = "pageNo",
                                                  required = false) int pageNo) {
        try {
            if (pageNo<=0){
                pageNo=1;
            }
            PageInfo<Question> topics=questionService.getHotTopic(pageNo+1,12);
            return new ResponseEntity<>(ResultUtil.successResult("OK",topics),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("获取热门话题失败"),HttpStatus.INTERNAL_SERVER_ERROR);
        }




    }


}
