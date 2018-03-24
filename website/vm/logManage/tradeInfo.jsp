<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>日志跟踪</title>
<link rel="stylesheet" href="/style/css/base_button.css" type="text/css" />
<link rel="stylesheet" href="/style/css/log.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/sharingStyle.css" type="text/css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<script type="text/javascript">
	
	function dotrade(){
		var data = jQuery.ajax({
		 	url: "/LogManageAction.do?operation=4&opType=tradeAjax&path=$!path&fileName=$!fileName",
		 	async: false   
		}).responseText;
		
		if(data != null&&data != undefined){
			$("#tradeInfo").val(data);		
			
			$("#tradeInfo").focus();
		    var len = $("#tradeInfo").val().length;
		    if (document.selection) {
		        var sel = $("#tradeInfo")[0].createTextRange();
		        sel.moveStart('character',len);
		        sel.collapse();
		        sel.select();
		    } else if (typeof $("#tradeInfo")[0].selectionStart == 'number' && typeof $("#tradeInfo")[0].selectionEnd == 'number') {
		        $("#tradeInfo")[0].selectionStart = $("#tradeInfo")[0].selectionEnd = len;
		    }	
		    $("#tradeInfo")[0].scrollTop=$("#tradeInfo")[0].scrollHeight	
		}else{
		}
		//setTimeout('dotrade()',1000);		
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
<body onload="dotrade()">
	<input type="button" style="float:right;margin:3px 50px 3px" value="刷新" onclick="dotrade()"/>
	<textarea id="tradeInfo" style="width: 99%;height: 98%;"></textarea>
  	<script type="text/javascript">		
		var sHeight2=document.documentElement.clientHeight- 40;		
		$("#tradeInfo").css("height",sHeight2+"px");
	</script>	
</body>
</html>
