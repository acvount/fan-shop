package com.acvount.user.api;

import com.acvount.user.bean.UserAuthorization;
import com.acvount.user.bean.UserInfo;

/**
 * @author : acfan
 * date : create in 2023/7/1 19:30
 * description :
 **/

@SuppressWarnings("unused")
public interface UserService {
    UserInfo createUser(UserInfo userInfo, UserAuthorization userAuthorization);

    UserInfo getUserByMobile(String phone);

    UserAuthorization getUserAuthorizationByMobile(String phone);

    Integer modifyServerFlag(Long userId);
}
