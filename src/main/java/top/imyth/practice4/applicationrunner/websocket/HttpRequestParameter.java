package top.imyth.practice4.applicationrunner.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@ChannelHandler.Sharable
public class HttpRequestParameter {

    private Map<String, String> parametersMap;

    public Map<String, String> getParametersMap(FullHttpRequest request) {
        try {
            initGetParametersMap(request);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>(1);
        }
        return parametersMap;
    }

    public HttpRequestParameter() throws IOException {
    }

    public void initGetParametersMap(FullHttpRequest request) throws IOException {
        parametersMap = new LinkedHashMap<>();
        HttpMethod method = request.method();
        if (method == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            decoder.parameters().entrySet().forEach( entry -> {
                // entry.getValue()是一个List, 只取第一个元素
                parametersMap.put(entry.getKey(), entry.getValue().get(0));
            });
        }else if (method == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            decoder.offer(request);
            List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
            for (InterfaceHttpData data : datas) {
                Attribute attribute = (Attribute) data;
                parametersMap.put(attribute.getName(), attribute.getValue());
            }
        }
    }
}
