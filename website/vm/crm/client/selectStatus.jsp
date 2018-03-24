<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="/style/css/client.css" rel="stylesheet" type="text/css" />
<link href="/style/css/client_sub.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
function returnValue(){
 	return $("input:[name='preMode']:checked",document).val() ;
}

function dblSelect(){
	var preModeId = $("input:[name='preMode']:checked",document).val() ;
	parent.dbPreMode(preModeId);
}

function checkRadio(obj){
	$(obj).find("input").attr("checked","checked");
}
</script>
</head>
<body class="body_f4">
<div class="boxbg2 subbox_w265">
<div class="subbox cf">
  <div class="operate operate2">
  <ul>
  <li class="sel">选择生命周期</li>
  </ul>
  <div class="close2"></div>
  </div>
  <div class="bd">
      <div class="inputbox inputbox3">
		<ul>
			#set($public=$request.getParameter("public"))
        	#foreach($item in $globals.getEnumerationItems($enumerationName))
        	#if($item.value==1 && "$public"=="public")
        	#else
        	<li ondblclick="dblSelect()" onclick="checkRadio(this);"><input type="radio" name="preMode" value="$item.value" #if("$!item.value"=="4") checked="checked" #end/>$item.name</li>
        	#end 
        	#end
        	<!--<li class="colSel"><input type="radio" name="type" checked="checked"/>公共池客户</li>-->
        </ul>
      </div>
  </div>
</div>
</div>
</body>
</html>