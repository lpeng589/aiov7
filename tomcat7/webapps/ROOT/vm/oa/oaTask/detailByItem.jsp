
    <!--  任务task 分割线End-->
    <div class="task">
    	#set($isShowPoint = "")
    	#if(("$loginBean.id"=="$itemBean.executor" || "$loginBean.id"=="$itemBean.createBy"))
	    	#set($isShowPoint = "showAppoint")
    	#end
    	#if(("$loginBean.id"=="$itemBean.executor" || "$loginBean.id"=="$itemBean.createBy") && "$itemBean.status" !="2")
    	<div class="addTask" onclick="addTaskFast();">
        	<b class="lf icons"></b>
            <i class="lf" >快速添加任务</i>
        </div>
        #end
        <!-- 待完成任务 Start -->
        <div class="tasking">
        	<p class="subhead">
            	<b class="lf icons"></b>
                <i class="lf">待完成任务</i>
            </p>
            <ul class="uTasking" id="runTaskUl">
	          	#foreach($task in $runTaskList)  
	          		
	            	
	            	<li id="$globals.get($task,0)">
	            	#if("$loginBean.id"=="$globals.get($task,8)")
	            		<div class="d-oper" >
	            			<b class="b-del icons" title="删除"></b>
	            			
	            		</div>
	            	#end
	                <span class="tContent" title="$globals.get($task,1)">$globals.get($task,1)</span>
	                    
	                #if("$!globals.get($task,2)"== "")
                   		<span class="sAppoint $isShowPoint">
                   			未指派


                   		</span>
                   		#if("$!globals.get($task,6)" !="0")
		                    <span class="sFinish ">$!globals.get($task,6)条评论</span>
		                #end
                   	#else
                   		<span class="sFinish  $isShowPoint">
                    		$!globals.getEmpFullNameByUserId($globals.get($task,2)) $globals.get($task,5)
                    	</span>
                    	#if("$!globals.get($task,6)" !="0")
		                    <span class="sFinish">$!globals.get($task,6)条评论</span>
		                #end
		                
                    	#if("$!globals.get($task,3)" == "3")
                    		<span class="sFinish ">
                    			验收中


                    			#if("$loginBean.id" == "$!globals.get($task,7)")
                    			<div class="d-opac-btn" taskId="$globals.get($task,0)">
			                    	<b class="opac-btn surveyor-pass updTaskStatus" status="2" >通过</b>
			                    	<b class="opac-btn surveyor-back updTaskStatus" status="1" >退回</b>
		                    	</div>
		                    	#end
                    		</span>
                    	#else
                    		#if("$loginBean.id"=="$itemBean.createBy" || "$loginBean.id"=="$itemBean.executor" || "$loginBean.id" == "$!globals.get($task,2)")
		                    	#set($status="2")
		                    	#if("$!globals.get($task,7)" != "" && "$!globals.get($task,7)" != "$loginBean.id")
	                    			#set($status="3")
		                    	#end
			                   	<span class="sFinish updTaskStatus" status="$status">完成</span>
		                   	#end
	                   	#end
		                   	
		                   	
           			#end
           			
           			
	                </li>
                 #end
            </ul>
        </div>
        <!-- 待完成任务 End -->
        <!-- 已完成任务 Start -->
        <div class="tasked">
        	<p class="subhead">
            	<b class="lf icons"></b>
                <i class="lf">已完成任务</i>
            </p>
            <ul class="uTasked" id="finishTaskUl">
            	#foreach($task in $finishTaskList)
            	<li id="$globals.get($task,0)">
                    <span class="tContent" title="$globals.get($task,1)">$globals.get($task,1)</span>
                    	#if("$!globals.get($task,2)" == "")
                    		<span class="sAppoint">
                    			未指派




                    		</span>
                    	#else
                    		<span class="sFinish">
	                    		$!globals.getEmpFullNameByUserId($globals.get($task,2)) $globals.get($task,5)
	                    	</span>
                    	#end
                    #if("$!globals.get($task,6)" !="0")
	                    <span class="sFinish">
	                    	$!globals.get($task,6)条评论




	                    </span>
                    #end	
                </li>
                #end
            </ul>
        </div>
        <!-- 已完成任务 End -->
    </div>
     <div class="addWrap pop-layer" id="addTaskDiv" style="display: none;"></div>
     
    <!--  任务task 分割线End-->
<script type="text/html" id="copyAddTaskFast">        
    <!-- 弹出添加层 add 分割线 Start -->
    	<div class="a-title">
        	<span class="lf pr">
            	<b class="icons pa"></b>
            	新建任务
            </span>
        </div>
        <ul class="aU">
            <li>
            	<i class="lf iM">任务内容：</i>
                <textarea class="lf areaTxt" id="fastTitle" name="fastTitle"></textarea>
            </li>
        	<li>
            	<i class="lf iM">负责人员：</i>
                <select name="fastExecutor" id="fastExecutor">
	        		<option value="">暂不指定</option>
	        		#foreach($employeeId in $itemBean.participant.split(","))
	        			#if("$!employeeId"!="")
	        			<option value="$employeeId">$!globals.getEmpFullNameByUserId($employeeId)</option>
	        			#end
	                #end
	        	</select>
            </li>
            <li>
            	<i class="lf iM">截止时间：</i>
            	<span class="lf no-p">
            		<input class="lf" type="radio" name="taskEndTimeFlag" checked="checked" value="0" onclick="taskEndTimeSel('0')" id="unPointTime"><label class="lf" for="unPointTime">不指定(项目结束时间)</label>
            	</span>
               	<span class="lf no-p">
               		<input class="lf" type="radio" name="taskEndTimeFlag" value="1" onclick="taskEndTimeSel('1')" id="pointTime"><label class="lf" for="pointTime">指定时间</label>
               	</span>
                <span class="lf pr icon-txt" id="showEndTime" style="display: none;">
                	<input class="txtIcon ip_txt" type="text" id="fastEndTime" name="fastEndTime" value='$itemBean.endTime' defValue="$itemBean.endTime" isAddChild="fasle"  onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly"/>
                    <b class="icons pa bDate" onclick="WdatePicker({el:$dp.$('fastEndTime')})"></b>
                </span>
            </li>
			<input type="hidden" id="taskClientId" name="taskClientId" value="">
        </ul>
        <div class="btn-items-wp">
        	<span class="btn-items celBtn" onclick="closeLayer(this);">取消</span>
	        <span class="btn-items conBtn" onclick="fastTaskSubmit('true',this);">确定</span>
        </div>
    <!-- 弹出添加层 add 分割线 End --> 
</script>

