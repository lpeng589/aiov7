
<link rel="stylesheet" href="/style/css/mgs.css"/>
<link rel="stylesheet" href="/js/tree/jquery.treeview.css" />
<script type="text/javascript" src="/js/tree/jquery.treeview.js" ></script>

<script>
function changeTab(tab){
	var tabList = $(".mag_list li") ;
	for(var i=0;i<tabList.length;i++){
		var cName = tabList[i].className ;
		if(tabList[i].id == tab){
			if(cName.indexOf("_a")<0){
				tabList[i].className = cName+"_a" ;
			}
			if("deptTab"==tab || "groupTab"==tab){
				jQuery.get("/UtilServlet?operation=showEmp&tabType="+tab+"&"+(new Date().getTime()), function(data){
					$("#navigation").html(data) ;
					$("#navigation").treeview({	
						animated: false,
						collapsed: true,
						unique: false
					});	
				});
				$("#deptLlist").show() ;
				$("#userList").hide() ;
			}else{
				jQuery.get("/UtilServlet?operation=showEmp&tabType="+tab+"&"+(new Date().getTime()), function(data){
					$("#userList").html(data) ;
				});
				$("#deptLlist").hide() ;
				$("#userList").show() ;
			}
		}else{
			if(cName.indexOf("_a")>0){
				tabList[i].className = cName.substring(0,cName.indexOf("_a"))
			}
		}
	}
}

function searchUser(keyWord){
	var tabList = $(".mag_list li") ;
	for(var i=0;i<tabList.length;i++){
		var cName = tabList[i].className ;
		if(cName.indexOf("_a")>0){
			tabList[i].className = cName.substring(0,cName.indexOf("_a"))
		}
	}
	if(keyWord.length>0){
		jQuery.get("/UtilServlet?operation=showEmp&keyWord="+encodeURI(keyWord)+"&"+(new Date().getTime()), function(data){
			$("#userList").html(data) ;
		});
	}else{
		jQuery.get("/UtilServlet?operation=showEmp&tabType=onlineTab&"+(new Date().getTime()), function(data){
			$("#userList").html("") ;
		});
	}
	$("#deptLlist").hide() ;
	$("#userList").show() ;
}

function organization(){
	jQuery.get("/UtilServlet?operation=showEmp&departmentCode="+encodeURI("$text.get("emList.Title")")+"&"+new Date().getTime(), function(data){		
		$("#popup_msg").html(data);
		$("#navigation").treeview({	
			animated: false,
			collapsed: false,
			unique: false
		});	
		/*$("#popup_msg").draggable({
			cursor:'move',
			handle:".mag_head",
			zIndex: 10000
		});*/
	 }); 
}

function showATalk(obj){
	$(obj).children("img[src*='aiochat.png']").show();
}

function hideATalk(obj){
	$(obj).children("img[src*='aiochat.png']").hide();
}
function openATalk(server,from_id,obj_id,obj_type){
	var url=window.location.href;
	url=url.split("http://")[1];
	url=url.substring(0,url.indexOf("/"));
	var server=url;
 	var linkUrl="AioP://AioChat/?server="+server+"&from_id="+from_id+"&obj_id="+obj_id+"&obj_type="+obj_type;
	window.location.href=linkUrl;
}
var openDg;
var lod;

function openDialog(sendType,sendId,sendName,obj){	
	$(obj).addClass("focus-li").removeClass("h-li").siblings("li").removeClass("focus-li");
	
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
					
			//n个div
			
			//判断是否存在ul

			
			
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
				//var tDov = '<div id="show_t" class="show-t"></div>';
				//$("#talk-list").append(tDov);
				$("#showKK").append(div);				
				$("#ul_left").append(li);
			}
			var kkDiv='<div class="kk_class" id="right_'+sendId+'"></div>';			
			$("#li_"+sendId).addClass("focus-li").siblings("li").removeClass("focus-li");
			$("#showKK").append(kkDiv);
			$("#right_"+sendId).html(msg);
			$("#right_"+sendId).show();
			$("#showKK").show();	
			//点击快速获取数据
			
			var message = $("#msgList_"+ksSendId) ;
			jQuery.get("/MessageAction.do?operation=69&receiveType="+ksSendType+"&receiveId="+ksSendId,function(data){
				if(data.length>0){
					message.append(data);
					var varMsg = document.getElementById("msgend_"+ksSendId) ;						
					varMsg.scrollIntoView() ;												
				}
			});
									
			load();
			//openDg = setTimeout(openDialog(sendType,sendId,sendName,this),1000);
			//setTimeout(openDialog(sendType,sendId,sendName,this),500);
			publicDragDiv($("#showKK"));
		}
	});	
}

function hideKKdLog(){
	$("#showKK").hide();
	$("#showKKDiv").show();
}

function delKKdLog(){
	clearTimeout(lod);
	if($("#ul_left>li").size() == 1){
		$("#showKK").html('');
		$("#showKK").hide();
		$("#showKKDiv").hide();		
		//clearTimeout(openDg);	
	}else{
		if(confirm("是否关闭所有窗口！")){				
			//clearTimeout(openDg);		
			$("#showKK").html('');
			$("#showKK").hide();
			$("#showKKDiv").hide();		
		}
	}				
}

function mdiwin(url,title){	
	top.showreModule(title,url);
}
</script>
<input type="hidden" id="winIndex" name="winIndex" value="$!winCurIndex"/>
<div id="msg_box">
	<div id="popup_msg" class="mag">
	</div> 
</div>

