package com.org.bchio.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.org.bchio.dto.FileDetailsDto;
import com.org.bchio.service.decl.FileStorageService;

@Controller
public class FileStorageController {

	Logger logger = LoggerFactory.getLogger(FileStorageController.class);

	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private ResourceLoader resourceLoader;

	@RequestMapping("/")
	public ModelAndView index(Model model) {
		return getPagedData(1);
	}

	@RequestMapping("/getPagedData/{page}")
	public ModelAndView getPagedData(@PathVariable("page") int page) {
		ModelAndView modelAndView = new ModelAndView("index");
		PageRequest pageable = null;
		Page<FileDetailsDto> fileDetailsPage = null;
		List<Integer> pageNumbers = null;
		try {
			if (page == 0)
				page = 1;
			pageable = PageRequest.of(page - 1, 15);
			fileDetailsPage = fileStorageService.getPaginatedData(pageable);
			int totalPages = fileDetailsPage.getTotalPages();
			if (totalPages > 0) {
				pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				modelAndView.addObject("pageNumbers", pageNumbers);
			}
			modelAndView.addObject("fileDetailsListStatus", true);
			modelAndView.addObject("fileDetailsList", fileDetailsPage.getContent());

		} catch (Exception ex) {

		} finally {
			pageable = null;
			fileDetailsPage = null;
			pageNumbers = null;

		}
		return modelAndView;
	}

	@RequestMapping("/getSearchData")
	public ModelAndView getSearchData(@RequestParam("fileName") String fileName) {
		ModelAndView modelAndView = new ModelAndView("index");
		PageRequest pageable = null;
		Page<FileDetailsDto> fileDetailsPage = null;
		List<Integer> pageNumbers = null;
		try {
			pageable = PageRequest.of(0, 15);
			fileDetailsPage = fileStorageService.getPaginatedData(pageable, fileName);
			int totalPages = fileDetailsPage.getTotalPages();
			if (totalPages > 0) {
				pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				modelAndView.addObject("pageNumbers", pageNumbers);
			}
			modelAndView.addObject("fileDetailsListStatus", true);
			modelAndView.addObject("fileDetailsList", fileDetailsPage.getContent());

		} catch (Exception ex) {

		} finally {
			pageable = null;
			fileDetailsPage = null;
			pageNumbers = null;

		}
		return modelAndView;
	}

	@PostMapping("/uploadFile")
	@ResponseBody
	public FileDetailsDto uploadFile(@RequestParam("file") MultipartFile file) {
		FileDetailsDto dto = null;
		try {
			dto = fileStorageService.save(file);
			return dto;
		} finally {
			dto = null;
		}
	}

	@GetMapping("/downloadSample")
	public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
		String contentType = null;
		Resource resource = null;
		try {
			resource = resourceLoader.getResource("classpath:sample/sample.zip");
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Problem Occoured : " + ex);
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}
