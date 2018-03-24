<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<title>$text.get("oa.common.bylaw")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />	
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript">
var x="$!ordainSearchForm.selectId";
var y="$!ordainSearchForm.selectType";
var n="$!thetype";
if(y=="type"){
	self.parent.frames["leftFrame"].insertType(x);	
}
	
function showSearch(){
	if($('#w').css("display")== "none"){
	 $('#w').show();
	 $('#w').append("<input type='hidden' name='dbData' id='dbData' value='' onpropertyChange='javascript:if(this.value!=null)evaluate()'/>");
	}else{
	 $('#w').hide();
	 $('#dbData').remove();
	}
}

function closeSearch(){
	$('#w').hide();
	$('#dbData').remove();
}

function subForm(){
	myform.submit();
	closeSearch();
}

function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
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
　　　　　			document.location = "/OAnewOrdain.do?operation=3&type=ordain&ordainId="+mydatasIds;
　			}});	
	}else{
		asyncbox.alert('$text.get('common.msg.mustSelectOne')','$!text.get("clueTo")');
	}
}

function show1(){
	var fCode = window.parent.frames["leftFrame"].document.getElementById("fCode").value;
	if(fCode.length==0){
		asyncbox.alert('当前没有可选组','$!text.get("clueTo")');
		return false;
	}
	var displayName="$text.get('oa.common.bylaw.group')"; 
	var url = "/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=tblEmpGroup&selectName=OrdainGroupNote&classCode="+fCode+"&popupWin=Popdiv&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName;
	asyncbox.open({id:'Popdiv',title:displayName,url:url,width:750,height:470})
}

function isDelete(m){
	asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
	　　 if(action == 'ok'){
		document.location = "/OAnewOrdain.do?operation=$globals.getOP("OP_DELETE")&type=ordain&ordainId="+m;
		}
	});
}	

function dingWei(){
	var ordainId="$!selectOrdain";
	if(ordainId!= ''){
		window.location.href="#"+ordainId;
	}
}

function groupOperate(m){
	if("$ordainSearchForm.selectType"!="type" ){
		asyncbox.alert('请先选择一个目录!','$!text.get("clueTo")');
		return false;
	}
	switch (m){
	case  1:
		btn("$!ordainSearchForm.selectId",'2');
		
		break;
	case  2:
		del("$!ordainSearchForm.selectId",'$!flag');
		
		break;
	case  3:	
		var selectId = $('#selectId').val();	
   		str = selectId.charAt(5);  
   		if(str != "") {
			updategroup("$!ordainSearchForm.selectId",'2');
   		}else{
   			updategroup("$!ordainSearchForm.selectId",'1');
   		}               
		break;
	}
}
function btn(floderId,flag){
	var date=new Date();
	asyncbox.open({
		id : 'dealdiv',
　　　   	url : '/OAOrdainGroupCenter.do?operation=4&insertPlace=listpage&dealType=addGroup&time='+date+'&folderId='+floderId + "&flag=" +flag,
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
		   		//asyncbox.confirm('您现在是否对组进行授权?','$!text.get("clueTo")',function(action){
				　//　 if(action == 'ok'){
					//	return false;
				 //   }
				//});
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
	self.parent.frames["leftFrame"].refreshself();
	window.location.reload();
}
function updategroup(id,flag){
	asyncbox.open({
		id : 'dealdiv',
　　		url : '/OAOrdainGroupCenter.do?operation=4&insertPlace=listpage&dealType=updateGroup&folderId='+id + '&flag=' + flag,
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

function del(id){
	var str="/OAOrdainGroupCenter.do?operation=$globals.getOP("OP_QUERY")&insertPlace=listpage&keyIds="+id+"&dealType=del&check=true";
	AjaxRequest(str);
 	value = response;
 	var x=new Array();
 	x=value.split(";");
 
 	if(x[0] == "0"){
 		asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
		　　 if(action == 'ok'){
				document.location = "/OAOrdainGroupCenter.do?operation=$globals.getOP("OP_QUERY")&insertPlace=listpage&keyIds="+x[2]+"&dealType=del";
		    }
		});		
 	}else{
 		asyncbox.alert("目录:"+x[1]+"包含子目录或数据,不可以删除!","$!text.get("clueTo")");
 	}
}
function refreshTree(){
	if("$!insertPlace"!=null &&  "$!insertPlace"!=""){
		self.parent.frames["leftFrame"].refreshself();
	}
}


function exePopdiv(returnValue){
	if(typeof(returnValue)=="undefined") return ;
	var note = returnValue.split("#;#") ;
	document.getElementById('groupId').value=note[0]
	document.getElementById('groupName').value=note[1]
	jQuery.close('Popdiv');
}


function fillData(datas){
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv');
}
</script>
	</head>
	<body onLoad="showStatus();dingWei();refreshTree();">
		<form method="post" name="form" id="form" action="/OAnewOrdain.do?type=ordain">
			<input type="hidden" name="operation" value="4" />
			<input type="hidden" name="selectId" id="selectId" value="$!ordainSearchForm.selectId"/>
			<input type="hidden" name="selectType" value="$!ordainSearchForm.selectType"/>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" >
				<tr>
					<td colspan="2" class="bigTitle" style="background-image: none">&nbsp;						
					</td>
				</tr>				
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="F_right" valign="middle">					
						<div class="right_title">
							#if(!($query && !$add && !$delete && !$update))
						 		&nbsp;&nbsp;<input type="checkbox" value="checkbox" name="selAll" onClick="checkAll('ordainId')"/>$text.get("common.lb.selectAll")&nbsp;						
							#end
							#if($add)
								<a href="/OAnewOrdain.do?operation=$globals.getOP("OP_ADD_PREPARE")&selectType=$!ordainSearchForm.selectType&selectId=$!ordainSearchForm.selectId">
								<img src="style1/images/oaimages/add.bmp"  style="margin-top: 5px;"/><font style="margin-left: 2px;">$text.get("common.lb.add")</font></a>
							#end&nbsp;	
							#if($delete)
								<a href="javascript:handle('ordainId',2)">
								<img  src="style1/images/oaimages/l-i-03.gif" style="margin-top: 5px;"/><font style="margin-left: 2px;">$text.get("oa.common.del")</font></a>									
							#end				
											
						</div>
		
					 <ul id="nn"
							style="overflow:hidden;overflow-y:auto;width:100%;padding:0;margin:0;">
<script type="text/javascript">
	var oDiv=document.getElementById("nn");
	var sHeight=document.documentElement.clientHeight-135;
	oDiv.style.height=sHeight+"px";
</script>
							#foreach ($ordain in $ordainList)
							<li #if($globals.isOddNumber($velocityCount)) class="c1" #else class="c2" #end>
								<div class="newsTitle">
								<a name="$!ordain.id"></a>
								#if(!($query && !$add && !$delete && !$update))
									<input type="checkbox"
											name="ordainId" value="$!ordain.id" />
								#end
											<a href="javascript:void(0);" onclick="javascript:window.location.href='/OAnewOrdain.do?operation=$globals.getOP("OP_DETAIL")&ordainId=$!ordain.id'">
											$globals.subTitle($globals.replaceHTML("$!ordain.ordainTitle"),150)
											</a>
							
								</div>
								<div class="newsCon">
									<P>
										<a href="javascript:void(0);" onclick="javascript:window.location.href='/OAnewOrdain.do?operation=$globals.getOP("OP_DETAIL")&ordainId=$!ordain.id'">
										  	#if($globals.subTitle($globals.replaceHTML("$!ordain.Content"),320)==" ")
												<img src="style1/images/234.png" style="cursor: pointer;"/>
											#else
												$globals.subTitle($globals.replaceHTML("$!ordain.Content"),320)
											#end					  
										</a>
							
									</P>
								</div>
								<div class="newsIon">
									<span style="float: left; margin-left: 30px;">
										#if($!ordain.accessories != "")
											<img  src="style1/images/78.bmp" alt="$text.get("upload.lb.affix")" title="内容包含附件"/>
										#end
										#if($!ordain.Content.indexOf("<img") > -1)
											<img  src="style1/images/234.png" alt="内容包含图片" title="内容包含图片"/>
										#end
									</span>
									
									
									<span>$globals.substring($!ordain.createTime,10)</span>
									<a href="javascript:void(0);" onclick="javascript:window.location.href='/OAnewOrdain.do?operation=$globals.getOP("OP_DETAIL")&ordainId=$!ordain.id'"><span class="NI_1">$text.get("common.lb.detail")</span></a>
									#if($update)
										<a href="javascript:void(0);" onclick="javascript:window.location.href='/OAnewOrdainAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&ordainId=$!ordain.id'" ><span class="NI_2">$text.get("oa.common.upd")</span></a>
									#end
									#if($delete)
									
										<a href="javascript:isDelete('$ordain.id');"><span class="NI_3">$text.get("oa.common.del")</span></a>
									#end
								</div>
							</li>
							#end
							#if($ordainList.size()==0)
								<div class="nodata">未找到与您查询条件相匹配的信息</div>		
						   	#end	
						</ul>
					</td>
				</tr>
				</table>
			</table>
			<div class="bottom">
				<div class="bottom_left">
				
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAOrdainGroup").add() ||  $LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAOrdainGroup").delete() || $LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAOrdainGroup").update())
					<span style="margin-left: 10px;">目录操作：</span>&nbsp;|&nbsp;
					#end
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAOrdainGroup").add())
						<a href="javascript:void(0);" onclick="groupOperate(1);">
							<img src="style1/images/oaimages/addfloder.png" width="14" height="15" /><font style="margin-left: 2px;">添加子目录</font>
						</a>
					#end		
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAOrdainGroup").update())
						<a href="javascript:void(0);" onclick="groupOperate(3);">
							<img src="style1/images/oaimages/setfloder.png" width="14" height="15" /><font style="margin-left: 2px;">目录设置</font>
						</a>
					#end
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=OAOrdainGroup").delete())
						<a href="javascript:void(0);" onclick="groupOperate(2);">
							<img src="style1/images/oaimages/deletefloder.png" width="14" height="15" /><font style="margin-left: 2px;">删除目录</font>
						</a>
					#end
				</div>
				<div class="listRange_pagebar">
					$!pageBar
				</div>
			</div>
		</form>


		<div id="w" class="search_popup" style="display:none;height: 190px;top: 150px;left: 250px;">
			<div id="Divtitle" style="cursor: move;">
				<span class="ico_4"></span>$text.get("com.query.conditions")
			</div>
			<form method="post" name="myform" action="/OAnewOrdain.do?operation=4&selectType=gaoji">
				<table style="margin-top: 10px;">				
					<tr>
						<td align="right">$text.get("oa.common1.title")：</td>
						<td><input size="13" name="ordainTitle" type="text" value="$!ordainSearchForm.ordainTitle" onKeyDown="if(event.keyCode==13) subForm();"/><br /></td>
						<td>创建人：</td>
						<td>
							<input name="createBy"  id="createBy" type="hidden" value="$!ordainSearchForm.createBy"/>
							<input name="proUserName"  id="proUserName" size="13" onKeyDown="if(event.keyCode==13) subForm();" type="text" readonly="readonly"  onClick="deptPopForAccount('userGroup','proUserName','createBy');" value="$!proUserName" />
							<img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccount('userGroup','proUserName','createBy');" />
						</td>
					</tr>
					<tr>
						<td align="right"><span style="margin-left: 15px;">$text.get("common.lb.createTime")：</span></td>
						<td >
							<input name="beginTime" size="13"
								value="$!ordainSearchForm.beginTime" 
								onKeyDown="if(event.keyCode==13) subForm();"
								onClick="openInputDate(this);"  />
						</td>
						<td align="center">至</td>
						<td>
							<input name="endTime" size="13" value="$!ordainSearchForm.endTime"
								onKeyDown="if(event.keyCode==13) subForm();"
								onClick="openInputDate(this);" />
						</td>
					</tr>
					<tr>
						<td  align="right">$text.get("or.ordain.gorup")：</td>
						<td  colspan="3">
							<input type="hidden" name="groupId" id="groupId" value="$!ordainSearchForm.groupId" />
							<input name="groupName" id="groupName" type="text" value="$!groupName" readonly="true" size="13" onclick="show1();" onKeyDown="if(event.keyCode==13) subForm();" />
								<img onClick="show1()" src="/$globals.getStylePath()/images/St.gif" class="search"  />
						</td>
					</tr>	
				</table>
				 <span class="search_popup_bu"><input type="button" 
						onClick="subForm();" class="bu_1"  />
					<input type="button" onClick="closeSearch();" class="bu_2"  />
				</span>
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
				var y="$!ordainSearchForm.selectType";
				if(y == "returnindex"){
					 $('#w').show();
				 }
			</script>
		</div>
</html>

