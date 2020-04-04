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
    private long start;
    private long end;

    public synchronized RemainRange split() {
        long size = end - start;
        long newStart = start + size / 2;

        RemainRange range = new RemainRange(newStart, end);
        end = newStart;
        return range;
    }

    public void moveStart(int len) {
        start += len;
    }
}
