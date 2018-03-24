<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("oa.kenowledgeCenter.licensed")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript">
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
function m(value){
	return document.getElementById(value) ;
}
function delOpation()
{
	 var formatName=form.formatFileName;
	 	 if(formatName.selectedIndex==-1)
		{
		alert("$text.get('oa.lb.common.pleaseChoose.remove')");
		}
		if(formatName.selectedIndex<0)return false ;
	 for(var i=formatName.length-1;i>=0;i--){
	 	if(formatName[i].selected && formatName[i].value!='1')
	 	{
	 		formatName.options.remove(i);
	 	}
	 }
	 
}

function delOpation2()
{
	 var formatName=form.formatDeptName;
	 if(formatName.selectedIndex==-1)
		{
		alert("$text.get('oa.lb.common.pleaseChoose.remove')");
		}
		if(formatName.selectedIndex<0)return false ;
		
	 for(var i=formatName.length-1;i>=0;i--){
		 if(formatName[i].selected)
		 {
		 	formatName.options.remove(i);
		 }
	 }
	 
}
function openInputDate(obj)
{
	var d = new Date();
	LaunchCalendar(obj,d);
}
function show2()
{
	//show();
	var displayName=encodeURI("$text.get('oa.common.person')") ;
	var urlstr = '/UserFunctionAction.do?operation=$globals.getOP('OP_POPUP_SELECT')&tableName=tblEmpGroup&selectName=SelectOnlyEmployeeM' ;
	
	openSelect("formatFileName","popedomUserIds",displayName,urlstr);	
}

function showDept(){
	//mywinds.show() ;
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
function show3()
{
//tree.expandAll();

wind.show();
//document.getElementById("Layer1").style.display="block";
}
function checkForm()
{
	var fileName = document.getElementById("fileName").value;
	//var file = document.getElementById("file_0").value;
	var folderId = document.getElementById("groupName").value;
	var fileTitle = document.getElementById("fileTitle").value;
	var Editor =FCKeditorAPI.GetInstance("FCKeditor1");
	var Content=Editor.GetXHTML();
	var childnum = document.getElementById("files").getElementsByTagName("input").length;      	
	if(folderId==null || folderId.length<=0)
	{
		alert('$text.get("oa.kenowledgeCenter.appointDirectory")');
		return false;
	}
	 if(!isNull(fileTitle,'$text.get("message.lb.title")'))
	{

		return false;
	}
	 if(!isNull(Content,'$text.get("message.lb.content")'))
	{

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
function changeFileName(obj)
{
	var filePath = obj.value;
	var starIndex = filePath.lastIndexOf("\\");
	var endIndex = filePath.lastIndexOf(".");
	var fileName = filePath.substr(starIndex+1,(endIndex-starIndex-1));
	document.getElementById("fileName").value = fileName;
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
function submitForm()
{
	var popedomUserIds=""; 
	var popedomDeptIds="";
	var datas = document.getElementById("formatFileName").options;
	var deptDatas = document.getElementById("formatDeptName").options ;
	var deptDatas = document.getElementById("formatDeptName").options ;
	
	for(var i=0;i<datas.length;i++)
	{
		var value = datas[i].value+",";
		popedomUserIds = popedomUserIds+value;
	}
	for(var j=0;j<deptDatas.length;j++)
	{
		var value = deptDatas[j].value+",";
		popedomDeptIds = popedomDeptIds+value ;
	}
	popedomUserIds = popedomUserIds.substr(0,popedomUserIds.length-1);
	popedomDeptIds = popedomDeptIds.substr(0,popedomDeptIds.length-1) ;
	document.getElementById("popedomUserIds").value = popedomUserIds;
	document.getElementById("popedomDeptIds").value = popedomDeptIds;
	
	AjaxRequest('/UtilServlet?operation=folderRight&tableName=$request.getParameter("tableName")&keyId=$request.getParameter("keyId")&popedomUserIds='+popedomUserIds+"&popedomDeptIds="+popedomDeptIds+"&EmpGroupIds=");
	window.close();

}
function isExists(checkvalue)
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


function insertNextFile(obj) 
  { 


var childnum = document.getElementById("files").getElementsByTagName("input").length;      
      var id = childnum -1;
      var fullName = obj.value;


      var fileHtml = '';
      fileHtml += '<div  id ="file_preview' + id + '" style ="height:18px; color:#ff0000;">';
      //fileHtml += '<img  width =30 height =30 src ="images/file.gif" title="' + fullName + '"/>';
      fileHtml += '<a href="javascript:;" onclick="removeFile(' + id + ');"><img src="/$globals.getStylePath()/images/del.gif"></a>&nbsp;&nbsp;';
      fileHtml += fullName.substr(fullName.lastIndexOf('\\')+1) +'  </div>';
  
      var fileElement = document.getElementById("files_preview");
      fileElement.innerHTML = fileElement.innerHTML + fileHtml;    
      obj.style.display = 'none'; 
      addUploadFile(childnum); 
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
		m(popedomId).value =appcers;
		formatName.options.remove(formatName.selectedIndex);
}
function rt(){
	for(var i=0;i<m("formatDeptName").options.length;i++){
		m("formatDeptName").options.remove(i);
	}
	for(var i=0;i<m("formatFileName").options.length;i++){
		m("formatFileName").options.remove(i);
	}
	
	//for(var i=0;i<m("EmpGroup").options.length;i++){
	//	m("EmpGroup").options.remove(i);
	//}
	//m("EmpGroupId").value="";
	m("popedomUserIds").value="";
	m("popedomDeptIds").value="";
}
</script>
</head>
<body >
 <div class="oamainhead">
<div class="HeadingButton">
	<li><button onclick="window.close();">$text.get("common.lb.close")</button></li>
</div>
</div>
    <div id="oalistRange">
	<form id="form" name="form" method="post"  action="/OAKnowledgeCenter.do?operation=1&type=folderRight" >

<input type="hidden" name="folderId" id="groupId" value="$!folder.id"/>
<input type="hidden" name="popedomUserIds" id="popedomUserIds" value=""/>
<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value=""/>
		<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" class="oalistRange_list_function" name="table">
		<thead>
			<tr>
				<td colspan="2" class="oabbs_tl">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
			<tr id="_trno" >
				<td height="176" class="oabbs_tr" id="_title" style="display:none">$text.get("oa.common.garget")：</td>
				<td valign="middle">
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="showDept();"  alt="$text.get("oa.common.adviceDept")" class="search"><a href="#" onclick="showDept();">&nbsp;$text.get("oa.select.dept")</a></div>
				<select name="formatDeptName" id="formatDeptName" multiple="multiple">
				#foreach($dept in $targetDept)
					<option value="$!globals.get($dept,0)">$!globals.get($dept,1)</option>
				#end
				</select></div>
				<div class="oa_signDocument2">
				<img onClick="delOpation2()" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">				</div>
				<div class="oa_signDocument1" id="_context">
				<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="show2();" alt="$text.get("oa.common.advicePerson")" class="search"><a href="#" onclick="show2();">&nbsp;$text.get("oa.select.personal")</a></div>
				<select name="formatFileName" id="formatFileName" multiple="multiple">
				#foreach($user in $popemdomUsers)
					<option value="$!globals.get($user,0)">$!globals.get($user,1)</option>
				#end
				</select></div>
				<div class="oa_signDocument2">
				<img onClick="delOpation()" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">				</div>		
				
				<!-- 
				<input type="hidden" name="EmpGroupId" id="EmpGroupId" value="">
						<div class="oa_signDocument1" id="_context">
						<div class="oa_signDocument_3"><img src="/$globals.getStylePath()/images/St.gif" onClick="showUserGroup('EmpGroup','EmpGroupId');" alt="$text.get("oa.oamessage.usergroup")" class="search">&nbsp;$text.get("oa.oamessage.usergroup")：</div>
							<select name="EmpGroup" id="EmpGroup" multiple="multiple">
							#foreach($grp in $targetEmpGroup)
								<option value="$!globals.get($grp,0)">$!globals.get($grp,1)</option>
							#end
							</select>
						</div>
						<div class="oa_signDocument2">
							<img onClick="deleteOpation('EmpGroup','EmpGroupId')" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" class="search">
						</div>
						
					</td>
					 -->
		  </tr>	
				
			<tr>
					  <td colspan="2" class="oabbs_tc"> <button type="button" onClick="submitForm()" name="Submit" class="b2">$text.get("common.lb.save")</button>
			   <button type="reset" onClick="rt()" class="b2" >$text.get("common.lb.reset")</button></td>
          </tr>
		  </tbody>
		</table>
	  </form>
</div>
</body>
</html>
