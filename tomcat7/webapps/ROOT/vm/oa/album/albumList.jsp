<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>$text.get("oa.advice.readAdvice")</title>
		<link rel="stylesheet"
			href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
		<script language="javascript" src="/js/function.js"></script>
		<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
		<script language="javascript" charset="utf-8"
			src="/js/kindeditor-min.js"></script>
		<script language="javascript" src="/js/validate.vjs"></script>
		<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
		
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
		<link type="text/css" href="/js/ui/jquery-ui-1.8.18.custom.css"
			rel="stylesheet" />
		<script language="javascript" src="/js/public_utils.js"></script>
		<script type="text/javascript"
			src="/js/ui/jquery-ui-1.8.18.custom.min.js"></script>
		<link rel="stylesheet" href="/style/css/mgs.css" />
		<link rel="stylesheet" href="/js/tree/jquery.treeview.css" />
		<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
		<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"  />
		<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
		<style type="text/css">
		button{
			width: 80px;
		}
		</style>
		<SCRIPT LANGUAGE=javascript>
			/*
			$(window).bind('load', function() {
				$('img').bind("contextmenu", function(e){ return false; })
			});
			*/
			function stop(){ 
				return false; 
			} 
			//document.oncontextmenu=stop; 
		</SCRIPT> 
		<script type="text/javascript">
			$(function(){
				// Dialog			
				$('#dialog').dialog({
					autoOpen: false,
					height: 200,
					width: 360,
					buttons: {
						"$text.get('common.lb.ok')": function() { 
							
							 var isUpdate = document.getElementById('isUpdate');
							 var albumName = $("#albumName_inp").val();
							 var albumDesc = $("#albumDesc_inp").val();
							if(albumName==null || albumName.length<=0)
							{
								alert("$text.get('album')$text.get('name')$text.get('isNotNull')"); 
								return false;
							}	 
							if(getStringLength(albumName)>50){
								alert("$text.get('album')$text.get('name')"+"$text.get('oa.common.not.more.than')"+50+"$text.get('oa.common.word.countOfCharacter')");
								return false ;
							}
						
							
							if(getStringLength(albumDesc)>200){
								alert("$text.get('album')$text.get('desc')"+"$text.get('oa.common.not.more.than')"+200+"$text.get('oa.common.word.countOfCharacter')");
								return false ;
							}
							 //albumName = encodiURILocal(albumName);
							 
							 var bool1 = isHaveSpecilizeChar(albumName);
							 if (bool1==false) {
								//alert("名称不能含有\"、'、\\等特殊字符");
							 	alert("$text.get('name')$text.get('contain.specilize')");							 	
								return false;
							 }
							 var bool2 = isHaveSpecilizeChar(albumDesc);
							 if (bool2==false) {
							 	//alert("描述不能含有\"、'、\\等特殊字符");
							 	alert("$text.get('desc')$text.get('contain.specilize')");
							 	return false;
							 }
						 	 albumName = encodeURIComponent(albumName);
							 //albumDesc = isHaveSpecilizeChar(albumDesc);
							 albumDesc = encodeURIComponent(albumDesc);
							 var addOrUpdate = 1;
							 if (isUpdate.value !=''&&isUpdate.value != undefined) {
							 	//修改
							 	form.operation.value = 2;
								 //var albumName = $("#albumName_inp").val();
								 //var albumDesc = $("#albumDesc_inp").val();
								 form.action = "/AlbumQueryAction.do?operation=2&albumName="+albumName+"&albumDesc="+albumDesc+"&albumId="+isUpdate.value+"&orderType=$!orderType";
								 addOrUpdate = 2;
							 } else {
								 form.operation.value = 1;
								 //var albumName = $("#albumName_inp").val();
								 //var albumDesc = $("#albumDesc_inp").val();
								 form.action = "/AlbumQueryAction.do?operation=1&albumName="+albumName+"&albumDesc="+albumDesc+"&orderType=$!orderType";
							 }
							 var whetherExistName = 0;
							 
							 jQuery.ajax({
							   type: "POST",
							   async:false,
							   url: "AlbumQueryAction.do?operation=1",
							   data: "addType=whetherExistName&albumName="+albumName,
							   success: function(data){
							   		
								   	whetherExistName = data;
									if (whetherExistName > 0 && addOrUpdate == 1) {
									 	//alert("该相册名称已存在");
									 	alert("$text.get('this.album.exist')");
									 	$("#albumName_inp").focus();
									 	return false;
									} else if (whetherExistName > 0 && addOrUpdate == 2 && oldName!= $("#albumName_inp").val() ) {
										//修改 如果是老名字 可以通过 否则不能
										alert("$text.get('this.album.exist')");
									 	$("#albumName_inp").focus();
									 	oldName = "";
									 	return false;
									} 
									
									 isUpdate.value="";
									 form.submit();
									 //$("#albumName_inp").attr("value","");
									 //$("#albumDesc_inp").attr("value","");
									 //$(this).dialog("close"); 
							   		
							   }
							});
							 
							 /*
							 jQuery.getJSON("/AlbumQueryAction.do?operation=1&addType=whetherExistName&albumName="+albumName, function(data){
								whetherExistName = data;
								if (whetherExistName > 0 && addOrUpdate == 1) {
								 	//alert("该相册名称已存在");
								 	alert("$text.get('this.album.exist')");
								 	$("#albumName_inp").focus();
								 	
								 	return false;
								 }
								 isUpdate.value="";
								 form.submit();
								 $("#albumName_inp").attr("value","");
								 $("#albumDesc_inp").attr("value","");
								 $(this).dialog("close"); 
							 })
							 */
						}, 
						"$text.get('cancel')": function() { 
							document.getElementById('isUpdate').value="";
							$("#albumName_inp").attr("value","");
							$("#albumDesc_inp").attr("value","");
						
							$(this).dialog("close"); 
						}
					}
				});
				
				// Dialog Link 
				$('#dialog_link').click(function(){
					$('#dialog').dialog('open');
					return false;
				});
				
			});
			
			$(function(){
			
				$(".tempNameCla").each(function(i){
					
					  $(this).text(substr($(this).text(),20));
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
			
			
			var oldName = "";
			function update(aId,name,desc) {
					oldName = name;
					document.getElementById('albumName_inp').value = name;
					document.getElementById('dialog').title = "修改相册";
					document.getElementById('albumDesc_inp').value = desc;
					document.getElementById('isUpdate').value = aId;
					var len_name = getStringLength(name);
					var len_desc = getStringLength(desc);
					$("#fontnum_inp").html(len_name);
					$("#fontnum").html(len_desc);
					$('#dialog').dialog('open');
					return false;
			}
			//上传前先判断是否存在相册
			function toPreUpload(){
				 if ("$albumList.size()" <= 0) {
				 	//alert("请先创建相册");
				 	alert("$text.get('please.create.album')");
				 	return false;
				 }
				 form.operation.value = 91;
				 form.submit();
			}
			
			function toDel(id,num){
			
				//if(confirm("您确定删除该相册？相册中有"+num+"张照片")){
				if(confirm("$text.get('del.info')"+num+"$text.get('numInfo')")){
					 var albumName = $("#albumName_inp").val();
					 var albumDesc = $("#albumDesc_inp").val();
					 form.action = "/AlbumQueryAction.do?operation=3&keyId="+id;
					 form.submit();
				 }
			}
			//改变排序
			function toChangeOrder (obj) {
				var orderType = obj.value;
				form.submit();
				
					
			}

//新邀请阅读








function openInvite(){
	var title = encodeURI("“$text.get("personal.album")”");
	var favoriteName = encodeURI("$!messageTitle");
	var userinvite = encodeURI("$globals.getEmpNameById($LoginBean.id)");
	var urls = "/AdviceAction.do?module=invitePre&id=$oaOrdain.id&userinvite="+userinvite+"&favoriteURL=$!favoriteURL&favoriteName="+favoriteName+"&title="+title;
	asyncbox.open({
	 title : '邀请阅读',
	　　　url : urls,
	　　　cache : true,
	　　　width : 700,
	　　　height : 350,
		 btnsbar : jQuery.btn.OKCANCEL,
			callback : function(action,opener){
	　　　　　	//判断 action 值


	　　　　　	if(action == 'ok'){
	　　　　　　　	//在回调函数中 this.id 可以得到该窗口 ID
	　　　　　　　		if(!opener.checkeSet()){
					return false;
		   		}else{
		   			opener.submitForm();
		   		}
	　　　　　}
　　　 	}
　	});
}

		
		</script>

		<style type="text/css">
			.demoHeaders { margin-top: 2em; }
			#dialog_link {padding: .4em 1em .4em 20px;text-decoration: none;position: relative;}
			#dialog_link span.ui-icon {margin: 0 5px 0 0;position: absolute;left: .2em;top: 50%;margin-top: -8px;}
		</style>

		<script type="text/javascript">
		
			function saveReadingInfo()
			{
			  jQuery(".operateOfAlbum").hide();
			  if("$!globals.get($arr_newInfo,15)"=="1"){
		 		var str="/UtilServlet?operation=addOAReadingInfo&infoId=$!globals.get($arr_newInfo,0)&infoTable=OAAdviceInfo";
		 	     AjaxRequest(str);
		    	var value = response;		
				/*if(value=="1"){			 	
					alert('$text.get("mrp.help.saveSucess")');
				}
				else
				{
				alert('$text.get("common.alert.updateFailure")');
				}*/
				
				}
				//MJ
				//	var width = jQuery(document).width();
				//var v = jQuery("#showReplyList_advice").find("div div").css('width',width-50);
				//v.width = width;
			}
			function checkForm()
			{
				return confirm("$text.get("oa.common.sureDelete")")
			}
			
			function collection()
			{	
				var favoriteURL = "$!favoriteURL" ;
				var favoriteName = encodeURI("$!globals.get($arr_newInfo,2)") ;						
				//var str="/OAFavorite.do?module=dedailAdviceInfo&favoriteName="+favoriteName+"&favoriteURL="+favoriteURL;
		 		var str="/UtilServlet?operation=addDedailAdviceInfoCollection&module=dedailAdviceInfo&favoriteName="+favoriteName+"&favoriteURL="+favoriteURL;
		 		 AjaxRequest(str);
		    	var value = response;
				var oaul=document.getElementById("empList");
				
				if(value=="1"){			 	
					alert('$text.get("oa.favorite.add.success")');
				}
				else if(value=="2"){
					alert('$text.get("oa.favorite.exist")');
				}else
				{
				alert('$text.get("oa.favorite.add.failture")');
				}
			}
			function openSelect(urlstr,obj,field){
				var display = encodeURI("$text.get("oabbs.com.lb.PleaseReader")") ;
				var str  = window.showModalDialog(urlstr+"&MOID=47d49699_0909161434159530002&MOOP=add&LinkType=@URL:&displayName="+display,"","dialogWidth=730px;dialogHeight=450px"); 
				var value="";
				if(str.split(';')[0]=="")return;
				if(str.indexOf("@Sess:")>=0){
					var value="";
					return;
			    }else{
					fs=str.split("|");  
					for(var i=0;i<fs.length;i++)
					{
						var o=fs[i].split(";");
						if(o[0]!=null&&o[0]!="")
						{
							var favoriteURL = "$!favoriteURL";
							var favoriteName = encodeURI('$globals.getLoginBean().empFullName$text.get("oa.common.inviteread")$text.get("enterprise.album")') ;
							var str="/AdviceAction.do?module=dedailAdviceInfo&favoriteName="+favoriteName+"&favoriteURL="+favoriteURL+"&receive="+o[0];
			 				AjaxRequest(str);
			    			value = response;
						}
					}
					if(value!="0"&&value!=""){			 	
						alert("$text.get("oa.common.invitesucceed")");
					}
					else{
						alert("$text.get("oa.common.inviteunsucceed")");
					}
				}
			}
			
			//显示隐藏的删除修改















			function showOperate(obj) {
				var v = $(obj).children(".operateOfAlbum").show();
	        }
	
			function hideOperate(obj) {
			 	var v = $(obj).children(".operateOfAlbum").hide();
			}
			//跳转到 展示相册的页面















			
			function toPhotoInfoList(albumId) {
				window.location="/PhotoAction.do?operation=$globals.getOP('OP_QUERY')&albumSelectId="+albumId;
			}
		
		</script>
	</head>
	<body onLoad="saveReadingInfo()">
		<!--<form action="/UpFileServlet" enctype="multipart/form-data" onSubmit="return checkForm()" name="form2" method="post">-->
		<form action="/AlbumQueryAction.do" name="form" method="post">
			<input type="hidden" name="noback" value="$!noback" />
			<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")"/>
			<input type="hidden" name="pageType"  value="albumList" />
			<div class="Heading" >
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif"/>
				</div>
				<div class="HeadingTitle">
					$text.get("personal.album")
				</div>
				<ul class="HeadingButton">
					<li>
						<!--  
						<button type="button"
							onClick="openSelect('/UserFunctionAction.do?tableName=OAMessage&selectName=MsgReceive&operation=22')"
							style="width:80px;" class="b2">
							$text.get("oa.common.invitereading")
						</button> 
						-->
						<button type="button"
							onClick="openInvite()"
							class="bu_bg">
							<font color="black">$text.get("oa.common.invitereading")</font>
						</button>
						<!-- 
						<button type="button" onClick="collection()" class="b2">
							$text.get("aio.add.favorite")
						</button> -->
					</li>
					<li>
						<!-- 
						<button type="button" onClick="closeWin();" class="b2">
							$text.get("common.lb.close")
						</button> -->
					</li>
				</ul>
			</div>
			<!-- heading end-->
			<div id="listRange_id" style="text-align:center;overflow:hidden;height:400px;overflow-y:auto;">
				<div style="width: 820px;margin: 0 auto;">
					<script type="text/javascript">
					//var oDiv=document.getElementById("listRange_id");
					var oDiv = $("#listRange_id");
					var sHeight=document.body.clientHeight-38;
					$(oDiv).attr("height",sHeight+"px");
					
					
				</script>

					<table style="width: 820px;margin-left: auto;margin-right: auto;"
						border="0" cellpadding="0" cellspacing="0"
						class="oalistRange_list_function" name="table">
						<thead>
							<tr>
								<td class="oabbs_tr">
									<input type="hidden" name="keyId"
										value="$!globals.get($arr_newInfo,0)" />
								
									<div style="float:left;">
										<a name="HTML_top">$text.get("oa.common.currentStation")：$text.get("personal.album")<!--$text.get("album.list")--></a>
									</div>
								</td>
							</tr>
						</thead>
					</table>

					<div class="scroll_function_small_bbs"
						style="width: 820px;margin-left: auto;margin-right: auto;">
						<table cellpadding="0" cellspacing="0"
							class="oabbs_function_index" width="100%">
							<tr>
								<td colspan="4">
									<!--
										 <div style="float: left;margin-left: 4px;font-size: 12px;"><a href="#HTML_bottom">$text.get("speedReply")</a></div>
									 -->
									<div align="left"
										style="margin-left: 10px;margin-bottom: 15px;">
										<a href="#" onclick="toPreUpload()">$text.get("upload.photo")</a>
										&nbsp;
										
										<a href="#" id="dialog_link" style="margin-left: -20px;">
										<span class="ui-icon ui-icon-newwin"></span>$text.get("createAlbum")</a>
									
										 $text.get("order")
										<select name="orderType" onchange="toChangeOrder(this)">
											<option value="desc" #if("$!orderType" ==
												'desc') selected="selected" #end>
												$text.get("orderByCreateTime.desc")
											</option>
											<option value="asc" #if("$!orderType" ==
												'asc') selected="selected" #end>
												$text.get("orderByCreateTime.asc")
											</option>
										</select>
										
									</div>
								</td>
							</tr>
							<tbody id="showReplyList_advice">
								<tr>
									<td >
										#foreach ($album in $albumList)
										<div onmousemove="showOperate(this)"
											onmouseout="hideOperate(this)"
											style="float: left;height:160px; width:145px;border: solid 1 #C5C5C5;padding: 1px;position:relative; vertical-align:top;margin-left: 10px; mamargin-top: 33px;margin-bottom: 13px;">
											<div
												style="width:145px; border: solid 1 #F7EFD6;padding: 1px;height: 100px;">
												#if("$album.cover"!='')
												<a style="cursor:hand;color:#BACAE6"
													onclick="toPhotoInfoList('$album.id')"> <img
														src="/ReadFile?tempFile=path&path=/album/img/small/&fileName=$globals.encode($album.cover)"
														name="rephoto"  width="130px;" height="110px;"   border="1"
														title="$!album.name"/>
												</a>
												
												#else
												
												<a style="cursor:hand;color:#BACAE6" onclick="toPhotoInfoList('$album.id')">
													<img name="myPhoto" title="$!album.name" src="/style/images/no_head.gif" border="1" width="130px" height="100px"/> 
												</a> 
												
												#end
											</div>
											<div style="height: 5px;">
												<span style="color: #545454;" class = "tempNameCla"><!--#if($album.name.length()>10)$album.name.toString().substring(0,10)#else $album.name #end--> $!album.name</span>
											</div>
											<div style="height: 10px;margin-top: 10px;">
												<span style="color: #A8A8A8">$text.get("share")$!album.totalPhotoNum
													$text.get("sheet")#if ($!album.totalReplyNum > 0)
													&nbsp;&nbsp;$text.get("reply")$!album.totalReplyNum #end</span>
											</div>
											<div class="operateOfAlbum"
												style="position:absolute; bottom: 5px;left: 45px;  ">
												<a href="#" class="dialog_link_update"
													onclick="update('$!album.id','$!album.name','$!album.albumDesc')">$text.get("oa.common.upd")</a>
													
												<a onclick="toDel('$!album.id','$!album.totalPhotoNum')"
													href="#">$text.get("common.lb.del")</a> 
											
												
													#if($!album.totalPhotoNum>0)
														<a href="/UtilServlet?operation=uploadPhos&albumId=$!album.id" class = "dialog_link_update">$text.get("oa.common.download")</a>
													#end
												
											</div>
										</div>
										#end
										<div class="oabbs_function_revert" style="margin-top: 220px;">
											<ul>
												&nbsp;
											</ul>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>#if($albumList.size()>0)
				<div class="listRange_pagebar" style="margin-right: -50px;" >
					$!pageBar
				</div>
				#end
				</div>
			</div>
			<script type="text/javascript">
				function  totalNum(obj,changeId)
				{	var o = obj.value;
					var len = getStringLength(o);
					var id = "#"+changeId;
					$(id).html(len);
					if (changeId == 'fontnum_inp') {
						if (len == 50) {
							$(id).css("color","#B68101");
						} else if (len > 50) {
							$(id).css("color","red");
						} else {
						    $(id).css("color","green");
						}	
					} else {
						if (len == 200) {
							$(id).css("color","#B68101");
						} else if (len > 200) {
							$(id).css("color","red");
						} else {
						    $(id).css("color","green");
						}	
					}
				}
			</script>
			
			<!-- ui-dialog -->
			<div id="dialog" title="$text.get('album')$text.get('information')"
				style="display: none;">
				<label>
					$text.get('album')$text.get('name')：










				</label>
				<input type="text" name="albumName" id="albumName_inp"
					style="width: 212px;" onkeypress="totalNum(this,'fontnum_inp')" onkeyup="totalNum(this,'fontnum_inp')"/>
				<span id="fontnum_inp" style="color:green;">0</span>/<span id=maxnum_inp" style="color:red">50</span>
				<br>
				<br>
				<label>
					$text.get('album')$text.get('desc')：










				</label>
				<textarea name="albumDesc" id="albumDesc_inp" rows="3"
					style="width: 212px;" onkeypress="totalNum(this,'fontnum')" onkeyup="totalNum(this,'fontnum')"></textarea>
				<span id="fontnum" style="color:green">0</span>/<span id="maxnum" style="color:red">200</span>
				<input type="hidden" name="isUpdate" id="isUpdate" value="" />
				<!-- 
				<br>
				<br>
				<label >
					<center>
						<span style="color: red;">输入如:",',\等特殊字符将无效！</span>
					</center>
				</label>
				 -->
			</div>
			<div class="clear"></div>
		</form>
	</body>
</html>

