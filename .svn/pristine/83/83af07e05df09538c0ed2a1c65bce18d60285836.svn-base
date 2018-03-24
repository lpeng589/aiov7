<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.communicationManager.groupInfo")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/sharingStyle.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/listPage.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">

function goFrame(url){
	window.parent.f_mainFrame.location=url;
}

function btn(floderId,flag){
	asyncbox.open({
	 id : 'dealdiv',
　　　   url : '/OAKnowFolderCenter.do?operation=4&dealType=addGroup&folderId='+floderId + "&flag=" + flag,
	 title : '添加组',
　　　   width : 680,
　　     height : 370,
     btnsbar : jQuery.btn.OKCANCEL,
	 callback : function(action,opener){
　　　　　//判断 action 值。
　　　　　if(action == 'ok'){
		   var re = /^\+?[0-9][0-9]*$/;　　//正整数

		   var folderName = opener.document.getElementById("folderName");
		   var userIds = opener.document.getElementById("popedomUserIds");
		   var deptIds = opener.document.getElementById("popedomDeptIds");
		   var downDept = opener.document.getElementById("downDeptIds");
		   var downUser = opener.document.getElementById("downUserIds");
		   var listorder=opener.document.getElementById("listOrder");
		   if(folderName.value == "" || folderName.value == null || folderName.value.length>25){
		   		folderName.focus();
		   		return false;
		   }
		   if(listorder.value != "" && !re.test(listorder.value)){
		   		listorder.focus();
		   		return false;
		   }
		   
		   var depts=opener.form.deptNames.value;  //父级授权的部门名称

		   var fuDept=opener.form.fuDept.value;   //父级授权的部门Id
		   var UserIds=opener.form.UserIds.value;   //父级授权的个人Id
		   
		   if((fuDept =='' && UserIds =='') &&(userIds.value == "" || userIds.value == null) && (deptIds.value == "" || deptIds.value == null)){
				if(confirm("您现在是否对组进行授权?")){
					return false;
				}
		    }
			if(fuDept=='' && UserIds!='' && deptIds.value!='' ){
				asyncbox.alert('该组没有可选择的部门!','$!text.get("clueTo")');
				return false;
			}
			if((fuDept!='' || UserIds!='') &&(deptIds.value =='' && userIds.value =='')){
				asyncbox.alert('授权不能为空!','$!text.get("clueTo")');
				return false;
			}
		    
		    if(opener.checkAlert()==true){
			}
			return false;　
　　　　  　}
　　   　}
　	});
}

function dealAsyn(){
	jQuery.close('dealdiv');
	window.location.reload();
}

function updategroup(id,flag){
	asyncbox.open({
	 id : 'dealdiv',
　　　url : '/OAKnowFolderCenter.do?operation=4&dealType=updateGroup&folderId='+ id + "&flag=" + flag,
	 title : '修改组',
　　　width : 680,
　　  height : 370,
     btnsbar : jQuery.btn.OKCANCEL,
	 callback : function(action,opener){
　　　　　//判断 action 值。
　　　　　if(action == 'ok'){
		   var re = /^\+?[0-9][0-9]*$/;　　//正整数
		   var folderName = opener.document.getElementById("folderName");
		   var userIds = opener.document.getElementById("popedomUserIds");
		   var deptIds = opener.document.getElementById("popedomDeptIds");
		   var downDept = opener.document.getElementById("downDeptIds");
		   var downUser = opener.document.getElementById("downUserIds");
		   var listorder=opener.document.getElementById("listOrder");
		   if(folderName.value == "" || folderName.value == null || folderName.value.length>25){
		   		folderName.focus();
		   		return false;
		   }
		   if(listorder.value != "" && !re.test(listorder.value)){
		   		listorder.focus();
		   		return false;
		   }
		  
		   var depts=opener.form.deptNames.value;
		   var fuDept=opener.form.fuDept.value;   //父级授权的部门Id
		   var UserIds=opener.form.UserIds.value;   //父级授权的个人Id
		   		   
		   		   
		   if((fuDept =='' && UserIds =='') && (userIds.value == "" || userIds.value == null) && (deptIds.value == "" || deptIds.value == null)){
				if(confirm("您现在是否对组进行授权?")){
					return false;
				}
		   }
		   if(fuDept=='' && UserIds!='' && deptIds.value!=''){
				asyncbox.alert('该组没有可选择的部门!','$!text.get("clueTo")');
				return false;
			} 
		    if((fuDept!='' || UserIds!='') &&(deptIds.value =='' && userIds.value =='')){
				asyncbox.alert('授权不能为空!','$!text.get("clueTo")');
				return false;
			}
		   
		   if(opener.checkAlert()==true){
		   }
		   return false;
		}
　　    　}
　	});
}

function deleteFile(classCode){
	asyncbox.confirm('$text.get("common.know.alert")','$!text.get("clueTo")',function(action){
	　　 if(action == 'ok'){
			document.location = "/OAKnowFolderCenter.do?operation=$globals.getOP("OP_QUERY")&folderId="+classCode+"&dealType=deleteFile";
			window.parent.frames["leftFrame"].location.reload();
		}
	});
}

function handle(name,type){
	var items = document.getElementsByName(name);
	var mydatasIds="";
	for(var i=0;i<items.length;i++){
		if(type == 1){
			if(items[i].checked){
				mydatasIds+=items[i].value;
			}
		}else if(type == 2){
			if(items[i].checked){
				var value = items[i].value;
				mydatasIds+=value+",";
			}
		}
	}
	if(mydatasIds!="" && mydatasIds.length!=0){
		if(type == 2){
			mydatasIds = mydatasIds.substr(0,mydatasIds.length-1);			
			del(mydatasIds,'$!OAKnowFolderSearchForm.folderId',$!flag);
		}
	}else{
		asyncbox.alert('$text.get('common.msg.mustSelectOne')','$!text.get("clueTo")');
	}
}

function del(id,folderId,flag){
	var str="/OAKnowFolderCenter.do?operation=$globals.getOP("OP_QUERY")&keyIds="+id+"&dealType=del&check=true&folderId="+folderId + "&flag=" + flag;
	AjaxRequest(str);
 	value = response;
 	var x=new Array();
 	x=value.split(";");
 	if(x[0] == "0"){
 		asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
		　　 if(action == 'ok'){
				document.location = "/OAKnowFolderCenter.do?operation=$globals.getOP("OP_QUERY")&keyIds="+id+"&dealType=del&folderId="+folderId + "&flag=" + flag;
			}
		});
 	}else{
 		asyncbox.alert("目录:"+x[1]+"包含子目录或数据,不可以删除!","$!text.get("clueTo")");
 	}
}

function refreshTree(){
	self.parent.frames["leftFrame"].refreshself();
}
</script>
<style type="text/css">
.colors{
	margin:0px;
	color:#E27C03;
	padding-bottom:5px;
	padding-left: 10px;
}
</style>
	</head>
	<body scroll="no" onload="refreshTree();">
		<iframe name="formFrame" style="display:none"></iframe>
		<form id="form" name="form" method="post" action="/OAKnowFolder.do"
			target="formFrame">
			<input type="hidden" name="dealType" value="" id="dealType" />
			<input type="hidden" name="operation" value="" />
			<input type="hidden" name="folderId"
				value="$!OAKnowFolderSearchForm.folderId" />
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="2" class="bigTitle" style="background-image: none">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="1" cellpadding="0" cellspacing="0">
							<tr>
								<td class="F_right" valign="middle">
									#if("$!OAKnowFolderSearchForm.folderId" == "")
										#set($flag = '1')
									#end
									<div class="right_title" style="border-bottom: 1px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="checkbox" value="checkbox" name="selAll"
											onClick="checkAll('keyId')" />
										$text.get("common.lb.selectAll")&nbsp;&nbsp;
										#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").add())
										<a href="javascript:btn('$!OAKnowFolderSearchForm.folderId','$!flag')"
											style="color: black;"> <img
												src="style1/images/oaimages/add.bmp"
												style="margin-top: 5px;" /><font style="margin-left: 2px;">
												$text.get("common.lb.addSameClass")</font>
										</a>#end&nbsp;
										#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").delete())
										<a href="javascript:handle('keyId',2)" style="color: black;">
											<img src="style1/images/oaimages/l-i-03.gif"
												style="margin-top: 5px;" /><font style="margin-left: 2px;">
												$text.get("common.lb.del")</font>
										</a> #end #set($folderIds = $!OAKnowFolderSearchForm.folderId)
										#set($len = $globals.getStringLength($!folderIds)) #if($len ==
										5) #set($folderIds = "") #else #set($folderIds =
										$globals.classCodeSubstring("$folderIds",5)) #end
										#if("$!OAKnowFolderSearchForm.folderId" != "")
										<a
											href="javascript:goFrame('/OAKnowFolder.do?operation=4&folderId=$!folderIds&flag=$!flag');"
											style="color: black;"><img
											src="style1/images/thread-prev.png" width="14" height="14" />&nbsp;$text.get("oa.filder.retrunUpFile")</a>
										#end
									</div>
									<div id="cc" class="dd" style="border-top: 1px solid #A3A8A7;"
										style="overflow:hidden;overflow-y:auto;width:100%;">
										<script type="text/javascript">
	var oDiv=document.getElementById("cc");
	var sHeight=document.documentElement.clientHeight-105;
	oDiv.style.height=sHeight+"px";
</script>
										<table width="98%" border="0" cellpadding="0" cellspacing="1">
											<tr>
												<td valign="top" class="list">
													<table cellpadding="0" cellspacing="0">
														<thead>
															<tr>
																<td width="5%">
																	$!text.get("common.lb.select")
																</td>
																<td width="30%">
																	$text.get("oa.userGroup.groupName")
																</td>
																<td width="40%">
																	$text.get("oa.common.description")
																</td>
																<td>
																	$text.get("common.lb.operation")
																</td>
															</tr>
														</thead>
														#foreach($folder in $!groupList)
														<tr onMouseMove="setBackground(this,true);"
															onMouseOut="setBackground(this,false);">
															<td>
																<input name="keyId" type="checkbox"
																	style="margin-left: 8px;" value="$!folder.classCode" />
															</td>
															<td>
																<div title="$!folder.folderName">
																	$globals.subTitle($globals.replaceHTML("$!folder.folderName"),60)
																</div>
															</td>
															<td>
																<div title="$!folder.description">
																	$globals.subTitle($globals.replaceHTML("$!folder.description"),50)&nbsp;
																</div>
															</td>
															<td>
																#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").update())
																<a href="javascript:updategroup('$!folder.id','$!flag')"><span
																	class="colors">$text.get("oa.folder.update")</span>
																</a> #end
																#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").delete())
																<a
																	href="javascript:del('$!folder.classCode','$!OAKnowFolderSearchForm.folderId','$!flag')"><span
																	class="colors">$text.get("common.lb.del")</span>
																</a> #end
																#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").add())
																<a href="javascript:btn('$!folder.classCode','2')"><span
																	class="colors">$text.get("acctype.lb.addNext")</span>
																</a> #end #if("$!folder.isCatalog"=="1")
																<a
																	href="javascript:goFrame('/OAKnowFolder.do?operation=4&flag=2&folderId=$!folder.classCode')"><span
																	class="colors">$text.get("common.lb.viewNextClass")</span>
																</a> #else
																<span style="color: gray;padding-left: 10px;"><span>$text.get("common.lb.noChild")</span>
																</span> #end
															</td>
														</tr>
														#end
													</table>
												</td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
