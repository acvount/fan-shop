package com.acvount.auth.login.service.login;

import com.acvount.auth.login.bean.dto.PhoneLoginRedisDTO;
import com.acvount.auth.login.bean.vo.LoginVO;
import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.exception.config.ExceptionType;
import com.acvount.common.core.id.SnowflakeIdGenerator;
import com.acvount.sms.code.bean.SendPhoneCodeSmsDTO;
import com.acvount.sms.code.constants.RocketMQConstants;
import com.acvount.sms.code.constants.topic.LoginTopicConstants;
import com.acvount.user.api.UserServiceApi;
import com.acvount.user.bean.UserInfo;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author : acfan
 * date : create in 2023/7/1 15:18
 * description : 手机号登陆服务
 **/

@Service
public class PhoneLoginService {
    public static final String TEMPLATE_CODE = "SMS_269465068";
    public static final String SIGN_NAME = "俄芒特";
    public static final String REDIS_PREFIX = "login_code_";
    public static final Integer ERROR_COUNT = 5;
    @SuppressWarnings("unused")
    @DubboReference
    private UserServiceApi userServiceApi;
    @Resource
    private StreamBridge streamBridge;
    @Resource
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private LoginService loginService;


    public boolean sendCode(String phone) {
        String redisCodeKey = REDIS_PREFIX + phone;
        SendPhoneCodeSmsDTO sendPhoneCodeSmsDTO = buildSendPhoneCodeSmsDTO(phone);
        PhoneLoginRedisDTO phoneLoginRedisDTO = PhoneLoginRedisDTO.builder()
                .code(sendPhoneCodeSmsDTO.getCode())
                .createTimestamp(System.currentTimeMillis())
                .errorCount(0).build();
        Message<SendPhoneCodeSmsDTO> msg = MessageBuilder.withPayload(sendPhoneCodeSmsDTO)
                .setHeader(RocketMQConstants.ROCKET_MQ_GROUP_HEADER, "login-code")
                .setHeader(RocketMQConstants.ROCKET_MQ_CONTENT_TYPE_HEADER_NAME, RocketMQConstants.ROCKET_MQ_APPLICATION_JSON)
                .build();
        redisTemplate.opsForValue().set(redisCodeKey, JSON.toJSONString(phoneLoginRedisDTO), 5, TimeUnit.MINUTES);
        streamBridge.send(LoginTopicConstants.SEND_CODE_TOPIC, msg);
        return true;
    }

    @SuppressWarnings("all")
    public String checkCode(LoginVO loginVO) throws BaseException {
        String redisCodeKey = REDIS_PREFIX + loginVO.getPhone();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(redisCodeKey))) {
            throw new BaseException(ExceptionType.Code_Not_Found);
        }
        PhoneLoginRedisDTO loginDto = JSON.parseObject(redisTemplate.opsForValue().get(redisCodeKey), PhoneLoginRedisDTO.class);
        if (System.currentTimeMillis() - loginDto.getCreateTimestamp() > TimeUnit.MINUTES.toMillis(3)) {
            redisTemplate.delete(redisCodeKey);
            throw new BaseException(ExceptionType.Code_Error_Time_Out);
        }
        if (loginDto.getCode().equals(loginVO.getCode())) {
            UserInfo user = loginService.checkAccountPhoneAndCreateIfNeeded(loginVO.getPhone());
            redisTemplate.delete(redisCodeKey);
            return loginService.login(user.getUserId());
        }
        checkIfExceededMaxAttemptsForVerificationCode(loginDto, redisCodeKey);
        throw new BaseException(ExceptionType.Code_Mismatch);
    }

    private void checkIfExceededMaxAttemptsForVerificationCode(PhoneLoginRedisDTO loginDto, String redisCodeKey) throws BaseException {
        String errorMessage = "验证码错误！还有 %s 次机会";
        ExceptionType code_error = ExceptionType.Code_Mismatch;
        if (loginDto.getErrorCount() >= ERROR_COUNT) {
            redisTemplate.delete(redisCodeKey);
            throw new BaseException(ExceptionType.Code_Error_Count_Gt_5);
        }
        Integer reTryCount = ERROR_COUNT - loginDto.getErrorCount();
        code_error.setMessage(String.format(errorMessage, reTryCount));
        loginDto.setErrorCount(loginDto.getErrorCount() + 1);
        redisTemplate.opsForValue().set(redisCodeKey, JSON.toJSONString(loginDto));
        throw new BaseException(code_error);
    }

    private SendPhoneCodeSmsDTO buildSendPhoneCodeSmsDTO(String phone) {
        return SendPhoneCodeSmsDTO.builder().phone(phone).signName(SIGN_NAME).templateCode(TEMPLATE_CODE).code(generateVerificationCode()).outId(String.valueOf(snowflakeIdGenerator.nextId())).build();
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
}
