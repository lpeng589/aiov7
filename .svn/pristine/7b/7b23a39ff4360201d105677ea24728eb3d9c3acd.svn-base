
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get('common.alert.setalert')</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/setTime.js"></script>
<script>
function showState()
{
	var state="$!state";
	if(state=="true")
	{
	alert("$text.get('oa.bbs.operationOk')");	
	window.returnValue=true;
	window.close();
	}else if(state=="false")
	{
	  alert("$text.get('common.alert.updateFailure')");
	  window.returnValue=false;
	  window.close();
	}
}

</script>
</head>

<body  onLoad="showState()" style="overflow-y:auto; overflow-x:hidden;">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request"  name="form1" action="/TimeNoteSetAction.do" target="formFrame">
  <input type="hidden" name="operation" value="$globals.getOP('OP_UPDATE')">
  <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="">
  <input type="hidden" name="alertId" value="$!alertId">
  <input type="hidden" name="type" value="time">
  <div class="Heading">
    <div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
    <div class="HeadingTitle">$text.get('common.msg.set')"$globals.getEnumerationItemsDisplay("TimingMsg",$!sqlDefine)"$text.get('timeNote.lb.time') </div>
    <ul class="HeadingButton">
      <li>
        <button type="submit" name="submit" id="offPre" title="Ctrl+F" class="b2">$text.get('common.lb.submit')</button>
      </li>
      <li>
        <button type="button" name="closeBtn" id="onPre" title="Ctrl+O"  class="b2" onClick="window.close();">$text.get('common.lb.close')</button>
      </li>
    </ul>
  </div>
  <div class="scroll_function_small_a">
     $text.get('timeNote.lb.time'):<input type="text" name="noteTime" value=""  onClick="_SetTime(this);" >
    </div>
</form>
</body>
</html>
