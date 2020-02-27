package com.happy3w.utils.downlod.httpdownload;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.net.MalformedURLException;

@SpringBootApplication
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

}
