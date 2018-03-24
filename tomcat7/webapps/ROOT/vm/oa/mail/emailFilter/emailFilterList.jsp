<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>$text.get("unit.lb.emailFilter")</title>
		<link rel="stylesheet"
			href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script language="javascript" src="/js/function.js"></script>
		
		<style type="text/css">
			.newsReply{
			
		        width: 300px;
		        margin-left: auto;
		        margin-right: auto;
		        padding: 3px;
		        outline: 0;
		        word-wrap: break-word;
		        overflow-x: hidden;
		        overflow-y: auto;
		        _overflow-y: visible">
			}				
		
		</style>
	</head>
	<body onLoad="showtable('tblSort');showStatus(); ">
		<form method="post" scope="request" name="form"
			action="/EmailFilterQueryAction.do">
			<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
			<input type="hidden" name="org.apache.struts.taglib.html.TOKEN"
				value="$!globals.getToken()">
			
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("unit.lb.emailFilter")
				</div>
				<ul class="HeadingButton">
					#if($LoginBean.operationMap.get("/EmailFilterQueryAction.do?operation=$globals.getOP('OP_QUERY')").add())
					##if($globals.getMOperation().add())
					<li>
						<button type="button" onClick="location.href='/EmailFilterAction.do?operation=$globals.getOP("OP_ADD_PREPARE")'" class="b2">
							$text.get("common.lb.add")
						</button>
					</li>
					#end 
					#if($LoginBean.operationMap.get("/EmailFilterQueryAction.do?operation=$globals.getOP('OP_QUERY')").delete())
					##if($globals.getMOperation().delete())
					<li>
						<button type="submit"
							onClick="javascript:return sureDel('keyId');" class="b2">
							$text.get("common.lb.del")
						</button>
					</li>
					#end
				</ul>
			</div>
			<div id="listRange_id">
				<div class="listRange_1">
				
					<div style="float: left;margin-left: 40px;">
						<span>$text.get("EmailFilter.mailAddress")：</span>
						<input name="oaMailinfoSetting" type="text" value="$!emailFilterSearchForm.getOaMailinfoSetting()"
							maxlength="50">
					</div>
					<div style="float: left;margin-left: 30px;">	
						<span>$text.get("EmailFilter.filterCondition")：</span>
						<input name="filterCondition" type="text" value="$!emailFilterSearchForm.getFilterCondition()"
							maxlength="50">
					</div>
					<div style="float: left;margin-left: 35px;">	
						<button name="Submit" type="submit"
							onClick="forms[0].operation.value=$globals.getOP('OP_QUERY');return beforeBtnQuery();" class="b2">
							$text.get("common.lb.query")
						</button>
						<button name="clear" type="button" onClick="clearForm(form);"
							class="b2">
							$text.get("common.lb.clear")
						</button>
					</div>
				
				</div>
				<div class="scroll_function_small_a" id="conter">
					<table border="0" cellpadding="0" cellspacing="0"
						class="listRange_list" name="table" id="tblSort">
						<thead>
							<tr>

								<td width="30px" class="oabbs_tc">
									<input type="checkbox" name="selAll"
										onClick="checkAll('keyId')">
								</td>
								<td class="oabbs_tc" width="200px">
									<center>$text.get("EmailFilter.mailAddress")</center>
								</td>
								<td class="oabbs_tc" width="150px">
									$text.get("EmailFilter.folderName")
								</td>
								<td width="330px" class="oabbs_tc">
									<div>$text.get("EmailFilter.filterCondition")</div>
								</td>
								#if($LoginBean.operationMap.get("/EmailFilterQueryAction.do?operation=$globals.getOP('OP_QUERY')").update())
								##if($globals.getMOperation().update())
								<td width="90" class="oabbs_tc">
									$text.get("oa.calendar.option")
								</td>
								#end
							</tr>
						</thead>
						<tbody>
							#foreach ($filter in $result )
							<tr onMouseMove="setBackground(this,true);"
								onMouseOut="setBackground(this,false);">

								<td align="center">
									<input type="checkbox" name="keyId" value="$!filter.id">
								</td>
								<td align="left">
									$!filter.addressName &nbsp;
								</td>

								<td align="left">
									$!filter.folderName &nbsp;
								</td>
								<td align="left">
									<div class="newsReply">$!filter.filterCondition &nbsp;
									</div>
								</td>
								
								<td align="center">
								 	#if($LoginBean.operationMap.get("/EmailFilterQueryAction.do?operation=$globals.getOP('OP_QUERY')").update())
									##if($globals.getMOperation().update())
									<a
										href='/EmailFilterAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$filter.id'>$text.get("common.lb.update")</a>&nbsp;&nbsp;&nbsp;
										<a href="/EmailFilterAction.do?operation=$globals.getOP('OP_DETAIL')&id=$filter.id">$text.get("common.lb.detail")&nbsp;</a> 
									#end
								</td>
							</tr>
							#end
						</tbody>
					</table>
				</div>
				<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-100;
oDiv.style.height=sHeight+"px";
</script>
				<div class="listRange_pagebar">
					$!pageBar
				</div>
			</div>
		</form>
	</body>
</html>
