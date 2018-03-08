package com.cll.wenda.mapper;

import com.cll.wenda.model.Comment;
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
public interface CommentMapper {


    /**
     * 用户针对某个回答发表评论
     *
     * @param comment
     */
    int save(Comment comment);


    /**
     *
     * @param id
     * @return
     */
    Comment findById(@Param("id") int id);

    /**
     * 跟新评论点赞数
     * @param id
     * @param number
     * @return
     */
    int updateDz(@Param("id") int id,@Param("number") int number);


    /**
     * 删除评论
     *
     * @param id
     * @return int
     */
    int deleteById(@Param("id")int id);

    /**
     * 评论点赞
     * @param uid
     * @param cid
     * @return
     */
    int commentThumbsUp(@Param("uid") int uid,@Param("cid") int cid);

    /**
     * 取消点赞
     * @param uid
     * @param cid
     * @return
     */
    int deleteThumbsUp(@Param("uid") int uid,@Param("cid") int cid);

    /**
     * 跟新回复数
     * @param uid
     * @param number
     * @return
     */
    int updateReply(@Param("cid") int uid,@Param("number") int number);


    /**
     * 根据作者查询评论
     * @param uid
     * @return
     */
    List<Comment> findByPuber(@Param("uid") int uid);


    /**
     * 根据回答查询评论
     * @param aid
     * @return
     */
    List<Comment> findByAnswerId(@Param("aid") int aid);

    /**
     * 查询回答的最热评论
     * @param aid
     * @return
     */
    Comment findHotComment(@Param("aid") int aid);

    /**
     * 统计用户是否点赞
     * @param uid
     * @param cid
     * @return
     */
    int countThumbsUp(@Param("uid") int uid,@Param("cid") int cid);


    int deleteByAid(@Param("aid") int aid);




}
