package com.org.bchio.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.org.bchio.properties.FileStorageProperties;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@EnableConfigurationProperties({ FileStorageProperties.class })
public class WebConfig {

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

}
