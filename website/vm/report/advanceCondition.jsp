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
<script type="text/javascript">
function add(){
	parent.showAdvanceUpdatePanel("add","");
}
function sort(){
	parent.showAdvanceUpdatePanel("sort","");
}
function update(id){
	parent.showAdvanceUpdatePanel("update",id);
}
function selectfa(id){
	jQuery.get("/ReportDataAction.do?operation=advance&opType=def&id="+id+"&reportNumber=$!reportNumber",function(){
		if(parent.location.href.indexOf("/UserFunctionQueryAction.do")>-1){
			parent.location.href="/UserFunctionQueryAction.do?tableName="+$("#tableName",parent.document).val()+"&moduleType="+$("#moduleType",parent.document).val()+"&winCurIndex="+$("#winCurIndex",parent.document).val()+"&parentTableName="+$("#parentTableName",parent.document).val()+"&checkTab=Y&src=menu";
		}else{ 
			
			parent.location.href="/ReportDataAction.do?reportNumber="+$("#reportNumber",parent.document).val()+
				"&noback="+$("#noback",parent.document).val()+
				"&detTable_list="+$("#detTable_list",parent.document).val()+
				"&moduleType="+$("#moduleType",parent.document).val()+
				"&popWinName="+$("#popWinName",parent.document).val()+
				"&winCurIndex="+$("#winCurIndex",parent.document).val()+
				"&checkTab=Y&src=menu";
		}
	});	
}

#if("$!msg" != "")
	alert('$!msg');
#end
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
</style>
</head>
<body style="overflow: hidden;">
<div class="Heading">
	<div class="HeadingTitle"></div>
	<ul class="HeadingButton">
		<li><span onClick="sort();" class="btn btn-small">排序</span></li> 
		<li><span onClick="add();" class="btn btn-small">添加方案</span></li> 
		<li><span onClick="javascript:closeAsyn();" class="btn btn-small">$text.get("common.lb.close")</span></li>	
	</ul>
	<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>
</div>
<div id="listRange_id">
		<div class="scroll_function_small_a" id="conter" style="height: 340px;">
			<div id="formatList">
			<table border="0" width="550" cellpadding="0" cellspacing="0" class="listRange_list_function"  id="tblSort">
			<thead>
				<tr >
					<td width="*" align="center">方案名称</td>
					<td width="50" align="center">默认</td>
					<td width="110" align="center">$text.get("common.lb.print.Operation")</td>				
				</tr>			
			</thead>
			<tbody>
				<tr #if($noDef)style="background-color: #78A56A;" #end ondblclick="selectfa('')">
					<td align="left">默认方案</td>
					<td align="left">#if($noDef)默认#end</td>
					<td align="left"><a href="javascript:selectfa('')"/>选择</a></td>				
				</tr>
				#foreach($row in $result)
				<tr #if(!$noDef && $globals.get($row,2) == "1")style="background-color: #78A56A;" #end ondblclick="selectfa('$globals.get($row,0)')">
					<td align="left">$globals.get($row,1)</td>
					<td align="left">#if(!$noDef && $globals.get($row,2) == "1")默认#end</td>
					<td align="left">						
						<a href="javascript:selectfa('$globals.get($row,0)')"/>选择</a>&nbsp;&nbsp;
						<a href="javascript:update('$globals.get($row,0)')"/>修改</a>&nbsp;&nbsp;
						<a href="/ReportDataAction.do?operation=advance&opType=delete&id=$globals.get($row,0)&reportNumber=$!reportNumber"/>删除</a>
					</td>				
				</tr>
				#end			
			</tbody>
			<tbody>
			
			</tbody>
			</table>
			</div>
		</div>
</div>
</body>
</html>