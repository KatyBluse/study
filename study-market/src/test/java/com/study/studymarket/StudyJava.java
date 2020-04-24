package com.study.studymarket;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StudyJava {

    /**
     * Lambda表达式使用
     * Stream使用
     */

    @Test
    public void filter() {
//        用于过滤，相当于又一个if条件
        List<Integer> integers  = Arrays.asList(4,54,542,52,54,35,2,5,25,42,542,35,2,52,354);
        List<Integer> filterParams = integers.stream().filter(i -> i > 42).collect(Collectors.toList());
        System.out.println("filterParams:" + JSONArray.toJSONString(filterParams));
    }


    @Test
    public void map() {
//        用于映射结果， 相当于直接操作参数
        List<Integer> integers  = Arrays.asList(4,54,542,52,54,35,2,5,25,42,542,35,2,52,354);
        List<Integer> filterParams = integers.stream().map(i -> i * 2).collect(Collectors.toList());
        System.out.println("mapParams:" + JSONArray.toJSONString(filterParams));
    }

    @Test
    public void limit() {
//        取指定数量
        List<Integer> integers  = Arrays.asList(1,2,3,4,5,6,7,8,9,42,542,35,2,52,354);
        List<Integer> filterParams = integers.stream().limit(3).map(i -> i * 2).collect(Collectors.toList());
        System.out.println("mapParams:" + JSONArray.toJSONString(filterParams));
    }

    @Test
    public void sorted() {
//        取指定数量
        List<Integer> integers  = Arrays.asList(4,54,542,52,54,35,2,5,25,42,542,35,2,52,354);
        List<Integer> filterParams = integers.stream().map(i -> i * 2).limit(3).sorted().collect(Collectors.toList());
        System.out.println("mapParams:" + JSONArray.toJSONString(filterParams));
    }

    @Test
    public void distinct() {
//        去重
        List<Integer> integers  = Arrays.asList(4,54,542,52,54,35,2,5,25,42,542,35,1,1,1);
        List<Integer> filterParams = integers.stream().sorted().distinct().collect(Collectors.toList());
        System.out.println("mapParams:" + JSONArray.toJSONString(filterParams));
    }

    @Test
    public void joining() {
//        字符串连接
        List<String> integers  = Arrays.asList("4","54","542");
        String result = integers.stream().collect(Collectors.joining(","));
        System.out.println("result:" + result);
    }

    @Test
    public void intSummaryStatistics() {
        List<Integer> integers  = Arrays.asList(4,54,542,52,54,35,2,5,25,42,542,35,1,1,1);
        IntSummaryStatistics stats = integers.stream().mapToInt((x) -> x).summaryStatistics();
        log.info("最大值：{}" , stats.getMax());
        log.info("最小值：{}" , stats.getMin());
        log.info("所有数之和：{}" , stats.getSum());
        log.info("平均数：{}" , stats.getAverage());
        Long count = integers.parallelStream().count();
        log.info("平均数：{}" , count);
    }

}
