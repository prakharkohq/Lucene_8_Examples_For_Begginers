package com.lucene.Basic;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class IndexWriterAndReader {
    public IndexWriter writer;
    public Analyzer analyzer;
    public IndexReader reader;
    public Directory dir;


    public static void getWriterDetails(IndexWriter writer, IndexReader idr) {
        System.out.println("Writer details are ");
        IndexWriter.DocStats docStats = writer.getDocStats();
        System.out.println("Maximum number of Doc's " + docStats.maxDoc);
        System.out.println(" Number of Doc's " + docStats.numDocs);
        /** Moves all in-memory segments to the {sDirectory}, but does not commit
         *  (fsync) them (call  #commit} for that). */
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Writer details After Flush are ");
        System.out.println("Maximum number of Doc's " + docStats.maxDoc);
        System.out.println(" Number of Doc's " + docStats.numDocs);
        // Check number of Doc's from Index Reader should be same
        System.out.println("\n \n Max Number of Doc's in Index Reader " + idr.maxDoc());
        System.out.println("Number of Doc's in Index Reader " + idr.numDocs());

    }

   /* public void writeWithMergePolicy(IndexWriter writer, IndexReader idr,Directory dir) {
        writer=new IndexWriter(dir,new IndexWriterConfig(
                new
        ))
    }*/

    public static void main(String[] args) throws IOException {
        IndexWriterAndReader O = new IndexWriterAndReader();
        O.analyzer = new StandardAnalyzer();
        O.dir = new RAMDirectory();
        IndexWriterConfig iwc = new IndexWriterConfig(O.analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        O.writer = new IndexWriter(O.dir, iwc);
        for (int i = 0; i < 100; i++) {
            Document doc = new Document();
            doc.add(new TextField("content", RandomStringUtils.randomAlphabetic(15), Field.Store.YES));
            O.writer.addDocument(doc);
        }
        System.out.println("Indexed 100 records successfully ");
        O.writer.commit();
        O.reader = DirectoryReader.open(O.dir);
        getWriterDetails(O.writer, O.reader);
        O.writer.close();
    }

}
