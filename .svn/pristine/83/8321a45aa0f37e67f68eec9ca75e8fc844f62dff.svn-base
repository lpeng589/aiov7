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
<link type="text/css" rel="stylesheet" href="/style/css/oaWorkLog.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="/js/oa/oaTask.js"></script>
<script type="text/javascript" src="/js/oa/itemTaskPublic.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>
<script type="text/javascript" src="/js/oa/oaWorkLog.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
var treeObj;
var setting;
setting = {
	isSimpleData: true,
	treeNodeKey : "id",
    treeNodeParentKey : "pId",
	checkable: true,
	checkType : {"Y":"", "N":""}, 
	callback: {
		change:followEmp,
		click: showFollow
	},
};

$(function(){

	treeObj = jQuery("#treeDemo").zTree(setting, clone($personalFrameTree));
		
	//已选的关注人，打钩处理
	var followIds = $("#followIds").val();
	for(var i=0;i<followIds.split(",").length;i++){
		if(followIds.split(",")[i] !=""){
			var node = treeObj.getNodeByParam("id",followIds.split(",")[i]);
			if(node!=null){
				node.checked = true;
				treeObj.updateNode(node, false);
			}
		}
	}
	#if($personalFrameCount>0)
		$(".record-content").addClass("pdr");
	#end
	/*
	滑动到今天日期











	var scrollId = "day"+$("#today").val();	
	if($("#tabCreateByConDiv div[scrollId='"+scrollId+"']").length>0){
		var top = $("#tabCreateByConDiv div[scrollId='"+scrollId+"']").position().top;
		$("#scroll-wrap").animate({scrollTop: top}, 500);
	}
	*/
	$("#hideBg") .height($("#wrap").outerHeight(true));
})

var textBoxObj;

/*加载控件*/
function loadTextBox(boxId){
	textBoxObj = new jQuery.TextboxList('#'+boxId, {unique: true, plugins: {autocomplete: {}}});
	textBoxObj.getContainer().addClass('textboxlist-loading');
	textBoxObj.plugins['autocomplete'].setValues($textBoxValues);
	textBoxObj.getContainer().removeClass('textboxlist-loading');
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

function dealAsyn(){
	jQuery.close('workLogTemplate');
	form.submit();
	//workLogTemplateList();
}

</script>


</head>
<body >
#if("$!addTHhead" == "true")
#parse("./././body2head.jsp")
<form name="form" action="/OAWorkLogAction.do?addTHhead=true" method="post" />
#else
<form name="form" action="/OAWorkLogAction.do" method="post" />
#end
<input type="hidden" id="tableName" name="tableName"  value="OAWorkLog"/>
<input type="hidden" id="operation" name="operation"  value="$globals.getOP('OP_QUERY')"/>
<input type="hidden" id="searchStatus" name="searchStatus"  value="$!workLogSearchForm.searchStatus"/>
<input type="hidden" id="weekStartTime" name="weekStartTime"  value="$!workLogSearchForm.weekStartTime" />
<input type="hidden" id="followEmpId" name="followEmpId"  value="$!workLogSearchForm.followEmpId" />
<input type="hidden" id="followIds" name="followIds"  value="$!followIds" />
<input type="hidden" id="tabSelectName" name="tabSelectName"  value="$!workLogSearchForm.tabSelectName" />
<input type="hidden" id="teamLogDate" name="teamLogDate"  value="$!workLogSearchForm.teamLogDate" />
<input type="hidden" id="teamLogType" name="teamLogType"  value="$!workLogSearchForm.teamLogType" />
<input type="hidden" id="personalFrameCount" name="personalFrameCount"  value="$personalFrameCount" />
<input type="hidden" id="userId" name="userId"  value="$!userId" />
<input type="hidden" id="today" name="today"  value="$!globals.getHongVal("sys_date")" />
<input type="hidden" id="addTHhead" name="addTHhead"  value="$!addTHhead" />
#if("$!addTHhead" == "true")
<div id="scroll-quto" >
#else
<div id="scroll-wrap">
#end
<div class="pr wrap"  id="wrap">
	<div id="workLogTemplateList" class="worklog-t"></div>
	#if($loginBean.operationMap.get("/OAWorkLogTemplateAction.do").query())
		<i class="fit-template" onclick="workLogTemplateList()" title='模板设置'></i>
	#end
	<!-- 头部header 分割线Start -->
	<div class="pr header">
    	<div class="lf h-left">
        	<a class="icons record-logo"></a>
            <div class="lf name-week">
            	<div class="name-d">
                	<a href="#">$globals.getEmpFullNameByUserId($loginBean.id)</a>
                    <span class="sel-week">
                    	<b class="lf icons prev" title='上周' currDate="$monday" sign='-' onclick="addWeek(this)"></b>
                        <em class="lf">$monday - $sunday</em>
                        <b class="lf icons next" title='下周' currDate="$sunday" sign="+" onclick="addWeek(this)"></b>
                    </span>
                </div>
                <ul class="week-ul">
                	#if("$!workLogSearchForm.tabSelectName" == "tabFollowBy")
                		<li class=" #if("$!workLogSearchForm.teamLogType" == "week") sel #end teamLogQuery" workLogDate='$globals.getHongVal("sys_date")' workLogType="week">周报</li>
	                	#foreach ($keys in $weekScopeMap.keySet())
	                		 <li class=" #if("$!workLogSearchForm.teamLogDate" == "$keys" && "$!workLogSearchForm.teamLogType" == "day") sel #end teamLogQuery" workLogDate="$keys" title="$keys" workLogType="day">$weekScopeMap.get($keys)</li>	
	                	#end
                	#else
	                	<li class="existWorkLog" workLogDate='$globals.getHongVal("sys_date")' workLogType="week">周报</li>
	                	#foreach ($keys in $weekScopeMap.keySet())
	                		 <li class=" #if($!globals.getHongVal("sys_date") == "$keys") sel #end  existWorkLog" workLogDate="$keys" title="$keys" workLogType="day">$weekScopeMap.get($keys)</li>	
	                	#end
                	#end
                </ul>
            </div>
        </div>
        <!-- 
        <span class="search-record">
        	<input class="lf" type="text" placeholder="输入需要搜索的内容" />
            <b class="icons lf" title="搜索"></b>
        </span>
         -->
    </div>
    <!-- 头 header 分割线End -->
    <!-- 内容  record-content 分割线 Start -->
    <div class="record-content">
    	<div class="record-content-left">
	    	<div class="type-btns" id="tabSelect">
	    		#if("$!workLogSearchForm.tabSelectName" == "tabFollowBy")
	    			<span id="tabCreateBy" >我的日志</span>
		            <span class="sel"  id="tabFollowBy" >团队日志</span>
	    		#else
		        	<span class="sel" id="tabCreateBy" >我的日志</span>
		        	#if($personalFrameCount != 0)
			            <span id="tabFollowBy" >团队日志</span>
		        	#else
		        		<span id="followByExplain" >团队日志说明</span>
		            #end
	    		#end
	        </div>
	        <div class="con-list">
		        <div class="con-list tabSelect" id="tabCreateByConDiv" #if("$!workLogSearchForm.tabSelectName" == "tabFollowBy") style="display: none;" #end>
		        	#foreach($maps in $fullWeekScopeMap.keySet())
		        		#set($summaryCreateTime = "")
		        		#set($workLog ="")
		        		#set($workLog =$!existWorkLogMap.get($maps))
		        		<div name="con-wrap" class="con-wrap  #if("$!maps" == "week") weekWorkLog #end "  #if("$!maps" == "week") scrollId="week" workLogType="week" workLogDate="$!sunday" #else  scrollId="day${maps}" workLogType="day" workLogDate="$!maps" #end  workLogId="$!workLog.id">
			            	<div class="con-body">
			            		#if("$!workLog" !="")
			                	<div class="c-body-top">
			                    	<div class="body-top-left">
			                        	<a href="#"><img src="$globals.checkEmployeePhoto("48",$loginBean.id)" class="head_photo"/></a>
			                            <span class="name-time">
			                            	<a href="#" class="lf">$globals.getEmpFullNameByUserId("$workLog.createBy")</a> #if($!workLog.createBy == $loginBean.id) <span class="lf icons detail" title="详情" workLogId="$!workLog.id" workLogDate="$!workLog.workLogDate"></span>#end
			                            </span>
			                            <br/>
			                        </div>
			                    </div>
			                    #end
			                    
			                    #set($workLogDet = "")
			                    #set($workLogDet = $!workLogDetMap.get("$workLog.id"))
			                    	#if("$!workLog.isPlanTemplate" == "true")
			                    		<p class="period-title">模板 <span detType="plan" isPlanTemplate="true" class="icons updWorkLog" >	</span></p>
			                    		#foreach($det in $!workLogDet.get("2"))
		                            		<div class="d-tmplate">
		                            			$globals.get($det,1)
		                            		</div>
		                            	#end
		                            	#if("$!workLog.affix" !="")
				                            <ul class="acc-file">
				                            #foreach($affStr in $!workLog.affix.split(";"))
				                            	<li title='$affStr'><a href="/ReadFile?fileName=$globals.urlEncode($affStr)&realName=$globals.urlEncode($affStr)&tempFile=false&type=AFFIX&tableName=OAWorkLog" target="_blank">$affStr</a></li>
				                            #end
				                            </ul>
			                            #end
			                    	#else
					                    <div class="c-body-bottom">
					                    	<div class="t-period">
					                        	#if("$!workLogDet.get('2')" == "")
						                        	<p class="in-p-add">
						                        		<span detType="plan" class="addWorkLogDet" operation="1">新增计划</span>
						                        	</p>
					                        	#else
					                        		<p class="period-title">计划 <span detType="plan" isPlanTemplate="false" class="icons updWorkLog" >	</span></p>
						                            <ul class="period-ul" workLogId="$workLog.id">
						                            	#if($!workLogDet !="")
							                            	#foreach($det in $!workLogDet.get("2"))
							                            		<li>
							                            			<em>$globals.get($det,1)</em>
								                            		#if("$!globals.get($det,3)"=="OATask" && "$!globals.get($det,4)"!="")
								                            			<span class="relationTask" taskId="$!globals.get($det,4)" style="font-size: 12px;cursor: pointer;">【关联任务】</span>	
								                            		#elseif("$!globals.get($det,3)"=="OAClient" && "$!globals.get($det,4)"!="")
								                            			<span class="relationClient" taskId="$!globals.get($det,4)" style="font-size: 12px;cursor: pointer;">【关联客户】</span>
								                            			<input type="hidden" id="clientName" value="$!globals.get($det,1)"/>	
								                            		#else
									                            		<i class="createTask" detId="$globals.get($det,0)">生成任务</i>
								                            		#end
								                            		
							                            		</li>
							                            	#end
						                            	#end
						                            </ul>
						                            #if("$!workLog.affix" !="")
							                            <ul class="acc-file">
							                            #foreach($affStr in $!workLog.affix.split(";"))
							                            	<li title='$affStr'><a href="/ReadFile?fileName=$globals.urlEncode($affStr)&realName=$globals.urlEncode($affStr)&tempFile=false&type=AFFIX&tableName=OAWorkLog" target="_blank">$affStr</a></li>
							                            #end
							                            </ul>
						                            #end
					                        	#end
					                        </div>
					                        <div class="n-period">
					                        	#if("$!workLogDet.get('1')" == "")
					                        		#if("$!workLogDet.get('2')" != "")
						                        		#if(("$!maps" == "week" && "$!templateWeekContent" == "") || ("$!maps" != "week" && "$!templatedayContent" == ""))
							                        	<p class="in-p-add">
							                        		<span detType="summary" class="addWorkLogDet">新增总结</span>
							                        	</p>
							                        	#end
						                        	#end
					                        	#else
					                        		<p class="period-title">总结 <span detType="summary" isPlanTemplate="false" class="icons updWorkLog" ></span></p>
						                        	<ul class="period-ul">
							                            #if($!workLogDet !="")
							                            	#foreach($det in $!workLogDet.get("1"))
							                            	#if($velocityCount==1)
							                            		#set($summaryCreateTime = $!globals.get($det,7))
							                            	#end
							                            		<li>
							                            			<em>$globals.get($det,1)</em>
							                            			#if("$!globals.get($det,6)"!="")<em style="color: red;margin-left: 10px;">$!globals.get($det,6)%</em>#end
							                            		</li>
							                            	#end
							                            #end
						                            </ul>
						                            #if("$!workLog.summaryAffix" !="")
							                            <ul class="acc-file">
							                            #foreach($affStr in $!workLog.summaryAffix.split(";"))
							                            	<li title='$affStr'><a href="/ReadFile?fileName=$globals.urlEncode($affStr)&realName=$globals.urlEncode($affStr)&tempFile=false&type=AFFIX&tableName=OAWorkLog" target="_blank">$affStr</a></li>
							                            #end
							                            </ul>
						                            #end
					                        	#end
					                        </div>
					                        <div class="clear"></div>
					                    </div>
			                    	#end
			                   
			                    #if("$!workLog" !="")
				                    <div class="con-comment">
				                    	<i>发布时间：$!workLog.createTime</i>
				                    	#if("$!summaryCreateTime" !="")
				                    		<i class="pd-270">发布时间：$!summaryCreateTime</i>
				                    	#end
				                    	#set($discussCount = "")
				                    	#set($discussCount = $!discussCountMap.get("$workLog.id"))
				                    	<em workLogId="$workLog.id" class="discuss" frameType="createBy">评论(#if("$!discussCount" == "")0#else$discussCount#end)</em>
					                    <div style="display: none;" class="discussDiv">
					                    	<iframe frameborder="0" scrolling="0" width="100%"  id="createByFrame_$workLog.id" ></iframe>
					                    </div>
				                    </div>
			                    #end
			                    #if("$!workLog"!="" && "$!workLog.createBy" == "$loginBean.id")
			                   		<b class="icons delWorkLog" workLogId="$!workLog.id" title="删除"></b>
			                    #end
			                </div>
			                #if("$!maps" == "week")
				                
				                <span class="type-time"><i>周报($!monday至$!sunday)</i></span>
			                #else
			                	
			                	<span class="type-time"><i>$!weekScopeMap.get($maps)($!maps)</i></span>
			                #end
			            </div>
		        	#end
		        </div>
		        
		        <!-- 关注开始 -->
		        <div class="con-list tabSelect"  id="tabFollowByConDiv" #if("$!workLogSearchForm.tabSelectName" != "tabFollowBy") style="display: none;" #end>
		           #foreach($workLog in $followByList)
		           		#set($summaryCreateTime = "")
			        	<div class="con-wrap #if("$!workLog.type" == "week") weekWorkLog #end">
			            	<div class="con-body">
			                	<div class="c-body-top">
			                    	<div class="body-top-left">
			                        	<a href="#" onclick="showFollowQuery('$workLog.createBy','$globals.getEmpFullNameByUserId("$workLog.createBy")')"><img src="$globals.checkEmployeePhoto("48",$workLog.createBy)" class="head_photo"/></a>
			                            <span class="name-time">
			                            	<a href="#" onclick="showFollowQuery('$workLog.createBy','$globals.getEmpFullNameByUserId("$workLog.createBy")')">$globals.getEmpFullNameByUserId("$workLog.createBy")($globals.getDeptByUserId("$workLog.createBy")-$globals.getTitleIDByUserId("$workLog.createBy"))</a> 
			                            	#if("$!workLog.type" == "week")
			                            		<i>周报：$monday 至 $sunday</i>
			                            	#else
				                                <i>日报：$workLog.workLogDate（$!weekScopeMap.get("$workLog.workLogDate")）</i>
			                            	#end
			                            </span>
			                        </div>
			                    </div>
			                    #set($workLogDet = "")
			                    #set($workLogDet = $!followByDetMap.get("$workLog.id"))
			                    	#if("$!workLog.isPlanTemplate" == "true")
			                    		<p class="period-title">模板 <span detType="plan" isPlanTemplate="true" class="icons updWorkLog" >	</span></p>
			                    		#foreach($det in $!workLogDet.get("2"))
		                            		<div class="d-tmplate">
		                            			$globals.get($det,1)
		                            		</div>
		                            	#end
			                    	#else
					                    <div class="c-body-bottom">
					                    	<div class="t-period">
					                        	<p class="period-title">计划</p>
					                            <ul class="period-ul" workLogId="$workLog.id">
					                            	#if($!workLogDet !="")
						                            	#foreach($det in $!workLogDet.get("2"))
						                            		<li><em>$globals.get($det,1)</em></li>
						                            	#end
					                            	#end
					                            </ul>
					                            
					                        </div>
					                        <div class="n-period">
					                        	<p class="period-title">总结</p>
					                            <ul class="period-ul">
						                            #if($!workLogDet !="")
						                            	#foreach($det in $!workLogDet.get("1"))
						                            		#if($velocityCount==1)
							                            		#set($summaryCreateTime = $!globals.get($det,7))
							                            	#end
						                            		<li><em>$globals.get($det,1)</em>#if("$!globals.get($det,6)"!="")<em style="color: red;margin-left: 10px;">$!globals.get($det,6)%</em>#end</li>
						                            	#end
						                            #end
					                            </ul>
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
			                    	<em workLogId="$workLog.id" class="discuss" frameType="followBy">评论(#if("$!discussCount" == "")0#else$!discussCount#end)</em>
				                    <div style="display: none;" class="discussDiv">
				                    	<iframe frameborder="0" scrolling="0" width="100%"  id="followByFrame_$workLog.id" ></iframe>
				                    </div>
			                    </div>
			                     
			                </div>
			            </div>
		           #end
		           
		           #foreach($followId in $personalFrameAllEmp.split(","))
			           #if("$!followId" !="" && "$loginBean.id" != "$!followId")
					   #set($empIdStr = ","+$followId+",")
					       #if($!personalFrameAllEmp.indexOf($empIdStr)>-1 && "$!existFollowMap.get($!followId)"!="true")
							<div name="con-wrap" class="con-wrap">
				            	<div class="con-body">
				                	<div class="c-body-top">
				                    	<div class="body-top-left">
				                        	<a href="#" onclick="showFollowQuery('$!followId','$globals.getEmpFullNameByUserId("$!followId")')"><img src="$globals.checkEmployeePhoto("48",$!followId)" class="head_photo"/></a>
				                            <span class="name-time">
				                            	<a href="#" class="lf" onclick="showFollowQuery('$!followId','$globals.getEmpFullNameByUserId("$!followId")')">$globals.getEmpFullNameByUserId("$!followId")($globals.getDeptByUserId("$!followId")-$globals.getTitleIDByUserId("$!followId"))</a> 
				                            	
				                            </span>
				                        </div>
				                        <span class="no-work-log" onclick="warmToWriteLog('$!followId')">无计划,点击通知提醒</span>
				                    </div>
				                </div>
			           		 </div>
					       #end
					   #end
		           #end
		        </div>
		        <!-- 关注结束 -->
	        </div>
		</div>
		<div style="display: none;" id="followByExplainDiv">
			<p style="font-size:18px;padding:5px 0;line-height:25px;">
			<i style="color:red;">使用说明:</i>
					我的团队需要在菜单资料-职员处设置职员的直接上司，上司默认就能看下级的日志，关注下属后下属写日志会发送通知消息给上司。





			</p>
		
					<img src="style/images/item/explain.jpg" alt="" width="948" style="border:1px solid red;" />
					
		
		</div>
		
		<!-- 联系人最近 Start -->
		<div class="newly-contact" #if($personalFrameCount == 0) style="display: none;" #end>
			<p class="t-p  " tabName="myFollow">特别关注</p>
			<div style="clear:both;" >
				<ul class="newly-ul" id="myFollowUL">
					#foreach($empId in $followIds.split(","))
						#if("$!empId" !="")
						#set($empIdStr = ","+$empId+",")
							#if($!personalFrameAllEmp.indexOf($empIdStr)>-1)
								<li empId="$empId" empName="$globals.getEmpFullNameByUserId($empId)">
									<input type="hidden">
									<span class="followQuery" >$globals.getEmpFullNameByUserId($empId)</span>
									<b class="icons delFollow delFollowEmp"></b>
								</li>
							#end
						#end
					#end
				</ul>
			</div>
			<p class="t-p t-br " tabName="myTeam">我的团队 </p>
			<div style="clear:both;" >
				<div class="d-sea">
					<span class="sp-sea">
						<input type="text" style="display:none">
						<input class="inp" type="text" id="searchPersonalName" name="searchPersonalName" placeholder="搜索团队成员"  onkeydown="if(event.keyCode==13) searchPersonal()"/>
						<b class="b-s m-icons" onclick="searchPersonal();"></b>
					</span>
				</div>
				<div class="newly-aic" id="personalFrameTree">
					<label class="l-cbox">
						<input class="lf" type="checkbox" value="all" id="selectFollowAll" onclick="selFollowAll();"/>
						<em class="lf">全选</em> 
					</label>
					<ul id="treeDemo" class="ztree" ></ul>
				</div>
				<ul class="newly-ul" id="searchPersonalFrame" style="display: none;">
					#set($followIdsStr = ","+$followIds)
					#foreach($empId in $personalFrameAllEmp.split(","))
						#set($empIdStr = ","+$empId+",")
						<li empId="$empId"  empName="$globals.getEmpFullNameByUserId($empId)">
							<span class="followQuery">$globals.getEmpFullNameByUserId($empId)</span>
							<b class="icons followIcon #if($followIdsStr.indexOf($empIdStr)==-1) fol #else foled #end" id="${empId}Icon" #if($followIdsStr.indexOf($empIdStr)==-1) title="点击关注" #else title="取消关注" #end></b>
						</li>
					#end
				</ul>
			</div>
			
			
			
		</div>
		<!--  联系人最近 End -->
		
		
		
		
		
		
		
    </div>
    <!-- 内容  record-content 分割线  End -->
    <div class="addWrap pop-layer" id="addWorkLog" style="display: none;"></div>
	<div class="addWrap pop-layer" id="updateWorkLog" style="display: none;"></div>
	<div class="addWrap pop-layer" id="addTaskDiv" style="margin:0 auto;display: none;" ></div>
	<div class="addWrap pop-layer" id="addWorkLogDet" style="display: none;"></div>
	
</div>
</div>
 <div id="hideBg" class="hideBg"></div> 
</form>
#if("$!addTHhead" == "true")
<script type="text/javascript">
	var sHeight=document.documentElement.clientHeight;	
	$("#scroll-quto").height(sHeight);
	
</script>
#else
<script type="text/javascript">
	var sHeight=document.documentElement.clientHeight;	
	$("#scroll-wrap").height(sHeight);		
</script>
#end
</body>
</html>
