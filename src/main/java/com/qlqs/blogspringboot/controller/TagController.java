package com.qlqs.blogspringboot.controller;

import com.qlqs.blogspringboot.entity.TagDetail;
import com.qlqs.blogspringboot.service.BlogDetailService;
import com.qlqs.blogspringboot.service.OtherInformationService;
import com.qlqs.blogspringboot.vo.PageIndexVo;
import com.qlqs.blogspringboot.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TagController {
    @Autowired
    BlogDetailService blogDetailService;
    @Autowired
    private OtherInformationService otherInformationService;
    @RequestMapping("tag/{tagid}")
    public String blogIndexByCategory(HttpServletRequest request, @PathVariable("tagid") int tagid) {
        return this.blogIndexByCategory(request, tagid, 1,true);
    }

    @RequestMapping("tag/{tagid}/{pagenum}")
    public String blogIndexByCategory(HttpServletRequest request, @PathVariable("tagid") long tagid, @PathVariable("pagenum") int pagenum,boolean effective_click) {
        PageIndexVo pageIndexVo = blogDetailService.findBlogDetailByTag(tagid,pagenum,effective_click);
        request.setAttribute("pageIndexVo",pageIndexVo);
        request.setAttribute("preUrl","/tag/"+tagid+"/");
        request.setAttribute("hotTags",otherInformationService.findHotTag());
        request.setAttribute("newBlogs",otherInformationService.findNewBlog());
        request.setAttribute("hotBlogs",otherInformationService.findHotBlog());
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogindex/index";
    }

    @RequestMapping("/user/allTags")
    public @ResponseBody ResultVo allTagsFind(HttpServletRequest request){
        int limit = Integer.parseInt(request.getParameter("limit"));
        int pagenum = Integer.parseInt(request.getParameter("page"));
        return otherInformationService.allTagsFind(limit, pagenum);
    }

    @RequestMapping("/user/insertNewTags")
    public @ResponseBody ResultVo insertNewTags(@RequestBody TagDetail tagdetail){
        return otherInformationService.insertNewTags(tagdetail);
    }

    @RequestMapping("/user/deleteOldTags")
    public @ResponseBody ResultVo deleteOldTags(@RequestBody List<Long> tagIds){
        System.out.println(tagIds);
        return otherInformationService.deleteOldTags(tagIds);
    }
}