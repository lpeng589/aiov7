
<div class="a-title">
    <span class="lf pr">
        <b class="icons pa"></b>
        #if("$!detType" == "plan")
        	修改计划
        #else
        	修改总结
        #end
    </span>
</div>
<div class="d-scroll">
<ul class="aU">
	<li class="type-li" id="typeLi">
        <i class="lf iM">日志类型：</i>
        <i class="lf" style="line-height:27px;">#if("$!workLogBean.type" == "day")日报#else周报#end</i>	
        #if("$!workLogBean.isPlanTemplate" != "true")
	        #if("$!detType" == "plan")
		        <ul class="u-quote" quoteLogType="$!workLogBean.type" quoteLogDate="$!workLogBean.workLogDate" workLogId="$!workLogBean.id">
		        	<li class="quote" isoperation="true" tablename="OAToDo" backfillname="planDiv">引用代办</li>
		        	<li class="quote" isoperation="true" tablename="OACalendar" backfillname="planDiv">引用日程</li>
		        	<li class="quote" isoperation="true" tablename="OATask" backfillname="planDiv">引用任务</li>
		        	<li isoperation="true" onclick="publicPopSelectClient('CrmClickGroup','taskClientId','normalId','false');" backfillname="planDiv">引用客户</li>
		        </ul>
	        #else 
	        	<ul class="u-quote" quoteLogType="$!workLogBean.type" quoteLogDate="$!workLogBean.workLogDate" workLogId="$!workLogBean.id">
		        	<li class="quote" isoperation="true" tablename="OAWorkLogDet" backfillname="summaryDiv">引用计划</li>
		        	<li class="quote" isoperation="true" tablename="OACalendar" backfillname="summaryDiv">引用日程</li>
		        	<!-- <li isoperation="true" onclick="publicPopSelectClient('CrmClickGroup','taskClientId','normalId','false');" backfillname="planDiv">引用客户</li>  -->
		        </ul>
	        #end
        #end
    </li>
    <li id="workLogDateLi" #if("$!workLogBean.type"=="week") style="display: none;" #end>
        <i class="lf iM">指定日期：</i>
       	<input type="hidden" id="workLogDate" name="workLogDate" value="$workLogBean.workLogDate">
       	<i class="lf" style="line-height:27px;">$workLogBean.workLogDate</i>
        	<!-- 
        	<select id="workLogDate" name="workLogDate">
        		$optionInfo
        	</select>
        	 -->
    </li>
    
    #if("$!detType" == "plan")
    <li class="quote-li" style="display: none;">
        <i class="lf iM" >分享给同事：</i>
        <input type="hidden" id="hideShareByInfo" name="hideShareByInfo" value="$!shareByInfo">
        <div class="lf txt-list share-d" style="width:390px;">
           	<input class="lf txtIconLong ip_txt" type="text" id="shareBy"/>
           	<span class="icons share-s" onclick="publicPopSelect('userGroup','shareId','shareId','true');"></span>
         	</div>
		<div class="clear"></div>
    </li> 
    #end
    #if("$!detType" == "summary")
    	#set($affixStr = $workLogBean.summaryAffix)
    #else
	    #set($affixStr = $workLogBean.affix)
    #end
    #set($isPlanTemplate = "")
    #if("$!workLogBean.isPlanTemplate" == "true")
    	#set($isPlanTemplate = "true")
    	#set($workLogDet = "")
        #set($workLogDet = $!workLogDetMap.get("$workLogBean.id"))
       	#foreach($det in $!workLogDet.get("2"))
    		<textarea name="summary" id="summary" style="height:300px;width:98%;line-height:normal;">$globals.get($det,1)</textarea>
    	#end
       	<script type="text/javascript">
       		loadKEditor();
       	</script>
    #else
	    #if("$!detType" == "plan")
		    <li class="quote-li">
		        <i class="lf iM" id="nextPlan">#if("$!workLogBean.type"=="week") 下周计划：#else 明天计划：#end</i>
		        <div class="lf txt-list" id="planDiv">
		        	#set($workLogDet = "")
		            #set($workLogDet = $!workLogDetMap.get("$workLogBean.id"))
		           	#foreach($det in $!workLogDet.get("2"))
		           	#set($quoteInputInfo = "empty,empty")
		            #if("$!globals.get($det,3)" !="")
		            	#set($quoteInputInfo = "$!globals.get($det,3),$!globals.get($det,4)")
		            #end
		           		<span class="pr icon-long-txt">
		           			<i class="No-i">$velocityCount、</i>
		                	<input class="txtIconLong ip_txt" type="text" value="$globals.get($det,1)" id="taskClientIdName"/>
		                	<b class="icons pa b-del" ></b>
		                	<input type="hidden" class="quoteInputInfo" value="$quoteInputInfo">
		           		</span>
		           	#end
		        </div>
		        <div class="clear"></div>
		    </li>  
	    #else
		    <li class="quote-li">
		        <i class="lf iM" id="thisSummary">#if("$!workLogBean.type"=="week") 本周总结：#else 今天总结：#end</i>
		        <div class="lf txt-list" id="summaryDiv">
		        	#set($workLogDet = "")
		            #set($workLogDet = $!workLogDetMap.get("$workLogBean.id"))
		           	#foreach($det in $!workLogDet.get("1"))
		           		#set($quoteInputInfo = "empty,empty")
			            #if("$!globals.get($det,3)" !="")
			            	#set($quoteInputInfo = "$!globals.get($det,3),$!globals.get($det,4)")
			            #end
		           		<span class="pr icon-long-txt">
		           			<i class="No-i">$velocityCount、</i>
		                	<input class="lf txtIconLong ip_txt" type="text" value="$globals.get($det,1)" id="taskClientIdName"/>
		                	<b class="icons pa b-del" ></b>
		                	<input type="hidden" class="quoteInputInfo" value="$quoteInputInfo">
		                	<select class="scheduleSelect" title="选择总结进度">
		                		<option value="0">进度</operation>
								<option value="0" #if("$globals.get($det,6)" == "0") selected="selected" #end)>0%</operation>
								<option value="25" #if("$globals.get($det,6)" == "25") selected="selected" #end>25%</operation>
								<option value="50" #if("$globals.get($det,6)" == "50") selected="selected" #end>50%</operation>
								<option value="75" #if("$globals.get($det,6)" == "75") selected="selected" #end>75%</operation>
								<option value="100" #if("$globals.get($det,6)" == "100") selected="selected" #end>100%</operation>
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
        <span class="lf pr fujian"  onClick="upload('AFFIX','daffix');">
            <b class="icons pa fujian" id="picbutton" name="picbutton" ></b>
           	 附件
          	 
        </span>
        <ul id="affixuploadul_daffix" class="add-tu">
	        	#foreach($uprow in $affixStr.split(";"))
					#if($uprow != "")
						<li class="file_li" id="uploadfile_$uprow">
						<input type=hidden id="daffix" name=daffix value='$uprow'/><div class="showAffix">$uprow</div>&nbsp;&nbsp;&nbsp;			
							<a href='javascript:deleteupload("$uprow","false","OAWorkLog","AFFIX");'>$text.get("common.lb.del")</a>
							&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=OAWorkLog" target="_blank">$text.get("common.lb.download")</a>
						</li>
					#end
				#end
	         </ul>
    </li>   
</ul>
</div>
<div class="btn-items-wp">
    <span class="btn-items celBtn" onclick="closeLayer(this);">取消</span>
    <span class="btn-items conBtn"  operation="2" workLogId="$!workLogBean.id" workLogDate="$!workLogBean.workLogDate" workLogType="$!workLogBean.type" detType="$!detType" isPlanTemplate="$isPlanTemplate" onclick="workLogDetSubmit(this);">确定</span>
</div>

