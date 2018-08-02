package com.study.studymarket.common.util.weixin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by liangyu on 2017/3/8.
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    /**
     * 字符串转化为Map对象
     * @param str
     * @return
     * @throws Exception
     */
    public static Map setString2Map(String str) throws Exception{
        return mapper.readValue(str, Map.class);
    }

    public static List setString2List(String str) throws Exception{
        return mapper.readValue(str, List.class);
    }

    /**
     * 对象转化json字符串
     * @param object
     * @return
     * @throws Exception
     */
    public static String setMap2String(Object object) throws Exception{
        return mapper.writeValueAsString(object);
    }

    /**
     * 加载json文件
     * @param clazz
     * @param file
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T loadJsonFile(Class<T> clazz, String file) throws Exception{
        return loadJsonFile(clazz, new File(file));
    }

    /**
     * 加载json文件
     * @param clazz
     * @param file
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T loadJsonFile(Class<T> clazz, File file) throws Exception{
        try {
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
            return (T)mapper.readValue(new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8")), clazz);
        } catch (IOException e) {
            logger.error("加载配置文件：{} 异常", file, e);
            throw e;
        }
    }

}
