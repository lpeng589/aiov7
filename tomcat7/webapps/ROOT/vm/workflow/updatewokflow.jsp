<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("wokeflow.update.approval")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript" src="/js/formvalidate.vjs"></script>
<script language="javascript">
</script>
</head>

<body>

<form action="/WokFlowAction.do?operation=$globals.getOP("OP_UPDATE")" name="form" method="post" >
<input type="hidden" name="id" value="$wokflow.id"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("wokeflow.update.approval")</div>
	<ul class="HeadingButton">
				<li><button type="button" onClick="location.href='/WokFlowAction.do'" class="b2">$text.get("common.lb.back")</button></li>
						
			</ul>
</div>
<div id="listRange_id">
<div class="scroll_function_small_a" id="conter">
			<script type="text/javascript">
				var oDiv=document.getElementById("conter");
				var sHeight=document.body.clientHeight-38;
				oDiv.style.height=sHeight+"px";
			</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
		<tr>
			<td class="oabbs_tr">$text.get("module.lb.moduleName")：</td>
			<td>
			<input name="ModuleName" type="text" value="$wokflow.ModuleName"  class="text">				  
			</td>
		</tr>		
		<tr>
			<td class="oabbs_tr">$text.get("excel.add.tableName")：</td>
			<td>
			  <input type="text" name="TableName" value="$wokflow.TableName" class="text"></td>
			</tr>

		<tr>
			<td class="oabbs_tr" valign="top">$text.get("wokeflow.add.file")：</td>
			<td><input type="text" name="FlowName" value="$wokflow.FlowName" class="text"></td>
		</tr>
		<tr>
			<td class="oabbs_tr" valign="top">$text.get("excel.list.isUsed")：</td>
			<td>			 	
          		<input type="radio" #if($wokflow.IsStatart==1) checked #end name="IsStatart" value="1"/>$text.get("currency.lb.isChecked")      
				<input type="radio" #if($wokflow.IsStatart==0) checked #end name="IsStatart" value="0"/>$text.get("currency.lb.noChecked")        			    		
		 	</td>
		</tr>		
		<tr>										
			<td colspan="2" class="oabbs_tc"> <button type="submit" name="Submit" class="b2">$text.get("common.lb.save")</button>
			  <button type="reset" class="b2">$text.get("common.lb.reset")</button>
			 </td>
         </tr>
		 </tbody>
		</table>	
	</div>
</div>
</form>
</body>
</html>
