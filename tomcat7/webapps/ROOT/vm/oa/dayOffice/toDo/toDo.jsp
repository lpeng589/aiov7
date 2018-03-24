<!doctype html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
#if("$!addTHhead" == "true")
<title>$globals.getCompanyName()</title>
#else
<title>我的待办</title>
#end
<link type="text/css" href="/style/css/todo/MyToDo_1.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/oa/itemTaskPublic.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/oa/itemTaskPublic.js"></script>
<script type="text/javascript" src="/js/oa/oatodo.js"></script>
<script type="text/javascript" src="/js/includeAllTextBoxList.js"></script>
<script type="text/javascript" src="/js/crm/upload.vjs"></script>
<script type="text/javascript">
var textBoxObj;

/*加载控件*/
function loadTextBox(boxId){
	textBoxObj = new jQuery.TextboxList('#'+boxId, {unique: true, plugins: {autocomplete: {}}});
	textBoxObj.getContainer().addClass('textboxlist-loading');
	textBoxObj.plugins['autocomplete'].setValues($textBoxValues);
	textBoxObj.getContainer().removeClass('textboxlist-loading');
}



/*填充时间提醒*/
   function fillTime(time,obj){
   	var day="";
   	var hours="";
   	var mintues="";
   	var alertTime="";	    	
    var d = new Date();
   	if(time=="30"){
   		$(name[0]).val("$!globals.getDate()");
   		if(d.getMinutes() > 30){
   			hours = d.getHours()+1;
   			mintues=d.getMinutes()-30;
   		}else{
   			hours=d.getHours();
   			mintues=d.getMinutes()+30;
   		}
   		alertTime = "$!globals.getDate()" +" "+hours+":"+mintues+":00"; 		
    }
   	if(time=="60"){	    		
   		if(d.getHours() == 23){
   			day="$!globals.getTomorrowDate()";
   			hours=d.getHours();
   		}else{
   			day="$!globals.getDate()";
   			hours=d.getHours()+1;
   		}   			   			
  			mintues=d.getMinutes(); 
  			alertTime = day +" "+hours+":"+mintues+":00";  		
   	}
   	if(time=="one"){
   		day="$!globals.getTomorrowDate()";   		
  		hours=d.getHours();
		if(d.getMinutes() >= 10){
			mintues=d.getMinutes(); 
		}else{
			mintues="0"+d.getMinutes();
		}   
  		alertTime = day +" "+hours+":"+mintues+":00";			  		
   	}
   	 
   	if(time == "me"){
   		if($("#aday").val() =="" && ($("#hours").val() != "" || $("#minutes").val() != "")){
        		 highlight($("#aday"),5);       		 
        		var e = event || window.event;
        		 return false;
        		
       	 	}
       	 	
       	 	if($("#aday").val() < "$!globals.getDate()"){      	 	  
        		 highlight($("#aday"),5);       		 
        		var e = event || window.event;
				window.event?e.cancelBubble=true:e.stopPropagation();
        		 return false;      		         		 
       	 	}
       	 	if($("#aday").val() == "$!globals.getDate()" && $("#hours").val() < d.getHours()){
        		 highlight($("#aday"),5);
        		 event.stopPropagation();
        		 return false;
       	 	}  
       	 	if($("#aday").val() == "$!globals.getDate()" && $("#hours").val() <= d.getHours() && $("#minutes").val() <= d.getMinutes()){
        		 highlight($("#aday"),5);
        		var e = event || window.event;
				window.event?e.cancelBubble=true:e.stopPropagation();
        		 return false;
       	 	}      	   	 
   		day=$("#aday").val();   		
 		hours=$("#hours").val();
 		mintues=$("#minutes").val(); 
 		alertTime = day +" "+hours+":"+mintues+":00";
 			  		
   	}
   
   	//主界面提交  	
  		var id = $("#newFormId").val();
  		var url = "/ToDoAction.do?operation=2&updateFlag=time";
  		jQuery.ajax({
  	 	type: "POST",url: url,
  	 	data:{     
  	 		id:id,   	 		         	 		
     	 	alertTime:alertTime
       	 	},	   
   	 	success: function(msg){	
   	 		if(msg =="3"){
 				asyncbox.tips('提醒设置成功!','success');
 				var idss = $("b:[name=iconsId_"+id+"]");
 				$(idss).remove();	
				var li = '<b  class="icons s-remind" style="background-position:-123px -155px;" onclick="cancelTime(\''+id+'\');" name="iconsId_'+id+'" id="iconsId" title="取消提醒"></b>'                                     	                     
                $("#time_"+id).find(".alertTime").attr('value',alertTime);						 	 			   	 		    
				var obb = $("#time_"+id);
				$(obb).prepend(li);	
   	 		}else if(msg.length>60){
   	 		//用户是否被T出


   	 			location.href = "/ToDoAction.do";
   	 		}else{
   	 			asyncbox.tips(msg,'error');
   	 		} 
   	 		$("#aday").val("$!globals.getDate()");   	 				
  	 	}
	});	  			    	
   	$("#set").hide();
   	$("#show_t").hide();
     	
   }

/**加载被选中的,判断是否待办，隐藏分页*/   
function loadPic(){
	/*
	if($("#tab").val() == "" || $("#tab").val() == undefined){
		$(".scott").hide();
	}else{
		var oldpagelen = $(".page-number").html().length;
	   	var oldpagenum = $(".page-number").html().substring(1,oldpagelen-1);
		if(oldpagenum<16){
			$(".scott").hide();
		}
	}
	*/
	var id = $("#selTypeId").val();
	var ids = id.split(";");
	//alert(id)
	if(ids.length>0 && ids !=""){
	$(".uType>li>b").attr('sel','f').removeClass("checked");
		for(var i=0;i<ids.length;i++){		
			if(ids[i] !=""){
				var obj = $("#"+ids[i]+">b");
				
				$(obj).attr('sel','t').addClass("checked");
			}			
		}				
	}
	var e = event || window.event;
	window.event?e.cancelBubble=true:e.stopPropagation();
	//event.stopPropagation();
}
$(document).ready(function() {
	$(".p-block b.cbox").click(function(){
		$(this).addClass("checked").siblings("b.cbox").removeClass("checked");
		$("#color").val(($(this).attr('bg')));
	});
})

</script>
<style type="text/css">
.cls{background-color:#ffcccc;}
.hideBg{ display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;  height: 100%;  background-color:rgba(0, 0, 0, 0.23);  z-index:98;  -moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);}
</style>
</head>
<body onload="loadPic();showStatus();">
#if("$!addTHhead" == "true")
#parse("./././body2head.jsp")
<div>
#else
<div id="scroll-wrap">
#end
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" name="form" action="/ToDoAction.do" >
<input type="hidden" name="operation" value=""/>
<input type="hidden" name="tab" id="tab" value="$!tab"/>
<input type="hidden" name="selType" id="selType" value="$!selType"/>
<input type="hidden" name="selTypeId" id="selTypeId" value="$!selTypeId"/>
<input type="hidden" name="fdContxt" id="fdContxt" value="$!fdContxt"/>
<input type="hidden" name="flagAll" id="flagAll"/>
<input type="hidden" name="myformId" id="myformId" value=""/>
<input type="hidden" name="updateFlag" id="updateFlag" value=""/>
<input type="hidden" name="newFormId" id="newFormId" value=""/>
<input type="hidden" id="taskId" name="taskId" />
<input type="hidden" name="color" id="color" value="da4c4b"/>
<input type="hidden" name="addTHhead" id="addTHhead" value="$addTHhead"/>
<div class="wrap" id="conter">
<script type="text/javascript">
var height = document.documentElement.clientHeight-10;
$("#scroll-wrap").height(height);
$("#conter").css("min-height",height);
</script>
        <!-- 头部header 分割线 Start -->
        <div class="header">
            <div class="left-title">
                <b class="icons WaitPhoto"></b>
                <em>我的待办</em>
                <div class="t-search">
                	<input class="t-search-txt" type="text" id="findcontxt" value="$!fdContxt" placeholder="输入要找的待办信息" />
                    <b class="t-search-btn icons" title="搜索" onclick="findContxt();"></b>
                </div>
                <div class="t-btns">
                	<span  #if("$!tab" == "over")class="t-finish"#else class="t-todo" #end onclick="tabPage();"><i>待办</i><em id="unOver">$!unOver</em></span>
                    <span #if("$!tab" == "over")class="t-todo"#else class="t-finish" #end onclick="tabPage('over');"><i>完成</i><em id="oversd">$!oversd</em></span>
                </div>
            </div>
            <span class="add" id="newTrans" onclick="showDiv();">
            	<b class="icons ab" ></b>
                <em>新建待办</em>
            </span>   
    
        </div>
        <!-- 头部header 分割线 End -->
        <!-- td_main 分割线 Start -->   
        <div class="td-main">
        	<div class="lf td-main-left">
            	<div class="quick-publish">
                	<input  class="lf publish-txt" id="context" name="context" onkeydown="checkLenght(this,'context');"
                	 placeholder="输入待办后回车" >
                    <span class="lf pr publish-btn" onclick="quaikAdd();"><b class="pa icons"></b></span>
                </div>
            	<!-- 项目列表 proList 分割线 Start -->
                <div class="proList">
                 <ul class="proListU" id="proListU">  
                 #foreach($log in $!List)
                 	<li id="$!log.id" title='$!log.title'>
                 	<input type="hidden" name="oldId" value="$!log.id"/>
                 	<input type="hidden" name="oldTitle" value="$!log.title"/>
                 	<input type="hidden" name="oldType" value="$!log.type"/>         
                 	<input type="hidden" name="oldclientId" value="$!log.relationId"/>       
                 	<input type="hidden" name="oldclientName" value='#foreach($user in $globals.listEmpNameByUserId("$!log.relationId"))$!user.name;#end '/>
                
                	#if("$!tab" == "over")
                	 <b class="lf icons bCheck" title="取消完成" style="background-position:-234px -92px;" id="sureOver" onclick="sureOver('$!log.id','NO');">                   
                     </b> 
                	#else
                	  <b class="lf icons bCheck" id="sureOver" title="勾上完成" onclick="sureOver('$!log.id','YES');">                   
                      </b> 
                	#end             
                      <i class="lf iContent" onclick='updateTram("$!log.id");'>$!log.title</i> 
                      <span class="lf pr typeWait" id="color_$!log.id">
                      	<b class="pa"  style="background:#$!mapList.get($!log.type);cursor: pointer;" onclick="getColor('color_$!log.id','$!log.type');"></b>
                        <span class="ug_pa" style="cursor: pointer;" onclick='changeTypePre("publicType","color_$!log.id","$!log.id");'>$!log.type</span>
                 
                      </span>                      
                      <b class="lf time">#if("$!log.createTime.substring(0, 10)" == "$globals.getDate()")今日$!log.createTime.substring(11, 19)                      
                      #elseif("$!log.createTime.substring(0, 10)" == "$!yestoday" )昨天$!log.createTime.substring(11, 19)#elseif("$!log.createTime.substring(0, 10)" == "$!oldthree")前天$!log.createTime.substring(11, 19)
                      #else$!log.createTime#end</b>
                      <span class="pr rf tiXing" id="time_$!log.id">
                      #if("$!tab" != "over")
                      #if("$!log.alertTime" == "")                     
                      <b  class="icons s-remind" onclick="displayTime('time_$!log.id','oldForm','$!log.id');" name="iconsId_$!log.id" id="iconsId" title="提醒"></b>
                      #else
                      <b  class="icons s-remind" style="background-position:-123px -155px;"  onclick="cancelTime('$!log.id');" name="iconsId_$!log.id" id="iconsId" title="取消提醒"></b> 
                      
                      #end
                      #else
                      <b  class="icons s-remind"  ></b>
                      #end  
                      <input type="text" class="alertTime" value="$!log.alertTime" />                      
                      </span>
                      <b class="del-type icons" title="删除" onclick="delForm('$!log.id');"></b>
                    </li> 
                    #end                 
                </ul>                  
                </div>
                <!-- 项目列表 proList 分割线 End -->    
            </div>
            <div class="lf td-main-right">
            <ul class="uType" id="uType">    
            	<li  onclick="allType();">
                	<b class="b-cbox" style="background-color:#438ab4"></b> 
                	<i>全部分类</i>
                </li>
                <li  name="未分类" id="1_1"  onclick="selectType('未分类','1_1');" style="border-bottom: 1px dashed #d1d1d1;" #if("$!selTypeId" == "1_1") class="li-sel" #end>
                	<b class="b-cbox checked" onclick="moreData(this);"  sel="t"></b>
                	<i>未分类</i>
                	
                </li>        
        		#foreach($log in $!type)       	 	
            	<li  onclick="selectType('$globals.get($!log,1)','$globals.get($!log,0)');" bg="$globals.get($!log,3)" name="$globals.get($!log,1)" id="$globals.get($!log,0)" #if("$!selTypeId"=="$globals.get($!log,0)") class="li-sel" #end>              	
                	<b class="b-cbox checked" sel="t" id="lil_$globals.get($!log,0)" style="background-color:#$globals.get($!log,3)" onclick="moreData(this);" ></b>
                    <i>$globals.get($!log,1)  </i>   
                    <em class="update-type icons" title='更改颜色' onclick="getColor('$globals.get($!log,0)','$globals.get($!log,1)');"></em>               
                    <em class="del-type icons" id="li_$globals.get($!log,0)" onclick="deleteType('$globals.get($!log,1)','$globals.get($!log,0)');"></em>                          
                </li>               
                #end            
            </ul>
            <ul id="ulAddType">
            
                <li class="addBlock" id="addBlock">
                	<div onclick="displayNo('add-cal-type','');"><b class="bg-icons"></b>
                	添加分类</div>
                	<div class="add-cal-type" id="add-cal-type" style="display:none;">
						<input class="inp-txt" type="text" id="addpe" onkeydown="checkLenght(this,'addpe');" placeholder="输入分类名称" />
						<p>选择分类颜色</p>
						<div class="p-block" id="p-block">
							<b class="cbox cbox-color-1 checked" bg="da4c4b"></b>
							<b class="cbox cbox-color-2" bg="ec6754"></b>
							<b class="cbox cbox-color-3" bg="e88b3b"></b>
							<b class="cbox cbox-color-4" bg="cb9e5d"></b>
							<b class="cbox cbox-color-5" bg="a8bb48"></b>
							<b class="cbox cbox-color-6" bg="68aa63"></b>
							<b class="cbox cbox-color-7" bg="358560"></b>
							<b class="cbox cbox-color-8" bg="3caaa9"></b>
							<b class="cbox cbox-color-9" bg="438ab4"></b>
							<b class="cbox cbox-color-10" bg="457198"></b>
							<b class="cbox cbox-color-11" bg="6c74a3"></b>
							<b class="cbox cbox-color-12" bg="9670c7"></b>
							<b class="cbox cbox-color-13" bg="d25db5"></b>
							<b class="cbox cbox-color-14" bg="e9599e"></b>
							<b class="cbox cbox-color-15" bg="b1846f"></b>
						</div>
						<div class="p-block">
							<input class="lf btn-add" type="button" value="添加分类" onclick="addType('addpe');">
							<input class="lf btn-cel" type="button" value="取消" onclick="canselType();">
						</div>
						<b class="arrow-point"></b>											
					</div>
                </li>
                
                <!-- 
                <li id="addtype" style="display: none"><input  style="width: 100px;" id="addpe" name="addpe" onkeydown="checkLenght(this,'addpe');" />
                <div class="btn btn-mini" onclick="addType('addpe');">确定</div> -->
                </li>
            </ul>    
            
            </div>
            <div style="clear:both;"></div>
        </div>
        <!-- td_main 分割线 End -->
	$!pageBar
	<div style="clear:both;"></div>
	<div class="addWrap pop-layer" id="addTaskDivs" ></div>
	<div class="addWrap pop-layer" id="updateTaskDivs" ></div>
	
</div> 

</form>	
<div id="AddToDo" ></div>
<script type="text/html" id="copyAddToDo"> 
<!-- 弹出添加层 add 分割线 Start -->
	     
	    <!-- 编辑层  分割线 Start -->
		<div class="update-wrap" id="addItemDiv" >
		    <div class="u-title">
		        <span class="lf pr">
		            <b class="icons pa"></b>
		            编辑待办
		        </span>
		    </div>
		    <div class="update-body" >
		    	<div class="u-top">	            
		            <div class="update-content">
		               <textarea  style="border:1px solid #efefef;width:380px;height:80px"  maxlength="250" name="title" id="toTitle" ></textarea>
		            </div>
		            <span  id="appoint-other" class="appoint-other" onclick="addTask();">指派任务:
		            	        	
		            </span>
		        </div>
		        <ul class="update-operate" >

		            <li id="selectwin">
		            	<b class="icons b-type" onclick="displayNo('typeDisplay');"></b>	
		            	<span  onclick="displayNo('typeDisplay');">选择分类:
		            		<input type="hidden" id="nextColor" name="nextColor" />
		            		<i style="display:none;"id="type" name="type"></i>
		            	</span>
		            	<ul style="display: none" class="u-type" id="typeDisplay">
			            	<li class="addBlock"  class="li-sel" onclick="fillType('未分类','b4b3b3');">
		                		<b class="pa"></b>
		                		<i>未分类</i>
		                	</li> 
		        			#foreach($log in $!type)
		            		<li id="second_$globals.getFromArray($!log,0)" onclick="fillType('$globals.getFromArray($!log,1)','$globals.get($!log,3)');"> 
		            			<b class="pa" style="background:#$globals.get($!log,3)"  ></b>$globals.getFromArray($!log,1)               
		                	</li>
		                	#end
							<li class="addBlockCopy" style="padding-left: 15px;" id="addBlockCopy" onclick="displayNo('addtypeCopy','addBlockCopy');">
                				添加分类
                			</li>
                			<li id="addtypeCopy" style="display: none;">
								<input  style="width: 90px;height: 27px;border: none;" id="addpeCopy" name="addpeCopy" onkeydown="checkLenght(this,'addpeCopy');" />
                				<div class="btn btn-mini" onclick="addType('addpeCopy');">确定</div>
                			</li>               
		            	</ul>
		            </li>

		            <li onclick="findClient('client');" style="overflow:hidden;">
		            	<b class="icons b-relate"></b>
		            	<span class="lf" >关联客户:</span>
			        	<input type="hidden" id="clientId" name="clientId" />
			        	<div class="lf" style="border:0px;margin:-3px 0 0 0;width:300px;" id="clientName" name="clientName"></div>
			        	<div class="clear"></div>
		            </li>
					
					<li style="overflow:hidden;">
						<b class="icons b-up-file" style="background-position:-204px -219px;width:16px;" onClick="upload('AFFIX','todo');"></b>
		            	<span class="lf" onClick="upload('AFFIX','todo');">附件上传:</span>
						<ul id="affixuploadul_todo" class="add-tu" style="padding:0;"></ul>
					</li>
					
		        </ul>
				
				<div class="btn-items-wp">
	    			<span class="btn-items celBtn" style="float: left;display:none" id="delId" onclick="delForm('');">删除</span>
	    			<span class="btn-items celBtn" onclick="closeForm();">取消</span>
	        		<span class="btn-items conBtn"  onclick="formSubimt();">确定</span>
	    		</div>
			</div>	    
		</div>
	<!-- 编辑层 分割线 End -->       
	
</script> 	
  <script type="text/javascript">
	   
    </script>
<!-- 弹出添加层 add 分割线 End -->
<!-- 时间提醒 -->
 <ul class="pa uRemind" id="set" style="display: none">
	<li onclick="fillTime('30',this);"><b class="icons pa" ></b>半小时后</li>
    <li onclick="fillTime('60',this);"><b class="icons pa" ></b>一小时后</li>
    <li onclick="fillTime('one',this);"><b class="icons pa" ></b>明天此时</li>
    <li id="show_h" onclick="displayTmNo('show_t','set');"><b class="icons pa" ></b>指定时间</li> 

</ul>
    <div id="show_t" style="display: none;">
	    <input type="text" id="aday" style="width:70px" name="aday" value="$!globals.getDate()" onClick="openInputDate(this);" />
		    <select id="hours" name="hours">
		    	#foreach($log in [0..23])
		    	#if($log < 10)		    			    	
		    	<option #if("$log" == "8") selected="selected" #end>0$!log</option>		    	
		    	#else
		    	<option>$!log</option>
		    	#end
		    	#end
		    </select>
		     <select id="minutes" name="minutes">
		    	#foreach($log in [00,10,20,30,40,50])
		    	<option>$!log</option>
		    	#end
		    </select>
		<div class="btn btn-mini" onclick="fillTime('me',this);">确定</div>
 	</div>  
 </div>
 </div>
<script type="text/javascript">
	var oDiv=document.getElementById("scroll-wrap");
	var sHeight=document.documentElement.clientHeight;
	oDiv.style.height=sHeight+"px";

</script>

  <ul class="select-color" id="select-color" style="display: none;" bg="0">                    
  	<li onclick="fillColor('df7ba6')" style="background:#df7ba6"></li>                   	
  	<li onclick="fillColor('47a91c')" style="background:#47a91c"></li>
  	<li onclick="fillColor('db8933')" style="background:#db8933"></li>
  	<li onclick="fillColor('b54143')" style="background:#b54143"></li>
  	<li onclick="fillColor('3796bf')" style="background:#3796bf"></li>
  	<li onclick="fillColor('e5acae')" style="background:#e5acae"></li>
  	<li onclick="fillColor('aedfa3')" style="background:#aedfa3"></li>
  	<li onclick="fillColor('f3d1a8')" style="background:#f3d1a8"></li>
  	<li onclick="fillColor('a5daea')" style="background:#a5daea"></li>
  	<li onclick="fillColor('f4c9df')" style="background:#f4c9df"></li>
  	<li onclick="fillColor('43bc97')"style="background:#43bc97"></li>
  	<li onclick="fillColor('c7ad24')" style="background:#c7ad24"></li>
  	<li onclick="fillColor('cf69e2')" style="background:#cf69e2"></li>
  	<li onclick="fillColor('abe7d9')" style="background:#abe7d9"></li>
  	<li onclick="fillColor('efc0f6')" style="background:#efc0f6"></li>
  </ul>    
  <!--  -->
  <ul style="display:none;" class="u-type" id="publicType" bg="">
   	<li class="addBlock"  class="li-sel" onclick="changeType('未分类');">
      		<b class="pa"></b>
      		<i>未分类</i>
      	</li> 
	#foreach($log in $!type)
  	<li id="three_$globals.getFromArray($!log,0)" onclick="changeType('$globals.getFromArray($!log,1)');"> 
  		<b class="pa" style="background:#$globals.get($!log,3)"  ></b>$globals.getFromArray($!log,1)               
    </li>
      #end                
	</ul>
                           
 <div id="hideBg" class="hideBg"></div>
 <div id="closeDiv" style='width:135px;height:102px;position:absolute;left:650px;top:240px;display:none;z-index:10001;'><img src='/style/images/load.gif'/><div style='color:#0179bb;font-weight:bold;'>正在查询，请稍等......</div></div>               
</body>
</html>