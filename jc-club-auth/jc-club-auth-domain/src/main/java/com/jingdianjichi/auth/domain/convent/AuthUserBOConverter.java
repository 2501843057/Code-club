package com.jingdianjichi.auth.domain.convent;

import com.jingdianjichi.auth.domain.entity.AuthUserBO;
import com.jingdianjichi.basic.entity.AuthUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthUserBOConverter {

    AuthUserBOConverter INSTANCE = Mappers.getMapper(AuthUserBOConverter.class);

    AuthUser BoToEntity(AuthUserBO authUserBO);
}
