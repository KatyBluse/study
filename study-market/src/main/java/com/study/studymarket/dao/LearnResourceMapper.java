package com.study.studymarket.dao;

import com.study.studymarket.model.LearnResouce;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LearnResourceMapper {

    int add(LearnResouce learnResouce);

    int update(LearnResouce learnResouce);

    int deleteByIds(String[] ids);

    LearnResouce queryLearnResouceById(Long id);

    List<LearnResouce> queryLearnResouceList(Map<String, Object> params);
}
