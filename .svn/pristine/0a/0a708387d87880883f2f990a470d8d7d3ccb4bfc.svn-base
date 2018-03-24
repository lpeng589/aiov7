<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">
body,div,form,p,input,h1,h2,h3,h4,h5,h6,span,img,font,ul,li,ol,a,table,thead,tboady,tr,td{margin:0;padding:0;}
body{font-size:14px;font-family:微软雅黑;/*background:#FBEAE1;*/}
li{list-style:none;}
.w_div{width:850px;margin:0 auto;overflow:hidden;color:#5D5D5C;padding:0 0 20px 0;border:1px #a1a1a1 solid;margin-top:50px;}
.w_div .b_p{text-align:right;}
.w_div .b_p .r_btn{margin:15px 30px 0 0;width:60px;height:30px;line-height:30px;background:url(/style/images/client/showTransferRsBg.jpg) repeat 0 0;border:1px #a1a1a1 solid;font-size:12px;font-family:微软雅黑;}
.w_div .s_p,.w_div .y_p{padding:15px 0 0 10px;}
.w_div .s_p span,.w_div .y_p span{color:red;border:1px #a1a1a1 dashed;padding:0 5px;}
.list_div{padding:15px 0 0 0;overflow:hidden;}
.list_div .list_p{line-height:21px;padding:0 10px 0 10px;background:#F0DECF;}
.list_div .list_u{width:830px;height:200px;overflow:auto;padding:0 10px 0 10px;}
.list_div .list_u .list_li{float:left;padding:0 5px 0 5px;display:inline-block;overflow:hidden;margin:10px 10px 0 0;border:1px dashed #a1a1a1;}
</style>

<script type="text/javascript">
$(document).ready(function(){
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
});

function back(url){

	window.location.href=url;
}
</script>
</head>
<body>
<div class="w_div">
	<p class="s_p">
  	成功转入数量：<span>$successCount</span>
  </p>
  <p class="y_p">
  	已转入数量：<span>$existCount</span>
  </p>
  <div class="list_div">
  	<p class="list_p">
    	已转入客户名称：
    </p>
    <ul class="list_u">
    	#foreach($client in $existList)
    	<li class="list_li">$velocityCount. $globals.get($client,1)</li>
		#end
    </ul>
    <p class="b_p">
  		<input class="r_btn" type="button" value="返  回" title="返回" onclick="back('$backUrl')"/>
  	</p>
  </div>
</div>
</body>
</html>
