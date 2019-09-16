package com.org.bchio.service.event;


import org.springframework.context.ApplicationEvent;

import com.org.bchio.dto.FileDetailsDto;

public class FileUploadEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FileDetailsDto fileDetailsDto;

	public FileUploadEvent(Object source, FileDetailsDto fileDetailsDto) {
		super(source);
		this.fileDetailsDto = fileDetailsDto;
	}

	public FileDetailsDto getFileDetailsDto() {
		return fileDetailsDto;
	}

}
