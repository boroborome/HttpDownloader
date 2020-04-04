package com.happy3w.utils.downlod.httpdownload.service;

import com.happy3w.utils.downlod.httpdownload.DownloadConst;
import com.happy3w.utils.downlod.httpdownload.mode.DownloadTask;
import com.happy3w.utils.downlod.httpdownload.mode.RemainRange;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class DownloadWorkerService {
    @Autowired
    private DownloadTaskService downloadTaskService;

    @Autowired
    private ExecutorService executorService;
    private List<RemainRange> remainRangeInQueue = Collections.synchronizedList(new ArrayList<>());

    @Scheduled(fixedDelay = 5000l)
    public void triggerLookupWork() {
        try {
            lookupWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lookupWork() {
        for (DownloadTask task : downloadTaskService.getTasks()) {
            ensureThreadCount(task);
            ensureRemainRangeDownloading(task);
        }
    }

    private void ensureRemainRangeDownloading(DownloadTask task) {
        for (RemainRange range : task.getRemainRanges()) {
            if (remainRangeInQueue.contains(range)) {
                continue;
            }
            remainRangeInQueue.add(range);
            executorService.submit(new DownloadWorkerTask(task, range));
        }
    }

    @AllArgsConstructor
    private class DownloadWorkerTask implements Runnable {
        private DownloadTask downloadTask;
        private RemainRange range;

        @Override
        public void run() {
            try {
                if (downloadFilePart()) {
                    downloadTask.getRemainRanges().remove(range);
                }
            } catch (Exception e) {
                log.error("Unknown error", e);
            } finally {
                remainRangeInQueue.remove(range);
            }
        }

        private boolean downloadFilePart() throws Exception {
            log.info("Start to download:" + range.getStart() + "====" + range.getEnd());
            URL url = new URL(downloadTask.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(15000);
            //设置请求数据的区间
            conn.setRequestProperty("Range", "bytes=" + range.getStart() + "-" + range.getEnd());
            //请求部分数据的响应码是206
            if (conn.getResponseCode() == 206) {
                //获取一部分数据来读取
                InputStream is = conn.getInputStream();
                byte[] b = new byte[1024];
                int len = 0;
                int total = 0;
                //拿到临时文件的引用
                File targetFile = new File(downloadTask.getDir());
                RandomAccessFile raf = new RandomAccessFile(targetFile, "rwd");
                //更新文件的写入位置，startIndex
                raf.seek(range.getStart());
                while ((len = is.read(b)) != -1) {
                    //每次读取流里面的数据，同步吧数据写入临时文件
                    raf.write(b, 0, len);
                    total += len;
                    range.moveStart(len);
                }
                log.info("Finished");
                raf.close();
                return true;
            }
            return false;
        }
    }

    private void ensureThreadCount(DownloadTask task) {
        if (task.getRemainRanges() == null) {
            initRemainRange(task);
        }

        if (task.getRemainRanges() != null) {
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

    private void initRemainRange(DownloadTask task) {
        try {
            URL url = new URL(task.getUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            if (conn.getResponseCode() == 200) {
                int length = conn.getContentLength();
                task.setTotalSize(length);

                long rangeSize = calculateRangeSize(length);
                List<RemainRange> ranges = new ArrayList<>();
                for (int start = 0; start < length; start += rangeSize) {
                    long end = start + rangeSize;
                    if (end > length) {
                        end = length;
                    }
                    ranges.add(new RemainRange(start, end));
                }
                task.setRemainRanges(ranges);
            } else {
                task.setMessage(conn.getResponseMessage());
            }
        } catch (Exception e) {
            task.setMessage("Error:" + e.getMessage());
            log.error("Unknown error", e);
        }
    }

    private long calculateRangeSize(int length) {
        if (length > DownloadConst.ThreadCount * DownloadConst.MinRangeSize) {
            return length / DownloadConst.ThreadCount;
        }
        return DownloadConst.MinRangeSize;
    }
}
