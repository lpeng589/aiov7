<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新建工作流</title>
<link type="text/css" rel="stylesheet" href="/style/css/workflow_list.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<style type="text/css">
.html{background:url(/style/images/item/html_bg.jpg) repeat 0 0;}
.icons{background-image:url(/style/v7/images/menu-icons.png);background-repeat:no-repeat;display:inline-block;}
.icon16{background-image:url(/style/images/client/icon16.png);background-repeat:no-repeat;display:inline-block;}
#scroll-wrap{overflow:auto;}
.wrap{width:960px;margin:0 auto;margin-top:10px;background:#fff;padding:0 20px 10px 20px;}
.flow_group{overflow:hidden;padding:10px;}
.flow_group li{float:left;width:150px;position:relative;}
.flow_group li>a{color:#000;float:left;display:inline-block;max-width:130px;color:#448ac3;border-bottom:2px solid #fff;}
.flow_group li>a:hover{border-bottom:2px solid #448ac3;}
.flow_group li>b.icons{width:16px;height:16px;margin:0 0 0 2px;background-position:-91px -48px;float:left;display:none;cursor:pointer;}
.flow_group li:hover>b.icons{display:inline-block;}
.aoHeadingFrametop{border-bottom:2px solid #b8d8f2;padding:5px 0;overflow:hidden;}
.aoHeadingFrametop .t-em{padding:0 10px;font-weight:bold;font-size:14px;float:left;display:inline-block;line-height:21px;}
.aoHeadingFrametop .inp-wp{float:left;display:inline-block;overflow:hidden;position:relative;}
.inp-wp .inp{margin:0;width:120px;border:1px #bbb solid;height:21px;padding:0 5px;border-radius:4px;padding-right:20px;}
.inp-wp b.icon16{width:16px;height:16px;background-position:-32px 0;cursor:pointer;position:absolute;right:2px;top:2px;}
.inp-wp b.icon16:hover{background-position:-32px -16px;}
.d-block{border:1px #c9c9c9 solid;margin:15px 0;}
.d-block .t-p{height:30px;line-height:30px;padding:0 10px;background:transparent url(/style/images/client/bg.gif) repeat-x 0px -78px;font-weight:bold;cursor:default;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript">

function keywordPress(obj){
	var keyword = $(obj).val();
	if(keyword.trim().length>0){
		$(".flow_group li a").each(function(index){
			$(this).css("background","");
			if($(this).html().indexOf(keyword)!=-1){
				$(this).css("background","#FF9632");
			}
		});
	}
}

//加入收藏
function addMyDest(url,title){
	if(url.trim().length>0 && title.trim().length>0){
		jQuery.get("/moduleFlow.do?operation=addMyDest&title="+encodeURIComponent(title)
											+"&url="+encodeURIComponent(url)+"&time="+(new Date()).getTime(), function(data){
			if("ok"==data){
				asyncbox.tips('$text.get("common.lb.menu.addmydest")$text.get("common.msg.DEFAULT_SUCCESS")','success')
			}else if("two"==data){
				asyncbox.tips('$text.get("common.lb.menu.addmydest")$text.get("importData.repeat")');
			}else{
				asyncbox.tips('$text.get("common.lb.menu.addmydest")$text.get("common.msg.DEFAULT_FAILURE")','error');
			}
	 	}); 
	}
}

function addFlow(strUrl,strTitle){
	var width = 1024;
	if($(window).width()>1024) width = 1124;
	openPop('PopMainOpdiv',strTitle,strUrl,width,500,true,false);
	//asyncbox.open({id:'PopMainOpdiv',url:strUrl,title:strTitle,width:1000,height:500});
}
</script>

</head>

<body class="html" onload="showStatus();">
<div id="scroll-wrap">
<div class="wrap">
 <div class="aoHeadingFrametop">
	<em class="t-em">$text.get("oa.workflow.create")</em>
	<span class="inp-wp">
		<input class="inp" type="text" id="keyword" onkeyup="keywordPress(this);"/>
		<b class="icon16"></b>
	</span>
</div>
<div id="conter">
<script type="text/javascript">
$("#scroll-wrap").height(document.documentElement.clientHeight);
</script>
		
		#set($str=",")
		#foreach($pal in $result)
		#if(!($str.indexOf($!globals.get($pal,5))>-1))
		#set($str=$str+"$!globals.get($pal,5)"+",")
		<div class="d-block"> 
			<p class="t-p">$!globals.get($pal,3)</p>
			<ul class="flow_group">
			#foreach($flow in $result) 
               	#if($!globals.get($flow,5)==$!globals.get($pal,5)) 
               		#if("$globals.get($flow,12)"!="1")
                		#if($!globals.get($flow,8).startsWith("Flow") || $LoginBean.operationMap.get("/UserFunctionQueryAction.do?tableName=$!globals.get($flow,8)").add())
                			<li>
                				<a href="javascript:addFlow('/UserFunctionQueryAction.do?tableName=$!globals.get($flow,8)&src=menu&operation=6&designId=$!globals.get($flow,0)','$!globals.get($flow,1)');">$!globals.get($flow,1)</a>
                				<b class="icons" title="收藏" onclick="addMyDest('/UserFunctionQueryAction.do?tableName=$!globals.get($flow,8)&src=menu&operation=$globals.getOP("OP_ADD_PREPARE")&designId=$!globals.get($flow,0)','$!globals.get($flow,1)')"></b>
                			</li>
                		#end
               		#else
               			<li>
               				<a href="javascript:addFlow('/OAWorkFlowAction.do?tableName=$!globals.get($flow,8)&src=menu&operation=6&designId=$!globals.get($flow,0)','$!globals.get($flow,1)');">$!globals.get($flow,1)</a>
               				<b class="icons" title="收藏" onclick="addMyDest('/OAWorkFlowAction.do?tableName=$!globals.get($flow,8)&src=menu&operation=$globals.getOP("OP_ADD_PREPARE")&designId=$!globals.get($flow,0)','$!globals.get($flow,1)')"></b>
               			</li>
               		#end
				#end
			#end
			</ul>
		</div>
		#end
		#end
</div>
</div>
</div>
</body>
</html>