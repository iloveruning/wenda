package com.cll.wenda.service;

import com.cll.wenda.model.Answer;
import com.github.pagehelper.PageInfo;

/**
 * @author chenliangliang
 * @date: 2017/10/27
 */
public interface AnswerService {


    /**
     *
     * @param answer
     * @param uid
     * @throws RuntimeException
     */
    void pubAnswer(Answer answer,int uid) throws RuntimeException;

    void thumbsUp(int uid,int aid) throws RuntimeException;

    PageInfo<Answer> getQuestionAnswers(int qid,int pageNum) throws RuntimeException;


    PageInfo<Answer> getCollectAnswers(int uid, int pageNum) throws RuntimeException;

    /**
     * 收藏回答
     * @param uid
     * @param aid
     * @throws RuntimeException
     */
    void collectAnswer(int uid,int aid) throws RuntimeException;

    PageInfo<Answer> getMyAnswers(int uid, int pageNum);

    void deleteAnswer(int aid) throws RuntimeException;

    /**
     * 取消点赞
     * @param uid
     * @param aid
     * @throws RuntimeException
     */
    void deleteThumbsUp(int uid,int aid) throws RuntimeException;

    /**
     * 按id查询回答
     * @param aid
     * @return
     */
    Answer getAnswerById(int aid);

    /**
     * 用户uid阅读回答aid
     * @param uid
     * @param aid
     * @return
     */
    Answer readAnswer(int uid,int aid);

    /**
     * 取消收藏
     * @param uid
     * @param aid
     * @throws RuntimeException
     */
    void cancelCollection(int uid,int aid) throws RuntimeException;

    void removeMyAnswer(int uid,int aid) throws RuntimeException;
}
