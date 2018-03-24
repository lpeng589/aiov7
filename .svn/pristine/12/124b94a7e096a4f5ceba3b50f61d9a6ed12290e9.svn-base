<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加车辆审批人</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/listPage.vjs"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
var fieldNames;
var fieldNIds;
var flag;
function deptPop(popname,fieldName,fieldNameIds,flag){
	var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
	var titles = "请选择部门";
	if(popname == "userGroup"){
		titles = "请选择个人"
	}
	fieldNames = fieldName;
	fieldNIds = fieldNameIds;
	flag=flag;
	asyncbox.open({
	id : 'Popdiv',
	 title : titles,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 top: 5,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。


　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。

				var employees = opener.strData;
				newOpenSelect(employees,fieldName,fieldNameIds,flag)
　　　　　	}
　　　	}
　	});
}
function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds,flag);
	parent.jQuery.close('Popdiv');
}

function newOpenSelect(employees,fieldName,fieldNameIds,flag){
	var id = jQuery("#formatFileName").val();
	var checkId = jQuery("#checkId").val();
	var employees = employees.split("|") ;
	var name = "";	
	var nameId = "";	
	for(var j=0;j<employees.length-1;j++){
		var field = employees[j].split(";") ;		
		if(field!=""){				
			if(checkId.indexOf(field[0])<0){
				name += field[1]+";";
				nameId += field[0]+";";
			}
		}
	}		
	document.getElementById("formatFileName").value=name+id;
	document.getElementById("checkId").value=nameId+checkId;	
}
function save(){	
	form.submit();		
}
function redel(){
	document.getElementById("formatFileName").value="";
	document.getElementById("checkId").value="";
}
</script>
<style type="text/css">
input{margin-left:0;}
</style>
</head>
<body>
<form method="post" name="form" id="form" action="/CarInfoAction.do">
<input type="hidden" name="operation" value="$globals.getOP('OP_CHECK')"/>
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">添加车辆审批人</div>
	
</div>
<div id="listRange_id">
	<div  id="conter" style="margin-top:10px;" align="center">
		<table width="90%" border="0" cellpadding="0" align="center" cellspacing="0" id="firtTbl" >
			<tr>
				<td>车辆审批人员：</td>
				<td>
				<input type="hidden" name="checkId" id="checkId" value="$!nameId"/>
				<input type="text" name="checker" style="width:250px;height:100px;"  readonly="readonly" id="formatFileName" value="$!name" />
				<img src="/style1/images/St.gif" 
				onclick="deptPop('userGroup','formatFileName','formatFileName');"class="search" />	
				</td>
			</tr>
		</table>
		<div class="btn btn-small" title="保存" onclick="save();">保存</div>
		<div class="btn btn-small" title="清空" onclick="redel();">清空</div>
	</div>
</div>
</form>
</body>

</html>