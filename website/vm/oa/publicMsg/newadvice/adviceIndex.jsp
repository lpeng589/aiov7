<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>$text.get("oa.mydesk.notice")</title>		
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>		
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link rel="stylesheet" type="text/css" href="/style/css/easyui.css" />
<link rel="stylesheet" type="text/css" href="/style/css/icon.css" />
<link href="/js/skins/ZCMS/asyncbox.css" type="text/css" rel="stylesheet" />
<script language="javascript" src="/js/jquery.js"></script>	
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/jquery.easyui.min.js"></script>	
<script language="javascript" src="/js/public_utils.js"></script>

<script language="javascript">
var x="$!adviceSearchForm.selectId";
var y="$!adviceSearchForm.selectType";
var n="$!thetype";
if(y=="type"){
	self.parent.frames["leftFrame"].insertType(x);	
}

function subForm(){
	myform.submit();
	closeSearch();
}

function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function handle(name,type){
	var items = document.getElementsByName(name);
	var mydatasIds="";
	for(var i=0;i<items.length;i++){
		if(type == 1){
			if(items[i].checked){
				mydatasIds+=items[i].value;
				update(items[i].value);
				return true;
			}
		}else if(type == 2){
			if(items[i].checked){
				var value = items[i].value;
				mydatasIds+=value+",";
			}
		}
	}
	if(mydatasIds!="" && mydatasIds.length!=0){
			mydatasIds = mydatasIds.substr(0,mydatasIds.length-1);
			asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
　　　				if(action == 'ok'){
　　　　　				document.location = "/OAnewAdvice.do?operation=3&type=advice&adviceId="+mydatasIds;		
　			}});	
	}else{
		asyncbox.alert('$text.get('common.msg.mustSelectOne')','$!text.get("clueTo")');
	}
}

function isDelete(m){
	asyncbox.confirm('$text.get("oa.common.sureDelete")','$!text.get("clueTo")',function(action){
	　　 if(action == 'ok'){
			document.location = "/OAnewAdvice.do?operation=$globals.getOP("OP_DELETE")&type=advice&adviceId="+m;
		}
	});
}

function dingWei(){
	var adviceId="$!selectAdvice";
	if(adviceId!= ''){
		window.location.href="#"+adviceId;
	}
}
function fillData(datas){
	newOpenSelectSearch(datas,fieldNames,fieldNIds);
	jQuery.close('Popdiv');
}
</script>
	</head>
	<body onLoad="showStatus();dingWei();">
		<form method="post" name="form" id="form" action="/OAnewAdvice.do?type=news">
				<input type="hidden" name="dbData" id="dbData" value="" onpropertyChange="javascript:if(this.value!=null)evaluate()"/>
			<input type="hidden" name="operation" value="4" />
			<table width="100%" border="0" cellpadding="0" cellspacing="0" >		
					<tr>
					<td colspan="2" class="bigTitle" style="background-image: none">&nbsp;</td>
					</tr>	
					<table width="100%" border="0" cellpadding="0" cellspacing="0" height="84%">			
					<tr>
						<td class="F_right" >
							<div class="right_title">
							 &nbsp;&nbsp;
							#if(!($query && !$add && !$delete && !$update))
							 <input type="checkbox" value="checkbox" name="selAll" onClick="checkAll('adviceId')"/>$text.get("common.lb.selectAll")&nbsp;						
							#end
							#if($add)
								<a href="/OAnewAdvice.do?operation=$globals.getOP("OP_ADD_PREPARE")">
								<img src="style1/images/oaimages/add.bmp"  style="margin-top: 5px;"/><font style="margin-left: 2px;">$text.get("common.lb.add")</font></a>
							#end&nbsp;	
							#if($delete)
								<a href="javascript:handle('adviceId',2)">
								<img  src="style1/images/oaimages/l-i-03.gif" style="margin-top: 5px;"/><font style="margin-left: 2px;">$text.get("oa.common.del")</font></a>	
							#end		
							
							</div>
							<ul id="nn" style="overflow:hidden;overflow-y:auto;width:100%;padding:0;margin:0;">
<script type="text/javascript">
	var oDiv=document.getElementById("nn");
	var sHeight=document.documentElement.clientHeight-120;
	oDiv.style.height=sHeight+"px";
</script>
								#foreach($advice in $adviceList)
										<li #if($globals.isOddNumber($velocityCount)) class="c1" #else class="c2" #end>
											<div class="newsTitle">
											<a name="$!advice.id"></a>
												#if(!($query && !$add && !$delete && !$update))
												<input type="checkbox" name="adviceId" value="$!advice.id"/>
												#end
												<a href="javascript:void(0);" onclick="javascript:window.location.href='/OAnewAdvice.do?operation=$globals.getOP("OP_DETAIL")&adviceId=$!advice.id'">
												$globals.subTitle($globals.replaceHTML("$!advice.adviceTitle"),150)
												</a>
												</div>
											<div class="newsCon" >
												<P>
													<a href="javascript:void(0);" onclick="javascript:window.location.href='/OAnewAdvice.do?operation=$globals.getOP("OP_DETAIL")&adviceId=$!advice.id'">	
														#if($globals.subTitle($globals.replaceHTML("$!advice.adviceContext"),320)==" ")
															<img src="style1/images/234.png" style="cursor: pointer;"/>
														#else
															$globals.subTitle($globals.replaceHTML("$!advice.adviceContext"),320)
														#end	
													</a>
												</P>
											</div>
											<div class="newsIon">
												<span style="float: left;margin-left: 30px;">
												#if($!advice.filePath != "")
													<img  src="style1/images/78.bmp" alt="$text.get("upload.lb.affix")" title="内容包含附件"/>
												#end
											 	#if($!advice.adviceContext.indexOf("<img") > -1)
													<img  src="style1/images/234.png" alt="内容包含图片" title="内容包含图片"/>
												#end
												</span>
										
												<span>
												#if($!advice.statusId !=1)
													
													$globals.substring($!advice.pulishDate,10)
												#else
													<img src="style1/images/oaimages/caogao.png" alt="草稿" title="草稿" style="margin-right: 10px;margin-bottom: -4px;"/>
													$globals.substring($!advice.createTime,10)
												#end
												</span>
												<a href="javascript:void(0);" onclick="javascript:window.location.href='/OAnewAdvice.do?operation=$globals.getOP("OP_DETAIL")&adviceId=$!advice.id'"><span class="NI_1">$text.get("common.lb.detail")</span></a>
												#if($update)
												<a href="javascript:void(0);" onclick="javascript:window.location.href='/OAnewAdviceAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&adviceId=$!advice.id'" ><span class="NI_2">$text.get("oa.common.upd")</span></a>		
												#end
												#if($delete)										
													<a href="javascript:isDelete('$advice.id');"><span class="NI_3">$text.get("oa.common.del")</span></a>
												#end			
											</div>
										</li>					
								#end
								#if($adviceList.size()==0)
								<div class="nodata">未找到与您查询条件相匹配的信息</div>		
						   	    #end	
							</ul>
					</td>
				</tr>
				</table>
			</table>
	
			<div class="listRange_pagebar">			   
				$!pageBar
			</div>
			</form>
    <div id="w" class="search_popup" style="display:none;height: 190px; top: 150px;left: 250px;">
		<div id="Divtitle" style="cursor: move;">
			<span class="ico_4"></span>$text.get("com.query.conditions")
		</div>
		<form method="post" id="myform" name="myform" action="/OAnewAdvice.do?operation=4&selectType=gaoji">
			<table style="margin-top: 10px;">			
				<tr>
					<td align="right"><span style="margin-left: 15px;">$text.get("oa.common.adviceTitle")：</span></td>
					<td ><input size="13" id="adviceTitle" name="adviceTitle" type="text" value="$!adviceSearchForm.adviceTitle" onKeyDown="if(event.keyCode==13) subForm();"/></td>
					<td>$text.get("oa.common.publisher")：	</td>		
					<td>
						<input name="createBy" id="createBy"  type="hidden" value="$!adviceSearchForm.createBy" />
						<input name="proUserName" id="proUserName" size="13" onKeyDown="if(event.keyCode==13) subForm();" type="text" readonly="readonly"  onClick="deptPopForAccount('userGroup','proUserName','createBy');" value="$!adviceSearchForm.proUserName" />
						<img style="cursor:pointer;" src="/$globals.getStylePath()/images/St.gif" onClick="deptPopForAccount('userGroup','proUserName','createBy');" />		
					</td>
				</tr>
				<tr>
					<td align="right">$text.get("oa.common.publishTime")：</td>
					<td >
						<input name="beginTime"  id="beginTime" size="13" value="$!adviceSearchForm.beginTime" onKeyDown="if(event.keyCode==13) subForm();" onClick="openInputDate(this);" />
				    </td>
					<td align="center"> 至	</td>
					<td>					
						<input name="endTime"  id="endTime" size="13" value="$!adviceSearchForm.endTime" onKeyDown="if(event.keyCode==13) subForm();" onClick="openInputDate(this);" />					
					</td>
				</tr>
				<tr>
					<td align="right">$text.get("oa.common.adviceType")：</td>
					<td>
						<select name="adviceType" id="select" value="$!adviceSearchForm.adviceType" style="width:110px" onKeyDown="if(event.keyCode==13) subForm();">
							<option value="" selected="selected"  ></option>
							#foreach($row_adviceType in $globals.getEnumerationItems("AdviceType"))
								<option  value="$row_adviceType.value" 
									#if($!adviceSearchForm.adviceType==$row_adviceType.value) selected #end >
									$row_adviceType.name 
								</option>
							#end
						</select>
					</td>
					<td></td>
					<td></td>
					
				</tr>
			</table>
			<span class="search_popup_bu"><input type="button" onClick="subForm();" class="bu_1" /><input type="button" onclick="closeSearch();" class="bu_2" /></span>
			</form>
			<script language="javascript">
			
			var posX;
var posY;       
	fdiv = document.getElementById("w");         
	document.getElementById("Divtitle").onmousedown=function(e){
	    if(!e) e = window.event;  //IE
	    posX = e.clientX - parseInt(fdiv.style.left);
	    posY = e.clientY - parseInt(fdiv.style.top);
	    document.onmousemove = mousemove;           
	}

document.onmouseup = function(){
    document.onmousemove = null;
}
function mousemove(ev){
    if(ev==null) ev = window.event;//IE
    fdiv.style.left = (ev.clientX - posX) + "px";
    fdiv.style.top = (ev.clientY - posY) + "px";
}
			
				var y="$!adviceSearchForm.selectType";
				if(y == "returnindex"){
					 $('#w').show();
				 }
			
			function showSearch(){
				$("#createBy").val("");
				$("#proUserName").val("");
				if($('#w').css("display")== "none")
				 $('#w').show();
				else
				 $('#w').hide();
			}
			
			function closeSearch(){
				$('#w').hide();
			}
			
			</script>
	</div>
</html>


