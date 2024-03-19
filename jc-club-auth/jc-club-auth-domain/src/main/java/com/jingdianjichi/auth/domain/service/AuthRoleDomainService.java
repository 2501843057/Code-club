package com.jingdianjichi.auth.domain.service;

import com.jingdianjichi.auth.domain.entity.AuthRoleBO;

public interface AuthRoleDomainService {

    Boolean add(AuthRoleBO authRoleBO);

    Boolean update(AuthRoleBO authRoleBO);

    Boolean delete(AuthRoleBO authRoleBO);
}
