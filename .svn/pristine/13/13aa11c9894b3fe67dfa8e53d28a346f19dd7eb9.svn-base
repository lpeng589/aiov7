<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("AIOSHOP.lb.warmsetting")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript">
function openSelect(obj1,obj2){
	var displayName=encodeURI("$text.get("message.lb.receiver")") ;
	var str  = window.showModalDialog("/UserFunctionAction.do?selectName=MsgReceive&operation=$globals.getOP("OP_POPUP_SELECT")&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=700px;dialogHeight=450px");  
	if(typeof(str)=="undefined")return ;
	if(str.indexOf("@Sess:")>=0||str.length<=3){
		document.getElementById(obj1).value="";
		document.getElementById(obj2).value="";
		return ;
	}
	var varStr = str.split("|");  
	var varBbc1 = document.getElementById(obj1).value ;
	var varBbc2 = document.getElementById(obj2).value ;
	for(var i=0;i<varStr.length;i++){
		var varUser = varStr[i].split(";");
		if(varUser[0]!=""){
			var isExist = varBbc2.indexOf(varUser[0]+",") ;
			if(isExist==-1){
				varBbc1 += varUser[1]+"," ;
				varBbc2 += varUser[0]+"," ;
			}
		}
	}
	document.getElementById(obj1).value = varBbc1;
	document.getElementById(obj2).value = varBbc2;
}

function beforSubmit(){
	return true ;
}
</script>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/AIOShopAction.do?operation=2" onSubmit="return beforSubmit(this);" target="formFrame">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">
		$text.get("AIOSHOP.lb.warmsetting")
	</div>
	<ul class="HeadingButton">
		
	</ul>
</div>
<div id="listRange_id" align="center">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	    <div style="margin-top:10px;">
			<table width="620" cellpadding="0" align="center" cellspacing="0" class="oabbs_function_index" name="table" id="tblSort">
				<thead>
					<tr>
						<td width="150px;" align="center">$text.get("message.lb.SynchronizationData")</td>
						<td width="150px;" align="center">$text.get("comm.alert.mode")</td>
						<td width="320px;" align="center">$text.get("message.lb.receiver")</td>
					</tr>			
				</thead>
				<tbody>
					<tr>
				      	<td class="oabbs_tr">$text.get("message.lb.OrderMessage")</td>
				      	<td align="center">&nbsp;$text.get("message.lb.MobileMessage")</td>
				      	<td>&nbsp;<input type="text" name="orderMobile" class="inputText" value="$!globals.get($!result,0)"/></td>
				    </tr>
					<tr>
				      	<td class="oabbs_tr">&nbsp;</td>
				      	<td align="center">&nbsp;$text.get("message.lb.popupMessage")</td>
				      	<td>&nbsp;<input type="text" name="orderMsg" class="inputText" value="$!orderMsg" readonly="readonly" ondblclick="openSelect('orderMsg','orderMsgId')"/>
				      			  <input type="hidden" name="orderMsgId" value="$!globals.get($!result,1)" />
				      			  <img src="/$globals.getStylePath()/images/St.gif" onClick="openSelect('orderMsg','orderMsgId');" style="cursor: pointer;"/></td>
				    </tr>
					<tr>
				      	<td class="oabbs_tr">&nbsp;</td>
				      	<td align="center">&nbsp;$text.get("timeNote.lb.email")</td>
				      	<td>&nbsp;<input type="text" name="orderEmail" class="inputText" value="$!globals.get($!result,2)"/>
				      			</td>
				    </tr>
				    
					<tr>
				      	<td class="oabbs_tr">$text.get("message.lb.VIPData")</td>
				      	<td align="center">&nbsp;$text.get("message.lb.MobileMessage")</td>
				      	<td>&nbsp;<input type="text" name="memberMobile" class="inputText" value="$!globals.get($!result,3)"/></td>
				    </tr>
					<tr>
				      	<td class="oabbs_tr">&nbsp;</td>
				      	<td align="center">&nbsp;$text.get("message.lb.popupMessage")</td>
				      	<td>&nbsp;<input type="text" name="memberMsg" class="inputText"  value="$!memberMsg"  readonly="readonly" ondblclick="openSelect('memberMsg','memberMsgId')"/>
				      			<input type="hidden" name="memberMsgId" value="" value="$!globals.get($!result,4)"/>
				      			<img src="/$globals.getStylePath()/images/St.gif" onClick="openSelect('memberMsg','memberMsgId');" style="cursor: pointer;"/></td>
				    </tr>
					<tr>
				      	<td class="oabbs_tr">&nbsp;</td>
				      	<td align="center">&nbsp;$text.get("timeNote.lb.email")</td>
				      	<td>&nbsp;<input type="text" name="memberEmail" class="inputText" value="$!globals.get($!result,5)"/></td>
				    </tr>
				    
				    <tr>
				      	<td class="oabbs_tr">$text.get("message.lb.consultationAccuse")</td>
				      	<td align="center">&nbsp;$text.get("message.lb.MobileMessage")</td>
				      	<td>&nbsp;<input type="text" name="consultMobile" class="inputText" value="$!globals.get($!result,6)"/></td>
				    </tr>
					<tr>
				      	<td class="oabbs_tr">&nbsp;</td>
				      	<td align="center">&nbsp;$text.get("message.lb.popupMessage")</td>
				      	<td>&nbsp;<input type="text" name="consultMsg" class="inputText" value="$!consultMsg" readonly="readonly" ondblclick="openSelect('consultMsg','consultMsgId')"/>
				      			<input type="hidden" name="consultMsgId" value="" value="$!globals.get($!result,7)"/>
				      			<img src="/$globals.getStylePath()/images/St.gif" onClick="openSelect('consultMsg','consultMsgId')" style="cursor: pointer;"/></td>
				    </tr>
					<tr>
				      	<td class="oabbs_tr">&nbsp;</td>
				      	<td align="center">&nbsp;$text.get("timeNote.lb.email")</td>
				      	<td>&nbsp;<input type="text" name="consultEmail" class="inputText" value="$!globals.get($!result,8)"/></td>
				    </tr>
				    <tr>
				      	<td class="oabbs_tr">&nbsp;$text.get("message.lb.InputClew")</td>
				      	<td colspan="2">&nbsp;<font color="red">$text.get("message.lb.InputReceiverClew")</font></td>
				    </tr>
				    
				    <tr>
				      	<td class="oabbs_tr">&nbsp;</td>
				      	<td colspan="2">&nbsp;<button type="submit" style="width: 70px;">$text.get("common.lb.save")</button></td>
				    </tr>
				</tbody>
			</table>
		</div>
</div>
</form> 
</body>
</html>