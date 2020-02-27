package com.happy3w.utils.downlod.httpdownload.mode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadTask {
    private String fileName;
    private String url;
    private long totalSize;
    private long downloadSize;

    private String dir;
    private int taskCount;
}
