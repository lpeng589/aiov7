<div class="oa-worklog">	
	<ul class="aU" >
    	<li class="type-li">
            <i class="lf iM">日志类型：</i>
            <i class="lf l-h25">日报</i>
            <ul class="u-quote" quotelogtype="day" quotelogdate="$!workLogDate" worklogid="$!workLogId">
	        	<li class="quote quote_th" isoperation="true" tablename="OAWorkLogDet" backfillname="summaryDiv">引用计划</li>	        	
	        </ul>
        </li>
        <li id="workLogDateLi">
            <i class="lf iM">指定日期：</i>
	        <i class="lf l-h25">$!workLogDate</i>
        </li>
        <li>
        	<i class="lf iM">今天总结：</i>
        	<ul class="lf txt-list" id="summaryDiv">
        	#foreach ( $foo in [1..4])
				<li class="pr icon-long-txt">
					<input class="lf txt-l inp" type="text" />
					<b class="icons-2 pa b-del" ></b>
					<input type="hidden" class="quoteInputInfo" value="empty,empty">
					<select class="s-select" title="选择总结进度">
						<option value="0">进度</option>
						<option value="0">0%</option>
						<option value="25">25%</option>
						<option value="50">50%</option>
						<option value="75">75%</option>
						<option value="100">100%</option>
					</select>
				</li>
			#end								
        	</ul>
        </li>
     	<li>
			<i class="lf iM">上传附件：</i>
			<span class="lf pr fujian" id="picbutton" name="picbutton" onClick="upload('AFFIX','daffix');">
   				<b class="icons-2 pa fujian" ></b>
  	 				附件
			</span>
				<ul class="add-tu" id="affixuploadul_daffix"></ul>
		</li>
	</ul>
	
	
	#if("$!existNextWorkLog"=="true" || $!nextThreeDateMap.size()==0)
	    	<div class="h-next">
	    	#if("$!workLogType" == "day")明日#else下周#end计划已存在
			</div>
	#else
	<input type="hidden" name="existNextWorkLog" id="existNextWorkLog" value="$!existNextWorkLog"/>
	<div class="a-f-title">
      		#if("$!workLogType" == "day")新增明天计划#else新增下周计划#end		    
  		</div>
		<ul class="aU">
			<li class="type-li">
	            <i class="lf iM">日志类型：</i>
	            <i class="lf l-h25">日报</i>
	            <ul class="u-quote" quoteLogType="$!workLogType" quoteLogDate="$!quoteDate" workLogId="$!workLogId" id="nextQuoteUl">
	           		<li class="quote quote_th" isoperation="true" tablename="OAToDo" backfillname="nextPlanDiv">引用待办</li>	           		
		        	<li class="quote quote_th" isoperation="true" tablename="OACalendar" backfillname="summaryDiv">引用日程</li>
		        	<li class="quote quote_th" isoperation="true" tablename="OATask" backfillname="nextPlanDiv">引用任务</li>
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
		            	<input type="radio" name="nextWorkLogDate" id="nextWorkLogDate" value="$!nextWorkLogDate" checked="checked"/>
		            	</div>
		            #else
		            	#foreach ($maps in $nextThreeDateMap.keySet())
		            		<input class="changeRadioDate" type="radio" id="nextWorkLogDate" name="nextWorkLogDate" value="$maps" #if($velocityCount==1) checked="checked" #set($quoteDate = $maps) #end>$maps($nextThreeDateMap.get($maps))
		            		
		            	#end
		            	
		            #end
		            
		        </li>
		     #end            
	        <li>
	        	<i class="lf iM" id="nextPlan">#if("$!workLogType" == "day")明天计划：#else下周计划：#end</i>
	        	<ul class="lf txt-list" id="nextPlanDiv">   						
					#foreach ( $foo in [1..4])
					<li class="pr icon-long-txt">
						<input class="lf txt-l inp" type="text" />	
						<b class="icons-2 pa b-del" ></b>
						<input type="hidden" class="quoteInputInfo" value="empty,empty">				
					</li>
					#end				
	        	</ul>
	        </li>
	    	<li>
				<i class="lf iM">上传附件：</i>
				<span class="lf pr fujian" id="picbutton" name="picbutton" onClick="upload('AFFIX','nextPlanAffix');">
		  				<b class="icons-2 pa fujian"></b>
		 	 				附件
				</span>
				<ul class="add-tu" id="affixuploadul_nextPlanAffix"></ul>
			</li>
		</ul>
		
	#end
	<div class="point-block">
		<input class="rf btn-cel" type="button" onClick="cel();" value="取消" />
		<span class="rf btn-add" type="button" operation="1" workLogId="$!workLogId" workLogDate="$!workLogDate" workLogType="$!workLogType" detType="$!detType" isPlanTemplate="$!isPlanTemplate" onclick="workLogDetSubmit(this);" >添加</span>
	</div>
</div>