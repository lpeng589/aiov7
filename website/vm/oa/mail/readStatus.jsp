<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<title>查阅状态</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312"/>
<link type="text/css" rel="stylesheet" href="/style/css/sharingStyle.css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script src="/js/ccorrect_bt.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<link rel="stylesheet" type="text/css" href="/style/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="/style/themes/icon.css"/>
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<style type="text/css">
/*  列表型表格样式  */
.TableList{
   border:1px #cccccc solid;
   line-height:21px;
   font-size:9pt;
   border-collapse:collapse;
   padding:3px;
}
.TableList td{
   padding:3px;
}
.TableList .TableHeader td,
.TableList td.TableHeader{
   height:30px !important;
   height:32px;
   background:#c4de83;
   border-bottom:1px #9cb269 solid;
   border-right:1px #9cb269 solid;
   font-weight:bold;
   text-align:center;
   color:#383838;
   padding:0px;
}
.TableList .TableHeader td.TableCorner{
   background:#99c729;
}
.TableList .TableLine1 td,
.TableList td.TableLine1{
   background:#FFFFFF;
   border-bottom:1px #cccccc solid;
}
.TableList .TableLine2 td,
.TableList td.TableLine2{
   background:#FFFFFF;
   border-bottom:1px #cccccc solid;
}
.TableList .TableData td,
.TableList td.TableData{
   background:#FFFFFF;
   border-bottom:1px #cccccc solid;
}
.TableList .TableContent td,
.TableList td.TableContent{
   background:#f2f2f2;
   border-bottom:1px #cfcfcf solid;
}
.TableList .TableFooter td,
.TableList .TableControl td,
.TableList td.TableFooter,
.TableList td.TableControl{
   background: #FFFFFF;
   border:1px #cccccc solid;
}
.TableList .TableRed td,
.TableList td.TableRed
{
   background:#edf6db;
}
.TableList .TextColor1 td,
.TableList td.TextColor1
{
   COLOR: #FF6600;
}
.TableList .TextColor2 td,
.TableList td.TextColor2
{
   COLOR: #FF0000;
}

.TableList .TableLeft td,
.TableList td.TableLeft{
   background:#c4de83;
   border-bottom:1px #a7bd74 solid;
}

.TableList .TableRowHover td,
.TableList td.TableRowHover
{
   background:#edf6db;
}

.TableList .TableRowActive td,
.TableList td.TableRowActive
{
   background:#e5e5e5;
}
.TableList .TableControl td,
.TableList td.TableFooter,
.TableList td.TableControl{
   background: #FFFFFF;
   border:1px #cccccc solid;
}
.TableControl{ background: #FFFFFF}
input.SmallButtonA{width:60px;height:21px;color:#36434E;background:url("/style/images/btn_a.png") no-repeat;border:0px;cursor:pointer;}
</style>


<script> 

function sendMsg(mailId,empId){
		var html = '<div align="center" style=\"margin-top: 2px;\"><textarea name=\"mytextarea\" id =\"mytextarea\" cols="36" style=\"height:80px;\" >您好，请尽快查收我的邮件：$!emailTitle</textarea></div>';
		var value = "";		
		if (!mailId){
				
				formObj = document.getElementsByName("form")[0];
				if(delHasChild("pho")){
					asyncbox.alert('$text.get("common.exist.childcategory")!','$text.get("clueTo")');
					return false;
				}
				
				if(!isChecked("pho")){
					//$.messager.alert('$text.get("clueTo")','$text.get("common.msg.mustSelectOne")!','info');
					asyncbox.alert('$text.get("common.msg.mustSelectOne")!','$text.get("clueTo")');
					return false;
				}
				
				asyncbox.open({
				　　　html : html,
					 width : 350,
				　　　height : 200,
					 title : '发送通知',
				　　　btnsbar : jQuery.btn.OKCANCEL,
				　　　callback : function(action,opener){
						
				　　　　　if(action == 'ok'){
				　　　　　　	var name = $("#mytextarea").val();
							if(name == null || name.length <=0){
								asyncbox.alert("通知内容$text.get('isNotNull')",'$text.get("clueTo")');
								return false;
							}	 
							if(getStringLength(name)>200){
								asyncbox.alert("通知内容"+"$text.get('oa.common.not.more.than')"+200+"$text.get('oa.common.word.countOfCharacter')",'$text.get("clueTo")');
								return false ;
							}
							var bool1 = isHaveSpecilizeChar(name);
							if (bool1==false) {
								asyncbox.alert("通知内容$text.get('contain.specilize')",'$text.get("clueTo")');
								return false;
							}
							var groupId = "$groupId";
					        var emailType = "$emailType";
					  		var favoriteName = encodeURI(name) ;
					  		
					  		if (mailId) {//存在 单独发送 
					  			var favoriteURL = encodeURIComponent("/EMailAction.do?operation=$globals.getOP('OP_DETAIL')&groupId="+groupId+"&emailType="+emailType+"&iframe=true&keyId="+mailId);
						        var str="/AdviceAction.do?module=dedailAdviceInfo&favoriteName="+favoriteName+"&favoriteURL="+favoriteURL+"&receive="+empId+"&relationId="+mailId;
						 		AjaxRequest(str);
						    	value = response;
						    	
					  		} else {
					  			//var ids = $(":checkbox:checked");
					 	     	var phostr = "";
					 	     	jQuery("input[type='checkbox'][name='pho']").each(function () { 
				                   if (jQuery(this).attr("checked") == "checked" || jQuery(this).attr("checked") == true) { 
				                       	var v = jQuery(this).val();
						 	     		var uId = jQuery(this).attr("accept");
						 	     		var favoriteURL = encodeURIComponent("/EMailAction.do?operation=$globals.getOP('OP_DETAIL')&groupId="+groupId+"&emailType="+emailType+"&iframe=true&keyId="+v);
						 	     		var str="/AdviceAction.do?module=dedailAdviceInfo&favoriteName="+favoriteName+"&favoriteURL="+favoriteURL+"&receive="+uId+"&relationId="+v;
								 		AjaxRequest(str);
								    	value = response;
				                   } 
				               }); 
					  		}
					  		if(value!="0"&&value!=""){
								asyncbox.tips("通知成功",'success',1000);			
							}
							else{
								asyncbox.tips("通知失败",'success',1000);			
							}
						 	jQuery("#folder").val("");
				　　　　　}
				　　　　　if(action == 'cancel'){
				　　　　　　jQuery("#folder").val("");
				　　　　　}
				　　　　　if(action == 'close'){
				　　　　　　jQuery("#folder").val("");
				　　　　　}
				　　　}
				　});
				
				
		} else {
			asyncbox.open({
		　　　html : html,
			 width : 350,
		　　　height : 200,
			 title : '发送通知',
		　　　btnsbar : jQuery.btn.OKCANCEL,
		　　　callback : function(action,opener){
		　　　　　if(action == 'ok'){
		　　　　　　	var name = $("#mytextarea").val();
					if(name == null || name.length <=0){
						asyncbox.alert("通知内容$text.get('isNotNull')",'$text.get("clueTo")');
						return false;
					}	 
					if(getStringLength(name)>200){
						asyncbox.alert("通知内容"+"$text.get('oa.common.not.more.than')"+200+"$text.get('oa.common.word.countOfCharacter')",'$text.get("clueTo")');
						return false ;
					}
					var bool1 = isHaveSpecilizeChar(name);
					if (bool1==false) {
						asyncbox.alert("通知内容$text.get('contain.specilize')",'$text.get("clueTo")');
						return false;
					}
					var groupId = "$groupId";
			        var emailType = "$emailType";
			  		var favoriteName = encodeURI(name) ;
			  		
			  		if (mailId) {//存在 单独发送  
			  			var favoriteURL = encodeURIComponent("/EMailAction.do?operation=$globals.getOP('OP_DETAIL')&groupId="+groupId+"&emailType="+emailType+"&iframe=true&keyId="+mailId);
				        var str="/AdviceAction.do?module=dedailAdviceInfo&favoriteName="+favoriteName+"&favoriteURL="+favoriteURL+"&receive="+empId+"&relationId="+mailId;
				 		AjaxRequest(str);
				    	value = response;
				    	
			  		} else {
			  			var ids = jQuery(":checkbox:checked");
			 	     	var phostr = "";
			 	     	for (var i = 0;i<ids.length;i++) {
			 	     		var p = ids[i];
			 	     		var v = jQuery(p).val();
			 	     		var uId = jQuery(p).attr("accept");
			 	     		var favoriteURL = encodeURIComponent("/EMailAction.do?operation=$globals.getOP('OP_DETAIL')&groupId="+groupId+"&emailType="+emailType+"&iframe=true&keyId="+v);
			 	     		var str="/AdviceAction.do?module=dedailAdviceInfo&favoriteName="+favoriteName+"&favoriteURL="+favoriteURL+"&receive="+uId+"&relationId="+v;
					 		AjaxRequest(str);
					    	value = response;
			 	     	}
			  		}
			  		if(value!="0"&&value!=""){
			  			asyncbox.tips("通知成功",'success',1000);
					}
					else{
						asyncbox.tips("通知失败",'success',1000);			
					}
				 	jQuery("#folder").val("");
		　　　　　}
		　　　　　if(action == 'cancel'){
		　　　　　　jQuery("#folder").val("");
		　　　　　}
		　　　　　if(action == 'close'){
		　　　　　　jQuery("#folder").val("");
		　　　　　}
		　　　}
		　});
		
		}
}
</script>

<body>
<form name="form" action="" method="post">
<div>
<table class="TableList" width="95%" cellpadding="0" cellspacing="0" >
   <tr  style="COLOR:#383838; FONT-WEIGHT: bold; FONT-SIZE: 9pt; background:#c4de83;line-height:18px;">
      <td  align="center" width="10%">选择</td>
      <td  align="center" width="10%">状态</td>
      <td  align="center" width="15%">收件人</td>
      <td  align="center" width="25%">所在部门</td>
      <td  align="center" width="25%">阅读时间</td>
      <td  align="center" width="15%">操作</td>
    </tr>
 </table>
 </div>
 <div style="overflow:hidden;height:180px;overflow:auto;">
 <table class="TableList" width="95%"  cellpadding="0" cellspacing="0">
 #foreach($rev in $receives)        
	<tr class = "" onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
	      <td align="center" width="10%"><input type="checkbox" accept="$!globals.get($rev,6)" title="" name="pho"  #if($!globals.get($rev,3)!=0) disabled="disabled" #end  value="$!globals.get($rev,5)" ></td>
	      <td align="center" width="10%">#if($globals.get($rev,3) == 1)<img src='/style/images/email_open.gif' alt='收件人已读'/> #elseif($globals.get($rev,3) == 0)<img src='/style/images/email_close.gif' alt='收件人未读'/>#else<img src='/style/images/email_delete.gif' alt='收件人未读,发件人收回'/>#end</td>
	      <td  align="center"  width="15%">
			  $!globals.get($rev,0)<span style="color:#FF0000;"></span>
	      </td>
	      <td  align="center" width="25%">
	     		 $!globals.get($rev,1)
	       </td>
	       <td  align="center" width="25%">
	        	$!globals.get($rev,2)
	       </td>
	       <td align="center"  width="15%">
	       #if($!globals.get($rev,3)==0)
	     		<a class="A1" style="cursor: pointer;"  href="javascript:sendMsg('$!globals.get($rev,5)','$!globals.get($rev,6)');" >发通知</a>&nbsp;
	       #else
	        	 <a class="A1"  style="cursor: text;color: #CAC8BB" >发通知</a>&nbsp;
	       #end
	       </td>
	</tr>
	#end
	</table>
</div>
<div style="margin-top:10px; height: 80%; ">
	<table  class="TableList" width="95%" style="float:left;">
		<tr class="TableControl">
			<td colspan="5" style="border-right-width:0">
			&nbsp;
			 <input type="checkbox"  id = "checkAll" onclick="isCheckAll(this.value,'pho')"  value="true" />$text.get("common.lb.selectAll")
		 	 <input type="button"  value="撤销   "  class="SmallButtonA" onClick="multinomialDel('pho','form')"/> 
		     <input type="button" style="width: 50px;" value="发通知" class="SmallButtonA" onClick="sendMsg();"/> 
			</td>
			<td style="text-align:right;border-left-width:0;">共 $!receives.size() 个收件人</td>
		</tr>
	</table>
</div>
</form>
<script> 
	//批量删除
	function multinomialDel(itemName,formObj){
		formObj = document.getElementsByName(formObj)[0];
		if(delHasChild(itemName)){
			asyncbox.alert('$text.get("common.exist.childcategory")!','$text.get("clueTo")');
			return false;
		}
		
		if(!isChecked(itemName)){
			//$.messager.alert('$text.get("clueTo")','$text.get("common.msg.mustSelectOne")!','info');
			asyncbox.alert('$text.get("common.msg.mustSelectOne")!','$text.get("clueTo")');
			return false;
		}
		asyncbox.confirm('您确定撤销邮件吗?收件人将无法收到您的邮件！','$text.get("clueTo")',function(action){
		　　　if(action == 'ok'){
		 		var groupId = "$!groupId";
				var emailType = "$!emailType";	
		 		var url="/EMailAction.do?operation=$globals.getOP('OP_DELETE')&type=undo&groupId="+groupId+"&emailType="+emailType+"&pId=$pId";//撤销
				form.action = url;
				form.submit();
			
		　　　}
		　　
　		});
	}
</script>
</html>

