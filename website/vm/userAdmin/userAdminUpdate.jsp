
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("user.lb.userAdmin")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/sharingStyle.css"/>
<link type="text/css" rel="stylesheet" href="/style/themes/default/easyui.css"/>
<link type="text/css" rel="stylesheet" href="/style/themes/icon.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<style type="text/css">
.Li_ol{width:100%;height:34px;line-height:34px;float:left;font-weight:bold;border-top:1px solid #c8cece; border-bottom:1px solid #c8cece;background:#f9f9f9;}
.Li_ol span{padding-left:15px}
.ABK_Add div label{text-align:right;width:120px;float:left;}
.Company_U{width:100%; padding:0 0 0 0; overflow:hidden;}
.Company_U .Company_Li{float:left; width:150px; height:50px; overflow:hidden; font-size:12px; color:#666;}
.Company_U .Company_Li input{margin:0; padding:0;}
input.hBtns{background:none;font-family:microsoft yahei;height:25px;line-height:21px;}
input.hBtns:hover{background:#f2f2f2;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script> 
<script type="text/javascript" src="/js/formvalidate.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
 function selectAll(tagName,name,targetId,type){

	items = document.getElementsByTagName(tagName);
	for(var i=0;i<items.length;i++){
		if(items[i].type == type && items[i].name == name ){
			items[i].checked = document.getElementById(targetId).checked;
		}
	}
}


function openSelect(urlstr,obj,field){
	var str  = window.showModalDialog(urlstr,"","dialogWidth=730px;dialogHeight=450px");
	if(typeof(str)=="undefined") return ;
	fs=str.split(";");   	
	//保存字段
	document.form.employeeId.value = fs[0];	
	document.form.employeeName.value = fs[1];	
	return 0;
}

putValidateItem("employeeId","$text.get("userManager.lb.userName")","any","",false,0,30);	
putValidateItem("sysName","$text.get("userManager.lb.LoginName")","any","",false,0,30);	
putValidateItem("ipstart","$text.get("userManager.lb.ipstart")","ip","",true,0,30);	
putValidateItem("ipend","$text.get("userManager.lb.ipend")","ip","",true,0,30);	


function beforSubmit(form){  
	if("$type"=="modifyPass"){
		var newpassword=document.form.newpassword.value;
		var confirmpassword=document.form.confirmpassword.value;
	    if(!isNull(newpassword,'$text.get("sms.modpass.newpass")')){
			//alert("请输入新密码！");
			return false;
		} else if(!isNull(confirmpassword,'$text.get("userManager.lb.confirmpassword")')){
			//alert("请输入确认密码！");
			return false;
		}else if(trimStr(form.newpassword.value) != trimStr(form.confirmpassword.value)) {
			alert("$text.get("userManager.msg.confirmPassword")");
			form.newpassword.value = "";
			form.confirmpassword.value = "";
			form.newpassword.focus();
			return false;
		}
	}else{
		if(trimStr(form.password.value) != trimStr(form.confirmpassword.value)) {
			alert("$text.get("userManager.msg.confirmPassword")");
			form.password.value = "";
			form.confirmpassword.value = "";
			form.password.focus();
			return false;
		}
		
		if((document.form.loginStartTime1.value != "" && document.form.loginEndTime1.value == "") || (document.form.loginStartTime1.value == "" && document.form.loginEndTime1.value != "")){
			alert("$text.get("userMnager.msg.loginTimeOneError")");
			return false;
		}
		if(document.form.loginStartTime1.value != "" && document.form.loginEndTime1.value != ""){
			document.form.loginStartTime.value=document.form.loginStartTime2.value != ""?document.form.loginStartTime1.value+":"+document.form.loginStartTime2.value:document.form.loginStartTime1.value+":00";
			document.form.loginEndTime.value=document.form.loginEndTime2.value != ""?document.form.loginEndTime1.value+":"+document.form.loginEndTime2.value:document.form.loginEndTime1.value+":00";
			if(document.form.loginEndTime.value <= document.form.loginStartTime.value){
				alert("$text.get("userMnager.msg.loginTimeLessError")");
				return false;
			}
		}else{
			document.form.loginStartTime.value="";
			document.form.loginEndTime.value="";
		}
	
		if(form.password.value.length>0 && trimStr(form.password.value)==""){
			alert("密码不能为空.");
			form.password.value = "";
			form.confirmpassword.value = "";
			form.password.focus();
			return false;
		}
		var noSys=true;
		#foreach($erow in $globals.getEnumerationItems("MainModule"))
			#if($!erow.value=="1")
			if($("#canJxc")[0].checked){
				noSys=false;
			}
			#end
			#if($!erow.value=="2")
			if($("#canOa")[0].checked){
				noSys=false;
			}
			#end
			#if($!erow.value=="3")
			if($("#canCrm")[0].checked){
				noSys=false;
			}
			#end
		#end 
		if(noSys){
			alert("至少选择一个功能模块");
			return;
		}
		if(!validate(form))return false;
		disableForm(form);
	}
	
	return true;
}
	
$("input")		
			
function addRoleId(obj,id){
	var input = document.getElementById(id);
	if(obj.checked){
		input.value += obj.value + ";";
	}else{
		input.value = input.value.replaceAll(obj.value + ";","");
	}					
}
  function isNull(variable,message)
{
	if(variable=="" || variable==null)
	{
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else
	{
		return true;
	}
			
}
function showSpan(obj,currentId){
	//显示或隐藏所选分支机构下的角色选择框，同时修改sunCompanyIds隐藏域的值






	var span = document.getElementById("span_" + currentId);
	var sunCompanyId = document.getElementById("sunCompanyIds_" + currentId);
	if(obj.checked){
		span.style.display = "inline";
		sunCompanyId.value = currentId;
	}else{
		span.style.display = "none";
		sunCompanyId.value = "";
	}
}

function selectDefaultPage(){
	var urlstr = '/UserFunctionAction.do?operation=22&tableName=tblModules&fieldName=linkAddress&selectName=SelectSySModules'+"&MOID=$MOID&MOOP=query&popupWin=PagePopdiv" ;
	asyncbox.open({id:'PagePopdiv',title:'默认页面弹出框',url:urlstr,width:750,height:470});
	//var str = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=query","","dialogWidth=730px;dialogHeight=450px"); 
	//var content = str.split(";") ;
	//document.getElementById("defaultPage").value=content[0];
	//document.getElementById("defaultPageName").value=content[1];
}

//默认页面回填弹出框






function exePagePopdiv(returnValue){
	var content = returnValue.split(";") ;
	$("#defaultPage").val(content[0]);
	$("#defaultPageName").val(content[1]);
}

function getuserKeyId(){
	 try
	 {
		 //创建插件或控件






		 if(navigator.userAgent.indexOf("MSIE")>0 && !navigator.userAgent.indexOf("opera") > -1)
		 {
			aObject = new ActiveXObject("Syunew6A.s_simnew6");
		 }else{
			aObject = document.getElementById('s_simnew61');
		 }
		 
		 DevicePath = aObject.FindPort(0);
		 if( aObject.LastError!= 0 )
		 {
			window.alert ("$text.get("common.msg.plsInsertUsbKey")");
			return ;
		 }
		 rUserKeyId=toHex(aObject.GetID_1(DevicePath))+toHex(aObject.GetID_2(DevicePath));
		 if( aObject.LastError!= 0 )
		 {
			 window.alert("$text.get("common.msg.readUsbKeyError")" +aObject.LastError.toString());
			 return ;
		 }
		 document.form.userKeyId.value = rUserKeyId.toUpperCase();	 
								 
	}
	catch (e) 
	{
			alert("$text.get("common.msg.activexerror")");
	}
}

var digitArray = new Array('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f');

function toHex( n ) {

        var result = ''
        var start = true;

        for ( var i=32; i>0; ) {
                i -= 4;
                var digit = ( n >> i ) & 0xf;

                if (!start || digit != 0) {
                        start = false;
                        result += digitArray[digit];
                }
        }

        return ( result == '' ? '0' : result );
}

//表单验证

$(function(){
	$('#uiform input').each(function () { 
    	if ($(this).attr('required') || $(this).attr('validType')) 
		$(this).validatebox(); 
    });
    jQuery.extend(jQuery.fn.validatebox.defaults.rules, {
	    remarks: {
	    	validator: function(value, param){
	            return value.length <= param[0];
	        },
	       // message: '$text.get("com.comm.remark.length")'
	       message:'您输入的通讯备注文字长度不能超过200'
	    },
	}); 
});

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
			title : '设置[用户:'+roleName+']&nbsp;&nbsp;'+tit,
			url :'/RoleAction.do?operation=31&method=scopeMain&roleId='+id+'&winCurIndex=&!winCurIndex&roleName='+roleName+'&type='+type+'&fromUser=true',
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
		title : '设置[用户:'+roleName+']&nbsp;&nbsp;'+tit,
		url :'/RoleAction.do?operation=31&method=scopeMain&roleId='+id+'&winCurIndex=&!winCurIndex&roleName='+roleName+'&type='+type+'&tableName='+tableName+'&fromUser=true',
		width : 800,
		height :height,
		btnsbar : jQuery.btn.CANCEL,
		callback : function(action,opener){
	  		
  	  	}
	});		
	
}
function updateModRight(url,roleName){
	openPop('setScopediv','设置[用户:'+roleName+']&nbsp;&nbsp;模块权限',url,1000,550,false,false); 
}

function nameFocus(){
	$("#sysName").focus();
}
</script>
</head>
<body onload="nameFocus()">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="#if($type.equals("modifyPass"))/UserManagePassAction.do#else/UserManageAction.do#end" 
	onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")" />
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()" />
<input type="hidden" name="id" value="$!result.id" />
<input type="hidden" name="type" value="$type" />
 <input type="hidden" name="winCurIndex" value="$!winCurIndex" />
 <input name="employeeId" id="employeeId" type="hidden"  value="$!result.id" />
 
<ul class="TopTitle">
	<li style="float:left; display:inline-block; text-align:center;">
		#if($type!="modifyPass")$text.get("user.lb.userAdmin")#else $text.get("user.lb.modifyPass") #end
	</li>
	#if($LoginBean.operationMap.get("/UserQueryAction.do").query() || $!LoginBean.id=="1")
	  #if($type!="modifyPass")
	  <li class="sharingbutton" style="display:inline-block;float:right;"><input type="button" onClick="location.href='/UserQueryAction.do?operation=$globals.getOP("OP_QUERY")&type=$type&winCurIndex=$!winCurIndex'" class="hBtns" value="$text.get("common.lb.back")" /></li>
	  #else
	  <li class="sharingbutton" style="display:inline-block;float:right;"><input type="button" onClick="location.href='/UserQueryPassAction.do?operation=$globals.getOP("OP_QUERY")&type=$type&winCurIndex=$!winCurIndex'" class="hBtns" value="$text.get("common.lb.back")" /></li>
	  #end
	#end
	#if($LoginBean.operationMap.get("/UserQueryAction.do").update() || $!LoginBean.id=="1")
	  <li class="sharingbutton" style="display:inline-block;float:right;"><input type="button" onClick="if(beforSubmit(document.form)){ document.form.winCurIndex.value='$!winCurIndex'; document.form.submit();}" class="hBtns" value="$text.get("mrp.lb.save")" /></li>
	#end
</ul>
<div class="dd" id="data_list_id">
<script type="text/javascript">
	var oDiv=document.getElementById("data_list_id");
	var sHeight=document.documentElement.clientHeight-65;
	oDiv.style.height=sHeight+"px";
</script>
<style>
</style>
<table>
	<tr>
		<td valign="top" class="list">
				<ul class="ABK_Add">					
					<ol><span >基本信息</span></ol>
					<li style="width:950px">
					
		<div><label>$text.get("userManager.lb.userName")：</label>
			<input type="text" name="employeeName" id="employeeName" class="easyui-validatebox" readonly="true" value="$!result.empFullName" validType="selectNames['OK','$!address.id']" required="true"/>
						<span>*</span></div>
		<div><label>$text.get("userManager.lb.LoginName")：</label>
			<input type="text" name="sysName" id="sysName" class="easyui-validatebox" onKeyDown="if(event.keyCode==13) event.keyCode=9" value="$!result.sysName"  #if("1" == $result.id) readonly="readonly" #end validType="selectNames['OK','$!address.id']" required="true"/>
						<span>*</span></div>
		<div><label>$text.get("userManager.lb.password")：</label>
			<input id="password" name="password" type="password" value="" onKeyDown="if(event.keyCode==13) event.keyCode=9"/>
		</div>
		
		<div><label>$text.get("userManager.lb.confirmpassword")：</label>
			<input id="confirmpassword" name="confirmpassword" type="password" value="" onKeyDown="if(event.keyCode==13) event.keyCode=9"/>
		</div>
		
		<div style="width:950px"><label>功能模块：</label>
		#foreach($erow in $globals.getEnumerationItems("MainModule"))
			#if($!erow.value=="1")
			<input id="canJxc" name="canJxc" type="checkbox" style="border:0px;width:16px;" value="1" #if("$!result.canJxc"=="1")checked #end #if("1" == $result.id) onclick="return false;" #end  onKeyDown="if(event.keyCode==13) event.keyCode=9"/>
			<label for="canJxc" style="text-align:left">ERP-进销存财务</label>	
			#end
			#if($!erow.value=="2")
			<input id="canOa" name="canOa" type="checkbox" style="border:0px;width:16px;" value="1" #if("$!result.canOa"=="1")checked #end #if("1" == $result.id) onclick="return false;" #end onKeyDown="if(event.keyCode==13) event.keyCode=9"/>
			<label for="canOa" style="text-align:left">OA-办公自动化</label>
			#end
			#if($!erow.value=="3")
			<input id="canCrm" name="canCrm" type="checkbox" style="border:0px;width:16px;" value="1" #if("$!result.canCrm"=="1")checked #end #if("1" == $result.id) onclick="return false;" #end onKeyDown="if(event.keyCode==13) event.keyCode=9"/>
			<label for="canCrm" style="text-align:left">CRM-客户关系管理</label>
			#end
		#end
		</div>
		<div style="width:950px"><label>默认平台：</label>	
			<input type="radio" id="showERP" name="showDesk"style="border:0px;width:16px;"  value="ERP" #if("$!result.showDesk"=="ERP")checked #end />
			<label style="text-align:left;"  for="showERP">ERP-进销存财务</label>
			<input type="radio" id="showOA" name="showDesk" style="border:0px;width:16px;" value="OA" #if("$!result.showDesk"=="OA")checked #end />
			<label style="text-align:left;"  for="showOA">OA-办公自动化</label>
			<!-- 
			<input type="radio"  id="showNewOA" name="showDesk" style="border:0px;width:16px;" value="newOA" #if("$!result.showDesk"=="newOA")checked #end />
			<label style="text-align:left;" for="showNewOA">办公平台</label> -->						
		</div>
		
					</li>
					<li class="Li_ol"><span>安全设置</span></li>
					<li style="width:800px;color:#000000; padding:0 0 10px 0; overflow:hidden;">
					<div><label>$text.get("userManger.lb.loginStartTime")：</label>
					<input type="hidden" name=loginStartTime value="$!result.loginStartTime"/>
							<select name="loginStartTime1" id="loginStartTime1" style="width: 80px;" onKeyDown="if(event.keyCode==13) event.keyCode=9">
			<option value="" ></option>
			<option value="00" #if("$!result.loginStartTime.substring(0,2)" =="00") selected #end>00</option>
			<option value="01" #if("$!result.loginStartTime.substring(0,2)" =="01") selected #end >01</option>	
			<option value="02" #if("$!result.loginStartTime.substring(0,2)" =="02") selected #end >02</option>
			<option value="03" #if("$!result.loginStartTime.substring(0,2)" =="03") selected #end >03</option>	
			<option value="04" #if("$!result.loginStartTime.substring(0,2)" =="04") selected #end >04</option>
			<option value="05" #if("$!result.loginStartTime.substring(0,2)" =="05") selected #end >05</option>	
			<option value="06" #if("$!result.loginStartTime.substring(0,2)" =="06") selected #end >06</option>
			<option value="07" #if("$!result.loginStartTime.substring(0,2)" =="07") selected #end >07</option>	
			<option value="08" #if("$!result.loginStartTime.substring(0,2)" =="08") selected #end >08</option>
			<option value="09" #if("$!result.loginStartTime.substring(0,2)" =="09") selected #end>09</option>	
			<option value="10" #if("$!result.loginStartTime.substring(0,2)" =="10") selected #end >10</option>
			<option value="11" #if("$!result.loginStartTime.substring(0,2)" =="11") selected #end >11</option>
			<option value="12" #if("$!result.loginStartTime.substring(0,2)" =="12") selected #end>12</option>
			<option value="13" #if("$!result.loginStartTime.substring(0,2)" =="13") selected #end>13</option>	
			<option value="14" #if("$!result.loginStartTime.substring(0,2)" =="14") selected #end>14</option>
			<option value="15" #if("$!result.loginStartTime.substring(0,2)" =="15") selected #end>15</option>	
			<option value="16" #if("$!result.loginStartTime.substring(0,2)" =="16") selected #end>16</option>
			<option value="17" #if("$!result.loginStartTime.substring(0,2)" =="17") selected #end>17</option>	
			<option value="18" #if("$!result.loginStartTime.substring(0,2)" =="18") selected #end>18</option>
			<option value="19" #if("$!result.loginStartTime.substring(0,2)" =="19") selected #end>19</option>	
			<option value="20" #if("$!result.loginStartTime.substring(0,2)" =="20") selected #end>20</option>
			<option value="21" #if("$!result.loginStartTime.substring(0,2)" =="21") selected #end>21</option>	
			<option value="22" #if("$!result.loginStartTime.substring(0,2)" =="22") selected #end>22</option>
			<option value="23" #if("$!result.loginStartTime.substring(0,2)" =="23") selected #end>23</option>			
		</select>:
		<select  name="loginStartTime2" onKeyDown="if(event.keyCode==13) event.keyCode=9"   style="width:80px">
			<option value="" ></option>
			<option value="00"  #if("$!result.loginStartTime.substring(3)" =="00") selected #end>00</option>
			<option value="10"  #if("$!result.loginStartTime.substring(3)" =="10") selected #end>10</option>	
			<option value="20"  #if("$!result.loginStartTime.substring(3)" =="20") selected #end>20</option>		
			<option value="30"  #if("$!result.loginStartTime.substring(3)" =="30") selected #end>30</option>		
			<option value="40"  #if("$!result.loginStartTime.substring(3)" =="40") selected #end>40</option>		
			<option value="50"  #if("$!result.loginStartTime.substring(3)" =="50") selected #end>50</option>				
		</select></div>	
			<div><label>$text.get("userManger.lb.loginEndTime")：</label>
					<input type="hidden" name=loginEndTime value="$!result.loginEndTime"/>
							<select name="loginEndTime1" id="loginEndTime1" style="width: 80px;" onKeyDown="if(event.keyCode==13) event.keyCode=9">
							<option value="" ></option>
			<option value="00" #if("$!result.loginEndTime.substring(0,2)" =="00") selected #end>00</option>
			<option value="01" #if("$!result.loginEndTime.substring(0,2)" =="01") selected #end >01</option>	
			<option value="02" #if("$!result.loginEndTime.substring(0,2)" =="02") selected #end >02</option>
			<option value="03" #if("$!result.loginEndTime.substring(0,2)" =="03") selected #end >03</option>	
			<option value="04" #if("$!result.loginEndTime.substring(0,2)" =="04") selected #end >04</option>
			<option value="05" #if("$!result.loginEndTime.substring(0,2)" =="05") selected #end >05</option>	
			<option value="06" #if("$!result.loginEndTime.substring(0,2)" =="06") selected #end >06</option>
			<option value="07" #if("$!result.loginEndTime.substring(0,2)" =="07") selected #end >07</option>	
			<option value="08" #if("$!result.loginEndTime.substring(0,2)" =="08") selected #end >08</option>
			<option value="09" #if("$!result.loginEndTime.substring(0,2)" =="09") selected #end>09</option>	
			<option value="10" #if("$!result.loginEndTime.substring(0,2)" =="10") selected #end >10</option>
			<option value="11" #if("$!result.loginEndTime.substring(0,2)" =="11") selected #end >11</option>
			<option value="12" #if("$!result.loginEndTime.substring(0,2)" =="12") selected #end>12</option>
			<option value="13" #if("$!result.loginEndTime.substring(0,2)" =="13") selected #end>13</option>	
			<option value="14" #if("$!result.loginEndTime.substring(0,2)" =="14") selected #end>14</option>
			<option value="15" #if("$!result.loginEndTime.substring(0,2)" =="15") selected #end>15</option>	
			<option value="16" #if("$!result.loginEndTime.substring(0,2)" =="16") selected #end>16</option>
			<option value="17" #if("$!result.loginEndTime.substring(0,2)" =="17") selected #end>17</option>	
			<option value="18" #if("$!result.loginEndTime.substring(0,2)" =="18") selected #end>18</option>
			<option value="19" #if("$!result.loginEndTime.substring(0,2)" =="19") selected #end>19</option>	
			<option value="20" #if("$!result.loginEndTime.substring(0,2)" =="20") selected #end>20</option>
			<option value="21" #if("$!result.loginEndTime.substring(0,2)" =="21") selected #end>21</option>	
			<option value="22" #if("$!result.loginEndTime.substring(0,2)" =="22") selected #end>22</option>
			<option value="23" #if("$!result.loginEndTime.substring(0,2)" =="23") selected #end>23</option>			
		</select>:
		<select  name="loginEndTime2" onKeyDown="if(event.keyCode==13) event.keyCode=9"   style="width:80px">
			<option value="" ></option>
			<option value="00"  #if("$!result.loginEndTime.substring(3)" =="00") selected #end>00</option>
			<option value="10"  #if("$!result.loginEndTime.substring(3)" =="10") selected #end>10</option>	
			<option value="20"  #if("$!result.loginEndTime.substring(3)" =="20") selected #end>20</option>		
			<option value="30"  #if("$!result.loginEndTime.substring(3)" =="30") selected #end>30</option>		
			<option value="40"  #if("$!result.loginEndTime.substring(3)" =="40") selected #end>40</option>		
			<option value="50"  #if("$!result.loginEndTime.substring(3)" =="50") selected #end>50</option>					
		</select>
			</div>	
			<div><label>$text.get("userManager.lb.ipstart")：</label>
					 <input type="text" name="ipstart" class="easyui-validatebox"  id="ipstart" value="$!result.ipstart" onKeyDown="if(event.keyCode==13) event.keyCode=9" />
			</div> 
			<div><label>$text.get("userManager.lb.ipend")：</label>
					 <input type="text" name="ipend" class="easyui-validatebox"  id="ipend" value="$!result.ipend" onKeyDown="if(event.keyCode==13) event.keyCode=9" />
			</div>  
			<div><label>$text.get("userManager.lb.MACAddress")：</label>
					 <input type="text" name="MACAddress" class="easyui-validatebox"  id="MACAddress" value="$!result.MACAddress" onKeyDown="if(event.keyCode==13) event.keyCode=9" />
			</div> 
			<div style="width:400px"><label>$text.get("userManager.lb.userKeyId")：</label>
					 <input type="text" name="userKeyId" class="easyui-validatebox"  id="userKeyId" value="$!result.userKeyId" 
					 	onKeyDown="if(event.keyCode==13) event.keyCode=9"  style="width:150px"/>
					 <input onclick="getuserKeyId()" type="button" value="$text.get("common.lb.automatismAchieve")" class="b2" style="width:80px;height:26px; cursor:pointer; background:url(../../style1/images/BtnBG.jpg) repeat-x 0 0; margin:0 0 0 5px;"/>
			</div>
									<div>
										<label>
											移动设备ID：











										</label>
										<input type="text" name="mobileID" class="easyui-validatebox"
											id="mobileID" value="$!result.mobileID"
											onKeyDown="if(event.keyCode==13) event.keyCode=9" />
									</div>
									<div>
										<input id="checkMobile" name="checkMobile" type="checkbox"
											style="border:0px;width:16px;" value="1"
											#if("$!result.checkMobile"== "1")checked #end
											onKeyDown="if(event.keyCode==13) event.keyCode=9" />
										<label for="checkMobile" style="text-align:left">
											校验移动设备ID
										</label>

									</div>


								</li>
					  
					<!-- <li class="Li_ol"><span>其它信息</span></li>
					<li style="width:800px;color:#000000; padding:0 0 10px 0; overflow:hidden;">
					<div><label>$text.get("user.lb.defSys")：</label>
							<select name="defSys" id="defSys" style="width: 100px;" onKeyDown="if(event.keyCode==13) event.keyCode=9">
			#foreach($erow in $globals.getEnumerationItems("MainModule"))
			#if($globals.hasMainModule($erow.value))
						<option value="$erow.value" #if("$erow.value" == "$!result.defSys") selected="selected" #end>$erow.name</option>
			#end
			#end
					      	</select>
					</div>
					</li> -->
					#if("$!result.id" !="1")
					<li  class="Li_ol"><span>
					#if($globals.getSysSetting("sunCompany")=="true")
				$text.get("userManager.lb.ramusorgan")
				#else
				$text.get("userManager.lb.userRole")
				#end</span>
				<span style="float:right;padding-right:30px"><a href="javascript:mdiwin('/RoleQueryAction.do?src=menu','权限分组');">权限分组管理</a></span></li>
					<li >					
					<!--$globals.get($row,0)是分支id,1是分支名，2是分支的classCode-->
				#foreach ($row in $result_sunCompany)
					<div style="width:100%;height:100%;">				
			
					#set ($tempCheck = "")
					#set ($display = "none")
					#set ($roleidValue = "")
					#set ($sunCompanyValue = "")		
					#foreach ($sunRole in $result_role)
						#if ($globals.get($sunRole,1) == $globals.get($row,0))
							#set ($tempCheck = "checked")
							#set ($display = "block")
							#set ($roleidValue = $globals.get($sunRole,2))
							#set ($sunCompanyValue = $globals.get($sunRole,1))
						#end
					#end	
					
					<span style="display:block;color:#000000; padding:0 0 10px 0; overflow:hidden;">
						<input name="roleIds" id="roleIds_$globals.get($row,0)" type="hidden" value="#if($globals.get($row,0) == '1' && $userId == '1' && $roleidValue.indexOf('1;') < 0)1;#end$roleidValue" />
						<input name="sunCompanyIds" id="sunCompanyIds_$globals.get($row,0)" type="hidden" value="$sunCompanyValue"/>
						<input id="sunCompanyCheck_$globals.get($row,0)" type="checkbox" value="$globals.get($row,0)" style="border:0px;margin:0;width:16px;" onKeyDown="if(event.keyCode==13) event.keyCode=9" onClick="showSpan(this,'$globals.get($row,0)')" $tempCheck #if("1" == $result.id && "1" == $globals.get($row,0)) disabled="disabled" #end />
						 $globals.getLocaleDisplay($globals.get($row,1))
						<img src="/$globals.getStylePath()/images/arrowhead.gif" />
						<!--
						#if ($globals.get($row,0) == "1" && $userId == "1")
							<input name="roles_1" type="checkbox" value="1" disabled="disabled" checked >$globals.getLocaleDisplay($!defaultSunCmpName)
						#end-->
					</span>	
					<span id="span_$globals.get($row,0)" style="display:$display;float:left;color:#000000">	
          	<ul class="Company_U">
							#foreach ($role in $all_roles)	
							<!--$globals.get($role,0)是角色id,1是角色名，2是角色的SCompanyID-->
								#if($globals.get($row,2) == $globals.get($role,2))		
									#set ($roleIds = "")
									#foreach ($sun in $result_role)
										#if ($globals.get($sun,1) == $globals.get($row,0))
											#set ($roleIds = $globals.get($sun,2))
										#end
									#end
									#if ($roleIds.indexOf($globals.get($role,0)) > -1)
										#set ($roleIds = "checked")
									#else	
										#set ($roleIds = "")
									#end
									#if ($globals.get($role,0) != "1")	
									<li class="Company_Li">
									<input name="roles_$globals.get($row,0)" id="roles_$globals.get($role,0)" type="checkbox" value="$globals.get($role,0)" onClick='addRoleId(this,"roleIds_$globals.get($row,0)")' onKeyDown="if(event.keyCode==13) event.keyCode=9" style="border:0px;width:16px;" $roleIds />
									<label for="roles_$globals.get($role,0)" style="text-align:left;">$globals.get($role,1)</label>
									</li> 
									
									#end	
								#end						
							#end
             </ul>
						</span>	
					</div>					
				#end
				
			      	
					</li>
					
					<li class="Li_ol"><span>用户权限</span></li>
					<li style="width:800px;color:#000000; padding:0 0 10px 50px; overflow:hidden;">
						<div>
						<label>
						<button class="hBtns" type="button" style="width:120px" onclick="updateModRight('/RoleAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&keyId=$!result.id&roleName=$!result.empFullName&fromUser=true&updatePage=true&winCurIndex=$!winCurIndex','$!result.empFullName')">模块权限设置</button></label>
						</div>
						<div>
						<label>
						<button class="hBtns" type="button" style="width:120px"  onclick="setScope('5','$!result.id','$!globals.encodeHTMLLine($!result.empFullName)')">部门管辖范围</button></label>
							</div>
					<div><label>
						<button class="hBtns" type="button" style="width:120px"  onclick="setScope('1','$!result.id','$!globals.encodeHTMLLine($!result.empFullName)')">职员管辖范围</button></label>
							</div>
					#foreach ($scrow in $scopeCata )
									#set($svc = $velocityCount+11)		
					<div><label>
						<button class="hBtns" type="button" style="width:120px"  onclick="setScope('0','$!result.id','$!globals.encodeHTMLLine($!result.empFullName)','$globals.get($scrow,0)','$globals.get($scrow,3).get($globals.getLocale())管辖范围')">$globals.get($scrow,3).get($globals.getLocale())管辖范围</button></label>
							</div>	
					#end	
					#if($!globals.hasMainModule("1"))	
					<div><label>
						<button class="hBtns" type="button" style="width:120px"  onclick="setScope('4','$!result.id','$!globals.encodeHTMLLine($!result.empFullName)')">单据列控制</button></label>
							</div>
					<div><label>
						<button class="hBtns" type="button" style="width:120px"  onclick="setScope('6','$!result.id','$!globals.encodeHTMLLine($!result.empFullName)')">查看范围值控制</button></label>
							</div>	
					#end					
					</li>#end
				</ul>
				
			</td>
	</tr>
</table>
</div>
</form>
<embed id="s_simnew61"  type="application/npsyunew6-plugin" hidden="true"> </embed><!--创建firefox,chrome等插件-->
</body>
</html>
		
	