package cn.sh.ideal.util;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by XieTianYi on 2020/5/26.
 *
 * @author XieTianYi
 */
public class HttpClientsUtil {

    private static final CloseableHttpClient httpclient = HttpClients.createDefault();
    private static HttpClientContext context = HttpClientContext.create();

    private static final Logger log = LoggerFactory.getLogger(HttpClientsUtil.class);

    public static String post(String url, Map<String, String> map) throws ParseException, IOException {
        String body = "";
        System.out.println(url);

        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (null != entry.getValue()) {
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
        }

        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        //设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = httpclient.execute(httpPost, context);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "UTF-8");
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }
}
