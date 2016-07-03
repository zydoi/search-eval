package com.litb.search.eval.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.litb.search.eval.dto.solr.SolrItemDTO;
import com.litb.search.eval.entity.EvalItem;
import com.litb.search.eval.entity.EvalItemAnnotation;
import com.litb.search.eval.repository.AnnotationRepository;
import com.litb.search.eval.repository.ItemRepository;
import com.litb.search.eval.repository.QueryRepository;

@Service
public class ItemService {
	
	@Autowired
	private SolrEvalService solrService;
	
	@Autowired
	private ItemRepository itemRepo;
	
	@Autowired
	private QueryRepository queryRepo;
	
	@Autowired
	private AnnotationRepository annotationRepo;
	
	@Transactional
	public Set<EvalItem> syncDBAndSolr() {
		List<SolrItemDTO> itemDTOs = solrService.getAllItems();
		Set<EvalItem> items = new HashSet<>();
		for (SolrItemDTO dto : itemDTOs) {
			EvalItem item = new EvalItem(dto.getId(), dto.getName());
			items.add(item);
			if (dto.getQueries() != null) {
				for(Entry<String, Integer> entry: dto.getQueries().entrySet()) {
					EvalItemAnnotation annotation = new EvalItemAnnotation();
					annotation.setAnnotatedTimes(entry.getValue());
					annotation.setQuery(queryRepo.findOne(Integer.valueOf(entry.getKey())));
					annotation.setItem(item);
					annotationRepo.save(annotation);
				}
			}
		}
		itemRepo.save(items);
		return items;
	}
}
