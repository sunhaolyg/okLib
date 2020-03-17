package com.sh.oklib.http;

import java.io.File;

public interface OnDownloadListener {
    void onDownloadFailed(Exception e);

    void onDownloading(int progress);

    void onDownloadSuccess(File file);
}
