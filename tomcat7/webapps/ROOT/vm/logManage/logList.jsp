<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>日志管理</title>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" rel="stylesheet" href="/style/css/log.css" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/sharingStyle.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/date.vjs"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
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
		asyncbox.confirm('你确定删除30天前的操作日志?','提示',function(action){
		　　　if(action == 'ok'){
			　　　	$("#operation").val('3');
				$("#opType").val('deleteLog');
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
</script>
<style type="text/css">
.moveLog{margin:0 5px 0px 5px;overflow:auto;position: relative;}
.moveLog>.t_u{background:#d1d1d1;width:100%;}
.moveLog>.po_u{position:fixed;top:0;left:5px;}
.moveLog>ul{overflow:hidden;}
.moveLog>ul li{float:left;display:inline-block;height:31px;line-height:31px;text-align:center;}
.w{width:45%;}
.w40{width:4%}
.w100{width:10%}
.w120{width:12%}
.w130{width:13%}
.w160{width:16%}
.bg_color{background-color: #FFFFCC}
.ul_client{background-color: #CCCC99}
</style>

</head>
<body>
	<iframe name="formFrame" style="display:none"></iframe>
	<form action="/LogManageAction.do" name="form" method="post" id="form" target="">
	<input type="hidden" id="operation" name="operation" value="4"/>
	<input type="hidden" id="opType" name="opType" value="operateLog"/>
  	<div class="search">
    	<div class="items">
	      	<i class="red_i">日期：</i>
	        <input class="inp_txt" type="text" id="beginTimeSearch" name="beginTimeSearch" onclick="openInputDate(this);" value="$!LogSearchForm.beginTimeSearch"/>
	        <i class="red_i">-</i>
	        <input class="inp_txt" type="text" id="endTimeSearch" name="endTimeSearch" onclick="openInputDate(this);" value="$!LogSearchForm.endTimeSearch"/>
	    </div>
		<div class="items">
			<i>职员：</i>
		  <span class="h_icon">
		  	<input type="hidden" id="searchUserId" name="searchUserId" value="$!LogSearchForm.searchUserId"/>
		  	<input class="inp_staff" type="text" id="searchUserName" name="searchUserName" value="$!LogSearchForm.searchUserName" ondblclick="selectUsers('userGroup','searchUserName','searchUserId')" style="border-right:0px;"/>
		  	<a href="#" onclick="selectUsers('userGroup','searchUserName','searchUserId')" style="border:1px solid #bbb;height:19px;width:20px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%"></a>
		  </span>
		</div>
      <div class="items">
      	<i>操作：</i>
        <select class="slt" id="searchOperation" name="searchOperation">
        	<option #if("$!LogSearchForm.searchOperation"=="") selected #end></option>
        	<option value="0" #if("$!LogSearchForm.searchOperation"=="0") selected #end>添加</option>
        	<option value="1" #if("$!LogSearchForm.searchOperation"=="1") selected #end>修改</option>
        	<option value="2" #if("$!LogSearchForm.searchOperation"=="2") selected #end>删除</option>
        	<option value="3" #if("$!LogSearchForm.searchOperation"=="3") selected #end>转交</option>
        	<option value="4" #if("$!LogSearchForm.searchOperation"=="4") selected #end>暂存</option>
        	<option value="5" #if("$!LogSearchForm.searchOperation"=="5") selected #end>草稿添加</option>
        	<option value="6" #if("$!LogSearchForm.searchOperation"=="6") selected #end>草稿修改</option>
        	<option value="7" #if("$!LogSearchForm.searchOperation"=="7") selected #end>草稿删除</option>
        	<option value="8" #if("$!LogSearchForm.searchOperation"=="8") selected #end>草稿审核</option>
        	<option value="9" #if("$!LogSearchForm.searchOperation"=="9") selected #end>列配置</option>
        	<option value="10" #if("$!LogSearchForm.searchOperation"=="10") selected #end>导入</option>
        	<option value="11" #if("$!LogSearchForm.searchOperation"=="11") selected #end>零售上传</option>
        	<option value="12" #if("$!LogSearchForm.searchOperation"=="12") selected #end>手机上传</option>
        	<option value="13" #if("$!LogSearchForm.searchOperation"=="13") selected #end>单据推送</option>
        	<option value="14" #if("$!LogSearchForm.searchOperation"=="14") selected #end>扩展</option>
        	<option value="15" #if("$!LogSearchForm.searchOperation"=="15") selected #end>导出</option>
        </select>
      </div>
      <div class="items">
      	<i>类型：</i>
        <input class="slt" id="searchBill" name="searchBill" value="$!LogSearchForm.searchBill"/>
      </div>
      <div class="items">
      	<i>内容：</i>
        <input class="slt" id="searchContent" name="searchContent" value="$!LogSearchForm.searchContent"/>
      </div>
      <div class="items" style="margin:0 0 0 10px;">
      	<div class="btn btn-mini" onclick="searchData()">查询</div>
      	#if($LoginBean.operationMap.get("/LogManageAction.do").delete())
        <div class="btn btn-inverse btn-mini" onclick="deleteLog()">删除30天前操作日志</div>
       	#end
      </div>
      <p class="clear"></p>
    </div>
    <div class="moveLog" id="div_move">
    	<script type="text/javascript"> 
    		$("#div_move").height(document.documentElement.clientHeight-80);
		</script>
    	<ul class="t_u">
    		<li class="w40">No.</li>
    		<li class="w100">职员全称</li>
    		<li class="w120" style="text-align: left;">类型名称</li>
    		<li class="w130">操作</li>
    		<li class="w160">执行时间</li>
    		<li class="w">内容</li>
    	</ul>
    	#set($count = 1)
		#foreach($!log in $!logList)
		<ul #if($globals.isOddNumber($velocityCount)) class="c1" #else class="c2" #end>
			<li class="w40">$count</li>
			<li class="w100" title="$!log.userName" >$globals.subTitle($!log.userName,20)</li>
    		<li class="w120" style="text-align: left;">$!log.billTypeName</li>
    		<li class="w130" style="text-align: left;">
    		#if("$!log.operation"=="0")
    			添加
    		#elseif("$!log.operation"=="1")
    			修改
    		#elseif("$!log.operation"=="2")
    			删除
    		#elseif("$!log.operation"=="3")
    			转交
    		#elseif("$!log.operation"=="4")
    			暂存
    		#elseif("$!log.operation"=="5")
    			草稿添加
    		#elseif("$!log.operation"=="6")
    			草稿修改
    		#elseif("$!log.operation"=="7")
    			草稿删除
    		#elseif("$!log.operation"=="8")
    			草稿审核
    		#elseif("$!log.operation"=="9")
    			列配置

    		#elseif("$!log.operation"=="10")
    			导入
    		#elseif("$!log.operation"=="11")
    			零售上传
    		#elseif("$!log.operation"=="12")
    			手机上传
    		#elseif("$!log.operation"=="13")
    			单据推送

			#elseif("$!log.operation"=="14")
    			<span title="扩展($!log.extendFun)">扩展($!log.extendFun)</span>
    		#elseif("$!log.operation"=="15")
    			导出
    		#else
    			$!log.operation
    		#end
    		</li>
    		<li class="w160">$!log.operationTime</li>
    		<li class="w" style="text-align: left;" title="$!log.content">#if($!log.content.indexOf("Id:")>0)$globals.subTitle($globals.substring($!log.content,'Id:'),75)#else$globals.subTitle($!log.content,75)#end</li>
		</ul>
		#set($count = $count+1)
		#end
		#if($logList.size()==0)
			<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">暂无操作日志信息</div>
		#end
	</div>
	<div class="listRange_pagebar">
		$!pageBar
	</div>	
    </form>
</body>
</html>
