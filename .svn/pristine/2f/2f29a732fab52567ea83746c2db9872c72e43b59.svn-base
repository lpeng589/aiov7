<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/formvalidate.vjs"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script src="/js/jquery.js" type="text/javascript"></script>  
<script language="javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="content"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});


function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function isexits(obj,array)
{
			var fs=array.split(";");
			for(i=0;i<fs.length;i++)
			{
			if((fs[i]+";")==(obj))
			{
			return false;
			}
			}
			return true;
			
}

putValidateItem("title","$text.get("oa.subjects")","any","",false,0,100);
putValidateItem("leaveDate",'$text.get("common.msg.sameEmail")',"int","",true,0,2000 );
putValidateItem("ccMail",'$text.get("oa.mail.cc")',"any","",true,0,1000 );
putValidateItem("mailaccount","$text.get("oa.common.mail.account")","any","",false,0,1000 );

function beforSubmit(form){   
	if(!validate(form))return false;
	
	
	return true;
}	

function send(saveType)
{
	if(!validate(form))return false;
	
	//alert(form.mailaccount.value);
	//return false;
	form.content.value = editor.html();
	
	document.form.submit();
}
  
function showStatus(obj){
    document.getElementById("status").style.visibility="visible";
}

function isNull(variable,message)
{
	if(variable=="" || variable==null)
	{
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else
	{
		return true;
	}
			
}

function doBack(){	
	document.form.action='/AIOBOL88EMailAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&type=keyword&winCurIndex=$winCurIndex';
	document.form.target="_self";
	document.form.submit();
}

function stop(){
	document.form.status.value="0";
	document.form.submit();
}
function doRefresh(){
	window.location.href="/AIOBOL88EMailAction.do?operation=6&type=mailWrite&from=$from";
}

</script>

</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form name="form" method="post"  action="/AIOBOL88EMailAction.do?type=keyword" enctype="multipart/form-data" target="formFrame">
	<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")">
	<input type="hidden" name="winCurIndex" value="$winCurIndex">
	<input type="hidden" name="noback" value="$!noback"> 
	<input type="hidden" name="mailkeyword" value="$!MailPoolForm.mailkeyword">
	<input type="hidden" name="curKeywordName" value="$!MailPoolForm.curKeywordName">
	<input type="hidden" name="curKwMailNum" value="$!MailPoolForm.curKwMailNum">
	<input type="hidden" name="curKeywordId" value="$!MailPoolForm.curKeywordId">
	<input type="hidden" name="query" value="true">
	<input type="hidden" name="attachFiles" value="$!MailPoolForm.attach">
	<input type="hidden" name="status" value="1">
	<input type="hidden" name="delFiles" value="">
	<input type="hidden" name="from" value="$from">
<div class="oamainhead">
<div class="HeadingButton" style="float:left; display:inline;width: 100%;">
<div style="float:left; color:#666; padding-top:5px;"><img src="/$globals.getStylePath()/images/ico.gif" style=" vertical-align:middle">&nbsp;$text.get("oa.mail.importContent") 
</div>
<ul style="float:right;margin:2px 0 0 5px;padding:0;"> 
#if("$!MailPoolForm" == "" || "$!MailPoolForm.status" =="0") 
<li><button type="button" onClick="send('save')"  name="Submit" class="b4">$text.get("oa.mail.lb.startup")</button></li>	
#else
<li><button type="button" onClick="send('save')"  name="Submit" class="b4">$text.get("bol88.mailInfo.AgainConfigure")</button></li>
<li><button type="button" onClick="stop()"  name="Submit" class="b4">$text.get("bol88.mailInfo.StopGroupware")</button></li>	
#end
<li><button type="button" onClick="doRefresh()" class="b2">$text.get("common.lb.refresh")</button></li>		
#if("$from" == "CRM")
#else  		
<li><button type="button" onClick="doBack()" class="b2">$text.get("common.lb.back")</button></li>
#end
</ul>
	</div>	
	</div>	
    <div id="oalistRange">
<script type="text/javascript">
var oDiv=document.getElementById("oalistRange");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="4" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>			
			<tr>
				<td class="oabbs_tr" width="80">&nbsp;</td>
				<td colspan="3">
				 <div style="color:red;font-size:18px">	#if("$from"=="CRM")CRM$text.get("bol88.mailWrite.batchSend") #else BOL88$text.get("bol88.mailWrite.batchSend") #end</div>
					
			<div >
					$text.get("bol88.mailInfo.currentlyState")： #if("$!MailPoolForm" == "" || "$!MailPoolForm.status" =="0") $text.get("bol88.mailInfo.NoStartup") #else $text.get("bol88.mailInfo.BeExecute") #end  #if("$!MailPoolForm" !="")		#end
					&nbsp;&nbsp;&nbsp;$MailPoolInfo								
			</div>
			
					#if("$!MailPoolForm" !="")	
			<div >
					$text.get("bol88.mailInfo.SumGroupware") $!MailPoolForm.totalSend  $text.get("oa.mail.mailAddress") <br/>
					$text.get("bol88.mailInfo.ThisGroupware") $!curCount  $text.get("oa.mail.mailAddress") <br/>
			</div>	
					#end	
				</td>
			</tr>
			#if("$from"!="CRM")
			<tr>
				<td class="oabbs_tr" width="80">$text.get("bol88.mailWrite.choosedKeyword"):</td>
				<td colspan="3">
					#if($!MailPoolForm.curKeywordName != "") ($!MailPoolForm.curKeywordName) #end &nbsp;</td>
			</tr>
			#end
			<tr>
				<td class="oabbs_tr" valign="top">$text.get("bol88.mailWrite.groupwareDate"):</td>
			 	<td colspan="3">
			 	$text.get("alertCenter.lb.startDate")：<input name="startSendDate" type="text" class="text" size="10" value="$!MailPoolForm.startSendDate" onClick="openInputDate(this)">$text.get("oa.bol88.msg.mailWrite.blankStart")
				&nbsp;&nbsp;
				$text.get("alertCenter.lb.endDate")：<input name="endSendDate" type="text" class="text" size="10" value="$!MailPoolForm.endSendDate" onClick="openInputDate(this)">$text.get("oa.bol88.msg.mailWrite.blankEnd"),
				&nbsp;&nbsp; &nbsp;&nbsp; 
					
					<input name="leaveDate" type="text" class="text" size="2" value="$!MailPoolForm.leaveDate">$text.get("oa.bol88.msg.mailWrite.SameEmailNoRepeat")

					&nbsp;&nbsp; 
				</td>
			</tr>		
			<tr>
				
				<td class="oabbs_tr" >$text.get("bol88.mailWrite.groupwareTime"):</td>			
				<td colspan="3">$text.get("common.msg.everyDay")
				<select name="startTime">						
					<option value="0" #if("$!MailPoolForm.startTime" == "0") selected #end>0:00</option>
					<option value="1" #if("$!MailPoolForm.startTime" == "1") selected #end>1:00</option>
					<option value="2" #if("$!MailPoolForm.startTime" == "2") selected #end>2:00</option>
					<option value="3" #if("$!MailPoolForm.startTime" == "3") selected #end>3:00</option>
					<option value="4" #if("$!MailPoolForm.startTime" == "4") selected #end>4:00</option>
					<option value="5" #if("$!MailPoolForm.startTime" == "5") selected #end>5:00</option>
					<option value="6" #if("$!MailPoolForm.startTime" == "6") selected #end>6:00</option>
					<option value="7" #if("$!MailPoolForm.startTime" == "7") selected #end>7:00</option>
					<option value="8" #if("$!MailPoolForm.startTime" == "8") selected #end>8:00</option>
					<option value="9" #if("$!MailPoolForm.startTime" == "9") selected #end>9:00</option>
					<option value="10" #if("$!MailPoolForm.startTime" == "10") selected #end>10:00</option>
					<option value="11" #if("$!MailPoolForm.startTime" == "11") selected #end>11:00</option>
					<option value="12" #if("$!MailPoolForm.startTime" == "12") selected #end>12:00</option>
					<option value="13" #if("$!MailPoolForm.startTime" == "13") selected #end>13:00</option>
					<option value="14" #if("$!MailPoolForm.startTime" == "14") selected #end>14:00</option>
					<option value="15" #if("$!MailPoolForm.startTime" == "15") selected #end>15:00</option>
					<option value="16" #if("$!MailPoolForm.startTime" == "16") selected #end>16:00</option>
					<option value="17" #if("$!MailPoolForm.startTime" == "17") selected #end>17:00</option>
					<option value="18" #if("$!MailPoolForm.startTime" == "18") selected #end>18:00</option>
					<option value="19" #if("$!MailPoolForm.startTime" == "19") selected #end>19:00</option>
					<option value="20" #if("$!MailPoolForm.startTime" == "20") selected #end>20:00</option>
					<option value="21" #if("$!MailPoolForm.startTime" == "21") selected #end>21:00</option>
					<option value="22" #if("$!MailPoolForm.startTime" == "22") selected #end>22:00</option>
					<option value="23" #if("$!MailPoolForm.startTime" == "23") selected #end>23:00</option>
				</select>
				$text.get("common.msg.until")

				<select name="endTime" >						
					<option value="0" #if("$!MailPoolForm.endTime" == "0") selected #end>0:00</option>
					<option value="1" #if("$!MailPoolForm.endTime" == "1") selected #end>1:00</option>
					<option value="2" #if("$!MailPoolForm.endTime" == "2") selected #end>2:00</option>
					<option value="3" #if("$!MailPoolForm.endTime" == "3") selected #end>3:00</option>
					<option value="4" #if("$!MailPoolForm.endTime" == "4") selected #end>4:00</option>
					<option value="5" #if("$!MailPoolForm.endTime" == "5") selected #end>5:00</option>
					<option value="6" #if("$!MailPoolForm.endTime" == "6") selected #end>6:00</option>
					<option value="7" #if("$!MailPoolForm.endTime" == "7") selected #end>7:00</option>
					<option value="8" #if("$!MailPoolForm.endTime" == "8") selected #end>8:00</option>
					<option value="9" #if("$!MailPoolForm.endTime" == "9") selected #end>9:00</option>
					<option value="10" #if("$!MailPoolForm.endTime" == "10") selected #end>10:00</option>
					<option value="11" #if("$!MailPoolForm.endTime" == "11") selected #end>11:00</option>
					<option value="12" #if("$!MailPoolForm.endTime" == "12") selected #end>12:00</option>
					<option value="13" #if("$!MailPoolForm.endTime" == "13") selected #end>13:00</option>
					<option value="14" #if("$!MailPoolForm.endTime" == "14") selected #end>14:00</option>
					<option value="15" #if("$!MailPoolForm.endTime" == "15") selected #end>15:00</option>
					<option value="16" #if("$!MailPoolForm.endTime" == "16") selected #end>16:00</option>
					<option value="17" #if("$!MailPoolForm.endTime" == "17") selected #end>17:00</option>
					<option value="18" #if("$!MailPoolForm.endTime" == "18") selected #end>18:00</option>
					<option value="19" #if("$!MailPoolForm.endTime" == "19") selected #end>19:00</option>
					<option value="20" #if("$!MailPoolForm.endTime" == "20") selected #end>20:00</option>
					<option value="21" #if("$!MailPoolForm.endTime" == "21") selected #end>21:00</option>
					<option value="22" #if("$!MailPoolForm.endTime" == "22") selected #end>22:00</option>
					<option value="23" #if("$!MailPoolForm.endTime" == "23") selected #end>23:00</option>
				</select> 
	  			 </td>
			</tr>	
			
	       <tr>
			<tr>
				<td class="oabbs_tr" valign="top">$text.get("oa.bol88.msg.mailWrite.accelerate"):</td>
			 	<td colspan="3">
			 	<select name="threadNum" >	
			 	<option value="-1" #if("-1"=="$!MailPoolForm.threadNum") selected #end>$text.get("bol88.mailWrite.safeSpeed")</option>	
			 	#foreach( $i in[1..100])
			 	<option value="$i" #if("$i"=="$!MailPoolForm.threadNum") selected #end>$i$text.get("bol88.mailWrite.times")</option>
			 	#end
			 	</select>
				<span style="color:red">$text.get("oa.bol88.msg.mailWrite.annotate")<br/>
					  </span>
				</td>
			</tr>	
	       <tr>
				
				<td class="oabbs_tr" >$text.get("oa.subjects"):</td>
			
				<td class="oabbs_tl" style="width:320px;">
				<input name="title" type="text" class="text" style="width:300px;"  value="$!MailPoolForm.title"><font color="#FF0000">*</font></td>
				<td class="oabbs_tr" style="width:70px;">$text.get("oa.common.mail.account"):</td>
				<td width="600">
					<select name="mailaccount" >
					#foreach($row in $MailinfoSetting)
						<option value="$!globals.get($row,0)" 
					#if("$!MailPoolForm.mailaccount" == "$!globals.get($row,0)") selected="selected"	 #end>
					$!globals.get($row,1)
					</option>
					#end 
					</select>					
  			 	</td>
			</tr>	
			<tr>
				<td class="oabbs_tr" valign="top">$text.get("bol88.mailWrite.copySend"):</td>
			 	<td colspan="3">
				<input name="ccMail" type="text" class="text" style="width:300px;" value="$!MailPoolForm.ccMail"><br/>
				<span>$text.get("bol88.mailWrite.msg.annotate1")<br/>
					  $text.get("bol88.mailWrite.msg.annotate2")<br/>
						  $text.get("bol88.mailWrite.msg.annotate3")</span>
				</td>
			</tr>	
			<tr>
				<td class="oabbs_tr" valign="top">$text.get("oa.mail.contenet"):</td>
			 	<td colspan="3">
				<textarea name="content" style="width:100%;height:300px;visibility:hidden;">$!MailPoolForm.content</textarea><font color="#FF0000">* </font></td>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("upload.lb.affix"):</td>
				<td colspan="3"> 
					
					<span id="files">
						<button type=button class="b2" onClick="openAttachUpload('/email/bol88/')">$text.get("oa.common.accessories")</button>
					</span>
					<div id ="status" style="visibility:hidden;color:Red"></div>
					<div id="files_preview">
					#if($!MailPoolForm.attach.indexOf(";") > 0)
					  #foreach ($str in $globals.strSplit($!MailPoolForm.attach,';'))
					  #if($str.length()>0)
					  	 <div  id ="$str" style ="height:18px; color:#ff0000;">
					  	 <a href="javascript:;" onClick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
					  	 $str<input type=hidden name="attachFile" value="$str"/></div>					 
					  #end
				 	  #end
				 	#end
					</div>				</td>
			</tr>			

		  	</tbody>
		</table>	
</div>

</form>
</body>
</html>
