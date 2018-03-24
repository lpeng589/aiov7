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
<script type="text/javascript" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" src="/js/lang/${globals.getLocale()}.js"></script>
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

	if(!checkTable(allHidFields)){  //验证表单设计中设置的必填字段
		return false;
	}
	return true;
}

function deliverTo(url,retValUrl){
	$("#retValUrl").val(retValUrl);
	asyncbox.open({
		id : 'dealdiv',
　　　   	url : url,
	 	title : '转交',
　　 　 	width : 650,
　　 　	height : 370,
        btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    if(action == 'ok'){
			if(opener.beforSubmit(opener.form)){
				opener.form.submit();
			}
			return false;
		}else if(action == 'close'){
			window.location.href = retValUrl ;
		}else if(action == 'cancel'){
			window.location.href = retValUrl ;
		}
　　  　}
　 });
}
function flowDepict(applyType,keyId){ 
	var winWidth = 1024;
	if($(window).width()>1024){
		winWidth = 1150;
	}
	window.open("/OAMyWorkFlow.do?keyId=$!keyId&operation=$globals.getOP("OP_DETAIL")&applyType="+applyType,null,"height=570, width="+winWidth+",top=50,left=100 ");
}

function dealAsyncbox(){
	var retValUrl = $("#retValUrl").val();
	window.location.href = retValUrl;
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

<body onload="getFlowField();showStatus();" >

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form" name="form"  action="/OAWorkFlowAction.do?operation=$globals.getOP("OP_ADD")" onSubmit="return beforeSubmit(this);" target="formFrame">
<input type="hidden"  name="designId" id="designId" value="$!designId"/>
<input type="hidden" id="button" name="button" value="" />
<input type="hidden" id="noback" name="noback" value="$!noback">
<input type="hidden"  name="tableName" id="tableName" value="$!tableInfo.tableName" />
<input type="hidden" name="retValUrl"  id="retValUrl" value=""/>
<div class="Heading">
#if("$!addType" != "viewFlow") <!--判断是否是表单预览 -->
	<div class="HeadingIcon" ><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$!tableCHName</div>
	<ul class="HeadingButton">
		<li><button type="button" id="deliverToBut" name="deliverToBut" title="Ctrl+S" onClick="if(beforeSubmit(document.form)) {form.button.value='deliverTo';window.save=true; document.form.submit();}" class="b2">保存</button></li>
		<li><button type="button" id="copySave" name="copySave" title="Ctrl+S" onClick="if(beforeSubmit(document.form)){form.button.value='';window.save=true; document.form.submit();}" class="b2">存为草稿</button></li>
		<li><button type="button" id="backList"  name="backList" title="Ctrl+Z" onClick="closeWindows();" class="b2">$text.get("common.lb.close")</button></li>
		<li><button type="button" onClick="javascript:flowDepict('$!designId','');">$text.get("common.lb.checkFlowChart")&nbsp;</button></li>
	</ul>
	</div> 
#end
	<div id="listRange_id">
	<script type="text/javascript">
		$("#listRange_id").height(document.documentElement.clientHeight-32);
	</script>
	
		<div class="listRange_1" >
			<div class="secondDiv" >
				<div class="thirdDiv">
					$!layOutHtml
				</div>
			</div>
		</div>
	</div>
</form>
</body>
<script>

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
							$("img[name=$!field.fieldName]").css("display","none");
						#elseif("$!inputType"=="8") // 只读
							$("input[name='"+labelId+"']").removeAttr("onclick").removeAttr("ondblclick").attr("readonly","readonly").css("color","#b1b1c1");
							$("img[name=$!field.fieldName]").removeAttr("onclick").css("color","#b1b1c1");
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
				
				#if($field.defaultValue == "textarea")
					#if("$!inputType"=="3") // 隐藏
						allHidFields+="$field.fieldName"+";";
						$("textarea[name='$field.fieldName']").css("display","none");
					#elseif("$!inputType"=="8") // 只读
						$("textarea[name='$field.fieldName']").attr("readonly","readonly").css("color","#b1b1c1");
					#end
				#elseif($field.defaultValue == "html")
					var editor=$field.fieldName;
					#if("$!inputType"=="3") // 隐藏
						allHidFields+="$field.fieldName"+";";
						$("textarea[name='$field.fieldName']").prev().css("display","none");
					#elseif("$!inputType"=="8") // 只读
						if(typeof(editor)!="undefined"){
							editor.readonly();
						}
					#end
				#elseif("$field.defaultValue" == "checkbox" || "$field.defaultValue" == "radio" )
					#if("$!inputType"=="3") // 隐藏
						allHidFields+="$field.fieldName"+";";
						$("#$field.defaultValue$field.fieldName").css("display","none");
					#elseif("$!inputType"=="8") // 只读
						$("input[name='$field.fieldName']").each(function(){
							if($(this).attr("checked")!="checked"){
								$(this).attr("disabled","disabled");
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
					if(labelText.length == 0 && "$!inputType"!="3" && "$!inputType"!="8"){ //如果设置不为空的字段被隐藏或者只读了，则不需要提示

						alert("$!field.languageId"+"不能为空，请重新输入!");
						return false;
					}
				#end
			#end
		#end
	#end
	return true;
}

//初始化html编辑器


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
	//计算控件
	$(document).ready(function (){
		#foreach ($field in $tableInfo.fieldInfos)
			#if("$field.fieldIdentityStr"=="BillNo")
				#set($key=$tableInfo.tableName+"_"+$field.fieldName)
				$("#$field.fieldName").val("$!globals.getBillNoCode($key)");		
			#end
			#set($inputVal=$field.inputValue)
			#if("$field.defaultValue"=="count"  && "$!inputVal"!="")
				var docText="$!field.inputValue";
				docText=docText.replace("count","").replace("day","").replace("date","").replace("time","");
				var re=/{.[^{}]*}/g;  //正则公式获取标签title值


				var arr=docText.match(re);
				for(var i=0;i<arr.length;i++){
	    			var fieldName=arr[i].replace("{","").replace("}","");
	    			$("input[id='"+fieldName+"']").change(function(){
		    			var fieldVal=$("#"+fieldName).val();
		    			if(fieldVal!=""){
		    				docText=docText.replace("{"+fieldName+"}",fieldVal);
		    			}
	    			});
	    		}
	    		if(docText.indexOf("field_")>-1){
	    			return false;
	    		}
	    		
			#end
		#end
	});
	
</script>
</html>