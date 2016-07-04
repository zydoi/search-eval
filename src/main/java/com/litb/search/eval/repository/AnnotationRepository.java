package com.litb.search.eval.repository;

import org.springframework.data.repository.CrudRepository;

import com.litb.search.eval.entity.EvalItemAnnotation;

public interface AnnotationRepository extends CrudRepository<EvalItemAnnotation, Long>{

	public EvalItemAnnotation findByQueryIdAndItemId(int queryId, String itemId);
}
