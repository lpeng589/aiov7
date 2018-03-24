<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBoxList.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script src="/js/jquery.js" type="text/javascript"></script>


<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/aioselect.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script language="javascript" src="/js/jquery.divbox.js"></script>
<script language="javascript">

  function setId(vars)
  {
  	 form.alertDetID.value=vars;
	 form.submit(); 
  }
  
  function read(keyId){
	jQuery.get("/UtilServlet?operation=updateAlertStatus&type=advice&alertId="+keyId,function(){
		$("#st_"+keyId).html("$text.get("oa.mail.hasread")");
		$("#rd_"+keyId).html("");
	}) ;
}
function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}

function openSelect(urlstr,obj,field){
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=update","","dialogWidth=730px;dialogHeight=450px");
	
	fs=str.split(";");   	
    form.userId.value=fs[0];
	form.userName.value=fs[2];
	return 0;
}
function updateDate(vars)
{
	 this.location.href="/AlertAction.do?operation=$globals.getOP('OP_UPDATE')&alertDetID="+vars+"&"+vars+"="+document.getElementsByName(vars)[0].value;
}
function beforeHasRead(){
	if(!isChecked("keyId")){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	document.form.operation.value = "2" ;
	document.form.submit();
}
function updateStatus(alertId){
	if(typeof(alertId)!="undefined"){
		jQuery.get("/UtilServlet?operation=updateAlertStatus&alertId="+alertId,function(){
			$("#st_"+alertId).html("$text.get("oa.mail.hasread")");
			$("#rd_"+alertId).html("");
		}) ;
	}
}
$(document).ready(function(){
if(typeof(top.junblockUI)!="undefined"){
	  top.junblockUI();
   }
   $("#queryCondition").click(function() { $('#condtionsList').OpenDiv(); });
   $("#draftCondition").click(function() {
   		  $("#btnConfirmSCA").hide() ;
   	  	  $('#condtionsList').OpenDiv(); 
   	  });
   $("#btnConfirmSCA").click(function() {
   		form.submit() ;
   		$("#condtionsList").CloseDiv();
   	});
   $("#btnCancelSCA").click(function() { $("#condtionsList").CloseDiv(); });
   $("#closeDiv").click(function() { $("#condtionsList").CloseDiv();});
});
</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">

<form style="margin:0px;"  method="post" scope="request" name="form" action="/AlertAction.do">
 <input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
   <input type="hidden"  name="alertDetID">
<div class="Heading">
	<div style="float:left;margin-left:30px;padding-bottom:1px;">
	<div style="float:left;padding-left:20px" >
	<script type="text/javascript">aioselect('name',[{value:'',name:'$text.get("alertCenter.lb.type")'}
	  #foreach($name in $globals.getEnumerationItems("SysAlert"))
			,{value:'$!name.value',name:'$name.name'}
	  #end
	  ],
	'$!AlertCenterForm.name','300','document.form.submit()');</script>
	<script type="text/javascript">aioselect('alertStatus',[{value:'',name:'$text.get("alertCenter.lb.status")'}
	  ,{value:'noRead',name:'$text.get("oa.mail.notRead")'},{value:'hasRead',name:'$text.get("oa.mail.readed")'}
	  ],
	'$!AlertCenterForm.alertStatus','80','document.form.submit()');</script>
	</div>
	</div>
	<ul class="HeadingButton">
	<li><button id="queryCondition"  type="button" class="b2">$text.get("com.query.conditions")</button></li>
	#if($globals.getMOperation().delete())
		<li><button type="button" onClick="javascript:var sd = sureDel('keyId'); if(sd){ form.operation.value=$globals.getOP('OP_DELETE'); document.form.submit();}" class="b2">$text.get("common.lb.del")</button></li>
	#end
	<li><button type="button"" onclick="return beforeHasRead();" class="b2">$text.get("oa.mail.readed")</button>
	</ul>
</div>
<div id="condtionsList">
		<div style="border: 1px solid #A2CEF5; background: #3399FF;margin-bottom: 10px; width:587px">
		 <div style="float: left;line-height: 25px; margin-left:10px;font-weight: bold;color: #fff">$text.get("tableInfo.lb.searchCondition") </div>
		 <div style="margin-top:3px;float:right;text-align: right;margin-right: 5px;"><img id="closeDiv" style="cursor: pointer;" src="/$globals.getStylePath()/images/bottom/CloseWin_1.gif"/></div>
		</div>
		<div class="listRange_3" id="listid">
					<li style="width: 500px;">
						
						<span>$text.get("advice.lb.time")：</span>$text.get("oa.common.from")
						<input style="width:70px;" name="startDate" value="$!AlertCenterForm.startDate" 
									onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"/>
						$text.get("oa.common.to")
						<input style="width:70px;" name="endDate" value="$!AAlertCenterForm.endDate"
									onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"/>
						&nbsp;
					</li>
					
		</div>
		<div style="float:left; width:99%; margin:5px; padding:5px; text-align: center;">
            <button id="btnConfirmSCA" class="b3">$text.get("button.lable.sure")</button>
	<button type="button" onClick="clearForm(form);" class="b2">$text.get("common.lb.reset")</button>
            <button id="btnCancelSCA" class="b3">$text.get("common.lb.close")</button>
        </div>
</div>
<div id="listRange_id">
	  
		<div class="scroll_function_small_a" id="conter">
			<script type="text/javascript">
				var oDiv=document.getElementById("conter");
				var sHeight=document.body.clientHeight-115;
				oDiv.style.height=sHeight+"px";
				</script>
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr>
				    <td width="30" align="center"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
					<td width="250" align="center">$text.get("alertCenter.lb.type")</td>
					<td width="500" align="center" >$text.get("alertCenter.lb.msg")</td>
					<td width="120" align="center">$text.get("alertCenter.lb.alertTime")</td>
					<td width="45" align="center">$text.get("alertCenter.lb.status")</td>				
					<td width="45"align="center">$text.get("com.lb.operation")</td>
				</tr>			
			</thead>
			<tbody>
				#foreach ($row in $result )
				<tr>
				   <td align="center"><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td>
					<td align="left">$globals.get($row,1) &nbsp;</td>
					<td align="left" onClick="updateStatus('$globals.get($row,6)')">$globals.get($row,2) &nbsp;</td>
					<td align="center">$globals.get($row,4) &nbsp;</td>
					<td align="center">&nbsp;<span id="st_$row.id">$globals.get($row,5)</span> &nbsp;</td>
					<td align ="center">&nbsp;
					#if("$globals.get($row,5)"!=$text.get("oa.mail.hasread"))	
					<span id="rd_$row.id">				
					<a href='/AlertAction.do?keyId=$globals.get($row,6)&operation=$globals.getOP("OP_UPDATE")&winCurIndex=$winCurIndex&statuId=1&name=$!AlertCenterForm.name&userId=$!AlertCenterForm.userId&alertStatus=$!AlertCenterForm.alertStatus&startDate=$!AlertCenterForm.startDate&endDate=$!AlertCenterForm.endDate&userName=$!AlertCenterForm.userName'>$text.get("oa.mail.readed")</a>
					</span>
					#end&nbsp;
					</td>				
				</tr>
				#end		
			</tbody>
		  </table>

		</div>	
	<div class="listRange_pagebar"> $!pageBar </div>
	</div>
	</form>
	
</body>
</html>

