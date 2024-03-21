package com.jingdianjichi.basic.service.impl;

import com.jingdianjichi.basic.entity.AuthUser;
import com.jingdianjichi.basic.mapper.AuthUserDao;
import com.jingdianjichi.basic.service.AuthUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户信息表(AuthUser)表服务实现类
 *
 * @author makejava
 * @since 2024-03-17 18:59:00
 */
@Service("authUserService")
public class AuthUserServiceImpl implements AuthUserService {
    @Resource
    private AuthUserDao authUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AuthUser queryById(Long id) {
        return this.authUserDao.queryById(id);
    }


    /**
     * 新增数据
     *
     * @param authUser 实例对象
     * @return 实例对象
     */
    @Override
    public Integer insert(AuthUser authUser) {
        return this.authUserDao.insert(authUser);
    }

    /**
     * 修改数据
     *
     * @param authUser 实例对象
     * @return 实例对象
     */
    @Override
    public Integer update(AuthUser authUser) {
        return this.authUserDao.update(authUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.authUserDao.deleteById(id) > 0;
    }

    @Override
    public Boolean queryIsExist(String openId) {
        return this.authUserDao.queryIsExist(openId) > 0;
    }

    @Override
    public List<AuthUser> queryByCondition(AuthUser existAuthUser) {
        return this.authUserDao.queryAllByLimit(existAuthUser);
    }
}
