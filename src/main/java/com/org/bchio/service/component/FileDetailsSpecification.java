package com.org.bchio.service.component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.org.bchio.model.FileDetails;

@Component
@SuppressWarnings("serial")
public class FileDetailsSpecification {

	public Specification<FileDetails> isLike(String param) {
		return new Specification<FileDetails>() {
			@Override
			public Predicate toPredicate(Root<FileDetails> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.like(cb.lower(root.get("fileName")), "%" + param.toLowerCase() + "%");
			}
		};
	}

}
