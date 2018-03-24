<!DOCTYPE html>
<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$!tableCHName</title>
<link rel="stylesheet" href="/style/css/tableLayout.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script type="text/javascript" src="$globals.js("/js/workflow.vjs",$tableInfo.tableName,$text)"></script>
<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script>
var allHidFields="";

$(function(){
	var oWidth = $(".thirdDiv").width();
	$(".secondDiv").css("width",oWidth);
});

function beforeSubmit(form){
	if(!checkFlowField()){ //验证流程设计必填字段是否为空
		return false;
	}

	if(!checkTable(allHidFields)){
		return false;
	}
	return true;
}


function deliverTo(url,retValUrl){
	$("#retValUrl").val(retValUrl);
	if(url.indexOf("addAffix=true")>-1){
		addAffix();
	}else{
		asyncbox.open({
			id:'dealdiv',url:url,title:'转交',width:650,height:370,
	        btnsbar : jQuery.btn.OKCANCEL,
		    callback : function(action,opener){
			    if(action == 'ok'){
					if(opener.beforSubmit(opener.form)){
						opener.form.submit();
					}
					return false;
				}else if(action == 'close'){
					jQuery.close('dealdiv');
				}else if(action == 'cancel'){
					jQuery.close('dealdiv');
				}
	　　  　}
	　 });
	}
}

function dealAsyncbox(){
	var retValUrl = $("#retValUrl").val();
	jQuery.close('dealdiv');
	if($!noback){
		closeWin();
	}else{
		window.save = true;
		window.location.href = retValUrl;
	}
}

function addAffix(){
	asyncbox.open({
		id : 'dealdiv',
　　　    url : '/OAMyWorkFlow.do?keyId=$!keyId&tableName=$tableInfo.tableName&operation=6',
	 	title : '$text.get("com.flow.add.affix")',
　　 　   width : 650,
　　     height : 370,
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
	location.href='/OAWorkFlowAction.do?tableName=$!tableInfo.tableName&currNodeId=$!currNodeId&&keyId=$!keyId&operation=7&winCurIndex=$winCurIndex&designId=$!designId';
}

function preview(){
	var isPrintDeliver=$("#isPrintDeliver").attr("checked");
	if(typeof(isPrintDeliver)=="undefined"){
		$("#deliverTable").hide();
	}
	
	var isPrintAffix=$("#isPrintAffix").attr("checked");
	if(typeof(isPrintAffix)=="undefined"){
		$("#affixTable").hide();
	}
	
	var oDiv2=m("listRange_id");
	oDiv2.style.height="100%";
	window.print();
	oDiv2.style.height=(document.documentElement.clientHeight-32)+"px";
	$("#deliverTable").show();
	$("#affixTable").show();
}


function keyDown(e) { 
	var iekey=event.keyCode; 
	if(iekey==8){
		if(!((event.srcElement.tagName=="INPUT"  && event.srcElement.readOnly == false && event.srcElement.type =="text") || ( event.srcElement.tagName=="TEXTAREA" && event.srcElement.readOnly == false))){
			return false;
		}
	}
}
document.onkeydown = keyDown; 

</script>
</head>

<body onload="fillContent();getFlowField();showStatus();">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form" name="form" action="/OAWorkFlowAction.do" onSubmit="return beforeSubmit(this);" target="formFrame">
<input type="hidden" name="operation" id="operation" value="$globals.getOP("OP_UPDATE")"  />
<input type="hidden" name="button" id="button" value=""/>
<input type="hidden" id="noback" name="noback" value="$!noback">
<input type="hidden"  name="tableName" id="tableName" value="$!tableInfo.tableName"/>
<input type="hidden" name="billid" id="billid"  value="$!keyId"/>
<input type="hidden" id="currNodeId" name="currNodeId" value="$!currNodeId">
<input type="hidden" id="winCurIndex" name="winCurIndex" value="$winCurIndex">
<input type="hidden" id="retValUrl" name="retValUrl" value=""/>
<input type="hidden" id="fromAdvice" name="fromAdvice" value="$!fromAdvice">
<input type="hidden" id="designId" name="designId" value="$!designId">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$!tableCHName</div>
	<ul class="HeadingButton">
		<li><button type="button" id="deliverToBut" name="deliverToBut" title="Ctrl+S" onClick="if(beforeSubmit(document.form)) {form.button.value='deliverTo'; document.form.submit();}" disabled class="btDisabled b2">审核</button></li>		
		<li><button type="button" id="copySave" name="copySave" title="Ctrl+S" onClick="if(beforeSubmit(document.form)){window.save=true;form.button.value=''; document.form.submit();}" disabled  class="btDisabled b2">$text.get("common.lb.TemporarilySave")</button></li>
		<li><button type="button" onClick='preview();' disabled  class="btDisabled b2">$text.get("common.lb.print")</button></li>
		<li><button type="button" id="backList"  name="backList" onClick="closeWindows();" class="b2">关闭</button></li>
		<li><button type="button" onClick="javascript:flowDepict('$!designId','$!keyId');"  disabled class="btDisabled" >$text.get("common.lb.checkFlowChart")&nbsp;</button></li>
	</ul>
	</div>
	<div id="listRange_id">
	<script type="text/javascript">
		$("#listRange_id").height(document.documentElement.clientHeight-32);
	</script>
		<div class="listRange_1">
			<div class="secondDiv" >
				<div class="thirdDiv">
					$!layOutHtml
				</div>
			</div>
			<div id="signDiv">
				<div style="width:825px;margin:0 auto;">
					<input  type="button" class="bu_02" onClick="if(beforeSubmit(document.form)){form.button.value='addAffix'; document.form.submit();};" value="补充意见"/>
					#if($!delivers.size()>0) 
						<span id="isPrintSpan" class="isPrint">
							<input name="isPrintDeliver" type="checkbox"  id="isPrintDeliver" value="1" style="margin-left:20px;" />
								打印审批记录
						</span>
					#end
					#if( $!affixs.size()>0) 
						<span id="isPrintSpan" class="isPrint">
							<input name="isPrintAffix" type="checkbox"  id="isPrintAffix" value="1" style="margin-left:20px;" />
								打印附件
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
									<em class="node-em">$!deliver.nodeID</em>
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
				<table class="ViewTable" id="deliverTable" border="0" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD" >
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
		    		<table class="ViewTable" id="affixTable"  border="0" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD" style="width: 820px;" >
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
		</div>
	</div>
</form>

</body>

<script type="text/javascript">

var sys_dateValue     ='$!globals.getHongVal("sys_date")';
var sys_timeValue     ='$!globals.getHongVal("sys_time")'
var sys_datetimeValue ='$!globals.getHongVal("sys_datetime")';
var hiddenempId       ='$!globals.getLoginBean().id';
var sys_empNameValue  ='$!globals.getLoginBean().empFullName';
var hiddentitleId     ='$!globals.getLoginBean().titleId';
var sys_empTitleValue ='$!globals.getEnumerationItemsDisplay("duty", $globals.getLoginBean().titleId,"zh_CN")';
var hiddendeptId      ='$!globals.getLoginBean().departCode'
var sys_deptValue     ='$!globals.getLoginBean().departmentName';
var sys_startdateValue='$!globals.getHongVal("sys_date")';
var sys_starttimeValue='$!globals.getHongVal("sys_datetime")';


function flowDepict(applyType,keyId){ 
	window.open("/OAMyWorkFlow.do?keyId=$!keyId&operation=$globals.getOP("OP_DETAIL")&applyType="+applyType);
}

//过滤流程设计中设置的字段
function getFlowField(){
		#foreach ($fieldBean  in $workFlowFields)	
		#set($fieldName=$fieldBean.fieldName)
		#set($inputType=$fieldBean.inputType)
		#set($isNotNull=$fieldBean.isNotNull())
		#foreach ($field in $tableInfo.fieldInfos)
			#if("$fieldName" == "$field.fieldName")
				// 弹出框 
				#if("$field.defaultValue"=="employee" || "$field.defaultValue"=="dept" || "$field.defaultValue"=="client" || "$field.defaultValue" == "popup")
					var labelA="";
					var labelImg="";
					var labelDel="";
					var labelNum=0;
					var divId="";
					var labelId="";
					#if("$field.defaultValue"=="employee") //职员弹出框 
						labelA="empA_"+"$field.fieldName";
						labelImg="empImg_"+"$field.fieldName";
						labelDel="empDel_"+"$field.fieldName";
						labelNum=$("img[id='"+labelDel+"']").size();
						divId="emp_context"+"$!field.fieldName";
						labelId="popedomUserIds_"+"$!field.fieldName";
					#elseif("$field.defaultValue"=="dept") //部门弹出框   
						labelA="deptA_"+"$field.fieldName";
						labelImg="deptImg_"+"$field.fieldName";
						labelDel="deptDel_"+"$field.fieldName";
						labelNum=$("img[id='"+labelDel+"']").size();
						divId="dept_context"+"$!field.fieldName";
						labelId="popedomDeptIds_"+"$!field.fieldName";
					#elseif("$field.defaultValue" == "client")//客户弹出框   
						labelA="clientA_"+"$field.fieldName";
						labelImg="clientImg_"+"$field.fieldName";
						labelDel="clientDel_"+"$field.fieldName";
						labelNum=$("img[id='"+labelImg+"']").size();
						divId="client_context"+"$!field.fieldName";
						labelId="popedomClientId_"+"$!field.fieldName";
					#elseif("$field.defaultValue" == "popup") //定制弹出框   
						labelA="popupA_"+"$field.fieldName";
						labelImg="popupImg_"+"$field.fieldName";
						labelDel="popupDel_"+"$field.fieldName";
						labelNum=$("img[id='"+labelDel+"']").size();
						divId="popup_context"+"$!field.fieldName";
						labelId="popup_"+"$!field.fieldName";
					#end
					if(labelNum>0){ //弹出框多选  					
						#if("$!inputType"=="3") // 隐藏
							allHidFields+="$field.fieldName"+";";
							$("div[id='"+divId+"']").css("display","none");
						#elseif("$!inputType"=="8") // 只读
							$("a[id='"+labelA+"']").removeAttr("onclick").css("color","#b1b1c1");
							$("img[id='"+labelImg+"']").removeAttr("onclick").css("color","#b1b1c1");
							$("img[id='"+labelDel+"']").removeAttr("onclick").css("color","#b1b1c1");
						#end
					}else{ // 弹出框单选   
						#if("$!inputType"=="3") // 隐藏
							allHidFields+="$field.fieldName"+";";
							$("input[name='"+labelId+"']").css("display","none");
							$("img[name='$!field.fieldName']").css("display","none");
						#elseif("$!inputType"=="8") // 只读
							$("input[name='"+labelId+"']").removeAttr("ondblclick").removeAttr("onclick").attr("readonly","readonly").css("color","#b1b1c1");
							$("img[name='$!field.fieldName']").removeAttr("onclick").css("color","#b1b1c1");
						#end
					}
				#end
				
				#if("$field.defaultValue"=="affix" || "$field.defaultValue"=="img") //附件和图片    
					var labelSpan="";
					#if("$field.defaultValue"=="affix")
						labelSpan="affixbuttonthe"+"$field.fieldName";
					#elseif("$field.defaultValue"=="img") //图片
						labelSpan="picbuttonthe"+"$field.fieldName";
					#end
					#if("$!inputType"=="3") // 隐藏
						allHidFields+="$field.fieldName"+";";
						$("span[id='"+labelSpan+"']").css("display","none");
					#elseif("$!inputType"=="8") // 只读
						$("span[id='"+labelSpan+"']").removeAttr("onclick").css("color","#b1b1c1");
					#end
				#end
				
				#if("$field.defaultValue" == "textarea")
					#if("$!inputType"=="3") // 隐藏
						allHidFields+="$field.fieldName"+";";
						$("textarea[name='$field.fieldName']").css("display","none");
					#elseif("$!inputType"=="8") // 只读
						$("textarea[name='$field.fieldName']").attr("readonly","readonly").css("color","#b1b1c1");
					#end
				#elseif("$field.defaultValue" == "html")
					#if("$!inputType"=="3") // 隐藏
						allHidFields+="$field.fieldName"+";";
						$("textarea[name='$field.fieldName']").prev().css("display","none");
					#elseif("$!inputType"=="8") // 只读
						${field.fieldName}.readonly();
					#end
				#elseif("$field.defaultValue" == "checkbox" || "$field.defaultValue" == "radio")
					#if("$!inputType"=="3") // 隐藏
						allHidFields+="$field.fieldName"+";";
						$("#$field.defaultValue$field.fieldName").css("display","none");
					#elseif("$!inputType"=="8") // 只读
						$("input[name='$field.fieldName']").each(function(){
							if($(this).attr("checked")!="checked"){
								$(this).click(function(){return false;});
							}
						});
					#end
				#elseif("$field.defaultValue" == "select" )
					#if("$!inputType"=="3") // 隐藏
						allHidFields+="$field.fieldName"+";";
						$("select[name='$field.fieldName']").css("display","none");
					#elseif("$!inputType"=="8") // 只读
						var selectIndex=$("select[name='$field.fieldName'] option:selected").val();
						$("select[name='$field.fieldName'] option[value!='"+selectIndex+"']").remove();
					#end
				#else
					#if("$!inputType"=="3") // 隐藏
						allHidFields+="$field.fieldName"+";";
						$("#$field.fieldName").css("display","none");
					#elseif("$!inputType"=="8") // 只读
						$("#$field.fieldName").attr('readonly','readonly');
						$("#$field.fieldName").unbind('click');
					#end
				#end
			#end
		#end
	#end
}

//验证流程设计字段必填
function checkFlowField(){
	#foreach ($fieldBean  in $workFlowFields)	
		#set($fieldName=$fieldBean.fieldName)
		#set($isNotNull=$fieldBean.isNotNull())
		#set($inputType=$fieldBean.inputType) 
		#if("$isNotNull"=="true")
			#foreach ($field in $tableInfo.fieldInfos)
				#if("$fieldName" == "$field.fieldName")
					var labelText="";
					#if("$field.defaultValue" == "text")
						labelobj=$("input[name='$field.fieldName']");
						labelText=labelobj.size()==0?"":labelobj.val();
					#elseif("$field.defaultValue" == "radio")
						labelobj=$("input[name='$field.fieldName']:checked");
						labelText=labelobj.size()==0?"":labelobj.val();
					#elseif("$field.defaultValue" =="checkbox")
						$("input[name='$field.fieldName']").each(function(){
							if($(this).attr("checked")=="checked"){
								labelText=labelText+$(this).val()+";"
							}
						});	
					#elseif("$field.defaultValue" == "affix" || "$field.defaultValue" == "img")
						var len=$("input[name='$field.fieldName']").size();
						if(len==0){
							labelText="";
						}else{
							labelText="exist";
						}
					#elseif("$field.defaultValue" == "html" )
						labelText=${field.fieldName}.html();
						$("#$field.fieldName").val(labelText);
					#else
						labelText=$("#$field.fieldName").val();
					#end
					if(labelText.length == 0 && "$!inputType"!="3"  && "$!inputType"!="8" ){ //如果设置不为空的字段被隐藏了，则不需要提示

						alert("$!field.languageId"+"不能为空，请重新输入!");
						return false;
					}
				#end
			#end
		#end
	#end
	return true;
}

//初始化html编辑框




#foreach ($field in $tableInfo.fieldInfos)
	#if($field.defaultValue == "html" || $field.defaultValue == "textarea")
		$("#$field.fieldName").removeAttr("title");
	#end
	
	#if($field.defaultValue == "html")
		var isHtml=$("#$field.fieldName").attr("ishtml");
		if(isHtml=="true"){
			$("#$field.fieldName").css({width:'100%'});
			KindEditor.ready(function(K) {
				$field.fieldName = K.create('textarea[name="$field.fieldName"]',{
					uploadJson:'/UtilServlet?operation=uploadFile',
				});
			});
		}
	#end
#end

//填充表單內容   
function fillContent(){
	jQuery.getJSON("/OAWorkFlowAction.do?operation=7&type=getJson&keyId=$!keyId&tableName=$!tableInfo.tableName",function(data){
		#foreach ($field in $tableInfo.fieldInfos )
		#if($field.fieldName.indexOf("field_")>-1 || "$field.fieldName"=="id")
			#if("$field.defaultValue"!="html")
				$("#$field.fieldName").val(data.$field.fieldName);
			#end
			
			#if("$field.defaultValue"=="html")
				${field.fieldName}.html(data.$field.fieldName);
			#elseif("$field.defaultValue"=="employee") //职员弹出框

				
				var empallName=eval("data.popedomUserIds_$field.fieldName");
				if(empallName.indexOf(";")>-1){
					var emphtml="";
					var empList=empallName.split(";");
					for(var i=0;i<empList.length;i++){
						var empname=empList[i];
						if(empname.length>0){
							var empstrList=empname.split("%koron%");
							emphtml += '<option value="'+empstrList[0]+'">'+empstrList[1]+'</option>';
						}
					}
					$("select[name='empNames$field.fieldName']").append(emphtml);
				}else{
					$("#popedomUserIds_$field.fieldName").val(empallName);
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
							depthtml += '<option value="'+deptstrList[0]+'">'+deptstrList[1]+'</option>';
						}
					}
					$("select[name='deptNames$field.fieldName']").append(depthtml);
				}else{
					$("#popedomDeptIds_$field.fieldName").val(deptallName);
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
							clienthtml += '<option value="'+clientstrList[0]+'">'+clientstrList[1]+'</option>';
						}
					}
					$("select[name='clientNames$field.fieldName']").append(clienthtml);
				}else{
					$("#popedomClientId_$field.fieldName").val(clientallName);
				}
		
			#elseif("$field.defaultValue" == "popup") //自定义弹出框
				var popallName=eval("data.popup_$field.fieldName");
				if(popallName.indexOf(";")>-1){
					var pophtml="";
					var popList=popallName.split(";");
					for(var i=0;i<popList.length;i++){
						var popname=popList[i];
						if(popname.length>0){
							var popstrList=popname.split("%koron%");
							pophtml += '<option value="'+popstrList[0]+'">'+popstrList[1]+'</option>';
						}
					}
					$("select[name='popupNames$field.fieldName']").append(pophtml);
				}else{
					$("#popup_$field.fieldName").val(popallName);
				}
				
			#elseif("$field.defaultValue"=="affix") //附件
				var affixStr="affixbuttonthe$field.fieldName";
				var html = '<ul id="affixuploadul_$field.fieldName">';
				var allAffix=eval("data.$field.fieldName");
				var affixList=allAffix.split(";");
				for(var i=0;i<affixList.length;i++){
					var uprow=affixList[i];
					if( uprow.length > 0){
						if(uprow.indexOf(":") > -1){
							var up1=uprow.split(":")[0];
							var up2=uprow.split(":")[1];
							html += '<li style="background:url();" id="uploadfile_'+up1+'"><input type=hidden name=$field.fieldName value="'+uprow+'">'+up2+'' 			
								 + '<a href=\'javascript:deleteupload("'+up1+'","false","$tableInfo.tableName","AFFIX");\'>$text.get("common.lb.del")</a>' 
								 + '&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?fileName='+encodeURI(up1)+'&realName='+encodeURI(up2)+'&tempFile=true&type=AFFIX&down=true" target="_blank">$text.get("common.lb.download")</a>' ;
								 + '</li> ' ;
						}else{
							html += '<li style="background:url();" id="uploadfile_'+uprow+'">'
								 + ' <input type=hidden name=$field.fieldName value="'+uprow+'">'+uprow+'' 			
								 + '<a href=\'javascript:deleteupload("'+uprow+'","false","$tableInfo.tableName","AFFIX");\'>$text.get("common.lb.del")</a>&nbsp;&nbsp;'
								 + '<a href="/ReadFile?fileName='+encodeURI(uprow)+'&realName='+encodeURI(uprow)+'&tempFile=true&type=AFFIX&down=true" target="_blank">$text.get("common.lb.download")</a>'
								 + '</li>' ;
						}	
					}
				}
				html += '</ul>' ;
				$("#"+affixStr).after(html);
					
			#elseif("$field.defaultValue" == "img")
				var picStr="picbuttonthe$field.fieldName";
				var html = '<ul id="picuploadul_$field.fieldName">';
				var allImg=eval("data.$field.fieldName");
				var picList=allImg.split(";");
				for(var i=0;i<picList.length;i++){
					var uprow=picList[i];
					if(uprow.length>0){
						if(uprow.indexOf(":") > -1){
							var up1=uprow.split(":")[0];
							var up2=uprow.split(":")[1];
							html += '<li style="float:left;margin-left:15px;list-style:none;" id="uploadfile_'+up1+'"><input type=hidden name=$field.fieldName value="'+uprow+'"><div style="float:left;">'
							+ '<a href="/ReadFile.jpg?fileName='+encodeURI(up1)+'&realName='+encodeURI(up2)+'&tempFile=true&type=PIC&tableName=$tableInfo.tableName" target="_blank"> '
							+ '<img src="/ReadFile.jpg?fileName='+encodeURI(up1)+'&realName='+encodeURI(up2)+'&tempFile=true&type=PIC&tableName=$tableInfo.tableName" width="150" border="0"></a></div><div style="float:left;">' 
							+ '<img src="/style1/images/delete_button.gif" id="empDel_field_1" onclick=\'javascript:deleteupload("'+up1+'","false","$tableInfo.tableName","PIC");\'  alt="$text.get("common.lb.del")" title="$text.get("common.lb.del")" class="search"></div>';
							+ '</li>';
						}else{
						
							html += '<li style="float:left;margin-left:15px;list-style:none;" id="uploadfile_'+uprow+'"><input type=hidden name=$field.fieldName value="'+uprow+'"><div style="float:left;">'
				 			+ '<a href="/ReadFile.jpg?fileName='+encodeURI(uprow)+'&realName='+encodeURI(uprow)+'&tempFile=true&type=PIC&tableName=$tableInfo.tableName" target="_blank">'
				 			+ '<img src="/ReadFile.jpg?fileName='+encodeURI(uprow)+'&realName='+encodeURI(uprow)+'&tempFile=true&type=PIC&tableName=$tableInfo.tableName" width="150" border="0"></a></div><div style="float:left;">'
				 			+ '<img src="/style1/images/delete_button.gif" id="empDel_field_1" onclick=\'javascript:deleteupload("'+uprow+'","false","$tableInfo.tableName","PIC");\'  alt="$text.get("common.lb.del")" title="$text.get("common.lb.del")" class="search"></div>';
							+ '</li>';
				
						}
					}
				}
				html += '</ul>' ;
				$("#"+picStr).after(html);
				
			#elseif("$field.defaultValue" == "checkbox") //复选框
				var allCheckbox=eval("data.$field.fieldName");
				var chkList=allCheckbox.split(";");
				for(var i=0;i<chkList.length;i++){
					var uprow=chkList[i];
					if(uprow.length>0){
						$("input[name='$field.fieldName'][value='"+uprow+"']").attr("checked","checked");
					}
				}
			#elseif("$field.defaultValue" == "radio") //单选框
				var radioVal=data.$field.fieldName;
				if(radioVal.length>0){
					$("input[name='$field.fieldName'][value="+radioVal+"]").attr("checked","checked");
				}				
			#end
		#end
	#end
	
	//数据加载完毕才能让按扭可用
	$(".btDisabled").removeAttr("disabled");
	$(".btDisabled").removeClass("btDisabled");
	});	
}

</script>
</html>