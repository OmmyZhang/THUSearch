<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
request.setCharacterEncoding("utf-8");
System.out.println(request.getCharacterEncoding());
response.setCharacterEncoding("utf-8");
System.out.println(response.getCharacterEncoding());
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
System.out.println(path);
System.out.println(basePath);
%>

<!DOCTYPE HTML>

<html lang="en-US">

<head>

  <meta charset="UTF-8">

  <title>校园搜索</title>

  <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

  <style type="text/css">

    #search{float:left;height:40px;width:400px;}

    #searchBtn{float:right;height:40px;line-height:30px;font-size:16px;padding:0 15px;z-index:999;left:380px;position:absolute;}

    #search2{float:left;height:40px;width:400px;}

    #searchBtn2{height:40px;line-height:30px;font-size:16px;padding:0 15px;margin:0 10px;}

  </style>

</head>

<body>

  <h3 style="font-family:Microsoft Yahei;margin:20px 100px">搜索</h3>

  <form action="servlet/ImageServer" method="GET" style="position:relative;margin:50px 100px;">

    <input type="search" name="query" id="search" class="form-control xx10" placeholder="清华">

    <button type="submit" id="searchBtn" class="btn btn-primary"><span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;搜索</button>

  </form>

   

   

</body>

</html>

<!--  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>搜索</title>
<style type="text/css">
<!--
#Layer1 {
	position:absolute;
	left:489px;
	top:326px;
	width:404px;
	height:29px;
	z-index:1;
}
#Layer2 {
	position:absolute;
	left:480px;
	top:68px;
	width:446px;
	height:152px;
	z-index:2;
}

</style>
</head>
<body>
<div id="Layer1" style="top: 210px; left: 353px; width: 441px;">
  <form id="form1" name="form1" method="get" action="servlet/ImageServer">
    <label>
      <input name="query" type="text" size="50" />
    </label>
    <label>
    <input type="submit" name="Submit" value="搜索" />
    </label>
  </form>
</div>
</body>
</html>
-->


