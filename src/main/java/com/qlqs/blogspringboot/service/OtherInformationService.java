package com.qlqs.blogspringboot.service;

import com.qlqs.blogspringboot.entity.*;
import com.qlqs.blogspringboot.vo.ResultVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface OtherInformationService {
    List<TagDetail> findHotTag();
    List<BlogDetail> findNewBlog();
    List<BlogDetail> findHotBlog();
    ConfigurationDetail findConfiguration();
    UserDetail findUserDetailByUserId(String userid);
    List<CategoryDetail> findAllCategoryDetails();
    Map<Byte,List<LinkDetail>> findAllLinkDetail();
    ResultVo allCategoriesFind(int limit, int pagenum);
    ResultVo saveBlogCategory(CategoryDetail categoryDetail);
    ResultVo updateBlogCategory(CategoryDetail categoryDetail);
    ResultVo deleteBlogCategory(List<Long> categoryIds);
    ResultVo allTagsFind(int limit, int pagenum);
    ResultVo insertNewTags(TagDetail tagdetail);
    ResultVo deleteOldTags(List<Long> tagIds);
    ResultVo allLinksFind(int limit, int pagenum);
    ResultVo insertNewLink(LinkDetail linkDetail);
    ResultVo updateOldLink(LinkDetail linkDetail);
    ResultVo deleteOldLink(List<Long> linkIds);
    ResultVo updateWebIcon(MultipartFile imageFile, String url);
    ResultVo updateWebMessage(ConfigurationDetail configurationDetail);
    ResultVo updateUserInfo(ConfigurationDetail configurationDetail);
}
