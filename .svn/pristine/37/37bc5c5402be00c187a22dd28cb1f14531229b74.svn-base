<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单据编号规则设置</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/workflow2.css" />
<link rel="stylesheet" href="/style1/css/oa_news.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/gen/listPage.vjs_zh_CN.js?1357894434000"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
 
<script type="text/javascript">
	function addPrepare(){
		window.location.href="BillNo.do?operation=6";					//增加
	}
	function update(key){
		window.location.href="BillNo.do?operation=7&key="+key;			//修改
	}
	//function details(key){
		//window.location.href="BillNo.do?operation=5&key="+key;			//详情
	//}
	function deleteList(id){
		if(id == ""){
			//批量删除
			if(!isChecked('keyId')){
				alert("请选择一条数据！");
				return false;
			}else{
				form.operation.value=3;
				form.submit();
			}
		}else{
			window.location.href="/BillNoAction.do?operation=3&keyId="+id;	
		}
	}
	
	//重置单据编号
	function resets(id){
		if(!confirm("是否要重置单据编号！"))return
		if(!confirm("重置单据编号时一定要保证无单据,不然单据编号可能重复！"))return
		window.location.href="/BillNoAction.do?selectType=resetBillNO&keyId="+id;
	}
	
	
	function checks(){
		//var keySearch=jQuery("#keySearch").val();
		window.location.href='/BillNoAction.do?operation=4';
	}
	function dealBillNo(type,id){
		var title = "";
		var urls = "";
		if(type==1){
			title="添加单据编号规则";
			urls = "BillNo.do?operation=6";
		}else{
			title="修改单据编号规则";
			urls = "BillNo.do?operation=7&key="+id;
		}
		asyncbox.open({
		 title : title,
		 	 id : 'billDiv',
		　　　url : urls,
		　　　width : 900,
		　　　height : 450,
			 btnsbar : jQuery.btn.OKCANCEL,
			 callback : function(action,opener){
	　　　　　	if(action == 'ok'){
					opener.paterns();
					var key = opener.document.getElementById("key");
					var billName = opener.document.getElementById("billName");
					var starts = opener.document.getElementById("start");
					var step = opener.document.getElementById("step");
					var pattern = opener.document.getElementById("pattern");
					var oldkey = opener.document.getElementById("oldkey");
					var validateKey = opener.document.getElementById("validateKey");
					if(key.value == ""){
						alert("模块标识不能为空！");
						key.focus();
						return false;
					}
					if(billName.value == ""){
						alert("模块名称不能为空！");
						billName.focus();
						return false;
					}
					if(pattern.value == ""){
						alert("单据编号规则不能为空！");
						return false;
					}
					if(!isInt(starts.value) || starts.value<0){
						alert("起始流水号必须为大于等于0的正整数!");
						starts.focus();
						return false;
					}
					if(!isInt2(step.value)){
						alert("流水号增量必须为大于0的正整数!");
						step.focus();
						return false;
					}
					if(type==1 || key.value!=oldkey.value){
						if(validateKey.value=="OK"){
							alert("模块标识不能重复！");
							key.focus();
							return false;
						}
					}
					opener.saves(0);
					
					return false;
	　　　　　	}
	　　　	}
	　	});
	}
	
	function dealAsyn(){
		checks();
	}
	
	function details(key){
		asyncbox.open({
		 title : '单据编号规则详情',
		 	 id : 'div',
		　　　url : 'BillNo.do?operation=5&key='+key,
		　　　width : 850,
		　　　height : 400,
			 btnsbar : jQuery.btn.OK,
			 callback : function(action,opener){
	　　　　　	}})
	}
	
	$(document).ready(function(){
		$("#keySearch").focus();
	});
</script>

<style type="text/css">
	.oabbs_tc1{
		width:10%;
		text-align: center;
	}
	.oabbs_tc2{
		width:20%;
		text-align: center;
	}
	.oabbs_tc{
		width:10%;
		text-align: center;
	}
	.td_width1{
		width:20%;
		text-align: center;
	}
	.td_width3{
		width:*;
	}
	.td_width4{
		width:22%;
	}
	.td_width2{
		width:15%;
		text-align: center;
	}
	.td_tbox{
		width:5%;
	}
	.search_text{
		color:black;
	}
</style>
</head>
 
<body >
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" id="form" name="form" action="/BillNoAction.do">
<input type="hidden" id="operation" name="operation" value="4"/>
		<table cellpadding="0" cellspacing="0" border="0" class="framework" >
			<tr>
				<td>
					<div class="TopTitle">
						<span style="font-weight:normal;">
						<img src="/style1/images/workflow/ti_001.gif"/>单据编号规则设置
						</span>
						<span style="padding-left: 50px;font-weight:normal;">
							<ul><li style="margin: 3px;">
							<div style="float: left;position: inherit;">关键字查询：</div><div style="float: left;background-color: #FFF;">
							<input type="text" id="keySearch" name="keySearch"  class="search_text" value="$!billNoSearchForm.keySearch" onkeydown="if(event.keyCode==13){document.forms[0].submit();}"/><input type="button" class="search_button" style="padding-left: 0px;" onclick="document.forms[0].submit();"/>
							<!-- <input name="Submit" class="bu_02" type="submit" onClick="forms[0].operation.value=4;return beforeBtnQuery();" value="查询" /> -->
							</div>
							</li></ul>
						</span>
						
						<span style="float: right;font-weight: normal;margin-right: 12px;margin-bottom: 4px;">
							<input type="button" class="bu_02" onClick="window.location.href='/BillNoAction.do?operation=4'" value="刷新"/>
							#if($LoginBean.operationMap.get("/BillNoAction.do").add())
							<input type="button" class="bu_02" onclick="dealBillNo(1,'')" value="添加" />	
							#end
							<!-- #if($LoginBean.operationMap.get("/BillNoAction.do").delete())			
							<input type="button" class="bu_02" onclick="deleteList('')" value="删除" />	
							#end -->
						</span>
					</div>
					
					<table class="data_list_head"  cellpadding="0" cellspacing="0" >
						<thead>
							<tr>
								<td width="30px;">序号</td>
								<!-- <td width="25px;" style="vertical-align:middle;">
									<input name="selAll" type="checkbox" onClick="checkAll('keyId')"/>
								</td><td class="td_width3">
									模块标识
								</td> -->
								<td class="td_width4"> 
									模块名称
								</td>
								<td class="td_width3">
									单据编号规则
								</td>
								<td class="oabbs_tc">起始流水号</td>
								<td class="oabbs_tc">流水号增量</td>
								<td class="oabbs_tc1">补号规则</td>
								<td class="oabbs_tc2" align="center">
									操作
								</td>
							</tr>
						</thead>
					</table>
					<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow-y:auto;width:99%;" >
					<script type="text/javascript"> 
						var oDiv=document.getElementById("data_list_id");
						var sHeight=document.documentElement.clientHeight-115;
						oDiv.style.height=sHeight+"px";
					</script>
						<table  cellpadding="0" cellspacing="0" >					
							<tbody>
								#set($count=1)
								#foreach($!bill in $!billList)
									<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
										<td width="30px;">$!count</td>
										<!--<td width="25px;"><input name="keyId" type="checkbox" value="$!bill.ID"/></td>
										<td class="td_width3">$!bill.KEY</td> --><td class="td_width4">$!bill.BILLNAME</td>
										<td class="td_width3"><span title="$!bill.EXPLAIN" id="patternvalue$!count">$!bill.EXPLAIN</span></td>
										#set($start = $!bill.START)
										<td class="oabbs_tc"><span id="start_$count">$start</span></td>
										<td class="oabbs_tc">$!bill.STEP</td>
										<td class="oabbs_tc1">
											#if("$!bill.ISFILLBACK"=="true" && "$!bill.ISADDBEFORM"=="false") 保存时生成编号
											#elseif("$!bill.ISFILLBACK"=="true" && "$!bill.ISADDBEFORM"=="true") 补号 
											#else 新增时生成编号 
											#end
										</td>
										<td align="center" class="oabbs_tc2">
											#if($LoginBean.operationMap.get("/BillNoAction.do").update())
											<a href="javascript:void(0)" onclick="dealBillNo(2,'$!bill.KEY')" style="color:blue;">修改</a>&nbsp;
											#end
											<!-- <a href="javascript:void(0)" onclick="deleteList('$!bill.ID')" style="color:blue;">删除</a>&nbsp; -->
											<a href="javascript:void(0)" onclick="details('$!bill.KEY')" style="color:blue;">详情</a>&nbsp;
											<a href="javascript:void(0)" onclick="resets('$!bill.KEY')" style="color:blue;">重置</a>
											</td>
									</tr>
									#set($count=$count+1)
								#end
							</tbody>		
						</table>
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