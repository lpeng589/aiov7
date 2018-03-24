jQuery().ready(function() {
	$(".more-pr").mouseover(function(){
		$(".more-menu").show();
	})
	$(".more-pr").mouseout(function(){
		$(".more-menu").hide();
	})
	//安卓二维码

	var sWidth = $(document.body).outerWidth(true);
	var BW = (sWidth-300)/2;
	$("#picDiv").css("left",BW);
	
	$(document).bind("mouseup",function(){
		$(this).unbind("mousemove");
	});
	publicDragDiv($(".search_popup"));
	
	
	getAdvice();
	setTimeout('flushMsg()',1000);
	//通知菜单查看
	$(".notice-top").hover(
	    function() { 
	    	getAdvice();	    	
	    },
	    function() {}
	);
	
	//显示即时通讯消息
	$(".meddage-li").hover(
	    function() { $("#msgList").show();},
	    function() { $("#msgList").hide(); }
	);
	/*添加打分*/
	
});

function opennews(){
	window.parent.location.href  = "/AdviceAction.do?src=menu&addTHhead=true";
}
/**
*	获取最新的通知
*/
function getAdvice(){
	jQuery.getJSON("/UtilServlet?operation=adviceData"+"&time="+(new Date()).getTime(),function(data){
		if(data.length>0){		
			$(".notice-top>.notice-num").html(data.length).show();
		}else{
			$(".notice-top>.notice-num").hide();
		}
	});
}
/**
*	刷新系统消息
*/
function flushMsg(){
	jQuery.get("/UtilServlet?operation=msgData"+"&time="+(new Date()).getTime(),function(response){
		if(response.length>0 && response!="no response text"&&response!="close"){
			var arrayMsg = response.split("||") ;	
			if(arrayMsg[0] == 0){				
				$("#alertId").html("") ;
			}else{
				$("#alertId").html('<div class="newsmag">'+arrayMsg[0]+'</div>') ;
			}
			if(arrayMsg[1] == 0){				
				$('#adviceId').html('') ;
			}else{
				if(arrayMsg[1] > 99){
					$('#adviceId').html('<div class="newsmag" title='+arrayMsg[1]+'>99+</div>');
				}else{
					$('#adviceId').html('<div class="newsmag">'+arrayMsg[1]+'</div>');
				}
				
			}	
			if(arrayMsg[2] == 0){				
				$("#messageId").html("") ;
			}else{											
				if(arrayMsg[3].indexOf("show") != -1){
					var idpre = arrayMsg[3].split("'")[3];		
					//1.当已经打开当中存在					
					if($("#li_"+idpre).val() != undefined){
						if($("#showKKDiv").is(":visible")){
							$("#showKKDiv").addClass('showKKlog-back');						
						}
						if($("#ul_left>li").size()>1 && !$("#li_"+idpre).hasClass("focus-li")){
							$("#li_"+idpre).addClass('h-li');							
						}
					}else{	
						if(arrayMsg[2] > 99){
							$("#messageId").html('<div class="newsmag" title='+arrayMsg[2]+'>99+</div>') ;
						}else{
							$("#messageId").html('<div class="newsmag">'+arrayMsg[2]+'</div>') ;
						}			
						$("#userIdDiv").html(arrayMsg[3]) ;
					}			
				}			
			}
					
			if(arrayMsg[4] != ""){
				$("#soundDivId").html(arrayMsg[4]);
			}		
		}
		var alertTime = 0.5 ;		
		setTimeout('flushMsg()',alertTime*60*1000);
	}); 
}

/*wyy弹出聊天框*/
//全局
var ksSendId;
var ksSendType;
function openkk(){	
	jQuery.ajax({
		type:"post",
		url:"/MessageQueryAction.do?src=menu",
		success:function(msg){						
			$("#showKs").html(msg);			
			organization();			
			var firstLoad = $("#firstLoad").val();
			eval(firstLoad);
			//publicDragDiv($("#popup_msg"));	
		}			
	})　
	
}
function closeKK(){
	$("#popup_msg").hide();	
}
function showKKLog(){
	$("#showKK").show();
	$("#showKKDiv").removeClass("showKKlog-back").hide();
}

function openMsgDialog(sendType,sendId,sendName,obj){
	openkk();	
	$(obj).addClass("focus-li").siblings("li").removeClass("focus-li");	
	//判断是否大于5个



	if($("#ul_left>li").size() == 5 && $("#li_"+sendId).val() == undefined){
		asyncbox.tips("打开窗口不能多于5个","error");
		return;
	}		
	jQuery.ajax({
		type:"post",
		url:"/MessageAction.do?operation=70&sendType="+sendType+"&sendId="+sendId,
		success:function(msg){
			
			ksSendId = sendId;	
			ksSendType = sendType;		
			
			
			if($("#ul_left").val() != undefined){	
				$("#showKK>.kk_class").hide();//全部清除			
				//是否重复				
				if($("#li_"+sendId).val() == undefined){
					var li='<li id="li_'+sendId+'" onclick=\'openDialog("'+sendType+'","'+sendId+'","'+sendName+'",this)\'><em class="in-em" title='+sendName+'>'+sendName+'</em><b title="关闭" onClick="closePopup(\''+sendId+'\');" class="close-talk"></b></li>';
					$("#ul_left").append(li);
				}else{
					$("#right_"+sendId).show();
					$("#li_"+sendId).addClass("focus-li").siblings("li").removeClass("focus-li");
					return;
				}			
			}else{			
				var div ='<div class="talk-list" id="talk-list"><ul id="ul_left"></ul><b class="hide-talk" onclick="hideKKdLog();">-</b><b title="关闭" class="close-talk" onclick="delKKdLog();"></b></div>';	
				var li='<li id="li_'+sendId+'" onclick=\'openDialog("'+sendType+'","'+sendId+'","'+sendName+'",this)\'><em class="in-em" title='+sendName+'>'+sendName+'</em><b title="关闭" onClick="closePopup(\''+sendId+'\');" class="close-talk"></b></li>';
				$("#showKK").append(div);				
				$("#ul_left").append(li);
			}
			var kkDiv='<div class="kk_class" id="right_'+sendId+'"></div>';
			
			
			$("#li_"+sendId).addClass("focus-li").siblings("li").removeClass("focus-li");
			$("#showKK").append(kkDiv);
			$("#right_"+sendId).html(msg);
			$("#right_"+sendId).show();
			$("#showKK").show();			
			load();
			
			$("#show"+sendId).remove();
			//消息- 1
			if($("#messageId>.newsmag").text() > 1){
				var num = parseFloat($("#messageId>.newsmag").text()) - 1;
				$("#messageId>.newsmag").text(num);
			}else{
				$("#messageId").html('');
			}
			publicDragDiv($("#showKK"));
		}
	});	
}
/*公用拖动方法
传入一个DIV对象
*/
function publicDragDiv(obj){
	$(obj).bind("mousedown",function(event){
		var offset_x = $(this)[0].offsetLeft;//x坐标
		var offset_y = $(this)[0].offsetTop;//y坐标
		/* 获取当前鼠标的坐标 */
		var mouse_x = event.pageX;
		var mouse_y = event.pageY;				
	
		/* 绑定拖动事件 */
		/* 由于拖动时，可能鼠标会移出元素，所以应该使用全局（document）元素 */
		$(document).bind("mousemove",function(ev){
			/* 计算鼠标移动了的位置 */
			var _x = ev.pageX - mouse_x;
			var _y = ev.pageY - mouse_y;
			
			/* 设置移动后的元素坐标 */
			var now_x = (offset_x + _x ) + "px";
			var now_y = (offset_y + _y ) + "px";					
			/* 改变目标元素的位置 */
			$(obj).css({
				top:now_y,
				left:now_x
			});
		});
	})
}


function feedBack(){
	asyncbox.open({
	 	title : '反馈建议',
	 	id : 'D345',
	　　	url : 'feedback.jsp',
		height: 285,
		width: 420,
		callback : function(action,opener){
				
	　　　	}
　　　	});
	$("#D345").find("iframe").css("height","255px");

}

function closedialog(code){
	if(code >= 200 && code <= 400){
		parent.jQuery.close('D345');
		asyncbox.success('感谢您的反馈建议，我们将尽快解决您的问题！   <a class="help-a" href="http://help.koronsoft.com" target="_blank">去帮助中心看看</a>','提示',function(action){
			if(action == 'close'){
			}
		});
	}else if(code>400){
		asyncbox.tips('反馈建议失败','error',1500);
	}
}

function showDiv(){
	$("#picDiv").show();
}

function closeSearch(){
	$('#picDiv').hide();
}
/**
*	退出系统
*/
function sureLoginOut(){
	if(confirm('退出系统','')) {
		window.location='/loginAction.do?operation=$globals.getOP("OP_LOGOUT")';
	}
}



