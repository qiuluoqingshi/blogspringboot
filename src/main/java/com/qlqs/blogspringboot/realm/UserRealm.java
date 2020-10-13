package com.qlqs.blogspringboot.realm;

import com.qlqs.blogspringboot.dao.UserPageDao;
import com.qlqs.blogspringboot.entity.RoleDetail;
import com.qlqs.blogspringboot.entity.UserAccount;
import com.qlqs.blogspringboot.exception.LockedAccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserPageDao userPageDao;
    @Override
    //授权方法
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String primaryPrincipal = (String)principalCollection.getPrimaryPrincipal();
        List<RoleDetail> roleDetails = userPageDao.findRoleDetailByUserId(primaryPrincipal);
        if(roleDetails.get(0) != null){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            for (RoleDetail roleDetail : roleDetails){
                simpleAuthorizationInfo.addRole(roleDetail.getRoleType());
            }
            return simpleAuthorizationInfo;
        }
        return null;
    }

    @Override
    //认证方法
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userId = (String) authenticationToken.getPrincipal();
        UserAccount userAccount = userPageDao.findUserAccountByUserId(userId);
        if (!ObjectUtils.isEmpty(userAccount)){
            if (userAccount.getLocked() == 1) {
                throw new LockedAccountException();
            }
            return new SimpleAuthenticationInfo(userAccount.getUserId(),userAccount.getUserPassword(), ByteSource.Util.bytes(userAccount.getSalt()),this.getName());
        }
        return null;
    }

}
