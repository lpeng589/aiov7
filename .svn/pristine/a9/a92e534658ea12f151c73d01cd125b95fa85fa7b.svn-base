<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="/style/css/plan.css" />
<link type="text/css" rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" src="/js/webDate.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript">
var curUserId = "" ;
var curPlanType = "day" ;
var curDate = "" ;
function clickDate(planDate){
	curDate = planDate;
	window.frames["planList"].location= "/OAWorkPlanAction.do?operation=4&userId="+curUserId+"&planType="+curPlanType+"&strDate="+curDate ;
}

function dealAsyn(){
	jQuery.close('planId');
	window.frames["planList"].location.reload()
}

function clickType(type){
	curPlanType = type;
	$("#planType a").each(function(i){
		if(type == $(this).attr("id")){
			$(this).attr("class","data_day_b") ;
		}else{
			$(this).attr("class","data_day_a") ;
		}
	});
	window.frames["planList"].location= "/OAWorkPlanAction.do?operation=4&userId="+curUserId+"&planType="+curPlanType+"&strDate="+curDate ;
}
var setting = {
	//checkable:true,
	expandSpeed : false,
	callback:{
		click : zTreeOnClick
	}
};
var zTree ;	
jQuery.get("/OAWorkPopedAction.do?type=type",function(response){
	var shuzu=response.split("||");
	var query=shuzu[1];
	var add=shuzu[2];
	var del=shuzu[3];
	var upd=shuzu[4];
	if(query=="true"){
		jQuery("#setting").show();
	}
	if(shuzu[0].length>0){
		jQuery("#leftMenuId").show();
		var zNodes =eval(shuzu[0]) ;
		zTree = $("#treeNode").zTree(setting, zNodes);
	}
});

function zTreeOnClick(event, treeId, treeNode){
	checkeds(event,treeNode);
	if(treeNode.id.length>0){
		clickTree(treeNode.id) ;
	}
}

function checkeds(event,treeNode) {
	var srcNode = zTree.getSelectedNode();
	if (treeNode.open) {
		if (srcNode) {
			zTree.expandNode(srcNode, false,null);
		}
	} else {
		if (srcNode) {
			zTree.expandNode(srcNode, true,null);
		}
	}
}

function clickTree(userId){
	curUserId = userId ;
	window.frames["planList"].location= "/OAWorkPlanAction.do?operation=4&userId="+curUserId+"&planType="+curPlanType+"&strDate="+curDate ;
}

function mdiwin(url,title){
	top.showreModule(title,url);
}

//处理旧弹出框返回值



function exePopdiv(returnValue){
	if(typeof(returnValue)=="undefined") return ;
	if(jQuery.exist('planId')){
	    jQuery.opener('planId').dealPopInfo(returnValue);
	}
}

$(function(){
	
	//alert(planList.document.body.scrollHeight);
	$("#planList").css("height",$(window).height()-70);
	
	
})
</script>

<style>
.Calendar {font-family:Verdana;background:url(/style/images/plan/beijing.gif);font-size:12px;
width:237px;height:230px;line-height:1.5em;float:left;}
.calendar_title{height:40px;padding-left:30px;padding-right:30px;}
.calendar_title{color:#FFFFFF;font-size:11px;} 
.calendar_title div{padding-top:10px;}
.Calendar table{border:0;width:232px;}
.Calendar table a{float:left;color:#6C7174;width:30px;height:20px; font-size:12px;font-family:"黑体";text-decoration:none;font-weight:bold;}
.Calendar table thead{color:#acacac;}
.Calendar table thead td {text-align:center;font-size:10px;color:#8E9396;}
.Calendar table tbody td {font-size: 11px;height:31px;text-align:center;vertical-align:middle;}
.Calendar table tbody td span{float:left;height:5px;width:5px;margin-left:13px;}
#idCalendarPre{cursor:pointer;float:left;padding-right:5px;}
#idCalendarNext{cursor:pointer;float:right;padding-right:5px;}
#idCalendarPreYear{cursor:pointer;float:left;padding-right:5px;}
#idCalendarNextYear{cursor:pointer;float:right;padding-right:5px;}
.onToday {background:#A4D143;color:#fff;}
#idCalendar td.onSelect {font-weight:bold;}
#plan_l {width: 100%;margin-right:2px; float: left; height: auto; overflow-y:auto; overflow-x:hidden; background:#ECF4FB;border:1px solid #A2CEF5; height: auto;}
#plan_top {
	width:100%;
	height:25px; margin-top:5px;text-align:center;
	color:#0000FF;
}
#plan_r {width: 100%;float: left; height:100%;}
.calendar_title{font-weight:bold;}
.calendar_title{font-size:14px;}
.calendar_title img{padding-top:5px;}
.LeftMenu_list a{text-decoration: none;}
</style>
</head>
<body onload="openDate();">
<table cellpadding="0" cellspacing="0" style="width:99%; height:100%; margin-top:10px;">
		<tr><!--左边菜单开始-->
			<td class="leftMenu" style="padding-left:10px; vertical-align:top;">
			<div id="planType" class="TopTitle" style="padding-left:80px;">
				<a id="day" href="javascript:clickType('day');" class="data_day_b" title="日计划">日</a>
				<a id="week" href="javascript:clickType('week');" class="data_day_a" title="周计划">周</a>
				<a id="month" href="javascript:clickType('month');" class="data_day_a" title="月计划">月</a>
			</div>
			<div class="Calendar" id=idCalendar">
				<div class="calendar_title">
				<div id="idCalendarPre"><img src="/style/images/plan/left.jpg"/></div>
				<div id="idCalendarNext"><img src="/style/images/plan/right.jpg"/></div>
				<div style="margin-left:45px;padding-top:12px;"><span id="idCalendarYear"></span> $text.get("com.date.year") <span id="idCalendarMonth"></span> $text.get("oa.calendar.month")</div>
				</div>
				<div style="float:left;">
				<table cellspacing="0" style="margin-left:4px;">
				  <thead>
				    <tr>
				      <td width="14.2%">SUN</td>
				      <td width="14.2%">MON</td>
				      <td width="14.2%">TUE</td>
				      <td width="14.2%">WED</td>
				      <td width="14.2%">THU</td>
				      <td width="14.2%">FRI</td>
				      <td width="14.2%">SAT</td>
				    </tr>
				  </thead>
				  <tbody id="idCalendar">
				  </tbody>
				</table>
				</div>
			</div>
							
			<div id="leftMenuId" class="LeftBorder" style="display:none;">
				<div class="LeftMenu_list">
					<span style="margin-left:10px;">全体职员</span>
					<span style="margin-left:2px; display:none;" id="setting"><a style="cursor: pointer;color:blue;" onclick="mdiwin('/OAWorkPopedomeActon.do?src=menu','权限查看设置')">[设置]</a></span>
					<span style="margin-left:45px;"><a href="javascript:clickTree('$LoginBean.id');">[$text.get("workplan.lb.myplan")]</a></span>
					<ul id="treeNode" class="tree" style="overflow:hidden;overflow-y:auto;">
					</ul>
				</div>
			</div>	
		</td><!--左边菜单结束-->
			<!--右边列表开始-->
		<td style="padding-left:10px; vertical-align:top;">
			<iframe id="planList" name="planList" src="/OAWorkPlanAction.do?operation=4&planId=$!planId" style="height:535px;overflow:auto;"  frameborder="no" width="100%" scrolling="no" onload=""/>
		</td><!--右边列表结束-->
	</tr>
</table>		
</body>
</html>