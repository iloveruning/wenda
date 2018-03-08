package com.cll.wenda.service.impl;

import com.cll.wenda.mapper.UserMapping;
import com.cll.wenda.model.User;
import com.cll.wenda.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author chenliangliang
 * @date: 2017/11/18
 */
@Service
public class UserServiceImpl implements UserService{


    @Autowired
    private UserMapping userMapping;


    @Override
    public int addUser(User user) {
        user.setUuid(UUID.randomUUID().toString());
        String username=user.getUsername();
        if (username==null|| StringUtils.isEmpty(username)){
            username=user.getWxname();
            user.setUsername(username);
        }
        return userMapping.save(user);
    }

    @Override
    public User getUserByOpenid(String openid) {
        return userMapping.findByOpenid(openid);
    }

    @Override
    public User getUserByUUId(String uuid) {
        return userMapping.findByUUId(uuid);
    }
}
