<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get('comm.alert.setalertobject')</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" language="javascript">
	function showState()
	{
		var state="$!state";		
		if(state=="true")
		{
		alert("$text.get('oa.bbs.operationOk')");	
		parent.parent.dealStatus();//调用父类reload页面方法
			//window.returnValue=true;
			//window.close();
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
<form  method="post" scope="request"  name="alertSetForm" action="/TimeNoteSetAction.do" target="formFrame">
 <input type="hidden" name="operation" value="$globals.getOP('OP_UPDATE')">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="">
 <input type="hidden" name="type" value="employee">
 <input type="hidden" name="alertId" value="$!alertId">
<div class="Heading">
  <div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
  <div class="HeadingTitle">$text.get('common.msg.set')"$globals.getEnumerationItemsDisplay("TimingMsg",$!sqlDefine)"$text.get('timeNote.lb.object') </div>
  <ul class="HeadingButton">
    <li>
      <button type="submit" name="offPre" id="offPre" title="Ctrl+F" class="b2">$text.get('common.lb.submit')</button>
    </li>
    <li>
      <button type="button" name="onPre" id="onPre" title="Ctrl+O"  class="b2" onClick="window.close();">$text.get('common.lb.close')</button>
    </li>
  </ul>
</div>
  <div class="scroll_function_small_a">
  <table sortCol="0" isDesc="true" border="0" hasStat=false cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table" width="100%">
    <THead>
      <tr class="scrollColThead">
        <td width="40" align="center" ><input type="checkbox" name="selAll"  onClick="checkAll('empids')"></td>
        <td width="*" align="center">$text.get('timeNote.lb.object')</td>
		 <td width="100" align="center">$text.get('oa.dept.name')</td>
      </tr>
    </THead>
    <tbody>
	#foreach($row in $empList)
      <tr >
        <td align="center" ><input list="true" type="checkbox" name="empids" value="$globals.get($row,0)" #if($globals.get($row,3)==1) checked="checked" #end></td>
        <td  align="center">$globals.get($row,1)</td>
		<td align="center">$globals.get($row,2)</td>
      </tr>
    #end
    </tbody>
  </table>
</div>
</form>
</body>
</html>
