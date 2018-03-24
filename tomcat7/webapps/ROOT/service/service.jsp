<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科荣AIO客服服务平台</title>
<link rel="stylesheet" href="/style/css/base_button.css" type="text/css" />
<link rel="stylesheet" href="/style/css/log.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
	//选项卡切换
	


	function changeoption(obj){
		var obj_id = $(obj).attr("id");
		if($("#"+obj_id+"_iframe").css("display") == "none"){
			$(".main_wp").find("iframe").each(function(){
				var iframe_id = $(this).attr("id");
				if(obj_id+'_iframe'==iframe_id){
					$(this).show();
				}else{
					$(this).hide();
				}
			});
			if($("#"+obj_id+"_iframe").attr("src")== ""){
				var url = "";
				var vm = $(obj).attr("vm");
				url = "/ServiceAction.do?opType="+vm;
				$("#"+obj_id+"_iframe").attr("src",url);	
			}
			$(".t_n").find("li").removeClass("sel");
			$(obj).addClass("sel");
		}
	}
	
	function frmem(){
		window.location.href='/ServiceAction.do?opType=refreshMem';
	}
	function restart(){
		if(confirm("重启将会中断服务2-10分钟,确定重启吗？")){
				var timestamp = Date.parse(new Date()); 
				jQuery.get("/CommonServlet?operation=setRestart&t="+timestamp,function(data){
					alert(data);
				});
		}
	}
</script>
</head>
<body>
  <!-- 头部标签 Start -->
  <ul class="t_n" style="width:500px;float: left;padding-left: 30px;">
    <li class="t_li sel" id="div_datbase" onclick="changeoption(this)" vm="database">
       		数据库操作
    </li>
    <li class="t_li" id="div_file" onclick="changeoption(this)" vm="file">
			文件操作
    </li>
    <li class="t_li" id="div_log" onclick="changeoption(this)" vm="logList">
			客服日志
    </li>
    
  </ul>
<li style="width:300px;float: left;height: 46px;vertical-align: middle;">
		<button type="button"  onClick="frmem()" class="hBtns" style="margin:15px;">刷新内存</button>
    	<button type="button"  onClick="restart()" class="hBtns" style="margin:15px;">重启系统</button>
    	<button type="button"  onClick="window.location.href='/ServiceAction.do?opType=logout'" class="hBtns" style="margin:15px;">退出客服</button> </li>
  
  <!-- 头部标签 End -->
  <div class="m_wp" style="width:95%">
	<div class="main_wp" id="data_list_id">
  	<script type="text/javascript"> 
  		
	</script>
		<iframe id="div_datbase_iframe" frameborder=false src="/ServiceAction.do?opType=database" name="div_operation_iframe" style="display: block;" scrolling="no" width="100%" ></iframe>
		<iframe id="div_file_iframe" frameborder=false src="" name="div_run_iframe" style="display: none" scrolling="no" width="100%"></iframe>
		<iframe id="div_log_iframe" frameborder=false src="" name="div_log_iframe" style="display: none" scrolling="no" width="100%"></iframe>
	</div>
  </div>
  <script type="text/javascript"> 
  	$(function(){
  		var sH = document.documentElement.clientHeight-60;
		$("#data_list_id").height(sH);
		$("#div_datbase_iframe").css("height",sH);
		$("#div_file_iframe").css("height",sH);
		$("#div_log_iframe").css("height",sH);
  	});
  </script>
</body>
</html>
