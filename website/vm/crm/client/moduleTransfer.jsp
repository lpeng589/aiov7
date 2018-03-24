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
 	return $("input:[name='module']:checked",document).val() ;
}

function dblSelect(){
	var moduleId = $("input:[name='module']:checked",document).val() ;
	parent.dbModuleTran(moduleId);
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
  <li class="sel">选择模板</li>
  </ul>
  <div class="close2"></div>
  </div>
  <div class="bd">
      <div class="inputbox inputbox3">
		<ul>
			#if("$!isSetDefModule" == "true")
	        	#foreach($module in $moduleList)
	        		#if("$moduleId" != "$globals.get($module,1)")
	        			#if($!moduleCountMap.get($!globals.get($module,1)) >0)
				        	<li onclick="checkRadio(this);"><input type="radio" name="module" value="$globals.get($module,1)" #if("$!defModuleId" =="$globals.get($module,1)") checked="checked" #end/>$globals.get($module,0)</li>
			        	#end
	        		#end
	        	#end
        	#else
	        	#foreach($module in $moduleList)
	        		#if("$moduleId" != "$globals.get($module,1)")
	        			#if($!moduleCountMap.get($!globals.get($module,1)) >0)
				        	<li onclick="checkRadio(this);" ondblclick="dblSelect()"><input type="radio" name="module" value="$globals.get($module,1)" #if("$first" !="false") checked="checked" #end/>$globals.get($module,0) </li>
		        			#set($first = "false")
		        		#end
	        		#end
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