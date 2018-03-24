<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加</title>
<link type="text/css" rel="stylesheet" href="/style/css/taskAssign.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/oaCalendarAdd.css"/>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/crm/crm_attachUpload.vjs"></script>
<script type="text/javascript" src="/js/crm/crm_taskAssignOp.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/oa/oaCalendarAdd.js"></script>
<style type="text/css">

.bd li input.inp{width:387px;}
select{height: 23px;}
.subbox_w700{width:auto;}
.inputbox li{width:100%;}
.inputbox li>div{overflow:hidden;}
.inputbox li span{width: 85px;}
.boxbg2 .subbox{border:0;}
.l-cbox{float:left;display:inline-block;overflow:hidden;margin-right:5px;cursor:pointer;}
.l-cbox .r-cbox{float:left;margin-top:3px;}
.l-cbox>em{float:left;display:inline-block;}


.bg-icons{background-image:url(/style/images/item/icons.png);background-repeat:no-repeat;}
.lf{float:left;display:inline-block;}
.b-remind{width:16px;height:17px;background-position:-159px -155px;cursor:pointer;margin:4px 7px 0 7px;}
.b-remind-h{background-position:-123px -155px;}

</style>


<script type="text/javascript">

$(function(){
	jQuery("#summary").focus();

})

function submitBefore(){
	if(jQuery.trim(jQuery("#summary").val()) == ""){
		asyncbox.tips('反馈情况不能为空');
		jQuery("#summary").focus();
		return false;
	}

	form.submit();
}

</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>

<form name="form" action="/CRMTaskAssignAction.do" method="post" target="formFrame" >
<input type="hidden" name="operation" value="$globals.getOP('OP_UPDATE')">
<input type="hidden" name="id" id="id" value="$!taskAssignBean.id">
<input type="hidden" name="createBy" id="createBy" value="$!taskAssignBean.createBy">
<input type="hidden" name="createTime" id="createTime" value="$!taskAssignBean.createTime">
<input type="hidden" name="relationId" id="relationId" value="$!taskAssignBean.id">
<div class="boxbg2 subbox_w700">
	<div class="subbox cf" >
		<div class="head_btns"></div>
		<div class="bd">
			<div class="inputbox">
				<div class="contactDiv">
					<ul>
						<li>
							<span>关联客户：</span>
							<div>
								<input type="hidden" id="ref_id" name="ref_id" value="$!taskAssignBean.ref_id">
								<input type="hidden" id="ref_idName" value="$!clientName">
								<i>$!clientName</i>
								
							</div>
						</li>
						<li>
							<span>优先级别：</span>
							<i>
								#if("$!taskAssignBean.priority"  == "high")高 #elseif("$!taskAssignBean.priority" == "low")低 #else中 #end
								<input type="hidden" id="priority" name="priority" value="$!taskAssignBean.priority">
							</i>
						</li>
						<li>
							<span>完成时间：</span>
								<i>$!taskAssignBean.finishTime</i>
								<input type="hidden"  id="finishTime" name="finishTime" value="$!taskAssignBean.finishTime" />
						</li>
						<li>
							<span>执行人员：</span>
								<i>$!globals.getEmpFullNameByUserId($!taskAssignBean.userId)</i>
								<input type="hidden" id="userIdName" value="$!globals.getEmpFullNameByUserId($!taskAssignBean.userId)">
								<input type="hidden" id="userId" name="userId" value="$!taskAssignBean.userId">
						</li>
						<li>
							<span>任务类型：</span>
								<i>$!globals.getEnumerationItemsDisplay("CRMTaskType","$!taskAssignBean.taskType") </i>
								<input type="hidden" id="taskType" name="taskType" value="$!taskAssignBean.taskType">
							
						</li>
						<li>
							<span>任务内容：</span>
							<i>$!taskAssignBean.content</i>
							<input type="hidden" id="content" name="content" value="$!taskAssignBean.content">
						</li>
						<li style="width: 630px;">
							<span>任务状态：</span> <i>已处理</i>
							<input  type="hidden" id="taskStatus" name="taskStatus" value="0">
						</li>
						<li style="width: 630px;">
							<span>下次回访日程：</span> <i><button class="btn btn-mini" type="button" onclick="addCalendar()" id="addCalendarBtn">添加日程</button></i>
						</li>
						<li>
							<span>反馈情况：</span>
							<textarea name="summary"  id="summary" style="width:400px;height:70px;" placeholder="请输入描述内容"></textarea>
						</li>
					</ul>
				</div>
				<p></p>
			</div>
			<div id="affixDiv" style="display: none;">
				<div class="listRange_1_photoAndDoc_1"><span></span><button name="affixbuttonthe222" type="button" onClick="upload('AFFIX');" class="b4">$text.get("upload.lb.affixupload")</button>
				<ul id="affixuploadul"></ul>
			</div>
		
		</div>
	</div>
     
</div>
<div class="add-calendar" id="addCalendar" style="display:none;"></div>
</form>
<script type="text/html" id="copyAddCalendar">   
	<input type="hidden" name="myFormId" id="myFormId" />				
	<input class="inp-txt" type="text" id="calendarTitle" maxlength="20" placeholder="新的日程安排" style="width: 200px;height:20px;" />
	<div class="point-block">
		<label class="lf">分类</label>		
			<select class="lf point-s" id="typeNames">
				<option value="客户日程">客户日程</option>
			</select>
	</div>
	<div class="point-block">
		<label class="lf">开始</label>
		<input class="lf point-i" id="calendarStratTime" type="text"  onClick="selectDate('calendarStratTime')" readonly="readonly"/>
	</div>
	<div class="point-block">
		<label class="lf">结束</label>		
		<input class="lf point-i" id="calendarFinishTime" type="text"  onClick="selectDate('calendarFinishTime')" readonly="readonly"/>
	</div>
	<div class="point-block" id="setDay">
		<b class="lf bg-icons b-remind" bg="f" title='设置提醒'  id="bgg" onclick="showTime(this);"></b>
		<span class="lf" id="setTime" style="display:none;">		
		<input class="lf point-i" id="dayTime" style="width: 80px;" type="text" onClick="openInputDate(this);" readonly="readonly"/>
	    <select class="lf point-s" id="hours" name="hours" style="margin:0 0 0 -1px;">
	    	#foreach($log in [0..23])
	    	#if($log < 10)		    			    	
	    	<option value="$!log" #if("$log" == "8")  selected="selected" #end>0$!log</option>		    	
	    	#else
	    	<option value="$!log">$!log</option>
	    	#end
	    	#end
	    </select>
	    <select class="lf point-s" id="minutes" name="minutes" style="margin:0 0 0 -1px;">
			<option value="0">00</option>
	    	#foreach($log in [10,20,30,40,50])
	    	<option value="$!log">$!log</option>
	    	#end
	    </select>
	    </span>
	</div>
	<div class="point-block share-d pr" >
		<label class="lf">客户</label>
		<input class="lf point-i" id="calendarClientId" name="calendarClientId" type="hidden"  value="$!taskAssignBean.ref_id"/>				
		<input class="lf point-i" id="calendarClientIdName" name="calendarClientIdName" value="$!clientName" type="text" readonly="readonly"/>
	</div>
	<div class="point-block">
		<input class="lf btn-add" type="button" id="dns_btn" value="添加" onclick="saveForm();" />
		<input class="lf btn-cel" type="button" value="取消" onclick="hideForm();" />
		<input id="delId" style="display: none;" class="lf btn-del" type="button" value="删除" onclick="delForm();" /> 
	</div>
	<b class="arrow-point"></b>
</script>
</body>
</html>
