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
 

				-->	
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
<input type="hidden" name="tableName" value="$!tableName">
<input type="hidden" name="isSave" value="false"> 
 <div class="Heading">
	<div class="HeadingIcon"><img src="/style1/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">表名：$!tableName &nbsp;&nbsp;&nbsp;描述：<input name="tableDesc" value="$!tableDesc"/></div>
	<ul class="HeadingButton">

	<li><button name="b" onClick="form.optimize.value='tableInfo';form.isSave.value='true'; form.submit();">保存</button></li>
	</ul>
	</div>
						<div class="scroll_function_small_a" id="conter">
 
$!MSG			
		
<table sortCol="0" isDesc="true" border="1" isStatsAll=false hasStat=true cellpadding="0" cellspacing="0"  id="tblSort" name="table" width="100%">
<THead>
<tr class="scrollColThead"> 
<td width="30" align="center" >字段编号</td>
<td width="100" align="center" >字段名字</td>
<td width="30" align="center" >主键</td>
<td width="30" align="center" >自增长</td>
<td width="30" align="center" >计算字段</td>
<td width="30" align="center" >类型</td>
<td width="30" align="center" >长度</td>
<td width="30" align="center" >精度</td>
<td width="30" align="center" >小数</td>
<td width="30" align="center" >是否为空</td>
<td width="30" align="center" >默认值</td>
<td width="200" align="center" >描述</td>
<td width="100" align="center" >索引</td>
<td width="30" align="center" >索引排序</td>
<td width="30" align="center" >创建时间</td>
<td width="30" align="center" >修改时间</td>

</tr>
</THead>
<TBody>
#foreach($row in $result)

<tr class="trbg" bk="#EEF7FF" >
#foreach($i in [0..$column]) 
	#if($i == 11)
	<td  align=left style="white-space:wrap;"> 
	<input name="column" type="hidden" value="$globals.get($row,1)"/>
	<input name="col$globals.get($row,1)" value="$globals.get($row,$i)"/></td>
	#else
	<td  align=left style="white-space:wrap;"> &nbsp;$globals.encodeHTML("$globals.get($row,$i)")</td>
	#end
	
#end
</tr>
#end  

</TBody>
</table>
索引情况
<table sortCol="0" isDesc="true" border="1" isStatsAll=false hasStat=true cellpadding="0" cellspacing="0"  id="tblSort" name="table" width="100%">
<THead>
<tr class="scrollColThead"> 
#foreach($i in [0..$indexcolumn]) 
<td align="center" >$globals.get($indexfirst,$i)</td>
#end
</tr>
</THead>
<TBody>
#foreach($row in $indexresult)
#if($velocityCount > 1)
<tr class="trbg" bk="#EEF7FF" >

#foreach($i in [0..$indexcolumn])	
	<td  width=130 align=left style="white-space:wrap;"> &nbsp;$globals.encodeHTML("$!globals.get($row,$i)")</td>
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

