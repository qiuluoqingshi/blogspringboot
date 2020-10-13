package com.qlqs.blogspringboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BlogDetail implements Serializable {
    private Long blogId;
    private String blogTitle;
    private String blogCoverImage;
    private String blogContent;
    private String blogCategory;
    private Byte blogStatus;
    private Long blogViews;
    private Byte enableComment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    private UserDetail userDetail;
    private String blogTags;

}
