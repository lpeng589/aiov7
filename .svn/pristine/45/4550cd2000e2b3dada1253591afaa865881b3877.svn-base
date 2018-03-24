<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("property.title.add")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="/js/editGrid.js"></script>

<script language="javascript">
/**
 * 增加列




 * fieldType  :  0：整形，1：浮点型，2任意字符，3：大文本型,4：英文字符，5：8位日期8位，6：14位日期 14位，7：ip地址30位，8：email 50位，9：电话 20位，10：手机20位，11：url 50位，12：邮编 15位




 * nullable 是否可为空




 * defaultValue 默认值




 * maxLength 最大长度




 * inputType 输入类型  -1：自定义;0：输入，1:枚举型，2：数据表中选择，3：从表中选择
 * inputValue 当输入类型为枚举型时，这里指明枚举代号集合(value:name;value:name), 当输入类型为数据表中选择或从表选择时，这里指明表名和字段名（tableId:fieldId）




 * 当输入类型为从表选择时，这里指明主表，从表，字段（mainTableId:childTableId:fieldId）




 * 
 * addCols(name,display,width,fieldType,nullable,defaultValue,maxLength,inputType,inputValue)
 */
 var editgriddata = new gridData();
putValidateItem("propName","$text.get("property.lb.propName")","any",false,0,10);
putValidateItem("maindisplay","$text.get("property.lb.name")","any",false,0,10);

function mainmd(){
  
  var str  = window.showModalDialog("/vm/property/display.jsp?str="+document.form.maindisplay.value,"","dialogWidth=630px;dialogHeight=600px");
	 document.form.maindisplay.value = str;
  
}

function moredisClick(obj){
  if(event.keyCode ==13){
     var str  = window.showModalDialog("/vm/property/display.jsp?str="+obj.value,"","dialogWidth=630px;dialogHeight=600px");
	 obj.value = str;
   }
}
var IsAble="1:$text.get("wokeflow.lb.enable");0:$text.get("oa.bbs.stop")";
var va = 'moredisClick(this)';

addCols(editgriddata,'displays','$text.get("property.lb.name")',200,2,false,'',500,-1,va);
addCols(editgriddata,'values','$text.get("property.lb.values")',100,2,false,'',100,0,'');
addCols(editgriddata,'status','$text.get("property.lb.status")',100,2,false,'',100,1,IsAble);



function $(obj){
	return document.getElementById(obj);
}
function beforSubmit(form){   
	if($("maindisplay").value == "" || $("maindisplay").value == "en:;zh_CN:;zh_TW:;"){
		alert("$text.get("property.msg.NoNameSorp")");
		return false;
	}
	disableForm(form);
	document.form.submit();
	return true;
}	
</script>
</head>

<body onLoad="showtable('edittable'); showStatus(); initTableList(edittableDIV,edittable,editgriddata);">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/PropertyAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("property.title.add")</div>
	<ul class="HeadingButton">
	<li><button type="button" onClick="beforSubmit(document.form);" class="b2">$text.get("common.lb.add")</button></li>		
		<li><button type="button" onClick="location.href='/PropertyQueryAction.do?operation=$globals.getOP("OP_QUERY")'" class="b2">$text.get("common.lb.back")</button></li>
		
	</ul>
</div>
<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
	<div class="listRange_1">	
		<li><span>$text.get("property.lb.propName"):</span><input name="propName" type="text" id="propName" style="width:80px">
		</li>
		<li><span>$text.get("property.lb.name"):</span><input type="text" name="maindisplay" style="width:50px" onKeyPress="if(event.keyCode == 13){  mainmd();  }"></li>			
	</div>
	<div class="scroll_function_small_a">
		<div  name="edittableDIV" id="edittableDIV"> 
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="edittable" id="edittable">
		</table>
		</div>
	</div>
</div>
</form> 
</body>
</html>
