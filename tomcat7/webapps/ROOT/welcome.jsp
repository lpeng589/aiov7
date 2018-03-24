<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Welcome</title>

<link rel="stylesheet" href="/$globals.getStylePath()/css/menu.css" type="text/css">
<link rel="stylesheet" href="/js/tree/jquery.treeview.css" /> 
<script src="/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="$globals.js("/js/mdiwin.vjs","",$text)"></script>
<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/shortCut.js"></script>
<link type="text/css" href="/$globals.getStylePath()/css/shortCut.css" rel="stylesheet" />
<script type="text/javascript" src="$globals.js("/js/welcome.vjs","",$text)"></script>
<style type="text/css">
.menuPopBg,.PopUp_organizationBg,.c1,.c2,.shortBg,img{ behavior: url(iepngfix.htc) } 
</style>

<script type="text/javascript">
$(document).ready(function(){
#if("$!screenWidth" == "")
	//非正常登陆进入,先把屏幕宽度放入内存
	var screenWidth = $(document).width();
	jQuery.ajax({
	   type: "POST",
	   url: "/UtilServlet?operation=screenWidth&screenWidth="+screenWidth,
	   success: function(msg){
	   }
	});
#end
});
</script>

</head>
<body onLoad="init();setMyDest(); load(); #if($LoginBean.viewLeftMenu == "1") ShortCut.cut.show(); ShortCut.cu2(); #end"> 
<!-- 右键加入我的桌面-->
<div class="menuPop" id ="menuPop"  style="display:none">
<div class="menuPopBg" id ="menuPopBg"  >

</div>
</div>
<div id="main" class="main">
	<div class="shortBg" id="shortBg" style="display:none">
		
	</div>
	<div class="shortcut" style="display:none">	  
	  <IFRAME width=100% height=100px style="position:absolute;top:0px;left:0px;z-index:-1;border-style:none;filter:Alpha(opacity=0)" frameborder="0"></IFRAME>
	  <div class="shortcut_top"><span>$text.get("left.menu.Title")</span><b style="margin-right:25px" class="icon57" title="" id="short_cu" onClick="ShortCut.cu()" ></b></div>
	  <div class="shortcut_body">
	  	<div id="shortcutBody">
	  	<div id="shortcutId">

	  	</div>
	  	</div>
	  </div>
	  <!--<div class="shortcut_trundle">
	  	<a href="javascript:void(0)" onClick="leftMove(true)" class="shortcut_up"></a>
		<a href="javascript:void(0)" onClick="leftMove(false)" class="shortcut_down"></a>
	  </div>-->
	  <div class="shortcut_bottom"></div>
	</div>

	<div class="shortcut_btn" id="shortcut_btn"></div>
	
	<table id="mtable" height="100%" width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout: fixed;"
        id="Table1">
        <!--  多窗口代码 -->
        <tr >
            <td class="frametr">
                <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout: fixed">
                   <tr>
                   	   <td width="20" >
                   	   	<a style="cursor: pointer;" class="prev" onMouseDown="tabScroll('left')" onMouseUp="tabScrollStop()" title="$text.get("multiwindow.lb.leftstep")"></a>
                   	   </td>
                       <td valign="middle" nowrap >
                           <div class="tab" >			
								<div style="overflow: hidden; width: 100%;">
					                <div id="titlelist" class="menu_title">
					                </div>
					            </div>               
							</div>	
                       </td>
                       <td class="systemButtom">
                           <div >
								<a style="cursor: pointer;" class="next" onMouseDown="tabScroll('right')" onMouseUp="tabScrollStop()" title="$text.get("multiwindow.lb.rightstep")"></a>
								<a href="javascript:void(0)" style="margin-top:-2px;" class="closeAll" onClick="win.removeall()"  title="$text.get("multiwindow.lb.closeall")"></a>	
								<!--<a href="javascript:void(0)" class="closeOne" onClick="win.removewin(win.currentwin)" title="$text.get("multiwindow.lb.closecurrent")"></a>	-->
								#if($LoginBean.operationMap.get("/FrameworkAction.do").query())							
								<a id="organization" style="margin-top:2px; margin-right:24px;cursor: pointer;" class="organization" title="$text.get("sys.onLine.amount")" onclick="javascript:mdiwin('/FrameworkAction.do?1=1','$text.get("sys.onLine.amount")');"></a>
								#end
								<a style="margin-top:2px;cursor: pointer;"  id="menubutton" #if($LoginBean.viewTopMenu != "1") class="flexible_down" title="$text.get('common.title.displayMenu')" #else class="flexible_up" title="$text.get('common.title.hiddenMenu')" #end  onClick="clickMenu(this)"></a>
							</div>                          
                       </td>
                   </tr>
               </table>		
            </td>
        </tr>
         
        <!--  多窗口代码 -->
		<tr>
          <td >
          <table height="100%" width="100%" cellspacing="0" cellpadding="0" style="margin-left: 0px;">
          	<tr><td id="leftTd" width="0"></td>
          	<td width="*" >
                <table height="100%" width="100%" cellspacing="0" cellpadding="0" style="margin-left: 0px;">
      <tr id="worarea">
    <td align="center" valign="middle">
<iframe id="home" name="home" src=""  frameborder=false scrolling="no" width="100%" height="100%" frameborder=no ></iframe>	
	</td>
	</tr>
	 <tr style="display:none">
                        <td align="center" id="mywindows" style="width:100%"></td>
                    </tr>
                </table>
              </td> 
              </tr>
            </table>    
          </td>
        </tr>        
    </table>
</div>
<div id="PopUp_organizationDIV" class="PopUp_organization" style="display:none">
</div>
<div id="PopUp_organizationDIVBg" class="PopUp_organizationBg" style="display:none">
</div>
</body>
</html>