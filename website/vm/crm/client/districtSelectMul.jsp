<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" type="text/css"/>
<link rel="stylesheet" href="/vm/oa/album/ui/ztree/demoStyle/demo.css" type="text/css"/>
<script src="/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>
<title>无标题文档</title>
<style>
body{padding:0px;margin:0px;font-size:12px;}
.area_body{
	width:770px;
	height:360px;
	background:#F6F6F6;
	padding-top:5px;
}

.area_left{
	width:220px;
	float:left;
	margin-left:5px;
	height:350px;
}
.area_left .search_left{float:left;width:100%;}
.area_left select{width:64px;float:left;margin-left:10px;margin-top:3px;}
.area_left .search_div{width:135px;height:25px;float:left;margin-left:11px;background:url(style/images/client/search.gif);}
.area_left .area_tree{float:left;width:218px;height:325px;border:1px solid #E8E5DC;background:#FEFBF1;}
.area_left .area_all{float:left;margin-left:20px;margin-top:3px;}
.area_left .area_all input{margin-right:3px;}
.area_left .input_search{float:left;width:100px;height:18px;background:#F3F3F3;margin:1px;border:0px;}

.area_center{
	width:333px;
	float:left;
	margin-left:10px;
	border:1px solid #BABFC4;
	height:350px;
	background:#fff;
}
.area_center .area_show{padding-left:10px;padding-top:5px;font-weight:bold;height:25px;text-align: left;}
.area_center ul{padding-left:10px;margin:0px;float:left;padding-top:5px;text-align: left;}
.area_center li{list-style:none;width:95px;float:left;}
.area_center li span{cursor: pointer;}
.area_right{
	width:185px;
	float:left;
	margin-left:10px;
	height:350px;
	border:1px solid #BABFC4;
	background:#FAFFF4;
}

.area_right .area_select{float:left;width:160px;height:25px;font-weight:bold;padding-top:5px;margin-left:20px;vertical-align:middle;}
.area_select span{float:left;padding-top:5px;}
.area_right .recycle{float:right;width:14px;height:17px;background:url(style/images/client/icon.gif) no-repeat 0px -664px;display:inline-block;margin-right:20px;}
.area_right ul{padding-left:15px;margin:0px;float:left;padding-top:5px;height:20px;overflow-y:auto;}
.area_right li{list-style:none;width:150px;float:left;line-height:20px;text-align: left;}
.area_right li a{width:10px;height:12px;background:url(style/images/client/icon.gif) no-repeat -14px -661px;;display:inline-block;margin-right:5px;}

#searchDiv{float: left;width: 133px;margin-left: 13px;}
#sear{float: left;width:28px;height:24px;float:left;background:url(style/images/client/search.gif)  no-repeat scroll -107px 0px ;margin-left: -1px;margin-top: 1px;}

</style>

<SCRIPT LANGUAGE="JavaScript">
	var zTree1;
	var zNodes;
	#if("$!zNodes"=="")
		zNodes = [];
	#else
		zNodes=$!zNodes;
	#end
	var setting;
		setting = {
			checkable: true,
			checkType : {"Y":"", "N":""}, 
			callback: {
				change:	zTreeOnChange,
				click:zTreeOnClick
			},
		};

	$(document).ready(function(){
		refreshTree();
		var  districtIds = parent.form.district.value;
		var  tempDistrictIds = parent.form.tempDistrictIds.value;
		districtIds = districtIds+tempDistrictIds;
		var node = "";
		var str = "";
		var infos = "";
		for(var i=0;i<districtIds.split(",").length;i++){
			if(districtIds.split(",") !=""){
				node = zTree1.getNodeByParam("id", districtIds.split(",")[i]);
				if(node != undefined){
					node.checked = true;
					zTree1.updateNode(node, false);//true标示关联父类子类,false标示不关联	
				}		
				if(districtIds.split(",")[i].length == 20){
					str += districtIds.split(",")[i] + ",";
				}
			}
		}
		jQuery.ajax({
		    type: "POST",
		    url: "/CRMopenSelectAction.do?operation=4&type=popSelect&enterPage=districtMul&popName=district&selIds="+districtIds,
		    success: function(msg){
		  	    jQuery("#showCheckedDiv").html(msg);
		    }
		});
		jQuery("#saveAreaIds").val(str);
		jQuery("#areaConts").find(":checkbox").each(function(){
			if(jQuery("#saveAreaIds").val().indexOf($(this).attr("areaId")) != -1){
				$(this).attr("checked","checked");
			}
		})
	});

	function zTreeOnChange(event, treeId, treeNode) {
		if(treeNode.checked){
			var str = '<ul id="'+treeNode.id+'" ><li><a href="#" onclick="imgdel(this);" style="cursor: pointer;"></a><span title="'+treeNode.districtFullName+'">'+treeNode.name+'</span></li></ul>'
			if(treeNode.isParent){
				var childNodes = zTree1.getNodesByParam("pId", treeNode.id, treeNode);
				//取消城市选项
				for (var i=0; i<childNodes.length; i++) {
					if(childNodes[i].checked){
						jQuery("#showCheckedDiv ul[id='"+childNodes[i].id+"']").remove();		
						childNodes[i].checked = false;
						zTree1.updateNode(childNodes[i], false);//true标示关联父类子类,false标示不关联	
					}
				}
			}else{
				jQuery("#provinceAll").removeProp("checked");//取消省份的全选按钮

				if(treeNode.pId != undefined){
					var pNode = zTree1.getNodeByParam("id", treeNode.pId);
					if(jQuery("#showCheckedDiv ul[id='"+pNode.id+"']").attr("id") != undefined){
						pNode.checked = false;
						zTree1.updateNode(pNode, false);//true标示关联父类子类,false标示不关联	
						jQuery("#showCheckedDiv ul[id='"+pNode.id+"']").remove();			
					} 
				}
			}
			//取消地区选项
			jQuery("#areaConts").find(":checkbox[areaId^='"+treeNode.id+"']:checked").each(function(){
				jQuery(this).removeProp("checked");
				jQuery("#showCheckedDiv ul[id='"+jQuery(this).attr("areaId")+"']").remove();	
			})
			
			var areaIds = jQuery("#saveAreaIds").val();
			for(var i=0;i<jQuery("#saveAreaIds").val().split(",").length;i++){
				if(jQuery("#saveAreaIds").val().split(",")[i].indexOf(treeNode.id) !=-1){
					areaIds = areaIds.replace(jQuery("#saveAreaIds").val().split(",")[i]+",","");
					jQuery("#showCheckedDiv ul[id='"+jQuery("#saveAreaIds").val().split(",")[i]+"']").remove();
				}
			}
			jQuery("#saveAreaIds").val(areaIds);
			//全选按钮




			if(jQuery("#checkAll").attr("checked") =="checked"){
				jQuery("#checkAll").removeProp("checked");
			}
			if(jQuery("#showCheckedDiv ul").length>0){
				jQuery("#showCheckedDiv ul:last").after(str)
			}else{
				jQuery("#showCheckedDiv").html(str);
			}
		}else{
			if(treeNode.isParent){
				var childNodes = zTree1.getNodesByParam("pId", treeNode.id, treeNode);
				for(var i=0;i<childNodes.length;i++){
					jQuery("#showCheckedDiv ul[id='"+childNodes[i].id+"']").remove();
				}
					jQuery("#showCheckedDiv ul[id='"+treeNode.id+"']").remove();
			}else{
				jQuery("#showCheckedDiv ul[id='"+treeNode.id+"']").remove();
				jQuery("#showCheckedDiv ul[id='"+treeNode.pId+"']").remove();
			}
		}
		//getCheckedNodesLength();
	}

	function zTreeOnClick(event, treeId, treeNode){
		if(treeNode.id.length == 15){
			jQuery("#checkAll").removeProp("checked");
			jQuery.ajax({
			   type: "POST",
			   url: "/CRMopenSelectAction.do?operation=4&type=area&cityId="+treeNode.id,
			   dataType:"json",
			   success: function(msg){
			   	var str = "";
			   	for(var i=0;i<msg.areaList.length;i++){
			   		str += '<ul><li><input type="checkbox"'
			   		if(jQuery("#saveAreaIds").val().indexOf(msg.areaList[i][1]) != -1){
						str+= ' checked="checked"';
					}
					if(msg.areaList[i][0].length>6){
						str +=' areaId="'+msg.areaList[i][1]+'" areaName="'+msg.areaList[i][0]+'" onclick="selectArea(this,\'true\');"/><span onclick="selectArea(this,\'false\');" areaId="'+msg.areaList[i][1]+'" areaName="'+msg.areaList[i][0]+'" title="'+msg.areaList[i][2]+'">'+msg.areaList[i][0].substring(0,4) +'...</span></li></ul>'
					}else{
			   			str +=' areaId="'+msg.areaList[i][1]+'" areaName="'+msg.areaList[i][0]+'" onclick="selectArea(this,\'true\');"/><span onclick="selectArea(this,\'false\');" areaId="'+msg.areaList[i][1]+'" areaName="'+msg.areaList[i][0]+'" title="'+msg.areaList[i][2]+'">'+msg.areaList[i][0]+'</span></li></ul>'
					}
			   	}
			    jQuery("#areaConts").html(str);
			   }
			});
		}
		if(treeNode.open){
			zTree1.expandNode(treeNode, false,null);
		}else{
			zTree1.expandNode(treeNode, true,null);
		}
	}


	function refreshTree() {
		zTree1 = $("#treeDemo").zTree(setting, clone(zNodes));
	}

	function selectArea(obj,isCheck){
		if(isCheck == "true"){
			if($(obj).attr("checked") == "checked"){
				var str = '<ul id="'+$(obj).attr("areaId")+'" ><li><a href="#" onclick="imgdel(this);" style="cursor: pointer;"></a><span title="'+$(obj).next().attr("title")+'">'+$(obj).attr("areaName")+'</span></li></ul>'
				if(jQuery("#showCheckedDiv").find("ul").length == 0){
					jQuery("#showCheckedDiv").html(str);
				}else{
					jQuery("#showCheckedDiv").find("ul:last").after(str);
				}
				jQuery("#saveAreaIds").val(jQuery("#saveAreaIds").val() + $(obj).attr("areaId")+",");
				jQuery("#provinceAll").removeProp("checked");//取消省份的全选按钮

			}else{
				jQuery("#showCheckedDiv ul[id='"+$(obj).attr("areaId")+"']").remove();
				jQuery("#checkAll").removeProp("checked");
				jQuery("#saveAreaIds").val(jQuery("#saveAreaIds").val().replace($(obj).attr("areaId")+",",""))
			}
		}else{
			if($(obj).prev().attr("checked") == "checked"){
				$(obj).prev().removeProp("checked");
				jQuery("#showCheckedDiv").find("ul[id='"+$(obj).attr("areaId")+"']").remove();
				jQuery("#saveAreaIds").val(jQuery("#saveAreaIds").val().replace($(obj).attr("areaId")+",",""));
			}else{
				$(obj).prev().attr("checked","checked");
				var str = '<ul id="'+$(obj).attr("areaId")+'" ><li><a href="#" onclick="imgdel(this);" style="cursor: pointer;"></a><span title="'+$(obj).attr("title")+'">'+$(obj).attr("areaName")+'</span></li></ul>'
				if(jQuery("#showCheckedDiv").find("ul").length == 0){
					jQuery("#showCheckedDiv").html(str);
				}else{
					jQuery("#showCheckedDiv").find("ul:last").after(str);
				}
				jQuery("#saveAreaIds").val(jQuery("#saveAreaIds").val() + $(obj).attr("areaId")+",");
				jQuery("#provinceAll").removeProp("checked");//取消省份的全选按钮




			}
		}
		//取消城市选项
		var node = zTree1.getNodeByParam("id", $(obj).attr("areaId").substring(0, $(obj).attr("areaId").length-5));
		if(node !=null && node.checked){
			node.checked = false;
			zTree1.updateNode(node, false);//true标示关联父类子类,false标示不关联	
			jQuery("#showCheckedDiv ul[id='"+$(obj).attr("areaId").substring(0, $(obj).attr("areaId").length-5)+"']").remove();				
		}
		
		//取消省份选项
		node = zTree1.getNodeByParam("id", $(obj).attr("areaId").substring(0, $(obj).attr("areaId").length-10));
		if(node !=null && node.checked){
			node.checked = false;
			zTree1.updateNode(node, false);//true标示关联父类子类,false标示不关联	
			jQuery("#showCheckedDiv ul[id='"+$(obj).attr("areaId").substring(0, $(obj).attr("areaId").length-10)+"']").remove();				
		}
	}
	
	//地区全选

	function checkAll(obj){
		var areaStr = "";
		if($(obj).attr("checked") == "checked"){
			jQuery("#areaConts").find(":checkbox").attr("checked","checked");
			jQuery("#areaConts ul").each(function(i){
				var areaId = $(this).find("li:first").find("input").attr("areaId");
				if(jQuery("#showCheckedDiv").find("ul[id='"+areaId+"']").attr("id") == undefined){
					areaStr += areaId +",";
					var str = '<ul id="'+areaId+'" ><li><a href="#" onclick="imgdel(this);" style="cursor: pointer;"></a><span>'+$(this).find("li:first").find("input").attr("areaName")+'</span></li></ul>'
					if(jQuery("#showCheckedDiv").find("ul").length == 0){
						jQuery("#showCheckedDiv").html(str);
					}else{
						jQuery("#showCheckedDiv").find("ul:last").after(str);
					}
				}
				if(i==0){
					//取消城市选项
					var node = zTree1.getNodeByParam("id", areaId.substring(0, areaId.length-5));
					if(node !=null && node.checked){
						node.checked = false;
						zTree1.updateNode(node, false);//true标示关联父类子类,false标示不关联	
						jQuery("#showCheckedDiv ul[id='"+areaId.substring(0, areaId.length-5)+"']").remove();				
					}
					//取消省份选项
					node = zTree1.getNodeByParam("id", areaId.substring(0, areaId.length-10));
					if(node !=null && node.checked){
						node.checked = false;
						zTree1.updateNode(node, false);//true标示关联父类子类,false标示不关联	
						jQuery("#showCheckedDiv ul[id='"+areaId.substring(0, areaId.length-10)+"']").remove();				
					}
				}
			})
			jQuery("#saveAreaIds").val(jQuery("#saveAreaIds").val()+areaStr);
			jQuery("#provinceAll").removeProp("checked");//取消省份的全选按钮




		}else{
			jQuery("#areaConts").find(":checkbox").each(function(){
				jQuery("#showCheckedDiv").find("ul[id='"+$(this).attr("areaId")+"']").remove();	
				jQuery("#saveAreaIds").val(jQuery("#saveAreaIds").val().replace($(this).attr("areaId")+",",""))
			})
			jQuery("#areaConts").find(":checkbox").removeProp("checked");
			
		}

	}
	
	function imgdel(obj){
		$(obj).parent().parent().remove();
		if(jQuery(obj).parent().parent().attr("id").length == 20){
			jQuery("#areaConts").find(":checkbox[areaId='"+jQuery(obj).parent().parent().attr("id")+"']").removeProp("checked");
			if(jQuery("#checkAll").attr("checked") == "checked"){
				jQuery("#checkAll").removeProp("checked");
			}
			jQuery("#saveAreaIds").val(jQuery("#saveAreaIds").val().replace(jQuery(obj).parent().parent().attr("id")+",",""));
		}else{
			var node = zTree1.getNodeByParam("id", jQuery(obj).parent().parent().attr("id"));
			node.checked = false;
			zTree1.updateNode(node, false);//true标示关联父类子类,false标示不关联

		}
		
	}
	
	/*返回结果*/
	function returnVal(){
		var tradeIds = "" ;
		var tradeNames = "";
		jQuery("#showCheckedDiv ul").each(function(){
			tradeIds += jQuery(this).attr("id") + ",";
			tradeNames += jQuery(this).find("span").text() + ",";
		})
		var trades = ","+tradeNames + ";" + ","+tradeIds;
		return trades ;
	}
	
	function insertkeyword(){
		var  districtIds = parent.form.district.value;
		var disIds ="";
		//var disNames = "";
		jQuery("#showCheckedDiv ul").each(function(){
			if(districtIds.indexOf(jQuery(this).attr("id")+",") == -1){
				disIds += jQuery(this).attr("id")+",";
			//	disNames += jQuery(this).find("span").text()+",";
			}
			
		})	
		parent.form.tempDistrictIds.value = disIds;
	//	parent.form.tempDistrictNames.value = disNames;
		var keyWord = jQuery("#keyWord").val();
		var selOption = jQuery("#selOption").val();
		window.location.href ="/CRMopenSelectAction.do?operation=4&isMultiple=true&selOption="+selOption+"&keyWord="+encodeURIComponent(encodeURIComponent(keyWord));
	}
	
	function provinceAll(){
		var checkNodes = zTree1.getCheckedNodes();
		var pNodes = zTree1.getNodesByParamFuzzy("isCataLog", "0");//拿到父级node
		if(jQuery("#provinceAll").attr("checked") == "checked"){
			for(var i=0;i<checkNodes.length;i++){
				checkNodes[i].checked = false;
				zTree1.updateNode(checkNodes[i], false);//true标示关联父类子类,false标示不关联

			}
			jQuery("#saveAreaIds").val("");
			var str = "";
			jQuery(":checkbox[id!=provinceAll]").removeProp("checked");
			for(var i=0;i<pNodes.length;i++){
				pNodes[i].checked = true;
				zTree1.updateNode(pNodes[i], false);//true标示关联父类子类,false标示不关联

				str += '<ul id="'+pNodes[i].id+'" ><li><a href="#" onclick="imgdel(this);" style="cursor: pointer;"></a><span>'+pNodes[i].name+'</span></li></ul>'
			}
			jQuery("#showCheckedDiv").html(str);
		}else{
			for(var i=0;i<pNodes.length;i++){
				pNodes[i].checked = false;
				zTree1.updateNode(pNodes[i], false);//true标示关联父类子类,false标示不关联

				jQuery("#showCheckedDiv ul[id='"+pNodes[i].id+"']").remove();		
			}
		}
	}
	
	function delAll(){
		if(confirm("确定清除可选数据?")){
			jQuery("#showCheckedDiv").html("");
			jQuery("#saveAreaIds").val("");
			jQuery(":checkbox").removeProp("checked");
			var checkNodes = zTree1.getCheckedNodes();
			for(var i=0;i<checkNodes.length;i++){
				checkNodes[i].checked = false;
				zTree1.updateNode(checkNodes[i], false);//true标示关联父类子类,false标示不关联

			}
		}
		
	}
  </SCRIPT>
</head>

<body>
<input type="hidden" id="saveAreaIds" name="saveAreaIds"/>
<div class="area_body">
<div class="area_left">
	<div class="search_left">
    <select id="selOption" name="selOption" onKeyDown="if(event.keyCode==13) insertkeyword();">
    	<option value="province" #if("$!selOption" == "province") selected="selected" #end>省份</option>
		<option value="city" #if("$!selOption" == "city") selected="selected" #end>城市</option>
		<option value="area" #if("$!selOption" == "area") selected="selected" #end>地区</option>
		<option value="all" #if("$!selOption" == "all") selected="selected" #end>全部</option>
    </select>
    <div  id="searchDiv">
     <input style="border: 1px solid #AEAEAE;border-right: 0;height: 20px;" type="text" class="input_search" id="keyWord" name="keyWord" class="search_text" #if("$!keyWord" !="") value="$!keyWord" #else value="关键字搜索"  #end
				onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索';" 
				onfocus="if(this.value=='关键字搜索'){this.value='';}"  /><a id="sear" href="#" onclick="insertkeyword();"></a>			
	</div>
    </div>
    <div class="area_tree">
    	#if("$!zNodes"!="[]")
    	<div class="area_all"><input type="checkbox" id="provinceAll" onclick="provinceAll();"/>省份</div>
    	#end
        <div>
        	#if("$!zNodes"=="[]")
				<span style="float: left;margin-left: 80px;margin-top: 125px;">无匹配数据</span>
			#else
				<ul id="treeDemo" class="tree" style="height: 291px;width: 205px;overflow: auto;"></ul>   
			#end
        </div>
    </div>
</div>
<div class="area_center">
	<div class="area_show"><input type="checkbox" id="checkAll" onclick="checkAll(this);"/>地区全选</div>
   
    <div  id="areaConts" style="height: 320px;overflow: auto;">
	    #if("$!areaList" != "[]")
			#foreach($area in $!areaList)
				 <ul>
			    	<li><input type="checkbox" areaId="$globals.get($area,1)" areaName="$globals.get($area,0)" onclick="selectArea(this,'true');" /><span onclick="selectArea(this,'false');" areaId="$globals.get($area,1)" areaName="$globals.get($area,0)" title="$globals.get($area,2)">$globals.subTitle("$globals.get($area,0)",14)</span></li>
			    </ul>
			#end
		#else
			#if("$!selOption" == "area" || "$!selOption" == "all")
				<span style="display: block;margin-top: 120px;">无匹配数据</span>
			#end
		
		#end
	</div>
</div>
<div class="area_right">
	<div class="area_select"><span>已选数据</span><a class="recycle" href="#" onclick="delAll();" title="清空已选数据"></a></div>
	<div id="showCheckedDiv" style="height: 320px;overflow: auto;width: 100%;">
    </div>	
</div>
</div>
</body>
</html>
