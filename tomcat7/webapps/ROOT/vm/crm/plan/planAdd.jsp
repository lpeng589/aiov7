<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="/style/css/plan.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="$globals.js("/js/workPlan.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>

<script type="text/javascript">
var varLoginId;
var varPlanId;
var varId;
var varIsEmployee;
var varLinkaddress;
var varObj;
var varPlanType ;
var varDisplayName ;
$(function(){

	//从CRM兄弟表工作计划新增计划,默认关联当前客户
	#if("$!enterFrom" == "CRMBrother")
		varLoginId = $("#employeeID").val();
		varPlanId = '';
		varId = '5';
		varIsEmployee = '2';
		varLinkaddress = '/CRMClientAction.do?operation=5&type=detailNew&keyId=assID';
		varObj = 'ass5';
		varDisplayName = '关联客户';
		exePopdiv('$!clientInfo');
	#end
})
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="content"]',{
		uploadJson:'/UtilServlet?operation=uploadFile',
		resizeType : 0
	});
});

function openInputDate(){
	WdatePicker({lang:'$globals.getLocale()'});
}

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
	    fileHtml += '<a href="javascript:;" onclick="removeFile(\'' + str + '\');"><img src="/style/images/plan/del_01.gif"></a>&nbsp;';   
	    fileHtml += str+'  <input type=hidden name="attachFile" value="'+str+'"/></li>';		   
	    var fileElement = document.getElementById("files_preview");
	    fileElement.innerHTML = fileElement.innerHTML + fileHtml;  
	    document.form.attachFiles.value = filevalue+str+";";
	}
}

/*验证是否为空*/
function isNull(variable,message){
	if(variable==null || jQuery.trim(variable)==""){
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else{
		return true;
	}		
}

function beforSubmit(type){
	var title = form.title.value ;
	if(!isNull(title,'$text.get("oa.subjects")')){
		return false;
	}
	
	#if($planType=="day")
	var date = jQuery("#planDate").val();
	if(!isNull(date,'时间')){
		return false;
	}
	
	bh = document.form.beginDateHour.value;	
	bm = document.form.beginDateMin.value;
	eh = document.form.endDateHour.value;
	em = document.form.endDateMin.value;
	
	document.form.beginDate.value = $("#planDate",document).val()+' '+bh+":"+bm+":00";
	document.form.endDate.value = $("#planDate",document).val()+' '+eh+":"+em+":00";
 	document.form.strDate.value = $("#planDate",document).val();
 	if(document.form.beginDate.value >=document.form.endDate.value){
 		alert("$text.get("scope.time.timeCompare")");
 		return false;
 	}
 	#end
 	var conText=editor.text();
	if(!isNull(conText,'$text.get("common.lb.content")'))	{
		return false;
	}
 	
	var content= editor.html();
  	document.form.content.value = content ;

	if(typeof(type)!="undefined"){
		document.form.submitType.value = type ;
	}
	form.submit();
}	

function showOrHide(strId){
	var str = jQuery("#"+strId);
	var sr = jQuery("#span_"+strId);
	var sum = jQuery("#"+strId+" li").length;
	if(str.css("display") == "block"){
		str.hide();
		/*jQuery("#imgasc_"+strId).remove();	
		jQuery("#imgdesc_"+strId).remove();	
		sr.after("<span id=\"imgasc_"+strId+"\" ><a onclick=\"showOrHide('"+strId+"')\">V ("+sum+")</a></span>");*/
		
	}else{
		str.show();	
		/*jQuery("#imgasc_"+strId).remove();	
		jQuery("#imgdesc_"+strId).remove();			
		sr.after("<span id=\"imgdesc_"+strId+"\" ><a onclick=\"showOrHide('"+strId+"')\">V ("+sum+")</a></span>");*/
	}
}

function closeWin(){
	parent.jQuery.close('planId');
}

function back(){
	window.location.href='/OAWorkPlanAction.do?operation=4&planType=$planType&userId=$result.employeeID&strDate=$strDate&score=$!score&planStatus=$!planStatus&winCurIndex=$!winCurIndex#if("$calendar"=="true")&opType=calendar#end' ;
}


/*
	新客户弹出框
	popName:弹出框类型userGroup:职员
*/
function openClientPop(){
	var url ="/Accredit.do?inputType=checkbox&popname=CrmClickGroup";
	asyncbox.open({
		id :'clientPopId',url:url,title:'选择客户',width:755,height:450,
		btnsbar :jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		var strArr = str.split("|");
	    		var retInfo = "";
	    		for(var i=0;i<strArr.length;i++){
	    			if("" != strArr[i].split(";")[0]){
		    			varLoginId = strArr[i].split(";")[1];
						varPlanId = '';
						varId = '5';
						varIsEmployee = '2';
						varLinkaddress = '/CRMClientAction.do?operation=5&type=detailNew&keyId=assID';
						varObj = 'ass$!{result.id}5';
						varDisplayName = '关联客户';
						exePopdiv(strArr[i]);
	    			}
	    		}
	    	}
	    }
　  });
}

/*双击回填字段*/
function fillData(datas){
	varLoginId = datas.split(";")[1];
	varPlanId = '';
	varId = '5';
	varIsEmployee = '2';
	varLinkaddress = '/CRMClientAction.do?operation=5&type=detailNew&keyId=assID';
	varObj = 'ass$!{result.id}5';
	varDisplayName = '关联客户';
	var strArr = datas.split(";")[0]+";"+datas.split(";")[1];
	exePopdiv(strArr);
	
	parent.jQuery.close('clientPopId');
}



</script>
<title>科荣AIO</title>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form name="form" method="post"  action="/OAWorkPlanAction.do" target="formFrame">
#if($!result.id != "")
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
#else
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")"/>
#end
<input type="hidden" name="noback" value="$!noback"/>
<input type="hidden" name="planType" value="$!planType"/>
<input type="hidden" name="id" value="$!result.id"/>
<input type="hidden" name="keyId" value="$!keyId"/>
<input type="hidden" name="strDate" value='$!strDate'/>
<input type="hidden" name="employeeID" value='$!result.employeeID'/>
<input type="hidden" name="departmentCode" value='$!result.departmentCode'/>
<input type="hidden" name="winCurIndex" value='$!winCurIndex'/>
<input type="hidden" name="calendar" value='$!calendar'/>
<input type="hidden" name="copy" value='$!copy'/>
<input type="hidden" name="score" value='$!score'/>
<input type="hidden" name="planStatus" value='$!planStatus'/>
<input type="hidden" name="attachFiles" value="$!result.attach"/>
<input type="hidden" name="delFiles" value=""/>
<input type="hidden" name="submitType" value=""/>
<input type="hidden" name="flagId" id="flagId" value="$!planId"/>
	<table cellpadding="0" cellspacing="0" class="framework" style="margin:1px;">
		<tr>
			<!--左边列表开始-->
			<td>
				<div class="data" style="width:730px;">
				<!-- 
					<div class="data_top"></div> -->
					<ul class="new_workflow" style="width:95%;padding:0;">
						<li><label>$text.get("oa.subjects")：</label><input type="text" name="title" style="width:300px;" value='$globals.revertTextCode3("$!result.title")' onKeyDown="if(event.keyCode==13) return false;"/><font color="red">*</font></li>
					<input type="hidden" id="beginDate"  name="beginDate" value="$!result.beginDate"/>
					<input type="hidden" id="endDate" name="endDate" value="$!result.endDate"/>
					#if($planType=="day")
						<!-- 
						<li><label>时间</label><input type="text" value="2012-01-10" style="color:#838383;" /><span>至</span><input type="text" value="2012-06-10" style="color:#e48e06;" /><a href="#">提醒</a></li>
						 -->
						<li><label style="margin-top:3px;">时间：</label><span style="width:100px;text-align:left;"><input id="planDate" name="planDate"  value="$!strDate" style="width:80px;float:left;" onclick="openInputDate();"/></span>
							<select name=beginDateHour style="width:40px">
							<option value="00" #if($result.beginDate.substring(11,13)=="00") selected #end>00</option>
							<option value="01" #if($result.beginDate.substring(11,13)=="01") selected #end >01</option>
							<option value="02" #if($result.beginDate.substring(11,13)=="02") selected #end >02</option>
							<option value="03" #if($result.beginDate.substring(11,13)=="03") selected #end >03</option>
							<option value="04" #if($result.beginDate.substring(11,13)=="04") selected #end >04</option>
							<option value="05" #if($result.beginDate.substring(11,13)=="05") selected #end >05</option>
							<option value="06" #if($result.beginDate.substring(11,13)=="06") selected #end >06</option>
							<option value="07" #if($result.beginDate.substring(11,13)=="07") selected #end >07</option>
							<option value="08" #if($result.beginDate.substring(11,13)=="08") selected #end >08</option>
							<option value="09" #if($result.beginDate.substring(11,13)=="09") selected #end >09</option>
							<option value="10" #if($result.beginDate.substring(11,13)=="10") selected #end >10</option>
							<option value="11" #if($result.beginDate.substring(11,13)=="11") selected #end >11</option>
							<option value="12" #if($result.beginDate.substring(11,13)=="12") selected #end >12</option>
							<option value="13" #if($result.beginDate.substring(11,13)=="13") selected #end >13</option>
							<option value="14" #if($result.beginDate.substring(11,13)=="14") selected #end >14</option>
							<option value="15" #if($result.beginDate.substring(11,13)=="15") selected #end >15</option>
							<option value="16" #if($result.beginDate.substring(11,13)=="16") selected #end >16</option>
							<option value="17" #if($result.beginDate.substring(11,13)=="17") selected #end >17</option>
							<option value="18" #if($result.beginDate.substring(11,13)=="18") selected #end >18</option>
							<option value="19" #if($result.beginDate.substring(11,13)=="19") selected #end >19</option>
							<option value="20" #if($result.beginDate.substring(11,13)=="20") selected #end >20</option>
							<option value="21" #if($result.beginDate.substring(11,13)=="21") selected #end >21</option>
							<option value="22" #if($result.beginDate.substring(11,13)=="22") selected #end >22</option>
							<option value="23" #if($result.beginDate.substring(11,13)=="23") selected #end >23</option>
						</select>&nbsp;$text.get("com.date.hour")
						<select name=beginDateMin style="margin-top:3px;width:40px">
							<option value="00" #if($result.beginDate.substring(14,16)=="00") selected #end>00</option>
							<option value="05" #if($result.beginDate.substring(14,16)=="05") selected #end >05</option>
							<option value="10" #if($result.beginDate.substring(14,16)=="10") selected #end >10</option>
							<option value="15" #if($result.beginDate.substring(14,16)=="15") selected #end >15</option>
							<option value="20" #if($result.beginDate.substring(14,16)=="20") selected #end >20</option>
							<option value="25" #if($result.beginDate.substring(14,16)=="25") selected #end >25</option>
							<option value="30" #if($result.beginDate.substring(14,16)=="30") selected #end >30</option>
							<option value="35" #if($result.beginDate.substring(14,16)=="35") selected #end >35</option>
							<option value="40" #if($result.beginDate.substring(14,16)=="40") selected #end >40</option>
							<option value="45" #if($result.beginDate.substring(14,16)=="45") selected #end >45</option>
							<option value="50" #if($result.beginDate.substring(14,16)=="50") selected #end >50</option>
							<option value="55" #if($result.beginDate.substring(14,16)=="55") selected #end >55</option>
						</select>&nbsp;$text.get("com.date.minute")
						&nbsp;$text.get("common.msg.until")&nbsp;
						<select name=endDateHour style="width:40px">
							<option value="00" #if($result.endDate.substring(11,13)=="00") selected #end>00</option>
							<option value="01" #if($result.endDate.substring(11,13)=="01") selected #end >01</option>
							<option value="02" #if($result.endDate.substring(11,13)=="02") selected #end >02</option>
							<option value="03" #if($result.endDate.substring(11,13)=="03") selected #end >03</option>
							<option value="04" #if($result.endDate.substring(11,13)=="04") selected #end >04</option>
							<option value="05" #if($result.endDate.substring(11,13)=="05") selected #end >05</option>
							<option value="06" #if($result.endDate.substring(11,13)=="06") selected #end >06</option>
							<option value="07" #if($result.endDate.substring(11,13)=="07") selected #end >07</option>
							<option value="08" #if($result.endDate.substring(11,13)=="08") selected #end >08</option>
							<option value="09" #if($result.endDate.substring(11,13)=="09") selected #end >09</option>
							<option value="10" #if($result.endDate.substring(11,13)=="10") selected #end >10</option>
							<option value="11" #if($result.endDate.substring(11,13)=="11") selected #end >11</option>
							<option value="12" #if($result.endDate.substring(11,13)=="12") selected #end >12</option>
							<option value="13" #if($result.endDate.substring(11,13)=="13") selected #end >13</option>
							<option value="14" #if($result.endDate.substring(11,13)=="14") selected #end >14</option>
							<option value="15" #if($result.endDate.substring(11,13)=="15") selected #end >15</option>
							<option value="16" #if($result.endDate.substring(11,13)=="16") selected #end >16</option>
							<option value="17" #if($result.endDate.substring(11,13)=="17") selected #end >17</option>
							<option value="18" #if($result.endDate.substring(11,13)=="18") selected #end >18</option>
							<option value="19" #if($result.endDate.substring(11,13)=="19") selected #end >19</option>
							<option value="20" #if($result.endDate.substring(11,13)=="20") selected #end >20</option>
							<option value="21" #if($result.endDate.substring(11,13)=="21") selected #end >21</option>
							<option value="22" #if($result.endDate.substring(11,13)=="22") selected #end >22</option>
							<option value="23" #if($result.endDate.substring(11,13)=="23") selected #end >23</option>
						</select>&nbsp;$text.get("com.date.hour")
						<select name=endDateMin style="margin-top:3px;width:40px">
							<option value="00" #if($result.endDate.substring(14,16)=="00") selected #end>00</option>
							<option value="05" #if($result.endDate.substring(14,16)=="05") selected #end >05</option>
							<option value="10" #if($result.endDate.substring(14,16)=="10") selected #end >10</option>
							<option value="15" #if($result.endDate.substring(14,16)=="15") selected #end >15</option>
							<option value="20" #if($result.endDate.substring(14,16)=="20") selected #end >20</option>
							<option value="25" #if($result.endDate.substring(14,16)=="25") selected #end >25</option>
							<option value="30" #if($result.endDate.substring(14,16)=="30") selected #end >30</option>
							<option value="35" #if($result.endDate.substring(14,16)=="35") selected #end >35</option>
							<option value="40" #if($result.endDate.substring(14,16)=="40") selected #end >40</option>
							<option value="45" #if($result.endDate.substring(14,16)=="45") selected #end >45</option>
							<option value="50" #if($result.endDate.substring(14,16)=="50") selected #end >50</option>
							<option value="55" #if($result.endDate.substring(14,16)=="55") selected #end >55</option>
						</select><sapn style="border:none; ">$text.get("com.date.minute")</sapn>
						</span>
						<span style="float:right;margin-left:20px;margin-top:3px;">重要程度：<select name=grade style="margin-top:3px">
					#foreach($row in $globals.getEnumerationItems("planGrade"))
						<option value="$row.value" #if("$row.value"=="$result.grade") selected #end>$row.name</option>
					#end
					</select></span>
					</li>
					#end
					</ul>
					<div class="workflow_data">
						<div><textarea name="content" style="width:100%;height:235px;visibility:hidden;">$!result.content</textarea></div>
						<div class="workflow_data_bu" style="width:100%">
						<a class="upload_a" href="javascript:openAttachUpload('/plan/');" style="display:block;">上传附件</a>
						<span id="files_preview">				  		  
					  	#foreach ($str in $globals.strSplit($!result.attach,';'))
							 <li id ="$str">
						  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/plan/del_01.gif"></a>
						  	 $str<input type=hidden name="attachFile" value="$str"/>
						  	 </li>	
						#end
				    	</span>
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
					<span class="on" id="span_ass$!result.id$row.id">$row.name</span>
					#if("$row.name" == "关联客户")
						<a href="javascript:openClientPop();"><img src="/style/images/plan/Ic_005.gif" /></a>
					#else
						<a href="javascript:openPop('$!LoginBean.id','','$row.popSelect','$row.name','$row.id','$row.isEmployee','$row.linkAddress','ass$!result.id$row.id')"><img src="/style/images/plan/Ic_005.gif" /></a>
					#end
					
				</div>
				<ul id="ass$!result.id$row.id" style="padding:0;">
					#foreach($item in $assItem.get($!result.id))
					#if($globals.get($item,1) == $row.id)
					<li id="${result.id}_${row.id}_$globals.get($item,2)">
					<a href="javascript:delAssoicate('$result.id','${row.id}','$globals.get($item,2)','true')"><img src="/style/images/plan/del_01.gif" /></a>
					<span> #if($globals.get($item,3).indexOf("/") <=0) $globals.get($item,3) #else $globals.get($item,3) #end</span>
					<!-- <span>#if($globals.get($item,3).length() >15) $globals.get($item,3).substring(0,15) #else $globals.get($item,3) #end</span> -->
					#if("$row.isEmployee"=="1" && "$globals.get($item,2)"!="$!LoginBean.id")
						<a href="javascript:msgComm('$globals.get($item,2)','$globals.get($item,3)')"><img src="/style/images/plan/pp_01.gif" /></a>
					#end
					<input type="hidden" name="assoicate" value="${row.id}:$globals.get($item,2):$globals.get($item,3):0:0"/></li>			
					 #end
					 #end
				</ul>
				#end
				#end	
			</div>	
			</td><!--右边菜单结束-->
		</tr>
	</table>
</form>
</body>
</html>