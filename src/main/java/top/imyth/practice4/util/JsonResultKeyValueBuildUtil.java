package top.imyth.practice4.util;

import java.util.HashMap;
import java.util.Map;

public class JsonResultKeyValueBuildUtil {

    /**
     * 将传入的Long 数字包装成key为 result 的map返回
     * @param resultNumber 数字
     * @return map
     */
    public Map<String, Long> getResultMapFromLong(Long resultNumber) {
        Map<String, Long> resultMap = new HashMap<>(1);
        resultMap.put("result", resultNumber);
        return resultMap;
    }

    /**
     * 将传入的Long 数字包装成key为 resultKey 的map返回
     * @param resultNumber 数字
     * @param resultKey 包装后的map的key值
     * @return map
     */
    public Map<String, Long> getResultMapFromLong(Long resultNumber, String resultKey) {
        Map<String, Long> resultMap = new HashMap<>(1);
        resultMap.put(resultKey, resultNumber);
        return resultMap;
    }

    /**
     * 将传入的Integer 数字包装成key为 result 的map返回
     * @param resultNumber 数字
     * @return map
     */
    public Map<String, Integer> getResultMapFromInteger(Integer resultNumber) {
        Map<String, Integer> resultMap = new HashMap<>(1);
        resultMap.put("result", resultNumber);
        return resultMap;
    }

    /**
     * 将传入的Integer 数字包装成key为 resultKey 的map返回
     * @param resultNumber 数字
     * @param resultKey 包装后的map的key值
     * @return map
     */
    public Map<String, Integer> getResultMapFromInteger(Integer resultNumber, String resultKey) {
        Map<String, Integer> resultMap = new HashMap<>(1);
        resultMap.put(resultKey, resultNumber);
        return resultMap;
    }


    /**
     * 将传入的String 字符串包装成key为 result 的map返回
     * @param s 字符串
     * @return map
     */
    public Map<String, String> getResultMapFromString(String s) {
        Map<String, String> resultMap = new HashMap<>(1);
        resultMap.put("result", s);
        return resultMap;
    }
}
