package com.swust.common;

/**
 * @author : MrLawrenc
 * date  2020/7/13 23:45
 * <p>
 * 启动类接口
 */
public interface Starter {

    /**
     * 启动方法
     *
     * @param args 参数集
     * @return 启动结果
     * @throws Exception 启动异常
     */
    boolean start(String[] args) throws Exception;
}