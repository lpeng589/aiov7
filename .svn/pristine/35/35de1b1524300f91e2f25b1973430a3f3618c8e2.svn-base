<html>
	<head>
<meta name="renderer" content="webkit">		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>$text.get("oa.advice.readAdvice")</title>
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
		<script language="javascript" src="/js/function.js"></script>
	
		
		<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
	
		<script type="text/javascript" src="/js/jquery.js"></script>
		<link type="text/css" href="/js/ui/jquery-ui-1.8.18.custom.css" rel="stylesheet" />	
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/ui/jquery-ui-1.8.18.custom.min.js"></script>
		<link rel="stylesheet" href="/style/css/mgs.css"/>
		<link rel="stylesheet" href="/js/tree/jquery.treeview.css" />
		<script src="/js/tree/jquery.treeview.js" type="text/javascript"></script>

		<style type="text/css">  
		  .fil
		  {  
		    width:300px;
		  }
		  .fieldset_img
		  {
		  border:1px solid blue;
		  width:550px;
		  height:180px;
		  text-align:left;
		
		  }
		  .fieldset_img img
		  {
		     border:1px solid #ccc;
		  padding:2px;
		  margin-left:5px;
		  }
		  #ImgList li
		  {
		     text-align:center;
		     list-style:none;
			  display:block;
			  float:left;
			  margin-left:5px;
		  }
		  .showFontPho
		  {
		  	width: 150px;
	        margin-left: auto;
	        margin-right: auto;
	        outline: 0;
	        word-wrap: break-word;
	        overflow-x: hidden;
	        overflow-y: auto;
	        _overflow-y: visible
	   
		  }
		</style>  
	<script type="text/javascript">
		
		
		function checkForm()
		{
			return confirm("$text.get("oa.common.sureDelete")")
		}
		
		
	
	</script>
	<script type="text/javascript">
			$(function(){
				// Dialog			
				$('#dialog_upload').dialog({
					autoOpen: false,
					height: 300,
					width: 350,
					buttons: {
						"$text.get('common.lb.ok')": function() { 
							$(this).dialog("close"); 
							 form.operation.value = 1;
							 var albumName = $("#albumName_inp_upload").val();
							 var albumDesc = $("#albumDesc_inp_upload").val();
							 form.action = "/AlbumQueryAction.do?operation=1&currentAlbum="+albumName+"&albumName="+albumName+"&albumDesc="+albumDesc;
							 form.submit();
							 $(this).dialog("close"); 
						}, 
						"$text.get('cancel')": function() { 
							$(this).dialog("close"); 
						}
					}
				});
				// Dialog Link
				$('#dialog_link_upload').click(function(){
					$('#dialog_upload').dialog('open');
					return false;
				});
			});
			function toPreUpload(){
				 form.operation.value = 91;
				
				 form.submit();
			}
			
			
			
			var curpicattType= "ATT";
			var isrefresh = false;
			var pathOfImg = '/album/img';
			function openAttachUpload(path,type,refresh){
			
			 pathOfImg = path;
			 var filter = "";
			 if(type == "PIC"){
				 filter = "Image";
				 curpicattType = "PIC";
			 }
			 
			 if(refresh ){
				isrefresh = refresh;//本来true
			 }
			 
			
			 var attachUpload = document.getElementById("attachUpload");
			
			 if(attachUpload == null){
				 uploadDiv =document.createElement('<div id="attachUpload" style="position:absolute; top:10px;left:30px; width=600px;height:300px; display:block"></div>');
				 document.body.appendChild(uploadDiv);
				 attachUpload = document.getElementById("attachUpload");
			 }
			 attachUpload.style.left= ((document.body.clientWidth - 500) /2) +"px";
			 attachUpload.style.top= ((document.body.clientHeight - 250) /2) +"px";
			 attachUpload.style.display="block";
			
			 attachUpload.innerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="fileUpload" width="500" height="250" codebase="http:/'+'/fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">'+
			 ' <param name="movie" value="/flash/FileUpload.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#869ca7" /><param name="flashvars" value="maxSize=0&uploadUrl=/UploadServlet;jsessionid=$session.id?path='+path+'&filter='+filter+'" />'+
			 ' <param name="allowScriptAccess" value="sameDomain" /><embed src="/flash/FileUpload.swf" quality="high" bgcolor="#869ca7" width="500" height="250" name="column" align="middle" play="true" loop="false"'+
			 ' flashvars="maxSize=0&uploadUrl=/UploadServlet;jsessionid=$session.id?path='+path+'&filter='+filter+'" quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http/'+'/www.adobe.com/go/getflashplayer"></embed></object>';
			
			} 
			
			function onCompleteUpload(retstr){ 
			  retstr = decodeURIComponent(retstr);  
			 var strs = retstr.split(";");
			 for(i=0;i<strs.length;i++){ 
				 if(strs[i] != ""){ 
					 insertNextFile(strs[i]);
				 }
			 } 
			 
			 var attachUpload = document.getElementById("attachUpload");
			 attachUpload.style.display="none";
			 /*
			 if(isrefresh){
			 
				 window.location.href = window.location.href;
			 }
			 */
			 
			 //让页面相应的button显示隐藏 isrefresh 暂时没用到 就用来接收传过来的值





	
			 var isHavePic = document.form.picFiles.value;
			 if (isrefresh == 'uploadPho' && isHavePic) {
			 	//第一次上传后 显示 继续上传 完成等





			 	$('#uploadOk').show();
			 	$('#uploadPho').hide();
			 	$('#uploadAgain').show();
			 	/*
			 	alert(document.getElementById("uploadPho").style.display)
			 	document.getElementById("uploadOk").style.display='block';
			 	document.getElementById("uploadAgain").style.display='block';
			 	*/
			 }
			 
			}
			 
			function insertNextFile(str) 
			{ 
			 if(curpicattType == "PIC"){
				
				 var filevalue = document.form.picFiles.value;
				 if(typeof(document.form.picFiles) == "undefined"){
					 return;
				 }
				 if(filevalue.indexOf(str) == -1){ 
						 var fileHtml = '';
						 fileHtml += '<div id ="'+str+'" style ="float: left;height:160px; width:165px;border: solid 1 #C5C5C5;padding: 1px;position:relative; vertical-align:top;margin-left: 25px; mamargin-top: 33px;margin-bottom: 13px;">';
						 fileHtml += '<div style="width:155px;border: solid 1 #F7EFD6;padding: 1px;height: 122px;"><img src="/ReadFile?tempFile=path&path=/album/img/small/&fileName='+str+'" width="160px;" height="120px;" title="'+str+'"  border="5"/></div><div><span style="margin-left: 0px;color: green;font;max-width:150px;height:20px;" class="showFontPho">'+str.substr(0,12)+'</span></div><div style="position:absolute; text-align:left;top: 0px;left: 154px;height:10px;"><a  onclick="removeFile(\'' + str + '\',\'PIC\');"><img src="/style/images/del.gif"></a></div></div>';
						 var fileElement = document.getElementById("files_preview");
						 fileElement.innerHTML = fileElement.innerHTML + fileHtml; 
						 document.form.picFiles.value = filevalue+str+";";
						//修改 完成等与上面的间距





						$("#operateUp").attr("style","margin-top: 15px;");
						
				 }
				
			 }else{
				
				 if(typeof(document.form.attachFiles) == "undefined"){
					 return;
				 }
				 var filevalue = document.form.attachFiles.value;
				 
				 if(filevalue.indexOf(str) == -1){ 
					 var fileHtml = '';
					 fileHtml += '<div id ="'+str+'" style ="height:18px; color:#ff0000;">';
					 fileHtml += '<a href="javascript:;" onclick="removeFile(\'' + str + '\');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;';
					 fileHtml += str+' <input type=hidden name="attachFile" value="'+str+'"/></div>';
				
					 var fileElement = document.getElementById("files_preview");
					 fileElement.innerHTML = fileElement.innerHTML + fileHtml; 
					 document.form.attachFiles.value = filevalue+str+";";
				 }
				
			 }
			
			}
			function removeFile(file,picType) 
			{
			
			 if(picType == "PIC"){
				 document.getElementById("files_preview").removeChild(document.getElementById(file)); 
				 var filevalue = document.form.picFiles.value;
				 document.form.picFiles.value = filevalue.substr(0,filevalue.indexOf(file))+ filevalue.substr(filevalue.indexOf(file)+file.length+1);
				 document.form.delPicFiles.value = document.form.delPicFiles.value+file+";";
			 }else{
			 	
				 document.getElementById("files_preview").removeChild(document.getElementById(file)); 
				 var filevalue = document.form.attachFiles.value;
				 document.form.attachFiles.value = filevalue.substr(0,filevalue.indexOf(file))+ filevalue.substr(filevalue.indexOf(file)+file.length+1);
				 document.form.delFiles.value = document.form.delFiles.value+file+";";
			 }
			}
			//当点击Ok的时候 提交对应的数据 选择对应的相册 展示 需要修改的信息
			function toSubmitByOK(){
				var actionn = document.form.action;
				document.form.operation.value = 7;
				var filevalue = document.form.picFiles.value;
				var delFiles = document.form.delFiles.value;
				var picFiles = document.form.picFiles.value;
				var attachFiles = document.form.attachFiles.value;
				var delPicFiles = document.getElementById("delPicFiles").value;
				if (filevalue == ''||filevalue == null){
					alert("$text.get('please.upload.photo')");
				} else {
					form.submit();
				}
			}
			//返回
			function back() {
				if ("$!pageType" == "photoInfoList") {
						var url = "/PhotoAction.do?operation=$globals.getOP('OP_QUERY')&albumSelectId=$!currentAlbum&orderType=$!orderType";
						window.location= url;//返回当前相册
				
				} else {
						var url = "/AlbumQueryAction.do?operation=$globals.getOP('OP_QUERY')&orderType=$!orderType";
						window.location= url;//返回相册
				}
			
			}
			
		</script>
	</head>
	<body >
		<form action="/AlbumQueryAction.do" name="form" method="post">
			<input type="hidden" name="operation" value="$globals.getOP("OP_QUERY")"/>
			<input type="hidden" name="attachFiles" value="">
			<input type="hidden" name="delFiles" value="">
			<input type="hidden" name="picFiles" value="">
			<input type="hidden" name="delPicFiles" value="">
			<input type="hidden" name="orderType" value = "$!orderType"/>
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$text.get("personal.album")
				</div>
				<ul class="HeadingButton">
					<li>
						<!-- 
						<button type="button" onClick="closeWin();" class="b2">
							$text.get("common.lb.close")
						</button>
 							-->
 						<button type="button" onClick="back()" class="b2">
							$text.get("common.lb.back")
						</button>
					</li>
				</ul>
			</div>
			<!-- heading end-->	
			<div id="listRange_id" style="text-align:center;overflow: auto">
			<div style="width: 800px;">
				<script type="text/javascript">
					var oDiv=document.getElementById("listRange_id");
					var sHeight=document.body.clientHeight-38;
					oDiv.style.height=sHeight+"px";
				</script>
		
				<table style="width: 800px;margin-left: auto;margin-right: auto;"
					border="0" cellpadding="0" cellspacing="0"
					class="oalistRange_list_function" name="table">
					<thead>
						<tr>
							<td class="oabbs_tr">
								<div style="float: left;">
									<a name="HTML_top">$text.get("oa.common.currentStation")：$text.get("upload.photo")</a>
								</div>
							</td>
						</tr>
					</thead>
				 </table>
		
				<div class="scroll_function_small_bbs"
					style="width: 800px;margin-left: auto;margin-right: auto;">
					<table cellpadding="0" cellspacing="0" class="oabbs_function_index" width="100%">
					   <tr>
						  <td>
								
								<div align="left" style="margin-left: 10px;margin-bottom: 15px;">
									<ul>
									<li style="float: left;">$text.get("choose.album"):
									<select name="albumSelectId" id="albumSelectId" style="width: 230px;">
										#foreach($album in $albumList)
										<option title="$album.name" value="$album.id" #if("$!currentAlbum" == "$album.id") selected #end>
											$album.name  
										</option>
										#end
									</select>
									</li>
									<!-- 
									<li style="float: left" >
									&nbsp;&nbsp;&nbsp;&nbsp;<span style="text-align: center"> 或</span></li>
									<li >
										<a href="#" id="dialog_link_upload" class="ui-state-default ui-corner-all"><span class="ui-icon ui-icon-newwin"></span>创建相册</a></p>
									</li>
									 -->
									</ul>
								</div>
							 </td>
						</tr>
						<tbody >
							<tr>
								<td>
									<div class="oabbs_function_revert" >
										<ul>
											 <div id="files_preview"></div>
											
										</ul>
									</div>
									<div class="oabbs_function_revert" id = "operateUp" style="margin-top: 200px;">
										<ul>
											<center>
												<span id="files"  > 
													<button type=button id = "uploadPho"  class="b2" onClick="openAttachUpload('/album/img','PIC','uploadPho')">$text.get("upload.photo")</button>
													<button type=button id = "uploadOk" style="display: none;" class="b2" onClick="toSubmitByOK()",''>$text.get("complete")</button>
													<button type=button id = "uploadAgain" style="display: none;" class="b2" onClick="openAttachUpload('/album/img','PIC','uploadAgain')">$text.get("again.upload")</button>
													<!-- 
													<button type="button" onClick="back()" class="b2">
															$text.get("common.lb.back")
													</button>
													 -->
												</span> 
											</center>
										</ul>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				</div></div>
				<div class="clear"></div>
				<!-- ui-dialog -->
				<div id="dialog_upload" title="创建相册$text.get('createAlbum')" style="display: none;">
						<label >$text.get('album')$text.get('name')相册名称：</label>
						<input type="text" name="albumName" id="albumName_inp_upload"  /> 0/30
						<br>
						<br>
						<label >$text.get('album')$text.get('desc')相册描述：</label>
						<input type="text" name="albumDesc" id="albumDesc_inp_upload"/>0/200
				</div>	
			

		<div class="clear"></div>
		</form>
	</body>
</html>

