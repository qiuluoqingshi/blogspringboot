package com.qlqs.blogspringboot.controller;

import com.qlqs.blogspringboot.entity.CategoryDetail;
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
public class CategoryController {
    @Autowired
    private BlogDetailService blogDetailService;

    @Autowired
    private OtherInformationService otherInformationService;

    //根据分类查找blog列表
    @RequestMapping("category/{categoryname}")
    public String blogIndexByCategory(HttpServletRequest request, @PathVariable("categoryname")String categoryname){
        return this.blogIndexByCategory(request,categoryname,1);
    }
    @RequestMapping("category/{categoryname}/{pagenum}")
    public String blogIndexByCategory(HttpServletRequest request, @PathVariable("categoryname")String categoryname, @PathVariable("pagenum") int pagenum){
        PageIndexVo pageIndexVo = blogDetailService.findBlogDetailByCategory(categoryname,pagenum);
        request.setAttribute("preUrl","/category/"+categoryname+"/");
        request.setAttribute("pageIndexVo",pageIndexVo);
        request.setAttribute("hotTags",otherInformationService.findHotTag());
        request.setAttribute("newBlogs",otherInformationService.findNewBlog());
        request.setAttribute("hotBlogs",otherInformationService.findHotBlog());
        request.setAttribute("configuration",otherInformationService.findConfiguration());
        return "blogindex/index";
    }
    @RequestMapping("/user/allCategories")
    public @ResponseBody ResultVo allCategoriesFind(HttpServletRequest request){
        int limit = Integer.parseInt(request.getParameter("limit"));
        int pagenum = Integer.parseInt(request.getParameter("page"));
        return otherInformationService.allCategoriesFind(limit, pagenum);
    }

    @RequestMapping("/user/saveBlogCategory")
    public @ResponseBody ResultVo saveBlogCategory(@RequestBody CategoryDetail categoryDetail){
        return otherInformationService.saveBlogCategory(categoryDetail);
    }
    @RequestMapping("/user/updateBlogCategory")
    public @ResponseBody ResultVo updateBlogCategory(@RequestBody CategoryDetail categoryDetail){
        return otherInformationService.updateBlogCategory(categoryDetail);
    }
    @RequestMapping("/user/deleteBlogCategory")
    public @ResponseBody ResultVo deleteBlogCategory(@RequestBody List<Long> categoryIds){
        return otherInformationService.deleteBlogCategory(categoryIds);
    }
}
