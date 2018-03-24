<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>AIO</title>
<link type="text/css" href="/$globals.getStylePath()/css/classFunction.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/oaCalendarAdd.css"/>
<link type="text/css" href="/style/css/oa_myTask.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/crm/crm_client.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/oa/oaCalendarAdd.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="/js/oa/oaTask.js"></script>
<script type="text/javascript" src="/js/oa/itemTaskPublic.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<style type="text/css">
em,i{font-style:normal;}
.h-child-btn{position:relative;padding:1px 17px 1px 12px;}
.sbtn a{float:left;display:inline-block;width:65px;}
.op{position:relative;}
.op .isd_dv{display:none;border-radius:4px;left:-70px;top:32px;position:absolute;z-index:500;border:1px solid #666;width:250px;height:80px;background:#fff;box-shadow:0 0 10px rgba(0,0,0,.5);cursor:default;zoom:1;}
.op .isd_dv .jt_b{width:20px;height:11px;position:absolute;background:url(/style/images/client/784.png) no-repeat 0 0;left:45%;top:-11px;}
.op .isd_dv .wd_em{display:block;text-align:center;margin:15px 0 0 0;}
.op .isd_dv .btn_sp{display:inline-block;width:100%;margin:12px 0 0 0;background:#f5f5f5;border-top:1px solid #ddd;border-radius:0 0 4px 4px;}
.op .isd_dv .btn_sp i{color:#025494;cursor:pointer;height:29px;line-height:27px;float:left;width:33.2%;display:inline;text-align:center;border-top:1px solid #fff;border-left:1px solid #ddd;margin-right:-1px;text-decoration:none;}
.op .isd_dv .btn_sp i:hover{background-color:#ebebeb;}
.add-calendar{left: 40%;top: 150px;}
div.scott select{display: inline-block;}
</style>
<script type="text/javascript">
$(document).ready(function(){	
	$(".h-child-btn").mouseover(function(){
		$(this).addClass("h-height").find(".d-more").show().siblings(".br-link").show();
	}).mouseout(function(){
		$(this).removeClass("h-height").find(".d-more").hide().siblings(".br-link").hide();
	});

	//个数为0的按扭去掉

	$(".h-child-btn").each(function(){
		if($(this).find(".d-more a").size()==0){			
			$(this).parents("li").hide();
		}
	});
});
var deleteMsg = '$text.get("crm.msg.mustSelectOne")' ;
var confirmMsg = '$text.get("common.msg.confirmDel")' ;
var sucessDel = '$text.get("common.msg.delSuccess")' ;
var operation = $globals.getOP("OP_DELETE") ;
var selectOne = '$text.get("crm.msg.mustSelectOne")';
var varMOID = "$!MOID";
var textBoxObj;

//双击个人部门返回处理函数
function fillData(datas){
	if(hideAsynId == "normalId"){
		$("#"+hideFieldName).val(datas.split(";")[0]);
	    $("#"+hideFieldName+"Name").val(datas.split(";")[1]);
	    jQuery.close(hideAsynId);
	}else if("participantBoxId" == hideAsynId){
		textBoxObj.add(datas.split(";")[1],datas.split(";")[0]);
		 jQuery.close(hideAsynId);
    }else{
		indexSearch(datas,'');
		jQuery.close('Popdiv');
	}
	/*
	if(jQuery.exist('indexPop')){
		//处理首页选择个人、选择客户弹出框双击 
		indexSearch(datas,'');
	}else if(jQuery.exist('dealdivNew')){
		//处理首页-点击修改-共享双击
		jQuery.opener('dealdivNew').evaluate(datas);
	}else if(jQuery.exist('dealdiv')){
		//处理首页共享双击
		jQuery.opener('dealdiv').evaluate(datas);
	}else if(jQuery.exist('handOver')){
		//处理首页处理移交
		jQuery.opener('handOver').evaluate(datas);
	}
	*/
	
}

function addWorkPlan(){
	var nowStr = getCurDate();
	var keyId = $("input[name='keyId']:checked").val() ;
	var url = "/OAWorkPlanAction.do?operation=6&userId=$!LoginBean.id&planType=day&strDate="+nowStr+"&winCurIndex=&score=&planStatus=&enterFrom=CRMBrother&clientId="+keyId;
	var height = 490;
	if($(document).height()<400){height=360}
	asyncbox.open({
		id:'planId',url:url,title:'添加工作计划',width:970,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
		    	opener.beforSubmit();
				return false;
			}
　　  　	}
　  });
}

/*添加点击转交,添加操作成功后回调的方法,
1.关闭添加弹出框  
2.调用审核方法
*/
function dealCheck(keyId){
	jQuery.close('dealdiv');
	approveByAdd(keyId);//添加后调用审核方法  
}


/*添加后调用审核方法,取消或关闭按钮刷新列表*/
function approveByAdd(keyId){
	var url = '/OAMyWorkFlow.do?&operation=82&tableName=$clientTableName&keyId='+keyId;
	asyncbox.open({
		id:'deliverTo',url:url,title:'转交',width:650,height:370,
        btnsbar:jQuery.btn.OKCANCEL,
	    callback:function(action,opener){
		    if(action == 'ok'){
				if(opener.beforSubmit(opener.form)){
					opener.form.submit();
				}
				return false;
			}else if(action == 'close' ||action == 'cancel'){
				window.location.href="/CRMClientAction.do?operation=$globals.getOP('OP_QUERY')&ModuleId="+$("#ModuleId").val()+"&viewId="+$("#viewId").val();
			}
　　     }
　 });
}

/*加载控件*/
function loadTextBox(boxId){
	textBoxObj = new jQuery.TextboxList('#'+boxId, {unique: true, plugins: {autocomplete: {}}});
	textBoxObj.getContainer().addClass('textboxlist-loading');
	textBoxObj.plugins['autocomplete'].setValues($textBoxValues);
	textBoxObj.getContainer().removeClass('textboxlist-loading');
}

function billDetQuery(reportNumber){ 
	width=document.documentElement.clientWidth-50;
	height=document.documentElement.clientHeight-20;
	
	var items = document.getElementsByName("keyId");
	var clientName='';
	var CompanyCode='';
	for(var i=0;i<items.length;i++){
	    if(items[i].checked){
	    	clientName=encodeURIComponent($(items[i]).parent().attr("ClientName"));
	    	CompanyCode=encodeURIComponent($(items[i]).parent().attr("CompanyCode"));
	    }
	}   
	if(reportNumber=='CRMSaleFollowUp'){//联系记录 
		url='/CRMBrotherAction.do?tableName=CRMSaleFollowUp&keyOption=ClientId&keyword='+clientName+'&noback=true&url=@URL:';
	}else if(reportNumber=='ReportSalesTotalGrossProfit'){//成交记录
		url='/ReportDataAction.do?reportNumber=ReportSalesTotalGrossProfit&LinkType=@URL:&tblCompany.ComFullName='+clientName+'&CompanyCode='+CompanyCode+'&hide_CompanyCode='+CompanyCode+encodeURIComponent('#;#0#;#'+'#;#')+'&noback=true&isEqualZero=0&DataType=tblCompany&startDate=$!periodBegin';
	}	 
	
	openPop('PopBillDetQuerydiv'+reportNumber,'',url,width,height,false,false);
}

</script>
</head>
<body class="body_ef">
<div id="wrapper">
<div class="cont cf">
<div class="pagetitle">
	<span class="all_icon i_man"></span><h3>#if("$!attention"=="ok")我的关注#elseif("$!conMap.get('public')"=="public")公共池客户#else客户资料#end</h3>
	#if("$!conMap.get('public')"=="public" || "$!attention"=="ok")
	<div id="clientBtn" class="backDiv" title="返回客户列表"><span class="backBtn"></span><h3>客户列表</h3></div>
	#end
	#if("$!conMap.get('public')"!="public")
	<!-- 
	<div class="inpsearch"><input name="" type="text" class="inpsearch" value="输入客户名称或联系人手机号码" onblur="if(this.value=='')this.value='输入客户名称或联系人手机号码';"
		onfocus="if(this.value=='输入客户名称或联系人手机号码'){this.value='';}this.select();" /><a class="inpsearch_btn" href="javascript:"></a></div>
	<span class="sameSearch">撞单查询：</span>
	 -->
	#end
	#set($moduleCount = 0)
	#if($!filterModuleList.size()>1)
		<div class="module_list f_l">
			#foreach($module in $filterModuleList)
				#if($globals.get($module,4)>0)
				#if("$!globals.get($module,0)" != "weixinModuleId" || ("$!globals.get($module,0)" == "weixinModuleId" && "$!globals.getSysSetting('weixinBgUrl')" !="0"))
					<div #if("$!globals.get($module,0)"=="$!conMap.get('moduleId')") class="lp2 sbtn63" #else class="lp sbtn72" #end id="$globals.get($module,0)" ><span class="a"></span><a >$globals.get($module,1)</a><span class="c"></span></div>
				#end
				#set($moduleCount = $moduleCount+1)
				#end
			#end
		</div>
	#end
	
</div>
<div class="c_operate_l">
#if($LoginBean.operationMap.get("/CRMClientAction.do").add())
<a href="javascript:" class=" icon_left i_add" id="addBtn" title="添加客户" ></a>
#end
#if($LoginBean.operationMap.get("/ClientSettingAction.do").query())
<a href="javascript:" class=" icon_left i_reset" id="setBtnNew" title="客户模板设置"></a>
#end
<a href="javascript:" class=" icon_left i_edit" id="publicBtn" title="公共池客户"></a>
<!-- 
<a href="javascript:" class=" all_icon i_tel" id="telBtn" title="电话"></a>
<a href="javascript:" class=" icon_left i_star" id="attentionBtn" title="我的关注"></a> -->
#if($moduleList.size()>1)
<a href="javascript:" class=" icon_left i_defModule" id="setDefModuleBtn" title="设置默认模板"></a>
#end
</div>
<div class="c_main winw1200 f_l" id="clientId">
<a name="html_top"></a>
<script type="text/javascript">
	var oDiv=document.getElementById("clientId");
	var sHeight=document.documentElement.clientHeight-80;
	var sWidth=document.documentElement.clientWidth-80;
	oDiv.style.height=sHeight+"px";
	oDiv.style.width=sWidth+"px";
</script>
<form name="form" action="/CRMClientAction.do" method="post" id="form">
<input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_QUERY')">
<input type="hidden" id="statusTo" name="statusTo" value="">
<input type="hidden" id="type" name="type" value="main">
<input type="hidden" id="firstEnter" name="firstEnter" value="false">
<input type="hidden" id="ButtonType" name="ButtonType" value="">
<input type="hidden" id="reportName" name="reportName" value="$text.get('call.lb.client')">
<input type="hidden" id="fromCRM" name="fromCRM" value="clientList">
<input type="hidden" id="newCreateBy" name="newCreateBy" value="">
<input type="hidden" id="handContent" name="handContent" value="">
<input type="hidden" id="handSmsType" name="handSmsType" value="">
<input type="hidden" id="strStatus" name="strStatus" value="">
<input type="hidden" id="clientStatus" name="clientStatus" value="">
<input type="hidden" id="expandStatus" name="expandStatus" value="$!conMap.get('expandStatus')">
<input type="hidden" id="public" name="public" value="$!conMap.get('public')">
<input type="hidden" id="myAttention" name="myAttention" value=""> 
<input type="hidden" id="popSelectName" name="popSelectName" />
<input type="hidden" id="trade" name="trade" value="$!trade" />
<input type="hidden" id="district" name="district" value="$!district" />
<input type="hidden" id="employee" name="employee" value="$!employee" />
<input type="hidden" id="departMent" name="departMent" value="$!departMent" />
<input type="hidden" id="searchName" name="searchName" value="$!searchName"/>
<input type="hidden" id="ModuleId" name="ModuleId" value="$!conMap.get('moduleId')"/>
<input type="hidden" id="viewId" name="viewId" value="$!conMap.get('viewId')"/>
<input type="hidden" id="moduleTransferId" name="moduleTransferId" value=""/>
<input type="hidden" id="multiple" name="multiple" value="$!conMap.get('multiple')"/>
<input type="hidden" id="isFirstSearch" name="isFirstSearch" value="$!conMap.get('isFirstSearch')"/>
<input type="hidden" id="defModuleId" name="defModuleId" value=""/>
<input type="hidden" id="searchFields" value="$moduleViewBean.searchFields"/>
<input type="hidden" id="updateFlag" name="updateFlag" value="$!updateFlag"/>
<input type="hidden" id="updateKeyId" name="updateKeyId" value="$!updateKeyId"/>
<input type="hidden" id="tempDistrictIds" name="tempDistrictIds" value=""/>
<input type="hidden" id="sortInfo" name="sortInfo" value="$!conMap.get('sortInfo')"/>
<input type="hidden" id="rekeyword" value="$!conMap.get('keyword')">
<input type="hidden" id="transferType" name="transferType" value="">
<input type="hidden" id="clientIds" name="clientIds" value="">
<input type="hidden" id="existIds" name="existIds" value="">
<input type="hidden" id="parentClasscode" name="parentClasscode" value="">
<input type="hidden" id="exportAll" name="exportAll" value="">
<input type="hidden" id="isPostpay" name="isPostpay" value='$!conMap.get("isPostpay")'>
<input type="hidden" id="defineName" name="defineName" value="">
#foreach($col in $moduleViewBean.searchFields.split(","))
#set($field=$globals.getField($col,$clientTableName,$contactTableName))
#if("$field.inputType"=="1")
<input type="hidden" id="$field.fieldName" name="$field.fieldName" value="$!conMap.get($field.fieldName)"/>
<input type="hidden" id="${field.fieldName}_mul" name="${field.fieldName}_mul" value="$!request.getAttribute("${field.fieldName}_mul")"/>
#end
#end
<input type="hidden" id="SQLSave" name="SQLSave" value="$!SQLSave">
<input type="hidden" id="labelQueryIds" name="labelQueryIds" value="$!labelQueryIds">
<div class="main_search">
<div class="col">
<span>模糊查询：</span>
<div class="inpsearch"> 
	<select style="float:left;height: 22px;" name="keyOption" onKeyDown="if(event.keyCode==13) keywordSubmit();">
		#foreach($col in $keyMaps.keySet())
			<option value="$col" #if("$!conMap.get('keyOption')" == "$col") selected="selected" #end>$keyMaps.get($col)</option>
		#end
		<option value="all" #if("$!conMap.get('keyOption')" == "all") selected="selected" #end>全部</option>
	</select>  
	<input id="keyword" name="keyword" type="text" class=""   value="$!conMap.get('keyword')" onKeyDown="if(event.keyCode==13) keywordSubmit();"/><a class="inpsearch_btn" href="javascript:keywordSubmit();"></a></div>
<div class="stnn">
<div class="sbtn sbtn110" id="sdepartMent"><em class="a"></em><a href="javascript:" title="选择部门">选择部门</a><em class="c"></em></div>
<div class="sbtn sbtn110" id="semployee"><em class="a"></em><a href="javascript:" title="选择职员">选择职员</a><em class="c"></em></div>
<div class="sbtn sbtn110" id="sdistrict"><em class="a"></em><a href="javascript:" title="选择地区">选择地区</a><em class="c"></em></div>
<div class="sbtn sbtn110" id="strade"><em class="a"></em><a href="javascript:" title="选择行业">选择行业</a><em class="c"></em></div>
</div>
</div>

#if("$!OAWorkFlow" == "true")
<div class="col">
	<span>审核状态：</span>
	<ul class="searchMySelfStatus" fieldName="approveStatus">
		#set($approveStatus = $!conMap.get('approveStatus'))
		<input type="hidden" id="approveStatus" name="approveStatus" value="$!conMap.get('approveStatus')"/>
		<li #if("$approveStatus" == "0") class="sel" #end statusVal="0" title="待审核"><a>待审核</a></li>
		<li #if("$!approveStatus" == "all" || "$!approveStatus" == "") class="sel" #end statusVal="all"><a>全部</a></li>
		<li #if("$approveStatus" == "approveing") class="sel" #end statusVal="approveing" title="审核中"><a>审核中</a></li>
		<li #if("$approveStatus" == "-1") class="sel" #end statusVal="-1" title="已审核"><a>已审核</a></li>
		<!-- 
		<li #if("$approveStatus" == "back") class="sel" #end statusVal="back" title="审核失败"><a>审核失败</a></li>
		 -->
	</ul>
</div>
#end

<!-- 
#if($LoginBean.operationMap.get("/DeliverManageAction.do").query())
<div class="col">
		<span>发货状态：</span>
		<ul class="searchMySelfStatus" fieldName="deliverStatus">
			#set($deliverStatus = $!conMap.get('deliverStatus'))
			<input type="hidden" id="deliverStatus" name="deliverStatus" value="$!conMap.get('deliverStatus')"/>
			<li #if("$!deliverStatus" == "all" || "$!deliverStatus" == "") class="sel" #end statusVal="all"><a>全部</a></li>
			<li #if("$deliverStatus" == "1") class="sel" #end statusVal="1" title="未发货"><a>未发货</a></li>
			<li #if("$deliverStatus" == "2") class="sel" #end statusVal="2" title="已发货"><a>已发货</a></li>
		</ul>
</div>
#end

 -->



#if("$!district" != "")
<div class="col districtDiv" id="district_select">
<span>已选地区：</span>
<ul>
#foreach($districtId in $showDistrictMap.keySet())
#if("$!districtId" !="")
<div class="navigationShow"  id="$districtId"><div >$showDistrictMap.get($districtId)</div><img  src="/style/images/body/i_007.gif"  onclick="delSelected('$districtId','$showDistrictMap.get($districtId)','district','districtDiv')"/></div>
#end
#end
#if($showDistrictMap.size() > 1)<a href="#" class="nomore" onclick="submitQuery();" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span></a><a href="#" class="nomore" onclick="clearALL('district');" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="清空">清空</span></a>#end
</ul>
</div>
#end
#if("$!trade" != "")
<div class="col tradeDiv" id="trade_select">
<span>已选行业：</span>
<ul>
#foreach($tradeId in $showTradeMap.keySet())
#if("$!tradeId" !="")
<div class="navigationShow"  id="$tradeId"><div >$showTradeMap.get($tradeId)</div><img  src="/style/images/body/i_007.gif"  onclick="delSelected('$tradeId','','trade','tradeDiv')"/></div>
#end
#end
#if($showTradeMap.size() > 1)<a href="#" class="nomore" onclick="submitQuery();" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span></a><a href="#" class="nomore" onclick="clearALL('trade');" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="清空">清空</span></a>#end
</ul>
</div>
#end
#if("$!employee" != "")
<div class="col employeeDiv" id="employee_select">
<span>已选职员：</span>
<ul>
#foreach($col in $employee.split(","))
#if("$!col" !="")
<div class="navigationShow"  id="$col"><div >$globals.getEmpFullNameByUserId($col)</div><img  src="/style/images/body/i_007.gif"  onclick="delSelected('$col','','employee','employeeDiv')"/></div>
#end
#end
#if($globals.splitLength($employee,",") > 2)<a href="#" class="nomore" onclick="submitQuery();" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span></a><a href="#" class="nomore" onclick="clearALL('employee');" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="清空">清空</span></a> #end
</ul>
</div>
#end
#if("$!departMent" != "")
<div class="col deptDiv" id="departMent_select">
<span>已选部门：</span>
<ul>
#foreach($col in $departMent.split(","))
#if("$!col" !="")
<div class="navigationShow"  id="$col"><div >$!deptMap.get("$col")</div><img  src="/style/images/body/i_007.gif"  onclick="delSelected('$col','','departMent','deptDiv')"/></div>
#end
#end
#if($globals.splitLength($departMent,",") >2)<a href="#" class="nomore" onclick="submitQuery();" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span></a><a href="#" class="nomore" onclick="clearALL('departMent');" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="清空">清空</span></a>#end
</ul>
</div>
#end


#if("$!labelQueryIds" != "")
<div class="col">
<span>已选标签：</span>
<ul>
#foreach($labelId in $labelQueryIds.split(","))
#if("$!labelId" !="")
<div tabindex="0"  class="nui-tag nui-tag-1" >
	<span id="searchLabelName" style="background-color: $labelMap.get($labelId).labelColor;">$labelMap.get($labelId).labelName</span>
	<span id="searchLabelX" style="background-color: $labelMap.get($labelId).labelColor;" onclick="delSearchLabel('$labelMap.get($labelId).id')">X</span>
</div>
#end
#end

#if($globals.splitLength($labelQueryIds,",") >=2)
<div tabindex="0"  class="nui-tag nui-tag-1" >
	<span id="searchLabelName" style="background-color: #3399CC;border-top-right-radius:3px;border-bottom-right-radius:3px;" onclick="delSearchLabel('clear')" >清空</span>
</div>
#end
</ul>
</div>
#end



#set($hideFlag="false")
#set($index=1)
#foreach($col in $moduleViewBean.searchFields.split(","))
#set($enumValue ="")
#if($index<3)
#set($field=$globals.getField($col,$clientTableName,$contactTableName))
#if("$!conMap.get('public')"=="public" && "$!field.refEnumerationName"=="ClientStatus")
#set($hideFlag="true")
#else

	#if("$field.inputType"=="1")
		<div class="col">
		<span>#if($col== "LastContractTime") 未联系天数：  #else $globals.getColName($col,$clientTableName,$contactTableName)： #end</span>
		<ul>
		<li #if("$!isFirstSearch" != "false") class="sel" #end name="$field.fieldName" enumValue="all"><a>全部</a></li>
		#set($enumValue = ","+$conMap.get($field.fieldName))
		#foreach($enum in $globals.getEnumerationItems($field.refEnumerationName))
			#if("$!conMap.get('public')"!="public" && "$!field.refEnumerationName"=="ClientStatus" && "$!enum.value"=="1")
			#else
			<input type="checkbox" name="${field.fieldName}_boxs" #if($enumValue.indexOf(",$!{enum.value},")!=-1) checked="checked" #end  class="nabigationCheck" value="$!enum.value"/>
			<li #if($enumValue.indexOf(",$!{enum.value},")!=-1) class="sel" #end name="$field.fieldName" enumValue="$!enum.value" title="$enum.name"><a>$enum.name</a></li>
			#end
		#end
		#if("$field.fieldName" != "LastContractTime")<a href="#" class="more multipleSelect" id="${field.fieldName}_btn" mulFlag='$!request.getAttribute("${field.fieldName}_mul")' mulName='${field.fieldName}_mul'><span style="width: 40px;margin-top: -1px;" title="多选">多选</span></a>#end
		</ul>
		</div>
	#else
		<div class="col">
		<span>$globals.getColName($col,$clientTableName,$contactTableName)：</span>
		<ul>
		<div style="float: left;"><input type="text" name="${col}Start" onClick="WdatePicker({lang:'$globals.getLocale()'})"  onKeyDown="if(event.keyCode==13) submitQuery();" value='$!conMap.get("${col}Start")' />&nbsp; 至 &nbsp;<input type="text" name="${col}End" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value='$!conMap.get("${col}End")'/></div>&nbsp;<a href="#" class="nomore" onclick="submitQuery();" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span></a>
		</ul>
		</div>
	#end


#set($index=$index+1)
#end
#end
#end
<!-- 隐藏条件 -->
<div style="display:none;" id="showSearch">
#set($hideCount=2)
#if($hideFlag =="true")
	#set($hideCount=3)
#end
#foreach($col in $moduleViewBean.searchFields.split(","))
#set($enumValue ="")
#if($velocityCount>$hideCount)
#set($field=$globals.getField($col,$clientTableName,$contactTableName))
#if("$!conMap.get('public')"=="public" && "$!field.refEnumerationName"=="ClientStatus")
#else

	#if("$field.inputType"=="1")
		<div class="col">
		<span>#if($col== "LastContractTime") 未联系天数：#elseif($col== "NextFollowTime")待联系天数：#else $globals.getColName($col,$clientTableName,$contactTableName)： #end
		</span>
		<ul>
		<li #if("$!isFirstSearch" != "false") class="sel" #end name="$field.fieldName" enumValue="all"><a>全部</a></li>
		#set($enumValue = ","+$conMap.get($field.fieldName))
		#foreach($enum in $globals.getEnumerationItems($field.refEnumerationName))
			#if("$!conMap.get('public')"!="public" && "$!field.refEnumerationName"=="ClientStatus" && "$!enum.value"=="1")
			#else
			<input type="checkbox" name="${field.fieldName}_boxs" #if($enumValue.indexOf(",$!{enum.value},")!=-1) checked="checked" #end  class="nabigationCheck" value="$!enum.value"/>
			<li #if($enumValue.indexOf(",$!{enum.value},")!=-1) class="sel" #end name="$field.fieldName" enumValue="$!enum.value" title="$enum.name"><a>$enum.name</a></li>
			#end
		#end
		#if("$field.fieldName" != "LastContractTime" and "$field.fieldName" != "NextFollowTime" and "$field.fieldName" != "createTime")<a href="#" class="more multipleSelect" id="${field.fieldName}_btn" mulFlag='$!request.getAttribute("${field.fieldName}_mul")' mulName='${field.fieldName}_mul'><span style="width: 40px;margin-top: -1px;" title="多选">多选</span></a>#end
		</ul>
		</div>
	#else
		<div class="col">
		<span>$globals.getColName($col,$clientTableName,$contactTableName)：</span>
		<ul>
		<div style="float: left;"><input type="text" name="${col}Start" onClick="WdatePicker({lang:'$globals.getLocale()'})"  onKeyDown="if(event.keyCode==13) submitQuery();" value='$!conMap.get("${col}Start")' />&nbsp; 至 &nbsp;<input type="text" name="${col}End" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value='$!conMap.get("${col}End")'/></div>&nbsp;<a href="#" class="nomore" onclick="submitQuery();" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span></a>
		</ul>
		</div>
	#end
#end
#end
#end
</div>
</div>
<div class="maintablearea">
<div class="bd" id="maintop">
<div class="hd">
<div class="m_l f_l">
<div class="zk" id="searchExpand">展开</div>
</div>
#if($moduleViewList.size()>1)
<div class="view_list f_l">
#foreach($view in $moduleViewList)
<div #if("$view.id"=="$!conMap.get('viewId')") class="lp2 sbtn63" #else class="lp sbtn72" #end id="$view.id" ><span class="a"></span><a>$view.viewName</a><span class="c"></span></div>
#end
</div>
#end
<div class="m_r f_r">
#if("$!globals.getSysSetting('weixinBgUrl')" !="0")
<div class="op sbtn41" id="weixinBtn"><span class="a"></span><a>微信</a><span class="c"></span></div>
#end
<div class="op sbtn41" id="emailBtn"><span class="a"></span><a>邮件</a><span class="c"></span></div>
<div class="op sbtn41" id="msgBtn"><span class="a"></span><a>短信</a><span class="c"></span></div>
#if($!scopeMap.get("importFlag"))
<div class="op sbtn41" id="importBtn"><span class="a"></span><a>导入</a><span class="c"></span></div>
#end
#if("$LoginBean.id" == "1")
<div class="op sbtn63" id="exportAllBtn"><span class="a"></span><a>导出所有</a><span class="c"></span></div>
#end
#if($!scopeMap.get('exportFlag'))
<div class="op sbtn41" id="exportBtn"><span class="a"></span><a>导出</a><span class="c"></span></div>
#end

#if($LoginBean.operationMap.get("/CRMClientAction.do").update())
<!-- <div class="op sbtn41" id="startBtn"><span class="a"></span><a>启用</a><span class="c"></span></div>
<div class="op sbtn41" id="stopBtn"><span class="a"></span><a>停用</a><span class="c"></span></div> -->
<div class="op sbtn63" id="statusBtn"><span class="a"></span><a>周期转换</a><span class="c"></span></div>
#if($LoginBean.operationMap.get("/CRMClientTransferAction.do").query())
<div class="op sbtn41" id="handBtn"><span class="a"></span><a>移交</a><span class="c"></span></div>
#end
<div class="op sbtn41" id="mulShareClient"><span class="a"></span><a>共享</a><span class="c"></span></div>
#if($moduleCount>1)
<div class="op sbtn63" id="moduleBtn"><span class="a"></span><a>模板转移</a><span class="c"></span></div>
#end
#end
<!-- 
#if("$printFlag"=="true")
<div class="op sbtn41" id="printBtn"><span class="a"></span><a>打印</a><span class="c"></span></div>
#end
 -->
#if($LoginBean.operationMap.get("/CRMClientAction.do").delete())
<div class="op sbtn41" id="deleteBtn"><span class="a"></span><a>删除</a><span class="c"></span></div>
#end
#if($LoginBean.operationMap.get("/ClientSettingAction.do").query())
<div class="op sbtn63" id="moduleViewEdit"><span class="a"></span><a>视图设置</a><span class="c"></span></div>
#end
#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblCompany&moduleType=2").query())
<div class="op sbtn105">
	<span class="a"></span><a id="crmToErp" transferType="crmToErp">转入ERP客户</a><span class="c"></span>
	<div class="isd_dv" >
		<b class="jt_b"></b>
		<em class="wd_em">是否将客户转入ERP已有客户的下级?</em>
		<span class="btn_sp">
			<i class="i_y" onclick="transferConfirm(this);" Con="y">是</i>
			<!-- <i class="i_n"  Con="n"></i>-->
			<i class="i_n" onclick="transferConfirm(this);" Con="n">转根级目录</i>
			<i class="i_r" onclick="transferConfirm(this);" Con="r">取消操作</i>
		</span>
	</div>
</div>
<div class="op sbtn105" id="erpToCrm" transferType="erpToCrm"><span class="a"></span><a>引入ERP客户</a><span class="c"></span></div>
#end
#if($LoginBean.operationMap.get("/CRMBrotherAction.do?tableName=CRMSaleFollowUp").query())
<div class="op sbtn63" id="CRMSaleFollowUp" onclick="billDetQuery('CRMSaleFollowUp')"><span class="a"></span><a>联系记录</a><span class="c"></span></div>
#end
#if($LoginBean.operationMap.get("/ReportDataAction.do?reportNumber=ReportSalesTotalGrossProfit").query())
<div class="op sbtn63" id="ReportSalesTotalGrossProfit" onclick="billDetQuery('ReportSalesTotalGrossProfit')"><span class="a"></span><a>成交记录</a><span class="c"></span></div>
#end

#if($LoginBean.operationMap.get("/TelecomRecordAction.do").query())
<div class="op sbtn63" id="getWorkFlowInfo"><span class="a"></span><a>获取数据</a><span class="c"></span></div>
#end
#if($LoginBean.operationMap.get("/CRMReleaseInfoAction.do").query())
<div class="op sbtn63" id="releaseInfo"><span class="a"></span><a>释放数据</a><span class="c"></span></div>
#end

</div>
</div>

<ul class="maintop">

#if("$!screenWidth" == "")
	#set($screenWidth = "1024")
#end
#set($maintopWidth = 0)
#set($minfoTranFields = "")
#if("$!moduleBean.isUserLabel" == "1")#set($usedWidth = $math.sub($screenWidth,385)) #else #set($usedWidth = $math.sub($screenWidth,245)) #end
#if("$!OAWorkFlow" == "true")#set($usedWidth = $math.sub($usedWidth,90))  #end
#set($usedWidth = $math.sub($usedWidth,90))
#foreach($col in $moduleViewBean.listFields.split(","))
	#if("$col" != "labelId")
		#set($field=$globals.getField($col,$clientTableName,$contactTableName))
		#if("$!field.width"!="" && $!field.width!=0) #set($maintopWidth = $maintopWidth +${field.width}) #else #set($maintopWidth = $maintopWidth +90) #end
		
			#if($velocityCount==1)
			<input type="checkbox" id="checkAll" style="float: left;margin:8px 5px 0 5px;"/>
			<li class="col_hy" style="width:60px;text-align:center;">操作</li>
			<li class="col_hy"  style="width:${field.width}px;text-align:center;" ><span sort="DESC" fieldName="$col" style="cursor: pointer;" title="点击排序">$globals.getColName($col,$clientTableName,$contactTableName)&nbsp;</span></li>
			#else
			<li class="col_hy" #if("$!field.width"!="" && $!field.width!=0)style="width:${field.width}px;" #end><span sort="DESC" fieldName="$col" style="cursor: pointer;" title="点击排序">$globals.getColName($col,$clientTableName,$contactTableName)&nbsp;</span></li>
			#end
		
	#end
#end
<!-- 
#if("$!moduleBean.isUserLabel" == "1")<li class="col_hy" style="width: 140px;text-align: center;">客户标签</li>#end
 -->
<li class="col_hy" style="width: 90px;">未联系(天)</li>

#if("$!OAWorkFlow" == "true")
<!-- 
<li class="col_hy" style="width: 90px;"><span>审核状态</span></li>
 -->
<li class="col_hy" style="width: 90px;"><span>当前审核节点</span></li>
#end
</ul>
</div>

#set($contentWidth = 0)
#set($contentTranFields = "")
<!-- 
#if("$!moduleBean.isUserLabel" == "1") #set($contUserWidth = $math.sub($screenWidth,385)) #else #set($contUserWidth = $math.sub($screenWidth,245)) #end
-->
#set($contUserWidth = $math.sub($screenWidth,245))
#if("$!moduleBean.workflow" == "100")#set($contUserWidth = $math.sub($contUserWidth,80))#end
#set($contUserWidth = $math.sub($contUserWidth,90))
<div class="bd" id="titleId">
#set($rowIndex=1)
#foreach($client in $clientList)
#set($shareOp = "false")
#if($LoginBean.operationMap.get("/CRMClientAction.do").update() &&($LoginBean.id=="1" || $LoginBean.id=="$client.get('createBy')"))
#set($shareOp = "true")
#end
#if($LoginBean.operationMap.get("/CRMClientAction.do").update())
#set($shareOp = "true")
#end
<ul class="col  context-menu-one" id="scroll$!client.get('id')" shareOp="$shareOp" clientName="$client.get('ClientName')" CompanyCode="$client.get('CompanyCode')">
	
	#set($colIndex=1)
	#foreach($col in $moduleViewBean.listFields.split(","))
	
		#set($field=$globals.getField($col,$clientTableName,$contactTableName))
		#if("$!field.width"!="" && $!field.width!=0) #set($contentWidth = $contentWidth +${field.width}) #else #set($contentWidth = $contentWidth +90) #end
			#if($usedWidth>$contentWidth)
			#if($colIndex==1)
				<input name="keyId" type="checkbox" value="$!client.get('id')" style="float: left;margin:6px 5px 0 5px;"  title="第${rowIndex}行"/>
				
				<li class="col_hy" style="width:60px;text-align:center;">
					<span style="display:inline-block;overflow:hidden;">
					#if($client.get("workFlowNode")!="-1" && $client.get("checkPersons").indexOf(";${LoginBean.id};")>-1 && $LoginBean.operationMap.get("/CRMClientAction.do").update())
						<a class="update updateBtn" title='修改'></a>
					#elseif((("$!OAWorkFlow" == "") || ("$!OAWorkFlow" == "true" && $client.get("workFlowNode")=="0")) && $LoginBean.operationMap.get("/CRMClientAction.do").update())
						<a class="update updateBtn" title='修改'></a>
					#end
					<a class="detail detailBtn" title='详情'></a>
					<span>
				</li>
				
				
				
				<li class="col_name" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;cursor: pointer;" #end title="$!client.get("$field.fieldName")">
				<span class="expands" title="展开"></span>${rowIndex}. $!client.get("$field.fieldName")</li>
			#else
			#set($fieldValue = $!client.get("$field.fieldName"))	
			#if("$field.inputType"=="0")
				#if("$!field.fieldName"=="Trade")
					<li class="col_hy" title="$!workProfessionMap.get($!fieldValue)" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$!workProfessionMap.get($!fieldValue)&nbsp;</li>
				#elseif("$!field.fieldName"=="District")
					<li class="col_hy" title="$!districtMap.get($!fieldValue)" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$!districtMap.get($!fieldValue)&nbsp;</li>
				#elseif("$field.fieldType"=="5" || "$field.fieldType"=="6")
					<li class="col_hy" title="$!client.get("$field.fieldName")" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$!fieldValue</li>
				#else
					<li class="col_hy" title="$!client.get("$field.fieldName")" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$!fieldValue</li>
				#end
			#elseif("$field.inputType"=="1" || "$field.inputType"=="5" || "$field.inputType"=="10")
				#if("$!field.fieldName"=="LastContractTime")
					<li class="col_hy" #if("$!fieldValue"!="") title="$!fieldValue.substring(0,10)" #end #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>#if("$!fieldValue"!="")$!fieldValue.substring(0,10) #end&nbsp;</li>
				#elseif("$!field.fieldName"=="createTime"||"$!field.fieldName"=="NextFollowTime")
					<li class="col_hy" #if("$!fieldValue"!="") title="$!fieldValue" #end #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$!fieldValue&nbsp;</li>
				#else
					<li class="col_hy" title="$!globals.getEnumerationItemsDisplay("$!field.refEnumerationName","$!fieldValue")" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$!globals.getEnumerationItemsDisplay("$!field.refEnumerationName","$!fieldValue")&nbsp;</li>
				#end
			#elseif("$field.inputType"=="2")
			#elseif("$field.inputType"=="14")
				#set($deptName = $deptMap.get($!fieldValue))
				<li class="col_hy" title="$deptName" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$!deptName</li>
			#elseif("$field.inputType"=="15")
				#set($empFullName = $!globals.getEmpFullNameByUserId($!fieldValue))
				<li class="col_hy" title="$empFullName" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$!empFullName</li>
			#elseif("$field.inputType"=="20")
			#elseif("$field.inputType"=="100")
				#if("$field.fieldType"=="5" || "$field.fieldType"=="6")
					<li class="col_hy" title="$!client.get("$field.fieldName")" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$!fieldValue</li>
				#elseif("$!field.fieldName"=="createBy")
					<li class="col_hy" title="$!globals.getEmpFullNameByUserId($!fieldValue)" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$globals.getEmpFullNameByUserId($!fieldValue)&nbsp;</li>
				#end
			#else
				<li class="col_hy" title="$!client.get("$field.fieldName")" #if("$!field.width"!="" && $!field.width!=0) style="width:${field.width}px;" #end>$!fieldValue</li>
			#end
		#end
		#set($colIndex=$colIndex+1)
		#end
	
	#end
	#set($contentWidth = 0)
	<!-- 
	#if("$!moduleBean.isUserLabel" == "1")
	<li class="col_hy" style="width: 140px;"  onmouseover="editLabel(this)"  id="${rowIndex}_label" >
		#if($!client.get("labelId") !="")
		#foreach($labelId in $!client.get("labelId").split(","))
			#if("$!labelMap.get($labelId).labelName" !="")
			<div labelId="$labelMap.get($labelId).id" tabindex="0"  class="nui-tag nui-tag-1" onmousemove="" >
				<span onclick="searchLabel('$labelId')" onmouseover="showDel(this,'$labelMap.get($labelId).labelColor','name')" onmouseout="hideDel(this,'name')" class="nui-tag-text" style=" background-color:$labelMap.get($labelId).labelColor">$!labelMap.get($labelId).labelName</span>
			    <span id="$labelId" onmouseover="showDel(this,'$labelMap.get($labelId).labelColor','del')" onmouseout="hideDel(this,'del')" onclick="delLabel(this)" tabindex="0" class="nui-tag-close nui-close" style="display:none;background-color:$labelMap.get($labelId).labelColor" title="取消标签"><b class="nui-ico" >x</b></span>
			</div>
			#end
		#end
		#end
		<img src="/style/images/client/crmLabel.gif" id="imgEdit"  class="labelEdit" style="float: right;" title="编辑" onclick="openLabelEdit(this,'${rowIndex}_label')" />
	</li>
	#end
	 -->
	#if("$!moduleBean.workflow" == "100")<li class="col_hy" style="width:80px;text-align:center;"  onmouseover="editLabel(this)"  id="${rowIndex}_label" >审核状态</li>#end
	<li class="col_hy">	
		$globals.getTimeDiff(null,$client.get("LastContractTime"))天








	</li>
		
	#if("$!OAWorkFlow" == "true")
		<!-- 
		#set($flowStatus = "")
		#if($client.get("workFlowNode")=="-1")
			#set($flowStatus = "完毕")
		#elseif($client.get("workFlowNode")=="0")
			#set($flowStatus = "待审核")
		#else
			#set($flowStatus = "审核中")
			
		#end
		<li class="col_hy">
			<em style="width:90px;"><i>$flowStatus</i>
			#if("$!OAWorkFlow" == "true"&&$client.get("workFlowNode")!="-1"&&$client.get("checkPersons").indexOf(";${LoginBean.id};")>-1)
				#set($keyId = $client.get("id"))
				<b class="approve" title='审核' onclick="approve('$keyId','approve','$clientTableName')"></b> 
			#end
			</em>
			
		</li>
		 -->
		#set($flowInfo = "")
		#if($client.get("workFlowNode")=="0")
			#set($flowInfo = "待审核")
			#if($!client.get('WorkFlowStatus') == "back")
				#set($flowInfo = $flowInfo+"(审核失败)")
			#end
			
		#elseif($client.get("workFlowNode")=="-1")
			#set($flowInfo = "审核完毕")
		#else
			#set($checkPerson="")
			#foreach($str in $client.get("checkPersons").split(";"))
				#if("$!str" !="")
					#set($checkPerson=$checkPerson+$globals.getEmpFullNameByUserId($!str)+",")
				#end
			#end
			#if("$!checkPerson" !="")
				#set($checkPerson= $globals.subEndwith("$checkPerson",$checkPerson.length()))
			#end
			#set($workFlowNode = "")
			#set($workFlowNode = $!client.get("workFlowNode"))
			#set($flowInfo = $!globals.getCurrNodeName("$designId","$workFlowNode")+" ("+($checkPerson)+")")
		#end
			<li class="col_hy"><em style="width:100px;cursor: pointer;#if($!client.get('WorkFlowStatus') == "back")color: red;#end" onclick="workFlowDepict('$client.get("id")')" title='$flowInfo'>$flowInfo</em></li>
	#end	
		
		
		
<div class="minfo">
<p>
#set($minfoFields = $!moduleViewBean.detailFields+$!minfoTranFields)
#foreach($col in $!moduleViewBean.detailFields.split(","))
	#if("$col" != "")
	#set($field=$globals.getField($col,$clientTableName,$contactTableName))
	#if("$!client.get($field.fieldName)"!="")
		#if($field.inputType==1)
			#if("$field.fieldName" == "LastContractTime")
				<span class="minfoSpan">$globals.getColName($col,$clientTableName,$contactTableName)：</span>$!client.get("$field.fieldName")&nbsp;
			#else
				#if("$!client.get($field.fieldName)" != "0")
				#set($fieldVal = $!client.get("$field.fieldName") + "")
				<span class="minfoSpan">$globals.getColName($col,$clientTableName,$contactTableName)：</span>$!globals.getEnumerationItemsDisplay($field.refEnumerationName,$fieldVal)&nbsp;
				#end
			#end
		#elseif($field.inputType==5 || $field.inputType==10)
			<span class="minfoSpan">$globals.getColName($col,$clientTableName,$contactTableName)：</span>
			#foreach($val in $!client.get("$field.fieldName").split(","))
				$!globals.getEnumerationItemsDisplay($field.refEnumerationName,$val)&nbsp;,
			#end
		#elseif("$field.inputType" == "14")
			<span class="minfoSpan">$globals.getColName($col,$clientTableName,$contactTableName):</span>#foreach($row in $!client.get("$field.fieldName").split(","))#if("$row" != "")$deptMap.get("$row"),#end#end
		#elseif("$field.inputType" == "15")
			<span class="minfoSpan">$globals.getColName($col,$clientTableName,$contactTableName):</span>#foreach($row in $!client.get("$field.fieldName").split(","))#if("$row" != "")$globals.getEmpFullNameByUserId($row),#end#end
		#else
			#if("$field.fieldName" == "Trade")
				<span class="minfoSpan">$globals.getColName($col,$clientTableName,$contactTableName)：</span>$!workProfessionMap.get($!client.get("$field.fieldName"))&nbsp;
			#elseif("$field.fieldName" == "District")
				<span class="minfoSpan">$globals.getColName($col,$clientTableName,$contactTableName)：</span>$!districtMap.get($!client.get("$field.fieldName"))&nbsp;
			#elseif("$!field.fieldName"=="createBy")
				<span class="minfoSpan">$globals.getColName($col,$clientTableName,$contactTableName)：</span>$globals.getEmpFullNameByUserId($!client.get("$field.fieldName"))&nbsp;
			#else
				<span class="minfoSpan">$globals.getColName($col,$clientTableName,$contactTableName)：</span>$globals.replaceHTML($!client.get("$field.fieldName"))&nbsp;
			#end
		#end
	#end
	#end
#end

</p>
<ul>
	#if("$!contactMap.get($client.get('id'))"=="")
		<li></li>
	#else
		#foreach($contact in $contactMap.get($client.get("id")))
		<li><div class="f_l t"><span class=" all_icon i_linkman">联系人</span></div>
			$!contact.get("UserName")：





			#foreach($col in $moduleViewBean.detailFields.split(","))
				#set($colName=$col.replaceAll("contact",""))
				#if("$!contact.get($colName)"!="" && "$!colName"!="UserName")
					#if("QQ"=="$!colName")
					<li><span class="all_icon i_linkqq">QQ</span><a  href="tencent://message/?uin=$!contact.get($colName)&Site=$!contact.get("UserName")&Menu=yes">$!contact.get($colName)</a>,</li>
					#elseif($globals.getField($colName,$clientTableName,$contactTableName).inputType == 1)
						#set($enumerVal = $!contact.get($colName) + "")
						$globals.getColName($colName,$clientTableName,$contactTableName)：$!globals.getEnumerationItemsDisplay($globals.getField($colName,$clientTableName,$contactTableName).refEnumerationName,$enumerVal)&nbsp;,
					#else	
						$globals.getColName($colName,$clientTableName,$contactTableName)：$!contact.get($colName),
					#end
				#end
			#end
		</li>
		#end
	#end
</ul>
<div class="moperate">
	#if($LoginBean.operationMap.get("/OATaskAction.do").query())
	<em class="btn-s addCRMTaskAssign">
		任务分派
		<b class="icon16"></b>
	</em>
	#end
	#if($LoginBean.operationMap.get("/OACalendarAction.do?crmEnter=true").query()||$LoginBean.operationMap.get("/OACalendarAction.do?operation=4&queryFlag=List&crmEnter=true").query())
	<em class="btn-s addOACalendar">
		日程
		<b class="icon16"></b>
	</em>
	#end
	#if($LoginBean.operationMap.get("/CRMBrotherAction.do?tableName=CRMSaleFollowUp").add())
	<em class="btn-s addCRMSaleFollowUp">
		联系记录
		<b class="icon16"></b>
	</em>
	#end
	 <!--
	#if($client.get("Status")!="4")
	<a href="javascript:" class="btnbg btn_sorg completeBtn"><span class=" all_icon i_sstar"></span>成交</a>
	#end
	<a href="javascript:" class="btnbg btn_sorg attentBtn"><span class="all_icon i_sheard"></span>#if("$!attentMap.get($client.get('id'))"=="")关注#else取消#end</a>
	 -->
</div>
	<iframe width="90%" frameborder=no scrolling="no" id="Frame_$client.get('id')"></iframe>
</div>
</ul>
#set($rowIndex=$rowIndex+1)
#end
</div>
</div>
</div>
</div>
$!pageBar
<div class="pageright">
<div class="pagerightbtn" style="display: none;"></div>
</div>
<div class="pop-weixin" style="display: none;">
	<div class="a-title">
	   <span class="lf pr">
	   <b class="icons pa"></b>微信信息</span>
	</div>
	<div class="d-review">
		<textarea class="t-area" id="weixinContents" name="weixinContents"></textarea>
	</div>
	<div class="btn-items-wp">
		<span class="btn-items celBtn" onclick="closeWeixin();">取消</span>
		<span class="btn-items conBtn" onclick="weixinSendMsg();">确定</span>
	</div>
</div>
</form>
<!--  -->
<div class="pagetop">
<a id="html_top" class="pagetopbtn" title="返回顶部"></a>
</div>
</div>
</div>

<div class="add-calendar" id="addCalendar" style="display:none;"></div>
<div class="addWrap pop-layer" id="addTaskDiv" ></div>


</body>
<script type="text/javascript">
$(document).ready(function(){
#if("$!conMap.get('expandStatus')"=="open")
$("#searchExpand").click();
#end
});
</script>

<!-- 
<div class="nui-menu" style="display: none;position:relative;z-index: 99;" id="labelShow" onmouseover="showLabelEdit('labelShow')" onmouseout="hiddenLabelEdit('labelShow')">
	<div id="labelContDiv">
		#foreach($label in $labelList)
    	<div class="nui-menu-item" onclick="selLabel(this,'$label.id','$label.labelName','$label.labelColor')">
        	<div class="nui-menu-item-link">
            	<span class="nui-menu-item-icon"><input id="${label.id}_box" type="checkbox"/></span>
               	<span class="nui-menu-item-text"><b class="nui-ico nui-ico-tag" style="background:$label.labelColor;"></b>$label.labelName</span>
            </div>
        </div>
        #end
     </div>
     #if($LoginBean.operationMap.get("/CRMClientAction.do").update())
     <div class="nui-menu-split nui-split"></div>
	     <div class="nui-menu-item">
	     	<div class="nui-menu-item-link">
	        	<span class="nui-menu-item-text2" onclick="delAllLabel()">取消所有标签</span>
	        </div>
	     </div>
	     <div class="nui-menu-split nui-split"></div>
	     <div class="nui-menu-item">
	     	<div class="nui-menu-item-link">
	        	<span class="nui-menu-item-text2" onclick="addLabel();">新建标签</span>
	        </div>
	     </div>
	     <div class="nui-menu-split nui-split"></div>
	     <div class="nui-menu-item">
	     	<div class="nui-menu-item-link">
	        	<span class="nui-menu-item-text2" onclick="labelManager()">管理标签</span>
	        </div>
	      </div>
     #end
</div>
 -->
</html>
