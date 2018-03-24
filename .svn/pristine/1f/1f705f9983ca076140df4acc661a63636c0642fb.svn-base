
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>

<title>Insert title here</title>
<style type="text/css" >
.listRange_1_photoAndDoc_1{margin-left:0px;width:98%;}
</style>
<script type="text/javascript" >
function boxclose(){
	parent.jQuery.close('updatebox');
}

function successbox(){
	if(parent.jQuery == undefined){
		alert('修改成功');
		location.href='/CRMQASearchAction.do';
	}else{
		parent.asyncbox.alert('修改成功','提示',function(){parent.form.submit();parent.jQuery.close('updatebox');});
	}
}

function addsuccessbox(){
	if(parent.jQuery == undefined){
		alert('添加成功');
		location.href='/CRMQASearchAction.do';
	}else{
		
		
		parent.asyncbox.alert('添加成功','提示',function(){parent.form.submit();parent.jQuery.close('updatebox');});
		
		
	}
}

function formsubmited(){
	var val=form.ref_id.value.trim();
	if(val==""){
		asyncbox.tips('问题不能为空','error');
		form.ref_id.focus();
		return false;
	}
	form.submit();
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/CRMQAAction.do" name="form" method="post" target="formFrame"  >
#if("$!qa.id"!="")
<input name="operation" type="hidden" value="2" /> 
#else
<input name="operation" type="hidden" value="1" /> 
#end


<input type="hidden" name="attachFiles" value="$!qa.attachment" />
<input type="hidden" name="delFiles" value="" />
<input name="qaid" type="hidden" value="$!qa.id" /> 

<div style="float:right;  margin:5px ;" >
<a onclick="formsubmited()"  class="asyncbox_btn"><span>&nbsp;保&nbsp;&nbsp;存&nbsp;</span></a>
<a onclick="boxclose()"  class="asyncbox_btn"><span>&nbsp;取&nbsp;&nbsp;消&nbsp;</span></a>
</div>

<div class="listRange_1_photoAndDoc_1">
<span>问题：</span>

</div>

<div class="listRange_1_photoAndDoc_1" style=" height:150px;overFlow-y:scroll;">
<textarea name="ref_id" rows="8" >$!qa.ref_id</textarea>

</div>
<div class="listRange_1_photoAndDoc_1">
<span>解答：</span>
</div>
<div class="listRange_1_photoAndDoc_1" style=" height:150px;overFlow-y:scroll;">
<textarea name="answer" rows="8" >$!qa.answer</textarea>
</div>
<div class="listRange_1_photoAndDoc_1" style=" height:50px;">

<a href="javascript:void(0)" onclick="openAttachUpload('/crm/QA/');window.save=true;">&nbsp;<span></span>
								<img src="/style1/images/oaimages/uploading.gif" /><font style="margin-left: 2px;">$text.get("upload.lb.affixupload")</font></a>
								<div id="status" style="visibility:hidden;color:Red"></div>
								<div id="files_preview">
								      #if($!qa.attachment.indexOf(";") > 0)
								  #foreach ($str in $globals.strSplit($!qa.attachment,';'))
								  	 <div  id ="$str" style ="height:18px; color:#ff0000;">
								  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
								  	 $str<input type=hidden name="attachFile" value="$str"/></div>					 
								  #end	
							 	 #end
								
								</div>	
</div>

</form>
</body>
</html>