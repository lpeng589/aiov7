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
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/crm/crm_brother.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<link type="text/css" rel="stylesheet" href="/style/css/StyleFlash.css"  />
<script type="text/javascript" src="/js/FusionCharts.js"></script>
<script type="text/javascript">
var deleteMsg = '$text.get("common.msg.mustSelectOne")' ;
var confirmMsg = '$text.get("common.msg.confirmDel")' ;
var sucessDel = '$text.get("common.msg.delSuccess")' ;
var operation = $globals.getOP("OP_DELETE") ;
var selectOne = '$text.get("common.msg.mustSelectOne")';
var searchPopName;//用于查询语句隐藏的字段












$(function(){

	$(".viewItem").mouseover(function(){
		$(this).find(".viewList").show();
	}).mouseout(function(){
		$(this).find(".viewList").hide();
	});
	//单击li 显示下拉内容
	jQuery(".col_name").toggle(
	  function () {
	    jQuery(".bd ul").each(function(i){
			jQuery(this).find(":checkbox").removeAttr("checked") ;
		});
		jQuery(this).css("background-position","0 -644px").siblings("div.minfo").show().siblings(":checkbox").attr("checked","checked");
		
		var url = "CRMBrotherAction.do?type=comment&operation=4&keyId="+$(this).prev().val()+"&tableName="+$("#tableName").val()+"&employeeId="+$(this).attr("employeeId");
		$(this).parent().find(".minfo iframe").attr("src",url);
		$("#commentFrameId").val($(this).parent().find(".minfo iframe").attr("id"));//展开时用一个隐藏域记录当时的iframe的Id,方便评论页面评论后改变详情里面的高度
	  },
	  function () {
	  	jQuery(".bd ul").each(function(i){
			jQuery(this).find(":checkbox").removeAttr("checked") ;
		});
	    jQuery(this).css("background-position","0 -1031px").siblings("div.minfo").hide().siblings(":checkbox").attr("checked","checked");
	  }
	);
	//处理枚举值多选问题












	


	#foreach($fieldName in $fieldDisplayBean.searchFields.split(","))
		#set($mulName = ${fieldName}+"_mul")
		#if("$!conMap.get($mulName)" == "true"){
			jQuery("ul[name='$fieldName'] .no_more").show();
			jQuery("ul[name='$fieldName'] .more").hide();	
			jQuery("ul[name='$fieldName'] :checkbox").show();
		}
		#end	
	#end
	//单击ul 选择复选框
	jQuery(".bd ul").click(function (event) {
		if(event.target.tagName=="INPUT"){return;}
		if(jQuery(this).attr("class")=="maintop"){return;}
		jQuery(".bd ul").each(function(i){
			jQuery(this).find(":checkbox").removeAttr("checked") ;
		}) ;
		jQuery(this).find(":checkbox").attr("checked","checked") ;
	});
	//点击多选处理方法

















	$(".more").click(function(){
		$(this).parent().find(".no_more").show();
		$(this).hide();
		$(this).parents("ul").find(":checkbox").show();
		$("#"+$(this).attr("fieldName")+"_mul").val("true");
	})
	//点击单选处理方法















	$(".single").click(function(){
		$(this).parent().find(".more").show();
		$(this).parent().find(".no_more").hide();
		$(this).parents("ul").find(":checkbox").hide();
		$("#"+$(this).attr("fieldName")+"_mul").val("");
		//去除多选的选项并查询















		$(this).parents("ul").find(":checkbox").removeAttr("checked");
		submitQuery();
	})
	//枚举选项
	$(".col ul li label").click(function(){
		var $mulFlag = $("#"+$(this).parents("ul").attr("name")+"_mul");//获取XX_mul对象
		if($mulFlag.val() == "true"){
			//多选处理















			if($(this).attr("enumValue") == "all"){
				//去除其他选项与多选框
				$(this).parents("ul").find("li .enumName").removeClass("enumName");
				$(this).parents("ul").find("li input:checked").removeAttr("checked");
				//选择全部
				$(this).addClass("enumName");
				$mulFlag.val("");//变为单选















				submitQuery();
			}else{
				//其他选项删除全部按钮背景颜色
				$(this).parents("ul").find("li:first label").removeClass("enumName");
				if($(this).parent().find("input:checked").length==1){
					$(this).prev().removeAttr("checked");
					$(this).removeClass("enumName");
				}else{
					//$(this).prev().attr("checked","checked")
					$(this).prev().attr("checked","checked");
					$(this).addClass("enumName");
				}
			}
		}else{
			//单选















			if($(this).attr("enumValue") == "all"){
				$(this).addClass("enumName");
				$(this).parents("ul").find("li .enumName").removeClass("enumName");
				$(this).parents("ul").find("li input:checked").removeProp("checked");
			}else{
				$(this).parents("ul").find("li:first label").removeClass("enumName");
				$(this).parents("ul").find("li input:checked").removeProp("checked");
				$(this).prev().attr("checked","checked");
				$(this).addClass("enumName");
			}
			submitQuery();
		}
	})
	//枚举点击多选框
	$(".checkbox").click(function(){
		if($(this).attr("checked") == "checked"){
			$(this).parents("ul").find("li:first label").removeClass("enumName");
			$(this).next().addClass("enumName");
			$(this).attr("checked","checked");
		}else{
			$(this).next().removeClass("enumName");
			$(this).removeAttr("checked");
		}
	})
	//表示没有显示字段数据
	#if("$!noDisplayBean" == "true")
		alert("首次进入必须进行字段设置");
		fieldSetting();
	#end
});
//首页职员查询双击回填方法	
function fillData(datas){
	var fieldName = "employeeId";
	var ids = ","+jQuery("#"+fieldName).val();
	var selVal = ","+datas.split(";")[0]+",";
	if(ids.indexOf(selVal)==-1){
		$("#"+fieldName).val(jQuery("#"+fieldName).val()+datas.split(";")[0]+",");
		submitQuery();
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
		submitQuery();
	}
}
//查询清空
function clearALL(fieldName){
	$("#"+fieldName).val("");
	submitQuery();
}

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
	var url = '/OAMyWorkFlow.do?&operation=82&keyId='+keyId+"&tableName="+$("#tableName").val();
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
				window.location.href="/CRMBrotherAction.do?tableName="+jQuery("#tableName").val();
			}
　　     }
　 });
}
</script>
<style type="text/css">
.yj_time{float:left;display:inline-block;margin-left:150px}
.yj_time>i{width:110px;display:inline-block;text-align:right;}
.jh_dv{float:left;display:inline-block;margin:0 0 0 30px;}
</style>
</head>
<body class="body_ef" onload="mainTop();">
<div id="wrapper">
<div class="cont cf">
<div class="pagetitle">
	<span class="all_icon i_man"></span><h3>$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")</h3>
</div>
<div class="c_operate_l">
#if($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$!tableName").add())
<a href="javascript:" class=" icon_left i_add" id="addBtn" title="添加" ></a>
#end
#if($loginBean.operationMap.get("/CRMBrotherSettingAction.do").query())
<a href="javascript:" class=" icon_left i_reset" id="setBtn" onclick="fieldSetting();" title="字段设置"></a>
#end
</div>
<div class="c_main winw1200 f_l" id="clientId">
<a name="html_top"></a>
<script type="text/javascript">
	var oDiv=document.getElementById("clientId");
	var sHeight=document.documentElement.clientHeight-92;
	var sWidth=document.documentElement.clientWidth-80;
	oDiv.style.height=sHeight+"px";
	oDiv.style.width=sWidth+"px";
</script>
<form id="form" name="form" action="/CRMBrotherAction.do?tableName=$!tableName" method="post">
<input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_QUERY')">
<input type="hidden" id="type" name="type" value="">
<input type="hidden" name="tableName" id="tableName" value="$!tableName">
<input type="hidden" id="expandStatus" name="expandStatus" value="$!conMap.get('expandStatus')">
<input type="hidden" name="employeeId" id="employeeId" value="$!conMap.get('employeeId')">
<input type="hidden" name="commentFrameId" id="commentFrameId" value="">
<input type="hidden" name="ButtonType" id="ButtonType" value="">
<input type="hidden" name="parentTableName" id="parentTableName" value="CRMClientInfo">
<input type="hidden" id="sortInfo" name="sortInfo" value="$!conMap.get('sortInfo')"/>
<input type="hidden" id="designId" name="designId" value="$!designId"/>
<input type="hidden" id="addBillKeyId" name="addBillKeyId" value="$!addBillKeyId"/>
<input type="hidden" id="operationFlag" name="operationFlag" value="index"/>
<input type="hidden" id="moduleId" name="moduleId" value="">
<input type="hidden" id="clientKeyId" name="clientKeyId" value="$!conMap.get('clientKeyId')">
<input type="hidden" id="EmployeeID" name="EmployeeID" value="">
<input type="hidden" id="ButtonTypeName" name="ButtonTypeName" value="">
<input type="hidden" id="defineName" name="defineName" value="">
<!-- 获取资料分类 -->
<input type="hidden" id="getDataType" name="getDataType" value="">
<div class="main_search">
<!-- 模糊查询 Start -->
<div class="inp_wp">
	<span class="inp_t_span">模糊查询：</span>
	<div class="inpsearch"> 
		<select class="inp_sct" name="keyOption" id="keyOption" onKeyDown="if(event.keyCode==13) keywordSubmit();">
			#foreach($fieldName in $fieldDisplayBean.keyFields.split(","))
				#set($fieldBean=$globals.getCRMBrotherField($fieldName,$tableName,$childTableName))
				<option value="$fieldName" #if("$!conMap.get('keyOption')" == "$fieldName") selected="selected" #end>$fieldBean.display.get("$globals.getLocale()")</option>
			#end
			<option value="all" #if("$!conMap.get('keyOption')" == "all") selected="selected" #end>全部</option>
		</select>
		<input class="inp_txt"  id="keyword" name="keyword" type="text" class="" value="$!conMap.get('keyword')" reValue="$!conMap.get('keyword')" onKeyDown="if(event.keyCode==13) keywordSubmit();"/>
		<a class="inpsearch_btn" href="javascript:submitQuery();"></a>
	</div>
	<div class="stnn">
		<div class="sbtn">
			<a class="sbtn_a" popName="userGroup">选择职员</a>
		</div>
	</div>
</div>
<!-- 模糊查询 End -->

#if($!conMap.get("employeeId") != "")
<div class="col" id="employeeIdDiv">
<span>已选职员：</span>
<ul>
#foreach($col in $!conMap.get("employeeId").split(","))
#if("$!col" !="")
<div class="navigationShow"  id="$col"><div >$globals.getEmpFullNameByUserId($col)</div><img  src="/style/images/body/i_007.gif"  onclick="delSelected('$col','employeeId','employeeIdDiv')"/></div>
#end
#end
#if($globals.splitLength($!conMap.get("employeeId"),",") > 1)<a href="#" class="nomore" onclick="submitQuery();" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span></a><a href="#" class="nomore" onclick="clearALL('employeeId');" style="margin-left: 5px;margin-top: 1px;"><span style="float: left;width: 35px;margin-top: -1px;" title="清空">清空</span></a> #end
</ul>
</div>
#end

<!-- 枚举、时间类型开始 -->
#foreach($fieldName in $fieldDisplayBean.searchFields.split(","))
	#if("$!fieldName" != "")
		#if($velocityCount<=2)
			#set($fieldBean=$globals.getCRMBrotherField($fieldName,$tableName,$childTableName))
			#if("$fieldBean.inputType"=="1")
			<!-- 跟单阶段 Start -->
			<div class="col">
			<input type="hidden" name="${fieldName}_mul" id="${fieldName}_mul" value='$!conMap.get("${fieldName}_mul")'>
			<span>$fieldBean.display.get("$globals.getLocale()")：</span>
			<ul name="$fieldName">
			#set($enumVals = "")
			#set($enumVals = ","+$!conMap.get("$fieldName"))
			<li><label #if("$!conMap.get($fieldName)"=="") class="enumName" #end enumValue="all" name="all">全部</label></li>
			#foreach($enum in $globals.getEnumerationItems("$fieldBean.refEnumerationName"))
			#set($selFlag = "false")
			#if($enumVals.indexOf(",${enum.value},")>-1)
				#set($selFlag = "true")
			#end
			<li><input type="checkbox" class="checkbox" value="$!enum.value" name="$fieldName" #if("$selFlag" == "true") checked="checked" #end  /><label  #if("$selFlag" == "true") class="enumName" #end  enumValue="$!enum.value" title="$enum.name" >$enum.name</label></li>
			#end
			<a href="#" class="more" fieldName="$fieldName"><span style="width: 40px;margin-top: -1px;" title="多选" >多选</span></a>
			<a class="no_more" onclick="javascript:submitQuery();" >查询</a>
			<a class="no_more single" fieldName="$fieldName">单选</a>
			</ul>
			</div>
			<!-- 跟单阶段 End -->
			#else
			<div class="col">
				<span>$fieldBean.display.get("$globals.getLocale()")：</span>
				<ul class="time_s">
					<div>
						<input type="text" name="${fieldName}Start" readonly="readonly" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value='$!conMap.get("${fieldName}Start")' />
						至








	
	
	
	
	
	
	
						<input type="text" name="${fieldName}End" readonly="readonly" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value='$!conMap.get("${fieldName}End")'/>
					</div>
					<a href="#" class="nomore" onclick="submitQuery();">
						<span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span>
					</a>
				</ul>
			</div>
			#end
		#end
	#end
#end
<!-- 枚举、时间类型结束 -->

<!-- 隐藏枚举、时间类型开始 -->
<div style="display:none;" id="showSearch">
#foreach($fieldName in $fieldDisplayBean.searchFields.split(","))
	#if($velocityCount>2)
		#set($fieldBean=$globals.getCRMBrotherField($fieldName,$tableName,$childTableName))
		#if("$fieldBean.inputType"=="1")
			<!-- 跟单阶段 Start -->
			<div class="col">
			<input type="hidden" name="${fieldName}_mul" id="${fieldName}_mul" value='$!conMap.get("${fieldName}_mul")'>
			<span>$fieldBean.display.get("$globals.getLocale()")：</span>
			<ul name="$fieldName">
			#set($enumVals = "")
			#set($enumVals = ","+$!conMap.get("$fieldName"))
			<li><label #if("$!conMap.get($fieldName)"=="") class="enumName" #end enumValue="all" name="all">全部</label></li>
			#foreach($enum in $globals.getEnumerationItems("$fieldBean.refEnumerationName"))
			#set($selFlag = "false")
			#if($enumVals.indexOf(",${enum.value},")>-1)
				#set($selFlag = "true")
			#end
			<li><input type="checkbox" class="checkbox" value="$!enum.value" name="$fieldName" #if("$selFlag" == "true") checked="checked" #end  /><label  #if("$selFlag" == "true") class="enumName" #end  enumValue="$!enum.value" title="$enum.name" >$enum.name</label></li>
			#end
			<a href="#" class="more" fieldName="$fieldName"><span style="width:40px;margin-top:-1px;" title="多选" >多选</span></a>
			<a class="no_more" onclick="javascript:submitQuery();">查询</a>
			<a class="no_more single" fieldName="$fieldName">单选</a>
			</ul>
			</div>
			<!-- 跟单阶段 End -->
		#else
			<div class="col">
			<span>$fieldBean.display.get("$globals.getLocale()")：</span>
			<ul class="time_s">
				<div>
					<input type="text" name="${fieldName}Start" readonly="readonly" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value='$!conMap.get("${fieldName}Start")' />
					至















					<input type="text" name="${fieldName}End" readonly="readonly" onClick="WdatePicker({lang:'$globals.getLocale()'})" onKeyDown="if(event.keyCode==13) submitQuery();" value='$!conMap.get("${fieldName}End")'/>
				</div>
				<a href="#" class="nomore" onclick="submitQuery();">
					<span style="float: left;width: 35px;margin-top: -1px;" title="查询">查询</span>
				</a>
			</ul>
			</div>
		#end
	#end
#end
</div>
<!-- 隐藏枚举、时间类型结束 -->

<div class="maintablearea">
<div class="bd" id="maintop">
<div class="hd">
	<div class="m_l f_l">
		<div class="zk" id="searchExpand">展开</div>
	</div>
	<div class="m_r f_r">
		$!extendButton
		#if("$tableName" == "CRMPotentialClient")
		
		<div class="viewItem">
			<span><i class="viewSelect">客户转移</i><b></b></span>
			<ul class="viewList" style="display:none;">
				#foreach($module in $moduleList)
					#if("$!globals.get($module,0)" != "weixinModuleId")
					<li onclick="clientTransfer('index','$globals.get($module,0)')">
						<span class='$globals.get($module,0)'>$globals.get($module,1)</span>
					</li>
					#end
				#end
				
			</ul>
		</div>
		#end
		
		#if("$!hasGetDatasScope" == "true" && "$!loginBean.id" !="1" && "$!tableName"=="CRMPotentialClient")
			<!-- -->
			<div class="viewItem">
				<span><i class="viewSelect">获取数据</i><b></b></span>
				<ul class="viewList" style="display:none;">
					#set($field = $globals.getFieldBean("CRMPotentialClient","zlfl"))
					#foreach($item in $globals.getEnumerationItems("$field.refEnumerationName"))	 
						<li class="getDatas" itemVal="$item.value">
							<span >$item.name</span>
						</li>
			        #end
				</ul>
			</div>
		#end
		
		#if("$!publicScopeStr" == "" || $!publicScopeStr.indexOf("import")>-1)
			<div class="btn btn-small" id="importBtn">导入</div>
		#end
		#if("$!publicScopeStr" == "" || $!publicScopeStr.indexOf("export")>-1)
			<div class="btn btn-small" id="exportBtn">导出</div>
		#end
		#if($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$!tableName").delete())
			<div class="btn btn-small" id="deleteBtn">删除</div>
		#end
	</div>
</div>
<ul class="maintop">
<li class="col_hy" style="width:30px;padding:0 0 0 5px;"></li>
#if("$tableName" == "CRMSaleFollowUp")
<li class="col_hy" style="width:26px;"></li>
#end
<li class="col_hy" style="width:20px;"><input type="checkbox" id="checkAll" style="float:left;display:inline-block;margin:4px 0 0 0;"/></li>

<li class="col_hy" style="line-height:30px;text-align:center;width:100px;">操作</li>
#if("$!OAWorkFlow" == "true")
<!-- 
<li class="col_name" style="line-height:30px;width:90px;"><span>审核状态</span></li>
-->
<li class="col_name" style="line-height:30px;width:100px;"><span>当前审核节点</span></li>
#end
#foreach($fieldName in $fieldDisplayBean.listFields.split(","))
#set($fieldBean=$globals.getCRMBrotherField($fieldName,$tableName,$childTableName))
#set($width = $fieldBean.width)
<li class="col_name" style="line-height:30px;width:${width}px;"><span sort="DESC" fieldName="$fieldName" title="点击排序">$fieldBean.display.get("$globals.getLocale()")</span></li>
#if("$fieldBean.fieldName" == "EmployeeID")
##<li class="col_name" style="line-height:30px;width:${width}px;"><span sort="DESC" fieldName="$fieldName" title="点击排序">所属部门</span></li>
#end
#end
</ul>
</div>
<div class="bd" id="titleId">
#set($rowIndex = 0)
#foreach($brother in $brotherList)
#set($rowIndex = $rowIndex+1)
#set($updateOp = "false")
#if($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$!tableName").update())
#set($updateOp = "true")
#end
<ul class="col h-col" id='$brother.get("id")'  #if($!brother.get("yysj").length()>=10&&$!brother.get("yysj").substring(0,10)==$globals.getDate()) style="color:red" #end>
	<li class="num_li">$rowIndex、</li>
	#if("$tableName" == "CRMSaleFollowUp")
	<li class="down-up" sc="hide" style="width:26px;"></li>
	#end
	<li class="cbox_li"><input class="check_i" name="keyId" type="checkbox" value="$!brother.get('id')" /></li>
	
	<li class="col_hy" style="text-align:center;min-width:60px;width:100px;" name="operationLi">
		#if($brother.get("workFlowNode")!="-1" && $brother.get("checkPersons").indexOf(";${loginBean.id};")>-1 && "$updateOp" == "true")
			<a class="update" title='修改'></a> 
		#elseif((("$!OAWorkFlow" == "") || ("$!OAWorkFlow" == "true" && $brother.get("workFlowNode")=="0")) && "$updateOp" == "true")
			<a class="update" title='修改'></a> 
		#end
		<a class="detail" title='详情'></a>
	</li>
	#if("$!OAWorkFlow" == "true")
		<!-- 
		#set($flowStatus = "")
		#if($brother.get("workFlowNode")=="-1")
			#set($flowStatus = "完毕")
		#elseif($brother.get("workFlowNode")=="0")
			#set($flowStatus = "待审核")
		#else
			#set($flowStatus = "审核中")
			
		#end
		<li class="col_hy">
			<em style="width:90px;"><i>$flowStatus</i>
			#if("$!OAWorkFlow" == "true"&&$brother.get("workFlowNode")!="-1"&&$brother.get("checkPersons").indexOf(";${loginBean.id};")>-1)
				#set($keyId = $brother.get("id"))
				<b class="approve" title='审核' onclick="approve('$keyId','approve','$tableName')"></b> 
			#end
			</em>
			
		</li>
		 -->
		#set($flowInfo = "")
		#set($flowInfo = "")
		#if($brother.get("workFlowNode")=="0")
			#set($flowInfo = "待审核")
		#elseif($brother.get("workFlowNode")=="-1")
			#set($flowInfo = "审核完毕")
		#else
			#set($checkPerson="")
			#foreach($str in $brother.get("checkPersons").split(";"))
				#if("$!str" !="")
					#set($checkPerson=$checkPerson+$globals.getEmpFullNameByUserId($!str)+",")
				#end
			#end
			#if("$!checkPerson" !="")
				#set($checkPerson= $globals.subEndwith("$checkPerson",$checkPerson.length()))
			#end
			#set($workFlowNode = "")
			#set($workFlowNode = $!brother.get("workFlowNode"))
			#set($flowInfo = $!globals.getCurrNodeName("$designId","$workFlowNode")+" ("+($checkPerson)+")")
		#end
			<li class="col_hy"><em style="width:100px;cursor: pointer;" onclick="workFlowDepict('$brother.get("id")')" title='$flowInfo'>$flowInfo</em></li>
	#end
	#foreach($fieldName in $fieldDisplayBean.listFields.split(","))
		 
		#set($fieldBean=$globals.getCRMBrotherField($fieldName,$tableName,$childTableName))
		#set($width = "90")
		#if("$!fieldBean.width"!="")
			#set($width = $fieldBean.width)
		#end
		#if("$fieldBean.inputType"=="0")
			#if("$fieldBean.fieldType" == "1")
	        	<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName">$!globals.dealDoubleDigits("$!brother.get($fieldBean.fieldName)","amount") </em></li>
			#elseif("$fieldBean.fieldType"=="5")
				<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName">#if("$!brother.get($fieldBean.fieldName)"!="")$!brother.get("$fieldBean.fieldName").substring(0,10) #end </em></li>
			#elseif("$fieldBean.fieldType"=="6")
				#if("$tableName"=="CRMcomplaints"&&"$fieldBean.fieldName"=="TimeLimit")
					#set($differTimeMap = $globals.getDifferTimeInfo($!brother.get("$fieldBean.fieldName"),"$globals.getHongVal('sys_datetime')"))
					#set($isOver = $!differTimeMap.get("isOver"))
					#set($timeDisplay = $!brother.get("$fieldBean.fieldName"))
					#if("$!isOver"=="true")
						#set($timeDisplay = $timeDisplay + "(超时"+$!differTimeMap.get("timeStr")+")")
					#end
					<li class="col_hy"><em style=" width:${width}px;#if("$!isOver"=="true") color: red; #end" name="$fieldBean.fieldName" title="$timeDisplay">$timeDisplay</em></li>
				#else
					<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName">$!brother.get("$fieldBean.fieldName")</em></li>
				#end
				
			#elseif("$fieldBean.fieldType"=="3" || "$fieldBean.fieldType"=="16")
				<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName">$!brother.get("$fieldBean.fieldName")</em></li>
			#elseif("$fieldBean.fieldType" == "13"||"$fieldBean.fieldType" == "14")
			
			#elseif("$!tableName"=="CRMPotentialClient" && "$fieldBean.fieldName"=="Trade")
				<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName"> $!workProfessionMap.get($!brother.get("$fieldBean.fieldName")) </em></li>
				
			#elseif("$!tableName"=="CRMPotentialClient" && "$fieldBean.fieldName"=="District")
				<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName"> $!districtMap.get($!brother.get("$fieldBean.fieldName")) </em></li>
			#else
				<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName">$!brother.get("$fieldBean.fieldName")</em></li>
			#end
		#elseif("$fieldBean.inputType"=="1" || "$fieldBean.inputType"=="5" || "$fieldBean.inputType"=="10")
			#set($value = $!brother.get("$fieldBean.fieldName"))
			<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName">$!globals.getEnumerationItemsDisplay("$fieldBean.refEnumerationName","$value")</em></li>
		#elseif("$fieldBean.inputType"=="2")
			#if("$fieldBean.fieldName" == "ClientId")
				<li class="col_hy" ><em style="width:${width}px;" name="$fieldBean.fieldName" title='$!brother.get("clientName")'>〖$!brother.get("clientName")<img src="/style/images/client/read_bar.png"  style="cursor: pointer;" onclick="showClient('$!brother.get("$fieldBean.fieldName")','$!brother.get("clientName")');" title="查看客户详情"/>〗</em></li>
			#elseif("$fieldBean.fieldName" == "EmployeeID")
				<li class="col_hy" title='$!globals.getEmpFullNameByUserId($!brother.get("EmployeeID"))'><em style="width:${width}px;" name="$fieldBean.fieldName">$!globals.getEmpFullNameByUserId($!brother.get("EmployeeID"))</em></li>
				<li class="col_hy" title='$!globals.getDeptByUserId($!brother.get("EmployeeID"))'><em style="width:${width}px;" name="$fieldBean.fieldName">$!globals.getDeptByUserId($!brother.get("EmployeeID"))</em></li>
			#else
				<li class="col_hy" title='$!brother.get($fieldBean.fieldName)'><em style="width:${width}px;" name="$fieldBean.fieldName">$!brother.get($fieldBean.fieldName)</em></li>
			#end
		#elseif("$fieldBean.inputType"=="14")
			#set($deptVal ="")
			#set($deptVal =$!brother.get("$fieldBean.fieldName"))
			<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName">$!deptMap.get("01")</em></li>
		#elseif("$fieldBean.inputType"=="15")
			#set($empVal ="")
			#set($empVal =$!brother.get("$fieldBean.fieldName"))
			<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName">$!globals.getEmpFullNameByUserId("$empVal")</em></li>
		#elseif("$fieldBean.inputType"=="20")
			#set($tempTableName = "CRMSaleContract")
			#if("$fieldBean.defaultValue"=="2")
				#set($tempTableName = "CRMSalesChance")
			#end
			<li class="col_hy relative">
				<em style="width:${width}px;" name="$fieldBean.fieldName">#if("$!relateClientMap.get($!brother.get($fieldBean.fieldName))" !="")〖$!relateClientMap.get($!brother.get($fieldBean.fieldName))<em><img src="/style/images/client/read_bar.png" #if("$fieldBean.defaultValue"=="1") onclick="showContact('$!brother.get($fieldBean.fieldName)',this)" #elseif("$fieldBean.defaultValue"=="2" || "$fieldBean.defaultValue"=="3") onclick="mdiwin('/CRMBrotherAction.do?operation=5&tableName=$tempTableName&keyId=$!brother.get($fieldBean.fieldName)','$!brother.get("Topic")')" #else #end/></em>〗#end</em>
				<div class="showDetail">
				</div>
			</li>
		#else
			<li class="col_hy"><em style="width:${width}px;" name="$fieldBean.fieldName">$!brother.get("$fieldBean.fieldName")&nbsp;</em></li>
		#end
		
	#end
	
	
	<div class="discuss">
		<div class="moperate">
			跟单内容:#if("$!brother.get('Content')" == "") 无 #else $brother.get("Content") #end
		</div>
		#set($keyId = $brother.get("id"))
		<iframe width="100%" frameborder=no scrolling="no" id="Frame_${keyId}"></iframe>
	</div>
</ul>
#end

</div>

</div>

#if("$!sginFlag" == "true")
#if("" != $!dataRight && "$!dataRight" != "null")
<p style="color:red;font-size:15px;padding:10px 0;text-align:center;">◆销售漏斗 </p>
<div style="text-align:center;">

	<p style="padding:10px 0;">
		<b>销售漏斗：仅统计含有阶段的<span style="color:red">"状态=跟踪"</span>的销售机会</b>
	</p>
	

	<div style="display:inline-block;">
		
		<div align="center" style="float:left;display:inline-block;"> 
			<p>
				各阶段机会数量













			</p>
			<p id="chartdiv">
				
			</p>
		</div>
		<div align="center" style="float:left;display:inline-block;">
			<p>
				各阶段机会预期金额













			</p>
			<p id="chartdiv1">
				
			</p>
		</div>
		<script type="text/javascript">
			var datas = "$!dataRight";
			if(datas!="" && datas != null){
				var chart = new FusionCharts("/flash/FCF_Funnel.swf", "ChartId", "420", "300");		
				chart.setDataXML(datas);   
				chart.render("chartdiv");
			}
			var datas1 = "$!dataLeft";
			if(datas1 !="" && datas1 != null){
				var chart1 = new FusionCharts("/flash/FCF_Funnel.swf", "ChartId", "420", "300");
				chart1.setDataXML(datas1);   
				chart1.render("chartdiv1");
			}		
		</script> 
	</div>
<div style="overflow:hidden;display:inline-block;">
	<p style="color:red;padding:10px 0;">销售预测</p>
	<div class="yj_time">
		<i>预计签单日期,从：</i>
		<input name="bgTime"  id="bgTime" size="15" onkeydown="if(event.keyCode==13) openInputDate(this);"
				value="$!beginTime" onClick="openInputDate(this);" />
		<br/>
		<br/>
		<i>至：</i>
		<input name="edTime"  id="edTime" size="15" onkeydown="if(event.keyCode==13) openInputDate(this);"
				value="$!endTime" onClick="openInputDate(this);" />		
	</div>
	<div class="jh_dv">
		<i>机会可能性</i>
		<select name="changeType" id="changeType"  style="width:100px" >
			<option value="0" >&gt;=0%</option>
			<option value="1">&gt;=10%</option>
			<option value="2">&gt;=20%</option>
			<option value="3" selected="selected" >&gt;=30%</option>	
			<option value="4">&gt;=40%</option>	
			<option value="5">&gt;=50%</option>	
			<option value="6">&gt;=60%</option>	
			<option value="7">&gt;=70%</option>	
			<option value="8">&gt;=80%</option>	
			<option value="9">&gt;=90%</option>	
			<option value="10">&gt;=100%</option>					
		</select>
		<i><b><font color="#0066CC">预测值=</font></b></i>
		<input type="text" style="border:0px;width:100px" id="totValue" readonly="readonly" name="totValue" /> 
		<span style="display:block;clear:both;padding:15px 0 0 0;">
			<div class="btn btn-mini" onClick="subCast('0');" title="含成功机会" >销售预测</div>  
			<div class="btn btn-mini"  onClick="subCast('2');" title="不含成功机会" >新签预测</div>  
		</span>
	</div>
</div>	
<script type="text/javascript">
/*var container = $('div');
var scrollTo = $("#sginScrool");
container.scrollTop(
scrollTo.offset().top - container.offset().top + container.scrollTop()
);*/
</script>
</div>
#end
#end

<!-- 
<div class="pageflow">
<a href="#" class=" all_icon i_pageup"></a>
<a href="#" class=" all_icon i_pagedown"></a>
</div>
 -->
</div>
</div>
$!pageBar

<div class="pagetop">
<a id="html_top" class="pagetopbtn" title="返回顶部"></a>
</div>

 <div class="brother_table" id="brotherid" style="display:block;">
 <!-- 
 	<iframe id="brotherTable" name="brotherTable" allowTransparency="true" src="" 
 			frameborder="no" scrolling="no" onload="this.height=brotherTable.document.documentElement.scrollHeight-10;this.width=brotherTable.document.documentElement.scrollWidth;"></iframe>
  -->
  <div style="background:#a1a1a1;width:100%;height:100%;">
  
  </div>
 </div>
</div>


</body>
<script type="text/javascript">
function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}
function subCast(obj){
	var beginTime = jQuery("#bgTime").val();
	var endTime = jQuery("#edTime").val();
	var changeType = jQuery("#changeType").val();
	jQuery.ajax({ 
		url: "/CRMBrotherAction.do?operation=15&tableName=CRMSalesChance&beginTime="+beginTime+"&endTime="+endTime+"&changeType="+changeType+"&flagValue="+obj, 	
		success: function(totData){
	    	jQuery("#totValue").val(totData);
		}
	});
}
#if("$!conMap.get('expandStatus')"=="open")
	$(document).ready(function(){
		$("#searchExpand").click();
	});
#end
</script>
</html>
