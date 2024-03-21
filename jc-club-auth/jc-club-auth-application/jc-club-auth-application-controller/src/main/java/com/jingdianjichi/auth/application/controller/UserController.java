package com.jingdianjichi.auth.application.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.base.Preconditions;
import com.jingdianjichi.auth.domain.entity.AuthUserBO;
import com.jingdianjichi.auth.domain.service.AuthUserDomainService;
import com.jingdianjichi.auth.application.convent.AuthUserDTOConverter;
import com.jingdianjichi.auth.application.dto.AuthUserDTO;
import com.jingdianjichi.auth.common.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/")
@Slf4j
public class UserController {
    @Resource
    private AuthUserDomainService authUserDomainService;

    /**
     * 用户注册
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("register")
    public Result<Boolean> register(@RequestBody AuthUserDTO authUserDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("UserController.register.dto:{}", JSON.toJSONString(authUserDTO));
            }

            Preconditions.checkArgument(!StringUtils.isBlank(authUserDTO.getUserName()), "用户名不能为空");
            Preconditions.checkArgument(!StringUtils.isBlank(authUserDTO.getPassword()), "密码不能为空");
            Preconditions.checkArgument(!StringUtils.isBlank(authUserDTO.getEmail()), "邮箱地址不能为空");

            // dto -> bo
            AuthUserBO authUserBO = AuthUserDTOConverter.INSTANCE.dtoToBo(authUserDTO);
            return Result.ok(authUserDomainService.register(authUserBO));
        } catch (Exception e) {
            log.error("UserController.register.error", e);
            return Result.fail("用户注册失败");
        }
    }

    /**
     * 修改用户详细
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("update")
    public Result<Boolean> update(@RequestBody AuthUserDTO authUserDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("UserController.update.dto:{}", JSON.toJSONString(authUserDTO));
            }
            Preconditions.checkNotNull(authUserDTO.getId(), "用户名Id不能为空");
            // dto -> bo
            AuthUserBO authUserBO = AuthUserDTOConverter.INSTANCE.dtoToBo(authUserDTO);
            return Result.ok(authUserDomainService.update(authUserBO));
        } catch (Exception e) {
            log.error("UserController.update.error", e);
            return Result.fail("更新用户失败");
        }
    }


    /**
     * 删除用户详细
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("delete")
    public Result<Boolean> delete(@RequestBody AuthUserDTO authUserDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("UserController.delete.dto:{}", JSON.toJSONString(authUserDTO));
            }
            Preconditions.checkNotNull(authUserDTO.getId(), "用户名Id不能为空");
            // dto -> bo
            AuthUserBO authUserBO = AuthUserDTOConverter.INSTANCE.dtoToBo(authUserDTO);
            return Result.ok(authUserDomainService.delete(authUserBO));
        } catch (Exception e) {
            log.error("UserController.delete.error", e);
            return Result.fail("删除用户失败");
        }
    }

    /**
     * 用户启用/禁用详细
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("changeStatus")
    public Result<Boolean> changeStatus(@RequestBody AuthUserDTO authUserDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("UserController.changeStatus.dto:{}", JSON.toJSONString(authUserDTO));
            }
            Preconditions.checkNotNull(authUserDTO.getId(), "用户名Id不能为空");
            // dto -> bo
            AuthUserBO authUserBO = AuthUserDTOConverter.INSTANCE.dtoToBo(authUserDTO);
            return Result.ok(authUserDomainService.update(authUserBO));
        } catch (Exception e) {
            log.error("UserController.changeStatus.error", e);
            return Result.fail("用户启用/禁用户失败");
        }
    }

    /**
     * 获取用户信息
     *
     * @param authUserDTO
     * @return
     */
    @RequestMapping("getUserInfo")
    public Result<Boolean> getUserInfo(@RequestBody AuthUserDTO authUserDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("UserController.getUserInfo.dto:{}", JSON.toJSONString(authUserDTO));
            }
            Preconditions.checkNotNull(authUserDTO.getUserName(), "用户名名称不能为空");
            // dto -> bo
            AuthUserBO authUserBO = AuthUserDTOConverter.INSTANCE.dtoToBo(authUserDTO);
            AuthUserBO userInfo  =authUserDomainService.getUserInfo(authUserBO);
            AuthUserDTO userInfoDto = AuthUserDTOConverter.INSTANCE.boToDto(userInfo);

            return Result.ok(userInfoDto);
        } catch (Exception e) {
            log.error("UserController.getUserInfo.error", e);
            return Result.fail("获取用户信息失败");
        }
    }


    /**
     * 登录
     * @param validCode
     * @return
     */
    @RequestMapping("doLogin")
    public Result doLogin(String validCode) {

        try {
            Preconditions.checkArgument(Strings.isNotBlank(validCode), "验证码不能为空");
            return Result.ok(authUserDomainService.doLogin(validCode));
        } catch (Exception e) {
            log.error("UserController.doLogin.error", e);
            return Result.fail("用户登录失败");
        }

    }


    /**
     * 登出
     *
     * @param
     * @return
     */
    @RequestMapping("logOut")
    public Result logOut(String UserName) {
        try {
            log.info("UserController.logOut.dto:{}", UserName);
            Preconditions.checkNotNull(UserName, "用户名名称不能为空");
            StpUtil.logout(UserName);
            return Result.ok();
        } catch (Exception e) {
            log.error("UserController.getUserInfo.error", e);
            return Result.fail("用户登出失败");
        }
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

}
