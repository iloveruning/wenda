package com.cll.wenda.mapper;

import com.cll.wenda.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/10/26
 */
@Component
@Mapper
public interface QuestionMapper {

    int save(Question question);

    Question findById(@Param("id") int id);

    /**
     * 跟新关注数
     * @param qid
     * @param number
     * @return
     */
    int updateAtt(@Param("qid") int qid,@Param("number") int number);

    /**
     * 关注问题
     * @param uid
     * @param qid
     * @return
     */
    int attention(@Param("uid") int uid,@Param("qid") int qid);

    /**
     * 根据id删除问题
     * @param id
     * @return
     */
    int deleteById(@Param("id") int id);

    /**
     * 查询用户关注的问题
     * @param uid
     * @return
     */
    List<Question> findAttQuestionByUserId(@Param("uid") int uid);

    List<Question> findMyQuestions(@Param("uid")int uid);

    /**
     * 跟新回答数
     * @param qid
     * @param number
     * @return
     */
    int updateAns(@Param("qid") int qid,@Param("number") int number);

    int increaseReadSize(@Param("qid") int qid);

    /**
     * 热门问题
     *
     * @return List<Question>
     */
    List<Question> findHotestQuestion();

    /**
     * 热门话题
     * @return
     */
    List<Question> findHotTopic();



    /**
     * 是否关注这个问题
     *
     * @param uid
     * @param qid
     * @return
     */
    int isAttentted(@Param("uid") int uid, @Param("qid") int qid);

    /**
     * 取消关注
     * @param uid
     * @param qid
     * @return
     */
    int deleteAtt(@Param("uid") int uid, @Param("qid") int qid);

    /**
     *
     * @param topic
     * @return
     */
    List<Question> findByTopic(@Param("topic") String topic);

    List<String> searchTopic(@Param("topic") String topic);




}
