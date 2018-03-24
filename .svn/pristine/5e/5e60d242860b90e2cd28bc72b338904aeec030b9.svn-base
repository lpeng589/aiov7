<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.common.newsloca")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<style type="text/css">
#files_preview>div{float:left;padding:5px;display:inline-block;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.cookie.js" ></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript">
/*wyy*/
function addColletion(oTopicId){
	
	 var flag = $("#News_"+oTopicId).attr('bg');
	
	 var url = "/OACollectionAction.do?operation=2&attType="+flag+"&typeName=OANews&keyId="+oTopicId;
	 jQuery.ajax({
	 	 type: "POST",
	  	 url: url,	
	  	 data:{
	  	 	urlparam:"/OANews.do?operation=5&newsId="+oTopicId,
	  	 	titleName:"新闻中心:$!oaNews.newsTitle"
	  	 },	   
	  	 success: function(msg){
	  	 	if(flag == "add"){
	  	 		if(msg == "OK"){
		  	 		asyncbox.tips('收藏成功','success');
		  	 		$("#News_"+oTopicId).attr('bg','del');
		  	 		$("#News_"+oTopicId).html('取消收藏');		  	 		
	  			}else{
	  				asyncbox.tips('收藏失败','error');		    
	  			}
	  	 	}else{
	  	 		if(msg == "OK"){
		  	 		asyncbox.tips('取消收藏成功','success');
		  	 		$("#News_"+oTopicId).attr('bg','add');
		  	 		$("#News_"+oTopicId).html('加入收藏');
	  			}else{
	  				asyncbox.tips('取消收藏失败','error');		    
	  			}
	  	 	}	  	 	
	  	}
	});
}

function showReading(){
	asyncbox.open({
		title : '$text.get("oa.readingMark")',
		url :'/OAReadingRecordAction.do?readingInfoId=$!oaNews.id&readingInfoTable=OACompanyNewsInfo',
		width : 800,
		height :330
	});	
}

function collection(){
	var favoriteURL = "$!favoriteURL" ;
	var favoriteName = encodeURI("$!oaNews.newsTitle");
	var tabName = encodeURI("$text.get('oa.common.newList')");
	var str="/UtilServlet?operation=addDedailAdviceInfoCollection&module=dedailAdviceInfo&favoriteName="+favoriteName+"&tabName="+tabName+"&favoriteURL="+favoriteURL;
 	AjaxRequest(str);
    var value = response;
	var oaul=document.getElementById("empList");
	if(value=="1"){			 	
		asyncbox.tips('$text.get("oa.favorite.add.success")','success');
	}else if(value=="2"){
		asyncbox.tips('$text.get("oa.favorite.exist")','alert');
	}else{
		asyncbox.tips('$text.get("oa.favorite.add.failture")','error');
	}
}
	
function saveReadingInfo(){
	 if("$!oaNews.isSaveReading"=="1"){
 		var str="/UtilServlet?operation=addOAReadingInfo&infoId=$!oaNews.id&infoTable=OACompanyNewsInfo";
 	     AjaxRequest(str);
    	var value = response;		
	}
}
		
function openAddReply(){
	window.frames["newsframe"].openAddReply();
}

function isDelete(m){
	asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
	　　 if(action == 'ok'){
		document.location = "/OANews.do?operation=$globals.getOP("OP_DELETE")&type=news&newsId="+m;
		}
	});
}

//新邀请阅读

function openInvite(){
	var title = encodeURI("“$text.get("oa.common.newList")”主题为【$!oaNews.newsTitle】");
	var favoriteName = encodeURI("$globals.getEmpNameById($!LoginBean.id)$text.get("oa.knowledgeCenter.requestRead")$!oaNews.newsTitle") ;
	var userinvite = encodeURI("$globals.getEmpNameById($!LoginBean.id)");
	var urls = "/AdviceAction.do?module=invitePre&id=$!oaNews.id&userinvite="+userinvite+"&favoriteURL=$!favoriteURL&favoriteName="+favoriteName+"&title="+title;
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
function fb(){		    
	window.location.href="/OANewsAction.do?operation=$globals.getOP("OP_UPDATE")&updateType=fb&newsId=$!oaNews.id";
}
</script>
</head>
<body onload="saveReadingInfo();">
	<div class="bigTitle" style="background-image: none">
		<div class="new_button">
		 #if($!oaNews.statusId != 1 )
				<button type="button" id="showReading" onclick="showReading();" style="width:80px;"
					class="bu_bg">
					<font color="black">$text.get("oa.readingMark")</font>
				</button>
				<button type="button"
						onClick="openInvite();"
						class="bu_bg">
						<font color="black">$text.get("oa.common.invitereading")</font>
				</button>
				#if("$!attention" == "OK")
				<button type="button" id="News_$!oaNews.Id" bg="del" onClick="addColletion('$!oaNews.Id');" class="bu_bg">
						取消收藏
				</button>
				#else
				<button type="button" id="News_$!oaNews.Id" bg="add" onClick="addColletion('$!oaNews.Id');" class="bu_bg">
						$text.get("aio.add.favorite")
				</button>
				#end
		#else
			<button type="submit" name="Submit" onClick="fb();" class="fb"></button>
		#end
				#if("$IsEspecial"!="1")	
				
					#if($LoginBean.operationMap.get("/OANewsAction.do").update() && "$backtype" !="true" )
						<button class="bu_bg" type="button" onClick="location.href='/OANewsAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&position=detailpage&newsId=$!oaNews.id'">
							<font color="black">$text.get("oa.common.upd")</font>
						</button>
					#end
					#if($LoginBean.operationMap.get("/OANewsAction.do").delete() && "$backtype" !="true")
						<button class="bu_bg" type="button" onclick="isDelete('$oaNews.id');">
							<font color="black">$text.get("oa.common.del")</font>
						</button>
					#end
					#if("$backtype"!="true")
						<button type="button"
							onClick="location.href='/OANews.do?operation=$globals.getOP("OP_QUERY")&selectNews=$!oaNews.Id'"
									class="fh">
						</button>
					#end
				#end	
				#if("$IsEspecial"=="1" || "$backtype"=="true")					
					<button type="button" onClick="closeWin();" class="bu_bg">
					<font color="black">$text.get("common.lb.close")</font>
					</button>
				#end
			</div>
		</div>
		<div id="nn" style="overflow:hidden; overflow-y:auto; width:99%;">
<script type="text/javascript">
	var oDiv=document.getElementById("nn");
	var sHeight=document.documentElement.clientHeight-55;
	oDiv.style.height=sHeight+"px";
</script>
			<div >
				<div class="SNews">
					#if($!oaNews.statusId ==1)
							<p style="text-align: right;"><img src="style1/images/oaimages/caogao.png" alt="草稿" title="草稿" style=" margin-right: 10px;" /></p>
					#end
					<div class="SNews_title">
						<h2 >$!oaNews.newsTitle</h2>
						<div>							
							#if("$!oaNews.statusId" == "1")
								创建人：<span style="color:olive;">$!globals.getEmpFullNameByUserId($!oaNews.createBy)</span>
								$text.get("common.lb.createTime")：<span style="color:olive;">$!oaNews.createTime </span>
							#else
								发布人：<span style="color:olive;">$!globals.getEmpFullNameByUserId($!oaNews.userName)</span>
								$text.get("oa.common.publishTime")：<span style="color:olive;">$!oaNews.releaseTime </span>
							#end
							&nbsp;&nbsp;$text.get("oa.common.lastUpdateTime")：<span style="color:olive;">$!oaNews.lastupDateTime</span>
							$text.get("oa.common.newsType")：


								<font color="olive">#foreach($row_newsType in
								$globals.getEnumerationItems("NewsType")) #if($!oaNews.newsType
								== $row_newsType.value) $!row_newsType.name #end #end </font>
						</div>
					</div>
					<div class="SNews_Co" style="margin:0;">
						$!oaNews.newsContext			
					</div>
					<div id="files_preview" style="overflow:hidden;padding:0 20px;">
								#foreach($att in $oaNews.picFiles.split(",|;"))
								#if($att.length()>0)
								<div id="$att"
									style=" color:#ff0000;float:left;vertical-align:top">
									<a
										href="/ReadFile.jpg?type=PIC&tempFile=path&path=/news/&fileName=$att"
										target="_blank"><img
											src="/ReadFile.jpg?type=PIC&tempFile=path&path=/news/&fileName=$att"
											width="150" height="115" border="0"/> </a>						
								</div>
								#end #end
							</div>	
					<div align="left" style="margin-bottom:20px;margin-left:20px">
						<span style="margin-left: 80px;">
						</span>
						<div style="margin-left: 30px;">
						<b>$text.get("oa.common.garget") </b>
						#if($oaNews.isAlonePopedmon=="0")
							：$text.get("oa.common.target")
						#else
						<br/>	
							<span style="margin-left: 30px;">
							#if($targetUsers.size()>0)
								$text.get("oa.common.person")：


								#foreach($user in $targetUsers) $!user.empFullName 
								#end
							#end
							</span>
							<br /><br />	
							<span style="margin-left: 30px;">
							#if($targetDept.size()>0)
								$text.get("mrp.lb.department")：


								#foreach($dept in $targetDept) $!globals.get($dept,2)
							    #end
							#end
							</span>
							<br /><br />	
							<span style="margin-left: 30px;">
							#if($targetEmpGroup.size()>0)
								$text.get("oa.oamessage.usergroup")：


							    #foreach($grp in $targetEmpGroup) $!globals.get($grp,5)
							    #end
							#end
							</span>
							<br />
						#end
						</div>
					</div>
					<!--  判断是否是收藏夹或通知消息进入详情页面-->
					<div class="SNews_bottom">
						#if("$IsEspecial"!="1")				
							#if($!preId)
							<p>
							<a href="/OANews.do?operation=5&newsId=$!preId"><font color="black">$text.get("oa.common.backPiece")：</font>
							$globals.subTitle($globals.replaceHTML("$!preTitle"),100)
							</a>
							</p>
							#end			
							#if($!nextId)
							<p>
							<a href="/OANews.do?operation=5&newsId=$!nextId"><font color="black">$text.get("oa.common.nextPiece")：</font>
							$globals.subTitle($globals.replaceHTML("$!nextTitle"),100)
							</a>
							</p>
							#end
						
						#end			
					</div>
				</div>				
			</div>	
			#if("$oaNews.whetherAgreeReply"=="1" && "$!oaNews.statusId" == "0" )
			<div class="SNews" style="margin-top:-10px;min-height:200px;">
				<iframe src="/OANews.do?operation=5&type=addreplyPrepare&newsId=$!oaNews.id" name="newsframe" frameborder="0" width="100%" scrolling="no" onload="this.height=newsframe.document.body.scrollHeight"></iframe>
			</div>
			#end
		</div>
	</body>
</html>
