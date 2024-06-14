package com.jingdianjichi.club.gateway.auth;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 权限认证配置器
 * @author click33
 */
@Component
public class SaTokenConfigure {
    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
            // 拦截地址
            .addInclude("/**")    /* 拦截全部path */
            // 鉴权方法：每次访问进入
            .setAuth(obj -> {
                System.out.println("-------- 前端访问path：" + SaHolder.getRequest().getRequestPath());
                // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
//                SaRouter.match("/auth/**", "/auth/user/doLogin", r -> StpUtil.checkRole("admin"));
                SaRouter.match("/oss/**", r -> StpUtil.checkLogin());
                SaRouter.match("/subject/subject/add",  r -> StpUtil.checkPermission("subject:add"));
                SaRouter.match("/subject/**",  r -> StpUtil.checkLogin());
            });
    }
}
