package com.qlqs.blogspringboot.dao;

import com.qlqs.blogspringboot.entity.RoleDetail;
import com.qlqs.blogspringboot.entity.UserAccount;
import com.qlqs.blogspringboot.entity.UserDetail;
import com.qlqs.blogspringboot.vo.ResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserPageDao {
    List<RoleDetail> findRoleDetailByUserId(String userId);
    UserAccount findUserAccountByUserId(String userId);
    UserDetail findUserDetailByUserId(String userId);
    long findBlogAmount(String userId);
    long findCommentAmount(String userId);
    long findCategoryAmount();
    long findTagAmount();
    long findLinkAmount();
    void updateUserAvatar(@Param("newNameUrl") String newNameUrl, @Param("userid") String userid);
    void updateUserPassword(@Param("newPassword")String newPassword, @Param("userid") String userid);
    void updateUserMessage(@Param("userDetail")UserDetail userDetail, @Param("userid") String userid);
    void insertNewUserAccount(UserAccount userAccount);
    void insertNewUserDetail(UserDetail userDetail);
    void insertNewUserRole(UserAccount userAccount);

}
