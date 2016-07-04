package com.litb.search.eval.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.litb.search.eval.entity.EvalItem;

public interface ItemRepository extends CrudRepository<EvalItem, String>{

	public List<String> findIdById(Collection<String> ids);
}
