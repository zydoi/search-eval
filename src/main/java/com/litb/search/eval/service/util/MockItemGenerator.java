package com.litb.search.eval.service.util;

import java.util.ArrayList;
import java.util.List;

import com.litb.search.eval.dto.litb.ItemDTO;
import com.litb.search.eval.dto.litb.ItemsResultDTO;
import com.litb.search.eval.dto.litb.LitbInfoDTO;

public class MockItemGenerator {
	
	public static ItemsResultDTO generateItems() {
		ItemsResultDTO items = new ItemsResultDTO();
		items.setResult("success");
		LitbInfoDTO infoDTO = new LitbInfoDTO();
		List<ItemDTO> is = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			ItemDTO item = new ItemDTO();
			item.setCurrency("USD");
			item.setFavoriteTimes(i);
			item.setItemName("test item");
			item.setSalePrice(i);
			item.setReviewCount(100/(i+1));
			item.setItemId(String.valueOf(i));
			item.setMainImgURL("http://litbimg8.rightinthebox.com/images/182x240/201508/russtt1439971810458.jpg");
			is.add(item);
		}
		infoDTO.setItems(is);
		items.setInfo(infoDTO);
		
		return items;
	}
}
