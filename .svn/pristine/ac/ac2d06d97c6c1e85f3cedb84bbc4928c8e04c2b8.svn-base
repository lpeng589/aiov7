<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.common.kenowledgeCenter.dirctoryInfo")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/sharingStyle.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript"src="/js/function.js"></script>
<script type="text/javascript" src="/js/listPage.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script type="text/javascript">
function goFrame(url){
	window.parent.f_mainFrame.location=url;
}

function btn(floderId,flag){
	var date=new Date();
	asyncbox.open({
		id : 'dealdiv',
　　　   	url : '/OAOrdainGroupCenter.do?operation=4&dealType=addGroup&time='+date+'&folderId='+ floderId + "&flag=" + flag,
	 	title : '添加组',
　　 　 	width : 680,
　　  　	height : 370,
        btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
　　　　　   //判断 action 值。
		if(action == 'ok'){
    		 //var re=/^(-|\+)?\d+(\.\d+)?$/; //判断是否是整数
			var re = /^\+?[0-9][0-9]*$/;　　//正整数 
		    var folderName = opener.document.getElementById("groupName");
		    var userIds = opener.document.getElementById("popedomUserIds");
		    var deptIds = opener.document.getElementById("popedomDeptIds");
		    var listorder=opener.document.getElementById("listOrder");
		    if(folderName.value == "" || folderName.value == null || opener.strLength(folderName.value)== false){
		   		folderName.focus();
		   		return false;
		    }
		    if(listorder.value != "" &&　!re.test(listorder.value)) {
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

function updategroup(id,flag){
	asyncbox.open({
		id : 'dealdiv',
　　		url : '/OAOrdainGroupCenter.do?operation=4&dealType=updateGroup&folderId='+id + "&flag=" + flag,
	    title : '修改组',
　　      width : 580,
　　      height : 370,
    	btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
　　　　　   //判断 action 值。
　　　　   　 if(action == 'ok'){
	 			//var re=/^(-|\+)?\d+(\.\d+)?$/; //判断是否是整数
				var re = /^\+?[0-9][0-9]*$/;　　//正整数 
			    var folderName = opener.document.getElementById("groupName");
			    var userIds = opener.document.getElementById("popedomUserIds");
			    var deptIds = opener.document.getElementById("popedomDeptIds");
			    var listorder=opener.document.getElementById("listOrder");
			    if(folderName.value == "" || folderName.value == null ){
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
function dealAsyn(){
	jQuery.close('dealdiv');
	window.location.reload();
}
function del(id,flag){
	var str="/OAOrdainGroupCenter.do?operation=$globals.getOP("OP_QUERY")&keyIds="+id+"&dealType=del&check=true&flag=" + flag;
	AjaxRequest(str);
 	value = response;
 	var x=new Array();
 	x=value.split(";");
 	if(x[0] == "0"){
 		asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
		　　 if(action == 'ok'){
				document.location = "/OAOrdainGroupCenter.do?operation=$globals.getOP("OP_QUERY")&keyIds="+id+"&dealType=del&flag=" + flag;
		    }
		});		
 	}else{
 		asyncbox.alert("目录:"+x[1]+"包含子目录或数据,不可以删除!","$!text.get("clueTo")");
 	}
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
			del(mydatasIds,'$!flag');			
		}
	}else{
		asyncbox.alert("$text.get('common.msg.mustSelectOne')","$!text.get("clueTo")");
	}
}

function refreshTree(){
	self.parent.frames["leftFrame"].refreshself();
}
</script>
	</head>
	<body scroll="no" onload="refreshTree();">
		<iframe name="formFrame" style="display:none"></iframe>
		<form id="form" name="form" method="post" action="/OAOrdainGroup.do"
			target="formFrame">
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
							#if("$!groupLen" == "")
								#set($flag = '1')	
							#end
							<div class="right_title" style="border-bottom: 1px;">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="checkbox" value="checkbox" name="selAll"
									onClick="checkAll('keyId')" />
								$text.get("common.lb.selectAll") &nbsp; 
								#if($addGroup)
									<a href="javascript:btn('$!groupLen','$!flag')">
									 <img src="style1/images/oaimages/add.bmp"  style="margin-top: 5px;"/>
									 <font style="margin-left: 2px;">$text.get("common.lb.addSameClass")</font></a> 
										#end &nbsp;
								#if($deleteGroup)
									<a href="javascript:handle('keyId',2)">
									<img  src="style1/images/oaimages/l-i-03.gif" style="margin-top: 5px;"/>
									 <font style="margin-left: 2px;">$text.get("common.lb.del")</font></a> 
								#end 
								#if("$!groupLen" != "")
								<a
									href="javascript:goFrame('/OAOrdainGroup.do?operation=4&dealType=ordainGroup&type=last&groupId=$groupLen&flag=$!flag');"><img
										src="style1/images/thread-prev.png" width="14" height="14" />&nbsp;$text.get("oa.filder.retrunUpFile")</a>
								#end
							</div>

							<div  id="cc" class="dd" style="border-top: 1px solid #A3A8A7;" style="overflow:hidden;overflow-y:auto;width:100%;">
<script type="text/javascript">
	var oDiv=document.getElementById("cc");
	var sHeight=document.documentElement.clientHeight-105;
	oDiv.style.height=sHeight+"px";
</script>
								<table width="100%" border="0" cellpadding="0" cellspacing="1">
									<input type="hidden" value="$groupLen" />
									<tr>
										<td valign="top" class="list">
											<table cellpadding="0" cellspacing="0">
												<thead >
													<tr  >
														<td width="5%" >
															$!text.get("common.lb.select")
														</td>
														<td width="30%" >
															$text.get("oa.userGroup.groupName")
														</td>
														<td width="40%">
															$text.get("oa.common.description")
														</td>
														<td >
															$text.get("common.lb.operation")
														</td>
													</tr>
												</thead>				
													#foreach($folder in $!groupList)
													<tr onMouseMove="setBackground(this,true);"
														onMouseOut="setBackground(this,false);">
														<td align="left">
															<input name="keyId" type="checkbox"
																style="margin-left: 8px;" value="$!folder.id" />
														</td>
														<td >
															<span title="$!folder.groupName"  >$globals.subTitle($globals.replaceHTML("$!folder.groupName"),35)</span>
														</td>
														<td > 
															<span title="$!folder.description" >$globals.subTitle($globals.replaceHTML("$!folder.description"),50)</span>
														</td>
														<td>
															#if($updateGroup)
															<a href="javascript:updategroup('$!folder.id','$!flag')"> <span
																class="colors">$text.get("oa.folder.update")</span> </a>
															#end #if($deleteGroup)
															<a href="javascript:del('$!folder.id','$!flag')"> <span
																class="colors">$text.get("common.lb.del")</span> </a>
															#end
															
															#if($addGroup)
															<a href="javascript:btn('$!folder.classCode','2')"> <span
																class="colors">$text.get("acctype.lb.addNext")</span> </a>
															#end 
															#if("$!folder.isCatalog"=="1")
															<a
																href="javascript:goFrame('/OAOrdainGroup.do?operation=4&flag=2&dealType=ordainGroup&groupId=$!folder.classCode')">
																<span class="colors">$text.get("common.lb.viewNextClass")</span>
															</a> #else
															<span style="color: gray;padding-left: 10px;"><span>$text.get("common.lb.noChild")</span>
															</span> #end
														</td>
													</tr>
													#end
												
											
											</table>
											<script type="text/javascript">
										var o=document.getElementById("nn");
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
									</script>
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
