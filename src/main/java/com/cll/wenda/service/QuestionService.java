package com.cll.wenda.service;

import com.cll.wenda.model.Question;
import com.github.pagehelper.PageInfo;

/**
 * @author chenliangliang
 * @date: 2017/10/26
 */
public interface QuestionService {

    /**
     * 用户关注问题
     * @param uid
     * @param qid
     * @throws RuntimeException
     */
    void attention(int uid,int qid) throws RuntimeException;

    PageInfo<Question> getAttQuestion(int id,int pageNo,int pageSize);


    PageInfo<Question> getMyQuestion(int uid,int pageNo,int pageSize);

    Question readQuestion(int uid,int qid);

    void saveQuestion(Question question,int uid);

    PageInfo<Question> getHotQuestion(int uid,int pageNum, int pageSize);

    /**
     * 取消关注
     * @param uid
     * @param qid
     * @throws RuntimeException
     */
    void cancelAttention(int uid,int qid) throws RuntimeException;

    /**
     * 获取热门话题
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Question> getHotTopic(int pageNum, int pageSize);


    /**
     * 获取话题对应的问题
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Question> getTopicQuestion(String topic,int uid,int pageNum, int pageSize);


    void removeMyQuestion(int uid,int qid) throws RuntimeException;

}
