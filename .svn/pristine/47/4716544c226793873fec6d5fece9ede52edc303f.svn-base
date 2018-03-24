<!DOCTYPE html>
<html>
<head>
<title>我的工作流</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="/mobile/css/themes/default/jquery.mobile-1.4.5.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" href="/mobile/css/mWorkFlowList.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/mobile/css/scrollbar.css">
<link rel="stylesheet" type="text/css" href="/mobile/css/mobile.css">

<link rel="shortcut icon" href="/favicon.ico">
<script src="/mobile/js/jquery.min.js"></script>
<script src="/mobile/js/jquery.mobile-1.4.5.min.js"></script>
<script type="text/javascript" charset="utf-8" src="/mobile/js/iscroll.js"></script>
<script src="/mobile/js/mobileapi.min.js"></script>
<script type="text/javascript">
var pageNo = 1;
$(document).ready(function() {
	myWorkflowQuery($("#approveStatus").val(),$("#keyWord").val(),pageNo);
	startScroll(function() {
		if(totalPage==0 || totalPage>pageNo){
			pageNo++; //页数加1
		}
		myWorkflowQuery($("#approveStatus").val(),$("#keyWord").val(),pageNo);
	}); 
});


function qt(type){ 
	pageNo = 1;
	$("#myflowlist").empty();
	$("#approveStatus").val(type);
	$("#keyWord").val('');
	$("#kw").val('');
	myWorkflowQuery($("#approveStatus").val(),$("#keyWord").val(),pageNo);
}
function qk(){
	pageNo = 1;
	$("#myflowlist").empty();
	$("#pageone").trigger('create');
	$("#keyWord").val($("#kw").val());
	myWorkflowQuery($("#approveStatus").val(),$("#keyWord").val(),pageNo);
	$('.ui-dialog').dialog('close'); //关闭弹出窗	
}

function tourl(tableName,billId){
	ajaxUpdateHtml("/MobileAction.do?operation=82&tableName="+tableName+"&keyId="+billId,"pageUpdate",function(){
		jQuery.mobile.changePage($("#pageUpdate"),{ transition: "flip"} );
		$("#pageUpdate").trigger('create');
		showWeiXinPic("pageUpdate"); //用微信显示表框中图片
		if(typeof( update ) == 'function' ){ //有个性化自定义，就会有update方法
			update(billId);
		}
	});
}

</script>
</head>
<body>
<form method="post" name="form" id="form" action="/MobileQueryAction.do">
<input type="hidden" id="approveStatus" name="approveStatus" value="transing" />
<input type="hidden" id="keyWord" name="keyWord" value="" />
</form>
<div data-role="page" id="pageone">
  <div data-role="header" data-position="fixed">
    <a href="javascript:window.location.href='/mobile/mBody.jsp'" data-icon="home" data-role="button">首页</a>
    <h1>我的工作流</h1>
    <a href="#conditon" data-role="button" data-rel="dialog" class="ui-btn-right" data-icon="search">搜索</a>
    <div data-role="navbar">
      <ul>
        <li><a href="javascript:qt('transing')"  data-icon="user" class="ui-btn-active ui-state-persist   ">待审核</a></li>
        <li><a href="javascript:qt('consignation')" data-icon="heart"   ">委托我</a></li>
        <li><a href="javascript:qt('transing2')" data-icon="calendar" ">办理中</a></li>
        <li><a href="javascript:qt('finish')" data-icon="check" >已办结</a></li> 
      </ul>
    </div>
  </div>

  <div id="wrapper" style="top: 102px;" data-role="content">
	<div id="scroller" >
	    <ol data-role="listview" class="mlist" style="margin:0" id="myflowlist">
		</ol>
		<div id="pullUp">
	            <span class="pullUpIcon"></span><span class="pullUpLabel">上拉显示更多...</span>
	    </div>
	</div>
  </div>
</div> 
<div data-role="page" id="conditon">
  <div data-role="header">
    <h1>请输入查询条件</h1>
  </div>

  <div data-role="content">
    <label for="fullname">关键字:</label>
	<input type="search" id="kw" value="$!workFlowForm.keyWord">
  </div>

  <div data-role="footer" class="ui-btn">
  <div data-role="controlgroup" data-type="horizontal">
    <a href="javascript:qk()" data-role="button" data-icon="search">查询</a>
  </div>  
  </div>
</div> 


<div class="pull-demo-page"  data-role="page" id="pageUpdate">
  
</div> <!-- pageUpdate 添加修改界面 -->

</body>
</html>
			