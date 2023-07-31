package com.acvount.user.service;

import com.acvount.common.core.exception.BaseException;
import com.acvount.common.core.file.FileUtils;
import com.acvount.common.core.id.SnowflakeIdGenerator;
import com.acvount.common.core.result.Result;
import com.acvount.common.file.FileInfo;
import com.acvount.common.file.FileStorageService;
import com.acvount.user.bean.UserInfo;
import com.acvount.web.LoginUser;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author : acfan
 * date : create in 2023/7/30 17:50
 * description :
 **/

@Service
public class UserInfoService {

    @Resource
    private UserService userService;

    @Resource
    private FileStorageService fileStorageService;

    @Resource
    private SnowflakeIdGenerator snowflakeIdGenerator;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Transactional
    public Result<UserInfo> changeHeaderImage(MultipartFile file) throws BaseException {
        Long loginUserID = LoginUser.getLoginUserID();
        UserInfo userInfo = userService.selectById(loginUserID);

        deleteHistoryHeaderImage(userInfo);
        FileInfo upload = upload(file, loginUserID);

        userService.updateUserById(UserInfo.builder()
                .userId(userInfo.getUserId())
                .avatar(upload.getUrl())
                .thumbnailAvatar(upload.getThUrl())
                .build());

        userInfo.setAvatar(upload.getUrl());
        userInfo.setThumbnailAvatar(upload.getThUrl());
        return Result.success(userInfo);
    }

    private FileInfo upload(MultipartFile file, Long userId) {
        String fileName = "%s_%s".formatted(snowflakeIdGenerator.nextId(), userId);
        return fileStorageService.of(file)
                .setPath(formatter.format(LocalDate.now()))
                .setSaveFilename(fileName + FileUtils.suffixName(file))
                .setSaveThFilename(fileName).upload();
    }

    private void deleteHistoryHeaderImage(UserInfo userInfo) {
        if (userInfo.getAvatar().indexOf("default-headpic.jpg") > 0) {
            fileStorageService.delete(userInfo.getAvatar());
            fileStorageService.delete(userInfo.getThumbnailAvatar());
        }
    }

    public UserInfo getUserDetail() throws BaseException {
        return userService.selectById(LoginUser.getLoginUserID());
    }
}
