package com.qlqs.blogspringboot.controller;


import com.qlqs.blogspringboot.entity.*;
import com.qlqs.blogspringboot.service.BlogDetailService;
import com.qlqs.blogspringboot.service.OtherInformationService;
import com.qlqs.blogspringboot.service.UserPageService;

import com.qlqs.blogspringboot.utils.NewDateUtil;
import com.qlqs.blogspringboot.vo.ResultVo;
import com.qlqs.blogspringboot.vo.UserAccountVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class UserPageController {
    @Autowired
    private UserPageService userPageService;
    @Autowired
    private OtherInformationService otherInformationService;
    @Autowired
    private BlogDetailService blogDetailService;

    @RequestMapping("login")
    public @ResponseBody int login(HttpServletResponse response, @RequestBody UserAccountVo userAccountVo) throws UnsupportedEncodingException {
        int loginCode = userPageService.login(userAccountVo);
        if (loginCode == 1){
            UserDetail userDetail = otherInformationService.findUserDetailByUserId(userAccountVo.getUserId());
            Cookie cookie1 = new Cookie("userId",userDetail.getUserId());
            Cookie cookie2 = new Cookie("userName",URLEncoder.encode(userDetail.getUserName(),"UTF-8"));
            Cookie cookie3 = new Cookie("userAvatar", userDetail.getUserAvatar());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            if (userAccountVo.isRememberMe()){
                cookie1.setMaxAge(30*24*60*60);
                cookie2.setMaxAge(30*24*60*60);
                cookie3.setMaxAge(30*24*60*60);
                Cookie cookie4 = new Cookie("expire", simpleDateFormat.format(NewDateUtil.addDayOfDate(new Date(),30)));
                cookie4.setMaxAge(30*24*60*60);
                response.addCookie(cookie4);
            }
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            response.addCookie(cookie3);
        }
        return loginCode;
    }
    @RequestMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/page/login";
    }
    @RequestMapping("user/insertNewUserAccount")
    public @ResponseBody ResultVo insertNewUserAccount(@RequestBody UserAccountVo userAccountVo){
        return userPageService.insertNewUserAccount(userAccountVo);
    }
    @RequestMapping("user/background")
    public String toBackground(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        request.setAttribute("path","index");
        Map<String, Long> map = userPageService.fingAllTypeAmount((String) subject.getPrincipal());
        request.setAttribute("allTypeAmount",map);
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogbackground/background";
    }
    @RequestMapping("/user/blogsEdit")
    public String toBlogsEdit(HttpServletRequest request){
        request.setAttribute("path","edit");
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        request.setAttribute("categories",otherInformationService.findAllCategoryDetails());
        return "blogbackground/edit";
    }
    @RequestMapping("/user/blogsEdit/{blogid}")
    public String toBlogsEdit(HttpServletRequest request, @PathVariable("blogid")long blogid){
        request.setAttribute("path","edit");
        BlogDetail blogDetail = blogDetailService.findBlogDetailByBlogId(blogid);
        List<TagDetail> tagDetails = new ArrayList<>();
        request.setAttribute("blog",blogDetail);
        request.setAttribute("categories",otherInformationService.findAllCategoryDetails());
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogbackground/edit";
    }
    @RequestMapping("/user/blogsManage")
    public String toBlogsManage(HttpServletRequest request){
        request.setAttribute("path","blogs");
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogbackground/blog";
    }

    @RequestMapping("user/comments")
    public String toComments(HttpServletRequest request){
        request.setAttribute("path","comments");
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        request.setAttribute("blogs",blogDetailService.findBlogDetailByUserId((String) SecurityUtils.getSubject().getPrincipal()));
        return "blogbackground/comment";
    }
    @RequestMapping("/user/categories")
    public String toCategories(HttpServletRequest request){
        request.setAttribute("path","categories");
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogbackground/category";
    }
    @RequestMapping("/user/tags")
    public String toTags(HttpServletRequest request){
        request.setAttribute("path","tags");
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogbackground/tag";
    }
    @RequestMapping("/user/links")
    public String toLinks(HttpServletRequest request){
        request.setAttribute("path","links");
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogbackground/link";
    }
    @RequestMapping("/user/allLinks")
    public @ResponseBody ResultVo allLinksFind(HttpServletRequest request){
        int limit = Integer.parseInt(request.getParameter("limit"));
        int pagenum = Integer.parseInt(request.getParameter("page"));
        return otherInformationService.allLinksFind(limit, pagenum);
    }
    @RequestMapping("/user/insertNewLink")
    public @ResponseBody ResultVo insertNewLink(@RequestBody LinkDetail linkDetail){
        return otherInformationService.insertNewLink(linkDetail);
    }
    @RequestMapping("/user/updateOldLink")
    public @ResponseBody ResultVo updateOldLink(@RequestBody LinkDetail linkDetail){
        return otherInformationService.updateOldLink(linkDetail);
    }
    @RequestMapping("/user/deleteOldLink")
    public @ResponseBody ResultVo deleteOldLink(@RequestBody List<Long> linkIds){
        return otherInformationService.deleteOldLink(linkIds);
    }
    @RequestMapping("/user/profile")
    public String toProfile(HttpServletRequest request){
        request.setAttribute("path","profile");
        request.setAttribute("userDetail",userPageService.findUserDetailByUserId((String) SecurityUtils.getSubject().getPrincipal()));
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogbackground/profile";
    }

    @RequestMapping("/user/updateUserAvatar")
    public @ResponseBody
    ResultVo updateUserAvatar(@RequestParam(value = "userAvatar-image-file",required = false)MultipartFile imageFile, HttpServletRequest request){
        return userPageService.updateUserAvatar(imageFile, request.getRequestURL().toString());
    }

    @RequestMapping("/user/updateUserPassword")
    public @ResponseBody ResultVo updateUserPassword(HttpServletRequest request){
        return userPageService.updateUserPassword(request.getParameter("oldPassword"),request.getParameter("newPassword"));
    }

    @RequestMapping("/user/updateUserMessage")
    public @ResponseBody ResultVo updateUserMessage(@RequestBody UserDetail userDetail){
        return userPageService.updateUserMessage(userDetail);
    }
    @RequestMapping("/user/configurations")
    public String toConfigurations(HttpServletRequest request){
        request.setAttribute("path","configurations");
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogbackground/configuration";
    }
    @RequestMapping("/user/updateWebIcon")
    public @ResponseBody
    ResultVo updateWebIcon(@RequestParam(value = "webIcon-image-file",required = false)MultipartFile imageFile, HttpServletRequest request){
        return otherInformationService.updateWebIcon(imageFile, request.getRequestURL().toString());
    }
    @RequestMapping("/user/updateWebMessage")
    public @ResponseBody
    ResultVo updateWebMessage(@RequestBody ConfigurationDetail configurationDetail){
        return otherInformationService.updateWebMessage(configurationDetail);
    }
    @RequestMapping("/user/updateUserInfo")
    public @ResponseBody
    ResultVo updateUserInfo(@RequestBody ConfigurationDetail configurationDetail){
        return otherInformationService.updateUserInfo(configurationDetail);
    }
}
