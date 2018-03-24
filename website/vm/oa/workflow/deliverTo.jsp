<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("common.lb.DeliverTo")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<style type="text/css">
i{font-style:normal;}
.listRange_jag{padding:0px;float:left;}
.listRange_jag li{width:93%;margin:0px;padding:0px;margin-bottom:5px;float:left;}
.listRange_jag li span{width:80px;float:left;display:inline-block;text-align:right;}
.listRange_jag li button.b2{float:left;}
.listRange_jag li .in_radio{float:left;margin:5px 0 0 0;}
.listRange_jag li .lb_radio{float:left;}
.listRange_jag li .in_ft{float:left;line-height:19px;}
.listRange_jag li .cbox_li .lb_cbox{float:left;line-height:19px;}
.HeadingTitle div{ float:left;}
#files_preview div {float:left;display:inline-block;}
#files_preview div i{width:80px;display:inline-block;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;text-align:left;}

button.b4{float:left;margin:0 0 0 5px;}
</style>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript">
function $(obj){
	return document.getElementById(obj);
}	
//定义一个二维数组存放不同节点的审核人



var allNodeCheck=new Array();
#set($count=0)
#foreach($!nextNB in $!nextNBs)
#if($!nextNB.zAction.equals("CHECK")||"$!nextNB.keyId"=="back_0")
var nodeCheck$count=new Array();
allNodeCheck[$count]=nodeCheck$count;
nodeCheck$count[0]='$!nextNB.keyId';
nodeCheck$count[1]='$!nextNB.approvePeople';
nodeCheck$count[2]='$!nextNB.forwardTime';
#set($tempCount=3)
#foreach($!cp in $!nextNB.checkPeople)
#if($!globals.getEmployeeById($globals.get($!cp,0)).statusId!="-1")
nodeCheck$count[$tempCount]=new Array('$globals.get($!cp,0)','$globals.get($!cp,1)');
#set($tempCount=$tempCount+1)
#end
#end
#set($count=$count+1)
#end
#end

function changeStep(type){
	var flag=false;
	var nextSteps=document.getElementsByName("nextStep");
	//if(form.nextStep.length==undefined)return;	
	for(var i=0;i<nextSteps.length;i++){
		if(nextSteps[i].checked){
			for(var j=0;j<allNodeCheck.length;j++){
				var nodeCheck=allNodeCheck[j];
				if(nodeCheck[0]==nextSteps[i].value){
					if(type=="change"){
						document.getElementById("search").innerHTML="";
						if(nodeCheck.length>=23){
							document.getElementById("search").innerHTML="<span>$text.get("oa.myWorkflow.searchUser")：</span>"+
								"<input type='text' name='userName' value='' style='width:100px;height:18px;'>&nbsp;<button type='button' class='b4' onClick='javascript:changeStep();' value=''>GO</button>";						
						}
					}
					var cpHTML = "" ;
					if(nodeCheck.length>0){
						flag=true;
						cpHTML +="<span>$text.get("call.lb.transactor")：</span><ul style='float:left;width:450px;padding:0px;margin-top:5px;'>";
					}
					for(var k=3;k<nodeCheck.length;k++){
						if(nodeCheck[1]=="fix"||nodeCheck[0].indexOf("back")>=0){
							cpHTML += "<input type='hidden' name='checkPerson' value='"+nodeCheck[k][0]+"'>";
							cpHTML += "<li style=\"float:left;width: 70px;\"><input type='checkBox' style=\"width: 15px;border: 0px;\"  checked disabled='false'  name='checkPerson' value='"+nodeCheck[k][0]+"'>"+nodeCheck[k][1]+"</li>";
						}else{
							if(nodeCheck.length==4){
								cpHTML += "<li style=\"float:left;width: 70px;\"><input type='checkBox' style=\"width: 15px;border: 0px;\" checked name='checkPerson' value='"+nodeCheck[k][0]+"'>"+nodeCheck[k][1]+"</li>";
							}else{
								var flag2=true;
								if(form.userName!=undefined){
									var vals=form.userName.value.split(",");								
									if(vals.length>0){
										flag2=false;
										for(var f=0;f<vals.length;f++){
											if(nodeCheck[k][1].indexOf(vals[f])>=0){
												flag2=true;
												break;
											}
										}
									}
								}
								if(flag2){
									cpHTML += "<li class='cbox_li' style=\"float: left;width: 70px;\"><input type='checkBox' style=\"width: 15px;border: 0px;\" name='checkPerson' value='"+nodeCheck[k][0]+"' id='"+nodeCheck[k][0]+"'><label for='"+nodeCheck[k][0]+"' class='lb_box' style='float:left;line-height:19px;'>"+nodeCheck[k][1]+"</label>";							
								}
							}
						}
					}
					if(nodeCheck.length>0){
						cpHTML += "</ul>" ;
					}
					document.getElementById("cps").innerHTML = cpHTML ;
					var timeHtml = document.getElementById("timeLit") ;
					if(timeHtml!=null && typeof(timeHtml)!="undefined"){
						if(nodeCheck[2]=="true"){
							document.getElementById("timeLit").innerHTML="<span style='color:red;'>$text.get("reportServlet.lb.dealMaxtime")：</span><input type='text' style='width:40px;'  name='oaTimeLimit' value=''>"+
										"<select name='oaTimeLimitUnit' style='width:50px;float:left;margin:2px 0 0 5px;border:1px solid #8CBEDC;border-radius:2px;height:23px;'>"+
										"<option value='0'>$text.get("oa.calendar.day")</option>"+
										"<option value='1'>$text.get("oa.work.hour")</option>"+
										"<option value='2'>$text.get("common.msg.minute")</option>"+
										"</select>";
						}else{
							document.getElementById("timeLit").innerHTML="";
						}
					}
					break;
				}
			}
			break;
		}
	}
	
	if(!flag){
		document.getElementById("cps").innerHTML="";
		document.getElementById("timeLit").innerHTML="";
		document.getElementById("search").innerHTML="";
	}
	return false;
}
function flowDepict(applyType,keyId){ 
	var winWidth = 1024;
	if($(window.parent).width()>1024){
		winWidth = 1150;
	}
	window.open("/OAMyWorkFlow.do?keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")&applyType="+applyType,null,"height=570, width="+winWidth+",top=50,left=100 ");
}


function getValueById(value){
	return document.getElementById(value) ;
}
function beforSubmit(form){ 
	if(form.checkPerson!=undefined){
		if(!isChecked("checkPerson")){
			alert('$text.get("oa.transactor.select.one")');
			return false;
		}
	}
	if(form.oaTimeLimit!=undefined){
		if(form.oaTimeLimit.value.length==0){
			alert('$text.get("oa.transactor.inputTransLimit")');
			return false;
		}else{
			if(!gtZero(form.oaTimeLimit.value)){
				alert("$text.get("reportServlet.lb.dealMaxtime") $text.get('common.validate.error.number')");
				return false;
			}
		}
	}
	if(form.deliverance!=undefined){
		if(form.deliverance.value.length>500){
			alert('$text.get("oa.deliverance.excess")') ;
			return false ;
		}
	}
	#if("$!ideaRequired"=="true")
	if(form.deliverance!=undefined){
		if(form.deliverance.value.length==0){
			alert('$text.get("com.deliverance.not.null")') ;
			return false ;
		}
	}
	#end
	window.parent.returnValue="deliverTo";
	if(typeof(top.jblockUI)!="undefined" && submitBefore){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}	
	return true;
}
function unLoadFun(){
	if(window.parent.returnValue!="deliverTo"){	
		window.parent.returnValue="$!keyId";
	}
}

//双击回填数据
function fillData(datas){
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	parent.jQuery.close('Popdiv');
}

function getValueById(value){
	return document.getElementById(value) ;
}

function newOpenSelectSearch(str,fieldName,fieldNameIds){
	var employees = str.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			getValueById(fieldName).options.add(new Option(field[1],field[0]));
			getValueById(fieldNameIds).value+=field[0]+",";
		}
	}
}

</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body onLoad="showtable('tblSort');showStatus(); " onunload="unLoadFun()">

<form  method="post" scope="request" name="form" action="/OAMyWorkFlow.do"  target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("Op_AUDITING")"/>
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/>
<input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
<input type="hidden" name="keyId" value="$!keyId"/>
<input type="hidden" name="attachFiles" value=""/>
<input type="hidden" name="delFiles" value="" />
<input type="hidden" name="designId" value="$!designId" />
<input type="hidden" name="currNode" value="$!currNB.keyId" />
<input type="hidden" name="department" value="$!department"/>
<input type="hidden" name="fromAdvice" value="$!request.getParameter("fromAdvice")"/>
<input type="hidden" name="approveStatus" value="$!request.getParameter("approveStatus")"/>
<input type="hidden" name="noback" value="$!noback"/>
<input type="hidden" name="toPage" value="$!toPage"/>
<input type="hidden" name="fromPage" value="$!fromPage"/>
<input type="hidden" id="tableName" name="tableName" value="$!tableName"/>
<input type="hidden" id="checkFlag" name="checkFlag" value="$!checkFlag" />
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType" />
<input type="hidden" id="f_brother" name="f_brother" value="$!f_brother" />
<input type="hidden" id="winCurIndex" name="winCurIndex" value="$!winCurIndex" />
<input type="hidden" id="pageNo" name="pageNo" value="$!pageNo"/>
<input type="hidden" id="parentCode" name="parentCode" value="$!parentCode"/>
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName" />
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="" />
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="" />
<input type="hidden" id="saveDraft" name="saveDraft" value="$!saveDraft" />
<input type="hidden" id="type" name="type" value="$!type" />
<div class="Heading" style="width:100%; margin:0px;">
	<!-- <div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div> -->
	<div class="HeadingTitle" style="width:96%">
		<div>$text.get("common.lb.DeliverTo")</div>
		<div style="color:red; font-size:14px; margin:0px 10px 0px 10px;">$!moduleName</div>
		<div><a style="cursor: pointer;" onclick="javascript:flowDepict('$!designId','$!keyId');">$text.get("common.lb.checkFlowChart")&nbsp;</a></div>
		<div  style=" margin:0px 10px 0px 10px;">$text.get("common.lb.CurrentlyStep")$!currNB.display</div>
	</div>

</div>
<div class="listRange_frame" align="center"  style="width:100%; margin:0px;overflow-y:auto;overflow-x: hidden" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-38;
if(sHeight>0){
	oDiv.style.height=sHeight+"px";
}
</script>
	<div class="listRange_div_jag" style="float:left;width:97%; margin:0px;">
			<ul class="listRange_jag" style="width:auto;">
				<li>
					<span style="float:left;display:inline-block;width:80px;text-align:right;">$text.get("common.msg.SignAdvice")#if("$!ideaRequired"=="true")<font color="red">*</font>#end</span>
					<textarea style="width:420px;float:left;" name=deliverance rows="3" cols="40" >$!deliver.deliverance</textarea>
				</li>
				#if("$!Safari"!="true")
				<li><span style="float:left;display:inline-block;width:80px;text-align:right;">$text.get("upload.lb.affix")：</span>
					<button type=button class="b2" onClick="openAttachUpload('/affix/$!tableName/')">$text.get("oa.common.accessories")</button>
					<div id="files_preview" style="float:left;background:none;width:400px;"></div>
				</li>
				#end
				<li><span style="float:left;display:inline-block;width:80px;text-align:right;">$text.get("common.lb.nextT")：</span>
				#set($flag=false)
				#foreach($!nextNB in $!nextNBs)
				#if($!nextNB.keyId.indexOf("back")<0)
				<div style="float:left;display:inline-block;">
					<input type="radio" class="in_radio" style="border:0px;width:15px;" onclick="changeStep('change')" #if($velocityCount==1)checked#end name="nextStep" id="nextStep_$!nextNB.keyId" value="$!nextNB.keyId" /><label for="nextStep_$!nextNB.keyId" class="lb_radio">$!nextNB.display</label>
				</div>
								
				#else
				#set($flag=true)
				#end
				#end
				</li>	
				#if($flag)
				<li><span style="float:left;display:inline-block;width:80px;text-align:right;">$text.get("oa.msg.back")</span>
				#foreach($!nextNB in $!nextNBs)
				#if($!nextNB.keyId.indexOf("back")>=0)
				<div style="float:left;display:inline-block;">
					<input type="radio" class="in_radio" style="border:0px;width:15px;" onclick="changeStep('change')" #if($velocityCount==1)checked#end name="nextStep" id="nextStep_$!nextNB.keyId" value="$!nextNB.keyId" /><label for="nextStep_$!nextNB.keyId" class="lb_radio">$!nextNB.display</label>
				</div>
				#end
				#end
				</li>				
				#end
				<li id="search" #if($globals.getList($!nextNBs,0).checkPeople.size()<20)style="display: none;"#end>
				#if($globals.getList($!nextNBs,0).checkPeople.size()>=20)
				<span style="float:left;display:inline-block;width:80px;text-align:right;">$text.get("oa.myWorkflow.searchUser")：</span>
				<input type="text" name="userName" value="" style="width:100px;height:18px;" onKeyDown="if(event.keyCode==13){changeStep(); return false;}" />&nbsp;<button type="button" class="b4" onClick="javascript:changeStep();">GO</button>
				#end
				</li>		
				<li id="cps" style="width:99%;">
				#if($globals.getList($!nextNBs,0).checkPeople.size()>0) 
					<span style="float:left;display:inline-block;width:80px;text-align:right;">$text.get("call.lb.transactor")：</span>
					<ul style="float:left;width:450px;padding:0px;margin-top: 5px;">
					#foreach($!cp in $globals.getList($!nextNBs,0).checkPeople) 
					#if($!globals.getEmployeeById($globals.get($!cp,0)).statusId!="-1")<!-- 过滤已经停用的用户 -->
						#if($globals.getList($!nextNBs,0).approvePeople.equals("fix")||$globals.getList($!nextNBs,0).id.indexOf("back")>=0)
						<input type="hidden" name='checkPerson' value="$globals.get($!cp,0)" />
						<li class="cbox_li" style="float:left;width:70px;"><input style="width:15px;border:0px;float:left;" type="checkBox" checked disabled="false" name="checkPerson" value="$globals.get($!cp,0)" id="$globals.get($!cp,0)" />#if($globals.isOnline($globals.get($!cp,0)))<label for="$globals.get($!cp,0)" class="lb_cbox"> <font class="in_ft" color="blue">$globals.get($!cp,1)</font> </label>#else <label for="$globals.get($!cp,0)" class="lb_cbox"> $globals.get($!cp,1)</label> #end</li>
						#else
						<li class="cbox_li" style="float:left;width:70px;"><input style="width:15px;border:0px;float:left;" type="checkBox" #if($globals.getList($!nextNBs,0).checkPeople.size()==1)checked #end  name="checkPerson" value="$globals.get($!cp,0)" id="$globals.get($!cp,0)" />#if($globals.isOnline($globals.get($!cp,0)))<label for="$globals.get($!cp,0)" class="lb_cbox">  <font class="in_ft" color="blue">$globals.get($!cp,1)</font></label>#else<label for="$globals.get($!cp,0)" class="lb_cbox">  $globals.get($!cp,1) #end</label></li>
						#end
					#end
					#end
					</ul>
				#end
				</li>
				#if("$!dispenseWake"!="")
				<li>
				<span style="float:left;display:inline-block;width:80px;text-align:right;">分发：</span>
				<div id="_context" style="float:left;display:inline-block;">
					<div style="float:left;display:inline-block;">
						<div style="text-align:left;">
							<img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop2('deptGroup','formatDeptName','popedomDeptIds','1');"  alt="$text.get("oa.common.adviceDept")" class="search"/>
							<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');">$text.get("oa.select.dept")</a>
						</div>
						<select name="formatDeptName" id="formatDeptName" multiple="multiple" style="width:120px;">
						</select>
					</div>
					<img style="float:left;display:inline-block;margin:20px 0 0 5px;" onClick="deleteOpation2('formatDeptName','popedomDeptIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
				</div>
				<div id="_context" style="float:left;display:inline-block;">
					<div style="float:left;display:inline-block;">
						<div style="text-align:left;">
							<img src="/$globals.getStylePath()/images/St.gif" onClick="deptPop2('userGroup','formatFileName','popedomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search"/>
							<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');">$text.get("oa.select.personal")</a>
						</div>
						<select name="formatFileName" id="formatFileName" multiple="multiple" style="width:120px;">
						</select>
					</div>
					<img style="float:left;display:inline-block;margin:20px 0 0 5px;" onClick="deleteOpation2('formatFileName','popedomUserIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search" />
				</div>
				</li>
				#end
				#if($globals.getList($!nextNBs,0).forwardTime)
				<li id="timeLit" style="vertical-align:top">
				<span style="float:left;display:inline-block;width:80px;text-align:right;color:red;">$text.get("reportServlet.lb.dealMaxtime")：</span><input type="text" style="width: 40px;" name="oaTimeLimit" value="" />
				<select name="oaTimeLimitUnit"  style="width:50px;float:left;margin:2px 0 0 5px;border:1px solid #8CBEDC;border-radius:2px;height:23px;">
				<option value="0">$text.get("oa.calendar.day")</option>
				<option value="1">$text.get("oa.work.hour")</option>
				<option value="2">$text.get("common.msg.minute")</option>
				</select>
				</li>
				#end
				<!-- 
				</li>
				<li style="margin-top:5px;"><span></span>
					<button type = "submit" class="b2">$text.get("common.msg.ConfirmDeliverTo")</button>
					<button type = "button" onClick="javascript:closeWin();" class="b2">$text.get("common.lb.close")</button>
				</li>
				 -->
	</ul>
	</div>
</div>
</form>
</body>
</html>
