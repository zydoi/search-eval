package com.litb.search.eval.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Value("${quary.mul.eval}")
	private String mulQueryIDs;
	
	@Autowired
	private QueryRepository queryRepo;
	
	@Autowired
	private AnnotationRepository annotationRepo;
	
	private Map<QueryType, EvalResultDTO> evalResuts = new EnumMap<>(QueryType.class);
	
	private List<Integer> nums = Arrays.asList(10, 20, 48);
	
	public EvalResultDTO generateEvaluationResult(QueryType type) {
		if (evalResuts.containsKey(type)) {
			return evalResuts.get(type);
		}
		
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
		
		calculateAveragePn(result);
		
		LOGGER.info("Search Engine Mean Average Precision (MAP): " + map);
		evalResuts.put(type, result);
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
		case MUL:
			qids = mulQueryIDs.split(",");
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
	
	public double getAverageValue(Collection<Double> values) {
		double result = 0;
		for (Double value : values) {
			result += value;
		}
		return result / values.size();
	}
	
	private void calculateAveragePn(EvalResultDTO result) {
		Map<Integer, Double> aPn = new HashMap<>();
		for (int num : nums) {
			aPn.put(num, 0.0);
			int count = 0;
			for ( QueryEvalResultDTO queryEval : result.getQueryEvalResults().values()) {
				if (queryEval.getPrecisions().containsKey(num)) {
					double value = aPn.get(num) + queryEval.getPrecisions().get(num);
					aPn.put(num, value);
					count++;
				}
			}
			result.getAveragePn().put(num, aPn.get(num) / count);
			LOGGER.info("Search Engine Average P@{}: {}", num, result.getAveragePn().get(num));
		}
	}
	
	public void invalidateResults() {
		evalResuts.clear();
	}
}
