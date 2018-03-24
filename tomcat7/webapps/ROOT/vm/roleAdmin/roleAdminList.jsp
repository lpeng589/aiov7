<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/style1/css/roleAdmin.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/popOperation.css" />
<link type="text/css" href="/style1/css/fg.menu.css" media="screen" rel="stylesheet" />
<link type="text/css" href="/style1/css/ui.all.css" media="screen" rel="stylesheet" />
<style type="text/css">
.opbt{background:none;}
#viewOpDiv{z-index: 99; position: absolute; left:40px; top:15px; display: none;}
.custom-menu li a{white-space:nowrap;}
.custom-menu li:hover {line-height: 19px!important;height: 19px!important;border: 1px solid #f47c02!important;background: #fa9b3d;}
.custom-menu li:hover a{color: #fff;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/fg.menu.js"></script>
<script type="text/javascript" src="/js/popOperationNew.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	//添加
	
	$("#add").bind("click", function(){
		location.href='/RoleAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex';
	});
	$("#batchdelete").bind("click", function(){
		if(sureDel('keyId')){document.form.submit();}
	});
	$(".tdimg").mouseover( function() {  $(this).find("img").show(); } ); 
	$(".tdimg").mouseout( function() { $(this).find("img").hide(); } ); 
	$("#data_list_id").height(document.documentElement.clientHeight-125);
});
	
//另存为



function copyOp(id,roleName,roleDesc){
	asyncbox.open({
 		id:'dealdiv',
		title : '[角色:'+roleName+']&nbsp;&nbsp;另存为',
		html :'<div style="width:300px;float:left;padding:10px 0 0 30px;"><span style="width:100px;display:block;padding-bottom:5px">新分组名称:</span><input id="saveName" style="width:300px" name="saveName" value="'+roleName+'"/></div>'+
			'<div style="width:300px;float:left;padding:10px 0 0 30px;"><span style="width:100px;display:block;padding-bottom:5px">描述:</span><input id="saveDesc" style="width:300px" name="saveDesc" value="'+roleDesc+'"/></div>',
		width : 400,
		height :220,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
	  		if(action == 'ok'){
	  		  	if(roleName == $("#saveName").val()){
	  		  		alert("分组名称重复");
	  		  		return false;
	  		  	}
	  			ustr= '/RoleAction.do?operation=$globals.getOP("OP_UPDATE")&winCurIndex=$!winCurIndex&copy=copy&saveName='+encodeURIComponent($("#saveName").val())+'&saveDesc='+encodeURIComponent($("#saveDesc").val())+'&sourceRoleId='+id;
	  			location.href=ustr;
	  		}
  	  }
	});		
}

//各种范围权限弹出窗的回调关闭函数
function dealAsyn(act){
	//当删除范围权限时，只需刷新界面
	if(act=="refresh"){
		jQuery.reload('setScopediv'); 
	}else{
		if(jQuery.exist('AddUpdatediv')){
			jQuery.close('AddUpdatediv');
			jQuery.reload('setScopediv'); 
		}else if(jQuery.exist('setScopediv')){
			jQuery.close('setScopediv'); 
		}
	}
	if(jQuery.exist('dealdiv')){
		jQuery.close('dealdiv'); 
	}
	
}

//设置各种范围权限
function setScope(type,id,roleName,tableName,tableDisplay){
	tit = '';
	opDiv= '';
	height=document.documentElement.clientHeight-50;
	if(type=="5"){ //部门管辖范围有确定按扭，其它没有，所以单独列出 
		tit = "部门管辖范围";
		asyncbox.open({
	 		id:'setScopediv',
			title : '设置[角色:'+roleName+']&nbsp;&nbsp;'+tit,
			url :'/RoleAction.do?operation=31&method=scopeMain&roleId='+id+'&winCurIndex=&!winCurIndex&roleName='+roleName+'&type='+type,
			width : 800,
			height :height,
			btnsbar : jQuery.btn.OKCANCEL,
			callback : function(action,opener){
		  		if(action == 'ok'){
		  			opener.saveDept();
		  			return false;
		  		}
	  	  }
		});		
		return;
	}else if(type=="1"){
		tit = "职员管辖范围";
	}else if(type=="0"){
		tit = tableDisplay;
	}else if(type=="4"){
		tit = "单据列控制";
	}else if(type=="6"){
		tit = "查看范围值控制";
	}
	asyncbox.open({
 		id:'setScopediv',
		title : '设置[角色:'+roleName+']&nbsp;&nbsp;'+tit,
		url :'/RoleAction.do?operation=31&method=scopeMain&roleId='+id+'&winCurIndex=&!winCurIndex&roleName='+roleName+'&type='+type+'&tableName='+tableName,
		width : 800,
		height :height,
		btnsbar : jQuery.btn.CANCEL,
		callback : function(action,opener){
	  		
  	  	}
	});		
	
}

//关键字搜索





function doquery(){
	document.form.operation.value=$globals.getOP("OP_QUERY");
	if(document.form.name.value.replace(/(^\s*)|(\s*$)/g,'')=='关键字搜索...'){document.form.name.value='';} 
	document.form.submit();
}


//分配用户
function showUser(roleName,id){
 	asyncbox.open({
 		id:'dealdiv',
		title : '[角色:'+roleName+']&nbsp;&nbsp;拥有的用户',
		url :'/RoleQueryAction.do?operation=4&showType=showUser&keyId='+id,
		width : 470,
		height :260,
		btnsbar : jQuery.btn.OKCANCEL.concat(
		[{
     		text    : '撤消所有用户',                  //按钮文本
      		action  : 'clearAll'             //按钮 action 值，用于按钮事件触发 唯一
  		}]),
		callback : function(action,opener){
		var oldVal=opener.myform.oldUser.value;
		var newVal=opener.myform.allUser.value;
  		if(action == 'ok'){
  			if(oldVal!=newVal){ //判断是否修改了用户


	  			opener.myform.submit();
	  			return false;
  			}else{
  				dealAsyn();
  				return false;
  			}
  		}else if(action == 'clearAll'){
  			if (confirm("您确定要撤销该角色的所有用户?")) {
					opener.jQuery("#allUser").val("");
					opener.myform.submit();	  				
			}
			return false;
  		}
  	  }
	});	
}
</script>
</head>
 
<body class="bg-html">
<iframe name="formFrame" style="display:none"></iframe>
<form class="wp-form"  method="post" scope="request" name="form" action="/RoleQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")"/>
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/> 
 <input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
		<div class="TopTitle top-btns" >							
			<span class="hBtns" id="batchdelete" title="删除">删除</span>							
			<span class="hBtns" id="add" title='$text.get("common.lb.add")'>$text.get("common.lb.add")</span>
			<span class="search-span">
				<input type="text" id="name" name="name" class="search_text" value="#if("$!roleSearchForm.getName()"=="")关键字搜索...#else$!roleSearchForm.getName()#end" onkeydown="if(event.keyCode==13) {doquery();}" onblur="if(this.value=='')this.value='关键字搜索...';" onfocus="if(this.value.replace(/(^\s*)|(\s*$)/g,'')=='关键字搜索...'){this.value='';}" />
				<b class="icon16" onclick="doquery();"></b>
			</span>
		</div>	
		<ul class="head-ul-list">
			<li class="cbox-li">
				<input class="cbox" type="checkbox" name="selAll" onclick="checkAll('keyId');">
			</li>
			<li class="ope-li">操作</li>
			<li class="dist-li">分组信息</li>
	        <li class="dist-li">已分配用户</li>
	        #if($globals.getSysSetting("sunCompany").equals("true"))
	        <li>$text.get("role.lb.roleSunCompany")</li>
	        #end
		</ul>			
						
		<div id="data_list_id" style="overflow:hidden;overflow-y:auto;width:100%;">
			#foreach ($row in $result )
			<ul class="body-ul-list" id="tr_$!accmain.id">
				<li class="cbox-li">
					<input class="cbox" type="checkbox" name="keyId" value="$globals.get($row,0)">
				</li>
				<li class="ope-li opbt" f1="分配模块权限:/RoleAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$globals.get($row,0)&winCurIndex=$!winCurIndex"
                f2="分配用户:javascript:void(0);showUser('#if($globals.get($row,0) == "1")$globals.getLocaleDisplay($defaultSunCmpName)#end$!globals.encodeHTMLLine($globals.get($row,1))','$globals.get($row,0)')"
                f3="另存为:javascript:void(0);copyOp('$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))','$!globals.encodeHTMLLine($globals.get($row,2))')"
                f4="部门管辖范围:javascript:void(0);setScope('5','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))')"
                f5="职员管辖范围:javascript:void(0);setScope('1','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))')"
                #foreach ($scrow in $scopeCata )
                #set($svc = $velocityCount+6)
                f$svc="$globals.get($scrow,3).get($globals.getLocale())管辖范围:javascript:void(0);setScope('0','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))','$globals.get($scrow,0)','$globals.get($scrow,3).get($globals.getLocale())管辖范围')" 
                #end
                #if($!globals.hasMainModule("1"))
                f15="单据列控制:javascript:void(0);setScope('4','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))')"
                f16="查看范围值控制:javascript:void(0);setScope('6','$globals.get($row,0)','$!globals.encodeHTMLLine($globals.get($row,1))')"
                #end>
					<span>操作<b class="triangle"></b></span>
				</li>
				<li class="dist-li">
					<p>#if($globals.get($row,0) == "1")$globals.getLocaleDisplay($defaultSunCmpName)#end$!globals.encodeHTMLLine($globals.get($row,1))</p>
					<p class="p-dep" style="height: 25px; overflow: hidden;">$!globals.encodeHTMLLine($globals.get($row,2))</p>
				</li>
                <li class="dist-li">
               	 	<div class="d-short">$!globals.encodeHTMLLine($globals.get($row,4))</div>
               	 	<div class="d-long">$!globals.encodeHTMLLine($globals.get($row,4))</div>
                </li>
                #if($globals.getSysSetting("sunCompany").equals("true"))
                    <li>$!globals.encodeHTMLLine($globals.getLocaleDisplay($globals.get($row,3)))</li>
                #end
			</ul>
			#end
			#if($result.size()==0)
			<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">
				暂无用户权限分组信息
			</div>
			#end
		</div>
					
					
		<div style="overflow:hidden;">
			<div class="listRange_pagebar">
				$!pageBar
			</div>	
		</div>		
	</form>
</body>
</html>

	