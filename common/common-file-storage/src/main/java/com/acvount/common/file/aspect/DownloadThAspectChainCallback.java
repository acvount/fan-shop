package com.acvount.common.file.aspect;

import com.acvount.common.file.platform.FileStorage;
import com.acvount.common.file.FileInfo;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 下载缩略图切面调用链结束回调
 */
public interface DownloadThAspectChainCallback {
    void run(FileInfo fileInfo, FileStorage fileStorage, Consumer<InputStream> consumer);
}
