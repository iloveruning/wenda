package com.cll.wenda.service.impl;

import com.cll.wenda.mapper.ReportMapper;
import com.cll.wenda.model.Report;
import com.cll.wenda.service.ReportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/10/27
 */
@Service
public class ReportServiceImpl implements ReportService {


    private ReportMapper reportMapper;

    @Autowired
    protected ReportServiceImpl(ReportMapper reportMapper){
        this.reportMapper=reportMapper;

    }



    @Override
    public void report(Report report) throws RuntimeException {
        int res=reportMapper.save(report);

        if (res!=1){
            throw new RuntimeException("保存举报内容失败");
        }
    }

    @Override
    public PageInfo<Report> getAllReport(int pageNo,int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<Report> reports=reportMapper.listAllReport();
        return new PageInfo<>(reports);
    }
}
