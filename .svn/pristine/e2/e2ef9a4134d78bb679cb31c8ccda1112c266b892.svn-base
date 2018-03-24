<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>领用用品修改</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/oa/public_goods.js"></script>
<script type="text/javascript">
function saveUpApply(){	
	var buyDate = document.getElementById("applyDate").value;
	buyDate = buyDate.replace(/-/g,"/");
	var dtime=new Date();
	var today = dtime.getFullYear()+"/"+(dtime.getMonth()+1)+"/"+dtime.getDate();
	var td = new Date(Date.parse(today));
	var bd = new Date(Date.parse(buyDate));
	if(td>bd){
		alert("请输入正确时间！");
		return false;
	}
	document.getElementById("upApplyForm").submit();
}
function checkDel(obj,index){
	if(obj == "0.0" || obj==""){
		var row = index.parentNode.parentNode.rowIndex - 1;
		var table = document.getElementById("UpAyTody");
		table.deleteRow(row);
		
	}else{
		alert("已有归还，不能进行删除！");
	}	
}
//添加异性

function addRowClick(){
	var e = document.getElementById("UpAyTody");
	var tr = document.createElement("tr");
	var name = [5];
	name = ['goodsName','type','applyQty','unit','use'];
	for(var j=0;j<6;j++){   
		if(j==5){
			td = document.createElement("td");
			td.innerHTML="<span onclick=\"checkDel('',this)\" title=\"删除\" style=\"color:#ff0000;font-size:14px;padding-left:15px;\">X</span>";
			tr.appendChild(td);
		}else if(j==2){
			td = document.createElement("td");
			td.innerHTML="<input name="+name[j]+"  onchange=\"addSum(this)\" id="+name[j]+" type=\"text\" />";
			tr.appendChild(td);
		}else{
			td = document.createElement("td");
			td.innerHTML="<input name="+name[j]+"  id="+name[j]+" type=\"text\" />";
			tr.appendChild(td);
		}					
	}
	e.appendChild(tr);
}
// 自动加行
function addSum(index){	
		var row = index.parentNode.parentNode.rowIndex - 1;		
		var flag = document.getElementById("flag").value;
		var applyQty = document.getElementsByName("applyQty");		
		if(isNum(applyQty[row].value)){	
			alert("请输入数字！");
			document.getElementsByName("applyQty")[row].value = "";
			return false;
		}
		if(Number(applyQty[row].value)<0 || parseFloat(applyQty[row].value) == parseFloat(0)){		
			document.getElementsByName("applyQty")[row].value = "";
			alert("请输入正数！");
			return false;
		}		
		if(applyQty[row].value.toString().indexOf(".") !=-1){
			if(applyQty[row].value.toString().split(".")[1].length > flag){
				document.getElementsByName("applyQty")[row].value = "";
				alert("有效位数不能大于"+flag);
				return false;
			}
		}
	applyqty = parseFloat(applyQty[row].value);	
	var app_qty = parseFloat(0);					
		$("#UpAyTody",document).each(function(){
			$("tr",this).each(function(){					   
				var applyQty = $("#applyQty",this).val();
				if(applyQty==""){
					applyQty=parseFloat(0);
				}					
				app_qty += parseFloat(applyQty);				
	  		});
			})						
	$("#apply_qty",document).val(app_qty);	
	
}
</script>
<style type="text/css">
input{margin-left:0;}
</style>
</head>
<body >
<form method="post" name="upApplyForm" id="upApplyForm" action="/ApplyGoodsAction.do">
<input type="hidden" name="operation" value="$globals.getOP('OP_UPDATE')"/>
<input type="hidden" name="flag" id="flag" value="$globals.getDigits()"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">领用用品修改</div>
</div>
<div id="listRange_id">
<div  id="conter" style="margin-top:10px;" align="center">
		<table width="80%" border="0" cellpadding="0" cellspacing="0" id="tblSort" >

			<tr>	
			
			<td >领用单号:</td>
			<td><input type="text" name="applyNO" id="applyNO" readonly="readonly" value="$!ApplyGoods.applyNO"/><font color="red">*</font>
			</td>  
			</tr>
			<tr>
		    <td >领用人:</td>
		    <td><input type="text" name="applyRole"  id="formatFileName" onDblClick="deptPop('userGroup','formatFileName','formatFileName');" 
		   	onKeyDown="deptPop('userGroup','formatFileName','formatFileName');" value="$!ApplyGoods.applyRole"
		   	onBlur="deptPop('userGroup','formatFileName','formatFileName');" onKeyPress="deptPop('userGroup','formatFileName','formatFileName');" />		   	
				<font color="red">*</font><img src="/style1/images/St.gif" 
				onClick="deptPop('userGroup','formatFileName','formatFileName');"class="search">									
		   	</td>	
		    <!-- <td><input type="text" name="applyRole" id="applyRole" value="$!log.applyRole"/></td> -->	
		    <td >领用日期:</td>
     	    <td><input type="text" name="applyDate" id="applyDate" onClick="openInputDate(this);" value="$!ApplyGoods.applyDate"/></td>   
		    </tr>
		   
		    <tr>
		    <td >总数量:</td>
     	    <td><input type="text" readonly="readonly" name="apply_qty" id="apply_qty" value="$!ApplyGoods.apply_qty"/></td>
     	    </tr>			    		 			
		   	<tr>
		    <td >备注:</td>
			<td colspan="3"><input type="text" name="apply_title" style="width:503px;" readonly="readonly" id="apply_title" value="$!ApplyGoods.apply_title"/></td> 		
		    </tr>
		</table>
	</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="mytbl" >
		<thead>
			<tr>
				<td width="15%" class="oabbs_tl" id="goodsName">用品名称</td>
				<td width="8%" class="oabbs_tc" id="type">类型</td>	     	     	     	   		      
		        <td width="8%" class="oabbs_tc" id="applyQty">领用数量</td>
		        <td width="5%" class="oabbs_tc" id="unit">单位</td>	
		        <td width="25%" class="oabbs_tc" id="a_Use">备注</td>
		        <td width="5%" class="oabbs_tc">
		        <img title="加一行" src="/style/images/add.gif" border="0" onclick="addRowClick();"></td>
			</tr>
		</thead>
		<tbody id="UpAyTody">
		
		   #foreach ($log in $UpdateList)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
								
				<input type="hidden" name="keyId" id="keyId"  value="$!log.id"/> 							
				<td ><input type="text" name="goodsName" id="goodsName" value="$!log.goodsName"/></td>	
				<td ><input type="text" name="type" id="type" value="$!log.type"/></td>
								<td ><input type="text" name="applyQty" id="applyQty" value="$!log.applyQty" onchange="addSum(this)"/></td>
				<td > <input type="text" name="unit" id="unit" value="$!log.unit"/></td>

				<td ><input type="text" name="use" id="use"  value="$!log.a_use"/></td>
				<td >
					<input type="button" value="X" style="color:#ff0000;font-size:15px;padding-left:15px" title="删除"  onclick="checkDel('$!log.back_sign',this)" />
				</td>
				<input type="hidden" name="back_sign" id="back_sign" value="$!log.back_sign"/>	
				<input type="hidden" name="id" id="id" value="$!log.id"/>    			
			</tr>
			#end
		  </tbody>
</table>
</div>
</form>
</body>

</html>