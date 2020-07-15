package com.swust.client;

import com.swust.client.proxy.ProxyClient;

/**
 * @author : LiuMing
 * @date : 2019/11/4 14:15
 */
public class ClientMain {

    public static void main(String[] args) throws Exception {
        new ProxyClient().start(args);
    }

}
