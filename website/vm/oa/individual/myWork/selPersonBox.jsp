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

var flag = true;
function checkAlert(){
	var userId = document.getElementById("userId");
	if(userId.value == "" || userId.value == null){
	   	flag = false;
		return flag;
	}
	if(flag){
   		//form.submit();
		window.parent.$("#selPersonBox").remove() ;
   	}
}
var curUserId = "" ;
var curPlanType = "day" ;
var curDate = "" ;
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
		//jQuery("#setting").show();
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
 	document.getElementById("userId").value=curUserId;
	//window.frames["planList"].location= "/OAWorkPlanAction.do?operation=4&userId="+curUserId+"&planType="+curPlanType+"&strDate="+curDate ;
}
</script>


</head>
<body >
<input type="hidden" name="userId" id="userId" value="" />
<table cellpadding="0" cellspacing="0" style="width:99%; height:100%; margin-top:10px;">
		<tr><!--左边菜单开始-->
			<div id="leftMenuId" class="LeftBorder" style="display:none;">
				<div class="LeftMenu_list">
					<span style="margin-left:10px;">全体职员</span>
					<span style="margin-left:10px; display:none;" id="setting"><a style="cursor: pointer;color:blue;" onclick="mdiwin('/OAWorkPopedomeActon.do?src=menu','权限查看设置')">[设置]</a></span>
					<!--  <span style="margin-left:60px;"><a href="javascript:clickTree('$LoginBean.id');">[$text.get("workplan.lb.myplan")]</a></span> -->
					<ul id="treeNode" class="tree" style="overflow:hidden;overflow-y:auto;">
					</ul>
				</div>
			</div>	
		 <!--左边菜单结束-->
			<!--右边列表开始-->
		<td style="padding-left:10px; vertical-align:top;display:none;">
			<iframe id="planList" name="planList" src="/OAWorkPlanAction.do?operation=4&planId=$!planId"  frameborder="no" width="100%" scrolling="no" onload="this.height=planList.document.body.scrollHeight"/>
		</td><!--右边列表结束-->
	</tr>
</table>
</body>
</html>