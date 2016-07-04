package com.litb.search.eval.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
	@Qualifier("SolrEvalServer")
	private SolrServer solrServer;
	
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
	
	public void annotate(String annotator, int queryID, Set<String> ids, Set<String> relevantIDs, List<ItemDTO> items) {
		// make sure all items have been indexed
		List<String> nonExsitIDs = evalService.getNonExsitIDs(ids);
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
		
		if (relevantIDs == null || relevantIDs.isEmpty()) {
			return;
		}
		
		EvalQuery query = queryRepo.findOne(queryID);
		StringBuilder sb = new StringBuilder("Annotator: ");
		sb.append(annotator).append(", Query: ").append(query).append("(").append(queryID).append("), ids: ");
		
		Set<SolrInputDocument> docs = new HashSet<>();
		Map<String, Object> queryInc = new HashMap<>();
		queryInc.put("inc", 1);
		Map<String, Object> setNotNew = new HashMap<>();
		setNotNew.put("set", false);

		for (String id : relevantIDs) {
			sb.append(id).append(",");

			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", id);
			doc.addField(SolrQueryUtils.QUERY_RELEVANCE_PRIFIX + queryID, queryInc);
			doc.addField("is_new", setNotNew);
			docs.add(doc);
			
			annotateItem(query.getId(), id);
		}
		sb.append("; total: ").append(relevantIDs.size());
		
		try {
			solrServer.add(docs);
			solrServer.commit();
		} catch (SolrServerException | IOException e) {
			LOGGER.error("Failed to increase annotation!", e);
			return;
		}
		ANNOTATE_LOGGER.info(sb.toString());
	}
	
	public void annotateItem(int queryId, String itemId) {
		EvalItemAnnotation annotation = annotationRepo.findByQueryIdAndItemId(queryId, itemId);
		if(annotation != null) {
			annotation.incrementAnnotatedTimes();
		} else {
			annotation = new EvalItemAnnotation();
			annotation.setAnnotatedTimes(1);
			annotation.setItem(itemRepo.findOne(itemId));
			annotation.setQuery(queryRepo.findOne(queryId));
		}
		annotationRepo.save(annotation);
	}
}