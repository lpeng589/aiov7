<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
<meta name="renderer" content="webkit">	
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>$text.get("oa.advice.readAdvice")</title>
		<script src="/vm/oa/netDisk/attach.js" type="text/javascript"></script>	 
		<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
		<link  type="text/css" rel="stylesheet" href="/style/css/sharingStyle.css" />
		<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
		
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/function.js"></script>
		<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
		
		
		
	
		<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
		<script language="javascript" src="/js/public_utils.js"></script>
	
		<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>

		<script language="javascript" src="/js/define.vjs"></script>
		
 		<script> 
 	    	$(function(){
 	    		
				$(".tempName").each(function(i){
					if (getStringLength($(this).text()) > 20){
						$(this).text(substr($(this).text(),20)+"...");
					}
					 
				 });
			});
			if(typeof(top.jblockUI)!="undefined")top.junblockUI();
		</script>
		<script>
			var curpicattType= "ATT";
			var isrefresh = false;
			var pathOfImg = '$!pId';
			var uploadDiv = "";
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
					 //uploadDiv =document.createElement('<div id="attachUpload" style="position:absolute; top:10px;left:30px; width=600px;height:300px; display:block"></div>');
					// document.body.appendChild(uploadDiv);
					// attachUpload = document.getElementById("attachUpload");
					 uploadDiv = document.createElement("div");   
					 uploadDiv.id="attachUpload"; 
			         uploadDiv.style.position = "absolute";   
			       	 uploadDiv.style.top ="10px";   
			         uploadDiv.style.left ="30px";   
			         uploadDiv.style.width = "600px";   
			         uploadDiv.style.height = "300px"; 
			         uploadDiv.style.display = "block";   
					 document.body.appendChild(uploadDiv);
					 attachUpload = document.getElementById("attachUpload");
				 }
				 attachUpload.style.left= ((document.body.clientWidth - 500) /2) +"px";
				 attachUpload.style.top= ((document.body.clientHeight - 250) /2) +"px";
				 attachUpload.style.display="block";
				 path = path+",uploadTree.jsp";
				 attachUpload.innerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="fileUpload" width="500" height="250" codebase="http:/'+'/fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">'+
				 ' <param name="movie" value="/flash/FileUpload.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#869ca7" /><param name="flashvars" value="maxSize=0&uploadUrl=/UploadServlet;jsessionid=$session.id?path='+path+'&filter='+filter+'" />'+
				 ' <param name="allowScriptAccess" value="sameDomain" /><embed src="/flash/FileUpload.swf" quality="high" bgcolor="#869ca7" width="500" height="250" name="column" align="middle" play="true" loop="false"'+
				 ' flashvars="maxSize=0&uploadUrl=/UploadServlet;jsessionid=$session.id?path='+path+'&filter='+filter+'" quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http/'+'/www.adobe.com/go/getflashplayer"></embed></object>';
			} 
			
			function onCompleteUpload(retstr){ 
				retstr = decodeURIComponent(retstr);  
				 var attachUpload = document.getElementById("attachUpload");
				 attachUpload.style.display="none";
				 if (retstr) {
					 asyncbox.tips('上传刷新页面可能需要一段时间，请耐心等待...','success',10000);
					 if(isrefresh){
					 window.location.href = window.location.href;
	 				 }
				 }
			}
			//当点击Ok的时候 提交对应的数据 选择对应的相册 展示 需要修改的信息
			function toSubmitByOK(){
				form.submit();
			}
			//改变排序
			function toChangeOrder(obj) {
				form.submit();
			}
			
			
		</script>
		
		<script type="text/javascript">
			
			$(function(){
				var path = "$viewPath";
				path = path.replace("\\\\","\\");
				$("#imgsPath").html(path);
				$(".photoList_title").each(function(i){
					  $(this).text(substr($(this).text(),20));
				 });
			});
			
			function showOp(id){
			   var bid = document.getElementById(id);
			   var sid = document.getElementById(id+" _menu");
			   var path = "$pId";
			   var softurl = "/ReadFile?tempFile=path&path="+encodeURIComponent(path)+"&fileName="+encodeURIComponent(id)+"&albumType=tree";
			   var fileExtend = id.substr(id.lastIndexOf(".")+1).toLowerCase();
			   
			   if (fileExtend == 'gif' ||fileExtend == 'bmp' ||fileExtend == 'png' ||fileExtend == 'jpeg' ||fileExtend == 'jpg') {
			  	   if(sid && sid.innerHTML.trim()==""){
				   	  sid.innerHTML="<img id='"+id+"_loading' title='加载中...' src='/style/images/loading.gif' style='vertical-align:middle;'/>";
				      sid.innerHTML+="<img width='130px;' height='100px;' id='"+id+"_img' src='"+softurl+"' onload='showImg(\""+id+"\")' style='display:none;vertical-align:middle;'/>";
				   }
				  // jQuery.noConflict();
			   	   showMenu(id);
			   }
			 }
		
						 
			 function showImg(id){
				   var mid = document.getElementById(id+"_loading");
				   var iid = document.getElementById(id+"_img");
				   mid.style.display='none';
				   iid.style.display='block';
				  // var sid = document.getElementById(id+" _menu");
			 	   //sid.style.display="block";
			 }
			
			 
			//返回
			function forBack(formType){
					var url = "/AlbumQueryAction.do?operation=$globals.getOP('OP_QUERY')";
					window.location= url;
			}
			
			//删除单个
			function del(fileName,index){
				
				 asyncbox.confirm('确定删除该文件吗?','$text.get("clueTo")',function(action){
				　　　if(action == 'ok'){
			 	     	var url1="/NetDiskQueryAction.do?operation=$globals.getOP('OP_DELETE')";
					 	var path = encodeURIComponent("$pId");
					 	var pageNo = $("#pageNo").val();
						var pageSize = $("#pageSize").val();
						var  key = form.key.value;
						var sortType = form.sortType.value;
						
					 	jQuery.ajax({
								   type: "POST",
								   url: url1,
								   data: "fileName="+encodeURIComponent(fileName)+"&path="+path,
								   async:true,
								   success: function(msg){
							  		    var ifre = window.parent.document.getElementById("firameMain");
							  		    var nodeId = "$pId";
							  		    index=index-1;
							  		    ifre.src =  "/NetDiskQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(nodeId)+"&pageNoURL="+pageNo+"&pageSizeURL="+pageSize+"&keyURL="+key+"&sortTypeURL="+sortType+"&index="+index;
							  	   },
								   error:function(){
								  	 	alert("error");
								   }
						});
				　　　}
　				});
			}
			//批量删除
			function multinomialDel(itemName,formObj){
				formObj = document.getElementsByName(formObj)[0];
				if(delHasChild(itemName)){
					asyncbox.alert('$text.get("common.exist.childcategory")!','$text.get("clueTo")');
					return false;
				}
				
				if(!isChecked(itemName)){
					asyncbox.alert('$text.get("common.msg.mustSelectOne")!','$text.get("clueTo")');
					return false;
				}
				
				asyncbox.confirm('确定删除所有文件吗?','$text.get("clueTo")',function(action){
				　　　if(action == 'ok'){
				 		var phos = $(":checkbox:checked");
			 	     	var phostr = "";
			 	     	for (var i = 0;i<phos.length;i++) {
			 	     		var p = phos[i];
			 	     		var v = $(p).val();
			 	     		phostr += v+",";
			 	     	}
			 	     	phostr = phostr.substr(0,phostr.length-1);
						var url1="/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_DELETE')";
					 	var path = encodeURIComponent("$pId");
					 	
					 	var pageNo = $("#pageNo").val();
						var pageSize = $("#pageSize").val();
						var  key = form.key.value;
						var sortType = form.sortType.value;
						
					 	jQuery.ajax({
								   type: "POST",
								   url: url1,
								   data: "pho="+encodeURIComponent(phostr)+"&path="+path,
								   async:true,
								   success: function(msg){
							  		    var ifre = window.parent.document.getElementById("firameMain");
							  		    var nodeId = "$pId";
							  		    ifre.src =  "/NetDiskQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(nodeId)+"&pageNoURL="+pageNo+"&pageSizeURL="+pageSize+"&keyURL="+key+"&sortTypeURL="+sortType;
							  	     	//$(ifre).attr("src","/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(nodeId));
								   },
								   error:function(){
								  	 	alert("error");
								   }
						});
				　　　}
				　　　if(action == 'cancel'){
				　　　　　
				　　　}
				　　　if(action == 'close'){
				　　　　
				　　　}
　				});
			}
			
			function downLoad(fileName){
				var a = document.getElementById(fileName);
				var path = encodeURIComponent("$pId");
				var url = "\ReadFile?tempFile=path&albumType=tree&path="+path+"&fileName="+encodeURIComponent(fileName);
				a.href=url;
			}
			
		function dingWei(){
			var newsId="$!index";
			if(newsId!= ''){
				window.location.href="#"+newsId;
			}
		}	
	</script>
	</head>
	<body onload="dingWei();">
		<form action="/NetDiskSearch.do" name="form" method="post">
			<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')"/>
			<input type="hidden" name="requestType" value="ajax"/>
			<input type="hidden" name="parPath" value="$!pId"/>
			<input type="hidden" name="id" value="$!pId"/>
			<input type="hidden" name="dateId"value="$dateId"/>
<script type="text/javascript">
	$(function(){
		//var zz = screen.height
		var zz = $(document).height()
		//$("#outer").css("height",zz-230);
		$("#outer").css("height",zz-50);
		//$("#list_photo").css("height",zz-480);
		$("#list_photo").css("height",zz-308);
		//$("#no_file").css("height",zz-420).css("line-height",zz-420+"px");
		$("#no_file").css("height",zz-200).css("line-height",zz-200+"px");
	});
</script>
			<div style="overflow: hidden;width: 99%;;height: 510px;padding:0 0 0 1%;position:relative;" id="outer">
			<table cellpadding="0" cellspacing="0" class="frame2" style="margin:0;">
        <!-- 本JS获得滚动条拉伸的高度,用于图片预览时错位的问题, -->
        <script type="text/javascript"> 
          var outer_div = document.getElementById("outer"); 
          scrollValue(); 
        </script> 
				<tr>
					<td valign="top" class="list">
						<div class="PhotoAlbum_top" style="width: 98%">
							#if($query)
							<a href="/DirectorySettingNetDiskQueryAction.do?isRoot=1" class="root_a ti_01" id="dialog_link" >
							目录设置</a>
							#end
							<div>
								$text.get("order")
										<select  id="cc"  onchange="toChangeOrder(this)"  name="key" style="width:100px;" required="true">
												<option  value="3" #if("$!key" == 3) selected #end>按名称排序</option>
												<option value="1" #if("$!key" == 1) selected #end>按修改时间</option>
												<option value="2" #if("$!key" == 2) selected #end>按大小排序</option>
										</select>
										<select onchange="toChangeOrder(this)" name="sortType" style="width:70px;" required="true">
												<option value="1" #if("$!sortType" == 1) selected #end>升序</option>
												<option value="2" #if("$!sortType" == 2) selected #end>降序</option>
										</select>
							</div>
							
						</div>
						<ul class="ABK_Add2 photoAlbumCheck" style="width: 98%;border:0px;">
								<ol style="text-align: left;height: 30px;border:1px solid #D5D5D5;" ><span>$text.get("oa.common.currentStation")：</span><span id = "imgsPath">$!viewPath</span></ol>
								#if($files.size()<=0)
										<div id="no_file" style="border:1px solid #D5D5D5;border-top:none;">
											<span style="color: orange;margin:0 0 0 40%;">当前目录还没有文件！</span>	<a style="cursor:hand;color: blue;" onclick="openAttachUpload('$!pId','ATT','uploadPho')" > 上传文件</a>	
										</div>
								#else
							
								<table cellpadding="0" cellspacing="0" class="frame2" style="margin-top: 0px;width: 100%" border="0">
									<tr>
										<td valign="top" class="list3">
											
												<table cellpadding="0" cellspacing="0" style="border:0px solid red;">
													<thead>
														<tr>
														#if($upd || $add ||$del || $onDown)
															<td style="background:#F9F9F9;" width="7%">&nbsp;</td>
														#end
															<td width="20%">文件名</td>
															<td width="20%">修改日期</td>
															<td width="20%">大小(KB)</td>
															<td width="15%">类型</td>
															#if($upd || $add ||$del || $onDown)
															<td>操作</td>
															#end
														</tr>
													</thead>
								</table>
								<div id="list_photo" style="margin-top: 0px;;overflow-y:auto;height: 280px;width:99%; padding:0 0 0 1%;">
								<table cellpadding="0" cellspacing="0" class="frame2" style="margin-top: 0px; border:0px solid red;">	
													#set($index=1)
													#foreach ($d in $files)
													<tr onMouseMove="setBackground(this,true);"
														onMouseOut="setBackground(this,false);"> 
														
														#if($upd || $add ||$del || $onDown)
														<td width="8%"><input name="pho" type="checkbox" value="$!d.tempName" /></td>
														#end
														<td style="text-align: left;"  width="20%">
														 		<img src="$!d.fileIcon" border="0" align="middle"/> 
														 		<a id="$!d.tempName" onmouseover="showOp(this.id);" #if($onDown) style="cursor:pointer;"   onclick="downLoad('$!d.tempName')" #end target=_blank>
															 		<span class="tempName" title="$!d.tempName" >$!d.tempName</span>
															 	</a>&nbsp;
														     	<div  id = '$!d.tempName _menu' class="attach_div" style="text-align:center;padding:5px;width:130px;height:100px;font-size:80px;margin-left:30px;">
														     	
														     	</div>										
														</td>
														<td width="20%">$!d.lastUpdateTime</td>
														<td align="center" width="20%">
															#set($fileSize=$!d.FileSize/1024)
															#set($fileLast=$!d.FileSize%1024)
															$fileSize.$fileLast
														</td>
														<td width="15%" align="center">$!d.fileDesc</td>
														<td>
															<a name="$!index"></a>
															#if($onDown)<a style="cursor:pointer;color: #000000;padding-left:15px;height:22px; line-height:22px;text-decoration:none; padding:2px 0px 0px 18px;" href="/ReadFile?tempFile=path&albumType=tree&path=$pId&fileName=$globals.encode($!d.tempName)" class = "ti_06" > $text.get("common.lb.download")</a>
															#end
															#if($del)
															<a style="cursor:pointer;color: #000000;" onclick="del('$!d.tempName','$!index')"><img src="style/plan/M_2.gif"/> 删除</a>
															#end
														
														</td>
													</tr>
													#set($index=$index+1)
													#end
													</div>
												</table>
											</td>
										</tr>
									</table>
							#end
						</ul>
						#if($upd || $add ||$del || $onDown)
						<ul class="ABK_Add2 photoAlbumCheck_list" style="margin-top: 1px;width: 98%;border:0px solid #CCCCCC">
							
							<ol style="height: 30px;line-height:30px;border:1px solid #CCCCCC;">
								<a>文件操作:</a>
								<span style="margin-left: -40px;">
								<a><input type="checkbox"  id = "checkAll" onclick="isCheckAll(this.value,'pho')"  value="true" />$text.get("checkAllOfPage") 
								</a>
								#if($onDown)
								<a style="cursor:pointer;" id = "downLoadId" onclick="toUploadPhos('pho','form')" class = "ti_06">$text.get("oa.common.download")</a>
								#end
								#if($del)
								<a  style="cursor:pointer;margin-left: 0.1%;" onclick="multinomialDel('pho','form')" ><img src="style/plan/M_2.gif"/> $text.get("delete")</a>
								#end
								#if($upd || $add)
								<a style="cursor:pointer;" onclick="openAttachUpload('$!pId','ATT','uploadPho')" class="ti_08"> 上传文件</a>	
								#end
								</span>
							</ol>
							#if($!add || $!upd ||$!del)	
							<ol style="height: 30px;line-height:30px;border:1px solid #CCCCCC;margin:5px 0 0 0;">	
								<a>文件夹操作:</a>
								<span style="margin-left: -40px;">
								#if($add)
								<a id="add" title="新建文件夹" style="cursor:pointer;margin-left: 0.1%;" onfocus="this.blur();" onclick="addNode();" ><img src="style/plan/M_3.gif" /> 新建文件夹</a>
								#end
								#if($upd)
									<a  title="修改文件夹" style="cursor:pointer;margin-left: 0.1%;" onfocus="this.blur();" onclick="updateNode();" ><img src="style/plan/M_1.gif" /> 修改文件夹</a>
								#end
								#if($del)
									<a style="cursor:pointer;margin-left: 0.1%;"  onfocus="this.blur();" title="删除文件夹" onclick="javascript:parent.removeTreeNode('$pId');"><img src="style/plan/M_2.gif"/> 删除文件夹</a>
								#end
								</span>
							</ol>
							#end
							
						</ul>
						#end
						
						<ul>
								#if($files.size()>0)
									<div class="listRange_pagebar" style="width:auto;padding:0 15px 0 0;">
										$!pageBar
									</div>
								#end
								
						</ul>
					</td>
					
				</tr>
			</table>
						
			
			 	<script>
					function openDialog(type,srcNode){
						$('#dialog').dialog({
							buttons:[{
								text:'确定',
								iconCls:'icon-ok',
								handler:function(){
									var path = "$pId";
									var name = $("#folder").val();
									if(name == null || name.length <=0){
										
										asyncbox.alert('$text.get("name")$text.get("isNotNull")','$text.get("clueTo")');
										return false;
									}	 
									if(getStringLength(name)>50){
										asyncbox.alert("$text.get('name')"+"$text.get('oa.common.not.more.than')"+50+"$text.get('oa.common.word.countOfCharacter')",'$text.get("clueTo")');
										return false ;
									}
									var bool1 = isHaveSpecilizeChar2(name);
									if (bool1==false) {
									 	asyncbox.alert("$text.get('name')$text.get('contain.specilize2')",'$text.get("clueTo")');
										return false;
									}
									
									if (type == "add") {
										window.parent.addNode(path,name);
								 	} else {
								 		window.parent.updateNode(path,name,srcNode);
								 	}
									$("#folder").val("");
								 	$('#dialog').dialog('close');
								}
							},{
								text:'取消',
								handler:function(){
									$("#folder").val("");
									$('#dialog').dialog('close');
								}
							}]
						});
					}
					function close1(){
						$('#dialog').dialog('close');
						
					}
				</script>
				
				<script>
					function openAsyncBox(type,srcNode,title,html){
						　//url : '/vm/oa/album/albumTree/template.html',
						　asyncbox.open({
						　　　html : html,
							 width : 350,
						　　　height : 200,
						　　　tipsbar : {
						　　　　　title : '请注意：',
						　　　　　content : "$text.get('name')$text.get('isNotNull'),$text.get('contain.specilize')!"
						　　　},
							 title : title,
						　　　btnsbar : jQuery.btn.OKCANCEL,
						　　　callback : function(action,opener){
							 	if(type=="add"){
							 	    document.getElementById("zhezhao").style.display="none";
							 	}
						　　　　　if(action == 'ok'){
						　　　　　　　//在回调函数中 this.id 可以得到该窗口 ID。




									var path = "$pId";
									var name = document.getElementById("folder").value;
									if(name == null || name.length <=0){
										asyncbox.alert("$text.get('name')$text.get('isNotNull')",'$text.get("clueTo")');
										return false;
									}	 
									if(getStringLength(name)>50){
										asyncbox.alert("$text.get('name')"+"$text.get('oa.common.not.more.than')"+50+"$text.get('oa.common.word.countOfCharacter')",'$text.get("clueTo")');
										return false ;
									}
									var bool1 = isHaveSpecilizeChar2(name);
									if (bool1==false) {
										asyncbox.alert("$text.get('name')$text.get('contain.specilize2')",'$text.get("clueTo")');
										return false;
									}
									if (type == "add") {
										window.parent.addNode(path,name);
								 	} else {
								 		window.parent.updateNode(path,name,srcNode);
								 	}
									$("#folder").val("");

						　　　　　}
						　　　　　if(action == 'cancel'){
						　　　　　　$("#folder").val("");
						　　　　　}
						　　　　　if(action == 'close'){
						　　　　　　　$("#folder").val("");
						　　　　　}
						　　　}
						　});
					}
				</script>
				
				<script>
				
					 function addNode(){
					 	//$("#dialog").attr("title","新建文件夹");
					 	//openDialog("add");
					 	var srcNode=window.parent.zTree1.getSelectedNode();
					 	if(srcNode==null){
					 		asyncbox.alert('请选择你要添加的根目录！','$text.get("clueTo")');
					 		return false;
					 	}
					 	var html = '<div style=\"margin-top: 0px;\">&nbsp;&nbsp;文件夹名： <input id=\"folder\" name=\"folder\"  /></div>';
						openAsyncBox("add","",'新建文件夹',html); 
						document.getElementById("zhezhao").style.display="block";
					 }
					 
					 function updateNode(){
					 	//$("#dialog").attr("title","修改文件夹");
					 	var srcNode = window.parent.zTree1.getSelectedNode();
					 	if(srcNode==null){
					 		asyncbox.alert('请选择你要修改的子目录！','$text.get("clueTo")');
					 		return false;
					 	}
					 	var isParent = srcNode.icon;
					 	if (isParent == "1") {
					 		asyncbox.alert('请在硬盘目录设置里面修改该根目录！','$text.get("clueTo")');
					 		return false;
					 	}
					 	var html = '<div style=\"margin-top: 0px;\">文件夹名： <input id=\"folder\" name=\"folder\" value = "'+srcNode.name+'" /></div>';
						openAsyncBox("update",srcNode,'修改文件夹',html);  
					 	
					 	//$("#folder").attr("value",srcNode.name);
					 	//openDialog("update",srcNode);
					 }
					 
					 function checkFolder(obj){
				 		name = obj.value;
						var bool1 = isHaveSpecilizeChar2(name);
						if (bool1==false) {
						 	alert("$text.get('name')$text.get('contain.specilize2')");							 	
							obj.focus();
							
						}
					 }
					 
					 function toUploadPhos(itemName,formObj){
						formObj = document.getElementsByName(formObj)[0];
						if(delHasChild(itemName)){
							asyncbox.alert('$text.get("common.exist.childcategory")!','$text.get("clueTo")');
							return false;
						}
						if(!isChecked(itemName)){
							asyncbox.alert('$text.get("common.msg.mustSelectOne")!','$text.get("clueTo")');
							return false;
						}
			
						var phos = $(":checkbox:checked");
			 	     	var phosLen = phos.length;	
			 	     	var isCheckAll = $("#checkAll").attr("checked");
			 	     	
			 	     	var notCheck = $(":checkbox:not(:checked)");
			 	     	var notCheckLen = notCheck.length;
			 	     	
			 	     	
			 	     	var allCheckBox = $(":checkbox");//所有的checkBox元素
			 	     	var allLen = allCheckBox.length;
			 	     	
			 	     	if (isCheckAll == 'checked') {
			 	     		//全选被选中了









			 	     		phosLen = phosLen-1;
			 	     	} else {
			 	     		notCheckLen = notCheckLen-1;
			 	     	}
			 	     
			 	    	allLen = allLen -1;
			 	     	var phostr = "";
		 	     		var index = "$pId".lastIndexOf("\\");
		 	     		var downName = "$pId".substr(index+1);
			 	     	//是否全部被选中
			 	     	if (phosLen == allLen && notCheckLen == 0 ) {
			 	     		//全部下载当前文件夹









			 	     		var url1="/PublicServlet?operation=downLoadFiles&downName="+encodeURIComponent(downName)+"&path=$pId";
			 	     		$("#downLoadId").attr("href",url1);
			 	     		return false;
			 	     	} else {
			 	     		var isNeedDebar = false;//后台是否需要排除包含该照片列表
			 	     		//判断 选中的多 还是未选中的多些   暂时不适合这样做的 该想法很不错 适合不分页的时候 现在分页了如果还这样做 将导致复杂化
			 	     		/*
			 	     		if (phosLen >= notCheckLen) {
			 	     			isNeedDebar = true;
			 	     			//传未选中的值









					 	     	for (var i = 0; i < notCheckLen; i++) {
					 	     		var p = notCheck[i];
					 	     		var v = $(p).val();
					 	     		phostr += v+",";
					 	     		
					 	     	}
			 	     		} else {*/
			 	     			isNeedDebar = false;
			 	     			//传选中的值









			 	     			for (var i = 0;i < phosLen; i++) {
					 	     		var p = phos[i];
					 	     		var v = $(p).val();
					 	     		phostr += v+",";
			 	     			}	
			 	     		/*}*/
			 	     	
			 	     		phostr = phostr.substr(0,phostr.length-1);
						
			 	     		var url1="/PublicServlet?operation=downLoadFiles&downName="+encodeURIComponent(downName)+"&path=$pId&pho="+encodeURIComponent(phostr)+"&isNeedDebar="+isNeedDebar;
			 	     		
			 	     		var maxLen = 2083;
			 	     		var bro = jQuery.browser;
							var binfo="";
							if(bro.msie) {
								binfo=bro.version;
								if (binfo=='7.0' || binfo=='6.0') {
									maxLen = 2083;
								} else if (binfo=='8.0'||binfo=='9.0') {
									maxLen = 4098;
								}  else {
									maxLen = 4098;
								}
								
							} else if(bro.mozilla) {
								maxLen = 8192;// 对于Firefox浏览器URL的长度限制为65,536个字符， apache服务器，最大处理能力为8192个字符









							} else if(bro.safari) {
							   	//binfo="Apple Safari "+bro.version;
							    maxLen = 8192; //最大80,000
							}else if(bro.opera) {
								maxLen = 8192; // URL最大长度限制为190,000个字符。









							}
						
							//var index = pathStr.lastIndexOf("\\");
			 	     		//pathStr = pathStr.substring(0,index);
			 	     		
			 	     		if(getStringLength(url1) > maxLen){
							　   asyncbox.confirm('您选择的下载量过多，有可能下载失败!','$text.get("clueTo")',function(action){
								　　　//confirm 返回三个 action 值，分别是 'ok'、'cancel' 和 'close'。









								　　　if(action == 'ok'){
								　　　　　$("#downLoadId").attr("href",url1);
								　　　}
								　　　if(action == 'cancel'){
								　　　　　
								　　　}
								　　　if(action == 'close'){
								　　　　　
								　　　}
							　  });
							} else {
								$("#downLoadId").attr("href",url1);
							}
			 	     		
			 	     	}
					 }
				</script>
				<!-- 
				<div style="display:none">
					<div id="dialog" icon="icon-save" title="新建文件夹" style="padding:3px;width:300px;height:150px;">
						<div style="margin-top: 20px;">文件夹名： <input id="folder" name="folder"  /></div>
					</div>
					<div id="dlg-buttons">
						<a style="cursor:hand;color:#BACAE6" class="easyui-linkbutton" onclick="javascript:alert('save')">Save</a>
						<a style="cursor:hand;color:#BACAE6" class="easyui-linkbutton" onclick="javascript:$('#d2').dialog('close')">Close</a>
					</div>
				</div>
				 -->
				<div class="clear"></div>
				
				</div>
				<div id="zhezhao" style="position:absolute;top:0px;left:0px; opacity:0.3;filter:alpha(opacity=30);background:#999999; width:100%;height:600px;display:none;"> 
						 
				</div>
		</form>
		
	</body>
</html>

