package com.org.bchio.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
	private String uploadDir;

	public String getUploadDir() {
		if (uploadDir != null && (uploadDir.endsWith("/") || uploadDir.endsWith("\\"))) {
			return uploadDir;
		} else {
			return uploadDir + "/";
		}

	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
}
