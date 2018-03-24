<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增会议室</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<style type="text/css">
.inp{border:1px solid #ccc;border-radius:3px;padding:3px;}
</style>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" >
function newSubmit(){
	var address = jQuery("#address").val();
	var boardroomName = jQuery("#boardroomName").val();
	if(form.boardroomName.value == ""){
	    alert("会议室名称不能为空");
	    return false;
 	}
	if(form.address.value == ""){
	     alert("会议室地址不能为空");
	     return false; 
	}
	var ren=form.peopleNumber.value;
	if( ren != ""){
	    if(isNaN(ren)){
	       alert("人数请输入数字");
	       return false;
	   	}
	    if(ren.indexOf(".")!=-1){
	        alert("人数请输入整数");
	       return false;
	    }
	    if(parseInt(ren)>9999999999){
	      alert("人数不能超过9999999999");
	      return false;
	    }
	 }
	  var flag = jQuery("#flagOut").val();	
	  url=form.action;		 
	  jQuery.ajax({
	   type: "post",
	   url:url ,
	   data:{
	   		boardroomId :jQuery("#boardroomId").val(),
	   		describe : jQuery("#describe").val(),
			boardroomName : boardroomName,
			address : address,		
			peopleNumber : jQuery("#peopleNumber").val()
	   },
	   success: function(msg){	    
		   if(msg != "0"){
		   		if(flag == "Out"){	   			
		   			parent.jQuery.close("Prodiv");
		   			var li ="<option renshu=\""+jQuery("#peopleNumber").val()+"\" value=\""+msg+"\">"+boardroomName+"</option>";
		   			$("#1_9").after(li);
		   			parent.asyncbox.tips('操作成功','success');				   				   				   				   				   				   				   		
		   		}else{		   				   			
		   			parent.asyncbox.tips('操作成功','success');
		   			setTimeout("parent.location.reload();",2000);
		   		}			   				   				      	
			}
			if(msg == "0"){
				parent.jQuery.close("Prodiv");
				asyncbox.tips('添加失败!','error');				
			}
	 	}
	});
	
	//form.submit();	
	
}
</script>
</head>
<body onload="showStatus();">


#if("$request.getParameter('requestType')"=="UPDATE")
<form name="form" id="form" action="/OABoardroom.do?operation=2&requestType=UPDATE"  method="post" onsubmit="return check();">
<input type="hidden" name="boardroomId" id="boardroomId" value="$!request.getAttribute('boardroom').boardroomId"  />
#else
<form name="form"  id="form" action="/OABoardroom.do?operation=1&requestType=ADD"  method="post" onsubmit="return check();">
#end
<input type="hidden" id="flagOut" value="$!flagOut"  />

<table width="80%" border="0" style="margin:0 auto;margin-top:30px;">
  <tr>
    <td align="right"><span style="color:Red">名称:</span></td>
    <td><input class="inp" name="boardroomName" id="boardroomName" type="text" size="20" value="$!boardroom.boardroomName" />
  </tr>
  <tr>
    <td align="right"><span style="color:Red">地点:</span></td>
    <td><input class="inp" name="address" id="address" type="text" size="20"    value="$!boardroom.address"/>
  </tr>
 
  <tr>
    <td align="right"><span>最多可容纳人数:</span></td>
    <td><input class="inp" name="peopleNumber" type="text" id="peopleNumber" value="$!boardroom.peopleNumber" size="20"  /></td>
  </tr>
  <tr>
    <td align="right"><span>描述:</span></td>
    <td><textarea class="inp" name="describe" cols="40" id="describe" rows="6">$!boardroom.describe</textarea></td>
  </tr>
</table>
<div class="btn btn-small" style="margin: 15px 220px;" title="保存" onclick="newSubmit();">保存</div>
</form>
</body>
</html>