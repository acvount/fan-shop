package com.acvount.common.core.id;

import com.acvount.common.core.security.CalculateUtils;
import com.acvount.common.core.thread.ThreadUtils;

/**
 * @author : acfan
 * date : create in 2023/7/22 10:32
 * description :
 **/

public class IDUtils {

    public static String programID(){
        return CalculateUtils.md5(ThreadUtils.programName()+ThreadUtils.getCurrentHostName());
    }
}
