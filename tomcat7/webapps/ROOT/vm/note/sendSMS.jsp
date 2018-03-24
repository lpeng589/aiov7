<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("note.send.title")</title>
<link rel="stylesheet" type="text/css" href="/$globals.getStylePath()/css/ListRange.css">
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />


<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>
<script type="text/javascript">

putValidateItem("receiver","$text.get('note.lb.CollectionMessage')","any","",false,0,10000000);
putValidateItem("content","$text.get('note.send.content')","any","",false,1,9999 );


function showdxmDiv(){
	top.showMOBDiv('/dcat.html','/common/3g.qq.com/mcat.apk','下载短信猫到电脑');
}
function beforeform(){   
	if(!validate(document.form))return false;
	disableForm(document.form);
	return true;
}	

function showEmploy(){
	var urlstr = "/UserFunctionAction.do?tableName=HREmpinform&popupWin=NotePopup&selectName=pop_tblEmployee&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/NoteAction.do?operator=sendSMS').moduleId&MOOP=query";
	asyncbox.open({id:'NotePopup',title:"选择职员",url:urlstr,width:750,height:470});
}
function showCumSuper(){
	var urlstr = "/UserFunctionAction.do?tableName=tblUser&popupWin=NotePopup&selectName=pop_tblCompany&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/NoteAction.do?operator=sendSMS').moduleId&MOOP=query";
	asyncbox.open({id:'NotePopup',title:"选择客户供应商",url:urlstr,width:750,height:470});
}
function showOAComm(){
	var urlstr = "/UserFunctionAction.do?tableName=OACommunicationNoteInfo&popupWin=NotePopup&selectName=pop_OACommunicationNoteInfo&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/NoteAction.do?operator=sendSMS').moduleId&MOOP=query";
	asyncbox.open({id:'NotePopup',title:"选择OA通讯录",url:urlstr,width:750,height:470});
}
function showCRMClient(){
	var urlstr = "/UserFunctionAction.do?tableName=CRMCompanyAll&popupWin=NotePopup&selectName=pop_CRMCompanyAll&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/NoteAction.do?operator=sendSMS').moduleId&MOOP=query";
	asyncbox.open({id:'NotePopup',title:"选择CRM客户",url:urlstr,width:750,height:470});
}

function selectSMSModel(obj){
  openSelect("/UserFunctionAction.do?tableName=tblSMSModel&popupWin=NotePopup&selectName=SelectSMSModel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/NoteAction.do?operator=sendSMS').moduleId&MOOP=query",obj);
}

function exeNotePopup(str){
	if(str == undefined || str == ""){
		return;
	}
	var obj = form.receiver;
	if(str==";;"){
		obj.value = "" ;
	}else{
		fs=str.split(";|");
		var mobiles="";
		var mobileNames="";
		for(var i=0;i<fs.length;i++){
			var value=fs[i];
			if(value !=""){
				v = value.split(";")
			 	mobiles+=v[1]+"("+v[0]+")"+":"+v[0]+";";
			 	mobileNames +=v[1]+"("+v[0]+")"+";\r\n";
			}
		}
		obj.value = obj.value+ mobileNames;
		document.form.receiverMobile.value = document.form.receiverMobile.value+mobiles;
	}	
}

function recresize(){
	var s=document.form.receiver.value.match(/\n/g);
	if(s ){
		sl = s.length+1;
		if(sl >10) sl = 10;
		document.form.receiver.rows = sl;
	}
}
function changeSmsType(){
	if(document.form.smsType.value==1){
		if(!confirm("$text.get("sms.msg.choiceNote")")){
			document.form.smsType.value = 0;
		}
	}
}

function  totalNum()
{
	cont = document.form.content.value;
	contlen = cont.length;	
	if(contlen > 490){
		document.form.content.value = document.form.content.value.substring(0,490)
		contlen = 490;
	}
	document.getElementById("fontnum").innerHTML=contlen;
}

function immediately(){
    var element = document.getElementById("mobileContent");
    if("\v"=="v"){//判断IE
        element.onpropertychange = totalNum;
    }else{//非IE
        element.addEventListener("input",totalNum,false);
    }
}

</script>
</head>
<body onload="immediately()";>
<form action="/NoteAction.do?operator=sendSMS&type=add" onSubmit="return beforeform()"  name="form" method="post">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="smsContent" value=""/>
 <input type="hidden" name="noback" value="$!noback">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("note.send.title")</div>
	
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
				<td class="oabbs_tr">$text.get("note.lb.CollectionMessage")：</td>
				<td>
				<textarea name="receiverMobile"  id="receiverMobile" cols="45" rows="1" style="display:none" ></textarea>
				<textarea name="receiver"  id="receiver" cols="45" rows="10" onpropertychange="recresize();" style="float:left" ></textarea>
				<div style="float:left;padding:10px 0 0 10px">
				<a class="btn btn-small" style="width:90px" href="javascript:void(0)" onClick="showEmploy()">$text.get("note.lb.SelectEmployee")</a><br/><br/>
				<a class="btn btn-small" style="width:90px" href="javascript:void(0)" onClick="showCumSuper()">$text.get("note.lb.SelectProvider") </a><br/><br/>
				<a class="btn btn-small" style="width:90px" href="javascript:void(0)" onClick="showCRMClient()">$text.get("note.lb.SelectCRM")</a>
				</div>
				</td>
			</tr>		
				
			<tr>
			 <td class="oabbs_tr">$text.get("note.send.content")：</td>
			 <td>
		<span>
			 <span style="color: #0066FF">请在群发短信前确保短信内容不违规			 
			 </span>
			 <br/>
			<textarea name="content"  id="mobileContent" cols="45" rows="10" ></textarea>		
			<br/> 
			支持长短信，最多不超过490个字<span id=fontnum style="color:red">0</span>$text.get("note.msg.InputIngC")
		</span>
			<span style="color: #0066FF">说明：<br/>	 
				因国家政策及运营商限制，从V7版开始，将不再支持106通道，<br/>
				使用短信功能，请按以下方法执行<br/>
				1、使用安桌系统智能机，下载并安装“科荣短信猫”  <span onclick="showdxmDiv();" class="regbut btn btn-small btn-danger">下载短信猫</span><br/>
				2、启动科荣短信猫，输入您服务器地址和admin帐号密码登陆<br/>
				科荣短信猫会自动通过手机发送短信，费用由您绑定的手机进行结算，科荣软件不再提供短信充值服务。<br/>
				建议根据您的短信量选择合适的短信套餐，每台手机每小时最多能发送100条短信，如短信量大建议配置多台手机</td>
			</span>

		</td>
			   
		</tr>
		<tr>
			<td colspan="2">
				<button type="button" name="button" onClick="if(beforeform()) {document.form.submit();}" class="b2" style="margin-left:250px;width:80px">$text.get("oa.mail.sendMail")</button>
				
			</td>
		</tr>
		  </tbody>
		</table>
	</div>
</div>
</form>
</body>
</html>
