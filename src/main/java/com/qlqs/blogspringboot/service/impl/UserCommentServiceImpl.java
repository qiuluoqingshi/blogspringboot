package com.qlqs.blogspringboot.service.impl;

import com.qlqs.blogspringboot.dao.BlogDetailDao;
import com.qlqs.blogspringboot.dao.UserCommentDao;
import com.qlqs.blogspringboot.entity.CommentDetail;
import com.qlqs.blogspringboot.service.UserCommentService;
import com.qlqs.blogspringboot.vo.AllDetailVo;
import com.qlqs.blogspringboot.vo.BlogDetailVo;
import com.qlqs.blogspringboot.vo.CommentDetailVo;
import com.qlqs.blogspringboot.vo.ResultVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserCommentServiceImpl implements UserCommentService {
    @Autowired(required = false)
    private UserCommentDao userCommentDao;
    @Autowired(required = false)
    private BlogDetailDao blogDetailDao;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int userComment(CommentDetailVo commentDetailVo) {
        CommentDetail commentDetail = new CommentDetail();
        BeanUtils.copyProperties(commentDetailVo,commentDetail);
        Long blogid = commentDetailVo.getBlogId();
        if (commentDetailVo.getPid() == null)
        {
            commentDetail.setPid((long) 0);
        }
        java.util.Date currDate = new java.util.Date();
        Timestamp timestamp = new Timestamp(currDate.getTime());
        commentDetail.setCommentTime(timestamp);
        int insertSuccess = userCommentDao.insertUserComment(commentDetail)+userCommentDao.insertBlogCommentTable(commentDetail,blogid);
        return insertSuccess;

    }
    @Override
    public BlogDetailVo freshComment(Long blogid,int limit,int pagenum){
        BlogDetailVo blogDetailVo = new BlogDetailVo();
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
            int OnlyCommentAmount = blogDetailDao.findBlogOnlyCommentAmountByBlogId(blogid);
            blogDetailVo.setOnlyCommentAccount(OnlyCommentAmount);
            blogDetailVo.setCommentLimitNum(8);
            blogDetailVo.setCurrentPageNum(pagenum);
            blogDetailVo.setCommentPageNum((int) Math.ceil((double) OnlyCommentAmount / 8));
        }else{
            blogDetailVo.setUserCommentDetails(null);
            blogDetailVo.setUserReplyDetails(null);
            blogDetailVo.setOnlyCommentAccount(0);
            blogDetailVo.setCommentLimitNum(8);
            blogDetailVo.setCurrentPageNum(1);
            blogDetailVo.setCommentPageNum(0);
        }
        return blogDetailVo;
    }

    @Override
    public ResultVo allCommentDetailFind(int pagenum, int limit, Long blogid) {
        ResultVo resultVo = new ResultVo();
        AllDetailVo allDetailVo = new AllDetailVo();
        List<CommentDetail> commentDetails = new ArrayList();
        Map map = new HashMap();
        map.put("limit",limit);
        map.put("start",(pagenum-1)*limit);
        map.put("blogid",blogid);
        commentDetails.addAll(userCommentDao.findCommentDetailByBlogId(map));

        if(!CollectionUtils.isEmpty(commentDetails)&&commentDetails.get(0)!=null){
            allDetailVo.setDetails(commentDetails);
            allDetailVo.setCurrPage(pagenum);
            int totalCount = userCommentDao.findCommentDetailAmountByBlogId(map);
            allDetailVo.setTotalCount(totalCount);
            allDetailVo.setTotalPage((int) Math.ceil((double) totalCount / limit));
            resultVo.setResultCode(200);
            resultVo.setData(allDetailVo);
        }else{
            allDetailVo.setDetails(null);
            allDetailVo.setCurrPage(1);
            allDetailVo.setTotalPage(0);
            allDetailVo.setTotalCount(0);
            resultVo.setResultCode(500);
            resultVo.setData(null);
        }
        return resultVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo deleteCommentDetail(List<Long> blogids) {
        ResultVo resultVo = new ResultVo();
        try {
            userCommentDao.deleteCommentDetail(blogids);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("删除成功");
            return resultVo;
        }catch (Exception ex) {
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("删除失败，请重新尝试！");
            return resultVo;
        }

    }
}
