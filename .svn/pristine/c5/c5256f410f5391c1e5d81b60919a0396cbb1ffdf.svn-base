<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("excel.lb.addModel")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript">

function getExcel(obj)
{
	if(obj.id == "mappedBtn"){
		$("type").value = "excelmapped";
	 	var path = $("fileName").value;
	   if(path.length>0) {
	        var pos=path.lastIndexOf(".");
			var lastName=path.substring(pos,path.length);
			if(lastName.toLowerCase()!=".xls")
			{
				alert('$text.get("excel.format.error")');
				return false;
			}
			
		}else {
		     alert('$text.get("excel.up.select")');
			 return false;
	   }
    }else if(obj.id == "dataBtn"){
		$("type").value = "excelData";
	}
	document.excelform.submit();
}

function $(obj){
	return document.getElementById(obj);
}

function ref()
{
	window.location.refresh();
}

function sava(){
	$("type").value="saveDate";
	document.excelform.submit();
}
</script>
</head>

<body>

<form  method="post" id="excelform" name="excelform" action="/InCardAnnal.do" enctype="multipart/form-data">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("com.checkOnWorkAttendance.importCardNote")</div>
	<ul class="HeadingButton">
		<li><button type="button" #if($enableDiv=="false")disabled="disabled"#end onClick="sava()" class="b2">$text.get("common.lb.save")</button></li>
		<li><button type="button" name="backList" title="Ctrl+Z" onClick="javascript:document.excelform.submit();" class="b2">$text.get("common.lb.back")</button></li>
	</ul>
</div>
		<div class="listRange_1">	
				<li style="width:300px"><span>$text.get("excel.lb.upstruct")：</span>
					<input type="file" name="fileName"   id="fileName" style=" width:170px" />
					</li>
					<li style=" padding-top:1px;">
					<button type="button" id="mappedBtn" onClick="getExcel(this)" class="b4">excel.map.add</button>
					<button type="button" id="dataBtn" #if($enable=="false")style="display: none;"#end onClick="getExcel(this)" class="b4">$text.get("com.checkOnWorkAttendance.excelUpload.importData")</button>
					</li>
		</div>
		<input type="hidden" name="type" id="type">
		<div class="scroll_function_small_a" id="mappedDiv" #if($enableDiv=="true")style="display: none;"#end>
			<table border="0" width="500" cellpadding="0" cellspacing="0" class="listRange_list">
				<thead>
					<tr style="text-align: center;">
						<td>$text.get("com.checkOnWorkAttendance.excelUpload.tblMapColName")</td>
						<td width="250">$text.get("com.checkOnWorkAttendance.excelUpload.colNo")</td>
					</tr>
				</thead>
				<tbody id="mappedTbody" style="text-align: center;">
					<tr>
						<td>$text.get("user.lb.employeeId")</td>
						<td>
							<select name="employeeNo" id="employeeNo" style="width: 150px;" >
							#foreach($cellNo in $cells)
								<option value="$cellNo">$text.get("common.theNo")$cellNo$text.get("common.column")</option>
							#end
							</select>
						</td>
					</tr>
					<tr>
						<td>$text.get("com.checkOnWorkAttendance.CardNote")</td>
						<td>
							<select name="cardAnnal" id="cardAnnal" style="width: 150px;">
							#foreach($cellNo in $cells)
								<option value="$cellNo">$text.get("common.theNo")$cellNo$text.get("common.column")</option>
							#end
							</select>
						</td>
					</tr>
				</tbody>
			</table>
		</div> 
		<div class="scroll_function_small_a" id="dataDiv" #if($enableDiv == "false")style="display: none;"#end>
			<table border="0" width="500" cellpadding="0" cellspacing="0" class="listRange_list" >
				<thead style="text-align: center;">
					<tr>
						<td class="listheade" width="30" align="center">
								<span style="vertical-align:middle;"><IMG
										src="/$globals.getStylePath()/images/down.jpg" border=0 /> </span>
							</td>
						<td>$text.get("user.lb.employeeId")</td>
						<td width="250">$text.get("com.checkOnWorkAttendance.CardNote")</td>
					</tr>
				</thead>
				<tbody id="dataTbody" style="text-align: center;">
				#foreach($!brushCardAnnal in $!brushCardAnnals)
					<tr>
						<td class="listheadonerow" align="center" width="30">
								$velocityCount
							</td>
						<td>
						#if($!brushCardAnnal.employeeNo != "")
						$!brushCardAnnal.employeeNo #else &nbsp; #end</td>
						<td>
						#if($!brushCardAnnal.cardAnnalTime != "")
						$!brushCardAnnal.cardAnnalTime #else &nbsp; #end</td>
					</tr>
				#end
				</tbody>
			</table>
		</div>
				<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("dataDiv");
var sHeight=document.body.clientHeight-100;
oDiv.style.height=sHeight+"px";
//]]>	</script>
				#if($enablePage == "true")
				<div class="listRange_pagebar" style="position:relative">
					$text.get("common.theNo")$pageNo$text.get("ao.common.page")&nbsp;&nbsp;
					<a href="javascript:document.excelform.submit();"
						onclick="getFristPage()">$text.get("common.firstPage")</a>&nbsp;&nbsp;#if($pageNo!=1)
					<a href="javascript:document.excelform.submit();"
						onclick="prePage()">$text.get("common.prePage")</a>#end&nbsp;&nbsp;
					#if($pageNo!=$pageSumList.size())
					<a href="javascript:document.excelform.submit();"
						onclick="nextPage()">$text.get("common.nextPage")</a>#end&nbsp;&nbsp;&nbsp;
					<select name="pageNo" id="pageNo">
						#foreach($pageNumber in $pageSumList) #if($pageNo == $pageNumber)
						<option value="$pageNumber" selected="selected">
							$pageNumber
						</option>
						#else
						<option value="$pageNumber">
							$pageNumber
						</option>
						#end #end
					</select>
					<input type="hidden" id="pageSize" name="pageSize"
						value="$pageSize">
					<button type="submit" name="ppbutton" onClick="return excelData();">
						go
					</button>
					&nbsp;
				</div>
				#end
 
</form> 
</body>
<script type="text/javascript">
		function excelData(){
			$("type").value = "excelData";
			return true;
		}
		function nextPage(){
			$("pageNo").value = Number($("pageNo").value) + 1;
			excelData();
		}
		
		function prePage(){
			$("pageNo").value = Number($("pageNo").value) - 1 < 1?1:Number($("pageNo").value)-1;
			excelData();
		}
		
		function getFristPage(){
			$("pageNo").value = 1;
			excelData();
		}
	</script>
</html>
