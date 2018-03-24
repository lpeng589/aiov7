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
				
				disabledAll();
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
							 var bool1 = isHaveSpecilizeChar(albumName);
							 if (bool1==false) {
							 	alert("$text.get('name')$text.get('contain.specilize')");
							 	return false;
							 }
							 var bool2 = isHaveSpecilizeChar(albumDesc);
							 if (bool2==false) {
							 	alert("$text.get('desc')$text.get('contain.specilize')");
							 	return false;
							 }
							 //albumName = encodiURILocal(albumName);
							 albumName = encodeURIComponent(albumName);
							// albumDesc = encodiURILocal(albumDesc);
							 albumDesc = encodeURIComponent(albumDesc);
							 form.action = "/AlbumQueryAction.do?operation=1&currentAlbum="+albumName+"&albumName="+albumName+"&albumDesc="+albumDesc+"&orderType=$!orderType";
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
			
			//给其他的文本添加 统一的信息

			function addInputVal (obj,type) {
				var v = obj.value;
				if (type == 'name') {
					//获得页面的所有的 name
					$(".tempName").attr("value", v);
				}else if (type == 'desc') {
					$(".phoneDesc").attr("value",v);
				} else {
					alert("需要修改的统一信息不明！");
				}
			}
			
			//清空
			function cleanInput(){
				
				$(".oneName").attr("value", "");
			
				$(".oneDesc").attr("value","");
				
			}
			
			//让所有的方法二中的不可编辑

			function disabledAll() {
				//让所有的方法二中不能编辑
				//$(".operationDiv input").attr("disabled","disabled").css({"color":"#ccc"});
			}
			
		
			
			function showOperate(obj){
				//$(obj).parent().parent().next().children(".operationDiv input:enabled");
				//(".operationDiv input").attr("disabled","disabled").css({"color":"#ccc"});
				//alert($(obj).parent().parent().next().children(".operationDiv").eq(0).children(".inputs").html());
				//$(obj).parent().parent().next().children(".operationDiv").eq(0).children(".inputs").attr("disabled","ss");
				//alert($(obj).parent().parent().next().children(".operationDiv").eq(0).children().attr("disabled","ss").html());
				
			}
			function check() {
				/*
				var covers = $(":radio:checked");
				if(covers.length<=0){
					alert("请先选择封面");
					return false;
				}
				return true;
				*/
				var names = $(".tempName");
				var descs = $(".phoneDesc");
				
				
				//判断是否超出规定字符大小
				for (var i = 0;i < names.length; i++) {
					var v = $(names[i]).val();
					 var bool1 = isHaveSpecilizeChar(v);
					 if (bool1==false) {
					 	alert("$text.get('name')$text.get('contain.specilize')");
					 	return false;
					 }
					if(v == null || v.length<=0) {
						alert("$text.get('name')$text.get('isNotNull')"); 
						return false;
					}
					if(getStringLength(v)>50){
						alert("$text.get('name')"+"$text.get('oa.common.not.more.than')"+50+"$text.get('oa.common.word.countOfCharacter')");
						return false ;
					}
				
				}
				
				for (var i = 0;i < descs.length; i++) {
					var v = $(descs[i]).val();
					 var bool2 = isHaveSpecilizeChar(v);
					 if (bool2==false) {
					 	alert("$text.get('desc')$text.get('contain.specilize')");
					 	return false;
					 	
					    
					 }
					if(getStringLength(v)>200){
						alert("$text.get('desc')"+"$text.get('oa.common.not.more.than')"+200+"$text.get('oa.common.word.countOfCharacter')");
						return false ;
					}
					
				}
				return true;
			
			}
			function forBack(){
				var url = "/PhotoAction.do?operation=$globals.getOP('OP_QUERY')&albumSelectId=$!albumSelectId&orderType=$!orderType";
				window.location= url;
			}
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
			
		</script>
	</head>
	<body >
		<form action="/PhotoAction.do" name="form" method="post" onsubmit="return check()">
			<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")"/>
			<input type ="hidden" name = "albumSelectId" value = "$!albumSelectId"/>
			<input type="hidden" name = "type_upd" value = "uploadEnd"/>
			<input type="hidden" name="orderType" value = "$!orderType"/>
			<div class="Heading">
				<div class="HeadingIcon">
					<img src="/$globals.getStylePath()/images/Left/Icon_1.gif">
				</div>
				<div class="HeadingTitle">
					$!text.get("update.photo.information")
				</div>
				<ul class="HeadingButton">
					<li><!-- 
						<button type="button" onClick="closeWin();" class="b2">
							$text.get("common.lb.close")
						</button>-->
						<button type="button" onClick="forBack()" class="b2">
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
								<a name="HTML_top">$text.get("oa.common.currentStation")：$!text.get("update.photo.information")</a>
							</td>
						</tr>
					</thead>
				 </table>
				<div class="scroll_function_small_bbs"
					style="width: 800px;margin-left: auto;margin-right: auto;">
					<table cellpadding="0" cellspacing="0" class="oabbs_function_index" width="100%">
					   <tr>
						  <td colspan="2">
								<div align="left" style="margin-left: 10px;margin-bottom: 15px;">
									<ul>
										<li style="float: left;">	$!text.get("uploadSuccess")<span style="color:green;font-size: 13px;font-weight: bold">$picList.size()</span>$text.get("sheet")$text.get('photo')
										</li>
									</ul>
								</div>
								<div  style="margin-left: 10px;margin-top: 60px;">
									<ul>
									<li>	
										<span>$!text.get("otherInfo")</span> <span style="margin-left: 300px;">
											<button type="submit" name="Submit" class="b3">
												$!text.get("common.lb.save")
											</button><!-- $!text.get("or") &nbsp;&nbsp; -->
										<button type="button" onclick="forBack()" class="b3">$!text.get("backAlbum")</button></span>
									</li>
									</ul>
								</div>
							 </td>
							</tr>
							<tbody id="showReplyList_advice" >
							<tr>
								<td colspan="2">$text.get('way.one')</td>
							</tr>
								<tr>
								<td colspan="2"> 
									<div>
										<div> 
											$text.get('name')：<input name = "tempName" class = "oneName" value = "" style="width: 220px;" onblur="disabledAll()"  onkeyup="addInputVal(this,'name')" onkeypress ="addInputVal(this,'name')"></input>
										</div>
										<div>
											$text.get('desc')：<textarea name = "phoneDesc" class = "oneDesc"  rows="3" style="width: 220px;" onblur="disabledAll()" onkeyup="addInputVal(this,'desc')" onkeypress="addInputVal(this,'desc')"></textarea>
											
										</div>
									</div>
								</td>
							</tr>
							
							
							<tr >
								<td colspan="2" style="margin-top: 40px;">$text.get('way.two')</td>
							</tr>
							#foreach ($pho in $picList)
							<tr>
								<td>
										<div style="float: left;">
											<div  > 
												<img src="/ReadFile?tempFile=path&path=/album/img/small/&fileName=$globals.urlEncode($pho.beginName)" name="rephoto"  width="140px;" height="100px;"   border="0">
											</div>
											<div>
												<span style="color: green;">$pho.tempName</span>
												<input type="hidden" name="phoId" value="$!pho.id">
											</div>
											<div >
												<input type="radio" name = "isCover" value="$!{pho.id}" #if("$pho.isCover"=="1") checked="checked" #end>$text.get("cover") &nbsp;&nbsp;
												<!-- <a  onclick = "showOperate(this)">编辑</a> -->
										    </div>
										</div>
										<div class="operationDiv" style="margin-left:400px;margin-top: 10px;">
											<div> 
												$text.get('name')：<input  style="width: 220px;" type="text" class = "tempName" name = "$!{pho.id}tempName" value = "$!pho.tempName" onkeydown="cleanInput()" onkeypress="cleanInput()" ></input>
											</div>
											<div style="margin-top: 5px;">
												$text.get('desc')：<textarea rows="3" style="width: 220px;" class = "phoneDesc" name = "$!{pho.id}phoneDesc"   onkeydown="cleanInput()" onkeypress="cleanInput()" >$!pho.phoneDesc</textarea>
											</div>
										</div>
								</td>
							</tr>
							#end
						</tbody>
					</table>
				</div>
				</div></div>
		<div class="clear"></div>
		</form>
	</body>
</html>

