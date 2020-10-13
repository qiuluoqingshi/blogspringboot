package com.qlqs.blogspringboot.vo;

import com.qlqs.blogspringboot.entity.BlogDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AllDetailVo implements Serializable {
    private Object details;
    private int currPage;
    private int totalPage;
    private int totalCount;
}
