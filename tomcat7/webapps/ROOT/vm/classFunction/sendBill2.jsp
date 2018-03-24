<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.mail.sendBill")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>

<script type="text/javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="Notepadcontent"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

function checkForm(){
	var mailContent = editor.html();
	var from = document.form.from.value ;
  	var to = document.form.to.value;
  	var subject = document.form.subject.value;

	if(!isNull(from,'$text.get("oa.mail.emailUser")')){
		return false;
	}else if(!isNull(to,'$text.get("oa.mail.addressee")')){
		return false;
	}else if(!isNull(subject,'$text.get("oa.subjects")')){
		return false;
	}else if(subject.length>40){
		alert("$text.get('oa.mail.content.mailtitle')");
		return false ;
	}else if(!isNull(mailContent,'$text.get("oa.mail.contenet")')){
		return false ;
	}else{
		return true;
	}
}
function isNull(variable,message){
	if(variable=="" || variable==null){
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else{
		return true;
	}		
}

function openSelect1(){
	var displayName = encodeURI("$text.get("oa.mail.emailUser")") ;
	var urlstr = '/UserFunctionAction.do?selectName=emailUser&operation=$globals.getOP("OP_POPUP_SELECT")&displayName='+displayName+"&LinkType=@URL:";
	var str  = window.showModalDialog(urlstr+"&MOID=17&MOOP=add",winObj,"dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined")return ;
	if(str.indexOf("@Sess:")>=0){
		document.getElementById("from").value="";
		return ;
	}
	fs=str.split(";");  
	document.getElementById("from").value=fs[1];
}

function openSelect2(){
	var displayName = encodeURI("$text.get("oa.mail.addressee")") ;
	var str  = window.showModalDialog("/UserFunctionAction.do?selectName=Communication_Select2&operation=$globals.getOP("OP_POPUP_SELECT")&MOID=17&MOOP=add&displayName="+displayName+"&LinkType=@URL:",winObj,"dialogWidth=730px;dialogHeight=450px");  
	
	if(typeof(str)=="undefined")return ;
	
	if(str.indexOf("@Sess:")>=0||str.length<=1){
		document.getElementById("to").value="";
		return ;
	}
	var varUser = str.split(";");
	
	document.getElementById("to").value =varUser[0];
}

</script>
</head>
<body>
<form id="form" name="form" method="post" action="/UserFunctionQueryAction.do?tableName=$tableName" onSubmit="return checkForm();">
<input type="hidden" id="operation" name="operation" value="1"/>
<input type="hidden" id="kid" name="kid" value="$!kid"/>
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName">
<input type="hidden" id="parentCode" name="parentCode" value="$parentCode"/>
<input type="hidden" id="winCurIndex" name="winCurIndex" value="$!winCurIndex"/>
<input type="hidden" id="type" name="type" value="email"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.mail.sendBill")</div>
	<ul class="HeadingButton">
			<li><button  type="submit" id="submit" name="Submit" class="b2">$text.get("oa.mail.sendMail")</button></li>
			#if($!noback)
			<button onclick="closeWin();" type="button" id="btnClose" name="btnClose">$text.get("common.lb.close")</button>
			#else
			#if($tableName=="OAJobodd")
				<li><button type="button" onClick="location.href='/OAJob.do?operation=$globals.getOP("OP_DETAIL")&keyId=$keyId&winCurIndex=$!request.getParameter("winCurIndex")'" class="b2">$text.get("common.lb.back")</button></li>
			#else
				#if("$!fromJsp"=="update")
					<li><button type="button" onClick="location.href='/UserFunctionAction.do?noback=$!noback&keyId=$!kid&tableName=$!tableName&parentTableName=$!parentTableName&parentCode=$!parentCode&operation=7&saveDraft=$!saveDraft&winCurIndex=$!request.getParameter("winCurIndex")'" class="b2">$text.get("common.lb.back")</button></li>
				#else
					<li><button type="button" onClick="location.href='/UserFunctionAction.do?noback=$!noback&keyId=$!kid&tableName=$!tableName&parentTableName=$!parentTableName&parentCode=$!parentCode&operation=5&saveDraft=$!saveDraft&winCurIndex=$!request.getParameter("winCurIndex")'" class="b2">$text.get("common.lb.back")</button></li>			
				#end
			#end
			#end
				
	</ul>
	<div style="margin-top:3px; margin-left:3px;">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" id="table" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tc">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width="10%" class="oabbs_tr">$text.get("oa.mail.emailUser")：</td>
			    <td width="80%"><input id="from"  name="from" size="60" class="text" value="$!setting.MailAddress" onDblClick="openSelect1()">
			    <img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1()">	</td>
			</tr>		
			<tr>
				<td class="oabbs_tr">$text.get("oa.mail.addressee")：</td>
			    <td><input id="to" name="to" size="60" class="text" onDblClick="openSelect2()">
			    <img style="cursor:pointer;" id="div_to" src="/$globals.getStylePath()/images/St.gif"  onClick="openSelect2();"></td>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("oa.subjects")：</td>
				<td><input id="subject" name="subject" size="60"  class="text" ></td>
			</tr>
			<tr>
				<td valign="top" class="oabbs_tr">$text.get("oa.mail.contenet")：</td>
				<td>
				<textarea id="Notepadcontent" name="Notepadcontent" style="width:100%;height:300px;visibility:hidden;">$html_str</textarea>
				</td>
			</tr>
		  </tbody>
		</table>
	</div>
</form>
</body>
</html>
