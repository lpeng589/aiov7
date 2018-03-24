
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<title>Insert title here</title>
<style type="text/css" >
.listRange_1_photoAndDoc_1{margin-left:0px;width:98%;}
</style>

#if("$!nodate"=="nodate")
<script type="text/javascript" >
window.onload=function(){
	parent.asyncbox.confirm("该数据可能被删除","警告",function(action){
		parent.jQuery.close('warmsetting');
	});
}
</script>
#end

<script type="text/javascript" >

//回退按钮
function black(rowid){
	asyncbox.open({
		id:'handlebox',
		btnsbar : jQuery.btn.OKCANCEL,
		　　　html : "<textarea id='resulttext' cols='57'  rows='10' ><\/textarea>",
		   width : 450, height : 250,title:'行动描述', 
		   callback : function(action,opener){
			  
			   if(action=="ok"){
				  var str=$("#resulttext").val();
				  if(str.trim() == ""){
					  alert("原因不能为空");
					  return false;
				  }
				  str= encodeURIComponent(str);
				 var  url="/CRMDistWorkAction.do?operation=5&requestType=back&workid="+rowid+"&result="+str+"&userId="+$!globals.getLoginBean().getId();
				  jQuery.ajax({
							type: "GET",
						url:url,
							success: function(msg){
								
							 if(msg == "yes"){
								 asyncbox.tips("操作成功",'success');
									jQuery.close('handlebox');
                                 
									}else{
										asyncbox.tips("操作失败",'error');
									}
		 				}
				});
			   }
		   }
		　});

}



//处理按钮
function handle(rowid){
	asyncbox.open({
		id:'handlebox',
		btnsbar : jQuery.btn.OKCANCEL,
		　　　html : "<textarea id='resulttext' cols='57'  rows='10' ><\/textarea>",
		   width : 450, height : 250,title:'行动描述', 
		   callback : function(action,opener){
			  
			   if(action=="ok"){
				  var str=$("#resulttext").val();
				  if(str.trim() == ""){
					  alert("原因不能为空");
					  return false;
				  }
				  str= encodeURIComponent(str);
				 var  url="/CRMDistWorkAction.do?operation=4&requestType=handle&workid="+rowid+"&result="+str;
				  jQuery.ajax({
							type: "GET",
						url:url,
							success: function(msg){
								
							 if(msg == "yes"){
								 asyncbox.tips("操作成功",'success');

                                   
									}else{
										asyncbox.tips("操作失败",'error');
									}
		 				}
				});
			   }
		   }
		　});

}

</script>
</head>
<body>
#if($!distwork.statusId==0  )	 

 #set($userIds=",$distwork.userId,")
		 #set($temp=",$!globals.getLoginBean().getId(),")  
		 #if( $userIds.indexOf($temp)!=-1) 
		 #set($fin=";$!globals.getLoginBean().getId();")  
		    #if($distwork.finishUser.indexOf($fin)==-1) 
		                 
		    
		   <div class="listRange_1_photoAndDoc_1">
 				<input type="button"  value="完成" onclick="handle('$!distwork.id')"/>						
			</div>
		    
		   #end
		  #end 

#end
<div class="listRange_1_photoAndDoc_1">
<div style="float:left;">
<span>关联客户&nbsp;&nbsp;：$!clientName</span>
</div>
<div style="float:left;">
<span>分类：$globals.getEnumerationItemsDisplay("CRMTaskType","$!distwork.taskType")</span>

</div>
</div>
<div class="listRange_1_photoAndDoc_1">

<span>分派者：$!globals.getEmpFullNameByUserId($!distwork.createBy)</span>
						
</div>
<div class="listRange_1_photoAndDoc_1">

<span>执行者：#if("$!distwork.userId"!="")#foreach($str in $!distwork.userId.split(","))$!globals.getEmpFullNameByUserId($str)  #set($temp=";$str;")  #if($!distwork.finishUser.indexOf($temp)!=-1) 
完成  
#if("$!globals.getLoginBean().getId()"=="$!distwork.createBy")
<a href="javascript:void(0);" onclick="black('$!distwork.id')"> 回退</a> 
#end
#end ;
#end#end</span>
						
</div>
<div class="listRange_1_photoAndDoc_1" >
<span>完成期限&nbsp;&nbsp;：$!distwork.finishTime</span>
</div>

<div class="listRange_1_photoAndDoc_1" >
<span>现在状态&nbsp;&nbsp;：

		#if($!distwork.statusId==0)	   
		   	    未结束			
		#elseif($!distwork.statusId==1)
		    已结束
		#elseif($!distwork.statusId==2)
		  取消
		#end
</span>
</div>


<div class="listRange_1_photoAndDoc_1">
<span>内容：</span>
<br />
<div style=" height:150px;overFlow-y:scroll;">
$!distwork.content
</div>
</div>
<div class="listRange_1_photoAndDoc_1" style=" height:30px;">
#if($!distwork.crmAffix.indexOf(";") > 0)
							#foreach ($str in $globals.strSplit($!distwork.crmAffix,';'))
								<img src="/$globals.getStylePath()/images/down.gif"/>
								<a	href='/ReadFile?tempFile=path&path=/crm/QA/&fileName=$!globals.encode($!str)'target="_blank"> $str</a> 
						    #end
						#else
							暂无
						#end
								</div>

<div class="listRange_1_photoAndDoc_1">
<span>行动描述：</span>
</div>
<div  class="listRange_1_photoAndDoc_1" >
<ul>
$!distwork.action
</ul>
</div>


</body>
</html>