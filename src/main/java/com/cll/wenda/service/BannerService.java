package com.cll.wenda.service;

import com.cll.wenda.model.Banner;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/11/26
 */
public interface BannerService {

    void saveBanner(Banner banner);

    List<Banner> listNewestBanner();

    void removeBanner(int bid) throws RuntimeException;
}
