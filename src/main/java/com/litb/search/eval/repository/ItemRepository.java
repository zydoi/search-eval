package com.litb.search.eval.repository;

import org.springframework.data.repository.CrudRepository;

import com.litb.search.eval.entity.EvalItem;

public interface ItemRepository extends CrudRepository<EvalItem, String>{

}
