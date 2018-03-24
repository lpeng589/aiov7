<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.work.consign")</title>
<link rel="stylesheet" href=" /$globals.getStylePath()/css/ListRange.css" type="text/css">
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
var jsonType = eval('$!flowJson');

function selectFlowType(){
	var flowType = $("#flowTypeId option:selected",document).val();
	$("#LeftSelectId option",document).remove();
	parent.jQuery.each(jsonType,function(key,type){
		if(type.tclass.indexOf(flowType)==0 && "$flowIds".indexOf(type.keyId+";")==-1){
			$("#LeftSelectId",document).append("<option value='"+type.keyId+"'>"+type.name+"</option>");
		}
	});
}

/*关键字搜索*/
function searchKeyWord(obj){
	$("#LeftSelectId option",document).remove();
	parent.jQuery.each(jsonType,function(key,type){
		if(type.name.indexOf(obj.value)>-1 || type.name.length==0){
			$("#LeftSelectId",document).append("<option value='"+type.keyId+"'>"+type.name+"</option>");
		}
	});
}

/*右移*/
function moveRight(all) {
	if(all){
		$("#RightSelectId",document).append($("#LeftSelectId option",document));
	}else{
		$("#RightSelectId",document).append($("#LeftSelectId option:selected",document));
	}
}

/*移除*/
function moveLeft(all) {
	if(all){
   		$("#LeftSelectId",document).append($("#RightSelectId option",document));
   	}else{
   		$("#LeftSelectId",document).append($("#RightSelectId option:selected",document));
   	}
}

/*返回值*/
function returnValue(){
	var returnValue = "";
	var returnName = "";
	$("#RightSelectId  option",document).each(function(key){
		if(typeof($(this).val())!="undefined"){
			returnValue += $(this).val() + ";";
			returnName += $(this).html() + ";";
		}
	});
	//if(returnValue.length == 0){
	//	alert("必须选择工作流类型！");
	//	return;
	//}
	return returnValue + "|" + returnName;
}

/*加载默认值*/
jQuery(function(){
  	parent.jQuery.each(jsonType,function(key,type){
		if("$flowIds".indexOf(type.keyId+";")!=-1){
			$("#RightSelectId",document).append("<option value='"+type.keyId+"'>"+type.name+"</option>");
		}
	});
});

</script>
</head>
<body>
<div id="listRange_id" align="center">
<div class="grid">
	<div style="float:left;margin:10px 0px 10px 45px;">
	流程类型：<select id="flowTypeId" style="width:160px;" onchange="selectFlowType();">
			<option value="">全部流程</option>
			#foreach($class in $flowClass)
			<option value="$globals.get($class,0)" >$globals.get($class,1)</option>
			#end
		</select>
		<span style="margin-left:70px;">关键字：<input type="text" onkeyup="searchKeyWord(this);"></span>
	</div>
	<table cellpadding="0" cellspacing="0" style="border-collapse: separate">
    <tbody><tr>
        <td style="background: #f0f0f0; border: solid 1px #abadb3; border-bottom: none; padding: 5px 0 3px 3px">
            待选择项
        </td>
        <td style="width: 120px;">
            &nbsp;
        </td>
        <td style="background: #f0f0f0; border: solid 1px #abadb3; border-bottom: none; padding: 5px 0 3px 3px">
            已选择项
        </td>
    </tr>
    <tr>
        <td>
            <select id="LeftSelectId" style="width: 220px; height:250px; border: solid 1px #abadb3;" multiple="multiple">
				#foreach($type in $flowType)
				#if(!$flowIds.contains("${type.keyId};"))
				<option value="$type.keyId">$type.name</option>
				#end
				#end
			</select>
        </td>
        <td align="center" style="width: 120px;">
            <p style="margin: 10px 0">
                <input type="button" onclick="moveRight()" value="添 加 →"></p>
            <p style="margin: 10px 0">
                <input type="button" onclick="moveLeft()" value="删 除 ←"></p>
            <p style="margin: 10px 0">
                <input type="button" onclick="moveRight(true)" value="全部添加"></p>
            <p style="margin: 10px 0">
                <input type="button" onclick="moveLeft(true)" value="全部删除"></p>
        </td>
        <td>
            <select id="RightSelectId" style="width: 220px; height: 250px; border: solid 1px #abadb3;" multiple="multiple">
			</select>
        </td>
    </tr>
</tbody>
</table>
</div>										
</div>
</body>
</html>
