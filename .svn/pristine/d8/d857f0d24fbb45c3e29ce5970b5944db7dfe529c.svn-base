function openInputDate(obj){
	WdatePicker({lang:'zh_CN'});
}

jQuery(document).ready(function() {
	//处理添加修改弹出层点击input框不拖动方法
	$('.update-wrap :input').live('mouseover mouseout', function(event) {
		if (event.type == 'mouseover') {
			$(".update-wrap").unbind("mousedown");
		}else {
		    publicDragDiv($(".update-wrap"))
		}
	});
	$("#proListU li").hover(
		function(){$(this).addClass("select").find(".del-type").show();},	
		function(){$(this).removeClass("select").find(".del-type").hide();}
	);
	$(document).click(function(event){
		var target = $(event.target);
		if(target.attr("id")!="ulAddType" && target.parents("#ulAddType").attr("id")!="ulAddType"
		 && target.attr("id")!="add-cal-type" && target.parents("#add-cal-type").attr("id")!="add-cal-type"
		 && target.attr("id")!="addBlock" && target.parents("#addBlock").attr("id")!="addBlock"){		
			$("#add-cal-type").hide();
		}	
		if(target.attr("id")!="set" && target.parents("#set").attr("id")!="set"
		&& target.attr("id")!="divId" && target.parents("#divId").attr("id")!="divId"
		&& target.attr("id")!="iconsIds" && target.parents("#iconsIds").attr("id")!="iconsIds"
		&& target.attr("id")!="iconsId" && target.parents("#iconsId").attr("id")!="iconsId"
		&& target.attr("id")!="show_h" && target.parents("#show_h").attr("id")!="show_t"){
			$("#set").hide();			
		}
		if(target.attr("id")!="timeId" && target.parents("#timeId").attr("id")!="timeId"){
			$("#showTime").hide();			
		}
		if(target.attr("id")!="select-color" && target.parents("#timeId").attr("id")!="select-color"){
			$("#select-color").hide();			
		}
		if(target.attr("id")!="show_t" && target.parents("#show_t").attr("id")!="show_t"
		&& target.attr("id")!="show_h" && target.parents("#show_t").attr("id")!="show_h"){
			$("#show_t").hide();					
		}	
		if(target.attr("id")!="typeDisplay" && target.parents("#typeDisplay").attr("id")!="typeDisplay"
		&& target.attr("id")!="selectwin" && target.parents("#selectwin").attr("id")!="selectwin"){
			$("#typeDisplay").hide();					
		}
		
		if(target.attr("class")!="ug_pa" && target.attr("id")!="publicType" ){
			$("#publicType").hide();					
		}	
	});
	    $("#findcontxt").keydown(function(event) {  
         if (event.keyCode == 13) {  
         	$("#fdContxt").val(trimStr($("#findcontxt").val()));
         	$(".hideBg").show();
         	$("#closeDiv").show();
			form.submit();
         }  
     }) 
     $("#context").keydown(function(event) {  
         if (event.keyCode == 13) {  
         	 var context = encodeTextCode($("#context").val());
         	 
         	 var color = $("#uType li[class='li-sel']").attr('bg');  
         	 if(color == undefined){
     			color = $("#未分类").attr('bg');   	
    		 }        	 
         	 if((context).length<=0){
         	 	highlight($("#context"),5);
         	 	return false;
         	 }         	         
         	 var selType="";
         	 $(".uType>li").each(function(){				
				if($(this).attr('class') == "li-sel"){
					selType = $(this).attr('name');
				}				  			 	
	 		 })	 		
             var url = "/ToDoAction.do?operation=1&addFlag=quick";
			 jQuery.ajax({
		  	 	type: "POST",url: url,	
		  	 	data:{
		  	 		context:context,
		  	 		selType:selType
		  	 	},	   
		   	 	success: function(msg){
		   	 		if(msg != ""){
			   	 		if(msg.indexOf("err")>-1){
			   	 			asyncbox.tips(msg,'error');
			   	 		}else if(msg.length>60){
	   	 				//用户是否被T出
	   	 					location.href = "/ToDoAction.do";
	   	 				}else{
			   	 			asyncbox.tips('添加成功','success');
			   	 			autoAdd(msg,context,selType,color,'');	
			   	 		}		   	 				   	 					   	 			
		   	 		}else{
		   	 			asyncbox.tips('添加失败!','error');
		   	 		}		   	 		    
		   	 	}
			});	
         }  
     })  
});
/*快速添加*/
function quaikAdd(){
     var context = encodeTextCode($("#context").val());
     //var selType = $("#selType").val();
     var color = $("#uType li[class='li-sel']").attr('bg');
     
     if(color == undefined){
     	color = $("#未分类").attr('bg');   	
     }   
     if(context.length<=0){
       	 highlight($("#context"),5);
       	 return false;
     } 
      var selType="";
	  $(".uType>li").each(function(){				
		if($(this).attr('class') == "li-sel"){
			selType = $(this).attr('name');
		}				  			 	
	  })	    	 
     var url = "/ToDoAction.do?operation=1&addFlag=quick";
	 jQuery.ajax({
  	 	type: "POST",url: url,	
  	 	data:{
  	 		context:context,
  	 		selType:selType
  	 	},	   
   	 	success: function(msg){  	 	
   	 		if(msg != ""){ 
   	 			if(msg.indexOf("err")>-1){
	   	 			asyncbox.tips(msg,'error');
	   	 		}else if(msg.length>60){
	   	 		//用户是否被T出
	   	 			location.href = "/ToDoAction.do";
	   	 		}else{
	   	 			asyncbox.tips('添加成功','success');
	   	 			autoAdd(msg,context,selType,color,'');	
	   	 		}		 		 	 			  	 			
   	 		}else{
		   	 	asyncbox.tips('添加失败!','error');
		   	}			   	 		    
   	 	}
	});
}
/*
 me:jquery对象
 count:闪烁的次数倍数
 调用例子:highlight($("#userName"),5);
*/
function highlight(me,count){
	var count = count-1;
	if(count<=0){
		me.css('background-color' , "");
		return;
	}
	if(count%2==0){
		me.css('background-color' , "#ffcccc");
	}else{
		me.css('background-color' , "");
	}
	setTimeout(function(){
		highlight(me,count);
	},200);
}

/*
divId--xianshi
flag -- 
*/
function displayNo(divId,flag){
	var obj = $("#"+divId);
	var che =$("#"+flag);
	$("#addpe").val("");
	if(divId == "typeDisplay"){
		$("#addtypeCopy").hide();
		$("#addBlockCopy").show();
	}
	if(obj.is(":hidden")){
		if(flag != ""){			
			$("#addpeCopy").val("");
			che.hide();						
		}
		obj.show();
		$("#addpe").focus();
	}else{
		if(flag != ""){
			$("#addpeCopy").val("");
			che.show();			
		}
		obj.hide();
	}
}
/**添加分类*/
function addType(Id){
	 var addpre = trimStr($("#"+Id).val());
	 if(addpre == ""){
	 	highlight($("#"+Id),5);
        return false;
	 }
	 if(addpre=="未分类"){
	 	asyncbox.tips('有此分类','error');
        return false;
	 }
	 var url = "/ToDoAction.do?operation=6&type="+addpre+"&color="+$("#color").val();
	 jQuery.ajax({
  	 type: "POST",
   	 url: url,		   
   	 success: function(msg){
   	 	if(msg != ""){
   	 		if(Id == "addpeCopy"){
   	 			$("#addtypeCopy").hide();
   	 			$("#addBlockCopy").show();
   	 		}
   	 		asyncbox.tips('添加成功','success');
   	 		autoUl(msg,addpre);
   		}else{
   			asyncbox.tips('已有相同的分类','error');
   			highlight($("#addpe"),5);
   			}		    
   		}
	});
}
/**/
function canselType(){
	$("#addpe").val("");
	$("#add-cal-type").hide();
}

/*页面切换*/
function tabPage(obj){
	$("#tab").val(obj);
	$("#closeDiv").show();
	$(".hideBg").show();
	form.submit();
}
function findContxt(){	
	
	$("#fdContxt").val(trimStr($("#findcontxt").val()));
	
	$("#closeDiv").show();
	$(".hideBg").show();
	form.submit();
	//location.href = "/ToDoAction.do?operation=4&findcontxt="+$("#findcontxt").val()+"&tab="+$("#tab").val();
}
/*完成*/
function sureOver(obj,flag){	
	 var url = "/ToDoAction.do?operation=2&updateFlag=over&id="+obj+"&overOrNo="+flag;
	 jQuery.ajax({
	  	 type: "POST",
	   	 url: url,		   
	   	 success: function(msg){ 	 	  	 	
	   	 	if(msg != ""){ 	 	
	   	 		if(msg.indexOf("err")>-1){
	   	 			asyncbox.tips(msg,'error');
	   	 		}else if(msg.length>60){
	   	 		//用户是否被T出
	   	 			location.href = "/ToDoAction.do";
	   	 		}else{  	 			
		   	 	  	var ids = $("#"+obj);
		   	 	  	$(ids).hide(500);
		   	 		    		
		    		if($("#tab").val() !="over"){
	   	 				var oldunOver = $("#unOver").text();//旧的待办数量
	   	 				$("#unOver").text(parseFloat(oldunOver)-1);
	   	 				var oldoversd = $("#oversd").text();
	   	 				$("#oversd").text(parseFloat(oldoversd)+1); 	 			
	   	 			}else{
	   	 				var oldunOver = $("#unOver").text();//旧的待办数量
	   	 				$("#unOver").text(parseFloat(oldunOver)+1);
	   	 				var oldoversd = $("#oversd").text();
	   	 				$("#oversd").text(parseFloat(oldoversd)-1);
	   	 				
	   	 			}	
	   	 			var oldpagelen = $(".page-number").html().length;
   	 				var oldpagenum = $(".page-number").html().substring(1,oldpagelen-1);
   	 				var newnum = parseFloat(oldpagenum)-1;
   	 				$(".page-number").html("("+newnum+")");    			    	
	   	 		}	  	 	 			
	   		}		    
   		}
	});
}
/**自动添加一行*/
function autoAdd(msg,context,selType,color,flagType){
	var d = new Date();
	var hours=d.getHours();
   	var mintues=d.getMinutes();
   	if(hours<10){
   		hours = "0"+hours;
   	}
   	if(mintues<10){
   		mintues = "0"+mintues;
   	}   	  	   
    var alertTimes = hours+":"+mintues+":"+d.getSeconds();  			
	var len = $("#proListU>li").length;
	if(selType == ""){
		selType ="未分类";
	}
	msg = msg.split(";");   
    $("#context").val("");
    if(flagType=="all"){
    	$("#unOver").text(msg[3]); 
   	 	$("#oversd").text(msg[4]);
    }else{
	    $("#unOver").text(msg[1]); 
	    $("#oversd").text(msg[2]); 
    }
    
	/*自动添加分页*/		
	if(len>14){
		var yu = (parseInt(msg[1])-parseInt(1))%15;
		if(yu == 0){
			var nb = $(".scott>a").eq(-2).text();
			var ns = parseInt(nb)+parseInt(1);		
			$(".scott>a").eq(-2).after('<a href="javascript:pageSubmit('+ns+');">'+ns+'</a>');
		}	
		var remId = $("#proListU>li").last().attr("id");
		$("#"+remId).remove();		
	}
	/**/
    if($("#tab").val()=="over" && $("#updateFlag").val() != "update"){   	
    	$(".page-number").html("("+msg[2]+")");
    }else{   	
    	var style = 'background-position:-202px -92px;';
    	 if($("#tab").val()=="over" && $("#updateFlag").val() == "update"){
    	 	style = 'background-position:-234px -92px;';
    	 }
    	 var li ="<li id="+msg[0]+"><input type=\"hidden\" name=\"oldId\" value="+msg[0]+">"
            +"<input type=\"hidden\" name=\"oldTitle\" value="+context+">"
            +"<input type=\"hidden\" name=\"oldType\" value="+selType+">"                
    		+"<b class=\"lf icons bCheck\" id=\"sureOver\" style=\""+style+"\" title=\"勾上完成\" onclick=\"sureOver('"+msg[0]+"','YES');\"></b> "
     		+"<i class=\"lf iContent\" onclick=\"updateTram('"+msg[0]+"');\">"+context+"</i>"     		
     		+"<span class=\"lf pr typeWait \" id='color_"+msg[0]+"'><b class=\"pa \" style=\"background:#"+color+" \" onclick='getColor(\"color_"+msg[0]+"\",\""+selType+"\");'></b>"
     		+"<span class=\"ug_pa\" style=\"cursor: pointer;\" onclick='changeTypePre(\"publicType\",\"color_"+msg[0]+"\",\""+msg[0]+"\");'>"+selType+"</span></span>"    		   		
     		+" <b class=\"lf time\">今日"+alertTimes+"</b><span class=\"pr rf tiXing\" id=time_"+msg[0]+">"
     		+"<b class=\"icons s-remind\" name=\"iconsId_"+msg[0]+"\" onclick=\"displayTime('time_"+msg[0]+"','oldForm','"+msg[0]+"');\" id=\"iconsId\" title=\"提醒\"></b>"
     		+"<input type=\"text\" class=\"alertTime\" /></span><b class=\"del-type icons\" title=\"删除\" onclick=\"delForm('"+msg[0]+"');\"></b></li>";            
    	$("#proListU").prepend(li);
    	if($("#updateFlag").val() != "update"){ 
    		$(".page-number").html("("+msg[1]+")");
    	}
    	
    }		 
}

function autoUl(msg,name){
	
	var color = msg.split(";");
	var li = "<li  onclick=\"selectType('"+name+"','"+color[0]+"');\" bg="+color[1]+" name="+name+" id="+color[0]+">"
			+"<b class=\"b-cbox\" sel='f' id=\"lil_"+color[0]+"\" style=\"background:#"+color[1]+"\" onclick=\"moreData(this);\" ></b>"
			+"<i>"+name+"</i>"
			+"<em class=\"update-type icons\" title='更改颜色' onclick=\"getColor('"+color[0]+"','"+name+"');\"></em>"
			+"<em class=\"del-type icons\" id=\"li_"+color[0]+"\" onclick=\"deleteType('"+name+"','"+color[0]+"');\"></em>" 
			+"</li>";		         
			
	var lil = "<li id=\"second_"+color[0]+"\" onclick=\"fillType('"+name+"','"+color[1]+"');\"> "
	           +"<b class=\"pa\" style=\"background:#"+color[1]+"\"  ></b>"+name+"</li>";   
	       
	var lils = "<li id=\"three_"+color[0]+"\" onclick=\"changeType('"+name+"');\">" 
  			+"<b class=\"pa\" style=\"background:#"+color[1]+"\"  ></b>"+name+"</li>";                                   
    $("#uType").append(li); 
    $("#addBlockCopy").before(lil); 
    $("#publicType").append(lils);  
   	$("#addBlock").show();
    $("#add-cal-type").hide();
}
function selectType(name,ID){
	$("#selType").val(name);
	$("#selTypeId").val(ID);
	$(".hideBg").show();
    $("#closeDiv").show();	
	form.submit();
	//location.href = "/ToDoAction.do?operation=4&selType="+$("#selType").val()+"&tab="+$("#tab").val();	
}
function allType(){
	$("#selType").val("");
	$("#flagAll").val("all");
	$(".hideBg").show();
    $("#closeDiv").show();
	form.submit();
}
function displayTmNo(divId,flag){
	var addpre = $("#"+divId);
	var apre = $("#"+flag);
	var ids = "#time_"+$("#newFormId").val();
	//alert($("#newFormId").val());
	$(ids).append(addpre);	
	addpre.show();
	apre.hide();
	event.stopPropagation();	
}
function showDiv(){
	$("#AddToDo").html($("#copyAddToDo").html());
	$("#AddToDo").show();
	$("#clientId").val("");
	$("#myformId").val("");
	$("#taskId").val("");
	$("#updateFlag").val("");
	$("#taskId").val("");	
	publicDragDiv($("#AddToDo"));
	
}

function updateTram(id){
	$("#delId").show();
	var url = "/ToDoAction.do?operation=7&id="+id;
	jQuery.ajax({
	  	 type: "POST",
	   	 url: url,		   
	   	 success: function(msg){
	   	 	if(msg != ""){ 	 	
	   	 	  	AddToDoShow(msg);  	 		
	   		}else if(msg.length>60){
	   	 		//用户是否被T出
	   	 		location.href = "/ToDoAction.do";
	   	 	}		    
	   	}
	});	
}
/*弹出框显示*/
function AddToDoShow(data){
	var datas = data.split("|");
	$("#AddToDo").html($("#copyAddToDo").html());
	$("#AddToDo").show();
	$("#delId").show();
	$("#updateFlag").val("update");
 	$("#myformId").val(datas[0]);
	$("#toTitle").val(revertTextCode(datas[1]));
	$("#type").html(datas[2]);
	$("#clientId").val(datas[3]);	
	$("#taskId").val(datas[5]);
	if(datas[5] != ""){
		$("#appoint-other").text("已指派任务:"+datas[5].split(";")[1]);
	}else{
		$("#appoint-other").text("指派任务:");
	}
	//附件
	if(datas[6] !=null && datas[6] !=""){
		for(var i=0;i<datas[6].split(";").length;i++){
			if(datas[6].split(";")[i]!=""){
				var afax = '<li class="file_li" id="uploadfile_'+datas[6].split(";")[i]+'">'
				+'<input type="hidden" id="affix" name="todo" value="'+datas[6].split(";")[i]+'"><div class="showAffix">'+datas[6].split(";")[i]+'</div>'
				+'<a class="del-a-btn" href=\'javascript:deleteload("'+datas[0]+'","'+datas[6].split(";")[i]+'");\'>删除</a>'
				+'<a class="download-a-btn" href="/ReadFile?fileName='+datas[6].split(";")[i]+'&realName='+datas[6].split(";")[i]+'&tempFile=false&type=AFFIX&tableName=OAToDo" target="_blank">下载</a></li>';
				$("#affixuploadul_todo").append(afax);
			}
		}		
	}
	
	
	//填充数据
	var li ="";	
	if(datas[4] !=null && datas[4] != ""){
		var names = datas[4].split(";");	
		var namesId = datas[3].split(",");
		for(var i=0;i<names.length;i++){
			if(names[i] !=null && names[i] !=""){
				li += '<li id='+namesId[i]+' class="textboxlist-bit textboxlist-bit-box textboxlist-bit-box-deletable textboxlist-bit-hover textboxlist-bit-box-hover"><a onclick="yys(\''+namesId[i]+'\',\''+names[i]+'\')" class="selectlist">'+names[i]+'</a><a onclick="yy(\''+namesId[i]+'\')" class="textboxlist-bit-box-deletebutton"></a></li>'
			}			
		}
	}
	$("#clientName").html(li);
	$("#type").show();
}

//重写删除附件
function deleteload(Id,fileName){
	if(!confirm('确定要删除?')){
		return;
	}
		
	var url="/ToDoAction.do?operation=3&delFlag=affax&id="+Id;
	jQuery.ajax({
		type: "POST",
		url: url,
		cache: false,
		data:{
			afax:fileName
		},
		success: function(msg){
		  	if(msg==3){
		  		var li = jQuery("li[id='uploadfile_"+fileName+"']");				
				li.html("<input type='hidden' name='uploadDelAffix' value='"+fileName+"'>")				
				li.find("div").remove();
		  		//$("#uploadfile_"+fileName).remove();
		  		//$("#AddToDo").hide();
		  	}
		}
	});
}

/*删除*/
function delForm(id){
  	if(!confirm("确定要删除？")){
		return false;
	} 
	var ckFlag = "";
	if(id == ""){
		id = $("#myformId").val();//判断是否是打开窗口删除1 bushi 
		ckFlag = "1";
	}   	 
    var url = "/ToDoAction.do?operation=3&id="+id;
	 jQuery.ajax({
  	 	type: "POST",url: url,
  	 	data:{
  	 		selType:$("#selType").val()
  	 	},		   
   	 	success: function(msg){
   	 		if(msg == "3"){  	 		
   	 			asyncbox.tips('删除成功','success');
   	 			//var das = msg.split(";");
   	 			var obj = $("#"+id);
   	 			$(obj).hide(500);  		 			
	    		if(ckFlag =="1"){
	    			$("#AddToDo").hide();	
	    		} 				
   	 			if($("#tab").val() !="over"){
   	 				var oldunOver = $("#unOver").text();//旧的待办数量
   	 				$("#unOver").text(parseFloat(oldunOver)-1);  	 				
   	 			}else{
   	 				var oldoversd = $("#oversd").text();
   	 				$("#oversd").text(parseFloat(oldoversd)-1);
   	 				var oldpagelen = $(".page-number").html().length;
   	 				var oldpagenum = $(".page-number").html().substring(1,oldpagelen-1);
   	 				var newnum = parseFloat(oldpagenum)-1;
   	 				$(".page-number").html("("+newnum+")");
   	 			}			  	 		
   	 			    		
   	 		}		   	 		    
   	 	}
	});				
}
		/*弹出框保存*/
function formSubimt(){	  	  		
	var context=trimStr($("#toTitle").val());	
	var selType=$("#type").html();	
	var color = $("#nextColor").val();
	var affix = "";//附件
	//附件
	$("#affixuploadul_todo").find(":input[name='todo']").each(function(){
		affix += $(this).val() +";"; 
	})
	//var color = $("#uType li[class='li-sel']").attr('bg'); 	  				
   	if(context.length<=0){
   	 	highlight($("#toTitle"),5);
   	 	return false;
   	 }
   	var flagType="";
   	$(".uType>li").each(function(){				
		
		if($(this).attr('class') == "li-sel" ){
			flagType = $(this).attr('name');
		}				  			 	
	})	
   	 if($("#updateFlag").val() == "update"){        	 	
   	  	var url = "/ToDoAction.do?operation=2&updateFlag=update";			
   	 }else{
   	 	 var url = "/ToDoAction.do?operation=1";      	              
   	 }   
   	jQuery.ajax({
		type: "POST",url: url,
		data:{
	   	 		context:context,
	   	 		selType:selType,
	   	 		clientId:$("#clientId").val(),
	   	 		id:$("#myformId").val(),
	   	 		alertHours:$("#alertHours").val(),
	   	 		alertDay:$("#alertDay").val(),
	   	 		alertMinute:$("#alertMinute").val(),
	   	 		taskId:$("#taskId").val(),
	   	 		affix:affix   	 		
	   	 	},	   
	 	success: function(msg){
	 		if(msg != ""){
	 			if(msg.indexOf("err")>-1){
	 				asyncbox.tips(msg,'error');
	 			}else if(msg.length>60){
	 				location.href = "/ToDoAction.do";
	 			}else{
		 			$("#AddToDo").hide();
		 			if($("#updateFlag").val() == "update"){ 
		 				asyncbox.tips('修改成功 ','success');
		 				$("#"+$("#myformId").val()+">i").text(context);		 				
		 				$("#"+$("#myformId").val()).find(".pa").css('background','#'+color);
		 				$("#"+$("#myformId").val()).find(".ug_pa").text(selType);
		 				//$("#"+$("#myformId").val()).remove();
		 				//autoAdd(msg,context,selType,color);
		 			}else{
		 				asyncbox.tips('添加成功 ','success');
		 				if(flagType ==""){		 				
		 					autoAdd(msg,context,selType,color,"all");
		 				}else if(trimStr(flagType) == selType){
		 					autoAdd(msg,context,selType,color,"");
		 				}	 					
		 			}
	 			}			   	 			 	 			 	 			
	 		}else{
	 			asyncbox.tips('操作失败 ','error ');
	 		}		   	 		    
	 	}
	});		
}

/*更改分类**/
function changeTypePre(typeDiv,spanId,Id){
	
	var DivObj = $("#"+typeDiv);
	DivObj.show();
	DivObj.attr('bg',Id);
	//alert(DivObj);
	var IdObj= $("#"+spanId);
	IdObj.append(DivObj);
}
function changeType(type){
	var id = $("#publicType").attr('bg');
	jQuery.ajax({
		type:"post",
		url:"/ToDoAction.do?operation=2&typeFlag=type&id="+id+"&type="+type,
		success:function(msg){
			if(msg == "3"){ 	
				asyncbox.tips("更改成功","success"); 		
   	 			//form.submit();
   	 			window.location.reload();	   	 		 	  	 		
   			}else if(msg.length>60){
		 		location.href = "/ToDoAction.do";
		 	}else{
   				asyncbox.tips("更改失败","error");
   			}		      		
		}
	})
}


/*取消*/
 function closeForm(){	
	$("#AddToDo").hide();		
}
/*填充分类*/
   function fillType(obj,color){
   	$("#typeDisplay").hide();
   	$("#type").html(obj).show();
   	$("#nextColor").val(color);
   
   }
   
/*更改颜色*/
function fillColor(color){
	var type = $("#select-color").attr('bg');
	var url = "/ToDoAction.do?operation=15&type="+type+"&color="+color;
	 jQuery.ajax({
	  	 type: "POST",
	   	 url: url,		   
	   	 success: function(msg){
	   	 	if(msg == "3"){
	   	 		//location.href = "/ToDoAction.do?operation=4";
	   	 		form.submit();	   	 		 	  	 		
	   		}else if(msg.length>60){
		 		location.href = "/ToDoAction.do";
		 	}		    
	   	}
	});
	event.stopPropagation();
}
/*获取颜色*/
function getColor(ID,name){
	if(name=="未分类"){
		return false;
	}
	var b = $("#"+ID);	
	$("#select-color").css('left',b.position().left+5);
	$("#select-color").css('top',b.position().top+28);
	//b.append($("#select-color"));
	$("#select-color").show();
	$("#select-color").attr('bg',name);		
	event.stopPropagation();
}
/*页面切换*/
function pageSubmit(num){
	$("#pageNo").val(num);
	form.submit();
}
/*指定页*/
function submitQuery(){
	form.submit();
}

/*验证长度*/
function checkLenght(obj,flag){
	var len = 7;
	if(flag=="context"){
		len = 50;
	}
	if(getStringLength(obj.value)>2*len){
		obj.value=getTopic(obj.value,len,'');
	}
}


/*附件上传*/
function uploadFile(){
	
}


/*删除分类*/
function deleteType(name,ID){
	if(!confirm("是否要删除此分类下所有数据？")){
		event.stopPropagation();	
		return false;		
	}	
	 var url = "/ToDoAction.do?operation=3&delFlag=TYPE&type="+name;
	 jQuery.ajax({
	  	 type: "POST",
	   	 url: url,		   
	   	 success: function(msg){
	   	 	if(msg == "3"){
	   	 		var addpre = $("#"+ID);
				var li = $("#li_"+ID);
				var lil = $("#lil_"+ID);
				var second_li = $("#second_"+ID);
				var three_li = $("#three_"+ID);
				var num = 0;
				$("#proListU>li").each(function(){
					if($(this).find('[name=oldType]').val() == name){
						$(this).find('[name=oldType]').parent().remove();
						num++;
					}
				})			
	   	 		$(addpre).remove();
	   	 		$(li).remove();
	   	 		$(lil).remove();
	   	 		$(second_li).remove();
	   	 		$(three_li).remove();
	   	 		if($("#tab").val() !="over"){
   	 				var oldunOver = $("#unOver").text();//旧的待办数量
   	 				$("#unOver").text(parseFloat(oldunOver)-num);  	 				
   	 			}else{
   	 				var oldoversd = $("#oversd").text();
   	 				$("#oversd").text(parseFloat(oldoversd)-num);
   	 				var oldpagelen = $(".page-number").html().length;
   	 				var oldpagenum = $(".page-number").html().substring(1,oldpagelen-1);
   	 				var newnum = parseFloat(oldpagenum)-num;
   	 				$(".page-number").html("("+newnum+")");
   	 			}	
	   		}else{
	 			asyncbox.tips('删除失败!','error');
	   		}		    
   		} 
	});
	event.stopPropagation();
}


/*取消提醒**/
function cancelTime(id){
	//$("#set").hide();
	if(!confirm("确定要取消？")){
			return false;
	}		
	var url = "/ToDoAction.do?operation=2&updateFlag=time";
	jQuery.ajax({
 	type: "POST",url: url,
 	data:{     
 		id:id,   	 		         	 		
  	 	alertTime:""
    	 	},	   
	 	success: function(msg){
	 		if(msg == "3"){
 				asyncbox.tips('提醒已取消!','success');
 				$("#time_"+id).find(".alertTime").attr('value','');	
 				var idss = $("b[name=iconsId_"+id+"]");
 				$(idss).attr('title','提醒');	
 				$(idss).removeAttr("onclick");
 				$(idss).bind('click', function(){
 					displayTime('time_'+id,'oldForm',id);
				}); 			
  	 			$(idss).css('background-position','-204px -187px');	
	 		}else{
	 			asyncbox.tips('取消失败!','error');
	 		}   	 			   	 		    
 		}
	});	 
}

/*多条件过滤查询**/
function moreData(obj){
	
	 if($(obj).attr("sel") == "f"){
		$(obj).attr("sel","t").addClass("checked");
	 }else{
	 	$(obj).attr("sel","f").removeClass("checked");
	 }	
	 var ids ="";
	 var names ="";
	 $(".uType>li").each(function(){
		if($("b",this).attr("sel") == "t"){
			names += $("b",this).parents("li").attr('name')+";";
			ids += $("b",this).parents("li").attr('id')+";";					
   		}
	 })
	 $("#selType").val(names);
	 $("#selTypeId").val(ids);
	 form.submit();
	 event.stopPropagation();
}

/*快速添加任务方法&添加任务打开*/
function addTask(){	
	$("#addTaskDivs").html("");
	$("#updateTaskDivs").html("");
	var taskId = $("#taskId").val();	
	
	
	if(taskId != "" && taskId != undefined){
		var Id = taskId.split(";")[0];
		$.ajax({
			type: "POST",
			url: "/OATaskAction.do",
			data: "operation=7&taskId="+Id,
			success: function(msg){		
						
				$("#updateTaskDivs").html(msg);
				$("#updateTaskDivs").show();
				$(".addWrap").css("top","127px");
				$(".addWrap").css("left","35%");
				publicDragDiv($(".addWrap"));	
				loadTextBox('participantInfo');
			}
		});	
	}else{
		$.ajax({
			type: "POST",
			url: "/OATaskAction.do",
			data: "operation=6",
			success: function(msg){								
				$("#addTaskDivs").html(msg);
				$("#addTaskDivs").show();
				$(".addWrap").css("top","127px");
				$(".addWrap").css("left","35%");
				$("#hideBg").show();
				loadTextBox('participantInfo');
				$("#title").val($("#toTitle").val());
				publicDragDiv($(".addWrap"));
									
			}
		});
		/*$("#addTaskDivs").html($("#copyAddTask").html());
		$(".addWrap").css("top","127px");
		$(".addWrap").css("left","35%");		
		$("#addTaskDivs").show();		
		$("#title").val($("#toTitle").val());*/	
	}
	$("#hideBg").show();
	publicDragDiv($(".addWrap"));
}
/*提交任务*/
function taskSubmit(obj){
	var title = $.trim($("#title").val());//标题
	var remark = $.trim($("#remark").val());//内容
	var executor = $("#executor").val();//负责人

	var beginTime = $.trim($("#beginTime").val());//开始时间

	var endTime = $.trim($("#endTime").val());//结束时间
	var surveyor = $("#surveyor").val();//验收人

	var itemId = $("#itemId").val();//关联项目
	var operation = $(obj).attr("operation");//1:新增 2:修改
	var schedule = $("#schedule").val();//进度
	var taskClientId = $("#taskClientId").val();//客户
	var status = $("#uptStatus").val();//状态	var participant = $("#participantInfo").val();//参与人员
	var emergencyLevel = $("#emergencyLevel").val();//紧急程度
	var oaTaskType = $("#oaTaskType").val();//任务分类
	
	var affix = "";//附件
	//附件
	$("#affixuploadul_affix").find(":input[name='affix']").each(function(){
		affix += $(this).val() +";"; 
	})
	//alert($("#itemId").find("option:selected").attr("itemEndTime"))
	if(title==""){
		highlight($("#title"),5);
		return false;
	}
	//判断负责人是否为空
	if(executor==""){
		highlight($("#executor"),5);
		return false;
	}
	//判断截止时间是否小于今天
	if(endTime==""){
		highlight($("#endTime"),5);
		return false;
	}
	if(schedule == undefined){
		schedule="0";
	}
	var today=new Date();//取今天的日期  
	var beginDate = new Date(Date.parse(beginTime));  
	var endDate = new Date(Date.parse(endTime));  
	if(beginDate>endDate){  
		alert("执行截止时间不得小于开始时间"); 
		highlight($("#endTime"),5);
		return false;
	} 
	
	if(itemId!=""){
		var itemEndTime = $("#itemId").find("option:selected").attr("itemEndTime");//关联项目结束时间
		var itemEndDate = new Date(Date.parse(itemEndTime));  
		if(endDate>itemEndTime){  
			alert("执行截止时间不得大于关联项目的结束时间："+itemEndDate); 
			highlight($("#endTime"),5);
			return false;
		} 
	}
	
	$("#hideBg").hide();
	$.ajax({
		type: "POST",
		url: "/OATaskAction.do",
		data: "operation="+operation+"&keyId="+$(obj).attr("keyId")+"&title="+encodeURIComponent(title)+"&remark="+encodeURIComponent(remark)+"&beginTime="+beginTime+"&endTime="+endTime+"&surveyor="+surveyor+"&itemId="+itemId+"&executor="+executor+"&status="+status+"&schedule="+schedule+"&participant="+participant+"&emergencyLevel="+emergencyLevel+"&oaTaskType="+oaTaskType+"&affix="+encodeURIComponent(affix)+"&clientId="+encodeURIComponent(taskClientId),
		success: function(msg){
		hideAsynId="";
			if(msg=="error"){
				asyncbox.tips('添加失败!','error');
			}else{				
				var taskId = msg.split(";")[0];								
				if(operation == "1"){
					asyncbox.tips('添加成功','success');
					$("#taskId").val(taskId+";"+msg.split(";")[2]);
					$("#toTitle").val(msg.split(";")[1]);
					$("#appoint-other").text("已指派任务给:"+msg.split(";")[2]);
					$("#addTaskDivs").hide();
				}else{
					asyncbox.tips('修改成功','success');
					$("#taskId").val($(obj).attr("keyId")+";"+$("#executorName").val());
					$("#toTitle").val(title);
					$("#appoint-other").text("已指派任务给:"+$("#executorName").val());
					$("#updateTaskDivs").hide();
				}			
				
			}
		}
	});
	
}
function closeLayer(obj){
	hideAsynId ="";
	$("#hideBg").hide();
	$(obj).parent().parent().hide();
}


function displayTime(liId,flag,id){	
	$("#newFormId").val(id);
	var ids = "#"+liId;
	//alert($("#newFormId").val());
	$(ids).append($("#set"));
	$("#set").toggle();
}

/*客户弹出框*/
var nameArr = "";
function findClient(name){
	nameArr = name;
	if(name=="client"){
		var url ="/Accredit.do?popname=CrmClickGroup&inputType=checkbox";
		var title = "请选择客户";
	}
	if(name=="employee"){
		var url ="/Accredit.do?popname=userGroup&inputType=checkbox";
		var title = "请选择人";	
	}				
	asyncbox.open({
	   id : 'clientdiv',
	   title : title,
	　 url : url,
	　 width : 750,
	　 height : 430,
	   btnsbar : jQuery.btn.OKCANCEL.concat([{
     		text    : '清空 ',                  
      		action  : 'resetBtn'             
  		}]),
	   callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var str = opener.strData;
				openSelect(str,name)				
　　　　　	}
			if(action == 'resetBtn' && name =="client"){
				$("#clientName").html("");	
				$("#clientId").val("");						
　　　　　	}
			if(action == 'resetBtn' && name =="employee"){
				$("#toIdName").html("");	
				$("#toId").val("");						
　　　　　	}
　　　	}
　	});
}
function openSelect(str,name){
	if(str != "" && str !="undefined"){
		var data = str.split("|");
		if(name=="client"){
			var clientIds = "";
			var oldIds = $("#clientId").val();
			var clientNames = $("#clientName").html();		
			for(var i=0;i<data.length;i++){
				var dataArr =data[i].split(";");
				if(oldIds.indexOf(dataArr[0]) <0 ){					
					oldIds += dataArr[0]+",";
					clientNames +='<li id='+dataArr[0]+' class="textboxlist-bit textboxlist-bit-box textboxlist-bit-box-deletable textboxlist-bit-hover textboxlist-bit-box-hover"><a onclick="yys(\''+dataArr[0]+'\',\''+dataArr[1]+'\')" class="selectlist">'+dataArr[1]+'</a><a onclick="yy(\''+dataArr[0]+'\')" class="textboxlist-bit-box-deletebutton"></a></li>'
					//clientNames += dataArr[1]+";";								
				}
			}
			$("#clientName").html(clientNames);
			$("#clientId").val(oldIds);
		}
		if(name=="employee"){
			var clientIds = "";
			var oldIds = $("#toId").val();
			var clientNames = $("#toIdName").html();		
			for(var i=0;i<data.length;i++){
				var dataArr =data[i].split(";");
				if(oldIds.indexOf(dataArr[0]) <0 ){					
					oldIds += dataArr[0]+",";
					clientNames += dataArr[1]+";";								
				}
			}
			$("#toIdName").html(clientNames);
			$("#toId").val(oldIds);
		}
	}
}
function yy(id){
	var ID = $("#"+id);
	ID.remove();
	var ids = $("#clientId").val();
	var newId = ids.replace(id+",","");
	$("#clientId").val(newId);
	event.stopPropagation();
}
function yys(id,name){
	mdiwin("/CRMClientAction.do?operation=5&type=detailNew&keyId="+id,name);
	event.stopPropagation();
}
/*双击回填字段*/
function fillData(datas){
	if(hideAsynId == "normalId"){
		$("#"+hideFieldName).val(datas.split(";")[0]);
	    $("#"+hideFieldName+"Name").val(datas.split(";")[1]);
	    jQuery.close(hideAsynId);
	}else if("participantBoxId" == hideAsynId){
		textBoxObj.add(datas.split(";")[1],datas.split(";")[0]);
		jQuery.close(hideAsynId);
    }else{
		
		openSelect(datas,nameArr);
		jQuery.close('clientdiv');
	}
	
}
