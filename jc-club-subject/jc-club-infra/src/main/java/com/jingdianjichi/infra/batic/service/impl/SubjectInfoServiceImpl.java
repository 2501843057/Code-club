package com.jingdianjichi.infra.batic.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jingdianjichi.infra.batic.entity.SubjectInfo;
import com.jingdianjichi.infra.batic.entity.SubjectLabel;
import com.jingdianjichi.infra.batic.mapper.SubjectInfoDao;
import com.jingdianjichi.infra.batic.mapper.SubjectLabelDao;
import com.jingdianjichi.infra.batic.service.SubjectInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 题目信息表(SubjectInfo)表服务实现类
 *
 * @author makejava
 * @since 2024-03-08 20:12:20
 */
@Service("subjectInfoService")
public class SubjectInfoServiceImpl implements SubjectInfoService {
    @Resource
    private SubjectInfoDao subjectInfoDao;

    @Resource
    private SubjectLabelDao subjectLabelDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SubjectInfo queryById(Long id) {
        return this.subjectInfoDao.queryById(id);
    }


    /**
     * 新增数据
     *
     * @param subjectInfo 实例对象
     * @return 实例对象
     */
    @Override
    public SubjectInfo insert(SubjectInfo subjectInfo) {
        this.subjectInfoDao.insert(subjectInfo);
        return subjectInfo;
    }

    /**
     * 修改数据
     *
     * @param subjectInfo 实例对象
     * @return 实例对象
     */
    @Override
    public SubjectInfo update(SubjectInfo subjectInfo) {
        this.subjectInfoDao.update(subjectInfo);
        return this.queryById(subjectInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.subjectInfoDao.deleteById(id) > 0;
    }

    @Override
    public int countByCondition(SubjectInfo subjectInfo, Long categoryId, Long labelId) {
        return subjectInfoDao.countByCondition(subjectInfo,categoryId,labelId);
    }

    @Override
    public List<SubjectInfo> queryPage(SubjectInfo subjectInfo, Long categoryId, Long labelId, int start, Integer pageSize) {
        return subjectInfoDao.queryPage(subjectInfo,categoryId,labelId,start,pageSize);
    }

    @Override
    public List<SubjectInfo> batchQuery(List<Long> list) {
        return subjectInfoDao.batchQuery(list);
    }

    @Override
    public List<SubjectLabel> batchQueryById(List<Long> list) {
        return subjectLabelDao.batchQueryByLabelId(list);
    }

    @Override
    public List<SubjectInfo> getRankings() {
        return subjectInfoDao.getRankings();
    }

    @Override
    public Long querySubjectIdCursor(Long subjectId, Long categoryId, Long labelId, int cursor) {
        return this.subjectInfoDao.querySubjectIdCursor(subjectId, categoryId, labelId, cursor);
    }


}
