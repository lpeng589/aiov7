<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.readingMark")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
</head>
<body>
<div style="height:260px;overflow:auto;">
		<table border="0" cellpadding="0" width="100%" cellspacing="0" class="listRange_list_function" name="table">
		<thead>
			<tr>
				<td width="40" class="oabbs_tc" style="width:40px">&nbsp;
			    <td width="120" class="oabbs_tc">$text.get("oa.readingMark.reader")</td>
			    <td width="120" class="oabbs_tc">$text.get("oa.employee.department")</td>
			    <td width="200" class="oabbs_tc">$text.get("oa.readingMark.read.time")</td>
			    <!--  <td width="150" class="oabbs_tc">$text.get("oa.readingMark.visitMachine")</td>-->
			    <td width="*" class="oabbs_tc">$text.get("oa.readingMark.visitIP")</td>
		    </tr>
		</thead>
		<tbody>
		 #foreach($vo in $listOAReadingRecord)
			<tr>
				<td class="oabbs_tc">
					$!velocityCount
				</td>
			    <td class="oabbs_tl">$!globals.getEmpNameById($!vo.reader)&nbsp;</a></td>
			    <td class="oabbs_tl">$!globals.getDeptByUserId($!vo.reader)</td>
			    <td class="oabbs_tl">$!vo.readingTime&nbsp;</td>
			   <!--  <td class="oabbs_tc">$!vo.visitMachine&nbsp;</td>-->
			    <td class="oabbs_tl">$!vo.ip&nbsp;</td>
		    </tr>
		#end
		 </tbody>
		</table>
		<div class="listRange_pagebar"> $!pageBar </div>
</div>
</body>
</html>
