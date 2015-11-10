package com.radojko.linkGrabber;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TermsLinks {
	@Id
	@GeneratedValue
	private int id;
	private String term;
	private String link;
	
	public TermsLinks(String term, String link) {
		super();
		this.term = term;
		this.link = link;
	}
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
