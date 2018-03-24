<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>$text.get("oa.bbs.indexPage")</title>
#end
<link href="/$globals.getStylePath()/css/ListRange.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/validate.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script language="javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>

<script language="javascript">
var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="voteRemark"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

function delOpation(){
	var formatName=form.formatFileName;
	var value = formatName.options[formatName.selectedIndex].value;
	var appcers = form.popedomUserIds.value;
	appcers = appcers.replace(value+",","");
	form.popedomUserIds.value =appcers
	formatName.options.remove(formatName.selectedIndex);
}
 
function isNull(variable,message){
	if(variable=="" || variable==null){
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else{
		return true;
	}
			
}

function m(value){
	return document.getElementById(value) ;
}

function delOpation2(){
	
	var formatName=form.formatDeptName;
	var value = formatName.options[formatName.selectedIndex].value;
	var appcers = form.popedomDeptIds.value;
	appcers = appcers.replace(value+",","");
	form.popedomDeptIds.value =appcers
	formatName.options.remove(formatName.selectedIndex);
}

function chikeRadio(obj){
	if(obj.checked==true){
		if(obj.value==1){
			document.getElementById("_title").style.display='block';  
			document.getElementById("_context").style.display='block';
			document.getElementById("_trno").style.display=''		
		}else{
			document.getElementById("_title").style.display='none';
			document.getElementById("_context").style.display='none';
			document.getElementById("_trno").style.display='none'		
		}
	}
}

function show2(){
	var displayName=encodeURI("$text.get('oa.common.person')") ;
	var urlstr = '/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=tblEmpGroup&selectName=SelectOnlyEmployeeM' ;
	openSelect("formatFileName","popedomUserIds",displayName,urlstr);	
}

function showDept(){
	var displayName=encodeURI("$text.get('oa.common.department')") ;
	var urlstr = '/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=tblEmpGroup&selectName=SelectDepartmentM' ;
	openSelect("formatDeptName","popedomDeptIds",displayName,urlstr);	
}

function openSelect(fieldName,fieldNameIds,displayName,urlstr){
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined") return ;
	var employees = str.split("|") ;
	
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = m(fieldName).options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++){
				if(existOption[i].value==field[0]){
					talg = true;
				}
			}
			if(!talg){
				m(fieldName).options.add(new Option(field[1],field[0]));
				m(fieldNameIds).value+=field[0]+",";
			}
		}
	}
}

function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function addMore(){
	
	var td = document.getElementById("more");	
	var br = document.createElement("br");
	var input = document.createElement("input");
	var button = document.createElement("img");
	
	input.type = "text";
	input.name = "voteAnswers";
	input.id = "voteAnswers";
	input.className="text";
	input.onkeyup=function(){
		textValue(this);
	}
	button.src = "/$globals.getStylePath()/images/del.gif";
	button.onclick = function(){
		
		td.removeChild(br);
		td.removeChild(input);
		td.removeChild(button);
	}
	td.appendChild(br);
	td.appendChild(input);
	td.appendChild(button);
}

function checkForm(){
	var title = form.voteTopic.value;
	var beginTime = form.beginTime.value;
	var endTime = form.endTime.value;
	
	var beginTime1 = beginTime.split("-") ;
	var endTime1 = endTime.split("-") ;
	var beginTime2 = new Date(beginTime1[0],beginTime1[1]-1,beginTime1[2]);
	var endTime2 = new Date(endTime1[0],endTime1[1]-1,endTime1[2]);
	var diff = endTime2 -beginTime2 ;
	if(!isNull(title,'$text.get("oabbs.com.lb.IssueTheme")')){
		return false;
	}
	var content = editor.html();
	if(content.length==0){
  		alert("$text.get("oabbs.com.lb.IssueExplain")"+"$text.get('common.validate.error.null')") ;
  		form.voteTopic.focus() ;
  		return false ;
  	}
  	
	if(!isNull(beginTime,'$text.get("alertCenter.lb.startDate")')){
		return false;
	}
	if(!isDate(beginTime)){
		alert('$text.get("alertCenter.lb.startDate")'+'$text.get("tableinfo.update.calerror4")');
		return false;
	}
	if(!isNull(endTime,'$text.get("alertCenter.lb.endDate")')){
		return false;
	}	
	if(!isDate(endTime)){
		alert('$text.get("alertCenter.lb.endDate")'+'$text.get("tableinfo.update.calerror4")');
		return false;
	}
	
	if (diff < 0){ 
		alert('$text.get("js.msg.startDLessED")');
		return false;
	}
	var diff2 = endTime2 - new Date(new Date().getYear(),new Date().getMonth(),new Date().getDate()) ;
    if (diff2 < 0){
		alert('$text.get("oa.common.time.rang.meg")');
		return false;
	}
	var talg = true;
	var isnull=false;
	var choiceAnwsers =  document.forms[0].voteAnswers;	
	var length = choiceAnwsers.length;
	if(document.getElementById("voteAnswers").value.length<=0){
	   isnull =  true;
	}
		
	for(var i=0;i<length;i++){
		if(choiceAnwsers[i].value.length>1000){
			talg =  false;
		}
		if(choiceAnwsers[i].value.length<=0){
			isnull =  true;
		}
	}
	
	if(talg == false){
		alert("$text.get("oabbs.com.msg.overMax")");
		return false;
	}
	if(isnull == true&&form.answerType.value!=3){
		alert("$text.get("oabbs.com.msg.downMin")");
		return false;
	}
	window.save=true;
	return true;	
}

function isExist(checkvalue){
	var existOption = document.getElementById("formatDeptName").options;
	var length = existOption.length;
	var talg = false ;
	for(var i=0;i<length;i++){
		if(existOption[i].value==checkvalue){
			talg = true;
		}
	}
	return talg ;
}
</script>
</head>

<body >
#if("$!addTHhead" == "true")
	#parse("./././body2head.jsp")
#end
<form action="/OABBSVoteAction.do?addTHhead=$!addTHhead" name="form" onSubmit="return checkForm()" method="post">
<input type="hidden" name="$globals.getOP("OP_ADD")">
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value=""/>
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value=""/>
<input type="hidden" name="topicId" value="$!topicId">
<input type="hidden" name="alertStatus" value="-1">

<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oabbs.com.title.IssueVote")</div>
	<div class="HeadingButton">
 		 <button type="submit" name="Submit" class="b2">$text.get("mrp.lb.save")</button>
 		 <button type="reset" class="b2">$text.get("common.lb.reset")</button>
 		 <button type="button" class="b2" onclick="location.href='/OABBSForumQueryAction.do?topicId=$!topicId&addTHhead=$!addTHhead'; ">$text.get("common.lb.back")</button>
	</div>
</div>

<div id="listRange_id">
<script type="text/javascript">
	var oDiv=document.getElementById("listRange_id");
	var sHeight=document.body.clientHeight-38;
	oDiv.style.height=sHeight+"px";
</script>
<div class="scroll_function_small_a">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oabbs_function_index" name="table">
		<thead>
			<tr>
				<td align="left" colspan="2" >$text.get("common.msg.currstation")：#if("$!parentName"=="")$text.get("oabbs.com.lb.bbsHead") #else $!parentName #end&nbsp;&gt;&gt;$text.get("oabbs.com.title.IssueVote")</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="oabbs_tr" width="10%">$text.get("oabbs.com.lb.IssueTheme")：&nbsp;&nbsp;</td>
				<td width="80%" >
					<input name="voteTopic" type="text" class="text" style="width:355px;"><font color="#FF0000">* </font>			
			  </td>
			</tr>
			#if($topicType.size()>0)
			<tr>
				<td align="right" valign="middle">$text.get("oa.workflow.type")：&nbsp;&nbsp;</td>
				<td width="*" valign="top">
			    <select name="topicTyle">
					#foreach($row in $topicType)
					<option value="$globals.get($row,1)">$globals.get($row,1)</option>
					#end
				</select>
			    </td>
			</tr>
			 #end
			<tr>
				<td class="oabbs_tr">$text.get("oabbs.com.lb.IssueExplain")：&nbsp;&nbsp;</td>
				<td>
					<textarea  name=voteRemark style="width:100%;height:300px;visibility:hidden;"></textarea>
			  </td>
			</tr>
			<tr>
				<td class="oabbs_tr">$text.get("oabbs.com.lb.IssueForm")：&nbsp;&nbsp;</td>
				<td>
					<input type="radio" name="voteType" checked="checked" value="0"/>$text.get("oa.inquisition.anonymity") 
					<input type="radio" name="voteType" value="1"/>$text.get("oa.inquisition.onymous") 
			    </td>
			</tr>
			<tr>
			 	<td class="oabbs_tr">$text.get("alertCenter.lb.startDate")≥：&nbsp;&nbsp;</td>
			 	<td><input name="beginTime" value="" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  class="text"><font color="#FF0000">* </font></td>
			</tr>	
			<tr>
			 	<td class="oabbs_tr">$text.get("alertCenter.lb.endDate")≤：&nbsp;&nbsp;</td>
			 	<td><input name="endTime" value="" onKeyDown="if(event.keyCode==13) openInputDate(this);" onClick="openInputDate(this);"  class="text"><font color="#FF0000">* </font></td>
			</tr>						
			<tr>
				<td class="oabbs_tr">$text.get("oa.inquisition.answerType")：&nbsp;&nbsp;</td>
				<td>
				#foreach($row in $globals.getEnumerationItems("answerType"))
				    <input type="radio" name="answerType" value="$row.value" #if($row.value=="single") checked #end/>$row.name
				#end
			    </td>
			</tr>
			<tr id="mytr">
				<td class="oabbs_tr">&nbsp;$text.get("oa.inquisition.option")：&nbsp;&nbsp;</td>
			    <td>
			      <div id="type_dx">
			      	
					<div id="more"><input name="voteAnswers" id="voteAnswers" class="text" onkeyup="textValue(this);">
					<img src="/$globals.getStylePath()/images/add.gif" onClick="addMore()"><font color="#FF0000">&nbsp;&nbsp;*</font></div>
					
				  </div>
				  <div id="type_wb" style="display:none;"></div>
				</td>
				
			</tr>
			<tr id="mytr">
				<td class="oabbs_tr">&nbsp;</td>
			    <td>
			      <div id="type_dx">
					<div id="more"><input type="checkbox" name="replayNote" value="1">$text.get("bbs.lb.replayNote")</div>
				  </div>
				  <div id="type_wb" style="display:none;"></div>
				</td>
				
			</tr>
		
		  </tbody>
		</table>
		<div style="height:30px;"></div>
</div>
</div>
</form>
</body>
<script language="javascript">
function textValue(texv){
	var textLang=150;

	if(texv.value.length>textLang){
		texv.value=texv.value.substring(0,textLang);
		alert("不能超过150个字符!");
		texv.focus();
	}
}
 
</script>
</html>
