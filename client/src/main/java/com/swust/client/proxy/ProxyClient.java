package com.swust.client.proxy;

import com.swust.client.proxy.handler.ClientHandler;
import com.swust.common.Starter;
import com.swust.common.cmd.CmdOptions;
import com.swust.common.codec.MessageDecoder;
import com.swust.common.codec.MessageEncoder;
import com.swust.common.config.LogUtil;
import com.swust.common.constant.Constant;
import com.swust.common.entity.ClientConfig;
import com.swust.common.entity.ConfigBuilder;
import com.swust.common.util.CommandUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.StringUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * @author hz20035009-逍遥
 * date   2020/7/14 17:56
 * <p>
 * 内网的netty客户端，该客户端内部嵌了一个或多个代理客户端，内部的代理客户端是访问本地的应用
 * <p>
 * 单机 -h localhost -p 9000 -password 123lmy -proxy_h localhost -proxy_p 880 -remote_p 11000
 * 多个 -profile F:\JavaProject\Netty_Proxy\client.pro
 */
public class ProxyClient implements Starter {
    private ClientConfig clientConfig;

    @Override
    public boolean start(String[] args) throws Exception {
        Options options = new Options();

        CommandUtil.addClientOptions(options);

        CommandLine cmd = new DefaultParser().parse(options, args);
        if (cmd.hasOption(CmdOptions.HELP.getLongOpt()) || cmd.hasOption(CmdOptions.HELP.getOpt())) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Constant.OPTIONS, options);
            return false;
        }
        String profile = cmd.getOptionValue(CmdOptions.PROFILE.getOpt());
        if (!StringUtil.isNullOrEmpty(profile)) {
            LogUtil.infoLog("start read profile info");
            clientConfig = CommandUtil.clientConfigByProperties(profile);
        } else {
            //opt和longOpt都可以拿到命令对应的值
            String serverAddress = cmd.getOptionValue(CmdOptions.HOST.getOpt());
            if (serverAddress == null) {
                LogUtil.errorLog("server_addr cannot be null");
                return false;
            }
            String serverPort = cmd.getOptionValue(CmdOptions.PORT.getOpt());
            if (serverPort == null) {
                LogUtil.errorLog("server_port cannot be null");
                return false;
            }
            String password = cmd.getOptionValue(CmdOptions.PASSWORD.getOpt());
            String proxyAddress = cmd.getOptionValue(CmdOptions.PROXY_HOST.getOpt());
            if (proxyAddress == null) {
                LogUtil.errorLog("proxy_addr cannot be null");
                return false;
            }

            String proxyPort = cmd.getOptionValue(CmdOptions.PROXY_PORT.getOpt());
            if (proxyPort == null) {
                LogUtil.errorLog("proxy_port cannot be null");
                return false;
            }

            String remotePort = cmd.getOptionValue(CmdOptions.REMOTE_PORT.getOpt());
            if (remotePort == null) {
                LogUtil.errorLog("remote_port cannot be null");
                return false;
            }
            clientConfig = ConfigBuilder.buildClient(password, serverPort, serverAddress, CommandUtil.parseArray(proxyAddress)
                    , CommandUtil.parseArray(proxyPort), CommandUtil.parseArray(remotePort));
        }
        start0();
        return true;
    }

    public void start0() throws Exception {
        TcpClient.connect(clientConfig.getServerHost(), Integer.parseInt(clientConfig.getServerPort()), new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ClientHandler clientHandler = new ClientHandler(clientConfig.getRemotePort(), clientConfig.getServerPassword(),
                        clientConfig.getProxyHost(), clientConfig.getProxyPort());
                ch.pipeline().addLast(
                        new MessageDecoder(), new MessageEncoder(),
                        new IdleStateHandler(60, 20, 0), clientHandler);
            }
        });
    }

}