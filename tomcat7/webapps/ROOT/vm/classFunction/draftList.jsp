<!DOCTYPE HTML>
<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self"></base>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$reportName $text.get("common.draft.list")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<script type="text/javascript" src="/js/AC_OETags.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/listPage.vjs","",$text)"></script>
<script type="text/javascript" src="/js/listGrid.js"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script> 
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="$globals.js("/js/popupPage.vjs","",$text)"></script>
<script type="text/javascript" src="/js/k_listgrid.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script> 
<script type="text/javascript">
if(typeof(top.junblockUI)!="undefined"){
	top.junblockUI();
}
var MOID="$MOID";
function G(id){
	return document.getElementById(id);
} 

function openInputDate(obj)
{
	WdatePicker({lang:'$globals.getLocale()'});
}

function down(){
	if(event.ctrlKey&&event.keyCode==68){
		if(document.createEvent){
    		var evt = document.createEvent("HTMLEvents");	 	    		
    		evt.initEvent("click",false,false);
    		form.addPre.dispatchEvent(evt);	
		  }else{
			form.addPre.fireEvent('onClick');
		  } 
		
		event.keyCode=9;
	}else if(event.ctrlKey&&event.keyCode==90){
		if(document.createEvent){
    		var evt = document.createEvent("HTMLEvents");	 	    		
    		evt.initEvent("click",false,false);
    		form.backList.dispatchEvent(evt);	
		  }else{
			form.backList.fireEvent('onClick');
		  } 		
		event.keyCode=9;
	}
}

function setIframSrc(srcUrl,obj){
	return ;
}
function setIframSrc2(srcUrl,obj){
	return ;
}
function dbSetIframSrc(srcUrl){
	var varKeyId = "" ;
	if(srcUrl.indexOf("keyId=")>=0){
	    var valStart=srcUrl.indexOf("keyId=")+6;
	    var valEnd=srcUrl.indexOf("&",valStart);
	    if(valEnd>0){
	    	varKeyId=srcUrl.substring(valStart,valEnd);
	    }else{
	    	varKeyId=srcUrl.substring(valStart);
	    }
	}
	if(varKeyId!=""){
		window.parent.exeQuoteDraft(varKeyId,"$!moduleType");
		window.parent.jQuery.close('QuoteDraft');
	}
}
function beforeSubmit(str){ 
	var varKeyId = str;
	if(str == undefined){
		var v = "";
		jQuery("input[name='keyId']").each(function(){if(this.checked)v+=$(this).val()});
		varKeyId = v;
	 }else{
		varKeyId = str;
	}
		
	if(varKeyId==""){
		alert("$text.get('please.select.draft')") ;
		return ;
	}
	window.parent.exeQuoteDraft(varKeyId,"$!moduleType");
	window.parent.jQuery.close('QuoteDraft');
}

function beforeQuery(){
	#if($conditions.size()==0)
		document.form.queryChannel.value="";
	#else
		document.form.queryChannel.value="normal";
	#end
	document.form.submit() ;
}
function setIniFocus()
{
	 var keyIds=document.getElementsByName("keyId")
	 if(typeof(keyIds[0])=="undefined")return ;
	 keyIds[0].checked = true ;
	 
	 var varHref = $("table a") ;
	 for(var i=0;i<varHref.length;i++){
	 	varHref[i].removeAttribute("href","") ;
	 }
}


function detail(str){
	beforeSubmit(str.split(",")[0]);
}
</script>

<style type="text/css">
.listRange_1 li{white-space:normal;}
.wp_ul li{margin:2px 0 0 0;}
.wp_ul_6 li{margin:2px 20px 0  0;}
.listrange_pagebar button{height:13px\9;}
</style>
<script type="text/javascript">
$(function(){
	cyh.lableAlign();
});
</script>
</head>

<body onLoad="showStatus();setIniFocus();">

<form  method="post" scope="request"  name="form" action="/UserFunctionQueryAction.do" onKeyDown="down()">
 <input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
 <input type="hidden" name="tableName" value="$!tableName">
 <input type="hidden" name="optionType" value="" > 
 <input type="hidden" name="parentCode" value="$!parentCode"> 
 <input type="hidden" name="f_brother" value="$!f_brother">
 <input type="hidden" name="defineName" value="">
 <input type="hidden" name="winCurIndex" value="$winCurIndex">
 <input type="hidden" name="parentTableName" value="$!parentTableName">
 <input type="hidden" name="reportName" value="$!reportName"/>
 <input type="hidden" name="isAllListQuery" value="$!isAllListQuery"/>
 <input type="hidden" name="parentCodeList" value=""/>
 <input type="hidden" name="src" value="">
 
 <input type="hidden" name="queryChannel" value="$!queryChannel" />
 <input type="hidden" name="newCreateBy" value=""/>
 <input type="hidden" name="wakeUp" value=""/>
 <input type="hidden" name="sendMessage" value=""/>
 <input type="hidden" name="classCode" value=""/>
 <input type="hidden" name="SQLSave" value="$!SQLSave">
 <input type="hidden" name="right" value="$!SQLSave">
 <input type="hidden" name="draftQuery" value="$!draftQuery">
<div class="Heading">
	<div class="HeadingTitle" style="padding:0;font-size:13px;">#if("$!moduleName"=="")$!reportName#else$!moduleName#end $text.get("common.draft.list")</div>
	<ul class="HeadingButton">
		<li><span class="hBtns" onclick="beforeQuery();">$text.get("common.lb.query")</span></li>
		<li><span class="hBtns" onClick="javascript:beforeSubmit();">$text.get("common.lb.ok")</span></li>
		<li><span class="hBtns" onClick="javascript:window.parent.jQuery.close('QuoteDraft');">$text.get("com.lb.close")</span></li>
	</ul>
</div>
<div class="listRange_frame">
	<div class="listRange_1" id="listid">
		<ul class="draft-ul">
		#set($totalFields=0)
		#set($queryType = $!conditinSessionMap.get("queryType"))	
		#if("Advance" != "$queryType")
			#foreach($row in $conditions)
				#set($totalFields=$totalFields+1)
				#if($!globals.get($row,3) == "0")
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
					<div class="swa_c2" ><input name="$!globals.get($row,1)" value="$!globals.get($row,2)" onKeyDown="if(event.keyCode==13) event.keyCode=9" ></div>
				</li>
				#elseif($!globals.get($row,3) == "1")			
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
					<div class="swa_c2" >
						<select name="$!globals.get($row,1)" >
						<option value="" ></option>
						#foreach($erow in $globals.getEnumerationItems($!globals.get($row,4)))
							<option title="$erow.name" value="$erow.value" #if("$erow.value" == "$!globals.get($row,2)") selected #end>$erow.name</option>
						#end
						</select>
					</div>
				</li>
				#elseif($!globals.get($row,3) == "2")
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
					<div class="swa_c2" ><input name="$!globals.get($row,1)" date="true" value="$!globals.get($row,2)" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  ></div>
				</li>			
				#elseif($!globals.get($row,3) == "3")			
				<input type="hidden" name="$!globals.get($row,1)" value="$!globals.get($row,2)">
				#elseif($!globals.get($row,3) == "4")			
				<li>
					<div class="swa_c1"><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
					<div class="swa_c2" >
						<input name="$!globals.get($row,1)" value="$!globals.get($row,2)" onKeyDown="if(event.keyCode==13) openSelect('$!globals.get($row,0)','$!globals.get($row,6)','$!globals.get($row,1)');" onDblClick="openSelect('$!globals.get($row,0)','$!globals.get($row,6)','$!globals.get($row,1)');"  />
						<b class="stBtn icon16" onClick="openSelect('$!globals.get($row,0)','$!globals.get($row,6)','$!globals.get($row,1)');"></b>
					</div>
				</li>			
				#end
			#end
		#end
		#if("$!lngIfClass"=="1" && "$!conditions.size()"!="0")
			<li>
				<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("com.report.displayType")</div></div></div>
				<div class="swa_c2" >
					<select name="selectType">
						<option value=""></option>
						<option  title="$text.get("com.report.classDisplay")" value="normal" #if("$!selectType"=="normal")selected #end>$text.get("com.report.classDisplay")</option>
						<option title="$text.get("com.report.endDisplay")" value="endClass" #if("$!selectType"=="endClass")selected #end>$text.get("com.report.endDisplay")</option>
					</select>
				</div>
			</li>
		#end
		</ul>
	</div>
	<div style="width:100%;overflow:hidden;">
		<div class="listRange_Pop-up_scroll" id="conter">
			<div style="width:100%;height:100%;overflow:auto;position:relative">
			$!tableList
			</div>
		</div>	
	</div>
	<div class="page-wp">	
		<div class="listRange_pagebar" style="position:relative"> $!pageBar </div>
	</div>	
</div>
<script language="javascript">
var condHeight= 80;
$(function(){
	
	$("#k_data",document).height($("#conter",document).height()-10);
	$("#k_column",document).height($("#k_data",document)[0].clientHeight);
	$("#k_head",document).width($("#k_data",document)[0].clientWidth);
	jQuery("#k_column",document).width(jQuery("#k_column>table",document).outerWidth(true));
	jQuery("#kt_head",document).width(jQuery("#k_column>table",document).outerWidth(true));
	
	jQuery("#k_data tbody tr",document).click(function(){jQuery("#k_column tbody",document).find("tr:eq("+jQuery(this).index()+")").toggleClass("highlightrow");jQuery(this).toggleClass("highlightrow");});
	jQuery("#k_column tbody tr",document).click(function(){jQuery("#k_data tbody",document).find("tr:eq("+jQuery(this).index()+")").toggleClass("highlightrow");jQuery(this).toggleClass("highlightrow");});
	
	drag22();
	popMenu();
	$("#k_data",document).scroll(function () { 
		$("#k_head",document).scrollLeft($("#k_data",document).scrollLeft()); 
		$("#k_column",document).scrollTop($("#k_data",document).scrollTop()); 
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
	//$("body").addClass("html");
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

           
</script>		
<script type="text/javascript">


var rowCount = document.getElementsByName("keyId").length ;
if("$!pageSize"!=rowCount){
	var varPage = document.getElementById("nextPageSize") ;
	if(typeof(varPage)!="undefined" && varPage!=null){
		varPage.removeAttribute("href","") ;
	}
}	
</script>

</form>
</body>
</html>
