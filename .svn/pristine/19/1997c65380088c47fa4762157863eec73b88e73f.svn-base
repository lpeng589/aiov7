<!-- 
<span class="type-s">
	<i class="task-tags sel" tasktype="selfPerosn">本人</i>
	<i class="task-tags " tasktype="deptPerson">部门</i>
</span> -->
	#foreach($items in $itemsList)	
	<div class="mt-item">
		<a href="#" class="i-a">
			<img class="de-img" src="$globals.checkEmployeePhoto("48",$items.executor)" alt="">
		</a>
		<a href="/OATaskAction.do?operation=5&taskId=$task.id&addTHhead=true" class="n-a">$items.title</a>
		<span class="t-op">发起：$globals.getEmpFullNameByUserId($items.createBy)</span>
		<div class="d-mc">
			<b class="icon-1"></b>
			<div class="d-list">
				<span class="l-sp">负责人：</span>
				<span class="c-sp">$globals.getEmpFullNameByUserId($items.executor)</span>
			</div>
			<div class="d-list">
				<span class="l-sp">参与人：</span>
				<span class="c-sp"> 
				#foreach($log in $items.participant.split(","))
				$globals.getEmpFullNameByUserId($log);
				#end
				</span>
			</div>
			<div class="d-list">
				<span class="l-sp">执行时间：</span>
				<span class="c-sp">$items.beginTime - $items.endTime</span>
			</div>
			<div class="d-list">
				<span class="l-sp">项目描述：</span>
				<span class="c-sp">$items.remark</span>
			</div>
			#if("$items.createBy" == "$loginBean.id")
	        <i class="pingfen" id="pingfen_$items.id" title="评分" workId="$items.Id" createPlanDate="$items.createTime" sroceManId="$items.executor">评分</i>
			#end
		</div>
		<p class="c-time">$items.createTime</p>
	</div>
	
	#end