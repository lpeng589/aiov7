<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<meta name="renderer" content="webkit">    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script language="javascript" src="$globals.js("/js/login.vjs","",$text)"></script>	
	<script language="javascript" src="/js/function.js"></script>	
	<script type="text/javascript" src="/js/jquery.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
function loading(){
	url = '/UtilServlet?operation=queryHTMLModuleID&id='+"$!request.getParameter("selectId")";
	jQuery.get(url,function(data){
		var result=document.getElementById("result");
		result.innerHTML=data;
	});	
}
</script>
  </head>
 
  <body onload="loading()">
  #if($!request.getParameter("selectId")=="1.html")
  <div>
  	<h3>
		<img align="left" height="100" style="margin-right: 10px" width="100" />在此处输入标题

	</h3>
	<p>
		在此处输入内容

	</p>
  </div>
  #end
   #if($!request.getParameter("selectId")=="2.html")
  <div>
  <h3>
		标题
	</h3>
	<table style="width:100%;" cellpadding="2" cellspacing="0" border="1">
		<tbody>
			<tr>
				<td>
					<h3>标题1</h3>
				</td>
				<td>
					<h3>标题1</h3>
				</td>
			</tr>
			<tr>
				<td>
					内容1
				</td>
				<td>
					内容2
				</td>
			</tr>
			<tr>
				<td>
					内容3
				</td>
				<td>
					内容4
				</td>
			</tr>
		</tbody>
	</table>
	<p>
		表格说明
	</p>
  </div>
  #end
  #if($!request.getParameter("selectId")=="3.html")
  <div>
  	<p>
		在此处输入内容
	</p>
	<ol>
		<li>
			描述1
		</li>
		<li>
			描述2
		</li>
		<li>
			描述3
		</li>
	</ol>
	<p>
		在此处输入内容
	</p>
	<ul>
		<li>
			描述1
		</li>
		<li>
			描述2
		</li>
		<li>
			描述3
		</li>
	</ul>
  </div>
  #end
    <div id="result"></div> 
  </body>
</html>
