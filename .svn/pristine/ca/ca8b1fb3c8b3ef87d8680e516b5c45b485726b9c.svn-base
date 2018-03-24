<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
</style>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">


putValidateItem("clientName","$text.get("call.lb.client")","any","",false,0,100);
putValidateItem("linkman","$text.get("crm.linkman.name")","any","",false,0,100);
putValidateItem("email","EMAIL","any","",false,0,50 );

function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	//window.parent.frames['leftFrame'].location.reload() ;
	return true;
}	

if(typeof(top.junblockUI)!="undefined"){
	top.junblockUI();
}

function showClient(){
	var urlstr = '/UserFunctionAction.do?tableName=CRMSaleFollowUp&fieldName=ClientId&operation=22'
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add","","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined") return ;
	ss = str.split(";");
	document.form.clientId.value=ss[0];
	document.form.clientName.value=ss[2]; 
}
function blackload(){
}
function ml(){
	alert("ml");
	window.location.reload();

}
function mload(){
	window.close();
}
var oper = opener;
function addClient(){	
	
	var wo =window.open("/UserFunctionQueryAction.do?tableName=CRMClientInfo&operation=6&noback=true&CRMClientInfoDet_UserNameDV="+encodeURIComponent('$!linkman')+"&ClientName="+encodeURIComponent('$!linkman')+"&CRMClientInfoDet_ClientEmailDV=$!email&src=menu&winCurIndex=99","_blank");

	if (window.addEventListener){
		wo.addEventListener('unload', mload, false);
	} else if (window.attachEvent){
		wo.attachEvent('onunload', mload);		
	} 
	/*
	var wo =window.showModalDialog("/UserFunctionQueryAction.do?tableName=CRMClientInfo&operation=6&noback=true&CRMClientInfoDet_UserNameDV="+encodeURIComponent('$!linkman')+"&ClientName="+encodeURIComponent('$!linkman')+"&CRMClientInfoDet_ClientEmailDV=$!email&src=menu&winCurIndex=99","","dialogWidth=800px;dialogHeight=450px");  
	window.close();
	*/
	
}

</script>
</head>
<body  scroll="no">

<form id="form" name="form" method="post" action="/EMailAction.do" onSubmit="return beforSubmit(this)" >
<input type="hidden" name="id" value="$!result.id" />
<input type="hidden" name="operation" value="4"/>
<input type="hidden" name="type" value="mailCRM"/>
<input type="hidden" name="op" value="add"/>
<input type="hidden" name="clientId" value="$!clientId"/>


<div class="oamainhead">
<div class="HeadingButton">

	</div>
  </div>			

<div align="center">
	<div class="scroll_function_small_a" >
				<div class="listRange_div_jag_hint" style="width:600px;" align="center" >
					<div class="listRange_div_jag_div" style="width:600px; height:22px; line-height:22px;">$text.get("calendar.lb.RelationKeHu")</div>							
					<ul class="listRange_woke" style="width:480px; " >
						<!--  第一页-->
						<li style="width:480px;">
							<span style="width:100px;">$text.get("call.lb.client")</span>
							<input type="text" name="clientName" id="clientName" value="$!clientName" class="text" style="width:230px;" onKeyDown="if(event.keyCode!=13&&event.keyCode!=9)event.returnValue=false;"  onDblClick="showClient()">
      						<font color="#FF0000">* </font><img onClick="showClient()" src="/$globals.getStylePath()/images/St.gif" class="search">
      						#if("$LoginId" == "1"  || $LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=CRMClientInfo").add())
      						<button type="button" onClick="addClient()" style="margin-left:20px;">$text.get("email.title.addnewclient")</button>
      						#end
						</li>
						<li style="width:480px;">
						<span style="width:100px;">$text.get("crm.linkman.name")</span>
						<input name="linkman" id="linkman" type="text" value="$!linkman" class="text"  style="width:230px;">
						<font color="#FF0000">* </font>
						</li>
						<li style="width:480px;">
							<span style="width:100px;">EMAIL</span>
							<input name="email" id="email" type="text" value="$!email" class="text"  style="width:230px;">
							<font color="#FF0000">* </font>
						</li>						
						<li style="width:420px;text-align:center"><button type="submit" name="Submit">$text.get("common.lb.ok")</button>
						<button type="button" onClick="window.close()">$text.get("common.lb.close")</button>
						</li>
					
					</ul>
				</div>																		
	</div>			
 
</div>
</form>
</body>
</html>
