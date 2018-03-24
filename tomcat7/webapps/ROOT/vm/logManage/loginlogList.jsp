<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>日志管理</title>
<link rel="stylesheet" href="/style/css/base_button.css" type="text/css" />
<link rel="stylesheet" href="/style/css/log.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/sharingStyle.css" type="text/css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/date.vjs"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>

<script type="text/javascript">
	/* 时间选择*/
	function openInputDate(obj){
		WdatePicker({lang:'zh_CN'});
	}
	
	/* 搜索 */
	function searchData(){
		var searchUserName = $("#searchUserName").val();
		if(searchUserName==""){
			$("#searchUserId").val('');
		}
		form.submit();
	}
	
	var fieldNames;
	var fieldNIds;
	/* 职员弹出框 */
	function selectUsers(popname,fieldName,fieldNameIds){
		var urls = "/Accredit.do?popname=" + popname + "&inputType=radio";
		fieldNames=fieldName;
		fieldNIds=fieldNameIds;
		asyncbox.open({
			id : 'Popdiv',
		 	title : '请选择职员',
		　　　	url : urls,
		　　　	width : 750,
		　　　	height : 430,
			btnsbar : [{
		     text    : '清空',
		      action  : 'clear'
		  	}].concat(jQuery.btn.OKCANCEL),
			callback : function(action,opener){
	　　　　　	if(action == 'ok'){
				var str = opener.strData;
				newOpenSelect(str,fieldName,fieldNameIds)
	　　　　　	}else if(action == 'clear'){
				//清空
				clearPop();
			}
	　　　	}
	　	});
	}
	
	function getValueById(id){
		return document.getElementById(id);
	}
	
	
	/* 清空弹出框数据 */
	function clearPop(){
		getValueById(fieldNames).value = '';
		getValueById(fieldNIds).value = '';
	}
	
	function newOpenSelect(str,fieldName,fieldNameIds){
		var employees = str.split("|") ;
		for(var j=0;j<employees.length;j++){
			var field = employees[j].split(";") ;
			if(field!=""){
				var existOption = getValueById(fieldName);
				getValueById(fieldName).value = field[1];
				getValueById(fieldNameIds).value = field[0];
			}
		}
	}
	
	function fillData(datas){
		newOpenSelect(datas,fieldNames,fieldNIds,1);
		jQuery.close('Popdiv');
	}
	
	/* 删除30天日志 */
	function deleteLog(){
		asyncbox.confirm('你确定删除30天前的登陆日志?','提示',function(action){
		　　　if(action == 'ok'){
			　　　	$("#operation").val('3');
				$("#opType").val('deleteLoginLog');
				form.submit();
		　　　}
		　});
	}
	
	$(function(){
		$(".moveLog").scroll(function(){
			var topNum = $(this).scrollTop();
			$(".t_u").addClass("po_u").css("top","40px").width($("ul.c1").width());
			if(topNum == "0"){
				$(".t_u").removeClass("po_u");
			}
		})
		$(".search").find("input").keydown(function(event){
			if(event.keyCode == 13){
				searchData();
			}
		});
		$(".moveLog").find("ul").not(".t_u").mouseover(function(){
			$(this).addClass("bg_color");
		});
		$(".moveLog").find("ul").not(".t_u").mouseout(function(){
			$(this).removeClass("bg_color");
		});
		$(".moveLog").find("ul").not(".t_u").click(function(){
			$(".moveLog").find("ul").not(".t_u").removeClass("ul_client");
			$(this).addClass("ul_client");
		});
	})
//下拉弹出窗功能结束
var hideFieldName;//存放fieldName值
var hideSelectName;//存放selectName值
function openSelect(selectName,fieldName,display){
	if(typeof(window.parent.$("#bottomFrame").attr("id"))!="undefined"){
		asyncbox = parent.asyncbox;
	}
	hideFieldName = fieldName;
	hideSelectName = selectName;
	
	var urlstr = '/UserFunctionAction.do?operation=22&src=menu&moduleType=&displayName='+encodeURI(display)+'&LinkType=@URL:&tableName=$!tableName&selectName='+selectName ;
	
	if(urlstr.indexOf("#")!=-1){
		urlstr=urlstr.replaceAll("#","%23") ;
    }
    if(urlstr.indexOf("+")!=-1){
		urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
    }
	 
	urlstr += "&MOID=$!MOID&MOOP=query&popupWin=Popdiv";
	
	asyncbox.open({id:'Popdiv',title:display,url:urlstr,width:750,height:470});
}
function exePopdiv(returnValue){
	document.getElementById(hideFieldName).value= returnValue;
}
</script>
<style type="text/css">
	.moveLog{margin: 20px 5px 0px 5px;overflow:auto;position: relative;}
	.moveLog>.t_u{background:#d1d1d1;width:100%;}
	.moveLog>.po_u{position:fixed;top:0;left:5px;}
	.moveLog>ul{overflow:hidden;}
	.moveLog>ul li{float:left;display:inline-block;height:31px;line-height:31px;text-align:center;}
	.w{width:24%}
	.w40{width:4%}
	.w60{width:6%}
	.w100{width:10%}
	.w120{width:12%}
	.w160{width:16%}
	.bg_color{background-color: #FFFFCC}
	.ul_client{background-color: #CCCC99}
</style>

</head>
<body>
	<iframe name="formFrame" style="display:none"></iframe>
	<form action="/LogManageQueryAction.do" name="form" method="post" id="form" target="">
	<input type="hidden" id="operation" name="operation" value="4"/>
	<input type="hidden" id="opType" name="opType" value="loginLog"/>
  	<div class="search">
    	<div class="items">
	      	<i class="red_i">日期：</i>
	        <input class="inp_txt" type="text" id="beginTimeSearch" name="beginTimeSearch" onclick="openInputDate(this);" value="$!LoginlogSearchForm.beginTimeSearch"/>
	        <i class="red_i">-</i>
	        <input class="inp_txt" type="text" id="endTimeSearch" name="endTimeSearch" onclick="openInputDate(this);" value="$!LoginlogSearchForm.endTimeSearch"/>
	    </div>
		<div class="items">
			<i>登录人：</i>
		  <span class="h_icon">
		  	<input type="hidden" id="searchUserId" name="searchUserId" value="$!LoginlogSearchForm.searchUserId"/>
		  	<input class="inp_staff" type="text" id="searchUserName" name="searchUserName" value="$!LoginlogSearchForm.searchUserName" ondblclick="selectUsers('userGroup','searchUserName','searchUserId')" style="border-right:0px;"/>
		  	<a href="#" onclick="selectUsers('userGroup','searchUserName','searchUserId')" style="border:1px solid #bbb;height:19px;width:20px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%"></a>
		  </span>
		</div>
		<div class="items">
			<i>部门：</i>
		  <span class="h_icon">
		  	<input class="inp_staff" type="text" id="searchDeptName" name="searchDeptName" value="$!LoginlogSearchForm.searchDeptName" ondblclick="openSelect('ReportSelectDepartment','searchDeptName','部门')" style="border-right:0px;"/>
		  	<a href="#" onclick="openSelect('ReportSelectDepartment','searchDeptName','部门')" style="border:1px solid #bbb;height:19px;width:20px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%"></a>
		  </span>
		</div>
      <div class="items">
      	<i>操作：</i>
        <select class="slt" id="searchOperation" name="searchOperation">
        	<option #if("$!LoginlogSearchForm.searchOperation"=="") selected #end></option>
        	<option value="LOGIN" #if("$!LoginlogSearchForm.searchOperation"=="LOGIN") selected #end>登入</option>
        	<option value="LOGOUT" #if("$!LoginlogSearchForm.searchOperation"=="LOGOUT") selected #end>退出</option>
        </select>
      </div>
      <div class="items">
      	<i>终端：</i>
        <input class="slt" id="searchTerminal" name="searchTerminal" value="$!LoginlogSearchForm.searchTerminal"/>
      </div>
      <div class="items" style="margin:0 0 0 30px;">
      	<div class="btn btn-mini" onclick="searchData()">查询</div>
      	#if($LoginBean.operationMap.get("/LogManageAction.do").delete())
        <div class="btn btn-inverse btn-mini" onclick="deleteLog()">删除30天前登陆日志</div>
        #end
      </div>
    </div>
    <div class="moveLog" id="div_move">
    	<script type="text/javascript" > 
			var oDiv=document.getElementById("div_move");
			var sHeight=document.documentElement.clientHeight-90;
			oDiv.style.height=sHeight+"px";
		</script>
    	<ul class="t_u">
    		<li class="w40">No.</li>
    		<li class="w100">$!text.get("hr.review.employee.name")</li>
    		<li class="w100">部门</li>
    		<li class="w160">登录时间</li>
    		<li class="w120">IP地址</li>
    		<li class="w120">终端</li>
    		<li class="w60">操作</li>
    		<li class="w60">结果</li>
    		<li class="w">备注</li>
    	</ul>
    	#set($count = 1)
		#foreach($!log in $!loginlogList)
		<ul #if($globals.isOddNumber($velocityCount)) class="c1" #else class="c2" #end>
			<li class="w40">$count</li>
			<li class="w100" title="$!log.userName">$globals.subTitle($!log.userName,20)</li>
    		<li class="w100">$!globals.getDeptByUserId("$!log.userid")</li>
    		<li class="w160">$!log.createTime</li>
    		<li class="w120">$!log.ip</li>
    		<li class="w120">$!log.terminal</li>
    		<li class="w60">#if("$!log.operation"=="LOGIN")登入#elseif("$!log.operation"=="LOGOUT")退出#end</li>
    		<li class="w60">#if("$!log.opResult"=="SUCCESS")成功#elseif("$!log.opResult"=="FAIL")失败#end</li>
    		<li class="w" title="$!log.reason">$globals.subTitle($!log.reason,40)</li>
		</ul>
		#set($count = $count + 1)
		#end
		#if($loginlogList.size()==0)
			<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">暂无登陆日志信息</div>
		#end
	</div>
	<div class="listRange_pagebar">
		$!pageBar
	</div>	
    </form>
</body>
</html>
