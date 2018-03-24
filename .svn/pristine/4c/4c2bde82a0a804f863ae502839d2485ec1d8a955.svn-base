<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
		<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
		<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/date.vjs"></script>
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/function.js"></script>
		<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
		<script type="text/javascript" src="/js/gen/define.vjsComputeWageDeduct_zh_CN.js"></script>
		<script type="text/javascript">
			try{
				var koron = new ActiveXObject("KoronCom.TrustedSites") ;
				koron.add("$!IP") ;
			}catch (e){
			}
		</script>
		<script type="text/javascript">
		var linkStr="";
		function ajaxSelects(goodsCode,fieldName,display){
			var url="/UtilServlet?operation=Ajax&selectName=LabelSelectGoods&MOID=$LoginBean.getOperationMap().get("/LabelAction.do").moduleId&MOOP=query&selectField="+fieldName+"&selectValue="+encodeURIComponent(document.getElementById(fieldName).value);
			 AjaxRequest(url) ;
			 if(response == "" || response == null){
			 	show(goodsCode,fieldName,display,0);
			 }else if(response.indexOf("@condition:") >= 0){
				 response = revertTextCode(response) ;
				 var array = response.split("@condition:");
				 linkStr="&keySearch="+array[1]+"&url=@URL:";
				 show(goodsCode,fieldName,display,1);
			 }else if(response.indexOf(";") >= 0){
				var note = response.split(";") ;
				jQuery("#"+goodsCode).val(note[0]);
				jQuery('GoodsCode').val(note[0]);
				jQuery("#"+fieldName).val(note[1]);
				jQuery("#"+display).val(note[2]);
				jQuery("#goodsNumber").html(note[0]);
			 }
			 return false;
		}
		
		//商品名称弹出框选择
		var goodsCodes = "";
		var goodsNames = "";
		var units = "";
		var selectType = "";
		var colors = "";
		var colorNames = "";
		var typenames = "";
		var typeValue = "";
		function show(goodsCode,goodsName,unit,types){
			selectType = "goods";
			var displayName=encodeURI("$text.get('iniGoods.lb.goodsName')") ;
			var urlstr = '/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=tblLabel&selectName=LabelSelectGoods' ;
			if(types==1){
				urlstr+=encodeURI(linkStr);
			}
			urlstr += "&popupWin=Popdiv&MOID=$LoginBean.getOperationMap().get("/LabelAction.do").moduleId&MOOP=add&LinkType=@URL:&displayName="+displayName;
			asyncbox.open({id:'Popdiv',title:"$text.get('iniGoods.lb.goodsName')",url:urlstr,width:750,height:470})
			goodsCodes = goodsCode;
			goodsNames = goodsName;
			units = unit;
		}
		
		//返回数据
		function exePopdiv(returnValue){
			if(typeof(returnValue)=="undefined") return ;
			var note = returnValue.split("#;#") ;
			if(selectType == "goods"){
				//商品弹出框返回值


				jQuery("#"+goodsCodes).val(note[0]);
				jQuery("#GoodsCode").val(note[0]);
				jQuery("#"+goodsNames).val(note[1]);
				jQuery("#"+units).val(note[2]);
				jQuery("#goodsNumber").html(note[0]);
			}else if(selectType == "color"){
				jQuery("#"+colors).val(note[0]);
				jQuery("#"+colorNames).val(note[1]);
			}else if(selectType == "types"){
				jQuery("#"+typeValue).val(note[1]);
				jQuery("#"+typenames).val(note[0]);
			}else if(selectType == "trackno"){
				jQuery("#trackNo").val(note[0]);
			}
		}
		
		
		function showSelect(color,colorName){
			var goodes = jQuery("#GoodsCode").val();
			if(goodes == ""){
				alert("请选择商品！");
				$("#goodsName").focus();
				return false;
			}
			selectType = "color";
			var displayName=encodeURI("$text.get("Label.colorName")") ;
			var urlstr = '/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=tblLabel&selectName=SelectcolorNum&GoodsCode='+goodes;
			urlstr += "&popupWin=Popdiv&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName;
			asyncbox.open({id:'Popdiv',title:"$text.get('Label.colorName')",url:urlstr,width:750,height:470})
			colors = color;
			colorNames = colorName;
		}
		
		function showSelect2(type,typename,selectName,name){
			var displayName=encodeURI(name) ;
			selectType = "types"
			var urlstr = '/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=tblLabel&selectName='+selectName ;
			urlstr += "&popupWin=Popdiv&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName;
			asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:750,height:470})
			typeValue = type;
			typenames = typename;
		}
		
		
		function selectTrackNo(){
			selectType = "trackno";
			var displayName=encodeURI("$text.get('mrp.lb.trackNo')") ;
			var urlstr = '/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=tblLabel&selectName=SelectTrackNo' ;
			urlstr += "&popupWin=Popdiv&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName;
			asyncbox.open({id:'Popdiv',title:"$text.get('mrp.lb.trackNo')",url:urlstr,width:750,height:470})
		}
		
		function checkForm(){
			var seq = document.getElementById("seq").value;
			var goodsName = document.getElementById("goodsName").value;
			var qty = document.getElementById("qty").value;
			if(seq==null || seq.length<=0){
				alert("$text.get("Label.seq.null")"); 
				document.getElementById("seq").focus();
				return false;
			}
			if(seq.length != 10){
				alert("序列号长度只能是10位"); 
				document.getElementById("seq").focus();
				return false;
			}
			if(!isEn(seq)){
				alert("$text.get("pop.title.sequence1")$text.get("common.validate.error.en")"); 
				document.getElementById("seq").focus();
				return false;
			}
			if(goodsName==null || goodsName.length<=0){
				alert("$text.get("tblCustomerPrice.error.goods.count")"); 
				document.getElementById("goodsName").focus();
				return false;
			}
			#if("$!Meter" == "true")
				var meter = document.getElementById("meter").value;
				if(!isFloat(meter)){
					alert("$text.get("Label.meter")$text.get("Label.if.num")");
					document.getElementById("meter").focus();
					return false;
				}
			#end
			#if("$!Coil" == "true")
				var coil = document.getElementById("coil").value;
				if(!isFloat(coil)){
					alert("$text.get("Label.coil")$text.get("Label.if.num")");
					document.getElementById("coil").focus();
					return false;
				}
			#end
			if(qty==null || qty.length<=0){
				alert("数量不能为空"); 
				document.getElementById("qty").focus();
				return false;
			}
			if(!isFloat(qty)){
				alert("$text.get("mrp.lb.count")$text.get("Label.if.num")");
				document.getElementById("qty").focus();
				return false;
			}
			if(qty.length>10){
				alert("数量长度不能大于10位"); 
				document.getElementById("qty").focus();
				return false;
			}
			if(!isFloat(qty) || getDecimalDigit(qty)>$!num){
				alert("数量不能超过$!num位小数位"); 
				document.getElementById("qty").focus();
				return false;
			}
			if(qty<0){
				alert("数量必须大于0");
				document.getElementById("qty").focus(); 
				return false;
			}
			var id = document.getElementById("id").value;
			var url = "/PublicServlet?operation=selectSeq&seq="+seq;
			if(id != ""){
				url+="&id="+id;
			}
			AjaxRequest(url);
			if(response == "echo"){
				alert("$text.get("Label.seq.exist")");
				document.getElementById("seq").focus();
				return false;
			}
			var goodscodes = document.getElementById("goodsCode").value;
			if(goodscodes==null || goodscodes.length<=0){
				alert("$text.get("tblCustomerPrice.error.goods.count")"); 
				jQuery("#goodsName").val("");
				jQuery("#goodsName").focus();
				return false;
			}
			return true;
		}
		function setAutoScale(str){
			document.getElementById("autoScale").value=str;
			document.form.submit();
		}
		
		var reportNumber2;
		var billId2;
		var moduleType = "";
		var winCurIndex = "";
		function prints(billId,reportNumber){
			reportNumber2=reportNumber;
			billId2=billId
			window.showModalDialog("/UserFunctionQueryAction.do?tableName=$!tableName&reportNumber="+reportNumber+"&moduleType="+moduleType+"&operation=$globals.getOP("OP_PRINT")&BillID="+billId+"&parentTableName=$!parentTableName&winCurIndex="+winCurIndex,window,"dialogWidth=268px;dialogHeight=245px,scroll=no;")
		}
		
		function doPrint(format,billTable,reportNumber,billId){
			try{
				if(document.getElementById("print").checked){
					var varSQL = "$!SQLSave" ;
					var mimetype = navigator.mimeTypes["application/np-print"];
					if(mimetype){
						if(mimetype.enabledPlugin){
							 var cb = document.getElementById("plugin");
							 cb.print('$!IP|$!port',format,billId,reportNumber,billTable,'$!JSESSIONID','$LoginBean.id','$LoginBean.id','$!globals.getLocale()',varSQL);
						}
					}else{
						var obj = new ActiveXObject("KoronReportPrint.BatchPrint") ;
					    obj.URL="$!IP|$!port" ;
					    obj.SQL= varSQL ;
					    obj.StyleList = format ;
					    obj.BillID= billId ;
					    obj.BillTable=billTable ;
					   	obj.Cookie ="$!JSESSIONID" ;
		   				obj.UserId ='$LoginBean.id' ;
					    obj.ReportNumber=reportNumber ;
					    obj.Language = "$!globals.getLocale()" ;
						obj.doPrint();
					}
					alert("打印成功");
				}else{
					alert("保存成功");
				}
				AjaxRequest('/PublicServlet?operation=selectSeqMax');
				document.getElementById("seq").value=response;
				document.getElementById("qty").value="";
				var date = new Date();
		    	jQuery("#lableList").attr("src","/LabelListAction.do?operation=4&types=1&time="+date);
		    	
		    	
		    	document.getElementById("qty").focus();
			}catch (e){
				alert("$text.get('com.sure.print.control')") ;
			}
			
		}
		
		function printFormatSet(keyId){
			//window.showModalDialog("/ReportSetAction.do?operation=84&reportId="+keyId,winObj,"dialogWidth:650px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
			var urls = "/ReportSetAction.do?operation=84&reportId="+keyId;
			asyncbox.open({id:'Popdiv',title:'样式设计弹出框',url:urls,width:750,height:470});
		}
		
		function fillSelected()
		{
			var id =jQuery("#childId").val();
			jQuery.ajax({
		    	type: "POST",
		    	url: "/LabelAction.do?operation=2&id="+id,
		   		data: "name=John&location=Boston",
		   		dataType: "json",
		    	success: function(msg){
		    		jQuery("#id").val(msg[0].id);
		    		jQuery("#goodsCode").val(msg[0].goodsCode);
		    		jQuery("#goodsName").val(msg[0].goodsFullName);
		    		jQuery("#unit").val(msg[0].unit);
		    		jQuery("#seq").val(msg[0].seq);
		    		jQuery("#batchNo").val(msg[0].batchNo=="null"?"":msg[0].batchNo);
		    		jQuery("#design").val(msg[0].design);
		    		jQuery("#color").val(msg[0].color);
		    		jQuery("#colorBit").val(msg[0].colorBit);
		    		jQuery("#coil").val(msg[0].coil=='null'?"":msg[0].coil);
		    		var meter = msg[0].meter;
		    		if(meter!=""){
		    			meter= meter.substring(0,meter.indexOf('.'));
		    		}
		    		jQuery("#meter").val(meter);
		    		jQuery("#qty").val(msg[0].qty);
		    		jQuery("#colorName").val(msg[0].colorName);
		    		jQuery("#gram").val(msg[0].gram);
		    		jQuery("#breadth").val(msg[0].breadth);
		    		jQuery("#density").val(msg[0].density=="null"?"":msg[0].density);
		    		jQuery("#user1").val(msg[0].user1);
		    		jQuery("#user2").val(msg[0].user2);
		    		jQuery("#trackNo").val(msg[0].trackNo);
		    		jQuery("#procedures").val(msg[0].procedures);
		    		jQuery("#Designs").val(msg[0].Design);
		    		jQuery("#Users1").val(msg[0].User1);
		    		jQuery("#Users2").val(msg[0].User2);
		    		jQuery("#goodsNumber").html("编号："+msg[0].goodsNumber);
		    		jQuery("#qty").focus();
		    	}
			});
		}
		function loadScale(){
			if("$globals.getSysSetting("AutoScale")"=="true"){
				autoScaleObj.runWeight(); //启动称重函数
				return true; 
			}
		}
		
		
		function isSelectField(){
			var goodscode = jQuery("#goodsCode").val();
			if(goodscode==""){
				jQuery("#goodsName").focus;
				jQuery("#goodsName").val("");
			}
		}
</script>

	#if($globals.getSysSetting("AutoScale")=="true")	
<SCRIPT LANGUAGE="JavaScript">
try{
	objStr = '<OBJECT classid="clsid:B4985DFB-F445-41C0-A01B-0C6723EB4AB3" ID="autoScaleObj"  width="0" height="0"></OBJECT>';
	document.write(objStr);
}catch (e){
	alert("请正确安装磅秤控件");
}
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript" FOR="autoScaleObj" EVENT="OnWeight(weight)">
	jQuery("#qty").val(weight); 
</SCRIPT>
#end
<style type="text/css">
.text{background:none;padding:0;box-sizing:border-box;border:1px solid #8CBEDC;border-radius:3px;}
.oalistRange_list_function select{box-sizing:border-box;border:1px solid #8CBEDC;border-radius:3px;height:20px;}
</style>
	</head>

	<body onload="loadScale()">	
		<iframe name="formFrame" id="formFrame" style="display:none"></iframe>
		<form name="form" method="post" action="/LabelAction.do"
			onsubmit="return checkForm()" target="formFrame">
			<input name="operation" type="hidden" value="1" />
			<input name="childId" id="childId" value="" type="hidden"/>
			<input name="id" value="" id="id" type="hidden"/>
			<input type="hidden" name="goodsCode" id="goodsCode" />
			<input type="hidden" name="GoodsCode" id="GoodsCode" value="$!label.goodsCode" />
			<input type="hidden" name="tableName" id="tableName" value="$!tableName" />
			<input type="hidden" name="reportNumber" id="reportNumber" value="$!tableName" />
			<input type="hidden" name="saveType" value="printSave" />
			<input type="hidden" name="tableName" value="tblBuyApplication" />
			<input type="hidden" name="unit" id="unit" value="$!label.unit" />
			<input type="hidden" name="autoScale" id="autoScale" value="" />
			
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/style1/images/Left/Icon_1.gif" />
				</div>
				<div class="HeadingTitle">
					$text.get("Label.Mimeograph")
				</div>
				<ul class="HeadingButton">
				<label><input type="checkbox" id="print" name="print" value="true" #if($!scaleprint=="true") checked #end />保存后打印标签&nbsp;&nbsp;</label>
				<button type="submit" name="submitok" class="b2">
										$text.get("common.lb.ok")
									</button>
									<button type="button" name="pro" class="b2"
										onclick="printFormatSet('$!reportid');">
										$text.get("common.lb.print.FormatDeisgn")
									</button>
									<button type="button" onClick="closeWin();" class="b2">
										$text.get("common.lb.close")
									</button>
									<button type="button" onClick="window.location.reload();" class="b2">
										刷新
									</button>		
									#if($globals.getSysSetting("AutoScale")=="true")							
									<button type="button" id="atutoscale"
										onClick="setAutoScale('stop')">										
										停用磅秤
									</button>
									#else
									<button type="button" id="atutoscale"
										onClick="setAutoScale('start')">										
										启用磅秤
									</button>
									#end
									
				</ul>
			</div>
			<div id="listRange_id">
				<div class="oalistRange_scroll_1">
					<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>
					<table width="90%" border="0" cellpadding="0" cellspacing="0" align="center"
						class="oalistRange_list_function" name="table">
						<tbody>
							<tr>
								<td class="oabbs_tr">
									$text.get("call.lb.goodsName")：







								</td>
								<td>
									<input type="text" id="goodsName" name="goodsName"
										id="goodsName" value="$!goodsName"
										onDblClick="show('goodsCode','goodsName','unit')" class="text"
										onblur="isSelectField()" 
										onKeyDown="if(event.keyCode==13){if(this.value.length>0){ajaxSelects('goodsCode','goodsName','unit');event.keyCode=9;}else{show('goodsCode','goodsName','unit',0);event.keyCode=9;}}"/>
									<img onClick="show('goodsCode','goodsName','unit')"
										src="/$globals.getStylePath()/images/St.gif" class="search" />
									<font color="#FF0000">* </font>
								</td>
								<td class="oabbs_tr">
									$text.get("mrp.lb.trackNo")：

								</td>
								<td>
									<input type="text" id="trackNo" name="trackNo"
										id="goodsName" value="$!trackNo"
										onDblClick="selectTrackNo()" class="text" />
									<img onClick="selectTrackNo()"
										src="/$globals.getStylePath()/images/St.gif" class="search" />
									<font color="#FF0000"> </font>

								</td>
							</tr>
							#foreach($!listtype in $!list) #if("$!globals.get($listtype,0)"=="color")
							<tr>
								<td class="oabbs_tr">
									$text.get("Label.color")：

								</td>
								<td>
									<input type="text" name="$!globals.get($listtype,0)"
										onDblClick="showSelect('color','colorName')"
										id="$!globals.get($listtype,0)" value="" class="text" />
									<img onClick="showSelect('color','colorName')"
										src="/$globals.getStylePath()/images/St.gif" class="search" />
								</td>
								<td class="oabbs_tr">
									$text.get("Label.colorName")：

								</td>
								<td>
									<input type="text" name="colorName" id="colorName" value=""
										onDblClick="showSelect('color','colorName')" class="text" />
									<img onClick="showSelect('color','colorName')"
										src="/$globals.getStylePath()/images/St.gif" class="search" />
								</td>
							</tr>
							#end #end #set($listone = 0) 
							#foreach($!listtype in $!list)
							#if($listone ==0)
							<tr>
							#end 
							#if("$!globals.get($listtype,0)" == "Design")
								<td class="oabbs_tr">
									$!globals.get($listtype,1)：

								</td>
								<td><input name="design" id="design" type="hidden"/>
									<input type="text" name="Designs" id="Designs"
										onDblClick="showSelect2('design','Designs','SelectersDesign','$!globals.get($listtype,1)')"
										readonly="true" value=""
										class="text" />
									<img onClick="showSelect2('design','Designs','SelectersDesign','$!globals.get($listtype,1)')"
										src="/$globals.getStylePath()/images/St.gif" class="search" />
								</td>
								#set($listone = $listone+1)
							#elseif("$!globals.get($listtype,0)" == "User1")
								<td class="oabbs_tr">
									$!globals.get($listtype,1)：

								</td>
								<td><input type="hidden" name="user1" id="user1" />
									<input type="text" name="Users1" id="Users1"
										onDblClick="showSelect2('user1','Users1','SelectersUser1','$!globals.get($listtype,1)')"
										readonly="true" value=""
										class="text" />
									<img
										onClick="showSelect2('user1','Users1','SelectersUser1','$!globals.get($listtype,1)')"
										src="/$globals.getStylePath()/images/St.gif" class="search" />
								</td>
								#set($listone = $listone+1)
							#elseif("$!globals.get($listtype,0)" == "User2")
								<td class="oabbs_tr">
									$!globals.get($listtype,1)：

								</td>
								<td><input type="hidden" name="user2" id="user2" />
									<input type="text" name="Users2" id="Users2"
										onDblClick="showSelect2('user2','Users2','SelectersUser2','$!globals.get($listtype,1)')"
										readonly="true" value=""
										class="text" />
									<img
										onClick="showSelect2('user2','Users2','SelectersUser2','$!globals.get($listtype,1)')"
										src="/$globals.getStylePath()/images/St.gif" class="search" />
								</td>
								#set($listone = $listone+1)
							#elseif("$!globals.get($listtype,0)" == "BatchNo")
								<td class="oabbs_tr">
									$!globals.get($listtype,1)：

								</td>
								<td>
									<input type="text" name="batchNo" id="batchNo" value=""
										class="text" />
								</td>
								#set($listone = $listone+1)
							#elseif("$!globals.get($listtype,0)" == "Breadth")
								<td class="oabbs_tr">
									$!globals.get($listtype,1)：

								</td>
								<td>
									<input type="text" name="breadth" id="breadth" value=""
										class="text" />
								</td>
								#set($listone = $listone+1)
							#elseif("$!globals.get($listtype,0)" == "Density")
								<td class="oabbs_tr">
									$!globals.get($listtype,1)：

								</td>
								<td>
									<input type="text" name="density" id="density" value=""
										class="text" />
								</td>
								#set($listone = $listone+1)
							#elseif("$!globals.get($listtype,0)" == "Gram")
								<td class="oabbs_tr">
									$!globals.get($listtype,1)：

								</td>
								<td>
									<input type="text" name="gram" id="gram" value="" class="text" />
								</td>
								#set($listone = $listone+1)
							#elseif("$!globals.get($listtype,0)" == "ColorBit")
								<td class="oabbs_tr">
									$!globals.get($listtype,1)：

								</td>
								<td>
									<input type="text" name="colorBit" id="colorBit" value=""
										class="text" />
								</td>
								#set($listone = $listone+1)
							#elseif("$!globals.get($listtype,0)" == "Coil")
								<td class="oabbs_tr">
									$text.get("Label.coil")：

								</td>
								<td>
									<input id="coil" name="coil" value="$!label.coil" class="text"
										type="text" />
								</td>
							#set($listone = $listone+1)
							#elseif("$!globals.get($listtype,0)" == "Meter")
								<td class="oabbs_tr">
									$text.get("Label.meter")：

								</td>
								<td>
									<input id="meter" name="meter" value="$!label.meter"
										class="text" type="text" />
								</td>
							#set($listone = $listone+1)
							#elseif("$!globals.get($listtype,0)" == "procedures")
								<td class="oabbs_tr">工序：

								</td>
								<td>
									<select name="procedures" id="procedures" style="width:132px;">
									#foreach($row in $globals.getEnumerationItems("Procedures"))
											<option value="$row.value" #if("$row.value"=="$!label.procedures") selected #end>$row.name</option>
										#end
										</select>
								</td>
								<td>
								</td>
								<td>
								</td>
							#set($listone = $listone+1)
							#else
								#if("$!globals.get($listtype,0)"!="color")
									<td class="oabbs_tr">
										$!globals.get($listtype,1)：

									</td>
									<td>
										<input type="text" name="$!globals.get($listtype,0)"
											id="$!globals.get($listtype,0)" value="" class="text" />
									</td>
									#set($listone = $listone+1)
								#end
							#end 
							#if($listone%2 == 0)
							</tr>
							#end 
							#end
							<tr>
								<td class="oabbs_tr">
									$text.get("pop.title.sequence1")：

								</td>
								<td>
									<input type="text" name="seq" id="seq" value="$!seq"
										class="text" />
									<font color="#FF0000">* </font>
								</td>
								<td class="oabbs_tr">
									$text.get("mrp.lb.count")：

								</td>
								<td>
									<input id="qty" name="qty" value="$!label.qty" class="text"
										type="text" />
									<font color="#FF0000">* </font>
								</td>
							</tr>
						</tbody>
					</table>
			</div>
			</div>
		</form>
	<div align="center">
		<iframe id="lableList" name="lableList" src="/LabelListAction.do?operation=4&types=1" frameborder="no" width="90%" scrolling="no" onload="this.height=lableList.document.body.scrollHeight"/>
	</div>
	</body>
</html>

