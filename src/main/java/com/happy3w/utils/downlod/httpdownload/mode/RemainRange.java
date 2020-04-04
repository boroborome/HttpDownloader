package com.happy3w.utils.downlod.httpdownload.mode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemainRange {
    private DownloadTask task;
    private long start;
    private long end;

    public synchronized RemainRange split() {
        long size = end - start;
        long newStart = start + size;

        RemainRange range = new RemainRange(task, newStart, end);
        end = newStart;
        return range;
    }
}
