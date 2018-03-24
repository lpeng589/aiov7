<!doctype html>
<html class="html">
<head>
<meta name="renderer" content="webkit">
<meta charset="utf-8">
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>我的任务</title>
#end
<link type="text/css" rel="stylesheet" href="/style/css/oa_myTask.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="/js/oa/oaTask.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/oa/itemTaskPublic.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript">
var hideTaskId;//隐藏任务ID
var hideStatusBtnObj;//隐藏状态按钮OBJ
var textBoxObj;//textBoxObj对象
$(function(){
	loadTextBox('participantBoxId');
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
			url: "/OATaskAction.do",
			data: "operation=54&taskId=$taskBean.id&uploadStrs="+retstr,
			success: function(msg){
				if(msg == "success"){
				    for(i=0;i<mstrs.length;i++){
				       jQuery("#li_uploadfile").remove();
					   str = mstrs[i];
					   if(str == "") continue;
					   var ul=jQuery('#'+buttonId);
					   var imgstr = "<li class='file_li' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div class='showAffix'>"+str+"</div>";
					   #set($empId = $loginBean.id)
					   #if("$!taskBean.executor" =="$empId" || "$!taskBean.createBy" =="$empId" || "$!itemBean.createBy" =="$empId" || "$!itemBean.executor" =="$empId" || "$!taskBean.surveyor" == "$empId" || "$!mainTaskBean.createBy" =="$empId" || "$!mainTaskBean.executor" =="$empId")
					       imgstr += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"false\",\"OATask\",\"AFFIX\");'>删除</a>&nbsp;&nbsp;&nbsp;";
					   #end
					   
					   imgstr += "<a href=\"/ReadFile?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=false&type=AFFIX&tableName=OATask\" target=_blank>下载</a></li>";
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
		url: '/OATaskAction.do',
		data: "operation=3&taskId=$taskBean.id&type=affix&tempFile="+tempFile+"&fileName="+encodeURIComponent(fileName),
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

function showWeek(){
	$("#showWeekMap").show();
}

function cDayFunc(){
	cFunc('d');
}

function cYearFunc(){
	alert('y');
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

<input type="hidden" id="taskId" name="taskId" value="$!taskBean.id">
<input type="hidden" id="surveyor" name="surveyor" value="$!taskBean.surveyor">
<input type="hidden" id="today" name="today" value='$globals.getHongVal("sys_date")'>
<input type="hidden" id="textBoxValues" name="textBoxValues" value='$!textBoxValues'>
<div class="wrap"  id="wrap">
#set($userId = $loginBean.id)

#set($isEditStatus = "false")
#if("$!taskBean.executor" =="$userId" || "$!taskBean.createBy" =="$userId")
#set($isEditStatus = "true")
#end

#set($isEditOperation = "fasle")
#if($isEditStatus == "true" && "$!taskBean.status" != "2")
	#set($isEditOperation = "true")
#end 

#if("$!taskBean.status" == "2")
<div class="pa pa-statu"></div>
#end
	<!--  项目顶部dHeader 分割线Start-->
	<div class="dHeader clear">
    	<div class="dHeaderL">
        	<b class="icons projectPhoto"></b>
            <div class="lf proD">
            	<div class="wp-dd">
	         		<span class="No-color">NO.$!taskBean.taskOrder</span>
	            	#if($isEditOperation == "true")
	            		<span class="pr pro-title">
	            			#set($encodeTitle = $globals.encodeHTML2($!taskBean.title))
	            			<input type="text" class="proN" id="taskTitle" placeholder="编辑任务名称" title="点击修改"  value="$encodeTitle" defValue="$encodeTitle" fieldName="title" onchange="updSingleField(this);"/>
	            			<em class="pa t-task">
	            				<b class="icons arrow-point"></b>
	            				$encodeTitle
	            			</em>
	            		</span>
	            	#else
	            		<p class="pr pro-title">
	            			<i class="proN">$!taskBean.title</i>
	            			<em class="pa t-task">
	            				<b class="icons arrow-point"></b>
	            				$!taskBean.title
	            			</em>
	            		</p>
	            	#end
            	</div>
                <div class="rWp">
                	#if("$!taskBean.status" != "2" && "$!taskBean.createBy" == "$!loginBean.id")
                		<span class="lf pr proT">
	                		<input type="text" class="lf time-txt" id="taskBeginTime" value='$!taskBean.beginTime' defValue="$!taskBean.beginTime" title="$!taskBean.beginTime" fieldName="beginTime" onClick="selectDate('taskBeginTime')" readonly="readonly" />
	                		<em class="lf">至</em>
	                		<input type="text" class="lf time-txt" id="taskEndTime" value='$!taskBean.endTime' defValue="$!taskBean.endTime" title="$!taskBean.endTime" fieldName="endTime" onClick="selectDate('taskEndTime')" readonly="readonly"/>
	                	</span>
                	#else
                		<span class="lf pr proT">
	                		<i class="lf time-txt"  title="$!taskBean.beginTime">$!taskBean.beginTime</i>
	                		<em class="lf">至</em>
	                		<i class="lf time-txt" title="$!taskBean.endTime">$!taskBean.endTime</i>
	                	</span>
                	#end
                	
                	#if("$!taskBean.status" != "2")
						#set($timeDiff= $globals.getTimeDiff($taskBean.endTime,$globals.getHongVal("sys_date")))
						#set($timeDiff = $math.toNumber($timeDiff))
						#if($timeDiff>0)
							<span class="lf cDown" id="taskTimeDiff">剩余<i>$timeDiff</i>天</span>
						#elseif($timeDiff==0)
							<span class="lf cDown" id="taskTimeDiff">今天到期<i></i></span>
						#else
							<span class="lf cDown" id="taskTimeDiff">超过<i>$math.abs($timeDiff)</i>天</span>
						#end
						
						#if("$!taskBean.executor" == "$!loginBean.id")
		                    <span class="lf pr sR" >
			                    <b class="pa icons" title="提醒设置" id="setWarn"></b>
			                    <b id="alertTimeShow" class="pr">
			                    #if("$!taskBean.nextAlertTime" !="")
			                    	<span title='提醒时间:$!taskBean.nextAlertTime'>已设提醒</span>
			                    	<b class="delAlert" actionName="OATaskAction" keyId="$!taskBean.id"></b>
										#end
			                    </b>
			                    <div id="show_t" style="display: none;">
								    <input type="text" id="alterDay" name="alterDay" #if("$!alertDate" == "") value='$globals.getHongVal("sys_date")' #else value="$!alertDate" #end onClick="WdatePicker({lang:'$globals.getLocale()'})" readonly="readonly" style="width:70px"/>
									    <select id="alterHour" name="alterHour">
									    	#foreach($hour in [0..23])
										    	#if($hour < 10)		    			    	
										    		<option #if("$hour" == "$!alertHour") selected="selected" #end>0$!hour</option>		    	
										    	#else
										    		<option>$!hour</option>
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
                    <div class="clear"></div>
                </div>
                #if($isEditOperation == "true")
	                <span class="pr remark-span r-detail">
	                	<input type="text" class="lf time-txt" id="remarkInput" title="点击修改" value='$!taskBean.remark' placeholder="编辑详情描述"/>
	                	<div class="pa e-up d-hover" id="remarkDiv">
	                		<b class="icons arrow-point"></b>
	                		<i>$!taskBean.remark</i>
	                	</div>
	                	<div class="pa e-up d-txt" style="display:none;" id="remarkEditDiv">
	                		<b class="icons arrow-point"></b>
	                		<textarea id="remarkTextarea" title="$!taskBean.remark">$!taskBean.remark</textarea>
	                		<b class="pa btn-blue" onclick="updDetailRemark('OATaskAction','$!taskBean.id');" >确定</b>
	                		<b class="pa qx remarkEditCancel">取消</b>
	                	</div>
	                </span>
                #else
                	<span class="pr remark-span r-detail">
	                	<i class="lf time-txt">$!taskBean.remark</i>
	                	<div class="pa e-up d-hover" id="remarkDiv"><b class="icons arrow-point"></b>$!taskBean.remark</div>
	                </span>
                #end
                
                <input type="hidden" id="taskClientId" name="taskClientId" value="$!taskBean.clientId">
                #if("$!taskBean.clientId" !="")
	                <div class="rWp">
		                <span class="remark-span">
		                	关联客户名称：<a onclick="clientDetail('$!taskBean.clientId','$clientName')" href="#">$clientName</a>
		                </span>
	                </div>
	                
                #end
                
                #if("$!mainTaskBean" !="")
	                #set($allowEmpIds = ","+$!mainTaskBean.createBy+","+$!mainTaskBean.executor+","+$!mainTaskBean.participant)
	               	#set($empIdStr = ","+$!loginBean.id+",")
	                #if($allowEmpIds.indexOf($empIdStr)>-1)
	                <div class="rWp">
		                <span class="remark-span">
		                	主任务名称：<a onclick="taskDetail('$mainTaskBean.id','$mainTaskBean.title')" itemid="$mainTaskBean.id" href="#">$mainTaskBean.title</a>
		                </span>
	                </div>
	                #end
                #end
                
                #if("$!itemBean" !="")
                <div class="rWp">
	                <span class="remark-span">
	                	关联项目名称：<a onclick="itemDetail(this)" itemid="$itemBean.id" href="#">$itemBean.title</a>
	                </span>
                </div>
                #end
                
                #set($colorBarWidth = "0")
                #if("$!taskBean.schedule" !="")
                	#set($colorBarWidth = $!taskBean.schedule)
                #end
                
                #set($editSchedule = "")
                #set($cursorStr = "")
                #if($isEditOperation == "true")
                	#set($editSchedule = "changeSchedule")
                	#set($cursorStr = "cursor:pointer;")
                #end
                <div class="d-pace-wp" style="clear:both;">
                	<em class="lf">进度：</em>
	                <div class="ui-slider" style="width:176px;margin:10px 0 0 10px;float:left;" actionName="OATaskAction">
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
			            <div class="clear"></div>
			        </div>
			        
		        </div>
            </div>
        </div>
        <div class="dHeaderR">
        	#if("$isEditStatus" == "true" || "$!taskBean.surveyor" == "$userId")
        		#if("$taskBean.status" == "3")
        			<span class="finish surveyor pr" title="任务验收中">验收中





        			#if("$!taskBean.surveyor" == "$userId")
        				<div class="d-opac-btn">
	                    	<b class="opac-btn surveyor-pass "isDetail="true" status="2" onclick="showFeedback('$taskBean.id',this);">通过</b>
	                    	<b class="opac-btn surveyor-back "isDetail="true" status="1" onclick="showFeedback('$taskBean.id',this);">退回</b>
                    	</div>
        			#end
        			</span>
        		#elseif("$taskBean.status" == "2")
        			<span class="finish pr" onclick="showFeedback('$taskBean.id',this);" status="1" isDetail ="true" title="点击将重新启动任务"><b class="pa icons"></b>重新启动</span>
        		#else
        			#set($statusVal = "2")
        			#if("$!taskBean.surveyor" != "" && "$!taskBean.surveyor" != "$userId")
        				#set($statusVal = "3")
        			#end
        			<span class="finish pr" onclick="showFeedback('$taskBean.id',this);" status="$statusVal" isDetail ="true" title="点击将任务标为完成"><b class="pa icons"></b>完成</span>
        		#end
        	#end
        	
            <div class="tPerson">
            	#if("$!taskBean.createBy" !="$taskBean.executor")
            	<div class="party">
            	<span class="charge">
                    <em class="lf">创建人：</em>
                    <i class="lf" id="showExecutor">$!globals.getEmpFullNameByUserId($taskBean.createBy)</i>
                    
                </span>
                </div>
                #end
            	<span class="charge">
                    <em class="lf">负责人：</em>
                    <i class="lf" id="showExecutor">$!globals.getEmpFullNameByUserId($taskBean.executor)</i>
                    #if($isEditOperation == "true")
                   		<b class="icons updateSurveyor" onclick="publicPopSelect('userGroup','changeExecutor','changeExecutor','false')" title="修改负责人"></b>
                    #end
                </span>
                #if("$!taskBean.surveyor" != "" )
                <div class="party">
	                <span class="charge">
	                    <em class="lf">验收人：</em>
	                    <i class="lf" id="showSurveyorName">$!globals.getEmpFullNameByUserId($!taskBean.surveyor)</i>
	                    #if("$!taskBean.createBy" =="$loginBean.id")
	                   		<b class="icons updateSurveyor" onclick="publicPopSelect('userGroup','changeSurveyor','changeSurveyor','false')" title="修改验收人"></b>
	                    #end
	                </span>
                </div>
                #end
                
                <div class="party pr participat">
                    <em class="lf">参与人：</em>
                    
                    <span class="lf" id="participants"> </span><!-- <span class="participantMore">更多</span> -->
                    <div class="clear"></div>
                    <div id="participantsDiv" style="display: none;"> </div>
                </div>
            </div>
            <div class="create-time">创建时间：$taskBean.createTime</div>
        </div>
    </div>
    
    <!--  项目顶部dHeader 分割线End-->
    <ul class="uNav">
    	<li id="tabTaskManager" class="sel">任务管理</li>
        <li id="tabAffix">附件</li>
        <li id="tabParticipant">人员</li>
    </ul>
    
    
    <!-- 附件开始-->
	    <div class="mission" id="tabAffixShow" style="display: none;">
			<div class="pr">
				<em></em>
				<span id="picbutton" name="picbutton"  onClick="upload('AFFIX','uploadBtn');">上传</span>
			</div>											
				<ul id="affixuploadul_uploadBtn">
					#foreach($uprow in $!taskBean.affix.split(";"))
						#if($uprow != "")
							<li class="file_li" id="uploadfile_$uprow">
							<input type=hidden id="uploadBtn" name=uploadBtn value='$uprow'/><div class="showAffix">$uprow</div>
								#if("$!taskBean.executor" == "$loginBean.id" || "$!taskBean.createBy" == "$loginBean.id")&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload("$uprow","false","OATask","AFFIX");'>$text.get("common.lb.del")</a>&nbsp;&nbsp;#end
								<a href="/ReadFile?fileName=$globals.urlEncode($uprow)&realName=$globals.urlEncode($uprow)&tempFile=false&type=AFFIX&tableName=OATask" target="_blank">$text.get("common.lb.download")</a>
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
		    		/*if(files[i].size>52428800){
		    			asyncbox.alert("上传的文件不能超过50M");
		    			return false;
		    		}*/
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
    
    <!--  人员Person 分割线End-->
    <div class="mission Person" style="display:none;" id="tabParticipantShow">
    	<p id="participantCount"></p>
        <div class="pListWp">
        	<ul class="party">
        		<li class="mesOnline" empId="$taskBean.executor">
	            	<a href="javascript:top.msgCommunicate('$!taskBean.executor','$!globals.getEmpFullNameByUserId($!taskBean.executor)');" class="lf Image"><img src="$globals.checkEmployeePhoto("48",$taskBean.executor)" /></a>
	                <i class="lf"><b>$!globals.getEmpFullNameByUserId($taskBean.executor)</b>（负责人）</i>
                </li>
                #if("$!taskBean.createBy" !="$taskBean.executor")
                <li class="mesOnline" empId="$taskBean.createBy">
                	<a href="javascript:top.msgCommunicate('$!taskBean.createBy','$!globals.getEmpFullNameByUserId($!taskBean.createBy)');" class="lf Image"><img src="$globals.checkEmployeePhoto("48",$!taskBean.createBy)" /></a>
	                <i class="lf"><b>$!globals.getEmpFullNameByUserId($!taskBean.createBy)</b>（创建人）</i>
	            </li>
                #end
                
                #if("$!taskBean.surveyor" != "")
                <li class="mesOnline" empId="$taskBean.surveyor">
	                <a href="javascript:top.msgCommunicate('$!taskBean.surveyor','$!globals.getEmpFullNameByUserId($!taskBean.surveyor)');" class="lf Image"><img src="$globals.checkEmployeePhoto("48",$!taskBean.surveyor)" /></a>
	                <i class="lf"><b>$!globals.getEmpFullNameByUserId($!taskBean.surveyor)</b>（验收人）</i>
               	</li>
                #end
            </ul>
            #if($isEditOperation == "true")
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
            	#foreach($employeeId in $taskBean.participant.split(","))
            		#if("$!employeeId" !="" && "$!employeeId" !="$taskBean.executor" && "$!employeeId" !="$!taskBean.createBy" && "$!employeeId" !="$!taskBean.surveyor")
		            	<li empId="$!employeeId" class="mesOnline">
		                	<a href="javascript:top.msgCommunicate('$employeeId','$!globals.getEmpFullNameByUserId($employeeId)');" class="lf Image"><img src="$globals.checkEmployeePhoto("48",$employeeId)" /></a>
		                    <i class="lf"><b>$!globals.getEmpFullNameByUserId($employeeId)</b></i>
		                    #if($isEditOperation == "true")
		                    <b class="icons b-del-t"></b>
		                    #end
		                </li>
            		#end
            	#end
                #if($isEditOperation == "true")
                <li class="addL pr" onclick="publicPopSelect('userGroup','taskTitle','participantId','true')" empId="addParticipant">
                	<b class="icons pa" ></b>
                </li>
                #end
            </ul>
        </div>
    </div>
    <!--  人员Person 分割线End-->
    
    <!-- 评论开始-->
    <div class="mission" id="tabTaskManagerShow">
    	<div class="task">
    		<!-- 待完成任务 Start -->
	        <div class="tasking">
	        	<p class="subhead">
	            	<b class="lf icons"></b>
	                <i class="lf">待完成任务</i>
	            </p>
	            <ul class="uTasking" id="runTaskUl">
	            	#foreach($task in $runTaskList)
	            	
	            	#set($isShowPoint = "")
	            	#if("$!globals.get($task,11)" == "$userId" || "$!globals.get($task,2)" == "$userId")
	            		#set($isShowPoint = "showAppoint")
	            	#end
	            	
	            	<li id="$globals.get($task,0)">
	            		#if("$!globals.get($task,11)" == "$userId")
		            		<div class="d-oper">
		            			<b class="b-del icons" title="删除"></b>
		            			<!--  <b class="b-update icons" title="修改"></b>  -->
		            		</div>
	            		#end
	                    <span class="tContent">$globals.get($task,1)</span>
                   		#if("$!globals.get($task,2)" == "") <span class="sAppoint $isShowPoint">未指派 </span> #else <span class="sFinish $isShowPoint">$globals.getEmpFullNameByUserId($!globals.get($task,2)) $!globals.get($task,5)</span> #end
	                    <span class="sFinish">#if("$!globals.get($task,6)" != "0") $!globals.get($task,6)条评论 #end</span>
	                    
	                    
	                    #if("$!globals.get($task,2)" == "$userId" || "$!globals.get($task,7)" == "$userId" || "$!globals.get($task,11)" == "$userId")
	                    	#if("$!globals.get($task,2)" != "")
		                    	#if("$!globals.get($task,3)" == "1")
		                    	 	#set($statusVal = "2")
		                    	 	#if("$!globals.get($task,7)" != "" && "$!globals.get($task,7)" != "$userId")
		                    	 		#set($statusVal = "3")
		                    	 	#end
		                    	 	<span class="updTaskStatus" status="$statusVal">完成</span>
		                    	#else
		                    		<span class="sFinish" >
			                    		验收中





			                    		#if("$!globals.get($task,7)" != "$userId")
			                    		<div class="d-opac-btn">
					                    	<b class="opac-btn surveyor-pass updTaskStatus" status="2">通过</b>
					                    	<b class="opac-btn surveyor-back updTaskStatus" status="1">退回</b>
				                    	</div>
				                    	#end
			                    	</span>
		                    	#end
	                    	#end
	                    #end
	                </li>
	                #end
	            </ul>
	            <div class="in-add-task">
	            	#if(("$userId" == "$taskBean.executor" || "$userId" == "$taskBean.createBy") && "$!taskBean.status" != "2") 
	            	<span class="add-span">添加子任务</span>
	            	#end
	                <div class="d-task">
	                	<textarea class="d-area" id="fastTitle" name="fastTitle"></textarea>
	                    <div>
	                    	<span class="save-add-btn" onclick="fastTaskSubmit('true',this)">保存，继续添加</span>
	                        <span class="cancel-btn">取消</span>
	                        <span class="appoint-span">
	                        	<em class="lf">负责人:</em>
	                            <select class="lf" id="fastExecutor" name="fastExecutor"></select>
	                        </span>
	                        <span class="end-time">
	                        	<em class="icons lf warn" title="默认截止时间为主任务结束时间"></em>
	                        	<em class="lf">截止时间:</em>
	                            <span class="inpnt-child">
	                            	<input type="text" id="fastEndTime" name="fastEndTime" value="$!taskBean.endTime" defValue="$!taskBean.endTime" isAddChild="true" onclick="WdatePicker({lang:'$globals.getLocale()'})" />
	                            	<b class="icons time"></b>
	                            	<ul class="pa uRemind" id="showWeekMap" style="display:none;">
	                            		#foreach ($maps in $weekMap.keySet())
	                            		<li value='$weekMap.get("$maps")'>$maps</li>
	                            		#end
				                        <li value='set'>指定时间</li>
		                            </ul>
		                            <div class="clear"></div>
	                            </span>
								
	                        </span>
	                        
	                    </div>
	                </div>
	                <div class="clear"></div>
	            </div>
	        </div>
	        <!-- 待完成任务 End -->
	        <!-- 已完成任务 Start -->
	        <div class="tasked">
	        	<p class="subhead">
	            	<b class="lf icons"></b>
	                <i class="lf">已完成任务</i>
	            </p>
	            <ul class="uTasked" id="finishTaskUl">
	            	#foreach($task in $finishTaskList)
	            	<li id="$globals.get($task,0)">
	                    <span class="tContent">$globals.get($task,1)</span>
                   		<span class="sAppoint">#if("$!globals.get($task,2)" == "")未指派 #else <span class="sFinish">$globals.getEmpFullNameByUserId($!globals.get($task,2)) $!globals.get($task,5)</span> #end</span>
	                    <span class="sFinish">#if("$!globals.get($task,6)" != "0") $!globals.get($task,6)条评论 #end</span>
	                </li>
	                #end
	            </ul>
	        </div>
	        <!-- 已完成任务 End -->
    	</div>
    	<ul class="f-nav">
	    	<li class="sel">动态</li>
	    </ul>
    	<input type="hidden" id="parentIframeName" name="parentIframeName" value="tabDiscussIframe"/>
    	<iframe frameborder="0" scrolling="0" width="100%" height="330px;" id="tabDiscussIframe" src="/DiscussAction.do?tableName=OATaskLog&f_ref=$taskBean.id&parentIframeName=tabDiscussIframe"></iframe>
    </div>
    <!--  评论结束-->
    <!-- 弹出评论层 Start -->
    <div class="addWrap pop-layer" id="addFeedbackDiv" style="top: 31px; left: 369px; display:none;"></div>
	<div class="addWrap pop-layer" id="addChildFeedbackDiv" style="position:fixed;top:200px; left:450px; display:none;"></div>
    <!-- 弹出评论层 End -->
    <!-- 收藏按钮 Start-->
	<b class="join-collect icons" #if("$!attentionCard" == "attentionCard") style="background-position:-236px -126px;" title="取消收藏" bg="f" #else title="加入收藏" bg="t" #end  onclick="getCollection('$taskBean.id','任务:$!taskBean.title','/OATaskAction.do?operation=5&taskId=$taskBean.id','OATask');"></b>
	<!-- 收藏按钮 End-->
</div>
<div class="add-calendar" id="pointEmpDiv" style="display:none;" ></div>
<div id="hideBg" class="hideBg"></div>
<script type="text/javascript">
	var sHeight=document.documentElement.clientHeight;
	$("#scroll-wrap").height(sHeight);
	$("#wrap").css("min-height",sHeight-15);
</script>

</div>
</body>
</html>
