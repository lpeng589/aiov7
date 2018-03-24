<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link type="text/css" rel="stylesheet" href="style/css/WorkingPlan.css" />
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="$globals.js("/js/workPlan.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script src="/js/jquery.js" type="text/javascript"></script> 
<script language="javascript">

putValidateItem("time","$text.get("common.lb.useTime")","int","",true,0,2000000);

function isNull(variable,message)
{
	if(variable=="" || variable==null)
	{
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else
	{
		return true;
	}
			
}
function beforSubmit(form){
 
	var content= editor.html();
  	document.form.summary.value = content ;
	if(!isNull(content,'$text.get("common.lb.content")'))	{
		return false;
	}
	if(document.form.statusId.value == "1" && !isNull(document.form.time.value,'$text.get("common.lb.useTime")'))	{
		return false;
	}
	if(!validate(form))return false;
	disableForm(form);
	return true;
}	

var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="summary"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});
</script>

</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form name="form" method="post"  action="/OAWorkPlanAction.do"  onSubmit="return beforSubmit(this);" target="formFrame">
	
	<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
	<input type="hidden" name="noback" value="$!noback">
	<input type="hidden" name="planType" value="$!planType">
	<input type="hidden" name="id" value="$!result.id">
	<input type="hidden" name="strDate" value='$!strDate'>
	<input type="hidden" name="employeeID" value='$!result.employeeID'>
	<input type="hidden" name="departmentCode" value='$!result.departmentCode'>
	<input type="hidden"  name="beginDate" value="$!result.beginDate"/>
	<input type="hidden"  name="endDate" value="$!result.endDate"/>
	<input type="hidden" name="winCurIndex" value='$!winCurIndex'>
	<input type="hidden" name="opType" value='summary'>
	<input type="hidden" name="score" value='$!score'>
	<input type="hidden" name="planStatus" value='$!planStatus'>
	<input type="hidden" name="keyId" value="$!keyId">
	<input type="hidden" name="attachFiles" value="$!result.attach">
	<input type="hidden" name="delFiles" value="">
	
<div style="margin:5px;overflow-x:hidden;overflow-y:auto;" id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");			
			var sHeight=document.documentElement.clientHeight-20;
			oDiv.style.height=sHeight+"px";
		</script>
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<td colspan="2" class="dateShow" height="20px;">
			&nbsp;
			
			$text.get("common.msg.daysummary")
			</td>
		</tr>
		<tr>
			<td class="WP_addC">
				<div class="WP_title">					
					#if($planType == "day") 
					<span>$!employeeMap.get($result.employeeID)$strDate$text.get("oa.type.day")&nbsp;[$result.title] </span>
					<span class="planGrade$result.grade"> [$globals.getEnumerationItemsDisplay("planGrade",$result.grade)] </span>
					<span>[$result.beginDate.substring(11,16) - $result.endDate.substring(11,16)] 
					#if("$result.statusId" == "0") [$text.get("oa.bbs.notFinish")] #else [$text.get("oa.bbs.finished")][$text.get("common.lb.useTime"):$!planrow.time$text.get("common.msg.minute")] #end
					</span> 
					#elseif($planType == "event") 
					<span>$!employeeMap.get($result.employeeID)&nbsp;$result.title </span>
					<span class="planGrade$result.grade">[$globals.getEnumerationItemsDisplay("planGrade",$result.grade)] </span>
					<span>[$result.beginDate - $result.endDate] 
					#if("$result.statusId" == "0") [$text.get("oa.bbs.notFinish")] #else [$text.get("oa.bbs.finished")]#end
					</span> 
					#else
					<span>$!employeeMap.get($result.employeeID)$result.title </span>
					#end					
				
				</div>
				<div style="margin:40px 10px 10px 10px">
				$result.content
				</div>
				<ul>
					
					<li class="WP_addC_bg WP_addMsg">
						<ul>
						#foreach($comm in $commits)
						#if("$globals.get($comm,2)"=="0" && "$globals.get($comm,1)"==$result.id)
							<li>
								<p>$text.get("oa.workplan.comment"):
								<br/>
								$globals.encodeHTML($globals.get($comm,3))</p>
								<div>$globals.get($comm,5)
								<a href="javascript:msgComm('$globals.get($comm,4)')"><img src="style/plan/msg_icon.gif" /></a>$globals.get($comm,6)</div>
							</li>
						#end#end	
						</ul>
					</li>
				</ul>				
			</td>
			
			<td class="WP_addL" rowspan="2">
			#foreach($row in $associate)
				<div class="WP_title">
					<span>$row.name</span>
					<a href="javascript:openPop('$!LoginBean.id','','$row.popSelect','$row.name','$row.id','$row.isEmployee','$row.linkAddress','ass$result.id$row.id')"><img src="style/plan/jh_icon.gif" /></a>
				</div>
				<ul id="ass$result.id$row.id">
					#foreach($item in $assItem)
					#if($globals.get($item,1) == $row.id)
				<li id="${result.id}_${row.id}_$globals.get($item,2)">
					<a href="javascript:delAssoicate('$result.id','${row.id}','$globals.get($item,2)','true')"><img src="style/plan/del_icon.gif" /></a>
					<span onclick="openLink('$globals.get($item,2)','$row.linkAddress')">#if($globals.get($item,3).length() >15) $globals.get($item,3).substring(0,15) #else $globals.get($item,3) #end</span>
					#if($row.isEmployee=="1")<a href="javascript:msgComm('$globals.get($item,2)')"><img src="style/plan/msg_icon.gif" /></a>#end
					<input type="hidden" name="assoicate" value="${row.id}:$globals.get($item,2):$globals.get($item,3):0:0"/></li>			
				
					 #end#end
				</ul>
			#end	
			</td>
			<tr>
			<td class="WP_addC">
				#if($planType=="day")
				<div class="WP_title">
					<span>$text.get("common.lb.useTime"):<input type="text" name="time" style="width:80px"  #if("$!result.time"  != "" &&"$!result.time"  != "0") value="$!result.time" #end/>$text.get("common.msg.minute") &nbsp;
					$text.get("stat.statusId"):<select name=statusId>
					<option value="0" #if($result.statusId == "0") selected #end>$text.get("oa.bbs.notFinish")</option>
					<option value="1" #if($result.statusId == "1") selected #end>$text.get("oa.bbs.finished")</option>
					</select></span>
					
					#foreach($gitem in $goalTypeItem)
					<input type="hidden" name="goalTypeItem" value="$globals.get($gitem,0)"/>	
					<span>$!globals.get($gitem,2):<input type="text" name="gi_$!globals.get($gitem,0)" style="width:80px" value="$!globals.get($gitem,1)"/></span>
					#end
				</div>
				#elseif($planType=="event")
				<span> &nbsp;&nbsp;&nbsp;$text.get("stat.statusId"):<select name=statusId>
					<option value="0" #if($result.statusId == "0") selected #end>$text.get("oa.bbs.notFinish")</option>
					<option value="1" #if($result.statusId == "1") selected #end>$text.get("oa.bbs.finished")</option>
					</select></span>
				<input type="hidden" name="time" value="0"/>
				#else			
					<input type="hidden" name="time" value="0"/>		
				#end
				<div><textarea name="summary" style="width:100%;height:300px;visibility:hidden;">$!result.summary</textarea></div>
				<div style="float:left;width:300px;">$text.get("upload.lb.affixupload")：<button type=button class="b2" onClick="openAttachUpload('/plan/')">$text.get("oa.common.accessories")</button>
				<div id="files_preview" style="padding-bottom:20px">				  		  
			  	#foreach ($str in $globals.strSplit($!result.attach,';'))
					 <div  id ="$str" style ="height:18px; color:#ff0000;">
				  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
				  	 $str<input type=hidden name="attachFile" value="$str"/></div>	
				#end
		    	</div>
				</div>
				<div class="WP_addButton" style="float:right;width:100px;"><input type="submit" value="$text.get("common.lb.save")" />				
				
				<input type="button" value="$text.get("common.lb.back")" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&planType=$planType&userId=$result.employeeID&strDate=$strDate&keyId=$!keyId&winCurIndex=$winCurIndex&score=$!score&planStatus=$!planStatus';"/>
				
				</div>
			</td>
			</tr>
		</tr>
	</table>
</div>
</form>
</body>
</html>
