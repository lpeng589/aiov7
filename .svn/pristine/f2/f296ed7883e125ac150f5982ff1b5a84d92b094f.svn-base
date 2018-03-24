<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/css/client.css"/>
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/function.js"></script>  
<style type="text/css">


</style>
<script language="javascript">

function install(){
	if(confirm("注意：覆盖安装，请会清除原来的所有数据，请慎重！！！")){
		document.form.submit();
	}
}
</script>
</head>
<body onload="aa();">
<form  method="post" scope="request" id="form" name="form" action="/CustomImportAction.do" enctype="multipart/form-data" >
<input type="hidden" name="type" value="importTable"/>
<input type="hidden" name="zipFileName" value="$!zipFileName"/>
<input type="hidden" name="isConfirm" value="yes"/>
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/>
		<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span>模块导入确认</span>
						
						
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
											<div style="line-height: 40px;"><font>注意：系统中已存在此模块，您可选择是否覆盖。覆盖将删除原来的数据，并全新安装请慎重选择</font></div>
											<div style="line-height: 40px;text-align: center;font-size:30px;margin-top: 30px;">
											<input type=button style="font-size: 20px; width: 100px;" value="覆盖安装" onclick="install()"/>
											<input type=button style="font-size: 20px; width: 100px;" value="取消" onclick="closeWin('TableimportDiv')"/>
											</div>
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
