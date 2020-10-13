package com.qlqs.blogspringboot.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BlogBriefVo implements Serializable {
    private Long blogId;
    private String blogTitle;
    private String blogCoverImage;
    private String blogCategory;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private String userId;
    private String userName;
    private String userAvatar;
}
