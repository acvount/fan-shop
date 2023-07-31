package com.acvount.user.service;

import com.acvount.common.core.exception.BaseException;
import com.acvount.user.bean.UserAuthorization;
import com.acvount.web.LoginUser;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author : acfan
 * date : create in 2023/7/31 13:33
 * description :
 **/

@Service
public class UserCheckService {
    @Resource
    private UserAuthorizationService userAuthorizationService;

    public Boolean checkLoginUserIsNeedRenewPassword() throws BaseException {
        UserAuthorization userAuthorization = userAuthorizationService.selectById(LoginUser.getLoginUserID());
        return StringUtils.isBlank(userAuthorization.getPasswordHash());
    }
}
