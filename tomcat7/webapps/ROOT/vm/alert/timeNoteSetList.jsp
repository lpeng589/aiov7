<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get('comm.alertset.list')</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript"> 
function openSelect(){
	var keyIds =document.getElementsByName("keyId") ;
		var keyVal="";
		var index = 0 ;
		for(var i=0;i<keyIds.length;i++){
			if(keyIds[i].checked){
				keyVal=keyIds[i].value;		
				index=index+1 ;
			}
		}
		if(!isChecked('keyId')){
		   alert("$text.get('common.msg.mustSelectOne')") ;
		   return false ;
		}
		if(index>1){
			alert("$text.get('property.alert.onlyselectone')") ;
			return false;;
		}
	//dialogSettings = "Center:yes;Resizable:no;DialogHeight:330px;DialogWidth:505px;Status:no";	
	//var opner=window.showModalDialog("/TimeNoteSetAction.do?operation=$globals.getOP('OP_UPDATE_PREPARE')&type=modepre&alertId="+keyVal,"",dialogSettings);
	
	var urlstr = "/TimeNoteSetAction.do?operation=$globals.getOP('OP_UPDATE_PREPARE')&type=modepre&alertId="+keyVal;
	asyncbox.open({id:'Popdiv',title:'通知方式设置',url:urlstr,width:505,height:300});
	/*
	if(true==opner)
	{
		window.location.reload();
	}
	*/	
}
function setNoteTime(){
	var keyIds =document.getElementsByName("keyId") ;
		var keyVal="";
		var index = 0 ;
		for(var i=0;i<keyIds.length;i++){
			if(keyIds[i].checked){
				keyVal=keyIds[i].value;		
				index=index+1 ;
			}
		}
		if(!isChecked('keyId')){
		   alert("$text.get('common.msg.mustSelectOne')") ;
		   return false ;
		}
		if(index>1){
			alert("$text.get('property.alert.onlyselectone')") ;
			return false;;
		}
	//dialogSettings = "Center:yes;Resizable:no;DialogHeight:330px;DialogWidth:505px;Status:no";	
//	var opner=window.showModalDialog("/TimeNoteSetAction.do?operation=$globals.getOP('OP_UPDATE_PREPARE')&type=timepre&alertId="+keyVal,"",dialogSettings);
	
	var urlstr = "/TimeNoteSetAction.do?operation=$globals.getOP('OP_UPDATE_PREPARE')&type=timepre&alertId="+keyVal;
	asyncbox.open({id:'Popdiv',title:'通知时间设置',url:urlstr,width:505,height:180});
	/*
	if(true==opner)
	{
		window.location.reload();
	}
	*/	
}





function setAlertUser(){
	var keyIds = document.getElementsByName("keyId") ;
		var keyVal="";
		var index = 0 ;
		for(var i=0;i<keyIds.length;i++){
			if(keyIds[i].checked){
				keyVal=keyIds[i].value;
				index=index+1 ;
			}
		}
		if(!isChecked('keyId')){
		   alert("$text.get('common.msg.mustSelectOne')") ;
		   return false ;
		}
		if(index>1){
			alert("$text.get('property.alert.onlyselectone')") ;
			return false;;
		}
	//var opner=window.showModalDialog("/TimeNoteSetAction.do?operation=$globals.getOP('OP_UPDATE_PREPARE')&type=employeepre&alertId="+keyVal,"","dialogWidth=600px;dialogHeight=400px");
	
	
	var urlstr = "/TimeNoteSetAction.do?operation=$globals.getOP('OP_UPDATE_PREPARE')&type=employeepre&alertId="+keyVal;
	asyncbox.open({id:'Popdiv',title:'通知对象设置',url:urlstr,width:600,height:400});
	/*
	if(true==opner)
	{
		window.location.reload();
	}
	*/
}
function onPreSet(status)
{
	if(!isChecked('keyId')){
		alert("$text.get('common.msg.mustSelectOne')") ;
		return false ;
	}
	
	document.alertSetForm.operation.value="$globals.getOP('OP_UPDATE')";
	document.alertSetForm.status.value=status;
	document.alertSetForm.type.value="status";
	document.alertSetForm.submit();
}
function showState()
{
	var state="$!state";
	//alert(state);
	//alert($!alert.statusDisplay);
	if(state=="true")
	{
		alert("$text.get('oa.bbs.operationOk')");
	}
	else if(state=="false")
	{
		alert("$text.get('common.alert.updateFailure')");
	}
}

function flushAlert(alertId,useParam)
{
	var regex=/^[0-9]*$/;
	if(null==useParam.value||useParam.value==""||regex.test(useParam.value))
	{
		AjaxRequest("/UtilServlet?operation=setAlertUseParam&alertId="+alertId+"&useParam="+useParam.value);
	}
	else
	{
		alert("$text.get('common.validate.error.any')");
		useParam.value="";
		return false;
	}
}

var minValue = -999999999 ;
var maxValue = 9999999999 ;
function changeDefSys(defSys){
	window.location.href = "/TimeNoteSetAction.do?operation=$globals.getOP('OP_QUERY')&defSys="+defSys;
}
-->	
</script>
</head>
<body onLoad="showState()">
<form  method="post" scope="request"  name="alertSetForm" action="/TimeNoteSetAction.do">
<input type="hidden" name="operation">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="">
<input type="hidden" name="winCurIndex" value="5">
<input type="hidden" name="status">
<input type="hidden" name="type">
<div class="Heading">
  <div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
  <div class="HeadingTitle">$text.get('timeNote.msg.title')</div>
  <ul class="HeadingButton">
      <li>
      <button type="button" name="onPre" id="onPre" title="Ctrl+O" onClick="onPreSet('0');" class="b2">$text.get('sms.button.start')</button>
    </li>
	    <li>
      <button type="button" name="offPre" id="offPre" title="Ctrl+F" onClick="onPreSet('1')" class="b2">$text.get('note.disable')</button>
    </li>
	    <li>
      <button type="button" name="offPre" id="setPre" title="Ctrl+S" onClick="setNoteTime()" class="b6">$text.get('timeNote.lb.time')$text.get('common.msg.set')</button>
    </li>
	    <li>
      <button type="button" name="offPre" id="setPre" title="Ctrl+S" onClick="setAlertUser()" class="b6">$text.get('timeNote.lb.object')$text.get('common.msg.set')</button>
    </li>
	    <li>
      <button type="button" name="setPreMode" id="setPreMode" title="Ctrl+M" onClick="openSelect()" class="b6">$text.get('timeNote.lb.method')$text.get('common.msg.set')</button>
    </li>
    <li>
      <button type="button" name="setCRMPre" id="setCRMPre" title="Ctrl+P" onClick="changeDefSys('1')" class="b6">$text.get('timeNote.set.pps')</button>
    </li>
    <li>
      <button type="button" name="setCRMPre" id="setCRMPre" title="Ctrl+P" onClick="changeDefSys('3')" class="b6">$text.get('timeNote.set.crm')</button>
    </li>
    <li>
      <button type="button" name="setOAPre" id="setOAPre" title="Ctrl+N" onClick="changeDefSys('2')" class="b6">$text.get('timeNote.set.oa')</button>
    </li>
    <li>
      <button type="button" name="setHRPre" id="setHRPre" title="Ctrl+L" onClick="changeDefSys('4')" class="b6">$text.get('timeNote.set.hr')</button>
    </li>
  </ul>
</div>
  <div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
    <table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" width="100%" name="table" id="tblSort">
      <THead>
        <tr class="scrollColThead">
          <td width="20" align="center" ><input type="checkbox" name="selAll"  onClick="checkAll('keyId')"></td>
          <td width="250" align="center">$text.get('timeNote.lb.setName')</td>
          <td width="100" align="center">$text.get('timeNote.lb.setTime')</td>
          <td width="100" align="center">$text.get('timeNote.lb.method')</td>
          <td width="100" align="center">$text.get('timeNote.lb.object')</td>
          <td width="50" align="center">$text.get('timeNote.lb.status')</td>
        </tr>
      </THead>
      <TBody>
      
      #foreach($note in $noteList)
      <tr>
        <td  width=30 align="center">
          <input list='true' type='checkbox' name='keyId' value='$globals.get($note,5)'></td>
        <td  width=250 align="left">$globals.getEnumerationItemsDisplay("TimingMsg",$globals.get($note,0))</td>
        <td  width=100 align="left">$globals.get($note,1)</td>
        <td  width=100 align="left">#if($globals.get($note,2)=="sendNote")$text.get('timeNote.lb.note')#elseif($globals.get($note,2)=="sendEmail")$text.get('timeNote.lb.email')#else$text.get('timeNote.lb.note');$text.get('timeNote.lb.email')#end</td>
        <td width="100" align="left">#if($globals.get($note,4).length()==0)&nbsp;#else$globals.get($note,4)#end</td>
        <td width="50" align="center">$globals.getEnumerationItemsDisplay("IsAble",$globals.get($note,3))</td>
      </tr>
      #end
      </TBody>
      
    </table>
  
</div>
<script type="text/javascript"> 
	var rowCount = document.getElementsByName("keyId").length ;
	if("20"!=rowCount){
		var varPage = document.getElementById("nextPageSize") ;
		if(typeof(varPage)!="undefined" && varPage!=null){
			varPage.removeAttribute("href","") ;
		}
	}	
</script>
</body>
</html>
