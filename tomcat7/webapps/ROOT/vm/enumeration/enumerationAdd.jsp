<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("enumeration.title.add")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="/js/editGrid.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
/**
 * 增加列 
 * fieldType  :  0：整形，1：浮点型，2任意字符，3：大文本型,4：英文字符，5：8位日期8位，6：14位日期 14位，7：ip地址30位，8：email 50位，9：电话 20位，10：手机20位，11：url 50位，12：邮编 15位  
 * nullable 是否可为空 
 * defaultValue 默认值 
 * maxLength 最大长度 
 * inputType 输入类型  -1：自定义;0：输入，1:选项数据型，2：数据表中选择，3：从表中选择
 * inputValue 当输入类型为选项数据型时，这里指明选项数据代号集合(value:name;value:name), 当输入类型为数据表中选择或从表选择时，这里指明表名和字段名（tableId:fieldId） 
 * 当输入类型为从表选择时，这里指明主表，从表，字段（mainTableId:childTableId:fieldId）  
 * 
 * addCols(name,display,width,fieldType,nullable,defaultValue,maxLength,inputType,inputValue)
 */

$(document).ready(function(){
	
});

var editgriddata = new gridData();
putValidateItem("enumName","$text.get("enumeration.lb.enumName")","any",false,0,10);
putValidateItem("maindisplay","选项数据名称","any",false,0,10);

function mainmd(){
   	var mlf = document.form.maindisplay_lan;
	var mhf = document.form.maindisplay;
	//var str  = window.showModalDialog("/common/moreLanguage.jsp?len=200&str="+encodeURI(mhf.value),"","dialogWidth=730px;dialogHeight=450px");
	var url = "/common/moreLanguage.jsp?popupWin=MainLanguage&len=200&str="+encodeURI(mhf.value) ;
	asyncbox.open({id:'MainLanguage',title:'多语言',url:url,width:530,height:300}); 
}

function fillMainLanguage(str){
	var mlf = document.form.maindisplay_lan;
	var mhf = document.form.maindisplay;
	var strs=str.split(";");
    for(var i=0;i<strs.length;i++){
	    var lanstr=strs[i].split(":");
		if(lanstr[0]=='$globals.getLocale()'){
		    mlf.value=lanstr[1];break;
		}
	}
	mhf.value = str;  
}

var moreLanguagefield ;
function moreLanguage(len,moreLanguagefield){
	window.moreLanguagefield = moreLanguagefield;
    moreLanguagefield.testi = "testi";	
	var id = 0;	
   	var mi = document.getElementsByName(moreLanguagefield.name); 
   	for(i=0;i<mi.length;i++){
		if(mi[i].testi == "testi"){
			id = i;
			moreLanguagefield.testi = "";	
		}	
	}
	var flan=moreLanguagefield.name.substr(0,moreLanguagefield.name.lastIndexOf("_"));
	var cf=document.getElementsByName(flan);
	var url = "/common/moreLanguage.jsp?popupWin=Language&len="+len+"&str="+encodeURI(cf[id].value);
	asyncbox.open({id:'Language',title:'多语言',url:url,width:530,height:300});
}

function fillLanguage(str){
	var strs=str.split(";");
    for(var i=0;i<strs.length;i++){
	    var lanstr=strs[i].split(":");
		if(lanstr[0]=='$globals.getLocale()'){
		    moreLanguagefield.value=lanstr[1];break;
		}
	}
	moreLanguagefield.testi = "testi";	
	var id = 0;	
   	var mi = document.getElementsByName(moreLanguagefield.name); 
   	for(i=0;i<mi.length;i++){
		if(mi[i].testi == "testi"){
			id = i;
			moreLanguagefield.testi = "";	
		}	
	}
	var flan=moreLanguagefield.name.substr(0,moreLanguagefield.name.lastIndexOf("_"));
	var cf=document.getElementsByName(flan);
	cf[id].value = str;
}

addCols(editgriddata,'displays_lan','$text.get("enumeration.lb.name")',180,2,false,'',500,4,'');
addCols(editgriddata,'displays','$text.get("enumeration.lb.name")',100,2,false,'',500,-2,'');
addCols(editgriddata,'values','$text.get("enumeration.lb.value")',120,2,false,'',100,0,'');
addCols(editgriddata,'orderBy','$text.get("customTable.lb.listOrder")',50,2,false,'0',100,0,'');

function n(obj){
	return document.getElementById(obj);
}

 function changeValue(obj){
	var myvalues = document.getElementsByName("values");
	for(var i=0;i<myvalues.length;i++){
		if(obj.value!="" && obj.value==myvalues[i].value
			&& (i!=obj.parentNode.parentNode.rowIndex-1)){
			alert("与第"+(i+1)+"行选项值重复");
			obj.value = "";
			break;
		}
	}
}

function beforSubmit(form){   
	if(n("maindisplay_lan").value == "" || n("maindisplay_lan").value == "en:;zh_CN:;zh_TW:;"){
		alert("选项数据名称不能为空");
		return false;
	}
	var displays = document.getElementsByName("displays_lan") ;
	var display_val = document.getElementsByName("displays") ;
	var values = document.getElementsByName("values") ;
	var existOne = false ;
	for(i=0;i<values.length;i++){
		if(values[i].value.length>0&&displays[i].value.length>0){
			existOne = true ;
			break ;
		}
	}
	var isEnVal=false;
	for(i=0;i<values.length;i++){
		if(!isEn(values[i].value)){
			isEnVal = true ;
			break ;
		}
	}
	//if(isEnVal){
	//	alert("选项值只能含有数字和英文字符!") ;
	//	return false ;
	//}
	
	if(!existOne){
		alert("$text.get('com.enumeration.item.null')") ;
		return false ;
	}
	for(i=0;i<values.length;i++){
		if(values[i].value.length>0&&displays[i].value.length==0){
			alert("$text.get('com.enumeration.item.inputdisplay')");
			return false;
		}
	}
	for(i=0;i<displays.length;i++){
		if(values[i].value.length==0&&displays[i].value.length>0){
			alert("$text.get('com.enumeration.item.inputval')");
			return false;
		}
	}
	for(i=0;i<display_val.length;i++){
		if(displays[i].value!="" && display_val[i].value==""){
			display_val[i].value = "$globals.getLocale():"+displays[i].value+";";
		}else{
			var disVal = display_val[i].value;
			var newDisVal = "";
			for(var j=0;j<disVal.split(";").length;j++){
				if(disVal.split(";")[j]!=""){
					if(disVal.split(";")[j].indexOf("$globals.getLocale():")>-1){
						newDisVal += "$globals.getLocale():"+displays[i].value+";";
					}else{
						newDisVal += disVal.split(";")[j]+";";
					}
				}
			}
			display_val[i].value = newDisVal;
		}
	}
	
	disableForm(form);
	document.form.submit();
	return true
	
}	
</script>
</head>

<body onLoad="showtable('edittable'); showStatus(); initTableList(edittableDIV,edittable,editgriddata,1);addRowClick('edittable');">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/EnumerationAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="crmPopFlag" id="crmPopFlag" value="">
#set ($str=$request.getParameter("type")) 
#if($str=="crm")
#else
<div class="Heading">
	<div class="HeadingTitle">$text.get("enumeration.title.add")</div>
	<ul class="HeadingButton">		
		#if($LoginBean.operationMap.get("/EnumerationQueryAction.do").add() || $!LoginBean.id=="1")
		<li><button type="button" onClick="document.form.winCurIndex.value='$!winCurIndex';beforSubmit(document.form);" class="hBtns">$text.get("common.lb.save")</button></li>
		#end
		#if($LoginBean.operationMap.get("/EnumerationQueryAction.do").query() || $!LoginBean.id=="1")
		<li><button type="button" onClick="location.href='/EnumerationQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="hBtns">$text.get("common.lb.back")</button></li>
		#end
	</ul>
</div>
#end
<div id="listRange">
	<div class="listRange_1 listRange_1_input2">	
		<li><span>选项数据$text.get("enumeration.lb.enumName")：</span><input type="text" id="enumName" name="enumName"></li>
		<li><span>选项数据名称：</span><input type="text" id="maindisplay_lan" name="maindisplay_lan"  ><!-- ondblclick="mainmd();" --><input type="hidden" id="maindisplay" name="maindisplay"></li>			
	</div>
	<div class="scroll_function_small_a" id="conter">
		<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-68;
oDiv.style.height=sHeight+"px";
</script>
		<div  name="edittableDIV" id="edittableDIV"> 
		<table border="0" width="100%" cellpadding="0" cellspacing="0" class="listRange_list_function_b" name="edittable" id="edittable">
		</table>
		</div>
</div>
</div>
</form> 
</body>
</html>
