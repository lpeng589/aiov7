<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/listGrid.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<style >
.tips_div{
width:520px;
height:147px;
border:1px solid #57a7ff;
background-color:#e3f0ff;
}

.tips_title{
width:100%;
height:28px;
line-height:28px;
vertical-align:middle;
border-top:1px solid #fff;
border-left:1px solid #fff;
border-right:1px solid #fff;
border-bottom:1px solid #57a7ff;
background-color:#acd3fe;
}

.left_title{
float:left;
padding-left:10px;
}

.tips_close{
padding-top:2px;
padding-right:2px;
float:right;
}

.tips_content{
margin-top:-20px;
margin-left:10px;
float:left;
}
.tips_operate{
width:100%;
float:left;
text-align:center;
margin-top:10px;
}
</style>
<script>
function closeNewTask(){
	document.getElementById("newTaskDiv").style.display="none";
}
function openNewTask(){
	document.getElementById("newTaskDiv").style.display="block";
}

function refreshStatus(){
	jQuery.get("/RobotAction.do?op=status", function(data){ 
		if(data != ""){
			rets = data.split("|");
			for(i=0;i<rets.length;i++){
				if(rets[i] != ""){
					rs = rets[i].split(":");
					$("#"+rs[0]).html(rs[1]);
					$("#DR_"+rs[0]).html(rs[2]);
					if(rs[3] == "0"){
						$("#ST_"+rs[0]).html("$text.get("common.lb.running")");
					}else{
						$("#ST_"+rs[0]).html("$text.get("common.lb.stop")");
						$("#OP_"+rs[0]).html('<a  href="javascript:deletes('+rs[0]+')"> $text.get("common.lb.del")</a>');
						
					}
					
				}
			}
		}		
		setTimeout("refreshStatus()",10000);
	}); 	
}

function followerKeyDown(){
	if(event.keyCode == 13){
		openEmp();
	}
}
function openEmp(){
	var displayName=encodeURI('$text.get("common.lb.follower")') ;
	var urlstr = '/UserFunctionAction.do?tableName=SelectRobotTask&selectName=SelectRobotTask&popupWin=EmployeePopup&operation=$globals.getOP("OP_POPUP_SELECT")&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName='+displayName;
	//var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	asyncbox.open({id:'EmployeePopup',title:'$text.get("common.lb.follower")',url:urlstr,width:800,height:470}); 
}

function exeEmployeePopup(str){
	if(typeof(str)!="undefined"){
		var mutli=str.split(";"); 
		hid="";
		dis="";
		if(str.length>2){		
			document.getElementById('follower').value=mutli[0];
			document.getElementById('followerDept').value=mutli[1];
			document.getElementById('followerName').value=mutli[2];
		}else{
			document.getElementById('follower').value='';
			document.getElementById('followerDept').value='';
			document.getElementById('followerName').value='';
		}
	}
}

jQuery(document).ready(function() {
	refreshStatus();
}); 

</script>
<script type="text/javascript">
	function deletes(code){
		if(confirm("$text.get("oa.common.sureDelete")")){
			document.location = "/RobotAction.do?op=delTask&pName="+code;
		}
	}
</script>
</head>
<body >
<div class="tips_div" id="newTaskDiv" style="display:none;height:100%;height:332px;  cursor:default; position:absolute; left:300px; top:50px;">
   	<div class="tips_title">
	     <div class="left_title">$text.get("common.lb.newTask")</div>
	     <div class="tips_close"><a href="javascript:closeNewTask(); " class="div_close">
	     <img src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("common.lb.close")" border="0"/></a></div>
   	</div>
   
   	<div class="tips_content">
		<form method="post" name="newTaskForm" action="/RobotAction.do?op=newTask">
	<table cellpadding="0" cellspacing="0" border="0" style="width:100%; margin:25px 0px 0px 0px; height:200px;">
		<tr>
			<td align="">$text.get("common.lb.key")：</td>
			<td colspan="2"><input name="keywords" type="text" style="width:410px" /><br/>
			<font color=red>$text.get("robot.lb.moreKeyMsg")</font></td>
		</tr>
		<tr>
			<td rowspan="5">&nbsp;</td>
			<td><input name="engine" type="checkbox"  checked  value="baidu" />$text.get("Baidu")</td>
	        <td rowspan="5"  valign="top"> 
				<div style="height:160px; margin:5px 0px 0px 0px; width:88%;">
				<table cellspacing="0" cellpadding="0" class="webad">					 
					  <tr>
						<td><input name="engine" type="checkbox"  checked  value="alibaba" /><a href="http://www.alibaba.com.cn/"  target="_blank">http://www.alibaba.com.cn &nbsp; &nbsp;$text.get("Alibaba")</a></td>
											
					  </tr>
					  <tr>
						<td><input name="engine" type="checkbox"  checked  value="huicong" /><a href="http://www.hc360.com.cn/"  target="_blank">http://www.hc360.com.cn &nbsp; &nbsp;$text.get("hc360")</a></td>
											
					  </tr>
					  <tr>
						<td><input name="engine" type="checkbox"  checked  value="net114" /><a href="http://www.net114.com/"  target="_blank">http://www.net114.com/ &nbsp; &nbsp;114</a></td>
											
					  </tr>
					</table>
				</div>
			</td>
        </tr>
		<tr>
		  <td><input name="engine" type="checkbox" checked  value="google" />$text.get("google")</td>
      </tr>
		<tr>
		  <td><input name="engine" type="checkbox" checked  value="soso" />$text.get("soso")</td>
      </tr>
		<tr>
		  <td><input name="engine" type="checkbox" checked  value="sogou" />$text.get("sogou")</td>
      </tr>
		<tr>
		  <td><input name="engine" type="checkbox" checked value="yahoo" />$text.get("yahoo")</td>
      </tr>
      <tr><td align="right">$text.get("common.lb.follower")：</td><td colspan=2>
      <input id="follower" name="follower" type="hidden" />
      <input id="followerDept" name="followerDept" type="hidden" />
      <input id="followerName" name="followerName" type="text" style="width:210px" onKeyDown="followerKeyDown();"/><a onClick="openEmp()"><img src="/$globals.getStylePath()/images/St.gif"/></a>
      &nbsp;&nbsp;&nbsp;$text.get("robot.lb.spages")&nbsp;&nbsp;<input name="spages" type="text" style="width:40px" value="20" />
      </td></tr>
      <tr><td></td><td colspan=2><font color=red>$text.get("common.msg.InternetRobotSearch")</font></td></tr>
	</table>
</form>

	</div>
   	<div class="tips_operate">
   	<button type="button" onClick="document.newTaskForm.submit();" style="width:80px;margin-right:20px">$text.get("common.lb.ok")</button>
	<button type="button" onClick="closeNewTask()" style="width:80px;margin-right:20px">$text.get("common.lb.close")</button>
   	</div>
</div>




<form  method="post" scope="request" name="form" action="">
	<input type="hidden" name="operation" value="4">	
	<div class="oamainhead">
		<div class="HeadingButton">
		<li style="padding-right:0px">
		<button type="button" onClick="javascript: openNewTask()">$text.get("common.lb.newTask")</button>
		<li>
		<li style="padding-right:20px">
		<button type="button" onClick="javascript: window.location.href='/RobotAction.do'">$text.get("common.lb.refresh")</button>
		<li>
		</div>
	</div>
	
	<div id="oalistRange">
	<script type="text/javascript">
	var oDiv=document.getElementById("oalistRange");
	var sHeight=document.body.clientHeight-68;
	oDiv.style.height=sHeight+"px";
	</script>
		<table width="99%" border="0" align="center" id="tblSort" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
			    <td class="oabbs_tl" width="*">$text.get("common.lb.key")</td>
				<td class="oabbs_tc" width="150">$text.get("scope.lb.tsscopeValue")</td>			
				<td class="oabbs_tc" width="80">$text.get("common.msg.SearchResult")</td>
				<td class="oabbs_tc" width="80">$text.get("import.upload.result")</td>
				<td class="oabbs_tc" width="50">$text.get("common.lb.status")</td>
				<td class="oabbs_tc" width="150">$text.get("oa.calendar.option")</td>
			</tr>
		</thead>
		<tbody>				
		  #foreach ($vg in $item.values())
		 	<tr >
			    <td class="oabbs_tl"  width="*">$!{vg.keywords}</td>
				<td class="oabbs_tc"  width="150">$!{vg.createTime}</td>
				<td class="oabbs_tr"  width="80"><div id=$vg.code> $!{vg.searchCount} </div></td>
				<td class="oabbs_tr"  width="80"><div id=DR_$vg.code> $!{vg.importCount} </div></td>
				<td class="oabbs_tc"  width="50"><div id=ST_$vg.code>#if("$!{vg.status}"=="0") $text.get("common.lb.running") #else $text.get("common.lb.stop") #end </div></td>
				<td class="oabbs_tc"  width="150"> &nbsp;
					<span id = "OP_$vg.code">
				   	#if("$!{vg.status}"=="0")
				   	<a  href="/RobotAction.do?op=stopTask&pName=$vg.code" > $text.get("common.lb.stop")</a>
				   	#else
				   	<a  href="javascript:deletes($vg.code)"> $text.get("common.lb.del")</a>
				   	#end
				   	</span>
					&nbsp;
				   	<a  href="/RobotAction.do?op=detail&pName=$vg.code"> $text.get("common.lb.detail")</a>
					&nbsp;
				</td>
			</tr>
		 #end
		
		  </tbody>
	   </table>
</div>	   
</form>	
</body>
</html>