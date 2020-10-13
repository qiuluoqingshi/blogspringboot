package com.qlqs.blogspringboot.service;

import com.qlqs.blogspringboot.entity.BlogDetail;
import com.qlqs.blogspringboot.entity.UserAccount;
import com.qlqs.blogspringboot.entity.UserDetail;
import com.qlqs.blogspringboot.vo.ResultVo;
import com.qlqs.blogspringboot.vo.UserAccountVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.Map;

public interface UserPageService {
    int login(UserAccountVo userAccountVo);
    ResultVo insertNewUserAccount(UserAccountVo userAccountVo);
    Map<String ,Long> fingAllTypeAmount(String userId);
    UserDetail findUserDetailByUserId(String userId);
    ResultVo updateUserAvatar(MultipartFile imageFile, String url);
    ResultVo updateUserPassword(String oldPassword, String newPassword);
    ResultVo updateUserMessage(UserDetail userDetail);
}
