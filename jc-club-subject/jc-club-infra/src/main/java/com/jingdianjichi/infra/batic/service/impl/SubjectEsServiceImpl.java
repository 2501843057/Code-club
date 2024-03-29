package com.jingdianjichi.infra.batic.service.impl;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jingdianjichi.infra.batic.entity.EsSubjectFields;
import com.jingdianjichi.infra.batic.entity.SubjectInfo;
import com.jingdianjichi.infra.batic.entity.SubjectInfoEs;
import com.jingdianjichi.infra.batic.es.EsIndexInfo;
import com.jingdianjichi.infra.batic.es.EsRestClient;
import com.jingdianjichi.infra.batic.es.EsSearchRequest;
import com.jingdianjichi.infra.batic.es.EsSourceData;
import com.jingdianjichi.infra.batic.service.SubjectEsService;
import com.jingdianjichi.subject.common.entity.PageResult;
import com.jingdianjichi.subject.common.enums.SubjectTypeEnums;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class SubjectEsServiceImpl implements SubjectEsService {

    @Override
    public boolean insert(SubjectInfoEs subjectInfoEs) {
        EsSourceData esSourceData = new EsSourceData();
        Map<String,Object> data = convert2EsSourceData(subjectInfoEs);
        esSourceData.setData(data);
        esSourceData.setDocId(subjectInfoEs.getDocId().toString());
        return EsRestClient.insertDoc(getEsIndexInfo(),esSourceData);
    }

    private Map<String, Object> convert2EsSourceData(SubjectInfoEs subjectInfoEs) {
        HashMap<String, Object> data = new HashMap<>();
        data.put(EsSubjectFields.SUBJECT_ID, subjectInfoEs.getSubjectId());
        data.put(EsSubjectFields.DOC_ID, subjectInfoEs.getDocId());
        data.put(EsSubjectFields.SUBJECT_NAME, subjectInfoEs.getSubjectName());
        data.put(EsSubjectFields.SUBJECT_ANSWER, subjectInfoEs.getSubjectAnswer());
        data.put(EsSubjectFields.SUBJECT_TYPE, subjectInfoEs.getSubjectType());
        data.put(EsSubjectFields.CREATE_USER, subjectInfoEs.getCreateUser());
        data.put(EsSubjectFields.CREATE_TIME, subjectInfoEs.getCreateTime());
        return data;
    }

    @Override
    public PageResult<SubjectInfoEs> querySubjectList(SubjectInfoEs req) {
        // 初始化分页结果对象
        PageResult<SubjectInfoEs> pageResult = new PageResult<>();
        // 创建搜索请求对象
        EsSearchRequest esSearchRequest = createSearchListQuery(req);
        // 向Elasticsearch发送搜索请求并获取响应
        SearchResponse searchResponse = EsRestClient.searchWithTermQuery(getEsIndexInfo(), esSearchRequest);

        // 初始化主题信息列表
        List<SubjectInfoEs> subjectInfoEsList = new LinkedList<>();
        // 获取搜索命中的结果
        SearchHits searchHits = searchResponse.getHits();
        // 如果没有搜索到任何命中结果，则设置分页结果并返回
        if(searchHits == null || searchHits.getHits() == null){
            pageResult.setPageSize(req.getPageSize());
            pageResult.setPageNo(req.getPageNo());
            pageResult.setRecords(subjectInfoEsList);
            pageResult.setTotal(0);
            return pageResult;
        }

        // 遍历搜索命中的每一个结果，并将其转换为SubjectInfoEs对象
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            SubjectInfoEs subjectInfoEs = conventResult(hit);
            if(Objects.nonNull(subjectInfoEs)){
                subjectInfoEsList.add(subjectInfoEs);
            }
        }
        // 设置分页结果的页大小、页码、记录列表和总记录数
        pageResult.setPageSize(req.getPageSize());
        pageResult.setPageNo(req.getPageNo());
        pageResult.setRecords(subjectInfoEsList);
        // 将搜索命中的总数量转换为整型，并设置到分页结果中
        pageResult.setTotal(Long.valueOf(searchHits.getTotalHits().value).intValue());
        return pageResult;
    }


    /**
     * 将搜索命中的结果hit转换为SubjectInfoEs对象
     */
    private SubjectInfoEs conventResult(SearchHit hit) {
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        if(CollectionUtils.isEmpty(sourceAsMap)){
            return null;
        }
        SubjectInfoEs result = new SubjectInfoEs();
        result.setSubjectId(MapUtils.getLong(sourceAsMap, EsSubjectFields.SUBJECT_ID));
        result.setSubjectAnswer(MapUtils.getString(sourceAsMap, EsSubjectFields.SUBJECT_ANSWER));
        result.setSubjectType(MapUtils.getInteger(sourceAsMap, EsSubjectFields.SUBJECT_TYPE));
        result.setSubjectName(MapUtils.getString(sourceAsMap, EsSubjectFields.SUBJECT_NAME));
        result.setCreateUser(MapUtils.getString(sourceAsMap, EsSubjectFields.CREATE_USER));
        result.setScore(new BigDecimal(String.valueOf(hit.getScore())).multiply(new BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP));

        // 处理name的高亮
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        HighlightField subjectNameField = highlightFields.get(EsSubjectFields.SUBJECT_NAME);
        if(Objects.nonNull(subjectNameField)){
            Text[] fragments = subjectNameField.getFragments();
            StringBuilder stringBuilder = new StringBuilder();
            for(Text fragment : fragments){
                stringBuilder.append(fragment.toString());
            }
            result.setSubjectName(stringBuilder.toString());
        }

        // 处理answer的高亮
        HighlightField subjectAnswerField = highlightFields.get(EsSubjectFields.SUBJECT_ANSWER);
        if(Objects.nonNull(subjectAnswerField)){
            Text[] fragments = subjectAnswerField.getFragments();
            StringBuilder stringBuilder = new StringBuilder();
            for(Text fragment : fragments){
                stringBuilder.append(fragment.toString());
            }
            result.setSubjectAnswer(stringBuilder.toString());
        }
        return result;
    }

    /**
     * 创建搜索请求对象
     */
    private EsSearchRequest createSearchListQuery(SubjectInfoEs req) {
        EsSearchRequest esSearchRequest = new EsSearchRequest();
        BoolQueryBuilder bq = new BoolQueryBuilder();
        // 标题的查询条件
        MatchQueryBuilder subjectNameQueryBuilder =
                QueryBuilders.matchQuery(EsSubjectFields.SUBJECT_NAME, req.getKeyWord());
        bq.should(subjectNameQueryBuilder);
        // 提高优先级
        bq.boost(2);

        // 答案的查询条件
        MatchQueryBuilder subjectAnswerQueryBuilder =
                QueryBuilders.matchQuery(EsSubjectFields.SUBJECT_ANSWER,req.getKeyWord());
        bq.should(subjectAnswerQueryBuilder);

        // 类型的查询条件
        MatchQueryBuilder subjectTypeQueryBuilder =
                QueryBuilders.matchQuery(EsSubjectFields.SUBJECT_TYPE, SubjectTypeEnums.BRIEF.getCode());
        bq.must(subjectTypeQueryBuilder);
        bq.minimumShouldMatch(1);

        // 设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");

        esSearchRequest.setBq(bq);
        esSearchRequest.setHighlightBuilder(highlightBuilder);
        esSearchRequest.setNeedScroll(false);
        esSearchRequest.setFrom((req.getPageNo() - 1) * req.getPageSize());
        esSearchRequest.setSize(req.getPageSize());
        esSearchRequest.setFields(EsSubjectFields.FIELD_QUERY);

        return esSearchRequest;
    }

    private EsIndexInfo getEsIndexInfo(){
        EsIndexInfo esIndexInfo = new EsIndexInfo();
        esIndexInfo.setClusterName("LAPTOP-4PRLTB0G");
        esIndexInfo.setIndexName("subject_index");
        return esIndexInfo;
    }
}
