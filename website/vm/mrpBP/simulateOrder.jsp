<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>$text.get("mrp.lb.mlProduce")</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script language="javascript" src="/js/function.js"></script>
		<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
		<script type="text/javascript" src="$globals.js("/js/mrp.vjs","",$text)"></script>
		<script>
function openInputDate(obj)
{
	WdatePicker();
}
//下拉框同步


function selSyn(obj){
	if(obj==m("pId")){
		m("pName").selectedIndex=m("pId").selectedIndex;
	}else{
		m("pId").selectedIndex=m("pName").selectedIndex;
	}
}
//返回值

var rVal = "cancel";
window.onunload = function(){
	window.returnValue=rVal;
}
function setOrderId(){
	var d = new Date();
	var month = d.getMonth()+"";
	var dateStr = d.getDate()+"";
	if(month.length==1){
		month = "0"+month;
	}
	if(dateStr.length==1){
		dateStr = "0"+dateStr;
	}
	//$("form").orderId.value = "MN"+d.getYear()+month+dateStr;
}
function add(){
	if(n("goodsID")[0].value==""){
		alert("$text.get('tblCustomerPrice.error.goods.count')");
		return false;
	}
	rVal = "";
	rVal += n('mrpFrom')[0].value+";";
	rVal += n('orderId')[0].value+";";
	rVal += n('goodsID')[0].value+";";
	rVal += n('goodsnumber')[0].value+";";
	rVal += n('goods')[0].value+";";
	rVal += n('count')[0].value+";";
	rVal += n('billDate')[0].value+";";
	rVal += n('EmployeeID')[0].value+";";
	rVal += n('employee')[0].value+";";
	rVal += n('DepartmentCode')[0].value+";";
	rVal += n('department')[0].value+";";
	rVal += n('title')[0].value+";";
	rVal += n('bomNo')[0].value+";";
	window.close();
}
function cancel(){
	rVal="cancel";
	window.close();
}

function selGoods(){
	dialogSettings = "Center:yes;Resizable:no;DialogHeight:400px;DialogWidth:700px;Status:no";
	gVal = window.showModalDialog("/MrpBP.do?method=selGoods",'',dialogSettings);
	if(!gVal)return false;
	fs = gVal.replace(" ","").split(";");
	if(fs){
		m("goodsID").value=fs[0];
		m("goodsnumber").value=fs[1];
		document.getElementsByName("goods")[0].value=fs[2];
		n('title')[0].value=fs[3];
		n('bomNo')[0].value=fs[4];
		
	}
}
function selEmployee(){
	openSelect('/UserFunctionAction.do?tableName=tblBuyOrder&fieldName=EmployeeID&operation=22'+'&DepartmentCode='+document.getElementsByName('DepartmentCode')[0].value,this,'EmployeeID');
}
function openSelect1(urlstr,displayName,obj,field){
	displayName=encodeURI(displayName) ;
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)!="undefined"){
	var mutli=str.split("|"); 
	hid="";
	dis="";
	if(str.length>0){
		var len=mutli.length;
		if(len>1){len=len-1}
		for(j=0;j<len;j++){ 
			fs=mutli[j].split(";");
				dis=fs[1];
				hid=fs[0];
			if(hid.indexOf("@Sess:")>=0){
				document.getElementById(obj).value="";
				document.getElementById(field).value="";
			}else{
				document.getElementById(obj).value=dis;
				document.getElementById(field).value=hid;
			}
		}
	}else{
		document.getElementById(obj).value="";
		document.getElementById(field).value="";
	}
	}
}
function openSelect(urlstr,obj,field){
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=update",winObj,"dialogWidth=730px;dialogHeight=450px");
	
	if(str){
	fs=str.split(";");   	
	//alert(fs.length);
		if(field=='EmployeeID'){
			m('EmployeeID').value=fs[0];
			m('DepartmentCode').value=fs[1];
			m('department').value=fs[2];
			m('employee').value=fs[3];
			
		}
		if(field=='DepartmentCode'){
			m('DepartmentCode').value=fs[0];
			m('department').value=fs[1];
			
		}
	}
}
</script>
	</head>
	<body scroll="no" onLoad="setOrderId()">
		<form method="post" name="form" id="form">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("mrp.lb.mlProduce")</div>
	<ul class="HeadingButton_Pop-up">
	<li><button type="button" onClick="cancel()">$text.get("mrp.lb.cancel")</button></li>
	<li><button type="button" onClick="add()">$text.get("common.lb.add")$text.get("mrp.lb.mlProduce")</button></li>
	</ul>
</div>
<div class="listRange_Pop-up">
<div class="listRange_1">
							<li>
							<span>$text.get("mrp.lb.mrpFrom")：</span>
							<input value="$!text.get("mrp.lb.mlProduce")" readonly="readonly">
							<input value="0" name="mrpFrom" id="mrpFrom" type="hidden" >
							</li>
							<li>
							<span>$text.get("mrp.lb.relationId")：</span>
								<input id="orderId" value="$!mnId" name="orderId" readonly="readonly">
							</li>

							<li>
								<span>$text.get("mrp.lb.goodsFullName")：</span>
								<input name="goods" onDblClick="selGoods()"><img src="/$globals.getStylePath()/images/St.gif" onClick="selGoods()">
								<input id="goodsID" name="goodsID" type="hidden">
								<input name="title" type="hidden">
							</li>
							<li>
								<span>$text.get("mrp.lb.goodsNumber")：</span><input value="" name="goodsnumber">
							</li>
							<li>
								<span>$text.get("muduleFlow.lb.produceDetailList")：</span><input value="" name="bomNo">
							</li>
							<li>
								<span>$text.get("mrp.lb.produceQty")：</span><input class="count" value="10" name="count">
							</li>

							<li>
								<span>$text.get("mrp.lb.needDate")：</span><input name="BillDate" value="" onKeyDown="return false;"
 onClick="openInputDate(this);">
							</li>
							<li>
								<span>$text.get("mrp.lb.employee")：</span><input name="employee" onDblClick="selEmployee()" id="employee"><img src="/$globals.getStylePath()/images/St.gif" onClick="selEmployee()"><input type="hidden" id="EmployeeID" name="EmployeeID" />
							</li>
							<li>
								<span>$text.get("mrp.lb.department")：</span><input id="department" name="department" onDblClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectDepartmentName','$text.get('mrp.lb.department')','department','departmentCode');"><img src="/$globals.getStylePath()/images/St.gif" onClick="openSelect1('/UserFunctionAction.do?operation=$globals.getOP("OP_POPUP_SELECT")&selectName=SelectDepartmentName','$text.get('mrp.lb.department')','department','departmentCode');"><input id="DepartmentCode" name="DepartmentCode" type="hidden" />
							</li>	
							
						</div>
				</div>
			</div>

		</form>
	</body>
</html>
