<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("scope.title.add")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"  />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/tab.css" />
<style type="text/css">
p{margin:0;padding:0;}
#showUser li {cursor:pointer;background-color:white;border: 1px solid #ff8040;float:left;width:auto;margin:0 5px 5px 0;line-height:25px;padding:0 20px 0 5px;text-align:center;position:relative;}
#showUser .new-show-li{width:125px;}
.new-show-li .p-short{width:125px;display:inline-block;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;}
.new-show-li .p-long{display:none;width:125px;border:1px #bbb solid;padding:0 5px;box-shadow:0 0 8px #bbb;background:#fff;font-size:14px;position:absolute;left:0;top:0;white-space:normal;z-index:99;}
.new-show-li:hover .p-long{display:inline-block;}
.delImg{width:10px;height:10px;display:none;position:absolute;bottom:8px;right:3px;z-index:9;}
.img_op{float:right; width:12px;margin-top:2px;margin-left:8px;height:12px;cursor: pointer;}
.mpsDiv{}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/date.vjs","",$text)"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">

#if("$!type"=="5")
var addTitle = "管辖部门";
#elseif("$!type"=="1")
var addTitle = "管辖职员";
#elseif("$!type"=="0")
var addTitle = "管辖$!{tableDisplay}";
#end


putValidateItem("scopeValue",addTitle,"any","",false,0,3000);


$(document).ready(function(){	
	$("#showUser",document).find("li").live("hover",function(){
		if($(this).attr("id") != "deptAdd"){
		  $(this).children("img").toggle();
		}
	})
	$(".delImg",document).live("click",function(){
		var userId=$(this).attr("name")+";";
		var newUser=jQuery("#scopeValue",document).val().replace(userId,'');
		jQuery("#scopeValue",document).val(newUser);
		$(this).parent().remove();
	});
	selectIsAllModules();
});

function beforSubmit(form){   

	#if($!type!="0")
		//非分类资料----- 选中的子模块id集合,如果有选择一个子模块，将表名和字段名填上值，以通过验证
		var childTableIds = document.getElementsByName("moduleOperations");
		form.tableName.value = "";
		for(var i=0;i<childTableIds.length;i++){		
			if(childTableIds[i].checked && childTableIds[i].mod != "undefined" && form.tableName.value.indexOf(childTableIds[i].getAttribute("mod")+";")<0){
				 form.tableName.value+=childTableIds[i].getAttribute("mod")+";";
			}
		}	
		var addTitle = "$!{tableDisplay}管辖职员";
	#end
	
	if(!validate(form))return false;	
	
	disableForm(form);
	window.save=true;
	return true;
}

function save(){
	if(beforSubmit(document.form)){
		form.winCurIndex.value='$!winCurIndex'; 
		document.form.submit();
	}else{
		return false;
	}
}


var displayDiv = new Array();
function selectAllOperation(tagName,targetId,mid,module){
	if($("#"+targetId,document).attr("checked")){
		$("#pindex_"+mid+" input[type='checkbox']",document).attr("checked","checked");
	}else{
		$("#pindex_"+mid+" input[type='checkbox']",document).removeAttr("checked");
	}
}

function selectAllModule(obj){	
	ched = obj.checked;
	$("#tagContent",document).find("input[type='checkbox']").each(function(){
		if($(this).attr("mall") != "undefined"){
			$(this).attr("checked",ched);
		}
	});
}

function selectAllChildOperation(tagName,targetId,mod){
	items = document.getElementsByName(tagName);
	for(var i=0;i<items.length;i++){		
		if(items[i].mod != "undefined" && items[i].getAttribute("mod") == mod ){
			items[i].checked = document.getElementById(targetId).checked;
		}		
	}
}
//部门弹出窗

function deptPop(popname){	
	condition=''
	#if($!type=="5")
	popname="deptGroup";
	#elseif($!type=="1")
	popname="userGroup";
	condition='';
	#elseif($!type=="0")
	popname="defineClass";
	condition="$!{tableName}";
	#end
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox&condition="+condition;
	var titles = "请选择部门";
	if(popname == "userGroup"){
		titles = "请选择职员"
	}
	#if($!type=="0")
		titles="请选择$!{tableDisplay}";
	#end
	
	asyncbox.open({
	id : 'Popdiv',
	 title : titles,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。

　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。

				var employees = opener.strData;
				newOpenSelect(employees);
　　　　　	}
　　　	}
　	});
}

function newOpenSelect(employees){
	if(employees == "") return; 		
	var employees = employees.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			if(jQuery("#scopeValue").val().indexOf(";"+field[0]+";")==-1 && jQuery("#scopeValue").val().indexOf(field[0]+";") != 0){
				jQuery("#scopeValue").val(jQuery("#scopeValue").val()+field[0]+";");
				var newUser="<li class='new-show-li'><p class='p-short'>"+field[1]+"</p><p class='p-long'>"+field[1]+"</p><img class='delImg' name='"+field[0]+"' src='/style/images/del.gif' alt='撤销'/></li>"
				jQuery(newUser).insertBefore("#deptAdd");
			}		
		}
	}
}

function fillData(data){
	newOpenSelect(data);
	parent.jQuery.close('Popdiv');
}

function selectIsAllModules(){
	chk = $("#isAllModules",document).attr("checked");
	if(chk){
		$("#rightDiv",document).show();
		$("#modulelb",document).hide();
	}else{
		#if($!type!="0")
		$("#rightDiv",document).hide();
		#end
		$("#modulelb",document).show();
	}
}
function goLocal(type){	
	oldId = $(".selectTag",document ).attr("id");
	oldId = oldId.substring(4);
	$("#pindex_"+oldId,document ).hide();
	$(".selectTag",document ).removeClass("selectTag");
	$("#pindex_"+type,document).show();
	$("#tag_"+type,document ).attr("class","selectTag");
}
</script>
	</head>
	<body>
		<iframe name="formFrame" style="display:none"></iframe>
		<form method="post" scope="request" name="form" action="/RoleAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
			#if("$!scopeBean.id"!="")
			<input type="hidden" name="operation" value="$globals.getOP("OP_SCOPE_RIGHT_UPDATE")"/>
			#else
			<input type="hidden" name="operation" value="$globals.getOP("OP_SCOPE_RIGHT_ADD")"/>
			#end
			<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/>
			<input type="hidden" name="tableName" value="$!tableName"/>
			<input type="hidden" name="fieldName" value="#if($!desys==3)CRMClientInfoEmp.EmployeeID#end"/>
			<input type="hidden" name="roleId" value="$roleId"/>
			<input type="hidden" name="roleScopeId" value="$!scopeBean.id"/>
			<input type="hidden" id="scopeValue" name="scopeValue" value="$!scopeBean.scopeValue"/>
			<input type="hidden" id="escopeValue" name="escopeValue" value=""/>
			<input type="hidden" name="flag" value="$!type"/>
			<input type="hidden" name="roleName" value="$!roleName"/>
			<input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
			<input type="hidden" name="fromUser" value="$!fromUser">
			<div id="listRange_id" style="margin:3px 0 0 0;">
				<div id="flagDept" style="display:block;padding:0px;" class="listRange_1">
				<ul id="showUser" style="margin-top:0px; clear:both;padding:2px;overflow:hidden; "> 
					#foreach($!row in $scopeValueList)
					<li class="new-show-li">
						<p class="p-short">$!row.name</p>
						<p class="p-long">$!row.name</p>
						<img class='delImg' name='$!row.value' src='/style/images/del.gif' alt='撤销'/>
					</li>
					#end	
					<li id="deptAdd" style="background-color:#FFF;padding:0;float:left;border:none;" onClick="deptPop();" >
						<img class="img_op" style="float:left;padding:5px 0 0 1px;width:16px;height:16px;" src="/style/themes/icons/edit_add.png"  title="添加"/>
						<span style="width:20px;padding:5px 0 0 5px">添加</span>
					</li>
				</ul>
				</div> 
				<div id="isAllModulesDiv" style="display:block" class="listRange_1">  
													
					<label for="isAllModules" style="overflow:hidden;">
						<input id="isAllModules" name="isAllModules" type="checkbox" value="1" style="float:left;width:15px;margin:2px 2px 0 0;" #if("$!scopeBean" == "" || "$!scopeBean.isAllModules" == "1" ) checked #end onClick="selectIsAllModules()" />	
						<i style="float:left;display:inline-block;">应用于所有模块</i>
					</label>	
					
					
					<div style="float:left;" id="rightDiv">	
						#if($!type=="0")	
						<!--  	<div style="float:left;width:280px;padding-left:40px">
																
								<label for="isAddLevel" style="overflow:hidden;">
									<input id="isAddLevel" name="isAddLevel" type="checkbox" value="1" style="float:left;width:15px;margin:2px 2px 0 0;"#if("$!scopeBean.isAddLevel" == "1") checked #end />	
									<i style="float:left;display:inline-block;">允许添加同级</i>
								</label>
							</div> -->
						#else		
							<div style="float:left;width:80px;padding-left:40px">授与权限：(</div>
							<div style="float:left;width:60px;">
								<label for="RightTypeQuery" style="overflow:hidden;">
									<input type="checkbox" name="RightTypeQuery" id="RightTypeQuery" style="float:left;width:15px;margin:2px 2px 0 0;"  checked disabled=true value="true" />
									<i style="float:left;display:inline-block;">查询</i>
								</label>
							</div>
							<div style="float:left;width:60px;" style="overflow:hidden;">
								<label for="rightUpdate">
									<input type="checkbox" name="rightUpdate" id="rightUpdate" style="float:left;width:15px;margin:2px 2px 0 0;" #if("$!scopeBean.rightUpdate" != "0") checked #end   value="true"/>
									<i style="float:left;display:inline-block;">修改</i>
								</label>
							</div>
							<div style="float:left;width:60px;" style="overflow:hidden;">
								
								<label for="rightDelete">
									<input type="checkbox" name="rightDelete" id="rightDelete" style="float:left;width:15px;margin:2px 2px 0 0;" #if("$!scopeBean.rightDelete" != "0") checked #end  value="true" />
									<i style="float:left;display:inline-block;">删除</i>
								</label>
							</div>
							<div style="float:left;">)</div>
						#end		
					
					</div>
				</div>
				
				
				<!--权限树显示区-->
				<div class="modulelb" id="modulelb" style="display:none;">
					<fieldset style="width:100%;border: 1px solid #ccc;">
					<input id="" name="" type="checkbox" value="" style="border:0px;width:16px;" onClick="selectAllModule(this)" />
					$text.get("role.lb.allRight")
						
		<ul id="tags">
			 #set($num=0)
			 #foreach($module in $allModule) 
			 	##if($module.mainModule !='2')
					<li id="tag_$module.getId().trim()"  #if($num == 0) class="selectTag" #end >
						<a onClick="goLocal('$module.getId().trim()');" style="cursor: pointer;">
							<span style="margin: 0px 5px 0px 5px ;">$module.modelDisplay.get($globals.getLocale())</span>
						</a>
					</li>
					#set($num=$num+1)
				##end
			 #end
			
		</ul>
		<div id="tagContent" style="width:99%;height:280px;">
	     #set($num=1)
		 #foreach($module in $allModule) 
		 	##if($module.mainModule !='2')
				<div id="pindex_$module.getId().trim()" name="pindex_$module.getId().trim()" #if($num!=1) style="display:none" #end  >						   
						#set($num=$num+1)  
							<div class="scroll_function_small_a" style="height:220px;">
								<table border="0" cellpadding="0" cellspacing="0"
									class="listRange_list">
									<thead>
										<tr class="adminthead" style="text-align:center;">
										#if("$!type"==0)
											<td style=" width:150px;" align="left" >&nbsp; 
												模块
											</td>
											#foreach ($operation in $globals.getEnumerationItems("EOperation"))
												<!-- 只保留增加，删除，修改，新增的权限 -->
												#if($operation.value =="1" )
													<td style="width:50px;" align="center" >
													&nbsp;<input id="checkall$module.getId().trim()" name="myOpertions" type="checkbox" value="$globals.get($operation,0)" mall=mall style="border:0px;width:16px;" onClick="selectAllOperation('moduleOperations','checkall$module.getId().trim()','$module.getId().trim()','${$module.getId().trim()}')" />
													选择</td>	
												#end
											#end
										#else
											<td style=" width:150px;" align="left" >&nbsp;<input id="checkall$module.getId().trim()" name="myOpertions" type="checkbox" value="$globals.get($operation,0)" mall=mall style="border:0px;width:16px;" onClick="selectAllOperation('moduleOperations','checkall$module.getId().trim()','$module.getId().trim()','${$module.getId().trim()}')" />
												$text.get("role.lb.allRight")
											</td>

											#foreach ($operation in $globals.getEnumerationItems("EOperation"))
												<!-- 只保留增加，删除，修改，新增的权限 -->
											#if($operation.value =="1" ||  $operation.value =="3" || $operation.value =="4")
													<td style="width:50px;" align="center" ><input type="checkbox" style="border:0px;width:16px;" onClick="checkColumn('$operation.value$module.getId().trim()',this)"/>$operation.name</td>	
												#end
											#end
										#end

										</tr>
									</thead>
									<tbody>
										#foreach ($child in $modulesMap.get($module.getId().trim()))
										#set($disName=$child.getModelDisplay().get("zh_CN")) 
										#if(true)
										<input type="hidden" name="moduleID" value="$child.getId().trim()" />
										<tr>
										#if("$!type"==0)
											<td style="text-align:left;">												
												$child.getModelDisplay().get($globals.getLocale())
											</td>
								
											#foreach ($operation in $globals.getEnumerationItems("EOperation"))
											<!-- 只保留增加，删除，修改，新增的权限 -->
											#if($operation.value =="1")						
											<td style="text-align:center;">
												#foreach ($mop in $child.moduleoperationinfo)
												#if($mop.operationID == $operation.value )
												#if("$!roleModules.get($mop.id)" != "" || "$!fromUser" == "true")
												<input id="check$module.getId().trim()" mall=mall
													type="checkbox" mid="$module.getId().trim()"
													mcd="$operation.value$module.getId().trim()"
													mod="$child.getId().trim()"
													#if($scopeRoleModules.get($mop.moduleOpId)) checked #end  
													value="$mop.moduleOpId" name="moduleOperations"
													style="border:0px;width:16px;" />
												 #end #end #end &nbsp;
											</td>
											#end
											#end
										#else
											<td style="text-align:left;">
												<input id="checkopall$child.getId().trim()"
													name="myChildOpertions" type="checkbox"
													value="$child.getId().trim()" mall=mall
													style="border:0px;width:16px;"
													onClick="selectAllChildOperation('moduleOperations','checkopall$child.getId().trim()','$child.getId().trim()')" />
												$child.getModelDisplay().get($globals.getLocale())
											</td>
								
											#foreach ($operation in $globals.getEnumerationItems("EOperation"))
											<!-- 只保留增加，删除，修改，新增的权限 -->
											#if($operation.value =="1" ||  $operation.value =="3" || $operation.value =="4")						
											<td style="text-align:center;">
												#foreach ($mop in $child.moduleoperationinfo)
												#if($mop.operationID == $operation.value )
												#if("$!roleModules.get($mop.id)" != "" || "$!fromUser" == "true")
												<input id="check$module.getId().trim()" mall=mall
													type="checkbox" mid="$module.getId().trim()"
													mcd="$operation.value$module.getId().trim()"
													mod="$child.getId().trim()"
													#if($scopeRoleModules.get($mop.moduleOpId)) checked #end  
													value="$mop.moduleOpId" name="moduleOperations"
													style="border:0px;width:16px;" />
												 #end #end #end &nbsp;
											</td>
											#end
											#end
										#end	
										</tr>
										#end
										#end
									</tbody>
								</table>
							</div>
						</div>
						##end  allModule
						#end
					</DIV>	
					</fieldset>
				</div>
			</div>
		</form>
	</body>

</html>

