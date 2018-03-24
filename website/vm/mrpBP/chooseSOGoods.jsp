<html>
	<head>
<meta name="renderer" content="webkit">		<base target="_self">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>$text.get("mrp.lb.maOrderChoose")</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script language="javascript" src="/js/function.js"></script>
		<script type="text/javascript" src="$globals.js("/js/mrp.vjs","",$text)"></script>
		<script>
		function returnVal(){
				var cb = document.getElementsByName("ra");
				var rows = $("tblSort").rows;
				var val = "";
				var title = "";
				var fieldsName = n("fieldsName")[0].value;
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
						if(n("bomNo")[i]!=undefined){
							val +=title+fieldsName;
							val +=";"+n("bomNo")[i].value;
						}					
					}
				}
				
				window.returnValue=val;
				window.close();
			}	
			 window.name="formFrame"//弹出窗口提交form在本页显示


			 //把分页的事件改掉,弹出框中使用<a>会打开一个新的页面

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
			 		var links=link.split(";");
			 		eval(links[0]+";"+links[1]+";"+links[3]);
			 		
			 	}
			 }
	function queryGoods(){
		var form = document.getElementsByName("form")[0];
		form.action = "/MrpBP.do?method=selGoods";
		form.submit();
	}
		</script>
	</head>
<body scroll="no" onLoad="showtable('tblSort');showStatus();checkFirst('ra');aLink() ">
		<form method="post" scope="request" name="form" action="/MrpBP.do?method=selGoods" target="formFrame">
		<input type="hidden" name="operation" value="4" >
		<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("mrp.lb.chooseGoods")</div>
	<ul class="HeadingButton_Pop-up">
	<li><button type="button" onClick="returnVal();">$text.get("common.lb.submit")</button></li>
	<li><button type="button" onClick="queryGoods();">$text.get("common.lb.query")</button></li>
	<li><button type="button" onClick="window.close()">$text.get("mrp.lb.back")</button></li>		
	</ul>
	<div class="listRange_frame">
		<div class="listRange_1" id="listid">
			<li>
				$text.get("mrp.lb.goodsNumber")：<input type="text" name="selGoodsNumber" value="$!selGoodsNumber">
			</li>
			<li>
				$text.get("mrp.lb.goodsFullName")：<input type="text" name="selGoodsFullName" value="$!selGoodsFullName">
			</li>
			<li>
				$text.get("iniGoods.lb.goodsSpec")：<input type="text" name="selGoodsSpec" value="$!selGoodsSpec">
			</li>
		</div>
	</div>
					
	<div class="listRange_Pop-up_scroll">
					<input name="fieldsName" value="$fieldsName" type="hidden">
					#if($!props.size()>0)
					#set($tableWidth=850+80*$!props.size())
					#else
					#set($tableWidth=850)
					#end
					<table border="0" cellpadding="0" cellspacing="0"
						class="listRange_list" name="table" id="tblSort" width="$tableWidth">
						<thead>
							<tr style="position:relative;top:expression(this.offsetParent.scrollTop);">
								<td width="50" align="center">NO.</td>
								<td width="60" align="center">$text.get("mrp.lb.chooseOne")</td>
								<td width="180" align="center">$text.get("mrp.lb.goodsNumber")</td>
								<td width="180" align="center">$text.get("mrp.lb.goodsFullName")</td>
								<td width="120" align="center">$text.get("iniGoods.lb.goodsSpec")</td>
								<td width="150"> $text.get("muduleFlow.lb.produceDetailList") </td>
								#foreach($prop in $propList)
									#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
										<td width="80" align="center">$prop.display.get($!locale)</td>
									#end
								#end
							</tr>
						</thead>
						<tbody>
						#set($rowNum=0)
						#foreach($row in $list)
						<tr onDblClick="document.getElementsByName('ra')[$rowNum].checked='checked';returnVal()">							
							<td align="center">$velocityCount</td>
							<td><input type="radio" name="ra" value="$row.get("id")">
							<input type="hidden" name="goodsnumber" value="$row.get("goodsnumber")">
							<input type="hidden" name="bomNo" value="$row.get("billNo")">
							</td>
							<td>$row.get("goodsnumber")&nbsp;</td>
							<td>
							
							<input value="$row.get("goodsFullName")" name="goodsName" readonly="readonly"></td>
							<td>$row.get("goodsSpec")&nbsp;</td>
							<td>$row.get("billNo")&nbsp;</td>
							#foreach($prop in $propList)
									#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
								<td><input type="text" name="$prop.propName" value="$!row.get($prop.propName)">&nbsp;</td>
									#end
								#end
							</tr>
							#set($rowNum=$rowNum+1)
						#end
						</tbody>
					</table>					
									
					
					</div>
					<div  style="float:left; width:300px; height:25px; line-height:25px; text-indent:5px;">$text.get("mrp.help.info3")</div>
					<div class="listRange_pagebar"> $!pageBar </div>		

		</form>
	</body>
</html>
