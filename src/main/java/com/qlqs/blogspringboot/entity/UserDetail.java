package com.qlqs.blogspringboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail implements Serializable {
    private String userId;
    private String userName;
    private String userSignature;
    private String userSex;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String userBirth;
    private String userWeixin;
    private String userQq;
    private String userPhone;
    private String userSchool;
    private String userCompany;
    private String userAvatar;

//    public UserDetail(String userName,String userSignature,String userSex,String userBirth,String userWeixin,String userQq,String userSchool,String userCompany,String userPhone){
//        this.userName = userName;
//        this.userSignature = userSignature;
//        this.userSex = userSex;
//        this.userBirth = userBirth;
//        this.userWeixin = userWeixin;
//        this.userQq = userQq;
//        this.userSchool = userSchool;
//        this.userCompany = userCompany;
//        this.userPhone = userPhone;
//    }
}
