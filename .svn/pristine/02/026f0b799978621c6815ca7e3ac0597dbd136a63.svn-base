<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>group</title>
<link rel="stylesheet" type="text/css" href="/$globals.getStylePath()/css/ListRange.css"  />
<link rel="stylesheet" type="text/css" href="/style1/css/oa_news.css" />
<link rel="stylesheet" type="text/css" href="/style1/css/sharingStyle.css" />
<link rel="stylesheet" type="text/css" href="/style/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css" />

<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript">

	
</script>
<style type="text/css">
ul{border:0; margin:0; padding:0;}
#pagination-flickr{background:#CBE6FF;}
#pagination-flickr li{font-size:12px;list-style:none;overflow:hidden;}
#pagination-flickr a{color: #3397CE;/*border:solid 1px #C0C0C0;border-right:0px;*/float:left;text-align:center;line-height:20px;padding:0;text-decoration:none;width:20px;}
#pagination-flickr a:link,#pagination-flickr a:visited {display:inline-block;float:left;text-decoration:none;}
#pagination-flickr a:hover{/*border:solid 1px #666666;*/background:#2E85C0;color:#fff;}

.list table tr td{background:none;font-family:微软雅黑;}
.list table thead tr td{background:none;}
table.tn_table{border:1px #C8CECE solid;border-top:none;}
table.tn_table tr td span{overflow:hidden;white-space:nowrap;text-overflow:ellipsis;}
.listRange_pagebar{width:auto;display:inline-block;padding:0;}
.listRange_pagebar div{line-height:25px;}
</style>
<script type="text/javascript">
//控制标头宽度
$(function(){
	var oThead = $(".tn_table thead");
	var oTbody = $(".tn_table tbody");
	$(".dHeight").css("height",oThead.find("tr").height());
	oThead.css("width",oTbody.width());
	var s = oTbody.find("tr:eq(0)").find("td").length;
	if(s == 0)
	{
		oThead.css({position:'relative'});
		$(".dHeight").remove();
	}else{
		for(var i =0;i<s;i++)
		{
			oThead.find("tr").find("td:eq("+i+")").css("width",oTbody.find("tr:eq(0)").find("td:eq("+i+")").width());
		}
	}
});

function save(){
	form.winCurIndex.value='$!winCurIndex'; 
	document.form.submit();
}

</script>
</head>

<body >
<iframe name="formFrame" style="display:none"></iframe>
<form method="post" scope="request" name="form"
			action="/RoleAction.do" onSubmit="return beforSubmit(this);"
			target="formFrame">
			<input type="hidden" name="operation" value="$globals.getOP("OP_SCOPE_RIGHT_ADD")"/>
			<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/>
			<input type="hidden" name="roleId" value="$roleId"/>
			<input type="hidden" name="flag" value="$!type"/>
			<input type="hidden" name="roleName" value="$!roleName"/>
			<input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
			<input type="hidden" name="tableDisplay" value="$!tableDisplay"/>
			<input type="hidden" name="fromUser" value="$!fromUser">
			
				
		<div style="overflow: hidden;overflow-y: auto;" id="data_list_id">
		<script type="text/javascript"> 
		var oDiv=document.getElementById("data_list_id");
		var sHeight=document.documentElement.clientHeight-10;		
		oDiv.style.height=sHeight+"px";
		</script>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
			<td valign="top" class="list">
			<table  class="tn_table" cellpadding="0" cellspacing="0" align="center" style="width: 100%;box-sizing:border-box;">
				<thead class="tn_thead" style="top:0px">
				<tr>
					<td>行号</td>
					<td>列名</td>
					#if($type == "4" )
					<td>隐藏</td>
					#if($tbtype == "TABLE" )
					<td>只读</td>
					#end
					#else
					<td>范围值</td>
					<td>范围结束值</td>
					#end
				</tr>
				</thead>
				<div class="dHeight" style="width:100%;">
				</div>
				#foreach($row in $!result)
				<tr onMouseMove="setBackground(this,true);"	onMouseOut="setBackground(this,false);" >
					<input type="hidden" name="field" value="$!globals.get($!row,0)_;_$!globals.get($!row,1)">
					<input type="hidden" name="name_$!globals.get($!row,0)_;_$!globals.get($!row,1)" value="$!globals.get($!row,2)">
					<td width="20"><span style="width:20px;display:inline-block;">$velocityCount</span></td>
					<td width="200"><span style="width:200px;display:inline-block;text-align:left;">$!globals.get($!row,2)</span></td>
					#if($type == "4" )
					<td width="50"><span style="width:50px;display:inline-block;"><input type="checkbox" name="hidden_$!globals.get($!row,0)_;_$!globals.get($!row,1)"  value="1"/></span></td>
					#if($tbtype == "TABLE")
					<td width="50"><span style="width:50px;display:inline-block;"><input type="checkbox" name="readOnly_$!globals.get($!row,0)_;_$!globals.get($!row,1)"  value="1"/></span></td>
					#end
					#else
						##查看范围值控制						
						#if("$!globals.get($!row,4)" != "")
							<input type="hidden" name="type_$!globals.get($!row,0)_;_$!globals.get($!row,1)" value="10">
							<td width="100"><span style="width:100px;display:inline-block;">
							<select style="width:95px" name="scopeValue_$!globals.get($!row,0)_;_$!globals.get($!row,1)"  >
							<option value=""></option>
							#foreach($rw in $!globals.get($!row,4).split("_:_")) 
							#set($pos = $rw.indexOf("_;_")+3)
							<option value="$rw">$rw.substring($pos)</option>
							#end
							</select>  
							</span></td>
							<td width="100"><span style="width:100px;display:inline-block;">
							</span></td>
						#elseif("$!globals.get($!row,3)" == "0" || "$!globals.get($!row,3)" == "1"  
							|| "$!globals.get($!row,3)" == "5"  || "$!globals.get($!row,3)" == "6")
							<input type="hidden" name="type_$!globals.get($!row,0)_;_$!globals.get($!row,1)" value="$!globals.get($!row,3)">
							<td width="100"><span style="width:100px;display:inline-block;">
							<input style="width:95px" type="text" name="scopeValue_$!globals.get($!row,0)_;_$!globals.get($!row,1)"  value=""
							#if("$!globals.get($!row,3)"=="6")
							onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});"
							#elseif("$!globals.get($!row,3)"=="5")
							onClick="WdatePicker({lang:'$globals.getLocale()'});"
							#end
							/>
							</span></td>
							<td width="100"><span style="width:100px;display:inline-block;">
							<input style="width:95px" type="text" name="escopeValue_$!globals.get($!row,0)_;_$!globals.get($!row,1)"  value=""
							#if("$!globals.get($!row,3)"=="6")
							onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});"
							#elseif("$!globals.get($!row,3)"=="5")
							onClick="WdatePicker({lang:'$globals.getLocale()'});"
							#end
							/>
							</span></td>
						#else
							<input type="hidden" name="type_$!globals.get($!row,0)_;_$!globals.get($!row,1)" value="10">
							<td width="100"><span style="width:100px;display:inline-block;">
							<input style="width:95px" type="text" name="scopeValue_$!globals.get($!row,0)_;_$!globals.get($!row,1)"  value=""
							/>
							</span></td>
							<td width="100"><span style="width:100px;display:inline-block;">							
							</span></td>
						#end
					#end
				</tr>
				#end
			</table>
			</td>
			</tr>
		</table> 
		#if("$!result"=="") 
		<div style="text-align: center;width: 100%;	height: 234px;line-height: 140px;	background: url(/style1/images/workflow/no_data_bg.gif) no-repeat center;float: left;margin-top: 20px;margin-bottom: 2px;">
			请选择模块
		</div>
		#end
		</div>	
		
</form>
</body>
</html>