package com.litb.search.eval.dto.litb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultDTO {

	private String result;

	private SearchInfoDTO info;

	public SearchResultDTO() {
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public SearchInfoDTO getInfo() {
		return info;
	}

	public void setInfo(SearchInfoDTO info) {
		this.info = info;
	}
}
