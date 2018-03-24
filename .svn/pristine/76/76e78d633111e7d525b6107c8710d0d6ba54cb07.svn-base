<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改信息</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/listPage.vjs"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}
var fieldNames;
var fieldNIds;
var flag;
function deptPop(popname,fieldName,fieldNameIds,flag){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	var titles = "请选择部门";
	if(popname == "userGroup"){
		titles = "请选择个人"
	}
	fieldNames = fieldName;
	fieldNIds = fieldNameIds;
	flag=flag;
	asyncbox.open({
	id : 'Popdiv',
	 title : titles,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。


　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。

				var employees = opener.strData;
				newOpenSelect(employees,fieldName,fieldNameIds,flag)
　　　　　	}
　　　	}
　	});
}
function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,flag);
	parent.jQuery.close('Popdiv');
}
function newOpenSelect(employees,fieldName,fieldNameIds,flag){
	var employees = employees.split("|") ;	
	//for(var j=0;j<employees.length-1;j++){
	if(employees.length>1){
		var field = employees[employees.length-2].split(";") ;
		if(field!=""){			
				document.getElementById(fieldNameIds).value=field[1];
				document.getElementById(fieldNameIds+"_id").value=field[0];
			}
	}
		
		//}
			
}	
function save(){
	var nextMaintainDate = new Date(Date.parse(jQuery("#nextMaintainDate").val()));
	var insureDate = new Date(Date.parse(jQuery("#insureDate").val()));
	var ovreDate = new Date(Date.parse(jQuery("#ovreDate").val()));
	var time = new Date(); 
	if(nextMaintainDate !="" && nextMaintainDate<time && nextMaintainDate !=undefined){
		alert("下次保养时间不能小于今天！");
		return false;
	}
	if(ovreDate !="" && ovreDate<time && ovreDate !=undefined){
		alert("投保结束时间不能小于今天！");
		return false;
	}
	if(insureDate !="" && insureDate>ovreDate){
		alert("投保结束时间不能小于投保时间");
		return false;
	}		
	form.submit();		
}
</script>
<style type="text/css">
input{margin-left:0;}
</style>
</head>
<body>
<form method="post" name="form" id="form" action="/CarInfoAction.do">
<input type="hidden" name="operation" value="2"/>
<input type="hidden" name="updateFlag" value="XY"/>
<input type="hidden" name="openFlag" value="$!openFlag"/>
<input type="hidden" name="id" value="$!rsList.id"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">修改信息</div>
	
</div>
<div id="listRange_id">
	<div  id="conter" style="margin-top:10px;" align="center">
		<table width="90%" border="0" cellpadding="0" align="center" cellspacing="0" id="firtTbl" >
			<tr>								
				<td >车牌号:</td>
	     	    <td ><input type="text" name="carNo" id="carNo"  readonly="readonly" value="$!rsList.carNo"/>    
			    <span style="color:Red">*</span></td>
			    <td >车辆名称:</td>
				<td ><input type="text" name="carName" id="carName" readonly="readonly" value="$!rsList.carName"/>
				<span style="color:Red">*</span></td> 
			</tr>
			#if($!openFlag == 2 )
			<tr>
			    <td >保险公司:</td>
	     	    <td ><input type="text"  name="company" id="company" value="$!rsList.company"/></td>
	     	    <td >保费:</td>
				<td ><input type="text" name="insureCost" id="insureCost" value="$!rsList.insureCost"/></td> 				    			
			</tr>
			<tr>
		    	<td >保险经办人:</td>
		    	<td >
		    	<input  type="hidden" name="dealPeople" id="formatFileName_id" value="$!rsList.dealPeople"/><input type="text"  id="formatFileName" 
		    	onDblClick="deptPop('userGroup','formatFileName','formatFileName');" value='$globals.getEmpFullNameByUserId("$!rsList.dealPeople")'/>		   	
				<img src="/style1/images/St.gif" 
				onClick="deptPop('userGroup','formatFileName','formatFileName');"class="search">									
		   		</td>
		   	</tr> 
		    <tr>
			    <td >投保日期:</td>
	     	    <td ><input type="text"  name="insureDate" id="insureDate" onClick="openInputDate(this);" value="$!rsList.insureDate"/></td>
				<td >结束日期:</td>
	     	    <td ><input type="text"  name="ovreDate" id="ovreDate" onClick="openInputDate(this);" value="$!rsList.ovreDate"/></td>		    		   
			</tr>
			#else	
			<tr>
				<td >保养经办人:</td>
		    	<td >
		    	<input  type="hidden" name="maintainPeople" id="maintainPeople_id" value="$!rsList.maintainPeople"/>
		    	<input type="text"   id="maintainPeople" 
		    	onDblClick="deptPop('userGroup','formatFileName','maintainPeople');" value='$globals.getEmpFullNameByUserId("$!rsList.maintainPeople")'/>		   	
				<img src="/style1/images/St.gif" 
				onClick="deptPop('userGroup','formatFileName','maintainPeople');"class="search">									
		   		</td>
				<td >下次保养时间:</td>
	     	    <td ><input type="text"  name="nextMaintainDate" id="nextMaintainDate" onClick="openInputDate(this);" value="$!rsList.nextMaintainDate"/></td>		   		   	    				    						
			</tr>
			#end	    		
		</table>
		<div class="btn btn-small" title="保存" onclick="save();">保存</div>
	</div>
</div>
</form>
</body>

</html>