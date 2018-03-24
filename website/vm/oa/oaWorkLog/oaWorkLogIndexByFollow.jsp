<!doctype html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
<title>我的日志</title>
<link type="text/css" rel="stylesheet" href="/style/css/oaWorkLog.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>
<script type="text/javascript" src="/js/oa/oaWorkLog.js"></script>
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

</script>


</head>
<body >
<form name="form" action="/OAWorkLogAction.do" method="post" />
<input type="hidden" id="operation" name="operation"  value="$globals.getOP('OP_QUERY')"/>
<input type="hidden" id="weekStartTime" name="weekStartTime"  value="$!workLogSearchForm.weekStartTime" />
<input type="hidden" id="followIds" name="followIds"  value="$!followIds" />
<div id="scroll-wrap" >
<div class="wrap"  id="wrap">
	<!-- 头部header 分割线Start -->
	<div class="pr header">
    	<div class="lf h-left">
        	<a class="icons record-logo"></a>
            <div class="lf name-week">
            	<div class="name-d">
                	<a href="#">$globals.getEmpFullNameByUserId($!workLogSearchForm.followEmpId)</a>
                    <span class="sel-week">
                    	<b class="lf icons prev" title='上周' currDate="$monday" sign='-' onclick="addWeek(this)"></b>
                        <em class="lf">$monday - $sunday</em>
                        <b class="lf icons next" title='下周' currDate="$sunday" sign="+" onclick="addWeek(this)"></b>
                    </span>
                </div>
                <ul class="week-ul">
                	<li class="existWorkLog" workLogDate='$globals.getHongVal("sys_date")' workLogType="week">周报</li>
                	#foreach ($keys in $weekScopeMap.keySet())
                		 <li class=" #if($!globals.getHongVal("sys_date") == "$keys") sel #end  existWorkLog" workLogDate="$keys"  title="$keys" workLogType="day">$weekScopeMap.get($keys)</li>	
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
	        <div class="con-list">
		        <div class="con-list tabSelect" id="tabCreateByConDiv">
		        	
		        	#foreach($maps in $fullWeekScopeMap.keySet())
		        		#set($summaryCreateTime = "")
		        		#set($workLog ="")
		        		#set($workLog =$!existWorkLogMap.get($maps))
		        		<div name="con-wrap" class="con-wrap  #if("$!maps" == "week") weekWorkLog #end " workLogId="$!workLog.id" #if("$!maps" == "week") scrollId="week" workLogType="week" workLogDate="$!sunday" #else  scrollId="day${maps}" workLogType="day" workLogDate="$!maps" #end >
			            	<div class="con-body">
			            		#if("$!workLog" !="")
			                	<div class="c-body-top">
			                    	<div class="body-top-left">
			                        	<a href="#"><img src="$globals.checkEmployeePhoto("48",$userId)" class="head_photo"/></a>
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
						                        		<span>无</span>
						                        	</p>
					                        	#else
					                        		<p class="period-title">计划 </p>
						                            <ul class="period-ul" workLogId="$workLog.id">
						                            	#if($!workLogDet !="")
							                            	#foreach($det in $!workLogDet.get("2"))
							                            		<li>
							                            			<em>$globals.get($det,1)</em>
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
						                        	<p class="in-p-add">
						                        		<span>无</span>
						                        	</p>
						                        	#end
					                        	#else
					                        		<p class="period-title">总结</p>
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
			                </div>
			                #if("$!maps" == "week")
				                <span class="type-time"><i>周报($!monday至$!sunday)</i></span>
			                #else
			                	<span class="type-time"><i>$!weekScopeMap.get($maps)($!maps)</i></span>
			                #end
			            </div>
		        	#end
		        </div>
		        
	        </div>
		</div>
    </div>
    <!-- 内容  record-content 分割线  End -->
</div>

</div>

</form>
<script type="text/javascript">
	var sHeight=document.documentElement.clientHeight;
	$("#scroll-wrap").height(sHeight);
</script>

</body>
</html>
