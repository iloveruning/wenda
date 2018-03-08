package com.cll.wenda.service;

import com.cll.wenda.model.Reply;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/10/29
 */
public interface ReplyService {

    void pubReply(Reply reply,int uid) throws RuntimeException;

    List<Reply> getCommentReply(int cid);
}
