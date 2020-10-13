package com.qlqs.blogspringboot;

import com.qlqs.blogspringboot.dao.BlogDetailDao;
import com.qlqs.blogspringboot.dao.OtherInformationDao;
import com.qlqs.blogspringboot.dao.UserCommentDao;
import com.qlqs.blogspringboot.dao.UserPageDao;
import com.qlqs.blogspringboot.entity.BlogDetail;
import com.qlqs.blogspringboot.entity.CommentDetail;
import com.qlqs.blogspringboot.entity.TagDetail;
import com.qlqs.blogspringboot.service.BlogDetailService;
import org.apache.shiro.util.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class BlogspringbootApplicationTests {
    @Autowired
    private BlogDetailService blogDetailService;
    @Autowired(required = false)
    private OtherInformationDao otherInformationDao;
    @Autowired(required = false)
    private UserPageDao userPageDao;
    @Autowired(required = false)
    private UserCommentDao userCommentDao;
    @Autowired(required = false)
    private BlogDetailDao blogDetailDao;
    @Test
    @Transactional
    void contextLoads() {
//        Map map = new HashMap();
//        map.put("limit",10);
//        map.put("start",(1-1)*10);
//        map.put("blogid",6);
//        map.put("pid",0);
//        List<CommentDetail> userCommentDetails = new ArrayList<>();
//        userCommentDetails.addAll(blogDetailDao.findBlogCommentDetailByBlogId(map));
//        List<CommentDetail> userReplyDetails = new ArrayList<>();
//        if(!CollectionUtils.isEmpty(userCommentDetails))
//        {
//            for(CommentDetail userCommentDetail:userCommentDetails){
//                Map map2 = new HashMap();
//                map2.put("commentid",userCommentDetail.getCommentId());
//                map2.put("blogid",6);
//                List<CommentDetail> userReplyDetail = blogDetailDao.findBlogReplyDetailByBlogId(map2);
//                if(!CollectionUtils.isEmpty(userReplyDetail)){
//                    userReplyDetails.addAll(userReplyDetail);
//                }
//            }
//        }
//        System.out.println(userCommentDetails);

    }
    public static Date addDayOfDate(Date date,int i){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, i);
        Date newDate = c.getTime();
        return newDate;
    }
}
