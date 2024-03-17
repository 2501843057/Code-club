package com.jingdianjichi.auth.domain.service;

import com.jingdianjichi.auth.domain.entity.AuthUserBO;

public interface AuthUserDomainService {

    Boolean register(AuthUserBO authUserBO);
}
