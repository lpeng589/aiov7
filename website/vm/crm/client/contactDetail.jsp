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
<style type="text/css">
	.bd li{height: 23px;}
	.bd li input{height: 15px;}
	select{height: 22px;width: 100px;}
</style>
<script type="text/javascript">
	$(function(){
		#if("$!isConsole"=="true")
			parent.$("#billFrame").css("height",jQuery(".inputbox").height()+50)
		#end
	})
</script>	
</head>
<body>
<div class="boxbg2 subbox_w700" style="margin: 5px auto;">
<div class="subbox cf" >
  <div class="bd">
      <div class="inputbox">
        <div class="contactDiv" >
	       		#foreach ( $det in $!result.get("TABLENAME_${contactTableName}")) 
		       		#foreach ($maps in $contactMap.keySet())	
			       		#if($contactMap.get($maps).size() !=0)
				       		<div id="contacts" #if("$!addContact" == "true") style="display: none;"  #end>
					        <h4 style="height: 10px;"><span style="float: left;">主联系人</span><input type="checkbox" style="float: left;" #if($!det.get("mainUser") == "1") checked="checked" #end><input type="hidden" name="${contactTableName}_mainUser" value='$!det.get("mainUser")'/></h4>
			        		<input name="${contactTableName}_id"  type="hidden" value="$!det.get("id")"/>
			        		<ul>
					        #foreach ($fieldNames in $contactMap.get($maps))
					        	#set($row = $globals.getFieldBean($contactTableName,$fieldNames))
					        	#if("$row.inputType" == "0" )
					        		 #if("$row.fieldType" == "5")
				        		 		<li ><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date"   onClick="WdatePicker({lang:'$globals.getLocale()'})" value="$!det.get($row.fieldName)"/></li>
				        		 	#elseif("$row.fieldType" == "6")
		        		   				<li ><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date"   onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});"  value="$!det.get($row.fieldName)"/></li>
				        		 	#elseif("$row.fieldType" == "3" || "$row.fieldType" == "16")
				        		 		<li><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!det.get($row.fieldName)"/></li>
				        		 	#elseif("$row.fieldType" == "18")
				        		 		<li style="width: 670px;"><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" style="width: 505px;" class="inp_w" value="$!det.get($row.fieldName)"/></li>
				        		 	#else
							        	#if("$row.fieldName"== "ClientEmail" ||  "$row.fieldName"== "Mobile" || "$row.fieldName"== "Telephone")
							        		<li><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input  onchange="ajaxVerify('$row.fieldName',this,'update')" name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!det.get($row.fieldName)" onblur="checkExist(this)"/><div></div></li>
							      		#else
							        		<li><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!det.get($row.fieldName)"/></li>
										#end
									#end
								#elseif("$row.inputType" == "1" && "$row.fieldName" != "mainUser")
									 <li>
				        		 	 <span isnull="$row.isNull">#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
					        		 	 <select  name="${contactTableName}_$row.fieldName"  id="$row.fieldName" #if($row.fieldName == "Emergency") onchange="addEmergencyWhy(this);" #end enumerName="$row.refEnumerationName">
					        		 	 	 <option value="0">--选择$row.display.get("$globals.getLocale()")--</option>
							             	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
							        	       <option value="$item.value" #if("$!det.get($row.fieldName)" == "$item.value") selected="selected" #end>$item.name</option>
									         #end
							             </select>	
						        	 </li>	
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
</body>
</html>
