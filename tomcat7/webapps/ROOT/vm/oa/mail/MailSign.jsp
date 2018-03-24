<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link rel="stylesheet" href="/style/css/mail.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script src="/js/jquery.js" type="text/javascript"></script> 
<style type="text/css">
button{border: none;}
</style>
<script language="javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="signature"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});
function beforSubmit(form){   
	if(!validate(form))return false;
	disableForm(form);
	//window.parent.frames['leftFrame'].location.reload() ;
	return true;
}	

if(typeof(top.junblockUI)!="undefined"){
	top.junblockUI();
}

function closeDiv(){
	parent.jQuery.close('dealSign');
}

</script>
</head>
<body scroll="no">
<iframe name="formFrame" style="display:none"></iframe>
<form id="form" name="form" method="post" action="/EMailAccountAction.do" onSubmit="return beforSubmit(this)"  target="formFrame">
<input type="hidden" name="type" value="mailSet"/>
<input type="hidden" name="op" value="sign" id="op"/>
<input type="hidden" name="id" value="$!result.id" />
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE_PREPARE")"/>
<input type="hidden" name="mainAccount" value="$!mainAccount">
<div align="center">
	<div class="scroll_function_small_a" >
		<div>
			<div style=" height:22px; line-height:22px;padding-bottom: 10px;">$text.get("oa.acount.signature")($!result.mailaddress)</div>					
			<textarea name="signature" style="width:97%;height:300px;visibility:hidden;">$!result.signature</textarea>
			<div style="text-align:center;">
				<span style="display:inline-block;overflow:hidden;padding-top:5px;">						
					<button type="submit" name="Submit" class="toolbu_01">$text.get("common.lb.save")</button>
					<button type="button" onClick="closeDiv()" class="toolbu_01">关闭</button>
				</span>	
			</div>
		</div>																		
	</div>			
 
</div>
</form>

</body>
</html>
