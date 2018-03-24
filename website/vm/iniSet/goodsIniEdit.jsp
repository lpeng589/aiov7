<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("unitadmin.title.update")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="/js/longTime.vjs"></script>
<script language="javascript" src="/js/editGrid.js"></script>
<script language="javascript">
#if($globals.hasUsedSeq('tblStockDet'))
function addSeq(o,goodsCode){
	if(o.value.trim().length==0)return false;
	if(!isEn(o.value)){
		alert('$text.get("common.lb.seq")'+":"+"$text.get('common.validate.error.en')");
		return false;
	}
	var ri=o.parentNode.parentNode.rowIndex-1;
	var seqStr=document.getElementsByName("tblGoodsOfProp_"+o.name)[ri].value;
	var seqList=seqStr.split("~");
	var defSeqStr="";
	if(defSeqArr.length>=ri+1){
		defSeqStr=defSeqArr[ri];
	}
	var lastSeq="";
	var hidSeqs=document.getElementsByName("tblGoodsOfProp_"+o.name);
	for(i=hidSeqs.length-1;i>=0;i--){
		if(hidSeqs[i].value!=""){
			lastSeq=hidSeqs[i].value;break;
		}
	}
	//验证序列号是否重复








	for(i=0;i<hidSeqs.length;i++){
		var seqList1=hidSeqs[i].value.split("~");
		for(j=0;j<seqList1.length-1;j++){
		    if(seqList1[j]==o.value){
		     	alert('$text.get("seq.validate.error10")'+'['+o.value+']'+'$text.get("seq.validate.error10_1")'+'['+(i+1)+']'+'$text.get("seq.validate.error10_2")');
				return false;
		    }
	 	}
	}
	
	var urls="/UtilServlet?operation=validateSeq&tableName=tblIniStockDet&validateField="+o.name+"&goodsCode="+goodsCode+"&oldSeq="+seqStr+"&defSeqStr="+defSeqStr+"&seqStr="+seqStr+o.value+"~"+"&lastSeq="+lastSeq;
	AjaxRequest(urls) ;
	if(response!="" && response!="no response text"){
		alert(response);
		return false;
	}else{
			
		if(o.value!=seqList[seqList.length-2]){
			seqStr=seqStr+o.value+"~";
			document.getElementsByName("tblGoodsOfProp_"+o.name)[ri].value=seqStr;
			var seqQtyArr=document.getElementsByName('$!SeqQtyfName');
			seqQtyArr[ri].value=seqList.length;
			if(document.createEvent){
	    		var evt = document.createEvent("HTMLEvents");	 	    		
	    		evt.initEvent("change",false,false);
	    		seqQtyArr[ri].dispatchEvent(evt);	
			 }else{
				seqQtyArr[ri].fireEvent("onchange");
			 }	
			
		}
			
	}
}
#end
var digits=$globals.getDigits();
var digitsDet=$globals.getDetDigits();
var defSeqArr=new Array();//保存修改加载后的最初序列号字符串









function getDefaultSeq(){
    var seqFname=document.getElementById("SeqPropfName").value;
    var objs=document.getElementsByName('tblGoodsOfProp_'+seqFname); 
	for(var i=0;i<objs.length;i++){
		defSeqArr[i]=objs[i].value;
	}
	
}

function openInputDate(obj)
{
	var d = new Date();
	LaunchCalendar(obj,d); 
}

function openInputTime(obj)
{
	setday(obj); 
}

function iniValueChange(vars)
{	
	if((isNaN(vars.value)||vars.value.length==0)&&vars.name!='setUnit'){
		vars.value=0;
	}		
	
	var mi =document.getElementsByName(vars.name);	
	var pos;
	for(i=0;i<mi.length;i++){
		if(mi[i] == vars){
			pos = i;	
		}	
	}
	
	var InstoreAmount=document.getElementsByName("InstoreAmount")[pos];
	var InstoreQty=document.getElementsByName("InstoreQty")[pos];
	var InstorePrice=document.getElementsByName("InstorePrice")[pos];	
	
	#if($existsTowUnit)
	var IniTwoQty=document.getElementsByName('IniTwoQty')[pos];	
	var twoUnitPro=document.getElementsByName('$!twoUnitPro')[pos];
	#end
	#if("$!globals.getSysSetting('AssUnit')"=="true")<!--启用辅助单位-->
	var conv=document.getElementsByName('conversionRate')[pos];
	var secUnitQty=document.getElementsByName('secUnitQty')[pos];
	var secUnitPrice=document.getElementsByName('secUnitPrice')[pos];
	#end
	if(vars.name=='InstoreQty'||vars.name=='setUnit')	{
		
		if(InstoreQty.value<0){
			InstoreQty.value=0;			
			return;
		}	
		InstoreAmount.value=f(f(InstoreQty.value,$globals.getDigitsOrSysSetting('tblStockDet','InstoreQty'))*f(InstorePrice.value,$globals.getDigitsOrSysSetting('tblStockDet','InstorePrice')),$globals.getDigitsOrSysSetting('tblStockDet','InstoreAmount'));
		InstoreQty.value=f(InstoreQty.value,$globals.getDigitsOrSysSetting('tblStockDet','InstoreQty'));
		
		#if("$!globals.getSysSetting('AssUnit')"=="true")<!--启用辅助单位-->
			if(conv.value.indexOf("/")>0)
		  		secUnitQty.value=Number(parseFloat(InstoreQty.value)*parseFloat(conv.value.substr(conv.value.indexOf("/")+1)/conv.value.substr(0,conv.value.indexOf("/")))).toFixed(digitsDet);
		  	else
		  		secUnitQty.value=Number(parseFloat(InstoreQty.value)/parseFloat(conv.value)).toFixed(digitsDet);
		  		
		    if(!isFloat(secUnitQty.value)) secUnitQty.value='0';
            else secUnitQty.value=f(secUnitQty.value,$globals.getDigitsOrSysSetting('tblStockDet','secUnitQty'));
		    secUnitPrice.value=Number(parseFloat(InstoreAmount.value)/parseFloat(secUnitQty.value)).toFixed(digitsDet);
		    if(!isFloat(secUnitPrice.value)) secUnitPrice.value='0';
            else secUnitPrice.value=f(secUnitPrice.value,$globals.getDigitsOrSysSetting('tblStockDet','secUnitPrice'));
		#end
		#if($existsTowUnit&&$twoUnitPro)
		IniTwoQty.value=Number(parseFloat(InstoreQty.value)/parseFloat(twoUnitPro.value)).toFixed(digitsDet);
		 if(!isFloat(IniTwoQty.value)) IniTwoQty.value='0';
          else IniTwoQty.value=f(IniTwoQty.value,$globals.getDigitsOrSysSetting('tblStockDet','IniTwoQty'));
		#end
		
	}
	else if(vars.name=='InstorePrice')
	{			
		if(vars.value<0){
			vars.value=0;			
			return;
		}		
		InstoreAmount.value=f(f(InstoreQty.value,$globals.getDigitsOrSysSetting('tblStockDet','InstoreQty'))*f(InstorePrice.value,$globals.getDigitsOrSysSetting('tblStockDet','InstorePrice')),$globals.getDigitsOrSysSetting('tblStockDet','InstoreAmount'));
		vars.value=f(InstorePrice.value,$globals.getDigitsOrSysSetting('tblStockDet','InstorePrice'));		
		#if("$!globals.getSysSetting('AssUnit')"=="true")<!--启用辅助单位-->
		 secUnitPrice.value=Number(parseFloat(InstoreAmount.value)/parseFloat(secUnitQty.value)).toFixed(digitsDet);
		   if(!isFloat(secUnitPrice.value)) secUnitPrice.value='0';
          else secUnitPrice.value=f(secUnitPrice.value,$globals.getDigitsOrSysSetting('tblStockDet','secUnitPrice'));
		#end
	}
	else if(vars.name=='InstoreAmount')
	{
		if(InstoreQty.value==0){
			InstorePrice.value=0;
		}else{
			InstorePrice.value=f(parseFloat(InstoreAmount.value)/parseFloat(InstoreQty.value),$globals.getDigitsOrSysSetting('tblStockDet','InstorePrice'));
		}
		#if("$!globals.getSysSetting('AssUnit')"=="true")<!--启用辅助单位-->
		if(secUnitQty.value==0){
		 	secUnitPrice.value=0;
		}else{
			secUnitPrice.value=Number(parseFloat(InstoreAmount.value)/parseFloat(secUnitQty.value)).toFixed(digitsDet);
		}
		   if(!isFloat(secUnitPrice.value)) secUnitPrice.value='0';
          else secUnitPrice.value=f(secUnitPrice.value,$globals.getDigitsOrSysSetting('tblStockDet','secUnitPrice'));
		#end
	}
	#if("$!globals.getSysSetting('AssUnit')"=="true")<!--启用辅助单位-->
	 else if(vars.name=='secUnitQty'){
	    if(vars.value<0){
			vars.value=0;			
			return;
		}	
	 	if(conv.value.indexOf("/")>0)
		  	InstoreQty.value=Number(parseFloat(secUnitQty.value)/parseFloat(conv.value.substr(conv.value.indexOf("/")+1)*conv.value.substr(0,conv.value.indexOf("/")))).toFixed(digitsDet);
		else
		  	InstoreQty.value=Number(parseFloat(secUnitQty.value)*parseFloat(conv.value)).toFixed(digitsDet);
	      if(!isFloat(InstoreQty.value)) InstoreQty.value='0';
          else InstoreQty.value=f(InstoreQty.value,$globals.getDigitsOrSysSetting('tblStockDet','InstoreQty'));
	   InstoreAmount.value=Number(parseFloat(secUnitQty.value)*parseFloat(secUnitPrice.value)).toFixed(digitsDet);
	   InstorePrice.value=parseFloat(InstoreAmount.value)/parseFloat(InstoreQty.value);
	      if(!isFloat(InstorePrice.value)) InstorePrice.value='0';
          else InstorePrice.value=f(InstorePrice.value,$globals.getDigitsOrSysSetting('tblStockDet','InstorePrice'));
	 }else if(vars.name=='secUnitPrice'){
	 	if(vars.value<0){
			vars.value=0;			
			return;
		}	
	   InstoreAmount.value=Number(parseFloat(secUnitQty.value)*parseFloat(secUnitPrice.value)).toFixed(digitsDet);
	   InstorePrice.value=parseFloat(InstoreAmount.value)/parseFloat(InstoreQty.value);
	   if(!isFloat(InstorePrice.value)) InstorePrice.value='0';
       else InstorePrice.value=f(InstorePrice.value,$globals.getDigitsOrSysSetting('tblStockDet','InstorePrice'));
	 }
	#end
	#if($existsTowUnit)
	 else if(vars.name=='IniTwoQty'){
	InstoreQty.value=Number(parseFloat(IniTwoQty.value)*parseFloat(twoUnitPro.value));
	 if(!isFloat(InstoreQty.value)) InstoreQty.value='0';
          else InstoreQty.value=f(InstoreQty.value,$globals.getDigitsOrSysSetting('tblStockDet','InstoreQty'));

	}else if(vars.name='$twoUnitPro'){
	     InstoreQty.value=Number(parseFloat(IniTwoQty.value)*parseFloat(twoUnitPro.value));
	 if(!isFloat(InstoreQty.value)) InstoreQty.value='0';
          else InstoreQty.value=f(InstoreQty.value,$globals.getDigitsOrSysSetting('tblStockDet','InstoreQty'));
		 
		  InstoreAmount.value=Number(parseFloat(InstoreQty.value)*parseFloat(InstorePrice.value)).toFixed(digitsDet);
	 if(!isFloat(InstoreAmount.value)) InstoreAmount.value='0';
          else InstoreAmount.value=f(InstoreAmount.value,$globals.getDigitsOrSysSetting('tblStockDet','InstoreAmount'));
		  var saveField=document.getElementsByName("tblGoodsOfProp_"+vars.name)[pos];
		  saveField.value=twoUnitPro.value;
	}
	#end
}
function submitForm()
{

	if(!validateDigit("InstoreQty","$text.get("iniGoods.lb.iniQty")",$globals.getFieldDigits("tblStockDet","InstoreQty"),false))return false;	
	if(!validateDigit("InstorePrice","$text.get("iniGoods.lb.iniPrice")",$globals.getFieldDigits("tblStockDet","InstorePrice"),false))return false;
	#if($existsTowUnit)
	if(!validateDigit("IniTwoQty","$text.get("iniGoods.lb.IniTwoQty")",$globals.getFieldDigits("tblStockDet","IniTwoQty"),false))return false;		
	#end
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	form.submit();
}

function openChildSelect(obj,goodsCode,thisField,flag,seqGoodsCode,displayName){ 
  
	obj.testi = "testi";	
	var id = 0;
   	var mi = document.getElementsByName(thisField); 	
   	for(i=0;i<mi.length;i++){
		if(mi[i].testi == "testi"){
			id = i;
			obj.testi = "";	
		}	
	}
	if(goodsCode=="@SEQ"){
	    if(flag!="0") return false;
		<!--如果该商品启用了序列号属性并且必须输入-->
		var defSeqStr="";
		if(defSeqArr.length>=id+1){
		    defSeqStr=defSeqArr[id];
		}
	    var str=window.showModalDialog("/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&type=addSequence&seq="+document.getElementsByName("tblGoodsOfProp_"+thisField)[id].value+"&seqFname=tblIniStockDet_"+thisField+"&goodsCode="+seqGoodsCode+"&defSeqStr="+defSeqStr,"","dialogWidth=465px;dialogHeight=360px"); 
		if(typeof(str)=="undefined"){
			return ;
		}
		if(str!=""){
			var arr=str.split(";");
		
			var hidSeqs=document.getElementsByName("tblGoodsOfProp_"+thisField);
			
			var seqList1=arr[1].split("~");
			//验证序列号是否重复








			for(i=0;i<seqList1.length;i++){
				if(seqList1[i].length>0){
					for(j=0;j<hidSeqs.length;j++){
						if(i!=id && hidSeqs[j].value.indexOf(seqList1[i]+"~")!=-1){
							alert('$text.get("seq.validate.error10")'+'['+seqList1[i]+']'+'$text.get("seq.validate.error10_1")'+'['+(j+1)+']'+'$text.get("seq.validate.error10_2")');
							return ;
						}
					}
				}	
			}
			var saveFields=document.getElementsByName("tblGoodsOfProp_"+thisField);
			saveFields[id].value=arr[1];
			document.getElementsByName(thisField)[id].value=arr[0];
			var seqQtyArr=document.getElementsByName('$!SeqQtyfName');
			seqQtyArr[id].value=arr[1].split("~").length-1;
			if(document.createEvent){
	    		var evt = document.createEvent("HTMLEvents");	 	    		
	    		evt.initEvent("change",false,false);
	    		seqQtyArr[id].dispatchEvent(evt);	
			 }else{
				seqQtyArr[id].fireEvent("onchange");
			 }
		}else{
			var saveFields=document.getElementsByName("tblGoodsOfProp_"+thisField);
			saveFields[id].value="";
			document.getElementsByName(thisField)[id].value="";
			var seqQtyArr=document.getElementsByName('$!SeqQtyfName');
			seqQtyArr[id].value=0;
			if(document.createEvent){
	    		var evt = document.createEvent("HTMLEvents");	 	    		
	    		evt.initEvent("change",false,false);
	    		seqQtyArr[id].dispatchEvent(evt);	
			 }else{
				seqQtyArr[id].fireEvent("onchange");
			 }
		}
	}else if(flag=="tblIniStockDet"){
	  	var str =window.showModalDialog("/UserFunctionAction.do?keyId=&tableName=tblIniStockDet&fieldName=SecUnit&operation=22&tblIniStockDet_GoodsCode="+goodsCode+"&MOID=36&&MOOP=add&iniPropField="+thisField,"","dialogWidth=800px;dialogHeight=450px");
	    fs=str.split(";"); 
	    mi[id].value=fs[2];
	    #if("$!globals.getSysSetting('AssUnit')"=="true")<!--启用辅助单位-->
		    document.getElementsByName("hidSecUnit")[id].value=fs[0];
		    document.getElementsByName("conversionRate")[id].value=fs[1];
		    if(document.createEvent){
	    		var evt = document.createEvent("HTMLEvents");	 	    		
	    		evt.initEvent("change",false,false);
	    		document.getElementsByName("InstoreQty")[id].dispatchEvent(evt);	
			 }else{
				document.getElementsByName("InstoreQty")[id].fireEvent("onchange");
			 }
	    #end
	}else if(flag=="provider"){//供应商弹出框
		var str =window.showModalDialog("/UserFunctionAction.do?keyId=&tableName=tblIniStockDet&fieldName=CompanyCode&operation=22&MOID=36&&MOOP=add","","dialogWidth=800px;dialogHeight=450px");
		fs=str.split(";");
		obj.value=fs[1];
		document.getElementsByName("hidProvider")[id].value=fs[0];
	}else{
		var fieldDisplay = encodeURI(displayName) ;
		var str  = window.showModalDialog("/UserFunctionAction.do?keyId=&tableName=tblGoodsOfProp&selectName=SelectProp&displayName="+fieldDisplay+"&operation=22&GoodsCode="+goodsCode+"&MOID=36&&MOOP=add&&LinkType=@URL:&iniPropField="+thisField,"","dialogWidth=800px;dialogHeight=450px"); 
		if(str!=""){
		   fs=str.split(";"); 
		   var dis=document.getElementsByName(thisField+"NV");   
		   if(dis.length!=0){
				mi[id].value=fs[0];
				var saveFields=document.getElementsByName("tblGoodsOfProp_"+thisField);
				saveFields[id].value=fs[0];
				dis[id].value=fs[1];
		   }else{
				mi[id].value=fs[1];
				var saveFields=document.getElementsByName("tblGoodsOfProp_"+thisField);
				saveFields[id].value=fs[0];
		   }
		}
	}
	if(obj.name=='setUnit'){
		iniValueChange(obj);
	}
	return 0;
}
function iniPropValueChange(vars){
	var mi =document.getElementsByName(vars.name);
	var pos;
	for(i=0;i<mi.length;i++){
		if(mi[i] == vars){
			pos = i;	
		}	
	}
	var prop=document.getElementsByName("tblGoodsOfProp_"+vars.name)[pos];
	prop.value=vars.value;
}
</script>
</head>

<body onLoad="showStatus();getDefaultSeq();">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" target="formFrame" action="/IniGoodsQueryAction.do">
 <input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"> 
 <input type="hidden" name="type" value="edit">
 <input type="hidden" name="classCode" value="$!goodsCode">
   <input type="hidden" name="stockName" value="$!stockName">
 <input type="hidden" name="seqPropfName" value="$!seqPropfName">
  <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("iniGoods.lb.title")</div>
	<ul class="HeadingButton">
	#if($globals.getMOperation().update())
	<li><button type="button"  onClick="submitForm()" class="b2">$text.get("common.lb.save")</button></li>	
	#end
	#if($globals.getMOperation().query())
	#set($strGoodsCode=$!goodsCode+"00001")
	<li><button type="button" onClick="location.href='/IniGoodsQueryAction.do?type=back&goodsCode=$!strGoodsCode&stockName=$!stockName&winCurIndex=$!winCurIndex'" class="b2">$text.get("common.lb.back")</button></li>	
	#end
	</ul>
</div>
<div id="listRange_id">
	  <div class="scroll_function_small_a" id="conter">
	  <script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-52;
oDiv.style.height=sHeight+"px";
</script>
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table" id="tblSort">
			<thead>
				<tr>
					<td width="45" align="center">$text.get("iniAcc.lb.iniAcc.lb.sequencenumber")</td>
					<td width="100" align="center">$text.get("iniGoods.lb.goodsNo")</td>
					<td width="100" align="center">$text.get("iniGoods.lb.goodsName")</td>
					<td width="100" align="center">$text.get("iniGoods.lb.goodsSpec")</td>
					<td width="100" align="center">$text.get("iniGoods.lb.stock")</td>
					#if("$!globals.getSysSetting('StockLocation')"=="true")
					<td width="100" align="center">$text.get("iniGoods.lb.StockLocation")</td>
					#end
					#foreach($item in $existsPropNames)
					 <td width="100" align="center">$globals.get($item,1) </td>
					   #foreach($nvItem in $existNVPropDis)
					     #if($globals.get($item,0)==$globals.get($nvItem,0))
						 <td width="100" align="center">$globals.get($nvItem,2) </td>
						 #end
					   #end
					#end
					#if($globals.hasUsedSeq('tblStockDet'))
					 <td width="100" align="center">$text.get("iniGoods.lb.scompany") </td>					
					#end
					#if($existsTowUnit)
					<td width="80" align="center">$text.get("iniGoods.lb.IniTwoQty")</td>
                    #end
					<td width="80" align="center">$text.get("iniGoods.lb.iniQty")</td>
					<td width="80" align="center">$text.get("iniGoods.lb.iniPrice")</td>
					<td width="80" align="center">$text.get("iniGoods.lb.iniAmount")</td>	
					#if("$!globals.getSysSetting('AssUnit')"=="true")<!--启用辅助单位-->
					<td width="80" align="center">$text.get("iniGoods.lb.secUnit")</td>	
					<td width="80" align="center">$text.get("iniGoods.lb.secQty")</td>	
					<td width="80" align="center">$text.get("iniGoods.lb.secPrice")</td>	
					#end
				</tr>			
			</thead>
			<tbody> #set($count=0)
			        #set($rowC=0)
					#foreach ($row in $props )
					 #set($count=$count+1)
				<tr align="left" onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td align="left">$!count</td>
				<td align="left">$!globals.get($row,2)</td>
				<td align="left">$!globals.get($row,3)</td>
				<td align="left">$!globals.get($row,9) &nbsp;</td>
				<td align="left">$!globals.get($row,4) </td>
				#if("$!globals.getSysSetting('StockLocation')"=="true")
				<td align="left"><input type="text" name="StockLocation" value="$!globals.get($row,10)" onKeyDown="if(event.keyCode==13)event.keyCode=9;"></td>
				#end
				#set($num=1)
				#set($fg=$count+(-1))
				#foreach($item in $existsPropNames)
				#set($i=$num+10)
				
				<td align="left">
				#set($fi=$globals.getFieldBean('tblIniStockDet',$globals.get($item,0)))
				#if($fi.inputType=="2")
					<input type="hidden" name="tblGoodsOfProp_$!globals.get($item,0)" value="$!globals.get($!globals.get($row,$i).split('@#'),0)">
					#if($globals.getPropBean($!globals.get($item,0)).isSequence=="1")<!--如果是序列号属性-->
					  #if($!globals.get($seqFlag,$rowC)=="0")
					 	<input style="width:80px; overflow:hidden" type="text" onDblClick="openChildSelect(document.getElementsByName('$!globals.get($item,0)')[this.parentNode.parentNode.rowIndex-1],'@SEQ','$!globals.get($item,0)','$!globals.get($seqFlag,$fg)','$globals.get($row,1)','$globals.get($item,1)');" onKeyDown="if(event.keyCode==13) {addSeq(this,'$globals.get($row,1)');select();}" value="$!globals.getSeqDis($!globals.get($row,$i))"  name="$!globals.get($item,0)"  onfocus="select()"/><img src="/$globals.getStylePath()/images/St.gif" class="search" onClick="openChildSelect(document.getElementsByName('$!globals.get($item,0)')[this.parentNode.parentNode.rowIndex-1],'@SEQ','$!globals.get($item,0)','$!globals.get($seqFlag,$fg)','$globals.get($row,1)');"/></td>
					  #else
					    <input type="text" value=""  name="$!globals.get($item,0)"  readonly="readonly"/></td>
					  #end
					#else						 
						 #set($nvRowC=0)
						 #foreach($nvItem in $nvPropDis)						 	
						    #if($nvRowC==$rowC)
						    #if($nvItem.size()==0)
						 		<input  type="text" #if($globals.getPropBean($globals.get($item,0)).isUsed=="1") onDblClick="openChildSelect(this,'$globals.get($row,1)','$!globals.get($item,0)','','','$globals.get($item,1)');"  onKeyDown="if(event.keyCode==13) openChildSelect(this,'$globals.get($row,1)','$!globals.get($item,0)','','','$globals.get($item,1)');" #else onKeyDown="if(event.keyCode==13)event.keyCode=9" #end value="$!globals.get($!globals.get($row,$i).split('@#'),1)"  name="$!globals.get($item,0)" #if($globals.get($item,2)!=1) readonly="true" #end #if($globals.get($item,3)==1) onChange="iniValueChange(this);" #else  onChange="iniPropValueChange(this)" #end /></td>						 		
						 	#else						    
						     #foreach($arr in $nvItem)
							   	#if($globals.get($arr,0)==$globals.get($item,0))
							   		<input  type="text" #if($globals.getPropBean($globals.get($item,0)).isUsed=="1") onDblClick="openChildSelect(this,'$globals.get($row,1)','$!globals.get($item,0)','','','$globals.get($item,1)');"  onKeyDown="if(event.keyCode==13) openChildSelect(this,'$globals.get($row,1)','$!globals.get($item,0)','','','$globals.get($item,1)');" #else onKeyDown="if(event.keyCode==13)event.keyCode=9" #end value="$!globals.get($!globals.get($row,$i).split('@#'),0)"  name="$!globals.get($item,0)" #if($globals.get($item,2)!=1) readonly="true" #end #if($globals.get($item,3)==1) onChange="iniValueChange(this);" #else  onChange="iniPropValueChange(this)" #end /></td>
									<td><input name="$globals.get($item,0)NV" type="text" onKeyDown="if(event.keyCode==13) event.keyCode=9" value="$!globals.get($globals.get($arr,1).split('@#'),1)"></td>
								#else
									<input  type="text" #if($globals.getPropBean($globals.get($item,0)).isUsed=="1") onDblClick="openChildSelect(this,'$globals.get($row,1)','$!globals.get($item,0)','','','$globals.get($item,1)');"  onKeyDown="if(event.keyCode==13) openChildSelect(this,'$globals.get($row,1)','$!globals.get($item,0)','','','$globals.get($item,1)');" #else onKeyDown="if(event.keyCode==13)event.keyCode=9" #end value="$!globals.get($!globals.get($row,$i).split('@#'),1)"  name="$!globals.get($item,0)" #if($globals.get($item,2)!=1) readonly="true" #end #if($globals.get($item,3)==1) onChange="iniValueChange(this);" #else  onChange="iniPropValueChange(this)" #end /></td>
								#end				 
 							 #end
							#end
							#end
							 #set($nvRowC=$nvRowC+1)
							
						 #end
					 #end
				 #elseif($fi.inputType=="0")
                    #if("$fi.fieldType" == "5")
					<input name="tblGoodsOfProp_$!globals.get($item,0)" type="text" onKeyDown="if(event.keyCode==13){ if(this.value.length>0){ event.keyCode=9; }else{  openInputDate(this);}}" onClick="openInputDate(this);"  value="$!globals.get($!globals.get($row,$i).split('@#'),0)"></td>
					#elseif("$fi.fieldType" == "6")
					<input name="tblGoodsOfProp_$!globals.get($item,0)" type="text" onKeyDown="if(event.keyCode==13){ if(this.value.length>0){ event.keyCode=9; }else{  openInputTime(this);}}" onClick="openInputTime(this);" value="$!globals.get($!globals.get($row,$i).split('@#'),0)"></td>
					#elseif("$fi.fieldType"=="2")
					<input name="tblGoodsOfProp_$!globals.get($item,0)" type="text" onKeyDown="if(event.keyCode==13) event.keyCode=9" value="$!globals.get($!globals.get($row,$i).split('@#'),0)"></td>
					#end
				 #end
			    #set($num=$num+1)
				#end
				#if($existsTowUnit)
				 #if("$!globals.getSysSetting('AssUnit')"=="true")<!--启用辅助单位-->
				   #set($j=$colLength+(-6))
				  #else
				   #set($j=$colLength+(-1))
				  #end
					<td><input type="text" onKeyDown="if(event.keyCode==13)event.keyCode=9;"  onChange="iniValueChange(this);" value="$!globals.get($row,$j)"  name="IniTwoQty"></td>
                 #end
				#if($globals.hasUsedSeq('tblStockDet'))<!--启用了序列号则显示供应商字段-->
				   #if($!globals.get($seqFlag,$rowC)=="0")
					   #set($provC=0)
					   #foreach($prov in $providorList)
						  #if($provC==$rowC)
							<td><input type="hidden" name="hidProvider" value="$globals.get($prov,0)"><input type="text" name="provider" value="$globals.get($prov,1)" onDblClick="openChildSelect(this,'','provider','provider','');" onKeyDown="if(event.keyCode==13)openChildSelect(this,'','provider','provider','');"></td>
						  #end
						  #set($provC=$provC+1)
					   #end
				   #else
				     <td><input type="hidden" name="hidProvider" value=""><input type="text" name="provider" readonly="readonly"></td>
				   #end
				#end
				<td>
				<input type="hidden" name="keyId" value="$!globals.get($row,0)">
				<input type="hidden" name="goodsCode" value="$!globals.get($row,1)">
				<input type="hidden" name="stockCode" value="$!globals.get($row,8)">
				<input type="text" onKeyDown="if(event.keyCode==13)event.keyCode=9;" onDblClick="select()"
  value="$!globals.get($row,5)"  onBlur="iniValueChange(this);"  name="InstoreQty" #if($!globals.get($seqFlag,$rowC)=="0") readonly="readonly" #end></td>
				<td><input type="text" onDblClick="select()"
 onKeyDown="if(event.keyCode==13)event.keyCode=9;"  onBlur="iniValueChange(this);" value="$!globals.get($row,6)"  name="InstorePrice"></td>
				<td><input type="text" onKeyDown="if(event.keyCode==13)event.keyCode=9;"  onBlur="iniValueChange(this);" value="$!globals.get($row,7)"  name="InstoreAmount" readonly="readonly"></td>
				#if("$!globals.getSysSetting('AssUnit')"=="true")<!--启用辅助单位-->
								<td>
								#set($k=$colLength+(-5))
								<input type="hidden" name="hidSecUnit" value="$!globals.get($row,$k)"/>
								#set($k=$colLength+(-4))
								<input type="text" onDblClick="openChildSelect(this,'$globals.get($row,1)','setUnit','tblIniStockDet','')"
 onKeyDown="if(event.keyCode==13)event.keyCode=9;"  onChange="iniValueChange(this);" value="$!globals.get($row,$k)"  name="setUnit">
                                 #set($k=$colLength+(-3))
                                <input type="hidden" name="conversionRate" value="$!globals.get($row,$k)">
 </td>                           
 		      <td>
			    #set($k=$colLength+(-2))
				<input type="text" onKeyDown="if(event.keyCode==13)event.keyCode=9;" onDblClick="select()"
   value="$!globals.get($row,$k)"  onBlur="iniValueChange(this);"  name="secUnitQty" #if($!globals.get($seqFlag,$rowC)=="0") readonly="readonly" #end></td>
				<td>
				  #set($k=$colLength+(-1))
				<input type="text" onDblClick="select()"
 onKeyDown="if(event.keyCode==13)event.keyCode=9;"   onBlur="iniValueChange(this);" value="$!globals.get($row,$k)"  name="secUnitPrice"></td>
				#end				
				</tr>
				   #set($rowC=$rowC+1)
				#end
					</tbody>
		</table>	
	</div>
</div>
</form>
</body>
</html>

