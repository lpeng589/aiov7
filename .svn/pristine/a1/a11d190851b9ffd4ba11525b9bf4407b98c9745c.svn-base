<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/workflow.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
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

function dblclick(obj){
	parent.callBackPopup($(curRow).find("td:eq(1)").text().trim(),$(curRow).find("td:eq(2)").text().trim());
}
function updatePopUp(popupName,copy){
	#if("$!from" == "adv")
	vtitle = "弹出窗修改";
	if(popupName ==""){
		vtitle = "弹出窗新建";
	}
	if(copy){
		vtitle = "弹出窗复制";
	}
	width=document.documentElement.clientWidth;
	height=document.documentElement.clientHeight;
	urls = '/CustomAction.do?type=popupUpdate&copy='+copy+'&popupName='+popupName+"&popupWin=popupUpdateWin";
	openPop('popupUpdateWin',vtitle,urls,width,height,true,true);
	#else
		parent.updatePopUp(popupName,copy);
	#end
}
function doAdd(){
	updatePopUp("");
}

</script>
<style type="text/css">

#FieldInfoId input{background:none;border:0px;width: 100%;height: 100%;}
#FieldInfoId select{background:none;border:0px;width: 100%;height: 100%;}
#FieldInfoIdTitle thead td{border-right:1px solid #999}
#FieldInfoId tbody td{border-right:1px solid #999;border-bottom:1px solid #999;position: relative;}

.data_list .dataListUL{margin:0px 5px 0px 5px;line-height:33px;border: 1px solid #999;min-height:67px}
.data_list .dataListUL li{ border-bottom: 1px solid #999;  }
.data_list .dataListUL .titleLi{ background:url(/style1/images/workflow/data_list_head_bg.gif); height:33px;padding-left:10px }
.data_list .dataListUL input,.data_list .dataListUL select{width: 117px;margin-left: 5px;}
.data_list .dataListUL label{width: 48px;margin-left: 5px;display: inline-block;}
.data_list table{width:880px;}
.data_list table td{overflow:hidden;text-align:left;}
.tstBtn{background-image: url(/style/images/client/icon16.png);background-position: -32px 0;width: 16px;height: 16px;position: absolute;cursor: pointer;right: 1px;top: 8px;}



.focusRow{background-color:#9AF850;}
</style>
</head>
<body >

	<form method="post" scope="request" name="form" action="/CustomAction.do?type=popupQuery" class="mytable" >
	<input type="hidden" name="from" value="$!from" />
	<table cellpadding="0" cellspacing="0" border="0" class="framework" >
		<tr>
			<td>
				<div class="TopTitle">
					<span style="margin-top: 4px;margin-bottom: 12px;">
					关键字:<input name=keyword id= keyword value="$!keyword" onkeydown="if(event.keyCode==13) document.form.submit();"/> 
					</span>
					<span style="float: right;font-weight: normal;margin-right: 12px;margin-bottom: 10px;">
						<input type="submit" class="bu_02"  value="查询" />	
						#if("$!from" == "adv")
						<input type="button" onclick="doAdd()" class="bu_02"  value="添加" />	
						#end
					</span>
				</div>
				<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;" >					
					<table cellpadding="0" cellspacing="0" style="border-left: 0px;table-layout:fixed" id="FieldInfoIdTitle">
						<thead>
							<tr >
								<td width="30">序号</td>
								<td width="175px" >标识</td>
								<td width="175px">名称</td>	
								<td  width="400px" >路径</td>	
								<td  width="50px" >修改</td>
								<td  width="50px" >复制</td>
							</tr>
						</thead>
					</table>
					<div id="data_list_idTabXa" style="overflow-y:auto;padding: 0px;margin: 0px;height: 100%;width: 100%;background: none;">
					<script type="text/javascript">
					var oDiv=document.getElementById("data_list_id");
					var sHeight=document.documentElement.clientHeight-60;
					oDiv.style.height=sHeight+"px";					
					</script>
					<table cellpadding="0" cellspacing="0" style="border-left: 0px;table-layout:fixed" id="FieldInfoId">	
						<tbody>
						#foreach($row in $result)
							<tr  class="c1" onclick="focusRow(this)"  ondblclick="dblclick(this)">
								<td  width="30">$velocityCount</td>
								<td width="175px" >
									$row.name
								</td>
								<td width="175px" >
									$row.desc
								</td>
								<td width="400px" >
									#set($pos = $row.path.lastIndexOf(".."))
									#set($pos = $pos + 3)
									$row.path.substring($pos)
								</td>
								<td  width="50px" >
								<a href="javascript:updatePopUp('$row.name','false')" style="text-decoration: underline;color: #2720CF;">修改</a>
								</td>
								<td  width="50px" >
								<a href="javascript:updatePopUp('$row.name','true')" style="text-decoration: underline;color: #2720CF;">复制</a>
								</td>
							</tr> 
						#end	
						</tbody>
					</table>
					</div>
				</div>
			</td>				
		</tr>
	</table>
	</form>
</body>
</html>
