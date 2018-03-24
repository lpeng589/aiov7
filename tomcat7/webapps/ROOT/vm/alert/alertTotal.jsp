<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预警汇总</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/style/css/base_button.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/workflow2.css" />
<link rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link type="text/css" href="/style1/css/fg.menu.css" media="screen" rel="stylesheet" />
<link type="text/css" href="/style1/css/ui.all.css" media="screen" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/gen/listPage.vjs_zh_CN.js?1357894434000"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/financereport.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/fg.menu.js"></script>
 
<style type="text/css">
body{background: none;}
.td_occd1{width: 3%;text-align: center;}
.td_occd8{width: 6%;}
.td_occd2{width: 5%;}
.td_occd3{width: 5%;}
.td_occd4{width: 10%;}
.td_occd5{width: 15%;}
.td_occd7{width: 5%;}
.data_list_head{	text-align: center;}
.framework td{padding-left: 20px;}
.btnlp{margin:6px;float: left;cursor: pointer;}
.search_popup input{border: 1px solid #b4b4b4;}
.TopTitle{margin-left: 8px;}
.mouseover{border: 1px gray solid;}
.search_popup{z-index: 99;margin:-200px 0px -200px -220px;display:none;
		#if("$!AccMainSetting.isAuditing"=="1" || "$!globals.getSysSetting('standardAcc')"=="true")
			height:80%;
		#else
			height:70%;
		#end
		width:460px;
		top: 50%;
		left: 50%;
		border: #808080 1px solid;
	}
.search_popup table div{line-height: normal;padding-left: 0px;background: none;width: auto;font-weight: normal;border: 1px solid #b4b4b4;padding: 0px 0px 5px 10px;height: auto;}
.search_popup table div input{margin-top: 5px;}
.tdDes1{text-align: right;}
.tdDes2{text-align: center;}
.tdDes3{text-align: left;}
label{font-family:微软雅黑;margin:0;padding:0;font-size:12px;}
.a-width{width:80px;height:10px;}
.data_list_head{box-sizing:border-box;border:1px #ccc solid;}
.list_table{width:99%;box-sizing:border-box;line-height:30px;table-layout:fixed;border-bottom:1px #ccc solid;border-left:1px #ccc solid;}
.list_table tr td{overflow:hidden;text-overflow:ellipsis;white-space:nowrap;border-top:1px #ccc solid;border-right:1px #ccc solid;}
a.ui-corner-all{font-family:微软雅黑;}
a.fg-button{border:1px solid #C4C5C3;}
.td_center{text-align: center;}
</style>

<script type="text/javascript">
</script>
<script type="text/javascript">


//刷新
function refurbish(){
	window.location.reload();
}

function mdiwin(url,title){
	top.mdiwin(url,title);
}

</script>
</head>
 
<body >
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" id="form" name="form" action="/AlertTotalAction.do" target="">
		<table cellpadding="0" cellspacing="0" border="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<img src="/style1/images/workflow/ti_003.gif"/>
						<span style="font-size: 14px;float: none;">预警汇总表</span>
						<span style="float: right;font-weight: normal;margin-bottom: 4px;">
							<input type="button" class="bu_02" onClick="refurbish()" value="刷新"/>
						</span>
					</div>
					<table class="data_list_head" cellpadding="0" cellspacing="0" style="table-layout:fixed;" >
						<thead>
							<tr>
								<td width="40px">序号</td>
								<td width="500px">预警名称</td>
								<td width="120px">预警数量</td>
							</tr>
						</thead>
					</table>
					<div id="data_list_id" style="overflow:hidden;overflow-y:auto;width:100%;" >
						<script type="text/javascript"> 
							var oDiv=document.getElementById("data_list_id");
							var sHeight=document.documentElement.clientHeight-125;
							oDiv.style.height=sHeight+"px";
						</script>
						<table class="list_table" cellpadding="0" cellspacing="0" border="0">					
							<tbody>
								#set($counts = 1)
								#foreach($alert in $alertlist)
								<tr>
									<td width="40px" class="td_center">$!counts</td>
									<td width="500px">$!alert.alertName</td>
									#set($url = "/ReportDataAction.do?reportNumber=$!{alert.reportNumber}&$!{alert.condition}&LinkType=@URL:")
									#if("$!alert.BillTable"!="")
										#set($url = $url + "&detTable_list=$!alert.BillTable")
									#end
									#if("$!alert.mainNumber"!="")
										#set($url = $url + "&mainNumber=$!alert.mainNumber")
									#end
									<td width="120px" class="td_center"><a href="javascript:mdiwin('$url','$!alert.modelName')" style="color: blue;">$!alert.count</a></td>
								</tr>
								#set($counts = $counts + 1)
								#end
							</tbody>
						</table>
						#if($alertlist.size()==0)
						<div style="text-align: center;width: 50%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">
							暂无预警信息
						</div>
						#end
					</div>
					<div class="listRange_pagebar">
						$!pageBar
					</div>
			</td>
			</tr>
			</table>
		</form>
	</body>
	</html>