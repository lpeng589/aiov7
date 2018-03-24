<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.mail.mailDirectory")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
 
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/listGrid.js"></script>

<script>

function del(email){
 	if(confirm("$text.get("oa.common.sureDelete")")){
		location.href("/EMailBlackQueryAction.do?operation=$!globals.getOP("OP_DELETE")&keyId="+email);
	}
}

function openSelect(){
	var urlstr = '/UserFunctionAction.do?operation=22&displayName='+encodeURI("$text.get("oa.mydata.createBy")")+'&LinkType=@URL:&selectName=ReportSelectEmployee';
	var str = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=query",winObj,"dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)!="undefined"){
		if(str==";"){
			document.getElementById("empName").value="";
		}else{
			document.getElementById("empName").value=str.split(";")[0];
		}
	}
}
</script>
</head>
<body >
<form  method="post" scope="request" name="form" action="EMailBlackQueryAction.do">
	<input type="hidden" name="operation" value="4">	
	
	<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("email.title.blackMgt") </div>
		<ul class="HeadingButton">
		<li><button name="qu" type="submit">$text.get("common.lb.query")</button>	
		<li>
	#if($LoginBean.operationMap.get("/EMailBlackQueryAction.do").add())
		<li><button name="readOuter" type="button" onClick="window.location.href='EMailBlackAction.do?operation=$globals.getOP("OP_ADD_PREPARE")'">$text.get("common.lb.add")</button>	
		<li>
	#end
		</ul>
	</div>
	<div class="listRange_frame">
		<div class="listRange_1" id="listid">
			<li><span>$text.get("oa.email.address")：</span><input name="email" value="$!EMaiBlackSearchForm.email" ></li>	
			<li><span>$text.get("role.lb.createBy")：</span><input name="empName" value="$!EMaiBlackSearchForm.empName" ondblclick="openSelect();"><img src="/$globals.getStylePath()/images/St.gif" onClick="openSelect();"></li>			
		</div>
		<div class="scroll_function_small_a" id="conter">
			<script type="text/javascript">
var oDiv=document.getElementById("conter");
var dHeight=document.body.clientHeight;
var varHeight=document.getElementById("listid");
var sHeight = 0 ;
if(typeof(varHeight)!="undefined" && varHeight!=null){
	sHeight = varHeight.clientHeight ;
}
oDiv.style.height=dHeight-sHeight-68+"px";
</script>			
			
		<table sortCol="0" isDesc="true" border="0" isStatsAll=false hasStat=true cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table" width="895">
		<THead>
		<tr class="scrollColThead">
		<td class="oabbs_tc">$text.get("oa.email.address")</td>
						<td class="oabbs_tl">$text.get("role.lb.createBy")</td>			
						<td class="oabbs_tc" >$text.get("common.lb.createTime")</td>
						<td class="oabbs_tc" width="10%">$text.get("oa.calendar.option")</td>
		</tr>
		</THead>
		<TBody>
		#foreach ($vg in $list)
	 	<tr >
		    <td class="oabbs_tl">$!globals.get($vg,0)</td>
			<td class="oabbs_tl">$!globals.get($vg,1)</td>
			<td class="oabbs_tc">$!globals.get($vg,2)</td>				
			<td class="oabbs_tc"> &nbsp;				   	
				#if($LoginBean.operationMap.get("/EMailBlackQueryAction.do").delete())
			 	<a  href="javascript:del('$!globals.get($vg,0)')">$text.get("common.lb.del")</a>
			 	&nbsp;
			 	#end				 	
			</td>
		</tr>
		#end
		</TBody>
		</table>
		</div>	
<div class="listRange_pagebar" style="position:relative">$!pageBar</div>
</div>
	
</form>	
</body>
</html>