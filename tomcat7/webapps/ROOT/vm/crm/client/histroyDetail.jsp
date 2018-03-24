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
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/crm_operation.js"></script>
<style type="text/css">
.inputbox li{width:315px;padding-top:2px;}
.inputbox li ul{float:left;background-color:#F5F5F5;width:200px;height:18px;padding-left:3px;}
</style>

<script type="text/javascript">
	$(function() {
  		$(".clientDiv font").attr("class","notNull");
  		$("#clientTitle").html($('#head1').val());
  		$("#contactTitle").html($('#head2').val());
  		$("input").css("backgroundColor","gainsboro");
  		$("input").css("border","none");
  		$("input").attr("readonly","true");
  		$("select").attr("disabled","true");
  		
	});
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form name="form" action="/CRMClientAction.do" method="post" target="formFrame" onsubmit="return beforeSubmit();">
<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')"/>
<input type="hidden" name="clientDetCou" id="clientDetCou" value="2"/>
<input type="hidden" name="District" id="District" value="$!District"/>
<input type="hidden" name="Trade" id="Trade" value="$!Trade"/>
<input type="hidden" name="SourceInfo" id="SourceInfo" value="00004"/>
<input type="hidden" name="clientCols" id="clientCols" value="$!clientRecords"/>
<input type="hidden" name="contactCols" id="contactCols" value="$!contactRecords" />
<input type="hidden" name="isAttachment" id="isAttachment" value="$!attachment" />
<input type="hidden" name="tableName" id="tableName" value="$!tableName" />
<input type="hidden" name="contactTableName" id="contactTableName" value="$!contactTableName" />
<input type="hidden" name="clientId" value="$!keyId"/>
<input type="hidden" id="nowHead" value=""/>
<input type="hidden" id="linkMan" value="$!linkMan"/>

<div class="boxbg2 subbox_w700" style="margin: 0 auto;margin-top:5px;" >
<div class="subbox cf" style="border: 1px solid #dedede;">
  <div class="operate operate2" >
  <ul>
  #if("$!linkMan" != "true")<li id="clientHead" class="sel" ><a href="#" onclick="showInfo('client')" ><span id="clientTitle"></span></a></li>#end
  <li id="contactHead"><a href="#" onclick="showInfo('contact')" >联系人信息</a></li>
   #if("$!linkMan" != "true")<li id="affixHead" ><a href="#"  style="#if("$!isAttachment" != "true")display: none; #end" onclick="showInfo('addfix')" >附件管理</a></li>#end
  <li style="float: right;margin-top: -3px;">
  </li>
  </ul>
  </div>
  
  <div class="bd" >
      <div class="inputbox">
      	#set($firstEnter = "true")
      	#foreach ($maps in $clientMap.keySet())	
	      	#if($clientMap.get($maps).size() != 0)	
	        <div class="clientDiv">
	        	#if("$firstEnter" == "true")<input type="hidden" id="head1" value="$maps"/>#set($firstEnter = "false")#else<h4> $maps </h4>  #end
		        <ul>
		        #foreach ($row in $clientMap.get($maps))
		        	#if("$row.inputType" == "0" || "$row.inputType" == "6")
		        		 #if("$row.fieldType" == "5")
		        		 		<li><span>$row.display.get("$globals.getLocale()")：</span><ul id="$row.fieldName">$!result.get($row.fieldName)</ul></li>
		        		 #elseif("$row.fieldName" == "Address" || "$row.fieldName" == "ClientRemark" || "$row.fieldName" == "ClientName")
		        		 	<li style="width: 700px;"><span>$row.display.get("$globals.getLocale()")：</span><ul style="width: 515px;" id="$row.fieldName">$!result.get($row.fieldName)</ul></li>
		        		 #elseif("$row.fieldName" == "BriefContent")
		        		 	<li style="width: 700px;margin-bottom: 3px;"><span>$row.display.get("$globals.getLocale()")：</span><div style="height: 101px;width:520px; float: left;overflow: auto;background-color:#F5F5F5;">$globals.replaceHTML($!result.get($row.fieldName))</div></li>
		        		 #else
		        		 	#if("$row.fieldName" == "Attachment")
		        		 	
		        		 	#elseif("$row.fieldName" == "Trade")
		        		 		<li><span>$row.display.get("$globals.getLocale()")：</span><ul id="$row.fieldName">$!tradeName</ul></li>
		        		 	#elseif("$row.fieldName" == "District")
		        		 		<li><span>$row.display.get("$globals.getLocale()")：</span><ul id="$row.fieldName">$!DistrictName</ul></li>
		        		 	#else
		        		 		<li><span>$row.display.get("$globals.getLocale()")：</span><ul id="$row.fieldName">$!result.get($row.fieldName)</ul></li>
		        		 	#end
		        		 #end
		        	#elseif("$row.inputType" == "1")
		        		 <li>
		        		 	 <span>$row.display.get("$globals.getLocale()")：</span>
				    	 	 <ul id="$row.fieldName">$globals.getEnumerationItemsDisplay("$row.refEnumerationName","$result.get($row.fieldName)")</ul>
				         </li>	
				     #elseif("$row.inputType" == "5")
				    	 <li style="width: 670px;">
				    	 	 <span>$row.display.get("$globals.getLocale()")：</span>
				    	 	 	 <ul id="$row.fieldName" style="width: 515px;">
			        		 	 #foreach($item in $!result.get($row.fieldName).split(","))
			        		 		$globals.getEnumerationItemsDisplay("$row.refEnumerationName","$item"),
			        		 	 #end
			        		 	 </ul>
				         </li>	     
			        #elseif("$row.inputType" == "14")
			     		 <li style="width: 700px;"><span>$row.display.get("$globals.getLocale()")：</span><ul style="width: 515px;" id="$row.fieldName" title="#foreach($row in $!result.get($row.fieldName).split(","))#if("$row" != "")$deptMap.get("$row"),#end#end">#foreach($row in $!result.get($row.fieldName).split(","))#if("$row" != "")$deptMap.get("$row"),#end#end</ul></li>
			        #elseif("$row.inputType" == "15")
			     	    <li style="width: 700px;"><span>$row.display.get("$globals.getLocale()")：</span><ul style="width: 515px;" id="$row.fieldName" title="#foreach($row in $!result.get($row.fieldName).split(","))#if("$row" != "")$globals.getEmpFullNameByUserId($row),#end#end">#foreach($row in $!result.get($row.fieldName).split(","))#if("$row" != "")$globals.getEmpFullNameByUserId($row),#end#end</ul></li>
				    #else
				    	<li>
							<span>$row.display.get("$globals.getLocale()")：</span><ul id="$row.fieldName">$!result.get($row.fieldName)</ul>   	
		        		</li>	
		        	#end
				#end
		        </ul>
	        </div>
		    #end
        #end

        <div class="contactDiv" style="display: none; ">
        	#if("$!contactCou" != "0")
		       	#foreach ( $det in $!result.get("TABLENAME_${conTableName}") ) 
		       		#foreach ($maps in $contactMap.keySet())	
			       		#if($contactMap.get($maps).size() !=0)
			       		<div id="contacts">
				        <h4 style="height: 13px;"></h4>
				        <ul>
				        #foreach ($row in $contactMap.get($maps))
				        	#if("$row.inputType" == "1")
				        		<li><span>$row.display.get("$globals.getLocale()")：</span><ul>$globals.getEnumerationItemsDisplay("$row.refEnumerationName","$det.get($row.fieldName)")</ul></li>
				        	#else
				        		<li><span>$row.display.get("$globals.getLocale()")：</span><ul>$!det.get($row.fieldName)</ul></li>	
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
   		
   		 <div id="affixDiv" style="display: none;">
	       	<div class="listRange_1_photoAndDoc_1"><span></span>
				<ul id="affixuploadul">
					#if($AFFIX.size() != 0 || $broAttachments.keySet().size()!=0)
						#foreach($uprow in $AFFIX)
							#if("$uprow" != "")	
							<li style='background:url();' id="uploadfile_$uprow">
							<input type=hidden id="uploadaffix" name=uploadaffix value='$uprow'/><div class="showAffix">$uprow</div>&nbsp;&nbsp;&nbsp;			
								<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=$tableName" target="_blank">$text.get("common.lb.download")</a>
							</li>
							#end
						#end
						
						#foreach ($maps in $broAttachments.keySet())	
							#foreach ($uprow in $broAttachments.get($maps))	
							#if("$uprow" != "")
							<li style='background:url();' id="uploadfile_$uprow">
							<input type=hidden id="uploadaffix" name=uploadaffix value='$uprow'/><div class="showAffix">$uprow</div>&nbsp;&nbsp;&nbsp;			
								<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=$maps" target="_blank">$text.get("common.lb.download")</a>
							</li>
							#end
							#end
						#end
						
						
					#else
						<span style="color: gray;float: left;margin-left: 300px;margin-top: 20px;">暂无附件</span>
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
