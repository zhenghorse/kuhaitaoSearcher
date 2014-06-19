package com.softleader.kuhaitaoSearcher.jsoup;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlFetcher {
	
	private int timeoutMillis  = 1 * 60 * 1000;
	private String url;
	private Document document;
	
	public HtmlFetcher(String url) {
		this.url = url;
	}
	
	public List<Product> fetch() throws Exception {
		//Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0").get();
		document = Jsoup.parse(new URL(url), timeoutMillis);
		Elements items = document.select("div.status-item");
		List<Product> products = new ArrayList<Product>(items.size());
		for (Element item : items) {
			Product product = new Product();
			product.setUserName(item.select("a.avatar").attr("href"));
			Element span = item.select("div.body > span").first();
			product.setDescription(span.text());
			product.setUrl(span.select("a").attr("href"));
			products.add(product);
		}
		return products;
	}

}
