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

function beforeSubmit(){
	var errorFlag = "false";//用于判断
	var tableNameStr = "";//表中文名
	var childTableInfo = "";//明细表名s
	var childDisplayFields = "";//明细字段s
	$(".tableStatus:checked").each(function(){
		var optionLength = $(this).parents(".inputbox").find("select[name$='ShowFields'] option").length;
		childTableInfo += $(this).attr("tableName")+",";
		if(optionLength==0){
			errorFlag ="true";
			tableNameStr = $(this).prev().text();
			return false;
		}else{
			$(this).parents(".inputbox").find("select[name$='ShowFields'] option").each(function(){
				childDisplayFields +=$(this).val()+",";
			})
		}
	})
	
	if(errorFlag == "true"){
		alert(tableNameStr+"不能为空");
		return false;
	}
	
	$("#childTableInfo").val(childTableInfo);
	$("#childDisplayFields").val(childDisplayFields);
	form.submit();
}


document.onkeydown = keyDown; 
</script>
</head>
<body>
	<form method="post" scope="request" name="form" action="/ClientSettingAction.do" class="mytable">
		<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')"/>
		<input type="hidden" name="updType" id="updType" value="childDisplay"/>
		<input type="hidden" name="viewId" id="viewId" value="$!viewId"/>
		<input type="hidden" name="moduleId" id="moduleId" value="$!moduleId"/>
		<input type="hidden" name="childTableInfo" id="childTableInfo" value="$!moduleViewBean.childTableInfo"/>
		<input type="hidden" name="childDisplayFields" id="childDisplayFields" value="$!moduleViewBean.childDisplayFields"/>
		<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span><img src="/style1/images/ti_002.gif" />
						</span>
						<span>明细显示设置</span>
						<div>
							<input type="button" class="bu_02" onclick="beforeSubmit();" value="保存" />
							<input type="button" class="bu_02" onclick="javascript:window.location.href='/FieldScopeSetAction.do?operation=4&queryType=fieldScopeSet&moduleId=$!moduleId'" value="返回" />
						</div>
					</div>
					<div id="cc" class="data"
						style="overflow:hidden;overflow-y:auto;width:100%;">
						<script type="text/javascript">
							var oDiv=document.getElementById("cc");
							var sHeight=document.documentElement.clientHeight-55;
							oDiv.style.height=sHeight+"px";
						</script>
						
						#foreach($tableBean in $childTableList)
							#set($tableDis = $tableBean.display.get("$globals.getLocale()"))
							#set($dbTableName = $tableBean.tableName)
							#if($dbTableName.indexOf("CRMClientInfoDet") == -1 && "$!dbTableName" != "CRMClientInfoEmp")
							<div class="boxbg2 subbox_w700">
								<div class="subbox cf">
									<div class="inputbox">
										<h4 style="margin-top: 1px;">
											<i>$tableDis</i>
											<input type="checkbox" class="tableStatus"  tableName="$dbTableName" #if($!moduleViewBean.childTableInfo.indexOf("$dbTableName")>-1)  checked="checked" #end title="启用"/>
										</h4>
										<ul class="cf">
											<div align="center" class="client_div" style="margin-left: 100px;">  
											  	<table width="100%" border="0" cellpadding="1" cellspacing="0" class="client_tab" >
												      <tr align="center" valign="middle">
												        <td style="height: 230px; width: 35%;" align="center">
												          <fieldset>
												            <legend><span style="margin-left: 30px;">$text.get("com.define.canselect")</span></legend>   
															<select name='${dbTableName}SelectFields' id="${dbTableName}SelectFields" multiple="multiple" size="10"  >
															    #foreach($field in $tableBean.fieldInfos)
															    	#set($str = ${dbTableName}+"_"+$field.fieldName)
															    	#set($childDisplayFieldsStr = "")
															    	#set($childDisplayFieldsStr = "$!moduleViewBean.childDisplayFields")
																    #if($globals.canSelectField($field,$!mainTableName,$dbTableName)==true && $!childDisplayFieldsStr.indexOf($str)==-1)
																		#set($dis=$globals.getLocaleDisplay("$field.display"))
														    			<option value="$str">$dis</option> 
															    	#end
															    #end												  
															</select>
												          </fieldset>
												       
												        </td>
												        <td align="center" style="vertical-align: middle;width:15%;padding-left: 12px;"> 	
												          <span onclick="MoveOrDelete('${dbTableName}SelectFields','${dbTableName}ShowFields');"  class="moveright"></span><br/><br/>
												          <span onclick="MoveOrDelete('${dbTableName}ShowFields','${dbTableName}SelectFields');"  class="moveleft"></span>
												        </td>
												        <td style="width: 35%;" align="left">
												          <fieldset >
												           <legend><span style="margin-left: 30px;">显示字段</span></legend>
														   <select name='${dbTableName}ShowFields' id="${dbTableName}ShowFields" multiple="multiple" size="10" >
																#foreach($fieldNameStr in $moduleViewBean.childDisplayFields.split(","))
																	#if($fieldNameStr.indexOf("${dbTableName}")>-1)
																		#set($fieldName = $globals.replaceString($fieldNameStr,"${dbTableName}_",""))
																		#set($fieldBean = $globals.getFieldBean("$dbTableName",$fieldName))
																		#set($dis=$globals.getLocaleDisplay("$fieldBean.display"))
																		<option value="${dbTableName}_${fieldBean.fieldName}">$dis</option> 
																	#end
																#end
																
														   </select>
												         </fieldset>
												       
												        </td>
												        <td align="center" style="vertical-align: middle;padding: 0px;width: 15%;">
												           <span  onclick="MoveFirst('${dbTableName}ShowFields');"  class="movefirst"></span><br/><br/>
												           <span  onclick="MoveUp('${dbTableName}ShowFields');"  class="moveup"></span><br/><br/>
												           <span  onclick="MoveDown('${dbTableName}ShowFields');"  class="movedown"></span><br/><br/>
												           <span  onclick="MoveLast('${dbTableName}ShowFields');"  class="movelast"></span><br/>
												        </td>
												      </tr>
												    </table>										
											    </div>					
										</ul>
									</div>
								</div>
							</div>
							#end
						#end
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
