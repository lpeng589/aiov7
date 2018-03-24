<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$reportName</title>
<link type="text/css" href="/$globals.getStylePath()/css/classFunction.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/CRselectBoxList.css" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css" />
<link type="text/css" rel="stylesheet" href="/style/css/popOperation.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" /> 
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<style type="text/css">
.HeadingTitle{white-space:nowrap;}
.scroll_function_small_a>div{background:#fff;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.divbox.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>

<script type="text/javascript" src="$globals.js("/js/listGrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/function.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/defineList.vjs",$!tableName,$text)"></script>
<script type="text/javascript" src="$globals.js("/js/k_listgrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/popOperation.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/dropdownselect.js","",$text)"></script>

<script language="javascript">

var idListArray = new Array(); //用于存储本页所有id值，用于上一条下一条的索引，新增后id也会存在这里，使新增后的单据也且有上一条的功能
var curloginUserId = '$LoginBean.id';
saveDraft="$!saveDraft";
winCurIndex="$!winCurIndex";
draftQuery = "$!draftQuery";
lngIfClass = "$!lngIfClass";
f_brother = "$!f_brother" ;
parentCode="$!parentCode";
var parentTableName = "$!parentTableName" ;
var reportNumber = "$!BillRepNumber" ;
var AuditPrint = "$!AuditPrint" ;//审核后打印   
var cols=[];
#foreach($c1 in $cols)
	cols[cols.length] = [ '$globals.get($c1,0)','$globals.get($c1,4)'];
#end

function doPrint(){ //这方法不宜放入defineList
	pname = $(".printMore").find(".selectedprintMore").text();
	pfile = $(".printMore").find(".selectedprintMore").attr("file");
	
	jQuery.close("printMoreFormat");

	var keyVal="";		
	var keyIds=document.getElementsByName("keyId");
	for(var i=0;i<keyIds.length;i++){
		if(keyIds[i].checked){
			billID=keyIds[i].value;	
	try{				
		var mimetype = navigator.mimeTypes["application/np-print"];		
		if(mimetype){
			if(mimetype.enabledPlugin){
				 var cb = document.getElementById("plugin");
				 cb.print('$!IP|$!port',pname+"@col"+pfile+"@row",billID,'','$!tableName','$!JSESSIONID','$LoginBean.id','zh_CN',"");
			}
		}else{
			var obj = new ActiveXObject("KoronReportPrint.BatchPrint") ;
		    obj.URL="$!IP|$!port" ;
		    obj.SQL="" ;
		    obj.StyleList =pname+"@col"+pfile+"@row" ;
		    obj.BillID=billID ;
		    obj.BillTable="$!tableName" ;
		   	obj.Cookie ="$!JSESSIONID" ;
		   	obj.UserId ='$LoginBean.id' ;
		    obj.ReportNumber="" ;
		    obj.Language = "$!globals.getLocale()" ;
			obj.doPrint();	
		}
	}catch (e){
		asyncbox.alert("$text.get('com.sure.print.control')<br><br><a class='aio-print' href='/common/AIOPrint.exe' target='_blank'>下载控件</a>","信息提示");
	}
	
			}
	}
}

function selPrintMoreFormat(){
	
	mfhtml = "<ul class='printMore'>"+
	#foreach($row in $formatList)
	"<li file='$!globals.get($row,1)' onclick='selPmf(this)' ondblclick='doPrint()' #if($velocityCount==1) class='selectedprintMore' #end >$!globals.get($row,0)</li>"+
	#end
	"</ul>";
	asyncbox.open({id:'printMoreFormat',title:'选择样式',
		html:mfhtml,
		btnsbar:jQuery.btn.OKCANCEL,
	    callback:function(action,opener){
		    if(action == 'ok'){
				doPrint();
			}
　　     	},width:200,height:200
	});
}


//审核状态的超链接  
function status(str){
	st = "/OAMyWorkFlow.do?keyId="+str.split(",")[0]+"&operation=$globals.getOP("OP_DETAIL")&tableName=$!tableName&applyType=$!designId";
	var winWidth = 1024;
	if($(window).width()>1024){
		winWidth = 1150;
	}
	window.open(st,null,"height=570, width="+winWidth+",top=50,left=100 ");
}

var notOpenAccount = false;//未开帐不能做单 
#if($!LoginBean.sessMap.get('AccPeriod').accMonth < 0)		
	#if($!tableInfo.SysParameter =="UnUseBeforeStart" ||
         $!tableInfo.SysParameter == "CurrentAccBefBillAndUnUseBeforeStart"||($!tableInfo.tableName=="tblFixedAssetAdd"&&$!moduleType=="1"))
        notOpenAccount = true;
    #end 
#end



var updateRight = $globals.getMOperationMap($request).update();
var printRight = $globals.getMOperationMap($request).print();
var MOID="$globals.getMOperationMap($request).getModuleId()";



var draftUpdate = '$!globals.getSysSetting("draftUpdate")' ;
var draftQuery = "$!draftQuery" ;
var moduleType = "$!moduleType" ;

#if("$!formMenu"=="menu" && "$!showQuery"=="1")
var varOpacity = 1 ;
#end

#foreach($row in $userConditions)
#if($!globals.get($row,3) == "0" || $!globals.get($row,3) == "2" || $!globals.get($row,3) == "4")
#if("$!globals.get($row,7)"=="1")
putValidateItem("$!globals.get($row,1)","$!globals.get($row,0)","$!globals.get($row,8)","",false,minValue,maxValue,true) ;
#else
putValidateItem("$!globals.get($row,1)","$!globals.get($row,0)","$!globals.get($row,8)","",true,minValue,maxValue,true) ;
#end
#end
#end



//储存所有条件信息
#if("$!dateConditionsStr" != "")
var dateConditions = $!dateConditionsStr; 
#end
#if("$!workFlowNodeNameCondStr" != "")
var workFlowNodeNameCond =  $!workFlowNodeNameCondStr 
#end;
#if("$!conditionsStr" != "")
var otherConditions = $!conditionsStr 
#end;


$(document).ready(function(){

});

</script>
</head>
<body onLoad="checkHasFrame();showStatus();setListFocus(); initDownSelect();showSerach(); "  onKeyDown="down()" style="overflow:hidden">

 <div id="viewOpDiv" class="custom-menu" style="z-index: 99; position: absolute; left: 100px; top: 20px; display: none;">
    <ul >        
    </ul>
</div>
<div id="Larger_content">
<iframe   name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" id="form"  name="form" action="/UserFunctionQueryAction.do"  target="">
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_QUERY")" />
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()" />
<input type="hidden" id="tableName" name="tableName" value="$!tableName" />
<input type="hidden" id="optionType" name="optionType"  value="" /> 
<input type="hidden" id="parentCode" name="parentCode" value="$!parentCode" /> 
<input type="hidden" id="f_brother" name="f_brother" value="$!f_brother" />
<input type="hidden" id="defineName" name="defineName" value="" />
<input type="hidden" id="winCurIndex" name="winCurIndex" value="$winCurIndex" />
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName" />
<input type="hidden" id="reportName" name="reportName" value="$!reportName"/>
<input type="hidden" id="moduleName" name="moduleName" value="$!moduleName"/>
<input type="hidden" id="isAllListQuery" name="isAllListQuery" value="$!isAllListQuery"/>
<input type="hidden" id="parentCodeList" name="parentCodeList" value=""/>
<input type="hidden" id="src" name="src" value="" />
<input type="hidden" id="queryChannel" name="queryChannel" value="$!queryChannel" />
<input type="hidden" id="newCreateBy" name="newCreateBy" value=""/>
<input type="hidden" id="wakeUp" name="wakeUp" value=""/>
<input type="hidden" id="sendMessage" name="sendMessage" value=""/>
<input type="hidden" id="classCode" name="classCode" value=""/>
<input type="hidden" id="SQLSave" name="SQLSave" value="$!globals.replaceSpecLitter($!SQLSave)" />
<input type="hidden" id="right" name="right" value="$!globals.replaceSpecLitter($!SQLSave)" />
<input type="hidden" id="draftQuery" name="draftQuery" value="$!draftQuery" />
<input type="hidden" id="export" name="export" value="" />
<input type="hidden" id="query" name="query" value="" />
<input type="hidden" id="defineInfo" name="defineInfo" value="" />
<input type="hidden" id="isTab" name="isTab" value="$!isTab" />
<input type="hidden" id="ButtonType" name="ButtonType" value="" />
<input type="hidden" id="ButtonTypeName" name="ButtonTypeName" value="" />
<input type="hidden" id="varKeyIds" name="varKeyIds" value="" />
<input type="hidden" name="brotherEnter" id="brotherEnter" value="$!brotherEnter" />
<input type="hidden" id="hasFrame" name="hasFrame" value=""/>
<input type="hidden" name="detTable_list" id="detTable_list" value="$!detTable_list">
<input type="hidden" name="checkTab" id="checkTab" value="Y">
<input type="hidden" name="popReturnVal" id="popReturnVal" value="">
<input type="hidden" name="defineOrderBy" id="defineOrderBy" value="$!defineOrderBy">


#if("$!existShop"=="exist" && "$!mainTableName"=="tblGoods")
<input type="hidden" id="opType" name="opType" value="" />
<input type="hidden" id="updateImg" name="updateImg" value="" />
<input type="hidden" id="shelfType" name="shelfType" value="" />
#end
#if("$!mainTableName"=="tblView")
<input type="hidden" id="billTypes" name="billTypes" value="" />
#end

<div class="Heading">
	#if(""=="$!f_brother")
	<div class="HeadingTitle">
		<b class="icons b-list"></b>
		#if("$!moduleName"=="")$!reportName#else$!moduleName#end #if("$!draftQuery"=="draft"||"$!draftQuery"=="quoteDraft")$text.get("common.draft.list")#end
	</div>
	#end
	<ul class="HeadingButton f-head-u">		
		
		#if($!parentCode !="")
			<li><span class="btn btn-small" id="backList" name="backList" id="backSubmitBt" onClick="location.href='/UserFunctionQueryAction.do?tableName=$tableName&parentCode=$globals.classCodeSubstring("$!parentCode",5)&operation=$globals.getOP("OP_QUERY")&parentTableName=$!parentTableName&moduleType=$!moduleType&winCurIndex=$!winCurIndex&checkTab=Y'" title='$text.get("common.lb.back")'>$text.get("common.lb.back")</span></li>
		#end   
		#if("$!winCurIndex"!="" || "$!parentTableName"!="")  
			#if($globals.getMOperationMap($request).add()&&!$!cantAddLevel)
				#if("$!tableName"=="PDMRP")
				<li><span class="btn btn-small" id="addPre" name="addPre" id="addPre" #if($!CannotOper)disabled="true"#end  title="Ctrl+D" onClick="openPop('PopMainOpdiv','','/Mrp.do?type=selSalesOrder',document.documentElement.clientWidth,document.documentElement.clientHeight,true,true)" title='$text.get("common.lb.add")'>$text.get("common.lb.add")</span></li>
				#else
					#if("$!tableName"!="tblWorkFlowType" && "$!CannotOper" != "true")	<!--工作流类型设置不需要添加按钮-->		
						<li><span class="btn btn-small" id="addPre" name="addPre" id="addPre" #if($!CannotOper)disabled="true"#end title="Ctrl+D" onClick="addPrepare();" title='$text.get("common.lb.add")'>$text.get("common.lb.add")</span></li>
					#end
				#end
			#end			 
		#end			
		
		<li>			
			<div class="btn btn-small h-child-btn">
			<em>操作</em>
			<div id="list_operatoin" class="d-more">
				<div class="out-css">

				
				#if(($globals.getMOperationMap($request).delete() || "true"==$globals.getSysSetting("draftUpdate")) 
					 && "$!CannotOper" != "true")	
					<a href="javascript:void(0);#if("$!CannotOper" !="true")deleteList('$!rowRemark')#end " title='$text.get("common.lb.del")'>$text.get("common.lb.del")</a>
				#end
				
				#foreach($brow in $customButton)			
					$!brow
				#end
				$!extendButton
							
				#if("$!globals.getVersion()"=="8" && "$!tableName"=="tblBOM" && $globals.getMOperationMap($request).add())
				<a href="javascript:void(0);copyBom();" title='$text.get("common.lb.copy")'>$text.get("common.lb.copy")</a>
				#end
				#if($!OAWorkFlow)
				<a href="javascript:void(0);deliverToAll();" title='$text.get("Bill.batch.Auditing")'>批量审核</a>	
				#end	
				#if($tableInfo.draftFlag==1)
				<a href="javascript:void(0);draftAudit('$!rowRemark');" title="草稿过帐" >草稿过帐</a>
				#end
				
				#if("$!billExport"=="OK" && $globals.getMOperationMap($request).print() && $tableName!="tblBillView" && "$showDetail" == "1")
					<a href="javascript:void(0);extendSubmit('billExport','导出单据')" title="导出单据" class="b2">导出单据</a>
					<input type="hidden" id="ButtonType" name="ButtonType" value="billExport">
				#end	
				#if("$!existShop"=="exist" && "$!mainTableName"=="tblSalesOrder")
				<a href="javascript:void(0);getShopOrder();" class="b2" title='$text.get("common.msg.SynchronizeInternetOrder")'>$text.get("common.msg.SynchronizeInternetOrder")</a>
				#end  			
				#if($globals.getMOperationMap($request).print())
				<a href="javascript:void(0);exportAll();" class="b3" title='导出列表'>导出列表</a>
				#end	
				
				
				
				</div>	
			</div>
			<b class="triangle"></b>
			</div>		 	
		</li>
		#if("$!toolBtn" != "")
		<li>
			<div class="btn btn-small h-child-btn">
				<a id="tb_toolBtn" moreId="toolMore">工具</a>
				<div id="toolMore" class="d-more" >
					<div class="out-css">
						$!toolBtn
					</div>				
				</div>
				<b class="triangle"></b>
			</div>			
		</li>
		#end
		
		#if("$!queryBtn" != "")
		<li>
			<div class="btn btn-small h-child-btn">
				<em id="tb_mr1" onClick="">关联报表</em>
				<div id="more1" class="d-more">
					<div class="out-css">
						$!queryBtn		
					</div>	
				</div>
				<b class="triangle"></b>
			</div>
		</li>
		#end
		
		#if($globals.getMOperationMap($request).print())
		<li>
			#if("$print"=="true"&& $globals.getMOperationMap($request).print()) 
			<div class="btn btn-small h-child-btn">
			<em id="tb_mr1" onClick="">打印</em>
			<div class="d-more">
				<div class="out-css">
					<a href="javascript:void(0)" onClick='printData()'>$text.get("common.lb.printData")</a>
					<a href="javascript:void(0)" onClick='printMoreData()'>$text.get('common.lb.more.printData')</a>
					#if("$!printList" =="true")
					<a href="javascript:void(0)" onClick="printControl('/UserFunctionQueryAction.do?tableName=$tableName&moduleType=$!moduleType&reportNumber=$!tableName&operation=$globals.getOP("OP_PRINT")&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex')">打印列表</a>
					#end
					#if("$!printLabel"=="label")
					<a href="javascript:void(0)" onClick='printLabelData()'>$text.get('com.label.print')</a>
					#end
				</div>
			</div>
			<b class="triangle"></b>
			</div>
		 	#else
			  #if($tableName!="tblMSGroup" && "$!printList" =="true") 
		 		<span class="btn btn-small" onClick="printControl('/UserFunctionQueryAction.do?tableName=$tableName&reportNumber=$!tableName&moduleType=$!moduleType&operation=$globals.getOP("OP_PRINT")&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex');" >$text.get("common.lb.print")</span>
			   #end
		 	#end
		</li>
		#end
		 
		#if("$f_brother" != "")
		<li><span class="btn btn-small"  defbtn="backList" onClick="window.save=true;closeWindows('$!popWinName');">$text.get("common.lb.back")</span></li>	
		#end
		
		<li id="refreshBt" style="display:none"><span class="btn btn-small"  defbtn="backList" onClick="if( beforeButtonQuery()) {submitQuery();}">刷新</span></li>
	</ul>
</div>
<div class="listRange_frame" id="listRange_frame">
		<div class="listRange_3" id="conditionDIV"  >
		<table><tr><td>
		<div style="height:55px;max-width:840px;float:left" id="conditionDIV2" >
			<ul class="search-list-ul" >	
#set($totalFields=0) 

#if("$!userDateConditions" != "") #set($totalFields=$totalFields +2)
		<li style="min-width:600px;width:75%;" class="dateQueryLi">
			<div class="swa_c1">
				<div class="d_box"><div class="d_test">$!globals.get($userDateConditions,0)</div></div>
			</div>
			<div class="swa_c2">
				<input id="date_start" class="ls3_in" style="width:105px;" name="$!globals.get($userDateConditions,1)" date="true" value="$!globals.get($userDateConditions,3)" onKeyDown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}" onClick="openInputDate(this);" >
				-
				<input id="date_end" class="ls3_in" style="width:105px;" name="$!globals.get($userDateConditions,2)" date="true" value="$!globals.get($userDateConditions,4)" onKeyDown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}" onClick="openInputDate(this);" >
				
				<a href="javascript:void(0)" class="qd_a" id="qdate_zt">昨天</a>
				<a href="javascript:void(0)" class="qd_a"  id="qdate_jt">今天</a>
				<a href="javascript:void(0)" class="qd_a"  id="qdate_yz">7天内</a>
				<a href="javascript:void(0)" class="qd_a"  id="qdate_yy">30天内</a>
				<a href="javascript:void(0)" class="qd_a"  id="qdate_by">本月内</a>
			</div>
		</li>			
#end
#if("$!userWorkFlowNodeNameCond" != "") #set($totalFields=$totalFields +1) 
	<li>
		<div class="swa_c1" #if($!globals.get($userWorkFlowNodeNameCond,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($userWorkFlowNodeNameCond,0)</div></div></div>
		<div class="swa_c2">
			<select class="ls3_in" id="$!globals.get($userWorkFlowNodeNameCond,1)" name="$!globals.get($userWorkFlowNodeNameCond,1)" onKeyDown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}">
			<option title="abc" value="123" >--请选择--</option>
			#foreach($erow in $!nodes)
				<option title="$erow.name" value="$erow.value" #if("$erow.value" == "$!globals.get($userWorkFlowNodeNameCond,2)") selected #end>$erow.name</option>
			#end
			</select>
		</div>
	</li>
#end


#set($queryType = $!conditinSessionMap.get("queryType"))
#foreach($row in $userConditions)#set($totalFields=$totalFields+1)
	#if($!globals.get($row,3) == "0")
	<li>
		<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
		<div class="swa_c2">
			<input class="ls3_in" id="$!globals.get($row,1)" name="$!globals.get($row,1)" value="$!globals.replaceSpecLitter($!globals.get($row,2))" onKeyDown="if(event.keyCode==13) {if(!beforeButtonQuery())return false ; submitQuery();}" >
		</div>
	</li>
	#elseif($!globals.get($row,3) == "1")			
	<li>
		<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
		<div class="swa_c2">
			<select class="ls3_in" id="$!globals.get($row,1)" name="$!globals.get($row,1)" onKeyDown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}">
				#if($!globals.get($row,7)!=1)<option value="" ></option>#end
				#foreach($erow in $globals.getEnumerationItems($!globals.get($row,4)))
					<option title="$erow.name" value="$!globals.replaceSpecLitter($erow.value)" #if("$erow.value" == "$!globals.get($row,2)") selected #end>$erow.name</option>
				#end
			</select>
		</div>
	</li>
	#elseif($!globals.get($row,3) == "16")			
	<li>
		<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
		<div class="swa_c2">
			<select id="$!globals.get($row,1)" class="ls3_in" name="$!globals.get($row,1)" vl="$!globals.replaceSpecLitter($!globals.get($row,2))" onKeyDown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}">
				#if($!globals.get($row,7)!=1)<option value="" ></option>#end
			</select>
		</div>
	</li>
	#elseif($!globals.get($row,3) == "5")			
		<li style="width:100%">
			<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
			<div class="swa_c2" >
				#foreach($erow in $globals.getEnumerationItems($!globals.get($row,4)))
				<input type="checkBox" style="color: #717171;width:20px;border:0px;" class="ls3_in cbox" id="$!globals.get($row,1)_$erow.value" name="$!globals.get($row,1)" #if("$erow.value"=="$!globals.get($row,2)")checked#end value="$!globals.replaceSpecLitter($erow.value)"><label for="$!globals.get($row,1)_$erow.value" class="cbox_w">$erow.name</label>
				#end
			</div>
		</li>
	#elseif($!globals.get($row,3) == "2")
		<li>
			<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
			<div class="swa_c2">
				<input id="$!globals.get($row,1)" class="ls3_in" name="$!globals.get($row,1)" date="true" value="$!globals.replaceSpecLitter($!globals.get($row,2))" onKeyDown="if(event.keyCode==13){if(!beforeButtonQuery())return false ; submitQuery();}" onClick="openInputDate(this);" >
			</div>
		</li>	
	#elseif($!globals.get($row,3) == "3")	
			
		<input type="hidden" class="ls3_in" id="$!globals.get($row,1)" name="$!globals.get($row,1)" value="$!globals.replaceSpecLitter($!globals.get($row,2))">
	
	#elseif($!globals.get($row,3) == "4")	
		<li>
			<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
			<div class="swa_c2">
				<input id="$!globals.get($row,1)" ondblclick="jQuery(this).next().click()"  belongTableName="$!globals.get($row,13)"  class="ls3_in h-icons" name="$!globals.get($row,1)" value="$!globals.replaceSpecLitter($!globals.get($row,2))"  onFocus="this.oldValue=this.value;this.select();" onKeyUp="showData(this,'$!globals.get($row,6)','$!globals.get($items,1)','$!globals.get($row,0)');"  relationkey="true" onblur="cl(this);">
				<b class="stBtn icon16" onClick="openSelect('$!globals.get($row,6)','$!globals.get($row,1)','$!globals.get($row,0)');"></b>
			</div>
		</li>
	#end
#end


#if("$!lngIfClass"=="1" && "$!conditions.size()"!="0")
	<li>
		<div class="swa_c1"><div class="d_box"><div class="d_test">搜索范围</div></div></div>
		<div class="swa_c2">
			<select id="selectType" class="ls3_in" name="selectType">				
				<option title="搜索所有最末级数据不包含目录" value="endClass" #if("$!selectType"=="endClass")selected #end>所有末级</option>
				<option  title="只在当前级别查询不包括子目录" value="normal" #if("$!selectType"=="normal")selected #end>当前级别</option>
			</select>
		</div>
	</li>
#end
	<div class="clear"></div>
	</ul>
	</div>
	</td><td style="width: 200px;vertical-align: top;">		
			<ul class="floatleft b-btns-ul">
	            <li style="float:none">
	            	<span id="btnConfirmSCA"  class="floatleft more-txt" >查询</span>
	            	<span id="btnClearSCA"  class="floatleft more-txt" style="border-radius: 0 0 0 0;" >清空</span>
	            	<span id="btnAdanceSCA"  class="floatleft more-txt" onclick="showAdvancePanel();" style="border-radius: 0 0 0 0;" >高级</span>
	            	<span class="floatleft icons more-search more-down" show="f" title="展开搜索列表" onselectstart="return false;"></span>
	            </li>
	       	</ul>
	       	<div class="clear"></div>
	</td></tr></table>	       	
	       	
</div>

#if($parentName.length()>0)			
<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span>
</div>
#end

<div class="scroll_function_small_a" id="conter" style="overflow:hidden" >
<div style="width:100%;height:100%;overflow:hidden;position:relative">
	$!tableList
</div>
</div>
<script language="javascript">
var condHeight= 80;
$(function(){
	
	$("#k_data").height($("#conter").height()-10);
	$("#k_column").height($("#k_data")[0].clientHeight);
	$("#k_head").width($("#k_data")[0].clientWidth);
	jQuery("#k_column").width(jQuery("#k_column>table").outerWidth(true));
	jQuery("#kt_head").width(jQuery("#k_column>table").outerWidth(true));
	
	jQuery("#k_data tbody tr").click(function(){jQuery("#k_column tbody").find("tr:eq("+jQuery(this).index()+")").toggleClass("highlightrow");jQuery(this).toggleClass("highlightrow");});
	jQuery("#k_column tbody tr").click(function(){jQuery("#k_data tbody").find("tr:eq("+jQuery(this).index()+")").toggleClass("highlightrow");jQuery(this).toggleClass("highlightrow");});
	
	drag22();
	popMenu();
	$("#k_data").scroll(function () { 
		$("#k_head").scrollLeft($("#k_data").scrollLeft()); 
		$("#k_column").scrollTop($("#k_data").scrollTop()); 
	}); 
});

	
var oDiv=document.getElementById("conter");
var dHeight=document.documentElement.clientHeight;
var varHeight=document.getElementById("listid");
var sHeight = 0 ;
if(typeof(varHeight)!="undefined" && varHeight!=null){
	sHeight = varHeight.clientHeight ;
}
#if($parentName.length()>0)
oDiv.style.height=dHeight-sHeight-90-condHeight +"px";
#else 
oDiv.style.height=dHeight-sHeight-55-condHeight +"px";
#end



t_width =($("#k_data >table").width());
if(t_width +50 < document.documentElement.clientWidth){
	$("#Larger_content").width(t_width+50).css({"margin":"0 auto","margin-top":"10px"});
	$("body").addClass("html");
}

cll = 0;
$("#k_data >table >tbody").find("tr").each( function(){
	if(cll ==0){
		$(this).addClass("spaceRow");
		cll = 1;
	}else{
		cll = 0;
	}
});
cll = 0;
$("#k_column >table >tbody").find("tr").each( function(){
	if(cll ==0){
		$(this).addClass("spaceRow");
		cll = 1;
	}else{
		cll = 0;
	}
});

//如果没有条件，则条件框隐藏
if($("#conditionDIV2 ul li").size() ==0){
	$("#conditionDIV").hide();
	$("#refreshBt").show();
	
}
           
</script>			
		
</div>	
<div class="page-wp">
	<div class="listRange_pagebar" style="position:relative"> $!pageBar </div>
</div>
<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>

<script type="text/javascript"> 
	var rowCount = document.getElementsByName("keyId").length ;
	if("$!pageSize"!=rowCount){
		var varPage = document.getElementById("nextPageSize") ;
		if(typeof(varPage)!="undefined" && varPage!=null){
			varPage.removeAttribute("href","") ;
		}
	}	
</script>
 <input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
</form>

#if($LoginBean.id =="1")
<form method="post" id="colConfigForm" name="colConfigForm" action="/ColConfigAction.do?operation=1">
<input type="hidden" id="tableName" name="tableName" value="$!tableName"/>
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName" />
<input type="hidden" id="parentCode" name="parentCode" value="$!parentCode"/> 
<input type="hidden" id="strAction" name="strAction" value="$globals.getModuleUrlByWinCurIndex("$!winCurIndex")&src=menu&operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex"/>
<input type="hidden" id="colType" name="colType" value="list" />
<input type="hidden" id="colSelect" name="colSelect" value="" />
</form>
#end
<script type="text/javascript">
	$(function(){
		cyh.lableAlign();
	});
</script>
</div>
#if($parentName.length()>0)		##有分级的生成关闭目录树的功能
<div id="closeTree" class="closeTreeDiv">
	<div class="cTree" onclick="clickTree()">&nbsp;</div>
</div>
<script type="text/javascript"> 
	function clickTree(){
		cols = window.parent.document.getElementsByTagName("frameset")[0].cols; 
		if(cols=="0,*"){
			window.parent.document.getElementsByTagName("frameset")[0].cols="190,*"; 
			$(".cTree").addClass("oTree").removeClass("cTree");
		}else{
			window.parent.document.getElementsByTagName("frameset")[0].cols="0,*"; 
			$(".oTree").addClass("cTree").removeClass("oTree");
		}
	}
</script>
#end
</body>
</html>
