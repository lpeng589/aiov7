<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.common.addAdvice")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="/style/themes/default/easyui.css"/>

<script type="text/javascript">
	
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="adviceContext"]',{
		uploadJson:'/UtilServlet?operation=uploadFile',
		resizeType : 0
	});
});

//检查通知对象
function checkTarget(){
	var dept=$('#popedomDeptIds').val();
	var user=$('#accepter').val();
	var group=$('#popedomEmpGroupIds').val();
	var ischeck=document.getElementsByName("isAlonePopedmon");
	if(ischeck[1].checked && dept=="" && user=="" && group==""){
		asyncbox.alert('$!text.get("Please.select.one.target")','$!text.get("clueTo")');
		return false;
	}
	return true;		
}

//检查表单必填项
function checkForm(){	
	var Content=editor.html();
	var flag=true;
	$("input").each(function (){
	   	if($(this).attr('required') || $(this).attr('validType')) {
		   	if(!$(this).validatebox('isValid')){
		   	$(this).focus();
		   		flag=false;
		       return false;	       
		   	}
	   }
	})
	if(flag){
		if(!isNull(Content,'$text.get("oa.common.adviceContent")')){
			return false;
		}
		if(checkTarget()==false){
			return false;
		}
	}
	return flag;
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

function submitZC(){
	if(checkForm()){
		$("#statuid").val("zc");
		form.adviceContext.value = editor.html();
		form.submit();
	}
}

//弹出框双击回填内容

function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	jQuery.close('Popdiv')
}
</script>
	</head>
	<body>
		<form action="/OAnewAdviceAction.do?operation=$globals.getOP("OP_ADD")&type=addadvice " onSubmit="return checkForm();" name="form"
			method="post">
			<input type="hidden" name="accepter" id="accepter" value="" />
			<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="" />
			<input type="hidden" name="popedomEmpGroupIds" id="popedomEmpGroupIds" value="" />
			<input type="hidden" name="statu" id="statuid" value="" />
			<input type="hidden" name="attachFiles" value="" />
			<input type="hidden" name="delFiles" value="" />	
			<div>	
				<div class="bigTitle" style="background-image: none">
					<div class="new_button">
				
						<button type="submit" name="Submit" class="fb"></button>
						<button type="button" onClick="submitZC();" class="bu_bg">
						<img src="style1/images/oaimages/54.bmp" style="vertical-align: middle;" />&nbsp;<font color="black">存为草稿</font>
						</button>
						<button type="button"
							onClick="location.href='/OAnewAdvice.do?operation=$globals.getOP("OP_QUERY")'"
							class="fh"></button>
					</div>
				</div>
				<div id="nn" style="overflow:auto;width:100%;">
<script type="text/javascript">
	var oDiv=document.getElementById("nn");
	var sHeight=document.documentElement.clientHeight-55;
	oDiv.style.height=sHeight+"px";
</script>
					<table width="100%" border="0" cellpadding="3" cellspacing="0"
						name="table" >
						<br/>
						<tr>
							<td class="oabbs_tr" width="10%">
								$text.get("oa.common.adviceTitle")：<font color="red">*</font>
							</td>
							<td >
								<input name="adviceTitle" type="text" size="55"
									class="easyui-validatebox" required="true"  />
							</td>
						</tr>
						<tr>
							<td class="oabbs_tr">
								$text.get("oa.common.adviceType")：

							</td>
							<td>
								<label for="select"></label>
								<select name="adviceType" id="select">
									#foreach($row_adviceType in $globals.getEnumerationItems("AdviceType"))
									<option value="$row_adviceType.value"
										#if($!adviceSearchForm.selectId==$row_adviceType.value) selected="selected" #end>
										$row_adviceType.name
									</option>
									#end
								</select>
								</td>
						</tr>					
						<tr>
							<td class="oabbs_tr" valign="top">
								$text.get("advice.lb.content")：<font color="red">*</font>
							</td>
							<td colspan="3">
								<textarea name="adviceContext"
								 class="easyui-validatebox" style="width:80%;height:280px;visibility:hidden;"></textarea>
							</td>
						</tr>
						<tr>
							<td class="oabbs_tr" valign="top">
								$text.get("upload.lb.affixupload")：</td>
							<td>
								<a href="javascript:void(0)" onclick="openAttachUpload('/affix/OAAdviceInfo/');window.save=true;">&nbsp;<span></span>
								<img src="/style1/images/oaimages/uploading.gif" /><font style="margin-left: 2px;">$text.get("upload.lb.affixupload")</font></a>
								<div id="status" style="visibility:hidden;color:Red"></div>
								<div id="files_preview"></div>				
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
											onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');" alt="$text.get("oa.common.adviceDept")" class="search" />
										&nbsp;
										<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');">$text.get("oa.select.dept")</a>
									</div>
									<select name="formatDeptName" id="formatDeptName"
										multiple="multiple">
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="deleteOpation('formatDeptName','popedomDeptIds')"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")"  class="search" />
								</div>
								<div class="oa_signDocument1" id="_context">
									<div class="oa_signDocument_3">
										<img src="/$globals.getStylePath()/images/St.gif"
											onClick="deptPop('userGroup','formatFileName','accepter','1');" alt="$text.get("oa.common.advicePerson")" class="search" />
										&nbsp;
										<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName','accepter','1');">$text.get("oa.select.personal")</a>
									</div>
									<select name="formatFileName" id="formatFileName"
										multiple="multiple">
									</select>
								</div>
								<div class="oa_signDocument2">
									<img onClick="deleteOpation('formatFileName','accepter');"
										src="/$globals.getStylePath()/images/delete_button.gif"
										alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search" />
								</div>
								<input type="hidden" name="popedomEmpGroupIds"
									id="popedomEmpGroupIds" value="" />
							</td>
						</tr>
						<tr>
							<td class="oabbs_tr">
								$text.get("oa.whetherAgreeReply.label")：

							</td>
							<td>
								<input name="whetherAgreeReply" type="radio" checked value="1" />
								$text.get("oa.common.yes")
								<input name="whetherAgreeReply" type="radio" value="0" />
								$text.get("oa.common.no")
							</td>
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
			</div>
		</form>
	</body>
</html>