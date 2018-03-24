<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.careforaction.title.pathexec")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css" />
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="$globals.js("/js/date.vjs","",$text)"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript">
function openInputDate(obj)
{
	c.showMoreDay = false;
	c.show(obj);
}
function start(){
	if(form.baseDate.value==""){
		alert('$text.get("crm.carefor.lb.afterdate")$text.get("common.validate.error.null")');return false;
	}
	form.submit();
	returnValue = true;
}

var obj;
var obj2;
function openSelect1(urlstr,displayName,obj,obj2){
	window.obj = obj;
	window.obj2 = obj2;
	displayName=encodeURI(displayName) ;
	urlstr += "&popupWin=EmpPopup&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName; 
	asyncbox.open({id:'EmpPopup',title:'执行人',url:urlstr,width:730,height:450})
}

function exeEmpPopup(str){
	if(typeof(str)=="undefined"){return;}
	if(str.length>0){
		if(str==";;"){
			document.getElementById(obj).value="";
			document.getElementById(obj2).value="";
		}else{
			var s1 = '';
			var s2 = '';
			
			var strs = str.split('|');
			if(str.indexOf('|')==-1){
				var t = str.split(';');
					s1 = t[0]+";";
					s2 = t[1]+";";
			}else{
				for(var i = 0;i<strs.length-1;i++){
					var t = strs[i].split(';');
					s1 = s1 + t[0]+";";
					s2 = s2 + t[1]+";";
				}
			}
			document.getElementById(obj).value=s1;
			document.getElementById(obj2).value=s2;
		}
	}else{
		document.getElementById(obj).value="";
		document.getElementById(obj2).value="";
	}
}
</script>
</head>

<body style="overflow-x:hidden;">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" action="/CareforExecuteAction.do" onSubmit="return val();" target="formFrame">
	<input type="hidden" name="operation" value="$globals.getOP('OP_ADD')">
	<input type="hidden" name="clientId" value="$clientId">
	<input type="hidden" name="careforId" value="$bean.id">
	<input type="hidden" name="noback" value="true">
	<input type="hidden" name="careforName" value="$bean.name">
	
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("crm.careforaction.title.pathexec")</div>
	<!-- 
	<ul class="HeadingButton_Pop-up">
		<li>
		<button onClick="start();">$text.get('oa.bbss.begin')</button></li>
			<li><button name="back" type="button" onClick="javascript:closeTheWin();">$text.get("common.lb.close")</button>
		</li>
	</ul> -->
</div>
    <div style=" float:left; width:730px;padding:5px;overflow-x:hidden; overflow-y:auto;">
		<table border="0" width="720" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td width="100" align="right" valign="middle">$text.get('call.lb.client')：</td>
				<td width="570">$clientName</td>
			</tr>
			<tr>
				<td align="right" valign="middle">$text.get('crm.carfor.lb.path')：</td>
				<td>$bean.name</td>
			</tr>
			<tr>
				<td align="right" valign="middle">$text.get('crm.carefor.lb.afterdate')：</td>
				<td><input type="text" readonly="readonly" name="baseDate" value="$time" class="text2" onClick="openInputDate(this);"></td>
			</tr>
			<tr>
				<td align="right" valign="middle">$text.get('crm.careforaction.lb.executant')：</td>
				<td>
					<input name="actorName" id="actorName" type="text"  class="text2"
					onKeyDown="if(event.keyCode==13) event.keyCode=9"
							ondblclick="openSelect1('/UserFunctionAction.do?operation=22&selectName=EmpleeNameAndId&ClientId=$!clientId','$text.get('crm.careforaction.lb.executant')','actor','actorName')"';><img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('/UserFunctionAction.do?operation=22&selectName=EmpleeNameAndId&ClientId=$!clientId','$text.get('crm.careforaction.lb.executant')','actor','actorName')"';>
					<input id="actor" name="actor" type="hidden">
					<br>
					<font color="#0000ff">$text.get('crm.careforaction.title.executeman')</font>
				</td>
			</tr>
			<tr>
				<td align="right" valign="middle">$text.get('crm.careforaction.lb.tomen')：</td>
				<td>
					#foreach($linkman in $clients)
						<div style="width:140px; float:left; height:25px;"><input type="checkbox" name="linkman" value="$globals.get($linkman,3)">$globals.get($linkman,1)&nbsp;</div>
						#if($velocityCount%8==0)
						<br>
						#end
					#end
					<div style="width:100%; float:left; border-top:1px dashed #CCCCCC">&nbsp;<font color="#0000ff">$text.get('crm.careforaction.title.toman')</font></div>
				</td>
			</tr>
		</tbody>
	  </table>	
  </div>
			
	
</form>
</body>
</html>
