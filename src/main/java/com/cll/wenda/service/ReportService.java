package com.cll.wenda.service;

import com.cll.wenda.model.Report;
import com.github.pagehelper.PageInfo;

/**
 * @author chenliangliang
 * @date: 2017/10/27
 */
public interface ReportService {

    void report(Report report) throws RuntimeException;

    PageInfo<Report> getAllReport(int pageNo,int PageSize);
}
