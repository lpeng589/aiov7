<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>快递单打印须知</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">


</style>
<script type="text/javascript">
function onloader(){
	var Sys = {};
	var ua = navigator.userAgent.toLowerCase();
    if (window.ActiveXObject){
        Sys.ie = ua.match(/msie ([\d.]+)/)[1]
    }else if (window.MessageEvent && !document.getBoxObjectFor)
        Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1]
    if(Sys.ie){
    	jQuery("#chrome").css("display","none");
    }else if(Sys.chrome){
    	//alert("2");
    	jQuery("#ie").css("display","none");
    }
}
</script>
</head>
<body style="stopmargin:10px;leftmargin:10px;font-size:12px;line-height:25px;" onload="onloader();">
<form method="post" action="">
	<div id="ie">
      <p align="left">
      <b><font color="#FF0066">注意：必须设置后再打印，否则将造成整体错位　</font></b>
      <b><font color="#FF0066"><br/>
      </font></b>1.在浏览器菜单中选择“文件”下的“页面设置”　　<br/>
      2.按照下图提示设置参数<br/></p>
      <table border="0" width="100%">
        <tr>
          <td width="100%"></td>
        </tr>
        <tr>
          <td width="100%">
            <p align="center"><img border=0 style="width:330px;height:300px;" src="/$globals.getStylePath()/images/Left/kdsz.png"></img></p>
          </td>
        </tr>
      </table>
   	</div>
   	<div id="chrome">
      <p align="left">
      <b><font color="#FF0066">注意： 点击下面打印按钮，进入打印页面后，不需要页眉页脚！</font></b>
      <b><font color="#FF0066"><br/>         
      
   	</div>
      </form>
</body>
</html>
