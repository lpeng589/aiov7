<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self"></base>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$tableDisplayName</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<style type="text/css">
.contextMenuPlugin {-webkit-user-select: none;display: none;font-family: tahoma, arial, sans-serif;font-size:11px;position:absolute;left: 100px;top:100px;min-width:100px;list-style-type:none;margin:0;padding:0;background-color: #f7f3f7;border: 2px solid #f7f7f7;outline: 1px solid #949694;}
.contextMenuPlugin li{margin: 0 0 0 0padding: 1px;background-repeat: no-repeat;cursor:pointer;line-height:12px;}
.contextMenuPlugin li a{position: relative;display: block;padding: 3px 3px 3px 28px;color: ButtonText;text-decoration: none;font-size: 12px;}
.contextMenuPlugin li a div {position: absolute;left: 3px;margin-top: -3px;width: 18px;height: 18px;}
.contextMenuPlugin li:hover {outline: 1px solid #b5d3ff;margin: 0;background: -webkit-linear-gradient(top, rgba(239,239,255,0.5) 0%,rgba(223,223,255,0.5) 100%);filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#80efefff', endColorstr='#80dfdfff',GradientType=0 ); background: linear-gradient(top, rgba(239,239,255,0.5) 0%,rgba(223,223,255,0.5) 100%); /* W3C */cursor: default;}
.contextMenuPlugin li.divider {border-top: 1px solid #e7e3e7;border-bottom: 1px solid #ffffff;height: 0;padding: 0;margin: 3px 0 3px 27px;}
.contextMenuPlugin .header {background: rgb(90,90,90);background: -webkit-linear-gradient(top, rgba(90,90,90,1) 0%,rgba(20,20,20,1) 100%);filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#5a5a5a', endColorstr='#141414',GradientType=0 ); /* IE6-9 */background: linear-gradient(top, rgba(90,90,90,1) 0%,rgba(20,20,20,1) 100%); /* W3C */position: relative;cursor: default;padding: 3px 3px 3px 3px;color: #ffffff;}
.contextMenuPlugin .gutterLine {position: absolute;border-left: 1px solid #e7e3e7;border-right: 1px solid #ffffff;width:0;top:0;bottom:0;left:26px;z-index: 0;}
#listid .cbox {float:left;display:inline-block;border:0px;width:15px;margin:6px 0 0 0;padding:0;}
#listid .cbox_w {float:left;display:inline-block;line-height:29px;font-style:normal;margin:0;padding:0;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/popupGrid.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="$globals.js("/js/Main.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/popupPage.vjs","",$text)"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/k_listgrid.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript"> 
	var hasChild=false;
	var childPop;
	var selectName;
	var mainId;
	var isChild=false; //单据引用时在第二个明细界面
	var isMain = "$!isMain"; //表示是否主表弹出窗，用于回填时，回填主表还是明细表
	var hasClass=false;
	var rows=0;
	var popType="$!popBean.getType()";
	var returnCols = "$!returnCols";
	
	var detailPopup="$!detailPopup";
	
	#if($!popBean.getHasChild().length()>0)
     hasChild=true;    
	 childPop="$!popBean.getHasChild()";
	 selectName="$!popBean.getName()";
    #end
	#if($!mainPop.length()>0)
		isChild=true;
		mainId="$!mainId";
	#end
    #if ($!hasClass)
		hasClass=true;	
    #end    

var varIds = "" ;
#if("$!topId" == "") ##如果是多单引用明细表不能把上一页的varIds传入到下一页并返回
#foreach($str in $listValue)
	#if("$str"!="#;#")
		varIds += "$str"+"#|#" ;
	#end
#end
#end

#set($root = $request.getParameter("root"))
var parentCode5 = "$globals.classCodeSubstring("$!parentCode",5)";
var mainPop = "$!mainPop";

var hasChildids = "$!mainId;childKeyId;";
var isQuote = "$!isQuote";

function isQuoteFuction(str){
   window.opener.openQuoteChild$!isQuote();
} 

function exeQuotepopup(str){
	window.parent.exeQuotepopup$!isQuote(str);
}


var retpopvalue='';
#foreach($field in $!popBean.getReturnFields())
	retpopvalue=retpopvalue+'$field.getDefaultValue()#;#';
#end


var resultSize = $result.size();
var keyId = "$!keyId";
var popBeanType = "$!popBean.getType()";
var saveParentFlag = $!popBean.isSaveParentFlag();
var MOID="$MOID";
var varColWidth = $!newCols.size() ;
var strSelectName = "$!selectName" ;
var tableName = "$!tableName" ;
var parentTableName = "$!parentTableName";

function selectConfig(){
	AjaxRequest("${strAction}&selectType=config") ;
	document.getElementById("selectConfig").innerHTML = response ;
}

function onItemClick(str){
	//alert(str);	
}
function onItemDBClick(str){
	str = decodeURIComponent(str); 
	eval(str);
}
function nextClass(strs){
	if(strs.split(":").length==2){
		form.action="/UserFunctionAction.do?root=root&operation=22&selectName="
					+ strs.split(":")[0] + "&MOID="+MOID+"&popDataType=$!popDataType&MOOP=add&" 
					+ "LinkType=@URL:&displayName=%E5%88%86%E7%BA%A7%E7%9B%AE%E5%BD%95";
		form.submit();
	}else{
		hasClassBill(true,strs);
	}
}

function nextClass2(strs){
	if(strs.split(":").length==2){
		form.action="/UserFunctionAction.do?root=root&operation=22&selectName="
					+ strs.split(":")[0] + "&MOID="+MOID+"&popDataType=$!popDataType&MOOP=add&" 
					+ "LinkType=@URL:&displayName=%E5%88%86%E7%BA%A7%E7%9B%AE%E5%BD%95";
		form.submit();
	}else{
		hasClassBill(true,strs);
	}
}

function onFlashKeyDown(keyCode,ctrl,alt,shiftKey){
	if(keyCode==27){
		window.close() ;
	}
}

var pupupWin = "$!popupWin";

function doAutoCommit(){
	closeSubmit()
	window.parent.jQuery.close(pupupWin);	
}
</script>

</head>

<body onLoad="setIniFocus();#if("$!autoCommit"=="true") doAutoCommit();  #end" scroll="no"> 
<form  method="post" scope="request" name="form" onKeyDown="down() " action="/UserFunctionAction.do">
 <input type="hidden" name="root" value="$!root">
  <input type="hidden" name="isRoot" value="">
 <input type="hidden" name="operation" value="$globals.getOP('OP_POPUP_SELECT')">
 <input type="hidden" name="tableName" value="$!tableName">
 <input type="hidden" name="parentTableName" value="$!parentTableName">
 <input type="hidden" name="fieldName" id ="fieldName"  value="$!fieldName">
 <input type="hidden" name="popDataType" value="$!popDataType">
 <input type="hidden" name="parentCode" value="$!parentCode">
 <input type="hidden" name="selectName" value="$!selectName">
 <input type="hidden" name="popupType" value="$!popupType">
 <input type="hidden" name="displayName" value="$!displayName">
 <input type="hidden" name="reportNumber" value="$!reportNumber">
 <input type="hidden" id="reportName" name="reportName" value="$!reportName">
 <input type="hidden" name="backType" value="">
 <input type="hidden" name="isQuote" value="$!isQuote"/>
 <input type="hidden" name="MOID" value="$!MOID">
 <input type="hidden" name="MOOP" value="$!MOOP">
 <input type="hidden" name="iniPropField" value="$!iniPropField">
 <input type="hidden" name="keyIdType" value="saveType">
 <input type="hidden" name="queryChannel" value="$!queryChannel"/>
 <input type="hidden" name="strAction" value="$strAction" />
 <input type="hidden" name="nextClass" value="">
 <input type="hidden" name="popPop" value="$!popPop">
 <input type="hidden" name="noOp" value="$!noOp">
 <input type="hidden" name="popupWin" value="$!popupWin">
 <input type="hidden" name="isMain" value="$!isMain">
 <input type="hidden" name="topId" value="$!topId">
 <input type="hidden" name="toDetail" value="$!toDetail">
 <input type="hidden" name="checkTab" id="checkTab" value="Y">
 
 #if($!mainId.length()>0)
	<input type="hidden" name="mainId" value="$!mainId">
 	<input type="hidden" name="mainPop" value="$!mainPop">
 #end
 <input type="hidden" name="queryField">
 $!mainHiddenFields
 <div class="Heading" >
	<div class="HeadingTitlesmall">$!tableDisplayName</div>
	
	<ul class="HeadingButton_Pop-up h-child-btn">
		#if($conditions.size()>0 && $!mainId.length()==0)
		<li>
		    <span class="btn btn-small" onClick="javascript:beforeSubmit();" >$text.get("common.lb.query")</span>		
		</li>
		#end
		#if($!mainId.length()>0 && "true"!="$!popPop")
		<li><span class="btn btn-small" onClick="backMain()">$text.get("common.lb.backmainlist")</span></li>		
		#end
		
		#if("$!detailPopup" !="")
		<li><button  type="button" onClick="gotoDetail('$!detailPopup')">查看明细</button></li>		
		#end
		
		#if("$!toDetail" =="true")
		<li><button  type="button" onClick="backTop('$!topPopup')">返回上级</button></li>		
		#end
		
		#if($!popBean.getForwardModel().length()>0 && "true"!="$!popPop" && "$!modelAdd"=="true" && "$!cantAddLevel" !="true")
			#if(("$!parentCode"!="" && "$tableName"=="tblAccDetail") || "$tableName"!="tblAccDetail")
				#if("$!tableName"!="tblUser")
					#set($ftb = $!popBean.getForwardModel())
					#if($ftb.indexOf("&")>0 ) #set($ftb = $ftb.substring(0,$ftb.indexOf("&")))  #end
					<li> <span class="btn btn-small"button name="addPage" 
					onClick="openNewWin('/UserFunctionQueryAction.do?tableName=$!popBean.getForwardModel()&parentCode=$!parentCode&operation=6&forward=add&fresh=dialog',$!globals.getTableInfoBean($!ftb).tWidth,$!globals.getTableInfoBean($!ftb).tHeight);">$text.get("common.lb.add")</span></li>
				#end
			#end
		#end
		#if("$!isQuote"==""&& "$!root" != "root" && "$!noOp" !="true")
		<li><span class="btn btn-small" onClick="resetSubmit()">$text.get("com.db.clear")</span></li>
		#end
		
	#if("$!noOp" !="true")
		<li><span class="btn btn-small" onClick="closeSubmit()">$text.get("common.lb.ok")</span></li>
		#if("$!popDataType" =="dataMove")
		<li><span class="btn btn-small" onClick="moveRoot()">移到根目录</span></li>
		#end
		#if("$!isRoot" != "root" && "$!parentCode" =="" && $!root == "root")
			<li><span class="btn btn-small" id="backSubmitBt" onClick="backSubmit2()">$text.get("common.lb.back")</span></li>	
		#end
		#if("$!parentCode" !="")
			<li><span class="btn btn-small" id="backSubmitBt" onClick="backSubmit()">$text.get("common.lb.back")</span></li>	
		#end
		<li><span class="btn btn-small" onClick="closeWindow();">$text.get("common.lb.close")</span></li>
	#end
	</ul>
</div>
<div class="listRange_Pop-up" id="midDiv">
			#if($conditions.size()>0 && $!mainId.length()==0)
	<ul class="listRange_1_Pop-up" id="listid">
			#if($!popBean.keySearch)
			<li>
				<span>$text.get("com.lb.keySearch")</span>
				<input class="ipt" name="keySearch" value="$!globals.replaceSpecLitter($!keySearch)" onKeyDown="if(event.keyCode==13) {form.queryField.value=this.name;beforeSubmit()}" />
			</li>
			#end
			#foreach($row in $conditions)
			#if($!globals.get($row,3) == "0")
			<li>
				<span>$!globals.get($row,0)</span>
				<input class="ipt" name="$!globals.get($row,1)" value="$!globals.replaceSpecLitter($!globals.get($row,2))" onKeyDown="if(event.keyCode==13) {form.queryField.value=this.name;beforeSubmit()} " />
			</li>
			#elseif($!globals.get($row,3) == "1")
			<li>
				<span>$!globals.get($row,0)</span>
				<input class="ipt" name="$!globals.get($row,1)" date="true" value="$!globals.replaceSpecLitter($!globals.get($row,2))" onKeyDown="if(event.keyCode==13) WdatePicker({lang:'$globals.getLocale()'});" onClick="WdatePicker({lang:'$globals.getLocale()'});" />
			</li>
			#elseif($!globals.get($row,3) == "2")
			
			<li>
				<span>$!globals.get($row,0)</span>
				<select class="slt" name="$!globals.get($row,1)" >
					<option value="" ></option>
					#foreach($erow in $globals.getEnumerationItems($!globals.get($row,4)))
					<option value="$!globals.replaceSpecLitter($erow.value)" #if("$erow.value"=="$!globals.get($row,2)") selected #end>$erow.name</option>
					#end
				</select>
			</li>
			#elseif($!globals.get($row,3) == "3")
			<li>
				<span>$!globals.get($row,0)</span>
				<input class="ipt" name="$!globals.get($row,1)" value="$!globals.replaceSpecLitter($!globals.get($row,2))" onDblClick="openSelect('$!globals.get($row,0)','$!globals.get($row,4)','$!globals.get($row,1)')" />
				<img src="/$globals.getStylePath()/images/St.gif" class="search" onClick="openSelect('$!globals.get($row,0)','$!globals.get($row,4)','$!globals.get($row,1)')" />
			</li>
			#elseif($!globals.get($row,3) == "4")
			<li>
				<span>$!globals.get($row,0)</span>
				#foreach($erow in $globals.getEnumerationItems($!globals.get($row,4)))
				<input type="checkBox" name="$!globals.get($row,1)" value="$!globals.replaceSpecLitter($erow.value)" class="cbox"  #if("$erow.value"=="$!globals.get($row,2)") checked #end />
				<label class="cbox_w">$erow.name</label>
				#end
			</li>
			#end
			#end
	</ul>
			#end
	#if($parentName.length()>0)			
	<div class="scroll_function_big">
		<span>$text.get("common.msg.currstation")：$!parentName</span>
	</div>
	#end
	<div class="select-list-wp">
		<div class="listRange_Pop-up_scroll" id="conter" style="overflow:hidden">
			<div style="width:100%;height:100%;overflow:hidden;position:relative">
				$popupHTML
			</div>
		</div>
		<script type="text/javascript">
		jQuery(function(){
			jQuery("#k_data").height($(this).height()-$("#listid").height()-110);
			jQuery("#k_column").height(jQuery("#k_data")[0].clientHeight);
			jQuery("#k_head").width(jQuery("#k_data")[0].clientWidth);
			drag22();
			popMenu();
			jQuery("#k_data").scroll(function () { 
				jQuery("#k_head").scrollLeft(jQuery("#k_data").scrollLeft()); 
				jQuery("#k_column").scrollTop(jQuery("#k_data").scrollTop()); 
			}); 
		});
		</script>	
		#if($!popBean.getType().equals("multiSeleToRow"))
		<div style="float:right; width:150px; padding-top:2px; text-align:right;">
			<button type="button" id="ok" onClick="addSelRow()" class="b2">$text.get("common.lb.moveRight")</button>
		</div>
		#end
	</div>
	<div style="width:288px; #if($!popBean.getType().equals('multiSeleToRow'))display:block;#else display:none;#end overflow:hidden">
		<div class="listRange_Pop-up_scroll_2" id="conter2">
			<table border="0" width="100%" cellpadding="0" cellspacing="0" class="selectlist" name="table" id="tblSelTable">
						
			</table>
		</div>
	</div>
	
</div>
	#if($!mainPop.length()>0)	
	#else	
	<div class="page-wp">
		<div class="listRange_pagebar"> $!pageBar </div>
	</div>
	#end
</form>
<script type="text/javascript">
	$("#midDiv").height($(window).height() - 85);
	$("#conter").height($("#midDiv").height()-($("#listid").height()-10));
</script>
<div>
<div id="selectConfig">
</div>

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
