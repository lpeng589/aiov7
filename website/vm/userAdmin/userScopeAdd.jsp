<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>aio_a</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript">
function changeMenu()
{
  	var evetParent = event.srcElement.parentElement.parentElement;
  	if(evetParent!=null&&evetParent!=undefined)
  	{
   		for(var i=0;i<evetParent.childNodes.length;i++)
   		{
    		if(evetParent.childNodes[i].tagName=="DIV"&&evetParent.childNodes[i]
											!=event.srcElement.parentElement  &&
											evetParent.childNodes[i].className.
											indexOf("listRange_1_space_1_div_1")!=-1){
			
     			evetParent.childNodes[i].className = "listRange_1_space_1_div_2";
     			document.getElementById(evetParent.childNodes[i].showTb).style.display = "none";
     			event.srcElement.parentElement.className ="listRange_1_space_1_div_1";
     			document.getElementById(event.srcElement.parentElement.showTb).style.display = "block";
    		}
   		}
  	}
}
 
 function openWindow(url,element,moduleid,tableName,fieldName){
 
	var strings = document.getElementById(fieldName).value.split("@");
	var urltemp = "";
	for(var i = 0; i < strings.length; i ++){
		if(strings[i] != "" && strings[i] != null && strings[i] != undefined){
			urltemp +=  strings[i].split("#")[3] + "-";
		}
	}
	url += ("&alreaPurview=" + urltemp);
 	var str = window.showModalDialog(url,"","dialogWidth=730px;dialogHeight=450px");
	var items = str.split("@");
	var values = "";
	var str = "";
	for(var i = 0; i < items.length - 1; i ++){
	 	if(element == "data"){
			values += items[i].split('#')[0] + "\n";
		}else{
			values += items[i].split('#')[1] + "\n";
		}
		str += moduleid + "#" + tableName + "#" + fieldName 
			 + "#" + items[i].split('#')[0] + "@";
	}
	document.getElementById(element + moduleid).value =  values;
	document.getElementById(fieldName).value = str;

 }
</script>
</head>

<body>

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/UserManageAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_SCOPE_PURVIEW_ADD")">
<input type="hidden" name="NOTTOKEN" value="true">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("common.lb.userRight")</div>
	<ul class="HeadingButton">
		<!--<li><button onclick="changeRights(this);">高级设置</button></li>-->
		
		 <li><button type="submit" onClick="return check();" class="b2">$text.get("mrp.lb.save")</button></li>
		 <li><button type="button" onClick="location.href='/UserQueryAction.do?operation=$globals.getOP("OP_QUERY")'" class="b2">$text.get("common.lb.back")</button></li>
		 
	</ul>
</div>

<div id="listRange_id">
	<div class="listRange_1">
	    <input type="hidden" name="id" value="$!result.id"/>
		<li><span>$text.get("userManager.lb.userName")：</span><input name="employeeId" type="text" value="$!result.employeeId"></li>
		<li><span>$text.get("userManager.lb.LoginName")：</span><input name="sysName" type="text" value="$!result.sysName"></li>
		<li><span>$text.get("bol88.e.password")：</span><input name="password" type="password" value="$!result.password"></li>	

	</div>

		<div class="listRange_1_space_1">
		#set($temp = 1)
		#set($num = 0)
		#foreach ($row in $stairModule)
		#set($num = $num + 1)
		#if($num == 1)
			#set($firstModule = $row.getId())
		#end
		<div class="listRange_1_space_1_div_$temp#if($temp==1)#set($temp=2)#end" showTb="show_$row.getId().trim()"><span onClick="changeMenu('$row.getId().trim')"> $row.getModelName()</span>
		</div>
		#end
		</div>
		
		#set($display='block;')
		#foreach ($row in $moduleMap.keySet())
		#if($row.getId() == $firstModule) #set($display='block;') #else #set($display='none;') #end
		<div id="show_$row.getId().trim()" style="display:$display#if($display=='block;')#set($display='none;')#end">
				<div class="scroll_function_small_a" id="conter">	
				 <table border="0" cellpadding="0" cellspacing="0" class="listRange_list">
					<thead>
						<tr style="text-align:center;">
							<td>$text.get("common.lb.allModule")</td>
							<td colspan="2">$text.get("common.lb.personArea")</td>
							<td colspan="2">$text.get("common.lb.deptArea")</td>
							<td colspan="2">$text.get("common.lb.userAdmin.dataArea")</td>
						</tr>
					</thead>
					
					<tbody>
					#foreach ($child in $moduleMap.get($row).keySet())		
						<tr style="text-align:center;">
							<td>$child.getModelName()</td>
							<td  style="text-align:right;width:130px" valign="top">
								#set($pValue = "")
								#set($mValue = "")
								#set($dValue = "")
								#set($pParam = "")
								#set($mParam = "")
								#set($dParam = "")
								#foreach($cell in $moduleMap.get($row).get($child))
									#set($num = 0)
									#set($flag = "")
									#foreach($val in $cell)
										#set($num = $num + 1)
										#if($num == 1)
											#set($flag = $val)
										#end										
										#if($num == 2 && $flag == "P")
											#set($pParam = $pParam + ($child.getId().trim() + "#" + "tblEmployee" + "#" + "employee" +"#" + $val + "@")) 
										#end
										#if($num == 2 && $flag == "M")
											#set($mParam = $mParam + ($child.getId().trim() + "#" + "tblDepartment" + "#" + "department" + "#" +$val +"@"))
										#end
										#if($num == 2 && $flag == "D")
											#set($dParam = $dParam  + ($child.getId().trim() + "#" + $child.getTblName() + "#" + "id" +"#" + $val +"@"))
										#end								 
										#if($num == 3 && $flag == "P")
											#set($pValue = $pValue + $val)
										#end
										#if($num == 3 && $flag == "M")
											#set($mValue = $mValue + $val)
										#end
										#if($num == 3 && $flag == "D")
											#set($dValue = $dValue + $val)
										#end
									#end
								#end
								<textarea id="employee$child.getId().trim()">$!pValue</textarea>
								</td>
							<td  style="width:100px; border-left:0px" valign="top">
								<div style="float:left;margin-top:2px"><button onClick="openWindow('/selectTableAction.do?tableName=tblEmployee&fieldName=EmpFullName&moduleId=$child.getId().trim()&scopeFlag=P&employeeId=$!result.employeeId.trim()','employee','$child.getId().trim()','tblEmployee','employeeParam$child.getId().trim()');">$text.get("common.lb.maintenance")</button></div>
							</td>
							
							<td  style="text-align:right;width:130px" valign="top">

								<textarea id='department$child.getId().trim()'>$!mValue</textarea>
							</td>
							<td  style="width:100px; border-left:0px" valign="top">
								<div style="float:left;margin-top:2px"><button onClick='openWindow("/selectTableAction.do?tableName=tblDepartment&fieldName=DeptFullName&moduleId=$child.getId().trim()&scopeFlag=M&employeeId=$!result.employeeId.trim()","department","$child.getId().trim()","tblDepartment","departmentIDParam$child.getId().trim()");'>$text.get("common.lb.maintenance")</button></div>
							</td>
							
							<td  style="text-align:right;width:130px" valign="top">
			
								 <textarea id='data$child.getId().trim()'>$!dValue</textarea></td>
							<td  style="width:100px; border-left:0px" valign="top">
								<div style="float:left;margin-top:2px"><button onClick='openWindow("/selectTableAction.do?tableName=$child.getTblName().trim()&operation=$globals.getOP("OP_QUERY")&moduleId=$child.getId().trim()&employeeId=$!result.employeeId.trim()","data","$child.getId().trim()","$child.getTblName()","dataIDParam$child.getId().trim()");'>$text.get("common.lb.maintenance")</button></div>
							</td>					
						</tr>
						<input type="hidden" id="employeeParam$child.getId().trim()" name="employeeID" value="$!pParam"/>
						<input type="hidden" id="departmentIDParam$child.getId().trim()" name="departmentID" value="$!mParam" />
						<input type="hidden" id="dataIDParam$child.getId().trim()" name="dataID" value="$!dParam" />
						#end
						
					  </tbody>
				  </table>
				</div>	
		</div>
		#end
</form>
<script type="text/javascript">
//<![CDATA[
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-160;
oDiv.style.height=sHeight+"px";
//]]>
</script>
</body>
</html>
