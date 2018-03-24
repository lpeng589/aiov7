<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("oa.advice.readAdvice")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>		
<link type="text/css" rel="stylesheet" href="/style/css/sharingStyle.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script type="text/javascript" src="/js/jquery.easyui.min.js"></script>
		
	

<script> 
	$(function(){
		$(".photoList_title").each(function(i){
			if (getStringLength($(this).text()) > 13){
				$(this).text(substr($(this).text(),13)+"...");
			}
		 });
	});
	if(typeof(top.jblockUI)!="undefined")top.junblockUI();
    function open_pic(pic_id,sub_dir,file_name){
	   var pageNo = $("#pageNo").val();
	   var pageSize = $("#pageSize").val();
	   var  key = form.key.value;
	   var sortType = form.sortType.value;
	   URL = "/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&path="+encodeURIComponent("$pId")+"&requestType=showPic&tempName="+encodeURIComponent(file_name)+"&pageNoURL="+pageNo+"&pageSizeURL="+pageSize+"&keyURL="+key+"&sortTypeURL="+sortType+"&onDown="+$!onDown;
	   window.open(URL,"图片浏览","toolbar=0,status=0,menubar=0,scrollbars=no,resizable=1,width="+(screen.availWidth-10)+",height="+(screen.availHeight-100)+",top=0,left=0");
	}

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
		 	toSubmitByOK();
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
				
	$(function(){
		var path = "$viewPath";
		
		path = path.replace("\\\\","\\");
		
		//path = path.replace("/&/g","\&");
		
		$("#imgsPath").html(path);
		$(".photoList_title").each(function(i){
			$(this).text(substr($(this).text(),20));
		});
	});
			
		
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
				
		asyncbox.confirm('$text.get('confirm.del.pho')','$text.get("clueTo")',function(action){
				　　　//confirm 返回三个 action 值，分别是 'ok'、'cancel' 和 'close'。  可以直接用 submit();因为当前页面已经刷新了 无需ajax
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
							  		    var ifre = window.parent.document.getElementById("newIframe");
							  		    var nodeId = "$pId";
							  		    ifre.src =  "/AlbumTreeQueryAction.do?operation=$globals.getOP('OP_QUERY')&requestType=ajax&id="+encodeURIComponent(nodeId)+"&pageNoURL="+pageNo+"&pageSizeURL="+pageSize+"&keyURL="+key+"&sortTypeURL="+sortType;
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
</script>
<style type="text/css">
body{
	scrollbar-face-color:  #DEE3E7;  
	scrollbar-highlight-color:  #FFFFFF;  
	scrollbar-shadow-color:  #DEE3E7;  
	scrollbar-3dlight-color:  #D1D7DC;  
	scrollbar-arrow-color:    #006699;  
	scrollbar-track-color:  #EFEFEF;  
	scrollbar-darkshadow-color:  #98AAB1;	
}
</style>
</head>
	<body >
		<form action="/AlbumTreeSearch.do" name="form" method="post">
			<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')"/>
			<input type="hidden" name="requestType" value="ajax"/>
			<input type="hidden" name="parPath" value="$!pId"/>
			<input type="hidden" name="id" value="$!pId"/>
      <script type="text/javascript">
				$(function(){
					
					//var zz = screen.height
					var zz = $(document).height()

					$("#w_div").css("height",zz-50);
					$("#List_photo").css("height",zz-270);
					$("#no_file").css("height",zz-200).css("line-height",zz-200+"px");
				});
			</script>
			<div id="w_div" style="overflow:hidden;width:99%;height:500px;border:0px;padding:0 0 0 1%;">
			<table cellpadding="0" cellspacing="0" class="frame2" >
				<tr>
					<td valign="top" class="list">
						<div class="PhotoAlbum_top">
							#if($query)
							<a href="/DirectorySettingAlbumQueryAction.do?isRoot=0" class="root_a ti_01" id="dialog_link" >目录设置</a>
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
							<ul>
								
							</ul>
						</div>
						<ul class="ABK_Add2 photoAlbumCheck"  style="width:98%;">
							<ol style="text-align: left;height:30px;"><span>&nbsp; $text.get("oa.common.currentStation")：</span><span id = "imgsPath">$!viewPath</span></ol>
								#if($photos.size()<=0)
									<div id="no_file" style="border:1px solid #D5D5D5;border-top:none;">
										<span style="color: orange;margin:0 0 0 40%;">$text.get("album.no.photo")！</span>	<a style="cursor:hand;color: blue;" onclick="openAttachUpload('$!pId','PIC','uploadPho')" > #if($upd || $add) $text.get("upload.photo") #end</a>	
									</div>
								#else
           
							<div id="List_photo" style="margin-top: 0px;overflow-y:auto;width:100%;height: 250px;" onContextMenu="return false" onSelectStart="return false">
              	
									#foreach ($p in $photos)
											<li  id="showPics" style="margin:10px; margin-left:20px;">
												<a style="cursor:hand;color:#BACAE6" class="photoList_bg"  onclick="javascript:open_pic('4','','$globals.encode($!p.tempName)');" >
													<img  src="/ReadFile?tempFile=path&path=$p.path\small&fileName=$globals.encode($!p.tempName)&albumType=tree" title="名称：$!p.tempName
大小：$!p.FileSize KB  
最后修改时间：$!p.lastUpdateTime"  name="rephoto"  width="125px" height="90px" border="1"/>
												</a>
												 #if($upd || $add ||$del || $onDown)
													<span><input type="checkbox" class ="phoClass"  name="pho" value="$!p.tempName" /></span>
												 #end
												<a  class="photoList_title" >$!p.tempName</a>
											</li>
									#end
							</div>
								#end
						</ul>
						#if($upd || $add ||$del || $onDown)
							<ul class="ABK_Add2 photoAlbumCheck_list" style="width:98%;margin-top:-5px;border:0px;">
								<!--<li style="padding:0 0 0 0;overflow:hidden;">-->
									<ol style="border:1px solid #CCCCCC;height:30px;">
									 
										<a>文件操作:</a>
										<span style="margin-left: -40px;">
											<a><input type="checkbox"  id = "checkAll" onclick="isCheckAll(this.value,'pho')"  value="true" />$text.get("checkAllOfPage")</a>
											#if($onDown)
											<a style="cursor:pointer;" id = "downLoadId" onclick="toUploadPhos('pho','form')" class = "ti_06">$text.get("oa.common.download")</a>
											#end
											#if($del)
											<a  style="cursor:pointer;margin-left: 0.1%;" onclick="multinomialDel('pho','form')" ><img src="style/plan/M_2.gif"/> $text.get("delete")</a>
											#end
											#if($upd || $add)
											<a style="cursor:pointer;" onclick="openAttachUpload('$!pId','PIC','uploadPho')" class="ti_08"> $text.get("upload.photo")</a>	
											#end
										</span>
									</ol>
									#if($!add || $!upd ||$!del)		
									<ol style="border:1px solid #CCCCCC;height:30px;margin:5px 0 0 0;">
										<a>文件夹操作:</a>
										<span style="margin-left: -40px;">
										#if($add)
										<a  title="新建文件夹" style="cursor:pointer;margin-left: 0.1%;" onfocus="this.blur();" onclick="addNode();" ><img src="style/plan/M_3.gif" /> 新建文件夹</a>
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
								<!--</li>-->
							</ul>
						#end
            #if($photos.size()>0)
            <div class="listRange_pagebar" style="width:auto;padding:0 15px 0 0;">
              $!pageBar
            </div>
            #end
					</td>
				</tr>
			</table>
      
		<div style="margin-top: 80px;"></div>		
		<div class="clear"></div>
</div>
</form>
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

	function openAsyncBox(type,srcNode,title,html){
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
				 	$('#dialog').dialog('close');
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
				
	 function addNode(){
	 	var scrNode=window.parent.zTree1.getSelectedNode();
	 	if(scrNode==null){
	 		asyncbox.alert('  请选择你要添加的根目录！','$text.get("clueTo")');
	 		return false;
	 	}
		var html = '<div style=\"margin-top: 0px;\">&nbsp;&nbsp;文件夹名： <input id=\"folder\" name=\"folder\"  /></div>';
		openAsyncBox("add","",'新建文件夹',html); 
	 }
		
	 function updateNode(){
	 	//$("#dialog").attr("title","修改文件夹");
	 	var srcNode = window.parent.zTree1.getSelectedNode();
	 	if(srcNode==null){
	 		asyncbox.alert('  请选择你要修改的子目录！','$text.get("clueTo")');
	 		return false;
	 	}
	 	var isParent = srcNode.icon;
	 	if (isParent == "1") {
	 		asyncbox.alert('请在目录设置里面修改该根目录！','$text.get("clueTo")');
	 		return false;
	 	}
	 	//$("#folder").attr("value",srcNode.name);
		var html = '<div style=\"margin-top: 0px;\">文件夹名： <input id=\"folder\" name=\"folder\" value = "'+srcNode.name+'" /></div>';
		openAsyncBox("update",srcNode,'修改文件夹',html);  
		//openDialog("update",srcNode);
	 }
					 
	 function checkFolder(obj){
 		name = obj.value;
		var bool1 = isHaveSpecilizeChar2(name);
		if (bool1==false) {
			asyncbox.alert("$text.get('name')$text.get('contain.specilize2')",'$text.get("clueTo")');
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
     		var isNeedDebar = false;
     		//后台是否需要排除包含该照片列表
     		//判断 选中的多 还是未选中的多些






     		isNeedDebar = false;
     		//传选中的值






     		for (var i = 0;i < phosLen; i++) {
 	     		var p = phos[i];
 	     		var v = $(p).val();
 	     		phostr += v+",";
     		}
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
	     	if(getStringLength(url1) > maxLen){
				asyncbox.confirm('您选择的下载量过多，有可能下载失败!','$text.get("clueTo")',function(action){
					if(action == 'ok'){
					　$("#downLoadId").attr("href",url1);
					}
				});
			} else {
				$("#downLoadId").attr("href",url1);
			}
	   }
	 }
</script>
</body>
</html>

