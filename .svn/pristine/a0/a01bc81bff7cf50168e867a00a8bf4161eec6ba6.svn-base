<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.common.knowledgeCenter")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css"/>
<link type="text/css" rel="stylesheet" href="/style/themes/icon.css"/>
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/style/themes/default/easyui.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" rel="stylesheet" href="/style/themes/icon.css"/>
<script type="text/javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/kindeditor-min.js" charset="utf-8" ></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/lang/${globals.getLocale()}.js" charset="utf-8" ></script>
<script type="text/javascript">
var userIds;  //获取组授权处理后用户的Id
var deptNames; //获取组授权的部门的名称

var deptIds;	//获取组授权的部门的Id
var usId; //获取组授权的个人Id
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="description"]',{
		uploadJson:'/UtilServlet?operation=uploadFile',
		resizeType : 0
	});
});

//判断是否为空
function isNull(variable,message){
	if(variable=="" || variable==null){
		asyncbox.alert(message+" "+"$text.get('common.validate.error.null')","$!text.get("clueTo")");
		return false;
	}else{
		return true;
	}		
}

//检查通知对象
function checkTarget(){
	var description = editor.html();
	if(!isNull(description,'$text.get("message.lb.content")')){
		return false;
	}
	var dept=$('#popedomDeptIds').val();
	var user=$('#popedomUserIds').val();
	var ischeck=document.getElementsByName("isAlonePopedom");
	if(ischeck[1].checked && dept=="" && user==""){
		asyncbox.alert('$!text.get("Please.select.one.target")','$!text.get("clueTo")');
		return false;
	}
	return true;		
}

//检查表单必填项
function checkForm(){
	var flag = true;
	$("input").each(function () {
	   	if($(this).attr('required') || $(this).attr('validType')) {
		   	if(!$(this).validatebox('isValid')) {
		   	   $(this).focus();
		       flag = false; 
		       return false; 
		   	}
	   }
	})
	if(!flag){
   		return false;
   	}
   	if(checkTarget()==false){
		return false;
	}
   	return true;
}

//通知对象单选框
function chikeRadio(obj){
	if(obj.checked==true){
		if(obj.value==1){
			document.getElementById("_title").style.display='block';  
			document.getElementById("_context").style.display='block';
			document.getElementById("_trno").style.display=''		
		}else{
			document.getElementById("_title").style.display='none';
			document.getElementById("_context").style.display='none';
			document.getElementById("_trno").style.display='none'		
		}
	}
}


//获取目录授权
function getShouquan(){
	var code=form.folderId.value;
	var str="/OAKnowledgeCenter.do?operation=$globals.getOP("OP_ADD")&thetype=getShouquan&groupCode="+code;
	AjaxRequest(str);
 	value = response;
 	var theStr=new Array();
 	theStr=value.split(";");
 	userIds=theStr[0];
 	deptNames=theStr[1];
    deptIds=theStr[2];
    usId=theStr[3];
}

//返回
function forback(){
	asyncbox.confirm('$text.get('oa.common.edit.content')','$!text.get("clueTo")',function(action){
	if(action == 'ok'){
		window.location.href="/OAKnowCenter.do?type=oaKnowList&selectKnow=$!preId" ;
	}
	});
}

//目录弹出框



function showGroup(){
	var fCode = window.parent.frames["leftFrame"].document.getElementById("fCode").value;
	if(fCode.length==0){
		asyncbox.alert('当前没有可选组','$!text.get("clueTo")');
		return false;
	}
	fCode=fCode.replaceAll("'","");
  	var displayName="$text.get('oa.common.department')" ;
	var urlstr = "/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&popupWin=Popdiv&selectName=KnowledgeCenterNote&classCode="+fCode+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName ;
	asyncbox.open({id:'Popdiv',title:displayName,url:urlstr,width:750,height:470});
}
//弹出框回填内容



function exePopdiv(returnValue){
	if(typeof(returnValue)=="undefined") return ;
	var note = returnValue.split("#;#") ;
	form.folderId.value=note[0];
	form.groupName.value=note[1];	
	getShouquan();
}

var fieldNames;
var fieldNIds;
function deptPopUser(popname,fieldName,fieldNameIds){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox&chooseData="+form.popedomUserIds.value ;
	if(usId != "noUser"){
		urls += "&userCode=" + usId;
	}
	if(deptIds != "noDept"){
		urls += "&parameterCode=" + deptIds;
	}
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({
		 id : 'Popdiv',
	 	 title : '请选择个人',
	　　　url : urls,
	　　　width : 750,
	　　　height : 430,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。



　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。



				var employees = opener.strData;
				jQuery("#"+fieldName+" option").remove();
				getValueById(fieldNameIds).value="";
				newOpenSelect(employees,fieldName,fieldNameIds,2);
　　　　　	}
　　　	}
　	});
}

function deptPop(popname,fieldName,fieldNameIds,flag){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	if(deptIds != "noDept"){
		urls += "&parameterCode=" + deptIds;
	}
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({
	 	 id : 'Popdiv',
	 	 title : '请选择部门',
	　　　url : urls,
	　　　width : 750,
	　　　height : 430,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。

　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。

				var employees = opener.strData;
				newOpenSelect(employees,fieldName,fieldNameIds,flag,1)
　　　　　	}
　　　	}
　	});
}
//双击回填值

function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	jQuery.close('Popdiv')
}
</script>
</head>
 
<body onload="getShouquan()">
<iframe name="formFrame" style="display:none"></iframe>
<form id="form" name="form" method="post" action="/OAKnowledgeCenter.do" target="formFrame" onsubmit="return checkForm();">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="$!file.popedomUserIds"/>
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="$!file.popedomDeptIds"/>
<input type="hidden" name="id" value="$!file.id"/>
<input type="hidden" name="attachFiles" value="$!file.filePath"/>
<input type="hidden" name="delFiles" value=""/>
<input type="hidden" name="createBy" value="$!file.createBy"/>
<input type="hidden" name="createTime" value="$!file.createTime"/>
<input type="hidden" name="preId" value="$!preId"/>
<input  type="hidden" name="position" value="$!position"/> 
	<div class="bigTitle" style="background-image: none">
		<div class="new_button">
			<button type="submit" name="Submit" class="fb" ></button> 
			<button type="button" class="fh" onclick="forback()" ></button>
		</div>
	</div>
	<tbody>
	<div  id="nn" style="overflow:hidden;overflow-y:auto;width:100%;">
<script type="text/javascript">
	var oDiv=document.getElementById("nn");
	var sHeight=document.documentElement.clientHeight-55;
	oDiv.style.height=sHeight+"px";
</script>

	<table cellpadding="0" width="100%" cellspacing="0" style="margin-top: 15px;margin-left:10px; border-spacing: 3px;border: 0px solid #7B7B7B">
		
		<tr>
			<td class="oabbs_tr" style="color:#FF0000;">$text.get("message.lb.title")：</td>
			<td><input name="fileTitle" value="$!file.fileTitle" type="text" class="easyui-validatebox borders" required="true" style="width: 300px;"/></td>
		</tr>
		<tr>
			<td class="oabbs_tr" style="color:#FF0000;">$text.get("oa.mydata.group")：</td>
			<td>
			<input type="hidden" id="folderId" name="folderId" value="$!file.folderId"/>
			<input style="float:left;margin:0;cursor:text;background:#FFFFEE url(/style/themes/default/images/validatebox_warning.png) no-repeat right 1px;" type="text" id="groupName" name="groupName" readonly="true" onpropertyChange="javascript:if(this.value!=null) getShouquan();" #foreach($!f in $!folder) value="$!f.folderName"#end class="easyui-validatebox borders" required="true" onclick="showGroup();"/>
			<img style="float:left;margin:2px 0 0 3px;" onClick="showGroup();" src="/$globals.getStylePath()/images/St.gif" class="search" />
			</td>
		</tr>
		<tr>
			<td class="oabbs_tr">$text.get("oa.data.fileName")：</td>
			<td><input type="text" name="fileName" value="$!file.fileName" class="borders" style="width: 300px;"/></td>
		</tr>
		<tr>
			<td valign="top" class="oabbs_tr" style="color:#FF0000;">$text.get("message.lb.content")：</td>
			<td><textarea name="description" style="width:80%;height:280px;visibility:hidden;">$file.description</textarea></td>
		</tr>
		<tr>
			<td valign="top" class="oabbs_tr">附件上传：</td>
			<td><a href="javascript:openAttachUpload('/affix/OAKnowledgeCenterFile/')" >&nbsp;<span></span>
			<img src="/style1/images/oaimages/uploading.gif" /><font style="margin-left: 2px;color: #000000;">附件上传</font></a>
				<div id="status" style="visibility:hidden;color:Red"></div>
				<div id="files_preview">
					#if($!file.filePath.indexOf(";") > 0)
						#foreach ($str in $globals.strSplit($!file.filePath,';'))
							<div  id ="$str" style ="height:18px; color:#ff0000;padding-top:10px;">
							<a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
							$str<input type=hidden name="attachFile" value="$str"/></div>					 
						#end	
			 	 	#end
				</div>
			</td>
		</tr>
		<tr>
			<td class="oabbs_tr">$text.get("oa.calendar.wakeupType")：</td>
			<td>
				#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
		 			<input type="checkbox" name="wakeUpMode" #foreach($t in $!wakeUpType) #if($row_wakeUpType.value==$t) checked="checked"  #end #end value="$row_wakeUpType.value" />$row_wakeUpType.name
	   			#end
			</td>
		</tr>
			#set ($isAlone = $!file.isAlonePopedom)
			 #if($isAlone=="0"|| "$!isAlone"=="")
			 	#set ($check1="checked")
			 	#set ($style="display:none")
			 #else 
			 	#set ($check2="checked")
			 	#set ($style="display:block")
			 #end
		<tr>
			<td class="oabbs_tr">$text.get("oa.common.garget")：</td>
			<td><input name="isAlonePopedom" onClick="chikeRadio(this)" type="radio" #if($!file.isAlonePopedom==0) checked #end value="0" id="radio" />
			  $text.get("oa.common.all")			 
			 <input name="isAlonePopedom" type="radio" onClick="chikeRadio(this)" #if($!file.isAlonePopedom==1) checked #end id="radiobutton" value="1" />
			  $text.get("oa.common.appoint")</td>
		</tr>
		<tr id="_trno" #if($isAlone=="0"||"$!isAlone"=="") style="display:none;" #end>
				<td class="oabbs_tr" id="_title">&nbsp;</td>
				<td valign="middle">
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds');"  alt="$text.get("oa.common.adviceDept")" class="search"/>&nbsp;<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds');">$text.get("oa.select.dept")</a></div>
				<select name="formatDeptName" id="formatDeptName" multiple="multiple">
				#foreach($dept in $targetDept)
					<option value="$dept.classCode">$!dept.DeptFullName</option>
				#end
				</select></div>
				<div class="oa_signDocument2">
				<img onClick="deleteOpation('formatDeptName','popedomDeptIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search"/>
				</div>
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="deptPopUser('userGroup','formatFileName','popedomUserIds');" alt="$text.get("oa.common.advicePerson")" class="search"/>&nbsp;<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPopUser('userGroup','formatFileName','popedomUserIds');">$text.get("oa.select.personal")</a></div>
				<select name="formatFileName" id="formatFileName" multiple="multiple">
				#foreach($user in $targetUsers)
					<option value="$user.id">$!user.EmpFullName</option>
				#end
				</select></div>
				<div class="oa_signDocument2">
				<img onClick="deleteOpation('formatFileName','popedomUserIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search"/>
				</div>
			</td>
		  </tr>
		  <tr>
		  	<td class="oabbs_tr">$text.get("oa.readingMark.save")：</td>
		  	<td><input name="isSaveReading" type="radio" #if("$!file.isSaveReading"=="1") checked #end value="1" />
			  $text.get("oa.common.yes")
			 <input name="isSaveReading" type="radio" #if("$!file.isSaveReading"=="0") checked #end value="0" />
			  $text.get("oa.common.no")	</td>
		  </tr>
	</table>
	</div>
	</tbody>
</form>
</body>
</html>