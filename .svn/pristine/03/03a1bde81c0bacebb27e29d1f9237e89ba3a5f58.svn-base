<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta name="renderer" content="webkit">
<title>demo</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="/style/css/brotherSetting.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<style type="text/css">
body{width:820px;}
</style>
<script type="text/javascript">
$(function(){
	#if("$!operationSuccess" == "true")
		asyncbox.tips('操作成功!','success');
	#end
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
})

/*记录并封装修改后的值*/
var hideObj;
var hideEnumerName;
function getUpdateInfo(obj,enumerName){	
	if(obj.value == "update"){
		var enumerId = obj.getAttribute("enumerId");
		hideEnumerName = obj.getAttribute("enumerName");
		asyncbox.open({
			id: 'EnumWinow',
			title : "选项数据管理",
			url :'/EnumerationAction.do?operation=7&type=crm&keyId='+enumerId,
			width : 620,
			height : 400,
			btnsbar : jQuery.btn.OKCANCEL,
			callback : function(action,opener){
				if(action == 'ok'){
					opener.jQuery("#crmPopFlag").val("true");//参数crmPopFlag设置为true,表示从CRM模板设置修改选项数据
					//赋值给隐藏参数
					hideObj = obj;
					opener.beforSubmit(opener.document.form);
				}else{
					$(obj).children().first().attr("selected","selected");
					jQuery.close('EnumWinow');
				}
				return false;
			}
		});		
		return;
	}


	var tableName = $(obj).parents("ul").attr("tableName");//表名
	var fieldName = $(obj).parents("ul").attr("fieldName");//字段名 
	var updateType = $(obj).attr("updateType");//跟新类型
	//判断宽度是否数字
	if(updateType == "width"){
		if(isNaN($(obj).val())){
			alert("宽度只能输入数字，请修改。");
			$(obj).select();
			return false;
		}
	}	
	//封装信息开始 
	var str = tableName+":"+fieldName+":"+updateType+":";//把update信息拼接起来 表名+字段名+更新类型+更新值,用"|"区分每一个 
	if(updateType == "statusId" || updateType == "isNull"){
		if($(obj).attr("checked") == "checked"){
			str+="1";//启用值为1
		}else{
			str+="0";//停用值为0
		}
	}else if(updateType == "inputType"){
		//修改输入类型
		if($(obj).val().indexOf("fieldType")==-1){
			str += $(obj).val();
		}else{
			//若值有包含"fieldType:"表示update fieldType
			str = tableName+":"+fieldName+":fieldType:"+$(obj).val().replace("fieldType:","");
		}	
	}else{
		str+=$(obj).val();
	}
	str+=";";
	$("#updateInfo").val($("#updateInfo").val()+str);
}

//选项数据操作成功后,处理方法
function dealAsyn(){
	var obj = hideObj;
	var enumerName = hideEnumerName;
	jQuery.ajax({
	   type: "POST",
	   url: "/ClientSettingAction.do?operation=4&queryType=reloadEnum&enumerName="+enumerName,
	   success: function(msg){
	 	   var str = '<option value="">--请选择--</option>';
		   str +=msg;
		   str +="<option value='update'>编辑选项数据</option>";
		   $(obj).children().remove()
		   $(obj).append(str);
		   $(obj).children().first().attr("selected","selected");		   
	   }
	});
	jQuery.close('EnumWinow');	
}


/*提交表单*/
function beforeSubmit(){
	var isError = "false";//错误标识，用于返回。true表示有错误  
	$(":input[updateType='width']").each(function(){
		if(isNaN($(this).val())){
			alert("宽度只能输入数字，请修改。");
			$(this).select();
			isError ="true";
		}
	})
	if(isError == "true"){
		return false;
	}
	$("input[name='fieldName']").each(function(){
		var test1 = /^[0-9]+[\s\S]*$/;
		if(test1.test($(this).val())){
			alert("字段不能以数字开头，请修改。");
			$(this).select();
			isError ="true";
		}
	})
	if(isError == "true"){
		return false;
	}
	
	var addMainFieldInfo = "";
	$(".addField:visible").each(function(){
		var tableType = $(this).attr("tabletype");//main表示新增主表字段,child表示明细表



		var fieldName = $(this).find("input[name='fieldName']").val();//字段名称
		var inputType = $(this).find("select[name='inputType']").val();//输入类型
		var width = $(this).find("input[name='width']").val();//宽度
		var groupName = $(this).find("select[name='groupName']").val();//分组选项
		
		var defaultValue = "empty";//默认值默认为empty
		if(inputType == "20"){
			//若是选择关联客户下拉框，获取相关值



			defaultValue = $(this).find("select[name='defaultValue']").val()
		}

		var statusId = "1";//启用，默认是
		if($(this).find(":checkbox[name='statusId']").attr("checked") != "checked"){
			statusId = "0";
		}
		
		var isNull = "0";//必填，默认否
		if($(this).find(":checkbox[name='isNull']").attr("checked") == "checked"){
			isNull = "1";
		}
		
		//拼接主表字段 显示名称+输入类型+默认值+宽度+组名+是否启用+是否必填
		addMainFieldInfo +=tableType+"&&"+fieldName+"&&"+inputType+"&&"+defaultValue+"&&"+width+"&&"+groupName+"&&"+statusId+"&&"+isNull+";";
		
	})
	
	$("#addMainFieldInfo").val(addMainFieldInfo);
	
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
	}
	form.submit();
}

/*添加字段
tableType:类型,main表示主表 child表示明细
*/
function addField(tableType){
	var indexDiv = "mainDiv";
	var ulId = "copyFieldInfo";
	if(tableType == "child"){
		indexDiv = "childDiv";
		ulId = "copyChildFieldInfo";
	}
	
	var index = $("#"+indexDiv+" ul:visible").length+1;
	var copyField = jQuery("#"+ulId).clone(true);
	jQuery("#"+ulId).before(copyField);
	jQuery("ul[id='"+ulId+"']:first li:first i").text(index)
	$("ul[id='"+ulId+"']:first").show();
	
}

/*添加修改类型*/
function changeInputType(obj){
	if($(obj).val() == "20"){
		$(obj).parents("li").next().find("select").show();
	}else{
		$(obj).parents("li").next().find("select").hide();
	}
}

/*删除字段*/
function delField(obj){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: "<div style='width:135px;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'></div></div>",css:{ background: 'transparent'}}); 
	}
	jQuery.ajax({
		type: "POST",
		url: "/CRMBrotherSettingAction.do",
		data: "operation=$globals.getOP("OP_DELETE")&type=delField&tableName="+$(obj).attr("tableName")+"&fieldName="+$(obj).attr("fieldName"),
		success: function(msg){
			if(typeof(top.junblockUI)!="undefined"){
				top.junblockUI();
			}
			if(msg == "success"){
				$(obj).parents("ul").remove();
				asyncbox.tips('删除成功!','success');
			}else{
				asyncbox.tips('删除失败!','error');
			}    
		}
	});

}



</script>	
	
</head>
<body>
<form action="/CRMBrotherSettingAction.do" name="form" method="post">
	<input type="hidden" name="operation" id="operation" value='$globals.getOP("OP_UPDATE")' />
	<input type="hidden" name="type" id="type" value="maintain"/>
	<input type="hidden" name="tableName" id="tableName" value="$!tableName"/>
	<input type="hidden" name="childTableName" id="childTableName" value="$!childTableName"/>
	<input type="hidden" name="updateInfo" id="updateInfo" >
	<input type="hidden" name="addMainFieldInfo" id="addMainFieldInfo" >
	<div style="overflow:hidden;padding:5px 0 0 10px;">
		<div style="float:left;display:inline-block;line-height:26px;">当前位置：字段维护</div>
		<div class="btn btn-small" onclick="beforeSubmit()" style="float:right;">保存</div>
	</div>
	<div class="blt_head">
		<ul class="head_ul">
			<li class="head_li" style="width:60px;">
				<i class="char" >
					序号
				</i>
			</li>
			<li class="head_li" style="min-width:100px;width:10%;">
				<i class="char">
					字段名称
				</i>
			</li>
			<li class="head_li" style="width:80px;">
				<i class="char">
					显示名称
				</i>
			</li>
			<li class="head_li" style="width:120px;">
				<i class="char">
					输入类型
				</i>
			</li>
			<li class="head_li" style="width:120px;">
				<i class="char">
					选项设置
				</i>
			</li>
			<li class="head_li" style="width:80px;">
				<i class="char">
					列表字段宽度
				</i>
			</li>
			<li class="head_li" style="width:120px;">
				<i class="char">
					分组
				</i>
			</li>
			<li class="head_li" style="width:40px;">
				<i class="char">
					启用
				</i>
			</li>
			<li class="head_li" style="width:40px;">
				<i class="char">
					必填
				</i>
			</li>
		</ul>
	</div>
	<div style="height:360px;overflow:auto;">
	<div class="wp_dv">
		<!-----------分割线 wp_blt板块 Start------------->
		<div class="wp_blt">
			<p>
				<em>主表信息 </em>
				<i class="btn btn-small" onclick="addField('main');">添加</i>
			</p>
			<div class="blt_main" id="mainDiv" >
				<ul class="c_ul addField" id="copyFieldInfo" style="display: none;" tableType="main">
					<li class="c_li" style="width:60px;">
						<i class="char" id="orderNo">
							
						</i>
					</li>
					<li class="c_li" style="min-width:100px;width:10%;">
						<i class="char">
							新增字段
						</i>
					</li>
					<li class="c_li" style="width:80px;">
						<input class="txt_inp" type="text" name="fieldName"/>
					</li>
					<li class="c_li" style="width:120px;">
						<i class="char">
							<select name="inputType" onchange="changeInputType(this);">
								<option value="0" >输入</option>
								<option value="1" >下拉列表</option>
								<option value="5" >复选框</option>
								<option value="14" >部门弹出框</option>
								<option value="15" >职员弹出框</option>
								<option value="2" >自定义弹出框</option>
								<option value="fieldType:5" >短日期</option>
								<option value="fieldType:6" >长日期</option>
								<option value="fieldType:3" >备注</option>
								<option value="20" >关联客户下拉框</option>
							</select>
						</i>
					</li>
					<li class="c_li" style="width:120px;">
						<select name="defaultValue" style="display: none;">
	        		 	 	 <option value="" >--请选择--</option>
	        		 	 	 #foreach($option in $relateClientOpetions)
			            		<option value="$globals.get($option,1)" >$globals.get($option,0)</option>
			            	#end
			            </select>
						
					</li>
					<li class="c_li" style="width:80px;">
						<input class="txt_inp_small" type="text" value="90" name="width"/>
					</li>
					<li class="c_li" style="width:120px;">
						<select name="groupName">							
			             	#foreach($item in $globals.getEnumerationItems("$tableName"))	 			             	
			        	      <option value="$item.value">$item.name</option>
					        #end
			            </select>
					</li>
					<li class="c_li" style="width:40px;">
						<input name="statusId"  type="checkbox" checked="checked"/>
					</li>
					<li class="c_li" style="width:40px;">
						<input name="isNull" type="checkbox" />						
					</li>
				</ul>
			#set($rowIndex = 1)
			#foreach($fieldBean in $fieldList)
				#if($globals.canSelectField($fieldBean,$tableName,$childTableName) )
				<ul class="c_ul" fieldName ="$fieldBean.fieldName" tableName="$tableName">
					<li class="c_li" style="width:60px;">
						<i class="char">
							$rowIndex
						</i>
					</li>
					<li class="c_li" style="min-width:100px;width:10%;">
						<i class="char">
							$fieldBean.fieldName
						</i>
					</li>
					<li class="c_li" style="width:80px;">
						<input class="txt_inp" type="text" value='$fieldBean.display.get("$globals.getLocale()")' updateType="language" onchange="getUpdateInfo(this)"/>
					</li>
					<li class="c_li" style="width:120px;">
						<i class="char">
							$globals.getInputTypeForModule("$fieldBean.inputType","$fieldBean.fieldType")
						</i>
					</li>
					<li class="c_li" style="width:120px;">
						#if("$fieldBean.inputType" == "1")
							<select name="$fieldBean.fieldName"  id="$fieldBean.fieldName" updateType="defaultValue" 
								enumerId='$!enumerMap.get("$fieldBean.refEnumerationName")' enumerName='$fieldBean.refEnumerationName' onchange="getUpdateInfo(this)">
		        		 	 	 <option value="" >--请选择--</option>
				             	 #foreach($item in $globals.getEnumerationItems("$fieldBean.refEnumerationName"))	 
				        	       <option value="$item.value"#if("$fieldBean.defaultValue" == "$item.value") selected="selected" #end>$item.name</option>
						         #end
						         <option value="update">编辑选项数据</option>
				            </select>
				        #elseif("$fieldBean.inputType" == "20")
				        	<select name="$fieldBean.fieldName"  id="$fieldBean.fieldName" updateType="defaultValue" onchange="getUpdateInfo(this)">
		        		 	 	 <option value="" >--请选择--</option>
		        		 	 	 #set($optionList = $relateClientMap.get($fieldBean.fieldName))
		        		 	 	 #foreach($option in $optionList)
				            		<option value="$globals.get($option,1)" #if("$fieldBean.defaultValue"=="$globals.get($option,1)") selected="selected" #end>$globals.get($option,0)</option>
				            	#end
				            </select>
				        #else
				        	
						#end
					</li>
					<li class="c_li" style="width:80px;">
						<input class="txt_inp_small" type="text" value="$fieldBean.width" updateType="width" onchange="getUpdateInfo(this)"/>
					</li>
					<li class="c_li" style="width:120px;">
						<select name="$fieldBean.fieldName"  id="$fieldBean.fieldName" updateType="groupName" onchange="getUpdateInfo(this)">
	        		 	 	<option value="" >--请选择--</option>
			             	#foreach($item in $globals.getEnumerationItems("$tableName"))	 
			        	      <option value="$item.value"#if("$fieldBean.groupName" == "$item.value") selected="selected" #end>$item.name</option>
					        #end
			            </select>
					</li>
					<li class="c_li" style="width:40px;">
						<input name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="checkbox" #if("$fieldBean.statusId"== '1') value="1" checked="checked" #else value="0" #end updateType="statusId" onchange="getUpdateInfo(this)" #if("$fieldBean.fieldName" == "ClientId") disabled="disabled" #end/>
					</li>
					<li class="c_li" style="width:40px;">
						<input name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="checkbox" #if("$fieldBean.isNull"== '1') value="1" checked="checked" #else value="0" #end updateType="isNull" onchange="getUpdateInfo(this)" #if("$fieldBean.fieldName" == "ClientId") disabled="disabled" #end/>						
					</li>
					#if("$fieldBean.isStat" != "1")<li onclick="delField(this)" tableName="$tableName" fieldName="$fieldBean.fieldName" style="cursor: pointer;">删除</li>#end
				</ul>
				#set($rowIndex = $rowIndex+1)
				#end
			#end
			</div>
		</div>
		<!-----------分割线 wp_blt板块 End------------->
	</div>
	#if($childFieldList.size()>0 && $childTableName !="CRMsalesQuot" && $childTableName !="CRMSaleContractDet")
	<div class="wp_dv">
		<!-----------分割线 wp_blt板块 Start------------->
		<div class="wp_blt">
			<p>
				<em>明细信息</em>
				<i class="btn btn-small" onclick="addField('child');">添加</i>
			</p>
			<div class="blt_main" id="childDiv" >
				<ul class="c_ul addField" id="copyChildFieldInfo" style="display: none;" tableType="child">
					<li class="c_li" style="width:60px;">
						<i class="char" id="orderNo">
							
						</i>
					</li>
					<li class="c_li" style="min-width:100px;width:10%;">
						<i class="char">
							新增字段
						</i>
					</li>
					<li class="c_li" style="width:80px;">
						<input class="txt_inp" type="text" name="fieldName"/>
					</li>
					<li class="c_li" style="width:120px;">
						<i class="char">
							<select name="inputType" onchange="changeInputType(this);">
								<option value="0" >输入</option>
								<option value="1" >下拉列表</option>
								<option value="5" >复选框</option>
								<option value="14" >部门弹出框</option>
								<option value="15" >职员弹出框</option>
								<option value="2" >自定义弹出框</option>
								<option value="fieldType:5" >短日期</option>
								<option value="fieldType:6" >长日期</option>
								<option value="fieldType:3" >备注</option>
							</select>
						</i>
					</li>
					<li class="c_li" style="width:120px;">
						<select name="defaultValue" style="display: none;">
	        		 	 	 <option value="" >--请选择--</option>
	        		 	 	 #foreach($option in $relateClientOpetions)
			            		<option value="$globals.get($option,1)" >$globals.get($option,0)</option>
			            	#end
			            </select>
						
					</li>
					<li class="c_li" style="width:80px;">
						<input class="txt_inp_small" type="text" value="90" name="width"/>
					</li>
					<li class="c_li" style="width:120px;">
						<select name="groupName" style="display: none;">
			        	      <option value="">--请选择--</option>
			            </select>
					</li>
					<li class="c_li" style="width:40px;">
						<input name="statusId"  type="checkbox" checked="checked"/>
					</li>
					<li class="c_li" style="width:40px;">
						<input name="isNull" type="checkbox" />						
					</li>
				</ul>
			#set($childRowIndex = 1)
			#foreach($fieldBean in $childFieldList)
				#if($globals.canSelectField($fieldBean,$tableName,$childTableName) )
				<ul class="c_ul" fieldName ="$fieldBean.fieldName" tableName="$childTableName">
					<li class="c_li" style="width:60px;">
						<i class="char">
							$childRowIndex
						</i>
					</li>
					<li class="c_li" style="width:100px;">
						<i class="char">
							$fieldBean.fieldName
						</i>
					</li>
					<li class="c_li" style="width:80px;">
						<input class="txt_inp" type="text" value='$fieldBean.display.get("$globals.getLocale()")' updateType="language" onchange="getUpdateInfo(this)"/>
					</li>
					<li class="c_li" updateType="inputType" style="width:120px;">
						<select name="inputType" id="inputType" updateType="inputType" onchange="getUpdateInfo(this)">
							<option value="0" #if("$fieldBean.inputType"=="0") selected="selected" #end>输入</option>
							<option value="1" #if("$fieldBean.inputType"=="1") selected="selected" #end>下拉列表</option>
							<option value="5" #if("$fieldBean.inputType"=="5") selected="selected" #end>复选框</option>
							<option value="14" #if("$fieldBean.inputType"=="14") selected="selected" #end>部门弹出框</option>
							<option value="15" #if("$fieldBean.inputType"=="15") selected="selected" #end>职员弹出框</option>
							<option value="2" #if("$fieldBean.inputType"=="2") selected="selected" #end>自定义弹出框</option>
							<option value="fieldType:5" #if("$fieldBean.inputType"=="0" && "$fieldBean.fieldType"=="5") selected="selected" #end>短日期</option>
							<option value="fieldType:6" #if("$fieldBean.inputType"=="0" && "$fieldBean.fieldType"=="6") selected="selected" #end>长日期</option>
							<option value="fieldType:3" #if("$fieldBean.inputType"=="0" && "$fieldBean.fieldType"=="3") selected="selected" #end>备注</option>
						</select>
					</li>
					<li class="c_li" style="width:120px;">
						#if("$fieldBean.inputType" == "1")
							<select  name="$fieldBean.fieldName"  id="$fieldBean.fieldName" updateType="defaultValue" onchange="getUpdateInfo(this)">
		        		 	 	 <option value="" >--请选择--</option>
				             	 #foreach($item in $globals.getEnumerationItems("$fieldBean.refEnumerationName"))	 
				        	       <option value="$item.value"#if("$fieldBean.defaultValue" == "$item.value") selected="selected" #end>$item.name</option>
						         #end
				             </select>
				        #else
				        	
						#end
					</li>
					<li class="c_li" style="width:80px;">
						<input class="txt_inp_small" type="text" value="$fieldBean.width" updateType="width" onchange="getUpdateInfo(this)"/>
					</li>
					<li class="c_li" style="width:120px;">
						
					</li>
					<li class="c_li" style="width:40px;">
						<input name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="checkbox" #if("$fieldBean.statusId"== '1') value="1" checked="checked" #else value="0" #end  updateType="statusId" onchange="getUpdateInfo(this)"/>
					</li>
					<li class="c_li" style="width:40px;">
						<input name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="checkbox" #if("$fieldBean.isNull"== '1') value="1" checked="checked" #else value="0" #end  updateType="isNull" onchange="getUpdateInfo(this)"/>						
					</li>
				</ul>
				#set($childRowIndex = $childRowIndex+1)
				#end
			#end
			</div>
		</div>
		<!-----------分割线 wp_blt板块 End------------->
	</div>
	#end
	</div>
</form>
</body>
</html>
