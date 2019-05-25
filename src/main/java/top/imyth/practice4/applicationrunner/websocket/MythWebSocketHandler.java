package top.imyth.practice4.applicationrunner.websocket;

import com.google.gson.Gson;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.imyth.practice4.observermode.Observable;
import top.imyth.practice4.observermode.Observer;

@Component
@ChannelHandler.Sharable
public class MythWebSocketHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private Observable observable;
    private Long userId;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof WebSocketFrame) {
            if (msg instanceof BinaryWebSocketFrame) {
            } else if (msg instanceof TextWebSocketFrame) {
                System.out.println("TextWebSocketFrame Received : ");
                String text = ((TextWebSocketFrame) msg).text();
                if (text.startsWith("userId")) {
                    userId = Long.valueOf(text.split(":")[1]);
                     observable.register(userId, message -> {
                         System.out.println("消息"+message);
                         ctx.writeAndFlush(new TextWebSocketFrame(message.toString()));
                     });
                    ctx.writeAndFlush(new TextWebSocketFrame("登录成功，注册观察者成功"));
               }
            } else if (msg instanceof PingWebSocketFrame) {
                ctx.writeAndFlush(new PongWebSocketFrame(Unpooled.copiedBuffer("pong", CharsetUtil.UTF_8)));
            } else if (msg instanceof PongWebSocketFrame) {
                ctx.writeAndFlush(new PingWebSocketFrame(Unpooled.copiedBuffer("ping", CharsetUtil.UTF_8)));
            } else if (msg instanceof CloseWebSocketFrame) {
                observable.unRegister(userId);

            } else {
                System.out.println("不支持的 WebSocketFrame");

            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        observable.unRegister(userId);
    }
}
