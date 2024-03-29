package com.jingdianjichi.auth.application.convent;

import com.jingdianjichi.auth.domain.entity.AuthUserBO;
import com.jingdianjichi.auth.entity.AuthUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthUserDTOConverter {

    AuthUserDTOConverter INSTANCE = Mappers.getMapper(AuthUserDTOConverter.class);

    AuthUserBO dtoToBo(AuthUserDTO authUserDTO);

    AuthUserDTO boToDto(AuthUserBO authUserBO);;
}
