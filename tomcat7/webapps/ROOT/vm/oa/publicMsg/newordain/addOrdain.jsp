<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.common.addBylaw")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"  />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css"  type="text/css"/>
<link type="text/css" rel="stylesheet" type="text/css" href="/style/themes/default/easyui.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>
<script type="text/javascript" src="/js/kindeditor-min.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/lang/${globals.getLocale()}.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">	
var userIds;  //获取组授权处理后用户的Id
var deptNames; //获取组授权的部门的名称

var deptIds;	//获取组授权的部门的Id
var usId; //获取组授权的个人Id
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="content"]',{
		uploadJson:'/UtilServlet?operation=uploadFile',
		resizeType : 0
	});
});

//获取目录授权
function getShouquan(){
	var code=form.groupId.value;
	var str="/OAnewOrdainAction.do?operation=$globals.getOP("OP_ADD")&thetype=getShouquan&groupCode="+code;
	AjaxRequest(str);
 	value = response;
 	var theStr=new Array();
 	theStr=value.split(";");
 	userIds=theStr[0];
 	deptNames=theStr[1];
    deptIds=theStr[2];
    usId=theStr[3];
}

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
	var dept=$('#popedomDeptIds').val();
	var user=$('#popedomUserIds').val();
	var ischeck=document.getElementsByName("isAlonePopedmon");
	if(ischeck[1].checked && dept=="" && user=="" ){
		asyncbox.alert('$!text.get("Please.select.one.target")','$!text.get("clueTo")');
		return false;
	}
	return true;		
}

//检查表单必填项
function checkForm(){
	var Content=editor.html();
	var groupName=form.groupName.value;
	var flag=true;
	$("input").each(function () {
	   	if($(this).attr('required') || $(this).attr('validType')) {
		   	if(!$(this).validatebox('isValid')) {
		   	$(this).focus();
		   		flag=false;
		       return false;	       
		   	}
	   }
	})
	
	if(flag){
		if(!isNull(Content,'$text.get("message.lb.content")')){
			return false;
		}
		if(checkTarget()==false){
			return false;
		}
	}
	else{
		return false;
	}
	return flag;
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

function showGroup(){
	var fCode = window.parent.frames["leftFrame"].document.getElementById("fCode").value;
	if(fCode.length==0){
		asyncbox.alert('当前没有可选组','$!text.get("clueTo")');
		return false;
	}
	var displayName="$text.get('oa.common.bylaw.group')" ;
	var urlstr = '/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&popupWin=Popdiv&tableName=OAOrdainGroup&selectName=OrdainGroupNote&classCode='+fCode+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName ;
	asyncbox.open({id:'Popdiv',title:displayName,url:urlstr,width:750,height:470});
}

function exePopdiv(str){
	if(typeof(str)=="undefined") return ;
	var note = str.split("#;#") ;
	form.groupId.value=note[0];
	form.groupName.value=note[1];
	getShouquan();
}

function isSelectGroup(){
 	var group=$('#groupName').val();
 	if(group =="" || group == null ){
 		asyncbox.alert('请先选择一个分组！','提示');
 		return false;
 	}
 	else{
 		return true;
 	}
}

/*处理通知指定对象函数
popname 表示是哪个选择进入 deptGroup表示部门 userGroup表示个人 empGroup表示职员分组
fieldName 传的是<select>标签的名字




fieldNameIds 隐藏域的ID,用于把相关ID传到后台处理
flag 用于表示是否需要过滤检查  默认为1表示不用  2表示进入知识中心  3表示规章制度
*/
var fieldNames;
var fieldNIds;
function deptPopSearch(popname,fieldName,fieldNameIds){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	if(deptIds != "noDept"){
		urls += "&parameterCode=" + deptIds;
	}
	
	fieldNames = fieldName;
	fieldNIds = fieldNameIds;
	asyncbox.open({
    	id:'Popdiv',title:'请选择部门',url:urls,width:750,height:430,btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
　　　　　	if(action == 'ok'){
			var employees = opener.strData;
				newOpenSelect(employees,fieldName,fieldNameIds,1)
　　　　　	}
　　　	}
　	});
}
	
function deptPopUser(popname,fieldName,fieldNameIds){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox&chooseData="+form.popedomUserIds.value ;
	if(usId != "noUser"){
		urls += "&userCode=" + usId;
	}
	if(deptIds != "noDept"){
		urls += "&parameterCode=" + deptIds;
	}
	fieldNames = fieldName;
	fieldNIds = fieldNameIds;
	asyncbox.open({
		id:'Popdiv',title:'请选择个人',url:urls,width:750,height:430,btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var employees = opener.strData;
				jQuery("#"+fieldName+" option").remove();
				getValueById(fieldNameIds).value="";
				newOpenSelect(employees,fieldName,fieldNameIds,2);
　　　　　	}
　　　	}
　	});
}

//弹出框双击回填内容



function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	jQuery.close('Popdiv')
}
</script>
</head>
	<body>
		<form action="/OAnewOrdainAction.do?operation=$globals.getOP("OP_ADD")&type=addordain " onSubmit="return checkForm();" name="form"
			method="post">
			<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="" />
			<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="" />
			<input type="hidden" name="groupId" value="$!selectId" />	
			<input type="hidden" name="attachFiles" value="" />		
			<input type="hidden" name="delFiles" value="" />	
			<div>	
				<div class="bigTitle" style="background-image: none">
					<div class="new_button">
						<button type="submit" name="Submit" class="fb"></button>
						<button type="button"
							onClick="location.href='/OAnewOrdain.do?operation=$globals.getOP("OP_QUERY")'"
							class="fh"></button>
					</div>
				</div>
				<div id="nn" style="overflow:hidden;overflow-y:auto;width:100%;">
<script type="text/javascript">
	var oDiv=document.getElementById("nn");
	var sHeight=document.documentElement.clientHeight-55;
	oDiv.style.height=sHeight+"px";
</script>	
					<table width="100%" border="0" cellpadding="3" cellspacing="0" name="table" >
						<tr>
							<td class="oabbs_tr" style="width:120px;color:#FF0000;">
								$text.get("message.lb.title")：

							</td>
							<td >
								<input name="ordainTitle" type="text" size="55" class="easyui-validatebox" required="true"  />
							</td>
						</tr>
						<tr>
							<td class="oabbs_tr" style="color:#FF0000;">
								$text.get("or.ordain.gorup")：

							</td>
							<td>
								<input style="float:left;margin:0;cursor:text;background:#FFFFEE url(/style/themes/default/images/validatebox_warning.png) no-repeat right 1px;" name="groupName" id="groupName" type="text"  value="$!selectType" readonly="true" class="easyui-validatebox" required="true" size="20" onclick="showGroup();" />
								<img style="float:left;margin:2px 0 0 3px;" onClick="showGroup()" src="/$globals.getStylePath()/images/St.gif" class="search"  />		
							</td>
						</tr>
					
						<tr>
							<td class="oabbs_tr" valign="top" style="color:#FF0000;">
								$text.get("message.lb.content")：

							</td>
							<td colspan="3">
								<textarea name="content"
								 class="easyui-validatebox" style="width:80%;height:280px;visibility:hidden;"></textarea>
							</td>
						</tr>
						<tr>
							<td class="oabbs_tr" valign="top">$text.get("upload.lb.affixupload")：</td>
							<td>
								<a href="javascript:void(0)" onclick="openAttachUpload('/affix/OAOrdainInfo/')">&nbsp;<span></span>
								<img src="/style1/images/oaimages/uploading.gif" /><font style="margin-left: 2px;">$text.get("upload.lb.affixupload")</font></a>
								<div id="status" style="visibility:hidden;color:Red"></div>
								<div id="files_preview">
								</div>	
							</td>
						</tr>
						<tr>
						<td class="oabbs_tr">
							$text.get("oa.calendar.wakeupType")：



						</td>
						<td>
							#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
							#if("$row_wakeUpType.name"=="通知提示")
								<input type="checkbox" name="wakeUpMode" checked value="$row_wakeUpType.value" />
							#else
								<input type="checkbox" name="wakeUpMode" value="$row_wakeUpType.value" />
							#end
							$row_wakeUpType.name #end
						</td>
						</tr>
						<tr>
							<td class="oabbs_tr">
								$text.get("oa.common.garget")：



							</td>
							<td>
								<input name="isAlonePopedmon" onClick="chikeRadio(this)"
									type="radio" checked value="0" id="radio" />
								$text.get("oa.common.all")
								<input name="isAlonePopedmon" type="radio"
									onClick="chikeRadio(this)" id="radiobutton" value="1" />
								$text.get("oa.common.appoint")
							</td>
						</tr>
						<tr id="_trno" style="display:none">
							<td class="oabbs_tr" id="_title" style="display:none">&nbsp;
								
							</td>
							<td valign="middle" colspan="3">
								<div class="oa_signDocument1" id="_context">
									<div class="oa_signDocument_3">
										<img src="/$globals.getStylePath()/images/St.gif" 
											onClick="if(isSelectGroup()==true) deptPopSearch('deptGroup','formatDeptName','popedomDeptIds');" alt="$text.get("oa.common.adviceDept")" class="search" />
										&nbsp;
										<a href="javascript:void(0);"  title="$text.get("oa.select.dept")"  onClick="if(isSelectGroup()==true) deptPopSearch('deptGroup','formatDeptName','popedomDeptIds');">$text.get("oa.select.dept")</a>
									</div>
									<select name="formatDeptName" id="formatDeptName"
										multiple="multiple">
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="deleteOpation('formatDeptName','popedomDeptIds')"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search" />
								</div>
								<div class="oa_signDocument1" id="_context">
									<div class="oa_signDocument_3">
										<img src="/$globals.getStylePath()/images/St.gif"
											onClick="if(isSelectGroup()==true) deptPopUser('userGroup','formatFileName','popedomUserIds'
											);" alt="$text.get("oa.common.advicePerson")" class="search" />
										&nbsp;
										<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="if(isSelectGroup()==true) deptPopUser('userGroup','formatFileName','popedomUserIds');">$text.get("oa.select.personal")</a>
									</div>
									<select name="formatFileName" id="formatFileName"
										multiple="multiple">
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="deleteOpation('formatFileName','popedomUserIds')"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search" />
								</div>
							
						</tr>
						<tr>
							<td class="oabbs_tr">
								$text.get("oa.readingMark.save")：




							</td>
							<td>
								<input name="isSaveReading" type="radio" checked value="1" />
								$text.get("oa.common.yes")
								<input name="isSaveReading" type="radio" value="0" />
								$text.get("oa.common.no")
							</td>
						</tr>
					</table>
					<br/>
				</div>
				<script>
var code=form.groupId.value;
if(code.length>=5){
	getShouquan();
}
				</script>
			</div>
		</form>
	</body>
</html>