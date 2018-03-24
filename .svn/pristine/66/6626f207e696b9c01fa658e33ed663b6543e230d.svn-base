<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$reportName</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" /> 
<script type="text/javascript" src="/js/jquery.js"></script> 
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/jquery.divbox.js"></script>
<script type="text/javascript" src="/js/menu/jquery.bgiframe.js"></script>
<script type="text/javascript" src="/js/FusionCharts.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script> 

<script type="text/javascript" src="$globals.js("/js/function.js","",$text)"></script> 
<script type="text/javascript" src="$globals.js("/js/listGrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/k_listgrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/report.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/dropdownselect.js","",$text)"></script>
<script language="javascript">
var reportNumber = "$!reportNumber";
var MOID="$MOID";
var curloginUserId = '$LoginBean.id';

#if("$!formMenu"=="menu" && "$!showQuery"=="1")
var varOpacity = 1 ;
#end

var cols=[];
#foreach($c1 in $cols)
	cols[cols.length] = [ '$globals.get($c1,0)','$globals.get($c1,4)'];
#end

function showData(inp,selectName,fieldName,display){
	var dropdown=inp.dropdown;
	
	if(dropdown==undefined)
	{
		if(event.keyCode==13){
			if(!beforeButtonQuery())
				return false ; 
			keyQuery();
			return;
		}
    	var data = {
    	selectName:selectName,
    	operation:"DropdownPopup",
    	MOID:MOID,
    	MOOP:"query",
    	selectField:fieldName,
    	};
		dropdown = new dropDownSelect("t_"+inp.id,data,inp,selectName);
    	dropdown.retFun = fillBack;
		dropdown.showData();
		return;
	}
	if(event.keyCode == 38)
	{
		if(dropdown!=undefined)
		{
			dropdown.selectUp();
		}
		return ;
	}else if(event.keyCode==40)
	{
		if(dropdown!=undefined)
		{
			dropdown.selectDown();
		}
		return ;
	}else if(event.keyCode==13)
	{
		if(dropdown!=undefined)
		{
			dropdown.curValue();
		}
		return ;
	}else if(event.keyCode==27)
	{
		if(dropdown!=undefined)
		{
			dropdown.close();
		}
		return ;
	}
	dropdown.showData();
}


function fillBack(str,selectName,fieldName)
{
	if(typeof(str)!="undefined"){
		#foreach($row in $conditions)
			#if($!globals.get($row,3) == "4")
		if(selectName=="$!globals.get($row,6)"){
			#set($popBean=$!globals.getPopupBean($!globals.get($row,6)))
			#if($popBean.saveFields.size()==0)
				document.getElementById(fieldName).value="";
				var group=str.split("#|#") ;
				var clear=true;
				for(var i=0;i<group.length;i++){
					if(group[i].length>0){
						var content = group[i].split("#;#") ;
						if(content[0].length>0){clear=false;if(document.getElementById(fieldName)!=null)document.getElementById(fieldName).value+=content[0]+",";}
					}
				}
				if(clear){
				if(document.getElementById(fieldName)!=null)
					document.getElementById(fieldName).value="";
				}else{
				if(document.getElementById(fieldName)!=null)
					document.getElementById(fieldName).value=document.getElementById(fieldName).value.substring(0,document.getElementById(fieldName).value.length-1);
				}
				/*
				var lastObject = jQuery("#"+fieldName).parents("li");
				for(var i=0;i<10;i++)
				{
					lastObject=lastObject.next();
					if(lastObject.find(":text").size()>0)
					{
						lastObject.find(":text").focus();
						lastObject;
					}
					if(lastObject.find(":checkbox:first").size()>0)
					{
						lastObject.find(":text").focus();
						break;
					}
				} */
			#else
    			#foreach($fv in $popBean.returnFields)
    			if(document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)
    			document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value="";
    			#end
    			var group=str.split("#|#") ;
    			#if($!globals.get($row,10))
    			if(str.indexOf("#;#")!=0)
					jQuery(document.getElementById("hide_$!globals.get($row,1)")).val(str);
    			else
    			document.getElementById("hide_$!globals.get($row,1)").value="";
    			#end
    			var clear=true;
				var lastObject = null;
    			for(var i=0;i<group.length;i++){
    				if(group[i].length>0){
    					var content = group[i].split("#;#") ;
    				#foreach($fv in $popBean.returnFields)	
    					if(content[${velocityCount}-1].length>0){clear=false;if(document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value+=content[${velocityCount}-1]+',';}
						#if(${velocityCount}==$popBean.returnFields.size())
							lastObject = document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end");
						#end
    				#end
    				}
    			}
    			if(clear){
    				#foreach($fv in $popBean.returnFields)
    				if(document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)
    				document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value="";
    				#end
    			}else{
    				#foreach($fv in $popBean.returnFields)
    				if(document.getElementById("$fv.parentName")!=null)
    				document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value=document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value.substring(0,document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value.length-1) ;
    				#end
    			}
    			/*
				lastObject = jQuery(lastObject).parents("li");
				for(var i=0;i<10;i++)
				{
					lastObject=lastObject.next();
					if(lastObject.find(":text").size()>0)
					{
						lastObject.find(":text").focus();
						break;
					}
					if(lastObject.find(":checkbox:first").size()>0)
					{
						lastObject.find(":text").focus();
						break;
					}
				}*/
			#end
		}
			#end
		#end
	}
}
function submitQuery(){
	if( beforeButtonQuery()) {
			document.forms[0].submit();
	} 
}
function beforeButtonQuery(){
	document.getElementById("export").value = "" ;	
	if(!validate(form)){
		return false ;
	}
	if(document.form.query.value=='true'&&typeof(document.form.pageNo)!="undefined"){
		document.form.pageNo.value = 1;
	}
	#if($conditions.size()==0)
		document.form.queryChannel.value="";
	#else
		document.form.queryChannel.value="normal";
	#end		
	document.getElementById("isAllListQuery").value = "" ;
	
	return true;
}
var minValue = -999999999;
var maxValue = 999999999;
#foreach($row in $userConditions)
	#if($!globals.get($row,3) == "0" || $!globals.get($row,3) == "1" || $!globals.get($row,3) == "2" 
							|| $!globals.get($row,3) == "4")
		#if("$!globals.get($row,7)"=="1")
			putValidateItem("$!globals.get($row,1)","$!globals.get($row,0)","$!globals.get($row,8)","",false,minValue,maxValue,#if($!isLinkType)false #else true #end) ;
		#else
			putValidateItem("$!globals.get($row,1)","$!globals.get($row,0)","$!globals.get($row,8)","",true,minValue,maxValue,#if($!isLinkType)false #else true #end) ;
		#end
	#end
#end

#foreach($chart in $fusionList)
	#set($chartType=$globals.strSplitByFlag($chart,"@koron@"))
	var $globals.get($chartType,1)$velocityCount = "$globals.get($chartType,2)" ;
#end

function choiceChart(strSWF,strXML){
	var chartdiv = document.getElementById("chartdiv");
	var chart = new FusionCharts("flash/"+strSWF, "ChartId", "480", "240");
	chart.setDataXML(strXML);		   
	chart.render("chartdiv");
	chartdiv.style.display = "block" ;
}
function hiddenChart() {
	var chartdiv = document.getElementById("chartdiv");
	chartdiv.style.display = "none" ;
}
function setFieldValue(str,selectName,fieldName){
	var fs=str.split(";");
	#foreach($row in $conditions)
		#if($!globals.get($row,3) == "4")
			#set($popBean=$!globals.getPopupBean($!globals.get($row,6)))
			if(selectName=="$!globals.get($row,6)"){
				#if($popBean.saveFields.size()>0)
				#foreach($fv in $popBean.saveFields)
				if(document.getElementById("$fv.parentName")!=null)
			    document.getElementById("$fv.parentName").value=fs[${velocityCount}-1];
				#end
				#else
				document.getElementById(fieldName).value=fs[0];
				#end
			}
		#end
		#if($!globals.get($row,10))
			if(str.indexOf(";")!=0)
			document.getElementById("hide_$!globals.get($row,1)").value=str;
			else
			document.getElementById("hide_$!globals.get($row,1)").value="";
		#end
	#end
}

var linkStr="";

function hasSC(fieldName){
   var varValue = document.getElementById(fieldName) ;
   if(!containSC(varValue.value)){
		alert("$text.get('con.validate.contain.sc')") ;
		varValue.focus() ;
		return false;
   }
   return true ;
}
//弹出框回填字段
var hideFieldName;
var hideSelectName;
function openSelect(selectName,fieldName,display){
	hideSelectName = selectName;
	hideFieldName = fieldName;
	var urlstr = '/UserFunctionAction.do?operation=22&src=menu&reportNumber='+reportNumber+'&reportName='+encodeURI($("#reportName").val())+'&displayName='+encodeURI(display)+'&LinkType=@URL:&selectName='+selectName+encodeURI(linkStr)+"&MOID="+MOID+"&MOOP=query" ;
	
	if(urlstr.indexOf("#")!=-1){
 	 	urlstr  =urlstr.replaceAll("#","%23") ;
    }
    if(urlstr.indexOf("+")!=-1){
   	   urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
    }
	linkStr="";
	urlstr+=urlstr + "&popupWin=Popdiv";
	asyncbox.open({id:'Popdiv',title:display,url:urlstr,width:750,height:470});
}  
function exePopdiv(returnValue){
	var str = returnValue;
	var fieldName = hideFieldName;
	var selectName = hideSelectName;	
	if(str==undefined)
	str = window.returnValue;
	if(typeof(str)!="undefined"){
		#foreach($row in $conditions)
			#if($!globals.get($row,3) == "4")
		if(selectName=="$!globals.get($row,6)"){
			#set($popBean=$!globals.getPopupBean($!globals.get($row,6)))
			#if($popBean.saveFields.size()==0)
				document.getElementById(fieldName).value="";
				var group=str.split("#|#") ;
				var clear=true;
				for(var i=0;i<group.length;i++){
					if(group[i].length>0){
						var content = group[i].split("#;#") ;
						if(content[0].length>0){clear=false;if(document.getElementById(fieldName)!=null)document.getElementById(fieldName).value+=content[0]+",";}
					}
				}
				if(clear){
				if(document.getElementById(fieldName)!=null)
					document.getElementById(fieldName).value="";
				}else{
				if(document.getElementById(fieldName)!=null)
					document.getElementById(fieldName).value=document.getElementById(fieldName).value.substring(0,document.getElementById(fieldName).value.length-1);
				}
			#else
			#foreach($fv in $popBean.returnFields)
			if(document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)
			document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value="";
			#end
			var group=str.split("#|#") ;
			#if($!globals.get($row,10))
			if(str.indexOf("#;#")!=0)
			jQuery(document.getElementById("hide_$!globals.get($row,1)")).val(str);
			else
			jQuery(document.getElementById("hide_$!globals.get($row,1)")).val("");
			#end
			var clear=true;
			for(var i=0;i<group.length;i++){
				if(group[i].length>0){
					var content = group[i].split("#;#") ;
				#foreach($fv in $popBean.returnFields)	
					if(content[${velocityCount}-1].length>0){
						clear=false;
						if(document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value+=content[${velocityCount}-1]+',';
					}	
				#end
				}
			}
			if(clear){
				#foreach($fv in $popBean.returnFields)
				if(document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)
				document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value="";
				#end
			}else{
				#foreach($fv in $popBean.returnFields)
				if(document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)
				document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value=document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value.substring(0,document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value.length-1) ;
				#end
			}
			#end
		}
			#end
		#end
	}
}


//弹出框回填字段
var advancehideFieldName;
var advancehideSelectName;
function openadvanceSelect(selectName,fieldName,display){
	advancehideSelectName = selectName;
	advancehideFieldName = fieldName;
	var urlstr = '/UserFunctionAction.do?operation=22&src=menu&reportNumber='+reportNumber+'&reportName='+encodeURI($("#reportName").val())+'&displayName='+encodeURI(display)+'&LinkType=@URL:&selectName='+selectName+encodeURI(linkStr)+"&MOID="+MOID+"&MOOP=query" ;
	
	if(urlstr.indexOf("#")!=-1){
 	 	urlstr  =urlstr.replaceAll("#","%23") ;
    }
    if(urlstr.indexOf("+")!=-1){
   	   urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
    }
	linkStr="";
	urlstr+=urlstr + "&popupWin=Popadvancediv";
	asyncbox.open({id:'Popadvancediv',title:display,url:urlstr,width:750,height:470});
}  
function exePopadvancediv(returnValue){
	var str = returnValue;
	var fieldName = advancehideFieldName;
	var selectName = advancehideSelectName;	
	if(typeof(str)!="undefined"){
		#foreach($row in $conditions)
			#if($!globals.get($row,3) == "4")
		if(selectName=="$!globals.get($row,6)"){
			#set($popBean=$!globals.getPopupBean($!globals.get($row,6)))
			#if($popBean.saveFields.size()==0)
				jQuery.opener("advanceUpdatePanel").document.getElementById(fieldName).value="";
				var group=str.split("#|#") ;
				var clear=true;
				for(var i=0;i<group.length;i++){
					if(group[i].length>0){
						var content = group[i].split("#;#") ;
						if(content[0].length>0){clear=false;if(jQuery.opener("advanceUpdatePanel").document.getElementById(fieldName)!=null)jQuery.opener("advanceUpdatePanel").document.getElementById(fieldName).value+=content[0]+",";}
					}
				}
				if(clear){
				if(jQuery.opener("advanceUpdatePanel").document.getElementById(fieldName)!=null)
					jQuery.opener("advanceUpdatePanel").document.getElementById(fieldName).value="";
				}else{
				if(jQuery.opener("advanceUpdatePanel").document.getElementById(fieldName)!=null)
					jQuery.opener("advanceUpdatePanel").document.getElementById(fieldName).value=jQuery.opener("advanceUpdatePanel").document.getElementById(fieldName).value.substring(0,jQuery.opener("advanceUpdatePanel").document.getElementById(fieldName).value.length-1);
				}
			#else
			#foreach($fv in $popBean.returnFields)
			if(jQuery.opener("advanceUpdatePanel").document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)
			jQuery.opener("advanceUpdatePanel").document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value="";
			#end
			var group=str.split("#|#") ;
			#if($!globals.get($row,10))
			if(str.indexOf(";")!=0)
			jQuery(jQuery.opener("advanceUpdatePanel").document.getElementById("hide_$!globals.get($row,1)")).val(str);
			else
			jQuery(jQuery.opener("advanceUpdatePanel").document.getElementById("hide_$!globals.get($row,1)")).val("");
			#end
			var clear=true;
			for(var i=0;i<group.length;i++){
				if(group[i].length>0){
					var content = group[i].split("#;#") ;
				#foreach($fv in $popBean.returnFields)	
					if(content[${velocityCount}-1].length>0){
						clear=false;
						if(jQuery.opener("advanceUpdatePanel").document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)jQuery.opener("advanceUpdatePanel").document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value+=content[${velocityCount}-1]+',';
					}	
				#end
				}
			}
			if(clear){
				#foreach($fv in $popBean.returnFields)
				if(jQuery.opener("advanceUpdatePanel").document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)
				jQuery.opener("advanceUpdatePanel").document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value="";
				#end
			}else{
				#foreach($fv in $popBean.returnFields)
				if(jQuery.opener("advanceUpdatePanel").document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end")!=null)
				jQuery.opener("advanceUpdatePanel").document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value=jQuery.opener("advanceUpdatePanel").document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value.substring(0,jQuery.opener("advanceUpdatePanel").document.getElementById("#if($fv.parentName)$!fv.parentName#else#if($fv.asName)$fv.asName#else$!fv.fieldName#end#end").value.length-1) ;
				#end
			}
			#end
		}
			#end
		#end
	}
}

function nextClass(str){
	gotoClass(str);
}
function backClass(){
	gotoClass("$globals.classCodeSubstring("$!parentCode",5)");
}
function doChangeParent(parentCode,name,ttableName){
	if(ttableName == "tblEmployee"){
		if(parentCode.indexOf("EMP_")==0){
			$("#DepartmentCode").val("");
			$("#hide_DepartmentCode").val("");
			document.getElementById("tblDepartment.DeptFullName").value="";
			
			$("#EmployeeID").val(parentCode.substring(4));
			$("#EmpFullName").val(name);
		}else if(parentCode !=""){
			$("#EmployeeID").val("");
			$("#EmpFullName").val("");
			
			$("#DepartmentCode").val(parentCode);
			$("#hide_DepartmentCode").val(parentCode+";0;"+name+";");
			document.getElementById("tblDepartment.DeptFullName").value=name;
		}else{
			$("#DepartmentCode").val("");
			$("#hide_DepartmentCode").val("");
			document.getElementById("tblDepartment.DeptFullName").value="";
			
			$("#EmployeeID").val("");
			$("#EmpFullName").val("");
		}
		gotoClass("");
	}else{
		gotoClass(parentCode);
	}
}
function gotoClass(classCode){
	if( beforeButtonQuery()) {
		//这是下级，反回，导行链接操作时执行的方法，但是在点操作时，相应查询条件中的条件要清除，如商品分级，点击下级时，商品的查询条件要去掉
		//belongTableName
		var dataTypeObj = document.form.DataType;
		if(dataTypeObj != undefined && dataTypeObj != null){
			var dataType = dataTypeObj.value;
			if(dataType != null && dataType != ""){
				if (dataType == "StockCode")
					dataType = "tblStock";
				else if (dataType == "DepartmentCode")
					dataType = "tblDepartment";
				else if (dataType == "CompanyCode")
					dataType = "tblCompany";
				else if (dataType == "EmployeeID")
					dataType = "tblEmployee";
				$("input[belongTableName="+dataType+"]").val("");	
			}
		}
		document.form.backParent.value="true";
		document.form.parentCode.value=classCode;
   		document.forms[0].submit();
   	} 
}
function k_endClass(str){
	str = '/ReportDataAction.do?reportNumber=$!endClassNumber&mainNumber=$!reportNumber&queryChannel=normal&noback=true&src=menu&LinkType=@URL:&parentCode='+str;	
	//链接过去的未级报表的条件取主报表条件
	str+="&reportCond="+reportNumber;	
	
	var dataTypeObj = document.form.DataType;
	if(dataTypeObj != undefined && dataTypeObj != null){
		var dataType = dataTypeObj.value;
		if(dataType != null && dataType != ""){
			if (dataType == "StockCode")
				dataType = "tblStock";
			else if (dataType == "DepartmentCode")
				dataType = "tblDepartment";
			else if (dataType == "CompanyCode")
				dataType = "tblCompany";
			else if (dataType == "EmployeeID")
				dataType = "tblEmployee";
			$("input[belongTableName="+dataType+"]").each(function(){
				str += "&"+$(this).attr("name")+"=";
			})	
		}
	}

	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;	
	
	dett = $("#detTable_list").val();
	if(dett !=""){
		//存在detTable_list的情况下，说明未级报表是从一个数据表列表进入，本身权限是跟据数据表列来来的		
		str = str+"&detTable_list="+dett;
	}	
	openPop("endPopDiv","$!{reportName}$text.get("common.lb.LinearityReport")",str,width,height,false,true);
}
function endClass(str){
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;	
	if(str.indexOf("src=")  == -1){
		str = str+"&src=menu";
	}
	dett = $("#detTable_list").val();
	if(dett !=""){
		//存在detTable_list的情况下，说明未级报表是从一个数据表列表进入，本身权限是跟据数据表列来来的  
		if(str.indexOf("&detTable_list=")>0){
			pos = str.indexOf("&detTable_list=")+"&detTable_list=".length;
			pos1 = str.indexOf("&",pos);
			str = str.substring(0,pos)+dett+(pos1==-1?"":str.substring(pos1));
		}else{
			str = str+"&detTable_list="+dett;
		}
	}else{
		//不存在detTable_list加&mainNumber=
		if(str.indexOf("&mainNumber=")>0){
			pos = str.indexOf("&mainNumber=")+"&mainNumber=".length;
			pos1 = str.indexOf("&",pos);
			str = str.substring(0,pos)+reportNumber +(pos1==-1?"":str.substring(pos1));
		}else{
			str = str+"&mainNumber="+reportNumber ;
		}
	}
	openPop("endPopDiv","$!{reportName}$text.get("common.lb.LinearityReport")",str+"&noback=true",width,height,false,true);
}
function onItemDBClick(str){
	eval(str);
}
function onLockClick(str){
	AjaxRequest("/UtilServlet?operation=lockColumn&report=$!reportNumber&number="+str);		
}


function setColumnSet(str,lockName){
	if(curloginUserId == "1"){
		if(!confirm("您是超级管理员，所做列配置操作将会影响其它所有用户，是否继续？")){
			return;
		}
	}
	if(lockName == undefined)
		lockName = "";
	var strURL = "/UtilServlet?operation=colconfig&reportNumber=$!reportNumber&colType=report&lockName="+lockName+"&colSelect="+encodeURI(str)+"&reportName="+encodeURI($("#reportName").val()) ;
	AjaxRequest(strURL);
	
	if(location.href.indexOf("src=menu")!=-1)
	{
		window.location.reload();
		return;
	}
	if( beforeButtonQuery()) {
   		document.form.query.value='true'; 
   		document.forms[0].submit();  
   	} 
}
function onDefaultCol(){
	if(curloginUserId == "1"){
		if(!confirm("您是超级管理员，所做列配置操作将会影响其它所有用户，是否继续？")){
			return;
		}
	}
	var strURL = "/UtilServlet?operation=defaultConfig&reportNumber=$!reportNumber&colType=report"+"&reportName="+encodeURI($("#reportName").val()) ;
	AjaxRequest(strURL);
	if(location.href.indexOf("src=menu")!=-1)
	{
		window.location.reload();
		return;
	}
	if( beforeButtonQuery()) {
   			document.form.query.value='true'; 
   			document.forms[0].submit();  
   	} 
}

//下拉选择框,生成数据
//找出所有有外部条件的字段增加onchange事件，初始查询所有初始数据   
function initDownSelect(){       
	$!globals.getDefineJS().initQueryDownSelect($conditions) 
	//$("#functionListObj").height("95%");
} 


function getMonthStr() 
{ 
	var dd = new Date(); 
	dd.setDate(1);//获取AddDayCount天后的日期 
	var y = dd.getFullYear()+"";  
	var m = (dd.getMonth()+1)+"";//获取当前月份的日期 
	var d = dd.getDate()+""; 
	return y+"-"+(m.length==1?"0"+m:m)+"-"+(d.length==1?"0"+d:d); 
} 
var showAdvanceDIV=false;
$(document).ready(function(){
	$(".h-child-btn").mouseover(function(){
		$(this).addClass("h-height").find(".d-more").show().siblings(".br-link").show();
	}).mouseout(function(){
		$(this).removeClass("h-height").find(".d-more").hide().siblings(".br-link").hide();
	});
	//个数为0的按扭去掉
	$(".h-child-btn").each(function(){
		if($(this).find(".d-more a").size()==0){			
			$(this).parents("li").hide();
		}
	});
	

   $("#qdate_zt").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val(getDateStr(-1));
		$("#date_end").val("");
	});	
	$("#qdate_jt").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val(getDateStr(0));
		$("#date_end").val("");
	});	
	$("#qdate_yz").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val(getDateStr(-7));
		$("#date_end").val("");
	});	
	$("#qdate_yy").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val(getDateStr(-30));
		$("#date_end").val("");
	});	
	$("#qdate_by").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val(getMonthStr());
		$("#date_end").val("");
	});	
	$("#qdate_default").click(function(){ $(".curqd_selected").removeClass("curqd_selected"); $(this).addClass("curqd_selected");
		$("#date_start").val($("#qdate_default").attr("def"));
		$("#date_end").val("");
	});
	var nd = $("#date_start").val();
	if(nd == getDateStr(-1)){
		$(".curqd_selected").removeClass("curqd_selected"); $("#qdate_zt").addClass("curqd_selected");
	}else if(nd == getDateStr(0)){
		$(".curqd_selected").removeClass("curqd_selected"); $("#qdate_jt").addClass("curqd_selected");
	}else if(nd == getDateStr(-7)){
		$(".curqd_selected").removeClass("curqd_selected"); $("#qdate_yz").addClass("curqd_selected");
	}else if(nd == getDateStr(-30)){
		$(".curqd_selected").removeClass("curqd_selected"); $("#qdate_yy").addClass("curqd_selected");
	}else if(nd == getMonthStr()){
		$(".curqd_selected").removeClass("curqd_selected"); $("#qdate_by").addClass("curqd_selected");
	}
	$("#btnConfirmSCA").click(function() {
   		if( beforeButtonQuery()) {
   				document.forms[0].submit();
   		} 
   	});
   	$("#btnClearSCA").click(function() {
   		clearCondition();
   	});
   
});

function keyQuery(){
	$("#btnConfirmSCA").click();
}


function clearCondition(){
	$("#conditionDIV").find("input[type=text]").val("");
	$("#conditionDIV").find("input:not([type])").val("");
	$("#conditionDIV").find("select").val("");
	$("#conditionDIV").find("input[belongTableName]").val("");
}
function showAdvancePanel(){
	var urls = "/ReportDataAction.do?operation=advance&reportNumber=$!reportNumber";
	asyncbox.open({id:'advancePanel',title:'条件高级设置',url:urls,width:450,height:400});
}
function showAdvanceUpdatePanel(opType,keyId){
	var urls = "/ReportDataAction.do?operation=advance&opType="+opType+"&keyId="+keyId+"&reportNumber=$!reportNumber";
	//openPop('advanceUpdatePanel','条件方案设置',urls,450,500,false,false);
	var tit = '条件方案设置';
	if(opType == 'sort'){
		tit = '字段排序';
	}
	asyncbox.open({id:'advanceUpdatePanel',title:tit,url:urls,width:450,height:500});
}
function closeAdvanceUpdate(){
	jQuery.close("advanceUpdatePanel");
	jQuery.reload('advancePanel',"/ReportDataAction.do?operation=advance&reportNumber=$!reportNumber"); 
}

//储存所有条件信息
#if("$!dateConditionsStr" != "")
var dateConditions = $!dateConditionsStr; 
#end
#if("$!conditionsStr" != "")
var otherConditions = $!conditionsStr 
#end;


function showSerach(){
		$(".more-search").click(function(){
		var mShow = $(this).attr("show");
		if(mShow == "f"){
			$("#conditionDIV2").height($(".search-list-ul").height());
			$(this).attr("show","t").addClass("more-up").removeClass("more-down").attr("title","收起查询列表");
			$("#conter").css("height",$("#conter").height()-($(".search-list-ul").height()-55));
			$("#k_data").css("height",$("#k_data").height()-($(".search-list-ul").height()-55));
			$("#k_column").css("height",$("#k_column").height()-($(".search-list-ul").height()-55));
		}else{
			$("#conditionDIV2").height("55px");
			$(this).attr("show","f").addClass("more-down").removeClass("more-up").attr("title","展开查询列表");
			$("#conter").css("height",$("#conter").height()+($(".search-list-ul").height()-55));
			$("#k_data").css("height",$("#k_data").height()+($(".search-list-ul").height()-55));
			$("#k_column").css("height",$("#k_column").height()+($(".search-list-ul").height()-55));
		}
	});
}

function dochangeDataType(obj){
	var dataType = $(obj).val();
	if(parent.frames['leftTreeFrame']){
		if(dataType != null && dataType != ""){
			if (dataType == "StockCode")
				dataType = "tblStock";
			else if (dataType == "DepartmentCode")
				dataType = "tblDepartment";
			else if (dataType == "CompanyCode")
				dataType = "tblCompany";
			else if (dataType == "EmployeeID")
				dataType = "tblEmployee";
			var SysType="";
			if(dataType== "tblCompany")	{
				var checkText=$(obj).find("option:selected").text(); 
				if("客户"== checkText){
					SysType = "&SysType=Customer";
				}else if("供应商"== checkText){
					SysType = "&SysType=Supplier";
				}
			}
			parent.document.getElementById("leftTreeFrame").src="/TreeAction.do?tableName="+dataType+SysType+"&MOID="+MOID;
			//parent.frames['leftTreeFrame'].setAttribute("src","/TreeAction.do?tableName="+dataType+SysType+"&MOID="+MOID);
		}
	}
}

function reportcl(inp){
	if(inp.oldValue != undefined && inp.oldValue != inp.value ){ //当输入框值和原值不等时，清空所有录入项
		 var rid=$(inp).attr("rid");
		 var ridfn = rid.substring(4);
		 if($("#"+ridfn).val() != ""){
		 	$("input[rid="+rid+"]:not([id="+($(inp).attr("id").replaceAll("\\.","\\\."))+"])").val("");
		 }
	}	 
}


</script>
<style type="text/css">
#closeDiv{-webkit-transform:rotate(0deg);-webkit-transition:all 0.5s ease-in;}
#closeDiv:hover{-webkit-transform:rotate(360deg);}

</style>
</head> 
<body  onLoad="showStatus();initDownSelect();showSerach();" >
<form  method="post" scope="request" name="form" action="/ReportDataAction.do" onSubmit="return beforeButtonQuery();">
<input type="hidden" name="operation" id="operation" value="4"/>
<input type="hidden" name="reportNumber" id="reportNumber" value="$!reportNumber"> 
<input type="hidden" name="reportName" id="reportName" value="$!reportName"> 
<input type="hidden" name="isClickQuery" id="isClickQuery" value="true"/>
<input type="hidden" id="isAllListQuery" id="isAllListQuery" name="isAllListQuery" value="$!isAllListQuery"/>
<input type="hidden" name="reportName" id="reportName" value="$!reportName">
<input type="hidden" name="winCurIndex" id="winCurIndex" value="$!winCurIndex">
<input type="hidden" name="src" id="src" value="">
<input type="hidden" name="parentCode" id="parentCode" value="$!parentCode">
<input type="hidden" name="queryChannel" id="queryChannel" value="$!queryChannel">
<input type="hidden" name="SQLSave" id="SQLSave" value="$!SQLSave">
<input type="hidden" name="query" id="query" value="">
<input type="hidden" name="NoButton" id="NoButton" value="$!NoButton">
<input type="hidden" name="backParent" id="backParent" value="">
<input type="hidden" name="checkTab" id="checkTab" value="Y">
<input type="hidden" name="defineOrderBy" id="defineOrderBy" value="$!defineOrderBy">

<!-- 点击这里的查询后不能再加这个参数，因为此参数导致数据被url解码，第一次通过get传入后，点查询会是pos，这时候再解决就乱了  
<input type="hidden" name="LinkType" id="LinkType" value="$!LinkType">
 -->
<input type="hidden" name="export" id="export" value="">
<input type="hidden" name="noback" id="noback" value="$!noback">
<input type="hidden" name="mainNumber" id="mainNumber" value="$!mainNumber">
<input type="hidden" name="detTable_list" id="detTable_list" value="$!detTable_list">
<input type="hidden" name="moduleType" id="moduleType" value="$!moduleType">
<input type="hidden" name="popWinName" id="popWinName" value="$!popWinName">
<div class="Heading" #if("$isTran"=="yes") style="display:none" #end #if($!notShowPage) style="display:none" #end>
	<div class="HeadingTitle">
		<b class="icons b-list"></b>
		$reportName
	</div>
	<ul class="HeadingButton f-head-u"> 
	<li>
		<div class="btn btn-small h-child-btn">
			<a id="tb_toolBtn" moreId="toolMore">操作</a>
			<div id="toolMore" class="d-more">
				<div class="out-css">
				#if("$!toolBtn"!="" && "$!NoButton" != "true")
				$!toolBtn	
				#end
				</div>			
			</div>
			<b class="triangle"></b>
		</div>			
	</li>
	
	#if("$!queryBtn"!="" &&  "$!NoButton" != "true")
	<li>
		<div class="btn btn-small h-child-btn">
			<a id="tb_queryBtn" moreId="queryMore">联查</a>
			<div id="queryMore" class="d-more">
				<div class="out-css">
					$!queryBtn	
				</div>			
			</div>
			<b class="triangle"></b>
		</div>			
	</li>
	#end
	
	#set($endtrue=false)
	#if(!$!mainNumber)
		#set($mainNumber=$!reportNumber)
	#elseif($!mainNumber&&$!mainNumber.length()==0)
		#set($mainNumber=$!reportNumber)
	#else
		#set($endtrue=true)
	#end
	
	#if( $!parentCode !="" && "$!reportIfClass"=="1")
		<li><span class="btn btn-small" name="backList" onClick="backClass()">$text.get("common.lb.back")</span></li>
	#end
	#if($MOPBean.print() && "" != "true")	
		<li><span class="btn btn-small" onClick="printControl('/ReportDataAction.do?operation=printActiveX&reportNumber=$!reportNumber&mainNumber=$mainNumber&detTable_list=$!detTable_list&moduleType=$!moduleType')">$text.get("common.lb.print")</span></li>
	#end
	#if("$!reportIfClass"=="1" && "$!conditions.size()"!="0"&&$!endClassNumber&&$!endClassNumber.length()>0 && "$!NoButton" != "true")
		<li><span class="btn btn-small" name="endClass" onClick="endClass('/ReportDataAction.do?reportNumber=$!endClassNumber&mainNumber=$!reportNumber&LinkType=@URL:')" >$text.get("common.lb.showEndClass")</span></li>
	#end	
	
	 
	#if("$!NoButton" != "true")	
	<li><span class="btn btn-small" onClick="exportAll();" >$text.get("com.bill.export")</span></li>
	#end
	
		<li><span class="btn btn-small"  name="backList" onClick="closeWin('$!popWinName');" >$text.get("common.lb.close")</span></li>
	
	</ul>
</div>

<div class="listRange_frame"> 

	<div class="listRange_3" id="conditionDIV" >
		<table><tr><td>
		<div style="max-width:840px;height:55px;float:left" id="conditionDIV2" >
		<ul class="search-list-ul">
		#set($totalFields=0)
		#if("$!userDateConditions" != "") #set($totalFields=$totalFields +2)
			<li style="min-width:600px;width:75%;" class="dateQueryLi">
				<div class="swa_c1">
				<div class="d_box"><div class="d_test">$!globals.get($userDateConditions,0)</div></div></div>
				<div class="swa_c2">
					<input id="date_start" class="ls3_in" style="width:105px;" name="$!globals.get($userDateConditions,1)" date="true" value="$!globals.get($userDateConditions,3)" onKeyDown="if(event.keyCode==13){keyQuery(); }" onClick="openInputDate(this);" >
					-
					<input id="date_end" class="ls3_in" style="width:105px;" name="$!globals.get($userDateConditions,2)" date="true" value="$!globals.get($userDateConditions,4)" onKeyDown="if(event.keyCode==13){keyQuery(); }" onClick="openInputDate(this);" >
					
					<a href="javascript:void(0)" class="qd_a" id="qdate_zt">昨天</a>
					<a href="javascript:void(0)" class="qd_a"  id="qdate_jt">今天</a>
					<a href="javascript:void(0)" class="qd_a"  id="qdate_yz">7天内</a>
					<a href="javascript:void(0)" class="qd_a"  id="qdate_yy">30天内</a>
					<a href="javascript:void(0)" class="qd_a"  id="qdate_by">本月内</a>
				</div>
			</li>			
		#end

			#set($openCurrency=$globals.getSysSetting("currency"))
			#set($openSunCompany=$globals.getSysSetting("sunCompany"))
			#set($totalFields=0)
			#foreach($row in $userConditions) 
				#set($totalFields=$totalFields+1)
				#if((!$openCurrency.equals("true") && $!globals.get($row,5) == "currency") || (!$openSunCompany.equals("true") && $!globals.get($row,5) == "sunCompany"))
					<input type="hidden" name="$!globals.get($row,1)" id="$!globals.get($row,1)" value="$!globals.replaceSpecLitter($!globals.get($row,2))" >
				#else 
					#if($!globals.get($row,3) == "0")
					<li>
						<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
						<div class="swa_c2">
							<input name="$!globals.get($row,1)" class="ls3_in" id="$!globals.get($row,1)" value="$!globals.replaceSpecLitter($!globals.get($row,2))"  onKeyDown="if(event.keyCode==13){keyQuery(); }" >
						</div>
					</li>			
					#elseif($!globals.get($row,3) == "1")			
					<li>
						<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
						<div class="swa_c2">
							<select name="$!globals.get($row,1)" #if("$!globals.get($row,1)"=="DataType" || "$!globals.get($row,1)"=="DateType") onchange="dochangeDataType(this)" #end class="ls3_in"  onKeyDown="if(event.keyCode==13){keyQuery(); }">
								<option value="" ></option>
								#foreach($erow in $globals.getEnumerationItems($!globals.get($row,4)))
									<option title="$erow.name" value="$!globals.replaceSpecLitter($erow.value)" #if("$erow.value" == "$!globals.get($row,2)") selected #end>$erow.name</option>
								#end
							</select>
						</div>
					</li>
					#elseif($!globals.get($row,3) == "16")			
					<li>
						<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
						<div class="swa_c2">
							<select name="$!globals.get($row,1)" id="$!globals.get($row,1)" vl="$!globals.replaceSpecLitter($!globals.get($row,2))" class="ls3_in"  onKeyDown="if(event.keyCode==13){keyQuery(); }">
							
							</select>
						</div>
					</li>
					#elseif($!globals.get($row,3) == "5")			
					<li style="width:auto;">
						<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
						<div class="swa_c2">
							#foreach($erow in $globals.getEnumerationItems($!globals.get($row,4)))
							<input type="checkBox" style="color: #717171;width:15px;border:0px;" name="$!globals.get($row,1)" id="$!globals.get($row,1)" class="cbox" #if("$erow.value"=="$!globals.get($row,2)")checked#end value="$!globals.replaceSpecLitter($erow.value)" />
							<label for="$!globals.get($row,1)" class="cbox_w" title='$erow.name'>$erow.name</label>
							#end
						</div>
					</li>
					#elseif($!globals.get($row,3) == "2")
					<li>
						<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
						<div class="swa_c2">
							<input name="$!globals.get($row,1)" id="$!globals.get($row,1)" class="ls3_in" date="true" value="$!globals.replaceSpecLitter($!globals.get($row,2))"  onKeyDown="if(event.keyCode==13){keyQuery(); }" onClick="openInputDate(this);"  />
						</div>
					</li>			
					#elseif($!globals.get($row,3) == "3")			
						<input type="hidden" name="$!globals.get($row,1)" id="$!globals.get($row,1)" value="$!globals.replaceSpecLitter($!globals.get($row,2))" />
					#elseif($!globals.get($row,3) == "4")	
						#set($temphf="hide_"+$!globals.get($row,1))
						#set($temphf2=";"+$!globals.get($row,1)+";")
						
						#if($reportversion >1) 		
							#if($!globals.get($row,11).indexOf($temphf2)==-1)<input name="$!globals.get($row,1)" rid="rid_$!globals.get($row,1)" id="$!globals.get($row,1)" belongTableName="$!globals.get($row,13)" value="$!globals.get($row,2)" type="hidden" />#end#if("$!globals.get($row,10)" !="")  <input name="hide_$!globals.get($row,1)" id="hide_$!globals.get($row,1)"  rid="rid_$!globals.get($row,1)"  belongTableName="$!globals.get($row,13)"  value="$!request.getAttribute($temphf)" type="hidden" />#end
							#foreach($fds in $!globals.split($!globals.get($row,11),"shu"))
							#set($items = $fds.split("#;#"))
							<li>
								<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($items,0)</div></div></div>
								<div class="swa_c2">
									<input name="$!globals.get($items,1)" id="$!globals.get($items,1)"  rid="rid_$!globals.get($row,1)" class="ls3_in" value="$!globals.get($items,2)"  belongTableName="$!globals.get($row,13)"   onDblClick="openSelect('$!globals.get($row,6)','$!globals.get($items,1)','$!globals.get($row,0)');" onFocus="this.oldValue=this.value;this.select();" onKeyUp="showData(this,'$!globals.get($row,6)','$!globals.get($items,1)','$!globals.get($row,0)');"    onblur="cl(this);"/>
									<b class="stBtn icon16" onClick="openSelect('$!globals.get($row,6)','$!globals.get($items,1)','$!globals.get($row,0)');" ></b>
								</div>
							</li>
							#end
						#else 
						<li> 
							<div class="swa_c1" #if($!globals.get($row,7)==1) style="color:#f13d3d;" #end><div class="d_box"><div class="d_test">$!globals.get($row,0)</div></div></div>
							<div class="swa_c2">
								#if("$!globals.get($row,10)" != "") 
								<input name="hide_$!globals.get($row,1)" id="hide_$!globals.get($row,1)"  belongTableName="$!globals.get($row,13)"   value="$!globals.replaceSpecLitter($!request.getAttribute($temphf))" type="hidden"/>
								#end
								<input name="$!globals.get($row,1)" class="ls3_in" id="$!globals.get($row,1)"  belongTableName="$!globals.get($row,13)"  value="$!globals.replaceSpecLitter($!globals.get($row,2))" onDblClick="openSelect('$!globals.get($row,6)','$!globals.get($row,1)','$!globals.get($row,0)');"  onFocus="this.oldValue=this.value;this.select();" onKeyUp="showData(this,'$!globals.get($row,6)','$!globals.get($items,1)','$!globals.get($row,0)');"   relationkey="true" onblur="cl(this);"/>
								<b class="stBtn icon16" onClick="openSelect('$!globals.get($row,6)','$!globals.get($row,1)','$!globals.get($row,0)');"></b>
							</div>
						</li>			
						#end					
					#end
				#end
			#end
			#if("$!fieldType"!="")
			<li>
				<div class="swa_c1"><div class="d_box"><div class="d_test">$text.get("common.lb.statisticsField")</div></div></div>
				<div class="swa_c2">
					<select name="fieldType"  onKeyDown="if(event.keyCode==13){keyQuery(); }">
							#foreach($field in $statFields)
								<option title="$field.fieldName" value="$field.fieldName" #if($fieldType==$field.fieldName)selected#end>$field.display</option>
							#end
					</select>
				</div>
			</li>
			#end
			<div class="clear"></div>
			</ul>
	</div>		
	</td><td style="width: 200px;vertical-align: top;">				
	<ul class="floatleft b-btns-ul"  id="conditionDIV3">
    	<li>
    		<span id="btnConfirmSCA"  class="floatleft more-txt" >查询</span>
    		<span id="btnClearSCA"  class="floatleft more-txt" style="border-radius: 0 0 0 0;" >清空</span>
    		<span id="btnAdanceSCA"  class="floatleft more-txt" onclick="showAdvancePanel();" style="border-radius: 0 0 0 0;" >高级</span>
	        <span class="floatleft icons more-search more-down" show="f" title="展开搜索列表" onselectstart="return false;"></span>
	   </li>
    </ul>
    <div class="clear"></div>
    </td></tr></table>	
</div>

#if($parentName.length()>0)			
	<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span></div>
#end	

#if($fusionList.size()>0)
<div class="scroll_function_small_a" id="conter" style="overflow:hidden;">
	<div>
	<a href="javascript:hiddenChart();" title="$text.get("report.title.hideGraphReportForm")">$text.get("report.lb.GraphReportForm")
	<img src="/$globals.getStylePath()/images/oabbs/top.gif">：</a>
	#foreach($chart in $fusionList)
		#set($chartType=$globals.strSplitByFlag($chart,"@koron@"))
		<a href="javascript:choiceChart('FCF_${globals.get($chartType,1)}.swf',$globals.get($chartType,1)$velocityCount)">$globals.get($chartType,0)</a>
	#end
	</div>
<div id="chartdiv"></div>
</DIV>
#else
<div class="scroll_function_small_a" id="conter"  style="overflow:hidden">
<div style="width:100%;height:100%;overflow:hidden;position:relative">
	$!tableList
</div>
</div>

<script language="javascript">
var condHeight= 55;
jQuery(function(){
	jQuery("#k_data").height($("#conter").height() -10);
	jQuery("#k_column").height(jQuery("#k_data")[0].clientHeight);
	jQuery("#k_data tbody tr").click(function(){jQuery("#k_column tbody").find("tr:eq("+jQuery(this).index()+")").toggleClass("highlightrow");jQuery(this).toggleClass("highlightrow");});
	jQuery("#k_column tbody tr").click(function(){jQuery("#k_data tbody").find("tr:eq("+jQuery(this).index()+")").toggleClass("highlightrow");jQuery(this).toggleClass("highlightrow");});
	jQuery("#k_head").width(jQuery("#k_data")[0].clientWidth);
	jQuery("#k_column").width(jQuery("#k_column>table").outerWidth(true));
	jQuery("#kt_head").width(jQuery("#k_column>table").outerWidth(true));
	drag22();
	popMenu();
	jQuery("#k_data").scroll(function () { 
		jQuery("#k_head").scrollLeft(jQuery("#k_data").scrollLeft()); 
		jQuery("#k_column").scrollTop(jQuery("#k_data").scrollTop()); 
	}); 
});		

cll = 0;
$("#k_data >table >tbody").find("tr").each( function(){
	if(cll ==0){
		$(this).addClass("spaceRow");
		cll = 1;
	}else{
		cll = 0;
	}
});
cll = 0;
$("#k_column >table >tbody").find("tr").each( function(){
	if(cll ==0){
		$(this).addClass("spaceRow");
		cll = 1;
	}else{
		cll = 0;
	}
});

//如果没有条件，则条件框隐藏
if($("#conditionDIV2 ul li").size() ==0){
	$("#conditionDIV").hide();
}

</script>

#end

<script type="text/javascript">

var oDiv=document.getElementById("conter");
var dHeight=document.documentElement.clientHeight;
var varHeight=document.getElementById("listid");
var sHeight = 0 ;
if(typeof(varHeight)!="undefined" && varHeight!=null){
	sHeight = varHeight.clientHeight ;
}
#if($parentName.length()>0)
oDiv.style.height=dHeight-sHeight-100-condHeight +"px";
#else 
oDiv.style.height=dHeight-sHeight-75-condHeight +"px";
#end

</script> 
		
	<div id="topReportDiv" style='width:100%;'>	</div>	
	<div id="leftReportDiv"  style='float:left;width:48%;'>	</div>	
	<div id="rightReportDiv" style='float:left;width:48%;'></div>
<div class="page-wp">
	<div class="listRange_pagebar" #if("$isTran"=="yes") style="display:none" #end> $!pageBar </div>	
</div>	
<script type="text/javascript">
	var rowCount = document.getElementsByName("keyIds").length ;
	if("$!pageSize"!=rowCount){
		var varPage = document.getElementById("nextPageSize") ;
		if(typeof(varPage)!="undefined" && varPage!=null){
			varPage.removeAttribute("href","") ;
		}
	}	
	
</script>
</form>
#if($LoginBean.id =="1")
<form method="post" name="colConfigForm" action="/ColConfigAction.do?operation=1">
<input type="hidden" name="reportNumber" id="reportNumber" value="$!reportNumber"> 
<input type="hidden" name="strAction" id="strAction" value="$globals.getModuleUrlByWinCurIndex("$!winCurIndex")&src=menu&operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex" />
<input type="hidden" name="colType" id="colType" value="report" />
<input type="hidden" name="colSelect" id="colSelect" value="" />
</form>
#end
<script>
	cyh.lableAlign();
	document.attachEvent?document.attachEvent("onclick",moreBlur):document.addEventListener("click",moreBlur,false);	
</script>

#if($parentName.length()>0)		##有分级的生成关闭目录树的功能
<div id="closeTree" class="closeTreeDiv">
	<div class="cTree" onclick="clickTree()">&nbsp;</div>
</div>
<script type="text/javascript"> 
	function clickTree(){
		cols = window.parent.document.getElementsByTagName("frameset")[0].cols; 
		if(cols=="0,*"){
			window.parent.document.getElementsByTagName("frameset")[0].cols="190,*"; 
			$(".cTree").addClass("oTree").removeClass("cTree");
		}else{
			window.parent.document.getElementsByTagName("frameset")[0].cols="0,*"; 
			$(".oTree").addClass("cTree").removeClass("oTree");
		}
	}
</script>
#end

</body>
</html>
