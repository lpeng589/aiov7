<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.client.attention.path")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript">
function openSelect1(urlstr,displayName,obj,obj2){
	displayName=encodeURI(displayName) ;
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined"){return;}
	if(str.length>0){
		var strs = str.split(';');
		document.getElementById(obj).value=strs[0];
		document.getElementById(obj2).value=strs[1];
	}else{
		document.getElementById(obj).value="";
		document.getElementById(obj2).value="";
	}
}
function query(){
	form.submit();
}
function openMessage(id){
	showModalDialog('/CareforExecuteAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&delId='+id,winObj,'dialogWidth=1065px;dialogHeight=660px');
	location.reload();
}
function Del(){
	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	if(!window.confirm('$text.get("com.delete.message.path")'))return;
	document.getElementById("operation").value = "$globals.getOP('OP_DELETE')" ;
	form.submit();
}

function extendSubmit(status)
{
	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	document.getElementById("operation").value = "$globals.getOP('OP_STOPORENABLE')" ;
	document.getElementById("statusTo").value = status ;
	form.submit();
}

function stopPath(){
	if(!isChecked("keyId")){		
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
}
</script>
</head>

<body onLoad="">
<form method="post" name="form" action="/CareforExecuteAction.do">
	<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')">
	<input type="hidden" name="statusTo" value="">
		<div class="Heading">
			<div class="HeadingIcon">
				<img src="/$globals.getStylePath()/images/Left/Icon_1.gif" />
			</div>
			<div class="HeadingTitle">
				$text.get("crm.path")
			</div>
				
		</div>
			<div class="listRange_1" id="listid">
				<ul>
					<li>
						<span>$text.get("crm.client.name"):</span>
						<input name="clientName" value="$bean.clientName">
					</li>
					<li>
						<span>$text.get("crm.path.name"):</span>
						<input name="careforName" value="$bean.careforName">
					</li>
					<li>
						<span>$text.get("sms.note.status"):</span>
						<input name="status" type="text" value="#foreach($enum in $globals.getEnumerationItems('actionStatus'))#if($enum.value == $bean.careStatus) $enum.name #end#end "/>
						</span>
					</li>
					
				</ul>
			</div>
	
		<div class="scroll_function_small_a" id="list_id">
				<script type="text/javascript">
var oDiv=document.getElementById("list_id");
var sHeight=document.body.clientHeight-100;
oDiv.style.height=sHeight+"px";
</script>
			<table width="500" border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="tblSort" name="table">
				<thead>
					<tr>
						
						
						<td width="50" class="oabbs_tc">
							$text.get("iniAcc.lb.iniAcc.lb.sequencenumber")
						</td>
						<td width="*" class="oabbs_tc">
							$text.get("oa.common.detailed")
						</td>
						
					</tr>
				</thead>
				<tbody>
					#set($index = 0)
					#foreach($dets in $bean.careforDel)
					#set($index = $index+1)
					<tr onMouseMove="setBackground(this,true);"
						onMouseOut="setBackground(this,false);">

						
						<td width="50" >
							$index
						</td>

						<td>
							$globals.toHTML("$dets")&nbsp;&nbsp;
						</td>
					</tr>
					#end
				</tbody>
			</table>
		</div>
	</form>
</body>
</html>
