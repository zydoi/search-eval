package com.litb.search.eval.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.litb.search.eval.dto.litb.ItemDTO;
import com.litb.search.eval.dto.litb.ItemsResultDTO;
import com.litb.search.eval.dto.solr.SolrItemDTO;
import com.litb.search.eval.entity.EvalItem;
import com.litb.search.eval.entity.EvalItemAnnotation;
import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.repository.AnnotationRepository;
import com.litb.search.eval.repository.ItemRepository;
import com.litb.search.eval.repository.QueryRepository;

@Service
public class ItemService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);
	
	@Autowired
	private SolrEvalService solrService;
	
	@Autowired
	private ItemRepository itemRepo;
	
	@Autowired
	private QueryRepository queryRepo;
	
	@Autowired
	private AnnotationRepository annotationRepo;
	
	@Autowired
	private LitbSearchService litbService;
	
	@Transactional
	public Set<EvalItem> syncDBAndSolr() {
		List<SolrItemDTO> itemDTOs = solrService.getAllItems();
		Set<EvalItem> items = new HashSet<>();
		for (SolrItemDTO dto : itemDTOs) {
			EvalItem item = new EvalItem(dto.getId(), dto.getName());
			item.setLastCategory(dto.getLastCategory());
			item.setPrice(item.getPrice());
			items.add(item);
			itemRepo.save(item);
			if (dto.getQueries() != null) {
				for(Entry<String, Integer> entry: dto.getQueries().entrySet()) {
					EvalItemAnnotation annotation = new EvalItemAnnotation();
					annotation.setAnnotatedTimes(entry.getValue());
					annotation.setItem(item);
					int queryId = Integer.valueOf(entry.getKey().substring(entry.getKey().indexOf("_") + 1));
					annotation.setQuery(queryRepo.findOne(queryId));
					annotationRepo.save(annotation);
				}
			}
		}
		return items;
	}
	
	@Transactional
	public void addItemDetails() {
		List<SolrItemDTO> itemDTOs = solrService.getAllItems();
		List<String> pids = new ArrayList<>();
		for (SolrItemDTO dto : itemDTOs) {
			pids.add(dto.getId());
		}
		Iterable<EvalQuery> queries = queryRepo.findAll();
		for (EvalQuery query : queries) {
			LOGGER.info("Start to enrich items for Query {}.", query);
			
			ItemsResultDTO result = litbService.getItems(query.getName(), 200, false);
			List<ItemDTO> items = result.getInfo().getItems();
			for (ItemDTO dto : items) {
				EvalItem item = itemRepo.findOne(dto.getItemId());
				if (item != null) {
					LOGGER.info("Enriching item: {} {}.", item.getId(), item.getName());
					item.setItemURL(dto.getItemURL());
					item.setLastCategory(dto.getMasterCategoryName());
					item.setFavNum(dto.getFavoriteTimes());
					item.setImageURL(dto.getCateShowImgs().getGrid().getRectangle());
					item.setPrice(dto.getSalePrice());
					itemRepo.save(item);
				}
			}
		}
	}
}
