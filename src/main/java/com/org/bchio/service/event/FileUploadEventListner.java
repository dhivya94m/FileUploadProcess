package com.org.bchio.service.event;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.org.bchio.constants.GenericConstants;
import com.org.bchio.model.FileDetails;
import com.org.bchio.repository.FileDetailsRepository;

@Component
public class FileUploadEventListner implements ApplicationListener<FileUploadEvent> {

	Logger logger = LoggerFactory.getLogger(FileUploadEventListner.class);

	@Autowired
	private FileDetailsRepository fileDetailsRepository;
	@Autowired
	private FileUploadEventService fileUploadEventService;

	@Override
	public void onApplicationEvent(FileUploadEvent event) {
		Optional<FileDetails> fileDetails = null;
		try {
			logger.info("Inside Event...");
			fileDetails = fileDetailsRepository.findById(event.getFileDetailsDto().getFileName());
			if (fileDetails.isPresent()) {
				if (fileUploadEventService.loadDataWithEntityManager(fileDetails.get())) {
					fileDetails.get().setStatus(GenericConstants.statusCompleted);
				} else {
					fileDetails.get().setStatus(GenericConstants.statusError);
				}
				fileDetailsRepository.save(fileDetails.get());
			} else {
				logger.info("File not exists.");
			}
		} catch (Exception ex) {
			logger.error("EVENT ERROR: " + ex);
		} finally {

		}

	}

}
