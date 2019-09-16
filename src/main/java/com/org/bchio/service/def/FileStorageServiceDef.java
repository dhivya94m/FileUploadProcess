package com.org.bchio.service.def;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.org.bchio.constants.GenericConstants;
import com.org.bchio.dto.FileDetailsDto;
import com.org.bchio.dto.TransactionSummaryDto;
import com.org.bchio.exceptions.CustomException;
import com.org.bchio.exceptions.MyFileNotFoundException;
import com.org.bchio.model.FileDetails;
import com.org.bchio.model.TransactionsSummary;
import com.org.bchio.properties.FileStorageProperties;
import com.org.bchio.repository.FileDetailsRepository;
import com.org.bchio.service.component.FileDetailsSpecification;
import com.org.bchio.service.decl.FileStorageService;
import com.org.bchio.service.event.FileUploadEvent;

@Service
public class FileStorageServiceDef implements FileStorageService {

	private final Path fileStorageLocation;

	@Autowired
	private FileDetailsRepository fileDetailsRepository;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	@Autowired
	private FileDetailsSpecification fileDetailsSpecification;

	@Autowired
	public FileStorageServiceDef(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new CustomException("Directory creation failed.", ex);
		}
	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}

	@Override
	public boolean save(FileDetailsDto dto) {
		return false;
	}

	@Override
	public FileDetailsDto save(MultipartFile file) {
		String fileName = null;
		FileDetailsDto fdDto = null;
		FileDetails fd = null;
		String checksumMD5 = null;
		FileUploadEvent fileUploadEvent = null;
		try {
			fileName = StringUtils.cleanPath(file.getOriginalFilename());
			checksumMD5 = DigestUtils.md5DigestAsHex(file.getInputStream());
			fd = fileDetailsRepository.findByFileNameOrChecksum(fileName, checksumMD5);
			if (fd != null) {
				throw new CustomException("File already exists.");
			} else {
				if (fileName.contains("..")) {
					throw new CustomException("Invalid path " + fileName);
				}
				Path targetLocation = this.fileStorageLocation.resolve(fileName);
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
				fd = new FileDetails(fileName, file.getContentType(), file.getSize(), checksumMD5,
						GenericConstants.statusPending, null, null, null, null, null, null, null, null, null, null,
						null);
				fileDetailsRepository.save(fd);
				fdDto = new FileDetailsDto();
				BeanUtils.copyProperties(fd, fdDto);
				fileUploadEvent = new FileUploadEvent(this, fdDto);
				applicationEventPublisher.publishEvent(fileUploadEvent);
				return fdDto;
			}
		} catch (IOException ex) {
			throw new CustomException("Could not store file " + fileName + ". Please try again!", ex);
		} finally {
			fileName = null;
			fdDto = null;
			fd = null;
			checksumMD5 = null;
			fileUploadEvent = null;
		}
	}

	@Override
	public Page<FileDetailsDto> getPaginatedData(Pageable pageable) {
		return fileDetailsRepository.findAll(pageable).map(this::mapEntityToDto);
	}

	@Override
	public FileDetailsDto mapEntityToDto(FileDetails entity) {
		FileDetailsDto dto = null;
		try {
			dto = new FileDetailsDto();
			BeanUtils.copyProperties(entity, dto);
			dto.setTransactionsSummary(entity.getTransactionsSummary().stream()
					.map(this::mapEntityToTransactionSummaryDto).collect(Collectors.toList()));
			return dto;
		} finally {
			dto = null;
		}
	}

	private TransactionSummaryDto mapEntityToTransactionSummaryDto(TransactionsSummary entity) {
		TransactionSummaryDto dto = null;
		try {
			dto = new TransactionSummaryDto();
			BeanUtils.copyProperties(entity, dto);
			return dto;
		} finally {
			dto = null;
		}
	}

	@Override
	public Page<FileDetailsDto> getPaginatedData(Pageable pageable, String searchParam) {
		return fileDetailsRepository.findAll(fileDetailsSpecification.isLike(searchParam), pageable)
				.map(this::mapEntityToDto);
	}

}
