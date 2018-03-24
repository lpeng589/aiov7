<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" type="" >
var items=new Array();
var itemCount=0;
items[0]="$tableName";
#if($mainTabKeyId != "")
 #foreach($row in $childTabList)
   itemCount++;
   items[itemCount]="$!globals.get($row,0)";
 #end
#else
#foreach($row in $childTabList)
   itemCount++;
   items[itemCount]="$row.tableName";
 #end
#end

 function changeDiv(divId,keyId)
 {
   document.getElementById("tabDiv_"+divId).style.display = "block";
   document.getElementById("spanDiv_"+divId).className="listRange_1_space_1_div_1";
   if(""==document.getElementById("iframe_"+divId).src||null==document.getElementById("iframe_"+divId).src){
     var f_brother="$mainTabKeyId";
     var operation="$operation";
     if(keyId==""){//ûID,ӵҳ
       operation="$globals.getOP("OP_ADD_PREPARE")";
     }
     document.getElementById("iframe_"+divId).src="/UserFunctionAction.do?tableName="+divId+"&keyId="+keyId+"&f_brother="+f_brother+"&operation="+operation+"&notCheckTab=Y";
   }
   for(var i=0;i<items.length;i++){
     if(items[i]!=divId ){
       document.getElementById("spanDiv_"+items[i]).className="listRange_1_space_1_div_2";
       document.getElementById("tabDiv_"+items[i]).style.display = "none";

     }
   }
}

function reinitIfrmae(){
  try{
    for(var i=0;i<items.length;i++){
      var iframe=document.getElementById("iframe_"+items[i]);
      var height1=iframe.contentWindow.document.body.scrollHeight;
      var height2=iframe.contentWindow.document.documentElement.scrollHeight;
      iframe.height=Math.max(height1,height2);
    }
  }catch(ex){
  }
}
window.setInterval("reinitIfrmae()",200);

</script>
</head>

<body >

<div id="spanDiv_$tableName" class="listRange_1_space_1_div_1">
<span  onClick="changeDiv('$tableName','$mainTabKeyId')">$globals.getTableDisplay($tableName)</span>
</div>

#if($mainTabKeyId != "")
#foreach($row in $childTabList)
<div id="spanDiv_$!globals.get($row,0)" class="listRange_1_space_1_div_2">
<span onClick="changeDiv('$!globals.get($row,0)','$!globals.get($row,1)')">$globals.getTableDisplay($!globals.get($row,0))</span>
</div>
#end
#else
#foreach($row in $childTabList)
<div id="spanDiv_$row.tableName" class="listRange_1_space_1_div_2">
<span onClick="changeDiv('$row.tableName','')">$globals.getTableDisplay($row.tableName)</span>
</div>
#end
#end
<div  id="tabDiv_$tableName"   >
<iframe frameborder="no" border="0" src="/UserFunctionAction.do?tableName=$tableName&keyId=$mainTabKeyId&f_brother=$mainTabKeyId&operation=$operation&notCheckTab=Y" name="iframe_$tableName"  id="iframe_$tableName"  height="500"  width="100%">
</iframe>
</div>

#if($mainTabKeyId != "")
#foreach($row in $childTabList)
<div  id="tabDiv_$!globals.get($row,0)"   style="display:none" >
<iframe frameborder="no" border="0" src="" name="iframe_$!globals.get($row,0)"  id="iframe_$!globals.get($row,0)"  width="100%" height="500" >
</iframe>
</div>
#end
#else
#foreach($row in $childTabList)
<div  id="tabDiv_$row.tableName"   style="display:none" >
<iframe frameborder="no" border="0" src="" name="iframe_$row.tableName"  id="iframe_$row.tableName"  width="100%" height="500" >
</iframe>
</div>
#end
#end

</body>
</html>
