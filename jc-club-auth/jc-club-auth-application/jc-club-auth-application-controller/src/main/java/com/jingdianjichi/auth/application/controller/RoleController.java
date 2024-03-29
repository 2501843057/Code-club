package com.jingdianjichi.auth.application.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.base.Preconditions;
import com.jingdianjichi.auth.application.convent.AuthRoleDTOConverter;
import com.jingdianjichi.auth.application.dto.AuthRoleDTO;
import com.jingdianjichi.auth.entity.Result;
import com.jingdianjichi.auth.domain.entity.AuthRoleBO;
import com.jingdianjichi.auth.domain.service.AuthRoleDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/role/")
@Slf4j
public class RoleController {
    @Resource
    private AuthRoleDomainService authRoleDomainService;

    /**
     * 添加角色
     *
     * @param
     * @return
     */
    @RequestMapping("add")
    public Result<Boolean> add(@RequestBody AuthRoleDTO authRoleDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("RoleController.add.dto:{}", JSON.toJSONString(authRoleDTO));
            }
            Preconditions.checkArgument(!StringUtils.isBlank(authRoleDTO.getRoleKey()), "角色key不能为空");
            Preconditions.checkArgument(!StringUtils.isBlank(authRoleDTO.getRoleName()), "角色名称不能为空");

            // dto -> bo
            AuthRoleBO authRoleBO = AuthRoleDTOConverter.INSTANCE.dtoToBo(authRoleDTO);
            return Result.ok(authRoleDomainService.add(authRoleBO));
        } catch (Exception e) {
            log.error("UserController.add.error", e);
            return Result.fail("角色添加失败");
        }
    }

    /**
     * 修改角色
     *
     * @param
     * @return
     */
    @RequestMapping("update")
    public Result<Boolean> update(@RequestBody AuthRoleDTO authRoleDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("RoleController.update.dto:{}", JSON.toJSONString(authRoleDTO));
            }
            Preconditions.checkNotNull(authRoleDTO.getId(), "角色id不能为空");

            // dto -> bo
            AuthRoleBO authRoleBO = AuthRoleDTOConverter.INSTANCE.dtoToBo(authRoleDTO);
            return Result.ok(authRoleDomainService.update(authRoleBO));
        } catch (Exception e) {
            log.error("UserController.update.error", e);
            return Result.fail("角色更新加失败");
        }
    }


    /**
     * 删除角色
     *
     * @param
     * @return
     */
    @RequestMapping("delete")
    public Result<Boolean> delete(@RequestBody AuthRoleDTO authRoleDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("RoleController.delete.dto:{}", JSON.toJSONString(authRoleDTO));
            }
            Preconditions.checkNotNull(authRoleDTO.getId(), "角色id不能为空");

            // dto -> bo
            AuthRoleBO authRoleBO = AuthRoleDTOConverter.INSTANCE.dtoToBo(authRoleDTO);
            return Result.ok(authRoleDomainService.delete(authRoleBO));
        } catch (Exception e) {
            log.error("UserController.delete.error", e);
            return Result.fail("角色删除加失败");
        }
    }

}
