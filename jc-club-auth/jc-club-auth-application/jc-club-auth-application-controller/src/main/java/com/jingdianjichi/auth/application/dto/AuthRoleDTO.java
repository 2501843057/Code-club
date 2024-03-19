package com.jingdianjichi.auth.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色DTO
 *
 * @author makejava
 * @since 2024-03-17 21:09:49
 */
@Data
public class AuthRoleDTO implements Serializable {

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

