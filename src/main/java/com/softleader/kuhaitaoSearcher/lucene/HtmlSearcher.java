package com.softleader.kuhaitaoSearcher.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.softleader.kuhaitaoSearcher.jsoup.HtmlFetcher;
import com.softleader.kuhaitaoSearcher.jsoup.Product;

public class HtmlSearcher {

	private IndexSearcher searcher;
	private Directory directory;
	private DirectoryReader reader;

	private void index() throws Exception {
		IndexWriter writer = null;
		directory = new RAMDirectory(); // 索引文件在内存
		Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_46);
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_46,
				analyzer);
		writer = new IndexWriter(directory, iwConfig);
		HtmlFetcher html = new HtmlFetcher(
				"http://www.kuhaitao.com/Discount?cid=32&p=5");
		List<Product> products = html.fetch();
		for (Product product : products) {
			Document doc = new Document();
			doc.add(new TextField("description", product.getDescription(),
					Store.YES));
			doc.add(new StringField("username", product.getUserName(),
					Store.YES));
			doc.add(new StringField("url", product.getUrl(), Store.YES));
			// FieldType fieldType = new FieldType();
			// fieldType.setIndexed(true);// set 是否索引
			// fieldType.setStored(true);// set 是否存储
			// fieldType.setTokenized(false);// set 是否分词*/
			writer.addDocument(doc);
		}
		writer.commit();
		writer.close();
	}

	public void indexHtml() throws Exception {
		index();
		reader = DirectoryReader.open(directory);
		searcher = new IndexSearcher(reader);
	}

	public void search(String field, String content) {
		try {
			Query query = new TermQuery(new Term(field, content));
			TopDocs topDocs = searcher.search(query, 10);
			ScoreDoc[] scores = topDocs.scoreDocs;
			System.out.println("找到个数：" + scores.length);
			for (int i = 0; i < scores.length; i++) {
				Document doc = searcher.doc(scores[i].doc);
				System.out.println("description:" + doc.get("description"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void close() {
		if (directory != null) {
			try {
				directory.close();
				directory = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (reader != null) {
			try {
				reader.close();
				reader = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		HtmlSearcher html = new HtmlSearcher();
		String field = "description";
		String content = "女士";
		try {
			html.indexHtml();
			html.search(field, content);
			html.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
