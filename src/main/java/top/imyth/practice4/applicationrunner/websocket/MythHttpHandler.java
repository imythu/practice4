package top.imyth.practice4.applicationrunner.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.imyth.practice4.dao.UserMapper;
import top.imyth.practice4.observermode.Observable;
import top.imyth.practice4.observermode.Observer;
import top.imyth.practice4.service.user.UserInfoService;

import java.util.Map;

@Component
@Order(2)
@ChannelHandler.Sharable
public class MythHttpHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private MythWebSocketHandler mythWebSocketHandler;
    @Autowired
    private HttpRequestParameter httpRequestParameter;
    @Autowired
    private UserInfoService userInfoService;

    private WebSocketServerHandshaker webSocketServerHandshaker;

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到请求");
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            System.out.println("netty : Http Request Received");
            HttpHeaders httpHeaders = request.headers();
            System.out.println("netty : Connection : " + httpHeaders.get("Connection"));
            System.out.println("netty : Upgrade : " + httpHeaders.get("Upgrade"));
            // 登录检查
            Map<String, String> params = httpRequestParameter.getParametersMap(request);
            String phoneNumber = params.get("phoneNumber");
            String password = params.get("password");
            Long result = userInfoService.loginCheck(phoneNumber, password);
            if (result <= 0 ) {
                ByteBuf byteBuf = Unpooled.copiedBuffer("{\"result\": \""+result+"\"}", CharsetUtil.UTF_8);
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
                byteBuf.release();
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
                ctx.writeAndFlush(response);
                return;
            }

            if ("Upgrade".equalsIgnoreCase(httpHeaders.get(HttpHeaderNames.CONNECTION)) &&
                    "WebSocket".equalsIgnoreCase(httpHeaders.get(HttpHeaderNames.UPGRADE))) {
                ctx.pipeline().replace(this, "websocketHandler", mythWebSocketHandler);
                // 握手以升级从HTTP到WebSocket协议的连接
                handleHandshake(ctx, request);
            } else {
                //这里处理不是websocket的http请求
                System.out.println(request.uri());
                QueryStringDecoder decoder = new QueryStringDecoder(request.uri(), CharsetUtil.UTF_8);
                String url = decoder.path();
                System.out.println(url);
            }
        } else {
            System.out.println("Incoming request is unknown");
        }
    }

    private void handleHandshake(ChannelHandlerContext ctx, FullHttpRequest request) {
        WebSocketServerHandshakerFactory webSocketServerHandshakerFactory
                = new WebSocketServerHandshakerFactory(getWebSocketUrl(request), null, false);
        webSocketServerHandshaker = webSocketServerHandshakerFactory.newHandshaker(request);
        if (webSocketServerHandshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            webSocketServerHandshaker.handshake(ctx.channel(), request);
        }
    }

    private String getWebSocketUrl(FullHttpRequest request) {
        System.out.println("Req URI : " + request.uri());
        String url = "ws://"+request.headers().get(HttpHeaderNames.HOST) + request.uri();
        System.out.println("Constructed URL : " + url);
        return url;
    }
}
