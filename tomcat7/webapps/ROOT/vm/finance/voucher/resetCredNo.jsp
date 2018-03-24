<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>凭证整理</title>
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

.b_1{left:345px}

.d_table>p{text-align:center;font-size:20px;}
.div_ts_bcolor1{border: 2px lightblue solid;}
.showW{margin:20px 0 0 0;background:#f2f2f2;text-align: left;border-radius:2px;}
.showW>div{position:relative;padding:15px 0;}
.showW>div>b{width:16px;height:16px;display:inline-block;position:absolute;background:url(/style/images/body/i_017.gif) no-repeat 0 0;top:-6px;}

.d_table .t-p{position:absolute;left:20px;top:0;font-size:16px;line-height:34px;color:#666;padding:0 0 0 35px;font-weight:bolder;}
.t-p>b{width:29px;height:29px;background:url(/style/v7/images/v7_icons.png) no-repeat -40px -2px;display:inline-block;position:absolute;left:0;top:0;}

.hBtns:hover{background: #f2f2f2;}
.hBtns{background: #FCFCFC;padding: 5px 10px;border:1px #bbb solid;color:#666;border-radius:3px;cursor:pointer;font-weight:bold;margin-left: 20px;}

.chooseItem{padding-bottom: 20px;padding-left: 32%;}
</style>

<script type="text/javascript">
$(document).ready(function (){
	//鼠标移动到选项时，突出
		//鼠标移动到选项时，突出
		$(".div_ts").mouseover(function(){
			$(this).addClass("div_ts_bcolor1");
		})
		$(".div_ts").mouseout(function(){
			$(this).removeClass("div_ts_bcolor1")
		});
});

/* 凭证整理 */
function reset(){
	var periodYear=$("#periodYear").val();
	var period=$("#period").val();
	var credTypeid=$("#credTypeid").val();
	if(periodYear.length==0||periodYear.length==0){
		if(periodYear.length==0){
			$("#periodYear").focus();
		}else{
			$("#period").focus();
		}
		asyncbox.tips("必须输入期间年和期间！",'alert',1500);
		return ;	
	}else{
		if(!isInt(periodYear)||!isInt(period)||(isInt(periodYear)&&(periodYear<1700||periodYear>9999))||(isInt(period)&&(period<1||period>12))){
			asyncbox.tips("期间年及期间必须输入有效的数值！",'alert',1500);
			$("#periodYear").focus();
			return;
		}
	}
	asyncbox.confirm('重新整理凭证号可能会把凭证进行重新排序，请慎重。是否确定整理凭证号?','提示',function(action){
		if(action == 'ok'){
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
			document.getElementById("tip").innerHTML='正在进行重新整理凭证号...根据电脑配置不同可能需要几十秒到几分钟.';
			var url = "/VoucherManage.do?optype=resetCredNo&credTypeid="+credTypeid+"&winCurIndex=3&periodYear="+periodYear+"&period="+period;
			url = encodeURI(encodeURI(url));
			location.href=url;	
		}
	});
}

</script>
</head>

<body>
<div class="div_con" id="listRange_id" align="center">
<div class="d_table">
	<p class="t-p">
		<b></b>
		凭证整理
	</p>
	<div>
		<div id="tip" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
		<div id="course" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
	</div>
	<table border="0" cellpadding="0" cellspacing="0"><tr>
		<td align="center"><div class="div_ts" onclick="reset()">
			<div><img src="/style/images/flow/pro3.gif"/></div>
			<div><label>凭证整理</label></div>
		</div></td>
	</tr>
	</table>
	<div class="showW">
		<div class="refCalculate">
				<div class="chooseItem">
					<span style="padding-left: 70px;">按以下条件重排</span><br />
					<span>凭证字：<select id="credTypeid" name="credTypeid" style="width:154px;">
					#foreach($erow in $globals.getEnumerationItems('CredTypeID'))
						<option title="$erow.name" value="$erow.value">$erow.name</option>
					#end
				</select></span><br />
					<span>期间年：<input type="text" name="periodYear" id="periodYear" value="$!accPeriod.AccYear"/></span><br />
					<span>&nbsp;&nbsp;&nbsp;期间：<input type="text" name="period" id="period" value="$!accPeriod.AccPeriod"/></span><br />
				</div>
			<span style="padding:0 20px;display:inline-block;line-height:30px;">
			<i style="padding-left:25px;display:inline-block;"></i>凭证整理简介：凭证整理适用于存在凭证断号，需要系统自动进行凭证号的连续排序的情况。只对可修改状态凭证、已复核的凭证、已作废凭证起作用。对已审核、已过账、已结账期间的凭证不参与凭证整理。</span>
			<b class="b_1"></b>
		</div>
	</div>
</div>
</div>

<script type="text/javascript"> 
	$("#listRange_id").css("height",document.documentElement.clientHeight);
</script>
</body>
</html>