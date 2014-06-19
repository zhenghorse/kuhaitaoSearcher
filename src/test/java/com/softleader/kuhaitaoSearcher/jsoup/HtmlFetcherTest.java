package com.softleader.kuhaitaoSearcher.jsoup;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class HtmlFetcherTest {

	@Test
	public void testFetch() {
		HtmlFetcher html = new HtmlFetcher("http://www.kuhaitao.com/Discount?cid=32&p=5");
		try {
			List<Product> products = html.fetch();
			assertEquals(20, products.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("采集失败");
		}
		
	}

}
