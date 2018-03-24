<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/oaCalendarAdd.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/brother_add.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/crm/crm_brother.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/oa/oaCalendarAdd.js"></script>
<script type="text/javascript">
	$(function(){
		var lLen = $(".det_ul .det_li").length;
		var lnum = lLen-1;
		$(".det_ul").width(lLen*110+16).find(".det_li:eq("+lnum+")").css("background","none");
		$(".det_cul").width(lLen*110+16);
	})
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/CRMBrotherAction.do" method="post" target="formFrame">
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')"/>
<input type="hidden" name="tableName" id="tableName" value="$!tableName">
<input type="hidden" name="childTableName" id="childTableName" value="$!childTableName">
<input type="hidden" name="f_brother" id="f_brother" value="$!result.get('ClientId')">
<input type="hidden" name="keyId" id="keyId" value='$!result.get("id")'>
<input type="hidden" name="id" id="id" value='$!result.get("id")'>
<input type="hidden" id="designId" name="designId" value="$!designId"/>
<input type="hidden" id="isConsole" name="isConsole" value="$!isConsole"/>
<input type="hidden" id="relationId" name="relationId" value="$!result.get("id")"/>
<input type="hidden" id="approveBefore" name="approveBefore" value=""/>
#foreach($bean in $tableInfoBean.fieldInfos)
	#if("$bean.inputType" == "100" || "$bean.inputType" == "3" || "$bean.inputType" == "6")
		#if("$!bean.defaultValue" !="")
			<input type="hidden" name="$bean.fieldName" id="$bean.fieldName" value="$!result.get($bean.defaultValue)" >
		#end
	#end
#end
<div class="subbox cf" style="height:350px;width:100%;overflow:hidden;overflow-y:auto;">

#set($showAttachment = "false") 
#set($workFlowNotNullFieldsStr = ","+$!workFlowNotNullFields)
<div class="bd" style="width:700px;margin:0 auto;">
	<div class="inputbox">
		<div class="head_btns">
			<div class="btn btn-small rf" onclick="alertSet('$!result.get("id")','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');">提醒</div>
			#if("$!OAWorkFlow" == "true"&&$result.get("workFlowNode")!="-1"&&$result.get("checkPersons").indexOf(";${loginBean.id};")>-1&&"$!noApprove" !="true")
				#set($keyId = $result.get("id"))
				<!-- 
				<div class="btn btn-small rf" onclick="approve('$keyId','deliver','$tableName')">审核</div>
				 -->
				<div class="btn btn-small rf" onclick="approveBeforeAdd()">审核</div>
			#end
		</div>
			<!-- 主表展示开始-->
		#foreach ($maps in $mainMap.keySet())	
			<div>
				<div class="title_dv">
					<em class="txt">#if("$!globals.getEnumerationItemsDisplay($tableName,$maps)" == "") 默认分组  #else $globals.getEnumerationItemsDisplay("$tableName","$maps") #end</em>
				</div>
				<ul class="bk_ul">
					#foreach ($row in $mainMap.get($maps))
						#set($fieldBean = $globals.getFieldBean($tableName,$row))
						
						#set($notNullFlag = "false")
			        	#set($fieldStr = ","+${row}+",")
			        	#if("$fieldBean.isNull" == "1" || $!workFlowNotNullFieldsStr.indexOf($fieldStr)>-1)
			        		#set($notNullFlag = "true")
			        	#end
			        	
			        	#set($readStr = '')
			        	#if($!readMainFieldsStr.indexOf(",${row},")>-1)
			        		#if("$fieldBean.inputType" == "1")
			        			#set($readStr = ' style="display:none;background-color: #f8f8f8;"')
			        		#else
				        		#set($readStr = ' readonly="readonly" style="background-color: #f8f8f8;"')
			        		#end
			        	#end
						
						
						#if("$!tableName"=="CRMSaleFollowUp" && "$row" == "calendarBtn")
							<li class="bk_li" >
				        		<div class="swa_c1" title='下次回访日程'>下次回访日程：</div>
				        		#if("$!calendarBean" == "")
					        		<button class="btn btn-mini" type="button" onclick="addCalendar()" id="addCalendarBtn">添加日程</button>
				        		#else
				        			<i relationId="$!calendarBean.relationId" class="calendarDetail">关联日程</i>
				        		#end
				        	</li>
						#elseif("$fieldBean.inputType" == "0")
							#if("$fieldBean.fieldType" == "1")
								<li class="bk_li #if("$fieldBean.fieldName" == "Topic") ess #end ">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="text" value="$!globals.dealDoubleDigits("$!result.get($fieldBean.fieldName)","amount")" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        	</li>
							#elseif("$fieldBean.fieldType" == "5")
								<li class="bk_li">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		#if($readStr.length()>0)
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)" type="text" readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end/>
					        		#else
					        		<input class="ip_txt inp_date" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)" type="text" onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        		#end
					        	</li>	
							#elseif("$fieldBean.fieldType" == "6")
								<li class="bk_li ">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		#if($readStr.length()>0)
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)" type="text"  readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        		#else
					        		<input class="ip_txt inp_date" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)" type="text" onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        		#end
					        	</li>
							#elseif("$fieldBean.fieldType" == "3" || ("$fieldBean.fieldType" == "16"))
								<li class="bk_li ess">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<textarea class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end onclick="changeBgColor(this);" $readStr>$!result.get($fieldBean.fieldName)</textarea>
					        	</li>
							#elseif("$fieldBean.fieldType" == "13"||"$fieldBean.fieldType" == "14")
								#set($showAttachment = "true") 
							#elseif("$fieldBean.fieldType" == "18")
								<li class="bk_li ess">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="text" value="$!result.get($fieldBean.fieldName)" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        	</li>
							#else
								#if("$!tableName"=="CRMPotentialClient" && "$fieldBean.fieldName"=="Trade")
									<li class="bk_li">
										<input type="hidden" name="Trade" id="Trade" value="$!result.get($fieldBean.fieldName)"/>
						        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
						        		<div class="swa_c2">
						        			#if("$!readStr" == "")
								        		<input class="ip_txt" name="tradeName"  id="tradeName" value="$!workProfessionMap.get($!result.get($fieldBean.fieldName))"  type="text" ondblclick="openTrade()" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
								        		<i class="adi" title='添加行业' onclick="openTrade()"></i>
							        		#else
							        			<input class="ip_txt bd-r" name="tradeName"  id="tradeName" $!readStr/>
							        		#end
						        		</div>
						        	</li>
								#elseif("$!tableName"=="CRMPotentialClient" && "$fieldBean.fieldName"=="District")
									<li class="bk_li">
										<input type="hidden" name="District" id="District" value="$!result.get($fieldBean.fieldName)"/>
						        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
						        		<div class="swa_c2">
						        			#if("$!readStr" == "")
								        		<input class="ip_txt" name="districtName"  id="districtName" value="$!districtMap.get($!result.get($fieldBean.fieldName))"  type="text" ondblclick="openDistrict()" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
								        		<i class="adi" title='添加地区' onclick="openDistrict()"></i>
							        		#else
							        			<input class="ip_txt bd-r" name="districtName"  id="districtName" $!readStr/>
							        		#end
						        		</div>
						        	</li>
								#else
								<li class="bk_li">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)" type="text" value="$!fieldBean.defaultValue" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        	</li>
					        	#end
							#end
						#elseif("$fieldBean.inputType" == "1")
							<li class="bk_li">
								#set($disName="")
				          		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					           	<select class="slt" $readStr name="$fieldBean.fieldName" id="$fieldBean.fieldName" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end >
					            	<option value="" >--请选择--</option>
					             	#foreach($item in $globals.getEnumerationItems("$fieldBean.refEnumerationName"))	 
					        	    	<option value="$item.value"#if("$!result.get($fieldBean.fieldName)" == "$item.value") selected="selected" #set($disName="$item.name") #end>$item.name</option>
							        #end
					            </select>
					            #if($readStr.length()>0)
					            <input name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" type="text" class="inp_w"  value="$disName" readonly="readonly"/>	
					            #end
				           	</li>
						#elseif("$fieldBean.inputType" == "2")
							#if("$fieldBean.fieldName" == "ClientId")
								<li class="bk_li ess">
									<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)" />
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="swa_c2">
					        			#if("$!readStr" == "")
						        			<input class="ip_txt" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value="$!result.get('CRMClientInfo.ClientName')" type="text" #if("$!keyInfo"=="") ondblclick="popSelect('CrmClickGroup','ClientId')" #end readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
						        			<i class="adi" title='添加$fieldBean.display.get("$globals.getLocale()")' #if("$!clientName"=="") onclick="popSelect('CrmClickGroup','ClientId')" #end></i>
					        			#else
					        				<input class="ip_txt bd-r" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value="$!result.get('CRMClientInfo.ClientName')" $!readStr/>
					        			#end
					        		</div>
					        	</li>
				        	#else
					        	<li class="bk_li">
									<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="swa_c2">
					        			#if("$!readStr" == "")
						        			<input class="ip_txt" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value="$!result.get($fieldBean.fieldName)"  type="text" ondblclick="definePop('/UserFunctionAction.do?tableName=$tableName&fieldName=$fieldBean.fieldName&operation=22','$fieldBean.fieldName','$fieldBean.display.get("$globals.getLocale()")');" readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end" $readStr/>
						        			<i class="adi" title='添加$fieldBean.display.get("$globals.getLocale()")' onclick="definePop('/UserFunctionAction.do?tableName=$tableName&fieldName=$fieldBean.fieldName&operation=22','$fieldBean.fieldName','$fieldBean.display.get("$globals.getLocale()")');"></i>
					        			#else
					        				<input class="ip_txt bd-r" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" $!readStr/>
					        			#end
					        		</div>
					        	</li>
				        	#end
						#elseif("$fieldBean.inputType" == "5")
						#elseif("$fieldBean.inputType" == "8")
							#if("$fieldBean.fieldType" == "1")
								<li class="bk_li" >
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="text" value="$!globals.dealDoubleDigits("$!result.get($fieldBean.fieldName)","amount")" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end readonly="readonly"/>
					        	</li>
							#else
								<li class="bk_li #if("$fieldBean.fieldType" == "18") ess #end ">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="text" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end" readonly="readonly"/>
					        	</li>
							#end
						#elseif("$fieldBean.inputType" == "10")
						#elseif("$fieldBean.inputType" == "14")
							<li class="bk_li">
								<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<div class="swa_c2">
				        			#if("$!readStr" == "")
				        				<input class="ip_txt" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value="$!deptMap.get($!result.get($fieldBean.fieldName))"   type="text" ondblclick="popSelect('deptGroup','$fieldBean.fieldName')" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
				        				<i class="adi" title='添加$fieldBean.display.get("$globals.getLocale()")' onclick="popSelect('deptGroup','$fieldBean.fieldName')"></i>
				        			#else
				        				<input class="ip_txt bd-r" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value="$!deptMap.get($!result.get($fieldBean.fieldName))" $!readStr/>
				        			#end
				        		</div>
				        	</li>
						#elseif("$fieldBean.inputType" == "15")
							<li class="bk_li">
								<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!result.get($fieldBean.fieldName)"/>
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<div class="swa_c2">
				        			#if("$!readStr" == "")
				        				<input class="ip_txt" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value='$!globals.getEmpFullNameByUserId($!result.get($fieldBean.fieldName))' readonly="readonly"  type="text" ondblclick="popSelect('userGroup','$fieldBean.fieldName')" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
				        				<i class="adi" title='添加$fieldBean.display.get("$globals.getLocale()")' onclick="popSelect('userGroup','$fieldBean.fieldName')"></i>
				        			#else
				        				<input class="ip_txt bd-r" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value="$!globals.getEmpFullNameByUserId($!result.get($fieldBean.fieldName))" $!readStr/>
				        			#end
				        		</div>
				        	</li>
						#elseif("$fieldBean.inputType" == "20")
							<li class="bk_li">
				          		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					           	<select class="slt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end" inputType="20">
					            	<option value=""></option>
					            	#set($optionList = $selectMap.get($fieldBean.fieldName))
					            	#foreach($option in $optionList)
					            		<option value="$globals.get($option,0)" #if("$!result.get($fieldBean.fieldName)"=="$globals.get($option,0)") selected="selected" #end>$globals.get($option,1)</option>
					            	#end
					            </select>
				           	</li>
						#else
							<li class="bk_li">
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName"  value="$!result.get($fieldBean.fieldName)" type="text" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
				        	</li>
						#end
					#end
		        </ul>
	        </div>
        #end
        <!-- 主表展示结束-->
        
        <!-- 明细 NewStart -->
      	
        <div class="det_dv">
	        
	        	#if("$!tableName" == "CRMSaleContract" || "$!tableName" == "CRMsalesQuot")
	        	<div class="title_dv">
		        	<em class="txt">明细信息</em>
		        	<i class="add" onclick="goodsSelect();"></i>
		        </div>
		        
		        <div class="con_dv">
		        	<ul class="det_ul">
			        	<li class="det_li"><i>产品名称</i></li>
			        	<li class="det_li"><i>计量单位</i></li>
			        	<li class="det_li"><i>数量</i></li>
			        	<li class="det_li"><i>单价</i></li>
			        	<li class="det_li"><i>合计</i></li>
			        </ul>
			        <ul class="det_cul" id="goodsContent">
		        	#foreach ($child in $!result.get("TABLENAME_${childTableName}")) 
						<li class="det_li">
							<span class="det_sp">
								<input type="hidden" class="ip_txt" name="${childTableName}_GoodsCode" value='$!child.get("GoodsCode")'/>
								<input type="text" class="ip_txt" value='$child.get("tblGoods.GoodsFullName")'/>
							</span>
							<span class="det_sp">
								<input type="hidden" class="ip_txt" name="${childTableName}_Unit" value="$!child.get("Unit")"/>
								
								#set($unitName = "Unit.Unit")
								#if("$!tableName" == "CRMsalesQuot")
									#set($unitName = "tblUnit.UnitName")
								#end
								<input type="text" class="ip_txt" value='$!child.get("$unitName")'/>
							</span>
							<span class="det_sp">
								#set($Qty = "")
								#set($Qty = $!child.get("Qty"))
								<input type="text" class="ip_txt" name="${childTableName}_Qty" value='$globals.substring("$Qty",".")'/>
							</span>
							<span class="det_sp">
								#set($price = "")
								#set($price = $!child.get("Price"))
								<input type="text" class="ip_txt" name="${childTableName}_Price" value='$!globals.dealDoubleDigits("$price","amount")'/>
							</span>
							<span class="det_sp">
								#set($Amount = "")
								#set($Amount = $!child.get("Amount"))
								<input type="text" class="ip_txt" name="${childTableName}_Amount" value='$!globals.dealDoubleDigits("$Amount","amount")'/>
							</span>
							<span class="del_sp" title='删除'></span>
						</li>
			       #end 	
			       </ul>
			       </div>
	        	#else
	        		#if("$!childTableName" != ""  && "$!fieldDisplayBean.pageChildFields" !="")
	        		<div class="title_dv">
			        	<em class="txt" >明细信息</em>
			        	<i class="add" onclick="addChild()" ></i>
			        </div>
			        
			        <div class="con_dv" id="childContent">
			        	#foreach ($maps in $childMap.keySet())
				        	<ul class="det_ul">
				        		#foreach ($row in $childMap.get($maps))
				        			#set($fieldBean = $globals.getFieldBean($childTableName,$row))
						        	<li class="det_li"><i>$fieldBean.display.get("$globals.getLocale()")</i></li>
				        		#end 
					        </ul>
					        #foreach ($child in $!result.get("TABLENAME_${childTableName}")) 
						        <ul class="det_cul">
						        	<li class="det_li">
						        	#foreach ($row in $childMap.get($maps))
							        	#set($fieldBean = $globals.getFieldBean($childTableName,$row))
										#if("$fieldBean.inputType" == "0")
											<span class="det_sp"><input type="text" class="ip_txt" id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName" value="$!child.get($fieldBean.fieldName)"/></span>
										#elseif("$fieldBean.inputType" == "1")
											<span class="det_sp">
												<select id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName">
									            	<option value="" >--请选择--</option>
									             	#foreach($item in $globals.getEnumerationItems("$fieldBean.refEnumerationName"))	 
									        	    	<option value="$item.value"#if("$child.get($fieldBean.fieldName)" == "$item.value") selected="selected" #end>$item.name</option>
											        #end
									            </select>
											</span>
										#elseif("$fieldBean.inputType" == "2")
											<span class="det_sp">
												<input type="hidden" id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName" value="$!child.get($fieldBean.fieldName)" class="ip_txt" />
												<input  name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value="$child.get($fieldBean.fieldName)"  type="text" class="inp_w" />
											</span>
										#elseif("$fieldBean.inputType" == "5")
										#elseif("$fieldBean.inputType" == "10")
										#elseif("$fieldBean.inputType" == "14")
										#elseif("$fieldBean.inputType" == "15")
										#elseif("$fieldBean.inputType" == "14")
										#else
										#end
							        #end
						        	</li>
						        </ul>
					        #end
					    #end
			        
		        </div>
	        	#end
	        	#end
        </div>
        <!-- 明细 NewEnd -->
        
        
       
        #if("$showAttachment" == "true")
        <div>
        <!--附件开始 -->
	        <div class="title_dv">
	        	<em class="txt">附件管理</em>
	        </div>
	        
	        #set($fieldInfoBean = $globals.getFieldBean("$tableName","Attachment"))
	        #if("$fieldInfoBean.fieldType" == "13")
		        <div class="btn btn-small" id="picbutton" name="picbutton"  onClick="upload('PIC','uploadBtn');">图片上传</div>
		        <ul id="picuploadul_uploadBtn">
		        	#foreach($uprow in $result.get("Attachment").split(";"))
						#if("$!uprow" !="")
						<li style='background:url();' id='uploadfile_$uprow'><input type=hidden id="uploadBtn" name="uploadBtn" value='$uprow'>
						<div><a class="img_wa" href="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" target="_blank"><img src="/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName" border="0"></a></div><div>$uprow&nbsp;&nbsp;#if("$detail" != "detail")<a href='javascript:deleteupload("$uprow","false","$tableName","PIC");'>$text.get("common.lb.del")</a>#end</div>
						</li>
						#end
					#end
		        </ul>
	        #else
		        <div class="btn btn-small" id="picbutton" name="picbutton"  onClick="upload('AFFIX','uploadBtn');">附件上传</div>
		        <ul id="affixuploadul_uploadBtn">
		        	#foreach($uprow in $result.get("Attachment").split(";"))
						#if($uprow != "")
							<li class="file_li" id="uploadfile_$uprow">
							<input type=hidden id="uploadBtn" name=uploadBtn value='$uprow'/><div class="showAffix">$uprow</div>&nbsp;&nbsp;&nbsp;			
							#if("$!detail" != "detail")
								<a href='javascript:deleteupload("$uprow","false","CRMClientInfo","AFFIX");'>$text.get("common.lb.del")</a>
							#end
								&nbsp;&nbsp;<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=$tableName" target="_blank">$text.get("common.lb.download")</a>
							</li>
						#end
					#end
		        </ul>
	        #end
        </div>
        <!-- 附件结束 -->
        #end
	</div>
</div>
	#if("$!isConsole"=="true")
		<a onclick="beforeSubmit();">提交</a>
	#end
</div>
<div class="add-calendar" id="addCalendar" style="display:none;"></div>
</form>

</body>
</html>
