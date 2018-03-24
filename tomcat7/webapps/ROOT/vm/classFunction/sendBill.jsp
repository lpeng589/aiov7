<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.mail.sendMail")$text.get("oa.mail.email")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="mailContent"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

function openSelect1(urlstr,obj,field){
	var displayName = encodeURI("$text.get('oa.mail.emailUser')") ;
	urlstr = '/UserFunctionAction.do?selectName=emailUser&operation=$globals.getOP("OP_POPUP_SELECT")';
	var str  = window.showModalDialog(urlstr+"&MOID=17&MOOP=add&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=730px;dialogHeight=450px"); 

	if(typeof(str)=="undefined")return ;
	if(str.indexOf("@Sess:")>=0){
		document.getElementById(obj).value="";
		document.getElementById(field).value="";
		return ;
	}
	fs=str.split(";");  
	document.getElementById(obj).value=fs[0];
	document.getElementById(field).value=fs[1];		
}

//上传附件
function insertNextFile(obj) { 
	  var childnum = document.getElementById("files").getElementsByTagName("input").length;      
      var id = childnum -1;
      var fullName = obj.value;

      var fileHtml = '';
      fileHtml += '<div  id ="file_preview' + id + '" style ="height:18px; color:#ff0000;">';
      fileHtml += '<a href="javascript:;" onclick="removeFile(' + id + ');"><img src="/$globals.getStylePath()/images/del.gif"></a>&nbsp;&nbsp;';
      fileHtml += fullName.substr(fullName.lastIndexOf('\\')+1) +'  </div>';
      var fileElement = document.getElementById("files_preview");
      fileElement.innerHTML = fileElement.innerHTML + fileHtml;    
      obj.style.display = 'none'; 
      addUploadFile(childnum); 
}


function addUploadFile(index){
      try{  
          var uploadHTML = document.createElement( "<input type='file' class='text' id='file_"+ index +"' name='file["+ index +"]' onchange='insertNextFile(this)'/>");
          document.getElementById("files").appendChild(uploadHTML);
      }
      catch(e) { 
          var uploadObj = document.createElement("input");
          uploadObj.setAttribute("name", "file["+ index +"]");
          uploadObj.setAttribute("onchange", "insertNextFile(this)");
          uploadObj.setAttribute("type", "file");
          uploadObj.setAttribute("id", "file_"+ index);
          document.getElementById("files").appendChild(uploadObj);
      }
}
  
function removeFile(index){
      document.getElementById("files_preview").removeChild(document.getElementById("file_preview"+ index)); 
      document.getElementById("files").removeChild(document.getElementById("file_"+ index));    
  }
  function showStatus(obj)  
  {
    document.getElementById("status").style.visibility="visible";
  }
  function checkForm()
  {
	var mailContent = editor.html();
  	var to = document.form.to.value;
  	var mailtitle = document.form.mailTitle.value;

	if(!isNull(to,'$text.get("oa.mail.addressee")'))
	{
		return false;
	}
	
	else if(!isNull(mailtitle,'$text.get("oa.subjects")'))
	{
		
		return false;
	}
	else if(mailtitle.length>40){
		alert("$text.get('oa.mail.content.mailtitle')");
		return false ;
	}
	
	else if(!isNull(mailContent,'$text.get("oa.mail.contenet")')){
		return false ;
	}else{
	
	return true;
	}
  }
function openSelect3(urlstr,to){
	var str  = window.showModalDialog(urlstr+"&MOID=17&MOOP=add",winObj,"dialogWidth=730px;dialogHeight=450px");  
	if(typeof(str)=="undefined")return ;
	if(str.indexOf("@Sess:")>=0||str.length<=1){
		document.getElementById(to).value="";
		return ;
	}
	var varStr = str.split("|");  
	var varBbc = document.getElementById(to).value ;
	for(var i=0;i<varStr.length;i++){
		var varUser = varStr[i].split(";");
		if(varUser[0]!=""){
			var isExist = varBbc.indexOf(varUser[0]+",") ;
			if(isExist==-1){
				varBbc += varUser[0]+"," ;
			}
		}
	}
	document.getElementById(to).value =varBbc;
}

function isNull(variable,message)
{
	if(variable=="" || variable==null)
	{
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else
	{
		return true;
	}
			
}

function send(){
	if(checkForm()){
		document.form.mailContent = editor.html()
		document.form.submit();
	}
}
</script>

</head>
<body style="overflow:auto;">
<form name="form" method="post"  action="/EMailAction.do?operation=$globals.getOP("OP_ADD")" enctype="multipart/form-data">
	<input type="hidden" name="type" value="sendouttermail">
	<input type="hidden" name="emailUser" value="$!emailUser"/>
	<input type="hidden" name="tableName" value="$!tableName"/>
	<input type="hidden" name="isOAEmail" value="no"> 
	<input type="hidden" name="saveType" value="save">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.common.sendEmail")</div>
	<ul class="HeadingButton">
	<li>
	<button type="button" onClick="send();"   name="Submit" class="b4">	$text.get("oa.mail.immediaatelySend")</button></li>
		<li><button type="button" onClick="location.href='/UserFunctionQueryAction.do?operation=$globals.getOP("OP_QUERY")&tableName=$tableName&checkTab=Y&winCurIndex=$!winCurIndex&pageNo=$!pageNo'" class="b2">$text.get("common.lb.back")</button></li>
		
	</ul>
	</div>	
	</div>	
<div class="listRange">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
			 <tr id="strEmail">
				<td class="oabbs_tr">$text.get("oa.mail.emailUser")：</td>
				<td><div>
						<div style="float:left">
						<input type="hidden" name="emailType" id="emailType" value="$!emailUser">
						<input name="from" id="from" type="text" class="text" size="60"  value="$!defaultAccount" onDblClick="openSelect1('','emailUser','from')" >
						</div>
						<div style="float:left">															
							<img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('','emailUser','from')">	
						</div>			
				  </div>						
			   </td>
			</tr>	
			
	       	<tr>
				<td nowrap class="oabbs_tr">$text.get("oa.mail.addressee")：</td>
				<td>
					<div>
						<div style="float:left">
						<input name="to" id="to" type="text" class="text"  size="60" value="$!strEmail" >
						</div>
					</div>					
				</td>
		  </tr>
	       <tr>
			<td class="oabbs_tr">$text.get("oa.subjects")：</td>
				<td>
					<input name="mailTitle" type="text" class="text" size="60">
				</td>
			</tr>			
			<tr>
				<td class="oabbs_tr" valign="top">$text.get("oa.mail.contenet")：</td>
			 	<td>
				<textarea name="mailContent" style="width:100%;height:300px;visibility:hidden;"></textarea>
			   	</td>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("oa.common.accessories")：</td>
				<td>
					<span id="files">
						<input  type="file" class="text" id="file_0" name="file[0]"  contenteditable="false" onChange="insertNextFile(this)" class="text" />
					</span>
					<div id ="status" style="visibility:hidden;color:Red"></div>
					<div id="files_preview"></div>
				</td>
			</tr>			
		  	</tbody>
		</table>
		<br>
</div>
</form>
</body>
</html>
