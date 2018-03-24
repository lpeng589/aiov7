<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.advice.readAdvice")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript">
	
	function doSubmit1()
	{
		document.getElementById("id").value=document.getElementById("lastId").value;
		window.document.form.submit();
	}
	function doSubmit2()
	{
		document.getElementById("id").value=document.getElementById("nextId").value;
		window.document.form.submit();
	}
	function doSubmit()
	{
		if(confirm("$text.get('com.confirm.submit.exam')")){
			document.getElementById("operation").value=2;
			window.document.form.submit();
		}
	}
	function doBack(){
		
	}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/ExamAction.do" name="form" method="post">
<input type="hidden" id="id" name="id" value=""/>
<input type="hidden" id="currId" name="currId" value="$currProblem.id"/>
<input type="hidden" id="lastId" name="lastId" value="$lastProblemId"/>
<input type="hidden" id="nextId" name="nextId" value="$nextProblemId"/>
<input type="hidden" id="timeLeft" value="$leavingTime"/>
<input type="hidden" id="startTime" value="$startTime"/>
<input type="hidden" id="submitType" name="submitType" value=""/>
<input type="hidden" id="operation" name="operation" value="$globals.getOP("OP_ADD")" />
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("exam.lb.testOnLine")</div>
	<ul class="HeadingButton">
		<li>
		   <button type="button" class="b2">$text.get("exam.lb.leavingTime")：<span id="leavingTime"></span>$text.get("exam.lb.minute")</button>
		</li>
		<li><button type="button"  onClick="doSubmit1();" class="b2">$text.get("exam.lb.last")</button></li>
		<li><button type="button" onClick="doSubmit2();" class="b2">$text.get("exam.lb.next")</button></li>
		<li><button type="button" onClick="doSubmit();" class="b2">$text.get("exam.lb.submit")</button></li>
	</ul>
</div>
<div id="listRange_id">
<script type="text/javascript">
var oDiv=document.getElementById("listRange_id");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
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
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td class="oabbs_tr"></td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>
				<div class="oa_art">
				<h3>$!globals.get($arr_newInfo,2) &nbsp;</h3>
				$text.get("exam.lb.empName")：<span>$!createBy</span>  $text.get("exam.lb.getExam")：<span>$!createTime </span>
					<div class="ao_Ordainrightcontent"><br>
						
					<div class="oa_accessories" style="height:auto;"><br>
					$text.get("exam.lb.content")：


					<div align="left">
							$currProblem.serialNum .&nbsp;&nbsp;(#if($currProblem.answerType=="single")单选题#else多选题#end) &nbsp;$currProblem.title
								</br>
					#foreach($ls_row in $currProblem.answers)
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							#if($ls_row.userAnswer)
							<input type="checkbox" name="chkAnswer" id="chkAnswer" value="$ls_row.id" checked="checked">
							#else
							<input type="checkbox" name="chkAnswer" id="chkAnswer" value="$ls_row.id">
							#end
							&nbsp;&nbsp;&nbsp;&nbsp;
							$ls_row.sign.
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$ls_row.description
						<br/>		    
					#end
					<br/>
					</div>
				</div>
				</div>
				</td>
			</tr>
		  </tbody>
		</table>
	</div>
	</div>
</form>
</body>
<script language="javascript">
	function getMyDate(s){
		var year = s.substr(0,4);
		var month = s.substr(5,2)-1;
		var date = s.substr(8,2);
		var hour = s.substr(11,2);
		var min = s.substr(14,2);
		return new Date(year,month,date,hour,min);
	}
	function reflesh(){
		var curDate = new Date();
		var start = document.getElementById('startTime').value;
		var d = getMyDate(start);
		var leavingTime = document.getElementById('timeLeft').value;
		var MinMilli = 1000 * 60;
		var result = leavingTime-((curDate.getTime()-getMyDate(start).getTime())/MinMilli) + 1;
		document.getElementById("leavingTime").innerHTML = parseInt(result);
		if(result<=0){
			document.getElementById("operation").value=2;
			document.getElementById("submitType").value="auto";
			window.document.form.submit();
		}
		setTimeout('reflesh()',60000);
	}
	reflesh();
</script>
</html>
