
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("user.lb.userAdmin")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>

<script language="javascript">
function selectAllOperation(tagName,targetId,mid){
	items = document.getElementsByName(tagName);
	for(var i=0;i<items.length;i++){		
		if(items[i].mid != "undefined" && items[i].mid == mid ){
			items[i].checked = document.getElementById(targetId).checked;
		}		
	}
}

 function changeMenu()
 {
  var evetParent = event.srcElement.parentElement.parentElement;
  if(evetParent!=null&&evetParent!=undefined)
  {
   for(var i=0;i<evetParent.childNodes.length;i++)
   {
    if(evetParent.childNodes[i].tagName=="DIV"&&evetParent.childNodes[i]!=event.srcElement.parentElement&&evetParent.childNodes[i].className.indexOf("listRange_1_space_1_div_1")!=-1)
    {
     evetParent.childNodes[i].className = "listRange_1_space_1_div_2";
     document.getElementById(evetParent.childNodes[i].showTb).style.display = "none";
     event.srcElement.parentElement.className ="listRange_1_space_1_div_1";
     document.getElementById(event.srcElement.parentElement.showTb).style.display = "block";
    }
   }
  }
 }
 
function beforSubmit(form){   
	disableForm(form);
	return true;
}
</script>
</head>

<body>

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/UserManageAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_MODULE_RIGHT")">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="userId" value="$userId">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("user.lb.userAdmin")</div>
	<ul class="HeadingButton">	  
	#if($globals.getMOperation().add())
	  <li ><button type="button" onClick="if(beforSubmit(document.form)){document.form.winCurIndex.value=$!winCurIndex ; document.form.submit();}" class="b2">$text.get("mrp.lb.save")</button></li>
	#end
	#if($globals.getMOperation().query())
	  <li><button type="button"    onClick="location.href='/UserQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>
	#end
	</ul>
</div>

<div id="listRange_id">
		
		<div class="listRange_1_space_1">
		#set($temp=1)
		#foreach($row in $moduleList)
		
		<div class="listRange_1_space_1_div_$temp#if($temp==1)#set($temp=2)#end" showTb="show_$row.getId().trim()"><span onClick="changeMenu('$row.getId().trim()')"> $row.getModelName()</span>
		</div>
		#end		
		</div>
		
		#set($display='block;')
		#foreach ($row in $moduleList)
		<div id="show_$row.getId().trim()" style="display:$display">
		#if($display=='block;')#set($display='none;')#end
			<div class="scroll_function_small_a" id="conter">
			<script type="text/javascript">
				var oDiv=document.getElementById("conter");
				var sHeight=document.body.clientHeight-38;
				oDiv.style.height=sHeight+"px";
			</script>
				<table border="0" cellpadding="0" cellspacing="0" class="listRange_list">
				<thead>
				<tr style="text-align:center;">
					<td><input id="checkall$row.getId().trim()" name="myOpertions" type="checkbox" value="$globals.get($operation,0)" style="border:0px;width:16px;" onClick="selectAllOperation('moduleOperations','checkall$row.getId().trim()','$row.getId().trim()')"/>$text.get("role.lb.allRight")</td>
					#foreach ($operation in $globals.getEnumerationItems("EOperation"))
					<td>$operation.name</td>	
					#end				
				</tr>
				</thead>
			<tbody>
				#foreach ($child in $modules.get($row))
				
				<input type="hidden" name="moduleID" value="$child.getId().trim()"/>	
				<tr style="text-align:center;">	

					<td> $child.getModelName() </td>	

					#foreach ($operation in $globals.getEnumerationItems("EOperation"))						
					<td>
						#foreach ($mop in $child.moduleoperationinfo)
						#if($mop.operationID == $operation.value )
						<input id="check$row.getId().trim()" type="checkbox" mid="$row.getId().trim()" value="$mop.id"  name="moduleOperations" #if($userModules.get($mop.id)) checked #end style="border:0px;width:16px;"/>
						#end
						#end
						&nbsp;
					</td>	
					#end
				</tr> 
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
