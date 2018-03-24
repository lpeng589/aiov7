	#set($tableBean = $globals.getTableInfoBean("$tableName"))
	<p class="title_p">
		<i class="w_i">$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")</i>
	</p>
	#if("$!brotherList" == "" || $brotherList.size() == 0)
		<div class="con_ndv">
			<i class="b_i">没有相关记录</i>
			#if("$!tableBean.isView" != "1")
				#if("$!oldPath" == "true" && $loginBean.operationMap.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=$tableName").add())
					<i class="add_i" onclick="dealOldBroBill('/UserFunctionAction.do?parentTableName=CRMClientInfo&tableName=$tableName&parentCode=&operation=6&moduleType=&winCurIndex=3&noback=true','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")','false');" >新建</i>
				#elseif($loginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=$tableName").add())
					<i class="add_i" onclick="dealOldBroBill('/UserFunctionAction.do?tableName=$tableName&parentCode=&operation=6&moduleType=&winCurIndex=3&noback=true','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")','true');" >新建</i>
				#elseif($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$tableName").add())
					<i class="add_i" onclick="addBroBill('$tableName','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');" >新建</i> 
				#end
			#end
		</div>
	#else
		#set($fieldArrSize = 0)
		#if("$!oldPath" == "true")
			#set($fieldArrSize = $fieldlist.size())
		#else
			#set($fieldArrSize = $globals.arrayLength($fieldDisplayBean.listFields.split(",")))
		#end
		
		#if("$!OAWorkFlow" == "true")
			#set($liWidth = $math.mul("$fieldArrSize","270"))
		#else
			#set($liWidth = $math.mul("$fieldArrSize","90"))
		#end
		
		#set($liWidth = $math.add($liWidth,"90"))
		 
		<div class="con_ndv">
			<div class="t_dv">
				<em class="add_em">
					#if("$!tableBean.isView" != "1")
						#if($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$tableName").add())
							<i class="m_i" onclick="addBroBill('$tableName','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');"></i><i class="w_i" onclick="addBroBill('$tableName','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');">新建</i>
						#elseif("$!oldPath" == "true" && $loginBean.operationMap.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=$tableName").add())
							<i class="m_i" onclick="dealOldBroBill('/UserFunctionAction.do?parentTableName=CRMClientInfo&tableName=$tableName&parentCode=&operation=6&moduleType=&winCurIndex=3&noback=true','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');"></i><i class="w_i" onclick="dealOldBroBill('/UserFunctionAction.do?parentTableName=CRMClientInfo&tableName=$tableName&parentCode=&operation=6&moduleType=&winCurIndex=3&noback=true','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');">新建</i>
						#elseif($loginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=$tableName").add())
							<i class="m_i" onclick="dealOldBroBill('/UserFunctionAction.do?parentTableName=CRMClientInfo&tableName=$tableName&parentCode=&operation=6&moduleType=&winCurIndex=3&noback=true','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');"></i><i class="w_i" onclick="dealOldBroBill('/UserFunctionAction.do?tableName=$tableName&parentCode=&operation=6&moduleType=&winCurIndex=3&noback=true','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');">新建</i>
						#end
					#end
				</em>
				<div tableName="$!tableName" class="pager" >
					$pageBar
				</div>
			</div>
			<div class="b_dv">
				<table  cellpadding="0" cellspacing="0" border="0" class="brother-table">
					<thead>
					<tr style="width: ${liWidth}px;">
						#if("$!tableName"!="CRMSendSMSView")
						<td class="txt-cen">
							<div class="item_dv" title="$tableName">
								操作
							</div>
						</td>
						#end
						#if("$!oldPath" == "true")
							#foreach($col in $fieldlist)
								#if("$!col.asFieldName" != "ComFullName")
								<td align="left">
									<div class="item_dv" title="$col.display">
										$col.display
									</div>
								</td>
								#end
							#end
						#else
							#foreach($fieldName in $fieldDisplayBean.listFields.split(","))
								#if("$!fieldName" != "ClientId")
									#set($fieldBean = "")
									#set($fieldBean = $globals.getFieldBean($tableName,$fieldName))
									<td align="left">
										<div class="item_dv">
											$fieldBean.display.get("$globals.getLocale()")
										</div>
									</td>
								#end
							#end
						#end
					</tr>
					</thead>
					<tbody>
					#foreach($brother in $brotherList)
					#set($updateOp = "false")
					#if($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$!tableName").update() &&( $allowUpdUserIds.indexOf(",$loginBean.id,")>-1 || $loginBean.id=="1" || $loginBean.id=="$brother.get('createBy')"))
						#set($updateOp = "true")
					#end
					
					<tr style="width: ${liWidth}px;">
							#if("$!tableName"!="CRMSendSMSView")
							<td class="txt-cen">
							<div class="item_dv">
								#if("$!oldPath" == "true" && $loginBean.operationMap.get("/UserFunctionQueryAction.do?parentTableName=CRMClientInfo&tableName=$tableName").query())
									#if("$!tableName"=="CRMMailInfoView")
										<i title='查看' class="read_i" onclick="javascript:top.mdiwin('/EMailAction.do?operation=5&noback=true&keyId=$brother.get("MailId")&newOpen=true','$brother.get("MailTitle")');"></i>
									#else
										<i title='查看' class="read_i" onclick="dealOldBroBill('/UserFunctionAction.do?parentTableName=CRMClientInfo&tableName=$tableName&parentCode=&operation=5&moduleType=&winCurIndex=3&noback=true&keyId=$brother.get("id")','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');"></i>
									#end
								#elseif($loginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=$tableName").query())
									<i title='查看' class="read_i" onclick="dealOldBroBill('/UserFunctionAction.do?tableName=$tableName&parentCode=&operation=5&moduleType=&winCurIndex=3&noback=true&keyId=$brother.get("id")','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');"></i>
								#elseif($loginBean.operationMap.get("/CRMBrotherAction.do?tableName=$tableName").query())
									<i title='查看' class="read_i" onclick="detailBroBill('$brother.get("id")','$tableName','$globals.getTableInfoBean("$tableName").display.get("$globals.getLocale()")');"></i>
								#end
							</div>
							</td>
							#end
							#if("$!oldPath" == "true")
								#foreach($col in $fieldlist)
									#set($remarkClass = "")
									#if("$col.fieldType" == "3" || ("$col.fieldType" == "16"))
										#set($remarkClass = "space")
									#end
									#if("$!col.asFieldName" != "ComFullName")
										#set($fieldVal = "")
										#if("$!col.inputType"=="1")
											#if("$!brother.get($col.asFieldName)" != "") #set($fieldVal = $!globals.getEnumerationItemsDisplay("$!col.popUpName","$!brother.get($col.asFieldName)")) #end
										#else
											#if("$!col.fieldType"=="1")
												#set($val = $!brother.get("$col.asFieldName"))
												#set($fieldVal = $!globals.dealDoubleDigits("$val","$col.asFieldName"))
											#else
												#set($fieldVal = $!brother.get("$col.asFieldName"))
													
											#end
										#end
										<td class="$!remarkClass">
											<div class="item_dv" title="$fieldVal">$fieldVal</div>
										</td>
									#end
								#end
							#else
								#foreach($fieldName in $fieldDisplayBean.listFields.split(","))
									#set($remarkClass = "")
									#set($onmouseoverStr = "")
									#set($field = $globals.getFieldBean($tableName,$fieldName))
									#if("$field.fieldType" == "3" || ("$field.fieldType" == "16"))
										#set($remarkClass = "space")
										#set($onmouseoverStr = "onmouseover='showTxt(this);'")
									#end
									
									
									#if("$field.fieldName" != "ClientId")
										#set($fieldVal = "")
										#if("$field.inputType"=="0")
											#if("$field.fieldType" == "1")
												#set($fieldVal = $!globals.dealDoubleDigits("$!brother.get($field.fieldName)","amount"))
											#elseif("$field.fieldType" == "18" || "$field.fieldType" == "3" || "$field.fieldType" == "16")
												#set($fieldVal = $globals.replaceHTML($!brother.get($field.fieldName)))
											#elseif("$fieldBean.fieldType" == "13"||"$fieldBean.fieldType" == "14")
											
											#else
												#set($fieldVal = $!brother.get($field.fieldName))
											#end
										#elseif("$field.inputType"=="1" || "$field.inputType"=="5")
											#set($fieldVal = $globals.getEnumerationItemsDisplay("$field.refEnumerationName","$!brother.get($field.fieldName)"))	
										#elseif("$field.inputType"=="2")
											#if("$field.fieldName" == "ClientId")
												#set($fieldVal = $!brother.get("clientName"))
											#else
												#set($fieldVal = $!brother.get($field.fieldName))
											#end
										#elseif("$field.inputType"=="14")
											#set($fieldVal = $!deptMap.get($!brother.get($field.fieldName)))
										#elseif("$field.inputType"=="15")
											#set($fieldVal = $!globals.getEmpFullNameByUserId($!brother.get($field.fieldName)))
										#elseif("$field.inputType"=="20")
											#set($fieldVal = $!relateClientMap.get($!brother.get($field.fieldName)))
										#else
											#set($fieldVal = $!brother.get($field.fieldName))
										#end
										<td class="$!remarkClass">
											<div class="pr item_dv" title="$fieldVal" $onmouseoverStr>
												$fieldVal
											</div>
										</td>
									#end
								#end
							#end
					</tr>
					#end
				 	</tbody>
				</table>
			</div>
		</div>
	#end
