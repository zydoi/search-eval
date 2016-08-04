package com.litb.search.eval.service;

import java.util.ArrayList;
import java.util.Collection;
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
import com.litb.search.eval.service.util.DtoConverter;

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
	public List<EvalItem> addNewItems(List<ItemDTO> itemDTOs) {
		List<EvalItem> items = new ArrayList<>();
		for (ItemDTO dto : itemDTOs) {
			EvalItem item = DtoConverter.convertItemDTO(dto);
			itemRepo.save(item);
			items.add(item);
		}
		return items;
	}
	
	@Transactional
	public EvalItem addNewItem(ItemDTO dto) {
		EvalItem item = DtoConverter.convertItemDTO(dto);
		itemRepo.save(item);
		return item;
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
	
	public Set<String> getNonExistIds(Collection<String> ids) {
		Set<String> nonExistIds = new HashSet<>(ids);
		nonExistIds.removeAll(itemRepo.findExistsIds(ids));
		
		return nonExistIds;
	}
	
	public void clearSolrAndDB() {
		List<EvalItem> items = itemRepo.findByItemURLIsNull();
		List<String> ids = new ArrayList<>();
		for (EvalItem item : items) {
			ids.add(item.getId());
		}
		
		solrService.deleteItems(ids);
		for (String id : ids) {
			itemRepo.delete(id);
		}
	}
	
	public List<String> getNotAnnotatedItemIds(int queryId, List<String> ids) {
		List<String> notAnnotatedIds = new ArrayList<>(ids);
		List<EvalItemAnnotation> annotations = annotationRepo.findByQueryId(queryId);
		for (EvalItemAnnotation annotation : annotations) {
			notAnnotatedIds.remove(annotation.getItem().getId());
		}
		return notAnnotatedIds;
	}
	
	@Transactional
	public void clearAnnotation(int queryId) {
		List<EvalItemAnnotation> annotations = annotationRepo.findByQueryId(queryId);
		annotationRepo.delete(annotations);
	}
	
	public Set<String> getAllExistIds() {
		return itemRepo.findAllIds();
	}
}
