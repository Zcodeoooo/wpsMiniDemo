package com.example.wpsminidemo;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @Author：zzt
 * @Version：1.0
 * @Date：2022-05-29-15:52
 * @Since:jdk1.8
 * @Description:
 */
@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor{

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution)
            throws IOException {
        //获取uri路径

        String path = request.getURI().getPath() ;
        if(request.getURI().getRawQuery() != null &&
                !"".equals(request.getURI().getRawQuery())) {
            path = path + "?" + request.getURI().getRawQuery();
        }

        //获取Content-type，“application/json"
        request.getHeaders().forEach((k,v)->{
            System.out.println(k+v);
        });
        //日期格式化
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = dateFormat.format(new Date());
        //open不参与签名，做替换处理
        if (path.startsWith("/open")) {
            path = path.replace("/open", "");
        }
        String sha256body;
        //body为空则为空，否则返回sha256(body)
        System.out.println(Arrays.toString(body));
        if (body.length > 0) {
            sha256body = HMacUtils.getSHA256StrJava(body);
        } else {
            sha256body = "";
        }
        //hmac-sha256(secret_key, Ver + HttpMethod + URI + Content-Type + Wps-Date + sha256(HttpBody))
        String signature = null;

        try {
            signature = HMacUtils.HMACSHA256("WPS-4" + request.getMethod() + path
                    + "application/json" + date + sha256body,"XvdFNUuwchxCKytHJIPVqRODQlpgfTSA");
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        request.getHeaders().add("Wps-Docs-Date",date);
        request.getHeaders().add("Wps-Docs-Authorization", String.format("WPS-4 %s:%s","AKUlhJjMXWIKczQa", signature));
        request.getHeaders().forEach((r,d)->{
            System.out.println(r+d);
        });
        return execution.execute(request, body);
    }
}
