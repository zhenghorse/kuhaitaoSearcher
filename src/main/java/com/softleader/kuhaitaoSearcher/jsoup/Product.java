package com.softleader.kuhaitaoSearcher.jsoup;

public class Product {
	private String userName;
	private String description;
	private String url;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "Product [userName=" + userName + ", description=" + description
				+ ", url=" + url + "]";
	}
	
}
