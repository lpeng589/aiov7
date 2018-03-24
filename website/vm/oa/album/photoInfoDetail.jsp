<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>$text.get("oa.advice.readAdvice")</title>
		<link rel="stylesheet"
			href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script language="javascript" src="/js/function.js"></script>
		
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
		<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
		<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
		<!--修订版 添加 幻灯效果 此代码需要放在 编辑的引入js上面 否则冲突-->
		<link rel="stylesheet" type="text/css" href="/vm/oa/album/ui/images/jquery.ad-gallery.css">
		<link rel="stylesheet" type="text/css" href="/vm/oa/album/ui/images/css.css">
		<script type="text/javascript" src="/vm/oa/album/ui/images/jquery.min.js"></script>
		<script type="text/javascript" src="/vm/oa/album/ui/images/jquery.ad-gallery.js"></script>
		<link rel="stylesheet" href="/vm/oa/album/ui/css/common.css"
			type="text/css" />
		<link rel="stylesheet" href="/vm/oa/album/ui/css/public.css"
			type="text/css" />
		<link type="text/css" href="/js/ui/jquery-ui-1.8.18.custom.css"
			rel="stylesheet" />
		<script type="text/javascript"
			src="/js/ui/jquery-ui-1.8.18.custom.min.js"></script>
		<link rel="stylesheet" href="/style/css/mgs.css" />
		<link rel="stylesheet" href="/js/tree/jquery.treeview.css" />
		
		<SCRIPT LANGUAGE=javascript>
			
			function stop(){ 
				return false; 
			} 
			//document.oncontextmenu=stop; 
		</SCRIPT> 
		
		
		<script type="text/javascript">
		$(window).bind('load', function() {
	   		var preload = new Array();
		    $(".hover").each(function() {
		        //s = $(this).attr("src").replace(/\.(.+)$/i, "_on.$1");
		        s = $(this).attr("href").replace(/\.(.+)$/i, "_on.$1");
		        preload.push(s)
		    });
		    var img = document.createElement('img');
		    $(img).bind('load', function() {
		        if(preload[0]) {
		            this.src = preload.shift();
		        }
		    }).trigger('load');
		    var a = document.createElement('a');
		    $(a).bind('load', function() {
		        if(preload[0]) {
		            this.href = preload.shift();
		        }
		    }).trigger('load');
		    
		});
		
		//幻灯片加载





		$(function() {
			//加载默认显示当前 照片
			/* 该方法 导致页面会出现缓慢效果 以及 多执行了多次请求
			var pid = "#$!photo.id";
			window.setTimeout(function(){
				$(pid).click();
			},300);
			*/
			var galleries = $('.ad-gallery').adGallery();
			$('#switch-effect').change(
				function() {
				galleries[0].settings.effect = $(this).val();
				return false;
				}
			);
			$('#toggle-slideshow').click(
				function() {
				galleries[0].slideshow.toggle();
				return false;
				}
			);
			$('#toggle-description').click(
				function() {bmp
				if(!galleries[0].settings.description_wrapper) {
				galleries[0].settings.description_wrapper = $('#descriptions');
				} else {
				galleries[0].settings.description_wrapper = false;
				}
				return false;
				}
			);
		});
		
		
		</script>
		<script type="text/javascript">
		 $(function(){
				// Dialog
				$('#dialog_photoDetail').dialog({
					autoOpen: false,
					height: 365,
					width: 355,
					buttons: {
						"$text.get('common.lb.ok')": function() { 
							 //修改
							 form2.operation.value = 2;
							 var tempName = $("#tempName_inp").val();
							 var photoDesc = $("#photoDesc_inp").val();
							 var isCover = $(":radio:checked").val();
							 if(tempName==null || tempName.length<=0)
							{
								alert("$text.get('name')$text.get('isNotNull')"); 
								return false;
							}
								 
							if(getStringLength(tempName)>50){
								alert("$text.get('name')"+"$text.get('oa.common.not.more.than')"+50+"$text.get('oa.common.word.countOfCharacter')");
								return false ;
							}
						
							if(getStringLength(photoDesc)>200){
								alert("$text.get('desc')"+"$text.get('oa.common.not.more.than')"+200+"$text.get('oa.common.word.countOfCharacter')");
								return false ;
							}
							
							 var bool1 = isHaveSpecilizeChar(tempName);
							 if (bool1==false) {
							 	alert("$text.get('name')$text.get('contain.specilize')");
							 	return false;
							 }
							 var bool2 = isHaveSpecilizeChar(photoDesc);
							 if (bool2==false) {
							 	alert("$text.get('desc')$text.get('contain.specilize')");
							 	return false;
							 }
							 //tempName = encodiURILocal(tempName);
							 tempName = encodeURIComponent(tempName);
							 //photoDesc = encodiURILocal(photoDesc);
							 photoDesc = encodeURIComponent(photoDesc);
							 form2.type_upd.value = "";
							 form2.action = "/PhotoAction.do?tempName="+tempName+"&photoDesc="+photoDesc+"&isCover="+isCover+"&replyType=photoUpdate&orderType=$!orderType";
							 form2.submit();
							 $(this).dialog("close"); 
						}, 
						"$text.get('cancel')": function() { 
							$(this).dialog("close"); 
							$("#photoDesc_inp").attr("value","");
							$("#tempName_inp").attr("value","");
							
						}
					}
				});
				// Dialog Link
				$('#dialog_link').click(function(){
					$('#dialog_photoDetail').dialog('open');
					return false;
				});
			});
			
			//特殊字符转码 encodeURIComponent 这个方法可以自动解析
			function encodiURILocal(data) {
				//var d = encodeURI(data).replace(/&/g,'%26').replace(/\+/g,'%2B').replace(/\$/g,'%20').replace(/&/g,'%26');
				var d = data.replace(/\"/g,'').replace(/\'/g, '').replace(/\\/g, '');
				return d;
			}
			//判断是否存在特殊字符
			function isHaveSpecilizeChar(data){
				var i = data.indexOf("\"");
				var j = data.indexOf("\'");
				var m = data.indexOf("\\");
				
				if (i >= 0 || j >= 0 || m >= 0) {
					return false;
				}
				return true;
			}
			function update(aId,name,desc) {
					var len_name = getStringLength(name);
					var len_desc = getStringLength(desc);
					$("#fontnum_inp_detail").html(len_name);
					$("#fontnum_detail").html(len_desc);
					$('#dialog_photoDetail').dialog('open');
					return false;
			}
			function toPreUpload(){
				 form.operation.value = 91;
				 form.submit();
			}
		</script>

		<!-- <script type="text/javascript" src="/vm/album/ui/js/jquery-1.2.6.pack.js"></script> 有冲突-->
		<script type="text/javascript" src="/vm/oa/album/ui/js/content_zoom.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$('div.small_pic img').fancyZoom({scaleImg: true, closeOnClick: true});
				$('#zoom_word_1').fancyZoom({width:400, height:200});
				$('#zoom_word_2').fancyZoom();
				$('#zoom_flash').fancyZoom();
			});
		</script>

		<style type="text/css">
			.demoHeaders { margin-top: 2em; }
			#dialog_link {padding: .4em 1em .4em 20px;text-decoration: none;position: relative;}
			#dialog_link span.ui-icon {margin: 0 5px 0 0;position: absolute;left: .2em;top: 50%;margin-top: -8px;}
		</style>


		<script type="text/javascript">
		
		/*评论暂时注释
		var editor  ;
		KindEditor.ready(function(K) {
			editor = K.create('textarea[name="content"]',{
				uploadJson:'/UtilServlet?operation=uploadFile'
			});
		});
		*/		
		//显示隐藏的删除修改


		function showOperate(obj) {
			var v = $(obj).children(".operateOfAlbum").show();
        }
		function hideOperate(obj) {
		 	var v = $(obj).children(".operateOfAlbum").hide();
		}
		//返回
		function forBack(formType){
			/*
			if (formType ==1 ) {
					alert(1);
				form.pageNo.value = 1;
			} else {
				alert(2);
				form2.pageNo.value = 1;
			}
			*/
			var url = "/PhotoAction.do?operation=$globals.getOP('OP_QUERY')&albumSelectId=$!albumId&orderType=$!orderType";
			window.location= url;
			
		}
			//返回企业
		function forBackQy(){
				var url = "/AlbumQueryAction.do?operation=$globals.getOP('OP_QUERY')";
				window.location= url;
		}
		
		//删除
		function delPhoto() {
			if (confirm("$text.get('confirm.del.pho')")) {
				form2.operation.value = 3;
				//新的照片随便
				var begName = $("#beginName").val();
				form2.delPhoedShowId.value = $(document.getElementById(begName)).next().attr("title");
			    form2.submit();
			}
		}
		//删除点评
		function toDelReply(id) {
			if (confirm("$text.get('confirm.del.reply')")) {
				form2.operation.value = '$globals.getOP("OP_DELETE")';
				form2.action = "/PhotoAction.do?type=delPhotoReply&replyId="+id;
				form2.submit();
			}
		}
		//移动文件夹




		function moveToAlbum(obj) {
			if(confirm("$text.get('whether.confirm.move.photo')")) {
				var v = obj.value;
				form2.operation.value=2;
				form2.submit();
				return true;
			}
			return false;
		}
		//open 照片
		function zoomImage(obj){
			//height = screen.height;
			//width  = screen.width;
			//window.open(obj.lang,"_blank",'height='+(height-65)+', width='+width+'toolbar =no,menubar=no,scrollbars=no,resizable=no, location=no, status=no,Top=0,Left=0');
			var begName = $("#beginName").val();
			begName = encodeURI(begName);
			var src = 	"/ReadFile?tempFile=path&path=/album/img/&tempFile=false&type=PIC&fileName="+begName;
			var oWd = window.open(src);
			owd.document.oncontextmenu=function(){return false;};
			if (oWd) {
				oWd.document.oncontextmenu=function(){return false;};
			}
			
		}
		//引用点评
		function fieldSetReplay(name,time,data){

			var str= '<fieldset class="bbsfieldset"><legend>$text.get("common.lb.Reference")：'+name+' '+time+'</legend>  '+document.getElementById(data).innerHTML+'</fieldset>'; 
			editor.html(str) ;
		}
		function toChangeInfo(obj,id){
			//var id = $(obj).parent().parent().attr("id");//选中小图的id
			//将整个页面的内容替换成 该id下的信息
			jQuery.getJSON("/PhotoAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&pId="+id,{pageNo:'$!pageNo'}, function(data){ 
				/*
				jQuery("#showReplyList_advice").empty();
				jQuery.each(data.replys, function(i, reply) {
		  	 		  var names = reply.fullName;
				  	  var rId = "REPLAY_"+i;
		              var trHTML = "<tr><td><div class='news_replays'><ul><li>$text.get('replyPhoto')$text.get('to')："+reply.createTime+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class='s3'>"+names+"</span>";
		              var trHTML2 = "#if($del)<span style='margin-left: 200px;'><a href='#' onclick=toDelReply("+"'"+reply.id+"'"+")>[$text.get('common.lb.del')]</a></span> #end </li><li style='float:left;padding-left:20px'><a href='#HTML_replaybox' onclick='fieldSetReplay("+"\""+reply.fullName+"\",\""+reply.createTime+"\",\""+rId+"\");'>$text.get('oa.bbs.lb.refreplay')</a></li><li style='float:right;padding-right:20px'>#set($cc=$velocityCount+1)</li></ul><div class='newsReply' id='"+rId+"'>"+reply.content+"</div></div></td></tr>";
					  var html = trHTML+trHTML2;
			          jQuery("#showReplyList_advice").append(html);	
			   });*/
				//更新描述和名称




				jQuery("#pInfo").empty(); 
				var p = data.photo;
				var desc = "<div ><pre>"+p.phoneDesc+"</pre></div>";
				if(p.phoneDesc=="null" || p.phoneDesc == "" || p.phoneDesc == undefined){
					//  desc = "$text.get('without')";
					  desc = "<div><pre>$text.get('without')</pre></div>";
				}
				var pInfoHtml = "<div class=\"newsReply_text\"><div style=\"float:left\">$text.get("detailName")&nbsp;:</div><div><pre>"+p.tempName+"</pre></div><div style='margin-top: -15px;' class='newsReply_text'><div style='float:left;margin-left:-1px;'>$text.get("detailDesc")&nbsp;:</div>"+desc+"</div>";
				jQuery("#pInfo").append(pInfoHtml);
				//上传时间
				jQuery("#uploadTime").empty(); 
				var uploadTimeHtml = '$text.get("uploadTime")：'+p.createTime;
				//alert(uploadTimeHtml);
				jQuery("#uploadTime").append(uploadTimeHtml);
				//跟新 删除和修改





				jQuery("#upOrDel").empty();
				var upOrDelHtml = "#if($del)<a href='#' style='cursor:hand;' onclick='delPhoto()'>$text.get('common.lb.del')</a>#end  #if($upd) <a href='#' id='dialog_link' style='cursor:hand;' onclick='update("+"\""+p.id+"\",\""+p.tempName+"\",\""+p.phoneDesc+"\");'>$text.get('update')</a>#end";
				//alert(upOrDelHtml);
				jQuery("#upOrDel").append(upOrDelHtml);
				jQuery("#pname").empty();
				var h = p.tempName;
				jQuery("#pname").append(h);
				
				//修改 hidden值




				$("#pId").attr("value",p.id);
				$("#tempName_inp").attr("value",p.tempName);
				$("#photoDesc_inp").attr("value",p.phoneDesc);
				
			
				if(p.isCover == '1') {
					$("#isCover_inp_yes").attr("checked","checked");
				} else {
				    $("#isCover_inp_no").attr("checked",true);
				}
				//回复
				$("#userId").attr("value",p.id);
				$("#newsId").attr("value",p.id);
				//修改显示原图的hidden值






				$("#beginName").attr("value",p.beginName);
			})
		}
	</script>
	</head>
	<body>
		<form action="/PhotoAction.do" name="form2" method="post">
			<input type="hidden" name="operation" value="$globals.getOP("OP_DETAIL")"/>
			<input type="hidden" name="attachFiles" value="">
			<input type="hidden" name="delFiles" value="">
			<input type="hidden" name="pId" value="$!photo.id" id = "pId">
			<input type="hidden" name="picFiles" value="">
			<input type="hidden" name="delPicFiles" value="">
			<input type="hidden" name="albumSelectId" value="$!albumId">
			<input type="hidden" name="albumId" value="$!albumId">
			<input type="hidden" name="begIsCover" value="$!photo.isCover">
			<input type="hidden" name="albumName" value="$!albumName" />
			<input type="hidden" name="type_upd" value="moveToAlbum" />
			<input type="hidden" name="orderType" value="$!orderType" />
			<input type="hidden" id="beginName" value="$!photo.beginName"/>
			<input type="hidden" name = "delPhoedShowId" id = "delPhoedShowId" value =""/>
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					<!-- <a style="cursor:hand;color:green" onclick="forBackQy()"></a> -->$text.get("personal.album")
					
				</div>
				<ul class="HeadingButton">
					<li>
						<button type="button" onClick="forBack('1')" class="b2">
							$text.get("common.lb.back")
						</button>
					</li>
				</ul>
			</div>
			<!-- heading end-->
			<div id="listRange_id" style="text-align:center;overflow: auto">
				<div style="width: 900px;">
					<script type="text/javascript">
					var oDiv=document.getElementById("listRange_id");
					var sHeight=document.body.clientHeight-38;
					oDiv.style.height=sHeight+"px";
				</script>
					<table style="width: 900px;margin-left: auto;margin-right: auto;"
						border="0" cellpadding="0" cellspacing="0"
						class="oalistRange_list_function" name="table">
						<thead>
							<tr>
								<td class="oabbs_tr" style="text-align: left">
									<a name="HTML_top"></a>$text.get("oa.common.currentStation")：<!-- $!photo.tempName $text.get("detailInfo") -->
									<a style="cursor:hand;" href="#" onclick="forBackQy()">$text.get("personal.album")</a> >>
									<a style="cursor:hand;" href="#" onclick="forBack()"">$!albumName</a>
									>> <span id="pname">$!photo.tempName</span>
								</td>
							</tr>
						</thead>
					</table>
					<div class="scroll_function_small_bbs"
						style="width: 900px;margin-left: auto;margin-right: auto;">
						<table cellpadding="0" cellspacing="0"
							class="oabbs_function_index" width="100%">
							<tbody>
								<tr>
									<td>
										<div id="container" style="float: left;margin-top:-24px;">
											<div style="margin:0 auto 5px auto;width:628px;clear:both;"></div>
											<div id="gallery" class="ad-gallery">
												
												<div class="ad-image-wrapper"></div>
												<div class="ad-controls"></div>
												<div class="ad-nav">
													
													<div class="ad-thumbs">
														<ul class="ad-thumb-list">
															#foreach ($p in $photos)
																<li title="$!p.id" class="pho_li" id="$!p.beginName">
																	<a href="/ReadFile?tempFile=path&path=/album/img/&fileName=$globals.encode($!p.beginName)">
																	
																	<!-- title =  $text.get('photo.show.effect') -->
																	<!-- alt="$!p.tempName" 会在图片的下方展示 对应的信息-->
																	 <img src="/ReadFile?tempFile=path&path=/album/img/small/&fileName=$globals.encode($!p.beginName)"  onclick="javascript:toChangeInfo(this,'$!p.id');" id="$!p.id" title="" style = "width: 80px;height: 60px;" class="image0"  ></a>
																</li>
															#end
														</ul>
													</div>
												</div>  
											</div>
										</div>
										<div style="margin-top: 12%;margin-left: 0%" onclick="zoomImage(this)" title="$text.get('click.show.photo')" lang="/ReadFile?tempFile=path&tempFile=false&type=PIC&path=/album/img/&fileName=$globals.encode($!p.beginName)" >
														<button  class="b3" >$text.get('show.barbarism.photo') </button>
										</div>
										<div style="margin-top: 3%;">
											<div id="pInfo">
												<div  class="newsReply_text">
													<div style="float:left">$text.get("detailName")&nbsp;:</div><div><pre>$!photo.tempName</pre></div>
												</div>
												<div class="newsReply_text" style="margin-top: -20px;">
													<div style="float:left">$text.get("detailDesc")&nbsp;:</div>#if("$!photo.phoneDesc"==
													'null' || "$!photo.phoneDesc"=="" ||"$!photo.phoneDesc" == 'undefined')<div><pre>$text.get("without")</pre></div>#else
													<div><pre>$!photo.phoneDesc</pre></div>
													#end
												</div>
											</div>
											#if(1==2)
											<div style="margin-top: 10px;" id="showAlbumSelect">
												$text.get("displace"):
												<select name="albumSelect" id="albumSelectId"
													style="width: 130px;" onchange="moveToAlbum(this)">
													#foreach($album in $albumList)
													<option title="$album.name" value="$album.id"
														#if("$!albumId" == "$album.id") selected #end>
														$album.name
													</option>
													#end
												</select>
											</div>
											#end
											<div style="margin-top: -15px;" id="uploadTime">
												$text.get("uploadTime")：$!photo.createTime
											</div>
											<div style="margin-top: 10px;" id="upOrDel">
											
												<a href="#" style="cursor:hand;"
													onclick="delPhoto()">$text.get("common.lb.del")</a>
											
												<a href="#" id="dialog_link"
													onclick="update('$!photo.id','$!photo.tempName','$!photo.phoneDesc')" style="cursor:hand;">$text.get("oa.common.upd")</a>
											
											</div>
										
										</div>
									</td>
								</tr>
						 </tbody>
						</table>
					</div>
					#if(1==2)
					<div class="scroll_function_small_bbs"
						style="width: 900px;margin-left: auto;margin-right: auto;">
						<table cellpadding="0" cellspacing="0"
							class="oabbs_function_index" name="table" width="100%">
							<tbody id="showReplyList_advice">
								#foreach ($reply in $replayList)
								<tr>
									<td>
										<div class="news_replays">
											<ul>
												<li>
													$text.get("replyPhoto")$text.get("to")：$!reply.createTime
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<!-- <span class="s3">$!reply.bbsUser.nickName#if("$!reply.bbsUser.nickName"!="$!reply.bbsUser.fullName")/$!reply.bbsUser.fullName#end</span> -->
													<span class="s3">$!reply.fullName</span> #if($del)
													<span style="margin-left: 200px;"><a href="#"
														onclick="toDelReply('$!reply.id')">[$text.get("common.lb.del")]</a>
													</span> #end
												</li>
												<li style="float:left;padding-left:20px">
													<a href="#HTML_replaybox"
														onclick="fieldSetReplay('$!reply.fullName','$!reply.createTime','REPLAY_$velocityCount')">
														$text.get("oa.bbs.lb.refreplay")</a>
												</li>
												<li style="float:right;padding-right:20px">
													#set($cc=$velocityCount+1)
												</li>
											</ul>
											<div class="newsReply" id="REPLAY_$velocityCount">
												<pre>$!reply.content</pre>
											</div>
										</div>
									</td>
								</tr>
								#end
							</tbody>
						</table>
					</div>
					#end
					<script type="text/javascript">
						function  totalNum(obj,changeId)
						{	var o = obj.value;
							var len = getStringLength(o);
							var id = "#"+changeId;
							$(id).html(len);
							if (changeId == 'fontnum_inp_detail') {
								if (len == 50) {
									$(id).css("color","#B68101");
								} else if (len > 50) {
									$(id).css("color","red");
								}
							} else {
								if (len == 200) {
									$(id).css("color","#B68101");
								} else if (len > 200) {
									$(id).css("color","red");
								}
							}
						}
					</script>
					<!-- ui-dialog -->
					<div id="dialog_photoDetail"
						title="$text.get('update')$text.get('photo')"
						style="display: none;">
						<label>
							$text.get('name')：






						</label>
						<input type="text" name="tempName" id="tempName_inp"
							value="$!photo.tempName" style="width: 212px;"
							onkeypress="totalNum(this,'fontnum_inp_detail')"
							onkeyup="totalNum(this,'fontnum_inp_detail')" />
						<span id="fontnum_inp_detail" style="color:green;">0</span>/
						<span id=maxnum_inp_detail " style="color:red">50</span>
						<br>
						<br>
						<label>
							$text.get('desc')： 
						</label>
						<!-- <input type="text" name="phoneDesc" id="photoDesc_inp" value = "$!photo.phoneDesc"/>0/200 -->
						<textarea name="phoneDesc" id="photoDesc_inp" rows="3"
							style="width: 212px;"
							onkeypress="totalNum(this,'fontnum_detail')"
							onkeyup="totalNum(this,'fontnum_detail')">$!photo.phoneDesc</textarea>
						<span id="fontnum_detail" style="color:green">0</span>/
						<span id="maxnum_detail" style="color:red">200</span>
						<br>
						<label>
							$text.get("isCover")：





						</label>
						
						<input type="radio" name="isCover" id="isCover_inp_yes" value="1"
							#if("$!photo.isCover" == "1") checked #end/>
						$!text.get("yes")
						<input type="radio" name="isCover" id="isCover_inp_no" value="0"
							#if("$!photo.isCover" == "0") checked #end />
						$!text.get("no")
					</div>
		</form>
		#if(1==2)
		<form name="form" method="post" action="/OACompanyNewsInfo.do"
			onsubmit="return checkReply()">
			<input type="hidden" name="operation" value="$globals.getOP("OP_SEND")"/>
			<input type="hidden" name="userId" id = "userId" value="$!photo.id" />
			<input type="hidden" name="newsId" id = "newsId" value="$!photo.id" />
			<input type="hidden" name="albumName" value="$!albumName" />
			<input type="hidden" name="replyType" value="photoInfoDetail">
			<input type="hidden" name="albumSelectId" value="$!albumId">
			<script type="text/javascript">
				
				function checkReply() {
					var content= editor.html();
					if(content.length==0){
				  		alert("$text.get("replyPhoto")$text.get('isNotNull')");
						return false;		
				  	}
					return true;
				}
			</script>
			<table border="0" cellpadding="0" cellspacing="0"
				class="oabbs_function_index" name="table" width="100%">
				<thead>
					<tr>
						<td style="height: 16px;line-height:100%;">
							<div>
								##$text.get("oa.bbs.celerityrevertto") $text.get("replyPhoto")
								<a name="HTML_replaybox"></a>
								<div>
						</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="height: 20px;">
							<div class="oabbs_function_revert">
								<ul>
									<li style="margin-bottom: 0px;margin-top: 0px;">
										<a name="HTML_bottom"></a>
										<textarea name="content" style="width:100%;height:90px;"
											id="replyContent"></textarea>
									</li>
									<div id="status" style="visibility:hidden;color:Red"></div>
									<div id="files_preview"></div>
									<li style="text-align:center;margin-top: 0px;height: 12px;">
										<br>
										<button type="submit" name="Submit" class="b3">
											$text.get("replyPhoto")
										</button>
										<button type="reset" class="b2">
											$!text.get("common.lb.reset")
										</button>
										<button type=button class="b2" onclick="forBack('1');">
											$!text.get("common.lb.back")
										</button>
									</li>
								</ul>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="clear"></div>
		</form>
		#end
	</body>
</html>