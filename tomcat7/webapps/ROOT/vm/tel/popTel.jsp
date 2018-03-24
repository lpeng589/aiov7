<html>
<head>
<meta name="renderer" content="webkit">
<title>$text.get("tel.title.selectCallNumber") </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="/js/formvalidate.vjs"></script>
<script type="text/javascript">


function beforeform(){   
	if(!validate(document.form))return false;
	disableForm(document.form);		
	return true;
}	
</script>
<base target="_self"/>
</head>
<body>
<form action="/TelAction.do?operator=popTel&type=call" name="form" method="post">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="noback" value="$!noback">
  <input type="hidden" name="mainCall" value="$!mainCall">
   <input type="hidden" name="name" value="$!name">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("tel.title.selectCallNumber") </div>
	<ul class="HeadingButton">
					 <li></li>
			</ul>
</div>
<div id="listRange_id">

    <div class="scroll_function_small_a">
		<table width="60%" border="0" align="center" cellpadding="0" cellspacing="0" class="oalistRange_list_function"  name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">$text.get("tel.title.selectCallNumber")&nbsp;</td>
			</tr>
		</thead>
		<tbody>
		#foreach($row in $phones) 
			<tr>		
				<td colspan="2"><input type="radio" name="tel" value="$globals.get($row,1)" checked>
					$globals.get($row,0):$globals.get($row,1)
				</td>
			</tr>
		#end	
		<tr>
			<td colspan="2" class="oabbs_tc">
				<button type="button" name="button" onClick="if(beforeform()) {document.form.submit();}" class="b2" style="width:80px">$text.get("tel.lb.beginCalling")</button>
				<button type="button" name="button" onClick="window.close()" class="b2" style="width:80px">$text.get("com.lb.close")</button>
			</td>
		</tr>
		  </tbody>
		</table>
	</div>
</div>
</form>
</body>
</html>
