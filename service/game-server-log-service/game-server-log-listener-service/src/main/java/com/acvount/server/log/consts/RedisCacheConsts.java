package com.acvount.server.log.consts;

/**
 * @author : acfan
 * date : create in 2023/7/21 21:45
 * description : 分配任务 缓存 常量
 **/

public class RedisCacheConsts {

    public static final String LeaderKey = "game-server-log-listener-leader";

    public static final String ServicePrefix = "game-server-server-";
    public static final String LeaderLockPrefix = "get-leader-lock-";

    public static final String ServerListKey = "game-log-listeners";
    public static final String TaskSuffix = "-tasks";

    public static final String FTP_Metadata_Prefix = "ftp-metadata-";
}
