package com.alipay.alipaydemo.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

@Configuration
public class AlipayConfig {

    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhuffPhQ1NxQZX3X8PGt3" +
            "qIA9Z8wexed2Lrj4ssHvt2fLcjVKr5MQ9GP4hEZG9uo66b7smU2xifeA8Wyd/QBTVXWzu7AXL5lRv8Aed" +
            "MSIXGOFXF88cLZhMpUl1qbKJe5fWKCXguX3vLBX+lDobzk0Ld8jo2V5bhdSzdb3JroHW5iCW5ZgXKDa4Q" +
            "2g7TFVvJAJ0BiccwH+zYJrIt0ionAY15qmS3GyRCXOVOq83PAYdpgsmv7JUQ7nAsLroF5fko31tDI45/m" +
            "hgMHnKrlqRSJRtilJsYkQzXT3x6ZHljW4lJG6JlxlurPgD03QiKrZA9I6wt28agYiZx0a9GomvMAZSwID" +
            "AQAB";

    public static final Config CONFIG = getConfig();
    // 签名方式


    static {
        Factory.setOptions(CONFIG);
    }

    public static Config getConfig() {

        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com";
        config.appId = "2016102700770343";
        config.signType = sign_type;

        config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhuffPhQ1NxQZX3X8PGt3" +
                "qIA9Z8wexed2Lrj4ssHvt2fLcjVKr5MQ9GP4hEZG9uo66b7smU2xifeA8Wyd/QBTVXWzu7AXL5lRv8Aed" +
                "MSIXGOFXF88cLZhMpUl1qbKJe5fWKCXguX3vLBX+lDobzk0Ld8jo2V5bhdSzdb3JroHW5iCW5ZgXKDa4Q" +
                "2g7TFVvJAJ0BiccwH+zYJrIt0ionAY15qmS3GyRCXOVOq83PAYdpgsmv7JUQ7nAsLroF5fko31tDI45/m" +
                "hgMHnKrlqRSJRtilJsYkQzXT3x6ZHljW4lJG6JlxlurPgD03QiKrZA9I6wt28agYiZx0a9GomvMAZSwID" +
                "AQAB";
        config.merchantPrivateKey = getPrivateKey(config.appId);
            config.notifyUrl = " http://mw94bj.natappfree.cc/notify_url";

        Factory.setOptions(config);
        return config;
    }

    private static String getPrivateKey(String appId) {
        InputStream stream = AlipayConfig.class.getResourceAsStream("/fixture/privateKey.json");
        Map<String, String> result = new Gson().fromJson(new InputStreamReader(stream), new TypeToken<Map<String, String>>() {}.getType());
        return result.get(appId);
    }
}
