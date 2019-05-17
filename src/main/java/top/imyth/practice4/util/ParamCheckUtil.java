package top.imyth.practice4.util;

import java.util.Map;

public class ParamCheckUtil {

    /**
     * 检查map类型参数是否为null，包括检查map每一个键的值是否为null
     * @param params map类型参数
     * @return map不为null且map每个键的值均不为null才返回true，否则返回false
     */
    public boolean mapParamsCheck(Map params) {
        if (params == null) {
            return false;
        }
        for (Object o : params.keySet()) {
            if (params.get(o) == null) {
                return false;
            }
        }
        return true;
    }
}
