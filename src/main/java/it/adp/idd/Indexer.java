package it.adp.idd;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.suggest.analyzing.FSTUtil;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indexer {
    public void indexDocs(String indexPath, Codec codec, String docsPath) throws Exception {
        Analyzer defaultAnalyzer = new StandardAnalyzer();
        Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();
        perFieldAnalyzers.put("titolo", new SimpleAnalyzer());
        perFieldAnalyzers.put("contenuto", new ItalianAnalyzer());

        Analyzer analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzers);

        Directory indexDirectory = FSDirectory.open(Paths.get(indexPath)); // Crea un oggetto Directory da un oggetto Path

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        if (codec != null) {
            config.setCodec(codec);
        }
        IndexWriter writer = new IndexWriter(indexDirectory, config);
        writer.deleteAll();
        try{
            List<Document> docs= new FileReader().getDocuments(docsPath);
            for( Document doc :docs){
                writer.addDocument(doc);
            }
        }
        catch (Exception e){
            throw e;
        }



        writer.commit();
        writer.close();

        //Stampa statistiche
        try (IndexReader reader = DirectoryReader.open(indexDirectory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            Collection<String> indexedFields = FieldInfos.getIndexedFields(reader);
            for (String field : indexedFields) {
                System.out.println(searcher.collectionStatistics(field));
            }
        } finally {
            indexDirectory.close();
        }
    }
}
