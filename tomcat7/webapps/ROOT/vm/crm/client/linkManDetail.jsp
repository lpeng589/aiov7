<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/client_sub.css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
.inputbox li{width:380px;padding-top:2px;height: 22px;}
.inputbox li font{float:left;background-color:#F5F5F5;width:190px;height:18px;padding-left:3px;}
select {height: 22px;}
</style>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body> 
<form name="form" action="/CRMClientAction.do" method="post" target="formFrame" onsubmit="return beforeSubmit();">
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')"/>
<input type="hidden" name="clientDetCou" id="clientDetCou" value="2"/>
<input type="hidden" name="clientCols" id="clientCols" value="$!clientRecords"/>
<input type="hidden" name="contactCols" id="contactCols" value="$!contactRecords" />
<input type="hidden" name="isAttachment" id="isAttachment" value="$!attachment" />
<input type="hidden" name="tableName" id="tableName" value="$!tableName" />
<input type="hidden" name="contactTableName" id="contactTableName" value="$!contactTableName" />
<input type="hidden" name="clientId" id="clientId" value="$!keyId"/>
<input type="hidden" id="nowHead" value=""/>
<input type="hidden" id="linkMan" value="$!linkMan"/>
<input type="hidden" id="detailPage" value="true"/>
<div class="boxbg2 subbox_w700" style="margin: 0 auto;margin-top:5px;width: 810px;" >
<div class="subbox cf" style="border: 1px solid #dedede;">
  
  <div class="bd" style="overflow-y: auto;" id="contentDiv" >
      <div class="inputbox">
      	
        <div class="contactDiv" >
        	#if($!result.get("TABLENAME_${conTableName}").size() != 0)
			       	#foreach ( $det in $!result.get("TABLENAME_${conTableName}") ) 
				       		#foreach ($maps in $contactMap.keySet())	
					       		#if($contactMap.get($maps).size() !=0)
					       		#if($LoginBean.operationMap.get("/CRMClientAction.do").update())
							  		#set($editFlag = "true" )
							    #else
							   	    #set($editFlag  = "false" )
							 	#end
					       		<div id="contacts">
						        <h4 style="height: 13px;"><span style="float: left;">主联系人:#if($!det.get("mainUser") == "1") 是 #else 否 #end </span> </h4>
						        <ul>
						        #foreach ($row in $contactMap.get($maps))
						        	#set($readExist = $globals.isExist("$!readStr",",contact${row.fieldName},"))
						        	#if("$row.fieldName" != "mainUser")
							        	#if("$row.inputType" == "1")
							        		 <div style="display: none;">
					 		        		 	<select  name="${row.fieldName}_enum"  id="${row.fieldName}_enum"  enumerName="$row.refEnumerationName" style="float: left;">
													 <option value=" : "></option>
									             	 #foreach($item in $globals.getEnumerationItems("$row.refEnumerationName"))	 
									        	        <option value="$item.value:$item.name" #if("$!result.get($row.fieldName)" == "$item.value") selected="selected" #end>$item.name</option>
											         #end
					     			             </select>	
							        		 </div>
							        		<li><span>$row.display.get("$globals.getLocale()")：</span><font fieldName="$row.fieldName" inputType="$row.inputType" fieldType="$row.fieldType" fieldVal="$det.get($row.fieldName)" >$globals.getEnumerationItemsDisplay("$row.refEnumerationName","$det.get($row.fieldName)")</font></li>
							        	#else
							        		<li><span>$row.display.get("$globals.getLocale()")：</span><font fieldName="$row.fieldName" inputType="$row.inputType" fieldType="$row.fieldType" fieldVal="$det.get($row.fieldName)" >$!det.get($row.fieldName)</font></li>	
							        	#end
						        	#end
								#end
						        </ul>
						        </div>
						    	#end
					    	#end
					    #end  
        	#else
        		<span style="color: gray;float: left;margin-left: 300px;margin-top: 20px;">暂无联系人</span>
        	#end
	   		<p></p>
   		</div>
   		 
      </div>
     
  </div>
</div>
</div>
</form>

</body>
</html>
