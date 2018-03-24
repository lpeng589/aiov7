<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("user.lb.userAdmin")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">


</head>
<body >

 
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">
		#if($isAdmin != "")
			$text.get("user.lb.EmailSetting")
		#else
			#if($type!="modifyPass")
				$text.get("user.lb.userAdmin")
			#else 
				$text.get("user.lb.modifyPass") 
			#end
		#end
	</div>
	<ul class="HeadingButton">
	<li><button type="button"  onClick="location.href='/UserQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
	</ul>
</div>
<div id="listRange_id">
		<div class="scroll_function_small_a" id="conter">
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr>
					<td width="50" align="center">$text.get("iniAcc.lb.iniAcc.lb.sequencenumber")</td>
					<td width="150" align="center">$text.get("userManager.lb.userName")</td>
					<td width="150" align="center">$text.get("user.lb.employeeId")</td>
					
					<td width="400" align="center">$text.get("com.error.cause")</td>
				</tr>
			</thead>
			<tbody>
				#set($index=0)
				#foreach ($row in $list)
				#set($index=$index+1)
				<tr>
					<td width="50" align="center">$index</td>
					<td width="150" align="center">$!globals.get($row,0)</td>
					<td width="150" align="center">$!globals.get($row,1)</td>
					<td width="400" align="center">$!globals.get($row,2)</td>
					
				</tr>
				#end
			</tbody>
		  </table>
		</div>	
</div>
</form>
</body>
</html>
