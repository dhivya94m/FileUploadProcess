package com.org.bchio.config;

import java.text.SimpleDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
//@EnableAsync
public class BeanConfig {

	@Bean
	public SimpleDateFormat simpleDateFormat() {
		return new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
	}

	@Bean(name = "applicationEventMulticaster")
	public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
		SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
		eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return eventMulticaster;
	}

	/*
	 * @Bean public Executor dataInsertExecutor() { ThreadPoolTaskExecutor executor
	 * = new ThreadPoolTaskExecutor(); executor.setCorePoolSize(25);
	 * executor.setMaxPoolSize(10000); executor.setQueueCapacity(100000);
	 * executor.setThreadNamePrefix("DATA_INSERT_EXEC_"); executor.initialize();
	 * return executor; }
	 */

}
