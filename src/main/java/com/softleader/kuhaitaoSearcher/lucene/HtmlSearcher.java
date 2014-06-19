package com.softleader.kuhaitaoSearcher.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	
	HtmlFetcher htmlFetcher;
	public HtmlSearcher(String url) {
		htmlFetcher = new HtmlFetcher(url);
	}

	private void index() throws Exception {
		IndexWriter writer = null;
		directory = new RAMDirectory(); // 索引文件在内存
		Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_46);
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_46,
				analyzer);
		writer = new IndexWriter(directory, iwConfig);
		List<Product> products = htmlFetcher.fetch();
		for (Product product : products) {
			Document doc = new Document();
			doc.add(new TextField("description", product.getDescription(),
					Store.YES));
			doc.add(new StringField("username", product.getUserName(),
					Store.YES));
			doc.add(new StringField("url", product.getUrl(), Store.YES));
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

	public List<String> search(String field, String content) {
		try {
			Query query = new TermQuery(new Term(field, content));
			TopDocs topDocs = searcher.search(query, 10);
			ScoreDoc[] scores = topDocs.scoreDocs;
			List<String> contents = new ArrayList<String>(scores.length);
			for (int i = 0; i < scores.length; i++) {
				Document doc = searcher.doc(scores[i].doc);
				String description = doc.get("description");
				contents.add(description);
			}
			return contents;
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
	}

	public void close() {
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

}
