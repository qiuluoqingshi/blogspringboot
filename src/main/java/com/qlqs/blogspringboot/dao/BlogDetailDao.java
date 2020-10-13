package com.qlqs.blogspringboot.dao;

import com.qlqs.blogspringboot.entity.BlogDetail;
import com.qlqs.blogspringboot.entity.CommentDetail;
import com.qlqs.blogspringboot.entity.TagDetail;
import com.qlqs.blogspringboot.vo.PageIndexVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BlogDetailDao {
    //获取所有blog，返回给首页
    List<BlogDetail> findBlogDetail(Map map);
    List<BlogDetail> findBlogDetailOnBackGround(Map map);
    //根据userid获取blog
    List<BlogDetail> findBlogDetailsByUserId(String userid);
    List<BlogDetail> findBlogDetailByUserId(Map map);
    int findBlogAmountByUserId(Map map);
    //获取blog总数
    int findBlogAmount(Map map);
    int findBlogAmountOnBackGround(Map map);
    //根据标签获取blog
    List<BlogDetail> findBlogDetailByTag(Map map);
    //通过标签获取的blog总数
    int findBlogAmountByTag(Map map);
    //更新标签访问数
    void updateTagViews(long tagid);
    //根据blogId获取指定blog
    BlogDetail findBlogDetailByBlogId(long blogid);
    //根据blogId获取指定blog的所有标签
    List<TagDetail> findBlogTagDetailByBlogId(long blogid);
    //根据blogId获取指定blog的所有评论
    List<CommentDetail> findBlogCommentDetailByBlogId(Map map);
    //根据blogId获取指定blog的所有回复
    List<CommentDetail> findBlogReplyDetailByBlogId(Map map);
    //更新指定blog的访问数
    void updateBlogViews(long blogid);
    //根据blogId获得该blog的评论数
    int findBlogCommentAmountByBlogId(long blogid);
    //根据blogId获取该blog的回复数
    int findBlogOnlyCommentAmountByBlogId(long blogid);
    //根据blogId获取指定blog的标签总数
    int findBlogTagAmountByBlogId(long blogid);
    //新增blog相关信息
    int newBlogInsert(BlogDetail blogDetail);
    //修改blog相关信息
    int oldBlogUpdate(BlogDetail blogDetail);
    //将发表blog的用户和该blog关联起来
    int newBlogAndUserConnect(@Param("blogDetail")BlogDetail blogDetail,@Param("userId")String userId);
    //根据blogid将该blog有关信息全部删除
    int oldBlogsDelete(List<Long> blogDetailsId);
}
