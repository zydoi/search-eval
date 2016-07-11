package com.litb.search.eval.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.litb.search.eval.dto.litb.ItemDTO;
import com.litb.search.eval.dto.solr.SolrItemDTO;
import com.litb.search.eval.entity.EvalItem;
import com.litb.search.eval.entity.EvalItemAnnotation;
import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.repository.AnnotationRepository;
import com.litb.search.eval.repository.ItemRepository;
import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.service.util.DtoConverter;
import com.litb.search.eval.service.util.SolrQueryUtils;

@Service
public class AnnotateService {

	private static final Logger ANNOTATE_LOGGER = LoggerFactory.getLogger("annotate");

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnotateService.class);

	@Autowired
	private QueryRepository queryRepo;

	@Autowired
	private ItemRepository itemRepo;

	@Autowired
	private AnnotationRepository annotationRepo;

	@Autowired
	private SolrEvalService evalService;

	@Autowired
	private SolrProdService prodService;

	@Autowired
	private ItemService itemService;

	public void annotate(String annotator, int queryID, Set<String> ids, Set<String> relevantIDs,
			Set<String> irrelevantIDs, List<ItemDTO> items) {
		// make sure all items have been added to the DB.
		Set<String> nonExsitIDs = itemService.getNonExistIds(ids);
		if (!nonExsitIDs.isEmpty()) {
			List<SolrItemDTO> dtos = prodService.getItems(nonExsitIDs);
			evalService.addItems(dtos);

			for (ItemDTO dto : items) {
				if (nonExsitIDs.contains(dto.getItemId())) {
					EvalItem item = DtoConverter.convertItemDTO(dto);
					itemRepo.save(item);
				}
			}

			LOGGER.info("Indexed new items: " + SolrQueryUtils.concatIDs(nonExsitIDs));
		}

		// init annotations in the DB
		annotateItems(queryID, ids, false);
		EvalQuery query = queryRepo.findOne(queryID);
		
		// add annotations for the relevant items
		if (relevantIDs != null && !relevantIDs.isEmpty()) {
			annotateItems(queryID, relevantIDs, true);
			evalService.annotateItems(queryID, relevantIDs);
			StringBuilder sb = new StringBuilder("Annotator: ");
			sb.append(annotator).append(" add annotations of Query: ").append(query).append(", ids: ").append(SolrQueryUtils.concatIDs(relevantIDs));
			ANNOTATE_LOGGER.info(sb.toString());
		}
		
		// remove annotations for irrelevant items
		if (irrelevantIDs != null && !irrelevantIDs.isEmpty()) {
			unannotateItems(queryID, irrelevantIDs);
			evalService.unannotateItems(queryID, irrelevantIDs);
			StringBuilder sb = new StringBuilder("Annotator: ");
			sb.append(annotator).append(" remove annotations of Query: ").append(query).append(", ids: ").append(SolrQueryUtils.concatIDs(irrelevantIDs));
			ANNOTATE_LOGGER.info(sb.toString());
		}
	}

	public EvalItemAnnotation annotateItem(int queryId, String itemId, boolean relevant) {
		EvalItemAnnotation annotation = annotationRepo.findByQueryIdAndItemId(queryId, itemId);
		if (annotation != null) {
			if (relevant) {
				annotation.incrementAnnotatedTimes();
			}
		} else {
			annotation = new EvalItemAnnotation();
			annotation.setItem(itemRepo.findOne(itemId));
			annotation.setQuery(queryRepo.findOne(queryId));
			if (relevant) {
				annotation.setAnnotatedTimes(1);
			}
		}
		return annotationRepo.save(annotation);
	}

	public void annotateItems(int queryId, Collection<String> itemIds, boolean relevant) {
		EvalQuery query = queryRepo.findOne(queryId);
		for (String itemId : itemIds) {
			EvalItemAnnotation annotation = annotationRepo.findByQueryIdAndItemId(queryId, itemId);
			if (annotation != null) {
				if (relevant) {
					annotation.incrementAnnotatedTimes();
				}
			} else {
				annotation = new EvalItemAnnotation();
				annotation.setItem(itemRepo.findOne(itemId));
				annotation.setQuery(query);
				if (relevant) {
					annotation.setAnnotatedTimes(1);
				}
			}
			annotationRepo.save(annotation);
		}
	}
	
	@Transactional
	public void unannotateItems(int queryId, Collection<String> itemIds) {
		Set<EvalItemAnnotation> annotations = annotationRepo.findByQueryIdAndItemIdIn(queryId, itemIds);
		for (EvalItemAnnotation annotation : annotations) {
			annotation.resetAnnotationTimes();
			annotationRepo.save(annotation);
		}
	}
}