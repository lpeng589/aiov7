<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html >
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>企业相册设置</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
		<link type="text/css" rel="stylesheet"
			href="/style/css/sharingStyle.css" />
		
		<link rel="stylesheet" type="text/css"
			href="/style/themes/default/easyui.css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" 
			rel="stylesheet" />
		<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
		<script language="javascript" src="/js/function.js"></script>
		<script language="javascript" src="/js/public_utils.js"></script>
		
		<script>
			$(function(){
				$(".tempNameCla").each(function(i){
					if (getStringLength($(this).text()) > 35){
						$(this).text(substr($(this).text(),35)+"...");
					}
				 });
			});
			
			var o = document.getElementById("nn");
			if(o){
				var gs=o.childNodes;
				var flag=false;
				for(i=0;i<gs.length;i++){
				  if(i%2==0){
					  flag=!flag;
				  }
				  gs[i].className=flag?"c1":"c2";
				}
			}
			
			//全选





			function checkAlls(obj,item) {
				//选择所有





				var phos = document.getElementsByName(item);
				var alt = obj.alt;
				for (var i = 0 ; i < phos.length; i++ ) {
					var c  = phos[i];
					if (c.type == 'checkbox') {
					   if( c.disabled == false ) {
					   	   if (alt == 'false') {
					  	 	   c.checked = false;
					  	 	   document.getElementById("checkAll").alt = true;
					   	   } else {
					  		   c.checked = true;
					   	   	   document.getElementById("checkAll").alt = false;
					   	   
					   	   }
					    }	
					}
				}
			}
		
			//批量删除
			function multinomialDel(itemName,formObj){
				if(!isChecked(itemName)){
					//$.messager.alert('$text.get("clueTo")','$text.get("common.msg.mustSelectOne")!','info');
					asyncbox.alert('$text.get("common.msg.mustSelectOne")!','$text.get("clueTo")');
					return false;
				}
				asyncbox.confirm('确定删除目录吗?','$text.get("clueTo")',function(action){
				　　　if(action == 'ok'){
						var url = "/DirectorySettingAlbumQueryAction.do?operation=$globals.getOP('OP_DELETE')&type=dels&isRoot=$isRoot";
						#if("$isRoot" == 1)
							url = "/DirectorySettingNetDiskQueryAction.do?operation=$globals.getOP('OP_DELETE')&type=dels&isRoot=$isRoot";
						#end
						formObj = document.getElementsByName(formObj)[0];
					    formObj.action = url;
					 	formObj.submit();
				　　　}
				　　　if(action == 'cancel'){
				　　　}
				　　　if(action == 'close'){
				　　　}
　				});
			}
			//删除
			function del(id,formObj){
				asyncbox.confirm('确定删除该目录吗?','$text.get("clueTo")',function(action){
				　　　if(action == 'ok'){
						var url = "/DirectorySettingAlbumQueryAction.do?operation=$globals.getOP('OP_DELETE')&isRoot=$isRoot&id="+id;
						#if("$isRoot" == 1)
							url = "/DirectorySettingNetDiskQueryAction.do?operation=$globals.getOP('OP_DELETE')&isRoot=$isRoot&id="+id;
						#end
						formObj = document.getElementsByName(formObj)[0];
					    formObj.action = url;
					 	formObj.submit();
					　}
　				});
			}
			//弹出对应的范围详细





			function showDetailShare(obj){
					
           		    var html = $(obj).prev().html();
           		    asyncbox.open({
				　　　html : html,
					 width : 350,
				　　　height : 200,
					 title : '发表范围',
				　　　btnsbar : jQuery.btn.OK,
				　　　callback : function(action,opener){
				　　　　　if(action == 'ok'){
						 	$("#folder").val("");
				　　　　　}
				　　　　　if(action == 'cancel'){
				　　　　　　$("#folder").val("");
				　　　　　}
				　　　　　if(action == 'close'){
				　　　　　　$("#folder").val("");
				　　　　　}
				　　　}
				　});
		}
	</script>
	
	</head>
	<body style="padding:0 0 0 0;overflow:hidden;width:100%;">
		<form action="" name="form" method="post">
			<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')"/>
      
<script type="text/javascript">
	$(function(){
		var zz = screen.height
		$("#listRange_id").css("height",zz-280);
	});
</script>

			<div id = "listRange_id"  style="overflow:auto;padding:0 0 0 1%;margin:0;width:99%;">
				<table cellpadding="0" cellspacing="0" class="frame2" style="margin:0;padding:0;">
					<tr>
						<td valign="top" class="list">
								<div class="PhotoAlbum_top">
									<span class="root_a ti_01" id="dialog_link" style=" margin-right:2px;">目录管理</span>
									#if($add)
									<a href="#if("$isRoot" == 0) /DirectorySettingAlbumQueryAction.do?operation=$globals.getOP('OP_ADD_PREPARE')&isRoot=$isRoot #else /DirectorySettingNetDiskQueryAction.do?operation=$globals.getOP('OP_ADD_PREPARE')&isRoot=$isRoot  #end" class="root_a ti_02" style="margin-right:2px;width: 36px;">添加</a>
									#end
									#if($del)
									<a onclick="multinomialDel('dir','form')" class="root_a ti_07" style="margin-right:20px;cursor:hand;color: #000000;width: 30px;">删除</a>
									
									#end
								</div>
								
								<table cellpadding="0" cellspacing="0">
										<thead>
											<tr>
												<td style="background:#F9F9F9;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input name="" type="checkbox" value="" id="checkAll" onclick="checkAlls(this,'dir')"/>全选</td>
												<td>目录名称</td>
												<td>目录路径</td>
												<td>发表范围</td>
												<td>操作</td>
											</tr>
										</thead>
										<tbody id="nn">
											#if($directorySetingList.size()<=0)
												<tr>
													<td colspan="5">
														<div class="oabbs_function_revert" style="margin-top: 150px;">
																
														</div>
														<div style="height:250px;">
															<a href="#if("$isRoot" == 0) /DirectorySettingAlbumQueryAction.do?operation=$globals.getOP('OP_ADD_PREPARE')&isRoot=$isRoot #else /DirectorySettingNetDiskQueryAction.do?operation=$globals.getOP('OP_ADD_PREPARE')&isRoot=$isRoot  #end" style="margin-bottom: 50px;"><span>暂无数据，您可以选择添加！</span></a>	
														</div>	
													</td>
												</tr>
											#else
										#foreach ($d in $directorySetingList)
												<tr onMouseMove="setBackground(this,true);"
														onMouseOut="setBackground(this,false);">
													<td><input name="dir" type="checkbox" value="$d.id" /></td>
													<td>$!d.name</td>
													<td>$!d.path</td>
													<td style="width:30%;" >
														<div align="left" style="display: none;" >
															#if(!$!d.shareDepts&&!$!d.shareEmpGroups&&!$!d.shareUserNames)
																<span style="color: green;">&nbsp;&nbsp;公开</span>
															#else
																#if($!d.shareDepts.size()>0)
																<span style="color: green;">&nbsp;&nbsp;部门：</span>
																	 #set($dNum = 0)
																	 #set($dLen = $!d.shareDepts.size())
																	 #foreach ($dept in $!d.shareDepts)
																		 $!dept.DeptFullName 
																		 #if($dNum!=$dLen-1)
																		 	#if($dNum<$dLen)、#end 
																		 	#end
																	 	 #set($dNum=$dNum+1)
																	 #end
																#end
																#if($!d.shareEmpGroups.size()>0)
																<span style="color: green;"><br/>&nbsp;&nbsp;职员分组：</span> #set($eNum = 0)
																	 #set($eLen = $!d.shareEmpGroups.size())#foreach($grp in $!d.shareEmpGroups)
																	 $!globals.get($grp,1)
																	 #if($eNum!=$eLen-1)
																	 	#if($eNum<$eLen)、#end
																	 #end
																	 #set($eNum=$eNum+1)
																#end
																#end
																#if($!d.shareUserNames.size()>0)
																<br/><span style="color: green;">&nbsp;&nbsp;人员：</span>
																#set($uNum = 0)
																 #set($uLen = $!d.shareUserNames.size())
																#foreach($user in $!d.shareUserNames)
																	$!user.EmpFullName 
																	#if($uNum!=$uLen-1)
																		#if($uNum<$uLen)、#end
																	#end
																	#set($uNum=$uNum+1)
																#end
																#end
															#end
														</div>
														
														<div class="tempNameCla A1"  style="text-align: center;cursor: pointer;color: green;" onclick="showDetailShare(this)">#if(!$!d.shareDepts&&!$!d.shareEmpGroups&&!$!d.shareUserNames)<span style="color: green;">公开</span>#else#if($!d.shareDepts.size()>0)<span style="color: green;">部门：</span>#set($dNum = 0) #set($dLen = $!d.shareDepts.size())#foreach ($dept in $!d.shareDepts)$!dept.DeptFullName#if($dNum<$dLen)、#end#set($dNum=$dNum+1)#end#end#if($!d.shareEmpGroups.size()>0)<span style="color: green;"> 职员分组：</span> #set($eNum = 0)#set($eLen = $!d.shareEmpGroups.size())#foreach($grp in $!d.shareEmpGroups)$!globals.get($grp,1) #if($eNum<$eLen)、#end #set($eNum=$eNum+1)#end#end#if($!d.shareUserNames.size()>0)<span style="color: green;">人员：</span>#set($uNum = 0) #set($uLen = $!d.shareUserNames.size())#foreach($user in $!d.shareUserNames)$!user.EmpFullName#if($uNum<$uLen)、#end#set($uNum=$uNum+1)#end#end#end</div> 
													</td>
													<td>
											
													#if($upd)
														#if("$isRoot" == 1)
															<a style="cursor:hand;color: #000000;" href="/DirectorySettingNetDiskQueryAction.do?operation=$globals.getOP('OP_UPDATE_PREPARE')&id=$d.id&isRoot=$isRoot"><img src="style/plan/M_1.gif" /> 修改</a>
														#else
															<a style="cursor:hand;color: #000000;" href="/DirectorySettingAlbumQueryAction.do?operation=$globals.getOP('OP_UPDATE_PREPARE')&id=$d.id&isRoot=$isRoot"><img src="style/plan/M_1.gif" /> 修改</a>
														#end
													#end
													#if($del)	<a style="cursor:hand;color: #000000;" onclick="del('$d.id','form')"><img src="style/plan/M_2.gif"/> 删除</a>#end</td>
												</tr>
												#end
											#end
										</tbody>
                    <!--
										<tr class="tfoot">
											<td colspan="2">&nbsp; </td>
											<td colspan="3">
												
											</td>
										</tr>
                    -->
							</table>
							
						</td>
				</tr>
			</table>
      	
			</div>
      
      <div>
        <div class="listRange_pagebar">
          $!pageBar
        </div>
      </div>
		</form>
	
	</body>
</html>

