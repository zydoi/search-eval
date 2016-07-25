package com.litb.search.eval.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.litb.search.eval.entity.EvalItem;
import com.litb.search.eval.entity.EvalItemAnnotation;
import com.litb.search.eval.repository.AnnotationRepository;
import com.litb.search.eval.repository.ItemRepository;
import com.litb.search.eval.repository.QueryRepository;

@Service
public class TestDataGenerator {

	@Autowired
	private AnnotationRepository annoRepo;
	
	@Autowired
	private ItemRepository itemRepo;
	
	@Autowired
	private QueryRepository queryRepo;
	
	
	public void generateItemsWithAnnotations() {
		EvalItem item = new EvalItem("123", "test");
		EvalItem item2 = new EvalItem("222", "test");
		itemRepo.save(item);
		itemRepo.save(item2);
		EvalItemAnnotation a = new EvalItemAnnotation(queryRepo.findOne(1), item);
		EvalItemAnnotation a2 = new EvalItemAnnotation(queryRepo.findOne(1), item2);
		a.setAnnotatedTimes(1);
		annoRepo.save(a);
		annoRepo.save(a2);
	}
	
	public void clearAll() {
		itemRepo.deleteAll();
	}
}
