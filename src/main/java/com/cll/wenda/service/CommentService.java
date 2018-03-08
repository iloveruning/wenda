package com.cll.wenda.service;

import com.cll.wenda.model.Comment;
import com.github.pagehelper.PageInfo;

/**
 * @author chenliangliang
 * @date: 2017/10/26
 */
public interface CommentService {

    void pubComment(Comment comment,int uid) throws RuntimeException;

    /**
     *
     * @param uid
     * @param cid
     * @throws RuntimeException
     */
    void thumbsUp(int uid,int cid) throws RuntimeException;


    Comment readComment(int uid,int cid);

    /**
     * 删除评论
     * @param cid
     * @throws RuntimeException
     */
    void deleteComment(int uid,int cid) throws RuntimeException;

    /**
     * 获取我的评论
     * @param uid
     * @return
     */
    PageInfo<Comment> getMyComment(int uid,int pageNo,int pageSize);

    /**
     * 取消点赞
     * @param uid
     * @param cid
     * @throws RuntimeException
     */
    void cancelThumsbUp(int uid,int cid) throws RuntimeException;


    PageInfo<Comment> getCommentByAid(int uid,int aid,int pageNo,int pageSize);
}
