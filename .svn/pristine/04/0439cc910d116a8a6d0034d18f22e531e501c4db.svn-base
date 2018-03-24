<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("oa.common.updateAdvice")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/style/themes/default/easyui.css" type="text/css"/>
<link rel="stylesheet" href="/style/themes/icon.css" type="text/css"/>
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"  />
<script language="javascript"  src="/js/function.js"></script>
<script language="javascript"  src="/js/jquery.js"></script>
<script language="javascript"  src="/js/formvalidate.vjs"></script>
<script language="javascript"  src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript"  src="/js/kindeditor-min.js" charset="utf-8"></script>
<script language="javascript"  src="/js/lang/${globals.getLocale()}.js"  charset="utf-8"></script>
<script language="javascript"  src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script language="javascript"  src="/js/jquery.easyui.min.js"></script>
<script language="javascript"  src="/js/public_utils.js"></script>


<script type="text/javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="adviceContext"]',{
		uploadJson:'/UtilServlet?operation=uploadFile',
		resizeType : 0
	});
});


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

//判断是否为空
function isNull(variable,message){
	if(variable=="" || variable==null){
		asyncbox.alert(message+" "+"$text.get('common.validate.error.null')","$!text.get("clueTo")");
		return false;
	}else{
		return true;
	}		
}

//检查表单必填项
function checkForm(){	
	var Content=editor.html();
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
		if(!isNull(Content,'$text.get("oa.common.adviceContent")')){
			return false;
		}
		if(checkTarget()==false){
			return false;
		}
	}	
	return flag;
}

//返回
function forback(){
	asyncbox.confirm('$text.get('oa.common.edit.content')','$!text.get("clueTo")',function(action){
	　　 if(action == 'ok'){
		window.location.href="/OAnewAdvice.do?operation=4&selectAdvice=$!ownAdvice.id";
		}
	});
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
		<form action="/OAnewAdviceAction.do?operation=$globals.getOP("OP_UPDATE")" onSubmit="return checkForm();" name="form" method="post">
			<input type="hidden" name="accepter" id="accepter" value="$!ownAdvice.accepter" />
			<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="$!ownAdvice.popedomDeptIds" />
			<input type="hidden" name="attachFiles" value="$!ownAdvice.filePath"/>
			<input type="hidden" name="statu" id="statuid" value="" />
			<input type="hidden" name="delFiles" value=""/>
			<input type="hidden" name="statusId" value="$!ownAdvice.statusId"/>
			<input type="hidden" name="pulishDate" value="$!ownAdvice.pulishDate"/>
			<input type="hidden" name="pulisher" value="$!ownAdvice.pulisher"/>
			<input  type="hidden" name="position" value="$!position"/> 
			<div class="bigTitle" style="background-image: none">
		
				<div class="new_button">
				
					#if($!ownAdvice.statusId == 1)
						<button type="submit" name="Submit" class="fb">
						</button>
					#end
					<button type="button" onclick="submitZC();" class="save"></button>
					  <button type="button" onClick="forback()" class="fh"></button>
				</div>
			</div>
			<div id="nn" style="overflow:hidden;overflow-y:auto;width:100%;">
<script type="text/javascript">
	var oDiv=document.getElementById("nn");
	var sHeight=document.documentElement.clientHeight-55;
	oDiv.style.height=sHeight+"px";
</script>
				<table width="100%" border="0" cellpadding="3" cellspacing="0">
				<br/>
					<tr>
						<td class="oabbs_tr" width="10%">
							$text.get("advice.lb.title")：<font color="red">*</font>
						</td>
						<td width="55%">
							<input type="hidden" name="adviceId" value="$!ownAdvice.Id" />
							<input name="adviceTitle" type="text" value="$!ownAdvice.adviceTitle"
								size="55" class="easyui-validatebox" required="true" />
						</td>
					</tr>
					<tr>
						<td class="oabbs_tr">
							$text.get("oa.common.adviceType")：
						</td>
						<td>
							<label for="select"></label>
							<select name="adviceType" id="select">
								#foreach($row_adviceType in
								$globals.getEnumerationItems("AdviceType"))
								<option value="$row_adviceType.value"
									#if($!ownAdvice.adviceType==$row_adviceType.value)
									selected="selected" #end>
									$row_adviceType.name
								</option>
								#end
							</select>
						</td>
					</tr>
					
					<tr>
						<td class="oabbs_tr" valign="top">
							$text.get("oa.common.adviceContent")：<font color="red">*</font>

						</td>
						<td colspan="3">
							<textarea name="adviceContext"
								style="width:80%;height:280px;visibility:hidden;">$!ownAdvice.adviceContext</textarea>
						</td>
					</tr>
					<tr>
						<td class="oabbs_tr" valign="top">
							$text.get("upload.lb.affixupload")：</td>
						<td>
							<a href="javascript:void(0)" onclick="openAttachUpload('/affix/OAAdviceInfo/')">&nbsp;<span></span>
							<img src="/style1/images/oaimages/uploading.gif" /><font style="margin-left: 2px;">$text.get("upload.lb.affixupload")</font></a>
							<div id="status" style="visibility:hidden;color:Red"></div>
							<div id="files_preview">
								 #if($!ownAdvice.filePath.indexOf(";") > 0)
								  #foreach ($str in $globals.strSplit($!ownAdvice.filePath,';'))
								  	 <div  id ="$str" style ="height:18px; color:#ff0000;">
								  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
								  	 $str<input type=hidden name="attachFile" value="$str"/></div>					 
								  #end	
							 	 #end
							</div>		
						</td>
						</tr>
					<tr>
						<td class="oabbs_tr">
							$text.get("oa.calendar.wakeupType")：
						</td>
						<td>
							#foreach($row_wakeUpType in
							$globals.getEnumerationItems("WakeUpMode"))
							<input type="checkbox" name="wakeUpMode" #foreach($t
								in $!wakeUpType) #if($row_wakeUpType.value==$t) checked="checked"
								#end #end value="$row_wakeUpType.value" />
							$row_wakeUpType.name #end
						</td>
					</tr>
					<tr>
						    #set ($isAlone = $ownAdvice.isAlonePopedmon) 
								#if($isAlone=="0"||"$!isAlone"=="") 
									#set ($check1="checked") 
									#set ($style="display:none") 
								#else 
									#set ($check2="checked") 
									#set ($style="display:block") 
							#end

						<td class="oabbs_tr">
							$text.get("oa.common.garget")：
						</td>
						<td>
							<input name="isAlonePopedmon" onClick="chikeRadio(this)"
								type="radio" $!check1 value="0" id="radio" /> 
							$text.get("oa.common.all")
							<input name="isAlonePopedmon" type="radio"
								onClick="chikeRadio(this)" $!check2 id="radiobutton" value="1" />
							$text.get("oa.common.appoint")
						</td>
					</tr>
					<tr id="_trno"   #if($isAlone=="0"||"$!isAlone"=="") style="display:none;" #end>
						<td class="oabbs_tr" id="_title">&nbsp;
							
						</td>
						<td valign="middle" colspan="3">
							<div class="oa_signDocument1" id="_context">
								<div class="oa_signDocument_3">
									<img src="/$globals.getStylePath()/images/St.gif"
										onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');" alt="$text.get("oa.common.adviceDept")" class="search" />
									&nbsp;
									<a href="javascript:void(0)" title="$text.get("oa.common.adviceDept")" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');">$text.get("oa.common.adviceDept")</a>
								</div>
								<select name="formatDeptName" id="formatDeptName"
									multiple="multiple">
									#foreach($dept in $targetDept)
									<option value="$!globals.get($dept,14)">
										$!globals.get($dept,2) 
									</option>
									#end
								</select>
							</div>
							<div class="oa_signDocument2">
								<img onClick="deleteOpation('formatDeptName','popedomDeptIds');"
									src="/$globals.getStylePath()/images/delete_button.gif"
									alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search" />
							</div>
							<div class="oa_signDocument1" id="_context">
								<div class="oa_signDocument_3">
									<img src="/$globals.getStylePath()/images/St.gif"
										onClick="deptPop('userGroup','formatFileName','accepter','1');" alt="$text.get("oa.common.advicePerson")" class="search"/>
									&nbsp;
									<a href="javascript:void(0)" title="$text.get("oa.common.advicePerson")" onClick="deptPop('userGroup','formatFileName','accepter','1');">$text.get("oa.common.advicePerson")</a>
								</div>
								<select name="formatFileName" id="formatFileName"
									multiple="multiple">
									#foreach($user in $targetUsers)
									<option value="$user.id">
										$!user.EmpFullName
									</option>
									#end
								</select>
							</div>
							<div class="oa_signDocument2">
								<img onClick="deleteOpation('formatFileName','accepter');"
									src="/$globals.getStylePath()/images/delete_button.gif"
									alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search" />
							</div>

							<input type="hidden" name="popedomEmpGroupIds"
								id="popedomEmpGroupIds" value="$!ownAdvice.popedomEmpGroupIds" />
							<div class="oa_signDocument1" id="_context">
								<div class="oa_signDocument_3">
									<img src="/$globals.getStylePath()/images/St.gif"
										onClick="deptPop('empGroup','EmpGroup','popedomEmpGroupIds','1');"
										alt="$text.get("oa.oamessage.usergroup")" class="search" />
									&nbsp;
									<a href="javascript:void(0)" style="cursor:pointer"
										title="$text.get('oa.oamessage.usergroup')"
										onClick="deptPop('empGroup','EmpGroup','popedomEmpGroupIds','1');">$text.get("oa.oamessage.usergroup")</a>
								</div>
								<select name="EmpGroup" id="EmpGroup" multiple="multiple">
									#foreach($grp in $targetEmpGroup)
									<option value="$!globals.get($grp,0)">
										$!globals.get($grp,5) 
									</option>
									#end
								</select>
							</div>
							<div class="oa_signDocument2">
								<img onClick="deleteOpation('EmpGroup','popedomEmpGroupIds');"
									src="/$globals.getStylePath()/images/delete_button.gif"
									alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search" />
							</div>
						</td>

					</tr>
					<tr>
						<td class="oabbs_tr">
							$text.get("oa.whetherAgreeReply.label")：
						</td>
						<td>
							#set($iwhetherAgreeReply=$ownAdvice.whetherAgreeReply)
							#if($iwhetherAgreeReply=="1") #set($c1="checked") #else
							#set($c2="checked") #end

							<input name="whetherAgreeReply" type="radio" value="1" $!c1 />
							$text.get("oa.common.yes")
							<input name="whetherAgreeReply" type="radio" value="0" $!c2 />
							$text.get("oa.common.no")
						</td>
					</tr>
					<tr>
						<td class="oabbs_tr">
							$text.get("oa.readingMark.save")：
						</td>
						<td>
							#set ($isSaveReading=$ownAdvice.isSaveReading)
							#if($isSaveReading=="1") #set($c3="checked") #else
							#set($c4="checked") #end

							<input name="isSaveReading" type="radio" value="1" $!c3 />
							$text.get("oa.common.yes")
							<input name="isSaveReading" type="radio" value="0" $!c4 />
							$text.get("oa.common.no")
						</td>
					</tr>
				</table>
			</div>
		</form>
	</body>
</html>
