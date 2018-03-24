<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.careforaction.title.viewEvent")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript">
function openInputDate(obj,curdate)
{
	if(curdate ==""){
		c.showMoreDay = false;
		c.show(obj);
	}
}
function sub(actionType){
	document.getElementById('actionType').value = actionType;
	if('createPlan' == actionType){
		if(document.getElementById('startDate').value == ''){
			alert('$text.get("alertCenter.lb.startDate")$text.get("common.validate.error.null")');
			return false;
		}
	}
	form.submit();
	returnValue = true;
	setTimeout('closeTheWin()',500);
}
function closeTheWin(){
	window.parent.jQuery.close("CareforPopup");
}
</script>
</head>

<body style="overflow-x:hidden">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" action="/CareforExecuteAction.do" onSubmit="return val();" target="formFrame">
	<input type="hidden" name="operation" value="$globals.getOP('OP_UPDATE')">
	<input type="hidden" name="id" value="$del.id">
	<input type="hidden" name="noback" value="true">
	<input type="hidden" name="actionType" value="">
	
	
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("crm.careforaction.title.viewEvent")</div>
	<ul class="HeadingButton">
				#if($bean.actionType==0 && $del.status == 1 && $del.careforAction.status == 1)
				<button onClick="sub('action');">$text.get('crm.careforaction.lb.actionevent')</button>
				<button onClick="sub('overleap');">$text.get('crm.careforaction.lb.overleap')</button>
					#if("$del.hasPlan" == "0")				
					<button onClick="sub('createPlan');">$text.get('crm.careforaction.lb.createjobplan')</button>
					#end
				#end
				<button onClick="javascript:closeTheWin();">$text.get('com.lb.close')</button>
				</ul>
</div>
    <div style="height: auto; padding-top:3px; text-align:center; padding-left:5px;">
		<table border="0" width="100%"  cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<tbody>
			<tr>
				<td colspan="3" bgcolor="#D7EBFF"><strong>$text.get('crm.carfor.lb.event')&nbsp;</strong></td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="middle">$text.get('com.carefor.lb.event')</td>
				<td colspan="2">$del.eventName</td>
			</tr>
			<tr>
				<td  align="right" valign="middle" width="20%">$text.get('crm.careforaction.lb.executant')</td>
				<td colspan="2">$actor</td>
			</tr>
			<tr>
				<td   align="right" valign="middle" width="20%">$text.get('alertCenter.lb.startDate')</td>
				<td colspan="2"><input readonly="readonly" type="text" class="text2" name="startDate" value="$!del.startDate" onClick="openInputDate(this,'$!del.startDate');"></td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="middle">$text.get('alertCenter.lb.endDate')</td>
				<td colspan="2"><input readonly="readonly" type="text"  class="text2"  name="endDate" value="$!del.endDate" onClick="openInputDate(this,'$!del.endDate');"></td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="middle">$text.get('crm.carefor.lb.execdate')</td>
				<td colspan="2"><input readonly="readonly" type="text"  class="text2"  name="createTime" value="$!del.createTime" onClick="openInputDate(this,'$!del.createTime');"></td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="middle">$text.get('common.lb.status')</td>
				<td colspan="2">
						#if("$del.status" == "2")
							$text.get('common.msg.DEFAULT_SUCCESS')<img src='/$globals.getStylePath()/images/carefor/ok.gif'>
						#elseif("$del.status" == "3")
							$text.get('crm.careforaction.lb.over')<img src='/$globals.getStylePath()/images/carefor/up.gif'>
						#elseif("$del.status" == "4")
							$text.get('common.msg.DEFAULT_FAILURE')<img src='/$globals.getStylePath()/images/carefor/error.gif'>
						#else
							$text.get('crm.careforaction.lb.notrun')<img src='/$globals.getStylePath()/images/carefor/not.gif'>
						#end	
						#if("$!del.endDate"!="")  
							#set($timediff = $globals.getTimeDiff("$!del.createTime","$!del.endDate"))
							#if( $timediff> 0)
								<font color="red">($text.get('crm.careforaction.lb.overTime')$timediff $text.get('oa.calendar.day'))</font>
							#end
						#end
				</td>
			</tr>
			<tr>
				<td  height="5" colspan="3"  bgcolor="#D7EBFF"><strong>$text.get("crm.carefor.lb.execaction"):</strong></td>
		    </tr>
#if($bean.actionType==0)			
			<tr>
				<td width="20%" rowspan="2" align="right" valign="top" class="oabbs_tr">$text.get("crm.carefor.lb.actionconcretely")</td>				
			  	<td colspan="2">$text.get("crm.carefor.lb.manualexec")</td>
		    </tr>
		    <tr>
		      	<td colspan="2">$bean.eventContent</td>
		  	</tr>
			<tr>
		      	<td width="20%" align="right" valign="top" class="oabbs_tr">$text.get("crm.careforaction.lb.actionexplain")</td>	
		      	<td colspan="2"><textarea name="remark" style="width:400px; border:0px" rows="6">$!del.remark</textarea></td>
		  	</tr>	  
#end
#if($bean.actionType==1)
			<tr>
				<td width="20%" rowspan="2" align="right" valign="top" class="oabbs_tr">$text.get("com.send.sms")</td>		    
			 	<td valign="top">$text.get("timermessage.timer")：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; $del.startDate  $bean.smsTime:00
                </td>
	      </tr>
	      <tr>	    
			 	<td valign="top">$bean.smsContent</td>
	      </tr>
#end

#if($bean.actionType==2)
			<tr>
			<td width="20%" rowspan="2" colspan="2" align="right" valign="top" class="oabbs_tr">$text.get("common.lb.sendmail")</td>
			<td valign="top">$text.get("crm.carefor.lb.emailtitle")：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; $bean.mailTitle                </td>
			</tr>
			<tr> 
		      <td>$bean.mailContent </td>
		  	</tr>
#end

		</tbody>
	  </table>	
  </div>
			
	
</form>
</body>
</html>
