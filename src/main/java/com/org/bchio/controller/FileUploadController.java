package com.org.bchio.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class FileUploadController {

	Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private FileStorageService fileStorageService;

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

}
