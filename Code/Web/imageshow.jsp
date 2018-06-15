<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String imagePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>搜索结果</title>

 <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

  <style type="text/css">

    #search{float:left;height:40px;width:400px;}

    #searchBtn{float:right;height:40px;line-height:30px;font-size:16px;padding:0 15px;z-index:999;left:380px;position:absolute;}

    #search2{float:left;height:40px;width:400px;}

    #searchBtn2{height:40px;line-height:30px;font-size:16px;padding:0 15px;margin:0 10px;}

</style>
</head>

<body>
<%
	String currentQuery=(String) request.getAttribute("currentQuery");
	int currentPage=(Integer) request.getAttribute("currentPage");
%>

  <h3 style="font-family:Microsoft Yahei;margin:20px 100px">搜索结果</h3>

  <form id="form1" action="ImageServer" method="GET" style="position:relative;margin:50px 100px;">

    <input type="search" name="query" id="search" class="form-control xx10" value="<%=currentQuery %>">

    <button type="submit" id="searchBtn" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;搜索</button>

  </form>


<div style="margin:130px 100px;">
  <div id="imagediv">

  <hr/>
  
  <Table style="left: 0px; width: 594px;">
  <% 
  	String[] Urls=(String[]) request.getAttribute("urls");
  	String[] Titles=(String[]) request.getAttribute("titles");
  	if(Titles!=null && Titles.length>0){
  		for(int i=0;i<Titles.length;i++){%>
  		<p>
  		<h4 style="margin:23px 0 0 0;">
  		<a href= "http://<%=Urls[i] %>" > <%=Titles[i] %> </a>
  		</h4>
  		<cite> <%=Urls[i] %> </cite>
  		<a target="_blank" href="/ImageSearch/mirr/<%=Urls[i] %>" >网页快照</a> 
  		
<%--   		<button class="btn btn-link dropdown-toggle" type="button" id="dropdownMenu<%=i %>" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
							_
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu<%=i %>">
							<li><a target="_blank" href="/ImageSearch/mirr/<%=Urls[i] %>" >网页快照</a></li>
						</ul>
  		
 --%>
  		<%-- <tr><h3><%=(currentPage-1)*10+i+1%>. <%=imgTags[i] %></h3></tr> --%>
  		<%-- <tr><img src="<%=imagePath+imgPaths[i]%>" alt="<%=imagePath+imgPaths[i]%>" width=200 height=100 /></tr> --%>
  		</p>
  		<%}; %>
  	<%}else{ %>
  		<p><tr><h3>no such result</h3></tr></p>
  	<%}; %>
  </Table>
  </div>
  <div>
  	<p>
		<%if(currentPage>1){ %>
			<a href="ImageServer?query=<%=currentQuery%>&page=<%=currentPage-1%>">上一页</a>
		<%}; %>
		<%for (int i=Math.max(1,currentPage-5);i<currentPage;i++){%>
			<a href="ImageServer?query=<%=currentQuery%>&page=<%=i%>"><%=i%></a>
		<%}; %>
		<strong><%=currentPage%></strong>
		<%for (int i=currentPage+1;i<=currentPage+5;i++){ %>
			<a href="ImageServer?query=<%=currentQuery%>&page=<%=i%>"><%=i%></a>
		<%}; %>
		<a href="ImageServer?query=<%=currentQuery%>&page=<%=currentPage+1%>">下一页</a>
	</p>
  </div>
</div>


<div>
</div>
</body>
