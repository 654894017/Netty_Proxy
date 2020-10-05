package com.swust.common.constant;

/**
 * @author : LiuMingyao
 * @date : 2019/11/4 20:24
 * @description : 常量
 */
public class Constant {
    public static final String DEFAULT_PORT = "12525";
    public static final String DEFAULT_PASSWORD = "121325";
    public static final String OPTIONS = "options";
    public static final String NULL_TOKEN = "lmy";


    /**
     * 默认编码器,为代理服务使用
     */
    public static String ENCODE_HANDLER_NAME = "encode";
    /**
     * 文件服务器编码器
     */
    public static String ENCODE_HANDLER4File_NAME = "encode";
    /**
     * 默认解码器,为代理服务使用
     */
    public static String DECODE_HANDLER_NAME = "encode";
    /**
     * 文件服务器解码器
     */
    public static String DECODE_HANDLER4File_NAME = "encode";
    /**
     * 业务默认handler,为代理服务使用
     */
    public static String BUSINESS_HANDLER_NAME = "businessHandler";
    /**
     * 文件服务的业务管理器
     */
    public static String BUSINESS_HANDLER4FILE_NAME = "business4FileHandler";
}