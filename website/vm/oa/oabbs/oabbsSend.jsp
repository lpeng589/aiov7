<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>$text.get("oa.bbs.indexPage")</title>
#end
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript">

var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="topicContent"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

putValidateItem("topicName","$text.get('oa.bbs.bbsSendTile')","any","",false,0,600 );

function showStatus(obj)  {
    document.getElementById("status").style.visibility="visible";
}
  
function send(){	
	window.save = true;
  	if(!validate(form)||!subPrepream())return false;
	disableForm(form);
	return true;
}
  
function subPrepream(){
	var content = editor.html();
	if(content.length==0){
  		alert("$text.get('oa.common.content')"+"$text.get('common.validate.error.null')") ;
  		if(typeof(top.junblockUI)!="undefined"){
			top.junblockUI();
		}
  		return false ;
  	}
	return true ;
}
  
function rt(){	
	editor.html() ;
}

function forback(){
	//if(confirm("$text.get('oa.common.edit.content')")==true){
		window.location.href="/OABBSForumQueryAction.do?classCode=$!classCode&topicId=$!topicId&addTHhead=$!addTHhead" ;
	//}
}



</script>

</head>

<body>
#if("$!addTHhead" == "true")
	#parse("./././body2head.jsp")
#end
<form name="form" method="post" action="/OABBSForumAction.do?&addTHhead=$!addTHhead" onSubmit="return send();">
<input type="hidden" name="operation" value="$!globals.getOP("OP_ADD")"/>
<input type="hidden" name="userId" value="$!BBSUser.id">
<input type="hidden" name="attachFiles" value="">
<input type="hidden" name="delFiles" value="">
<input type="hidden" name="alertStatus" value="-1">
<div class="oamainhead">
<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
		<div class="HeadingTitle">$text.get("oa.bbs.bbsSendTile")</div>
<div class="HeadingButton" style="height:25px;">
<button  type="submit" name="submit" >$text.get("oa.bbs.bbsPublish")</button>
<button  type="reset" onClick="rt()">$!text.get("common.lb.reset")</button>
<button  type=button  onClick="forback()">$!text.get("common.lb.back")</button>
</div>
</div>
<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
 <div class="oabbs_scroll">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oabbs_function_index" name="table">
		<thead>
			<tr>
				
				<td colspan="3" align="left">$text.get("common.msg.currstation")： $!parentName &gt;&gt; $text.get("oa.bbs.bbsSendTile")</td>
		    </tr>
		</thead>
		<tbody>
				 <tr>
			<!-- <td rowspan="8" valign="top" class="oabbs_lefttdbg">
		<div style="padding:10px 0; text-align:center">
		<table cellspacing="0" cellpadding="0" style="border:0"> 
			<tr>
				<td style="overflow:hidden; border:0; text-align:center; width:30px;" ></td>
			</tr>
		</table>
			</div>		
				 </td>--> 
				<td align="right" valign="middle" style="width:100px;">
				<input type="hidden" name="topicId" value="$topicId"  />
				$text.get("message.lb.title")：</td>
			    <td width="*" valign="top"><input name="topicName" id="titleId" type="text" size="130" class="text"><font color="#FF0000">* </font>
			    </td>
			</tr>
			#if($topicType.size()>0)
			<tr>
				<td align="right" valign="middle" style="width:100px;">$text.get("oa.workflow.type")：</td>
				<td width="*" valign="top">
			    <select name="topicTyle">
					#foreach($row in $topicType)
					<option value="$globals.get($row,1)">$globals.get($row,1)</option>
					#end
				</select>
			    </td>
			</tr>
			 #end
			<tr>
			  <td align="right" valign="middle" style="width:100px;">$text.get("oa.common1.content")：</td>
			  <td valign="top"><textarea name="topicContent" style="width:100%;height:300px;visibility:hidden;"></textarea>
			    <font color="#FF0000">* </font></td>
		  </tr>
		  #if($uploadScope || "$LoginBean.id"=="1" || "$!BBSTopic.bbsUserId"=="$!BBSUser.userID" || $BBSTopic.bbsUserId2==$BBSUser.userID)
			<tr>
			  <td align="right" valign="middle" style="width:100px;">$text.get("upload.lb.affix")：</td>
			  <td valign="top"><span id="files">
			  	<button type=button class="b2" onClick="openAttachUpload('/bbs/')">$text.get("oa.common.accessories")</button>
					</span>
					<div id ="status" style="visibility:hidden;color:Red"></div>
				<div id="files_preview"></div>
				<br>					</td>
		  </tr>
		  #end
		  <!-- 
			<tr>
			  <td align="right" valign="middle">$text.get("oa.calendar.wakeupType")：</td>
			  <td valign="top">#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
			 			<input type="checkbox" name="wakeUpMode" value="$row_wakeUpType.value"/>$row_wakeUpType.name
		   			#end</td>
		  </tr>
		  
		  <tr>
			<td class="oabbs_tr">$text.get("oa.readingMark.save")：</td>
			<td>
			 <input name="isSaveReading" type="radio" value="1" >
			  $text.get("oa.common.yes")
			 <input name="isSaveReading" type="radio" checked value="0" >
			  $text.get("oa.common.no")		
			</td>
			</tr>
		 -->
		 	<tr>
			  <td height="40"  align="center" >&nbsp;&nbsp;</td>
			  <td>
			  <input type="checkbox" name="replayNote" value="1">$text.get("bbs.lb.replayNote")</td>
		   </tr>
		  </tbody>
		</table>	
</div>
</div>
</form>
</body>
</html>
