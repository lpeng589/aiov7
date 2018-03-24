<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>反月结</title>
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
.div_ts{width:70px;border: 2px aliceblue solid;border-radius:7px;cursor:pointer;text-align:center;}
.div_ts label{cursor:pointer;}
.d_table{width:700px;padding:80px;background:#fff;margin-top:10px;min-height:350px;position:relative;}

.b_1{left:220px}
.b_2{left:470px}

.d_table>p{text-align:center;font-size:20px;}
.d_table table{width:400px;margin-botto:30px;}
.div_ts_bcolor1{border: 2px lightblue solid;}
.showW{margin:20px 0 0 0;background:#f2f2f2;text-align: left;border-radius:2px;}
.showW>div{position:relative;padding:15px 0;}
.showW>div>b{width:16px;height:16px;display:inline-block;position:absolute;background:url(/style/images/body/i_017.gif) no-repeat 0 0;top:-6px;}

.d_table .t-p{position:absolute;left:20px;top:0;font-size:16px;line-height:34px;color:#666;padding:0 0 0 35px;font-weight:bolder;}
.t-p>b{width:29px;height:29px;background:url(/style/v7/images/v7_icons.png) no-repeat -40px -2px;display:inline-block;position:absolute;left:0;top:0;}

.hBtns:hover{background: #f2f2f2;}

.hBtns{background: #FCFCFC;padding: 5px 10px;border:1px #bbb solid;color:#666;border-radius:3px;cursor:pointer;font-weight:bold;margin-left: 20px;}
</style>

<script type="text/javascript">
$(document).ready(function (){
	//鼠标移动到选项时，突出
		//鼠标移动到选项时，突出
		$(".div_ts").mouseover(function(){
			var sNum = $(this).attr("show");
			$(this).addClass("div_ts_bcolor1").parent("td").siblings("td").find("div.div_ts").removeClass("div_ts_bcolor1");
			$("."+sNum).show().siblings("div").hide();
		})
		showRefClose();
});

/* 显示设置开账期间 */
function showRefClose(){
	$("div[show=refCloseAcc]").addClass("div_ts_bcolor1").parent("td").siblings("td").find("div.div_ts").removeClass("div_ts_bcolor1");
	$(".refCloseAcc").show();
}

/* 财务反月结 */
function reCloseAcc(){
	#if("$flag"!="true")
		asyncbox.tips("$text.get('common.msg.NoMonthPostedPeriod')",'alert',1500);
		return false;
	#end
	asyncbox.confirm('确定财务反月结？','提示',function(action){
	　　　if(action == 'ok'){
			location.href="/SysAccAction.do?exe=exe&type=reSettleAcc&type2=AccReSettleAcc&winCurIndex=$winCurIndex";
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
		}
	});
}

/* 业务反月结 */
function reBusinessAcc(){
	asyncbox.confirm('确定业务反月结？','提示',function(action){
		if(action == 'ok'){
			location.href="/SysAccAction.do?exe=exe&type=reSettleAcc&type2=reSettleAcc&winCurIndex=$winCurIndex";
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
		}
	});
}
</script>
</head>

<body>
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/SysAccAction.do" target="formFrame">
<input type="hidden" id="type" name="type" value=""/>
<input type="hidden" id="exe" name="exe" value=""/>
<div class="div_con" id="listRange_id" align="center">
<div class="d_table">
	<p class="t-p">
		<b></b>
		反月结

	</p>
	<table border="0" cellpadding="0" cellspacing="0"><tr>
		<td align="center"><div class="div_ts" show="refCloseAcc" #if("$flagA"=="true")onClick="reCloseAcc()#end">
			<div><img src="/style/images/flow/pro3.gif"/></div>
			<div><label>财务反月结</label></div>
		</div></td>
		<td align="center"><div>
			<div><img src="/style/images/flow/flowr.gif"/></div>
		</div></td>
		<td align="center"><div class="div_ts" show="refBusinessAcc" #if("$flag"=="true")onclick="reBusinessAcc();#end">
			<div><img src="/style/images/flow/pro3.gif"/></div>
			<div><label>业务反月结</label></div>
		</div></td>
	</tr>
	</table>
	<div class="showW">
		<div class="refCloseAcc" style="display: none;">
			#if("$flagA"=="true")
			<table border="0" cellpadding="0" cellspacing="0" class="tab_content acc">
				<tbody>
				<tr class="acctr">
					<td width="100">$text.get("com.accPeriod.year")</td>
					<td width="100">$text.get("com.accPriod.period")</td>
					<td width="100" class="td1" align="center" rowspan="2">$text.get("com.accPriod.resettleto")</td>
					<td width="100">$text.get("com.accPeriod.year")</td>
					<td width="100">$text.get("com.accPriod.period")</td>
				</tr>
				<tr>
					<td width="100">$!globals.get($fromA,0)</td>
					<td width="100">$!globals.get($fromA,1)</td>
					<td width="100">$!globals.get($toA,0)</td>
					<td width="100">$!globals.get($toA,1)</td>
				</tr>
				</tbody>
			</table>	
			#else
				<div style="text-align: center;padding-bottom: 20px;"><span>没有已经月结的财务期间</span></div>
			#end
			<span style="padding:0 20px;display:inline-block;line-height:30px;">
			<i style="padding-left:25px;display:inline-block;"></i></span>
			<b class="b_1"></b>
		</div>
		<div style="display: none;" class="refBusinessAcc">
			<span style="padding:0 20px;display:inline-block;line-height:30px;"><i style="padding-left:25px;display:inline-block;"></i>业务反月结：</span>
			#if("$flag"=="true")
			<table border="0" cellpadding="0" cellspacing="0" class="tab_content acc">
				<tbody>
				<tr class="acctr">
					<td width="100">$text.get("com.accPeriod.year")</td>
					<td width="100">$text.get("com.accPriod.period")</td>
					<td width="100" class="td1" align="center" rowspan="2">$text.get("com.accPriod.resettleto")</td>
					<td width="100">$text.get("com.accPeriod.year")</td>
					<td width="100">$text.get("com.accPriod.period")</td>
				</tr>
				<tr>
					<td width="100">$!globals.get($from,0)</td>
					<td width="100">$!globals.get($from,1)</td>
					<td width="100">$!globals.get($to,0)</td>
					<td width="100">$!globals.get($to,1)</td>
				</tr>
				</tbody>
			</table>	
			#else
				<div style="text-align: center;padding-bottom: 20px;"><span>没有已经月结的业务期间</span></div>
			#end
			<span style="padding:0 20px;display:inline-block;line-height:30px;">
			<i style="padding-left:25px;display:inline-block;"></i>$text.get("sysAcc.msg.reCloseAcctite")：$text.get("sysAcc.msg.reCloseAcc")</span>
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



