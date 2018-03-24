<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("oa.common.bylaw")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script language="javascript" charset="utf-8" src="/js/lang/${globals.getLocale()}.js"></script>
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/jquery.cookie.js" ></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript">
function showReading(){
	asyncbox.open({
		title : '$text.get("oa.readingMark")',
		url :'/OAReadingRecordAction.do?readingInfoId=$!oaOrdain.id&readingInfoTable=OAOrdainInfo',
		width : 800,
		height :330
	});		
}
		
function addColletion(oTopicId){
	
	 var flag = $("#Konws_"+oTopicId).attr('bg');	 
	 var url = "/OACollectionAction.do?operation=2&attType="+flag+"&typeName=OAOrdain&keyId="+oTopicId;
	 jQuery.ajax({
	 	 type: "POST",
	  	 url: url,	
	  	 data:{
	  	 	urlparam:"/OAnewOrdain.do?operation=5&ordainId="+oTopicId,
	  	 	titleName:"规章制度:$!oaOrdain.ordainTitle"
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

function saveReadingInfo(){
	var str="/UtilServlet?operation=addOAReadingInfo&infoId=$!oaOrdain.id&infoTable=OAOrdainInfo";
	AjaxRequest(str);
   	var value = response;	
}

function isDelete(m){
	asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
	　　 if(action == 'ok'){
		document.location = "/OAnewOrdain.do?operation=$globals.getOP("OP_DELETE")&type=ordain&ordainId="+m;
		}
	});
}		

//新邀请阅读


function openInvite(){
	var title = encodeURI("“$text.get("oa.common.bylaw")”主题为【$!oaOrdain.ordainTitle】");
	var favoriteName = encodeURI("$globals.getEmpNameById($!LoginBean.id)规章制度$!oaOrdain.ordainTitle") ;
	var userinvite = encodeURI("$globals.getEmpNameById($!LoginBean.id)");
	var urls = "/AdviceAction.do?module=invitePre&id=$!oaOrdain.id&userinvite="+userinvite+"&favoriteURL=$!favoriteURL&favoriteName="+favoriteName+"&title="+title;
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
function nodown(){
	asyncbox.alert('没有下载权限!','$!text.get("clueTo")');
	return false;
}
</script>
</head>

<body onload="saveReadingInfo();">
	<div class="bigTitle" style="background-image: none">
		<div class="new_button">
				<button type="button" id="showReading" onClick="showReading();"style="width:80px;" class="bu_bg">
				<font color="black">$text.get("oa.readingMark")</font>
			</button>
			<button type="button"
				onClick="openInvite()"
				class="bu_bg">
				<font color="black">$text.get("oa.common.invitereading")</font>
			</button>
			#if("$!attention" == "OK")
				<button type="button" id="Konws_$oaOrdain.id" bg="del" onClick="addColletion('$oaOrdain.id');" class="bu_bg">
						取消收藏
				</button>
				#else
				<button type="button" id="Konws_$oaOrdain.id" bg="add" onClick="addColletion('$oaOrdain.id');" class="bu_bg">
						$text.get("aio.add.favorite")
				</button>
			#end
			#if("$IsEspecial"!="1")			
				#if($LoginBean.operationMap.get("/OAnewOrdainAction.do").update() && "$backtype"!="true")
					<button class="bu_bg" type="button" onClick="location.href='/OAnewOrdainAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&position=detailpage&ordainId=$!oaOrdain.id'">
						<font color="black">修改</font>
					</button>
				#end
				#if($LoginBean.operationMap.get("/OAnewOrdainAction.do").delete() && "$backtype"!="true")
					<button class="bu_bg" type="button" onclick="isDelete('$oaOrdain.id');">
						<font color="black">$text.get("oa.common.del")</font>
					</button>
				#end
				#if("$backtype"!="true")
					<button type="button"
						onClick="location.href='/OAnewOrdain.do?operation=$globals.getOP("OP_QUERY")&selectOrdain=$!oaOrdain.id'"
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
				<div class="SNews_title">
					<h2>$!oaOrdain.ordainTitle</h2>
					<div>
						创建人：<span style="color:olive;">$!globals.getEmpFullNameByUserId($!oaOrdain.createBy)</span>
						
						$text.get("common.lb.createTime")：<span style="color:olive;">$!oaOrdain.createTime </span> &nbsp;&nbsp;$text.get("oa.common.lastUpdateTime")：<span style="color:olive;">$!oaOrdain.lastupDateTime</span>
					    $text.get("or.ordain.gorup")： 
							#foreach($!g in $!group)
							<font color="olive" title="$!g.GroupName" >$globals.subTitle($globals.replaceHTML("$!g.GroupName"),30)</font>
							#end	
					  
					</div>
				</div>
				<div class="SNews_Co">				
					$!oaOrdain.content
				</div>
				<div align="left" style="margin-bottom:20px;margin-left:20px">
					<br />
					<br />
					<span style="margin-left: 60px;">
					$text.get("upload.lb.affix")：



					 #if($!oaOrdain.accessories.indexOf(";") > 0)
						#foreach ($str in $globals.strSplit($!oaOrdain.accessories,';'))
								<img src="/$globals.getStylePath()/images/down.gif"/>
							
								#if($str.indexOf(".pdf")>-1 || $str.indexOf(".PDF")>-1)
									<a href='/ReadFile?tempFile=path&path=/affix/OAOrdainInfo/&fileName=$!globals.encode($!str)&onDown=$!onDown' target="_blank">$str</a>			
								#else
									#if("$!onDown" == "true")
										<a href='/ReadFile?tempFile=path&path=/affix/OAOrdainInfo/&fileName=$!globals.encode($!str)' target="_blank">$str</a>			
									#else
										<span onclick="nodown()" style="cursor: pointer;">$str</span>
									#end
								#end	
								
						#end	
					 #else
					 	暂无
					 #end
					 </span>
					<br/><br/>
					<span style="margin-left: 30px;">
					<b>$text.get("oa.common.garget")</b> 
					#if($oaOrdain.isAlonePopedmon=="0")
						：$text.get("oa.common.target")
					</span>
					<br />
					#else
					<br/>
						<span style="margin-left: 60px;">
						#if($targetUsers.size()>0)
							$text.get("oa.common.person")：







							#foreach($user in $targetUsers) $!user.EmpFullName #end
						#end
						</span>
						<br /><br/>
						<span style="margin-left: 60px;">
						#if($targetDept.size()>0)
							$text.get("mrp.lb.department")：







							#foreach($dept in $targetDept) $!globals.get($dept,2) #end
						#end
						</span>
						<br />
					
					#end
				</div>
				<!--  判断是否是收藏夹或通知消息进入详情页面-->
				
					<div class="SNews_bottom">
					#if("$IsEspecial"!="1")
						#if($!preId)
						<p>
						<a href="/OAnewOrdain.do?operation=5&ordainId=$!preId"><font color="black">$text.get("oa.common.backPiece")：</font>
							$globals.subTitle($globals.replaceHTML("$!preTitle"),100)
						</a>
						</p>
						#end
						#if($!nextId)
						<p>
						<a href="/OAnewOrdain.do?operation=5&ordainId=$!nextId"><font color="black">$text.get("oa.common.nextPiece")：</font>
							$globals.subTitle($globals.replaceHTML("$!nextTitle"),100)
						</a>
						</p>
						#end
					#end
					</div>
		</div>
	</div>
</body>
</html>
