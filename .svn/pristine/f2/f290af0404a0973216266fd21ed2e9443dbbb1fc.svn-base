<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加领用用品</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/oa/public_goods.js"></script>
<script type="text/javascript">
function saveApply(){	
	var apply_title = document.getElementById("apply_title").value;
	var applyNO = document.getElementById("applyNO").value;
	var buyRole = document.getElementById("formatFileName").value;
	if(applyNO=="" || applyNO == null){
			alert("单号不能为空！");
			return false;
		
	}
	if(buyRole=="" || buyRole == null){
		alert("领用人不能为空！");
		return false;
	
}
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
	document.getElementById("ApplyForm").submit();		
}
/*
function fillData(datas){	
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	parent.jQuery.close('Popdiv');
}
function dbfillData(datas){	
	var e = document.getElementById("applyTody");	
	var table = 'applyTody';
	var nameArr = [10];
	nameArr = ['goodsName','type','goodsSpec','unit','qty','avgPrice','','applyQty','apply_total','a_use'];
	var oldArr = [];	
	$("#applyTody input[name='goodsId']",document).each(function(){ 
		var tr = []; 														   
  		tr.push($(this).val());      			             			   				
  		oldArr.push(tr);
			    		
	});
	changeData(e,11,nameArr,oldArr,table,datas);
	parent.jQuery.close('dealbadiv');
}
*/

//添加异性

function addRowClick(){
	var e = document.getElementById("applyTody");
	var tr = document.createElement("tr");
	var name = [5];
	name = ['goodsName','type','applyQty','unit','use'];
	for(var j=0;j<6;j++){   
		if(j==5){
			td = document.createElement("td");
			td.innerHTML="<span onclick=\"delRow('applyTody',this)\" title=\"删除\" style=\"color:#ff0000;font-size:14px;padding-left:15px;\">X</span>";
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
		$("#applyTody",document).each(function(){
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
<body>
<form method="post" name="ApplyForm" id="ApplyForm" action="/ApplyGoodsAction.do">
<input type="hidden" name="operation" value="$globals.getOP('OP_ADD')"/>
<input type="hidden" name="flag" id="flag" value="$globals.getDigits()"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">添加领用用品</div>
</div>
<div id="listRange_id">
<div  id="conter" style="margin-top:10px;" align="center">
		<table width="80%" border="0" cellpadding="0" cellspacing="0" id="firtTbl" >
			<tr>	
			
			<td >领用单号:</td>
			<td><input type="text" name="applyNO" id="applyNO" value="$globals.getBillNoCode('OAApplyGoodsInfo_applyNO')"/>
			<span style="color:Red">*</span></td> 
			</tr>
			<tr>
		    <td >领用人:</td>
		    <td><input type="text" name="applyRole"  id="formatFileName" onDblClick="deptPop('userGroup','formatFileName','formatFileName');" 
		   	onKeyDown="deptPop('userGroup','formatFileName','formatFileName');" value="$!roleName"
		   	onBlur="deptPop('userGroup','formatFileName','formatFileName');" onKeyPress="deptPop('userGroup','formatFileName','formatFileName');" />		   	
				<font color="red">*</font><img src="/style1/images/St.gif" 
				onClick="deptPop('userGroup','formatFileName','formatFileName');"class="search">									
		   	</td>	
		    <!--  <td><input type="text" name="applyRole" id="applyRole" /></td> -->	
		    <td >领用日期:</td>
     	    <td><input type="text" name="applyDate" id="applyDate" value="$!dateTime" onClick="openInputDate(this);"/></td>	    
		    </tr>
		   
		    <tr>
		    <td >总数量:</td>
     	    <td><input type="text" readonly="readonly" name="apply_qty" id="apply_qty" value="0"/></td>
		    </tr>
		    <tr>
		    <td >备注:</td>
			<td colspan="3"><input type="text" name="apply_title" id="apply_title" style="width:503px;"/></td> 		
		    </tr>
		</table>
	</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="applyTbl" >
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
		<tbody id="applyTody" name="applyTody">	
		#foreach($log in [1..5])
			<tr>
			<input type="hidden"  id="$!log"/>
				<td ><input type="text" name="goodsName" /></td>
				<td ><input type="text" name="type"/></td>
				<td ><input type="text" name="applyQty" id="applyQty" onchange="addSum(this)";/></td>
				<td ><input type="text" name="unit"/></td>
				
				<td ><input type="text" name="use"/></td>
				<td ><span style="color:#ff0000;font-size:14px;padding-left:15px;" title="删除" onclick="delRow('applyTody',this)">X</span></td>
			</tr>
		#end		
		 </tbody>
</table>
</div>
</form>
</body>

</html>