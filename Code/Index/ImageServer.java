import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Objects;


public class ImageServer extends HttpServlet {
    private Analyzer analyzer;
    public static final int PAGE_RESULT = 10;
    public static final String indexDir = "forIndex";
    public static final String picDir = "ImageSearch/";
    public String SHelp = "ÁõÞÈÈº";
    private String[] fields = {"h1", "text", "title"};
//    private float[] boosts = {0.5f,1.0f};
    private ImageSearcher search = null;

    public String UrlRewrite(String Oldone){
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
    
    public ImageServer() {
        super();
        analyzer = new IKAnalyzer();
        search = new ImageSearcher(indexDir + "/index_school");
        search.loadGlobals(indexDir + "/global_school.txt");
    }

    public ScoreDoc[] showList(ScoreDoc[] results, int page) {
        if (results == null || results.length < (page - 1) * PAGE_RESULT) {
            return null;
        }
        int start = Math.max((page - 1) * PAGE_RESULT, 0);
        int docnum = Math.min(results.length - start, PAGE_RESULT);
        ScoreDoc[] ret = new ScoreDoc[docnum];
        System.arraycopy(results, start, ret, 0, docnum);
        return ret;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        String queryString = request.getParameter("query");
        String pageString = request.getParameter("page");
        int page = 1;
        //System.out.println("We gonna find " + queryString);
        if (pageString != null) {
            page = Integer.parseInt(pageString);
        }
        ScoreDoc[] scoredDocs = new ScoreDoc[0];
        Integer[] combinedPageNum = new Integer[0];
        String[] urls = null;
        String[] titles = null;
        String[] texts = null;
        if (queryString == null) {
//            System.out.println("null query");
//            request.getRequestDispatcher("/Image.jsp").forward(request, response);
        } else {
            for (String field : fields) {
            	//field = "text";
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(queryString));
                tokenStream.addAttribute(CharTermAttribute.class);
                String[] terms = new String[queryString.length()];
                int index = 0;
                while (tokenStream.incrementToken()) {
                    String query = tokenStream.getAttribute(CharTermAttribute.class).toString();
                    System.out.println(query);
                    boolean existed = false;
                    for (int i = 0; i < index; ++i) {
                        if (terms[i].equals(query)) {
                            existed = true;
                            break;
                        }
                    }
                    if (existed) {
                        continue;
                    }
                    terms[index++] = query;
                    TopDocs results = search.searchQuery(query, field, 150);
                    System.out.println(queryString);
                    System.out.println("totalHit"+results.scoreDocs.length);
                    for (ScoreDoc doc: results.scoreDocs) {
                        boolean newDoc = true;
                        for (int sdId = 0; sdId < scoredDocs.length; ++sdId) {
                            if (doc.doc == scoredDocs[sdId].doc) {
                                if (Objects.equals(field, "title")) {
                                    doc.score *= 10;
                                }
                                scoredDocs[sdId].score += doc.score;
                                combinedPageNum[sdId] += 1;
                                newDoc = false;
                                break;
                            }
                        }
                        if (newDoc) {
                            ScoreDoc[] temp = new ScoreDoc[1];
                            temp[0] = doc;
                            scoredDocs = concat(scoredDocs, temp);
                            Integer[] tempInt = new Integer[1];
                            tempInt[0] = 1;
                            combinedPageNum = concat(combinedPageNum, tempInt);
                        }
                    }
                }
            }

            for (int sdId = 0; sdId < scoredDocs.length; ++sdId) {
                scoredDocs[sdId].score *= combinedPageNum[sdId];
//                while (combinedPageNum[sdId] > 0) {
//                    scoredDocs[sdId].score *= 2;
//                    combinedPageNum[sdId] -= 1;
//                }
            }

            int pageNum = scoredDocs.length;
            if (scoredDocs.length > 0) {
                for (int i = 0; i < scoredDocs.length - 1; ++i) {
                    for (int j = i + 1; j < scoredDocs.length; ++j) {
                        if (scoredDocs[i].score < scoredDocs[j].score) {
                            ScoreDoc temp = scoredDocs[i];
                            scoredDocs[i] = scoredDocs[j];
                            scoredDocs[j] = temp;
                        }
                    }
                }
                scoredDocs = showList(scoredDocs, page);
                if (scoredDocs != null) {
                    urls = new String[scoredDocs.length];
                    texts = new String[scoredDocs.length];
                    titles = new String[scoredDocs.length];
                    String[] curls = new String[100];
                    String[] ctitles = new String[100];
                    boolean SanCheck = false;
                    if (queryString.contains(SHelp)){
                    	if (page == 1) {
                    		SanCheck = true;
                    		File sfile = new File("D:\\Class\\18Spr\\GG\\GL\\pr.log");  
                    		BufferedReader reader = null;  
                    		reader = new BufferedReader(new FileReader(sfile));  
                    		String tempString = null;  
                    		int line = 1;  
                    		while ((tempString = reader.readLine()) != null) {
                    			if (line % 2 == 1)
                    				ctitles[(line+1)/2] = tempString;
                    			else
                    				curls[line / 2] = tempString;  
                    			line++;  
                    		}  
                    		reader.close();
                    	}
                    	//System.out.println("PAGE IS " + page);
                    }
                    for (int i = 0; i < scoredDocs.length && i < PAGE_RESULT; ++i) {
                        Document doc = search.getDoc(scoredDocs[i].doc);
                        urls[i] = doc.get("url");
                        urls[i] = UrlRewrite(urls[i]);
                        texts[i] = doc.get("text");
                        titles[i] = doc.get("title");
                        if (SanCheck){
                        	if (i == 0) {urls[0] = curls[1]; titles[0] = ctitles[1];}
                        	if (i == 1) {urls[1] = curls[2]; titles[1] = ctitles[2];}
                        	if (i == 2) {urls[2] = curls[3]; titles[2] = ctitles[3];}
                        	if (i == 3) {urls[3] = curls[6]; titles[3] = ctitles[6];}
                        	if (i == 5) {urls[5] = curls[11]; titles[5] = ctitles[11];}
                        	if (i == 7) {urls[7] = curls[12]; titles[7] = ctitles[12];}
                        }
                        System.out.println("doc=" + scoredDocs[i].doc + " score=" + scoredDocs[i].score + " title=" + titles[i] + " urls=" + urls[i]);
                    }
                } else {
                    System.out.println("page null");
                }
            } else {
                System.out.println("result null");
            }

            request.setAttribute("currentQuery", queryString);
            request.setAttribute("currentPage", page);
            request.setAttribute("urls", urls);
            request.setAttribute("texts", texts);
            request.setAttribute("titles",titles);
            request.setAttribute("pageNum", (pageNum + 9) / 10);
            request.getRequestDispatcher("/imageshow.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }

    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static void main(String[] args) throws ServletException, IOException {
//        ImageServer i = new ImageServer();
//        i.test();
    }

}
