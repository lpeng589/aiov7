<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>客服数据库操作</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script> 
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<link rel="stylesheet" type="text/css" href="/js/flexgrid/css/flexigrid.css" media="all" />
<script type="text/javascript" src="/js/flexgrid/flexigrid.js"></script>

<script type="text/javascript">
	
    jQuery(document).ready(function($) {
		$('#mytable').flexigrid({height:'auto',showToggleBtn:false});
	});
	
	var isMouseDown = false; 
	var mouseY = 0;
	var curHeight=0;
	
	function mosDown(){
		isMouseDown = true;
		mouseY = window.event.y;
		curHeight = $("#sqlTr").height();
	}
	function mosUp(){
		isMouseDown = false;
	}
	function mosMove(){
		if(isMouseDown){
			$("#sqlTr").height(curHeight+(window.event.y-mouseY));
		}
	}
	
	function doExec(){
		if($("#sql").val()==""){
			alert("执行语句不能为空");
			$("#sql").focus();
			return ;
		}
		document.form.submit();
	}
	

</script>
</head>

<body onLoad="showtable('tblSort');showStatus();">
<form  method="post" scope="request" name="form" action="/ServiceAction.do">
 <input type="hidden" name="opType" value="database">
 <input type="hidden" name="exec" value="true">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div class="Heading">
	<div class="HeadingTitle"><li><button type="button"  onClick="doExec()" class="hBtns">执行</button></li></div>
	
</div>
<div id="listRange_id" onmouseup="mosUp()" onmousemove="mosMove()">		
		<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-52;
oDiv.style.height=sHeight+"px";
</script>		
	<table width="100%" border=0  height="200" >
		<tr style="height:300px;" id="sqlTr"><td style="border:1px solid #837B83;">			
				<textarea  name="sql" id = "sql" style="border: none;width:98%;height:95%">$!sql</textarea>			
		</td></tr>
		<tr style="height:8px;cursor:row-resize" onmousedown="mosDown()" ><td></td></tr>
		<tr  ><td style="border:1px solid #837B83; vertical-align: top;">
		#if("$!msg" != "")
			$!msg
		#end
	#if("$!resultTitle" != "")	
	  <table id="mytable">
      <thead>
        <tr>
          <th width="30"  >#</th>
          #foreach($col in $!resultTitle)
          <th >$col</th>
          #end
        </tr>
      </thead>
      <tbody>
      #foreach($row in $!result)
        <tr>
          <td>$velocityCount</td>
          #foreach($col in $!row)
          <td>$!col</td>
		  #end
        </tr>
      #end
      </tbody>
    </table>
    #if($!result.size() >1000) 查询结果超过1000行(最多显示行数1000)，请缩小查询条件， #end
    #end
		</td></tr>	
	</table>	
		</div>
</div>
</form>

</body>
</html>
