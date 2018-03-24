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

		$("#addbt").click(function(){
			jQuery("#firameMain").attr("src","/CertTemplateAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex&");
		});
		$("#savebt").click(function(){
			document.getElementById("firameMain").contentWindow.save();
		});
		$("#delbt").click(function(){
			if(confirm("确认删除模板吗？这可能导致做单时报错")){
				id = document.getElementById("firameMain").contentWindow.document.form.id.value;
				if(id != ""){
					location.href="/CertTemplateAction.do?operation=$globals.getOP("OP_DELETE")&winCurIndex=$!winCurIndex&id="+id;
				}
			}
		});
	});
	function tbClick(tempNumber,tempName){
		$(".ico_1",document).attr("class","ico_No");
		$("#lm_"+tempName+" > span ",document).attr("class","ico_1");
		
		jQuery("#firameMain").attr("src","/CertTemplateAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&winCurIndex=$!winCurIndex&tempNumber="+tempNumber);
	}

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
	<table cellpadding="0" cellspacing="0" id="heart_id" class="frame2" width="100%" >
<script type="text/javascript"> 
	var oDiv=document.getElementById("heart_id");
	var sHeight=document.documentElement.clientHeight-0;
	oDiv.style.height=sHeight+"px";
	firameMain.style.height=(sHeight)+"px";
</script>	
		<tr><td colspan=2 align="right" style="height:40px;padding:10px 30px 0 0; ">
			<div class="TopTitle" style="float:right;width:35px">
				<div class="btnlp" id="delbt"><img src="/style/themes/icons/no.png" title="删除凭证模板"/><br />删除</div>
			</div>
			<div class="TopTitle" style="float:right;width:35px">
				<div class="btnlp" id="savebt"><img src="/style/themes/icons/filesave.png" title="保存凭证模板"/><br />保存</div>
			</div>
			<div class="TopTitle" style="float:right;width:35px">
				<div class="btnlp" id="addbt"><img src="/style/plan/M_3.gif" title="新增凭证模板"/><br />新增</div>
			</div>
			
		</td></tr>
		<tr>
			<td width="120px" style="border-right:1px #a1a1a1 dashed;border-top:1px #a1a1a1 dashed;padding:0 5px 0 0;" valign="top">
				<div class="leftModule" style="padding:3px 0 0 0;margin:0;border: 0px;height:400px; overflow-x:hidden; overflow-y:auto">
				<div class="leftMenu" id="t_lm" >
					#foreach($row in $result)
					<div class="leftMenu2" id="lm_$!row.tempNumber" onclick="tbClick('$!row.tempNumber','$!row.tempName')">
						<span class="ico_No" id=""></span>$!row.tempName
					</div>
					#end		
				</div> 
				</div>
			</td>
			<td valign="top" class="list" id="listTd" style="padding:0 0 0 5px;border-top:1px #a1a1a1 dashed;padding:0 5px 0 0;">
				<iframe id="firameMain"  frameborder=false src="/CertTemplateAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&winCurIndex=$!winCurIndex&id=" name = "firameMain" style="margin-top:3px;height:530px;" scrolling="no" width="99%"></iframe>
			</td>
		</tr>
	</table>
</body>
</html>
