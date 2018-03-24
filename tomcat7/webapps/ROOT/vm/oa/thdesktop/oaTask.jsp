<!-- 
<span class="type-s">
	<i class="task-tags sel" tasktype="selfPerosn">本人</i>
	<i class="task-tags " tasktype="deptPerson">部门</i>
</span> -->
	#foreach($task in $tasksList)
	
	<div class="mt-item">
		<a href="#" class="i-a">
			<img class="de-img" src="$globals.checkEmployeePhoto("48",$task.executor)" alt="">
		</a>
		<a href="/OATaskAction.do?operation=5&taskId=$task.id&addTHhead=true" class="n-a">$task.title</a>
		<span class="t-op">发起：$globals.getEmpFullNameByUserId($task.createBy)</span>
		<div class="d-mc">
			<b class="icon-1"></b>
			<div class="d-list">
				<span class="l-sp">负责人：</span>
				<span class="c-sp">$globals.getEmpFullNameByUserId($task.executor)</span>
			</div>
			<div class="d-list">
				<span class="l-sp">执行时间：</span>
				<span class="c-sp">$task.beginTime - $task.endTime</span>
			</div>
			<div class="d-list">
				<span class="l-sp">任务描述：</span>
				<span class="c-sp">$task.remark</span>
			</div>
			#if("$task.createBy" == "$loginBean.id")
	        <i class="pingfen" id="pingfen_$task.id" title="评分" workId="$task.Id" createPlanDate="$task.createTime" sroceManId="$task.executor">评分</i>
			#end
		</div>
		<p class="c-time">$task.createTime</p>
	</div>
	
	#end