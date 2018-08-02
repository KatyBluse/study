package com.study.studymarket.service;

import com.study.studymarket.model.LearnResouce;

import java.util.List;
import java.util.Map;

public interface LearnResourceService {
    int add(LearnResouce learnResouce);
    int update(LearnResouce learnResouce);
    int deleteByIds(String[] ids);
    LearnResouce queryLearnResouceById(Long learnResouce);
//    List<LearnResouce> queryLearnResouceList(Map<String, Object> params);
}
