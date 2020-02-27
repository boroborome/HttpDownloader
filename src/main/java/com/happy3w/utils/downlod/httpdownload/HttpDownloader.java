package com.happy3w.utils.downlod.httpdownload;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader {
    private final URL url;
    private final String localDir;
    private final int threadCount;

    public HttpDownloader(URL url, String localDir, int threadCount) {
        this.url = url;
        this.localDir = localDir;
        this.threadCount = threadCount;
    }

    public File syncDownload() {
        //发送get请求，请求这个地址的资源
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            if (conn.getResponseCode() == 200) {
                //获取到请求资源文件的长度
                int length = conn.getContentLength();
                File file = new File("apache-nutch-1.2-bin.tar.gz");
                //创建随机存储文件
                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                //设置临时文件的大小
                raf.setLength(length);
                //关闭raf
                raf.close();
                //计算出每一个线程下载多少字节

                int size = length / Multidownload.ThreadCount;

                for (int i = 0; i < Multidownload.ThreadCount; i++) {
                    //startIndex,endIndex分别代表线程的开始和结束位置
                    int startIndex = i * size;
                    int endIndex = (i + 1) * size - 1;
                    if (i == threadCount - 1) {
                        //如果是最后一个线程，那么结束位置写死
                        endIndex = length - 1;
                    }
                    //System.out.println("线程" + i + "的下载区间是" + startIndex + "到" + endIndex);
                    new DownLoadThread(startIndex, endIndex, i).start();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
