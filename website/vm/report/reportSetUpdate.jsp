
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("reportSet.lb.title")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript">
putValidateItem("reportNumber","$text.get("reportSet.lb.reportNumber")","any","",false,1,100);
putValidateItem("fixNumberCol","$text.get("common.lb.RivetRow")","int","",true,0,100);
putValidateItem("pageSize","$text.get("reportSet.lb.pageOfCount")","int","",true,-1,1000);


function beforSubmit(form){   
	if($("#reportName_lan").val() != ""){
		if($("#reportName").val()==""){
			$("#reportName").val("zh_CN:"+$("#reportName_lan").val()+";");
		}else if($("#reportName").val().indexOf("zh_CN:")==-1){
			$("#reportName").val($("#reportName").val()+";zh_CN:"+$("#reportName_lan").val()+";");
		}else{
			var pos = $("#reportName").val().indexOf("zh_CN:")+"zh_CN:".length;
			$("#reportName").val($("#reportName").val().substring(0,pos)+$("#reportName_lan").val()+($("#reportName").val().substring($("#reportName").val().indexOf(";",pos))));
		}
	}
	if($("#reportName_lan").value == "" || $("#reportName_lan").value == "en:;zh_CN:;zh_TW:;"){
		alert("$text.get("report.lb.NoReportFormName")");
		return false;
	}  
	
	var formatNames=form.formatFileName.options;	
	for(var i=0;i<formatNames.length;i++)
	{
		formatNames.item(i).selected=true;
	}
	if(!validate(form))return false;
	disableForm(form);
	return true;
}	
function addOpation()
{
	var format=form.tempFormat.value;
	var formatName=form.formatFileName;
	
	var oOption = document.createElement("OPTION");
	formatName.options.add(oOption);
	oOption.innerText = format;
	oOption.Value = format;	
}
function delOpation()
{
	 var formatName=form.formatFileName;
	 formatName.options.remove(formatName.selectedIndex);
}
function changeType()
{
	if(form.reportType.value=="BILL")
	{
		document.getElementById("tempContent").innerHTML="<span>$text.get("reportSet.lb.billTable")：</span><input name=\"billTable\""+
		" type=\"text\" value=\"\">";
	}else if(form.reportType.value=="TEMPTABLE" || form.reportType.value=="PROCLIST")
	{
		document.getElementById("tempContent").innerHTML="<span>$text.get("reportSet.lb.procName")：</span><input name=\"procName\""+
		" type=\"text\" value=\"\">";
	}else
	{
		document.getElementById("tempContent").innerHTML="";
	}
}

function mainmd(){
   	var mlf = document.form.reportName;
	var mhf=document.form.reportName_lan;
	var str  = window.showModalDialog("/common/moreLanguage.jsp?len=50&str="+encodeURI(mlf.value),"","dialogWidth=530px;dialogHeight=250px");
	if(typeof(str)=="undefined") return ;
	mlf.value=str;
	var strs=str.split(";");
    for(var i=0;i<strs.length;i++){
    	var lanstr=strs[i].split(":");
 	  	if(lanstr[0]=='$globals.getLocale()'){
			mhf.value=lanstr[1];break;
	  	}
   	}	   
}

function getLocale(reportName){
   var strs=reportName.split(";");
      for(var i=0;i<strs.length;i++){
	     var lanstr=strs[i].split(":");
		 if(lanstr[0]=='$globals.getLocale()'){
		    document.form.reportName_lan.value=lanstr[1];break;
		 }
	  }
}
</script>
<style type="text/css" />
.listrange_jag li{width:250px;}
.listrange_jag li select{width:130px;border:1px solid #ccc;border-radius:4px;margin:0;height:21px;}
.listrange_jag li input{padding:0;}
.listrange_jag li .swa_c2{display:inline-block;padding:0 0 0 2px;float:left;}
.search{float:left;margin:7px 0 0 3px;}
</style>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body onLoad="getLocale('$result.ReportName');cyh.lableAlign();">

<form  method="post" name="form" action="/ReportSetAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="id" value="$result.id">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingTitle">$text.get("reportSet.lb.title")</div>
	<ul class="HeadingButton">
		#if($!oper&&$!oper.equals("copy"))
		<li><button type = "submit" onClick="form.operation.value='$globals.getOP("OP_ADD")'" class="hBtns">$text.get("common.lb.copySave")</button></li>
		#else
		<li><button type = "submit" class="hBtns">$text.get("mrp.lb.save")</button></li>
		#end
		#if($globals.getMOperation().query())
	<li><button type = "button" onClick="location.href='/ReportSetQueryAction.do?winCurIndex=$!winCurIndex&reportNames=$globals.encode($!reportNames)'" class="hBtns">$text.get("common.lb.back")</button></li>
	#end

	</ul>
</div>
<div class="listRange_frame" align="center">
	<div class="scroll_function_small_a" id="conter">
	<script type="text/javascript">
		var oDiv=document.getElementById("conter");
		var sHeight=document.body.clientHeight-38;
		oDiv.style.height=sHeight+"px";
	</script>
	<div class="listRange_div_jag_report" style="height:500px;">
		<div class="listRange_div_jag_div"></div>
		<div>
			<ul class="listRange_jag" style="width:350px;margin:0 0 0 120px;">
				#if($!oper)
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.reportNumber")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input name="reportNumber" type="text" value="$!result.ReportNumber">
					</div>
				</li>
				#else
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.reportNumber")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input name="reportNumber" readonly="readonly" type="text" value="$!result.ReportNumber">
					</div>
				</li>
				#end
				
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.reportName")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input id="reportName_lan" name="reportName_lan" type="text" value="$!result.ReportName" aaondblClick="mainmd();">
						<input id="reportName" name="reportName" value="$!result.ReportName" type="hidden"/>
					</div>
				</li>
				
				#if($result.ReportType!="BILL")
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("common.lb.LastLevelRrportNumber")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input name="endClassNumber" type="text" value="$!result.endClassNumber">
					</div>
				</li>
				#end
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">链接父报表</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<input name="parentLinkReport" type="text" value="$!result.parentLinkReport">
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("com.report.module.type")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input name="moduleType" type="text" value="$!result.moduleType">
					</div>
				</li>
				
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("common.lb.RivetRow")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input name="fixNumberCol" type="text" value="$!result.fixNumberCol">
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.crossColNum")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input name="crossColNum" type="text" value="$!result.crossColNum">
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.pageOfCount")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input name="pageSize" #if($!result.pageSize==-1) readOnly=true #end type="text" value="$!result.pageSize">
					</div>
				</li>	
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.formatFile")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input name="tempFormat" type="text">
					</div>
					<img src="/$globals.getStylePath()/images/add.gif" class="search" onClick="addOpation()">
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test"></div></div></div>
					<div class="swa_c2" >
						<select name="formatFileName"  multiple="multiple" size="4" style="height:80px;">
							#foreach($reportDet in $result.reportDetBean.values())
							  <option value="$reportDet.FormatName">$reportDet.FormatName</option>
							#end	
						</select>
					</div>
					<img src="/$globals.getStylePath()/images/del.gif" style="margin:0 0 0 3px;" class="search" onClick="delOpation();">
				</li>
			</ul>
			<ul class="listRange_jag" style="width:350px;">
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.reportType")</div></div><div class="d_mh">：</div></div>$!reportTypeName
					<div class="swa_c2" >
					#if($!oper)<select name="reportType" onChange="changeType()">#end
						#set($reportTypeName="")
						#foreach($erow in $globals.getEnumerationItems("ReportType"))
						 	#if($erow.value==$result.ReportType)
						 		#set($reportTypeName=$erow.name)
						 	#end
							#if($!oper)<option value="$erow.value" #if($erow.value==$result.ReportType)  selected="selected" #end >$erow.name</option>#end
						   
						#end 
					#if($!oper)</select>#end
					#if(!$!oper)<input type="text" name="reportTypeName" value="$!reportTypeName" readonly="readonly">
					<input type="hidden" name="reportType" value="$result.ReportType"/>#end
					</div>
				</li>					
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.billTable")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input name="billTable" type="text" value="$!result.BillTable">
					</div>
				</li>
				#if($result.ReportType=="TEMPTABLE" || $result.ReportType=="PROCLIST")
				<li id="tempContent">
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.procName")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<input name="procName" type="text" value="$!result.ProcName">
					</div>
				</li>
				#end	
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("com.lb.defaultNoshow")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<select name="defaultNoshow">
							<option value="1" #if($!result.defaultNoshow==1)selected="true" #end>$text.get("currency.lb.isChecked")</option>
							<option value="0" #if($!result.defaultNoshow==0)selected="true" #end>$text.get("currency.lb.noChecked")</option>
						</select>
					</div>
				</li>	
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("common.lb.DefaultPopupQueryTerm")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="showCondition">
							<option value="0" #if($!result.showCondition==0)selected="true" #end>$text.get("currency.lb.noChecked")</option>
							<option value="1" #if($!result.showCondition==1)selected="true" #end>$text.get("currency.lb.isChecked")</option>
						</select>
					<div class="swa_c2" >
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("com.lb.colTitleSort")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="colTitleSort">
							<option value="1" #if($!result.colTitleSort==1)selected="true" #end>$text.get("currency.lb.isChecked")</option>
							<option value="0" #if($!result.colTitleSort==0)selected="true" #end>$text.get("currency.lb.noChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.allPage")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<select name="showTotalPage">
							<option value="0" #if($!result.showTotalPage==0)selected="true" #end>$text.get("currency.lb.noChecked")</option>
							<option value="1" #if($!result.showTotalPage==1)selected="true" #end>$text.get("currency.lb.isChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.pageCount")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<select name="showTotalStat" #if($result.ReportType=="PROCLIST")disabled="true"#end>
							<option value="0" #if($!result.showTotalStat==0)selected="true" #end>$text.get("currency.lb.noChecked")</option>
							<option value="1" #if($!result.showTotalStat==1)selected="true" #end>$text.get("currency.lb.isChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">列 表 数 据 多 选</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<select name="listType">
							<option value="1" #if($!result.listType==1)selected="true" #end>$text.get("currency.lb.isChecked")</option>
							<option value="0" #if($!result.listType==0)selected="true" #end>$text.get("currency.lb.noChecked")</option>
						</select>
					</div>
				</li> 
				#if($result.ReportType=="BILL")
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.ApprovedAfterPrint")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<select name="auditPrint">
							<option value="0" #if($!result.auditPrint==0)selected="true" #end>$text.get("currency.lb.noChecked")</option>
							<option value="1" #if($!result.auditPrint==1)selected="true" #end>$text.get("currency.lb.isChecked")</option>
						</select>
					</div>
				</li>
				#end
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.showHead")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<select name="showHead">
							<option value="0" #if($!result.showHead==0)selected="true" #end>$text.get("currency.lb.noChecked")</option>
							<option value="1" #if($!result.showHead==1)selected="true" #end>$text.get("currency.lb.isChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.showRowNumber")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
						<select name="showRowNumber">
							<option value="0" #if($!result.showRowNumber==0)selected="true" #end>$text.get("currency.lb.noChecked")</option>
							<option value="1" #if($!result.showRowNumber==1)selected="true" #end>$text.get("currency.lb.isChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.statusId")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" >
					 <select name="statusId">
					  #foreach($erow in $globals.getEnumerationItems("IsAble"))
						 <option value="$erow.value" #if($erow.value==$result.statusId) selected="selected" #end>$erow.name</option>
					  #end
				     </select>
				    </div>
				</li>
			</ul>
			</div>
			<div style="width:100%;display: inline-block;">
			<div style="padding-left:125px;text-align: left;">
			<span style="padding:5px 0 5px 0px;display: inline-block;">扩展按扭</span><br/>
			<textarea name="extendsBut" style="width:570px;height:60px">$!result.extendsBut</textarea>
			</div>
			</div>
	</div>
</div>
</form>
</body>
</html>
