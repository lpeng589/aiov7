<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆出入信息</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/clientTransfer.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
.listRange_list_function tbody td .item_sp{display:block;border-top:1px #AED3F1 solid;}
.listRange_list_function tbody td .item_sp:first-child{border:0;}
</style>
</head>
<body onload="loadOrder()">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" action="/CarOperateAction.do" >
<input type="hidden" name="operation" value="4"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle" >车辆出入信息</div>		
</div>
<div id="listRange_id">			
	<div class="scroll_function_small_a" id="conter" style="margin-top:0px;">
	#foreach($log in $!List)
			
	<div><span>$!log</span></div><br />
			
	#end		 
		
</div>
</div>
</form>	
</body>
</html>