package com.acvount.user.api;

import com.acvount.user.bean.UserAuthorization;
import com.acvount.user.bean.UserInfo;

/**
 * @author : acfan
 * date : create in 2023/7/1 19:30
 * description :
 **/

public interface UserService {
    public UserInfo createUser(UserInfo userInfo, UserAuthorization userAuthorization);

    public UserInfo getUserByMobile(String phone);

    public UserAuthorization getUserAuthorizationByMobile(String phone);
}
