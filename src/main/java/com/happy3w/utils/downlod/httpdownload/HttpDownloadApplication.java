package com.happy3w.utils.downlod.httpdownload;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableScheduling
public class HttpDownloadApplication {

	public static void main(String[] args) throws MalformedURLException {
//		HttpDownloader downloader = new HttpDownloader(
//				new URL("http://archive.apache.org/dist/mahout/0.11.0//apache-mahout-distribution-0.11.0.tar.gz"),
//				".",
//				10);
//
//		downloader.syncDownload();
		new SpringApplicationBuilder(HttpDownloadApplication.class)
			.headless(false)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	@Bean(destroyMethod = "shutdownNow")
	public ExecutorService jobMqThreadPool() {
		return Executors.newFixedThreadPool(30, new CustomizableThreadFactory("download-worker-"));
	}
}
