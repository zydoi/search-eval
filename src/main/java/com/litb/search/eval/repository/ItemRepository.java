package com.litb.search.eval.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.litb.search.eval.entity.EvalItem;

public interface ItemRepository extends CrudRepository<EvalItem, String>{

	List<EvalItem> findByItemURLIsNull();
	
	@Query("select i.id from item i where i.id in ?1")
	Set<String> findExistsIds(Collection<String> ids);
	
}
