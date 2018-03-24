<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crmdesk.lb.myattention")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBoxList.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script src="/js/jquery.js" type="text/javascript"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script language="javascript" src="/js/jquery.divbox.js"></script>
<script language="javascript" src="/js/aioselect.js"></script>
<script language="javascript">
  function beforeDelete(){
  	if(!isChecked('keyId')){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	form.submit();
  }

</script>
</head>

<body onLoad="showtable('tblSort');showStatus(); ">

<form style="margin:0px;"  method="post" scope="request" name="form" action="/CrmDeskTopAction.do?type=attentionList">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div style="float:left;margin-left:30px;padding-bottom:3px;">
	<span style="margin-left: 40px;">$text.get("common.lb.key"):<input name="title" type="text" value="$!title" style="border:0;width: 95px;border-bottom: 1px solid #1E95E9;text-align:left;"/></span>
	<span style="margin-left: 20px;">
			<button name="Submits" type="submit" class="b2" style="width: 60px;">$text.get("common.lb.query")</button>
		</span>
	</div>
	<ul class="HeadingButton">	
	<li><button type="button"" onclick="beforeDelete();" class="b2">$text.get("oa.bbs.bt.abolishAttention")$text.get("oa.bbs.bt.Attention")</button>
	</ul>	
</div>

		<div class="scroll_function_small_a" id="conter" >
			<script type="text/javascript">
				var oDiv=document.getElementById("conter");
				var sHeight=document.body.clientHeight-115;
				oDiv.style.height=sHeight+"px";
				</script>
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort" width="100%">
			<thead>
				<tr>
				    <td width="25" align="center"> <input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
					<td  align="center" >$text.get("message.lb.title")</td>
					<td width="180" align="center">$text.get("oa.common.lastUpdateTime")</td>
				</tr>			
			</thead>
			<tbody>
				#foreach ($row in $result )
				<tr>
				   <td align="center">
				   <input type="checkbox" name="keyId" value="$globals.get($row,0)">
				   </td>
				   
					<td align="left"><a href="javascript:mdiwin('$globals.get($row,2)','$globals.get($row,4)')"> $globals.get($row,1) </a> &nbsp;</td>
					<td align="left">$!globals.get($row,3)  &nbsp;</td>								
				</tr>
				#end		
			</tbody>
		  </table>
		</div>	
	<div class="listRange_pagebar"> $!pageBar </div>
	</div>
	</form>
	
	
</body>
</html>

