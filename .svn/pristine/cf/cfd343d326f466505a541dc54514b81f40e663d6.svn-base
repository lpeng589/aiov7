<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self"></base>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("pop.title.sequence")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript"> 
/*判断是否只含有数字和英文字符或下划线*/
function isEnNumLine(str){
	var pattern = new RegExp("^[0-9A-Za-z_-]*$");
	ret = str.search(pattern);
	return ret>-1;
}

function isEnOrNum(value,tip){
   if(value!=""&&!isEnNumLine(value)){
		alert(tip+":"+"$text.get('common.validate.error.en2')");
		return false
   }
   return true;
}
function isNum(obj,tip){
   if(obj.value!=""&&!isInt(obj.value)){
	   alert(tip+":"+"$text.get('common.validate.error.integer')")
	   obj.focus();
	   return false;
   }
   return true;
}
//确定按钮事件
function closeSubmit(){
	var rowCount=n("edittable_body").rows.length;
 	for(i=0;i<rowCount;i++){
   		if(!isEnOrNum(n("seq"+(i)).value.trim(),"$text.get('pop.title.sequence1')")){
   			n("seq"+(i)).focus();
   			return false;  
   		}
 	}
	var result="";
	var seqCount=0;
	if(rowCount!=0){
 		for(i=rowCount-1;i>=0;i--){
   			if(n("seq"+(i)).value.trim()!=""){
      			var lastSeq=n("seq"+(i)).value.trim();
      			result=lastSeq+"#;#";
	  			break;
   			}
 		}
 		for(i=0;i<rowCount;i++){
   			if(n("seq"+(i)).value.trim()!=""){
   				seqCount ++;
   				result+=n("seq"+(i)).value+"~";
   				//alert("序列号不能为空");return false;
   			}
   
 		}
  		for(i=0;i<rowCount;i++){
   			for(j=0;j<rowCount;j++){
    			if(j!=i){
	  				if(n("seq"+(i)).value.trim()!=""&&n("seq"+(j)).value.trim()!=""&&n("seq"+(i)).value.trim()==n("seq"+(j)).value.trim()){
        				alert('$text.get("seq.validate.error5")'+'['+$("seq"+(i)).value.trim()+']');
					    return false;
	  				}
     			} 
			}
   		}
   		if(rowCount>200){
   			alert('一次录入的序列号不能超过200');
   			return;
   		}
 		var urls="/UtilServlet?operation=validateSeq&tableName=$!tableName&validateField=$!seqFname&goodsCode=$!goodsCode&oldSeq=$!seq&defSeqStr=$!defSeqStr&seqStr="+result.split("#;#")[1]+"&lastSeq=$!lastSeq";
 		AjaxRequest(urls) ;
		if(response!="no response text"){
		   if(response!=""){
		   		alert(response);
				return false;
		   }
		}
		window.parent.dataBackIn("false","@TABLENAME.Seq_hid;@TABLENAME.Seq",result,window.parent);	
	}else{
		window.parent.dataBackIn("false","@TABLENAME.Seq_hid;@TABLENAME.Seq",'#;#',window.parent);
	}
	var logVal = "";
	for(var i = 0; i< parent.goodsCodeLogicValidate.length;i++){
		if(parent.goodsCodeLogicValidate[i].key==parent.curGridRowNum.curKey){
			logVal = parent.goodsCodeLogicValidate[i].value;
		}
	}
	if(logVal != ""){
		var seq_qtyName =  logVal.split(":")[2];
		var seq_qty = parent.document.getElementsByName(parent.curGridRowNum.curKey+"_"+seq_qtyName);
		seq_qty[parent.curGridRowNum.curLine].value=seqCount;
		$(seq_qty[parent.curGridRowNum.curLine]).trigger("change");
	}
	window.parent.jQuery.close("ChildPopup");
}
//页面加载初始化序列号列表
function iniData(){
	var seqStr='$!seq';
	
	var seqArr=seqStr.split("~");
	var seqTime="$!seqTime".split(",");
	for(i=0;i<seqArr.length-1;i++){
  		addTr("edittable_body",seqArr[i],seqTime[i]);
	}

 	
 	#foreach($varSeq in $seqList)
 		#if($seq.indexOf($globals.get($varSeq,0))==-1)
 			addTr("stocktable_body","$globals.get($varSeq,0)","$globals.get($varSeq,1)") ;
 		#end
 	#end
}


function  isExistSeq(table_body,seqArr){
	for(i=0;i<seqArr.length;i++){
		if(typeof(n(table_body).rows)=="undefined") continue;
		for(j=0;j<n(table_body).rows.length;j++){
			if(seqArr[i]==n("seq"+j).value){
			   alert('$text.get("seq.validate.error5")'+'['+seqArr[i]+']');
			   return true;
			}
		}
 	}
  	return false;
}

var No = 0;
var StockNo = 0 ;
function n(obj){
	return document.getElementById(obj);
}

function addTr(table_body,seqVal,timeVal){
	var my_body = n(table_body);
	var rowNo = my_body.rows.length ;
	var my_row = my_body.insertRow(rowNo);
	if(table_body=="edittable_body"){
		//var timeCell = my_row.insertCell();
		//var seqCell = my_row.insertCell();
		//var serial = my_row.insertCell();
		var serial = document.createElement("td");
		var seqCell = document.createElement("td");
		var timeCell = document.createElement("td");
		#if(!$!request.getParameter("page")||$!request.getParameter("page")!="detail")
		var del = my_row.insertCell();
		#end
		
		for(var i = 0;;i++){
			if(n(i) == null){
				No = i;	
				break;
			}
		}
		my_row.id = No;
		serial.id = "serial"+No;
		serial.className = "listheadonerow";
		serial.innerHTML = No+1;
		seqCell.innerHTML="<input type='text' name='seq"+No+"' onfocus='addRowByfocus(\""+table_body+"\",this)' id='seq"+No+"' size='38' value='"+seqVal+"' onKeyDown='if(event.keyCode ==13){event.keyCode=9}'>";
		seqCell.id="seqCell"+No;
		timeCell.innerHTML="<input type='hidden' name='time"+No+"' id='time"+No+"' value='"+timeVal+"'/>"+timeVal;
		timeCell.id="timeCell"+No;
		#if(!$!request.getParameter("page")||$!request.getParameter("page")!="detail")
		del.innerHTML = "<img src='/$globals.getStylePath()/images/del.gif' onclick='delRow(\""+table_body+"\",this)' />";
		#end
		No++;
		
		my_row.appendChild(serial);
		my_row.appendChild(seqCell);
		my_row.appendChild(timeCell);
		#if(!$!request.getParameter("page")||$!request.getParameter("page")!="detail")
		my_row.appendChild(del);
		#end
	}else{
		//var timeCell = my_row.insertCell();
		//var del = my_row.insertCell();
		//var seqCell = my_row.insertCell();
		//var noCel = my_row.insertCell();
		var noCel = document.createElement("td");
		var seqCell = document.createElement("td");
		var del = document.createElement("td");
		var timeCell = document.createElement("td");
		
		noCel.id = "No"+StockNo ;
		noCel.innerHTML = StockNo+1 ;
		seqCell.innerHTML = "<input type='checkBox' onclick='rightMove()'  name='stockSeq' value='"+seqVal+"'/>";
		seqCell.style.display="none";
		del.innerHTML=seqVal;
		timeCell.innerHTML="<input type='hidden' name='stockTime' value='"+timeVal+"'/>"+timeVal;
		StockNo++;
		
		my_row.appendChild(noCel);
		my_row.appendChild(seqCell);
		my_row.appendChild(del);
		my_row.appendChild(timeCell);
		my_row.onclick=function(){jQuery(this).find("input[name='stockSeq']")[0].checked=true;rightMove();};
		my_row.style.cursor='pointer';
	}
}

function rightMove(){
	var stockSeq = document.getElementsByName("stockSeq");
	var stockTime = document.getElementsByName("stockTime");
	if(typeof(stockSeq)=="undefined"){
		alert('$text.get("sequence.lb.alarm")') ;
	}else{
		var seqLen = stockSeq.length ;
		var hasCheck = false ;
		
		for(var i=0;i<seqLen;i++){
			if(typeof(stockSeq[i])!="undefined" && stockSeq[i].checked==true){
				var tr = stockSeq[i].parentNode.parentNode;
				var seqArr = new Array() ;
				seqArr.push(stockSeq[i].value) ;
				if(!isExistSeq("edittable_body",seqArr)){
					addTr("edittable_body",stockSeq[i].value,stockTime[i].value);
					n("seqStr").value = n("seqStr").value + stockSeq[i].value + "~" ;
				}
				delStockRow(tr.rowIndex) ;
				seqLen-- ;
				i-- ;
				hasCheck = true ;
			}
		}
		if(!hasCheck){
			alert('$text.get("sequence.lb.alarm")') ;
		}
	}
}
function delStockRow(rowIndex){
	n("stocktable_body").deleteRow(rowIndex-1);
	for(;;rowIndex++){
		if(n("No"+rowIndex) == null){
			break;
		}
		n("No"+rowIndex).innerHTML = rowIndex;
		n("No"+rowIndex).id ="No"+ (rowIndex-1);
	}
	StockNo-- ;
}
function delRow(table_body,obj){
	var tr = obj.parentNode.parentNode;
	var start = Number(tr.id) + 1;
	var delValue = n("seq"+(tr.rowIndex-1)).value ;
	var timeValue = n("time"+(tr.rowIndex-1)).value ;
	
	addTr("stocktable_body",delValue,timeValue) ;
	
	var strSeq = n("seqStr").value ;
	n("seqStr").value = strSeq.replace(delValue+"~","") ;
	n(table_body).deleteRow(tr.rowIndex-1);
	for(;;start++){
		if(n(start) == null){
			break;
		}
		n(start).id = start -1;  
		n("serial"+start).innerHTML = start;
		n("serial"+start).id = "serial"+(start -1);
		n("seqCell"+start).innerHTML ="<input type='text' onfocus='addRowByfocus(this)' name='seq"+(start-1)+"' id='seq"+(start-1)+"' value='"+n("seq"+start).value+"' size='38' onKeyDown='if(event.keyCode ==13){event.keyCode=9}'/>";
		n("seqCell"+start).id = "seqCell" + (start-1);
		n("timeCell"+start).innerHTML ="<input type='hidden' name='time"+(start-1)+"' id='time"+(start-1)+"' value='"+n("time"+start).value+"'/>"+n("time"+start).value;
		n("timeCell"+start).id = "timeCell" + (start-1);
		
	}
	No--;
}

function addRowByfocus(table_body,obj){
	//var tr = obj.parentNode.parentNode;
	//if(n(table_body).rows.length == tr.rowIndex ){
	//	addTr(table_body,"");
	//}
}

function resetSeq(){
	 window.parent.jQuery.close("ChildPopup");
}

function clearSeq(){
	window.parent.dataBackIn("false","@TABLENAME.Seq_hid;@TABLENAME.Seq",';',window.parent);
	var logVal = "";
	for(var i = 0; i< parent.goodsCodeLogicValidate.length;i++){
		if(parent.goodsCodeLogicValidate[i].key==parent.curGridRowNum.curKey){
			logVal = parent.goodsCodeLogicValidate[i].value;
		}
	}
	if(logVal != ""){
		var seq_qtyName =  logVal.split(":")[2];
		var seq_qty = parent.document.getElementsByName(parent.curGridRowNum.curKey+"_"+seq_qtyName);
		seq_qty[parent.curGridRowNum.curLine].value=0;
	}
    window.parent.jQuery.close("ChildPopup");
}
window.onerror=function(){return true};
</script>
<style>
.HeadingButton_seq {
	margin-top:10px;
	width:200px;
	float:right;
	padding-right:10px;
}
.HeadingButton_seq li{
	float:left;
}

.list_seq {
	#if($!request.getParameter("page")=="detail")
	width:440px; 
	#else 
	width:900px;
	#end
	padding-top:5px;
	overflow-y:auto;
}
.list_table {
	float:left; 
	#if($!request.getParameter("page")=="detail")
	width:400px;
	#else
	width:420px;
	#end
	margin-left:10px; 
	height:270px; 
	overflow-y:auto;
	overflow-x: none;
}

.list_table2 {
	float:left; 
	#if($!request.getParameter("page")=="detail")
	width:0px;
	#else
	width:390px;
	#end
	margin-left:20px; 
	height:270px; 
	overflow-y:auto;
}

.seq_select {
	width:250px;
	float:right;
	height:auto;
}
.seq_select li{
	float:left;
	width:230px;
	height:20px;
	line-height:180%;
}
.m20 {
	margin-left:8px;
}
.seq_select li span{
	width:115px;
	text-align:right;
}
.seq_select input{
	border:0;
	width: 100px;
	border-bottom: 1px solid #8FB8E5;	
	text-align:left;
	height:16px;
}
.selectlist tbody td{
	width:auto;
}
</style>
</head>
<body onLoad="iniData()" scroll="no">

<form  method="post" scope="request" name="form"  action="/UserFunctionAction.do?$!queryString" >
<input type="hidden" id="seqStr" name="seqStr" value="$!seq">
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_POPUP_SELECT")">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("pop.title.sequence")</div>
	<ul class="HeadingButton">
		<li #if($!request.getParameter("page")=="detail")style="display:none;"#end><button type="button"  onClick="closeSubmit()">$text.get("common.lb.save")</button></li>
		<li #if($!request.getParameter("page")=="detail")style="display:none;"#end><button type="button"  onClick="clearSeq()">$text.get("common.lb.clear")</button></li>
		<li><button type="button"  onClick="javascript:window.parent.jQuery.close('ChildPopup');">$text.get("common.lb.close")</button></li>
	</ul>
</div>
<div class="list_seq">
<div class="list_table" >
			<table border="0" cellpadding="0" cellspacing="0" width="85%" class="selectlist" name="edittable" id="edittable">
				<THead>
					<tr class="listhead">
						<td class="listheade" width="30" align="center">
							<span style="vertical-align:middle;"><IMG
									src="/$globals.getStylePath()/images/down.jpg" border=0 />
							</span>
						</td>
						<td width="220" align="center"> 
							$text.get("aready.chooce.sequence")
						</td>
						<td width="100" align="center">
							入库日期
						</td>
						<td width="30" align="center">
							&nbsp;
						</td>
					</tr>
				</THead>
				<TBody id="edittable_body" align="center">
				</TBody>
			</table>
</div>
<div class="list_table2" #if($!request.getParameter("page")=="detail")style="display:none;"#end >
			<table border="0" cellpadding="0" cellspacing="0" class="selectlist" name="stocktable" id="stocktable">
				<THead>
					<tr class="listhead">
						<td class="listheade" width="30" align="center">
							<input type="checkbox" onClick="checkAll('stockSeq');rightMove()"/>
						</td>
						<td width="250" align="center">
							$text.get("storage.sequence")
						</td>
						<td width="100" align="center">
							入库日期
						</td>
					</tr>
				</THead>
				 
				<TBody id="stocktable_body" align="center">
					
				</TBody>
				
			</table>
</div>
<div class="listRange_pagebar" #if($!request.getParameter("page")=="detail")style="display:none;"#end> $!pageBar </div>
</div>
</form>
</body>
</html>
