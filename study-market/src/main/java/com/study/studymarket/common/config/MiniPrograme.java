package com.study.studymarket.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource({"classpath:mini-program.properties"})
public class MiniPrograme {

    @Value("${mini.program.appId}")
    private String appId;
    @Value("${mini.program.appSecret}")
    private String appSecret;
    @Value("${mini.program.jscode2session.url}")
    private String jscode2sessionUrl;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getJscode2sessionUrl() {
        return jscode2sessionUrl;
    }

    public void setJscode2sessionUrl(String jscode2sessionUrl) {
        this.jscode2sessionUrl = jscode2sessionUrl;
    }

    public MiniPrograme() {
        super();
    }
}
