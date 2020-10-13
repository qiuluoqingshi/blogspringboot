package com.qlqs.blogspringboot.vo;

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
public class PageIndexVo implements Serializable {
    //博客简要信息列表
    private List<BlogBriefVo> blogBriefVos;
    //当前博客总数
    private int blogAmount;
    //每页记录数
    private int blogLimitNum;
    //博客页数
    private int blogPageNum;
    //当前页数
    private int currentPageNum;

}
