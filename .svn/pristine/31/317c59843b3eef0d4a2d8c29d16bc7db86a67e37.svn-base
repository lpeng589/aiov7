
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>凭证模板</title>
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/Voucher.css"/>
<script type="text/javascript" src="/js/gen/formvalidate.vjs_zh_CN.js?1382925173331"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>

<script type="text/javascript">
	if("true" == "$!request.getParameter("refresh")"){
		parent.location.reload();
	}
	var amtstr; //本币金额数据
	var curamtstr; //外币金额数据
	var hsstr; //核算项目字段
	var kmstr; //科目来源
	var zaiyaostr; //摘要
	$(document).ready(function() {
		 //添加
		 $("#addrow").bind("click", function(){
		 	if($("#copyTrHide").clone(true).length>0){
		 		if_tr = $("#copyTrHide").clone(true);
		 	}
		 	addRow('add');
		 });
		 $("#tableItem tr").bind("mouseover",function(){
		 	$(this).css("background","#f9f9f9");
		 });
		 $("#tableItem tr").bind("mouseout",function(){
		 	$(this).css("background","#fff");
		 });
		 $("#save").bind("click",function(){
		 	checkForm('');
		 });
		 $("select[name='accCodeSource']").change(function(){
		 	bdiv = $(this).parents("tr").find("td > div");
		  	if($(this).val()==""){
		  	    bdiv.find("input").show();
		  	    bdiv.find("span").show();
		  	    bdiv.find("input").val("");
		  	    bdiv.css("background-color","");
		  	}else{
		  		bdiv.find("input").hide();
		  		bdiv.find("span").hide();
		  		bdiv.find("input").val($(this).val());
		  		bdiv.css("background-color","rgb(223, 180, 134)");
		  	}
		 });
		 
		 jQuery.get("/CertTemplateAction.do?operation=4&type=getFieldList&tableName="+$("#tableName").val(),function(data){
		  	  ss = data.split("|");
		  	  amtstr = ss[0]; //金额字段
		  	  curamtstr = ss[1]; //金额字段
		  	  hsstr = ss[2]; //核算项目字段
		  	  kmstr = ss[3]; //科目来源
		  	  zaiyaostr = ss[4]; //摘要
		  	  
		  	  setField();
		 });
	});
	
	function tableNamechange(obj){
		opt = $("#tableName").find("option:selected");
		$("#tempName").val(opt.text());
		$("#tempNumber").val(opt.val());
		
		jQuery.get("/CertTemplateAction.do?operation=4&type=getFieldList&tableName="+$("#tableName").val(),function(data){
		  	  ss = data.split("|");
		  	  amtstr = ss[0]; //金额字段
		  	  curamtstr = ss[1]; //金额字段
		  	  hsstr = ss[2]; //核算项目字段
		  	  kmstr = ss[3]; //科目来源
		  	  zaiyaostr = ss[4]; //摘要
		  	  
		  	  setField();
		 });
	}
	
	//提交
	function checkForm(types){		
		form.submit();
	}	
	var curTr;
	function trclick(tr){
		curTr = tr; //记录当前光标所在行	
	}
	function addRow(strtype){
		$(curTr).after(curTr.outerHTML);
		$("select").each(function(){
			$(this).attr("val",$(this).val());
		});
		
		
		setField();
		
		$("select[name='accCodeSource']").change(function(){
		 	bdiv = $(this).parents("tr").find("td > div");
		  	if($(this).val()==""){
		  	    bdiv.find("input").show();
		  	    bdiv.find("span").show();
		  	    bdiv.find("input").val("");
		  	    bdiv.css("background-color","");
		  	}else{
		  		bdiv.find("input").hide();
		  		bdiv.find("span").hide();
		  		bdiv.find("input").val($(this).val());
		  		bdiv.css("background-color","rgb(223, 180, 134)");
		  	}
		 });
	}
	
	function addRowline(){
		if($("#copyTrHide").clone(true).length>0){
	 		if_tr = $("#copyTrHide").clone(true);
	 	}
	 	addRow('add');
	}
	
	//上移单元格  
	function upline(){
		if(curTr == null || typeof(curTr) == "undefined"){
			alert("请选择需要上移的字段");
			return;
		}
		
		$(curTr).insertBefore($(curTr).prev());
		curRowNo = -1;
	}
	//下移单元格  
	function downline(){
		if(curTr == null || typeof(curTr) == "undefined"){
			alert("请选择需要下移的字段");
			return;
		}
		$(curTr).insertAfter($(curTr).next());
		curRowNo = -1;
	}

	

	function setField(){	

		  //科目来源字段
		  objs = document.getElementsByName("accCodeSource"); 
	  	  for(i=0;i<objs.length;i++){
	  	  	objs[i].options.length = 0;   
	  	  	objs[i].add(new Option('指定科目','')); 
	  	  	amtfs = kmstr.split(";");
	  	  	for(j = 0 ;j<amtfs.length;j++){
	  	  		if(amtfs[j] != "")
		  			objs[i].add(new Option(amtfs[j].split(":")[1],amtfs[j].split(":")[0])); 
		  	}
		  }
		  
		  objs = document.getElementsByName("companyCode"); 
	  	  for(i=0;i<objs.length;i++){
	  	  	objs[i].options.length = 0;   
	  	  	objs[i].add(new Option('','')); 
	  	  	amtfs = hsstr.split(";");
	  	  	for(j = 0 ;j<amtfs.length;j++){
	  	  		if(amtfs[j] != "")
		  			objs[i].add(new Option(amtfs[j].split(":")[1],amtfs[j].split(":")[0])); 
		  	}
		  }
		  
		  objs = document.getElementsByName("departmentCode"); 
	  	  for(i=0;i<objs.length;i++){
	  	  	objs[i].options.length = 0; 
	  	  	objs[i].add(new Option('',''));  
	  	  	amtfs = hsstr.split(";");
	  	  	for(j = 0 ;j<amtfs.length;j++){
	  	  		if(amtfs[j] != "")
		  			objs[i].add(new Option(amtfs[j].split(":")[1],amtfs[j].split(":")[0])); 
		  	}
		  }
		  
		  objs = document.getElementsByName("employeeID"); 
	  	  for(i=0;i<objs.length;i++){
	  	  	objs[i].options.length = 0;  
	  	  	objs[i].add(new Option('','')); 
	  	  	amtfs = hsstr.split(";");
	  	  	for(j = 0 ;j<amtfs.length;j++){
	  	  		if(amtfs[j] != "")
		  			objs[i].add(new Option(amtfs[j].split(":")[1],amtfs[j].split(":")[0])); 
		  	}
		  }
		  
		  objs = document.getElementsByName("stockCode"); 
	  	  for(i=0;i<objs.length;i++){
	  	  	objs[i].options.length = 0;  
	  	  	objs[i].add(new Option('','')); 
	  	  	amtfs = hsstr.split(";");
	  	  	for(j = 0 ;j<amtfs.length;j++){
	  	  		if(amtfs[j] != "")
		  			objs[i].add(new Option(amtfs[j].split(":")[1],amtfs[j].split(":")[0])); 
		  	}
		  }
		  
		  objs = document.getElementsByName("goodsCode"); 
	  	  for(i=0;i<objs.length;i++){
	  	  	objs[i].options.length = 0;  
	  	  	objs[i].add(new Option('','')); 
	  	  	amtfs = hsstr.split(";");
	  	  	for(j = 0 ;j<amtfs.length;j++){
	  	  		if(amtfs[j] != "")
		  			objs[i].add(new Option(amtfs[j].split(":")[1],amtfs[j].split(":")[0])); 
		  	}
		  }
		  
		  objs = document.getElementsByName("currency"); 
	  	  for(i=0;i<objs.length;i++){
	  	  	objs[i].options.length = 0;  
	  	  	objs[i].add(new Option('','')); 
	  	  	amtfs = hsstr.split(";");
	  	  	for(j = 0 ;j<amtfs.length;j++){
	  	  		if(amtfs[j] != "")
		  			objs[i].add(new Option(amtfs[j].split(":")[1],amtfs[j].split(":")[0])); 
		  	}
		  }
		  objs = document.getElementsByName("currencyRate"); 
	  	  for(i=0;i<objs.length;i++){
	  	  	objs[i].options.length = 0;  
	  	  	objs[i].add(new Option('','')); 
	  	  	amtfs = hsstr.split(";");
	  	  	for(j = 0 ;j<amtfs.length;j++){
	  	  		if(amtfs[j] != "")
		  			objs[i].add(new Option(amtfs[j].split(":")[1],amtfs[j].split(":")[0])); 
		  	}
		  }
		  
		  objs = document.getElementsByName("projectCode"); 
	  	  for(i=0;i<objs.length;i++){
	  	  	objs[i].options.length = 0;  
	  	  	objs[i].add(new Option('','')); 
	  	  	amtfs = hsstr.split(";");
	  	  	for(j = 0 ;j<amtfs.length;j++){
	  	  		if(amtfs[j] != "")
		  			objs[i].add(new Option(amtfs[j].split(":")[1],amtfs[j].split(":")[0])); 
		  	}
		  }
		  
		  //设置每个字段的初始值

		  objs = document.getElementsByTagName("select"); 
		  for(i=0;i<objs.length;i++){  
		  	val = objs[i].getAttribute("val");
		  	if(val==null){
		  		continue;
		  	}
		  	if(val == ""){
		  		val = objs[i].name;	
		  		opts = objs[i].options;
			  	for(j=0;j<opts.length;j++){
			  		if(opts[j].value.toLowerCase( ).substring(opts[j].value.indexOf(".")+1)==val.toLowerCase( )){
			  			//opts[j].selected = true;
			  		}
			  	}	  		
		  	}else{
		  		opts = objs[i].options;
			  	for(j=0;j<opts.length;j++){
			  		if(opts[j].value.toLowerCase( )==val.toLowerCase( )){
			  			opts[j].selected = true;
			  		}
			  	}
		  	}
		  }
		  
		  //设置科目来源字段
		  objs = $("select[name='accCodeSource']").each(function(){
		  //style="background-color: rgb(223, 180, 134)"
		  	bdiv = $(this).parents("tr").find("td > div");
		  	if($(this).val()==""){
		  	    bdiv.find("input").show();
		  	    bdiv.find("span").show();
		  	    bdiv.css("background-color","");
		  	}else{
		  		bdiv.find("input").hide();
		  		bdiv.find("span").hide();
		  		bdiv.css("background-color","rgb(223, 180, 134)");
		  	}
		  }); 	
	}

var zaiyaostr='';	
function dbclickcomment(obj){
	titles="摘要编辑";		
	asyncbox.open({
	id : 'Popdiv',
	 	title : titles,
	　　　html : '<select size=5 id="zaiyaolist" style="width:435px" ondblclick="clickzaiyao()">'+zaiyaostr+'</select><br/><textarea id="zaiyao" rows=8 cols=70>'+obj.value+'</textarea>',
	　　　width : 450,
	　　　height : 320,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。

　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。

				obj.value=$("#zaiyao").val();
　　　　　	}
　　　	}
　	});
}	
function clickzaiyao(){	
	opt = $("#zaiyaolist").find("option:selected").text();
	
	insertTextArea($("#zaiyao")[0],opt);
}
var curAcccode;	
function dbclickaccCode(obj){
	curAcccode=obj;
	popname="defineClass";
	condition="tblAccTypeInfo";
	var urls = "/Accredit.do?popname=" + popname + "&inputType=radio&condition="+condition;
	titles="请选择会计科目";
		
	asyncbox.open({
	id : 'Popdiv',
	 title : titles,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。

　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。

				var accs = opener.strData;
				fillData(accs);
　　　　　	}
　　　	}
　	});
}

//金额公式
function dbclickFieldName(obj){
	titles="金额公式编辑";		
	asyncbox.open({
	id : 'Popdiv',
	 	title : titles,
	　　　html : '<select size=5 id="amountlist" style="width:435px" ondblclick="clickamount()">'+amtstr+'</select>'+
					'<input type=button value="+" style="width:25px" onclick="clickss(\'+\')"/><input type=button value="-"  style="width:25px" onclick="clickss(\'-\')"/><input type=button value="*"  style="width:25px" onclick="clickss(\'*\')"/><input type=button value="/"  style="width:25px" onclick="clickss(\'/\')"/><input type=button value="case"  style="width:35px" onclick="clickss(\'case\')"/><input type=button value="清空"  style="width:35px" onclick="clickss(\'R\')"/><br/><textarea  id="amount" rows=8 cols=70>'+obj.value+'</textarea>',
	　　　width : 450,
	　　　height : 320,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。

　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。

				obj.value=$("#amount").val();
　　　　　	}
　　　	}
　	});
}
function clickamount(){	
	opt = $("#amountlist").find("option:selected").text();
	insertTextArea($("#amount")[0],opt);
}
function clickss(str){	
	if(str=="R"){
		$("#amount").val('');
	}else if(str=="case"){
		insertTextArea($("#amount")[0],'case when 条件 then 条件为真代码 else 条件为否代码 end');
	}else{ 
		insertTextArea($("#amount")[0],str);
	}
}

//外币金额公式
function dbclickCurFieldName(obj){
	titles="外币金额公式编辑";		
	asyncbox.open({
	id : 'Popdiv',
	 	title : titles,
	　　　html : '<select size=5 id="amountCurlist" style="width:435px" ondblclick="clickCuramount()">'+curamtstr+'</select>'+
					'<input type=button value="+" style="width:25px" onclick="clickCurss(\'+\')"/><input type=button value="-"  style="width:25px" onclick="clickCurss(\'-\')"/><input type=button value="*"  style="width:25px" onclick="clickCurss(\'*\')"/><input type=button value="/"  style="width:25px" onclick="clickCurss(\'/\')"/><input type=button value="case"  style="width:35px" onclick="clickCurss(\'case\')"/><input type=button value="清空"  style="width:35px" onclick="clickCurss(\'R\')"/><br/><textarea id="amountCur" rows=8 cols=70>'+obj.value+'</textarea>',
	　　　width : 450,
	　　　height : 320,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。

　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。

				obj.value=$("#amountCur").val();
　　　　　	}
　　　	}
　	});
}
function clickCuramount(){	
	opt = $("#amountCurlist").find("option:selected").text();
	insertTextArea($("#amountCur")[0],opt);
}
function clickCurss(str){	
	if(str=="R"){
		$("#amountCur").val('');
	}else if(str=="case"){
		insertTextArea($("#amountCur")[0],'case when 条件 then 条件为真代码 else 条件为否代码 end');
	}else{ 
		insertTextArea($("#amountCur")[0],str);
	}
}

function insertTextArea(obj,str){
	 selection = document.selection;
	 checkFocus(obj);
	 if(typeof(obj.selectionStart)!='undefined') {
	 	obj.value = obj.value.substr(0, obj.selectionStart) + str + obj.value.substr(obj.selectionEnd);
	 } else if(selection && selection.createRange) {
	 	var sel = selection.createRange();
	  	sel.text = str;
	  	sel.moveStart('character', -str.length);
	 } else {
	  	obj.value += str;
	 }
}
 
function checkFocus(obj) {
 	obj.focus();
}



//科目弹出窗回调填值

function fillData(data){
	if(data != null && data != ""){
		if(data.split(";")[0].length<10){
			alert("不能选择科目类别");
			return;
		}
		curAcccode.value = data.split(";")[2];
		if(jQuery.exist('Popdiv')){
				jQuery.close('Popdiv'); 
		}
	}
}

function save(){	 
	document.form.submit();
}
function delimg(obj){
	if(confirm("确定要删除这一行吗?")==0){
		return;
	}
	$(obj).parents("tr").remove();
}

	
</script>
<style type="text/css">

.f-icon16{background-image:url(/style/v7/images/function_icon22.png);background-repeat:no-repeat;}
.detbtBar{float:left;margin: 5px 0px 3px 10px;/*border:1px solid #c2c2c2; */border-bottom: none;width:154px;margin: 5px 0px;} 
.detbtBar>li{width:22px;height:22px;cursor:pointer;margin:1px 2px 2px;float:left}
#b_addline{background-position:0 -66px;width:22px;min-width:22px}
#b_addline:hover{background-position:-22px -66px;}
#b_delline{background-position:0 -88px;width:22px;min-width:22px}
#b_delline:hover{background-position:-22px -88px;}
#b_upline{background-position:0 -153px;width:22px;min-width:22px}
#b_upline:hover{background-position:-22px -153px;}
#b_downline{background-position:0 -175px;width:22px;min-width:22px}
#b_downline:hover{background-position:-22px -175px;}

</style>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body style="background: #f5f5f5;">
<form id="form" name="form" method="post" action="/CertTemplateAction.do" target="formFrame">
#if("$!result.id" == "")
<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")"/>
#else
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
#end
<input type="hidden" id="winCurIndex" name="winCurIndex" value="4"/>
<input type="hidden" id="id" name="id" value="$!result.id"/>
<div class="div1" style="padding-top: 10px;">
<div align="center" style="padding-bottom:20px;"><span style="font-size:25px;">凭证模板</span></div>

<div style="padding-left:10px;text-align:left">
单据名称：<select name="tableName" id="tableName" onchange="tableNamechange(this);">
		#foreach($trow in $!tableList)
			<option value="$trow.value" #if("$trow.value"=="$!result.tableName") selected #end>$trow.name</option>
		#end
		</select>
模板编号：<input name="tempNumber" id="tempNumber" value="$!result.tempNumber"  style="width: 150px;"/>
模板名称：<input name="tempName" id="tempName" value="$!result.tempName"  style="width: 120px;"/>
凭证字：<select name="credTypeID" id="credTypeID">
	#foreach($er in $globals.getEnumerationItems("CredTypeID"))
		<option  value="$er.value" #if("$er.value" == "$!result.credTypeID") selected #end >$er.name</option>
	#end				
	  </select>
	  弹出窗名称：<input name="popupName" id="popupName" value="$!result.popupName"  style="width: 120px;"/>
	  模板类型：<select name="type" id="type">
		<option  value="0" #if("0" == "$!result.type") selected #end >单据模板</option>
		<option  value="1" #if("1" == "$!result.type") selected #end >月结模板</option>
	  </select>
</div>
</div>

<div id="all_content" style="overflow-x:hidden;overflow-y:auto;margin-top: 10px;width:100%;">

<div style="width:99%;" class="ABK_Add c_main winw1200 f_l mains">
<div style="padding-left: 10px;"></div>
<div class="tablediv" style="overflow:auto;" id="heart_id">
<script type="text/javascript"> 
	var oDiv=document.getElementById("heart_id");
	var sHeight=document.documentElement.clientHeight-150;
	var sWidth=document.documentElement.clientWidth;
	oDiv.style.height=sHeight+"px";
	oDiv.style.width=sWidth+"px";
</script>
<span style="float:left; width:100%;font:bold 14px;padding-left: 12px;height: 30px;">
	<ul class="detbtBar">
		<li id="b_addline" class="f-icon16" onclick="addRowline()" title="添加字段"></li>
		<li id="b_upline" class="f-icon16" onclick="upline()" title="上移"></li>
		<li id="b_downline" class="f-icon16" onclick="downline()" title="下移"></li>					
	</ul>
</span>
<table  height="30px" style="width:2450px" border="1" id="tableItem">
  <tr id="titles" name="titles">
    <td width="30"><div class="alignDiv">
    <img src="/style1/images/workflow/Add.gif" title="插入行" class="img1" id="addrow"/>
    </div></td>
    <td width="200"><div class="alignDiv">摘要...</div></td>
    <td width="50"><div class="alignDiv">借贷方向</div></td>
    <td width="200"><div class="alignDiv">科目来源...</div></td>   
    <td width="100"><div class="alignDiv">科目</div></td> 
    <td width="300"><div class="alignDiv">金额公式</div></td>
    <td width="300"><div class="alignDiv">外币金额公式</div></td>
    <td width="200"><div class="alignDiv">外币字段</div></td>
    <td width="200"><div class="alignDiv">外币汇率字段</div></td>
    <td width="200"><div class="alignDiv">往来字段</div></td>
    <td width="200"><div class="alignDiv">部门字段</div></td>
    <td width="200"><div class="alignDiv">职员字段</div></td>
    <td width="200"><div class="alignDiv">仓库字段</div></td>
    <td width="200"><div class="alignDiv">商品字段</div></td>
    <td width="200"><div class="alignDiv">项目字段</div></td>
  </tr>
  #foreach($row in $!result.tempList)
  	<tr id="copyTr" onclick="trclick(this)">
	    <td style="text-align: center;">
	    <input type="hidden" name="keyId" value="$row.id"/>
	    <img src="/style/plan/del_icon.gif" title="删除行" class="img1" onClick="delimg(this)"/></td>
	    <td><input style="width:185px" name="comment" value="$!row.comment" ondblclick="dbclickcomment(this);"><span >...</span></td>	    
	    <td><select name="dirc" style="width: 100%" >
	    	<option value="1" #if("$!row.dirc" == "1") selected #end>借</option>
	    	<option value="2" #if("$!row.dirc" == "2") selected #end>贷</option>
	    </select></td>
	    <td><select style="width:195px" name="accCodeSource" val="$!row.accCode" ></select></td>
	    <td ><div style="width:100%;height:20px;">
	    <input style="width:90px" name="accCode" value="$!row.accCode" ondblclick="dbclickaccCode(this);"/><span >...</span></div></td>
	    <td><input style="width:290px" name="fieldName" value="$!row.fieldName" ondblclick="dbclickFieldName(this)"/><span >...</span></td>
	    <td><input style="width:290px" name="curFieldName" value="$!row.curFieldName" ondblclick="dbclickCurFieldName(this)"/><span >...</span></td>
	    <td><select style="width:195px" name="currency" val="$!row.currency" ></select></td>
	    <td><select style="width:195px" name="currencyRate" val="$!row.currencyRate" ></select></td>
	    <td><select style="width:195px" name="companyCode" val="$!row.companyCode" ></select></td>
	    <td><select style="width:195px" name="departmentCode" val="$!row.departmentCode" ></select></td>
	    <td><select style="width:195px" name="employeeID" val="$!row.employeeID" ></select></td>
	    <td><select style="width:195px" name="stockCode" val="$!row.stockCode" ></select></td>
	    <td><select style="width:195px" name="goodsCode" val="$!row.goodsCode" ></select></td>	    
	    <td><select style="width:195px" name="projectCode" val="$!row.projectCode" ></select></td>
  	</tr>  
  #end	
  #if("$!result.tempList"=="")
  	<tr id="copyTr" onclick="trclick(this)">
	    <td style="text-align: center;">
	    <input type="hidden" name="keyId" value="$row.id"/>
	    <img src="/style/plan/del_icon.gif" title="删除行" class="img1" onClick="delimg(this,'keyId')"/></td>
	    <td><input style="width:185px" name="comment" value="$!row.comment" ondblclick="dbclickcomment(this);"><span >...</span></td>	    
	    <td><select name="dirc" style="width: 100%" >
	    	<option value="1" #if("$!row.dirc" == "1") selected #end>借</option>
	    	<option value="2" #if("$!row.dirc" == "2") selected #end>贷</option>
	    </select></td>
	    <td><select style="width:195px" name="accCodeSource" val="$!row.accCode" ></select></td>
	    <td ><div style="width:100%;height:20px;">
	    <input style="width:90px" name="accCode" value="$!row.accCode" ondblclick="dbclickaccCode(this);"/><span >...</span></div></td>
	    <td><input style="width:290px" name="fieldName" value="$!row.fieldName" ondblclick="dbclickFieldName(this)"/><span >...</span></td>
	    <td><input style="width:290px" name="curFieldName" value="$!row.curFieldName" ondblclick="dbclickCurFieldName(this)"/><span >...</span></td>
	    <td><select style="width:195px" name="currency" val="$!row.currency" ></select></td>
	    <td><select style="width:195px" name="currencyRate" val="$!row.currencyRate" ></select></td>
	    <td><select style="width:195px" name="companyCode" val="$!row.companyCode" ></select></td>
	    <td><select style="width:195px" name="departmentCode" val="$!row.departmentCode" ></select></td>
	    <td><select style="width:195px" name="employeeID" val="$!row.employeeID" ></select></td>
	    <td><select style="width:195px" name="stockCode" val="$!row.stockCode" ></select></td>
	    <td><select style="width:195px" name="goodsCode" val="$!row.goodsCode" ></select></td>	    
	    <td><select style="width:195px" name="projectCode" val="$!row.projectCode" ></select></td>
  	</tr>  
  #end
</table>
</div>
</div>
</form>
</body>
</html>