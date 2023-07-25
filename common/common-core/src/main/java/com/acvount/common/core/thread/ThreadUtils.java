package com.acvount.common.core.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author : acfan
 * date : create in 2023/7/22 10:22
 * description :
 **/

public class ThreadUtils {
    public static String programName() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        return runtimeBean.getName();
    }

    public static String getCurrentHostName() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

}
