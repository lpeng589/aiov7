<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/workflow2.css" />
<link type="text/css" rel="stylesheet" href="/style/themes/default/easyui.css" />
<style type="text/css">
.column li{width:48%}
.column li input{font-family:microsoft yahei;font-size:12px;}
#b li{float:none;height:100%;}
</style>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">
var moduleType = "$!moduleType";
$(function(){
	if(typeof(top.junblockUI)!="undefined"){
	  top.junblockUI();
   	}
	$('input').each(function () { 
    	if ($(this).attr('required') || $(this).attr('validType')) 
		$(this).validatebox(); 
    });
     jQuery.extend(jQuery.fn.validatebox.defaults.rules, {
	    isNum: {
	        validator: function(value,param){
	            return /^\+?[0-9][0-9]*$/.test(value);
	        },
	        message: '此字段必须是整数！'
   	    }
	 });
 });
 
putValidateItem("templateName","$text.get("oa.workflow.lb.tempName")","any","",false,0,100);
#if("$!type" == "1")
putValidateItem("templateFile","$text.get("oa.workflow.lb.tempFile")","any","",false,0,100);	
putValidateItem("titleTemp","$text.get("oa.workflow.TopicModule")","any","",false,0,100);
#end

function beforSubmit(form){
	if(!validate(form))return false;
	//先进行逻辑验证表单设计和流程设计是否符合规范






    var url="/UtilServlet?operation=workFlowIsStand&tempId=$!globals.get($result,0)";
    AjaxRequest(url) ;
    if("false"==response&&!confirm("$text.get("oa.workflow.designNoStand")")){
    	if(typeof(top.junblockUI)!="undefined"){
			top.junblockUI();
		}
    	return false;
    }
	disableForm(form);
	return true;
}

function m(value){
	return document.getElementById(value) ;
}

function jblockUI(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({message: "<img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
	}
}

//查看版本（流程版本和表单版本）  
function openVersion(type,designId){
	var urlstr = "/OAWorkFlowTempQueryAction.do?queryType=version&designId="+designId+"&tableName=$!globals.get($result,4)&versionType="+type+"&moduleType="+moduleType;
	var title="流程版本";
	if(type=="table"){
		title="表单版本";
	}
	asyncbox.open({id:'flowVersion',title:title,url:urlstr,width:500,height:400});
}

//判断物理表是否存在  
function isExistTable(){
	var name=$("#templateFile").val();
	var flag=true;
	jQuery.ajax({
		type: "POST",
	    url: "/OAWorkFlowTempAction.do",
		data: "operation=7&isExistTable=true&tableName="+name+"&moduleType="+moduleType,
		async: false,
	    success: function(msg){
			if(msg=="false"){
				alert("表单不能为空，请先选择表单!");
				flag = false;
			}
		}
	});
	return flag;
}

//流程设计
var globalFileName="";
function openFlowSet(){
	if(!isExistTable()){
		return false;
	}

	var newCreate="false";
	var workflowFile=$("#workFlowFile").val();
	#if("$!newCreate"=="true")
		newCreate="true";
	#end
	
	var str="/UtilServlet?operation=BillNotFinish&keyId=$!globals.get($result,0)";
 	AjaxRequest(str);
    var value = response;   
    if(value!="no response text" && value.length>0){
    	　asyncbox.open({
		　　　html : '<div style="padding:20px">存在未审核工作流，修改流程可能导致这些工作流无法继续执行，是否创建新的流程，并让原工作流继续按旧流程执行？</div>',
		　　　width : 400,
		　　　height : 150,	
			 title :'提示',	　　　
		　　　btnsbar : [{text    : '创建新版本',action  : 'newversion'},{text    : '修改原版本',action  : 'oldversion'},{text    : '取消',action  : 'cancel'}],
		　　　callback : function(action){
		　　　　　if(action == 'newversion'){
					var data =jQuery.ajax({url:"/OAWorkFlowTempAction.do?operation=1&type=copy&winCurIndex=$!winCurIndex&keyId=$!globals.get($result,0)&moduleType="+moduleType,async: false}).responseText;
					if(data.indexOf("ERROR:")==0){
						alert(data.substring(6));
					}else{
						keyId = data;
						if(document.getElementById("templateFile").value.length>0){
							window.open("/OAWorkFlowTempAction.do?queryType=design&ip=$host.replace('|',':')&keyId="+keyId+"&tableName="+document.getElementById("templateFile").value+"&newCreate="+newCreate+"&moduleType="+moduleType);
						}
						window.location.href='/OAWorkFlowTempAction.do?operation=7&keyId='+keyId+'&winCurIndex=$!winCurIndex&selectType=$!selectType&val=$!val&moduleType='+moduleType;
					}
    				return ;
		　　　　　}else if(action == 'oldversion'){
					if(document.getElementById("templateFile").value.length>0){
						var url="/OAWorkFlowTempAction.do?queryType=design&keyId=$!globals.get($result,0)&ip=$host.replace('|',':')&workFileName=$!{globals.get($result,0)}.xml&tableName="+document.getElementById("templateFile").value+"&newCreate="+newCreate+"&moduleType="+moduleType;
						window.open(url);
					}					
					return;
		　　　　　}else {
		　　　　　　　return;
		　　　　　}
		　　　}
		　});
    }else{ 
		if(document.getElementById("templateFile").value.length>0){
			var url="/OAWorkFlowTempAction.do?queryType=design&keyId=$!globals.get($result,0)&ip=$host.replace('|',':')&workFileName=$!{globals.get($result,0)}.xml&tableName="+document.getElementById("templateFile").value+"&newCreate="+newCreate+"&moduleType="+moduleType;
			window.open(url);
		}
	}
}
//个性化界面设计
function opLayoutHTML(){
	var tableCHName="$!globals.get($result,1)";
	var url="";
	if($("#flag").val() == "false"){
		alert("请先设计工作流表单");
	}else{
		url="/OAWorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&turnType=layoutHTML&designId=$!globals.get($result,0)&tableName=$!globals.get($result,4)&tableCHName="+encodeURI(tableCHName)+"&type=simple";
		window.open(url);
	}
}

//新的个性化表单设计
function opTempFileNew(){
	var tableCHName="$!globals.get($result,1)";
	var url="";
	if($("#flag").val() == "false"){ //添加表单
		url="/OAWorkFlowTableAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&designId=$!globals.get($result,0)&tableName=$!globals.get($result,4)&tableCHName="+encodeURI(tableCHName)+"&type=simple";
	}else{ //修改表单
		url="/OAWorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&turnType=page&designId=$!globals.get($result,0)&tableName=$!globals.get($result,4)&tableCHName="+encodeURI(tableCHName)+"&type=simple";
	}
	window.open(url);
}

//自定义表单设计  
function opTempFileAdd(){
	var tableCHName="$!globals.get($result,1)";
	var urls="/WorkFlowTableAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&tableName=$!globals.get($result,4)&tableCHName="+encodeURI(tableCHName)+"&type=simple";
    
    width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	openPop('TablemgtDiv','添加表单',urls,width,height,true,true);
    window.open(url);
}

//自定义表单修改  
function opTempFileUpdate(templateType,designId){
	if($("#flag").val() == "false"){
		opTempFileAdd();
		return;
	}else{
		tableName = $("#templateFile").val();		
		//如果表单名称以Flow_开头，说明是简单结构，否则要调高级方式去修改，可能导致没权限问题  
		simpleType = '';
		if(tableName.indexOf("Flow") == 0){ //简单表结构，调简单结构修改方法
			simpleType = "simple";
		}
		
		urls = "/WorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId="+tableName+"&type="+simpleType;
		
		width=document.documentElement.clientWidth;
		height=document.documentElement.clientHeight;
		openPop('TablemgtDiv','添加表单',urls,width,height,true,true);
		if(templateType == 2){
			window.open("WorkFlowTableAction.do?operation=16&designId="+designId+"&keyId="+tableName+"&type=simple");
		}else{
	   		 window.open(url);
		}
	}
}

//查找表单，模版字段调用方法   
var globalhiddenName;
var globalShowName;
var globalSelectName;
function openPopup(hiddenName,showName,selectName){
	var displayName="$text.get("workflow.lb.fiowtable")" ;
	var tableName=$("#templateFile").val();
	if(selectName=="tableSelect"){
		tableName="";
	}
	var urlstr = "/UserFunctionAction.do?operation=22&tableName="+tableName+"&selectName="+selectName+"&popupWin=Popdiv&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName ;
	asyncbox.open({id:'Popdiv',title:displayName,url:urlstr,width:750,height:470});
	globalhiddenName=hiddenName;
	globalShowName=showName;
	globalSelectName=selectName;
}

function exePopdiv(returnValue){
	var showName="";
	var hiddenName="";
	var strList = returnValue.split("#|#") ;
	
	if(returnValue=="#;#"+"#;#"){ //jsp文件中两个#在一起会当velocity的注释号
		$("#"+globalhiddenName).val("");
		$("#"+globalShowName).val("");
		return false;
	}
	for(var j=0;j<strList.length;j++){
		var field = strList[j].split("#;#") ;
		if(field!=""){
			if(globalSelectName=="mobileContentSelect"){ //手机模版显示内容
				showName=field[1];
			}else if(globalSelectName=="tableSelect"){ //查找流程表单
				showName=field[1];
				hiddenName=field[0];
			}else{ //网页模版主题
				showName+="["+field[1]+"]";	
			}
		}
	}
	if(globalSelectName=="mobileContentSelect"){  //手机模版显示内容
		$("#"+globalShowName).val(showName);
	}else if(globalSelectName=="tableSelect"){ //查找流程表单
		$("#"+globalShowName).val(showName);
		$("#"+globalhiddenName).val(hiddenName);
	}else{
		$("#"+globalShowName).val($("#"+globalShowName).val()+showName);
	}
	
	//不明白，这里有tableName还搞个templateFile.暂时这里保证通一
	$("#tableName").val($("#templateFile").val());
}

function timeChange(timeType){
	if(timeType.value=="0"&&timeType.checked){
		$("#tempMsg").html("$!text.get("OAWorkFlow.lb.timeTypeDet1")");
	}else if(timeType.value=="1"&&timeType.checked){
		$("#tempMsg").html("$!text.get("OAWorkFlow.lb.timeTypeDet2")");
	}else if(timeType.value=="2"&&timeType.checked){
		$("#tempMsg").html("$!text.get("OAWorkFlow.lb.timeTypeDet3")");
	}
}

function showDiv(){
	if(m('alertDivId').style.display=='block'){
		m('alertDivId').style.display='none';
		m('alertStatus').value='-1';
	}else{
		m('alertDivId').style.display='block';
		m('alertStatus').value='0';
	}
}

//隐藏/显示菜单
function showMenu(selectid){
	var id="#img_"+selectid;
	var theUl="#"+selectid;
	if($(theUl).css("display") == "block"){
		$(id).removeClass();
		$(id).addClass("column_a");
		$(theUl).hide();
		
	}else{
		$(id).removeClass();
		$(id).addClass("column_b");
		$(theUl).show();
	}
}

//检查页面必填内容






function checkForm(){
	var flag = true;
	$("input").each(function () {
	   	if($(this).attr('required') || $(this).attr('validType')) {
		   	if(!$(this).validatebox('isValid')) {
		   		$(this).focus();
		     	 flag=false; 
		     	 return flag;
		   	}
	   }
	});
	return flag;
}

//保存
function subForm(){
 	if($("input[name='autoPass']").attr("checked")=="checked"){
    	$("input[name='autoPass']").val("true");
    }
    else{
    	$("input[name='autoPass']").val("false");
    }
	$("#titleTemp").val($("#filetitleTemp").val());
	if(checkForm()==true){
		#if($!globals.get($result,3).indexOf("00002")!="0")
		var userVal=form.popedomUserIds.value.replaceAll(",","");
   		var deptVal=form.popedomDeptIds.value.replaceAll(",","");
		if(userVal.length==0 && deptVal.length==0){
	    	alert("反审核人未设置");
	    	if(typeof(top.junblockUI)!="undefined"){
				top.junblockUI();
			}
	    	return false;
    	}
    	#end
		document.form.submit();
		$("#subButton").attr("disabled","disabled");
		jblockUI();
	}
}

//返回
function forback(){
　　 if(confirm("$text.get('oa.common.edit.content')")){
		window.save=false;
		window.location.href='/OAWorkFlowTempQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex&selectType=$!selectType&val=$!val'+"&moduleType="+moduleType;
   }
}

//流程设计页面关闭后，调用此方法






function repVal(fileName){
	jQuery.ajax({
		type: "POST",
	    url: "/OAWorkFlowTempAction.do",
		data: "operation=7&isExistFile=true&fileName="+fileName+""+"&moduleType="+moduleType,
		async: false,
	    success: function(msg){
			if(msg=="true"){
				$("#workFlowFile").val(fileName);
				globalFileName=fileName;
			}
		}
	});
}

function showGraduate(){
	var urls = '/vm/crm/client/titleOpenSel.jsp' ;
	asyncbox.open({
		id:'titleOpenId',title :'选择职位',url:urls,cache:false,width:300,height:290, btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
	　　　　if(action == 'ok'){
				var str = opener.returnVal();
				newOpenSelect(str,'empTitle','empTitleIds',1);
	　　　	}
	　　}
　	});
}

function changeuserType(type){
	if(type==0){
		$("#userTypeDiv",document).hide();
	}else{
		$("#userTypeDiv",document).show();
	}
}

</script>
</head>
<body onLoad="showStatus();">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/OAWorkFlowTempAction.do" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/>
<input type="hidden" name="winCurIndex" value="$!winCurIndex" />
<input type="hidden" name="templateType" value="$!type" />
<input type="hidden" name="selectType" value="$!selectType" />
<input type="hidden" name="val" value="$!val" />
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="$!targetUserStr"/>
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="$!targetDeptStr"/>
<input type="hidden" name="EmpGroupId" id="EmpGroupId" value="$!targetEmpGroupStr"/>
<input type="hidden" name="monitorDeptIds" id="monitorDeptIds" value="$!globals.get($result,15)"/>
<input type="hidden" name="monitorUserIds" id="monitorUserIds" value="$!globals.get($result,16)"/>
<input type="hidden" name="empTitleIds" id="empTitleIds" value="$!empTitleIds"/>
<input type="hidden" name="tableName" id="tableName" value="$!globals.get($result,4)"/>
<input type="hidden" name="tableCHName" id="tableCHName" value="$!globals.get($result,1)"/>
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>

<!-- -流程是否已经存在表单 -->
<input type="hidden" name="flag" id="flag" value="$!flag"/> 
<!-- 自定义表单还是个性化表单 -->
<input type="hidden" name="designType" id="designType" value="$!globals.get($result,43)"/>

<input type="hidden" name="templateFile" id="templateFile" value="$!globals.get($result,4)"/>
<input type="hidden" name="workFlowFile" id="workFlowFile" value="$!workFileName"/>
<input type="hidden" name="id" id="id" value="$!globals.get($result,0)"/>
<input type="hidden" name="fromPage" id="fromPage" value="$!fromPage"/>

<table cellpadding="0" cellspacing="0" class="framework" style="table-layout:fixed;">
			<!--右边列表开始-->
			<tr>
			<td>
				<div class="TopTitle">
					<span><img src="/style1/images/ti_002.gif" /></span>
					<span>修改工作流</span>
					
					<div>
						<input type="button" class="bu_02" onclick="subForm();"    id="subButton"  value="保存" />						
											
						#if("$globals.get($result,43)"!="1" && $globals.get($result,3).indexOf("00002")==0 )  <!-- 自定义设计需要显示选中表单 -->
							<input type="button" class="bu_02" onclick=" opTempFileUpdate($globals.get($result,2),'$globals.get($result,0)');" value="$text.get("oa.workflow.title.tempFile")" />
							<input type="button" class="bu_02" onclick=" opLayoutHTML()" value="个性化界面" />
							
						#elseif("$globals.get($result,43)"=="1") ##老版本的 个性化的表单设计，43代表designType，为1是老版个性化										
							<input type="button" class="bu_02" onclick="opTempFileNew();" value="$text.get("oa.workflow.title.tempFile")" />
							<input type="button" class="bu_02"  onclick="if(!isExistTable()) return false;openVersion('table','$!globals.get($result,0)');" value="表单版本"/>
						#end						
						##流程设计			
						<input type="button"  class="bu_02" onClick="openFlowSet()" value="$text.get("oa.workflow.lb.workflowSet")" />
						#if("$!globals.get($result,0)"!="$!globals.get($result,39)")
							<input type="button" class="bu_02"  onclick="openVersion('flow','');" value="$text.get("com.flow.template.version")"/>
						#end
						
						#if("$!fromPage"!="table")
						<input type="button" class="bu_02" onclick="forback();"  value="返回" />	
						#end
					</div>
				</div>
				<div id="cc" class="data" style="overflow:hidden;overflow-y:auto;width:100%;">
				<script type="text/javascript">
					$("#cc").height($(window).height()-55);
				</script>
				<div id="flowPicDesc" class="flowPicDesc">
				<ul >
						<li class="flowli"><a class="flowpc">添加工作流设计</a></li>
						<li class="flowarrowli" ></li>
						#if($globals.get($result,3).indexOf("00002")==0 )
						<li class="flowli"><a #if("$!flag"=="true") class="flowpc" #else class="flowp" #end 
							onclick="#if("$globals.get($result,43)"!="1") opTempFileUpdate($globals.get($result,2),'$globals.get($result,0)'); #else opTempFileNew();  #end">设计工作流表单</a></li>
						<li class="flowarrowli"></li>
						<li class="flowli"><a #if("$!flag"=="true") class="flowpc" #else class="flowp" #end 
							onclick="#if("$globals.get($result,43)"!="1") opLayoutHTML(); #else opTempFileNew();  #end ">个性化界面(可略)</a></li>
						<li class="flowarrowli"></li>	
						#end				
						<li class="flowli"><a  #if("$!newCreate"=="false") class="flowpc" #else class="flowp" #end 
							onclick="openFlowSet();">设计工作流流程</a></li>
						<li class="flowarrowli"></li>
						<li class="flowli"><a #if("$!globals.get($result,17)"!="") class="flowpc" #else class="flowp" #end>完善资料(可略)</a></li>
				</ul>	
				</div>
				<div class="column_title" onclick="showMenu('a');"><div class="column_b"  id="img_a" >流程基本属性</div></div>
					<ul class="column" id="a" style="display: block;">
						<li>
							<label style="color:#f13d3d;">$text.get("oa.workflow.lb.tempName")：</label>
							<span>
								<input name="templateName" type="text" value="$!globals.get($result,1)" style="width:135px" class="easyui-validatebox" required="true"/>
							</span>
						</li>

						<li>
							<label style="color:#f13d3d;">$text.get("oa.workflow.lb.tempClass")：</label>
							<span>
								<select name="templateClass" style="width:135px"  onKeyDown="if(event.keyCode==13) event.keyCode=9">
									#foreach($row in $typeListbycata)
										<option id="tempClass" value="$globals.get($row,1)" #if("$!globals.get($result,3)"=="$globals.get($row,1)") selected #end >$globals.get($row,2)</option>
									#end
								</select>
							</span>
						</li>
						<li>
							<label>$text.get("oa.workflow.lb.tempStatus")：</label>
							<span>
								<select name="templateStatus" style="width:135px"  onKeyDown="if(event.keyCode==13) event.keyCode=9">
									<option value="1" #if("$!globals.get($result,7)"=="1") selected #end>$text.get("wokeflow.lb.enable")</option>
									<option value="0" #if("$!globals.get($result,7)"=="0") selected #end>$text.get("wokeflow.lb.noEnable")</option>
								</select>
							</span>
						</li>
						<li>
							<label>$text.get("oaWorkFlow.lb.flowOrder")：</label>
							<span>
								<input name="flowOrder" style="width:135px" type="text" class="text" onKeyDown="if(event.keyCode==13) event.keyCode=9" value="$!globals.get($result,9)" >	
							</span>
						</li>
						#if($!globals.get($result,3).indexOf("00002")!="0")
						<li>
								<label>默认查询状态：</label>
								<span>
								<select name="defStatus" style="width:135px"  onKeyDown="if(event.keyCode==13) event.keyCode=9">
									<option value="all" #if("$!globals.get($result,44)"=="all") selected #end>全部</option>
									<option value="notApprove" #if("$!globals.get($result,44)"=="notApprove") selected #end>审核中</option>
								</select>
							</span>
						</li>
						#end
						<li>
							<label  style="color:#f13d3d;">&nbsp;</label>		
							<input type="checkbox" name="autoPass" id="autoPass" #if("$!globals.get($result,31)"=="true") checked="checked" #end value=""/> 
							<label for="autoPass" style="float: none;">$text.get("workflow.msg.AutomatismFiltration")</label>
						</li>
					</ul>
					
					<div class="column_title" onclick="showMenu('f');"><div class="column_a" id="img_f"  >模板信息</div></div>
					<ul class="column" id="f" style="display: none;">
						<li style="width:98%;" >
							<label >$text.get("oa.workflow.TopicModule")：</label>
							<span class="pr">
								<input type="hidden" id="titleTemp" name="titleTemp" value="$!globals.get($result,17)"/>
								<input id="filetitleTemp" name="filetitleTemp" style="width:400px" type='text' class="text" onDblClick="if(!isExistTable()) return false;openPopup('titleTemp','filetitleTemp','mobileContentSelect2')"  class="easyui-validatebox" value="$!globals.get($result,17)";/>
								<b class="mb-zt icon16 b-read" onClick="if(!isExistTable()) return false;openPopup('titleTemp','filetitleTemp','mobileContentSelect2')"></b>
								<font color="dimgray">$text.get("com.template.title.remark")</font>
							</span>
						</li>
						<li style="width:80%;"><label>模板内容字段：</label>
							<span class="pr">
								<input type='hidden' id="fileContent" name='fileContent' value='$!globals.get($result,40)'/>
								<input id="fileContentDisplay" name='fileContentDisplay'  style="width:400px"  type='text' class="text" onDblClick="if(!isExistTable()) return false;openPopup('fileContent','fileContentDisplay','mobileContentSelect')" value="$!fileContentDisplay"/> 
								<b class="mb-zt icon16 b-read" onClick="if(!isExistTable()) return false;openPopup('fileContent','fileContentDisplay','mobileContentSelect')"></b>
								<font color="dimgray">$text.get("com.template.content.remark")</font>
							</span>
						</li> 
					</ul>

					<div class="column_title" onclick="showMenu('d');"><div class="column_a" id="img_d"  >流程用户及监控</div></div>
					<div  id="d" style="display: none;">
					<table width="100%" >
						#if($!globals.get($result,3).indexOf("00002")=="0")
						<tr>
							<td width="133px" style="vertical-align: middle;" align="right">
							$text.get("oa.workflow.lb.allowVisitor")：    
							</td>
							<td>
								##未指定任何数据视为所有用户  
								#set($userType = 0)
								#if($!targetDept.size()>0 ||$!targetUsers.size()>0 ||$!empTitleIds.length()>1 ||$!targetEmpGroup.size()>0)
								#set($userType = 1)
								#end
								<input type="radio" id="userType0" name="userType" #if($userType == 0) checked #end value="0" onclick="changeuserType(0)" ><label for="userType0">所有用户</label> 
								<input type="radio" id="userType1" name="userType" #if($userType == 1) checked #end  value="1" onclick="changeuserType(1)"><label for="userType1">指定用户</label> 
								
							</td>
						</tr>
						
						<tr id="userTypeDiv"  #if($userType == 0) style="display:none" #end >
							<td width="133px" style="vertical-align: middle;" align="right">							
							</td>
							<td >
								<div class="oa_signDocument1 wk-fl" id="_context">
									<div class="hBtns d-slt" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');">
										$text.get("oa.select.dept")
									</div>
									<select class="s-slt" name="formatDeptName" id="formatDeptName" multiple="multiple">
										#foreach($dept in $targetDept)
											<option value="$dept.classCode">$!dept.DeptFullName</option>
										#end
									</select>
									<b class="icons" onClick="deleteOpation('formatDeptName','popedomDeptIds')"></b>
								</div>
								<div class="oa_signDocument1 wk-fl" id="_context">
									<div class="hBtns d-slt" title='$text.get("oa.select.personal")' onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');">
										  $text.get("oa.select.personal")
									</div>
									<select class="s-slt" name="formatFileName" id="formatFileName" multiple="multiple">
										#foreach($user in $targetUsers)
											<option value="$user.id">$!user.EmpFullName</option>
										#end
									</select>
									<b class="icons" onClick="deleteOpation('formatFileName','popedomUserIds')"></b>
								</div>
								<div class="oa_signDocument1 wk-fl" id="_context">
									<div class="hBtns d-slt" title="职位" onClick="showGraduate();">
										职位
									</div>
									<select class="s-slt" name="empTitle" id="empTitle" multiple="multiple">
										#foreach($title in $globals.getEnumerationItems("duty"))
										#if($empTitleIds.indexOf("$!title.value,")!="-1")
											<option value="$!title.value" >$!title.name</option>
										#end
										#end
									</select>
									<b class="icons" onClick="deleteOpation('empTitle','empTitleIds')"></b>
								</div>
								<div class="oa_signDocument1 wk-fl" style="display:none" id="_context">
									<div class="hBtns d-slt" title="$text.get('oa.oamessage.usergroup')" onClick="deptPop('empGroup','EmpGroup','EmpGroupId','1');">
										$text.get("oa.oamessage.usergroup")
									</div>
									<select class="s-slt" name="EmpGroup" id="EmpGroup" multiple="multiple">
										#foreach($grp in $targetEmpGroup)
											<option value="$!globals.get($grp,0)">$!globals.get($grp,1)</option>
										#end
									</select>
									<b class="icons" onClick="deleteOpation('EmpGroup','EmpGroupId')"></b>
								</div>
							</td>
						</tr>
						#end
						
						<tr><td>&nbsp;</td><td>&nbsp;</td></tr>							
						<tr>
							<td style="vertical-align: middle;" align="right">
							$text.get("workflow.lb.monitor")
							</td>
							<td>
								<div class="oa_signDocument1 wk-fl" id="_context">									
									<div class="hBtns d-slt" title="选择监控部门" onClick="deptPop('deptGroup','depMonitor','monitorDeptIds','1');">
										$text.get("workflow.lb.AllowMonitorDept")
									</div>
									<select class="s-slt" name="depMonitor" id="depMonitor" multiple="multiple">
									#foreach($dept in $monDeptDis)
										<option value="$dept.classCode">$!dept.DeptFullName</option>
									#end
									</select>
									<b class="icons" style="top:55px;" onClick="deleteOpation('depMonitor','monitorDeptIds')"></b>
								</div>
								
								<div class="oa_signDocument1 wk-fl" id="_context">									
									<div class="hBtns d-slt" title="选择监控人员" onClick="deptPop('userGroup','perMonitor','monitorUserIds','1');">
										$text.get("workflow.lb.AllowMonitorEmp")
									</div>
									<select class="s-slt" name="perMonitor" id="perMonitor" multiple="multiple">
									#foreach($user in $monUserDis)
										<option value="$user.id">$!user.EmpFullName</option>
									#end
									</select>
									<b class="icons" style="top:55px;" onClick="deleteOpation('perMonitor','monitorUserIds')"></b>
								</div>
							</td>							
						</tr>
						<tr>
							<td></td>
							<td><!-- 原有的部分和人员分开设置监控范围的功能取消 -->
								<input type="checkbox" name="perMonitorScope"  id="perMonitorScope" #if("$!globals.get($result,14)"=="1") checked="checked" #end value="1"/> 
								<label for="perMonitorScope">只允许监控用户所在部门及下级部门</label>
							</td>
						</tr>
						  
					</table>
					</div>
					<div class="column_title" onclick="showMenu('e');"><div class="column_a" id="img_e" >流程提醒</div></div>
					<div id="e" style="display:none;">
					<table  width="100%"  style="padding: 5px;">
						
						<tr>
						<td width="133px" style="vertical-align: middle;" align="right"> 节点提醒：</td>
						<td>
							    <div class="d-cbox-wp">
									<span class="pa-span">$text.get("workflow.lb.nextNote")</span>
									#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
							 		<lable class="l-cbox">
							 			<input class="cbox" type="checkbox" id="nextWake_$row_wakeUpType.value" name="nextWake" value="$row_wakeUpType.value" #if($!globals.get($result,18).indexOf("$row_wakeUpType.value")>=0)checked #end/>
							 			<label  for="nextWake_$row_wakeUpType.value">$row_wakeUpType.name</label>
							 		</lable>
						   			#end								
								</div>
								<div class="d-cbox-wp">
									<span class="pa-span">$text.get("workflow.lb.startNote")</span>									 
									#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
						 			<lable class="l-cbox">
						 				<input class="cbox" type="checkbox" id="startWake_$row_wakeUpType.value" name="startWake" value="$row_wakeUpType.value" #if($!globals.get($result,19).indexOf("$row_wakeUpType.value")>=0)checked #end/>
						 				<label  for="startWake_$row_wakeUpType.value">$row_wakeUpType.name</label>
					   				</lable>
					   				#end					
								</div>
								<div class="d-cbox-wp">
									<span class="pa-span">$text.get("workflow.lb.allNote")</span>
									#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
									<lable class="l-cbox">	
						 				<input class="cbox" type="checkbox" id="allWake_$row_wakeUpType.value" name="allWake" value="$row_wakeUpType.value" #if($!globals.get($result,20).indexOf("$row_wakeUpType.value")>=0)checked #end/>
						 				<label  for="allWake_$row_wakeUpType.value">$row_wakeUpType.name</label>
						 			</lable>
					   				#end
								</div>
								<div class="d-cbox-wp">
									<span class="pa-span">$text.get("workflow.lb.setNote")</span>
									#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
									<lable class="l-cbox">
						 				<input class="cbox" type="checkbox" id="setWake_$row_wakeUpType.value" name="setWake" value="$row_wakeUpType.value" #if($!globals.get($result,21).indexOf("$row_wakeUpType.value")>=0)checked #end />
						 				<label  for="setWake_$row_wakeUpType.value">$row_wakeUpType.name</label>
						 			</lable>
					   				#end								
								</div>
								</td>
								</tr>
								
								<tr class="td-bt">
									<td width="133px" style="vertical-align: middle;" align="right"></td>
								<td>
								<li>
									<span style="border-top:1px ridge dashed #CCCCCC">
									<div class="oa_signDocument1 wk-fl" id="_context">
						   				<input type="hidden" id="setWakeDept" name="setWakeDept" value="$!globals.get($result,22)"/>
										<div class="hBtns d-slt" onClick="deptPop('deptGroup','setWakeDeptName','setWakeDept','1');" title="$text.get("oa.select.dept")">
											$text.get("oa.select.dept")
										</div>
										<select class="s-slt" name="setWakeDeptName" id="setWakeDeptName" multiple="multiple">
											#foreach($dept in $setWakeDept)
											<option value="$dept.classCode">$!dept.DeptFullName</option>
											#end
										</select>
										<b class="icons" onClick="deleteOpation('setWakeDeptName','setWakeDept')" ></b>
									</div>
									<div class="oa_signDocument1 wk-fl" id="_context">
										<input type="hidden" id="setWakePer" name="setWakePer" value="$!globals.get($result,23)"/>
										<div class="hBtns d-slt" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','setWakePerName','setWakePer','1');">
											$text.get("oa.select.personal")
										</div>
										<select class="s-slt" name="setWakePerName" id="setWakePerName" multiple="multiple">
											#foreach($user in $setWakePer)
											<option value="$user.id">$!user.EmpFullName</option>
											#end
										</select>
										<b class="icons" onClick="deleteOpation('setWakePerName','setWakePer')"></b>
									</div>
								</li>
						</td>	
						</tr>
						<tr class="td-bt" style="background:#f2f2f2;">
						<td style="vertical-align: middle;" width="133px"  align="right" > 超时提醒：</td>
						<td>
							<div class="d-cbox-wp">
								<span class="pa-span"></span>
								#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
								<lable class="l-cbox">
									<input class="cbox" type="checkbox" id="overTimeWake_$row_wakeUpType.value" name="overTimeWake" value="$row_wakeUpType.value" #if($!globals.get($result,45).indexOf("$row_wakeUpType.value")>=0)checked #end/>
									<label  for="overTimeWake_$row_wakeUpType.value">$row_wakeUpType.name</label>
								</lable>
								#end								
							</div>
						</td>
						</tr>
						<tr class="td-bt" style="background:#f2f2f2;">
						<td style="vertical-align: middle;" width="133px"  align="right" > 分发提醒：</td>
						<td>
							<div class="d-cbox-wp">
								<span class="pa-span"></span>
								#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
								<lable class="l-cbox">
									<input class="cbox" type="checkbox" id="dispenseWake_$row_wakeUpType.value" name="dispenseWake" value="$row_wakeUpType.value" #if($!globals.get($result,41).indexOf("$row_wakeUpType.value")>=0)checked #end/>
									<label  for="dispenseWake_$row_wakeUpType.value">$row_wakeUpType.name</label>
								</lable>
								#end								
							</div>
						</td>
						</tr>
						
						<tr>
						<td style="vertical-align: middle;" align="right"> 结束提醒：</td>
						<td>
								<div class="d-cbox-wp">
									<span class="pa-span">$text.get("workflow.lb.startNote")</span>
									#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
									<lable class="l-cbox">	
										<input class="cbox" type="checkbox" id="stopStartWake_$row_wakeUpType.value" name="stopStartWake" value="$row_wakeUpType.value" #if($!globals.get($result,25).indexOf("$row_wakeUpType.value")>=0)checked #end/>
										<label  for="stopStartWake_$row_wakeUpType.value">$row_wakeUpType.name</label>
									</lable>
									#end
								</div>
								<div class="d-cbox-wp">
									<span class="pa-span">$text.get("workflow.lb.allNote")</span>
									#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
									<lable class="l-cbox">	
										<input class="cbox" type="checkbox" id="stopSAllWake_$row_wakeUpType.value" name="stopSAllWake" value="$row_wakeUpType.value" #if($!globals.get($result,26).indexOf("$row_wakeUpType.value")>=0)checked #end/>
										<label  for="stopSAllWake_$row_wakeUpType.value">$row_wakeUpType.name</label>
									</lable>
									#end
								</div>
								<div class="d-cbox-wp">
									<span class="pa-span">$text.get("workflow.lb.setNote")</span>
										#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode")) 
										<lable class="l-cbox">	
											<input class="cbox" type="checkbox" id="stopSetWake_$row_wakeUpType.value" name="stopSetWake" value="$row_wakeUpType.value" #if($!globals.get($result,27).indexOf("$row_wakeUpType.value")>=0)checked #end />
											<label  for="stopSetWake_$row_wakeUpType.value">$row_wakeUpType.name</label>
										</lable>
										#end
								</div>
							</td>	
						</tr>						
						<tr>
						<td style="vertical-align: middle;" align="right"></td>
						<td>
							<li>
									
								<span style="border-top:1px ridge dashed #CCCCCC">
										
								<div class="oa_signDocument1 wk-fl" id="_context">
					   				<input type="hidden" id="stopSetWakeDept" name="stopSetWakeDept" value="$!globals.get($result,28)"/>
									<div class="hBtns d-slt" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','stopSetWakeDeptName','stopSetWakeDept','1');">
									  $text.get("oa.select.dept")
									 </div>
									<select class="s-slt" name="stopSetWakeDeptName" id="stopSetWakeDeptName" multiple="multiple">
										#foreach($dept in $stopSetWakeDept)
										<option value="$dept.classCode">$!dept.DeptFullName</option>
										#end
									</select>
									<b class="icons" onClick="deleteOpation('stopSetWakeDeptName','stopSetWakeDept')"></b>
								</div>
								<div class="oa_signDocument1 wk-fl" id="_context">
									<input type="hidden" id="stopSetWakePer" name="stopSetWakePer" value="$!globals.get($result,29)"/>
									<div class="hBtns d-slt" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','stopSetWakePerName','stopSetWakePer','1');">
										 $text.get("oa.select.personal")
									</div>
									<select class="s-slt" name="stopSetWakePerName" id="stopSetWakePerName" multiple="multiple">
									#foreach($user in $stopSetWakePer)
									<option value="$user.id">$!user.EmpFullName</option>
									#end
									</select>
									<b class="icons" onClick="deleteOpation('stopSetWakePerName','stopSetWakePer')"></b>
								</div>
							
								
								</span>
							</li> 
						</td>
						</tr>
						<tr>
						<td style="vertical-align: middle;" align="right">指定人限定范围：</td>
						<td>
						<span>结束提醒、结点提醒包括步骤独立设置提醒方式的以下指定人必须限定在以下自定义语句确定的人员范围内(非专业人员请勿设置)</span><br/>
						<textarea rows="2" style="width: 450px;" name="wakeLimitSQL" id="wakeLimitSQL">$!globals.get($result,47)</textarea>
						</td>
						</tr>
					</table>
					</div>	
					</ul>
					#if($!globals.get($result,3).indexOf("00002")!="0")
					<div class="column_title"  onclick="showMenu('h');"><div class="column_a" id="img_h">反审核人</div></div>
					<ul class="column" id="h" style="display: none;">
						<div style="margin-left:100px;">
							<li style="float:left;width:100%;height:100px;">
							<div class="oa_signDocument1" id="_context">
							反审核人：  
							</div>
							<div class="oa_signDocument1" id="_context">
							<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" style="cursor:pointer" onClick="deptPop('deptGroup','reverseDeptName','popedomDeptIds','1');"  alt="$text.get("oa.common.adviceDept")" class="search">&nbsp;<a  title="$text.get("oa.select.dept")" style="cursor:pointer" onClick="deptPop('deptGroup','reverseDeptName','popedomDeptIds','1');">$text.get("oa.select.dept")</a></div>
							<select name="reversetDeptName" id="reverseDeptName" multiple="multiple">
							#foreach($dept in $targetDept)
								<option value="$dept.classCode">$!dept.DeptFullName</option>
							#end
							</select>
							</div>
							<div class="oa_signDocument2">
							<img onClick="deleteOpation('reverseDeptName','popedomDeptIds')" style="cursor:pointer" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
							</div>
							<div class="oa_signDocument1" id="_context">
							<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" style="cursor:pointer" onClick="deptPop('userGroup','reverseFileName','popedomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search">&nbsp;<a  title="$text.get("oa.select.personal")" style="cursor:pointer" onClick="deptPop('userGroup','reverseFileName','popedomUserIds','1');">$text.get("oa.select.personal")</a></div>
							<select name="reverseFileName" id="reverseFileName" multiple="multiple">
							#foreach($user in $targetUsers)
								<option value="$user.id">$!user.EmpFullName</option>
							#end
							</select></div>
							<div class="oa_signDocument2">
							<img onClick="deleteOpation('reverseFileName','popedomUserIds')" style="cursor:pointer" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
							</div>
							</li>
							<li style="float:left;width:100%;"><span style="width:90px;text-align:right">$text.get("workflow.lb.startNote")</span>
								#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
									<input type="checkbox" name="reviewWake" value="$row_wakeUpType.value" #if($!globals.get($result,42).indexOf("$row_wakeUpType.value")>=0)checked #end/>$row_wakeUpType.name 
								#end	
				   			</li>
							<span>$text.get("workflow.lb.retCheckPerRule")
						
							<select name="retCheckPerRule" id="retCheckPerRule" >
								<option value="0" #if($!globals.get($result,36)=="0")selected#end>$text.get("workflow.lb.allCheckPerson")</option>		
								<option value="1" #if($!globals.get($result,36)=="1")selected#end>$text.get("workflow.lb.thisDept")</option>
								<option value="2" #if($!globals.get($result,36)=="2")selected#end>$text.get("workflow.lb.lastDept")</option>
								<option value="3" #if($!globals.get($result,36)=="3")selected#end>$text.get("workflow.lb.nextDept")</option>
								<option value="4" #if($!globals.get($result,36)=="4")selected#end>$text.get("workflow.lb.firstDept")</option>		
							</select>
						</div>
					</ul>
					#end
					<div class="column_title"  onclick="showMenu('h');"><div class="column_a" id="img_h">备注</div></div>
					<ul class="column" id="h" style="display: none;">
						<li style="margin-bottom: 30px;width: 80%;height: 80px;"　　><label>$text.get("oaWorkflow.lb.flowDetail")：</label>
							<span><textarea name="detail" style="width:70%;height: 100px;" rows="4">$!globals.get($result,11)</textarea>
							</span>
						</li>
					</ul>
				</div>
			</td>
		</tr>
	</table>
</form>
</body>
</html>