import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class PageRank
{
    private static final String base = "data";
    private static String site;
    private static Map<String, WebNode> nodeMap;
    
    private static void link(WebNode cNode,WebNode  tNode)
    {
        cNode.d_out++;
        tNode.d_in++;
    }
    
    private static void deal_file(File f, String path) throws Exception
    {
        System.out.println(path);
        
        WebNode curNode;
        if(!nodeMap.containsKey(site+path))
            nodeMap.put(site+path, new WebNode(site, path));
        curNode = nodeMap.get(site+path);
        
        Scanner scan = new Scanner(f);
        Pattern p = Pattern.compile("[\"\'](\\/.*?\\.html)[\"\']");
        while(scan.hasNext())
        {
            String line = scan.next();
            Matcher m = p.matcher(line);
            
            while(m.find())
            {
                String go_in_site_path = m.group(1);
                System.out.println(" : --> " + go_in_site_path);
                
                WebNode targetNode;
                if(!nodeMap.containsKey(site + go_in_site_path))
                    nodeMap.put(site + go_in_site_path, new WebNode(site, go_in_site_path));
                targetNode = nodeMap.get(site+go_in_site_path);
                
                link(curNode, targetNode);
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
                make_graph(path+"/"+fname);
            else
                if(fname.endsWith(".html"))
                    deal_file(f, path+"/"+fname);
        }
        
    }

    public static void main(String[] args) throws Exception
    {
        nodeMap = new HashMap<String,WebNode>();
        site = "math.tsinghua.edu.cn";
        make_graph("");
        
        for(WebNode wn:nodeMap.values())
        {
            System.out.println(wn.d_in + " " + wn.d_out + " " + wn.path);
        }
    }
}

class WebEdge
{
    public WebNode from,to;
    
    public WebEdge(WebNode f, WebNode t)
    {
        from = f;
        to = t;
    }
}
