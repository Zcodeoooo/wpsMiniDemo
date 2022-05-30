package com.example.wpsminidemo;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @Author：zzt
 * @Version：1.0
 * @Date：2022-05-29-15:31
 * @Since:jdk1.8
 * @Description:
 */
@SpringBootTest()
public class Collection {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpRequestInterceptor requestInterceptor;

    private static final String WPSURL = "10.226.48.56";
    //设置回调ip
//    http://{{url}}:20107/api/app/v1/config
    @Test
    public void setCallBack()  {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("access_key","AKUlhJjMXWIKczQa");
        jsonObject.put("secret_key","XvdFNUuwchxCKytHJIPVqRODQlpgfTSA");
        //设置回调地址
        jsonObject.put("endpoint","http://10.226.20.27:9999");
        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString());
        restTemplate.put("http://"+WPSURL+":20107/api/app/v1/config",entity, String.class);
    }


    //获取编辑链接
    @Test
    public void getEditUrl()  {
        //请替换文件id
        ResponseEntity<String> entity = restTemplate.getForEntity("http://"+WPSURL+"/open/api/edit/v1/files/11111/link?type=w", String.class);
        System.out.println("状态码:" + entity.getStatusCode());
        System.out.println("响应体" + entity.getBody());
        ResponseEntity.ok(entity.getBody());
    }


    //获取预览链接
    @Test
    public void getPreviewUrl() throws IOException {
        CloseableHttpClient httpClient = HttpClients.custom().addInterceptorFirst(requestInterceptor).build();
        //请替换文件id
        HttpGet httpGet = new HttpGet("http://"+WPSURL+"/open/api/preview/v1/files/11111/link?type=w");
        HttpResponse httpresponse = httpClient.execute(httpGet);
        System.out.println(HMacUtils.getStringByInputStream_1(httpresponse.getEntity().getContent()));
    }

//    格式处理
    @Test
    public void formatExecute() {
        JSONObject jsonObject = new JSONObject(new FormatExecuteBean()
                .setSrc_uri("http://10.226.20.105/wpsupdate/%E6%A0%B7%E7%AB%A0/wps/DOC1_10MB.doc")
                .setFile_name("DOC1_10MB.doc")
                .setExport_type("png")
                .setTask_id("taskid001")
                .setLong_pic(false)
                .setLong_txt(false)
                .setHold_line_feed(false)
                .setPassword("1111")
                .setShow_comments(false)
                .setFrom_page(1)
                .setTo_page(1)
                .setScale(100)
                .setQuality(1)
                .setSheet_count(1)
                .setSheet_index(1)
                .setFit_to_width(10)
                .setFit_to_height(10)
                .set_horizontal(false)
                .setPaper_size(0)
                .setFirst_page(false)
                .setMax_sheet_col(0)
                .setMax_sheet_row(0)
                .setMulti_page(2)
                .setSlim_type(1)
        );
        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString());
        ResponseEntity<String> entity2 = restTemplate.postForEntity("http://"+WPSURL+"/open/api/convert/v1/file/invoke",entity, String.class);
        System.out.println(entity2.getBody());

    }

}
    
