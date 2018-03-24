<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.mydesk.notice")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script src="/js/AsyncBox.v1.4.5.js" type="text/javascript"></script>
<script language="javascript">	
/*wyy*/
function addColletion(oTopicId){
	
	 var flag = $("#Advice_"+oTopicId).attr('bg');	 
	 var url = "/OACollectionAction.do?operation=2&attType="+flag+"&typeName=OAnewAdvice&keyId="+oTopicId;
	 jQuery.ajax({
	 	 type: "POST",
	  	 url: url,	
	  	 data:{
	  	 	urlparam:"/OAnewAdvice.do?operation=5&adviceId="+oTopicId,
	  	 	titleName:"通知通告:$!oaAdvice.adviceTitle"
	  	 },	   
	  	 success: function(msg){
	  	 	if(flag == "add"){
	  	 		if(msg == "OK"){
		  	 		asyncbox.tips('收藏成功','success');
		  	 		$("#Advice_"+oTopicId).attr('bg','del');
		  	 		$("#Advice_"+oTopicId).html('取消收藏');		  	 		
	  			}else{
	  				asyncbox.tips('收藏失败','error');		    
	  			}
	  	 	}else{
	  	 		if(msg == "OK"){
		  	 		asyncbox.tips('取消收藏成功','success');
		  	 		$("#Advice_"+oTopicId).attr('bg','add');
		  	 		$("#Advice_"+oTopicId).html('加入收藏');
	  			}else{
	  				asyncbox.tips('取消收藏失败','error');		    
	  			}
	  	 	}	  	 	
	  	}
	});
}


function showReading(){
	asyncbox.open({
		title : '$text.get('oa.readingMark')',
		url :'/OAReadingRecordAction.do?readingInfoId=$!oaAdvice.id&readingInfoTable=OAAdviceInfo',
		width : 800,
		height :330
	});
}


function collection(){
	var favoriteURL = "$!favoriteURL" ;
	var favoriteName = encodeURI("$!oaAdvice.adviceTitle");
	var myCollectionURL = "$!myCollectionURL" ;
	var tabName = encodeURI("$text.get('oa.mydesk.notice')");
	var str="/UtilServlet?operation=addDedailAdviceInfoCollection&module=dedailAdviceInfo&favoriteName="+favoriteName+"&tabName="+tabName+"&favoriteURL="+favoriteURL+myCollectionURL;
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
	 if("$!oaAdvice.isSaveReading"=="1"){
 		var str="/UtilServlet?operation=addOAReadingInfo&infoId=$!oaAdvice.id&infoTable=OAAdviceInfo";
 	    AjaxRequest(str);
    	var value = response;		
	}
}

function isDelete(m){
	asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
	　　if(action == 'ok'){
			document.location = "/OAnewAdvice.do?operation=$globals.getOP("OP_DELETE")&type=advice&adviceId="+m;
		}
	});
}

//新邀请阅读

function openInvite(){
	var title = encodeURI("“$text.get("oa.mydesk.notice")”主题为【$!oaAdvice.adviceTitle】");
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

function setOffSetTop(num){
	newPos=new Object();
	newPos.left="82";
	newPos.top="80";
	$("#buttonReply").offset(newPos);
}

function fb(){		    
	window.location.href="/OAnewAdviceAction.do?operation=$globals.getOP("OP_UPDATE")&updateType=fb&adviceId=$!oaAdvice.id";
}
function evaluate(){
			var datas =document.getElementById("dbData").value;
			newOpenSelect(datas,fieldNames,fieldNIds);
		
}
</script>
</head>
<body onload="saveReadingInfo();">
	<input type="hidden" name="dbData" id="dbData" value="" onpropertyChange="javascript:if(this.value!=null)evaluate()"/>
	<div class="bigTitle" style="background-image: none">
		<div class="new_button">
		#if($!oaAdvice.statusId != 1)
			<button type="button" onclick="showReading();" style="width:80px;" class="bu_bg">
				<font color="black">$text.get("oa.readingMark")</font>
			</button>
			<button type="button" onClick="openInvite()" class="bu_bg">
					<font color="black">$text.get("oa.common.invitereading")</font>
			</button>
			#if("$!attention" == "OK")
				<button type="button" id="Advice_$!oaAdvice.id" bg="del" onClick="addColletion('$!oaAdvice.id');" class="bu_bg">
						取消收藏
				</button>
				#else
				<button type="button" id="Advice_$!oaAdvice.id" bg="add" onClick="addColletion('$!oaAdvice.id');" class="bu_bg">
						$text.get("aio.add.favorite")
				</button>
			#end
		#else
			<button type="submit" name="Submit" onClick="fb();" class="fb"></button>
		#end
			#if("$IsEspecial"!="1")
				
				#if($LoginBean.operationMap.get("/OAnewAdviceAction.do").update() && "$backtype"!="true")
				<button class="bu_bg" type="button" onClick="location.href='/OAnewAdviceAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&position=detailpage&adviceId=$!oaAdvice.id'">
					<font color="black">修改</font>
				</button>
				#end
				#if($LoginBean.operationMap.get("/OAnewAdviceAction.do").delete() && "$backtype"!="true")
					<button class="bu_bg" type="button" onclick="isDelete('$oaAdvice.id');">
						<font color="black">$text.get("oa.common.del")</font>
					</button>
				#end
				#if("$backtype"!="true")
					<button type="button" onClick="location.href='/OAnewAdvice.do?operation=$globals.getOP("OP_QUERY")&selectAdvice=$!oaAdvice.id'" class="fh">
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
			<div class="SNews">
					#if($!oaAdvice.statusId ==1)
							<p style="text-align: right;"><img src="style1/images/oaimages/caogao.png" alt="草稿" title="草稿" style=" margin-right: 10px;" /></p>
					#end
					<div class="SNews_title">
						<h2>$!oaAdvice.adviceTitle</h2>
						<div>
							
							#if("$!oaAdvice.statusId" == "1")
								创建人：<span style="color:olive;">$!globals.getEmpFullNameByUserId($!oaAdvice.createBy)</span>
								$text.get("common.lb.createTime")：<span style="color:olive;">$!oaAdvice.createTime </span>
							#else
								发布人：<span style="color:olive;">$!globals.getEmpFullNameByUserId($!oaAdvice.pulisher)</span>
								$text.get("oa.common.publishTime")：<span style="color:olive;">$!oaAdvice.pulishDate </span>
							#end
								&nbsp;&nbsp;$text.get("oa.common.lastUpdateTime")：<span style="color:olive;">$!oaAdvice.lastupDateTime</span>
							$text.get("oa.common.adviceType")：<font color="olive">
								#foreach($row_adviceType in $globals.getEnumerationItems("AdviceType"))
								#if($!oaAdvice.adviceType == $row_adviceType.value)
								$!row_adviceType.name #end #end 
								</font>
						</div>
					</div>
					<div class="SNews_Co">
						$!oaAdvice.adviceContext
					</div>
					<div align="left" style="margin-bottom:20px;margin-left:20px">
						<br />
						<br />
						<span style="margin-left:60px;">
						$text.get("upload.lb.affix")：




 						#if($!oaAdvice.filePath.indexOf(";") > 0)
							#foreach ($str in $globals.strSplit($!oaAdvice.filePath,';'))
								<img src="/$globals.getStylePath()/images/down.gif"/>
								<a	href='/ReadFile?tempFile=path&path=/affix/OAAdviceInfo/&fileName=$!globals.encode($!str)'target="_blank"> $str</a> 
						    #end
						#else
							暂无
						#end
						
						  </span>
						<br/><br/>
						<span style="margin-left: 30px;">
						<b>$text.get("oa.common.garget")</b>
						#if($oaAdvice.isAlonePopedmon=="0")
							：$text.get("oa.common.target")
							</span>
						<br />
						#else
						<br />
							<span style="margin-left: 60px;">
							#if($targetUsers.size()>0)
								$text.get("oa.common.person")：










								#foreach($user in $targetUsers) $!user.EmpFullName #end
							#end
							</span>
							<br />	<br />
							<span style="margin-left: 60px;">
							#if($targetDept.size()>0)
								$text.get("mrp.lb.department")：











								#foreach($dept in $targetDept) $!globals.get($dept,2) #end
							#end
							</span>
							<br />		<br />
							<span style="margin-left: 60px;">
							#if($targetEmpGroup.size()>0)
								$text.get("oa.oamessage.usergroup")： 
								#foreach($grp in $targetEmpGroup) $!globals.get($grp,5) #end
							#end
							</span>
							<br />
						#end
					</div>
					<!--  判断是否是收藏夹或通知消息进入详情页面-->
					<div class="SNews_bottom" id="buttonReply"">
						#if("$IsEspecial"!="1")				
							#if($!preId)
							<p>
							<a href="/OAnewAdvice.do?operation=5&adviceId=$!preId"><font color="black">$text.get("oa.common.backPiece")：</font>
							$globals.subTitle($globals.replaceHTML("$!preTitle"),100)
							</a>
							</p>
							#end
				
							#if($!nextId)
							<p>
							<a href="/OAnewAdvice.do?operation=5&adviceId=$!nextId"><font color="black">$text.get("oa.common.nextPiece")：</font>
							$globals.subTitle($globals.replaceHTML("$!nextTitle"),100)
							</a>
							</p>
							#end						
						#end					
					</div>
			</div>
			#if("$oaAdvice.whetherAgreeReply"=="1" && "$!oaAdvice.statusId" == "0")
			<div class="SNews" style="margin-top:-10px;min-height:200px;">
			<iframe name="newsframe" src="/OAnewAdvice.do?operation=5&type=addreplyPrepare&adviceId=$!oaAdvice.id" frameborder="0" width="100%" scrolling="no" onload="this.height=newsframe.document.body.scrollHeight"></iframe>
			</div>
			#end
		</div>
	</body>
</html>
