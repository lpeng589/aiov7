<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style1/css/stockgoods.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/moveChooseSetting.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript" src="/js/public_utils.js"></script>
<style type="text/css">
a{color:#000000;}
a:link{color:#000000;}
a:visited{color:#000000;}
a:hover{color:#000000;}
body{width:650px;}
.bga{background:url(/style1/images/erpimages/brother_bg.png) no-repeat 0 0;display:block;width:16px;height:16px;}
.wp_dv{display:inline-block;margin:0 0 0 20px;}
.wp_dv .slt_wdv{display:inline-block;margin-top: 10px;}
.wp_dv .bk_dv{float:left;display:inline-block;}
.wp_dv .bk_dv .left_a{background-position:-64px 0;margin:50px 0 0 0;}
.wp_dv .bk_dv .right_a{background-position:-80px 0;margin:15px 0 0 0;}
.wp_dv .bk_dv .upT_a{background-position:-16px 0;margin:30px 0 0 0;}
.wp_dv .bk_dv .up_a{background-position:-48px 0;margin:7px 0 0 0;}
.wp_dv .bk_dv .down_a{background-position:-32px 0;margin:7px 0 0 0;}
.wp_dv .bk_dv .downD_a{background-position:0 0;margin:7px 0 0 0;}
.wp_dv .bks{padding:10px 5px;}
.wp_dv .bk_dv .left_slt{border:2px #f2b5af solid;border-radius:3px;}
.wp_dv .bk_dv .right_slt{border:2px #7fcce5 solid;border-radius:3px;}
.wp_dv .slt_woy{display:inline-block;}
.wp_dv .slt_woy .woy_lt{display:inline-block;float:left;}
.wp_dv .slt_woy .woy_lt .slt{width:120px;border:2px #7fcce5 solid;border-radius:3px;}
.wp_dv .slt_woy .woy_rt{display:inline-block;float:left;padding:0 0 0 2px;}
.wp_dv .slt_woy .woy_rt .add_a{background-position:-96px 0;cursor:pointer;}
.wp_dv .slt_woy .woy_rt .del_a{background-position:-112px 0;cursor:pointer;margin:3px 0 0 0;}
.margin_rt{margin:0 0 0 80px;}
.wp_bk .bk_t i{display:inline-block;background:url(/style1/images/erpimages/column_a.gif) no-repeat left;padding:0 0 0 20px;line-height:21px;color:#000;font-size:15px;}
.div_mos{padding:5px 0;margin-left: 30px;}
.div_mos input{border: 1px solid #BBB;}

</style>
<script type="text/javascript">

	/*保存*/
	function onsubmits(){
		var moduleName = jQuery("#moduleName").val();
		if(moduleName == ""){
			asyncbox.tips('模板名称不能为空','提示',1500);
			jQuery("#moduleName").focus();
			return false;
		}
		showContent('searchField','searchFields');
		var searchField = jQuery("#searchFields").val();
		if(searchField == null || searchField == ""){
			asyncbox.tips('查询字段不能为空','提示',1500);
			jQuery("#searchField").focus();
			return false;
		}
		showContent('showField','showFields');
		var showField = jQuery("#showFields").val();
		if(showField == null || showField == ""){
			asyncbox.tips('显示字段不能为空','提示',1500);
			jQuery("#showField").focus();
			return false;
		}
		form.submit();
	}
	
	function showContent(key,param){
		var fieldvalue = "";
		jQuery("#"+key+" option").each(function(){
			fieldvalue += this.value+",";
		});
		jQuery("#"+param).attr("value",fieldvalue);
	}
	
	
</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/StockGoodsAction.do" target="formFrame">
#if("$!moduleBean.id"!="")
	<input type="hidden" name="operation" id="operation" value="2"/>
	<input type="hidden" name="id" id="id" value="$!moduleBean.id"/>
#else
	<input type="hidden" name="operation" id="operation" value="1"/>
#end
<input type="hidden" name="searchFields" id="searchFields" value="$!moduleBean.searchFields"/>
<input type="hidden" name="showFields" id="showFields" value="$!moduleBean.showFields"/>
	<div class="wp_dv">
		<div style="overflow:hidden;clear:both;padding-top: 10px;">
			<div class="btn btn-small btn_item" style="float:right;" onclick="onsubmits();">保存</div>
		</div>
		<!-----------分割线 wp_bk板块 Start------------->
		<div class="wp_bk">
			<div class="bk_t">
				<i>模板基本信息</i>
			</div>
			<div>
				<div class="div_mos">
					<ul>
						<li style="margin:0 0 10px 0;"><i>模板名称：</i><input name="moduleName" id="moduleName" value="$!moduleBean.moduleName" style="width:200px;"/><span style="color:red;padding-left: 10px;">*</span></li>
						<li style="margin:0 0 5px 0;"><i>模板描述：</i><input name="moduleDesc" id="moduleDesc" value="$!moduleBean.moduleDesc" style="width:400px;"/></li>
					</ul>
				</div>
			</div>
			<div class="bk_t">
				<i>查询/显示字段信息</i>
			</div>
			<div class="bk_c">
				<!-----------分割线 Select选择 Start------------->
				<div class="slt_wdv">
					<div style="width:15px;float:left;display:inline-block;padding:50px 5px 0 15px;">查询字段</div>
					<div class="bk_dv" >
						<ul>
							<li style="text-align: center;">可选字段</li>
							<li>
							<select class="left_slt" multiple name="left_1" id="left_1" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('left_1'), document.getElementById('searchField'))">
								#if("$!moduleBean.id"!="")
									#foreach($searchmap in $searchList)
										#set($flag = "")
										#foreach($!fieldValue in $!globals.strSplit($moduleBean.searchFields,','))
											#if("$!fieldValue" == "$searchmap.value")
												#set($flag = "true")
											#end
										#end
										#if($flag == "")
							   				<option value="$!searchmap.value">$searchmap.name</option>
							   			#end
									#end
								#else
									#foreach($searchmap in $searchList)
										#if("$!searchmap.isnull" == "0")
									   	<option value="$!searchmap.value">$!searchmap.name</option>
									   	#end
									#end
								#end
							</select>
							</li>
						</ul>
					</div>
					<div class="bk_dv bks">
						<a class="left_a bga" title="添加" onclick="moveOption(document.getElementById('left_1'),document.getElementById('searchField'))" href="javascript:void(0);"></a>
						<a class="right_a bga" title="移除" onclick="moveOption(document.getElementById('searchField'), document.getElementById('left_1'))" href="javascript:void(0);"></a>
					</div>
					<div class="bk_dv">
						<ul>
							<li style="text-align: center;">显示字段</li>
							<li>
								<select class="right_slt" multiple name="searchField" id="searchField" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('searchField'), document.getElementById('left_1'))">
								#if("$!moduleBean.id"!="")
									#foreach($!fieldValue in $!globals.strSplit($moduleBean.searchFields,','))
										#foreach($searchmap in $searchList)
											#if("$!fieldValue" == "$searchmap.value")
									   			<option value="$!searchmap.value" #if("$!searchmap.isnull"=="1") lang="must" #end>$searchmap.name</option>
											#end
										#end
									#end
								#else
									#foreach($searchmap in $searchList)
										#if("$!searchmap.isnull" == "1")
									   	<option value="$!searchmap.value" lang="must">$!searchmap.name</option>
									   	#end
									#end
								#end
								</select>
							</li>
						</ul>
					</div>
					<div class="bk_dv bks">
						<a class="upT_a bga" title="置顶" onclick="moveTop(document.getElementById('searchField'));" href="javascript:void(0);"></a>
						<a class="up_a bga" title="上移" onclick="moveUp(document.getElementById('searchField'));" href="javascript:void(0);"></a>
						<a class="down_a bga" title="下移" onclick="moveDown(document.getElementById('searchField'));" href="javascript:void(0);"></a>
						<a class="downD_a bga" title="置底" onclick="moveBottom(document.getElementById('searchField'));" href="javascript:void(0);"></a>
					</div>
				</div>
				<!-----------分割线 Select选择 End------------->
				<!-----------分割线 Select选择 Start------------->
				<div class="slt_wdv" style="margin:10px 0 0 30px;">
					<div style="width:15px;float:left;display:inline-block;padding:50px 5px 0 15px;">显示字段</div>
					<div class="bk_dv">
						<ul>
							<li style="text-align: center;">可选字段</li>
							<li>
								<select class="left_slt" multiple name="left_2" id="left_2" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('left_2'), document.getElementById('showField'))">
								#if("$!moduleBean.id"!="")
									#foreach($showmap in $showList)
										#set($flag = "")
										#foreach($!fieldValue in $!globals.strSplit($moduleBean.showFields,','))
											#if("$!fieldValue" == "$showmap.value")
												#set($flag = "true")
											#end
										#end
										#if($flag == "")
							   				<option value="$!showmap.value">$showmap.name</option>
							   			#end
									#end
								#else
								    #foreach($showmap in $showList)
								    	#if("$!showmap.isnull" == "0")
									   	<option value="$!showmap.value">$!showmap.name</option>
									   	#end
									#end 
								#end
								</select>
							</li>
						</ul>
					</div>
					<div class="bk_dv bks">
						<a class="left_a bga" title="添加" onclick="moveOption(document.getElementById('left_2'),document.getElementById('showField'))" href="javascript:void(0);"></a>
						<a class="right_a bga" title="移除" onclick="moveOption(document.getElementById('showField'), document.getElementById('left_2'))" href="javascript:void(0);"></a>
					</div>
					<div class="bk_dv">
						<ul>
							<li style="text-align: center;">显示字段</li>
							<li>
								<select class="right_slt" multiple name="showField" id="showField" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('showField'), document.getElementById('left_2'))">
								#if("$!moduleBean.id"!="")
									#foreach($!fieldValue in $!globals.strSplit($moduleBean.showFields,','))
										#foreach($showmap in $showList)
											#if("$!fieldValue" == "$showmap.value")
									   			<option value="$!showmap.value" #if("$!showmap.isnull"=="1") lang="must" #end>$!showmap.name</option>
											#end
										#end
									#end
								#else
									#foreach($showmap in $showList)
										#if("$!showmap.isnull" == "1")
									   	<option value="$!showmap.value" lang="must">$!showmap.name</option>
									   	#end
									#end
								#end
								</select>
							</li>
						</ul>
					</div>
					<div class="bk_dv bks">
						<a class="upT_a bga" title="置顶" onclick="moveTop(document.getElementById('showField'));" href="javascript:void(0);"></a>
						<a class="up_a bga" title="上移" onclick="moveUp(document.getElementById('showField'));" href="javascript:void(0);"></a>
						<a class="down_a bga" title="下移" onclick="moveDown(document.getElementById('showField'));" href="javascript:void(0);"></a>
						<a class="downD_a bga" title="置底" onclick="moveBottom(document.getElementById('showField'));" href="javascript:void(0);"></a>
					</div>
				</div>
				<!-----------分割线 Select选择 End------------->
			</div>
		</div>
		<!-----------分割线 wp_bk板块 End------------->
	</div>
	<script type="text/javascript"> 
	<!--
	//上移
　	function moveUp(obj)
　　{　
　　　　　　for(var i=1; i < obj.length; i++)
　　　　　　{//最上面的一个不需要移动，所以直接从i=1开始
　　　　　　　　if(obj.options[i].selected)
　　　　　　　　{
　　　　　　　　　　if(!obj.options.item(i-1).selected)
　　　　　　　　　　{
　　　　　　　　　　　　var selText = obj.options[i].text;
　　　　　　　　　　　　var selValue = obj.options[i].value;
						obj.options[i].text = obj.options[i-1].text;
						obj.options[i].value = obj.options[i-1].value;
						obj.options[i].selected = false;
						obj.options[i-1].text = selText;
						obj.options[i-1].value = selValue;
						obj.options[i-1].selected=true;
　　　　　　　　　　}
　　　　　　　　}
　　　　　　}
　　　}
		//下移
		function moveDown(obj)
　　　　{
　　　　　　for(var i = obj.length -2 ; i >= 0; i--)
　　　　　　{//向下移动，最后一个不需要处理，所以直接从倒数第二个开始
　　　　　　　　if(obj.options[i].selected)
　　　　　　　　{
　　　　　　　　　　if(!obj.options[i+1].selected)
　　　　　　　　　　{
　　　　　　　　　　　　var selText = obj.options[i].text;
　　　　　　　　　　　　var selValue = obj.options[i].value;
						obj.options[i].text = obj.options[i+1].text;
						obj.options[i].value = obj.options[i+1].value;
						obj.options[i].selected = false;
						obj.options[i+1].text = selText;
						obj.options[i+1].value = selValue;
						obj.options[i+1].selected=true;
　　　　　　　　　　}
　　　　　　　　}
　　　　　　}
　　　　}
		//移动
		function moveOption(obj1, obj2)
		{
			 for(var i = obj1.options.length - 1 ; i >= 0 ; i--)
			 {
				 if(obj1.options[i].selected)
				 {
				 	if(obj1.options[i].lang == "must"){
				 		alert(obj1.options[i].text+"是必须显示字段，无法移动！");
				 		return false;
				 	}
					var opt = new Option(obj1.options[i].text,obj1.options[i].value);
					opt.selected = true;
					obj2.options.add(opt);
					obj1.remove(i);
				}
			 }
		}
		//置顶
	  function  moveTop(obj) 
	  { 
			var  opts = []; 
			for(var i =obj.options.length -1 ; i >= 0; i--)
			{
				if(obj.options[i].selected)
				{
					opts.push(obj.options[i]);
					obj.remove(i);
				}
			}
			var index = 0 ;
			for(var t = opts.length-1 ; t>=0 ; t--)
			{
				var opt = new Option(opts[t].text,opts[t].value);
				opt.selected = true;
				obj.options.add(opt, index++);
			}
		} 
	  //置底
	  function  moveBottom(obj) 
	  { 
			var  opts = []; 
			for(var i =obj.options.length -1 ; i >= 0; i--)
			{
				if(obj.options[i].selected)
				{
					opts.push(obj.options[i]);
					obj.remove(i);
				}
			}
			 for(var t = opts.length-1 ; t>=0 ; t--)
			{
				var opt = new Option(opts[t].text,opts[t].value);
				opt.selected = true;
				obj.options.add(opt);
			}
		} 
	//-->
</script>
</form>
</body>
</html>
