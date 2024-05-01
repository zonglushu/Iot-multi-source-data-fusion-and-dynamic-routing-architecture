package com.mayphyr.iotapiclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import javax.xml.bind.annotation.XmlType;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.mayphyr.iotapiclientsdk.utils.SignUtil.genSign;

/**
 * 淳阳项目组物联网API调用平台
 */
public class SunApiClient {

    /**
     * 访问密钥
     */
    private String accessKey;
    /**
     *  校验密钥
     */
    private String secretKey;


    /**
     * 网关的默认地址
     */
    private static String  DEFAULT_GATEWAY_HOST="http://localhost:8103";

    public SunApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public void setDEFAULT_GATEWAY_HOST(String DEFAULT_GATEWAY_HOST) {
        this.DEFAULT_GATEWAY_HOST = DEFAULT_GATEWAY_HOST;
    }




    /**
     * 客户端远程调用接口，这里就不用feign调用，因为是还要给开发者使用
     * 如果feign，他还需要引入我们的feign包，而feign包中还有其他管理的接口，
     * 不能让普通的调用者调用
     * @param params  接口的参数
     * @param url   接口的地址
     * @param method  接口的调用方式（GET,POST）
     * @return
     * @throws UnsupportedEncodingException
     */
    public String invokeInterface(String params, String url, String method) throws UnsupportedEncodingException {
        HttpResponse httpResponse = HttpRequest.post(DEFAULT_GATEWAY_HOST + url)
                .header("Accept-Charset", CharsetUtil.UTF_8)
                .addHeaders(getHeaderMap(params, method))
                .body(params)
                .execute();
        return JSONUtil.formatJsonStr(httpResponse.body());
    }


    /**
     * 获取传入的一些信息，并且为client加上一些信息，防止重刷
     * @param body
     * @param method
     * @return
     * @throws UnsupportedEncodingException
     */
    private Map<String, String> getHeaderMap(String body, String method) throws UnsupportedEncodingException {
        HashMap<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        map.put("nonce", RandomUtil.randomNumbers(10));
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", genSign(body, secretKey));
        // 请求体进行加密
        String encodedBody = URLUtil.encode(body, CharsetUtil.CHARSET_UTF_8);
        map.put("body", encodedBody);
        map.put("method", method);
        return map;
    }





}
