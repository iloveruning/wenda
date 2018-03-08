package com.cll.wenda.controller;

import com.cll.wenda.model.Reply;
import com.cll.wenda.model.Result;
import com.cll.wenda.service.ReplyService;
import com.cll.wenda.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/10/29
 */

@RestController
@RequestMapping(value = "/reply")
public class ReplyController {


    private ReplyService replyService;

    @Autowired
    protected ReplyController(ReplyService replyService){
        this.replyService=replyService;
    }


    @PostMapping
    public ResponseEntity<Result> pubReply(@RequestBody @Valid Reply reply,
                                           HttpServletRequest request,
                                           BindingResult result) {
        if (result.hasErrors()){
            return new ResponseEntity<>(ResultUtil.failResult(result.getAllErrors().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            Integer uid = (Integer) request.getAttribute("uid");
            String name = (String) request.getAttribute("name");
            reply.setPuber(uid);
            reply.setAuthor(name);
            replyService.pubReply(reply,uid);
            return new ResponseEntity<>(ResultUtil.successResult("发表回复成功", reply), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult("回复失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{cid}")
    public ResponseEntity<Result> getCommentReplies(@PathVariable("cid") int cid) {
        List<Reply> replies=replyService.getCommentReply(cid);
        return new ResponseEntity<>(ResultUtil.successResult(replies),HttpStatus.OK);
    }
}
