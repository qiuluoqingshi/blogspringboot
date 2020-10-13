package com.qlqs.blogspringboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.qlqs.blogspringboot.config.SystemConfig;
import com.qlqs.blogspringboot.entity.BlogDetail;
import com.qlqs.blogspringboot.service.BlogDetailService;
import com.qlqs.blogspringboot.service.OtherInformationService;
import com.qlqs.blogspringboot.utils.UrlUtil;
import com.qlqs.blogspringboot.vo.AllDetailVo;
import com.qlqs.blogspringboot.vo.BlogDetailVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class BlogController {
    @Autowired
    private BlogDetailService blogDetailService;

    @Autowired
    private OtherInformationService otherInformationService;

    @RequestMapping("blog/{blogid}")
    public String BlogDetailByBlogId(HttpServletRequest request, @PathVariable("blogid") long blogid){
        return this.BlogDetailByBlogId(request,blogid,1);
    }
    @RequestMapping("blog/{blogid}/{pagenum}")
    public String BlogDetailByBlogId(HttpServletRequest request, @PathVariable("blogid") long blogid,@PathVariable("pagenum")int pagenum){
        BlogDetailVo blogDetailVo = blogDetailService.findBlogDetailByBlogId(blogid,pagenum);
        if (blogDetailVo == null) {
            return "error/404";
        }
        request.setAttribute("blogDetailVo",blogDetailVo);
        request.setAttribute("blogid",blogid);
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogindex/detail";
    }

    @RequestMapping("user/newBlogInsert")
    public @ResponseBody int newBlogInsert(@RequestBody BlogDetail blogDetail){
        return blogDetailService.newBlogInsert(blogDetail);
    }
    @RequestMapping("user/oldBlogUpdate")
    public @ResponseBody int oldBlogUpdate(@RequestBody BlogDetail blogDetail){
        return blogDetailService.oldBlogUpdate(blogDetail);
    }
    @RequestMapping("/user/oldBlogsDelete")
    public @ResponseBody int oldBlogsDelete(@RequestBody String[] blogDetailId){
        List<Long> blogDetailsId = new ArrayList<>();
        for (String blogId:blogDetailId){
            blogDetailsId.add(Long.parseLong(blogId));
        }
        return blogDetailService.oldBlogsDelete(blogDetailsId);
    }
    @RequestMapping("/user/blogsImageUpload")
    public @ResponseBody JSONObject blogsImageUpload(@RequestParam(value = "editormd-image-file",required = false) MultipartFile imageFile, HttpServletRequest request) throws URISyntaxException, FileNotFoundException {

        JSONObject jsonObject = new JSONObject();
        //获取上传文件名称
        String fileName = imageFile.getOriginalFilename();
        //获取上传文件后缀
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //重新生成随机文件名称，避免图片发生冲突
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        StringBuilder builder = new StringBuilder();
        builder.append(dateFormat.format(new Date())).append("_"+(String) SecurityUtils.getSubject().getPrincipal()).append(suffixName);
        String newFileName = builder.toString();
        File destFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\"+ SystemConfig.uploadPath+newFileName);
        String fileUrl = UrlUtil.getHost(new URI(request.getRequestURL()+"")) + "/imageUpload/" + newFileName;
        try {
            if(!destFile.getParentFile().exists()){
                if(!destFile.getParentFile().mkdirs()){
                    throw new IOException("文件夹创建失败,路径为：" + destFile.getParentFile());
                }
            }
            imageFile.transferTo(destFile);
            jsonObject.put("success",1);
            jsonObject.put("url",fileUrl);
            jsonObject.put("message","图片上传成功");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            jsonObject.put("success",0);
        } catch (IOException e) {
            e.printStackTrace();
            jsonObject.put("success",0);
        }
        return jsonObject;
    }

    @RequestMapping("user/allBlogDetail")
    public @ResponseBody
    AllDetailVo allBlogDetailFind(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        int pagenum = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        String keyword = "";
        if (request.getParameter("keyword")!=null)
        {
            keyword = request.getParameter("keyword");
        }else {
            keyword = null;
        }
        String userId = (String) subject.getPrincipal();;
        return blogDetailService.findAllBlogDetail(pagenum,limit,keyword,userId);
    }

}

