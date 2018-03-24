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
 
 	var string = document.getElementById(fieldName + moduleid).value;
 	var strings = string.split("@");
	var tempUrl = "";
	for(i = 1 ; i < strings.length; i ++){
		tempUrl += (strings[i] + "-");
	}
	url += ("&alreaPurview=" + tempUrl);
 	var str = window.showModalDialog(url,"","dialogWidth=730px;dialogHeight=450px");
	var items = str.split("@");
	var values = "";
	var str = "";
	
	for(var i = 0; i < items.length - 1; i ++){
	 	
		values += items[i].split('#')[1] + "\n";
		str += "@" + items[i].split('#')[2] ;
	}
	document.getElementById(element + moduleid).value =  values;
	var value = document.getElementById(fieldName + moduleid).value;
	document.getElementById(fieldName + moduleid).value = moduleid + str;
 }
 
 function selectAll(tagName,name,targetId,type){
	
	items = document.getElementsByTagName(tagName);
	for(var i=0;i<items.length;i++){
		if(items[i].type == type && items[i].name == name && items[i].target == targetId.split("@")[1]){
			items[i].checked = document.getElementById(targetId).checked;
		}
	}
}
</script>
</head>

<body>

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/UserManageAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_SHOW_PURVIEW_ADD")">
<input type="hidden" name="NOTTOKEN" value="true">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("common.title.uerShowRight")</div>
	<ul class="HeadingButton">
		<!--<li><button onclick="changeRights(this);">高级设置</button></li>-->

	
<li><button type="submit" onClick="return check();" class="b2">$text.get("mrp.lb.save")</button></li>
<l<button type="button"    onClick="location.href='/UserQueryAction.do?operation=$globals.getOP("OP_QUERY")'" class="b2">$text.get("common.lb.back")</button></li>
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
		#set($firstModule = "")
		#foreach ($row in $stairModule)
		#set($num = $num + 1)
		#if($num == 1)
			#set($firstModule = $row.getId())
		#end
		<div class="listRange_1_space_1_div_$temp#if($temp==1)#set($temp=2)#end" showTb="show_$row.getId().trim()"><span onClick="changeMenu('$row.getId().trim()')"> $row.getModelName()</span>
		</div>
		#end
		</div>
		
		#set($display='block;')
		#foreach ($row in $moduleMap.keySet())
		#if($row.getId() == $firstModule) #set($display='block;') #else #set($display='none;') #end
		<div id="show_$row.getId().trim()" style="display:$display#if($display=='block;')#set($display='none;')#end">
				<div class="listRange_1_space_1">$text.get("role.lb.viewRight")</div>
		<div class="scroll_function_small_a" id="conter">
			<script type="text/javascript">
				var oDiv=document.getElementById("conter");
				var sHeight=document.body.clientHeight-38;
				oDiv.style.height=sHeight+"px";
			</script>
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list">
			<thead>
				<tr style="text-align:center;">
					<td><input id="allClos@$row.getId().trim()" type="checkbox" value="allClos" style="border:0px;width:16px;" target="$row.getId().trim()" onClick="selectAll('input','allColumns','allClos@$row.getId().trim()','checkbox')">$text.get("role.lb.allRight")</td>
					<td>$text.get("common.lb.AllLine")</td>
					<td colspan="2">$text.get("common.lb.NoShowLine")</td>
				</tr>
			</thead>
			<tbody>
			#foreach ($child in $moduleMap.get($row).keySet())
				<tr style="text-align:center;">
					<td>$child.getModelName()</td>
					
					<td><input id="allColumns" name="allColumns" type="checkbox" value="$child.getId().trim()" style="border:0px;width:16px;" target="$row.getId().trim()"></td>						
								#set($controlValue = "")
								#set($controlParam = "")
								#foreach($cell in $moduleMap.get($row).get($child))
									#set($num = 0)
									#foreach($val in $cell)
										#set($num = $num + 1)
										#if($num == 1)
											#set($controlParam = $controlParam + ("@" + $val))
										#end	
										#if($num == 2)
											#set($controlValue = $controlValue + $val)
										#end
									#end
								#end
					<td style="text-align:right;width:200px" valign="top">
					     <textarea id="field$child.getId().trim()" >$controlValue
                        </textarea>
					</td>
				    <td  style="width:100px; border-left:0px" valign="top">
						<div style="float:left;margin-top:2px"><button onClick='openWindow("/selectTableAction.do?tableName=$child.getTblName()&operation=$globals.getOP("OP_POPUP_SELECT")&employeeId=$!result.employeeId&moduleId=$child.getId().trim()","field","$child.getId().trim()","$child.getTblName()","discontrol");'>$text.get("common.lb.maintenance")</button></div>
					</td>
				</tr>
				<input id="discontrol$child.getId().trim()" name='discontrol' type='hidden' value="$child.getId().trim()$controlParam" />
			#end		
				
			  </tbody>
			</table>
		</div>
		#end
        </div>
</div>
</form>
</body>
</html>

