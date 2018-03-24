<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/workflow2.css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript">
var moduleType = "$!moduleType";
function showSearch(){
	if($('#w').css("display")== "none")
	    $('#w').show();
	else
	    $('#w').hide();
}

function closeSearch(){
	$('#w').hide();
}

function subForm(){
	myform.submit();
	closeSearch();
}

function theParam(){
	var selectType=$("#selectType").val();
	var val=$("#val").val();
	return "selectType="+selectType+"&val="+val;
}

function handle(name){
	var items = document.getElementsByName(name);
	for(var i=0;i<items.length;i++){
		if(items[i].checked && items[i].statusId=="100"){
			asyncbox.alert('$text.get("workflow.msg.SysWorkflowNotDel")','$text.get("clueTo")');
			return false;
		}
	}	
	var mydatasIds="";
	for(var i=0;i<items.length;i++){	
		if(items[i].checked){
			var value = items[i].value;
			mydatasIds+=value+",";
		}	
	}
	var strParam=theParam();

	if(mydatasIds!="" && mydatasIds.length!=0){
			mydatasIds = mydatasIds.substr(0,mydatasIds.length-1);
			asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
　　　				if(action == 'ok'){
				var url="/OAWorkFlowTempQueryAction.do?operation=3&keyId="+mydatasIds+"&windCurIndex=$!winCurIndex"+"&"+strParam+"&moduleType="+moduleType;
				document.location = url;
　			}});	
	}else{
		asyncbox.alert('$text.get('common.msg.mustSelectOne')','$!text.get("clueTo")');
	}
}

function beforeOp(){
	var items = document.getElementsByName("keyId");
	var mydatasIds="";
	for(var i=0;i<items.length;i++){	
			if(items[i].checked){
				var value = items[i].value;
				mydatasIds+=value+",";
			}	
	}
	document.getElementsByName("sendOpKeyId")[0].value = mydatasIds;
	if (mydatasIds =="" && mydatasIds.length ==0){
		asyncbox.alert('$text.get("common.msg.selectone")','$text.get("clueTo")');
		return false;
	}
	document.form.submit();
}

function beforeEnOp(){
	var items = document.getElementsByName("keyId");
	var mydatasIds="";
	for(var i=0;i<items.length;i++){	
			if(items[i].checked){
				var value = items[i].value;
				mydatasIds+=value+",";
			}	
	}
	document.getElementsByName("sendEnKeyId")[0].value = mydatasIds;
	if (mydatasIds =="" && mydatasIds.length ==0){
		asyncbox.alert('$text.get("common.msg.selectone")','$text.get("clueTo")');
		return false;
	}
	document.form.submit();
}

function exportFlow(){
	if(!isChecked("keyId")){
		asyncbox.alert('$text.get("common.msg.selectone")','$text.get("clueTo")');
		return false;
	}
	form.operation.value = "99" ;
	form.submit() ;
}

function importFlow(){
	form.action = "/OAWorkFlowTempAction.do" ;
	form.operation.value = "92" ;
	form.submit() ;	
}
function getVal(){
	var x=$('#win').val();
	return x;
}

//流程设计
function openFlowSet(keyId,type,templateFile,workflowFile){
	var newCreate="false";
	if(workflowFile==""){  //如果没有流程设计文件（xml文件），则为新建
		newCreate="true";
	}
	var str="/UtilServlet?operation=BillNotFinish&keyId="+keyId;
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
					var data =jQuery.ajax({url:"/OAWorkFlowTempAction.do?operation=1&type=copy&winCurIndex=$!winCurIndex&keyId="+keyId+"&moduleType="+moduleType,async: false}).responseText;
					if(data.indexOf("ERROR:")==0){
						alert(data.substring(6));
					}else{
						//$("input[name=keyId][value="+keyId+"]").val(data);
						keyId = data;
						if(type == "1"){
							if(templateFile.length>0){
								window.open("/OAWorkFlowTempAction.do?queryType=design&ip=$host.replace('|',':')&keyId="+keyId+"&tableName="+templateFile+"&newCreate="+newCreate+"&moduleType="+moduleType);
							}
						}else{
							if(templateFile.length>0){
								window.open("/OAWorkFlowTempAction.do?queryType=design&ip=$host.replace('|',':')&keyId="+keyId+"&tableName=OAWorkFlowTable"+"&moduleType="+moduleType);
							}
						}
						window.location.reload();
					}
    				return ;
		　　　　　}else if(action == 'oldversion'){
		　　　　　　　	if(type == "1"){
						if(templateFile.length>0){
							window.open("/OAWorkFlowTempAction.do?queryType=design&ip=$host.replace('|',':')&keyId="+keyId+"&tableName="+templateFile+"&newCreate="+newCreate+"&moduleType="+moduleType);
						}
					}else{
						if(templateFile.length>0){
							window.open("/OAWorkFlowTempAction.do?queryType=design&ip=$host.replace('|',':')&keyId="+keyId+"&tableName=OAWorkFlowTable"+"&moduleType="+moduleType);
						}
					}
					return;
		　　　　　}else {
		　　　　　　　return;
		　　　　　}
		　　　}
		　});
    }else{ 
		if(type == "1"){
			if(templateFile.length>0){
				window.open("/OAWorkFlowTempAction.do?queryType=design&ip=$host.replace('|',':')&keyId="+keyId+"&tableName="+templateFile+"&newCreate="+newCreate+"&moduleType="+moduleType);
			}
		}else{
			if(templateFile.length>0){
				window.open("/OAWorkFlowTempAction.do?queryType=design&ip=$host.replace('|',':')&keyId="+keyId+"&tableName=OAWorkFlowTable"+"&moduleType="+moduleType);
			}
		}
	}
}

//旧的表单设计
function opTempFileOld(tableName,tableCHName,flag,designId,templateType){
	if(tableName.length>0){
		 if(templateType == 2){//复制表单
			window.open("WorkFlowTableAction.do?operation=16&designId="+designId+"&keyId="+tableName+"&type=simple");
		}else{		
			if(flag=="false"){  //添加表单
				window.open("/WorkFlowTableAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&designId="+designId+"&tableName="+tableName+"&tableCHName="+encodeURI(tableCHName)+"&type=simple");
			}else{ //修改表单
				window.open("/WorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&designId="+designId+"&keyId="+tableName+"&type=simple");	
			}
		}
		
	}
}

//新的表单设计
function opTempFileNew(keyId,tableName,tableCHName,flag,designId){
	var url="";
	if(flag=="false"){ //添加表单
		url="/OAWorkFlowTableAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&designId="+designId+"&tableName="+tableName+"&tableCHName="+encodeURI(tableCHName)+"&type=simple";
	}else{ //修改表单
		url="/OAWorkFlowTableAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&designId="+designId+"&turnType=page&tableName="+tableName+"&tableCHName="+encodeURI(tableCHName)+"&type=simple";
	}
	window.open(url);
}

function recordUpdate(keyId){
	var strParam=theParam();
	var selectType=$("#selectType").val();
	var val=$("#val").val();
	document.location="/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId="+keyId+"&winCurIndex=$!winCurIndex&"+strParam+"&selectType="+selectType+"&val="+val+"&moduleType="+moduleType;
}

//新建工作流




function show2(Ids,method){
	var selectClass=$("#val").val();	
	var urls = "/OAWorkFlowTempAction.do?operation=6&windCurIndex=$!winCurIndex&selectClass="+encodeURI(selectClass)+"&moduleType="+moduleType;
    asyncbox.open({
    	id:'dealdiv',
		title : '新建工作流',
		url :urls,
		width : 410,
		height : 310,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
  		if(action == 'ok'){
	  		if(!opener.beforeSubmit()){
	  			return false;
	  		}else{
	  			opener.submitForm();
	  		}
  		  }
  		}
	});	 
}

function recordNewUpdate(keyId){
	document.location="/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&type=updateNew&keyId="+keyId+"&moduleType="+moduleType;
}

//判断物理表是否存在




function isExistTable(name){
	var flag=true;
	jQuery.ajax({
		type: "POST",
	    url: "/OAWorkFlowTempAction.do",
		data: "operation=7&isExistTable=true&tableName="+name+"&moduleType="+moduleType,
		async: false,
	    success: function(msg){
			if(msg=="false"){
				alert("表单不能为空，请先设计表单!");
				flag = false;
			}
		}
	});
	return flag;
}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus();">
	  <form  method="post" scope="request" name="form" id="form" action="/OAWorkFlowTempQueryAction.do?operation=4&type=1&windCurIndex=$!winCurIndex&val=$!globals.urlEncode($!val)">
	  <input type="hidden" name="operation" value="4"/>
	  <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/> 
	  <input type="hidden" id="win" name="winCurIndex" value="$!winCurIndex"/>
	  <input type="hidden" id="noback" name="noback" value="$!noback"/>
	  <input type="hidden" name="sendOpKeyId" value=""/>
	  <input type="hidden" name="sendEnKeyId" value=""/>
	  <input type="hidden" id="selectType" name="selectType" value="$!type"/>
	  <input type="hidden" id="val" name="val" value="$!val"/>
	  <input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
	  <input type="hidden" id="selectClass" name="selectClass" value="$!sForm.selectClass"/>
		<table cellpadding="0" cellspacing="0" border="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span>
						当前位置:
						#if("$!sForm.templateType"=="-1" && "$!sForm.templateStatus"=="-1" && "$!sForm.templateClass"=="" )
							所有工作流
						#end
						#if("$!sForm.templateStatus"=="1")
							启用工作流

						#end
						#if("$!sForm.templateStatus"=="0")
							停用工作流

						#end
						#if("$!sForm.selectClass"!="")
							$!sForm.selectClass
						#end
						</span>
						<span style="float: right;font-weight: normal;margin-right: 12px;margin-bottom: 4px;">
							#if($LoginBean.operationMap.get("/OAWorkFlowTempQueryAction.do").add()||
								$LoginBean.operationMap.get("/OAWorkFlowTempQueryAction.do?moduleType=sys").add())
							<input type="button" class="bu_02" onclick="show2();" value="$text.get("common.lb.add")" />	
							#end
							#if( $LoginBean.operationMap.get("/OAWorkFlowTempQueryAction.do").delete()||
								$LoginBean.operationMap.get("/OAWorkFlowTempQueryAction.do?moduleType=sys").delete())
							<!--<input type="button" class="bu_02" onclick="handle('keyId');" value="$text.get("common.lb.del")" />	-->			
							#end			
							<input type="button" class="bu_02" onclick="return beforeOp();" value="$text.get("wokeflow.lb.enable")" />	
							<input type="button" class="bu_02" onclick="return beforeEnOp();" value="停用" />	
							<input type="button" class="bu_02" onclick="window.location.reload();" value="刷新" />	
						</span>
					</div>
					
					<table class="data_list_head"  cellpadding="0" cellspacing="0" >
						<thead>
							<tr style="height: 20px;line-height: 26px;">
								<td width="25px;">
									序号
								</td>
								<td width="25px;" style="vertical-align: middle;">
									<input type="checkbox"  value="checkbox" name="selAll" onClick="checkAll('keyId')"/>
								</td>
								<td width="150px" style="" >
									流程名称
								</td>
								<td width="*" align="middle">
									流程表单
								</td>
								<!--<td width="80px;" align="middle">
									流程模式
								</td> -->
								<td width="80px;" align="middle">
									流程类别
								</td>
								<td width="80px;" align="middle">
									流程状态

								</td>
								<td width="200px;" align="middle">
									流程管理
								</td>
							</tr>
						</thead>
					</table>
					<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow-y:auto;width:99%;margin-top: 0px;" >
<script type="text/javascript">
	var oDiv=document.getElementById("data_list_id");
	var sHeight=document.documentElement.clientHeight-110;
	oDiv.style.height=sHeight+"px";
</script>
						<table  cellpadding="0" cellspacing="0" >					
							<tbody>
								#foreach ($row in $result )
								<tr #if($globals.isOddNumber($velocityCount)) class="c1" #else class="c2" #end>
									<td width="25px;">
									 $velocityCount
									</td>
									<td width="25px;" style="vertical-align: middle;">
										<input type="checkbox" name="keyId" statusId="$!globals.get($row,6)"  value="$globals.get($row,0)"/> 
									</td>
									<td width="150px">
										<span title='$!globals.get($row,1)' style="white-space:nowrap;overflow:hidden;text-overflow:ellipsis;width:200px;display:inline-block;">$!globals.get($row,1)</span>
									</td>
									<td  width="*">
										$!globals.get($row,8)
									</td>
									<!-- <td width="80px">
									#if("$globals.get($row,9)"=="1")
										&nbsp;个性化设计
									#else
										&nbsp;自定义设计

									#end
									</td>  -->
									<td  width="80px;" align="middle">
										$!globals.get($row,3)
									</td>
									<td  width="80px;" align="middle">  
										#if("$globals.get($row,4)" == "1")
										$text.get("wokeflow.lb.enable")
										#elseif("$globals.get($row,4)" == "0")
										停用
										#end
									</td>
									<td width="200px;">
										#set($flag="")
										#if("$!globals.getTableInfoBean($globals.get($row,8))" == "")
											#set($flag="false")
										#else
											#set($flag="true")
										#end
										##只有ERP权限时出问题，因为erp的审核流设置url带有参数
										##if($LoginBean.operationMap.get("/OAWorkFlowTempQueryAction.do").update())		
											#if($!globals.get($row,5).indexOf("00002")!="-1")
											<a #if("$globals.get($row,9)"!="1") href="javascript:opTempFileOld('$globals.get($row,8)','$globals.get($row,1)','$flag','$globals.get($row,0)','$globals.get($row,2)');" #else href="javascript:opTempFileNew('$globals.get($row,2)','$globals.get($row,8)','$globals.get($row,1)','$!flag','$globals.get($row,0)');" #end><img src="style1/images/oaimages/formdesign.png" width="14" height="14" style="vertical-align: middle;" /><font style="margin-left: 2px;margin-right:5px;">表单设计</font></a>
											#end
											<a href="javascript:openFlowSet('$globals.get($row,0)','$globals.get($row,2)','$globals.get($row,8)','$!globals.get($row,10)');"><img src="style1/images/oaimages/liudesign.png" width="14" height="14" style="vertical-align: middle;" /><font style="margin-left: 2px;margin-right:5px;">流程设计</font></a>
											<a  href="javascript:recordUpdate('$globals.get($row,0)');">
												<img src="style1/images/oaimages/ni_2.gif" width="14" height="14" style="vertical-align: middle;"/><font style="margin-left: 2px;">$text.get("common.lb.update")</font>
											</a>
											#if("$globals.get($row,9)"=="1")
											<!-- <a onclick="if(!isExistTable('$globals.get($row,8)')) return false;" href="/UtilServlet?operation=exportFlow&designId=$globals.get($row,0)&tableName=$globals.get($row,8)" target="_blank"><img src="style1/images/oaimages/export.gif" width="14" height="14" style="vertical-align: middle;" /><font style="margin-left: 2px;margin-right:5px;">导出表单</font></a>  -->
											#end
										##end
									</td>
								</tr>
								#end
							</tbody>		
						</table>	
						#if($result.size()==0)
							<div>暂无工作流数据</div>		
						#end		
					</div>
					<div class="listRange_pagebar">
							$!pageBar
						</div>
				</td>		
				</tr>	
			
			</table>
		</form>
	</body>
	</html>
