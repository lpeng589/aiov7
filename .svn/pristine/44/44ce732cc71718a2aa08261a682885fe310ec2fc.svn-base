<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get(&quot;oa.common.knowledgeCenter&quot;)</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/date.vjs"></script>

<style type="text/css">
.pager_dv{display:inline-block;float:right;}
.pager_dv>div{display:inline-block;float:left;line-height:23px;}
.pager_dv>select{float:left;}
.pager_dv>input{float:left;}
.pager_dv>button{float:left;}
</style>

</head>

<body >
<form action="GradeAction.do" name="form" method="post">
<input type="hidden" name="operation" value="$globals.getOP("OP_DETAIL")" />	
<input type="hidden" name="examId" value="$examId" />	
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("exam.lb.HistoryTestDetail")</div>
	<div class="HeadingButton">
		<ul class="HeadingButton">
			<li>
				<button type="submit"  onClick="" class="b2">$text.get("common.lb.query")</button>
			</li>
			<li>
			<button type="button"  onClick="location.href='/GradeAction.do?operation=$globals.getOP("OP_QUERY")';" class="b2">$text.get("exam.lb.back")</button>
			</li>
		</ul>
	</div>
</div>
<div class="listRange_frame">
		<div class="listRange_1" id="listid">
		<li><span>$text.get("exam.lb.title")：</span><input name="title" value="$title" onKeyDown="if(event.keyCode==13) {if(!beforeButtonQuery())return false ; submitQuery();}" ></li>
		</div>
</div>
<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var dHeight=document.body.clientHeight;
var varHeight=document.getElementById("listid");
var sHeight = 0 ;
if(typeof(varHeight)!="undefined" && varHeight!=null){
	sHeight = varHeight.clientHeight ;
}
oDiv.style.height=dHeight-sHeight-68+"px";
</script>
	<table width="400px" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" name="table">
		<thead>
			<tr height="25px">
			    <td width="500px" class="oabbs_tc">$text.get("exam.lb.title")</td>
			    <td width="400px" class="oabbs_tc">$text.get("exam.lb.option")</td>
				<td width="120px" class="oabbs_tc">$text.get("exam.lb.answer")</td>
				<td width="140px" class="oabbs_tc">$text.get("exam.lb.correctAnswer")</td>
			</tr>
		</thead>
		<tbody>
		 #set($i=1)
		 #foreach($ls_row in $listDetail)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				#if($globals.isEqualObject($globals.get($ls_row,6),"0"))
					<td align="left">&nbsp;</td>
					<td align="left">$!globals.get($ls_row,4)&nbsp;</td>
					<td class="oabbs_tc">&nbsp;</td>
			    	<td class="oabbs_tc">&nbsp;</td>
				#else
			    	<td align="left">$i.&nbsp;$!globals.get($ls_row,1)&nbsp;</td>
			    	<td align="left">$!globals.get($ls_row,4)&nbsp;</td>
			    	<td class="oabbs_tc">$!globals.get($ls_row,2)&nbsp;</td>
					<td class="oabbs_tc">$!globals.get($ls_row,3)&nbsp;</td>
					#set($i=$i+1)
			    #end
			</tr>
		#end
		#if(!$listDetail)
		 	<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
				<td style="width:30px" class="oabbs_tc">&nbsp;</td>
			    <td class="oabbs_tc">&nbsp;</td>
				<td class="oabbs_tc">&nbsp;</td>
				<td class="oabbs_tc">&nbsp;</td>
				<td class="oabbs_tc">&nbsp;</td>
			</tr>
		 #end
		  </tbody>
		</table>
	</div>
		<div class="oalistRange_pagebar">
			<div class="pager_dv">
		 		$!pageBar
		 	</div>
		 </div>
	</form>
</body>
</html>
