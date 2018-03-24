




<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("user.lb.userAdmin")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">

//附件上传的方法




function deleteupload(fileName,tempFile,tableName,type){
	//如果是临时文件，则需删除远程文件，正式文件，不能删除
	if(!confirm('$text.get("common.msg.confirmDel")')){
		return;
	}
	if("true" == tempFile){
		var str="/UploadDelAction.do?NOTTOKEN=NOTTOKEN&operation=$globals.getOP("OP_DELETE")&fileName="+encodeURIComponent(fileName)+"&tempFile="+tempFile+"&tableName="+tableName+"&type="+type;
 	    AjaxRequest(str);
    	var value = response;    	
    	if(value=="no response text" && value.length==0){
    		return;
    	} 
	}
	var li=m('uploadfile_'+fileName);
	if("false" == tempFile && type == "AFFIX"){
		li.outerHTML = "<input type=hidden name=uploadDelAffix value='"+fileName+"'>";
	}else if("false" == tempFile && type == "PIC"){
		li.outerHTML = "<input type=hidden name=uploadDelPic value='"+fileName+"'>";
	}else{	
		li.outerHTML = "";
	}
}
function upload(type){
	openAttachUpload(type);
	return;	
}
var curUploadType = "";
function openAttachUpload(type){
	curUploadType = type;
	var filter = "";
	if(type == "PIC"){
		filter = "Image";
	}
	
	var attachUpload = document.getElementById("attachUpload");
	if(attachUpload == null){
		uploadDiv =document.createElement('<div id="attachUpload" style="position:absolute; top:10px;left:30px; width=600px;height:400px; display:block"></div>');
		document.form.appendChild(uploadDiv);
		attachUpload = document.getElementById('attachUpload');
	}
	attachUpload.style.left=  ((document.body.clientWidth - 500) /2) +"px";
	attachUpload.style.top=  ((document.body.clientHeight - 250) /2) +"px";
	attachUpload.style.display="block";
	attachUpload.innerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="fileUpload" width="500" height="250" codebase="http:/'+'/fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">'+
			' <param name="movie" value="/flash/FileUpload.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#869ca7" /><param name="flashvars" value="uploadUrl=/UploadServlet;jsessionid=$session.id?path=/temp/@amp;fileType='+type+'&filter='+filter+'" />'+
			' <param name="allowScriptAccess" value="sameDomain" /><embed src="/flash/FileUpload.swf" quality="high" bgcolor="#869ca7"	width="500" height="250" name="column" align="middle" play="true" loop="false"'+
			'	flashvars="uploadUrl=/UploadServlet;jsessionid=$session.id?path=/temp/@amp;fileType='+type+'&filter='+filter+'"	quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http/'+'/www.adobe.com/go/getflashplayer"></embed></object>';
} 
function onCompleteUpload(retstr){  
	retstr = decodeURIComponent(retstr);   
	if(curUploadType == 'PIC'){
	   mstrs = retstr.split(";");
	   for(i=0;i<mstrs.length;i++){
		   str = mstrs[i];
		   if(str == "") continue;
		   var ul=m('picuploadul');
		   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadpic value='"+str+"'><div><a href=\"/ReadFile?fileName="+str+"&realName="+str+"&tempFile=true&type=PIC\" target=\"_blank\"><img src=\"/ReadFile.jpg?fileName="+str+"&realName="+str+"&tempFile=true&type=PIC\" width=\"150\" height=\"150\" border=\"0\"></a></div><div>"+str+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"true\",\"$tableName\",\"PIC\");'>$text.get("common.lb.del")</a></div></li>";
		   ul.innerHTML = ul.innerHTML+imgstr;
	   }
	}else if(curUploadType == 'AFFIX'){
		mstrs = retstr.split(";");
	    for(i=0;i<mstrs.length;i++){
		   str = mstrs[i];
		   if(str == "") continue;
		   var ul=m('affixuploadul');
		   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadaffix value='"+str+"'>"+str+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"true\",\"$tableName\",\"AFFIX\");'>$text.get("common.lb.del")</a>&nbsp;&nbsp;&nbsp;<a href=\"/ReadFile?fileName="+str+"&realName="+str+"&tempFile=true&type=AFFIX\" target=_blank>$text.get("common.lb.download")</a></li>";
		   ul.innerHTML = ul.innerHTML+	imgstr;
	    }
	}    
    var attachUpload = document.getElementById("attachUpload");
	attachUpload.style.display="none";
}
</script>
</head>

<body >
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/OAWorkFlowTempAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oaWorkflow.lb.flowDetail")</div>
	<ul class="HeadingButton">
				<li><button type="button" onClick="location.href='/OAMyWorkFlow.do?approveStatus=transing&flowBelong=self&operation=4'" class="b2">返回</button></li>
	</ul>
</div>
<div id="listRange_id">
<script type="text/javascript"> 
var oDiv=document.getElementById("listRange_id");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tc">#if("$!type" == "1")
		$text.get("oa.workflow.lb.commonTemp")
		#elseif("$!type" == "2")
		$text.get("oa.workflow.lb.wordTemp")
		#elseif("$!type" == "3")
		$text.get("oa.workflow.lb.xlsTemp")
		#end</td>
			</tr>
		</thead>
				<tbody>
		<tr><td width="300" align="right">
		<span>$text.get("oa.workflow.lb.tempName")：</span></td><td>&nbsp;#if($!globals.get($result,1).get($globals.getLocale()))$!globals.get($result,1).get($globals.getLocale())#else$!globals.get($result,1)#end</td></tr>
		<tr>
			<td width="300" align="right"><span>$text.get("oaWorkflow.lb.flowDetail")：</td>
			<td>&nbsp;
			$!globals.get($result,11)
			</td>
			</tr>
			
			<tr>
			<td width="300" align="right"><span>$text.get("oaWorkflow.lb.detailAffix")：</span></td>
			<td>&nbsp;
<ul id="affixuploadul">
#foreach($uprow in $affix)	
#if($uprow.indexOf(":") > -1)			
<li style='background:url();' id="uploadfile_$!globals.get($uprow.split(":"),0)">
<input type=hidden name=uploadaffix value='$uprow'>$!globals.get($uprow.split(":"),1)			
&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=AFFIX&tableName=workFlowDesign" target="_blank">$text.get("common.lb.download")</a>
</li>
#else
<li style='background:url();' id="uploadfile_$uprow">
<input type=hidden name=uploadaffix value='$uprow'>$uprow	
&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=workFlowDesign" target="_blank">$text.get("common.lb.download")</a>
</li>
#end
#end
</ul>
			</td>
		</tr>
		<tr>
		<td width="300" align="right">
		<span>$text.get("common.lb.FlowChart")</span></td>
		<td><object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="fileUpload" width="1000" height="500" 
			codebase="/swflash.cab">
			<param name="movie" value="/flash/WorkFlowView.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#869ca7" />
			<param name="flashvars" value="serverURL=http://$!ip/&fileName=$!fileName&state=$!flowDepict" />
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="/flash/WorkFlowView.swf" quality="high" bgcolor="#869ca7"
				width="1000" height="500" name="column" align="middle" 
				play="true"
				loop="false"
				flashvars="serverURL=http://$!ip/&fileName=$!fileName&state=$!flowDepict"
				quality="high" 
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object></td>
		</tr>
		  </tbody>
		</table>
	</div>
</script>
</form>
</body>
</html>
