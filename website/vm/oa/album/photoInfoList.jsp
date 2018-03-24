<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>$text.get("oa.advice.readAdvice")</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
		<script language="javascript" src="/js/function.js"></script>
		<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
		<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
		<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
		
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
		
		<link type="text/css" href="/js/ui/jquery-ui-1.8.18.custom.css" rel="stylesheet" />	
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/ui/jquery-ui-1.8.18.custom.min.js"></script>
		<link rel="stylesheet" href="/style/css/mgs.css"/>
		<link rel="stylesheet" href="/js/tree/jquery.treeview.css" />
		<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>
		<script language="javascript" src="/js/public_utils.js"></script>
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
			function toPreUpload(){
				 form.operation.value = 91;
				 form.action ="/AlbumQueryAction.do?currentAlbum=$!album.id&pageType=photoInfoList";
				 form.submit();
			}
			$(function(){
			
				// Dialog			
				$('#dialog').dialog({
					autoOpen: false,
					height: 250,
					width: 350,
					buttons: {
						"$text.get('common.lb.ok')": function() { 
							 form.operation.value = 2;
							 var tempName = $("#tempName_inp").val();
							 var photoDesc = $("#photoDesc_inp").val();
							 var isCover = $(":radio:checked").val();
							 var pId = document.getElementById('pId').value;
							 
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
							 
							 form.action = "/PhotoAction.do?tempName="+tempName+"&photoDesc="+photoDesc+"&isCover="+isCover+"&replyType=tophotoInfoList&pId="+pId+"&albumSelectId=$!album.id";
							 form.submit();
							 alert('$text.get("common.msg.updateSuccess")');
							 $(this).dialog("close"); 
						}, 
						"$text.get('cancel')": function() { 
							$(this).dialog("close"); 
						}
					}
				});
				// Dialog Link
				/*
				$('#dialog_link').click(function(){
					$('#dialog_photoDetail').dialog('open');
					return false;
				});
				*/
				
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
			
			function update(pId,name,desc,cover) {
					document.getElementById('tempName_inp').value = name;
					document.getElementById('photoDesc_inp').value = desc;
					if(cover=='1'){
						document.getElementById('isCover_inp_yes').checked = true;
					}else {
						document.getElementById('isCover_inp_no').checked = true;
					}
					document.getElementById('pId').value=pId;
					var len_name = getStringLength(name);
					var len_desc = getStringLength(desc);
					$("#fontnum_inp_photo").html(len_name);
					$("#fontnum_photo").html(len_desc);
					$('#dialog').dialog('open');
					return false;
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
			return ;
		     }else{
			 
			fs=str.split("|");  
			for(var i=0;i<fs.length;i++)
			{
				var o=fs[i].split(";");
				if(o[0]!=null&&o[0]!="")
				{
					var favoriteURL = "$!favoriteURL" ;
					var favoriteName = encodeURI('$globals.getLoginBean().empFullName$text.get("oa.common.inviteread")$text.get("personal.album")');		
					var str="/AdviceAction.do?module=dedailAdviceInfo&favoriteName="+favoriteName+"&favoriteURL="+encodeURIComponent(favoriteURL)+"&receive="+o[0];
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
		
		//展示该图片详细








		function toShowPhotoInfo(pId) {
			albumName = encodiURILocal("$!album.name");
			albumName = encodeURIComponent(albumName);
			var url = "/PhotoAction.do?operation=$globals.getOP('OP_DETAIL')&albumName="+albumName+"&pId="+pId+"&albumId=$!album.id&orderType=$!orderType";
			
			window.location= url;
		}
			//返回
		function forBack(formType){
				var url = "/AlbumQueryAction.do?operation=$globals.getOP('OP_QUERY')";
				window.location= url;
		}
			
		
			//删除
		function delPhoto(pId) {
			if (confirm("$text.get('confirm.del.pho')")) {
				form.operation.value = 3;
				form.pId.value = pId;
			    form.submit();
			}
		}
		
		//显示隐藏的删除修改








		function showOperate(obj) {
			var v = $(obj).children(".operateOfAlbum").show();
        }
		function hideOperate(obj) {
		 	var v = $(obj).children(".operateOfAlbum").hide();
		}
		//改变排序
		function toChangeOrder (obj) {
			var orderType = obj.value;
			form.submit();
		}
		
		function toChangeManagerMenu(type) {
			if (type == '1') {
				$('#manager_hide').hide();
				$('#manager_show').show();
				$('.phoClass').hide();
				$("#showManagerMenu").hide();
			} else {
				$('.phoClass').show();
				$('#manager_hide').show();
				$('#manager_show').hide();
				$("#showManagerMenu").show();
			}	
		}
	
		//批量删除
		function multinomialDel(itemName,formObj){
			formObj = document.getElementsByName(formObj)[0];
			if(delHasChild(itemName)){
				alert('$text.get("common.exist.childcategory")');
				return false;
			}
			if(!isChecked(itemName)){
				alert('$text.get("common.msg.mustSelectOne")');
				return false;
			}
			formObj.operation.value= '$globals.getOP("OP_DELETE")';
			if(!confirm('$text.get("common.msg.confirmDel")')){
				formObj.operation.value = $globals.getOP("OP_QUERY");
				cancelSelected("input");
			}else{
				formObj.type.value = 'delPhotos';
				formObj.submit();
			}
		}
		
		//批量修改
		function toManagerUpdatePhos() {
			if(!isChecked('pho')){
				alert('$text.get("common.msg.mustSelectOne")');
				return false;
			}
			form.action = "/AlbumQueryAction.do";
			document.form.operation.value = 7;
			
			form.submit();
			
		}
		

//新邀请阅读

function openInvite(){
	var title = encodeURI("“$text.get("personal.album")”主题为【$!album.name】");
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
	</head>
	<body onLoad="saveReadingInfo()">
		<!--<form action="/UpFileServlet" enctype="multipart/form-data" onSubmit="return checkForm()" name="form2" method="post">-->
		<form action="/PhotoAction.do" name="form" method="post">
			<input type="hidden" name="noback" value="$!noback" />
			<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")"/>
			<input type="hidden" name = "albumSelectId" value="$!album.id"/>
			<input type="hidden" name = "pId" value="$!photo.id"/>
			<input type="hidden" name = "managerType" value = "managerPhos"/>
			<input type="hidden" name = "type" value=""/><!-- 删除 -->
			
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif"/>
				</div>
				<div class="HeadingTitle">
					<!-- <a  style="cursor:hand;color:green" onclick="forBack()"> -->$text.get("personal.album")<!-- </a> >> $!album.name -->
				</div>
				<ul class="HeadingButton">
					<li>
						<button type="button"
							onClick="openInvite()"
							class="bu_bg">
							<font color="black">$text.get("oa.common.invitereading")</font>
						</button>
						
					</li>
					<!-- 
					<li>
						<button type="button" onClick="closeWin();" class="b2">
							$text.get("common.lb.close")
						</button>
					</li> -->
					<li>
					<button type="button" onClick="forBack()" class="b2">
							$text.get("common.lb.back")
						</button>
					</li>
				</ul>
			</div>
			<!-- heading end-->	
			<div id="listRange_id" style="text-align:center;overflow:auto;height:400px;">
			<div style="width: 820px;margin: 0 auto; padding:0 0 0 0; overflow:hidden;">
				<script type="text/javascript">
					var oDiv=document.getElementById("listRange_id");
					var sHeight=document.body.clientHeight-38;
					oDiv.style.height=sHeight+"px";
				</script>
			
				<table style="width: 820px;margin-left: auto;margin-right: auto; padding:0 0 0 0; overflow:hidden;"
					border="0" cellpadding="0" cellspacing="0"
					class="oalistRange_list_function" name="table">
					<thead>
						<tr>
							<td class="oabbs_tr" style="text-align: left">
								<input type="hidden" name="keyId"
									value="$!globals.get($arr_newInfo,0)" />
								<a name="HTML_top">$text.get("oa.common.currentStation")：</a>
								<a  href="#" onclick="forBack()">$text.get("personal.album")</a> >> $!album.name
							</td>
						</tr>
					</thead>
				 </table>
				<div class="scroll_function_small_bbs"
					style="width: 820px;margin-left: auto;margin-right: auto;">
					<table cellpadding="0" cellspacing="0" class="oabbs_function_index" width="100%">
					   <tr>
						  <td colspan="4">
								<!-- <div style="float: left;margin-left: 4px;font-size: 12px;"><a href="#HTML_bottom">$text.get("speedReply")</a></div>
								 -->
								<div align="left" style="margin-left: 10px;margin-bottom: 15px;">
									<a href="#" onclick="toPreUpload()">$text.get("upload.photo")</a>
									&nbsp;&nbsp;
									
									<a href="#" id ="manager_show" onclick="toChangeManagerMenu('0')" href="#">$text.get("manager")</a>
									<a href="#" id ="manager_hide" onclick="toChangeManagerMenu('1')" style="cursor:hand;display: none;">$text.get("exit.manager")</a>&nbsp;&nbsp;
									
									$text.get("order") <select name="orderType" onchange="toChangeOrder(this)">
												<option value="desc" #if("$!orderType" == 'desc') selected="selected" #end>
											$text.get("orderByUploadTime.desc")
												</option>
												<option value="asc" #if("$!orderType" == 'asc') selected="selected" #end>
													$text.get("orderByUploadTime.asc")
												</option>
											</select>&nbsp;&nbsp;
										 
								</div>
								<div id = "showManagerMenu" style="text-align:center; tmargin-bottom: 15px;display: none">
									<input type="checkbox"  id = "checkAll" onclick="isCheckAll(this.value,'pho')"  value="true" />$text.get("checkAllOfPage")
									&nbsp;
									<a  href="#" onclick="toManagerUpdatePhos()" >$text.get("update.photo.info")</a>
									&nbsp;
									<a  href="#" onclick="multinomialDel('pho','form')" >$text.get("delete")</a>
									
								</div>
							 </td>
						</tr>
						<tbody id="showReplyList_advice">
							#if($photos.size()<=0)
								<tr>
									<td>
										<div class="oabbs_function_revert" style="margin-top: 150px;">
											<ul>
												 <center>
													<span style="color: orange;">$text.get("album.no.photo")！</span>	<a href="#" onclick="toPreUpload()">$text.get("upload.photo")</a>
												</center>	
												
											</ul>
										</div>
										<div class="oabbs_function_revert" style="margin-top: 150px;">
											<ul>
													
											</ul>
										</div>
									</td>
								</tr>
							#else
							 <tr>
							 	<td style="">
									#foreach ($p in $photos)
										<div onmousemove="showOperate(this)"
											onmouseout="hideOperate(this)" 
											style="float: left;height:153px; width:145px;border: solid 1 #C5C5C5;padding: 1px; vertical-align:top;margin-left: 10px; mamargin-top: 33px;margin-bottom: 13px;">
											<div style="width:145px;border: solid 1 #F7EFD6;padding: 1px;height: 112px;">
												<a href="#" style="cursor:hand;color:green" onclick="toShowPhotoInfo('$p.id')">
													<img src="/ReadFile?tempFile=path&path=/album/img/small/&fileName=$globals.encode($!p.beginName)" title="#if(1==2)$text.get("replyPhoto")$!p.replyCount #else $!p.tempName  #end"  name="rephoto"  width="130px;" height="100px;"   border="1";/>
												</a>
											</div>
											<div style="height:20px; width:145px; margin-top: 3px;" >
												<input type="checkbox"  class ="phoClass"  name="pho" value="$!p.id" style="display: none;"/><span style="color: #545454;" class="tempNameCla"><!-- #if($!p.tempName.length()>10)$!p.tempName.toString().substring(0,10)#else $!p.tempName #end --> $!p.tempName</span>
											</div>
											<div class="operateOfAlbum" style="margin-top: 0px;">
												
													<a href="#"  onclick="delPhoto('$!p.id')">$text.get("common.lb.del")</a>		
												
												
													<a href="#" id="dialog_link"  onclick="update('$!p.id','$!p.tempName','$!p.phoneDesc','$!p.isCover')">$text.get("oa.common.upd")</a>			
												
											</div>
										</div>
										#end
										<div class="oabbs_function_revert" style="margin-top: 220px;">
											<ul>
												&nbsp;
											</ul>
										</div>
									#end
								 </td>
							</tr>
						</tbody>
					</table>
				</div>
				<script type="text/javascript">
				function  totalNum(obj,changeId)
				{	var o = obj.value;
					var len = getStringLength(o);
					var id = "#"+changeId;
					$(id).html(len);
					if (changeId == 'fontnum_inp_photo') {
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
				<div id="dialog" title="$text.get('update')$text.get('photo')" style="display: none;">
					<label >$text.get("name")：</label>
					<input type="text" name="tempName" id="tempName_inp"  value = ""  style="width: 212px;" onkeypress="totalNum(this,'fontnum_inp_photo')" onkeyup="totalNum(this,'fontnum_inp_photo')"/> 
						<span id="fontnum_inp_photo" style="color:green;">0</span>/<span id=maxnum_inp" style="color:red">50</span>
					<br/>
					<br/>
					<label >$text.get("desc")：</label>
					<textarea  name="phoneDesc" id="photoDesc_inp" rows="3" style="width: 212px;" onkeypress="totalNum(this,'fontnum_photo')" onkeyup="totalNum(this,'fontnum_photo')"></textarea>
					<span id="fontnum_photo" style="color:green">0</span>/<span id="maxnum" style="color:red">200</span>
					<br></br>
					<label >$text.get("isCover")：</label>
					<input type="radio" name="isCover" id="isCover_inp_yes" value = "1" /> $!text.get("yes")
					<input type="radio" name="isCover" id="isCover_inp_no" value = "0"/> $!text.get("no")
					<input type="hidden" name ="pId" id="pId" value=""/>
					<!-- 
				<br>
				<br>
				<label style="margin-left: 30px;">
					<span style="color: red;">输入如:",',\等特殊字符将无效！</span>
				</label>
				 -->
				<br/>
				</div>
				#if($photos.size()>0)
				<div class="listRange_pagebar">
					$!pageBar
				</div>
				#end
				<div class="clear"></div>
		</form>
	</body>
</html>

