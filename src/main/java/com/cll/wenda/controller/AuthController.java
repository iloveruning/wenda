package com.cll.wenda.controller;

import com.alibaba.fastjson.JSONObject;
import com.cll.wenda.model.Result;
import com.cll.wenda.model.User;
import com.cll.wenda.service.UserService;
import com.cll.wenda.utils.Coder;
import com.cll.wenda.utils.HttpUtil;
import com.cll.wenda.utils.JwtUtil;
import com.cll.wenda.utils.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author chenliangliang
 * @date: 2017/11/18
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/token")
    public ResponseEntity<Result> createAuthenticationToken(@RequestBody Map<String,Object> map){
        try {
            String code=(String)map.get("code");
            System.out.println("code:  "+code);
            String url="https://api.weixin.qq.com/sns/jscode2session" +
                    "?appid="+ HttpUtil.APPID+
                    "&secret=" +HttpUtil.APPSECRET+
                    "&js_code=" +code+
                    "&grant_type=authorization_code";
            String res= HttpUtil.doGet(url);
            if (res!=null) {
                JSONObject jsonObject = JSONObject.parseObject(res);
                String openid=jsonObject.getString("openid");
                User user=userService.getUserByOpenid(openid);
                if (user!=null&&StringUtils.isNotEmpty(user.getUuid())){
                    String userRes= JwtUtil.generateToken(user);
                    System.out.println("AuthController: "+res);
                    return new ResponseEntity<>(ResultUtil.successResult("身份认证成功",userRes),HttpStatus.OK);
                }else {
                    return new ResponseEntity<>(ResultUtil.failResult(Coder.AESEncode(openid)), HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }else {
                return new ResponseEntity<>(ResultUtil.failResult(null), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping("/register")
    public ResponseEntity<Result> register(@RequestBody @Valid User addedUser,
                                           BindingResult result) {
        System.out.println(addedUser);
        if (result.hasErrors()){
            return new ResponseEntity<>(ResultUtil.failResult(result.getAllErrors().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            String code=addedUser.getCode();
            System.out.println("code: "+code);
            String openid=Coder.AESDecode(code);
            if (openid!=null){
                addedUser.setOpenid(openid);
                System.out.println(addedUser);
                userService.addUser(addedUser);
                return new ResponseEntity<>(ResultUtil.successResult("OK"), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(ResultUtil.failResult(""), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(result.getAllErrors().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
