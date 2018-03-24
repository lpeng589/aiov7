<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/crm_operation.js"></script>
<script type="text/javascript" src="/js/crm/crm_attachUpload.vjs"></script>
<script type="text/javascript" src="/js/crm/jquery.select.js"></script>
<script type="text/javascript" src="/js/crm/crm_taskAssignOp.js"></script>
<input type="hidden" id="nowHead" value="$!nowHead" />
<style type="text/css">
	.bd li{height: 23px;}
	.bd li input{height: 15px;}
	select{height: 22px;width: 100px;}
	.inputbox li{width: 290px;}
	.inputbox li span{width: 65px;}
</style>

</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/CRMTaskAssignAction.do" method="post" target="formFrame" >
<input type="hidden" name="operation" value="$globals.getOP('OP_ADD')">
<div class="boxbg2 subbox_w700" style="margin: 5px auto;width: 600px;">
	<div class="subbox cf" >
		<div class="head_btns"></div>
		<div class="bd">
			<div class="inputbox">
				<div class="contactDiv">
					<ul>
						<li style="width: 630px;">
							<span>标题：</span>
							<div>
								<input type="text" id="title" name="title" style="width: 500px;" value="$!taskAssignBean.title"> 
							</div>
						</li>
						<li style="width: 630px;">
							<span>客户：</span>
							<div>
								<input type="hidden" id="ref_id" name="ref_id" value="$!taskAssignBean.ref_id"> 
								<input  name="ref_idName"  id="ref_idName" value="$!clientName" type="text" class="inp_w" style="border-right: 0;width: 487px;" readonly="readonly" ondblclick="publicPopSelect('CrmClickGroup','ref_id','popDiv','false')" />
								<a href="#" style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%" onclick="publicPopSelect('CrmClickGroup','Ref_id','popDiv','false')">
								</a>
							</div>
						</li>
						<li>
							<span>优先级：</span>
							<input type="radio" value="high" style="border: 0;float: none" name="priority" #if("$!taskAssignBean.priority" == "high") checked="checked" #end/>高&nbsp;
							<input type="radio" value="middle" style="border: 0;float: none" name="priority" checked="checked"/>中&nbsp;
							<input type="radio" value="low" style="border: 0;float: none" name="priority" #if("$!taskAssignBean.priority" == "low") checked="checked" #end/>低
						</li>
						<li>
							<span>完成时间：</span>
							<input type="text"  id="finishTime" name="finishTime"  class="inp_w" value="$!taskAssignBean.finishTime" onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd '});"/>
						</li>
						<li style="width: 630px;">
							<span>执行人：</span>
							<div>
								<input type="hidden" id="userId" name="userId" value="$!taskAssignBean.userId"> 
								<input  name="userIdName"  id="userIdName" value="$!globals.getEmpFullNameByUserId($!taskAssignBean.userId)" type="text" class="inp_w" style="border-right: 0;width: 487px;" readonly="readonly" ondblclick="publicPopSelect('userGroup','userId','popDiv','false')" />
								<a href="#" style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%" onclick="publicPopSelect('userGroup','userId','popDiv','false')">
								</a>
							</div>
						</li>
						<li style="width: 630px;margin-bottom: 3px;height: 75px;">
							<span>内容：</span>
							<textarea name="content"  id="content" style="width: 500px;height: 70px;">$!taskAssignBean.content</textarea>
						</li>
						<!-- <li><span><font id='$row.display.get("$globals.getLocale()")' color="red">*</font>状态：</span><input  type="text" class="inp_w" value="" /></li> -->
						#if("$!taskAssignBean.taskStatus" == "0")
						<li style="width: 630px;">
							任务状态：已处理
						</li>
						<li style="width: 630px;margin-bottom: 3px;height: 75px;">
							<span>反馈情况：</span>
							<textarea name="content"  id="content" style="width: 500px;height: 70px;">$!taskAssignBean.summary</textarea>
						</li>
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

</form>
</body>
</html>
