<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self"></base>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("pop.title.sequence")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript"> 
/*判断是否只含有数字和英文字符或下划线*/
function isEnNumLine(str){
	var pattern = new RegExp("^[0-9A-Za-z_-]*$");
	ret = str.search(pattern);
	return ret>-1;
}

function isEnOrNum(value,tip){
  	if(value!="" && !isEnNumLine(value)){
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
	var rowCount=n("my_body").rows.length;
    for(i=0;i<rowCount;i++){
   		if(!isEnOrNum(n("seq"+(i)).value.trim(),"$text.get('pop.title.sequence1')")){
			n("seq"+(i)).focus();;
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
	  				if(n("seq"+(i)).value.trim()!=""&&n("seq"+(j)).value.trim()!=""&&n("seq"+(i)).value.toLowerCase().trim()==n("seq"+(j)).value.toLowerCase().trim()){
        				alert('$text.get("seq.validate.error5")'+'['+n("seq"+(i)).value.trim()+']');
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

		if(response!="" && response!="no response text"){
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
	for(i=0;i<seqArr.length-1;i++){
  		addTr(seqArr[i]);
	}
	
	var rowCount=n("my_body").rows.length;
	if(rowCount==0){
     	addTr('');
 	}
}
//自动生成序列号







function createSeq(){
	var rad=document.getElementsByName("choice");
	var radVal="";
	for(i=0;i<rad.length;i++){
 		if(rad[i].checked){
   			radVal=rad[i].value;
 		}
	}
	if(radVal=="auto"){
		var res="";
		var before=document.getElementById("before");
		var end=document.getElementById("end");
		var startnum=document.getElementById("startnum");
		var inputcount=document.getElementById("inputcount"); 
	 	if(startnum.value!=""&&inputcount.value!=""){
	    	if(!containSC(before.value)){alert('$text.get("pop.lb.before")'+"$text.get("seq.message.message2")");return false;}
			if(!containSC(end.value)){ alert('$text.get("pop.lb.end")'+"$text.get("seq.message.message2")");return false;}
			if(!isNum(startnum,'$text.get("pop.lb.startnum")')) return false;
			if(!isNum(inputcount,'$text.get("pop.lb.inputcount")')) return false;
			var rs=parseInt(startnum.value)+parseInt(inputcount.value)-1;

			if(startnum.value.length>(parseInt(startnum.value)+"").length&&(rs+"").length>startnum.value.length){
				alert('$text.get("common.msg.errorAmountOverArea")');
				return false;
			}
			res="1:)0:)"+before.value+"~"+end.value+"~"+startnum.value+"~"+inputcount.value+";";
		}
		if(res!=""){
		 	var seqArr=getSeqArr(res);
		  	if(isExistSeq(seqArr)){
	        	return false;
	      	}
	      	for(i=0;i<seqArr.length;i++){
	      		if(!isEnOrNum(seqArr[i].trim(),"$text.get('pop.title.sequence1')")){
	      			return ;
	      		}
	      	}
		 	for(i=0;i<seqArr.length;i++){
	        	addTr(seqArr[i].trim());
		 	} 	
		}else{
			alert("请输入序列号的生成基数和生成数量");
		 	return false;
		}
	}else{
  		var sep=document.getElementById("separator").value;
  		var seqList=document.getElementById("seqList").value;
  		if(seqList==""){
  			alert("请输入序列号列表，以回车分隔");
    		return false;
  		}
  		var seqArr="";
  		if(sep=="comma"){
    		seqArr=seqList.split(",");
  		}else{
    		seqArr=seqList.split("\n");
  		}
   		if(isExistSeq(seqArr)){
    		return false;
   		}
   		for(i=0;i<seqArr.length;i++){
		    if(!isEnOrNum(seqArr[i].trim(),"$text.get('pop.title.sequence1')")){
		    	return ;
		    }
		}
  		for(i=0;i<seqArr.length;i++){
    		if(seqArr[i]!=""){
	 			addTr(seqArr[i].trim());
			}
  		}
	}
  	for(j=0;j<n("my_body").rows.length;j++){
	   if(n("seq"+j).value.trim().length==0){
	     delRow(n("seq"+j));
	   }
	}
}
function  isExistSeq(seqArr){
	for(i=0;i<seqArr.length;i++){
		for(j=0;j<n("my_body").rows.length;j++){
			if(seqArr[i].length>0 && seqArr[i]==n("seq"+j).value){
		   		alert('$text.get("seq.validate.error5")'+'['+seqArr[i]+']');
		   		return true;
			}
	 	}
 	}
  	return false;
}
var No = 0;

function n(obj){
	return document.getElementById(obj);
}

function addStateRow(){
	var my_body = n("my_body");
	var rowInd = Number(my_body.rows.length);
	var stateRow = my_body.insertRow(rowInd);
	stateRow.style.background = "#FFFFEE";
	stateRow.insertCell().innerHTML = "&nbsp;";
	stateRow.insertCell().innerHTML = "&nbsp;";
	stateRow.insertCell().innerHTML = "&nbsp;";
	addTr("");
}

function addTr(seqVal){
	var my_body = n("my_body");
	var rowNo = my_body.rows.length ;
	var my_row = my_body.insertRow(rowNo);
	
	//var del = my_row.insertCell();
	//var seqCell = my_row.insertCell();
	//var serial = my_row.insertCell();
	var serial = document.createElement("td");
	var seqCell = document.createElement("td");
	var del = document.createElement("td");
	
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
	seqCell.innerHTML="<input type='text' name='seq"+No+"' style='width:270px;' onfocus='addRowByfocus(this)' id='seq"+No+"' value='"+seqVal+"' onKeyDown='if(event.keyCode ==13){event.keyCode=9}'>";
	seqCell.id="seqCell"+No;
	del.innerHTML = "<img src='/$globals.getStylePath()/images/del.gif' onclick='delRow(this)' />";
	No++;
	my_row.appendChild(serial);
	my_row.appendChild(seqCell);
	my_row.appendChild(del);
}

function delRow(obj){
	var tr = obj.parentNode.parentNode;
	var start = Number(tr.id) + 1;
	n("my_body").deleteRow(tr.rowIndex-1);
	for(;;start++){
		if(n(start) == null){
			break;
		}
		n(start).id = start -1;
		n("serial"+start).innerHTML = start;
		n("serial"+start).id = "serial"+(start -1);
		n("seqCell"+start).innerHTML ="<input type='text' style='width:270px;' onfocus='addRowByfocus(this)' name='seq"+(start-1)+"' id='seq"+(start-1)+"' value='"+n("seq"+start).value+"' onKeyDown='if(event.keyCode ==13){event.keyCode=9}'/>";
		n("seqCell"+start).id = "seqCell" + (start-1);
	}
	No--;
}

function addRowByfocus(obj){
	var tr = obj.parentNode.parentNode;
	if(n("my_body").rows.length == tr.rowIndex ){
		addTr("");
	}
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
	float:right;
}

.list_seq {
	background:#F5F5F5; 
	width:750px; 
	padding-top:5px;
	overflow-y:auto
}
.list_table {
	float:left; 
	width:350px;
	height:270px; 
	overflow-y:auto;
}

.list_table2 {
	float:left; 
	width:250px;
	margin-left:35px; 
	overflow:hidden
}



.seq_select {
	width:250px;
	float:right;
	height:auto;
}
.seq_select li{
	float:left;
	width:290px;
	height:27px;
	line-height:180%;
}

.m20 {
	margin-left:8px;
}
.seq_select li span{
	text-align:right;
	float:left;
	line-height:28px;
}
.seq_select li.m20 span{
	width:80px;
}
.seq_select input{
	border:0;
	width: 150px;
	border-bottom: 1px solid #8FB8E5;	
	text-align:left;
	height:20px;
	float:left;
}
</style>
</head>
<!--showtable('edittable'); showStatus();initTableList(edittable,editgriddata,0);setCol();iniData()-->
<!--onKeyDown="if(event.keyCode ==13){form.before.focus();closeSubmit();}"-->
<body onLoad="iniData()" scroll="no">

<form  method="post" scope="request" name="form"  action="/UserFunctionAction.do" >
 <input type="hidden" name="operation" value="$globals.getOP("OP_POPUP_SELECT")">
 <input type="hidden" id="tableName" name="tableName" value="$!tableName">
 <input type="hidden" id="fieldName" name="fieldName" value="$!seqFname">
 <input type="hidden" id="parentCode" name="parentCode" value="$!parentCode">
 <input type="hidden" id="selectName" name="selectName" value="$!selectName">
 <input type="hidden" name="MOID" value="$!MOID">
 <input type="hidden" name="MOOP" value="$!MOOP">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("pop.title.sequence")</div>
	<ul class="HeadingButton_seq">
	    <li><button type="button"  onClick="resetSeq()">$text.get("login.lb.logout")</button></li>
		<li><button type="button"  onClick="closeSubmit()">$text.get("common.lb.save")</button></li>
		<li><button type="button"  onClick="clearSeq()">$text.get("common.lb.clear")</button></li>
        <li><button type="button" onClick="createSeq()">$text.get("button.lable.createseq")</button></li>	
	</ul>
</div>
<div class="listRange_Pop-up">
<div class="list_seq">
<div class="list_table">
		
			<table border="0" cellpadding="0" cellspacing="0" class="selectlist" name="edittable" id="edittable">
				<THead>
					<tr class="listhead">
						<td class="listheade" width="30" align="center">
							<span style="vertical-align:middle;"><IMG
									src="/$globals.getStylePath()/images/down.jpg" border=0 />
							</span>
						</td>
						<td width="250" align="center">
							$text.get("pop.title.sequence1")
						</td>
						<td width="30" align="center">
							<img src="/$globals.getStylePath()/images/add.gif" onClick="addTr('')"/>
						</td>
					</tr>
				</THead>
				<TBody id="my_body" align="center">
					
				</TBody>
			</table>

		
			
</div>
<div class="list_table2">
<div class="seq_select">
	<li><input name="choice" type="radio" id="clist" style="border:0px; width:15px;" value="list" checked="checked"><span><label for="clist" >$text.get("addseq.seq.compact")</label>：<select name="separator" id="separator"><option value="enter">$text.get("separator.select.enter")</option><option value="comma">$text.get("separator.selelct.comma")</option></select></span></li>
<textarea style="width:250px;" rows="8" id="seqList" name="seqList"></textarea>
</div>

	<div class="seq_select ">
			<li><input name="choice" type="radio"  id="cauto" style="border:0px; width:15px;" value="auto" onKeyDown="if(event.keyCode ==13){event.keyCode=9}"><span><label for="cauto" >$text.get("addseq.seq.increate")</label></span></li>
			<li class="m20"><span>$text.get("pop.lb.before")：</span><input name="before" type="text" id="before" onKeyDown="if(event.keyCode ==13){event.keyCode=9}"></li>
			<li class="m20"><span>$text.get("pop.lb.end")：</span><input name="end" type="text" id="end" onKeyDown="if(event.keyCode ==13){event.keyCode=9}"></li>
			<li class="m20"><span>$text.get("pop.lb.startnum")：</span><input name="startnum" type="text" id="startnum" onKeyDown="if(event.keyCode ==13){event.keyCode=9}"></li>
			<li class="m20"><span>$text.get("pop.lb.inputcount")：</span><input name="inputcount" type="text" id="inputcount" onKeyDown="if(event.keyCode ==13){event.keyCode=9}"></li>
			
	</div>

</div>
</div>
</div>
</form>
</body>
</html>
