<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("wokeflow.add.approval")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript" src="/js/formvalidate.vjs"></script>
<script language="javascript">
var ModuleName;
var TableName;
function openSelect3(urlstr,obj,field){
	var str  = window.showModalDialog(urlstr+"&MOID=17&MOOP=add","","dialogWidth=730px;dialogHeight=450px"); 
 
	fs=str.split(";");  
	form.ModuleName.value=fs[0];
	form.TableName.value=fs[1];	
	form.FlowName.value=fs[1]+".xml";	
}
</script>
</head>

<body>

<form action="/WokFlowAction.do?operation=$globals.getOP("OP_ADD")" name="form" method="post">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("wokeflow.add.approval")</div>
	<ul class="HeadingButton">
				<li><button type="button" onClick="location.href='/WokFlowAction.do'" class="b2">$text.get("common.lb.back")</button>
				</li>
						
			</ul>
</div>
<div id="listRange_id">
<div class="scroll_function_small_a" id="conter">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
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
	<input name="ModuleName" type="text" onKeyDown="if(event.keyCode!=13&&event.keyCode!=9)event.returnValue=false;"  onDblClick="openSelect3('/UserFunctionAction.do?tableName=tblDBTableInfo&selectName=workflow_table_select&operation=$globals.getOP("OP_POPUP_SELECT")')" value="" maxlength="50" class="text">
				  <img src="/$globals.getStylePath()/images/St.gif" onClick="openSelect3('/UserFunctionAction.do?tableName=tblDBTableInfo&selectName=workflow_table_select&operation=$globals.getOP("OP_POPUP_SELECT")')">
			</td>
		</tr>		
		<tr>
			<td class="oabbs_tr">$text.get("excel.add.tableName")：</td>
			<td>
			  <input type="text" name="TableName" value="" class="text"></td>
			</tr>

		<tr>
			<td class="oabbs_tr" valign="top">$text.get("wokeflow.add.file")：</td>
			<td><input type="text" name="FlowName" value="" class="text"></td>
		</tr>
		<tr>
			<td class="oabbs_tr" valign="top">$text.get("excel.list.isUsed")：</td>
			<td>			 	
          		<input type="radio" checked name="IsStatart" value="1"/>$text.get("currency.lb.isChecked")     
				<input type="radio" name="IsStatart" value="0"/>$text.get("currency.lb.noChecked")       			    		
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
