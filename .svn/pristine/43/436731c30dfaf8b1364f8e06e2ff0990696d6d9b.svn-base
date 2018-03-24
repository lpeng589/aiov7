<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("mrp.lb.mrpCount")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="$globals.js("/js/mrp.vjs","",$text)"></script>
<script type="text/javascript" src="/js/dragcols.js"></script>
<script type="text/javascript">
function showSon(b){
	var rows = getRows("tblSort");
	for(i=rows.length-1;i>=0;i--){
		if(b){
			rows[i].style.display="";
		}else{
			var tr = rows[i].childNodes;			
			if(tr[2].innerText.replaceAll(" ","")){
				
			}else{
				rows[i].style.display="none";	
				//$("tblSort").deleteRow(i+1);			
			}
		}
	}	
}

function selectLow(chk){
	var chooses = document.getElementsByName("choose");
	var i = parseInt(chk.value);
	var k=i-1;
	chooses[k].value=chk.checked;		
}

function onsub(){
	var flag = false;
	var cbs = document.getElementsByName("cb");
	var cb = new Array();
	for(i=1;i<cbs.length;i++){
		cb[cb.length] = cbs[i].checked;				
	}
	//如果有true，则说明有商品被选中
	if(cb.toString().indexOf("true")!=-1){	
		flag = true;
	}
	if(!flag){
		alert("$text.get('mrp.lb.noChooseGoods')");
		return;
	}
	form.action = "/MrpBP.do?method=bomDemand";
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	form.submit();
}

function checkAllSub(obj){
	var cbs = document.getElementsByName("cb");
	for(i=1;i<cbs.length;i++){
		if(cbs[i].disabled==false){
			if(obj==true){
				cbs[i].checked=true;
			}else{
				cbs[i].checked=obj.checked;
			}
			selectLow(cbs[i]);
		}
	}
}
function jiesuo(){
	if(typeof(top.junblockUI)!="undefined"){
		top.junblockUI();
	}
}
</script>
<style type="text/css">
.resizeDiv{width:2px;overflow:hidden; float:right; cursor:col-resize;}
.listRange_list_function thead td{
	padding-left:1px;
	padding-right:1px;
}
</style>
</head>
<body onLoad="showStatus();checkAllSub(true);sortables_init('tblSort');jiesuo();">
<form method="post" scope="request" name="form" id="form" action="/MrpBP.do?method=bomDemand">
<input type="hidden" name="mrpFrom" value="$!mrpFrom">
<div class="Heading">
	<div class="HeadingIcon">
		<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
	</div>
	<div class="HeadingTitle">$text.get("mrp.lb.mrpBom")</div>
	<ul class="HeadingButton">
		<li><button type="button" onClick="onsub()">$text.get("mrp.lb.nextStep")</button></li>
		<li><button type="button" onClick="location.href='/MrpBP.do?method=orderSel'" class="b2">$text.get("mrp.lb.preStep")</button></li>	
	</ul>
</div>
<div id="listRange_id">
	<div class="listRange_1">
		<li><label for="sonMaterilId" style="margin-left: 20px;">$text.get("mrp.lb.showSonBom")</label><input type="checkbox" id="sonMaterilId" class="cbox" name="sonMatereilOK" onClick="showSon(this.checked)" checked></li>
	</div>
	<div class="scroll_function_small_a" id="conter">
	<script type="text/javascript">
		var oDiv=document.getElementById("conter");
		var sHeight=document.body.clientHeight-92;
		oDiv.style.height=sHeight+"px";
	</script>
	<table border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort">
		<thead>
		<tr style="position:relative;top:expression(this.offsetParent.scrollTop);">
			<td width="30"><input type="checkbox" name="cb" onClick="checkAllSub(this)" value=""></td>
			<td width="120">$text.get("mrp.lb.mrpFrom")</td>
			<td width="110">$text.get("mrp.lb.trackNo")</td>								
			<td width="150">$text.get("mrp.lb.bomNumber")</td>
			<td width="150">$text.get("mrp.lb.bomName")</td>
			<td width="150">$text.get("call.lb.spec")</td>
			#if($globals.getVersion()=="8")
			<td width="80">$text.get("mrp.lb.MaterielAttribute")</td>
			#end
			#foreach($prop in $propList)
			#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
			
			<td width="80">$prop.display.get($!locale)</td>
			#end
			#end
			<td width="80">$text.get("mrp.lb.count")</td>						
			<td width="100">$text.get("mrp.lb.employee")</td>
		</tr>
		</thead>
		<tbody>
			#foreach($row in $list) 
			#if($!row.get("isPlace")=="false"||!$!row.get("isPlace"))
			<tr>
				<td width="30">
					<input type="checkbox" class="cbox" name="cb" value="$velocityCount"#if($!row.get("goodsNumber").indexOf("~")>=0)disabled="disabled"#end onclick="selectLow(this);">
					<input type="hidden" name="choose" value="false">
					<input type="hidden" name="trackNo" #if($!row.get("goodsNumber").indexOf("~")<0) value="$!row.get("trackNo")" #else value="" #end>
					<input type="hidden" name="bomNo" #if($!row.get("goodsNumber").indexOf("~")<0) value="$!row.get("bomNo")" #else value="" #end>
				</td>
				<td>#if($!row.get("goodsNumber").indexOf("~")<0)#if($!mrpFrom=="1")$text.get("mrp.lb.produceOrder")#end #if($!mrpFrom=="2")$text.get("mrp.lb.salesOrder")#end #if($!mrpFrom=="0")$text.get("mrp.lb.mlProduce")#end#end&nbsp;</td>								
				<td>$!row.get("trackNo")&nbsp;</td>								
				<td>$!row.get("goodsNumber")&nbsp;</td>								
				<td>$!row.get("goodsFullName")&nbsp;</td>
				<td>$!row.get("goodsSpec")&nbsp;</td>
				#if($globals.getVersion()=="8")
				#set($count=0)
				#foreach($erow in $globals.getEnumerationItems("MaterielAttribute"))
				#if($erow.value==$!row.get("MaterielAttribute"))
				<td>$erow.name&nbsp;</td>
				#set($count=1)
				#end
				#end
				#if($count==0)		
				<td>&nbsp;</td>	
				#end
				#end							
				#foreach($prop in $propList)
				#if($prop.isUsed==1&&!$!prop.propName.equals("Seq")&&$globals.getFieldBean("tblBOM",$!prop.propName))
				
				<td>$!row.get($prop.propName)&nbsp;</td>
				#end
				#end
				#if($!row.get("goodsNumber").indexOf("~")<0)
				<td align="right"><input type="hidden" name="count" value="$!row.get("count")">$!globals.formatNumber($!row.get("count"),false,false,$!globals.getSysIntswitch(),"tblBOM","Qty",true) &nbsp;</td>
				#else
				<td align="right"><input type="hidden" name="count" value="$!row.get("qty")">$!globals.formatNumber($!row.get("qty"),false,false,$!globals.getSysIntswitch(),"tblBOM","Qty",true) &nbsp;</td>
				#end		
				<td>$!row.get("empFullName")&nbsp;</td>									
			</tr>
			#end
			#end		
			</tbody>
		</table>
		</div>
	</div>
</form>
</body>
</html>
