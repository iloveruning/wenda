package com.cll.wenda.controller;

import com.cll.wenda.model.Result;
import com.cll.wenda.utils.ResultUtil;
import com.cll.wenda.utils.StringRandom;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2017/11/11
 */
@RestController
@RequestMapping("/pic")
public class PictureController {

    @GetMapping
    public String upload(){
        return "upload。。。";
    }


    @Value("${pic.location}")
    private String fileDest;
    @Value("${net.path}")
    private String netPath;


    @ApiOperation(value = "单图片上传")
    @PostMapping(value = "/upload")
    public ResponseEntity<Result> savePic( @RequestParam("file") MultipartFile file){

        if (file.isEmpty()){
            return new ResponseEntity<>(ResultUtil.failResult("上传的图片不那为空"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String type=file.getContentType();
        String url;
        if (type.contains("image")){
            String originalFilename=file.getOriginalFilename();
            System.out.println("filename--->"+originalFilename);
            String[] strs=StringUtils.split(originalFilename,".");
            try(InputStream in=file.getInputStream()){
                String filename= StringRandom.random(20)+"."+strs[1];
                url=netPath+filename;
                Thumbnails.of(in).scale(1.0).outputQuality(0.25f).toFile(new File(fileDest+filename));
            }catch (Exception e){
                e.printStackTrace();return new ResponseEntity<>(ResultUtil.failResult("保存图片失败"),HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }else {
            return new ResponseEntity<>(ResultUtil.failResult("上传的不是图片类型"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(ResultUtil.successResult("上传成功",url), HttpStatus.OK);
    }


    /**
     * 多图片上传
     * @param request
     * @return
     */
   @PostMapping(value = "/upload/batch")
    public ResponseEntity<Result> savePic(HttpServletRequest request){

        List<MultipartFile> files=((MultipartHttpServletRequest)request).getFiles("file");
        List<String> picUrls=new ArrayList<>(files.size());
        MultipartFile file;
        for (int i=0;i<files.size();i++){
            file=files.get(i);
            if (!file.isEmpty()){
                String url;
                String type=file.getContentType();
                if (type.contains("image")){
                    String originalFilename=file.getOriginalFilename();
                    System.out.println("filename--->"+originalFilename);
                    String[] strs=StringUtils.split(originalFilename,".");
                    try(InputStream in=file.getInputStream()){
                        String filename= StringRandom.random(20)+"."+strs[1];
                        url=netPath+filename;
                        Thumbnails.of(in).scale(1.0).outputQuality(0.25f).toFile(new File(fileDest+filename));
                    }catch (Exception e){
                        url="failed";
                        e.printStackTrace();
                    }
                    picUrls.add(url);
                }
            }
        }
        return new ResponseEntity<>(ResultUtil.successResult(picUrls), HttpStatus.OK);
    }



}
