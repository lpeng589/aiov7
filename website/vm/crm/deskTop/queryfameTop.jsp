<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self">

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("crm.deskTop.fameD")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<style type="text/css">
<!--
-->
</style>
<script type="text/javascript">

  function detail(id,typeid,userId){
  	if(typeid==1){
  		if(confirm("$text.get('crm.deskTop.applause')")){
  			AjaxRequest("/UtilServlet?operation=fameTopWish&id="+id+"&typeId="+typeid+"&userId="+userId);
  			window.location.reload();
  		}
  	}
  	else if(typeid==2){
  		if(confirm("$text.get('crm.deskTop.Blood')")){
  			AjaxRequest("/UtilServlet?operation=fameTopWish&id="+id+"&typeId="+typeid+"&userId="+userId);
  			window.location.reload();
  		}
  	}
  	else{
  		if(confirm("$text.get('crm.deskTop.Below.Defiant')")){
  			AjaxRequest("/UtilServlet?operation=fameTopWish&id="+id+"&typeId="+typeid+"&userId="+userId);
  			window.location.reload();
  		}
  	}
  }


  
  function fall(status){
  	window.location.href="/CrmDeskTopAction.do?operation=5&status="+status;
  }
</script>
</head>

<body scroll="yes">
<form action="">
<div class="glb">
<div style="margin-top:-23px; float:right; text-align:right; padding:0px 7px 0px 0px; border:0px">
		#if($!status=="0")
        <button type="button" onClick="fall(0)" >$text.get("crm.deskTop.apocalypse")</button>
        #else
        <button type="button" onClick="fall(1)" >$text.get("crm.deskTop.blackout")</button>
        #end
</div>
	<div>    #foreach($listTop in $listfameTop)
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td style="width:100px; border-right:10px solid #fff">
					<img id="$globals.get($listTop,4)" width="100" height="100" src=#if("$!globals.get($listTop,3)"=="") "/style/images/no_head.gif" #else "/ReadFile.jpg?fileName=$globals.urlEncode($globals.get($listTop,3))&tempFile=false&type=PIC&tableName=tblEmployee" #end></img>
					<div>$globals.get($listTop,2)($globals.get($listTop,1))</div>
				</td>
				<td style=" background:#EEEEEE; color:#2F5471; padding:10px" valign="top">$text.get("crm.deskTop.Eval")：$globals.get($listTop,9)</td>
				<td style=" background:#EEEEEE; color:#2F5471; padding:10px 10px 10px 25px; width:120px; text-align:center" valign="top">
				   <a href="#" onClick="detail('$globals.get($listTop,0)','1','$globals.get($listTop,4)')"> 
				   #set($vc = 0)
				   <span><img src="/style/images/0.gif" 
				   alt="$text.get('crm.deskTop.Applause')
#foreach($listWish in $listfameTopWish)#if($globals.get($listTop,0)==$globals.get($listWish,0) && $globals.get($listWish,3)=="1")
$globals.get($listWish,2)$text.get("crm.deskTop.Denote")$globals.getEnumerationItemsDisplay("Gala","1") $globals.get($listWish,4)
#set($vc = $vc+1)#end#end 	
				   "></span><br>
				   <span>($vc)</span>
				   </a>
					<a href="#" onClick="detail('$globals.get($listTop,0)','2','$globals.get($listTop,4)')">
					#set($vc = 0)
					<span><img src="/style/images/1.gif" 
					alt="$text.get('crm.deskTop.Blood.Arouse')
#foreach($listWish in $listfameTopWish)#if($globals.get($listTop,0)==$globals.get($listWish,0) && $globals.get($listWish,3)=="2")
$globals.get($listWish,2)$text.get("crm.deskTop.Denote")$globals.getEnumerationItemsDisplay("Gala","2") $globals.get($listWish,4)
#set($vc = $vc+1)#end#end "></span><br>
				   	<span>($vc)</span>
				   	</a>
					<a href="#" onClick="detail('$globals.get($listTop,0)','3','$globals.get($listTop,4)')">
					#set($vc = 0)
					<span><img src="/style/images/2.gif" 
					alt="$text.get('crm.deskTop.Defiant')
#foreach($listWish in $listfameTopWish)#if($globals.get($listTop,0)==$globals.get($listWish,0) && $globals.get($listWish,3)=="3")
$globals.get($listWish,2)$text.get("crm.deskTop.Denote")$globals.getEnumerationItemsDisplay("Gala","3") $globals.get($listWish,4)
#set($vc = $vc+1)#end#end"></span><br>
				    <span>($vc)</span>
				    </a>				   
				</td>
			</tr>
		</table>
		#end
	</div>
</div>
           
</form>
</body>
</html>