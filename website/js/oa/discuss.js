$(function(){
	if($("#parentIframeName").val()!=""){
		changeParentIframeHeight()
	}
	
	$('.short-txt-span').live('click', function(e) {
		$(this).hide().siblings("div").show().find(".Atxt").focus();
		changeParentIframeHeight();
		$("#replyAddDiv").remove();
		
	});
	
	$('.discussDel').live('click', function() {
		if(!confirm('确定删除?')){
			return false;
		}else{
			var tableName = $("#tableName").val();//表名
			var delType = $(this).attr("delType");//comment表示评论删除，reply表示回复删除
			var keyId = $(this).attr("keyId");//主键ID
			var currObj = $(this);//当前对象
			$.ajax({
				type: "POST",
				url: "/DiscussAction.do",
				data: "operation=3&keyId="+keyId+"&tableName="+tableName,
				success: function(msg){
					if("success" == msg){
						asyncbox.tips('删除成功','success');
						if("comment"==delType){
							$(currObj).parents(".t-block").remove();
						}else{
							$(currObj).parents(".in-block").remove();
						}
						changeParentIframeHeight();
					}else{
						asyncbox.tips('删除失败!','error');
					}
				}
			});		
		}
	});
})

/*评论提交*/
function commentSubmit(){
	$("#commentSubmitBtn").attr("disabled","disabled");//锁定不让选择按钮
	var content = $.trim($("#commentContents").val());
	if(content== "" || content == "请输入内容"){
		$("#commentContents").focus();
		asyncbox.tips('内容不能为空');
		$("#commentSubmitBtn").removeAttr("disabled");//锁定不让选择按钮
		return false;
	}
	if(!containSC($.trim(content))){
		asyncbox.tips("不能含有特殊字符(' \" | ; \\)");
		$("#commentSubmitBtn").removeAttr("disabled");//锁定不让选择按钮
		return false;
	}
	var tableName = $("#tableName").val();
	var f_ref = $("#f_ref").val();
	$.ajax({
		type: "POST",
		url: "/DiscussAction.do",
		data: "operation=1&f_ref="+f_ref+"&tableName="+tableName+"&content="+encodeURIComponent(content),
		success: function(msg){
			$("#commentSubmitBtn").removeAttr("disabled");//锁定不让选择按钮
			if(msg == "error"){
				asyncbox.tips('操作失败!','error');
			}else{
				asyncbox.tips('操作成功','success');
				$("#shareTrend").after(msg);
				
				if($("#parentIframeName").val()!=""){
					//改变父类iframe高度
					changeParentIframeHeight()
				}
			}
			$("#commentContents").val("")
		}
	});
}


/*添加回复
obj:当前对象
relationId:回复某个进展Id
replyId:回复某个职员Id
*/
function addReply(obj,commentId,replyId){
	hideObj($(commentContents));
	$("#replyAddDiv").remove();
	$("#commentId").val(commentId);
	$("#replyId").val(replyId);
	$(obj).parents("div[class='t-dv']").append(replyDiv)
	$("#replyContents").focus();
	if($("#parentIframeName").val()!=""){
		changeParentIframeHeight()
	}
		
}

/*回复提交*/
function replySubmit(){
	$(".reply-submit").attr("disabled","disabled");//锁定不让选择按钮
	
	content = $.trim($("#replyContents").val());
	if(content== "" || content == "请输入内容"){
		$("#replyContents").focus();
		asyncbox.tips('内容不能为空');
		return false;
	}
	
	var tableName = $("#tableName").val();
	var commentId = $("#commentId").val();
	var replyId = $("#replyId").val();
	var f_ref = $("#f_ref").val();
	$.ajax({
		type: "POST",
		url: "/DiscussAction.do",
		data: "operation=1&f_ref="+f_ref+"&tableName="+tableName+"&commentId="+commentId+"&replyId="+replyId+"&content="+encodeURIComponent(content),
		success: function(msg){
			$(".reply-submit").removeAttr("disabled");//锁定不让选择按钮
			if(msg == "error"){
				asyncbox.tips('操作失败!','error');
			}else{
				asyncbox.tips('操作成功','success');
				$("#replyAddDiv").parent().find(".in-reply-wrap").append(msg);
				$("#replyAddDiv").remove();
				if($("#parentIframeName").val()!=""){
					//改变父类iframe高度
					changeParentIframeHeight()
				}
			}
		}
	});
}

function changeParentIframeHeight(){
	var parentIframeName = $("#parentIframeName").val();
	if(parentIframeName!=""){
		parent.$("#"+parentIframeName).css("height",$(".trendWp").height()+30)
	}
}

/*
	回车提交
	submitType 确定类型， comment:评论，reply:回复
*/
function keyDownSubmit(submitType){
	if(submitType == "comment"){
		commentSubmit($("#commentSubmitBtn"));
	}else{
		replySubmit()
	}
}

function hideObj(obj){
	$("#commentContents").val("");
	$(obj).parent("div").hide().siblings("span").show();
}

