<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("wokeflow.list.record")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script>
if(typeof(top.junblockUI)!="undefined"){
	top.junblockUI();
}

function jblockUI(){
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({message: "<img src='/style/images/load.gif'/>",css:{ background: 'transparent'}}); 
	}
}

//修改类型
function ShowDialog(url,dialogSettings ){ 
	var var1 = "";  
	if(dialogSettings==null||dialogSettings==""){
	   dialogSettings = "Center:yes;Resizable:no;DialogHeight:360px;DialogWidth:400px;Status:no";
	}
	return window.showModalDialog( url, var1, dialogSettings);
}

function updateType1(boolvalue){ 
	var pageNo = document.getElementById("pageNo").value ;
	if(!isChecked("keyId")){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}else{		
	
	    var dialog = ShowDialog("WokFlowAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=updatetype");
	    if(typeof(dialog)=="undefined")return false ;
	    document.getElementById("updateType").value = "flowType" ;
	    document.getElementById("flowType").value = dialog ;
	}
}

//获取类型id
function getTypeClassCode(){
	var obj=document.getElementById("typeUpdate");
	return obj.value;
}
	
function add(){
	document.location = "/WokFlowAction.do?operation=$globals.getOP("OP_ADD_PREPARE")";
}
	
function upd(id,newFlag){
	if(newFlag!='NEW'){
		//var pageNo = document.getElementById("pageNo").value ;
		document.location = "/WokFlowAction.do?operation=$globals.getOP("OP_UPDATE")&id="+id+
							"&oper=$!oper&state=$!state&keyWord=$globals.encode($!keyWord)&tblWorkFlowType=$!tblWorkFlowType";
	}else{
		alert('$text.get("wokeflow.no.update")');
	}
}
	
function selectCheckBox(checkBox,NewFlag,FileName){
	if(checkBox.checked){		
		//为SQL控件赋值
		form.CPReportFormat.NewFlag=NewFlag;
		form.CPReportFormat.FileName=FileName;	
	}
}

function updateClewType(){
	if(!isChecked("keyId")){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}else{
		var cbs=document.getElementsByName("keyId");
		for(var i=0;i<cbs.length;i++){
			if(cbs[i].checked){
				var values = document.getElementsByName(cbs[i].value);
				var awokeMode="";
				for(var j=0;j<values.length;j++){
					if(values[j].checked){
						awokeMode +=values[j].value+",";
					}
				}
				document.getElementById("wakeUp"+cbs[i].value).value = awokeMode ;
			}				
		}
		document.getElementById("updateType").value = "wakeUp" ;
    }
}

function startStop(type){
	var items = document.getElementsByName("keyId");
	for(var i=0;i<items.length;i++){			
    	if(items[i].checked&&items[i].designStatus=="NEW"){
    		items[i].checked=false;
    	}
	}
	
	if(!isChecked("keyId")){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	
	document.getElementById("flowType").value = type ;
	document.getElementById("updateType").value = "startStop" ;
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
#if("$!oper"!="config")
	<form action="/WokFlowAction.do" name="form" method="post">
	#else
	<form action="/WokFlowAction.do?oper=$!oper" name="form" method="post">
	#end
	<input type="hidden" name="type" value="tablelist">
	<input type="hidden" name="pageParam" value="" />
	<input type="hidden" name="updateType" value=""/>
	<input type="hidden" name="flowType" value="">
	<input type="hidden" name="operation" value="4">
	<input type="hidden" name="winCurIndex" value="$!winCurIndex">
	<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div> 
	#if("$!oper"!="config")
	<div class="HeadingTitle">$text.get("wokeflow.setting.self")</div>
	#else
	<div class="HeadingTitle">$text.get("wokeflow.approval.setting")</div>
	#end
	<ul class="HeadingButton">	
	 <li><button name="button" type="submit" class="b2" onClick="document.getElementById('pageNo').value=1">$text.get("common.lb.query")</button></li>
	  <li><button type="submit" onclick="return startStop('start');">$text.get("OpenValue")</button></li>
	  <li><button type="submit" onclick="return startStop('stop');">$text.get("StopValue")</button></li>
	  <li><button  type="submit"  onClick="return updateClewType()">$text.get("oa.workflow.awokeMode.update")</button></li>
	  #if("$!oper"!="config")
		<li><button name="button" type="submit" onClick="return updateType1(true)">$text.get("oa.workflow.type.update")</button></li>	
	  #end	  
	  <li style="margin-right:5px;">
	    <object classid="clsid:147C50F5-F8DB-420F-BDD1-B89A31A28114" width=85 height=20 id=CPReportFormat >
          <param name="NewFlag" value="" >
          <param name="FileName" value="" >
          <param name="URL" value="$!IP|$!port" >
        </object>
	  </li>
	</ul>
</div>	
<div id="listRange_id">
	<input type="hidden" name="stat" value="$stat" id="stat"/>	
		<div class="listRange_1">	
		<li><span>$text.get("wokeflow.module.name")：</span>		
		<input type="text" id="tableName" name="keyWord" value="$!keyWord"/>
		</li>
		<li>$text.get("oa.workflow.type")：


		  <select name="tblWorkFlowType" >
            <option value="">&nbsp;</option>
		#foreach ($ls_row in $twftstype)
			#if(!($!globals.get($ls_row,0)=="0000200001"||$!globals.get($ls_row,0)=="0000200002"))
		    <option value="$!globals.get($ls_row,0)"
				 #if($!globals.get($ls_row,0)==$!tblWorkFlowType)
					selected
				#end	
			>$!globals.get($ls_row,1)</option>
			#end
		#end
	      </select>
		</li>
		<li>
		$text.get("common.lb.status"):
		<select name="state" >
		<option value="" selected="selected"></option>
			<option value="1" #if("$!state"=="1")selected #end >$text.get("oa.bbs.started")</option>
			<option value="0" #if("$!state"=="0")selected #end>$text.get("com.consign.enabled")</option>
		  </select>
		</li>
	</div>
    <div class="scroll_function_small_a" id="conter">
	<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-105;
oDiv.style.height=sHeight+"px";
</script>
		<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" style="width:700px;">
		<thead>
			<tr>
				<td style="width:30px" class="oabbs_tc"><input type="checkbox" name="selAll" onClick="checkAll('keyId')">
				</td>
				<td width="100" class="oabbs_tc">$text.get("module.lb.moduleName")</td>
				#if("$!oper"!="config")
				<td width="150" class="oabbs_tc">$text.get("excel.add.tableName")</td>
				<td width="100" class="oabbs_tc">$text.get("oa.workflow.type")</td>
				#end
				<td width="250" class="oabbs_tc">$text.get("oa.workflow.awokeMode")</td>
				<td width="60" class="oabbs_tc">$text.get("common.lb.status")</td>
				<td width="60" class="oabbs_tc">$text.get("wokeflow.list.isDesigned")</td>
				<td width="60" class="oabbs_tc">$text.get("common.lb.operation")</td>
			</tr>
		</thead>
			#foreach($vo in $list)
			<tbody>
		 	<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td class="oabbs_tc">
				  <input type="checkbox" name="keyId" value="$vo.id"  designStatus="$vo.NewFlag" onClick="selectCheckBox(this,'$vo.NewFlag','$vo.FlowName')"/>
				</td>
				<td class="oabbs_tl">$globals.getTableDisplayName("$vo.TableName") &nbsp;</td>
				#if("$!oper"!="config")
			    <td class="oabbs_tl">$vo.TableName &nbsp;</td>
			    <td class="oabbs_tc">$!vo.ClassCode &nbsp;</td>
				#end
			   	<td class="oabbs_tc">
			   	#foreach($wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
		 			#if($!vo.AwokeMode.indexOf($wakeUpType.value)!=-1)
		 				#set ($check="checked")
		 			#end
		 			<input type="checkbox" name="$vo.id"  value="$wakeUpType.value" $!check/> $globals.interceptString($wakeUpType.name,0,2)
		 			#set ($check="")
	   			#end
	   			<input type="hidden" id="wakeUp$vo.id" name="awokeMode" value="">
	   			</td>
				<td class="oabbs_tc">#if($vo.IsStatart==1)$text.get("oa.bbs.started") #else $text.get("com.consign.enabled") #end</td>
				<td class="oabbs_tc">#if($vo.NewFlag=="NEW") $text.get("wokeflow.lb.noDesign") #else $text.get("wokeflow.lb.isDesign") #end</td>
				<td class="oabbs_tc">#if($vo.NewFlag=="OLD")<a href="javascript:upd('$vo.id','$vo.NewFlag')">#if($vo.IsStatart==1)$text.get("StopValue") #else $text.get("OpenValue") #end</a>#else &nbsp;#end</td>
			</tr>
		  </tbody>#end	
		</table>
		</div>
		<!-- 以下是分页  -->
					<div class="listRange_pagebar">
						$!pageBar
					</div>
	 	</div>
	</form>
</body>
</html>
