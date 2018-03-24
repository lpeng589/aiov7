<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("workflow.msg.transactionWarmKind")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<script language="javascript" src="/js/function.js"></script>
<script>
function submitModenew(){
	var modeBeans=document.getElementsByName("preMode");
	var modes="";
	for(var i=0;i<modeBeans.length;i++){
		if(modeBeans[i].checked){
			modes+=modeBeans[i].value+",";
		}
	}
	if(form.content.value.length==0){
		alert("$text.get('common.msg.inputCenter')");
		form.content.focus();
		return false;
	}
	
	if(modes.length>0){
		document.getElementById("wakeType").value=modes;
	}else{
		alert("$text.get('workflow.msg.choiceWarmkind')");
		return false;
	}
	form.submit();
}

</script>
</head>

<body style="height:98%;">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" action="/UserFunctionAction.do?operation=$globals.getOP("OP_QUERY")&optionType=hurryTrans" target="formFrame">
  <input type="hidden" id="operation" name="operation" value="$globals.getOP('OP_UPDATE')"/>
  <input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value=""/>
  <input type="hidden" id="wakeType" name="wakeType" value=""/>
  <input type="hidden" id="createBy" name="createBy" value="$!request.getParameter("createBy")" />
  <input type="hidden" id="applyType" name="applyType" value="$!request.getParameter("applyType")" />
  <input type="hidden" id="checkPerios" name="checkPerios" value="$!request.getParameter("checkPerios")" />
  <input type="hidden" id="approveStatus" name="approveStatus" value="$!request.getParameter("approveStatus")" />
  <input type="hidden" id="nextNodeIds" name="nextNodeIds" value="$!request.getParameter("nextNodeIds")" />
  <input type="hidden" id="currNodeId" name="currNodeId" value="$!request.getParameter("currNodeId")" />
  <input type="hidden" id="keyId" name="keyId" value="$!request.getParameter("keyId")"/>
  <input type="hidden" id="oaTimeLimit" name="oaTimeLimit" value="$!request.getParameter("oaTimeLimit")" />
  <input type="hidden" id="oaTimeLimitUnit" name="oaTimeLimitUnit" value="$!request.getParameter("oaTimeLimitUnit")"/>
  <input type="hidden" id="tableName" name="tableName" value="$!request.getParameter("tableName")"/>
  <div class="Heading">
    <!-- 
    <div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
    <div class="HeadingTitle">$text.get("workflow.msg.transactionWarmKind")</div>
    <ul class="HeadingButton">
      <li>
        <button type="button" name="abc" onClick="submitMode()" id="offPre" class="b2">$text.get('common.lb.submit')</button>
      </li>
      <li>
        <button type="button" name="closeBtn" id="onPre"   class="b2" onClick="window.close();">$text.get('common.lb.close')</button>
      </li>
    </ul>
  </div>
     -->
  <div class="scroll_function_small_a">
      <table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" width="100%">
        <thead>
          <tr>
            <td width="15" align="center">&nbsp;</td>
            <td width="80" align="center">&nbsp;</td>
          </tr>
        </thead>
        <tbody>
        <tr style="height: 140px;">
          	<td width="15" align="center">$text.get("common.msg.center")</td>
          	<td width="80">
          	 	<textarea id="content" name="content" style="width: 400px;height: 120px;" rows="2">#if($!request.getParameter("applyBy"))$globals.toChinseChar($!request.getParameter("applyBy"))$text.get("workflow.msg.submit")$globals.toChinseChar($!request.getParameter("moduleName")):$globals.toChinseChar($!request.getParameter("content"))$text.get("workflow.msg.NeedYouAuditing")#else$text.get("workFlow.lb.have")$globals.toChinseChar($!request.getParameter("moduleName"))$text.get("workFlow.msg.checkSee") #end</textarea>
          	</td>
        </tr>
        <tr>
        	<td width="15" align="center">$text.get("workflow.msg.warmkind")：</td>
          	<td width="80">
          	#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
	   			<input type="checkbox" id="preMode" name="preMode" value="$row_wakeUpType.value" #if("$row_wakeUpType.value"=="4") checked  #end style="width:20px;" />$row_wakeUpType.name
	   		#end&nbsp;
          	</td>
        </tr>
        </tbody>
      </table>
    </div>
</form>
</body>
</html>
