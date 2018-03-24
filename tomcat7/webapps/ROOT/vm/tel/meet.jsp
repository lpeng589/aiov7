<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="/js/formvalidate.vjs"></script>
<script type="text/javascript">

putValidateItem("mainCall","$text.get('tel.lb.hostCall')","mobile","",false,0,10000000);
putValidateItem("calls","$text.get('tel.lb.passCall')","any","",false,1,9999 );

function beforeform(){   
	if(!validate(document.form))return false;
	disableForm(document.form);
		
	return true;
}	

function showEmploy(){
	openSelect("/UserFunctionAction.do?tableName=HREmpinform&selectName=pop_viewEmployeeTel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/TelAction.do?operator=callTel').moduleId&MOOP=query",form.calls);
}
function showCumSuper(){
	openSelect("/UserFunctionAction.do?tableName=tblUser&selectName=pop_viewCompanyTel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/TelAction.do?operator=callTel').moduleId&MOOP=query",form.calls);
}
function showOAComm(){
	openSelect("/UserFunctionAction.do?tableName=OACommunicationNoteInfo&selectName=pop_ViewOACommunicationTel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/TelAction.do?operator=callTel').moduleId&MOOP=query",form.calls);
}
function showCRMClient(){
	openSelect("/UserFunctionAction.do?tableName=CRMCompanyAll&selectName=pop_ViewCRMCompanyTel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/TelAction.do?operator=callTel').moduleId&MOOP=query",form.calls);
}

function selectSMSModel(obj){
  openSelect("/UserFunctionAction.do?tableName=tblSMSModel&selectName=SelectSMSModel&operation=$globals.getOP('OP_POPUP_SELECT')&MOID=$LoginBean.operationMap.get('/NoteAction.do?operator=sendSMS').moduleId&MOOP=query",obj);
}
function openSelect(urlstr,obj){
	var str  = window.showModalDialog(urlstr,"","dialogWidth=800px;dialogHeight=450px");	
	if(str == undefined || str == ""){
		return;
	}
	if(str.indexOf(";|") == -1){
		obj.value = obj.value+ str+"\r\n";
		return;
	}
	fs=str.split(";|");
	var mobileNames="";
	for(var i=0;i<fs.length;i++){
		var value=fs[i];
		if(value !="" ){
		 	mobileNames +=value+";\r\n";
		}
	}
	obj.value = obj.value+ mobileNames;
	
}


function  totalNum()
{

}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/TelAction.do?operator=meet&type=set" name="form" method="post" target="formFrame">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
 <input type="hidden" name="noback" value="$!noback">
 <input type="hidden" name="state" value="$!state">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("tel.msg.timingCallset")</div>
	<ul class="HeadingButton">
					 <li></li>
			</ul>
</div>
<div id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");
			var sHeight=document.body.clientHeight-38;
			oDiv.style.height=sHeight+"px";
		</script>
    <div class="scroll_function_small_a">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="oabbs_tr">$text.get("tel.lb.nonceState")：</td>
				<td>
					#if($state == 1)
						$text.get("oa.bbs.started")

					#else
						$text.get("com.consign.enabled")

					#end
				</td>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("tel.lb.meetingTime")：</td>
				<td>
					<select name="hour">
						<option value="06" #if($hour =="06") selected #end>06</option>
						<option value="07" #if($hour =="07") selected #end>07</option>
						<option value="08" #if($hour =="08") selected #end>08</option>
						<option value="09" #if($hour =="09") selected #end>09</option>
						<option value="10" #if($hour =="10") selected #end>10</option>
						<option value="11" #if($hour =="11") selected #end>11</option>
						<option value="12" #if($hour =="12") selected #end>12</option>
						<option value="13" #if($hour =="13") selected #end>13</option>
						<option value="14" #if($hour =="14") selected #end>14</option>
						<option value="15" #if($hour =="15") selected #end>15</option>
						<option value="16" #if($hour =="16") selected #end>16</option>
						<option value="17" #if($hour =="17") selected #end>17</option>
						<option value="18" #if($hour =="18") selected #end>18</option>
						<option value="19" #if($hour =="19") selected #end>19</option>
						<option value="20" #if($hour =="20") selected #end>20</option>
						<option value="21" #if($hour =="21") selected #end>21</option>
						<option value="22" #if($hour =="22") selected #end>22</option>
						<option value="23" #if($hour =="23") selected #end>23</option>
					</select>
					<select name="time">
						<option value="00" #if($time =="00") selected #end>00</option>
						<option value="05" #if($time =="05") selected #end>05</option>
						<option value="10" #if($time =="10") selected #end>10</option>
						<option value="15" #if($time =="15") selected #end>15</option>
						<option value="20" #if($time =="20") selected #end>20</option>
						<option value="25" #if($time =="25") selected #end>25</option>
						<option value="30" #if($time =="30") selected #end>30</option>
						<option value="35" #if($time =="35") selected #end>35</option>
						<option value="40" #if($time =="40") selected #end>40</option>
						<option value="45" #if($time =="45") selected #end>45</option>
						<option value="50" #if($time =="50") selected #end>50</option>
						<option value="55" #if($time =="55") selected #end>55</option>
					</select>
					&nbsp;&nbsp;&nbsp;&nbsp;
					
					<input type="checkbox" name="meetdate" $w1 value="1">$text.get("oa.calendar.monday")
					<input type="checkbox" name="meetdate" $w2 value="2">$text.get("oa.calendar.tuesday")

					<input type="checkbox" name="meetdate" $w3 value="3">$text.get("oa.calendar.wednesday")

					<input type="checkbox" name="meetdate" $w4 value="4">$text.get("oa.calendar.thursday")

					<input type="checkbox" name="meetdate" $w5 value="5">$text.get("oa.calendar.friday")

					<input type="checkbox" name="meetdate" $w6 value="6">$text.get("oa.calendar.saturday")

					<input type="checkbox" name="meetdate" $w0 value="0">$text.get("oa.calendar.sunday")

			</tr>	
			<tr>
				<td class="oabbs_tr">$text.get("tel.lb.hostCall")：</td>
				<td>
					<input type="text" name="mainCall" value="$!mainCall" class="text" style="width:200px"><span style="color: #0066FF;margin-left:10px">$text.get("tel.lb.hostMustMobilephone")</span>
				</td>
			</tr>		
				
			<tr>
			 <td class="oabbs_tr">$text.get("tel.lb.passCall")：</td>
			 <td><span style="color: #0066FF">$text.get("tel.msg.timingCallPower")<br/>$text.get("tel.msg.SystemCallAllusers")</span>
			 <br/>
		<textarea name="calls"  id="calls" cols="45" rows="10" onpropertychange="totalNum()" >$!calls</textarea>
		<a href="javascript:void(0)" onClick="showEmploy()">$text.get("note.lb.SelectEmployee")</a>
		</td>			   
		</tr>
		<tr>
			<td colspan="2">
				#if($state == 1)
				<button type="button" name="button" onClick="document.form.state.value='1';  if(beforeform()) {document.form.submit();}" class="b2" style="margin-left:250px;width:80px">$text.get("common.lb.reset")</button>
				<button type="button" name="button" onClick="document.form.state.value='0';  if(beforeform()) {document.form.submit();}" class="b2" style="width:80px">$text.get("common.lb.cancel")</button>
				#else
				<button type="button" name="button" onClick="document.form.state.value='1';  if(beforeform()) {document.form.submit();}" class="b2" style="margin-left:250px;width:80px">$text.get("common.msg.set")</button>
				#end
			</td>
		</tr>
		  </tbody>
		</table>
	</div>
</div>
</form>
</body>
</html>
