package com.jingdianjichi.auth.domain.service.impl;

import com.jingdianjichi.auth.common.enums.AuthUserStatusEnums;
import com.jingdianjichi.auth.common.enums.IsDeleteEnums;
import com.jingdianjichi.auth.domain.entity.AuthUserBO;
import com.jingdianjichi.auth.domain.service.AuthUserDomainService;
import com.jingdianjichi.auth.domain.convent.AuthUserBOConverter;
import com.jingdianjichi.basic.entity.AuthUser;
import com.jingdianjichi.basic.service.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class AuthUserDomainServiceImpl implements AuthUserDomainService {

    @Resource
    private AuthUserService authUserService;

    @Override
    public Boolean register(AuthUserBO authUserBO) {
        // bo -> AuthUser
        AuthUser authUser = AuthUserBOConverter.INSTANCE.BoToEntity(authUserBO);
        authUser.setStatus(AuthUserStatusEnums.OPEN.getCode());
        authUser.setIsDeleted(IsDeleteEnums.UN_DELETE.getCode());
        // 建立用户和角色的初步关系
        // 用户和权限刷到数据库中
        return authUserService.insert(authUser) > 0;
    }
}
