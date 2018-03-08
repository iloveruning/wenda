package com.cll.wenda.controller;

import com.cll.wenda.model.Report;
import com.cll.wenda.model.Result;
import com.cll.wenda.service.ReportService;
import com.cll.wenda.utils.ResultUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author chenliangliang
 * @date: 2017/10/27
 */

@RestController
@RequestMapping(value = "/report")
public class ReportController {


    private ReportService reportService;

    @Autowired
    protected ReportController(ReportService reportService){
        this.reportService=reportService;
    }


    @PostMapping
    public ResponseEntity<Result> report(@RequestBody @Valid Report report,
                                         HttpServletRequest request,
                                         BindingResult result) {
        if (result.hasErrors()){
            return new ResponseEntity<>(ResultUtil.failResult(result.getAllErrors().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Integer uid = (Integer) request.getAttribute("uid");
            report.setPuber(uid);
            reportService.report(report);
            System.out.println(report);
            return new ResponseEntity<>(ResultUtil.successResult("举报成功", null), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(ResultUtil.failResult(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{pageNo}/{pageSize}")
    public ResponseEntity<Result> getReport(@PathVariable(value = "pageNo") int pageNo,
                                            @PathVariable(value = "pageSize",required = false) int pageSize){

        if (pageSize<=0){
            pageSize=4;
        }
        PageInfo<Report> reportPageInfo=reportService.getAllReport(pageNo,pageSize);
        return new ResponseEntity<Result>(ResultUtil.successResult(reportPageInfo),HttpStatus.OK);

    }
}
