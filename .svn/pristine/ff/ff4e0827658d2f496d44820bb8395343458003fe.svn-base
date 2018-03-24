<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>AIO</title>
<link type="text/css" rel="stylesheet" href="/style/css/client.css"/>
<link rel="stylesheet" href="/style1/css/workflow2.css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript"> 
</script>
<style type="text/css">
	#searchDiv ul li{
		height: 30px;
		float: left;
	}
	#searchDiv{
		padding: 5px 0px 0px 10px;
		height: 35px;
	}
	input{
		padding-top: 4px;
	}
</style>
<script type="text/javascript">
function sub(){
	var periodyear = jQuery("#periodyear").val();
	var period = jQuery("#period").val();
	if(periodyear == ""){
		alert("期间年不能为空！");
		return false;
	}
	if(period == ""){
		alert("会计期间不能为空！");
		return false;
	}
	if(!isInt2(periodyear)){
		alert("期间年只能为正整数！");
		return false;
	}
	if(!isInt2(period)){
		alert("会计期间只能为正整数！");
		return false;
	}
	form.submit();
}
</script>
</head>
 
<body>
<form action="/VoucherManage.do" method="post" id="form" name="form">
<input type="hidden" id="optype" name="optype" value="stopCredNoList"/>
<div class="c_main  f_l" style="margin-left: 0;width:525px;height:275px;">
<div class="maintablearea">
<div class="hd">
<div id="searchDiv"><ul><li >凭证字：<select name="CredTypeID" id="CredTypeID">
	#foreach($erow in $globals.getEnumerationItems('CredTypeID'))
			<option title="$erow.name" #if("$!CredTypeID"=="$!globals.get($enumerate,0)") selected #end>$erow.name</option>
	#end
	</select>
</li>
<li style="padding-left: 20px;">期间年：
	</li><li><input name="periodyear" id="periodyear" style="width:40px;" value="$!periodyear"/>
</li>
<li style="padding-left: 20px;">
	会计期间：</li><li><input name="period" id="period" style="width:40px;" value="$!period"/>
</li>
<li style="padding-left: 20px;"><input name="btn" id="btn" value="查询" type="button" onclick="sub()"/></li>
</ul></div>
<div class="bd">
<ul class="maintop" style="width: 100%;">
<li class="col_hy" style="background: none;text-align: left;margin-left: 25px;">凭证字</li>
<li class="col_hy" style="background: none;text-align: left;margin-left: 25px;">会计期间年</li>
<li class="col_hy" style="background: none;text-align: left;margin-left: 25px;">会计期间</li>
<li class="col_hy" style="background: none;text-align: left;margin-left: 25px;">凭证号</li>
</ul>
#foreach($!accmain in $!accmainList)
<ul class="col" style="width: 100%;float: left;clear: none;">
<li class="col_hy" style="background: none;text-align: left;margin-left: 25px;">$!globals.get($!accmain,0)</li>
<li class="col_hy" style="background: none;text-align: left;margin-left: 25px;">$!globals.get($!accmain,1)</li>
<li class="col_hy" style="background: none;text-align: left;margin-left: 25px;">$!globals.get($!accmain,2)</li>
<li class="col_hy" style="background: none;text-align: left;margin-left: 25px;">$!globals.get($!accmain,3)</li>
</ul>
#end
#if($!accmainList.size()==0)
	<div style="text-align: center;width: 100%;color:red;padding-top: 50px">搜索无数据</div>
#end
</div>
</div></div>
</div>
</form>
</body>
</html>

