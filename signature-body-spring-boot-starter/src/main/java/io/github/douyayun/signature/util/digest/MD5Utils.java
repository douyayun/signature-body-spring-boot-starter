package io.github.douyayun.signature.util.digest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * TODO
 *
 * @author houp
 * @since 1.0.0
 **/
public class MD5Utils {
    private static final Logger log = LoggerFactory.getLogger(MD5Utils.class);

    /**
     * MD5加密
     *
     * @param data 待加密数据
     * @return 加密后数据
     */
    public static String md5(String data) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(data.getBytes("UTF-8"));
            byte[] messageDigest = algorithm.digest();
            return new String(toHex(messageDigest).getBytes("UTF-8"), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("MD5生成错误", e);
        }
    }

    private static String toHex(byte data[]) {
        if (data == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(data.length * 2);
        for (int i = 0; i < data.length; i++) {
            if ((data[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(data[i] & 0xff, 16));
        }
        return buf.toString();
    }

}
