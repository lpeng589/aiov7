<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="/style/css/jquery.contextmenu.css" type="text/css"/>
<link rel="stylesheet" href="/js/ui/jquery-ui-1.8.18.custom.css" type="text/css"/>
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/ui/jquery-ui-1.8.18.custom.min.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript"> 
	//界面改造 时，1、先显示界面，2、隐藏无权限字段，3赋值（添加默认值，修改原值）

var childCount=0;	
var childData = new Array();
#foreach ($rowlist in $childTableList ) 
#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$!rowlist.isUsed"=="1"  && "$!rowlist.isView"=="0")
#set($gridtable =$rowlist.tableName+"Table")
childData[childCount]='$gridtable';
childCount++;
#end#end

function changeChildTab(){
	type= document.getElementById("childfloatType").value;
	if(type==0){
		//分页
		$("#childTabHead",document).show();
		for(i=0;i<childData.length;i++){
			$("#"+childData[i]+"Title",document).hide();
		}
		for(i=1;i<childData.length;i++){
			$("#"+childData[i]+"Panel",document).css("display","none");
		}
	}else{
		//平铺
		$("#childTabHead",document).hide();
		for(i=0;i<childData.length;i++){
			$("#"+childData[i]+"Title",document).show();
			$("#"+childData[i]+"Panel",document).show();
		}
	}
}
function viewTab(div){
	for(i=0;i<childData.length;i++){		
		if(childData[i] == div){
			$("#"+childData[i]+"Panel",document).show();
		}else{
			$("#"+childData[i]+"Panel",document).hide();
		} 
	}
}
function clearSelectObj(obj){
	if(objType=="li"){
		obj.find("div").removeClass("selectedObjDiv");
	}else if(objType=="ul"){
		obj.removeClass("selectedObjDiv");
	}
}
//增加主表信息显示栏
function addPanel(){
	$("#topConter",document).append("<div class='listRange_1' ><ul class='wp_ul'></ul></div>");	
}
//显示添加字段界面
function showAddField(type){ 
   asyncbox.open({
　　　url : "/ViewConfigAction.do?tableName=$tableName&moduleType=$moduleType&winCurIndex=$winCurIndex&f_brother=$!f_brother&parentCode=$!parentCode&parentTableName=$!parentTableName",
　　　width : 400,
　　　height : 300,
   title :'添加字段',
   btnsbar : jQuery.btn.OKCANCEL, 
　　 callback : function(action){
　　　　　if(action == 'ok'){
　　　　　　　alert(this.id);
　　　　　}
　　 }
	
　}); 
	
}
//显示属性面板
var showPanelId="";
function showAttr(){
	if(selObject.attr("id")==showPanelId){
		return;
	}
	showPanelId = selObject.attr("id");
	apUl = $("#attrPanelUl",document);
	apUl.html("");
	if(objType=="ul"){	
		//操作
		apUl.append("<li><label>操作:</label><span><a href=''> 添加字段</a>&nbsp;&nbsp;&nbsp; <a href=''> 删除</a></span></li>");
		apUl.append("<li><label>移动:</label><span><a href=''> 前移</a> &nbsp;&nbsp;&nbsp;<a href=''> 后移</a></span></li>");
		
		//名称	
		name= selObject.find(".mTitle span").html();
		if(name=="null") name="";
		apUl.append("<li><label>名称:</label><span><input id=nameId name=nameId  type=text value="+name+"></span></li>");
		$("#nameId",document).keyup(function (){
			selObject.find(".mTitle").remove(); 
			if($(this).val()!=""){
				selObject.prepend("<div class=mTitle><span >"+$(this).val()+"</span></div>"); 
			}
		});
		$("#nameId",document).change(function (){
			selObject.find(".mTitle").remove(); 
			if($(this).val()!=""){
				selObject.prepend("<div class=mTitle><span >"+$(this).val()+"</span></div>"); 
			}
		});
		//布局
		apUl.append("<li><label>布局:</label><span><select id=ulCanvas><option value=flow>自由平铺</option><option value=2>固定2格</option><option value=3>固定3格</option><option value=4>固定4格</option><option value=5>固定5格</option></select></li>");
		$("#ulCanvas",document).val(selObject.attr("dcav"));
		$("#ulCanvas",document).change(function(){
			selObject.attr("dcav",$(this).val()+"");
			if($(this).val()=="flow"){
				selObject.find("ul").css("width","");
			}else{
				selObject.find("ul").css("width",(Number(selObject.attr("dlw"))+Number(selObject.attr("diw")))*Number(selObject.attr("dcav")) +"px");
			}
			
		});
		//标签宽度
		apUl.append("<li><label>标签宽度:</label><span><input id=labelWidth  type=text value="+selObject.attr("dlw")+"></span></li>");
		$("#labelWidth",document).change(function(){
			selObject.attr("dlw",$(this).val()+"");
			selObject.find("li").css("width",Number(selObject.attr("dlw"))+Number(selObject.attr("diw"))+"px");
			selObject.find("label").css("width",$(this).val()+"px");
			if(selObject.attr("dcav")!="flow"){
				selObject.find("ul").css("width",(Number(selObject.attr("dlw"))+Number(selObject.attr("diw")))*Number(selObject.attr("dcav")) +"px");
			}
			
		});
		$("#labelWidth",document).keyup(function(){
			selObject.attr("dlw",$(this).val()+"");
			selObject.find("li").css("width",Number(selObject.attr("dlw"))+Number(selObject.attr("diw"))+"px");
			selObject.find("label").css("width",$(this).val()+"px");
			if(selObject.attr("dcav")!="flow"){
				selObject.find("ul").css("width",(Number(selObject.attr("dlw"))+Number(selObject.attr("diw")))*Number(selObject.attr("dcav")) +"px");
			}
			
		});
		//输入框宽度
		apUl.append("<li><label>输入框宽度:</label><span><input id=inputWidth  type=text value="+selObject.attr("diw")+"></span></li>");
		$("#inputWidth",document).change(function(){
			selObject.attr("diw",$(this).val()+"");
			selObject.find("li").css("width",Number(selObject.attr("dlw"))+Number(selObject.attr("diw"))+"px");
			selObject.find("span").css("width",$(this).val()+"px");
			selObject.find("input").css("width",(Number($(this).val())-20)+"px");
			selObject.find("select").css("width",(Number($(this).val())-20)+"px");			
			if(selObject.attr("dcav")!="flow"){
				selObject.find("ul").css("width",(Number(selObject.attr("dlw"))+Number(selObject.attr("diw")))*Number(selObject.attr("dcav")) +"px");
			}
			
		});
		$("#inputWidth",document).keyup(function(){
			selObject.attr("diw",$(this).val()+"");
			selObject.find("li").css("width",Number(selObject.attr("dlw"))+Number(selObject.attr("diw"))+"px");
			selObject.find("span").css("width",$(this).val()+"px");
			selObject.find("input").css("width",(Number($(this).val())-20)+"px");
			selObject.find("select").css("width",(Number($(this).val())-20)+"px");			
			if(selObject.attr("dcav")!="flow"){
				selObject.find("ul").css("width",(Number(selObject.attr("dlw"))+Number(selObject.attr("diw")))*Number(selObject.attr("dcav")) +"px");
			}
			
		});
	}else if(objType=="li"){
	
	}
}


var selObject;
var cloneObj;
var objType;
var mDownOn = false;
var showdragView = false;
var mx=0;
var my=0;
var offx=0;
var offy=0;




$(document).ready(function() {
	changeChildTab();
	
	$(".listRange_1",document).mouseover( function() { $(this).addClass("ulMouse"); } ); 
	$(".listRange_1",document).mouseout( function() { $(this).removeClass("ulMouse"); } ); 
	$(".wp_ul li",document).mouseover( function() { $(this).addClass("liMouse"); return false;} ); 
	$(".wp_ul li",document).mouseout( function() { $(this).removeClass("liMouse"); return false;} ); 
	
	$(".wp_ul li",document).mousedown( function() {
		if(event.button==0){
		 	if(selObject){ clearSelectObj(selObject);} 
		 	$(this).find("div").addClass("selectedObjDiv"); 
		 	selObject=$(this); 	 	
		 	objType = "li";
		 	mx = event.clientX;
		 	my = event.clientY;
		 	mDownOn = true;
		 	return false;
	 	}
	 } ); 
	 
	$(".listRange_1",document).mousedown( function() {  	
		if(event.button==0){
			if(selObject){ clearSelectObj(selObject);} 
			$(this).addClass("selectedObjDiv"); 
			selObject=$(this);  
			objType = "ul";
			mDownOn = true;		
				
			return false;
		}		
	} ); 
	
	$(document).mouseup( function() { 
	 	mDownOn = false;
	 	showdragView = false;	 	
	 	if(selObject && istrash){
	 		selObject.remove();
	 		if(cloneObj) cloneObj.remove(); 
	 	}else{
		 	if(selObject) selObject.children().show();	 	
		 	if(cloneObj) cloneObj.remove(); 	 	
		 	//显示属性面板
		 	if(selObject) showAttr();
	 	}
	 	$("#trashDIV",document).hide();
	 	return false;
	 } );
	
	var istrash = false;
	$("#trashDIV",document).mouseover( function() { $(this).find("img").attr("src","/style/images/trashOpen.png"); istrash=true; } ); 
	$("#trashDIV",document).mouseout( function() { $(this).find("img").attr("src","/style/images/trashClose.png"); istrash = false;} ); 
		
	$(document).mousemove( function() {						
	 	if(selObject && mDownOn && objType=='li'){ 	 
	 		if(!showdragView){
	 			cloneObj = selObject.clone();
	 			cloneObj.attr('id','cloneo');
	 			cloneObj.css('position','absolute');
	 			selObject.children().hide();			 	
			 	cloneObj.prependTo(selObject.parent());
			 	showdragView = true;
			 	offx= event.clientX-selObject.offset().left;
			 	offy= event.clientY-selObject.offset().top;
			 	//显示垃圾桶
			 	$("#trashDIV",document).show();
			 	
		 	}
		 		 	
		 	cloneObj.css("left",(event.clientX-offx)+"px").css("top",(event.clientY-offy)+"px");
		 	
		 	//除ul对象的坐标，高宽，计算当前光标是否在ul对象之内		 		 	
			$(".wp_ul",document).each(function(i) {					
				if(event.clientX > $(this).offset().left && event.clientX < $(this).offset().left+$(this).width() &&
					event.clientY > $(this).offset().top && event.clientY < $(this).offset().top+$(this).parent().height()){
					if($(this).children().size()==0){
						//ul中还没有元素
						$(this).append(selObject);
					}else{
						$("#vinfo",document).val("dddddd"+i);	
						$(this).find("li").each(function(j) {	
							if(event.clientX > $(this).offset().left && event.clientX < $(this).offset().left+$(this).width() &&
							event.clientY > $(this).offset().top && event.clientY < $(this).offset().top+$(this).height()){
								if(selObject.attr("id") != $(this).attr("id") && $(this).attr("id") != "cloneo"){
									if(event.clientX < $(this).offset().left+$(this).width()/2 || 
										((event.clientY < selObject.offset().top) &&
										 (event.clientX < selObject.offset().left+selObject.width()))){
										selObject.insertBefore($(this));
									}else{
										selObject.insertAfter($(this));
									}
									$("#Remark",document).val(selObject.attr("id")+":"+selObject.find("label").html()+":"+$(this).attr("id")+":"+$(this).find("label").html()+":"+
									event.clientX +":"+ $(this).offset().left +":"+ ( $(this).offset().left+$(this).width()) +":"+
							event.clientY +":"+ $(this).offset().top +":"+ ( $(this).offset().top+$(this).height())+
							(event.clientY < $(this).offset().top)
							);	
								}															
							}
							
						});		
					}
				}
			});
	 	} 
	 	return false;
	 } ); 	
});
</script>
<style type="text/css">
/*主要控制参数*/
.listRange_1 .swa_c1{float:left;display:inline-block;width:100px}
.listRange_1 .swa_c2{float:left;display:inline-block;padding:0 0 0 2px;}
.listRange_1 .swa_c3{float:left;display:inline-block;padding:2px 0 0 0;}

/*属性框项目*/
.at_ul {padding:0px;}
.at_ul li{margin:2px 0 0 0;}
.at_ul li label{white-space: normal;width: 100px;float: left;padding-right:2px;text-align: right;margin:0px;}
.at_ul li span{width: 140px;}
.at_ul li span input{width: 140px; border:1px solid #8CBEDC;box-sizing:border-box;border-radius:3px;}


.wp_ul {height:25px;}
.wp_ul li{margin:2px 0 0 0;}
.wp_ul_6 li{margin:2px 20px 0  0;}

.add_Ul {height:25px;}
.add_Ul li{margin:2px 0 0 0;}
.add_Ul li label{white-space: normal;width: 100px;float: left;padding-right:2px;text-align: right;margin:0px;}
.add_Ul li span{display: none;border:1px solid #ff00ff}

.listRange_1 .mTitle span{border:1px solid #000000;}
.listRange_1 li{white-space:normal;width:260px;}
.listRange_1 li div {width:100%;height:25px}
.listRange_1 li div label{white-space: normal;width: 120px;float: left;text-align: right;margin:0px;}
.listRange_1 li div span{width: 140px;float:left;text-align:left}


/*项目LI，鼠标移入*/
.liMouse {}
.liMouse div{border:1px solid #ff0000;cursor:move}
.liMouse span{cursor:move}
.liMouse label{cursor:move}
.liMouse input{cursor:move}
.liMouse select{cursor:move}

/*外层面板，鼠标移入*/
.ulMouse{border:1px solid #ff0000;cursor:move}
/*已选择*/
.selectedObjDiv {border:1px solid #ff0000;}

</style>

</head>
<body style="overflow:auto;">
<form name="form" method="post"  action="/UserFunctionAction.do?tableName=$tableName&moduleType="+moduleType+"&operation=$globals.getOP("OP_VIEW_CONFIG")&configType=viewConfig" >

<table style="table-layout:fixed;width:100%">
<tr><td rowspan=2>
<div id="conter" style="overflow:scroll;">	
<div id="listRange_id" >
<script type="text/javascript">
var oDiv2=$("#listRange_id");
var sHeight=document.documentElement.clientHeight-32;
var sWidth=document.documentElement.clientWidth-32;
if(sHeight>0){
	oDiv2.height(sHeight);
	oDiv2.width(sWidth);
}
</script>
<div id="topConter">
<div class="listRange_1" dlw="120" diw="140" dcav="flow" >
<ul class="wp_ul" >
#foreach ($row in $mainFieldInfos )			
#if("$row.inputType" != "100" && "$row.inputType" != "3" && "$row.inputType" != "6" && $row.fieldName != "id" && "$row.fieldName"!="moduleType" && "$row.fieldType" != "3"  && "$row.fieldType" != "16"  && "$row.fieldType" != "13" && "$row.fieldType" != "14"  )
#if("$row.inputType" == "2")	
	#if("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1")	
	<li id="li_$dismh"><div>
		<label>$row.display.get("$globals.getLocale()") </label>
		<span>：<input id="$dismh" name="$dismh"  type="text"></span></div>
	</li>
	#elseif(!$row.getSelectBean().relationKey.hidden)
	<li id="li_$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)"><div>
		<label>$row.display.get("$globals.getLocale()") </label>
		<span>：<input id="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)" 
		name="$row.getSelectBean().getFieldName($row.getSelectBean().relationKey.parentName)"  type="text"></span></div>	
	</li>	
	#end
	#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
		#foreach($srow in $row.getSelectBean().viewFields)#set($colName="")		
			#if($!srow.display!="")
			#if($!srow.display.indexOf("@TABLENAME")==0)
				#set($index=$srow.display.indexOf(".")+1)#set($tableField=$tableName+"."+$srow.display.substring($index))
			#else
				#set($tableField=$srow.display)
			#end
			#set($colName="$srow.display")
			#else#set($tableField="$srow.fieldName")#set($colName="$srow.fieldName")
			#end
			#set($dis = $globals.getFieldDisplay($tableField))	
			#if($dis == "")#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName))#end			
			#if("$srow.hiddenInput"=="true")
			#else
				#if("$row.fieldType" == "18")
				<li id="li_$globals.getTableField($srow.asName)" style="width:95%;"><div>
					<label>$dis </label>
					<span>：<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)"  type="text"     ></span>	</div>				
				</li>
				#else
				<li id="li_$globals.getTableField($srow.asName)"><div>
					<label>$dis </label>
					<span>：<input id="$globals.getTableField($srow.asName)" name="$globals.getTableField($srow.asName)"  type="text"     ></span>	</div>			
				</li>				
				#end 
				#set($tableField="")
			#end				
		#end				
	#end
#elseif("$row.inputType" == "1" || "$row.inputType" == "16")
	<li id="li_$row.fieldName"><div>
		<label>$row.display.get("$globals.getLocale()") </label>
		<span>：<select id="$row.fieldName" name="$row.fieldName"  type="text"     ></select></span>	</div>
	</li>
#elseif("$row.inputType" == "5" )
#set($enumList=$globals.getEnumerationItems($row.refEnumerationName))
	<li id="li_$row.fieldName"><div>
		<label>$row.display.get("$globals.getLocale()") </label>
		<span>：
		#foreach($erow in $enumList)
<input type="checkbox" class="cbox" name="$row.fieldName" id="c_${row.fieldName}_${erow.value}" value="$erow.value" /><label for="c_${row.fieldName}_${erow.value}" class="cbox_w">$erow.name</label>
#end</span></div>					
	</li>	
#elseif("$row.inputType" == "10" )
#set($enumList=$globals.getEnumerationItems($row.refEnumerationName))
	<li id="li_$row.fieldName"><div>
		<label>$row.display.get("$globals.getLocale()") </label>
		<span>：
#foreach($erow in $enumList)
<input type="radio" class="cbox"  name="$row.fieldName" id="c_${row.fieldName}_${erow.value}" value="$erow.value" /><label class="cbox_w" for="c_${row.fieldName}_${erow.value}" >$erow.name</label>
#end</span></div>					
	</li>	
#else	
	<li id="li_$row.fieldName"><div >
		<label >$row.display.get("$globals.getLocale()") </label>
		<span >：<input id="$row.fieldName" name="$row.fieldName"  type="text"     ></span>	</div>				
	</li>	
#end
#end
#end

</ul>

</div>
</div><!-- topConter -->


<!-- 以上代码 主表表头 -->

<div id="childTalCon">
<div id="childTabHead">
#foreach ($rowlist in $childTableList ) 
#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$!rowlist.isUsed"=="1"  && "$!rowlist.isView"=="0")
#set($gridtable =$rowlist.tableName+"Table")
	<span style="font-weight:bolder;border:1px solid #666666;" onclick="viewTab('${gridtable}')">$rowlist.display.get($globals.getLocale())</span>
#end#end	
</div>
<div id="childTabContent">
#foreach ($rowlist in $childTableList ) 
#if("$globals.getSysSetting($rowlist.tableSysType)"=="true" && "$!rowlist.isUsed"=="1"  && "$!rowlist.isView"=="0")
#set($gridtable =$rowlist.tableName+"Table")
#set($colNum=0)
#set($totalcolWidth=0)
<div class="scroll_function_small_ud" id="${gridtable}Panel">
	<span id="${gridtable}Title" style="font-weight:bolder;color:#666666;">$rowlist.display.get($globals.getLocale())</span>
	<div  name="${gridtable}DIV" id="${gridtable}DIV"> 
	<table border="0" cellpadding="0"  cellspacing="0" class="listRange_list_function_b" width="2000" name="$gridtable" id="$gridtable">
	<thead>
	<tr><td class="listheade" width="35" align="center" style="color: rgb(0, 0, 255);">No.</td>
		<td width="25" align="center" style="cursor: pointer; color: rgb(0, 0, 255);"><img title="Ctrl++" src="/style/images/add.gif" border="0" ></td>
		#foreach ($row in $rowlist.fieldInfos )		
		#if("$row.inputType" != "100" && "$row.inputType" != "3" && "$row.inputType" != "6" && $row.fieldName != "id" && "$row.fieldName"!="moduleType" && "$row.fieldType" != "3"  && "$row.fieldType" != "16"  && "$row.fieldType" != "13" && "$row.fieldType" != "14"  )
		#if("$row.inputType" == "2")
			#if(!$row.getSelectBean().relationKey.hidden || ("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
			<td width="$row.width" align="center" style="color: rgb(0, 0, 255);">$row.display.get("$globals.getLocale()")</td>
			#set($totalcolWidth=$totalcolWidth + $row.width)
			#set($colNum=$colNum +1)
			#end
			#if(!("$row.fieldSysType"=="GoodsField" and $globals.getPropBean($row.fieldName).isSequence=="1"))
				#foreach($srow in $row.getSelectBean().viewFields)#set($colName="")		
					#if($!srow.display!="")
						#if($!srow.display.indexOf("@TABLENAME")==0)
							#set($index=$srow.display.indexOf(".")+1)#set($tableField=$rowlist.tableName+"."+$srow.display.substring($index))
						#else
							#set($tableField=$srow.display)
						#end
						#set($colName="$srow.display")
					#else#set($tableField="$srow.fieldName")#set($colName="$srow.fieldName")
					#end
					#set($dis = $globals.getFieldDisplay($tableField))
					#if($dis == "")#set($dis = $globals.getFieldDisplay($rowlist.tableName,$row.getSelectBean().name,$srow.fieldName))#end						
					#set($returnValue=$globals.getFieldWidth("$rowlist.tableName","$globals.getTableField($srow.asName)"))
					#if($returnValue!=0)
						#set($disWidth=$returnValue) 
					#else
						#set($disWidth=$srow.width) 
					#end		
					#if("$srow.hiddenInput"=="true")
					#else
						<td width="$disWidth" align="center" style="color: rgb(0, 0, 255);">$dis   </td>
						#set($colNum=$colNum +1)
						#set($totalcolWidth=$totalcolWidth + $disWidth)
						#set($tableField="")
					#end				
				#end				
			#end			
		#else
		<td width="$row.width" align="center" style="color: rgb(0, 0, 255);">$row.display.get("$globals.getLocale()")</td>
		#set($colNum=$colNum +1)
		#set($totalcolWidth=$totalcolWidth + $row.width)
		#end
		#end
		#end
	</tr>
	</thead>
	<tbody>
	#foreach($ti in[1..3])
	<tr height="22px"><td class="listheadonerow" align="center" width="20">1</td>
		<td align="center" width="25" style="cursor:pointer" rowid="1">	<div title="Ctrl+-" class="delImg" ></div></td>
		#foreach($ci in[1..$colNum])
		<td ></td>
		#end
	</tr>
	</tbody>
	#end
	</table>
	</div>
</div>	
<script type="text/javascript">
	$("#$gridtable").width($totalcolWidth);
</script>
#end	
#end
</div>
</div>
<!-- 以上代码 明细表 -->

<div><span style="color:#0000FF;line-height:20px">$!mainTable.tableDesc.replaceAll("\r\n","<br>")</span></div>
<!-- 以上代码 表描述 -->

#foreach ($row in $mainFieldInfos )
#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.inputType" !="6" && "$row.fieldType" == "3" && "$row.width"!="-1")
<div class="listRange_1_photoAndDoc_1"><span #if("$row.inputType"=="8")style="color: #C0C0C0;"#end>$row.display.get("$globals.getLocale()")：</span>
<textarea id="$row.fieldName" name="$row.fieldName" #if("$row.inputType"=="8") style="color: #C0C0C0;" readonly="readonly"#end rows="6"></textarea>
</div>
#end	
#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "16" && "$row.inputType"!="6"  && "$row.width"!="-1")
<div class="listRange_1_photoAndDoc_2">
<span class="docTitle">$row.display.get("$globals.getLocale()")：</span>
<div>					
<textarea id="$row.fieldName" name="$row.fieldName" style="width:100%;height:300px;visibility:hidden;"></textarea>
</div>
</div>
#end	
#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.fieldType" == "14" && "$row.inputType"!="6")		
<div class="listRange_1_photoAndDoc_1">
<span>$row.display.get("$globals.getLocale()")：</span>
<button type="button" name="affixbuttonthe222" onClick="upload('AFFIX');" class="b4">$text.get("upload.lb.affixupload")</button>
<ul id="affixuploadul" style="width:98%;">
</ul></div>
#end
#if("$row.inputType" != "100" && "$row.inputType" !="3" && "$row.inputType"!="6" && "$row.fieldType" == "13" )				
<div class="listRange_1_photoAndDoc_1">
<span>$row.display.get("$globals.getLocale()")：</span>
<button type="button" name="picbuttonthe222" onClick="upload('PIC');" class="b4">$text.get("upload.lb.picupload")</button>
<ul id="picuploadul" style="width:98%;">
</ul></div>
#end	
#end


<div class="clear"></div>

</div>

</div>
</td>
<td width="250;" valign="top" height="150">&nbsp;
<div style="width:100%;height:100%;">
	<div ><input type="button" id="btdefault" name="btdefault" onclick='selObject.find(".mTitle").hide();' value="默认"></div>
	<div class="abc"><input type="button" id="btdefault" name="btdefault" onclick='addPanel()' value="增加栏目"></div>
	<div class="abc"><input type="button" id="btdefault" name="btdefault" onclick='showAddField("mainTable")' value="增加字段"></div>
	<div>明细表显示方式
		<select id="childfloatType" name="childfloatType" onchange="changeChildTab()">
			<option value="0">分页</option>
			<option value="1">平铺</option> 
		</select>
	</div>
</div>
</td>
</tr>
<tr>
<td valign="top" >
<div style="width:100%;padding-top:30px">
	<div>属性</div>
	<div id="attrPanel" style="width:100%;height:100%;border:1px solid #000000">
		<ul id="attrPanelUl" class="at_ul">
		</ul>
	</div>
</div>
</td>
</tr>
</table>


</form>
<!-- 垃圾桶 -->
<div id="trashDIV" style="position:absolute;display:none;right:140px;top:0px;z-index:0"><img border=0 src="/style/images/trashClose.png"/></div>

</body>
</html>
