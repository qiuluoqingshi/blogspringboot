package com.qlqs.blogspringboot.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommentDetailVo implements Serializable {
    private String userId;
    private String commentContent;
    private String userAvatar;
    private Long blogId;
    private String replyUserId;
    private Long pid;

}
