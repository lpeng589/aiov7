<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script> 
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function beforSubmit(){
	#if($LoginBean.id !="1") alert("请用admin帐号修改此配置"); return;  #end
	
	if(confirm("修改显示列设置将默认所有列配置信息！是否继续？")){	
		form.save.value="true";
		form.submit();
	}
}	
</script>

</head>
<body>
<iframe name="formFrame" style="display: none"></iframe>
<form name="form" method="post"  action="/ReportSetQueryAction.do"  target="formFrame">
<input type="hidden" name="operation" value="4">
<input type="hidden" name="showSet" value="true">
<input type="hidden" name="save" value="false">
<input type="hidden" name="tableName" value="$!tableName">

	<div class="data" id="conter" style="width:100%;text-align:center;overflow-y:auto;overflow-x:hidden">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-172;
oDiv.style.height=sHeight+"px";
</script>	
	<table border="0" width="1000" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
		<thead>
		<tr>
			<td width="150">字段</td>
			<td width="50">报表显示</td>
			<td width="50">单据显示</td>
			<td width="50">弹窗显示</td>
			<td width="50">弹窗条件</td>
			<td width="60">关键字搜索</td>
		</tr>
		</thead>
		<tbody>
		#set($tableN=$tableName)
		#if($tableN.indexOf("_") > 0) #set($tableN=$tableName.substring(0,$tableN.indexOf("_")))  #end
		#foreach($row in $globals.getTableInfoBean($tableN).fieldInfos)		
			#if("$row.inputType" != "100" && "$row.fieldType" != "1" && "$row.fieldType" != "0" && 
				"$row.fieldType" != "16" && "$row.fieldType" != "3" && "$row.fieldType" != "14" 
				&& $row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother" && $row.fieldName != "HueGroup"
				 && $row.fieldName != "YearNoGroup" && 
				("$row.inputType" == "0" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "0") 
				|| "$row.inputType" == "7" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "7")
				|| "$row.inputType" == "1" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "1")
				|| "$row.inputType" == "16" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "16") 
				|| $row.fieldName == "BaseUnit"))	
				#set($fname="${tableName}_${row.fieldName}") 				
				#set($reportView="")
				#set($billView="")
				#set($popSel="")
				#set($keyword="")
				#set($popupView="")
				#foreach($rr in $result)
					#if(($globals.get($rr,0)+"_"+$globals.get($rr,1))==$fname)
						#if($globals.get($rr,2)=="1")
							#set($reportView="checked")
						#end
						#if($globals.get($rr,3)=="1")
							#set($billView="checked")
						#end
						#if($globals.get($rr,4)=="1")
							#set($popSel="checked")
						#end
						#if($globals.get($rr,5)=="1")
							#set($keyword="checked")
						#end
						#if($globals.get($rr,6)=="1")
							#set($popupView="checked")
						#end
					#end							
				#end	
				<tr>
					<input type="hidden" name="fName" value=$fname />
					<td align="left" style="padding-left:20px">$row.display.get($globals.getLocale())</td>
					<td><input type="checkbox" name="reportView_$fname" value="1" $!reportView/></td>
					<td><input type="checkbox" name="billView_$fname" value="1" $!billView/></td>
					<td><input type="checkbox" name="popupView_$fname" value="1" $!popupView/></td>
					<td><input type="checkbox" name="popSel_$fname" value="1" $!popSel/></td>
					<td><input type="checkbox" name="keyword_$fname" value="1" $!keyword/></td>
				</tr>
			#end	
			#end
		<tr></tr>
		</tbody>
	</table>				
		</div>
	<div>
	<p style="margin:5px;color:blue">说明：这里对相关字段进行全局设置</p>
	<p style="margin:5px;color:blue">1、报表显示:选择后将会在所有相关报表中显示该字段</p>
	<p style="margin:5px;color:blue">2、单据显示:选择后将在所有单据中显示该字段</p>
	<p style="margin:5px;color:blue">3、弹窗显示:选择后将在所有相关弹出窗中显示该字段</p>
	<p style="margin:5px;color:blue">4、弹窗条件:选择后将在所有相关弹出窗的查询条件中增加此字段</p>
	<p style="margin:5px;color:blue">5、关键字搜索：选择后将在所有相关弹出窗中以此字段为作关键字进行模糊查询，增加录入的方便性，但同时会显著降低系统性能，请谨慎使用。如选择商品编号和商品名称作关键字，则在所有录入商品的地方输入任意字符回车后将会查询出所有的商品编号或商品名称包含该字符的数据</p>
	</div>
</form>
</body>
</html>