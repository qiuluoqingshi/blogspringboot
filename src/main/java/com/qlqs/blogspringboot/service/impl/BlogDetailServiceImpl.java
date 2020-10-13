package com.qlqs.blogspringboot.service.impl;

import com.qlqs.blogspringboot.dao.BlogDetailDao;
import com.qlqs.blogspringboot.dao.OtherInformationDao;
import com.qlqs.blogspringboot.entity.BlogDetail;
import com.qlqs.blogspringboot.entity.CommentDetail;
import com.qlqs.blogspringboot.entity.TagDetail;
import com.qlqs.blogspringboot.service.BlogDetailService;
import com.qlqs.blogspringboot.vo.AllDetailVo;
import com.qlqs.blogspringboot.vo.BlogBriefVo;
import com.qlqs.blogspringboot.vo.BlogDetailVo;
import com.qlqs.blogspringboot.vo.PageIndexVo;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class BlogDetailServiceImpl implements BlogDetailService {
    @Autowired(required = false)
    private BlogDetailDao blogDetailDao;
    @Autowired(required = false)
    private OtherInformationDao otherInformationDao;
    private int limit = 8;//限制每页只能八条数据

    //搜索所有blog以及相关信息给主页
    @Override
    public PageIndexVo findAllBlogDetail(int pagenum) {
        Map map = new HashMap();
        map.put("start",(pagenum-1)*limit);
        map.put("limit",limit);
        map.put("blogStatus",1);
        List<BlogDetail> blogDetails =  blogDetailDao.findBlogDetail(map);
        List<BlogBriefVo> blogBriefVos = getBlogDetailVo(blogDetails);
        int blogAmount = blogDetailDao.findBlogAmount(map);
        PageIndexVo pageIndexVo = new PageIndexVo(blogBriefVos,blogAmount,limit,(int) Math.ceil((double) blogAmount / limit),pagenum);
        return pageIndexVo;
    }

    @Override
    public List<BlogDetail> findBlogDetailByUserId(String userid) {
        List<BlogDetail> blogs = blogDetailDao.findBlogDetailsByUserId(userid);
        if(!CollectionUtils.isEmpty(blogs)&&blogs.get(0)!=null){
            return blogs;
        }else{
            return null;
        }
    }

    @Override
    public PageIndexVo findBlogDetailByUserId(String userid, int pagenum) {
        Map map = new HashMap();
        map.put("start",(pagenum-1)*limit);
        map.put("limit",limit);
        map.put("blogStatus",1);
        map.put("userid",userid);
        List<BlogDetail> blogDetails =  blogDetailDao.findBlogDetailByUserId(map);
        List<BlogBriefVo> blogBriefVos = getBlogDetailVo(blogDetails);
        int blogAmount = blogDetailDao.findBlogAmountByUserId(map);
        PageIndexVo pageIndexVo = new PageIndexVo(blogBriefVos,blogAmount,limit,(int) Math.ceil((double) blogAmount / limit),pagenum);
        return pageIndexVo;
    }

    //搜索所有blog以及相关信息给blog管理页面
    @Override
    public AllDetailVo findAllBlogDetail(int pagenum, int limit, String keyword, String userId) {
        AllDetailVo allDetailVo = new AllDetailVo();
        List<BlogDetail> blogDetails = new ArrayList<>();
        Map map = new HashMap();
        map.put("start",(pagenum-1)*limit);
        map.put("limit",limit);
        map.put("userId",userId);
        map.put("keyword",keyword);
        blogDetails.addAll(blogDetailDao.findBlogDetailOnBackGround(map));
        if (!CollectionUtils.isEmpty(blogDetails)){
            allDetailVo.setDetails(blogDetails);
            allDetailVo.setCurrPage(pagenum);
            int totalCount = blogDetailDao.findBlogAmountOnBackGround(map);
            allDetailVo.setTotalCount(totalCount);
            allDetailVo.setTotalPage((int) Math.ceil((double) totalCount / limit));
        }else{
            allDetailVo.setDetails(null);
            allDetailVo.setCurrPage(0);
            allDetailVo.setTotalCount(0);
            allDetailVo.setTotalPage(0);
        }
        return allDetailVo;
    }
    //根据分类搜索blog
    @Override
    public PageIndexVo findBlogDetailByCategory(String categoryname,int pagenum) {
        Map map = new HashMap();
        map.put("limit",limit);
        map.put("start",(pagenum-1)*limit);
        map.put("blogStatus",1);
        map.put("categoryname",categoryname);
        List<BlogDetail> blogDetails = blogDetailDao.findBlogDetail(map);
        List<BlogBriefVo> blogBriefVos = getBlogDetailVo(blogDetails);
        int blogAmount = blogDetailDao.findBlogAmount(map);
        PageIndexVo pageIndexVo = new PageIndexVo(blogBriefVos,blogAmount,limit,(int) Math.ceil((double) blogAmount / limit),pagenum);
        return pageIndexVo;
    }
    //根据关键字搜索blog
    @Override
    public PageIndexVo findBlogDetailByKeyWord(String keyword, int pagenum) {
        Map map = new HashMap();
        map.put("limit",limit);
        map.put("start",(pagenum-1)*limit);
        map.put("keyword",keyword);
        map.put("blogStatus",1);
        List<BlogDetail> blogDetails = blogDetailDao.findBlogDetail(map);
        System.out.println(blogDetails);
        List<BlogBriefVo> blogBriefVos = getBlogDetailVo(blogDetails);
        int blogAmount = blogDetailDao.findBlogAmount(map);
        PageIndexVo pageIndexVo = new PageIndexVo(blogBriefVos,blogAmount,limit,(int) Math.ceil((double) blogAmount / limit),pagenum);
        return pageIndexVo;
    }
    //根据标签搜索blog
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageIndexVo findBlogDetailByTag(long tagid, int pagenum,boolean effective_click) {
        Map map = new HashMap();
        map.put("limit",limit);
        map.put("start",(pagenum-1)*limit);
        map.put("blogStatus",1);
        map.put("tagid",tagid);
        blogDetailDao.updateTagViews(tagid);
        List<BlogDetail> blogDetails = blogDetailDao.findBlogDetailByTag(map);
        List<BlogBriefVo> blogBriefVos = getBlogDetailVo(blogDetails);
        int blogAmount = blogDetailDao.findBlogAmountByTag(map);
        PageIndexVo pageIndexVo = new PageIndexVo(blogBriefVos,blogAmount,limit,(int) Math.ceil((double) blogAmount / limit),pagenum);
        return pageIndexVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BlogDetailVo findBlogDetailByBlogId(long blogid,int pagenum) {
        //blog访问数+1
        blogDetailDao.updateBlogViews(blogid);
        //获取查看的blog本身的详细信息
        BlogDetail blogDetail = blogDetailDao.findBlogDetailByBlogId(blogid);
        if (blogDetail == null){
            return null;
        }
        //进行vo转换
        BlogDetailVo blogDetailVo = new BlogDetailVo();
        blogDetailVo.setBlogDetail(blogDetail);
        //获取查看的blog的所有标签
        List<TagDetail> tagDetails = new ArrayList<>();
        tagDetails.addAll(blogDetailDao.findBlogTagDetailByBlogId(blogid));
        if(!CollectionUtils.isEmpty(tagDetails)&&tagDetails.get(0)!=null)
        {
            blogDetailVo.setTagDetails(tagDetails);
            blogDetailVo.setTagAmount(tagDetails.size());
        }else{
            blogDetailVo.setTagDetails(null);
            blogDetailVo.setTagAmount(0);
        }
        Map map = new HashMap();
        map.put("limit",limit);
        map.put("start",(pagenum-1)*limit);
        map.put("blogid",blogid);
        map.put("pid",0);
        List<CommentDetail> userCommentDetails = new ArrayList<>();
        userCommentDetails.addAll(blogDetailDao.findBlogCommentDetailByBlogId(map));
        List<CommentDetail> userReplyDetails = new ArrayList<>();
        if(!CollectionUtils.isEmpty(userCommentDetails))
        {
            for(CommentDetail userCommentDetail:userCommentDetails){
                Map map2 = new HashMap();
                map2.put("commentid",userCommentDetail.getCommentId());
                map2.put("blogid",blogid);
                List<CommentDetail> userReplyDetail = blogDetailDao.findBlogReplyDetailByBlogId(map2);
                if(!CollectionUtils.isEmpty(userReplyDetail)){
                    userReplyDetails.addAll(userReplyDetail);
                }
            }
            blogDetailVo.setUserCommentDetails(userCommentDetails);
            blogDetailVo.setUserReplyDetails(userReplyDetails);
            blogDetailVo.setCommentAmount(blogDetailDao.findBlogCommentAmountByBlogId(blogid));
            int OnlyCommentAmount = blogDetailDao.findBlogOnlyCommentAmountByBlogId(blogid);
            blogDetailVo.setOnlyCommentAccount(OnlyCommentAmount);
            blogDetailVo.setCommentLimitNum(8);
            blogDetailVo.setCurrentPageNum(pagenum);
            blogDetailVo.setCommentPageNum((int) Math.ceil((double) OnlyCommentAmount / 8));
        }else{
            blogDetailVo.setUserCommentDetails(null);
            blogDetailVo.setUserReplyDetails(null);
            blogDetailVo.setCommentAmount(0);
            blogDetailVo.setOnlyCommentAccount(0);
            blogDetailVo.setCommentLimitNum(8);
            blogDetailVo.setCurrentPageNum(1);
            blogDetailVo.setCommentPageNum(0);
        }

        return blogDetailVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int newBlogInsert(BlogDetail blogDetail) {
        blogDetail.setCreateTime(new Timestamp(new Date().getTime()));
        String[] tags = blogDetail.getBlogTags().split(",");
        List<TagDetail> OldTagDetails = new ArrayList<>();
        List<TagDetail> NewTagDetails = new ArrayList<>();
        for (String tag : tags){
            TagDetail tagDetail = otherInformationDao.findOldBlogTagId(tag);
            if(tagDetail==null) {
                NewTagDetails.add(new TagDetail(null,tag,new Timestamp(new Date().getTime()),null));
            }else {
                OldTagDetails.add(tagDetail);
            }
        }
        try {
            blogDetailDao.newBlogInsert(blogDetail);
            blogDetailDao.newBlogAndUserConnect(blogDetail,blogDetail.getUserDetail().getUserId());
            if (NewTagDetails.size()>0) {
                otherInformationDao.addNewBlogTagsByBlog(NewTagDetails);
                otherInformationDao.addNewBlogTagsAndBlogConnect(blogDetail,NewTagDetails);
            }
            if (OldTagDetails.size()>0) {
                otherInformationDao.addNewBlogTagsAndBlogConnect(blogDetail,OldTagDetails);
            }
            return 1;
        }catch (Exception ex){
            ex.printStackTrace();
            return -1;
        }

    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int oldBlogUpdate(BlogDetail blogDetail) {
        blogDetail.setUpdateTime(new Timestamp(new Date().getTime()));
        String[] tags = blogDetail.getBlogTags().split(",");
        List<TagDetail> OldTagDetails = new ArrayList<>();
        List<TagDetail> NewTagDetails = new ArrayList<>();
        for (String tag : tags){
            TagDetail tagDetail = otherInformationDao.findOldBlogTagId(tag);
            if(tagDetail==null) {
                NewTagDetails.add(new TagDetail(null,tag,new Timestamp(new Date().getTime()),null));
            }else {
                OldTagDetails.add(tagDetail);
            }
        }
        try {
            blogDetailDao.oldBlogUpdate(blogDetail);
            otherInformationDao.oldBlogTagAndBlogDeleteConnect(blogDetail.getBlogId());
            if (NewTagDetails.size()>0) {
                otherInformationDao.addNewBlogTagsByBlog(NewTagDetails);
                otherInformationDao.addNewBlogTagsAndBlogConnect(blogDetail,NewTagDetails);
            }
            if (OldTagDetails.size()>0) {
                otherInformationDao.addNewBlogTagsAndBlogConnect(blogDetail,OldTagDetails);
            }
            return 1;
        }catch (Exception ex){
            ex.printStackTrace();
            return -1;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int oldBlogsDelete(List<Long> blogDetailsId) {
        try {
            blogDetailDao.oldBlogsDelete(blogDetailsId);
            return 1;
        }catch (Exception ex){
            ex.printStackTrace();
            return -1;
        }

    }

    @Override
    public BlogDetail findBlogDetailByBlogId(long blogid) {
        BlogDetail blogDetail = blogDetailDao.findBlogDetailByBlogId(blogid);
        List<TagDetail> tagDetails = new ArrayList<>();
        tagDetails.addAll(blogDetailDao.findBlogTagDetailByBlogId(blogid));
        String blogTags = "";
        if(!CollectionUtils.isEmpty(tagDetails)&&tagDetails.get(0)!=null)
        {
            for (TagDetail tagDetail:tagDetails){
                blogTags = blogTags + tagDetail.getTagName() + ",";
            }
            blogTags = blogTags.substring(0,blogTags.length()-1);
            blogDetail.setBlogTags(blogTags);
        }else{
            blogDetail.setBlogTags(null);
        }
        return blogDetail;
    }

    public List<BlogBriefVo> getBlogDetailVo(List<BlogDetail> blogDetails){
        List<BlogBriefVo> blogBriefVos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(blogDetails)){
            for(BlogDetail blogDetail:blogDetails){
                BlogBriefVo blogBriefVo = new BlogBriefVo();
                BeanUtils.copyProperties(blogDetail,blogBriefVo);
                if(blogDetail.getUserDetail() != null){
                    blogBriefVo.setUserId(blogDetail.getUserDetail().getUserId());
                    blogBriefVo.setUserName(blogDetail.getUserDetail().getUserName());
                    blogBriefVo.setUserAvatar(blogDetail.getUserDetail().getUserAvatar());
                }
                blogBriefVos.add(blogBriefVo);
            }
        }
        return blogBriefVos;
    }
}
