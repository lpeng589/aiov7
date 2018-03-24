
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>

<script src="/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/public_utils.js"></script> 
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>

<title>Insert title here</title>
<style type="text/css" >
.listRange_1_photoAndDoc_1{margin-left:0px;width:98%;}
</style>
<script type="text/javascript" >
function boxclose(){
	parent.jQuery.close('updatebox');
}

function successbox(){
	if(parent.jQuery == undefined){
		alert('修改成功');
		location.href='/CRMDistWorkSearchAction.do';
	}else{
		parent.asyncbox.alert('修改成功','提示',function(){parent.form.submit();parent.jQuery.close('updatebox');});
	}
}

function addsuccessbox(){
	if(parent.jQuery == undefined){
		alert('添加成功');
		location.href='/CRMDistWorkSearchAction.do';
	}else{
		
		
		parent.asyncbox.alert('添加成功','提示',function(){parent.form.submit();parent.jQuery.close('updatebox');});
		
		
	}
}

function formsubmited(){
	
	form.submit();
}

var usermap=new Array();
function deptPopdestwork(){
	inputid="userName";
	var chooseDataURL = "&chooseData="+document.getElementById("userId").value;
	var urls = "/Accredit.do?popname=userGroup&inputType=checkbox" + chooseDataURL;
	var title = "请选择个人";
	asyncbox.open({
	   id : 'Popdiv',
	   title : title,
	　　　url : urls,
	　　　width : 755,
	　　　height : 435,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	//判断 action 值。
　　　　　	if(action == 'ok'){
　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID。
				var str = opener.strData;
　　　　　　　	    shuzu=str.split("|");
　　　　　　　	   var  userIds="";
　　　　　　　	   var   userNames="";
　　　　　　　	    for(var shu in shuzu){
　　　　　　　	    	var user= shuzu[shu];
　　　　　　　	    	if(user!=""){
　　　　　　　	    		usermap[user.split(";")[1]]=user.split(";")[0];
　　　　　　　	    	userIds += user.split(";")[0]+",";
　　　　　　　	    	userNames += user.split(";")[1]+";";
　　　　　　　	    	}
　　　　　　　	    }
　　　　　　　	   
　　　　　　　	   document.getElementById("userId").value=userIds;
　　　　　　　	  document.getElementById("userName").value=userNames;
　　　　　		}
　　　	}
　	});
}

var  inputid="";
function textclick(){
	inputid="crmclient";
	deptPopForAccount('CrmClickGroup','crmclient','clientId');
	
}

//弹出框双击回填内容

function fillData(datas){
	var user= datas;
	if(inputid=="userName"){
		if(document.getElementById("userName").value.indexOf(user.split(";")[1])==-1){
			document.getElementById("userId").value=document.getElementById("userId").value+user.split(";")[0]+",";
		　	  document.getElementById("userName").value=document.getElementById("userName").value+user.split(";")[1]+";";
		　	 usermap[user.split(";")[1]]=user.split(";")[0];
		　	 }
	}else if(inputid=="crmclient"){
		//document.getElementById("clientId").value=user.split(";")[0];
	　	  document.getElementById("crmclient").value=user.split(";")[1];
	}
	
　	 parent.jQuery.close('Popdiv');
	
}


function  textdel(self){
	var userids="";
     var text=self.value;
     if(text == ""){
    	 document.getElementById("userId").value="";
     }
    if(text != "" &&  text.indexOf(";")!=-1){
    	document.getElementById("userId").value="";
    	var shuzu=text.split(";");
    	for(var shu in shuzu){
    		if(shuzu[shu]!=""){
    			if(usermap[shuzu[shu]]!=undefined){
    				userids += usermap[shuzu[shu]] +",";
    			}
    		}
    	}
    	document.getElementById("userId").value=userids;
    }
    
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/CRMDistWorkAction.do" name="form" method="post" target="formFrame"  >
#if("$!distwork.id"!="")
<input name="operation" type="hidden" value="2" /> 
#else
<input name="operation" type="hidden" value="1" /> 
#end


<input type="hidden" name="attachFiles" value="$!distwork.crmAffix" />
<input type="hidden" name="delFiles" value="" />
<input name="distworkid" type="hidden" value="$!distwork.id" /> 

<div style="float:right;  margin:5px ;" >
<a onclick="formsubmited()"  class="asyncbox_btn"><span>&nbsp;保&nbsp;&nbsp;存&nbsp;</span></a>
<a onclick="boxclose()"  class="asyncbox_btn"><span>&nbsp;取&nbsp;&nbsp;消&nbsp;</span></a>
</div>

#if("$!distwork.id"!="")
<div class="listRange_1_photoAndDoc_1">

<span>现在状态：</span>
<input type="radio" value="0" name="statusId"  checked="checked" />未结束
<input type="radio" value="1" name="statusId" #if("$!distwork.statusId"=="1")checked="checked" #end  />已结束	
<input type="radio" value="2" name="statusId"  #if("$!distwork.statusId"=="2")checked="checked" #end />取消				
</div>
#end


<div class="listRange_1_photoAndDoc_1">
<div style="float:left;">
<span>关联客户&nbsp;&nbsp;：</span>
<input type="hidden"  id="clientId" name="ref_id" value="$!distwork.ref_id"/>
<input id="crmclient"  value="$!clientName"  readonly="readonly" type="text" ondblclick="textclick()" />
<img src="/style1/images/St.gif" onClick="textclick()"/>
</div>
<div style="float:left;">
<span>分类：</span>
<select class="ls3_in" id="taskType" name="taskType" >								
				#foreach($item in $globals.getEnumerationItems("CRMTaskType"))	 
<option value="$item.value"#if("$!distwork.taskType" == "$item.value") selected="selected" #end>$item.name</option>
#end

							
								</select>
</div>
</div>

<div class="listRange_1_photoAndDoc_1">

<span>任务分派给：</span>

<input type="hidden"  id="userId" name="userId" value="$!distwork.userId"/>
<input id="userName" readonly="readonly" onchange="textdel(this);" class="ls3_in" name="userName" 
value="#if("$!distwork.userId"!="")#foreach($str in $!distwork.userId.split(","))$!globals.getEmpFullNameByUserId($str);#end#end"  onDblClick="deptPopdestwork();"  >
<img src="/style1/images/St.gif" onClick="deptPopdestwork();"/>
								
</div>
<div class="listRange_1_photoAndDoc_1" >
<span>完成期限&nbsp;&nbsp;：</span><input type="text" value="$!distwork.finishTime" name="finishTime" onclick="WdatePicker({lang:'zh_CN'});" />
</div>


<div class="listRange_1_photoAndDoc_1">
<span>内容：</span>
</div>
<div class="listRange_1_photoAndDoc_1" style=" height:150px;overFlow-y:scroll;">
<textarea name="content" rows="8" >$!distwork.content</textarea>
</div>
<div class="listRange_1_photoAndDoc_1" style=" height:50px;">

<a href="javascript:void(0)" onclick="openAttachUpload('/crm/distwork/');window.save=true;">&nbsp;<span></span>
								<img src="/style1/images/oaimages/uploading.gif" /><font style="margin-left: 2px;">$text.get("upload.lb.affixupload")</font></a>
								<div id="status" style="visibility:hidden;color:Red"></div>
								<div id="files_preview">
								      #if($!distwork.crmAffix.indexOf(";") > 0)
								  #foreach ($str in $globals.strSplit($!distwork.crmAffix,';'))
								  	 <div  id ="$str" style ="height:18px; color:#ff0000;">
								  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
								  	 $str<input type=hidden name="attachFile" value="$str"/></div>					 
								  #end	
							 	 #end
								
								</div>	
</div>

</form>
</body>
</html>