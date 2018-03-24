<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>#if("$!moduleName"=="")$!mainTable.display.get("$globals.getLocale()")#else$!moduleName#end</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" rel="stylesheet" href="/js/ui/jquery-ui-1.8.18.custom.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css" /> 
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" href="/js/dialog/skins/default.css" rel="stylesheet" />
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
<script type="text/javascript" src="$globals.js("/js/Main.vjs","",$text)"></script>

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


<script type="text/javascript">

$(function(){
	setNextPos(); //设置上一条下一条信息
});
 
var __stat =0;
#if($mainTable.wakeUp=="1")
function alertSet(relationId){	
	var urls=encodeURIComponent('/UserFunctionAction.do?tableName=$!tableName&keyId='+relationId+'&operation=5&winCurIndex=16&pageNo=&parentCode=&parentTableName=$!parentTableName&saveDraft=');
	#if("$!moduleName"=="")
	var tableD='$!mainTable.display.get("$globals.getLocale()")';
	#else
	var tableD='$!moduleName';
	#end	
	var	title=encodeURIComponent(tableD);
	var	typestr=encodeURIComponent(tableD+'提醒');
	var date=new Date();
	var frame=window.parent.frames['moddiFrame'];
	if(typeof(frame)!="undefined"){
		window.parent.frames['moddiFrame'].setFrameHight('div_max');
	}
	
	asyncbox.open({
		id:'alertSetDiv',title:tableD+'提醒',width:552,height:420,
	　　 url : "/UtilServlet?operation=alertDetail&relationId="+relationId+"&falg=true"+"&title="+title+"&typestr="+typestr+"&urls="+urls+"&date="+date,		
		callback : function(action,opener){
	  		
  	  	}
　	});	 
	
	return;
	
	$("<div id='"+relationId+"' title='提醒设置' style='padding:0px;'><iframe  src='' width='100%' height='100%' frameborder='no' framespacing='0' scrolling='no' /></div>").dialog({
		resizable: false ,
		width:552,
		height:600,
		close: function(event, ui) {
			$("#"+relationId).remove() ;
			if(typeof(frame)!="undefined"){
				window.parent.frames['moddiFrame'].setFrameHight('div_ret');
			}
		}
	});
}
#end

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
var valueId = "$!values.get("id")";
var upIniFlag=1; 
  
var fromFoward = "$!fromFoward";
var moduleType = "$!moduleType";
var ids=-1;
var fieldS;
var returnValue;
var operation = $globals.getOP("OP_UPDATE");
var noback="$!noback";
var detail = "$!detail";

var flowNodeid ="$!flowNode.id"; 
var flowNodedisplay ="$!flowNode.display";
var detailFlow = "$!detailFlow";
var existFlow = "$!existFlow";
var winCurIndex = "$!winCurIndex";
var f_brother = "$!values.get("f_brother")";
var operation = $globals.getOP("OP_UPDATE");

var aioSessionId = "$session.id"; 

if("$!parentTableName"!="" 
			&& window.parent.document.getElementById("bottomFrame")){
	var displayType = window.parent.document.getElementById("displayType");
	if(displayType!=null && typeof(displayType)!="undefined"){
		displayType.value = "detail" ;
	}
}

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

var workFlowNode = "$!values.get('workFlowNode')";
var strWakeUp = "$!strWakeUp";
var printRight = $globals.getMOperationMap($request).print() ;
#if("$detail"=="detail")  window.save=true; #end



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

#if($!tableName!="tblBuyInStock"||($!tableName=="tblBuyInStock"&&($globals.getSysSetting("hiddenPricehidBuyIn")=="true" || ("$copy" != "copy" && "$quote" != "quote" && "$saveDraft" != "quoteDraft" &&"$saveDraft" != "draft" ))))
#foreach ($rowlist in $childTableList )  
#set($allChildName="$allChildName"+"$rowlist.tableName"+",")
#set($griddata =$rowlist.tableName+"Data")
#if($allConfigList.size()>0)
#foreach ($colConfig in $childTableConfigList.get($rowlist.tableName))
#set ($row="")#set ($isExist="false")
#foreach ($config_row in $rowlist.fieldInfos )#if($colConfig.colName==$config_row.fieldName)#set ($row=$config_row)#set ($isExist="true")#end#end
#if($isExist!="false") #set($fname = $rowlist.tableName+"_"+$row.fieldName)
#if(!$globals.getScopeRight($rowlist.tableName,$row.fieldName,$scopeRight))
#if("$row.inputType" == "2" || ("$row.inputType" == "6" && "$row.inputTypeOld"=="2"))	
#set($inputValue = "")
	updateColType(${griddata},"$fname",-2,"");
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
			  	    		updateColType(${griddata},"$disField",-2,'');
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

#set($lastNode="$!currNodeId")
#if($!isDetail)
	#set($lastNode="$!userLastNode")	
#end


//本段代码用于根据工作流结点的字段设置修改明细表字段类型    
#set($changeType="")
#if("$!designId"=="")
#set($changeType=$globals.changeDetInputType("$!tableName","$!lastNode","$!MOID"))
#else
#set($changeType=$globals.changeDetInputType("$!tableName","$!lastNode","$!designId"))
#end
$!changeType


#if("$!changeType"!="" && $LoginBean.id!=$values.get("createBy") && "$!fromFoward"!="quote")
var readOnly = true ;
#end

var childRowCount=0 ;
#foreach ($rowlist in $result ) 
	#set($griddata =$globals.get($rowlist,0)+"Data")
	#foreach ($row in $globals.get($rowlist,1) )
addRows($griddata,$globals.get($row,0).replaceAll('\r\n','\\r\\n'));
childRowCount=childRowCount+1;
	#end
#end

#foreach ($row in $fieldInfos )
#if("$row.inputType" != "100" && $row.fieldName != "id"  && $row.fieldName != "createBy" && $row.fieldName != "createTime" && $row.fieldName != "lastUpdateBy" && $row.fieldName != "lastUpdateTime")
#if("$row.inputType" == "0"||"$row.inputType" == "7" || "$row.inputType" == "4" || "$row.inputType" == "3" || "$row.inputType" == "8")		
#if("$row.inputType" == "4")
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"$row.getStringType()","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),false);
#elseif("$row.fieldType"=="16")
putValidateItem("$row.fieldName",'$!row.display.get("$globals.getLocale()")',"#if("$!Safari"=="true")any#else$row.getStringType()#end","$!row.fieldSysType",$row.getSringNull(),minValue,$row.getStringLength(),false);
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
#if("$!Safari"!="true")
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
#end

function deliverTo(url,retValUrl,noback){
	$("#noback").val(noback);
	$("#retValUrl").val(retValUrl);
	asyncbox.open({
		id:'dealdiv',url:url,title:'转交',width : 650,height:370,btnsbar:jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		    if(action == 'ok'){
				if(opener.beforSubmit(opener.form)){
					opener.form.submit();
				}
			}else if(action == 'close'){
				//if(noback == "true"){
				//	closeWin();
				//}else{
					window.location.href = retValUrl ;
				//}
			}else if(action == 'cancel'){
				//if(noback == "true"){
				//	closeWin();
				//}else{
					window.location.href = retValUrl ;
				//}
			}
			return false;
　　  　	}
　    });
}

function dealAsyncbox(){
	var retValUrl = $("#retValUrl").val();
	jQuery.close('dealdiv');
	if($!noback){
		closeWin();
	}else{
		window.save = true;
		if(retValUrl.length==0){
			window.location.reload();
		}else{
			window.location.href = retValUrl;
		}
	}
}
//详情页面table自适应宽度
function tableSuitWidth(){
	$(".listRange_list_function_b").each(function(){
		var tblWidth = 0;
		$(this).find(".listhead td").each(function(){
			var defaultWidth = $(this).attr("defaultWidth");
			var thisWidth = $(this).outerWidth(true);
			if(defaultWidth > thisWidth){
				$(this).css("width",defaultWidth);
				tblWidth+= parseInt(defaultWidth);
			}else{
				$(this).css("width",thisWidth);
				tblWidth+=parseInt(thisWidth);
			}
		}).end().css({"width":tblWidth,"table-layout":"fixed"});
	});
}

function initMainValues(){
	$("input[type=text]").attr("disableautocomplete","true").attr("autocomplete","off");

	#foreach ($row in $fieldInfos )
		#if($row.fieldName == "f_brother")
			#if("$!f_brother"=="")
			$("#$row.fieldName").val("$!values.get($row.fieldName)");
			#else
			$("#$row.fieldName").val("$!f_brother");
			#end
		#elseif("$row.inputType" == "4") ##多语言
			#set($lstr = $!values.get($row.fieldName))
			
			#if("$lstr" != "")
			   #set($lan= $!values.get("LANGUAGEQUERY").get($lstr).get("$globals.getLocale()"))
			   #set($lstr = $!values.get("LANGUAGEQUERY").get($lstr)) 
			#end	
			
			$("#$row.fieldName").val("$!globals.rereplaceSpecLitter2("$!lstr")");
			#set($cs = ${row.fieldName}+"_lan")
			$("#$cs").val("$!globals.rereplaceSpecLitter2("$!lan")");
		#elseif("$row.inputTypeOld" == "16" || "$row.inputType" == "16") ##动态选项
			$("#$row.fieldName").attr("vl","$!globals.rereplaceSpecLitter2("$!values.get($row.fieldName)")");
		#elseif(("$row.inputTypeOld" == "5" && "$row.inputType" != "3")  || "$row.inputType" == "5") ##复选框
			#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))				
				#set ($check="")  
				#foreach($fieldValue in $globals.strSplit("$!values.get($row.fieldName)",",")) 
					#if($erow.value == $fieldValue)
					$("#cbox_${row.fieldName}_$erow.value").attr("checked","checked");					
					#end
				#end
			#end
		#elseif(("$row.inputTypeOld" == "10" && "$row.inputType" != "3")  || "$row.inputType" == "10") ##单选框
			#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))	
				#set ($check="")
				#if($erow.value == $!values.get($row.fieldName))
				$("#cbox_${row.fieldName}_$erow.value").attr("checked","checked");		
				#end
			#end	
		#elseif(("$row.inputType" == "2"||"$row.inputTypeOld" == "2") && "$row.inputType" != "100" && "$row.inputType" != "3" && "$row.inputType" != "6") ##弹出窗


			#if("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")
				$("#$row.fieldName").val("$!globals.rereplaceSpecLitter2("$!values.get($row.fieldName)")");
				$("#${row.fieldName}_mh").val("$!globals.rereplaceSpecLitter2("$globals.getSeqDis($!values.get($row.fieldName))")");				
			#else
				#if("$row.fieldType" == "1")
					#if($!isDetail)
						$("#$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)").val("$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)");
					#else
						$("#$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)").val("$!globals.newFormatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)");
					#end
				#else
					$("#$!row.getSelectBean().getFieldName($!row.getSelectBean().relationKey.parentName)").val("$!globals.rereplaceSpecLitter2("$!values.get($row.fieldName)")");
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
							$("#$globals.getTableField($srow.asName)").val("$!globals.rereplaceSpecLitter2("$!values.get($srow.asName)")"); //a $srow.asName
						#end
					#end  ##hiddenInput不为true
				#end	##弹出窗显示字段循环			
			#end	##非序列号
		#elseif("$row.fieldType" == "13")  ##图片 
			#set($pfn = $!values.get("$row.fieldName").replaceAll("&#92;","/") )
			#foreach($uprow in $pfn.split(";")) 
				#if($uprow.indexOf("http:") > -1 )	
					$("#picuploadul_$!{row.fieldName}").append("<li style='background:url();' id='uploadfile_$uprow'><input type=hidden id='$row.fieldName' name=$row.fieldName value='$uprow'>"+
						"<div>#if("$detail" != "detail")<a href='javascript:deleteupload(\"$uprow\",\"false\",\"$tableName\",\"PIC\");'>$text.get("common.lb.del")</a>#end<em>$uprow</em></div><a href='$uprow' target='_blank'><img src='$uprow' height='120' border='0'></a></li>");	
				#elseif($uprow.indexOf("PICPATH:") > -1)	
					$("#picuploadul_$!{row.fieldName}").append("<li style='background:url();' id='uploadfile_$uprow'><input type=hidden id='$row.fieldName' name=$row.fieldName value='$uprow'>"+
						"<div>#if("$detail" != "detail")<a href='javascript:deleteupload(\"$uprow\",\"false\",\"$tableName\",\"PIC\");'>$text.get("common.lb.del")</a>#end<em>$uprow.substring(8)</em></div><a href='/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName' target='_blank'><img src='/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName' height='120' border='0'></a></li>");	
				#elseif($uprow.indexOf(":") > -1)	
					$("#picuploadul_$!{row.fieldName}").append("<li style='background:url();' id='uploadfile_$!globals.get($uprow.split(":"),0)'><input type=hidden id='$row.fieldName' name=$row.fieldName value='$uprow'>"+
						"<div>#if("$detail" != "detail")<a href='javascript:deleteupload(\"$!globals.get($uprow.split(":"),0)\",\"false\",\"$tableName\",\"PIC\");'>$text.get("common.lb.del")</a>#end<em>$!globals.get($uprow.split(":"),1)</em></div><a href='/ReadFile.jpg?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=PIC&tableName=$tableName' target='_blank'><img src='/ReadFile.jpg?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=PIC&tableName=$tableName' height='120' border='0'></a></li>");					
				
				#elseif($uprow != "")
					$("#picuploadul_$!{row.fieldName}").append("<li style='background:url();' id='uploadfile_$uprow'><input type=hidden id='$row.fieldName' name=$row.fieldName value='$uprow'>"+
						"<div>#if("$detail" != "detail")<a href='javascript:deleteupload(\"$uprow\",\"false\",\"$tableName\",\"PIC\");'>$text.get("common.lb.del")</a>#end<em>$uprow</em></div><a href='/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName' target='_blank'><img src='/ReadFile.jpg?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=PIC&tableName=$tableName' height='120' border='0'></a></li>");	
				#end
			#end 
		#elseif("$row.fieldType" == "14") ##附件	
			#set($pfn = $!values.get("$row.fieldName").replaceAll("&#92;","/") )
			#foreach($uprow in $pfn.split(";")) 
				#if($uprow.indexOf(":") > -1)	
					$("#affixuploadul_$!{row.fieldName}").append("<li style='background:url();' id='uploadfile_$!globals.get($uprow.split(":"),0)'>"+
						"<input type=hidden id='$row.fieldName' name=$row.fieldName value='$uprow'>$!globals.rereplaceSpecLitter2('$!globals.get($uprow.split(":"),1)')"+		
						#if("$detail" != "detail")
							"<a href='javascript:deleteupload(\"$!globals.get($uprow.split(":"),0)\",\"false\",\"$tableName\",\"AFFIX\");'>$text.get("common.lb.del")</a>"+
						#end
							"&nbsp;&nbsp;&nbsp;&nbsp;<a href='/ReadFile?fileName=$globals.urlEncode($!globals.get($uprow.split(":"),0))&realName=$globals.urlEncode($!globals.get($uprow.split(":"),1))&tempFile=false&type=AFFIX&tableName=$tableName' target='_blank'>$text.get("common.lb.download")</a></li>	");		
				#elseif($uprow != "")
					$("#affixuploadul_$!{row.fieldName}").append("<li style='background:url();' id='uploadfile_$uprow'>"+
						"<input type=hidden id='$row.fieldName' name=$row.fieldName value='$uprow'>$!globals.rereplaceSpecLitter2("$uprow")"+		
						#if("$detail" != "detail")
							"<a href='javascript:deleteupload(\"$uprow\",\"false\",\"$tableName\",\"AFFIX\");'>$text.get("common.lb.del")</a>"+
						#end
							"&nbsp;&nbsp;&nbsp;&nbsp;<a href='/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=$tableName' target='_blank'>$text.get("common.lb.download")</a></li>	");		
				#end
			#end		
		#elseif("$row.inputType" != "17" && "$row.fieldType" == "1" && "$row.fieldName" != "id"  && "$row.fieldName" != "moduleType")
			#if($!isDetail)
				$("#$row.fieldName").val("$!globals.formatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)"); 
			#else
				$("#$row.fieldName").val("$!globals.newFormatNumber($!values.get($row.fieldName),false,false,$!globals.getSysIntswitch(),$mainTable.tableName,$row.fieldName,true)"); 
			#end		
		#elseif("$row.inputType" != "17" && "$row.fieldName" != "id"  && "$row.fieldName" != "moduleType")
			$("#$row.fieldName").val("$!globals.rereplaceSpecLitter2("$!values.get($row.fieldName)")");  
		#end
		
		#if("$row.inputType" == "8"&&"$row.inputTypeOld" == "1") ##只读的选择框

			#set($enumName = "")
			#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
				#if("$!erow.value" == "$values.get($row.fieldName)")
					#set($enumName = $erow.name)
				#end
			#end
			#set($en = ${row.fieldName} +"Name" )
			$("#$en").val("$!globals.rereplaceSpecLitter2("$enumName")"); //$enumName
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
	#if(("$quote"=="quote" || "$saveDraft" == "quoteDraft" || "$saveDraft" == "draft" ) && $mainTable.triggerExpress==1 && !$!isDetail) 
		loadFireEvent(); 
	#end 
	#if("$detail"=="detail") 
		readonlyForm(document.form); 
	#end 
	execStat();
	
	initDownSelect(); 
	showStatus(); 
	bpWidth();
	showGridTags(); 
	jQuery(':text:eq(0)').focus(); 
	initTxtTitle();
	
	changeConfirm();
	
	jQuery("#listRange_mainField :text").each(function(){
	this.title=this.value;});
});



</script>
</head>

<body scroll="no" onKeyDown="down();">

<iframe name="formFrame" style="display:none"></iframe>
<!--  button onclick="document.form.Remark.value=document.body.innerHTML">ddd</button>-->
<form  method="post" onKeyDown="down()" scope="request" id="form" name="form" action="/UserFunctionAction.do" onSubmit="return beforSubmit(this);" target="formFrame"> 
<input type="hidden" id="conver" name="conver" />
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_UPDATE")" />
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()" />
<input type="hidden" id="tableName" name="tableName" value="$!tableName" />
<input type="hidden" id="button" name="button" value="" />
<input type="hidden" id="frameName" name="frameName" value="$fName" />
<input type="hidden" id="winCurIndex" name="winCurIndex" value="$winCurIndex" />
<input type="hidden" id="popWinName" name="popWinName" value="$!popWinName"/>
<input type="hidden" id="pageNo" name="pageNo" value="$!pageNo" />
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName" />
<input type="hidden" id="defineInfo" name="defineInfo" value="" />
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType" />
<input type="hidden" id="detLineNo" name="detLineNo" value="" />
<input type="hidden" id="saveDraft" name="saveDraft" value="$saveDraft" />
<input type="hidden" id="noback" name="noback" value="$!noback" />
<input type="hidden" id="logicValidate" name="logicValidate" value="" />
<input type="hidden" id="defineName" name="defineName" value="" />
<input type="hidden" id="popReturnVal" name="popReturnVal" value="" />

<input type="hidden" id="checkFieldName" name="checkFieldName" value="" />
<input type="hidden" id="fromCRM" name="fromCRM" value="$!fromCRM"/>
<input type="hidden" id="fromPage" name="fromPage" value="$!fromPage" />
<input type="hidden" id="checkTab" name="checkTab" value="$!checkTab" /> 

#if($!isDetail)
<input type="hidden" id="pageType" name="pageType" value="detail" />
#else
<input type="hidden" id="pageType" name="pageType" value="update" />
#end
<input type="hidden" id="LinkType" name="LinkType" value="$!LinkType" />
<input type="hidden" id="pframeName" name="pframeName" value="" />
<input type="hidden" id="varKeyIds" name="varKeyIds" value="$!values.get("id")" />
<input type="hidden" id="fresh" name="fresh" value="$!fresh"/>
<input type="hidden" id="retValUrl" value="" />
<input type="hidden" id="hasFrame" name="hasFrame" value=""/>
<input type="hidden" id="ButtonType" name="ButtonType" value=""/>
<input type="hidden" id="ButtonTypeName" name="ButtonTypeName" value="" />
#if("$!planType"!="")
<input type="hidden" id="planType" name="planType" value="$!planType"/>
<input type="hidden" id="strType" name="strType" value="$!strType"/>
<input type="hidden" id="strDate" name="strDate" value="$!strDate"/>
<input type="hidden" id="userId" name="userId" value="$!userId"/>
#end
<input type="hidden" id="deliverTo" name="deliverTo" value=""/>
<input type="hidden" id="OAWorkFlow" name="OAWorkFlow" value="#if($!OAWorkFlow)true#{else}false#end"/>
<input type="hidden" id="designId" name="designId" value="$!designId"/>
<input type="hidden" id="approveStatus" name="approveStatus" value="#if($!request.getParameter("approveStatus"))$!request.getParameter("approveStatus")#{else}transing#end"/>
#set($isAdd=false)
#if($!OAWorkFlow)
<input type="hidden" id="currNodeId" name="currNodeId" value="$!currNodeId"/>
<input type="hidden" id="fromAdvice" name="fromAdvice" value="$!noback"/>
#end
#if("$copy" == "copy")
<input type="hidden" id="id" name="id" value="" />
<input type="hidden" id="parentCode" name="parentCode" value="$!parentCode"/> 
#else
<input type="hidden" id="id" name="id" value="$!values.get("id")" />
<input type="hidden" id="parentCode" name="parentCode" value="$globals.classCodeSubstring("$!parentCode",5)"/>
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


#set($printCount=false)

<div class="Heading">
	
	<div class="HeadingTitle">
		<b class="icons"></b>
		#if("$!moduleName"=="")$!mainTable.display.get("$globals.getLocale()")#else$!moduleName#end -
		<font style="color:red">
		#if("$!copyParent" == "copyParent")
		添加
		#elseif("$copy" == "copy")
		复制
		#elseif("$quote" == "quote")
		引用
		#elseif("$saveDraft" == "quoteDraft")
		引用草稿
		#elseif("$saveDraft" == "draft" && $!isDetail)
		草稿详情
		#elseif("$saveDraft" == "draft")
		草稿
		#elseif($!isDetail)
		#if("$!currentNode"=="" || "$!currentNode"=="0")详情 #elseif($!OAWorkFlow && "$!currentNode"=="-1")已审核 #else $!currentNodeName  #end
		#else
		#if("$!currentNode"=="" || "$!currentNode"=="0")修改 #elseif($!OAWorkFlow && "$!currentNode"=="-1")已审核 #else $!currentNodeName  #end
		#end
		#if("$!brotherRMName" != "") ($!brotherRMName) #end
		</font>
	</div>
	<ul class="HeadingButton f-head-u">

	
#if(!$!isDetail) 
		<!--新增操作，复制，引用保存--> 
	#if($globals.getMOperationMap($request).add() &&("$copy" == "copy"||"$quote" == "quote"))				
		#if($!OAWorkFlow) 
		#set($isAdd=true) 
		<li><span id="copySave" name="save"   class="btn btn-small" onClick="if(beforSubmit(document.form)) {form.deliverTo.value='true';form.button.value='#if("$saveDraft" == "quoteDraft"||"$saveDraft" == "draft")quoteDraft#end';window.save=true;form.operation.value=$globals.getOP("OP_ADD"); document.form.submit();}">保存并送审</span></li>
		<li><span id="copySavetemp" name="savetemp"   class="btn btn-small" onClick="if(beforSubmit(document.form)) {form.button.value='#if("$saveDraft" == "quoteDraft"||"$saveDraft" == "draft")quoteDraft#end';window.save=true;form.operation.value=$globals.getOP("OP_ADD"); document.form.submit();}">保存不送审</span></li>
		#elseif("$copy" == "copy")
		<li><span id="copySave" name="save" class="btn btn-small" onClick="newSave();">保存</span></li>
		<li><span id="copyNew" defbtn="saveAdd" name="saveAdd"   class="btn btn-small" onClick="copyAdd();">保存再复制</span></li>
		#elseif("$quote" == "quote")
		<li><span id="copySave" name="save"   class="btn btn-small" onClick="newSave();">$text.get("common.lb.quoteSave")</span></li>
		#end	
						
		#if($mainTable.draftFlag==1)
		<li><span name="saveDraft"  class="btn btn-small" onclick="javascript:void(0);saveDraft2();">$text.get("common.lb.saveDraft")</span></li>		
		#end  
	#elseif(($globals.getMOperationMap($request).update()
					|| "true"==$globals.getSysSetting("draftUpdate") ) && "$update" == "update") 
		#if("$saveDraft" == "quoteDraft" || "$saveDraft" == "draft")  
			#if(!$!OAWorkFlow)
			<li><span id="copySave" name="copySave" class="btn btn-small"  onClick="form.deliverTo.value='true';draftSave();">保存</span></li>		
			#else
			<li><span id="deliverToBut" name="deliverToBut" class="btn btn-small"  onClick="draftSaveDeliver();">保存并送审</span></li>		 
			<li><span id="copySave" name="copySave" class="btn btn-small"  onClick="draftSave();">保存不送审</span></li>
			#end 
			<li><span id="copySave"  name="copySave"     class="btn btn-small"  onClick="form.button.value='saveDraft';if(beforSubmit(document.form)) {window.save=true;form.operation.value=$globals.getOP("OP_UPDATE"); document.form.submit();}">$text.get("common.lb.saveDraft")</span></li>		
		#elseif("$saveDraft" != "quoteDraft" && "$saveDraft" != "draft") 
			#if(!$!OAWorkFlow) 
			<li><span id="copySave"  name="copySave"  class="btn btn-small"  onClick=" if(beforSubmit(document.form)) {form.button.value='';window.save=true;document.form.submit();}">$text.get("common.lb.save")</span></li>		
			#else 
			<li><span id="deliverToBut" name="deliverToBut" class="btn btn-small"  onClick="if(beforSubmit(document.form)) {form.button.value='';form.deliverTo.value='true';window.save=true; document.form.submit();}">保存并送审</span></li>		
			<li><span id="copySave"  name="copySave"   class="btn btn-small"  onClick="if(beforSubmit(document.form)) {form.button.value='';window.save=true;document.form.submit();}">保存不送审</span></li>		
			#end
		#end 
	#end			
	
#end	
	
#if("true"!="$!isLinkType")	
	#if("$copy" != "copy" && "$saveDraft" != "quoteDraft" && "$quote" != "quote"  )
	<li><span class="btn btn-small" id="preOne" onClick="changePos('PRE');">上条</span></li>
	<li><span class="btn btn-small" id="nextOne" onClick="changePos('NEXT');">下条</span></li>
	#end
#end
	
	#if($!updateRight && $!isDetail )
		<li><span class="btn btn-small" #if($!CannotOper)disabled="true"#end onClick="update();">$text.get("common.lb.update")</span></li>
	#end
	#if($globals.getMOperationMap($request).update()&&$!updateRight && !$!CannotOper && "$workFlow"!="OA" && $!isDetail && ("$saveDraft" == "quoteDraft"||"$saveDraft" == "draft"))
		<li><span class="btn btn-small" #if($!CannotOper)disabled="true"#end onClick="draftAudit();">保存过帐</span></li>
	#end
	#if($checkRight && $!isDetail&& !$!CannotOper) 
	<li><span class="btn btn-small" id="deliverToBut" name="deliverToBut"  onClick="checkSave()">审核</span></li>
	#end
	
	#if($retCheckRight && $!isDetail&& !$!CannotOper) 
	<li><span class="btn btn-small" id="redeliverToBut" name="redeliverToBut"  onClick="reversecheck()">反审核</span></li>
	#end
	#if($hasCancel && $!isDetail&& !$!CannotOper) 
	<input type="hidden" id="nextStep" name="nextStep" value=""/>
	<li><span class="btn btn-small" id="checkcancelBut" name="checkcancelBut"  onClick="checkcancel()">撤回</span></li>
	#end
	#if($hurryTrans && $!isDetail&& !$!CannotOper) 
	<li><span class="btn btn-small" id="hurryToBut" name="hurryToBut"  onClick="hurryTrans()">催办</span></li>
	#end

	#if(("$auditprint"=="0" || "$flowStatus"=="finish"||"$flowStatus"==""||$currNodeId==-1)&&"$print"=="true" && $globals.getMOperationMap($request).print() && !("$copy" == "copy"||"$quote" == "quote"))
		#set($printCount=true)
		<li><span class="btn btn-small" onclick='javascript:printControl("/UserFunctionQueryAction.do?tableName=$tableName&reportNumber=$!reportNumber&moduleType=$!moduleType&BillID=$!values.get("id")&operation=$globals.getOP("OP_PRINT")&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex")'>$text.get("common.lb.print")</span></li>
	#end
	     
	<li>
		<div class="btn btn-small h-child-btn">
			<a id="tb_opBtn" moreId="opMore">操作</a>
			<div id="opMore" class="d-more">
				<div class="out-css">
	#if($globals.getMOperationMap($request).add()  && "$workFlow"!="OA" && !$!CannotOper && !("$copy" == "copy"||"$quote" == "quote"||"$saveDraft" == "quoteDraft"||"$saveDraft" == "draft")) 
		<a onClick="location.href='/UserFunctionAction.do?tableName=$tableName&operation=6&parentCode=$!globals.classCodeSubstring("$!parentCode",5)&parentTableName=$!parentTableName&moduleType=$!moduleType&f_brother=$!values.get("f_brother")&noback=$!noback&winCurIndex=$winCurIndex&popWinName=$!popWinName'">$text.get("common.lb.add")</a>
	#end	

	#if($!print && $globals.getMOperationMap($request).print()&&$globals.getSysSetting("saveBeforePrint")=="true" && ("$copy" == "copy"||"$quote" == "quote"||"$saveDraft" == "quoteDraft"||"$saveDraft" == "draft"))
		<a href="javascript:printSave()">$text.get("common.lb.print")</a>
	#end
				
	#foreach($brow in $customButton)
		$!brow
	#end	
	$!extendButton
	#if($LoginBean.id=='1' && "$!workFlow"!="OA" && "$!moduleTable"==""  && $mainTable.isLayout != 1)
	<a id="CustomSetLink" href="javascript:showDivCustomSetTable();">$text.get("com.define.colconfig")</a>
	#end
	#if($mainTable.wakeUp=="1")
	<a href="javascript:alertSet('$!values.get("id")')">$text.get("workflow.msg.warmsetting")</a>
	#end
	#if($existSMS)
	<!-- a href="javascript:sendBillMsg('update');">$text.get("common.lb.sendMessage")</a>  -->
	#end
	#if($inIniAmount)
	   <a id="inIniAmount" href="/UserFunctionAction.do?tableName=$tableName&keyId=$keyId&operation=$globals.getOP("OP_UPDATE_PREPARE")&f_brother=$!f_brother&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex&onOff=onOff">$!inIniAmountName</a>
	#end
	#if($!OAWorkFlow && $!designId != "")
	<a href="javascript:flowDepict('$!designId','$values.get('id')');">$text.get("common.lb.checkFlowChart")</a>
	#end
	
	#if($globals.getMOperationMap($request).sendEmail() && !("$copy" == "copy"||"$quote" == "quote"||"$saveDraft" == "quoteDraft"||"$saveDraft" == "draft"))
			#if("$!detail"!="detail")#set($isUpdate="update")#end
			#if($!OAWorkFlow)
			<a href="javascript:mdiwin('/UserFunctionAction.do?noback=true&fromJsp=$!isUpdate&type=email&parentCode=$globals.classCodeSubstring("$!parentCode",5)&keyId=$keyId&tableName=$tableName&operation=$globals.getOP("OP_DETAIL")&parentTableName=$!parentTableName&saveDraft=$!saveDraft','$text.get("oa.mail.sendBill")')">$text.get("common.lb.sendmail")</a>	
			#else
			<a href="/UserFunctionAction.do?noback=$!noback&fromJsp=$!isUpdate&type=email&parentCode=$globals.classCodeSubstring("$!parentCode",5)&keyId=$keyId&tableName=$tableName&operation=$globals.getOP("OP_DETAIL")&parentTableName=$!parentTableName&saveDraft=$!saveDraft&winCurIndex=$!winCurIndex">$text.get("common.lb.sendmail")</a>	
			#end
	#end
	
	#if($globals.getMOperationMap($request).add() && "$workFlow"!="OA" && !("$copy" == "copy"||"$quote" == "quote"||"$saveDraft" == "quoteDraft"||"$saveDraft" == "draft")) 
		#if($globals.getTableInfoBean($tableName).getExtendButton().indexOf("copy")>0 && !$!CannotOper )
			<a href="javascript:copyOp('$!values.get("id")')">$text.get("common.lb.copy")</a>
		#end
	#end
	#if("$copy" != "copy" &&  "$quote" != "quote" && "$saveDraft" != "quoteDraft" && $globals.getMOperationMap($request).print())
	<a href="javascript:billExport('$values.get("id")');">导出</a> 
	#end
	#if("$!values.get('isCatalog')" != "1" && $!deleteRight && "$copy" != "copy" &&  "$quote" != "quote" && "$saveDraft" != "quoteDraft")
	<a href="javascript:deleteBill();">删除</a>
	#end
	
				</div>
			</div>
			<b class="triangle"></b>
		</div>			
	</li>
	#if("$!pushBtn"!="")
	<li>
		<div class="btn btn-small h-child-btn">
			<a id="tb_pushBtn" moreId="pushMore">推单</a>
			<div id="pushMore" class="d-more">
				<div class="out-css">
				$!pushBtn	
				</div>			
			</div>
			<b class="triangle"></b>
		</div>			
	</li>
	#end	
	<li>
		<div class="btn btn-small h-child-btn">
			<a id="tb_toolBtn" moreId="toolMore">工具</a>
			<div id="toolMore" class="d-more" >
				<div class="out-css">
				$!toolBtn	
		
		#set($hasGoodsBath = false)
		#foreach ($rowlist in $childTableList )  
		#if(!$!isDetail && "$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$!rowlist.isUsed"=="1" && "$!rowlist.tableName" != "tblGoodsUnit"  && "$!rowlist.isView"=="0"&& ($childTableConfigList.get($rowlist.tableName).size()>0 || $allConfigList.size()==0))
			#foreach ($row in $rowlist.fieldInfos ) 
			#if(!$hasGoodsBath && "$row.fieldName"=="GoodsCode"  && $row.inputType != 3) #set($hasGoodsBath = true) 
			<a href="javascript:batchInput()">商品批量录入</a>
			#end
			#end
		#end	
		#end	
					
					
					<a href="javascript:calc()">计算器</a>
				</div>				
			</div>
			<b class="triangle"></b>
		</div>			
	</li>
	#if("$!queryBtn"!="")
	<li>
		<div class="btn btn-small h-child-btn">
			<a id="tb_queryBtn" moreId="queryMore">联查</a>
			<div id="queryMore" class="d-more">
				<div class="out-css">
					$!queryBtn		
				</div>		
			</div>
			<b class="triangle"></b>
		</div>			
	</li>
	#end
		
	
	#if("$!fresh"=="dialog")
	<li><span class="btn btn-small" defbtn="backList" onClick="refreshcloseWindows();">$text.get("common.lb.back")</span></li>		
	#else
	<li><span class="btn btn-small"  defbtn="backList" onClick="closeWindows('$!popWinName');">$text.get("common.lb.back")</span></li>			
	#end

	</ul>
</div>
#if($parentName.length()>0)			
	<div class="scroll_function_big"><span>$text.get("common.msg.currstation")：$!parentName</span> </div>
#end

	<div id="listRange_id" style="position:relative;">
		<div class="wrapInside">

#if($mainTable.isLayout != 1)		
#parse("/vm/classFunction/functionLayout.jsp")
#else
$!globals.getlayoutHtml($tableName,$fieldInfos,$childTableList,$globals.getLocale(),$detail)
#end

#if($!OAWorkFlow&&!$!isAdd)
#if("$!flowNew"=="true")
<div style="padding-left: 8px;">
	#if($!delivers.size()>0)
	<div class="view-ul-wp">
		<p class="t-p">审核记录<span class="hBtns" onClick="addAffix()">补充意见</span></p>
		<ul class="view-ul">
			#foreach($!deliver in $!delivers)
			<li class="view-li">
				<div class="d-pa">
					<em class="node-em">第$!velocityCount步&nbsp;&nbsp;$!deliver.nodeID</em>
					<span class="check-person">$!deliver.checkPerson</span>
				</div>
				<div class="d-dbk">
					<em class="end-time">[$!deliver.endTime]</em>
					<div class="app-work-check">
						&nbsp;$!deliver.approvalOpinions<br>
						#foreach($affix in $globals.strSplitByFlag($deliver.affix,';'))
							#if("$!affix"!="")
							$affix <a href="/ReadFile?fileName=$globals.urlEncode($affix)&realName=$globals.urlEncode($affix)&tempFile=false&type=AFFIX&tableName=$tableName&down=true" target="_blank">$text.get("common.lb.download")</a>
							#end
						#end
					</div>
					<em class="node-type">
						$!deliver.nodeType #if("$!deliver.workFlowNode"!="")-> [$!deliver.workFlowNode]-$!deliver.checkPersons #end
					</em>
				</div>
			</li>
			#end
		</ul>
		<p style="margin-top:10px;"><span class="hBtns" onClick="addAffix()">补充意见</span></p>
	</div>
 	#end
#else ##老流程
	#if($!delivers.size()>0)
	<div class="view-ul-wp">
		<p class="t-p">审核记录 #if(!$!checkRight)<span class="hBtns" onClick="addAffix()">补充意见</span>#end</p>
		<ul class="view-ul">
			#foreach($!deliver in $!delivers)
			<li class="view-li">
				<div class="d-pa">
					<em class="node-em">第$!velocityCount步&nbsp;&nbsp;$!workFlowDesignBeans.get($!designId).getFlowNodeMap().get($!deliver.nodeId).display</em>
					<span class="check-person">$!deliver.empFullName</span>
				</div>
				<div class="d-dbk">
					<em class="end-time">[$!deliver.attTime]</em>
					<div class="app-work-check">
						&nbsp;$!deliver.deliverance.replaceAll("\r\n","<br>")<br>
						
					</div>
					<em class="node-type">
						$!deliver.nodeType #if("$!deliver.workFlowNode"!="")-> [$!deliver.workFlowNode]-$!deliver.checkPersons #end
					</em>
				</div>
			</li>			
			#end
			#foreach($!deliver in $!affixs)
			<li style="width:95%;margin-top: 10px;">
					$text.get("oa.add.affix.person")：$!deliver.empFullName&nbsp;&nbsp;&nbsp;$text.get("oa.flow.upload.time")：$!deliver.attTime</li>&nbsp;&nbsp;&nbsp;
					$text.get("oa.flow.affix.name")： 
					#foreach($affix in $globals.strSplitByFlag($deliver.affix,';'))
					$affix <a href="/ReadFile?fileName=$globals.urlEncode($affix)&realName=$globals.urlEncode($affix)&tempFile=false&type=AFFIX&tableName=$tableName&down=true" target="_blank">$text.get("common.lb.download")</a>
					#end
			#end
		</ul>
		<p style="margin-top:10px;">#if(!$!checkRight)<span class="hBtns" onClick="addAffix()">补充意见</span>#end</p>
	</div>
 	#end
#end 
<input type="hidden" id="attachFiles" name="attachFiles" value="">
<input type="hidden" id="delFiles" name="delFiles" value="">
</div>
<script type="text/javascript">
function addAffix(){
	asyncbox.open({
		id : 'dealdiv',
　　　   	url : '/OAMyWorkFlow.do?keyId=$!values.get("id")&tableName=$!tableName&operation=6',
	 	title : '$text.get("com.flow.add.affix")',
　　 　 	width : 650,
　　 　	height : 370,
        btnsbar : jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
		   if(action == 'ok'){
				if(opener.beforSubmit(opener.form)){
					opener.form.submit();
				}
				return false;
			}
	　　  }
	});
}
function dealAsyn(){
	location.href='/UserFunctionAction.do?tableName=$!tableName&keyId=$!values.get("id")&moduleType=$!moduleType&f_brother=$!f_brother&operation=7&winCurIndex=$!winCurIndex&pageNo=$!pageNo&parentCode=$!parentCode&parentTableName=$!parentTableName&saveDraft=$!saveDraft&noback=$!noback';
}
</script>
#end



		</div>
	</div>
<div class="update-bottom" id="update-bottom">
	<div class="w150">制单人：$!values.get('createByName')</div> 
	<div class="w250">制单时间：$!values.get('createTime')</div>
	<div class="w150">最后修改人：$!values.get('lastUpdateByName')</div> 
	<div class="w250">最后修改时间：$!values.get('lastUpdateTime')</div>
	<div class="w150">打印次数：$!values.get('printCount')</div>
</div>	
</form> 
#if($LoginBean.id =="1")
<form method="post" id="colConfigForm" name="colConfigForm" action="/ColConfigAction.do?operation=1" target="_self">
<input type="hidden" id="tableName" name="tableName" value="$!tableName"/>
<input type="hidden" id="parentCode" name="parentCode" value="$!parentCode">
<input type="hidden" id="parentTableName" name="parentTableName" value="$!parentTableName">
<input type="hidden" id="allTableName" name="allTableName" value="$!allTableName"/>
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType">
#if("$!detail"=="detail")#set($strOperation = 5)#else#set($strOperation = 7) #end
<input type="hidden" id="strAction" name="strAction" value="$globals.getModuleUrlByWinCurIndex("$!winCurIndex")&operation=$!strOperation&keyId=$!values.get('id')&moduleType=$!moduleType&noback=$!noback&parentCode=$!parentCode&f_brother=$!f_brother&parentTableName=$!parentTableName&winCurIndex=$!winCurIndex" />
<input type="hidden" id="colType" name="colType" value="bill" />
<input type="hidden" id="colSelect" name="colSelect" value="" />
 </form>
#end

	<script type="text/javascript"> 
		var oDiv2=m("listRange_id");
		var sHeight2=document.documentElement.clientHeight- 30;
		var bottomH = m("update-bottom").offsetHeight+10; //显示制单人高度  
		sHeight2 = sHeight2 - bottomH;
		#if($parentName.length()>0) sHeight2 =sHeight2-25;   #end ##分级的还要去掉显示根目录的高度  
		oDiv2.style.height=sHeight2+"px";	
		
		#if($mainTable.isLayout != 1) ##个性布局不要算这些高度
			var mainFieldH  =m("listRange_mainField").offsetHeight;
				
			var remarkH =m("listRange_remark").offsetHeight;
			
			var tabH = sHeight2-mainFieldH-remarkH-45;
			if(tabH<150) tabH=150;
			if($("#listRange_tableInfo >div").size()==0){
				$("#listRange_tableInfo").height(0); //没有表格和附件 
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
		
		
	//在文字后加空格，chrome下实现两端对齐  
	cyh.lableAlign(); 
	if(window.parent.parent.frames[1] != undefined)
		form.pframeName.value=window.parent.parent.frames[1].name;
	jQuery(function(){
	jQuery("#listRange_tableInfo div").css("max-width",document.body.clientWidth+"px");
	/*    zxy 这段代码13年就存在会导致明细表锁列行时错位，暂时屏蔽
	var mHeight = document.body.clientHeight-jQuery(".Heading")[0].offsetHeight-jQuery(".showGridTags")[0].offsetHeight-document.getElementById("update-bottom").offsetHeight-document.getElementById("listRange_remark").offsetHeight-document.getElementById("listRange_mainField").offsetHeight-17;
	if(mHeight < 100){
		jQuery("#listRange_tableInfo div").css("max-height","100px");
	}else{
		jQuery("#listRange_tableInfo div").css("max-height",mHeight);
	} */
	});
</script>
</body>
</html>
