package com.cll.wenda.controller;

import com.cll.wenda.exception.AppException;
import com.cll.wenda.model.Question;
import com.cll.wenda.model.Result;
import com.cll.wenda.service.QuestionService;
import com.cll.wenda.utils.ResultUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author chenliangliang
 * @date: 2017/10/26
 */
@RestController
@RequestMapping(value = "/question")
public class QuestionController {

    private QuestionService questionService;

    @Autowired
    protected QuestionController(QuestionService questionService){
        this.questionService=questionService;
    }

    /**
     * 用户发表文章
     *
     * @param question
     * @return
     */
    @PostMapping
    public ResponseEntity<Result> addQuestion(@RequestBody @Valid Question question,
                                              HttpServletRequest request,
                                              BindingResult result) {
        if (result.hasErrors()){
            return new ResponseEntity<>(ResultUtil.failResult(result.getAllErrors().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            Integer uid = (Integer) request.getAttribute("uid");
            String name=(String) request.getAttribute("name");
            question.setPuber(uid);
            if (question.getIsanony()==0){
                question.setUsername(name);
            }else {
                question.setUsername("匿名");
            }
            questionService.saveQuestion(question,uid);
            return new ResponseEntity<>(ResultUtil.successResult("发表问题成功", question), HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/{qid}")
    public ResponseEntity<Result> getQuestion(@PathVariable("qid") int qid,
                                              HttpServletRequest request) {

        try {
            Integer uid = (Integer) request.getAttribute("uid");

            Question question = questionService.readQuestion(uid, qid);
            if (question != null) {
                return new ResponseEntity<>(ResultUtil.successResult("ok", question), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity<>(ResultUtil.failResult("NO The Question that it's id is" + qid), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 用户关注问题
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/att")
    public ResponseEntity<Result> attention(@RequestBody Map<String, Object> map,
                                            HttpServletRequest request) {

        try {
            Integer uid = (Integer)request.getAttribute("uid");
            Integer qid = Integer.valueOf(map.get("qid").toString());
            questionService.attention(uid, qid);
            return new ResponseEntity<>(ResultUtil.successResult("OK", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("/att/{pageNo}")
    public ResponseEntity<Result> getAttQuestion(@PathVariable(value = "pageNo",required = false) int pageNo,
                                                 HttpServletRequest request) {
        if (pageNo<0){
            pageNo=1;
        }

        try {
            Integer uid = (Integer) request.getAttribute("uid");
            PageInfo<Question> questions = questionService.getAttQuestion(uid,pageNo,12);
            return new ResponseEntity<>(ResultUtil.successResult("OK", questions), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/my/{pageNo}")
    public ResponseEntity<Result> getMyQuestions(@PathVariable(value = "pageNo") int pageNo,
                                                 HttpServletRequest request) {

        if (pageNo<=0){
            pageNo=1;
        }
        try {
            Integer uid = (Integer) request.getAttribute("uid");
            PageInfo<Question> questions = questionService.getMyQuestion(uid,pageNo,12);
            return new ResponseEntity<>(ResultUtil.successResult("OK", questions), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/att/{qid}")
    public ResponseEntity<Result> cancelAtt(@PathVariable("qid") int qid,
                                            HttpServletRequest request) {
        try {
            Integer uid = (Integer) request.getAttribute("uid");
            questionService.cancelAttention(uid,qid);
            return new ResponseEntity<>(ResultUtil.successResult("取消关注成功", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/hot/{pageNo}")
    @Cacheable(value = "hot", key = "'getHotQuestion_pageNum_'+#pageNo")
    public PageInfo<Question> getHotQuestion(@PathVariable("pageNo") int pageNo,
                                             HttpServletRequest request) {

        Integer uid = (Integer) request.getAttribute("uid");
        if (pageNo<=0){
            pageNo=1;
        }
        int pageSize = 12;
        return questionService.getHotQuestion(uid,pageNo+1, pageSize);
    }


    @PostMapping("/topic")
    public ResponseEntity<Result> getTopicQuestions(HttpServletRequest request,@RequestBody Map<String,Object> map) {
        try {
            String topic=(String)map.get("topic");
            System.out.println("topic---"+topic);
            Integer pageNo=(Integer)map.get("pageNo");
            if (pageNo==null||pageNo<=0){
                System.out.println("pageNo---"+pageNo);
                pageNo=1;
            }
            Integer uid = (Integer) request.getAttribute("uid");
            PageInfo<Question> questionPageInfo=questionService.getTopicQuestion(topic,uid,pageNo,12);
            System.out.println(questionPageInfo);
            return new ResponseEntity<>(ResultUtil.successResult("OK", questionPageInfo), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("查询错误"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
