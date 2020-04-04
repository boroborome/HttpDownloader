package com.happy3w.utils.downlod.httpdownload.mode;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadTask {
    private String url;
    private String dir;
    private List<RemainRange> remainRanges;

    private String fileName;

    @Builder.Default
    private long totalSize = -1;

    private String message;

    private int taskCount;
    private boolean canceled;

    @JSONField(serialize = false)
    public long getRemainSize() {
        if (totalSize < 0) {
            return -1;
        }
        if (CollectionUtils.isEmpty(remainRanges)) {
            return 0;
        }
        long size = 0;
        for (RemainRange range : remainRanges) {
            size += range.getEnd() - range.getStart();
        }
        return size;
    }

    @JSONField(serialize = false)
    public long getDownloadSize() {
        if (totalSize < 0) {
            return -1;
        }

        long remainSize = getRemainSize();
        if (remainSize < 0) {
            return -1;
        }
        return totalSize - remainSize;
    }
}
