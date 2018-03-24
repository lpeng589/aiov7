<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" type="text/css" href="/$globals.getStylePath()/css/ListRange.css">
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css" />
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="$globals.js("/js/date.vjs","",$text)"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript">
function ref(){
	location.reload();
}
function startPath(){
	var pathID = form.pathID.value;
	if(pathID=="") return;
	var urlstr = '/CareforExecuteAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&pathId='+pathID+'&clientId=$!clientId';
	window.parent.asyncbox.open({id:'CareforPopup',title:'客户关怀',url:urlstr,width:730,height:450,btnsbar:parent.jQuery.btn.OKCANCEL,
		callback : function(action,opener){
　　　　　	if(action == 'ok'){
				opener.start();
				return false;
　　　　　	}
　　　	}
　	});
}

function openMessage(id){
	var urlstr = '/CareforExecuteAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&delId='+id;
	parent.asyncbox.open({id:'CareforPopup',title:'客户关怀',url:urlstr,width:730,height:450});
}

function stop(id){
	form.id.value=id;
	form.isstop.value="true";
	form.submit();
}
function restart(id){
	form.id.value=id;
	form.isstop.value="false";
	form.submit();
}
</script>
<style>
.clientgo {
	width:100%;
	float:left;
	padding-top:5px;
	background:url(/style/images/aiobg.gif) repeat-x left -221px;
}
.clientgo ul { margin:0px; padding:0px;}
.clientgo li {   height:30px; line-height:30px;}
.clientgo2 ul { margin:0px; padding:0px;}
.clientgo2 li {height:28px; padding-top:8px; padding-bottom:4px;padding-left:5px}
.clientgo img { vertical-align:middle; margin-left:10px;}
.client_list img { vertical-align:middle;}
.butgo {
	width:auto;
	border:0px;
}
.client_list { border-top:1px solid #AED3F1;border-left:1px solid #AED3F1;}
.client_list td{ border-right:1px solid #AED3F1;border-bottom:1px solid #AED3F1; height:25px; line-height:25px; padding-left:5px; padding-right:5px;}

</style>
</head>
<body>
<form method="post" name="form" action="/CareforExecuteAction.do">
<input type="hidden" name="operation" value="$globals.getOP('OP_UPDATE')">
<input type="hidden" name="id" value="">
<input type="hidden" name="type" value="stopPath">
<input type="hidden" name="isstop" value="true">
<input type="hidden" name="clientId" value="$!clientId">
	<div class="clientgo">
	<ul>
		<li>
				&nbsp;&nbsp;$text.get('crm.careforaction.lb.startpath')：



				<select name='pathID'>
					#foreach($carefor in $allCarefor)
					<option value="$globals.get($carefor,0)">
						$globals.get($carefor,1)
					</option>
					#end
				</select>&nbsp;&nbsp;<button type="button" style="margin-bottom:8px" onClick="javascript:startPath();">$text.get('common.lb.run')</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

		$text.get('crm.careforaction.lb.imageshow')
		<img src='/$globals.getStylePath()/images/carefor/ok.gif'>$text.get('common.msg.DEFAULT_SUCCESS')
		<img src='/$globals.getStylePath()/images/carefor/up.gif'>$text.get('crm.careforaction.lb.over')
		<img src='/$globals.getStylePath()/images/carefor/error.gif'>$text.get('common.msg.DEFAULT_FAILURE')
		<img src='/$globals.getStylePath()/images/carefor/not.gif'>$text.get('crm.careforaction.lb.notrun')


			</li>
		</ul>
	</div>
	<div style="float:left; padding-left:3px; padding-right:3px; height:auto; width:100%">
	<table width="100%" class="client_list" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="100" align="right" valign="middle" bgcolor="#ECF5FF">$text.get('crm.careforaction.lb.execingpath')：</td>
    <td width="*" align="left" valign="top">
		<div class="clientgo2">
		<ul> 
			#foreach($bean in $r1)		
			<li> 
					<strong>$bean.careforName</strong>
					&nbsp;<button  onClick="stop('$bean.id');">$text.get('Stop.button.tblProduceInform')</button>				
					#foreach($one in $bean.careforActionDels) 
						#set($astyle = "")
						#if("$!one.endDate"!="")  
							#set($timediff = $globals.getTimeDiff("$!one.createTime","$!one.endDate")) 
							#if( $timediff> 0)
								#set($astyle = "style='color:red;' title='$text.get('crm.careforaction.lb.overTime')$timediff $text.get('oa.calendar.day')' ")
							#end
						#end					
						<a href='javascript:openMessage("$one.id")' $astyle > $one.eventName 
						#if("$one.status" == "2")
							<img src='/$globals.getStylePath()/images/carefor/ok.gif'>
						#elseif("$one.status" == "3")
							<img src='/$globals.getStylePath()/images/carefor/up.gif'>
						#elseif("$one.status" == "4")
							<img src='/$globals.getStylePath()/images/carefor/error.gif'>
						#else
							<img src='/$globals.getStylePath()/images/carefor/not.gif'>
						#end	
							</a>
							&nbsp;&nbsp; 
					#end							
				</li>
			#end						&nbsp;
			</ul>
		</div>
	</td>
  </tr>
  <tr>
    <td align="right" valign="middle" bgcolor="#ECF5FF">$text.get('crm.carefor.lb.overpath')：</td>
    <td  align="left" valign="top">
	<div class="clientgo2">
		<ul>
			#foreach($bean in $r2)		
			<li>
					<strong>$bean.careforName</strong>
					&nbsp;		
					#foreach($one in $bean.careforActionDels)
						
						#set($astyle = "")
						#if("$!one.endDate"!="")  
							#set($timediff = $globals.getTimeDiff("$!one.createTime","$!one.endDate")) 
							#if( $timediff> 0)
								#set($astyle = "style='color:red;' title='$text.get('crm.careforaction.lb.overTime')$timediff $text.get('oa.calendar.day')' ")
							#end
						#end					
						<a href='javascript:openMessage("$one.id")' $astyle > $one.eventName 
						#if("$one.status" == "2")
							<img src='/$globals.getStylePath()/images/carefor/ok.gif'>
						#elseif("$one.status" == "3")
							<img src='/$globals.getStylePath()/images/carefor/up.gif'>
						#elseif("$one.status" == "4")
							<img src='/$globals.getStylePath()/images/carefor/error.gif'>
						#else
							<img src='/$globals.getStylePath()/images/carefor/not.gif'>
						#end	
							</a>
							&nbsp;&nbsp; 
					#end							
				</li>
			#end						&nbsp;
		</ul>
</div>
	</td>
  </tr>
  <tr>
    <td align="right" valign="middle" bgcolor="#ECF5FF">$text.get('crm.careforaction.lb.stopedpath')：</td>
    <td align="left" valign="top">
			<div class="clientgo2">
			<ul>
				#foreach($bean in $r3)		
			<li>
					<strong>$bean.careforName</strong>
					&nbsp;<button  onClick="restart('$bean.id');">$text.get('common.lb.run')</button>				
					#foreach($one in $bean.careforActionDels)
						#set($astyle = "")
						#if("$!one.endDate"!="")  
							#set($timediff = $globals.getTimeDiff("$!one.createTime","$!one.endDate")) 
							#if( $timediff> 0)
								#set($astyle = "style='color:red;' title='$text.get('crm.careforaction.lb.overTime')$timediff $text.get('oa.calendar.day')' ")
							#end
						#end					
						<a href='javascript:openMessage("$one.id")' $astyle > $one.eventName 
						#if("$one.status" == "2")
							<img src='/$globals.getStylePath()/images/carefor/ok.gif'>
						#elseif("$one.status" == "3")
							<img src='/$globals.getStylePath()/images/carefor/up.gif'>
						#elseif("$one.status" == "4")
							<img src='/$globals.getStylePath()/images/carefor/error.gif'>
						#else
							<img src='/$globals.getStylePath()/images/carefor/not.gif'>
						#end	
							</a>
							&nbsp;&nbsp; 
					#end							
				</li>
			#end						&nbsp;		
		  </ul>
	  </div>
	</td>
  </tr>  
</table>
	</div>
		</form>
	</body>
</html>
