package com.softleader.kuhaitaoSearcher.lucene;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class HtmlSearcherTest {

	@Test
	public void testSearch() {
		String url = "http://www.kuhaitao.com/Discount?cid=32";
		HtmlSearcher htmlSearcher = new HtmlSearcher(url);
		String field = "description";
		String content = "女士";
		try {
			htmlSearcher.indexHtml();
			List<String> contents = htmlSearcher.search(field, content);
			htmlSearcher.close();
			assertEquals(true, !contents.isEmpty());
			System.out.println("总共找到个数:" + contents.size());
			for (String description : contents) {
				System.out.println(description);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("搜索失败！");
		}
		
	}

}
