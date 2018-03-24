<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加</title>
<link type="text/css" rel="stylesheet" href="/style/css/taskAssign.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/oaCalendarAdd.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/crm_operation.js"></script>
<script type="text/javascript" src="/js/crm/crm_attachUpload.vjs"></script>
<script type="text/javascript" src="/js/crm/crm_taskAssignOp.js"></script>
<script type="text/javascript" src="/js/oa/oaCalendarAdd.js"></script>
<input type="hidden" id="nowHead" value="$!nowHead" />
<style type="text/css">

.bd li input.inp{width:387px;}
select{height: 22px;width: 100px;}
.subbox_w700{width:auto;}
.inputbox li{width:100%;}
.inputbox li>div{overflow:hidden;}
.inputbox li span{width: 85px;}
.boxbg2 .subbox{border:0;}
.l-cbox{float:left;display:inline-block;overflow:hidden;margin-right:5px;cursor:pointer;}
.l-cbox .r-cbox{float:left;margin-top:3px;}
.l-cbox>em{float:left;display:inline-block;}


</style>

<script type="text/javascript">
function finishTask(){
	window.location.href='/CRMTaskAssignAction.do?operation=7&isDetail=true&finishStatus=true&taskAssignId=$!taskAssignBean.id';
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
<input type="hidden" name="taskStatus" id="taskStatus" value="$!taskAssignBean.taskStatus">
<input type="hidden" name="relationId" id="relationId" value="$!taskAssignBean.id">
<div class="boxbg2 subbox_w700">
	<div class="subbox cf" >
		<div class="head_btns"></div>
		<div class="bd">
			<div class="inputbox">
				<div class="contactDiv">
					<ul>
						<!-- 
						<li>
							<span>标题：</span>
							<div>
								#if("$!isDetail" == "true")
									<i>$!taskAssignBean.title</i>
								#else
									<input type="text" id="title" name="title" value="$!taskAssignBean.title" style="width:400px;" />
								#end 
							</div>
						</li>
						 -->
						<li>
							<span>关联客户：</span>
							<div>
								<input type="hidden" id="ref_id" name="ref_id" value="$!taskAssignBean.ref_id">
								#if("$!isDetail" == "true")
									<i>$!clientName</i>
								#else
									#if("$!clientId"!="")
										<input name="ref_idName"  id="ref_idName" value="$!clientName" type="text" class="inp_w" style="border: 0;width:387px;" readonly="readonly"/>
									#else
										<input  name="ref_idName"  id="ref_idName" value="$!clientName" type="text" class="inp_w" style="border-right: 0;width:387px;" readonly="readonly" ondblclick="publicPopSelect('CrmClickGroup','ref_id','popDiv','false')" />
										<a href="#" style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%" onclick="publicPopSelect('CrmClickGroup','ref_id','popDiv','false')"></a>
									#end 
								#end
							</div>
						</li>
						<li>
							<span>优先级别：</span>
							#if("$!isDetail" == "true" || "$loginBean.id" != "$!taskAssignBean.createBy")
								<input type="hidden" id="priority" name="priority" value="$!taskAssignBean.priority">
								<i>#if("$!taskAssignBean.priority"  == "high")高 #elseif("$!taskAssignBean.priority" == "low")低 #else中 #end</i>
							#else
								<div style="float:left;display:inline-block;">
									<label class="l-cbox">
										<input type="radio" class="r-cbox" value="high" name="priority" #if("$!taskAssignBean.priority"  == "high") checked="checked" #end/>
										<em>高</em>
									</label>
									<label class="l-cbox">
										<input type="radio" class="r-cbox"  value="middle" name="priority" checked="checked"/>
										<em>中</em>
									</label>
									<label class="l-cbox">
										<input type="radio" class="r-cbox"  value="low" name="priority" #if("$!taskAssignBean.priority" == "low") checked="checked" #end/>
										<em>低</em>
									</label>
								</div>
							#end
						</li>
						<li>
							<span>完成时间：</span>
							#if("$!isDetail" == "true" || "$loginBean.id" != "$!taskAssignBean.createBy")
								<input type="hidden"  id="finishTime" name="finishTime" value="$!taskAssignBean.finishTime" />
								<i>$!taskAssignBean.finishTime</i>
							#else
								<input type="text"  id="finishTime" name="finishTime"  class="inp_w" value="$!taskAssignBean.finishTime" onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd '});" style="width: 196px;"/>
							#end
						</li>
						<li>
							<span>执行人员：</span>
							<input type="hidden" id="userId" name="userId" value="$!taskAssignBean.userId">
							#if("$!isDetail" == "true")
								<i>$!globals.getEmpFullNameByUserId($!taskAssignBean.userId)</i>
							#else
								<div>
									#if("$loginBean.id" == "$!taskAssignBean.createBy")
									<input name="userIdName"  id="userIdName" value="$!globals.getEmpFullNameByUserId($!taskAssignBean.userId)" type="text" class="inp_w" readonly="readonly" ondblclick="publicPopSelect('userGroup','userId','popDiv','false')" style="border-right:0;" />
									<a href="#" style="border:1px solid #bbb;height:19px;float: left;margin:0;border-left:0;background: url(/style1/images/St.gif)  no-repeat right 50%" onclick="publicPopSelect('userGroup','userId','popDiv','false')">
									</a>
									#else
										<input name="userIdName"  id="userIdName" value="$!globals.getEmpFullNameByUserId($!taskAssignBean.userId)" type="hidden"/>
										<i>$!globals.getEmpFullNameByUserId($!taskAssignBean.userId)</i>
									#end
								</div>
							#end
						</li>
						<li>
							<span>任务类型：</span>
							#if("$!isDetail" == "true")
								<input type="hidden" id="taskType" name="taskType" value="$!taskAssignBean.taskType">
								<i>$!globals.getEnumerationItemsDisplay("CRMTaskType","$!taskAssignBean.taskType") </i>
							#else
								<select style="width: 202px;" id="taskType" name="taskType">
									<option value="">请选择</option>
									#foreach($item in $globals.getEnumerationItems("CRMTaskType"))	 
					        	    	<option value="$item.value" #if("$!taskAssignBean.taskType" == "$item.value") selected="selected" #end>$item.name</option>
							        #end
								</select>
							#end
							
						</li>
						<li>
							<span>任务内容：</span>
							#if("$!isDetail" == "true")
								<input type="hidden" name="content"  id="content" value="$!taskAssignBean.content">
								<i>$!taskAssignBean.content</i>
							#else
							<textarea name="content"  id="content" style="width:400px;height:70px;">$!taskAssignBean.content</textarea>
							#end
						</li>
						#if("$!calendarBean"!="" && "$!isDetail" == "true")
						<li style="width: 630px;">
							<span>下次回访日程：</span> 
							<i relationId="$!calendarBean.relationId" class="calendarDetail">关联日程</i>
						</li>
						#end
						<!-- <li><span><font id='$row.display.get("$globals.getLocale()")' color="red">*</font>状态：</span><input  type="text" class="inp_w" value="" /></li> -->
						#if("$!taskAssignBean.taskStatus" == "0")
							<li style="width: 630px;">
								<span>任务状态：</span> <i>已处理</i>
							</li>
							#if("$!isDetail" == "true")
								<li style="width: 630px;margin-bottom: 3px;height: 75px;">
									<span>反馈情况：</span>
									 <i>$!taskAssignBean.summary</i>
								</li>
							#else
								<li>
									<span>反馈情况：</span>
									<textarea name="summary"  id="summary" style="width:400px;height:70px;">$!taskAssignBean.summary</textarea>
								</li>
							#end
						#else
							#if("$!isDetail" == "true" && "$loginBean.id" == "$!taskAssignBean.userId")
							<li style="width: 630px;">
								<span>完成任务：</span> <i><button class="btn btn-mini" type="button" onclick="finishTask()" id="addCalendarBtn">完成</button></i>
							</li>
								
							#end
						#end
						
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
</body>
</html>
