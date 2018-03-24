<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("note.send.content")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/formvalidate.vjs"></script>
<script type="text/javascript">
function beforeform(){	
	var content=form.content.value;
	if(content.match(/^\s*$/)){
	    alert("$text.get('sms.send.contentnull')");
		form.content.focus();
		return false;
	}
	var str=document.getElementById("mobileContent").value;
	var str1="";
	var j=0;
	for(var i=0;i<str.length;i++){
	    var a=str.charAt(i);
	    if(a.match(/^[\u4e00-\u9fa5]$/)){
	        j+=2;      
	    }else{
	        j++;
	    }
	    if(j>140){
	        j=0;
	        i--;
	        str1+="/KORON_SMS_SPLIT_FLAG/";
	    }else{
	        str1+=a;
	    }
	}
	window.returnValue = str1 ;
	window.close() ;
}

function  totalNum()
{
    var str=document.getElementById("mobileContent").value;
	var count= str.replace(/[^\x00-\xff]/g,"**").length;
	document.getElementById("tip").innerHTML=count;
}

function selectModel(){
	var displayName = encodeURI("$text.get("send.lb.note")") ;
	var str  = window.showModalDialog("/UserFunctionAction.do?selectName=SelectSMSModel2&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$MOID&MOOP=query&LinkType=@URL:&displayName="+displayName,"","dialogWidth=800px;dialogHeight=450px");	
	if(str == undefined || str == ""){
		return;
	}
	var strValue = str.split(";") ;
	document.getElementById("mobileContent").value = strValue[1] ;
}
</script>
</head>
<body scroll="no">
<form action="/NoteAction.do?operator=sendSMS&type=add" onSubmit="return beforeform()"  name="form" method="post">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("note.send.content")</div>
	<ul class="HeadingButton">
		<li>
			<button onClick="javascript:window.close();">$text.get("common.lb.close")</button>
		</li>
	</ul>
</div>
    <div class="scroll_function_small_a">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
			 <td class="oabbs_tr" width="60">$text.get("note.send.content")：</td>
			 <td>
		<textarea name="content"  id="mobileContent" cols="55" rows="10""></textarea>
			   </td>
			   
		</tr>
					<tr>
					  <td colspan="2" class="oabbs_tc">
					  <button type="button" name="button" onClick="if(beforeform()) {document.form.submit();}" class="b2">$text.get("common.lb.save")</button>
			          <button type="reset" class="b2">$text.get("common.lb.reset")</button>
			   </td>
          </tr>
		  </tbody>
		</table>
	</div>
</form>
</body>
</html>
