<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/js/jquery.js"></script>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"/>
<title>评论</title>
<style type="text/css">
/**共用-全局 Start**/
body,div,img,input,em,p,span,a,ul,li,form{margin:0;padding:0;}
body{font-size:12px;font-family:微软雅黑;}
li{list-style:none;}
em{font-style:normal;}
input,textarea{outline:0;}
/**共用-全局 End**/
/**评论 Start**/
.tk_wp{}
.tk_wp .tk_iwp{border-bottom:1px dashed #aaaaaa;overflow:hidden;padding:5px 0 5px 0;width:450px;}
.tk_wp .tk_in{overflow:hidden;padding:0 0 0 0;}
.tk_wp .tk_in .in_img .in_img_a{display:inline-block;width:30px;height:30px;}
.tk_wp .tk_in .in_img .in_img_a img{width:30px;height:30px;}
.tk_wp .s_tk{overflow:hidden;padding:0 0 0 35px;margin:5px 0 0 0;}
.tk_in .in_img{float:left;display:inline-block;overflow:hidden;}
.tk_in .in_wd{float:left;display:inline-block;width:90%;margin:0 0 0 5px;line-height:16px;}
.in_wd_t .a_name{color:#04679E;}
.in_wd_t .txt_d{display:inline;}
.in_wd_b{line-height:18px;}
.in_wd_b .em_time{color:#9C9895;}
.in_wd_b .a_reply{color:#04679E;text-decoration:none;}

.tk_iwp .tk_bd{overflow:hidden;padding:0 0 0 35px;width:90%;}
.tk_iwp .tk_bd .tk_txt{width:100%;height:30px;display:block;box-sizing:border-box;overflow:auto;border:1px #a1a1a1 solid;}

.tk_bd{overflow:hidden;padding:5px 0;width:435px;}
.tk_bd .tk_txt{width:100%;height:30px;display:block;box-sizing:border-box;overflow:auto;border:1px #a1a1a1 solid;}
/**评论 End**/

</style>

<script type="text/javascript">
var commentBefore = '<div class="tk_bd" id="addComment"><textarea class="tk_txt" name="contents" id="contents">';
var commentAfter = '</textarea><a class="btn" style="font-size:12px;padding:0 6px;float:right;margin:5px 0 0 0;" type="button" id="cancel">取消</a><a class="btn btn-primary" style="font-size:12px;padding:0 6px;float:right;margin:5px 5px 0 0;" type="button" onclick="beforeSubmit(this)" id="commentBtn">回复</a></div>';


$(document).ready(function(){
	changeCommentHeight();//加载时先改变列表页面或详情页面的高度
	
	//取消
	$("#cancel").live("click", function() {
		//取消
		if(jQuery.trim(jQuery("#contents").val()) == "" || confirm("您确认要放弃正在编辑的评论吗？")){
			var commentStr = commentBefore + commentAfter;
			jQuery("#addComment").remove();
	    	jQuery("#commentIndex").after(commentStr);
			jQuery("#cancel").hide();
			//commentId与commentBy设为空
			jQuery("#commentId").val("");
			jQuery("#commentBy").val("");
			$("#commentBtn").text("评论");
			changeCommentHeight();//改变列表页面或详情页面的高度
			$("#contents").focus();
		}
	});
});

//提交表单
function beforeSubmit(obj){
	if(jQuery.trim($("#contents").val())==""){
		alert("内容不能为空!");
		$("#contents").focus();
		return false;
	}else{
		var clientName = parent.jQuery("#"+parent.jQuery("#commentFrameId").val()).attr("clientName");//获取客户名称
		if(/[@\/'\\"#$%&\^*]/.test($("#contents").val()))
		{
		    alert("评论内容有非法字符，请修改后提交。");
		    $("#contents").focus();
		    return false;
		} 
		jQuery.ajax({
			type: "POST",
			url: "/CRMBrotherAction.do",
			data: "tableName=$tableName&operation=1&type=comment&f_ref=$f_ref&billCreateBy=$!employeeId&contents="+$("#contents").val()+"&commentId="+$("#commentId").val()+"&commentBy="+$("#commentBy").val()+"&clientName="+encodeURIComponent(encodeURIComponent(clientName)),
			success: function(msg){
				if(msg == "error"){
					alert("添加失败");
				}else{
					if($("#commentId").val() == ""){
						//表示评论,附加到$("#commentIndex")后面
						$("#commentIndex").before(msg);
					}else{
						//表示回复
						$(obj).parent().before(msg);
					}
					$("#contents").val("");
					changeCommentHeight();//改变列表页面或详情页面的高度
				}
			}
		});
	}
}

/**添加回复框




*commentId：评论ID
*commentBy：记录回复人
*/
function addReply(commentId,commentBy,obj,employeeName){
	
	if(jQuery("#addComment").length == 1){
		if(jQuery.trim(jQuery("#contents").val()) == "" || confirm("您确认要放弃正在编辑的评论吗？")){
			var commentStr = commentBefore + commentAfter;
			jQuery("#addComment").remove();//删除添加框
			jQuery(obj).parents(".tk_iwp").append(commentStr);
			
			//赋值给隐藏域
			jQuery("#commentId").val(commentId);
			jQuery("#commentBy").val(commentBy);
			changeCommentHeight();
			$("#contents").focus();
		}
	}
}

//改变评论高度
function changeCommentHeight(){
	parent.jQuery("#"+parent.jQuery("#commentFrameId").val()).css("height",jQuery(".tk_wp").css("height"));
}

</script>
</head>

<body style="padding-left: 7px;padding-top: 2px;">
<form action="/CRMBrotherAction.do" method="post" name="form">
<input type="hidden" name="operation" id="operation" value='$globals.getOP("OP_ADD")'/>
<input type="hidden" name="type" id="type" value="comment"/>
<input type="hidden" name="f_ref" id="f_ref" value="$!f_ref"/>
<input type="hidden" name="commentId" id="commentId" value=""/>
<input type="hidden" name="commentBy" id="commentBy" value=""/>
<input type="hidden" name="tableName" id="tableName" value="$!tableName"/>
<input type="hidden" name="billCreateBy" id="billCreateBy" value="$!employeeId"/>

	<div class="tk_wp">
		<!-- 评论-循环 Start -->
		#foreach($comment in $commentList)
		<div class="tk_iwp">
			<!-- 评论-first Start-->
			<div class="tk_in">
				<!-- 左侧-头像 Start-->
				<div class="in_img">
					<a class="in_img_a">
					#set($photoSpl = "")
					#set($photoSpl = $!globals.get($comment,4).split(":"))
					#set($photo = "")
					#set($photo = $!globals.get($photoSpl,0))
					#if($photo =="")
						<img src="/style/images/bbs/us_photos.gif"/>
					#else
						<img src="/ReadFile.jpg?fileName=$photo&realName=$photo&tempFile=false&type=PIC&tableName=tblEmployee"/>
					#end
				  </a>
				</div>
				<!-- 左侧-头像 End-->
				<!-- 右侧-内容右侧 Start-->
				<div class="in_wd">
					<!--名字-->
					<div class="in_wd_t">   	
					  <a class="a_name">
						$!globals.getEmpFullNameByUserId($!globals.get($comment,2))
					  </a>
					  <em>:</em>
					  <div class="txt_d">$!globals.get($comment,1)</div>
					</div>
					<!--时间-->
					<div class="in_wd_b">
					  <em class="em_time">
						$!globals.get($comment,3)
					  </em>
					  <a href="#" class="a_reply" onclick="addReply('$!globals.get($comment,0)','$!globals.get($comment,2)',this,'$!globals.getEmpFullNameByUserId($!globals.get($comment,2))')">
						回复
					  </a>
					</div>
				</div>
				<!-- 右侧-内容右侧 End-->
			 </div>
			<!-- 评论-first End-->
			#if("$!replyMap.get($!globals.get($comment,0))" !="")
			#foreach($reply in $!replyMap.get($!globals.get($comment,0)))
			<!-- 评论-回复 Start-->
			<div class="tk_in s_tk">
				<div class="in_img">
					<a class="in_img_a">
					#set($photoSpl = "")
					#set($photoSpl = $!globals.get($reply,6).split(":"))
					#set($photo = "")
					#set($photo = $!globals.get($photoSpl,0))
					#if($photo =="")
						<img src="/style/images/bbs/us_photos.gif" width="15px" height="15px" />
					#else
						<img src="/ReadFile.jpg?fileName=$photo&realName=$photo&tempFile=false&type=PIC&tableName=tblEmployee" width="15px" height="15px" />
					#end
				  </a>
				</div>
				<div class="in_wd">
						<div class="in_wd_t">  
					  #if("$!globals.get($reply,2)" == "$!globals.get($reply,5)")
					  	 <a class="a_name">
						 	$!globals.getEmpFullNameByUserId($!globals.get($reply,2))
						 </a>
						 <em>回复</em>	 	
					  #else
					  <a class="a_name">
					 	$!globals.getEmpFullNameByUserId($!globals.get($reply,2))
					  </a>
					  <em>回复</em>
					  <a class="a_name">
					  	$!globals.getEmpFullNameByUserId($!globals.get($reply,5))
					  </a>
					  #end
					  <em>:</em>
					  <div class="txt_d">$!globals.get($reply,1)</div>
					</div>
					<div class="in_wd_b">
						<em class="em_time">
						$!globals.get($reply,3)
					  </em>
					  <a href="#" class="a_reply" onclick="addReply('$!globals.get($comment,0)','$!globals.get($reply,2)',this,'$!globals.getEmpFullNameByUserId($!globals.get($reply,2))')">
						回复
					  </a>
					</div>
				</div>
			</div>
			<!-- 评论-回复 End-->
			#end
			#end
		 </div>
		 #end
		<!-- 评论-循环 End -->
		<!-- 输入框 Start -->
		<!-- 记录评论位置开始 -->	
		<div id="commentIndex"></div>
		<!-- 记录评论位置结束 -->
		<div class="tk_bd" id="addComment">
			<textarea class="tk_txt" name="contents" id="contents"></textarea>
		  	<a class="btn btn-primary" style="padding:0 6px;font-size:12px;margin:5px 0 0 0;float:right;" type="button" onclick="beforeSubmit(this)">评论</a>
		</div>
		<!-- 输入框 End -->	
	</div>
</form>  
</body>
</html>
