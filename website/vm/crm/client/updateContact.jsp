<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/crm/jquery.select.js"></script>
<script type="text/javascript" src="/js/crm/crm_contact.js"></script>
<style type="text/css">
	.bd li{height: 23px;}
	.bd li input{height: 15px;}
	select{height: 22px;width: 100px;}
</style>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/CRMClientAction.do" method="post" target="formFrame" >
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_ADD')"/>
<input type="hidden" id="type" name="type" value="addContact">
<input type="hidden" id="clientId" name="clientId" value="$clientId">
<input type="hidden" id="contactTableName" name="contactTableName" value="$contactTableName">
<input type="hidden" id="contactId" name="contactId" value="$!contactId">
<input type="hidden" id="moduleId" name="moduleId" value="$!moduleId">
<input type="hidden" id="viewId" name="viewId" value="$!viewId">
<input type="hidden" id="tableName" name="tableName" value="$!tableName">
<div class="boxbg2 subbox_w700" style="margin: 5px auto;">
<div class="subbox cf" >
  <div class="bd">
      <div class="inputbox">
        <div class="contactDiv" >
        		#foreach ($det in $!contactValueMap)
		       		#foreach ($maps in $contactMap.keySet())	
		       		#if($contactMap.get($maps).size() != 0)
		       			<input type="hidden" id="${contactTableName}_id" name="${contactTableName}_id" value='$!det.get("id")'>
			       		<div id="contacts">
				        <h4 style="height: 10px;"><span style="float: left;">主联系人</span><input  type="checkbox" onclick="checkMainuser(this)" style="float: left;" #if("$!det.get('mainUser')" == "1") checked="checked" #end><input type="hidden" name="${contactTableName}_mainUser" value="$!det.get('mainUser')"></h4>
				        <ul>
				        #foreach ($fieldNames in $contactMap.get($maps))
				        	#set($row = $globals.getFieldBean($contactTableName,$fieldNames))
				        	#if("$row.inputType" == "0" )
				        		#if("$row.fieldType" == "1")
				        			<li><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!globals.dealDoubleDigits('$!det.get("$row.fieldName")',"amount")" /></li>
				        		#elseif("$row.fieldType" == "5")
			        		 		<li ><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date"   onClick="WdatePicker({lang:'$globals.getLocale()'})" value='$!det.get("$row.fieldName")'/></li>
			        		 	#elseif("$row.fieldType" == "6")
			        		   		<li ><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date"   onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});"  value='$!det.get("$row.fieldName")'/></li>
			        		 	#elseif("$row.fieldType" == "3" || "$row.fieldType" == "16")
			        		 		<li><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value='$!det.get("$row.fieldName")' /></li>
			        		 	#elseif("$row.fieldType" == "18")
			        		 		<li style="width: 670px;"><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" style="width: 505px;" class="inp_w" value='$!det.get("$row.fieldName")' /></li>
			        		 	#else
						        	<li><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w"  value='$!det.get("$row.fieldName")' /></li>
								#end
							#elseif("$row.inputType" == "1" && "$row.fieldName" != "mainUser")
								 <li>
			        		 	 <span isnull="$row.isNull">#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
				        		 	 <select  name="${contactTableName}_$row.fieldName"  id="$row.fieldName" enumerName="$row.refEnumerationName">
				        		 	 	 <option value="">--选择$row.display.get("$globals.getLocale()")--</option>
						             	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
						        	       <option value="$item.value"#if($!det.get("$row.fieldName") == "$item.value") selected="selected" #end>$item.name</option>
								         #end
						             </select>	
					        	 </li>	
					        #else
							#end
						#end
				        </ul>
				        </div>
			        #end
			    	#end
        		#end
	   		<p></p>
   		</div>
      </div>
  </div>
</div>
</div>

</form>
#if("$!isConsole"=="true")
		<a onclick="beforeSubmit();">提交</a>
	#end
</body>
</html>
