import java.util.*;

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
		WebNode wn = nodeMap.get(site+path);
        if(wn == null)
            nodeMap.put(site+path, wn = new WebNode(site, path));
			
		return wn;
	}
	public static void init_map()
	{
        nodeMap = new HashMap<String,WebNode>();
	}
 
    public WebNode(String s, String p)
    {
        site = s;
        path = p;
        d_in = d_out = 0;
    }   
}

