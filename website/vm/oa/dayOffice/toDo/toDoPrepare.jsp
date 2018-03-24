<div class="add-calendar">
	<input class="inp-txt" type="text" maxlength="20" value="$!toDoList.get('title')" style="width: 200px;height:20px;" />
	<div class="point-block">
		<label class="lf">分类</label>		
		<input class="lf point-i" type="text" value="$toDoList.get('type')" />
	</div>
	<div class="point-block" align="center">
		<label class="lf">开始</label>
		<input class="lf point-i" type="text" value="$toDoList.get('finishTime').substring(0,10)" />
	</div>
	<div class="point-block" align="center">
		<label class="lf">结束</label>
		<input class="lf point-i" type="text" value="$toDoList.get('finishTime').substring(0,10)" />
	</div>
	<div class="point-block share-d pr">
		<label class="lf">客户</label>
		<input class="lf point-i" type="text" value="$toDoList.get('relationId')" />
	</div>
	<div class="point-block share-d pr">
		<span style="float: left;">已指派任务给:</span>
		<input class="lf point-i" style="float: right;margin-right: 6px;width:130px " type="text" value="$toDoList.get('ref_taskId')" />
		
	</div>	
	<div class="point-block share-d pr">
		<span style="float: left;">附件:</span>
		<ul id="affixuploadul_todo" class="add-tu" style="padding:0;">
			#foreach($log in $!toDoList.get('uploadFile').split(";"))
			<li class="file_li" >
			<div class="showAffix">$log</div>
			<a class="download-a-btn" href="/ReadFile?fileName=$log&realName=$log&tempFile=false&type=AFFIX&tableName=OAToDo" target="_blank">下载</a>
			</li>
			#end		
		</ul>
		
	</div>
	<div class="point-block">
		<input class="lf btn-cel" type="button" value="取消" onclick="closeToDo();" />
	</div>
	<b class="arrow-point"></b>	       
</div>
