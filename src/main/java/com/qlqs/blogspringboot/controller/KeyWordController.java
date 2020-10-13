package com.qlqs.blogspringboot.controller;

import com.qlqs.blogspringboot.service.BlogDetailService;
import com.qlqs.blogspringboot.service.OtherInformationService;
import com.qlqs.blogspringboot.vo.PageIndexVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller

public class KeyWordController {
    @Autowired
    private BlogDetailService blogDetailService;
    @Autowired
    private OtherInformationService otherInformationService;
    @RequestMapping("keyword/{keyword}")
    public String blogIndexByKeyWord(HttpServletRequest request, @PathVariable("keyword")String keyword){
        return this.blogIndexByKeyWord(request,keyword,1);
    }

    @RequestMapping("keyword/{keyword}/{pagenum}")
    public String blogIndexByKeyWord(HttpServletRequest request, @PathVariable("keyword")String keyword,@PathVariable("pagenum")int pagenum){
        PageIndexVo pageIndexVo = blogDetailService.findBlogDetailByKeyWord(keyword,pagenum);
        request.setAttribute("preUrl","/keyword/"+keyword+"/");
        request.setAttribute("pageIndexVo",pageIndexVo);
        request.setAttribute("hotTags",otherInformationService.findHotTag());
        request.setAttribute("newBlogs",otherInformationService.findNewBlog());
        request.setAttribute("hotBlogs",otherInformationService.findHotBlog());
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogindex/index";
    }
}
