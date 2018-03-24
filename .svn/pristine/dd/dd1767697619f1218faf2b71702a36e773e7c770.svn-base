<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>盘点处理</title>
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<style type="text/css">
body,div,form,p,input,button,h1,h2,h3,h4,h5,h6,span,img,font,ul,li,ol,a,table,thead,tboady,tr,td{margin:0;padding:0;}
body{font-size:12px;font-family:微软雅黑;/*background:#FBEAE1;*/}
li{list-style:none;}

.h_div{width:95%;margin:0 auto;padding:5px 0 5px 0;overflow:hidden;}
.h_div_l{float:left;display:inline;padding:0 0 0 0;overflow:hidden;}
.h_div_l .img_div{width:58px;height:26px;background:url(/style1/images/Left/Icon_1.gif) no-repeat 0 0;float:left;display:inline-block;}
.h_div_l .t_span{height:25px;float:left;display:inline-block;border-bottom:1px #81B2E3 solid;line-height:25px;padding:0 10px 0 10px;color:#767271;font-weight:bold;}
.h_div_l .t_sl_div{float:left;display:inline-block;padding:0 0 0 10px;overflow:hidden;}
.h_div_l .t_sl_div .sl{width:100px;margin:5px 0 0 0;}
.h_div_r{float:right;display:inline;padding:0 0 0 0;overflow:hidden;}
.h_div_r .s_btn{height:24px;padding:0 10px 0 10px;background:url(/style/images/client/transferRecordBG.jpg) repeat-x 0 -30px;border:1px #000 solid;color:#fff;font-size:12px;font-family:微软雅黑;line-height:21px;cursor:pointer;}
.h_div_r .div_r_u{padding:0 0 0 0;overflow:hidden;}
.h_div_r .div_r_u .div_r_li{float:left;display:inline;padding:0 0 0 0;overflow:hidden;margin:0 0 0 5px;}

.hs_div{width:95%;margin:0 auto;padding:0 0 2px 0;overflow:hidden;border-bottom:1px #a1a1a1 dashed;}
.hs_div .s_btn{float:left;display:inline-block;padding:0 5px 0 5px;margin:5px 0 0 10px;height:20px;color:#fff;font-size:10px;line-height:18px;border:1px #000 solid;background:url(/style/images/client/transferRecordBG.jpg) repeat-x 0 -35px;cursor:pointer;}


.type_div{padding:8px 0 8px 10px;overflow:hidden;font-size:12px;border-right:1px #a1a1a1 solid;float:left;display:inline-block;}
.type_div .type_d_span{float:left;display:inline-block;line-height:13px;}
.type_div .type_u{float:left;display:inline-block;}
.type_u .type_li{float:left;display:inline-block;margin:0 10px 0 0;}
.type_u .type_li input{float:left;display:inline-block;}
.type_u .type_li a{float:left;display:inline-block;line-height:13px;margin:0 0 0 5px;}
.type_u .type_li .t_label{float:left;display:inline-block;line-height:12px;margin:0 0 0 5px;}

.k_div{padding:5px 10px 4px 0;overflow:hidden;font-size:12px;float:left;display:inline-block;}
.k_div .k_d_span{float:left;display:inline-block;line-height:20px;}
.k_div .k_i_div{width:100px;height:18px;border:1px #000 solid;float:left;display:inline-block;}
.k_div .k_i_div input{height:18px;width:80px;border:none;float:left;display:inline-block;}
.k_div .k_i_div a{width:20px;height:18px;float:left;display:inline-block;background:url(/style/images/client/se.png) no-repeat 0 -1px;cursor:pointer;}

.time_div{padding:5px 10px 4px 10px;overflow:hidden;font-size:12px;float:left;display:inline-block;}
.time_div .t_d_span{float:left;display:inline-block;line-height:20px;}
.time_div .t_i_div{float:left;display:inline-block;padding:0 0 0 0;overflow:hidden;}
.time_div .t_i_div .t_time{width:100px;height:18px;border:1px #000 solid;float:left;display:inline-block;}
.time_div .t_i_div .t_i_d_span{float:left;display:inline-block;padding:0 5px 0 5px;line-height:20px;}

.cz_div{width:95%;margin:0 auto;padding:5px 0 5px 0;overflow:hidden;line-height:20px;}
.cz_div .cz_span{float:left;display:inline-block;width:5%;;padding:4px 0 0 0;background:url(/style/images/client/Add.gif) no-repeat 40px 9px;cursor:pointer;}
.cz_div .cz_u{float:left;display:inline-block;cpadding:0 0 0 0;overflow:hidden;width:93%;}
.cz_div .cz_u .cz_li{float:left;display:inline-block;margin:5px 5px 0 0;padding:0 2px 0 2px;cursor:pointer;border:1px #a1a1a1 dashed;}
.cz_div .cz_u .cz_li .txt_a{float:left;display:inline-block;}
.cz_div .cz_u .cz_li .close_a{float:left;display:inline-block;width:10px;height:20px;background:url(/style/images/client/CloseBtn.png) no-repeat 0 6px;margin:0 0 0 5px;cursor:pointer;}

.ViewTable{border-top:1px #cdcdcd solid;border-left:1px #cdcdcd solid;width:100%;margin:0 auto;}
.ViewTable tr td{padding:0 5px 0 5px; height:28px; line-height:28px;border-bottom:1px #cdcdcd solid;border-right:1px #cdcdcd solid;}
.ViewTable tr td.detail{color:#0179BB;}
.ViewTable tr td.detail:hover{color:#F30;}
.ViewTable thead tr{background:url(/style/images/client/transferRecordBG.jpg) repeat-x 0 -54px; text-align:center;}
.ViewTable tbody tr td {text-align:center;}
.ViewTable tbody tr:hover{background:#C4D3DD;}
.oBtn{background: #6ba9df;color: #fff;padding: 0 10px;cursor: pointer;height: 25px;line-height: 25px;font-size: 13px;display: inline-block;margin: 0 30px 0 0;}
.detail{cursor: pointer;}
.btn_checkDeal{float: left;margin-left: 20%;}
</style>

<script type="text/javascript">
	/* 查询 */
	function querySubmit(){
		form.submit();
	}
	
	//盘点处理
	function checkDeal(){
		#if($stockCheckList.size()==0)
			asyncbox.alert('不存在未处理的盘点单','提示');
			return false;
		#end
		asyncbox.open({
		id:'dealDiv',
	　　　html : '<div class="spe-warn"><p>请选择盘点处理方式</p><span class="radio-wp-span"><input type="radio" id="ctype1" name="ctype" value="part" checked/><label for="ctype1"><i>部分盘点：</i>只处理您盘点单中已经录入的商品，未录入的商品库存保持不变。</label></span><span class="radio-wp-span"><input type="radio" id="ctype2" name="ctype" value="Total"/><label for="ctype2"><i>全部盘点：</i>处理全部商品，没有录入盘点单的商品在此仓库的数量将会被清零。</label></span></div>',
	　　　width : 580,
		 modal　: true,
		 title : '盘点处理',
		 btnsbar : jQuery.btn.OKCANCEL,
			 callback : function(action){
		　　　　　if(action == 'ok'){			
					var ctype = parent.jQuery("input[name='ctype']:checked").val();
					if(ctype=='Total'&&!window.confirm("盘点是不可逆的，全盘处理很危险,确定要全部盘点？")){
						return false;
					}

					jQuery("#checkType").val(ctype);
					jQuery("#operation").val('2');
					jQuery("#optype").val('checkDeal');
					parent.jQuery.close("dealDiv"); 
					form.submit();
					return false;
		　　　　　}
		　　　}
	　　　});
	}
	
	/* 盘点统计 */
	function statisticsStockCheck(stockCode){
		mdiwin('/ReportDataAction.do?reportNumber=tblStockCheckDet&StockCode='+stockCode+'&detTable_list=tblStockCheck','盘点统计');
	}
	
	/* 详情 */
	function detailCheck(id){
		mdiwin('UserFunctionQueryAction.do?tableName=tblStockCheck&operation=5&keyId='+id,'盘点录入单');
	}
	
	/* 修改 */
	function updateCheck(id){
		mdiwin('UserFunctionAction.do?tableName=tblStockCheck&operation=7&keyId='+id,'盘点录入单');
	}
	
	/* 删除 */
	function delCheck(id){
		asyncbox.confirm('你确定删除此盘点单吗？','提示',function(action){
		　　if(action == 'ok'){
				jQuery.ajax({type: "POST",url: "/StockCheckAction.do?operation=4&optype=delStockCheck&id="+id,async : false,success: function(data){
					if(data == "OK"){
						window.location.reload();
					}else if(data == "ERROR"){
						alert("删除失败");
					}else{
						alert(data);
					}
				}});
			}
		});
	}
</script>
</head>
<body>
<form action="/StockCheckAction.do" method="post" name="form" id="form">
<input type="hidden" id="operation" name="operation" value="4"/>
<input type="hidden" id="optype" name="optype" value="stockCheckDealList"/>
<input type="hidden" id="checkType" name="checkType" value=""/>
<div class="h_div">
	<div class="h_div_l">
    <span class="t_span">当前位置>>盘点处理</span>
  </div>
  <div class="h_div_r">
  </div>
</div>
<div class="hs_div">
  <input type="hidden" name="stockPreId" id="stockPreId" value="$!stockPreId"/>
  <div class="k_div">
  	<span class="k_d_span">仓库：</span>
  	<div class="k_i_div">
  		<input type="hidden" name="stockCode" id="stockCode" value="$!stockCode"/>
  		<input type="text" name="stockName" id="stockName" value="$!stockName" readonly="readonly"/> 
     <!--  <a onclick="popStock(this,'SelectStockDet','stockCode','stockName');"></a> -->
    </div>
  </div>
  <div class="time_div">
  	<span class="t_d_span">盘点日期：</span>
  	<div class="t_i_div">
  		<input type="text" name="stockCheckDate" id="stockCheckDate" class="t_time" readonly="readonly" value="$!stockCheckDate" class="inp_w inp_date"/> 
    </div>
  </div>
  <div class="btn_checkDeal">
	  <span class="oBtn" onclick="checkDeal()">盘点处理</span>
	  <span class="oBtn" onclick="statisticsStockCheck('$!stockCode')">盘点统计</span>
  </div>
</div>
<div id="contentsDiv" style="overflow: auto;margin: 0 auto; width: 95%;padding-top: 10px;">
<table class="ViewTable" border="0" cellpadding="0" cellspacing="0" bordercolor="#CDCDCD">
	<thead>
		<tr>
			<td>单据编号</td>
			<td>单据日期</td>
			<td>仓库</td>
			<td>单据状态</td>
		</tr>
	</thead>
	<tbody id="content_body">
		#foreach($stockCheck in $stockCheckList)
			<tr>
				<td><span id="span_billno">$stockCheck.BillNo</span></td>
				<td>$stockCheck.BillDate</td>
				<td>$stockCheck.StockFullName</td>
				<td>#if("$!stockCheck.statusId"=="1")已过账#else未过账#end</td>
			</tr>
		#end
	</tbody>	
</table>
#if($stockCheckList.size()==0)
	<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">
		进行过滤时，暂无盘点单信息

	</div>
#end
</div>
<div class="listRange_pagebar">
	$!pageBar
</div>
<script type="text/javascript">
	var oDiv=document.getElementById("contentsDiv");
	var sHeight=document.documentElement.clientHeight-130;
	oDiv.style.height=sHeight+"px";
</script>
</form>
</body>
</html>
