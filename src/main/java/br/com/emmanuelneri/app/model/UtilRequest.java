package br.com.emmanuelneri.app.model;

import java.time.LocalDate;

import br.com.emmanuelneri.app.utils.Model;


public class UtilRequest {

	private String url;

	private String token;

	private Object request;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Object getRequest() {
		return request;
	}

	public void setRequest(Object request) {
		this.request = request;
	}

	@Override
	public String toString() {
		return "UtilRequest [getUrl()=" + getUrl() + ", getToken()=" + getToken() + ", getRequest()=" + getRequest()
				+ ", toString()=" + super.toString() + "]";
	}

}
