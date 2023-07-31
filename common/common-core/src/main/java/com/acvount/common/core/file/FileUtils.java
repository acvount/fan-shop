package com.acvount.common.core.file;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : acfan
 * date : create in 2023/7/30 18:03
 * description :
 **/

public class FileUtils {
    public static String suffixName(MultipartFile file) {
        String suffix = ".png";
        if (StringUtils.isNotBlank(file.getOriginalFilename()) && file.getOriginalFilename().lastIndexOf('.') > 0) {
            return file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'));
        }
        return suffix;
    }
}
