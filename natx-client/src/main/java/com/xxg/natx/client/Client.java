package com.xxg.natx.client;

import com.xxg.natx.client.handler.ClientHandler;
import com.xxg.natx.client.net.TcpConnection;
import com.xxg.natx.common.codec.MessageDecoder;
import com.xxg.natx.common.codec.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.cli.*;

/**
 * @author : LiuMing
 * @date : 2019/11/4 14:15
 * @description :   内网的netty客户端，该客户端内部嵌了一个客户端，内部的客户端是访问本地的应用
 */
public class Client {

    public static void main(String[] args) throws Exception {

        // args
        Options options = new Options();
        options.addOption("h", false, "Help");
        options.addOption("server_addr", true, "Natx server address");
        options.addOption("server_port", true, "Natx server port");
        options.addOption("password", true, "Natx server password");
        options.addOption("proxy_addr", true, "Proxy server address");
        options.addOption("proxy_port", true, "Proxy server port");
        options.addOption("remote_port", true, "Proxy server remote port");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("h")) {
            // print help
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("options", options);
        } else {

            String serverAddress = cmd.getOptionValue("server_addr");
            if (serverAddress == null) {
                System.out.println("server_addr cannot be null");
                return;
            }
            String serverPort = cmd.getOptionValue("server_port");
            if (serverPort == null) {
                System.out.println("server_port cannot be null");
                return;
            }
            String password = cmd.getOptionValue("password");
            String proxyAddress = cmd.getOptionValue("proxy_addr");
            if (proxyAddress == null) {
                System.out.println("proxy_addr cannot be null");
                return;
            }
            String proxyPort = cmd.getOptionValue("proxy_port");
            if (proxyPort == null) {
                System.out.println("proxy_port cannot be null");
                return;
            }
            String remotePort = cmd.getOptionValue("remote_port");
            if (remotePort == null) {
                System.out.println("remote_port cannot be null");
                return;
            }
            System.out.println("pwd:"+password);
            TcpConnection tcpConnection = new TcpConnection();
            tcpConnection.connect(serverAddress, Integer.parseInt(serverPort), new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ClientHandler clientHandler = new ClientHandler(Integer.parseInt(remotePort), password,
                            proxyAddress, Integer.parseInt(proxyPort));
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4),
                            new MessageDecoder(), new MessageEncoder(),
                            new IdleStateHandler(60, 30, 0), clientHandler);
                }
            });
        }
    }
}
