<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>AIO</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/client_brother.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/crm/crm_taskAssign.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/popOperation.js"></script>
<style type="text/css">
.yj_time{float:left;display:inline-block;margin-left:150px}
.yj_time>i{width:110px;display:inline-block;text-align:right;}
.jh_dv{float:left;display:inline-block;margin:0 0 0 30px;}
#taskStatusUL label{cursor: pointer;}
</style>
<script type="text/javascript">
function mainTop(){
	var MWidth = $(".maintop").width();
	var lLen = $(".maintop li").length;
	var LWidth = 0;
	for(var i=0;i<lLen;i++){
		LWidth += $(".maintop li:eq("+i+")").outerWidth(true);
	}
	if(LWidth > MWidth){
		$(".maintop").width(LWidth);
		$("#maintop").width(LWidth);
		$("#form").width(LWidth+10);
	}else{
		$("#maintop").width(MWidth);
	}
	//删除按钮定位
	var hdW = $(".hd").width(),
		WSW =$("#clientId").width();
	if(hdW>WSW){
		$(".hd .m_r").css("right",hdW-WSW+20);
	}else{
		$(".hd .m_r").css("right","20px");
	}
	
}

//导航选项删除
function delSelected(id,delName,divName){
	$("#"+id).remove();//删除DIV
	var delVal = ","+ id + ","//删除的id
	var reVals = "," +$("#" +delName).val();//存在的ID
	reVals = reVals.replace(delVal,",");//替换
	$("#" +delName).val(reVals.substring(1));
	//删除最有一个直接查询

	if($("#"+divName+" ul div[class='navigationShow']").length <= 1){
		//$("#" +delName).val("");
		query();
	}
}
</script>
</head>
<body class="body_ef" onload="mainTop();">
<div id="wrapper">
<div class="cont cf">
<div class="pagetitle">
	<span class="all_icon i_man"></span><h3>任务分派</h3>
</div>
<div class="c_operate_l">
	<a href="javascript:" class=" icon_left i_add" onclick="addTaskAssign();" title="添加" ></a>
</div>
<div class="c_main winw1200 f_l" id="clientId">
<a name="html_top"></a>
<form method="post" name="form" action="/CRMTaskAssignQueryAction.do">
<input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_QUERY')">
<input type="hidden" id="searchTaskStatus" name="searchTaskStatus" value="$!searchForm.searchTaskStatus">
<input type="hidden" id="searchUserId" name="searchUserId" value="$!searchForm.searchUserId">
<input type="hidden" id="clientId" name="clientId" value="$!clientId">
<div class="main_search">
<!-- 模糊查询 Start -->
<div class="inp_wp">
	<span class="inp_t_span">模糊查询：</span>
	<div class="inpsearch"> 
		<select class="inp_sct" name="keyOption" id="keyOption" onKeyDown="if(event.keyCode==13) query();">
			<option value="clientName" >客户名称</option>
		</select>
		<input class="inp_txt"  id="clientName" name="clientName" type="text" class="" value="$!searchForm.clientName" onKeyDown="if(event.keyCode==13) query();"/>
		<a class="inpsearch_btn" href="javascript:query();"></a>
	</div>
	<div class="stnn">
		<div class="sbtn">
			<a class="sbtn_a" onClick="publicPopSelect('userGroup','searchUserId','popDiv','true')">选择执行人</a>
		</div>
	</div>
</div>
<!-- 模糊查询 End -->

#if("$!searchForm.searchUserId" != "")
<div class="col" id="employeeIdDiv">
<span>已选职员：</span>
<ul>
#foreach($col in $!searchForm.searchUserId.split(","))
	#if("$!col" !="")
	<div class="navigationShow"  id="$col"><div >$globals.getEmpFullNameByUserId($col)</div><img  src="/style/images/body/i_007.gif"  onclick="delSelected('$col','searchUserId','employeeIdDiv')"/></div>
	#end
#end
#if($globals.splitLength($!searchForm.searchUserId,",") > 1)<a href="#" class="nomore" onclick="query();" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span></a><a href="#" class="nomore" onclick="clearALL('searchUserId');" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="清空">清空</span></a> #end
</ul>
</div>
#end

<!-- 跟单阶段 Start -->
<div class="col">
	<span>任务状态：</span>
	<ul id="taskStatusUL">
		<li><label val="all" #if("$!searchForm.searchTaskStatus" == "all") class="enumName" #end>全部</label></li>
		<li><label val="-1" #if("$!searchForm.searchTaskStatus" == "-1") class="enumName" #end>未处理</label></li>
		<li><label val="0" #if("$!searchForm.searchTaskStatus" == "0") class="enumName" #end>已处理</label></li>
	</ul>
</div>
<!-- 跟单阶段 End -->


<div class="col">
	<span>指派时间：</span>
	<ul class="time_s">
		<div>
			<input type="text" name="searchStartTime" readonly="readonly" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value='$!searchForm.searchStartTime' />
			至
			<input type="text" name="searchEndTime" readonly="readonly" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value='$!searchForm.searchEndTime'/>
		</div>
		<a href="#" class="nomore" onclick="submitQuery();">
			<span style="float: left;width: 35px;margin-top: -1px;" title="查询" onclick="query();">查询</span>
		</a>
	</ul>
</div>

<div class="maintablearea">
<div class="bd" id="maintop">
<div class="hd d-btns">
	<span class="btn btn-small" onclick="delTaskAssign();">删除</span>
</div>
<ul class="maintop">
	<li class="col_hy" style="width:30px;padding:0 0 0 5px;"></li>
	<li class="col_hy" style="width:20px;"><input type="checkbox" id="checkAll" style="float:left;display:inline-block;margin:4px 0 0 0;"/></li>
	<li class="col_hy" style="line-height:30px;text-align:center;width:100px;">操作</li>
	<li class="col_name" style="line-height:30px;width:70px;"><span >状态</span></li>
	<li class="col_name" style="line-height:30px;width:70px;"><span >处理者</span></li>
	<li class="col_name" style="line-height:30px;width:200px;"><span>任务内容</span></li>
	<li class="col_name" style="line-height:30px;width:200px;"><span>客户名称</span></li>
	<li class="col_name" style="line-height:30px;width:150px;"><span>描述内容</span></li>
	<li class="col_name" style="line-height:30px;width:70px;"><span>优先级</span></li>
	<li class="col_name" style="line-height:30px;width:70px;"><span>任务分类</span></li>
	<li class="col_name" style="line-height:30px;width:70px;"><span>分派者</span></li>
	<li class="col_name" style="line-height:30px;width:150px;"><span>分派时间</span></li>
</ul>
</div>
<div class="bd" id="titleId">
#foreach ($taskAssign in $taskAssignList)
<ul class="col" taskAssignId='$taskAssign.get("id")'>
	
	<li class="num_li">$velocityCount</li>
	<li class="cbox_li">
		
		<input class="check_i" name="keyId" type="checkbox" value="$!taskAssign.get('id')" #if("$!loginBean.id" != "$taskAssign.get('createBy')") disabled="disabled" #end/>
		
	</li>
	
	<li class="col_hy" style="text-align:center;min-width:60px;width:100px;" name="operationLi">
		#if("$!loginBean.id" == $!taskAssign.get('createBy'))
			<a class="update" title='修改' onclick="updTaskAssign('$taskAssign.get("id")');"></a>
		#else
			<a class="update_close" title='修改'></a>
		
		#end 
		<a class="detail" title='详情' onclick="detailTaskAssign('$taskAssign.get("id")');"></a>
	</li>
		#if("$!taskAssign.get('taskStatus')" == "-1") 
			<li class="col_hy finish-li">
				<em style="width:70px;" >未处理</em>
				#if("$!loginBean.id" == $!taskAssign.get('userId') && "$!taskAssign.get('taskStatus')"!="0")
					#set($taskAssignId=$taskAssign.get("id"))
					<b class="finishStatus bg-icons "  title="点击完成" onclick="finishStatus('$taskAssignId')"></b>
				#end	
			</li>
			
		#else 
			<li class="col_hy"><em style="width:70px;" >已处理</em></li>
		#end
		
	<li class="col_hy"><em style="width:70px;" >$globals.getEmpFullNameByUserId($!taskAssign.get("userId"))</em></li>
	<li class="col_hy"><em style="width:200px;" >$!taskAssign.get("content")</em></li>
	<li class="col_hy" ><em style="width:200px;" title='$!taskAssign.get("clientName")'>〖$!taskAssign.get("clientName")<img src="/style/images/client/read_bar.png"  style="cursor: pointer;" onclick="showClient('$!taskAssign.get("clientId")','$!taskAssign.get("clientName")');" title="查看客户详情"/>〗</em></li>
	<li class="col_hy"><em style="width:150px;" >$!taskAssign.get("summary")</em></li>
	<li class="col_hy"><em style="width:70px;" >#if("$!taskAssign.priority"  == "high")高 #elseif("$!taskAssign.priority" == "low")低 #else中 #end</em></li>
	<li class="col_hy"><em style="width:70px;" >$globals.getEnumerationItemsDisplay("CRMTaskType","$!taskAssign.get('taskType')")</em></li>
	<li class="col_hy"><em style="width:70px;" >$globals.getEmpFullNameByUserId($!taskAssign.get("createBy"))</em></li>
	<li class="col_hy"><em style="width:150px;" >$!taskAssign.get("createTime")</em></li>
	
</ul>
#end
</div>

</div>

</div>
</div>
$!pageBar

<div class="pagetop">
<a id="html_top" class="pagetopbtn" title="返回顶部"></a>
</div>

 <div class="brother_table" id="brotherid" style="display:block;">
  <div style="background:#a1a1a1;width:100%;height:100%;">
  
  </div>
 </div>
</div>
<script type="text/javascript">
	var oDiv=document.getElementById("clientId");
	var sHeight=document.documentElement.clientHeight-92;
	var sWidth=document.documentElement.clientWidth-80;
	oDiv.style.height=sHeight+"px";
	oDiv.style.width=sWidth+"px";
</script>
</body>

</html>
