<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get('crm.carefor.lb.compemail')</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript">
function sub(){
	var check= false;
	var id = document.getElementsByName('id');
	for(var i=0;i<id.length;i++){
		if(id[i].checked){
			check=true;
		}
	}
	if(!check){
		alert('$text.get("roleAdmin.msg.checkNoSize")$text.get("oa.mail.emailUser")');return;
	}
	form.submit();
	setTimeout('closeTheWin()',500);
}

function closeTheWin(){
	window.close();
}
</script>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" name="form" action="/CareforDelAction.do" target="formFrame">
 <input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
  <input type="hidden" name="type" value="setMail"/>

<div class="Heading">
			<div class="HeadingIcon">
				<img src="/$globals.getStylePath()/images/Left/Icon_1.gif" />
			</div>
			<div class="HeadingTitle">
				$text.get('crm.carefor.lb.compemail')
			</div>
				<ul class="HeadingButton">
					
					<li>
						<button type="button" onClick="sub()" class="b2">
							$text.get("common.lb.ok")
						</button>
					</li>
					
					<li>
						<button name="back" type="button" onClick="closeTheWin()" class="b2">
							$text.get("common.lb.close")
						</button>
					</li>
				</ul>
</div>


	<div class="listRange_Pop-up_scroll">
			
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list" name="table" id="tblSort">
		<thead>
			<tr>
				<td width="20">&nbsp;</td>
				<td width="100">$text.get("oa.mail.emailUser")</td>
			</tr>			
		</thead>
		<tbody>
			#foreach($email in $list)
			<tr>
				<td>
				<input type="radio" name="id" value="$globals.get($email,0)" #if($!globals.get($email,2) == 1)	checked="checked"	#end/>
				</td>
				<td>$globals.get($email,1) </td>
			</tr>
			#end
		</tbody>

		</table>
	</div>

</form>
</body>
</html>
