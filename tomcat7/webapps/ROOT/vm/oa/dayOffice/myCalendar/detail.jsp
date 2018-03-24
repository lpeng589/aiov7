<input type="hidden" name="myFormId" id="myFormId" value="$!bean.id" />				
<input class="inp-txt" type="text" id="calendarTitle" maxlength="20" style="width: 200px;height:20px;" value="$!bean.title"/>

<div class="point-block">
	<label class="lf">分类</label>		
		<select class="lf point-s" id="typeNames">
			<option value="客户日程">客户日程</option>
		</select>
		<input class="lf btn-add" type="button" style="margin-left: 50px;" name="finishStatus" id="finishStatus" #if("$!bean.finishStatus" == "1")value="未完成"#else value="完成"#end onclick="finishEvent('$!bean.id');" />
		
</div>
<div class="point-block" align="center">
	<label class="lf">开始</label>
	<input class="lf point-i" id="calendarStratTime" type="text" value="$!bean.stratTime"/>
</div>
<div class="point-block">
	<label class="lf">结束</label>		
	<input class="lf point-i" id="calendarFinishTime" type="text" value="$!bean.finishTime"/>
</div>
			
#if("$!bean.alertTime" !="")
<div class="point-block">
	<label class="lf">提醒时间</label>
	<input class="lf point-i" id="calendarFinishTime" type="text"  value="$!bean.alertTime" style="width: 155px;"/>		
</div>
#end
<div class="point-block share-d pr" >
	<label class="lf">客户</label>
	<input class="lf point-i" id="calendarClientIdName" name="calendarClientIdName" value="$!calendarClientName" type="text" readonly="readonly"/>
</div>
<div class="point-block">
	<input class="lf btn-cel" type="button" value="取消" onclick="hideForm();" />
	#if("$!calendarFlag" == "detail")
	<input id="delId" class="lf btn-del" type="button" value="删除" onclick="delForm('客户日程');" /> 
	#end
</div>
<b class="arrow-point"></b>