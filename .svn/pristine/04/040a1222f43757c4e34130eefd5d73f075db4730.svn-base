<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$globals.getCompanyName('') </title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
#if($globals.getSystemState().lastErrorCode != 0 || ($globals.getSystemState().dogState != 0 && $globals.getSystemState().dogState != 1 ))
	<script language="javascript">
	#if($type=="closeWindow")
	   window.close();
	#end
	window.location.href = 'common/fatalMessage.jsp?from=index';	
	 </script>
#end



<script language="javascript">
#set($account = $globals.getAccountInfo())
#if($account.size() < 2)
	window.location.href = 'login.jsp';	
#end

var host = '$request.getHeader("host")';
function changeAccount(varAccount){
    local = host;
	if(local.indexOf(":")>0){
		local=local.substr(0,local.indexOf(":"));
		local='http://'+local+":"+varAccount+"/login.jsp";
	}else{
		local='http://'+local+":"+varAccount+"/login.jsp";
	}
	window.location.href=local;
}

</script>
<style>
body{ margin:0px; font-size:12px; background:url(/style/images/b.gif) repeat-x top}
.box{ background-image:url(/style/images/ig.jpg); background-repeat:no-repeat; width:628px; height:365px; margin:0 auto; margin-top:120px; padding-top:40px;}
.b1 { float:right; width:240px; height:auto;}
.b1 li { float:left; list-style:none; height:48px;}
.b2 { float:right; width:290px; height:auto;}
.b2 li { float:left; list-style:none; width:145px; height:35px;}
.but2 {
	border:0px;
	text-align:center;
	cursor:pointer;
	width:135px;
	height:25px;
	color:#FFFFFF;
	font-size:12px;
	font-weight:bolder;
	background:url(/style/images/b2.gif) no-repeat;
}
.but{
	border:0px;
	text-align:center;
	cursor:pointer;
	width:208px;
	height:43px;
	color:#FFFFFF;
	font-size:14px;
	font-weight:bolder;
	background:url(/style/images/b1.gif) no-repeat;
}
.Prongix {
	width:64px;
	height:64px;
	background:url(/style/images/allbg.gif) no-repeat -121px -130px;
}
</style>
</head>

<body>
<div class="box">
<table width="90%" height="272" border="0" align="center" cellpadding="0" cellspacing="0">
	 <tr>
      <td width="11%" height="35">&nbsp;</td>
      <td width="33%">&nbsp;</td>
      <td width="56%" rowspan="4" align="center" valign="middle">
	  <div class="b1">
	  #if($account.size()<=6)
	  #foreach($accrow in $account)
        <li><button name="bt" class="but" onClick="changeAccount('$accrow.value')">$accrow.name</button></li>
      #end
	  </div>
	  #else
	  <div class="b2">
	  	#foreach($accrow in $account)
		<li><button name="bt" class="but2" onClick="changeAccount('$accrow.value')">$accrow.name</button></li>
		#end
	  </div>
	  #end
	   </td>
    </tr>
    <tr>
      <td height="35">&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td height="35">&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td  height="60" align="" valign="middle" colspan="2" >&nbsp;</td>
    </tr>
  </table>
</div>
</body>
</html>
