<!doctype html>
<html class="html">
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>我的任务</title>
#end
<link type="text/css" href="/style/css/oa_myTask.css" rel="stylesheet" />
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet"   />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/oa/oaTask.js"></script>
<script type="text/javascript" src="/js/oa/itemTaskPublic.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript">
var textBoxObj;
function submitQuery(){
	document.forms[0].operation.value=$globals.getOP("OP_QUERY");
	document.forms[0].submit();
}
//选择完日期后提交
function dateChange(){
	//form.submit();
}

/*加载控件*/
function loadTextBox(boxId){
	textBoxObj = new jQuery.TextboxList('#'+boxId, {unique: true, plugins: {autocomplete: {}}});
	textBoxObj.getContainer().addClass('textboxlist-loading');
	textBoxObj.plugins['autocomplete'].setValues($textBoxValues);
	textBoxObj.getContainer().removeClass('textboxlist-loading');
}

</script>
</head>
<body onload="showStatus();">
#if("$!addTHhead" == "true")
#parse("./././body2head.jsp")
<div>
#else
<div id="scroll-wrap">
#end
<form action="/OATaskAction.do" name="form" method="post">
<input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_QUERY')">
<input type="hidden" id="tabSelectName" name="tabSelectName" value="$!oaTaskform.tabSelectName">
<input type="hidden" id="searchStatus" name="searchStatus" value="$!oaTaskform.searchStatus">
<input type="hidden" id="tableName" name="tableName"  value="OATask">
<input type="hidden" id="crmTaskEnter" name="crmTaskEnter"  value="$!crmTaskEnter">
<input type="hidden" id="hasSearchCondition" name="hasSearchCondition"  value="$!oaTaskform.hasSearchCondition">
<input type="hidden" id="addTHhead" name="addTHhead"  value="$!addTHhead" />	
	<div class="wrap">
    	<!-- 头部header 分割线 Start -->
    	<div class="header">
        	<div class="leftTitle">
                <b class="icons projectPhoto"></b>
                <em>我的任务 
                #if("$loginBean.id" == "1")
                	<div class="btn btn-small" onclick="showBatchTransfer();">批量移交</div>
                #end
                </em> 
            </div>
            <span onclick="addTask();" class="add">
            	<b class="icons ab"></b>
                <em>新建任务</em>
            </span>
            <div class="tagScreen">
                <ul class="tagU">
                    <li id="tab_executor">我负责的#if("$!countMap"!="")($!countMap.get("executor"))#end</li>
                    <li id="tab_participant">我参与的#if("$!countMap"!="")($!countMap.get("participant"))#end</li>
                     <li id="tab_createBy">我指派的#if("$!countMap"!="")($!countMap.get("createBy"))#end</li>
                     <li id="tab_surveyor">我验收的#if("$!countMap"!="")($!countMap.get("surveyor"))#end</li>
                </ul>
                <div class="tagS rf">
					<span class="lf pr wp-status" status="1">
                    	<i #if("$!oaTaskform.searchStatus" =="1") class="sel" #end status="1">进行中</i>
						<i #if("$!oaTaskform.searchStatus" =="2") class="sel" #end status="2">完成</i>
						<i #if("$!oaTaskform.searchStatus" =="3") class="sel" #end status="3">验收中</i>
					</span>
					
                	<span class="lf pr key-search">
                		<input class="lf key-inp" type="text" id="searchKeyWord" name="searchKeyWord" value="$!oaTaskform.searchKeyWord" placeholder="关键字搜索..." onKeyDown="if(event.keyCode==13) searchConSubmit();"/> 
                		<b class="lf icons" onclick="javascript:form.submit();"></b>
                		<i class="lf i-senior highSearch">高级</i>
                		 <div class="tag-pa"  >
		                	<ul class="pr tag-su">
		                		<b class="cz">重置</b>
                		 		<b class="qx">取消</b>
                		 		<li>
		                			<i class="lf iM">任务编号:</i>
					                <span class="lf pr icon-txt">
					                	<input class="txtIcon ip_txt" type="text" id="searchTaskOrder" name="searchTaskOrder" value="$!oaTaskform.searchTaskOrder" onKeyDown="if(event.keyCode==13)searchConSubmit();"/>
					                </span>
		                		</li>
		                		<li>
		                			<i class="lf iM">开始时间</i>
		                			<span class="lf pr icon-txt">
					                	<input class="txtIcon ip_txt" type="text" id="searchBeginStartTime" name="searchBeginStartTime" value="$!oaTaskform.searchBeginStartTime" onclick="selectDate('searchBeginStartTime');" readonly="readonly"/>
					                    <b class="icons pa bDate" onclick="selectDate('searchBeginStartTime');"></b>
					                </span>
					                <b class="lf link"></b>
					                <span class="lf pr icon-txt">
					                	<input class="txtIcon ip_txt" type="text" id="searchBeginOverTime" name="searchBeginOverTime" value="$!oaTaskform.searchBeginOverTime" onclick="selectDate('searchBeginOverTime');" readonly="readonly"/>
					                    <b class="icons pa bDate" onclick="selectDate('searchBeginOverTime');"></b>
					                </span>
		                		</li>
		                		<li>
		                			<i class="lf iM">结束时间</i>
		                			<span class="lf pr icon-txt">
					                	<input class="txtIcon ip_txt" type="text" id="searchEndStartTime" name="searchEndStartTime" value="$!oaTaskform.searchEndStartTime" onclick="selectDate('searchEndStartTime');" readonly="readonly"/>
					                    <b class="icons pa bDate" onclick="selectDate('searchEndStartTime');"></b>
					                </span>
					                <b class="lf link"></b>
					                <span class="lf pr icon-txt">
					                	<input class="txtIcon ip_txt" type="text" id="searchEndOverTime" name="searchEndOverTime" value="$!oaTaskform.searchEndOverTime" onclick="selectDate('searchEndOverTime');" readonly="readonly"/>
					                    <b class="icons pa bDate" onclick="selectDate('searchEndOverTime');"></b>
					                </span>
		                		</li>
		                		<li>
		                			<i class="lf iM">负责人员</i>
		                			<span class="lf pr icon-txt">
		                				<input type="hidden" id="searchExecutor" name="searchExecutor" value="$!oaTaskform.searchExecutor"/>
										<input class="txtIcon ip_txt" type="text" id="searchExecutorName" name="searchExecutorName" value="$!globals.getEmpFullNameByUserId($!oaTaskform.searchExecutor)"  readonly="readonly" ondblclick="publicPopSelect('userGroup','searchExecutor','normalId','false');">
					                    <b onclick="publicPopSelect('userGroup','searchExecutor','normalId','false');" class="icons pa bMag"></b>
					                </span>
		                		</li>
		                		<li>
		                			<i class="lf iM">指派人员</i>
		                			<span class="lf pr icon-txt">
		                				<input type="hidden" id="searchCreateBy" name="searchCreateBy" value="$!oaTaskform.searchCreateBy"/>
										<input class="txtIcon ip_txt" type="text" id="searchCreateByName" name="searchCreateByName" value="$!globals.getEmpFullNameByUserId($!oaTaskform.searchCreateBy)" readonly="readonly" ondblclick="publicPopSelect('userGroup','searchCreateBy','normalId','false');">
					                    <b onclick="publicPopSelect('userGroup','searchCreateBy','normalId','false');" class="icons pa bMag"></b>
					                </span>
		                		</li>
		                		<li>
		                			<i class="lf iM">验收人员</i>
		                			<span class="lf pr icon-txt">
		                				<input type="hidden" id="searchSurveyor" name="searchSurveyor" value="$!oaTaskform.searchSurveyor"/>
										<input class="txtIcon ip_txt" type="text" id="searchSurveyorName" name="searchSurveyorName" value="$!globals.getEmpFullNameByUserId($!oaTaskform.searchSurveyor)" readonly="readonly" ondblclick="publicPopSelect('userGroup','searchSurveyor','normalId','false');">
					                    <b onclick="publicPopSelect('userGroup','searchSurveyor','normalId','false');" class="icons pa bMag"></b>
					                </span>
		                		</li>
		                		<li>
		                			<i class="lf iM">关联客户</i>
		                			<span class="lf pr icon-txt">
		                				<input type="hidden" id="searchClientId" name="searchClientId" value="$!oaTaskform.searchClientId"/>
										<input class="txtIcon ip_txt" type="text" id="searchClientIdName" name="searchClientIdName" value="$!clientName" readonly="readonly" ondblclick="publicPopSelect('CrmClickGroup','searchClientId','normalId','false');">
					                    <b onclick="publicPopSelect('CrmClickGroup','searchClientId','normalId','false');" class="icons pa bMag"></b>
					                </span>
		                		</li>
			        			<li>
		                			<i class="lf iM">紧急程度</i>	
				                	<select class="lf slt" id="searchEmergencyLevel" name="searchEmergencyLevel">
				                		<option value="">请选择</option>
				                		#foreach($item in $globals.getEnumerationItems("emergencyLevel"))	 
						        	    	<option value="$item.value" #if("$item.value" == "$!oaTaskform.searchEmergencyLevel") selected="selected" #end>$item.name</option>
								        #end
				               		</select>
		                		</li>
		                		<li>
		                			<i class="lf iM">关联项目</i>	
				                	<select class="lf slt" id="searchItemId" name="searchItemId">
				                		<option value="">无关联</option>
				                		#foreach($item in $itemList)
				                			<option value="$!globals.get($item,0)" #if("$!oaTaskform.searchItemId" == "$!globals.get($item,0)") selected="selected" #end>$!globals.get($item,1)</option>
				                		#end
				               		</select>
		                		</li>
		                		<li>
		                			<input class="btn-add" type="button"  value="查询" onclick="javascript:searchConSubmit();"/>
		                		</li>
		                	</ul>
		                </div>
                	</span>
                </div>
            </div>
        </div>
        <!-- 头部header 分割线 End -->
        <!-- 项目列表 proList 分割线 Start -->
        <div class="proList" >
        	<ul class="proListU" >
        		#foreach($task in $tasksList)
        		#set($statusFlag = "")
        		#set($statusName = "")
        		#set($status = "1")
        		#if("$task.status" == "1")
        			#set($statusFlag = "running")
        			#set($statusName = "执行中")
        			#if("$!task.surveyor" =="" || "$!task.surveyor" =="$loginBean.id")
	        			#set($status = "2")
        			#else
        				#set($status = "3")
        			#end
        		#elseif("$task.status" == "3")
        			#set($statusFlag = "surveyoring")
        			#set($statusName = "验收中")
        			#set($status = "surveyoring")
        		#else
        			#set($statusFlag = "achieve")
        			#set($statusName = "完成")
        			#set($status = "1")
        		#end
                <li class="mdiwin-li" id="$task.id" >
                	<a class="lf userPhoto" href="#"><img src='$globals.checkEmployeePhoto("48",$task.executor)' /></a>
                    <div class="lf content-div">
                    	<i><em class="No-color">NO.$!task.taskOrder</em> <em class="titleName" title="$task.title">$task.title</em></i>
                    	#if($!clientsMap.get("$task.clientId") !="")
                    		<span style="font-size:12px;color:#999;">关联客户：〖$!clientsMap.get("$task.clientId")<img src="/style/images/client/read_bar.png"  style="cursor: pointer;" onclick="clientDetail('$task.clientId','$!clientsMap.get("$task.clientId")')"/>〗</span>
                    	#end
                        <div class="bottom-d">
                        	<span class="fa-qi-ren">
                            	<i>负责人：</i>
                                <em>$globals.getEmpFullNameByUserId($task.executor)</em>
                            </span>
                            #if("$!oaTaskform.searchStatus" == "2")
	                            <span class="jie-zhi-time">
	                            	<i>完成时间：</i>
	                                <i>$!task.finishTime</i>
	                            </span>
	                        #else
	                            <span class="jie-zhi-time">
	                            	<i>截止时间：</i>
	                                <i>$task.endTime</i>
	                            </span>
                            #end
                            #set($schedule = 0)
                            #if("$task.schedule" !="")
                            	#set($schedule = $task.schedule)
                            #end
                            
                            <div class="ui-slider" title="${schedule}%" style="">
			                	<div style="width:${schedule}%"></div>
			                	<em class="num-po">${schedule}%</em>
					        </div>
					        <div style="margin-left: 70px;color: red;display: inline-block;" title="紧系程度">
					        	$!globals.getEnumerationItemsDisplay("emergencyLevel","$task.emergencyLevel")
					        </div>
                        </div>
                    </div>
                    <span class="lf wan-type">
                    #if("$task.status" == "2")
                    	已完成




                    #else
                    	进行中




                    #end
                    </span>
                    #if("$!oaTaskform.tabSelectName" == "tab_participant")
                    	#set($participantFlag = "participantRun")
                    	#if("$!task.status" == "2")
                    		#set($participantFlag = "participantFinish")
                    	#end
	                    <span class="rf pr proBtn $participantFlag" status="$status" isDetail="false" id="participantFlag" style="cursor: text;">$statusName</span>
                    #else
                    	 <span class="rf pr proBtn $statusFlag" status="$status" isDetail="false">
	                    	$statusName
	                    	
		                    #if("$!oaTaskform.tabSelectName" == "tab_surveyor")
		                   	<div class="d-opac-btn">
		                    	<b class="opac-btn surveyor-pass surveyorOp" status="2" isDetail="false">通过</b>
		                    	<b class="opac-btn surveyor-back surveyorOp" status="1" isDetail="false">退回</b>
		                   	</div>
		                   	#end
	                    </span>
                    #end
                    #if("$!oaTaskform.tabSelectName" != "tab_participant" && "$!oaTaskform.tabSelectName" != "tab_surveyor")
                   		<span class="icons updateTask" title='修改'></span>
                   		#if("$loginBean.id" == "$!task.createBy")<span class="del-item icons del" title='删除'></span>#end
                   	#end
                   	<p class="clear"><p>
                </li>
                
                
                #end
                
            </ul>
        </div>
        <!-- 项目列表 proList 分割线 End -->
        <div style="overflow:hidden;padding:0 40px;">
        $pageBar
        </div>
    </div>
    <div class="addWrap pop-layer" bg="show" id="addTaskDiv" ></div>
    <div class="addWrap pop-layer" bg="show" id="updateTaskDiv" ></div>

</form>
<div class="addWrap pop-layer" id="addFeedbackDiv" style="top: 31px; left: 369px; display:none;"></div>
<div id="hideBg" class="hideBg"></div>
<script type="text/javascript">
	var sHeight=document.documentElement.clientHeight;
	$("#scroll-wrap").height(sHeight);
</script>
<div class="addWrap pop-layer" id="batchTransferDiv" style="display:none;width: 350px;" >
	<div class="a-title">
	        	<span class="lf pr">
	            	<b class="icons pa"></b>
	            	批量移交
	            </span>
	        </div>
	        <ul class="aU" style="height: 100px;">
	       	   <li>
	           		<i class="lf iM">处理人类型：</i>          
	               	<select class="s-select" id="transferType" name="transferType">
	               		<option value="Executor" >负责人</option>
	               		<option value="surveyor" >验收人</option>
	               	</select>              
	           </li>
	            <li>
	            	<i class="lf iM ">原处理人：</i>
	                <span class="lf pr icon-txt">
	               		<input type="hidden" name="transferExecutor" id="transferExecutor" value=""/>
						<input class="txtIcon ip_txt" type="text" name="transferExecutorName" value="" id="transferExecutorName"  readonly="readonly" ondblclick="publicPopSelect('userGroup','transferExecutor','normalId','false');"/>
	                    <b onclick="publicPopSelect('userGroup','transferExecutor','normalId','false');" class="icons pa bMag"></b>
	                </span>
	            </li>
	            <li>
	            	<i class="lf iM ">移交给：</i>
	                <span class="lf pr icon-txt">
	               		<input type="hidden" name="transferTo" value="" id="transferTo" />
						<input class="txtIcon ip_txt" type="text" name="transferToName" value="" id="transferToName"  readonly="readonly" ondblclick="publicPopSelect('userGroup','transferTo','normalId','false');"/>
	                    <b onclick="publicPopSelect('userGroup','transferTo','normalId','false');" class="icons pa bMag"></b>
	                </span>
	            </li>
	            
	        </ul>
	        <div class="btn-items-wp">
	        	<span class="btn-items celBtn" onclick="closeBatchTransfer();">取消</span>
		        <span class="btn-items conBtn" onclick="batchTransfer(this);">确定</span>
	        </div>
	</div>
</div>
</body>
</html>
