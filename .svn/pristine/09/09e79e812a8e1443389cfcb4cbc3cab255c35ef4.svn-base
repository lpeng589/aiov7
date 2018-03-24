<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="/$globals.getStylePath()/css/ListRange.css">
<script language="javascript" src="/js/function.js"></script> 
<title>left</title>
<script type="text/javascript">
#if("$!ClientServiceKeyWord" != "")
	$("keyWord").value = "$!ClientServiceKeyWord";
	queryClient();
#end
function $(obj){
	return document.getElementById(obj) ;
}
function queryClient(){
	var keyWord = $("keyWord").value ;
	if(keyWord.length==0){
		alert("$text.get('common.msg.KeywordNotNull')") ;
		return ;
	}
	AjaxRequest("/UtilServlet?operation=existClient&keyWord="+keyWord)
	var strUrl = "" ;
	var exist = response ;
	if("more"==exist){ 
		var urlStr = "/ClientServiceAction.do?type=query&keyWord="+keyWord ;
		var returnValue = window.showModalDialog(urlStr,"","dialogWidth=730px;dialogHeight=450px"); 
		if(typeof(returnValue)=="undefined")return ;
		$("clientId").value = returnValue ;
		strUrl = "/UserFunctionAction.do?operation=7&tableName=CRMClientInfo&fromCRM=service&keyId="+returnValue ;
		$("assignTask").style.display = "block" ;
		var clientDetail = $("clientDetail") ;
		clientDetail.style.height = "230px"
		clientDetail.src = strUrl ;
		return;
	}else if("noData"==exist){
		if(confirm("$text.get('common.msg.NoMatchingClient.ifAddClient')?")){
			$("assignTask").style.display = "none" ;
			strUrl = "/UserFunctionAction.do?tableName=CRMClientInfo&operation=6&fromCRM=service" ;
		}else{
			return ;
		}
	}else {
		$("assignTask").style.display = "block" ;
		$("clientId").value = exist ;
		strUrl = "/UserFunctionAction.do?operation=7&tableName=CRMClientInfo&fromCRM=service&keyId="+exist ;
		var clientDetail = $("clientDetail") ;
		clientDetail.style.height = "230px"
		clientDetail.src = strUrl ;
		return;
	}
	var clientDetail = $("clientDetail") ;
	clientDetail.style.height = "370px"
	clientDetail.src = strUrl ;
}

function openSelect(selectName,displayName){
	displayName=encodeURI(displayName) ;
	var clientId = $("clientId").value ;
	var str  = window.showModalDialog("/UserFunctionAction.do?selectName="+selectName
				+"&operation=22&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName+"&ClientId="+clientId,"","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)!="undefined"){
		var mutli=str.split(";"); 
		$("taskPersonName").value = mutli[1] ;
		$("taskPerson").value = mutli[0] ;
	}
}

function beforeSubmit(){
	var strContent = $("content").value ;
	if(strContent.length==0){
		alert("$text.get('common.msg.allocateContentNotNull')！");
		return false ;
	}
	return true ;
}

function deleteupload(fileName,tempFile,tableName,type){
	var str  = window.showModalDialog("/UploadDelAction.do?NOTTOKEN=NOTTOKEN&operation=$globals.getOP("OP_DELETE_PREPARE")&fileName="+fileName+"&tempFile="+tempFile+"&tableName="+tableName+"&type="+type,winObj,"dialogWidth=415px;dialogHeight=250px");
	if(typeof(str)=='undefined' || str.length == 0){
		return;
	}
	var li=$('uploadfile_'+str);
	if("false" == tempFile && type == "AFFIX"){
		li.outerHTML = "<input type=hidden name=uploadDelAffix value='"+str+"'>";
	}else{	
		li.outerHTML = "";
	}
}
function upload(type){
	var str  = window.showModalDialog("/UploadAction.do?NOTTOKEN=NOTTOKEN&operation=$globals.getOP("OP_ADD_PREPARE")&type="+type,winObj,"dialogWidth=380px;dialogHeight=190px");
	if(typeof(str)=='undefined' || str.length == 0){
		return;
	}
	var strs=str.split(":");
	if(type == 'AFFIX'){
	   var ul=$('affixuploadul');
	   var imgstr = "<li id='uploadfile_"+strs[0]+"'><input type=hidden name=crmAffix value='"+str+"'>"+strs[1]+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+strs[0]+"\",\"true\",\"CRMTaskAssign\",\"AFFIX\");'>$text.get("common.lb.del")</a>&nbsp;&nbsp;&nbsp;<a href=\"/ReadFile?fileName="+encodeURIComponent(strs[0])+"&tempFile=true&type=AFFIX\" target=_blank>$text.get("common.lb.download")</a></li>";
	   ul.innerHTML = ul.innerHTML+	imgstr;
	}
}
</script>
<style>
.dais_l{
	float:left;
	width:100%;
	margin:0;
	bottom:0px;
	padding:0px;
    overflow: auto !important;     
    _top:0px;         
    _OVERFLOW-Y: auto;  
    _OVERFLOW-X: auto;         
    _height:100%;
	overflow-y:auto;
    position:fixed;
    bottom:0px;            
    overflow: auto !important; 
    _position: relative;
	border-right:1px solid #ECECEC;
}
</style>
</head>

<body>
<div class="dais_l" >
	<div class="listRange_1" style="border:0px; border-bottom:1px dotted #CCCCCC">
		<li style="width:420px; padding-left:10px; height:30px; padding-top:5px;">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font style="font-weight: bold;">$text.get("oa.common.keyWord")</font>：




			<input name="keyWord" value="" onKeyDown="if(event.keyCode==13){queryClient();}">&nbsp;&nbsp;&nbsp;&nbsp;
			<button name="Submits" type="button" class="b2" onClick="return queryClient();">$text.get("common.lb.query")</button>
		</li>
	</div>
<form action="/ClientServiceAction.do?type=task" method="post">
	<input type="hidden" name="clientId" value=""/>
	<div id="assignTask" style="display: none;">
	<div class="listRange_1_photoAndDoc_1"><span style="width: 40px;float: left;" >$text.get("common.lb.assort")：</span>
	#foreach($enum in $globals.getEnumerationItems("CRMTaskType","$globals.getLocale()"))
	<input name="taskType" type="radio" value="$enum.value" #if($velocityCount==1)checked #end>$enum.name
	#end
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$text.get("common.lb.taskAllocate")：




	<input name="taskPerson" type="hidden" value=""/>
	<input name="taskPersonName" type="text" class="text" onDblClick="openSelect('SelectServiceEmployeeId','$text.get("common.lb.taskAllot")')"/>
	<img id="job_cy" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect('SelectServiceEmployeeId','$text.get("common.lb.taskAllot")')" class="search">
	&nbsp;&nbsp;&nbsp;&nbsp;<button type="submit" style="220px;" onClick="return beforeSubmit();">$text.get("common.lb.save")</button>
	</div>
	<div class="listRange_1_photoAndDoc_1"><span style="width: 45px;float: left;" >$text.get("common.lb.content")：<font color="red">*</font></span>
		<textarea name="content" rows="3" style="width: 488px;"></textarea>  
	</div>
	<div class="listRange_1_photoAndDoc_1"><span>$text.get("upload.lb.affix")：</span>
		<button name="affixbuttonthe222" onClick="upload('AFFIX');" class="b4">$text.get("upload.lb.affixupload")</button>
		<ul id="affixuploadul"></ul>
	</div>
    </div>
</form>
	<div class="scroll_function_small_a" id="conter">
		<iframe id="clientDetail" src="/UserFunctionAction.do?tableName=CRMClientInfo&operation=6&fromCRM=service" 
		frameborder=false width="100%" scrolling="no" height="390px;"></iframe>																																																													
	</div>
</div>
</body>
</html>

<script type="text/javascript">
function setHeight(height){
	$("clientDetail").style.height=height-45;
}	

	
#if("$!ClientServiceKeyWord" != "")
	$("keyWord").value = "$!ClientServiceKeyWord";
	queryClient();
#end
</script>
