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
	if(!checkForm())
		return false;
	if(!checkScope()){
		alert("请为选中的字段设置权限!");
		return false;
	}
	getShowContent("ShowFields","fieldsName");
	form.submit();
}


function checkForm(){
	var selectLen=$("#ShowFields option").size();
	if(!isNull(selectLen,"字段权限设置"))
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

function checkScope(){
	var readdept=$('#readpopedomDeptIds').val();
	var readuser=$('#readpopedomUserIds').val();
	var hiddendept=$('#hiddenpopedomDeptIds').val();
	var hiddenuser=$('#hiddenpopedomUserIds').val();
	if( readdept=="" && readuser=="" && hiddendept=="" && hiddenuser==""){
		return false;
	}
	return true;
}

function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	jQuery.close('Popdiv');
}

document.onkeydown = keyDown; 
</script>
</head>
<body>
	<form method="post" scope="request" name="form"
		action="/FieldScopeSetAction.do?operation=$globals.getOP("OP_UPDATE")&updType=fieldScopeSet" class="mytable">
		<input type="hidden" name="fieldScopeId" id="fieldScopeId" value="$!fieldScope.id"/>
		<input type="hidden" name="readpopedomDeptIds" id="readpopedomDeptIds" value="$!fieldScope.readpopedomDeptIds" />
		<input type="hidden" name="readpopedomUserIds" id="readpopedomUserIds" value="$!fieldScope.readpopedomUserIds" />
		<input type="hidden" name="hiddenpopedomDeptIds" id="hiddenpopedomDeptIds" value="$!fieldScope.hiddenpopedomDeptIds" />
		<input type="hidden" name="hiddenpopedomUserIds" id="hiddenpopedomUserIds" value="$!fieldScope.hiddenpopedomUserIds" />
		<input type="hidden" name="fieldsName" id="fieldsName" value="" />
		<input type="hidden" name="viewId" id="viewId" value="$!viewId" />
		<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span><img src="/style1/images/ti_002.gif" />
						</span>
						<span>修改字段权限设置</span>
						<div>
							<input type="button" class="bu_02" onclick="subForm();" value="保存" />
							<input type="button" class="bu_02" onclick="javascript:window.location.href='/FieldScopeSetAction.do?operation=4&queryType=fieldScopeSet&viewId=$!viewId'" value="返回" />
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
										字段<span style="float: right;">标<font color="red" size="0.1"> * </font>部分为必填内容</span>
									</h4>
									<ul class="cf">
										<div align="center" class="client_div" style="margin-left: 100px;">  
										  	<table width="100%" border="0" cellpadding="1" cellspacing="0" class="client_tab" >
											      <tr align="center" valign="middle">
											        <td style="height: 230px; width: 35%;" align="center">
											          <fieldset>
											            <legend><span style="margin-left: 30px;">$text.get("com.define.canselect")</span></legend>   
														<select name='SelectFields' id="SelectFields" multiple="multiple" size="10"  >
														    #set($selectStr=","+$fieldScope.fieldsName)				
														    #foreach($field in $fieldList)	
														    	#set($str=","+$field.fieldName+",")
														    	#set($str2=",contact"+$field.fieldName+",")
													   	   		#if($globals.canSelectField($field,$!mainTableName,$!childTableName))
													   	   			#if(!($selectStr.indexOf($str)>-1) && !($selectStr.indexOf($str2)>-1))
															   	   		#if($field.tableBean.tableName=="$!mainTableName")
																			#set($dis=$globals.getLocaleDisplay("$field.display"))
																    		<option value="$field.fieldName">$dis</option> 
															    		#else
																			#set($dis=$globals.getLocaleDisplay("$field.display"))
															    			<option value="contact$field.fieldName">联系人--$dis</option> 
															    		#end
															    	#end
														    	#end
														    #end														  
														</select>
											          </fieldset>
											       
											        </td>
											         <td align="center" style="vertical-align: middle;width:15%;padding-left:12px;"> 	
											          <span onclick="MoveOrDelete('SelectFields','ShowFields');"  class="moveright"></span><br/><br/>
											          <span onclick="MoveOrDelete('ShowFields','SelectFields');"  class="moveleft"></span>
											        </td>
											        <td style="width: 35%;" align="left">
											          <fieldset >
											           <legend><span style="margin-left: 30px;">控制字段</span><font color="red">&nbsp;*</font></legend>
													   <select name='ShowFields' id="ShowFields" multiple="multiple" size="10" >
														    #set($pageList=$$fieldScope.fieldsName.split(","))
														    #foreach($field in $pageList)	
														    	#set($f='')
														   		#if($field.indexOf("contact")>-1)
														    		#set($newfield=$field.replaceAll('contact',''))	
												    				#set($f=$globals.getFieldBean("$!childTableName",$newfield))
												    				#if("$!f"!="")
																		#set($dis=$globals.getLocaleDisplay("$f.display"))
															    		<option value="$field">联系人--$dis</option> 
														    		#end
																#else
																	#set($f=$globals.getFieldBean("$!mainTableName",$field))
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
											           <span  onclick="MoveFirst('ShowFields');"  class="movefirst"></span><br/><br/>
											           <span  onclick="MoveUp('ShowFields');"  class="moveup"></span><br/><br/>
											           <span  onclick="MoveDown('ShowFields');"  class="movedown"></span><br/><br/>
											           <span  onclick="MoveLast('ShowFields');"  class="movelast"></span><br/>
											        </td>
											      </tr>
											    </table>										
										    </div>					
									</ul>
									<h4>
										只读对象
									</h4>
									<ul class="cf">
										<div id="_title" style="margin-top: 15px;">
											<div class="oa_signDocument1" id="_context" style="margin-left: 100px;float: left;">
												<div class="oa_signDocument_3">
													<img src="/$globals.getStylePath()/images/St.gif"
														onClick="deptPop('deptGroup','formatDeptName1','readpopedomDeptIds','1');" alt="$text.get("oa.common.adviceDept")" class="search" />
													&nbsp;
													<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName1','readpopedomDeptIds','1');">
													$text.get("oa.select.dept")</a>
												</div>
												<select name="formatDeptName1" id="formatDeptName1" size="5"
													multiple="multiple">
														#foreach($dept in $readtargetDept)
														<option value="$!globals.get($dept,14)">
															$!globals.get($dept,2) 
														</option>
														#end
												</select>
											</div>
											<div class="oa_signDocument2">
												<img onClick="deleteOpation('formatDeptName1','readpopedomDeptIds')"
													src="/$globals.getStylePath()/images/delete_button.gif"
													alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
											</div>
											<div class="oa_signDocument1" id="_context" >
												<div class="oa_signDocument_3">
													<img src="/$globals.getStylePath()/images/St.gif"
														onClick="deptPop('userGroup','formatFileName1','readpopedomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search" />
													&nbsp;
													<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName1','readpopedomUserIds','1');">$text.get("oa.select.personal")</a>
												</div>
												<select name="formatFileName1" id="formatFileName1" size="5"
													multiple="multiple">
													#foreach($user in $readtargetUsers)
													<option value="$user.id">
														$!user.EmpFullName
													</option>
													#end
												</select>
											</div>
											<div class="oa_signDocument2">
												<img onClick="deleteOpation('formatFileName1','readpopedomUserIds')"
													src="/$globals.getStylePath()/images/delete_button.gif"
													alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
											</div>
										</div>
									</ul>
									<h4>
										隐藏对象
									</h4>
									<ul class="cf">
										<div id="_title" style="margin-top: 15px;">
											<div class="oa_signDocument1" id="_context" style="margin-left: 100px;float: left;">
												<div class="oa_signDocument_3">
													<img src="/$globals.getStylePath()/images/St.gif"
														onClick="deptPop('deptGroup','formatDeptName2','hiddenpopedomDeptIds','1');" alt="$text.get("oa.common.adviceDept")" class="search" />
													&nbsp;
													<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName2','hiddenpopedomDeptIds','1');">
													$text.get("oa.select.dept")</a>
												</div>
												<select name="formatDeptName2" id="formatDeptName2" size="5"
													multiple="multiple">
														#foreach($dept in $hiddentargetDept)
														<option value="$!globals.get($dept,14)">
															$!globals.get($dept,2) 
														</option>
														#end
												</select>
											</div>
											<div class="oa_signDocument2">
												<img onClick="deleteOpation('formatDeptName2','hiddenpopedomDeptIds')"
													src="/$globals.getStylePath()/images/delete_button.gif"
													alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")" class="search" />
											</div>
											<div class="oa_signDocument1" id="_context" >
												<div class="oa_signDocument_3">
													<img src="/$globals.getStylePath()/images/St.gif"
														onClick="deptPop('userGroup','formatFileName2','hiddenpopedomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search" />
													&nbsp;
													<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName2','hiddenpopedomUserIds','1');">$text.get("oa.select.personal")</a>
												</div>
												<select name="formatFileName2" id="formatFileName2" size="5"
													multiple="multiple">
													#foreach($user in $hiddentargetUsers)
													<option value="$user.id">
														$!user.EmpFullName
													</option>
													#end
												</select>
											</div>
											<div class="oa_signDocument2">
												<img onClick="deleteOpation('formatFileName2','hiddenpopedomUserIds')"
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
