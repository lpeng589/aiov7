<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.carefor.title.event")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="mailContent"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

function openSetting(){
	showModalDialog('/CareforDelAction.do?operation=$globals.getOP("OP_UPDATE")&type=setMail&action=query',winObj,'dialogWidth=350px;dialogHeight=400px');
	
}

function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}
function Back(){
	form.action= "/CareforAction.do";
	form.operation.value = "$globals.getOP('OP_UPDATE_PREPARE')";
	form.KeyId.value = "$ref_id";
	form.target='';
	form.submit();
}
function val(){
	if(getStringLength(form.actionName.value)==0){
		alert('$text.get("crm.carefor.lb.eventname")$text.get("common.validate.error.null")');
		return false;
	}
	if(getStringLength(form.actionName.value)>50){
		alert('$text.get("crm.carefor.lb.eventname")$!text.get('common.validate.error.longer')50$!text.get('common.excl')\n $!text.get('common.charDesc')');
		return false;
	}
	// 无日期(0)，绝对日期(1)，相对日期(2)，循环(3) 
	var dateType = document.getElementsByName('dateType');
	for(var i=0;i<dateType.length;i++){
		if(dateType[i].checked){
			dateValue = dateType[i].value;
		}
	}
	if(dateValue==1){
		if(!isDate(form.startDate.value)){
			alert("$text.get('alertCenter.lb.startDate')"+"$text.get('common.validate.error.date')")
			return false;
		}
		if(!isDate(form.endDate.value)){
			alert("$text.get('alertCenter.lb.endDate')"+"$text.get('common.validate.error.date')")
			return false;
		}
	}if(dateValue==2){
		if(!(isInt(form.baselineDate2.value)&&form.baselineDate2.value!="")){
			alert("$text.get('crm.carefor.lb.afterdate')$text.get('common.validate.error.integer')");
			return false;
		}
		if(!(isInt(form.space2.value) && form.space2.value>0)){
			alert("$text.get('crm.carefor.lb.erery')xx$text.get("crm.carefor.lb.toexec")$text.get('common.validate.error.integer')");
			return false;
		}
		if(!(isInt(form.runDates2.value) && form.runDates2.value>0)){
			alert("$text.get("crm.carefor.lb.repetition")xx $text.get('crm.carefor.lb.ci')$text.get('common.validate.error.integer')");
			return false;
		}
		if(form.runDates2.value>20){
			alert("$text.get("crm.carefor.lb.repetition")xx $text.get('crm.carefor.lb.ci')$!text.get('oa.less.than')20");
			return false;
		}
		form.baselineDate.value = form.baselineDate2.value;
		form.runDates.value = form.runDates2.value;
		form.space.value = form.space2.value;
	}if(dateValue==3){
		if(!(isInt(form.baselineDate3.value)&&form.baselineDate3.value!="")){
			alert("$text.get('crm.carefor.lb.afterdate')$text.get('common.validate.error.integer')");
			return false;
		}
		if(!(isInt(form.space3.value) && form.space3.value>0)){
			alert("$text.get('crm.carefor.lb.erery')xx $text.get("crm.carefor.lb.toexec")$text.get('common.validate.error.integer')");
			return false;
		}
		if(!(isInt(form.runDates3.value) && form.runDates3.value>0)){
			alert("$text.get('crm.carefor.lb.repetition')xx $text.get("crm.carefor.lb.ci")$text.get('common.validate.error.integer')");
			return false;
		}
		if(form.runDates3.value>20){
			alert("$text.get("crm.carefor.lb.repetition")xx $text.get('crm.carefor.lb.ci')$!text.get('oa.less.than')20");
			return false;
		}
		
		if(!(isInt(form.runTimes.value) && form.runTimes.value>0)){
			alert("$text.get("crm.carefor.lb.cycle")xx $text.get('crm.carefor.lb.ci')$text.get('common.validate.error.integer')");
			return false;
		}
		if(form.runTimes.value>10){
			alert("$text.get("crm.carefor.lb.cycleTitle")xx$text.get('crm.carefor.lb.yearci')$!text.get('oa.less.than')10");
			return false;
		}
		form.baselineDate.value = form.baselineDate3.value;
		form.runDates.value = form.runDates3.value;
		form.space.value = form.space3.value;
	}
	var actionType = document.getElementsByName('actionType');
	var actionValue;
	for(var i=0;i<actionType.length;i++){
		if(actionType[i].checked){
			actionValue = actionType[i].value;
		}
	}
	// 具体事件 (0)，发短信(1)，发邮件(2)
	if(actionValue == 0){
		var con = jQuery("#eventContent").val();
		if(getStringLength(con)==0){
			alert('$text.get("crm.carefor.lb.transactiondetach")$text.get("common.validate.error.null")');
			return false;
		}
		if(getStringLength(con)>500){
			alert('$text.get("crm.carefor.lb.transactiondetach")$!text.get('common.validate.error.longer')500$!text.get('common.excl')\n $!text.get('common.charDesc')');
			return false;
		}
	}if(actionValue == 1){
		var con = form.smsContent.innerHTML;
		if(getStringLength(con)==0){
			alert('$text.get("com.note.context")$text.get("common.validate.error.null")');
			return false;
		}
		if(getStringLength(con)>250){
			alert('$text.get("com.note.context")$!text.get('common.validate.error.longer')250$!text.get('common.excl')\n $!text.get('common.charDesc')');
			return false;
		}
	}if(actionValue == 2){
		if(getStringLength(form.mailTitle.value)==0){
			alert('$text.get("crm.carefor.lb.emailtitle")$text.get("common.validate.error.null")');
			return false;
		}
		if(getStringLength(form.mailTitle.value)>50){
			alert('$text.get("crm.carefor.lb.emailtitle")$!text.get('common.validate.error.longer')50$!text.get('common.excl')\n $!text.get('common.charDesc')');
			return false;
		}
		var emailContent = editor.html();
		if(getStringLength(emailContent)==0){
			alert('$text.get("oa.mail.context")$text.get("common.validate.error.null")');
			return false;
		}
	}
	
	return true;
}

function setSMSOrEmail(){
	var nodate = document.getElementById("nodate").checked ;
	if(nodate){
		alert("$text.get("crm.carefor.msg.smsnodate")") ;
		document.getElementById("actionType").click();
		return false ;
	}
	return true ;
}

</script>
<style>
.oalistRange_scroll_1 td { height:22px; line-height:22px; padding:2px 5px 2px 2px}
</style>
</head>

<body onLoad="form.actionName.focus()">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" action="/CareforDelAction.do" onSubmit="return val();" target="formFrame">
	<input type="hidden" name="operation" value="$globals.getOP('OP_ADD')">
	<input type="hidden" name="winCurIndex" value="$!winCurIndex">
	<input type="hidden" name="sequence" value="$sequence">
	<input type="hidden" name="KeyId" value="$ref_id">
	<input type="hidden" name="ref_id" value="$ref_id">
	<input type="hidden" name="baselineDate" value="">
	<input type="hidden" name="runDates" value="">
	<input type="hidden" name="space" value="">
	
	
	
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("crm.carefor.title.event")</div>
	<ul class="HeadingButton">
				
					<li>
						<button type="submit" class="b2">
							$text.get("common.lb.save")
						</button>
					</li>
				
					<li>
						<button name="back" type="button" onClick="Back()" class="b2">
							$text.get("common.lb.back")
						</button>
					</li>
	</ul>
</div>
<div id="listRange_id">
<script type="text/javascript">
var oDiv=document.getElementById("listRange_id");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
    <div class="oalistRange_scroll_1">
		<table border="0" width="100%" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		
			<tr>
				<td colspan="3" bgcolor="#D7EBFF" style=" border-top:1px solid #AED3F1"><strong>$text.get("crm.carefor.lb.eventname")：</strong></td>
			</tr>
	
		
				<tr><td width="10%" align="right" valign="middle" class="oabbs_tr"> $text.get("crm.carefor.lb.eventname")：<font color="red">*</font></td>
				
			  <td colspan="2"> &nbsp;
			    <input name="actionName" class="text2"  style="width:450px" type="text" /></td>
		  </tr>
		  <tr><td  height="5" colspan="3" valign="top" bgcolor="#D7EBFF"><strong>$text.get("crm.carefor.lb.execdate")</strong></td></tr>
			<tr>
			  <td rowspan="5" align="right" valign="top" class="oabbs_tr">$text.get("crm.carefor.lb.execdate")</td>
			  <td bgcolor="#F5F5F5"><input name="dateType" type="radio" id="nodate" value="0" checked="checked"/>$text.get("crm.carefor.lb.nodate")：</td>
			  <td>$text.get("crm.carefor.lb.nodatelimit")</td>
			</tr>
			<tr>
			  <td bgcolor="#F5F5F5"><input name="dateType" type="radio" value="1" />$text.get("crm.carefor.lb.absolutedate")：</td>
		      <td>$text.get("alertCenter.lb.startDate")
			  <input type="text" name="startDate" onClick="openInputDate(this)" class="text2" />$text.get("common.lb.endDate")
			  <input type="text" name="endDate" onClick="openInputDate(this)" class="text2"/></td>
		  </tr>
			<tr>
			  <td bgcolor="#F5F5F5"><input name="dateType" type="radio" value="2" />$text.get("crm.carefor.lb.comparativedate")：</td>
		      <td>$text.get("crm.carefor.lb.afterdate")
			  <input type="text" name="baselineDate2" size="2" class="text2"/>
			  $text.get("crm.carefor.lb.afterdateover")，$text.get("crm.carefor.lb.erery")
			  <input type="text" name="space2"  #if($bean.dateType==2) value="$bean.space" #end size="2" class="text2">
			  $text.get("crm.carefor.lb.toexec")$text.get("crm.carefor.lb.repetition")
			  <input type="text" name="runDates2" size="2" class="text2">$text.get("crm.carefor.lb.ci")</td>
		  </tr>
			<tr>
			  <td bgcolor="#F5F5F5"><input name="dateType" type="radio" value="3" />$text.get("crm.carefor.lb.cycle")：</strong></td>
		      <td>$text.get("crm.carefor.lb.afterdate")
			  <input type="text" name="baselineDate3" size="2" class="text2"/>
			  $text.get("crm.carefor.lb.afterdateover")，$text.get("crm.carefor.lb.erery")
			  <input type="text" name="space3" size="2" class="text2">
			  $text.get("crm.carefor.lb.toexec")$text.get("crm.carefor.lb.repetition")
			  <input type="text" name="runDates3"  size="2" class="text2">
			  $text.get("crm.carefor.lb.ci")，$text.get("crm.carefor.lb.cycleTitle")
			  <input type="text" name="runTimes" size="2" class="text2">$text.get("crm.carefor.lb.yearci")</td>
		  </tr>
			<tr>
			  <td colspan="2">&nbsp; $text.get("crm.carefor.lb.showDesc")</td>
	      </tr>
			<tr>
				<td  height="5" colspan="3"  bgcolor="#D7EBFF"><strong>$text.get("crm.carefor.lb.execaction")</strong></td>
		    </tr>
			<tr>
			  <td align="right" valign="top" class="oabbs_tr"><input name="actionType" type="radio" value="0" checked="checked">
			  $text.get("crm.carefor.lb.actionconcretely")</td>
				
			  <td align="right" valign="top" bgcolor="#F5F5F5">
			 
			 $text.get("crm.carefor.lb.transactiondetach")：			  </td>
			  <td valign="top"> &nbsp;
		      <textarea name="eventContent" id="eventContent"  cols="50" rows="6"></textarea>  ( $text.get("crm.carefor.lb.manualexec"))</td>
			</tr>
			<tr>
			  <td rowspan="2" align="right" valign="top" class="oabbs_tr"><input type="radio" onclick="return setSMSOrEmail();" name="actionType" value="1">$text.get("com.carefor.lb.sendsms")</td>
			
			  <td align="right" valign="middle" bgcolor="#F5F5F5">$text.get("timermessage.timer")：</td>
			  <td valign="top">
		      &nbsp; $text.get("crm.carefor.lb.startcurday")&nbsp; <select name="smsTime">
					     #foreach ( $index in [0..23] )
					    	<option value="$index">$index:00</option>
					    #end
					  </select></td>
			</tr>
			<tr>
			  <td width="10%" align="right" valign="top" bgcolor="#F5F5F5"> $text.get("com.note.context")：&nbsp;</td>
		      <td width="80%" valign="top">  &nbsp;
	          <textarea name="smsContent" cols="50" rows="6"></textarea> </td>
		  </tr>
			<tr>
			 <td rowspan="4" align="right" valign="top"  class="oabbs_tr"><input type="radio" onclick="return setSMSOrEmail();" name="actionType" value="2">$text.get("common.lb.sendmail")</td>
			 <td align="right" valign="middle" bgcolor="#F5F5F5">$text.get("oa.mail.cc")：</td>
			 <td> &nbsp;
			   <input name="ccSelf" type="hidden" value="0">
          <input type="checkbox" name="cc" onClick="javascript:if(this.checked){form.ccSelf.value='1'} else {form.ccSelf.value='0'} ">
        $text.get("crm.carefor.lb.copyme")</td>
			</tr>
			<tr>
			  <td align="right" valign="middle" bgcolor="#F5F5F5">$text.get("oa.mail.emailUser")：</td>
		      <td> &nbsp;
		      <input type="radio" name="addressType" value="1" checked="checked"/>
		      $text.get('crm.carefor.lb.compemail')(<a href="javascript:openSetting()">$text.get('common.msg.set')</a>) &nbsp;&nbsp;
		      <input type="radio" name="addressType" value="2"/>$text.get('crm.carefor.lb.exeemail')
		      </td>
		  </tr>
			<tr>
			  <td align="right" valign="middle" bgcolor="#F5F5F5">$text.get("crm.carefor.lb.emailtitle")：</td>
		      <td> &nbsp;
		        <input name="mailTitle" type="text" class="text" style="width:500px;"/></td>
		  </tr>
			<tr>
			  <td align="right" valign="top" bgcolor="#F5F5F5">$text.get("oa.mail.context")：</td>
		      <td><textarea  name="mailContent" style="width:100%;height:300px;visibility:hidden;"></textarea></td>
		  </tr>
		 
	  </table>	
  </div></div>
			
	
</form>
</body>
</html>
