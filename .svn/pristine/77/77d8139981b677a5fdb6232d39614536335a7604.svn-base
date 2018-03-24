<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/crm_operation.js"></script>
<style type="text/css">
.inputbox li{width:380px;padding-top:2px;height: 22px;}
.inputbox li font{float:left;background-color:#F5F5F5;width:190px;height:18px;padding-left:3px;}
select {height: 22px;}
@media print {
	#navigation{display: none;}
}
</style>
<script type="text/javascript">
	$(function() {
  		$(".clientDiv font").attr("class","notNull");
  		$("#clientTitle").html($('#head1').val());
  		$("#contactTitle").html($('#head2').val());
  		
  		$("input").css("backgroundColor","gainsboro");
  		$("input").css("border","none");
  		$("input").attr("readonly","true");
  		$("select").attr("disabled","true");
  		
  		if($("#linkMan").val() == "true"){
  			showInfo('contact');
  		}else{
  			var parentHeight = parent.document.getElementById("tagContent");
			var winHeight = parseInt(parentHeight.style.height.substring(0,parentHeight.style.height.length-2)) -70;
	  		$("#contentDiv").css("height",winHeight+"px");
	  		if(parent.document.getElementById("nowHead").value == ""){
	  			$("#nowHead").val("1");
	  		}else{
	  			$("#nowHead").val(parent.document.getElementById("nowHead").value);
	  			showInfo('contact');
	  		}
  		}
	});
	
	function update(keyId,viewId,contactFlag){
		parent.updateClient(keyId,viewId,$("#nowHead").val(),contactFlag)
	}
	
	
	function upload(type){
		openAttachUpload(type);
		return;	
	}
	
	var curUploadType = "";
	function openAttachUpload(type){
		curUploadType = type;
		var filter = "";
		if(type == "PIC"){
			filter = "Image";
		}
		
		var attachUpload = document.getElementById("attachUpload");
		if(attachUpload == null){
			uploadDiv = document.createElement("div"); 
			uploadDiv.id = "attachUpload" ;
			uploadDiv.style.cssText = "position:absolute; top:10px;left:30px; width=600px;height:300px; display:block";
			document.body.appendChild(uploadDiv);
			attachUpload = document.getElementById("attachUpload");
		}
		var clientHeight = document.documentElement.clientHeight;
		if(clientHeight==0) {
			clientHeight = document.documentElement.clientHeight ;
		}
		attachUpload.style.left=  ((document.documentElement.clientWidth - 500) /2) +"px";
		attachUpload.style.top=  ((clientHeight - 250) /2) +"px";
		attachUpload.style.display="block";
		attachUpload.innerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="fileUpload" width="500" height="250" codebase="http:/'+'/fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">'+
				' <param name="movie" value="/flash/FileUpload.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#869ca7" /><param name="flashvars" value="maxSize=$!globals.getSysSetting("defaultAttachSize")&uploadUrl=/UploadServlet;jsessionid=$session.id?path=/temp/@amp;fileType='+type+'&filter='+filter+'" />'+
				' <param name="allowScriptAccess" value="sameDomain" /><embed src="/flash/FileUpload.swf" quality="high" bgcolor="#869ca7"	width="500" height="250" name="column" align="middle" play="true" loop="false"'+
				'	flashvars="maxSize=$!globals.getSysSetting("defaultAttachSize")&uploadUrl=/UploadServlet;jsessionid=$session.id?path=/temp/@amp;fileType='+type+'&filter='+filter+'"	quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http/'+'/www.adobe.com/go/getflashplayer"></embed></object>';
		} 
		
		
		function onCompleteUpload(retstr){  
		
		retstr = decodeURIComponent(retstr);  
		if(curUploadType == 'PIC'){
		   mstrs = retstr.split(";");
		   for(i=0;i<mstrs.length;i++){
			   str = mstrs[i];
			   if(str == "") continue;
			   var ul=m('picuploadul');
			   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadpic value='"+str+"'><div><a href=\"/ReadFile?fileName="+str+"&realName="+encodeURI(str)+"&tempFile=true&type=PIC\" target=\"_blank\"><img src=\"/ReadFile.jpg?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=true&type=PIC\"  width=\"150\"  border=\"0\"></a></div><div>"+str+"&nbsp;&nbsp;&nbsp;&nbsp;<a style=\"cursor:pointer;\" onclick='deleteupload(\""+str+"\",\"true\",\"$tableName\",\"PIC\");'>$text.get("common.lb.del")</a></div></li>";
			   ul.innerHTML = ul.innerHTML+imgstr;
		   }
		}else if(curUploadType == 'AFFIX'){
			mstrs = retstr.split(";");
		    for(i=0;i<mstrs.length;i++){
			   str = mstrs[i];
			   if(str == "") continue;
			   var ul=m('affixuploadul');
			   //var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadaffix value='"+str+"'><div class='showAffix'>"+str+"</div>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"true\",\"$tableName\",\"AFFIX\");'>$text.get("common.lb.del")</a>&nbsp;&nbsp;&nbsp;<a href=\"/ReadFile?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=true&type=AFFIX\" target=_blank>$text.get("common.lb.download")</a></li>";
			   var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name=uploadaffix value='"+str+"'><div class='showAffix'>"+str+"</div>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#\" onclick='javascript:delUpload(\""+str+"\",this);'>$text.get("common.lb.del")</a>&nbsp;&nbsp;&nbsp;<a href=\"/ReadFile?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=false&type=AFFIX&tableName=CRMClientInfo\" target=_blank>$text.get("common.lb.download")</a></li>";
			   ul.innerHTML = ul.innerHTML+	imgstr;
		    }
		    
		    if(retstr != ""){
		    	var oldAffix ="";
				jQuery("#clientAffixDiv li div[class='showAffix']").each(function(){
					oldAffix += jQuery(this).text() +";";
				})
			    //异步上传附件
			    jQuery.ajax({
				   type: "POST",
				   url: "/CRMClientAction.do?operation=54&affix="+encodeURI(encodeURI(retstr))+"&clientId="+$("#clientId").val() + "&oldAffix="+encodeURI(encodeURI(oldAffix)),
				   success: function(msg){
				   		
				   }
				});
				if($("#noAffix").length == 1){
					$("#noAffix").remove();
				}
		    }
		}    
	    var attachUpload = document.getElementById("attachUpload");
		attachUpload.style.display="none";
	}
	
	function m(value){
		return document.getElementById(value) ;
	}
		
	//详情删除方法	
	function delUpload(fileName,obj){
		if(!confirm('$text.get("common.msg.confirmDel")')){
			return;
		}
		var allAffix ="";
		jQuery("#clientAffixDiv li div[class='showAffix']").each(function(){
			if(fileName != jQuery(this).text()){
				allAffix += jQuery(this).text() +";";
			}
		})
		jQuery("#affixuploadul li div[class='showAffix']").each(function(){
			if(fileName != jQuery(this).text()){
				allAffix += jQuery(this).text() +";";
			}
		})
	 	//异步删除附件
	    jQuery.ajax({
		   type: "POST",
		   url: "/CRMClientAction.do?operation=2&type=affixUpdate&affix="+encodeURI(encodeURI(allAffix))+"&clientId="+$("#clientId").val(),
		   success: function(msg){
		   }
		});
		$("#affix li[id='uploadfile_"+fileName+"']").remove();
	}	
	
	
	//显示编辑图标
	function showEdit(obj,isContact,contactId){
		$("img[id='imgEdit']").remove();
		var str = '<img src="/style/images/plan/Edit_icon.gif" width="14px" height="14px" id="imgEdit" style="cursor: pointer;" title="修改"';
		if(isContact == "true"){
			str +='onclick="updFieldInfo(this,\'true\',\''+contactId+'\')">'
		}else{
			str +='onclick="updFieldInfo(this,\'false\',\'\')">'
		}
		$(obj).after(str);
	}
	
	//还原
	function restore(obj){
		$(obj).before($copyField);
		$(obj).next().remove();
		$(obj).remove();
	}
	
	//编辑字段
	var $copyField;
	function updFieldInfo(obj,isContact,contactId){
		
		if($("#submitBtn").length == 1){
			if($("#submitBtn").attr("fieldName") == "Trade" || $("#submitBtn").attr("fieldName") == "District" || $("#submitBtn").attr("inputType") == "14" || $("#submitBtn").attr("inputType") == "15" ){
				$("#submitBtn").prev().remove();
			}
		    $("#submitBtn").prev().before($copyField);
			$("#submitBtn").prev().remove();
			$("#submitBtn").next().remove();
			$("#submitBtn").remove();
		}
	
		var $edit =$(obj).prev()
		$copyField = $edit.clone(true);
		var $str="";
		var submitClick = "";
		if(isContact == "true"){
			submitClick = "submitField('"+$edit.attr("fieldName") + "','"+$edit.attr("inputType")+"','true','"+contactId+"',this)";
		}else{
			submitClick = "submitField('"+$edit.attr("fieldName") + "','"+$edit.attr("inputType")+"','false','',this)";
		}
		var $submitBtn = '<input id="submitBtn" fieldName="'+$edit.attr("fieldName")+'" inputType="'+$edit.attr("inputType")+'"  type="button" value="保存" style="float:left;margin-left:2px;" onclick="'+submitClick+'"><input id="submitBtn"  type="button" fieldName="'+$edit.attr("fieldName")+'" inputType="'+$edit.attr("inputType")+'" value="取消" style="float:left;margin-left:5px;" onclick="cancelField(this)">'
		if($edit.attr("inputType") == 0){
			if($edit.attr("fieldName") == "BriefContent"){
				$str +='<textarea rows="6" cols="97" name="'+$edit.attr("fieldName")+'"  id="'+$edit.attr("fieldName")+'" style="float:left;">'+$edit.attr("fieldVal")+'</textarea>';
			}else{
				$str = '<input name="'+$edit.attr("fieldName")+'"  id="'+$edit.attr("fieldName")+'" type="text" value="'+$edit.attr("fieldVal")+'" class="inp_w'; 
				if($edit.attr("fieldType") == 2){
					if($edit.attr("fieldName") == "Trade"){
						$str += '" style="border-right: 0;width: 173px;" ondblclick="openTrade()"/><a href="#" onclick="openTrade()"  style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%"></a>'
					}else if($edit.attr("fieldName") == "District"){
						$str += '" ondblclick="openDistrict()" style="border-right: 0;width: 168px;"/><a href="#" onclick="openDistrict()"  style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%;"></a>'
					}else{
					
					}
				}else if($edit.attr("fieldType") == 5){
					$str += ' inp_date" onClick="WdatePicker({lang:\'$globals.getLocale()\'})" ';
				}else if($edit.attr("fieldType") == 6){
					$str += ' inp_date" onClick="WdatePicker({lang:\'$globals.getLocale()\',dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" ';
				}
				if($edit.attr("fieldName") != "Trade" && $edit.attr("fieldName") != "District"){
					if($edit.attr("fieldType") == 18){
						$str += '" style="width: 567px;"';
					}else{
						$str += '" style="width: 190px;"';
					}
					$str +='/>';
				}
			}
		}else if($edit.attr("inputType") == 1){
			var $enumer = $("#" + $edit.attr("fieldName") +"_enum").clone(true);
			$edit.before($enumer);
			$edit.prev().removeProp("disabled");
			$edit.prev().attr("name",$edit.attr("fieldName"));
			$edit.prev().attr("id",$edit.attr("fieldName"));
		}
		$(obj).prev().before($str+$submitBtn);
		$("#"+$edit.attr("fieldName")).select();
		$edit.remove();
		$("img[id='imgEdit']").remove();
	}
	
	//编辑取消
	function cancelField(obj){
		if($(obj).attr("fieldName") == "Trade" || $(obj).attr("fieldName") == "District" || $(obj).attr("inputType") == "14" || $(obj).attr("inputType") == "15" ){
			$(obj).prev().prev().remove();
		}
	    $(obj).prev().prev().before($copyField);
		$(obj).prev().prev().remove();
		$(obj).prev().remove();
		$(obj).remove();
	}
	
	//异步提交
	function submitField(fieldName,inputType,isContact,contactId,obj){
		if(fieldName == "Email" || fieldName == "ClientEmail" ){
			if(!isMail($("#" + fieldName).val())){
				alert("请输入正确的邮箱地址");
				$(obj).prev().select();
				return false;
			}
		}else if(fieldName == "ClientName" || fieldName == "UserName"){
			if(jQuery.trim($("#" + fieldName).val()) == ""){
				alert("内容不能为空,请输入");
				$(obj).prev().select();
				return false;
			}
		}
		var id="";
		if(isContact == "true"){
			id = contactId;
		}else{
			id = $("#clientId").val();
		}
		var $fieldValue = "";
		var enumVal ="";
		if(fieldName == "Trade" || fieldName == "District"){
			$fieldValue = $("#" + fieldName + "Id").val();
		}else{
			if(inputType == 1){
				enumVal = $("#" + fieldName).val();
				$fieldValue = enumVal.split(":")[0];
			}else{
				$fieldValue = $("#" + fieldName).val();
			}
		}
	    jQuery.ajax({
		   type: "POST",
		   url: "/CRMClientAction.do?operation=2&type=detailUpdate&moduleId=$!moduleId&viewId=$!viewId&clientId=$!keyId&id="+id + "&fieldName="+fieldName+"&isContact="+isContact,
		   data: "fieldValue="+encodeURI(encodeURI($fieldValue)),
		   success: function(msg){
		   		if(msg == "yes"){
		   			alert("您没有更新操作，不需要保存");
		   		}else{
		   			if(fieldName == "Trade" || fieldName == "District"){
		   				var fieldVal = $("#"+fieldName).val()
		   				$(obj).prev().prev().before($copyField);
			   			$(obj).prev().prev().prev().attr("fieldVal",fieldVal)
			   			$(obj).prev().prev().prev().text(fieldVal);
			   			$(obj).prev().prev().remove();
		   			}else{
		   				$(obj).prev().before($copyField);
		   				$(obj).prev().prev().attr("fieldVal",$fieldValue)
		   				if(inputType == 1){
			   				$(obj).prev().prev().text(enumVal.split(":")[1]);
		   				}else{
			   				$(obj).prev().prev().text($fieldValue);
		   				}
		   			}
		   			$(obj).prev().remove();
		   			$(obj).next().remove();
			   		$(obj).remove();
		   		}
		   }
		});
		if($("#noAffix").length == 1){
			$("#noAffix").remove();
		}
	}
	
//行业弹出框

function openTrade(){
	var height = parseInt($("#contentDiv").css("height").substring(0,$("#contentDiv").css("height").length-2)) + 26;
	if(height > 540){
		height = 540;
	}
	asyncbox.open({
		id : 'crmOpenId',
　　　   	url : '/CRMopenSelectAction.do',
	 	title : '行业',
　　 　 	width :840,
　　 　		height : height,
　   });
}
	
//行业弹出框处理函数

function dealTrade(professionId,professionName){
	$("#TradeId").val(professionId);
	$("#Trade").val(professionName);
}

//地区弹出框

function openDistrict(){
	asyncbox.open({
		id:'crmOpenId',url : '/CRMopenSelectAction.do?operation=4',
	 	title:'选择地区',width :610,height : 450,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
		    if(action == 'ok'){
		    	var info = opener.returnVal();
		    	if(info == ","){
		    		alert("必须选择一个");
		    		return false;
		    	}
		    	dealDistrict(info);
		   	}
　　  	}
　    });
}

//地区弹出框处理函数

function dealDistrict(districtInfo){
	var info = districtInfo.split(",");
	jQuery("#DistrictId").val(info[1]);
	jQuery("#District").val(info[0]);
}

//视图切换,暂未使用
function changeView(moduleId,viewId){
	window.location.href="/CRMClientAction.do?operation=5&tableName=$!tableName&contactTableName=$!contactTableName&keyId=$!keyId&type=contact&moduleId="+moduleId+"&viewId="+viewId
}

//处理详情页面共享双击函数
function fillData(datas){
	jQuery.opener('dealdiv').evaluate(datas);
	jQuery.close('Popdiv');
}

function printClient(){
	$("img[id='imgEdit']").remove();
	var contentDivHeight = $("#contentDiv").css("height");//原来的高度

	var contactDisplay =$(".contactDiv").css("display");//获取联系人是否显示


	$("#contentDiv").css("height","100%");
	$(".contactDiv").show();
	$(".clientDiv").show()
	window.print();//打印
	if(contactDisplay == "none"){
		//如果原来的联系人隐藏,设置为隐藏

		$(".contactDiv").hide()
	}else{
		//客户隐藏
		$(".clientDiv").hide();
	}
	$("#contentDiv").css("height",contentDivHeight);
}

function dealAsyn(){
	jQuery.close("CareforPopup");
	window.frames["careforIframe"].location.reload();
}

function flowDepict(){ 
	window.open("/OAMyWorkFlow.do?keyId=$!keyId&operation=5&applyType=$!designId",null,"height=570, width=1010,top=50,left=100 ");
}

function checkDialog(url){
	var urls = url+"&checkFlag=true";
	asyncbox.open({
		id:'dealdiv',url:urls,
	 	title:'审核',width:650,height : 370,
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

function dealCheck(){
	jQuery.close('dealdiv');
	window.location.href = '/CRMClientAction.do?operation=5&type=contact&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&moduleId=$!result.get("ModuleId")&viewId=$!viewId&keyId=$!keyId';
}



</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body> 
<form name="form" action="/CRMClientAction.do" method="post" target="formFrame" onsubmit="return beforeSubmit();">
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')"/>
<input type="hidden" name="clientDetCou" id="clientDetCou" value="2"/>
<input type="hidden" name="clientCols" id="clientCols" value="$!clientRecords"/>
<input type="hidden" name="contactCols" id="contactCols" value="$!contactRecords" />
<input type="hidden" name="isAttachment" id="isAttachment" value="$!attachment" />
<input type="hidden" name="tableName" id="tableName" value="$!tableName" />
<input type="hidden" name="contactTableName" id="contactTableName" value="$!contactTableName" />
<input type="hidden" name="clientId" id="clientId" value="$!keyId"/>
<input type="hidden" id="nowHead" value=""/>
<input type="hidden" id="linkMan" value="$!linkMan"/>
<input type="hidden" id="detailPage" value="true"/>
<div class="boxbg2 subbox_w700" style="margin: 0 auto;margin-top:5px;width: 810px;height: 100%;" >
<div class="subbox cf" style="border: 1px solid #dedede;">
  <div class="operate operate2" id="navigation">
  <ul>
  #if("$!linkMan" != "true")<li id="clientHead" class="sel" ><a href="#" onclick="showInfo('client')" ><span id="clientTitle"></span></a></li>#end
  <li id="contactHead"><a href="#" onclick="showInfo('contact')" >联系人信息</a></li>
   #if("$!linkMan" != "true")<li id="affixHead" ><a href="#"  style="#if("$!isAttachment" != "true")display: none; #end" onclick="showInfo('addfix')" >附件管理</a></li>#end
  <li style="float: right;margin-top: -3px;">
  #if("$!moduleBean.workflow" == "1" && "$!designBean"!="")
  	#if($!result.get("checkPersons").indexOf(";$!LoginBean.id;")!=-1)
  	<div class="op" style="margin: 0px;margin-left: 10px;width: 50px;"><span class="a"></span><a onclick="checkDialog('/OAMyWorkFlow.do?keyId=$!keyId&tableName=CRMClientInfo&operation=82')">审核</a><span class="c" style="margin-top: -22px;"></span></div>
  	#end
  	<div class="op" style="margin: 0px;margin-left: 10px;width: 55px;"><span class="a"></span><a onclick="flowDepict()">流程图</a><span class="c" style="margin-top: -22px;"></span></div>
  #end
  #if("$!printFlag" == "true")<div class="op" style="margin: 0px;margin-left: 10px;width: 60px;"><span class="a"></span><a onclick="printClient()">打印</a><span class="c" style="margin-top: -22px;"></span></div>#end
  #if($LoginBean.operationMap.get("/CRMClientAction.do").update())
  <div class="op" style="margin: 0px;margin-left: 10px;width: 70px;"><span class="a"></span><a onclick="shareClient('$!keyId','$!result.get('ModuleId')','$!viewId','detail','dealdiv')">客户共享</a><span class="c" style="margin-top: -22px;"></span></div>
  <div class="op" style="margin: 0px;margin-left: 10px;width: 70px;"><span class="a"></span><a onclick="update('$!keyId','$!viewId','true')">新增联系人</a><span class="c" style="margin-top: -22px;"></span></div>
  #end
  #if($LoginBean.operationMap.get("/CRMClientAction.do").update())#if("$!linkMan" != "true")<div class="op" style="margin: 0px 0px 0px 10px;width: 60px;"><span class="a"></span><a onclick="update('$!keyId','$!viewId','false')">修改</a><span class="c" style="margin-top: -22px;"></span></div>#end#end
  <div class="op" style="margin: 0px;margin-left: 10px;width: 70px;"><span class="a"></span><a onclick="parent.history('$!keyId')">历史日志</a><span class="c" style="margin-top: -22px;"></span></div>
  </li>
  </ul>
  </div>
  
  <div class="bd" style="overflow-y: auto;" id="contentDiv" >
  	#if($LoginBean.operationMap.get("/CRMClientAction.do").update())
  		#set($editFunction = "onmouseover='showEdit(this)'" )
    #else
   	    #set($editFunction = "" )
 	#end	
      <div class="inputbox">
      	#set($firstEnter = "true")
      	#foreach ($maps in $clientMap.keySet())	
	      	#if($clientMap.get($maps).size() != 0)	
	        <div class="clientDiv">
	        	#if("$firstEnter" == "true")<input type="hidden" id="head1" value="$maps"/>#set($firstEnter = "false")#else<h4> $maps </h4>  #end
		        <ul>
		        #foreach ($row in $clientMap.get($maps))
		        	#set($readExist = $globals.isExist("$!readStr",",${row.fieldName},"))
		        	#if("$row.inputType" == "0")
		        		 #if("$row.fieldType" == "18" || "$row.fieldName" == "ClientRemark")
		        		 	<li style="width: 750px;" ><span >$row.display.get("$globals.getLocale()")：</span><font style="width: 570px;" id="$row.fieldName" fieldName="$row.fieldName" inputType="$row.inputType" fieldType="$row.fieldType" fieldVal="$!result.get($row.fieldName)" #if("$!readExist" != "true") $editFunction #end>$!result.get($row.fieldName)</font> </li>
		        		 #elseif("$row.fieldName" == "BriefContent" || "$row.fieldType" == "3")
		        		 	<li style="width: 750px;margin-bottom: 3px;height: 100px;"><span>$row.display.get("$globals.getLocale()")：</span><div style="height: 101px;width:573px; float: left;overflow: auto;background-color:#F5F5F5;" fieldName="$row.fieldName" inputType="$row.inputType" fieldType="$row.fieldType" fieldVal="$globals.replaceHTML($!result.get($row.fieldName))" #if("$!readExist" != "true") $editFunction #end>$globals.replaceHTML($!result.get($row.fieldName))</div></li>
		        		 #else
		        		 	#if("$row.fieldName" == "Attachment")
		        		 	
		        		 	#elseif("$row.fieldName" == "Trade")
		        		 		<input type="hidden" value="$!result.get($row.fieldName)" id="TradeId" name="TradeId"/>
		        		 		<li><span>$row.display.get("$globals.getLocale()")：</span><font id="$row.fieldName" fieldName="$row.fieldName" inputType="$row.inputType" fieldType="$row.fieldType" fieldVal="$!workProfessionMap.get($!result.get($row.fieldName))" #if("$!readExist" != "true") $editFunction #end>$!workProfessionMap.get($!result.get($row.fieldName))</font></li>
		        		 	#elseif("$row.fieldName" == "District")
		        		 		<input type="hidden" value="$!result.get($row.fieldName)" id="DistrictId" name="DistrictId"/>
		        		 		<li><span>$row.display.get("$globals.getLocale()")：</span><font id="$row.fieldName" fieldName="$row.fieldName" inputType="$row.inputType" fieldType="$row.fieldType" fieldVal="$!districtMap.get($!result.get($row.fieldName))" #if("$!readExist" != "true") $editFunction #end>$!districtMap.get($!result.get($row.fieldName))</font></li>
		        		 	#else
		        		 		<li><span>$row.display.get("$globals.getLocale()")：</span><font id="$row.fieldName" fieldName="$row.fieldName" inputType="$row.inputType" fieldType="$row.fieldType" fieldVal="$!result.get($row.fieldName)" #if("$!readExist" != "true") $editFunction #end>$!result.get($row.fieldName)</font></li>
		        		 	#end
		        		 #end
		        	#elseif("$row.inputType" == "1")
		        		 <div style="display: none;">
 		        		 	<select  name="${row.fieldName}_enum"  id="${row.fieldName}_enum"  enumerName="$row.refEnumerationName" style="float: left;">
				             	 	<option value=" : "></option> 
				             	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
				        	        <option value="$item.value:$item.name" #if("$!result.get($row.fieldName)" == "$item.value") selected="selected" #end>$item.name</option>
						         #end
     			             </select>	
		        		 </div>
		        		 <li>
		        		 	 <span>$row.display.get("$globals.getLocale()")：</span>
				    	 	 <font id="$row.fieldName" fieldName="$row.fieldName" inputType="$row.inputType" fieldType="$row.fieldType" fieldVal="$!result.get($row.fieldName)" #if("$!readExist" != "true") $editFunction #end>$globals.getEnumerationItemsDisplay("$row.refEnumerationName","$result.get($row.fieldName)")</font>
				         </li>	
				     #elseif("$row.inputType" == "2")
				     		#set($disNames = "")
				    		#set($selectName="popup_"+$row.fieldName)
				    		#set($disNames = $!defineDisMap.get("$selectName"))    
				     		 <li style="width: 750px;"><span>$row.display.get("$globals.getLocale()")：</span><font style="width: 570px;" id="$row.fieldName" title="$disNames">$disNames</font></li>
				     #elseif("$row.inputType" == "14")
				     		 <li style="width: 750px;"><span>$row.display.get("$globals.getLocale()")：</span><font style="width: 570px;" id="$row.fieldName" title="#foreach($row in $!result.get($row.fieldName).split(","))#if("$row" != "")$deptMap.get("$row"),#end#end">#foreach($row in $!result.get($row.fieldName).split(","))#if("$row" != "")$deptMap.get("$row"),#end#end</font></li>
				     #elseif("$row.inputType" == "15")
				    	    <li style="width: 750px;"><span>$row.display.get("$globals.getLocale()")：</span><font style="width: 570px;" id="$row.fieldName" title="#foreach($row in $!result.get($row.fieldName).split(","))#if("$row" != "")$globals.getEmpFullNameByUserId($row),#end#end">#foreach($row in $!result.get($row.fieldName).split(","))#if("$row" != "")$globals.getEmpFullNameByUserId($row),#end#end</font></li>
				     #elseif("$row.inputType" == "5")
				    	 <li style="width: 670px;">
				    	 	 <span>$row.display.get("$globals.getLocale()")：</span>
				    	 	 	 <font id="$row.fieldName" style="width: 515px;">
			        		 	 #foreach($item in $!result.get($row.fieldName).split(","))
			        		 		$globals.getEnumerationItemsDisplay("$row.refEnumerationName","$item"),
			        		 	 #end
			        		 	 </font>
				         </li>	     
				    #else
				    	<li>
							<span>$row.display.get("$globals.getLocale()")：</span><font id="$row.fieldName">$!result.get($row.fieldName)</font>   	
		        		</li>	
		        	#end
				#end
		        </ul>
	        </div>
		    #end
        #end
        #if("$globals.getSysSetting('careforAction')"=="true")
		<div style="margin-left:3px;float:left;margin-top:3px;padding-bottom:3px;width:99%;border:2px solid #DAEDFE; overflow-x:hidden;overflow-y:auto;">
		<iframe name="careforIframe" src="/CareforExecuteAction.do?operation=4&type=iframe&clientId=$!keyId" width="100%" height="200" scrolling="no"  frameborder=false onload="this.height=careforIframe.document.body.scrollHeight;this.width=careforIframe.document.body.scrollWidth;"></iframe>
		</div>
		#end 
        <div class="contactDiv" style="display: none; ">
        	#if($!result.get("TABLENAME_${conTableName}").size() != 0)
			       	#foreach ( $det in $!result.get("TABLENAME_${conTableName}") ) 
				       		#foreach ($maps in $contactMap.keySet())	
					       		#if($contactMap.get($maps).size() !=0)
					       		#if($LoginBean.operationMap.get("/CRMClientAction.do").update())
							  		#set($editFlag = "true" )
							    #else
							   	    #set($editFlag  = "false" )
							 	#end
					       		<div id="contacts">
						        <h4 style="height: 13px;"><span style="float: left;">主联系人:#if($!det.get("mainUser") == "1") 是 #else 否 #end </span> </h4>
						        <ul>
						        #foreach ($row in $contactMap.get($maps))
						        	#set($readExist = $globals.isExist("$!readStr",",contact${row.fieldName},"))
						        	#if("$row.fieldName" != "mainUser")
							        	#if("$row.inputType" == "1")
							        		 <div style="display: none;">
					 		        		 	<select  name="${row.fieldName}_enum"  id="${row.fieldName}_enum"  enumerName="$row.refEnumerationName" style="float: left;">
													 <option value=" : "></option>
									             	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
									        	        <option value="$item.value:$item.name" #if("$!result.get($row.fieldName)" == "$item.value") selected="selected" #end>$item.name</option>
											         #end
					     			             </select>	
							        		 </div>
							        		<li><span>$row.display.get("$globals.getLocale()")：</span><font fieldName="$row.fieldName" inputType="$row.inputType" fieldType="$row.fieldType" fieldVal="$det.get($row.fieldName)" #if("$readExist" != "true") onmouseover="showEdit(this,'true','$!det.get("id")')" #end>$globals.getEnumerationItemsDisplay("$row.refEnumerationName","$det.get($row.fieldName)")</font></li>
							        	#else
							        		<li><span>$row.display.get("$globals.getLocale()")：</span><font fieldName="$row.fieldName" inputType="$row.inputType" fieldType="$row.fieldType" fieldVal="$det.get($row.fieldName)" #if("$readExist" != "true") onmouseover="showEdit(this,'true','$!det.get("id")')" #end>$!det.get($row.fieldName)</font></li>	
							        	#end
						        	#end
								#end
						        </ul>
						        </div>
						    	#end
					    	#end
					    #end  
        	#else
        		<span style="color: gray;float: left;margin-left: 300px;margin-top: 20px;">暂无联系人</span>
        	#end
	   		<p></p>
   		</div>
   		 <div id="affixDiv" style="display: none;">
   		 	#if($LoginBean.operationMap.get("/CRMClientAction.do").update())<button name="affixbuttonthe222" type="button" onClick="upload('AFFIX');" class="b4">$text.get("upload.lb.affixupload")</button>#end
	       	<div class="listRange_1_photoAndDoc_1" style="height: 375px;overflow-y: auto;">
				<ul id="affix" >
					#if($AFFIX.size() != 0 || $broAttachments.keySet().size()!=0)
						<div id="clientAffixDiv" >
							#if($AFFIX.size() != 0)<h4 style="height: 10px;margin-left: -10px;margin-top: 10px;">客户资料</h4>#end
							#foreach($uprow in $AFFIX)	
								#if($uprow != "")
								<li style='background:url();' id="uploadfile_$uprow">
								<input type=hidden id="uploadaffix" name=uploadaffix value='$uprow'/><div class="showAffix">$uprow</div>&nbsp;&nbsp;&nbsp;			
									<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=CRMClientInfo" target="_blank">$text.get("common.lb.download")</a>
								</li>
								#end
							#end
						</div>
						<ul id="affixuploadul" style="width: 100%;margin-left: -9px;"></ul>
						#foreach ($maps in $broAttachments.keySet())	
							<h4 style="height: 10px;margin-left: -10px;">$broNameMap.get($maps)</h4>
							#foreach ($uprow in $broAttachments.get($maps))	
							<li style='background:url();' id="uploadfile_$uprow">
							<input type=hidden id="uploadaffix" name=uploadaffix value='$uprow'/><div class="showAffix">$uprow</div>&nbsp;&nbsp;&nbsp;			
								<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=CRMClientInfo" target="_blank">$text.get("common.lb.download")</a>
							</li>
							#end
						#end
					#else
						<ul id="affixuploadul" style="width: 100%;margin-left: -9px;"></ul>
						<span style="color: gray;float: left;margin-left: 300px;margin-top: 20px;" id="noAffix">暂无附件</span>
					#end
				</ul>
			</div>

   		</div>
      </div>
     
  </div>
</div>
</div>
</form>
<script language="javascript"> 
	function keyDown(e) { 
		var iekey=event.keyCode; 
		if(iekey == 13){
			return false;
		}
	} 
	document.onkeydown = keyDown; 
</script>
</body>
</html>
