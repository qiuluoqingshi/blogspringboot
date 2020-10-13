package com.qlqs.blogspringboot.service;

import com.qlqs.blogspringboot.entity.BlogDetail;
import com.qlqs.blogspringboot.vo.AllDetailVo;
import com.qlqs.blogspringboot.vo.BlogDetailVo;
import com.qlqs.blogspringboot.vo.PageIndexVo;

import java.util.List;


public interface BlogDetailService {
    PageIndexVo findAllBlogDetail(int pagenum);
    List<BlogDetail> findBlogDetailByUserId(String userid);
    PageIndexVo findBlogDetailByUserId(String userid,int pagenum);
    AllDetailVo findAllBlogDetail(int pagenum, int limit, String keyword, String userId);
    PageIndexVo findBlogDetailByCategory(String categoryname,int pagenum);
    PageIndexVo findBlogDetailByKeyWord(String keyword,int pagenum);
    PageIndexVo findBlogDetailByTag(long tagid,int pagenum,boolean effective_click);
    BlogDetailVo findBlogDetailByBlogId(long blogid,int pagenum);
    int newBlogInsert(BlogDetail blogDetail);
    int oldBlogUpdate(BlogDetail blogDetail);
    int oldBlogsDelete(List<Long> blogDetailsId);
    BlogDetail findBlogDetailByBlogId(long blogid);
}
