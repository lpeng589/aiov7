<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("oa.job.addjob")</title>
<link type="text/css" href="/$globals.getStylePath()/css/classFunction.css" rel="stylesheet" />
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css"  rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="elaborateOn"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

var fieldNames;
var fieldNIds;
function deptPopForJob(popname,fieldName,fieldNameIds){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({
		title : '请选择人员',
	    id : 'Popdiv',
	　　 　url : urls,
	　　 　width : 755,
	　　　 height : 430,
		btnsbar :[{text:'清空',action:'remove'}].concat(jQuery.btn.OKCANCEL),
		callback : function(action,opener){
	　　　　　	//判断 action 值。


	　　　　　	if(action == 'ok'){
	　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。


				var str = opener.strData;
				newOpenSelectSearch(str,fieldName,fieldNameIds);
	　　　　　	}
			if(action == "remove"){ //清空数据
				removeData(fieldName,fieldNameIds);
			}
　　　	 	}
　	});
}

function newOpenSelectSearch(str,fieldName,fieldNameIds){
	var employees = str.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = getValueById(fieldName);
			if(!((";"+$("#"+fieldName).val()).indexOf(";"+field[1]+";")>-1)){
				$("#"+fieldName).val($("#"+fieldName).val()+field[1]+";");
				$("#"+fieldNameIds).val($("#"+fieldNameIds).val()+field[0]+";");
			}
		}
	}
}

//弹出框双击回填内容


function fillData(datas){
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv')
}

function compareDate(startDatetime,hour,minute,endDatetime,hour2,minute2){
	var sdStr = startDatetime.split("-") ;//开始时间


	var edStr = endDatetime.split("-") ;//结束时间
	var sd = new Date(sdStr[0],sdStr[1]-1,sdStr[2], hour, minute,"00");
	var ed = new Date(edStr[0],edStr[1]-1,edStr[2], hour2, minute2,"00");
	var diff = ed-sd;
	if (diff <=0){
		alert("$text.get('scope.time.timeCompare')");
		return false;
	}
	return true;
}

var emailType = "inner";
function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function isexits(obj,array){
	var fs=array.split(";");
	for(i=0;i<fs.length;i++){
		if((fs[i]+";")==(obj)){
			return false;
		}
	}
	return true;			
}

function changeFileName(obj){
	var filePath = obj.value;
	var starIndex = filePath.lastIndexOf("\\");
	var endIndex = filePath.lastIndexOf(".");
	var fileName = filePath.substr(starIndex+1,(endIndex-starIndex-1));
	document.getElementById("fileName").value = fileName;
}

function showStatus(obj){
   document.getElementById("status").style.visibility="visible";
}
  
function checkSubmit(forms){
	if(!isNull(forms.jobtheme.value,'$text.get("oa.subjects")')){
  		return false;
  	}
  	if(!isNull(forms.participantName.value,'$text.get("oa.jobodd.participant")')){
  		return false;
  	}
    if(!isNull(forms.jobBeginTime.value,'$text.get("scope.lb.tsscopeValue")')){
  		return false;
  	}
    if(!isNull(forms.jobEndTime.value,'$text.get("oa.job.endtime")')){
  		return false;
  	}
  	if(!isNull(forms.assessorName.value,'$text.get("check.lb.user")')){
  		return false;
  	}
	if(getStringLength(forms.jobtheme.value)>100){
		alert("$text.get('oa.subjects')"+"$text.get('oa.common.not.more.than')"+100+"$text.get('oa.common.word.countOfCharacter')");
		return false ;
	}
	if(!isTitle(forms.jobtheme.value))	{
		alert("$text.get('oa.subjects')"+'$text.get("common.validate.error.any")');
	    return false;
	}
	if(!compareDate(forms.jobBeginTime.value,forms.jobBeginTimeHour.value,forms.jobBeginTimeMinute.value,forms.jobEndTime.value,forms.jobEndTimeHour.value,forms.jobEndTimeMinute.value)){
		return false;
	}
  	return true;
  }
  
  
function isNull(variable,message){
	if(variable=="" || variable==null){
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else{
		return true;
	}
}

function rt(){	
	editor.html("") ;
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>

<form id="form" name="form" method="post" action="/OAJob.do" onsubmit="return checkSubmit(this);" target="formFrame">

<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")"/>
<input type="hidden" name="attachFiles" value=""/>
<input type="hidden" name="delFiles" value=""/>

 <input type="hidden" name="winCurIndex1" value="$!winCurIndex"/>
<div class="Heading">
	<div class="HeadingTitle">$text.get("oa.job.jobodd")</div>
	<ul class="HeadingButton">
		<li><button type="submit" name="Submit" class="hBtns">$text.get("mrp.lb.save")</button></li>
	 	<li><button type="reset" name="set" class="hBtns" onClick="rt()">$text.get("common.lb.reset")</button></li>
		<li><button type="reset" onClick="location.href='/OAJob.do?winCurIndex=2'" class="hBtns">$text.get("mrp.lb.back")</button></li>	
	</ul>
</div>
<div id="listRange_id">
<script type="text/javascript">
$("#listRange_id").height($(window).height()-38);
</script>
    <div class="oalistRange_scroll_1">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="oabbs_tr" width="20%">$text.get("oa.subjects")：</td>
				<!-- 主题 -->
				<td width="80%">
				
				<input name="jobtheme" type="text" size="60" class="text"><font color="#FF0000">* </font></td>
			</tr>
			<tr>
			 <td class="oabbs_tr">$text.get("customTable.lb.tableType")：</td>
			 <!-- 类型 -->
			 <td><input name="jobType" type="radio" checked="checked" value='$text.get("oa.job.common")' />$text.get("oa.job.common")
			 <input name="jobType" type="radio" value='$text.get("oa.job.jinji")'/>$text.get("oa.job.jinji")
		      </td>
			</tr><tr>
			 <td class="oabbs_tr">$text.get("oa.jobodd.participant")：</td>
			 <td>
			 <!-- 参与者 -->
			 <input name="participant" type="hidden" id="participant" value=""/>
			<input name="participantName" id="participantName" type="text" readonly="readonly" class="text" onDblClick="deptPopForJob('userGroup','participantName','participant');"/>
			<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForJob('userGroup','participantName','participant');" class="search"><font color="#FF0000">* </font>
			
		      </td>
			</tr>
			<tr>
			 <td class="oabbs_tr">$text.get("scope.lb.tsscopeValue")：</td>
			 <!-- 开始时间-->
			 <td>
			 <input name="jobBeginTime" class="text" value=""  readonly="readonly" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  />
			<select name="jobBeginTimeHour">
					#foreach($hour in [0..23])
						#if($hour<10)
							<option value="$hour">0$hour</option>
						#else
							<option value="$hour">$hour</option>
						#end
					#end
					</select>&nbsp;$text.get("oa.calendar.hour")
					<select name="jobBeginTimeMinute">
					#foreach($minute in [0..59])
						#if($minute<10)
							<option value="$minute">0$minute</option>
						#else
							<option value="$minute">$minute</option>
						#end
					#end
					</select>&nbsp; $text.get("oa.calendar.minutes")&nbsp;<font color="#FF0000">* </font>
					
		      </td>
			</tr>
			<tr>
			 <td class="oabbs_tr"> $text.get("oa.job.endtime")：</td>
			 <!-- 与结束时间 -->
			 <td>
			<input name="jobEndTime"  readonly="readonly" class="text" value=""  onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  />
			<select name="jobEndTimeHour">
					#foreach($hour in [0..23])
						#if($hour<10)
							<option value="$hour">0$hour</option>
						#else
							<option value="$hour">$hour</option>
						#end
					#end
					</select>&nbsp;$text.get("oa.calendar.hour")
					<select name="jobEndTimeMinute">
					#foreach($minute in [0..59])
						#if($minute<10)
							<option value="$minute">0$minute</option>
						#else
							<option value="$minute">$minute</option>
						#end
					#end
					</select>&nbsp; $text.get("oa.calendar.minutes")&nbsp;<font color="#FF0000">* </font>
		      </td>
			</tr>
			<tr>
			 <td class="oabbs_tr">$text.get("check.lb.user")：</td>
			 <td>
			 <!-- 审核人 -->
			 <input name="state" type="hidden"  value="notApprove"/>
			 <input name="assessor" type="hidden" id="assessor" value="" />
			 <input name="assessorName"  id="assessorName" type="text" class="text" readonly="readonly" onDblClick="deptPopForJob('userGroup','assessorName','assessor');"/>
			<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForJob('userGroup','assessorName','assessor');" class="search"><font color="#FF0000">* </font>
		      </td>
			</tr>
			<tr id="server">
			<!-- 关联单位 -->
			 <td class="oabbs_tr">$text.get("oa.jobodd.InterfixUnit")：</td>
			 <td><input name="intterfixServer" id="intterfixServer" id="intterfixServer" type="hidden" value=""/>
	 		 <input name="intterfixServerName" id="intterfixServerName" type="text" class="text"  readonly="readonly" onDblClick="deptPopForJob('CrmClickGroup','intterfixServerName','intterfixServer');"/>
			 <img src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForJob('CrmClickGroup','intterfixServerName','intterfixServer');" class="search"/>
			 
			 </td>
			</tr>	
			<tr>
			 <td valign="top" class="oabbs_tr">$text.get("oa.job.ElaborateOn")：</td>
			 <td>
			 <!-- 详细说明 -->
		<textarea name="elaborateOn" style="width:800px;height:300px;visibility:hidden;"></textarea>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("upload.lb.affix")：</td>
				<td>
					<span id="files"> 
							<button type=button class="b2" onClick="openAttachUpload('/affix/OAJobodd/')">$text.get("oa.common.accessories")</button>
					</span>
					<div id="status" style="visibility:hidden;color:Red"></div>
					<div id="files_preview"></div>				
				</td>
			</tr>
			
			<tr>
			<td class="oabbs_tr">$text.get("oa.readingMark.save")：</td>
			<td>
			 <input name="isSaveReading" type="radio" value="1" />
			  $text.get("oa.common.yes")
			 <input name="isSaveReading" type="radio" checked value="0" />
			  $text.get("oa.common.no")		
			</td>
			</tr>
		  </tbody>
		</table>	
		</div></div>
		</form>
</body>
</html>