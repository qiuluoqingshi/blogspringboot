package com.qlqs.blogspringboot.dao;

import com.qlqs.blogspringboot.entity.CommentDetail;
import com.qlqs.blogspringboot.vo.ResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface UserCommentDao {
    int insertUserComment(CommentDetail commentDetail);
    int insertBlogCommentTable(@Param("commentDetail") CommentDetail commentDetail, @Param("blogid") long blogid);
    List<Long> findBlogIdByUserId(String userid);
    List<CommentDetail> findCommentDetailByBlogId(Map map);
    int findCommentDetailAmountByBlogId(Map map);
    void deleteCommentDetail(List<Long> blogids);
}
