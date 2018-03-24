<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.createby.wakeup")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript">
	function selectNewCreateBy(){
		displayName = encodeURI("$text.get('com.crm.newcreateby')") ;
		var strURL = "/UserFunctionAction.do?&selectName=pop_NewClientRemove&operation=$globals.getOP("OP_POPUP_SELECT")" ;
		var str  = window.showModalDialog(strURL+"&MOID=$MOID&MOOP=add&popPop=true&LinkType=@URL:&&displayName="+displayName,winObj,"dialogWidth=700px;dialogHeight=430px"); 
		if(typeof(str)!="undefined"){
			var fieldValue=str.split(";"); 
			document.getElementById("newCreateBy").value=fieldValue[0];
			document.getElementById("newCreateByName").value=fieldValue[1];
		}
	}
	
	function beforeSubmit(){
		var varCreateBy = document.getElementById("newCreateByName").value ;
		var varCreateById = document.getElementById("newCreateBy").value ;
		var varWakeUp = document.getElementsByName("wakeUpMode") ;
		if(varCreateBy.trim().length==0){
			alert("$text.get('crm.createby.not.null')") ;
			return ;
		}
		var varWakeUpValue = "" ;
		for(i=0;i<varWakeUp.length;i++){
			if(varWakeUp[i].checked){
				varWakeUpValue += varWakeUp[i].value+"," ;
			}
		}
		window.parent.returnValue = varCreateById+"|"+varWakeUpValue;
		window.close() ;
	}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request"  name="form1" action="/AlertSetAction.do" target="formFrame">
  <input type="hidden" name="operation" value="$globals.getOP('OP_UPDATE')">
  <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="">
  <input type="hidden" name="alertId" value="$!alertId">
  <div class="Heading">
    <div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
    <div class="HeadingTitle">$text.get("crm.createby.wakeup") </div>
    <ul class="HeadingButton">
      <li>
        <button type="button" name="submit" id="offPre" title="Ctrl+F" class="b2" onClick="beforeSubmit()">$text.get('common.lb.submit')</button>
      </li>
      <li>
        <button type="button" name="closeBtn" id="onPre" title="Ctrl+O"  class="b2" onClick="window.close();">$text.get('common.lb.close')</button>
      </li>
    </ul>
  </div>
  <div class="listRange_Pop-up">
  <ul class="listRange_1_Pop-up">
    	<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$text.get("com.crm.newcreateby")：<input type="text" id="newCreateByName" name="newCreateByName" readonly="readonly" onDblClick="selectNewCreateBy();">
    			<input type="hidden" id="newCreateBy" name="newCreateBy" value="" />
    			<img src="/$globals.getStylePath()/images/St.gif" style=" cursor:pointer" onClick="selectNewCreateBy();">
    </li>
	</ul>
	<div style="margin-left:15px; margin-top:10px; padding-top:10px;">
	<fieldset style="width:100%">
                  <legend>$text.get("oa.calendar.wakeupType")</legend>
	<div style="margin-left:15px; padding-top:30px;padding-bottom:30px;">
        #foreach($item in $globals.getEnumerationItems("WakeUpMode"))
      	 	<input type="checkbox" id=""wakeUpMode"" style="width:15px; border:0px;" name="wakeUpMode" value="$item.value">$item.name
			&nbsp;&nbsp;
        #end
		</div>
    </fieldset>	
	</div>
  </div>
</form>
</body>
</html>
