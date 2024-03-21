package com.jingdianjichi.basic.service;

import com.jingdianjichi.basic.entity.AuthUser;

import java.util.List;

/**
 * 用户信息表(AuthUser)表服务接口
 *
 * @author makejava
 * @since 2024-03-17 18:58:59
 */
public interface AuthUserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AuthUser queryById(Long id);


    /**
     * 新增数据
     *
     * @param authUser 实例对象
     * @return 实例对象
     */
    Integer insert(AuthUser authUser);

    /**
     * 修改数据
     *
     * @param authUser 实例对象
     * @return 实例对象
     */
    Integer update(AuthUser authUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    Boolean queryIsExist(String openId);

    List<AuthUser> queryByCondition(AuthUser existAuthUser);
}
