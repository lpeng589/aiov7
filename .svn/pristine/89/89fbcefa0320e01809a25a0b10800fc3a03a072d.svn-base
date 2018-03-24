<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>group</title>
<link type="text/css"  rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" />
<link type="text/css"  rel="stylesheet" href="/vm/oa/album/ui/ztree/demoStyle/demo.css"/>
<link type="text/css"  rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" />
<link type="text/css"  rel="stylesheet" href="/style1/css/oa_news.css" />
<link type="text/css"  rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>
<script type="text/javascript">
	var zTree1;
	var setting;
	var zNodes;
	#if("$!data"=="")
		zNodes = [];
	#else
		zNodes=$!data;
	#end
 	#if("$!AccreditSearchForm.inputType" == "checkBox" || "$!AccreditSearchForm.inputType" == "checkbox" || "$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "defineClass" || "$!AccreditSearchForm.popname" == "communicationGroup" || "$!AccreditSearchForm.popname" == "clientGroup" || "$!AccreditSearchForm.popname" == "CrmClickGroup") 
	setting = {
 		#if("$!AccreditSearchForm.popname" != "userGroup" && "$!AccreditSearchForm.popname" != "defineClass")
		checkable : true,
		#end
		expandSpeed : false,
		checkType : {"Y":"s", "N":"ps"}, 
		showLine: true,
		callback: {
				click: zTreeOnClick,
				#if("$!AccreditSearchForm.popname" == "deptGroup" || "$!AccreditSearchForm.popname" == "empGroup")
				change:	zTreeOnChangedept
				#end
				#if("$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "communicationGroup" || "$!AccreditSearchForm.popname" == "clientGroup" || "$!AccreditSearchForm.popname" == "CrmClickGroup")
				change: zTreeOnChangeuser
				#end
		}
	};
	#else
	setting = {
		#if("$!AccreditSearchForm.popname" != "userGroup")
		checkable: true,
		checkStyle: "radio",
		checkRadioType: "all",	 //level 在每一级内进行进行分组控制 ,all 在整个 Tree 内进行分组控制







		#end
		callback: {
			click: zTreeOnClick,
			#if("$!AccreditSearchForm.popname" == "deptGroup" || "$!AccreditSearchForm.popname" == "empGroup")
			change:	zTreeOnChangedept
			#end
		}
	};
	#end
	
	$(document).ready(function(){
		reloadTree();			
		//initCopyEvent();
	});
	
	//点击组







	function zTreeOnClick(event, treeId, treeNode) {
		checkeds(event,treeNode);
		//alert("22");
		jQuery("#firameMain").attr("src","/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&chooseParent=$!AccreditSearchForm.chooseParent&keyType=group&value="+treeNode.id);
	}
	
	function checkeds(event,treeNode) {
		var setting1 = clone(setting);
		var srcNode = zTree1.getSelectedNode();
		if (treeNode.open) {
			if (srcNode) {
				zTree1.expandNode(srcNode, false,null);
			}
		} else {
			if (srcNode) {
				zTree1.expandNode(srcNode, true,null);
			}
		}
	}
	
	var chooseData="$!chooseData";//已选值的编号
	var strData = "";  //返回的值



	//弹出部门框点击







	function zTreeOnChangedept(event, treeId, treeNode) {
		var tmp = zTree1.getChangeCheckedNodes();
			for(var i=0;i<tmp.length;i++){
				if(tmp[i].isParent){
					var childNodes = zTree1.getNodesByParam("pId", tmp[i].id, tmp[i]);
						var flagTrue=true;
						var flagFalse=false;
						for(var j=0;j<childNodes.length;j++){
							if(childNodes[j].checked){
								flagFalse=true;
							}else{
								flagTrue=false;
							}
						}
						if(flagTrue){
							for(var j=0;j<childNodes.length;j++){
								childNodes[j].checked = false;
								zTree1.updateNode(childNodes[j], false);
							}
						}else if(flagFalse && !flagTrue){
							tmp[i].checked = false;
							zTree1.updateNode(tmp[i], false);
						}
				}
			}
		tmp =zTree1.getChangeCheckedNodes();
		var tmparr = "";
		var strarr = "";
		for (var i=0; i<tmp.length; i++) {
			if(tmp[i].checked){
				tmparr += tmp[i].id+",";
				strarr += tmp[i].id+";";
				strarr += tmp[i].name+";|";
			}
		}
		chooseData = tmparr;
		strData = strarr;
		choose();
	}
	
	//弹出人员框点击







	var userDept= "";
	function zTreeOnChangeuser(event, treeId, treeNode){
		var tmp = zTree1.getCheckedNodes();
		tmp = zTree1.getChangeCheckedNodes();
		var tmparr = "";
		var strarr = "";
		for (var i=0; i<tmp.length; i++) {
			if(tmp[i].checked){
				tmparr += tmp[i].id+",";
			}else{
				delString(tmp[i].id);
			}
		}
		userDept = tmparr;
		var url = "/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&keyType=manygroup&value=";
		if(userDept!=""){
			url += userDept.substring(0,userDept.length-1);
		}
		jQuery("#firameMain").attr("src",url);
	}
	
	//移除编号
	function delString(ss){
		if(chooseData != ""){
			var str = "";
			var strings = chooseData.split(",");
			for (var i = 0; i<strings.length; i++){
				if(ss != strings[i]){
					str == string[i];
				}
				if(i != strings.length-1){
					str +=",";
				}
			}
			chooseData = str;
		}
	}
	
	function reloadTree(node) {
		var setting1 = clone(setting);
		setting1.treeNodeKey = "id";
		setting1.treeNodeParentKey = "pId";
		setting1.isSimpleData = true;
		zNodes1 = clone(zNodes);
		setting1.showLine = true;
		
		zTree1 = $("#treeDemo").zTree(setting1, zNodes1);
		var nodes = zTree1.getNodes();
		if(nodes.length > 0){
			var url = "/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&value=$!AccreditSearchForm.value&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&chooseParent=$!AccreditSearchForm.chooseParent";
			#if("$!AccreditSearchForm.popname" == "deptGroup" || "$!AccreditSearchForm.popname" == "empGroup")
			zTree1.selectNode(nodes[0]);
			var node = nodes[0];
			var nodeId = node.id;
			url += "&keyType=group&parameterCode=$!AccreditSearchForm.parameterCode&value="+nodeId;
			#end
			#if("$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "communicationGroup" || "$!AccreditSearchForm.popname" == "clientGroup" || "$!AccreditSearchForm.popname" == "CrmClickGroup")
			url += "&value=&keyType=&parameterCode=$!AccreditSearchForm.parameterCode&userCode=$!AccreditSearchForm.userCode";
			#end
			$("#firameMain").attr("src",url);
		}else{
			var url = "/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition";
			$("#firameMain").attr("src",url);
		}

		if("$!AccreditSearchForm.popname"=="staffGroup"){
			jQuery("#chang1").addClass("wt");
		}else if("$!AccreditSearchForm.popname"=="userGroup"){
			jQuery("#chang").addClass("wt");
		}else if("$!AccreditSearchForm.popname"=="jobGroup"){
			jQuery("#chang2").addClass("wt");
		}else if("$!AccreditSearchForm.popname"=="onLineGroup"){
			jQuery("#chang3").addClass("wt");
		}
		else if("$!AccreditSearchForm.popname"=="leaveGroup"){
			jQuery("#chang4").addClass("wt");
		}
	}	
	
	function showMenu(selectid){	
		var url="";	
		if(selectid == "staffGroup"){
			url="/AccreditAction.do?popname=staffGroup&value=&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&parameterCode=$!AccreditSearchForm.parameterCode&userCode=$!AccreditSearchForm.userCode&chooseData="+chooseData;
		}
		if(selectid == "userGroup"){
			url="/AccreditAction.do?popname=userGroup&value=&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&parameterCode=$!AccreditSearchForm.parameterCode&userCode=$!AccreditSearchForm.userCode&chooseData="+chooseData;
		}	
		if(selectid == "jobGroup"){
			url="/AccreditAction.do?popname=jobGroup&value=&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&parameterCode=$!AccreditSearchForm.parameterCode&userCode=$!AccreditSearchForm.userCode&chooseData="+chooseData;
		}
		if(selectid == "roleGroup"){
			url="/AccreditAction.do?popname=roleGroup&value=&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&parameterCode=$!AccreditSearchForm.parameterCode&userCode=$!AccreditSearchForm.userCode&chooseData="+chooseData;
		}	
		if(selectid == "onLineGroup"){
			url="/AccreditAction.do?popname=onLineGroup&value=&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&parameterCode=$!AccreditSearchForm.parameterCode&userCode=$!AccreditSearchForm.userCode&chooseData="+chooseData;
		}
		if(selectid == "leaveGroup"){
			url="/AccreditAction.do?popname=leaveGroup&value=&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&parameterCode=$!AccreditSearchForm.parameterCode&userCode=$!AccreditSearchForm.userCode&chooseData="+chooseData;
		}		
		jQuery.ajax({
			type:"post",
			url:url,		
			success:function(msg){
				if(selectid == "userGroup"){
					jQuery(".tree_w>ul").remove();
					jQuery("#treeDemo").show();					
				}else{
					var arr = msg.split("|");
					var ul ='<ul>';
					for(var i=0;i<arr.length;i++){
						if(arr[i] !=""){
							var obj = arr[i].split(",");
							ul += '<li><a href=\'javascript:newclick("'+selectid+'","'+obj[0]+'")\'>'+obj[1]+'</a></li>';											
						}
					}
					ul +='</ul>';
					jQuery(".tree_w>ul").remove();
					jQuery("#treeDemo").hide();
					jQuery(".tree_w").append(ul);
				}				
				jQuery("#firameMain").attr("src",url+"&operation=accreditInfo");
			}
		})
	
	}
	function toShowMenu(selectid){
		var id="#"+selectid;
		if(jQuery(id).css("display") == "block"){
			jQuery("#change").removeClass();
			jQuery("#change").addClass("ico_1");
			jQuery(id).hide();
		}else{
			jQuery(id).show();
			jQuery("#change").removeClass();
			jQuery("#change").addClass("ico_2");
		}
	}
	
	function shou(){
		window.location="/AccreditAction.do?popname=$!AccreditSearchForm.popname&value=&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&parameterCode=$!AccreditSearchForm.parameterCode&userCode=$!AccreditSearchForm.userCode&chooseData=$!chooseData";
	}
	function insertkeyword(){
		
		var keywordVal = jQuery("#keyWord").val();
		if(keywordVal=="关键字搜索" || keywordVal.trim()==""){
			//asyncbox.alert("请输入关键字！","搜索提示") ;
			//return false ;
			shou();
		}
		#if("$!AccreditSearchForm.popname" == "deptGroup" || "$!AccreditSearchForm.popname" == "empGroup")
		window.location="/AccreditAction.do?keyType=keyWord&popname=$!AccreditSearchForm.popname&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&value="+encodeURIComponent(keywordVal);
		#end
		#if("$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "communicationGroup" || "$!AccreditSearchForm.popname" == "clientGroup" || "$!AccreditSearchForm.popname" == "CrmClickGroup" 
		|| "$!AccreditSearchForm.popname" == "defineClass" || "$!AccreditSearchForm.popname" == "jobGroup" || "$!AccreditSearchForm.popname" == "staffGroup" || "$!AccreditSearchForm.popname" == "onLineGroup" || "$!AccreditSearchForm.popname" == "leaveGroup")
			jQuery("#firameMain").attr("src","/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&parameterCode=$!AccreditSearchForm.parameterCode&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&keyType=keyWord&value="+(keywordVal));
		#end
	}
	
	function choose(){
		var url = "/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&keyType=choose&value=";
		if(chooseData!=""){
			url += chooseData.substring(0,chooseData.length-1);
		}
		jQuery("#firameMain").attr("src",url);
	}
	
	function chooseUser(){
		var url = "/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&keyType=choose&value=";
		if(chooseData!=""){
			url += chooseData.substring(0,chooseData.length-1);
		}
		jQuery("#firameMain").attr("src",url);
	}
	
	function chooseRoot(){
		var url = "/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&keyType=&value=";
		jQuery("#firameMain").attr("src",url);
	}
	
	function showData(){
		var url = "/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition";
		url += "&keyType=group&parameterCode=$!AccreditSearchForm.parameterCode";
		$("#firameMain").attr("src",url);
	}
	
	function selectAll(){
		var checkNodes = zTree1.getCheckedNodes();
		var pNodes = zTree1.getNodesByParam("level", "0");//拿到父级node
		var tmparr = "";
		var strarr = "";
		if(jQuery("#selectAll").is(":checked")){
			for(var i=0;i<pNodes.length;i++){
				pNodes[i].checked = true;
				zTree1.updateNode(pNodes[i], false);//true标示关联父类子类,false标示不关联







			}
			tmp =zTree1.getChangeCheckedNodes();
			for (var i=0; i<tmp.length; i++) {
				if(tmp[i].checked){
					tmparr += tmp[i].id+",";
					strarr += tmp[i].id+";";
					strarr += tmp[i].name+";|";
				}
			}
			chooseData = tmparr;
			strData = strarr;
			choose();
		}else{
			for(var i=0;i<pNodes.length;i++){
				pNodes[i].checked = false;
				zTree1.updateNode(pNodes[i], false);//true标示关联父类子类,false标示不关联







			}
			chooseData = tmparr;
			strData = strarr;
			choose();
		}
	}
	function newclick(popname,obj){
		jQuery("#firameMain").attr("src","/AccreditAction.do?operation=accreditInfo&popname="+popname+"&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&keyType=group&value="+obj);
		//jQuery("#firameMain").attr("src","/AccreditAction.do?operation=accreditInfo&popname=$!AccreditSearchForm.popname&inputType=$!AccreditSearchForm.inputType&condition=$!AccreditSearchForm.condition&keyType=group&value="+obj);
	}
	/*回填到此页面*/
	function fillBack(){
		alert(1);
	}
	/*删除已选数据*/
	function delChooseId(Id){
		var delBackData = jQuery('#choose_'+Id).attr('bg');
		//firameMain.window.delstrData(delBackData);
		strData = strData.replace(delBackData,'');
		
	 	if(chooseData.indexOf(Id)>=0){
	 		chooseData=chooseData.replace(Id,'');
	 	}
	 	
		jQuery('#choose_'+Id).remove();
		$(window.frames["firameMain"].document).find("#tr_"+Id+">input[name='keyId']").attr("checked",false); 
	}
	function delAllChoose(){
		strData="";
		chooseData="";
		jQuery('.d-sel-wp>.sel-ul>li').remove();
		$(window.frames["firameMain"].document).find("input[name='keyId']").attr("checked",false);
	}
</script>

<style type="text/css">
div.zTreeDemoBackground {width:auto;}
ul#treeDemo {width:auto;overflow:auto;}
#keyWord{font-family:微软雅黑;font-size:12px;width:129px;}
.leftMenu2{height:27px;padding:0 0 0 0;line-height:27px;font-family:微软雅黑;font-size:12px;}
.tree li a{font-family:微软雅黑;}
.leftMenu2>div>em{display:inline-block;float:left;margin:0 4px 0 0;font-family:微软雅黑;}
.leftMenu2>div>em.wt{color:red;}
.ico_1{margin:9px 5px 0 5px;}
.icons{background-image:url(/style/images/item/icons.png);background-repeat:no-repeat;display:inline-block;}
.tree_w>ul{padding:10px 5px 0 5px;margin:0;font-family:微软雅黑;}
.tree_w>ul li{line-height:18px;float:left;display:inline-block;font-size:14px;padding:5px;background:#CBE6FF;margin:0 0 1px 1px;}
.tree_w>ul li:hover{background:#2E85C0;}
.tree_w>ul li:hover a{color:#fff;}
.d-sel-wp{position:absolute;right:35px;top:45px;overflow:hidden;overflow-y:auto;z-index:999;font-family:microsoft yahei;width:120px;padding:5px 0;height:235px;background:#FAFFF4;border:1px #bbb solid;}
.d-sel-wp>p{font-family:microsoft yahei;font-weight:bold;padding:0 5px;position:relative;}
b.remove-all{position:absolute;right:5px;top:0;cursor:pointer;font-family:宋体;font-weight:normal;color:#2E85C0;}
b.remove-all:hover{text-decoration:underline;}
.d-sel-wp>ul{text-align:left;margin:0;padding:0;}
.d-sel-wp>ul>li{position:relative;line-height:21px;padding:3px 15px 3px 10px;}
.b-del{display:none;width:10px;height:10px;right:2px;top:11px;opacity:0.7;background-position:-362px -217px;position:absolute;cursor:pointer;}
.b-del:hover{opacity:1;}
.d-sel-wp>ul>li:hover{background:#f2f2f2;}
.d-sel-wp>ul>li:hover .b-del{display:inline-block;}
</style>
</head>
<body style="text-align:left;overflow:hidden;">
	<table cellpadding="0" cellspacing="0" class="frame2" width="100%">
		<tr>
			<td width="20%" style="vertical-align:top; border-right:1px #a1a1a1 dashed;padding:5px 5px 0 0;">
				<div style="padding:5px 0 0 5px; width:99%">
					<input style="float:left;display:inline-block;" type="text" id="keyWord" name="keyWord" class="search_text" #if("$!AccreditSearchForm.keyType" == "keyWord" && "$!AccreditSearchForm.value" != "") value="$!AccreditSearchForm.value" #else value="关键字搜索" #end onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索...';" onfocus="if(this.value=='关键字搜索'){this.value='';}" />
					<input style="float:left;display:inline-block;" type="button" class="search_button" onclick="insertkeyword();"/>
				</div>
				<div class="leftModule" style="padding:3px 0 0 0;margin:0;border: 0px;">
				<div class="leftMenu">
					#if("$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "onLineGroup" || "$!AccreditSearchForm.popname" == "staffGroup"  || "$!AccreditSearchForm.popname" == "communicationGroup" || "$!AccreditSearchForm.popname" == "clientGroup" || "$!AccreditSearchForm.popname" == "CrmClickGroup" || "$!AccreditSearchForm.popname" == "jobGroup" || "$!AccreditSearchForm.popname" == "empGroup"|| "$!AccreditSearchForm.popname" == "defineClass")
					<!-- <div class="leftMenu2" onclick="chooseUser()">
						<span class="ico_1" id=""></span>已选数据 
					</div> -->
					#end
					#if("$!AccreditSearchForm.popname" == "defineClass")
					<div class="leftMenu2" onclick="chooseRoot()">
						<span class="ico_1" id=""></span>根目录 
					</div>
					#end
					
					#if("$!AccreditSearchForm.popname" == "deptGroup" || "$!AccreditSearchForm.popname" == "communicationGroup" || "$!AccreditSearchForm.popname" == "clientGroup" || "$!AccreditSearchForm.popname" == "CrmClickGroup" || "$!AccreditSearchForm.popname" == "empGroup")									
					<div class="leftMenu2" onclick="toShowMenu('left_tree');shou()">
						<span class="ico_2" id="change"></span>组目录
					</div>
					#end
					#if("$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "onLineGroup" || "$!AccreditSearchForm.popname" == "staffGroup"   || "$!AccreditSearchForm.popname" == "jobGroup" || "$!AccreditSearchForm.popname" == "leaveGroup")							
					<div class="leftMenu2" >
						<span class="ico_1" id=""></span>
						<div>												
							<em id="chang" class="" onclick="showMenu('userGroup');">部门</em>
							<em id="chang1" class="" onclick="showMenu('staffGroup');">分组</em>
							<em id="chang2" class="" onclick="showMenu('jobGroup');">职位</em>
							<em id="chang3" class="" onclick="showMenu('onLineGroup');">在线</em>
							#if("$!AccreditSearchForm.leavePerson" == "yes" )
							<em id="chang4" class="" onclick="showMenu('leaveGroup');">离职</em>
							#end							
						</div>
					</div>					
					#end		
					<div style="height:253px;overflow:auto;float:left;width:100%;"  id="treeMain">
						<div id="left_tree" style="display:block;float:left;margin-left: 10px;white-space: normal">
							<div class="zTreeDemoBackground">
								<!-- 
								<span style="font-weight:inherit;margin-left: 20px;">	 -->
								#if(("$!AccreditSearchForm.popname" == "deptGroup" || "$!AccreditSearchForm.popname" == "empGroup") && "$!AccreditSearchForm.inputType" != "radio")
									<input type="checkbox" name="selectAll" id="selectAll" onclick="selectAll();" />&nbsp;<label for="selectAll">全选</label>
								#end														
								<ul id="treeDemo" class="tree"></ul>
							</div>		
						</div>
						<div class="tree_w">					
							<!-- <ul>
							#foreach($user in $!newData)
							<li><a href='javascript:newclick("$!globals.get($!user,0)")'>$!globals.get($!user,1)</a>
							</li>
							#end
							</ul>	-->				
						</div>															
					</div>					
				</div>
				</div>
			</td>
			<td valign="top" class="list" id="listTd" style="padding:0 0 0 5px;">
				<iframe id="firameMain" frameborder=false src="" name = "firameMain" style="margin-top:10px;height:340px;" scrolling="no" width="100%"></iframe>
			</td>
		</tr>
	</table>
	#if("$!AccreditSearchForm.popname" == "userGroup" || "$!AccreditSearchForm.popname" == "onLineGroup" || "$!AccreditSearchForm.popname" == "staffGroup"   || "$!AccreditSearchForm.popname" == "jobGroup" || "$!AccreditSearchForm.popname" == "leaveGroup" || "$!AccreditSearchForm.popname" == "defineClass")	
		#if("$!AccreditSearchForm.inputType" !="radio")
		<div class="d-sel-wp" style="">
			<p>已选数据 <b class="remove-all" onclick="delAllChoose();">清空</b></p>
			<ul class="sel-ul">			
			</ul>
		</div>
		#end
	#end
</body>
    <script type="text/javascript"> 
		var oDiv=document.getElementById("treeMain");
		var sHeight=document.documentElement.clientHeight-60;
		oDiv.style.height=sHeight+"px";
	</script>
</html>