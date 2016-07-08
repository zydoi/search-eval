package com.litb.search.eval.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.litb.search.eval.dto.EvalResultDTO;
import com.litb.search.eval.dto.QueryEvalResultDTO;
import com.litb.search.eval.entity.EvalItemAnnotation;
import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.repository.AnnotationRepository;
import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.repository.QueryType;

@Service
public class EvaluationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationService.class); 
	
	@Autowired
	private LitbSearchService litbService;

	@Value("${search.size}")
	private int maxSize;
	
	@Value("${quary.eval}")
	private String queryIDs;
	
	@Value("${quary.top.eval}")
	private String topQueryIDs;

	@Value("${quary.bad.eval}")
	private String badQueryIDs;
	
	@Autowired
	private QueryRepository queryRepo;
	
	@Autowired
	private AnnotationRepository annotationRepo;
	
	private List<Integer> nums = Arrays.asList(10, 20, 48);
	
	public EvalResultDTO generateEvaluationResult(QueryType type) {
		EvalResultDTO result = new EvalResultDTO();
		double map = 0;
		List<String> qids = getEvalQueryIDs(type);
		for (String qid : qids) {
			QueryEvalResultDTO queryResult = new QueryEvalResultDTO(qid);
			double ap = 0; // average precision
			double r = 0; // relevant items
			EvalQuery query = queryRepo.findOne(Integer.valueOf(qid));
			queryResult.setQueryName(query.getName());
			
			List<String> ids = litbService.search(query.getName(), maxSize, true).getInfo().getItems();
			int size =  Math.min(maxSize, ids.size());
			int n;
			for (int i = 0; i < size; i++) {
				EvalItemAnnotation annotation = annotationRepo.findByQueryIdAndItemId(Integer.valueOf(qid), ids.get(i));
				n = i + 1;
				if (isRelevant(annotation)) {
					r++;
					ap += r/n;
				}
				if(nums.contains(n)) {
					double p = r/n;
					queryResult.addPrecision(n, p);
				}
			}

			ap = (r == 0) ? 0 : ap / r;
			queryResult.setAp(ap);
			result.addQueryResult(queryResult);
			LOGGER.info("Average Precision for the query" + query + "' is: " + ap);
			map += ap;
		}
		map = map / qids.size();
		result.setMap(map);
		
		LOGGER.info("Search Engine Mean Average Precision (MAP): " + map);
		return result;
	}
	
	public double pn(int queryId, int n) {
		EvalQuery query = queryRepo.findOne(queryId);
		List<String> ids = litbService.search(query.getName(), maxSize, true).getInfo().getItems();
		double r = 0;
		for (String id : ids) {
			EvalItemAnnotation annotation = annotationRepo.findByQueryIdAndItemId(queryId, id);
			if (isRelevant(annotation)) {
				r++;
			}
		}
		
		int total = Math.min(n, ids.size());
		return r / total;
	}
	
	public List<String> getEvalQueryIDs(QueryType type) {
		String[] qids;
		switch (type) {
		case TOP:
			qids = topQueryIDs.split(",");
			break;
		case BAD:
			qids = badQueryIDs.split(",");
			break;
		default:
			qids = queryIDs.split(",");
			break;
		} 
		return Arrays.asList(qids);
	}
	
	public boolean isRelevant(EvalItemAnnotation annotation) {
		if (annotation != null && annotation.getAnnotatedTimes() > 0) {
			return true;
		}
		return false;
	}
}
