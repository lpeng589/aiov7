<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.dept.info")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
</head>
<body style="overflow-x:hidden">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$!globals.get($deptInfo,0) $text.get("com.particular.info") </div>
	<ul class="HeadingButton">	  
		<li style="margin-right:10px">
			<button onClick="window.close();">$text.get("common.lb.close")</button>
		</li>
	</ul>
</div>
<div class="ao_listRange_small">
		<table border="0" align="center" cellpadding="0" width="540" cellspacing="0" class="oalistRange_list_function" name="table">
		<tbody>
			<tr>
				<td colspan="4" bgcolor="#E8F3FF" class="oabbs_tl"><strong>$text.get("com.dept.info")</strong></td>
			</tr>
			<tr>
				<td class="oabbs_tr" width="118">$text.get("com.dept.superintend")：</td>
				<td width="202">$!globals.get($deptInfo,1)&nbsp;</td>
			    <td width="91" align="right">$text.get("com.dept.people.quantity")：</td>
			    <td width="139">$!deptNumber&nbsp;</td>
			</tr>	
			<tr>
				<td class="oabbs_tr">$text.get("com.quarters.responsibility")：</td>
				<td colspan="3">$!globals.get($deptInfo,2)&nbsp;</td>
			</tr>
			 <!-- 
			<tr>
				<td colspan="4" bgcolor="#E8F3FF" class="oabbs_tl"><strong>$text.get("com.day.count")</strong></td>
			</tr>
			#foreach($info in $infoList)
				#if($globals.get($info,0)=="$text.get('com.day.count')")
				<tr>
				<td class="oabbs_tr">$!globals.get($info,1)：</td>
					<td colspan="3">$!globals.get($info,2)&nbsp;</td>
			    </tr>
			    #end
		    #end
			
			<tr>
				<td colspan="4" bgcolor="#E8F3FF" class="oabbs_tl"><strong>$text.get("com.month.count")</strong></td>
			</tr>
			#foreach($info in $infoList)
				#if($globals.get($info,0)=="$text.get('com.month.count')")
				<tr>
				<td class="oabbs_tr">$!globals.get($info,1)：</td>
					<td colspan="3">$!globals.get($info,2)&nbsp;</td>
			    </tr>
			    #end
		    #end
			
			<tr>
				<td colspan="4" bgcolor="#E8F3FF" class="oabbs_tl"><strong>$text.get("com.year.count")</strong></td>
			</tr>
			#foreach($info in $infoList)
				#if($globals.get($info,0)=="$text.get('com.year.count')")
				<tr>
				<td class="oabbs_tr">$!globals.get($info,1)：</td>
					<td colspan="3">$!globals.get($info,2)&nbsp;</td>
			    </tr>
			    #end
		    #end
			  -->
		  </tbody>
		</table>
	</div>
</div>
</body>
</html>
