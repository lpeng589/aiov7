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
<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script type="text/javascript">

function closeForm(){
	
	if(!window.save && is_form_changed()){
		if(! confirm("您有未保存的数据，是否确定放弃保存并离开")){
			return;
		}
	}
	
	if(parent.callBackPopup){
	#if("$!bean.name"!="")
	parent.callBackPopup($("#popupName",document).val(),$("#desc",document).val());
	#else
	parent.callBackPopup('','');
	#end
	}else if(parent.jQuery.exist('popupUpdate2Win')){ //子弹出窗
		parent.jQuery.close("popupUpdate2Win");
	}else if(parent.jQuery.exist('popupUpdateWin')){ //子弹出窗
		parent.jQuery.close("popupUpdateWin");
	}
}
function goAction(tit){
	if(tit == "baseInfo"){
		$("#data_info_id",document).show();
		$("#data_list_id",document).hide();
		$("#data_source_id",document).hide();
		$("#saveSource").hide();
		
		$("#titleInfoId",document).text("基本信息");		
	}else if(tit == "fieldInfo"){
		$("#data_info_id",document).hide();
		$("#data_list_id",document).show();
		$("#data_source_id",document).hide();
		$("#saveSource").hide();
		
		$("#titleInfoId",document).text("字段信息");		
	}else{
		$("#data_info_id",document).hide();
		$("#data_list_id",document).hide();
		$("#data_source_id",document).show();
		$("#saveSource").show();
		
		$("#titleInfoId",document).text("源码信息");		
	}
}
function addRowline(){
	onetr = $("#FieldInfoId tbody",document).find("tr:eq(0)").clone();	
	$("input",onetr).val("");
	$(onetr).removeClass("focusRow");
	$("input",onetr).removeAttr("readonly");
	$(onetr).find("select").val("0");
	$("#FieldInfoId",document).append(onetr);
	
	rowNo = 0;
	$("#FieldInfoId tbody",document).find("tr").each(function (){
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

curRow = null;
curRowNo = 0;
function focusRow(obj){
	curRow = obj;
	curRowNo = jQuery.inArray(obj,$("#FieldInfoId tbody",document).find("tr"));
	$(".focusRow",document).removeClass("focusRow");
	$(curRow).addClass("focusRow");
	if(obj == $("#FieldInfoId tbody",document).find("tr:last")[0]){
		addRowline();
	}
}

function scrollDiv(obj){
	$('#data_list_idTabShang').scrollLeft($(obj).scrollLeft());
}
function subForm(){
	if($("#popupName").val()==''){
		alert("标识名称不能为空");
		$("#popupName").focus();
		return;
	}
	
	if($("#defineSQL").val()==''){
		alert("弹窗SQL不能为空");
		$("#defineSQL").focus();
		return;
	}
	hasItem = false;
	hasError = false;
	var ina=0;
	$("input[name=fieldName]").each(function (){
		if($(this).val() != ''){
			hasItem = true;
			if($("select[name=fType]:eq("+ina+")").val()=="1"){
				if($("input[name=parentName]:eq("+ina+")").val()==""){
					alert("第"+(ina+1)+"行回填字段名不能为空");
					$("input[name=parentName]:eq("+ina+")").focus();
					hasError = true;
				}
			}
		}
		ina++;
	});
	if(hasError){
		return;
	}
	
	if(!hasItem){
		alert("请录入字段信息");
		return;
	}
	setCbVal('hiddfd');
	setCbVal('compare');
	setCbVal('relationKey');
	setCbVal('parentDisplay');
	setCbVal('keySearch');
	setCbVal('hiddenInput');
	window.save = true;
	document.form.submit();
}

function exportPop(){
	if(!window.save && is_form_changed()){
		if(! confirm("您有未保存的数据，是否确定放弃保存并离开")){
			return;
		}
	}
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/CustomAction.do?type=popupExport",{popupName:$("#popupName").val()},function(data){ 
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
		$("#sourceArea").val(data);
		goAction('source');
	} ); 
}
function importPop(){
	if(typeof(top.jblockUI)!="undefined" ){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	jQuery.post("/CustomAction.do?type=popupImport",{popupName:$("#popupName").val(),xml:$("#sourceArea").val()},function(data){ 
		if(typeof(top.jblockUI)!="undefined" ){
			top.junblockUI()
		}
		if(data == "OK"){
			alert("保存成功");
		}else{
			alert(data);
		}

	} ); 
}

function setCbVal(name){
	relNum=0;
	$("input[name="+name+"]").each(function (){
		$(this).val(relNum);
		relNum ++;
	});
}

#if("$!errorMsg" != "")
	alert('$!errorMsg');
#end

function updatePopup(type){
	popupName = "";
	if(type=="topPopup"){
		popupName = $("#topPopup",document).val();
		if(popupName==""){
			alert("请先填写低级弹出窗");
			return;
		}
	}else{
		popupName = $("#hasChild",document).val();
		if(popupName==""){
			alert("请先填写明细弹出窗");
			return;
		}
	}
	vtitle = "弹出窗修改";
	if(popupName ==""){
		vtitle = "弹出窗新建";
	}
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	urls = '/CustomAction.do?type=popupUpdate&popupName='+popupName+"&popupWin=popupUpdate2Win";
	openPop('popupUpdate2Win',vtitle,urls,width,height,true,true);
}
//显示弹出窗引用关系
function quoteShow(){
	urls = '/CustomAction.do?type=popupQutoeShow&popupValue='+$("#popupName").val();
	asyncbox.open({
		id: 'PopWinow',
		title : "弹出窗引用关系",
		url :urls,
		width : 520,
		height : 300,
		btnsbar : [{text    : '取消',action  : 'cancel'}],
		callback : function(action,opener){
		}
	});	
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
#FieldInfoIdTitle thead td{border-right:1px solid #999;padding-left:5px;vertical-align: bottom;height:33px; line-height:33px;overflow:hidden;white-space: nowrap;}
#FieldInfoIdTitle thead .lineOneNoD{height:0px;}
#FieldInfoIdTitle thead .lineOneNoD td{height:0px;border-right:1px solid #999;padding-left:5px;vertical-align: bottom;line-height:33px;overflow:hidden;white-space: nowrap;}
#FieldInfoId tbody td{border-right:1px solid #999;border-bottom:1px solid #999;position: relative;padding-left:5px}

.data_list thead tr td{ background:url(/style1/images/workflow/data_list_head_bg.gif); height:33px; line-height:33px;}
.data_list .dataListUL{margin:0px 5px 0px 5px;line-height:33px;border: 1px solid #999;min-height:67px}
.data_list .dataListUL li{ border-bottom: 1px solid #999;  }
.data_list .dataListUL .titleLi{ background:url(/style1/images/workflow/data_list_head_bg.gif); height:33px;padding-left:10px }
.data_list .dataListUL input,.data_list .dataListUL select{width: 117px;margin-left: 5px;}
.data_list .dataListUL label{width: 48px;margin-left: 5px;display: inline-block;}
.data_list table{width:100%;}
.tstBtn{background-image: url(/style/images/client/icon16.png);background-position: -32px 0;width: 16px;height: 16px;position: absolute;cursor: pointer;right: 1px;top: 8px;}
.mainUl{margin:0px 5px 35px 5px;line-height:33px;}
.mainUl li{float:left;   }
.mainUl li span{ width:80px ;text-align:right;  display: inline-block;margin:0px 5px 0px 0px }
.mainUl li input{ width:160px ; }


.focusRow{background-color:#9AF850;}
</style>
</head>
<body   onload="changeConfirm();">
	<table cellpadding="10px;" cellspacing="0" border="0" class="frame" style="margin-top:4px;">
		<tr>
			<td class="leftMenu" style="padding-top: 14px;width:114px"  valign="top">
				<div class="TopTitle" ><span><img src="/style1/images/workflow/ti_001.gif" /></span><span>弹出窗#if("$!bean.name"=="")新建#else修改#end</span></div>
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
	<iframe name="formFrame" style="display:none"></iframe>			
	<form method="post" scope="request" name="form" action="/CustomAction.do?type=popupUpdate" class="mytable"  target="formFrame">
	<input type="hidden" name="exec" id="exec" value="true"/>
	<input type="hidden" name="copy" id="copy" value="$!copy"/>
	<table cellpadding="0" cellspacing="0" border="0" class="framework" >
		<tr>
			<td>
				<div class="TopTitle">
					<span style="margin-top: 4px;">
					<font id="titleInfoId">基本信息</font>
					</span>
					<span style="float: right;font-weight: normal;margin-right: 12px;margin-bottom: 10px;">
						<input type="button" class="bu_02"  onclick="subForm();" value="保存" />	
						<input type="button" class="bu_02"  onclick="closeForm();" value="关闭" />
						<input type="button" class="bu_02"  onclick="quoteShow();" value="引用关系" />
						<input type="button" class="bu_02"  onclick="exportPop();" value="源代码 " />
						<input type="button" id="saveSource" style="display:none" class="bu_02"  onclick="importPop();" value="保存源码" />
					</span>
				</div>
				<div id="data_info_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;" >
					<span style="float:left; width:100%;font:bold 14px;padding-left: 12px;">
						<ul class="mainUl">
							#set($typeCk = "")
							#if("$!bean.type" == "checkBox") #set($typeCk = "checked") 
							#elseif("$!bean.type" != "") 
							<script type="text/javascript">alert("未识别的类型type=$!bean.type");</script> #end
							#set($showType = "")
							#if("$!bean.showType" == "defNoShow") #set($showType = "checked") 
							#elseif("$!bean.showType" != "") 
							<script type="text/javascript">alert("未识别的showType=$!bean.showType");</script> #end
							
							<li style="width: 100%;padding-left: 84px;">
								<input name="checkBox" type="checkbox" id="checkBox" style="width:18px;height:18px;margin-top:4px" $typeCk value="checkBox" >
								<label  for="type">多选</label>
								<input name="showType" type="checkbox" id="showType" style="width:18px;height:18px;margin-top:4px" $showType value="defNoShow" >
								<label  for="showType">默认无条件不显示</label>
								<input name="saveParentFlag" type="checkbox" id="saveParentFlag" style="width:18px;height:18px;margin-top:4px" #if("$!bean.saveParentFlag" == "true") checked  #end value="true" >
								<label  for="saveParentFlag">分级时可选择目录</label>
								<select name="version" style="margin-left: 10px;">
									    <option value="" >1.0版本</option> 
									    <option value="2" #if("$bean.version"=="2") selected #end>2.0版本</option> 
								</select>
								#if("$!popupWin" != "popupUpdate2Win")
								<input type="button" onclick="updatePopup('topPopup')" value="编辑父级弹出窗" /> 
								<input type="button" onclick="updatePopup('hasChild')" value="编辑引用明细弹出窗" /> 
								#end
							</li> 
							<li title="弹出窗英文标识">
								<span style="margin-left: 5px;">标识名称:</span><input name="popupName" type="text" id="popupName" value="$!bean.name" #if("$!bean.name" !="" && "$!copy"!="true") readonly #end ></li>
							#set($desc = $!bean.desc)
							#if("$!bean.desc" == "$!bean.name") #set($desc = "") #end
							<li title="给弹出窗取一个好记点的别名">
								<span style="margin-left: 5px;">描述:</span><input name="desc" type="text" id="desc" value="$desc" ></li>		
							<li title="分级弹出窗指定分级字段的表名和字段名">
								<span style="margin-left: 5px;">分级字段:</span><input name="classCode" type="text" id="classCode" value="$!bean.classCode" ></li>
							<li title="分级弹出窗指定分级字段的标识">
								<span style="margin-left: 5px;">分级标识:</span>
								<select name="classSysType" type="text" id="classSysType" >
								<option value=""></option>
								<option value="Customer" #if("$!bean.classSysType" == "Customer")selected #end>客户</option>
								<option value="Supplier" #if("$!bean.classSysType" == "Supplier")selected #end>供应商</option>
								</select>
								</li>
							<li title="分级弹出窗默认显示的分类代号">
								<span style="margin-left: 5px;">默认父级:</span><input name="defParentCode" type="text" id="defParentCode" value="$!bean.defParentCode" ></li>
							<li title="排序字段">
								<span style="margin-left: 5px;">排序字段:</span><input name="orderBy" type="text" id="orderBy" value="$!bean.orderBy" ></li>
							<li title="多单引用时的一级弾出窗" #if("$!popupWin" == "popupUpdate2Win")style="display:none" #end>
								<span style="margin-left: 5px;">父级弹出窗:</span><input name="topPopup" type="text" id="topPopup" value="$!bean.topPopup" ></li>	
							<li title="单据引用时的明细弾出窗" #if("$!popupWin" == "popupUpdate2Win")style="display:none" #end>
								<span style="margin-left: 5px;">明细弹出窗:</span><input name="hasChild" type="text" id="hasChild" value="$!bean.hasChild" ></li>	
							<li title="弹出窗会出现添加按扭并跳转到相应模块执行添加操作">
								<span style="margin-left: 5px;">快速添加:</span><input name="forwardModel" type="text" id="forwardModel" value="$!bean.forwardModel" ></li>			
							<li title="分级弹窗点下级时要清除的相关表名对应的查询条件">
								<span style="margin-left: 5px;">所属表名:</span><input name="belongTableName" type="text" id="belongTableName" value="$!bean.belongTableName" ></li>			
						</ul>
					</span>
					<span style="float:left; width:98%;height:200px;font:bold 14px;padding-left: 12px;margin:10px 0px 15px 0px">
						<font>弹窗SQL</font> <font style="color:#4040D1">(注意：只需写from及之后的部分)</font>
						<textarea name="defineSQL" id="defineSQL" style="width:100%;height:180px">$!defineSQL</textarea>
					</span>
					<span style="float:left; width:98%;height:70px;font:bold 14px;padding-left: 12px;margin:0px 0px 15px 0px">
						<font>添加时执行的条件，修改时不执行</font> 
						<textarea name="changeCond" style="width:100%;height:50px">$!bean.changeCond</textarea>
					</span>
				</div>
				<div id="data_source_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;display:none" >
				<textarea id="sourceArea" style="width: 100%;height: 100%;">
				</textarea>
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
					<div id="data_list_idTab" style="overflow:hidden;padding: 0px;margin: 0px;height: 100%;width: 100%;background: none;">
					<div id="data_list_idTabShang" style="overflow:hidden;padding: 0px;margin: 0px;height: 65px;background: none;" >
					<table cellpadding="0" width="1740" cellspacing="0" style="border-left: 0px;table-layout:fixed;" id="FieldInfoIdTitle">
						<thead>
							<tr class="lineOneNoD">
								<td width="30px"></td>
								<td width="50px"></td>
								<td width="300px"></td>	
								<td width="100px"></td>
								<td width="110px"></td>
								<td width="150px"></td>
								<td width="50px"></td>								
								<td width="40px" ></td>
								<td width="50px"></td>
																
								<td width="150px"></td>
								<td width="40px"></td>
								<td width="40px"></td>
								<td width="60px"></td>	
								<td width="120px"></td>
								<td width="40px"></td>
								<td width="60px"></td>	
								<td width="50px"></td>
								<td width="150px"></td>							
								<td width="150px"></td>
							</tr>
							<tr >
								<td width="30px" rowspan="2">序号</td>
								<td width="50px"  rowspan="2">类型</td>
								<td width="300px" rowspan="2">字段</td>	
								<td width="100px"  rowspan="2">别名</td>
								<td width="110px"  rowspan="2">系统类型</td>
								<td colspan="4">回填字段信息</td>								
								<td colspan="10">显示字段信息</td>
							</tr>
							<tr >								
								<td width="150px">回填字段名</td>
								<td width="50px">详情条件</td>								
								<td width="40px" >比较</td>
								<td width="50px">关联字段</td>
								
								<td width="150px">显示为</td>
								<td width="40px">回显</td>
								<td width="40px">宽度</td>
								<td width="60px">条件类型</td>	
								<td width="120px">默认值</td>
								<td width="40px">关键字</td>
								<td width="60px">隐藏可显示</td>	
								<td width="50px">输入类型</td>
								<td width="150px">输入值</td>							
								<td width="150px">子弹窗</td>
							</tr>
						</thead>
					</table>
					</div>
					<div id="data_list_idTabXa" style="overflow:auto;padding: 0px;margin: 0px;height: 100%;background: none;" onscroll="scrollDiv(this)">
					<script type="text/javascript">
					var sHeight=document.documentElement.clientHeight-60;
					var oDiv=document.getElementById("data_list_id");					
					oDiv.style.height=sHeight+"px";
					var oDiv=document.getElementById("data_info_id");					
					oDiv.style.height=sHeight+"px";
					var oDiv=document.getElementById("data_source_id");					
					oDiv.style.height=sHeight+"px";
					
					
					var oDiv=document.getElementById("data_list_idTab");
					var sWidth=document.documentElement.clientWidth-187;
					oDiv.style.width=sWidth+"px";
					oDiv.style.height=(sHeight-30)+"px";
					var oDiv=document.getElementById("data_list_idTabXa");
					oDiv.style.width=$("#data_list_idTab").width()+"px";
					oDiv.style.height=(sHeight-107)+"px";
					
					var oDiv=document.getElementById("data_list_idTabShang");
					oDiv.style.width=($("#data_list_idTab").width()-20)+"px";
					</script>
					<table cellpadding="0" cellspacing="0" style="border-left: 0px;table-layout:fixed" id="FieldInfoId">	
						<tbody>
					#foreach($row in $fields)
							<tr  class="c1" onclick="focusRow(this)">
								<td  width="30">$velocityCount</td>
								<td width="50px" >
									<select name="fType">
									    <option value="0" #if("$row.type"=="0") selected #end>显示</option> 
									    <option value="1" #if("$row.type"=="1") selected #end>回填</option> 
									</select>
								</td>
								<td width="300px" >
									<input type="text" class="changeInput" value="$!row.fieldName" name="fieldName" disableautocomplete="true" autocomplete="off"/>
								</td>
								<td  width="100px">
								#set($asName = $!row.asName)
								#if($asName == $row.fieldName) #set($asName = "") #end
									<input type="text" class="changeInput" value="$!asName" name="asName" disableautocomplete="true" autocomplete="off"/>
								</td>
								<td  width="110px">
								<select name="sysType" >
									    <option value="" ></option> 
										#foreach($erow in $globals.getEnumerationItems("FieldSysType"))
											<option value="$erow.value" #if("$erow.value" =="$!row.sysType" ) selected #end>$erow.name</option> 
										#end
								</select>
								</td>
								
								<td  width="150px">
									<input type="text" class="changeInput" value="$!row.parentName" name="parentName" disableautocomplete="true" autocomplete="off"/>
								</td>
								<td  width="50px">
									<input name="hiddfd" type="checkbox" style="width: 15px;" #if($!row.hidden) checked #end  value="true">
								</td>
								<td  width="40px">
									<input name="compare" type="checkbox" style="width: 15px;" #if($!row.compare) checked #end  value="true">
								</td>		
								<td  width="50px">
									<input name="relationKey" type="radio" style="width: 15px;" #if($!row.relationKey) checked #end  value="true">
								</td>
														
								<td  width="150px">
									<input type="text" class="changeInput" value="$!row.display" name="display" disableautocomplete="true" autocomplete="off"/>
								</td>
								<td  width="40px">
									<input name="parentDisplay" type="checkbox" style="width: 15px;" #if($!row.parentDisplay) checked #end  value="true">
								</td>
								<td  width="40px">
									<input type="text" class="changeInput" value="$!row.width" name="width" disableautocomplete="true" autocomplete="off"/>
								</td>
								<td  width="60px">
								<select name="searchType" >
									    <option value="" ></option> 
									    <option value="equal"  #if("$row.searchType"=="1") selected #end>等于</option>
									    <option value="match"  #if("$row.searchType"=="2") selected #end>模糊</option>
									    <option value="scope"  #if("$row.searchType"=="3") selected #end>范围</option>
									    <option value="matchl"  #if("$row.searchType"=="4") selected #end>左匹配</option>
									    <option value="matchr"  #if("$row.searchType"=="5") selected #end>右匹配</option>
									    <option value="more"  #if("$row.searchType"=="6") selected #end>大于</option>
									    <option value="input"  #if("$row.searchType"=="7") selected #end>仅输入</option>
								</select>
								</td>
								
								<td  width="120px">
									<input type="text" class="changeInput" value="$!row.defaultValue" name="defaultValue" disableautocomplete="true" autocomplete="off"/>
								</td>
								<td  width="40px">
									<input name="keySearch" type="checkbox" style="width: 15px;" #if($!row.keySearch) checked #end  value="true">
								</td>
								
								<td  width="60px">
									<input name="hiddenInput" type="checkbox" style="width: 15px;" #if("$!row.hiddenInput"=="true") checked #end  value="true">
								</td>								
								<td  width="50px">
									<select name="inputType" >
									    <option value="" ></option> 
									    <option value="checkBox"  #if("$row.inputType"=="checkBox") selected #end>多选</option>
									</select>
								</td>
								<td  width="150px">
									<input type="text" class="changeInput" value="$!row.inputValue" name="inputValue" disableautocomplete="true" autocomplete="off"/>
								</td>
								<td  width="150px">
									<input type="text" class="changeInput" value="$!row.popSelect" name="popSelect" disableautocomplete="true" autocomplete="off"/>
								</td>								
							</tr> 
						#end	
						</tbody>
					</table>
					</div>
					</div>
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
