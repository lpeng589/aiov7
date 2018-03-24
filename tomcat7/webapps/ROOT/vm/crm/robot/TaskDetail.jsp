<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/listGrid.js"></script>
<script>
</script>


</head>
<body >
<form  method="post" scope="request" name="form" action="">
	<input type="hidden" name="operation" value="4">	
	<div class="oamainhead">
		<div class="HeadingButton">
		<li style="padding-right:20px">
			<button type="button" onclick="location.href='/RobotAction.do'">$text.get("common.lb.back1")&nbsp;&nbsp;$text.get("common.lb.back2")</button>
		<li>
		
		</div>
	</div>
	
	<div id="oalistRange">
	<script type="text/javascript">
	var oDiv=document.getElementById("oalistRange");
	var sHeight=document.body.clientHeight-68;
	oDiv.style.height=sHeight+"px";
	</script>
		<table width="99%" border="0" align="center" id="tblSort" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table">
		<thead>
			<tr>
			    <td class="oabbs_tl">$text.get("common.lb.synopsis")</td>
				<td class="oabbs_tc" width="300">$text.get("common.lb.URL")</td>			
				<td class="oabbs_tc" width="100">$text.get("oa.common.tel")</td>
				<td class="oabbs_tc" width="100">$text.get("oa.common.phone")</td>
				<td class="oabbs_tc" width="200">$text.get("oa.mail.email")</td>
				<td class="oabbs_tc" width="100">$text.get("common.lb.fax")</td>
			</tr>
		</thead>
		<tbody>				
		  #foreach ($vg in $list)
		 	<tr >
			    <td class="oabbs_tl">$vg.getName()</td>
				<td class="oabbs_tl"><a href="$vg.getSite()" target="_blank">$globals.encodeHTML($vg.getSite())</a></td>
				<td class="oabbs_tl">$vg.getPhone()</td>
				<td class="oabbs_tl">$vg.getMobile()</td>
				<td class="oabbs_tl">$vg.getEmail()</td>
				<td class="oabbs_tl">$vg.getFax() </td>
			</tr>
		 #end
		
		  </tbody>
	   </table>
</div>	   
</form>	
</body>
</html>