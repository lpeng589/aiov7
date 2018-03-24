<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="/js/formvalidate.vjs"></script>
<script type="text/javascript">

putValidateItem("mainCall","$text.get('tel.lb.hostCall')","mobile","",false,0,10000000);
putValidateItem("calls","$text.get('tel.lb.passCall')","any","",false,1,9999 );

function beforeform(){   
	if(!validate(document.form))return false;
	disableForm(document.form);
		
	return true;
}	

function showEmploy(){
	openSelect("/UserFunctionAction.do?tableName=HREmpinform&selectName=pop_viewEmployeeTel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/TelAction.do?operator=callTel').moduleId&MOOP=query",form.calls);
}
function showCumSuper(){
	openSelect("/UserFunctionAction.do?tableName=tblUser&selectName=pop_viewCompanyTel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/TelAction.do?operator=callTel').moduleId&MOOP=query",form.calls);
}
function showOAComm(){
	openSelect("/UserFunctionAction.do?tableName=OACommunicationNoteInfo&selectName=pop_ViewOACommunicationTel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/TelAction.do?operator=callTel').moduleId&MOOP=query",form.calls);
}
function showCRMClient(){
	openSelect("/UserFunctionAction.do?tableName=CRMCompanyAll&selectName=pop_ViewCRMCompanyTel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/TelAction.do?operator=callTel').moduleId&MOOP=query",form.calls);
}

function selectSMSModel(obj){
  openSelect("/UserFunctionAction.do?tableName=tblSMSModel&selectName=SelectSMSModel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/NoteAction.do?operator=sendSMS').moduleId&MOOP=query",obj);
}
function openSelect(urlstr,obj){
	var str  = window.showModalDialog(urlstr,"","dialogWidth=800px;dialogHeight=450px");	
	if(str == undefined || str == ""){
		return;
	}
	if(str.indexOf(";|") == -1){
		obj.value = obj.value+ str+"\r\n";
		return;
	}
	fs=str.split(";|");
	var mobileNames="";
	for(var i=0;i<fs.length;i++){
		var value=fs[i];
		if(value !="" ){
		 	mobileNames +=value+";\r\n";
		}
	}
	obj.value = obj.value+ mobileNames;
	
}


function  totalNum()
{

}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/TelAction.do?operator=callTel&type=call" name="form" method="post" target="formFrame">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="noback" value="$!noback">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("tel.lb.callPhone")</div>
	<ul class="HeadingButton">
					 <li></li>
			</ul>
</div>
<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
    <div class="scroll_function_small_a">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="oabbs_tr">$text.get("tel.lb.hostCall")：</td>
				<td>
					<input type="text" name="mainCall" value="$!mainCall" class="text" style="width:200px"><span style="color: #0066FF;margin-left:10px">$text.get("tel.lb.hostMustMobilephone")</span>
				</td>
			</tr>		
				
			<tr>
			 <td class="oabbs_tr">$text.get("tel.lb.passCall")：</td>
			 <td><span style="color: #0066FF">$text.get("tel.msg.callOneCharge")<br/>$text.get("tel.msg.callMoreOneCharge")<br/>$text.get("tel.msg.callMoreOneMark")</span>
			 <br/>
		<textarea name="calls"  id="calls" cols="45" rows="10" onpropertychange="totalNum()" >$!calls</textarea>
		<a href="javascript:void(0)" onClick="showEmploy()">$text.get("note.lb.SelectEmployee")</a>
				<a href="javascript:void(0)" onClick="showCumSuper()">$text.get("note.lb.SelectProvider") </a>
				<!-- <a href="javascript:void(0)" onClick="showOAComm()">$text.get("note.lb.SelectOAAdress")</a> -->
				<a href="javascript:void(0)" onClick="showCRMClient()">$text.get("note.lb.SelectCRM")</a>
		</td>			   
		</tr>
		<tr>
			<td colspan="2">
				<button type="button" name="button" onClick="if(beforeform()) {document.form.submit();}" class="b2" style="margin-left:250px;width:80px">$text.get("tel.lb.beginCalling")</button>
			</td>
		</tr>
		  </tbody>
		</table>
	</div>
</div>
</form>
</body>
</html>
