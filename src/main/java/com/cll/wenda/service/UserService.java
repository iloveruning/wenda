package com.cll.wenda.service;

import com.cll.wenda.model.User;

/**
 * @author chenliangliang
 * @date: 2017/11/18
 */
public interface UserService {


    int addUser(User user);

    User getUserByOpenid(String openid);

    User getUserByUUId(String uuid);


}
