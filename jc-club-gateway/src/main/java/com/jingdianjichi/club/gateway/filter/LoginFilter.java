package com.jingdianjichi.club.gateway.filter;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import lombok.SneakyThrows;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 登录拦截器，获取loginID
 */
@Component
public class LoginFilter implements GlobalFilter {
    @Override
    @SneakyThrows
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        String path = request.getURI().getPath();
        // 如果是登录接口，就直接放行
        if(path.equals("/user/doLogin")){
            return chain.filter(exchange);
        }
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        String loginId =(String) tokenInfo.loginId;
        if(StringUtils.isBlank(loginId)){
            throw new Exception("未获取到用户信息");
        }
        mutate.header("loginId",loginId);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }
}
