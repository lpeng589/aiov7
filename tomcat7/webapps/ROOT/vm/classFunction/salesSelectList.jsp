<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self"></base>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$tableDisplayName $text.get("common.msg.LegiblePriceOutStock")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/AC_OETags.js" ></script>
<script language="javascript" src="/js/functionListSWF.js" ></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/popupGrid.js"></script>
<script language="javascript" src="$globals.js("/js/date.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/Main.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/popupPage.vjs","",$text)"></script>

<script language="javascript"> 
#set($hasSeq = false)
#foreach($row in $conditions)
#if($!globals.get($row,0)=="序列号")
#set($hasSeq = true)
#end
#end


var hasSelect = false;

//如果通过序列号查询或出库数进行查询，则自动选择第一条，其它不自动选择
#set($needAutoSel = false)
#foreach($row in $conditions)
#if($!globals.get($row,0)=="出库数" || $!globals.get($row,0)=="序列号")
	#if($!globals.get($row,2) != "") #set($needAutoSel = true)  #end
#end
#end

#if($needAutoSel)
function onSize(width,height){ 
		var oDiv=document.getElementById("conter");
		var sh = oDiv.style.height;
		var oDivHeight = Number(sh.substr(0,sh.length-2));
		var oDivWidth = document.body.clientWidth;
		obj = document.getElementById("functionListObj");
		if(height < oDivHeight-20){
			obj.height=height;
		}else{
			obj.height="100%";
		}
		if(width<oDivWidth-20){
			obj.width=width;
		}else{
			obj.width="100%";
		}
		
		var listObj = document.getElementById("functionListObj");
		listObj.selectItem('autoNum','1',true);
		keyValue = listObj.getCBoxValue("hidden");
		if(keyValue != ""){
			var values = keyValue.split(";") ;
			addTr(keyValue,values[values.length-3],'') ;
		}
}
#end



function checkAll(name){
	items = document.getElementsByName(name);
	for(var i=0;i<items.length;i++){
	    if(!items[i].disabled){
	       items[i].checked = !hasSelect;
	       var keyValue = items[i].value ;
	       if(items[i].checked==true){
		       var varValues = keyValue.split(";") ;
			   addTr(keyValue,varValues[varValues.length-3],'') ;
		   }else{
			   var keyIds = document.getElementsByName("cutKeyId") ;
			   for(var j=0;j<keyIds.length;j++){
				  if(keyIds[j].value==keyValue){
					  delRow(keyIds[j].parentNode.parentNode) ;
					  break ;
				  }
			   }
		   }
	    }
	}
  	hasSelect = !hasSelect;
}
var hasChild=false;
var childPop;
var selectName;
var mainId;
var isChild=false;
var hasClass=false;
var rows=0;
var popType="$!popBean.getType()";
var strSelectName = "$!selectName" ;
var tableName = "$!tableName" ;

#if($!popBean.getHasChild().length()>0)
hasChild=true;    
childPop="$!popBean.getHasChild()";
selectName="$!popBean.getName()";
#end
   
#if($!mainPop.length()>0)
isChild=true;
mainId="$!mainId";
#end
   
#if ($!hasClass)
hasClass=true;	
#end    

var popupType = "salesPopup" ;
var varIds = "" ;
#foreach($str in $listValue)
	#if("$str"!=";")
		varIds += "$str"+"|" ;
	#end
#end

#set($root = $request.getParameter("root"))
var parentCode5 = "$globals.classCodeSubstring("$!parentCode",5)";
var mainPop = "$!mainPop";

var hasChildids = "$!mainId;childKeyId;";
var isQuote = "$!isQuote";

function isQuoteFuction(){
   window.opener.openQuoteChild$!isQuote();
} 


var retpopvalue='';
#foreach($field in $!popBean.getReturnFields())
	retpopvalue=retpopvalue+'$field.getDefaultValue();';
#end


var resultSize = $result.size();
var keyId = "$!keyId";
var popBeanType = "$!popBean.getType()";
var saveParentFlag = $!popBean.isSaveParentFlag();
var MOID="$MOID";
var varColWidth = $!newCols.size() ;

function selectConfig(){
	AjaxRequest("${strAction}&selectType=config") ;
	document.getElementById("selectConfig").innerHTML = response ;
}

function onItemClick(str){
	//alert(str);	
}

function onItemDBClick(str){
 	str = decodeURIComponent(str); 
	eval(str);
}

function selectItem(keyValue){ 
	var listObj = document.getElementById("functionListObj");
	var keyVal = listObj.getCBoxValue("hidden");
	var checked = true ;
	if(keyVal.indexOf(keyValue)!=-1){
		checked = false ;
	}
	if(checked){
		var values = keyValue.split(";") ;
		addTr(keyValue,values[values.length-3],'') ;
	}else{
		var keyIds = document.getElementsByName("cutKeyId") ;
		for(var j=0;j<keyIds.length;j++){
			if(keyIds[j].value==keyValue){
				delRow(keyIds[j].parentNode.parentNode) ;
				break ;
			}
		}
	}
	listObj.selectItem("hidden",keyValue,checked) ;
}

function nextClass(strs){
	if(strs.split(":").length==2){
		form.action="/UserFunctionAction.do?root=root&operation=22&selectName="
					+ strs.split(":")[0] + "&MOID=22249636_0811041000090620009&MOOP=add&" 
					+ "isQuote=1&LinkType=@URL:&displayName=%E5%88%86%E7%BA%A7%E7%9B%AE%E5%BD%95";
		form.submit();
	}else{
		hasClassBill(true,strs);
	}
}

function onFlashKeyDown(keyCode,ctrl,alt,shiftKey){
	if(keyCode==27){
		window.close() ;
	}
}

var No = 0;
function n(obj){
	return document.getElementById(obj);
}

var _batchAdd = 0;

function addTr(returnValue,keyValue,varCutQty){
	
	var values = returnValue.split(";") ;
	seq = values[values.length-2];
	var keyIds = document.getElementsByName("cutKeyId") ;
	for(var j=0;j<keyIds.length;j++){
		if(keyIds[j].value==returnValue){
			return ;
		}
	}
	var my_body = n("tblSelTable").tBodies[0];
	var rowNo = my_body.rows.length ;
	var my_row = my_body.insertRow(rowNo);
	//var serial = my_row.insertCell();	
	var serial = document.createElement("td");
	#if($hasSeq) 
		//var seqCell = my_row.insertCell();
		var seqCell = document.createElement("td");
		seqCell.innerHTML=seq;
	#end
	
	//var keyCell = my_row.insertCell();
	//var cutQty = my_row.insertCell(); 
	//var del = my_row.insertCell();
	var keyCell = document.createElement("td");
	var cutQty = document.createElement("td");
	var del = document.createElement("td"); 
	
	//for(var i = 0;;i++){
//		if($(i) == null){
//			No = i;	
//			break;
//		}
//	}

	No = my_body.rows.length-1;
	if(No<0)
	No = 0;

	my_row.id = No;
	serial.id = "serial"+No;
	serial.className = "listheadonerow";
	serial.innerHTML = No+1;
	
    keyCell.innerHTML="<input type='hidden' name='cutKeyId' value='"+returnValue+"'/><input type='hidden' name='cutSeq' value='"+seq+"'/>"
    	+"<input type='hidden' name='codeValue'  value='"+keyValue+"'>"+keyValue;
	cutQty.innerHTML = "<input type='text' name='cutQty' value='"+varCutQty+"' onchange='execStat();'/>" ;
	del.innerHTML = "<img src='/$globals.getStylePath()/images/del.gif' title='Ctrl+L' onclick='delRow(this.parentNode.parentNode)' />";
	No++;
	//if(_batchAdd != 1)
	
	my_row.appendChild(serial);
	#if($hasSeq) 
	my_row.appendChild(seqCell);
	#end
	my_row.appendChild(keyCell);
	my_row.appendChild(cutQty);
	my_row.appendChild(del);
	
	execStat() ;
}

function delRow(tr){
	var keyIds = document.getElementsByName("cutKeyId") 
	var keyValue = keyIds[tr.rowIndex-1].value ;
	var listObj = document.getElementById("functionListObj");
	listObj.selectItem("hidden",keyValue,false) ;
	var start = Number(tr.id) + 1;
	n("tblSelTable").deleteRow(tr.rowIndex);
	for(;;start++){
		if(n(start) == null){
			break;
		}
		n(start).id = start -1;
		n("serial"+start).innerHTML = start;
		n("serial"+start).id = "serial"+(start -1);
	}
	No--;
	execStat() ;
}

function execStat(){
	var cutQtys = document.getElementsByName("cutQty") ;
	var codeValue = document.getElementsByName("codeValue") ;
	var cutNumber = 0 ;
	for(var i=0;i<cutQtys.length;i++){
		if(cutQtys[i].value==""){
			cutNumber += parseFloat(codeValue[i].value) ;
		}else{
			cutNumber += parseFloat(cutQtys[i].value) ;
		}
	}
	document.getElementById("coutNumberId").innerHTML = cutQtys.length ;
	document.getElementById("cutNumberId").innerHTML = Math.round(cutNumber*100)/100.00; 
	
}

function initDatas(){
	#set($num=0)
	#foreach($key in $!keyValues)
	addTr('$key','$globals.get($codeValues,$num)','$globals.get($cutQtys,$num)') ;
	#set($num=$num+1)
	#end
}

function submitBefore(){
	var ids = "" ;
	var keyIds = document.getElementsByName("cutKeyId") ;	
	var cutSeqs = document.getElementsByName("cutSeq") ;
	var cutQtys = document.getElementsByName("cutQty") ;
	var codeValue = document.getElementsByName("codeValue") ;
	if(typeof(keyIds)=="undefined" || keyIds.length==0){
		alert('$text.get("common.msg.mustSelectOne")');
		return false ;
	}
	var num = 0 ;
	for(var i=0;i<cutQtys.length;i++){
		var cutQty = cutQtys[i].value ;
		var codeQty = codeValue[i].value ;
		if(parseFloat(cutQty)>=parseFloat(codeQty)){
			alert("第"+(i+1)+"行的裁剪数量不能大于等于原数量") ;
			return false ;
		}
	}
	for(var i=0;i<keyIds.length;i++){
		var keyValue =  keyIds[i].value ;
		var varKeys = keyValue.split(";") ;
		var keyValue = keyValue.substring(keyValue.indexOf("@#")+2,keyValue.length) ;
		if(cutQtys[i].value.length>0){
			var varSeq = varKeys[varKeys.length-2] ;
			keyValue = keyValue.replace(varSeq,codeValue[i].value+"@CUT_SEQ@"+varSeq) ;
			if("tblSalesOutStockDet"=="$!tableName"){
				var values = keyValue.split(";") ;
				keyValue = "" ;
				for(var j=0;j<values.length;j++){
					if(j==(values.length-3) || j==(values.length-4)){
						keyValue += cutQtys[i].value + ";" ;
					}else{
						keyValue += values[j] + ";" ;
					}
				}
				if(keyValue.length>0){
					keyValue = keyValue.substring(0,keyValue.length-1) ;
				}
			}else{
				keyValue = keyValue.replaceAll(";"+codeValue[i].value+";"+codeValue[i].value+"@CUT_SEQ@",";"+cutQtys[i].value+";"+codeValue[i].value+"@CUT_SEQ@") ;
			}
		}
		ids += keyValue +"|" ;
		
	}	
	eval('window.parent.exe'+pupupWin+'("'+ids+'");');
	window.parent.jQuery.close(pupupWin);
}

function firstCheck(){
	//var listObj = document.getElementById("functionListObj");
	//var keyVal = listObj.getCBoxValue("hidden");
	
	//var varKeyIds = document.getElementsByName("varKeyId") ;
	//if(varKeyIds.length>0 && $("selectQty").value.length>0){
	//	var keyValue = varKeyIds[0].value ;
	//	if(existValue(keyValue)){
	//		var varValues = keyValue.split(";") ;
	//		addTr(keyValue,varValues[varValues.length-3],'') ;
	//		varKeyIds[0].checked = true ;
	//	}
	//}
}

function onCheckBoxClick(str,selected){
	str = decodeURIComponent(str); 
	var keyValue = str.substring(str.indexOf("'")+1,str.lastIndexOf("'")) ;
	if(selected){
		var values = keyValue.split(";") ;
		addTr(keyValue,values[values.length-3],'') ;
	}else{
		var keyIds = document.getElementsByName("cutKeyId") ;
		for(var j=0;j<keyIds.length;j++){
			if(keyIds[j].value==keyValue){
				delRow(keyIds[j].parentNode.parentNode) ;
				break ;
			}
		}
	}
}

function onSelectAll(selected){
	var listObj = document.getElementById("functionListObj");
	if(selected){
		var keyVal = listObj.getCBoxValue("hidden");
		var items = keyVal.split("|") ;
		if(items.length>1)
		_batchAdd = 1;
		for(var i=0;i<items.length;i++){
		   var keyValue = items[i] ;
		   if(selected){
			  var varValues = keyValue.split(";") ;
			  addTr(keyValue,varValues[varValues.length-3],'') ;
		   }else{
			   var keyIds = document.getElementsByName("cutKeyId") ;
			   for(var j=0;j<keyIds.length;j++){
			   	  if(keyIds[j].value==keyValue){
			   		 delRow(keyIds[j].parentNode.parentNode) ;
		    		 break ;
			   	  }
			   }
		   }
		}
		_batchAdd = 0;
		execStat();
	}else{
		jQuery(".selectlist tbody tr").remove();
		execStat();

//		var keyVal = obj.getAllValue('hidden');
//		var items = keyVal.split("|") ;
//		for(var i=0;i<items.length;i++){
//		   	var keyValue = items[i] ;
//		   	var keyIds = document.getElementsByName("cutKeyId") ;
//			for(var j=0;j<keyIds.length;j++){
//				if(keyIds[j].value==keyValue){
//			   		delRow(keyIds[j].parentNode.parentNode) ;
//		    		break ;
//			   	}
//			}
//		}
	}
}

	
function qtyFocus(){
	var varQty = n("selectQty") ;
	if(typeof(varQty)!="undefined" && varQty!=null){
		varQty.focus() ;
	}
}

var pupupWin = "$!popupWin";
</script>
</head>

<body onLoad="qtyFocus();initDatas();" scroll="no">

<form  method="post" scope="request" name="form" onKeyDown="down()" action="/UserFunctionAction.do">
 <input type="hidden" name="root" value="$!root">
  <input type="hidden" name="isRoot" value="">
 <input type="hidden" name="operation" value="$globals.getOP('OP_POPUP_SELECT')">
 <input type="hidden" name="tableName" value="$!tableName">
 <input type="hidden" name="parentTableName" value="$!parentTableName">
 <input type="hidden" name="fieldName" value="$!fieldName">
 <input type="hidden" name="parentCode" value="$!parentCode">
 <input type="hidden" name="selectName" value="$!selectName">
 <input type="hidden" name="popupType" value="$!popupType">
 <input type="hidden" name="displayName" value="$!displayName">
 <input type="hidden" name="reportNumber" value="$!reportNumber">
 <input type="hidden" name="backType" value="">
 <input type="hidden" name="isQuote" value="$!isQuote"/>
 <input type="hidden" name="MOID" value="$!MOID">
 <input type="hidden" name="MOOP" value="$!MOOP">
 <input type="hidden" name="iniPropField" value="$!iniPropField">
 <input type="hidden" name="keyIdType" value="saveType">
 <input type="hidden" name="queryChannel" value="$!queryChannel"/>
 <input type="hidden" name="strAction" value="$strAction" />
 <input type="hidden" name="nextClass" value="">
 <input type="hidden" name="popType" value="$!popType">
 <input type="hidden" name="popupWin" value="$!popupWin">
 #if($!mainId.length()>0)
	<input type="hidden" name="mainId" value="$!mainId">
 	<input type="hidden" name="mainPop" value="$!mainPop">
 #end
 <input type="hidden" name="queryField">
 $!mainHiddenFields
 <div class="Heading" >
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$tableDisplayName</div>
	
	<ul class="HeadingButton_Pop-up">
		<li>
			<div class="tit"><a href="javascript:beforeSubmit();" onClick="">&nbsp;$text.get("common.lb.query")</a><img src="/$globals.getStylePath()/images/ico2.gif" id="tb_mr2" class="tb_mr">
				<div id="more2" class="more" style="width:70px;">
				<IFRAME width=100% height=80 style="position:absolute;top:0px;left:0px;z-index:-1; border-style:none;filter:Alpha(opacity=0)" frameborder="0"></IFRAME>
 
				#if("true"!="$!popPop" && $!mainId.length()==0)
				<a href="javascript:advanceQuery();" onClick="">&nbsp;$text.get("common.lb.Advancequery")</a>
				#end
				</div>
			</div>		
		</li>
		#if("$!queryChannel"!="advance")
			#if($!mainId.length()>0 && "true"!="$!popPop")
			<li><button  type="button" onClick="backMain()">$text.get("common.lb.backmainlist")</button></li>		
			#end
			#if($!popBean.getForwardModel().length()>0 && "true"!="$!popPop")
				#set($url="/UserFunctionQueryAction.do?tableName="+$!popBean.getForwardModel())
				#if($LoginBean.operationMap.get($!url).add())
				<li><button name="addPage" type="button" onClick="openNewWin('/UserFunctionQueryAction.do?tableName=$!popBean.getForwardModel()&operation=6&forward=add&noback=true');">$text.get("common.lb.add")</button></li>
				#end
			#end
			#if("$!isQuote"==""&& "$!root" != "root")
			<li><button type="button" onClick="resetSubmit()">$text.get("com.db.clear")</button></li>
			#end
		#end
		<li><button type="button" onClick="submitBefore();">$text.get("common.lb.ok")</button></li>
		#if("$!isRoot" != "root" && "$!parentCode" =="" && $!root == "root")
			<li><button type="button" id="backSubmitBt" onClick="backSubmit2()">$text.get("common.lb.back")</button></li>	
		#end
		#if("$!parentCode" !="")
			<li><button type="button" id="backSubmitBt" onClick="backSubmit()">$text.get("common.lb.back")</button></li>	
		#end
		<li><button type="button" onClick="closeWindow();">$text.get("common.lb.close")</button></li>
	</ul>
</div>
<div class="listRange_Pop-up" style="height:403px;">
			#if($conditions.size()>0)
	<ul class="listRange_1_Pop-up" id="listid">	 		
			#foreach($row in $conditions)
			#if($!globals.get($row,0)=="序列号")
			<li><span>$!globals.get($row,0)：</span><input id="selectQty" name="$!globals.get($row,1)" value="$!globals.get($row,2)" onKeyDown="if(event.keyCode==13) {form.queryField.value=this.name;beforeSubmit()} " ></li>
			#elseif($!globals.get($row,0)=="出库数")
			<li><span>$!globals.get($row,0)：</span><input id="selectQty" name="$!globals.get($row,1)" value="$!globals.get($row,2)" onKeyDown="if(event.keyCode==13) {form.queryField.value=this.name;beforeSubmit()} " ></li>
			#end
			#end
			#if($!popBean.keySearch)
			#<li><span>$text.get("com.lb.keySearch")：</span><input name="keySearch" value="$!keySearch" onKeyDown="if(event.keyCode==13) {form.queryField.value=this.name;beforeSubmit()} " ></li>
			#end
			#foreach($row in $conditions)
			#if($!globals.get($row,0)!="出库数" && $!globals.get($row,0)!="序列号")
				#if($!globals.get($row,3) == "0")
				<li><span>$!globals.get($row,0)：</span><input name="$!globals.get($row,1)" value="$!globals.get($row,2)" onKeyDown="if(event.keyCode==13) {form.queryField.value=this.name;beforeSubmit()} " ></li>
				#elseif($!globals.get($row,3) == "1")
				<li><span>$!globals.get($row,0)：</span><input name="$!globals.get($row,1)" date="true" value="$!globals.get($row,2)" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  ></li>
				#elseif($!globals.get($row,3) == "2")
				<li><span>$!globals.get($row,0)：</span><select name="$!globals.get($row,1)" >
						<option value="" ></option>
						#foreach($erow in $globals.getEnumerationItems($!globals.get($row,4)))
							<option value="$erow.value" #if("$erow.value"=="$!globals.get($row,2)") selected #end>$erow.name</option>
						#end
						</select></li>
				#elseif($!globals.get($row,3) == "3")
					<li><span>$!globals.get($row,0)：</span><input name="$!globals.get($row,1)" date="true" value="$!globals.get($row,2)" onKeyDown="if(event.keyCode==13) openInputDate(this);" onDblClick="openSelect('$!globals.get($row,0)','$!globals.get($row,4)','$!globals.get($row,1)')"  >
						<img src="/$globals.getStylePath()/images/St.gif" class="search" onClick="openSelect('$!globals.get($row,0)','$!globals.get($row,4)','$!globals.get($row,1)')">
					</li>
				#end
			#end
			#end
	</ul>
			#end
		#if($parentName.length()>0)			
				<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span></div>
		#end
<div  id="conter" style="float:left;">
#set($dwidth = "650")
#if($hasSeq) #set($dwidth = "595")  #end		
<div style="float:left; width:${dwidth}px;overflow:hidden;">
	<div class="listRange_Pop-up_scroll">
			<script type="text/javascript">writeFunctionListSWF();</script>		
	</div>	
</div>
#set($dwidth = "220")
#if($hasSeq) #set($dwidth = "280")  #end
<div class="listRange_Pop-up_scroll_2" style="float:left;width:${dwidth}px;" id="conter2">
	  <div class="listRange_Pop-up_scroll_3" style="height:220px; width:100%;padding:0px;margin:0px">
	  <table border="0" width="100%" cellpadding="0" cellspacing="0" class="selectlist" name="table" id="tblSelTable">
		  <thead>
		  	<tr>
		  		<td width="20">&nbsp;</td>
		  		#if($hasSeq) <td>序列号</td>  #end
				<td width="50">出库数</td>
				<td width="50">裁剪出库数量</td>
				<td width="22">&nbsp;</td>
			</tr>
		  </thead>
		  <tbody>
		  </tbody>
	  </table>
	  </div>
	  <div class="listRange_Pop-up_scroll_4" style="width:98%;padding:10px 0px 0px 0px;margin:0px;margin-left:5px;">
	  <table border="0" width="165px;" cellpadding="0" cellspacing="0" class="selectlist2" name="table">
		  <thead>
		  	<tr>
		  		<td width="50%" colspan="2">条数合计:</td>
				<td width="50%" colspan="2"><span id="coutNumberId">0</span></td>
			</tr>
			<tr>
		  		<td width="50%" colspan="2">数量合计:</td>
				<td width="50%" colspan="2"><span id="cutNumberId">0</span></td>
			</tr>
		  </thead>
		  <tbody>
		  </tbody>
	  </table>
	  </div>
</div>
</div>
			#if($!mainPop.length()>0)	
			#else	
			<div class="listRange_pagebar"> $!pageBar </div>
			#end
</div>

<script type="text/javascript">
	function getXMLData(){ 
		return '$popupHTML' ;
	}	
</script>

<script type="text/javascript">
	var rowCount = document.getElementsByName("varKeyId").length ;
	if("$!pageSize"!=rowCount){
		var varPage = document.getElementById("nextPageSize") ;
		if(typeof(varPage)!="undefined" && varPage!=null){
			varPage.removeAttribute("href","") ;
		}
	}
</script>

</form>
<script type="text/javascript">
	document.attachEvent?document.attachEvent("onclick",moreBlur2):document.addEventListener("click",moreBlur2,false);	
</script>
<div id="selectConfig">

</div>
</body>
</html>
