package com.acvount.common.file.aspect;

import com.acvount.common.file.platform.FileStorage;
import com.acvount.common.file.FileInfo;

/**
 * 文件是否存在切面调用链结束回调
 */
public interface ExistsAspectChainCallback {
    boolean run(FileInfo fileInfo, FileStorage fileStorage);
}
