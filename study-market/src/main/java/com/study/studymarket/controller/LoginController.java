package com.study.studymarket.controller;

import com.study.studymarket.common.config.MiniPrograme;
import com.study.studymarket.common.util.weixin.RestData;
import com.study.studymarket.common.util.weixin.miniProgram.ClientUtil;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(description = "LoginController相关api")
//@RestController
@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MiniPrograme miniPrograme;
    @Autowired
    private ClientUtil clientUtil;

//    private User
    @ApiOperation(value = "发送信息", notes = "展示用返回test")
    @RequestMapping(value = "/sendMsg", method = {RequestMethod.GET, RequestMethod.POST})
    public String sendMsg(){
        return "test";
    }

    /**
     * 根据code(临时登录凭证)获取openId(用户唯一标识)和sessionKey(session_key)
     * 前端可以通过wx.checkSession判断当前sessionkey是否有效，如果无效则让用户重新登录，调用wx.login接口才行
     * @param code
     * @return openId
     *
     * */
    @ApiOperation(value = "获取小程序openId")
    @ApiImplicitParams({@ApiImplicitParam(name = "code", value  = "微信传递的code", required = false,dataType = "String")})
    @ApiResponses(value = {
            @ApiResponse(code =  0000, message = "获取成功"),
            @ApiResponse(code = 9999, message = "获取用户openId和SessionKey失败"),
    })
    @RequestMapping(value = "/getImportMsg",  method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public RestData getImportMsg(String code) {
        RestData restData = new RestData();
        logger.info("获取code, 五分钟有效");
        logger.info("coede:"+code);
        String result = clientUtil.getSessionKeyAndOpenIdOrUnionID(code);
        JSONObject resultJson = JSONObject.fromObject(result);
        if (resultJson.has("errcode")) {
            logger.error("获取用户openId和SessionKey失败");
            String errcode = resultJson.getString("errcode");
            String msg = resultJson.getString("errmsg");
            logger.info("errcode:{}\r\nerrmsg:{}", errcode, msg);
            restData.setCode(RestData.FAILURE_CODE);
            restData.setMessage(msg);
        } else {
            logger.error("获取用户openId和SessionKey成功");
            String sessionKey = resultJson.getString("session_key");//TODO 开发者应该事先通过 wx.login 登录流程获取会话密钥 session_key 并保存在服务器。为了数据不被篡改，开发者不应该把session_key传到小程序客户端等服务器外的环境。
            String openId = resultJson.getString("openid");
            logger.info("openId:{}\r\nsessionKey:{}", openId, sessionKey);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("openId", openId);
            restData.setCode(RestData.SUCCESS_CODE);
            restData.setMessage("获取成功");
            restData.setData(openId);

        }
        return restData;
    }

    /**
     * top1、获取加密信息并解密， 获取用户信息
     *
     * @param iv 偏移向量
     * @param encryptedData 加密数据
     * @param openId
     * */
    public void getUserInfo (String iv, String encryptedData, String openId) {

    }
}
