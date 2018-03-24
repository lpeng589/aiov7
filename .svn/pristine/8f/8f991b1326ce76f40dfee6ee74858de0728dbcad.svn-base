<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/workflow.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">

function closeForm(){
	parent.jQuery.close("TablemgtDiv");

}
function goAction(tit){
	if(tit == "baseInfo"){
		$("#data_info_id",document).show();
		$("#data_list_id",document).hide();
		
		$("#titleInfoId",document).text("基本信息");		
	}else{
		$("#data_info_id",document).hide();
		$("#data_list_id",document).show();		
		$("#titleInfoId",document).text("字段信息");		
	}
}
function addRowline(){
	onetr = $("#FieldInfoId tbody").find("tr:eq(0)").clone();	
	$("input",onetr).val("");
	$(onetr).removeClass("focusRow");
	$("input",onetr).removeAttr("readonly");
	$(onetr).find("td:last").empty().
			append('<input type="text" class="changeInput" value="" name="moreInputId"  onchange="changeMoreInput(this)" />');
	$(onetr).find("select").val("0");
	$("#FieldInfoId").append(onetr);
	
	rowNo = 0;
	$("#FieldInfoId tbody").find("tr").each(function (){
		rowNo ++;
		$(this).find("td:eq(0)").text(rowNo);
	});
}
function delline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请先选择需要删除的字段");
		return;
	}
	if(curRow != null){
		$(curRow).remove();
		curRow = null;
	}
}
//上移单元格  
function upline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请选择需要上移的字段");
		return;
	}
	$(curRow).insertBefore($(curRow).prev());
}
//下移单元格  
function downline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请选择需要下移的字段");
		return;
	}
	$(curRow).insertAfter($(curRow).next());
}

function setRowLine(){
	$("#isStat").val("0");
	if($("#isStatId").attr("checked")=="checked"){
		$("input[name=isStat]:eq("+curRowNo+")").val("1");
	}
	$("#isUnique").val("0");
	if($("#isUniqueId").attr("checked")=="checked"){
		$("input[name=isUnique]:eq("+curRowNo+")").val("1");
	}
	$("input[name=width]:eq("+curRowNo+")").val($("#widthId").val());	
	$("input[name=calculate]:eq("+curRowNo+")").val($("#calculateId").val());
	$("input[name=fieldSysType]:eq("+curRowNo+")").val($("#fieldSysTypeId").val());
	$("input[name=fieldIdentityStr]:eq("+curRowNo+")").val($("#fieldIdentityStrId").val());
	$("input[name=copyType]:eq("+curRowNo+")").val($("#copyTypeId").val());
	$("input[name=logicValidate]:eq("+curRowNo+")").val($("#logicValidateId").val());
	$("input[name=groupName]:eq("+curRowNo+")").val($("#groupNameId").val());
	$("input[name=inputTypeOld]:eq("+curRowNo+")").val($("#inputTypeOldId").val());

	$("#isCopy").val("0");
	if($("#isCopyId").attr("checked")=="checked"){
		$("input[name=isCopy]:eq("+curRowNo+")").val("1");
	}
	$("#isReAudit").val("0");
	if($("#isReAuditId").attr("checked")=="checked"){
		$("input[name=isReAudit]:eq("+curRowNo+")").val("1");
	}
	$("#isLog").val("0");
	if($("#isLogId").attr("checked")=="checked"){
		$("input[name=isLog]:eq("+curRowNo+")").val("1");
	}
}

curRow = null;
curRowNo = 0;
function focusRow(obj){
	setRowLine(); //更新行高级属性数据
	curRow = obj;
	curRowNo = jQuery.inArray(obj,$("#FieldInfoId tbody").find("tr"));
	$(".focusRow").removeClass("focusRow");
	$(curRow).addClass("focusRow");
	if(obj == $("#FieldInfoId tbody").find("tr:last")[0]){
		addRowline();
	}
	//显示详情
	
	$("#isStatId").removeAttr("checked","checked");
	if($("input[name=isStat]:eq("+curRowNo+")").val()=="1"){
		$("#isStatId").attr("checked","true");
	}
	$("#isUniqueId").removeAttr("checked","checked");
	if($("input[name=isUnique]:eq("+curRowNo+")").val()=="1"){
		$("#isUniqueId").attr("checked","true");
	}
	$("#widthId").val($("input[name=width]:eq("+curRowNo+")").val());
	$("#calculateId").val($("input[name=calculate]:eq("+curRowNo+")").val());
	$("#fieldSysTypeId").val($("input[name=fieldSysType]:eq("+curRowNo+")").val());
	$("#fieldIdentityStrId").val($("input[name=fieldIdentityStr]:eq("+curRowNo+")").val());
	$("#copyTypeId").val($("input[name=copyType]:eq("+curRowNo+")").val());
	$("#logicValidateId").val($("input[name=logicValidate]:eq("+curRowNo+")").val());
	$("#groupNameId").val($("input[name=groupName]:eq("+curRowNo+")").val());
	$("#inputTypeOldId").val($("input[name=inputTypeOld]:eq("+curRowNo+")").val());

	$("#isCopyId").removeAttr("checked","checked");
	if($("input[name=isCopy]:eq("+curRowNo+")").val()=="1"){
		$("#isCopyId").attr("checked","true");
	}
	$("#isReAuditId").removeAttr("checked","checked");
	if($("input[name=isReAudit]:eq("+curRowNo+")").val()=="1"){
		$("#isReAuditId").attr("checked","true");
	}
	$("#isLogId").removeAttr("checked","checked");
	if($("input[name=isLog]:eq("+curRowNo+")").val()=="1"){
		$("#isLogId").attr("checked","true");
	}
}

function changeInput(obj){
	if($(obj).val() == "1"){//枚举
		$("#FieldInfoId tbody").find("tr:eq("+curRowNo+")").find("td:last").empty().
			append('<select name="moreInputId" enumerName="'+$("input[name=moreInput]:eq("+curRowNo+")").val()+'"  onchange="changeEnumInput(this)" ><option value=""></option><option value="updateEnum">修改选项</option><option value="newEnum">新建选项</option><option value="selectEnum">选择选项</option></select>');
	}else if($(obj).val() == "2"){//弹出窗
		popSel = $("input[name=moreInput]:eq("+curRowNo+")").val().split(":");
		popupName = "";
		popupDesc = "";
		if(popSel.length > 0){
			popupName = popSel[0];
			popupDesc = popSel[0];			
		}
		if(popSel.length > 1){
			popupDesc = popSel[1];			
		}
		$("#FieldInfoId tbody").find("tr:eq("+curRowNo+")").find("td:last").empty().
			append('<input type="text" class="changeInput" value="'+popupDesc+'" name="moreInputId" style="width:130px" readonly /><b class="tstBtn" onclick="clickPopUp(this)"></b>');
	}else{
		$("#FieldInfoId tbody").find("tr:eq("+curRowNo+")").find("td:last").empty().
			append('<input type="text" class="changeInput" value="'+$("input[name=moreInput]:eq("+curRowNo+")").val()+'" name="moreInputId" onchange="changeMoreInput(this)" />');
	}
}

function clickPopUp(obj){ 
	popSel = $("input[name=moreInput]:eq("+curRowNo+")").val().split(":");
	popupName = "";
	popupDesc = "";
	if(popSel.length > 0){
		popupName = popSel[0];
		popupDesc = popSel[0];			
	}
	if(popSel.length > 1){
		popupDesc = popSel[1];			
	}
	urls = '/CustomAction.do?type=popupSet&popupValue='+popupName;
	asyncbox.open({
		id: 'PopWinow',
		title : "弹出窗管理",
		url :urls,
		width : 520,
		height : 300,
		btnsbar : [{text    : '修改',action  : 'updatePop'},
					{text    : '新建',action  : 'addPop'},
					{text    : '选择',action  : 'selectPop'},
					{text    : '取消',action  : 'cancel'}],
		callback : function(action,opener){
			if(action == 'selectPop'){
				jQuery.close('PopWinow');
				queryPopUp();
			}else if(action == 'updatePop'){
				jQuery.close('PopWinow');
				popSel = $("input[name=moreInput]:eq("+curRowNo+")").val().split(":");
				if(popSel==""){
					alert("当前未选择弹出窗，不能修改");
				}else{
					popupName = "";
					popupDesc = "";
					if(popSel.length > 0){
						popupName = popSel[0];
						popupDesc = popSel[0];			
					}
					if(popSel.length > 1){
						popupDesc = popSel[1];			
					}
					updatePopUp(popupName);
				}
			}else if(action == 'addPop'){
				jQuery.close('PopWinow');
				updatePopUp();
			}else{
				jQuery.close('PopWinow');
			}
			return false;
		}
	});	
}

function callBackPopup(name,desc){
	$("input[name=moreInput]:eq("+curRowNo+")").val(name+":"+desc);
	$(document.getElementsByName("moreInputId")[curRowNo]).attr('value',desc);
	jQuery.close('PopWinow');
	jQuery.close('queryPopWinow');
	jQuery.close("popupUpdateWin");
}

function queryPopUp(){
	urls = '/CustomAction.do?type=popupQuery';
	asyncbox.open({
		id: 'queryPopWinow',
		title : "弹出窗管理",
		url :urls,
		width : 930,
		height : 400,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			if(action == 'ok'){
				callBackPopup($(opener.curRow).find("td:eq(1)").text().trim(),$(opener.curRow).find("td:eq(2)").text().trim());
			}else{
				jQuery.close('queryPopWinow');
			}
			return false;
		}
	});	
}
function updatePopUp(popupName){
	vtitle = "弹出窗修改";
	if(popupName ==""){
		vtitle = "弹出窗新建";
	}
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	urls = '/CustomAction.do?type=popupUpdate&popupName='+popupName+"&popupWin=popupUpdateWin";
	openPop('popupUpdateWin',vtitle,urls,width,height,true,true);
}

function changeMoreInput(obj){
	$("input[name=moreInput]:eq("+curRowNo+")").val($(obj).val());
}

//存放隐藏值，用于回填
var hideObj;
var hideGroupFlag;
var hideEnumerName; 
function UpdEnum(obj){
	asyncbox.open({
		id: 'EnumWinow',
		title : "选项数据管理",
		url :'/EnumerationAction.do?operation=7&type=crm&enumerName='+$(obj).attr("enumerName"),
		width : 620,
		height : 400,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			if(action == 'ok'){
				opener.jQuery("#crmPopFlag").val("true");//参数crmPopFlag设置为true,表示从CRM模板设置修改选项数据
				//赋值给隐藏参数
				hideObj = obj;
				hideEnumerName = $(obj).attr("enumerName");
				$("input[name=moreInput]:eq("+curRowNo+")").val(hideEnumerName);
				opener.beforSubmit(opener.document.form);
			}else{
				$(obj).children().first().attr("selected","selected");
				jQuery.close('EnumWinow');
			}
			return false;
		}
	});
}
function NewEnum(obj){
	asyncbox.open({
		id: 'EnumWinow',
		title : "选项数据管理",
		url :'/EnumerationAction.do?operation=6&type=crm',
		width : 620,
		height : 400,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			if(action == 'ok'){
				opener.jQuery("#crmPopFlag").val("true");//参数crmPopFlag设置为true,表示从CRM模板设置修改选项数据
				//赋值给隐藏参数
				hideObj = obj;
				hideEnumerName = opener.jQuery("#enumName").val();
				$(obj).attr("enumerName",hideEnumerName);
				$("input[name=moreInput]:eq("+curRowNo+")").val(hideEnumerName);
				opener.beforSubmit(opener.document.form);
			}else{
				$(obj).children().first().attr("selected","selected");
				jQuery.close('EnumWinow');
			}
			return false;
		}
	});
}

function SearchEnum(obj){
	hideObj = obj;
	asyncbox.open({
		id: 'EnumWinow',
		title : "选项数据管理",
		url :'/EnumerationQueryAction.do?type=crm',
		width : 620,
		height : 400,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
			$(obj).children().first().attr("selected","selected");
			jQuery.close('EnumWinow');
			if(action == 'ok'){				
				
				$(obj).children().first().attr("selected","selected");		
				hideEnumerName = opener.jQuery('input:radio[name="keyId"]:checked').val();		
				$(obj).attr("enumerName",hideEnumerName);
				$("input[name=moreInput]:eq("+curRowNo+")").val(hideEnumerName);
				dealAsyn();
				jQuery.close('EnumWinow');
			}else{
				$(obj).children().first().attr("selected","selected");
				jQuery.close('EnumWinow');
			}
			return false;
		}
	});
}

function seachAsyn(enumerName){
	hideEnumerName = enumerName;
	$(hideObj).attr("enumerName",hideEnumerName);
	$("input[name=moreInput]:eq("+curRowNo+")").val(hideEnumerName);
	dealAsyn();
	jQuery.close('EnumWinow');
}



function dealAsyn(){
	var obj = hideObj;
	var enumerName = hideEnumerName;
	jQuery.ajax({
	   type: "POST",
	   url: "/ClientSettingAction.do?operation=4&queryType=reloadEnum&enumerName="+enumerName,
	   success: function(msg){
	 	       var str = "";
			   str +=msg;
			   str +='<option value="---">-------------------</option><option value="updateEnum">修改选项</option><option value="newEnum">新建选项</option><option value="selectEnum">选择选项</option>';
			   $(obj).children().first().attr("selected","selected");
			   $(obj).children().remove()
			   $(obj).append(str);			
	   }
	});
	jQuery.close('EnumWinow');	
}


//选项数据回填方法
function filllanguage(datas){
	jQuery.opener('EnumWinow').filllanguage(datas);
}
 
function changeEnumInput(obj){
	if($(obj).val()=="newEnum"){
		NewEnum(obj);
	}else if($(obj).val()=="updateEnum"){
		if($("input[name=moreInput]:eq("+curRowNo+")").val() == ""){
			alert("请先选择或新建选项数据");
			$(obj).children().first().attr("selected","selected");
		}else{
			UpdEnum(obj);
		}
	}else if($(obj).val()=="selectEnum"){
		SearchEnum(obj);
	}else if($(obj).val()=="---"){
	}
}
//初图显示关联表选择
function changeTabView(theit){
     if(theit.checked){
	    oppoDiv.style.display='block';
	 }else{
	    oppoDiv.style.display='none';
	 }
}
function mainmd(){
   	var mlf = document.form.tableDisplayDis;
	var mhf = document.form.tableDisplay;
	var url = "/common/moreLanguage.jsp?popupWin=MainLanguage&len=200&str="+encodeURI(mhf.value) ;
	asyncbox.open({id:'MainLanguage',title:'多语言',url:url,width:530,height:300});    
}

function fillMainLanguage(str){
	var mlf = document.form.tableDisplayDis;
	var mhf = document.form.tableDisplay;
	var strs=str.split(";");
    for(var i=0;i<strs.length;i++){
	    var lanstr=strs[i].split(":");
		if(lanstr[0]=='$globals.getLocale()'){
		    mlf.value=lanstr[1];break;
		}
	}
	mhf.value = str;  
}

</script>
<style type="text/css">
.f-icon16{background-image:url(/style/v7/images/function_icon22.png);background-repeat:no-repeat;}
.detbtBar{float:left;margin: 5px 0px 3px 10px;/*border:1px solid #c2c2c2; */border-bottom: none;} 
.detbtBar>li{width:22px;height:22px;cursor:pointer;margin:1px 2px 2px;float:left}
#b_addline{background-position:0 -66px;}
#b_addline:hover{background-position:-22px -66px;}
#b_delline{background-position:0 -88px;}
#b_delline:hover{background-position:-22px -88px;}
#b_upline{background-position:0 -153px;}
#b_upline:hover{background-position:-22px -153px;}
#b_downline{background-position:0 -175px;}
#b_downline:hover{background-position:-22px -175px;}
#FieldInfoId input{background:none;border:0px;width: 100%;height: 100%;}
#FieldInfoId select{background:none;border:0px;width: 100%;height: 100%;}
#FieldInfoIdTitle thead td{border-right:1px solid #999;white-space: nowrap;overflow: hidden;}
#FieldInfoId tbody td{border-right:1px solid #999;border-bottom:1px solid #999;position: relative;}

.data_list .dataListUL{margin:0px 5px 0px 5px;line-height:33px;border: 1px solid #999;min-height:67px}
.data_list .dataListUL li{ border-bottom: 1px solid #999;  }
.data_list .dataListUL .titleLi{ background:url(/style1/images/workflow/data_list_head_bg.gif); height:33px;padding-left:10px }
.data_list .dataListUL input,.data_list .dataListUL select{width: 117px;margin-left: 5px;}
.data_list .dataListUL label{width: 48px;margin-left: 5px;display: inline-block;white-space: nowrap;}
.data_list table{width:780px;}
.tstBtn{background-image: url(/style/images/client/icon16.png);background-position: -32px -16px;width: 16px;height: 16px;position: absolute;cursor: pointer;right: 1px;top: 8px;}
.mainUl{margin:0px 5px 35px 5px;line-height:33px;}
.mainUl li{float:left;width:300px   }
.mainUl li span{ width:100px ;text-align:right;  display: inline-block;margin:0px 5px 0px 5px }
.mainUl li input{ width:160px ;padding:0px;margin:0px }
.mainUl li select{ width:162px ; padding:0px;margin:0px}


.focusRow{background-color:#9AF850;}
</style>
</head>
<body >
	<table cellpadding="10px;" cellspacing="0" border="0" class="frame" style="margin-top:4px;">
		<tr>
			<td class="leftMenu" style="padding-top: 14px;width:114px"  valign="top">
				<div class="TopTitle" ><span><img src="/style1/images/workflow/ti_001.gif" /></span><span>表单添加</span></div>
				<div class="LeftBorder" style="width:112px">
				<div id="cc" style=" width:100%; overflow:hidden;overflow-y: auto;">
				<script type="text/javascript">
				var oDiv=document.getElementById("cc");
				var sHeight=document.documentElement.clientHeight-80;
				oDiv.style.height=sHeight+"px";
				</script>
					<ul class="LeftMenu_list">	
						<li onclick="goAction('baseInfo');" style="cursor: pointer;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>基本信息</b></li>
						<li onclick="goAction('fieldInfo');" style="cursor: pointer;border-top: 0px;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>字段信息</b></li>		 
						
					</ul>
				</div>	
				</div>			
			</td>
			<td  style="padding-top: 0px;" valign="top">
		<form method="post" scope="request" name="form" action="/ClientSettingAction.do?operation=2&updType=fieldsMtain" class="mytable" >
		<input type="hidden" name="tableType" id="tableType" value="$!tableType"/>
		<!-- 分支机构共享，暂未用 -->
		<input type="hidden" name="isSunCmpShare" id="isSunCmpShare" value="$!result.isSunCmpShare"/>
		<!-- 是否需要复制，暂未发现使用 -->
		<input type="hidden" name="needsCopy" id="needsCopy" value="$!result.needsCopy"/>
		<!-- 支持提醒，暂未发现使用 -->
		<input type="hidden" name="wakeUp" id="wakeUp" value="$!result.wakeUp"/>
		<!-- 单据支持上下篇，应该已经失效 -->
		<input type="hidden" name="hasNext" id="hasNext" value="$!result.hasNext"/>
		<!-- 关联表，据说是用来做权限用的，现在已失效 -->
		<input type="hidden" name="relationTable" value="$!result.relationTable">
		<!-- 主表 -->
		<input type="hidden" name="perantTableName" value="$!result.perantTableName">
		
		<!-- 分级的级数，默认是6级，不要求用户手工录了，所以隐藏 -->
		#set($classCount = "6")
		#if("$!result.classCount"!="")
			#set($classCount = $!result.classCount)
		#end
		<input type="hidden" name="classCount" value="$classCount">
		
		<table cellpadding="0" cellspacing="0" border="0" class="framework" >
			<tr>
				<td>
					<div class="TopTitle">
						<span style="margin-top: 4px;">
						当前位置:<font id="posDiv">表单字段维护</font>
						</span>
						<span style="float: right;font-weight: normal;margin-right: 12px;margin-bottom: 10px;">
							<input type="button" class="bu_02"  onclick="subForm();" value="保存" />	
							<input type="button" class="bu_02"  onclick="closeForm();" value="关闭" />
						</span>
					</div>
					<div id="data_info_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;" >
					<span style="float:left; width:100%;font:bold 14px;padding-left: 12px;">
						<ul class="mainUl">							
														
							<li><span>标识名称：</span>
								<input  type="text" id="tableName" name="tableName" value="$!result.tableName" disableautocomplete="true" autocomplete="off"></li>
							<li><span>名称：</span>
								<input  type="hidden" id="tableDisplay" name="tableDisplay" value="$!result.handerDisplay()">
								<input  type="text" id="tableDisplayDis" name="tableDisplayDis" value="$!result.display.get("$globals.getLocale()")" ondblclick="mainmd();"  disableautocomplete="true" autocomplete="off">
							</li>
							<li><span>$text.get("com.report.module.type")：</span>
								<select name="MainModule" id="MainModule">				
									#foreach($erow in $globals.getEnumerationItems("MainModule"))
										<option value="$erow.value" #if($erow.value==$!result.MainModule) selected #end >$erow.name</option>
									#end
								</select>
							</li>	
							<br/>
							<li><span>录入控制：</span>
									<select name="sysParameter" >
									#foreach($erow in $globals.getEnumerationItems("SysParamControl"))
										<option value="$erow.value" #if($erow.value=="$!result.sysParameter") selected="selected" #end>$erow.name</option>
									#end
									</select>
							</li>				
							<li><span>$text.get("customTable.lb.fieldSysType")：</span>
									<select name="tableSysType" >
									#foreach($erow in $globals.getEnumerationItems("FieldSysType"))
										<option value="$erow.value" #if($erow.value=="$!result.tableSysType") selected="selected" #end>$erow.name</option>
									#end
									</select>
							</li>
							<br/>								
							<li><span>表单界面宽：</span>
								<input  type="text" name="tWidth" id="tWidth" value="$!result.tWidth" disableautocomplete="true" autocomplete="off">
							</li>
							<li><span>表单界面高：</span>
								<input  type="text" name="tHeight" id="tHeight" value="$!result.tHeight" disableautocomplete="true" autocomplete="off">
							</li>	
							
							#if("$!tableType" != "0") 
							<li id="rowCount">
								<span>明细默认行数：</span>		
								<input type="text" name="defRowCount" value="$!result.defRowCount">
							</li>
							#end
							<li style="width: 100%;padding-left: 84px;">								
								<input name="isView" id="isView" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.isView" =="1") checked #end value="1" >
								<label  for="isView">$text.get("com.db.view")</label>	
								<input name="isUsed" id="isUsed" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.isUsed" =="1") checked #end value="1" >
								<label  for="isUsed">$text.get("excel.list.isUsed")</label>
								<input name="isBaseInfo" id="isBaseInfo" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.isBaseInfo" =="1") checked #end value="1" >
								<label  for="isBaseInfo">$text.get("customTable.lb.isBaseInfo")</label>
								<input name="classFlag" id="classFlag" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.classFlag" =="1") checked #end value="1" >
								<label  for="classFlag">是否分类</label>
								<input name="draftFlag" id="draftFlag" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.draftFlag" =="1") checked #end value="1" >
								<label  for="draftFlag">能否存为草稿</label>	
								<input name="triggerExpress" id="triggerExpress" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.triggerExpress" =="1") checked #end value="1" >
								<label  for="triggerExpress">$text.get("customTable.lb.triggerExpress")</label>	
								<input name="isLayout" id="isLayout" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.isLayout" =="1") checked #end value="1" >
								<label  for="isLayout">启用个性化布局</label>
								#if("$!tableType" != "0") 
								<input name="isNULL" id="isNULL" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.isNULL" =="1") checked #end value="1" >
								<label  for="isNULL">明细不可为空</label>		
								#else 
								<input name="reAudit" id="reAudit" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.reAudit" =="1") checked #end value="1" >
								<label  for="reAudit">$text.get("ReportBillView.button.SupportCheck")</label>
								#end						
							</li>
						</ul>
					</span>
					<span style="float:left; width:98%;height:100px;font:bold 14px;padding-left: 12px;margin:10px 0px 15px 0px">
						<font>$text.get("customTable.lb.dbOperation")</font>
						<textarea name="fieldCalculate" id="fieldCalculate" style="width:100%;height:80px">$!result.fieldCalculate</textarea>
					</span>
					<span style="float:left; width:98%;height:100px;font:bold 14px;padding-left: 12px;margin:10px 0px 15px 0px">
						<font>$text.get("customTable.lb.extendButton")</font>
						<textarea name="extendButton" id="extendButton" style="width:100%;height:80px">$!result.extendButton</textarea>
					</span>
					<span style="float:left; width:98%;height:100px;font:bold 14px;padding-left: 12px;margin:10px 0px 15px 0px">
						<font>$text.get("oa.common.remark")</font>
						<textarea name="tableDesc" id="tableDesc" style="width:100%;height:80px">$!result.tableDesc</textarea>
					</span>
					<span style="float:left; width:98%;height:200px;font:bold 14px;padding-left: 12px;margin:10px 0px 15px 0px;display:none">
						<font>$text.get("com.tableInfo.layout.html")</font>
						<textarea name="layoutHTML" id="layoutHTML" style="width:100%;height:180px">$!globals.encodeHTML2($!result.layoutHTML)</textarea>
					</span>
					</div>
					<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;display:none" >
						<span style="float:left; width:100%;font:bold 14px;padding-left: 12px;height: 30px;">
						<ul class="detbtBar">
							<li id="b_addline" class="f-icon16" onclick="addRowline()" title="添加字段"></li>
							<li id="b_upline" class="f-icon16" onclick="upline()" title="上移"></li>
							<li id="b_downline" class="f-icon16" onclick="downline()" title="下移"></li>	
							<li id="b_delline" class="f-icon16" onclick="delline()" title="删除字段"></li>						
						</ul>
						</span>
						<table cellpadding="0" cellspacing="0" style="border: 0px;">
						<tr><td width="720px" style="padding: 0px;">	
						<div id="data_list_idTab" style="overflow-x:auto;padding: 0px;margin: 0px;height: 100%;width: 100%;background: none;">

						<table cellpadding="0" cellspacing="0" style="border-left: 0px;table-layout:fixed" id="FieldInfoIdTitle">
							<thead>
								<tr >
									<td width="30">序号</td>
									<td width="100px" >标识</td>
									<td width="100px">显示名称</td>	
									<td  width="70px" >字段类型</td>	
									<td width="50px">字符长度</td>
									<td width="50px">默认值</td>
									<td width="30px">必填</td>						
									<td width="90px">输入类型</td>
									<td  width="150px">选项设置</td>
								</tr>
							</thead>
						</table>
						<div id="data_list_idTabXa" style="overflow-y:auto;padding: 0px;margin: 0px;height: 100%;width: 100%;background: none;">
						<script type="text/javascript">
						var oDiv=document.getElementById("data_list_id");
						var sHeight=document.documentElement.clientHeight-60;
						oDiv.style.height=sHeight+"px";
						var oDiv=document.getElementById("data_info_id");					
						oDiv.style.height=sHeight+"px";
						var oDiv=document.getElementById("data_list_idTab");
						var sWidth=document.documentElement.clientWidth-387;
						if(sWidth >850) sWidth = 850;
						oDiv.style.width=sWidth+"px";
						oDiv.style.height=(sHeight-30)+"px";
						var oDiv=document.getElementById("data_list_idTabXa");
						oDiv.style.width=($("#FieldInfoIdTitle").width()+20)+"px";
						oDiv.style.height=(sHeight-87)+"px";
						</script>
						<table cellpadding="0" cellspacing="0" style="border-left: 0px;table-layout:fixed" id="FieldInfoId">	
							<tbody>
								<tr  class="c1" onclick="focusRow(this)">
									<td  width="30">1</td>
									<td width="100px" >
										<input type="hidden" name="fieldId" value=""/>
										<input type="hidden" name="isStat" value="0"/>
										<input type="hidden" name="isUnique" value="0"/>
										<input type="hidden" name="digits" value=""/>
										<input type="hidden" name="width" value=""/>
										<input type="hidden" name="moreInput" value=""/>
										<input type="hidden" name="calculate" value=""/>
										<input type="hidden" name="fieldSysType" value=""/>
										<input type="hidden" name="fieldIdentityStr" value=""/>
										<input type="hidden" name="copyType" value=""/>
										<input type="hidden" name="logicValidate" value=""/>
										<input type="hidden" name="popupType" value=""/>
										<input type="hidden" name="groupName" value=""/>
										<input type="hidden" name="insertTable" value=""/>
										<input type="hidden" name="isCopy" value="0"/>
										<input type="hidden" name="isReAudit" value="0"/>
										<input type="hidden" name="isLog" value="0"/>
										<input type="hidden" name="inputTypeOld" value="0"/>
										
										<input type="text" class="changeInput" name="fieldName" readonly disableautocomplete="true" autocomplete="off"/>
									</td>
									<td width="100px" >
										<input type="text" class="changeInput" value="编号" name="fieldDisplay"disableautocomplete="true" autocomplete="off" />
									</td>
									<td  width="70px">
										<select name="fieldType">
										 #foreach ($row in $globals.getDS("fieldType"))
										    <option value="$row.value">$row.name</option> 
										 #end
										</select>
									</td>
									<td  width="50px">
										<input type="text" class="changeInput" value="" name="maxLength" disableautocomplete="true" autocomplete="off"/>
									</td>
									<td  width="50px">
										<input type="text" class="changeInput" value="" name="defaultValue" disableautocomplete="true" autocomplete="off"/>
									</td>
									<td  width="30px"><input name="isNull" type="checkbox" style="width: 15px;"  value="1"></td>
									<td  width="90px">
										<select name="inputType" onchange="changeInput(this)">
										 #foreach ($row in $globals.getDS("inputType"))
										    <option value="$row.value">$row.name</option> 
										 #end
										</select>
									</td>
									<td   width="150px" align="left">
										<input type="text" class="changeInput" value="" name="moreInputId" disableautocomplete="true" autocomplete="off"/>
									</td>
								</tr> 
							</tbody>
						</table>
						</div>
						</div>
						</td>
						<td width="*" style="padding: 0px;min-width: 200px;">
							<ul class="dataListUL">
								<li class="titleLi">字段高级属性</li>
								<li>
									<input name="isUniqueId" type="checkbox" id="isUniqueId" style="width: 15px;" title="本字段的值不可重复"  value="1">
									<label  title="本字段的值不可重复" for="isUniqueId"  style="width: 25px;margin-left: 0px">唯一</label>
									<input name="isStatId" type="checkbox" id="isStatId" style="width: 15px;margin-left:5px"  title="明细表数值字段有效，录入明细时统计数量"  value="1">
									<label  title="明细表数值字段有效，录入明细时统计数量" for="isStatId"  style="width: 25px;margin-left: 0px">统计</label>
									<input name="isLogId" type="checkbox" id="isLogId" style="width: 15px;margin-left:5px"  title="单据添加删除时本字段是否记录到操作日志中"  value="1">
									<label  title="单据添加删除时本字段是否记录到操作日志中" for="isLogId"  style="width: 25px;margin-left: 0px">日志</label>
								</li>
								<li title="明细表字段有效，录单界面明细字段显示宽度">
									<label style="margin-left: 5px;">显示宽度</label>
									<input name="widthId" type="text" id="widthId" disableautocomplete="true" autocomplete="off">
								</li>
								<li title="数字字段有效">
									<label style="margin-left: 5px;">计算公式</label>
									<input name="calculateId" type="text" id="calculateId" disableautocomplete="true" autocomplete="off">
								</li>
								<li title="隐藏可显示字段的原始输入类型">
									<label style="margin-left: 5px;">原始类型</label>
									<select list="true" name="inputTypeOldId"  id="inputTypeOldId"  onchange="changeInput(this)">
										<option value=""></option>
										#foreach ($row in $globals.getDS("inputType"))
										    <option value="$row.value">$row.name</option> 
										 #end
									</select>
								</li>
								<li title="字段所属系统类型">
									<label style="margin-left: 5px;">系统类型</label>
									<select list="true" name="fieldSysTypeId"  id="fieldSysTypeId" >
										<option value=""></option>
										#foreach($row in $globals.getEnumerationItems("FieldSysType"))
											<option value="$row.value">$row.name</option> 
										#end
									</select>
								</li>
								<li title="字段所属标识">
									<label style="margin-left: 5px;">字段标识</label>
									<select name="fieldIdentityStrId"  id="fieldIdentityStrId" >
										<option value=""></option>
										#foreach($row in $globals.getEnumerationItems("fieldIdentityStr"))
											<option value="$row.value">$row.name</option> 
										#end
									</select>
								</li>
								
								<li title="明细字段有效，同一分组的字段明细表行头上有合并组名显示">
									<label style="margin-left: 5px;">分组显示</label>
									<input name="groupNameId" type="text" id="groupNameId"  disableautocomplete="true" autocomplete="off">
								</li>
								<li title="逻辑验证">
									<label style="margin-left: 5px;">逻辑验证</label>
									<input name="logicValidateId" type="text" id="logicValidateId" disableautocomplete="true" autocomplete="off">
								</li>
								<li title="字段复制标识">
									<label style="margin-left: 5px;">复制标识</label>
									<select name="copyTypeId"  id="copyTypeId" >
										<option value=""></option>
										#foreach($row in $globals.getEnumerationItems("copyType"))
											<option value="$row.value">$row.name</option> 
										#end
									</select>
								</li>
								<li>
									<input name="isCopyId" type="checkbox" id="isCopyId" style="width: 15px;" title="明细表行复制时本字段是否可复制"  value="1">
									<label  title="明细表行复制时本字段是否可复制" for="isCopyId" style="width: 50px;margin-left: 0px">单据复制</label>
									<input name="isReAuditId" type="checkbox" id="isReAuditId" style="width: 15px;margin-left:20px"  title="字段是否可以复核"  value="1">
									<label  title="字段是否可以复核" for="isReAuditId"  style="width: 30px;margin-left: 0px">复核</label>
									
								</li>
							</ul>
						</td>
						</tr></table>
					</div>
				</td>				
			</tr>
		</table>
		</form>
			</td>
		</tr>
	</table>
</body>
</html>
