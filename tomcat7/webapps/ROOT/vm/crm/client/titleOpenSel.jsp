<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta name="renderer" content="webkit">
<title>My JSP 'titleOpenSel.jsp' starting page</title>
<script language="javascript" src="/js/jquery.js"></script>
<style type="text/css">
body,ul,li{margin:0;padding:0;}
body {font:12px Microsoft Yahei;color:#0c0c0c;outline:0;overflow:hidden;}
li{list-style:none;}
.sel-list-ul{padding:10px 10px 0 30px;display:inline-block;height:190px;overflow:hidden;overflow-y:auto;}
.sel-list-ul>li{float:left;display:inline-block;overflow:hidden;margin:0 5px 8px 0;width:110px;}
.sel-list-ul>li>.cbox{float:left;display:inline-block;margin:2px 2px 0 0;}
.sel-list-ul>li>label{float:left;line-height:17px;display:inline-block;cursor:pointer;width:90px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;}
</style>
<script type="text/javascript">
function returnVal(){
	var str="";
	jQuery("input:checked").each(function(){
		str	+= $(this).val() + ";"+$(this).attr("opName") +"|";
	})
	return str;
}
</script>
</head>
  <body>
  	<ul class="sel-list-ul">
  	#set($inputType=$!request.getParameter("inputType"))
  	#set($type="duty")
  	#set($strtype=$!request.getParameter("strType"))
  	#if("$!strtype"!="")
  	#set($type=$!strtype)
  	#end
  	#foreach($item in $globals.getEnumerationItems("$!type"))	 
  		<li><input class="cbox" #if("radio"==$inputType) type="radio" name="post" #else type="checkbox"#end value="$item.value" id="$item.value" opName="$item.name"><label for="$item.value">$item.name</label></li>
    #end
    </ul>
  </body>
</html>
