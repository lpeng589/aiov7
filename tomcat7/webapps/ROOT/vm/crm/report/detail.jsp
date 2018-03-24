<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<link type="text/css" rel="stylesheet" href="/style/css/crmReport.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
</style>
     
<script type="text/javascript">
	function pageSubmit(pageNo){
		$("#pageNo").val(pageNo);
		form.submit();
	}
	/*模糊查询*/
	function submitQuery(){
		form.submit();
	}
	
	function showDeatil(tableName,keyId,showName,moduleId){
		if(tableName=="CRMSaleContractDet"){
			tableName="CRMSaleContract";
		}
		if(moduleId == ""){
			var url = '/CRMBrotherAction.do?tableName='+tableName+'&operation=5&type=detailNew&keyId='+keyId;
			if(tableName == "CRMsalesQuot" || tableName == "CRMSaleContract"){
				//合同与销售机会打开新页面

				mdiwin(url,showName);
			}else{
				parent.brotherDetail(url,showName);
			}
		}else{
			mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+keyId+'&moduleId='+moduleId+"&viewId=1_"+moduleId,showName);
		}
	}
	
	function bgColorChange(obj,isOut){
	if(isOut == "true"){
		$(obj).removeClass("mulTr");
	}else{
		$("#mulConTable tr").removeClass("mulTr");
		$(obj).addClass("mulTr");
	}
}
</script>
</head>

<body >
<form action="/CRMReportAction.do" method="post" name="form">
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_QUERY')"/>
<input type="hidden" name="type" id="type" value="detailList"/>
<input type="hidden" name="fieldName" id="fieldName" value="$!fieldName">
<input type="hidden" name="enumVal" id="enumVal" value="$enumVal">
<input type="hidden" name="moduleId" id="moduleId" value="$!moduleId">
<input type="hidden" name="condition" id="condition" value="$condition">
<input type="hidden" name="isDetail" id="isDetail" value="true">
<input type="hidden" name="noDecoder" id="noDecoder" value="true">
<input type="hidden" name="tableName" id="tableName" value="$!tableName">

<div class="rt_table3" style="height: 270px;overflow: auto;">
<table border="1" cellspacing="0" cellpadding="0" width="100%" id="mulConTable">
<thead>
<tr>
#if("$!moduleId" != "")
	#foreach($col in $moduleViewBean.listFields.split(","))
		#set($field=$globals.getField($col,$clientTableName,$contactTableName))
		<td style="white-space: nowrap;">&nbsp;#if("$col" != "labelId") $globals.getColName($col,$clientTableName,$contactTableName) #end&nbsp;</td>
	#end
#elseif("$!fieldDisplayBean" !="")
	#foreach($fieldName in $fieldDisplayBean.listFields.split(","))
		#set($fieldBean = $globals.getFieldBean($tableName,$fieldName))
		<td style="white-space: nowrap;">&nbsp;$fieldBean.display.get("$globals.getLocale()")&nbsp;</td>
	#end
#else
	#foreach($col in $fieldlist)
		<td class="col_hy" style="white-space: nowrap;">&nbsp;$col.display&nbsp;</td>
	#end
#end
</tr>
</thead>
<tbody>

#foreach($client in $rsList)
#set($clientName = $!client.get("ClientName"))
#if("$!tableName" == "CRMcomplaints")
	#set($clientName = $!client.get("CompanyName"))
#end
<tr  onmouseover="bgColorChange(this,'')" onmouseout="bgColorChange(this,'true')" ondblclick="showDeatil('$!tableName','$!client.get("id")','$clientName','$!moduleId');">
#if("$!moduleViewBean" != "")
	#foreach($col in $moduleViewBean.listFields.split(","))
		#set($field=$globals.getField($col,$clientTableName,$contactTableName))
		#if("$!field.fieldName"=="createBy")
			<td >&nbsp;$globals.getEmpFullNameByUserId($!client.get("$field.fieldName"))&nbsp;</td>
		#elseif("$!field.fieldName"=="createTime"  || "$!field.fieldName"=="lastUpdateTime")
			<td >&nbsp;#if("$!client.get($field.fieldName)"!="")$!client.get("$field.fieldName").substring(0,10) #end&nbsp;</td>
		#elseif("$field.inputType" == "0")
			#if("$field.fieldName" == "Trade")
				<td >&nbsp;#if("$!client.get($field.fieldName)" == "") &nbsp; #else $!workProfessionMap.get($!client.get($field.fieldName)) #end&nbsp;</td>
			#elseif("$field.fieldName" == "District")
				<td >&nbsp;#if("$!client.get($field.fieldName)" == "") &nbsp; #else $!districtMap.get($!client.get($field.fieldName)) #end&nbsp;</td>
			#else
				<td #if("$field.fieldName" == "ClientName") onclick="showDeatil('$!tableName','$!client.get("id")','$!client.get("ClientName")','$!moduleId');" style="cursor: pointer;text-align: left;" title='$!client.get("$field.fieldName")' #end >&nbsp;#if("$!client.get($field.fieldName)" == "") &nbsp; #else <span #if("$field.fieldName" == "ClientName") style="color: blue;" #end> $!client.get($field.fieldName)</span> #end&nbsp;</td>
			#end
		#elseif("$!field.inputType"=="1")
			<td >
			#if("$!field.fieldName"=="LastContractTime")
				#if("$!client.get($field.fieldName)"!="")$!client.get("$field.fieldName").substring(0,10) #end
			#else
				&nbsp;#if("$!client.get($field.fieldName)" == "") &nbsp; #else $!globals.getEnumerationItemsDisplay("$field.refEnumerationName","$!client.get($field.fieldName)") #end&nbsp;
			#end
			</td>
		#elseif("$field.inputType" == "14")
			#set($deptVal = "")
			#foreach($row in $!client.get($field.fieldName).split(","))
				#if("$row" != "")
					#set($deptVal = $deptVal + $deptMap.get("$row") +",")
				#end
			#end
			<td >&nbsp;#if("$!client.get($field.fieldName)" == "") &nbsp; #else $deptVal #end&nbsp;</td>
		#elseif("$field.inputType" == "15")
		 	#set($empVal = "")
			#foreach($row in $!client.get($field.fieldName).split(","))
				#if("$row" != "")
					#set($empVal = $empVal + $globals.getEmpFullNameByUserId($row) +",")
				#end
			#end
			<td >&nbsp;#if("$!client.get($field.fieldName)" == "") &nbsp; #else $empVal #end&nbsp;</td>
		#elseif("$field.inputType" == "5")
			<td>&nbsp;
			 	#foreach($item in $!client.get($field.fieldName).split(","))
   			 		$globals.getEnumerationItemsDisplay("$field.refEnumerationName","$item"),
   			 	#end&nbsp;
			</td>
		#else
			<td >&nbsp;#if("$!client.get($field.fieldName)" == "") &nbsp; #else $!client.get("$field.fieldName") #end&nbsp;</td>
		#end
		
	#end
#elseif("$!fieldDisplayBean" !="")
	#foreach($fieldName in $fieldDisplayBean.listFields.split(","))
		#set($field = $globals.getFieldBean($tableName,$fieldName))
		#set($fieldVal = "")
		#if("$field.inputType"=="0")
			#if("$field.fieldType" == "1")
				#set($fieldVal = $!globals.dealDoubleDigits("$!client.get($field.fieldName)","amount"))
			#elseif("$field.fieldType" == "18" || "$field.fieldType" == "3" || "$field.fieldType" == "16")
				#set($fieldVal = $globals.replaceHTML($!client.get($field.fieldName)))
			#elseif("$fieldBean.fieldType" == "13"||"$fieldBean.fieldType" == "14")
			
			#else
				#set($fieldVal = $!client.get($field.fieldName))
			#end
		#elseif("$field.inputType"=="1" || "$field.inputType"=="5")
			#set($fieldVal = $globals.getEnumerationItemsDisplay("$field.refEnumerationName","$!client.get($field.fieldName)"))	
		#elseif("$field.inputType"=="2")
			#if("$field.fieldName" == "ClientId")
				#set($fieldVal = $!client.get("clientName"))
			#else
				#set($fieldVal = $!client.get($field.fieldName))
			#end
		#elseif("$field.inputType"=="14")
			#set($fieldVal = $!deptMap.get($!client.get($field.fieldName)))
		#elseif("$field.inputType"=="15")
			#set($fieldVal = $!globals.getEmpFullNameByUserId($!client.get($field.fieldName)))
		#elseif("$field.inputType"=="20")
			#set($fieldVal = $!relateClientMap.get($!client.get($field.fieldName)))
		#else
			#set($fieldVal = $!client.get($field.fieldName))
		#end
		
		#if("$field.fieldName"=="ClientId")
			<td onclick="showDeatil('$tableName','$client.get("id")','$fieldVal','');" style="cursor: pointer;text-align: left;color: blue;" title='$fieldVal' ><span style="color: blue;"> 
			</span>$fieldVal&nbsp;</td>
		#else
			<td #if("$field.fieldName"=="Content")style="cursor: pointer;text-align: left;"#end>$fieldVal&nbsp;</td>
		#end
	#end
#else
	#foreach($col in $fieldlist)
		#if("$!col.inputType"=="1")
			<td >#if("$!client.get($col.asFieldName)" == "") &nbsp; #else $!globals.getEnumerationItemsDisplay("$!col.popUpName","$!client.get($col.asFieldName)") #end&nbsp;</td>
		#else
			<td #if("$col.asFieldName" == "ClientName") onclick="showDeatil('$!tableName','$!client.get("id")','$clientName','$!moduleId');"  title='$!client.get("$col.asFieldName")' #end #if("$col.asFieldName" == "ClientName"||"$col.asFieldName" == "Content")style="cursor: pointer;text-align: left;"#end>#if("$!client.get($col.asFieldName)" == "") &nbsp; #else <span #if("$col.asFieldName" == "ClientName") style="color: blue;" #end> 
			#if("$!col.fieldType"=="1")
				#set($val = $!client.get("$col.asFieldName"))
				$!globals.dealDoubleDigits("$val","$col.asFieldName")
			#else
				$!client.get("$col.asFieldName")	
			#end
			</span> #end&nbsp;</td>
		#end
	#end
#end

</tr>
#end
</tbody>
</table>
</div>
$!pageBar
</form>
</body>
</html>
