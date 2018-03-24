<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.job.addjob")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script type="text/javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="auditing"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

var editor2  ;
KindEditor.ready(function(K) {
	editor2 = K.create('textarea[name="participantRestore"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

	
function saveReadingInfo(){
	if("$!result.isSaveReading"=="1"){
 		var str="/UtilServlet?operation=addOAReadingInfo&infoId=$!result.id&infoTable=OAJobodd";
 	     AjaxRequest(str);
    	var value = response;		
		/*if(value=="1"){			 	
			alert('$text.get("mrp.help.saveSucess")');
		}
		else
		{
		alert('$text.get("common.alert.updateFailure")');
		}*/
		
		}
}
	//下载文件
function downFile(filepath){
		document.location = "OAMyData.do?type=downFile&filename="+filepath+"&mydataId=$file.id";
}

var emailType = "inner";

function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function openSelect2(obj,field){
	urlstr = '/UserFunctionAction.do?tableName=OAMessage&selectName=MsgReceive&operation=$globals.getOP("OP_POPUP_SELECT")';
	var str  = window.showModalDialog(urlstr+"&MOID=17&MOOP=add","","dialogWidth=730px;dialogHeight=450px"); 
	fs=str.split(";");  
	document.getElementById(field).value+=fs[0]+",";
	document.getElementById(obj).value+=fs[1]+",";		

}

function checkForm(){
	var newsTitle = form.newsTitle.value;
	var Notepadcontent =  form.Notepadcontent.value;
	if(!isNull(newsTitle,'$text.get("oa.subjects")')){
		return false;
	}
	if(newsTitle.length>1000){
		alert("$text.get("oa.job.themenot1000")") ;
		return false ;
	}
	if(!isNull(Notepadcontent,'$text.get("oa.common.newContent")')){
		return false;
	}
	if(Notepadcontent.length>8000){
		alert("$text.get("oa.job.elaborateOnnot8000")") ;
		return false ;
	}
	return true;	
}

function resetEditor(){
	editor.html("");
}


function checkAction(submitType){
  	if(submitType=="RestoreSubmit"){
	  	document.getElementById("form").action='/OAJob.do?operation=$globals.getOP("OP_CHECK")&createPerson=$!createPerson&createPersonName=$globals.encode($!createPersonName)&jobtheme=$globals.encode($!jobtheme)&jobBeginTime=$!jobBeginTime&jobEndTime=$!jobEndTime';
		var Content= editor2.html();
		form.participantRestore.value = Content ;
		if(Content=="")
		{
			alert("$text.get("common.lb.NoRevert")");
		}
		else
	  	{
	  		form.submit();
	  	}
  	}
  	if(submitType=="AuditingSubmit"){
  		form.auditing.value = editor.html() ;
  		form.action='/OAJob.do?operation=$globals.getOP("OP_CHECK")&createPerson=$!createPerson&createPersonName=$globals.encode($!createPersonName)&jobtheme=$globals.encode($!jobtheme)&jobBeginTime=$!jobBeginTime&jobEndTime=$!jobEndTime';
  		form.submit();
  	}
}
</script>
	</head>
	<body onLoad="saveReadingInfo()">
		<iframe name="formFrame" style="display:none"></iframe>
		<form action="/OAJob.do?operation=$globals.getOP('OP_CHECK')" name="form"  id="form" method="post" target="formFrame">
		 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
		  <input type="hidden" name="userType" value="$!userType">
		 <input type="hidden" name="attachFiles" value="">
<input type="hidden" name="delFiles" value="">
			<div class="Heading">
				<div class="HeadingTitle">
					$text.get("oa.job.jobDetail")
				</div>
				<ul class="HeadingButton">
					#if("$isAuditing"!="true" && "$isRestore"=="true")
					<li><button type="button" name="RestoreSubmit" onClick="checkAction('RestoreSubmit')" class="hBtns">
										$text.get("mrp.lb.save")									</button></li>
									<!--  保留重置按扭
									<button type="reset" onClick="resetEditor();" class="b2">
										$text.get("common.lb.reset")
									</button>
									-->	
					#end
					#if("$state"=="flase")
					#if("$isAuditing"=="true" || ("$isAuditing"=="true" && "$isRestore"=="true"))
					#if("$rsAuditing"=="false")
					<li><button type="button" name="AuditingSubmit" onClick="checkAction('AuditingSubmit')" class="hBtns">
										$text.get("mrp.lb.save")									</button></li>
									<!--  保留重置按扭
									<button type="reset" onClick="resetEditor();" class="b2">
										$text.get("common.lb.reset")
									</button>
									-->
					#end
					#end
					#end
			#if("$!currentUser"=="$!result.createPerson")
				#if($isSaveReading == 1)
				<li><button type="button" onClick="location.href='/OAReadingRecordAction.do?readingInfoId=$!result.id&readingInfoTable=OAJobodd&url=$!favoriteURL'" style="width:80px;" class="hBtns">$text.get("oa.readingMark")</button></li>
				#end
			#end
			
			#if("$backtype"!="true")
			<li>
			<button type="button" onClick="location.href='/OAJob.do?winCurIndex=$!winCurIndex&createPerson=$!createPerson&createPersonName=$globals.encode($!createPersonName)&jobtheme=$globals.encode($!jobtheme)&jobBeginTime=$!jobBeginTime&jobEndTime=$!jobEndTime'" class="hBtns">$text.get("mrp.lb.back")</button>
			</li>
			#else
			<li>
				<button type="button" onClick="closeWin();" class="b2">
					$text.get("common.lb.close")
				</button>
			</li>
			#end
				
			#if($LoginBean.operationMap.get("/OAJob.do").sendEmail())
			<li><button type="button" name="addPre" onClick="location.href='/OAJob.do?type=sendEmail&operation=5&winCurIndex=$!winCurIndex&keyId=$!result.id'" class="b3">$text.get("common.lb.sendmail")</button></li>	
			#end
				</ul>
			</div>
			<div id="listRange_id">
				<script type="text/javascript">
var oDiv=document.getElementById("listRange_id");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
				<div class="oalistRange_scroll_1">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="oalistRange_list_function" name="table">
						<thead>
							<tr>
								<td colspan="2" class="oabbs_tl">&nbsp;								</td>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="oabbs_tr" width="20%">
									$text.get("oa.subjects")：								</td>
								<td width="80%">
									<!-- 主题 -->
									$!result.jobtheme &nbsp;
									<input name="oajoboddId" type="hidden" value="$!result.id">								</td>
							</tr>
							<tr>
								<td class="oabbs_tr">
									$text.get("customTable.lb.tableType")：								</td>
								<td>
									<!-- 类型 -->
									$!result.jobType&nbsp;								</td>
							</tr>
							<tr>
								<td class="oabbs_tr">
									$text.get("oa.jobodd.participant")：								</td>
								<td>
									<!-- 参与者 -->
									$!participantNames&nbsp;								</td>
							</tr>
							<tr>
								<td class="oabbs_tr">
									$text.get("oa.mydata.creatTime")：								</td>
								<td>
									$!result.createTime &nbsp;
									<!-- 开始时间 -->
									$text.get("scope.lb.tsscopeValue")：






									$!result.jobBeginTime &nbsp; 
									<!-- 结束时间 -->
									$text.get("oa.job.endtime")：






									$!result.jobEndTime&nbsp;								</td>
							</tr>
							<tr>
								<td class="oabbs_tr">
									$text.get("check.lb.user")：								</td>
								<td>
									<!-- 审核人 -->
									$!assessorNames&nbsp;								</td>
							</tr>
							<tr id="server">
								<td class="oabbs_tr">
									$text.get("oa.jobodd.InterfixUnit")：								</td>
								<td>
									<!-- 关联单位 -->
									$!IntterfixServerName&nbsp;								</td>
							</tr>
							<tr>
								<td class="oabbs_tr">
									$text.get("oa.job.ElaborateOn")：								</td>
								<td>
									<!-- 详细说明 -->
									$!result.elaborateOn&nbsp;								</td>
							</tr>
							<tr>
								<td class="oabbs_tr">
									$text.get("upload.lb.affix")：</td>
								<td>
									<!-- 附件 -->
					
					#foreach ($str in $globals.strSplit($!result.attaches,';'))
					 <img src="/$globals.getStylePath()/images/down.gif">
						<a href='/ReadFile?tempFile=path&path=/affix/OAJobodd/&fileName=$!globals.encode($!str)' target="formFrame">
								$str</a>			
					#end
				
					</a>&nbsp;&nbsp;&nbsp;<br>								</td>
							</tr>
							<!-- 显示回复内容 -->
							<tr>
							<td class="oabbs_tr">$text.get("message.lb.revert")：</td>
							
							<td>
				#foreach($!row in $!RestoreList)
				<div style=" background:#E8F3FF; color:#666666; border-top: 1px solid #77BBEE; height:25px; line-height:25px; padding-left:4px; vertical-align:baseline">$text.get("oa.bbs.peopleofrevertto")：<span class="STYLE2">$!row.participantPerson</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$text.get("oa.bbs.reverttoas")：<span class="STYLE2"> $!row.restoreTime	</span></div>
				<div style="padding-left:5px; margin-top:5px; margin-bottom:5px;">#foreach ($str in $globals.strSplit($!row.attaches,';'))
					 <img src="/$globals.getStylePath()/images/down.gif">
						<a href='/ReadFile?tempFile=path&path=/affix/OAJobodd/&fileName=$!globals.encode($!str)'>
								$str</a>			
				#end $!row.participantRestore	
				
				</div>
				#end				&nbsp;</td>
							
							
							</tr>

			<tr>
							<td class="oabbs_tr">$text.get("common.Auditing")：</td>
							
							<td>
					#foreach($!rows int $!AuditingList)
				<div style=" background:#E8F3FF; color:#666666; border-top: 1px solid #77BBEE; height:25px; line-height:25px; padding-left:4px; vertical-align:baseline">$text.get("check.lb.user")：<span class="STYLE2">$!rows.assessor</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$text.get("oa.job.approval.time")：<span class="STYLE2"> $!rows.auditingTime</span></div><div style="padding-left:5px; margin-top:5px; margin-bottom:5px;">$text.get("check.lb.status")：<span class="STYLE2">
				#if($!rows.state=="pass")$text.get("check.lb.pass")#else$text.get("check.lb.notPass")#end #foreach ($str in $globals.strSplit($!rows.attaches,';'))
					 <img src="/$globals.getStylePath()/images/down.gif">
						<a href='/ReadFile?tempFile=path&path=/affix/OAJobodd/&fileName=$!globals.encode($!str)'>
								$str</a>			
				#end</span></div><div style="padding-left:5px; margin-top:5px; margin-bottom:5px;"><span class="STYLE2"> $!rows.auditing</span>
				</div>
				#end				&nbsp;</td>
							
							
							</tr>
							

							
							
							
							
							<!-- 判断最终的审核 -->
							#if("$state"=="flase")
							<!-- 判断当前用户是否审核过 -->
								#if("$isAuditing"=="true" || ("$isAuditing"=="true" && "$isRestore"=="true"))
									<!-- 判断当前用户是否审核通过，未通过可以继续审核 -->
									#if("$rsAuditing"=="false")
							<tr>
								<td class="oabbs_tr">
									$text.get("check.lb.status")：</td>
								<td>
									<!-- 审核操作 -->
									<input name="state" type="radio" checked="checked" value='pass' />
									$text.get("check.lb.pass")
									<input name="state" type="radio" value='notPass' />
									$text.get("check.lb.notPass")&nbsp;								</td>
							</tr>
							<tr>
								<td valign="top" class="oabbs_tr">
									$text.get("oa.job.approval.illuminate")：</td>
								<td>
								<!-- 审核人 -->
								<input name="editType" type="hidden" value="Auditing">
								<input name="assessor" type="hidden" value="$LoginBean.id;">
								<textarea name="auditing" style="width:800px;height:300px;visibility:hidden;"></textarea>
									</td>
							</tr>
							<tr>
							<td class="oabbs_tr">$text.get("upload.lb.affix")：</td>
							<td>
								<span id="files"> 
										<button type=button class="b2" onClick="openAttachUpload('/job/')">$text.get("oa.common.accessories")</button>
								</span>
								<div id="status" style="visibility:hidden;color:Red"></div>
								<div id="files_preview"></div>				</td>
						  </tr>
							
									#end
								#end 
							#end
								#if("$isAuditing"!="true" && "$isRestore"=="true")
							<tr>
								<td valign="top" class="oabbs_tr">
									$text.get("message.lb.revert")：</td>
								<td>
								<!-- 回复人 -->
								<input name="editType" type="hidden" value="Restore">
								<input name="participantPerson" type="hidden" value="$LoginBean.id;">
								<textarea name="participantRestore" style="width:800px;height:300px;visibility:hidden;"></textarea>
								</td>
							</tr>
							<tr>
							<td class="oabbs_tr">$text.get("upload.lb.affix")：</td>
							<td>
								<span id="files"> 
										<button type=button class="b2" onClick="openAttachUpload('/job/')">$text.get("oa.common.accessories")</button>
								</span>
								<div id="status" style="visibility:hidden;color:Red"></div>
								<div id="files_preview"></div>				</td>
						  </tr>
							
								
							#end




						</tbody>
					</table>
					
				</div>
			</div>
			</from>
	</body>