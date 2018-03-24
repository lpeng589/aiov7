<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		<link rel="stylesheet"
			href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script language="javascript" src="/js/function.js"></script>
		<script type="text/javascript" src="/js/mrp.vjs"></script>
		<script>
		function returnVal(){
				var cb = document.getElementsByName("ra");
				var rows = $("tblSort").rows;
				var val = "";
				var title = "";
				for(i=0;i<cb.length;i++){
					if(cb[i].checked){
						val +=n("ra")[i].value+";";
						val +=n("goodsnumber")[i].value+";";
						val +=n("goodsName")[i].value+";";
						index = 3;//没加属性时表格的列数

						proplen = rows[0].cells.length-index;
						for(j=0;j<proplen;j++){
							title +=rows[0].cells[index+j].innerText+"/";
							val += rows[i+1].cells[index+j].innerText+"/";
						}	
								
					}
				}
				var fieldsName = n("fieldsName")[0].value;
				val +=title+fieldsName;
				val +=n("bomNo")[i].value+";";			
				window.returnValue=val;
				window.close();
			}	
			 window.name="formFrame"//弹出窗口提交form在本页显示

			 //把分页的事件改掉,把所有的超链接都改掉了(这里可能会出错)
			 var toPage=new Array();
			 function aLink(){
			 	var alinks = document.getElementsByTagName("a");
			 	for(i=0;i<alinks.length;i++){
			 	
			 		toPage[toPage.length]=alinks[i].href.replace("javascript:","");
			 		alinks[i].attachEvent("onclick",onsub(toPage[i]));
			 		alinks[i].href="#";			 		
			 		
			 	}
			 }
			 function onsub(link){
			 	return function(){
			 		eval(link);
			 	}
			 }
		</script>
	</head>
<body onLoad="showtable('tblSort');showStatus();checkFirst('ra');aLink() ">
	<iframe name="formFrame" style="display:none"></iframe>
		<form method="post" scope="request" name="form" action="/MrpBP.do?method=selGoods" target="formFrame">
			<div>
				<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
				<div class="HeadingTitle">$text.get("mrp.lb.chooseGoods")</div>
				<ul class="HeadingButton">
					<li><button type="button" onClick="returnVal()" class="b2">$text.get("common.lb.submit")</button></li>
					<li><button type="button" onClick="window.close()" class="b2">$text.get("mrp.lb.back")</button></li>
				</ul>
				<div id="listRange_id">
					<div class="scroll_function_small_a" id="conter">
					<input name="fieldsName" value="$fieldsName" type="hidden">
					<table border="0" cellpadding="0" cellspacing="0"
						class="listRange_list_function" name="table" id="tblSort" width="500">
						<thead>
							<tr style="position:relative;top:expression(this.offsetParent.scrollTop);">
							<td width="50">$text.get("mrp.lb.chooseOne")</td>
							<td width="150"> $text.get("mrp.lb.goodsNumber") </td>
							<td width="150">$text.get("mrp.lb.goodsFullName") </td>
							<td width="150"> $text.get("muduleFlow.lb.produceDetailList") </td>
							#foreach($prop in $props)
							<td width="80">$prop</td>
							#end
							</tr>
						</thead>
						<tbody>
						#foreach($row in $list)
						<tr onDblClick="returnVal()">
							<td>
							<input type="radio" name="ra" value="$row.get("id")">
							<input type="hidden" name="goodsnumber" value="$row.get("goodsnumber")">
							<input type="hidden" name="bomNo" value="$row.get("billNo")">billNo
							</td>
							<td>$row.get("goodsnumber")&nbsp;</td>
							<td><input value="$row.get("goodsFullName")" name="goodsName" readonly="readonly"></td>
							<td>$row.get("billNo")&nbsp;</td>
							#foreach($prop in $propList)
								#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
								<td>$!row.get($prop.propName)&nbsp;</td>
								#end
							#end
							</tr>
						#end
						</tbody>
					</table>					
					</div>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-92;
oDiv.style.height=sHeight+"px";
//]]>
</script>
<div class="listRange_pagebar"> $!pageBar </div>	
</div>
		</form>
	</body>
</html>
