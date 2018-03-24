<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>AIO</title>
#end
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/workflow_list.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	if(typeof(top.junblockUI)!="undefined"){
	  top.junblockUI();
   	}
	$(".tags").click(function(){
		location.href = "/OAMyWorkFlow.do?operation=4&approveStatus="+$(this).attr("flowType")+"&addTHhead="+$("#addTHhead").val();
		jblockUI();
	});
	$(".main_search .col ul li").click(function (){
		$("#"+$(this).attr("name")).val($(this).attr("enumvalue"));
		if("timeType"==$(this).attr("name")){
			if($(this).attr("enumvalue")=="fix"){
				$("#fixTimeSpan").show();
				return ;
			}else{
				$('input[name="beginTime"]').val(getDateStr($(this).attr("enumvalue")));
			}
		}
		jblockUI();
		form.submit();
	});
	
	/*列表表头不在可见区域时，固定表头*/
	$("#clientId").scroll(function(){ 
		var maintop = $("#maintop");
		if($("#titleId").position().top<=25){
			maintop.addClass("maintop_fix");
			$(".pagetop").show();
		}else{
			maintop.removeClass("maintop_fix");
			$(".pagetop").hide();
		}
	});
	
	/*返回顶部*/
	$("#html_top").click(function(){
		$("#clientId").animate({scrollTop: 0}, 500);	
	});
	 $(".c_operate_l>div").hover(
	    function() { $(this).find("span").stop().animate({"width":"130px"},300).find("p").fadeIn("300");},
	    function() { $(this).find("span").stop().animate({"width":"40px"},300).find("p").fadeOut("300");}
	 );
	$(".col-long-hy").hover(
		function() { $(this).find(".po-list").show();},
	    function() { $(this).find(".po-list").hide();}
	)
});

function jblockUI(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({message: "<img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍后。。。</div>",css:{ background: 'transparent'}}); 
	}
}

function pageSubmit(pageNo){
	$("#pageNo").val(pageNo);
	jblockUI();
	form.submit();
}

function getDateStr(dayCount) { 
	if(dayCount=="") return "";
	var dd = new Date(); 
	dd.setDate(dd.getDate()+parseInt(dayCount));
	var y = dd.getFullYear()+""; 
	var m = (dd.getMonth()+1)+"";
	var d = dd.getDate()+""; 
	return y+"-"+(m.length==1?"0"+m:m)+"-"+(d.length==1?"0"+d:d); 
}
 
function beforeSubmit(){
	jblockUI();
	form.submit();
}

/*新转交窗口*/
function deliverTo(url){
	asyncbox.open({
		id:'dealdiv',url:url,title:'转交',width:650,height:370,
        btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
				if(opener.beforSubmit(opener.form)){
					opener.form.submit();
				}
				return false;
			}
　　  　}
　 });
}

/*催办弹出框*/
function hurryTransDialog(param){
	var url = "/vm/oa/workflow/noteModel.jsp?"+encodeURI(param);
	asyncbox.open({
		id:'deliverTo',url:url,title:'$text.get("workflow.msg.transactionWarmKind")',
　　 　 width : 750,height : 300,btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    	if(action == 'ok'){
				if(opener.submitModenew(opener.form)){
					opener.form.submit();
				}
				return false;
		  	}
　   　  }
	});
}

function dealAsyn(){
	jQuery.close('deliverTo');
}
function dealAsyncbox(){
	jQuery.close('dealdiv');
	window.location.reload();
}

function deliverToAll(){ 
	if(!isChecked("keyId")){
		alert('请选择至少一条记录');
		return false;
	}
	tn="";
	keyIds="";
	errorstr = false;
	jQuery("input[name='keyId']:checked").each(function(){
		keyIds += $(this).val()+",";
		if(tn==""){
			tn = $(this).attr("tn");
		}else if(tn !=$(this).attr("tn")){
			errorstr = true;
		}
	});
	if(errorstr){
		alert("所选流程包含不同类型不可批量审核");
		return;
	}
	keyIds = keyIds.substring(0,keyIds.length-1);
	
	var defi = "&defineInfo=";
	
	
	checkDialog("/OAMyWorkFlow.do?keyId="+keyIds+"&tableName=$!tableName&operation=$globals.getOP("OP_AUDITING_PREPARE")&fromPage=erp&type=deliverToAll");
	
	return;
}
function checkDialog(url){
	asyncbox.open({
		id:'deliverTo',url:url,title:'审核',width:650,height:370,
        btnsbar:jQuery.btn.OKCANCEL,
	    callback:function(action,opener){
		    if(action == 'ok'){
				if(opener.beforSubmit(opener.form)){
					opener.form.submit();
				}
				return false;
			}
　　     }
　 });
}
function deleteFlow(){
	
	if(!isChecked("keyId")){
		alert('请选择至少一条记录');
		return false;
	}
	isError = false;
	$("input[name=keyId]:checked").each(function(){
		if($(this).attr("currentNode") != "0" && !isError){
			isError = true;
			lname = $(this).parents("ul").find("em").text();
			lname = lname.substring(0,lname.length-1);
			alert("第"+lname+"行工作流不是开始状态，不可以删除");
			return 
		}
	});
	if(isError){
		return;
	}
	form.operation.value=3;
	if(!confirm('确定删除吗')){
		form.operation.value = 4;
		cancelSelected("input");
		return false;
	}

	form.submit();
}

function flowDepict(applyType,keyId){ 
	window.open("/OAMyWorkFlow.do?keyId="+keyId+"&operation=5&applyType="+applyType,null,"height=570, width=1110,top=50,left=100");
}

function showType(){
	var displayName = "流程类型"; 
	var urlstr = "/UserFunctionAction.do?selectName=OAMyWorkFlowType&operation=22&popupWin=Popdiv&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName;
	asyncbox.open({id:'Popdiv',title:displayName,url:urlstr,width:750,height:470});
}

function exePopdiv(str){
	if(typeof(str)=="undefined") return ;
	var note = str.split("#;#") ;
	$("#tableFullName").val(note[0]);
	jblockUI();
	form.submit();
}


function employeePopup(inputName){
	var url ="/Accredit.do?popname=userGroup&leavePerson=yes&inputType=radio&chooseData="+$("#employee").val();
	var title = '选择职员'
	if(inputName=="deptFullName"){
		url ="/Accredit.do?popname=deptGroup&inputType=radio&chooseData="+$("#departMent").val();
		title = '选择部门';
	}
	asyncbox.open({
		id:"employeePopup",url:url,title:title,width:780,height:435,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    	if(action == 'ok'){
    			var str = opener.strData;
	    		var existIds = $("#"+inputName).val();
	    		var newIds = "";
	    		var arrEmp = str.split("|");
	    		/*for(var i=0;i<arrEmp.length;i++){
	    			var strEmp = arrEmp[i].split(";")[1];
					if(typeof(strEmp)!="undefined" && existIds.indexOf(strEmp+",")==-1){
						newIds += strEmp + ",";
					}
	    		}*/
	    		$("#"+inputName).val(arrEmp[0].split(";")[1]);
	    		jblockUI();
	    		form.submit();
	    	}
	    }
　  });
}

function fillData(datas){
	if(typeof(datas)=="undefined") return ;
	var note = datas.split(";") ;
	var inputName = "";
	if(note[3]!="undefined"){
		inputName = "empFullName";
	}else{
		inputName = "deptFullName";
	}
	//var existIds = $("#"+inputName).val();
	//if(typeof(note[1])!="undefined" && existIds.indexOf(","+note[1]+",")==-1){
		$("#"+inputName).val(note[1]);
	//}
	jblockUI();
	form.submit();
}


/*删除职员*/
function deleteEmp(obj){
	var empObj = $(obj).parent();
	//$("#"+empObj.attr("name")).val().replace(empObj.attr("id")+",","")
	$("#"+empObj.attr("name")).val("");
	$(obj).parent().remove();
	jblockUI();
	form.submit();
}

function openPopup(strUrl,strTitle){
	var width = 1024;
	if($(window).width()>1024) width = 1150;
	height = 500;
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	openPop('PopMainOpdiv',strTitle,strUrl,width,height,false,true);
}
function submitQuery(){
	beforeSubmit();
}
</script>
<style type="text/css">
em,i{font-style:normal;}
.sbtn a{float:left;display:inline-block;width:65px;}
.op{position:relative;}
.op .isd_dv{display:none;border-radius:4px;left:-70px;top:32px;position:absolute;z-index:500;border:1px solid #666;width:250px;height:80px;background:#fff;box-shadow:0 0 10px rgba(0,0,0,.5);cursor:default;zoom:1;}
.op .isd_dv .jt_b{width:20px;height:11px;position:absolute;background:url(/style/images/client/784.png) no-repeat 0 0;left:45%;top:-11px;}
.op .isd_dv .wd_em{display:block;text-align:center;margin:15px 0 0 0;}
.op .isd_dv .btn_sp{display:inline-block;width:100%;margin:12px 0 0 0;background:#f5f5f5;border-top:1px solid #ddd;border-radius:0 0 4px 4px;}
.op .isd_dv .btn_sp i{color:#025494;cursor:pointer;height:29px;line-height:27px;float:left;width:33.2%;display:inline;text-align:center;border-top:1px solid #fff;border-left:1px solid #ddd;margin-right:-1px;text-decoration:none;}
.op .isd_dv .btn_sp i:hover{background-color:#ebebeb;}
</style>
</head>
<body class="body_ef" onload="showStatus();">
#if("$!addTHhead" == "true")
	#parse("./././body2head.jsp")
#end
<div id="wrapper">
<div class="cont cf">
<div class="c_operate_l">
	#if($globals.getMOperation("/OAWorkFlowCreateAction.do?operation=4").query())
	<div class="i_add" id="addBtn" onclick="mdiwin('/OAWorkFlowCreateAction.do?operation=4&src=menu','新建工作流');">
		<span>
			<b class="icon32"></b><em>新建工作流</em>
			<p class="bg-p"></p>
		</span>
	</div>
	#end
	#if($globals.getMOperation("/WorkConsignAction.do").query())
	<div class="i_reset" id="setBtnNew" onclick="mdiwin('/WorkConsignAction.do?src=menu','工作流委托');">
		<span>
			<b class="icon32"></b><em>工作流委托</em>
			<p class="bg-p"></p>
		</span>
	</div>
	#end
	#if($globals.getMOperation("/OAMyWorkFlowQuery.do?type=delete&operation=4").query())
	<div class="i_edit" id="publicBtn" onclick="mdiwin('/OAMyWorkFlowQuery.do?type=delete&operation=4&src=menu','工作流回收站');">
		<span>
			<b class="icon32"></b><em>工作流回收</em>
			<p class="bg-p"></p>
		</span>
	</div>
	#end
	#if($globals.getMOperation("/OAWorkFlowTempQueryAction.do").query())
	<div class="i_star" id="attentionBtn" onclick="mdiwin('/OAWorkFlowTempQueryAction.do?src=menu','工作流设计');">
		<span>
			<b class="icon32"></b><em>工作流设计</em>
			<p class="bg-p"></p>
		</span>
	</div>
	#end
</div>
<div class="c_main winw1200 f_l" id="clientId">
<a name="html_top"></a>
<script type="text/javascript">
	$("#clientId").height($(window).height()-55).width($(window).width()-80);
</script>
<form name="form" action="/OAMyWorkFlow.do" method="post">
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_QUERY")"/>
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value=""/> 
<input type="hidden" id="loginId" name="loginId" value=""/>
<input type="hidden" id="type" name="type" value="deliverToAll"/>
<input type="hidden" id="approveStatus" name="approveStatus" value="$!approveStatus"/>
<input type="hidden" id="empFullName" name="empFullName" value="$!workFlowForm.empFullName"/>
<input type="hidden" id="deptFullName" name="deptFullName" value="$!workFlowForm.deptFullName"/>
<input type="hidden" id="tableFullName" name="tableFullName" value="$!workFlowForm.tableFullName"/>
<input type="hidden" name="addTHhead" id="addTHhead" value="$addTHhead"/>
<div class="main_search">
	<div class="col">
		<span>模糊查询：</span>
		<div class="inpsearch"> 
		<input id="keyWord" name="keyWord" type="text" value="$!workFlowForm.keyWord" onKeyDown="if(event.keyCode==13) beforeSubmit();"/><a class="inpsearch_btn" href="javascript:beforeSubmit();"></a></div>
		<div class="stnn">
		<div class="sbtn sbtn110" id="sdepartMent"><em class="a"></em><a href="javascript:" onclick="showType();" title="选择流程类型">选择流程类型</a><em class="c"></em></div>
		<div class="sbtn sbtn110" id="semployee"><em class="a"></em><a href="javascript:" onclick="employeePopup('empFullName');" title="选择申请人">选择申请人</a><em class="c"></em></div>
		<div class="sbtn sbtn110" id="sdistrict"><em class="a"></em><a href="javascript:" onclick="employeePopup('deptFullName');" title="选择申请部门">选择申请部门</a><em class="c"></em></div>
		</div>
	</div>
	#if("$!workFlowForm.empFullName"!="" || "$!workFlowForm.deptFullName"!="" || "$!workFlowForm.tableFullName"!="")
	<div class="col employeeDiv" id="employee_select">
		<span>过滤条件：</span>
		<ul>
		#foreach($empName in $!workFlowForm.tableFullName.split(","))
		#if("$!empName"!="")
		<div class="navigationShow" id="$empName" name="tableFullName">$empName<b class="icons" onclick="deleteEmp(this);"></b></div>
		#end
		#end
		#foreach($empName in $!workFlowForm.empFullName.split(","))
		#if("$!empName"!="")
		<div class="navigationShow" id="$empName" name="empFullName">$empName<b class="icons" onclick="deleteEmp(this);"></b></div>
		#end
		#end
		#foreach($deptName in $!workFlowForm.deptFullName.split(","))
		#if("$!deptName"!="")
		<div class="navigationShow" id="$!deptName" name="deptFullName">$!deptName<b class="icons" onclick="deleteEmp(this);"></b></div>
		#end
		#end
		</ul>
	</div>
	#end
	<div class="col">
		<span>申请日期：</span>
		<ul>
			<li #if("$!workFlowForm.timeType" == "") class="sel" #end name="timeType" enumValue=""><a>全部</a></li>
			<li #if("$!workFlowForm.timeType" == "0") class="sel" #end name="timeType" enumvalue="0"><a>今天</a></li>
			<li #if("$!workFlowForm.timeType" == "-3") class="sel" #end name="timeType" enumvalue="-3"><a>3天内</a></li>
			<li #if("$!workFlowForm.timeType" == "-7") class="sel" #end name="timeType" enumvalue="-7"><a>7天内</a></li>
			<li #if("$!workFlowForm.timeType" == "-30") class="sel" #end name="timeType" enumvalue="-30"><a>30天内</a></li>
			<input type="hidden" id="timeType" name="timeType" value="$!workFlowForm.timeType"/>
			<li #if("$!workFlowForm.timeType" == "fix") class="sel" #end name="timeType" enumvalue="fix"><a>指定时间</a></li>
			<div #if("$!workFlowForm.timeType" != "fix")style="display:none;"#end id="fixTimeSpan"><input type="text" name="beginTime" onClick="WdatePicker({lang:'$globals.getLocale()'})"  onKeyDown="if(event.keyCode==13) submitQuery();" value='$!workFlowForm.beginTime' />&nbsp; 至 &nbsp;<input type="text" name="endTime" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value='$!workFlowForm.endTime'/> <a onclick="javascript:beforeSubmit();" style="cursor:point;">查询</a></div>
		</ul>
	</div>
	<div class="col">
		<span>关联类型：</span>
		<ul>
			<li #if("$!workFlowForm.flowBelong" == "all") class="sel" #end name="flowBelong" enumvalue=""><a>全部</a></li>
			<li #if("$!workFlowForm.flowBelong" == "self") class="sel" #end name="flowBelong" enumvalue="self"><a>我发起的</a></li>
			<li #if("$!workFlowForm.flowBelong" == "handle") class="sel" #end name="flowBelong" enumvalue="handle"><a>我经办的</a></li>
			<input type="hidden" id="flowBelong" name="flowBelong" value="$!workFlowForm.flowBelong"/>
		</ul>
	</div>
	<!-- 隐藏条件 -->
	<div style="display:none;" id="showSearch">
		
	</div>
</div>
<div class="maintablearea">

<div class="bd" id="maintop">
	<div class="hd">
		<div class="m_l f_l workflow-status">
			<span class="tags#if("$!approveStatus"=="transing") tags-sel#end" flowType="transing">待审</span>
			<span class="tags#if("$!approveStatus"=="consignation") tags-sel#end" flowType="consignation">委托</span>
			<span class="tags#if("$!approveStatus"=="transing2") tags-sel#end" flowType="transing2">办理中</span>
			<span class="tags#if("$!approveStatus"=="finish") tags-sel#end" flowType="finish">已办结</span>
		</div>
		<div class="m_r f_r">
			<span class="hBtns" id="deleteBtn" onclick="beforeSubmit();">刷新</span>
			<span class="hBtns" id="deleteBtn" onclick="deleteFlow();">删除</span>
			<span class="hBtns" id="deliverToAll" onclick="deliverToAll();">批量审核</span> 
		</div>
		<div class="clear"></div>
	</div>
	<ul class="maintop">
		<em class="f_l"></em>
		<input type="checkbox" id="selAll" name="selAll"  onClick="checkAll('keyId')" style="float:left;margin-top:10px;" />
		<li class="col_hy">申请时间</li>
		<li class="col_hy">更新时间</li>
		<li class="col_hy">申请人</li>
		<li class="col-long-hy title-hy">主题</li>
		<li class="col-long-hy col-step-li">
		步骤：<font color="red">上一节点</font> | <font color="#3bc13b">当前节点</font>
		<!-- 上一结点&nbsp;|&nbsp;当前结点 --></li>
		#if($!approveStatus=="finish")
		<li class="col_hy">完成时间</li>
		#end
		<li class="col_hy">操作</li>
	</ul>
</div>

<div class="bd" id="titleId">
	#foreach($flow in $list)
	<ul class="col">
		<em class="f_l">$!velocityCount、</em>
		<input class="c-box" name="keyId" type="checkbox" value="$!flow.billId" tn="$!flow.tableName" currentNode="$!flow.currentNode" title="第$velocityCount行"/>
		<li class="col_hy"><p>$!flow.applyDate</p> <p>$!flow.createTime</p></li>
		<li class="col_hy"><p>$!flow.lastUpdateTime</p> <p>$!flow.finishTime</p></li>
		<li class="col_hy"><p>$!flow.applyBy</p><p> $!flow.department</p></li>
		<li class="col-long-hy title-hy">
			<p>$!flow.templateName</p>
		
			#if($!workFlowDesignBeans.get($!flow.applyType).getFlowNodeMap().get("$!flow.currentNode")&&$!flow.currentNode!=-1&&$!flow.checkPerson.indexOf(";$!LoginBean.id;")>=0)
		    	#if("$!flow.workFlowType"!="1")
		     		<a class="de-list" href="javascript:openPopup('/UserFunctionAction.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")','$!flow.templateName');">$!flow.applyContent</a>		      			 
		    		<a class="po-list" href="javascript:openPopup('/UserFunctionAction.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")','$!flow.templateName');">$!flow.applyContent</a>
		    	#else
		    	  	<a class="de-list" href="javascript:openPopup('/OAWorkFlowAction.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")','$!flow.templateName');">$!flow.applyContent</a>	      			 	
		    		<a class="po-list" href="javascript:openPopup('/OAWorkFlowAction.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")','$!flow.templateName');">$!flow.applyContent</a>		      			 	
		    	#end
		    #else
		      	#if("$!flow.workFlowType"!="1")
				    <a class="de-list" href="javascript:openPopup('/UserFunctionAction.do?tableName=$!flow.tableName&noback=true&parentTableName=$!flow.parentTableName&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&designId=$!flow.applyType&approveStatus=$!approveStatus','$text.get("oa.my.workflow")')">$!flow.applyContent</a>
				    <a class="po-list" href="javascript:openPopup('/UserFunctionAction.do?tableName=$!flow.tableName&noback=true&parentTableName=$!flow.parentTableName&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&designId=$!flow.applyType&approveStatus=$!approveStatus','$text.get("oa.my.workflow")')">$!flow.applyContent</a>
			 	#else
			 	  	<a class="de-list" href="javascript:openPopup('/OAWorkFlowAction.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&nextNodeIds=$!flow.nextNodeIds&currNodeId=$!flow.currentNode&&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&designId=$!flow.applyType&approveStatus=$!approveStatus','$text.get("oa.my.workflow")')">$!flow.applyContent</a>	
			 	  	<a class="po-list" href="javascript:openPopup('/OAWorkFlowAction.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&nextNodeIds=$!flow.nextNodeIds&currNodeId=$!flow.currentNode&&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&designId=$!flow.applyType&approveStatus=$!approveStatus','$text.get("oa.my.workflow")')">$!flow.applyContent</a>	      			 	
			 	#end
			#end
		</li>
		<li class="col-long-hy col-step-li">
			<!-- <a class="de-list"  href="javascript:flowDepict('$!flow.applyType','$!flow.billId');">$!flow.flowDepict</a> -->
			<p class="de-list" style="cursor:pointer;" onclick="javascript:flowDepict('$!flow.applyType','$!flow.billId');">
				#if("$!flow.lastNodeName"!="")<font style="color:#606060;"><b class="icons prev-point"></b>$!{flow.lastNodeName}($!{flow.lastNodeCheckperson})</font><br/>#end
				<font #if("$!flow.lastNodeName" =="") class="single-font" #end style="color:#3bc13b;"><b class="icons this-point"></b>$!{flow.currNodeName}#if("$!flow.checkPersonName"!="")($!{flow.checkPersonName})#end</font>
			</p>
			<!-- <a class="po-list" href="javascript:flowDepict('$!flow.applyType','$!flow.billId');">$!flow.flowDepict</a> -->
		</li>
		#if($!approveStatus=="finish")
		<li class="col_hy"><p>$!flow.lastUpdateTime</p><p>$!flow.finishTime</p></li>
		#end
		<li class="col_hy"> 
			  #if($!flow.lastCheckPerson ==$LoginBean.id && $!flow.currentNode!=-1)	  
			  	<a href=javascript:if(confirm('$text.get("oa.workFlow.Cancel")'))location.href='/OAMyWorkFlow.do?nextStep=cancel&lastNodeId=$!flow.lastNodeId&keyId=$!flow.id&currNode=$!flow.currentNode&designId=$!flow.applyType&operation=$globals.getOP("Op_AUDITING")'>$text.get("common.msg.Cancel")</a>&nbsp;	
			  #end  			  
		      #if($!workFlowDesignBeans.get($!flow.applyType).getFlowNodeMap().get("$!flow.currentNode")&&$!flow.currentNode!=-1&&$!flow.checkPerson.indexOf(";$!LoginBean.id;")>=0)
	      		  #if("$!flow.workFlowType"!="1")
	      		  	<a href="javascript:openPopup('/UserFunctionAction.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")&designId=$!flow.applyType&approveStatus=$!approveStatus','$!flow.templateName');">$text.get("oa.myTransaction.Disposeing")</a>&nbsp;		      			 
	      		  #else
	      		 	 <a href="javascript:openPopup('/OAWorkFlowAction.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&keyId=$!flow.billId&operation=$globals.getOP("OP_UPDATE_PREPARE")&designId=$!flow.applyType','$!flow.templateName');">$text.get("oa.myTransaction.Disposeing")</a>&nbsp;		      			 	
	    	  	  #end
			      #if($!flow.nextNodeIds.length()>0&&$!flow.currNodeDetID) 
				  	<a href="javascript:deliverTo('/OAMyWorkFlow.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&keyId=$!flow.billId&currNodeId=$!flow.currentNode&nextNodeIds=$!flow.nextNodeIds&department=$!flow.departmentCode&designId=$!flow.applyType&operation=$globals.getOP("OP_AUDITING_PREPARE")&winCurIndex=$!winCurIndex')">$text.get("common.lb.DeliverTo")</a>&nbsp;		      		
				  #else
				  &nbsp;&nbsp;&nbsp;&nbsp;
				  #end
			  #else
				  #if($!workFlowDesignBeans.get($!flow.applyType).getFlowNodeMap().get("$!flow.currentNode")&&!$!flow.currentNode.equals("-1")&&$LoginBean.id.equals($flow.createBy))
				  	 <a href="javascript:hurryTransDialog('createBy=$!flow.createBy&applyType=$!flow.applyType&tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&keyId=$!flow.id&checkPerios=$!flow.checkPerson&approveStatus=transing&nextNodeIds=$!flow.nextNodeIds&currNodeId=$!flow.currentNode&billId=$!flow.billId&oaTimeLimit=$!flow.benchmarkTime&oaTimeLimitUnit=$!flow.oaTimeLimitUnit&content=$!flow.applyContent&applyBy=$!flow.applyBy&moduleName=#if($!workFlowInfo.get("$!flow.applyType").getLanguage())$!workFlowInfo.get("$!flow.applyType").getLanguage().get($globals.getLocale())#else$!workFlowInfo.get("$!flow.applyType").getModuleName()#end')">$text.get("common.lb.transaction")</a>&nbsp;	
				  #end
				  #if("$!flow.workFlowType"!="1")
				  	 <a href="javascript:openPopup('/UserFunctionAction.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&currNodeId=$!flow.currentNode&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&designId=$!flow.applyType&approveStatus=$!approveStatus','$!flow.templateName');">$text.get("common.lb.detail")</a>&nbsp;
				  #else
				  	<a href="javascript:openPopup('/OAWorkFlowAction.do?tableName=$!flow.tableName&parentTableName=$!flow.parentTableName&keyId=$!flow.billId&operation=$globals.getOP("OP_DETAIL")&designId=$!flow.applyType','$!flow.templateName');"> $text.get("common.lb.detail")</a>&nbsp;
				  #end
			  #end
		</li>
		<div class="clear"></div>
	</ul>
	#end
</div>
</div>
</div>
</div>
$!pageBar
</form>
	<div class="pagetop">
		<a id="html_top" class="pagetopbtn" title="返回顶部"></a>
	</div>
</div>
</div>
</body>
</html>
