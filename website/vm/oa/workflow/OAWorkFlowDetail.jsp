<!DOCTYPE html>
<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$!tableCHName</title>
<link rel="stylesheet" href="/style/css/tableLayout.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">	
$(function(){
	var oWidth = $(".thirdDiv").width();
	$(".secondDiv").css("width",oWidth);
});

function addAffix(){
	asyncbox.open({
		id : 'dealdiv',
	　　url : '/OAMyWorkFlow.do?keyId=$!keyId&tableName=$tableInfo.tableName&operation=6',
		title : '$text.get("com.flow.add.affix")',width : 730,height : 390,
	    btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
	    if(action == 'ok'){
			if(opener.beforSubmit(opener.form)){
				opener.form.submit();
			}
			return false;
		}
		}
	});
}
function dealAsyn(){
	location.href='/OAWorkFlowAction.do?tableName=$!tableInfo.tableName&currNodeId=$!currNodeId&&keyId=$!keyId&operation=5&winCurIndex=$winCurIndex&designId=$!designId';
}	

function flowDepict(applyType,keyId){ 
	var winWidth = 1024;
	if($(window).width()>1024){
		winWidth = 1150;
	}
	window.open("/OAMyWorkFlow.do?keyId=$!keyId&operation=$globals.getOP("OP_DETAIL")&applyType="+applyType,null,"height=570, width="+winWidth+",top=50,left=100 ");
}

function preview(){
	var isPrint=$("#isPrint").attr("checked");
	if(typeof(isPrint)=="undefined"){
		$("#signDiv").hide();
	}
	var oDiv2= document.getElementById("listRange_id");
	oDiv2.style.height="100%";
	window.print();
	oDiv2.style.height=(document.documentElement.clientHeight-32)+"px";
	$("#signDiv").show();
}

</script>
</head>
<body onload="fillContent();">
<iframe  name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form" name="form" action="/OAWorkFlowAction.do" onSubmit="return beforeSubmit(this);" target="formFrame">
<input type="hidden" name="operation" id="operation" value="$globals.getOP("OP_UPDATE")"  />
<input type="hidden" name="button" id="button" value=""/>
<input type="hidden"  name="tableName" id="tableName" value="$!tableInfo.tableName"/>
<input type="hidden" name="billid" id="billid"  value="$!keyId"/>
<input type="hidden" id="currNodeId" name="currNodeId" value="$!currNodeId">
<input type="hidden" id="winCurIndex" name="winCurIndex" value="$winCurIndex">
<input type="hidden" id="retValUrl" name="retValUrl" value=""/>
<input type="hidden" id="fromAdvice" name="fromAdvice" value="$!noback">
<input type="hidden" id="designId" name="designId" value="$!designId">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$!tableCHName</div>
	#if("$!mobileDetail"!="true")
	<ul class="HeadingButton">
		<li><button type="button" id="backList"  name="backList" onClick="window.save=true;closeWindows();" class="b2">关闭</button></li>
		<li><button type="button" id="backList"  name="backList" onClick="preview();"  disabled  class="btDisabled b2">打印 </button></li>
		<li><button type="button" onClick="javascript:flowDepict('$!designId','');"  disabled class="btDisabled">查看流程图&nbsp;</button></li>
	</ul>
	#end
</div>
	<!--start-->
	<div id="listRange_id">
	<script type="text/javascript">
		$("#listRange_id").height(document.documentElement.clientHeight-32);
	</script>
		<div class="listRange_1"  >
			<div class="secondDiv" >
				<div class="thirdDiv">
					$!layOutHtml
				</div>
			</div>
			#if("$!mobileDetail"!="true")
			<div id="signDiv" class="delivers-view">
				<div>
					<input  type="button" class="bu_02" onClick="addAffix()" value="补充意见"/>
					#if($!delivers.size()>0 || $!affixs.size()>0) 
						<span id="isPrintSpan" class="isPrint">
							<input name="isPrint" type="checkbox"  id="isPrint" value="1" style="margin-left:20px;" />打印审核记录
						</span>
					#end
				</div>
				<br/>
				#if("$!flowNew"=="true")  
					#if($!delivers.size()>0)
					<div class="view-ul-wp">
						<p class="t-p">审核记录</p>
						<ul class="view-ul">
							#foreach($!deliver in $!delivers)
							<li class="view-li">
								<div class="d-pa">
									<em class="node-em">第$!velocityCount步&nbsp;&nbsp;$!deliver.nodeID</em>
									<span class="check-person">$!deliver.checkPerson</span>
								</div>
								<div class="d-dbk">
									<em class="end-time">[$!deliver.endTime]</em>
									<div class="app-work-check">
										&nbsp;$!deliver.approvalOpinions<br>
										#foreach($affix in $globals.strSplitByFlag($deliver.affix,';'))
											#if("$!affix"!="")
											$affix <a href="/ReadFile?fileName=$globals.urlEncode($affix)&realName=$globals.urlEncode($affix)&tempFile=false&type=AFFIX&tableName=$!tableInfo.tableName&down=true" target="_blank">$text.get("common.lb.download")</a>
											#end
										#end
									</div>
									<em class="node-type">
										$!deliver.nodeType #if("$!deliver.workFlowNode"!="")-> [$!deliver.workFlowNode]-$!deliver.checkPersons #end
									</em>
								</div>
							</li>
							#end
						</ul>
						<p style="margin-top:10px;"><span class="hBtns" onClick="addAffix()">补充意见</span></p>
					</div>
	    			 #end
				#else
				    #if($!delivers.size()>0)
					<table class="ViewTable" border="0" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD" >
					<caption style="text-align: left;"><b>$text.get("common.sign.advice")</b></caption>
				    	<thead>
				      	<tr>
				          <td style="width: 140px;">$text.get("oa.workFlow.step")</td>
				          <td style="width: 100px;">$text.get("common.sign.human")</td>
				          <td style="width: 140px;">$text.get("common.sign.time")</td>
				          <td style="width: 400px;">会签意见</td>
				        </tr>
				        </thead>
				        <tbody>
				        #foreach($!deliver in $!delivers)
				      	<tr>
				          <td>$!workFlowDesignBeans.get($!designId).getFlowNodeMap().get($!deliver.nodeId).display</td>
				          <td>$!deliver.empFullName</td>
				          <td>$!deliver.attTime</td>
				          <td>$!deliver.deliverance</td>
				      	</tr>
				        #end
				        </tbody>
		    		 </table>
	    			 #end
		    		<br/>
		    		#if($!affixs.size()>0)
			    		<table class="ViewTable" border="0" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD" style="width: 820px;" >
						<caption style="text-align: left;"><b>$text.get("oa.flow.affix.down")</b></caption>
					    	<thead>
					      	<tr>
					          <td style="width: 140px;">$text.get("oa.add.affix.person")</td>
					          <td style="width: 140px;">$text.get("oa.flow.upload.time")</td>
					          <td >$text.get("oa.flow.affix.name")</td>
					        </tr>
					      </thead>
					      <tbody>
					      #foreach($!deliver in $!affixs)
					      	<tr>
					          <td>$!deliver.empFullName</td>
					          <td>$!deliver.attTime</td>
					          <td>
					          	#foreach($affix in $globals.strSplitByFlag($deliver.affix,';'))
									$affix <a href="/ReadFile?fileName=$globals.urlEncode($affix)&realName=$globals.urlEncode($affix)&tempFile=false&type=AFFIX&tableName=$!tableInfo.tableName&down=true" target="_blank">$text.get("common.lb.download")</a>
								#end
					          </td>
					      	</tr>
					      #end
					      </tbody>
			    		</table>
		    	   		 <input type="hidden" id="attachFiles" name="attachFiles" value="">
						<input type="hidden" id="delFiles" name="delFiles" value="">
		    		#end
	    		#end
	    		<br/>
			</div>
			#end
		</div>
	</div>
	<!--end-->
</form>

</body>

<script type="text/javascript">
//填充表單內容
function fillContent(){
jQuery.getJSON("/OAWorkFlowAction.do?operation=7&type=getJson&keyId=$!keyId&tableName=$!tableInfo.tableName",function(data){

	#set($hiddenFields="$hiddenFields")
	#foreach ($field in $tableInfo.fieldInfos )
		#set($fieldName=$field.fieldName)
		#if($fieldName.indexOf("field_")>-1)
			#set($fieldName=";"+"$field.fieldName"+";")
			#set($fieldHidden="false")
			
			#if($hiddenFields.indexOf($fieldName)>-1)
				#set($fieldHidden="true")
			#end
			
			#if($fieldHidden=="false")
				#if("$field.defaultValue"=="employee") //职员弹出框  
					var empallName=eval("data.popedomUserIds_$field.fieldName");
					if(empallName.indexOf(";")>-1){
						var emphtml="";
						var empList=empallName.split(";");
						for(var i=0;i<empList.length;i++){
							var empname=empList[i];
							if(empname.length>0){
								var empstrList=empname.split("%koron%");
								emphtml += '<font class="showFont">'+empstrList[1]+';&nbsp;</font>';
							}
						}
						$("#$field.fieldName").before(emphtml);
					}else{
						$("#$field.fieldName").before('<font class="showFont">'+empallName+'</font>');
					}
				
					
				#elseif("$field.defaultValue"=="dept") //部门弹出框  
					var deptallName=eval("data.popedomDeptIds_$field.fieldName");
					if(deptallName.indexOf(";")>-1){
						var depthtml="";
						var deptList=deptallName.split(";");
						for(var i=0;i<deptList.length;i++){
							var deptname=deptList[i];
							if(deptname.length>0){
								var deptstrList=deptname.split("%koron%");
								depthtml += '<font class="showFont">'+deptstrList[1]+';&nbsp;</font>';
							}
						}
						$("#$field.fieldName").before(emphtml);
					}else{
						$("#$field.fieldName").before('<font class="showFont">'+deptallName+'</font>');
					}
					
				#elseif("$field.defaultValue" == "client")//客户弹出框  				
					var clientallName=eval("data.popedomClientId_$field.fieldName");
					if(clientallName.indexOf(";")>-1){
						var clienthtml="";
						var clientList=clientallName.split(";");
						for(var i=0;i<clientList.length;i++){
							var clientname=clientList[i];
							if(clientname.length>0){
								var clientstrList=clientname.split("%koron%");
								clienthtml += '<font class="showFont">'+clientstrList[1]+';&nbsp;</font>';
							}
						}
						$("#$field.fieldName").before(clienthtml);
					}else{
						$("#$field.fieldName").before('<font class="showFont">'+clientallName+'</font>');
					}
			
				#elseif("$field.defaultValue" == "popup") 				
					var popallName=eval("data.popup_$field.fieldName");
					if(popallName.indexOf(";")>-1){
						var pophtml="";
						var popList=popallName.split(";");
						for(var i=0;i<popList.length;i++){
							var popname=popList[i];
							if(popname.length>0){
								var popstrList=popname.split("%koron%");
								pophtml += '<font class="showFont">'+popstrList[1]+';&nbsp;</font>';
							}
						}
						$("#$field.fieldName").before(pophtml);
					}else{
						$("#$field.fieldName").before('<font class="showFont">'+popallName+'</font>');
					}
				#elseif("$field.defaultValue"=="affix") //附件 				
					var allAffix=eval("data.$field.fieldName");
				    var affixList=allAffix.split(";");
				 	for(var i=0;i<affixList.length;i++){
				 		var uprow=affixList[i];
				 		if(uprow.length>0){
				 			if(uprow.indexOf(":") > -1){
					 			var up1=uprow.split(":")[0];
								var up2=uprow.split(":")[1];
				 				$("#$field.fieldName").before('<a href="/ReadFile?fileName='+encodeURI(up1)+'&realName='+encodeURI(up2)+'&tempFile=true&type=AFFIX&down=true" target="_blank"><font class="showFont">'+up2+';</font></a>');
				 			}else{
				 				$("#$field.fieldName").before('<a href="/ReadFile?fileName='+encodeURI(uprow)+'&realName='+encodeURI(uprow)+'&tempFile=true&type=AFFIX&down=true" target="_blank"><font class="showFont">'+uprow+';</font></a>');
				 			}
				 		}
				 	}
				 	
				#elseif("$field.defaultValue"=="img") //图片
					var allImg=eval("data.$field.fieldName");
				    var imgList=allImg.split(";");
				 	for(var i=0;i<imgList.length;i++){
				 		var uprow=imgList[i];
				 		if(uprow.length>0){
				 			if(uprow.indexOf(":") > -1){
					 			var up1=uprow.split(":")[0];
								var up2=uprow.split(":")[1];
				 				$("#$field.fieldName").before('<img src="/ReadFile.jpg?fileName='+encodeURI(up1)+'&realName='+encodeURI(up2)+'&tempFile=true&type=PIC&tableName=$!tableInfo.tableName" width="150" height="150" border="0"> ');
				 			}else{
				 				$("#$field.fieldName").before('<img src="/ReadFile.jpg?fileName='+encodeURI(uprow)+'&realName='+encodeURI(uprow)+'&tempFile=true&type=PIC&tableName=$!tableInfo.tableName" width="150" height="150" border="0"> ');
				 			}
				 		}
				 	}
				 	
				#else
					$("#$field.fieldName").before('<font class="showFont">'+data.$field.fieldName+'</font>');
				#end
			#end
			$("#$field.fieldName").remove();
		#end
	#end
	
	//数据加载完毕才能让按扭可用
	$(".btDisabled").removeAttr("disabled");
	$(".btDisabled").removeClass("btDisabled");
	
	});
}

</script>
</html>