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
function exportGrade(){
	form.queryType.value = "export";
	form.submit();
}

</script>
<style type="text/css">
.pager_dv{display:inline-block;float:right;}
.pager_dv>div{display:inline-block;float:left;line-height:23px;}
.pager_dv>select{float:left;}
.pager_dv>input{float:left;}
.pager_dv>button{float:left;}
</style>
</head>

<body>
<form action="GradeAction.do" name="form" method="post">
<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")" />	
<input type="hidden" name="queryType" value="" />	
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("exam.lb.historyTest")</div>
	<div class="HeadingButton">
		<li><button type="submit" class="b2">$text.get("common.lb.query")</button></li>
		<!-- 
		#if($LoginBean.getOperationMap().get("/GradeAction.do").delete())
		<li><button type="button"  onClick="javascript:return sureDelN('keyId');" class="b2">$text.get("common.lb.del")</button></li>
		#end -->
		<li><button type="submit"  onClick="javascript:exportGrade();" class="b2">导出成绩</button></li>
	</div>
</div>
<div class="listRange_frame">
		<div class="listRange_1" id="listid">
		<li><span>$text.get("exam.lb.testStartTime")≥：</span><input name="testStartTime" value="$!globals.get($searchValues,0)" onClick="openInputDate(this);" onKeyDown="if(event.keyCode==13) {if(!beforeButtonQuery())return false ; submitQuery();}" ></li>
		<li><span>$text.get("exam.lb.score")：</span><input name="score" value="$!globals.get($searchValues,1)" onKeyDown="if(event.keyCode==13) {if(!beforeButtonQuery())return false ; submitQuery();}" ></li>
		<li><span>$text.get("exam.lb.titleType")：</span>
			<select name="titleType" id="select">
			 <option value=""  selected="selected"></option>
			 #foreach($question in $questionType)
			  <option value="$!question" #if($question==$!globals.get($searchValues,2)) selected="selected" #end>$!question</option>
		     #end
			</select>
		</li>
		<li><span>$text.get("exam.lb.takePartPerson")：</span><input name="takePartPerson" value="$!globals.get($searchValues,3)" onKeyDown="if(event.keyCode==13) {if(!beforeButtonQuery())return false ; submitQuery();}" ></li>
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
		<table width="400px" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table">
		<thead>
			<tr height="25px">
				<td style="width:30px" class="oabbs_tc">&nbsp;</td>
				<td style="width:30px" class="oabbs_tc">
					<input type="checkbox" id="chkAll" onClick="checkAll('keyId')" />
				</td>
			    <td width="160px" class="oabbs_tc">$text.get("exam.lb.titleType")</td>
				<td width="140px" class="oabbs_tc">$text.get("exam.lb.takePartPerson")</td>
				<td width="120px" class="oabbs_tc">$text.get("exam.lb.score")</td>
				<td width="120px" class="oabbs_tc">$text.get("exam.lb.problemQty")</td>
				<td width="140px" class="oabbs_tc">$text.get("exam.lb.testStartTime")</td>
				<td width="140px" class="oabbs_tc">$text.get("exam.lb.testUseTime")</td>
			    <td width="100px" class="oabbs_tc">$text.get("oa.calendar.option")</td>
			</tr>
		</thead>
		<tbody>
		 #foreach($ls_row in $listOldExam)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td style="width:30px" class="oabbs_tc">$velocityCount</td>
				<td style="width:30px" class="oabbs_tc">
					<input type="checkbox" id="keyId" name="keyId" value="$!globals.get($ls_row,0)" #if(!$LoginBean.getOperationMap().get("/GradeAction.do").delete()) disabled #end />
				</td>
			    <td class="oabbs_tc">$!globals.get($ls_row,2)&nbsp;</td>
				<td class="oabbs_tc">$!globals.get($ls_row,1)&nbsp;</td>
				<td class="oabbs_tc">$!globals.get($ls_row,3)&nbsp;</td>
				<td class="oabbs_tc">$!globals.get($ls_row,4)&nbsp;</td>
				<td class="oabbs_tc">$!globals.get($ls_row,5)&nbsp;</td>
				<td class="oabbs_tc">$!globals.get($ls_row,6)$text.get("exam.lb.minute")&nbsp;</td>
				<td class="oabbs_tc">
					<a href="/GradeAction.do?operation=5&examId=$!globals.get($ls_row,0)&employeeId=$!globals.get($ls_row,7)">$text.get("exam.lb.testDetail")&nbsp;</a>
				</td>
			</tr>
		#end
		#if(!$listOldExam)
		 	<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td style="width:30px" class="oabbs_tc">&nbsp;</td>
				<td style="width:30px" class="oabbs_tc">&nbsp;</td>
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
		<div class="oalistRange_pagebar">
			<div class="pager_dv">
		 		$!pageBar
		 	</div>
		 </div>
	</form>
</body>
</html>
