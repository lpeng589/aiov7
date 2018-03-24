<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css"/>
<link rel="stylesheet" href="/style1/css/workflow2.css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<script language="javascript" src="/js/function.js"></script>

<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>

<script type="text/javascript">
function isDelete(m){	
	if(confirm('$text.get("oa.common.sureDelete")')){
		document.location = "/FieldScopeSetAction.do?operation=$globals.getOP("OP_DELETE")&delType=fieldScopeSet&keyId="+m+"&viewId=$viewId";		
	}
}
function BatchDelete(){
	var selectIds="";
	$("input[name='keyId']").each(function(){
	   if($(this).attr("checked")=="checked")
	  		selectIds+=this.value+";";
	});
	if(selectIds==""){
		alert("$text.get('common.msg.mustSelectOne')");
		return false;
	}else{
		if(confirm("$text.get('oa.common.sureDelete')")){
			window.location.href="/FieldScopeSetAction.do?operation=3&delType=fieldScopeSet&keyId="+selectIds +"&viewId=$viewId";
		}
	}
}

document.onkeydown = keyDown; 
</script>
</head>

<body>
	<form style="margin:0px;"  method="post" id="form" name="form" action="">
	 <input type="hidden" name="operation" value="$globals.getOP('OP_QUERY')"/>
		<table cellpadding="0" cellspacing="0" border="0" class="framework" >
			<tr>
				<td>
					<div class="TopTitle">
						<span>
						当前位置:字段权限设置
						</span>
						<span style="float: right;font-weight: normal;margin-right: 12px;margin-bottom: 4px;">
							<input type="button" class="bu_02" onclick="window.location.href='/FieldScopeSetAction.do?operation=$globals.getOP("OP_ADD_PREPARE")&addPreType=fieldScopeSet&viewId=$!viewId&moduleId=$!moduleId'" value="$text.get("common.lb.add")" />				
							<input type="button" class="bu_02" onclick="BatchDelete();" value="$text.get("common.lb.del")" />				
						</span>
					</div>
					
					<table class="data_list_head"  cellpadding="0" cellspacing="0" >
						<thead>
							<tr>
								<td width="25px;">
									序号
								</td>
								<td  width="25px;" style="vertical-align:middle;">
									<input type="checkbox"  value="checkbox" name="selAll" onClick="checkAll('keyId')"/>
								</td>
								<td  width="*">
									字段名称
								</td>
								<td  width="100px;"> 
									创建人




								</td>
								<td  width="140px;">
									创建时间
								</td>
								<td  width="190px;" align="center">
									操作
								</td>
							</tr>
						</thead>
					</table>
					<div id="data_list_id" class="data_list"  style="overflow:hidden;overflow-y:auto;width:99%;margin-top: 0px;" >
<script type="text/javascript">
	var oDiv=document.getElementById("data_list_id");
	var sHeight=document.documentElement.clientHeight-115;
	oDiv.style.height=sHeight+"px";

</script>
						<table  cellpadding="0" cellspacing="0" >					
							<tbody>
								#foreach ($row in $setList )
							
								<tr #if($globals.isOddNumber($velocityCount)) class="c1" #else class="c2" #end>		
									<td width="25px;">
										 $velocityCount
									</td>
									<td  width="25px;" style="vertical-align:middle;">		
										<input type="checkbox" name="keyId" value="$row.id"/>
									</td>
									<td width="*" >
				    					#set($keyList=$!row.fieldsName.split(","))	
				    					#set($str="")		
									    #foreach($field in $keyList)	
									   		#if($field.indexOf("contact")>-1)
									    		#set($newfield=$field.replaceAll('contact',''))	
							    				#set($f=$globals.getFieldBean($!childTableName,$newfield))
												#set($dis=$globals.getLocaleDisplay("$f.display"))
									    		#set($str=$str+"联系人--"+$dis+",")
											#else
												#set($f=$globals.getFieldBean("$!mainTableName",$field))
												#set($dis=$globals.getLocaleDisplay("$f.display"))
									    		#set($str=$str+$dis+",")
											#end
									    #end	
									    
									   $globals.subTitle($str,90)
									</td>
									<td width="100px;">
										$globals.getEmpFullNameByUserId("$!row.createBy")&nbsp;
									</td>
									<td width="140px;">
										$!row.createTime &nbsp;
									</td>
									<td  width="190px;" >
										<a href="javascript:void(0);" onclick="javascript:window.location.href='/FieldScopeSetAction.do?operation=$globals.getOP("OP_UPDATE_PREPARE")&updPreType=fieldScopeSet&keyId=$!row.id&viewId=$!viewId'" style="margin-left: 40px;" ><span class="NI_2" >$text.get("oa.common.upd")</span></a>
										<a href="javascript:isDelete('$row.id');" style="margin-left: 10px;"><span class="NI_3">$text.get("oa.common.del")</span></a>	
									</td>
								</tr>
								#end
							</tbody>		
						</table>
						
					</div>
					<div class="listRange_pagebar">
							$!pageBar
					</div>	
			</td>
			</tr>
			</table>
		</form>
	</body>
	</html>