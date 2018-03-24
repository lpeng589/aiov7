<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>日志管理</title>
<link rel="stylesheet" href="/style/css/base_button.css" type="text/css" />
<link rel="stylesheet" href="/style/css/log.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/sharingStyle.css" type="text/css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script type="text/javascript">
	
	/* 删除30天日志 */
	function deleteLog(){
		asyncbox.confirm('你确定删除30天前的运行日志?','提示',function(action){
		　　　if(action == 'ok'){
			　　　	$("#operation").val('3');
				$("#opType").val('deleteLogFile');
				form.submit();
		　　　}
		　});
	}
	
	/* 刷新 */
	function showData(){
		window.location.href="/LogManageAction.do?operation=4&opType=runLog";
	}
	
	/* 修改级别 */
	function updatelevel(){
		var rootLogger = "";
		var myLog = "";
		if($("#debug").is(":checked")==true){
			rootLogger = "debug";
			myLog = "debug";
		}else{
			rootLogger = "warn";
			myLog = "info";
		}
		var url = "/LogManageAction.do?opType=setLevel&rootLogger="+rootLogger+"&myLog="+myLog;
		jQuery.ajax({type:"POST",url:url,success:function(msg){alert(msg);}});
	}
</script>
<style type="text/css">
	.moveLog{margin: 20px 5px 0px 120px;height:400px;overflow:auto;position: relative;}
	.moveLog>ul{overflow:hidden;}
	.moveLog li{float:left;display:inline-block;height:31px;line-height:31px;text-align:center;}
	.items{margin:0 0 0 150px;}
	.btn{margin-left: 20px;}
</style>

</head>
<body>
	<iframe name="formFrame" style="display:none"></iframe>
	<form action="/LogManageAction.do" name="form" method="post" id="form" target="">
	<input type="hidden" id="operation" name="operation" value="4"/>
	<input type="hidden" id="opType" name="opType" value="runLog"/>
  	<div class="search">
    	<div class="items">
    		<input name="debug" id="debug" value="1" type="checkbox" #if("$!level"=="debug" || "$!level"=="DEBUG") checked #end onclick="updatelevel()"/><label for="debug">打开调试模式</label>
	    </div>
      <div class="items">
      	<div class="btn btn-mini" onclick="showData()">刷新</div>
      	#if($LoginBean.operationMap.get("/LogManageAction.do").delete())
        <div class="btn btn-inverse btn-mini" onclick="deleteLog()">删除30天前运行日志</div>
       	#end
      </div>
    </div>
    <div class="moveLog">
    	#set($count = 1)
		#foreach($!file in $!fileList)
		<ul>
			<li style="width:50px;">$count.</li>
			<li>$!file&nbsp;&nbsp;&nbsp;&nbsp;<a href="/ReadFile?tempFile=path&albumType=tree&path=$!logUrl/&fileName=$!file">下载</a>
			&nbsp;&nbsp;&nbsp;&nbsp;<a href="/LogManageAction.do?operation=4&opType=tradeLog&path=$!logUrl/&fileName=$!file" target="_blank">查看</a></li>
		</ul>
		#set($count = $count+1)
		#end
		#if($loginlogList.size()==0)
			<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">暂无运行日志信息</div>
		#end
	</div>
    </form>
</body>
</html>
