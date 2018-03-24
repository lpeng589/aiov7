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
		var sHeight2=document.documentElement.clientHeight- 120;
		
		$('#pathTable').flexigrid({height:sHeight2,showToggleBtn:false});
	});
	


	function updateFile(file){
		asyncbox.open({
	　　 	url : '/ServiceAction.do?opType=fileUpload&fileName='+encodeURI(file),
			title :'上传文件',
			id:'ServiceUpload',
	　　　	width : 400,
	　　　	height : 150,
			btnsbar : [{ text    : '上传', action  : 'upload' },{ text    : '取消', action  : 'cancel' }],
　　　		callback : function(action,opener){
　　　　　		if(action == 'upload'){
　　　　　　　			opener.beforeSubmit();
					return false;
　　　　　		}
　　　		}
	
	　	});		
	}
	function addDir(){
		asyncbox.open({
	　　 	html : '<span><input style="margin:20px;width:220px" type=text name=newDir id=newDir value=""/></span>',
			title :'新建文件夹',
			id:'ServiceUpload',
	　　　	width : 300,
	　　　	height : 130,
			btnsbar : [{ text    : '确定', action  : 'ok' },{ text    : '取消', action  : 'cancel' }],
　　　		callback : function(action,opener){
　　　　　		if(action == 'ok'){
　　　　　　　			jQuery.get("/ServiceAction.do?opType=addPath&path="+encodeURI('$!path.replace('\','\\')')+'&newDir='+encodeURI($("#newDir").val()),function(data){
						alert(data);
						toDir('$!path.replace('\','\\')');
					});
　　　　　		}
　　　		}
	
	　	});		
	}
	function addFile(file){
		asyncbox.open({
	　　 	url : '/ServiceAction.do?opType=fileUpload&path='+encodeURI(file),
			title :'上传文件',
			id:'ServiceUpload',
	　　　	width : 400,
	　　　	height : 150,
			btnsbar : [{ text    : '上传', action  : 'upload' },{ text    : '取消', action  : 'cancel' }],
　　　		callback : function(action,opener){
　　　　　		if(action == 'upload'){
　　　　　　　			opener.beforeSubmit();
					return false;
　　　　　		}
　　　		}
	
	　	});		
	}
	
	function logList(file){
		asyncbox.open({
	　　 	url : '/ServiceAction.do?opType=logList&logfileName='+encodeURI(file),
			title :'文件更新历史',
			id:'logListID',
	　　　	width : 900,
	　　　	height : 500,
			btnsbar : [{ text    : '关闭', action  : 'cancel' }]　　　			
	　	});		
	}
	
	
	var curSortName = "$!sortName";
	var curSortDir = $!sortDir;
	function sort(sortName){
		if(sortName == curSortName){
			window.location.href='/ServiceAction.do?opType=file&sortName='+sortName+'&sortDir='+(!curSortDir);
		}else{
			window.location.href='/ServiceAction.do?opType=file&sortName='+sortName+'&sortDir=true';
		}
	}
	function toDir(path){ 
		window.location.href='/ServiceAction.do?opType=file&sortName='+curSortName+'&sortDir='+curSortDir+'&path='+encodeURI(path);
	}
	function deleteFile(fileName,path){
		window.location.href='/ServiceAction.do?opType=deleteFile&fileName='+encodeURI(fileName)+'&sortName='+curSortName+'&sortDir='+curSortDir+'&path='+encodeURI(path);
	}
	
	#if("$!msg"!="")
		alert('$!msg');
	#end
</script>
</head>

<body onLoad="showtable('tblSort');showStatus();">

<form  method="post" scope="request" name="form" action="/ServiceAction.do?opType=file&exec=true&sortName=$!sortName&sortDir=$!sortDir">
 <input type="hidden" name="fileName" value="">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<div class="Heading">
	<div class="HeadingTitle">
	<li><button type="button"  onClick="toDir('$!path.replace('\','\\')')" class="hBtns">刷新</button>
	<button type="button"  onClick="addFile('$!path.replace('\','\\')')" class="hBtns">添加文件</button>
	<button type="button"  onClick="addDir('$!path.replace('\','\\')')" class="hBtns">添加文件夹</button></li>
</div>
	
</div>
<div id="listRange_id" >		
		<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-52;
oDiv.style.height=sHeight+"px";
</script>		
	  当前路径&nbsp;&nbsp;&nbsp;&nbsp;$!curPath
	  <table class="table table-bordered" id="pathTable" >
      <thead>
        <tr>
          <th width="200" onclick="sort('name')">
          #if("$!sortName"=="name")#if($!sortDir) <img src="/style/images/sort-asc.png"/> #else <img src="/style/images/sort-desc.png"/>#end #end 名称</th>
          <th width="150" onclick="sort('update')">
          #if("$!sortName"=="update")#if($!sortDir) <img src="/style/images/sort-asc.png"/> #else <img src="/style/images/sort-desc.png"/>#end #end 修改日期</th>
          <th  onclick="sort('type')">
          #if("$!sortName"=="type")#if($!sortDir) <img src="/style/images/sort-asc.png"/> #else <img src="/style/images/sort-desc.png"/>#end #end 类型</th>
          <th  onclick="sort('size')">
          #if("$!sortName"=="size")#if($!sortDir) <img src="/style/images/sort-asc.png"/> #else <img src="/style/images/sort-desc.png"/>#end #end 大小</th>
          <th width="200" >操作</th>
        </tr>
      </thead>
      <tbody>
      #foreach($row in $!fileList)
        <tr>          
          #if("$globals.get($row,4)" == "PATH")
          <td onclick="toDir('$globals.get($row,5).replace('\','\\')')";>
          <img src="/style/themes/default/images/tree_folder.gif"/> 
          #else
          <td>
          <img src="/style/themes/default/images/tree_file.gif"/> 
          #end
          $globals.get($row,0)</td>
          <td>$globals.get($row,1)</td>
          <td>$globals.get($row,2)</td>
          <td>$globals.get($row,3)</td>
          <td>
          #if("$globals.get($row,4)" != "PATH")
           <a style="padding-left:10px" href="/ReadFile?tempFile=serviceDown&fileName=$globals.get($row,5)">下载</a>
           <a style="padding-left:10px" href="javascript:updateFile('$globals.get($row,5).replace('\','\\')')">更新</a>
           <a style="padding-left:10px" href="javascript:logList('$globals.get($row,5).replace('\','\\')')">更新历史</a>
          #end 
          <a style="padding-left:10px" href="javascript:deleteFile('$globals.get($row,5).replace('\','\\')','$!path.replace('\','\\')')">删除</a>
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
