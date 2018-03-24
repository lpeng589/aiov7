<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>group</title>
<link type="text/css"  rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css"  rel="stylesheet" href="/vm/oa/album/ui/ztree/demoStyle/demo.css"/>
<link type="text/css"  rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<link type="text/css"  rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css"  rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>

<script type="text/javascript">
	$(document).ready(function() {		
		$("#savebt").click(function(){
			submitCerfi();
		});
	});
	
function changeBill(tempNumber,tableName,popupName,tableDisplay)
{
	$(".ico_1",document).attr("class","ico_No");
	$("#lm_"+tempNumber+" > span ",document).attr("class","ico_1");
	$("#tempNumber").val(tempNumber);
    jQuery("#firameMain").attr("src","/UserFunctionAction.do?operation=22&tableName="+tableName+"&selectName="+popupName+"&MOID=$MOID&MOOP=query&noOp=true&displayName="+encodeURI(tableDisplay));
    
    
}

function setCwinHeight(){	
	var oDiv=document.getElementById("leftconter");
	var sHeight=document.body.clientHeight-48;
	document.all['pindex'].style.height=(sHeight -10)+"px";  
}
function submitCerfi(){ 
	////<input name="varKeyId" onclick="this.checked=!this.checked" type="checkbox" CHECKED="checked" value="47ac85aa_1311131124057820010;"/>
	keyIds="";
	
	ks = window.frames["firameMain"].document.getElementsByName("varKeyId");
	for(i=0;i<ks.length;i++){ 
		if(ks[i].checked){
			keyIds+=ks[i].value+"#||#";
		}
	}
	if(keyIds==""){
		alert("请选择至少一个要生成凭证的单据");
		return;
	}
	$("#keyIds").val(keyIds);  
	
	document.form.submit();
	
}
$(function(){
	$(".pindex_wp").css("width",$(document.body).outerWidth(true)-220);
});

</script>

<style type="text/css">
div.zTreeDemoBackground {width:auto;}
ul#treeDemo {width:auto;overflow:auto;}
#keyWord{font-family:寰蒋闆呴粦;font-size:12px;width:139px;}
.leftMenu2{height:27px;padding:0 0 0 0;line-height:27px;font-family:寰蒋闆呴粦;font-size:12px;}
.tree li a{font-family:寰蒋闆呴粦;}
.leftMenu2>div>em{display:inline-block;float:left;margin:0 5px 0 0;font-family:寰蒋闆呴粦;}
.leftMenu2>div>em.wt{color:red;}
.ico_1{margin:9px 5px 0 5px;}
.ico_No{margin:9px 5px 0 5px;}

.tree_w>ul{padding:10px 5px 0 5px;margin:0;font-family:寰蒋闆呴粦;}
.tree_w>ul li{line-height:18px;float:left;display:inline-block;font-size:14px;padding:5px;background:#CBE6FF;margin:0 0 1px 1px;}
.tree_w>ul li:hover{background:#2E85C0;}
.tree_w>ul li:hover a{color:#fff;}
</style>
</head>
<body style="text-align: left;overflow:hidden;">
<form id="form" name="form" action="/GenCertificateAction.do">
	<input type="hidden" name="tempNumber" id="tempNumber" value="">
	<input type="hidden" name="tableDisplay" id="tableDisplay" value="">
	<input type="hidden" name="tid" id="tid" value="">
	<input type="hidden" name="op" id="op" value="gen">
	<input type="hidden" name="keyIds" id="keyIds" value="">
	
	<table cellpadding="0" cellspacing="0" id="heart_id" class="frame2" width="100%" >
<script type="text/javascript"> 
	var oDiv=document.getElementById("heart_id");
	var sHeight=document.documentElement.clientHeight-0;
	oDiv.style.height=sHeight+"px";
	$("#firameMain").css("height",(sHeight)+"px");
</script>	
		<tr><td colspan=2 align="right" style="height:30px;padding:10px 30px 0 0; ">
			<div class="TopTitle" style="float:right;width:55px">
				<input type="button" class="btnlp" id="savebt" value='生成凭证'/>
			</div>
			<div class="TopTitle" style="float:right;width:60px">
				<select name="certificateType" style="margin-right:10px">
				<option value="2">合并</option>
				<option value="1">按单</option>
				
				</select>
			</div>
			
			
			
		</td></tr>
		<tr>
			<td width="120px" style="border-right:1px #a1a1a1 dashed;border-top:1px #a1a1a1 dashed;padding:0 5px 0 0;" valign="top">
				<div class="leftModule" style="padding:3px 0 0 0;margin:0;border: 0px;height:400px; overflow-x:hidden; overflow-y:auto">
				<div class="leftMenu" id="t_lm" >
					#foreach($row in $billList) 
					<div class="leftMenu2" id="lm_$!row.id" onclick="changeBill('$row.tempNumber','$row.tableName','$row.popupName','$row.tempName');">
						<span class="ico_No" id=""></span>$!row.tempName
					</div>
					#end		
				</div> 
				</div>
			</td>
			<td valign="top" class="list" id="listTd" style="padding:0 0 0 5px;border-top:1px #a1a1a1 dashed;padding:0 5px 0 0;">
				<iframe id="firameMain"  frameborder=false src="" name = "firameMain" style="margin-top:3px;height:530px;" scrolling="no" width="99%"></iframe>
			</td>
		</tr>
	</table>
	</form>
</body>
</html>

