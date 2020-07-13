package com.swust.server.proxy;

import com.swust.common.Starter;
import com.swust.common.cmd.CmdOptions;
import com.swust.common.codec.MessageDecoder;
import com.swust.common.codec.MessageEncoder;
import com.swust.common.config.LogFormatter;
import com.swust.common.config.LogUtil;
import com.swust.common.constant.Constant;
import com.swust.server.proxy.handler.TcpServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.cli.*;

import java.util.concurrent.TimeUnit;

/**
 * @author : MrLawrenc
 * date  2020/7/13 23:43
 * <p>
 * 代理服务启动类
 */
public class ProxyStarter implements Starter {

    @Override
    public boolean start(String[] args) throws Exception {
        LogFormatter.init();
        /*
         *
         * 1.定义阶段:
         * 其中Option的参数：
         *   第一个参数：参数的简单形式
         *   第二个参数：参数的复杂形式
         *   第三个参数：是否需要额外的输入
         *   第四个参数：对参数的描述信息
         *
         * */
        Options options = new Options();
        options.addOption(CmdOptions.HELP.getOpt(), CmdOptions.HELP.getLongOpt(),
                CmdOptions.HELP.isHasArgs(), CmdOptions.HELP.getDescription());
        options.addOption(CmdOptions.PORT.getOpt(), CmdOptions.PORT.getLongOpt(),
                CmdOptions.PORT.isHasArgs(), CmdOptions.PORT.getDescription());
        options.addOption(CmdOptions.PASSWORD.getOpt(), CmdOptions.PASSWORD.getLongOpt(),
                CmdOptions.PASSWORD.isHasArgs(), CmdOptions.PASSWORD.getDescription());
        /*
         *
         * 2.解析阶段
         *      通过解析器解析参数
         * */
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        /*
         * 3.询问阶段
         *      根据commandLine查询参数，提供服务
         * */
        if (cmd.hasOption(CmdOptions.HELP.getOpt()) || cmd.hasOption(CmdOptions.HELP.getLongOpt())) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Constant.OPTIONS, options);
        } else {
            int port = Integer.parseInt(cmd.getOptionValue(CmdOptions.PORT.getLongOpt(), Constant.DEFAULT_PORT));
            String password = cmd.getOptionValue(CmdOptions.PASSWORD.getLongOpt(), Constant.DEFAULT_PASSWORD);

            start0(port, password);
        }
        return true;
    }

    private void start0(int port, String password) throws Exception {
        Channel serverChannel = new TcpServer().initTcpServer(port, new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                TcpServerHandler tcpServerHandler = new TcpServerHandler(password);
                //int为4字节，定义的长度字段(长度字段+消息体)
                ch.pipeline().addLast(new MessageDecoder(), new MessageEncoder(),
                        new IdleStateHandler(60, 20, 0, TimeUnit.SECONDS),
                        tcpServerHandler);
            }
        });
        LogUtil.infoLog("Server start success on port:{}", port);
    }
}