package com.jingdianjichi.practice.server.dao;


import com.jingdianjichi.practice.server.entity.dto.CategoryDTO;
import com.jingdianjichi.practice.server.entity.po.CategoryPo;
import com.jingdianjichi.practice.server.entity.po.PrimaryCategoryPO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectCategoryDao {
    List<PrimaryCategoryPO> getPrimaryCategory(CategoryDTO categoryDTO);

    CategoryPo selectById(Long id);

    List<CategoryPo> selectList(CategoryDTO categoryDTOTemp);
}
