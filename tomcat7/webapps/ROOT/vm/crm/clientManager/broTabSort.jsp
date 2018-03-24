<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>AIO</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery-ui.css"/>
<link type="text/css" rel="stylesheet" href="/style1/css/workflow2.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/broTabSort.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/jquery-ui.js"></script>
<script language="javascript" src="/js/function.js"></script>

<script>  
$(function(){ 
     $("#sortable").sortable();  
 	 $("#sortable").disableSelection();  
 	 displayId();
});  
 
function displayId(){
	var count=jQuery("ul[id*='sortable'] ul:has('#add')").size();
	
	jQuery("ul[id*='sortable'] ul:has('#add')").each(function(){
 		var num=$(this).index();
		if(num==0){
			$(this).find("a[id='moveUp']").css("display","none");
			$(this).find("a[id='moveDown']").css("display","inline");
		}else if(num==count-1){
			$(this).find("a[id='moveDown']").css("display","none");
			$(this).find("a[id='moveUp']").css("display","inline");
		}else{
			if($(this).find("a[id='moveUp']").css("display")=="none"){
				$(this).find("a[id='moveUp']").css("display","inline");
			}
			if($(this).find("a[id='moveDown']").css("display")=="none"){
				$(this).find("a[id='moveDown']").css("display","inline");
			}
		}
	});
	if(count==1){
		jQuery("a[id='moveDown']").css("display","none");
		jQuery("a[id='moveUp']").css("display","none");
	}
}
 
 /*上移*/
function MoveUp(obj){
	($(obj).parent().parent()).insertBefore($(obj).parent().parent().prev());
	displayId();
}

/*下移*/
function MoveDown(obj){
	($(obj).parent().parent()).insertAfter($(obj).parent().parent().next());
	displayId();	
}

//添加
function addOrder(obj){
	var count=jQuery("#sortable ul a[id='add']").size();
	if(count>=1){
		($(obj).parent().parent()).insertAfter(jQuery("#sortable ul").get(count-1));
	}else{
		($(obj).parent().parent()).appendTo(jQuery("#sortable"));
	}
	$(obj).parent().prev().html("<a id='moveUp' style='cursor: pointer;' onclick='MoveUp(this);'>上移</a><a id='moveDown' style='cursor: pointer; margin-left:5px;' onclick='MoveDown(this);'>下移</a>");
	$(obj).replaceWith("<a id='add'>已添加 </a><a style='cursor: pointer; margin-left:5px;' onclick='delOrder(this);'>移除</a>");
	displayId();	
}

//移除
function delOrder(obj){

	var count=jQuery("ul[id*='sortable'] ul:has('#add')").size();
	if(count==1){
		asyncbox.alert('至少添加一个邻居表!','提示');
		return false;
	}
	($(obj).parent().parent()).insertBefore(jQuery("#sortable2 ul").get(0));
	$(obj).parent().prev().html("");
	$(obj).parent().html("<a style='cursor: pointer;' onclick='addOrder(this);'>添加 </a>");
	displayId();	
}

function saveOrder(){
	var num=0;
	var tabName="";
	jQuery("#sortable ul a").each(function(){
		if($(this).attr("id")=="add"){
			num=num+1;
			tabName=tabName+$(this).parent().attr("tabName")+",";
		}
	});
	jQuery("#orderTab").val(tabName);
	
	var viewId="$viewId";
	var orderTab=jQuery("#orderTab").val();
	
	var str="/ClientSettingAction.do?operation=$globals.getOP('OP_UPDATE')&updType=brotherSort&orderTab="+orderTab+"&viewId="+viewId;
	AjaxRequest(str);
 	value = response;
	if(value == "true"){
		alert("设置成功!")
		parent.broSetfresh();
	}else{
		alert("设置失败!");
	}
	
}

</script>
</head>

<body>
<div class="c_main  f_l" style="margin-left: 0;height:255px; width: 465px;overflow: no;">
<form method="post" scope="request" name="form" action="/ClientSettingAction.do?operation=$globals.getOP("OP_ADD")&addType=broTabSet" class="mytable">
<input type="hidden" id="orderTab"  name="orderTab" value=""/>
<input type="hidden" id="viewId" name="viewId" value="$viewId" />
<div class="maintablearea">
<div class="hd">
	<div class="bd"  >
		<ul class="maintop" style="width: 100%;">
			<li class="col_hy" style="background: none;width: 30%;margin-left: 25px;">邻居表</li>
			<li class="col_hy" style="width:25%;margin-left: 25px;">排序</li>
			<li class="col_hy" style="width:20%;margin-left: 20px;">操作</li>
		</ul>
		<ul id="sortable">
		
		#foreach ($tableName in $userBrotherInfo.split(","))
			#set($tableBean = "")
			#set($tableBean = $moduleMap.get("$tableName"))
	     	#if("$!tableBean" !="")
				<ul class="col" style="width: 95%;cursor: move;" id="ul_$velocityCount">
					<li style="background: none;width: 30%;">$tableBean.display.get("$globals.getLocale()") </li>
					<li style="background: none;width: 25%;">
						<a id="moveUp" style="cursor: pointer;" onclick="MoveUp(this);">上移</a> 
						<a id="moveDown" style="cursor: pointer; margin-left:5px;" onclick="MoveDown(this);">下移</a>
					</li>
					<li  style="background: none;width: 20%;" tabName="$tableBean.tableName">
						<a id="add">已添加 </a>
						<a style="cursor: pointer; margin-left:5px;" onclick="delOrder(this);">移除</a>
					</li>
				</ul>
	     	#end
		#end
		</ul>
		<ul id="sortable2">
		#if("$!noUserBrotherInfo"=="")
			<ul class="col" style="width: 95%;cursor: move;" ></ul>
		#end
		
		#if("$!noUserBrotherInfo"=="")
			#foreach($bean in $brotherTableList)	<!-- 不可以看到的兄弟表 -->
				<ul class="col" style="width: 95%;cursor: move;" id="ul_$velocityCount"  >
					<li style="background: none;width: 30%;">$bean.display.get("$globals.getLocale()")</li>
					<li style="background: none;width: 25%;">
					</li>
					<li style="background: none;width: 20%;" tabName="$bean.tableName">
						<a  style="cursor: pointer;" onclick="addOrder(this);">添加 </a>
					</li>
				</ul>
			#end	
		#else
			#foreach($tableName1 in $noUserBrotherInfo.split(","))	<!-- 不可以看到的兄弟表 -->
				#set($bean = "")
				#set($bean = $moduleMap.get("$tableName1"))
		     	#if("$!bean" !="")
				<ul class="col" style="width: 95%;cursor: move;" id="ul_$velocityCount"  >
					<li style="background: none;width: 30%;">$bean.display.get("$globals.getLocale()")</li>
					<li style="background: none;width: 25%;">
					</li>
					<li style="background: none;width: 20%;" tabName="$bean.tableName">
						<a  style="cursor: pointer;" onclick="addOrder(this);">添加 </a>
					</li>
				</ul>
				#end
			#end		
		#end
		</ul>
	</div>
</div>
</div>
</form>
</div>
</body>
</html>