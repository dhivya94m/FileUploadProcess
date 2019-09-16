package com.org.bchio.service.component;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {

	private static AtomicInteger uniqueId = new AtomicInteger();

	public static Integer getId() {
		return uniqueId.incrementAndGet();
	}

}
