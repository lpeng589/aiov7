<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
		<link type="text/css" rel="stylesheet" href="/style/css/sharingStyle.css" />
		<link type="text/css" rel="stylesheet"href="/js/skins/ZCMS/asyncbox.css"  />
		<script language="javascript" src="/js/function.js"></script>
		<script language="javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>
		<script language="javascript" src="/js/kindeditor-min.js" charset="utf-8"></script>
		<script language="javascript" src="/js/lang/${globals.getLocale()}.js" charset="utf-8"></script>
		<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
		<script language="javascript" src="/js/public_utils.js"></script>
		
		<script language="javascript" src="/js/AsyncBox.v1.4.5.js"></script>
		<script type="text/javascript">
	
			function isExist(checkvalue){
				var existOption = document.getElementById("formatDeptName").options;
				var length = existOption.length;
				var talg = false ;
				for(var i=0;i<length;i++){
					if(existOption[i].value==checkvalue){
						talg = true;
					}
				}
				return talg ;
			}
			
			
			function checkForm(){	
				var name = $("#name").val();
				var path = $("#path").val();
				var oldName = "$!directorySetting.name";
				var oldpath = '$!pathstr';
			
				if (oldName == name && path == oldpath) {
				  	return true;
				}		
			
				var formatDeptName=form.formatDeptName;
				var formatDeptName = $("#popedomUserIds").val();
				var EmpGroup = $("#EmpGroupId").val();
				var formatFileName = $("#popedomDeptIds").val();
		
				var bool1 = formatDeptName == ""||formatDeptName == null;
				var bool2 = EmpGroup == ""||EmpGroup == null;
				var bool3 = formatFileName ==""||formatFileName ==null;
				/*
				if (bool1 == true &&  bool2== true&& bool3== true){
					asyncbox.alert('必须选择一项发布范围!','$text.get("clueTo")');
					return false;
					
				}*/
				
				if(name == ""){
					asyncbox.alert('请填写名称!','$text.get("clueTo")');
					return false;
				}
				var bool4 = isHaveSpecilizeChar2(name);
				
				if(bool4 == false){
					asyncbox.alert('目录名称不能含有\\、/、"等特殊字符','$text.get("clueTo")');
					return false;
				}
				if(getStringLength(name)>50){
						asyncbox.alert("$text.get('name')"+"$text.get('oa.common.not.more.than')"+50+"$text.get('oa.common.word.countOfCharacter')",'$text.get("clueTo")');
						return false ;
				}
				isExistName(1);
				if (isExistNameUp == false) {
					asyncbox.alert('该目录名称已经存在!','$text.get("clueTo")');
					return false;
				}
				
				if (path == "") {
					asyncbox.alert('请填写路径!','$text.get("clueTo")');
					return false;
				}
				
				var index = path.indexOf("\\");
				var index2 = path.indexOf("/");
				
				if (index < 0 && index2 < 0) {
					asyncbox.alert('请填写正确路径，可参照本页说明!','$text.get("clueTo")');
					return false;
				}
				
				path = encodeURIComponent(path);
				 var ifTrueOfPath = false;
				 
				var url = "/DirectorySettingAlbumQueryAction.do?operation=$globals.getOP('OP_QUERY')&isRoot=$!isRoot&type=whetherExistPath&path="+path;
				#if("$isRoot" == 1)
					url = "/DirectorySettingNetDiskQueryAction.do?operation=$globals.getOP('OP_QUERY')&isRoot=$!isRoot&type=whetherExistPath&path="+path;
				#end
				 jQuery.ajax({
					  url: url,
					  async: false,
					  type: "POST",
					  success: function(msg){
					  	 path = $("#path").val();
						 if(msg == 'false' && path != oldpath){
						 	asyncbox.alert('请填写正确路径，可参照本页说明!','$text.get("clueTo")');
						 } else {
						 	ifTrueOfPath = true;
						 }
					  }
				 });
				 return ifTrueOfPath;
			}
			
			function isNull(variable,message){
				if(variable=="" || variable==null){
					asyncbox.alert(message+" "+"$text.get('common.validate.error.null')",'$text.get("clueTo")');
					return false;
				} else {
					return true;
				}	
			}
			
			function chikeRadio(obj){
			
				if(obj.checked==true){
					if(obj.value==1){
						document.getElementById("_title").style.display='block';  
						document.getElementById("_context").style.display='block';
						document.getElementById("_trno").style.display=''		
					} else {
						document.getElementById("_title").style.display='none';
						document.getElementById("_context").style.display='none';
						document.getElementById("_trno").style.display='none'		
					}
				}
			}
			
	
			function resetEditor(){	
				var existfOption = form.formatFileName.options;
				var existdOption = form.formatDeptName.options;
				var existeOption = form.EmpGroup.options;
				for(var i=0;i<existfOption.length;i++){
					existfOption.remove(i);
					i=i-1;
				}
				for(var i=0;i<existdOption.length;i++){
					existdOption.remove(i);
					i=i-1;
				}
				for(var i=0;i<existeOption.length;i++){
					existeOption.remove(i);
					i=i-1;
				}
				editor.html("") ;
			}

		
			function isExistPath(type){
				var path = $("#path").val();
				var index = path.indexOf("\\");
				var index2 = path.indexOf("/");
				if (index < 0 && index2 < 0) {
					if (type==0) {
						$("#showErrInfo").show();
					}
					return false;
				}
				path = encodeURIComponent(path);
				var url = "/DirectorySettingAlbumQueryAction.do?operation=$globals.getOP('OP_QUERY')&isRoot=$!isRoot&type=whetherExistPath&path="+path;
				#if("$isRoot" == 1)
					url = "/DirectorySettingNetDiskQueryAction.do?operation=$globals.getOP('OP_QUERY')&isRoot=$!isRoot&type=whetherExistPath&path="+path;
				#end
				jQuery.getJSON(url, function(data){
					 if(data ==false){
						$("#showErrInfo").show();
					 	return false;
					 } else {
						 $("#showErrInfo").hide();
						 return true;
					 }
				 })
			}
			
			var isExistNameUp = false;
			function isExistName(type){//名称是否已经存在了





				var name = $("#name").val();
				var oldName = "$directorySetting.name";
				if (oldName == name) {//原名和现在的一样，那么就不需要去验证
				    isExistNameUp = true;
				    return true;
				}
				name = encodeURIComponent(name);
				var url = "/DirectorySettingAlbumQueryAction.do?operation=$globals.getOP('OP_QUERY')&type=whetherExistName&isRoot=$!isRoot&qtype=name&key="+name;
				#if("$isRoot" == 1)
					url = "/DirectorySettingNetDiskQueryAction.do?operation=$globals.getOP('OP_QUERY')&type=whetherExistName&isRoot=$!isRoot&qtype=name&key="+name;
				#end
				
				jQuery.getJSON(url, function(data){
						
					 if(data ==false){
					 	if(type == 0){
					 		asyncbox.alert('该目录名称已经存在!','$text.get("clueTo")');
					 	}
						isExistNameUp = false;
					 	return false;
					 }else {
					 	isExistNameUp = true;
					 	return true;
					 }
				 })
			
			}
			
			function forBack() {
				var url = "/DirectorySettingAlbumQueryAction.do?operation=$globals.getOP('OP_QUERY')&isRoot=$!isRoot";
				#if("$isRoot" == 1)
					url = "/DirectorySettingNetDiskQueryAction.do?operation=$globals.getOP('OP_QUERY')&isRoot=$!isRoot";
				#end
				form.action = url;
				form.submit();
			}
			
			function fillData(datas){
				newOpenSelect(datas,fieldNames,fieldNIds,1);
				jQuery.close('Popdiv')
			}
			
	</script>
	<style type="text/css">
		.search {
			cursor:pointer;
		}
	</style>
	</head>
	<body>	
	<form #if("$isRoot" == 1) action="/DirectorySettingNetDiskQueryAction.do?operation=$globals.getOP('OP_UPDATE')&isRoot=$!isRoot" #else action="/DirectorySettingAlbumQueryAction.do?operation=$globals.getOP('OP_UPDATE')&isRoot=$!isRoot" #end  onSubmit="return checkForm();" name="form" method="post">
			<input type="hidden" name="shareuserId" id="shareuserId" value="$!directorySetting.shareuserId" />
			<input type="hidden" name="shareDeptOfClassCode" id="shareDeptOfClassCode" value="$!directorySetting.shareDeptOfClassCode" />
			<input type="hidden" name="shareEmpGroup" id="shareEmpGroup" value="$!directorySetting.shareEmpGroup"/>
			
			<input type="hidden" name="downLoadUserId" id="downLoadUserId" value="$!directorySetting.downLoadUserId"/>
			<input type="hidden" name="downLoadDeptOfClassCode" id="downLoadDeptOfClassCode" value="$!directorySetting.downLoadDeptOfClassCode"/>
			<input type="hidden" name="downLoadEmpGroup" id="downLoadEmpGroup" value="$!directorySetting.downLoadEmpGroup"/>
			
			<input type="hidden" name="id" value="$directorySetting.id"/>
			<input type="hidden" name="picFiles" value="$!globals.get($arr_newsInfo,14)"/>
			<input type="hidden" name="delPicFiles" value="" />
			
			<div  style="overflow: hidden;width: 99%;overflow-y: auto;padding:0 0 0 1%;">
			<table cellpadding="0" cellspacing="0" class="frame2">
				<tr>
					<td valign="top" class="list">
								<div class="PhotoAlbum_top" style="width:50%;">
									<a style="cursor:hand" class="root_a ti_01">目录设置</a>
								</div>
								<div class="sharingbutton" style="float:right; margin-right:5%; margin-top:-10px;">
									<input type="submit" value="确 定" />
									<input type="button" value="返 回"  onclick="forBack()"/>
								</div>
								<ul class="ABK_Add PhotoAlbumAdd">
									<ol style="height:15px;"></ol>
									<li style="height: 400px;">
									<table style="width:100%; border:0px solid red;">
										<tr style="height:130px;width: 100%;">
											<td bgcolor="#F5F5F5" width="10%">目录授权：</td>
											<td width="90%">
												<div style="width:28%; height:100px; float:left;margin-left:30px;">
													<div style="width:200px;">
														<span style="float:left;"><img src="/style1/images/St.gif"  onClick="deptPop('deptGroup','formatDeptName','shareDeptOfClassCode','1');" class="search" /></span>
														<span style="float:left; margin-top:-20px;font-size:12px;height:25px;width:200px;"><a  href="javascript:void(0);" onClick="deptPop('deptGroup','formatDeptName','shareDeptOfClassCode','1');" style="color:blue;">选择部门</a></span>
													</div>
													 
													<select name="formatDeptName" id="formatDeptName" style="width:100px; float:left; margin-top:-15px; "
														multiple="multiple">
													 #foreach ($dept in $!directorySetting.shareDepts)
														<option value="$dept.classCode">$!dept.DeptFullName</option>
													 #end
													</select>
													<div style="width:30px;margin-top:-15px;">
													<img onClick="deleteOpation('formatDeptName','shareDeptOfClassCode')"
														src="/$globals.getStylePath()/images/delete_button.gif"
														alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
													</div>
							
												</div>
												
												 <div style="width:28%; height:100px; float:left;margin-left:30px;">
													<div style="width:200px;">
														<span style="float:left;"><img src="/style1/images/St.gif" onClick="deptPop('userGroup','formatFileName','shareuserId','1');" class="search" /></span>
														<span style="float:left; margin-top:-20px;font-size:12px;height:25px;width:200px;"><a href="javascript:void(0)" style="color:blue;" onClick="deptPop('userGroup','formatFileName','shareuserId','1');">选择个人</a></span>
													</div>
													 
													<select name="formatFileName" id="formatFileName" style="width:100px; float:left; margin-top:-15px; "
														multiple="multiple">
													#foreach($user in $!directorySetting.shareUserNames)
														<option value="$user.id">$!user.EmpFullName</option>
													#end
													</select>
													<div style="width:30px;margin-top:-15px;">
													<img onClick="deleteOpation('formatFileName','shareuserId')"
														src="/$globals.getStylePath()/images/delete_button.gif"
														alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
													</div>
							
												</div>
												
											  	<div style="width:28%; height:100px; float:left;margin-left:30px;">
													<div style="width:200px;">
														<span style="float:left;"><img src="/style1/images/St.gif"  onClick="deptPop('empGroup','EmpGroup','shareEmpGroup','1');"  class="search" /></span>
														<span style="float:left; margin-top:-20px;font-size:12px;height:25px;width:200px;"><a href="javascript:void(0);" onClick="deptPop('empGroup','EmpGroup','shareEmpGroup','1');" style="color:blue;">选择分组</a></span>
													</div>
													 
													<select name="EmpGroup" id="EmpGroup" style="width:100px; float:left; margin-top:-15px; "
														multiple="multiple">
													 	#foreach($grp in $!directorySetting.shareEmpGroups)
															<option value="$!globals.get($grp,0)">$!globals.get($grp,1)</option>
														#end
													</select>
													<div style="width:30px;margin-top:-15px;">
													<img onclick="deleteOpation('EmpGroup','shareEmpGroup')"
														src="/$globals.getStylePath()/images/delete_button.gif"
														alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
													</div>
												</div>
												
											 	<div style="color: gray;width:230px;margin-top:-10px;">注:若不选，所有人可见此目录信息。</div>
											 	 
					 	
											</td>
											
										</tr>
										
										<tr style="height:130px;">
											<td bgcolor="F5F5F5"  style="width:10%;">下载授权：</td>
											<td>
												 
												
												<div style="width:28%; height:100px; float:left;margin-left:30px;margin-top:0px;">
													<div style="width:200px;">
														<span style="float:left;"><img src="/style1/images/St.gif" class="search" onClick="deptPop('deptGroup','formatDeptName1','downLoadDeptOfClassCode','1');"  /></span>
														<span style="float:left; margin-top:-20px;font-size:12px;height:25px;width:200px;"><a  href="javascript:void(0);" onClick="deptPop('deptGroup','formatDeptName1','downLoadDeptOfClassCode','1');"  style="color:blue;">选择部门</a></span>
													</div>
													 
													<select name="formatDeptName1" id="formatDeptName1" style="width:100px; float:left; margin-top:-15px; "
														multiple="multiple">
													  #foreach ($dept in $!directorySetting.downLoadDepts)
														<option value="$dept.classCode">$!dept.DeptFullName</option>
													 #end
													</select>
													<div style="width:30px;margin-top:-15px;">
													<img onClick="deleteOpation('formatDeptName1','downLoadDeptOfClassCode');"
														src="/$globals.getStylePath()/images/delete_button.gif"
														alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
													</div>
												</div>
												
													<div style="width:28%; height:100px; float:left;margin-left:30px;margin-top:0px;">
													<div style="width:200px;">
														<span style="float:left;"><img src="/style1/images/St.gif" class="search" onClick="deptPop('userGroup','formatFileName1','downLoadUserId','1');" /></span>
														<span style="float:left; margin-top:-20px;font-size:12px;height:25px;width:200px;"><a href="javascript:void(0);" onClick="deptPop('userGroup','formatFileName1','downLoadUserId','1');" style="color:blue;">选择个人</a></span>
													</div>
													 
													<select name="formatFileName1" id="formatFileName1" style="width:100px; float:left; margin-top:-15px; "
														multiple="multiple">
													 #foreach($user in $!directorySetting.downLoadUserNames)
														<option value="$user.id">$!user.EmpFullName</option>
													#end
													</select>
													<div style="width:30px;margin-top:-15px;">
													<img onClick="deleteOpation('formatFileName1','downLoadUserId');"
														src="/$globals.getStylePath()/images/delete_button.gif"
														alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
													</div>
							
												</div>
												
												<div style="width:28%; height:100px; float:left;margin-left:30px;margin-top:0px;">
													<div style="width:200px;">
														<span style="float:left;"><img src="/style1/images/St.gif" class="search" onClick="deptPop('empGroup','EmpGroup1','downLoadEmpGroup','1');" /></span>
														<span style="float:left; margin-top:-20px;font-size:12px;height:25px;width:200px;"><a href="javascript:void(0);" onClick="deptPop('empGroup','EmpGroup1','downLoadEmpGroup','1');"  style="color:blue;">选择分组</a></span>
													</div>
													 
													<select name="EmpGroup1" id="EmpGroup1" style="width:100px; float:left; margin-top:-15px; "
														multiple="multiple">
													  	#foreach($grp in $!directorySetting.downLoadGroups)
															<option value="$!globals.get($grp,0)">$!globals.get($grp,1)</option>
														#end
													</select>
													<div style="width:30px;margin-top:-15px;">
													<img onClick="deleteOpation('EmpGroup1','downLoadEmpGroup');"
														src="/$globals.getStylePath()/images/delete_button.gif"
														alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
													</div>
							
												</div>
												<div style="color: gray;width:230px;margin-top:-10px;">注:若不选,目录授权的人可下载该信息。</div>
											</td>
										</tr>
										
										<tr style="height:50px;">
											<td bgcolor="F5F5F5"  style="width:10%;">目录名称：</td>
											<td>
												<div style="align:center;margin-left:30px;width:80%">
														<input type="text" style="width:297px;" value="$directorySetting.name" id="name" name="name" onblur="isExistName(0)"/>	
												</div>
											</td>
										</tr>
										
										<tr style="height:50px;" >
											<td bgcolor="F5F5F5" style="width:10%;">目录路径：</td>
											<td  >
											
												<div style="width:100%;float:left; margin-top:10px; margin-left:30px;">
													<input type="text" style="width:297px;" value="$directorySetting.path" id="path" name="path" onblur="isExistPath(0)" />	
														<div id = "showErrInfo" style="color: red; width:240px; margin-left:10px;margin-top:0px;display: none;">
													 		请填写正确路径，可参照路径说明!
														</div>
													
												</div>
							
												<b style="float:left;margin-left:30px;">路径说明："F:\我的文件夹\2012年文件"与"F:/我的文件夹/2012年文件"为同一路径，不能创建相同路径的目录"</b>
											
											</td>
										</tr>
										
									</table>
											
									</li>
								</ul>
								
							</td>
						</tr>
				</table>
				</div>
		</form>
	</body>
</html>
