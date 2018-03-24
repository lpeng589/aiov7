<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<link rel="stylesheet" href="/$globals.getStylePath()/css/oa_news.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript">

function UpdEnum(enumId){
  asyncbox.open({
  		id: 'EnumWinow',
		title : "选项数据管理",
		url :'/EnumerationAction.do?operation=7&type=crm&keyId='+enumId,
		width : 620,
		height : 400,
		btnsbar : jQuery.btn.OKCANCEL,
		callback : function(action,opener){
  		if(action == 'ok'){
  			if(opener.beforSubmit(opener.document.form)){
  				alert('操作成功!');
  			}else{
  				return false;
  			}
  		}
  	  }
	});	 
}
document.onkeydown = keyDown; 
</script>
</head>
<body>
	<form method="post" scope="request" name="form"
		action="/ClientSettingAction.do?operation=$globals.getOP("OP_ADD")" class="mytable">
		<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span style="height: 24px;">当前位置:选项数据管理   </span>
					</div>
					<div id="cc" class="data"
						style="overflow:hidden;overflow-y:auto;width:100%;">
						<script type="text/javascript">
							var oDiv=document.getElementById("cc");
							var sHeight=document.documentElement.clientHeight-55;
							oDiv.style.height=sHeight+"px";
						</script>
						<div class="boxbg2 subbox_w700">
							<div class="subbox cf">
								<div class="inputbox">
									#foreach ($etable in $enumMap.keySet())	
									<h4 style="margin-top: 1px;">
										$globals.getTableDisplayName($etable)
									</h4>
									<div class="data_list" style="width:80%;margin:0px 10px 10px;">
										<table  cellpadding="0" cellspacing="0" style="border: 0px;" >					
											<tbody>
												#foreach ($enum in $enumMap.get($etable))					
												<tr #if($globals.isOddNumber($velocityCount)) class="c1" #else class="c2" #end>	
													<td width="25px;">
													 $velocityCount
													</td>
													<td width="35%">
													#if($etable == "CRMClientInfoDet" || $etable=="CRMClientInfo")
												 		#foreach($field in $fieldList)
													    	#if($field.refEnumerationName==$!globals.get($enum,0))
													    	#set($dis=$globals.getLocaleDisplay("$field.display"))
																$dis
													    	#end
													    #end	
													#else
													 	$!globals.get($enum,1)
													 #end
													</td>
													<td width="35%">
														$!globals.get($enum,0)
													</td>
													<td width="20%">
														<a href="javascript:void(0);" onclick="UpdEnum('$!globals.get($enum,2)')" ><span class="NI_2">$text.get("oa.common.upd")</span></a>
													</td>	
												</tr>
												#end
											</tbody>
									    </table>
									</div>
									#end
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
