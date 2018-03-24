<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("common.lb.print.FormatDeisgn")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" /> 
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<script type="text/javascript">
var condStr = "<option value=''></option>";



function loadSort(){	
	if(parent.cols){
		for(var i = 0;i<parent.cols.length;i++){
			if(parent.cols[i][3] != "3"){
				condStr +="<option value='"+parent.cols[i][1]+"'>"+parent.cols[i][0]+"</option>";
			}
		}		
	}
	
	
	deFs = $("#defineOrderBy",parent.document).val().split(",");
	for(var i = 0;i<deFs.length;i++){
		var deF = deFs[i].trim();
		var f = deF.substring(0,deF.indexOf(" "));
		var o = deF.substring(deF.indexOf(" ")).trim();
		addRowline();
		$("#tblSort tbody tr:last",document).find("select[name=sortField]").val(f);
		$("#tblSort tbody tr:last",document).find("select[name=order]").val(o);
	}
	$("#tblSort tbody tr:first",document).remove();
	addRowline();
}
function beforSubmit(){
	var ordby = '';
	$("#tblSort tbody tr",document).each(function(){
		field = $(this).find("select[name=sortField]").val();
		if(field != "" && ordby.indexOf(field)==-1){
			ordby += field+" "+$(this).find("select[name=order]").val()+",";
		}
	});
	if(ordby.length > 0){
		ordby = ordby.substring(0,ordby.length-1);
	}
	$("#defineOrderBy",parent.document).val(ordby);
	jQuery.close("advanceUpdatePanel");
	jQuery.close('advancePanel');
	if( parent.beforeButtonQuery()) {
   		parent.submitQuery();
   	}
}

function addRowline(){
	var html = '<tr onclick="focustr(this)"><td ><select name="sortField" >'+condStr+'</select></td><td >\
		<select name="order"><option value="ASC">升序</option><option value="DESC">降序</option></select></td></tr>';
	$("#tblSort tbody tr:last",document).after(html);
}
function delline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请先选择需要删除的字段");
		return;
	}
	if(curRow != null){
		$(curRow).remove();
		curRow = null;
	}
	curRowNo  = -1;
}
//上移单元格  
function upline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请选择需要上移的字段");
		return;
	}
	$(curRow).insertBefore($(curRow).prev());
}
//下移单元格  
function downline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请选择需要下移的字段");
		return;
	}
	$(curRow).insertAfter($(curRow).next());
}


var curRow = null;
function focustr(obj){
	$(".selected",document).removeClass("selected");
	curRow = obj;	
	$(curRow).addClass("selected");
	if($("#tblSort tbody tr:last",document)[0] == obj){
		addRowline();		
	}
}
</script>
<style>
.tips_div{width:250px;height:147px;display: none;cursor:default;position:absolute;left:200px;top:50px;border:5px solid #B9E6FF; background-color:#fff;}
.tips_title{height:25px;line-height:25px;vertical-align:middle;background-color:#B9E6FF;padding:3px 0 0 0;}
.left_title{float:left;padding-left:10px;font-family:微软雅黑;font-size:14px;font-weight:bold;color:#000;}
.tips_close{padding:2px 2px 0 0;float:right;}
.tips_content{margin:20px 0 0 10px;text-align:center;float:left;}
.tips_operate{width:100%;margin:20px 0 10px 0;float:left;text-align:center;}
.HeadingButton{margin:0;}
.HeadingButton li{margin:0 5px 0 0;}
.stBtn {background-position: -32px 0;background-color: #fff;cursor: pointer;}
.selected{background-color: #B6BA31;}
</style>
</head>
<body style="overflow: hidden;" onload="loadSort()">
<div class="Heading">
	<div class="HeadingTitle"></div>
	<ul class="HeadingButton">
		<li><span onClick="javascript:beforSubmit();" class="btn btn-small">排序</span></li>
		<li><span onClick="javascript:parent.closeAdvanceUpdate();" class="btn btn-small">$text.get("common.lb.close")</span></li>		
	</ul>
	<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>
</div>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post"  scope="request" name="form"  action="/ReportDataAction.do?operation=advance&opType=save" onSubmit="return beforSubmit(this);" target="formFrame">

<div id="listRange_id">
			<li style="padding:5px 5px">				
				<ul class="detbtBar">
					<li id="b_addline" class="f-icon16" onclick="addRowline()" title="添加字段"></li>
					<li id="b_upline" class="f-icon16" onclick="upline()" title="上移"></li>
					<li id="b_downline" class="f-icon16" onclick="downline()" title="下移"></li>	
					<li id="b_delline" class="f-icon16" onclick="delline()" title="删除字段"></li>						
				</ul>
			</li>
		<div class="scroll_function_small_a" id="conter" style="height: 440px;">
			<div id="formatList">
			<table border="0" width="550" cellpadding="0" cellspacing="0" class="listRange_list_function"  id="tblSort">
			<thead>
				<tr>
					<td width="*" align="center">字段</td>	
					<td width="150" align="center">排序</td>		
				</tr>			
			</thead>
			<tbody>				
				<tr onclick="focustr(this)">
					<td ><select name="sortField" ><option value=""></option></select></td>	
					<td >
						<select name="order">
							<option value="ASC">升序</option>
							<option value="DESC">降序</option>
						</select>
					</td>		
				</tr>		
			</tbody>
			<tbody>
			
			</tbody>
			</table>
			</div>
		</div>
</div>
</form>
</body>
</html>