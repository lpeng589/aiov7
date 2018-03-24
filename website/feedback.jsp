<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>科荣软件产品交流中心</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<style type="text/css">
body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,select,legend,button,input,textarea,th,td {margin:0;padding:0;}
body,button,input,select,textarea {font:12px/1.5 Microsoft Yahei;color:#666;outline:0;}
img{border:0;}
em,i{font-style:normal;}
b{font-weight:normal;}
li{list-style:none;}
.form{margin:10px 0 0 30px;}
.div{padding:5px 0;}
.inp{border:1px solid #9caec6;border-radius:3px;padding:2px 5px;width:350px;}
.btn{width:165px;height:30px;border-radius:4px;font-size:16px;line-height:30px;color:#fff;border:0;font-family:宋体;margin:0 0 0 3px;float:left;cursor:pointer;text-align:center;display:inline-block;background:#428bca;}
.btn:hover{background:#3276b1;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	#set($public=$request.getParameter("responseCode"))
	window.parent.closedialog('$public'); 
});
function submitValidate(){
	var subject = jQuery("#subject").val();
	if(subject.length<4){
		jQuery("#subject").focus();
		return false;
	}
	form.submit();
}
</script>
</head>
<body>
	<form class="form" action="/UtilServlet" if="form" name="form" method="POST">
		<input type="hidden" name="operation" value="postFormula"/>
		<div class="div">
			<input class="inp" id="subject" name="subject" placeholder="标题 " /><br/>
			<span style="color:red;font-size:10px">请输入标题，不少于4个字!</span>
		</div>
		<textarea class="inp" id="postmessage" rows="5" cols="80" name="postmessage" placeholder="请详细描述您的问题反馈或者改进建议，方便我们快速的帮您解决问题。不少于10个字"></textarea>
		<div class="div">
			<input  class="inp" id="tel" name="tel" placeholder="联系方式" /><br/>
			<span style="color:red;font-size:10px">请输入您的联系方式，方便我们联系您</span>
		</div>
		<input class="btn" id="postsubmit" name="topicsubmit" type="button" value="提交" onclick="submitValidate();" />
	</form>
</body>
</html>