<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<title>$text.get("common.lb.detail")</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="/style/css/WorkingPlan.css" />
<link rel="stylesheet" href="/js/ui/jquery-ui-1.8.18.custom.css" type="text/css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script language="javascript" src="$globals.js("/js/workPlan.vjs","",$text)"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script src="/js/jquery.js" type="text/javascript"></script> 
<script src="/js/function.js" type="text/javascript"></script> 
<script type="text/javascript" src="/js/ui/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript">
var varLoginId;
var varPlanId;
var varId;
var varIsEmployee;
var varLinkaddress;
var varObj;
var varPlanType ;
var varDisplayName ;
function alertSet(relationId){
	var urls="";
	var typestr="";
	var title="";
	var date=new Date();
	#if("$!planType" != "event" && "$!keyId" == "" && "$!score" == "")
		urls=encodeURIComponent('/OAWorkPlanAction.do?operation=4&userId=$!strUserId&planType=$!planType&strDate=$!strDate');
		typestr=encodeURIComponent('$text.get("com.oa.plan.alert")');
		title=encodeURIComponent('$text.get("com.oa.plan.name")');
	#else
		urls=encodeURIComponent('/OAWorkPlanAction.do?operation=4&planType=event&keyId=$!keyId&winCurIndex=$winCurIndex');
		typestr=encodeURIComponent('$text.get("com.oa.event.alert")');
		title=encodeURIComponent('$text.get("crm.event.plan")');
	#end
	var url = "/UtilServlet?operation=alertDetail&relationId="+relationId+"&falg=true"+"&title="+title+"&typestr="+typestr+"&urls="+urls+"&date="+date;
	asyncbox.open({
		id : 'warmsetting',
　　　   	url : url,
	 	title : '$text.get("workflow.msg.warmsetting")',
　　 　	width : 560,
　　 　	height : 460,
	    callback : function(action,opener){
		    if(action == 'ok'){ 
		    	opener.checkAlertSet();
			}
　　      }
 　 });
}


function refreshIframe(){
	jQuery.close('warmsetting');
}


function closecommitSet(){
	document.getElementById("commitSet").style.display="none";
}
function commit(planId,commitType,planEmployeeId,planTitle){
	document.commitform.commitType.value= commitType;
	document.commitform.planId.value= planId;
	document.commitform.planEmployeeId.value= planEmployeeId;
	document.commitform.planTitle.value= planTitle;
	document.getElementById("commitSet").style.display="block";
	document.commitform.commit.focus();
}
function commitSet(){
	if(document.commitform.commit.value == ""){
		alert("$text.get("oa.workplan.comment")$text.get("common.msg.fileFlow.NoContent")");
		return;
	}
	if (getStringLength(document.commitform.commit.value)>1000)
	{
		alert("$text.get("oa.workplan.comment")$!text.get('common.validate.error.longer')1000$!text.get('common.excl')\n $!text.get('common.charDesc')")
		focusItem(element);
		return false;
	}
	document.commitform.submit();
}

function copy(id){
	document.copyform.planId.value = id;
	document.getElementById("copySet").style.display="block";
}
function closecopySet(){
	document.getElementById("copySet").style.display="none";
}
function copySet(){
	if(document.copyform.strDate.value == ""){
		alert("$text.get("oa.calendar.date")$text.get("common.validate.error.null")");
		return;
	}
	url = "/OAWorkPlanAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&userId=$!LoginBean.id&oldUserId=$strUserId&oldPlanType=$planType&oldStrDate=$strDate";
	url += "&keyId=$!keyId&winCurIndex=$!winCurIndex&planId="+document.copyform.planId.value+"&planType="+document.copyform.planType.value+
		"&strDate="+document.copyform.strDate.value+"&score=$!score&planStatus=$!planStatus";
	window.open(url);
	document.getElementById("copySet").style.display="none";
}

function resizeImg(){
	scrwidth = (document.body.clientWidth) - 430;
	for(var i=0; i<document.images.length; i++) {
     		var img = document.images[i];
     		if(img.width > scrwidth){
     			img.width = scrwidth;
     		}
     	}	
}



/*
	新客户弹出框
	popName:弹出框类型userGroup:职员
*/
function openClientPop(){
	var url ="/Accredit.do?inputType=checkbox&popname=CrmClickGroup";
	asyncbox.open({
		id :'clientPopId',url:url,title:'选择客户',width:755,height:450,
		btnsbar :jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		var strArr = str.split("|");
	    		var retInfo = "";
	    		for(var i=0;i<strArr.length;i++){
	    			if("" != strArr[i].split(";")[0]){
		    			varLoginId = strArr[i].split(";")[1];
						varPlanId = '';
						varId = '5';
						varIsEmployee = '2';
						varLinkaddress = '/CRMClientAction.do?operation=5&type=detailNew&keyId=assID';
						varObj = 'ass$!{planrow.id}5';
						varDisplayName = '关联客户';
						exePopdiv(strArr[i]);
	    			}
	    		}
	    	}
	    }
　  });
}

/*双击回填字段*/
function fillData(datas){
	varLoginId = datas.split(";")[1];
	varPlanId = '';
	varId = '5';
	varIsEmployee = '2';
	varLinkaddress = '/CRMClientAction.do?operation=5&type=detailNew&keyId=assID';
	varObj = 'ass$!{planrow.id}5';
	varDisplayName = '关联客户';
	var strArr = datas.split(";")[0]+";"+datas.split(";")[1];
	exePopdiv(strArr);
	
	jQuery.close('clientPopId');
}


</script>
</head>

<body onLoad="resizeImg();">
<form name="copyform" method="post"  action="/OAWorkPlanAction.do"  >
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD_PREPARE")"/>
<input type="hidden" name="userId" value="$!LoginBean.id" /> 
<input type="hidden" name="oldUserId" value="$strUserId" />
<input type="hidden" name="oldPlanType" value="$planType" />
<input type="hidden" name="oldStrDate" value="$strDate" />
<input type="hidden" name="keyId" value="$!keyId" />
<input type="hidden" name="planId" value="" />
<input type="hidden" name="score" value='$!score'/>
<input type="hidden" name="planStatus" value='$!planStatus'/>
<input type="hidden" name="winCurIndex" value='$!winCurIndex'/>
<div class="tips_div" id="copySet" style="display:none;  cursor:default; position:absolute; left:300px; top:150px;height:170px">
   	<div class="tips_title">
	     <div class="left_title">$text.get("common.lb.copy")</div>
	     <div class="tips_close"><a href="javascript:closecopySet(); " class="div_close"><img src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("common.lb.close")" border="0"/></a></div>
   	</div>   
   	<div class="tips_content" style="margin:10px 0px 10px 20px;">
	   	$text.get("customTable.lb.tableType")<select name="planType">
   	<option value="day">$text.get("oa.type.day")</option>
   	<option value="week">$text.get("oa.type.week")</option>
   	<option value="month">$text.get("oa.type.month")</option>
   	<option value="season">$text.get("oa.type.season")</option>
   	<option value="year">$text.get("oa.type.year")</option>
   	<option value="event">$text.get("crm.event.plan")</option>
   	</select><br/><br/>
	$text.get("oa.calendar.date")<input type="text" name="strDate" value="$!OADayWorkPlanSearchForm.endDate" maxlength="50" 
		onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN'});"/>
	
	</div>
   	<div class="tips_operate">
   	<button type="button" onClick="copySet()" style="width:70px;margin-right:20px">$text.get("common.lb.ok")</button>
	<button type="button" onClick="closecopySet()" style="width:70px;margin-right:20px">$text.get("common.lb.close")</button>
   	</div>
</div>
</form>
<form name="commitform" method="post"  action="/OAWorkPlanAction.do"  >
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")"/>
<input type="hidden" name="opType" value="commit"/>
<input type="hidden" name="planType" value="$planType" /> 
<input type="hidden" name="strUserId" value="$strUserId" /> 
<input type="hidden" name="commitType" value="" /> 
<input type="hidden" name="strDate" value="$strDate" /> 
<input type="hidden" name="planId" value="" />
<input type="hidden" name="keyId" value="$!keyId" />
<input type="hidden" name="planEmployeeId" value="" />
<input type="hidden" name="planTitle" value="" />
<input type="hidden" name="score" value='$!score'/>
<input type="hidden" name="planStatus" value='$!planStatus'/>
<input type="hidden" name="winCurIndex" value='$!winCurIndex'/>
<div class="tips_div" id="commitSet" style="display:none;  cursor:default; position:absolute; left:300px; top:150px;">
   	<div class="tips_title">
	     <div class="left_title">$text.get("oa.workplan.comment")</div>
	     <div class="tips_close"><a href="javascript:closecommitSet(); " class="div_close"><img src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("common.lb.close")" border="0"/></a></div>
   	</div>   
   	<div class="tips_content">
   	<textarea name = commit class="tips_textarea"></textarea>
	</div>
   	<div class="tips_operate">
   	<button type="button" onClick="commitSet()" style="width:70px;margin-right:20px">$text.get("common.lb.ok")</button>
	<button type="button" onClick="closecommitSet()" style="width:70px;margin-right:20px">$text.get("common.lb.close")</button>
   	</div>
</div>
</form>
<div style="margin:5px;overflow-x:hidden;overflow-y:auto;" id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");			
			var sHeight=document.documentElement.clientHeight-30;
			oDiv.style.height=sHeight+"px";
		</script>
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		#if("$!planType" != "event" && "$!keyId" == "" &&"$!score" == "")
		<!-- 显示日计划，周，月，季，年计划的快捷操作方式 -->
		<tr>
			<td class="dateShow" align="center" >
			<div class="WP_title" #if("$!planType" == "season" || "$!planType" == "year") style="height:30px" #else style="height:60px" #end>	
				<span><a style="#if("$!planType" == "day")color:#d39f01;#end float:left;"  href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=day&strDate=$strDate">$text.get("oa.type.day")$text.get("oa.bbs.plan")</a></span>
				<span style="margin-left:0px"><a style="#if("$!planType" == "week")color:#d39f01;#end float:left;" href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=week&strDate=$strDate">$text.get("oa.type.week")$text.get("oa.bbs.plan")</a></span>
				<span style="margin-left:0px"><a style="#if("$!planType" == "month")color:#d39f01;#end float:left;" href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=month&strDate=$strDate">$text.get("oa.type.month")$text.get("oa.bbs.plan")</a></span>
				<span style="margin-left:0px"><a style="#if("$!planType" == "season")color:#d39f01;#end float:left;" href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=season&strDate=$strDate">$text.get("oa.type.season")$text.get("oa.bbs.plan")</a></span>
				<span style="margin-left:0px"><a style="#if("$!planType" == "year")color:#d39f01;#end float:left;" href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=year&strDate=$strDate">$text.get("oa.type.year")$text.get("oa.bbs.plan")</a></span>				
				<span style="margin-left:100px;">
					#if("$!planType" == "day" || "$!planType" == "week")
						$strDate.substring(0,7)$text.get("oa.type.month")
					#elseif("$!planType" == "season")
					<span style="margin:0px"><a style="margin:0px" href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$preMD"><img style="border:none;padding:8px 0px 0px 0px;margin:0px" src="/$globals.getStylePath()/images/thread-prev.png"/></a></span>
					<span style="margin:0px">$strDate.substring(0,4)$text.get("oa.type.year")${season}$text.get("oa.type.season")</span>
					<span style="margin:0px"><a style="margin:0px" href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$nextMD"><img style="border:none;padding:8px 0px 0px 0px;margin:0px" src="/$globals.getStylePath()/images/thread-next.png"/></a></span>
					#elseif("$!planType" == "year")	
					<span style="margin:0px"><a style="margin:0px" href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$preMD"><img style="border:none;padding:8px 0px 0px 0px;margin:0px" src="/$globals.getStylePath()/images/thread-prev.png"/></a></span>
					<span style="margin:0px">$strDate.substring(0,4)$text.get("oa.type.year")</span>
					<span style="margin:0px"><a style="margin:0px" href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$nextMD"><img style="border:none;padding:8px 0px 0px 0px;margin:0px" src="/$globals.getStylePath()/images/thread-next.png"/></a></span>
					#else
						$strDate.substring(0,4)$text.get("oa.type.year")
					#end
					
				</span>
				
			<table cellpadding="0" cellspacing="0" border="0" width="100%">
			<tr style="height:25px;cursor:hand"> 
				#if("$!planType" == "day") 
					<td style="border:1px solid #C3C3C3;width:15px"  onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$preMD'"><img style="border:none;padding:0px;margin:0px" src="/$globals.getStylePath()/images/thread-prev.png"/></td>
					#foreach($d in [1..$lastD])
						#set($dds = $d+"")
						#if($d < 10)#set($dds = "0"+$d) #end
						#if($d == $strDateInt )
						<td style="border:1px solid #C3C3C3;background-color:#fcbe00" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate.substring(0,8)$dds'">$d</td>
						#elseif($d == $curDate ) 
						<td style="border:1px solid #C3C3C3;background-color:#339933" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate.substring(0,8)$dds'">$d</td>
						#else
						<td style="border:1px solid #C3C3C3" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate.substring(0,8)$dds'">$d</td>
						#end
					#end
					<td style="border:1px solid #C3C3C3;width:15px"  onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$nextMD'"><img  style="border:none;padding:0px;margin:0px" src="/$globals.getStylePath()/images/thread-next.png"/></td>
				#elseif("$!planType" == "week")
					<td style="border:1px solid #C3C3C3;width:15px"  onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$preMD'"><img style="border:none;padding:0px;margin:0px" src="/$globals.getStylePath()/images/thread-prev.png"/></td>
					#foreach($d in $weekList)
						#if($!globals.get($d,2) == $strDateMonth )
						<td style="border:1px solid #C3C3C3;background-color:#fcbe00" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$!globals.get($d,1)'">$!globals.get($d,0)</td>
						#elseif($!globals.get($d,2) == $curMonth ) 
						<td style="border:1px solid #C3C3C3;background-color:#339933" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$!globals.get($d,1)'">$!globals.get($d,0)</td>
						#else
						<td style="border:1px solid #C3C3C3" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$!globals.get($d,1)'">$!globals.get($d,0)</td>
						#end
					#end
					<td style="border:1px solid #C3C3C3;width:15px"  onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$nextMD'"><img  style="border:none;padding:0px;margin:0px" src="/$globals.getStylePath()/images/thread-next.png"/></td>
				#elseif("$!planType" == "month")
					<td style="border:1px solid #C3C3C3;width:15px"  onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$preMD'"><img style="border:none;padding:0px;margin:0px" src="/$globals.getStylePath()/images/thread-prev.png"/></td>
					#foreach($d in [1..12])
						#set($dds = $d+"")
						#if($d < 10)#set($dds = "0"+$d) #end
						#set($cm = $!strDate.substring(0,5)+$d)
						#if($cm == $strDateMonth )
						<td style="border:1px solid #C3C3C3;background-color:#fcbe00" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$!strDate.substring(0,5)$!{dds}-01'">$d</td>
						#elseif($cm == $curMonth ) 
						<td style="border:1px solid #C3C3C3;background-color:#339933" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$!strDate.substring(0,5)$!{dds}-01'">$d</td>
						#else
						<td style="border:1px solid #C3C3C3" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$!strDate.substring(0,5)$!{dds}-01'">$d</td>
						#end
					#end
					<td style="border:1px solid #C3C3C3;width:15px"  onclick="window.location.href='/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$nextMD'"><img  style="border:none;padding:0px;margin:0px" src="/$globals.getStylePath()/images/thread-next.png"/></td>
				#end
			</tr>
			</table>
			</div>	
			</td>
			<td class="WP_addL">&nbsp;</td>
		</tr>
		
		#end
		<tr>
			<td class="dateShow" align="right" height="20px;">
			#if("$!planType" != "event" && "$!keyId" == "")
			<span style="float:left;padding-left:10px;">
				<span ><a #if("$!associateId" =="") style="color:#d39f01" #end 
					href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate&keyId=$!keyId">[$!strUserName$text.get("oa.bbs.plans")]</a></span>
			#foreach($row in $associate)	
				#if( "$!row.isEmployee" == "1")
					 <span><a #if("$!associateId" =="$row.id") style="color:#d39f01" #end 
					 href="/OAWorkPlanAction.do?operation=4&userId=$strUserId&planType=$planType&strDate=$strDate&associateId=$row.id&keyId=$!keyId">[${row.name}$text.get("oa.bbs.plan")]</a></span>
				#end
			#end
			#end
			</span>
			&nbsp; 
			#if($planType == "event") 
			<a href="/OAWorkPlanQueryAction.do?operation=4&opType=eventWorkList&winCurIndex=$winCurIndex">[$text.get("common.lb.back")]</a>
			#elseif($planType == "week") 
			[<a style="margin:0px;padding:0px" href="javascript:opencalWindow('/OAWorkPlanAction.do?operation=4&userId=$!strUserId&planType=day&strDate=$!strDate&score=week');">$text.get("workplan.lb.weekall")</a>|
			 <a style="margin:0px;padding:0px" href="javascript:opencalWindow('/OAWorkPlanAction.do?operation=4&userId=$!strUserId&planType=day&strDate=$!strDate&score=week&planStatus=0');">$text.get("oa.bbs.notFinish")</a>]
			#elseif($planType == "month") 
			[<a style="margin:0px;padding:0px" href="javascript:opencalWindow('/OAWorkPlanAction.do?operation=4&userId=$!strUserId&planType=day&strDate=$!strDate&score=month')">$text.get("workplan.lb.monthall")</a>|
			 <a style="margin:0px;padding:0px" href="javascript:opencalWindow('/OAWorkPlanAction.do?operation=4&userId=$!strUserId&planType=day&strDate=$!strDate&score=month&planStatus=0')">$text.get("oa.bbs.notFinish")</a>]
			#end
			#if($planType == "day" && "$!keyId" =="")
			<a href="javascript:opencalWindow('/OAWorkPlanAction.do?operation=4&userId=$!strUserId&planType=day&strDate=$!strDate&opType=calendar')">[$text.get("workplan.lb.calenview")]</a>
			#end
			</td>
			<td class="WP_addL">&nbsp;</td>
		</tr>
	</table> 
#if("$!result.size()" == "0" && "$!planType" !="event" && "$!associateId" == "" && "$strUserId"=="$LoginBean.id" && "$!score" =="")
	<table cellpadding="0" cellspacing="0" border="0" width="100%">		
		<tr>
			<td class="WP_addC">
				<div class="WP_title"><a href="/OAWorkPlanAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&userId=$strUserId&planType=$planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus"  title="$text.get("common.lb.add")"><span style="padding:0px 10px 5px 0px">$text.get("workplan.msg.notplan")</span><img src="style/plan/M_3.gif" /></a>	</div>	
			</td>	
			<td class="WP_addL">&nbsp;</td>
		</tr>
	</table>		
#end		
#foreach($planrow in $result)
	<table cellpadding="0" cellspacing="0" border="0" width="100%">		
		<tr>
			<td class="WP_addC">
				<div class="WP_title">
					<span style="float:left;overflow-y:hidden;height:30px;white-space:nowrap;margin-left:0px;">								
					#if($planType == "day") 
					<span style="float:left;">$!employeeMap.get($planrow.employeeID)$planrow.beginDate.substring(0,10)$text.get("oa.type.day")[</span>
					<span style="white-space:nowrap;height:30px;float:left;margin-left:5px;" title="$planrow.title">#if($!planrow.title.length()>20) $!planrow.title.substring(0,20)... #else $!planrow.title #end</span>
					<span style="float:left;margin-left:5px;">]</span>
					<span class="planGrade$planrow.grade" style="float:left;margin-left:5px;"> [$globals.getEnumerationItemsDisplay("planGrade",$planrow.grade)] </span>
					<span style="float:left;margin-left:5px;">[$planrow.beginDate.substring(11,16) - $planrow.endDate.substring(11,16)] 
					#if("$planrow.statusId" == "0") [$text.get("oa.bbs.notFinish")] #else [$text.get("oa.bbs.finished")][$text.get("common.lb.useTime"):$!planrow.time$text.get("common.msg.minute")] #end
					</span> 
					#elseif($planType == "event") 
					<span style="float:left;">$!employeeMap.get($planrow.employeeID)&nbsp;[</span>
					<span style="white-space:nowrap;height:30px;float:left;margin-left:5px;" title="$planrow.title">#if($!planrow.title.length()>20) $!planrow.title.substring(0,20)... #else $!planrow.title #end</span>
					<span style="float:left;margin-left:5px;">]</span>
					<span class="planGrade$planrow.grade" style="float:left;margin-left:0px;">&nbsp;[$globals.getEnumerationItemsDisplay("planGrade",$planrow.grade)] </span>
					<span style="float:left;">[$planrow.beginDate - $planrow.endDate] 
					#if("$planrow.statusId" == "0") [$text.get("oa.bbs.notFinish")] #else [$text.get("oa.bbs.finished")]#end
					</span> 
					#else
					<span>$!employeeMap.get($planrow.employeeID)$planrow.title </span>
					#end
					
					</span>
					<span style="float:right;width:160px">
				<!--  	<a href="#" title="$text.get("oa.common.invitereading")"><img src="style/plan/M_4.gif" /></a>	-->	
					#if($planrow.employeeID==$LoginBean.id)								
					<a href="javascript:alertSet('$planrow.id')" title="$text.get("workflow.msg.warmsetting")"><img src="style/plan/time.gif" /></a>
					#end
					<!-- <a href="javascript:copy('$planrow.id')" title="$text.get("common.lb.copy")"><img src="style/plan/M_5.gif" /></a> -->
					<a href="javascript:commit('$planrow.id','0','$planrow.employeeID','$planrow.title')" title="$text.get("oa.workplan.comment")"><img src="style/plan/M_0.gif" /></a>
					#if($planrow.employeeID==$LoginBean.id)
					#if($planType=="day")
					<a href="javascript:if(confirm('$text.get("common.msg.confirmDel")')){window.location.href='/OAWorkPlanAction.do?operation=$globals.getOP("OP_DELETE")&keyId=$planrow.id&userId=$strUserId&planType=day&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus';}"  title="$text.get("common.lb.del")"><img src="style/plan/M_2.gif"/></a>
					<a href="/OAWorkPlanAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&planSum=plan&keyId=$!keyId&planId=$planrow.id&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus" title="$text.get("common.lb.update")"><img src="style/plan/M_1.gif" /></a>					
					<a href="/OAWorkPlanAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&userId=$strUserId&planType=day&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus"  title="$text.get("common.lb.add")"><img src="style/plan/M_3.gif" /></a>
					#else
					<a href="/OAWorkPlanAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&planSum=plan&keyId=$!keyId&planId=$planrow.id&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus" title="$text.get("common.lb.update")"><img src="style/plan/M_1.gif" /></a>					
					#end#end
					</span>
				</div>
				<div style="margin:40px 10px 10px 10px">
			#if($planrow.attach != "")
				$text.get("upload.lb.affix")：









			#foreach ($str in $globals.strSplit($planrow.attach,';'))
				 <a href="/ReadFile?tempFile=path&path=/plan/&fileName=$!globals.encode($str)" target="_blank"><span><img src="/$globals.getStylePath()/images/zip.gif"></span>&nbsp;$str</a>&nbsp;&nbsp;&nbsp;
			#end
			<br/><br/>
			#end
			
				$planrow.content
				</div>
				<ul  class="WP_addCul">
					
					<li class="WP_addC_bg WP_addMsg">
						<ul>
						#foreach($comm in $commits)
						#if("$globals.get($comm,2)"=="0" && "$globals.get($comm,1)"==$planrow.id)
							<li>
								<p>$text.get("oa.workplan.comment"):
								#if($globals.get($comm,4) == $LoginBean.id)
								<a href="javascript:if(confirm('$text.get("common.msg.confirmDel")')){window.location.href='/OAWorkPlanAction.do?operation=$globals.getOP("OP_DELETE")&commId=$globals.get($comm,0)&keyId=$!keyId&delType=commit&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&adUserId=$planrow.employeeID&planId=$planrow.id&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus';}" title="$text.get("common.lb.del")"><img src="/$globals.getStylePath()/images/delete_button.gif"  border="0"/></a>#end<br/>
								$globals.encodeHTML($globals.get($comm,3))</p>
								<div>$globals.get($comm,5)
								#if($globals.get($comm,4)!=$LoginBean.id)<a href="javascript:msgComm('$globals.get($comm,4)','$globals.get($comm,6)')"><img src="style/plan/msg_icon.gif" /></a>#end $globals.get($comm,6)</div>
							</li>
						#end#end	
						</ul>
					</li>
				</ul>
				<div class="WP_title">
					<span >$text.get("oa.planSummar.add") &nbsp;#if($!planrow.summaryTime != "")($text.get("oa.workplan.summar.time")：$!planrow.summaryTime)#end</span>					
					#if($!planrow.summaryTime != "") 
					<a href="javascript:commit('$planrow.id','1','$planrow.employeeID','$planrow.title')" title="$text.get("oa.workplan.comment")"><img src="style/plan/M_0.gif" /></a>
					#end 
					#if($!planrow.employeeID == "$LoginBean.id") 
					<a href="/OAWorkPlanAction.do?operation=$globals.getOP("OP_ADD")&opType=summaryPrepare&planSum=plan&keyId=$!keyId&planId=$planrow.id&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus" title="$text.get("oa.planSummar.add")"><img src="style/plan/M_1.gif" /></a>
					#end
				</div>
				<div style="margin:40px 10px 10px 10px">
				$!planrow.summary
				</div>
				<ul  class="WP_addCul">					
					<li class="WP_addC_bg WP_addMsg">
						<ul>
						#foreach($comm in $commits)
						#if("$globals.get($comm,2)"=="1" && "$globals.get($comm,1)"==$planrow.id)
							<li>
								<p>$text.get("oa.workplan.comment"):
								#if($globals.get($comm,4) == $LoginBean.id)
								<a href="/OAWorkPlanAction.do?operation=$globals.getOP("OP_DELETE")&commId=$globals.get($comm,0)&keyId=$!keyId&delType=commit&userId=$strUserId&planType=$planrow.planType&strDate=$strDate&adUserId=$planrow.employeeID&planId=$planrow.id&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus" title="$text.get("common.lb.del")"><img src="/$globals.getStylePath()/images/delete_button.gif"  border="0"/></a>#end<br/>
								$globals.encodeHTML($globals.get($comm,3))</p>
								<div>$globals.get($comm,5)
								#if($globals.get($comm,4)!=$LoginBean.id)<a href="javascript:msgComm('$globals.get($comm,4)','$globals.get($comm,5)')"><img src="style/plan/msg_icon.gif" /></a>#end $globals.get($comm,6)</div>
							</li>
						#end#end	
						</ul>
					</li>
				</ul>
			</td>
			<td class="WP_addL">
			#foreach($row in $associate)
				#if("$row.name" != "联系人")
				<div class="WP_title">
					<span>$row.name</span>
					#if("$row.name" == "关联客户")
						<a href="javascript:openClientPop();"><img src="style/plan/jh_icon.gif" /></a>
					#else
						<a href="javascript:openPop('$!LoginBean.id','$planrow.id','$row.popSelect','$row.name','$row.id','$row.isEmployee','$row.linkAddress','ass$planrow.id$row.id','$planrow.planType')"><img src="style/plan/jh_icon.gif" /></a>
					#end
				</div>
				<ul id="ass$planrow.id$row.id">
					#foreach($item in $assItem) 
					#if($globals.get($item,1) == $row.id && $globals.get($item,4)==$planrow.id)
				<li id="${planrow.id}_${row.id}_$globals.get($item,2)"> 
					#if("$!planrow.employeeID"!="$LoginBean.id")
					<a ></a>
					#else
					<a href="javascript:delAssoicate('$planrow.id','${row.id}','$globals.get($item,2)')"><img src="style/plan/del_icon.gif" /></a>
					#end
					<span onclick="openLink('$globals.get($item,2)','$row.linkAddress')">#if($globals.get($item,3).length() >10) $globals.get($item,3).substring(0,10) #else $globals.get($item,3) #end</span>
					#if($row.isEmployee=="1" and $globals.get($item,2)!=$LoginBean.id)<a href="javascript:msgComm('$globals.get($item,2)','$globals.get($item,3)')"><img src="style/plan/msg_icon.gif" /></a>#end
					<input type="hidden" name="assoicate" value="${row.id}:$globals.get($item,2):$globals.get($item,3)"/></li>	
					 #end#end
				</ul>
				#end
			#end	
			</td>
		</tr>
	</table>
#end	
</div>
</body>
</html>