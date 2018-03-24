<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("common.title.SystemOptimize")</title>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/listGrid.js"></script>
<script language="javascript" src="/js/gen/listPage.vjs_zh_CN.js"></script>
<script language="javascript"> 
 
var winCurIndex="3";
var draftQuery = "";
var lngIfClass = "0";
var f_brother = "" ;
var parentCode="";
 
var updateRight = false;
var printRight = false;
 

</script>
<style> 
.scrollColThead {
position: relative;
top: expression(this.parentElement.parentElement.parentElement.scrollTop);
z-index:2;
}
</style>
</head>
 
<body onLoad="showStatus();">
<form  method="post" scope="request"  name="form" action="/OptimizeAction.do" >
<input type="hidden" name="optimize" value=""> 
<input type="hidden" name="name" value="seward"> 
 <div class="Heading">
	<div class="HeadingIcon"><img src="/style1/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("common.title.SystemOptimize") </div>
	<ul class="HeadingButton">
	<li>#if("$!onlineUser" !="" )在线用户数：$!onlineUser ，网页手机在线用户数:$!wxqyonlineUser #end</li>
	<li><button name="b" onClick="form.optimize.value='runTime'; form.submit();">$text.get("optimize.lb.collectSystemRunMes")</button></li>	
	<li><button name="b" onClick="form.optimize.value='freeProcCache'; form.submit();">$text.get("optimize.lb.clearCashe")</button></li>	
	<li><button name="b" onClick="form.optimize.value='tableRows'; form.submit();">查看表信息</button></li>
	<li><button name="b" onClick="form.optimize.value='indexUse'; form.submit();">索引分析</button></li>
	<li><button name="b" onClick="form.optimize.value='indexFragmentation'; form.submit();">索引碎片整理</button></li>
	<li><button name="b" onClick="form.optimize.value='block'; form.submit();">$text.get("optimize.msg.CheckDBblock")</button></li>
	<li><button name="b" onClick="form.optimize.value='acc'; form.submit();">凭证不平查询</button></li>
	<li><button name="b" onClick="form.optimize.value='bug'; form.submit();">BUG检查</button></li>
	</ul>
	</div>
						<div class="scroll_function_small_a" id="conter">
 
$!MSG			
		
<table sortCol="0" isDesc="true" border="1" isStatsAll=false hasStat=true cellpadding="0" cellspacing="0"  id="tblSort" name="table" width="100%">
<THead>
<tr class="scrollColThead"> 
#foreach($i in [0..$column]) 
<td align="center" >$globals.get($first,$i)</td>
#end
</tr>
</THead>
<TBody>
#foreach($row in $result)
#if($velocityCount > 1)
<tr class="trbg" bk="#EEF7FF" >

#foreach($i in [0..$column]) 
	#if(($i == 9 && "$runTime" == "true") || ($i == 12 && "$block" == "true"))
	<td  width=350 align=left style="white-space:wrap;"><textarea rows="3" cols="80">$globals.get($row,$i)</textarea> </td>
	#elseif($optimize=="acc")
	<td  width=130 align=left style="white-space:wrap;"> &nbsp;$globals.get($row,$i)</td>
	#elseif($optimize=="tableRows" && $i == 6)
	<td  width=230 align=left style="white-space:wrap;">&nbsp; $globals.get($row,$i)</td>
	#elseif($optimize=="tableRows" && $i == 0)
	<td  width=230 align=left style="white-space:wrap;">&nbsp; <a target='_blank' href='/OptimizeAction.do?optimize=tableInfo&tableName=$globals.get($row,$i)' >$globals.get($row,$i)</td>	
	#else
	<td  width=130 align=left style="white-space:wrap;"> &nbsp;$globals.encodeHTML("$globals.get($row,$i)")</td>
	#end
#end
</tr>
#end  
#end
</TBody>
</table>
 
			
		</div>	
</div>
<script type="text/javascript"> 
	var rowCount = document.getElementsByName("keyId").length ;
	if("100"!=rowCount){
		var varPage = document.getElementById("nextPageSize") ;
		if(typeof(varPage)!="undefined" && varPage!=null){
			varPage.removeAttribute("href","") ;
		}
	}	
</script>
</form>
</body>
</html>

