package com.jingdianjichi.auth.domain.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.google.gson.Gson;
import com.jingdianjichi.auth.common.enums.AuthUserStatusEnums;
import com.jingdianjichi.auth.common.enums.IsDeleteEnums;
import com.jingdianjichi.auth.domain.constants.AuthConstants;
import com.jingdianjichi.auth.domain.entity.AuthUserBO;
import com.jingdianjichi.auth.domain.redis.RedisUtil;
import com.jingdianjichi.auth.domain.service.AuthUserDomainService;
import com.jingdianjichi.auth.domain.convent.AuthUserBOConverter;
import com.jingdianjichi.basic.entity.*;
import com.jingdianjichi.basic.service.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AuthUserDomainServiceImpl implements AuthUserDomainService {

    @Resource
    private AuthUserService authUserService;

    @Resource
    private AuthUserRoleService authUserRoleService;

    @Resource
    private AuthRoleService authRoleService;

    @Resource
    private AuthRolePermissionService authRolePermissionService;

    @Resource
    private AuthPermissionService authPermissionService;

    @Resource
    private RedisUtil redisUtil;


    private final String salt = "12312312";

    private static final String LOGIN_PREFIX = "loginCode";

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public Boolean register(AuthUserBO authUserBO) {
        // 判断用户是否登录过
        AuthUser existAuthUser = new AuthUser();
        existAuthUser.setUserName(authUserBO.getUserName());
        List<AuthUser> existUser = authUserService.queryByCondition(existAuthUser);
        if(existUser.size() > 0){
            return true;
        }
        // bo -> AuthUser
        AuthUser authUser = AuthUserBOConverter.INSTANCE.BoToEntity(authUserBO);
        if(authUser.getPassword() != null){
            authUser.setPassword(SaSecureUtil.md5BySalt(authUser.getPassword(), salt));
        }
        authUser.setStatus(AuthUserStatusEnums.OPEN.getCode());
        authUser.setIsDeleted(IsDeleteEnums.UN_DELETE.getCode());
        Integer countUser = authUserService.insert(authUser);
        // 建立用户和角色的初步关系
        AuthRole authRole = new AuthRole();
        authRole.setRoleKey(AuthConstants.NORMAL_USER);
        Long roleId = authRoleService.queryByCondition(authRole).getId();
        // 用户和角色的关联刷到数据库中
        AuthUserRole authUserRole = new AuthUserRole();
        authUserRole.setUserId(authUser.getId());
        authUserRole.setRoleId(roleId);
        authUserRole.setIsDeleted(IsDeleteEnums.UN_DELETE.getCode());
        Integer countUserRole = authUserRoleService.insert(authUserRole);

        // 将用户角色和权限写入缓存中
        String roleKey = redisUtil.buildKey(AuthConstants.authRolePrefix,authUser.getUserName());
        List<AuthRole> roleList = new LinkedList<>();
        roleList.add(authRole);
        redisUtil.set(roleKey,new Gson().toJson(roleList));

        String permissionKey = redisUtil.buildKey(AuthConstants.authPermissionPrefix,authUser.getUserName());
        AuthRolePermission authRolePermission = new AuthRolePermission();
        authRolePermission.setRoleId(roleId);
        List<AuthRolePermission> authRolePermissionList  =
                authRolePermissionService.queryByCondition(authRolePermission);
        List<Long> permissionIdList = authRolePermissionList.stream()
                .map(AuthRolePermission::getPermissionId).collect(Collectors.toList());

        List<AuthPermission> authPermissionList = authPermissionService.queryByCondition(permissionIdList);
        redisUtil.set(permissionKey,new Gson().toJson(authPermissionList));

        return (countUser > 0 && countUserRole > 0);
    }

    @Override
    public Boolean update(AuthUserBO authUserBO) {
        // bo -> AuthUser
        AuthUser authUser = AuthUserBOConverter.INSTANCE.BoToEntity(authUserBO);
        // 有任何更新要与缓存同步
        return authUserService.update(authUser) > 0;
    }

    @Override
    public Boolean delete(AuthUserBO authUserBO) {
        // bo -> AuthUser
        AuthUser authUser = AuthUserBOConverter.INSTANCE.BoToEntity(authUserBO);
        authUser.setIsDeleted(IsDeleteEnums.DELETE.getCode());
        // 有任何更新要与缓存同步
        return authUserService.update(authUser) > 0;
    }

    @Override
    public SaTokenInfo doLogin(String validCode) {

        // 验证码是否正确
        String loginKey = redisUtil.buildKey(LOGIN_PREFIX, validCode);
        String openId = redisUtil.get(loginKey);
        if (Strings.isBlank(openId)) {
            return null;
        }
        AuthUserBO authUserBO = new AuthUserBO();
        authUserBO.setUserName(openId);
        this.register(authUserBO);
        StpUtil.login(openId);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return tokenInfo;
    }

    @Override
    public AuthUserBO getUserInfo(AuthUserBO authUserBO) {
        AuthUser authUser = AuthUserBOConverter.INSTANCE.BoToEntity(authUserBO);
        List<AuthUser> authUsers = authUserService.queryByCondition(authUser);
        if(CollectionUtils.isEmpty(authUsers)){
            return new AuthUserBO();
        }
        AuthUser user = authUsers.get(0);
        AuthUserBO userInfoBo = AuthUserBOConverter.INSTANCE.EntityToBo(user);
        return userInfoBo;
    }
}
