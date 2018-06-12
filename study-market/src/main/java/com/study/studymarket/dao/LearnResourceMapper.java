package com.study.studymarket.dao;

import com.study.studymarket.model.LearnResouce;

import java.util.List;
import java.util.Map;

public interface LearnResourceMapper {

    int add(LearnResouce learnResouce);

    int update(LearnResouce learnResouce);

    int deleteByIds(String[] ids);

    LearnResouce queryLearnResouceById(Long id);

    List<LearnResouce> queryLearnResouceList(Map<String, Object> params);
}
