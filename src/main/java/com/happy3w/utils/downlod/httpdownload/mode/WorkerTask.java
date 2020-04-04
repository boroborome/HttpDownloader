package com.happy3w.utils.downlod.httpdownload.mode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerTask {
    private DownloadTask downloadTask;
    private RemainRange range;
}
