package com.swust.server;

import com.swust.server.proxy.ProxyStarter;

/**
 * @author : LiuMing
 * @date : 2019/11/4 9:46
 * @description :   服务端
 */
public class ServerMain {

    /**
     * Apache Commons CLI是开源的命令行解析工具，它可以帮助开发者快速构建启动命令，并且帮助你组织命令的参数、以及输出列表等。
     * 参考博文:https://www.cnblogs.com/xing901022/archive/2016/06/22/5608823.html
     * CLI分为三个过程：
     * <p>      </>定义阶段：在Java代码中定义Optin参数，定义参数、是否需要输入值、简单的描述等
     * <p>      </>解析阶段：应用程序传入参数后，CLI进行解析
     * <p>      </>询问阶段：通过查询CommandLine询问进入到哪个程序分支中
     */
    public static void main(String[] args) throws Exception {
        ProxyStarter.start(args);
    }

}
