<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.common.addNews")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/formvalidate.vjs","",$text)"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script type="text/javascript">

var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="Notepadcontent"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});

function showUserGroup(fieldName,fieldNameIds){
var displayName=encodeURI("$text.get('oa.oamessage.usergroup')") ;
	var urlstr = '/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=tblEmpGroup&selectName=SelectEmpGroup' ;
		var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined") return ;
	var employees = str.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = m(fieldName).options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++)
			{
				if(existOption[i].value==field[0])
				{
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
	var str  = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=730px;dialogHeight=450px"); 
	if(typeof(str)=="undefined") return ;
	var employees = str.split("|") ;
	
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = m(fieldName).options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++)
			{
				if(existOption[i].value==field[0])
				{
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

function m(value){
	return document.getElementById(value) ;
}

function isExist(checkvalue)
{
	var existOption = document.getElementById("formatDeptName").options;
	var length = existOption.length;
	var talg = false ;
	for(var i=0;i<length;i++)
	{
		if(existOption[i].value==checkvalue)
		{
			talg = true;
		}
	}
	return talg ;
}


function checkForm()
{	
	var Content=editor.html();
	var newsTitle = form.newsTitle.value;
	if(!isNull(newsTitle,'$text.get("oa.common.title")'))
	{
		return false;
	}
	if(getStringLength(newsTitle)>600){
		alert("$text.get('oa.news.titleLength')") ;
		return false ;
	}
	//判断是否正确的输入


	if(!isTitle(newsTitle))
	{
	alert('$text.get("oa.common.title")'+'$text.get("common.validate.error.any")');
	    return false;
	}
	if(!isNull(Content,'$text.get("oa.common.newsContent")')){
		return false;
	}
	return true;	
}

function isNull(variable,message)
{
	if(variable=="" || variable==null)
	{
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else
	{
		return true;
	}
			
}

function chikeRadio(obj)
{
	if(obj.checked==true)
	{
		if(obj.value==1)
		{
		document.getElementById("_title").style.display='block';  
		document.getElementById("_context").style.display='block';
		document.getElementById("_trno").style.display=''		
		}else
		{
		document.getElementById("_title").style.display='none';
		document.getElementById("_context").style.display='none';
		document.getElementById("_trno").style.display='none'		
		}

	}
}
function delOpation()
{
	
	var formatName=form.formatFileName;
			if(formatName.selectedIndex==-1)
		{
		alert("$text.get('oa.lb.common.pleaseChoose.remove')");
		}
		if(formatName.selectedIndex<0)return false ;
	var value = formatName.options[formatName.selectedIndex].value;
	var appcers = form.popedomUserIds.value;
	appcers = appcers.replace(value+",","");
	form.popedomUserIds.value =appcers
	formatName.options.remove(formatName.selectedIndex);

}

function delOpation2()
{
	
	var formatName=form.formatDeptName;
		if(formatName.selectedIndex==-1)
		{
		alert("$text.get('oa.lb.common.pleaseChoose.remove')");
		}
		if(formatName.selectedIndex<0)return false ;
	var value = formatName.options[formatName.selectedIndex].value;
	var appcers = form.popedomDeptIds.value;
	appcers = appcers.replace(value+",","");
	form.popedomDeptIds.value =appcers
	formatName.options.remove(formatName.selectedIndex);

}
function resetEditor(){	
	var existfOption = form.formatFileName.options;
	var existdOption = form.formatDeptName.options;
	var existeOption = form.EmpGroup.options;
	for(var i=0;i<existfOption.length;i++){
		existfOption.remove(i);
		i=i-1;
	}
	for(var i=0;i<existdOption.length;i++){
		existdOption.remove(i);
		i=i-1;
	}
	for(var i=0;i<existeOption.length;i++){
		existeOption.remove(i);
		i=i-1;
	}
	editor.html("") ;
}
function deleteOpation(fileName,popedomId)
{
		var formatName=m(fileName);
		if(formatName.selectedIndex==-1)
		{
		alert("$text.get('oa.lb.common.pleaseChoose.remove')");
		}
		if(formatName.selectedIndex<0)return false ;
		var value = formatName.options[formatName.selectedIndex].value;
		var appcers = m(popedomId).value;
		appcers = appcers.replace(value+",","");
		m(popedomId).value =appcers
		formatName.options.remove(formatName.selectedIndex);
}
</script>
</head>
<body>
<form action="/OACompanyNewsInfo.do?operation=$globals.getOP("OP_ADD")&type_add=addNewsInfo" onSubmit="return checkForm();" name="form" method="post">
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value=""/>
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value=""/>
<input type="hidden" name="picFiles" value="$!globals.get($arr_newsInfo,14)">
<input type="hidden" name="delPicFiles" value="">
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("oa.common.addNews")</div>
	<ul class="HeadingButton">
				<li><button type="button" onClick="location.href='/OACompanyNewsInfo.do?operation=4'" class="b2">$text.get("common.lb.back")</button></li>
					
			</ul>
</div>
<div id="listRange_id">
<script type="text/javascript">
var oDiv=document.getElementById("listRange_id");
var sHeight=document.body.clientHeight-38;
oDiv.style.height=sHeight+"px";
</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="oabbs_tr" width="20%">$text.get("oa.common.title")：</td>
				<td width="80%">
					<input name="newsTitle" type="text" size="60" class="text"><font color="#FF0000">* </font>
				</td>
			</tr><tr>
			 <td class="oabbs_tr">$text.get("oa.common.newsType")：</td>
			 <td><label for="select"></label>
				<select name="newsType" id="select">
		 #foreach($row_newsType in $globals.getEnumerationItems("NewsType"))
		  <option value="$row_newsType.value" selected>$row_newsType.name</option>
	   #end
		</select>
		      </td>
			</tr>	
						<tr>
			 <td class="oabbs_tr">$text.get("oa.common.newContent")：</td>
			 <td>
			<textarea name="Notepadcontent" style="width:800px;height:300px;visibility:hidden;"></textarea>
			   </td>
			</tr>
			<tr>
			 <td class="oabbs_tr">&nbsp;</td>
			 <td>
			<button name="picbuttonthe222" onClick="openAttachUpload('/news/','PIC');" class="b4">$text.get("upload.lb.picupload")</button>
					<div id ="status" style="visibility:hidden;color:Red"></div>
					<div id="files_preview">
					</div>
			   </td>
			</tr>						
			<tr>
				<td class="oabbs_tr">$text.get("oa.common.isPublish")：</td>
				<td ><input name="isUsed" type="radio" id="radiobutton" value="1" checked> $text.get("oa.common.immediatelyPublish")<input type="radio" name="isUsed" value="0" id="radio">
				  $text.get("oa.common.sincePublish")</td>
			</tr>
			<tr>
			 <td class="oabbs_tr">$text.get("oa.calendar.wakeupType")：</td>
			 <td>
		 		#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
		 			<input type="checkbox" name="wakeUpMode" value="$row_wakeUpType.value"/>$row_wakeUpType.name
	   			#end
		     </td>
			</tr>	
			<tr>
			<td class="oabbs_tr">$text.get("oa.common.garget")：</td>
			<td>
			 <input name="isAlonePopedmon" onClick="chikeRadio(this)" type="radio" checked value="0" id="radio" >
			  $text.get("oa.common.all")
			 <input name="isAlonePopedmon" type="radio" onClick="chikeRadio(this)" id="radiobutton" value="1" >
			  $text.get("oa.common.appoint")
			 </td>
			</tr>
			<tr id="_trno" style="display:none">
				<td class="oabbs_tr" id="_title" style="display:none">&nbsp;</td>
				<td valign="middle">
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="showDept();"  alt="$text.get("oa.common.adviceDept")" class="search">&nbsp;<a href="javascript:void(0)" title="$text.get("oa.select.dept")" onClick="showDept();">$text.get("oa.select.dept")</a></div>
				<select name="formatDeptName" id="formatDeptName" multiple="multiple">
				</select></div>
				<div class="oa_signDocument2">
				<img onClick="delOpation2()" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
				</div>
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="show2();" alt="$text.get("oa.common.advicePerson")" class="search">&nbsp;<a href="javascript:void(0)" title="$text.get("oa.select.personal")" onClick="show2();">$text.get("oa.select.personal")</a></div>
				<select name="formatFileName" id="formatFileName" multiple="multiple">
				</select></div>
				<div class="oa_signDocument2">
				<img onClick="delOpation()" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
				</div>
				
				<input type="hidden" name="EmpGroupId" id="EmpGroupId" value="">
						<div class="oa_signDocument1" id="_context">
						<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="showUserGroup('EmpGroup','EmpGroupId');" alt="$text.get("oa.oamessage.usergroup")" class="search">&nbsp;<a href="javascript:void(0)" style="cursor:pointer" title="$text.get('oa.oamessage.usergroup')" onClick="showUserGroup('EmpGroup','EmpGroupId');">$text.get("oa.oamessage.usergroup")：</a></div>
							<select name="EmpGroup" id="EmpGroup" multiple="multiple">
							</select>
						</div>
						<div class="oa_signDocument2">
							<img onClick="deleteOpation('EmpGroup','EmpGroupId')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
						</div>
			</td>
		  </tr>	
		  	<tr>
			<td class="oabbs_tr">$text.get("oa.whetherAgreeReply.label")：</td>
			<td>
			 <input name="whetherAgreeReply" type="radio" checked value="1" >
			  $text.get("oa.common.yes")
			 <input name="whetherAgreeReply" type="radio" value="0" >
			  $text.get("oa.common.no")		
			</td>
			</tr>
		  <tr>
			<td class="oabbs_tr">$text.get("oa.readingMark.save")：</td>
			<td>
			 <input name="isSaveReading" type="radio" checked value="1" >
			  $text.get("oa.common.yes")
			 <input name="isSaveReading" type="radio" value="0" >
			  $text.get("oa.common.no")		
			</td>
			</tr>
		
		   <tr>
			 <td colspan="2" class="oabbs_tc"> 
			 	<button type="submit" name="Submit" class="b2">$text.get("mrp.lb.save")</button>
			 	<button type="reset" onClick="resetEditor();" class="b2">$text.get("common.lb.reset")</button>
			 </td>
          </tr>
		  </tbody>
		</table>	
</div>
</form>
</body>
</html>
