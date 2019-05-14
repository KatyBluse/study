package com.study.studymarket.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "elasticsearchRestClient")
@Data
public class ElasticsearchRestClient {

    @Value("${restClient.host-port}")
    private int PORT;

    @Value("${restClient.host-name}")
    public String HOST_NAME;
}
