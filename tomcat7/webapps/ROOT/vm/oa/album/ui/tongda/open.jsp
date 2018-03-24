<html>
<head>
<meta name="renderer" content="webkit">
<title>图片浏览</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="/vm/oa/album/ui/tongda/theme/11/style.css"/>
<!-- <script src="/vm/album/ui/tongda/ccorrect_btn.js"></script>-->
<script type="text/javascript" src="/js/jquery.js"></script>
</head>
	<frameset rows="*,50"  cols="*" frameborder="NO" border="0" framespacing="0" id="frame1">
		  <frame name="open_main" scrolling="yes" src="vm/oa/album/ui/tongda/open_main.jsp" frameborder="0">
	      <frame name="open_control" scrolling="no" noresize src="/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&path=$globals.encode($pId)&requestType=showPic&tempName=$globals.encode($!tempName)&pageName=open.jsp&pageNoURL=$!pageNoURL&pageSizeURL=$!pageSizeURL&keyURL=$!keyURL&sortTypeURL=$!sortTypeURL&onDown=$!onDown"frameborder="0">
	</frameset>
</html>

