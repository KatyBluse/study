package com.study.studymarket.dao;

import com.study.studymarket.model.document.Unimed;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface UnimedRepository extends ElasticsearchCrudRepository<Unimed, String> , ElasticsearchRepository<Unimed, String> {

}
