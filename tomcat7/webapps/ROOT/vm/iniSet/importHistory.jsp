<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>期初导入历史</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<style type="text/css">
.listRange_1{overflow:hidden;}
.title_div{margin: 10px 10px 10px 20px;}
.listRange_list_function tbody tr{height:30px;}
.listRange_pagebar{float: right;margin-right: 40px;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/k_listgrid.js"></script>
<script type="text/javascript" src="/js/dropdownselect.js"></script>
<script type="text/javascript">
	function deleteImp(impId){
		document.form.impId.value=impId;
		document.form.submit();
	}
</script>
</head>
 
<body>
<form method="post" scope="request" id="form" name="form" action="/IniAccQueryAction.do">
<input type="hidden" id="operation" name="operation" value="7"/>
<input type="hidden" id="opType" name="opType" value="delImport"/>
<input type="hidden" id="impId" name="impId" value=""/>
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/> 
<input type="hidden" name="accCode" value="$!accCode"/>
<input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
		<div class="Heading">
			<div class="HeadingTitle" style="padding:0;">
				期初导入历史
			</div>
			<ul class="HeadingButton">
				<li>
					
				</li>
			</ul>
		</div>
		<div class="listRange_1" id="listid">
			
		</div>		
		<div class="scroll_function_small_a" id="list_id">
			<script type="text/javascript">
			var oDiv=document.getElementById("list_id");
			var dHeight=document.documentElement.clientHeight;
			var sHeight=document.getElementById("listid").clientHeight;
			oDiv.style.height=dHeight-sHeight-100+"px";;
			</script>
			<table width="600" border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="tblSort" name="table">
				<thead>
					<tr class="scrollColThead">
						<td width="45">No.</td>
						<td width="100">导入执行人</td>
						<td width="*">导入时间</td>
						<td width="120">操作</td>
					</tr>
				</thead>
				<tbody>
					#foreach ($row in $result)						 				
					<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
						<td align ="center" >$velocityCount</td>
						<td align="left"  style="cursor: default;">$row.userName &nbsp;</td>
						<td align="left" >$row.createTime </td>
						<td  class="center_td">						
						<a href='javascript:void(0)' onclick="deleteImp('$row.impId')" style="color:blue;">删除记录及期初 &nbsp;</a>
							
						</td>
					</tr>
					#end
				</tbody>
			</table>
		</div>
		</form>
	</body>
</html>