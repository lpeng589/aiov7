<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("send.title.sendMessage")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/formvalidate.vjs"></script>
<script type="text/javascript">
#set($tableName=$request.getParameter("tableName"))
#set($action=$request.getParameter("action"))
#set($userIds=$request.getParameter("userIds"))
#set($deptIds=$request.getParameter("deptIds"))
#set($otherEmail=$request.getParameter("otherEmail"))
#set($otherSMS=$request.getParameter("otherSMS"))
winObj = window.dialogArguments ;
function m(value){
	return document.getElementById(value) ;
}

function n(value){
	return document.getElementsByName(value) ;
}

var deptUrl = '/UserFunctionAction.do?operation=22&tableName=tblDepartment&fieldName=DeptFullName&selectName=SelectDepartmentMes&MOID=$MOID&MOOP=add';
var empUrl  = '/UserFunctionAction.do?operation=22&tableName=tblEmployee&selectName=SelectSMSEmployee&MOID=$MOID&MOOP=query&LinkType=@URL:';
var moduleUrl = '/UserFunctionAction.do?operation=22&tableName=tblSMSModel&selectName=SelectSMSModel&BillTableName=$!tableName&BillAction=$!action&MOID=$MOID&MOOP=query';

function openPopupSelect(strId,strUrl,title){
	asyncbox.open({id:strId,title:title,url:strUrl+'&popupWin='+strId,width:800,height:470}); 
}

function exeDeptpopup(str){
	if(typeof(str)=="undefined") return ;
	var depts = str.split("|") ;
	for(var j=0;j<depts.length;j++){
		var field = depts[j].split(";") ;
		var existOption = m("formatDeptName").options;
		var length = existOption.length;
		var talg = false ;
		for(var i=0;i<length;i++)
		{
			if(existOption[i].value==field[0])
			{
				talg = true;
			}
		}
		if(!talg){
			m("formatDeptName").options.add(new Option(field[1],field[0]));
			m("popedomDeptIds").value+=field[0]+",";
		}
	}
}

function exeEmppopup(str){
	if(typeof(str)=="undefined") return ;
	var employees = str.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = m("formatFileName").options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++)
			{
				if(existOption[i].value==field[0])
				{
					talg = true;
				}
			}
			if(!talg){
				m("formatFileName").options.add(new Option(field[1],field[0]));
				m("popedomUserIds").value+=field[0]+",";
			}
		}
	}
}

function exeModulepopup(str){
	if(typeof(str)=="undefined") return ;
	var content = str.split(";") ;
	m("wakeUpContent").value=content[0];
}


function showCrmCustomer(){
	var urlstr = '/UserFunctionAction.do?operation=22&tableName=CRMCompanyAll&selectName=selectCRMCompany' ;
	var str = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=query",winObj,"dialogWidth=750px;dialogHeight=450px"); 
	if(typeof(str)=="undefined") return ;
	var employees = str.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = m("formatCRMCompany").options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++)
			{
				if(existOption[i].value==field[0]+"#"+field[1])
				{
					talg = true;
				}
			}
			if(!talg){
				m("formatCRMCompany").options.add(new Option(field[2]+"-"+field[3],field[0]+"#"+field[1]));
				m("popedomCRMCompany").value+=field[0]+"#"+field[1]+",";
			}
		}
	}
}

function showCompany(){
	var urlstr = '/UserFunctionAction.do?operation=22&tableName=tblCompany&selectName=SelectCompanyEmployee' ;
	var str = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=query",winObj,"dialogWidth=750px;dialogHeight=450px"); 
	if(typeof(str)=="undefined") return ;
	var companys = str.split("|") ;
	for(var j=0;j<companys.length;j++){
		var field = companys[j].split(";") ;
		if(field!=""){
			var existOption = m("formatCompanyName").options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++)
			{
				if(existOption[i].value==field[0]+"-"+field[1])
				{
					talg = true;
				}
			}
			if(!talg){
				m("formatCompanyName").options.add(new Option(field[2]+"-"+field[3],field[0]+"-"+field[1]));
				m("popedomCompanyCodes").value+=field[0]+"-"+field[1]+",";
			}
		}
	}
}

function delOpation(fileName,popedomId){
	var formatName=m(fileName);
	if(formatName.selectedIndex==-1){
		alert("$text.get('oa.lb.common.pleaseChoose.remove')");
	}
	if(formatName.selectedIndex<0)return false ;
	var value = formatName.options[formatName.selectedIndex].value;
	var appcers = m(popedomId).value;
	appcers = appcers.replace(value+",","");
	m(popedomId).value =appcers
	formatName.options.remove(formatName.selectedIndex);
}

function sendBillMsg(){	
	var wakeUpMode = n("wakeUpMode") ;
	var varWakeUp = "" ;
	for(var i=0;i<wakeUpMode.length;i++){
		if(wakeUpMode[i].checked){
			varWakeUp +=wakeUpMode[i].value+";"
		}
	}
	if(varWakeUp.length==0){
		alert('$text.get("sendBill.lb.SelectAlarm")') ;
		return false;
	}
	var msg = m("wakeUpContent").value ;
	var userIds = m("popedomUserIds").value ;
	var deptIds = m("popedomDeptIds").value ;
	##if("$LoginBean.defSys"=="3")
	##var companys = m("popedomCompanyCodes").value ;
	##else
	##var companys = m("popedomCRMCompany").value ;
	##end
	if(userIds.length==0 && deptIds.length==0){
		alert("通知对象不能为空.") ;
		return false;
	}
	return varWakeUp+"@koron@"+msg+"@koron@"+m("popedomUserIds").value+"@koron@"
						 +m("popedomDeptIds").value+"@koron@"+m("otherEmail").value+"@koron@"
						 +m("otherSMS").value ;
	//msg = replaceMsg(msg)
	//var varURL = "/UtilServlet?operation=sendBillMsg&wakeUp="+varWakeUp+"&billMsg="+msg+"&popedomUserIds="+
	//			  m("popedomUserIds").value+"&popedomDeptIds="+m("popedomDeptIds").value
	//			  +"&popedomCompanyCodes=".value+"&popedomCRMCompany="+m("popedomCRMCompany").value;
    //AjaxRequest(varURL) ;
    //if(response == "sendSucess"){
	//	alert("发送成功") ;
	//window.close() ;
}

function replaceMsg(msg){
   var re = /\[[_=\w\u4e00-\u9fa5]+\]/g;   
   var varField = msg.match(re);  
   var winObj = window.dialogArguments ;
   for(var i=0;i<varField.length;i++){
   	  var fieldName = varField[i].substring(varField[i].indexOf("=")+1,varField[i].indexOf("]"))
	  var field = winObj.parentWindow.document.getElementById(fieldName) ;
	  if(field!=null){
	  	 msg = msg.replace(varField[i],"【"+field.value+"】") ;
	  }else{
	  	 alert(varField[i]+'$text.get("sendBill.lb.SelectNull")') ;
	  	 return false ;
	  }
   }
   return msg ;
}

function saveAsModule(){
	var msg = m("wakeUpContent").value ;
	if(msg.length==0){
		alert("通知内容不能为空.") ;
		return ;
	}
	AjaxRequest("/UtilServlet?operation=saveAsModule&tableName=$!tableName&action=$!action&content="+encodeURI(msg)) ;
	if("OK"==response){
		alert("存为模板成功.") ;
	}else{
		alert("存为模板失败.");
	}
}
</script>
</head>
<body style="overflow:hidden;">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="$!userIds"/>
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="$!deptIds"/>
<input type="hidden" name="popedomCompanyCodes" id="popedomCompanyCodes" value=""/>
<input type="hidden" name="popedomCRMCompany" id="popedomCRMCompany" value="">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("send.title.sendMessage")</div>
	<ul class="HeadingButton">
	</ul>
</div>
			<div class="listRange_1_photoAndDoc_1" style="width:98%;overflow:auto;" id="msgId">
					<div>
						<span>$text.get("oa.calendar.wakeupType")：</span>
						#foreach($wake in $globals.getEnumerationItems("WakeUpMode"))
		 					<input type="checkbox" name="wakeUpMode" #if("$wake.value"=="4") checked #end value="$wake.value"/>$wake.name
	   					#end
					</div>
					<div  style="width: 700px;height: 70px; overflow:hidden; padding-top: 10px;">
						<span>$text.get("oa.common.adviceContent")：</span>
						<div style="float:left;width: 500px; ;height: 65px;">
						<textarea rows="3" style="width: 500px;margin: 0px;padding: 0px;" id="wakeUpContent" name="wakeUpContent">[用户]$text.get("common.msg.billOfDocumentNumberIs")$globals.getTableDisplayName($request.getParameter("tableName"))</textarea>
						</div>
						<div style="float:left; width: 200x;height: 65px; text-align: left;">
						<img src="/$globals.getStylePath()/images/St.gif" onClick="openPopupSelect('Modulepopup',moduleUrl,'模板弹出窗');"  class="search" /><a style="cursor:pointer" title="$text.get('com.select.model')" onClick="openPopupSelect('Modulepopup',moduleUrl,'模板弹出窗');">$text.get('com.select.model')</a>
						<br><br>&nbsp;&nbsp;<button type="button" onclick="javascript:saveAsModule();">存为模板</button>
						</div>
						
					</div>
					<div style="float: right;"></div>
					<div style="padding-top: 10px;">
						<span>$text.get("oa.common.garget")：</span>
						<div class="oa_signDocument1" id="_context">
						<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="openPopupSelect('Deptpopup',deptUrl,'部门弹出窗');"  alt="$text.get("oa.common.adviceDept")" class="search">&nbsp;<a style="cursor:pointer" title="$text.get('oa.select.dept')" onClick="openPopupSelect('Deptpopup',deptUrl,'部门弹出窗');">$text.get("oa.select.dept")</a></div>
							<select name="formatDeptName" id="formatDeptName" multiple="multiple">
							#foreach($dept in $globals.queryUserName("$deptIds","dept"))
								<option value="$user.id">$dept.departmentName</option>
							#end
							</select>
						</div>
						<div class="oa_signDocument2">
							<img onClick="delOpation('formatDeptName','popedomDeptIds')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
						</div>
						<div class="oa_signDocument1" id="_context">
						<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="openPopupSelect('Emppopup',empUrl,'职员弹出窗');" alt="$text.get("oa.common.advicePerson")" class="search">&nbsp;<a style="cursor:pointer" title="$text.get('oa.select.personal')" onClick="openPopupSelect('Emppopup',empUrl,'职员弹出窗');">$text.get("oa.select.personal")</a></div>
							<select name="formatFileName" id="formatFileName" multiple="multiple">
								#foreach($user in $globals.queryUserName("$userIds","user"))
								<option value="$user.id">$user.name</option>
								#end
							</select>
						</div>
						<div class="oa_signDocument2">
							<img onClick="delOpation('formatFileName','popedomUserIds')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
						</div>
						<!-- 
						#if("$LoginBean.defSys"=="3")
							<div class="oa_signDocument1" id="_context">
							<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="showCrmCustomer();" alt="$text.get("oa.common.advicePerson")" class="search">&nbsp;<a style="cursor:pointer" title="$text.get('com.select.crm')" onClick="showCrmCustomer();">$text.get("com.select.crm")</a></div>
								<select name="formatCRMCompany" id="formatCRMCompany" multiple="multiple" style="width: 150px;">
								</select>
							</div>
							<div class="oa_signDocument2">
								<img onClick="delOpation('formatCRMCompany','popedomCRMCompany')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
							</div>
						#else
							<div class="oa_signDocument1" id="_context">
							<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="showCompany();" alt="$text.get("oa.common.advicePerson")" class="search">&nbsp;<a style="cursor:pointer" title="$text.get('com.select.company')" onClick="showCompany();">$text.get('com.select.company')</a></div>
								<select name="formatCompanyName" id="formatCompanyName" multiple="multiple">
								</select>
							</div>
							<div class="oa_signDocument2">
								<img onClick="delOpation('formatCompanyName','popedomCompanyCodes')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
							</div>
						#end
						 -->
					</div>
					<div  style="width: 700px;height:55px; overflow:hidden; padding-top: 10px;">
						<span>其它邮件：</span>
						<div style="float:left;width: 500px; ;height: 65px;">
						<textarea rows="3" style="width: 500px;margin: 0px;padding: 0px;" id="otherEmail" name="otherEmail">$!otherEmail</textarea>
						</div>
					</div>
					<div style="padding-left:80px;"><font color="red">注：邮件地址之间用":"隔开,如：info@koronsoft.com:info2@koronsoft.com</font></div>
					<div  style="width: 700px;height:55px; overflow:hidden; padding-top: 10px;">
						<span>其它手机：</span>
						<div style="float:left;width: 500px; ;height: 65px;">
						<textarea rows="3" style="width: 500px;margin: 0px;padding: 0px;" id="otherSMS" name="otherSMS">$!otherSMS</textarea>
						</div>
					</div>
					<div style="padding-left:80px;"><font color="red">注：手机号码之间用":"隔开,如：1581859****:1581859****</font></div>
			</div>
<script type="text/javascript">
	$("#msgId").height($(window).height()-40);
</script>
</body>
</html>
