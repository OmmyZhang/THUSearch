
class WebNode
{
    public String site;
    public String path;
    public int d_in,d_out;
    public float pr; //PageRank
 
    public WebNode(String s, String p)
    {
        site = s;
        path = p;
        d_in = d_out = 0;
    }   
}

