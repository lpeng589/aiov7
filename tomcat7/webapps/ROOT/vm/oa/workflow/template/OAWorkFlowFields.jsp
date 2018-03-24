<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
function submitBefore(){
	var fieldValue = $('input[name="keyId"]:checked',document);
	if(typeof(fieldValue.val())=="undefined"){
		alert("必须选择一个条件字段");
		return false;
	}
	return fieldValue.val() + "|" + fieldValue.attr("title") + "|" + fieldValue.attr("enumValue");
}

function returnValue(obj){
	var inputObj = $(obj).find("input");
	window.parent.fillFields(inputObj.attr("value")+"|"+inputObj.attr("title")+"|"+inputObj.attr("enumValue"));
}
</script>
<style type="text/css">
body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,select,legend,button,input,textarea,th,td {margin:0;padding:0;}
body,button,input,select,textarea {font:14px Microsoft Yahei;color:#0c0c0c;outline:0;}
body{overflow:hidden;}
img{border:0;}
li{list-style:none;}
.s-btn{padding:1px 8px;border:1px #b7babc solid;cursor:pointer;display:inline-block;margin:0 0 0 5px;}
.s-btn:hover{background:#f2f2f2;}
.u-list{overflow:hidden;}
.u-list>li{float:left;display:inline-block;width:150px;padding:5px 0;}
</style>
</head>
<body>
<form name="form" action="/OAWorkFlowTempAction.do?queryType=flowfields&tableName=$!tableName&flowId=$!flowId" method="post">
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
<div class="boxbg2 subbox_w700" style="width: 470px;margin: 0 auto;overflow-y: auto;height: 350px;">
	<div style="text-align:center;border-bottom:1px #e1e1e1 solid;"> 
		<div style="display:inline-block;padding:5px 0;"><input class="lf" name="keyword" value="$!keyword"/><span class="lf s-btn" onclick="javascript:submit();">查询</span></div>
	</div>
	<ul class="u-list">
		#foreach($field in $fieldList)
		<li ondblclick="returnValue(this);">
			<input id="$!globals.get($field,0)" name="keyId" type="radio" value="$!globals.get($field,0)" title="$!globals.get($field,1)" inputType="$!globals.get($field,2)" enumValue="$!globals.get($field,3)"/>
			<label for="$!globals.get($field,0)">$!globals.get($field,1)</label>
		</li>
		#end
	</ul>
</div>
</form>
</body>
</html>
