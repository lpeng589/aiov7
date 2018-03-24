<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>归还用品</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/oa/public_goods.js"></script>
<script type="text/javascript">
function saveBack(){	
	var back_title = document.getElementById("back_title").value;
	var backNO = document.getElementById("backNO").value;
	var buyRole = document.getElementById("formatFileName").value;
	var buyDate = document.getElementById("backDate").value;
	buyDate = buyDate.replace(/-/g,"/");
	var flay = true;
	if(backNO=="" || backNO == null){
			alert("单号不能为空！");
			flay = false;
			return false;
		
	}
	if(buyRole=="" || buyRole == null){
		alert("归还人不能为空！");
		flay = false;
		return false;
	
}
	var dtime=new Date();
	var today = dtime.getFullYear()+"/"+(dtime.getMonth()+1)+"/"+dtime.getDate();
	var td = new Date(Date.parse(today));
	var bd = new Date(Date.parse(buyDate));
	if(td>bd){
		alert("请输入正确时间！");
		return false;
	}
	if(flay){
		document.getElementById("BackForm").submit();	
	}		
}
function fillData(datas){	
	newOpenSelect(datas,fieldNames,fieldNIds,1);
	parent.jQuery.close('Popdiv');
}

function dbfillData(datas){	
	var e = document.getElementById("backTody");	
	var table = 'backTody';
	var nameArr = [8];
	nameArr = ['goodsName','applyDate','unit','applyRole','unback','backedQty','B_remark',''];
	var oldArr = [];	
	$("#backTody input[name='goodsId']",document).each(function(){ 
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
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="BackForm" id="BackForm" action="/BackGoodsAction.do" >
<input type="hidden" name="operation" value="$globals.getOP('OP_ADD')"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">归还用品</div>
</div>
<div id="listRange_id">
<div  id="conter" style="margin-top:10px;" align="center">
		<table width="80%" border="0" cellpadding="0" cellspacing="0" id="firtTbl" >
			<tr>	
					
			<td >归还单号:</td>
			<td><input type="text" name="backNO" id="backNO"  value="$globals.getBillNoCode('OABackedGoodsInfo_backNO')"/>
			<span style="color:Red">*</span></td> 
			</tr>
			<tr>
		    <td >归还人:</td>
		    <td><input type="text" name="backedRole"  id="formatFileName" onDblClick="deptPop('userGroup','formatFileName','formatFileName');" 
		   	onKeyDown="deptPop('userGroup','formatFileName','formatFileName');" value="$!roleName"
		   	onBlur="deptPop('userGroup','formatFileName','formatFileName');" onKeyPress="deptPop('userGroup','formatFileName','formatFileName');" />		   	
				<font color="red">*</font><img src="/style1/images/St.gif" 
				onClick="deptPop('userGroup','formatFileName','formatFileName');"class="search">									
		   	</td>			    
		   	<!-- <td><input type="text" name="backedRole" id="backedRole" /></td>	 --> 
		    <td >归还日期:</td>
     	    <td><input type="text" name="backDate" id="backDate" value="$!dateTime" onClick="openInputDate(this);"/></td>
     	    </tr>
	    
		    <tr>
		    <td >归还总数量:</td>
     	    <td><input type="text" readonly="readonly" name="back_qty" id="back_qty" value="0"/></td>
		    </tr>
		    <tr>
     	    <td >备注:</td>
			<td colspan="3"><input type="text" name="back_title" id="back_title" style="width:495px"/></td> 
     	    </tr>
		</table>
	</div>
<div class="scroll_function_small_a" style="margin-top:0px;" align="right">
	<ul class="HeadingButton">
		<li><button type="button" name="addBasicList"><a href="javascript:" id="toAddBasicBtn" title="添加" >添加</a></button>
		</li>	
		<li><input type="hidden" name="flay" id="flay" value="TOADDBACK"/></li>	
	</ul>	
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="backTbl" >
		<thead>
			<tr>			
				<td width="15%" class="oabbs_tl" id="goodsName">用品名称</td>
				<td width="12%" class="oabbs_tc" id="type">领用日期</td>			   
	     	    <td width="9%" class="oabbs_tc" id="unit">单位</td>	 
	     	   	<td width="9%" class="oabbs_tc">领用人</td>
	     	   	<td width="10%" class="oabbs_tc">未还数量</td> 	     	     
		        <td width="8%" class="oabbs_tc" id="backedQty">归还数量</td>		        
		        <td width="30%" class="oabbs_tc" id="B_remark">备注</td>
		         <td></td> 
		        <td width="8%" class="oabbs_tc"></td>		
		                  
			</tr>
		</thead>
		<tbody id="backTody">	
		 </tbody>
</table>
</div>
</form>
</body>

</html>