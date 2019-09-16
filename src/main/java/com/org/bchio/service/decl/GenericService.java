package com.org.bchio.service.decl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface GenericService<T, T1> {

	boolean save(T dto);

	T save(MultipartFile file);

	Page<T> getPaginatedData(Pageable pageable);

	Page<T> getPaginatedData(Pageable pageable, String searchParam);

	T mapEntityToDto(T1 entity);

}
