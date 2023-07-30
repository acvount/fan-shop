package com.acvount.common.file.aspect;

import com.acvount.common.file.platform.FileStorage;
import com.acvount.common.file.FileInfo;
import com.acvount.common.file.UploadPretreatment;
import com.acvount.common.file.recorder.FileRecorder;

/**
 * 上传切面调用链结束回调
 */
public interface UploadAspectChainCallback {
    FileInfo run(FileInfo fileInfo, UploadPretreatment pre, FileStorage fileStorage, FileRecorder fileRecorder);
}
