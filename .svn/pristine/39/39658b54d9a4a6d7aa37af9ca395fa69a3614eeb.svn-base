<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>办公用品领用</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/oa/public_goods.js"></script>
<script type="text/javascript">
function delAll(obj){		
	if(sureDel(obj)){
		form.operation.value = 3 ;
		form.part.value = "PARENT" ;
		form.submit();
	}
}
function loadOrder(){
	var len = $('#mybody tr',document).length;
    for(var i = 0;i<len;i++){
        $('#mybody tr:eq('+i+') input[name=order]').val(i+1);
    }
        
}
function deltable(obj){
	if(confirm("确定要删除？")){
		form.action = '/ApplyGoodsAction.do?operation=$globals.getOP("OP_DELETE")&flay='+obj;
		form.submit();
	}

}
</script>
<style type="text/css">
.listRange_list_function tbody td .item_sp{display:block;border-top:1px #AED3F1 solid;}
.listRange_list_function tbody td .item_sp:first-child{border:0;}
</style>
</head>
<body onload="loadOrder()">
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" action="/ApplyGoodsAction.do">
<input type="hidden" name="operation" value=""/>
<input type="hidden" name="part" value=""/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">办公用品领用</div>
	<ul class="HeadingButton">
		#if($query)
		<li><button type="button" name="purList" 
		onClick="showSearch();" class="b2">$text.get("com.query.conditions")</button>					
		</li>
		#end
		<li><button type="button" name="purList" ><a href="/ApplyGoodsAction.do" >刷新</a></button>					
		</li>
		#if($add)
		<li><button type="button" name="applyList"><a href="javascript:" id="addPre" title="领用" >领用</a></button>
		</li>		
		#end
		#if($del)
		<li><button type="button" name="Btn" onclick="delAll('keyId')">删除</button></li>
		#end	
		<li><button type="button"  name="backList" 
		onClick="closeWin();" class="b2">$text.get("common.lb.close")</button>
		</li>
		<li><input type="hidden" value="ADDAPPLY" /></li>
	</ul>	
</div>
<div id="listRange_id">
<div class="scroll_function_small_a" id="conter" style="margin-top:0px;">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.documentElement.clientHeight-125;
oDiv.style.height=sHeight+"px";
</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="applytable" name="applytable">
		<thead>
			<tr>
							
				<td width="4%" class="oabbs_tc">
				<input type="checkbox" name="selAll" onClick="checkAll('keyId')" /> 
				</td>	
				<td></td>	
				<td width="6%" class="oabbs_tc">NO.</td>				        
		        <td width="10%" class="oabbs_tc">领用单号</td>				
				<td width="12%" class="oabbs_tc">领用人</td>
			    <td width="10%" class="oabbs_tc">领用日期</td>
			    <td width="20%" class="oabbs_tc">用品名称</td>
			    <td width="7%" class="oabbs_tc">类型</td>
			    <td width="5%" class="oabbs_tc">单位</td>
			    <td width="6%" class="oabbs_tc">领用数量</td>			   
	     	    <td width="6%" class="oabbs_tc">领用总数</td>
		        <td width="5%" class="oabbs_tc">明细</td>
		        <td width="5%" class="oabbs_tc">修改</td>
		        <td width="5%" class="oabbs_tc">删除</td>
			</tr>
		</thead>
		<tbody id="mybody">
		  #foreach ($log in $applyList)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				
				<td width="4%" class="oabbs_tc"> 
					<input type="checkbox" name="keyId"  value="$!log.id"/> 
				</td>
				<td><input type="hidden" value="APPLYALL"/> </td>
				<td class="oabbs_tc"><input id="order" name="order" style="text-align:center" readonly="readonly"></td>
				
				
				<td class="oabbs_tc">$!log.applyNO&nbsp;</td>
				
				<td class="oabbs_tc">$!log.applyRole&nbsp;</td>
			 	<td class="oabbs_tc"> $!log.applyDate&nbsp;</td>
			 	<td >
				#foreach ($logdet in $log.applyGoodsDetBean)
				<span class="item_sp">$!logdet.goodsName</span>
				#end 
				</td>
				<td >
				#foreach ($logdet in $log.applyGoodsDetBean)
				<span class="item_sp">$!logdet.type</span>
				#end 
				</td>
				<td >
				#foreach ($logdet in $log.applyGoodsDetBean)
				<span class="item_sp">$!logdet.unit</span>
				#end 
				</td>
				<td align="right">
				#foreach ($logdet in $log.applyGoodsDetBean)
				<span class="item_sp">$!logdet.applyQty</span>
				<input type="hidden" name="flagBack_${velocityCount}" value="$!logdet.back_sign" />
				#end 
				</td>			 	
				<td class="oabbs_tc"> $!log.apply_qty&nbsp;</td>
		        <td class="oabbs_tc" >	
		        <a href="javascript:" id="allBtn" class="allBtn" title="明细" ><span style="color:#0000ff;">明细</span></a>	    
				</td>
				<td width="5%" class="oabbs_tc">	
				#if($update)	    
				<a href="javascript:" id="update_Btn" class="update_Btn" title="修改" ><span style="color:#0000ff;">修改</span></a>
				#end
				</td>
				<td width="5%" class="oabbs_tc">
				#if($del)		    
				<a href="javascript:deltable('$!log.id')" title="删除"><span style="color:#ff0000;font-size:14px;">X</span></a>
				#end
				</td>
				<td >
				<input type="hidden"  name="goodsId" id="goodsId" value="$!log.id"/>
				</td>
				
			</tr>
			#end
		  </tbody>
		</table>
	</div>
<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
<div id="Dvw" class="search_popup" style="display:none;height: 200px;top: 150px;left: 33%;">
		<div id="Divtitle" style="cursor: move;">
			<span class="ico_4"></span>领用条件查询
		</div>
		<form method="post" id="myform" name="myform" action="/ApplyGoodsAction.do?operation=4&GoodsNO=condition">				 
			<table style="margin-top:10px;">			
				<tr>
					<td align="right"><span style="margin-left: 15px;">用品名称：</span></td>
					<td ><input size="15" id="apply_title" name="apply_title" type="text" value="$!apply_title" /></td>
					<td align="right">领用人：</td>
					<td >
						<input name="applyRole"  id="applyRole" size="15" value="$!applyRole" />
				    </td>	
				</tr>
				<tr>
					<td align="right">日期：</td>
					<td >
						<input name="beginTime"  id="beginTime" size="15" onkeydown="if(event.keyCode==13) openInputDate(this);"
						value="$!beginTime" onClick="openInputDate(this);" />
				    </td>
					<td align="center"> 至	</td>
					<td>					
						<input name="endTime"  id="endTime" size="15" onkeydown="if(event.keyCode==13) openInputDate(this);"
						value="$!endTime" onClick="openInputDate(this);" />					
					</td>
				</tr>			
			</table>
			<span class="search_popup_bu" >
			<input type="button" onClick="subForm();" class="bu_1" />
			<input type="button" onclick="closeSearch();" class="bu_2" /></span>	
</form>
			<script language="javascript">
				function showSearch(){					
					if($('#Dvw').css("display")== "none")
					 $('#Dvw').show();
					else
					 $('#Dvw').hide();
				}
				
				function closeSearch(){
					$('#Dvw').hide();
				}
				function subForm(){
					myform.submit();
					closeSearch();
				}
				
var posX;
var posY;       
fdiv = document.getElementById("Dvw");         
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
</body>

</html>