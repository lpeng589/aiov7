<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("wokeflow.list.record")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
</head>
<body>
	<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("wokeflow.list.record")</div>
	<ul class="HeadingButton">

	<li>
		#if("$!fromPage"!="yes")
		<button name="button" type="button" onClick="javascript:location='/UserFunctionQueryAction.do?tableName=$!tableName&checkTab=Y&parentTableName=$!parentTableName&f_brother=$!f_brother&parentCode=$!parentCode'" class="b2">$text.get("common.lb.back")</button>
		#else
		<button onClick="javascript:window.close();" style="width: 68px;">$text.get("common.lb.close")</button>
		#end
				
	</li>
	</ul>
</div>	
<div class="listRange">
    <div class=".scroll_function_small_a" id="conter">
    	#if("flow"=="$existFlow")
		<table width=800" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table">
		<thead>
			<tr>
				<td width="90" class="oabbs_tc">$text.get("sms.note.time")</td>
				<td width="60" class="oabbs_tc">$text.get("com.auditing.operator")</td>
				<td width="50" class="oabbs_tc">$text.get("com.operate.type")</td>
			    <td width="250" class="oabbs_tc">$text.get("common.lb.appove")</td>
			    <td width="80" class="oabbs_tc">$text.get("common.lb.appoveNext")</td>
			    <td width="100" class="oabbs_tc">$text.get("common.lb.appover")</td>
			    <td width="100" class="oabbs_tc">$text.get("common.lb.underWrite")</td>
			</tr>
		</thead>
		<tbody>
			#foreach($log in $loglist)
		 	<tr>
				<td class="oabbs_tc">$!log.AuditingTime&nbsp;</td>
				<td class="oabbs_tc">$!log.Assessor&nbsp;</td>
			    <td class="oabbs_tc">$!log.AuditingType &nbsp;</td>
			    <td class="oabbs_tl" title="$!log.attitude">$!log.attitude &nbsp;</td>
			    <td class="oabbs_tc" title="$!log.workFlowNode">$!log.workFlowNode&nbsp;</td>
			    <td class="oabbs_tc" title="$!log.nextChkpName">$!log.nextChkpName &nbsp;</td>
			    <td class="oabbs_tc" title="$!log.nextChkpName">#if($!log.pictureWay.length()>0)<img src="/ReadFile.jpg?fileName=$globals.urlEncode($!log.pictureWay)&tempFile=false&type=PIC&tableName=tblAutograph" width="150" height="150" border="0">#end &nbsp;</td>
			</tr>
			#end		
		  </tbody>
		</table>
		#else
		<table width=600" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table">
		<thead>
			<tr>
				<td style="width:40px" class="oabbs_tc">
					<input type="checkbox" name="checkbox" value="checkbox" onClick="selectAll(this.checked)" id="checkbox">
				</td>
				<td width="20%" class="oabbs_tc">$text.get("wokeflow.node.name")</td>
				<td width="*" class="oabbs_tc">$text.get("com.auditing.operator")</td>
				<td width="150" class="oabbs_tc">$text.get("com.operate.type")</td>
			    <td width="150" class="oabbs_tc">$text.get("sms.note.time")</td>
			</tr>
		</thead>
		<tbody>
			#foreach($log in $loglist)
		 	<tr>
				<td class="oabbs_tc">
				  <input  type="checkbox" name="cb" value="$log.Id" #if($!log.id == 0) disabled #end/>
				</td>
				<td class="oabbs_tl">$!log.workFlowNode &nbsp;</td>
				<td class="oabbs_tl" #if($!log.id == 0)  colspan="3" #end >#if($!log.id == 0) $text.get("com.tobe.approved")&nbsp;#else $!log.Assessor #end </td>
			    #if($!log.id != 0)<td class="oabbs_tc">$log.Auditingtypename &nbsp;</td>
				
			    <td class="oabbs_tc">$!log.AuditingTime #end</td>
			</tr>
			#end		
		  </tbody>
		</table>
		#end
		</div>
</div>	
</body>
</html>
