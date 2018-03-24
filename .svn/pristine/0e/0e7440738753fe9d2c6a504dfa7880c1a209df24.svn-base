<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.calendar.calendarList")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">

<script language="javascript" src="/js/date.vjs"></script>
<script>
	function selectAll(boolvalue){
		var allCheckBoxs=document.getElementsByName("cb");
		for(var i=0;i<allCheckBoxs.length;i++){
			if(allCheckBoxs[i].type=="checkbox"){
				if(allCheckBoxs[i].disabled==false){
						allCheckBoxs[i].checked=boolvalue;
				}		
			}
		}
	}
	function add(){
		var allCheckBoxs=document.getElementsByName("cb");		
		var tablenames="";
		var displays="";
		for(var i=0;i<allCheckBoxs.length;i++){
			if(allCheckBoxs[i].type=="checkbox"){
				if(allCheckBoxs[i].checked==true){
					var value = allCheckBoxs[i].value;
					var title = allCheckBoxs[i].title;
					tablenames+=value+",";
					displays+=title+",";					
				}
			}			
		}	
		dialogArguments.optionTitle	= displays;
		dialogArguments.optionValue = tablenames;
		dialogArguments.setValueOption();
		window.close();
	}
	function dd(){
		//document.form2.target="";
	}
</script>
</head>
<body>

	<iframe name="formFrame" style="display:none"></iframe>
	<form action="/WokFlowAction.do" method="post" name="form2" onSubmit="dd()" target="formFrame">
		<input type="hidden" name="type" value="tablelist">
		<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
		<input type="hidden" name="pageParam" value="" />
		<div>
			<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
			<div class="HeadingTitlesmall">$text.get("table.list.function")</div>
			<ul class="HeadingButton_Pop-up">
				<li>
					<button type="button" onClick="add()" class="b2">$text.get("wokFlow.lb.apply")</button>
				</li>
			</ul>
			</div>
				<div class="listRange_Pop-up">
					<ul class="listRange_1_Pop-up">
						<li>
							<span>$text.get("table.list.displayname")：</span>		
							<input type="text" id="display" name="display" value="">	
						</li>
						<li>
							<span>$text.get("customTable.lb.tableName")：</span>		
							<input type="text" id="tableName" name="tableName" value="">	
						</li>		
						<li>
							<button name="button" type="submit" class="b2">$text.get("common.lb.query")</button>
						</li>
					</ul>
					  <div class="listRange_Pop-up_scroll">
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table">
						<thead>
							<tr>
								<td style="width:30px" class="oabbs_tc">
									<input type="checkbox" name="checkbox" value="checkbox" onClick="selectAll(this.checked)" id="checkbox">
								</td>
								<td class="oabbs_tc">$text.get("table.list.displayname")</td>
								<td class="oabbs_tc">$text.get("customTable.lb.tableName")</td>
							</tr>
						</thead>
						<tbody>		
						 #foreach($vo in $list)
							<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
								<td class="oabbs_tc">
								  <input  type="checkbox" name="cb" value="$vo.tableName" title="$vo.tableDisplay.display"/>
								</td>
								<td class="oabbs_tl">$vo.tableDisplay.display&nbsp;</td>
								<td class="oabbs_tl">$vo.tableName&nbsp;</td>		
							</tr>
						#end							
						  </tbody>
						</table>
				
					</div>
					<div class="oalistRange_pagebar">
						$!pageBar	  
					</div>	
				</div>
	</form>
</body>
</html>
