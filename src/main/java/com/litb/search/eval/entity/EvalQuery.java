package com.litb.search.eval.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;

import com.litb.search.eval.repository.QueryType;

@Entity(name = "Query")
public class EvalQuery {
	
	@Id
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@ElementCollection(targetClass=QueryType.class, fetch=FetchType.EAGER)
	@CollectionTable(name = "query_type")
	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private Set<QueryType> queryTypes = new HashSet<>();

	@Column(nullable = false)
	private boolean effective;
	
	@Column(nullable = false)
	private int annotatedTimes; 
	
	@Column(nullable = false)
	private Date lastModified = new Date();
	
	@OneToMany(mappedBy = "query", cascade = CascadeType.ALL)
	private Set<EvalItemAnnotation> itemAnnotation;
	
	public EvalQuery() {
	}
	
	public EvalQuery(int id, String name, QueryType queryType) {
		super();
		this.id = id;
		this.name = name;
		this.queryTypes.add(queryType);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEffective() {
		return effective;
	}

	public void setEffective(boolean effective) {
		this.effective = effective;
	}

	public int getAnnotatedTimes() {
		return annotatedTimes;
	}

	public void setAnnotatedTimes(int annotatedTimes) {
		this.annotatedTimes = annotatedTimes;
	}

	public Date getLastModified() {
		return lastModified;
	}

	@PreUpdate
	public void setLastModified() {
		this.lastModified = new Date();
	}

	public Set<EvalItemAnnotation> getItemAnnotation() {
		return itemAnnotation;
	}

	public void setItemAnnotation(Set<EvalItemAnnotation> itemAnnotation) {
		this.itemAnnotation = itemAnnotation;
	}
	
	public Set<QueryType> getQueryTypes() {
		return queryTypes;
	}

	public void setQueryTypes(Set<QueryType> queryTypes) {
		this.queryTypes = queryTypes;
	}
	
	public void addQueryType(QueryType queryType) {
		this.queryTypes.add(queryType);
	}

	@Override
	public String toString() {
		return "(" + id + ") " + name;
	}
}
