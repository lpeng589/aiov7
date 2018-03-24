<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link  type="text/css" rel="stylesheet" href="/style/css/plan.css" />
<link  type="text/css" rel="stylesheet" href="/style/css/fm.css" />
<link  type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="$globals.js("/js/workPlan.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>

<script type="text/javascript">


var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="summary"]',{
		uploadJson:'/UtilServlet?operation=uploadFile',
		resizeType : 0
	});
});

function onCompleteUpload(retstr){ 
	retstr = decodeURIComponent(retstr);    
    var strs = retstr.split(";");
    for(i=0;i<strs.length;i++){ 
    	if(strs[i] != ""){ 
    		insertNextFile(strs[i]);
    	}
    }  
    var attachUpload = document.getElementById("attachUpload");
	attachUpload.style.display="none";
}

function insertNextFile(str) {     
	if(typeof(document.form.attachFiles) == "undefined"){
		return;
	}
	var filevalue = document.form.attachFiles.value;
	if(filevalue.indexOf(str) == -1){	  
	 	var fileHtml = '';
	    fileHtml += '<li id ="'+str+'">';	  
	    fileHtml += '<a href="javascript:;" onclick="removeFile(\'' + str + '\');"><img src="/style/images/plan/del_01.gif"></a>&nbsp;&nbsp;';   
	    fileHtml += str+'  <input type=hidden name="attachFile" value="'+str+'"/></li>';		   
	    var fileElement = document.getElementById("files_preview");
	    fileElement.innerHTML = fileElement.innerHTML + fileHtml;  
	    document.form.attachFiles.value = filevalue+str+";";
	}
}

/*验证是否为空*/
function isNull(variable,message){
	if(variable=="" || variable==null){
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else{
		return true;
	}		
}

function beforSubmit(){
	var time = document.form.time.value ;
	if(time == null || time == ""){
		alert("耗时不能为空!") ;
		jQuery('#time').focus() ;
		return false ;
	}
	/*if(!gtZero(time)){
		alert("$text.get("common.lb.useTime")"+"必须是大于0的数字!") ;
		return false ;
	}*/
	
	if(time.indexOf(".")>-1 && time.length-time.lastIndexOf (".")>2){
		alert("$text.get("common.lb.useTime")"+"必须到小数点后1位!") ;
		return false ;
	}
	
	var conText=editor.text();
	if(!isNull(conText,'$text.get("common.lb.content")'))	{
		return false;
	}
	var content= editor.html();
  	document.form.summary.value = content ;
	if(document.form.statusId.value == "1" && !isNull(document.form.time.value,'$text.get("common.lb.useTime")'))	{
		return false;
	}
	form.submit() ;
}

function showOrHide(strId){
	var str = jQuery("#"+strId)
	if(str.css("display") == "block"){
		str.hide();
	}else{
		str.show();
	}
}

function closeWin(){
	parent.asyncbox.close('planId');
}

function back(){
	//window.location.href='/OAWorkPlanAction.do?operation=4&planType=$planType&userId=$result.employeeID&strDate=$strDate&score=$!score&planStatus=$!planStatus&winCurIndex=$!winCurIndex#if("$calendar"=="true")&opType=calendar#end' ;
	window.location.href="/OAWorkPlanAction.do?operation=4&planType=$!planType&keyId=$!keyId&winCurIndex=$!winCurIndex";
}
</script>

</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form name="form" method="post"  action="/OAWorkPlanAction.do"  target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="noback" value="$!noback">
<input type="hidden" name="planType" value="$!planType">
<input type="hidden" name="id" value="$!result.id">
<input type="hidden" name="strDate" value='$!strDate'>
<input type="hidden" name="employeeID" value='$!result.employeeID'>
<input type="hidden" name="departmentCode" value='$!result.departmentCode'>
<input type="hidden"  name="beginDate" value="$!result.beginDate"/>
<input type="hidden"  name="endDate" value="$!result.endDate"/>
<input type="hidden" name="winCurIndex" value='$!winCurIndex'>
<input type="hidden" name="opType" value='summary'>
<input type="hidden" name="score" value='$!score'>
<input type="hidden" name="planStatus" value='$!planStatus'>
<input type="hidden" name="keyId" value="$!keyId">
<input type="hidden" name="attachFiles" value="$!result.attach">
<input type="hidden" name="delFiles" value="">
<input type="hidden" name="flagId" id="flagId" value="$!planId"/>
	<table cellpadding="0" cellspacing="0" class="framework" style="margin:1px;">
		<tr>
			<!--左边列表开始-->
			<td>
				<div class="data">
				<!-- 
					<div class="data_top"></div> -->
					<ul class="new_workflow">
						<li><label>$text.get("common.lb.useTime"):</label><input type="text" id="time" name="time" style="width:40px;" #if("$!result.time" != "" && "$!result.time" != "0") value="$!result.time" #end onKeyDown="if(event.keyCode==13) return false"/><label>小时</label></li>
						<li><label>$text.get("stat.statusId"):</label>
						<!-- 
						<select name=statusId>
						<option value="0" #if($result.statusId == "0") selected #end>暂缓</option>
						<option value="1" #if($result.statusId == "1") selected #end>$text.get("oa.bbs.finished")</option>
						<option value="2" #if($result.statusId == "2") selected #end>继续</option>
						<option value="2" #if($result.statusId == "2") selected #end>终止</option>
						</select> -->
						<span><input name="statusId" type="radio" value="1" #if($result.statusId == "1" || $result.statusId == "0") checked="checked" #end id="status_1"/><label for="status_1">完成</label></span>
						<span><input name="statusId" type="radio" value="4" #if($result.statusId == "4") checked="checked" #end id="status_4"/><label for="status_4">暂缓</label></span>
						<span><input name="statusId" type="radio" value="3" #if($result.statusId == "3") checked="checked" #end id="status_3"/><label for="status_3">继续</label></span>
						<span><input name="statusId" type="radio" value="2" #if($result.statusId == "2") checked="checked" #end id="status_2"/><label for="status_2">终止</label></span>
						</li>
					</ul>
					<div class="workflow_data">
						<div><textarea name="summary" style="width:100%;height:235px;visibility:hidden;">$!result.summary</textarea></div>
						<div class="workflow_data_bu" style="width:100%">
						<a class="upload_a" href="javascript:openAttachUpload('/plan/');" style="display:block;float:left;">上传附件</a>
						<span id="files_preview" style="float: left;">				  		  
					  	#foreach ($str in $globals.strSplit($!result.attach,';'))
							 <li id ="$str">
						  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/plan/del_01.gif"></a>
						  	 $str<input type=hidden name="attachFile" value="$str"/>
						  	 </li>	
						#end
				    	</span>
				        #if("$!planType"=="event")
						<span>
						<button type="button" class="bu_02" onclick="beforSubmit();">保存</button>
						<button type="button" class="bu_02" onclick="back();">返回</button>
						</span>
						#end
						</div>
					</div>
				</div>
			</td><!--左边列表结束-->
			<!--右边菜单开始-->
			<td class="rightMenu" style="width:180px;">
			<div style="height:380px;overflow:hidden;overflow-y:auto;">
				#foreach($row in $associate)
				#if("$row.name" != "联系人")
				<div class="WP_title" ondblclick="showOrHide('ass$!result.id$row.id');">
					<span class="on">$row.name</span>
					<a href="javascript:openPop('$!LoginBean.id','','$row.popSelect','$row.name','$row.id','$row.isEmployee','$row.linkAddress','ass$!result.id$row.id')"><img src="/style/images/plan/Ic_005.gif" /></a>
				</div>
				#if("$!planType" == "event")
				<ul id="ass$!result.id$row.id">					
					#foreach($item in $assItem)																		
					#if($globals.get($item,1) == $row.id)
					<li id="${result.id}_${row.id}_$globals.get($item,2)">
					<a href="javascript:delAssoicate('$result.id','${row.id}','$globals.get($item,2)','true')"><img src="/style/images/plan/del_01.gif" /></a>
					<span onclick="openLink('$globals.get($item,2)','$row.linkAddress')">#if($globals.get($item,3).length() >15) $globals.get($item,3).substring(0,15) #else $globals.get($item,3) #end</span>
					#if("$row.isEmployee"=="1" && "$globals.get($item,2)"!="$!LoginBean.id")
						<a href="javascript:msgComm('$globals.get($item,2)','$globals.get($item,3)')"><img src="/style/images/plan/pp_01.gif" /></a>
					#end
					<input type="hidden" name="assoicate" value="${row.id}:$globals.get($item,2):$globals.get($item,3):0:0"/></li>			
					 #end
					 #end
				</ul>
				#else
				<ul id="ass$!result.id$row.id">													
					#foreach($item in $assItem.get($!result.id))										
					#if($globals.get($item,1) == $row.id)
					<li id="${result.id}_${row.id}_$globals.get($item,2)">
					<a href="javascript:delAssoicate('$result.id','${row.id}','$globals.get($item,2)','true')"><img src="/style/images/plan/del_01.gif" /></a>
					<span onclick="openLink('$globals.get($item,2)','$row.linkAddress')">#if($globals.get($item,3).length() >15) $globals.get($item,3).substring(0,15) #else $globals.get($item,3) #end</span>
					#if("$row.isEmployee"=="1" && "$globals.get($item,2)"!="$!LoginBean.id")
						<a href="javascript:msgComm('$globals.get($item,2)','$globals.get($item,3)')"><img src="/style/images/plan/pp_01.gif" /></a>
					#end
					<input type="hidden" name="assoicate" value="${row.id}:$globals.get($item,2):$globals.get($item,3):0:0"/></li>			
					 #end
					 #end
				</ul>
				#end
				#end
				#end	
			</div>	
			</td><!--右边菜单结束-->
		</tr>
	</table>
</form>
</body>
</html>