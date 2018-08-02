package com.study.studymarket.common.util.weixin;

/**
 * Created by heping on 2017/9/19
 */

public class RestData {
    /** 成功 */
    public static final String SUCCESS_CODE = "0000";
    /** 失败 */
    public static final String FAILURE_CODE = "9999";
    /** 未明 */
    public static final String UNKNOWN_CODE = "1111";
    /** 未登录 */
    public static final String UNLOGIN_CODE = "3333";
    /** 网路异常 */
    //public static final String NETERR_CODE = "6666";
    //未授权
    public static final String UN_OAUTH = "7777";
    /** 业务码值 */
    public static final String BUSINESS_CODE = "4444";


    /**
     * 状态码：“0000”成功 其他失败
     */
    private String code;
    /**
     * 描述
     */
    private String message;
    /**
     * 具体信息
     */
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public RestData() {
        super();
    }

    public RestData(String code, String message){

        this.code = code;

        this.message = message;
    }

}
