package io.github.douyayun.signature.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 签名工具
 *
 * @author houp
 * @since 1.0.0
 */
public class SignUtils {
    private static final Logger log = LoggerFactory.getLogger(SignUtils.class);

    private SignUtils() {
    }

    /**
     * 数据签名
     *
     * @param data 数据
     * @return
     */
    public static String getSign(String data) {
        // commons-codec
        // return DigestUtils.md5Hex(data).toLowerCase();
        return Md5Utils.md5(data).toLowerCase();
    }

    /**
     * map排序
     * 对参数进行ASCII码从小到大排序（字典序）
     *
     * @param map
     * @return
     */
    public static String sortMapByKey(Map<String, String[]> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        List<Map.Entry<String, String[]>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String[]>>() {
            @Override
            public int compare(Map.Entry<String, String[]> o1, Map.Entry<String, String[]> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        List<String> data = new ArrayList<>();
        for (Map.Entry<String, String[]> item : list) {
            String key = item.getKey();
            String[] value = item.getValue();
            if (null != value && value.length > 0) {
                data.add(key + "=" + String.join(",", value));
            } else {
                data.add(key + "=");
            }
        }
        return String.join("&", data);
    }
}
