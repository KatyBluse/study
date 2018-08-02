package com.study.studymarket.common.util.weixin.miniProgram;

import com.study.studymarket.common.config.MiniPrograme;
import com.study.studymarket.common.util.weixin.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ClientUtil {

    @Autowired
    private MiniPrograme miniPrograme;

    /**
     * 获取openId 和sessionkey或者UnionID
     *
     * */
    public String getSessionKeyAndOpenIdOrUnionID(String code){

        String result = null;
        Map<String, String> param = new HashMap<>();
        param.put("appid", miniPrograme.getAppId());
        param.put("secret", miniPrograme.getAppSecret());
        param.put("js_code", code);
        param.put("grant_type","authorization_code");
        HttpClient client = new HttpClient(miniPrograme.getJscode2sessionUrl());
        try {
            client.sendPost(param,"application/x-www-form-urlencoded");
            result = client.getResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
