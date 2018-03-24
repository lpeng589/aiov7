<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link type="text/css" rel="stylesheet" href="/style/css/broSort.css"/>
<link rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" type="text/css"  />

<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/jquery-ui2.js"></script>
<script language="javascript">  
$(function(){ 
     $("#sortable").sortable();  
 	 $("#sortable").disableSelection();  
 	 displayId();
});  
 
function del(){
	if(!isChecked('keyId')){
		alert('$text.get("common.msg.mustSelectOne")');
		return false;
	}
	if(confirm("$text.get("common.msg.confirmDel")")){
		items = document.getElementsByName("keyId");
		for(var i=0;i<items.length;i++){
		    if(items[i].checked){
		    	jQuery.ajax({ url: "/moduleFlow.do?operation=cancelMyDest&defaultSet=$defaultSet&id="+items[i].value, async: false});
		    	$("#"+items[i].value).parents(".tb_inner").remove(); 
		    	i--;	
		    }
		}
	}
}

function defaultSet(){
	document.form.action='/moduleFlow.do?operation=destDefaultSet';
	document.form.submit();

}

function myNavSet(){
	document.form.action='/moduleFlow.do?operation=destSet';
	document.form.submit();

}

function add(){
	urlstr ='/UserFunctionAction.do?operation=22&tableName=tblModules&popupWin=MyDestPopup&fieldName=linkAddress&selectName=SelectModulesAddress';
	var displayName = "$text.get("aio.lb.mynav")" ;
	urlstr += "&MOID=$MOID&MOOP=query&LinkType=@URL:&displayName="+encodeURI(displayName); 
	asyncbox.open({id:'MyDestPopup',title:displayName,url:urlstr,width:750,height:470});
}

function exeMyDestPopup(str){
	if(typeof(str)!="undefined"){
		navs = str.split("|");
		for(i=0;i<navs.length;i++){
			if(navs != ""){
				mods = navs[i].split(";");
				jQuery.ajax({ url: "/moduleFlow.do?operation=addMyDest&defaultSet=$defaultSet&title="+encodeURIComponent(mods[1])+"&url="+encodeURIComponent(mods[0]), async: false});	
			}
		}
	}
	window.location.reload();
}

function navClass(){
	window.location.href='/moduleFlow.do?operation=classSet&defaultSet=$!defaultSet';
}

 /*上移*/
function MoveUp(obj){
	($(obj).parent().parent().parent().parent()).insertBefore($(obj).parent().parent().parent().parent().prev());
	 displayId();
	 saveOrder();
}

/*下移*/
function MoveDown(obj){
	($(obj).parent().parent().parent().parent()).insertAfter($(obj).parent().parent().parent().parent().next());
	 displayId();
	 saveOrder();
}

function displayId(){
	var count=jQuery("#sortable li[name='num']").size();
	jQuery("#sortable li[name='num']").each(function(){
 		var num=$(this).index();
		if(num==0){
			$(this).find("a[id='moveUp']").css("display","none");
			$(this).find("a[id='moveDown']").css("display","inline");
		}else if(num==count-1){
			$(this).find("a[id='moveDown']").css("display","none");
			$(this).find("a[id='moveUp']").css("display","inline");
		}else{
			if($(this).find("a[id='moveUp']").css("display")=="none"){
				$(this).find("a[id='moveUp']").css("display","inline");
			}
			if($(this).find("a[id='moveDown']").css("display")=="none"){
				$(this).find("a[id='moveDown']").css("display","inline");
			}
		}
	});
}

//移除
function delOrder(obj){
	var delId=$(obj).attr("id");
	jQuery.ajax({ url: "/moduleFlow.do?operation=cancelMyDest&defaultSet=$defaultSet&id="+delId, async: false}); 	
	$("#"+delId).parents(".tb_inner").remove();
}
</script>
</head>
 
<body>
<form style="margin:0px;" method="post" scope="request" name="form"action="/moduleFlow.do?operation=destSet">
<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')"/>
<input type="hidden" name="type" value="$type"/>
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" bvalue="$!globals.getToken()"/>
<input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
	<div class="Heading">
		<div class="HeadingIcon">
			<img src="/$globals.getStylePath()/images/Left/Icon_1.gif"/>
		</div>
		<div class="HeadingTitle">
			我收藏的菜单
		</div>
		<!-- 
		<div class="HeadingTitle">
			#if("$defaultSet" == "true") $text.get("guide.lb.defguid") #else $text.get("aio.lb.mynav") #end
		</div>
		 -->
		<ul class="HeadingButton">
		<!-- 
			<li><button type="button" " class="b2" onClick="add();">$text.get("common.lb.add")</button></li>
			 -->
			<li><button type="button" onClick="del();" class="b2">$text.get("common.lb.del")</button></li>
			</li>
			#if("$LoginBean.id" == "1") 
				#if("$defaultSet" == "true")
					<li>
						<button type="button" onClick="myNavSet();" class="b2">$text.get("aio.lb.mynav")</button>
					</li>
				#else
				<!-- 
					<li>
						<button type="button" onClick="defaultSet();" class="b2">$text.get("guide.lb.defguid")</button>
					</li>
					 -->
				#end 
			#end
		</ul>
	</div>
	<div id="listRange_id">
		<div class="scroll_function_small_a" id="conter">
		<script type="text/javascript">
			$("#conter").height($(document).height()-50);
		</script>	
		<ul class="tb">
			<li>
		    	<ul class="tb_inner caption">
		        	<li class="cell1"><input name="selAll" type="checkbox" onClick="checkAll('keyId')"/></li>
		            <li class="cell2">$text.get("unit.lb.name")</li>
		            <li class="cell3">$text.get("common.lb.icon")</li>
		            <li class="cell4">操作</li>
		        </ul>
		    </li>
		    <ul #if("$type"!="destDefaultSet")	id="sortable" #end  >
			    #foreach ($row in $result ) 
			    	#if("$!globals.get($row,2)" != "")
				    <li name="num" style="cursor: move;" title="拖动上下排序">
				    	<ul class="tb_inner">
				        	<li class="cell1" style="cursor:default;"><input name="keyId" type="checkbox" value="$globals.get($row,0)" /></li>
				            <li class="cell2"><p>$globals.get($row,1)</li>
				            <li class="cell3">
								<div style="cursor:pointer;margin:0px 80px 0px 75px;" class="imgTarget">
									<img id="$globals.get($row,0)" src="$globals.get($row,3)" style="cursor:pointer;" onclick="showImages(this)" />
								</div>
				            </li>
				            <li class="cell4">
				           		<p>
			           			<a name="delSet" href="javascript:void(0);" onclick="delOrder(this);" style="cursor: pointer;" id="$globals.get($row,0)">删除</a>
									#if("$type"!="destDefaultSet")	
										<a id="moveUp" style="cursor: pointer;margin-left:5px;" onclick="MoveUp(this);">上移</a>
										<a id="moveDown" style="cursor: pointer; margin-left:5px; " 
											onclick="MoveDown(this);">下移</a>
									#end
								</p>
							</li>
				        </ul>
				    </li>
				    #end
				#end
		 	</ul>
		</ul>
	</div>
	<div id="showImage" destId="" class="imageBox"></div>
</form>
</body>
</html>
<script language="JavaScript">
jQuery.get("/moduleFlow.do?operation=getAllIcon", function(data){ 
	var kps = data.split(";");
	var showImage = $("#showImage");
	for(i=0;i<kps.length;i++){
		if(kps[i] != ""){showImage.append("<li><img src='"+kps[i]+"' onclick='selectme(this)'></li>");}
	}	
 });
 
$(document).click(function(event){
	var targetLen = $(event.target).parents(".imgTarget").length;
	if(targetLen==0){
		$("#showImage").hide();
	}
});

function showImages(obj){
	var showImage = $("#showImage");
	showImage.attr("destId",$(obj).attr("id"));
	if($(obj).position().top+285>$(document).height()){
		showImage.css("top",($(obj).position().top-265)+"px");
	}else{
		showImage.css("top",($(obj).position().top+40)+"px");
	}
	showImage.show();
}

function selectme(obj){
	var imageSrc = $(obj).attr("src");
	jQuery.get("/moduleFlow.do?operation=changePic&defaultSet=$defaultSet&id="+$("#showImage").attr("destId")+"&pic="+encodeURIComponent(imageSrc));
	$("#"+$("#showImage").attr("destId")).attr("src",imageSrc);
	$("#showImage").hide();
}
</script>

