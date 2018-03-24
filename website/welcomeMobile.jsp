<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Welcome</title>

<link rel="stylesheet" href="/js/tree/jquery.treeview.css" /> 
<script src="/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript">

function AddWin(Url,Title){
	document.getElementById("home").src=Url;
}

function mdiwin(url,title,alertId){  
	
	document.getElementById("home").src=url;
	if(typeof(alertId)!="undefined"){
		jQuery.get("UtilServlet?operation=updateAlertStatus&alertId="+alertId) ;
	}
}


function addWins(url,title){
	mdiwin(url+"&name="+encodeURIComponent(title),title) ;
}



</script>
</head>
<body> 
<iframe id="home" name="home" src="/moduleFlow.do?operation=my_dest"  frameborder=false scrolling="no" width="100%" height="100%" frameborder=no ></iframe>

</body>
</html>