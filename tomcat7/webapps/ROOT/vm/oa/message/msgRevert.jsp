<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("message.lb.pageTitle")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">

putValidateItem("content","$text.get("message.lb.content")","any","",false,0,100);
putValidateItem("receive","$text.get("message.lb.receiver")","any","",true,1,30 );

function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	return true;
}	
function openSelect(urlstr,obj,field){
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add","","dialogWidth=730px;dialogHeight=450px"); 
	
	fs=str.split(";");  
	form.receive.value=fs[0];
	form.receiveName.value=fs[1];
}
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
function m(value){
	return document.getElementById(value) ;
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
		   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadpic value='"+str+"'><div><a href=\"/ReadFile?fileName="+str+"&realName="+str+"&tempFile=true&type=PIC\" target=\"_blank\"><img src=\"/ReadFile.jpg?fileName="+str+"&realName="+str+"&tempFile=true&type=PIC\" width=\"150\" height=\"150\" border=\"0\"></a></div><div>"+str+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"true\",\"workFlowDesign\",\"PIC\");'>$text.get("common.lb.del")</a></div></li>";
		   ul.innerHTML = ul.innerHTML+imgstr;
	   }
	}else if(curUploadType == 'AFFIX'){
		mstrs = retstr.split(";");
	    for(i=0;i<mstrs.length;i++){
		   str = mstrs[i];
		   if(str == "") continue;
		   var ul=m('affixuploadul');
		   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadaffix value='"+str+"'>"+str+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"true\",\"workFlowDesign\",\"AFFIX\");'>$text.get("common.lb.del")</a>&nbsp;&nbsp;&nbsp;<a href=\"/ReadFile?fileName="+str+"&realName="+str+"&tempFile=true&type=AFFIX\" target=_blank>$text.get("common.lb.download")</a></li>";
		   ul.innerHTML = ul.innerHTML+	imgstr;
	    }
	}    
    var attachUpload = document.getElementById("attachUpload");
	attachUpload.style.display="none";
}

</script>
</head>

<body onLoad="showStatus();">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/MessageAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_REVERT")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="keyId" value="$result.id">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("message.lb.pageTitle")</div>
	<ul class="HeadingButton">
		<li><button type="submit" class="b2">$text.get("message.lb.send")</button></li>
		<li><button type="button"  onClick="location.href='/MessageQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
	</ul>
</div>
<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_div_msg">
		<div class="listRange_div_msg_div"></div>
		<div>
			<ul class="listRange_msg">
			 <li><span>$text.get("message.lb.receiver")：</span>
			  <input type="hidden" name="receive" value="$result.getReceive()">
			  <input name="receiveName" type="text"  value="$result.getReceiveName()" value="" maxlength="50" onKeyDown="if(event.keyCode!=13&&event.keyCode!=9)event.returnValue=false;" onDblClick="openSelect('/UserFunctionAction.do?tableName=OAMessage&selectName=MsgReceive&operation=$globals.getOP("OP_POPUP_SELECT")')"  class="text"><img src="/$globals.getStylePath()/images/St.gif" onClick="openSelect('/UserFunctionAction.do?tableName=OAMessage&selectName=MsgReceive&operation=$globals.getOP("OP_POPUP_SELECT")')"></li>
			
			  <li><span>$text.get("message.lb.content")：</span>
			  <textarea rows="5" cols="100" name="content"></textarea>
			  </li>
			  <li><span>$text.get("upload.lb.affix")：</span> 
			<button name="affixbuttonthe222" onClick="upload('AFFIX');" class="b4">$text.get("upload.lb.affixupload")</button>
<ul id="affixuploadul">
</ul>
			</li>
			</ul>
	</div>	
	</div>
</div>
</form> 
</body>
</html>
