<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.job.jobodd")</title>
<link type="text/css" href="/$globals.getStylePath()/css/classFunction.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript">

$(function(){
	$("#conter").height($(window).height()-115);
});

function openSelect1(urlstr,displayName,obj,field){
	displayName=encodeURI(displayName) ;
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)!="undefined"){
	var mutli=str.split("|"); 
	hid="";
	dis="";
	if(str.length>0){
		var len=mutli.length;
		if(len>1){len=len-1}
		for(j=0;j<len;j++){ 
			fs=mutli[j].split(";");
				dis=fs[1];
				hid=fs[0];
			if(hid.indexOf("@Sess:")>=0){
				document.getElementById(obj).value="";
				document.getElementById(field).value="";
			}else{
			   if(isexits(dis,document.getElementById(obj).value)==true){
				document.getElementById(obj).value=dis;
				document.getElementById(field).value=hid;
				}
			}
		}
	}else{
		document.getElementById(obj).value="";
		document.getElementById(field).value="";
	}
	}
}
function isexits(obj,array)
{
			var fs=array.split(";");
			for(i=0;i<fs.length;i++)
			{
			if((fs[i]+";")==(obj))
			{
			return false;
			}
			}
			return true;
			
}
function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}
/*
function openSelect2(urlstr,displayName,obj,field){
	displayName=encodeURI(displayName) ;
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	var mutli=str.split(";"); 
	document.getElementById(obj).value=mutli[0];
	document.getElementById(field).value=mutli[1];
}
*/
function openSelect2(urlstr,displayName,obj,field){
	displayName=encodeURI(displayName) ;
		var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
			if(typeof(str)!="undefined"){
		if(str.indexOf("@Sess:")>=0){
			document.getElementById(obj).value="";
			document.getElementById(field).value="";
			return ;
		}
		fs=str.split(";");
		var result="";
		var result2="";
		for(var i=0;i<fs.length;i++){
			if(i%2!=0){
				result=fs[i];
			}else{
				if(fs[i].length>1&&fs[i].indexOf("|")==0){
					fs[i]=fs[i].substr(1);
				}
				if(fs[i].length>=1){
					result2=fs[i];
				}
			}
		}	  
		document.getElementById(obj).value=result2;
		document.getElementById(field).value=result;	
	}

}

</script>
	</head>
	<body onLoad="showtable('tblSort');showStatus(); ">
		<iframe name="formFrame" style="display:none"></iframe>
		<form method="post" scope="request" name="form" action='/OAJob.do'>
			<input type="hidden" name="winCurIndex" value="$!winCurIndex">
			<input type="hidden" name="operation" value="" />
			<div class="Heading">
				<div class="HeadingTitle">
					$text.get("oa.job.jobodd")
				</div>
				<ul class="HeadingButton">
					<li><input type="hidden" name="pageParam" value="" />
						<button name="Submits" type="submit" class="hBtns">$text.get("common.lb.query")</button>
					</li>
					#if($$LoginBean.getOperationMap().get("/OAJob.do").add())
					<li>
						<button type="button" onClick="location.href='/OAJob.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex '" class="hBtns">$text.get("common.lb.add")</button>
					</li>
					#end
					#if($$LoginBean.getOperationMap().get("/OAJob.do").delete())
					<li>
						<button type="submit" onClick="javascript:return sureDel('keyId');" class="hBtns">$text.get("common.lb.del")</button>
					</li>
					#end 
				</ul>
			</div>
			<div id="listRange_id">
				<ul class="listRange_1">
					<li>
						<span>$text.get("role.lb.createBy")：</span>
						<input name="createPerson" type="hidden" value="$!createPerson">
						<input name="createPersonName" type="text" value="$!createPersonName" onDblClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=MsgReceive2','$text.get('role.lb.createBy')','createPersonName','createPerson');"><img id="job_cy" style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=MsgReceive2','$text.get('role.lb.createBy')','createPersonName','createPerson');" class="search">
					</li>
					<li>
						<span>$text.get("oa.subjects")：</span>
						<input name="jobtheme" type="text" value="$!jobtheme">
						</li>
					<li style="width:335px">
						<span>$text.get("oa.mydata.creatTime")：</span>
						<input name="jobBeginTime" value="$!jobBeginTime"
							onKeyDown="if(event.keyCode==13) openInputDate(this);"
							onClick="openInputDate(this);">
						$text.get("oa.common.to")
						<input name="jobEndTime" value="$!jobEndTime"
							onKeyDown="if(event.keyCode==13) openInputDate(this);"
							onClick="openInputDate(this);">
					</li>
					<li>
						<span>$text.get("common.msg.auditStatus")：</span>
							<select  name="auditStatus">
#foreach($erow in $globals.getEnumerationItems("WFQueyCondition"))
								<option title="$erow.name" value="$erow.value" #if($erow.value == $!auditStatus) selected #end>$erow.name</option>
#end
							</select>
						</li>
				</ul>
				<div class="scroll_function_small_a" id="conter">

					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="listRange_list_function" id="tblSort" name="table">
						<thead>
							<tr>
								<td width="30" class="oabbs_tc">&nbsp;
								</td>
								<td width="35" class="oabbs_tc">
									<input type="checkbox" name="selAll"
										onClick="checkAll('keyId')">
								</td>
								<td width="*" class="oabbs_tl">
									$text.get("oa.subjects")
								</td>
								<td width="80" class="oabbs_tc">
									$text.get("oa.mydata.createBy")
								</td>
								<td width="150" class="oabbs_tc">
									$text.get("oa.mydata.creatTime")
								</td>
								<td width="150" class="oabbs_tc">
									$text.get("common.lb.lastRevertTime")
								</td>
								<td width="100" class="oabbs_tc">
                                  $text.get("check.lb.status")
								</td>
							
								<td width="100" class="oabbs_tc">
									$text.get("common.lb.operation")
								</td>
							</tr>
						</thead>
						#foreach($row in $result)
						<tbody>
							<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
								<td width="30" class="oabbs_tc">$velocityCount
								</td>
								<td width="30" class="oabbs_tc">
									<!-- #if("$!LoginBean.empFullName"!="$!row.createPerson")
									&nbsp;
									#end
									#if("$!LoginBean.empFullName"=="$!row.createPerson")
									<input type="checkbox" name="keyId" value="$row.id">
									#end -->
									#if("$!row.state" != "finish" || "$!LoginBean.id" == "1")
										<input type="checkbox" name="keyId" value="$row.id">
									#else
										&nbsp;
									#end
								</td>
								<td width="394">
									<a
										href='/OAJob.do?operation=$globals.getOP("OP_DETAIL")&keyId=$!row.id&winCurIndex=$!winCurIndex&createPerson=$!createPerson&createPersonName=$globals.encode($!createPersonName)&jobtheme=$globals.encode($!jobtheme)&jobBeginTime=$!jobBeginTime&jobEndTime=$!jobEndTime'>$!row.jobtheme&nbsp;</a> 
								</td>
								<td width="100" class="oabbs_tc">
									$!row.createPerson&nbsp;
								</td>
								<td width="100" class="oabbs_tc">
									$!row.createTime&nbsp;    
								</td>
								<td width="100" class="oabbs_tc">
									$!row.lastUpdateTime&nbsp;    
								</td>
								<td width="100" style=" margin-left:10px;">
									&nbsp;&nbsp;&nbsp;
									#foreach($erow in $globals.getEnumerationItems("WFQueyCondition"))
										 #if($erow.value == $!row.state)
										 	$erow.name
										 #end
									#end
								</td>
								<td width="100" class="oabbs_tc">
									<a
										href='/OAJob.do?operation=$globals.getOP("OP_DETAIL")&keyId=$!row.id&winCurIndex=$!winCurIndex&createPerson=$!createPerson&createPersonName=$globals.encode($!createPersonName)&jobtheme=$globals.encode($!jobtheme)&jobBeginTime=$!jobBeginTime&jobEndTime=$!jobEndTime'>$!text.get("sms.note.detail")</a>
								#if("$!LoginBean.empFullName"=="$!row.createPerson" && $!row.state!="finish")
									#if($$LoginBean.getOperationMap().get("/OAJob.do").update())
										<a href='/OAJob.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$!row.id&winCurIndex=$!winCurIndex&createPerson=$!createPerson&createPersonName=$globals.encode($!createPersonName)&jobtheme=$globals.encode($!jobtheme)&jobBeginTime=$!jobBeginTime&jobEndTime=$!jobEndTime'>$!text.get("mrp.lb.update")</a>
									#else
										&nbsp;
									#end
								#end
								&nbsp;
								</td>
							</tr>
						</tbody>
						#end
					</table>
				</div>
					<div class="page-wp">
						<div class="listRange_pagebar">
							$!pageBar
						</div>
					</div>
				</div>
		</form>
	</body>
</html>
