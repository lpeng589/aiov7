<!DOCTYPE html>
<html>
<head>
<title>推荐我</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="/mobile/css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/scrollbar.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/mobile.css">

<link rel="shortcut icon" href="/favicon.ico">
<script src="/js/jquery.js"></script>
<script src="/mobile/js/jquery.mobile-1.4.5.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/mobile/js/iscroll.js"></script>
<script src="/mobile/js/mobileapi.min.js"></script>
<script type="text/javascript">  
//系统初始化时，查询报表
var pageSize = 10;
var pageNo = 1;

$(document).ready(function() { //进入列表后马上执行查询
	reportQuery("tzhlclient",true,{pageSize:pageSize,pageNo:pageNo},function(conditions,cols,rows){
		conds = conditions;
		
		for(i=0;i<conds.length;i++){
			$("#Q"+conds[i][0]).val(conds[i][1]);
		}		
		//显示列表
		addList(cols,rows);
		//初始化滚动翻页
		startScroll(function() {
			if(totalPage==0 || totalPage>pageNo){
				pageNo++; //页数加1
			}
			query(false);
		}); 
	});
	
});
//报表查询
function query(isClear){
	if(isClear){ //如果是从查询条件界面点查询要先清空列表中数据，滚动加载的不用清空数据
		$("#listDiv").empty();
		pageNo = 1;
	}
	//查询的条件参数
	cond = {statusId:$("#QclientStatus").val(),
			clientName:$("#QclientName").val(),
			recommendEmp:$("#QrecommendEmp").val(),
			pageSize:pageSize,
			pageNo:pageNo
			 };		 
	//执行报表查询		 
	reportQuery("tzhlclient",true,cond,function(conditions,cols,rows){
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
		      '<p>联系人：'+rowDis.connect+'&nbsp;&nbsp;电话：'+rowDis.tel+'</p>'+
			  '<p>推荐人：'+rowDis.recommendEmp+'&nbsp;&nbsp;状态：'+rowDis.status+'</p>'+
		      '</a></li>'
		 ); 
	 }
	 $("#listDiv").listview('refresh');
	 $("#pageone").trigger('create');
}
//显示修改界面
function showUpdate(keyId){
	//界面只需调入一次，如果存在无需重复调入
	if($("#pageUpdate").find("div").size() == 0){
		//通过ajax把界面调入
		ajaxUpdateHtml("tzhlclient.jsp","pageUpdate",function(){
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
	    <a href="#conditon" data-role="button" data-rel="dialog" data-icon="search">条件搜索</a>
	</div>

  </div>

  <div data-iscroll="" data-role="content" id="wrapper">
	 <div id="scroller">	  	
	  	<ol data-role="listview" id="listDiv"> 
	  		
	  	</ol>
	  	<p id="pullUp"><!-- 滚动翻页按扭 -->
			<span class="pullUpIcon"></span><span class="pullUpLabel">上拉加载更多...</span>
		</p>
	</div>
  </div>
  
</div> <!-- pageone 列表 -->

<div data-role="page" id="conditon">
  <div data-role="header">
    <h1>请输入查询条件</h1>
  </div>
  <div data-role="content">
	
	<label for="QclientName">客户全称</label>
	<input type="text" id="QclientName" value="">
	<label for="QclientStatus">状态</label>
	<select id="QclientStatus">
		<option value="">全部</option>
		<option value="0">待接收</option>
		<option value="1">拒绝</option>
		<option value="2">跟进中</option>
		<option value="3">已提成</option>
	</select>
	<label for="QrecommendEmp">推荐人</label>
	<input type="text" id="QrecommendEmp" value="">
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
			