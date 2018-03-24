<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Welcome</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/menu.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/mdiwin.vjs"></script>
<script type="text/javascript">
	function cancelAwoke(){
		 location.href="/UserManageAction.do?operation=$globals.getOP("OP_UPDATE")&type=dayStudy";
	}
	function mdiwin(url,title)
	{
	 	top.showreModule(title,url);	
	}
	function defaultpage(){
		#if("$!LoginBean.defaultPage"!="")
			#if($LoginBean.operationMap.get("$!LoginBean.defaultPage").query())
				#set($strName=$globals.getModelNameByLinkAddress("$!LoginBean.defaultPage"))
				#if($globals.isExist("$!LoginBean.defaultPage","?"))
					mdiwin("$!LoginBean.defaultPage"+"&src=menu&name=$!strName","$!strName") ;
				#else
					mdiwin("$!LoginBean.defaultPage"+"?src=menu&name=$!strName","$!strName") ;
				#end
			#else
				#if($!LoginBean.defSys=="1")
					#if($leftModuleFirst)  
						#if($LoginBean.operationMap.get("$globals.get($leftModuleFirst,4)").query())
							mdiwin('$globals.get($leftModuleFirst,4)&keyId=$globals.get($leftModuleFirst,0)&src=menu&name=$globals.encode($globals.get($leftModuleFirst,1))','$globals.get($leftModuleFirst,1)')
						#end
					#end
				#elseif($!LoginBean.defSys=="2")
					#if($LoginBean.operationMap.get("/MyDesktopAction.do").query())
						mdiwin('/MyDesktopAction.do?src=menu&name=$text.get("aio.oa.mydesk")','$text.get("aio.oa.mydesk")') ;
					#end
				#elseif($!LoginBean.defSys=="3")
					#if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblCMWillClient").query())
						mdiwin('/UserFunctionQueryAction.do?tableName=tblCMWillClient&src=menu&name=$text.get("com.interested.customer")','$text.get("com.interested.customer")');
					#end
				#else
				
			#end
			#end
		#else
			##if($!LoginBean.defSys=="1")
				###if($leftModuleFirst)  
					###if($LoginBean.operationMap.get("$globals.get($leftModuleFirst,4)").query())
						##mdiwin('$globals.get($leftModuleFirst,4)&keyId=$globals.get($leftModuleFirst,0)&src=menu&name=$globals.encode($globals.get($leftModuleFirst,1))','$globals.get($leftModuleFirst,1)')
					###end
				###end
			#if($!LoginBean.defSys=="2")
				#if($LoginBean.operationMap.get("/MyDesktopAction.do").query())
					mdiwin('/MyDesktopAction.do?src=menu&name=$text.get("aio.oa.mydesk")','$text.get("aio.oa.mydesk")') ;
				#end
			##elseif($!LoginBean.defSys=="3")
				##if($LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=tblCMWillClient").query())
					##mdiwin('/UserFunctionQueryAction.do?tableName=tblCMWillClient&src=menu&name=$text.get("com.interested.customer")','$text.get("com.interested.customer")');
				##end
			#else
				
			#end
		#end
	}
	
	function go_guide(defSys){
		if("$!LoginBean.defSys"==defSys){
			mdiwin('/vm/guide/guide_'+defSys+'.jsp?1=1','$text.get("common.lb.useGuide")');
		}else if("5"==defSys){
			window.open("$globals.getBol88()","_blank") ;
		}else{
			window.parent.parent.location.href = "/MenuQueryAction.do?guide=OK&sysType="+defSys ;
		}
	}
</script>
</head>
<body onLoad="init();defaultpage();" style=" #if($globals.getLocale()=="en") overflow: auto; background:url(/style/images/welcome/bottomen.gif) no-repeat right bottom; #else overflow: auto; background:url(/style/images/welcome/bottom.gif) no-repeat right bottom; #end">
    <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0" style="table-layout: fixed;"
        id="Table1">
		<tr>
          <td>
                <table height="100%" width="100%" cellspacing="0" cellpadding="0">
                    <tr id="worarea">		

 <td align="center" valign="middle">
 
 
 
	<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" style="width:600px; height:332px;">
		 #if("$!globals.getVersion()"=="8")
		<img src="/style/images/welcome_gm/welgm.png" width="600" height="332" border="0" usemap="#Map">
		#else
		<img src="/style/images/welcome_sy/welsy.png" width="600" height="332" border="0" usemap="#Map">
		#end
		</td>
	</tr>
	#if($LoginBean.daystudyAwoke==1)
	<tr>
		<td align="center">
		<iframe src="/vm/daystudy$!globals.getLocale()/day${globals.daystudyNum()}.html"  frameborder=false scrolling="no" width="460" height="110" frameborder=no></iframe>
  	<div style="width:440px; height:20px;color:#57543C; line-height:20px; text-align:right"><input type="checkbox" name="checkbox" onClick="cancelAwoke()"  value="checkbox">$text.get("alertCenter.lb.notAlert")</div>
		</td>
	</tr>
	#end
</table>
        </tr>
                    
                </table>
          </td>
        </tr>
    </table>

<map name="Map">
<area shape="rect" coords="78,154,149,261" href="javascript:go_guide('1')" alt="$text.get('alertCenter.lb.MoneyManageUseGuide')">
<area shape="rect" coords="160,136,230,261" href="javascript:go_guide('3')" alt="$text.get('alertCenter.lb.CustomerManageUseGuide')">
<area shape="rect" coords="239,112,314,261" href="javascript:go_guide('2')" alt="$text.get('alertCenter.lb.OAUseGuide')">
<area shape="rect" coords="326,84,407,261" href="javascript:go_guide('4')" alt="$text.get('alertCenter.lb.CRMUseGuide')">
<area shape="rect" coords="418,41,509,262" href="javascript:go_guide('5')" alt="$text.get('alertCenter.lb.bol88UseGuide')">
</map></body>
</html>