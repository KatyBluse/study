package com.study.studymarket.service.impl;

import com.study.studymarket.dao.UnimedRepository;
import com.study.studymarket.model.document.Unimed;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UnimedServiceImpl {

    @Resource
    private UnimedRepository unimedRepository;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;


    public List<Unimed> matchAll() {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        List<Unimed> result = elasticsearchTemplate.queryForList(searchQuery, Unimed.class);
        if (!CollectionUtils.isEmpty(result)) {
            return result;
        }
        return null;
    }

    public Unimed add(Unimed unimed) {
        Unimed result = unimedRepository.save(unimed);
        return result;
    }

}
