<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>知识中心</title>
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css"/>
<link  type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript">
/*wyy*/
function addColletion(oTopicId){
	
	 var flag = $("#Konws_"+oTopicId).attr('bg');	 
	 var url = "/OACollectionAction.do?operation=2&attType="+flag+"&typeName=OAKnowCenter&keyId="+oTopicId;
	 jQuery.ajax({
	 	 type: "POST",
	  	 url: url,	
	  	 data:{
	  	 	urlparam:"/OAKnowCenter.do?operation=5&fileId="+oTopicId,
	  	 	titleName:"知识中心:$!file.fileTitle"
	  	 },	   
	  	 success: function(msg){
	  	 	if(flag == "add"){
	  	 		if(msg == "OK"){
		  	 		asyncbox.tips('收藏成功','success');
		  	 		$("#Konws_"+oTopicId).attr('bg','del');
		  	 		$("#Konws_"+oTopicId).html('取消收藏');		  	 		
	  			}else{
	  				asyncbox.tips('收藏失败','error');		    
	  			}
	  	 	}else{
	  	 		if(msg == "OK"){
		  	 		asyncbox.tips('取消收藏成功','success');
		  	 		$("#Konws_"+oTopicId).attr('bg','add');
		  	 		$("#Konws_"+oTopicId).html('加入收藏');
	  			}else{
	  				asyncbox.tips('取消收藏失败','error');		    
	  			}
	  	 	}	  	 	
	  	}
	});
}


//添加阅读痕迹

function addmark(){
	if("$!file.isSaveReading" == "1"){
		var str="/UtilServlet?operation=addOAReadingInfo&infoId=$!file.id&infoTable=OAKnowledgeCenterFile";
 	    AjaxRequest(str);
	}
}

function mark(){
	 asyncbox.open({
	 title : '阅读痕迹',
	　　　url : '/OAReadingRecordAction.do?readingInfoId=$!file.id&readingInfoTable=OAKnowledgeCenterFile',
	　　　cache : true,
	　　　width : 800,
	　　　height : 330
　	});
}
function del(){
	asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
	　　 if(action == 'ok'){
		document.location="/OAKnowCenter.do?operation=3&keyId=$!file.id&preId=$!preId";
		}
	});
}
function update(){
	document.location="/OAKnowledgeCenter.do?operation=7&position=detailpage&fileId=$!file.id&preId=$!preId";
}
function stow(){
	var favoriteName = encodeURI("$!file.fileTitle");
	var tabName = encodeURI("知识中心");
	var myCollectionURL = "$!myCollectionURL" ;
	var str="/UtilServlet?operation=addDedailAdviceInfoCollection&module=dedailAdviceInfo&favoriteName="+favoriteName+"&tabName="+tabName+"&favoriteURL=$!favoriteURL" + myCollectionURL;
	AjaxRequest(str);
	var value = response;
	if(value=="1"){
		asyncbox.tips('$text.get("oa.favorite.add.success")','success');
	}else if(value=="2"){
		asyncbox.tips('$text.get("oa.favorite.exist")','alert');
	}else{
		asyncbox.tips('$text.get("oa.favorite.add.failture")','error');
	}
}


function resizeImg(){
	scrwidth = (document.body.clientWidth) - 130;
	$("img").bind("click", function(){
  		window.open($(this).attr("src"));
	});
}


//新邀请阅读


function openInvite(){
	var title = encodeURI("“$text.get("oa.common.knowledgeCenter")”主题为【$!file.fileTitle】");
	var favoriteName = encodeURI("$globals.getEmpNameById($!LoginBean.id)$text.get("oa.knowledgeCenter.requestRead")$!file.fileTitle") ;
	var userinvite = encodeURI("$globals.getEmpNameById($!LoginBean.id)");
	var urls = "/AdviceAction.do?module=invitePre&id=$!file.id&userinvite="+userinvite+"&favoriteURL=$!favoriteURL&favoriteName="+favoriteName+"&title="+title;
	asyncbox.open({
		id : 'dealdiv',
	 	title : '邀请阅读',
	　　　 url : urls,
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

function nodown(){
	asyncbox.alert('没有下载权限!','$!text.get("clueTo")');
	return false;
}

function backs(){
	
	if(typeof(parent.frames["leftFrame"]) != "undefined"){
		window.location.href='/OAKnowCenter.do?type=oaKnowList&selectKnow=$!preId';
	}else{
		window.location.href="/OAKnowledgeCenter.do";
	}
}

</script>
<style type="text/css">
.SNews_title div span.cg_sp{float:left;display:inline-block;margin:0 0 0 8px;}
.SNews_title div span.xg_sp{float:left;display:inline-block;margin:0 8px 0 0;}
</style>
</head>
<body style="margin:auto;" onload="addmark();resizeImg();">
	<div class="bigTitle" style="background-image: none">
		<div class="new_button">
			
				<button type="button" class="bu_bg" onclick="mark()"><span style="color:black;">$text.get("oa.readingMark")</span></button>
				<button type="button" class="bu_bg" onclick="openInvite();"><span style="color:black;">$text.get("oa.common.invitereading")</span></button>
			#if("$!attention" == "OK")
				<button type="button" id="Konws_$!file.id" bg="del" onClick="addColletion('$!file.id');" class="bu_bg">
						取消收藏
				</button>
				#else
				<button type="button" id="Konws_$!file.id" bg="add" onClick="addColletion('$!file.id');" class="bu_bg">
						$text.get("aio.add.favorite")
				</button>
			#end
			#if("$!isEspecial" == "1")
				<button type="button" class="bu_bg" onclick="closeWin();"><span style="color:black;">$text.get("common.lb.close")</span></button>
			#elseif("$!isEspecial" != "2")
				<!-- <button type="button" class="bu_bg" onclick="openSelect('/UserFunctionAction.do?tableName=OAMessage&selectName=MsgReceive&operation=22')"><span style="color:black;">$text.get("oa.common.invitereading")</span></button> -->
				#if($LoginBean.operationMap.get("/OAKnowledgeCenter.do").update())
				<button type="button" class="bu_bg" onclick="update()"><span style="color:black;">$text.get("oa.folder.update")</span></button>
				#end
				#if($LoginBean.operationMap.get("/OAKnowledgeCenter.do").delete())
				<button type="button" class="bu_bg" onclick="del()"><span style="color:black;">$text.get("common.lb.del")</span></button>
				#end
			#else
				<!-- <button type="button" class="bu_bg" onclick="openSelect('/UserFunctionAction.do?tableName=OAMessage&selectName=MsgReceive&operation=22')"><span style="color:black;">$text.get("oa.common.invitereading")</span></button> -->
				<button type="button" class="bu_bg" onclick="stow()"><span style="color:black;">$text.get("aio.add.favorite")</span></button>
				<button type="button" class="bu_bg" onclick="closeWin();"><span style="color:black;">$text.get("common.lb.close")</span></button>
			#end
			#if("$!isEspecial" != "1"  && "$!isEspecial" != "2")
				<button type="button" class="fh" onclick="backs()" style="margin-top:0px;"></button>
			#end
		</div>
	</div>
	<div  id="nn" style="overflow:hidden;overflow-y:auto;width:100%;">
<script type="text/javascript">
	var oDiv=document.getElementById("nn");
	var sHeight=document.documentElement.clientHeight-55;
	oDiv.style.height=sHeight+"px";
</script>
	<div class="SNews" style="width:95%;">
		<div class="SNews_title" style="width:100%;">
			<h2 >$!file.fileTitle</h2>
			<div style="margin:0;display:inline-block;width:95%;">
				<div style="display:inline-block;margin:0 auto;width:auto;">
					<span class="cg_sp">创建人：</span><span class="xg_sp" style="color:olive;">$globals.getEmpFullNameByUserId($!file.createBy)</span>
					<span class="cg_sp">$text.get("common.lb.createTime")：</span><span class="xg_sp" style="color:olive;">$!file.createTime</span>
					<span class="cg_sp">$text.get("oa.common.lastUpdateTime")：</span><span class="xg_sp" style="color:olive;">$!file.lastUpdateTime</span>
					<span class="cg_sp">$text.get("or.ordain.gorup")：</span><span class="xg_sp" style="color:olive;">#foreach($!s in $!folder)$!s.folderName#end</span>
				</div>
			</div>
		</div>
		<div class="SNews_Co">
			$!file.description
		</div>
		<div align="left" style="margin-bottom:20px;margin-left: 20px;">
				<br />
				<br />
			<span style="margin-left: 60px;">
			$text.get("upload.lb.affix")：

			#if($!file.filePath.indexOf(";") > 0)
				#foreach ($str in $globals.strSplit($!file.filePath,';'))
						<img src="/$globals.getStylePath()/images/down.gif"/>
						
						#if($str.indexOf(".pdf")>-1 || $str.indexOf(".PDF")>-1)
							<a href='/ReadFile?tempFile=path&path=/affix/OAKnowledgeCenterFile/&fileName=$!globals.encode($!str)&onDown=$!onDown' target="_blank">$str</a>			
						#else
							#if("$!onDown" == "true")
								<a href='/ReadFile?tempFile=path&path=/affix/OAKnowledgeCenterFile/&fileName=$!globals.encode($!str)' target="_blank">$str</a>			
							#else
								<span onclick="nodown()" style="cursor: pointer;">$str</span>
							#end
						#end	
				#end
			#else
			暂无
 			#end</span><br/><br/><span style="margin-left: 30px;">
			<b>$text.get("oa.common.garget")</b>

			#if("$!file.isAlonePopedom"=="0")
				：$text.get("oa.common.target")</span><br />
			#else
					<br/><br/><span style="margin-left: 60px;">
					#if($targetUsers.size()>0)
					$text.get("oa.common.person")：

						#foreach($user in $targetUsers)
							$!user.EmpFullName 
						#end
					#end
					</span><br/><br/>
					<span style="margin-left: 60px;">
					#if($targetDept.size()>0)
					$text.get("mrp.lb.department")：

						#foreach($dept in $targetDept)
							$!dept.DeptFullName 
						#end
					#end
					</span><br/>
			#end
		</div><br/>
		<div class="SNews_bottom">
		#if("$!isEspecial" == "2")
		#elseif("$!isEspecial" == "1")
		#else
			#if($!lastKnow!=true)
			<p>
				<a href="/OAKnowCenter.do?operation=$globals.getOP("OP_DETAIL")&fileId=$!lastKnow.get("id")"><font color="black">$text.get("oa.common.backPiece")：</font>$!lastKnow.get("fileTitle")</a>
			</p>
			#end
			
			#if($!nextKnow!=true)
			<p>
				<a href="/OAKnowCenter.do?operation=$globals.getOP("OP_DETAIL")&fileId=$!nextKnow.get("id")"><font color="black">$text.get("oa.common.nextPiece")：</font>$!nextKnow.get("fileTitle")</a>
			</p>
			#end
		#end
		</div>
	</div>
	</div>
</body>
</html>
