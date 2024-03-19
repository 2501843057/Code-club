package com.jingdianjichi.auth.domain.service.impl;


import com.jingdianjichi.auth.common.enums.IsDeleteEnums;
import com.jingdianjichi.auth.domain.convent.AuthRoleBOConverter;
import com.jingdianjichi.auth.domain.entity.AuthRoleBO;
import com.jingdianjichi.auth.domain.service.AuthRoleDomainService;
import com.jingdianjichi.basic.entity.AuthRole;
import com.jingdianjichi.basic.service.AuthRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class AuthRoleDomainServiceImpl implements AuthRoleDomainService {

    @Resource
    private AuthRoleService authRoleService;

    @Override
    public Boolean add(AuthRoleBO authRoleBO) {
        // 转换AuthRoleBO为AuthRole
        AuthRole authRole = AuthRoleBOConverter.INSTANCE.BoToEntity(authRoleBO);
        authRole.setIsDeleted(IsDeleteEnums.UN_DELETE.getCode());
        return authRoleService.insert(authRole) > 0;
    }

    @Override
    public Boolean update(AuthRoleBO authRoleBO) {
        // 转换AuthRoleBO为AuthRole
        AuthRole authRole = AuthRoleBOConverter.INSTANCE.BoToEntity(authRoleBO);
        return authRoleService.update(authRole) > 0;
    }

    @Override
    public Boolean delete(AuthRoleBO authRoleBO) {
        // 转换AuthRoleBO为AuthRole
        AuthRole authRole = AuthRoleBOConverter.INSTANCE.BoToEntity(authRoleBO);
        authRole.setIsDeleted(IsDeleteEnums.DELETE.getCode());
        return authRoleService.update(authRole) > 0;
    }
}
