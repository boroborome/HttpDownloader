package com.happy3w.utils.downlod.httpdownload.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.happy3w.utils.downlod.httpdownload.mode.DownloadTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DownloadTaskService {
    private static final String CONFIG_FILE = "config.json";
    private List<DownloadTask> tasks = Collections.synchronizedList(new ArrayList<>());

    public void addTask(DownloadTask task) {
        File file = new File(task.getDir());
        task.setFileName(file.getName());

        tasks.add(task);
    }

    public void deleteTask(DownloadTask task) {
        task.setCanceled(true);
        tasks.remove(task);
    }

    public List<DownloadTask> getTasks() {
        return tasks;
    }

    @PostConstruct
    public void loadTasks() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try {
                InputStream intput = Files.newInputStream(new File(CONFIG_FILE).toPath());
                List<DownloadTask> newTasks = JSON.parseObject(intput,
                        new ParameterizedTypeImpl(new Type[]{DownloadTask.class}, null, List.class){});
                tasks.addAll(newTasks);
            } catch (IOException e) {
                log.error("Unknown error", e);
            }
        }
    }


    @Scheduled(fixedDelay = 5000l)
    public void triggerSaveStage() {
        try {
            saveTasks();
        } catch (Exception e) {
            log.error("Unknown error", e);
        }
    }
    public void saveTasks() {
        try {
            OutputStream output = Files.newOutputStream(new File(CONFIG_FILE).toPath());
            JSON.writeJSONString(output, tasks);
        } catch (IOException e) {
           log.error("Unknown error", e);
        }
    }
}
