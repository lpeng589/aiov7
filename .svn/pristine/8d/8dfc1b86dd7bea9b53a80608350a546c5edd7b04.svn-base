<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta name="renderer" content="webkit">
<title>客户共享</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script language="javascript" src="/js/public_utils.js"></script>
<style type="text/css">
.oa_signDocument2{width: 30px;}
a{color:#000000;}
a:link{color:#000000;}
a:visited{color:#000000;}
a:hover{color:#000000;}
body{background-color:white;}
</style>
<script type="text/javascript">

	function fillData(datas){
		newOpenSelect(datas,fieldNames,fieldNIds,1);
		parent.jQuery.close('Popdiv');
	}
	
	//返回客户IDS
	function returnVal(valName){
		return jQuery("#"+valName).val();
	}
	
	//返回新增的数据


	function returnSaveVal(valueName){
		var reValues = jQuery("#"+valueName+"Bean").val();
		var nowValues = jQuery("#"+valueName).val();
		var returnVal = "";
		for(var i=0;i<nowValues.split(",").length;i++){
			if(reValues.indexOf(nowValues.split(",")[i]) == -1){
				returnVal +=nowValues.split(",")[i] + ",";
			}			
		}
		return returnVal;
	}
	
	//返回删除的数据

	function returnDelVal(valueName){
		var reValues = jQuery("#"+valueName+"Bean").val();
		var delValues = jQuery("#del"+valueName).val();
		var returnVal = "";
		if(delValues !=""){
			for(var i=0;i<delValues.split(",").length;i++){
				if(reValues.indexOf(delValues.split(",")[i]) != -1){
					returnVal +=delValues.split(",")[i] + ",";
				}			
			}
		}
		return returnVal;
	}
	
	function deleteOpation(fileName,popedomId){
		var index = jQuery("#"+fileName+" option:selected").index();
		if(index==-1){
			alert("请选择要移除的项!");
			return false;
		}
		//得到需要删除的通知消息
		var delValues = "";//删除的Ids
		var delName ="";//删除的id名字
		jQuery("#"+fileName+" option:selected").each(function(){
			delValues += jQuery(this).val() + ",";
		})
		if(fileName == "formatDeptName"){
			delName = "delpopedomDeptIds";
		}
		if(fileName == "formatFileName"){
			delName = "delpopedomUserIds";
		}
		if(fileName == "EmpGroup"){
			delName = "delpopedomEmpGroupIds";
		}
		if(fileName == "selTitle"){
			delName = "delpopedomTitleIds";
		}
		jQuery("#"+delName).val(jQuery("#"+delName).val()+delValues)
		jQuery("#"+fileName+" option:selected").remove();
		getShowContent(fileName,popedomId);
	}
	
	/*获取指定下拉列表的值，并为指定的参数赋值*/
	function getShowContent(showOption,param){
		var showContent="";
		jQuery("#"+showOption+" option").each(function(){
		   showContent+= this.value+",";
		});
		jQuery("#"+param).attr("value",showContent);
	}
	
	/*返回是否通知提醒共享客户*/
	function returnIsShare(){
		return jQuery("input[name='isShareClient']:checked").val() ;
	}
	
	function showGraduate(showId){
		var urls = '/vm/crm/client/titleOpenSel.jsp' ;
		asyncbox.open({
			id:'titleOpenId',title :'选择职位',url:urls,cache:false,modal:true,width:300,height:290, btnsbar:jQuery.btn.OKCANCEL,
			callback : function(action,opener){
	　　　　　	if(action == 'ok'){
					var str = opener.returnVal();
					newOpenSelect(str,'selTitle','popedomTitleIds',1);
	　　　　　	}
	　　　	}
　		});
	}
	
</script>
</head>
  
  <body>
		<input type="hidden" name="clientIds" id="clientIds" value="$!clientIds" />
		<input type="hidden" name="isSingle" id="isSingle" value="$!isSingle" />
		<input type="hidden" name="popedomDeptIdsBean" id="popedomDeptIdsBean" value="$!shareClientBean.popedomDeptIds" />
		<input type="hidden" name="popedomUserIdsBean" id="popedomUserIdsBean" value="$!shareClientBean.popedomUserIds" />
		<input type="hidden" name="popedomEmpGroupIdsBean" id="popedomEmpGroupIdsBean" value="$!shareClientBean.popedomEmpGroupIds" />
		<input type="hidden" name="popedomTitleIdsBean" id="popedomTitleIdsBean" value="$!shareClientBean.popedomTitleIds" />
  		<input type="hidden" name="popedomDeptIds" id="popedomDeptIds" value="$!shareClientBean.popedomDeptIds" />
		<input type="hidden" name="popedomUserIds" id="popedomUserIds" value="$!shareClientBean.popedomUserIds" />
		<input type="hidden" name="popedomEmpGroupIds" id="popedomEmpGroupIds" value="$!shareClientBean.popedomEmpGroupIds" />
		<input type="hidden" id="popedomTitleIds" name="popedomTitleIds" value="$!shareClientBean.popedomTitleIds">
		<input type="hidden" name="delpopedomDeptIds" id="delpopedomDeptIds" value="" />
		<input type="hidden" name="delpopedomUserIds" id="delpopedomUserIds" value="" />
		<input type="hidden" name="delpopedomEmpGroupIds" id="delpopedomEmpGroupIds" value="" />
		<input type="hidden" id="delpopedomTitleIds" name="delpopedomTitleIds" value="">
    	<span style="width: 100%;margin: 10px 0 5px 15px">通知提醒共享人：	
			<input type="radio" name="isShareClient"  value="1"/>是&nbsp;<input type="radio" name="isShareClient" value="0" checked="checked"/>否

		</span>
		<div id="_title">
		<div class="oa_signDocument1" id="_context" style="margin-left: 15px;">
		<div class="oa_signDocument_3">
			<img src="/$globals.getStylePath()/images/St.gif"
				onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');" alt="$text.get("oa.common.adviceDept")" class="search"/>
			&nbsp;
			<a href="javascript:void(0)" title="$text.get("oa.common.adviceDept")" onClick="deptPop('deptGroup','formatDeptName','popedomDeptIds','1');">$text.get("oa.common.adviceDept")</a>
		</div>
		<select name="formatDeptName" id="formatDeptName" size="5"
			multiple="multiple">
			#foreach($dept in $targetDept)
			<option value="$!globals.get($dept,14)">
				$!globals.get($dept,2) 
			</option>
			#end
		</select>
		</div>
		<div class="oa_signDocument2">
			<img onClick="deleteOpation('formatDeptName','popedomDeptIds');"
				src="/$globals.getStylePath()/images/delete_button.gif"
				alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search"/>
		</div>
		<div class="oa_signDocument1" id="_context">
		<div class="oa_signDocument_3">
			<img src="/$globals.getStylePath()/images/St.gif"
				onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');" alt="$text.get("oa.common.advicePerson")" class="search"/>
			&nbsp;
			<a href="javascript:void(0)" title="$text.get("oa.common.advicePerson")" onClick="deptPop('userGroup','formatFileName','popedomUserIds','1');">$text.get("oa.common.advicePerson")</a>
		</div>
		<select name="formatFileName" id="formatFileName" size="5"
			multiple="multiple">
			#foreach($user in $targetUsers)
			<option value="$user.id">
				$!user.EmpFullName
			</option>
			#end
		</select>
		</div>
		<div class="oa_signDocument2">
			<img onClick="deleteOpation('formatFileName','popedomUserIds');"
				src="/$globals.getStylePath()/images/delete_button.gif"
				alt="$text.get("oa.common.clear")"  title="$text.get("oa.common.clear")"  class="search"/>
		</div>


	<!-- 
		职员分组
		<div class="oa_signDocument1" id="_context">
		<div class="oa_signDocument_3">
			<img src="/$globals.getStylePath()/images/St.gif"
				onClick="deptPop('empGroup','EmpGroup','popedomEmpGroupIds','1');"
				alt="$text.get("oa.oamessage.usergroup")" class="search"/>
			&nbsp;
			<a href="javascript:void(0)" style="cursor:pointer"
				title="$text.get('oa.oamessage.usergroup')"
				onClick="deptPop('empGroup','EmpGroup','popedomEmpGroupIds','1');">$text.get("oa.oamessage.usergroup")</a>
		</div>
		<select name="EmpGroup" id="EmpGroup" size="5" multiple="multiple">
			#foreach($grp in $targetEmpGroup)
			<option value="$!globals.get($grp,0)">
				$!globals.get($grp,5) 
			</option>
			#end
		</select>
		</div>
		<div class="oa_signDocument2">
			<img onClick="deleteOpation('EmpGroup','popedomEmpGroupIds')"
				src="/$globals.getStylePath()/images/delete_button.gif"
				alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search"/>
		</div>
		</div>
	 -->
	 <div class="oa_signDocument1" id="_context">
		<div class="oa_signDocument_3">
			<img src="/$globals.getStylePath()/images/St.gif"
				onClick="showGraduate('graduateDiv')"
				alt="$text.get("oa.oamessage.usergroup")" class="search"/>
			&nbsp;
			<a href="javascript:void(0)" style="cursor:pointer"
				title="$text.get('oa.oamessage.usergroup')"
				onClick="showGraduate('graduateDiv')">选择职位</a>
		</div>
		<select name="selTitle" id="selTitle" size="5" multiple="multiple">
			#foreach($titleId in $!shareClientBean.popedomTitleIds.split(","))
				#if("$titleId" !="")
					<option value="$titleId">
						$globals.getEnumerationItemsDisplay("duty","$titleId") 
					</option>
				#end
			#end
		</select>
		</div>
		<div class="oa_signDocument2">
			<img onClick="deleteOpation('selTitle','popedomTitleIds')"
				src="/$globals.getStylePath()/images/delete_button.gif"
				alt="$text.get("oa.common.clear")" title="$text.get("oa.common.clear")"  class="search"/>
		</div>
	</div>
	
  </body>
</html>
