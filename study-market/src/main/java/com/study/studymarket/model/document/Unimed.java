package com.study.studymarket.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "test",type = "user_info", shards = 5,replicas = 0, refreshInterval = "-1")
@com.study.studymarket.common.util.elasticSearch.annotations.Document(index = "test",type = "user_info")
/**
 *     String indexName();//索引库的名称，个人建议以项目的名称命名
 *
 *     String type() default "";//类型，个人建议以实体的名称命名
 *
 *     short shards() default 5;//默认分区数
 *
 *     short replicas() default 1;//每个分区默认的备份数
 *
 *     String refreshInterval() default "1s";//刷新间隔
 *
 *     String indexStoreType() default "fs";//索引文件存储类型
 *
 * */
public class Unimed {
    @Id
    private String id;
    //    @Field
//    private String name;
//    @Field
//    private String mobile;
//    @Field
//    private String idNumber;
//    @Field(fielddata = true)
//    private String gender;
//
//    @Field(type = FieldType.Date, format = DateFormat.custom,pattern ="yyyy-MM-dd")
//    private Date bithday;

    @Field
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",timezone="GMT+:08:00")
    private Date createDate;

}
