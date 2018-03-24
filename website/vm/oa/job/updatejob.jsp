<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("oa.job.updatejob")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
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

function openInputDate(obj)
{
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


function showStatus(obj)  {
   document.getElementById("status").style.visibility="visible";
}
  //获取数据库里类别
function loadType() {
  	//获取数据库的类别，在隐表单域里


  	var types = document.getElementById("isOrNot").value;
  	if(types!=null && types!=""){
	  	var jobType2 = document.getElementById("jobType2").value;
	  	if(types==jobType2){
	  		document.getElementById("jobType2").checked=true;
	  	}
	  	else{
	  		document.getElementById("jobType1").checked=true;
	  	}
  	}
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

function forback(){
	if(confirm("$text.get('oa.common.edit.content')")==true){
		window.location.href="/OAJob.do?winCurIndex=2&createPerson=$!createPerson&createPersonName=$globals.encode($!createPersonName)&jobtheme=$globals.encode($!jobtheme)&jobBeginTime=$!jobBeginTime&jobEndTime=$!jobEndTime" ;
	}
}
</script>
</head>
<body onLoad="loadType()">
<iframe name="formFrame" style="display:none"></iframe>
<form id="form" name="form" method="post" onsubmit="return checkSubmit(this)" action="/OAJob.do" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
<input type="hidden" name="attachFiles" value="$!result.attaches">
<input type="hidden" name="delFiles" value="">

 <input type="hidden" name="winCurIndex1" value="$!winCurIndex">
 <!--查询条件-->
 <input name="createPerson1" type="hidden" value="$!createPerson">
 <input name="createPersonName1" type="hidden" value="$!createPersonName">
 <input name="jobtheme1" type="hidden" value="$!jobtheme">
 <input name="jobBeginTime1" type="hidden" value="$!jobBeginTime">
 <input name="jobEndTime1" type="hidden" value="$!jobEndTime">
 
<div class="Heading">
	<div class="HeadingTitle">$text.get("oa.job.updatejob")</div>
	<ul class="HeadingButton">
				<li><button type="submit" name="Submit" class="hBtns">$text.get("mrp.lb.save")</button></li>
				<li>
				<button type="button" onClick="forback()" class="hBtns">$text.get("mrp.lb.back")</button>
				</li>
						
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
				<td width="80%">
				<!-- 主题 -->
				<input name="oaid" type="hidden" value="$!result.id">
				<input name="createPerson" type="hidden" value="$!result.createPerson">
					<input name="jobtheme" type="text" size="60" class="text" value="$!result.jobtheme">&nbsp;<font color="#FF0000">* </font>
				</td>
			</tr>
			<tr>
			 <td class="oabbs_tr">$text.get("customTable.lb.tableType")：</td>
			 <td>
			  <!-- 类型 -->
			 <input name="isOrNot" type="hidden" value="$!result.jobType">
			 	<input name="jobType" id="jobType1" type="radio" value='$text.get("oa.job.common")' />$text.get("oa.job.common")
			 	<input name="jobType" id="jobType2" type="radio" value='$text.get("oa.job.jinji")' />$text.get("oa.job.jinji")
		      </td>
			</tr>	
<tr>
			 <td class="oabbs_tr">$text.get("oa.jobodd.participant")：</td>
			 <td>
			 <!-- 参与者 -->
			 <input name="participant" id="participant" type="hidden" value="$!result.participant"/>
			<input name="participantName" id="participantName" type="text" class="text" readonly="readonly" value="$!participantNames" onDblClick="deptPopForJob('userGroup','participantName','participant');"/>
			<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForJob('userGroup','participantName','participant');" class="search"/><font color="#FF0000">* </font>
			
		      </td>
			</tr>
			<tr>
			 <td class="oabbs_tr">$text.get("scope.lb.tsscopeValue")：</td>
			 <td>
			 <!-- 开始时间与结束时间 -->
			 <input name="createTime" type="hidden" value="$!result.createTime">
			 <input name="jobBeginTime" class="text" value="$beginTime"  readonly="readonly" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  >
			 <select name="jobBeginTimeHour">
					#foreach($hour in [0..23])
						#if($hour<10)
							<option value="$hour" #if($hour==$jobBeginTimeHour) selected="selected" #end>0$hour</option>
						#else
							<option value="$hour" #if($hour==$jobBeginTimeHour) selected="selected" #end>$hour</option>
						#end
					#end
				</select>&nbsp;$text.get("oa.calendar.hour")
				<select name="jobBeginTimeMinute">
				#foreach($minute in [0..59])
					#if($minute<10)
						<option value="$minute" #if($minute==$jobBeginTimeMin) selected="selected" #end>0$minute</option>
					#else
						<option value="$minute" #if($minute==$jobBeginTimeMin) selected="selected" #end>$minute</option>
					#end
				#end
				</select>&nbsp; $text.get("oa.calendar.minutes")&nbsp;<font color="#FF0000">* </font>
			 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		      </td>
			</tr>
			<tr>
			  <td class="oabbs_tr">$text.get("oa.job.endtime")：</td>
			  <td>
				 <input name="jobEndTime" class="text"  readonly="readonly" value="$endTime"  onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  >
				 <select name="jobEndTimeHour">
					#foreach($hour in [0..23])
						#if($hour<10)
							<option value="$hour" #if($hour==$jobEndTimeHour) selected="selected" #end>0$hour</option>
						#else
							<option value="$hour" #if($hour==$jobEndTimeHour) selected="selected" #end>$hour</option>
						#end
					#end
					</select>&nbsp;$text.get("oa.calendar.hour")
					<select name="jobEndTimeMinute">
					#foreach($minute in [0..59])
						#if($minute<10)
							<option value="$minute" #if($minute==$jobEndTimeMin) selected="selected" #end>0$minute</option>
						#else
							<option value="$minute" #if($minute==$jobEndTimeMin) selected="selected" #end>$minute</option>
						#end
					#end
					</select>&nbsp; $text.get("oa.calendar.minutes")&nbsp;<font color="#FF0000">* </font>
			  </td>
			</tr>
			<tr>
			 <td class="oabbs_tr">$text.get("check.lb.user")：</td>
			 <td>
			 <!-- 审核人 -->
			 <input name="state" type="hidden" value="$!result.state"/>
			 <input name="assessor" id="assessor" type="hidden" value="$!result.assessor"/>
			 <input name="assessorName" id="assessorName" readonly="readonly" type="text" class="text" value="$!assessorNames" onDblClick="deptPopForJob('userGroup','assessorName','assessor');"/>
			<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForJob('userGroup','assessorName','assessor');" class="search"/><font color="#FF0000">* </font>
			
			
			
		      </td>
			</tr>
			<tr>
			<!-- 关联单位 -->
			 <td class="oabbs_tr">$text.get("oa.jobodd.InterfixUnit")：</td>
			 <td>
			 <input name="intterfixServer" id="intterfixServer" type="hidden" value="$!result.intterfixServer"/>
			 <input name="intterfixServerName" id="intterfixServerName" readonly="readonly" type="text" class="text" value="$!IntterfixServerName" onDblClick="deptPopForJob('CrmClickGroup','intterfixServerName','intterfixServer');"/>
			 <img src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForJob('CrmClickGroup','intterfixServerName','intterfixServer');" class="search"/>
			 
		      </td>
			</tr>	
			
						<tr>
						<!-- 详细说明 -->
			 <td valign="top" class="oabbs_tr">$text.get("oa.job.ElaborateOn")：</td>
			 <td>
		<textarea name="elaborateOn" style="width:800px;height:300px;visibility:hidden;">$!result.elaborateOn</textarea>
			   </td>
			</tr>
		<tr>
				<td class="oabbs_tr">$text.get("upload.lb.affix")：</td>
				<td>
					<span id="files"> 
							<button type=button class="b2" onClick="openAttachUpload('/affix/OAJobodd/')">$text.get("oa.common.accessories")</button>
					</span>
					<div id="status" style="visibility:hidden;color:Red"></div>
					<div id="files_preview">
				 #if($!result.attaches.indexOf(";") > 0)
				  #foreach ($str in $globals.strSplit($!result.attaches,';'))
				  	 <div  id ="$str" style ="height:18px; color:#ff0000;">
				  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
				  	 $str<input type=hidden name="attachFile" value="$str"/></div>					 
				  #end	
			 	 #end
					</div>
				</td>
			</tr>
			
			 <tr>
			<td class="oabbs_tr">$text.get("oa.readingMark.save")：</td>
			<td>
				#set ($isSaveReading="$!result.isSaveReading")
					#if($isSaveReading=="1")
						#set ($c1="checked")
					#else
						#set ($c2="checked")
					#end
					
			 <input name="isSaveReading" type="radio" value="1" $!c1>
			  $text.get("oa.common.yes")
			 <input name="isSaveReading" type="radio" value="0" $!c2>
			  $text.get("oa.common.no")		
			</td>
			</tr>
		  </tbody>
		</table>	
		</div>
</div>
</body>
</html>
