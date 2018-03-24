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

function closeForm(){
	parent.jQuery.close("$popWinName");

}

function getSelect(){
	ret = "";
	$("input[name=keyId]:checked",document).each(function(){
		ret +=$(this).val()+";";
	});
	return ret;
}

</script>
<style type="text/css">
.f-icon16{background-image:url(/style/v7/images/function_icon22.png);background-repeat:no-repeat;}
.detbtBar{float:left;margin: 5px 0px 3px 10px;/*border:1px solid #c2c2c2; */border-bottom: none;} 
.detbtBar>li{width:22px;height:22px;cursor:pointer;margin:1px 2px 2px;float:left}
#b_addline{background-position:0 -66px;}
#b_addline:hover{background-position:-22px -66px;}
#b_delline{background-position:0 -88px;}
#b_delline:hover{background-position:-22px -88px;}
#b_upline{background-position:0 -153px;}
#b_upline:hover{background-position:-22px -153px;}
#b_downline{background-position:0 -175px;}
#b_downline:hover{background-position:-22px -175px;}
#FieldInfoId input{background:none;border:0px;width: 100%;height: 100%;}
#FieldInfoId select{background:none;border:0px;width: 100%;height: 100%;}
#FieldInfoIdTitle thead td{border-right:1px solid #999;white-space: nowrap;overflow: hidden;}
#FieldInfoId tbody td{border-right:1px solid #999;border-bottom:1px solid #999;position: relative;}

.childTable  thead td{border-right:1px solid #999;border-bottom:1px solid #999;height:33px;white-space: nowrap;overflow: hidden;background-color: #EFEAF8;vertical-align: middle;padding-left:10px}
.childTable  tbody td{border-right:1px solid #999;border-bottom:1px solid #999;position: relative;padding-left:10px}

.data_list .dataListUL{margin:0px 5px 0px 5px;line-height:33px;border: 1px solid #999;min-height:67px}
.data_list .dataListUL li{ border-bottom: 1px solid #999;  }
.data_list .dataListUL .titleLi{ background:url(/style1/images/workflow/data_list_head_bg.gif); height:33px;padding-left:10px }
.data_list .dataListUL input,.data_list .dataListUL select{width: 117px;margin-left: 5px;}
.data_list .dataListUL label{width: 48px;margin-left: 5px;display: inline-block;white-space: nowrap;}
.data_list table{width:780px;}
.tstBtn{background-image: url(/style/images/client/icon16.png);background-position: -32px -16px;width: 16px;height: 16px;position: absolute;cursor: pointer;right: 1px;top: 8px;}
.mainUl{margin:0px 5px 35px 5px;line-height:33px;}
.mainUl li{float:left;width:300px   }
.mainUl li span{ width:100px ;text-align:right;  display: inline-block;margin:0px 5px 0px 5px }
.mainUl li input{ width:160px ;padding:0px;margin:0px }
.mainUl li select{ width:162px ; padding:0px;margin:0px}

.focusRow{background-color:#9AF850;}
</style>
</head>
<body >
<div id="data_brother_id" class="data_list"  style="overflow:hidden;overflow:hidden;width:99%;margin-top: 0px;" >						
	<table cellpadding="0" cellspacing="0" style="border: 1px solid #b4b4b4;border-left: 0px;table-layout:fixed;margin-bottom: 30px;" class="childTable">
		<thead>
			<tr>
				<td width="40">序号</td>
				<td width="40"></td>
				<td width="200px" >标识</td>
				<td width="*">显示名称</td>	
			</tr>
		</thead>
		<tbody>
		#set($rowc = 1)
		#foreach ($row in $TABLE_INFO.values() )
		#if($row.tableType == 2 )
			<tr id="childList_$globals.get($row,1)">
				<td >$rowc</td>
				<td ><input name="keyId" id="keyId" type="checkbox" style="width:18px;height:18px;margin-top:4px" value="$row.tableName" ></td>
				<td >$row.tableName</td>
				<td >$row.display.get("$globals.getLocale()")</td>	
			</tr>
		#set($rowc = $rowc + 1)	
		#end
		#end	
		</tbody>
	</table>
</div>
</body>
</html>
