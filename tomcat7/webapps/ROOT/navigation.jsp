<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Welcome</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/menu.css" type="text/css">
<script type="text/javascript">
	
function cancelAwoke(){
	location.href="/UtilServlet?operation=dayStudy";
}

function mdiwin(url,title,alertId){
	top.showreModule(title,url);
}

function addWins(url,title){
	mdiwin(url+"&name="+encodeURIComponent(title),title) ;
}

	
function go_guide(defSys){
		if("5"==defSys){
			mdiwin('/AIOBOL88Action.do?src=menu','$text.get("role.lb.bol88")');
		}else{
			#if("en"=="$LoginBean.defLoc")
			mdiwin('/vm/guide_en/guide_'+defSys+'.jsp?1=1','$text.get("common.lb.useGuide")');
			#else
			mdiwin('/vm/guide/guide_'+defSys+'.jsp?1=1','$text.get("common.lb.useGuide")');
			#end
		}
}

</script>
</head>
<body style=" #if($globals.getLocale()=="en") overflow: auto; background:url(/style/images/welcome/bottomen.gif) no-repeat right bottom; #else overflow: auto; background:url(/style/images/welcome/bottom.gif) no-repeat right bottom; #end">
    <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout: fixed;margin-left: 10px;"
        id="Table1">
		<tr>
          <td>
                <table height="100%" width="100%" cellspacing="0" cellpadding="0">
                    <tr id="worarea">		

 <td align="center" valign="top" >
	<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" style="width:600px; height:332px;">
		#if($globals.getLocale()=="en")
		<img src="/style/images/welcome_gm/welgm_en.png" width="600" height="332" border="0" usemap="#Map">
		
		 #else
		 
		 #if("$!globals.getVersion()"=="8")
		<img src="/style/images/welcome_gm/welgm.png" width="600" height="332" border="0" usemap="#Map">
		
		#else
		 #if("$!globals.getVersion()"=="10")
		<img src="/style/images/welcome_ky/welky.png" width="600" height="332" border="0" usemap="#Map">
		#else
		<img src="/style/images/welcome_sy/welsy.png" width="600" height="332" border="0" usemap="#Map">
		#end
		#end
		#end
		

		</td>
	</tr>
	#if(false&&$LoginBean.daystudyAwoke==1&&$!globals.fileExists("daystudy$!globals.getLocale()/day${globals.daystudyNum()}.html"))<!--每日一学没有翻译，暂时只在中文时显示  -->
	<tr>
		<td align="center">
		<iframe src="/vm/daystudy$!globals.getLocale()/day${globals.daystudyNum()}.html"  frameborder=false scrolling="no" width="460" height="100" frameborder=no></iframe>
  	<div style="width:440px; height:20px;color:#57543C; line-height:20px; text-align:right"><input type="checkbox" name="checkbox" onClick="cancelAwoke()"  value="checkbox">$text.get("lertCenter.lb.notAlert")</div>
		</td>
	</tr>
	#end
</table>
        </tr>
                    <tr style="display:none">
                        <td align="center" id="mywindows" style="width:100%"></td>
                    </tr>
                </table>
          </td>
        </tr>
        
    </table>

<map name="Map">
<area shape="rect" coords="78,154,149,261" href="javascript:go_guide('1')" alt="$text.get("alertCenter.lb.MoneyManageUseGuide")">
<area shape="rect" coords="160,136,230,261" href="javascript:go_guide('3')" alt="$text.get("alertCenter.lb.CustomerManageUseGuide")">
<area shape="rect" coords="239,112,314,261" href="javascript:go_guide('2')" alt="$text.get("alertCenter.lb.OAUseGuide")">
<area shape="rect" coords="326,84,407,261" href="javascript:go_guide('4')" alt="$text.get("alertCenter.lb.CRMUseGuide")">
<area shape="rect" coords="418,41,509,262" href="javascript:go_guide('5')" alt="$text.get("alertCenter.lb.bol88UseGuide")">
</map>

</body>
</html>