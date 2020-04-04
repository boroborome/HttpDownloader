package com.happy3w.utils.downlod.httpdownload.mode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadTask {
    private String fileName;
    private String url;
    private long totalSize;
    private long downloadSize;
    private String message;

    private String dir;
    private List<RemainRange> remainRanges;
    private int taskCount;
    private boolean canceled;
}
