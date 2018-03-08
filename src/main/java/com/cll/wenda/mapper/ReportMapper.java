package com.cll.wenda.mapper;

import com.cll.wenda.model.Report;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/10/27
 */
@Mapper
@Component
public interface ReportMapper {

    int save(Report report);

    List<Report> listAllReport();
}
