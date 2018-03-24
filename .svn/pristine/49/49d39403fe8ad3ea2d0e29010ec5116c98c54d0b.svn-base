<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="/style1/css/tab.css" type="text/css" />
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript">

//保存
function saves(){
	var check_name = "";
	var f = false;
	$(".alertTypes").each(function(){
		var flag = false;
		var statuss = $(this).parent().parent().find("input[vs=statuss]").is(":checked");
		if(statuss==true){
			$(this).find("input[type='checkbox']").each(function(){
				check_name = $(this).attr("vm");
				if($(this).attr("checked")=="checked"){
					flag = true;
					return false;
				}
			});
		}else{
			flag = true;
		}
		if(!flag){
			f = true;
			return false;
		}
	});
	if(f){
		alert("请选择"+check_name+"提醒方式");
		return false;
	}
	var inputObj = null;
	$(".con_input").each(function(){
		var flag = false;
		$(this).find("input:visible").each(function(){
			if($(this).val()!='' && !gtZero($(this).val())){
				inputObj = $(this);
				flag = true;
			}
		});
		if(flag){
			f = true;
			return false;
		}
	});
	if(f){
		alert("输入必须为整数");
		$(inputObj).focus();
		return false;
	}
	var flag = false;
	$("input[class=ck_box]").each(function(){
		var ischeck = $(this).is(":checked")==true?"0":"1";
		if(ischeck != $(this).attr("ov")){
			flag = true;
		}
	});
	$("select[class=se_dx]").each(function(){
		if($(this).val() != $(this).attr("ov")){
			flag = true;
		}
	});
	$(".alertTypes").each(function(){
		$(this).find("input[type='checkbox']").each(function(){
			var ischeck = $(this).is(":checked")==true?$(this).val():undefined;
			if(ischeck != $(this).attr("ov")){
				flag = true;
			}
		});
	});
	$("input[class=in_txt]").each(function(){
		if($(this).val() != $(this).attr("ov")){
			flag = true;
		}
	});
	$("input[class=user_input]").each(function(){
		if($(this).val() != $(this).attr("ov")){
			flag = true;
		}
	});
	if(!flag){
		alert("您未修改任何预警项目");
		return false;
	}
	form.submit();
}

function showalert(alertCode){
	if($("#"+alertCode+"_user_div").is(":hidden")){
		$("#"+alertCode+"_user_div").show();
	}else{
		$("#"+alertCode+"_user_div").hide();
	}
}

var fieldNames;
var fieldNIds;
/* 选择提示用户 */
function deptPopUser(fieldName,fieldNameIds){
	var users = jQuery("#popedomUserIds").val();
	var urls = "/Accredit.do?popname=userGroup&inputType=checkbox&chooseData="+users ;
	fieldNames=fieldName;
	fieldNIds=fieldNameIds;
	asyncbox.open({id:'Popdiv',title:'请选择提醒对象',url:urls,width:750,height:430,
		 btnsbar : jQuery.btn.OKCANCEL,
		 callback : function(action,opener){
　　　　　	if(action == 'ok'){
				var employees = opener.strData;
				newOpenSelect(employees,fieldName,fieldNameIds);
　　　　　	}
　　　	}
　	});
}

function newOpenSelect(employees,fieldName,fieldNameIds){
	var employees = employees.split("|") ;
	for(var j=0;j<employees.length;j++){
		var field = employees[j].split(";") ;
		if(field!=""){
			var existOption = getValueById(fieldName).options;
			var length = existOption.length;
			var talg = false ;
			for(var i=0;i<length;i++){
				if(existOption[i].value==field[0]){
					talg = true;
				}
			}
			if(!talg){
				getValueById(fieldName).options.add(new Option(field[1],field[0]));
				jQuery("#"+fieldNameIds).val(jQuery("#"+fieldNameIds).val()+field[0]+",");
			}
		}
	}
}

function getValueById(value){
	return document.getElementById(value);
}

//弹出框双击回填数据
function fillData(datas){
	newOpenSelect(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv');
}

/*下拉列表的批量删除操作*/
function deleteOpation(fileName,popedomId){
	var index = jQuery("#"+fileName+" option:selected").size();
	if(index==0){
		alert("请选择要移除的项!");
		return false;
	}
	jQuery("#"+fileName+" option:selected").remove();
	/*获取指定下拉列表的值，并为指定的参数赋值*/
	var showContent="";
	jQuery("#"+fileName+" option").each(function(){
	   showContent+= this.value+",";
	});
	jQuery("#"+popedomId).attr("value",showContent);
}

function queryAlert(){
	asyncbox.open({
　　　html : '<input style="margin:10px;width:260px" name="search" id="search" onkeydown="if(event.keyCode==13){queryCallBack();}"/>',
　　　width : 300,
　　　height : 130,
	 right: 15,
	 top: 30,
	 title : '搜索',
	 btnsbar: [{
     text    : '下一个',                  
      action  : 'nextq'             
     }].concat(jQuery.btn.CANCEL), 
　　　callback : function(action){
		if(action == 'nextq'){
			queryCallBack();
　　　　　　　return false;
　　　　　}
		
　　　}
		
　});
  $("#search").focus();
}

var curCondition = "";
var curPos = -1;
var curCount = 0;
function queryCallBack(){ 
	if($("#search").val()==''){
		$("#search").focus();
		return;
	}
	if($("#search").val() != curCondition){
		//如果查询条件不一致了，要更新
		curCondition = $("#search").val();
		curPos = -1;
		curCount = $(".lt_lb").size();
	}
	$(".conSelected").removeClass("conSelected");
	$(".curconSelected").removeClass("curconSelected");
	curexec = false; //一次点击只能执行一次查询

	$(".lt_lb").each(function(i) {
		if($(this).html().indexOf($("#search").val())>-1){ 
			$(this).addClass("conSelected");
			if(i>=curCount){
				curPos  =  -1;
			}
			if(i>curPos && !curexec){
				curexec = true;
				curPos = i;
				$(this).addClass("curconSelected");
				var y,fcount;  
				y=$(this)[0].getBoundingClientRect().top ; 
				fcount = 0;
				if(y<120){
					while(y<120 && fcount<200 && $("#tagContent")[0].scrollTop > 0 ){				
						fcount ++;
						$("#tagContent")[0].scrollTop = $("#tagContent")[0].scrollTop - 20; 
						y=$(this)[0].getBoundingClientRect().top ; 
					}
				}else if(y>parent.document.body.clientHeight -160){			
					while(y>parent.document.body.clientHeight -160 && fcount<200 ){				
						fcount ++;
						$("#tagContent")[0].scrollTop = $("#tagContent")[0].scrollTop + 20; 
						y=$(this)[0].getBoundingClientRect().top ; 
					}
				}
			}
		};
	});
}

function refresh(){
	window.location.reload();
}

function showbewrite(obj){
	if($(obj).attr("checked")=="checked"){
		$(obj).parent().find(".con_input").show();
		$(obj).parent().find(".span_dx").show();
	}else{
		$(obj).parent().find(".con_input").hide();
		$(obj).parent().find(".span_dx").hide();
	}
}
</script>
<style type="text/css">
/*主要控制参数*/
.title_font{font-size:16px;font-weight:bold;padding:10px 0 0 20px;display:block;}
.wp_ul {overflow:hidden;min-width:900px;width:100%;}
.wp_ul {margin:2px 0 0 0;padding:8px 0;}
.wp_ul .lt_lb{float:left;white-space: normal;min-width:200px;width:25%;line-height:25px;display:inline-block;text-align: right;margin:0px;height:60px; }
.wp_ul .rt_sp{float:left;min-width:550px;width:70%;padding-left:8px;text-align:left;min-height:60px; overflow:hidden;}
.wp_ul .rt_sp>div{border: 1px solid #b4b4b4;width:50%;padding:5px;margin:10px 0 0 0;}
.wp_ul .rt_sp .con_input{margin-left:20px;}
.wp_ul .rt_sp .con_input input{width: 30px;}
.wp_ul .rt_sp .ck_box{margin:6px 0 0 0;}
.wp_ul .rt_sp .txt_sp{display:block;margin:5px 0 0 0;color:#a1a1a1;}

.hr_link{height:1px;border-top:1px #ebebeb solid;}
.conSelected{color:#FF00CC; }
.curconSelected{ color:red;}

/*用于jquery选择*/
.conDiv{}
.txt_jx{padding-left: 30px;}
.ts_div{overflow: hidden;}
.del_div{padding: 20px 0 0 0}
.del_div img{padding-left: 10px;}
.ts_div select{width:120px;}
.div_name{float: left;}
</style>
</head>

<body onLoad="showtable('tblSort');showStatus();">

<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" name="form" action="/AlertSetAction.do" target="formFrame">
<input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')" />
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/> 
<input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
<input type="hidden" name="optype" id="optype" value="save"/>
<div class="Heading" id="head">
	<div class="HeadingIcon" ><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">预警设置</div>
	<div class="HeadingButton" id="theButton" style="display:block; float: right;">		
		<button type="button" style="margin-right: 3px;" onClick="queryAlert()" class="b2">$text.get("common.lb.query")</button>
		<button type="button" style="margin-right: 3px;" onClick="refresh()" class="b2">$text.get("oa.common.refresh")</button>
		<button type="button" style="margin-right: 50px;" onClick="saves()" class="b2">$text.get("common.lb.ok")</button>
	</div>
</div>
	<div id="con" style="min-width:960px;width:90%;margin:0 auto;">
		<UL id="tags" >
			 <LI id="tag_0"   >
		</UL>
		<div id="tagContent" style="width:99%;overflow:auto;padding:10px 0 0 0;">				
			<div class="conDiv">
		 	#foreach($group in $!groupList)
		 	#if($globals.get($group,2).size()>0)
		 	<font class="title_font">$globals.get($group,1)</font>
		 	<hr style="border:1px solid #00CCFF"/>
		 		#foreach($row in $globals.get($group,2))
		 			<input id="sysAlert" name="sysAlert" value="$globals.get($row,0)" type="hidden"/>
		 			<div class="wp_ul">
		 				<label class="lt_lb">$globals.get($row,1)：</label>
			 			<span class="rt_sp">
				 			<input class="ck_box" vs="statuss" id="$globals.get($row,0)_status" name="$globals.get($row,0)_status" type="checkbox" #if($globals.get($row,2)=="0") ov="0" checked #else ov="$globals.get($row,2)" #end value="0" onclick="showbewrite(this)"/>
				 			#if("$globals.get($row,14)"=="1")
				 				<input class="in_txt" id="$globals.get($row,0)_condition" name="$globals.get($row,0)_condition" value="$!globals.get($row,5)" ov="$!globals.get($row,5)" type="hidden"/>
				 			#else
				 				<span class="con_input" #if($globals.get($row,2)!="0") style="display: none;" #end>$!globals.get($!globals.strSplit($!globals.get($row,13),'input'),0)<input class="in_txt" id="$globals.get($row,0)_condition" name="$globals.get($row,0)_condition" value="$!globals.get($!globals.stringToArray($!globals.get($row,5),'='),1)" ov="$!globals.get($!globals.stringToArray($!globals.get($row,5),'='),1)"/>$!globals.get($!globals.strSplit($!globals.get($row,13),'input'),1)</span>
				 			#end
				 			<span class="txt_sp">
				 				#if($globals.get($row,11)!="")
				 					($globals.encodeHTML($globals.get($row,11)))
				 				#end
				 			</span>
		 					<span class="span_dx" #if($globals.get($row,2)!="0") style="display: none;" #end><span class="alertTypes">
		 					#foreach($row_wakeUpType in $globals.getEnumerationItems("WakeUpMode"))
					 			<input type="checkbox" id="$globals.get($row,0)_alertType" name="$globals.get($row,0)_alertType" #foreach($t in $globals.strSplit($globals.get($row,8),',')) #if($row_wakeUpType.value==$t) checked="checked" ov="$t" #end #end value="$row_wakeUpType.value" vm="$globals.get($row,1)"/>$row_wakeUpType.name
							#end</span>
							<span class="txt_jx">
							<select class="se_dx" name="$globals.get($row,0)_actionTime" id="$globals.get($row,0)_actionTime" ov="$globals.get($row,6)">
							#foreach($foo in [0..23])
								#if($foo<10)
									<option value="0$foo" #if("$globals.get($row,6)"=="0$foo") selected #end>0$foo</option>
								#else
									<option value="$foo" #if("$globals.get($row,6)"=="$foo") selected #end>$foo</option>
								#end
							#end
							</select>点</span>
							<span class="txt_jx">
							每天
							<select class="se_dx" name="$globals.get($row,0)_actionFrequency" id="$globals.get($row,0)_actionFrequency" ov="$globals.get($row,7)">
								<option value="1" #if("$globals.get($row,7)"=="1") selected #end>1</option>
								<option value="2" #if("$globals.get($row,7)"=="2") selected #end>2</option>
								<option value="3" #if("$globals.get($row,7)"=="3") selected #end>3</option>
								<option value="4" #if("$globals.get($row,7)"=="4") selected #end>4</option>
								<option value="5" #if("$globals.get($row,7)"=="5") selected #end>5</option>
							</select>次</span>
							<span class="txt_jx"><a href="javascript:void(0)" onclick="showalert('$globals.get($row,0)')" title="提醒对象设置">提醒对象</a></span>
								<input class="user_input" type="hidden" name="$globals.get($row,0)_popedomUserIds" id="$globals.get($row,0)_popedomUserIds" value="$!globals.get($row,15)" ov="$!globals.get($row,15)"/>
					 			<div id="$globals.get($row,0)_user_div" style="display: none;">
									<div class="div_name" id="div_title"></div>
									<div class="ts_div" >
										<div id="_context">
											<div style="float:left;display:inline-block;">
												<div>
													<img src="$globals.getStylePath()/images/St.gif" onclick="deptPopUser('$globals.get($row,0)_formatUserName','$globals.get($row,0)_popedomUserIds');" alt="$text.get("oa.common.advicePerson")" class="search" style="cursor: pointer;"/>
													<a href="javascript:void(0)" title="选择用户" onclick="deptPopUser('$globals.get($row,0)_formatUserName','$globals.get($row,0)_popedomUserIds');">选择用户</a>
												</div>
												<select name="$globals.get($row,0)_formatUserName" id="$globals.get($row,0)_formatUserName" multiple="multiple">
													#foreach($!det in $globals.strSplit($globals.get($row,15),','))
														<option value="$!det">$!globals.getEmpFullNameByUserId($!det)</option>
													#end
												</select>
											</div>
											<div class="del_div" style="float:left;display:inline-block;">
												<img onclick="deleteOpation('$globals.get($row,0)_formatUserName','$globals.get($row,0)_popedomUserIds');" src="/$globals.getStylePath()/images/delete_button.gif" alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")" class="search deloption" />
											</div>
										</div>
									</div>
								</div>
				 			</span>
				 			</span>
		 			</div>
		 			<div class="hr_link"></div>
		 		#end
		 		#end
		 	#end
		 	</div>
		 	#if("$!noData"=="true")
				<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">
					暂无预警设置记录
				</div>
			#end
		<script type="text/javascript">
			$("#tagContent").height($(window).height()-70);
			var sHeight = $(window).height()-70;	
		</script>
		
	</div>
</form>
</body>
</html>