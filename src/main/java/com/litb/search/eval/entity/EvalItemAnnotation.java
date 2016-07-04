package com.litb.search.eval.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"query_id", "item_id"})})
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
	
	public void incrementAnnotatedTimes() {
		this.annotatedTimes += 1;
	}

	public boolean isRelevant() {
		return relevant;
	}

	public void setRelevant(boolean relevant) {
		this.relevant = relevant;
	}
}
