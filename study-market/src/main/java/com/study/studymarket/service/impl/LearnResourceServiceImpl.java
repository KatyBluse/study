package com.study.studymarket.service.impl;

import com.study.studymarket.dao.LearnResourceMapper;
import com.study.studymarket.model.LearnResouce;
import com.study.studymarket.service.LearnResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LearnResourceServiceImpl implements LearnResourceService {

    @Resource
    private LearnResourceMapper learnResourceMapper;

    @Override
    public int add(LearnResouce learnResouce) {
        return learnResourceMapper.add(learnResouce);
    }

    @Override
    public int update(LearnResouce learnResouce) {
        return 0;
    }

    @Override
    public int deleteByIds(String[] ids) {
        return 0;
    }

    @Override
    public LearnResouce queryLearnResouceById(Long learnResouce) {
        return null;
    }
}
