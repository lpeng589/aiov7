<!DOCTYPE html>
<html>
<head>
<title>易站通客户</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="/mobile/css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/scrollbar.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/mobile.css">

<link rel="shortcut icon" href="/favicon.ico">
<script src="/js/jquery.js"></script>
<script src="/mobile/js/jquery.mobile-1.4.5.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/mobile/js/iscroll.js"></script>
<script src="/mobile/js/mobileapi.min.js?111=1"></script>
<script type="text/javascript">  
//系统初始化时，查询报表
var hasAgtName = false;
var pageSize = 10;
var pageNo = 1;

$(document).ready(function() { //进入列表后马上执行查询
	reportQuery("tzclient",true,{pageSize:pageSize,pageNo:pageNo},function(conditions,cols,rows){
		conds = conditions;
		
		for(i=0;i<conds.length;i++){
			if(conds[i][0]=='agentName'){
				hasAgtName = true; //代理商这个字段有权限控制，没权限的人要隐藏 这里如果查询条件中没有代理商字段表示没权限，从而控制本界面中是否显示代理字段
			}
			$("#Q"+conds[i][0]).val(conds[i][1]);
		}		
		if(hasAgtName){ //没有代理商显示权限要隐藏某些字段
			$("#QgroupName").remove();
			$("label[for=QgroupName]").remove();
			$("#QempFullName").remove();
			$("label[for=QempFullName]").remove();
		}else{
			$("#QagentName").remove();
			$("label[for=QagentName]").remove();
		}
		//显示列表
		addList(cols,rows);
		
		startScroll(function() {
			if(totalPage==0 || totalPage>pageNo){
				pageNo++; //页数加1
			}
			query(false);
		}); 
	});

	canAdd("tzclient","","",function(data){ //判断系统是否有添加权限,没有要隐藏
		if(data=="true"){
			$("#addBt").show();
		}else{
			$("#addBt").hide();
		}
	});
	
});
//报表查询
function query(isClear){
	if(isClear){ //如果是从查询条件界面点查询要先清空列表中数据，滚动加载的不用清空数据
		$("#listDiv").empty();
		pageNo = 1;
	}
	//查询的条件参数
	cond = {clientStatus:$("#QclientStatus").val(),
			agentName:$("#QagentName").val(),
			clientName:$("#QclientName").val(),
			groupName:$("#QgroupName").val(),
			empFullName:$("#QempFullName").val(),
			pageSize:pageSize,
			pageNo:pageNo
			 };		 
	//执行报表查询		 
	reportQuery("tzclient",true,cond,function(conditions,cols,rows){
		addList(cols,rows);
		jQuery.mobile.changePage($("#pageone"),{ transition: "fade"} );		//显示主界面
	});
}
//查询的回调函数
function addList(cols,rows){	
	for(i = 0 ;i<rows.length;i++){ 
		rowDis = rows[i];
		if($("li[keyId="+rowDis.keyId+"]").size() > 0){ //从服务返回的数据可能是本地已经显示的数据，这里判断如果数据存在则再显示
			continue;
		}
		//把数据显示在列表中
		$("#listDiv").append( 
			  '<li keyId="'+rowDis.keyId+'"><a href="javascript:showUpdate(\''+rowDis.keyId+'\')">'+
		      '<h2> '+rowDis.clientName+'</h2>'+
		      (hasAgtName?'<p> '+rowDis.agentName+'</p>':'')+
		      '<p>'+rowDis.empFullName+', '+rowDis.clientStatus+', '+rowDis.protectEDate+'</p>'+
		      '<p>'+rowDis.District+''+
		      ', '+rowDis.mobile+'</p>'+
		      '</a></li>'
		 ); 
	 }
	 $("#listDiv").listview('refresh');
	 $("#pageone").trigger('create');
}
//显示添加界面
function showAdd(){
	//界面只需调入一次，如果存在无需重复调入
	if($("#pageUpdate").find("div").size() == 0){
		//通过ajax把界面调入
		ajaxUpdateHtml("tzclient.jsp","pageUpdate",function(){
			add();
		});
	}else{
		add();
	}
}
//显示修改界面
function showUpdate(keyId){
	//界面只需调入一次，如果存在无需重复调入
	if($("#pageUpdate").find("div").size() == 0){
		//通过ajax把界面调入
		ajaxUpdateHtml("tzclient.jsp","pageUpdate",function(){
			update(keyId);
		});
	}else{
		update(keyId);
	}
}


</script>
</head>
<body>
<div class="pull-demo-page"  data-role="page" id="pageone">
  <div data-role="header" data-position="fixed">
    <div data-role="controlgroup" data-type="horizontal" style="padding-left: 5px;">
	    <a href="javascript:window.location.href='/MobileQueryAction.do?operation=21'" data-icon="home" data-role="button">返回首页</a>
	    <a href="javascript:showAdd()" id="addBt" data-role="button" data-icon="plus">添加</a>
	    <a href="#conditon" data-role="button" data-rel="dialog" data-icon="search">条件搜索</a>
	</div>

  </div>

  <div data-iscroll="" data-role="content" id="wrapper">
	 <div id="scroller">	  	
	  	<ol data-role="listview" id="listDiv"> 
	  		
	  	</ol>
	  	<div id="pullUp">
	            <span class="pullUpIcon"></span><span class="pullUpLabel">上拉显示更多...</span>
	    </div>
	</div>
  </div>
  
</div> <!-- pageone 列表 -->



<div data-role="page" id="conditon">
  <div data-role="header">
    <h1>请输入查询条件</h1>
  </div>
  <div data-role="content">
	
	<label for="QagentName">代理商</label>
	<input type="text" id="QagentName" value="">
	<label for="QclientName">客户全称</label>
	<input type="text" id="QclientName" value="">
	<label for="QclientName">客户状态</label>
	<select id="QclientStatus">
		<option value="">全部</option>
		<option value="0">正在培养</option>
		<option value="1">放弃客户</option>
		<option value="2">不存在客户</option>
		<option value="100">已成交客户</option>
	</select>
	
	<label for="QgroupName">小组</label>
	<input type="text" id="QgroupName" value="">
	<label for="QempFullName">所有人</label>
	<input type="text" id="QempFullName" value="">
  </div>

  <div data-role="footer" class="ui-btn">
  <div data-role="controlgroup" data-type="horizontal">
    <a href="javascript:query(true)" data-role="button" data-icon="search">查询</a>
    <a href="javascript:$('input').val('')" data-role="button" data-icon="minus">清空</a>
  </div>  
  </div>
</div> <!-- conditon 弹窗条件  -->


<div class="pull-demo-page"  data-role="page" id="pageUpdate">
  
</div> <!-- pageUpdate 添加修改界面  这里用于显示修改界面，执行ajax从tzclient.jsp中截取本div中的html代码贴在这里 -->

</body>
</html>
			