package com.qlqs.blogspringboot.vo;

import com.qlqs.blogspringboot.entity.BlogDetail;
import com.qlqs.blogspringboot.entity.CommentDetail;
import com.qlqs.blogspringboot.entity.TagDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BlogDetailVo implements Serializable {
    private BlogDetail blogDetail;
    private List<TagDetail> tagDetails;
    private List<CommentDetail> userCommentDetails;
    private List<CommentDetail> userReplyDetails;
    private int commentAmount;
    private int tagAmount;
    //不包括回复
    private int onlyCommentAccount;
    //每页记录数
    private int commentLimitNum;
    //评论页数
    private int commentPageNum;
    //当前评论页数
    private int currentPageNum;
}
