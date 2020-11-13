package com.kcb.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the callback_url database table.
 * 
 */
@Entity
@Table(name="callback_url")
@NamedQuery(name="CallbackUrl.findAll", query="SELECT c FROM CallbackUrl c")
public class CallbackUrl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Lob
	@Column(name="callback_url")
	private String callbackUrl;

	public CallbackUrl() {
	}

	public CallbackUrl(String callbackUrl) {
		super();
		this.callbackUrl = callbackUrl;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCallbackUrl() {
		return this.callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

}