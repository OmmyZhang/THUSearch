import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class PageRank
{
    private static final String base_data_dir = "data";
	private static String base; // current base path
    private static String site; // current site
	private static Set<String> sites; // all sites

	private static final String slash = "/"; // NOTE: may change to \ in Windows

    

    private static void deal_file(File f, String path) throws Exception
    {
        
        WebNode curNode = WebNode.get_or_create(site, path);

		if(curNode == null)
		{
			System.out.println("Warning: html file not in log\n" + site + path + "\n\n");
			//System.out.println(WebNode.nodeMap);
			//return;
		}
        
        Scanner scan = new Scanner(f);
		Pattern p = Pattern.compile("[\"\']\\s*(((https?:)?\\/\\/([^\\s\'\"]*?\\.cn))?\\/)?([^\\/\"\'][^\\s\"\']*?)\\.html(\\?([^\\s\"\']*?=[^\\s\"\']*?))?\\s*[\"\']");
        while(scan.hasNext())
        {
            String line = scan.next();
            Matcher m = p.matcher(line);
            
            while(m.find())
            {
                String full_site = m.group(1);
                String go_site = m.group(4);
                String go_path  = m.group(5);
                String parm = m.group(7);
               
				if(full_site == null) // relative path
				{
					go_site = site;
					int pos = path.lastIndexOf("/");
					String par_path = path.substring(0,pos+1);

					while(go_path.startsWith("../"))
					{
						par_path = par_path.substring(0,pos);
						pos = par_path.lastIndexOf("/");
						par_path = par_path.substring(0,pos+1);

						go_path = go_path.substring(3);
						
					}
					go_path = par_path + go_path;

				}
				else
				{
					if(full_site.equals("/"))
						go_site = site;
					go_path = "/" + go_path;
				}

				if(parm == null)
					go_path = go_path + ".html";
				else
					go_path = go_path + parm + ".html";
                //System.out.println(" : --> " + go_in_site_path);
                
                WebNode targetNode = WebNode.get_or_create(go_site, go_path);
				if(targetNode == null)
				{
				//	System.out.println("now at: " + site + path);
				//	System.out.println("ori: " + m.group(0));

					System.out.println("Outside link: (" + go_site + "), (" + go_path + ")");
				}
				else
					WebEdge.link(curNode, targetNode);
            }
        }
        scan.close();
    }
    
    public static void make_graph(String path) throws Exception
    {
        File currDir = new File(base+path);
        String[] fileList = currDir.list();
        for(String fname:fileList)
        {
            File f = new File(currDir.getPath(), fname);
            if(f.isDirectory())
                make_graph(path + slash + fname);
            else
                if(fname.endsWith(".html"))
                    deal_file(f, path + slash + fname);
        }
        
    }

	private static void calc_pr()
	{
		final double alpha = 0.15;
		final int TN = 30; //  How many times when calc PageRank

		int n = WebNode.nodeMap.size();
		for(WebNode wn:WebNode.nodeMap.values())
		{
			wn.pr = 1.0 / n;
		}

		for(int k=1; k< TN; ++k)
		{
			double free = 0;
			for(WebNode wn:WebNode.nodeMap.values())
			{
				wn.old_pr = wn.pr;
				wn.pr = alpha / n;

				if(wn.d_out == 0)
					free += wn.old_pr;
			}

			for(WebEdge we:WebEdge.eList)
				we.to.pr += (1 - alpha) * we.from.old_pr / we.from.d_out;

			for(WebNode wn:WebNode.nodeMap.values())
				wn.pr += (1 - alpha) * free / n;
		}
	}

    public static void main(String[] args) throws Exception
    {
		File rootDir = new File(base_data_dir);
		WebNode.init_map("data/crawl.log");

		sites = new HashSet<String>(Arrays.asList(rootDir.list()));
		for(String fname:sites)
		{
			if( ! new File(base_data_dir + slash + fname).isDirectory())
				continue;
			site = fname;
			System.out.println("site: " + site);
			base = base_data_dir + slash + site;
			make_graph("");
		}

		// save if need

		calc_pr();
       /* 
        for(WebNode wn:WebNode.nodeMap.values())
        {
            System.out.println(String.format("%.6f %d %d %s",wn.pr,wn.d_in,wn.d_out,wn.path));
        }
		*/
    }
}

//--------------------------------------

class WebEdge
{
    public WebNode from,to;
	public static List<WebEdge> eList = new ArrayList<WebEdge>(); 
    
    public WebEdge(WebNode f, WebNode t)
    {
        from = f;
        to = t;
    }
    
	protected static void link(WebNode f,WebNode  t)
    {
        f.d_out++;
        t.d_in++;
		eList.add(new WebEdge(f,t));

    }
}
