<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加</title>
<link type="text/css" rel="stylesheet" href="/style/css/taskAssign.css"/>
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
<input type="hidden" id="nowHead" value="$!nowHead" />
<style type="text/css">

.bd li input.inp{width:387px;}
select{height: 22px;width: 100px;}
.subbox_w700{width:auto;}
.inputbox li{width:100%;}
.inputbox li>div{overflow:hidden;}
.inputbox li span{width: 65px;}
.boxbg2 .subbox{border:0;}
.l-cbox{float:left;display:inline-block;overflow:hidden;margin-right:5px;cursor:pointer;}
.l-cbox .r-cbox{float:left;margin-top:3px;}
.l-cbox>em{float:left;display:inline-block;}
</style>

</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/CRMTaskAssignAction.do" method="post" target="formFrame" >
<input type="hidden" name="operation" value="$globals.getOP('OP_ADD')">
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
								<input type="text" id="title" name="title" value="" style="width:400px;" /> 
							</div>
						</li>
						 -->
						<li>
							<span>关联客户：</span>
							<div>
								<input type="hidden" id="ref_id" name="ref_id" value="$!clientId">
								#if("$!clientId"!="")
									<input name="ref_idName"  id="ref_idName" value="$!clientName" type="text" class="inp_w" style="border: 0;width:387px;" readonly="readonly"/>
								#else
									<input  name="ref_idName"  id="ref_idName" value="" type="text" class="inp_w" style="border-right: 0;width:387px;" readonly="readonly" ondblclick="publicPopSelect('CrmClickGroup','ref_id','popDiv','false')" />
									<a href="#" style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%" onclick="publicPopSelect('CrmClickGroup','ref_id','popDiv','false')"></a>
								#end 
							</div>
						</li>
						<li>
							<span>优先级别：</span>
							<div style="float:left;display:inline-block;">
								<label class="l-cbox">
									<input type="radio" class="r-cbox" value="high" name="priority"/>
									<em>高</em>
								</label>
								<label class="l-cbox">
									<input type="radio" class="r-cbox"  value="middle" name="priority" checked="checked"/>
									<em>中</em>
								</label>
								<label class="l-cbox">
									<input type="radio" class="r-cbox"  value="low" name="priority" />
									<em>低</em>
								</label>
							</div>
						</li>
						<li>
							<span>完成时间：</span>
							<input type="text"  id="finishTime" name="finishTime"  class="inp_w" value="" onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd '});" style="width: 196px;"/>
						</li>
						<li>
							<span>执行人员：</span>
							<div>
								<input type="hidden" id="userId" name="userId" value=""> 
								<input name="userIdName"  id="userIdName" value="" type="text" class="inp_w" readonly="readonly" ondblclick="publicPopSelect('userGroup','userId','popDiv','false')" style="border-right:0;" />
								<a href="#" style="border:1px solid #bbb;height:19px;float: left;margin:0;border-left:0;background: url(/style1/images/St.gif)  no-repeat right 50%" onclick="publicPopSelect('userGroup','userId','popDiv','false')">
								</a>
							</div>
						</li>
						<li>
							<span>任务类型：</span>
							<select style="width: 202px;" id="taskType" name="taskType">
								<option value="">请选择</option>
								#foreach($item in $globals.getEnumerationItems("CRMTaskType"))	 
				        	    	<option value="$item.value">$item.name</option>
						        #end
							</select>
						</li>
						
						<li>
							<span>任务内容：</span>
							<textarea name="content"  id="content" style="width:400px;height:70px;"></textarea>
						</li>
						<!-- <li><span><font id='$row.display.get("$globals.getLocale()")' color="red">*</font>状态：</span><input  type="text" class="inp_w" value="" /></li> -->
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

</form>
</body>
</html>
