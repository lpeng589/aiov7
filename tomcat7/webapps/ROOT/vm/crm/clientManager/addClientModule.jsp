<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<style type="text/css">
a{color:#000000;}
a:link{color:#000000;}
a:visited{color:#000000;}
a:hover{color:#000000;}
</style>
<script type="text/javascript">
function subForm(){
	if($("#show").html()!=""){
		$("input[name='moduleName']").val("");
		$("#show").html("");
		$("input[name='moduleName']").focus();
		return false;
	}
	if(!checkForm())
		return false;
		
	if(!selectRadio()){
		alert("权限设置为指定，请选择授权对象!");
		return false;
	}
	getShowContent("KeyShowFields","keyFields");
	getShowContent("SearchShowFields","searchFields");
	getShowContent("ListShowFields","listFields");
	getShowContent("PageShowFields","pageFields");
	getShowContent("DetailShowFields","detailFields");
	getShowContent("groupName","groupNames");
	getShowContent("ShowTables","brotherTables");
	form.submit();
}

function pageSelect(){
	var str=$("#mustSelect").val();
	var num=0;
	$("#PageShowFields option:selected").each(function(){
		if(str.indexOf($(this).val())>-1){
			num++;
		}
	});
	if(num>0)
		return false;
	else
		return true;
	
}

function pageMust(){
	var names=$("#mustNames").val();
	if(pageSelect()){
		MoveOrDelete('PageShowFields','PageSelectFields');
	}else{
		alert(names+"为必选项,不可移除!");
		return false;
	}
	return true;
}

function checkRadio(obj){
	if(obj.checked==true){
		if(obj.value==1){
		 	$("#_title").show();		 			
		}else{
			$("#_title").hide();
		}
	}
}

function checkForm(){
	var moduleName=$("input[name='moduleName']").val();
	if(moduleName==null  || moduleName==""){
		alert("模板名称不能为空，请重新填写!");
		return false;
	}
	var keyLen=$("#KeyShowFields option").size();
	var searchLen=$("#SearchShowFields option").size();
	var listLen=$("#ListShowFields option").size();
	var detailLen=$("#DetailShowFields option").size();
	var pageLen=$("#PageShowFields option").size();
	var tableLen=$("#ShowTables option").size();
	if( 
		!isNull(keyLen,"关键字设置") ||
		!isNull(searchLen,"搜索设置") || 
		!isNull(listLen,"列表设置") ||
		!isNull(detailLen,"详细布局") ||
		!isNull(pageLen,"页面布局") || 
		!isNull(tableLen,"从表设置")
	)
	return false;
	return true;
}


function isNull(variable,message){
	if(variable==0){
		alert(message+"显示字段不能为空，请重新填写!");
		return false;
	}	
	return true;	
}

function selectRadio(){
	var dept=$('#popedomDeptIds').val();
	var user=$('#popedomUserIds').val();
	var group=$('#popedomEmpGroupIds').val();
	if($("#radiobutton").attr("checked")=="checked" && dept=="" && user=="" && group==""){
		return false;
	}
	return true;
}


function loadGroup(){
	var groupNames=";";
	$("#PageShowFields option").each(function(){
		var group=$(this).attr("group");
		if(!(groupNames.indexOf(";"+group+";")>-1)){
			groupNames+=group+";"
			jQuery("<option></option>").val(group).text(group).appendTo("#groupName"); 
		}
	});
}

function getGroup(){
	$("#groupName option").each(function(){
		$(this).remove();
	});
	loadGroup();
}

function evaluate(){
	var datas =$("#dbData").val();
	newOpenSelect(datas,fieldNames,fieldNIds,1);
}

function checkModueName(){
	var name=$("input[name='moduleName']").val();
	jQuery.ajax({
	   type: "get",
	   url: "/ClientSettingAction.do",
	   cache: false,
	   data: "operation=1&addType=checkName&moduleName="+name+"",
	   success: function(msg){
	   		if(msg=="exist"){
			     $("#show").html("模版名称已存在，请重新输入");
			     $("input[name='moduleName']").focus();
		    }else{
		    	$("#show").html("");
		    }
	    }
	});
}

document.onkeydown = keyDown; 

</script>
</head>
<body onload="getGroup();">
	<form method="post" scope="request" name="form"
		action="/ClientSettingAction.do?operation=$globals.getOP("OP_ADD")" class="mytable">
		<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="" />
		<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="" />
		<input type="hidden" name="popedomEmpGroupIds" id="popedomEmpGroupIds" value="" />
		<input type="hidden" name="keyFields" id="keyFields" value="" />
		<input type="hidden" name="searchFields" id="searchFields" value="" />
		<input type="hidden" name="listFields" id="listFields" value="" />
		<input type="hidden" name="pageFields" id="pageFields" value="" />
		<input type="hidden" name="detailFields" id="detailFields" value="" />
		<input type="hidden" name="mustSelect" id="mustSelect" value="" />
		<input type="hidden" name="mustNames" id="mustNames" value="" />
		<input type="hidden" name="groupNames" id="groupNames" value="" />
		<input type="hidden" name="brotherTables" id="brotherTables" value="" />
		<input type="hidden" name="dbData" id="dbData" value="" onpropertyChange="javascript:if(this.value!=null)evaluate()"/>
		<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span><img src="/style1/images/ti_002.gif" />
						</span>
						<span>添加客户模版</span>
						<div>
							<input type="button" class="bu_02" onclick="subForm();" value="保存" />
							<input type="button" class="bu_02" onclick="javascript:window.location.href='/ClientSettingAction.do?operation=4'" value="返回" />
						</div>
					</div>
					<div id="cc" class="data"
						style="overflow:hidden;overflow-y:auto;width:100%;">
						<script type="text/javascript">
							var oDiv=document.getElementById("cc");
							var sHeight=document.documentElement.clientHeight-55;
							oDiv.style.height=sHeight+"px";
						</script>
						<div class="boxbg2 subbox_w700">
							<div class="subbox cf">
								<div class="inputbox">
									<h4 style="margin-top: 1px;">
										模版基本信息<span style="float: right;">标<font color="red" size="0.1"> * </font>部分为必填内容</span>
									</h4>
									<ul>
										<li style="width: 100%;">
											<span>模版名称：</span>
											<input name="moduleName" type="text" class="inp_w"
												style="width: 200px;" onblur="checkModueName();" /><font color="red" style="float: left;">&nbsp;*</font><font id="show" style=" width: 20%; float: left;"></font>
										</li>
										<li style="width: 100%;">
											<span>模版描述：</span>
											<input name="moduleDesc" type="text" class="inp_w2" />
										</li>
										<li style="width: 100%;">
											<span>列表客户数：</span>
											<select name="defPageSize" id="defPageSize" >
												<option value="15" #if("$!clientModule.defPageSize"=="15") selected="selected"  #end>15</option>
												<option value="30" #if("$!clientModule.defPageSize"=="30") selected="selected"  #end>30</option>
												<option value="50" #if("$!clientModule.defPageSize"=="50") selected="selected"  #end>50</option>
												<option value="100" #if("$!clientModule.defPageSize"=="100") selected="selected"  #end>100</option>
												<option value="500" #if("$!clientModule.defPageSize"=="500") selected="selected"  #end>500</option>
												<option value="1000" #if("$!clientModule.defPageSize"=="1000") selected="selected"  #end>1000</option>
											</select>
											<font>（注：客户列表每页显示客户的数量）</font>
										</li>
									</ul>
									<h4>
										查询条件显示
									</h4>
									<ul class="cf">
									
									  <div style="width: 40%;float: left;margin-left: 100px;">模糊查询显示</div> <div style="width: 40%;float: left;">条件查询显示</div> 	
										<div align="center" class="client_div" style="margin-left: 100px;">
											
										  	<table width="100%" border="0" cellpadding="1" cellspacing="0" class="client_tab" >
											      <tr align="center" valign="middle">
											        <td style="height: 230px; width: 35%;" align="center">
											        	<fieldset><div align="center"> 
											            <legend><span style="margin-left: 30px;">$text.get("com.define.canselect")</span></legend>    
														<select name="KeySelectFields" id="KeySelectFields" multiple="multiple" size="10">
														    #set($selectStr=","+$clientModule.keyFields)
														    #foreach($field in $fieldList)
														    	#set($str=","+$field.fieldName+",")
														    	#set($str2=",contact"+$field.fieldName+",")
															    #if($globals.canSelectField($field,$!mainTableName,$!childTableName))
															    	#if($field.inputType=='0' && !($selectStr.indexOf($str)>-1) && !($selectStr.indexOf($str2)>-1) && $field.fieldName!="Attachment")
															    		#if($field.tableBean.tableName==$!mainTableName)
																			#set($dis=$globals.getLocaleDisplay("$field.display"))
																    		<option value="$field.fieldName">$dis</option> 
															    		#else
																			#set($dis=$globals.getLocaleDisplay("$field.display"))
															    			<option value="contact$field.fieldName">联系人--$dis</option> 
															    		#end
															    	#end
														    	#end
														    #end			
														</select></div>
											          </fieldset>
											       
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width:15%;padding-left: 12px;"> 	
											          <span onclick="MoveOrDelete('KeySelectFields','KeyShowFields');" class="moveright"></span><br/><br/>
											          <span onclick="MoveOrDelete('KeyShowFields','KeySelectFields');" class="moveleft"></span>
											        </td>
											        <td style="width: 35%;" align="left">
											          <fieldset >
											           <legend><span style="margin-left: 30px;">$text.get("com.define.myselect")</span><font color="red" >&nbsp;*</font></legend>
													   <select name='KeyShowFields' id="KeyShowFields" multiple="multiple" size="10" >
													   		#set($keyList=$clientModule.keyFields.split(","))
														    #foreach($field in $keyList)
														    	#set($f='')	
														    	#if($field.indexOf("contact")>-1)
														    		#set($newfield=$field.replaceAll('contact',''))	
												    				#set($f=$globals.getFieldBean('CRMClientInfoDet',$newfield))
												    				#if("$!f"!="")
																		#set($dis=$globals.getLocaleDisplay("$f.display"))
															    		<option value="$field">联系人--$dis</option> 
														    		#end
																#else
																	#set($f=$globals.getFieldBean('CRMClientInfo',$field))
																	#if("$!f"!="")
																		#set($dis=$globals.getLocaleDisplay("$f.display"))
															    		<option value="$field">$dis</option> 
														    		#end
																#end
														    #end
													   </select>
											         </fieldset>											       
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width: 15%;">
											              <span  onclick="MoveFirst('KeyShowFields');" class="movefirst"> </span><br/><br/>
											           <span  onclick="MoveUp('KeyShowFields');" class="moveup"></span><br/><br/>
											           <span  onclick="MoveDown('KeyShowFields');" class="movedown"> </span><br/><br/>
											           <span  onclick="MoveLast('KeyShowFields');" class="movelast"> </span><br/>
										
											          
											        </td>
											      </tr>
											    </table>
										</div> 
											
										<div align="center" class="client_div">  
										
										  	<table width="100%" border="0" cellpadding="1" cellspacing="0" class="client_tab" >
											      <tr align="center" valign="middle">
											        <td style="height: 230px; width:35%;" align="center">
											          <fieldset>
											            <legend><span style="margin-left: 30px;">$text.get("com.define.canselect")</span></legend>   
														<select name='SearchSelectFields' id="SearchSelectFields"  multiple="multiple" size="10"  >
													   	 
														    #set($selectStr=","+$clientModule.searchFields)		
														    #foreach($field in $fieldList)
														    	#set($str=","+$field.fieldName+",")
														    	#if($globals.canSelectField($field,$!mainTableName,$!childTableName) || $field.fieldName == "statusId" || $field.fieldName == "createTime")
														   	 		#if(($field.fieldName == "statusId" || $field.inputType=="1"  || $field.fieldName == "createTime") && !($selectStr.indexOf($str)>-1))
																		#if($field.tableBean.tableName=="CRMClientInfo")
																			#set($dis=$globals.getLocaleDisplay("$field.display"))
																    		<option value="$field.fieldName">$dis</option> 
															    		#end
																	#end
																#end
														    #end								  
														</select>
											          </fieldset>
											       
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width:15%;padding-left: 12px;"> 	
											          <span onclick="MoveOrDelete('SearchSelectFields','SearchShowFields');" class="moveright"></span><br/><br/>
											          <span onclick="MoveOrDelete('SearchShowFields','SearchSelectFields');" class="moveleft" ></span>
											        </td>
											        <td style="width: 35%;" align="left">
											          <fieldset >
											           <legend><span style="margin-left: 30px;">$text.get("com.define.myselect")</span><font color="red" >&nbsp;*</font></legend>
													   <select name='SearchShowFields' id="SearchShowFields" multiple="multiple" size="10" >
													   		#set($searchList=$clientModule.searchFields.split(","))
														    #foreach($field in $searchList)	
														    	#set($f='')
														   		#if(!($field.indexOf("contact")>-1))
																	#set($f=$globals.getFieldBean('CRMClientInfo',$field))
																	#if("$!f"!="")
																		#set($dis=$globals.getLocaleDisplay("$f.display"))
															    		<option value="$field">$dis</option> 
														    		#end
																#end
														    #end
													   </select>
											         </fieldset>											       
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width: 15%;">
											            <span  onclick="MoveFirst('SearchShowFields');" class="movefirst" ></span><br/><br/>
											           <span  onclick="MoveUp('SearchShowFields');" class="moveup" ></span><br/><br/>
											           <span  onclick="MoveDown('SearchShowFields');" class="movedown" ></span><br/><br/>
											           <span  onclick="MoveLast('SearchShowFields');" class="movelast" ></span><br/>
											        </td>
											      </tr>
											    </table>										
										    </div>	
										    <div style="float: left; width: 100%;margin-left: 100px;color:#999999;">注: 为了查询效率，模糊查询显示字段尽量少选</div>	
									</ul>
									<h4>
										客户列表显示
									</h4>
									<ul class="cf">
										 <div style="width: 40%;float: left;margin-left: 100px;">列表信息显示</div> <div style="width: 40%;float: left;">详情信息显示</div> 	
										<div align="center" class="client_div"  style="margin-left: 100px;">  
										  	<table width="100%;" border="0" cellpadding="1" cellspacing="0" class="client_tab" >
											      <tr align="center" valign="middle">
											        <td style="height: 230px; width: 35%;" align="center">
											          <fieldset>
											            <legend><span style="margin-left: 30px;">$text.get("com.define.canselect")</span></legend>   
														<select name='ListSelectFields' id="ListSelectFields" multiple="multiple" size="10"  >
													   	  
														    #set($selectStr=","+$clientModule.listFields)				
														    #foreach($field in $fieldList)	
														    	#set($str=","+$field.fieldName+",")
														    	#if($field.fieldName!="Attachment")
													   	   		#if($globals.canSelectField($field,$!mainTableName,$!childTableName)==true || ($field.fieldName=="createBy" || $field.fieldName=="createTime"))
													   	   			#if(!($selectStr.indexOf($str)>-1))
															   	   		#if($field.tableBean.tableName=="CRMClientInfo")
																			#set($dis=$globals.getLocaleDisplay("$field.display"))
																    		<option value="$field.fieldName">$dis</option> 
															    		#end
																    #end
														    	#end
														    	#end
														    #end													  
														</select>
											          </fieldset>
											       
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width:15%;padding-left: 12px;"> 	
											         	 <span  onclick="MoveOrDelete('ListSelectFields','ListShowFields');" class="moveright"></span><br/><br/>
											          	 <span  onclick="MoveOrDelete('ListShowFields','ListSelectFields');" class="moveleft"></span>
											        </td>
											        <td style="width: 35%;" align="left">
											          <fieldset >
											           <legend><span style="margin-left: 30px;">$text.get("com.define.myselect")</span><font color="red">&nbsp;*</font></legend>
													   <select name="ListShowFields" id="ListShowFields" multiple="multiple" size="10" >
															#set($listList=$clientModule.listFields.split(","))
														    #foreach($field in $listList)	
														    	#set($f='')
														   		#if(!($field.indexOf("contact")>-1))
																	#set($f=$globals.getFieldBean('CRMClientInfo',$field))
																	#if("$!f"!="")
																		#set($dis=$globals.getLocaleDisplay("$f.display"))
																		
														    			<option value="$field" #if("$field"=="ClientName") disabled="disabled" #end>$dis</option> 
																	#end
																#end
														    #end
													   </select>
											         </fieldset>
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width:15%;">
											            <span  onclick="MoveFirst('ListShowFields');" class="movefirst"></span><br/><br/>
											           <span  onclick="MoveUp('ListShowFields');" class="moveup"></span><br/><br/>
											           <span  onclick="MoveDown('ListShowFields');" class="movedown"></span><br/><br/>
											           <span  onclick="MoveLast('ListShowFields');" class="movelast"></span><br/>
											         </td>
											      </tr>
											    </table>										
										    </div>								
									
										<div align="center" class="client_div">  
										  	<table width="100%" border="0" cellpadding="1" cellspacing="0" class="client_tab" >
											      <tr align="center" valign="middle">
											        <td style="height: 230px; width: 35%;" align="center">
											          <fieldset>
											            <legend><span style="margin-left: 30px;">$text.get("com.define.canselect")</span></legend>   
														<select name='DetailSelectFields' id="DetailSelectFields" multiple="multiple" size="10"  >
													   	  
														    #set($selectStr=","+$clientModule.detailFields)				
														    #foreach($field in $fieldList)	
														    	#set($str=","+$field.fieldName+",")
														    	#set($str2=",contact"+$field.fieldName+",")
														    	#if($field.fieldName!="Attachment")
													   	   		#if($globals.canSelectField($field,$!mainTableName,$!childTableName))
													   	   			#if(!($selectStr.indexOf($str)>-1) && !($selectStr.indexOf($str2)>-1))
															   	   		#if($field.tableBean.tableName=="CRMClientInfo")
																			#set($dis=$globals.getLocaleDisplay("$field.display"))
																    		<option value="$field.fieldName">$dis</option> 
															    		#else
																			#set($dis=$globals.getLocaleDisplay("$field.display"))
															    			<option value="contact$field.fieldName">联系人--$dis</option> 
															    		#end
															    	#end
														    	#end
														    	#end
														    #end											  
														</select>
											          </fieldset>
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width:15%;padding-left:12px;"> 	
											         	 <span  onclick="MoveOrDelete('DetailSelectFields','DetailShowFields');" class="moveright"></span><br/><br/>
											         	 <span  onclick="MoveOrDelete('DetailShowFields','DetailSelectFields');" class="moveleft"></span>
										        	</td>
											        <td style="width: 35%;" align="left">
											          <fieldset>
											           <legend><span style="margin-left: 30px;">$text.get("com.define.myselect")</span><font color="red">&nbsp;*</font></legend>
													   <select name="DetailShowFields" id="DetailShowFields" multiple="multiple" size="10" >
													   		#set($detailList=$clientModule.detailFields.split(","))
														    #foreach($field in $detailList)	
														    	#set($f='')
														   		#if($field.indexOf("contact")>-1)
														    		#set($newfield=$field.replaceAll('contact',''))	
												    				#set($f=$globals.getFieldBean('CRMClientInfoDet',$newfield))
												    				#if("$!f"!="")
																		#set($dis=$globals.getLocaleDisplay("$f.display"))
															    		<option value="$field">联系人--$dis</option> 
															    	#end
																#else
																	#set($f=$globals.getFieldBean('CRMClientInfo',$field))
																	#if("$!f"!="")
																		#set($dis=$globals.getLocaleDisplay("$f.display"))
															    		<option value="$field">$dis</option> 
															    	#end
																#end
														    #end
														        
													   </select>
											         </fieldset>
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width: 15%;">
											            <span  onclick="MoveFirst('DetailShowFields');" class="movefirst"></span><br/><br/>
											           <span  onclick="MoveUp('DetailShowFields');" class="moveup"></span><br/><br/>
											           <span  onclick="MoveDown('DetailShowFields');" class="movedown"></span><br/><br/>
											           <span  onclick="MoveLast('DetailShowFields');" class="movelast"></span><br/>
											        </td>
											      </tr>
											    </table>										
										    </div>	
										   <div style="float: left; width: 100%;margin-left: 100px;color:#999999;">注: 为了界面美观，列表信息显示字段尽量不超过10个</div>									
									</ul>
									<h4>
										客户页面显示
									</h4>
									<ul class="cf">
									 <div style="width: 100%;float: left;margin-left: 100px;">客户添加、修改、详情页面字段的显示</div>	
										<div align="center" class="client_div" style="float: left; margin-left: 100px;">  
										  	<table width="100%" border="0" cellpadding="1" cellspacing="0" class="client_tab" >
											      <tr align="center" valign="middle">
											        <td style="height: 230px; width: 35%;" align="center">
											          <fieldset>
											            <legend><span style="margin-left: 30px;">$text.get("com.define.canselect")</span></legend>   
														<select name='PageSelectFields' id="PageSelectFields" multiple="multiple" size="10"  >
													   	    
														    #set($selectStr=","+$clientModule.pageFields)				
														    #foreach($field in $fieldList)	
														    	#set($str=","+$field.fieldName+",")
														    	#set($str2=",contact"+$field.fieldName+",")
													   	   		#if($globals.canSelectField($field,$!mainTableName,$!childTableName))
													   	   			#if(!($selectStr.indexOf($str)>-1) && !($selectStr.indexOf($str2)>-1))
															   	   		#if($field.tableBean.tableName=="CRMClientInfo")
																			#set($dis=$globals.getLocaleDisplay("$field.display"))
																			#set($groupDis=$globals.getLocaleDisplay("$field.groupDisplay"))
																    		<option value="$field.fieldName" group="$groupDis">$dis</option> 
															    		#else
																			#set($dis=$globals.getLocaleDisplay("$field.display"))
																			#set($groupDis=$globals.getLocaleDisplay("$field.groupDisplay"))
															    			<option value="contact$field.fieldName" group="$groupDis">联系人--$dis</option> 
															    		#end
															    	#end
														    	#end
														    #end				
														</select>
											          </fieldset>
											       
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width:15%;padding-left: 12px;"> 	
											          <span  onClick="MoveOrDelete('PageSelectFields','PageShowFields');getGroup();" class="moveright"></span><br/><br/>
											          <span   onClick="javascript:if(pageMust())getGroup();"  class="moveleft"></span>
											        </td>
											        <td style="width: 35%;" align="left">
											          <fieldset >
											           <legend><span style="margin-left: 30px;">$text.get("com.define.myselect")</span><font color="red">&nbsp;*</font></legend>
													   <select name='PageShowFields' id="PageShowFields" multiple="multiple" size="10">
														
														   	#set($pageList=$clientModule.pageFields.split(","))
														    #foreach($field in $pageList)
														    	#set($f='')	
														   		#if($field.indexOf("contact")>-1)
														    		#set($newfield=$field.replaceAll('contact',''))	
												    				#set($f=$globals.getFieldBean('CRMClientInfoDet',$newfield))
												    				#if("$!f"!="")
																		#set($dis=$globals.getLocaleDisplay("$f.display"))
																		#set($groupDis=$globals.getLocaleDisplay("$f.groupDisplay"))
																		#if($f.isNull=="1")
																			<option id="op_$field" value="$field" group="$groupDis">联系人--$dis</option>
																		#else
															    			<option value="$field" group="$groupDis">联系人--$dis</option>
															    		#end 
														    		#end
																#else
																	#set($f=$globals.getFieldBean('CRMClientInfo',$field))
																	#if("$!f"!="")
																		#set($dis=$globals.getLocaleDisplay("$f.display"))
																		#set($groupDis=$globals.getLocaleDisplay("$f.groupDisplay"))
																		#if($f.isNull=="1")
																			<option id="op_$field" value="$field" group="$groupDis">$dis</option>
																		#else
															    			<option value="$field" group="$groupDis">$dis</option> 
															    		#end
														    		#end
																#end
														    #end
														    			
													   </select>

											         </fieldset>
											       
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width:15%;">
											            <span  onclick="MoveFirst('PageShowFields');" class="movefirst"> </span><br/><br/>
											           <span  onclick="MoveUp('PageShowFields');"  class="moveup"></span><br/><br/>
											           <span  onclick="MoveDown('PageShowFields');"  class="movedown"></span><br/><br/>
											           <span  onclick="MoveLast('PageShowFields');"  class="movelast"></span><br/>
											        </td>
											      </tr>
											    </table>										
										    </div>	
<script type="text/javascript">
	var mustSelect="";
	var mustNames="";
	$("#PageShowFields option[id^='op_']").each(function(){
		 mustSelect+=$(this).val()+";";
		 mustNames+=$(this).text()+",";
	});
	$("#mustSelect").val(mustSelect);
	$("#mustNames").val(mustNames);
</script>
										<div align="center" class="client_div" style="width:17%;">  
										  	<table width="100%" border="0" cellpadding="1" cellspacing="0" class="client_tab" >
											      <tr align="center" valign="middle">
											        <td style="height: 230px; width: 75%;" align="center">
											          <fieldset>
											            <legend><span style="margin-left: 30px;">分组排序</span></legend>   
														<select name='groupName' id="groupName" multiple="multiple" size="10"  >
															#set($groupList=$clientModule.groupNames.split(","))
															#foreach($g in $groupList)
																<option value="$g">$g</option>
															#end
														</select>
											          </fieldset>
											           
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width:25%;">
											           <span  onclick="MoveFirst('groupName');"  class="movefirst"></span><br/><br/>
											           <span  onclick="MoveUp('groupName');"  class="moveup"></span><br/><br/>
											           <span  onclick="MoveDown('groupName');"  class="movedown"></span><br/><br/>
											           <span  onclick="MoveLast('groupName');"  class="movelast"></span><br/>
											        </td>
											      </tr>
											  </table>										
										 </div>				
									</ul>

									<h4>
										邻居表显示



									</h4>
									<ul class="cf">
										<div align="center" class="client_div"  style="margin-left: 100px;" >  
										  	<table width="100%" border="0" cellpadding="1" cellspacing="0" class="client_tab" >
											      <tr align="center" valign="middle">
											        <td style="height: 230px; width:35%; " align="center">
											          <fieldset>
											            <legend><span style="margin-left: 30px;">可选邻居表</span></legend>   
														<select name='SelectTables' id="SelectTables" multiple="multiple" size="10"  >
													   	
													   		#set($selectStr=","+$clientModule.brotherTables)	
													   		#foreach($table in $brotherList)	
													   			#set($dis=$globals.getLocaleDisplay("$table.display"))
													   			#set($str=","+$table.tableName+",")
													   			#if(!($selectStr.indexOf("$str")>-1))
													   				<option value="$table.tableName">$dis</option>
													   			#end
													   		#end								  
														</select>
											          </fieldset>
											        </td>
											        <td align="center" style="vertical-align: middle;width:15%;padding-left: 12px;"> 	
											          <span onclick="MoveOrDelete('SelectTables','ShowTables');" class="moveright"></span><br/><br/>
											          <span onclick="MoveOrDelete('ShowTables','SelectTables');"  class="moveleft"></span>
											        </td>
											        <td style="width: 35%；" align="left">
											          <fieldset >
											           <legend><span style="margin-left: 30px;">显示邻居表</span><font color="red">&nbsp;*</font></legend>
													   <select name="ShowTables" id="ShowTables" multiple="multiple" size="10" >
												   			#set($selectList=$clientModule.brotherTables.split(","))	
													   		#foreach($str in $selectList)
													   			#set($table=$globals.getTableInfoBean("$str"))
													   			#set($dis=$globals.getLocaleDisplay("$table.display"))
													   			<option value="$str">$dis</option>
													   		#end
													   </select>
											         </fieldset>
											        </td>
											        <td align="center" style="vertical-align: middle;padding: 0px;width: 15%;">
											           <span  onclick="MoveFirst('ShowTables');"  class="movefirst"></span><br/><br/>
											           <span  onclick="MoveUp('ShowTables');"  class="moveup"></span><br/><br/>
											           <span  onclick="MoveDown('ShowTables');"  class="movedown"></span><br/><br/>
											           <span  onclick="MoveLast('ShowTables');"  class="movelast"></span><br/>
											        </td>
											      </tr>
											    </table>										
										    </div>		
										    <div style="float: left; width: 100%;margin-left: 100px;color:#999999;">注: 邻居表最多只能选择10个</div>							
									</ul>
									<h4>
										权限控制
									</h4>
									<ul class="cf">
										<span style="width: 100%;margin-left: 100px;float: left;">权限控制：	
											<input name="isAlonePopedmon" onClick="checkRadio(this)" type="radio" id="radio"  checked="checked" value="0" />
												$text.get("oa.common.all")
											<input name="isAlonePopedmon" onClick="checkRadio(this)" type="radio" id="radiobutton" value="1" />
												$text.get("oa.common.appoint")
										</span>
										<div id="_title" style="margin-top: 15px;display: none;">
										<div class="oa_signDocument1" id="_context" style="margin-left: 100px;float: left;">
										<div class="oa_signDocument_3">
											<img src="/$globals.getStylePath()/images/St.gif"
												onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');" alt="$text.get("oa.common.adviceDept")" class="search" />
											&nbsp;
											<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');">
											$text.get("oa.select.dept")</a>
										</div>
										<select name="formatDeptName" id="formatDeptName" size="5"
											multiple="multiple">
										</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="deleteOpation('formatDeptName','popedomDeptIds')"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
										</div>
										<div class="oa_signDocument1" id="_context" >
											<div class="oa_signDocument_3">
												<img src="/$globals.getStylePath()/images/St.gif"
													onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search" />
												&nbsp;
												<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');">$text.get("oa.select.personal")</a>
											</div>
											<select name="formatFileName" id="formatFileName" size="5"
												multiple="multiple">
											</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="deleteOpation('formatFileName','popedomUserIds')"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
										</div>
										<input type="hidden" name="popedomEmpGroupIds"
											id="popedomEmpGroupIds" value="" />
										<div class="oa_signDocument1" id="_context" >
										<div class="oa_signDocument_3">
											<img src="/$globals.getStylePath()/images/St.gif"
												onClick="deptPop('empGroup','EmpGroup','popedomEmpGroupIds','1');"
												alt="$text.get("oa.oamessage.usergroup")" class="search" />
											&nbsp;
											<a href="javascript:void(0)" style="cursor:pointer"
												title="$text.get('oa.oamessage.usergroup')"
												onClick="deptPop('empGroup','EmpGroup','popedomEmpGroupIds','1');">选择分组</a>
										</div>
										<select name="EmpGroup" id="EmpGroup" size="5" multiple="multiple">
										</select>
										</div>
										<div class="oa_signDocument2">
											<img onClick="deleteOpation('EmpGroup','popedomEmpGroupIds')"
												src="/$globals.getStylePath()/images/delete_button.gif"
												alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
										</div>
										</div>
									</ul>
									<br />
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
