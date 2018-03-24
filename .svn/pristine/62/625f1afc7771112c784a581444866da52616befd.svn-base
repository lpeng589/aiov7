<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html >
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript" src="/js/jquery.easyui.min.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript">
var moduleType = "$!moduleType";
$(function(){
	$('input').each(function () { 
    	if ($(this).attr('required') || $(this).attr('validType')) 
		$(this).validatebox(); 
    });
 });
 
putValidateItem("templateName","$text.get("oa.workflow.lb.tempName")","any","",false,0,100);
#if("$!type" == "1")
putValidateItem("titleTemp","$text.get("oa.workflow.TopicModule")","any","",false,0,100);
#end

function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function m(value){
	return document.getElementById(value);
}



function setPopParam(fieldName){
	//将已经选择的人员，在弹出框时过滤掉
	var vars=m(fieldName).value.split(",");
	var employeeIds="";
	for(var i=0;i<vars.length;i++){
		if(vars[i].length>0){
			employeeIds+="'"+vars[i]+"',"
		}
	}
	if(employeeIds.length>0){
		employeeIds="("+employeeIds.substr(0,employeeIds.length-1)+")";
	}
	return employeeIds;
}

function delOpation(fileName,popedomId){
	var formatName=m(fileName);
	if(formatName.selectedIndex==-1){
		alert('$text.get('oa.lb.common.pleaseChoose.remove')');
	}
	if(formatName.selectedIndex<0)return false ;
	var value = formatName.options[formatName.selectedIndex].value;
	var appcers = m(popedomId).value;
	appcers = appcers.replace(value+",","");
	m(popedomId).value =appcers
	formatName.options.remove(formatName.selectedIndex);
}

function mainmd(){
   var mlf = document.form.templateName;
   var str  = window.showModalDialog("/common/moreLanguage.jsp?len=50&str="+encodeURI(mlf.value),"","dialogWidth=530px;dialogHeight=300px");
   if(typeof(str)!="undefined"){
   	mlf.value = str;
   }
}

function isExist(checkvalue){
	var existOption = document.getElementById("formatDeptName").options;
	var length = existOption.length;
	var talg = false ;
	for(var i=0;i<length;i++)
	{
		if(existOption[i].value==checkvalue)
		{
			talg = true;
		}
	}
	return talg ;
} 

function opTempFile(){
	var width = screen.availWidth-300;
	var height = screen.availHeight-250;
	#if("$!type" == "1")
		var tableName=document.getElementsByName("templateFile")[0];
		if(tableName.value.length>0){
			var file = window.showModalDialog("/WorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId="+tableName.value+"&type=simple",winObj,"dialogWidth:"+width+"px;dialogHeight:"+height+"px;center:yes;help:no;resizable:no;status:no;scroll:no;");
		}else{
			alert('$text.get("workfolw.msg.selectModle")');
			return false;	
		}
	#else
		var file = window.showModalDialog("/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex&type=$!type&openUpload=openUpload&templateFile="+document.form.templateFile.value+"&moduleType="+moduleType,winObj,"dialogWidth:"+width+"px;dialogHeight:"+height+"px;center:yes;help:no;resizable:no;status:no;scroll:no;");
		if(file){ 
			document.form.templateFile.value=file;
		}
	#end
}
function opTempFileNew(){
	var width = screen.availWidth;
	var height = screen.availHeight;
	var file = window.showModalDialog("/WorkFlowTableAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=simple",winObj,"dialogWidth:"+width+"px;dialogHeight:"+height+"px;center:yes;help:no;resizable:no;status:no;scroll:no;");
	if(file!=undefined) {
		var fs=file.split(";");
		document.getElementsByName("templateFile")[0].value=fs[0];
		document.getElementsByName("tableDisplay")[0].value=fs[1];
	}
}

//附件上传的方法








function deleteupload(fileName,tempFile,tableName,type){
	//如果是临时文件，则需删除远程文件，正式文件，不能删除
	if(!confirm('$text.get("common.msg.confirmDel")')){
		return;
	}

	if("true" == tempFile){
		var str="/UploadDelAction.do?NOTTOKEN=NOTTOKEN&operation=$globals.getOP("OP_DELETE")&fileName="+encodeURIComponent(fileName)+"&tempFile="+tempFile+"&tableName="+tableName+"&type="+type;
 	    AjaxRequest(str);
    	var value = response;    	
    	if(value=="no response text" && value.length==0){
    		return;
    	} 
	}
	var li=m('uploadfile_'+fileName);
	if("false" == tempFile && type == "AFFIX"){
		li.outerHTML = "<input type=hidden name=uploadDelAffix value='"+fileName+"'>";
	}else if("false" == tempFile && type == "PIC"){
		li.outerHTML = "<input type=hidden name=uploadDelPic value='"+fileName+"'>";
	}else{	
		li.outerHTML = "";
	}
}

function upload(type){
	openAttachUpload(type);
	return;	
}

var curUploadType = "";
function openAttachUpload(type){
	curUploadType = type;
	var filter = "";
	if(type == "PIC"){
		filter = "Image";
	}
	
	var attachUpload = document.getElementById("attachUpload");
	if(attachUpload == null){
		uploadDiv =document.createElement('<div id="attachUpload" style="position:absolute; top:10px;left:30px; width=600px;height:400px; display:block"></div>');
		document.form.appendChild(uploadDiv);
		attachUpload = document.getElementById('attachUpload');
	}
	attachUpload.style.left=  ((document.body.clientWidth - 500) /2) +"px";
	attachUpload.style.top=  ((document.body.clientHeight - 250) /2) +"px";
	attachUpload.style.display="block";
	attachUpload.innerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="fileUpload" width="500" height="250" codebase="http:/'+'/fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">'+
			' <param name="movie" value="/flash/FileUpload.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#869ca7" /><param name="flashvars" value="uploadUrl=/UploadServlet;jsessionid=$session.id?path=/temp/@amp;fileType='+type+'&filter='+filter+'" />'+
			' <param name="allowScriptAccess" value="sameDomain" /><embed src="/flash/FileUpload.swf" quality="high" bgcolor="#869ca7"	width="500" height="250" name="column" align="middle" play="true" loop="false"'+
			'	flashvars="uploadUrl=/UploadServlet;jsessionid=$session.id?path=/temp/@amp;fileType='+type+'&filter='+filter+'"	quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http/'+'/www.adobe.com/go/getflashplayer"></embed></object>';
} 

function onCompleteUpload(retstr){    
	retstr = decodeURIComponent(retstr); 
	if(curUploadType == 'PIC'){
	   mstrs = retstr.split(";");
	   for(i=0;i<mstrs.length;i++){
		   str = mstrs[i];
		   if(str == "") continue;
		   var ul=m('picuploadul');
		   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadpic value='"+str+"'><div><a href=\"/ReadFile?fileName="+str+"&realName="+str+"&tempFile=true&type=PIC\" target=\"_blank\"><img src=\"/ReadFile.jpg?fileName="+str+"&realName="+str+"&tempFile=true&type=PIC\" width=\"150\" height=\"150\" border=\"0\"></a></div><div>"+str+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"true\",\"workFlowDesign\",\"PIC\");'><img src='/style/images/del.gif'  title='$text.get("common.lb.del")'></a></div></li>";
		   ul.innerHTML = ul.innerHTML+imgstr;
	   }
	}else if(curUploadType == 'AFFIX'){
		mstrs = retstr.split(";");
	    for(i=0;i<mstrs.length;i++){
		   str = mstrs[i];
		   if(str == "") continue;
		   var ul=m('affixuploadul');
		   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadaffix value='"+str+"'>"+str+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"true\",\"workFlowDesign\",\"AFFIX\");'><img src='/style/images/del.gif'  title='$text.get("common.lb.del")'></a>&nbsp;&nbsp;&nbsp;<a href=\"/ReadFile?fileName="+str+"&realName="+str+"&tempFile=true&type=AFFIX\" target=_blank><img src='/style1/images/down.gif' title='$text.get("common.lb.download")'></a></li>";
		   ul.innerHTML = ul.innerHTML+	imgstr;
	    }
	}    
    var attachUpload = document.getElementById("attachUpload");
	attachUpload.style.display="none";
}

function changeType(){
	if(form.billType.value=="0"){
		document.getElementById("tempContent").innerHTML="";
	}else if(form.billType.value=="1"){
		document.getElementById("tempContent").innerHTML="<input type='hidden' name='tableName' value=''>"+
			  	"<input name='tableDisplay' type='text' onKeyDown='if(event.keyCode!=13&&event.keyCode!=9)event.returnValue=false;'  onDblClick=\"openSelects('templateFile','tableDisplay','tableSelect')\" value=\"\" maxlength=\"50\">"+
			  	"<img  style='cursor:pointer;' src='/$globals.getStylePath()/images/St.gif' onClick=\"openSelects('templateFile','tableDisplay','tableSelect')\">";
	}else{
		document.getElementById("tempContent").innerHTML="<input type='hidden' name='tableName' value=''><button type='button'  onClick='opTempFile()' />$text.get("oa.workflow.title.tempFile")</button>";
	}
}

function openSelects(fieldName,disName,selectName){
	var displayName = encodeURI("$text.get("workflow.lb.fiowtable")") 
	var tableName="";
	
	if((selectName=="titlefSelect" || selectName=="mobileContentSelect" ) 
			&& document.getElementsByName("templateFile")[0].value.length==0){
		alert('$text.get("workflow.msg.ChoiceFiowTable")');
		return false;	
	}else if(selectName=="titlefSelect" || selectName=="mobileContentSelect" ){
		tableName="&tableName="+document.getElementsByName("templateFile")[0].value;
	}

	var str  = window.showModalDialog("/UserFunctionAction.do?selectName="+selectName+tableName+"&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined")return ;
	if(selectName=="titlefSelect"){
		
		var dis="";
		if(str.length==0||(str.replaceAll(";","")).length==0){
			document.getElementsByName(disName)[0].value="";
		}else{
			var fs=str.split("|");
			for(var i=0;i<fs.length&&fs[i].length>0;i++){
				dis+=fs[i].split(";")[1]+",";
			}
			if(dis.length>0){
				document.getElementsByName(disName)[0].value=dis.substring(0,dis.length-1);
			}
		}
	}else if(fieldName=="templateFile"){		
		fs=str.split(";");  
		document.getElementsByName(fieldName)[0].value=fs[0];
		document.getElementsByName(disName)[0].value=fs[1];
		document.getElementsByName("filetitleTemp")[0].value="";
	}else if(fieldName=="fileContent"){	
		fs=str.split(";");  
		document.getElementsByName(fieldName)[0].value=fs[0];
		document.getElementsByName(disName)[0].value=fs[1];
	}
}

function openSelects2(fieldName,disName,selectName){
	var displayName = encodeURI("$text.get("workflow.lb.fiowtable")") 
	var tableName="";
	
	if( selectName=='mobileContentSelect2' && document.getElementsByName("templateFile")[0].value.length==0){
		alert('$text.get("workflow.msg.ChoiceFiowTable")');
		return false;	
	}else if(selectName=="mobileContentSelect2"){
		tableName="&tableName="+document.getElementsByName("templateFile")[0].value;
	}

	var str  = window.showModalDialog("/UserFunctionAction.do?selectName="+selectName+tableName+"&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$MOID&type=selectField&MOOP=add&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined")return ;
	var  disNames=document.getElementsByName(disName)[0].value;
	if(str==";;"){
		disNames="";
	}else{
		fs=str.split("|");	
		var len=fs.length>1?fs.length-1:fs.length;	
		for(var i=0;i<len;i++){
			if(fs[i].split(";")[1]!=""){
				disNames+="["+fs[i].split(";")[1]+"]";	
			}
		}
	}
	document.getElementsByName(disName)[0].value=disNames;
}

function showMenu(selectid){
	var id="#img_"+selectid;
	var theUl="#"+selectid;
	if($(theUl).css("display") == "block"){
		$(id).removeClass();
		$(id).addClass("column_a");
		$(theUl).hide();		
	}else{
		$(id).removeClass();
		$(id).addClass("column_b");
		$(theUl).show();
	}
}

function checkForm(){
	var flag = true;
	$("input").each(function () {
	   	if($(this).attr('required') || $(this).attr('validType')) {
		   	if(!$(this).validatebox('isValid')) {
		   		$(this).focus();
		     	 flag=false; 
		     	 return flag;
		   	}
	   }
	});
	return flag;
}

function sub(){
	var titleTemps=document.getElementsByName("filetitleTemp")[0].value;
	document.getElementsByName("titleTemp")[0].value=titleTemps;
	if(checkForm()==true){
		document.form.submit();
		$("#subButton").attr("disabled","disabled");
	}
}

function showDiv(){
	if(m('alertDivId').style.display=='block'){
		m('alertDivId').style.display='none';
		m('alertStatus').value='-1';
		
	}else{
		m('alertDivId').style.display='block';
		m('alertStatus').value='0';

	}
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none;"></iframe>
<form method="post"  scope="request" name="form" action="/OAWorkFlowTempAction.do"  class="mytable" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")"/>
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()" />
<input type="hidden" name="winCurIndex" value="$!winCurIndex" />
<input type="hidden" name="templateType" value="$!type" />
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="" />
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="" />
<input type="hidden" name="monitorUserIds" id="monitorUserIds" value="" />
<input type="hidden" name="monitorDeptIds" id="monitorDeptIds" value="" />
<input type="hidden" name="EmpGroupId" id="EmpGroupId" value="" />
<input type="hidden" name="templateFile" id="templateFile" value="" />
<input type="hidden" name="selectClass" value="$!selectClass" />
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>

	<table cellpadding="0" cellspacing="0" class="framework">
			<!--右边列表开始-->
			<td>
				<div class="TopTitle">
					<span><img src="/style1/images/ti_002.gif" /></span>
					<span>添加工作流</span>
					
					<div>		
						<input type="button" class="bu_02" onclick="sub();"  id="subButton"  value="保存" />
						<input type="button" class="bu_02" onclick="location.href='/OAWorkFlowTempQueryAction.do?operation=4&winCurIndex=$!winCurIndex'"  value="返回" />	
					</div>
				</div>
				<div id="cc" class="data" style="overflow:hidden;overflow-y:auto;width:100%;">
<script type="text/javascript">
	var oDiv=document.getElementById("cc");
	var sHeight=document.documentElement.clientHeight-55;
	oDiv.style.height=sHeight+"px";
</script>
					<div class="column_title" onclick="showMenu('a');"><div class="column_b"  id="img_a" >流程基本属性</div></div>
					<ul class="column" id="a" style="display: block;">
						<li><label>$text.get("oa.workflow.lb.tempName")：</label>
							<span>
								<input  name="templateName" type="text" onKeyPress="if(event.keyCode == 13){mainmd();}" 	class="easyui-validatebox" required="true" />
								<img style='cursor:pointer;h'src='/$globals.getStylePath()/images/St.gif' onClick="mainmd();"/><font color="#FF0000">*</font>
							</span>
						</li>
						<li><label>$text.get("oa.workflow.lb.tempClass")：</label>
							<span>
								<select name="templateClass" onKeyDown="if(event.keyCode==13) event.keyCode=9" 	class="easyui-validatebox" required="true" >
								#foreach($row in $typeListbycata)
									<option id="tempClass" value="$globals.get($row,1)" #if("$!globals.get($row,2)"=="$!selectClass") selected="selected"  #end>
										$globals.get($row,2)
									</option>
								#end
								</select><font color="#FF0000">*</font>
							</span>
						</li>
						<li><label>$text.get("oaWorkFlow.lb.flowOrder")：</label>
							<span>
								<input name="flowOrder"  type="text"
									 onKeyDown="if(event.keyCode==13) event.keyCode=9" value="$floworder" 	class="easyui-validatebox" required="true" /><font color="#FF0000">*</font>
							</span>
						</li>
						<li><label>$text.get("oa.workflow.lb.tempStatus")：</label>
							<span>
								<select  name="templateStatus"
									onKeyDown="if(event.keyCode==13) event.keyCode=9">
									<option value="1">
										$text.get("wokeflow.lb.enable")
									</option>
									<option value="0">
										$text.get("wokeflow.lb.noEnable")
									</option>
								</select>
							</span>
						</li>
						<li><label>$text.get("workflow.lb.fiowtable")：</label>
							<span>
								<input type='hidden' name='templateFile' value='' />
								<input name='tableDisplay'  type='text' class="text"
									onKeyDown='if(event.keyCode!=13&&event.keyCode!=9)event.returnValue=false;'
									onDblClick="openSelects('templateFile','tableDisplay','tableSelect')" value="" />
								<img style='cursor:pointer;' src='/$globals.getStylePath()/images/St.gif'
									onClick="openSelects('templateFile','tableDisplay','tableSelect')" />
								<input type="button" class="bu_02"  style="margin-left: 10px;" onclick="opTempFile();" value="$text.get("oa.workflow.title.tempFile")" />
								<input type="button" class="bu_02" onclick="opTempFileNew();" value="$text.get("workflow.lb.newModle")" />
							</span>
						</li>
					</ul>
					<div class="column_title"  onclick="showMenu('f');"><div class="column_b" id="img_f" >模板信息</div></div>
					<ul class="column" id="f" style="display:block;">
						<li style="width:98%;" ><label>$text.get("oa.workflow.TopicModule")：</label>
							<span>
								<input  type="hidden"  name="titleTemp"/>
								<input name="filetitleTemp" style="width:400px" type='text' class="text"
									onDblClick="openSelects2('titleTemp','filetitleTemp','mobileContentSelect2')"  class="easyui-validatebox" required="true"/>
								<img style='cursor:pointer;'
									src='/$globals.getStylePath()/images/St.gif'
									onClick="openSelects2('titleTemp','filetitleTemp','mobileContentSelect2')" />
								<font color="red">* &nbsp;</font><font color="dimgray">例子：([请假人]由于[请假主题]需要请假)
									&nbsp;注：[请假人],[请假主题]是表单设计中的字段</font>
							</span>
						</li>
						<li style="width:80%;"><label>模板内容字段：</label>
							<span>
									<input type='hidden' name='fileContent' />
								<input name='fileContentDisplay' style="width:400px" type='text'
									class="text"
									onDblClick="openSelects('fileContent','fileContentDisplay','mobileContentSelect')" />
								<img style='cursor:pointer;'
									src='/$globals.getStylePath()/images/St.gif'
									onClick="openSelects('fileContent','fileContentDisplay','mobileContentSelect')" />
								<font color="dimgray">(注：在手机版标识工作流内容的显示字段)</font>
							</span>
						</li> 
						
					</ul>
					<div class="column_title"  onclick="showMenu('d');"><div class="column_b" id="img_d" >流程用户及监控</div></div>
					<div  id="d" style="display: block;">
					<table width="100%" >
						<tr>
							<td width="133px" style="vertical-align: middle;" align="right">
							$text.get("oa.workflow.lb.allowVisitor")：








							</td>
							<td >
								<div class="oa_signDocument1" id="_context">
									<div class="oa_signDocument_3">
										<img src="/$globals.getStylePath()/images/St.gif"
											onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');"
											alt="$text.get("oa.common.adviceDept")" class="search" />
										&nbsp;
										<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');">$text.get("oa.select.dept")</a>
									</div>
									<select name="formatDeptName" id="formatDeptName"
										multiple="multiple">
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="delOpation('formatDeptName','popedomDeptIds')" style="cursor:pointer"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
								</div>
								<div class="oa_signDocument1" id="_context">
									<div class="oa_signDocument_3">
										<img src="/$globals.getStylePath()/images/St.gif"
											onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');"
											alt="$text.get("oa.common.advicePerson")" class="search" />
										&nbsp;
										<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');">$text.get("oa.select.personal")</a>
									</div>
									<select name="formatFileName" id="formatFileName"
										multiple="multiple">
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="delOpation('formatFileName','popedomUserIds')" style="cursor:pointer"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
								</div>

								<div class="oa_signDocument1" id="_context">
									<div class="oa_signDocument_3">
										<img src="/$globals.getStylePath()/images/St.gif"
											onClick="deptPop('empGroup','EmpGroup','EmpGroupId','1');"
											alt="$text.get("oa.oamessage.usergroup")" class="search" />
										&nbsp;
										<a href="javascript:void(0)" style="cursor:pointer"
											title="$text.get('oa.oamessage.usergroup')"
											onClick="deptPop('empGroup','EmpGroup','EmpGroupId','1');">$text.get("oa.oamessage.usergroup")</a>
									</div>
									<select name="EmpGroup" id="EmpGroup" multiple="multiple">
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="delOpation('EmpGroup','EmpGroupId')" style="cursor:pointer"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
								</div>
							</td>
							
						</tr>
						<tr>
							<td style="vertical-align: middle;" align="right">
							$text.get("workflow.lb.monitor")
							</td>
							<td>
									<div class="oa_signDocument1" id="_context">
									<select style="width:135px;25px" name="depMonitorScope"
										onKeyDown="if(event.keyCode==13) event.keyCode=9">
										<option value="0">
											$text.get("workflow.lb.MonitorEntire")
										</option>
										<option value="1">
											$text.get("workflow.lb.MonitorDept")
										</option>
										<option value="2">
											$text.get("workflow.lb.MonitorFatherDept")
										</option>
									</select>
									<div class="oa_signDocument_3">
										<img src="/$globals.getStylePath()/images/St.gif"
											onClick="deptPop('deptGroup','depMonitor','monitorDeptIds','1');"
											alt="$text.get("oa.common.adviceDept")" class="search" />
										&nbsp;
										<a href="javascript:void(0)" title="选择监控部门" onClick="deptPop('deptGroup','depMonitor','monitorDeptIds','1');">$text.get("workflow.lb.AllowMonitorDept")</a>
									</div>
									<select name="depMonitor" id="depMonitor" multiple="multiple">
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="delOpation('depMonitor','monitorDeptIds')"  style="cursor:pointer"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
								</div>

								<div class="oa_signDocument1" id="_context">
									<select style="width:135px;25px" name="perMonitorScope"
										onKeyDown="if(event.keyCode==13) event.keyCode=9">
										<option value="0">
											$text.get("workflow.lb.MonitorEntire")
										</option>
										<option value="1">
											$text.get("workflow.lb.MonitorDept")
										</option>
										<option value="2">
											$text.get("workflow.lb.MonitorFatherDept")
										</option>
									</select>
									<div class="oa_signDocument_3">
										<img src="/$globals.getStylePath()/images/St.gif"
											onClick="deptPop('userGroup','perMonitor','monitorUserIds','1');"
											alt="$text.get("oa.common.advicePerson")" class="search" />
										&nbsp;
										<a href="javascript:void(0)" title="选择监控人员" onClick="deptPop('userGroup','perMonitor','monitorUserIds','1');">$text.get("workflow.lb.AllowMonitorEmp")</a>
									</div>
									<select name="perMonitor" id="perMonitor" multiple="multiple">
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="delOpation('perMonitor','monitorUserIds')"  style="cursor:pointer"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
								</div>
							</td>
						</tr>
						<tr>
							<td></td>
							<td><input type="checkbox" name="autoPass" value="true"/> $text.get("workflow.msg.AutomatismFiltration")</td>
						</tr>
					</table>
				
					</div>
					
					<div class="column_title" onclick="showMenu('e');"><div class="column_a" id="img_e" >流程提醒</div></div>
					<div id="e" style="diplay:block;">
					<table  width="100%" style="padding: 5px;">
						<tr>
						<td style="vertical-align: middle;" width="133px"  align="right" > 分发提醒：</td>
						<td><li>
								<span style="width:85px;float:left;">&nbsp;&nbsp;</span>
									#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
										<input type="checkbox" name="dispenseWake" value="$row_wakeUpType.value" />$row_wakeUpType.name 
									#end								
							</li>
						</td>
						</tr>
						<tr>
						<td style="vertical-align: middle;" width="133px"  align="right" > 节点提醒：</td>
						<td>
							    <li>
									<span style="width:200px;text-align:right;">$text.get("workflow.lb.nextNote")</font></span>
										#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
											<input type="checkbox" name="nextWake" value="$row_wakeUpType.value" #if($row_wakeUpType.value=="4")checked#end />
											$row_wakeUpType.name 
										#end								
								</li>
								<li>
									<span style="width:200px;text-align:right;"><font style="margin-left: 36px;">$text.get("workflow.lb.startNote")</font></span>									 
										#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
										    <input type="checkbox" name="startWake" value="$row_wakeUpType.value" />
											$row_wakeUpType.name 
										#end						
								</li>
								<li>
									<span style="width:200px;text-align:right;"><font style="margin-left: 12px;">$text.get("workflow.lb.allNote")</font></span>
										#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
											<input type="checkbox" name="allWake" value="$row_wakeUpType.value" />
											$row_wakeUpType.name 
										#end
								</li>
								<li>
									<span style="width:200px;text-align:right;border-top:1px ridge dashed #CCCCCC"><font style="margin-left: 12px;">$text.get("workflow.lb.setNote")</font></span>
										#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode")) 
											<input type="checkbox" name="setWake" value="$row_wakeUpType.value" />
											$row_wakeUpType.name
										#end									
								</li>
							</td>
							</tr>
							<tr>
								<td style="vertical-align: middle;" width="133px"  align="right" > 提醒对象：</td>
								<td>		
								<li style="margin-top:5px">
									<span style="border-top:1px ridge dashed #CCCCCC">
										<div class="oa_signDocument1" id="_context" >
											<input type="hidden" id="setWakeDept" name="setWakeDept" />
											<div class="oa_signDocument_3">
												<img src="/$globals.getStylePath()/images/St.gif"
													onClick="deptPop('deptGroup','setWakeDeptName','setWakeDept','1');"
													alt="$text.get("oa.common.adviceDept")" class="search" />
												&nbsp;
												<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','setWakeDeptName','setWakeDept','1');">$text.get("oa.select.dept")</a>
											</div>
											<select name="setWakeDeptName" id="setWakeDeptName"
												multiple="multiple">
											</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="delOpation('setWakeDeptName','setWakeDept')"  style="cursor:pointer"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
										</div>
										<div class="oa_signDocument1" id="_context" >
											<input type="hidden" id="setWakePer" name="setWakePer" />
											<div class="oa_signDocument_3">
												<img src="/$globals.getStylePath()/images/St.gif"
													onClick="deptPop('userGroup','setWakePerName','setWakePer','1');"
													alt="$text.get("oa.common.advicePerson")" class="search" />
												&nbsp;
												<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','setWakePerName','setWakePer','1');">$text.get("oa.select.personal")</a>
											</div>
											<select name="setWakePerName" id="setWakePerName"
												multiple="multiple">
											</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="delOpation('setWakePerName','setWakePer')"  style="cursor:pointer"
												src="/$globals.getStylePath()/images/delete_button.gif" 
												alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
										</div>
										<div class="oa_signDocument1" id="_context" >
											<input type="hidden" id="setWakeGroup" name="setWakeGroup" />
											<div class="oa_signDocument_3">
												<img src="/$globals.getStylePath()/images/St.gif"
													onClick="deptPop('empGroup','setWakeGroupName','setWakeGroup','1');"
													alt="$text.get("oa.oamessage.usergroup")" class="search" />
												&nbsp;
												<a href="javascript:void(0)" style="cursor:pointer"
													title="$text.get('oa.oamessage.usergroup')"
													onClick="deptPop('empGroup','setWakeGroupName','setWakeGroup','1');">$text.get("oa.oamessage.usergroup")</a>
											</div>
											<select name="setWakeGroupName" id="setWakeGroupName"
												multiple="multiple">
											</select>
										</div>
										<div class="oa_signDocument2" >
											<img onClick="delOpation('setWakeGroupName','setWakeGroup')"  style="cursor:pointer"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
										</div>
								</li>
						</td>	
						</tr>
						<tr style="border-top-style: dotted;border-width: 2px;border-color: red;">
						<td  style="vertical-align: middle;" align="right"> 结束提醒：</td>
						<td >
								<li>
									<span style="width:120px;text-align:right;"><font style="margin-left: 36px;">$text.get("workflow.lb.startNote")</font></span>
									#foreach($row_wakeUpType in
									$globals.getEnumerationItems("WakeUpMode"))
									<input type="checkbox" name="stopStartWake"
										value="$row_wakeUpType.value" #if($row_wakeUpType.value=="4")checked#end />
									$row_wakeUpType.name #end
								</li>
								<li>
									<span style="width:120px;text-align:right"><font style="margin-left: 12px;">$text.get("workflow.lb.allNote")</font></span>
									#foreach($row_wakeUpType in
									$globals.getEnumerationItems("WakeUpMode"))
									<input type="checkbox" name="stopSAllWake"
										value="$row_wakeUpType.value" />
									$row_wakeUpType.name #end
								</li>
								<li>
										<span style="width:200px;text-align:right;border-top:1px ridge dashed #CCCCCC"><font style="margin-left: 12px;">$text.get("workflow.lb.setNote")</font></span>
										#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode")) 
											<input type="checkbox" name="stopSetWake" value="$row_wakeUpType.value" />
											$row_wakeUpType.name
										#end
								</li>
								</td>
								</tr>
								<tr>
								<td  style="vertical-align: middle;" align="right"> 提醒对象：</td>
							<td>
								<li style="margin-top:5px">
									
									<span style="border-top:1px ridge dashed #CCCCCC">
										
										<div class="oa_signDocument1" id="_context">
											<input type="hidden" id="stopSetWakeDept" name="stopSetWakeDept" />
											<div class="oa_signDocument_3">
												<img src="/$globals.getStylePath()/images/St.gif"
													onClick="deptPop('deptGroup','stopSetWakeDeptName','stopSetWakeDept','1');"
													alt="$text.get("oa.common.adviceDept")" class="search" />
												&nbsp;
												<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','stopSetWakeDeptName','stopSetWakeDept','1');">$text.get("oa.select.dept")</a>
											</div>
											<select name="stopSetWakeDeptName" id="stopSetWakeDeptName"
												multiple="multiple">
											</select>
										</div>
										<div class="oa_signDocument2"  >
											<img
												onClick="delOpation('stopSetWakeDeptName','stopSetWakeDept')"  style="cursor:pointer"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
										</div>
										<div class="oa_signDocument1" id="_context">
											<input type="hidden" id="stopSetWakePer" name="stopSetWakePer" />
											<div class="oa_signDocument_3">
												<img src="/$globals.getStylePath()/images/St.gif"
													onClick="deptPop('userGroup','stopSetWakePerName','stopSetWakePer','1');"
													alt="$text.get("oa.common.advicePerson")" class="search" />
												&nbsp;
												<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','stopSetWakePerName','stopSetWakePer','1');">$text.get("oa.select.personal")</a>
											</div>
											<select name="stopSetWakePerName" id="stopSetWakePerName"
												multiple="multiple">
											</select>
										</div>
										<div class="oa_signDocument2" >
											<img
												onClick="delOpation('stopSetWakePerName','stopSetWakePer')"  style="cursor:pointer"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
										</div>
										<div class="oa_signDocument1" id="_context" >
											<input type="hidden" id="stopSetWakeGroup" name="stopSetWakeGroup" />
											<div class="oa_signDocument_3">
												<img src="/$globals.getStylePath()/images/St.gif"
													onClick="deptPop('empGroup','stopSetWakeGroupName','stopSetWakeGroup','1');"
													alt="$text.get("oa.oamessage.usergroup")" class="search" />
												&nbsp;
												<a href="javascript:void(0)" style="cursor:pointer"
													title="$text.get('oa.oamessage.usergroup')"
													onClick="deptPop('empGroup','stopSetWakeGroupName','stopSetWakeGroup','1');">$text.get("oa.oamessage.usergroup")</a>
											</div>
											<select name="stopSetWakeGroupName" id="stopSetWakeGroupName"
												multiple="multiple">
											</select>
										</div>
										<div class="oa_signDocument2"  >
											<img
												onClick="delOpation('stopSetWakeGroupName','stopSetWakeGroup')"  style="cursor:pointer"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
										</div>
								</li> 
						</td>
						</tr>
					</table>
					</div>
						<div class="column_title" onclick="showMenu('b');"><div class="column_b"  id="img_b" >流程时段</div></div>
					<ul class="column" id="b" style="display: block;">
						<li><label>$text.get("oa.workflow.lb.usefulLife")：</label>
							<span>
								<input type="text" class="text"
									onClick="WdatePicker({lang:'$globals.getLocale()'});"  name="usefulLifeS" />
								$text.get("oa.workflow.lb.to")
								<input type="text" class="text"
									onClick="WdatePicker({lang:'$globals.getLocale()'});"  name="usefulLifeE" />
										<input type="hidden" id="alertStatus" name="alertStatus" value="-1" />
								
							</span>
						<li><label>$text.get("oa.workflow.lb.allowAdd")：</label>
							<span>
								<input type="radio" name="allowAdd" value="1" checked />
								$text.get("excel.list.yes")
								</radio>
								&nbsp;&nbsp;
								<input type="radio" name="allowAdd" value="0" />
								$text.get("excel.list.no")
								</radio>
							</span>
						</li>
						<li><label><input type="button" class="bu_02" onclick="showDiv();" value="$text.get("workflow.msg.warmsetting")" /></label></li>
							<ul>
							<div id="alertDivId"
										style="border:1px; margin-left: 160px; width: 520px;display: none;">
										<table border="0" width="90%" cellpadding="0" cellspacing="0"
											class="oalistRange_list_function" name="table">
											<tr>
												<td height="5" colspan="4" bgcolor="#D7EBFF">
													<strong>$text.get("workflow.msg.warmsetting")</strong>
												</td>
											</tr>
											<tr>
												<td align="right" valign="middle" bgcolor="#F5F5F5">
													<font color="red">*</font>$text.get("workflow.lb.standaloneNote")：








												</td>
												<td valign="top" colspan="3">
													#foreach($wake in $globals.getEnumerationItems("WakeUpMode"))
														#if("$wake.value"=="4")
															<input type="checkbox" name="alertType" checked  value="$wake.value" />
														#else
															<input type="checkbox" name="alertType" value="$wake.value" />
														#end
													$wake.name #end
												</td>
											</tr>
											<tr>
												<td align="right" valign="middle" bgcolor="#F5F5F5">
													<font color="red">*</font>$text.get("workflow.msg.warmtime")：








												</td>
												<td valign="top" colspan="3">
													<input name="alertDate" type="text" class="text2" size="40"
														style="width:80px"
														onKeyDown="if(event.keyCode==13){event.keyCode=9;}"
														onClick="WdatePicker({lang:'$globals.getLocale()'});"
														value="" />
													<select name="alertHour">
														#foreach($hour in [0..23]) #if($hour<10)
														<option value="$hour">
															0$hour
														</option>
														#else
														<option value="$hour">
															$hour
														</option>
														#end #end
													</select>
													&nbsp;&nbsp;$text.get("oa.calendar.hour")
													<select name="alertMinute">
														<option value="05">
															05
														</option>
														<option value="10">
															10
														</option>
														<option value="15">
															15
														</option>
														<option value="20">
															20
														</option>
														<option value="25">
															25
														</option>
														<option value="30">
															30
														</option>
														<option value="35">
															35
														</option>
														<option value="40">
															40
														</option>
														<option value="45">
															45
														</option>
														<option value="50">
															50
														</option>
														<option value="55">
															55
														</option>
													</select>
													&nbsp; $text.get("oa.calendar.minutes")
												</td>
											</tr>
											<tr>
												<td align="right" valign="top" bgcolor="#F5F5F5">
													<font color="red">*</font>$text.get("common.msg.center")
												</td>
												<td valign="top" colspan="3">
													<textarea name="alertContent" cols="30" rows="6"></textarea>
												</td>
											</tr>
											<tr>
												<td align="right" valign="top" bgcolor="#F5F5F5">
													$text.get("workflow.msg.whileset")：








												</td>
												<td valign="top" colspan="3">
													&nbsp;$text.get("workflow.msg.warmcyclsi")：








													<input type="radio" name="isLoop" value="no"
														checked="checked"
														onClick="javascript:m('loopId').style.display='none';" />
													$text.get("common.lb.no")
													<input type="radio" name="isLoop" value="yes"
														onClick="javascript:m('loopId').style.display='block';" />
													$text.get("common.lb.yes")
													<br />
													<div id="loopId"
														style="display: none;border:1px solid #A2CEF5;width: 350px;padding-bottom: 5px;">
														&nbsp;$text.get("workflow.msg.cycleSet")：








														<br />
														<div
															style="border:1px solid #A2CEF5;margin-left: 40px;width: 300px;">
															<input type="radio" name="loopType" value="day"
																checked="checked"
																onClick="javascript:m('loopName').innerHTML='$text.get("oa.calendar.day") '"/>
															$text.get("workflow.lb.byday")
															<input type="radio" name="loopType" value="week"
																onClick="javascript:m('loopName').innerHTML='$text.get("oa.calendar.week") '"/>
															$text.get("workflow.lb.byweek")
															<input type="radio" name="loopType" value="month"
																onClick="javascript:m('loopName').innerHTML='$text.get("oa.calendar.month") '"/>
															$text.get("workflow.lb.bymonth")
															<input type="radio" name="loopType" value="year"
																onClick="javascript:m('loopName').innerHTML='$text.get("oa.calendar.year") '"/>
															$text.get("workflow.lb.byyear")
															<br />
															&nbsp;$text.get("workflow.msg.partition")
															<input type="text" name="loopTime" class="text2"
																style="width: 30px;text-align: center;" value="1" />
															<label id="loopName">
																$text.get("oa.calendar.day")
															</label>
															$text.get("workflow.lb.warmonce")

														</div>
														&nbsp;$text.get("workflow.lb.endtimesetting")：








														<br />
														<div
															style="border:1px solid #A2CEF5;margin-left: 40px;width: 300px;">
															<input type="radio" name="hasEndDate" checked="checked"
																value="false"
																onClick="javascript:m('endDate2').value='';m('endDate2').disabled=true;" />
															$text.get("workflow.lb.noendtime")
															<br />
															<input type="radio" name="hasEndDate" id="endDateId"
																value="true"
																onClick="javascript:m('endDate2').disabled=false;" />
															$text.get("common.lb.endDate")：








															<input id="endDate2" name="endDate" disabled="disabled"
																type="text" class="text2" size="40" style="width:80px"
																onKeyDown="if(event.keyCode==13){event.keyCode=9;}"
																onClick="WdatePicker({lang:'$globals.getLocale()'});"
																value="" />
														</div>
													</div>
												</td>
											</tr>
										</table>
								</div>
								</ul>
						
						</ul>
					
					<div class="column_title" onclick="showMenu('h');"><div class="column_b" id="img_h" >备注</div></div>
					<ul class="column" id="h" style="display: block;">
						<li style="margin-bottom: 30px; width: 80%;;height: 80px;"　　><label>流程说明：</label>
							<span><textarea name="detail" style="width:70%;height: 100px;" rows="4"></textarea>
							</span>
						</li>
						<li style="width:80%"><label>	
								<a href="javascript:void(0)"
									onclick="upload('AFFIX');">&nbsp;
									<img src="/style1/images/oaimages/uploading.gif" /><font style="margin-left: 2px;">说明附件</font></a>
								</label>
							<span >						
								<ul id="affixuploadul">
								</ul>
							</span>
						</li>
					
					</ul>
			
				</div>
			</td>
		</tr>
	</table>
</form>
</body>
</html>