package com.acvount.server.log.worker.thread.dto;

import com.acvount.server.log.domain.ftp.entity.ServerFTP;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author : acfan
 * date : create in 2023/7/23 12:34
 * description :
 **/

@Data
public class FTPMetadata {
    private ServerFTP serverFTP;
    private String threadId;
    private LocalDateTime createTime;
    private LocalDateTime lastRunTime;
    private Map<String, FileMD5DTO> fileTypes;
}
