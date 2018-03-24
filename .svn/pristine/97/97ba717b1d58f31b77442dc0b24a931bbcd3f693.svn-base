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
	var copyChildUl;
	$(function(){
		var lLen = $(".det_ul .det_li").length;
		var lnum = lLen-1;
		$(".det_ul").width(lLen*110+16).find(".det_li:eq("+lnum+")").css("background","none");
		$(".det_cul").width(lLen*110+16);
		copyChildUl = $("#childUl").clone(true);
		
		#if("$!isConsole"=="true")
			parent.$("#billFrame").css("height",jQuery(".inputbox").height()+100)
		#end
	})
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/CRMBrotherAction.do" method="post" target="formFrame">
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_ADD')"/>
<input type="hidden" name="tableName" id="tableName" value="$!tableName">
<input type="hidden" name="childTableName" id="childTableName" value="$!childTableName">
<input type="hidden" name="f_brother" id="f_brother" value="$!clientId">
<input type="hidden" id="designId" name="designId" value="$!designId"/>
<input type="hidden" id="approveBefore" name="approveBefore" value=""/>
<input type="hidden" id="isConsole" name="isConsole" value="$!isConsole"/>
<input type="hidden" id="keyId" name="keyId" value="$!newId"/>
<input type="hidden" id="relationId" name="relationId" value="$!newId"/>

#foreach($bean in $tableInfoBean.fieldInfos)
	#if("$bean.inputType" == "100" || "$bean.inputType" == "3" || "$bean.inputType" == "6")
		#if("$bean.fieldType" == "5")
			<input type="hidden" name="$bean.fieldName" id="$bean.fieldName" value='$globals.getHongVal("sys_date")'>
		#elseif("$bean.fieldType" == "6")
			<input type="hidden" name="$bean.fieldName" id="$bean.fieldName" value='$globals.getHongVal("sys_datetime")'>
		#else
			<input type="hidden" name="$bean.fieldName" id="$bean.fieldName" value="$!bean.defaultValue">	
		#end
	#end
#end
<div class="subbox cf" style="height:350px;width:100%;overflow:hidden;overflow-y:auto;">

#set($showAttachment = "false") 
<div class="bd" style="width:700px;margin:0 auto;">
	<div class="inputbox">
		<div class="head_btns">
			#if("$!OAWorkFlow" == "true" && "$!noApprove" !="true")<div class="btn btn-small rf" onclick="approveBeforeAdd()">审核</div>#end
		</div>
		#foreach ($maps in $mainMap.keySet())	
			<!-- bk Start -->
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
				        		<button class="btn btn-mini" type="button" onclick="addCalendar()" id="addCalendarBtn">添加日程</button>
				        	</li>
						#elseif("$!tableName"=="CRMSaleFollowUp" && "$fieldBean.fieldName" == "location")
						#elseif("$fieldBean.inputType" == "0")
							#if("$fieldBean.fieldType" == "1")
								<li class="bk_li" >
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end $fieldBean.display.get("$globals.getLocale()")：</div>
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="text" value="$!globals.dealDoubleDigits("$!fieldBean.defaultValue","amount")" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        	</li>
							#elseif("$fieldBean.fieldType" == "5")
								<li class="bk_li">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		#if($readStr.length()>0)
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value='$fieldBean.getDefValue()' type="text" readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end/>
					        		#else
					        		<input class="ip_txt inp_date" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value='$fieldBean.getDefValue()' type="text" onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        		#end
					        	</li>	
							#elseif("$fieldBean.fieldType" == "6")
								<li class="bk_li ">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		
					        		<input class="ip_txt inp_date" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value='$fieldBean.getDefValue()' type="text" onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        	</li>
							#elseif("$fieldBean.fieldType" == "3" || ("$fieldBean.fieldType" == "16"))
								<li class="bk_li ess">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<textarea class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end" onclick="changeBgColor(this);" $readStr></textarea>
					        	</li>
							#elseif("$fieldBean.fieldType" == "13"||"$fieldBean.fieldType" == "14")
								#set($showAttachment = "true") 
							#elseif("$fieldBean.fieldType" == "18")
								<li class="bk_li ess">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="text" value="$!fieldBean.defaultValue" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        	</li>
							#else
								#if("$!tableName"=="CRMPotentialClient" && "$fieldBean.fieldName"=="Trade")
									<li class="bk_li">
										<input type="hidden" name="Trade" id="Trade" value=""/>
						        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
						        		<div class="swa_c2">
						        			#if("$!readStr" == "")
								        		<input class="ip_txt" name="tradeName"  id="tradeName"  type="text" #if("$!readStr" == "") ondblclick="openTrade()" #end #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
								        		<i class="adi" title='添加行业' onclick="openTrade()"></i>
						        			#else
						        				<input class="ip_txt bd-r" name="tradeName"  id="tradeName" $!readStr/>
						        			#end
						        		</div>
						        	</li>
								#elseif("$!tableName"=="CRMPotentialClient" && "$fieldBean.fieldName"=="District")
									<li class="bk_li">
										<input type="hidden" name="District" id="District" value=""/>
						        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
						        		<div class="swa_c2">
						        			#if("$!readStr" == "")
							        			<input class="ip_txt" name="districtName"  id="districtName"  type="text" #if("$!readStr" == "") ondblclick="openDistrict()" #end #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
							        			<i class="adi" title='添加地区' onclick="openDistrict()"></i>
							        		#else
							        			<input class="ip_txt bd-r" name="districtName"  id="districtName" $!readStr/>
							        		#end
						        		</div>
						        	</li>
								#else
									<li class="bk_li">
										#set($defValue = "$!fieldBean.defaultValue")
										#if($!fieldBean.getFieldIdentityStr()=="BillNo")
											#if($!fieldBean.getDefaultValue()=="")
												#set($str=$fieldBean.getFieldName())
												#set($str="_"+$str)
												#set($str=$!tableName+$str)
												#set($defValue=$globals.getBillNoCode($str))
											#else
												#set($defValue=$globals.getBillNoCode($!fieldBean.getDefaultValue()))
											#end
										#end
						        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
						        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="text" value="$defValue" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
						        	</li>
								#end
							#end
						#elseif("$fieldBean.inputType" == "1")
							<li class="bk_li">
				          		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					           	<select class="slt" $readStr name="$fieldBean.fieldName" id="$fieldBean.fieldName" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr>
					            	<option value="" >--请选择--</option>
					             	#foreach($item in $globals.getEnumerationItems("$fieldBean.refEnumerationName"))	 
					        	    	<option value="$item.value"#if("$fieldBean.defaultValue" == "$item.value") selected="selected" #end>$item.name</option>
							        #end
					            </select>
					            #if($readStr.length()>0)
					            <input name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" type="text" class="inp_w"  value="" readonly="readonly"/>	
					            #end
				           	</li>
						#elseif("$fieldBean.inputType" == "2")
							#if("$fieldBean.fieldName" == "ClientId")
								<li class="bk_li ess">
									<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!clientId" />
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="swa_c2">
					        			#if("$!clientName"=="" && "$!readStr" == "")
						        			<input class="ip_txt" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" type="text" ondblclick="popSelect('CrmClickGroup','ClientId')" readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
						        			<i class="adi" title='添加$fieldBean.display.get("$globals.getLocale()")' #if("$!clientName"=="") onclick="popSelect('CrmClickGroup','ClientId')" #end></i>
					        			#else
					        				<input class="ip_txt bd-r" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value="$!clientName" readonly="readonly" $!readStr />
					        			#end
					        		</div>
					        	</li>
				        	#else
					        	<li class="bk_li">
									<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="" />
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<div class="swa_c2">
					        			#if("$!readStr" == "")
						        			<input class="ip_txt" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value=""  type="text" #if("$!readStr" == "") ondblclick="definePop('/UserFunctionAction.do?tableName=$tableName&fieldName=$fieldBean.fieldName&operation=22','$fieldBean.fieldName','$fieldBean.display.get("$globals.getLocale()")');" #end readonly="readonly" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
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
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="text" value="$!globals.dealDoubleDigits("$!fieldBean.defaultValue","amount")" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end readonly="readonly" $readStr/>
					        	</li>
							#else
								<li class="bk_li #if("$fieldBean.fieldType" == "18") ess #end ">
					        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="text" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end readonly="readonly" $readStr/>
					        	</li>
							#end
						#elseif("$fieldBean.inputType" == "10")
						#elseif("$fieldBean.inputType" == "14")
							<li class="bk_li">
								<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value=""/>
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<div class="swa_c2">
					        		#if("$!readStr" == "")
						        		<input class="ip_txt" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name"  type="text" #if("$!readStr" == "") ondblclick="popSelect('deptGroup','$fieldBean.fieldName')" #end #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        			<i class="adi" title='添加$fieldBean.display.get("$globals.getLocale()")' onclick="popSelect('deptGroup','$fieldBean.fieldName')"></i>
					        		#else
					        			<input class="ip_txt bd-r" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" $!readStr/>
					        		#end
				        		</div>
				        	</li>
						#elseif("$fieldBean.inputType" == "15")
							<li class="bk_li">
								<input type="hidden" name="$fieldBean.fieldName" id="$fieldBean.fieldName" value="$!loginBean.id"/>
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<div class="swa_c2">
					        		#if("$!readStr" == "")
					        			<input class="ip_txt" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value="$!globals.getEmpFullNameByUserId($!loginBean.id)" readonly="readonly"  type="text" #if("$!readStr" == "") ondblclick="popSelect('userGroup','$fieldBean.fieldName')" #end #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
					        			<i class="adi" title='添加$fieldBean.display.get("$globals.getLocale()")' onclick="popSelect('userGroup','$fieldBean.fieldName')"></i>
					        		#else
					        			<input class="ip_txt bd-r" name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" $!readStr/>
					        		#end
				        		</div>
				        	</li>
						#elseif("$fieldBean.inputType" == "20")
					        #set($optionList = $selectMap.get($fieldBean.fieldName))
							<li class="bk_li">
				          		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
					           	<select class="slt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end inputType="20" $readStr>
					            	#if("$!optionList" =="" || $optionList.size() == 0)
						            	<option value=""></option>
					            	#else
						            	#foreach($option in $optionList)
						            		<option value="$globals.get($option,0)" >$globals.get($option,1)</option>
						            	#end
					            	#end
					            </select>
				           	</li>
						#else
							<li class="bk_li">
				        		<div class="swa_c1" title='$fieldBean.display.get("$globals.getLocale()")'>#if("$notNullFlag"=="true")<i class="red">*</i>#end$fieldBean.display.get("$globals.getLocale()")：</div>
				        		<input class="ip_txt" name="$fieldBean.fieldName" id="$fieldBean.fieldName" type="text" #if("$notNullFlag"=="true") notNull="true" displayName='$fieldBean.display.get("$globals.getLocale()")' #end $readStr/>
				        	</li>
						#end
					#end
		        </ul>
	        </div>
        #end
        <!-- bk End -->
        
        <!-- 明细 NewStart -->
      
        <div class="det_dv">
	        	#if("$!tableName" == "CRMSaleContract" || "$!tableName" == "CRMsalesQuot")
	        	<div class="title_dv">
		        	<em class="txt">明细信息 </em>
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
			        	<!-- 
						<li class="det_li">
							<span class="det_sp">
								<input type="text" class="ip_txt" />
							</span>
							<span class="det_sp">
								<input type="text" class="ip_txt" />
							</span>
							<span class="det_sp">
								<input type="text" class="ip_txt" />
							</span>
							<span class="det_sp">
								<input type="text" class="ip_txt" />
							</span>
							<span class="det_sp">
								<input type="text" class="ip_txt" />
							</span>
							<span class="del_sp" title='删除'></span>
			        	</li>
			        	 -->
			        </ul>
			        
			    </div>    
	        	#else
	        		#if("$!childTableName" != "" && "$!fieldDisplayBean.pageChildFields" !="")
			        #foreach ($maps in $childMap.keySet())
			        <div class="title_dv">
			        	<em class="txt">明细信息 </em>
			        	<i class="add" onclick="addChild()"></i>
			        </div>
		        	<div class="con_dv" id="childContent">
			        	<ul class="det_ul">
			        		#foreach ($row in $childMap.get($maps))
			        			#set($fieldBean = $globals.getFieldBean($childTableName,$row))
					        	<li class="det_li"><i>$fieldBean.display.get("$globals.getLocale()")</i></li>
			        		#end 
				        </ul>
				        <ul class="det_cul" id="childUl">
				        	<li class="det_li">
				        	#foreach ($row in $childMap.get($maps))
					        	#set($fieldBean = $globals.getFieldBean($childTableName,$row))
								#if("$fieldBean.inputType" == "0")
									#if("$fieldBean.fieldType" == "5")
										<span class="det_sp "><input type="text" class="ip_txt" id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName" onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd'})" readonly="readonly"/></span>
									#elseif("$fieldBean.fieldType" == "6")
										<span class="det_sp "><input type="text" class="ip_txt" id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName" onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly"/></span>
									#else
										<span class="det_sp"><input type="text" class="ip_txt" id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName"/></span>
									#end
								#elseif("$fieldBean.inputType" == "1")
									<span class="det_sp">
										<select id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName">
							            	<option value="" >--请选择--</option>
							             	#foreach($item in $globals.getEnumerationItems("$fieldBean.refEnumerationName"))	 
							        	    	<option value="$item.value"#if("$fieldBean.defaultValue" == "$item.value") selected="selected" #end>$item.name</option>
									        #end
							            </select>
									</span>
								#elseif("$fieldBean.inputType" == "2")
									<span class="det_sp">
										<input type="hidden" id="${childTableName}_$fieldBean.fieldName" name="${childTableName}_$fieldBean.fieldName" class="ip_txt" />
										<input  name="${fieldBean.fieldName}Name"  id="${fieldBean.fieldName}Name" value=""  type="text" class="inp_w" />
									</span>
								#elseif("$fieldBean.inputType" == "5")
								#elseif("$fieldBean.inputType" == "10")
								#elseif("$fieldBean.inputType" == "14")
								#elseif("$fieldBean.inputType" == "15")
								#elseif("$fieldBean.inputType" == "14")
								#else
								#end
					        #end
					        <span class="del_sp" title='删除' style="display: none;"></span>
				        	</li>
				        </ul>
			        </div>
				    #end
				    #end
	        	#end
        </div>
        <!-- 明细 NewEnd -->
        
        
       #if("$showAttachment" == "true")
	        <div style="margin:10px 0 0 0;">
	        <!-- bk Start -->
		        <div class="title_dv">
		        	<em class="txt">附件管理</em>
		        </div>
		        #set($fieldInfoBean = $globals.getFieldBean("$tableName","Attachment"))
		        #if("$fieldInfoBean.fieldType" == "13")
			        <div class="btn btn-small" id="picbutton" name="picbutton"  onClick="upload('PIC','uploadBtn');">图片上传</div>
			        <ul id="picuploadul_uploadBtn"></ul>
		        #else
			        <div class="btn btn-small" id="picbutton" name="picbutton"  onClick="upload('AFFIX','uploadBtn');" >附件上传</div>
			        <ul id="affixuploadul_uploadBtn"></ul>
		        #end
		        
	        </div>
	        <!-- bk End -->
       #end
	</div>
</div>
	#if("$!isConsole"=="true")
		<a onclick="beforeSubmit();" >提交</a>
	#end
</div>
<div class="add-calendar" id="addCalendar" style="display:none;"></div>
</form>


</body>
</html>
