<!DOCTYPE html>
<html>
<head>
<title>科荣AIO首页</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="/mobile/css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/scrollbar.css">

<link rel="shortcut icon" href="/favicon.ico">
<script src="/mobile/js/jquery.min.js"></script>
<script src="/mobile/js/jquery.mobile-1.4.5.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/mobile/js/iscroll.js"></script>
<script src="/mobile/js/mobileapi.min.js"></script>
<script type="text/javascript">  
//系统初始化时，查询报表
var pageSize = 10;
var pageNo = 1;

$(document).ready(function() {
	reportQuery("tzMBReportblankCity",false,{pageSize:pageSize,pageNo:pageNo},function(conditions,cols,rows){		
		addList(cols,rows);
		
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
	if(isClear){
		$("#listDiv").empty();
		pageNo = 1;
	}
	cond = {DistrictFullName:$("#DistrictFullName").val(),
			pageSize:pageSize,
			pageNo:pageNo
			 };
	reportQuery("tzMBReportblankCity",false,cond,function(conditions,cols,rows){
		addList(cols,rows);
		jQuery.mobile.changePage($("#pageone"),{ transition: "fade"} );		

	});
}

function addList(cols,rows){	
	for(i = 0 ;i<rows.length;i++){
		rowDis = rows[i];
		found = false;
		$("li").each(function(){
			if($(this).text().match(rowDis.DistrictFullName)){
				found = true;
			}
		}); 
		if(found){
			continue;
		}
		
		$("#listDiv").append( 
			  '<li ><span >'+
		      ''+rowDis.DistrictFullName+'</span><span  style="float:right">'+	rowDis.bhstauts+ 	      
		      '</span></li>'
		 ); 
	 }
	 $("#listDiv").listview('refresh');
	 $("#pageone").trigger('create');
}

</script>
<style type="text/css">
li span{display:inline-block}
</style>
</head>
<body>
<div class="pull-demo-page"  data-role="page" id="pageone">
  <div data-role="header" data-position="fixed">
    <a href="javascript:window.location.href='/mobile/mBody.jsp'" data-icon="home" data-role="button">首页</a>
    <h1>区域限制查询 <span id="testid"></span> </h1>
      <div data-role="fieldcontain" style="padding:0px 10px;border: none;">
  	 		<input type="search" id="DistrictFullName" onchange="query(true)" value="">
  	 	</div>
  </div>

  <div data-iscroll="" data-role="content" id="wrapper" style="top:92px">

	 <div id="scroller">	
		
	  	<ol data-role="listview" id="listDiv" data-inset="true" >
	  		
	  	</ol>
	  	<p id="pullUp">
			<span class="pullUpIcon"></span><span class="pullUpLabel">上拉加载更多...</span>
		</p>
	</div>
  </div>
  
</div> 

</body>
</html>
			