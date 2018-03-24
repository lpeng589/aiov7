<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>期初导入</title>
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
	function downModule(){
		var url = "/IniAccQueryAction.do?operation=7&opType=downModule";
		jQuery.ajax({type:"post",
			url:url,
			success: function(msg){
		    	window.location.href=msg;
		   }
		});
	}
	
	/* 导入财务期初 */
	function getExcel(){
 		var path=document.getElementById("fileName").value;
	   	if(path.length>0){
	        var pos=path.lastIndexOf(".");
			var lastName=path.substring(pos,path.length);
			if(lastName.toLowerCase()!=".xls"){
				alert('文件格式不正确');
				return false;
			}else{
				var starIndex = path.lastIndexOf("\\");
				var endIndex = path.lastIndexOf(".");
				var fileName = path.substr(starIndex+1,(endIndex-starIndex-1));
				jQuery("#newDate").val(new Date());
				form.submit();
			}
		}else{
	    	alert('请选择文件');
			return false;
	    }
	    if(typeof(top.jblockUI)!="undefined"){
			top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
		}
	}
	
	jQuery(document).ready(function(){
		if(jQuery("#showRsDiv").val() == "true"){
			jQuery("#showRs").show();
			if(typeof(top.junblockUI)!="undefined"){
				top.junblockUI();
			}
		}
	})
</script>
</head>
<body>
<form method="post" scope="request"  name="form" id="form" action="/IniAccImportAction.do" enctype="multipart/form-data">
<input type="hidden" name="operation" value="2"/>
<input type="hidden" name="opType" id="opType" value="importIniAcc"/>
<input type="hidden" name="importName" id="importName" value="tblIniAccDet" />
<input type="hidden" name="showRsDiv" id="showRsDiv" value="$!showRsDiv"/>
<input type="hidden" name="newDate" id="newDate" value=""/>
<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span>数据导入  --  [ 财务期初 ] </span>
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
											<span style="line-height: 40px;"><span class="num_1"></span><font>获取execl导入模板，并填写内容</font></span>
											<div style="width: 100%;">
												<div style="float: left;margin-left: 40px;margin-right:5px; padding-top:2px; vertical-align: middle;">导入模板: </div>
							        		 	<div class="lp" style="float: left;" onclick="downModule()"><span class="a"></span>点击下载<span class="c"></span></div>
											</div>
											<li style="list-style-type: none;"><font color="red">*</font>注：请勿改动模板中的名称，否则可能导入错误</li>
										</ul>
									</div>
									<div>
										<ul>
											<span style="line-height: 40px;"><span class="num_2"></span><font>上传xls格式文件</font></span>
											<li>将上一步生成的xls文件上传到服务器</li>
											<li style="list-style: none;">
												<input type="file" name="fileName" id="fileName" style="width:250px;height:24px; vertical-align:bottom" class="text" />
												<button type="button" onclick="return getExcel();" class="bu_02">导入</button>&nbsp;
											</li>
											
										</ul>
									</div>
									<div>
										<ul>
											<span style="line-height: 40px;"><span class="num_3"></span><font>$text.get("import.upload.result")</font></span>
										</ul>
										<ul id="showRs" style="display: none;">		
											<li>
												<span>$text.get("aio.import.total"):<font style="color: red;">$!totalimport</font></span>					
											</li>
											<li>
												<span>$text.get("import.succees.number"):<font style="color: red;">$!successimport</font></span>					
											</li>
											<li>
												<span>$text.get("import.failure.number"):<font style="color: red;">$!errorimport</font></span>				
											</li>
											#if("$!errorimport" != "0")
											<li style="width: 200px;margin-left: 75px;">
												<a href="/UtilServlet?operation=readErrorExcel&fileName=$!fileName"><font style="color: red;">$text.get("download.error.report")</font></a>				
											</li>
											#end
										</ul>
									</div>
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