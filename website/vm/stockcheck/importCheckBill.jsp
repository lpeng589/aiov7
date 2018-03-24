<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>盘点单导入</title>
<link type="text/css" rel="stylesheet" href="/style1/css/ListRange.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/css/client.css"/>
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<style type="text/css">
.tblDepot{border-collapse:collapse;table-layout:fixed;border-top:1px #d2d2d2 solid;border-right:1px #d2d2d2 solid;}
.tblDepot tr td{text-align:center;height:33px;line-height:33px;border-bottom:1px #d2d2d2 solid;border-left:1px #d2d2d2 solid;}
.tblDepot thead tr td{background:#2e8cbc;color:#fff;}
</style>

<script type="text/javascript">

	/* 下载模板 */
	function downModule(stockPreId,stockCode,stockCheckDate){
		var impmoduleType = $("input[name='impmoduleType']:checked").val();
		var url = "/StockCheckAction.do?operation=1&optype=exportTemplet";
		jQuery.ajax({type:"post",
			url:url,
			data: "isModule=true&stockPreId="+stockPreId+"&stockCode="+stockCode+"&stockCheckDate="+stockCheckDate+"&impmoduleType="+impmoduleType,
			success: function(msg){
			if(msg.indexOf("/ReadFile")=="-1"){
				alert(msg);
			}else{
		    	 window.location.href=msg;
		    }
		   }
		});
	}
	
	/* 导入盘点单 */
	function getExcel(stockName){
 		var path=document.getElementById("fileName").value;
	   	if(path.length>0){
	        var pos=path.lastIndexOf(".");
			var lastName=path.substring(pos,path.length);
			if(lastName.toLowerCase()!=".xls"){
				alert('文件格式不正确');
				return false;
			}else{
				var mhtml = $("input[name='impmoduleType']:checked").next("label").html();
				asyncbox.confirm('模板类型：'+mhtml+'<br />导入仓库：'+stockName+'<br />确定以上信息无误请点击确定！','提示',function(action){
		　　			if(action == 'ok'){
						var starIndex = path.lastIndexOf("\\");
						var endIndex = path.lastIndexOf(".");
						var fileName = path.substr(starIndex+1,(endIndex-starIndex-1));
						jQuery("#newDate").val(new Date());
						form.submit();
					}
				});	
			}
		}else{
	    	alert('请选择文件');
			return false;
	    }
	}
	
	jQuery(document).ready(function(){
		if(jQuery("#showRsDiv").val() == "true"){
			jQuery("#showRs").show();
		}
	})
</script>
</head>
<body>
<form method="post" scope="request"  name="form" id="form" action="/StockCheckAction.do" enctype="multipart/form-data">
<input type="hidden" name="operation" value="1"/>
<input type="hidden" name="optype" id="optype" value="importCheckBill"/>
<input type="hidden" name="importName" id="importName" value="tblStockCheckDet" />
<input type="hidden" name="showRsDiv" id="showRsDiv" value="$!showRsDiv"/>
<input type="hidden" name="newDate" id="newDate" value=""/>

<input type="hidden" name="stockCode" id="stockCode" value="$!stockCode"/>

<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span>当前位置:盘点单导入 </span>
					</div>
					<div id="cc" class="data" style="width:100%; overflow:hidden;overflow-y:auto;width:100%;">
						<script type="text/javascript">
							var oDiv=document.getElementById("cc");
							var sHeight=document.documentElement.clientHeight-55;
							oDiv.style.height=sHeight+"px";
						</script>
						<div class="boxbg2 subbox_w700">
							<div class="subbox cf">
								<div class="inputbox">
									<div>
										<ul>
											<span style="line-height: 40px;"><font style="padding-left:40px">获取execl导入模板，并填写内容</font></span>
											<div style="width: 100%;">
												<div style="float: left;margin-left: 40px;margin-right:5px; padding-top:2px; vertical-align: middle;">选择下载模板: </div>
												<div><input name="impmoduleType" id="impmoduleType1" type="radio" value="1" checked="checked"/><label for="impmoduleType1">商品标准模板</label>
													#if($!globals.getFieldBean("tblGoods","BarCode").inputType!=3 && $!globals.getFieldBean("tblGoods","BarCode").inputType!= 100)
													<input name="impmoduleType" id="impmoduleType2" type="radio" value="2"/><label for="impmoduleType2">条形码模板</label>
													#end
													#if($!globals.getFieldBean("tblStockCheckDet","Seq").inputType!=3)
													<input name="impmoduleType" id="impmoduleType3" type="radio" value="3"/><label for="impmoduleType3">序列号模板</label></div><br/>
													#end
												<div class="mBlockRight" style="overflow-y:auto;width: 80%;padding-left:40px" id="div_stock">
													<table class="tblDepot" cellpadding="0" cellspacing="0">
														<thead>
														<tr>
															<td width="250">仓库</td>
															<td width="120">准备时间</td>
															<td width="80">下载</td>
															<td width="400">导入</td>
														</tr>
														</thead>
														<tbody>
														#foreach($result in $list)
															<tr>
																<td style="text-align: left;">$!result.get("$!StockMarkerName")</td>
																<td>$!result.stockCheckDate</td>
																<td><a href="javascript:downModule('$!result.stockPreId','$!result.stockCode','$!result.stockCheckDate')" class="down_a" style="color:blue;">下载模板</a></td>
																<td>
																	<input type="file" name="fileName"  id="fileName" style="width:250px;height:24px; vertical-align:bottom" class="text" />
																	<button type="button" onclick="return getExcel('$!result.stockName');" class="bu_02">导入</button>&nbsp;
																</td>
															</tr>
														#end
														</tbody>
													</table>
												</div>
												<script type="text/javascript">
													var oDiv=document.getElementById("div_stock");
													if(oDiv.offsetHeight>300){
														oDiv.style.height="300px";
													}else{
														oDiv.style.height = oDiv.offsetHeight;
													}
												</script>
							        		 </div>
										</ul>
									</div>
									<!-- <div>
										<ul>
											<span style="line-height: 40px;"><span class="num_2"></span><font>上传xls格式文件</font></span>
											<li>将上一步生成的xls文件上传到服务器</li>
											<li style="list-style: none;">
												<input type="file" name="fileName"  id="fileName" style="width:250px;height:24px; vertical-align:bottom" class="text" />
												<button type="button" onclick="return getExcel();" class="bu_02">导入</button>&nbsp;
											</li>
											
										</ul>
									</div> -->
									
									<br />
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
</form>
</body>
</html>
