package com.cll.wenda.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/11/19
 */
@Mapper
@Component
public interface KeywordMapper {

    int batchSave(List<String> keywords);

    int save(@Param("keyword") String keyword);

    List<String> blurQuery(@Param("kwd") String kwd);

    List<String> findHotKeywords();

    int updateKeywordCounts(@Param("kwd") String kwd);

    int isExist(@Param("kwd") String kwd);
}
