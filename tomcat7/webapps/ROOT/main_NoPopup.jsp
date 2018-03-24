
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>login page</title>
<Script Language="JavaScript">   

height = screen.height;
width  = screen.width;
window.parent.opener="a";

//window.open('/MenuQueryAction.do?sysType=$!LoginBean.defSys',"_blank",'height='+(height-65)+', width='+width+'toolbar =no,menubar=no,scrollbars=no,resizable=no, location=no, status=no,Top=0,Left=0');
window.parent.location.href  = '/MenuQueryAction.do?sysType=$!LoginBean.defSys';
setTimeout("aa()",5000);
function aa(){
	window.parent.close();
}
</Script>
</head>
</html>
