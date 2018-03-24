<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>日志管理</title>
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
				if(obj_id=="div_login"){
					//登陆日志地址不同
					url = "/LogManageQueryAction.do";
				}else{
					url = "/LogManageAction.do";
				}
				url += "?operation=4&opType="+vm;
				$("#"+obj_id+"_iframe").attr("src",url);	
			}
			$(".t_n").find("li").removeClass("sel");
			$(obj).addClass("sel");
		}
	}
</script>
</head>
<body>
  <!-- <p class="top_tp">日志管理</p> -->
  <!-- 头部标签 Start -->
  <ul class="t_n" style="width:1100px">
    <li class="t_li sel" id="div_operation" onclick="changeoption(this)" vm="operateLog">
        	操作日志
    </li>
    <li class="t_li" id="div_login" onclick="changeoption(this)" vm="loginLog">
       		登陆日志
    </li>
    <li class="t_li" id="div_run" onclick="changeoption(this)" vm="runLog">
			运行日志
    </li>
  </ul>
  <!-- 头部标签 End -->
  <div class="m_wp"  style="width:1100px">
	<div class="main_wp" id="data_list_id">
  	<script type="text/javascript"> 
  		
	</script>
		<iframe id="div_operation_iframe" frameborder=false src="/LogManageAction.do?operation=4&opType=operateLog" name="div_operation_iframe" style="display: block;" scrolling="no" width="100%" ></iframe>
		<iframe id="div_login_iframe" frameborder=false src="" name="div_login_iframe" style="display: none" scrolling="no" width="100%" ></iframe>
		<iframe id="div_run_iframe" frameborder=false src="" name="div_run_iframe" style="display: none" scrolling="no" width="100%"></iframe>
	</div>
  </div>
  <script type="text/javascript"> 
  	$(function(){
  		var sH = document.documentElement.clientHeight-60;
		$("#data_list_id").height(sH);
		$("#div_operation_iframe").css("height",sH);
		$("#div_login_iframe").css("height",sH);
		$("#div_run_iframe").css("height",sH);
  	});
  </script>
</body>
</html>
