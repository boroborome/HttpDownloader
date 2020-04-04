package com.happy3w.utils.downlod.httpdownload.service;

import com.happy3w.utils.downlod.httpdownload.DownloadConst;
import com.happy3w.utils.downlod.httpdownload.mode.DownloadTask;
import com.happy3w.utils.downlod.httpdownload.mode.RemainRange;
import com.happy3w.utils.downlod.httpdownload.mode.WorkerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class DownloadWorkerService {
    @Autowired
    private DownloadTaskService downloadTaskService;

    private Map<DownloadTask, List<WorkerTask>> workerTaskMap = new HashMap<>();

    @Scheduled(fixedDelay = 5000l)
    public void triggerLookupWork() {
        try {
            lookupWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 5000l)
    public void triggerSaveStage() {
        try {
            saveStage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveStage() {
        // TODO

    }

    private void lookupWork() {
        for (DownloadTask task : downloadTaskService.getTasks()) {
            ensureThreadCount(task);
            ensureRemainRangeDownloading(task);
        }
    }

    private void ensureRemainRangeDownloading(DownloadTask task) {
        List<WorkerTask> workers = workerTaskMap.get(task);
        // TODO
        for (RemainRange range : task.getRemainRanges()) {

        }
    }

    private void ensureThreadCount(DownloadTask task) {
        if (task.getRemainRanges() == null) {
            initRemainRange(task);
        }

        if (task.getRemainRanges() != null) {
            cleanFinishedRemainRange(task);
            while (task.getRemainRanges().size() < DownloadConst.ThreadCount) {
                RemainRange range = findBigEnoughRange(task);
                if (range == null) {
                    break;
                }
                RemainRange newRange = range.split();
                task.getRemainRanges().add(newRange);
            }
        }
    }

    private RemainRange findBigEnoughRange(DownloadTask task) {
        for (RemainRange range : task.getRemainRanges()) {
            long size = range.getEnd() - range.getStart();
            if (size >= DownloadConst.MinRangeSize * 2) {
                return range;
            }
        }
        return null;
    }

    private void cleanFinishedRemainRange(DownloadTask task) {
        Iterator<RemainRange> it = task.getRemainRanges().iterator();
        while (it.hasNext()) {
            RemainRange range = it.next();
            if (range.getStart() >= range.getEnd()) {
                it.remove();
            }
        }
    }

    private void initRemainRange(DownloadTask task) {
        try {
            URL url = new URL(task.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            if (conn.getResponseCode() == 200) {
                int length = conn.getContentLength();
                long rangeSize = calculateRangeSize(length);
                List<RemainRange> ranges = new ArrayList<>();
                for (int start = 0; start < length; start += rangeSize) {
                    long end = start + rangeSize;
                    if (end > length) {
                        end = length;
                    }
                    ranges.add(new RemainRange(task, start, end));
                }
                task.setRemainRanges(ranges);
            } else {
                task.setMessage(conn.getResponseMessage());
            }
        } catch (Exception e) {
            task.setMessage("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private long calculateRangeSize(int length) {
        if (length > DownloadConst.ThreadCount * DownloadConst.MinRangeSize) {
            return length / DownloadConst.ThreadCount;
        }
        return DownloadConst.MinRangeSize;
    }
}
