
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<style type="text/css">
.bd li{height: 23px;}
.bd li input{height: 15px;}
select{height: 22px;width: 100px;}
.clientDiv select{float:left;}
</style>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/crm_operation.js"></script>
<script type="text/javascript" src="/js/crm/crm_attachUpload.vjs"></script>
<script type="text/javascript" src="/js/crm/jquery.select.js"></script>
<input type="hidden" id="nowHead" value="$!nowHead" />
<script type="text/javascript">

	jQuery(function() {
  		jQuery("font").attr("class","notNull");
  		
  		var clientTitle = jQuery('#head1').val();
  		if(clientTitle == ""){
  			clientTitle = "公司信息";
  		}
  		jQuery("#clientTitle").html(clientTitle);
  		if(jQuery("#nowHead") != "" && jQuery("#nowHead").val() == "2") {
  			 showInfo('contact');
  		}
	  	jQuery("#contacts").find(".menu_select").remove();
		//addSelectDiv(jQuery("#contacts"));
		
		
	
	});
	
	var MOID='$MOID';
	
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body style="text-align:center;">
<form name="form" action="/CRMClientAction.do" method="post" target="formFrame" style="display:inline-block;width:100%;">
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_ADD')"/>
<input type="hidden" name="clientDetCou" id="clientDetCou" value="2"/>
<input type="hidden" name="District" id="District" />
<input type="hidden" name="Trade" id="Trade" />
<input type="hidden" name="isAttachment" id="isAttachment" value="$!showAttachment" />
<input type="hidden" name="ModuleId" id="ModuleId" value="$moduleId" />
<input type="hidden" name="nowHead" id="nowHead" value="$!nowHead" />
<input type="hidden" id="dbData" name="dbData" value="" onpropertyChange="javascript:dbDate(this.value)"/>
<input type="hidden" id="openSelectName" name="openSelectName" value=""/>
<input type="hidden" id="designId" name="designId" value="$!designId"/>
<input type="hidden" id="approveBefore" name="approveBefore" value=""/>

<div class="boxbg2 subbox_w700" style="height:400px;overflow:hidden;overflow-y:auto;width:100%;">

<div class="subbox cf" style="width:700px;margin:0 auto;">
  <div class="operate operate2" >
  <ul>
  <li id="clientHead" class="sel" ><a href="#" onclick="showInfo('client')" ><span id="clientTitle"></span></a></li>
  #if($!contactMap.get("contact").size()>0)<li id="contactHead"><a href="#" onclick="showInfo('contact')" >联系人信息</a></li>#end
  <li id="affixHead" style="#if("$!showAttachment" != "true")display: none; #end"><a href="#" onclick="showInfo('addfix')">附件</a></li>
  </ul>
  </div>
  <div class="head_btns">
	#if("$!OAWorkFlow" == "true")<div class="btn btn-small rf" onclick="approveBeforeAdd()">审核</div>#end
  </div>
  <div class="bd">
      <div class="inputbox">
      <div id="clientAllDiv">
        #set($showAttachment = "false") 
      	#foreach ($maps in $clientMap.keySet())	
      	#if($clientMap.get($maps).size() != 0)	
	        <div class="clientDiv">
	        	#if($velocityCount ==1)
		        	<input type="hidden" id="head1" value="$globals.getEnumerationItemsDisplay("$tableName","$maps")"/>
	        	#else
		        	<h4>  $globals.getEnumerationItemsDisplay("$tableName","$maps") </h4>  
	        	#end
		        <ul class="mainContent">
		        #foreach ($fieldName in $clientMap.get($maps))
		        	#set($row = $globals.getFieldBean($tableName,$fieldName))
		        	
		        	#set($readStr = '')
		        	#if($!readMainFieldsStr.indexOf(",${fieldName},")>-1)
		        		#if("$row.inputType" == "1")
		        			#set($readStr = ' readonly="readonly" ')
		        		#else
			        		#set($readStr = ' readonly="readonly" ')
		        		#end
		        	#end
		        	
		        	
		        	#if("$row.inputType" == "0" )
		        		 #if("$row.fieldType" == "1")
		        		 	<li><span> #if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")： </span><input name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!globals.dealDoubleDigits("$!row.defaultValue","amount")" $readStr/></li>
		        		 #elseif("$row.fieldType" == "5")
		        		 	<li ><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date" #if("$!readStr" =="")  onClick="WdatePicker({lang:'$globals.getLocale()'})" #end value="$globals.getHongVal($!row.defaultValue)" readonly="readonly"/></li>
		        		 #elseif("$row.fieldType" == "6")
		        		    <li ><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date" #if("$!readStr" =="")  onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});" #end value="$globals.getHongVal($!row.defaultValue)" readonly="readonly"/></li>
		        		 #elseif("$row.fieldType" == "3" || "$row.fieldType" == "16")
		        		 	<li style="width: 630px;margin-bottom: 3px;height: 105px;"><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><textarea name="$row.fieldName"  id="$row.fieldName" style="float:left;width: 505px;height: 100px;" $readStr>$!row.defaultValue</textarea></li>
		        		 #elseif("$row.fieldType" == "13" || "$row.fieldType" == "14")
		        		 	#set($showAttachment = "true") 
		        		 #elseif("$row.fieldType" == "18") 
		        		 	<li style="width: 630px;"><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" style="width: 505px;" value="$!row.defaultValue" $readStr/></li>
		        		 #else
		        		 	#if("$row.fieldName" == "Trade")
				    		 <li>
				    	 		<span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span>
				    	 		 <div><input  name="tradeName"  id="tradeName" type="text" class="inp_w" #if("$!readStr" =="") ondblclick="openTrade()" #end style="border-right: 0;width: 168px;" readonly="readonly"/><a href="#" #if("$!readStr" =="") onclick="openTrade()" #end style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%"></a></div>
				    	 	</li> 
				    		#elseif("$row.fieldName" == "District")
				    	 	<li>
				    	 		<span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span>
				    	 		<div><input  name="districtName"  id="districtName" type="text" class="inp_w" #if("$!readStr" =="") ondblclick="openDistrict()" #end style="border-right: 0;width: 168px;" readonly="readonly"/><a href="#" #if("$!readStr" =="") onclick="openDistrict()" #end style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%;"></a> </div>
				    	 	</li> 
		        		 	#else
		        		 		<li><span> #if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")： </span><input name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" #if("$row.fieldName"=="ClientNo") value='$globals.getBillNoCode("CRMClientInfo_ClientNo")' #else value="$row.defValue" #end $readStr/></li>
		        		 	#end
		        		 #end
		        	#elseif("$row.inputType" == "1")
		        		 <li>
		        		 	 <span isnull="$row.isNull">#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
			        		 	 <select  name="$row.fieldName"  id="$row.fieldName"  enumerName="$row.refEnumerationName" onchange="enumSelect(this)">
			        		 	 	 <option value="" >--请选择--</option>
					             	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
					        	       <option value="$item.value"#if("$row.defaultValue" == "$item.value") selected="selected" #end>$item.name</option>
							         #end
							         #if($loginBean.operationMap.get("/AdvanceAction.do").update())
							        	 <option value="add">新增$row.display.get("$globals.getLocale()")</option>
							         #end
					             </select>	
				         </li>	
				    #elseif("$row.inputType" == "2")     
				     		<li>
					    		<input type="hidden" name="$row.fieldName" id="$row.fieldName" value=""/>
				    	 		<span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span>
				    	 		<div><input  name="${row.fieldName}Name"  id="${row.fieldName}Name" type="text" class="inp_w"  style="border-right: 0;width: 168px;" #if("$!readStr" =="") ondblclick="definePop('$row.fieldName','$!row.display.get($globals.getLocale())','$!row.inputValue','$!tableName','SelectPop')" #end $!readStr />#if("$!readStr" =="")<a href="#" onclick="definePop('$row.fieldName','$!row.display.get($globals.getLocale())','$!row.inputValue','$!tableName','SelectPop')" style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%;"></a>#end </div>
				    	 	</li>
				    #elseif("$row.inputType" == "5")
				    	 <li style="width: 670px;">
		        		 	 <span isnull="$row.isNull">#if($row.isNull==1)
		        		 	 	<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
			        		 	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
			        		 		 <label class="l-cbox">
			        		 		 	 <input type="checkbox" name="$row.fieldName"  id="$row.fieldName" value="$item.value" #if("$row.defaultValue" == "$item.value") checked="checked" #end />
			        		 		 	 <i>$item.name</i>
			        		 		 </label>
			        		 	 #end
				         </li>	
				    #elseif("$row.inputType" == "10")
				    	 <li style="width: 670px;">
		        		 	 <span isnull="$row.isNull">#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
			        		 	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
			        		 		<label class="l-cbox">
			        		 		 	<input type="radio" name="$row.fieldName"  id="$row.fieldName" value="$item.value"  #if("$row.defaultValue" == "$item.value") checked="checked" #end />
			        		 		 	<i>$item.name</i>
			        		 		 </label>
			        		 	 #end
				         </li>	
				    #elseif("$row.inputType" == "14")
				    		<li>
					    		<input type="hidden" name="$row.fieldName" id="$row.fieldName" value=""/>
				    	 		<span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span>
				    	 		<div><input  name="${row.fieldName}Name"  id="${row.fieldName}Name" type="text" class="inp_w"  style="border-right: 0;width: 168px;" #if("$!readStr" =="") ondblclick="deptPop('deptGroup','$row.fieldName')" #end readonly="readonly"/><a href="#" #if("$!readStr" =="") onclick="deptPop('deptGroup','$row.fieldName')" #end style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%;"></a></div>
				    	 	</li>
				    #elseif("$row.inputType" == "15")
				     		<input type="hidden" name="$row.fieldName" id="$row.fieldName" value=""/>
				    	 	<li>
				    	 		<span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span>
				    	 		<div><input  name="${row.fieldName}Name"  id="${row.fieldName}Name" type="text" class="inp_w"  style="border-right: 0;width: 168px;" #if("$!readStr" =="") ondblclick="deptPop('userGroup','$row.fieldName')" #end readonly="readonly"/><a href="#" #if("$!readStr" =="") onclick="deptPop('userGroup','$row.fieldName')" #end style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%;" ></a> </div>
				    	 	</li> 
				   #else
				    	<li>
							<span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input  name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" $readStr/>	    	
		        		</li>	
		        	#end
				#end
		        </ul>
	        </div>
		    #end
        #end
        <div>
        <!-- 自定义明细开始 -->
	        <ul id="defineDetUl">
	        	#foreach($childTableName in $moduleViewBean.childTableInfo.split(","))
	        		#if("$!childTableName" !="")
	        		<span class="title-span">
	        			<em class="txt">$globals.getTableInfoBean("$childTableName").display.get("$globals.getLocale()")</em>
	        			<i class="add" onclick="addChildRow('${childTableName}')"></i>
	        		</span>  
	        		<div style="overflow:auto;">
	        		<table border="0" cellspacing="0" class="d-table" id="${childTableName}_table" tableName="${childTableName}">
	        			<thead>
			        		<tr>
			        			<td style="width:30px;">操作</td>
			        			#foreach($childFieldName in $moduleViewBean.childDisplayFields.split(","))
			        				#if($childFieldName.indexOf("${childTableName}")>-1)
										#set($fieldNameStr = $globals.replaceString($childFieldName,"${childTableName}_",""))
										#set($bean = $globals.getFieldBean("$childTableName",$fieldNameStr))
										#if("$bean.inputType"=="2")
											#foreach($srow in $bean.getSelectBean().viewFields)
												#set($selectFieldBean = $globals.getFieldBean($srow.fieldName))
												#if($selectFieldBean.inputType!="100")
													#set($dis=$globals.getLocaleDisplay("$selectFieldBean.display"))
													<td>$dis</td>
												#end
											#end
										#else
											#set($dis=$globals.getLocaleDisplay("$bean.display"))
											<td>$dis</td>
										#end
									#end
			        			#end
			        		</tr>
		        		</thead>
		        		<tbody>
			        		<tr>
			        			<td class="childDel" ><b class="del-sp"></b></td>
			        			#foreach($childFieldName in $moduleViewBean.childDisplayFields.split(","))
			        				#if($childFieldName.indexOf("${childTableName}")>-1)
										#set($fieldNameStr = $globals.replaceString($childFieldName,"${childTableName}_",""))
										#set($bean = $globals.getFieldBean("$childTableName",$fieldNameStr))
										#set($nameStr = $childTableName+"_"+$bean.fieldName)
										#set($defaultWidth = "90px")
										#if("$bean.inputType"=="0")
											#if("$bean.fieldType"=="1")
												<td width="$defaultWidth"><input id="$nameStr" name="$nameStr"  type="text"> </td>
											#elseif("$bean.inputType"=="5")
												<td width="$defaultWidth"><input id="$nameStr" name="$nameStr" type="text" class="inp_w inp_date" #if("$!readStr" =="")  onClick="WdatePicker({lang:'$globals.getLocale()'})" #end value="$globals.getHongVal($!bean.defaultValue)" readonly="readonly"> </td>
											#elseif("$bean.inputType"=="6")
												<td width="$defaultWidth"><input id="$nameStr" name="$nameStr" type="text" #if("$!readStr" =="")  onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});" #end value="$globals.getHongVal($!bean.defaultValue)" readonly="readonly"> </td>
											#elseif("$bean.fieldType" == "13" || "$bean.fieldType" == "14")
											#else
												<td width="$defaultWidth"><input id="$nameStr" name="$nameStr" type="text"> </td>
											#end
										#elseif("$bean.inputType"=="1")
											<td width="$defaultWidth">
												<select  name="$nameStr"  id="$nameStr"  enumerName="$bean.refEnumerationName">
							        		 	 	 <option value="" >--请选择--</option>
									             	 #foreach($item in $globals.getEnumerationItems("$bean.refEnumerationName"))	 
									        	       <option value="$item.value"#if("$bean.defaultValue" == "$item.value") selected="selected" #end>$item.name</option>
											         #end
									            </select>	
								            </td>
								            
										#elseif("$bean.inputType"=="2")
												<td style="display: none;">
												<input type="text" id="$nameStr" name="$nameStr" value=""/>
												</td>
												#set($returnFields = "")
												#foreach($srow in $bean.getSelectBean().viewFields)
													#set($selectFieldBean = $globals.getFieldBean($srow.fieldName))
													#if($selectFieldBean.inputType!="100")
														<td width="$defaultWidth">
															<div class="pr">
																<b class="mx-bs" onclick="definePopDetClick('$bean.fieldName','$!bean.display.get($globals.getLocale())','$!bean.inputValue','$childTableName',this)"></b>
																<input name="$srow.fieldName"  id="$srow.fieldName" value="" type="text" class="inp_w"  style="border-right: 0;width: 168px;" #if("$!readStr" =="") ondblclick="definePopDet('$bean.fieldName','$!bean.display.get($globals.getLocale())','$!bean.inputValue','$childTableName',this)" #end  readonly="readonly"/>
																#set($returnFields = $returnFields + $srow.fieldName+",")
															</div>
											            </td>
													#end
												#end
												<td style="display: none;">
												<input type="hidden" id="${bean.fieldName}ReturnFieldsStr" value="$returnFields" class="returnFields"/>
												</td>
										#elseif("$bean.inputType"=="14")
										#elseif("$bean.inputType"=="15")
										#end
									#end
			        			#end
			        		</tr>
		        		</tbody>
		        		
	        		</table>
	        		</div>
	        		#end
	        	#end
	        </ul>
	        <!-- 自定义明细结束 -->
        </div>
        </div>
        <div class="contactDiv" style="display: none;">
	       		#set($contNum = $velocityCount)
	       		#foreach ($maps in $contactMap.keySet())	
		       		<div id="contacts">
			        <h4 style="height: 10px;"><span style="float: left;">主联系人</span><input type="checkbox" onclick="checkMainuser(this)" style="float: left;" checked="checked"><input type="hidden" name="${contactTableName}_mainUser" value="1"><img src="/style1/images/Add_button.gif" title=" $text.get('common.add.contact.person')" style="float: right;margin-top: -5px;cursor: pointer;width: 18px;height: 18px;" onclick="addClient();"/> <img src="/style1/images/delete_button.gif" title=" $text.get('common.lb.del')" style="float: right;margin-top:-4px;  cursor: pointer;width: 18px;height: 18px;" onclick="delClient(this);"/> </h4>
			        <ul>
			        #foreach ($fieldNames in $contactMap.get($maps))
			        	#set($row = $globals.getFieldBean($contactTableName,$fieldNames))
			        	#set($readStr = '')
			        	#if($!readchildFieldsStr.indexOf(",${fieldNames},")>-1)
			        		#if("$row.inputType" == "1")
			        			#set($readStr = ' readonly="readonly" ')
			        		#else
				        		#set($readStr = ' readonly="readonly" ')
			        		#end
			        	#end
			        	#if("$row.inputType" == "0" )
			        		#if("$row.fieldType" == "1")
			        			<li><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!globals.dealDoubleDigits("$!row.defaultValue","amount")" $readStr/></li>
			        		#elseif("$row.fieldType" == "5")
		        		 		<li ><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date" #if("$!readStr" =="")  onClick="WdatePicker({lang:'$globals.getLocale()'})" #end value="$!row.defaultValue"  $readStr/></li>
		        		 	#elseif("$row.fieldType" == "6")
		        		   		<li ><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date" #if("$!readStr" =="")  onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});" #end value="$!row.defaultValue" $readStr/></li>
		        		 	#elseif("$row.fieldType" == "3" || "$row.fieldType" == "16")
		        		 		<li><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!row.defaultValue" $readStr/></li>
		        		 	#elseif("$row.fieldType" == "18")
		        		 		<li style="width: 670px;"><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" style="width: 505px;" class="inp_w" value="$!row.defaultValue" $readStr/></li>
		        		 	#else
					        	<li><span>#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" #if("$!email"!="" && "$row.fieldName" == "ClientEmail") value="$!email"  #else value="$!row.defaultValue" #end $readStr/></li>
							#end
						#elseif("$row.inputType" == "1" && "$row.fieldName" != "mainUser")
							 <li>
		        		 	 <span isnull="$row.isNull">#if($row.isNull==1)<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
			        		 	 <select  name="${contactTableName}_$row.fieldName"  id="$row.fieldName" enumerName="$row.refEnumerationName" onchange="enumSelect(this)">
			        		 	 	 <option value="">--选择$row.display.get("$globals.getLocale()")--</option>
					             	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
					        	       <option value="$item.value"#if("$row.defaultValue" == "$item.value") selected="selected" #end>$item.name</option>
							         #end
							         <option value="add">新增$row.display.get("$globals.getLocale()")</option>
					             </select>	
				        	 </li>	
				        #else
						#end
					#end
			        </ul>
			        </div>
		    	#end
	   		<p></p>
   		</div>
   		 <div id="affixDiv" style="display: none;">
	       	<div class="listRange_1_photoAndDoc_1"><span>#if($row.isNull==1)<font color="red">*</font>#end</span><button name="affixbuttonthe222" type="button" onClick="upload('AFFIX');" class="b4">$text.get("upload.lb.affixupload")</button>
				<ul id="affixuploadul">
				</ul>
			</div>

   		</div>
      </div>
     
  </div>
</div>
</div>

</form>

</body>
</html>
