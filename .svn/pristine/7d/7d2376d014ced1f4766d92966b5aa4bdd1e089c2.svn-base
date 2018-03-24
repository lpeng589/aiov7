<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.common.upd")$text.get("alertCenter.lb.user")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript">
function saveInfo(){
	if(form.nickName.value.trim().length==0){
		alert("$text.get("oa.bbs.nickname")不能为空！") ;
		return false;
	}
	if(!checkLen()){
		alert("个性签名最多输入150个字!");
		return false;
	}
	if(!checkImage()){
		alert( "请上传正确格式的图片文件!!"); 
		return false;
	}
		
	form.submit() ;
	window.parent.asyncbox.tips('修改个人资料成功 !','success');
}

function checkLen(){
	var maxLen=150;
	var myLen=0;
	var i=0;
	var str=form.signature.value;
	for(;(i<str.length)&&(myLen<=maxLen*2);i++){
		if(str.charCodeAt(i)>0&&str.charCodeAt(i)<128)
			myLen++;
		else
			myLen+=2;
	}
	if(myLen>maxLen*2)
		return false;
	else
		return true;	
}

function checkImage(){
	var theImage=form.imageFile.value.toLowerCase();
	if(theImage!=""){
	    var strLen=theImage.length; 
	    var strImg=theImage.substring(strLen-4,strLen); 
	    if(strImg!='.jpg' && strImg!= '.gif' && strImg!='.bmp' && strImg!=".jpeg" && strImg!=".png"){ 
			return false;
	    } 
	 }
	 return true;
}


function checkFile(){
	if(!checkImage()){
		alert( "请上传正确格式的图片文件!!"); 
		return false;
	}
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form name="form" action="/OABBSUserAction.do?operation=$globals.getOP("OP_UPDATE")&type=user" enctype="multipart/form-data" method="post" target="formFrame">
<div id="listRange_id" style="text-align:center;">
<table width="800" border="0" cellpadding="0" cellspacing="0" style="margin:5px 0 0 0;">
	<tbody> 
		<tr>
			<td class="oabbs_tr">$text.get("oa.bbs.nickname")：</td>
			<td class="oabbs_tl">
	   			<input type="text" name="nickName" value="$!userBean.nickName" class="text" style="width: 250px;" onKeyDown="if(event.keyCode==13) event.keyCode=9"/>
	   		</td>
		</tr>
		<tr style="line-height:42px;">
			<td class="oabbs_tr">$text.get("mydesktop.lb.MyGif")：</td>
			<td class="oabbs_tl">
	   			<input name="imageFile" type="file" value="" onChange="checkFile();">
	   			<font color="#66CCFF">(注：头像最佳大小为126*126px)</font>
	   		</td>
		</tr>
		<tr>
			<td class="oabbs_tr">$text.get("oa.bbs.personalityDescribe")：</td>
			<td class="oabbs_tl">
				  <textarea name="signature" style="width:400px;height:50px;">$!globals.replaceHTML($!userBean.signature)</textarea>
	   		</td>
		</tr>
	</tbody>
</table>
</div>
</form>
</body>
</html>