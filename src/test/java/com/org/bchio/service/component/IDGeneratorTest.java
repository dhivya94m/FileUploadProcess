package com.org.bchio.service.component;

import static org.junit.Assert.*;

import org.junit.Test;

public class IDGeneratorTest {

	@Test
	public void getId() {
		assertNotNull(IDGenerator.getId());
	}

}
