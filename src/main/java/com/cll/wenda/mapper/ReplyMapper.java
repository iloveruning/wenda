package com.cll.wenda.mapper;

import com.cll.wenda.model.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/10/29
 */
@Mapper
@Component
public interface ReplyMapper {

    int save(Reply reply);


    List<Reply> findReplyByCommentId(@Param("cid") int cid);

    int deleteByCommentId(@Param("cid") int cid);

}
