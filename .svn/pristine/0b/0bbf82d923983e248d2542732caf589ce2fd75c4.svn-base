<html> 
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<title>$text.get("aio.oa.mydesk")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link rel="stylesheet" href="/$globals.getStylePath()/css/crmdesktop.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="JavaScript" src="/js/FusionCharts.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<!--知识中心……等几栏的tab Show-->
<script src="/js/tabShow/ui.core.js" type="text/javascript"></script>
<script src="/js/tabShow/ui.tabs.js" type="text/javascript"></script> 
<script type="text/javascript">
       $(function() {
           $('#rotate > ul').tabs({ }).tabs('rotate', 0);
       });
       
       function cancelAttention(keyId){
       		url = "/UserFunctionQueryAction.do?tableName=CRMClientInfo&keyId="+keyId+"&operation=4&optionType=abolishAttention&empId=$LoginBean.id";
       		jQuery.get(url,function(data){
       			window.location.reload();
       		});
       }
       
       function flushMsg(){
		jQuery.get("/CrmDeskTopAction.do?type=msgCount"+"&time="+(new Date()).getTime(),function(response){
			if(response.length>0 && response!="no response text"&&response!="close"){
				var arrayMsg = response.split(":") ;	
				if(arrayMsg[0] == 0){				
					document.getElementById("Msg1").innerHTML="";
				}else{
					document.getElementById("Msg1").innerHTML="<font color='red'>"+arrayMsg[0]+"</font>";
				}
				
				if(arrayMsg[1] == 0){				
					document.getElementById("Msg2").innerHTML="";
				}else{
					document.getElementById("Msg2").innerHTML="<font color='red'>"+arrayMsg[1]+"</font>";
				}
				
				if(arrayMsg[2] == 0){				
					document.getElementById("Msg3").innerHTML="";
				}else{
					document.getElementById("Msg3").innerHTML="<font color='red'>"+arrayMsg[2]+"</font>";
				}
				if(arrayMsg[3] == 0){
					document.getElementById("Msg4").innerHTML="";
				}else{
					document.getElementById("Msg4").innerHTML="<font color='red'>"+arrayMsg[3]+"</font>";
				}	
	
			}
			var alertTime = 1 ;		
			setTimeout('flushMsg()',alertTime*60*1000);
		}); 
	}
</script>


<style type="text/css">
.hym{border:yellow solid outset 3px;}
    #demo {
     overflow:hidden;
     border: 1px dashed #CCC;
     width: 500px;
    }
    #demo img {
     border: 3px solid #F2F2F2;
    }
    #indemo {
     float: left;
     width: 800%;
    }
    #demo1 {
     float: left;
    }
    #demo2 {
     float: left;
    }
</style>
</head> 
<body onLoad="setTabColumn();setTabClock();setTabLine();setTimeout('flushMsg()',10000);">
<div class="content">
<table cellpadding="0" cellpadding="0" align="left" style="margin:10px 0px 0px 0px;">
	<tr>
	<td valign="top" style="width:238px">
		<ul class="usleft">
			<li class="usleft_Head">$LoginBean.empFullName,$text.get("common.lb.welcome")!</li>
			<li class="usleft_body"  style="height:228px;">
					<div class="usimg crmusimg" valign="top">						
						<img name="myPhoto" src="$!myPhoto" onClick="uploadImage();">						
					</div>
					
					<dl class="crmusimg">
						<dt>$countDownTitle</dt>
						#set($fz="55")
						#if($!countDownValue.length()>=3)
						#set($fz="28")
						#elseif($!countDownValue.length()==2)
						#set($fz="40")
						#end
						#if("$LoginBean.id"=="1")
						<dd ><a href="javascript:updateCoundDown()" ><span style="color:red;font-size:${fz}px">$countDownValue</span></a><span>$text.get("oa.calendar.day")</span></dd>
						#else <dd ><span style="color:red;font-size:${fz}px">$countDownValue</span><span>$text.get("oa.calendar.day")</span></dd>#end
					</dl>
					
					<ul class="crmusimg">						
						<li class="CRMmsgcon">
								<div class="msg_1"><a href="javascript:mdiwin('/EMailAction.do?src=menu','$text.get("oa.mail.myMail")')" title="$text.get("mydesktop.msg.com.Noread")"></a><span id="Msg1"><font color="red"></font></span></div>
								<div class="msg_2"><a href="javascript:mdiwin('/MessageQueryAction.do?src=menu','$text.get("oa.mydesk.news")')" title="$text.get("mydesktop.msg.com.NoReadMess")"></a><span id="Msg2"><font color="red"></font></span></div>
								<div class="msg_3"><a href="javascript:mdiwin('/AdviceAction.do?type=notApprove','$text.get("advice.lb.info")');" title="$globals.getEnumerationItemsDisplay('adviceType2','notApprove')"></a><span id="Msg3"><font color="red"></font></span></div>
								<div class="msg_4"><a href="javascript:mdiwin('/AdviceAction.do?type=Other','$text.get("advice.lb.info")');" title="$globals.getEnumerationItemsDisplay('adviceType2','Other')"></a><span id="Msg4"><font color="red"></font></span></div>
						</li>
					</ul>
					
					<div class="usMotto">
						#if("$!wisdom"!="")
						 <MARQUEE scrollamount=2 scrollDelay=200 direction=left width=100% height=20 style="white-space: nowrap;">
						 $!wisdom
						 </MARQUEE>
						 #end
				 	</div>
			</li>
			<li class="usleft_foot"></li>
	   </ul>
		<ul class="usleft">
			<li class="usleft_Head">$text.get("crm.deskTop.myAchievement")				
			</li>
			<li class="usleft_body" id="main0" style="position:relative; height:228px; padding-top:15px; text-align:center;">				
			<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
				id="myComplete" width="210" height="200" codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
				<param name="movie" value="flash/clock.swf" />
				<param name="quality" value="high" />
				<param name="bgcolor" value="#869ca7" />
				<param name="flashvars" value="" />
				<param name="allowScriptAccess" value="sameDomain" />
				<embed src="flash/clock.swf" quality="high" bgcolor="#869ca7"
					width="210" height="200" name="column" align="middle" 
					play="true" loop="false" flashvars=""
					quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash"
					pluginspage="http://www.adobe.com/go/getflashplayer">
				</embed>
			</object>
			</li>
			<!-- 漏斗图 -->
			<!--
			<ul id="menu2" style=" margin-right:10px; position:absolute; top:-35px; right:0px; float:right;">
					<select onChange="setTab3(this)">
						<option>$text.get("crm.client.lifecycle")</option>
						<option>$text.get("crm.deskTop.followPhaseUsed")</option>
					</select>
					<!-- 
					<select onchange="changeType()" id="displayType">
						<option value="week">本周</option>
						<option value="month" selected="selected">本月</option>
						<option value="year">本年</option>
					</select>
					 -->
					 <!-- 
				</ul>
			<div id="main2" style="margin:0px; padding:0px; height:210px; width:210px; ">
			<ul class="block" id="statusChart" style="margin:0px; padding:0px;">
				
			</ul>
			<ul id="followChart" style="margin:0px; padding:0px;">
				
			</ul>
			</div>-->
			
			<li class="usleft_foot"></li>
	   </ul>
		
	   </td>
	<td valign="top">
			<table cellpadding="0" cellspacing="0" border="0" style="margin-bottom:8px; width:100%">
				<tr>
					<td class="crmDeskTop_1"></td>
					<td class="crmDeskTop_middle" style="height:297px;" valign="top">
						<div class="listHead">$text.get("crm.deskTop.mySalesData")</div> 
						<!-- 柱状图 -->			 
						<table cellpadding="0" class="crmdmidtable" cellspacing="0">
								<tr>
								<td>
									<div id="myGoalId" style="float: left; width:auto; margin-top:35px">
							 
									</div>
								</td>
								<td>  
									<div id="goalMsLineId" style="float: left; width:auto; margin-top:35px">
									
									</div>
								</td>
							</tr>
						</table>
					</td>
					<td class="crmDeskTop_2"></td>
				</tr>
			</table>
			<table cellpadding="0" cellspacing="0" border="0" style="margin-bottom:8px; width:100%">
				<tr>
					<td>
						<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="crmDeskTop_left"></td>
								<td class="crmDeskTop_middle" valign="top">
							<div class="listHead">
							<div >
								<a style="background:none;width:100px; line-height:17px; margin-top:5px" href="javascript:mdiwin('/ClientAction.do?src=menu','$text.get("crm.client.list")')">$text.get("crm.deskTop.myClient")</a> 
								#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=CRMClientInfo").add())
								<a style="background:none;width:100px; line-height:17px; margin-top:5px" href="javascript:mdiwin('/UserFunctionQueryAction.do?tableName=CRMClientInfo&src=menu&operation=6&noback=true','$text.get("common.msg.addClientData")');">$text.get("common.lb.addClient")</a>#end
							</div>
							<div style="float:right">
								<a href="javascript:mdiwin('/ClientAction.do?src=menu','$text.get("common.lb.clientDetail")')"></a>
							</div>
						</div>
						<table cellpadding="0" cellspacing="0" class="CrmFCus">
													 
								#foreach($client in $clientMap.get("attentionFollow"))
								<tr>	
									<td>
									<a href="javascript:mdiwin('/CrmTabAction.do?operation=5&keyId=$globals.get($client,0)','$text.get("common.lb.clientDetail")')" title="$text.get('common.lb.spotNews'):&#10;$!globals.get($!client,2)">$globals.get($client,1)</a>
									$!globals.get($client,3) &nbsp;$!globals.get($client,4) &nbsp;	
									</td>
									<td class="CrmFcusC" align="center" style=" padding:0px;"><a href="javascript:cancelAttention('$globals.get($client,0)')" title="$text.get('common.lb.cancelAttention')" rel="$text.get('common.lb.cancelAttention')"></a></td>
								</tr>
								#end
								
							
						</table>
										
										
								</td>
								<td class="crmDeskTop_right" style="padding-right:10px"></td>
							</tr>
						</table>
					

					</td>
					<td style="width:332px;">
						<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="crmDeskTop_left"></td>
								<td class="crmDeskTop_middle" valign="top">
										#if($!listfameTop!="") 
										<div class="listHead">$text.get("crm.deskTop.fameTop")</div>
	<div style="width:310px;height:230px; overflow:hidden;"  id=demo >
	<table cellspacing=0 cellpadding=0 align=left border=0>
		<tr>
		<td valign=top style="border:0;padding:0;margin:0;">
		<div id="demo1">
		<table style="border:none;" cellpadding="0" cellspacing="0" class="crmHonor">
		<tr>
		#foreach($listTop in $listfameTop)
			<td nowrap><nobr>	
			<div style="width:94px;">						
			<img id="$globals.get($listTop,4)" src=#if("$!globals.get($listTop,3)"=="") "/style/images/no_head.gif" #else "/ReadFile.jpg?fileName=$globals.urlEncode($globals.get($listTop,3))&tempFile=false&type=PIC&tableName=tblEmployee" #end
				onMouseDown="onfamewish('$globals.get($listTop,4)')" onClick="fameWishdetail('0')"
													alt="$text.get('crm.deskTop.Eval'):$!globals.get($listTop,9)
$text.get('crm.deskTop.Applause')：$!globals.get($listTop,10)
$text.get('crm.deskTop.Blood.Arouse')：$!globals.get($listTop,11)
$text.get('crm.deskTop.Defiant')：$!globals.get($listTop,12)"></img>						
				<div>$!globals.get($listTop,1)</div>
				<div id="crmHonorName">$!globals.get($listTop,2)</div>							
				<div >
				<img src="/style/images/0.gif" style="width:16px;height:16px;border:none;cursor:hand" onClick="detail('$globals.get($listTop,0)','1','$globals.get($listTop,4)')" alt="$text.get('crm.deskTop.Applause')" />
				
				<img src="/style/images/1.gif" style="width:16px;height:16px;border:none;cursor:hand" onClick="detail('$globals.get($listTop,0)','2','$globals.get($listTop,4)')" alt="$text.get('crm.deskTop.Blood.Arouse')" />
				
				<img src="/style/images/2.gif" style="width:16px;height:16px;border:none;cursor:hand" onClick="detail('$globals.get($listTop,0)','3','$globals.get($listTop,4)')" alt="$text.get('crm.deskTop.Defiant')" />
				<input type="hidden" value=$listfameTop.size() id="list"/>
				</div>
			</div>	
		</nobr></td>
		#end
	</tr>
		
</table></div>
		</td>
		<td valign=top style="border:0;padding:0;margin:0;"><div id="demo2"></div></td>
		</tr>
	</table>				
										
					</div>
										#end
								</td>
								<td class="crmDeskTop_right"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>			
			<table cellpadding="0" style="width:100%" cellspacing="0" border="0">
				<tr>
					<td class="crmDeskTop_left"></td>
					<td class="crmDeskTop_middle" valign="top">			
						<div id="rotate" style="height:175px; float:left">
							  <ul class="listHead">
								#set($cc=1)				
							#foreach ($myDesk in $myDeskList)
							<li><a href="#fragment-$cc"><span>$!myDesk.modulName</span></a><li>
							#set($cc=$cc+1)
							#end
								<!--  <li><a href="#fragment-memory"><span>$text.get("crm.deskTop.birthdayRemind")</span></a></li> -->
							  </ul>
						  #set($cc=1)				    
						#foreach ($myDesk in $myDeskList)
						<div id="fragment-$cc">#set($cc=$cc+1) <!--这里是工作计划、知识中心等的Tab Show 的列表-->
							<table cellpadding="0" cellspacing="0"  class="crmdmidtable">
									#foreach ($objs in $myDesk.modulList)
										<tr #if($globals.isOddNumber($velocityCount))  #end><td>
											#if($!globals.get($objs,0).indexOf("//") > 0)
											<a href="$!globals.get($objs,0)" target="_blank" title="$!globals.get($objs,1)">$globals.subTitle($!globals.get($objs,1),60)</a>
											#else
											#if($!myDesk.moreLinkAddress!="/UserFunctionQueryAction.do?tableName=tblEmployee")
											<a href="javascript:mdiwin('$!globals.get($objs,0)','$!myDesk.modulName')" title="$!globals.get($objs,1)">$globals.subTitle($!globals.get($objs,1),60)</a>
											#else
											$globals.subTitle($!globals.get($objs,1),60)
											#end
											#end
											&nbsp;&nbsp;&nbsp;<span>$!globals.get($objs,2)</span>
										</td></tr>
									#end  
									#if($!myDesk.moreLinkAddress!="/UserFunctionQueryAction.do?tableName=tblEmployee")  
									<tr ><td style="background:none;padding:0px 0px 0px 20px; text-align:left">
									<a href="javascript:mdiwin('$myDesk.moreLinkAddress','$myDesk.modulName')">$text.get("alertCenter.lb.more")</a>		
									</td></tr>
									#end
							</table>
						</div>	
						#end
						
						</div>
					</td>
					<td class="crmDeskTop_right"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
</body> 
<script language="javascript"> 
<!--   
/* 柱状图   第一种形式 第二种形式 更换显示样式*/   
function setTabColumn(obj){
	//暂时来说，CRM桌面的柱状图和折线图都是同一数据来源，因此没必要执行2次，因些暂时把这里的代码移入曲线图中
	return;	
	
	//取各分辩率下操作区域大小，网面宽-左边人像宽度 除2 ，因要显示两个图  ""+fwidth, ""+fheight  "380", "250"
	fwidth = (document.body.scrollWidth-360)/2;
	fheight = 210;
	
 	var n = 0 ;
 	if(typeof(obj)!="undefined"){
		n = obj.selectedIndex ;
	}
 	var goalClass = "" ;
 	if(n==0){
 		goalClass = "myGoal" ;
 	}else if(n==1){
 		goalClass = "tblDepartmentGoal" ;
 	}else{
 		goalClass = "tblCompanyGoal" ;
 	}
 	
 	jQuery.get("/UtilServlet?operation=displayGoal&goalClass="+goalClass+"&time="+(new Date()).getTime(),function(response){
 		if(response=="no"){
	 		var goalId = document.getElementById("myGoalId") ;
	 		goalId.style.height = "240px" ;
	 		goalId.innerHTML = "<a href=\"javascript:mdiwin('/OAWorkPlanAction.do?1=1&src=menu','$text.get("oa.mydesk.workPlan")')\">$text.get("crm.set.month.plan")</a>" ;
	 	}else{ 
	 		var chart = new FusionCharts("flash/FCF_MSColumn3D.swf", "ChartId", ""+fwidth, ""+fheight);	 		
	 		chart.setDataXML(response);
			chart.render("myGoalId");
		}
 	}) ;
 	
}  

/*曲线图*/
function setTabLine(obj){   
	//取各分辩率下操作区域大小，网面宽-左边人像宽度 除2 ，因要显示两个图  ""+fwidth, ""+fheight  "380", "250"
	fwidth = (document.body.scrollWidth-360)/2;
	fheight = 210;
 	var n = 0 ;
 	if(typeof(obj)!="undefined"){
		n = obj.selectedIndex ;
	}
 	var goalClass = "" ;
 	if(n==0){
 		goalClass = "myGoal" ;
 	}else if(n==1){
 		goalClass = "tblDepartmentGoal" ;
 	}else{
 		goalClass = "tblCompanyGoal" ;
 	}
 	
 	var goalId = document.getElementById("goalMsLineId") ;	 		
	goalId.style.height = fheight+"px" ;
	goalId.style.width = fwidth+"px" ;
	var goalId = document.getElementById("myGoalId") ;
	goalId.style.height = fheight+"px" ;
	goalId.style.width = fwidth+"px" ;
	
 	jQuery.get("/UtilServlet?operation=displayMS2line&goalClass="+goalClass+"&time="+(new Date()).getTime(),function(response){
 		
 		if(response=="no"){
	 		var goalId = document.getElementById("myGoalId") ;
	 		goalId.innerHTML = "<a href=\"javascript:mdiwin('/UserFunctionQueryAction.do?tableName=tblEmployeeGoal&operation=4&parentCode=&f_brother=&draftQuery=&moduleType=&checkTab=Y&parentTableName=&pageNo=1','$text.get("crm.deskTop.empTargetManager")')\">$text.get("crm.set.month.plan")</a>" ;
	 	}else if(response=="stop"){
	 		alert("$text.get("crmdesk.msg.sourceStop")");
	 	}else if(response=="gradeStop"){
	 		alert("$text.get("crmdesk.msg.gradestop")");
	 	}else if(response == ""){
	 		
	 	}else if(response!="no" ){
 			vals = response.split("::::");
	 		var chart = new FusionCharts("flash/FCF_MSLine.swf", "ChartId", ""+fwidth, ""+fheight);
	 		chart.setDataXML(vals[0]);
			chart.render("goalMsLineId");
			
			var chart = new FusionCharts("flash/FCF_MSColumn3D.swf", "ChartId", ""+fwidth, ""+fheight);	 		
	 		chart.setDataXML(vals[1]);
			chart.render("myGoalId");
		}
 	}); 	
}  
/*仪表盘*/
function setTabClock(obj,varType){   
 		jQuery.get("/UtilServlet?operation=displayYibiaopan&goalClass=tblEmployeeGoal&displayType="+varType+"&time="+(new Date()).getTime(),function(response){
 			if("noExist"!=response &&"stop"!=response){
	 			var dataArray = response.split(";") ;
	 			var varFlash = document.getElementById("myComplete") ;
	 			varFlash.flashvars = "data="+dataArray[0]+"&color="+dataArray[1]+"&curData="+dataArray[2] ;
	 			if(typeof(varFlash.setData)!="undefined"){
	 				varFlash.setData(dataArray[0],dataArray[1],dataArray[2]) ;
	 			}
			}
 		}) ;
}  

var statusXML = "$!statusXML" ;
var followXML = "$!followXML" ;
function setTabFunnel(obj){  
	var n = 0 ;
	if(typeof(obj)!="undefined"){ 
		n = obj.selectedIndex ;
	}
 	var mli=document.getElementById("main2").getElementsByTagName("ul");
 	
 	for(i=0;i<mli.length;i++){   
  		mli[i].style.display=i==n?"block":"none";   
 	}
 	var chart = new FusionCharts("flash/FCF_Funnel.swf", "ChartId", "200", "250");
 	if(1==n){
		chart.setDataXML(followXML); 		   
		chart.render("followChart");
 	}else{
 		chart.setDataXML(statusXML);	 
		chart.render("statusChart");
 	}
}

function changeType(){
	var strType = document.getElementById("displayType") ;
}

function changeTab(varType){
	var obj=document.getElementById("completeId");   
 	setTab2(obj,varType)
 	if(varType == "varMonth"){
 		document.getElementById("mo").className="hym";
 		document.getElementById("season").className="";
 		document.getElementById("ye").className="";
 	}else if(varType == "varSeason"){
 		document.getElementById("mo").className="";
 		document.getElementById("season").className="hym";
 		document.getElementById("ye").className="";
 	}else{
 		document.getElementById("mo").className="";
 		document.getElementById("season").className="";
 		document.getElementById("ye").className="hym";
 	}
}

function uploadImage(){
	if(confirm("$text.get('crm.deskTop.ifUploadImage')?")){
		var str  = window.showModalDialog("/CrmDeskTopAction.do?operation=$globals.getOP("OP_ADD_PREPARE")","","dialogWidth=380px;dialogHeight=190px");
		if(typeof(str)=='undefined' || str.length == 0){
			return;
		}
		document.getElementById("myPhoto").src = "/ReadFile.jpg?fileName="+encodeURIComponent(str)+"&tempFile=false&type=PIC&tableName=tblEmployee" ;
	}
}
function updateCoundDown(){	
		var str  = window.showModalDialog("/CrmDeskTopAction.do?type=Countd","","dialogWidth=480px;dialogHeight=290px");
		if(typeof(str)=='undefined' || str.length == 0){
			return;
		}else{
			window.location.reload();
		}
	
}


function openStatTable(){
	if("block"==document.getElementById("statusChart").style.display){
		mdiwin('/ReportDataAction.do?reportNumber=ReportTradeTotal&fieldType=CRMClientInfo.Status','$text.get("common.lb.ReportTradeTotal")') ;
	}else{
		mdiwin('/ReportDataAction.do?reportNumber=ReportCRMFollowUp','$text.get("common.lb.ReportCRMFollowUp")') ;
	}
}
//-->  
</script>
<script>

var d_w = $('#demo').width();
var length=document.getElementById("list").value;
if(length>1)(
$(function(){
	var speed=6000,d =$('#demo'),d1 =$('#demo1'),d2 =$('#demo2');
	d2[0].innerHTML = d1[0].innerHTML;
	function Marquee(){
		if(d2[0].offsetWidth-d[0].scrollLeft<=0)
			d[0].scrollLeft -= d1[0].offsetWidth+1;
		else{
			var w = 0, t = 0,j = 0,ow = d[0].scrollLeft;
			d2.find('.crmHonor td').each(function(i){
				t += $(this).width();
				if(t>=ow) {
					if(j<3) {
						w += $(this).outerWidth();
						j++;
					}
				}
			});
			d.animate({ 
				scrollLeft: d[0].scrollLeft+w
			}, 3000);
		}
	}
	var MyMar = setInterval(Marquee,speed);
	d.mouseover(function() {clearInterval(MyMar)}).mouseout(function() {MyMar=setInterval(Marquee,speed)});
	d.width(d_w);
}));

  function detail(id,typeid,userId){
  	if(typeid==1){
  		if(confirm("$text.get('crm.deskTop.applause')")){
  			AjaxRequest("/UtilServlet?operation=fameTopWish&id="+id+"&typeId="+typeid+"&userId="+userId);
  		}
  	}
  	else if(typeid==2){
  		if(confirm("$text.get('crm.deskTop.Blood')")){
  			AjaxRequest("/UtilServlet?operation=fameTopWish&id="+id+"&typeId="+typeid+"&userId="+userId);
  		}
  	}
  	else{
  		if(confirm("$text.get('crm.deskTop.Below.Defiant')")){
  			AjaxRequest("/UtilServlet?operation=fameTopWish&id="+id+"&typeId="+typeid+"&userId="+userId);
  		}
  	}
  }
  
 
  
  function fameWishdetail(status){
	window.open("/CrmDeskTopAction.do?operation=5&status="+status,"","dialogWidth=800px;dialogHeight=500px");
  }
  function onfamewish(employeeID){
	  if(document.getElementById(employeeID).alt !=""){
	  		document.getElementById(employeeID).alt="";
	  }
  }
  </script>
</html> 

