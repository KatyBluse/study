package com.study.studymarket.common.util.elasticSearch.model;

import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

@Data
public class Sort {

    private String column;

    private SortOrder sortOrder;
}
