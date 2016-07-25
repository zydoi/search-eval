package com.litb.search.eval.service.util;

import com.litb.search.eval.dto.litb.ItemDTO;
import com.litb.search.eval.entity.EvalItem;

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
}
