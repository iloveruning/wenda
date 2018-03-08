package com.cll.wenda.mapper;

import com.cll.wenda.model.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/11/26
 */
@Mapper
@Component
public interface BannerMapper {

    int save(Banner banner);

    List<Banner> listNewestBanner();


    int deleteById(@Param("bid") int bid);

}
