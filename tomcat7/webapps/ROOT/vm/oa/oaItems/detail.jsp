<!doctype html>
<html class="html">
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>我的项目</title>
#end
<link type="text/css" rel="stylesheet" href="/style/css/oa_myItems.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/oa/oaItems.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/oa/itemTaskPublic.js"></script>
<script type="text/javascript">
var hideTaskId;//隐藏任务ID
var hideItemId;//隐藏项目ID
var hideStatusBtnObj;//隐藏状态按钮OBJ
var textBoxObj;//textBoxObj对象
$(function(){
	loadTextBox('participantBoxId');
})

$(function(){
	/*项目详情-任务，任务详情-任务 点击进入详情*/
	$(".tContent").click(function(){
		var url = "/OATaskAction.do?operation=5&taskId="+$(this).parent().attr("id");
		top.mdiwin(url,$(this).text());
	})
	
	$("#itemTitle").click(function(){
		$(this).attr("contenteditable","true").focus().addClass("border-all");
	}).blur(function(){
		$(this).removeClass("border-all")
	});
	
})

/*重载上传附件成功方法,添加成功后异步提交后台*/
function onCompleteUpload(retstr,btnId){
	retstr = decodeURIComponent(retstr);  
	var buttonId = "affixuploadul";
	var hiddenName = "uploadaffix";
   	if(btnId!="undefined" && btnId!=null){
   		buttonId = "affixuploadul_"+btnId;
   		hiddenName = btnId;
   	}
   	
   	if(retstr ==""){
  	 	attachUpload.style.display="none";
   	}else{
		var mstrs = retstr.split(";");
		
		jQuery.ajax({
			type: "POST",
			url: "/OAItemsAction.do",
			data: "operation=54&itemId=$itemBean.id&uploadStrs="+retstr,
			success: function(msg){
				if(msg == "success"){
				    for(i=0;i<mstrs.length;i++){
				    	 jQuery("#li_uploadfile").remove();
					   str = mstrs[i];
					   if(str == "") continue;
					   var ul=jQuery('#'+buttonId);
					   var imgstr = "<li class='file_li' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div class='showAffix'>"+str+"</div>";
					   
					   #if("$!itemBean.executor" == "$loginBean.id")
					       imgstr +="&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"false\",\"OAItems\",\"AFFIX\");'>删除</a>&nbsp;&nbsp;&nbsp;";
					   #end
					   
					   imgstr += "<a href=\"/ReadFile?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=false&type=AFFIX&tableName=OAItems\" target=_blank>下载</a></li>";
					   ul.append(imgstr);
				    }
				    asyncbox.tips('上传成功','success');
				    var attachUpload = document.getElementById("attachUpload");
					attachUpload.style.display="none";
					//$("#noAffix").hide();
					
				}else{
					asyncbox.tips('上传失败!','error');
				}
			}
		});
   	}
	
}


function deleteupload(fileName,tempFile,tableName,type){
	//如果是临时文件，则需删除远程文件，正式文件，不能删除
	if(!confirm('$text.get("common.msg.confirmDel")')){
		return;
	}
	
	var li = jQuery("li[id='uploadfile_"+fileName+"']");
	jQuery.ajax({
		type: "POST",
		url: '/OAItemsAction.do',
		data: "operation=3&itemId=$itemBean.id&type=affix&tempFile="+tempFile+"&fileName="+encodeURIComponent(fileName),
		cache: false,
		success: function(msg){
		  	if(msg=="success"){
		  		asyncbox.tips('删除成功','success');
		  		jQuery("li[id='uploadfile_"+fileName+"']").remove();
		  		if($("#affixuploadul_uploadBtn li").length == 0){
		  			$("#noAffix").show();
		  		}
		  	}else if(msg=="failDelAffix"){
		  		asyncbox.tips('删除附件文件失败!','error');
		  	}else{
		  		asyncbox.tips('删除失败!','error');
		  	}
		}
	});
}

/*加载控件*/
function loadTextBox(boxId){
	textBoxObj = new jQuery.TextboxList('#'+boxId, {unique: true, plugins: {autocomplete: {}}});
	textBoxObj.getContainer().addClass('textboxlist-loading');
	textBoxObj.plugins['autocomplete'].setValues($textBoxValues);
	textBoxObj.getContainer().removeClass('textboxlist-loading');
}
</script>

</head>

<body>
#if("$!addTHhead" == "true")
#parse("./././body2head.jsp")
<div>
#else
<div id="scroll-wrap">
#end
<input type="hidden" id="itemId" name="itemId" value="$!itemBean.id">
<input type="hidden" id="today" name="today" value='$globals.getHongVal("sys_date")'>
<input type="hidden" name="addTHhead" id="addTHhead" value="$addTHhead"/>
<div class="wrap" id="wrap">
#set($isEdit = "false")
#if("$!itemBean.status" != "2" && ("$!itemBean.createBy" == "$loginBean.id" || "$!itemBean.executor" == "$loginBean.id"))
#set($isEdit = "true")
#end 
#if("$!itemBean.status" == "2")
<div class="pa pa-statu"></div>
#end
	<!--  项目顶部dHeader 分割线Start-->
	<div class="dHeader">
    	<div class="dHeaderL">
        	<b class="icons projectPhoto"></b>
            <div class="lf proD">
            	<span class="No-color">NO.$!itemBean.itemOrder</span>
            	#if($isEdit == "true")
            		<span class="pr pro-title">
            			#set($encodeTitle = $globals.encodeHTML2($!itemBean.title))
	            		<input type="text" class="proN" id="itemTitle" title="$encodeTitle" value="$encodeTitle" placeholder="编辑项目名称" defValue="$encodeTitle" fieldName="title" onchange="updSingleField(this);"/>
	            		<em class="pa t-task">
            				<b class="icons arrow-point"></b>
            				$encodeTitle
            			</em>
	            	</span>
            	#else
            		<p class="pr pro-title">
            			<i class="proN" title="$!itemBean.title">$!itemBean.title</i>
            			<em class="pa t-task">
            				<b class="icons arrow-point"></b>
            				$!itemBean.title
            			</em>
            		</p>
            	#end
                <div class="rWp clear">
                	#if("$!itemBean.status" != "2" && "$!itemBean.createBy" == "$!loginBean.id")
                		<span class="lf proT">
	                		<input type="text" class="lf time-txt" id="itemBeginTime" value='$!itemBean.beginTime' defValue="$!itemBean.beginTime" title="$!itemBean.beginTime" fieldName="beginTime" onClick="selectDate('itemBeginTime')" readonly="readonly" />
	                		<em class="lf">至</em>
	                		<input type="text" class="lf time-txt" id="itemEndTime" value='$!itemBean.endTime' defValue="$!itemBean.endTime" title="$!itemBean.endTime" fieldName="endTime"  onClick="selectDate('itemEndTime')" readonly="readonly"/>
	                	</span>
                	#else
                		<span class="lf pr proT">
	                		<i class="lf time-txt"  title="$!itemBean.beginTime">$!itemBean.beginTime</i>
	                		<em class="lf">至</em>
	                		<i class="lf time-txt" title="$!itemBean.endTime">$!itemBean.endTime</i>
	                	</span>
                	#end
                	
                	
                	#if("$!itemBean.status" != "2")
						#set($timeDiff= $globals.getTimeDiff($itemBean.endTime,$globals.getHongVal("sys_date")))
						#set($timeDiff = $math.toNumber($timeDiff))
						#if($timeDiff>0)
							<span class="lf cDown" id="itemTimeDiff">剩余<i>$timeDiff</i>天</span>
						#elseif($timeDiff==0)
							<span class="lf cDown" id="itemTimeDiff">今天到期<i></i></span>
						#else
							<span class="lf cDown" id="itemTimeDiff">超过<i>$math.abs($timeDiff)</i>天</span>
						#end
						#if("$!itemBean.executor" == "$loginBean.id")
	                    <span class="lf pr sR" >
		                    <b class="pa icons" title="提醒设置" id="setWarn"></b>
		                    <b id="alertTimeShow" class="pr">
		                    #if("$!itemBean.nextAlertTime" !="")
		                    	<span title='提醒时间:$!itemBean.nextAlertTime'>已设提醒</span>
		                    	<b class="delAlert" actionName="OAItemsAction" keyId="$!itemBean.id"></b>
		                    #end
		                    </b>
		                    <div id="show_t" style="display: none;">
							    <input type="text" id="alterDay" name="alterDay" #if("$!alertDate" == "") value='$globals.getHongVal("sys_date")' #else value="$!alertDate" #end  onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly" style="width:70px"/>
								    <select id="alterHour" name="alterHour">
								    	#foreach($hour in [0..23])
									    	#if($hour < 10)		    			    	
									    		<option #if("$hour" == "$!alertHour") selected="selected" #end>0$!hour</option>		    	
									    	#else
									    		<option #if("$hour" == "$!alertHour") selected="selected" #end>$!hour</option>
									    	#end
								    	#end
								    </select>
								    <select id="alterMinutes" name="alterMinutes">
								    	#foreach($minute in ['00',10,20,30,40,50])
								    		<option #if("$minute" == "$!alterMinutes") selected="selected" #end>$!minute</option>
								    	#end
								    </select>
								<div class="btn btn-mini" onclick="setAlter();">确定</div>
						 	</div>  
	                    </span>
	                     #end
                	#end
                </div>
                #if($isEdit == "true")
	                <span class="pr remark-span">
	                	<input type="text" class="lf time-txt" id="remarkInput" value='$!itemBean.remark' placeholder="编辑详情描述"/>
	                	<div class="pa e-up d-hover" id="remarkDiv">
	                		<b class="icons arrow-point"></b>
	                		<i>$!itemBean.remark</i>
	                	</div>
	                	<div class="pa e-up d-txt" style="display:none;" id="remarkEditDiv">
	                		<b class="icons arrow-point"></b>
	                		<textarea id="remarkTextarea" title="$!itemBean.remark">$!itemBean.remark</textarea>
	                		<b class="pa btn-blue" onclick="updDetailRemark('OAItemsAction','$!itemBean.id');" >确定</b>
	                		<b class="pa qx remarkEditCancel">取消</b>
	                	</div>
	                </span>
                #else
                	<span class="pr lf remark-span">
	                	<i class="lf time-txt" title="$!itemBean.remark">$!itemBean.remark</i>
	                	<div class="pa e-up">$!itemBean.remark</div>
	                </span>
                #end
                
                #set($colorBarWidth = 0)
                #if("$!itemBean.schedule" !="")
                	#set($colorBarWidth = $!itemBean.schedule)
                #end
                
                #set($editSchedule = "")
                #set($cursorStr = "")
                #if($isEdit == "true"  && "$!itemBean.status" != "2")
                	#set($editSchedule = "changeSchedule")
                	#set($cursorStr = "cursor:pointer;")
                #end
                
                #if("$!itemBean.clientId" !="")
                <div class="rWp">
	                <span class="remark-span">
	                	<b class="lf">关联客户名称：</b><a class="lf" onclick="clientDetail('$!itemBean.clientId','$clientName')" href="#">$clientName</a>
	                </span>
                </div>
                #end
                
                <div class="clear"></div>
                <div class="d-pace-wp" style="clear:both;">
                	<em class="lf">进度：</em>
	                <div class="ui-slider" style="margin:10px 0 0 10px;width:176px;float:left;" actionName="OAItemsAction">
	                	<em class="num-po">${colorBarWidth}%</em>
	                	<div style="height:4px;background:#FFD904;width:${colorBarWidth}%;" id="colorBar"></div>
	                	<div class="ui-slider-segment" style="margin-left:0;">0%</div>
	                	<div class="ui-slider-segment">25%</div>
	                	<div class="ui-slider-segment">50%</div>
	                	<div class="ui-slider-segment">75%</div>
	                	<div class="ui-slider-segment" style="position:absolute;right:1px;margin:0;top:7px;">100%</div>
			            <a class="ui-slider-handle $!editSchedule" style="left:0;$cursorStr" fieldVal="0"></a>
			            <a class="ui-slider-handle $!editSchedule" style="left:25%;$cursorStr" fieldVal="25"></a>
			            <a class="ui-slider-handle $!editSchedule" style="left:50%;$cursorStr" fieldVal="50"></a>
			            <a class="ui-slider-handle $!editSchedule" style="left:75%;$cursorStr" fieldVal="75"></a>
			            <a class="ui-slider-handle $!editSchedule" style="left:100%;$cursorStr" fieldVal="100"></a>
			        </div>   
                </div>
                
            </div>
        </div>
        <div class="dHeaderR">
        	#if("$!itemBean.executor" == "$loginBean.id" || "$!itemBean.createBy" == "$loginBean.id")
	        	#if("$itemBean.status" == "1")
	        		<span class="finish pr" status="2" isDetail="true" onclick="showFeedback('$itemBean.id',this);" title="点击将项目标为完成"><b class="pa icons"></b>完成</span>
	        	#else
	        		<span class="finish pr" status="1" isDetail="true" onclick="showFeedback('$itemBean.id',this);" title="点击将重新启动项目"><b class="pa icons"></b>重新启动</span>
	        	#end
        	#end
            <div class="tPerson">
            	<span class="charge">
                    <em class="lf">负责人：</em>
                    <i class="lf" id="showExecutor">$!globals.getEmpFullNameByUserId($itemBean.executor)</i>
                </span>
                <div class="pr party">
                    <em class="lf">参与人：</em>
                    <span class="lf" id="participants"></span>
                    <span class="pa" id="particPa"></span>
                </div>
            </div>
           	<div class="create-time">创建时间：$itemBean.createTime</div>
        </div>
        <div class="clear"></div>
    </div>
    
    <!--  项目顶部dHeader 分割线End-->
    <ul class="uNav">
    	<li id="tabDiscuss" class="sel">动态</li>
        <li id="tabTask">任务</li>
        <li id="tabAffix">附件</li>
       	<li id="tabParticipant">人员</li>
    </ul>
    
    
    <!-- 评论开始-->
    <div class="mission" id="tabDiscussShow">
    	<input type="hidden" id="parentIframeName" name="parentIframeName" value="tabDiscussIframe"/>
    	<iframe frameborder="0" scrolling="0" width="100%" height="330px;" id="tabDiscussIframe" src="/DiscussAction.do?tableName=OAItemsLog&f_ref=$itemBean.id&parentIframeName=tabDiscussIframe"></iframe>
    </div>
    <!--  评论结束-->
    
    
    <!-- 附件开始-->
    <div class="mission" id="tabAffixShow" style="display: none;">
		<div class="pr">
			<em></em>
			<span id="picbutton" name="picbutton"  onClick="upload('AFFIX','uploadBtn');">上传</span>
		</div>										
			<ul id="affixuploadul_uploadBtn">
				#foreach($uprow in $!itemBean.affix.split(";"))
					#if($uprow != "")
						<li class="file_li" id="uploadfile_$uprow">
						<input type=hidden id="uploadBtn" name=uploadBtn value='$uprow'/><div class="showAffix">$uprow</div>
										
							#if("$!itemBean.executor" == "$loginBean.id" || "$!itemBean.createBy" == "$loginBean.id")&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload("$uprow","false","OAItems","AFFIX");'>$text.get("common.lb.del")</a>&nbsp;&nbsp;#end
							<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=OAItems" target="_blank">$text.get("common.lb.download")</a>
						</li>
					#end
				#end
			</ul>    	
		<div class="no-file" onClick="upload('AFFIX','uploadBtn');" id="noAffix">
				拖拽文件到此或点击添加附件






		</div>
    </div>
    <script type="text/javascript">
	    /*wyy拖拽上传*/
		var output = document.getElementById('affixuploadul_uploadBtn');
		var dropZone = document.getElementById('noAffix');
		
		if (!(('draggable' in dropZone) && ('ondragenter' in dropZone)
		          && ('ondragleave' in dropZone) && ('ondragover' in dropZone)
		          && window.File)) {
		  	alert("支持谷歌，火狐和IE8以上版本的浏览器");
		} else {
		  	function handleFileDragEnter(e) {
		    	e.stopPropagation();
		    	e.preventDefault();
		    	this.classList.add('hovering');
		  	}
		
		  	function handleFileDragLeave(e) {
			    e.stopPropagation();
			    e.preventDefault();
		    	this.classList.remove('hovering');
		  	}
		
		  	function handleFileDragOver(e) {
			    e.stopPropagation();
			    e.preventDefault();
		    	e.dataTransfer.dropEffect = 'copy';
		  }
		
		  	function handleFileDrop(e) {   
		 		var files = e.dataTransfer.files;
		 		
		    	var outputStr = [];
		    	var filesName = "";
		    	var fd = new FormData(); 
		    	for(var i=0;i<files.length;i++){
		    		filesName =filesName+files[i].name+";"; 
		    		fd.append(files[i].name, files[i]);
		    		var li = '<li class="file_li" id="li_uploadfile">'
							 +'<div class="showAffix">'+files[i].name+'</div><p style="margin-left:150px;"><img src="/style/images/loading2.gif"/>正在上传...</p></li>';
					jQuery("#affixuploadul_uploadBtn").append(li);
		    	}
		        jQuery.ajax({
		        	type:"POST",url: "/UploadServlet?path=/temp/",data:fd, contentType:false,processData:false,
					success: function(data){
						if(data.indexOf("ERROR")>=0){
							jQuery("#li_uploadfile").remove();
							asyncbox.alert(data,'error');
						}else{
							onCompleteUpload(filesName,"uploadBtn");
						}						
					},
					error:function(msg){
						jQuery("#li_uploadfile").remove();
						asyncbox.alert(msg,'error');
					}
				});	        	    		  
	    		e.stopPropagation();
	    		e.preventDefault();
	    		this.classList.remove('hovering');    
		    	   
		  }
		  	dropZone.addEventListener('dragenter', handleFileDragEnter, false);
		  	dropZone.addEventListener('dragleave', handleFileDragLeave, false);
		  	dropZone.addEventListener('dragover', handleFileDragOver, false);
		  	dropZone.addEventListener('drop', handleFileDrop, false);
		}
	    
	    </script>
    <!--  附件结束-->
    
    
    <!--  任务mission 分割线End-->
    <div class="mission" id="tabTaskShow"  style="display: none;"></div>
    <!--  任务mission 分割线End-->
    
    
    <!--  人员Person 分割线End-->
    <div class="mission Person" style="display:none;" id="tabParticipantShow">
    	
    	<p id="participantCount"></p>
        <div class="pListWp">
        	<div class="charge">
        		<li class="mesOnline" empId="$itemBean.executor">
	            	<a href="javascript:top.msgCommunicate('$!itemBean.executor','$!globals.getEmpFullNameByUserId($!itemBean.executor)');" class="lf Image"><img src="$globals.checkEmployeePhoto("48",$itemBean.executor)" /></a>
	                <span class="lf">
	                	<i><b>$!globals.getEmpFullNameByUserId($itemBean.executor)</b>（负责人）</i>
	                </span>
                </li>
                
            </div>
            
            #if($isEdit == "true")
            <div class="lately-person">
            	<span class="lf">
            	快速添加



            	</span>
            	<div class="lf">
					<input class="lf txtIconLong ip_txt" type="text" id="participantBoxId"/>
				</div>
				<div class="lf d-btn-w" onclick="participantBoxSubmit();">
					 确定
				</div>
				<div class="clear"></div>
			</div>
			#end
			
            <ul class="party" id="showParticipant">
            	#foreach($employeeId in $itemBean.participant.split(","))
            		#if("$!employeeId" !="")
		            	<li empId="$!employeeId" class="mesOnline">
		                	<a href="javascript:top.msgCommunicate('$employeeId','$!globals.getEmpFullNameByUserId($employeeId)');" class="lf Image"><img src="$globals.checkEmployeePhoto("48",$employeeId)" /></a>
		                    <i class="lf"><b>$!globals.getEmpFullNameByUserId($employeeId)</b></i>
		                    #if(("$!itemBean.executor" == "$loginBean.id" || "$!itemBean.createBy" == "$loginBean.id") && "$isEdit" == "true")
		                    	<b class="icons b-del-t"></b>
		                    #end
		                </li>
            		#end
            	#end
                #if($isEdit == "true")
                <li class="addL pr" onclick="publicPopSelect('userGroup','itemTitle','participantId','true')" empId="addParticipant">
                	<b class="icons pa" ></b>
                </li>
                #end
            </ul>
        </div>
    </div>
    <!--  人员Person 分割线End-->
     <!-- 弹出评论层 Start -->
    <div class="addWrap pop-layer" id="addFeedbackDiv" style="top: 31px; left: 369px; display:none;"></div>
	<div class="addWrap pop-layer" id="addChildFeedbackDiv" style="position:fixed;top:200px; left:450px; display:none;"></div>
    <!-- 弹出评论层 End -->
	<!--  收藏按钮 Start-->
	<b class="join-collect icons" #if("$!attentionCard" == "attentionCard") style="background-position:-236px -126px;" title="取消收藏" bg="f" #else title="加入收藏" bg="t" #end onclick="getCollection('$itemBean.id','项目:$!itemBean.title','/OAItemsAction.do?operation=5&itemId=$itemBean.id','OAItems');"></b>
	<!--  收藏按钮 End-->
</div>
<div id="hideBg" class="hideBg"></div>
<script type="text/javascript">
	var sHeight=document.documentElement.clientHeight;
	$("#scroll-wrap").height(sHeight);
	$("#wrap").css("min-height",sHeight-15);
</script>
</div>
</body>
</html>
