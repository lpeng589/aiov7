<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<style type="text/css">
.div_content li{height:40px;}
.div_content{margin: 50px 0 0 30px;}
</style>
<script language="javascript">
putValidateItem("groupName","$text.get("oa.mail.folderName")","any","",false,0,50);

function checkForm(){
	if(!validate(form)){
		return false
	}
	form.submit();
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form id="form" name="form" method="post" action="/EMailAction.do" target="formFrame">
<input type="hidden" name="type" value="groupmanager"/>
<input type="hidden" name="op" id="op" value="$op"/>
<input type="hidden" name="id" id="id" value="$!result.id" />
<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")"/>
<div class="div_content">
	<ul>
		<li>
			<span style="font-weight:normal;font-style: normal;color: black;">$text.get("oa.mail.lb.account")：</span>
			<select name="account" style="width:133px;">
				<option value="">$text.get("oa.common.innerMail")</option>
				#foreach($row in $outteremail)
				<option value="$row.id" #if("$row.id"=="$result.account") selected #end>$row.account</option>
				#end
			</select>
		</li>
		<li>
			<span style="font-weight:normal;font-style: normal;color: black;">$text.get("oa.mail.folderName")：</span>
			<input name="groupName" id="groupName" type="text" value="$!result.groupName">
		</li>
	</ul>
</div>
</body>
</html>

