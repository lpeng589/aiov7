#if($LoginBean.id =="1") 
<form method="post" name="colConfigForm" action="/ColConfigAction.do">
<input type="hidden" name="popupName" value="$!popupName"/>
<input type="hidden" name="operation" value="1">
<input type="hidden" name="strAction" value="$strAction" />
<input type="hidden" name="colType" value="popup" />
<input type="hidden" name="parentCode" value="$!parentCode">
<input type="hidden" name="tableName" value="$!tableName">
<input type="hidden" name="reportNumber" value="$!reportNumber">
<input type="hidden" name="fieldName" value="$!fieldName">
<input type="hidden" name="MOID" value="$!MOID">
<input type="hidden" name="MOOP" value="$!MOOP">
<input type="hidden" id="colSelect" name="colSelect" value="" />
#if($!mainId.length() > 0)
<input type="hidden" name="mainId" value="$!mainId">
<input type="hidden" name="mainPop" value="$!mainPop">
#end
#set($strTableName="$!tableName")
#if("$!tableName"=="") 
	#set($strTableName=$reportNumber)
#end
<input type="hidden" name="colSelect" value="" />

<div id="DivCustomSetTable" style="display:none;position:absolute;width:480px; background: #F0FCFF; border: 1px solid #0099CC" onMouseDown="getFocus(this)">
<IFRAME width=480 height=280 style="position:absolute;top:-1px;left:-1px;z-index:-1;border-style:none;filter:Alpha(opacity=0)" frameborder="0"></IFRAME>
    <div  id="colNameSetting" style="display: block;">    
    <table width="100%" align="center" border=0 id="CustomSetTable" cellpadding="0" cellspacing="0" class="BgTable">
       <tr style=" height:25px; background:#92D8FF url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -170px; border-bottom: 1px solid #F0FCFF">
        <td colspan="5" width="300" style="cursor: move;text-align: left;font-weight: bold;" align="left" onMouseDown="moveDown(DivCustomSetTable)">&nbsp;$text.get("com.define.colconfig")</td>
        <td align="right" >
          <a href="javascript:void(0)" title="$text.get("common.lb.close")" onclick="document.getElementById('CustomDivIsFocus').value='0';hiddenDivCustomSetTable();"/><img src="../$globals.getStylePath()/images/del.gif">&nbsp;</a>
        </td>
      </tr>
      <tr align="center" valign="middle" class="BgRow">
        <td width="5%" align="left">&nbsp;<br></td>
        <td width="20%">
          <fieldset style="border:0;font-size:12px;">
            <legend>$text.get("com.define.canselect")</legend>   
			<select name='FieldsToSelectList' id='FieldsToSelectList' multiple="multiple" size="10" class="MultiSelect" style="width:130px" >
					#foreach($popup in $oldCols)
						#if(!$newCols.contains($popup))
						#if($popup.getDisplay())
							#set($fieldName=$popup.getDisplay().replace("@TABLENAME", $!tableName))
						#else
							#set($fieldName=$popup.fieldName)
						#end						
						<option value="$popup.fieldName">$globals.getFieldDisplay($strTableName ,$!popupName,$fieldName,"list")</option>
						#end
					#end
			</select>
          </fieldset>
        <br></td>
        <td>
          <button type="button" class="ActionButton" style="width:75px" onClick="colWidthSet();">$text.get("com.config.colwidth")</button><br><br>
          <button type="button" class="ActionButton" style="width:75px" onClick="addItem('FieldsToSelectList','FieldsToShowList')">&gt;&gt;</button><br/><br>
          <button type="button" class="ActionButton" style="width:75px" onClick="popDelItem('FieldsToShowList','FieldsToSelectList')">&lt;&lt;</button>
        <br></td>
        <td>
          <fieldset style="border:0;font-size:12px;">
           <legend>$text.get("com.define.myselect")</legend>
		   <select name='FieldsToShowList' id='FieldsToShowList' multiple="multiple" size="10" class="MultiSelect" style="width:130px" >
		   		#foreach($col in $newCols)
		   			#if($col.getDisplay())
						#set($fieldName=$col.getDisplay().replace("@TABLENAME", $!tableName))
					#else
						#set($fieldName=$col.fieldName)
					#end
					<option value="$col.fieldName">$globals.getFieldDisplay($strTableName,$!popupName,$fieldName,"list")</option>
				#end
		   </select>
         </fieldset>
        <br></td>
        <td align="left">
          <button type="button"  style="width:30px" onClick="JavaScript:upItem('FieldsToShowList')">&uarr;</button><br /><br />
          <button type="button"  style="width:30px" onClick="JavaScript:downItem('FieldsToShowList')">&darr;</button>
        <br></td>
      </tr>
      <tr>
        <td colspan="5" align="center" style="padding:10px 0 6px">
          <button type="button" class="ActionButton" onClick="submitForm2();">$text.get("common.lb.ok")</button>
          <button type="button" class="ActionButton" onClick="setDefault();">$text.get("com.define.defaultField")</button>
        <br></td>
      </tr>
      <tr>
      	<td colspan="5" style="cursor: move;height: 20px;" align="left" onMouseDown="moveDown(DivCustomSetTable)"> <br></td>
      </tr>
    </table>
    </div>
    <input type="hidden" name="CustomDivIsFocus"  id="CustomDivIsFocus" value="" />
    <input type="hidden" name="FieldsToShow"  id="FieldsToShow" value="" />
    <div id="colWidthSetting" style="display: none;">
  	 <table width="100%" align="center" border=0 id="CustomSetTable2" cellpadding="0" cellspacing="0"  class="BgTable">
     <tr style=" height:25px; background:#92D8FF url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -170px; border-bottom: 1px solid #F0FCFF">
      	<td width="8%"></td>
        <td colspan="5" width="300" style="cursor: move;text-align: left;font-weight: bold;" align="left" onMouseDown="moveDown(DivCustomSetTable)">&nbsp;$text.get("com.config.colwidth")</td>
		<td align="right" >
          <a href="javascript:void(0)" title="$text.get("common.lb.close")" onclick="document.getElementById('CustomDivIsFocus').value='0';hiddenDivCustomSetTable();"/><img src="../$globals.getStylePath()/images/del.gif"></a>&nbsp;
        </td>
      </tr>
            <tr>
      	<td colspan="6" align="left" >&nbsp;</td>
      </tr>
			<td colspan="6">
				<div id="userSettingWidth" style="float: left;width: 420px;margin-right: 20px;margin-bottom: 7px;">
					#set($tdIndex=1)
					#foreach($col in $newCols)
						#if($col.getDisplay())
							#set($fieldName=$col.getDisplay().replace("@TABLENAME", $!tableName))
						#else
							#set($fieldName=$col.fieldName)
						#end
						#set($userWidth = $globals.getFieldWidth($!tableName,$col.fieldName))
						<li style="float: left;width: 210px;height: 22px;  line-height: 22px;">
								<span style="float: left;text-align:right;width: 120px;">$globals.getFieldDisplay($strTableName,$!popupName,$!fieldName,"list") ：</span>
								<input type="text" id="tdWidth$tdIndex" name="$!tableName:$col.fieldName" onKeyDown="keyIsNumber();"
											style="width: 50px; border:0px;height: 18px; border: 1px solid #006699;" value="#if("$userWidth"=="0")$col.width#else$userWidth#end"/>
						</li>
						#set($tdIndex=$tdIndex+1)
					#end
				</div>
			</td>
	  <tr>
	  	<td colspan="6" align="center">
	  		 <button type="button"  onclick="beforeUpdate();" style="width: 50px;">$text.get("common.lb.ok")</button>
	  		 <button type="button" onClick="returnColConfig();">$text.get("com.return.config")</button>
          	 <button type="button" style="width: 50px;" onClick="setDefaultColWidth();">$text.get("com.define.defaultField")</button>
	  	</td>
	  </tr>
      <tr>
      	<td colspan="6" style="cursor: move;height: 20px;" align="left" onMouseDown="moveDown(DivCustomSetTable)">&nbsp;</td>
      </tr>
    </table>
  </div>
  </div>
</form>
#end