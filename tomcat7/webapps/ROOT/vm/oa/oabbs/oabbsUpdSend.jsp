<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>$text.get("oa.bbs.bbsSend")</title>
#end
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
 <script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="topicContent"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

function changeFileName(obj){
	var filePath = obj.value;
	var starIndex = filePath.lastIndexOf("\\");
	var endIndex = filePath.lastIndexOf(".");
	var fileName = filePath.substr(starIndex+1,(endIndex-starIndex-1));
	document.getElementById("fileName").value = fileName;
}

function showStatus(obj)  {
    document.getElementById("status").style.visibility="visible";
}

function send(){
  	window.save = true;
  	if(!validate(form))return false;
	disableForm(form);
	return true;
}
  
function forback(){
	window.location.href="/OABBSForumAction.do?operation=70&forumId=$!forum.id&topicId=$!forum.topic.id&pageNo=$!pageNo&searchSel=$!searchSel&addTHhead=$!addTHhead" ;
}

function rt(){	
	editor.html();
}

</script>
</head>
<body>
#if("$!addTHhead" == "true")
	#parse("./././body2head.jsp")
#end
<form name="form" method="post" action="/OABBSForumAction.do?searchSel=$!searchSel&addTHhead=$!addTHhead" onSubmit="return send();">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
<input type="hidden" name="forumId" value="$!forum.id">
<input type="hidden" name="topicId" value="$!forum.sortId">
<input type="hidden" name="attachFiles" value="$forum.attachment">
<input type="hidden" name="delFiles" value="">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
		<div class="HeadingTitle">$text.get("mrp.lb.update")$text.get("oa.subjects")</div>
		<div class="HeadingButton" style="height:25px;">
		   <button type="submit" name="Submit" class="b2">$text.get("mrp.lb.save")</button>
		 <!-- <button type="reset" class="b2">$!text.get("common.lb.reset")</button> 重置-->
		  <button type="button" onClick="forback()" class="b2">$text.get("common.lb.back")</button>
		</div>
</div>
<div id="listRange_id">
<script type="text/javascript">
var oDiv=document.getElementById("listRange_id");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oabbs_function_index" name="table">
		<thead>
			<tr>
				<td width="160">&nbsp;</td>
				<td colspan="2" align="center">$text.get("mrp.lb.update")$text.get("oa.subjects")</td>
			</tr>
		</thead>
		<tbody>
			<tr>
			 	<!--   <td rowspan="4" valign="top" class="oabbs_lefttdbg">
		<div style="padding:10px 0; text-align:center">
		<table cellspacing="0" cellpadding="0" style="border:0">
			<tr>
				<td style="overflow:hidden; border:0; text-align:center; width:30px;">
				<!-- <img src="/$globals.getStylePath()/images/oabbs/noavatar_middle.gif" border="0" />
				</td>
			</tr>
		</table>
		</div>		
			 </td> 
			  -->
				<td align="right" valign="middle" >
				$text.get("message.lb.title")：				</td>
			    <td width="*" valign="middle"><input name="topicName" type="text" size="60" value="$!forum.topicName" class="text"><font color="#FF0000">* </font>
			    #if($topicType.size()>0)
			    $text.get("oa.workflow.type"):<select name="topicTyle">
					#foreach($row in $topicType)
					<option value="$globals.get($row,1)" #if("$globals.get($row,1)"=="$!forum.topicTyle") selected #end>$globals.get($row,1)</option>
					#end
					</select>
			    #end
			    </td>
			</tr>
			<tr>
			  <td align="right" valign="middle">$text.get("oa.common1.content")：</td>
			  <td valign="middle"><textarea name="topicContent" style="width:800px;height:300px;visibility:hidden;">$!forum.topicContent</textarea>
			  <font color="#FF0000">* </font></td>
		  </tr>
			<tr>
			  <td align="right" valign="middle">$text.get("upload.lb.affixupload")：</td>
			  <td valign="middle">
			  <span id="files">
			  		<button type=button class="b2" onClick="openAttachUpload('/bbs/')">$text.get("oa.common.accessories")</button>
			  </span> 
			<div id ="status" style="visibility:hidden;color:Red"></div>
			<div id="files_preview">
 		    #if($!forum.attachment.indexOf("|") > 0)
				  #foreach ($str in $globals.strSplit($!forum.attachment,'|'))
				  	 <div  id ="$str" style ="height:18px; color:#ff0000;">
				  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
				  	 $str<input type=hidden name="attachFile" value="$str"/></div>					 
				  #end	
			 #else	  		  
			  	#foreach ($str in $globals.strSplit($!forum.attachment,';'))
					 <div  id ="$str" style ="height:18px; color:#ff0000;">
				  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
				  	 $str<input type=hidden name="attachFile" value="$str"/></div>	
				#end
			  #end
		    
		    
		    </div></td>
		  </tr>
		  <!-- 
		    <tr>
			<td class="oabbs_tr">$text.get("oa.readingMark.save")：</td>
			<td>
			#set ($isSaveReading=$!globals.get($arr_send,10))
			#if($isSaveReading=="1")
				#set ($c1="checked")
			#else
				#set ($c2="checked")
			#end	
			<input name="isSaveReading" type="radio" value="1" $!c1>
			  $text.get("oa.common.yes")
			<input name="isSaveReading" type="radio" value="0" $!c2>
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
		<div style="height:40px;"></div>
</div>
</form>
</body>
</html>
