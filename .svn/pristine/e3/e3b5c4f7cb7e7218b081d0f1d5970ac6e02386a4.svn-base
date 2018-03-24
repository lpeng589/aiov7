<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("com.define.colconfig")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="$globals.js("/js/Main.vjs","",$text)"></script>
<script language="javascript"> 
function colWidthSet(){
    document.getElementById("colWidthSetting").style.display="block" ;	
    document.getElementById("colNameSetting").style.display="none" ;	
}

function returnColConfig(){
    document.getElementById("colWidthSetting").style.display="none" ;	
    document.getElementById("colNameSetting").style.display="block" ;	
}

function keyIsNumber()
{
	var keyCode = window.event.keyCode ;
	if(((keyCode>47)&&(keyCode<58))|| ((keyCode>95)&&(keyCode<106)) || (keyCode==8)||(keyCode==46))
	{

	}
	else if(keyCode==9 || keyCode==13)
	{
		window.event.keyCode=9;
	}
	else
	{
		window.event.returnValue=false;
	}
}

function colNameSet(strUrl){
	window.parent.returnValue = "colNameSet@@"+strUrl;
	window.close() ;
}
</script>
</head>

<body>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitlesmall">$text.get("com.define.colconfig")</div>
	<ul class="HeadingButton">
		<li><button type="button" onClick="javascript:window.close();" class="b2">$text.get("com.lb.close")</button></li>
	</ul>
	<br />
	<div align="center" style=" clear:both; margin-top: 50px;">
	<div id="DivCustomSetTable" align="center" style="width:480px;height:250px; background: #F0FCFF; border: 1px solid #0099CC"">
    <div id="colNameSetting" style="display: block;">
    <table width="100%" align="center" border=0 id="CustomSetTable" cellpadding="0" cellspacing="0" class="BgTable">
	<tr style=" height:25px; background:#92D8FF url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -170px; border-bottom: 1px solid #F0FCFF">
        <td colspan="5" width="300" align="left">&nbsp;$text.get("com.define.colconfig")</td>
        <td align="right" >&nbsp;
         	 
        </td>
      </tr>
      <tr align="center" valign="middle" class="BgRow">
        <td>
          <fieldset style="border:0;font-size:12px;">
            <legend>$text.get("com.define.canselect")</legend>   
			<select name='FieldsToSelectList' id='FieldsToSelectList' multiple="multiple" size="10" class="MultiSelect" style="width:120px" >
			   #if($!configField.size()>0) 
				   #foreach($field in $!disFields2)
				 	 #if($field.width!='0')	
				 		#if(!$configField.contains($field))		 	
						<option value='$field.asFieldName'>$field.display</option>
						#end
					 #end
				   #end
			   #else
			   	   #foreach($field in $!disFields2)
			   	   	 #if($globals.getScopeRight($globals.getFieldBean($field.fieldName).tableBean.tableName,$globals.getFieldBean($field.fieldName).fieldName,$scopeRight))
				 	 #if($field.width!='0' && "$field.inputType"=="6")	
						<option value='$field.asFieldName'>$field.display</option>
					 #end
					 #end
				   #end
			   #end
			</select>
          </fieldset>
        </td>
        <td>
          <button type="button" class="ActionButton" style="width:75px" onClick="colWidthSet();">$text.get("com.config.colwidth")</button><br><br>
          <button type="button" class="ActionButton" title="$text.get('com.config.right.move')" style="width:75px" onClick="addItem('FieldsToSelectList','FieldsToShowList')">&gt;&gt;</button><br><br>
          <button type="button" class="ActionButton" title="$text.get('com.config.left.move')" style="width:75px" onClick="delItem('FieldsToShowList','FieldsToSelectList')">&lt;&lt;</button><br><br>
          #if("$!reportType"!="reportList")
          <button type="button" class="ActionButton" style="width:75px" onClick="colNameSet('/ColDisplayAction.do?operation=7&keyId=$!tableName&fromConfig=config');">$text.get("com.colName.setting")</button>
          #end
        </td>
        <td>
          <fieldset style="border:0;font-size:12px;">
           <legend>$text.get("com.define.myselect")</legend>
		   <select name='FieldsToShowList' id='FieldsToShowList' multiple="multiple" size="10" class="MultiSelect" style="width:120px" >
			  #if($!configField.size()>0)
				 #foreach($field in $!configField)
				 	#if($field.width!='0')
						<option value='$field.asFieldName'>$field.display</option>
					#end
				 #end
			 #else
				#foreach($field in $!disFields)		
					#if($globals.getScopeRight($globals.getFieldBean($field.fieldName).tableBean.tableName,$globals.getFieldBean($field.fieldName).fieldName,$scopeRight))	
				 	#if($field.width!='0')
						<option value='$field.asFieldName'>$field.display</option>
					#end
					#end
			    #end
			#end
		   </select>
         </fieldset>
        </td>
        <td>
           <button type="button" style="width:30px; text-align:center" onClick="JavaScript:upItem('FieldsToShowList')">&uarr; </button><br/><br/>
          <button type="button" style="width:30px; text-align:center" onClick="JavaScript:downItem('FieldsToShowList')">&darr;</button>
        </td>
      </tr>
      <tr>
        <td colspan="5" align="center" style="padding:10px 0 6px">
          <button type="button" class="ActionButton" onClick="submitForm();" style="width: 50px;">$text.get("mrp.lb.save")</button>
          <button type="button" class="ActionButton" style="width: 50px;" onClick="defaultForm('defaultConfig')"/>
          				$text.get("com.define.defaultField")</button><br/>
        </td>
      </tr>
      <tr>
      	<td colspan="5" align="left">&nbsp;</td>
      </tr>
    </table>
    </div>
       <div id="colWidthSetting" style="display: none;">
  	 <table width="100%" align="center" border=0 id="CustomSetTable2" cellpadding="0" cellspacing="0"  class="BgTable">
     <tr style=" height:25px; background:#92D8FF url(/$globals.getStylePath()/images/aiobg.gif) repeat-x left -170px; border-bottom: 1px solid #F0FCFF">
        <td colspan="5" width="300"  align="left" >&nbsp;$text.get("com.config.colwidth")</td>
        <td align="right" >&nbsp;
          
        </td>
      </tr>
            <tr>
      	<td colspan="6" align="left" >&nbsp;</td>
      </tr>
			<td colspan="6">
				#set($tdIndex=2)
				<div style="float: left;width: 430px;margin-right: 20px;margin-bottom: 7px;">
				#if($!configField.size()>0)
					 #foreach($field in $!configField)
					 	#if($field.width!='0')
							<li style="float: left;width: 210px;height: 22px;  line-height: 22px;">
									<span style="float: left;text-align:right;width: 120px;">$field.display：</span>
									<input type="text" id="tdWidth$tdIndex" name="$!tableName:$field.asFieldName" onKeyUp="keyIsNumber(event.keyCode);"
												style="width: 80px; border:0px;height: 18px; border: 1px solid #006699;" value="$field.width"/>
							</li>
							#set($tdIndex=$tdIndex+1)
						#end
					 #end
			 	#else
					#foreach($field in $!disFields)
					 	#if($field.width!='0')
						 	<li style="float: left;width: 210px;height: 22px;  line-height: 22px;">
									<span style="float: left;text-align:right;width: 120px;">$field.display ：</span>
									<input type="text" id="tdWidth$tdIndex" name="$!tableName:$field.asFieldName" onKeyDown="keyIsNumber();"
												style="width: 80px; border:0px;height: 18px; border: 1px solid #006699;" value="$field.width"/>
							</li>
							#set($tdIndex=$tdIndex+1)
						#end
					#end
				#end
				  </div>
			</td>
	  <tr>
	  	<td colspan="6" align="center">
	  		 <button type="button"  onclick="beforeUpdate();" style="width: 50px;">$text.get("common.lb.ok")</button>
	  		 <button type="button" onClick="returnColConfig();">$text.get("com.return.config")</button>
          	 <button type="button" style="width: 50px;" onClick="defaultForm('defaultWidth');">$text.get("com.define.defaultField")</button>
	  	</td>
	  </tr>
      <tr>
      	<td colspan="6">&nbsp;</td>
      </tr>
    </table>
  </div>
</div>
  </div>
</div>
 <script type="text/javascript">
	function beforeUpdate(){
		var colSelect = "colWidth@@" ;
		for(var i=2;i<$tdIndex;i++){
			var obj = document.getElementById("tdWidth"+i) ;
			var fieldName = obj.name ;
			var colWidth = obj.value ;
			if(colWidth<=0){
				alert("$text.get('com.config.width.zero')") ;
				return ;
			}
			colSelect += fieldName+":"+colWidth+";" ;
		}
		window.parent.returnValue = colSelect;
		window.close() ;
	}
</script>
</body>
</html>
