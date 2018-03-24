<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>开账</title>
<link type="text/css" rel="stylesheet" href="/style/css/reg.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<style type="text/css">
body{font:12px/1.5 Microsoft Yahei,Arial;color:#000;background:url(/style/images/item/html_bg.jpg) repeat 0 0;margin:0;padding:0;}
.div_con{overflow-y:auto;}
.div_ts{width:70px;border:2px #5fa3e7 solid;border-radius:7px;cursor:pointer;text-align:center;}
.div_ts label{cursor:pointer;}
.d_table{width:700px;padding:80px;background:#fff;margin-top:10px;min-height:350px;position:relative;}
.b_1{left:220px}
.b_2{left:470px}
.d_table>p{text-align:center;font-size:20px;}
.d_table table{width:400px;margin-botto:30px;}
.div_ts_bcolor1{border: 2px #ff3d3d solid;}
.showW{margin:20px 0 0 0;background:#f2f2f2;text-align: left;border-radius:2px;}
.showW>div{position:relative;padding:15px 0;}
.showW>div>b{width:16px;height:16px;display:inline-block;position:absolute;background:url(/style/images/body/i_017.gif) no-repeat 0 0;top:-5px;}
.d_table .t-p{position:absolute;left:20px;top:0;font-size:16px;line-height:34px;color:#666;padding:0 0 0 35px;font-weight:bolder;}
.t-p>b{width:29px;height:29px;background:url(/style/v7/images/v7_icons.png) no-repeat -40px -2px;display:inline-block;position:absolute;left:0;top:0;}
.hBtns:hover{background: #f2f2f2;}
.hBtns{background: #FCFCFC;padding:2px 10px;border:1px #bbb solid;color:#666;border-radius:3px;cursor:pointer;font-weight:bold;margin-left:5px;}
</style>
<script type="text/javascript">
	$(document).ready(function (){
		//鼠标移动到选项时，突出
		$(".div_ts").mouseover(function(){
			var sNum = $(this).attr("show");
			$(this).addClass("div_ts_bcolor1").parent("td").siblings("td").find("div.div_ts").removeClass("div_ts_bcolor1");
			$("."+sNum).show().siblings("div").hide();
		})
		
		showPeriod();
	});
	
	
	/* 显示设置开账期间 */
	function showPeriod(){
		$("div[show=accperiod]").addClass("div_ts_bcolor1").parent("td").siblings("td").find("div.div_ts").removeClass("div_ts_bcolor1");
		$(".accperiod").show();
	}
	
	/* 时间选择*/
	function openInputDate(obj){
		WdatePicker({lang:'zh_CN'});
	}
	
	/* 设置开账期间 */
	function sendPeriod(){
		var accPeriodDate = $("#accPeriodDate").val();
		var urls="/UtilServlet?operation=getCurPeriod";
		AjaxRequest(urls);
		if(response!="-1"){
			asyncbox.tips("$text.get('tblSysSetting.statusid.error')",'error',1500);
			return false;
		}
		if(accPeriodDate == ""){
			asyncbox.tips("$text.get("accPeriod.msg.set")",'error',1500);
			return false;
		}
		$("#exe").val("exe");
		$("#type").val("setAccPeriod");
		if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
		form.submit();
	}
	
	/* 开账 */
	function openAcc(){
		if("$!currentlyPeriod" == ""){
			asyncbox.tips("$text.get('sysacc.begin.nobeginperiod')",'error',1500);
			showPeriod();
			$(".openacc").hide();
			return false;
		}
		#if($!NowPeriod!=-1)
			asyncbox.tips("$text.get('common.msg.RET_BEGINACC_END')",'alert',1500);
			return false;
		#end
		asyncbox.confirm('$msgtext','提示',function(action){
	　　　if(action == 'ok'){
	　　　　　location.href="/SysAccAction.do?exe=exe&type=$type&winCurIndex=$winCurIndex";	
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
	　　　}
	　});
	}
	
	/* 设置开账期间返回进行处理 */
	function dealCheck(data){
		var datasplit = data.split(";");
		if(datasplit[0]=="OK"){
				asyncbox.tips(datasplit[1],'success',1500);
			setTimeout(function(){
				window.location.href="/SysAccAction.do?type=beginAcc";
		  	},1500);
		}else{
			asyncbox.tips(datasplit[1],'error',1500);
		}
	}
	
	
	function openAccBack(data){
		var datasplit = data.split(";");
		if(datasplit[0]=="OK"){
				asyncbox.tips(datasplit[1],'success',1500);
			setTimeout(function(){
				window.location.href="/SysAccAction.do?type=beginAcc";
		  	},1500);
		}else{
			asyncbox.tips(datasplit[1],'error',1500);
		}
	}
</script>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/SysAccAction.do?exe=exe&type=beginAcc" target="formFrame">
<input type="hidden" id="beginAccType" name="beginAccType" value="setAccPeriod"/>
<div class="div_con" id="listRange_id" align="center">
<div class="d_table">
	<p class="t-p">
		<b></b>
		开账


	</p>
	<table border="0" cellpadding="0" cellspacing="0"><tr>
		<td align="center"><div class="div_ts" show="accperiod">
			<div><img src="/style/images/flow/war8.gif"/></div>
			<div><label>开账期间</label></div>
		</div></td>
		<td align="center"><div>
			<div><img src="/style/images/flow/flowr.gif"/></div>
		</div></td>
		<td align="center"><div class="div_ts" show="openacc" onclick="openAcc();">
			<div><img src="/style/images/flow/pro3.gif"/></div>
			<div><label>开账</label></div>
		</div></td>
	</tr>
	</table>
	<div class="showW">
		<div class="accperiod" style="display: none;">
			<div style="width:300px;margin:0 auto;padding:10px 20px;" >
				$text.get("present.accounting.time")：&nbsp;&nbsp;&nbsp;$!currentlyPeriod
				<br />
				$text.get("choose.accounting.time")：<input name="accPeriodDate" id="accPeriodDate" value="" onclick="openInputDate(this)" readonly="readonly" style="margin-left: 10px;width:100px;"/>
				<span class="hBtns" onclick="sendPeriod()">$text.get("mrp.lb.save")</span>
			</div>
			<b class="b_1"></b>
		</div>
		<div style="display: none;" class="openacc">
			<span style="padding:0 20px;display:inline-block;line-height:30px;"><i style="padding-left:25px;display:inline-block;"></i>$text.get("sysAcc.msg.openAcctite")：$text.get("sysAcc.msg.openAcc")</span>
			<b class="b_2"></b>
		</div>
	</div>
</div>
</div>

<script type="text/javascript"> 
	$("#listRange_id").css("height",document.documentElement.clientHeight);
</script>
</form>
</body>
</html>



