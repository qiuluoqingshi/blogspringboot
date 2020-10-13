package com.qlqs.blogspringboot.controller;

import com.qlqs.blogspringboot.entity.CommentDetail;
import com.qlqs.blogspringboot.entity.TagDetail;
import com.qlqs.blogspringboot.service.UserCommentService;
import com.qlqs.blogspringboot.vo.BlogDetailVo;
import com.qlqs.blogspringboot.vo.CommentDetailVo;
import com.qlqs.blogspringboot.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class UserCommentController {
    @Autowired
    UserCommentService userCommentService;
    @RequestMapping("user/comment")
    public @ResponseBody int userComment(@RequestBody CommentDetailVo commentDetailVo) {
        return userCommentService.userComment(commentDetailVo);
    }
    @RequestMapping("user/freshcomment")
    public String userFreshComment(HttpServletRequest request) {
        BlogDetailVo blogDetailVo = userCommentService.freshComment(Long.parseLong(request.getParameter("blogid")), 8, Integer.parseInt(request.getParameter("pagenum")));
        request.setAttribute("blogDetailVo",blogDetailVo);
        return "blogindex/comment::comment-fragment";
    }
    @RequestMapping("/user/allCommentDetail")
    public @ResponseBody ResultVo allCommentDetailFind(HttpServletRequest request){
        ResultVo resultVo = new ResultVo();
        int pagenum = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        Long blogid = Long.parseLong(request.getParameter("blogid"));
        resultVo = userCommentService.allCommentDetailFind(pagenum,limit,blogid);
        return resultVo;
    }
    @RequestMapping("/user/deleteCommentDetail")
    public @ResponseBody ResultVo deleteCommentDetail(@RequestBody List<Long> blogids){
        return userCommentService.deleteCommentDetail(blogids);
    }
}

