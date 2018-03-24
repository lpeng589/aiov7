<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$!nameList</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/listPage.vjs"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="$globals.js("/js/oa/picUpload.vjs","tblGoods",$text)"></script>
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
	jQuery.close('Popdiv');
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
function save(obj){	
	var carNo = jQuery("#carNo").val();
	var carName = jQuery("#carName").val();
	var carStatus = jQuery("#carStatus").val();
	var price = jQuery("#price").val();
	var maxLoad = jQuery("#maxLoad").val();
	var maintainPeople = document.getElementsByName("maintainPeople").value;
	var formatFileName = jQuery("#formatFileName").val();
	if(carNo==""){	
		alert("车牌号不能为空！");		
		return false;
	}
	if(carName==""){	
		alert("车辆名称不能为空！");		
		return false;
	}
	if(carStatus==""){	
		alert("状态不能为空！");		
		return false;
	}
	if(price!="" && isNum(price)){	
		alert("请输入数字！");
		jQuery("#price").val("");
		return false;
	}
	if(price!="" && isNum(price)){	
		alert("购买价格请输入数字！");
		jQuery("#price").val("");
		return false;
	}
	if(maintainPeople ==""){		
		alert("保养经办人不能为空！");
		return false;
	}
	if(formatFileName ==""){		
		alert("保险经办人不能为空！");
		return false;
	}
	if(maxLoad!="" && isNum(maxLoad)){	
		alert("最大载重请输入数字！");
		jQuery("#maxLoad").val("");
		return false;
	}
	if(maxLoad!="" && Number(maxLoad)<0){		
		jQuery("#maxLoad").val("");
		alert("最大载重请输入数字！");
		return false;
	}
	var nextMaintainDate = new Date(Date.parse(jQuery("#nextMaintainDate").val()));
	var runPapersDate = new Date(Date.parse(jQuery("#runPapersDate").val()));
	var insureDate = new Date(Date.parse(jQuery("#insureDate").val()));
	var ovreDate = new Date(Date.parse(jQuery("#ovreDate").val()));
	var time = new Date(); 
	if(nextMaintainDate !="" && nextMaintainDate<time){
		alert("下次保养时间不能小于今天！");
		return false;
	}
	if(runPapersDate !="" && runPapersDate<time){
		alert("行驶证到期时间不能小于今天！");
		return false;
	}
	if(ovreDate !="" && ovreDate<time){
		alert("投保结束时间不能小于今天！");
		return false;
	}
	if(insureDate !="" && insureDate>ovreDate){
		alert("投保结束时间不能小于投保时间");
		return false;
	}	
	if(obj !=undefined && obj != "" && obj !=null){
		form.action="/CarInfoAction.do?operation=2&id="+obj;
		form.submit();	
	}else{
		form.submit();	
	}
		
}

function isNum(str) 
{ 
	return isNaN(str); 
}
function checkedNO(){
	var carNO = jQuery("#carNo").val();
	if(carNO == ""){
		alert("请填写车牌号");
		return false;
	}else{
		var url = "/CarInfoAction.do?operation=69&carNO="+carNO;
		jQuery.ajax({
		   type: "POST",
		   url: url,		   
		   success: function(msg){
		   	if(msg != "3"){
		   		alert(msg);
				jQuery("#carNo").val("");
		   		}		    
		   }
		});			
	}

}
function loadData(){
	if(jQuery("#statusCar").val() !="" && jQuery("#statusCar").val() !=null && jQuery("#statusCar").val() !=undefined){
		jQuery("#carStatus").val(jQuery("#statusCar").val());
	}
	var str = document.getElementById("photo").value;
	if(str!=""){		
	      document.getElementById("delImg").src = "/ReadFile.jpg?type=PIC&tempFile=path&path=/pic/OACar/&fileName="+str;
	      document.getElementById("oa_photo").value = "/ReadFile.jpg?type=PIC&tempFile=path&path=/temp/&fileName="+str;	      
	}	
}
function backSubmit(){
	location.href="/CarInfoAction.do?operation=4";
}
function moreData(){
	if(jQuery("#twoTbl").css("display") =="none"){
		jQuery("#twoTbl").css("display","block");
		jQuery("#tuBtton").css("display","none");
	}
}
</script>
<style type="text/css">
input{margin-left:0;}
</style>
</head>
<body onload="loadData();">
<form method="post" name="form" id="form" action="/CarInfoAction.do">
<input type="hidden" name="operation" value="$globals.getOP('OP_ADD')"/>
<input type="hidden" id="typeCar" value="$!perList.carType"/>
<input type="hidden" id="statusCar" value="$!perList.carStatus"/>
<input type="hidden" id="carChecker" value="$!perList.checker"/>
<input type="hidden" name="picFiles" id="picFiles" value="/temp/">
<input type="hidden" name="delPicFiles" id="delPicFiles" value="">
<input type="hidden" name="photo" id="photo" value="$!perList.carPicture">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$!nameList</div>
	<ul class="HeadingButton">
		<li><button type="button"  name="backList" 
		onClick="backSubmit();" class="b2">返回</button>
		</li>
	</ul>
</div>
<div id="listRange_id">
	<div  id="conter" style="width:60%;min-width:800px;margin:0 auto;margin-top:10px;position:relative;" align="center" >
	
		<table width="100%" border="0" cellpadding="0" align="center" cellspacing="0" id="firtTbl" >
			<tr>								
				<td >车牌号:</td>
	     	    <td style="padding-left: 45px;"><input type="text" name="carNo" id="carNo"  onblur="return checkedNO();" value="$!perList.carNo"/>    
			    <span style="color:Red">*</span></td>
			</tr>
			<tr>
				<td >车辆名称:</td>
				<td style="padding-left: 45px;"><input type="text" name="carName" id="carName" value="$!perList.carName"/>
				<span style="color:Red">*</span></td> 
			</tr>
			<tr>
				<td >状态:</td>
			    <td style="padding-left: 45px;"><select name="carStatus"  id="carStatus" style="width:150px">
						<option value="1">正常</option>
						<option value="2">维修</option>
						<option value="3">报废</option>							
					<span style="color:Red">*</span></select>
	     	    </td>	
	     	 </tr>
			<tr>
			    <td >保险公司:</td>
	     	    <td style="padding-left: 45px;"><input type="text"  name="company" id="company" value="$!perList.company"/></td>
			</tr>
			<tr>
				<td >保费:</td>
				<td style="padding-left: 45px;"><input type="text" name="insureCost" id="insureCost" value="$!perList.insureCost"/></td> 				    
			</tr>
		    <tr>
			    <td >投保日期:</td>
	     	    <td style="padding-left: 45px;"><input type="text"  name="insureDate" id="insureDate" onClick="openInputDate(this);" value="$!perList.insureDate"/></td>
			</tr>
			<tr>	
				<td >结束日期:</td>
	     	    <td style="padding-left: 45px;"><input type="text"  name="ovreDate" id="ovreDate" onClick="openInputDate(this);" value="$!perList.ovreDate"/></td>		    		   
			</tr>		    
			<tr>
		    	<td >保险经办人:</td>
		    	<td style="padding-left: 45px;">
		    	<input  type="hidden" name="dealPeople" id="formatFileName_id" />
		    	<input type="text"   id="formatFileName" onblur="deptPop('userGroup','formatFileName','formatFileName');"
		    	onDblClick="deptPop('userGroup','formatFileName','formatFileName');" value='$globals.getEmpFullNameByUserId("$!perList.dealPeople")'
		    	onkeypress="deptPop('userGroup','formatFileName','formatFileName');"
		    	onkeydown="deptPop('userGroup','formatFileName','formatFileName');"
		    	onkeyup="deptPop('userGroup','formatFileName','formatFileName');"/><span style="color:Red">*</span>	   	
				<img src="/style1/images/St.gif" 
				onClick="deptPop('userGroup','formatFileName','formatFileName');"class="search">									
		   		</td>
		   	</tr> 	
		    <tr>
		    	<td >保养经办人:</td>
		    	<td style="padding-left: 45px;">
		    	<input  type="hidden" name="maintainPeople" id="maintainPeople_id" />
		    	<input type="text"   id="maintainPeople" onblur="deptPop('userGroup','formatFileName','maintainPeople');"
		    	onDblClick="deptPop('userGroup','formatFileName','maintainPeople');" value='$globals.getEmpFullNameByUserId("$!perList.maintainPeople")'
		    	onkeypress="deptPop('userGroup','formatFileName','maintainPeople');"
		    	onkeydown="deptPop('userGroup','formatFileName','maintainPeople');"
		    	onkeyup="deptPop('userGroup','formatFileName','maintainPeople');" />
		    	<span style="color:Red">*</span>		   	
				<img src="/style1/images/St.gif" 
				onClick="deptPop('userGroup','formatFileName','maintainPeople');"class="search">									
		   		</td>
		   		<td >下次保养时间:</td>
	     	    <td ><input type="text"  name="nextMaintainDate" id="nextMaintainDate" onClick="openInputDate(this);" value="$!perList.nextMaintainDate"/></td>		   		   	    				    			
		    </tr> 		
			<tr>
			    <td >年检日期:</td>
	     	    <td style="padding-left: 45px;"><input type="text"  name="surveyDate" id="surveyDate" onClick="openInputDate(this);" value="$!perList.surveyDate"/></td>		    
			    <td >行驶证到期日期:</td>
	     	    <td ><input type="text"  name="runPapersDate" id="runPapersDate" onClick="openInputDate(this);" value="$!perList.runPapersDate"/></td>
		    </tr>
		    <tr>
		    	<td >年检情况:</td>
				<td colspan="3" style="padding-left: 45px;"><input type="text" name="survey" id="survey" style="width:606px;" value="$!perList.survey"/></td> 		
		    </tr>	
		    
		    </table>
		    #if("$!perList.id" != "null" && "$!perList.id" != "")
		    <table width="100%" border="0" style="display: block" cellpadding="0" align="center" cellspacing="0" id="twoTbl">		  
		    #else
		    <table width="100%" border="0" style="display: none" cellpadding="0" align="center" cellspacing="0" id="twoTbl">		
		    #end
		    <tr>
		    	<td >购置车辆时间:</td>
				<td ><input type="text" style="margin-left: 15px;" name="buyTime" id="buyTime" onClick="openInputDate(this);" value="$!perList.buyTime"/></td> 					    		    
		    </tr>
		    <tr>
			    <td >购买价格:</td>
	     	    <td ><input type="text" style="margin-left: 15px;"  name="price" id="price" value="$!perList.price"/></td>			     				    
		    </tr>
		    <tr>
			    <td >最大载人数:</td>
				<td ><input type="text" style="margin-left: 15px;" name="busLoad" id="busLoad" value="$!perList.busLoad"/></td> 				    		    
		    </tr>
		    <tr>
		    	<td >最大载重(单位为T):</td>
				<td ><input type="text" style="margin-left: 15px;" name="maxLoad" id="maxLoad" value="$!perList.maxLoad"/></td>
			</tr>		    
		    <tr>
			    <td >报废年限:</td>
	     	    <td ><input type="text" style="margin-left: 15px;" name="badLimit" id="badLimit" value="$!perList.badLimit"/></td>			    	    
		    </tr>		    	       
		    <tr>
		    	<td >备注:</td>
				<td colspan="3" ><input type="text" style="margin-left: 15px;" name="remark" id="remark" style="width:606px;" value="$!perList.remark"/></td> 		
		    </tr>	    		   
		</table>
		#if("$!perList.id" == "null" || "$!perList.id" == "")
		<div id="tuBtton" class="btn btn-mini btn-danger" onclick="moreData();">更多</div>
		#end
		<div class="btn btn-mini" title="保存"  onclick="save('$!perList.id');">保存</div>
		<input type="reset" class="btn btn-mini" style="font-family:microsoft yahei;" value="重置" />	
		
		<div style="width:200px;height:200px;position:absolute;right:25%;top:0;">
			<div  id="files_preview" style="height:190px" >
				<div id="del"><img id="delImg" src="" alt="点击添加图片" 
				style="width:150px;height:145px;border:4px solid #E8E8E8;cursor:pointer;" onclick="openUploadAttach('/temp/','PIC');"/>
				</div>
				<div style ="height:18px; color:#ff0000;">
					<input type=hidden name="carPicture" id="oa_photo" >
				</div>
			</div>
		</div>		
	</div>
	
</div>
</form>
</body>

</html>