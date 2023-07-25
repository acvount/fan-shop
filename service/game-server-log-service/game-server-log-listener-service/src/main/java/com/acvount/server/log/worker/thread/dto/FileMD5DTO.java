package com.acvount.server.log.worker.thread.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : acfan
 * date : create in 2023/7/23 12:54
 * description :
 **/

@Data
public class FileMD5DTO implements Serializable {
    private String type;
    private String lastFileMd5;
    private Long lastLength;
    private String lastFileName;
    private LocalDateTime lastTime;
}
