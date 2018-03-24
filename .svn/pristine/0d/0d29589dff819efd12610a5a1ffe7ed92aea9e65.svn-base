<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/workflow.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"  />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script type="text/javascript">
try{
	var koron = new ActiveXObject("KoronCom.TrustedSites") ;
	koron.add("$!IP") ;
}catch (e){
}
function closeForm(){
	if(!window.save && is_form_changed()){
		if(! confirm("您有未保存的数据，是否确定放弃保存并离开")){
			return;
		}
	}
	if(parent.location.href.indexOf("OAWorkFlowTempAction.do")>-1){
		parent.location.href=parent.location.href;
	}
	parent.jQuery.close("$popWinName");

}
function goAction(tit){
	if(tit == "baseInfo"){
		$("#data_info_id",document).show();
		$("#data_list_id",document).hide();
		$("#data_child_id",document).hide();
		$("#data_brother_id",document).hide();
		$("#posDiv",document).text("基本信息");		
	}else if(tit == "fieldInfo"){
		$("#data_info_id",document).hide();
		$("#data_list_id",document).show();		
		$("#data_child_id",document).hide();
		$("#data_brother_id",document).hide();
		$("#posDiv",document).text("字段信息");		
	}else if(tit == "childInfo"){
		$("#data_info_id",document).hide();
		$("#data_list_id",document).hide();		
		$("#data_child_id",document).show();
		$("#data_brother_id",document).hide();
		$("#posDiv",document).text("明细表信息");		
	}else if(tit == "brotherInfo"){
		$("#data_info_id",document).hide();
		$("#data_list_id",document).hide();		
		$("#data_child_id",document).hide();
		$("#data_brother_id",document).show();
		$("#posDiv",document).text("邻居表信息");		
	}
}
function addRowline(){
	onetr = $("#FieldInfoId tbody").find("tr:eq(0)").clone();	
	$("input",onetr).val("");
	$(onetr).removeClass("focusRow");
	$("input",onetr).removeAttr("readonly");
	$(onetr).find("td:last").empty();
	$(onetr).find("select").val("0");
	$(onetr).find("select[name=fieldType]").val("2");
	$(onetr).find("input[type=checkbox]").removeAttr("checked");
	$(onetr).find("input[name=isCopy]").val("1");
	$(onetr).find("input[name=isMobile]").val("2");
	
	//$("#FieldInfoId").append(onetr);
	$(onetr).insertAfter($(curRow));
	
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
		if(curRowNo>=$("#FieldInfoId tbody").find("tr").size()){
			curRowNo = $("#FieldInfoId tbody").find("tr").size() -1;
		}
		focusRow($("#FieldInfoId tbody").find("tr:eq("+curRowNo+")").get(0));
	}
	
}
//上移单元格  
function upline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请选择需要上移的字段");
		return;
	}
	
	$(curRow).insertBefore($(curRow).prev());
	curRowNo = -1;
}
//下移单元格  
function downline(){
	if(curRow == null || typeof(curRow) == "undefined"){
		alert("请选择需要下移的字段");
		return;
	}
	$(curRow).insertAfter($(curRow).next());
	curRowNo = -1;
}

function setRowLine(){ 
	if(curRowNo== -1){
		return;
	}
	curUpdateAdv = false;
	$("#isStat").val("0");
	if($("#isStatId").attr("checked")=="checked"){
		$("input[name=isStat]:eq("+curRowNo+")").val("1");
	}else{
		$("input[name=isStat]:eq("+curRowNo+")").val("0");
	}
		
	$("input[name=fieldSysType]:eq("+curRowNo+")").val($("#fieldSysTypeId").val());
	$("input[name=fieldIdentityStr]:eq("+curRowNo+")").val($("#fieldIdentityStrId").val());
	$("input[name=copyType]:eq("+curRowNo+")").val($("#copyTypeId").val());
	$("input[name=logicValidate]:eq("+curRowNo+")").val($("#logicValidateId").val());
	
	str  = $("input[name=groupName]:eq("+curRowNo+")").val();
	var strs=str.split(";");
	strret="";
	found=false;
    for(var i=0;i<strs.length;i++){
    	if(strs[i].length>0){
		    var lanstr=strs[i].split(":");
			if(lanstr[0]=='$globals.getLocale()'){
				strret +='$globals.getLocale():'+$("#groupNameId").val()+";";
				found=true;
			}else {
				strret +=strs[i]+";";
			}
		}
	}
	if(!found){
		strret +='$globals.getLocale():'+$("#groupNameId").val()+";";
	}
	$("input[name=groupName]:eq("+curRowNo+")").val(strret);
	
	
	$("input[name=inputTypeOld]:eq("+curRowNo+")").val($("#inputTypeOldId").val());

	$("#isCopy").val("0");
	if($("#isCopyId").attr("checked")=="checked"){
		$("input[name=isCopy]:eq("+curRowNo+")").val("1");
	}else{
		$("input[name=isCopy]:eq("+curRowNo+")").val("0");
	}
	$("#isReAudit").val("0");
	if($("#isReAuditId").attr("checked")=="checked"){
		$("input[name=isReAudit]:eq("+curRowNo+")").val("1");
	}else{
		$("input[name=isReAudit]:eq("+curRowNo+")").val("0");
	}
	$("#isLog").val("0");
	if($("#isLogId").attr("checked")=="checked"){
		$("input[name=isLog]:eq("+curRowNo+")").val("1");
	}else{
		$("input[name=isLog]:eq("+curRowNo+")").val("0");
	}
	$("#isMobile").val("0");
	if($("#isMobileId").attr("checked")=="checked"){
		$("input[name=isMobile]:eq("+curRowNo+")").val("1");
	}else{
		$("input[name=isMobile]:eq("+curRowNo+")").val("2");
	}
}

curRow = null;
curRowNo = -1;
curUpdateAdv = false;
function focusRow(obj){
	
	$("#advPropDiv").show();
	setRowLine(); //更新行高级属性数据
	curUpdateAdv = true;
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
	$("#fieldSysTypeId").val($("input[name=fieldSysType]:eq("+curRowNo+")").val());
	$("#fieldIdentityStrId").val($("input[name=fieldIdentityStr]:eq("+curRowNo+")").val());
	$("#copyTypeId").val($("input[name=copyType]:eq("+curRowNo+")").val());
	$("#logicValidateId").val($("input[name=logicValidate]:eq("+curRowNo+")").val());
	
	if($("input[name=groupName]:eq("+curRowNo+")").val()==""){
		$("#groupNameId").val("");
	}else{
		gn = $("input[name=groupName]:eq("+curRowNo+")").val();
		if(gn.indexOf("$globals.getLocale():")> -1){
			gn = gn.substring(gn.indexOf("$globals.getLocale():")+"$globals.getLocale():".length);
			gn = gn.substring(0,gn.indexOf(";"));
			$("#groupNameId").val(gn);
		}else{
			$("#groupNameId").val("");
		}
	}
	
	
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
	$("#isMobileId").removeAttr("checked","checked");
	if($("input[name=isMobile]:eq("+curRowNo+")").val()=="1"){
		$("#isMobileId").attr("checked","true");
	}
}

function setEnumMoreInput(rowNo){
	$("#FieldInfoId tbody").find("tr:eq("+rowNo+")").find("td:last").empty().
		append('<select name="moreInputId" enumerName="'+$("input[name=refEnumerationName]:eq("+rowNo+")").val()+'"  onchange="changeEnumInput(this)" ><option value=""></option><option value="updateEnum">修改选项</option><option value="newEnum">新建选项</option><option value="selectEnum">选择选项</option></select>');
	hideObj = $("#FieldInfoId tbody").find("tr:eq("+rowNo+")").find("td:last").find("select")[0];
	hideEnumerName = $("input[name=refEnumerationName]:eq("+rowNo+")").val();
	dealAsyn();
}
function setPopUpInput(rowNo){
	popSel = $("input[name=inputValue]:eq("+rowNo+")").val().split(":");
	popupName = "";
	popupDesc = "";
	if(popSel.length > 0){
		popupName = popSel[0];
		popupDesc = popSel[0];			
	}
	if(popSel.length > 1){
		popupDesc = popSel[1];			
	}
	$("#FieldInfoId tbody").find("tr:eq("+rowNo+")").find("td:last").empty().
		append('<input type="text" class="changeInput" value="'+popupDesc+'" name="moreInputId" style="width:130px" readonly /><b class="tstBtn" onclick="clickPopUp(this)"></b>');	
}
function setDropSelectInput(rowNo){
	$("#FieldInfoId tbody").find("tr:eq("+rowNo+")").find("td:last").empty().
		append('<input type="text" class="changeInput" value="'+$("input[name=inputValue]:eq("+rowNo+")").val()+'" name="moreInputId" onchange="changeMoreInput(this)" />');
}
function changeInput(obj){
	
	if($(obj).val() == "1" ||$(obj).val() == "5" ||$(obj).val() == "10" ){//枚举,复选，单选
		setEnumMoreInput(curRowNo);
	}else if($(obj).val() == "2"){//弹出窗
		setPopUpInput(curRowNo);
	}else if($(obj).val() == "16"){//下拉选择
		setDropSelectInput(curRowNo);
	}else {
		$("#FieldInfoId tbody").find("tr:eq("+curRowNo+")").find("td:last").empty();
	}
}
function initMoreInput(){
	rowC = 0;
	$("#FieldInfoId tbody").find("select[name=inputType]").each(function (){
		inputOld = $("#FieldInfoId tbody").find("input[name=inputTypeOld]:eq("+rowC+")").val();
		if($(this).val()=="1" || $(this).val()=="5" || $(this).val()=="10" || inputOld=="1" || inputOld=="5" || inputOld=="10"){
			setEnumMoreInput(rowC);
		}else if($(this).val()=="2" || inputOld=="2" ){
			setPopUpInput(rowC);
		}else if($(this).val()=="16" || inputOld=="16" ){
			setDropSelectInput(rowC);
		}
		rowC++;
	});
}

function clickPopUp(obj){
	focusRow($(obj).parents("tr")[0]);
 
	popSel = $("input[name=inputValue]:eq("+curRowNo+")").val().split(":");
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
				popSel = $("input[name=inputValue]:eq("+curRowNo+")").val().split(":");
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
	if(desc != ""){
		$("input[name=inputValue]:eq("+curRowNo+")").val(name+":"+desc);
		$("#FieldInfoId tbody").find("tr:eq("+curRowNo+")").find("td:last").find("input").val(desc);
	}else if(name != ""){
		$("input[name=inputValue]:eq("+curRowNo+")").val(name);
		$("#FieldInfoId tbody").find("tr:eq("+curRowNo+")").find("td:last").find("input").val(name);
	}
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
		width : 1030,
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
function updatePopUp(popupName,copy){
	vtitle = "弹出窗修改";
	if(popupName ==""){
		vtitle = "弹出窗新建";
	}
	if(copy){
		vtitle = "弹出窗复制";
	}
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	urls = '/CustomAction.do?type=popupUpdate&copy='+copy+'&popupName='+popupName+"&popupWin=popupUpdateWin";
	openPop('popupUpdateWin',vtitle,urls,width,height,true,true);
}


function changeMoreInput(obj){
	$("input[name=inputValue]:eq("+curRowNo+")").val($(obj).val());
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
				$("input[name=refEnumerationName]:eq("+curRowNo+")").val(hideEnumerName);
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
				$("input[name=refEnumerationName]:eq("+curRowNo+")").val(hideEnumerName);
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
				//枚举查询窗点确定按扭
				$(obj).children().first().attr("selected","selected");		
				hideEnumerName = opener.jQuery('input:radio[name="keyId"]:checked').val();		
				$(obj).attr("enumerName",hideEnumerName);
				$("input[name=refEnumerationName]:eq("+curRowNo+")").val(hideEnumerName);
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
//枚举查询窗双击
function seachAsyn(enumerName){
	hideEnumerName = enumerName;
	$(hideObj).attr("enumerName",hideEnumerName);
	$("input[name=refEnumerationName]:eq("+curRowNo+")").val(hideEnumerName);
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
		if($("input[name=refEnumerationName]:eq("+curRowNo+")").val() == ""){
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
	if(str==""){
		mlf.value="";
		mhf.value = str;  
		return;
	}
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

function childmd(){ 
	var url = "/common/moreLanguage.jsp?popupWin=ChildLanguage&len=200&str="+encodeURI($("input[name=fieldDisplay]:eq("+curRowNo+")").val()) ;
	asyncbox.open({id:'ChildLanguage',title:'多语言',url:url,width:530,height:300});    
}

function fillChildLanguage(str){
	if(str==""){
		$("input[name=fieldDisplayDis]:eq("+curRowNo+")").val("");
		$("input[name=fieldDisplay]:eq("+curRowNo+")").val(str);
		return;
	}
	var strs=str.split(";");
    for(var i=0;i<strs.length;i++){
	    var lanstr=strs[i].split(":");
		if(lanstr[0]=='$globals.getLocale()'){
			$("input[name=fieldDisplayDis]:eq("+curRowNo+")").val(lanstr[1]);
		    break;
		}
	}
	$("input[name=fieldDisplay]:eq("+curRowNo+")").val(str);
}
function groupmd(){ 
	var url = "/common/moreLanguage.jsp?popupWin=GroupLanguage&len=200&str="+encodeURI($("input[name=groupName]:eq("+curRowNo+")").val()) ;
	asyncbox.open({id:'GroupLanguage',title:'多语言',url:url,width:530,height:300});    
}
function fillGroupLanguage(str){
	if(str==""){
		$("#groupNameId").val("");
		$("input[name=groupName]:eq("+curRowNo+")").val("");
		return;
	}
	var strs=str.split(";");
    for(var i=0;i<strs.length;i++){
	    var lanstr=strs[i].split(":");
		if(lanstr[0]=='$globals.getLocale()'){
			$("#groupNameId").val(lanstr[1]);
		    break;
		}
	}
	$("input[name=groupName]:eq("+curRowNo+")").val(str);
}
function scrollDiv(obj){
	$('#data_list_idTabShang').scrollLeft($(obj).scrollLeft());
}


function subForm(){
	if(curUpdateAdv){ //如果高级页还未提交数据，先提交
		setRowLine();
	}
	if($("#tableName",document).val().trim()==""){
		alert("标识名称不能为空");
		goAction('baseInfo');
		$("#tableName",document).focus();
		return;
	}
	if(!containSC($("#tableName",document).val())){
		alert("标识名称不能包含特殊字符");
		goAction('baseInfo');
		$("#tableName",document).focus();
		return;
	}
	if($("#tableDisplayDis",document).val().trim()==""){
		alert("名称不能为空");
		goAction('baseInfo');
		$("#tableDisplayDis",document).focus();
		return;
	}
	if(!containSC($("#tableDisplayDis",document).val())){
		alert("名称不能包含特殊字符");
		goAction('baseInfo');
		$("#tableName",document).focus();
		return;
	}
	
	if($("#tableDisplayDis",document).val().trim()!="" && $("#tableDisplay",document).val().trim()==""){
		$("#tableDisplay",document).val("$globals.getLocale():"+$("#tableDisplayDis",document).val().trim()+";");
	}else{
		var disVal = $("#tableDisplay",document).val().trim().split(";");
		var newDisVal = "";
		for(var j=0;j<disVal.length;j++){
			if(disVal[j]!=""){
				if(disVal[j].indexOf("$globals.getLocale():")>-1){
					newDisVal += "$globals.getLocale():"+$("#tableDisplayDis",document).val().trim()+";";
				}else{
					newDisVal += disVal[j]+";";
				}
			}
		}
		$("#tableDisplay",document).val(newDisVal);
	}
	
	if($("#tableType").val() == "1" && Number($("#defRowCount").val()) <=0){
		alert("默认行数不能小于等于0");
		goAction('baseInfo');
		$("#defRowCount",document).focus();
		return;
	}
	
	allFieldName=";";
	sRowNum = 0;
	isError = false;
	$("input[name=fieldName]",document).each(function(){
		$("input[name=listOrder]:eq("+sRowNum+")",document).val(sRowNum);
		$("input[name=isNull]:eq("+sRowNum+")",document).val($("input[name=isNullId]:eq("+sRowNum+")",document).attr("checked")=="checked"?"1":"0");
		$("input[name=isUnique]:eq("+sRowNum+")",document).val($("input[name=isUniqueId]:eq("+sRowNum+")",document).attr("checked")=="checked"?"1":"0");
		
		if($(this).val().trim() != ""){
			if(allFieldName.indexOf(";"+$(this).val().trim()+";")>-1){
				isError = true;
				alert("第"+(sRowNum+1)+"行字段标识重复");
				goAction('fieldInfo');
				$(this).focus();
				sRowNum ++;
				return;
			}else{
				allFieldName +=$(this).val().trim()+";";
			} 
			
		
			if(!containSC($(this).val().trim())){
				isError = true;
				alert("第"+(sRowNum+1)+"行字段字段标识不能包含特殊字符");
				goAction('fieldInfo');
				$("input[name=fieldDisplayDis]:eq("+sRowNum+")",document).focus();
				sRowNum ++;
				return;
			}else if($("input[name=fieldDisplayDis]:eq("+sRowNum+")",document).val().trim()==""){
				isError = true;
				alert("第"+(sRowNum+1)+"行字段显示名称不能为空");
				goAction('fieldInfo');
				$("input[name=fieldDisplayDis]:eq("+sRowNum+")",document).focus();
				sRowNum ++;
				return;
			}else if(!containSC($("input[name=fieldDisplayDis]:eq("+sRowNum+")",document).val().trim())){
				isError = true;
				alert("第"+(sRowNum+1)+"行字段显示名称不能包含特殊字符");
				goAction('fieldInfo');
				$("input[name=fieldDisplayDis]:eq("+sRowNum+")",document).focus();
				sRowNum ++;
				return;
			}else if($("input[name=fieldDisplayDis]:eq("+sRowNum+")",document).val().trim()!="" && $("input[name=fieldDisplay]:eq("+sRowNum+")",document).val().trim()==""){
				$("input[name=fieldDisplay]:eq("+sRowNum+")",document).val("$globals.getLocale():"+$("input[name=fieldDisplayDis]:eq("+sRowNum+")",document).val().trim()+";");
			}else{
				disValOld = $("input[name=fieldDisplay]:eq("+sRowNum+")",document).val().trim();
				if(disValOld.indexOf("$globals.getLocale():") == -1){
					$("input[name=fieldDisplay]:eq("+sRowNum+")",document).val(disValOld+"$globals.getLocale():"+$("input[name=fieldDisplayDis]:eq("+sRowNum+")",document).val().trim()+";");
				}else{
					var disVal = disValOld.split(";");
					var newDisVal = "";
					for(var j=0;j<disVal.length;j++){
						if(disVal[j]!=""){
							if(disVal[j].indexOf("$globals.getLocale():")>-1){
								newDisVal += "$globals.getLocale():"+$("input[name=fieldDisplayDis]:eq("+sRowNum+")",document).val().trim()+";";
							}else{
								newDisVal += disVal[j]+";";
							}
						}
					}
					$("input[name=fieldDisplay]:eq("+sRowNum+")",document).val(newDisVal);
				}
			}
			if($("input[name=width]:eq("+sRowNum+")",document).val().trim()==""){
				$("input[name=width]:eq("+sRowNum+")",document).val(200);
			}
			ftype = $("select[name=fieldType]:eq("+sRowNum+")",document).val().trim();
			intype = $("select[name=inputType]:eq("+sRowNum+")",document).val().trim();
			if((ftype=="2"||ftype=="3"||ftype=="18"||ftype=="4"||ftype=="19"||ftype=="20") && intype !='17'){
				maxl =$("input[name=maxLength]:eq("+sRowNum+")",document).val().trim();
				if(maxl=="" ||maxl=="0"){
					isError = true;
					alert("第"+(sRowNum+1)+"行字符长度不能为空或为0");
					goAction('fieldInfo');
					$("input[name=maxLength]:eq("+sRowNum+")",document).focus();
					sRowNum ++;
					return;
				}
			}
			
		}
		sRowNum ++;
	});
	if(isError){
		return false;
	}
	if(allFieldName.length<=1){
		alert("您还未录入字段");
		goAction('fieldInfo');
		return;
	}
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
 	disableForm(form);
 	
 	window.save = true;
	document.form.submit()
}

function updateChild(tname){
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	urls = '/CustomAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId='+tname;
	parent.openPop('TableChildmgtDiv','添加表单',urls,width,height,true,true);
}
function addChild(type){
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	urls = '/CustomAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&tableType='+type+'&perantTableName=$!result.tableName';
	parent.openPop('TableChildmgtDiv','添加表单',urls,width,height,true,true);
}
function selectChild(type){
	asyncbox.open({
　　		url : '/vm/tableInfo/brotherTableSelect.jsp',
　　 	width : 800,
　　 	height : 500,
		title  :'选择邻居表',
　　　	btnsbar : jQuery.btn.OKCANCEL, 
　　　	callback : function(action,opener){
	　　　　　if(action == 'ok'){
	　　　　　　　rstr =opener.getSelect();
				if(rstr != ""){
						jQuery.get("/CustomAction.do?type=addBrother&tableName=$!result.tableName&keyId="+rstr,function(data){
							if(data.indexOf("OK:") > -1){
								alert(data.substring(3));
							}else{
								alert(data);
							}
							
						});
				}else{
					alert("请选择需要添加的邻居表");
					return false;
				}
	　　　　　}
　　　	}
    });
}
function delChild(pname,tname,obj){
	if(confirm("确定要删除明细表吗？此操作不可恢复!!")){
		jQuery.get("/CustomAction.do?operation=$globals.getOP("OP_DELETE")&ajax=true&keyId="+tname+"&parentTableName="+pname,function(data){
			if(data.indexOf("OK:") > -1){
				alert(data.substring(3));
				$("#"+obj).remove();
			}else{
				alert(data);
			}
			
		});
	}
}
function defineOp(){
	asyncbox.open({
　　		url : '/CustomAction.do?type=defineOp',
		id  : 'defineOp',
　　 		width : 800,
　　 		height : 500,
		title  :'自定义操作',
　　　		btnsbar : jQuery.btn.OKCANCEL, 
　　　		callback : function(action,opener){
	　　　 		if(action == 'ok'){
	　　　　　　　		str = opener.getdefineOp();
				$("#fieldCalculate",document).val(str);
	　　　		}
　　　	   }
    });
    
}


function extendBt(){
	width=document.documentElement.clientWidth-50;
	height=document.documentElement.clientHeight-50;
	asyncbox.open({
　　		url : '/vm/tableInfo/extendButton.jsp',
		id  : 'extendButtonbt',
　　 		width : width,
　　 		height : height,
		title  :'扩展按扭',
　　　		btnsbar : jQuery.btn.OKCANCEL, 
　　　		callback : function(action,opener){
	　　　 		if(action == 'ok'){
	　　　　　　　		str = opener.getdefineOp();
					if(str == "error"){
						return false;
					}else{
						$("#extendButton",document).val(str);
					}
	　　　		}
　　　	   }
    });
}
function showCalculate(){
	width=document.documentElement.clientWidth-50;
	height=document.documentElement.clientHeight-50;
	asyncbox.open({
　　		url : '/vm/tableInfo/calculate.jsp',
		id  : 'calculatebt',
　　 		width : width,
　　 		height : height,
		title  :'计算公式配置',
　　　		btnsbar : jQuery.btn.OKCANCEL, 
　　　		callback : function(action,opener){
	　　　 		if(action == 'ok'){
	　　　　　　　		str = opener.getdefineOp();
					if(str == "error"){
						return false;
					}else{
						$("input[name=calculate]:eq("+curRowNo+")").val(str);
					}
	　　　		}
　　　	   }
    });
}

function changeWap(obj){
	if(obj.checked){
		$(obj).parent("td").find("input[name=width]").val(0)
	}else{
		$(obj).parent("td").find("input[name=width]").val(200);
	}
}

//新的个性化表单设计
function opLayoutHTML(){
	var tableCHName=$("#tableDisplayDis").val();
	var url="";
	
	url="/OAWorkFlowTableAction.do?operation=7&turnType=layoutHTML&designId=$!result.id&tableName=$!result.tableName&tableCHName="+encodeURI(tableCHName)+"&type=simple";
	window.open(url);
}
function formatSet(reportType){
	jQuery.get("/CustomAction.do?type=getReportId&tableName=$!result.tableName&reportType="+reportType,function(data){
		if(data.CODE !="OK"){
			alert(data.MSG);
			return;
		}
		var urls = "/ReportSetAction.do?operation=84&reportId="+data.id;
		asyncbox.open({id:'Popdiv',title:'样式设计弹出框',url:urls,width:750,height:470});
	},'json');
}

function sqlSet(reportType){
	//取单据打印报表
	jQuery.get("/CustomAction.do?type=getBillSQL&tableName=$!result.tableName&reportType="+reportType,function(data){
		if(data.CODE !="OK"){
			alert(data.MSG);
			return;
		}
		var flag=data.data[3];
		var fileName=data.data[1];
		var reportNumber=data.data[2];
		var reportId=data.data[0];
		try{
			var mimetype = navigator.mimeTypes["application/np-print"];
			if(mimetype){
				if(mimetype.enabledPlugin){
					var cb = document.getElementById("plugin");
					cb.sql('$!globals.getLocale()',fileName,'$!IP|$!port',reportId,reportNumber,flag,"$!JSESSIONID",'$LoginBean.id');
				}
			}else{
				var obj = new ActiveXObject("KoronReportPrint.SQLDesign") ;
		    	obj.URL='$!IP|$!port' ;
		    	obj.NewFlag = flag ;
		    	obj.FileName = fileName ;
		    	obj.ReportName = reportNumber ;
		   		obj.ReportId = reportId ;
		   		obj.Cookie ="$!JSESSIONID" ;
			   	obj.UserId ='$LoginBean.id' ;
		    	obj.Language = '$!globals.getLocale()' ;
				obj.OpenSQL();	
			}
		}catch (e){
			alert("请安装最新的科荣控件，如果已经安装请在浏览器设置中允许使用本网插件。") ;
		}
	},'json');
}
//启用审核流
function startFlow(){	
	var urlstr = "/UtilServlet?operation=startFlow&tableName=$!result.tableName&MainModule="+$("#MainModule").val();
	jQuery.get(urlstr,function(data){
		if(data.indexOf("ID=")==0){
			turl="/OAWorkFlowTempAction.do?operation=7&keyId="+(data.substring(3))+"&selectType=status&val=1&moduleType=sys&fromPage=table";
			width=document.documentElement.clientWidth-100;
			height=document.documentElement.clientHeight-100;
			openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight); 
		}else{
			alert(data);
		}
	})
}
function menu(){
	jQuery.get("/CustomAction.do?type=getMenu&tableName=$!result.tableName",function(data){
		if(data.CODE !="OK"){
			alert(data.MSG);
			return;
		}
		turl="/UserFunctionAction.do?tableName=tblModules&keyId="+data.id+"&moduleType=&f_brother=&operation=5";
		
		width=document.documentElement.clientWidth;
		height=document.documentElement.clientHeight;
		openPop('PopMainOpdiv','',turl,width,height,false,height==document.documentElement.clientHeight);  
	},'json');
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

.childTable  thead td{border-right:1px solid #999;border-bottom:1px solid #999;height:33px;white-space: nowrap;overflow: hidden;background-color: #EFEAF8;vertical-align: middle;}
.childTable  tbody td{border-right:1px solid #999;border-bottom:1px solid #999;position: relative;}

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
<body onload="initMoreInput();">
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
						#if("$!tableType"!=1&&"$!result.id" !="")
						<li onclick="goAction('childInfo');" style="cursor: pointer;border-top: 0px;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>明细表</b></li>		 
						#end
						#if("$!tableType"==0&&"$!result.id" !="")
						<li onclick="goAction('brotherInfo');" style="cursor: pointer;border-top: 0px;"><img  style="margin-left: 10px;margin-right:5px;" src="/style1/images/workflow/2.png" /><b>邻居表</b></li>		 	 
						#end
					</ul>
				</div>	
				</div>			
			</td>
			<td  style="padding-top: 0px;" valign="top">
		<iframe name="formFrame" style="display:none"></iframe>
		<form  method="post" scope="request" name="form" action="/CustomAction.do?OAWorkFlowId=$designId"  target="formFrame">
		#if("$!result.id" =="")
		<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
		#else
		<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
		#end
		<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
		<input type="hidden" name="winCurIndex" value="$!winCurIndex">
		<input type="hidden" name="popWinName" value="$!popWinName">
		
		
		<input type="hidden" name="tableId" value="$!result.id">
		
		<input type="hidden" name="tableType" id="tableType" value="$!tableType"/>
		<!-- 分支机构共享，暂未用 -->
		<input type="hidden" name="isSunCmpShare" id="isSunCmpShare" value="$!result.isSunCmpShare"/>
		<!-- 是否需要复制，暂未发现使用 -->
		<input type="hidden" name="needsCopy" id="needsCopy" value="$!result.needsCopy"/>
		
		<!-- 单据支持上下篇，应该已经失效 -->
		<input type="hidden" name="hasNext" id="hasNext" value="$!result.hasNext"/>
		<!-- 关联表，据说是用来做权限用的，现在已失效 -->
		<input type="hidden" name="relationTable" value="$!result.relationTable">
		
		
		<!-- 分级的级数，默认是6级，不要求用户手工录了，所以隐藏 -->
		#set($classCount = "6")
		#if("$!result.classCount"!="")
			#set($classCount = $!result.classCount)
		#end
		<input type="hidden" name="classCount" value="6">
		
		<table cellpadding="0" cellspacing="0" border="0" class="framework" >
			<tr>
				<td>
					<div class="TopTitle">
						<span style="margin-top: 4px;">
						当前位置:<font id="posDiv">基本信息</font>
						</span>
						<span style="float: right;font-weight: normal;margin-right: 12px;margin-bottom: 10px;">
							<button type="button" class="bu_02"  onclick="subForm();" >保存</button>	
							<input type="button" class="bu_02"  onclick="closeForm();" value="关闭" />
							#if("$!tableType"!=1)
							<input type="button" class="bu_02"  onclick="defineOp();" value="自定义操作" />
							<input type="button" class="bu_02"  onclick="extendBt();" value="扩展按扭" />
							#end  
							#if("$!result.id" !="" && "$tableType" != '1')
							<input type="button" class="bu_02"  onclick="opLayoutHTML();" value="个性化界面" />
							#if("$!ftableType"!="simple")
							<input type="button" class="bu_02"  onclick="startFlow();" value="审核流程" />
							<input type="button" class="bu_02"  onclick="menu();" value="菜单" />
							<input type="button" class="bu_02"  onclick="sqlSet('TABLELIST');" value="列表数据源" />
							<input type="button" class="bu_02"  onclick="formatSet('TABLELIST');" value="列表打印样式" />
							<input type="button" class="bu_02"  onclick="sqlSet('BILL');" value="单据打印数据源" />
							<input type="button" class="bu_02"  onclick="formatSet('BILL');" value="单据打印样式" />
							#end
							
							#end
						</span>
					</div>
					<div id="data_info_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;" >
					<span style="float:left; width:100%;font:bold 14px;padding-left: 12px;">
						<ul class="mainUl">							
														
							<li><span>标识名称：</span>
								<input  type="text" id="tableName" name="tableName" value="$!result.tableName" #if("$!result.id" !="") readonly #end disableautocomplete="true" autocomplete="off">
								<input  type="hidden" id="oldtableName" name="oldtableName" value="$!result.tableName"></li>
							<li><span>名称：</span>
								<input  type="hidden" id="tableDisplay" name="tableDisplay" value="$!result.handerDisplay()">
								<input  type="text" id="tableDisplayDis" name="tableDisplayDis" value="$!result.display.get("$globals.getLocale()")"  disableautocomplete="true" autocomplete="off"><!-- ondblclick="mainmd();" -->
							</li>
							#if("$!tableType" != "0")
							<li><span>主表：</span>
								<input  type="text" id="perantTableName" name="perantTableName" value="$!result.perantTableName" disableautocomplete="true" autocomplete="off"></li>
							#end
							
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
										<option value="$erow.value" #if($erow.value=="$!result.tableSysType" || ("$!result.tableSysType" == "" && $erow.value=="Normal" )) selected="selected" #end>$erow.name</option>
									#end
									</select>
							</li>
							#if("$!tableType" == "2") 
							<li><span>邻居表关系：</span>
									<select name="brotherType" >
										<option value="0" #if("0"=="$!result.brotherType") selected="selected" #end>一对一</option>
										<option value="1" #if("1"=="$!result.brotherType") selected="selected" #end>一对多</option>
									</select>
							</li>
							#end
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
								<input type="text" name="defRowCount" id="defRowCount" value="$!result.defRowCount">
							</li>
							#end
							<li style="width: 100%;padding-left: 84px;">								
								<input name="isView" id="isView" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.isView" =="1") checked #end #if("$!result.id" !="") onclick="return false;" #end value="1" >
								<label  for="isView">$text.get("com.db.view")</label>	
								<input name="isUsed" id="isUsed" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.isUsed" =="1") checked #end value="1" >
								<label  for="isUsed">$text.get("excel.list.isUsed")</label>
								<input name="isBaseInfo" id="isBaseInfo" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.isBaseInfo" =="1") checked #end value="1" >
								<label  for="isBaseInfo">$text.get("customTable.lb.isBaseInfo")</label>
								<input name="classFlag" id="classFlag" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.classFlag" =="1") checked #end value="1" >
								<label  for="classFlag">是否分类</label>
								<input name="copyParent" id="copyParent" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.copyParent" =="1") checked #end value="1" >
								<label  for="copyParent">添加分类资料自动复制上级</label>
								<input name="draftFlag" id="draftFlag" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.draftFlag" =="1") checked #end value="1" >
								<label  for="draftFlag">能否存为草稿</label>	
								<input name="triggerExpress" id="triggerExpress" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.triggerExpress" =="1") checked #end value="1" >
								<label  for="triggerExpress">$text.get("customTable.lb.triggerExpress")</label>	
								<input name="isLayout" id="isLayout" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.isLayout" =="1") checked #end value="1" >
								<label  for="isLayout">启用个性化布局</label> 
								#if("$!tableType" == "1") 
								<input name="tableIsNull" id="tableIsNull" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.isNull" =="1") checked #end value="1" >
								<label  for="isNULL">明细不可为空</label>		
								#else 
								<input name="reAudit" id="reAudit" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.reAudit" =="1") checked #end value="1" >
								<label  for="reAudit">$text.get("ReportBillView.button.SupportCheck")</label>
								#end	
								#if("$!tableType" != "1") 
								<input name="wakeUp" id="wakeUp" type="checkbox" style="width:18px;height:18px;margin-top:4px" #if("$!result.wakeUp" =="1") checked #end value="1" >
								<label  for="wakeUp">支持提醒</label>
								#end				
							</li>
						</ul>
					</span>
					<span style="float:left; width:98%;height:100px;font:bold 14px;padding-left: 12px;margin:10px 0px 15px 0px;display:none">
						<font>$text.get("customTable.lb.dbOperation")</font>
						<textarea name="fieldCalculate" id="fieldCalculate" style="width:100%;height:80px">$!result.fieldCalculate</textarea>
					</span>
					<span style="float:left; width:98%;height:100px;font:bold 14px;padding-left: 12px;margin:10px 0px 15px 0px;display:none">
						<font>$text.get("customTable.lb.extendButton")</font>
						<textarea name="extendButton" id="extendButton" style="width:100%;height:80px">$!result.extendButton</textarea>
					</span>
					<span style="float:left; width:98%;height:100px;font:bold 14px;padding-left: 12px;margin:10px 0px 15px 0px">
						<font>$text.get("oa.common.remark")</font>
						<textarea name="tableDesc" id="tableDesc" style="width:100%;height:80px">$!result.tableDesc</textarea>
					</span>
					<span style="float:left; width:98%;height:100px;font:bold 14px;padding-left: 12px;margin:10px 0px 15px 0px">
						<font>详情取数语句（当打开详情太慢时，可编写一条SQL语句一次性取出所有数据）</font>
						<textarea name="PopDisSentence" id="PopDisSentence" style="width:100%;height:80px">$!PopDisSentence</textarea>
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
						<tr><td width="900px" style="padding: 0px;">	
						<div id="data_list_idTab" style="overflow-x:auto;padding: 0px;margin: 0px;height: 100%;width: 100%;background: none;">
						<div id="data_list_idTabShang" style="overflow: hidden; padding: 0px; margin: 0px; height: 37px; background: none;">
							<table cellpadding="0" cellspacing="0" style="border: 1px solid #b4b4b4;border-left: 0px;table-layout:fixed" id="FieldInfoIdTitle">
								<thead>
									<tr >
										<td width="30">序号</td>
										<td width="100px" >标识</td>
										<td width="100px">显示名称</td>	
										<td  width="80px" >字段类型</td>	
										<td width="50px">字符长度</td>
										<td width="90px">默认值</td>
										<td width="30px">必填</td>	
										<td width="30px">唯一</td>	
										#if("$!tableType" == "0") <td width="50px" >折行</td>  #else  <td width="50px" >显示宽度</td> #end
																
										<td width="90px">输入类型</td>
										<td  width="200px">选项设置</td>
									</tr>
								</thead>
							</table>
						</div>
						<div id="data_list_idTabXa" style="overflow-y:auto;padding: 0px;margin: 0px;height: 100%;width: 100%;background: none;"  onscroll="scrollDiv(this)">
						<script type="text/javascript">
						var oDiv=document.getElementById("data_list_id");
						var sHeight=document.documentElement.clientHeight-60;
						oDiv.style.height=sHeight+"px";
						var oDiv=document.getElementById("data_info_id");					
						oDiv.style.height=sHeight+"px";
						
						
						var oDiv=document.getElementById("data_list_idTab");
						var sW=document.documentElement.clientWidth-400;
						if(sW >1000){
							sW = 1000;
						}
						oDiv.style.width=sW +"px";
						oDiv.style.height=(sHeight-30)+"px";
						
						var oDiv=document.getElementById("data_list_idTabXa");
						oDiv.style.width=$("#data_list_idTab").width()+"px";
						oDiv.style.height=(sHeight-87)+"px";				
						
						var oDiv=document.getElementById("data_list_idTabShang");
						oDiv.style.width=($("#data_list_idTab").width()-20)+"px";
						
						</script>
						<table cellpadding="0" cellspacing="0" style="border-left: 0px;table-layout:fixed" id="FieldInfoId">	
							<tbody>
							#set($rowCount=0)
							#foreach($row in $result.fieldInfos) 
								#if($row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother")
								#if("$row.fieldName"=="checkPersons" || "$row.fieldName"=="classCode" || "$row.fieldName"=="workFlowNode"
							 		|| "$row.fieldName"=="workFlowNodeName" || "$row.fieldName"=="createBy" || "$row.fieldName"=="lastUpdateTime"
							 		|| "$row.fieldName"=="SCompanyID" || "$row.fieldName"=="statusId" || "$row.fieldName"=="lastUpdateBy"
							 		|| "$row.fieldName"=="createTime" || "$row.fieldName"=="isCatalog" || "$row.fieldName"=="finishTime"
							 		|| "$row.fieldName"=="printCount")
								#else 
								#set($rowCount=$rowCount +1)
								
								<tr  class="c1" onclick="focusRow(this)">
									<td  width="30">$rowCount</td>
									<td width="100px" >
										<input type="hidden" name="fieldId" value="$!row.id"/>
										#if("$!isCopy"=="copy")
										<input type="hidden" name="languageId" value=""/>	
										#else
										<input type="hidden" name="languageId" value="$!row.languageId"/>	
										#end									
										<input type="hidden" name="listOrder" value="$!row.listOrder"/>										
										<input type="hidden" name="isStat" value="$!row.isStat"/>
										<input type="hidden" name="isNull" value="$!row.isNull"/>
										<input type="hidden" name="isUnique" value="$!row.isUnique"/>
										<!-- 小数位数，隐藏不用户改，基本都按系统配置来 -->
										<input type="hidden" name="digits" value="$!row.digits"/>
										<input type="hidden" name="refEnumerationName" value="$!row.refEnumerationName.replaceAll("'", "&apos;")"/>
										<input type="hidden" name="inputValue" value="$!row.inputValue.replaceAll("'", "&apos;")"/>
										<input type="hidden" name="calculate" value="$!row.calculate"/>
										<input type="hidden" name="fieldSysType" value="$!row.fieldSysType"/>
										<input type="hidden" name="fieldIdentityStr" value="$!row.fieldIdentityStr"/>
										<input type="hidden" name="copyType" value="$!row.copyType"/>
										<input type="hidden" name="logicValidate" value="$!row.logicValidate"/>
										<!-- 弹出类型，已无用 -->
										<input type="hidden" name="popupType" value="$!row.popupType"/>
										
										<input type="hidden" name="groupLanguageId" value="$!row.groupName"/>
										<input type="hidden" name="groupName" value="$!row.groupDisplay.toString()"/>
										<!-- 保存时插入表的表名，已经没用 -->
										<input type="hidden" name="insertTable" value="$!row.insertTable"/>
										<input type="hidden" name="isCopy" value="$!row.isCopy"/>
										<input type="hidden" name="isReAudit" value="$!row.isReAudit"/>
										<input type="hidden" name="isLog" value="$!row.isLog"/>
										<input type="hidden" name="inputTypeOld" value="$!row.inputTypeOld"/>
										<input type="hidden" name="isMobile" value="$!row.isMobile"/>
										
										<input type="text" class="changeInput" name="fieldName" value="$!row.fieldName" #if("$!row.id" != "") readonly #end disableautocomplete="true" autocomplete="off"/>
									</td>
									<td width="100px" >
										<input type="hidden"  value="$!row.handerDisplay()" name="fieldDisplay" />
										<input  type="text" id="fieldDisplayDis" name="fieldDisplayDis" value="$!row.display.get("$globals.getLocale()")"  disableautocomplete="true" autocomplete="off"><!-- ondblclick="childmd();"  -->
									</td>
									<td  width="80px">
										<select name="fieldType">
										 #foreach ($erow in $globals.getDS("fieldType"))
										    <option value="$erow.value" #if("$erow.value"=="$!row.fieldType") selected #end>$erow.name</option> 
										 #end
										</select>
									</td>
									<td  width="50px">
										<input type="text" class="changeInput" value="$!row.maxLength" name="maxLength" disableautocomplete="true" autocomplete="off"/>
									</td>
									<td  width="90px">
										<input type="text" class="changeInput" value="$!row.defaultValue" name="defaultValue" disableautocomplete="true" autocomplete="off"/>
									</td>
									<td  width="30px"><input name="isNullId" type="checkbox" style="width: 15px;" #if("1"=="$!row.isNull") checked #end  value="1"></td>
									<td  width="30px"><input name="isUniqueId" type="checkbox" style="width: 15px;" #if("1"=="$!row.isUnique") checked #end  value="1"></td>
									<td  width="50px">
										#if("$!tableType" == "0") 
										<input name="zhWidth" type="checkbox" style="width: 15px;" #if("$!row.width"=="0" && "$!result.id" !="") checked  #end onclick="changeWap(this)" value="200">
										<input type="hidden" value="$!row.width" name="width"/>
										#else  
										<input type="text" class="changeInput" value="$!row.width" name="width" disableautocomplete="true" autocomplete="off"/>
										#end
									</td>
									<td  width="90px">
										<select name="inputType" onchange="changeInput(this)">
										 #foreach ($erow in $globals.getDS("inputType"))
										    <option value="$erow.value" #if("$erow.value"=="$!row.inputType") selected #end>$erow.name</option> 
										 #end
										</select>
									</td>
									<td   width="200px" align="left">
										
									</td>
								</tr> 
								#end
								#end
								#end
							</tbody>
						</table>
						</div>
						</div>
						</td>
						<td width="*" style="padding: 0px;min-width: 200px;">
							<ul class="dataListUL" id="advPropDiv" style="display:none">
								<li class="titleLi">字段高级属性</li>
								<li>
									<input name="isStatId" type="checkbox" id="isStatId" style="width: 15px;margin-left:5px"  title="明细表数值字段有效，录入明细时统计数量"  value="1">
									<label  title="明细表数值字段有效，录入明细时统计数量" for="isStatId"  style="width: 25px;margin-left: 0px">统计</label>
									<input name="isLogId" type="checkbox" id="isLogId" style="width: 15px;margin-left:5px"  title="单据添加删除时本字段是否记录到操作日志中"  value="1">
									<label  title="单据添加删除时本字段是否记录到操作日志中" for="isLogId"  style="width: 25px;margin-left: 0px">记录日志</label>
								</li>
								<li title="数字字段有效">																
									<input name="calculateBt" type="button" id="calculateBt" onclick="showCalculate()"  style="width: 175px;" value="编辑计算公式">
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
									<input name="groupNameId" type="text" id="groupNameId" ondblclick="groupmd()" disableautocomplete="true" autocomplete="off">
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
									<label  title="明细表行复制时本字段是否可复制" for="isCopyId" style="width: 20px;margin-left: 0px">复制</label>
									<input name="isReAuditId" type="checkbox" id="isReAuditId" style="width: 15px;margin-left:20px"  title="字段是否可以复核"  value="1">
									<label  title="字段是否可以复核" for="isReAuditId"  style="width: 20px;margin-left: 0px">复核</label>
									<input name="isMobileId" type="checkbox" id="isMobileId" style="width: 15px;margin-left:20px"  title="字段是否在手机端显示"  value="2">
									<label  title="字段是否在手机端显示" for="isMobileId"  style="width: 20px;margin-left: 0px">手机</label>
									
								</li>
							</ul>
						</td>
						</tr></table>
					</div>
					<div id="data_child_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;Display:none" >
						<span style="float:left; width:100%;font:bold 14px;height: 30px;padding-top: 5px;">
							<font style="color:#3918E9;width: 660px;display: inline-block;padding-left: 20px;">注：添加修改明细表后，要刷新本界面才能看到</font>
							<button type="button" class="bu_02" onclick="addChild('1')">添加明细表</button>
						</span>
						<table cellpadding="0" cellspacing="0" style="border: 1px solid #b4b4b4;border-left: 0px;table-layout:fixed;margin-bottom: 30px;" class="childTable">
							<thead>
								<tr>
									<td width="30">序号</td>
									<td width="150px" >标识</td>
									<td width="150px">显示名称</td>	
									<td  width="50px" >修改</td>	
									<td  width="50px" >删除</td>	
								</tr>
							</thead>
							<tbody>
							#foreach($row in $childList)
								<tr id="childList_$globals.get($row,1)">
									<td >$velocityCount</td>
									<td >$globals.get($row,1)</td>
									<td >$globals.get($row,2)</td>	
									<td ><a href="javascript:updateChild('$globals.get($row,1)')">修改</a></td>	
									<td ><a href="javascript:delChild('$!result.tableName','$globals.get($row,1)','childList_$globals.get($row,1)')">删除</a></td>	
								</tr>
							#end	
							</tbody>
						</table>
					</div>
					<div id="data_brother_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;Display:none" >
						<span style="float:left; width:100%;font:bold 14px;height: 30px;padding-top: 5px;">
							<font style="color:#3918E9;width: 570px;display: inline-block;padding-left: 20px;">注：添加修邻居表后，要刷新本界面才能看到</font>
							<button type="button" class="bu_02" onclick="addChild('2')">添加邻居表</button>
							<button type="button" class="bu_02" onclick="selectChild('2')">选择邻居表</button>
						</span>
						<table cellpadding="0" cellspacing="0" style="border: 1px solid #b4b4b4;border-left: 0px;table-layout:fixed;margin-bottom: 30px;" class="childTable">
							<thead>
								<tr>
									<td width="30">序号</td>
									<td width="150px" >标识</td>
									<td width="150px">显示名称</td>	
									<td  width="50px" >修改</td>	
									<td  width="50px" >删除</td>	
								</tr>
							</thead>
							<tbody>
							#foreach($row in $brotherList)
								<tr id="childList_$globals.get($row,1)">
									<td >$velocityCount</td>
									<td >$globals.get($row,1)</td>
									<td >$globals.get($row,2)</td>	
									<td ><a href="javascript:updateChild('$globals.get($row,1)')">修改</a></td>	
									<td ><a href="javascript:delChild('$!result.tableName','$globals.get($row,1)','childList_$globals.get($row,1)')">删除</a></td>	
								</tr>
							#end	
							</tbody>
						</table>
					</div>
				</td>				
			</tr>
		</table>
		</form>
			</td>
		</tr>
	</table>
</body>
<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>
</html>
