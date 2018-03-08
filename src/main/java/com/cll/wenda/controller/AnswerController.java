package com.cll.wenda.controller;

import com.cll.wenda.model.Answer;
import com.cll.wenda.model.Result;
import com.cll.wenda.service.AnswerService;
import com.cll.wenda.utils.ResultUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/answer")
public class AnswerController {



    private AnswerService answerService;

    @Autowired
    protected AnswerController(AnswerService answerService){
        this.answerService=answerService;
    }

    /**
     * 回答问题
     *
     * @param answer
     * @return
     */
    @PostMapping
    public ResponseEntity<Result> pubAnswer(@RequestBody @Valid Answer answer,
                                            HttpServletRequest request,
                                            BindingResult result) {
        if (result.hasErrors()){
            return new ResponseEntity<>(ResultUtil.failResult(result.getAllErrors().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Integer uid = (Integer) request.getAttribute("uid");
            String username = (String) request.getAttribute("name");

            answer.setPuber(uid);
            if (answer.getIsanony()==0){
                answer.setUsername(username);
            }else {
                answer.setUsername("匿名");
            }
            answerService.pubAnswer(answer,uid);
            return new ResponseEntity<>(ResultUtil.successResult("回答问题成功", answer), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 回答点赞
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/dz")
    public ResponseEntity<Result> thumbsUp(@RequestBody Map<String, Object> map,
                                           HttpServletRequest request) {
        try {
            int aid = Integer.parseInt(map.get("aid").toString()) ;
            Integer uid = (Integer) request.getAttribute("uid");
            answerService.thumbsUp(uid, aid);
            return new ResponseEntity<>(ResultUtil.successResult("点赞成功", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 收藏回答
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/collect")
    public ResponseEntity<Result> collectAnswer(@RequestBody Map<String, Object> map,
                                                HttpServletRequest request) {
        try {
            Integer aid = Integer.valueOf(map.get("aid").toString());
            Integer uid = (Integer) request.getAttribute("uid");
            answerService.collectAnswer(uid, aid);
            return new ResponseEntity<>(ResultUtil.successResult("收藏成功", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 阅读回答
     * @param aid
     * @param request
     * @return
     */
    @GetMapping(value = "/{aid}")
    public ResponseEntity<Result> getAnswer(@PathVariable("aid") int aid,
                                            HttpServletRequest request) {
        try {
            Integer uid=(Integer)request.getAttribute("uid");
            Answer answer = answerService.readAnswer(uid,aid);
            return new ResponseEntity<>(ResultUtil.successResult("OK", answer), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("no the answer"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping(value = "/qu/{qid}/{pageNum}")
    public PageInfo<Answer> getQuestionAnswers(@PathVariable("qid") int qid,
                                               @PathVariable("pageNum") int pageNum) {

        System.out.println("getQuestionAnswers------------>没有走缓存！");
        return answerService.getQuestionAnswers(qid, pageNum);
    }


    /**
     * 我收藏的回答
     * @param pageNum
     * @param request
     * @return
     */
    @GetMapping(value = "/collect/{pageNo}")
    public ResponseEntity<Result> getCollectAnswers(@PathVariable(value = "pageNo",required = false) int pageNum,
                                                    HttpServletRequest request) {
        try {
            if (pageNum<=0){
                pageNum=1;
            }
            Integer uid=(Integer)request.getAttribute("uid");
            PageInfo<Answer> answerPageInfo=answerService.getCollectAnswers(uid, pageNum);
            return new ResponseEntity<>(ResultUtil.successResult("OK", answerPageInfo), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("获取异常"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/my/{pageNum}")
    public ResponseEntity<Result> getMyAnswers(@PathVariable(value = "pageNum",required = false) int pageNum,
                                         HttpServletRequest request) {
        try {
            Integer uid=(Integer)request.getAttribute("uid");
            if (pageNum<=0){
                pageNum=1;
            }
            PageInfo<Answer> answerPageInfo=answerService.getMyAnswers(uid, pageNum);
            return new ResponseEntity<>(ResultUtil.successResult("OK", answerPageInfo), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("获取异常"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 删除回答
     * @param aid
     * @return
     */
    @DeleteMapping("/my/{aid}")
    public ResponseEntity<Result> deleteAnswer(@PathVariable("aid") int aid,
                                               HttpServletRequest request) {
        try {
            Integer uid=(Integer)request.getAttribute("uid");
            answerService.removeMyAnswer(uid,aid);
            return new ResponseEntity<>(ResultUtil.successResult("删除回答成功", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("删除回答失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 取消收藏
     * @return
     */
    @DeleteMapping("/collect/{aid}")
    public ResponseEntity<Result> cancelCollection(@PathVariable("aid") int aid,
                                                   HttpServletRequest request){

        try {
            Integer uid=(Integer)request.getAttribute("uid");
            answerService.cancelCollection(uid,aid);
            return new ResponseEntity<>(ResultUtil.successResult("取消收藏成功"),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("您可能还没有收藏这个回答"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/dz/{aid}")
    public ResponseEntity<Result> cancelThuumbsUp(@PathVariable("aid") int aid,
                                                  HttpServletRequest request){
        try {
            Integer uid=(Integer)request.getAttribute("uid");
            answerService.deleteThumbsUp(uid,aid);
            return new ResponseEntity<>(ResultUtil.successResult("取消点赞成功"),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("您可能还没有给这个回答点赞"),HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }
}
