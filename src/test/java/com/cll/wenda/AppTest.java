package com.cll.wenda;

import com.cll.wenda.mapper.UserMapping;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chenliangliang
 * @date: 2017/11/27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
public class AppTest {

    @Autowired
    private UserMapping userMapping;

    @Test
    public void test(){
        System.out.println("hdhyfri");
        String img=userMapping.findHeadImgByUid(4);
        System.out.println(img);
    }


}
