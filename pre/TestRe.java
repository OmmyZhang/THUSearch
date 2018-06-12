import java.util.*;
import java.util.regex.*;


public class TestRe
{
	public static void main(String[] args)
	{
		Pattern p = Pattern.compile("[\"\']\\s*(((https?:)?\\/\\/([^\\s\"\']*?\\.edu\\.cn))?\\/)?([^\"\'\\/][^\\s\"\']*?\\.html)(\\?([^\\s\'\"]*?=[^\\s\"\']*?))?\\s*[\"\']");
		
		String s = "herf = \" http://www.edu.cn/haha/h2.html?page=2\"";
		Matcher m = p.matcher(s);

		if(m.find())
		for(int i=0;i<=7;++i)
			System.out.println(m.group(i));
		System.out.println("--------------");

		s = "herf = \"/haha/h2.html\"";
		m = p.matcher(s);

		if(m.find())
		for(int i=0;i<=7;++i)
			System.out.println(m.group(i));
		System.out.println("--------------");
		
		s = "herf = \"../h3.html?page=2\"";
		m = p.matcher(s);

		if(m.find())
		for(int i=0;i<=7;++i)
			System.out.println(m.group(i));
		System.out.println("--------------");

		//s = "\"Commput.Math.oper.Res\",\"/publish/mathen/7018/index.html\"";
		//s = " href=\"http://ymsc.tsinghua.edu.cn/sli/index.html\" ";
		s = " \"http://www.tsinghuamathcamp.cn/index.html\" ";
		m = p.matcher(s);

		if(m.find())
		for(int i=0;i<=7;++i)
			System.out.println(m.group(i));
		System.out.println("--------------");

		String ts = "../0123456";
		System.out.println(ts.substring(0,3));

	}
}
