package com.cll.wenda.service.impl;

import com.cll.wenda.mapper.BannerMapper;
import com.cll.wenda.model.Banner;
import com.cll.wenda.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/11/26
 */
@Service
public class BannerServiceImpl implements BannerService {


    private BannerMapper bannerMapper;

    @Autowired
    protected BannerServiceImpl(BannerMapper bannerMapper){
        this.bannerMapper=bannerMapper;
    }

    @Override
    public void saveBanner(Banner banner) {
        bannerMapper.save(banner);
    }

    @Override
    public List<Banner> listNewestBanner() {
        return bannerMapper.listNewestBanner();
    }

    @Override
    public void removeBanner(int bid) throws RuntimeException {
        int res=bannerMapper.deleteById(bid);
        if (res!=1){
            throw new RuntimeException("该轮播可能不存在了");
        }

    }
}
