 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html> 
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$mainTable.display.get("$globals.getLocale()")</title>
<link type="text/css" href="/$globals.getStylePath()/css/classFunction.css" rel="stylesheet" />
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet" />
<link type="text/css" href="/style/css/jquery.contextmenu.css" rel="stylesheet"/>
<link type="text/css" href="/js/dialog/skins/default.css" rel="stylesheet" />
<link type="text/css" href="/style/css/base_button.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/setTime.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-min.js"></script> 
<script type="text/javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/formtab.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/Map.js"></script>


<script type="text/javascript"> 

##先于define.vjs必须加载的数据 
##生成明细下拉列表的inputValue,addCol时，引用，已避免数据发生变化后要重启服务器  
#foreach ($rowlist in $childTableList ) 
	#foreach ($row in $rowlist.fieldInfos )
		#if("$row.inputType" == "16" || ("$row.inputType" == "6" && "$row.inputTypeOld" == "16"))
			#set($inputValue = $globals.getDefineJS().initChildDownSelectNoCond($rowlist.tableName,$row.fieldName))
			var inputType_$row.fieldName = '$inputValue';
		#end
	#end
#end
</script>
<script type="text/javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/scrollgrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/k_listgrid.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/dropdownselect.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/function.js","",$text)"></script>
<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
#if($globals.isLowerIE8())
<script type="text/javascript" src="$globals.js("/js/editGrid.js","",$text)"></script>
#else
<script type="text/javascript" src="$globals.js("/js/editGrid2.js","",$text)"></script>
#end
<script type="text/javascript" src="$globals.js("/js/define.vjs",$mainTable.tableName,$text)"></script>



<script language="javascript">

var tableListRowHeight = 25; //明细行高，有图片时高度可由系统配置调整  
var tableListImgRowHeight = 25;//有图片明细的行高 
var tableListViewImg = true; //明细是否显示图片缩略图，还是文字 
#set($picSize = $globals.getSysSetting("picSize"));
#if("$!picSize" == "0")
	tableListViewImg = false;
#elseif("$!picSize" != "")
	if($!picSize > 25){
		tableListImgRowHeight = $picSize;	
	}
#end

var MOID='$!MOID';
var ids=-1;
var fieldS;
var returnValue;
var existFlow;
var flowNodeid ="$!flowNode.id"; 
var flowNodedisplay ="$!flowNode.display";
var detailFlow = "$!detailFlow";
existFlow = "$!existFlow";
var operation = $globals.getOP("OP_ADD");
var winCurIndex = "$!winCurIndex";
var printRight = $globals.getMOperationMap($request).print() ;
var noback = "$!noback" ;
var fromCRM = "$!fromCRM" ;
var moduleType = "$!moduleType";
var detail = "$!detail"
var aioSessionId = "$session.id";


#foreach($sehash in $!sessionSet.keySet())
	var Sess_$sehash = '$!globals.replaceSpecLitter($!sessionSet.get("$sehash"))';
#end


var notOpenAccount = false;//未开帐不能做单 
#if($!LoginBean.sessMap.get('AccPeriod').accMonth < 0)		
	#if($!mainTable.SysParameter =="UnUseBeforeStart" ||
         $!mainTable.SysParameter == "CurrentAccBefBillAndUnUseBeforeStart")
        notOpenAccount = true;
    #end 
#end

##跟据权限只读部分字段 
#foreach ($rowlist in $globals.getScopeRightChildReadOnly($tableName,$scopeRight) ) 
#set($griddata =$globals.get($rowlist,0)+"Data")
#set($fname = $globals.get($rowlist,0)+"_"+$globals.get($rowlist,1))
	for(var i=0;i<${griddata}.cols.length;i++){
		if(${griddata}.cols[i].name=="$fname" && ${griddata}.cols[i].inputType !=-2){ 
			${griddata}.cols[i].inputTypeOld=${griddata}.cols[i].inputType;
			${griddata}.cols[i].inputType=8;
		}
	}
#end


/************页面根据权限设置隐藏字段(2011-10-09确定只有采购入库单界面不根据权限设置)。2012-03-15根据系统配置来控制**********************/

$!globals.changeDefineInputType($!tableName,$!values.get("DEFINE_INPUTTYPE"))

#if($!tableName!="tblBuyInStock"||($!tableName=="tblBuyInStock"&&$globals.getSysSetting("hiddenPricehidBuyIn")=="true"))
#foreach ($rowlist in $childTableList )  
#set($allChildName="$allChildName"+"$rowlist.tableName"+",")
#set($griddata =$rowlist.tableName+"Data")
#if($allConfigList.size()>0)
#foreach ($colConfig in $childTableConfigList.get($rowlist.tableName))
#set ($row="")#set ($isExist="false")
#foreach ($config_row in $rowlist.fieldInfos )#if($colConfig.colName==$config_row.fieldName)#set ($row=$config_row)#set ($isExist="true")#end#end
#if($isExist!="false") #set($fname = $rowlist.tableName+"_"+$row.fieldName)
#if(!$globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight))
#if("$row.inputType" == "2")	
	updateColType(${griddata},"$fname",-2,'');
#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
#foreach($srow in $row.getSelectBean().viewFields)
#set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))	
	updateColType(${griddata},"$disField",-2,"");
#end				   
#end
#else
	updateColType(${griddata},"$fname",-2,"");
#end
#end



#if($isExist=="false")
	#foreach ($row in $rowlist.fieldInfos )
		#if("$row.inputType" == "2" || ("$row.inputType"=="6" && "$row.inputTypeOld"=="2"))
			 #if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
			  	 #foreach($srow in $row.getSelectBean().viewFields)
			  	    #if("$srow.asName"=="$colConfig.colName")		  	  	  	  
			  	    	#if(!$globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight))
			  	    	#set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))
			  	    		updateColType(${griddata},"$disField",-2,"");
						#end
			  	    #end
			  	 #end
			#end
		#end
	#end
#end


#end
#end
#else
	#foreach ($row in $rowlist.fieldInfos )
	#set($fname = $rowlist.tableName+"_"+$row.fieldName)
	#set($flag=true)
	#set($readOnley = false)
	#set($moduleSetDis = "")
	#if($!moduleTable&&$!moduleTable.get($rowlist.tableName))
		#set($flag=false)
		#foreach($mrow in $!moduleTable.get($rowlist.tableName))
			#if($globals.get($mrow,0)==$row.fieldName)
				#set($flag=true) 
				#if($globals.get($mrow,1)=="true") #set($readOnley = true) #end
				#set($moduleSetDis = $globals.get($mrow,2))
				
			#end	
		#end
	#end
	#if(!$globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight)||!$flag)
		#if("$row.inputType" != "100" && $row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother"&&$row.inputType!=3)			  			
			#if("$row.inputType" == "2" || ("$row.inputType" == "6" && "$row.inputTypeOld"=="2"))			  			
				updateColType(${griddata},"$fname",-2,"$!moduleSetDis");
				#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
					#foreach($srow in $row.getSelectBean().viewFields)
						#set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))	
						updateColType(${griddata},"$disField",-2,"$!moduleSetDis");						
					#end				   
				#end
			#else
				updateColType(${griddata},"$fname",-2,"$!moduleSetDis");
			#end
		#end
	#elseif($readOnley)	
		#if("$row.inputType" != "100" && $row.fieldName != "id" && $row.fieldName != "f_ref" && $row.fieldName != "f_brother"&&$row.inputType!=3)			  			
			#if("$row.inputType" == "2" || ("$row.inputType" == "6" && "$row.inputTypeOld"=="2"))			  			
				#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
					#foreach($srow in $row.getSelectBean().viewFields)
						#set($disField = $rowlist.tableName+"_"+$globals.getTableField($srow.asName))	
						updateColType(${griddata},"$disField",8,"$!moduleSetDis");						
					#end				   
				#end
			#else
				updateColType(${griddata},"$fname",8,"$!moduleSetDis");
			#end
		#end
	#elseif("$!moduleSetDis" != "")	
		updateColTypeDisplay(${griddata},"$fname","$!moduleSetDis");	
	#end
#end
#end
#end
#end

//本段代码用于根据工作流结点的字段设置修改明细表字段类型  
#set($changeType="")
#if("$!designId"=="")
#set($changeType=$globals.changeDetInputType("$!tableName","0","$!MOID"))
#else
#set($changeType=$globals.changeDetInputType("$!tableName","0","$!designId"))
#end
$!changeType

#if("$!changeType"!="" && $LoginBean.id!=$values.get("createBy") && "$!fromFoward"!="quote")
var readOnly = true ;
#end


//这里用于URL明细表的传值。原来的getUrlBillDef对于明细表不再适用，因为，明细JS被静态化。这里为了区分URL值前加DF 例子MailCRM.jsp
$globals.changeDefaultValue()
$globals.changeDetInputType("$!tableName","0","$!MOID")
#foreach ($row in $fieldInfos )
#if("$row.inputType" != "100" && $row.fieldName != "id"  && $row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime")
#if("$row.inputType" == "0"||"$row.inputType" == "7" || "$row.inputType" == "4" || "$row.inputType" == "3" || "$row.inputType" == "8")		
#if("$row.inputType" == "4"  || "$row.fieldType"=="16" )
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),false);
#elseif("$row.fieldType" == "13")
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),false);
#elseif("$row.fieldType" == "14")
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),false);				
#else
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),true);
#end
#elseif("$row.inputType" == "2"||"$row.inputType" == "1"||"$row.inputType" == "16")		
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),true);	
#elseif("$row.inputType" == "5" || "$row.inputType" == "10")		
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"checkBox","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),true);	
#end		
#end	
#end
//加载HTML编辑器  
#foreach($row in $fieldInfos)
#if($row.fieldType==16 && $row.inputType!=3 && $row.inputType!=6)
var $row.fieldName  ;
KindEditor.ready(function(K) {
	$row.fieldName = K.create('textarea[name="$row.fieldName"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
		#if($row.inputType==8)
		,readonlyMode : true
		#end
	});
});
#end
#end
function deliverTo(url,retValUrl){
	$("#retValUrl").val(retValUrl);
	asyncbox.open({
		id : 'dealdiv',
　　　   	url : url,
	 	title : '转交',
　　 　 	width : 650,
　　 　	height : 370,
        btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    if(action == 'ok'){
			if(opener.beforSubmit(opener.form)){
				opener.form.submit();
				//window.location.href =  retValUrl;
			}
			return false;
		}else if(action == 'close'){
			window.location.href = retValUrl ;
		}else if(action == 'cancel'){
			window.location.href = retValUrl ;
		}
　　  　}
　 });
}

function dealAsyncbox(){
	var retValUrl = $("#retValUrl").val();
	window.location.href = retValUrl;
}
//table中第一行td的默认高
function tbodyD(){
	$(".listRange_list_function_b tr td").each(function(){
		$(this).css("height",$(this).height());
	});
	var firstTag = $(".showGridTags .tags:eq(0)");
	firstTag.addClass("tagSel");
	$("#"+firstTag.attr("show")).show().siblings("div").hide();
}


function addHeight(){
	var pDiv = $(".scroll_function_small_ud");
	pDiv.height(pDiv.height()+100);
	$(".scroll_function_small_ud>div").each(function(){
		var ah = $(this).attr("ah");
		$("#"+ah+"_tableLayout").height($("#"+ah+"_tableLayout").height()+100);
		$("#"+ah+"_tableData").height($("#"+ah+"_tableData").height()+100);
		$("#"+ah+"_tableColumn").height($("#"+ah+"_tableColumn").height()+100);
	})
}


function initMainValues(){
	$("input[type=text]").attr("disableautocomplete","true").attr("autocomplete","off");
	
	#foreach ($row in $fieldInfos )
		#set($defValue="")
		#if($!values.get($row.fieldName).toString().length()>0||$row.getDefValue().indexOf("@Sess:")>=0)
			#set($defValue=$!values.get($row.fieldName))  
		#elseif($globals.getUrlBillDef($row.fieldName).length()>0)
			#set($defValue=$globals.getUrlBillDef($row.fieldName))
		#elseif($globals.getUrlBillDef($row.fieldName).length()==0)
			#if($!parentCode.length()>0&&$row.getFieldIdentityStr().equals("copySuper"))
				#set($defValue=$copySupValue.get($row.getFieldName()))
			#elseif($!row.getFieldIdentityStr()=="BillNo")
				#if($!row.getDefaultValue()=="")
					#set($str=$row.getFieldName())
					#set($str="_"+$str)
					#set($str=$!tableName+$str)
					#set($defValue=$globals.getBillNoCode($str))
				#else
					#set($defValue=$globals.getBillNoCode($!row.getDefaultValue()))
				#end
			#elseif($row.getFieldIdentityStr().equals("lastValueAdd"))
				#set($defValue=$lastValues.get($row.getFieldName()))
			#elseif($row.getFieldIdentityStr().equals("paramDefaultValue"))
				#set($defValue=$paramValues.get($row.getFieldName()))
			#else
				#set($defValue=$row.getDefValue())
			#end
		#end
	
		#if($row.fieldName == "f_brother")
			$("#$row.fieldName").val("$!f_brother");
		#elseif("$row.inputTypeOld" == "16" || "$row.inputType" == "16") ##动态选项
			$("#$row.fieldName").attr("vl","$!globals.rereplaceSpecLitter2("$!defValue")");
		#elseif(("$row.inputTypeOld" == "5" && "$row.inputType" != "3")  || "$row.inputType" == "5") ##复选框
			#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))				
				#set ($check="")  
				#foreach($fieldValue in $globals.strSplit("$!defValue",",")) 
					#if($erow.value == $fieldValue)
					$("#cbox_${row.fieldName}_$erow.value").attr("checked","checked");					
					#end
				#end
			#end
			
		#elseif(("$row.inputTypeOld" == "10" && "$row.inputType" != "3") || "$row.inputType" == "10") ##单选框
			#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))	
				#set ($check="")
				#if($erow.value == $!defValue)
				$("#cbox_${row.fieldName}_$erow.value").attr("checked","checked");		
				#end
			#end	
		#elseif(("$row.inputType" == "2"||"$row.inputTypeOld" == "2")  && "$row.inputType" != "100" && "$row.inputType" != "3" && "$row.inputType" != "6") ##弹出窗			#if($row.getSelectBean().relationKey.hidden)
				#set($pdef = "")
				#if($!parentCode.length()>0&&$row.getFieldIdentityStr().equals("copySuper"))
					#set($pdef = $!defValue)					
				#else
					#set($pdef = "$!values.get($row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName))")
				#end
				$("#$!row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)").val("$!globals.rereplaceSpecLitter2("$!pdef")"); 
			#elseif("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")			
			#else				
				#set($pdef = "$!values.get($row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName))")
				#if("$row.fieldType" == "1")
					$("#$!row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)").val("$!globals.newFormatNumber($!pdef,false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)");
				#else
					$("#$!row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)").val("$!globals.rereplaceSpecLitter2("$!pdef")");
				#end
				
			#end 
			#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
				#foreach($srow in $row.getSelectBean().viewFields)	
					#if("$srow.hiddenInput"=="true")
						$("#$globals.getTableField($srow.asName)").val("$!globals.rereplaceSpecLitter2("$!values.get($srow.asName)")");
						$("#tbl_$globals.getTableField($srow.asName)").val("$!globals.rereplaceSpecLitter2("$!values.get($srow.asName)")");
					#else
						#if($globals.getFieldBean($srow.display).fieldType==1||$globals.getFieldBean($srow.fieldName).fieldType==1)
							#if($!isDetail)
								$("#$globals.getTableField($srow.asName)").val("$!globals.formatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)");
							#else
								$("#$globals.getTableField($srow.asName)").val("$!globals.newFormatNumber($!values.get($srow.asName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)");
							#end			
						#else
							$("#$globals.getTableField($srow.asName)").val("$!globals.rereplaceSpecLitter2("$!values.get($srow.asName)")"); 
						#end
					#end  ##hiddenInput不为true
				#end	##弹出窗显示字段循环			
			#end	##非序列号
		#elseif("$row.fieldType" == "1" && "$row.fieldName" != "id"  && "$row.fieldName" != "moduleType")
			$("#$row.fieldName").val("$!globals.newFormatNumber($!defValue,false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)");
		#elseif("$row.fieldName" != "id"  && "$row.fieldName" != "moduleType")
			$("#$row.fieldName").val("$!globals.rereplaceSpecLitter2("$!defValue")"); 
		#end
		
		
		#if("$row.inputType" == "8"&&"$row.inputTypeOld" == "1") ##只读的选择框


			#set($enumName = "")
			#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
				#if($erow.value == $!defValue)
					#set($enumName = $erow.name)
				#end
			#end
			#set($en = ${row.fieldName} +"Name" )
			$("#$en").val("$!globals.rereplaceSpecLitter2("$enumName")");
		#end
	#end

}

jQuery(document).ready(function(){
	initMainValues();
	checkHasFrame();  
	#foreach ($rowlist in $childTableList ) 
		#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$rowlist.isUsed"=="1" && "$rowlist.isView"=="0" && ($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0)) 
			#set($gridtable =$rowlist.tableName+"Table")  
			#set($griddata =$rowlist.tableName+"Data") 
			if(typeof(${gridtable}DIV) != "undefined"){
			showtable('$gridtable');  
			initTableList(${gridtable}DIV,${gridtable},$griddata,$rowlist.defRowCount);
			drag22('$gridtable');
			}
		#end 
	#end 
	initCalculate();
	initMainCaculate();
	
	#if($globals.isExistSeq())
		initSeqQtySet();
	#end 
	initDownSelect(); 
	showStatus(); 
	bpWidth();
	tbodyD();
	jQuery(':text:eq(0)').focus();
	initTxtTitle();
	
	changeConfirm();
});

function beforeSaveDraft2() {
 	$("#deliverTo").val(false);
	saveDraft2();
}

</script>
</head>

<body onKeyDown="down();">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post"  onKeyDown="down()" scope="request" name="form"  action="/UserFunctionAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" id="conver" name="conver" />
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_ADD")" />
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()" />
<input type="hidden" id="tableName" name="tableName" value="$!tableName" />
<input type="hidden" id="parentCode" name="parentCode" value="$!parentCode" />
<input type="hidden" id="button" name="button" />
<input type="hidden" id="noback" name="noback" value="$!noback"/>
<input type="hidden" id="fresh" name="fresh" value="$!fresh"/>
<input type="hidden" id="detLineNo" name="detLineNo" value=""/>
<input type="hidden" id="logicValidate" name="logicValidate" value=""/>
<input type="hidden" id="defineInfo" name="defineInfo" value=""/>
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName"/>
<input type="hidden" id="winCurIndex" name="winCurIndex" value="$!winCurIndex"/>
<input type="hidden" id="popWinName" name="popWinName" value="$!popWinName"/>
<input type="hidden" id="defineName" name="defineName" value=""/>
<input type="hidden" id="checkFieldName" name="checkFieldName" value=""/>
<input type="hidden" id="isTab" name="isTab" value="$!isTab"/>
<input type="hidden" id="deliverTo" name="deliverTo" value=""/>
<input type="hidden" id="retValUrl" value=""/>
<input type="hidden" id="hasFrame" name="hasFrame" value=""/>
<input type="hidden" id="ButtonType" name="ButtonType" value=""/>
<input type="hidden" id="ButtonTypeName" name="ButtonTypeName" value="" />
<input type="hidden" id="saveDraft" name="saveDraft" value="" />
#if("$!planType"!="")
<input type="hidden" id="planType" name="planType" value="$!planType"/>
<input type="hidden" id="strType" name="strType" value="$!strType"/>
<input type="hidden" id="strDate" name="strDate" value="$!strDate"/>
<input type="hidden" id="userId" name="userId" value="$!userId"/>
#end

#if($existSMS)
<input type="hidden" id="smsType" name="smsType" value=""/>
<input type="hidden" id="wakeUpMode" name="wakeUpMode" value=""/>
<input type="hidden" id="wakeUpContent" name="wakeUpContent" value=""/>
<input type="hidden" id="popedomDeptIds" name="popedomDeptIds" value="$!popedomDeptIds"/>
<input type="hidden" id="popedomUserIds" name="popedomUserIds" value="$!popedomUserIds"/>
<input type="hidden" id="popedomCRMCompany" name="popedomCRMCompany" value=""/>
<input type="hidden" id="popedomCompanyCodes" name="popedomCompanyCodes" value=""/>
<input type="hidden" id="otherEmail" name="otherEmail" value="$!otherEmail"/>
<input type="hidden" id="otherSMS" name="otherSMS" value="$!otherSMS"/>
#end
#if($!OAWorkFlow)
<input type="hidden" id="designId" name="designId" value="$!designId"/>
#end
<input type="hidden" id="OAWorkFlow" name="OAWorkFlow" value="#if($!OAWorkFlow)true#{else}false#end"/>

<div class="Heading" >
	<div class="HeadingTitle">
		<b class="icons"></b>
		#if("$!moduleName"=="")$!mainTable.display.get("$globals.getLocale()")#else $!{moduleName} #end -添加
		#if("$!brotherRMName" != "") ($!brotherRMName) #end
	</div>
	<ul class="HeadingButton f-head-u">
	
#if($globals.getMOperationMap($request).add() && "$!CannotOper" != "true") 
		#if(!$!OAWorkFlow) 
			<li><span name="save" defbtn="save"  class="btn btn-small"  onClick="if(beforSubmit(document.form)) {form.button.value='';#if($!OAWorkFlow)form.deliverTo.value='true';#end window.save=true; document.form.submit();}">$text.get("common.lb.save")</span></li>	
			#if("$!noback"=="false" && "$!fromCRM"!="service" && "$!planType"=="" && ("$mainTable.tableType" != "2" || "$mainTable.brotherType" != "0"))
			<li><span name="saveAdd" defbtn="saveAdd"  class="btn btn-small"  onClick="javascript:subAdd(); window.save=true;">$text.get("common.lb.saveAdd")</span></li>	
			#end	
		#else
			<li><span name="save" defbtn="save"  class="btn btn-small"  onClick="if(beforSubmit(document.form)) {form.button.value='';form.deliverTo.value='true'; window.save=true; document.form.submit();}">保存并送审</span></li>	
			<li><span name="savetemp" defbtn="savetemp" class="btn btn-small"  onClick="if(beforSubmit(document.form)) {form.button.value='';window.save=true; document.form.submit();}">保存不送审</span></li>	
		#end
		#if($mainTable.draftFlag==1)
		<li><span class="btn btn-small"  defbtn="backList" onClick="beforeSaveDraft2();">$text.get("common.lb.saveDraft")</span></li>		
		#end
#end


<li>
	<div class="btn btn-small h-child-btn">
		<a id="tb_opBtn" moreId="opMore">操作</a>
		<div id="opMore" class="d-more">
			<div class="out-css">
			#if($!print && $globals.getMOperationMap($request).print()&&$globals.getSysSetting("saveBeforePrint")=="true")
				<a href="javascript:printSave()">$text.get("common.lb.print")</a>
			#end	
			#foreach($brow in $customButton)
				$!brow
			#end		
			$!extendButton		
		
		
			#if($LoginBean.id=='1' && "$!workFlow"!="OA" && "$!moduleTable"=="" && $mainTable.isLayout != 1)
				<a id="CustomSetLink" href="javascript:showDivCustomSetTable();">$text.get("com.define.colconfig")</a>
			#end
		
			#if($existSMS)
			<!-- <a href="javascript:sendBillMsg('add');">$text.get("common.lb.sendMessage")</a>  -->
			#end
		
			#if($inIniAmount)
			<a id="inIniAmount" href='/UserFunctionAction.do?tableName=$tableName&parentCode=$!parentCode&operation=$globals.getOP("OP_ADD_PREPARE")&f_brother=$!f_brother&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex&onOff=onOff'>$!inIniAmountName</a>
			#end
			
			#if($!OAWorkFlow)
			<a href="javascript:flowDepict('$!designId','');">$text.get("common.lb.checkFlowChart")</a>
			#end
			</div>
		</div>
		<b class="triangle"></b>
	</div>			
</li>
	

	<li>
		<div class="btn btn-small h-child-btn">
			<a id="tb_toolBtn" moreId="toolMore">工具</a>
			<div id="toolMore" class="d-more">
				<div class="out-css">
				<a href="javascript:calc()">计算器</a>
		#set($hasGoodsBath = false)
		#foreach ($rowlist in $childTableList )  
		#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$!rowlist.isUsed"=="1" && "$!rowlist.tableName" != "tblGoodsUnit"  && "$!rowlist.isView"=="0"&& ($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0))
			#foreach ($row in $rowlist.fieldInfos ) 
			#if(!$hasGoodsBath && "$row.fieldName"=="GoodsCode"  && $row.inputType != 3)  #set($hasGoodsBath = true)
			<a href="javascript:batchInput()">商品批量录入</a>
			#end
			#end
		#end	
		#end
				$!toolBtn		
				</div>		
			</div>
			<b class="triangle"></b>
		</div>			
	</li>
	

#if("$!quoteBtn"!="" || $mainTable.draftFlag==1)
	<li>
		<div class="btn btn-small h-child-btn">
			<a id="tb_pushBtn">$text.get("common.lb.Reference")</a>
			<div class="d-more">
				<div class="out-css">
				$!quoteBtn
				#if($mainTable.draftFlag==1 && "$!workFlow"!="OA")
				<a href="javascript:quoteDraft('$mainTable.tableName','$!moduleType');" >$text.get("common.quote.draft")</a>
				#end	
				</div>		
			</div>
			<b class="triangle"></b>
		</div>			
	</li>
#end   
	#if("$!fresh"=="dialog")
	<li><span class="btn btn-small"  defbtn="backList" onClick="refreshcloseWindows();">$text.get("common.lb.back")</span></li>		
	#else
	<li><span class="btn btn-small"  defbtn="backList" onClick="closeWindows('$!popWinName');">$text.get("common.lb.back")</span></li>			
	#end
	
	</ul>	
</div>
	
#if($parentName.length()>0)			
	<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span></div>
#end

	<div id="listRange_id" style="position:relative;">
		<div class="wrapInside">
#if($mainTable.isLayout != 1)		
#parse("/vm/classFunction/functionLayout.jsp")
#else
$!globals.getlayoutHtml($tableName,$fieldInfos,$childTableList,$globals.getLocale(),$detail)
#end
		</div>
	</div>
</form>
#if($LoginBean.id =="1")
<form method="post" name="colConfigForm" action="/ColConfigAction.do?operation=1" target="_self">
<input type="hidden" id="tableName" name="tableName" value="$!tableName"/>
<input type="hidden" id="parentCode" name="parentCode" value="$!parentCode">
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName">
<input type="hidden" id="allTableName" name="allTableName" value="$!allTableName"/>
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType">
<input type="hidden" id="strAction" name="strAction" value="$globals.getModuleUrlByWinCurIndex("$!winCurIndex")&operation=$globals.getOP("OP_ADD_PREPARE")&f_brother=$!f_brother&noback=$!noback&parentCode=$!parentCode&winCurIndex=$!winCurIndex" />
<input type="hidden" id="colType" name="colType" value="bill" />
<input type="hidden" id="colSelect" name="colSelect" value="" />
 </form>
#end
	<script type="text/javascript">  
		var oDiv2=m("listRange_id"); 
		var sHeight2=document.documentElement.clientHeight-40;
		#if($parentName.length()>0) sHeight2 =sHeight2-25;  #end ##分级的还要去掉显示根目录的高度	
		oDiv2.style.height=sHeight2+"px";
		#if($mainTable.isLayout != 1) ##个性布局不要算这些高度
					
			var mainFieldH  = m("listRange_mainField").offsetHeight;	
			var remarkH = m("listRange_remark").offsetHeight;
			var tabH = sHeight2-mainFieldH-remarkH-45;
			if(tabH<150) tabH=150;
			if($("#listRange_tableInfo >div").size()==0){
				$("#listRange_tableInfo").height(0);
				$("#listRange_remark textarea").height(80);
			}else if($("#listRange_tableInfo table").size()==0 && $("div[attType=PIC]").size()>0){
				$("#listRange_tableInfo").css("min-height","200px"); //没有表格有图片  
				$("#listRange_tableInfo").height(200);	
				$("#listRange_remark textarea").height(80);
			}else if($("#listRange_tableInfo table").size()==0 && $("div[attType=ATT]").size()>0){
				$("#listRange_tableInfo").css("min-height","100px"); //没有表格没有图片有符件  
				$("#listRange_tableInfo").height(100);	
				$("#listRange_remark textarea").height(80);
			}else{
				$("#listRange_tableInfo").height(tabH);		
			} 
		#end
		$("#listRange_tableInfo > div").height($("#listRange_tableInfo").height());	
		
	</script>

<script>
	function flowDepict(applyType,keyId){
		window.open("/OAMyWorkFlow.do?keyId="+keyId+"&operation=$globals.getOP("OP_DETAIL")&tableName=$!tableName&applyType="+applyType,null,"height=570, width=1010,top=50,left=100 ");
	}
	//在文字后加空格，chrome下实现两端对齐    
	cyh.lableAlign(); 
</script>

</body>
</html>