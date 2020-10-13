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
public class UserAccountVo implements Serializable {
    private String userId;
    private String userName;
    private String userPassword;
    private String verifyCode;
    private boolean rememberMe;
}
