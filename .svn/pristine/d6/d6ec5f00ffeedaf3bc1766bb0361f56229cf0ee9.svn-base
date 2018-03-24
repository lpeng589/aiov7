<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("property.title.update")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="/js/editGrid.js"></script>

<script language="javascript">
 var editgriddata = new gridData();
var isStat="1:$text.get("common.lb.yes");0:$text.get("common.lb.no")";
putValidateItem("enumName","$text.get("enumeration.lb.enumName")","any","",false,0,10);
putValidateItem("maindisplay","$text.get("enumeration.lb.name")","any","",false,0,10);

function mainmd(){
  var maindisplay=document.form.propDisplay;
   var str  = window.showModalDialog("/common/moreLanguage.jsp?len=50&str="+encodeURI(maindisplay.value),"","dialogWidth=730px;dialogHeight=450px");
	 maindisplay.value = str;
	  var strs=str.split(";");//alert(str);alert(moreLanguagefield.name+"_lan");
	  for(var i=0;i<strs.length;i++){
	     var lanstr=strs[i].split(":");
		 if(lanstr[0]=='$globals.getLocale()'){
		   document.form.propDisplay_lan.value=lanstr[1];break;
		 }
	  }
  
}

function moreLanguage(len,obj){
   obj.testi = "testi";	
	var id = 0;	
   	var mi = document.getElementsByName(obj.name); 
   	for(i=0;i<mi.length;i++){
		if(mi[i].testi == "testi"){
			id = i;
			obj.testi = "";	
		}	
	}
	var flan=obj.name.substr(0,obj.name.lastIndexOf("_"));
	var cf=document.getElementsByName(flan);
      var str  = window.showModalDialog("/common/moreLanguage.jsp?len=50&str="+encodeURI(cf[id].value),"","dialogWidth=730px;dialogHeight=450px");
	   var strs=str.split(";");
      for(var i=0;i<strs.length;i++){
	     var lanstr=strs[i].split(":");
		 if(lanstr[0]=='$globals.getLocale()'){
		    obj.value=lanstr[1];break;
		 }
	  }
		 cf[id].value = str;
}

var va = 'moredisClick(this)';
addCols(editgriddata,'fieldId','',130,2,true,'',500,-2,'');
addCols(editgriddata,'lgId','',130,2,true,'',500,-2,'');
addCols(editgriddata,'enumName_lan','$text.get("property.lb.enumName")',130,2,true,'',500,4,'');
addCols(editgriddata,'enumName','$text.get("enumeration.lb.name")',300,2,false,'',500,-2,'');
addCols(editgriddata,'enumValue','$text.get("property.lb.enum")',120,2,true,'',500,0,'');
addCols(editgriddata,'goupName','$text.get("property.lb.groupName")',120,2,true,'',500,0,'');
addCols(editgriddata,'enumStatu','$text.get("excel.list.isUsed")',120,0,false,'',10,1,isStat);

#foreach($row in $result.enumItem)
	addRows(editgriddata,'$row.id','$row.getDisplay().getId()','$row.display.get($globals.getLocale())','$row.handerDisplay()','$row.enumValue','$!row.groupName','$row.isUsed');
#end
function $(obj){
	return document.getElementById(obj);
}
function beforSubmit(form){   
	if($("propDisplay_lan").value == "" || $("propDisplay_lan").value == "en:;zh_CN:;zh_TW:;"){
		alert("$text.get('languageId.tblGoodsOfProp.error')");
		return false;
	}
	disableForm(form);
	/*
    var enumValues=document.getElementsByName("enumValue");
	for(var i=0;i<enumValues.length;i++)
	{
	   if(enumValues[i].value.length==0){
	      alert('$text.get("property.lb.valuenotNull")');return false;
	   }
	}*/
	document.form.submit();
}	
</script>
</head>

<body onLoad="showtable('edittable'); showStatus(); initTableList(edittableDIV,edittable,editgriddata,1);">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/PropAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="id" value="$!result.id">
<input type="hidden" name="oldPropDisplay" value="$!result.handerDisplay()">
<input type="hidden" name="oldCalculate" value="$!result.isCalculate">
<input type="hidden" name="winCurIndex"  value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("property.title.update")</div>
	<ul class="HeadingButton">		
		#if($globals.getMOperation().update())
		<li><button type="button" onClick="beforSubmit(document.form);" class="b2">$text.get("common.lb.update")</button></li>
		#end
		#if($globals.getMOperation().query())
		<li><button type="button" onClick="location.href='/PropQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
		#end
	</ul>
</div>
<div id="listRange_id">
			<script type="text/javascript">
				var oDiv=document.getElementById("listRange_id");
				var sHeight=document.body.clientHeight-38;
				oDiv.style.height=sHeight+"px";
			</script>
	<ul class="listRange_1">	
			<li><span>$text.get("property.lb.propName")：</span><input  type="text"  name="propName" id="propName" value="$!result.propName" disabled="false"></li>
			<li><span>$text.get("property.lb.propDisplay")：</span>	
			  <input  type="text"  name="propDisplay_lan" id="propDisplay_lan" onDblClick="mainmd();" value="$!result.display.get("$globals.getLocale()")" onKeyPress="if(event.keyCode == 13){  mainmd();  }" readonly="readonly"> 
			  <input type="hidden" name="propDisplay" value="$!result.handerDisplay()"/>
            </li>
			<li><span>$text.get("excel.list.isUsed")：</span>
			<select name="isUsed" id="select"> 
			     #if($!result.isUsed==1)
				 <option value="1" selected>$text.get("excel.list.yes")</option>
				 <option value="2">$text.get("excel.list.no")</option>
				 #else
			    <option value="1">$text.get("excel.list.yes")</option>
				<option value="2" selected>$text.get("excel.list.no")</option>
				#end
				
		      </select>
			</li>
			<li><span>$text.get("property.lb.isCalculate")：</span>
			<select name="isCalculate" id="select"> 
			    #if($!result.isCalculate==1)
				 <option value="1" selected>$text.get("excel.list.yes")</option>
				 <option value="2">$text.get("excel.list.no")</option>
				 #else
			    <option value="1">$text.get("excel.list.yes")</option>
				<option value="2" selected>$text.get("excel.list.no")</option>
				#end
		      </select>
			</li>
		<li><span>$text.get("property.lb.jointable")：</span>
			<select name="joinTable" id="select"> 
			    #if($!result.joinTable==1)
				 <option value="1" selected>$text.get("excel.list.yes")</option>
				 <option value="2">$text.get("excel.list.no")</option>
				 #else
			    <option value="1">$text.get("excel.list.yes")</option>
				<option value="2" selected>$text.get("excel.list.no")</option>
				#end
		      </select>
		</li>
				<li><span>$text.get("property.lb.inputbill")：</span>
			<select name="inputBill" id="select"> 
			    #if($!result.inputBill==1)
				 <option value="1" selected>$text.get("excel.list.yes")</option>
				 <option value="2">$text.get("excel.list.no")</option>
				 #else
			    <option value="1">$text.get("excel.list.yes")</option>
				<option value="2" selected>$text.get("excel.list.no")</option>
				#end
		      </select>
		</li>
		<li><span>$text.get("property.lb.twoUnit")：</span>
			<select name="twoUnit" id="select"> 
			    #if($!result.twoUnit==1)
				 <option value="1" selected>$text.get("excel.list.yes")</option>
				 <option value="2">$text.get("excel.list.no")</option>
				 #else
			    <option value="1">$text.get("excel.list.yes")</option>
				<option value="2" selected>$text.get("excel.list.no")</option>
				#end
		      </select>
		</li>
		<li><span>$text.get("property.lb.selectalert")：</span><input  type="text" value="$!result.alertName"  name="selectAlert" id="selectAlert"></li>
		<li><span>$text.get("property.lb.sequence")：</span>
		     <select name="sequence" id="select"> 
			    #if($!result.isSequence==1)
				 <option value="1" selected>$text.get("excel.list.yes")</option>
				 <option value="2">$text.get("excel.list.no")</option>
				 #else
			    <option value="1">$text.get("excel.list.yes")</option>
				<option value="2" selected>$text.get("excel.list.no")</option>
				#end
		      </select>
		</li>
		<li><span>$text.get("property.lb.groupOrNot")：</span>
		      <select name="groupOrNot" id="select"> 
                 #if($!result.groupOrNot==1)
				 <option value="1" selected>$text.get("excel.list.yes")</option>
				 <option value="2">$text.get("excel.list.no")</option>
				 #else
			    <option value="1">$text.get("excel.list.yes")</option>
				<option value="2" selected>$text.get("excel.list.no")</option>
				#end
		      </select>
		</li>
		<li><span>$text.get("property.lb.sepAppoint")：</span>
		     <select name="sepAppoint" id="select"> 
			    #if($!result.sepAppoint==1)
				 <option value="1" selected>$text.get("excel.list.yes")</option>
				 <option value="2">$text.get("excel.list.no")</option>
				 #else
			    <option value="1">$text.get("excel.list.yes")</option>
				<option value="2" selected>$text.get("excel.list.no")</option>
				#end
		      </select>
		</li>
		<li><span>$text.get("property.lb.nameAndValue")：</span>
		      <select name="nameAndValue" id="select"> 
			  #if($!result.nameAndValue==1)
			    <option value="1"  selected>$text.get("excel.list.yes")</option>
			    <option value="2">$text.get("excel.list.no")</option>
			  #else
			    <option value="1">$text.get("excel.list.yes")</option>
			    <option value="2"  selected>$text.get("excel.list.no")</option>
			  #end
		      </select>
		</li>
		</ul>
	<div class="scroll_function_small_a">
		<div  name="edittableDIV" id="edittableDIV"> 
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function_b" name="edittable" id="edittable">
		</table>
		</div>
	</div>
</div>
</form> 
</body>
</html>
