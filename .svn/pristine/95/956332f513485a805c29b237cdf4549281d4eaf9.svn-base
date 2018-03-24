<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>库存商品图片列表</title>
<link type="text/css" rel="stylesheet" href="/style1/css/stockgoods.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link rel="stylesheet" href="/style1/css/financereport.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" type="text/css" href="/js/fancybox/fancybox.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/fancybox/jquery.fancybox-1.3.1.pack.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<style type="text/css">
	@media print {
		.al_btn{display: none;}
		.scott{display: none;}
	}
</style>
<script type="text/javascript">
	
	jQuery(function(){
		var zz = $("ul.m_list_ul li.m_list_li").length;
		jQuery("ul.m_list_ul li.m_list_li").each(function(){
			
		})
		jQuery(".have_btn").mouseover(function(){
			jQuery(this).find(".in_btn_dv").show();
			var Ln = jQuery(".btn_item_u li").length;
			var uWidth = 0;
			for(var i=0;i<Ln;i++){
				uWidth += jQuery(".btn_item_u li:eq("+i+")").width();
			}
			jQuery(".btn_item_u").width(uWidth);
		}).mouseout(function(){
			jQuery(this).find(".in_btn_dv").hide();
		});
		
		jQuery("a[rel=group]").fancybox({
			'titlePosition' : 'over',
			'cyclic'        : true,
			'titleFormat'	: function(title, currentArray, currentIndex, currentOpts) {
						return '<span id="fancybox-title-over">' + (currentIndex + 1) + ' / ' + currentArray.length + (title.length ? ' &nbsp; ' + title : '') + '</span>';
			}
		});
	})
		
	/*设置显示模板*/
	function dealModule(){
		var url = "/StockGoodsAction.do?optype=queryModule";
		asyncbox.open({id : 'dealdiv',url : url,title : '模板管理',width : 550,height : 350});
	}
	
	function dealAsyn(){
		jQuery.close('dealModule');
		if(jQuery.exist('dealdiv')){
			jQuery.opener('dealdiv').refurbish();
		}
	}
	
	/* 隐藏过滤div */
	function closeSearch(){
		jQuery('#highSearch').hide();
		if(document.getElementById('back')!=null){
			document.getElementById('back').parentNode.removeChild(document.getElementById('back'));
		}
	}
	
	var tbl_field;
	var objNames;
	/* 搜索中的弹出框选择 */
	function selectPops(fieldName,field,selectName,objName){
		var displayName=encodeURI(fieldName) ;
		tbl_field = field;
		objNames = objName;
		var urlstr = '/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=&selectName='+selectName ;
		urlstr += "&popupWin=Popdiv&MOID=$LoginBean.getOperationMap().get("/StockGoodsAction.do").moduleId&MOOP=add&LinkType=@URL:&displayName="+displayName;
		asyncbox.open({id:'Popdiv',title:"请选择"+fieldName,url:urlstr,width:750,height:470})
	}
	
	//返回数据
	function exePopdiv(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var note = returnValue.split("#;#") ;
		if(tbl_field == "CompanyCode"){
			jQuery("#"+tbl_field).val(note[0]);
			jQuery("#"+objNames).val(note[2]);
		}else{
			jQuery("#"+tbl_field).val(note[0]);
			jQuery("#"+objNames).val(note[1]);
		}
	}

	//刷新
	function refur(){
		submitQuery();
	}
	
	/* 重新选择新模板 */
	function newModule(moduleId){
		jQuery("#moduleId").val(moduleId);
		submitQuery();
	}
	
	//分页
	function pageSubmit(pageNo){
		jQuery("#pageNo").val(pageNo);
		submitQuery();
	}
	
	
	//提交表单
	function submitQuery(){
		form.submit();
	}
	
	
	
	
	var isIe=(document.all)?true:false;
	/* 显示过滤div */
	function showSearchDiv(){
		jQuery('#highSearch').show();
		var bWidth=parseInt(document.documentElement.scrollWidth);
		var bHeight=parseInt(document.documentElement.scrollHeight);
		var back=document.createElement("div");
		back.id="back";
		var styleStr="top:0px;left:0px;position:absolute;background:#666;width:"+bWidth+"px;height:"+bHeight+"px;";
		styleStr+=(isIe)?"filter:alpha(opacity=0);":"opacity:0;";
		back.style.cssText=styleStr;
		document.body.appendChild(back);
		showBackground(back,50);
	}

	//让背景渐渐变暗

	function showBackground(obj,endInt){
		if(isIe){
			obj.filters.alpha.opacity+=5;
			if(obj.filters.alpha.opacity<endInt){
				setTimeout(function(){showBackground(obj,endInt)},1);
			}
		}else{
			var al=parseFloat(obj.style.opacity);al+=0.05;
			obj.style.opacity=al;
			if(al<(endInt/100)){
				setTimeout(function(){showBackground(obj,endInt)},1);}
			}
		}
	function hsHeight(){
		var zz = jQuery("#highSearch").height();
		jQuery("#highSearch").css("height",zz+70);
	}
	
	//清空搜索数据
	function cleaDate(){
		jQuery("#highSearch input").val('');
	}
	
	/* 打印列表*/
	function prints(){
		var dataHeight = jQuery("#data_list_id").css("height");
		jQuery("#data_list_id").css("height","100%");
		jQuery("#data_list_id").show();
		window.print();//打印
		jQuery("#data_list_id").css("height",dataHeight);
	}
</script>
<style type="text/css">
.b_li .swa_c1{float:left;display:inline-block;}
.b_li .swa_c1 .d_box{width:60px;overflow:hidden;height:20px;float:left;word-spacing:-4pt;}
.d_test{text-align:right;}
.d_mid{text-align:justify;text-justify:distribute-all-lines;/*ie6-8*/text-align-last:justify;/* ie9*/-moz-text-align-last:justify;/*ff*/-webkit-text-align-last:justify;/*chrome 20+*/}

@media screen and (-webkit-min-device-pixel-ratio:0){
.d_test:after{content:".";display: inline-block;width:100%;overflow:hidden;height:0;}
}
.d_mh{line-height:25px;float:left;display:inline-block;width:10px;}
</style>
</head>
<body onload="cyh.lableAlign();hsHeight();">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" id="form" name="form" action="/StockGoodsAction.do" target="">
<input type="hidden" id="operation" name="operation" value="4"/>
<input type="hidden" id="moduleId" name="moduleId" value="$!moduleBean.id"/>
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value=""/>
	<div class="wrap_dv">
		<!------------分割线 al_btn按钮  Start------------->
		<div class="al_btn">
			<div class="al_title">
				<i class="char">
					<h3><img src="/style1/images/erpimages/PhotoIcon.jpg" />库存商品图片——（$!moduleBean.moduleName）</h3>
				</i>
			</div>
			<div class="items_dv">
				<div class="btn_item have_btn" style="position:relative;">
					<i class="btn btn-small" onclick="showSearchDiv();">高级查询</i>
					<div class="in_btn_dv">
						<div class="t_bg_dv"></div>
						<div class="m_bg_dv">
							<div class="btn_wp">
								<ul class="btn_item_u">
									#foreach($module in $moduleList)
										<li class="btn_item_li">
											<a class="btn_in_item" onclick="newModule('$!module.id')">$!module.moduleName</a>
										</li>
									#end
								</ul>
							</div>
						</div>
						<div class="b_bg_dv"></div>
					</div>
				</div>
				<div class="btn btn-small btn_item" onclick="refur();">
					$text.get('oa.common.refresh')
				</div>
				<div class="btn btn-small btn_item" onclick="dealModule();">
					显示设置
				</div>
				#if($LoginBean.operationMap.get("/StockGoodsAction.do").print() || $!LoginBean.id=="1")
				<div class="btn btn-small btn_item" onclick="prints();">
					打印
				</div>
				#end
			</div>
		</div>
		<!------------分割线 al_btn按钮   End------------->
		<!------------分割线 main_dv内容  Start------------->
		<div id="data_list_id" style="table-layout:fixed;overflow:auto;border-collapse:collapse;margin-top: 10px;">
		<script type="text/javascript"> 
			var oDiv=document.getElementById("data_list_id");
			var sHeight=document.documentElement.clientHeight-80;
			oDiv.style.height=sHeight+"px";
		</script>
		<div class="main_dv">
			<ul class="m_list_ul">
				#foreach($goods in $goodsList)
				<li class="m_list_li">
					<div class="tb_dv">
						<div class="t_dv" style="text-align: center;">
							#if("$!goods.Picture" != "")
								#foreach($imgs in $!globals.strSplit($!goods.Picture,';'))
									#if($imgs.indexOf("http:") > -1)
										<a rel="group" href="$imgs" title=""><img alt="$goods.GoodsFullName" src="$imgs"/></a>
									#else
									 	<a rel="group" href="/ReadFile.jpg?fileName=$imgs&realName=$imgs&tempFile=false&type=PIC&tableName=tblGoods"><img alt="$goods.GoodsFullName" src="/ReadFile.jpg?fileName=$imgs&realName=$imgs&tempFile=false&type=PIC&tableName=tblGoods" /></a>
									#end
								#end
							#else
								<img alt="$goods.GoodsFullName" src="/style1/images/erpimages/noimg.png" style="width: 72px;height:72px;margin: 40px 0 0 0;"/>
							#end
						</div>
						<ul class="b_ul">
							#foreach($showField in $!showList)
								#if("$showField.fieldName" != "Picture")
								<li class="b_li">
									<div class="swa_c1">
										<div class="d_box"><div class="d_test">
										#if("$showField.fieldName" == "LastQty")
											库存数量
										#elseif("$showField.fieldName" == "LastTwoQty")
											库存匹数
										#elseif("$showField.fieldName" == "lastAmount")
											金额
										#elseif("$showField.fieldName" == "lastPrice")
											库存单价
										#else
											$showField.display.get("$globals.getLocale()")
										#end
										</div></div>
										<div class="d_mh">：</div>
									</div>
									<i class="c_i" title="$goods.get($!showField.fieldName)">
										#if($goods.get($!showField.fieldName) != "0E-8")
											#if("$showField.fieldName" == "LastQty" || "$showField.fieldName" == "LastTwoQty" || "$showField.fieldName" == "lastAmount" || "$showField.fieldName" == "lastPrice")
												#set($otherValue = $!globals.newFormatNumber($goods.get($!showField.fieldName),false,false,$!globals.getSysIntswitch(),'tblStocks','lastAmount',true))
												$otherValue
											#else
												$goods.get($!showField.fieldName)
											#end
										#end
									</i>
								</li>
								#end
							#end
						</ul>
					</div>
				</li>
				#end
			</ul>
		</div>
		#if($!goodsList.size()==0 || "$!goodsList.size()"=="")
			<div class="querydiv1">
				<p style="width:150px;padding:50px 0 0 0;overflow:hidden;line-height:21px;margin:0 auto;">
					很抱歉，没有找到与您条件匹配的库存商品图片信息!
				</p>
			</div>
		#end
		</div>
		<!------------分割线 main_dv内容  End------------->
		$!pageBar
	</div>
	<!-- 条件查询 -->
		
		#set($div_height = $math.mul("$!searchList.size()","25"))
		<div id="highSearch" class="search_popup" style="display:none;height: ${div_height}px;top: 220px;width:500px;">
		<script type="text/javascript">
			var sLeft=document.body.offsetWidth/2
			$("#highSearch").css("left",sLeft);
		</script>
		<input type="hidden" name="queryType" id="queryType" value=""/>
		<div id="Divtitle" style="cursor: move;width:98%;">
			<span class="ico_4"></span>&nbsp;高级查询
		</div>
			<table style="width:100%;padding-top: 5px;" id="tableSearch">
				<tr>
					<td><div><table cellpadding="0" cellspacing="0" width="100%">
						<thead>
						#set($count = 0)
						#foreach($row in $!searchList)
							#if($count == 0)
							<tr>
							#end
							<td class="tdDes1" title="$row.display.get("$globals.getLocale()")" >#if($row.display.get("$globals.getLocale()").length()>5) $globals.subTitle($row.display.get("$globals.getLocale()"),5) #else $row.display.get("$globals.getLocale()") #end:&nbsp;</td>
							#if("$row.inputType" == "0")
								<td style="white-space: nowrap;" class="searchinput"><input name="$!row.fieldName" id="$!row.fieldName" value="$!searchMap.get($!row.fieldName)" #if("$!row.fieldType"=="5") onclick="WdatePicker({lang:'$globals.getLocale()'});" #elseif("$!row.fieldType"=="6") onclick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});" #end/></td>
							#elseif("$row.inputType" == "1")
								<td style="white-space: nowrap;" class="searchinput">
									<select id="$!row.fieldName" name="$!row.fieldName">
										#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
											<option title="$erow.name" value="$erow.value" #if("$!searchMap.get($!row.fieldName)"=="$erow.value") selected #end>$erow.name</option>
										#end
									</select>
								</td>
							#elseif("$row.inputType" == "2")
								<td style="white-space: nowrap;" class="searchinput">
								<input type="hidden" name="$!row.fieldName" id="$!row.fieldName" value="$!searchMap.get($!row.fieldName)"/>
									#set($fieldname = "$!{row.fieldName}_name")
									<input name="$!{row.fieldName}_name" id="$!{row.fieldName}_name" value="$!searchMap.get($!fieldname)" ondblclick="selectPops('$row.display.get("$globals.getLocale()")','$!row.fieldName','$!row.inputValue','$!{row.fieldName}_name')"/>
								<img src='/style1/images/search.gif' style="cursor: pointer;" onclick="selectPops('$row.display.get("$globals.getLocale()")','$!row.fieldName','$!row.inputValue','$!{row.fieldName}_name')" class='search'/></td>
							#elseif("$row.inputType" == "7")
								<td style="white-space: nowrap;" class="searchinput"><input name="$!row.fieldName" id="$!row.fieldName" value="$!searchMap.get($!row.fieldName)"/></td>
							#end
							#set($count = $count + 1)
							#if($count%2 == 0)
							</tr>
							#set($count = 0)
							#end
						#end
						</thead>
					</table></div><td>
				</tr>
			</table>
			<span class="search_popup_bu">
				<input type="button" onclick="submitQuery();" class="bu_1"/>
				<input type="button" class="bu_3" value="" onclick="cleaDate()"/>
				<input type="button" onClick="closeSearch();" class="bu_2" />
			</span>
			<script language="javascript">
			var posX;
			var posY;       
			fdiv = document.getElementById("highSearch");         
			document.getElementById("Divtitle").onmousedown=function(e){
				if(!e) e = window.event;  //IE
			    posX = e.clientX - parseInt(fdiv.style.left);
			    posY = e.clientY - parseInt(fdiv.style.top);
			    document.onmousemove = mousemove;           
			}
 
			document.onmouseup = function(){
			    document.onmousemove = null;
			}
			function mousemove(ev){
			    if(ev==null) ev = window.event;//IE
			    fdiv.style.left = (ev.clientX - posX) + "px";
			    fdiv.style.top = (ev.clientY - posY) + "px";
			}
			</script>
		</div>
</form>
</body>
</html>

