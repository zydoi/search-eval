package com.litb.search.eval.service.util;

import com.litb.search.eval.dto.QueryDTO;
import com.litb.search.eval.dto.litb.ItemDTO;
import com.litb.search.eval.entity.EvalItem;
import com.litb.search.eval.entity.EvalQuery;

public final class DtoConverter {

	public static EvalItem convertItemDTO(ItemDTO dto) {
		EvalItem item = new EvalItem(dto.getItemId(), dto.getItemName());
		item.setFavNum(dto.getFavoriteTimes());
		item.setImageURL(dto.getShowImgURL());
		item.setItemURL(dto.getItemURL());
		item.setPrice(dto.getSalePrice());
		item.setLastCategory(dto.getMasterCategoryName());
		return item;
	}
	
	public static EvalQuery convertQueryDTO(QueryDTO dto) {
		EvalQuery query = new EvalQuery(dto.getId(), dto.getName(), null);
		query.setQueryTypes(dto.getTypes());
		query.setEffective(dto.isEffective());
		return query;
	}
}
