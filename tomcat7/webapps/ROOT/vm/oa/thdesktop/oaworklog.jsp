
#foreach($workLog in $workLogBeanList)
#if($velocityCount < 5)
                             			
	<div class="mt-item">
		<a href="#" class="i-a">
			<img class="de-img" src="$globals.checkEmployeePhoto('48',$workLog.createBy)" alt="">
		</a>
		<span>$globals.getEmpFullNameByUserId("$workLog.createBy")</span>
		<span class="t-op"><a href="/OAWorkLogAction.do?operation=5&workLogId=$workLog.id&addTHhead=true">日志：$!workLog.workLogDate</a></span>
		<div class="d-mc">	
			<p class="tw-p">计划</p>
			<ul class="wl-ul">
			 #set($workLogDet = "")
	         #set($workLogDet = $!workLogDetMap.get("$workLog.id"))
	         #if($!workLogDet !="")
	           	#foreach($det in $!workLogDet.get("2"))
	           		<li>$globals.get($det,1)
	           		<i class="pingfen" id="pingfen_$globals.get($det,0)" title="评分" workId="$globals.get($det,0)" createPlanDate="$!workLog.createTime" sroceManId="$workLog.createBy">评分</i>
	           		</li>           		
	           	#end
			 #end
			</ul>
			<p class="tw-p">总结</p>
			<ul class="wl-ul">
			#if($!workLogDet !="")
	        	#foreach($det in $!workLogDet.get("1"))       		
	        		<li><em>$globals.get($det,1)</em>
	        		#if("$!globals.get($det,6)"!="")<em style="color: red;margin-left: 10px;">$!globals.get($det,6)%</em>#end
	        		<i class="pingfen" id="summary_$globals.get($det,0)" title="评分" workId="$globals.get($det,0)" createPlanDate="$!workLog.createTime" sroceManId="$workLog.createBy">评分</i>	
	        		</li>       		
	        	#end
	       	#end
			</ul>			
		</div>
		<p class="c-time">$!workLog.createTime</p>
	</div>

#end
#end

               