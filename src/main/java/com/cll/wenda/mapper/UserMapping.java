package com.cll.wenda.mapper;

import com.cll.wenda.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author chenliangliang
 * @date: 2017/11/18
 */
@Mapper
@Component
public interface UserMapping {


    int save(User user);

    User findByOpenid(@Param("openid") String openid);

    User findByUUId(@Param("uuid") String uuid);

    String findHeadImgByUid(@Param("uid") int uid);
}
