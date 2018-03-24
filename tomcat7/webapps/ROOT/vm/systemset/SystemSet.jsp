<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"  />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/tab.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">
function goLocal(type){	
	$("#tagContent > div").css("display","none");
	$("#sd_"+type).css("display","block");
	$("#tags li" ).attr("class","");
	$("#tag_"+type ).attr("class","selectTag");
}

function savep(){
	ischange = false;
	$("div.conDiv[id!='sd_alertSet']").each(function(){
		$(this).find("input[type='checkbox']").each(function(i) {
			cv = $(this).attr("checked")=="checked"?"true":"false";
			if(cv!=$(this).attr("ov")){
				ischange = true;
			}
		});
	});
	$("input[type='text']").each(function(i) {
		if($(this).val()!=$(this).attr("ov") && $(this).attr("ov") != undefined){
			ischange = true;
		}
	});
	$("select").each(function(i) {		
		if($(this).val()!=$(this).attr("ov")){
			ischange = true;
		}
	});
	var check_name = "";
	var f = false;
	$(".alertTypes").each(function(){
		var flag = false;
		var statuss = $(this).parent().parent().find("input[vs=statuss]").is(":checked");
		if(statuss==true){
			$(this).find("input[type='checkbox']").each(function(){
				check_name = $(this).attr("vm");
				if($(this).attr("checked")=="checked"){
					flag = true;
					return false;
				}
			});
		}else{
			flag = true;
		}
		if(!flag){
			f = true;
			return false;
		}
	});
	if(f){
		alert("请选择"+check_name+"提醒方式");
		return false;
	}
	var inputObj = null;
	$(".con_input").each(function(){
		var flag = false;
		$(this).find("input:visible").each(function(){
			if($(this).val()!='' && !gtZero($(this).val())){
				inputObj = $(this);
				flag = true;
			}
		});
		if(flag){
			f = true;
			return false;
		}
	});
	if(f){
		alert("输入必须为整数");
		$(inputObj).focus();
		return false;
	}
	
	$("input[class=ck_box_alert]").each(function(){
		var ischeck = $(this).is(":checked")==true?"0":"1";
		if(ischeck != $(this).attr("ov")){
			ischange = true;
		}
	});
	$("select[class=se_dx_alert]").each(function(){
		if($(this).val() != $(this).attr("ov")){
			ischange = true;
		}
	});
	$(".alertTypes_alert").each(function(){
		$(this).find("input[type='checkbox']").each(function(){
			var ischeck = $(this).is(":checked")==true?$(this).val():undefined;
			if(ischeck != $(this).attr("ov")){
				ischange = true;
			}
		});
	});
	$("input[class=in_txt]").each(function(){
		if($(this).val() != $(this).attr("ov")){
			ischange = true;
		}
	});
	$("input[class=user_input]").each(function(){
		if($(this).val() != $(this).attr("ov")){
			ischange = true;
		}
	});
	if(!ischange){
		alert("您未修改任何配置项目");
		return;
	}

	asyncbox.open({
　　　html : '<div class="asyncbox_confirm"><span></span></div><div style="padding:5px 0 10px 60px;">确定保存吗？<div>',
　　　width : 300,
　　　height : 130,
	 modal　: true,
	 title : '保存',
	 btnsbar: [{text    : '保存',action  : 'okreload' }].concat(jQuery.btn.CANCEL), 
　　　callback : function(action){
		if(action == 'okreload'){
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}	
	　　　　　document.form.submit();
	　　　}
	　　　if(action == 'ok'){
			document.form.reload.value="false";
	　　　　　document.form.submit();
	　　　}
	　　　if(action == 'close'){
	　　　　　
	　　　}
		
　　　}
		
　});	
}
function showRemark(str){
	asyncbox.alert(str,'说明');
}

var curCondition = "";
var curPos = -1;
var curCount = 0;
var selarray = new Array(); //保存查到的所有数据。







function queryCallBack(isPre){ 
	if($("#queryi").val()==""){
		return;
	}
	
	if($("#queryi").val() != curCondition){
		$(".conSelected").removeClass("conSelected");
		$(".curconSelected").removeClass("curconSelected");
		//如果查询条件不一致了，要更新
		curCondition = $("#queryi").val();
		curPos = -1;
		curCount = $(".lt_lb").size();
		
		selarray = new Array(); //保存查到的所有数据。






		$(".lt_lb").each(function(i) {
			if($(this).html().toLowerCase().indexOf($("#queryi").val().toLowerCase())>-1){ 
				selarray[selarray.length] = $(this);
				$(this).addClass("conSelected");
			}
		});
	}	
	
	curexec = false; //一次点击只能执行一次查询	
	if(isPre && curPos == 0){
		curPos=selarray.length -1;		
	}else if(isPre && curPos > -1){
		curPos--;		
	}else if(curPos >= selarray.length){
		curPos=0;
	}else{
		curPos++;
	}
	
	//$(".curconSelected").addClass("conSelected");
	$(".curconSelected").removeClass("curconSelected");	
	selarray[curPos].addClass("curconSelected");
	goLocal(selarray[curPos].parents(".conDiv").attr("id").substr(3));			
	var y,fcount;  
	y=selarray[curPos][0].getBoundingClientRect().top ; 
	fcount = 0;
	if(y<120){
		while(y<120 && fcount<200 && $("#tagContent")[0].scrollTop > 0 ){				
			fcount ++;
			$("#tagContent")[0].scrollTop = $("#tagContent")[0].scrollTop - 20; 
			y=selarray[curPos][0].getBoundingClientRect().top ; 
		}
	}else if(y>parent.document.body.clientHeight -160){			
		while(y>parent.document.body.clientHeight -160 && fcount<200 ){				
			fcount ++;
			$("#tagContent")[0].scrollTop = $("#tagContent")[0].scrollTop + 20; 
			y=selarray[curPos][0].getBoundingClientRect().top ; 
		}
	}

}

function showbewrite(obj){
	if($(obj).attr("checked")=="checked"){
		$(obj).parent().find(".con_input").show();
		$(obj).parent().find(".span_dx").show();
	}else{
		$(obj).parent().find(".con_input").hide();
		$(obj).parent().find(".span_dx").hide();
	}
}

function showalert(alertCode){
	if($("#"+alertCode+"_user_div").is(":hidden")){
		$("#"+alertCode+"_user_div").show();
	}else{
		$("#"+alertCode+"_user_div").hide();
	}
}

var fieldNames;
var fieldNIds;
/* 选择提示用户 */
function deptPopUser(fieldName,fieldNameIds){
	var users = jQuery("#popedomUserIds").val();
	var urls = "/Accredit.do?popname=userGroup&inputType=checkbox&chooseData="+users ;
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({id:'Popdiv',title:'请选择提醒对象',url:urls,width:750,height:430,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var employees = opener.strData;
				newOpenSelect(employees,fieldName,fieldNameIds);
　　　　　	}
　　　	}
　	});
}

function newOpenSelect(employees,fieldName,fieldNameIds){
	var employees = employees.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = getValueById(fieldName).options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++){
				if(existOption[i].value==field[0]){
					talg = true;
				}
			}
			if(!talg){
				getValueById(fieldName).options.add(new Option(field[1],field[0]));
				jQuery("#"+fieldNameIds).val(jQuery("#"+fieldNameIds).val()+field[0]+",");
			}
		}
	}
}

function getValueById(value){
	return document.getElementById(value);
}

//弹出框双击回填数据







function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv');
}

/*下拉列表的批量删除操作*/
function deleteOpation(fileName,popedomId){
	var index = jQuery("#"+fileName+" option:selected").size();
	if(index==0){
		alert("请选择要移除的项!");
		return false;
	}
	jQuery("#"+fileName+" option:selected").remove();
	/*获取指定下拉列表的值，并为指定的参数赋值*/
	var showContent="";
	jQuery("#"+fileName+" option").each(function(){
	   showContent+= this.value+","; 
	});
	jQuery("#"+popedomId).attr("value",showContent);
}

var curRemoteName = "$!globals.getSystemState().remoteName";
function startRemote(){
	//启用远程助手
	var name = $("#remoteAssisName").val();
	if(name == ""){
		alert("请输入标识名称");
		$("#remoteAssisName").focus();
		return;
	}
	if(!isEn(name)){
		alert("标识名称只能是数字或字母");
		$("#remoteAssisName").focus();
		return;
	}
	
	if(name == curRemoteName){
		
		return;
	}
	jQuery.get("/CommonServlet?operation=setRemoteAssis&name="+name,function(data){
		data = data.trim();
		if("OK"==data ){
			alert("执行成功，您的远程访问IP地址为http://vip.krrj.cn/"+name);
			$("#remoteUrl").html("http://vip.krrj.cn/"+name);
			$("#remoteUrl").attr("href","http://vip.krrj.cn/"+name);
			
			curRemoteName = name;
		}else{
			alert(data);
		}
	});
}

function refreshRemote(){
	jQuery.get("/CommonServlet?operation=refreshRemote",function(data){
		data = data.trim();
		if("OK"==data ){
			alert("刷新成功");
		}else{
			alert(data);
		}
	});
}
//微信企业号设置更改

// 微信应用keyName的映射
var keyNameMap = {wxqy:'微信企业号', 'wxwork':'企业微信', 'wx':'微信服务号','wxworkcontact':'企业微信'}
function updateWxSet(keyName){
	var Flag=$("#"+keyName+"_Flag").val();
	var CorpID=$("#"+keyName+"_CorpID").val();
	var Secret=$("#"+keyName+"_Secret").val();
	var AgentId_check = keyName == 'wx' ? '0' : $("#"+keyName+"_AgentId_check").val();
	var RemoteUrl=$("#"+keyName+"_RemoteUrl").val();
	// 以下两项在企业微信里面才会有
	var ContactSecret = keyName == 'wxwork' ? $("#"+keyName+"_ContactSecret").val(): ' ';
	var ContactAgentId_check = keyName == 'wxwork' ? $("#"+keyName+"_ContactAgentId_check").val(): '0';
	var tip="关闭";
	if(CorpID==null||CorpID==""){
		alert("请输入"+ (keyName == 'wx' ? 'AppID': 'CorpID'));
		return;
	}
	if(Secret==null||Secret==""){
		alert("请输入"+ (keyName == 'wx' ? 'AppSecret': 'Secret'));
		return;
	}
	if(AgentId_check==null || AgentId_check=="" || !(/^[0-9]+$/.test(AgentId_check))){
		alert("请输入正确的应用id");
		return;
	}
	if(ContactSecret==null||ContactSecret==""){
		alert("请输入企业微信通讯录Secret");
		return;
	}
	if(ContactAgentId_check==null||ContactAgentId_check=="" || !(/^[0-9]+$/.test(ContactAgentId_check))){
		alert("请输入正确的企业微信通讯录应用id");
		return;
	}
	if(RemoteUrl==null||RemoteUrl==""){
		alert("请输入域名");
		return;
	}
	if(Flag=="true"){
		tip="开启";
	}
	if(confirm("确定要"+tip+keyNameMap[keyName]+"吗？")){
		var obj={
			Flag:Flag,
			CorpID:CorpID,
			Secret:Secret,
			KeyName:keyName,
			ContactSecret:ContactSecret,
			AgentId_check:AgentId_check,
			ContactAgentId_check:ContactAgentId_check,
			RemoteUrl:RemoteUrl
		};
		console.log(obj);
		jQuery.post("/CommonServlet?operation=updateWxSet",obj,function(data){
			alert(tip+keyNameMap[keyName]+"成功");		
		});
	}
	
}
function syncQyh(keyName){
	if(confirm("确定要同步部门和职员到"+keyNameMap[keyName]+"吗？")){
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		jQuery.post("/MobileAjax?op=sync",{KeyName:keyName},
	       function(data){
	       	  top.junblockUI();
	          if(data.result == "ok"){
	          	alert(data.resultMsg);
	          }else{
	        	  if(data.resultMsg && data.resultMsg != ""){
	        		  alert("同步失败"+data.resultMsg);
	        	  }else{
	          		alert("同步失败");
	        	  }
	          }
	       },
	       "json" 
		 ); 
	}
}
</script>
<style type="text/css">
/*主要控制参数*/
.title_font{font-size:16px;font-weight:bold;padding:10px 0 0 20px;display:block;}
.wp_ul {overflow:hidden;min-width:900px;width:100%;}
.wp_ul {margin:2px 0 0 0;padding:8px 0;}
.wp_ul .lt_lb{float:left;white-space: normal;width:300px;line-height:25px;display:inline-block;text-align: right;margin:0px;height:60px; }
.wp_ul .rt_sp{float:left;min-width:550px;width:65%;padding-left:8px;text-align:left;min-height:60px; overflow:hidden;}
.wp_ul .rt_sp input{margin:0;}
.wp_ul .rt_sp .con_input{margin-left:20px;}
.wp_ul .rt_sp .con_input input{width: 30px;}
.wp_ul .rt_sp .ck_box{margin:6px 0 0 0;}
.wp_ul .rt_sp .ck_box_alert{margin:6px 0 0 0;}
.wp_ul .rt_sp .txt_sp{display:block;margin:5px 0 0 0;color:#a1a1a1;}

.hr_link{height:1px;border-top:1px #ebebeb solid;}
.conSelected{color:#FF00CC; }
.curconSelected{ color:red;}

/*用于jquery选择*/
.conDiv{}
.txt_jx{padding-left: 30px;}
.ts_div{overflow: hidden;}
.del_div{padding: 20px 0 0 0}
.del_div img{padding-left: 10px;}
.ts_div select{width:120px;}
.div_name{float: left;}

#queryi{border:1px #ccc solid;border-right:0;width:105px;height:21px;border-radius:3px 0 0 3px;padding:0 5px;float:left;}
.s-span{display:inline-block;float:left;background:#eee;background-image:-webkit-linear-gradient(top,#f9f9f9,#f2f2f2);background-image:linear-gradient(top,#f9f9f9,#f2f2f2);}
.s-span b{width:21px;height:21px;background-image:url(/style/images/item/icons.png);background-repeat:no-repeat;display:inline-block;cursor:pointer;border-top:1px #ccc solid;border-bottom:1px #ccc solid;}
.s-down b{background-position:-294px -213px;border-left:1px #ccc solid;}
.s-up b{background-position:-326px -213px;border-right:1px #ccc solid;}
.savep{float:left;margin:0 0 0 5px;}
.jiaocheng li {
	list-style-type:decimal;
	list-style-position:inside;
}
</style>
</head>

<body onLoad="showtable('tblSort');showStatus(); $('#queryi').focus(); #if("$!page" != "")goLocal('$!page');#end">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/SystemSetAction.do" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')" />
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/> 
<input type="hidden" name="winCurIndex" id="winCurIndex" value="$!winCurIndex"/>
<input type="hidden" name="reload"  value="true"/>
<input type="hidden" name="save"  value="true"/>
<div class="Heading" id="head">
	<div class="HeadingTitle">系统配置</div>
	<div class="HeadingButton" id="theButton" style="display:block; float: right;padding:0 50px 0 0;">		
		<input type="text" name=queryi id="queryi" onkeydown="if(event.keyCode==13){queryCallBack(false);}"  />
		<span class="s-span s-down" onClick="queryCallBack(false)" title="下一条"><b></b></span> 
		<span class="s-span s-up" onClick="queryCallBack(true)" title="上一条"><b></b></span>
		<span class="btn btn-mini savep" onClick="savep()">保存</span>
	</div>
</div>
	<DIV id="con" style="min-width:960px;width:90%;margin:0 auto;margin-top:10px;">
		<UL id="tags" >
			 <LI id="tag_0"   >
			 
			 #foreach($module in $groupOneList)  
					<LI id="tag_$globals.get($module,1)" #if($velocityCount==1) class="selectTag" #end>
						<A onClick="goLocal('$globals.get($module,1)');" style="cursor: pointer;">
							<span style="margin: 0 8px;">$globals.get($module,1)</span></A></LI>
			 #end
			 <LI id="tag_alertSet">
				<A onClick="goLocal('alertSet');" style="cursor: pointer;">
					<span style="margin: 0 8px;">预警配置</span></A></LI>
			<LI id="tag_remoteSet">
				<A onClick="goLocal('remoteSet');" style="cursor: pointer;">
					<span style="margin: 0 8px;">远程助手</span></A></LI>
			<LI id="tag_wxSet">
				<A onClick="goLocal('wxSet');" style="cursor: pointer;">
					<span style="margin: 0 8px;">微信服务号配置</span></A></LI>					
			<LI id="tag_qyhSet">
				<A onClick="goLocal('qyhSet');" style="cursor: pointer;">
					<span style="margin: 0 8px;">微信企业号配置</span></A></LI>
			<LI id="tag_wxworkSet">
				<A onClick="goLocal('wxworkSet');" style="cursor: pointer;">
					<span style="margin: 0 8px;">企业微信配置</span></A></LI>
		</UL>
		<DIV id="tagContent" style="width:99%;overflow:auto;padding:10px 0 0 0;">				
		#foreach($module in $groupOneList) 	 	
		 	<div id="sd_$globals.get($module,1)" class="conDiv" #if($velocityCount!=1) style="display:none;" #end  >
		 	#foreach($modulerow in $!groupList)
		 	#if($globals.get($modulerow,0).startsWith($globals.get($module,0)) && $globals.get($modulerow,0) != $globals.get($module,0))
		 	<font class="title_font">$globals.get($modulerow,1)</font>
		 	<hr style="border:1px solid #00CCFF"/>
		 		#foreach($row in $!groupmap.get($globals.get($modulerow,0)))
		 			<div class="wp_ul">
		 				<label class="lt_lb">$globals.get($row,3)：</label>
			 			<span class="rt_sp">
				 			#if($!NowPeriod==-1 || $globals.get($row,6)!="-1")
				 			<input name="sysDeploy" type="hidden" value="$globals.get($row,4)__$globals.get($row,5)"/>
				 			#end
				 			#if($globals.get($row,8)=="2" )
				 			<input name="$globals.get($row,4)" type="text" style="width:120px" #if($!NowPeriod!=-1 && $globals.get($row,6)=="-1")disabled="true" #end value="$globals.get($row,5)" ov="$globals.get($row,5)"/>
				 			#elseif($globals.get($row,8)=="1" )
				 				<select name="$globals.get($row,4)" #if($!NowPeriod!=-1 && $globals.get($row,6)=="-1")disabled="true" #end ov="$globals.get($row,5)">
				 				#foreach($itemrow in $globals.get($row,9))
				 					<option value="$globals.get($itemrow,1)" #if($globals.get($itemrow,1)==$globals.get($row,5)) selected #end>$globals.get($itemrow,0)</option>
				 				#end
				 				</select>
				 			#else 	
				 			<input class="ck_box" name="$globals.get($row,4)"  #if($!NowPeriod!=-1 && $globals.get($row,6)=="-1")disabled="true" #end type="checkbox" #if($globals.get($row,5)=="true") checked #end value="true" ov="$globals.get($row,5)"/>
				 			#end		 			
				 			<span class="txt_sp">
				 				($globals.encodeHTML($globals.get($row,7)))
				 			</span>
			 			</span>
		 			</div>
		 			<div class="hr_link"></div>
		 		#end
		 		
		 	
		 	#end
		 	#end
		 	</div>
		#end	
		
		<!-- 预警设置 -->
		<div id="sd_alertSet" class="conDiv" style="display:none;">
		#foreach($group in $!alertGroupList)
		 	#if($globals.get($group,2).size()>0)
		 	<font class="title_font">$globals.get($group,1)</font>
		 	<hr style="border:1px solid #00CCFF"/>
		 		#foreach($row in $globals.get($group,2))
		 			<input id="sysAlert" name="sysAlert" value="$globals.get($row,0)" type="hidden"/>
				 	<input type="hidden" id="$globals.get($row,0)_oldStatus" name="$globals.get($row,0)_oldStatus" value="$!globals.get($row,2)" />
				 	<input type="hidden" id="$globals.get($row,0)_oldAlertType" name="$globals.get($row,0)_oldAlertType" value="$!globals.get($row,8)"/>
				 	<input type="hidden" id="$globals.get($row,0)_oldActionTime" name="$globals.get($row,0)_oldActionTime" value="$!globals.get($row,6)"/>
				 	<input type="hidden" id="$globals.get($row,0)_oldActionFrequency" name="$globals.get($row,0)_oldActionFrequency" value="$!globals.get($row,7)"/>
				 	<input type="hidden" id="$globals.get($row,0)_oldPopedomUserIds" name="$globals.get($row,0)_oldPopedomUserIds" value="$!globals.get($row,15)"/>
				 	
		 			<div class="wp_ul">
		 				<label class="lt_lb">$globals.get($row,1)：</label>
		 				<input type="hidden" id="$globals.get($row,0)_name" name="$globals.get($row,0)_name" value="$globals.get($row,1)" />
			 			<span class="rt_sp">
				 			<input class="ck_box_alert" vs="statuss" id="$globals.get($row,0)_status" name="$globals.get($row,0)_status" type="checkbox" #if($globals.get($row,2)=="0") ov="0" checked #else ov="$globals.get($row,2)" #end value="0" onclick="showbewrite(this)"/>
				 			#if("$globals.get($row,14)"=="1")
				 				<input class="in_txt" id="$globals.get($row,0)_condition" name="$globals.get($row,0)_condition" value="$!globals.get($row,5)" ov="$!globals.get($row,5)" type="hidden"/>
				 			#else
				 				<span class="con_input" #if($globals.get($row,2)!="0") style="display: none;" #end>
				 				$!globals.get($!globals.strSplit($!globals.get($row,13),'input'),0)
				 				<input class="in_txt" id="$globals.get($row,0)_condition" name="$globals.get($row,0)_condition" value="$!globals.get($!globals.stringToArray($!globals.get($row,5),'='),1)" ov="$!globals.get($!globals.stringToArray($!globals.get($row,5),'='),1)"/>$!globals.get($!globals.strSplit($!globals.get($row,13),'input'),1)</span>
				 			#end
				 			
				 			<span class="txt_sp">
				 				#if($globals.get($row,11)!="")
				 					($globals.encodeHTML($globals.get($row,11)))
				 				#end
				 			</span>
		 					<span class="span_dx" #if($globals.get($row,2)!="0") style="display: none;" #end><span class="alertTypes_alert">
		 					#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
					 			<input type="checkbox" id="$globals.get($row,0)_alertType" name="$globals.get($row,0)_alertType" #foreach($t in $globals.strSplit($globals.get($row,8),',')) #if($row_wakeUpType.value==$t) checked="checked" ov="$t" #end #end value="$row_wakeUpType.value" vm="$globals.get($row,1)"/>$row_wakeUpType.name
							#end</span>
							<span class="txt_jx">
							<select class="se_dx_alert" name="$globals.get($row,0)_actionTime" id="$globals.get($row,0)_actionTime" ov="$globals.get($row,6)">
							#foreach($foo in [0..23])
								#if($foo<10)
									<option value="0$foo" #if("$globals.get($row,6)"=="0$foo") selected #end>0$foo</option>
								#else
									<option value="$foo" #if("$globals.get($row,6)"=="$foo") selected #end>$foo</option>
								#end
							#end
							</select>点</span>
							<span class="txt_jx">
							每天
							<select class="se_dx" name="$globals.get($row,0)_actionFrequency" id="$globals.get($row,0)_actionFrequency" ov="$globals.get($row,7)">
								<option value="1" #if("$globals.get($row,7)"=="1") selected #end>1</option>
								<option value="2" #if("$globals.get($row,7)"=="2") selected #end>2</option>
								<option value="3" #if("$globals.get($row,7)"=="3") selected #end>3</option>
								<option value="4" #if("$globals.get($row,7)"=="4") selected #end>4</option>
								<option value="5" #if("$globals.get($row,7)"=="5") selected #end>5</option>
							</select>次</span>
							<span class="txt_jx"><a href="javascript:void(0)" onclick="showalert('$globals.get($row,0)')" title="提醒对象设置">提醒对象</a></span>
								<input class="user_input" type="hidden" name="$globals.get($row,0)_popedomUserIds" id="$globals.get($row,0)_popedomUserIds" value="$!globals.get($row,15)" ov="$!globals.get($row,15)"/>
					 			<div id="$globals.get($row,0)_user_div" style="display: none;">
									<div class="div_name" id="div_title"></div>
									<div class="ts_div" >
										<div id="_context">
											<div style="float:left;display:inline-block;">
												<div>
													<img src="$globals.getStylePath()/images/St.gif" onclick="deptPopUser('$globals.get($row,0)_formatUserName','$globals.get($row,0)_popedomUserIds');" alt="$text.get("oa.common.advicePerson")" class="search" style="cursor: pointer;"/>
													<a href="javascript:void(0)" title="选择用户" onclick="deptPopUser('$globals.get($row,0)_formatUserName','$globals.get($row,0)_popedomUserIds');">选择用户</a>
												</div>
												<select name="$globals.get($row,0)_formatUserName" id="$globals.get($row,0)_formatUserName" multiple="multiple">
													#foreach($!det in $globals.strSplit($globals.get($row,15),','))
														<option value="$!det">$!globals.getEmpFullNameByUserId($!det)</option>
													#end
												</select>
											</div>
											<div class="del_div" style="float:left;display:inline-block;">
												<img onclick="deleteOpation('$globals.get($row,0)_formatUserName','$globals.get($row,0)_popedomUserIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search deloption" />
											</div>
										</div>
									</div>
								</div>
				 			</span>
				 			</span>
		 			</div>
		 			<div class="hr_link"></div>
		 		#end
		 		#end
		 	#end

	
		</div>
		<!-- 远程助手 -->
		<div id="sd_remoteSet" class="conDiv" style="display:none;">
			<font class="title_font">远程助手！可以让您通过统一域名
			<a id="remoteUrl" style="color:red;margin:0 10px" href="http://vip.krrj.cn/$!globals.getSystemState().remoteName" target="_blank">http://vip.krrj.cn/$!globals.getSystemState().remoteName</a> 远程访问本系统</font>
		 	<div class="wp_ul">
	 			<span class="rt_sp" >
		 			<span class="txt_sp">
		 				&nbsp;&nbsp;&nbsp;&nbsp;(没有固定IP地址，没有域名,离开办公室如何操作本系统？远程助手来帮您)
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>
		 	#if("$!globals.getSystemState().remoteName"=="")
		 	<font class="title_font">第一步：设置网络环境</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px"></label>
	 			<span class="rt_sp">
		 			<a href='http://www.krrj.cn/info/learn/882.html' target="blank">点击查看设置方法</a>
		 			<span class="txt_sp">
		 				(设置路由器、防火墙、杀毒软件等使得可以远程访问本系统)
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>
		 	
		 	<font class="title_font">第二步：注册并启动远程助手</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入标识名称：</label>
	 			<span class="rt_sp">
		 			<input name="remoteAssisName" id="remoteAssisName" type="text" style="width:120px" value="" disableautocomplete="true" autocomplete="off">
		 			<span onclick="startRemote();" class="regbut btn btn-small btn-danger">启用</span>
		 			<span class="txt_sp">
		 				(只能是数字或英文字母，此标识将做为您访问本系统的唯一身份标识)
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>
		 	#else
		 	<font class="title_font">您已开通远程助手，本系统将每半小时更新一次您的最新动态外网IP</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px"></label>
	 			<span class="rt_sp">
		 			如果您不能正常远程访问，请执行手动刷新


		 			<span onclick="refreshRemote();" class="regbut btn btn-small btn-danger">立即刷新</span>
		 			<span class="txt_sp">
		 				(不能远程访问可能是因为您的IP发生变动。手动刷新可以及时更新IP地址，请刷新后重新输入http://vip.krrj.cn/$!globals.getSystemState().remoteName)
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>
		 	
		 	<font class="title_font">如果不能正常远程访问，请检查网络环境设置</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px"></label>
	 			<span class="rt_sp">
		 			<a href='http://www.krrj.cn/info/learn/882.html' target="blank">点击查看设置方法</a>
		 			<span class="txt_sp">
		 				(设置路由器、防火墙、杀毒软件等使得可以远程访问本系统)
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>
		 	
		 	<font class="title_font">如果您不喜欢这个标识名称，可重新换一个</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">我要换标识：</label>
	 			<span class="rt_sp">
		 			<input name="remoteAssisName" id="remoteAssisName" type="text" style="width:120px" value="$!globals.getSystemState().remoteName" disableautocomplete="true" autocomplete="off">
		 			<span onclick="startRemote();" class="regbut btn btn-small btn-danger">修改</span>
		 			<span class="txt_sp">
		 				(只能是数字或英文字母，此标识将做为您访问本系统的唯一身份标识)
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>
		 	#end
		</div>
		<!-- 微信服务号 -->
 		<div id="sd_wxSet" class="conDiv" style="display:none;">
			<font class="title_font">微信服务号设置</font>
		 	<div class="wp_ul">
	 			<span class="rt_sp" >
		 			<span class="txt_sp">
		 				&nbsp;&nbsp;&nbsp;&nbsp;(配置微信服务号</a>后您就可以进行远程办公，包括工作流审核，消息通知等)
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>
		 	<font class="title_font">第一步：配置服务号</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
	 			<ol class="jiaocheng">
	 				<li><a href='https://mp.weixin.qq.com' target="blank">登陆微信公众平台</a>;</li>
	 				<li>进入自定义菜单编辑(功能-->自定义菜单);</li>
	 				<li>
	 					添加菜单，菜单内容设置为跳转网页，页面地址设置为：AIO系统的访问地址/mobilevue<span style="color:red">?state=wx</span><br/>
	 					(比如您的AIO系统的访问地址为http://krrj.cn，那页面地址需设置为：http://krrj.cn/mobilevue<span style="color:red">?state=wx</span>);
	 				</li>		 	
	 				<li>
	 					保存并发布
	 				</li>
	 				<li>
	 					进入公众号功能设置(设置-->公众号设置-->功能设置)
	 				</li>			
	 				<li>
	 					设置网页授权域名，设置为：AIO系统的访问地址/mobilevue，确认通过即可
	 				</li>
	 				<li>
	 					设置JS接口安全域名，设置为：AIO系统的访问地址/mobilevue，确认通过即可
	 				</li>
	 			</ol>
 			</div>		 		
		 	<div class="hr_link"></div>	
		 	<font class="title_font">第二步：配置参数</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">当前状态：</label>
	 			<span class="rt_sp">
		 			<select name="wx_Flag" id="wx_Flag" type="text" style="width:150px" disableautocomplete="true" autocomplete="off">
		 				<option value="true">开启</option>
		 				<option value="false">关闭</option>		 				
		 			</select>
		 			<span class="txt_sp">
		 				(是否开启微信服务号)
		 			</span>
	 			</span>
 			</div>
 			<script>
 			var wx_Flag="$!wxset.wx.Flag";
 			if(wx_Flag==""){
 				wx_Flag="false";
 			}
 			$("#wx_Flag").val(wx_Flag);
 			</script>
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入AppID：</label>
	 			<span class="rt_sp">
		 			<input name="wx_CorpID" id="wx_CorpID" type="text" style="width:150px" value="$!wxset.wx.CorpID" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(AppID是微信服务号的应用ID。AppID查看：登入微信公众平台在开发-->基本配置-->应用ID)
		 			</span>
	 			</span>
 			</div>	
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入AppSecret：</label>
	 			<span class="rt_sp">
		 			<input name="wx_Secret" id="wx_Secret" type="text" style="width:500px" value="$!wxset.wx.Secret" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(AppSecret是微信服务号的密钥。AppSecret查看：登入微信公众平台在开发-->基本配置-->secret)
		 			</span>
	 			</span>
 			</div>	
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入域名：</label>
	 			<span class="rt_sp">
		 			<input name="wx_RemoteUrl" id="wx_RemoteUrl" type="text" style="width:150px" value="$!wxset.wx.RemoteUrl" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(域名是本AIO系统的访问地址(<span style="color:red">以/结束</span>))
		 			</span>
	 			</span>
 			</div>
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px"></label>
	 			<span class="rt_sp">
		 			<span onclick="updateWxSet('wx')" class="regbut btn btn-small btn-danger">保存</span>
	 			</span>
 			</div>		
 			<div class="hr_link"></div>			 		
 		</div>			
		<!-- 企业号 -->
		<div id="sd_qyhSet" class="conDiv" style="display:none;">
			<font class="title_font">微信企业号设置</font>
		 	<div class="wp_ul">
	 			<span class="rt_sp" >
		 			<span class="txt_sp">
		 				&nbsp;&nbsp;&nbsp;&nbsp;(关注微信就能在您的微信上进行远程办公，包括工作流审核，消息通知等)
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>
		 	<font class="title_font">第一步：配置企业号</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
	 			<ol class="jiaocheng">
	 				<li><a href='https://work.weixin.qq.com' target="blank">登陆微信企业号</a>;</li>
	 				<li>添加应用工作流审核(应用中心)，应用类型选择消息型应用，设置可信域名为AIO系统的访问地址，开启回调模式</li>
	 				<li>
	 					开启自定义菜单，添加菜单，页面地址设置为：AIO系统的访问地址/mobilevue<span style="color:red">?state=wxqy</span><br/>
	 					(比如您的AIO系统的访问地址为http://krrj.cn，那页面地址需设置为：http://krrj.cn/mobilevue<span style="color:red">?state=wxqy</span>);
	 				</li>		 	
	 			</ol>
 			</div>			 		
		 	<div class="hr_link"></div>	
		 	<font class="title_font">第二步：配置参数</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">当前状态：</label>
	 			<span class="rt_sp">
		 			<select name="wxqy_Flag" id="wxqy_Flag" type="text" style="width:150px" disableautocomplete="true" autocomplete="off">
		 				<option value="true">开启</option>
		 				<option value="false">关闭</option>		 				
		 			</select>
		 			<span class="txt_sp">
		 				(是否开启微信企业号)
		 			</span>
	 			</span>
 			</div>
 			<script>
 			var wxqy_Flag="$!wxset.wxqy.Flag";
 			if(wxqy_Flag==""){
 				wxqy_Flag="false";
 			}
 			$("#wxqy_Flag").val(wxqy_Flag);
 			</script>
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入CorpID：</label>
	 			<span class="rt_sp">
		 			<input name="wxqy_CorpID" id="wxqy_CorpID" type="text" style="width:150px" value="$!wxset.wxqy.CorpID" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(CorpID是企业号的id,登入微信企业号管理后台在设置-->帐号信息处查看)
		 			</span>
	 			</span>
 			</div>	
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入Secret：</label>
	 			<span class="rt_sp">
		 			<input name="wxqy_Secret" id="wxqy_Secret" type="text" style="width:500px" value="$!wxset.wxqy.Secret" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(Secret是企业号的开发者凭据，请使用内部管理员的身份进入微信企业号管理后台在设置-->权限管理-->开发者凭据处查看)
		 			</span>
	 			</span>
 			</div>	
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入应用ID：</label>
	 			<span class="rt_sp">
		 			<input name="wxqy_AgentId_check" id="wxqy_AgentId_check" type="text" style="width:150px" value="$!wxset.wxqy.AgentId_check" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(先在微信企业号管理后台建立应用“工作流审核”，应用ID是对应的代号在应用中心-->工作流审批-->用户ID处查看)
		 			</span>
	 			</span>
 			</div>	
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入域名：</label>
	 			<span class="rt_sp">
		 			<input name="wxqy_RemoteUrl" id="wxqy_RemoteUrl" type="text" style="width:150px" value="$!wxset.wxqy.RemoteUrl" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(域名是本AIO系统的访问地址(<span style="color:red">以/结束</span>))
		 			</span>
	 			</span>
 			</div>
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px"></label>
	 			<span class="rt_sp">
		 			<span onclick="updateWxSet('wxqy')" class="regbut btn btn-small btn-danger">保存</span>
	 			</span>
 			</div>		
 			<div class="hr_link"></div>			 		
			<font class="title_font">第三步：同步职员到微信企业号</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px"></label>
	 			<span class="rt_sp">
		 			<span onclick="syncQyh('wxqy')" class="regbut btn btn-small btn-danger">开始同步</span>
	 			</span>
 			</div>
 		</div>
 		<div class="hr_link"></div>
 		<div id="sd_wxworkSet" class="conDiv" style="display:none;">
			<font class="title_font">企业微信设置</font>
		 	<div class="wp_ul">
	 			<span class="rt_sp" >
		 			<span class="txt_sp">
		 				&nbsp;&nbsp;&nbsp;&nbsp;(<a href='https://work.weixin.qq.com' target="blank">安装企业微信</a>后您就可以进行远程办公，包括工作流审核，消息通知等)
		 			</span>
	 			</span>
 			</div>		 		
		 	<div class="hr_link"></div>
		 	<font class="title_font">第一步：配置企业微信</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
	 			<ol class="jiaocheng">
	 				<li><a href='https://work.weixin.qq.com' target="blank">登陆企业微信</a>;</li>
	 				<li>添加应用工作流审核(企业应用)，选择应用可见范围，设置应用类型为消息型应用，设置可信域名为AIO系统的访问地址</li>
	 				<li>
	 					修改自定义菜单，添加菜单，菜单内容设置为跳转网页，页面地址设置为：AIO系统的访问地址/mobilevue<span style="color:red">?state=wxwork</span><br/>
	 					(比如您的AIO系统的访问地址为http://krrj.cn，那页面地址需设置为：http://krrj.cn/mobilevue<span style="color:red">?state=wxwork</span>);
	 				</li>		 	
	 			</ol>
 			</div>			 		 		
		 	<div class="hr_link"></div>	
		 	<font class="title_font">第二步：配置参数</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">当前状态：</label>
	 			<span class="rt_sp">
		 			<select name="wxwork_Flag" id="wxwork_Flag" type="text" style="width:150px" disableautocomplete="true" autocomplete="off">
		 				<option value="true">开启</option>
		 				<option value="false">关闭</option>		 				
		 			</select>
		 			<span class="txt_sp">
		 				(是否开启企业微信)
		 			</span>
	 			</span>
 			</div>
 			<script>
 			var wxwork_Flag="$!wxset.wxwork.Flag";
 			if(wxwork_Flag==""){
 				wxwork_Flag="false";
 			}
 			$("#wxwork_Flag").val(wxwork_Flag);
 			</script>
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入CorpID：</label>
	 			<span class="rt_sp">
		 			<input name="wxwork_CorpID" id="wxwork_CorpID" type="text" style="width:150px" value="$!wxset.wxwork.CorpID" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(CorpID是企业号的id,登入企业微信管理台在我的企业-->企业信息)
		 			</span>
	 			</span>
 			</div>	
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入Secret：</label>
	 			<span class="rt_sp">
		 			<input name="wxwork_Secret" id="wxwork_Secret" type="text" style="width:500px" value="$!wxset.wxwork.Secret" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(先在企业微信中建立应用“工作流审核”。Secret查看：企业应用-->工作流审核-->secret)
		 			</span>
	 			</span>
 			</div>	
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入应用ID：</label>
	 			<span class="rt_sp">
		 			<input name="wxwork_AgentId_check" id="wxwork_AgentId_check" type="text" style="width:150px" value="$!wxset.wxwork.AgentId_check" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(先在企业微信中建立应用“工作流审核”，应用ID查看：企业应用-->工作流审核-->应用ID)
		 			</span>
	 			</span>
 			</div>
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入通讯录Secret：</label>
	 			<span class="rt_sp">
		 			<input name="wxwork_ContactSecret" id="wxwork_ContactSecret" type="text" style="width:500px" value="$!wxset.wxworkcontact.Secret" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(通讯录Secret查看：管理工具-->通讯录-->secret)
		 			</span>
	 			</span>
 			</div>	
		 	<input name="wxwork_ContactAgentId_check" id="wxwork_ContactAgentId_check" type="text" style="display:none" value="0" disableautocomplete="true" autocomplete="off">
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px">请输入域名：</label>
	 			<span class="rt_sp">
		 			<input name="wxwork_RemoteUrl" id="wxwork_RemoteUrl" type="text" style="width:150px" value="$!wxset.wxwork.RemoteUrl" disableautocomplete="true" autocomplete="off">
		 			<span class="txt_sp">
		 				(域名是本AIO系统的访问地址(<span style="color:red">以/结束</span>))
		 			</span>
	 			</span>
 			</div>
 			<div class="wp_ul">
 				<label class="lt_lb" style="height:40px"></label>
	 			<span class="rt_sp">
		 			<span onclick="updateWxSet('wxwork')" class="regbut btn btn-small btn-danger">保存</span>
	 			</span>
 			</div>		
 			<div class="hr_link"></div>			 		
			<font class="title_font">第三步：同步职员到企业微信</font>
		 	<hr style="border:1px solid #00CCFF">
		 	<div class="wp_ul">
 				<label class="lt_lb" style="height:40px"></label>
	 			<span class="rt_sp">
		 			<span onclick="syncQyh('wxworkcontact')" class="regbut btn btn-small btn-danger">开始同步</span>
	 			</span>
 			</div>
 		</div>	
		<div class="hr_link"></div>		
		<script type="text/javascript">
			$("#tagContent").height($(window).height()-70);
			var sHeight = $(window).height()-70;	
		</script>
	</div>
</form>
</body>
</html>