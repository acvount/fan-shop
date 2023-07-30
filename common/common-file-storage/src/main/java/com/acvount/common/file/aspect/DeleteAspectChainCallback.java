package com.acvount.common.file.aspect;

import com.acvount.common.file.platform.FileStorage;
import com.acvount.common.file.FileInfo;
import com.acvount.common.file.recorder.FileRecorder;

/**
 * 删除切面调用链结束回调
 */
public interface DeleteAspectChainCallback {
    boolean run(FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder);
}
