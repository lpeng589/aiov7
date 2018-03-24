<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>#if("$op"=="update") $text.get("oa.common.upd")$text.get("oa.ordain.directory") #else $text.get("oa.mail.createDirectory") #end</title>
<link type="text/css" rel="stylesheet" href="/style/css/mail.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/classFunction.css" type="text/css" />
<script src="/js/jquery.js" type="text/javascript"></script>
<script>
function submitform(){
	if(checkForm()){
		document.form1.submit();
	}
}

function checkForm(){
	if(document.form1.emailType.value == ""){
		if(document.form1.emailType.options.length==0){
			alert("$text.get("oa.mail.msg.setemail")");
		}else{
			alert("$text.get("oa.mail.lb.account")$text.get("common.validate.error.null")");
		}
		return false;
	}
   	var filetypes=document.getElementsByName("fileType");
   	var isnull=false;
   	for(var i in filetypes){
       i=filetypes[i];
       if(i.checked){
          isnull=true;
       }
   	}
  	if(!isnull){
       alert('$text.get("oa.mail.file.type")');
       return false;
  	}
	if(document.getElementById("fileTypePath").checked){
		if(document.form1.path.value == ""){
			alert("$text.get("email.lb.importFile")$text.get("common.validate.error.null")");
			return false;
		}
	}else{
		if(document.form1.file.value == ""){
			alert("$text.get("email.lb.importFile")$text.get("common.validate.error.null")");
			return false;
		}
	}
	if(document.getElementById("fileTypeZip").checked){
		if(document.form1.file.value.substr(document.form1.file.value.lastIndexOf(".")).toLowerCase() != ".zip"){
			alert("$text.get("mail.msg.uploadzip")");
			return false;
		}
	}else if(document.getElementById("fileTypeEml") && document.getElementById("fileTypeEml").checked){
		if(document.form1.file.value.substr(document.form1.file.value.lastIndexOf(".")).toLowerCase() != ".eml"){
			alert("$text.get("mail.msg.uploademl")");
			return false;
		}
	}
	return true;
}

function accountChange(){
	emailType = document.form1.emailType.value;
	groupSel = document.form1.groupId;
	colls = groupSel.options; //获取引用
	var length = colls.length;
    for(var i=length-1;i>=5;i--){
        colls.remove(i);
    }	
	if(emailType == ""){
		#foreach($group in $emailgroup)
		#if("$!globals.get($group,2)" == "")
		colls.add(new Option('$!globals.get($group,1)','$!globals.get($group,0)'));	
		#end
		#end	
	}
	#foreach($row in $outteremail)  
	if(emailType == "$row.id"){
		#foreach($group in $emailgroup)
		#if("$!globals.get($group,2)" == "$row.id")
		colls.add(new Option('$!globals.get($group,1)','$!globals.get($group,0)'));	
		#end
		#end	
	}
	#end
}
function selFileType(type){
	document.getElementById("fileType"+type).checked=true;
	if(type=="Path"){
		document.getElementById("fileDiv").innerHTML='<input name="path" id="path" type="text" value="" style="width:193px;" class="text">';
	}else{
		document.getElementById("fileDiv").innerHTML='<input name="file" id="file" type="file" value="" style="width:193px;" class="text">';
	}
}

</script>
<style type="text/css">
.listrange_woke li{width:400px;}
.listrange_woke li span{width:90px;}
</style>
</head>
<body onload="accountChange()" scroll="no">
<iframe name="formFrame" style="display:none"></iframe>
<form id="form1" name="form1" method="post" action="/EMailImportAction.do" onSubmit="return checkForm()" target="formFrame" enctype="multipart/form-data">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD_PREPARE")"/>
<input type="hidden" name="importMail" value="exec"/>
<input type="hidden" name="importType" value="$importType"/>
<div class="MailSearch" id="list_mainframe" style="height:36px;border-bottom:1px solid #cccccc;">
	<li style="float:left;margin-left:10px;">
		<div style="color:#3C78AA;;font-weight:bold;font-size:15px;">
			&nbsp;&nbsp;
			#if("$importType"=="backup")
				$text.get("email.lb.import")
			#else
				$text.get("email.lb.stadImport")
			#end
		</div>
	</li>
</div>			

<div align="center">
	<div class="scroll_function_small_a">
				<div class="listRange_div_jag_hint" align="center" style="height:280px;">
					<div class="listRange_div_jag_div" style=" height:22px; line-height:22px;">
					#if("$importType"=="backup")
						$text.get("email.lb.import")
					#else
						$text.get("email.lb.stadImport")
					#end
					</div>	
					<p style="margin-top:5px;color:#ff0000;">
					#if("$importType"=="backup")
						$text.get("mail.msg.importlocal")
					#else
						$text.get("email.msg.importMsg")
					#end
					</p>			
					<ul class="listRange_woke" style="height:165px;margin:5px 0;">
							<li>
								<span>$text.get("oa.mail.lb.account")：</span>
								<select name="emailType" onchange="accountChange()" style="width:193px;">
					#if("$importType"=="backup")
					<option value="inner">$text.get("oa.common.innerMail")</option>
					#end
					#foreach($row in $outteremail)
					<option value="$row.id" #if("$row.id"=="$emailType") selected #end>$row.account&lt;$row.mailaddress&gt;</option>
					#end
				</select>
				</li>
				<li>
					<span>$text.get("oa.mail.mailDirectory")：</span>
			<select name="groupId" style="width:193px;">
				<option value="1">$text.get("oa.mail.receive.box")</option>
				<option value="2">$text.get("oa.mail.draft")$text.get("oa.mail.box")</option>
				<option value="3">$text.get("oa.mail.outBox")$text.get("oa.mail.box")</option>
				<option value="4">$text.get("oa.mail.dust")</option>
				<option value="5">$text.get("oa.mail.deleted")</option>							
			</select>
				</li>
				<li>
					<span>$text.get("email.lb.importFile")：</span>
					<div style="float:left;display:inline-block;text-align:left;width:310px;">
					<div  onclick="selFileType('Zip')"><input name="fileType" id="fileTypeZip" type="radio" value="zip"  />$text.get("email.msg.zipfile")</div>
					#if("$importType"!="backup")
					<div  onclick="selFileType('Eml')"><input name="fileType" id="fileTypeEml" type="radio" value="eml" checked />$text.get("email.msg.singleeml")</div>
					#end
					<div  onclick="selFileType('Path')"><input name="fileType" id="fileTypePath" type="radio" value="path" />
					#if("$importType"=="backup")
					$text.get("mail.msg.serverFile")
					#else
					$text.get("email.msg.useDir")
					#end
					</div>
					<div id="fileDiv"><input name="file" id="file" type="file" value="" style="width:193px;height:23px;border:1px #a1a1a1 solid;padding:0;font-family:微软雅黑;" class="text"></div>
					</div>
				</li>
			</ul>
			<div style="clear:both;">	
				<span class="hBtns" onclick="submitform();">$text.get("common.lb.save")</span>
				<span class="hBtns" onClick=" window.location.href='/EMailQueryAction.do?operation=4&emailType=$emailType&type=main&groupId=$groupId'">$text.get("common.lb.back")</span>
			</div>
				</div>																		
	</div>			
 
</div>
</form>
</body>
</html>
