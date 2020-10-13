package com.qlqs.blogspringboot.service;

import com.qlqs.blogspringboot.vo.BlogDetailVo;
import com.qlqs.blogspringboot.vo.CommentDetailVo;
import com.qlqs.blogspringboot.vo.ResultVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;


public interface UserCommentService {
    int userComment(CommentDetailVo commentDetailVo);
    BlogDetailVo freshComment(Long blogid,int limit,int pagenum);
    ResultVo allCommentDetailFind(int pagenum, int limit, Long blogid);
    ResultVo deleteCommentDetail(List<Long> blogids);
}
