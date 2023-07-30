package com.acvount.sms;

import com.acvount.sms.code.bean.SendPhoneCodeSmsDTO;
import com.alibaba.fastjson.JSON;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author : acfan
 * date : create in 2023/7/30 20:13
 * description :
 **/

@Slf4j
@Service
public class AliSmsService {
    private static final String KEY = "LTAI5tG556cnJQXZJ6cj58xN";
    private static final String SECRET = "zriUKcJfrPUpOJrWzkINivn8FihVFS";


    public void sendSMS(SendPhoneCodeSmsDTO sendPhoneCodeSmsDTO) {
        try {
            StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder().accessKeyId(KEY).accessKeySecret(SECRET).build());
            SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                    .phoneNumbers(sendPhoneCodeSmsDTO.getPhone())
                    .signName(sendPhoneCodeSmsDTO.getSignName())
                    .outId(sendPhoneCodeSmsDTO.getOutId())
                    .templateCode(sendPhoneCodeSmsDTO.getTemplateCode())
                    .templateParam(JSON.toJSONString(Map.of("code", sendPhoneCodeSmsDTO.getCode())))
                    .build();
            AsyncClient client = AsyncClient.builder().credentialsProvider(provider).overrideConfiguration(ClientOverrideConfiguration.create().setEndpointOverride("dysmsapi.aliyuncs.com")).build();
            CompletableFuture<SendSmsResponse> sendSmsResponseCompletableFuture = client.sendSms(sendSmsRequest);
            client.close();


            log.info(sendSmsResponseCompletableFuture.toString());
        } catch (Exception e) {
            log.error(" send log error : {}", e.getMessage());
        }
    }
}
