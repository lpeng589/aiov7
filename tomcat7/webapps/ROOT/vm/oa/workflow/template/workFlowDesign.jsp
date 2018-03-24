<!DOCTYPE html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>流程设计-$!{flowDisplay}</title>
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet" />
<link type="text/css" href="/style/css/flow-design.css" rel="stylesheet" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/Map.js"></script>
<script language="javascript" src="/js/jquery.json-2.4.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script language="javascript"></script>
<script type="text/javascript"> 
var moduleType = "$!moduleType";
function closeWin(){
	var fileName=$("#fileName").val();
	fileName=fileName.replace(".xml","");
	window.opener.repVal(fileName);

}

function onCompleteUpload(str){
    alert(str);
}

/*清除所有审核结点*/
function onClear(){
	nodeMap.clear();
}

/*删除一个审核结点*/
function onDelNode(actName,nodeId,endNodeId){
	//alert(actName);
	//$("#display_div").html(actName+"---"+nodeId+"-----"+endNodeId);
	if(actName.indexOf("FlowNode")==0 || actName.indexOf("ChoiceNode")==0 
	 	 || actName.indexOf("StartNode")==0 || actName.indexOf("EndNode")==0){
	 	nodeMap.remove(nodeId);	
	}else if(actName.indexOf("LineNode")==0){
		nodeMap.get(nodeId).to = "";
	}else if("LineChoice"==actName){
	 	var node = nodeMap.get(nodeId);
	 	if(typeof(node.conditionList)!="undefined"){
		 	jQuery.each(node.conditionList,function(index,cond){
				if(endNodeId == cond.to){
					node.conditionList.remove(index);
				}
			});
		}
	}
}

/*js选择flash某结点*/
function selectNode(noteId){
	obj = document.getElementById("flowDesignId");
	obj.selectNode(noteId);
}

/*复制一个审核结点*/
function onCopy(actName,nodeId,endNodeId){
	//$("#display_div").html(actName+"---"+nodeId+"-----"+endNodeId);
	var nNode = {};
	nNode = jQuery.extend(true, {},nodeMap.get(nodeId));
	nNode.keyId = endNodeId ;
	nNode.code = endNodeId;
	nNode.to = "";
	nodeMap.put(endNodeId,nNode)
}

/*连线*/
function onConnect(nodeId){
	var nodeIds = nodeId.split(";")
	var snode = nodeMap.get(nodeIds[0]);
	var enode = nodeMap.get(nodeIds[1]);
	if(typeof(enode)=="undefined" || enode==null){
		var node = {};
		node.keyId = nodeIds[1];
		if(node.keyId=="0"){
			node.zAction = "START";
			node.display = "开始";
		}else if(node.keyId=="-1"){
			node.zAction = "STOP";
			node.display = "结束";
		}
		nodeMap.put(node.keyId,node);
	}
	if(typeof(snode)=="undefined" || snode==null){
		var node = {};
		node.keyId = nodeIds[0];
		if(node.keyId=="0"){
			node.zAction = "START";
			node.display = "开始";
		}else if(node.keyId=="-1"){
			node.zAction = "STOP";
			node.display = "结束";
		}
		node.to = nodeIds[1];
		nodeMap.put(node.keyId,node);
	}else{
		snode.to = nodeIds[1];
		if(snode.zAction == "CHOICE"){
			lineCond = {};
			lineCond.to = nodeIds[1];
			lineCond.toCode = nodeIds[1];
			lineCond.conditions = new Array();
			if(typeof(snode.conditionList)=="undefined"){
				snode.conditionList = new Array();
			}
			snode.conditionList.push(lineCond);
		}
	}
}


/*功能函数调用*/
function onFunction(actName){
	 $("#uploadFile").click();
}

/*上传工作流文件*/
function uploadFlowFile(){
	form.submit();
}

/*选中某个结点*/
function onSelectNode(actName,nodeId,endNodeId){
	//$("#display_div").html(actName+"---"+nodeId+"-----"+endNodeId);
	var node = nodeMap.get(nodeId);
	if(node == null){
		node = {};
		node.keyId = nodeId;
		node.zAction = actName;
		nodeMap.put(nodeId,node);
	}
	$("#nodeAttrId").attr("nodeId",nodeId);
	if("START"==actName){
		node.display = "开始";
		node.zAction = "START";
		$("#nodeAttrId").html("").html($("#startHtml").html());
		/*字段设置*/
		if(typeof(node.fields)!="undefined"){
			jQuery.each(node.fields,function(i){
				var field = node.fields[i];
				var type = "isNotNull" ;
				if(field.inputType>0){ type = field.inputType==3 ? "hidden":"readOnly"};
				$("input[name='"+field.fieldName+"'][value='"+type+"']").attr("checked",true);
			});
		}
		/*高级 自定义标识*/
		$("#passExec").val(node.passExec);
	}else if("STOP"==actName){
		node.display = "结束";
		node.zAction = "STOP";
		$("#nodeAttrId").html("");
	}else if("CHOICE"==actName){
		$("#nodeAttrId").html("").html($("#choiceHtml").html());
		$("#display").val(node.display);
		node.zAction = "CHOICE";
	}else if("LINECHOICE"==actName){
		
		$("#nodeAttrId").html("").html($("#lineHtml").html());
		if(typeof(node.conditionList)=="undefined"){
			node.conditionList = new Array();
		}
		var lineCond = null
		if(node.conditionList.length>0){
			jQuery.each(node.conditionList,function(i){
				var condition = node.conditionList[i];
				if(endNodeId == condition.to){
					$("#display").val(condition.display);
					$("#display").attr("to",condition.to)
					lineCond = condition;
					jQuery.each(condition.conditions,function(index,cond){
						var groupUl = $('.condition_ul[groupId="'+cond.groupId+'"]')
						if(groupUl.size()>0){
							addCondition(cond);
						}else{
							conditionGroup(cond.groupId);
							 $('.condition-group-ul select[groupId="'+cond.groupId+'"]').val(cond.groupType);
							addCondition(cond);
						}
					});
					if(condition.conditions.length==0){
						var conditionHtml = $("#condition_group").html();
						conditionHtml = conditionHtml.replaceAll("#groupId#",getRandom());
						$("#condition_group_div").html(conditionHtml);
					}
				}
			});
		}
		if(lineCond==null){
			var conditionHtml = $("#condition_group").html();
			conditionHtml = conditionHtml.replaceAll("#groupId#",getRandom());
			$("#condition_group_div").html(conditionHtml);
			$("#display").attr("to",endNodeId)
			
			lineCond = {};
			lineCond.to = endNodeId;
			lineCond.toCode = endNodeId;
			lineCond.conditions = new Array();
			node.conditionList.push(lineCond);
		}
	}else if("CHECK"==actName){
		$("#nodeAttrId").html("").html($("#nodeHtml").html());
		node.zAction = "CHECK";
		$("#display").val(node.display);
		/*时间设置 提醒设置*/
		if(node.forwardTime){
			$("#timeLimit").parents(".b-block").hide();
		}else{
			$("#timeLimit").parents(".b-block").show();
		}
		$("#forwardTime").attr("checked",node.forwardTime);
		
		$("#timeLimit").val(node.timeLimit);
		$("#timeLimitUnit").val(node.timeLimitUnit);
		$("#noteTime").val(node.noteTime);
		$("#noteTimeUnit").val(node.noteTimeUnit);
		$("#noteRate").val(node.noteRate);
		$("#noteRateUnit").val(node.noteRateUnit);
		
		/*允许滤过 允许撤回 允许全审 会签意见必填*/
		$("#allowBack").attr("checked",node.allowBack);
		$("#allowStop").attr("checked",node.allowStop);
		$("#allowJump").attr("checked",node.allowJump);		
		$("#allowCancel").attr("checked",node.allowCancel);	
		$("#useAllApprove").attr("checked",node.useAllApprove);
		$("#ideaRequired").attr("checked",node.ideaRequired);
		
		/*独力设置提醒方式*/
		$("#standaloneNoteSet").attr("checked",node.standaloneNoteSet);
		$("#nextSMS").attr("checked",node.nextSMS);
		$("#nextMobile").attr("checked",node.nextMobile);
		$("#nextMail").attr("checked",node.nextMail);		
		$("#startSMS").attr("checked",node.startSMS);	
		$("#startMobile").attr("checked",node.startMobile);
		$("#startMail").attr("checked",node.startMail);
		$("#allSMS").attr("checked",node.allSMS);	
		$("#allMobile").attr("checked",node.allMobile);
		$("#allMail").attr("checked",node.allMail);
		$("#setSMS").attr("checked",node.setSMS);	
		$("#setMobile").attr("checked",node.setMobile);
		$("#setMail").attr("checked",node.setMail);
		if(typeof(node.notePeople)!="undefined"){
			jQuery.each(node.notePeople,function(i){
				var note = node.notePeople[i];
				addApprove('sel-con-ul',note.userName,note.user,note.typeName,note.type);
			});
		}
		
		/*审核人设置*/
		$("#filterSet").val(node.filterSet);
		$("#autoSelectPeople").val(node.autoSelectPeople);
		if($("#filterSet").val()==5 || $("#filterSet").val()==6){
			$("#filterSetSQLDiv").show();
		}else{
			$("#filterSetSQLDiv").hide();
		}
		if($("#autoSelectPeople").val()==3){
			$("#autoSelectPeopleSQLDiv").show();
		}else{
			$("#autoSelectPeopleSQLDiv").hide();
		}
		
		$("#filterSetSQL").val(node.filterSetSQL);
		$("#autoSelectPeopleSQL").val(node.autoSelectPeopleSQL);
		
		$("input[name='approvePeople'][value='"+node.approvePeople+"']").attr("checked",true);
		if(typeof(node.approvers)!="undefined"){
			jQuery.each(node.approvers,function(i){
				var app = node.approvers[i];
				addApprove('list-ul',app.userName,app.user,app.typeName,app.type);
			});
		}
		
		/*字段设置*/
		if(typeof(node.fields)!="undefined"){
			jQuery.each(node.fields,function(i){
				var field = node.fields[i];
				var type = "isNotNull" ;
				if(field.inputType>0){ type = field.inputType==3 ? "hidden":"readOnly"};
				$("input[name='"+field.fieldName+"'][value='"+type+"']").attr("checked",true);
			});
		}
		/*高级 自定义标识*/
		$("#passExec").val(node.passExec);
		$("#backExec").val(node.backExec);
		$("#stopExec").val(node.stopExec);
	}else{
		$("#nodeAttrId").html("");
	}
	

	//$("input[type='text'],[type='checkbox'],[type='radio'],select").change( function() {
	$("input,select").change(function() {
		if($(this).attr("class") == "select-field"){
			/*字段设置的复选只能选一个*/
			var ischecked = $(this).is(":checked");
			$('input[name="'+$(this).attr("name")+'"]').attr('checked',false);
			$(this).attr("checked",ischecked);
		}
		
	  	if("START"==actName){
	  		/*字段设置*/
			if($(this).attr("class") == "select-field"){
				node.fields = addCheckField(node.fields);
			}
			var strId = $(this).attr("id");
			/*高级 自定义标识*/
			if(strId == "passExec"){
				node.passExec = $("#passExec").val();
			}
	  	}else if("CHOICE"==actName){
			node.display = $("#display").val();
		}else if("LINECHOICE"==actName){
			jQuery.each(node.conditionList,function(i){
				var condition = node.conditionList[i];
				if(endNodeId == condition.to){
					condition.display = $("#display").val();
					
				}
			});
		}else if("CHECK"==actName){
			var strId = $(this).attr("id");
			if(strId=="display"){
				node.display = $(this).val();
			}else if(strId=="forwardTime"){
				node.forwardTime = $("#forwardTime").is(":checked");
			}else if(strId=="timeLimit"){
				node.timeLimit = $("#timeLimit").val();
			}else if(strId=="timeLimitUnit"){
				node.timeLimitUnit = $("#timeLimitUnit").val();
			}else if(strId=="noteTime"){
				node.noteTime = $("#noteTime").val();
			}else if(strId=="noteTimeUnit"){
				node.noteTimeUnit = $("#noteTimeUnit").val();
			}else if(strId=="noteRate"){
				node.noteRate = $("#noteRate").val();
			}else if(strId=="noteRateUnit"){
				node.noteRateUnit = $("#noteRateUnit").val();
			}else if(strId=="allowBack"){
				node.allowBack = $("#allowBack").is(":checked");
			}else if(strId=="allowStop"){
				node.allowStop = $("#allowStop").is(":checked");
			}else if(strId=="allowJump"){
				node.allowJump = $("#allowJump").is(":checked");
			}else if(strId=="allowCancel"){
				node.allowCancel = $("#allowCancel").is(":checked");
			}else if(strId=="useAllApprove"){
				node.useAllApprove = $("#useAllApprove").is(":checked");
			}else if(strId=="ideaRequired"){
				node.ideaRequired = $("#ideaRequired").is(":checked");
			}
			
			/*独力设置提醒方式*/
			else if(strId == "standaloneNoteSet"){
				node.standaloneNoteSet = $("#standaloneNoteSet").is(":checked");
			}else if(strId == "nextSMS"){
				node.nextSMS = $("#nextSMS").is(":checked");
			}else if(strId == "nextMobile"){
				node.nextMobile = $("#nextMobile").is(":checked");
			}else if(strId == "nextMail"){
				node.nextMail = $("#nextMail").is(":checked");
			}else if(strId == "startSMS"){	
				node.startSMS = $("#startSMS").is(":checked");
			}else if(strId == "startMobile"){
				node.startMobile = $("#startMobile").is(":checked");
			}else if(strId == "startMail"){
				node.startMail = $("#startMail").is(":checked");
			}else if(strId == "allSMS"){
				node.allSMS = $("#allSMS").is(":checked");
			}else if(strId == "allMobile"){
				node.allMobile = $("#allMobile").is(":checked");
			}else if(strId == "allMail"){
				node.allMail = $("#allMail").is(":checked");
			}else if(strId == "setSMS"){
				node.setSMS = $("#setSMS").is(":checked");
			}else if(strId == "setMobile"){	
				node.setMobile = $("#setMobile").is(":checked");
			}else if(strId == "setMail"){
				node.setMail = $("#setMail").is(":checked");
			}
			
			/*审核人设置*/
			else if(strId == "filterSet"){
				node.filterSet = $("#filterSet").val();
			}else if(strId == "autoSelectPeople"){
				node.autoSelectPeople = $("#autoSelectPeople").val();
			}else if(strId == "filterSetSQL"){
				node.filterSetSQL = $("#filterSetSQL").val();
			}else if(strId == "autoSelectPeopleSQL"){
				node.autoSelectPeopleSQL = $("#autoSelectPeopleSQL").val();
			}else if($(this).attr("name") == "approvePeople"){
				node.approvePeople = $("input[name='approvePeople']:checked").val();
			}
			
			/*字段设置*/
			else if($(this).attr("class") == "select-field"){
				node.fields = addCheckField(node.fields);
			}
			
			/*高级 自定义标识*/
			else if(strId == "passExec"){
				node.passExec = $("#passExec").val();
			}else if(strId == "backExec"){
				node.backExec = $("#backExec").val();
			}else if(strId == "stopExec"){
				node.stopExec = $("#stopExec").val();
			}
			//$("#display_div").html(node.autoSelectPeople);
		}
		//alert(jQuery.toJSON(node));
	});
	
	
	/*Tab切换*/
	$(".tags-btn .tag-s").click(function(){
		$(this).removeClass("tag-sel").addClass("tag-sel").siblings("span").removeClass("tag-sel");
		$("."+$(this).attr("show")).show().siblings("div").hide();
	});
	/*点击转交时设置办理时间*/
	$('#forwardTime').click(function(){
	 	if($(this).is(":checked")){
			$("#timeLimit").val('').parents(".b-block").hide(); 		
	 	}else{
	 		$("#timeLimit").val('').parents(".b-block").show();
	 	}
	});
	
	/*独力设置提醒方式*/
	$("#remind_btn").click(function(){
		modal();
		$('#remind-id').show();
	});
	
	/*保密字段设置*/
	$("#hidden-btn").click(function(){
		$('.hidden-field-ul').html("");
		$('input:checkbox[value="hidden"][checked]').each(function(){
			$('.hidden-field-ul').append('<li><i class="border-right">'+$(this).attr("iTitle")+'</i><i class="short-i"><input class="hidden-checked" type="checkbox" name="'+$(this).attr("name")+'"/></i></li>');
		});
		if(node.hiddenFields!=null){
		jQuery.each(node.hiddenFields,function(i,field){
			$('input:checkbox[name="'+field.fieldName+'"]').attr("checked",true);
		});
		}
		modal();
		$('#hidden-id').show();
	});
	

	
	/*保密字段设置公开的复选*/
	$("#enPublic").click(function(){
		var ischecked = $(this).is(":checked");
		$('input:checkbox[class="hidden-checked"]').attr('checked',ischecked);
	});
	
	$("#publicBtn").click(function(){
		node.hiddenFields = [];
		$('input:checkbox[class="hidden-checked"][checked]').each(function(){
			node.hiddenFields.push({fieldName:$(this).attr("name"),endPublic:true});
		});
		$('#hidden-id').hide();
		clearModal();
	});
	
	/*字段全选*/
	$("#field_readonly").click(function(){
		$(".select-field").attr('checked',false);
		$('input[value="readOnly"]').attr('checked',$(this).is(":checked"));
		node.fields = addCheckField(node.fields);
		
		$("#field_hidden").attr("checked",!$(this).is(":checked"));
		$("#field_notnull").attr("checked",!$(this).is(":checked"));
	});
	$("#field_hidden").click(function(){
		$(".select-field").attr('checked',false);
		$('input[value="hidden"]').attr('checked',$(this).is(":checked"));
		node.fields = addCheckField(node.fields);
		
		$("#field_readonly").attr("checked",!$(this).is(":checked"));
		$("#field_notnull").attr("checked",!$(this).is(":checked"));
	});
	$("#field_notnull").click(function(){
		$(".select-field").attr('checked',false);
		$('input[value="isNotNull"]').attr('checked',$(this).is(":checked"));
		node.fields = addCheckField(node.fields);
		
		$("#field_readonly").attr("checked",!$(this).is(":checked"));
		$("#field_hidden").attr("checked",!$(this).is(":checked"));
	});
	
	/*职位弹出窗口*/
	$("#dutyId").click(function(){
		openTitle();
	});
	
	/*岗位弹出窗口*/
	$("#postId").click(function(){
		openPost();
	});
	
	/*职员弹出窗口*/
	$("#employeeId").click(function(){
		employeePopup('list-ul','userGroup');
	});
	/*部门弹出窗口*/
	$("#deptId").click(function(){
		employeePopup('list-ul','deptGroup');
	});
	/*职员分组弹出窗口*/
	$("#groupId").click(function(){
		groupPopup('list-ul');
	});
}

/*职位弹出窗口*/
function openTitle(inputType){
	var urls = '/vm/crm/client/titleOpenSel.jsp?inputType='+inputType ;
	asyncbox.open({
		id:'titleOpenId',title:'选择职位',url:urls,modal:true,width:300,height:290,right:200,top:200, btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
	　　　	if(action == 'ok'){
				var str = opener.returnVal();
				var arr = str.split("|") ;
				if("radio"==inputType){
					var field = arr[0].split(";") ;
					if(field!=""){
						$("#valueDisplay").val(field[1]).attr("title",field[0]);
					}
				}else{
					for(var j=0;j<arr.length;j++){
						var field = arr[j].split(";") ;
						if(field!=""){
							addApprove('list-ul',field[1],field[0],'职位','duty');
						}
					}
					changeApprove('list-ul');
				}
	　　　　}
	　	}
　	});
}

/*岗位弹出窗口*/
function openPost(inputType){
	var urls = '/vm/crm/client/titleOpenSel.jsp?strType=responsibility&inputType='+inputType ;
	asyncbox.open({
		id:'titleOpenId',title:'选择岗位',url:urls,modal:true,width:300,height:290,right:200,top:200, btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
	　　　	if(action == 'ok'){
				var str = opener.returnVal();
				var arr = str.split("|") ;
				if("radio"==inputType){
					var field = arr[0].split(";") ;
					if(field!=""){
						$("#valueDisplay").val(field[1]).attr("title",field[0]);
					}
				}else{
					for(var j=0;j<arr.length;j++){
						var field = arr[j].split(";") ;
						if(field!=""){
							addApprove('list-ul',field[1],field[0],'岗位','post');
						}
					}
					changeApprove('list-ul');
				}
　　　　　	}
	　　}
　	});
}

/*选项数据弹出窗口*/
function openEnumValue(enumValue){
	var urls = '/vm/crm/client/titleOpenSel.jsp?strType='+enumValue+'&inputType=radio';
	asyncbox.open({
		id:'titleOpenId',title:'选择岗位',url:urls,modal:true,width:300,height:290,right:200,top:200, btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
	　　　	if(action == 'ok'){
				var str = opener.returnVal();
				var arr = str.split("|") ;
				var field = arr[0].split(";") ;
				if(field!=""){
					$("#valueDisplay").val(field[1]).attr("title",field[0]);
				}
　　　　　	}
	　　}
　	});
}

/*添加分组条件*/
function conditionGroup(groupId){
	var conditionHtml = $("#condition_group").html();
	if(typeof(groupId)=="undefined"){
		groupId = getRandom();
	}
	conditionHtml = conditionHtml.replaceAll("#groupId#",groupId.trim());
	$("#condition_group_div").append(conditionHtml);
}

/*条件字段*/
function flowfield(){
	var urls = '/OAWorkFlowTempAction.do?queryType=flowfields&tableName=$!tableName&flowId=$!flowId'+"&moduleType="+moduleType ;
	asyncbox.open({
		id:'flowfields',title:'流程条件字段',url:urls,modal:true,width:500,height:410,right:200,top:100, btnsbar:jQuery.btn.OKCANCEL,
		callback : function(action,opener){
　　　　　	if(action == 'ok'){
				if(opener.submitBefore()==false){
					return false;
				}else{
					fillFields(opener.submitBefore());
				}
　　　　　	}
　　　	}
　	});
}

/*删除审核人*/
function delApprove(obj){
	var className = $(obj).parents("ul").attr("class");
	$(obj).parent().remove();
	changeApprove(className);
}

/*添加审核人*/
function addApprove(className,userName,user,typeName,type){
	if(typeof($('.'+className+' li[user="'+user+'"]').attr("userName"))=="undefined"){
		$('.'+className).append('<li userName="'+userName+'" user="'+user+'" typeName="'+typeName+'" type="'+type+'"><i class="border-right">'+typeName+'</i><i>'+userName+'</i><b class="b-del icons" title="删除" onclick="delApprove(this);"></b></li>');
	}
}

/*不管添加 还是删除 都先删除，然后重新生成结点审核人json对象*/
function changeApprove(className){
	var node = nodeMap.get($("#nodeAttrId").attr("nodeId"));
	if("list-ul"==className){
		node.approvers = new Array();
		jQuery.each($(".list-ul li"),function(){
			node.approvers.push({userName:$(this).attr("userName"),user:$(this).attr("user"),typeName:$(this).attr("typeName"),type:$(this).attr("type")});
		});
	}else{
		node.notePeople = new Array();
		jQuery.each($(".sel-con-ul li"),function(){
			node.notePeople.push({userName:$(this).attr("userName"),user:$(this).attr("user"),typeName:$(this).attr("typeName"),type:$(this).attr("type")});
		});
	}
}


/*字段设置中 添加选中的字段到json对象中*/
function addCheckField(fields){
	fields = new Array();
	console.log('addCheckField');
	$(".select-field").each(function(i){
		if($(this).is(":checked")){
			var type = "0" ;
			if($(this).attr("value") != "isNotNull") type = ($(this).attr("value")=="hidden"?"3":"8");
			var notnull = (type == "0" ? true:false);
			fields.push({fieldName:$(this).attr("name"),inputType:type,isNotNull:notnull})
		}
	});
	return fields;
}

/*获取随机数*/
function getRandom(){
	var rnd="";
	for(var i=0;i<6;i++)rnd+=Math.floor(Math.random()*10);
	return rnd;
}

/*添加条件*/
function conditionShow(groupId){
	modal();
	$("#condition_div").html($("#condition_add").html().replaceAll("#groupId#",groupId)).show();
}

/*添加条件*/
function delCondition(obj){
	$(obj).parent().remove();
	changeCondition();
}

/*条件设置 添加条件*/
function addCondition(condition){
	var condId = $("#fieldName").attr("condId");
	
	var condHtml = '<li id="'+getRandom()+'" groupId="'+condition.groupId+'" display="'+condition.display+'" fieldName="'+condition.fieldName+'" andOrDisplay="'+condition.display+'" andOr="'+condition.andOr+'" valueDisplay="'+condition.valueDisplay+'" valueStr="'+condition.value+'" relationDisplay="'+condition.relationDisplay+'" relation="'+condition.relation+'"><i class="long-width border-right" title="'+condition.display+'">'+condition.display+'</i><i class="border-right">'+condition.relationDisplay+'</i><i class="ct-width border-right">'+condition.valueDisplay+'</i><i>'+(andMap.get(condition.andOr))+'</i><b class="icons b-del" title="删除" onclick="delCondition(this);"></b><!--<b class="icons b-update" title="修改" onclick="updateCondition(this)"></b>--></li>';
	if(typeof(condId)=="undefined"){
		$('.condition_ul[groupId="'+condition.groupId+'"]').append(condHtml);
	}else{
		$('.condition_ul[groupId="'+condition.groupId+'"] li[id="'+condId+'"]').before(condHtml).remove();
	}
}

/*条件设置 修改条件*/
function updateCondition(conObj){
	modal();
	var condition = $(conObj).parent();
	$("#condition_div").html($("#condition_add").html().replaceAll("#groupId#",condition.attr("groupId"))).show();
	
	$("#fieldName").attr("fieldvalue",condition.attr("fieldName"));
	$("#fieldName").val(condition.attr("display"));
	$("#fieldName").attr("condId",condition.attr("id"));
	$("#relation").val(condition.attr("relation"));
	$("#valueDisplay").val(condition.attr("valueDisplay"));
	$("#valueDisplay").attr("title",condition.attr("value"));
	addCondRow();
	$("#andOr").val(condition.attr("andOr"));
}

/*弹出窗口回调 回填数据*/
function fillFields(strValue){
	var strValues = strValue.split("|");
	$("#fieldName").val(strValues[1]);
	$("#fieldName").attr("fieldValue",strValues[0]);
	addCondRow(strValues[0],strValues[2]);
	jQuery.close('flowfields');
}

function addCondRow(strField,strType){
	var fieldHtml = "";
	if("employee"==strField || "EmployeeID"==strField){
		fieldHtml = '<input type=text id="valueDisplay" readOnly ondblclick="employeePopup2(\'userGroup\');"/><b class="icons b-search" onclick="employeePopup2(\'userGroup\');"></b>';
	}else if("post"==strType){
		fieldHtml = '<input type=text id="valueDisplay" readOnly ondblclick="openPost(\'radio\');"/><b class="icons b-search" onclick="openPost(\'radio\');"></b>';
	}else if("duty"==strType){
		fieldHtml = '<input type=text id="valueDisplay" readOnly ondblclick="openTitle(\'radio\');"/><b class="icons b-search" onclick="openTitle(\'radio\');"></b>';
	}else if("dept"==strType){
		fieldHtml = '<input type=text id="valueDisplay" readOnly ondblclick="employeePopup2(\'deptGroup\');"/><b class="icons b-search" onclick="employeePopup2(\'deptGroup\');"></b>';
	}else if(strType.length>0){
		fieldHtml = '<input type=text id="valueDisplay" readOnly ondblclick="openEnumValue(\''+strType+'\');"/><b class="icons b-search" onclick="openEnumValue(\''+strType+'\');"></b>';
	}else{
		fieldHtml = '<input id="valueDisplay" title="" />'
	}
	$("#inputHTML").html(fieldHtml);
}

//条件提交
function condSubmit(groupId){
	//确定按钮添加单击事件
	var cond = {};
	cond.fieldName 	= $("#fieldName").attr("fieldvalue");
	if(cond.fieldName.length==0){alert("条件字段不能为空！");$("#fieldName").focus();return false;}
	cond.display 	= $("#fieldName").val();
	cond.relation 	= $("#relation").find("option:selected").val();
	cond.relationDisplay = $("#relation").find("option:selected").text() ;
	cond.value 		= $("#valueDisplay").attr("title");
	cond.valueDisplay = $("#valueDisplay").val();
	if(cond.valueDisplay.length==0){alert("条件值不能为空！");$("#valueDisplay").focus();return false;}
	if(cond.value==""){
		cond.value = cond.valueDisplay ;
	}
	cond.andOr 		= $("#andOr").find("option:selected").val();
	cond.groupId	= groupId;
	addCondition(cond);
	changeCondition();
	$('#condition_div').hide();
	clearModal();			
}

/*更新条件对象*/
function changeCondition(){
	var condition = new Array();
	var to = $("#display").attr("to");
	var node = nodeMap.get($("#nodeAttrId").attr("nodeId"));
	if(typeof(node.conditionList)!="undefined"){
		jQuery.each(node.conditionList,function(i){
			var condObj = node.conditionList[i];
			if(to == condObj.to){
				condition = condObj;
			}
		});
	}
	condition.conditions = new Array();
	$("#condition_group_div .b-block").each(function(){
		var groupId = $(this).find(".condition_ul").attr("groupId");
		var groupType = $(this).find(".condition-group-ul select").find("option:selected").val();
		$(this).find(".condition_ul li").each(function(){
			var cond = {};
			cond.fieldName 	= $(this).attr("fieldName");
			cond.display 	= $(this).attr("display");
			cond.relation 	= $(this).attr("relation");
			cond.relationDisplay = $(this).attr("relationDisplay");
			cond.value 		= $(this).attr("valueStr");
			cond.valueDisplay = $(this).attr("valueDisplay");
			cond.andOr 		= $(this).attr("andOr");
			if(cond.value.length==0){
				cond.value = cond.valueDisplay;
			}
			cond.groupId 	= groupId;
			cond.groupType 	= groupType;
			condition.conditions.push(cond);
		});
	});
}

var curPopType = ""; //当前弹出窗的类型，在选择职员部分弹出窗时用这个标识，来区分是弹出窗决定回填字段



/*职员分组弹出窗口*/
function groupPopup(className){
	curPopType = "group";
	var urls = "/Accredit.do?popname=empGroup&inputType=checkbox";
	asyncbox.open({id:'Popdiv',title:'请选择职员分组',url:urls,modal:true,width:755,height:435,top:50,btnsbar:jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var employees = opener.strData;
				var arrEmp = employees.split("|") ;
				for(var j=0;j<arrEmp.length;j++){
					var field = arrEmp[j].split(";") ;
					if(field!=""){
						addApprove(className,field[1],field[0],'职员分组','group');
					}
				}
				changeApprove(className);
　　　　　	}
　　　	}
　	});
}

/*职员部门单选弹出窗口*/
function employeePopup2(pType){
	curPopType = "empDepRadio";
	var urls = "/Accredit.do?popname="+pType+"&inputType=radio";
	var popupType = (pType=='userGroup'?'职员':'部门') ;
	asyncbox.open({id:'Popdiv',title:'请选择'+popupType,url:urls,modal:true,width:755,height:435,top:50,btnsbar:jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var employees = opener.strData;
				var strEmp = employees.split("|")[0] ;
				var field = strEmp.split(";") ;
				if(field!=""){
					$("#valueDisplay").val(field[1]).attr("title",field[0]);
				}
　　　　　	}
　　　	}
　	});
}

/*职员部门弹出窗口*/
function employeePopup(className,pType){
	curPopType = "empDepCheckBox";
	var urls = "/Accredit.do?popname="+pType+"&inputType=checkbox";
	var popupType = (pType=='userGroup'?'职员':'部门') ;
	asyncbox.open({id:'Popdiv',title:'请选择'+popupType,url:urls,modal:true,width:755,height:435,top:50,btnsbar:jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var employees = opener.strData;
				var arrEmp = employees.split("|") ;
				for(var j=0;j<arrEmp.length;j++){
					var field = arrEmp[j].split(";") ;
					if(field!=""){
						addApprove(className,field[1],field[0],popupType,(pType=='userGroup'?'employee':'dept'));
					}
				}
				changeApprove(className);
　　　　　	}
　　　	}
　	});
}

/*js动态改变flash中结点名称*/
function setNodeName(nodeName){
	var flowObj = document.getElementById('flowDesignId');
	flowObj.setName(nodeName.value);
}

function saveWorkFlow(flowStr){
	flowStr = decodeURIComponent(flowStr);
	jQuery.ajax({type:"POST",url:"/OAWorkFlowTempAction.do?operation=7&type=saveFlow&flowId=$!flowId"+"&moduleType="+moduleType,
   		dataType:"json",
   		data:{json:jQuery.toJSON(nodeMap.values()),flowxml:flowStr},
   		success: function(msg){
     		if("ok"==msg.code){
     			asyncbox.confirm('流程修改成功，马上关闭设计窗口吗？','流程设计',function(action){
			　　　if(action == 'ok'){
			　　　　　window.close();
			　　　}
			　});
     		}else if("ok"==msg.code){
     			alert("流程修改失败");
     		}else{
     			var nodeId = msg.nodeId;
	     		if(typeof(msg.to)!="undefined"){
	     			nodeId += ";" + msg.to;
	     		}
	     		if(typeof(nodeId)!="undefined" && nodeId.length>0){
	     			selectNode(nodeId);
	     		}
	     		alert(msg.code);
     		}
   		}
	});
}

function modal(){
	$("body").append('<div id="bg"></div>');
	$("#bg").css({position:'absolute',left:'0px',top:'0px',background:'lightgrey','z-index':'1000',
	filter:'alpha(opacity=40)',opacity:'0.4',width:'100%',height:'100%'});
}

function clearModal(){
	$("#bg").remove();
}

function onFilterSetChange(obj){
	if($(obj).val() == 5  || $(obj).val()==6){
		$("#filterSetSQLDiv").show();
	}else{
		$("#filterSetSQLDiv").hide();
	}
}
function onAutoSelectPeopleChange(obj){
	if($(obj).val() == 3){
		$("#autoSelectPeopleSQLDiv").show();
	}else{
		$("#autoSelectPeopleSQLDiv").hide();
	}
}

var andMap = new Map();
andMap.put('||','或者');
andMap.put('&&','并且');

$(function(){
	$(".m-left").width($(window).width()-300);
});
</script>
</head>

<body onunload="closeWin();">
<form name="form" action="/OAWorkFlowTempAction.do?queryType=uploadFile" method="post" enctype="multipart/form-data">
<input type="file" id="uploadFile" name="file" style="display:none;" onchange="uploadFlowFile();"/>
<input type="hidden" id="ip" name="ip" value="$!ip">
<input type="hidden" id="flowId" name="flowId" value="$!flowId">
<input type="hidden" id="tableName" name="tableName" value="$!tableName">
<input type="hidden" id="moduleType" name="moduleType" value="$!moduleType"/>
</form>
<input type="hidden" value="$!workFileName" id="fileName" name="fileName" />
<div class="m-left">
	<script type="text/javascript">
	if(navigator.userAgent.toLowerCase().indexOf("msie")!=-1){
		document.write('<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" '
			+'id="flowDesignId" width="100%" height="100%" ' 
			+'codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab"> '
			+'<param name="movie" value="/flash/WorkFlowDesignNew.swf" /> '
			+'<param name="quality" value="high" /> '
			+'<param name="bgcolor" value="#000000" /> '
			+'<param name="wmode" value="transparent" /> '
			+'<param name="flashvars" value="serverURL=http://$!ip/&fileName=$!workFileName&tableName=$!tableName&newCreate=$!newCreate&importFile=$!importFile" /> '
			+'<param name="allowScriptAccess" value="sameDomain" /> '
			+'</object>');
	}else{
		document.write('<embed src="/flash/WorkFlowDesignNew.swf" id="flowDesignId"  quality="high" bgcolor="#000000" '
			+'	width="100%" height="100%" name="column" align="middle"  '
			+'	play="true" '
			+'	loop="false" '
			+'	flashvars="serverURL=http://$!ip/&fileName=$!workFileName&importFile=$!importFile&tableName=$!tableName&newCreate=$!newCreate" '
			+'	quality="high"  '
			+'  wmode="transparent" '
			+'	allowScriptAccess="sameDomain" '
			+'	type="application/x-shockwave-flash" '
			+'	pluginspage="http://www.adobe.com/go/getflashplayer"> '
			+'</embed>');
	}
	</script>
</div>
<div id="display_div"></div>
<div class="m-right" id="nodeAttrId" nodeId=""> 

</div>
<script type="text/html" id="startHtml">
		<div class="tags-btn  tag-sel">
            <span class="s-btn tag-s" show="d-filed">字段设置</span>
			<span class="s-btn tag-s" show="d-define">高级</span>
        </div>
		<div class="wp-main">
		<!-- 字段设置Start -->
		<div class="d-filed">
            	<div class="b-block pad-left">
                	<table class="filed-table" cellpadding="0" cellspacing="0" border="0">
                    	<thead>
                        	<tr>
                            	<td width="85">字段</td>
                                <td width="45">
                                	<span class="cbox-span">
                                        <input type="checkbox" id="field_readonly"/>
                                        <label for="field_readonly">只读</label>
                                    </span>
                                </td>
                                <td width="45">
                                	<span class="cbox-span">
                                        <input type="checkbox" id="field_hidden"/>
                                        <label for="field_hidden">保密</label>
                                    </span>
                                </td>
                                <td width="45">
                                	<span class="cbox-span">
                                        <input type="checkbox" id="field_notnull"/>
                                        <label for="field_notnull">必填</label>
                                    </span>
                                </td>
                            </tr>
                        </thead>
                    </table>
                    <div class="d-scroll">
                        <table class="filed-table" cellpadding="0" cellspacing="0" border="0" style="margin-top:-1px;">
                            <tbody>
                            #foreach($field in $tableInfo.fieldInfos)
                            	#if($globals.canDisField2($field))
                                <tr>
									#set($display = $field.display.get("zh_CN"))
									#if("$!flowType" == "1")#set($display = $field.languageId)#end
                                    <td width="85" title="$!display">$!display</td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="readOnly" name="$field.fieldName"/></td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="hidden" name="$field.fieldName" iTitle="$field.display.get("zh_CN")"/></td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="isNotNull"name="$field.fieldName"/></td>
                                </tr>
                                #end
                             #end
							 #set($tCount=0)
							 #foreach($table in $globals.getChildTable($tableInfo.tableName))
								#set($tCount=$tCount+1)
								#foreach($field in $table.fieldInfos)
								#if($globals.canDisField2($field))
                                <tr>
									#set($display = $field.display.get("zh_CN"))
									#if("$!flowType" == "1")#set($display = $field.languageId)#end
                                    <td width="85" title="$table.display.get("zh_CN") - $!display">明细$!{tCount}- $!{display}</td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="readOnly" name="${table.tableName}_$field.fieldName"/></td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="hidden" name="${table.tableName}_$field.fieldName" iTitle="$field.display.get("zh_CN")"/></td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="isNotNull"name="${table.tableName}_$field.fieldName"/></td>
                                </tr>
								#end
                                #end
							 #end
                            </tbody>
                        </table>
                    </div>
                </div>
                <p class="p-bottom">
                	<span class="s-btn">保密字段设置</span>
                </p>
            </div>
			 <!-- 自定义Start -->
            <div class="d-define" style="display:none;">
            	<p class="p-noto">自定义标识</p>
            	<div class="b-block">
                	<i>执行通过</i>
                    <input class="inp-txt long-txt" type="text" id="passExec"/>
                </div>
				<div class="exec_note">
				注意：本高级功能需要先为不同动作编写自定义代码，非高级技术员请勿使用.
				</div>
            </div>
            <!-- 自定义End -->
		</div>
</script>
<script type="text/html" id="nodeHtml">
    	<div class="tags-btn">
            <span class="s-btn tag-s tag-sel" show="d-basic">基本属性</span>
            <span class="s-btn tag-s" show="d-conduct">审核人</span>
            <span class="s-btn tag-s" show="d-filed">字段设置</span>
            <span class="s-btn tag-s" show="d-define">高级</span>
        </div>
        <div class="wp-main">
            <!-- 基本属性 Start -->
            <div class="d-basic">
                <div class="b-block">
                	<i>步骤名称</i>
                    <span>
                    	<input class="inp-txt long-txt" type="text" onkeyUp="setNodeName(this)" id="display"/>
                    </span>
                </div>
                <div class="b-block forward-time">
               		<span class="cbox-span">
                       	<input type="checkbox" id="forwardTime" />
                        <label for="forwardTime">转交时设置办理时限</label>
                    </span>
                </div>
                <div class="b-block">
                	<i>办理时限</i>
                    <input class="inp-txt short-txt" type="text" id="timeLimit"/>
                    <select class="slt" id="timeLimitUnit">
                    	<option value="0">天</option>
                    	<option value="1">小时</option>
                    	<option value="2">分钟</option>
                    </select>
                </div>
                <div class="b-block">
                	<i>提前提醒</i>
                    <input class="inp-txt short-txt" type="text" id="noteTime"/>
                    <select class="slt" id="noteTimeUnit">
                    	<option value="0">天</option>
                    	<option value="1">小时</option>
                    	<option value="2">分钟</option>
                    </select>
                </div>
                <div class="b-block">
                	<i>超时提醒频率</i>
                    <input class="inp-txt short-txt" type="text" id="noteRate" />
                    <select class="slt" id="noteRateUnit">
                    	<option value="0">天</option>
                    	<option value="1">小时</option>
                    	<option value="2">分钟</option>
                    </select>
                </div>
                <div class="b-block">
                	<i></i>
                    <span>
                        <span class="cbox-span">
                        	<input type="checkbox" id="allowBack"/>
                            <label for="allowBack">允许回退</label>
                        </span>
                        <span class="cbox-span">
                        	<input type="checkbox" id="allowCancel"/>
                            <label for="allowCancel">允许撤回</label>
                        </span>
                        <span class="cbox-span">
                        	<input type="checkbox" id="allowJump"/>
                            <label for="allowJump">允许略过</label>
                        </span>
                        <span class="cbox-span">
                        	<input type="checkbox" id="allowStop"/>
                            <label for="allowStop">允许结束</label>
                        </span>
                        <span class="cbox-span">
                        	<input type="checkbox" id="useAllApprove"/>
                            <label for="useAllApprove">允许全审</label>
                        </span>
                        <span class="cbox-span">
                        	<input type="checkbox" id="ideaRequired"/>
                            <label for="ideaRequired">会签意见必填</label>
                        </span>
                    </span>
                </div>
                <p class="p-bottom">
                	<span class="remind-sbtn" id="remind_btn">提醒设置</span>
                </p>
            </div>
            <!-- 基本属性 End -->
            <!-- 审核人 Start -->
            <div class="d-conduct" style="display:none;">
            	<div class="b-block">
                	<i>选人过滤规则</i>
                    <select class="slt long-slt" id="filterSet" onchange="onFilterSetChange(this)">
                    	<option value="0">选择全部指定的审核人</option>
                    	<option value="1">只选择本部门及子部门审核人</option>
						<option value="7">不选择本部门及子部门审核人</option>
						<option value="8">不选择制单人</option>
                    	<option value="2">只选择上级部门审核人</option>
						<option value="9">只选择本部门及上级部门审核人</option>
                    	<option value="3">只选择下级部门审核人</option>
                    	<option value="4">只选择一级部门审核人</option>
						<option value="5">过滤自定义审核人</option>
						<option value="6">只选择自定义审核人</option>
                    </select>
                </div>
				<div class="b-block" id="filterSetSQLDiv" style="display:none">
                	<i>过滤规则SQL</i>
                    <input class="slt long-slt" id="filterSetSQL" />
                </div>
                <div class="b-block">
                	<i>自动选人规则</i>
                    <select class="slt long-slt" id="autoSelectPeople"  onchange="onAutoSelectPeopleChange(this)">
                    	<option value="0">不进行自动选择</option>
                    	<option value="1">自动选择流程发起人</option>
                    	<option value="2">自动选择直属上司</option>
						<option value="3">自定义选人</option>
                    </select>
                </div>
				<div class="b-block" id="autoSelectPeopleSQLDiv" style="display:none">
                	<i>选人规则SQL</i>
                    <input class="slt long-slt" id="autoSelectPeopleSQL" />
                </div>
                <div class="b-block d-radio">
                	<span class="cbox-span">
                        <input id="selectId" type="radio" name="approvePeople" value="select" checked="checked"/>
                        <label for="selectId">允许选择审核人</label>
                    </span>
                    <span class="cbox-span">
                        <input id="fixId" type="radio" name="approvePeople" value="fix"/>
                        <label for="fixId">固定审核人</label>
                    </span>
                </div>
                <div class="b-block pad-left">
                	<table class="list-table" cellpadding="0" cellspacing="0" border="0">
                    	<thead>
                        	<tr>
                            	<td>类型</td>
                                <td>人员</td>
                            </tr>
                        </thead>
                    </table>
                    <div class="d-scroll" style="max-height:270px;">
                        <ul class="list-ul">
                        	
                        </ul>
                    </div>
                </div>
                <p class="p-bottom">
                	<span class="s-btn" id="dutyId">职位</span>
                	<span class="s-btn" id="postId">岗位</span>
                    <span class="s-btn" id="employeeId">职员</span>
                    <span class="s-btn" id="deptId">部门</span>
                </p>
            </div>
            <!-- 审核人End -->
            <!-- 字段设置Start -->
            <div class="d-filed" style="display:none;">
            	<div class="b-block pad-left">
                	<table class="filed-table" cellpadding="0" cellspacing="0" border="0">
                    	<thead>
                        	<tr>
                            	<td width="85">字段</td>
                                <td width="45">
                                	<span class="cbox-span">
                                        <input type="checkbox" id="field_readonly"/>
                                        <label for="field_readonly">只读</label>
                                    </span>
                                </td>
                                <td width="45">
                                	<span class="cbox-span">
                                        <input type="checkbox" id="field_hidden"/>
                                        <label for="field_hidden">保密</label>
                                    </span>
                                </td>
                                <td width="45">
                                	<span class="cbox-span">
                                        <input type="checkbox" id="field_notnull"/>
                                        <label for="field_notnull">必填</label>
                                    </span>
                                </td>
                            </tr>
                        </thead>
                    </table>
                    <div class="d-scroll">
                        <table class="filed-table" cellpadding="0" cellspacing="0" border="0" style="margin-top:-1px;">
                            <tbody>
                            #foreach($field in $tableInfo.fieldInfos)
                            	#if($globals.canDisField2($field))
                                <tr>
									#set($display = $field.display.get("zh_CN"))
									#if("$!flowType" == "1")#set($display = $field.languageId)#end
                                    <td width="85" title="">$!display</td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="readOnly" name="$field.fieldName"/></td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="hidden" name="$field.fieldName"  iTitle="$!display"/></td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="isNotNull"name="$field.fieldName"/></td>
                                </tr>
                                #end
                             #end
							 #set($tCount=0)
							 #foreach($table in $globals.getChildTable($tableInfo.tableName))
								#set($tCount=$tCount+1)
								#foreach($field in $table.fieldInfos)
								#if($globals.canDisField2($field))
                                <tr>
									#set($display = $field.display.get("zh_CN"))
									#if("$!flowType" == "1")#set($display = $field.languageId)#end
                                    <td width="85" title="$table.display.get("zh_CN") - $!display">明细$!{tCount}- $!{display}</td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="readOnly" name="${table.tableName}_$field.fieldName"/></td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="hidden" name="${table.tableName}_$field.fieldName" iTitle="$field.display.get("zh_CN")"/></td>
                                    <td width="45" align="center"><input class="select-field" type="checkbox" value="isNotNull"name="${table.tableName}_$field.fieldName"/></td>
                                </tr>
								#end
                                #end
							 #end
                            </tbody>
                        </table>
                    </div>
                </div>
                <p class="p-bottom">
                	<span class="s-btn" id="hidden-btn">保密字段设置</span>
                </p>
            </div>
            <!-- 字段设置End -->
            <!-- 自定义Start -->
            <div class="d-define" style="display:none;">
            	<p class="p-noto">自定义标识</p>
            	<div class="b-block">
                	<i>执行通过</i>
                    <input class="inp-txt long-txt" type="text" id="passExec"/>
                </div>
                <div class="b-block">
                	<i>执行回退</i>
                    <input class="inp-txt long-txt" type="text" id="backExec"/>
                </div>
                <div class="b-block">
                	<i>执行结束</i>
                    <input class="inp-txt long-txt" type="text" id="stopExec"/>
                </div>
				<div class="exec_note">
				注意：本高级功能需要先为不同动作编写自定义代码，非高级技术员请勿使用.
				</div>
            </div>
            <!-- 自定义End -->
        </div>

<div class="remind-wp short-remind-wp" id="hidden-id">
	<p class="p-title">
		保密字段-流程结束是否公开
	<b class="icons b-del" onclick="$('#hidden-id').hide();clearModal();"></b>
	</p>
    <div class="remind-sel">
        <ul class="sel-title-ul">
            <li>
                <i class="border-right">字段</i><i class="short-i"><input type="checkbox" id="enPublic" /><label for="enPublic">是否公开</label></i>
            </li>
        </ul>
		<div class="srcoll-div">
        	<ul class="hidden-field-ul">
        	</ul>
		</div>
    </div>
	<p class="p-bottom">
		<span class="s-btn" id="publicBtn">确定</span>
    	<span class="s-btn" onclick="$('#hidden-id').hide();clearModal();">关闭</span>
   </p>
</div>		
<div class="remind-wp" id="remind-id">
	<p class="p-title">
		提醒方式
	<b class="icons b-del" onclick="$('#remind-id').hide();clearModal();"></b>
	</p>
	<span class="cbox-span">
    	<input id="standaloneNoteSet" type="checkbox" />
        <label for="standaloneNoteSet">此步骤独立设置提醒方式</label>
    </span>
    <ul class="remind-ul">
    	<li>
        	<i>下一审核人：</i>
        	<span class="cbox-span"><input type="checkbox" id="nextSMS"/><label for="nextSMS">通知</label></span>
            <span class="cbox-span"><input type="checkbox" id="nextMobile"/><label for="nextMobile">短信</label></span>
            <span class="cbox-span"><input type="checkbox" id="nextMail"/><label for="nextMail">邮件</label></span>
        </li>
        <li>
        <i>发起人：</i>
        	<span class="cbox-span"><input type="checkbox" id="startSMS"/><label for="startSMS">通知</label></span>
            <span class="cbox-span"><input type="checkbox" id="startMobile"/><label for="startMobile">短信</label></span>
            <span class="cbox-span"><input type="checkbox" id="startMail"/><label for="startMail">邮件</label></span>
        </li>
        <li>
        	<i>全部审核人：</i>
        	<span class="cbox-span"><input type="checkbox" id="allSMS"/><label for="allSMS">通知</label></span>
            <span class="cbox-span"><input type="checkbox" id="allMobile"/><label for="allMobile">短信</label></span>
            <span class="cbox-span"><input type="checkbox" id="allMail"/><label for="allMail">邮件</label></span>
        </li>
        <li>
        	<i>以下指定人：</i>
        	<span class="cbox-span"><input type="checkbox" id="setSMS"/><label for="setSMS">通知</label></span>
            <span class="cbox-span"><input type="checkbox" id="setMobile"/><label for="setMobile">短信</label></span>
            <span class="cbox-span"><input type="checkbox" id="setMail"/><label for="setMail">邮件</label></span>
        </li>
    </ul>
    <div class="remind-sel">
        <ul class="sel-title-ul">
            <li>
                <i class="border-right">类型</i><i>人员</i>
            </li>
        </ul>
		<div class="srcoll-div">
        	<ul class="sel-con-ul">
        	</ul>
		</div>
    </div>
 	<p class="p-bottom">
    	<span class="s-btn" onclick="$('#remind-id').hide();clearModal();">确定</span>
		<span class="s-btn" onclick="employeePopup('sel-con-ul','userGroup');">+职员</span>
		<span class="s-btn" onclick="employeePopup('sel-con-ul','deptGroup');">+部门</span>
   </p>
</div>
</script>
<script type="text/html" id="choiceHtml">
<div class="tags-btn">
            <span class="s-btn tag-s">条件设置</span>
        </div>
        <div class="wp-main">
            <div class="d-define">
            	<div class="b-block">
                	<i>条件名称</i>
                    <input class="inp-txt long-txt" type="text" id="display" onkeyUp="setNodeName(this)"/>
                </div>
			</div>
		</div>
</div>
</script>
<script type="text/html" id="lineHtml">
    	<div class="tags-btn">
            <span class="s-btn tag-s">条件设置</span><span class="btn-group" onclick="conditionGroup();">+分组条件</span>
        </div>
        <div class="wp-main">
            <div class="d-define">
            	<div class="b-block">
                	<i>条件名称</i>
                    <input class="inp-txt long-txt" type="text" id="display" onkeyUp="setNodeName(this)" to=""/>
                </div>
                <div id="condition_group_div">
				</div>
            </div>
        <div>
		<div class="remind-wp condition-wp" id="condition_div">
		</div>
</script>
<script type="text/html" id="condition_group">
		<div class="b-block pad-left" >
        	<ul class="filed-ul condition-group-ul">
				<li class="head-li">分组关系：<select onchange="changeCondition();" groupId="#groupId#"><option value="||">或者</option><option value="&&">并且</option></select>
					 <span class="s-btn" onclick='conditionShow("#groupId#");'>+添加条件</span>
				</li>
            	<li>
                   <i class="long-width border-right">字段</i>
                   <i class="border-right">等式</i>
                   <i class="border-right ct-width">值</i>
                   <i>关系</i>
                </li>
        	</ul>
        	<div class="d-scroll">
                <ul class="filed-ul condition_ul" groupId="#groupId#">
                
             	</ul>
             </div>
		</div>
</script>
<script type="text/html" id="condition_add">
		<p class="p-title">
			添加条件
			<b class="icons b-del" onclick="$('#condition_div').hide();clearModal();"></b>
		</p>
	 	<ul class="remind-ul">
	  		<li><i>字段：</i><input id="fieldName" readOnly=readOnly ondblclick="flowfield();" value="" fieldValue=""/><b class="icons b-search" onclick="flowfield();"></b></li>
			<li>
				<i>等式：</i>
				<select id="relation">
					<option value="==">等于</option>
					<option value="!=">不等于</option>
					<option value=">">大于</option>
					<option value=">=">大于等于</option>
					<option value="<">小于</option>
					<option value="<=">小于等于</option>
					<option value="like">包含</option>
					<option value="not like">不包含</option>				
				</select>
			</li>
			<li><i>值：</i><div id="inputHTML"><input id="valueDisplay" title=""/></div></li>
			<li><i>关系：</i><select id="andOr">
				<option value="||">或者</option>
				<option value="&&">并且</option>
			</select>
			</li>
		</ul>
		<p class="p-bottom">
			<span class="s-btn" id="condition_submit" onclick='condSubmit("#groupId#");'>确认</span>
   			<span class="s-btn" onclick="$('#condition_div').hide();clearModal();">关闭</span>
  		</p>
</script>
<script type="text/javascript">
var node_json = '$!node_json'.replaceAll('\\\\','\\\\');
var nodeMap = new Map();
if(node_json!="" && node_json.length>0){
	jQuery.each(JSON.parse(node_json),function(key,value){
		nodeMap.put(key,value);
	});
}

$(function(){
	/*改变flash高度*/
	$('body').height($(window).height()-10);
});

//双击回填数据(适用于多选下拉框),
function fillData(strData){
	var field = strData.split(";") ;
	
	if(curPopType == "empDepRadio"){//部门职员单选弹出窗，用于条件，选择部门
		$("#valueDisplay").val(field[1]).attr("title",field[0]);
		jQuery.close('Popdiv');
		return;
	}
	
	var className = "list-ul";
	if($(".tag-sel").attr("show") == "d-basic"){
		className = "sel-con-ul";
	}
	if(field[3]!="undefined"){
		addApprove(className,field[1],field[0],'职员','employee');
	}else{
		if(field[0].length==28){
			addApprove(className,field[1],field[0],'职员分组','group');
		}else{
			addApprove(className,field[1],field[0],'部门','dept');
		}
	}
	changeApprove(className);
	jQuery.close('Popdiv');
}
</script>
</body>
</html>