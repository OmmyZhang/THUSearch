import java.util.*;
import java.util.regex.*;
import java.io.*;

class WebNode
{
    public String site;
    public String path;
    public int d_in,d_out;
    public double pr, old_pr; //PageRank

	// more var for Index

    public static Map<String, WebNode> nodeMap;
	
	public static WebNode get_or_create(String site, String path)
	{
		return get_or_create(site,path,false);
	}
	public static WebNode get_or_create(String site, String path, boolean create)
	{
		site = site.toLowerCase();
		path = path.toLowerCase(); // Tsinghua's Website is interesting..

		WebNode wn = nodeMap.get(site+path);
        if(wn == null && create)
            nodeMap.put(site+path, wn = new WebNode(site, path));

		return wn;
	}

	public static void init_map()
	{
		System.out.println("Warning: will not use crawl.log");
        nodeMap = new HashMap<String,WebNode>();
	}

	public static void init_map(String log_file) throws Exception
	{
        nodeMap = new HashMap<String,WebNode>();
		File lf = new File(log_file);

		Scanner scan = new Scanner(lf);
		Pattern p = Pattern.compile("https?:\\/\\/(\\S*?\\.edu\\.cn)(\\/\\S*?)\\.html(\\?(\\S*=\\S*))?");

		while(scan.hasNextLine())
		{
			String line = scan.nextLine();
			//if(line.contains("200"))
			{
				Matcher m = p.matcher(line);

				while(m.find())
				{
					String site = m.group(1);
					String path = m.group(2);
					String parm = m.group(4);

					//if(path.equals("/activitycenter/activity"))
					//	System.out.println(site+'\n'+path+'\n'+parm);
					
					if(parm != null)
					{
						get_or_create(site, path+parm+".html",true);
						//System.out.println(line);
						//if(parm.equals("page=2") && path.equals("/activitycenter/activity"))
						//	System.out.println(site+'\n'+path+'\n'+parm);
					}
					else
						get_or_create(site,path+".html",true);

				}
			}
		}

	}
 
    public WebNode(String s, String p)
    {
        site = s;
        path = p;
        d_in = d_out = 0;
    }   
}

