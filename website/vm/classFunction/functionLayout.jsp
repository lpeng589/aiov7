

		<div class="listRange_1" id="listRange_mainField">
		<ul class="wp_ul"> 
#set($totalFields=0)
#foreach ($row in $fieldInfos ) 
	#if($row.fieldName == "id")
	#elseif("$row.inputType"=="17")
	<li class="cutLine" style="width:100%;height:14px;font: 500 12px '宋体';"><span  style="width:auto">$row.display.get("$globals.getLocale()")</span></li>	
	#elseif("$row.fieldSysType"=="MainBarCodeScan" && "$row.inputType"!="100")
		<li><div class="swa_c1"  title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2"> <input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" size="20" class="input_type" onkeydown="if(event.keyCode==13)parseDown(event);" onclick="parseDown(event)" value=""></div></li>
	#elseif("$row.inputType" == "100" && "$row.fieldName"!="moduleType")
		<input type="hidden" id="$row.fieldName" name="$row.fieldName" />
	#elseif($row.fieldName == "f_brother")
		<input type="hidden" id="f_brother" name="f_brother" />
	#elseif("$row.inputType" != "100" && "$row.fieldType" != "3"  && "$row.fieldType" != "16"  && "$row.fieldType" != "13" && "$row.fieldType" != "14" && $row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime" && "$row.fieldName"!="moduleType")
		#if("$row.inputType" == "0")
			#if("$row.fieldType" == "17")
				<li><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" class="input_type" name="$row.fieldName" type="text" onfocus="mainFocus(this);" size="20" onKeyDown="if(event.keyCode==13){ if(this.value.length>0){ tabObj.next(this); }else{  _SetTime(this);}}" onClick="_SetTime(this);" ></div></li>
			#elseif("$row.fieldType" == "5" )
				<li><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" size="20" onKeyDown="if(event.keyCode==13){tabObj.next(this);}" class="input_type" onClick="WdatePicker({lang:'$globals.getLocale()'});"></div></li>
			#elseif("$row.fieldType" == "6")
				<li><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" size="40" class="inputMid" onKeyDown="if(event.keyCode==13){tabObj.next(this);}" class="input_type" onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});"></div></li>
			#elseif("$row.fieldType" == "19")
				<li><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="password" size="20" onKeyDown="if(event.keyCode==13) tabObj.next(this);" class="input_type" ></div></li>
			#elseif("$row.fieldType" == "1")
				<li><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" size="20" onKeyDown="if(event.keyCode==13) tabObj.next(this);" class="input_type" ></div></li>
			#elseif("$row.fieldType" == "18")
				<li class="longChar2"><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" class="inputLong input_type" onKeyDown="if(event.keyCode==13) tabObj.next(this);" ></div></li>
			#elseif("$row.fieldType" == "20")
				<li class="longChar"><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" size="40" class="inputMid input_type" onKeyDown="if(event.keyCode==13){tabObj.next(this);}" class="input_type"></div></li>
			#else
				<li><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end  title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" #if($row.fieldName=="Principal")onblur="setCapital(this);" #end onfocus="mainFocus(this);" size="20" onKeyDown="if(event.keyCode==13) tabObj.next(this);" class="input_type"></div></li>
			#end
			#if("$row.width"=="0")
			<li style="float: none;width: 1px;"></li>
			#end
			#set($totalFields=$totalFields+1)		
		#elseif("$row.inputType" == "8"&&"$row.inputTypeOld" != "2") ##只读
			#if("$row.inputTypeOld" == "1")
				<li>
					<div class="swa_c1"  style="color:#717171;" title='$row.display.get("$globals.getLocale()")'  style="color:#c0c0c0;">
						<div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div>
					</div>
					
					<div class="swa_c2" >
						<input id="${row.fieldName}Name" name="${row.fieldName}Name" type="text" onfocus="mainFocus(this);" size="20" style="color: #717171;" readonly="readonly" onKeyDown="if(event.keyCode==13) tabObj.next(this);" class="input_type" title="$enumName" />	
						<select id="$row.fieldName" name="$row.fieldName" class="input_type" style="display:none" selectRName="${row.fieldName}Name" >
						<option title="" value="" ></option>
						#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
						<option title="$erow.name" value="$erow.value" >$erow.name</option>
						#end
						</select>
					</div>
				</li>	 
			#elseif("$row.inputTypeOld" == "5")
				<li #if("$row.fieldType" == "18") class="longChar2" #elseif("$row.fieldType" == "20") class="longChar" #end  ><div class="swa_c1"  title='$row.display.get("$globals.getLocale()")' style="color:#c0c0c0;"><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>
					<div class="swa_c2" >
					#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
					<input name="$row.fieldName" type="checkbox" class="cbox " onKeyDown="if(event.keyCode==13)tabObj.next(this);"  
						style="color: #717171;width:15px;border:0px;" onclick="return false;" id="cbox_${row.fieldName}_$erow.value" value="$erow.value"/>
						<label for="cbox_${row.fieldName}_$erow.value" class="cbox_w">$erow.name</label>
					#end
					</div>  
				</li>
			#elseif("$row.inputTypeOld"=="10")
			#set($enumList=$globals.getEnumerationItems($row.refEnumerationName))
			<li #if("$row.fieldType" == "18") class="longChar2" #elseif("$row.fieldType" == "20") class="longChar" #end><div class="swa_c1"  title='$row.display.get("$globals.getLocale()")' style="color:#c0c0c0;"><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>
				<div class="swa_c2" >
				#foreach($erow in $enumList)
					<input type="radio" class="cbox " onclick="return false;" id="cbox_${row.fieldName}_$erow.value" name="$row.fieldName" value="$erow.value" />
					<label for="cbox_${row.fieldName}_$erow.value" class="cbox_w">$erow.name</label>
				#end
				</div>
			</li>			
			#else
				#if("$row.fieldType" == "18")
				<li class="longChar2"><div class="swa_c1"  title='$row.display.get("$globals.getLocale()")'  style="color:#c0c0c0;"><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" style="color: #302B2B;" readonly="readonly"  class="inputLong  input_type" onKeyDown="if(event.keyCode==13) event.keyCode=9" ></div></li>
				#elseif("$row.fieldType" == "20")
				<li  class="longChar"><div class="swa_c1"  title='$row.display.get("$globals.getLocale()")' style="color:#c0c0c0;"><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" size="40" style="color: #302B2B;" readonly="readonly"  class="inputMid  input_type" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" ></div></li>		
				#else 
				<li><div class="swa_c1"  title='$row.display.get("$globals.getLocale()")' style="color:#c0c0c0;"><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" size="20" style="color: #302B2B;" readonly="readonly" onKeyDown="if(event.keyCode==13) event.keyCode=9" ></div></li>					
				#end
			#end
			#set($totalFields=$totalFields+1)
			#if("$row.width"=="0")
				<li style="float: none;width: 1px;"></li>
			#end
		#elseif("$row.inputType" == "1")
			<li #if("$row.fieldType" == "18")class="longChar2"#end>
				<div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'>
					<div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div>
				</div>
				<div class="swa_c2" >
			<select id="$row.fieldName"  onfocus="mainFocus(this);" name="$row.fieldName" onKeyDown="if(event.keyCode==13) tabObj.next(this);"  class="input_type">
			<option title="" value="" ></option>
			#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
				<option title="$erow.name" value="$erow.value" >$erow.name</option>
			#end
			</select>
			</div>
			</li>
			#if("$row.width"=="0")
				<li style="float: none;width: 1px;"></li>
			#end
			#set($totalFields=$totalFields+1)
		#elseif("$row.inputType" == "16") 
			<li #if("$row.fieldType" == "18")class="longChar2"#end><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>
			<div class="swa_c2" >
			<select id="$row.fieldName" name="$row.fieldName" onKeyDown="if(event.keyCode==13) tabObj.next(this);" class="input_type"  >
			
			</select>
			</div>
			</li>
			#if("$row.width"=="0")
				<li style="float: none;width: 1px;"></li>
			#end
			#set($totalFields=$totalFields+1)
		#elseif("$row.inputType" == "3" || "$row.inputType" == "6" ) 
			<input id="$row.fieldName" name="$row.fieldName" type="hidden">	
		#elseif("$row.inputType" == "7")
			#set($pymUrl="/UtilServlet?operation=ajaxPYM&type=updatePYM")
			<li #if("$row.fieldType" == "18") class="longChar2" #elseif("$row.fieldType" == "20") class="longChar" #end><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" #if("$row.fieldType" == "18")class="inputLong input_type"#elseif("$row.fieldType" == "20")class="inputMid input_type"#else size="20" #end onKeyDown="if(event.keyCode==13)tabObj.next(this);" onchange="dyGetPYM('$pymUrl',this,'$row.fieldName')"  /></div></li>
		#elseif("$row.inputType"=="9")
			#set($reqUrl="/UtilServlet?operation=calculate")
			#if("$row.fieldType" == "1")
			<li><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" size="20" class="input_type" onKeyDown="if(event.keyCode==13) dyGetCalculate('$reqUrl',this);" ></div></li>
			#else
			<li><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" size="20" class="input_type" onKeyDown="if(event.keyCode==13) tabObj.next(this);" ></div></li>
			#end
			#if("$row.width"=="0")  
			<li style="float: none;width: 1px;">t</li>
			#end
		#elseif("$row.inputType" == "4")
			#set($popUrlValue="mainMoreLanguage('$row.getStringLength()','$row.fieldName')")
			
			<li><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>
			
				#set($name=$row.fieldName+"_lan")
				<div class="swa_c2" >
				<input id="$name" name="$name" type="text" onfocus="mainFocus(this);"  size="20" onchange="changeMoreLanguage('$name','$row.fieldName')"  onKeyDown="if(event.keyCode==13&&this.value.length>0) tabObj.next(this);" class="input_type" popup="select" ><!--  onDblClick="$popUrlValue" -->
				<!--  <b class="stBtn icon16" onClick="$popUrlValue"></b>-->
				</div>
				<input id="$row.fieldName" name="$row.fieldName" type="hidden" />
			
			</li>
			#if("$row.width"=="0")
			<div align="left"><li style="float: none; width: 1px;"><br /></li></div>
			#end
		#elseif("$row.inputType" == "11")
			<li><div class="swa_c1" title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div><div class="swa_c2" ><input id="$row.fieldName" name="$row.fieldName" type="text" onfocus="mainFocus(this);" class="input_type" size="20" onKeyDown='if(event.keyCode==13) ${row.inputValue};' value=""></div></li>
		#elseif("$row.inputType"==5)
			#set($enumList=$globals.getEnumerationItems($row.refEnumerationName))
			<li #if("$row.fieldType" == "18") class="longChar2" #elseif("$row.fieldType" == "20") class="longChar" #end><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>
				<div class="swa_c2" >
					#foreach($erow in $enumList)						
						<input type="checkbox" class="cbox " id="cbox_${row.fieldName}_$erow.value" name="$row.fieldName"  value="$erow.value" /><label for="cbox_${row.fieldName}_$erow.value" class="cbox_w">$erow.name</label>
					#end
				</div>
			</li>	
		#elseif("$row.inputType"=="10")
			#set($enumList=$globals.getEnumerationItems($row.refEnumerationName))
			<li #if("$row.fieldType" == "18") class="longChar2" #elseif("$row.fieldType" == "20") class="longChar" #end><div class="swa_c1" #if($row.isNull==1)style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")'><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>
				<div class="swa_c2" >
				#foreach($erow in $enumList)
					<input type="radio" class="cbox " id="cbox_${row.fieldName}_$erow.value" name="$row.fieldName" value="$erow.value" /><label for="cbox_${row.fieldName}_$erow.value" class="cbox_w">$erow.name</label>
				#end
				</div>
			</li>			
		#elseif("$row.inputType" == "2"||"$row.inputTypeOld" == "2")
			#if($row.getSelectBean().relationKey.hidden)
				<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" type="hidden"/>
			#elseif("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
				#set($dismh="$row.fieldName"+"_mh")          
				#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))    
				#if($!row.logicValidate.indexOf("@IOO:Outstore")!=-1)
					#set($imgPopUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?operation="+$globals.getOP("OP_POPUP_SELECT")+"&type=stockSequence&seq=1',n('$dismh')[0],'$dismh')")
					#set($dbPopUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?operation="+$globals.getOP("OP_POPUP_SELECT")+"&type=addSequence&seq=1',n('$dismh')[0],'$dismh')")
				#else    
					#set($dbPopUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?operation="+$globals.getOP("OP_POPUP_SELECT")+"&type=addSequence&seq=1',n('$dismh')[0],'$dismh')")
					#set($imgPopUrlValue=$dbPopUrlValue)
				#end
				<li>
				<div class="swa_c1" #if($row.isNull==1 && "$row.inputType" != "8")style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")' #if("$row.inputType" == "8") style="color: #C0C0C0;" #end>
					<div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div>
				</div>
				<div class="swa_c2" >
				<input type="hidden" id="$row.fieldName" name="$row.fieldName"/>
				<input id="$dismh" name="$dismh" type="text" onfocus="mainFocus(this);" size="20" #if("$row.inputType" == "8") readonly="readonly" style="color: #302B2B;" #else
				  popup="select" class="input_type" onDblClick="$dbPopUrlValue" #end/>
				 #if("$row.inputType" != "8")
				 <b class="stBtn icon16" onClick="$imgPopUrlValue"></b>
				 #end
				 </div>
			#else
				#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))    
				#set($popUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?keyId=$!values.get($row.fieldName)&tableName=$mainTable.tableName&fieldName=$row.fieldName&operation="+$globals.getOP("OP_POPUP_SELECT")+"'#foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+nec('$mainField')#end,this,'$row.fieldName')")
				#set($popUrlValue2="if(popMainInput('$mainInput'))mainSelect2('/UserFunctionAction.do?keyId=$!values.get($row.fieldName)&tableName=$mainTable.tableName&fieldName=$row.fieldName&operation="+$globals.getOP("OP_POPUP_SELECT")+"'#foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+nec('$mainField')#end,'',this,'$row.fieldName')")
				<li>
					<div class="swa_c1" #if($row.isNull==1 && "$row.inputType" != "8")style="color:#f13d3d;"#end title='$row.display.get("$globals.getLocale()")' #if("$row.inputType" == "8") style="color: #C0C0C0;" #end><div class="d_box"><div class="d_test">$row.display.get("$globals.getLocale()")</div></div><div class="d_mh"></div></div>
					#if("$row.fieldType" == "1")
						<div class="swa_c2" >
						<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" 
						#if("$row.inputType" == "8") readonly="readonly" style="color: #302B2B;" #else class="input_type" #end
						 type="text" relationkey="true" onfocus="mainFocus(this);"  size="20" #if("$row.inputType" != "8")onKeyDown="if(event.keyCode==13&&this.value.length>0)tabObj.next(this);" popup="select" onDblClick="$popUrlValue" #end  />
						#if("$row.inputType" != "8")
							<b class="stBtn icon16" onClick="$popUrlValue"></b>
						#end 
						 </div>
					#else
						<div class="swa_c2">
						<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" 
						#if("$row.inputType" == "8") readonly="readonly" style="color: #302B2B;" #else class="input_type" #end
						type="text" relationkey="true" onfocus="mainFocus(this);"  size="20" #if("$row.inputType" != "8") onKeyDown="if(event.keyCode==13&&this.value.length>0)tabObj.next(this);"  popup="select" onKeyUp="$popUrlValue2" onfocus="this.oldValue=this.value; mainFocus(this);" onblur="cl(this);" onDblClick="$popUrlValue" #end/>
						#if("$row.inputType" != "8")
							<b class="stBtn icon16" onClick="$popUrlValue"></b>
						#end
						 </div>
					#end
					
				</li>
			#end ##relationKey=true且非序列号


			#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
				#foreach($srow in $row.getSelectBean().viewFields)	
					#set($totalFields=$totalFields+1)
					#set($colName="")
		 			#if($!srow.display!="")
						#if($!srow.display.indexOf("@TABLENAME")==0)
							#set($index=$srow.display.indexOf(".")+1)#set($tableField=$tableName+"."+$srow.display.substring($index))
						#else
							#set($tableField=$srow.display)
						#end
					#else
						#set($tableField="")
					#end
					#set($colName="$srow.asName")
					#set($viewFieldType=$globals.getFieldBean($srow.fieldName).fieldType)
					#if("$!srow.display" !="" && $!srow.display.indexOf(".")==-1)
						#set($dis = $srow.display)
					#else 
						#set($dis = $globals.getFieldDisplay($tableName,$row.getSelectBean().name,$tableField))
						#if($dis == "") #set($dis = $globals.getFieldDisplay($srow.fieldName)) #end
					#end
					#set($mainInput=$globals.getMainInputValue("$mainTable.tableName","$row.fieldName"))    
					#set($popUrlValue="if(popMainInput('$mainInput'))openSelect('/UserFunctionAction.do?keyId=$!values.get($row.fieldName)&tableName=$mainTable.tableName&fieldName=$row.fieldName&operation="+$globals.getOP("OP_POPUP_SELECT")+"'#foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+nec('$mainField')#end,this,'$row.fieldName')")
					#set($ajaxUrlValue="'/UtilServlet?operation=Ajax&tableName=$mainTable.tableName&fieldName=$row.fieldName' #foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+n('$mainField')[0].value#end")
					#set($openUrlValue="'/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=$mainTable.tableName&fieldName=$row.fieldName' #foreach($mainField in $row.getSelectBean().tableParams)+'&$mainField='+n('$mainField')[0].value#end")												
					#if($allConfigList.size()>0) 
						#if($globals.colIsExistConfigList("$mainTable.tableName","$colName","bill"))
		<li   #if($globals.getFieldBean($srow.asName).fieldType==18) class="longChar2" #elseif($globals.getFieldBean($srow.asName).fieldType==20) class="longChar" #end >		
			<div class="swa_c1" #if($row.isNull==1  && "$row.inputType" != "8")style="color:#f13d3d;"#end title='$dis' #if("$row.inputType" == "8") style="color: #C0C0C0;" #end><div class="d_box"><div class="d_test">$dis</div></div><div class="d_mh"></div></div>
			
								#if($globals.getFieldBean($srow.asName).fieldType==1)
			<div class="swa_c2" >
			<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #302B2B;" #end class="input_type" 
			#if("$row.inputType" != "8") onKeyup="if(popMainInput('$globals.getMainInputValue("$mainTable.tableName","$row.fieldName")'))mainSelect2($ajaxUrlValue,$openUrlValue,this,'$row.fieldName');" onblur="cl(this);" onfocus="this.oldValue=this.value; mainFocus(this);" popup="select" onDblClick="$popUrlValue" #end type="text" size="20"  />
			#if("$row.inputType" != "8")
				<b class="stBtn icon16" onClick="$popUrlValue"></b>
			#end
			</div>
								#else
			<div class="swa_c2">
			<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)"  #if("$row.inputType" == "8") readonly="readonly" style="color: #302B2B;" #end class="input_type" 
			#if("$row.inputType" != "8") onKeyUp="if(popMainInput('$globals.getMainInputValue("$mainTable.tableName","$row.fieldName")'))mainSelect2($ajaxUrlValue,$openUrlValue,this,'$row.fieldName');" onblur="cl(this);" onfocus="this.oldValue=this.value; mainFocus(this);" popup="select" onDblClick="$popUrlValue" #end type="text"  size="20" />
				#if("$row.inputType" != "8")
					<b class="stBtn icon16" onClick="$popUrlValue"></b>
				#end
			</div>
								#end  ##不为数字的情况 
		</li> 
							#set($tableField="")
						#end  ##列配置存在


					#else ##没有列配置时  
						#if("$srow.hiddenInput"=="true")
		<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" type="hidden"/>
		<input id="tbl_$globals.getTableField($srow.asName)" name="tbl_$globals.getTableField($srow.asName)" type="hidden" />
						#else
							
		<li #if($globals.getFieldBean($srow.asName).fieldType==18) class="longChar2" #elseif($globals.getFieldBean($srow.asName).fieldType==20) class="longChar" #end >
			<div class="swa_c1" #if($row.isNull==1  && "$row.inputType" != "8")style="color:#f13d3d;"#end title='$dis' #if("$row.inputType" == "8") style="color: #C0C0C0;" #end><div class="d_box"><div class="d_test">$dis</div></div><div class="d_mh"></div></div>
								#if($globals.getFieldBean($srow.asName).fieldType==1)
			<div class="swa_c2">
			<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #302B2B;" #end class="input_type" 
			 #if("$row.inputType" != "8")onKeyup="if(popMainInput('$globals.getMainInputValue("$mainTable.tableName","$row.fieldName")'))mainSelect2($ajaxUrlValue,$openUrlValue,this,'$row.fieldName');" onblur="cl(this);" onfocus="this.oldValue=this.value; mainFocus(this);" #end type="text" #if("$row.inputType" != "8") popup="select" onDblClick="$popUrlValue" #end  size="20"  />
			 #if("$row.inputType" != "8")
			 	<b class="stBtn icon16" onClick="$popUrlValue"></b>
			 #end
			 </div>
			
								#else
			<div class="swa_c2"  f >
			<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)" #if("$row.inputType" == "8") readonly="readonly" style="color: #302B2B;"  #end class="input_type"
			 #if("$row.inputType" != "8") onKeyUp="if(popMainInput('$globals.getMainInputValue("$mainTable.tableName","$row.fieldName")'))mainSelect2($ajaxUrlValue,$openUrlValue,this,'$row.fieldName');"  onblur="cl(this);" onfocus="this.oldValue=this.value; mainFocus(this);" #end type="text" onfocus="mainFocus(this);" #if("$row.inputType" != "8") popup="select" onDblClick="$popUrlValue" #end  size="20"  />
			 #if("$row.inputType" != "8")
			 	<b class="stBtn icon16" onClick="$popUrlValue"></b>
			 #end
			 </div>
								#end
		</li>
							#set($tableField="")
						#end  ##hiddenInput不为true
					#end	##没有列配置结束时			
				#end	##弹出窗显示字段循环			
			#end	##非序列号
		#end ##弹出窗字段inputType=2
	#end  ##不等于100 3 createBy等,字段内部处理
#end ## 循环处理字段fieldInfos
<div style="clear:both;"></div>
</ul>
</div>
#foreach ($row in $fieldInfos )
	#if("$row.inputType" != "100" && "$row.inputType" !="3" 
			&& "$row.fieldType" == "16" && "$row.inputType"!="6" && "$row.width"=="-1")
		#set($defValue="")
		#if($globals.getUrlBillDef($row.fieldName).length()>0)
		#set($defValue=$globals.getUrlBillDef($row.fieldName))
		#elseif($globals.getUrlBillDef($row.fieldName).length()==0)
		#set($defValue=$row.getDefValue())
		#end
		<div class="listRange_1_photoAndDoc_2" style="border: 1px solid red;"><span class="docTitle">$row.display.get("$globals.getLocale()")：#if($row.isNull==1)<font color="red">*</font>#end</span>
		<div style="margin-left: 6px;float: left;">&nbsp; </div>
		<div>
		<textarea id="$row.fieldName"  name="$row.fieldName" style="width:92%;height:300px;visibility:hidden;"></textarea>
		</div>
		</div>
	#end

	#if("$row.inputType" != "100" && "$row.inputType" !="3"  && "$row.inputType"!="6" && "$row.fieldType" == "3" && "$row.width"=="-1")
	#if($row.maxLength>5000)#set($rows=8)#else#set($rows=5)#end
	<div class="listRange_1_photoAndDoc_1">
	<span #if("$row.inputType"=="8")style="color: #C0C0C0;"#end>$row.display.get("$globals.getLocale()")：#if($row.isNull==1)<font color="red">*</font>#end</span>
	<textarea id="$row.fieldName" name="$row.fieldName" #if("$row.inputType"=="8") style="color: #302B2B;" readonly="readonly" #else class="input_type" #end rows="$!rows"></textarea>
	</div>
	#end  
#end ##备注HTML框宽度为-1时的显示

#if("$globals.getSysSetting('careforAction')"=="true" && "$!fromCRM"=="detail")
<div style="margin-left:3px;float:left;margin-top:3px; height:200px;padding-bottom:3px;width:99%;border:2px solid #DAEDFE; overflow-x:hidden;overflow-y:auto;">
<iframe src="/CareforExecuteAction.do?operation=4&type=iframe&clientId=$values.get('id')" width="100%" height="200" scrolling="no"  frameborder=false></iframe>
</div>
#end
#foreach ($rowlist in $childTableList )
<input type="hidden" id="${rowlist.tableName}PopupType" name="${rowlist.tableName}PopupType" value="$globals.getTableInfoBean(${rowlist.tableName}).popupType"/> 
#end
	 
<div class="showGridTags">
#set($hasChildTable = false)
#foreach ($rowlist in $childTableList ) 
	#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$rowlist.isUsed"=="1" && "$!rowlist.isView"=="0")
		#set($gridtable =$rowlist.tableName+"Table")
		#if($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0)
			#set($hasChildTable = true)
			<span show="${gridtable}DIV" id="${gridtable}DIVTitle" name="detTitle" class="tags" onclick="changeTags(this);">$rowlist.display.get($globals.getLocale())</span>
		#end	
	#end
#end  ##明细区显示明细表

#foreach ($row in $fieldInfos )
	#if("$row.inputType" != "100" && "$row.inputType" !="3" && ("$row.fieldType" == "13" || "$row.fieldType" == "14") && "$row.inputType"!="6")	
	<span show="$!{row.fieldName}DIV" class="tags" name="detTitle" onclick="changeTags(this);">$row.display.get($globals.getLocale()) <img src="/style/images/mail/attach.gif" border=0 /> </span>
	#end
#end ##明细区显示图片和附件
#if($hasChildTable && "$detail"=="detail") ##在明细区显示各种按扭和条码扫描
	<div class="Detzoom"> 
	     <span class="zoomUp"  title="表格全屏" onselectstart="return false;"></span>
	</div>
#end
#if($hasChildTable && "$detail"!="detail") ##在明细区显示各种按扭和条码扫描


	<div class="detbtBar">
		<li id='b_calc' class='f-icon16' onclick="cellCalc()" title='计算器'></li>
		<li id='b_dragcel' class='f-icon16' onclick="cellDrag()" title='拖选复制单元格'></li>
		<li id='b_upline' class='f-icon16' onclick="celupline()"  title='上移'></li>
		<li id='b_downline' class='f-icon16' onclick="celdownline()" title='下移'></li>
		<li id='b_addline' class='f-icon16' onclick="celladdline()" title='插入一行'></li>
		<li id='b_stickline' class='f-icon16' onclick="cellstickline()" title='复制整行'></li>
		<li id='b_delline' class='f-icon16' onclick="celldelline()" title='删除本行'></li>
	</div>
	<div class="Detzoom"> 
	     <span class="zoomUp"  title="表格全屏" onselectstart="return false;"></span>
	</div>
	#set($hasGoodsbc = false)
	#if($!globals.getSysSetting("scanType") == "1")
		#foreach ($rowlist in $childTableList )  
			#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$!rowlist.isUsed"=="1" && "$!rowlist.tableName" != "tblGoodsUnit"  && "$!rowlist.isView"=="0"&& ($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0))
				#foreach ($row in $rowlist.fieldInfos ) 
					#if(!$hasGoodsbc && "$row.fieldName"=="GoodsCode"  && $row.inputType != 3) #set($hasGoodsbc = true) 
					<div class="tags" style="margin-left:50px;padding:0px 10px 0px 10px">
						<label >条码扫描</label>
						<input type="text" id="scanBarcode" name="scanBarcode" onkeydown="if(event.keyCode==13){ scanBarCodeEv(this,'$rowlist.tableName',true);stopBubble(event);}"/>
						<label style="vertical-align: middle;"><img src="/style/images/wh.png" height="17" style="margin-top:-2px" onclick="scanBarCodeHelp('barcode')"/></label>
					</div>
					#end
				#end
			#end	
		#end
	#elseif($!globals.getSysSetting("scanType") == "2")
		#foreach ($rowlist in $childTableList )  
			#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$!rowlist.isUsed"=="1" && "$!rowlist.tableName" != "tblGoodsUnit"  && "$!rowlist.isView"=="0"&& ($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0))
				#foreach ($row in $rowlist.fieldInfos ) 
					#if(!$hasGoodsbc && "$row.fieldName"=="GoodsCode"  && $row.inputType != 3)   #set($hasGoodsbc = true) 
					<div class="tags" style="margin-left:50px;padding:0px 10px 0px 10px">
						<label >条码扫描</label>
						<input type="text" id="scanBarcode" name="scanBarcode" onkeydown="if(event.keyCode==13){ scanBarCodeEv(this,'$rowlist.tableName',false);stopBubble(event);}"/>
						<label style="vertical-align: middle;"><img src="/style/images/wh.png" height="17" style="margin-top:-2px" onclick="scanBarCodeHelp('barcode')"/></label>
					</div>
					#end
				#end
			#end	
		#end
	#elseif($!globals.getSysSetting("scanType") == "3")
		#foreach ($rowlist in $childTableList ) 
			#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$!rowlist.isUsed"=="1"  && "$!rowlist.isView"=="0"&& ($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0)) 
				#foreach ($row in $rowlist.fieldInfos ) 
					#if("$row.fieldName"=="Seq"  && $row.inputType != 3 && "$row.logicValidate"=="@IOO:Outstore")
					<div class="tags" style="margin-left:50px;padding:0px 10px 0px 10px">
						<label >序列号扫描</label>
						<input type="text" id="scanBarcode" name="scanBarcode" onkeydown="if(event.keyCode==13) scanSeqEv(this,'$rowlist.tableName');"/>
						<label style="vertical-align: middle;"><img src="/style/images/wh.png" height="17" style="margin-top:-2px" onclick="scanBarCodeHelp('seq')"/></label>
					</div>
					#end
				#end
			#end	
		#end	
	#end		
#end 
</div>	 <!-- 明细头部tab区 -->

<div class="scroll_function_small_ud" id="listRange_tableInfo" >
#foreach ($rowlist in $childTableList ) 
	#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$rowlist.isUsed"=="1" && "$!rowlist.isView"=="0")
		#set($gridtable =$rowlist.tableName+"Table")
		<script language="javascript">
		var tableId="$gridtable";   
		</script>
		#if($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0)
			<div  name="${gridtable}DIV" id="${gridtable}DIV"  ah="${gridtable}"  > 
				<table border="0" cellpadding="0"  cellspacing="0" class="listRange_list_function_b" name="$gridtable" id="$gridtable">
				</table>		
			</div>
		#end	
	#end
#end   ##显示明细表



#foreach ($row in $fieldInfos )	
	#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "13" && "$row.inputType"!="6")	
	<div  name="$!{row.fieldName}DIV" id="$!{row.fieldName}DIV" attType="PIC" style="border:1px solid #dedede; overflow-y:auto"> 			
		<div class="listRange_1_photoAndDoc_1">		
		#if("$detail" != "detail")
		#if($!globals.getSysSetting('picPath')=='')
		<span class="btn btn-mini" id="picbuttonthe222" name="picbuttonthe222"  onClick="upload('PIC','$!{row.fieldName}');">$text.get("upload.lb.picupload")</span>
		<span class="btn btn-mini" name="picbuttonthe333" onClick="linkPIC('$!{row.fieldName}');">链接图片</span>	
		#else
		<span class="btn btn-mini" name="picbuttonthe333" onClick="selectPIC('$!{row.fieldName}');">选择图片</span>	
		#end
		#end	
		<ul id="picuploadul_$!{row.fieldName}" >
		
		</ul>
		</div>
	</div>	
	#elseif("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "14" && "$row.inputType"!="6")	
	<div  name="$!{row.fieldName}DIV" id="$!{row.fieldName}DIV"  attType="ATT" style="border:1px solid #dedede; overflow-y:auto"> 			
		<div class="listRange_1_photoAndDoc_1">		
		<span class="btn btn-mini" id="affixbuttonthe222"  name="affixbuttonthe222"  onClick="upload('AFFIX','$!{row.fieldName}');">$text.get("upload.lb.affixupload")</span>		
		<ul id="affixuploadul_$!{row.fieldName}" style="width:98%;">
		
		</ul>		
		</div>
	</div>	
	#end	
#end  ##显示图片和附件


	
</div> <!-- 明细图片区 -->

<div id="listRange_remark"><!-- 显示备注和html编辑框 -->
#foreach ($row in $fieldInfos )	
	#if("$row.inputType" != "100" && "$row.fieldType" == "3"  && "$row.width"!="-1")
		#if("$row.inputType" !="3" && "$row.inputType"!="6" )
			#if($row.maxLength>5000)#set($rows=8)#else#set($rows=5)#end
			<div class="listRange_1_photoAndDoc_1">
			<span #if("$row.inputType"=="8")style="color: #C0C0C0;" #end>$row.display.get("$globals.getLocale()")：#if($row.isNull==1)
			<font color="red">*</font>#end</span>
			<textarea id="$row.fieldName" name="$row.fieldName" #if("$row.inputType"=="8") style="color: #302B2B;" readonly="readonly"  #end rows="$!rows"></textarea>
			</div>
		#else
		<input type="hidden"  id="$row.fieldName" name="$row.fieldName" />
		#end
	#end	##备注
	
	#if("$row.inputType" != "100" && "$row.fieldType" == "16"   && "$row.width"!="-1")
		#if("$row.inputType" !="3" && "$row.inputType"!="6")
		<div class="listRange_1_photoAndDoc_2" >
			<span >$row.display.get("$globals.getLocale()")：#if($row.isNull==1)<font color="red">*</font>#end</span>
			<div style="margin-left: 6px;float: left;">&nbsp; </div>
			<div>
			#if("$!Safari"=="true")
			<textarea id="$row.fieldName"  name="$row.fieldName" style="display:none;"></textarea>
			#else
			<textarea id="$row.fieldName"  name="$row.fieldName" style="width:92%;height:300px;visibility:hidden;"></textarea>
			#end
			</div>
		</div>
		#else
		<input type="hidden"  id="$row.fieldName" name="$row.fieldName" /> 
		#end
	#end  ##html编辑器


#end ##备注html区   
</div>
<div><span style="color:#0000FF;line-height:20px">$!mainTable.tableDesc.replaceAll("\r\n","<br>")</span></div>
$!globals.getDefineJs($mainTable.tableName)
