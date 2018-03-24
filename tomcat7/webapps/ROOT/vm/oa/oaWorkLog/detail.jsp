<!doctype html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>我的日志</title>
#end
<link type="text/css" href="/style/css/oaWorkLog.css" rel="stylesheet" />
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet"   />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="/js/oa/oaWorkLog.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript" src="/js/oa/oaTask.js"></script>
<script type="text/javascript" src="/js/oa/itemTaskPublic.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript">
function loadTextBox(boxId){
	shareByTBox = new jQuery.TextboxList('#'+boxId, {unique: true, plugins: {autocomplete: {}}});
	shareByTBox.getContainer().addClass('textboxlist-loading');
	//executorTBox.add('<a id="$loginBean.id">$loginBean.empFullName</a>')
	shareByTBox.plugins['autocomplete'].setValues($textBoxValues);
	shareByTBox.getContainer().removeClass('textboxlist-loading');
}
/*快速添加任务方法&添加任务打开*/
function addTaskFast(){
	$("#addTaskDiv").html($("#copyAddTask").html());
	$("#addTaskDiv").show();
	$(".addWrap").css("top","20px");
	$(".addWrap").css("left","25%");
	publicDragDiv($(".addWrap"));
}
//关闭层




function closeLayer(obj){
	$(obj).parent().parent().hide();
	$(obj).parent().parent().html("");
	$("#hideBg").hide();
}

function loadTextBox(boxId){
	/*
		shareByTBox = new jQuery.TextboxList('#'+boxId, {unique: true, plugins: {autocomplete: {}}});
		shareByTBox.getContainer().addClass('textboxlist-loading');
		//executorTBox.add('<a id="$loginBean.id">$loginBean.empFullName</a>')
		shareByTBox.plugins['autocomplete'].setValues($textBoxValues);
		shareByTBox.getContainer().removeClass('textboxlist-loading');
	*/
}

/*快速添加任务方法&添加任务打开*/
function addTaskFast(){
	$("#addTaskDiv").html($("#copyAddTask").html());
	$("#addTaskDiv").show();
	$(".addWrap").css("top","20px");
	$(".addWrap").css("left","25%");
	publicDragDiv($(".addWrap"));
}

</script>
</head>
<body >
#if("$!addTHhead" == "true")
#parse("./././body2head.jsp")
#end
<form name="form" action="/OAWorkLogAction.do" method="post">
<input type="hidden" id="tableName" name="tableName"  value="OAWorkLog">
<input type="hidden" id="operation" name="operation"  value="$globals.getOP('OP_DETAIL')">
<input type="hidden" id="workLogId" name="workLogId"  value="$workLogBean.id">
#if("$!addTHhead" == "true")
<div>
#else
<div id="scroll-wrap" style="overflow: auto;">
#end
<div class="wrap"  id="wrap">
	<!-- 
	<div class="d-header">
		#if("$workLogBean.type" == "week")
			<p>$globals.getEmpFullNameByUserId("$workLogBean.createBy")$monday 至 $sunday周报</p>
		#else
			<p>$globals.getEmpFullNameByUserId("$workLogBean.createBy")${workLogBean.workLogDate}($!weekScopeMap.get($workLogBean.workLogDate)) 日志</p>
		#end
	</div>
	 -->
    <div class="record-content detail-content">
        <div class="con-list" id="createByConDiv">
        	#foreach($workLog in $workLogBeanList)
        	#set($summaryCreateTime = "")
        	<!-- con-wrap内容块 分割线Start -->
        	<div class="con-wrap #if("$!workLog.type" == "week") weekWorkLog #end this-wrap " #if("$!workLog.type" == "week") workLogType="week" workLogDate="$!sunday" #else workLogType="day" workLogDate="$!workLog.workLogDate" #end workLogId="$!workLog.id" name="con-wrap">
            	<div class="con-body">
                	<div class="c-body-top">
                    	<div class="body-top-left">
                        	<a href="#"><img src="$globals.checkEmployeePhoto("48",$workLog.createBy)" class="head_photo"/></a>
                            <span class="name-time">
                            	<a href="#">$globals.getEmpFullNameByUserId("$workLog.createBy")</a> 
                            </span>
                        </div>
                    </div>
                    #set($workLogDet = "")
                    #set($workLogDet = $!workLogDetMap.get("$workLog.id"))
                    #if("$!workLog.isPlanTemplate" == "true")
                    	<p class="period-title">模板
                    		#if("$!workLog.createBy" == "$loginBean.id") 
                    			<span detType="plan" isPlanTemplate="true" class="icons updWorkLog" />
                    		#end
                    	</p>
                  		#foreach($det in $!workLogDet.get("2"))
                      		<div class="d-tmplate">
                      			$globals.get($det,1)
                      		</div>
                      	#end
                    #else
	                    <div class="c-body-bottom">
	                        <div class="n-period">
	                        	#if("$!workLogDet.get('2')" == "")
	                        		#if("$!workLog.createBy" == "$userId")
			                        	<p class="in-p-add">
			                        		<span detType="plan" class="addWorkLogDet" operation="1">新增计划</span>
			                        	</p>
		                        	#end
	                        	#else
		                        	<p class="period-title">计划 
		                        	#if("$!workLog.createBy" == "$userId")  
		                        		<span detType="plan" isPlanTemplate="false" class="icons updWorkLog" >
		                        	#end
		                        	</p>
		                            <ul class="period-ul">
			                            #if($!workLogDet !="")
			                            	#foreach($det in $!workLogDet.get("2"))
			                            		<li><em>$globals.get($det,1)</em>
			                            		#if("$!workLog.createBy" == "$loginBean.id")
				                            		#if("$!globals.get($det,3)"!="task" && "$!globals.get($det,4)"!="")
				                            			<span class="relationTask" taskId="$!globals.get($det,4)" style="font-size: 12px;cursor: pointer;">【关联任务】</span>	
				                            		#else
					                            		<i class="createTask" detId="$globals.get($det,0)">生成任务</i></li>
				                            		#end
			                            		#end
			                            	#end
			                            #end
		                            </ul>
	                        	#end
	                        </div>
	                        
	                        <div class="t-period">
	                        	#if("$!workLogDet.get('1')" == "")
		                       		#if("$!workLogDet.get('2')" != "")
			                       		#if("$!workLog.createBy" == "$userId")
				                        	<p class="in-p-add">
				                        		<span detType="summary" class="addWorkLogDet">新增总结</span>
				                        	</p>
			                        	#end
		                        	#end
		                       	#else
		                        	<p class="period-title">总结
		                        	#if("$!workLog.createBy" == "$userId")
			                        	<span detType="summary" isPlanTemplate="false" class="icons updWorkLog" >
		                        	#end
		                        	</p>
		                            <ul class="period-ul" workLogId="$workLog.id">
		                            	#if($!workLogDet !="")
			                            	#foreach($det in $!workLogDet.get("1"))
			                            		#if($velocityCount==1)
				                            		#set($summaryCreateTime = $!globals.get($det,7))
				                            	#end
			                            		<li><em>$globals.get($det,1)</em>#if("$!globals.get($det,6)"!="")<em style="color: red;margin-left: 10px;">$!globals.get($det,6)%</em>#end</li>
			                            	#end
		                            	#end
		                            </ul>
		                       	#end
	                        </div>
	                    </div>
                    #end
                   
                    <div class="con-comment">
                    	<i>发布时间：$!workLog.createTime</i>
                    	#if("$!summaryCreateTime" !="")
                    		<i class="pd-270">发布时间：$!summaryCreateTime</i>
                    	#end
                    	#set($discussCount = "")
			            #set($discussCount = $!discussCountMap.get("$workLog.id"))
                    	<em workLogId="$workLog.id">评论(#if("$!discussCount" == "")0#else$discussCount#end)</em>
	                    <div style="width: 100%;" class="discussDiv">
	                    	<iframe frameborder="0" scrolling="0" width="100%"  id="createByFrame_$workLog.id" src="/DiscussAction.do?tableName=OAWorkLogDiscuss&f_ref=$workLog.id&parentIframeName=createByFrame_${workLog.id}"></iframe>
	                    </div>
                    </div>
                </div>
                #if("$!workLog.type" == "week")
	                <b class="weekWorkLog-top-link"></b>
	                <b class="weekWorkLog-bottom-link"></b>
	                <span class="type-time">
		                <i>
		                	周报
		                	#if($velocityCount==1)
			                	($!monday至$!sunday)
		                	#else
		                		($!nextMonday至$!nextSunday)
		                	#end
		                </i>
	                </span>
                #else
                	<span class="type-time">
	                	<i>
	                	#if($velocityCount==1)
	                		$!workLogDateName
	                	#else
	                		$!nextDayName
	                	#end	
	                	
	                	($workLog.workLogDate)
	                	</i>
                	</span>
                #end
            </div>
            <!-- con-wrap内容块 分割线End -->
            #end
        </div>
        
        
    </div>
</div>



<div class="addWrap pop-layer" id="addWorkLog" style="display: none;"></div>

<div class="addWrap pop-layer" id="updateWorkLog" style="display: none;"></div>

<div class="addWrap pop-layer" id="addTaskDiv" style="margin:0 auto;display: none;" ></div>

<div class="addWrap pop-layer" id="addWorkLogDet" style="display: none;"></div>

<div class="add-calendar" style="display: none;"></div>
</div>

</form>

<script type="text/javascript">
	var sHeight=document.documentElement.clientHeight;
	$("#scroll-wrap").height(sHeight);
</script>

<div id="hideBg" class="hideBg"></div> 
</body>
</html>
