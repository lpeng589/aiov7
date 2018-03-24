<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<base target="_self"></base>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>会计科目</title>
<link rel="stylesheet" href="/style1/css/oa_news.css" />
<link rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" type="text/css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>
<script language="javascript"> 
var zTree1;
var setting;
var zNodes;
#if("$!folderTree"=="")
	zNodes = [];
#else
	zNodes=$!folderTree;
#end
setting = {
	checkable : true,
	expandSpeed : false,
	checkType : {"Y":"s", "N":"ps"}, 
	showLine: true,
	callback: {
		change:	zTreeOnChange,
		dblclick: zTreeOnDBlClick,
		click: zTreeOnClick
	}
};

var datas = "";
function zTreeOnClick(event, treeId, treeNode){
	if(treeNode!=null){
		#if("$!selectType"=="all")
			#if("$!isParent"=="false")
				if(!treeNode.isParent){
					datas = treeNode.tname;
				}
			#else
				datas = treeNode.tname;
			#end
		#elseif("$!selectType"=="chooseChild")
			if(!treeNode.isParent){
				datas = treeNode.tname;
			}
		#end
		
	}
}

function zTreeOnDBlClick(event, treeId, treeNode) {
	if(treeNode!=null){
		#if("$!selectType"=="all")
			#if("$!isParent"=="false")
				if(!treeNode.isParent){
					window.returnValue = treeNode.tname;
					window.close();
				}
			#else
				window.returnValue = treeNode.tname;
				window.close();
			#end
		#elseif("$!selectType"=="chooseChild")
			if(!treeNode.isParent){
				window.returnValue = treeNode.tname;
				window.close();
			}
		#end
		
	}
}

function zTreeOnChange(event, treeId, treeNode) {
	var tmp = zTree1.getChangeCheckedNodes();
	var tmparr = "";
	var strarr = "";
	for (var i=0; i<tmp.length; i++) {
		if(tmp[i].checked){
			tmparr += tmp[i].tname;
		}
	}
	datas = tmparr;
}

$(document).ready(function(){
	$(".classdiv2 table tbody tr").hover(
		function () {
			$(this).addClass("mulTr");
		},
		function () {
			$(this).removeClass("mulTr");
		}
	);
	$("#keywordSearch").focus();
	reloadTree();
});

function reloadTree(node) {
	var setting1 = clone(setting);
	setting1.treeNodeKey = "id";
	setting1.isSimpleData = true;
	zNodes1 = clone(zNodes);
	setting1.showLine = true;
	
	zTree1 = $("#treeDemo").zTree(setting1, zNodes1);
	var nodes = zTree1.getNodes();
}	

function clears(){
	window.returnValue = "";
	window.close();
}

function querys(){
	if($("#keywordSearch").val()=="" && $("#accCodeSearch").val()==""
		&& $("#accNameSearch").val()=="" && $("#accFullNameSearch").val()==""){
		$("#classCode").remove();
	}else{
		$("#classCode").val('');
	}
	$("#strValue").val('');
	form.submit();
}

function closes(){
	window.close();
}

function submits(){
	if(datas == ""){
		alert("$text.get("oa.popup.select.one")") ;
		return false;
	}
	window.returnValue = datas;
	window.close();
}

function returns(classCode,value){
	$("#classCode").val(classCode);
	$("#strValue").val(value);
	form.submit();
}

</script>
<style type="text/css">
.bu_2{background: url(/style1/images/oaimages/Top_button.jpg); HEIGHT: 19px ;width: 45px;color: white;}
.ulclass li{height: 30px;float: left;padding-left: 10px;}
.classdiv1{margin-left: 5px;border: #dedede 1px solid;width: 100%;background: white;padding-top: 10px;}
.classdiv2{margin-left: 5px;width: 98%;background: white;}
.trs{background: url(/style/images/client/bg.gif) repeat-x 0px -78px;text-align: center;}
table tr{height:28px;}
#ulclass input{border:solid 1px #CFCFCF;}
.classdiv2 table{width: 100%}
.classdiv2 table tr{border-collapse: collapse;}
.mulTr{background-color: #C2F6D1;}
a:visited{color: blue;}
a:link{color:blue;}
</style>
</head>

<body scroll="no" style="background: #f5f5f5;">
<form method="post" scope="request" name="form" action="/VoucherManageAction.do">
<input name="optype" id="optype" value="queryAccCode" type="hidden"/>
<input name="classCode" id="classCode" value="$!classCode" type="hidden"/>
<input name="strValue" id="strValue" value="$!strValue" type="hidden"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/style/images/desktop/msg_icon_3.jpg" align="left"/>
	<h3 style="font-size: 12px;font: bold 14px '宋体';padding-top: 10px;">请选择会计科目</h3>
	<div style="float: right;">
	<input type="button" class="bu_2" value="查询" onclick="querys()"/>
	<input type="button" class="bu_2" value="清空" onclick="clears()"/>
	<input type="button" class="bu_2" value="确定" onclick="submits()"/>
	<input type="button" class="bu_2" value="关闭" onclick="closes()"/></div></div>
</div>
<div class="classdiv1">
	<ul class="ulclass" id="listid">
		<li><span>关键字搜索：</span><input name="keywordSearch" id="keywordSearch" value="$!keywordSearch" onKeyDown="if(event.keyCode==13) {querys()}"/></li>
		<li><span>会计科目：</span><input name="accCodeSearch" id="accCodeSearch" value="$!accCodeSearch"  onKeyDown="if(event.keyCode==13) {querys()}"/></li>
		<li><span>&nbsp;&nbsp;科目名称：</span><input name="accNameSearch" id="accNameSearch" value="$!accNameSearch"  onKeyDown="if(event.keyCode==13) {querys()}"/></li>
		<!-- <li><span>&nbsp;&nbsp;科目全称：</span><input name="accFullNameSearch" id="accFullNameSearch" value="$!accFullNameSearch"  onKeyDown="if(event.keyCode==13) {querys()}"/></li> -->
	</ul>
</div>
	<div class="scroll_function_big" style="height: 10px;">
		<span><!-- 当前位置：
			<a href="/VoucherManageAction.do?optype=queryAccCode">根目录</a> >> 
			#if($!strValue.indexOf("|") > 0)
				#set($strs = "")
				#foreach ($str in $globals.strSplit($!strValue,'|'))
					#set($strs = $strs+$str+"|")
					#set($s = $globals.strSplit($str,';'))
					<a href="javascript:returns('$globals.get($s,0)','$strs')">$globals.get($s,1)</a> >> 
				#end
			#end -->
		</span></div>
	<div class="classdiv2" style="overflow-x:hidden;overflow-y:auto;height: 300px;padding-left: 20px;" id="computeDiv">
		<!-- <table border="#dedede 1px solid;" style="border-collapse: collapse;">
			<tr class="trs">
				<td width="6%">No.</td>
				<td width="6%"><input name="selAll" id="selAll" onClick="checkAll('keyId')" type="checkbox"/></td>
				<td width="20%">会计科目</td>
				<td width="25%">科目名称</td>
				<td width="25%">科目全称</td>
				<td width="10%">显示下级</td>
			</tr>
			<tbody>
			#set($count=1)
			#foreach($accMain in $accMainList)
			<tr ondblclick="showData('$!accMain.classCode','$!accMain.counts','$accMain.AccName')">
				<td align="center" width="6%">$count</td>
				<td align="center" width="6%"><input name="keyId" id="keyId" value="$!accMain.classCode" type="checkbox"/></td>
				<td title="$accMain.AccNumber">$!globals.subTitle($accMain.AccNumber,30)</td>
				<td title="$!accMain.AccName">$!globals.subTitle($!accMain.AccName,30)</td>
				<td title="$!accMain.AccFullName">$!globals.subTitle($!accMain.AccFullName,30)</td>
				<input name="isjunior_$!accMain.classCode" id="isjunior_$!accMain.classCode" value="$!accMain.counts" type="hidden"/>
				<input name="accNumber_$!accMain.classCode" id="accNumber_$!accMain.classCode" value="$accMain.AccNumber;$!accMain.AccName;|" type="hidden"/>
				<td align="center" width="10%">
				#if($!accMain.counts!=0)
					<a href="javascript:void(0);" style="color: blue;" onclick="showData('$!accMain.classCode','$!accMain.counts','$accMain.AccName')">下级</a>
				#else
					<span style="color: blue;">无子类</span>
				#end</td>
			</tr>
			#set($count = $count+1)
			#end
			</tbody>
		</table> -->
		<ul id="treeDemo" class="tree">
			#if("$!folderTree"=="")
				暂无数据
			#end
		</ul>
	</div>
</form>
</body>
</html>
