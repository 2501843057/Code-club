package com.jingdianjichi.auth.domain.convent;

import com.jingdianjichi.auth.domain.entity.AuthRoleBO;

import com.jingdianjichi.basic.entity.AuthRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthRoleBOConverter {

    AuthRoleBOConverter INSTANCE = Mappers.getMapper(AuthRoleBOConverter.class);

    AuthRole BoToEntity(AuthRoleBO authRoleBO);
}
