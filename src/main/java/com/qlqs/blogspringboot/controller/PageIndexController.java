package com.qlqs.blogspringboot.controller;

import com.qlqs.blogspringboot.config.SystemConfig;
import com.qlqs.blogspringboot.entity.BlogDetail;
import com.qlqs.blogspringboot.entity.LinkDetail;
import com.qlqs.blogspringboot.entity.TagDetail;
import com.qlqs.blogspringboot.service.BlogDetailService;
import com.qlqs.blogspringboot.service.OtherInformationService;
import com.qlqs.blogspringboot.service.UserPageService;
import com.qlqs.blogspringboot.utils.UrlUtil;
import com.qlqs.blogspringboot.utils.VerifyCodeUtils;
import com.qlqs.blogspringboot.vo.PageIndexVo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Controller
public class PageIndexController {
    @Autowired
    private BlogDetailService blogDetailService;
    @Autowired
    private OtherInformationService otherInformationService;
    @Autowired
    private UserPageService userPageService;
    //默认首页请求获取所有blog数据
    @RequestMapping({"/","/page/index"})
    public String defaultPage(HttpServletRequest request){
        return this.blogIndexByAll(request,1);
    }

    @RequestMapping("/page/index/{pagenum}")
    public String blogIndexByAll(HttpServletRequest request, @PathVariable("pagenum") int pagenum){
        PageIndexVo pageIndexVo = blogDetailService.findAllBlogDetail(pagenum);
        request.setAttribute("pageIndexVo",pageIndexVo);
        request.setAttribute("preUrl","/page/index/");
        request.setAttribute("hotTags",otherInformationService.findHotTag());
        request.setAttribute("newBlogs",otherInformationService.findNewBlog());
        request.setAttribute("hotBlogs",otherInformationService.findHotBlog());
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogindex/index";
    }
    //跳转至登录界面
    @RequestMapping("/page/login")
    public String tologinPage(HttpServletRequest request){
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogindex/login";
    }
    @RequestMapping("/page/register")
    public String toregisterPage(HttpServletRequest request){
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogindex/register";
    }
    @RequestMapping("/page/link")
    public String toLinkPage(HttpServletRequest request){
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        request.setAttribute("friendLinks",null);
        Map<Byte, List<LinkDetail>> linkDetailMap = otherInformationService.findAllLinkDetail();
        if (linkDetailMap != null) {
            //判断友链类别并封装数据 0-友链 1-推荐 2-个人网站
            if (linkDetailMap.containsKey((byte) 0)) {
                request.setAttribute("friendLinks", linkDetailMap.get((byte) 0));
            }
            if (linkDetailMap.containsKey((byte) 1)) {
                request.setAttribute("recommendLinks", linkDetailMap.get((byte) 1));
            }
            if (linkDetailMap.containsKey((byte) 2)) {
                request.setAttribute("personalLinks", linkDetailMap.get((byte) 2));
            }
        }
        return "blogindex/link";
    }

    @RequestMapping("/page/userMessage/{userId}")
    public String toUserMessagePage(HttpServletRequest request, @PathVariable("userId")String userid){
        return this.toUserMessagePage(request,userid,1);
    }
    @RequestMapping("/page/userMessage/{userId}/{pagenum}")
    public String toUserMessagePage(HttpServletRequest request, @PathVariable("userId")String userid,@PathVariable("pagenum")int pagenum){
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        request.setAttribute("userDetail",userPageService.findUserDetailByUserId(userid));
        PageIndexVo pageIndexVo = blogDetailService.findBlogDetailByUserId(userid,pagenum);
        request.setAttribute("pageIndexVo",pageIndexVo);
        request.setAttribute("preUrl","/page/userMessage/"+userid+"/");
        return "blogindex/usermessage";
    }
    @RequestMapping("/page/getVerifyCode")
    public void getVerifyCode(HttpSession session, HttpServletResponse response) throws IOException {
        String verifyCode = VerifyCodeUtils.generateVerifyCode(5);
        session.setAttribute("verifyCode",verifyCode);
        OutputStream os = response.getOutputStream();
        response.setContentType("image/png");
        VerifyCodeUtils.outputImage(200,47,os,verifyCode);
    }

}
