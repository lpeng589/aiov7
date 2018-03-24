<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8" />
<title>工作台设置</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/oa/jquery.ui.core.js"></script>
<script type="text/javascript" src="/js/oa/jquery.ui.widget.js"></script>
<script type="text/javascript" src="/js/oa/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="/js/oa/jquery.ui.sortable.js"></script>
<link rel="stylesheet" href="/style/css/base_button.css" type="text/css"/>
<style type="text/css">
html,body,div,ul,li{margin:0;padding:0;}
body{font-size:12px;font-family:微软雅黑;}
.ui-state-default a, .ui-state-default a:link, .ui-state-default a:visited { color: #555555; text-decoration: none; }
.ui-state-hover, .ui-widget-content .ui-state-hover, .ui-widget-header .ui-state-hover, .ui-state-focus, .ui-widget-content .ui-state-focus, .ui-widget-header .ui-state-focus { border: 1px solid #999999; background: #dadada  50% 50% repeat-x; font-weight: normal; color: #212121; }
.ui-state-hover a, .ui-state-hover a:hover { color: #212121; text-decoration: none; }
.ui-state-active, .ui-widget-content .ui-state-active, .ui-widget-header .ui-state-active { border: 1px solid #aaaaaa; background:#ffffff 50% 50% repeat-x; font-weight: normal; color: #212121; }
.ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited { color: #212121; text-decoration: none; }
.arrange_popup_content{margin:5px 0 0 10px;background:#d5eded;width:700px;height:80px;border-radius:4px;}
.arrange_popup_content .drop_title{float:left;overflow:hidden;display:inline-block;width:80px;height:80px;border-radius:4px 0 0 4px;font-size:14px;text-align:center;line-height:80px;background:#8ed8d8;}
.arrange_popup_content .drop_title .inp_txt{width:70px;outline:0;}
.arrange_popup_content .droptrue{float:left;display:inline-block;width:600px;height:70px;overflow:auto;padding:5px 0;}
.arrange_popup_content .droptrue li {cursor:move;width:62px;height:62px;margin:4px 0 0 9px;border-radius:4px;background:#8ed8d8;border:1px #95A5A6 solid;line-height:62px;text-align:center;float:left;display:inline-block;}
.arrange_popup_content .droptrue li:hover {background:#fcb43b;}
.arrange_popup_content .items_btn{float:left;display:inline-block;}
.arrange_popup_content .items_btn .up_btn{width:16px;height:16px;margin:18px 0 0 0;cursor:pointer;background:url(/style/images/client/first_btn.png) no-repeat 0 -92px;}
.arrange_popup_content .items_btn .down_btn{width:16px;height:16px;margin:10px 0 0 0;cursor:pointer;background:url(/style/images/client/first_btn.png) no-repeat -16px -92px;}
.btn-items{margin:5px 0 0 10px;}
</style>
<script type="text/javascript">
var copyFlow = '<div class="arrange_popup_content" ><div class="drop_title"><input type="text" name="flowName"></input></div><ul class="droptrue"></ul><div class="items_btn"><div class="up_btn" onclick="MoveItem(this)" move="up"></div><div class="down_btn" onclick="MoveItem(this)" move="down"></div></div></div>';
$(function() {
	ulSortable();
});
function ulSortable(){
	$("ul.droptrue" ).sortable({
		connectWith: "ul"
	});
	$("ul.dropfalse" ).sortable({
		connectWith: "ul",
		dropOnEmpty: true
	});
}
function MoveItem(obj){
	var move = $(obj).attr("move");
	var oDiv = $(obj).parents("div.arrange_popup_content");
	var sort;
	if(move == "up"){
		sort = oDiv.prev().length;
		if(sort == "1"){
			oDiv.prev().before(oDiv.clone(true));
			oDiv.remove();
			jQuery("ul.droptrue" ).sortable({
				connectWith: "ul"
			});
		}
	}else if(move == "down"){
		sort = oDiv.next().length;
		if(sort == "1"){
			oDiv.next().after(oDiv.clone(true));
			oDiv.remove();
		}
	}
}
function downMove(obj){
	var oDiv = $(obj).parents("div.arrange_popup_content");
	var sort = oDiv.next().length;
	if(sort == "1"){
		oDiv.next().after(oDiv.clone(true));
		oDiv.remove();
	}
}

function add(){
	jQuery(".items_arrange").append(copyFlow);
	var sortable = "sortable"+(jQuery(".arrange_popup_content").length +1);
	jQuery(".arrange_popup_content[id!='selectBrother']:last ul").attr("id",sortable);
	jQuery("ul.droptrue" ).sortable({
		connectWith: "ul"
	});
	
}

function beforeSubmit(){
	var flowInfo = "";
	var error = "false";//记录是否错误
	jQuery(".arrange_popup_content[id!='selectBrother']:visible").each(function(){
		if($(this).find("li").length >0){
			if(jQuery.trim($(this).find("input").val()) == ""){
				alert("请输入流程名称.");
				$(this).find("input").focus();
				error = "true";
				return false;
			}
			flowInfo +=$(this).find("input").val()+":"
			$(this).find("li").each(function(){
				flowInfo +=$(this).attr("name")+",";
			})
			flowInfo +=";";
		}
	})
	
	if(error == "true"){
		return false;
	}
	jQuery("#flowInfo").val(flowInfo);
	form.submit();
}
</script>
</head>
<body>
<form action="/CRMSalesFlowAction.do" method="post" name="form">
<input type="hidden" name="operation" id="operation" value='$globals.getOP("OP_ADD")'/>
<input type="hidden" id="flowInfo" name="flowInfo" value=""/>
<input type="hidden" id="tableName" name="tableName" value="$tableName"/>
<div class="btn btn-small btn-items" onclick="add()">添加</div>
<div class="items_arrange" style="max-height:260px;overflow:auto;">
	#set($rowIndex = 1)
	#set($sortableIndex = 1)
	#foreach($flow in $flowList)
		<div class="arrange_popup_content">
			<div class="drop_title"><input type="text" class="inp_txt" name="flowName" value="$globals.get($flow,0)"/></div>
			<ul id="sortable${sortableIndex}" class='droptrue'>
				#foreach($tableName in $globals.get($flow,2).split(","))
					#if("$!tableName" !="")
						#set($tableBean = "")
						#set($tableBean = $moduleMap.get("$tableName"))
						#if("$!tableBean" !="")
							<li class="ui-state-default" id="brother${rowIndex}" name="$tableName">$tableBean.display.get("$globals.getLocale()")</li>
							#set($rowIndex = $rowIndex + 1)
						#end
					#end
				#end
				#set($sortableIndex = $sortableIndex + 1)
			</ul>
			<div class="items_btn">
				<div class="up_btn" onclick="MoveItem(this)" move="up"></div>
				<div class="down_btn" onclick="MoveItem(this)" move="down"></div>
			</div>
		</div>
	#end
</div>
<div class="arrange_popup_content" id="selectBrother">
	<div class="drop_title">可选邻居表</div>
	<ul id="sortable${sortableIndex}" class='droptrue'>
		#foreach($tableName in $noSelectTables.split(","))
			#if("$!tableName" !="")
				#set($tableBean = "")
				#set($tableBean = $moduleMap.get("$tableName"))
				#if("$!tableBean" !="")
					<li class="ui-state-default" id="brother${rowIndex}" name="$tableName">$tableBean.display.get("$globals.getLocale()")</li>
					#set($rowIndex = $rowIndex + 1)
				#end
			#end
		#end
	</ul>
</div>
</form>
</body>
</html>