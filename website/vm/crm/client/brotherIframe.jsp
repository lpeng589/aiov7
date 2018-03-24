<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link rel="stylesheet" type="text/css" href="/style/css/client.css"  />
<link rel="stylesheet" type="text/css" href="/style/css/client_sub.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">
.cont_dv table{width: 100%;table-layout:fixed;}
</style>
<script type="text/javascript">
if(typeof(parent.junblockUI)!="undefined"){
	parent.junblockUI();
}
$(document).ready(function(){
	
	$(".c1").click(function () {
		if($(this).attr("class").indexOf("show_all2")>0){
			$(this).removeClass("show_all2").addClass("show_all");
			$(".col").each(function(){
				$(this).removeClass("zkbg").find("div").attr("show","no").hide().end().find(".t1").css('background-position','0 -1029px');
			});
		}else{
			$(this).removeClass("show_all").addClass("show_all2");
			$(".col").each(function(){
				$(this).addClass("zkbg").find("div").attr("show","yes").show().end().find(".t1").css('background-position','0 -642px');
			});
		}
		if($("#scroLeft")[0].scrollHeight<=$("#scroLeft").height()){
			$("#scroRight").hide();
		}else{
			$("#scroRight").show();
		}
	});
	/*单击弹开联系人明细*/
	$(".t1").click(function () { 
		var oDiv = $(this).parent("ul").siblings("div.cont_dv");
		var show = oDiv.attr("show");
		if(show == "yes"){
			oDiv.attr("show","no").removeClass("zkbg").hide();
			$(this).css('background-position','0 -1029px') ;
		}else{
			oDiv.attr("show","yes").addClass("zkbg").show();
			$(this).css('background-position','0 -642px') ;
		}
		if($("#scroLeft")[0].scrollHeight<=$("#scroLeft").height()){
			$("#scroRight").hide();
		}else{
			$("#scroRight").show();
		}
	});
});
/*打开更多的邻居表数据*/
function openMore(){
	#if($LoginBean.operationMap.get("/CRMBrotherAction.do?tableName=$!tableInfo.tableName").query())
		var strUrl = '/CRMBrotherAction.do?tableName=$!tableInfo.tableName' ;
	#else
		var strUrl = '/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=$!tableInfo.tableName&src=menu&f_brother=$!f_brother&popup=crmPopup' ;
	#end
	parent.parent.mdiwin(strUrl,'$!tableDisplay');
	//parent.parent.asyncbox.open({
	//	id:'crmOpDiv',url:strUrl,title:'$!tableDisplay',modal:true,width:1043,height:540
　  //});
}
/*添加邻居表数据*/
function openAdd(){
	#if($LoginBean.operationMap.get("/CRMBrotherAction.do?tableName=$!tableInfo.tableName").add())
		parent.parent.addBill('$!tableInfo.tableName');
	#else
		var strUrl = "/UserFunctionAction.do?tableName=$!tableInfo.tableName&operation=6&parentTableName=CRMClientInfo&&f_brother=$!f_brother&noback=true" ;
		parent.parent.mdiwin(strUrl,'$!tableDisplay');
	#end
	//parent.parent.asyncbox.open({
	//	id:'crmOpDiv',url:strUrl,title:'$!tableDisplay',modal:true,width:1043,height:540
　  //});
}

//刷新页面,用于列表添加成功后刷新此页面
function reloadDetail(){
	window.location.reload();
}

function addWorkPlan(){
	parent.parent.addWorkPlan();
}

</script>
<style>
/*主窗*/
#scroll{height:275px;position:absolute;left:12px;top:75px;}
/*左边内容区*/
#scroLeft{float:left;height:100%;overflow:hidden;width:513px;}
/*右边滚动条轨道*/
#scroRight{background:#999;background:rgba(0,0,0,0.1);float:right;height:100%;width:5px;border-radius:5px;overflow:hidden;margin-left:3px;cursor:pointer;}
/*滚动条*/
#scroLine{position:absolute;top:0;right:0;width:5px;background:#7494B8;opacity:0.7;border-radius:5px;cursor:pointer;}
</style>
<style type="text/css">
.saletable .t4{line-height:22px;height:22px;}
br{width:0;padding:0;height:0;margin:0;}
</style>
</head>
<body>
<div class="hd"><h3>$!tableDisplay</h3>
	<span>
		#if("$!tableInfo.isView" != "1")<a href="javascript:openMore();" class="closeBtn">更多..</a>#end
		#if("$!tableInfo.tableName" == "CRMWorkPlanView")
			#if($LoginBean.operationMap.get("/OAWorkPlanAction.do").add())
			<a href="javascript:addWorkPlan();" class="closeBtn">添加</a>
			#end
		#else
			#if("$!tableInfo.isView" != "1")
			<a href="javascript:openAdd();" class="closeBtn">添加</a>
			#end
		#end
	</span></div>
    <div class="bd saletable">
    <ul class="conttop">
    #if("$!tableInfo.tableName" == "CRMMailInfoView")
    	<li class="c$index show_all" title="展开所有">发送人邮箱</li>
    	<li class="c$index">收件人邮箱</li>
    	<li class="c$index">发送时间</li>
    	<li class="c$index">接收方式 操作</li>
    #elseif("$!fieldDisplayBean" == "")
	    #set($index=1)
	    #foreach($col in $cols)
	    #if($velocityCount>1 && $velocityCount<6)
	    #if($index==1)
	    <li class="c$index show_all" title="展开所有">$globals.get($col,0)</li>
	    #else
	    	#if($velocityCount == 5)
	    		<li class="c$index">$globals.get($col,0)#if("$!tableInfo.tableName" == "CRMSaleFollowUp" || "$!tableInfo.tableName" == "CRMWorkPlanView") <span style="float: right;margin-right: 3px;">操作</span> #end</li>
	    	#else
		   		<li class="c$index">$globals.get($col,0)</li>
	    	#end
	    #end
	    #set($index=$index+1)
	    #end
	    
	    #end
    #else
	    #foreach($fieldName in $displayFields)
	    #set($fieldBean = $globals.getFieldBean("$!tableInfo.tableName","$fieldName"))
	    #if($velocityCount==1)
	    <li class="c$index show_all" title="展开所有">$fieldBean.display.get("$globals.getLocale()")</li>
	    #else
	    	#if($velocityCount <=4)
		    	#if($velocityCount == 4)
		    		<li class="c$velocityCount">$fieldBean.display.get("$globals.getLocale()")<span style="float: right;margin-right: 3px;">操作</span></li>
		    	#else
			   		<li class="c$velocityCount">$fieldBean.display.get("$globals.getLocale()")</li>
		    	#end
	    	#end
	    #end
	    #end
    #end	
   
    </ul>
   <div id="scroll">
      <div id="scroLeft">
       #if("$!tableInfo.tableName" == "CRMMailInfoView")
           
           #foreach($mail in $mailList)
       	   #set($sendMailType = "")
           #set($sendMailType = $!globals.get($mail,9))
           
           #set($mailFrom = "")
           #if("$!globals.get($mail,7)" =="") 
               #set($mailFrom = $showMailMap.get($!globals.get($mail,0)))
           #else 
               #set($mailFrom = $globals.getEmpFullNameByUserId($!globals.get($mail,7)))
           #end
           
           #set($mailTo = "")
           #if("$!globals.get($mail,8)" =="") 
               #set($mailTo = $showMailMap.get($!globals.get($mail,0)))
           #else 
               #set($mailTo = $globals.getEmpFullNameByUserId($!globals.get($mail,8)))
           #end
           
		    <div class="col">
			    <ul style="overflow:hidden;">
				    <li class="t1" title='$mailAddress' style="height:21px;overflow:hidden;background-position: 0px -642px;">$mailFrom&nbsp;</li>
				    <li>$mailTo&nbsp;</li>
				    <li title='$!globals.get($mail,6)'>$!globals.get($mail,6)&nbsp;</li>
				    <li class="t4">$globals.getEnumerationItemsDisplay("SendMailType","$sendMailType")<span style="float: right;margin-right: 3px;cursor: pointer;" onclick="javascript:top.mdiwin('/EMailAction.do?operation=5&emailType=$globals.get($mail,5)&noback=true&keyId=$globals.get($mail,0)&newOpen=true','$!globals.get($mail,4)')">详情</span></li>
			    </ul>
			    <div style="background:#ffffeb;overflow:hidden;" class="cont_dv zkbg" show="no">
			    	<p>标题：$!globals.get($mail,4)</p>
			    </div>
			</div>
           #end
       #elseif("$!fieldDisplayBean" == "")
		   #foreach($row in $result)
		   #set($idIndex = $math.sub($globals.arrayLength($row),1))
		   #if($velocityCount==1)
		    <div class="zkbg col">
			    <ul class="" style="padding:0;overflow:hidden;">
				    <li class="t1" title='$!globals.get($row,1)' style="height:21px;overflow:hidden;">$!globals.get($row,1)&nbsp;</li>
				    <li>$!globals.get($row,2)&nbsp;</li>
				    <li>$!globals.get($row,3)&nbsp;</li>
				    <li class="t4">$!globals.get($row,4)#if("$!tableInfo.tableName" == "CRMSaleFollowUp" || "$!tableInfo.tableName" == "CRMWorkPlanView") <span style="float: right;margin-right: 3px;cursor: pointer;" #if("$!tableInfo.tableName" == "CRMSaleFollowUp") onclick="javaScript:parent.parent.billDetail('CRMSaleFollowUp','$globals.get($row,$idIndex)')" #else onclick="parent.parent.mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&planType=day&keyId=$globals.get($row,$idIndex)','工作计划')" #end >详情</span>#end </li>
			    </ul>
			    <div style="display:block;" class="cont_dv" show="yes">
			    	<p>$globals.replaceHTML($!globals.get($row,5))</p>
			    </div>
		    </div>
		    #else
		    <div class="col">
			    <ul>
				    <li class="t1" title='$!globals.get($row,1)' style="height:21px;overflow:hidden;">$!globals.get($row,1)&nbsp;</li>
				    <li>$!globals.get($row,2)&nbsp;</li>
				    <li>$!globals.get($row,3)&nbsp;</li>
				    <li class="t4">$!globals.get($row,4)#if("$!tableInfo.tableName" == "CRMSaleFollowUp" || "$!tableInfo.tableName" == "CRMWorkPlanView") <span style="float: right;margin-right: 3px;cursor: pointer;" #if("$!tableInfo.tableName" == "CRMSaleFollowUp") onclick="javaScript:parent.parent.billDetail('CRMSaleFollowUp','$globals.get($row,$idIndex)')" #else onclick="parent.parent.mdiwin('/OAWorkPlanAction.do?operation=4&flagAdvice=advice&planType=day&keyId=$globals.get($row,$idIndex)','工作计划')" #end >详情</span>#end</li>
			    </ul>
			    <div style="display:none;" class="cont_dv" show="no">
			    	<p>$globals.replaceHTML($!globals.get($row,5))</p>
			    </div>
			</div>
		    #end
		    #end
       #else
		   #set($brotherIndex = 1) 
	       #foreach($brother in $brotherList)
			    <div #if($brotherIndex == 1) class="zkbg col" #else class="col" #end>
				    <ul #if($brotherIndex == 1) class="" style="padding:0;overflow:hidden;" #end>
				    	#set($detailVal = "")
				    	#foreach($fieldName in $displayFields)
				    		#set($field = $globals.getFieldBean($!tableInfo.tableName,$fieldName))
							#set($fieldVal = "")
							#if("$field.inputType"=="0")
								#if("$field.fieldType" == "1")
									#set($fieldVal = $!globals.dealDoubleDigits("$!brother.get($field.fieldName)","amount"))
								#elseif("$fieldBean.fieldType" == "13"||"$fieldBean.fieldType" == "14")
								#elseif("$field.fieldType" == "18" || "$field.fieldType" == "3" || "$field.fieldType" == "16")
									#set($fieldVal = $globals.replaceHTML($!brother.get($field.fieldName)))
								#else
									#set($fieldVal = $!brother.get($field.fieldName))
									
								#end
							#elseif("$field.inputType"=="1" || "$field.inputType"=="5")
								#set($fieldVal = $globals.getEnumerationItemsDisplay("$field.refEnumerationName","$!brother.get($field.fieldName)"))	
							#elseif("$field.inputType"=="2")
								#if("$field.fieldName" == "ClientId")
									#set($fieldVal = $!brother.get("clientName"))
								#else
									#set($fieldVal = $!brother.get($field.fieldName))
								#end
							#elseif("$field.inputType"=="14")
								#set($fieldVal = $!deptMap.get($!brother.get($field.fieldName)))
							#elseif("$field.inputType"=="15")
								#set($fieldVal = $!globals.getEmpFullNameByUserId($!brother.get($field.fieldName)))
							#elseif("$field.inputType"=="20")
								#set($fieldVal = $!relateClientMap.get($!brother.get($field.fieldName)))
							#else
								#set($fieldVal = $!brother.get($field.fieldName))
							#end
				    		
				    		#if($velocityCount == 5)
				    			#set($detailVal = $fieldVal)
				    		#else
					    		<li #if($velocityCount == 1) class="t1" title='$fieldVal' style="height:21px;overflow:hidden;  #if($brotherIndex == 1) background-position: 0px -642px; #end"  #elseif($velocityCount == 4) class="t4" #else  #end>$fieldVal&nbsp;#if($velocityCount == 4)<span style="float: right;margin-right: 3px;cursor: pointer;"  onclick="javaScript:parent.parent.billDetail('$tableInfo.tableName','$brother.get("id")')" >详情</span>#end</li>
				    		#end
				    	#end
				    </ul>
				    <div #if($brotherIndex == 1) style="display:block;" #else style="display:none;" show="no" #end class="cont_dv" >
				    	<p>$detailVal</p>
				    </div>
			    </div>
		    #set($brotherIndex = $brotherIndex + 1) 
		    #end
        #end
	    </div>
	    <div id="scroRight" >
              <div id="scroLine"></div>
        </div>
   </div>
</body>
<script type="text/javascript" src="/js/crm/scroll.js"></script> 
</html>