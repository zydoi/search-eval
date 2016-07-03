package com.litb.search.eval.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.litb.search.eval.entity.EvalQuery;

public interface QueryRepository extends CrudRepository<EvalQuery, Integer> {

	List<EvalQuery> findByQueryType(QueryType queryType);
	
	List<EvalQuery> findByEffectiveTrue();
}
