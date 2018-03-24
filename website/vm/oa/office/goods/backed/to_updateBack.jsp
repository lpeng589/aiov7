<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用品归还修改</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/oa/public_goods.js"></script>
<script type="text/javascript">
function saveUpBack(){	
	var buyDate = document.getElementById("backDate").value;
	buyDate = buyDate.replace(/-/g,"/");
	var dtime=new Date();
	var today = dtime.getFullYear()+"/"+(dtime.getMonth()+1)+"/"+dtime.getDate();
	var td = new Date(Date.parse(today));
	var bd = new Date(Date.parse(buyDate));
	if(td>bd){
		alert("请输入正确时间！");
		return false;
	}
	document.getElementById("upBackForm").submit();
}
function ldUnback(){
	$("#UpBackTody",document).each(function(){
		$("tr",this).each(function(){					   
			var applyQty = $("#applyQty",this).val();
			var back_sign = $("#back_sign",this).val();
			if(back_sign==""){
				back_sign ="0";
				}
			var total = parseFloat(applyQty) - parseFloat(back_sign);
			$("#unback",this).val(total);   			             			   				
  		});
		})
}
function fillData(datas){	
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	parent.jQuery.close('Popdiv');
}

function dbfillData(datas){	
	var e = document.getElementById("UpBackTody");	
	var table = 'UpBackTody';
	var nameArr = [8];
	nameArr = ['goodsName','applyDate','unit','applyRole','unback','backedQty','B_remark',''];
	var oldArr = [];	
	$("#UpBackTody input[name='goodsId']",document).each(function(){ 
		var tr = []; 														   
  		tr.push($(this).val());      			             			   				
  		oldArr.push(tr);
			    		
	});
	changeData(e,9,nameArr,oldArr,table,datas);
	parent.jQuery.close('dealbadiv');
}
</script>
<style type="text/css">
input{margin-left:0;}
</style>
</head>
<body onload="ldUnback()">
<form method="post" name="upBackForm" id="upBackForm" action="/BackGoodsAction.do">
<input type="hidden" name="operation" value="$globals.getOP('OP_UPDATE')"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">用品归还修改</div>
</div>
<div id="listRange_id">
<div  id="conter" style="margin-top:10px;" align="center">
		<table width="80%" border="0" cellpadding="0" cellspacing="0" id="tblSort" >	
			<tr>	
			
			<td >归还单号:</td>
			<td><input type="text" name="backNO" id="backNO" readonly="readonly" value="$!BackGoods.backNO"/><span style="color:Red">*</span>
			</td>  
			</tr>
			<tr>
		    <td >归还人:</td>
		    <td><input type="text" name="backedRole"  id="formatFileName" onDblClick="deptPop('userGroup','formatFileName','formatFileName');" 
		   	onKeyDown="deptPop('userGroup','formatFileName','formatFileName');" value="$!BackGoods.backedRole"
		   	onBlur="deptPop('userGroup','formatFileName','formatFileName');" onKeyPress="deptPop('userGroup','formatFileName','formatFileName');" />		   	
				<font color="red">*</font>
				<img src="/style1/images/St.gif" 
				onClick="deptPop('userGroup','formatFileName','formatFileName');"class="search">									
		   	</td>		
		    <!--  <td><input type="text" name="backedRole" id="backedRole" value="$!log.backedRole"/></td>	-->
		    <td >归还日期:</td>
     	    <td><input type="text" name="backDate" id="backDate" onClick="openInputDate(this);" value="$!BackGoods.backDate"/></td>   
		    </tr>
		  
		    <tr>
		    <td >归还总数量:</td>
     	    <td><input type="hidden" name="old_qty" value="$!BackGoods.back_qty"/><input type="text" readonly="readonly" name="back_qty" id="back_qty" value="$!BackGoods.back_qty"/></td>
		    </tr>			    		 			
		    <tr>
		    <td >备注:</td>
			<td colspan="3"><input type="text" name="back_title" style="width:495px" id="back_title" value="$!BackGoods.back_title"/></td> 		
		    </tr>
		</table>
	</div>
<div class="scroll_function_small_a" style="margin-top:0px;" align="right">
	<ul class="HeadingButton" >
		<li><button type="button" name="addBasicList"><a href="javascript:" id="toAddBasicBtn" title="添加用品" >添加用品</a></button>
		</li>
		<li><input type="hidden" name="flay" id="flay" value="TOUPDATEBACK"/></li>
	</ul>	
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="mytbl" >
		<thead>
			<tr>
				<td width="20%" class="oabbs_tl" id="goodsName">用品名称</td>
				<td width="15%" class="oabbs_tc" id="type">领用日期</td>			 
	     	    <td width="10%" class="oabbs_tc" id="unit">单位</td>	 
	     	   	<td width="15%" class="oabbs_tc">领用人</td>
	     	   	<td width="10%" class="oabbs_tc">未还数量</td>     
		        <td width="10%" class="oabbs_tc" id="backedQty">归还数量</td>
		        <td width="15%" class="oabbs_tc" id="B_remark">备注</td>
		        <td></td>
		        <td width="9%" class="oabbs_tc"></td>
			</tr>
		</thead>
		<tbody id="UpBackTody">
		
		   #foreach ($log in $UpdateList)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">			
				<td class="oabbs_tc"><input type="text" name="goodsName" readonly="readonly" id="goodsName" value="$!log.applyDetBean.goodsName"/></td>	
				<td  class="oabbs_tc"><input type="text" name="type" id="type" readonly="readonly" value="$!log.applyDetBean.applyGoodsBean.applyDate"/></td>
				<td class="oabbs_tc"> <input type="text" name="unit" id="unit" readonly="readonly" value="$!log.applyDetBean.unit"/></td>
				<td  class="oabbs_tc"><input type="text" name="qty" id="qty" readonly="readonly" value="$!log.applyDetBean.applyGoodsBean.applyRole"/></td>
				<td  class="oabbs_tc"><input type="text" name="unback" id="unback" readonly="readonly" value=""/></td>
				
				<td  class="oabbs_tc"><input type="text" name="backedQty" id="backedQty" onchange="checkData('UpBackTody',this);" value="$!log.applyDetBean.back_sign"/></td>
				<td  class="oabbs_tc"><input type="text" name="B_remark" id="B_remark" value="$!log.B_remark"/></td>
				<td><input type="hidden" name="goodsId" id="goodsId" value="$!log.applyDetBean.id"/></td>
				<td >
					<input type="button" value="X" style="color:#ff0000;font-size:15px;" title="删除" onclick="delRow('UpBackTody',this);" />
				</td>
				<input type="hidden" id="back_sign" name="back_sign" value="$!log.applyDetBean.back_sign"/> 
				<input type="hidden" id="applyQty" name="applyQty" value="$!log.applyDetBean.applyQty"/> 
						    			
			</tr>
			#end
		  </tbody>
</table>
</div>
</form>
</body>

</html>