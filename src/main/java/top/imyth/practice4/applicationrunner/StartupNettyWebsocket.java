package top.imyth.practice4.applicationrunner;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.imyth.practice4.applicationrunner.websocket.MythHttpHandler;

@Component
@Order(2)
public class StartupNettyWebsocket implements ApplicationRunner {

    @Value("${netty.port:6666}")
    private int port;

    @Autowired
    private MythHttpHandler mythHttpHandler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread thread = new Thread(() -> {
            // 启动websocket
            EventLoopGroup bossWorker = new NioEventLoopGroup();
            EventLoopGroup worker = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossWorker, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("httpServerCodec", new HttpServerCodec())
                                    .addLast("aggregator", new HttpObjectAggregator(65536))
                                    .addLast("httpChunked", new ChunkedWriteHandler())
                                    .addLast("httpHandler", mythHttpHandler);
                        }
                    });
            try {
                Channel channel = bootstrap.bind(port).sync().channel();
                System.out.println("websocket已启动，端口为"+port);
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossWorker.shutdownGracefully();
                worker.shutdownGracefully();
            }
        });
        thread.start();
    }
}
