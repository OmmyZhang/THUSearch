import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class ImageSearcher {
    private IndexReader reader;
    private IndexSearcher searcher;
    private Analyzer analyzer;
    Map<String, Float> avgLength;


    public ImageSearcher(String indexdir) {
        analyzer = new IKAnalyzer();
        try {

            reader = IndexReader.open(FSDirectory.open(new File(indexdir)));
            searcher = new IndexSearcher(reader);
            searcher.setSimilarity(new SimpleSimilarity());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TopDocs searchQuery(String queryString, String field, int maxnum) {
        try {
        	System.out.println("We gonna find " + queryString);
            Term term = new Term(field, queryString);
            float Temp = (float)avgLength.get(field);
            Query query = new SimpleQuery(term, Temp);
            query.setBoost(1.0f);
            //Weight w=searcher.createNormalizedWeight(query);
            //System.out.println(w.getClass());

            System.out.println(query);

            return searcher.search(query, maxnum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Document getDoc(int docID) {
        try {
            return searcher.doc(docID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadGlobals(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            avgLength = new HashMap<String, Float>();
            avgLength.put("h1", Float.valueOf(reader.readLine()));
            avgLength.put("h2", Float.valueOf(reader.readLine()));
            avgLength.put("text", Float.valueOf(reader.readLine()));
            avgLength.put("title", Float.valueOf(reader.readLine()));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String UrlRewrite(String Oldone){
    	int pos = Oldone.indexOf(".edu.cn");
    	StringBuilder Newone = new StringBuilder(Oldone);
    	Newone.insert(pos+7, "/");
    	pos = Oldone.indexOf("publish");
    	if (pos > 0){
    		Newone.insert(pos+8, "/");
    	}
    	pos = Oldone.indexOf(" hunews");
    	if (pos > 0){
    		Newone.delete(pos+2, pos+9);
    		Newone.insert(pos+2, "thunews/");
    		pos = pos + 10;
    		char Temp = Oldone.toCharArray()[pos-3];
    		if (Temp == '1')
    			pos = pos + 5;
    		else pos = pos + 4;
    		Newone.insert(pos, "/");
    		Newone.insert(pos+5, "/");
    		Newone.insert(pos+29, "/");
    	}
    	else{
    		int cnt = 0;
    		String RootPath = "D:\\Class\\18Spr\\FINAL\\.metadata\\.me_tcat85\\webapps\\ImageSearch\\mirr\\";
    		Newone = new StringBuilder(Oldone);
    		char[] Temp = new char[128];
    		Temp = Oldone.toCharArray();
    		for (int i = 0 ; i < Oldone.length() ; ++i){
    			RootPath = RootPath + Temp[i];
    			File file = new File(RootPath);
    			if (file.exists()){
    				if (i < Oldone.length() - 3){
    					if (Temp[i+1] == '0') continue;
    					Newone.insert(i+1+cnt, '/');
    					cnt++;
    					RootPath = RootPath + "\\";
    				}
    			}
    		}
    	}
    	return Newone.toString();
    }
    
    public static void main(String[] args) {
        ImageSearcher search = new ImageSearcher("forIndex/index");
        search.loadGlobals("forIndex/global_school.txt");

        TopDocs results = search.searchQuery("夏令营", "text", 100);
        System.out.println(results.totalHits);
        ScoreDoc[] hits = results.scoreDocs;
        for (int i = 0; i < hits.length; i++) { // output raw format
            Document doc = search.getDoc(hits[i].doc);
            String TrueUrl;
            TrueUrl = UrlRewrite(doc.get("url"));
            System.out.println(TrueUrl);
            System.out.println(doc.get("title"));
            //System.out.println(" score=" + hits[i].score + " picPath= " + doc.get("url") + " PR:" + doc.get("PR"));
        }
        System.out.println("ALL JOB FINISHED!\n");
    }
}
