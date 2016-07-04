package com.litb.search.eval.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EvalItemAnnotation {
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "query_id")
	private EvalQuery query;
	
	@ManyToOne
	@JoinColumn(name = "item_id")
	private EvalItem item;
	
	@Column
	private int annotatedTimes;
	
	@Column
	private boolean relevant;
	
	public EvalQuery getQuery() {
		return query;
	}

	public void setQuery(EvalQuery query) {
		this.query = query;
	}

	public EvalItem getItem() {
		return item;
	}

	public void setItem(EvalItem item) {
		this.item = item;
	}

	public int getAnnotatedTimes() {
		return annotatedTimes;
	}

	public void setAnnotatedTimes(int annotatedTimes) {
		this.annotatedTimes = annotatedTimes;
	}
}
