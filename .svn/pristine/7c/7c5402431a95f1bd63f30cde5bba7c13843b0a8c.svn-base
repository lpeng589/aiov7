<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/crm_operation.js"></script>
<script type="text/javascript" src="/js/crm/crm_attachUpload.vjs"></script>
<script type="text/javascript" src="/js/crm/jquery.select.js"></script>
<input type="hidden" id="nowHead" value="$!nowHead" />
<style type="text/css">
	.bd li{height: 23px;}
	.bd li input{height: 15px;}
	select{height: 22px;width: 100px;}
</style>
<script type="text/javascript">
	var con_mobile = "";
	var con_tel = "";
	var con_email = "";
	
	jQuery(function() {
  		jQuery("font").attr("class","notNull");//若有font表示有必填项,加个class为not null
  		
  		var clientTitle = jQuery('#head1').val();
  		if(clientTitle == ""){
  			clientTitle = "公司信息";
  		}
  		jQuery("#clientTitle").html(clientTitle);//获得客户分组第一组的名称
  		
  		if("$!addContact" != "true"){
  			//判断哪些字段为可读










			var readField = jQuery("#readField").val();//可读字段
	  		for(var i=0;i<jQuery(".bd input").length;i++){
	  			
	  			if($("#readField").val().indexOf(jQuery(".bd input").eq(i).attr("id")) != -1){
	  				jQuery(".bd input").eq(i).attr("readonly","true");
	  				jQuery(".bd input").eq(i).css("backgroundColor","buttonface");
	  				if(jQuery(".bd input").eq(i).attr("class") == "inp_w inp_date"){
	  					jQuery(".bd input").eq(i).attr("onclick",'');
	  				}
	  			}
	  		}
	  		
	  		//若字段设为只读值为空，不控制










	  		var contactEmpty = "0"
	  		$("div[id='contacts']").each(function(i){
	  			jQuery(this).find("input").each(function(){
	  				if(jQuery(this).val() != ""){
	  					contactEmpty = "1";
		  				return false;
	  				}  				
	  			});
	  			if(contactEmpty == "0"){
	  				jQuery(this).find("input").removeProp("readonly")
	  				jQuery(this).find("input").css("backgroundColor","")
	  			}
	  			contactEmpty = "0";
	  		})
	  		
	  		jQuery("#contacts input").each(function(i){
	  			if($(this).attr("id") == "Mobile"){
	  				con_mobile += $(this).val() + ","; 
	  			}else if($(this).attr("id") == "Telephone"){
	  				con_tel += $(this).prev().prev().val() + "-" + $(this).val();
	  			}else if($(this).attr("id") == "ClientEmail"){
	  				con_email += $(this).val() + ",";
	  			}
	  		})
	  		
  		}
  		//用于详情点击修改进入
  		if(jQuery("#nowHead").val() == "2"){
  			showInfo('contact');
  		}else if(jQuery("#nowHead").val() == "3"){
  			showInfo('addfix')
  		}
  		jQuery("div[id='contacts']").each(function(){
  			jQuery(this).find(".menu_select").remove();
	  		//addSelectDiv(jQuery(this));
  		})		
	});
	var MOID='$MOID';
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/CRMClientAction.do" method="post" target="formFrame" >
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')"/>
<input type="hidden" name="clientCols" id="clientCols" value="$!clientRecords"/>
<input type="hidden" name="contactCols" id="contactCols" value="$!contactRecords" />
<input type="hidden" name="isAttachment" id="isAttachment" value="$!showAttachment" />
<input type="hidden" name="readField" id="readField" value="$!readField" />
<input type="hidden" name="clientId" value="$!keyId"/>
<input type="hidden" name="createBy" value="$!result.get('createBy')"/>
<input type="hidden" name="lastUpdateBy" value="$!result.get('lastUpdateBy')"/>
<input type="hidden" name="createTime" value="$!result.get('createTime')""/>
<input type="hidden" name="workFlowNode" value="$!result.get('workFlowNode')"/>
<input type="hidden" name="workFlowNodeName" value="$!result.get('workFlowNodeName')"/>
<input type="hidden" name="SCompanyID" value="$!result.get('SCompanyID')""/>
<input type="hidden" name="ModuleId" id="ModuleId" value="$!result.get('ModuleId')" />
<input type="hidden" name="viewId" id="viewId" value="$!viewId" />
<input type="hidden" name="cliName" id="cliName" value="$!result.get('ClientName')" />
<input type="hidden" name="CheckPersont" id="CheckPersont" value="$!result.get('CheckPersont')" />
<input type="hidden" name="classCode" id="classCode" value="$!result.get('classCode')" />
<input type="hidden" name="finishTime" id="finishTime" value="$!result.get('finishTime')" />
<input type="hidden" name="handFlag" id="handFlag" value="$!result.get('handFlag')" />
<input type="hidden" name="shareFlag" id="shareFlag" value="$!result.get('shareFlag')" />
<input type="hidden" name="statusId" id="statusId" value="$!result.get('statusId')" />
<input type="hidden" name="NextFollowTime" id="NextFollowTime" value="$!result.get('NextFollowTime')" />
<input type="hidden" name="noRelationDay" id="noRelationDay" value="$!result.get('noRelationDay')" />
<input type="hidden" name="TransferDate" id="TransferDate" value="$!result.get('TransferDate')" />
<input type="hidden" name="Transferer" id="Transferer" value="$!result.get('Transferer')" />
<input type="hidden" id="nowHead" value="$!nowHead" />
<input type="hidden" id="linkMan" value="$!linkMan" />
<input type="hidden" id="openSelectName" name="openSelectName" value=""/>
<input type="hidden" id="approveBefore" name="approveBefore" value=""/>
<div class="boxbg2 subbox_w700" style="height:400px;overflow:hidden;overflow-y:auto;width:100%;">
<div class="subbox cf" style="width:700px;margin:0 auto;" >
  #if("$!linkMan" != "true")	
  <div class="operate operate2" >
  <ul>
  <li id="clientHead" class="sel" ><a href="#" onclick="showInfo('client')" ><span id="clientTitle"></span></a></li>
  #if($!contactMap.get("contact").size()>0)<li id="contactHead"><a href="#" onclick="showInfo('contact')" >联系人信息</a></li>#end
  <li id="affixHead" style="#if("$!showAttachment" != "true")display: none; #end"><a href="#" onclick="showInfo('addfix')">附件</a></li>
  </ul>
  </div>
  #end
  <div class="head_btns">
  #if("$!OAWorkFlow" == "true"&&$result.get("workFlowNode")!="-1"&&$result.get("checkPersons").indexOf(";${loginBean.id};")>-1)
		#set($keyId = $result.get("id"))
		<div class="btn btn-small rf" onclick="approveBeforeAdd()">审核</div>
  #end
  </div>
  <div class="bd">
      <div class="inputbox">
      <div id="clientAllDiv">
        #set($firstEnter = "true")
        #set($showAttachment = "false") 
        #set($workFlowNotNullFieldsStr = ","+$!workFlowNotNullFields)
      	#foreach ($maps in $clientMap.keySet())	
      	#if($clientMap.get($maps).size() != 0)	
	        <div class="clientDiv">
	        	#if($velocityCount ==1)
	        		<input type="hidden" id="head1" value="$globals.getEnumerationItemsDisplay("$tableName","$maps")"/>
	        	#else
	        		<h4> $globals.getEnumerationItemsDisplay("$tableName","$maps") </h4>  
	        	#end
		        <ul class="mainContent">
		        #foreach ($fieldName in $clientMap.get($maps))
		        	#set($row = $globals.getFieldBean($tableName,$fieldName))
		        	
		        	#set($readStr = '')
		        	#if($!readMainFieldsStr.indexOf(",${fieldName},")>-1)
		        		#if("$row.inputType" == "1")
		        			#set($readStr = ' style="display:none" ')
		        		#else
			        		#set($readStr = ' readonly="readonly" ')
		        		#end
		        	#end
		        	
		        	#set($notNullFlag = "false")
		        	#set($fieldStr = ","+${row.fieldName}+",")
		        	#if("$row.isNull" == "1" || $!workFlowNotNullFieldsStr.indexOf($fieldStr)>-1)
		        		#set($notNullFlag = "true")
		        	#end
		        	#if("$row.inputType" == "0" )
		        		 #if("$row.fieldType" == "1")
		        		 	<li><span> #if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")： </span><input name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!globals.dealDoubleDigits("$!result.get($row.fieldName)","amount")" $readStr/></li>
		        		 #elseif("$row.fieldType" == "5")
		        		 	<li ><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date" #if("$!readStr" == "")  onClick="WdatePicker({lang:'$globals.getLocale()'})" #end value="$!result.get($row.fieldName)" readonly="readonly" /></li>
		        		 #elseif("$row.fieldType" == "6")
		        		    <li ><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date" #if("$!readStr" == "")  onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});" #end value="$!result.get($row.fieldName)" readonly="readonly"/></li>
		        		 #elseif("$row.fieldType" == "3" || "$row.fieldType" == "16")
		        		 	<li style="width: 630px;margin-bottom: 3px;height: 55px;"><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><textarea name="$row.fieldName"  id="$row.fieldName" style="width: 505px;height: 50px;" $!readStr>$!result.get($row.fieldName)</textarea></li>
		        		 #elseif("$row.fieldType" == "13" || "$row.fieldType" == "14")
		        		 	#set($showAttachment = "true") 
		        		 #elseif("$row.fieldType" == "18") 
		        		 	<li style="width: 630px;"><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" style="width: 505px;" value="$!result.get($row.fieldName)" $!readStr/></li>
		        		 #else
		        		 	#if("$row.fieldName" == "Trade")
		        		 	<input type="hidden" name="Trade" id="Trade" value="$!result.get($row.fieldName)"/>
				    		 <li>
				    	 		<span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span>
				    	 		 <div><input  name="tradeName"  id="tradeName" type="text" value="$!workProfessionMap.get($!result.get($row.fieldName))" class="inp_w" #if("$!readStr" == "") ondblclick="openTrade()" #end style="border-right: 0;width: 168px;" readonly="readonly"/><a href="#" #if("$!readStr" == "") onclick="openTrade()" #end style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%"></a> </div>
				    	 	</li> 
				    		#elseif("$row.fieldName" == "District")
				    		<input type="hidden" name="District" id="District" value="$!result.get($row.fieldName)" />
				    	 	<li>
				    	 		<span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span>
				    	 		<div><input  name="districtName"  id="districtName" value="$!districtMap.get($!result.get($row.fieldName))" type="text" class="inp_w" #if("$!readStr" == "")  ondblclick="openDistrict()" #end style="border-right: 0;width: 168px;" readonly="readonly"/><a href="#" #if("$!readStr" == "") onclick="openDistrict()" #end  style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%;"></a> </div>
				    	 	</li> 
		        		 	#else
		        		 		<li><span> #if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")： </span><input name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w"  value="$!result.get($row.fieldName)" $!readStr/></li>
		        		 	#end
		        		 #end
		        	#elseif("$row.inputType" == "1")
		        		 <li>
		        		 	#set($disName="")
		        		 	 <span isnull="$row.isNull">#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
			        		 	 <select  name="$row.fieldName" $readStr id="$row.fieldName"  enumerName="$row.refEnumerationName" onchange="enumSelect(this)">
			        		 	 	 <option value="" >--请选择--</option>
					             	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
					        	       <option value="$item.value"#if("$!result.get($row.fieldName)" == "$item.value") selected="selected" #set($disName="$item.name") #end>$item.name</option>
							         #end
							         #if($loginBean.operationMap.get("/AdvanceAction.do").update())
							             <option value="add">新增$row.display.get("$globals.getLocale()")</option>
							         #end
					             </select>
					             #if($readStr.length()>0)
					             <input name="${row.fieldName}Name"  id="${row.fieldName}Name" type="text" class="inp_w"  value="$disName" readonly="readonly"/>	
					             #end
				         </li>	
				    #elseif("$row.inputType" == "2")
				    		#set($disNames = "")
				    		#set($selectName="popup_"+$row.fieldName)
				    		#set($disNames = $!defineDisMap.get("$selectName"))     
				     		<li>
					    		<input type="hidden" name="$row.fieldName" id="$row.fieldName" value="$!result.get($row.fieldName)"/>
				    	 		<span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span>
				    	 		<div><input  name="${row.fieldName}Name"  id="${row.fieldName}Name" value='$disNames' type="text" class="inp_w"  style="border-right: 0;width: 168px;" #if("$!readStr" == "") ondblclick="definePop('$row.fieldName','$!row.display.get($globals.getLocale())','$!row.inputValue','$!tableName','SelectPop')" #end readonly="readonly"/><a href="#" #if("$!readStr" == "") onclick="definePop('$row.fieldName','$!row.display.get($globals.getLocale())','$!row.inputValue','$!tableName','SelectPop')" #end style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%;"></a> </div>
				    	 	</li>
				    #elseif("$row.inputType" == "5")
				    	 <li style="width: 670px;">
		        		 	 <span isnull="$row.isNull">#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
			        		 	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
			        		 	 	 #set($itemVal = $item.value + ",")
			        		 	 	 <label class="l-cbox">
				        		 		 <input type="checkbox" name="$row.fieldName"  id="$row.fieldName" value="$item.value" #if($!result.get($row.fieldName).indexOf("$itemVal") != -1) checked="checked" #end/>
				        		 		 <i>$item.name</i>
			        		 		 </label>
			        		 	 #end
				         </li>	
				    #elseif("$row.inputType" == "10")
				    	 <li style="width: 670px;">
		        		 	 <span isnull="$row.isNull">#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
			        		 	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
			        		 	 	<label class="l-cbox">
				        		 		<input type="radio" name="$row.fieldName"  id="$row.fieldName" value="$item.value"  #if($!result.get($row.fieldName).indexOf("$item.value") != -1) checked="checked" #end />
				        		 		<i>$item.name</i>
			        		 		</label>
			        		 	 #end
				         </li>	
				    #elseif("$row.inputType" == "14")
				    		<li>
					    		<input type="hidden" name="$row.fieldName" id="$row.fieldName" value="$!result.get($row.fieldName)"/>
				    	 		<span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span>
				    	 		<div><input  name="${row.fieldName}Name"  id="${row.fieldName}Name" value='#foreach($row in $!result.get($row.fieldName).split(","))#if("$row" != "")$deptMap.get($row),#end#end' type="text" class="inp_w"  style="border-right: 0;width: 168px;"  #if("$!readStr" == "") ondblclick="deptPop('deptGroup','$row.fieldName')" #end readonly="readonly"/><a href="#"  #if("$!readStr" == "") onclick="deptPop('deptGroup','$row.fieldName')" #end style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%;"></a> </div>
				    	 	</li>
				    #elseif("$row.inputType" == "15")
				     			<input type="hidden" name="$row.fieldName" id="$row.fieldName" value="$!result.get($row.fieldName)"/>
				    	 	<li>
				    	 		<span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span>
				    	 		<div><input  name="${row.fieldName}Name"  id="${row.fieldName}Name" value='#foreach($row in $!result.get($row.fieldName).split(","))#if("$row" != "")$globals.getEmpFullNameByUserId($row),#end#end' type="text" class="inp_w"  style="border-right: 0;width: 168px;"  #if("$!readStr" == "") ondblclick="deptPop('userGroup','$row.fieldName')" #end readonly="readonly"/><a href="#"  #if("$!readStr" == "") onclick="deptPop('userGroup','$row.fieldName')" #end style="border:1px solid #bbb;height:19px;float: left;margin-top: 0px;margin-left: -3px;border-left: 0;background: url(/style1/images/St.gif)  no-repeat right 50%;"></a> </div>
				    	 	</li> 
				   #else
				    	<li>
							<span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input  name="$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" $!readStr/>	    	
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
		        			#if($!result.get("TABLENAME_${childTableName}").size() == 0)
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
															<input  name="$srow.fieldName"  id="$srow.fieldName" value="" type="text" class="inp_w"  style="border-right: 0;width: 168px;" #if("$!readStr" =="") ondblclick="definePopDet('$bean.fieldName','$!bean.display.get($globals.getLocale())','$!bean.inputValue','$childTableName',this)" #end $!readStr />
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
		        			#else
		        				#foreach ($det in $!result.get("TABLENAME_${childTableName}"))
		        					
					        		<tr>
					        			<td class="childDel" ><b class="del-sp"></b></td>
					        			#foreach($childFieldName in $moduleViewBean.childDisplayFields.split(","))
					        				#if($childFieldName.indexOf("${childTableName}")>-1)
												#set($fieldNameStr = $globals.replaceString($childFieldName,"${childTableName}_",""))
												#set($bean = $globals.getFieldBean("$childTableName",$fieldNameStr))
												#set($nameStr = $childTableName+"_"+$bean.fieldName)
												#set($defaultWidth = "90px")
												#set($fieldVal = "")
												#set($fieldVal = $det.get("$bean.fieldName"))
												#if("$bean.inputType"=="0")
													#if("$bean.fieldType"=="1")
														<td width="$defaultWidth"><input id="$nameStr" name="$nameStr" type="text" value=$!globals.dealDoubleDigits("$fieldVal","amount")> </td>
													#elseif("$bean.inputType"=="5")
														<td width="$defaultWidth"><input id="$nameStr" name="$nameStr" type="text" class="inp_w inp_date" #if("$!readStr" =="")  onClick="WdatePicker({lang:'$globals.getLocale()'})" #end value="$fieldVal" readonly="readonly"> </td>
													#elseif("$bean.inputType"=="6")
														<td width="$defaultWidth"><input id="$nameStr" name="$nameStr" type="text" #if("$!readStr" =="")  onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});" #end value="$fieldVal" readonly="readonly"> </td>
													#elseif("$bean.fieldType" == "13" || "$bean.fieldType" == "14")
													#else
														<td width="$defaultWidth"><input id="$nameStr" name="$nameStr" type="text" value="$fieldVal"> </td>
													#end
												#elseif("$bean.inputType"=="1")
													<td width="$defaultWidth">
														<select  name="$nameStr"  id="$nameStr"  enumerName="$bean.refEnumerationName">
									        		 	 	 <option value="" >--请选择--</option>
											             	 #foreach($item in $globals.getEnumerationItems("$bean.refEnumerationName"))	 
											        	       <option value="$item.value"#if("$fieldVal" == "$item.value") selected="selected" #end>$item.name</option>
													         #end
											            </select>	
										            </td>
										            
												#elseif("$bean.inputType"=="2")
													<td style="display: none;">
													<input type="text" id="$nameStr" name="$nameStr" value="$fieldVal"/>
													</td>
													#set($returnFields = "")
													#foreach($srow in $bean.getSelectBean().viewFields)
														#set($selectFieldBean = $globals.getFieldBean($srow.fieldName))
														#set($defineVal ="")
														#set($defineVal = $!det.get("$srow.fieldName"))
														#if($selectFieldBean.fieldType=="1")
															#set($defineVal = $!globals.dealDoubleDigits("$defineVal","amount"))
														#end
														#if($selectFieldBean.inputType!="100")
															<td width="$defaultWidth">
																<div class="pr">
																	<b class="mx-bs" onclick="definePopDetClick('$bean.fieldName','$!bean.display.get($globals.getLocale())','$!bean.inputValue','$childTableName',this)"></b>
																	<input  name="$srow.fieldName"  id="$srow.fieldName" value='$defineVal' title="$defineVal" type="text" class="inp_w"  style="border-right: 0;width: 168px;" #if("$!readStr" =="") ondblclick="definePopDet('$bean.fieldName','$!bean.display.get($globals.getLocale())','$!bean.inputValue','$childTableName',this)" #end $!readStr />
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
		        				#end
		        			#end
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
       		#if($!result.get("TABLENAME_${contactTableName}").size() != 0)
		       	#foreach ( $det in $!result.get("TABLENAME_${contactTableName}")) 
		       		#foreach ($maps in $contactMap.keySet())	
			       		#if($contactMap.get($maps).size() !=0)
				       		<div id="contacts" >
					        <h4 style="height: 10px;"><span style="float: left;">主联系人</span><input type="checkbox" onclick="checkMainuser(this)" style="float: left;" #if($!det.get("mainUser") == "1") checked="checked" #end><input type="hidden" name="${contactTableName}_mainUser" value='$!det.get("mainUser")'/><img src="/style1/images/Add_button.gif" title=" $text.get('common.add.contact.person')" style="float: right;margin-top: -5px;cursor: pointer;width: 18px;height: 18px;" onclick="addClient();"/> <img src="/style1/images/delete_button.gif" title=" $text.get('common.lb.del')" style="float: right;margin-top:-4px;  cursor: pointer;width: 18px;height: 18px; #if("$!isConDel" == "true") display: none; #end " onclick="delClient(this);" id="delImg"/> </h4>
			        		<input name="${contactTableName}_id"  type="hidden" value="$!det.get("id")"/>
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
					        	
					        	#set($notNullFlag = "false")
					        	#set($fieldStr = ","+${row.fieldName}+",")
		        				#if("$row.isNull" == "1" || $!workFlowNotNullFieldsStr.indexOf($fieldStr)>-1)
					        		#set($notNullFlag = "true")
					        	#end
					        	#if("$row.inputType" == "0" )
					        		 #if("$row.fieldType" == "5")
				        		 		<li ><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date" #if("$readStr" == "")  onClick="WdatePicker({lang:'$globals.getLocale()'})" #end value="$!det.get($row.fieldName)" readonly="readonly"/></li>
				        		 	#elseif("$row.fieldType" == "6")
		        		   				<li ><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date" #if("$readStr" == "") onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});" #end  value="$!det.get($row.fieldName)" readonly="readonly"/></li>
				        		 	#elseif("$row.fieldType" == "3" || "$row.fieldType" == "16")
				        		 		<li><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!det.get($row.fieldName)" $readStr/></li>
				        		 	#elseif("$row.fieldType" == "18")
				        		 		<li style="width: 670px;"><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" style="width: 505px;" class="inp_w" value="$!det.get($row.fieldName)" $readStr/></li>
				        		 	#else
							        	<li><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!det.get($row.fieldName)" $readStr/></li>
									#end
								#elseif("$row.inputType" == "1" && "$row.fieldName" != "mainUser")
									 <li>
				        		 	 <span isnull="$row.isNull">#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
					        		 	 <select  name="${contactTableName}_$row.fieldName"  id="$row.fieldName" enumerName="$row.refEnumerationName" onchange="enumSelect(this)">
					        		 	 	 <option value="0">--选择$row.display.get("$globals.getLocale()")--</option>
							             	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
							        	       <option value="$item.value" #if("$!det.get($row.fieldName)" == "$item.value") selected="selected" #end>$item.name</option>
									         #end
									         <option value="add">新增$row.display.get("$globals.getLocale()")</option>
							             </select>	
						        	 </li>	
								#end
							#end
					        </ul>
					        </div>
				        #end
			    	#end
			    #end
        	#else
		       #foreach ($maps in $contactMap.keySet())	
		       		#if($contactMap.get($maps).size() != 0)
			       		<div id="contacts">
				        <h4 style="height: 10px;"><span style="float: left;">主联系人</span><input type="checkbox" onclick="checkMainuser(this)" style="float: left;" checked="checked"><input type="hidden" name="${contactTableName}_mainUser" value="1"><img src="/style1/images/Add_button.gif" title=" $text.get('common.add.contact.person')" style="float: right;margin-top: -5px;cursor: pointer;width: 18px;height: 18px;" onclick="addClient();"/> <img src="/style1/images/delete_button.gif" title=" $text.get('common.lb.del')" style="float: right;margin-top:-4px;  cursor: pointer;width: 18px;height: 18px;" onclick="delClient(this);"/> </h4>
				        <input name="${contactTableName}_id"  type="hidden" value=""/>
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
				        	
				        	#set($notNullFlag = "false")
				        	#set($fieldStr = ","+${row.fieldName}+",")
	        				#if("$row.isNull" == "1" || $!workFlowNotNullFieldsStr.indexOf($fieldStr)>-1)
				        		#set($notNullFlag = "true")
				        	#end
				        	#if("$row.inputType" == "0" )
				        		#if("$row.fieldType" == "1")
				        			<li><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!globals.dealDoubleDigits("$!row.defaultValue","amount")" $!readStr/></li>
				        		#elseif("$row.fieldType" == "5")
			        		 		<li ><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date" #if("$!readStr" == "") onClick="WdatePicker({lang:'$globals.getLocale()'})" #end value="$!row.defaultValue" readonly="readonly"/></li>
			        		 	#elseif("$row.fieldType" == "6")
			        		   		<li ><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")'  color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w inp_date"  #if("$!readStr" == "") onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});" #end value="$!row.defaultValue" readonly="readonly"/></li>
			        		 	#elseif("$row.fieldType" == "3" || "$row.fieldType" == "16")
			        		 		<li><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!row.defaultValue" $!readStr/></li>
			        		 	#elseif("$row.fieldType" == "18")
			        		 		<li style="width: 670px;"><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" style="width: 505px;" class="inp_w" value="$!row.defaultValue" $!readStr/></li>
			        		 	#else
					        		<li><span>#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：</span><input name="${contactTableName}_$row.fieldName"  id="$row.fieldName" type="text" class="inp_w" value="$!row.defaultValue" $!readStr/></li>
								#end
							#elseif("$row.inputType" == "1" && "$row.fieldName" != "mainUser")
								 <li>
			        		 	 <span isnull="$row.isNull">#if("$notNullFlag"=="true")<font id='$row.display.get("$globals.getLocale()")' color="red">*</font>#end$row.display.get("$globals.getLocale()")：  </span>
				        		 	 <select  name="${contactTableName}_$row.fieldName"  id="$row.fieldName" #if($row.fieldName == "Emergency") onchange="addEmergencyWhy(this);" #end enumerName="$row.refEnumerationName">
				        		 	 	 <option value="">--选择$row.display.get("$globals.getLocale()")--</option>
						             	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
						        	       <option value="$item.value"#if("$row.defaultValue" == "$item.value") selected="selected" #end>$item.name</option>
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
   		<div id="affixDiv" style="display: none;">
	       	<div class="listRange_1_photoAndDoc_1"><span></span><button name="affixbuttonthe222" type="button" onClick="upload('AFFIX');" class="b4">$text.get("upload.lb.affixupload")</button>
				<ul id="affixuploadul">
					#foreach($uprow in $AFFIX)	
						#if($uprow != "")
							<li style='background:url();' id="uploadfile_$uprow">
							<input type=hidden id="uploadaffix" name=uploadaffix value='$uprow'/><div class="showAffix">$uprow</div>&nbsp;&nbsp;&nbsp;			
							#if("$!detail" != "detail")
								<a href='javascript:deleteupload("$uprow","false","CRMClientInfo","AFFIX");'>$text.get("common.lb.del")</a>
							#end
								&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=CRMClientInfo" target="_blank">$text.get("common.lb.download")</a>
							</li>
						#end
					#end
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
