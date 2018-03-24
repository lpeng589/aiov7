<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$!name</title>
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
function save(obj){	
	var carNo = jQuery("#carNo").val();
	var Name = jQuery("#userCarPerson").val();
	var reson = jQuery("#userCarReason").val();
	var aboutDistance = jQuery("#aboutDistance").val();
	
	if(carNo==""){	
		alert("车牌号不能为空！");		
		return false;
	}
	if(Name==""){	
		alert("领用人不能为空！");		
		return false;
	}
	if(reson==""){	
		alert("事由 不能为空！");		
		return false;
	}
	if(aboutDistance!="" && (isNum(aboutDistance) || Number(aboutDistance)<0)){	
		alert("大约里程数请输入数字！");
		jQuery("#aboutDistance").val("");
		return false;
	}
	var outCarDate = new Date(Date.parse(jQuery("#outCarDate").val()));
	var overCarDate = new Date(Date.parse(jQuery("#overCarDate").val()));
	var time = new Date(); 
	if(overCarDate !="" && overCarDate<time){
		alert("结束时间时间不能小于今天！");
		return false;
	}
	if(outCarDate !="" && outCarDate>overCarDate){
		alert("结束时间不能小于领用时间");
		return false;
	}	
	if(obj !=undefined && obj != "" && obj !=null){		
		form.action="/CarOperateAction.do?operation=2&carFlay=UPDATE&id="+obj;
		form.submit();	
	}else{		
		form.submit();			
	}			
}

function isNum(str) 
{ 
	return isNaN(str); 
}
function loadData(){
	if(jQuery("#carId").val() !="" && jQuery("#carId").val() !=null && jQuery("#carId").val() !=undefined){
		jQuery("#carNo").val(jQuery("#carId").val());
	}
	if(jQuery("#checkId").val() !="" && jQuery("#checkId").val() !=null && jQuery("#checkId").val() !=undefined){
		jQuery("#approver").val(jQuery("#checkId").val());
	}
}
function backSubmit(){
	location.href="/CarOperateAction.do?operation=4";
}
function yesOrNo(obj){
	form.action="/CarOperateAction.do?operation=2&Outflag="+obj+"&id="+jQuery("#upId").val();
	form.submit();
}	
</script>
<style type="text/css">
input{margin-left:0;}
</style>
</head>
<body onload="loadData();">
<form method="post" name="form" id="form" action="/CarOperateAction.do">
<input type="hidden" name="operation" value="$globals.getOP('OP_ADD')"/>
<input type="hidden" id="carId" value="$!perList.carNo"/>
<input type="hidden" id="checkId" value="$!perList.approver"/>
<input type="hidden" name="carFlag" value="$!carFlag"/>
<input type="hidden" id="upId" name="upId" value="$!perList.id"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$!name</div>
</div>
<div id="listRange_id">
	<div  id="conter" style="width:100%;margin:0 auto;margin-top:10px;position:relative;" align="center" >
	#if("$!carFlag" == "PAI")
		<div>否决原因:</div>								
		<div><textarea rows="4" cols="4" id="reason" name="reason"></textarea> </div>	 		  
		    #else
		    <table width="100%" border="0" cellpadding="0" align="center" cellspacing="0" id="firtTbl" >
			<tr>								
				<td >车牌号:</td>
	     	    <td  >
		     	    <select name="carNo" id="carNo" style="width:153px;">
		     	    	<option value="$!perList.carNo" selected="selected">$!perList.carNo</option>
		     	    	#foreach($log in $!carList)
		     	    	<option value="$!log.carNo">$!log.carNo</option>
		     	    	#end
		     	    </select> 
			    <span style="color:Red">*</span></td>
			</tr>
			<tr>
			    <td >领用人:</td>
	     	    <td >
		    	<input  type="hidden" name="userCarPerson" id="formatFileName_id" value="$!perList.userCarPerson" />
		    	<input type="text"   id="formatFileName"  onblur="deptPop('userGroup','formatFileName','formatFileName');"
		    	onDblClick="deptPop('userGroup','formatFileName','formatFileName');" value='$globals.getEmpFullNameByUserId("$!perList.userCarPerson")'
		    	onkeypress="deptPop('userGroup','formatFileName','formatFileName');" onkeydown="deptPop('userGroup','formatFileName','formatFileName');"
		    	onkeyup="deptPop('userGroup','formatFileName','formatFileName');"/>		   	
				<img src="/style1/images/St.gif" 
				onClick="deptPop('userGroup','formatFileName','formatFileName');"class="search">									
		   		<span style="color:Red">*</span></td>
			</tr>
			<tr>
		    	<td >用车事由:</td>
				<td colspan="3" ><input type="text" name="userCarReason" id="userCarReason" style="width:606px;" value="$!perList.userCarReason"/>
				<span style="color:Red">*</span></td> 		
		    </tr>
		    <tr>
			    <td >领用日期:</td>
	     	    <td ><input type="text"  name="outCarDate" id="outCarDate" onClick="openInputDate(this);" value="$!perList.outCarDate"/></td>	
				<td >结束日期:</td>
	     	    <td ><input type="text"  name="overCarDate" id="overCarDate" onClick="openInputDate(this);" value="$!perList.overCarDate"/></td>		    		   
			</tr>		    	
		    <tr>
		    	<td >大约里程数:</td>
		    	<td >
		    	<input  type="text" name="aboutDistance" id="aboutDistance" value="$!perList.aboutDistance"/>
		    	</td>
		   		<td >目的地:</td>
	     	    <td ><input type="text"  name="destination" id="destination"  value="$!perList.destination"/></td>		   		   	    				    			
		    </tr> 		
			<tr>
			    <td >审批人:</td>
	     	    <td >
	     	    	<select name="approver" id="approver" style="width:153px;">
	     	    		<option selected="selected" ></option>
	     	    		#foreach($log in $!nameList)
	     	    		<option value='$!globals.get($!log,2)'>$!globals.get($!log,1)</option>
	     	    		#end
	     	   		</select>	    
		    </tr>
		  	
		    
		    </table>
		    #end
		    #if("$!carFlag" == "PAI")
		    <div class="btn btn-mini btn-danger" title="派车" onclick="yesOrNo('yes');">派车</div>
			<div class="btn btn-mini btn-danger" title="否决" onclick="yesOrNo('no');">否决</div>		
		    #else
		    <div class="btn btn-mini" title="发送申请"  onclick="save('$!perList.id');">发送申请</div>	
		    #end
		
	</div>
	
</div>
</form>
</body>

</html>