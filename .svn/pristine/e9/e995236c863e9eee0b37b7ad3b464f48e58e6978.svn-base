<!DOCTYPE html>
<html>
<head>
<title>$globals.getCompanyName('') </title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="/mobile/css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" href="/mobile/css/mWorkFlow.css" type="text/css" />
<link rel="shortcut icon" href="/favicon.ico">
<script src="/mobile/js/jquery.min.js"></script>
<script src="/mobile/js/jquery.mobile-1.4.5.min.js"></script>
<script type="text/javascript"> 
$(document).ready(function() {
	jQuery.get("/MobileAjax?op=getMenu",function(data){ 
		if(data.code=="OK"){
			for(i=0;i<data.obj.length;i++){
				$("#pageOne").find("ul").append("<li onclick='goto(\"/mobile/define/"+data.obj[i][0]+"\")'><img src='/mobile/define/"+data.obj[i][2]+"' /><span>"+data.obj[i][1]+"</span></li>");
			}
		}else{
			location.href="/mlogin.jsp"
		} 
	},"json" ); 
	jQuery.get("/MobileAjax?op=getHome",function(data){ 
		if(data.code=="OK"){
			for(i=0;i<data.obj.length;i++){
				$("#pageOne").find("ul").append("<li onclick='goto(\"/mobile/define/"+data.obj[i][0]+"\")'><img src='/mobile/define/"+data.obj[i][2]+"' /><span>"+data.obj[i][1]+"</span></li>");
			}
		}else{
			location.href="/mlogin.jsp"
		} 
	},"json" ); 
}); 
function goto(url){
	window.location.href=url;
}
</script>
<style type="text/css">
#pageOne ul{
padding: 0px;
margin: 0px;
}
#pageOne li{
float: left;
width: 80px;
list-style-type: none;
text-align: center;
margin: 4px;
padding: 0px;
}
#pageOne li img{
border: none;
display: block;
margin: auto;
width: 80px;
height: 80px;
border: 1px solid #CDBFCD;
border-radius: 6px;
}
#pageOne li span{
width: 80px;
overflow: hidden;
color: #B8B5BE;
font-size: 13px;
}
</style>
</head>
<body>
<div data-role="page" id="pageOne">
  <div data-role="content">
  	<p>$LoginBean.departmentName &nbsp;$LoginBean.empFullName 您好</p>
	<ul>
		<li onclick="goto('/MobileQueryAction.do')"><img src="/mobile/flow.png" /><span>我的工作流</span></li>
	</ul>
  </div>
</div>
</body>
</html>
			