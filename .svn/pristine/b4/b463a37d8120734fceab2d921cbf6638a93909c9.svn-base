<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/workflow.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">


var curRow;
var curRowNo; 
function focusRow(obj){
	curRow = obj;
	curRowNo = jQuery.inArray(obj,$("#FieldInfoId tbody",document).find("tr"));
	$(".focusRow",document).removeClass("focusRow");
	$(curRow).addClass("focusRow");	
}

function delRow(mydobj){
	if(confirm("确定删除本行吗?")){
		$(curRow).remove();
		curRow = null;
		row = 1;
		$("#FieldInfoId tbody",document).find("tr").each(function (){
			$(this).find("td:eq(0)").text(row);
			row ++;
		});
	}
}
function initdefine(){	
	str =parent.$("input[name=calculate]:eq("+parent.curRowNo+")").val();
	
	strs = str.split(";");
	row = 1;
	for(i=0 ;i <strs.length;i++){
		row = $("#FieldInfoId tbody",document).find("tr").size()+1;
		rowHtml = '<td >'+row+'</td>'+
	   			  '<td ><input type="text" name="calc" value="'+strs[i]+'"/></td>';	      
	   	rowHtml +='<td ><a href="javascript:delRow(this)">删除</a></td>';	  
	   	rowHtml = '<tr  class="c1" onclick="focusRow(this)" >'+rowHtml+'</tr>';		
	   	$("#FieldInfoId tbody",document).append(rowHtml);  
	   	row ++;
	}
}
function addRow(){
	row = $("#FieldInfoId tbody",document).find("tr").size()+1;
	rowHtml = '<td >'+row+'</td>'+
   			  '<td ><input type="text" name="calc" value=""/></td>';	      
   	rowHtml +='<td ><a href="javascript:delRow(this)">删除</a></td>';	  
   	rowHtml = '<tr  class="c1" onclick="focusRow(this)" >'+rowHtml+'</tr>';		
   	$("#FieldInfoId tbody",document).append(rowHtml);  
}
function getdefineOp(){
	ret = '';
	rowNum = 0;
	error = false;
	$("input[name=calc]",document).each(function(){
		if($(this).val() != "" &&　!error){
			ret	 +=	$(this).val() +';';
		}
		rowNum ++;
	});
	if(error){
		return "error";
	}		
	return ret;
}

</script>
<style type="text/css">

#FieldInfoId input{background:none;border:0px;width: 100%;height: 100%;}
#FieldInfoId select{background:none;border:0px;width: 100%;height: 100%;}
#FieldInfoId thead td{border-right:1px solid #999;border-bottom:1px solid #999;line-height: 33px;}
#FieldInfoId tbody td{border-right:1px solid #999;border-bottom:1px solid #999;position: relative;}

.data_list .dataListUL{margin:0px 5px 0px 5px;line-height:33px;border: 1px solid #999;min-height:67px}
.data_list .dataListUL li{ border-bottom: 1px solid #999;  }
.data_list .dataListUL .titleLi{ background:url(/style1/images/workflow/data_list_head_bg.gif); height:33px;padding-left:10px }
.data_list .dataListUL input,.data_list .dataListUL select{width: 117px;margin-left: 5px;}
.data_list .dataListUL label{width: 48px;margin-left: 5px;display: inline-block;}
.data_list table{width:700px;}
.data_list table td{overflow:hidden;text-align:left;}
.tstBtn{background-image: url(/style/images/client/icon16.png);background-position: -32px 0;width: 16px;height: 16px;position: absolute;cursor: pointer;right: 1px;top: 8px;}



.focusRow{background-color:#9AF850;}
</style>
</head>
<body onload="initdefine()">

	
	<table cellpadding="0" cellspacing="0" border="0" class="framework" >
		<tr>
			<td>
				<div class="TopTitle">
					<span style="margin-top: 4px;margin-bottom: 12px;">
					<input type="button" class="bu_02" onclick="addRow()" value="增加一行" />	
					</span>
				</div>
				<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;" >	
					<div id="data_list_idTabXa" style="overflow-y:auto;padding: 0px;margin: 0px;height: 100%;width: 100%;background: none;">
					<script type="text/javascript">
					var oDiv=document.getElementById("data_list_id");
					var sHeight=document.documentElement.clientHeight-60;
					oDiv.style.height=sHeight+"px";					
					</script>
					<table cellpadding="0" cellspacing="0" style="border-left: 0px;table-layout:fixed;width:99%" id="FieldInfoId">	
						<thead>
							<tr >
								<td width="30">序号</td>
								<td width="*">公式</td>
								<td  width="100px" >操作</td>	
							</tr>
						</thead>
						<tbody>
							
						</tbody>
					</table>
					</div>
				</div>
			</td>				
		</tr>
	</table>
<embed type="application/np-print" style="width:0px;height:0px;" id="plugin"/>	
</body>
</html>
