<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("user.lb.userAdmin")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript">

putValidateItem("templateName","$text.get("oa.workflow.lb.tempName")","any","",false,0,100);
#if("$!type" != "1")
putValidateItem("templateFile","$text.get("oa.workflow.lb.tempFile")","any","",false,0,100);	
#end

function beforSubmit(form){
	if(!validate(form))return false;
	   
	//先进行逻辑验证表单设计和流程设计是否符合规范




    var url="/UtilServlet?operation=workFlowIsStand&tempId=$!globals.get($result,0)";
    AjaxRequest(url) ;
    if("false"==response&&!confirm("$text.get("oa.workflow.designNoStand")")){
    	if(typeof(top.junblockUI)!="undefined"){
			top.junblockUI();
		}
    	return false;
    }
    var isUsed=document.getElementsByName("templateStatus")[0].value; 
    var userVal=form.popedomUserIds.value.replaceAll(",","");
    var deptVal=form.popedomDeptIds.value.replaceAll(",","");
    
    if(("false"==response)&&isUsed=="1"){
    	alert("$text.get("workFlow.msg.cannotUseFlow")");
    	if(typeof(top.junblockUI)!="undefined"){
			top.junblockUI();
		}
    	return false;
    }
    if(((userVal.length==0&&deptVal.length==0))&&isUsed=="1"){
    	alert("反审核人未设置");
    	if(typeof(top.junblockUI)!="undefined"){
			top.junblockUI();
		}
    	return false;
    }
    
	disableForm(form);
	return true;
}

function opTempFile(){
	var width = screen.availWidth;
	var height = screen.availHeight;
	#if("$!type" == "1")
		var tableName=document.getElementsByName("templateFile")[0];
		if(tableName.value.length>0){
			var file = window.showModalDialog("/WorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId="+tableName.value+"&type=simple","","dialogWidth:"+width+"px;dialogHeight:"+height+"px;center:yes;help:no;resizable:no;status:no;scroll:no;");
		}else{
			alert("$text.get("workfolw.msg.selectModle")");
			return false;
		}
	#else
		var file = window.showModalDialog("/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex&type=$!type&openUpload=openUpload&templateFile="+document.form.templateFile.value,"","dialogWidth:"+width+"px;dialogHeight:"+height+"px;center:yes;help:no;resizable:no;status:no;scroll:no;");
		if(file){ 
			document.form.templateFile.value=file;
		}
	#end
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
		   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadpic value='"+str+"'><div><a href=\"/ReadFile?fileName="+str+"&realName="+str+"&tempFile=true&type=PIC\" target=\"_blank\"><img src=\"/ReadFile.jpg?fileName="+str+"&realName="+str+"&tempFile=true&type=PIC\" width=\"150\" height=\"150\" border=\"0\"></a></div><div>"+str+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"true\",\"$tableName\",\"PIC\");'>$text.get("common.lb.del")</a></div></li>";
		   ul.innerHTML = ul.innerHTML+imgstr;
	   }
	}else if(curUploadType == 'AFFIX'){
		mstrs = retstr.split(";");
	    for(i=0;i<mstrs.length;i++){
		   str = mstrs[i];
		   if(str == "") continue;
		   var ul=m('affixuploadul');
		   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadaffix value='"+str+"'>"+str+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"true\",\"$tableName\",\"AFFIX\");'>$text.get("common.lb.del")</a>&nbsp;&nbsp;&nbsp;<a href=\"/ReadFile?fileName="+str+"&realName="+str+"&tempFile=true&type=AFFIX\" target=_blank>$text.get("common.lb.download")</a></li>";
		   ul.innerHTML = ul.innerHTML+	imgstr;
	    }
	}    
    var attachUpload = document.getElementById("attachUpload");
	attachUpload.style.display="none";
}

var globalFileName="";
function openFlowSet(){

	var workflowFile="$!workFlowDesignBeans.get($!globals.get($result,0))";
	var newCreate="false";
	if(workflowFile=="" && globalFileName==""){ //如果没有流程设计文件（xml文件），则为新建
		newCreate="true";
	}
	var str="/UtilServlet?operation=BillNotFinish&keyId=$!globals.get($result,0)";
 	AjaxRequest(str);
    var value = response;    	
    if(value!="no response text" && value.length>0){
    	if(confirm("存在未审核的单据，不能修改流程步骤！\n\n是否创建新的流程版本？")){
    		location.href="/ERPWorkFlowTempAction.do?operation=1&flowType=erp&type=copy&winCurIndex=$!winCurIndex&keyId=$!globals.get($result,0)" ;
    		return ;
    	}
    }
    
	var width=screen.width ;
	var height=screen.height;
	var url="/vm/oa/workflow/workFlowDesign.jsp?ip=$host.replace('|',':')&workFileName=$!{globals.get($result,0)}.xml&tableName="+document.getElementById("templateFile").value+"&newCreate="+newCreate;
	window.open(url);
}

function mainmd(){
   var mlf = document.form.templateName;
   var str  = window.showModalDialog("/common/moreLanguage.jsp?len=50&str="+mlf.value,"","dialogWidth=730px;dialogHeight=450px");
   mlf.value = str;
}

function openFlowVersion(){
	var urlstr = "/OAWorkFlowTempQueryAction.do?queryType=version&designId=$!globals.get($result,0)&tableName=$!tableName&versionType=flow";
	asyncbox.open({
		id:'flowVersion',
		title:'流程版本',
		url:urlstr,
		width:500,
		height:400
	});
}

//弹出框回填内容

function fillData(datas){	
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	jQuery.close('Popdiv');
}

//流程设计页面关闭后，调用此方法




function repVal(fileName){
	jQuery.ajax({
		type: "POST",
	    url: "/OAWorkFlowTempAction.do",
		data: "operation=7&isExistFile=true&fileName="+fileName+"",
		async: false,
	    success: function(msg){
			if(msg=="true"){
				$("#workFlowFile").val(fileName);
				globalFileName=fileName;
			}
		}
	});
}

</script>
<style type="text/css">
.oalistrange_list_function span{display:inline-block;}
a{cursor: pointer;}
</style>
</head>

<body onLoad="showStatus();">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/ERPWorkFlowTempAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/>
<input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
<input type="hidden" name="templateType" value="$!type"/>
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="$!targetUserStr"/>
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="$!targetDeptStr"/>
<input type="hidden" name="EmpGroupId" id="EmpGroupId" value="$!targetEmpGroupStr"/>
<input type="hidden" name="monitorDeptIds" id="monitorDeptIds" value="$!globals.get($result,15)"/>
<input type="hidden" name="monitorUserIds" id="monitorUserIds" value="$!globals.get($result,16)"/>
<input type="hidden" name="templateFile" id="templateFile" value="$!globals.get($result,4)"/>
<input type="hidden" name="workFlowFile" id="workFlowFile" value="$!workFileName"/>
<input type="hidden" name="id" id="id" value="$!globals.get($result,0)"/>
<input type="hidden" name="templateClass" id="templateClass" value="00001"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.workflow.title.updateTemp")</div>
	<ul class="HeadingButton">	  
	#if($globals.getMOperation().add())
	  <li><button type="button" onClick="if(beforSubmit(document.form)){window.save=true; document.form.winCurIndex.value='$!winCurIndex';document.form.submit();}" class="b2">$text.get("mrp.lb.save")</button></li>
	#end
	#if($globals.getMOperation().query())
	  <li><button type="button" name="backList" onClick="window.save=false;location.href='/ERPWorkFlowTempSearchAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
	#end
	</ul>
</div>
<div id="listRange_id">
<script type="text/javascript"> 
var oDiv=document.getElementById("listRange_id");
var sHeight=document.documentElement.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tc">#if("$!type" == "1")
		$text.get("oa.workflow.lb.commonTemp")
		#elseif("$!type" == "2")
		$text.get("oa.workflow.lb.wordTemp")
		#elseif("$!type" == "3")
		$text.get("oa.workflow.lb.xlsTemp")
		#end</td>
			</tr>
		</thead>
				<tbody>
		<tr><td width="10%" align="right">
		<span>$text.get("oa.workflow.lb.tempName")：</span></td><td><input name="templateName"  style="color: #C0C0C0;" readonly="readonly" type="text" onKeyPress="if(event.keyCode == 13){mainmd();}" value="$!globals.get($result,1)" class="text" style="width:135px"></td></tr>
		<tr><td width="10%" align="right"><span>$text.get("oa.workflow.lb.tempStatus")：</span></td><td>
		<select name="templateStatus" style="width:135px"  onKeyDown="if(event.keyCode==13) event.keyCode=9">
			<option value="1" #if("$!globals.get($result,7)"=="1") selected #end>$text.get("wokeflow.lb.enable")</option>
			<option value="0" #if("$!globals.get($result,7)"=="0") selected #end>$text.get("wokeflow.lb.noEnable")</option>
					
		</select>
		</td></tr>
		<tr><td width="10%" align="right"><span>$text.get("oa.workflow.lb.workflowSet")：</span></td><td>
		<button type="button"  onClick="openFlowSet()" >$text.get("oa.workflow.lb.workflowSet")</button>
		&nbsp;&nbsp; 
		#if("$!globals.get($result,0)"!="$!globals.get($result,39)")
		<button onClick="openFlowVersion();" type="button">流程版本</button>
		#end
		</td></tr>
		<tr><td width="10%" align="right"><span>$text.get("oa.calendar.wakeupType")：</td>
				<td width="90%">
					<li><span style="width:90px;text-align:right">分发提醒：</span>
					#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
						<input type="checkbox" name="dispenseWake" value="$row_wakeUpType.value" #if($!globals.get($result,41).indexOf("$row_wakeUpType.value")>=0)checked #end/>$row_wakeUpType.name 
					#end	
	   				</li>
					<li><span style="width:90px;text-align:right">$text.get("workflow.lb.nextNote")</span>
					#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
		 			<input type="checkbox" name="nextWake" value="$row_wakeUpType.value" #if($!globals.get($result,18).indexOf("$row_wakeUpType.value")>=0)checked #end />$row_wakeUpType.name
	   				#end
	   				</li>
	   				<li><span style="width:90px;text-align:right">$text.get("workflow.lb.startNote")</span>
					#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
		 			<input type="checkbox" name="startWake" value="$row_wakeUpType.value" #if($!globals.get($result,19).indexOf("$row_wakeUpType.value")>=0)checked #end />$row_wakeUpType.name
	   				#end
	   				</li>
	   				<li><span style="width:90px;text-align:right">$text.get("workflow.lb.allNote")</span>
	   				#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
		 			<input type="checkbox" name="allWake" value="$row_wakeUpType.value" #if($!globals.get($result,20).indexOf("$row_wakeUpType.value")>=0)checked #end />$row_wakeUpType.name
	   				#end
	   				</li>
	   				<li style="margin-top:1px">
            <div style="padding:0;overflow:hidden;">
	   				<span style="width:90px;text-align:right;border-top:1px ridge dashed #CCCCCC">$text.get("workflow.lb.setNote")</span>
					<span style="border-top:1px ridge dashed #CCCCCC">
					#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
		 			<input type="checkbox" name="setWake" value="$row_wakeUpType.value" #if($!globals.get($result,21).indexOf("$row_wakeUpType.value")>=0)checked #end />$row_wakeUpType.name
	   				#end
	   				</span>
            </div>
	   				<div class="oa_signDocument1" id="_context">
	   				<input type="hidden" name="setWakeDept" id="setWakeDept" value="$!globals.get($result,22)">
					<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('deptGroup','setWakeDeptName','setWakeDept','1');" alt="$text.get("oa.common.adviceDept")" class="search">&nbsp;<a  title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','setWakeDeptName','setWakeDept','1');">$text.get("oa.select.dept")</a></div>
					<select name="setWakeDeptName" id="setWakeDeptName" multiple="multiple">
					#foreach($dept in $setWakeDept)
					<option value="$dept.classCode">$!dept.DeptFullName</option>
					#end
					</select></div>
					<div class="oa_signDocument2">
					<img onClick="deleteOpation('setWakeDeptName','setWakeDept')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
					</div>
					<div class="oa_signDocument1" id="_context">
					<input type="hidden" name="setWakePer" id="setWakePer" value="$!globals.get($result,23)">
					<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('userGroup','setWakePerName','setWakePer','1');" alt="$text.get("oa.common.advicePerson")" class="search">&nbsp;<a title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','setWakePerName','setWakePer','1');">$text.get("oa.select.personal")</a></div>
					<select name="setWakePerName" id="setWakePerName" multiple="multiple">
					#foreach($user in $setWakePer)
					<option value="$user.id">$!user.EmpFullName</option>
					#end
					</select></div>
					<div class="oa_signDocument2">
					<img onClick="deleteOpation('setWakePerName','setWakePer')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
					</div>
	   				</li>
				</td>
			</tr>
			<tr><td width="10%" align="right"><span>$text.get("oa.workflow.FlowEndAwokeMode")</td>
				<td width="90%">
	   				<li><span style="width:90px;text-align:right">$text.get("workflow.lb.startNote")</span>
					#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
		 			<input type="checkbox" name="stopStartWake" value="$row_wakeUpType.value" #if($!globals.get($result,25).indexOf("$row_wakeUpType.value")>=0)checked #end  />$row_wakeUpType.name
	   				#end
	   				</li>
	   				<li><span style="width:90px;text-align:right">$text.get("workflow.lb.allNote")</span>
	   				#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
		 			<input type="checkbox" name="stopSAllWake" value="$row_wakeUpType.value" #if($!globals.get($result,26).indexOf("$row_wakeUpType.value")>=0)checked #end />$row_wakeUpType.name
	   				#end
	   				</li>
	   				<li style="margin-top:1px">
            <div style="padding:0;overflow:hidden;">
            <span style="width:90px;text-align:right;border-top:1px ridge dashed #CCCCCC">$text.get("workflow.lb.setNote")</span>
					<span style="border-top:1px ridge dashed #CCCCCC">
					#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
		 			<input type="checkbox" name="stopSetWake" value="$row_wakeUpType.value" #if($!globals.get($result,27).indexOf("$row_wakeUpType.value")>=0)checked #end />$row_wakeUpType.name
	   				#end
            </div>
	   				<div class="oa_signDocument1" id="_context">
	   				<input type="hidden" name="stopSetWakeDept" id="stopSetWakeDept" value="$!globals.get($result,28)">
					<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('deptGroup','stopSetWakeDeptName','stopSetWakeDept','1');" alt="$text.get("oa.common.adviceDept")" class="search">&nbsp;<a title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','stopSetWakeDeptName','stopSetWakeDept','1');">$text.get("oa.select.dept")</a></div>
					<select name="stopSetWakeDeptName" id="stopSetWakeDeptName" multiple="multiple">
					#foreach($dept in $stopSetWakeDept)
					<option value="$dept.classCode">$!dept.DeptFullName</option>
					#end
					</select></div>
					<div class="oa_signDocument2">
					<img onClick="deleteOpation('stopSetWakeDeptName','stopSetWakeDept')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
					</div>
					<div class="oa_signDocument1" id="_context">
					<input type="hidden" name="stopSetWakePer" id="stopSetWakePer" value="$!globals.get($result,29)">
					<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('userGroup','stopSetWakePerName','stopSetWakePer','1');" alt="$text.get("oa.common.advicePerson")" class="search">&nbsp;<a title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','stopSetWakePerName','stopSetWakePer','1');">$text.get("oa.select.personal")</a></div>
					<select name="stopSetWakePerName" id="stopSetWakePerName" multiple="multiple">
					#foreach($user in $stopSetWakePer)
					<option value="$user.id">$!user.EmpFullName</option>
					#end
					</select></div>
					<div class="oa_signDocument2">
					<img onClick="deleteOpation('stopSetWakePerName','stopSetWakePer')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
					</div>
	   				</li>
				</td>
			</tr>
			<tr>
				<td width="10%" align="right"><span>$text.get("wokeflow.CounterReviewedBy")</td>
				<td width="90%">
				<li style="float:left;width:100%;">
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');"  alt="$text.get("oa.common.adviceDept")" class="search">&nbsp;<a  title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');">$text.get("oa.select.dept")</a></div>
				<select name="formatDeptName" id="formatDeptName" multiple="multiple">
				#foreach($dept in $targetDept)
					<option value="$dept.classCode">$!dept.DeptFullName</option>
				#end
				</select></div>
				<div class="oa_signDocument2">
				<img onClick="deleteOpation('formatDeptName','popedomDeptIds')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
				</div>
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search">&nbsp;<a  title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');">$text.get("oa.select.personal")</a></div>
				<select name="formatFileName" id="formatFileName" multiple="multiple">
				#foreach($user in $targetUsers)
					<option value="$user.id">$!user.EmpFullName</option>
				#end
				</select></div>
				<div class="oa_signDocument2">
				<img onClick="deleteOpation('formatFileName','popedomUserIds')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
				</div>
				</li>
				<li style="float:left;width:100%;"><span style="width:90px;text-align:right">$text.get("workflow.lb.startNote")</span>
					#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
						<input type="checkbox" name="reviewWake" value="$row_wakeUpType.value" #if($!globals.get($result,42).indexOf("$row_wakeUpType.value")>=0)checked #end/>$row_wakeUpType.name 
					#end	
	   			</li>
				</td>
			</tr>	
			<tr>
				<td width="10%" align="right"><span>$text.get("workflow.lb.retCheckPerRule")</td>
				<td width="90%">
				<select name="retCheckPerRule" id="retCheckPerRule" >
					<option value="0" #if($!globals.get($result,36)=="0")selected#end>$text.get("workflow.lb.allCheckPerson")</option>		
					<option value="1" #if($!globals.get($result,36)=="1")selected#end>$text.get("workflow.lb.thisDept")</option>
					<option value="2" #if($!globals.get($result,36)=="2")selected#end>$text.get("workflow.lb.lastDept")</option>
					<option value="3" #if($!globals.get($result,36)=="3")selected#end>$text.get("workflow.lb.nextDept")</option>
					<option value="4" #if($!globals.get($result,36)=="4")selected#end>$text.get("workflow.lb.firstDept")</option>		
				</select>
				</td>
			</tr>	
		#if("$!type" == "1")
		<tr>
			<td width="10%" align="right"><span>$text.get("oa.workflow.TopicModule"):</td>
			<td width="90%">
			<input name="titleTemp"  style="width:400px"  type='text' class="text" value="$!globals.get($result,17)"> 
			<font color="red">$text.get("com.template.title.remark")</font>
			</td>
		</tr>
		#end
		<tr>
				<td width="10%" align="right"><span></td>
				<td width="90%">
				<input type="checkBox" name="autoPass" value="true" #if("$!globals.get($result,31)"=="true")checked#end>$text.get("workflow.msg.AutomatismFiltration")



				</td>
			</tr>
		<tr>
			<td width="10%" align="right"><span>$text.get("oaWorkflow.lb.flowDetail")：</td>
			<td>
			<textarea name="detail" style="width:90%" rows="4">$!globals.get($result,11)</textarea>
			</td>
			</tr>
		  </tbody>
		</table>
	</div>
</script>
</form>
</body>
</html>
