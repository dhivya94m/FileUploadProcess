package com.org.bchio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.org.bchio.model.FileDetails;

public interface FileDetailsRepository
		extends JpaRepository<FileDetails, String>, JpaSpecificationExecutor<FileDetails> {

	FileDetails findByFileNameOrChecksum(String fileName, String checksum);

}
