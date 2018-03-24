<input type="hidden" name="myFormId" id="myFormId" value="$!globals.get($!calendarList,0)" />	
<input type="hidden" id="calendarTaskId" name="calendarTaskId" value="$!globals.get($!calendarList,12)"/>					
	<input class="inp-txt" type="text" id="calendarTitle"  #if("$!calendarFlag" == "detail")value="$!globals.get($!calendarList,1)"#else placeholder="新的日程安排"#end style="width: 200px;height:20px;" />	
	<div class="point-block">
		<label class="lf">分类</label>		
			<select class="lf point-s" id="typeNames">
				#if("$!calendarFlag" == "")
					<option value="客户日程" >客户日程</option>
				#else
					<option value="默认分类" #if("$!globals.get($!calendarList,2)" == "默认分类") selected="selected" #end>默认分类</option>
					<option value="客户日程" #if("$!globals.get($!calendarList,2)" == "客户日程") selected="selected" #end>客户日程</option>
					
					#foreach($log in $!typeNames)
					#if("$!globals.get($log,3)" != "")
					<option id="select_$!globals.get($!log,0)" value="$!globals.get($!log,1)" #if("$!globals.get($!calendarList,2)" == "$!globals.get($!log,1)")selected="selected"#end>$!globals.get($!log,1)</option>
					#end
					#end
				#end
			</select>
			#if("$!calendarFlag" == "detail")
			<input class="lf btn-add" type="button" style="margin-left: 50px;" name="finishStatus" id="finishStatus" #if("$!globals.get($!calendarList,11)" == "1")value="未完成"#else value="完成"#end onclick="finishEvent('$!globals.get($!calendarList,0)');" />
			#end
	</div>
	#if("$!calendarFlag" == "")
	<div class="point-block" align="center">
		<label class="lf">开始</label>
		<input class="lf point-i" id="calendarStratTime" type="text"  onClick="selectDate('calendarStratTime')"readonly="readonly" value="$!calendarStratTime"/>
	</div>
	<div class="point-block">
		<label class="lf">结束</label>		
		<input class="lf point-i" id="calendarFinishTime" type="text" onClick="selectDate('calendarFinishTime')" readonly="readonly" value="$!calendarFinishTime" />
	</div>
	#else
	<div class="point-block" align="center">
		<label class="lf">开始</label>
		<input class="lf point-i" id="calendarStratTime" type="text"  #if("$!calendarFlag" == "detail") value="$!globals.get($!calendarList,3).substring(0,10)"#else value="$!calendarStratTime"#end onClick="openInputDate(this);" readonly="readonly"/>
	</div>
	<div class="point-block">
		<label class="lf">结束</label>		
		<input class="lf point-i" id="calendarFinishTime" type="text"  #if("$!calendarFlag" == "detail") value="$!globals.get($!calendarList,4).substring(0,10)"#else value="$!calendarFinishTime"#end onClick="openInputDate(this);" readonly="readonly"/>
	</div>
	#end
	#if("$!dayTimes" != "")
	<div class="point-block" id="setDay">
		<b class="lf bg-icons b-remind b-remind-h" bg="t" title='取消提醒' id="bgg" onclick="showTime(this);"></b>
		<span class="lf" id="setTime" >		
		<input class="lf point-i" id="dayTime" style="width: 80px;" value="$!dayTimes.substring(0,10)" type="text" onClick="WdatePicker({lang:'$globals.getLocale()'});" readonly="readonly"/>
	    <select class="lf point-s" id="hours" name="hours" style="margin:0 0 0 -1px;">
	    	#foreach($log in [0..23])
	    	#if($log < 10)		    			    	
	    	<option value="0$!log" #if("$!hours" == "$!log")selected="selected" #end>0$!log</option>		    	
	    	#else
	    	<option value="$!log" #if("$!hours" == "$!log")selected="selected"#end>$!log</option>
	    	#end
	    	#end
	    </select>
	    <select class="lf point-s" id="minutes" name="minutes" style="margin:0 0 0 -1px;">
			<option value="00">00</option>
	    	#foreach($log in [10,20,30,40,50])
	    	<option value="$!log" #if("$!mitues" == "$!log")selected="selected"#end>$!log</option>
	    	#end
	    </select>
	    </span>
	</div>
	#else
	<div class="point-block" id="setDay">
		<b class="lf bg-icons b-remind " bg="f" title='设置提醒'  id="bgg" onclick="showTime(this);"></b>
		<span class="lf" id="setTime" style="display:none;">		
		<input class="lf point-i" id="dayTime" style="width: 80px;" value="$!dayTimes.substring(0,10)" type="text" onClick="WdatePicker({lang:'$globals.getLocale()'});" readonly="readonly"/>
	    <select class="lf point-s" id="hours" name="hours" style="margin:0 0 0 -1px;">
	    	#foreach($log in [0..23])
	    	#if($log < 10)		    			    	
	    	<option value="0$!log" #if("$log" == "8")selected="selected" #end>0$!log</option>		    	
	    	#else
	    	<option value="$!log" >$!log</option>
	    	#end
	    	#end
	    </select>
	    <select class="lf point-s" id="minutes" name="minutes" style="margin:0 0 0 -1px;">
			<option value="00">00</option>
	    	#foreach($log in [10,20,30,40,50])
	    	<option value="$!log" >$!log</option>
	    	#end
	    </select>
	    </span>
	</div>	
	#end
	#if("$!calendarFlag" == "")
	<div class="point-block share-d pr" >
		<label class="lf">客户</label>
		<input class="lf point-i" id="calendarClientId" name="calendarClientId" type="hidden"  value="$!calendarClientId"/>				
		<input class="lf point-i" id="calendarClientName"  name="calendarClientName" value="$!calendarClientName" type="text"  #if("$!onlyShow" != "true") ondblclick="deptPopForJob('CrmClickGroup','calendarClientId','calendarClientName','fillNew');" #end/>
		#if("$!onlyShow" != "true")
		<span class="icons share-s pa" style="right:8px;top:2px" onclick="deptPopForJob('CrmClickGroup','calendarClientId','calendarClientName','fillNew');"></span>
		#end
	</div>
	#else
	<div class="point-block share-d pr" >
		<label class="lf">客户</label>
		<input class="lf point-i" id="calendarClientId" name="calendarClientId" value="$!globals.get($!calendarList,9)" type="hidden"  />
		#if("$!calendarFlag" == "new" || "$!globals.get($!calendarList,9)" == "")				
		<input class="lf point-i" id="calendarClientName" name="calendarClientName" type="text" ondblclick="deptPopForJob('CrmClickGroup','calendarClientId','calendarClientName','fillNew');" />
		<span class="icons share-s pa" style="right:8px;top:2px" onclick="deptPopForJob('CrmClickGroup','calendarClientId','calendarClientName','fillNew');"></span>
		#else
		<div id="oldClinetName" onclick="openCtDetail('$!globals.get($!calendarList,9)','$!globals.get($!calendarList,10)');" style="border: 1px solid #dfdfdf;background: #f9f9f9;border-radius: 4px;width: 180px;margin-left: 35px;cursor:pointer;text-align: left;">$!globals.get($!calendarList,10)</div>
		<span class="icons share-s pa" style="right:8px;top:2px" onclick="deptPopForJob('CrmClickGroup','calendarClientId','oldClinetName','fillOld');"></span>
		#end		
	</div>
	#end
	#if("$!calendarFlag" != "")
	<div class="point-block share-d pr">
		#if("$!globals.get($!calendarList,12)" == "")
		<span  id="appoint-other" class="appoint-other" onclick="addTask();">指派任务:</span>
		#else
		
		<span  id="appoint-other" class="appoint-other" onclick="addTask();">已指派任务给:$!globals.getSplitIndex($!globals.get($!calendarList,12),';',1)</span>
		#end
	</div>
	#end
	<div class="point-block">
		<input class="lf btn-add" type="button" id="dns_btn" #if("$!calendarFlag" == "detail")value="修改"#else value="添加"#end onclick="saveForm();" />
		<input class="lf btn-cel" type="button" value="取消" onclick="hideForm();" />
		#if("$!calendarFlag" == "detail")
		<input id="delId" class="lf btn-del" type="button" value="删除" onclick="delForm('$!globals.get($!calendarList,2)');" /> 
		#end
	</div>
	<b class="arrow-point"></b>