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
	width:580px;
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
.area_center ul{padding-left:15px;margin:0px;float:left;padding-top:5px;text-align: left;margin-top: 10px;}
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
			checkStyle: "radio",
			checkRadioType: "all",
			callback: {
				change:	zTreeOnChange,
				click:zTreeOnClick
			},
		};

	$(document).ready(function(){
		refreshTree();

	});

	function zTreeOnChange(event, treeId, treeNode) {
		if(treeNode.checked){
			jQuery("#districtId").val(treeNode.id);
			jQuery("#districtName").val(treeNode.districtFullName);
		}else{
			jQuery("#districtId").val("");
			jQuery("#districtName").val("");
		}
		
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
			   		str += '<ul><li>'
					if(msg.areaList[i][0].length>6){
						msg.areaList[i][0] = msg.areaList[i][0].substring(0,4) +"..."
					}
			   		str +='<span onclick="selectArea(this);" areaId="'+msg.areaList[i][1]+'" areaName="'+msg.areaList[i][2]+'">'+msg.areaList[i][0]+'</span></li></ul>'
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

	function selectArea(obj){
		var districtInfo = $(obj).attr("areaName") + "," + $(obj).attr("areaId")
		if(parent.jQuery.exist('dealdiv')){
		     parent.jQuery.opener('dealdiv').dealDistrict(districtInfo);
		}else{
		     parent.dealDistrict(districtInfo)
		}
		
		parent.jQuery.close('crmOpenId');
	}

	/*返回结果*/
	function returnVal(){
		var trades = jQuery("#districtName").val() + "," + jQuery("#districtId").val()
		return trades ;
	}
	
	function insertkeyword(){
		var keyWord = jQuery("#keyWord").val();
		var selOption = jQuery("#selOption").val();
		window.location.href ="/CRMopenSelectAction.do?operation=4&selOption="+selOption+"&keyWord="+encodeURIComponent(encodeURIComponent(keyWord));
	}
	
  </SCRIPT>
</head>

<body>
<input type="hidden" id="districtId" name="districtId"/>
<input type="hidden" id="districtName" name="districtName"/>
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
        <div>
        	#if("$!zNodes"=="[]")
				<span style="float: left;margin-left: 80px;margin-top: 125px;">暂无数据</span>
			#else
				<ul id="treeDemo" class="tree" style="height: 315px;width: 208px;overflow: auto;"></ul>   
			#end
        </div>
    </div>
</div>
<div class="area_center">
    <div  id="areaConts" style="height: 320px;overflow: auto;">
	    #if("$!areaList" != "[]")
			#foreach($area in $!areaList)
				 <ul>
			    	<li><span onclick="selectArea(this);" areaId="$globals.get($area,1)" areaName="$globals.get($area,2)"  title="$globals.get($area,2)">$globals.subTitle("$globals.get($area,0)",14)</span></li>
			    </ul>
			#end
		#else
			#if("$!selOption" == "area" || "$!selOption" == "all")
				<span style="display: block;margin-top: 120px;">暂无数据</span>
			#end
		
		#end
	</div>
</div>
</div>
</body>
</html>
