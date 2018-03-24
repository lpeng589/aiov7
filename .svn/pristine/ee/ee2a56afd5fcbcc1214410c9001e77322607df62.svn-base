<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.calendar.calendarList")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>

<script language="javascript">
var nodeId='$!folderId';
function selectAll(boolvalue){
	var allCheckBoxs=document.getElementsByName("cb");
	for(var i=0;i<allCheckBoxs.length;i++){
		if(allCheckBoxs[i].type=="checkbox"){
			if(allCheckBoxs[i].disabled==false){
					allCheckBoxs[i].checked=boolvalue;
			}
		}
	}
}
function delCalendar(){
	if(sureDel('keyId')){ 
		form.operation.value=$globals.getOP('OP_DELETE'); 
		document.form.submit();	
	}
}
function singleMyCalendar(id){
	document.location = '/OAMyCalendar.do?operation=$globals.getOP("OP_DETAIL")&id='+id+"&keyWord=$globals.encode($!keyWord)&year=$year&month=$month&pageNo=$!pageParam.pageNo";;
}
function goGraphicsCalendar(){
	document.location = "OAMyCalendar.do";
}
function addCalendar(){
	document.location='/OAMyCalendar.do?operation=$globals.getOP("OP_ADD_PREPARE")&calendar_time=$calendar_time&keyWord=$globals.encode($!keyWord)&year=$year&month=$month&type2=1&pageNo=$!pageParam.pageNo';
}	
function upd(id){
	document.location="/OAMyCalendar.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&id="+id+"&keyWord=$globals.encode($!keyWord)&year=$year&month=$month&pageNo=$!pageParam.pageNo";
}
function del(id){
	if(confirm("$text.get("oa.common.sureDelete")")){
		document.location="/OAMyCalendar.do?operation=$globals.getOP("OP_DELETE")&keyId="+id;
	}
}
function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}
</script>
</head>

<body>
<form action="/OAMyCalendarList.do" name="form">
<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
	<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.calendar.calendarList")</div>
	<ul class="HeadingButton">
	#if($LoginBean.operationMap.get("/OAMyCalendar.do").add())
	<li><button type="button" onClick="addCalendar()" class="b2">$text.get("common.lb.add")</button></li>
	#end
	#if($LoginBean.operationMap.get("/OAMyCalendar.do").delete())
	<li><button name="button" type="button" onClick="delCalendar()" class="b2">$text.get("common.lb.del")</button></li>
	#end
	<li><button name="button" type="button" onClick="goGraphicsCalendar()" class="b4">$text.get("oa.calendar.gui")</button></li>
	</ul>
</div>
<div id="listRange_id">
	<input type="hidden" name="type" value="list" />	
	<input type="hidden" name="stat" value="$stat" id="stat"/>	
		<ul class="oalistRange_1">
		<li><span>$text.get("oa.calender.Calenderdate")：</span>
		  <input name="calendarDate" type="text" class="text" size="24" value="$!oaCalendarSearchForm.calendarDate" onClick="openInputDate(this);" onKeyDown="if(event.keyCode==13) openInputDate(this);">
			  $text.get("oa.common.to")：<input name="endDate" type="text" class="text" size="24" value="$!oaCalendarSearchForm.endDate" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="openInputDate(this);" >
		</li>
		<li><span></span>
		</li>
		<li><span></span></li>
		
		<li><span>$text.get("oa.common.keyWord")：</span>		
		<input type="text" id="keyWord" name="keyWord" value="$!oaCalendarSearchForm.keyWord">	
		</li>
		<li><button type="submit" name="button" class="b2">$text.get("common.lb.query")</button></li>
	</ul>
	<div class="scroll_function_small_a" id="conter">
	<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-96;
oDiv.style.height=sHeight+"px";
</script>

		<table width="90%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table">
		<thead>
			<tr>
				<td style="width:30px" class="oabbs_tc">
					<input type="checkbox" name="checkbox" value="checkbox" onClick="checkAll('keyId')" id="checkbox">
				</td>
			    <td width="*" class="oabbs_tc">$text.get("oa.calendar.calendarTitle")</td>
				<td width="12%" class="oabbs_tc">$text.get("scope.lb.tsscopeValue")</td>
				<td width="12%" class="oabbs_tc">$text.get("scope.lb.tescopeValue")</td>
				#if($LoginBean.operationMap.get("/OAMyCalendar.do").update() || $LoginBean.operationMap.get("/OAMyCalendar.do").delete())
			    <td width="10%" class="oabbs_tc">$text.get("oa.calendar.option")</td>
			    #end
			</tr>
		</thead>
		<tbody>
		 #foreach($vo in $list)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td class="oabbs_tc"><input type="checkbox" name="keyId" value="$vo.id"/></td>
			    <td class="oabbs_tl"><a href="javascript:singleMyCalendar('$vo.id')">$vo.calendarTitle</a></td>
			    <td class="oabbs_tc">$vo.calendarDate</td>
			    <td class="oabbs_tc">#if("$!vo.endDate" == "")&nbsp;#else $!vo.endDate#end</td>
			    #if($LoginBean.operationMap.get("/OAMyCalendar.do").update() || $LoginBean.operationMap.get("/OAMyCalendar.do").delete())
			    <td class="oabbs_tc">
			    #if($LoginBean.operationMap.get("/OAMyCalendar.do").update())
				<a href="javascript:upd('$vo.id')">$text.get("oa.common.upd") &nbsp;</a>
				#end
				#if($LoginBean.operationMap.get("/OAMyCalendar.do").delete())
				<a href="javascript:del('$vo.id')">$text.get("common.lb.del") &nbsp;</a>
				#end
				</td>
				#end
			</tr>
		#end
		  </tbody>
		</table>
</div>
<div class="listRange_pagebar">
						$!pageBar
					</div>
</div>
	</form>
</body>
</html>
