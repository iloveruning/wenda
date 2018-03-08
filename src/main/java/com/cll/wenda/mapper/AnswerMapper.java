package com.cll.wenda.mapper;

import com.cll.wenda.model.Answer;
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
public interface AnswerMapper {


    /**
     * 保存回答
     *
     * @param answer
     */
    int save(Answer answer);

    /**
     * 按ID查找回答
     *
     * @param id
     */
    Answer findById(@Param("id") int id);


    /**
     * 用户收藏回答
     * @param uid
     * @param aid
     * @return
     */
    int saveCollection(@Param("uid")int uid,@Param("aid")int aid);


    /**
     * 更新评论数
     * @param aid
     * @param number
     * @return
     */
    int updateComment(@Param("aid")int aid,@Param("number") int number);


    /**
     * 保存用户的点赞
     * @param uid
     * @param aid
     * @return
     */
    int saveThumbsUp(@Param("uid") int uid, @Param("aid") int aid);



    /**
     * 更新点赞数加一
     *
     * @param id
     * @return int
     */
    int updateDz(@Param("id")int id,@Param("number")int number);


    /**
     * 删除回答
     *
     * @param id
     * @return int
     */
    int deleteById(@Param("id")int id);


    /**
     * 获取问题对应的回答
     *
     * @param id
     * @return List<Answer>
     */
    List<Answer> findAnswersByQuestionId(@Param("id") int id);



    /**
     * 获取用户关注的回答
     *
     * @param uid
     * @return List<Answer>
     */
    List<Answer> findCollectAnswersByUserId(@Param("uid") int uid);


    /**
     * 获取我的回答
     *
     * @param uid
     * @return List<Answer>
     */
    List<Answer> findMyAnswers(@Param("uid") int uid);

    /**
     * 取消点赞
     * @param uid
     * @param aid
     * @return int
     */
    int removeThumbsUp(@Param("uid") int uid, @Param("aid") int aid);

    /**
     * 统计用户uid是否点赞了这个回答
     * @param uid
     * @param aid
     * @return
     */
    int countThumbsUp(@Param("uid") int uid, @Param("aid") int aid);

    /**
     * 获取问题QID的最热回答
     * @param qid
     * @return Answer
     */
    Answer findQuestionHotAnswer(@Param("qid") int qid);

    /**
     * 更新回答的收藏数
     * @param aid
     * @param number
     * @return
     */
    int updateCollection(@Param("aid") int aid,@Param("number") int number);

    /**
     * 统计用户uid是否关注了这个回答
     * @param uid
     * @param aid
     * @return
     */
    int countCollection(@Param("uid") int uid,@Param("aid") int aid);

    /**
     * 取消收藏
     * @param uid
     * @param aid
     * @return
     */
    int removeCollection(@Param("uid") int uid,@Param("aid") int aid);

    /**
     * 删除我的回答
     * @param uid
     * @param aid
     * @return
     */
    int deleteMyAnswer(@Param("uid") int uid,@Param("aid") int aid);


    int deleteCollectionByAid(@Param("aid") int aid);

    int deleteThumbsUpByAid(@Param("aid") int aid);
}
