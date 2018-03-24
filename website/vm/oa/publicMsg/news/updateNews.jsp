<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("oa.common.updateNews")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/style/themes/default/easyui.css" type="text/css"/>
<link rel="stylesheet" href="/style/themes/icon.css" type="text/css"/>
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"  />
<script language="javascript"  src="/js/function.js"></script>
<script language="javascript"  src="/js/jquery.js"></script>
<script language="javascript"  src="/js/formvalidate.vjs"></script>
<script language="javascript"  src="/js/kindeditor-min.js"  charset="utf-8"></script>
<script language="javascript"  src="/js/lang/${globals.getLocale()}.js" charset="utf-8"></script>
<script language="javascript"  src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript"  src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script language="javascript"  src="/js/jquery.easyui.min.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>

<script type="text/javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="newsContext"]',{
		uploadJson:'/UtilServlet?operation=uploadFile',
		resizeType : 0
	});
});

//检查通知对象
function checkTarget(){
	var dept=$('#popedomDeptIds').val();
	var user=$('#popedomUserIds').val();
	var group=$('#popedomEmpGroupIds').val();
	var ischeck=document.getElementsByName("isAlonePopedmon");
	if(ischeck[1].checked && dept=="" && user=="" && group==""){
		asyncbox.alert('$!text.get("Please.select.one.target")','$!text.get("clueTo")');
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

//检查表单必填项
function checkForm(){	
	if($("#files_preview").find('img').attr('src') == undefined){
		asyncbox.alert('请上传一张图片','$!text.get("clueTo")');	
		return false;
	}
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
		if(!isNull(Content,'$text.get("oa.common.newsContent")')){
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

//返回
function forback(){
	asyncbox.confirm('$text.get('oa.common.edit.content')','$!text.get("clueTo")',function(action){
	　　 if(action == 'ok'){
			window.location.href="/OANews.do?operation=4&selectNews=$!ownNews.Id";
		}
	});
}

function submitZC(){
	if(checkForm()){
		$("#statuid").val("zc");
		form.newsContext.value = editor.html();
		form.submit();
	}
}

//弹出框回填内容


function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	jQuery.close('Popdiv');
}
</script>
	</head>
<body>
		<form action="/OANewsAction.do?operation=$globals.getOP("OP_UPDATE")" onSubmit="return checkForm();" name="form" method="post">
			<input type="hidden" name="popedomUserIds" id="popedomUserIds"
				value="$!ownNews.popedomUserIds" />
			<input type="hidden" name="popedomDeptIds" id="popedomDeptIds"
				value="$!ownNews.popedomDeptIds" />
			<input type="hidden" name="picFiles" value="$!ownNews.picFiles"/>
			<input type="hidden" name="delPicFiles" value=""/>
			<input type="hidden" name="statu" id="statuid" value="" />
			<input type="hidden" name="statusId" value="$!ownNews.statusId"/> 
			<input type="hidden" name="userName" value="$!ownNews.userName"/>
			<input type="hidden" name="releaseTime" value="$!ownNews.releaseTime"/>
			<input  type="hidden" name="position" value="$!position"/> 
			<div class="bigTitle" style="background-image: none">
				<div class="new_button">
					
					#if($!ownNews.statusId == 1)
						<button type="submit" name="Submit" class="fb" ></button>
					#end
					 <button type="button" onclick="submitZC();" class="save"></button>
					 <button type="button" onClick="forback()" class="fh" ></button>
				</div>
			</div>
			<div  id="nn" style="overflow:hidden;overflow-y:auto;width:100%;">
<script type="text/javascript">
	var oDiv=document.getElementById("nn");
	var sHeight=document.documentElement.clientHeight-55;
	oDiv.style.height=sHeight+"px";
</script>
				<table width="100%" border="0" cellpadding="3" cellspacing="0">
				<br/>
					<tr>
						<td class="oabbs_tr" width="10%">
							$text.get("oa.common.title")：<font color="red">*</font>
						</td>
						<td>
							<input type="hidden" name="newsId" value="$!ownNews.Id" />
							<input name="newsTitle" id="hh" type="text" value="$!ownNews.newsTitle"
								size="55" class="easyui-validatebox" required="true"   />
						</td>
					</tr>
					<tr>
						<td class="oabbs_tr">
							$text.get("oa.common.newsType")：


						</td>
						 <td>
							<label for="select"></label>
							<select name="newsType" id="select">
								#foreach($row_newsType in $globals.getEnumerationItems("NewsType"))
								<option value="$row_newsType.value"
									#if($!ownNews.newsType==$row_newsType.value)
									selected="selected" #end>
									$row_newsType.name
								</option>
								#end
							</select>
							<!--<a href="javascript:void(0)"
									onclick="openAttachUpload('/news/','PIC');">&nbsp;<span></span>
									<img src="/style1/images/oaimages/uploading.gif" />$text.get("upload.lb.picupload")</a>
									&nbsp;&nbsp;(注:上传的图片将显示在OA工作台和CRM工作台页,建议图片的高度和宽度分别为180和390!)
						--></td>
					</tr>
					<tr>
						<td class="oabbs_tr">
							图片上传：<font color="red">*</font>
						</td>
						<td>
							<a href="javascript:void(0)"
								onclick="openAttachUpload('/news/','PIC');">&nbsp;<span></span>
								<img src="/style1/images/oaimages/uploading.gif" />$text.get("upload.lb.picupload")</a>
								&nbsp;&nbsp;(建议:上传的图片将显示在OA工作台和CRM工作台页,建议图片的高度和宽度分别为180和390!)
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<div id="status" style="visibility:hidden;color:Red"></div>
					
								<div id="files_preview">
									#foreach($att in $ownNews.picFiles.split(",|;"))
									#if($att.length()>0)
									<div id="$att"
										style=" color:#ff0000;float:left;vertical-align:top">
										<a
											href="/ReadFile.jpg?type=PIC&tempFile=path&path=/news/&fileName=$att"
											target="_blank"><img
												src="/ReadFile.jpg?type=PIC&tempFile=path&path=/news/&fileName=$att"
												width="150" height="115" border="0"/> </a>
										<div
											style="top:0px; left:150px; float:right; ">
											<a href="javascript:;" onClick="removeFile('$att','PIC');">
												<img src="/$globals.getStylePath()/images/del.gif"/> </a>&nbsp;&nbsp;
										</div>
									</div>
									#end #end
								</div>		
						</td>
					</tr>
					<tr>
						<td class="oabbs_tr" valign="top">
							$text.get("oa.common.newContent")：<font color="red">*</font>

						</td>
						<td colspan="3">
							<textarea name="newsContext"
								style="width:80%;height:280px;visibility:hidden;">$!ownNews.newsContext</textarea>
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
						    #set ($isAlone = $ownNews.isAlonePopedmon) 
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
								type="radio" $!check1 value="0" id="radio"/>
							$text.get("oa.common.all")
							<input name="isAlonePopedmon" type="radio"
								onClick="chikeRadio(this)" $!check2 id="radiobutton" value="1"/>
							$text.get("oa.common.appoint")
						</td>
					</tr>
					<tr id="_trno" #if($isAlone=="0"||"$!isAlone"=="") style="display:none;" #end>
						<td class="oabbs_tr" id="_title">&nbsp;
						</td>
						<td valign="middle" colspan="3">
							<div class="oa_signDocument1" id="_context">
								<div class="oa_signDocument_3">
									<img src="/$globals.getStylePath()/images/St.gif"
										onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');" alt="$text.get("oa.common.adviceDept")" class="search"/>
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
									alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search"/>
							</div>
							<div class="oa_signDocument1" id="_context">
								<div class="oa_signDocument_3">
									<img src="/$globals.getStylePath()/images/St.gif"
										onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search"/>
									&nbsp;
									<a href="javascript:void(0)" title="$text.get("oa.common.advicePerson")" onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');">$text.get("oa.common.advicePerson")</a>
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
								<img onClick="deleteOpation('formatFileName','popedomUserIds');"
									src="/$globals.getStylePath()/images/delete_button.gif"
									alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")"  class="search"/>
							</div>

							<input type="hidden" name="popedomEmpGroupIds"
								id="popedomEmpGroupIds" value="$!ownNews.popedomEmpGroupIds"/>
							<div class="oa_signDocument1" id="_context">
								<div class="oa_signDocument_3">
									<img src="/$globals.getStylePath()/images/St.gif"
										onClick="deptPop('empGroup','EmpGroup','popedomEmpGroupIds','1');"
										alt="$text.get("oa.oamessage.usergroup")" class="search"/>
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
								<img onClick="deleteOpation('EmpGroup','popedomEmpGroupIds')"
									src="/$globals.getStylePath()/images/delete_button.gif"
									alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search"/>
							</div>
						</td>

					</tr>
					<tr>
						<td class="oabbs_tr">
							$text.get("oa.whetherAgreeReply.label")：


						</td>
						<td>
							#set($iwhetherAgreeReply=$ownNews.whetherAgreeReply)
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
							#set ($isSaveReading=$ownNews.isSaveReading)
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
