	<div class="a-title">
        <span class="lf pr">
           <b class="icons pa"></b>
           #if("$!detType" == "plan")
	                       新增日志
           #else
                  新增总结
           #end
        </span>
    </div>
    <div class="d-scroll">
    <ul class="aU">
    	<li class="type-li" id="typeLi">
            <i class="lf iM" >日志类型：</i>
            <i class="lf" style="line-height:27px;">#if("$!workLogType" == "day")日报#else周报#end</i>
            #if("$!planTemplateContent" == "")
	            #if("$!detType" == "plan")
			        <ul class="u-quote" quoteLogType="$!workLogType" quoteLogDate="$!workLogDate" workLogId="$!workLogId">
			        	<li class="quote" isoperation="true" tablename="OAToDo" backfillname="planDiv">引用待办</li>
			        	<li class="quote" isoperation="true" tablename="OACalendar" backfillname="planDiv">引用日程</li>
			        	<li class="quote" isoperation="true" tablename="OATask" backfillname="planDiv">引用任务</li>
			        	<li isoperation="true" tablename="OAClient" onclick="publicPopSelectClient('CrmClickGroup','taskClientId','normalId','false');" backfillname="planDiv">引用客户</li>
			        </ul>
		        #else
		        	<ul class="u-quote" quoteLogType="$!workLogType" quoteLogDate="$!workLogDate" workLogId="$!workLogId">
			        	<li class="quote" isoperation="true" tablename="OAWorkLogDet" backfillname="summaryDiv">引用计划</li>
			        	<li class="quote" isoperation="true" tablename="OACalendar" backfillname="summaryDiv">引用日程</li>
			        	<!--  <li isoperation="true" tablename="OAClient" onclick="publicPopSelectClient('CrmClickGroup','taskClientId','normalId','false');" backfillname="summaryDiv">引用客户</li>-->
			        </ul>
		        #end
	        #end
        </li>
        #if("$!workLogType" == "day")
        <li id="workLogDateLi" >
            <i class="lf iM">指定日期：</i>
	        <i class="lf"  style="line-height:27px;">$!workLogDate</i>
	        
        </li>
        #end
        
        #if("$!detType" == "plan")
        <li class="quote-li" style="display: none;">
            <i class="lf iM">分享给同事：</i>
            <div class="lf txt-list share-d" style="width:390px;">
            	<input class="lf txtIconLong ip_txt" type="text" id="shareBy"/>
            	<span class="icons share-s" onclick="publicPopSelect('userGroup','shareId','shareId','true');"></span>
            </div>
			<div class="clear"></div>
        </li> 
        #end
        
        #if("$!planTemplateContent" != "" && "$!detType" == "plan")
        	<textarea name="summary" id="summary" style="height:300px;width:98%;line-height:normal;">$!planTemplateContent</textarea>
        	<script type="text/javascript">
        		loadKEditor();
        	</script>
        #else
	        #if("$!detType" == "plan")
	        	<li class="quote-li">
		            <i class="lf iM" id="nextPlan">#if("$!workLogType" == "day")今天计划：#else本周计划：#end</i>
		            <div class="lf txt-list" id="planDiv">
						#foreach ( $foo in [1..4])
							<span class="pr icon-long-txt">
								<i class="No-i">$foo、</i>
		                    	<input class="txtIconLong ip_txt" type="text" id="taskClientIdName"/>
		                    	<input type="hidden" id="taskClientId"/>
		                    	<b class="icons pa b-del" ></b>
								<input type="hidden" class="quoteInputInfo" value="empty,empty" id="clientType">	
		                	</span>
						#end
		            </div>
					<div class="clear"></div>
		        </li> 
	        #else
		        <li class="quote-li">
		            <i class="lf iM" id="thisSummary">
						#if("$!workLogType" == "day")今天总结：#else 本周总结：#end
					</i>
		            <div class="lf txt-list" id="summaryDiv">
						#foreach ( $foo in [1..4])
						<span class="pr icon-long-txt">
							<i class="No-i">$foo、</i>
		                    <input class="lf txtIconLong ip_txt" type="text" id="taskClientIdName"/>
		                    <input type="hidden" id="taskClientId"/>
		                    <b class="icons pa b-del" ></b>
							<input type="hidden" class="quoteInputInfo" value="empty,empty" id="clientType">
							<select class="scheduleSelect" title="选择总结进度">
								<option value="0">进度</operation>
								<option value="0">0%</operation>
								<option value="25">25%</operation>
								<option value="50">50%</operation>
								<option value="75">75%</operation>
								<option value="100">100%</operation>
							</select>
		                </span>
						#end
		            </div>
					<div class="clear"></div>
		        </li>  
	        #end
        #end
        <li>
            <i class="lf iM">上传附件：</i>
            <span class="lf pr fujian" id="picbutton" name="picbutton" onClick="upload('AFFIX','daffix');">
                <b class="icons pa fujian"></b>
               	 附件
            </span>
             <ul class="add-tu" id="affixuploadul_daffix"></ul>
            
        </li>
    </ul>
    
    #if("$!detType" == "summary")
	    #if("$!existNextWorkLog"=="true" || $!nextThreeDateMap.size()==0)
	    	<div class="h-next">
	    	#if("$!workLogType" == "day")明日#else下周#end计划已存在			</div>

	    #else
	    	<input type="hidden" name="existNextWorkLog" id="existNextWorkLog" value="$!existNextWorkLog"/>
		    <div class="a-f-title">
		        #if("$!workLogType" == "day")新增明天计划#else新增下周计划#end
		    </div>
		    <ul class="aU">
		    	<li class="type-li" id="typeLi">
		            <i class="lf iM" >日志类型：</i>
		            <i class="lf" style="line-height:27px;">#if("$!workLogType" == "day")日报#else周报($globals.get($mondayAndSunday,0)至$globals.get($mondayAndSunday,1))#end</i>
					<ul class="u-quote" quoteLogType="$!workLogType" quoteLogDate="$quoteDate" workLogId="$!workLogId" id="nextQuoteUl">
			        	<li class="quote" isoperation="true" tablename="OAToDo" backfillname="nextPlanDiv">引用待办</li>
			        	<li class="quote" isoperation="true" tablename="OACalendar" backfillname="nextPlanDiv">引用日程</li>
			        	<li class="quote" isoperation="true" tablename="OATask" backfillname="nextPlanDiv">引用任务</li>
			        	<li isoperation="true" tablename="OAClient" onclick="publicPopSelect('CrmClickGroup','taskClientId2','normalId','false');" backfillname="nextPlanDiv">引用客户</li>
			        </ul>
		        </li>
		        #if("$!workLogType" == "day")
		        <li id="workLogDateLi" >
		            <i class="lf iM">指定日期：</i>
		            #set($quoteDate = "")
		            #if("$!nextWorkLogDate"!="")
		            	#set($quoteDate = $!nextWorkLogDate)
		            	<i class="lf"  style="line-height:27px;">$!nextWorkLogDate($!nextWorkLogDateCN)</i>
		            	<div style="display: none;">
		            	<input type="radio" name="nextWorkLogDate" id="nextWorkLogDate" value="$!nextWorkLogDate" checked="checked" />
		            	</div>
		            #else
		            	#foreach ($maps in $nextThreeDateMap.keySet())
		            		<input class="changeRadioDate" type="radio" id="nextWorkLogDate" name="nextWorkLogDate" value="$maps" #if($velocityCount==1) checked="checked" #set($quoteDate = $maps) #end>$maps($nextThreeDateMap.get($maps))
		            		
		            	#end
		            	
		            #end
		            
		        </li>
		        #end
		       	<li class="quote-li">
		            <i class="lf iM" id="nextPlan">#if("$!workLogType" == "day")明天计划：#else下周计划：#end</i>
		            <div class="lf txt-list" id="nextPlanDiv">
						#foreach ( $foo in [1..4])
							<span class="pr icon-long-txt">
								<i class="No-i">$foo、</i>
		                    	<input class="txtIconLong ip_txt" type="text" id="taskClientIdName"/>
		                    	<input type="hidden" id="taskClientId" />
		                    	<b class="icons pa b-del" ></b>
								<input type="hidden" class="quoteInputInfo" value="empty,empty" id="clientType">	
		                	</span>
						#end
		            </div>
					<div class="clear"></div>
		        </li> 
		        
		        <li>
		            <i class="lf iM">上传附件：</i>
		            <span class="lf pr fujian" id="picbutton" name="picbutton" onClick="upload('AFFIX','nextPlanAffix');">
		                <b class="icons pa fujian"></b>
		               	 附件
		            </span>
		             <ul class="add-tu" id="affixuploadul_nextPlanAffix"></ul>
		            
		        </li>
		    </ul>
	    #end
    
    
    #end
    </div>
    <div class="btn-items-wp">
    	<input type="hidden" id="isPlanTemplate" name="isPlanTemplate" value="$!isPlanTemplate"/>
        <span class="btn-items celBtn" onclick="closeLayer(this);">取消</span>
        <span class="btn-items conBtn" operation="1" workLogId="$!workLogId" workLogDate="$!workLogDate" workLogType="$!workLogType" detType="$!detType" isPlanTemplate="$!isPlanTemplate" onclick="workLogDetSubmit(this);" onmouseover="changevalue()">确定</span>
    </div>
    <script type="text/javascript">
    function changevalue(){
    var taskClientId=$("#taskClientId").val();
     if(taskClientId!=""&&taskClientId!=null){
	$("#clientType").val("OAClient,"+taskClientId);
    }
    }
    
    </script>