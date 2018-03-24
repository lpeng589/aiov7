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
putValidateItem("reportName","$text.get("reportSet.lb.reportName")","any","",false,1,100);
putValidateItem("reportNumber","$text.get("reportSet.lb.reportNumber")","any","",false,1,100);
putValidateItem("fixNumberCol","$text.get("common.lb.RivetRow")","int","",true,0,100);
putValidateItem("reportType","报表类型","any","",false,0,100);

function mainmd(){
   	var mlf = document.form.reportName;
	var mhf=document.form.reportName_lan;
	var str  = window.showModalDialog("/common/moreLanguage.jsp?len=50&str="+encodeURI(mlf.value),"","dialogWidth=530px;dialogHeight=250px");
	if(typeof(str)=="undefined")return ;
	mlf.value=str;
	var strs=str.split(";");
   	for(var i=0;i<strs.length;i++){
	   var lanstr=strs[i].split(":");
		if(lanstr[0]=='$globals.getLocale()'){
		    mhf.value=lanstr[1];break;
		}
	}	   
}

function beforSubmit(form){   
	if($("#reportName_lan").val() != ""){
		if($("#reportName").val()==""){
			$("#reportName").val("zh_CN:"+$("#reportName_lan").val()+";");
		}else if($("#reportName").val().indexOf("zh_CN:")==-1){
			$("#reportName").val($("#reportName").val()+";zh_CN:"+$("#reportName_lan").val()+";");
		}else{
			var pos = $("#reportName").val().indexOf("zh_CN:")+"zh_CN:".length;
			$("#reportName").val($("#reportName").val().substring(0,pos)+$("#reportName_lan").val()+$("#reportName").val().substring($("#reportName").val().indexOf(";",pos)));
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
	
	document.getElementsByName("showTotalStat")[0].disabled=false;
	
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
	document.getElementsByName("showTotalStat")[0].disabled=false;
	document.getElementById("auditPrintDiv").style.display = "none" ;
	if(form.reportType.value=="TEMPTABLE" || form.reportType.value=="PROCLIST") 
	{		
		document.getElementById("tempContent").style.display = "block" ;
		if(form.reportType.value=="PROCLIST")
		{
			document.getElementsByName("showTotalStat").value="0";
			document.getElementsByName("showTotalStat")[0].disabled=true;
		}
	}else
	{
		document.getElementById("tempContent").style.display = "none" ;
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
<body onLoad="showtable('tblSort');showStatus();cyh.lableAlign(); ">

<form  method="post" scope="request" name="form" action="/ReportSetAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
#if($globals.getMOperation().add())
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
#end
<div class="Heading">
	<div class="HeadingTitle">$text.get("reportSet.lb.title")</div>
<ul class="HeadingButton">
<li>
<button type = "submit" class="hBtns">$text.get("mrp.lb.save")</button>
</li>
#if($globals.getMOperation().query())
		<li><button type = "button" onClick="location.href='/ReportSetQueryAction.do?winCurIndex=$!winCurIndex'" class="hBtns">$text.get("common.lb.back")</button></li>
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
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.reportNumber")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" ><input name="reportNumber" type="text" value="" /></div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.reportName")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<input id="reportName_lan" name="reportName_lan" type="text" value="" >
						<input id="reportName" name="reportName" value="" type="hidden"/>
					</div>
				</li>	
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("common.lb.LastLevelRrportNumber")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<input name="endClassNumber" type="text" value="">
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">链接父报表</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<input name="parentLinkReport" type="text" value="">
					</div>
				</li>
				<li>
				
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("com.report.module.type")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<input name="moduleType" type="text" value="">
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("common.lb.RivetRow")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<input name="fixNumberCol" type="text" value="">
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.crossColNum")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<input name="crossColNum" type="text" value="">
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.pageOfCount")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<input name="pageSize" type="text" value="">
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.formatFile")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<input name="tempFormat" type="text">
					</div>
					<img src="/$globals.getStylePath()/images/add.gif" class="search" onClick="addOpation()">
				<li>
					<div class="swa_c1"></div>
					<div class="swa_c2">
						<select name="formatFileName"  multiple="multiple" style="height:80px;">
						</select>
					</div>
					<img src="/$globals.getStylePath()/images/del.gif" class="search" onClick="delOpation()">
				</li>
			</ul>
			<ul class="listRange_jag" style="width:350px;">
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.reportType")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="reportType"  onChange="changeType()">
						<option value="" >$text.get("reportSet.lb.selectType")</option>
						#foreach($erow in $globals.getEnumerationItems("ReportType"))				 
							<option value="$erow.value" >$erow.name</option>
						#end
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test" id="tempContentTitle">$text.get("reportSet.lb.billTable")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" id="tempContentIn"><input name="billTable" type="text" value="">
					</div>
				</li>
				<li id="tempContent" style="display: none;">
					<div class="swa_c1"><div class="d_box"><div class="d_test" id="tempContentTitle">$text.get("reportSet.lb.procName")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2" id="tempContentIn"><input name="procName" type="text" value="">
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("com.lb.defaultNoshow")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="defaultNoshow">
							<option value="0">$text.get("currency.lb.noChecked")</option>
							<option value="1">$text.get("currency.lb.isChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("common.lb.DefaultPopupQueryTerm")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="showCondition">
							<option value="0">$text.get("currency.lb.noChecked")</option>
							<option value="1">$text.get("currency.lb.isChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("com.lb.colTitleSort")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="colTitleSort">
							<option value="1">$text.get("currency.lb.isChecked")</option>
							<option value="0">$text.get("currency.lb.noChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.allPage")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="showTotalPage">
							<option value="0">$text.get("currency.lb.noChecked")</option>
							<option value="1">$text.get("currency.lb.isChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.pageCount")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="showTotalStat">
							<option value="0">$text.get("currency.lb.noChecked")</option>
							<option value="1">$text.get("currency.lb.isChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">列 表 数 据 多 选</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="listType">
							<option value="1" selected="selected">$text.get("currency.lb.isChecked")</option>
							<option value="0">$text.get("currency.lb.noChecked")</option>
						</select>
					</div>
				</li>
				<li id="auditPrintDiv">
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.ApprovedAfterPrint")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="auditPrint">
							<option value="1">$text.get("currency.lb.isChecked")</option>
							<option value="0">$text.get("currency.lb.noChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.showHead")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="showHead">
							<option value="1">$text.get("currency.lb.isChecked")</option>
							<option value="0">$text.get("currency.lb.noChecked")</option>
						</select>
					</div>
				</li>
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("reportSet.lb.showRowNumber")</div></div><div class="d_mh">：</div></div>
					<div class="swa_c2">
						<select name="showRowNumber">
							<option value="1">$text.get("currency.lb.isChecked")</option>
							<option value="0">$text.get("currency.lb.noChecked")</option>
						</select>
					</div>
				</li>
			</ul>
			</div> 
		<div style="width:100%;display: inline-block;">
		<div style="padding-left:125px;text-align: left;">
		<span style="padding:5px 0 5px 0px;display: inline-block;">扩展按扭</span><br/>
		<textarea name="extendsBut" style="width:570px;height:60px"></textarea>
		</div>
		</div>
	</div>
	
</div>
</div>
</form>
</body>
</html>
