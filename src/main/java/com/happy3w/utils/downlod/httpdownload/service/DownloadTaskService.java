package com.happy3w.utils.downlod.httpdownload.service;

import com.happy3w.utils.downlod.httpdownload.mode.DownloadTask;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DownloadTaskService {
    private List<DownloadTask> tasks = Collections.synchronizedList(new ArrayList<>());

    public void addTask(DownloadTask task) {
        tasks.add(task);
    }

    public void deleteTask(DownloadTask task) {
        task.setCanceled(true);
        tasks.remove(task);
    }

    public List<DownloadTask> getTasks() {
        return tasks;
    }
}
