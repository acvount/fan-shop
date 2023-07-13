package com.acvount.sms.code.api;

import com.acvount.sms.code.bean.SendPhoneCodeSmsDTO;

/**
 * @author : acfan
 * date : create in 2023/7/1 13:17
 * description :
 **/

abstract class PhoneCodeService {

    public abstract boolean sendPhoneCode(SendPhoneCodeSmsDTO sendPhoneCodeSmsDTO);
}
