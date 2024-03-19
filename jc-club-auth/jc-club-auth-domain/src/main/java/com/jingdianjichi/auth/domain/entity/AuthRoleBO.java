package com.jingdianjichi.auth.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色DTO
 *
 * @author makejava
 * @since 2024-03-17 21:09:49
 */
@Data
public class AuthRoleBO implements Serializable {

    private Long id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色唯一标识
     */
    private String roleKey;


}

