package com.jingdianjichi.infra.batic.service;

import com.jingdianjichi.infra.batic.entity.SubjectLabel;
import com.jingdianjichi.infra.batic.entity.SubjectMapping;

import java.util.ArrayList;
import java.util.List;


/**
 * 题目分类关系表(SubjectMapping)表服务接口
 *
 * @author makejava
 * @since 2024-03-07 21:23:30
 */
public interface SubjectMappingService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SubjectMapping queryById(Long id);

    /**
     * 新增数据
     *
     * @param subjectMapping 实例对象
     * @return 实例对象
     */
    SubjectMapping insert(SubjectMapping subjectMapping);

    /**
     * 修改数据
     *
     * @param subjectMapping 实例对象
     * @return 实例对象
     */
    SubjectMapping update(SubjectMapping subjectMapping);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     *  根据分类id查询标签
     */
    List<SubjectMapping> queryLabel(SubjectLabel subjectLabel);

    /**
     *  批量插入
     */
    void batchInsert(List<SubjectMapping> mappingList);

    /**
     * 根据题目id查询对应的标签
     */
    List<SubjectMapping> queryLabelId(SubjectMapping subjectMapping);


    int getCount(Long id);
}
