<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("sysAcc.msg.reCalculate")</title>
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
.d_table table{margin-bottom:30px;}
.div_ts_bcolor1{border: 2px lightblue solid;}
.showW{margin:20px 0 0 0;background:#f2f2f2;text-align: left;border-radius:2px;}
.showW>div{position:relative;padding:15px 0;}
.showW>div>b{width:16px;height:16px;display:inline-block;position:absolute;background:url(/style/images/body/i_017.gif) no-repeat 0 0;top:-6px;}

.d_table .t-p{position:absolute;left:20px;top:0;font-size:16px;line-height:34px;color:#666;padding:0 0 0 35px;font-weight:bolder;}
.t-p>b{width:29px;height:29px;background:url(/style/v7/images/v7_icons.png) no-repeat -40px -2px;display:inline-block;position:absolute;left:0;top:0;}

.hBtns:hover{background: #f2f2f2;}
.acc {border-top:1px solid #bbb;border-right:1px solid #bbb;margin:0 auto;margin-bottom:15px;width:90%;margin-top:5px;}
.acctr {height:22px;}
.acc td {border-left:1px solid #bbb;border-bottom:1px solid #bbb; text-align:center; height:22px;}
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


/* 重算成本 */
function recalculate(){
	var periodYear=$("#periodYear").val();
	var period=$("#period").val();
	var GoodsCode=$("#GoodsCode").val();
	var isCatalog=$("#isCatalog").val();
	var GoodsFullName=$("#tblGoods_GoodsFullName").val();
	
	if(periodYear.length==0||periodYear.length==0){
		if(periodYear.length==0){
			$("#periodYear").focus();
		}else{
			$("#period").focus();
		}
		asyncbox.tips("$text.get("common.msg.MustInputYear")",'alert',1500);
		return ;	
	}else{
		if(!isInt(periodYear)||!isInt(period)||(isInt(periodYear)&&(periodYear<1700||periodYear>9999))||(isInt(period)&&(period<1||period>12))){
			asyncbox.tips("$text.get("reCalculate.periodInt.error")",'alert',1500);
			return;
		}
		var curYear=$periodYear;
		var curPeriod=$period;
		if(periodYear<curYear||(periodYear==curYear&&period<curPeriod)){
			asyncbox.tips('$text.get("common.msg.NotInputPostedpPeriod")','alert',1500);
			return ;
		}
	}
	asyncbox.confirm('$msgtext','提示',function(action){
		if(action == 'ok'){
			document.getElementById("tip").innerHTML='$text.get("reCalculate.tip.title")';
			if(typeof(top.jblockUI)!="undefined"){
				top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
			}
			location.href="/SysAccAction.do?exe=exe&type=$type&winCurIndex=$winCurIndex&periodYear="+periodYear+"&period="+period+"&GoodsCode="+GoodsCode+"&isCatalog="+isCatalog+"&GoodsFullName="+GoodsFullName;	
		}
	});
}

function openSelect(urlstr,obj,field){
	urlstr +="&isMain=true";
	width =800;
	height=470;
	if(width > document.documentElement.clientWidth -50){
		width = document.documentElement.clientWidth -50;
	}
	if(height > document.documentElement.clientHeight -50){
		height = document.documentElement.clientHeight -50;
	}

	window.urlstr = urlstr;
	window.obj = obj;
	window.field = field;
	if(typeof(window.parent.$("#bottomFrame").attr("id"))!="undefined"){
		asyncbox = parent.asyncbox;
	}
 	if(urlstr.indexOf("&url=@URL:")==-1){
 		urlstr = encodeURI(urlstr) ;
 	}
 	urlstr = encodeURI(urlstr) ;
 	if(urlstr.indexOf("+")!=-1){
		urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
	}
	if(urlstr.indexOf("&amp;")!=-1){
		urlstr=urlstr.replaceAll("&amp;","@join:amp;") ;
	}
	urlstr = urlstr.replaceAll("#","%23") ;
	urlstr += "&src=menu&MOID=$MOID&MOOP=add&popupWin=MainPopup";
	asyncbox.open({id:'MainPopup',title:'',url:urlstr,width:width,height:height});
	//设置框架没有按扭后的高度
	$("#MainPopup_content").height($("#MainPopup").height()-25);   
}
</script>
</head>

<body>
<div class="div_con" id="listRange_id" align="center">
<div class="d_table">
	<p class="t-p">
		<b></b>
		$text.get("sysAcc.lb.reCalculate")
	</p>
	<div>
		<div id="tip" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
		<div id="course" align="center" style="color: red;font-size: 14px;font-weight:bold;"></div>
	</div>
	<table border="0" cellpadding="0" cellspacing="0"><tr>
		<td align="center"><div class="div_ts" onclick="recalculate()">
			<div><img src="/style/images/flow/pro3.gif"/></div>
			<div><label>$text.get("sysAcc.lb.reCalculate")</label></div>
		</div></td>
	</tr>
	</table>
	<div class="showW">
		<div class="refCalculate">
				<div class="chooseItem">
					<span style="padding-left: 50px;">$text.get("common.msg.InputProcessCostPeriod")</span><br /><br />
					<span>$text.get("common.lb.periodYear")：<input type="text" name="periodYear" id="periodYear" value="#if($periodYear!=-1)$periodYear#end"/></span><br />
					<span>&nbsp;&nbsp;&nbsp;$text.get("common.lb.period")：<input type="text" name="period" id="period" value="#if($periodYear!=-1)$period#end"></span><br />
					<span>&nbsp;&nbsp;&nbsp;商品：<input type="text" name="tblGoods_GoodsFullName" id="tblGoods_GoodsFullName" value="$!GoodsFullName" readonly="readonly" onDblClick="openSelect('/UserFunctionAction.do?selectName=ReCalculateGoods&operation=22',this,'GoodsCode')"><img src="/$globals.getStylePath()/images/St.gif" onClick="openSelect('/UserFunctionAction.do?selectName=ReCalculateGoods&operation=22',this,'GoodsCode')"  />
					<input type="hidden" name="GoodsCode" id="GoodsCode" value="$!GoodsCode">
					<input type="hidden" name="isCatalog" id="isCatalog" value="$!isCatalog">
					</span><br />
				</div>
				#if($result)
				<table border="0" cellpadding="0" cellspacing="0" class="tab_content acc" >
					<thead>
						<tr>
							<td width="30px"><input type="checkbox" name="selAll" onClick="checkAll('keyId')"></td>
							<td width="100px">$text.get("reCalculate.lb.billCode")</td>
							<td width="100px">$text.get("reCalculate.lb.bilType")</td>
							<td width="100px">$text.get("reCalculate.lb.bilDate")</td>
							<td width="100px">$text.get("reCalculate.lb.stock")</td>
							<td width="100px">$text.get("reCalculate.lb.goodsName")</td>
							#foreach($row in $existPropNames)
								<td width="100">$globals.get($row,1)</td>
								#end
							<td width="100px">$text.get("reCalculate.lb.lastPrice")</td>
							<td width="100px">$text.get("reCalculate.lb.totalQty")</td>
							<td width="100px">$text.get("reCalculate.lb.totalAmt")</td>
						</tr>
					</thead>
					<tbody>
						#foreach ($row in $result )
							<tr>
								<td align="center"><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td>
								<td align="center">$globals.get($row,1) &nbsp;</td>
								<td align="center">$globals.get($row,2) &nbsp;</td>
								<td align="center">$globals.get($row,3) &nbsp;</td>
								<td align="center">$globals.get($row,4) &nbsp;</td>
								<td align="center">$globals.get($row,5) &nbsp;</td>
								#foreach($key in $propValues.keySet())
									  #if($globals.get($row,0)==$key)
									      #foreach($value in $propValues.get($key))
										  <td align="right">$value &nbsp;</td>
										  #end
									  #end
									#end
								<td align="center">$!globals.encodeHTMLLine($globals.get($row,6)) &nbsp;</td>
								<td align="center">$!globals.encodeHTMLLine($globals.get($row,7)) &nbsp;</td>
								<td align="center">$!globals.encodeHTMLLine($globals.get($row,8)) &nbsp;</td>
							</tr>
						#end
					</tbody>
				</table>
				#end
			<span style="padding:0 20px;display:inline-block;line-height:30px;">
			<i style="padding-left:25px;display:inline-block;"></i>重算成本简介：商品成本计算方法为全月一次平均时，做出库单时的成本是取最近入库单价。重算成本功能是计算输入期间商品的出库成本等于（上月结存金额+本期入库金额）/(上月结存数量+本期入库数量)。如有选择商品，则只计算此商品在当前期间的出库成本</span>
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