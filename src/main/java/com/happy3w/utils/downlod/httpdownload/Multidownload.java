package com.happy3w.utils.downlod.httpdownload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class Multidownload {

    static int ThreadCount = 10;   //线程的个数
    static int finishedThread = 0;   //初始化下载完成的线程的个数
    static String path = "http://archive.apache.org/dist/nutch/apache-nutch-1.2-bin.tar.gz";  //确定下载地址
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        //发送get请求，请求这个地址的资源
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            if(conn.getResponseCode() == 200){
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

                for(int i = 0; i < Multidownload.ThreadCount; i ++){
                    //startIndex,endIndex分别代表线程的开始和结束位置
                    int startIndex = i * size;
                    int endIndex = (i + 1) * size - 1;
                    if(i == ThreadCount - 1){
                        //如果是最后一个线程，那么结束位置写死
                        endIndex = length -1;
                    }
                    //System.out.println("线程" + i + "的下载区间是" + startIndex + "到" + endIndex);
                    new DownLoadThread(startIndex, endIndex, i).start();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

class DownLoadThread extends Thread{
    int startIndex;
    int endIndex;
    int threadId;

    public DownLoadThread(int startIndex, int endIndex, int threadId) {
        super();
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        while(true) {
            try {
                download();
                break;
            } catch (Exception e) {
                System.out.println("Error in " + threadId);
                e.printStackTrace();
            }
        }
    }

    private void download() throws Exception {
        //使用http请求下载安装包文件
        URL url;
        File fileProgress = new File(threadId + ".txt");
        //判断存储下载进度的临时文件是否存在，
        if(fileProgress.exists()){
            FileInputStream fis = new FileInputStream(fileProgress);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            //从下载进度的临时文件中读取上一次下载的总进度，然后和原来文本的开始位置相加，得到新的下载位置
            startIndex += Integer.parseInt(br.readLine());
            fis.close();
        }
        System.out.println("线程" + threadId + "下载区间是" + startIndex +"====" + endIndex);
        url = new URL(Multidownload.path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(15000);
        //设置请求数据的区间
        conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
        //请求部分数据的响应码是206
        if(conn.getResponseCode() == 206){
            //获取一部分数据来读取
            InputStream is = conn.getInputStream();
            byte[] b = new byte[1024];
            int len = 0;
            int total = 0;
            //拿到临时文件的引用
            File file = new File("apache-nutch-1.2-bin.tar.gz");
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            //更新文件的写入位置，startIndex
            raf.seek(startIndex);
            while((len = is.read(b)) != -1 ){
                //每次读取流里面的数据，同步吧数据写入临时文件
                raf.write(b, 0, len);
                total += len;
                //System.out.println("线程" + threadId + "下载了" + total);

                //生成一个专门记录下载进度的临时文件
                //File fileProgress = new File(threadId + ".txt");
                RandomAccessFile fileProgressraf = new RandomAccessFile(fileProgress, "rwd");
                //每一次读取流里面的数据以后，把当前线程下载的总进度写入临时文件中
                fileProgressraf.write((total + "").getBytes());
                fileProgressraf.close();

            }
            System.out.println("线程" + threadId + "下载过程结束===========================");
            raf.close();
            //三条线程下载完成以后，清理临时文件
            Multidownload.finishedThread++;
            //线程安全
//                synchronized(Multidownload.path){
//                    if(Multidownload.finishedThread == Multidownload.ThreadCount){
//                        for(int i = 0; i < Multidownload.ThreadCount; i++){
//                            File filefinish = new File(i + ".txt");
//                            filefinish.delete();
//
//                        }
//                        Multidownload.finishedThread = 0;
//                    }
//                }
        }

    }
}
