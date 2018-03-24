<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("tableInfo.title")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script type="text/javascript">
function copyOperation()
{
	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	keyIds = $("input[name=keyId]:checked");
	if(keyIds.size() >1){
		alert('只能选择一个表进行复制');
		return false;
	}
	keyId = keyIds.val();
	urls = '/CustomAction.do?operation=$globals.getOP("OP_COPY_PREPARE")&keyId='+keyId+'&search=$!encode';
	openPop('TablemgtDiv','添加表单',urls,width,height,true,true);
}


function addPrepare(){

	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	urls = '/CustomAction.do?operation=$globals.getOP("OP_ADD_PREPARE")';
	openPop('TablemgtDiv','添加表单',urls,width,height,true,true);
	 
}
function updatePrepare(keyId){
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	urls = '/CustomAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId='+keyId+'&search=$!encode';
	openPop('TablemgtDiv','添加表单',urls,width,height,true,true);
}
function openImport(){
	width=500;
	height=350;
	urls = '/vm/tableInfo/importTable.jsp?type=1';
	openPop('TableimportDiv','导入模块',urls,width,height,true,false);
}
</script>
</head>

<body onLoad="showtable('tblSort');document.getElementById('abcd').focus();">

<form  method="post" scope="request" name="form" action="/CustomQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingTitle">$text.get("tableInfo.title")</div>
	<ul class="HeadingButton">
		<li><span class="hBtns" onClick="addPrepare()">$text.get("common.lb.add")</span></li>
		<li><span class="hBtns" onClick="form.winCurIndex.value='$!winCurIndex';var sd = sureDel('keyId'); if(sd) document.form.submit();">$text.get("common.lb.del")</span></li>
		<li><span class="hBtns" onClick="form.winCurIndex.value='$!winCurIndex';copyOperation();">$text.get("common.lb.copy")</span></li>
		<li><span class="hBtns" onClick="openImport()">导入</span></li>
	</ul>	
</div>
<div class="listRange_frame" style="padding:0 10px;">		
	<div class="listRange_1">
		<li style="width: 350px;"><span>$text.get("common.lb.query")：</span>
		<input type="text" id="abcd" name="search" style="width:200px" value="$!search" onkeydown="if(event.keyCode==13){  document.form.submit();}">	
		</li>
		<li>
<button name="Submit" type="submit" onClick="forms[0].operation.value=$globals.getOP("OP_QUERY");" class="hBtns">$text.get("common.lb.query")</button>
<button name="clear" type="botton" onClick="clearForm(form);" class="hBtns">$text.get("common.lb.clear")</button>

	</li>
	
	</div>
	<div class="scroll_function_small_a" id="conter">
		<table border="0" width="100%" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr class="listhead">
			  <td width="30" align="center" class="listheade"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
			  	<td width="100" align="center" class="listhead30">$text.get("com.report.module.type")</td>
				<td width="100" align="center" class="listhead30">$text.get("tableInfo.lb.tableName")</td>
				<td align="center">$text.get("tableInfo.lb.tabeDisplayName")</td>
				<td align="center">表类型</td>
				<td width="80" align="center">$text.get("tableInfo.lb.userType")</td>
				<td width="80" align="center">$text.get("tableInfo.lb.updateType")</td>
				<td width="150" align="center">$text.get("common.lb.update")</td>
			</tr>			
		</thead>
		<tbody>
		#foreach ($row in $result ) 
			#if($!globals.filterTabel($!globals.get($row,0)))
			##if(!($!globals.get($row,0).indexOf("Flow_")>-1 || $!globals.get($row,0).indexOf("FlowN")>-1))			
			#if(!($!globals.get($row,0).indexOf("Flow_")>-1) || $!globals.get($row,0).indexOf("FlowNwaichushenqingdan2")>-1 || $!globals.get($row,0).indexOf("FlowNqingjiadan2")>-1)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">				
				<td align="center" class="listheadonerow"><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td>
				<td align="left">#foreach($erow in $globals.getEnumerationItems("MainModule"))#if($erow.value==$globals.get($row,7))$erow.name#end#end</td>
				<td align="left">$!globals.get($row,0)</td>
				<td align="left">$!globals.get($row,1)</td>
				<td align="left">#if("$!globals.get($row,2)"=="0")主表#elseif("$!globals.get($row,2)"=="1")明细表($!globals.get($row,5))#else 邻居表($!globals.get($row,5)) #end</td>
				<td align="center">#if($!globals.get($row,3) == 0) $text.get("tableInfo.lb.userTable")  #else $text.get("tableInfo.lb.sysTable") #end</td>
				<td align="center">#if($!globals.get($row,4) == 0) $text.get("tableInfo.lb.canUpdate")  #else $text.get("tableInfo.lb.noUpdate") #end</td>
				
				<td align="center">
				
				<a href='javascript:updatePrepare("$globals.get($row,0)")'>$text.get("common.lb.update")</a>
				
				<a style="padding-left:20px" href='/CustomQueryAction.do?type=export&tableName=$globals.get($row,0)'>导出模块</a>
				</td>
			</tr>
			#end
			#end
		#end
		</tbody>
		</table>
	  </div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-92;
oDiv.style.height=sHeight+"px";
//]]>
</script>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
</body>
</html>
