<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/workflow.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">



</script>
<style type="text/css">

.data_list .dataListUL{margin:20px 5px 0px 5px;line-height:33px;border: 1px solid #999;min-height:67px}
.data_list .dataListUL li{ border-bottom: 1px solid #999;  }
.data_list .dataListUL .titleLi{ background:url(/style1/images/workflow/data_list_head_bg.gif); height:33px;padding-left:10px }
.data_list .dataListUL input,.data_list .dataListUL select{width: 117px;margin-left: 5px;}
.data_list .dataListUL label{width: 78px;margin-left: 5px;display: inline-block;}
.data_list table{width:780px;}
.tstBtn{background-image: url(/style/images/client/icon16.png);background-position: -32px 0;width: 16px;height: 16px;position: absolute;cursor: pointer;right: 1px;top: 8px;}

.focusRow{background-color:#9AF850;}
</style>
</head>
<body >
	<form method="post" scope="request" name="form" action="/ClientSettingAction.do?operation=2&updType=fieldsMtain" class="mytable" >
	<input type="hidden" name="isHasFlag" id="isHasFlag" value=""/>
		<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px; overflow-y: auto;" >
			<script type="text/javascript">
			var oDiv=document.getElementById("data_list_id");
			var sHeight=document.documentElement.clientHeight-20;
			oDiv.style.height=sHeight+"px";
			</script>
				<ul class="dataListUL">
				#foreach($row in $result)
					<li >
						<span style="margin-left: 5px;">$row</span>
					</li>
				#end
				
				</ul>			
		</div>
	</form>
</body>
</html>
