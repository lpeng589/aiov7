<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
<title>工作台设置</title>
<script src="/js/jquery.js"></script>
<script src="/js/function.js"></script>
<script src="/js/oa/jquery.ui.core.js"></script>
<script src="/js/oa/jquery.ui.widget.js"></script>
<script src="/js/oa/jquery.ui.mouse.js"></script>
<script src="/js/oa/jquery.ui.sortable.js"></script>
<style>
body{font-size: 12px;}
#sortable1, #sortable2, #sortable3, #sortable4 { list-style-type: none; margin: 0; padding: 0; float: left; margin-right: 10px; background: #eee; padding: 10px; width: 120px;height:200px;overflow-y:auto;}
#sortable1 li, #sortable2 li, #sortable3 li , #sortable4 li {cursor:move; margin: 5px; padding: 5px; font-size: 12px; width: 80px;text-align: center;}
.ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default { border: 1px solid #d3d3d3; background: #e6e6e6 50% 50% repeat-x; font-weight: normal; color: #555555; }
.ui-state-default a, .ui-state-default a:link, .ui-state-default a:visited { color: #555555; text-decoration: none; }
.ui-state-hover, .ui-widget-content .ui-state-hover, .ui-widget-header .ui-state-hover, .ui-state-focus, .ui-widget-content .ui-state-focus, .ui-widget-header .ui-state-focus { border: 1px solid #999999; background: #dadada  50% 50% repeat-x; font-weight: normal; color: #212121; }
.ui-state-hover a, .ui-state-hover a:hover { color: #212121; text-decoration: none; }
.ui-state-active, .ui-widget-content .ui-state-active, .ui-widget-header .ui-state-active { border: 1px solid #aaaaaa; background: #ffffff 50% 50% repeat-x; font-weight: normal; color: #212121; }
.ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited { color: #212121; text-decoration: none; }
.arrange_popup_title{ float:left; width:100%; height:26px; line-height:22px; border-bottom:1px solid #d7d7d7;}
.arrange_popup_title span{ float:left; font-weight:bold;}
.arrange_popup_title label{ float:left; vertical-align:middle; margin-left:20px;}
.arrange_popup_title div{ float:left; vertical-align:middle; margin-right:10px;}
.arrange_popup_title div input{ float:left; vertical-align:middle;}
.arrange_popup_content{ float:left; width:144px; margin:10px 10px 0px 0px}
.arrange_popup_content div{width:100%; text-align:center; height:22px; line-height:22px;}
.arrange_popup_content ul{ background:#f5f5f5; border:1px solid #d0d0d0; width:100%; height:240px; overflow:auto;}
</style>
<script>
$(function() {
	$( "ul.droptrue" ).sortable({
		connectWith: "ul"
	});

	$( "ul.dropfalse" ).sortable({
		connectWith: "ul",
		dropOnEmpty: true
	});

	$("#sortable1, #sortable2, #sortable3, #sortable4" ).disableSelection();
});

function changeType(type){
	if("2"==type){
		jQuery("#popup3").hide();
		jQuery("#sortable3 li").appendTo("#sortable4") ;
		jQuery("#sortable3 li").remove();
	}else{
		jQuery("#popup3").show() ;
	}
}

function saveDesk(){
	var column1 = "" ;
	jQuery("#sortable1 li").each(function(i){
		column1 += jQuery(this).attr("id")+"|" ;
	});
	jQuery("#column1").val(column1) ;
		
	var column2 = "" ;
	jQuery("#sortable2 li").each(function(i){
		column2 += jQuery(this).attr("id")+"|" ;
	});
	jQuery("#column2").val(column2) ;
		
		
	var column3 = "" ;
	jQuery("#sortable3 li").each(function(i){
  		column3 += jQuery(this).attr("id")+"|" ;
	});
	jQuery("#column3").val(column3) ;
	if(column1.length==0 || column2.length==0){
		alert("第一列和第二列不能为空！") 
		return false ;
	}
	var width1 = jQuery("#width1").val() ;
	var width2 = jQuery("#width2").val() ;
	var width3 = jQuery("#width3").val() ;
	if(!isInt2(width1)){alert("第一列的宽度所占比例必须是正整数");return false;};
	if(!isInt2(width2)){alert("第二列的宽度所占比例必须是正整数");return false;};
	var popup3 = jQuery("#popup3").css("display") ;
	if(popup3=="block" && !isInt2(width3)){alert("第三列的宽度所占比例必须是正整数");return false;};
	if(popup3=="none"){
		if((parseInt(width1)+parseInt(width2))>100){
			alert("二列比例加起来不能大于100") ;
			return false ;
		}
	}else{
		if((parseInt(width1)+parseInt(width2)+parseInt(width3))>100){
			alert("三列比例加起来不能大于100") ;
			return false ;
		}
	}
	form.submit() ;
	//window.parent.location.reload()
	//window.parent.$("#deskTopId").remove() ;
}

</script>
</head>
<body>
<div>
<iframe name="formFrame" style="display:none"></iframe>
<form name="form" action="/MyDesktopAction.do?operation=85" method="post" target="formFrame">
<input type="hidden" id="column1" name="column1" value=""/>
<input type="hidden" id="column2" name="column2" value=""/>
<input type="hidden" id="column3" name="column3" value=""/>
	<div class="arrange_popup_title"><label>排列方式：</label>
		<div><input type="radio" name="columnType" onclick="changeType('2')" #if("$!deskMap.get(3)"=="")checked="checked"#end/>两列</div>
		<div><input type="radio" name="columnType" onclick="changeType('3')" #if("$!deskMap.get(3)"!="")checked="checked"#end/>三列</div>
		<div style="float: right;font-size: 12px;color:#B4B4B4;">(注：可拖动指定的模块到任意一列)</div>
	</div>
	<div class="arrange_popup_content">
		<div>未选模块</div>
		<ul id="sortable4" class='droptrue'>
		#foreach($desk in $noDesk)
		<li class="ui-state-default" id="$!desk.id">$!desk.modulName</li>
		#end
		</ul>
	</div>
	<div class="arrange_popup_content">
		<div>第一列</div>
		<ul id="sortable1" class='droptrue'>
			#foreach($desk in $deskMap.get(1))
			<li class="ui-state-default" id="$!desk.id">$!desk.modulName</li>
			#end
		</ul>
		<div>宽度所占比例：<input type="text" id="width1" name="width1" style="width:30px;margin-top:5px;" value="$!width1"/>%</div>
	</div>
	<div class="arrange_popup_content">
		<div>第二列</div>
		<ul id="sortable2" class='dropfalse'>
			#foreach($desk in $deskMap.get(2))
			<li class="ui-state-default" id="$!desk.id">$!desk.modulName</li>
			#end
		</ul>
		<div>宽度所占比例：<input type="text" id="width2" name="width2" style="width:30px;margin-top:5px;"  value="$!width2"/>%</div>
	</div>
	<div class="arrange_popup_content" id="popup3" #if("$!deskMap.get(3)"=="")style="display:none;"#end>
		<div>第三列</div>
		<ul id="sortable3" class='droptrue'>
			#foreach($desk in $deskMap.get(3))
			<li class="ui-state-default" id="$!desk.id">$!desk.modulName</li>
			#end
		</ul>
		<div>宽度所占比例：<input type="text" id="width3" name="width3" style="width:30px;margin-top:5px;"  value="$!width3"/>%</div>
	</div>
</div>
</form>
</body>
</html>