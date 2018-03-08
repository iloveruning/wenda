package com.cll.wenda.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author chenliangliang
 * @date: 2017/10/26
 */
public interface BaseMapper<T> extends Mapper<T>,MySqlMapper<T> {
}
