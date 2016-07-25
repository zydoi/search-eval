package com.litb.search.eval.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.litb.search.eval.entity.EvalItemAnnotation;

public interface AnnotationRepository extends CrudRepository<EvalItemAnnotation, Long>{

	EvalItemAnnotation findByQueryIdAndItemId(int queryId, String itemId);

	Set<EvalItemAnnotation> findByQueryIdAndItemIdIn(int queryId, Collection<String> itemIds);
	
	List<EvalItemAnnotation> findByQueryId(int queryId);
	
	@Query("select a.id from EvalItemAnnotation a where a.query.id=?1")
	Set<String> findAnnotatedItemIds(int queryId);
	
	@Query("select a.item.id from EvalItemAnnotation a where a.query.id=?1 and a.annotatedTimes > 0")
	Set<String> findRelevantItemIds(int queryId);

	List<EvalItemAnnotation> deleteByQueryId(int queryId);
	
}
