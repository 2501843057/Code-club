package com.jingdianjichi.practice.server.service.impl;

import com.jingdianjichi.practice.api.enums.CompleteStatusEnum;
import com.jingdianjichi.practice.api.enums.IsDeleteEnums;
import com.jingdianjichi.practice.api.enums.SubjectTypeEnums;
import com.jingdianjichi.practice.api.req.GetPracticeSubjectsReq;
import com.jingdianjichi.practice.server.dao.*;
import com.jingdianjichi.practice.server.entity.dto.CategoryDTO;
import com.jingdianjichi.practice.server.entity.dto.PracticeSubjectDTO;
import com.jingdianjichi.practice.server.entity.po.*;
import com.jingdianjichi.practice.server.service.PracticeSetService;
import com.jingdianjichi.practice.server.vo.*;
import com.jingdianjichi.subject.common.util.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class PracticeSetServiceImpl implements PracticeSetService {

    @Resource
    private SubjectCategoryDao subjectCategoryDao;

    @Resource
    private SubjectMappingDao subjectMappingDao;

    @Resource
    private SubjectLabelDao subjectLabelDao;

    @Resource
    private PracticeSetDao practiceSetDao;

    @Resource
    private PracticeSetDetailDao practiceSetDetailDao;

    @Resource
    private SubjectDao subjectDao;

    @Resource
    private SubjectRadioDao subjectRadioDao;

    @Resource
    private SubjectMultipleDao subjectMultipleDao;

    @Resource
    private PracticeDetailDao practiceDetailDao;

    @Resource
    private PracticeInfoDao practiceInfoDao;


    @Override
    public List<SpecialPracticeVO> getSpecialPracticeContent() {
        List<SpecialPracticeVO> specialPracticeVOList = new LinkedList<>();
        List<Integer> subjectTypeList = new LinkedList<>();
        subjectTypeList.add(SubjectTypeEnums.RADIO.getCode());
        subjectTypeList.add(SubjectTypeEnums.MULTIPLE.getCode());
        subjectTypeList.add(SubjectTypeEnums.JUDGE.getCode());
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setSubjectTypeList(subjectTypeList);
        // 查出大类
        List<PrimaryCategoryPO> poList = subjectCategoryDao.getPrimaryCategory(categoryDTO);
        if (CollectionUtils.isEmpty(poList)) {
            return specialPracticeVOList;
        }
        poList.forEach(primaryCategoryPO -> {
            SpecialPracticeVO specialPracticeVO = new SpecialPracticeVO();
            specialPracticeVO.setPrimaryCategoryId(primaryCategoryPO.getParentId());
            CategoryPo categoryPO = subjectCategoryDao.selectById(primaryCategoryPO.getParentId());
            specialPracticeVO.setPrimaryCategoryName(categoryPO.getCategoryName());
            CategoryDTO categoryDTOTemp = new CategoryDTO();
            categoryDTOTemp.setCategoryType(2);
            categoryDTOTemp.setParentId(categoryPO.getId());
            List<CategoryPo> smallPoList = subjectCategoryDao.selectList(categoryDTOTemp);
            if (CollectionUtils.isEmpty(smallPoList)) {
                return;
            }
            List<SpecialPracticeCategoryVO> categoryList = new LinkedList();
            smallPoList.forEach(smallPo -> {
                List<SpecialPracticeLabelVO> labelVOList = getLabelVOList(smallPo.getId(), subjectTypeList);
                if (CollectionUtils.isEmpty(labelVOList)) {
                    return;
                }
                SpecialPracticeCategoryVO specialPracticeCategoryVO = new SpecialPracticeCategoryVO();
                specialPracticeCategoryVO.setCategoryId(smallPo.getId());
                specialPracticeCategoryVO.setCategoryName(smallPo.getCategoryName());
                List<SpecialPracticeLabelVO> labelList = new LinkedList<>();
                labelVOList.forEach(labelVo -> {
                    SpecialPracticeLabelVO specialPracticeLabelVO = new SpecialPracticeLabelVO();
                    specialPracticeLabelVO.setId(labelVo.getId());
                    specialPracticeLabelVO.setAssembleId(labelVo.getAssembleId());
                    specialPracticeLabelVO.setLabelName(labelVo.getLabelName());
                    labelList.add(specialPracticeLabelVO);
                });
                specialPracticeCategoryVO.setLabelList(labelList);
                categoryList.add(specialPracticeCategoryVO);
            });
            specialPracticeVO.setCategoryList(categoryList);
            specialPracticeVOList.add(specialPracticeVO);
        });
        return specialPracticeVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PracticeSetVO addPractice(PracticeSubjectDTO dto) {
        PracticeSetVO practiceSetVO = new PracticeSetVO();
        // 查询到套卷中的所有题目
        List<PracticeSubjectDetailVO> practiceList = getPracticeList(dto);
        if(CollectionUtils.isEmpty(practiceList)){
            return practiceSetVO;
        }

        // 添加practice_set,practice_set_detail
        PracticeSetPO practiceSetPO = new PracticeSetPO();
        List<String> assembleIds = dto.getAssembleIds();
        HashSet<Long> categoryIdSet = new HashSet<>();
        assembleIds.forEach(assembleId->{
            String categoryId = assembleId.split("-")[0];
            categoryIdSet.add(Long.valueOf(categoryId));
        });

        // 编辑套卷名称
        StringBuffer setName = new StringBuffer();
        int i =1;
        for(Long categoryId:categoryIdSet){
            if(i > 2){
                break;
            }
            CategoryPo categoryPo = subjectCategoryDao.selectById(categoryId);
            setName.append(categoryPo.getCategoryName());
            setName.append("、");
            i = i + 1;
        }
        setName.deleteCharAt(setName.length() - 1);
        if (i == 2) {
            setName.append("专项练习");
        } else {
            setName.append("等专项练习");
        }

        String labelId = assembleIds.get(0).split("-")[1];
        SubjectLabelPO subjectLabelPO = subjectLabelDao.queryById(Long.valueOf(labelId));

        practiceSetPO.setPrimaryCategoryId(subjectLabelPO.getCategoryId());
        practiceSetPO.setSetName(setName.toString());
        practiceSetPO.setSetType(1);
        practiceSetPO.setIsDeleted(IsDeleteEnums.UN_DELETE.code);
        practiceSetPO.setCreatedTime(new Date());
        practiceSetPO.setCreatedBy(LoginUtil.getLoginId());
        practiceSetDao.add(practiceSetPO);
        Long practiceSetId = practiceSetPO.getId();

        List<PracticeSetDetailPO> practiceSetDetailPOS = new ArrayList<>();
        practiceList.forEach(e -> {
            PracticeSetDetailPO detailPO = new PracticeSetDetailPO();
            detailPO.setSetId(practiceSetId);
            detailPO.setSubjectId(e.getSubjectId());
            detailPO.setSubjectType(e.getSubjectType());
            detailPO.setIsDeleted(IsDeleteEnums.UN_DELETE.getCode());
            detailPO.setCreatedBy(LoginUtil.getLoginId());
            detailPO.setCreatedTime(new Date());
            practiceSetDetailPOS.add(detailPO);
        });
        practiceSetDetailDao.insertBatch(practiceSetDetailPOS);

        practiceSetVO.setSetId(practiceSetId);
        return practiceSetVO;
    }

    @Override
    public PracticeSubjectListVO getSubjects(GetPracticeSubjectsReq req) {
        Long titleId = req.getTitleId();
        Long practiceId = req.getPracticeId();
        String loginId = LoginUtil.getLoginId();
        List<PracticeSetDetailPO> practiceSetDetailPOS = practiceSetDetailDao.getSubjects(titleId);
        if(CollectionUtils.isEmpty(practiceSetDetailPOS)){
            return null;
        }

        ArrayList<PracticeSubjectDetailVO> practiceSubjectDetailVOS = new ArrayList<>();
        practiceSetDetailPOS.forEach(item->{
            PracticeSubjectDetailVO practiceSubjectDetailVO = new PracticeSubjectDetailVO();
            practiceSubjectDetailVO.setSubjectId(item.getSubjectId());
            practiceSubjectDetailVO.setSubjectType(item.getSubjectType());
            if(Objects.nonNull(practiceId)){
                PracticeDetailPO practiceDetailPO = practiceDetailDao.selectDetail(loginId,practiceId,item.getSubjectId());
                if(Objects.nonNull(practiceDetailPO) && Strings.isNotBlank(practiceDetailPO.getAnswerContent())){
                    practiceSubjectDetailVO.setIsAnswer(1);
                }else{
                    practiceSubjectDetailVO.setIsAnswer(0);
                }
            }
            practiceSubjectDetailVOS.add(practiceSubjectDetailVO);
        });

        PracticeSubjectListVO practiceSubjectListVO = new PracticeSubjectListVO();
        practiceSubjectListVO.setPracticeSubjectDetailVOS(practiceSubjectDetailVOS);
        PracticeSetPO practiceSetPo = practiceSetDao.getTitleName(titleId);
        practiceSubjectListVO.setTitle(practiceSetPo.getSetName());

        // 第一次创建练习practice_info
        if(Objects.isNull(practiceId)){
            Long newPracticeId = insertUnCompletePractice(titleId);
            practiceSubjectListVO.setPracticeId(newPracticeId);
        }else{
            updateUnCompletePractice(practiceId);
            PracticeInfoPO po = practiceInfoDao.selectById(practiceId);
            practiceSubjectListVO.setTimeUse(po.getTimeUse());
            practiceSubjectListVO.setPracticeId(practiceId);
        }

        return practiceSubjectListVO;
    }

    private Long insertUnCompletePractice(Long practiceSetId) {
        PracticeInfoPO practicePO = new PracticeInfoPO();
        practicePO.setSetId(practiceSetId);
        practicePO.setCompleteStatus(CompleteStatusEnum.NO_COMPLETE.getCode());
        practicePO.setTimeUse("00:00:00");
        practicePO.setSubmitTime(new Date());
        practicePO.setCorrectRate(new BigDecimal("0.00"));
        practicePO.setIsDeleted(IsDeleteEnums.UN_DELETE.getCode());
        practicePO.setCreatedBy(LoginUtil.getLoginId());
        practicePO.setCreatedTime(new Date());
        practiceInfoDao.insert(practicePO);
        return practicePO.getId();
    }

    private void updateUnCompletePractice(Long practiceId) {
        PracticeInfoPO practicePO = new PracticeInfoPO();
        practicePO.setId(practiceId);
        practicePO.setSubmitTime(new Date());
        practiceInfoDao.update(practicePO);
    }


    @Override
    public PracticeSubjectVO getPracticeSubject(PracticeSubjectDTO dto) {
        PracticeSubjectVO practiceSubjectVO = new PracticeSubjectVO();
        Long subjectId = dto.getSubjectId();

        SubjectPO subjectPO = subjectDao.getSubjectById(subjectId);
        practiceSubjectVO.setSubjectName(subjectPO.getSubjectName());
        practiceSubjectVO.setSubjectType(subjectPO.getSubjectType());
        // 单选
        if(dto.getSubjectType() == SubjectTypeEnums.RADIO.getCode()){
            List<PracticeSubjectOptionVO> optionList = new ArrayList<>();
            List<SubjectRadioPo> subjectRadioPos = subjectRadioDao.selectBySubjectId(subjectId);
            subjectRadioPos.forEach(item->{
                PracticeSubjectOptionVO practiceSubjectOptionVO = new PracticeSubjectOptionVO();
                practiceSubjectOptionVO.setOptionType(item.getOptionType());
                practiceSubjectOptionVO.setOptionContent(item.getOptionContent());
                practiceSubjectOptionVO.setIsCorrect(item.getIsCorrect());
                optionList.add(practiceSubjectOptionVO);
            });
            practiceSubjectVO.setOptions(optionList);
        }
        // 多选
        if(dto.getSubjectType() == SubjectTypeEnums.MULTIPLE.getCode()){
            List<PracticeSubjectOptionVO> optionList = new ArrayList<>();
            List<SubjectMultiplePO> subjectRadioPos = subjectMultipleDao.selectBySubjectId(subjectId);
            subjectRadioPos.forEach(item->{
                PracticeSubjectOptionVO practiceSubjectOptionVO = new PracticeSubjectOptionVO();
                practiceSubjectOptionVO.setOptionType(item.getOptionType());
                practiceSubjectOptionVO.setOptionContent(item.getOptionContent());
                practiceSubjectOptionVO.setIsCorrect(item.getIsCorrect());
                optionList.add(practiceSubjectOptionVO);
            });
            practiceSubjectVO.setOptions(optionList);
        }
        return practiceSubjectVO;
    }

    private List<PracticeSubjectDetailVO> getPracticeList(PracticeSubjectDTO dto) {
        List<PracticeSubjectDetailVO> practiceSubjectListVOS = new LinkedList<>();
        //避免重复
        List<Long> excludeSubjectIds = new LinkedList<>();

        //设置题目数量，之后优化到nacos动态配置
        Integer radioSubjectCount = 10;
        Integer multipleSubjectCount = 6;
        Integer judgeSubjectCount = 4;
        Integer totalSubjectCount = 20;

        // 单选
        dto.setSubjectCount(radioSubjectCount);
        dto.setSubjectType(SubjectTypeEnums.RADIO.getCode());
        assembleList(dto,practiceSubjectListVOS,excludeSubjectIds);
        // 多选
        dto.setSubjectCount(multipleSubjectCount);
        dto.setSubjectType(SubjectTypeEnums.MULTIPLE.getCode());
        assembleList(dto,practiceSubjectListVOS,excludeSubjectIds);
        // 判断
        dto.setSubjectCount(judgeSubjectCount);
        dto.setSubjectType(SubjectTypeEnums.JUDGE.getCode());
        assembleList(dto,practiceSubjectListVOS,excludeSubjectIds);

        //补充题目
        if (practiceSubjectListVOS.size() == totalSubjectCount) {
            return practiceSubjectListVOS;
        }
        Integer remainCount = totalSubjectCount - practiceSubjectListVOS.size();
        dto.setSubjectCount(remainCount);
        dto.setSubjectType(1);
        assembleList(dto, practiceSubjectListVOS, excludeSubjectIds);
        return practiceSubjectListVOS;
    }

    private List<PracticeSubjectDetailVO> assembleList(PracticeSubjectDTO dto,
                                                       List<PracticeSubjectDetailVO> list,
                                                       List<Long> excludeSubjectIds) {
        dto.setExcludeSubjectIds(excludeSubjectIds);
        List<SubjectPO> subjectPOList = subjectDao.getPracticeSubject(dto);
        if (CollectionUtils.isEmpty(subjectPOList)) {
            return list;
        }
        subjectPOList.forEach(e -> {
            PracticeSubjectDetailVO vo = new PracticeSubjectDetailVO();
            vo.setSubjectId(e.getId());
            vo.setSubjectType(e.getSubjectType());
            excludeSubjectIds.add(e.getId());
            list.add(vo);
        });
        return list;
    }

    private List<SpecialPracticeLabelVO> getLabelVOList(Long categoryId, List<Integer> subjectTypeList) {
        List<LabelCountPO> countPOList = subjectMappingDao.getLabelSubjectCount(categoryId, subjectTypeList);
        if(CollectionUtils.isEmpty(countPOList)){
            return Collections.emptyList();
        }
        List<SpecialPracticeLabelVO> voList = new LinkedList<>();
        countPOList.forEach(countPo->{
            SpecialPracticeLabelVO vo = new SpecialPracticeLabelVO();
            vo.setId(countPo.getLabelId());
            vo.setAssembleId(categoryId + "-" + countPo.getLabelId());
            SubjectLabelPO subjectLabelPO = subjectLabelDao.queryById(countPo.getLabelId());
            vo.setLabelName(subjectLabelPO.getLabelName());
            voList.add(vo);
        });
        return voList;
    }

}
