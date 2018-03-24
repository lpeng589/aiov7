<html><head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("unitadmin.title.update")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<!--
 <script language="javascript" src="/js/date.vjs"></script> 
-->
<script language="javascript" src="/js/longTime.vjs"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script language="javascript" src="/js/editGrid.js"></script>
<script language="javascript">
#if($globals.hasUsedSeq('tblStockDet'))
function addSeq(o){
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

		var urls="/UtilServlet?operation=validateSeq&tableName=tblIniStockDet&validateField="+o.name+"&goodsCode=$!goodsCode&oldSeq="+seqStr+"&defSeqStr="+defSeqStr+"&seqStr="+seqStr+o.value+"~"+"&lastSeq="+lastSeq;
		AjaxRequest(urls) ;
		if(response!="" && response!="no response text"){
			alert(response);
			return false;
		}else{
		  	if(o.alt.length>0){
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
}
#end
var digits=$globals.getDigits();
var digitsDet=$globals.getDetDigits();
var defSeqArr=new Array();//保存修改加载后的最初序列号字符串







childData[0]='';
var minValue=-999999999
var maxValue=999999999
putChildValidateItem("iniQty","$text.get("iniGoods.lb.iniQty")","double","",false,minValue,maxValue,false);
putChildValidateItem("iniPrice","$text.get("iniGoods.lb.iniPrice")","double","",false,minValue,maxValue,false);
putChildValidateItem("iniAmount","$text.get("iniGoods.lb.iniAmount")","double","",false,minValue,maxValue,false);
function getDefaultSeq(){
    var seqFname=document.getElementById("SeqPropfName").value;
    var objs=document.getElementsByName('tblGoodsOfProp_'+seqFname); 
	for(var i=0;i<objs.length;i++){
		defSeqArr[i]=objs[i].value;
	}
}

function beforSubmit(){
	if(!validate(form))return false;
	#if($!existsPropNames.size()>0)
	if(!checkProp())return false;
    #end
	if(!checkIniData())return false;
	if(!validateDigit("iniQty","$text.get("iniGoods.lb.iniQty")",$!globals.getDigitsOrSysSetting("tblStockDet","IniQty"),true))return false;	
	if(!validateDigit("iniPrice","$text.get("iniGoods.lb.iniPrice")",$!globals.getDigitsOrSysSetting("tblStockDet","IniPrice"),true))return false;	
	//if(!sureSubmit())return false;	
	disableForm(form);
	form.submit();
}	


var editgriddata = new gridData();
var tableId="edittable";
var selArr=new Array();//保存是弹出框属性的索引
var existSeq=false;
var num=0;
#foreach($item in $!existsPropNames)
	#set($fi=$globals.getFieldBean('tblIniStockDet',$globals.get($item,0)))
	num=num+1;
	#if($fi.inputType=="2")
		addCols(editgriddata,'tblGoodsOfProp_$globals.get($item,0)','$globals.get($item,1)','100',2,true,'',0,-2,'');
		#if($globals.getPropBean($globals.get($item,0)).isSequence=="1")//如果是序列号属性







			addCols(editgriddata,'$globals.get($item,0)','$globals.get($item,1)','100','$globals.get($item,2)',true,'','',2,'@SEQ');
			function setSeqFname(){
				document.getElementById("SeqPropfName").value='$globals.get($item,0)';
				var seqQty=document.getElementsByName("$!SeqQtyfName");
				for(i=0;i<seqQty.length;i++){
					seqQty[i].readOnly="readOnly";
					document.getElementsByName('$globals.get($item,0)')[i].alt= " ";
				}
			}//设置保存序列号字段名
			selArr[selArr.length]=num-1;
			existSeq=true;
		#else
			addCols(editgriddata,'$globals.get($item,0)','$globals.get($item,1)','100','$globals.get($item,2)',true,'',"tblGoodsProp",2,   'tblGoodsOfProp::$globals.get($item,0):GoodsCode');
			selArr[selArr.length]=num-1;
	 		//支持名称&值的名称字段
   		    #foreach($nvItem in $!nvPropNames)
	 			#if("$globals.get($item,0)"=="$globals.get($nvItem,0)")
	  				#set($nvfi=$globals.getFieldBean('tblStockDet',$globals.get($nvItem,1)))
	    			#set($nullable = "false")
					#if("$nvfi.isNull" == "0")
		  				#set($nullable = "true")
					#end
	  				addCols(editgriddata,'$globals.get($nvItem,1)','$globals.get($nvItem,2)','$nvfi.width',$nvfi.fieldType,$nullable,'$!nvfi.getDefValue()',$nvfi.maxLength,$nvfi.inputType,'');	
	  				num=num+1;
      			#end
			#end
	   #end
	#else if($fi.inputType=="0")
		#set($nullable = "false")
		#if("$fi.isNull" == "0")
  			#set($nullable = "true")
		#end
		addCols(editgriddata,'tblGoodsOfProp_$fi.fieldName','$fi.display.get("$globals.getLocale()")','$fi.width',$fi.fieldType,$nullable,'$fi.getDefValue()',$fi.maxLength,$fi.inputType,'');	
    	//支持名称&值的名称字段
    	#foreach($nvItem in $!nvPropNames)
	 		#if("$globals.get($item,0)"=="$globals.get($nvItem,0)")
	  			#set($nvfi=$globals.getFieldBean('tblStockDet',$globals.get($nvItem,1)))
	    		#set($nullable = "false")
				#if("$nvfi.isNull" == "0")
		  			#set($nullable = "true")
				#end
	 			addCols(editgriddata,'$globals.get($nvItem,1)','$globals.get($nvItem,2)','$nvfi.width',$nvfi.fieldType,$nullable,'$nvfi.getDefValue()',$nvfi.maxLength,$nvfi.inputType,'');	
      		#end
		#end
	#end
#end
if(existSeq){//如果存在序列号，显示供应商字段







	addCols(editgriddata,'hidProvider','$text.get("iniGoods.lb.scompany")','100',2,true,'',0,-2,'');
	addCols(editgriddata,'provider','$text.get("iniGoods.lb.scompany")','100',1,true,'',0,2,'tblIniStockDet:CompanyCode:provider:GoodsCode');
}
#if("$!globals.getSysSetting('StockLocation')"=="true")
	addCols(editgriddata,'StockLocation','$text.get("iniGoods.lb.StockLocation")','100',1,false,'',0,0,'');
#end
#if($existsTowUnit)
	addCols(editgriddata,'IniTwoQty','$text.get("iniGoods.lb.IniTwoQty")','100',1,false,'0',0,0,'');
#end

addCols(editgriddata,'iniQty','$text.get("iniGoods.lb.iniQty")','100',1,false,'0',0,0,'');
addCols(editgriddata,'iniPrice','$text.get("iniGoods.lb.iniPrice")','100',1,false,'0',0,0,'');
addCols(editgriddata,'iniAmount','$text.get("iniGoods.lb.iniAmount")','100',1,false,'0',0,0,'');
#if("$!globals.getSysSetting('AssUnit')"=="true")//启用辅助单位
addCols(editgriddata,'hidSecUnit','$text.get("iniGoods.lb.secUnit")','100',2,true,'',0,-2,'');
addCols(editgriddata,'secUnit','$text.get("iniGoods.lb.secUnit")','100',1,true,'',0,2,'tblIniStockDet:SecUnit:secUnit:GoodsCode');
addCols(editgriddata,'conversionRate','$text.get("iniGoods.lb.secUnit")','100',2,true,'0',0,-2,'');
addCols(editgriddata,'secUnitQty','$text.get("iniGoods.lb.secQty")','100',1,false,'0',0,0,'');
addCols(editgriddata,'secUnitPrice','$text.get("iniGoods.lb.secPrice")','100',1,false,'0',0,0,'');
#end

#foreach($row in $!props)
  #set($col=0)
  var row=" addRows(editgriddata";
  #set($propLen=$!existsPropNames.size()+$!nvPropNames.size())
  #foreach($field in $row )
	 #if($col>=$propLen)
	    row+=",'$!field'";
	 #else
	    var isTrue=false;
	    for(var i=0;i<selArr.length;i++){
	      if(selArr[i]=='$col'){
		     isTrue=true;
		  }
	    }
	    if(isTrue){
	   		row+=",'$!field.get(0)','$!field.get(1)'";
	    }else{
	    	row+=",'$!field.get(0)'";
	    }
	 #end
	 #set($col=$col+1)
  #end
   row+=")";
   eval(row);
#end
function defAddOne(){
   
}
function openInputDate(obj)
{
	c.showMoreDay = false;
	c.show(obj);
}
function openInputTime(obj)
{
	setday(obj); 
}
function checkIniData(){
   var iniQty=document.getElementsByName('iniQty');
   /*
   if($!existsPropNames.size()==0&&iniQty.length>1)
   {
    alert("$text.get('goodsIni.data.more')");return false;
   }*/
		for(i = 0;i<iniQty.length;i++){
			
			if(!isFloat(iniQty[i].value)){
				alert(iniQty[i].title+"$text.get('common.validate.error.number')")
				focusItem(iniQty[i]);
				return false;
			}
		}
	var iniPrice=document.getElementsByName('iniPrice');
		for(i = 0;i<iniPrice.length;i++){
			if(!isFloat(iniPrice[i].value)){
				alert(iniPrice[i].title+"$text.get('common.validate.error.number')")
				focusItem(iniPrice[i]);
				return false;
			}
		}
	var iniAmount=document.getElementsByName('iniAmount');
		for(i = 0;i<iniAmount.length;i++){
			if(!isFloat(iniAmount[i].value)){
				alert(iniAmount[i].title+"$text.get('common.validate.error.number')")
				focusItem(iniAmount[i]);
				return false;
			}
		}
		return true;
}
function checkProp(){
  var arr=new Array($!existsPropNames.size());
  var col=0;
  #foreach($item in $!existsPropNames)
   var str="obj_'$globals.get($item,0)'";
    var str=document.getElementsByName('$globals.get($item,0)');
	arr[col]=str;
	col+=1;
  #end
  var eq=0;
   for(i=0;i<arr.length;i++)
   {
     var props=arr[i];
	 var ieq=true;
   	 for(j=0;j<props.length-1;j++)
	 {
	  if(props[j].value!=props[j+1].value)
	  {
	  ieq=false;
	  }
	 }
	 if(!ieq)
	 {
	   break;
	 }
	 eq+=1;
   }
  
   //if(eq==arr.length&&arr[0].length>1)
   //{
  // alert("$text.get('goodsIni.data.eq')");return false;
  // }
   return true;
  
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
function initCalculate (){
#if("$!globals.getSysSetting('AssUnit')"=="true")//启用辅助单位
 var secUnitQty=document.getElementsByName('secUnitQty');
		for(i = 0;i<secUnitQty.length;i++){
			secUnitQty[i].onchange=function (){
			
						this.testi = "testi";	
				var pos = 0;	
				var mi = document.getElementsByName('secUnitQty'); 
				for(i=0;i<mi.length;i++){
					if(mi[i].testi == "testi"){
						pos = i;
						this.testi = "";	
					}	
				}	
				if(isNaN(secUnitQty[pos].value)||secUnitQty[pos].value.length==0){secUnitQty[pos].value=0;}	
				if(secUnitQty[pos].value<0){secUnitQty[pos].value=0;}	
					document.getElementsByName('iniAmount')[pos].value=document.getElementsByName('secUnitPrice')[pos].value*document.getElementsByName('secUnitQty')[pos].value;
 				if(!isFloat(document.getElementsByName('iniAmount')[pos].value)) document.getElementsByName('iniAmount')[pos].value='0';
				else document.getElementsByName('iniAmount')[pos].value=f(document.getElementsByName('iniAmount')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniTwoQty"));
				var conv=document.getElementsByName('conversionRate')[pos].value;
   				if(conv.indexOf("/")>0){
  					 document.getElementsByName('iniQty')[pos].value=document.getElementsByName('secUnitQty')[pos].value/conv.substr(conv.indexOf("/")+1)*conv.substr(0,conv.indexOf("/"));
 				}else{
					document.getElementsByName('iniQty')[pos].value=document.getElementsByName('secUnitQty')[pos].value*document.getElementsByName('conversionRate')[pos].value;
 				}
				if(document.getElementsByName('iniQty')[pos].value!='0'){
					document.getElementsByName('iniQty')[pos].value=f(document.getElementsByName('iniQty')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","iniQty"))
   					document.getElementsByName('iniPrice')[pos].value=document.getElementsByName('iniAmount')[pos].value/document.getElementsByName('iniQty')[pos].value;
   					if(!isFloat(document.getElementsByName('iniPrice')[pos].value)) document.getElementsByName('iniPrice')[pos].value='0';
 					else document.getElementsByName('iniPrice')[pos].value=f(document.getElementsByName('iniPrice')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniPrice"));
 				}else{
 					document.getElementsByName('iniPrice')[pos].value='0'
 				}
			execStat();
			};
		}
#end

#if("$!globals.getSysSetting('AssUnit')"=="true")//启用辅助单位
 var secUnitPrice=document.getElementsByName('secUnitPrice');
		for(i = 0;i<secUnitPrice.length;i++){
			secUnitPrice[i].onchange=function (){
			
						this.testi = "testi";	
				var pos = 0;	
				var mi = document.getElementsByName('secUnitPrice'); 
				for(i=0;i<mi.length;i++){
					if(mi[i].testi == "testi"){
						pos = i;
						this.testi = "";	
					}	
				}	
				if(isNaN(secUnitPrice[pos].value)||secUnitPrice[pos].value.length==0){secUnitPrice[pos].value=0;}
				if(secUnitPrice[pos].value<0){secUnitPrice[pos].value=0;}		
document.getElementsByName('iniAmount')[pos].value=document.getElementsByName('secUnitPrice')[pos].value*document.getElementsByName('secUnitQty')[pos].value;
 if(!isFloat(document.getElementsByName('iniAmount')[pos].value)) document.getElementsByName('iniAmount')[pos].value='0';
 else document.getElementsByName('iniAmount')[pos].value=f(document.getElementsByName('iniAmount')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniTwoQty"));
 if(document.getElementsByName('iniQty')[pos].value!='0'){
   document.getElementsByName('iniPrice')[pos].value=document.getElementsByName('iniAmount')[pos].value/document.getElementsByName('iniQty')[pos].value;
 }
  if(!isFloat(document.getElementsByName('iniPrice')[pos].value)) document.getElementsByName('iniPrice')[pos].value='0';
 else document.getElementsByName('iniPrice')[pos].value=f(document.getElementsByName('iniPrice')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniPrice"));
			execStat();
			};
		}
#end

 var iniQty=document.getElementsByName('iniQty'); 
		for(i = 0;i<iniQty.length;i++){
			iniQty[i].onchange=function (){			
				this.testi = "testi";	
				var pos = 0;	
				var mi = document.getElementsByName('iniQty'); 
				for(i=0;i<mi.length;i++){
					if(mi[i].testi == "testi"){
						pos = i;
						this.testi = "";	
					}	
				} 		
if(isNaN(iniQty[pos].value)||iniQty[pos].value.length==0){iniQty[pos].value=0;}
if(iniQty[pos].value<0){iniQty[pos].value=0;}
document.getElementsByName('iniAmount')[pos].value=document.getElementsByName('iniPrice')[pos].value*document.getElementsByName('iniQty')[pos].value;
 if(!isFloat(document.getElementsByName('iniAmount')[pos].value)) document.getElementsByName('iniAmount')[pos].value='0';
 else document.getElementsByName('iniAmount')[pos].value=f(document.getElementsByName('iniAmount')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniAmount"));
  if(!isFloat(document.getElementsByName('iniQty')[pos].value)) document.getElementsByName('iniQty')[pos].value='0';
  
 else{ 
 document.getElementsByName('iniQty')[pos].value=f(document.getElementsByName('iniQty')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniQty"));}

#if("$!globals.getSysSetting('AssUnit')"=="true")//启用辅助单位
if(document.getElementsByName('conversionRate')[pos].value.length==0){document.getElementsByName('conversionRate')[pos].value='0';}
if(document.getElementsByName('conversionRate')[pos].value=='0'){
document.getElementsByName('secUnitQty')[pos].value='0';
document.getElementsByName('secUnitPrice')[pos].value='0';
}else{
   var conv=document.getElementsByName('conversionRate')[pos].value;
   if(conv.indexOf("/")>0){
   document.getElementsByName('secUnitQty')[pos].value=document.getElementsByName('iniQty')[pos].value*conv.substr(conv.indexOf("/")+1)/conv.substr(0,conv.indexOf("/"));
   }else{
document.getElementsByName('secUnitQty')[pos].value=document.getElementsByName('iniQty')[pos].value/document.getElementsByName('conversionRate')[pos].value;
   }

  if(!isFloat(document.getElementsByName('secUnitQty')[pos].value)) document.getElementsByName('secUnitQty')[pos].value='0';
 else document.getElementsByName('secUnitQty')[pos].value=f(document.getElementsByName('secUnitQty')[pos].value,$!globals.getDigitsOrSysSetting("tblIniStockDet","secUnitQty"));
document.getElementsByName('secUnitPrice')[pos].value=document.getElementsByName('iniAmount')[pos].value/document.getElementsByName('secUnitQty')[pos].value;
   if(!isFloat(document.getElementsByName('secUnitPrice')[pos].value)) document.getElementsByName('secUnitPrice')[pos].value='0';
 else document.getElementsByName('secUnitPrice')[pos].value=f(document.getElementsByName('secUnitPrice')[pos].value,$!globals.getDigitsOrSysSetting("tblIniStockDet","secUnitPrice"));
}
#end
execStat();
#if($existsTowUnit)
 if("$!twoUnitPro"!=""){
document.getElementsByName('IniTwoQty')[pos].value=document.getElementsByName('iniQty')[pos].value/document.getElementsByName('$!twoUnitPro')[pos].value;
 if(!isFloat(document.getElementsByName('IniTwoQty')[pos].value)) document.getElementsByName('IniTwoQty')[pos].value='0';
 else document.getElementsByName('IniTwoQty')[pos].value=f(document.getElementsByName('IniTwoQty')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniTwoQty"));
  }
#end

			};
		}
	var iniPrice=document.getElementsByName('iniPrice');
		for(i = 0;i<iniPrice.length;i++){
			iniPrice[i].onchange=function (){
			
						this.testi = "testi";	
				var pos = 0;	
				var mi = document.getElementsByName('iniPrice'); 
				for(i=0;i<mi.length;i++){
					if(mi[i].testi == "testi"){
						pos = i;
						this.testi = "";	
					}	
				}	
				if(isNaN(iniPrice[pos].value)||iniPrice[pos].value.length==0){iniPrice[pos].value=0;}
				if(iniPrice[pos].value<0){iniPrice[pos].value=0;}		
document.getElementsByName('iniAmount')[pos].value=document.getElementsByName('iniPrice')[pos].value*document.getElementsByName('iniQty')[pos].value;
 if(!isFloat(document.getElementsByName('iniAmount')[pos].value)) document.getElementsByName('iniAmount')[pos].value='0';
 else document.getElementsByName('iniAmount')[pos].value=f(document.getElementsByName('iniAmount')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniTwoQty"));
  if(!isFloat(document.getElementsByName('iniPrice')[pos].value)) document.getElementsByName('iniPrice')[pos].value='0';
 else document.getElementsByName('iniPrice')[pos].value=f(document.getElementsByName('iniPrice')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniPrice"));
 #if("$!globals.getSysSetting('AssUnit')"=="true")//启用辅助单位
if(document.getElementsByName('conversionRate')[pos].value.length==0){document.getElementsByName('conversionRate')[pos].value='0';}
//document.getElementsByName('secUnitQty')[pos].value=document.getElementsByName('iniQty')[pos].value/document.getElementsByName('conversionRate')[pos].value;
if(document.getElementsByName('secUnitQty')[pos].value=='0'){
 document.getElementsByName('secUnitPrice')[pos].value='0';
}else{
document.getElementsByName('secUnitPrice')[pos].value=document.getElementsByName('iniAmount')[pos].value/document.getElementsByName('secUnitQty')[pos].value;
}
   if(!isFloat(document.getElementsByName('secUnitPrice')[pos].value)) document.getElementsByName('secUnitPrice')[pos].value='0';
 else document.getElementsByName('secUnitPrice')[pos].value=f(document.getElementsByName('secUnitPrice')[pos].value,$!globals.getDigitsOrSysSetting("tblIniStockDet","secUnitPrice"));
#end
			execStat();
			};
		}
	var iniAmount=document.getElementsByName('iniAmount');
		for(i = 0;i<iniAmount.length;i++){
			iniAmount[i].onchange=function (){
			
						this.testi = "testi";	
				var pos = 0;	
				var mi = document.getElementsByName('iniAmount'); 
				for(i=0;i<mi.length;i++){
					if(mi[i].testi == "testi"){
						pos = i;
						this.testi = "";	
					}	
				}		
				if(isNaN(iniAmount[pos].value)||iniAmount[pos].value.length==0){iniAmount[pos].value=0;}	
document.getElementsByName('iniPrice')[pos].value=document.getElementsByName('iniAmount')[pos].value/document.getElementsByName('iniQty')[pos].value;
 if(!isFloat(document.getElementsByName('iniPrice')[pos].value)) document.getElementsByName('iniPrice')[pos].value='0';
 else document.getElementsByName('iniPrice')[pos].value=f(document.getElementsByName('iniPrice')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniPrice"));
 if(!isFloat(document.getElementsByName('iniAmount')[pos].value)) document.getElementsByName('iniAmount')[pos].value='0';
 else document.getElementsByName('iniAmount')[pos].value=f(document.getElementsByName('iniAmount')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniAmount"));
 #if("$!globals.getSysSetting('AssUnit')"=="true")//启用辅助单位
  if(document.getElementsByName('secUnitQty')[pos].value!='0'){
   document.getElementsByName('secUnitPrice')[pos].value=document.getElementsByName('iniAmount')[pos].value/document.getElementsByName('secUnitQty')[pos].value;
      if(!isFloat(document.getElementsByName('secUnitPrice')[pos].value)) document.getElementsByName('secUnitPrice')[pos].value='0';
 else document.getElementsByName('secUnitPrice')[pos].value=f(document.getElementsByName('secUnitPrice')[pos].value,$!globals.getDigitsOrSysSetting("tblIniStockDet","secUnitPrice"));
  }
 #end
execStat();
			};
		}
	#if($existsTowUnit)
		var iniAmount=document.getElementsByName('$twoUnitPro');
		for(i = 0;i<iniAmount.length;i++){
			iniAmount[i].onchange=function (){
			
						this.testi = "testi";	
				var pos = 0;	
				var mi = document.getElementsByName('$twoUnitPro'); 
				for(i=0;i<mi.length;i++){
					if(mi[i].testi == "testi"){
						pos = i;
						this.testi = "";	
					}	
				}		
				#if($twoUnitBillInput=="1")
					iniPropValueChange(document.getElementsByName('$twoUnitPro')[pos]);
				#end	
				if(isNaN(iniAmount[pos].value)||iniAmount[pos].value.length==0){iniAmount[pos].value=0;}	
document.getElementsByName('iniQty')[pos].value=document.getElementsByName('$twoUnitPro')[pos].value*document.getElementsByName('IniTwoQty')[pos].value;	
if(!isFloat(document.getElementsByName('iniQty')[pos].value)) document.getElementsByName('iniQty')[pos].value='0';
else document.getElementsByName('iniQty')[pos].value=f(document.getElementsByName('iniQty')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniQty"));

document.getElementsByName('iniAmount')[pos].value=document.getElementsByName('iniPrice')[pos].value*document.getElementsByName('iniQty')[pos].value;
 if(!isFloat(document.getElementsByName('iniAmount')[pos].value)) document.getElementsByName('iniAmount')[pos].value='0';
 else document.getElementsByName('iniAmount')[pos].value=f(document.getElementsByName('iniAmount')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniAmount"));
  #if("$!globals.getSysSetting('AssUnit')"=="true")//启用辅助单位
  if(document.getElementsByName('secUnitQty')[pos].value!='0'){
    document.getElementsByName('secUnitPrice')[pos].value=document.getElementsByName('iniAmount')[pos].value/document.getElementsByName('secUnitQty')[pos].value;
  }
 #end
execStat();
			};
		}
		
		var iniAmount=document.getElementsByName('IniTwoQty');
		for(i = 0;i<iniAmount.length;i++){
			iniAmount[i].onchange=function (){
			
						this.testi = "testi";	
				var pos = 0;	
				var mi = document.getElementsByName('IniTwoQty'); 
				for(i=0;i<mi.length;i++){
					if(mi[i].testi == "testi"){
						pos = i;
						this.testi = "";	
					}	
				}	
				if(isNaN(IniTwoQty[pos].value)||IniTwoQty[pos].value.length==0){IniTwoQty[pos].value=0;}			
document.getElementsByName('iniQty')[pos].value=document.getElementsByName('$twoUnitPro')[pos].value*document.getElementsByName('IniTwoQty')[pos].value;	
if(!isFloat(document.getElementsByName('iniQty')[pos].value)) document.getElementsByName('iniQty')[pos].value='0';
else document.getElementsByName('iniQty')[pos].value=f(document.getElementsByName('iniQty')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniAmount"));

document.getElementsByName('iniAmount')[pos].value=document.getElementsByName('iniPrice')[pos].value*document.getElementsByName('iniQty')[pos].value;
 if(!isFloat(document.getElementsByName('iniAmount')[pos].value)) document.getElementsByName('iniAmount')[pos].value='0';
 else document.getElementsByName('iniAmount')[pos].value=f(document.getElementsByName('iniAmount')[pos].value,$!globals.getDigitsOrSysSetting("tblStockDet","IniAmount"));
execStat();
			};
		}
	#end
					execStat();
}
function loadFireEvent()
{
           var fieldCaculate=document.getElementsByName('iniQty');		
			for(var i = 0;i<fieldCaculate.length;i++){				
		    	if(fieldCaculate[i].value.length>0){
						if(("".length>0&&fieldCaculate[i].value!='') ||"".length==0){
						if(document.createEvent){
				    		var evt = document.createEvent("HTMLEvents");	 	    		
				    		evt.initEvent("change",false,false);
				    		fieldCaculate[i].dispatchEvent(evt);	
						 }else{
							fieldCaculate[i].fireEvent("onchange");
						 }
				 	}
				}
			}
			var fieldCaculate=document.getElementsByName('iniPrice');		
			for(var i = 0;i<fieldCaculate.length;i++){				
		    	if(fieldCaculate[i].value.length>0){
					if(("".length>0&&fieldCaculate[i].value!='') ||"".length==0){
						if(document.createEvent){
				    		var evt = document.createEvent("HTMLEvents");	 	    		
				    		evt.initEvent("change",false,false);
				    		fieldCaculate[i].dispatchEvent(evt);	
						 }else{
							fieldCaculate[i].fireEvent("onchange");
						 }
				 	}
				}
			}
		        	var fieldCaculate=document.getElementsByName('iniAmount');		
			for(var i = 0;i<fieldCaculate.length;i++){				
		    	if(fieldCaculate[i].value.length>0){
					if(("".length>0&&fieldCaculate[i].value!='') ||"".length==0){
						if(document.createEvent){
				    		var evt = document.createEvent("HTMLEvents");	 	    		
				    		evt.initEvent("change",false,false);
				    		fieldCaculate[i].dispatchEvent(evt);	
						 }else{
							fieldCaculate[i].fireEvent("onchange");
						 }
				 	}
				}
			}
			
}
function initCountCal(){

}
function execStat(){
 var statmi = document.getElementsByName('iniAmount'); 
				var totalstat = 0;
				for(i=0;i<statmi.length;i++){
				    if(isFloat(statmi[i].value)){
					   totalstat = totalstat + Number(statmi[i].value);
					}
				}
				var mistat = document.getElementById('iniAmount'+'_total'); 
				mistat.innerHTML = f(totalstat,$globals.getDigitsOrSysSetting("tblStockDet","iniAmount"));
																												
}
function openChildSelect(urlstr,obj,field,thisField){
   obj.testi = "testi";	
	var id = 0;	
   	var mi = document.getElementsByName(thisField); 
   	for(i=0;i<mi.length;i++){
		if(mi[i].testi == "testi"){
			id = i;
			obj.testi = "";	
		}	
	}
	var index=urlstr.indexOf("&seq=");
	if(index>-1){
	   if(!$!startSeq) return false;
	   <!--如果该商品启用了序列号属性并且必须输入-->
	   var defSeqStr="";
	   if(defSeqArr.length>=id+1){
	       defSeqStr=defSeqArr[id];
	   }
	   var lastSeq="";
	   var hidSeqs=document.getElementsByName("tblGoodsOfProp_"+thisField);
	   for(i=hidSeqs.length-1;i>=0;i--){
	     if(hidSeqs[i].value!="")
		 {
		   lastSeq=hidSeqs[i].value;break;
		 }
	   }
	urlstr=urlstr.substring(0,index+"&seq=".length)+document.getElementsByName("tblGoodsOfProp_"+thisField)[id].value+"&goodsCode=$goodsCode&seqFname=tblIniStockDet_"+thisField+"&defSeqStr="+defSeqStr+"&lastSeq="+lastSeq+"&newSeq="+mi[id].value;
	
	}
    var tbname=urlstr.substring(urlstr.indexOf("&tableName=")+"&tableName=".length,urlstr.indexOf("&fieldName="));
	var fname=urlstr.substring(urlstr.indexOf("&fieldName=")+"&fieldName=".length,urlstr.indexOf("&operation="));
	
	if(tbname!=""&&fname==""){
	   urlstr=urlstr+"&selectName=SelectProp";
	}
	if(index>-1){
	
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&tblIniStockDet_GoodsCode=$!goodsCode&iniPropField="+thisField,"","dialogWidth=465px;dialogHeight=360px"); 
	}else{
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&tblIniStockDet_GoodsCode=$!goodsCode&iniPropField="+thisField,"","dialogWidth=800px;dialogHeight=450px"); 
	}
	
if(index>-1){


var seqArr=document.getElementsByName(thisField);
var seqQtyArr=document.getElementsByName('$!SeqQtyfName');
	   if(typeof(str)=="undefined")return false;
	   if(str!=""){//验证序列号是否重复











        var result="";
        var curSeqArr=str.split(";")[1].split("~");
		var objs=document.getElementsByName('tblGoodsOfProp_'+thisField);
          for(var k=0;k<objs.length;k++){
		   if(k!=id&&objs[k].value!=""){
		    var tempArr=objs[k].value.split("~");
	         for(var p=0;p<curSeqArr.length-1;p++){
			  var tempSeq=curSeqArr[p];
			  for(var i=0;i<tempArr.length-1;i++){
			   if(tempArr[i]==tempSeq){
					  result+=tempSeq+"、"
				 }
			  }
			}
		   }
		  }
		  if(result!=""){
         alert('$text.get("seq.validate.error5")'+'\n'+result.substring(0,result.lastIndexOf("、")));
					   return false;
			}
		var curRowSeq=str.split(";");
	    seqArr[id].value=curRowSeq[0];
	   var seqHiddenArr=document.getElementsByName("tblGoodsOfProp_"+thisField);
	   seqHiddenArr[id].value=curRowSeq[1];
	   seqQtyArr[id].value=curRowSeq[1].split("~").length-1;
	   if(document.createEvent){
    		var evt = document.createEvent("HTMLEvents");	 	    		
    		evt.initEvent("change",false,false);
    		seqQtyArr[id].dispatchEvent(evt);	
		 }else{
			seqQtyArr[id].fireEvent("onchange");
		 }
	   }else{
	     seqArr[id].value="";
		 var seqHiddenArr=document.getElementsByName("tblGoodsOfProp_"+thisField);
		 seqHiddenArr[id].value="";
		 seqQtyArr[id].value=0;
		 if(document.createEvent){
    		var evt = document.createEvent("HTMLEvents");	 	    		
    		evt.initEvent("change",false,false);
    		seqQtyArr[id].dispatchEvent(evt);	
		 }else{
			seqQtyArr[id].fireEvent("onchange");
		 }
  	   }
	   

  }else{
  if(str!=""){
		if(str.indexOf("|")<=-1){
		  str=str+"|";
		}
		var mutli=str.split("|");
		for(j=0;j<mutli.length-1;j++){
			var han= document.getElementsByName(obj.name).length;		
			if(id+1>han){
				var trObj=obj.parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
				if(document.createEvent){
		    		var evt = document.createEvent("HTMLEvents");	 	    		
		    		evt.initEvent("change",false,false);
		    		trObj.cells[trObj.cells.length-1].dispatchEvent(evt);	
				 }else{
					trObj.cells[trObj.cells.length-1].fireEvent("onchange");
				 }
			}
			
			fs=mutli[j].split(";");
			if(thisField=="provider"){//供应商弹出框
			 document.getElementsByName("provider")[id].value=fs[1];
			 document.getElementsByName("hidProvider")[id].value=fs[0];
			}else if(thisField=="secUnit"){//辅助单位弹出框









			  mi[id].value=fs[2];
			  document.getElementsByName("conversionRate")[id-1].value=fs[1];
			  document.getElementsByName("hidSecUnit")[id-1].value=fs[0];
			  if(document.createEvent){
		    		var evt = document.createEvent("HTMLEvents");	 	    		
		    		evt.initEvent("change",false,false);
		    		document.getElementsByName("iniQty")[id-1].dispatchEvent(evt);	
				 }else{
					document.getElementsByName("iniQty")[id-1].fireEvent("onchange");
				 }
			}else{
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
			id++;
		}
	  }
	}
    return 0;
}
function removeOtherEvent(){//凡是属性并且在库存明细表中的输入类型为关联表选择，都会配有弹出框，但是如果该商品没有关联商品表则不需要











  #foreach($item in $!existsPropNames)
    #if($globals.getPropBean($globals.get($item,0)).joinTable=="2"&&$globals.getPropBean($globals.get($item,0)).sequence=="2")
	   var items=document.getElementsByName("$globals.get($item,0)");
	   for(i=0;i<items.length;i++){
	     items[i].ondblclick=function(){};
		 items[i].onkeypress=function(){};
		 items[i].onkeydown=function(){if(event.keyCode ==13){event.keyCode=9}};
	   }
	#end
  #end
}
function initSelect(rowCount){
 #if($startSeq)//启用序列号










 
   document.getElementsByName("$!SeqQtyfName")[rowCount-2].readOnly="readOnly";
   var seqname=document.getElementById("SeqPropfName");
   document.getElementsByName(seqname.value)[rowCount-2].alt= " ";
 #end
 #if($existsTowUnit)
   document.getElementsByName("iniQty")[rowCount-2].attachEvent('onchange',function()
   {
     var flag=false;
     #foreach($item in $!existsPropNames)
 	   var propVal=document.getElementsByName("tblGoodsOfProp_$globals.get($item,0)")[rowCount-2].value;
	   if(propVal!=""){
	     flag=true;
	   }
	 #end
	 if(!flag){
	   #foreach($item in $!existsPropNames)
	   if(document.getElementsByName("tblGoodsOfProp_$globals.get($item,0)").length!=0){
 	   document.getElementsByName("tblGoodsOfProp_$globals.get($item,0)")[rowCount-2].value=document.getElementsByName("tblGoodsOfProp_$globals.get($item,0)")[rowCount-3].value;
	   }
	   if(document.getElementsByName("$globals.get($item,0)").length!=0){
	   document.getElementsByName("$globals.get($item,0)")[rowCount-2].value=document.getElementsByName("$globals.get($item,0)")[rowCount-3].value;
	   }
	   #end
	   #if("$!globals.getSysSetting('StockLocation')"=="true")
	   document.getElementsByName("StockLocation")[rowCount-2].value=document.getElementsByName("StockLocation")[rowCount-3].value;
	   #end
	   document.getElementsByName("iniPrice")[rowCount-2].value=document.getElementsByName("iniPrice")[rowCount-3].value
	   if(document.createEvent){
    		var evt = document.createEvent("HTMLEvents");	 	    		
    		evt.initEvent("change",false,false);
    		document.getElementsByName("iniPrice")[rowCount-2].dispatchEvent(evt);	
		 }else{
			document.getElementsByName("iniPrice")[rowCount-2].fireEvent("onchange");
		 }
	 } 
   });
   
 #end
}
</script>
</head>

<body onLoad="showtable('edittable'); showStatus(); initTableList(edittableDIV,edittable,editgriddata,#if($!props.size()!=0) 0 #else 1 #end);initCalculate();initCountCal();if(typeof(setSeqFname)!='undefined'){setSeqFname();getDefaultSeq();};removeOtherEvent();">

<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" scope="request" name="form" action="/IniGoodsQueryAction.do" onSubmit="return beforSubmit(this);" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")">
<input type="hidden" name="type" value="$!type">
<input type="hidden" name="companyCode" value="$companyCode">
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()">
<input type="hidden" name="GoodsCode" value="$goodsCode">
<input type="hidden" name="StockCode" value="$stockCode">
<input type="hidden" name="SeqPropfName" id="SeqPropfName" value="">
<!-- 保存查询条件，在返回的时候可以保存条件 -->
<input type="hidden" name="goodsNumber" value="$!iniGoodsForm.getGoodsNumber()">
<input type="hidden" name="goodsFullName" value="$!iniGoodsForm.getGoodsFullName()">
<input type="hidden" name="stockName" value="$!iniGoodsForm.getStockName()">
<input type="hidden" name="dimQuery" value="$!iniGoodsForm.getDimQuery()">
<input type="hidden" name="pageNo" value="$!pageNo">
 <input type="hidden" name="winCurIndex" value="$!winCurIndex">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("iniGoods.lb.title")</div>
	<ul class="HeadingButton">		
	   #if($globals.getMOperation().update())
		<li><button type="button" onClick="beforSubmit(document.form);" class="b2">$text.get("common.lb.save")</button></li>
		#end
		#if($globals.getMOperation().query())
			<li>	<button type="button" onClick="location.href='/IniGoodsQueryAction.do?type=back&goodsCode=$!goodsCode&winCurIndex=$!winCurIndex&goodsNumber=$!iniGoodsForm.getGoodsNumber()&goodsFullName=$!globals.encode($!iniGoodsForm.getGoodsFullName())&stockName=$!globals.encode($!iniGoodsForm.getStockName())&dimQuery=$!globals.encode($!iniGoodsForm.getDimQuery())&pageNo=$!pageNo'" class="b2">$text.get("common.lb.back")</button></li>
	   #end
	</ul>
</div>
<div id="listRange_id">
		<div class="listRange_1">	
			<li><span>$text.get("iniGoods.lb.goodsNo")：</span><input  type="text"   name="goodsNumber" readonly style=" color:#666666" value="$!globals.get($detail,1)" ></li>
			<li><span>$text.get("iniGoods.lb.goodsName")：</span><input  type="text"  name="goodsFullName" readonly style=" color:#666666" value="$!globals.get($detail,2)" ></li>			
			<li><span>$text.get("iniGoods.lb.goodsSpec")：</span><input  type="text"  name="goodsSpec" readonly style=" color:#666666" value="$!globals.get($detail,9)" ></li>
			<li><span>$text.get("iniGoods.lb.stock")：</span><input name="stockCode" type="hidden" value="$globals.get($detail,3)">
			<input name="stockName" type="text"   readonly style=" color:#666666"  value="$globals.get($detail,4)" ></li>
			#if("$!globals.getSysSetting('AssUnit')"=="true")
			<li><span>$text.get("iniGoods.lb.Unit")：</span>
			<input name="secUnit" type="text"  readonly style=" color:#666666"  value="$globals.get($detail,11)" ></li>
			#end
			#if($existsTowUnit)
			<li><span>$text.get("iniGoods.lb.inputType")：</span><select  name="inputType" onKeyDown="if(event.keyCode==13) event.keyCode=9"  >
					#foreach($erow in $globals.getEnumerationItems("QtyInputype"))
						<option value="$erow.value" #if($erow.value ==$inputType) selected #end>$erow.name</option>
					#end
			</select></li>
			#end
		</div>
	  <div class="scroll_function_small_a" id="conter">
			<div  name="edittableDIV" id="edittableDIV"> 
			<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function_b" name="edittable" id="edittable">
			
			</table>
			</div>
	</div>
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-115;
oDiv.style.height=sHeight+"px";
</script>
</div>
</form> 
</body>
</html>
