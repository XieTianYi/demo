package cn.sh.ideal.controller;

import cn.sh.ideal.util.HttpClientsUtil;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by XieTianYi on 2020/2/3.
 *
 * @author XieTianYi
 */
@RestController
@RequestMapping("public")
public class TestController {

    @RequestMapping("translate")
    public JSON translate(@RequestParam String text) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("source", "zh");
        params.put("app_id", "2128441329");
        params.put("nonce_str", "1");
        params.put("target", "en");
        params.put("text", text);
        params.put("time_stamp", String.valueOf(System.currentTimeMillis() / 1000));

        String sign = getReqSign(params, "MSAU0YXZW8i5NOJP");
        params.put("sign", sign);
        String response = HttpClientsUtil.post("https://api.ai.qq.com/fcgi-bin/nlp/nlp_texttranslate", params);
        return JSONObject.fromObject(response);
    }

    private static String getReqSign(Map<String, String> params, String appkey) {
        Map<String, String> sortMap = new TreeMap<>(params);

        // 2. 拼按URL键值对
        String str = "";
        for (Map.Entry<String, String> entry : sortMap.entrySet()) {
            if (entry.getValue() != "") {
                str += entry.getKey() + '=' + URLEncoder.encode(entry.getValue()) + '&';
            }
        }
        // 3. 拼接app_key
        str += "app_key=" + appkey;
        // 4. MD5运算+转换大写，得到请求签名
        return encoderByMd5(str).toUpperCase();
    }

    public static String encoderByMd5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return null;
        }
    }
}