package com.qlqs.blogspringboot.service.impl;

import com.qlqs.blogspringboot.dao.UserPageDao;
import com.qlqs.blogspringboot.entity.UserAccount;
import com.qlqs.blogspringboot.entity.UserDetail;
import com.qlqs.blogspringboot.exception.LockedAccountException;
import com.qlqs.blogspringboot.service.UserPageService;
import com.qlqs.blogspringboot.utils.SaltUtil;
import com.qlqs.blogspringboot.utils.UrlUtil;
import com.qlqs.blogspringboot.vo.ResultVo;
import com.qlqs.blogspringboot.vo.UserAccountVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

@Service
public class UserPageServiceImpl implements UserPageService {
    @Autowired(required = false)
    UserPageDao userPageDao;
    @Override
    public int login(UserAccountVo userAccountVo) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String verifyCode = userAccountVo.getVerifyCode();
        //验证码错误
        if(!verifyCode.equals(session.getAttribute("verifyCode"))){
            System.out.println("验证码错误");
            return 0;
        }
        String userId = userAccountVo.getUserId();
        String userPassword = userAccountVo.getUserPassword();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userId,userPassword);
        try {
            usernamePasswordToken.setRememberMe(userAccountVo.isRememberMe());
            subject.login(usernamePasswordToken);
            System.out.println("登录成功");
            return 1;
        }catch (UnknownAccountException ex) {
            ex.printStackTrace();
            System.out.println("账号不存在或者错误");
            return -1;
        }catch (IncorrectCredentialsException ex) {
            ex.printStackTrace();
            System.out.println("密码错误");
            return -2;
        }catch (LockedAccountException ex){
            //ex.printStackTrace();
            System.out.println("账号封禁");
            return -3;
        }
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public ResultVo insertNewUserAccount(UserAccountVo userAccountVo) {
        ResultVo resultVo = new ResultVo();
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String verifyCode = userAccountVo.getVerifyCode();
        //验证码错误
        if(!verifyCode.equals(session.getAttribute("verifyCode"))){
            resultVo.setResultCode(500);
            resultVo.setResultMessage("验证码错误");
            return resultVo;
        }
        if(userPageDao.findUserAccountByUserId(userAccountVo.getUserId())!=null){
            resultVo.setResultCode(500);
            resultVo.setResultMessage("该账号已存在！");
            return resultVo;
        }
        try{
            String salt = SaltUtil.getSalt(10);
            Md5Hash md5Hash = new Md5Hash(userAccountVo.getUserPassword(),salt,1024);
            UserAccount userAccount = new UserAccount(userAccountVo.getUserId(),md5Hash.toHex(),0,salt);
            UserDetail userDetail = new UserDetail();
            userDetail.setUserId(userAccountVo.getUserId());
            userDetail.setUserName(userAccountVo.getUserName());
            userDetail.setUserAvatar("/images/userAvatar/default.jpg");
            userPageDao.insertNewUserAccount(userAccount);
            userPageDao.insertNewUserDetail(userDetail);
            userPageDao.insertNewUserRole(userAccount);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("注册成功，点击确定跳转到登录界面");
            return resultVo;
        }catch(Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("出现未知错误，请联系管理员或者开发者进行维护！");
            return resultVo;
        }
    }

    @Override
    public Map<String, Long> fingAllTypeAmount(String userId) {
        Map<String,Long> map = new Hashtable<>();
        Subject subject = SecurityUtils.getSubject();
        map.put("blogAmount", userPageDao.findBlogAmount(userId));
        map.put("commentAmount", userPageDao.findCommentAmount(userId));
        if(subject.hasRole("admin")||subject.hasRole("superadmin"))
        {
            map.put("categoryAmount", userPageDao.findCategoryAmount());
            map.put("tagAmount", userPageDao.findTagAmount());
            map.put("linkAmount", userPageDao.findLinkAmount());
        }
        return map;
    }

    @Override
    public UserDetail findUserDetailByUserId(String userId) {
        return userPageDao.findUserDetailByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo updateUserAvatar(MultipartFile imageFile, String url) {
        ResultVo resultVo = new ResultVo();
        String oldNamePath = imageFile.getOriginalFilename();
        String suffixName = oldNamePath.substring(oldNamePath.lastIndexOf("."));
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        StringBuilder newNameBuilder = new StringBuilder();
        newNameBuilder.append("userAvatar_"+SecurityUtils.getSubject().getPrincipal())
                      .append(suffixName);
        String newName = newNameBuilder.toString();
        File destFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\userAvatar\\" + newName);
        try {
            String newNameUrl = UrlUtil.getHost(new URI(url)) + "/userAvatarUpload/" + newName;
            if(!destFile.getParentFile().exists()){
                if(!destFile.getParentFile().mkdirs()){
                    throw new IOException("创建文件夹失败");
                }
            }
            imageFile.transferTo(destFile);
            userPageDao.updateUserAvatar(newNameUrl,(String)SecurityUtils.getSubject().getPrincipal());
            resultVo.setResultCode(200);
            resultVo.setResultMessage("头像更改成功");
            resultVo.setData(newNameUrl);
            return resultVo;
        } catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("头像更改失败,请重试");
            return resultVo;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVo updateUserPassword(String oldPassword, String newPassword) {
        ResultVo resultVo = new ResultVo();
        Subject subject = SecurityUtils.getSubject();
        String userid = (String) subject.getPrincipal();
        UserAccount userAccount = userPageDao.findUserAccountByUserId(userid);
        Md5Hash md5Hash = new Md5Hash(oldPassword,userAccount.getSalt(),1024);
        if (!md5Hash.toHex().equals(userAccount.getUserPassword())){
            resultVo.setResultCode(500);
            resultVo.setResultMessage("旧密码输入错误，请重新输入");
            return resultVo;
        }
        try {
            Md5Hash md5HashNew = new Md5Hash(newPassword,userAccount.getSalt(),1024);
            userPageDao.updateUserPassword(md5HashNew.toHex(),userid);
            resultVo.setResultCode(200);
            resultVo.setResultMessage("修改密码成功，即将重新登录");
            return resultVo;
        }catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("修改密码失败，请重新尝试");
        }
        return null;
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public ResultVo updateUserMessage(UserDetail userDetail) {
        ResultVo resultVo = new ResultVo();
        try {
            userPageDao.updateUserMessage(userDetail, (String) SecurityUtils.getSubject().getPrincipal());
            resultVo.setResultCode(200);
            resultVo.setResultMessage("修改个人信息成功，请刷新页面查看");
            return resultVo;

        } catch (Exception ex){
            ex.printStackTrace();
            resultVo.setResultCode(500);
            resultVo.setResultMessage("修改个人信息失败，请重新尝试");
            return resultVo;
        }
    }

}
