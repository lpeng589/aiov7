<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/gen/define.vjstblEmployee_zh_CN.js"></script>
<script type="text/javascript">
$(document).ready(function(){    
	$("input[class='changeInput']").change(function(){
		var list= $(this).attr("name").split("&&");   
		if($(this).attr("name").indexOf("fieldLen")>-1){
			if(isInt2($(this).val())==false){
				alert("必须输入大于0的正整数!")
				return false;
				$(this).focus();
			}
		}
		if($(this).attr("name").indexOf("thefieldName")>-1){
			if($(this).val()==""){
				alert("字段名不能为空!")
				$(this).val($(this).attr("hiddenValue"));
				$(this).focus();
				return false;
			}
		}
        var changeField=list[1]+";";
        var changeNames=list[2]+";";
        if(list[2]=="ClientNo" && list[1]=="defaultValue"){
        	if($(this).val()==""){
				alert("客户编号默认值不能为空!")
				$(this).val($(this).attr("hiddenValue"));
				$(this).focus();
				return false;
			}
        }
        
        if(jQuery(this).attr("id") == "popSelected"){
        	//判断是否有配置不存在的弹出框
			var selectName = "";
			var isNoSelectName ="false";
			selectName = $(this).val();
			if(selectName!=""){
				jQuery.ajax({
				type: "POST",
				url: "/CRMClientAction.do",
				data: "operation=4&type=popupSelectName&selectName="+selectName,
				async: false,
				success: function(msg){
					if(msg != "false"){
						alert("没有名为"+msg+"的自定义弹出框,请重新设置");
						isNoSelectName = "true";
					}
				}
			});
			}
			
			if(isNoSelectName == "true"){
				return false;
			}
        }
        
        
		var value="";
        if($(this).val()=="")
        	value=" ";
        else
        	value=$(this).val();
        	
        var changeValue=value+";";
		$("#changeField").val($("#changeField").val()+changeField);  // 标识更改的字段  
		$("#changeValue").val($("#changeValue").val()+changeValue);  //标识更改后的值  
		$("#changeName").val($("#changeName").val()+changeNames);  //标识记录的fieldName
	});
	  
    $("input[type='checkbox']").click(function (){
 		if($(this).val()=="0"){
 			$(this).val("1");
 		}else{
 			$(this).val("0");
 		}
 		var list= $(this).attr("id").split("&&");    
       	var changeField=list[0]+";";
        var changeName=list[1]+";";
        var value="";
        if($(this).val()==""){
        	value=" ";
        }else{
        	value=$(this).val();
        }
        var changeValue=value+";";
        $("#changeField").val($("#changeField").val()+changeField);
		$("#changeValue").val($("#changeValue").val()+changeValue);
		$("#changeName").val($("#changeName").val()+changeName);
 	});
 	
	$("select[name^='changeField']").change(function(){
	 	var list=$(this).children("option").parent().attr("name").split("&&");
	 	var changeField=list[1]+";";
        var changeName=list[2]+";";
        var changeValue=$(this).children("option:selected").val()+";";
        $("#changeField").val($("#changeField").val()+changeField);
		$("#changeValue").val($("#changeValue").val()+changeValue);
		$("#changeName").val($("#changeName").val()+changeName);
	 	
	});
 });

 
 function subForm(){
 
 	if($("#changeField").val()==""){
 		$("#changeField").val("1");
 	}
 	var pattern = /^[a-z\d\u4E00-\u9FA5]+$/i;   /*正则表达式，只能输入中文、字母、英文*/
 	var fieldNameFlag = "false";
 	$("input[name='fieldNameNew']:visible").each(function(){
 		if($(this).val() == ""){
 			alert("显示名称不能为空,请重新输入");
 			$(this).focus();
 			fieldNameFlag = "true";
 		}else if(!isNaN($(this).val().charAt(0))){
 			alert("显示名称不能以数字开头,请重新修改");
 			$(this).select();
 			fieldNameFlag = "true";
 		}else if(!pattern.test($(this).val())){
			alert( "显示名称含有特殊字符,只能输入中文、字母、数字"); 
			$(this).select();
			fieldNameFlag = "true";
		}else if($(this).val()=="asc" || $(this).val()=="ASC" || $(this).val()=="desc" || $(this).val()=="DESC"){
 			alert("显示名称不能用asc或desc命名,请重新修改");
 			$(this).select();
 			fieldNameFlag = "true";
 		}
 		if(fieldNameFlag == "true"){
 			return false;
 		}
 	})
 	
 	
 	var error ="false";//判断新增的宽度 
 	$("input[name='widthNew']:visible").each(function(){
 		if(isInt2($(this).val())==false){
			alert("必须输入大于0的正整数!")
			$(this).focus();
			error ="true";
			return false;
		}
 	})
 	
 	if(fieldNameFlag == "true"){
 		return false;
 	}
 	
 	//判断新增的宽度  
 	if(error == "true"){
 		return false;
 	}
 	
	var isError ="false";//true表示不提交表单 
	var returnVal ="false";//检验的返回值 
 	$("tr[id='clientRowModle']:visible").each(function(){
 		returnVal = checkFieldName('clientRowModle',$(this).find("input[name='fieldNameNew']"));
 		if(returnVal == true || $("#isHasFlag").val() == "true"){
 			//存在相同的名称或已存在中文返回true,跳出循环
 			isError = "true";
 			return false;
 		}
 	})
 	
 	//true已有相同的不再进入查询联系人部分
 	if(isError =="false"){
	 	$("tr[id='contactRowModle']:visible").each(function(){
	 		returnVal = checkFieldName('contactRowModle',$(this).find("input[name='fieldNameNew']"));
		 	if(returnVal == true || $("#isHasFlag").val() == "true"){
		 		//存在相同的名称或已存在中文返回true,跳出循环
	 			isError = "true";
	 			return false;
	 		}
	 	})
 	}
 	
 	if(isError == "true"){
	 	return "false";
 	}
 	
 	//判断是否有配置不存在的弹出框
	var selectName = "";
	jQuery("input[name='defineTableName']:visible").each(function(){
		selectName +=$(this).val()+",";
	}) 	
	
	var isNoSelectName ="false";
	if(selectName!=""){
		jQuery.ajax({
		type: "POST",
		url: "/CRMClientAction.do",
		data: "operation=4&type=popupSelectName&selectName="+selectName,
		async: false,
		success: function(msg){
			if(msg != "false"){
				alert("没有名为"+msg+"的自定义弹出框,请重新设置");
				isNoSelectName = "true";
			}
		}
	});
	}
	
	if(isNoSelectName == "true"){
		return false;
	}
 	
 	
 	
 	$("tr[id='clientRowModle']:last").remove();
 	$("tr[id='contactRowModle']:last").remove();
 	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
	}
 	form.submit();
 }

//存放隐藏值，用于回填
var hideObj;
var hideGroupFlag;
var hideEnumerName; 
function UpdEnum(obj,groupFlag,enumerName){
	var enumerId = obj.getAttribute("enumerId");
	if(obj.value == "update"){
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
					hideGroupFlag = groupFlag;
					hideEnumerName = enumerName;
					opener.beforSubmit(opener.document.form);
				}else{
					$(obj).children().first().attr("selected","selected");
					jQuery.close('EnumWinow');
				}
				return false;
			}
		});		
	}else{
		if(groupFlag == "group"){
			var groupVal = obj.getAttribute("fieldName") + ":" + obj.value
			$("#groupValues").val($("#groupValues").val() + groupVal + ",");
		}
	}
}

//选项数据回填方法
function filllanguage(datas){
	//在调用enumerationUpdate.jsp的filllanguage回填回去
	jQuery.opener('EnumWinow').filllanguage(datas);
}

//选项数据操作成功后,处理方法
function dealAsyn(){
	var obj = hideObj;
	var groupFlag = hideGroupFlag;
	var enumerName = hideEnumerName;
	jQuery.ajax({
	   type: "POST",
	   url: "/ClientSettingAction.do?operation=4&queryType=reloadEnum&enumerName="+enumerName,
	   success: function(msg){
	 	       var str = "";
	 	       if(groupFlag != "group"){
		   str +="<option value='0'>"+$(obj).find("option:first").text()+"</option>"
	 	       }
		   str +=msg;
		   str +="<option value='update'>编辑选项数据</option>";
		   $(obj).children().first().attr("selected","selected");
		   if(groupFlag == "group"){
			$("select[name='groupName']").each(function(){
				var optionVal = $(this).val();
				$(this).children().remove()
		   	    $(this).append(str);
		   	    $(this).children().each(function(){
		   	    	if($(this).val() == optionVal){
		   	    		$(this).attr("selected","selected");
		   	    	}
		   	    })
			})
			
			var newStr = '<select name="groupNameNew" id="groupNameNew" value="" style="width: 83%;">'
			newStr += msg;
			newStr +="</select>"
			$("select[name='groupNameNew']").each(function(){
				var optionValues = $(this).val();
				jQuery(this).before(newStr);
				jQuery(this).remove();
			})
		   }else{
			   $(obj).children().remove()
			   $(obj).append(str);
		   }
	   }
	});
	jQuery.close('EnumWinow');	
}

function addRow(addType){
	var count
	var $copy
	var $selTr
	if(addType == "client"){
		count = $("#clientTable tr").length+1;
		$copy = $("#addMainRowCopy").html();
		$selTr = $("#clientTable tr:first");
	}else{
		count = $("#"+addType+"Table tr").length+1;
		$copy = $("#addChildRowCopy").html();
		$selTr = $("#"+addType+"Table tr:first");
	}
	$selTr.before($copy);
	$selTr = $selTr.prev();
	
	$selTr.show();
	$selTr.find("td:first").html(count);
	$selTr.find("input:first").focus();
	if(count%2 ==0){
		$selTr.addClass("c2");
	}else{
		$selTr.addClass("c1");
	}
}

function changeCheckVal(obj){
	if($(obj).attr("checked") == "checked"){
		$(obj).next().val("1");
	}else{
		$(obj).next().val("0");
	}
}
 
function delTr(obj){
	$(obj).parent().parent().remove();
}

function delField(fieldName,obj){
	var tableName=$(obj).parents("table").attr("tableName");
	if(confirm("确定删除?")){
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
		}
		jQuery.ajax({
		   type: "POST",
		   url: "/ClientSettingAction.do?operation=3&delType=delField&fieldName="+fieldName+"&tableName="+tableName,
		   success: function(msg){
		   		if(msg == "ok"){
		   			asyncbox.tips('删除成功','success');
		   			$(obj).parent().parent().remove();
		   		}else{
		   			asyncbox.tips('删除失败','error');
		   		}
		   		if(typeof(top.junblockUI)!="undefined"){
					top.junblockUI();
				}
		   }
		});
	}
}

//检查是否有相同文中与相同显示名称








function checkFieldName(trName,obj){
	var temp =0;
	$("tr[id='"+trName+"']:visible").each(function(){
		if($(obj).val() == $(this).find("input[name='fieldNameNew']").val()){
			temp++;
		}
	})
	if(temp >=2){
		alert("不能输入相同的显示名称");
		$(obj).select();
		return true;
	}
	
	jQuery.ajax({
	   type: "POST",
	   url: "/ClientSettingAction.do?operation=4&queryType=checkFieldName&moduleId=$moduleId&viewId=$viewId&checkFlag="+trName+"&fieldName="+encodeURIComponent(encodeURIComponent($(obj).val())),
	   async: false,
	   success: function(msg){
	       if(msg == "yes"){
	       		alert($(obj).val() + "已存在,请修改");
	       		$(obj).select();
	       		$("#isHasFlag").val("true");//存在相同标识
	       }else{
	       		$("#isHasFlag").val("");
	       }	  
	   }
	});
}

function selectInputType(obj){
	if($(obj).val() == 2){
		$(obj).parent().next().find("input").show();
		$(obj).parent().next().find("input").val("请输入弹出框名称");
	}else{
		$(obj).parent().next().find("input").hide();
	}
}

function dealDefineName(obj){
	if($(obj).val() == "请输入弹出框名称"){
		$(obj).val("");
	}
}

function blurDefineName(obj){
	if($(obj).val() == ""){
		$(obj).val("请输入弹出框名称");
	}
}


 document.onkeydown = keyDown; 
</script>
</head>
<body>
<form method="post" scope="request" name="form"
	action="/ClientSettingAction.do?operation=$globals.getOP('OP_UPDATE')&updType=fieldsMtain" class="mytable" >
		<input type="hidden" value="" id="changeField"  size="50" name="changeField"/>
		<input type="hidden" value="" id="changeValue" size="50" name="changeValue"/>
		<input type="hidden" value="" id="changeName" size="50" name="changeName"/>
		<input type="hidden" name="moduleId" id="moduleId" value="$!moduleId"/>
		<input type="hidden" name="groupValues" id="groupValues" value=""/>
		<input type="hidden" name="isHasFlag" id="isHasFlag" value=""/>
		<table cellpadding="0" cellspacing="0" border="0" class="framework" >
			<tr>
				<td>
					<div class="TopTitle">
						<span>
						当前位置:模板字段维护   
						</span>
						<span style="float: right;font-weight: normal;margin-right: 12px;margin-bottom: 4px;">
							<input type="button" class="bu_02"  onclick="subForm();" value="保存" />	
						</span>
						<span style="float: right; margin-right: 20px;">注:输入框可编辑</span>
					</div>
					
					<table class="data_list_head"  cellpadding="0" cellspacing="0">
						<thead>
							<tr>
								<td width="20">序号</td>
								<td width="12%" >原始名称</td>
								<td width="15%">显示名称</td>								
								<td width="10%">输入类型</td>
								<td  width="11%">选项设置</td>
								<td width="10%;">列表字段宽度</td>
								<td  width="15%" >分组</td>
								<td  width="7%" >启用</td>
								<td  width="6%">必填</td>
								<td  width="10%" title="是否允许重复值">是否唯一</td>
							</tr>
						</thead>
					</table>
					<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow-y:auto;width:99%;margin-top: 0px;" >
<script type="text/javascript">
	var oDiv=document.getElementById("data_list_id");
	var sHeight=document.documentElement.clientHeight-90;
	oDiv.style.height=sHeight+"px";
</script>
		<span style="float:left; width:100%;font:bold 14px;padding-left: 12px;height: 25px;"><b style="float: left;margin-top: 2px;"> 客户信息</b> <img src="/style/images/plan/Ic_005.gif" title="添加客户字段" style="cursor: pointer;float: left;margin-top: 4px;margin-left: 8px;" onclick="addRow('client')"/></span>
			<table cellpadding="0" cellspacing="0" style="border-left: 0px;" id="clientTable" tableName="$!mainTableName">
				<tbody>
					#set($numIndex=0) 
					#foreach ( $foo in [1..0] )
					#foreach ($field in $fieldList )
					#if("$field.statusId"=="$foo" && $globals.canSelectField($field,$!mainTableName,$!childTableName)
					&& $field.tableBean.tableName=="$!mainTableName")
					#set($numIndex=$numIndex+1)
					<tr #if($globals.isOddNumber($numIndex)) class="c1" #else class="c2" #end>
						<td width="20">$numIndex</td>
						<td width="12%"><span title="$field.fieldName">#if($field.isStat == 1) $field.display.get("$globals.getLocale()") #else 新增字段 #end</span></td>
						#set($dis=$globals.getLocaleDisplay("$field.display"))
						#set($languageId="languageId%%"+$field.languageId)
						<td width="15%" style="vertical-align: middle;">
							<input type="text" class="changeInput" hiddenValue="$dis" value="$dis" name="changeField&&$languageId&&$field.fieldName&&thefieldName" />
						</td>
						<td width="10%">
							$globals.getInputTypeForModule("$field.inputType","$field.fieldType")
						</td>
						<td width="11%" style="vertical-align: middle;">
							#if("$field.inputType"=="1" || "$field.inputType"=="5")
							<select name="changeField&&defaultValue&&$field.fieldName"
								onchange="UpdEnum(this,'','$field.refEnumerationName')" enumerId='$!enumerMap.get("$field.refEnumerationName")'
								value="" class="changeInput" style="width: 83%;" fieldName = "$field.fieldName" >
								<option value="0">--请选择$field.display.get("$globals.getLocale()")--</option>
								#foreach($row in
								$globals.getEnumerationItems("$field.refEnumerationName"))
								<option value="$row.value" #if("$field.defaultValue"==$row.value) selected #end>$row.name</option>
								#end
								
								<option value="update">编辑选项数据</option>
								
							</select>
							#elseif("$field.inputType"=="2")
								<input class="changeInput" name="changeField&&inputValue&&$!field.fieldName&&thefieldName" hiddenValue="$!field.inputValue"  type="text" value="$!field.inputValue" id="popSelected"/>
							#else
							<!-- <input type="text" class="changeInput" hiddenValue="$field.defaultValue"
								value="$field.defaultValue" name="changeField&&defaultValue&&$field.fieldName" /> -->
							#end
						</td>
						<td width="10%" style="vertical-align: middle;">
							<input type="text" class="changeInput" style="width: 50px;" value="$field.width"
								name="changeField&&width&&$field.fieldName&&fieldLen" />
						</td>
						<td width="15%" style="vertical-align: middle;">
							<select name="groupName" value="" style="width: 83%;" enumerId='$!enumerMap.get("$field.tableBean.tableName")' fieldName='$field.fieldName' onchange="UpdEnum(this,'group','$field.tableBean.tableName')" >
								#foreach($row in $globals.getEnumerationItems("$field.tableBean.tableName"))
								<option value="$row.value" #if("$field.groupName"=="$row.value") selected #end>$row.name</option>
								#end
								#if($loginBean.operationMap.get("/AdvanceAction.do").update())
									<option value="update">编辑选项数据</option>
								#end
							</select>
						</td>
						<td width="7%" style="vertical-align: middle;padding-left: 20px;">
							#if($field.fieldName !="ClientName")<input name="$field.id"  type="checkbox"  id="statusId&&$field.fieldName" #if("$field.statusId"=='1') checked="checked" value="1" #end  value="0" />#end
						</td>
						<td width="6%"	style="vertical-align: middle;padding-left: 20px;">
							<input name="$field.id" id="isNull&&$field.fieldName" type="checkbox" #if("$field.isNull"== '1') value="1"
								checked="checked" #else value="0" #end />
								
						</td>
						<td width="10%" style="vertical-align: middle;padding-left: 20px;">
							<input name="$field.id"  type="checkbox"  id="isUnique&&$field.fieldName" #if("$field.isUnique"=='1') checked="checked" value="1" #end  value="0" />
							#if($field.isStat == 1)
							#else
								&nbsp;&nbsp;&nbsp;
								<img src="/style/images/plan/del_02.gif" width="10px;" height="10px;" style="cursor: pointer;" onclick="delField('$field.fieldName',this)"/>
							#end
						</td>
					</tr>
					#end #end
					 #end
				</tbody>
			</table>
			<span style="float:left;  font:bold 14px;padding-left: 12px;height: 25px;"><b style="float: left;margin-top: 2px;">联系人信息</b><img src="/style/images/plan/Ic_005.gif" title="添加联系人字段" style="cursor: pointer;float: left;margin-top: 4px;margin-left: 8px;" onclick="addRow('$!childTableName')"/></span>
			<table  cellpadding="0" cellspacing="0" style="border-left: 0px;border-bottom:0px;" id="${childTableName}Table" tableName="$!childTableName">					
				<tbody>
					#set($hideValue = "change_CRMClientInfoDet")
					<input type="hidden" name="fieldNameNew" value="$hideValue"></input>
					<input type="hidden" name="inputTypeNew" value="$hideValue"></input>
					<input type="hidden" name="defineTableName" value="$hideValue"></input>
					<input type="hidden" name="widthNew" value="$hideValue"></input>
					<input type="hidden" name="groupNameNew" value="$hideValue"></input>
					<input type="hidden" name="statusIdVal" value="$hideValue"></input>
					<input type="hidden" name="isNullVal" value="$hideValue"></input>
					<input type="hidden" name="isUniqueVal" value="$hideValue"></input>
					
				
					#set($i=0)
					#foreach ( $foo in [1..0] )
					#foreach ($field in $fieldList )
					#if($field.fieldName != "mainUser")
						#if("$field.statusId"=="$foo" && $globals.canSelectField($field,$!mainTableName,$!childTableName) && $field.tableBean.tableName=="$!childTableName")
						#set($i=$i+1)
						<tr #if($globals.isOddNumber($i)) class="c1" #else class="c2" #end>		
							<td width="20">$i</td>						
							<td width="12%" style="vertical-align: middle;">
								<span title="$field.fieldName">#if($field.isStat == 1) $field.display.get("$globals.getLocale()") #else 新增字段 #end</span>
							</td>
								#set($dis=$globals.getLocaleDisplay("$field.display"))
								#set($languageId="contact_languageId%%"+$field.languageId)
							<td width="15%" style="vertical-align: middle;" >
								<input type="text" class="changeInput" value="$dis" name="changeField&&$languageId&&$field.fieldName"/>
							</td>
							<td width="10%">
								 $globals.getInputTypeForModule("$field.inputType","$field.fieldType")
							
							</td>
							<td width="11%" style="vertical-align: middle;">					
								#if("$field.inputType"=="1")	
									 <select name="changeField&&contact_defaultValue&&$field.fieldName"  value="" class="changeInput" style="width: 83%;"
									 onclick="UpdEnum(this,'','$field.refEnumerationName')" enumerId='$!enumerMap.get("$field.refEnumerationName")'>
										<option value="0">
												--请选择$field.display.get("$globals.getLocale()")--
										</option>
										#foreach($row in $globals.getEnumerationItems("$field.refEnumerationName"))
											<option  value="$row.value" #if("$field.defaultValue"==$row.value) selected #end>$row.name</option>
										#end
										<option value="update">编辑选项数据</option>
									 </select>
								#else
									<!--  <input type="text" class="changeInput" value="$field.defaultValue" name="changeField&&contact_defaultValue&&$field.fieldName"/>-->
								#end
							</td>
							<td width="10%" ></td>
							<td width="15%"  ></td>
							<td width="7%" style="vertical-align: middle;padding-left: 20px;">
								#if($field.fieldName !="UserName")<input name="$field.id"  type="checkbox"  id="contact_statusId&&$field.fieldName" #if("$field.statusId"=='1') checked="checked" value="1" #end  value="0" />#end
							</td>
							<td width="6%" style="vertical-align: middle;padding-left: 20px;">
								<input name="$field.id"  type="checkbox"  id="contact_isNull&&$field.fieldName" #if("$field.isNull"=='1') checked="checked" value="1" #end  value="0" />
								
							</td>
							<td width="10%" style="vertical-align: middle;padding-left: 20px;">
							<input name="$field.id"  type="checkbox"  id="contact_isUnique&&$field.fieldName" #if("$field.isUnique"=='1') checked="checked" value="1" #end  value="0" />
							#if($field.isStat == 1)
							#else
								&nbsp;&nbsp;&nbsp;
								<img src="/style/images/plan/del_02.gif" width="10px;" height="10px;" style="cursor: pointer;" onclick="delField('$field.fieldName',this)"/>
							#end
						</td>
						</tr>
						#end
					#end
					#end
					#end
				</tbody>		
			</table>
			
			#foreach($dbTableBean in $childTableList)
			#set($dbTableName = $dbTableBean.tableName)
			#if($dbTableName.indexOf("CRMClientInfoDet") == -1 && "$!dbTableName" != "CRMClientInfoEmp")
			<span style="float:left;  font:bold 14px;padding-left: 12px;height: 25px;"><b style="float: left;margin-top: 2px;">$dbTableBean.display.get("$globals.getLocale()")信息</b><img src="/style/images/plan/Ic_005.gif" title="添加联系人字段" style="cursor: pointer;float: left;margin-top: 4px;margin-left: 8px;" onclick="addRow('$dbTableName')"/></span>
			<table  cellpadding="0" cellspacing="0" style="border-left: 0px;border-bottom:0px;" id="${dbTableName}Table" tableName="$dbTableName">					
				<tbody>
					#set($hideValue = "change_"+${dbTableName})
					<input type="hidden" name="fieldNameNew" value="$hideValue"></input>
					<input type="hidden" name="inputTypeNew" value="$hideValue"></input>
					<input type="hidden" name="defineTableName" value="$hideValue"></input>
					<input type="hidden" name="widthNew" value="$hideValue"></input>
					<input type="hidden" name="groupNameNew" value="$hideValue"></input>
					<input type="hidden" name="statusIdVal" value="$hideValue"></input>
					<input type="hidden" name="isNullVal" value="$hideValue"></input>
					<input type="hidden" name="isUniqueVal" value="$hideValue"></input>
					#set($i=0)
					#foreach ($field in $dbTableBean.fieldInfos)
					#if($field.fieldName != "mainUser")
						#if($globals.canSelectField($field,$!mainTableName,$!dbTableBean.tableName))
						#set($i=$i+1)
						<tr #if($globals.isOddNumber($i)) class="c1" #else class="c2" #end>		
							<td width="20">$i</td>						
							<td width="12%" style="vertical-align: middle;">
								<span title="$field.fieldName">#if($field.isStat == 1) $field.display.get("$globals.getLocale()") #else 新增字段 #end</span>
							</td>
								#set($dis=$globals.getLocaleDisplay("$field.display"))
								#set($languageId="${dbTableName}_languageId%%"+$field.languageId)
							<td width="15%" style="vertical-align: middle;" >
								<input type="text" class="changeInput" value="$dis" name="changeField&&$languageId&&$field.fieldName"/>
							</td>
							<td width="10%">
								 $globals.getInputTypeForModule("$field.inputType","$field.fieldType")
							
							</td>
							<td width="11%" style="vertical-align: middle;">					
								#if("$field.inputType"=="1")	
									 <select name="changeField&&${dbTableName}_defaultValue&&$field.fieldName"  value="" class="changeInput" style="width: 83%;"
									 onclick="UpdEnum(this,'','$field.refEnumerationName')" enumerId='$!enumerMap.get("$field.refEnumerationName")'>
										<option value="0">
												--请选择$field.display.get("$globals.getLocale()")--
										</option>
										#foreach($row in $globals.getEnumerationItems("$field.refEnumerationName"))
											<option  value="$row.value" #if("$field.defaultValue"==$row.value) selected #end>$row.name</option>
										#end
										<option value="update">编辑选项数据</option>
									 </select>
								#elseif("$field.inputType"=="2")
									<input class="changeInput" name="changeField&&${dbTableName}_inputValue&&$!field.fieldName&&thefieldName" hiddenValue="$!field.inputValue"  type="text" value="$!field.inputValue" id="popSelected" title="$!field.inputValue"/>
								#else
									<!--  <input type="text" class="changeInput" value="$field.defaultValue" name="changeField&&${dbTableName}_defaultValue&&$field.fieldName"/>-->
								#end
							</td>
							<td width="10%" ></td>
							<td width="15%"  ></td>
							<td width="7%" style="vertical-align: middle;padding-left: 20px;">
								#if($field.fieldName !="UserName")<input name="$field.id"  type="checkbox"  id="${dbTableName}_statusId&&$field.fieldName" #if("$field.statusId"=='1') checked="checked" value="1" #end  value="0" />#end
							</td>
							<td width="6%" style="vertical-align: middle;padding-left: 20px;">
								<input name="$field.id"  type="checkbox"  id="${dbTableName}_isNull&&$field.fieldName" #if("$field.isNull"=='1') checked="checked" value="1" #end  value="0" />
								
							</td>
							<td width="10%" style="vertical-align: middle;padding-left: 20px;">
							<input name="$field.id"  type="checkbox"  id="${dbTableName}_isUnique&&$field.fieldName" #if("$field.isUnique"=='1') checked="checked" value="1" #end  value="0" />
							#if($field.isStat == 1)
							#else
								&nbsp;&nbsp;&nbsp;
								<img src="/style/images/plan/del_02.gif" width="10px;" height="10px;" style="cursor: pointer;" onclick="delField('$field.fieldName',this)"/>
							#end
						</td>
						</tr>
						#end
					#end
					#end
				</tbody>
			</table>
			#end	
			#end	
		</div>
		</td>
		</tr>
		</table>
</form>

<script type="text/html" id="addMainRowCopy"> 
<tr id="clientRowTr" style="display: none;">
	<td width="20"  style="vertical-align: middle;"></td>
	<td width="12%"  style="vertical-align: middle;"></td>
	<td width="15%"  style="vertical-align: middle;"><input class="changeInput" type="text" name="fieldNameNew" id="fieldNameNew" onchange="checkFieldName('clientRowModle',this)"/></td>
	<td width="10%" style="vertical-align: middle;">
		<select name="inputTypeNew" id="inputTypeNew" onchange="selectInputType(this)">
			<option value="0" >输入</option>
			<option value="1" >下拉列表</option>
			<option value="5" >复选框</option>
			<option value="10" >单选框</option>
			<option value="14" >部门弹出框</option>
			<option value="15" >职员弹出框</option>
			<option value="2" >自定义弹出框</option>
			<option value="fieldType:5" >短日期</option>
			<option value="fieldType:6" >长日期</option>
			<option value="fieldType:3" >备注</option>
			<option value="fieldType:18" >长字符</option>
		</select>
		
	</td>								
	<td width="11%"  style="vertical-align: middle;"><input class="changeInput" type="text" name="defineTableName" id="defineTableName" value="" style="display: none;color: gray;" onmousedown="dealDefineName(this)" onblur="blurDefineName(this)"/></td>
	<td width="10%;" style="vertical-align: middle;"><input class="changeInput" type="text" style="width: 50px;" name="widthNew" id="widthNew" value="90"/></td>
	<td width="15%"  style="vertical-align: middle;"> 
		<select name="groupNameNew" id="groupNameNew" value="" style="width: 83%;"
			onchange="UpdEnum(this,'')" enumerId='$!enumerMap.get("$!cliTableName")'>
			#foreach($row in $globals.getEnumerationItems("$!cliTableName"))
			<option value="$row.value" >$row.name</option>
			#end
		</select>
	</td>
	<td  width="7%" style="vertical-align: middle;padding-left: 20px;">
		<input type="checkbox" name="statusIdNew" id="statusIdNew" onclick="changeCheckVal(this)" checked="checked"/>
		<input type="hidden" name="statusIdVal" id="statusIdVal" value="1"/>
		&nbsp;&nbsp;&nbsp;
	</td>
	<td  width="6%" style="vertical-align: middle;padding-left: 20px;">
		<input type="checkbox" name="isNullNew" id="isNullNew" onclick="changeCheckVal(this)"/>
		<input type="hidden" name="isNullVal" id="isNullVal" value="0"/>
		&nbsp;&nbsp;&nbsp;
	</td>
	<td  width="10%" style="vertical-align: middle;padding-left: 20px;">
		<input type="checkbox" name="isUniqueNew" id="isUniqueNew" onclick="changeCheckVal(this)" />
		<input type="hidden" name="isUniqueVal" id="isUniqueVal" value="0"/>
		&nbsp;&nbsp;&nbsp;
		<img src="/style/images/plan/del_02.gif" width="10px;" height="10px;" style="cursor: pointer;" onclick="delTr(this)"/>
	</td>
</tr>
</script> 

<script type="text/html" id="addChildRowCopy"> 
<tr id="contactRowTr" style="display: none;">
	<td width="20"  style="vertical-align: middle;"></td>
	<td width="12%"  style="vertical-align: middle;"></td>
	<td width="15%"  style="vertical-align: middle;">
		<input class="changeInput" type="text" name="fieldNameNew" id="fieldNameNew"  onchange="checkFieldName('contactRowModle',this)"/>
	</td>
	<td width="10%" style="vertical-align: middle;">
		<select name="inputTypeNew" id="inputTypeNew" onclick="selectInputType(this)">
			<option value="0" >输入</option>
			<option value="1" >下拉列表</option>
			<option value="5" >复选框</option>
			<option value="2" >自定义弹出框</option>
			<option value="fieldType:5" >短日期</option>
			<option value="fieldType:6" >长日期</option>
			<option value="fieldType:3" >备注</option>
		</select>
	</td>								
	<td width="11%"  style="vertical-align: middle;"><input class="changeInput" type="text" name="defineTableName" id="defineTableName" value="" style="display: none;color: gray;" onmousedown="dealDefineName(this)" onblur="blurDefineName(this)"/></td>
	<td width="10%;" style="vertical-align: middle;"></td>
	<td  width="15%"  style="vertical-align: middle;"></td>
	<td  width="7%" style="vertical-align: middle;padding-left: 20px;">
		<input type="checkbox" name="statusIdNew" id="statusIdNew" onclick="changeCheckVal(this)" checked="checked"/>
		<input type="hidden" name="statusIdVal" id="statusIdVal" value="1"/>
		&nbsp;&nbsp;&nbsp;
	</td>
		<td  width="6%" style="vertical-align: middle;padding-left: 20px;">
		<input type="checkbox" name="isNullNew" id="isNullNew" onclick="changeCheckVal(this)"/>
		<input type="hidden" name="isNullVal" id="isNullVal" value="0"/>
		&nbsp;&nbsp;&nbsp;
	</td>
	<td  width="10%" style="vertical-align: middle;padding-left: 20px;">
		<input type="checkbox" name="isUniqueNew" id="isUniqueNew" onclick="changeCheckVal(this)"/>
		<input type="hidden" name="isUniqueVal" id="isUniqueVal" value="0"/>
		&nbsp;&nbsp;&nbsp;
		<img src="/style/images/plan/del_02.gif" width="10px;" height="10px;" style="cursor: pointer;" onclick="delTr(this)"/>
	</td>
						
</tr>
</script>




</body>
</html>