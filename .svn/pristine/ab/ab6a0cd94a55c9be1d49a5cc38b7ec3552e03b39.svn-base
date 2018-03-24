<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.common.knowledgeCenter")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css"/>
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/date.vjs"></script>
<script type="text/javascript" src="/js/listPage.vjs"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">

var x="$!OAKnowSearchForm.folderCode";
var y="$!OAKnowSearchForm.queryType";
if(y=="number"){
	self.parent.frames["leftFrame"].insertType(x);	
}
if("$!insertPlace"!=null && "$!insertPlace"!=""){
	self.parent.frames["leftFrame"].refreshself();
}
function goFarme(url){
	window.location.href=url;
}

function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}

function update(id){
	document.location="/OAKnowledgeCenter.do?operation=7&fileId="+id;
}
function closeSearch(){
	$("#dbData").remove();
	$('#w').hide();
}

function groupOperate(m){
	if("$!OAKnowSearchForm.queryType"!="number" ){
		asyncbox.alert('请先选择一个目录!','$!text.get("clueTo")');
		return false;
	}
	switch (m){
	case  1:
		btn("$!OAKnowSearchForm.folderCode",'2');
		
		break;
	case  2:
		del("$!OAKnowSearchForm.folderCode",'$!flag');
		
		break;
	case  3:	
		var folderCode = $('#folderCode').val();
   		str = folderCode.charAt(5);  
   		if(str != "") {
			updategroup("$!OAKnowSearchForm.folderCode",'2');
   		}else{
   			updategroup("$!OAKnowSearchForm.folderCode",'1');
   		} 
   		break;
	}
}

function btn(floderId,flag){
	var date=new Date();
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
    		 //var re=/^(-|\+)?\d+(\.\d+)?$/; //判断是否是整数



			var re = /^\+?[0-9][0-9]*$/;　　//正整数 
		    var folderName = opener.document.getElementById("folderName");
		   var userIds = opener.document.getElementById("popedomUserIds");
		   var deptIds = opener.document.getElementById("popedomDeptIds");
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
		   
		 	if((fuDept =='' && UserIds =='') && (userIds.value == "" || userIds.value == null) && (deptIds.value == "" || deptIds.value == null)){
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
　　		url : '/OAKnowFolderCenter.do?operation=4&dealType=updateGroup&insertPlace=list&folderId='+id + "&flag=" +flag,
	    title : '修改组',
　　      width : 580,
　　 	height : 370,
    	btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
　　　　　   //判断 action 值。



　　　　   　if(action == 'ok'){
 			   //var re=/^(-|\+)?\d+(\.\d+)?$/; //判断是否是整数



			   var re = /^\+?[0-9][0-9]*$/;　　//正整数 
			   var folderName = opener.document.getElementById("folderName");
			   var userIds = opener.document.getElementById("popedomUserIds");
			   var deptIds = opener.document.getElementById("popedomDeptIds");
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

function dealAsyn(){
	jQuery.close('dealdiv');
	self.parent.frames["leftFrame"].refreshself();
	window.location.reload();
}
function del(id,flag){
	var str="/OAKnowFolderCenter.do?operation=$globals.getOP("OP_QUERY")&keyIds="+id+"&dealType=del&check=true&flag=" + flag;
	AjaxRequest(str);
 	value = response;
 	var x=new Array();
 	x=value.split(";");
 
 	if(x[0] == "0"){
 		asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
		　　 if(action == 'ok'){
				document.location = "/OAKnowFolderCenter.do?operation=$globals.getOP("OP_QUERY")&insertPlace=knowList&keyIds="+id+"&dealType=del&folderId="+id;
		    }
		});		
 	}else{
 		asyncbox.alert("目录:"+x[1]+"包含子目录或数据,不可以删除!","$!text.get("clueTo")");
 	}
}

function show1(){
	var fCode = window.parent.frames["leftFrame"].document.getElementById("fCode").value;
	if(fCode.length==0){
		asyncbox.alert('当前没有可选组','$!text.get("clueTo")');
		return false;
	}
	var displayName="$text.get('oa.common.kownleageCenter.directory')"; 
	var url = "/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=tblEmpGroup&selectName=KnowledgeCenterNote&classCode="+fCode+"&popupWin=Popdiv&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName;
	asyncbox.open({id:'Popdiv',title:displayName,url:url,width:750,height:470})
}

//弹出框返回值




function exePopdiv(returnValue){
	if(typeof(returnValue)=="undefined") return ;
	var note = returnValue.split("#;#") ;
	document.getElementById('groupIdSearch').value=note[0]
	document.getElementById('groupNameSearch').value=note[1]
	jQuery.close('Popdiv');
}

function handle(name,type){
	var items = document.getElementsByName(name);
	var mydatasIds="";
	for(var i=0;i<items.length;i++){
		if(type == 1){
			if(items[i].checked){
				mydatasIds+=items[i].value;
				update(items[i].value);
				return true;
			}
		}else if(type == 2){
			if(items[i].checked){
				var value = items[i].value;
				mydatasIds+=value+",";
			}
		}
	}
	if(mydatasIds!="" && mydatasIds.length!=0){
			mydatasIds = mydatasIds.substr(0,mydatasIds.length-1);
			asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
	　　 	if(action == 'ok'){
				form.operation.value=$globals.getOP("OP_DELETE");
				form.submit();
			}});
	}else{
		asyncbox.alert('$text.get('common.msg.mustSelectOne')','$!text.get("clueTo")');
	}
}

function deleteFile(id,preId){
	asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
	　　 if(action == 'ok'){
			document.location = "OAKnowledgeCenter.do?operation=$globals.getOP("OP_DELETE")&keyId="+id+"&preId="+preId;
		}
	});
}

function checkw(){
	var divs = document.getElementById("w");
	
	if(divs.style.display == "block"){
		form.queryType.value='keyword';
	}else if (!$("#w").is(":hidden")){
		form.queryType.value='keyword';	
	}
}
function dingWei(){
	var knowId="$!selectKnow";
	if(knowId!= ''){
		window.location.href="#"+knowId;
	}
}

function statuss(){
	if("$!status" == 1){
		document.getElementById("w").style.display='block';
	}
}
function fillData(datas){
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv');
}
</script>
<style type="text/css">
.search_popup ul li input{border:1px solid #42789C;width:120px;}
</style>

</head>

<body onLoad="dingWei();statuss();">
<iframe name="formFrame" style="display:none"></iframe>
<form action="OAKnowCenter.do" name="form" method="post" id="form" onsubmit="checkw()">
<input type="hidden" name="stat" value="$!stat" id="stat"/>	
<input type="hidden" name="folderCode" id="folderCode" value="$!OAKnowSearchForm.folderCode"/>
<input type="hidden" name=type value="oaKnowList"/>
<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")" />

	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;" >
		<tr>
			<td colspan="2" class="bigTitle" style="background-image: none">&nbsp;</td>
		</tr>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;" >
				<tr>
				<td class="F_right" valign="middle">
					
					#if(!($LoginBean.operationMap.get("/OAKnowledgeCenter.do").query() && !$LoginBean.operationMap.get("/OAKnowledgeCenter.do").update() && !$LoginBean.operationMap.get("/OAKnowledgeCenter.do").delete()&& !$LoginBean.operationMap.get("/OAKnowledgeCenter.do").add()))
					<div class="right_title">&nbsp;&nbsp;<input type="checkbox" value="checkbox" name="selAll" onClick="checkAll('keyId')"/>$text.get("common.lb.selectAll")&nbsp;
					#end
					#if($LoginBean.operationMap.get("/OAKnowledgeCenter.do").add())
					<a href="javascript:goFarme('/OAKnowledgeCenter.do?operation=6&folderCode=$!OAKnowSearchForm.folderCode')">
					<img  src="style1/images/oaimages/add.bmp" style="margin-top: 5px;"/><font style="margin-left: 2px;">添加</font></a>
					#end&nbsp;
					#if($LoginBean.operationMap.get("/OAKnowledgeCenter.do").delete())
					<a href="javascript:handle('keyId',2)"><img src="style1/images/oaimages/l-i-03.gif"  style="margin-top: 5px;"/><font style="margin-left: 2px;">$text.get("oa.common.del")</font></a>
					#end
				</div>
				<ul id="nn" style="overflow:hidden;overflow-y:auto;width:100%;margin:0;padding:0;">
<script type="text/javascript">
	var oDiv=document.getElementById("nn");
	var sHeight=document.documentElement.clientHeight-135;
	oDiv.style.height=sHeight+"px";
</script>
					#set($preId = "")
					#foreach($map in $knowMap)
					<li>
						<div class="newsTitle"><span><a name="$map.get("id")"></a>
						#if(!($LoginBean.operationMap.get("/OAKnowledgeCenter.do").query() && !$LoginBean.operationMap.get("/OAKnowledgeCenter.do").update() && !$LoginBean.operationMap.get("/OAKnowledgeCenter.do").delete()&& !$LoginBean.operationMap.get("/OAKnowledgeCenter.do").add()))
							<input type="checkbox" name="keyId" value="$!map.get("id")"/>
						#end
						</span>
						
						<a  href="javascript:goFarme('/OAKnowCenter.do?operation=5&fileId=$!map.get("id")&preId=$!preId')" title="$!map.get("fileTitle")">$globals.subTitle($!map.get("fileTitle"),150)</a></div>
						<div class="newsCon"><P><a href="javascript:goFarme('/OAKnowCenter.do?operation=5&fileId=$!map.get("id")&preId=$!preId')"><font style="color: #7B7B7B;">$globals.subTitle($globals.replaceHTML("$!map.Description"),320)</font></a></P></div>
						<div class="newsIon">
							<span style="float: left; margin-left: 30px;">
							#if($map.get("FilePath") != "")
							<img  src="style1/images/78.bmp" alt="$text.get("upload.lb.affix")" title="内容包含附件" />
							#end
							#if($map.get("Description").indexOf("<img") > -1) 
								<img  src="style1/images/234.png" alt="内容包含图片" title="内容包含图片"/>
							#end
							</span>
							<span>$globals.substring($map.get("createTime"),10)</span>
							
							<a href="javascript:goFarme('/OAKnowCenter.do?operation=5&fileId=$!map.get("id")&preId=$!preId')"><span class="NI_1">$!text.get("sms.note.detail")</span></a>
							#if($LoginBean.operationMap.get("/OAKnowledgeCenter.do").update())
							<a href="javascript:goFarme('/OAKnowledgeCenter.do?operation=7&position=detailpage&fileId=$!map.get("id")&preId=$!preId')"><span class="NI_2">$!text.get("sms.button.update")</span></a>
							#end
							#if($LoginBean.operationMap.get("/OAKnowledgeCenter.do").delete())
							<a href="javascript:deleteFile('$!map.get("id")','$!preId')"><span class="NI_3">$text.get("oa.common.del")</span></a>
							#end
						</div>
					</li>
					#set($preId = $!map.get("id"))
					#end
					#if($knowMap.size()==0)
						<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">未找到与您查询条件相匹配的信息</div>
					#end
				</ul>
				<script type="text/javascript">
				var o=document.getElementById("nn");
				if(o){
					var gs=o.childNodes;
					for(i=0;i<gs.length;i++){
					    gs[i].className=(i%2==0)?"c1":"c2";
					}
				}
				</script>
			</td>
		</tr>
	</table>
	<div class="bottom">
				<div class="bottom_left">
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").add() || $LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").delete() || $LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").update())
					<span style="margin-left: 10px;">目录操作：</span>&nbsp;|&nbsp;
					#end
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").add())
						<a href="javascript:void(0);" onclick="groupOperate(1);">
							<img src="style1/images/oaimages/addfloder.png" width="14" height="15" /><font style="margin-left: 2px;">添加子目录</font>
						</a>
					#end
				
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").update())
						<a href="javascript:void(0);" onclick="groupOperate(3);">
							<img src="style1/images/oaimages/setfloder.png" width="14" height="15" /><font style="margin-left: 2px;">目录设置</font>
						</a>
					#end
					
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAKnowledgeCenterFolder").delete())
						<a href="javascript:void(0);" onclick="groupOperate(2);">
							<img src="style1/images/oaimages/deletefloder.png" width="14" height="15" /><font style="margin-left: 2px;">删除目录</font>
						</a>
					#end
				</div>
				<div class="listRange_pagebar">
					$!pageBar
				</div>
			</div>
	<div id="w" class="search_popup" style="display:none;height: 190px;top: 150px;left: 250px;">
			<input type="hidden" name="queryType" id="queryType" value="$!OAKnowSearchForm.queryType"/>
			<div id="Divtitle" style="cursor: move;">
				<span class="ico_4"></span>&nbsp;条件查询
			</div>
				<table style="padding-top: 5px;">				
					<tr>
						<td align="right">标题:<br /><br /></td>
						<td align="left" colspan="3">
							<input size="13" name="fileTitleSearch" type="text" onKeyDown="if(event.keyCode==13) subForm();" value="$!OAKnowSearchForm.fileTitleSearch"/>
								&nbsp;&nbsp;创建人:
							<input name="createBySearch" id="createBySearch" type="hidden" value="$!OAKnowSearchForm.createBySearch"/>
							<input name="proUserName" id="proUserName" size="13" onKeyDown="if(event.keyCode==13) subForm();" type="text"  ondblclick="deptPopForAccount('userGroup','proUserName','createBySearch');" value="$!OAKnowSearchForm.proUserName" />
							<img style="cursor:pointer;" src="/style1/images/St.gif" onClick="deptPopForAccount('userGroup','proUserName','createBySearch');" />
						</td>
					</tr>
					<tr>
						<td align="right">&nbsp;&nbsp;创建时间:<br /><br /></td>
						<td align="left" colspan="3">
							<input name="beginTimeSearch" size="13"
								value="$!OAKnowSearchForm.beginTimeSearch" 
								onKeyDown="if(event.keyCode==13) subForm();"
								onClick="openInputDate(this);"  />
							&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="endTimeSearch" size="13" value="$!OAKnowSearchForm.endTimeSearch"
								onKeyDown="if(event.keyCode==13) subForm();"
								onClick="openInputDate(this);" />
						</td>
					</tr>
						<td width="20%" align="right">所属组:<br /><br /></td>
						<td align="left">
							<input type="hidden" name="groupIdSearch" id="groupIdSearch" value="$!OAKnowSearchForm.groupIdSearch" />
							<input name="groupNameSearch" id="groupNameSearch" type="text" value="$!OAKnowSearchForm.groupNameSearch" size="13" ondblclick="show1();" onKeyDown="if(event.keyCode==13) subForm();" />
								<img onClick="show1()" src="/style1/images/St.gif" class="search"  />
						</td>
						<td></td>
				</table>
				 <span class="search_popup_bu"><input type="button" 
						onclick="document.form.operation.value='null';document.form.queryType.value='keyword';document.form.submit();" class="bu_1"  />
					<input type="button" onClick="closeSearch();" class="bu_2"  />
				</span>
		</div>
		</table>
</form>
	<script language="javascript">
var posX;
var posY;       
	fdiv = document.getElementById("w");         
	document.getElementById("Divtitle").onmousedown=function(e){
	    if(!e) e = window.event;  //IE
	    posX = e.clientX - parseInt(fdiv.style.left);
	    posY = e.clientY - parseInt(fdiv.style.top);
	    document.onmousemove = mousemove;           
	}

document.onmouseup = function(){
    document.onmousemove = null;
}
function mousemove(ev){
    if(ev==null) ev = window.event;//IE
    fdiv.style.left = (ev.clientX - posX) + "px";
    fdiv.style.top = (ev.clientY - posY) + "px";
}
</script>
</body>
</html>
