<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get(&quot;oa.common.knowledgeCenter&quot;)</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>

<script>
	function openInputDate(obj){
		WdatePicker({lang:'$globals.getLocale()'});
	}
	
	function sureDelN(itemName){
		if(!isChecked(itemName)){
			alert('$text.get("common.msg.mustSelectOne")');
			return false;
		}
		form.operation.value=$globals.getOP("OP_DELETE");
		if(!confirm('$text.get("common.msg.confirmDel")')){
			form.operation.value = $globals.getOP("OP_QUERY");
			cancelSelected("input");
			return false;
		}else{
			form.submit();
		}
	}
</script>
<style type="type/css">

</style>
</head>

<body >
<form action="ExamAction.do" name="form" method="post">
<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")" />	
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("exam.lb.takepart")</div>
	<div class="HeadingButton">
		<li><button type="submit"  onClick="" class="b2">$text.get("common.lb.query")</button></li>
		#if($LoginBean.getOperationMap().get("/ExamAction.do").delete())
		<li><button type="button"  onClick="javascript:return sureDelN('keyId');" class="b2">$text.get("common.lb.del")</button></li>
		#end
	</div>
</div>
<div class="listRange_frame">
		<div class="listRange_1" id="listid">
		<li><span>$text.get("exam.lb.startTime")≥：</span><input name="startTime" value="$!globals.get($searchValues,1)" onClick="openInputDate(this);" onKeyDown="if(event.keyCode==13) {if(!beforeButtonQuery())return false ; submitQuery();}" ></li>
		<li><span>$text.get("exam.lb.titleType")：</span>
			<select name="titleType" id="select">
			 <option value=""  selected="selected"></option>
			 #foreach($question in $questionType)
			  <option value="$question" #if($question==$!globals.get($searchValues,2)) selected="selected" #end>$question</option>
		     #end
			</select>
		</li>
		<li><span>$text.get("exam.lb.empName")：</span><input name="empName" value="$!globals.get($searchValues,3)" onKeyDown="if(event.keyCode==13) {if(!beforeButtonQuery())return false ; submitQuery();}" ></li>
		</div>
</div>
<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var dHeight=document.body.clientHeight;
var varHeight=document.getElementById("listid");
var sHeight = 0 ;
if(typeof(varHeight)!="undefined" && varHeight!=null){
	sHeight = varHeight.clientHeight ;
}
oDiv.style.height=dHeight-sHeight-68+"px";
</script>
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" style="table-layout:auto;">
		<thead>
			<tr height="25px">
				<td style="width:30px" class="oabbs_tc">&nbsp;</td>
				<td style="width:20px" class="oabbs_tc">
					<input type="checkbox" id="chkAll" onClick="checkAll('keyId')" />
				</td>
			    <td style="width:140px" class="oabbs_tc">$text.get("exam.lb.titleType")</td>
				<td style="width:120px" class="oabbs_tc">$text.get("exam.lb.quantity")</td>
				<td style="width:140px" class="oabbs_tc">$text.get("exam.lb.startTime")</td>
				<td style="width:140px" class="oabbs_tc">$text.get("exam.lb.endTime")</td>
				<td style="width:120px" class="oabbs_tc">$text.get("exam.lb.empName")</td>
				<td style="width:140px" class="oabbs_tc">$text.get("exam.lb.getExam")</td>
			    <td style="width:100px" class="oabbs_tc">$text.get("oa.calendar.option")</td>
			</tr>
		</thead>
		<tbody>
		 #foreach($ls_row in $listTest)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td style="width:30px" class="oabbs_tc">$velocityCount</td>
				<td style="width:20px" class="oabbs_tc">
					<input type="checkbox" id="keyId" name="keyId" value="$!globals.get($ls_row,8)" #if(!$LoginBean.getOperationMap().get("/ExamAction.do").delete()) disabled #end />
				</td>
			    <td class="oabbs_tc">$!globals.get($ls_row,1)&nbsp;</td>
				<td class="oabbs_tc">$!globals.get($ls_row,2)&nbsp;</td>
				<td class="oabbs_tc">$!globals.get($ls_row,3)&nbsp;</td>
				<td class="oabbs_tc">$!globals.get($ls_row,4)&nbsp;</td>
				<td class="oabbs_tc">$!globals.get($ls_row,5)&nbsp;</td>
				<td class="oabbs_tc">$!globals.get($ls_row,6)&nbsp;</td>
			    <td class="oabbs_tc">
					<a href="/ExamAction.do?operation=1&examId=$!globals.get($ls_row,0)&titleType=$!globals.get($ls_row,7)&examDetId=$!globals.get($ls_row,8)&quantity=$!globals.get($ls_row,2)">$text.get("exam.lb.takepart")&nbsp;</a>
				</td>
			</tr>
		#end
		#if(!$listTest)
		 	<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td style="width:30px" class="oabbs_tc">&nbsp;</td>
				<td style="width:20px" class="oabbs_tc">&nbsp;</td>
			    <td class="oabbs_tc">&nbsp;</td>
				<td class="oabbs_tc">&nbsp;</td>
				<td class="oabbs_tc">&nbsp;</td>
				<td class="oabbs_tc">&nbsp;</td>
				<td class="oabbs_tc">&nbsp;</td>
				<td class="oabbs_tc">&nbsp;</td>
			    <td class="oabbs_tc">&nbsp;</td>
			</tr>
		 #end
		  </tbody>
		</table>
	</div>
		<div class="oalistRange_pagebar listRange_pagebar" style="float:right;display:inline-block;width:auto;">
		 $!pageBar
		 </div>
	</form>
</body>
</html>
