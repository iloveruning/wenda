package com.cll.wenda.controller;

import com.cll.wenda.model.Comment;
import com.cll.wenda.model.Result;
import com.cll.wenda.service.CommentService;
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
@RequestMapping(value = "/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 发表评论
     * @param comment
     * @return
     */
    @PostMapping
    public ResponseEntity<Result> pubComment(@RequestBody @Valid Comment comment,
                                             HttpServletRequest request,
                                             BindingResult result) {
        if (result.hasErrors()){
            return new ResponseEntity<>(ResultUtil.failResult(result.getAllErrors().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Integer uid = (Integer) request.getAttribute("uid");
            String name = (String) request.getAttribute("name");
            comment.setPuber(uid);
            comment.setAuthor(name);
            commentService.pubComment(comment,uid);
            return new ResponseEntity<>(ResultUtil.successResult("发表评论成功", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/dz")
    public ResponseEntity<Result> commentThumbsUp(@RequestBody Map<String,Object> map,
                                                  HttpServletRequest request){
        try {
            Integer uid=(Integer)request.getAttribute("uid");
            Integer cid=(Integer)map.get("cid");
            commentService.thumbsUp(uid,cid);
            return new ResponseEntity<>(ResultUtil.successResult("点赞成功", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{cid}")
    public ResponseEntity<Result> readComment(@PathVariable("cid") int cid,
                                              HttpServletRequest request){
        try {
            Integer uid=(Integer)request.getAttribute("uid");
            Comment comment=commentService.readComment(uid,cid);
            return new ResponseEntity<>(ResultUtil.successResult( comment), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("该评论可能不存在了"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/my/{pageNo}")
    public ResponseEntity<Result> getMyComments(@PathVariable(value = "pageNo",required = false) int pageNo,
                                                HttpServletRequest request){
        try {
            Integer uid=(Integer)request.getAttribute("uid");
           if (pageNo<=0){
               pageNo=1;
           }
            PageInfo<Comment> commentPageInfo=commentService.getMyComment(uid,pageNo,12);
            return new ResponseEntity<>(ResultUtil.successResult( commentPageInfo), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("获取异常"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{cid}")
    public ResponseEntity<Result> removeComment(@PathVariable("cid") int cid, HttpServletRequest request){
        try {
            Integer uid=(Integer)request.getAttribute("uid");
            commentService.deleteComment(uid,cid);
            return new ResponseEntity<>(ResultUtil.successResult( null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("该评论已被删除或者您无权限删除"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/dz/{cid}")
    public ResponseEntity<Result> cancelThumbsUp(@PathVariable("cid") int cid,
                                                 HttpServletRequest request){

        try {
            Integer uid=(Integer)request.getAttribute("uid");
            commentService.cancelThumsbUp(uid,cid);
            return new ResponseEntity<>(ResultUtil.successResult( null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("您还没有给该评论点赞呢"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ans/{aid}/{pageNo}")
    public ResponseEntity<Result> getAnswerComments(@PathVariable("aid") int aid,
                                                    @PathVariable(value = "pageNo",required = false) int pageNo,
                                                    HttpServletRequest request){
        if (pageNo<=0){
            pageNo=1;
        }
        try {
            Integer uid=(Integer)request.getAttribute("uid");
            PageInfo<Comment> pageInfo=commentService.getCommentByAid(uid,aid,pageNo,12);
            return new ResponseEntity<>(ResultUtil.successResult(pageInfo),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("该回答可能不存在了"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
