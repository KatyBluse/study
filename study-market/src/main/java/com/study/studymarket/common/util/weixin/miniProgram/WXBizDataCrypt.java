package com.study.studymarket.common.util.weixin.miniProgram;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WXBizDataCrypt {
    public static String illegalAesKey = "-41001";//非法密钥
    public static String illegalIv = "-41002";//非法初始向量
    public static String illegalBuffer = "-41003";//非法密文
    public static String decodeBase64Error = "-41004"; //解码错误
    public static String noData = "-41005"; //数据不正确
    private String appId;
    private String sessionKey;

    public WXBizDataCrypt(String appId, String sessionKey) {
        this.appId = appId;
        this.sessionKey = sessionKey;
    }

    public String decryptData(String encryptedData, String iv) {
        if (StringUtils.length(sessionKey) != 24) {
            return illegalAesKey;
        }
        //
        byte[] base64Key = Base64.decodeBase64(sessionKey);

        if (StringUtils.length(iv) != 24) {
            return illegalIv;
        }

        byte[] base64Iv = Base64.decodeBase64(iv);

        byte[] base64EncryptedData =  Base64.decodeBase64(encryptedData);

        try {
            byte[] aesResult = AESUtils.decrypt(base64Key, base64Iv, base64EncryptedData);
            if (null != aesResult && aesResult.length > 0) {
                String userInfo = new String(aesResult, "UTF-8");
                JSONObject jsons = JSON.parseObject(userInfo);
                String id = jsons.getJSONObject("watermark").getString("appid");
                if (!StringUtils.equals(id, appId)) {
                    return illegalBuffer;
                }
                return userInfo;
            } else {
                return noData;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据完整性校验
     *
     * 通过调用接口（如 wx.getUserInfo）获取数据时，接口会同时返回 rawData、signature，其中 signature = sha1( rawData + session_key )
     * 开发者将 signature、rawData 发送到开发者服务器进行校验。服务器利用用户对应的 session_key 使用相同的算法计算出签名 signature2 ，比对 signature 与 signature2 即可校验数据的完整性。
     *
     */
    public static boolean checkSignature(String json, String signature, String sessionKey) {
        boolean checkResult = false;
        net.sf.json.JSON jsonObject = net.sf.json.JSONObject.fromObject(json);
        if (((net.sf.json.JSONObject) jsonObject).has("watermark")) {
            ((net.sf.json.JSONObject) jsonObject).discard("watermark");
        }
        String rawData = jsonObject.toString();
        String signStr = rawData + sessionKey;
        System.out.println("signStr:"+signStr);
        String result = SHA1.encode(signStr);
        System.out.println("signature:" + signature);
        System.out.println("result   :" + result);
        return checkResult;
    }

    public static void main(String[] args) {
//        WXBizDataCrypt wxBizDataCrypt = new WXBizDataCrypt("wx836dd89f7951e8a6", "9bSA60sLP7XhUgydp4jIYQ==");
//        String encryptedData ="KBkRCuukLFeoh9XvC4zOudZt8op/98qA48IKME96t4S1HKz0D+2R4NQxhx9PxN3/p7bDhrksVcdr1Uz3v2xtsJZIIVJgazoBXvvwkCT87iAhvG8zeDgw1elvBF6FJ6X1O2In7oYy/qjpNKAKj/3v78EXvMboYC0N1XBv4AvieQKwqHChdS9xFEId1VWr8Nae8nD7CFHgOJY1WrlmE/0DwyOSmLFnB5bwGGZ9LyuA0//vJuvMrJMOvdarsPC+UtivY5YDVSn2HxB5inuWBpPT0w9KSxTrcHNVtzw3DIWGOlj9eLMgL9hLiaRlt6sV07rBPIgiggvqIo+La6SsSRPkz2dF6ZNTgcW/k3gL2az5u5r2S7axyHEdyRAgw6R2cpAV+uUls0DGvNXxe+azusrxKw3eeKiy0GV8M+P9PautWrryp0Z7dWfucVtSoasxyKCqnEni+4nbITmVq16NIXt80vPNiupNnF2nzN1tj57DH1w=";
//        String iv = "jVeB5NAciUr0/DSGRlacNA==";
//        String result = wxBizDataCrypt.decryptData(encryptedData, iv);
//        System.out.println(result);

//        String rawData = "{\"nickName\":\"南湘梓木\",\"gender\":1,\"language\":\"zh_CN\",\"city\":\"Anqing\",\"province\":\"Anhui\",\"country\":\"China\",\"avatarUrl\":\"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJZNpvn3iaLjLhpTQq9pGc2ibk9Siav0akCJJ7xLT9Mic0d3JibPkCAuxmqRdQ8ZDEAwGkcMialefuTEbYA/132\"}";
//        String sessionKey = "RZGxVcGZ6e/dpmx2iwSCHA==";
//        String signature = "20349d529ba8a7e7cc4b396f9e0666f537dd2251";
//        WXBizDataCrypt.checkSignature(rawData, signature, sessionKey);


        LocalDate localDate = LocalDate.of(2019, 1, 13);
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,4);
        System.out.println(localDate.get(weekFields.weekOfMonth()));

    }
}
