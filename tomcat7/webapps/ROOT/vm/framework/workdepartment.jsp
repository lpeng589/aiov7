<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.individual.info")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
</head>
<body style="overflow-x:hidden">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$!globals.get($emp,1) $text.get("com.individual.info")</div>
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
				<td colspan="4" bgcolor="#E8F3FF" class="oabbs_tl"><strong>$text.get("com.individual.info")</strong></td>
			</tr>
			<tr>
				<td class="oabbs_tr" width="118">$text.get("com.job.dept")：</td>
				<td width="172">$!globals.get($emp,2)&nbsp;</td>
			    <td width="91" align="right">$text.get("com.individual.duty")：</td>
			    <td width="169">$!globals.getEnumerationItemsDisplay("duty",$globals.get($emp,5))&nbsp;</td>
			</tr>
			<tr>
				<td class="oabbs_tr" width="118">$text.get("upgrade.lb.tel")：</td>
				<td width="172">$!globals.get($emp,6)&nbsp;</td>
			    <td width="91" align="right">$text.get("upgrade.lb.phone")：</td>
			    <td width="169">$!globals.get($emp,7)&nbsp;</td>
			</tr>
			<tr>
				<td class="oabbs_tr" width="118">$text.get("oa.email.address")：</td>
				<td width="172">$!globals.get($emp,8)&nbsp;</td>
			    <td width="91" align="right">$text.get("oa.communcaton.birthday")：</td>
			    <td width="169">$!globals.get($emp,9)&nbsp;</td>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("com.superior.lead")：</td>
				<td colspan="3">$!managerName&nbsp;</td>
			</tr>
			
			<tr>
				<td class="oabbs_tr">$text.get("com.quarters.responsibility")：</td>
				<td colspan="3">
					$!globals.encodeHTML($globals.get($emp,3))&nbsp;
				</td>
		    </tr>
			
			
			
			<tr>
				<td class="oabbs_tr">$text.get("com.underling.staff")：</td>
				<td colspan="3">
					#foreach($bean in $empList)
						#if("$bean.id"!="$!globals.get($emp,0)")
						$bean.empFullName 、

						#end
					#end
					&nbsp;
				</td>
		    </tr>
			<!-- <tr>
				<td colspan="4" bgcolor="#E8F3FF" class="oabbs_tl"><strong>$text.get("com.day.count")</strong></td>
			</tr>
			#foreach($info in $infoList)
				#if($globals.get($info,0)=="$text.get('com.day.count')")
				<tr>
				<td class="oabbs_tr">$globals.get($info,1)：</td>
					<td colspan="3">$globals.get($info,2)&nbsp;</td>
			    </tr>
			    #end
		    #end
			
			<tr>
				<td colspan="4" bgcolor="#E8F3FF" class="oabbs_tl"><strong>$text.get("com.month.count")</strong></td>
			</tr>
			#foreach($info in $infoList)
				#if($globals.get($info,0)=="$text.get('com.month.count')")
				<tr>
				<td class="oabbs_tr">$globals.get($info,1)：</td>
					<td colspan="3">$globals.get($info,2)&nbsp;</td>
			    </tr>
			    #end
		    #end
			
			<tr>
				<td colspan="4" bgcolor="#E8F3FF" class="oabbs_tl"><strong>$text.get("com.year.count")</strong></td>
			</tr>
			#foreach($info in $infoList)
				#if($globals.get($info,0)=="$text.get('com.year.count')")
				<tr>
				<td class="oabbs_tr">$globals.get($info,1)：</td>
					<td colspan="3">$globals.get($info,2)&nbsp;</td>
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
