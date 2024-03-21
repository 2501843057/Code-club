package com.jingdianjichi.auth.domain.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.jingdianjichi.auth.domain.entity.AuthRoleBO;
import com.jingdianjichi.auth.domain.entity.AuthUserBO;

public interface AuthUserDomainService {

    Boolean register(AuthUserBO authUserBO);

    Boolean update(AuthUserBO authUserBO);

    Boolean delete(AuthUserBO authUserBO);

    SaTokenInfo doLogin(String validCode);

    AuthUserBO getUserInfo(AuthUserBO authUserBO);
}
