package com.kcb.http.request;

public class Disbursement {
	
	private Header header;
	
	private RequestPayload requestPayload;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public RequestPayload getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(RequestPayload requestPayload) {
		this.requestPayload = requestPayload;
	}

}
