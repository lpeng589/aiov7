<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("role.lb.roleAdmin")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" type="text/css"/>
<script language="javascript">

function selectAllOperation(tagName,targetId,mid){
	items = document.getElementsByName(tagName);
	for(var i=0;i<items.length;i++){		
		if(items[i].mid != "undefined" && items[i].getAttribute("mid") == mid ){
			items[i].checked = document.getElementById(targetId).checked;
		}		
	}
}



function selectAllChildOperation(tagName,targetId,mod){
	items = document.getElementsByName(tagName);
	for(var i=0;i<items.length;i++){		
		if(items[i].mod != "undefined" && items[i].getAttribute("mod") == mod ){
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
     var p_id=evetParent.childNodes[i].id;
     document.getElementById(p_id.substr(2)).style.display = "none";
     event.srcElement.parentElement.className ="listRange_1_space_1_div_1";
     p_id=event.srcElement.parentElement.id;
     document.getElementById(p_id.substr(2)).style.display = "block";
    }
   }
  }
 }
 
function beforSubmit(form){   
	disableForm(form);
	return true;
}
var isAllSelectSelected = false;
function checkColumn(colId,vars){
	var flag=vars.checked;
	items = document.getElementsByTagName("INPUT");
	for(var i=0;i<items.length;i++){		
		if(items[i].type =="checkbox" && items[i].getAttribute("mcd") != undefined ){
			if(colId==items[i].getAttribute("mcd")){
				items[i].checked = flag;
			}
		}		
	}
	isAllSelectSelected = !isAllSelectSelected;
}
</script>
<style>
.adminthead {
position: relative;
top: expression(this.parentElement.parentElement.parentElement.scrollTop);
z-index:2;
}
</style>
</head>

<body>
<form  method="post" scope="request" name="myform">
<div >
<div class="modulelb">
	<fieldset style="width:100%;border:0;" >
			<div id="dispaly"  class="scroll_function_small_a" style="height: 230px;">
					<table border="0" cellpadding="0" cellspacing="0" class="listRange_list">
						<thead>
							<tr class="adminthead" style="text-align:center;">
								<td  style="width:180px;" align="left" > <input id="checkall" name="myOpertions" type="checkbox" value="$globals.get($operation,0)" mall=mall style="border:0px;width:16px;" onClick="selectAllOperation('moduleOperations','checkall','')"/>$text.get("role.lb.allRight")</td>
								#foreach ($operation in $globals.getEnumerationItems("EOperation"))
									<td  style="width:55px;"><input type="checkbox" style="border:0px;width:16px;" onClick="checkColumn('$operation.value',this)"/>$operation.name</td>	
								#end				
							</tr>
						</thead>
						<tbody>
						#set($allrow=0)
						#set($selectrow=0)
						#foreach ($child in $row)
						<input type="hidden" name="moduleID" value="$child.getId().trim()"/>	
						<tr>	
							<td style="text-align:left;"><input id="checkopall$child.getId().trim()" name="moduleOperations" mid="" type="checkbox" value="" style="border:0px;width:16px;" mall=mall  onClick="selectAllChildOperation('moduleOperations','checkopall$child.getId().trim()','$child.getId().trim()')"/> 
							#if($child.isUsed==1)
							#if($child.linkAddress.indexOf("/UserFunctionQueryAction.do?parentTableName=")>=0&&$child.isHidden==1)
							#set($index=$child.linkAddress.indexOf("="))
							#set($index=$index+1)
							#set($index2=$child.linkAddress.indexOf("&")) 
							#set($parentTableName="$child.linkAddress.substring($index,$index2)")
							$globals.getTableDisplayName("$!parentTableName")_$child.getModelDisplay().get($globals.getLocale())
							#else $child.getModelDisplay().get($globals.getLocale())
							#end
							#end</td>	
							#set($allrow =$allrow+1)
							#set($num=0)
							#set($selectnum=0)			
							#foreach ($operation in $globals.getEnumerationItems("EOperation"))						
							<td style="text-align:center;">
								
								#foreach ($mop in $child.moduleoperationinfo)
									#if($mop.operationID == $operation.value )
										#set($num=$num+1)
										<input id="check" type="checkbox" mcd="$operation.value" mid="" mod="$child.getId().trim()" value="$mop.moduleOpId" mall=mall   name="moduleOperations" #if($roleModules.get($mop.moduleOpId)) #set($selectnum=$selectnum+1) checked #end style="border:0px;width:16px;"/>
									#end
									
								#end
								&nbsp;
							</td>	
							#end
						
							<script language="javascript">								
							 var mId='checkopall$child.getId().trim()';	
							 if($num==$selectnum ){
								document.getElementById(mId).checked=true;
									#set($selectrow=$selectrow+1)
							 }						 
							</script>  		
						</tr> 
			           #end   
					</tbody>
					
					<script language="javascript">								
					 var rowId='checkall';	
					 if($allrow==$selectrow && $selectnum !=0){
						document.getElementById(rowId).checked=true;
					 }
					</script>			  
				</table>
		    </div>
		</fieldset>
		</div>
</div>
</form>
</body>
</html>
