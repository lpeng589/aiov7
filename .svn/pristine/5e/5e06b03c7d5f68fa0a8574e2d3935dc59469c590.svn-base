<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>$text.get("oa.workflow.title.tempFileSet")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<style type="text/css">
<!--
.topSide {
	border-top-width: 1px;
	border-right-width: 1px;
	border-bottom-width: 1px; 
	border-left-width: 1px;
	border-top-style: solid;
	border-top-color: #3366CC;
	border-right-color: #3366CC;
	border-bottom-color: #3366CC;
	border-left-color: #3366CC;
}
.leftBorder {
	border-top-width: 1px;
	border-right-width: 1px;
	border-bottom-width: 1px;
	border-left-width: 1px;
	border-left-style: dashed;
	border-left-color: #0066FF;
}
.downSide {
	border-top-width: 1px;
	border-right-width: 1px;
	border-bottom-width: 1px;
	border-left-width: 1px;
	border-bottom-style: dashed;
	border-bottom-color: #0066FF;
}
.bu{
	border:0;
	border:1px solid #A0A0A0;
	text-align:center;
	margin-right:5px;
	height:20px;
	cursor:pointer;
	width:0;
	width:auto;
	padding:2px 3px 0 3px;
	overflow:visible;
	background:url(/style/images/aiobg.gif) repeat-x left top;
}
.STYLE1 {color: #FF0000}
-->
</style>
<!-- --------------------=== 调用Weboffice初始化方法 ===--------------------- -->
<SCRIPT LANGUAGE=javascript FOR=WebOffice1 EVENT=NotifyCtrlReady>
	newDoc();
<!--
 //WebOffice1_NotifyCtrlReady()			// 在装载完Weboffice(执行<object>...</object>)控件后执行 "WebOffice1_NotifyCtrlReady"方法
//-->
</SCRIPT>
<SCRIPT LANGUAGE=javascript>
<!--
// ---------------------=== 控件初始化WebOffice方法 ===---------------------- //
function WebOffice1_NotifyCtrlReady() {
		document.all.WebOffice1.LoadOriginalFile("", "doc");
}
//$templateFile ddd
// ---------------------=== 新建文档 ===---------------------- //
function newDoc() {		
		#set($typ = $type)
		#if("$typ" == "3")
		#set($doctype = "xls")
		#elseif("$typ" == "4")
		#set($doctype = "wps")
		#else
		#set($doctype = "doc")
		#end	
		
		#set($tf="")
		#if("$!templateFile"!="")
		#set($tf="http://"+$request.getHeader("Host")+"/ReadFile?fileName="+$globals.urlEncode($!templateFile)+"&tempFile=workflowtemplete")
		
		#end
		document.all.WebOffice1.LoadOriginalFile("$tf", "$doctype");
		//document.all.WebOffice1.ShowToolBar(0);
		
		
}

// ---------------------== 关闭页面时调用此函数，关闭文件 ==---------------------- //
function window_onunload() {
	document.all.WebOffice1.Close();
}
function window_onload(){
	//alert(screen.availHeight) ;
	document.all.WebOffice1.height=screen.availHeight-60;
}

// -----------------------------== 返回首页 ==------------------------------------ //
function fileclose() {
	try{
		window.close();
		document.all.WebOffice1.Close();
		//window.location.href  = "index.jsp"
	}catch(e){}
	
}
function filesave(){
	try{
		
		#set($typ = $type)
		#if("$typ" == "3")
		#set($doctype = "xls")
		#elseif("$typ" == "4")
		#set($doctype = "wps")
		#else
		#set($doctype = "doc")
		#end
		
		#if("$!templateFile"!="")
			filename = "$!templateFile";
		#else
			filename = new Date().getTime()+".$doctype" ;
		#end
		
		document.all.WebOffice1.HttpInit();	
		//document.all.WebOffice1.HttpAddPostString(String FieldName, String someValue);
		//subfile = filename.substr(0,filename.indexOf(".")); 
		document.all.WebOffice1.HttpAddPostCurrFile("file", filename);	
		returnValue = document.all.WebOffice1.HttpPost("http://$request.getHeader("Host")/OAWorkFlowTempAction.do?operation=$globals.getOP("OP_ADD")&type=fileupload");	
		//alert(returnValue);
		if("OK" == returnValue){ 
			window.returnValue   =   filename;    
			window.close();  
		} else  {
			alert("$text.get("oa.workflow.msg.fileFailture")")
		}
				
	}catch(e){}
}

  
//-->
</SCRIPT>
</head>

<body onUnload="window_onunload();"  onload="window_onload();" >
<form name="form">
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td height="35">
<div class="Heading" style="border-bottom:1px solid #CCCCCC;">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.workflow.lb.tempFile")</div>
	<ul class="HeadingButton" >	  
		  <li>
		  <input type="button" class="bu" name="save" value="$text.get("common.lb.save")" onclick="filesave()">
		  <input type="button"  class="bu" name="close" value="$text.get("common.lb.close")" onclick="fileclose()"> 
 </li>
		</ul>
</div>
</td></tr>
  <tr>
  <td valign="top" class="leftBorder">

  <script  language="javascript" src="/js/LoadWebOffice.js"></script>
   </td>
  </tr>
</table>
<br>
<br>
</form>
</body>
</html>
