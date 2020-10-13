package com.qlqs.blogspringboot.service.impl;

import com.qlqs.blogspringboot.dao.OtherInformationDao;
import com.qlqs.blogspringboot.entity.*;
import com.qlqs.blogspringboot.service.OtherInformationService;
import com.qlqs.blogspringboot.vo.AllDetailVo;
import com.qlqs.blogspringboot.vo.ResultVo;
import lombok.AllArgsConstructor;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OtherInformationServiceImpl implements OtherInformationService {
    @Autowired(required = false)
    private OtherInformationDao otherInformationDao;
    private int limit = 5;
    @Override
    public List<TagDetail> findHotTag() {
        return otherInformationDao.findHotTag(limit);
    }

    @Override
    public List<BlogDetail> findNewBlog() {
        return otherInformationDao.findNewBlog(limit);
    }

    @Override
    public List<BlogDetail> findHotBlog() {
        return otherInformationDao.findHotBlog(limit);
    }

    @Override
    public ConfigurationDetail findConfiguration() {
        return otherInformationDao.findConfiguration();
    }

    @Override
    public UserDetail findUserDetailByUserId(String userid) {
        return otherInformationDao.findUserDetailByUserId(userid);
    }

    @Override
    public List<CategoryDetail> findAllCategoryDetails() {
        return otherInformationDao.findAllCategoryDetails();
    }

    @Override
    public Map<Byte, List<LinkDetail>> findAllLinkDetail() {
        List<LinkDetail> linkDetails = otherInformationDao.findAllLinkDetail();
        if(!CollectionUtils.isEmpty(linkDetails)&&linkDetails.get(0)!=null){
            Map<Byte, List<LinkDetail>> linkDetailMap = linkDetails.stream().collect(Collectors.groupingBy(LinkDetail::getLinkType));
            return linkDetailMap;
        }
        return null;
    }

    @Override
    public ResultVo allCategoriesFind(int limit, int pagenum) {
        AllDetailVo allDetailVo = new AllDetailVo();
        ResultVo resultVo = new ResultVo();
        Map map = new HashMap();
        map.put("start", (pagenum-1)*limit);
        map.put("limit", limit);
        List<CategoryDetail> categoryDetails = otherInformationDao.findAllCategoryDetailsByMap(map);
        if(!CollectionUtils.isEmpty(categoryDetails) && categoryDetails.get(0)!=null){
            allDetailVo.setDetails(categoryDetails);
            allDetailVo.setCurrPage(pagenum);
            int totalCount = otherInformationDao.findAllCategoryDetailsAmount();
            allDetailVo.setTotalCount(totalCount);
            allDetailVo.setTotalPage((int) Math.ceil((double) totalCount / limit));
            resultVo.setData(allDetailVo);
            return resultVo;
        }else{
            allDetailVo.setDetails(null);
            allDetailVo.setCurrPage(1);
            allDetailVo.setTotalCount(0);
            allDetailVo.setTotalPage(0);
            resultVo.setData(allDetailVo);
            return resultVo;
        }
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public ResultVo saveBlogCategory(CategoryDetail categoryDetail) {
        ResultVo resultVo = new ResultVo();
        categoryDetail.setBlogcategoryViews(0);
        java.util.Date currDate = new java.util.Date();
        Timestamp timestamp = new Timestamp(currDate.getTime());
        categoryDetail.setCreateTime(timestamp);
        try {
            otherInformationDao.saveBlogCategory(categoryDetail);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("新增分类成功");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("新增分类失败，刷新页面重试");
            return resultVo;
        }
    }
    @Override
    @Transactional( rollbackFor = Exception.class )
    public ResultVo updateBlogCategory(CategoryDetail categoryDetail) {
        ResultVo resultVo = new ResultVo();
        try {
            otherInformationDao.updateBlogCategory(categoryDetail);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("修改分类成功");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("修改分类失败，刷新页面重试");
            return resultVo;
        }
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public ResultVo deleteBlogCategory(List<Long> categoryIds) {
        ResultVo resultVo = new ResultVo();
        try {
            otherInformationDao.updateBlogDetailCategory(categoryIds);
            otherInformationDao.deleteBlogCategory(categoryIds);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("删除分类成功");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("删除分类失败，刷新页面重试");
            return resultVo;
        }
    }

    @Override
    public ResultVo allTagsFind(int limit, int pagenum) {
        AllDetailVo allDetailVo = new AllDetailVo();
        ResultVo resultVo = new ResultVo();
        Map map = new HashMap();
        map.put("start", (pagenum-1)*limit);
        map.put("limit", limit);
        List<TagDetail> tagDetails = otherInformationDao.findAllTagDetailsByMap(map);
        if(!CollectionUtils.isEmpty(tagDetails) && tagDetails.get(0)!=null){
            allDetailVo.setDetails(tagDetails);
            allDetailVo.setCurrPage(pagenum);
            int totalCount = otherInformationDao.findAllTagDetailsAmount();
            allDetailVo.setTotalCount(totalCount);
            allDetailVo.setTotalPage((int) Math.ceil((double) totalCount / limit));
            resultVo.setData(allDetailVo);
            return resultVo;
        }else{
            allDetailVo.setDetails(null);
            allDetailVo.setCurrPage(1);
            allDetailVo.setTotalCount(0);
            allDetailVo.setTotalPage(0);
            resultVo.setData(allDetailVo);
            return resultVo;
        }
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public ResultVo insertNewTags(TagDetail tagdetail) {
        ResultVo resultVo = new ResultVo();
        tagdetail.setTagViews((long)0);
        java.util.Date currDate = new java.util.Date();
        Timestamp timestamp = new Timestamp(currDate.getTime());
        tagdetail.setCreateTime(timestamp);
        try {
            otherInformationDao.insertNewTags(tagdetail);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("新增标签成功");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("新增标签失败，该标签可能已经存在，请刷新页面重试");
            return resultVo;
        }
    }
    @Override
    @Transactional( rollbackFor = Exception.class )
    public ResultVo deleteOldTags(List<Long> tagIds) {
        ResultVo resultVo = new ResultVo();
        try {
            otherInformationDao.deleteOldTags(tagIds);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("删除标签成功");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("删除标签失败，刷新页面重试");
            return resultVo;
        }
    }

    @Override
    public ResultVo allLinksFind(int limit, int pagenum) {
        AllDetailVo allDetailVo = new AllDetailVo();
        ResultVo resultVo = new ResultVo();
        Map map = new HashMap();
        map.put("start", (pagenum-1)*limit);
        map.put("limit", limit);
        List<LinkDetail> linkDetails = otherInformationDao.findAllLinkDetailByMap(map);
        if(!CollectionUtils.isEmpty(linkDetails) && linkDetails.get(0)!=null){
            allDetailVo.setDetails(linkDetails);
            allDetailVo.setCurrPage(pagenum);
            int totalCount = otherInformationDao.findAllLinkDetailsAmount();
            allDetailVo.setTotalCount(totalCount);
            allDetailVo.setTotalPage((int) Math.ceil((double) totalCount / limit));
            resultVo.setData(allDetailVo);
            return resultVo;
        }else{
            allDetailVo.setDetails(null);
            allDetailVo.setCurrPage(1);
            allDetailVo.setTotalCount(0);
            allDetailVo.setTotalPage(0);
            resultVo.setData(allDetailVo);
            return resultVo;
        }
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public ResultVo insertNewLink(LinkDetail linkDetail) {
        ResultVo resultVo = new ResultVo();
        java.util.Date currDate = new java.util.Date();
        Timestamp timestamp = new Timestamp(currDate.getTime());
        linkDetail.setCreateTime(timestamp);
        try {
            otherInformationDao.insertNewLink(linkDetail);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("新增友链成功");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("新增友链成功，请刷新页面重试");
            return resultVo;
        }
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public ResultVo updateOldLink(LinkDetail linkDetail) {
        ResultVo resultVo = new ResultVo();
        try {
            otherInformationDao.updateOldLink(linkDetail);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("修改友链成功");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("修改友链成功，请刷新页面重试");
            return resultVo;
        }
    }

    @Override
    public ResultVo deleteOldLink(List<Long> linkIds) {
        ResultVo resultVo = new ResultVo();
        try {
            otherInformationDao.deleteOldLink(linkIds);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("删除友链成功");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("删除友链成功，请刷新页面重试");
            return resultVo;
        }
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public ResultVo updateWebIcon(MultipartFile imageFile, String url) {
        ResultVo resultVo = new ResultVo();
        String oldNamePath = imageFile.getOriginalFilename();
        String suffixName = oldNamePath.substring(oldNamePath.lastIndexOf("."));
        StringBuilder builder = new StringBuilder();
        builder.append("webIcon_image").append(suffixName);
        String newName = builder.toString();
        File destFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\" + newName);
        String filePath = "\\images\\"+newName;
        try {
            if (!destFile.getParentFile().exists()){
                if (!destFile.getParentFile().mkdirs()){
                    throw new IOException("创建文件夹失败");
                }
            }
            imageFile.transferTo(destFile);
            otherInformationDao.updateWebIcon(filePath);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("网站图标更改成功");
            return resultVo;
        } catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("网站图标更改失败,请重新尝试");
            return resultVo;
        }
    }

    @Override
    public ResultVo updateWebMessage(ConfigurationDetail configurationDetail) {
        ResultVo resultVo = new ResultVo();
        try {
            otherInformationDao.updateWebMessage(configurationDetail);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("修改网站信息成功");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("修改网站信息失败，请刷新页面重试");
            return resultVo;
        }
    }

    @Override
    public ResultVo updateUserInfo(ConfigurationDetail configurationDetail) {
        ResultVo resultVo = new ResultVo();
        try {
            otherInformationDao.updateUserInfo(configurationDetail);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("修改个人信息成功");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("修改个人信息失败，请刷新页面重试");
            return resultVo;
        }
    }
}
