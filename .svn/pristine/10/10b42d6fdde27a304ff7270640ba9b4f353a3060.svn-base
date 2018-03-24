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
function addFormatStyle(){
	var addFormat = document.getElementById("addFormat") ;
	addFormat.style.display = "block" ;
}

function closeFormat(){
	var addFormat = document.getElementById("addFormat") ;
	addFormat.style.display = "none" ;
}

function submitFormat(){
	var styleName = document.getElementById("styleName").value ;
	if(styleName.length==0){
		alert("$text.get('common.lb.print.FormatNull')") ;
		return ;
	}
	if(getStringLength(styleName)>30){
		alert("$text.get('common.lb.print.FormatName')"+"$!text.get('common.validate.error.longer')"+"30"+"$!text.get('common.excl')\n $!text.get('common.charDesc')")
		return ;
	}
	var reportNumber = document.getElementById("styleReportNumber").value ;	
	AjaxRequest("/UtilServlet?operation=setFormatStyle&reportNumber="+reportNumber+"&styleName="+encodeURI(styleName));
	if(response=="OK"){
		alert("$text.get('common.msg.addSuccess')") ;
		var addFormat = document.getElementById("addFormat") ;
		addFormat.style.display = "none" ;
		document.getElementById("reload").click() ;
	}else{
		alert("$text.get('common.msg.addFailture')") ;
	}
	
}
function deleteFormat(reportId){
	if(confirm('$text.get("common.msg.confirmDel")')){
		AjaxRequest("/UtilServlet?operation=setFormatStyle&reportId="+reportId) ;
		document.getElementById("reload").click() ;
	}
}

function m(value){
	return document.getElementById(value) ;
}

//存放弹出框值



var hideReportId;//reportId
var hideNames;//回填的select的名称



var hideIds;//回填隐藏域ID的名称



function showDept(deptNames,deptIds,reportId){
	hideReportId = reportId;
	hideIds = deptIds;
	hideNames = deptNames;
	var urlstr = '/UserFunctionAction.do?operation=22&tableName=tblDepartment&fieldName=DeptFullName&selectName=SelectDepartmentMes'+"&MOID=$MOID&MOOP=add&popupWin=DeptDiv" ;
	showChildPop(urlstr,'DeptDiv');
}


//回填部门选择信息函数
function exeDeptDiv(returnValue){
	var str = returnValue;
	var deptNames = hideNames;
	var deptIds = hideIds;
	if(typeof(str)=="undefined") return ;
	var depts = str.split("#|#") ;
	for(var j=0;j<depts.length;j++){
		var field = depts[j].split("#;#") ;
		if(field!=""){
			var existOption = m(deptNames).options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++)
			{
				if(existOption[i].value==field[0])
				{
					talg = true;
				}
			}
			if(!talg){
				m(deptNames).options.add(new Option(field[1],field[0]));
				m(deptIds).value+=field[0]+",";
			}
		}
	}
	AjaxRequest("/UtilServlet?operation=setFormatScope&reportId="+hideReportId+"&scopeType=deptIds&deptIds="+m(deptIds).value) ;
	alert("$text.get('common.msg.addSuccess')") ;	
	return false;
}


//职员弹出框



function showEmployee(fieldName,fieldNameIds,reportId){
	hideReportId = reportId;
	hideIds = fieldNameIds;
	hideNames = fieldName;
	var urlstr = "/UserFunctionAction.do?operation=22&tableName=tblEmployee&selectName=SelectSMSEmployee"+"&MOID=$MOID&MOOP=query&popupWin=EmployeeDiv&LinkType=@URL:" ;
	showChildPop(urlstr,'EmployeeDiv');
}

function exeEmployeeDiv(returnValue){
	var str = returnValue;
	var fieldName = hideNames;
	var fieldNameIds = hideIds;
	
	if(typeof(str)=="undefined"){
		return false;
	}
	var employees = str.split("#|#") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split("#;#") ;
		if(field!=""){
			var existOption = m(fieldName).options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++)
			{
				if(existOption[i].value==field[0])
				{
					talg = true;
				}
			}
			if(!talg){
				m(fieldName).options.add(new Option(field[1],field[0]));
				m(fieldNameIds).value+=field[0]+",";
			}
		}
	}
	AjaxRequest("/UtilServlet?operation=setFormatScope&reportId="+hideReportId+"&scopeType=userIds&userIds="+(m(fieldNameIds).value.replace(",,",","))) ;
	alert("$text.get('common.msg.addSuccess')") ;
	
}

//用于printFormatSet子类选择部门时调用弹出,使部门弹出框不被样式设计弹出框掩盖



function showChildPop(urlstr,asyncboxId){
	var title = "部门弹出框";
	if(asyncboxId == "EmployeeDiv"){
		title ="职员弹出框";
	}
	asyncbox.open({id:asyncboxId,title:title,url:urlstr,width:750,height:470});
}


function delOpation(fileName,popedomId,formatId){
	var formatName=m(fileName);
	if(formatName.selectedIndex==-1){
		alert("$text.get('oa.lb.common.pleaseChoose.remove')");
		return;
	}
	if(formatName.selectedIndex<0)return false ;
	var value = formatName.options[formatName.selectedIndex].value;
	var appcers = m(popedomId).value;
	appcers = appcers.replace(","+value+",",",");
	appcers = appcers.replace(",,",",");
	m(popedomId).value =appcers
	formatName.options.remove(formatName.selectedIndex);
	if(fileName.indexOf("formatFileName")!=-1){
		AjaxRequest("/UtilServlet?operation=setFormatScope&reportId="+formatId+"&scopeType=userIds&userIds="+appcers) ;
	}else{
		AjaxRequest("/UtilServlet?operation=setFormatScope&reportId="+formatId+"&scopeType=deptIds&deptIds="+appcers);
	}
}

function styleFormat(styleName,newFlag,reportId,reportNumber,SQLFileName){
	try{
		var mimetype = navigator.mimeTypes["application/np-print"];
		if(mimetype){
			if(mimetype.enabledPlugin){ 
				var cb = document.getElementById("plugin");
				cb.format('$!globals.getLocale()','$!IP|$!port',reportId,reportNumber,SQLFileName,styleName,newFlag,"$!JSESSIONID",'$LoginBean.id');
			}
		}else{
			var obj = new ActiveXObject("KoronReportPrint.FormatDesign") ;
	    	obj.URL="$!IP|$!port" ;
	   		obj.StyleFileName = styleName ;
	    	obj.NewFlag = newFlag ;
	    	obj.SQLFileName = SQLFileName ;
	    	obj.ReportName = reportNumber ;
	   		obj.ReportId = reportId ;
	   		obj.Cookie ="$!JSESSIONID" ;
		   	obj.UserId ='$LoginBean.id' ;
	    	obj.Language = "$!globals.getLocale()" ;
			obj.OpenFormat();
		}	
	}catch (e){
		asyncbox.alert("$text.get('com.sure.print.control')<br><br><a href='/common/AIOPrint.exe' target='_blank'>下载控件</a>","信息提示");
	}
}

function filterFormat(id,filterSQL){ 
	  filterSQL = filterSQL.replaceAll("]]","'");
	　asyncbox.open({
	　　　html : '<textarea id="filterSQL" style="width: 99%;height: 180px">'+filterSQL+'</textarea>注意：此为高级功能，请谨慎使用，错误使用会造成无法显示打印样式，此功能为根据表单内容过滤样式，取单据代号为@ValueOfDB:id',
		 title: '请输入过滤SQL',
	　　　width : 400,
	　　　height : 300,
	　　　btnsbar : jQuery.btn.OKCANCEL, 
	　　　callback : function(action,iframe){
	　　　　　if(action == 'ok'){
				jQuery.post("/ReportSetAction.do?filterSQL=true", { sql: $("#filterSQL").val(),id:id },
				function(data){
					alert(data);
					window.location.href=$("#reload",document).attr("href");
				});
	　　　　　}
	　　　}
	　});
}

function sqlSet(flag,fileName,reportNumber,reportId){
	try{
		var mimetype = navigator.mimeTypes["application/np-print"];
		if(mimetype){
			if(mimetype.enabledPlugin){
				var cb = document.getElementById("plugin");
				cb.sql('$!globals.getLocale()',fileName,'$!IP|$!port',reportId,reportNumber,flag,"$!JSESSIONID",'$LoginBean.id');
			}
		}else{
			var obj = new ActiveXObject("KoronReportPrint.SQLDesign") ;
	    	obj.URL="$!IP|$!port" ;
	    	obj.NewFlag = flag ;
	    	obj.FileName = fileName ;
	    	obj.ReportName = reportNumber ;
	   		obj.ReportId = reportId ;
	   		obj.Cookie ="$!JSESSIONID" ;
		   	obj.UserId ='$LoginBean.id' ;
	    	obj.Language = "$!globals.getLocale()" ;
			obj.OpenSQL();	
		}
	}catch (e){
		alert("$text.get('com.sure.print.control')") ;
	}
}

</script>
<style>
.tips_div{width:250px;height:167px;display: none;cursor:default;position:absolute;left:200px;top:50px;border:5px solid #B9E6FF; background-color:#fff;}
.tips_title{height:25px;line-height:25px;vertical-align:middle;background-color:#B9E6FF;padding:3px 0 0 0;}
.left_title{float:left;padding-left:10px;font-family:微软雅黑;font-size:14px;font-weight:bold;color:#000;}
.tips_close{padding:2px 2px 0 0;float:right;}
.tips_content{margin:20px 0 0 10px;text-align:center;float:left;}
.tips_operate{width:100%;margin:20px 0 10px 0;float:left;text-align:center;}
.HeadingButton{margin:0;}
.HeadingButton li{margin:0 5px 0 0;}
</style>
</head>
<body> 
<div class="Heading">
	<div class="HeadingTitle">$text.get("common.lb.print.FormatDeisgn")</div>
	<ul class="HeadingButton">
		<li><span onClick="addFormatStyle();" class="btn btn-small">$text.get("common.lb.add")</span></li> 
		<li><span onClick="javascript:closeAsyn();" class="btn btn-small">$text.get("common.lb.close")</span></li>	
	</ul>
	<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>
</div>
<div id="listRange_id">
		<a id="reload" href="/ReportSetAction.do?operation=84&reportId=$!reportId&isbillType=$!isbillType&tableName=$!tableName&moduleType=$!moduleType" style="display: none;"></a>
		<div class="scroll_function_small_a" id="conter" style="height: 340px;">
			<div class="tips_div" id="addFormat">
				<div class="tips_title">
			     	<div class="left_title">$text.get("common.lb.print.AddDesign")</div>
			     	<div class="tips_close"><a href="javascript:closeFormat(); " class="div_close"><img src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("common.lb.close")" border="0"/></a></div>
		   		</div>
		   		<div class="tips_content">
					$text.get("common.lb.print.FormatName")：<input type="text" id="styleName" name="styleName" style="border:1px solid #1CA4FC; "/>
				</div>
				<div class="tips_content">
					报表名称：<select name = "styleReportNumber" id="styleReportNumber">
						#foreach($rbList in $styleFormat)
						<option value="$rbList.reportNumber">$rbList.ReportName</option>
						#end
							</select>
				</div>
			   	<div class="tips_operate" style="padding-bottom:20px">
			   		<span onClick="submitFormat()" class="btn btn-mini" style="margin-right:20px">$text.get("button.lable.sure")</span>
					<span onClick="closeFormat()" class="btn btn-mini" style="margin-right:20px">$text.get("common.lb.close")</span>
			   	</div>
			</div>
			<div id="formatList">
			<table border="0" width="550" cellpadding="0" cellspacing="0" class="listRange_list_function"  id="tblSort">
			<thead>
				<tr>
					<td width="*" align="center">$text.get("common.lb.print.FormatName")</td>
					<td width="110" align="center">数据源</td>
					<td width="180" align="center">$text.get("common.lb.print.DeptRight")</td>
					<td width="180" align="center">$text.get("common.lb.print.UserRight")</td>
					<td width="60" align="center">$text.get("common.lb.print.Operation")</td>				
				</tr>			
			</thead>
			<tbody>
			#foreach($rbList in $styleFormat)
			#foreach($style in $rbList.reportDetBean.values())
				#if($style.languageType==$globals.getLocale())
				<tr height="100">
					<td>$style.FormatName</td>
					<td>#if($LoginBean.operationMap.get("/ReportSetQueryAction.do").update()) 
						<a href="javascript:sqlSet('OLD','$rbList.SQLFileName','$rbList.ReportNumber','$rbList.id');">$rbList.reportName</a>
						#else $rbList.reportName #end  
						</td>
					<td>
						<input type="hidden" name="popedomDeptIds${rbList.id}$velocityCount" id="popedomDeptIds${rbList.id}$velocityCount" value=",$!style.deptIds"/>
			          	<div class="oa_signDocument1" id="_context">
						<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="showDept('formatDeptName${rbList.id}$velocityCount','popedomDeptIds${rbList.id}$velocityCount','$style.id');"  alt="$text.get("oa.common.adviceDept")" class="search">&nbsp;<a style="cursor:pointer" title="$text.get('oa.select.dept')" onClick="showDept('formatDeptName${rbList.id}$velocityCount','popedomDeptIds${rbList.id}$velocityCount','$style.id');">$text.get("oa.select.dept")</a></div>
							<select name="formatDeptName${rbList.id}$velocityCount" id="formatDeptName${rbList.id}$velocityCount" multiple="multiple">
							#foreach($deptId in $!style.deptIds.split(","))
								#if("$!deptId" !="" && "$!deptId" != ",")
								<option value="$!deptId">$!deptMap.get("$deptId")</option>
								#end
							#end
							</select>
						</div>
						<div class="oa_signDocument2">
							<img onClick="delOpation('formatDeptName${rbList.id}$velocityCount','popedomDeptIds${rbList.id}$velocityCount','$!style.id')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
						</div>
					</td>
					<td>
						<input type="hidden" name="popedomUserIds${rbList.id}$velocityCount" id="popedomUserIds${rbList.id}$velocityCount" value=",$!style.userIds"/>
						<div class="oa_signDocument1" id="_context">
						<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="showEmployee('formatFileName${rbList.id}$velocityCount','popedomUserIds${rbList.id}$velocityCount','$style.id');" alt="$text.get("oa.common.advicePerson")" class="search">&nbsp;<a style="cursor:pointer" title="$text.get('oa.select.personal')" onClick="showEmployee('formatFileName${rbList.id}$velocityCount','popedomUserIds${rbList.id}$velocityCount','$style.id');">$text.get("oa.select.personal")</a></div>
							<select name="formatFileName${rbList.id}$velocityCount" id="formatFileName${rbList.id}$velocityCount" multiple="multiple">
							#foreach($user in $globals.listEmpNameByUserId("$!style.userIds"))
								#if("$!user" !="")
								<option value="$user.id">$user.name</option>
								#end
							#end
							</select>
						</div>
						<div class="oa_signDocument2">
							<img onClick="delOpation('formatFileName${rbList.id}$velocityCount','popedomUserIds${rbList.id}$velocityCount','$!style.id')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
						</div>			
					</td>
					<td align="center">
						<span style="margin-bottom:10px" onClick="styleFormat('$style.FormatFileName','$style.newFlag','$!rbList.id','$!rbList.ReportNumber','$!rbList.SQLFileName')" class="btn btn-danger btn-mini">$text.get("common.lb.print.Design")</span><br>
						<span style="margin-bottom:10px" onClick="deleteFormat('$style.id')" class="btn btn-mini">$text.get("common.lb.print.Delete")</span><br>
						#if("$!isbillType"=="true")
						<span onClick='filterFormat("$!style.id","$!style.filterSql.replaceAll("\'", "]]")")' class="btn btn-mini">过滤</span>
						#end
					</td>
				</tr>
				#end
			#end
			#end
			</tbody>
			</table>
			</div>
		</div>
</div>
</body>
</html>