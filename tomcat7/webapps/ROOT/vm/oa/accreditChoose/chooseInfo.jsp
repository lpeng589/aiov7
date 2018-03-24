<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>group</title>
<link rel="stylesheet" type="text/css" href="/$globals.getStylePath()/css/ListRange.css"  />
<link rel="stylesheet" type="text/css" href="/style1/css/oa_news.css" />
<link rel="stylesheet" type="text/css" href="/style1/css/sharingStyle.css" />
<link rel="stylesheet" type="text/css" href="/style/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css" />

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
//职员-全选按钮  
#if("$!AccreditSearchForm.keyType" == "choose") 
var isAllSelectSelected = true;
#else
var isAllSelectSelected = false;
#end
function checkKeyId(name){

	items = document.getElementsByName(name);
	for(var i=0;i<items.length;i++){
	    if(!items[i].disabled){
	       if(!isAllSelectSelected){
	       		window.parent.chooseData += items[i].value+",";
	       		window.parent.strData += items[i].value+";";
	       		if(form.popname.value != "clientGroup"){
	       			if(form.popname.value == "CrmClickGroup"){
	       				window.parent.strData += $("#name_"+items[i].value).attr("title");
	       			}else{
	       				if(form.popname.value == "userGroup" || form.popname.value == "leaveGroup" || form.popname.value == "onLineGroup" || form.popname.value == "staffGroup" || form.popname.value == "jobGroup" || form.popname.value == "defineClass"){
							//回天到已选数据





							var backDatas = items[i].value+";"+document.getElementById("name_"+items[i].value).innerHTML+";";	
							var codes = document.getElementById("code_"+items[i].value);
		       			 	if(codes != null && typeof(codes) !='undefined'){
		       			 		backDatas +=";"+codes.innerHTML;
		       			 	}	
		       			 	backDatas += ";|";
		       			 			
		       			 	if(window.parent.strData.indexOf(backDatas)==-1){
		       			 		var li = '<li id="choose_'+items[i].value+'" bg="'+backDatas+'">'+jQuery("#name_"+items[i].value).attr('title')+'<b class="icons b-del" onclick="delChooseId(\''+items[i].value+'\');"></b></li>';
								window.parent.jQuery(".d-sel-wp>.sel-ul").append(li);
		       			 	}		
		       			 	//回填已选数据		       			 	
		       			 	if(window.parent.chooseData.indexOf(items[i].value)==-1){		       			 		
		       			 		window.parent.chooseData +=items[i].value+",";
		       			 	}			   			 				   			 	
						}
		       			window.parent.strData += document.getElementById("name_"+items[i].value).innerHTML+";";
	       			 	code = document.getElementById("code_"+items[i].value);
	       			 	if(code != null && typeof(code) !='undefined'){
	       			 		window.parent.strData +=";"+code.innerHTML;
	       			 	}
		       			//window.parent.strData += document.getElementById("name_"+items[i].value).innerHTML;
		       		}
	       		}
	       		if(form.popname.value == "communicationGroup"){
	       			window.parent.strData += document.getElementById("name_"+items[i].value).innerHTML+";"+document.getElementById("email_"+items[i].value).innerHTML;
	       		}
	       		if(form.popname.value == "clientGroup"){
	       			window.parent.strData +=document.getElementById("name_"+items[i].value).value+";"+document.getElementById("email_"+items[i].value).value;
	       		}
	       		window.parent.strData += ";|";
	     	}else{
	     		window.parent.strData="";
				window.parent.chooseData="";
				window.parent.jQuery('.d-sel-wp>.sel-ul>li').remove();    		
			    delStringuser(items[i].value);
	     		var message = items[i].value+";";
	     		if(form.popname.value != "clientGroup"){
	     			if(form.popname.value == "CrmClickGroup"){
	     				message += $("#name_"+items[i].value).attr("title")+";";
	       			}else{
	       				message += document.getElementById("name_"+items[i].value).innerHTML+";";
	       			}
	       		}
	     		if(form.popname.value == "communicationGroup"){
	       			message += document.getElementById("name_"+items[i].value).innerHTML+";"+document.getElementById("email_"+items[i].value).innerHTML+";";
	       		}
	       		if(form.popname.value == "clientGroup"){
	       			message += document.getElementById("name_"+items[i].value).value+";"+document.getElementById("email_"+items[i].value).value+";";
	       		}
	     		delstrData(message);
	     	}
	      
	       items[i].checked = !isAllSelectSelected;
	    }
	}
	delitera();
 	isAllSelectSelected = !isAllSelectSelected;
}

//职员-点击单个复选框
function checkKey(name,id){	
	var namearr = document.getElementsByName(name);		
	for(var i = 0;i<namearr.length;i++){
		if(namearr[i].value==id){
			if(namearr[i].checked){																				
				window.parent.chooseData += namearr[i].value+",";
				window.parent.strData += namearr[i].value+";";
				if(form.popname.value != "clientGroup"){
					if(form.popname.value == "CrmClickGroup"){
	       				window.parent.strData += $("#name_"+namearr[i].value).attr("title");
	       			}else{
	       				if(form.popname.value == "userGroup" || form.popname.value == "leaveGroup" || form.popname.value == "onLineGroup" || form.popname.value == "staffGroup" || form.popname.value == "jobGroup" || form.popname.value == "defineClass"){
							//回填到已选数据


							var backDatas = namearr[i].value+";"+document.getElementById("name_"+namearr[i].value).innerHTML+";";	
							var codes = document.getElementById("code_"+namearr[i].value);
		       			 	if(codes != null && typeof(codes) !='undefined'){
		       			 		backDatas +=";"+codes.innerHTML;
		       			 	}	
		       			 	backDatas += ";|";
		       			 			
		       			 	if(window.parent.strData.indexOf(backDatas)==-1){
		       			 		var li = '<li id="choose_'+id+'" bg="'+backDatas+'">'+jQuery("#name_"+id).attr('title')+'<b class="icons b-del" onclick="delChooseId(\''+id+'\');"></b></li>';
								window.parent.jQuery(".d-sel-wp>.sel-ul").append(li);
		       			 	}		
		       			 	//回填已选数据		       			 	
		       			 	if(window.parent.chooseData.indexOf(namearr[i].value)==-1){		       			 		
		       			 		window.parent.chooseData +=namearr[i].value+",";
		       			 	}			   			 				   			 	
						}
		       			window.parent.strData += document.getElementById("name_"+namearr[i].value).innerHTML+";";
	       			 	code = document.getElementById("code_"+namearr[i].value);
	       			 	if(code != null && typeof(code) !='undefined'){
	       			 		window.parent.strData +=";"+code.innerHTML;
	       			 	}
		       		}
	       		}
				if(form.popname.value == "communicationGroup"){
	       			window.parent.strData += document.getElementById("name_"+namearr[i].value).innerHTML+";"+document.getElementById("email_"+namearr[i].value).innerHTML;
	       		}
	       		if(form.popname.value == "clientGroup"){
	       			window.parent.strData += document.getElementById("name_"+namearr[i].value).value+";"+document.getElementById("email_"+namearr[i].value).value;
	       		}
	       		window.parent.strData += ";|";								
			}else{
								
				delStringuser(id);
				var message = namearr[i].value+";";
				if(form.popname.value != "clientGroup"){
					if(form.popname.value == "CrmClickGroup"){
		       			message += $("#name_"+namearr[i].value).attr("title")+";";
		       		}else{
		       			if(form.popname.value == "userGroup" || form.popname.value == "defineClass"){
		       				//已选数据删除


							var delBackData = window.parent.jQuery('#choose_'+id).attr('bg');
							delstrData(delBackData);			
							//window.parent.strData.replace(delBackData,'');
							window.parent.jQuery('#choose_'+id).remove();
							//删除已选数据


		       			 	if(window.parent.chooseData.indexOf(namearr[i].value)>=0){
		       			 		window.parent.chooseData=window.parent.chooseData.replace(namearr[i].value,'');
		       			 	}
		       			}
		       			message += document.getElementById("name_"+namearr[i].value).innerHTML+";";
		       		}
	       		}
	     		if(form.popname.value == "communicationGroup"){
	       			message += document.getElementById("name_"+namearr[i].value).innerHTML+";"+document.getElementById("email_"+namearr[i].value).innerHTML+";";
	       		}
	       		if(form.popname.value == "clientGroup"){
	       			message += document.getElementById("name_"+namearr[i].value).value+";"+document.getElementById("email_"+namearr[i].value).value+";";
	       		}
				delstrData(message);
			}
	    }
	}
	
	delitera();
	
}
	
//职员-点击单选按钮


function checkKeyRedio(id){
	window.parent.chooseData = id+",";
	window.parent.strData = id+";";
	if(form.popname.value == "userGroup" || form.popname.value == "leaveGroup" || form.popname.value == "onLineGroup" || form.popname.value == "staffGroup" || form.popname.value == "jobGroup"){						
	    window.parent.strData += document.getElementById("name_"+id).innerHTML+";"+document.getElementById("deptCode_"+id).value+";"+document.getElementById("deptName_"+id).innerHTML+";"+document.getElementById("title_"+id).innerHTML;
	}
	if(form.popname.value == "communicationGroup"){
		window.parent.strData += document.getElementById("name_"+id).innerHTML+";"+document.getElementById("email_"+id).innerHTML;
	}
	if(form.popname.value == "clientGroup"){
		window.parent.strData += document.getElementById("name_"+id).value+";"+document.getElementById("email_"+id).value;
	}
	if(form.popname.value == "CrmClickGroup"){
		window.parent.strData += $("#name_"+id).attr("title");
	}
	if(form.popname.value == "defineClass"){
		window.parent.strData += $("#name_"+id).attr("title")+";"+$("#code_"+id).attr("title");
	}
	window.parent.strData += ";|";
}
	
//清除重复项


function delitera(){
	var strDatas = window.parent.strData;
	if(strDatas != ""){
		var str = "";
		var datasStr = "";
		var strings = strDatas.split("|");
		for(var i = 0; i<strings.length; i++){
			var talg = false ;
			if(strings[i]!=""){
				for(var j = i+1;j<strings.length;j++){
					if(strings[i]==strings[j]){
						talg = true;
					}
				}
				if(!talg){
					datasStr = datasStr+strings[i]+"|";
				}
			}
		}
		window.parent.strData = datasStr;
	}
}
	
//移除
function delStringuser(ss){
	var strings = window.parent.chooseData.split(",");
	var str = "";
	for(var i=0;i<strings.length-1;i++){
		if(strings[i] != ss){
			str += strings[i]+",";
		}
	}
	window.parent.chooseData=str;
}
	
//移除编号和名称


function delstrData(ss){
	var strDatas = window.parent.strData;
	if(strDatas != ""){
		window.parent.strData = strDatas.replace(ss,'');
	/*	var str = "";
		var strings = strDatas.split("|");
		for(var i = 0; i<strings.length-1; i++){
			if(ss !=strings[i]){
				str += strings[i];
				if(i<strings.length-1){
					str += "|";
				}
			}
		}*/
		//window.parent.strData = str;
	}
}
	
//字母搜索
function edh(value){
	window.location="/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&condition=$!AccreditSearchForm.condition&inputType=$!AccreditSearchForm.inputType&keyType=letter&value="+value;
}
	
//选中
function selectData(){
	
	var sData = window.parent.chooseData;	
	if(sData != ""){
		var str = sData.split(",");	
		 var i = 0;
		 while(i<str.length-1){
		 	var userId = document.getElementById(str[i]);
		 	i++;
		 	if(typeof(userId)=="undefined" || userId == null){
		 		continue;
		 	}
		 	userId.checked = "true";
		 	checkKey('keyId',userId.value);
		 }
	}
}

/*单击选中数据*/
function checkTr(id){
	var checkeds = document.getElementById(id);
	if(checkeds.checked){
		checkeds.checked="";
	}else{
		checkeds.checked="true";
	}
	if("$!AccreditSearchForm.inputType" == "radio"){
		checkKeyRedio(id);
	}else{
		checkKey('keyId',id);
	}
}
	
/*双击回填数据*/	
function dbData(code, name,deptCode,deptName,title){
	var data =  code+";"+name+";"+deptCode+";"+deptName+";"+title+";|";
	var popDiv = $(".asyncbox_normal",parent.parent.document) ;
	if(popDiv.size()>1){
		var popId = popDiv.last().prev().attr("id");
		if(parent.parent.jQuery.opener(popId)==undefined){
			parent.parent.fillData(data);
		}else{
			parent.parent.jQuery.opener(popId).fillData(data);
		}
	}else if(typeof(parent.parent.fillData)!="undefined"){
		parent.parent.fillData(data);
	}else{
		if(typeof(parent.parent.jQuery("#dealdiv").attr("id"))!="undefined"){
			if(typeof(parent.parent.jQuery("#dealdivNew").attr("id"))!="undefined"){
				$("#dbData",parent.parent.jQuery.opener('dealdivNew').document).val(data);
			}else{
				$("#dbData",parent.parent.jQuery.opener('dealdiv').document).val(data);
			}
		}else{
			$("#dbData",parent.parent.document).val(data);
		}
		parent.parent.jQuery.close('Popdiv');
	}
	
}
	
</script>
<style type="text/css">
ul{border:0; margin:0; padding:0;}
#pagination-flickr{background:#CBE6FF;}
#pagination-flickr li{font-size:12px;list-style:none;overflow:hidden;}
#pagination-flickr a{color: #3397CE;float:left;text-align:center;line-height:20px;padding:0;text-decoration:none;width:20px;}
#pagination-flickr a:link,#pagination-flickr a:visited {display:inline-block;float:left;text-decoration:none;}
#pagination-flickr a:hover{/*border:solid 1px #666666;*/background:#2E85C0;color:#fff;}

.list table tr td{background:none;font-family:微软雅黑;}
.list table thead tr td{background:none;}
table.tn_table{border:1px #C8CECE solid;border-top:none;}
table.tn_table tr td span{overflow:hidden;white-space:nowrap;text-overflow:ellipsis;float:left;}
.listRange_pagebar{width:auto;display:inline-block;padding:0;}
.listRange_pagebar div{line-height:25px;}
.l-person{width:400px;padding:0 0 5px 0;}
.l-p-head{overflow:hidden;border-bottom:1px solid #C8CECE;border-right:1px solid #C8CECE;background:#eee;background-image:-webkit-linear-gradient(top,#f9f9f9,#f2f2f2);background-image:linear-gradient(top,#f9f9f9,#f2f2f2);}
.f-u-list{height:230px;overflow:hidden;overflow-y:auto;border-right:1px solid #C8CECE;}
.f-u-list>li{overflow:hidden;border-bottom:1px #ddd dashed;}
.f-u-list>li:hover{background:#f2f2f2;}
.lf-cbox{float:left;margin:8px 0 0 5px;}
</style>
<script type="text/javascript">
//控制标头宽度
$(function(){
	var oThead = $(".tn_table thead");
	var oTbody = $(".tn_table tbody");
	//$(".dHeight").css("height",oThead.find("tr").height());
	oThead.css("width",oTbody.width());
	var s = oTbody.find("tr:eq(0)").find("td").length;
	if(s == 0){
		oThead.css({position:'relative'});
		$(".dHeight").remove();
	}else{
		for(var i =0;i<s;i++){
			oThead.find("tr").find("td:eq("+i+")").css("width",oTbody.find("tr:eq(0)").find("td:eq("+i+")").width());
		}
	}
});
</script>
</head>

<body #if("$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "communicationGroup" || "$!AccreditSearchForm.popname" == "clientGroup" || "$!AccreditSearchForm.popname" == "CrmClickGroup"|| "$!AccreditSearchForm.popname" == "defineClass") onload="selectData();" #end>
<iframe name="formFrame" style="display:none" ></iframe>
<form  method="post" name="form" action="AccreditAction.do">
<input type="hidden" name="operation" value="accreditInfo" />
<input type="hidden" name="condition" value="$!AccreditSearchForm.condition" />

<input type="hidden" name="popname" value="$!AccreditSearchForm.popname" />
<input type="hidden" name="inputType" value="$!AccreditSearchForm.inputType" />
		<div style="padding-left: 10px;font-family:微软雅黑;color:#B4B4B4;padding:0 0 5px 5px;">
		<strong>当前位置
		#if("$!AccreditSearchForm.popname" == "deptGroup" || "$!AccreditSearchForm.popname" == "empGroup")
			#if("$!AccreditSearchForm.keyType" == "group")
				- 目录
			#elseif("$!AccreditSearchForm.keyType" == "choose")
				- 已选数据





			#end
		#end
		#if("$!AccreditSearchForm.popname" == "userGroup")
			#if("$!AccreditSearchForm.keyType" == "choose")
				- 已选数据





			#else
				- 职员列表
			#end
		#end
		#if("$!AccreditSearchForm.popname" == "communicationGroup")
			#if("$!AccreditSearchForm.keyType" == "choose")
				- 已选数据





			#else
				- 人员列表
			#end
		#end
		#if("$!AccreditSearchForm.popname" == "clientGroup")
			#if("$!AccreditSearchForm.keyType" == "choose")
				- 已选数据





			#else
				- 客户联系人





			#end
		#end
		#if("$!AccreditSearchForm.popname" == "CrmClickGroup")
			#if("$!AccreditSearchForm.keyType" == "choose")
				- 已选数据





			#else
				- 客户
			#end
		#end
		</strong>
		</div>
		<div style="overflow:hidden;overflow-y:auto;height:280px;width:560px;" id="data_list_id">
		<script type="text/javascript"> 
		var oDiv=document.getElementById("data_list_id");
		
		#if("$!AccreditSearchForm.popname" == "deptGroup" || "$!AccreditSearchForm.popname" == "empGroup" )
		var sHeight=document.documentElement.clientHeight-30;
		#end
		#if("$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "onLineGroup" || "$!AccreditSearchForm.popname" == "staffGroup" || "$!AccreditSearchForm.popname" == "jobGroup" || "$!AccreditSearchForm.popname" == "communicationGroup" || "$!AccreditSearchForm.popname" == "clientGroup" || "$!AccreditSearchForm.popname" == "CrmClickGroup" || "$!AccreditSearchForm.popname" == "leaveGroup")
		var sHeight=document.documentElement.clientHeight-70;
		#end
		#if("$!AccreditSearchForm.popname" == "defineClass")
		var sHeight=document.documentElement.clientHeight-50;
		#end
		
		//oDiv.style.height=sHeight+"px";
		</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td valign="top" class="list">
			<table class="tn_table" cellpadding="0" cellspacing="0" align="center" style="width: 100%;box-sizing:border-box;">
				#if("$!AccreditSearchForm.popname" == "deptGroup")
				<thead class="tn_thead">
				<tr>
					<td>部门编号</td>
					<td style="text-align:left;">部门全称</td>
				</tr>
				</thead>
				<div class="dHeight" style="width:100%;">
				</div>
				#foreach($dept in $!list)
				<tr onMouseMove="setBackground(this,true);"	onMouseOut="setBackground(this,false);" #if("$!AccreditSearchForm.chooseParent"=="false" && "$!globals.getFromArray($!dept,4)"=="1") #else ondblclick="dbData('$!globals.getFromArray($!dept,3)','$!globals.getFromArray($!dept,2)')" #end>
					<td width="200"><span style="width:200px;display:inline-block;">$!globals.getFromArray($!dept,1)</span></td>
					<td width="220" style="text-align:left;"><span style="width:220px;display:inline-block;">$!globals.getFromArray($!dept,2)</span></td>
				</tr>
				#end
				#end
				
				##自定义的分类资料
				#if("$!AccreditSearchForm.popname" == "defineClass")
					#if("$!AccreditSearchForm.inputType" !="radio")
						<tr>
						<td>
							<div class="l-person">
								<div class="l-p-head">
									<input class="lf-cbox" type="checkbox" value="checkbox" id="selAll" name="selAll" #if("$!AccreditSearchForm.keyType" == "choose") checked #end onClick="checkKeyId('keyId')"/>
									<span style="width:100px;display:inline-block;">编号</span>
									<span style="width:150px;display:inline-block;">名称</span>
									<span style="width:100px;display:inline-block;">下级</span>
								</div>
								<ul class="f-u-list">
								#foreach($dept in $!list)
								<li id="tr_$!globals.getFromArray($!dept,3)" 
									ondblclick="dbData('$!globals.getFromArray($!dept,3)','$!globals.getFromArray($!dept,2)','$!globals.getFromArray($!dept,1)')">
									<input class="lf-cbox" type="checkbox" name="keyId" id="$!globals.getFromArray($!dept,3)" value="$!globals.getFromArray($!dept,3)" onclick="checkKey('keyId','$!globals.getFromArray($!dept,3)')" />
									<span onclick="checkTr('$!globals.getFromArray($!dept,3)')" style="width:100px;display:inline-block;" id="code_$!globals.getFromArray($!dept,3)" title='$!globals.getFromArray($!dept,1)'>$!globals.getFromArray($!dept,1)</span>
									<span onclick="checkTr('$!globals.getFromArray($!dept,3)')" style="width:168px;display:inline-block;" class="b_sp" id="name_$!globals.getFromArray($!dept,3)" title='$!globals.getFromArray($!dept,2)'>$!globals.getFromArray($!dept,2)</span>
									<span style="width:50px;display:inline-block;" class="b_sp">
									#if("$!globals.getFromArray($!dept,4)"=="1") <a style="color:rgb(17, 69, 163);" href="/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&keyType=group&value=$!globals.getFromArray($!dept,3)">下级</a> #end</span>
								</li>
								#end
								</ul>
							</div>
						</td>
					</tr>
					#else
					<thead class="tn_thead">
					<tr>
						<td>&nbsp;</td>
						<td style="text-align:left;">编号</td>
						<td style="text-align:left;">名称</td>
						<td style="text-align:left;">下级</td>
					</tr>
					</thead>
					<div class="dHeight" style="width:100%;">
					</div>			
						#foreach($dept in $!list)
						<tr onMouseMove="setBackground(this,true);"	onMouseOut="setBackground(this,false);" id="tr_$!globals.getFromArray($!dept,3)" 
							ondblclick="dbData('$!globals.getFromArray($!dept,3)','$!globals.getFromArray($!dept,2)','$!globals.getFromArray($!dept,1)')">
							<td width="40"><input name="keyId" id="$!globals.getFromArray($!dept,3)" type="radio" value="$!globals.getFromArray($!dept,3)" onclick="checkKeyRedio('$!globals.getFromArray($!dept,3)')" /></td>
							<td width="100" style="text-align:left;" onclick="checkTr('$!globals.getFromArray($!dept,3)')"><span style="width:100px;display:inline-block;" id="code_$!globals.getFromArray($!dept,3)" title='$!globals.getFromArray($!dept,1)'>$!globals.getFromArray($!dept,1)</span></td>
							<td width="*" style="text-align:left;" onclick="checkTr('$!globals.getFromArray($!dept,3)')"><span style="width:168px;display:inline-block;" class="b_sp" id="name_$!globals.getFromArray($!dept,3)" title='$!globals.getFromArray($!dept,2)'>$!globals.getFromArray($!dept,2)</span></td>
							<td width="50" style="text-align:left;" ><span style="width:50px;display:inline-block;" class="b_sp"  >
							#if("$!globals.getFromArray($!dept,4)"=="1") <a style="color:rgb(17, 69, 163);" href="/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&keyType=group&value=$!globals.getFromArray($!dept,3)">下级</a> #end</span></td>
						</tr>
						#end
					#end
				#end
			
				#if("$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "staffGroup" || "$!AccreditSearchForm.popname" == "jobGroup" 
				|| "$!AccreditSearchForm.popname" == "onLineGroup" || "$!AccreditSearchForm.popname" == "leaveGroup")
				#if("$!AccreditSearchForm.inputType" !="radio")
				<tr>
					<td>
						<div class="l-person">
							<div class="l-p-head">
								<input class="lf-cbox" type="checkbox" value="checkbox" id="selAll" name="selAll" #if("$!AccreditSearchForm.keyType" == "choose") checked #end onClick="checkKeyId('keyId')"/>
								<span style="width:100px;display:inline-block;">职员编号</span>
								<span style="width:100px;display:inline-block;">职员全称</span>
								<span style="width:100px;display:inline-block;">部门全称</span>
							</div>
							<ul class="f-u-list">
							#foreach($user in $!list)
								<li id="tr_$!user.id" ondblclick="dbData('$!user.id','$globals.getEmpFullNameByUserId("$!user.id")','$!user.deptId','$!user.departmentName','$!globals.getEnumerationItemsDisplay("duty","$!user.TitleID")');">
									#if("$!AccreditSearchForm.inputType" == "checkbox")<input class="lf-cbox" type="checkbox" name="keyId" id="$!user.id" value="$!user.id" onclick="checkKey('keyId','$!user.id')" />#end
									<span onclick="checkTr('$!user.id')" style="width:100px;display:inline-block;" id="code_$!user.id" title='$!user.empNumber'>$!user.empNumber</span>
									<span onclick="checkTr('$!user.id')" style="width:100px;display:inline-block;" id="name_$!user.id" title='$globals.getEmpFullNameByUserId("$!user.id")'>$globals.getEmpFullNameByUserId("$!user.id")</span>
									<input type="hidden" name="deptCode_$!user.id" id="deptCode_$!user.id" value="$!user.deptId"/>
									<span onclick="checkTr('$!user.id')" style="width:100px;display:inline-block;" class="b_sp" id="deptName_$!user.id" title='$!user.departmentName'>$!user.departmentName</span>
								</li>
							#end
							</ul>
						</div>
					</td>
				</tr>
				#else
				<tr>
					<td></td>
					<td style="text-align:left;">职员编号</td>
					<td style="text-align:left;">职员全称</td>
					<td style="text-align:left;">职位</td>
					<td style="text-align:left;">部门全称</td>
				</tr>
				<div class="dHeight" style="width:100%;">
				</div>			
				#foreach($user in $!list)
				<tr onMouseMove="setBackground(this,true);"	onMouseOut="setBackground(this,false);" id="tr_$!user.id" ondblclick="dbData('$!user.id','$globals.getEmpFullNameByUserId("$!user.id")','$!user.deptId','$!user.departmentName','$!globals.getEnumerationItemsDisplay("duty","$!user.TitleID")');">

					<td width="40"><input name="keyId" id="$!user.id" type="radio" value="$!user.id" onclick="checkKeyRedio('$!user.id')" /></td>
					<td width="100" style="text-align:left;" onclick="checkTr('$!user.id')"><span style="width:100px;display:inline-block;" title='$!user.empNumber'>$!user.empNumber</span></td>
					<td width="107" style="text-align:left;" onclick="checkTr('$!user.id')"><span style="width:107px;display:inline-block;" id="name_$!user.id" title='$globals.getEmpFullNameByUserId("$!user.id")'>$globals.getEmpFullNameByUserId("$!user.id")</span></td>
					<td width="100" style="text-align:left;" onclick="checkTr('$!user.id')"><span style="width:100px;display:inline-block;" id="title_$!user.id" title='$!globals.getEnumerationItemsDisplay("duty","$!user.TitleID")'>$!globals.getEnumerationItemsDisplay("duty","$!user.TitleID")</span></td>
					<input type="hidden" name="deptCode_$!user.id" id="deptCode_$!user.id" value="$!user.deptId"/>
					<td width="168" style="text-align:left;" onclick="checkTr('$!user.id')"><span style="width:168px;display:inline-block;" class="b_sp" id="deptName_$!user.id" title='$!user.departmentName'>$!user.departmentName</span></td>
				</tr>
				#end
				#end
						
				
				#end
				<!-- by wyy -->
				#if("$!AccreditSearchForm.popname" == "empGroup")
				<thead class="tn_thead">
				<tr>
					<td>组名</td>
					<td style="text-align:left;">组描述</td>
				</tr>
				</thead>
				<div class="dHeight" style="width:100%;">
				</div>
				#foreach($emp in $!list)
				<tr onMouseMove="setBackground(this,true);"	onMouseOut="setBackground(this,false);" ondblclick="dbData('$!globals.getFromArray($!emp,0)','$!globals.getFromArray($!emp,1)');">
					<td width="200"><span style="width:200px;display:inline-block;">$!globals.getFromArray($!emp,1)</span></td>
					<td width="220"><span style="width:220px;display:inline-block;" title="$!globals.getFromArray($!emp,2)">$!globals.subTitle($!globals.getFromArray($!emp,2),30)</span></td>
				</tr>
				#end
				#end 
				#if("$!AccreditSearchForm.popname" == "communicationGroup")
				<thead class="tn_thead">
				<tr>
					<td>#if("$!AccreditSearchForm.inputType" == "radio") &nbsp; #else <input type="checkbox" value="checkbox" id="selAll" name="selAll" #if("$!AccreditSearchForm.keyType" == "choose") checked #end onClick="checkKeyId('keyId')"/> #end</td>
					<td style="text-align:left;">编号</td>
					<td style="text-align:left;">姓名</td>
					<td style="text-align:left;">电子邮箱</td>
					<td style="text-align:left;">部门全称</td>
				 </tr>
				</thead>
				<div class="dHeight" style="width:100%;">
				</div>
				#foreach($comm in $!list)
				<tr onMouseMove="setBackground(this,true);"	onMouseOut="setBackground(this,false);" id="tr_$!comm.get("id")" ondblclick="dbData('$!comm.get("id")','$!comm.get("empFullName")','$!comm.get("email")');">
					#if("$!AccreditSearchForm.inputType" == "checkbox")<td width="40"><input type="checkbox" name="keyId" id="$!comm.get("id")" value="$!comm.get("id")" onclick="checkKey('keyId','$!comm.get("id")')" /></td>#end
					#if("$!AccreditSearchForm.inputType" == "radio")<td width="40"><input name="keyId" id="$!comm.get("id")" type="radio" value="$!comm.get("id")" onclick="checkKeyRedio('$!comm.get("id")')"/></td>#end
					<td width="100" style="text-align:left;" onclick="checkTr('$!comm.get("id")')"><span style="width:100px;display:inline-block;">$!comm.get("empNumber")</span></td>
					<td width="100" style="text-align:left;"  onclick="checkTr('$!comm.get("id")')"><span style="width:100px;display:inline-block;" id="name_$!comm.get("id")" title="$!comm.get("empFullName")">$!comm.get("empFullName")</span></td>
					<td width="150" style="text-align:left;" onclick="checkTr('$!comm.get("id")')"><span style="width:150px;display:inline-block;" id="email_$!comm.get("id")" title="$!comm.get("email")">$!comm.get("email")</span></td>
					<td width="100" style="text-align:left;" onclick="checkTr('$!comm.get("id")')"><span style="width:50px;display:inline-block;" title="$!comm.get("deptFullName")">$!comm.get("deptFullName")</span></td>
				 </tr>
				#end
				#end
				
				#if("$!AccreditSearchForm.popname" == "clientGroup")
				<thead class="tn_thead">
				<tr>
					<td>#if("$!AccreditSearchForm.inputType" == "radio") &nbsp; #else <input type="checkbox" value="checkbox" id="selAll" name="selAll" #if("$!AccreditSearchForm.keyType" == "choose") checked #end onClick="checkKeyId('keyId')"/> #end</td>
					<td style="text-align:left;">客户名称</td>
					<td style="text-align:left;">联系人</td>
					<td style="text-align:left;">邮件地址</td>
				</tr>
				</thead>
				<div class="dHeight" style="width:100%;">
				</div>
				#foreach($client in $!list)
				<tr onMouseMove="setBackground(this,true);"	onMouseOut="setBackground(this,false);" ondblclick="dbData('$!client.get("detid")','$!client.get("userName")','$!client.get("clientEmail")');">
					#if("$!AccreditSearchForm.inputType" == "checkbox")<td width="30"><input type="checkbox" name="keyId" id="$!client.get("detid")" value="$!client.get("detid")" onclick="checkKey('keyId','$!client.get("detid")')" /></td>#end
					#if("$!AccreditSearchForm.inputType" == "radio")<td width="30"><input name="keyId" id="$!client.get("detid")" type="radio" value="$!client.get("detid")" onclick="checkKeyRedio('$!client.get("detid")')"/></td>#end
					<td width="150" style="text-align:left;" onclick="checkTr('$!client.get("detid")')"><input id="clickName_$!client.get("detid")" type="hidden" value="$!client.get("clientName")" /><span style="width:150px;display:inline-block;" title="$!client.get("clientName")">$!globals.subTitle($!client.get("clientName"),20)</span></td>
					<td width="100" style="text-align:left;" onclick="checkTr('$!client.get("detid")')"><input id="name_$!client.get("detid")" type="hidden" value="$!client.get("userName")" /><span style="width:100px;display:inline-block;" id="" title="$!client.get("userName")">$!globals.subTitle($!client.get("userName"),20)</span></td>
					<td width="150" style="text-align:left;" onclick="checkTr('$!client.get("detid")')"><input id="email_$!client.get("detid")" type="hidden" value="$!client.get("clientEmail")" /><span style="width:120px;display:inline-block;" id="" title="$!client.get("clientEmail")">$!globals.subTitle($!client.get("clientEmail"),20)</span></td>
				</tr>
				#end
				#end
				#if("$!AccreditSearchForm.popname" == "CrmClickGroup")
				<thead class="tn_thead">
				<tr>
					<td>#if("$!AccreditSearchForm.inputType" == "radio") &nbsp; #else <input type="checkbox" value="checkbox" id="selAll" name="selAll" #if("$!AccreditSearchForm.keyType" == "choose") checked #end onClick="checkKeyId('keyId')"/> #end</td>
					<td>客户编号</td>
					<td>客户名称</td>
				</tr>
				</thead>
				<div class="dHeight" style="width:100%;">
				</div>
				#foreach($crmclick in $!list)
				<tr onMouseMove="setBackground(this,true);"	onMouseOut="setBackground(this,false);" ondblclick="dbData('$!crmclick.get("id")','$!crmclick.get("clientName")');">
					#if("$!AccreditSearchForm.inputType" == "checkbox")<td width="30"><input type="checkbox" name="keyId" id="$!crmclick.get("id")" value="$!crmclick.get("id")" onclick="checkKey('keyId','$!crmclick.get("id")')" /></td>#end
					#if("$!AccreditSearchForm.inputType" == "radio")<td width="30"><input name="keyId" id="$!crmclick.get("id")" type="radio" value="$!crmclick.get("id")" onclick="checkKeyRedio('$!crmclick.get("id")')"/></td>#end
					<td width="200" style="text-align:left;"><span style="width:200px;display:inline-block;">$!crmclick.get("clientNo")</span></td>
					<td width="200" style="text-align:left;" onclick="checkTr('$!crmclick.get("id")')"><span style="width:200px;display:inline-block;" id="name_$!crmclick.get("id")" title="$!crmclick.get("clientName")">$!globals.subTitle($!crmclick.get("clientName"),30)</span></td>
				</tr>
				#end
				#end
			</table>
			</td>
			</tr>
		</table></div>
		#if("$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "onLineGroup" || "$!AccreditSearchForm.popname" == "communicationGroup" || "$!AccreditSearchForm.popname" == "clientGroup" || "$!AccreditSearchForm.popname" == "CrmClickGroup" || "$!AccreditSearchForm.popname" == "staffGroup" || "$!AccreditSearchForm.popname" == "jobGroup" || "$!AccreditSearchForm.popname" == "leaveGroup")
		<div style="float:left;padding:0;">
		<ul id="pagination-flickr" style="padding:0;overflow:hidden;">
		<li><a href="javascript:edh('0')" title="全部">ALL</a>
		<a href="javascript:edh('A')">A</a>
		<a href="javascript:edh('B')">B</a>
		<a href="javascript:edh('C')">C</a>
		<a href="javascript:edh('D')">D</a>
		<a href="javascript:edh('E')">E</a>
		<a href="javascript:edh('F')">F</a>
		<a href="javascript:edh('G')">G</a>
		<a href="javascript:edh('H')">H</a>
		<a href="javascript:edh('I')">I</a>
		<a href="javascript:edh('J')">J</a>
		<a href="javascript:edh('K')">K</a>
		<a href="javascript:edh('L')">L</a>
		<a href="javascript:edh('M')">M</a>
		<a href="javascript:edh('N')">N</a>
		<a href="javascript:edh('O')">O</a>
		<a href="javascript:edh('P')">P</a>
		<a href="javascript:edh('Q')">Q</a>
		<a href="javascript:edh('R')">R</a>
		<a href="javascript:edh('S')">S</a>
		<a href="javascript:edh('T')">T</a>
		<a href="javascript:edh('U')">U</a>
		<a href="javascript:edh('V')">V</a>
		<a href="javascript:edh('W')">W</a>
		<a href="javascript:edh('X')">X</a>
		<a href="javascript:edh('Y')">Y</a>
		<a href="javascript:edh('Z')">Z</a></li></ul></div>
		<div class="listRange_pagebar">
			$!pageBar
		</div>
		#end
		#if("$!AccreditSearchForm.popname" == "defineClass") 
		<div class="listRange_pagebar">
			$!pageBar
		</div>
		#end
		
</form>
</body>
</html>