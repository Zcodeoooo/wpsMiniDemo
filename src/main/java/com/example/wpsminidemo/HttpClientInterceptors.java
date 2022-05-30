package com.example.wpsminidemo;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import static com.example.wpsminidemo.HMacUtils.getStringByInputStream_1;


@Configuration
public class HttpClientInterceptors {


    @Bean()
    public HttpRequestInterceptor httpRequestInterceptor(){
        return (request, context) -> {
            String body = null;
            HttpRequestWrapper httpRequestWrapper = (HttpRequestWrapper) request;
            HttpRequest originHttpRequest = httpRequestWrapper.getOriginal();
            HttpEntity entity = null;
            if (originHttpRequest instanceof HttpPost) {
                HttpPost originHttpRequest1 = (HttpPost) originHttpRequest;
                entity = originHttpRequest1.getEntity();
                InputStream content = entity.getContent();
                body = getStringByInputStream_1(content);
                System.out.println(body);
            }
            if (originHttpRequest instanceof HttpPut) {
                HttpPut originHttpRequest1 = (HttpPut) originHttpRequest;
                entity = originHttpRequest1.getEntity();
                InputStream content = entity.getContent();
                body = getStringByInputStream_1(content);
                System.out.println(body);
            }
            System.out.println("**********");
            DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = dateFormat.format(new Date());
            String path = request.getRequestLine().getUri();
            //open不参与签名，做替换处理
            if (path.startsWith("/open")) {
                path = path.replace("/open", "");
            }

            System.out.println(path);
            String sha256body;

            //body为空则为空，否则返回sha256(body)
            if (body == null || Objects.equals(body, "{}") || Objects.equals(body, "")) {
                sha256body = "";
            } else {
                sha256body = HMacUtils.getSHA256StrJava(body.getBytes(StandardCharsets.UTF_8));
            }
//         hmac-sha256(secret_key, Ver + HttpMethod + URI + Content-Type + Wps-Date + sha256(HttpBody))
            String signature = null;
            try {
//                signature = HMacUtils.HMACSHA256("WPS-4" + ((HttpRequestWrapper) request).getMethod() + path + entity.getContentType().getValue() + date + sha256body, "secretKey");
                signature = HMacUtils.HMACSHA256("WPS-4" + ((HttpRequestWrapper) request).getMethod() + path + "application/json" + date + sha256body, "XvdFNUuwchxCKytHJIPVqRODQlpgfTSA");
            } catch (Exception e) {
                e.printStackTrace();
            }
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Wps-Docs-Date", date);
            request.setHeader("Wps-Docs-Authorization", String.format("WPS-4 %s:%s", "AKUlhJjMXWIKczQa", signature));
            for (Header allHeader : request.getAllHeaders()) {
                System.out.println(allHeader);
            }
        };
    }


}
