package com.demo.models;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class MailModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private Set<String> receiverList = new LinkedHashSet<String>();
	private String mailTemplate;
	public Set<String> getReceiverList() {
		return receiverList;
	}
	public void setReceiverList(Set<String> receiverList) {
		this.receiverList = receiverList;
	}
	public String getMailTemplate() {
		return mailTemplate;
	}
	public void setMailTemplate(String mailTemplate) {
		this.mailTemplate = mailTemplate;
	}
	
	
}
