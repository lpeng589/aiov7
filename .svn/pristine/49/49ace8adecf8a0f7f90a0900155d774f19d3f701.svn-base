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
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>


<link rel="stylesheet" type="text/css" href="/js/flexgrid/css/flexigrid.css" media="all" />
<script type="text/javascript" src="/js/flexgrid/flexigrid.js"></script>
		
<script type="text/javascript">		
	jQuery(document).ready(function($) {
		var sHeight2=document.documentElement.clientHeight- 200;
		
		$('#pathTable').flexigrid({height:sHeight2,showToggleBtn:false});
	});
	
	function openInputTime(obj)
	{
		WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});
	}
	function submits(){
		document.form.submit();
	}
	function recoveFile(time,file){
		window.location.href='/ServiceAction.do?opType=recoveFile&createTime='+encodeURI(time)+'&fileName='+encodeURI(file)+'&logfileName='+encodeURI('$!logfileName.replace('\','\\')');
	}
</script>
</head>

<body onLoad="showtable('tblSort');showStatus();">

<form  method="post" scope="request" name="form" action="/ServiceAction.do?opType=logList">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div class="Heading">
	<div class="HeadingTitle"> 客服日志</div>
	<embed type="application/np-print" style="width:0px;height:0px;" id="plugin">
	<ul class="HeadingButton">
	#if("$!logfileName"=="")
		<li>
			<button type="submit"  class="hBtns">查询</button>
		</li>
	</ul>
	#end
</div>

<div id="listRange_id" >	
#if("$!logfileName"!="")
<input name="logfileName" type="hidden" value="$!logfileName"/>
#else
<div class="listRange_1">
	<li><span>工号：</span>
	  <input name="workNo" type="text" value="$!workNo" onkeydown="if(event.keyCode==13){submits();}" disableautocomplete="true" autocomplete="off"></li>
	<li><span>名称：</span>
	  <input name="name" type="text" value="$!name" onkeydown="if(event.keyCode==13){submits();}" disableautocomplete="true" autocomplete="off"></li>
	<li><span>公司：</span>
	  <input name="company" type="text" value="$!company" onkeydown="if(event.keyCode==13){submits();}" disableautocomplete="true" autocomplete="off"></li>
	<li><span>内容：</span>
	  <input name="content" type="text" value="$!content" onkeydown="if(event.keyCode==13){submits();}" disableautocomplete="true" autocomplete="off"></li>
	<li><span>开始日期：</span>
	  <input name="startTime" type="text" value="$!startTime" onkeydown="if(event.keyCode==13){submits();}" onclick="openInputTime(this);" disableautocomplete="true" autocomplete="off"></li>
	<li><span>结束日期：</span>
	  <input name="endTime" type="text" value="$!endTime" onkeydown="if(event.keyCode==13){submits();}" onclick="openInputTime(this);" disableautocomplete="true" autocomplete="off"></li>          
	<li><span>类型：</span>				  
	  <select name="type">
			<option value="-1" #if("$!type"=="-1") selected #end></option>
			<option value="0" #if("$!type"=="0") selected #end>数据库操作</option>
			<option value="1" #if("$!type"=="1") selected #end>更新文件</option>
			<option value="2" #if("$!type"=="2") selected #end>恢复文件</option>
			<option value="3" #if("$!type"=="3") selected #end>删除文件</option>
			<option value="4" #if("$!type"=="4") selected #end>增加文件</option>
			<option value="5" #if("$!type"=="5") selected #end>创建文件夹</option>
	  </select></li>
</div>	
#end		
		<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-152;
oDiv.style.height=sHeight+"px";
</script>		
	  
	  <table class="table table-bordered" id="pathTable" >
      <thead>
        <tr>
          <th width="50">工号</th>
          <th width="60">名字</th>
          <th width="150">公司</th>
          <th width="60">类型</th>
          <th width="120">时间</th>
          <th style="min-width:200px">内容</th>
          <th  width="50">操作</th>
        </tr>
      </thead>
      <tbody>
      #foreach($row in $!result)
        <tr>          
          
          <td>$globals.get($row,1)</td>
          <td>$globals.get($row,2)</td>
          <td>$globals.get($row,3)</td>
          <td>#if("$!globals.get($row,4)"=="0")数据库操作 
          	#elseif("$!globals.get($row,4)"=="1")更新文件
          	#elseif("$!globals.get($row,4)"=="2")恢复文件
          	#elseif("$!globals.get($row,4)"=="3")删除文件
          	#elseif("$!globals.get($row,4)"=="4")增加文件
          	#elseif("$!globals.get($row,4)"=="5")创建文件夹 #end</td> 
          <td>$globals.get($row,5)</td>
          <td style="min-width:200px">#if($globals.get($row,6).length()>350) $globals.get($row,6).substring(0,350)#else $globals.get($row,6) #end</td>
          <td>   
           #if("$!globals.get($row,4)"=="1" || "$!globals.get($row,4)"=="2" || "$!globals.get($row,4)"=="3")      
           <a style="padding-left:10px" href="javascript:recoveFile('$globals.get($row,5)','$globals.get($row,7).replace('\','\\')')">恢复</a>  
           #end        
          &nbsp;
          </td>
          
        </tr>
      #end
      </tbody>
    </table>
		</div>
</div>
</form>

</body>
</html>
