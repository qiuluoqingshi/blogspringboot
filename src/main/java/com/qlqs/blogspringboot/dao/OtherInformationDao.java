package com.qlqs.blogspringboot.dao;

import com.qlqs.blogspringboot.entity.*;
import com.qlqs.blogspringboot.vo.ResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OtherInformationDao {
    List<TagDetail> findHotTag(int limit);
    List<BlogDetail> findNewBlog(int limit);
    List<BlogDetail> findHotBlog(int limit);
    ConfigurationDetail findConfiguration();
    UserDetail findUserDetailByUserId(String userid);
    //将发表的blog中所有标签进行添加
    int addNewBlogTagsByBlog(List<TagDetail> tagDetails);
    int addNewBlogTagsAndBlogConnect(@Param("blogDetail")BlogDetail blogDetail, @Param("tagDetails")List<TagDetail> tagDetails);
    int oldBlogTagAndBlogDeleteConnect(long blogid);
    TagDetail findOldBlogTagId(String tagName);
    List<CategoryDetail> findAllCategoryDetails();
    List<CategoryDetail> findAllCategoryDetailsByMap(Map map);
    int findAllCategoryDetailsAmount();
    List<LinkDetail> findAllLinkDetail();
    void saveBlogCategory(CategoryDetail categoryDetail);
    void updateBlogCategory(CategoryDetail categoryDetail);
    void updateBlogDetailCategory(List<Long> categoryIds);
    void deleteBlogCategory(List<Long> categoryIds);
    List<TagDetail> findAllTagDetailsByMap(Map map);
    int findAllTagDetailsAmount();
    void insertNewTags(TagDetail tagdetail);
    void deleteOldTags(List<Long> tagIds);
    List<LinkDetail> findAllLinkDetailByMap(Map map);
    int findAllLinkDetailsAmount();
    void insertNewLink(LinkDetail linkDetail);
    void updateOldLink(LinkDetail linkDetail);
    void deleteOldLink(List<Long> linkIds);
    void updateWebIcon(String filePath);
    void updateWebMessage(ConfigurationDetail configurationDetail);
    void updateUserInfo(ConfigurationDetail configurationDetail);
}
