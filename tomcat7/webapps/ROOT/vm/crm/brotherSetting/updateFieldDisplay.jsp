<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<title>demo</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="/style/css/brotherSetting.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<style type="text/css">
body{width:820px;}
</style>
<script type="text/javascript">
$(function(){
	#if("$!operationSuccess" == "true")
		asyncbox.tips('操作成功!','success');
	#end
})

function beforeSubmit(){
	if($("#pageFieldsRight option").length == 0){
		alert("新增、修改页面显示字段不可为空,请重新操作。");
		return false;
	}
	
	if($("#listFieldsRight option").length == 0){
		alert("列表信息显示字段不可为空,请重新操作。");
		return false;
	}
	
	setHideValues('pageFields');
	setHideValues('pageChildFields');
	setHideValues('keyFields');
	setHideValues('searchFields');
	setHideValues('listFields');
	setHideValues('detailFields');
	form.submit();
}

/*获取每个select标签选中的字段，封装并赋值给隐藏域*/
function setHideValues(selectName){
	var str ="";
	$("#"+selectName+"Right option").each(function(){
		str += $(this).val()+",";
	})
	$("#"+selectName).val(str);
}

</script>
</head>
<body>
<form action="/CRMBrotherSettingAction.do" name="form" method="post">
<input type="hidden" name="operation" id="operation" value="$globals.getOP("OP_UPDATE")">
<input type="hidden" name="tableName" id="tableName" value="$tableName">
<input type="hidden" name="pageFields" id="pageFields">
<input type="hidden" name="pageChildFields" id="pageChildFields">
<input type="hidden" name="keyFields" id="keyFields">
<input type="hidden" name="searchFields" id="searchFields">
<input type="hidden" name="listFields" id="listFields">
<input type="hidden" name="detailFields" id="detailFields">
	<div style="overflow:hidden;padding:5px 0 0 10px;">
		<div style="float:left;display:inline-block;line-height:26px;">当前位置：显示设置</div>
		<div class="btn btn-small" onclick="beforeSubmit()" style="float:right;">保存</div>
	</div>
	<div style="height:360px;overflow:auto;margin:5px 0 0 0;">
	<div class="wp_dv">
		<!-----------客户添加、修改、详情页面字段的显示 Start------------->
		<div class="wp_bk">
			<div class="bk_t">
				<i>添加、修改、详情页面显示</i>
			</div>
			<div class="bk_c">
				<div class="ddv">
					<!-----------分割线 Select选择 Start------------->
					<div class="slt_wdv">
						<div class="remark">
							主表字段
						</div>
						<div class="bk_dv">
							<p class="note_p">（可选字段）<p>
							<select class="left_slt" multiple name="pageFieldsLeft" id="pageFieldsLeft" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('pageFieldsLeft'), document.getElementById('pageFieldsRight'))">
								#set($selectFields = ","+$fieldDisplayBean.pageFields)
								#foreach($fieldBean in $mainFieldList)
									#if($globals.canSelectField($fieldBean,$tableName,$childTableName) )
										#if("$!fieldDisplayBean.pageFields" == "" || ("$selectFields.indexOf($fieldBean.fieldName)" == "-1" && "$fieldBean.statusId" == "1"))
											<option  value="$fieldBean.fieldName">$fieldBean.display.get("$globals.getLocale()")</option>
										#end
									#end
								#end
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="left_a bga" title="添加" onclick="moveOption(document.getElementById('pageFieldsLeft'),document.getElementById('pageFieldsRight'))" href="javascript:void(0);"></a>
							<a class="right_a bga" title="移除" onclick="moveOption(document.getElementById('pageFieldsRight'), document.getElementById('pageFieldsLeft'))" href="javascript:void(0);"></a>
						</div>
						<div class="bk_dv">
							<p class="note_p pRed">（已选字段）<p>
							<select class="right_slt" multiple name="pageFieldsRight" id="pageFieldsRight" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('pageFieldsRight'), document.getElementById('pageFieldsLeft'))">
								#foreach($fieldName in $fieldDisplayBean.pageFields.split(","))
									#if("$!fieldName" != "")
										#set($fieldBean = $globals.getFieldBean("$tableName",$fieldName))
										<option  value="$fieldBean.fieldName">#if("$fieldBean.isNull" == "1")*#end$fieldBean.display.get("$globals.getLocale()")</option>
									#end
								#end
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="upT_a bga" title="置顶" onclick="moveTop(document.getElementById('pageFieldsRight'));" href="javascript:void(0);"></a>
							<a class="up_a bga" title="上移" onclick="moveUp(document.getElementById('pageFieldsRight'));" href="javascript:void(0);"></a>
							<a class="down_a bga" title="下移" onclick="moveDown(document.getElementById('pageFieldsRight'));" href="javascript:void(0);"></a>
							<a class="downD_a bga" title="置底" onclick="moveBottom(document.getElementById('pageFieldsRight'));" href="javascript:void(0);"></a>
						</div>
					</div>
					<!-----------分割线 Select选择 End------------->
					
					#if("$!childTableName" !="" && "$!tableName" !="CRMsalesQuot" && "$!tableName" !="CRMSaleContract")
					<!-----------分割线 Select选择 Start------------->
					<div class="slt_wdv margin_rt">
						<div class="remark">
							明细字段
						</div>
						<div class="bk_dv">
							<p class="note_p">（可选字段）<p>
							<select class="left_slt" multiple name="pageChildFieldsLeft" id="pageChildFieldsLeft" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('pageChildFieldsLeft'), document.getElementById('pageChildFieldsRight'))">
								#set($selectFields = ","+$fieldDisplayBean.pageChildFields)
								#foreach($fieldBean in $childFieldList)
									#if($globals.canSelectField($fieldBean,$tableName,$childTableName) )
										#if("$!fieldDisplayBean.pageChildFields" == "" || ("$selectFields.indexOf($fieldBean.fieldName)" == "-1" && "$fieldBean.statusId" == "1"))
											<option  value="$fieldBean.fieldName">明细--$fieldBean.display.get("$globals.getLocale()")</option>
										#end
									#end
								#end
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="left_a bga" title="添加" onclick="moveOption(document.getElementById('pageChildFieldsLeft'),document.getElementById('pageChildFieldsRight'))" href="javascript:void(0);"></a>
							<a class="right_a bga" title="移除" onclick="moveOption(document.getElementById('pageChildFieldsRight'), document.getElementById('pageChildFieldsLeft'))" href="javascript:void(0);"></a>
						</div>
						<div class="bk_dv">
							<p class="note_p pRed">（已选字段）<p>
							<select class="right_slt" multiple name="pageChildFieldsRight" id="pageChildFieldsRight" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('pageChildFieldsRight'), document.getElementById('pageChildFieldsLeft'))">
								#foreach($fieldName in $fieldDisplayBean.pageChildFields.split(","))
									#if("$!fieldName" !="")
										#set($fieldBean = $globals.getFieldBean("$childTableName",$fieldName))
										<option  value="$fieldBean.fieldName">#if("$fieldBean.isNull" == "1")*#end明细--$fieldBean.display.get("$globals.getLocale()")</option>
									#end
								#end
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="upT_a bga" title="置顶" onclick="moveTop(document.getElementById('pageChildFieldsRight'));" href="javascript:void(0);"></a>
							<a class="up_a bga" title="上移" onclick="moveUp(document.getElementById('pageChildFieldsRight'));" href="javascript:void(0);"></a>
							<a class="down_a bga" title="下移" onclick="moveDown(document.getElementById('pageChildFieldsRight'));" href="javascript:void(0);"></a>
							<a class="downD_a bga" title="置底" onclick="moveBottom(document.getElementById('pageChildFieldsRight'));" href="javascript:void(0);"></a>
						</div>
					</div>
					<!-----------分割线 Select选择 End------------->
					#end
				</div>
			</div>
		</div>
		<!-----------客户添加、修改、详情页面字段的显示 End------------->


		<!-----------分割线 wp_bk板块 Start------------->
		<div class="wp_bk">
			<div class="bk_t">
				<i>查询显示</i>
			</div>
			<div class="bk_c">
				<div class="ddv">
					<!-----------模糊查询显示 Start------------->
					<div class="slt_wdv">
						<div class="remark">
							模糊字段显示
						</div>
						<div class="bk_dv">
							<p class="note_p">（可选字段）<p>
							<select class="left_slt" multiple name="keyFieldsLeft" id="keyFieldsLeft" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('keyFieldsLeft'), document.getElementById('keyFieldsRight'))">
								#set($selectFields = ","+$fieldDisplayBean.keyFields)
								#foreach($fieldBean in $fieldList)
									#if(("$globals.canSelectField($fieldBean,$tableName,$childTableName)" == "true" && "$fieldBean.inputType" == "0" && "$fieldBean.fieldType" != "1" && "$fieldBean.fieldType" != "13" && "$fieldBean.fieldType" != "14" && "$fieldBean.fieldType" != "5" && "$fieldBean.fieldType" != "6") || "$fieldBean.fieldName"=="ClientId")
										#if("$fieldBean.tableBean.tableName" == "$tableName")
											#if("$!fieldDisplayBean.keyFields" == "" || ($selectFields.indexOf("$fieldBean.fieldName") == -1 && "$fieldBean.statusId" == "1" ))
												<option  value="$fieldBean.fieldName">$fieldBean.display.get("$globals.getLocale()")</option>
											#end
										#end
									#end
								#end
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="left_a bga" title="添加" onclick="moveOption(document.getElementById('keyFieldsLeft'),document.getElementById('keyFieldsRight'))" href="javascript:void(0);"></a>
							<a class="right_a bga" title="移除" onclick="moveOption(document.getElementById('keyFieldsRight'), document.getElementById('keyFieldsLeft'))" href="javascript:void(0);"></a>
						</div>
						<div class="bk_dv">
							<p class="note_p pRed">（已选字段）<p>
							<select class="right_slt" multiple name="keyFieldsRight" id="keyFieldsRight" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('keyFieldsRight'), document.getElementById('keyFieldsLeft'))">
								#foreach($fieldName in $fieldDisplayBean.keyFields.split(","))
									#if("$!fieldName" != "")
										#if($fieldName.indexOf("child")==-1)
											#set($fieldBean = $globals.getFieldBean("$tableName",$fieldName))
											<option  value="$fieldBean.fieldName">$fieldBean.display.get("$globals.getLocale()")</option>
										#else
											#set($fieldBean = $globals.getFieldBean("$childTableName",$fieldName))
											<option  value="$fieldBean.fieldName">明细--$fieldBean.display.get("$globals.getLocale()")</option>
										#end
									#end
								#end
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="upT_a bga" title="置顶" onclick="moveTop(document.getElementById('keyFieldsRight'));" href="javascript:void(0);"></a>
							<a class="up_a bga" title="上移" onclick="moveUp(document.getElementById('keyFieldsRight'));" href="javascript:void(0);"></a>
							<a class="down_a bga" title="下移" onclick="moveDown(document.getElementById('keyFieldsRight'));" href="javascript:void(0);"></a>
							<a class="downD_a bga" title="置底" onclick="moveBottom(document.getElementById('keyFieldsRight'));" href="javascript:void(0);"></a>
						</div>
					</div>
					<!-----------模糊查询显示 End------------->
					<!-----------条件查询 Start------------->
					<div class="slt_wdv margin_rt">
						<div class="remark">
							枚举字段、时间字段显示
						</div>
						<div class="bk_dv">
							<p class="note_p">（可选字段）<p>
							<select class="left_slt" multiple name="searchFieldsLeft" id="searchFieldsLeft" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('searchFieldsLeft'), document.getElementById('searchFieldsRight'))">
								#set($selectFields = ","+$fieldDisplayBean.searchFields)
								#foreach($fieldBean in $fieldList)
									#if("$globals.canSelectField($fieldBean,$tableName,$childTableName)" == "true" && ("$fieldBean.inputType" == "1" || "$fieldBean.fieldType" == "5" || "$fieldBean.fieldType" == "6"))
										#if("$!fieldDisplayBean.searchFields" == "" || ("$fieldBean.tableBean.tableName" == "$tableName" && $selectFields.indexOf("$fieldBean.fieldName") == -1 && "$fieldBean.statusId" == "1"))
											<option  value="$fieldBean.fieldName">$fieldBean.display.get("$globals.getLocale()")</option>
										#end
									#end
								#end
								
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="left_a bga" title="添加" onclick="moveOption(document.getElementById('searchFieldsLeft'),document.getElementById('searchFieldsRight'))" href="javascript:void(0);"></a>
							<a class="right_a bga" title="移除" onclick="moveOption(document.getElementById('searchFieldsRight'), document.getElementById('searchFieldsLeft'))" href="javascript:void(0);"></a>
						</div>
						<div class="bk_dv">
							<p class="note_p pRed">（已选字段）<p>
							<select class="right_slt" multiple name="searchFieldsRight" id="searchFieldsRight" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('searchFieldsRight'), document.getElementById('searchFieldsLeft'))">
								#foreach($fieldName in $fieldDisplayBean.searchFields.split(","))
									#if("$!fieldName" != "")
										#if($fieldName.indexOf("child")==-1)
											#set($fieldBean = $globals.getFieldBean("$tableName",$fieldName))
											<option  value="$fieldBean.fieldName">$fieldBean.display.get("$globals.getLocale()")</option>
										#else
											#set($fieldBean = $globals.getFieldBean("$childTableName",$fieldName))
											<option  value="$fieldBean.fieldName">明细--$fieldBean.display.get("$globals.getLocale()")</option>
										#end
									#end
								#end
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="upT_a bga" title="置顶" onclick="moveTop(document.getElementById('searchFieldsRight'));" href="javascript:void(0);"></a>
							<a class="up_a bga" title="上移" onclick="moveUp(document.getElementById('searchFieldsRight'));" href="javascript:void(0);"></a>
							<a class="down_a bga" title="下移" onclick="moveDown(document.getElementById('searchFieldsRight'));" href="javascript:void(0);"></a>
							<a class="downD_a bga" title="置底" onclick="moveBottom(document.getElementById('searchFieldsRight'));" href="javascript:void(0);"></a>
						</div>
					</div>
					<!-----------条件查询 End------------->
				</div>
			</div>
		</div>
		<!-----------分割线 wp_bk板块 End------------->
		
		<!-----------分割线 wp_bk板块 Start------------->
		<div class="wp_bk">
			<div class="bk_t">
				<i>列表信息显示</i>
			</div>
			<div class="bk_c">
				<div class="ddv">
					<!-----------列表信息显示 Start------------->
					<div class="slt_wdv">
						<div class="remark">
							列表字段显示
						</div>
						<div class="bk_dv">
							<p class="note_p">（可选字段）<p>
							<select class="left_slt" multiple name="listFieldsLeft" id="listFieldsLeft" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('listFieldsLeft'), document.getElementById('listFieldsRight'))">
								#set($selectFields = ","+$fieldDisplayBean.listFields)
								#foreach($fieldBean in $fieldList)
									#if($globals.canSelectField($fieldBean,$tableName,$childTableName) && "$fieldBean.fieldType" != "13" && "$fieldBean.fieldType" != "14")
										#if("$fieldBean.tableBean.tableName" == "$tableName")
											#if("$!fieldDisplayBean.listFields" == "" || ($selectFields.indexOf("$fieldBean.fieldName") == -1 && "$fieldBean.statusId" == "1"))
												<option  value="$fieldBean.fieldName">$fieldBean.display.get("$globals.getLocale()")</option>
											#end
										#end
									#end
								#end 
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="left_a bga" title="添加" onclick="moveOption(document.getElementById('listFieldsLeft'),document.getElementById('listFieldsRight'))" href="javascript:void(0);"></a>
							<a class="right_a bga" title="移除" onclick="moveOption(document.getElementById('listFieldsRight'), document.getElementById('listFieldsLeft'))" href="javascript:void(0);"></a>
						</div>
						<div class="bk_dv">
							<p class="note_p pRed">（已选字段）<p>
							<select class="right_slt" multiple name="listFieldsRight" id="listFieldsRight" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('listFieldsRight'), document.getElementById('listFieldsLeft'))">
								#foreach($fieldName in $fieldDisplayBean.listFields.split(","))
									#if("$!fieldName" != "")
										#set($fieldBean = $globals.getFieldBean("$tableName",$fieldName))
										<option  value="$fieldBean.fieldName">$fieldBean.display.get("$globals.getLocale()")</option>
									#end
								#end
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="upT_a bga" title="置顶" onclick="moveTop(document.getElementById('listFieldsRight'));" href="javascript:void(0);"></a>
							<a class="up_a bga" title="上移" onclick="moveUp(document.getElementById('listFieldsRight'));" href="javascript:void(0);"></a>
							<a class="down_a bga" title="下移" onclick="moveDown(document.getElementById('listFieldsRight'));" href="javascript:void(0);"></a>
							<a class="downD_a bga" title="置底" onclick="moveBottom(document.getElementById('listFieldsRight'));" href="javascript:void(0);"></a>
						</div>
					</div>
					<!-----------列表信息显示 End------------->
					#if("$!childTableName" != "")
					<!-----------详情信息显示 Start------------->
					<!-- 
					<div class="slt_wdv margin_rt">
						<div class="remark">
							列表详情字段显示
						</div>
						<div class="bk_dv">
							<p class="note_p">（可选字段）<p>
							<select class="left_slt" multiple name="detailFieldsLeft" id="detailFieldsLeft" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('detailFieldsLeft'), document.getElementById('detailFieldsRight'))">
								#set($selectFields = ","+$fieldDisplayBean.detailFields)
								#foreach($fieldBean in $fieldList)
									#if($globals.canSelectField($fieldBean,$tableName,$childTableName) )
										#if("$fieldBean.tableBean.tableName" == "$childTableName")
											#if($selectFields.indexOf("child${fieldBean.tableName}") == -1 && "$fieldBean.statusId" == "1")
												<option  value="child$fieldBean.fieldName">明细--$fieldBean.display.get("$globals.getLocale()")</option>
											#end
										#end
									#end
								#end 
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="left_a bga" title="添加" onclick="moveOption(document.getElementById('detailFieldsLeft'),document.getElementById('detailFieldsRight'))" href="javascript:void(0);"></a>
							<a class="right_a bga" title="移除" onclick="moveOption(document.getElementById('detailFieldsRight'), document.getElementById('detailFieldsLeft'))" href="javascript:void(0);"></a>
						</div>
						<div class="bk_dv">
							<p class="note_p pRed">（已选字段）<p>
							<select class="right_slt" multiple name="detailFieldsRight" id="detailFieldsRight" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('detailFieldsRight'), document.getElementById('detailFieldsLeft'))">
								#foreach($fieldName in $fieldDisplayBean.detailFields.split(","))
									#if("$!fieldName" !="")
										#set($fieldBean = $globals.getFieldBean("$childTableName",$fieldName))
										<option  value="$fieldBean.fieldName">明细--$fieldBean.display.get("$globals.getLocale()")</option>
									#end
								#end
							</select>
						</div>
						<div class="bk_dv bks">
							<a class="upT_a bga" title="置顶" onclick="moveTop(document.getElementById('detailFieldsRight'));" href="javascript:void(0);"></a>
							<a class="up_a bga" title="上移" onclick="moveUp(document.getElementById('detailFieldsRight'));" href="javascript:void(0);"></a>
							<a class="down_a bga" title="下移" onclick="moveDown(document.getElementById('detailFieldsRight'));" href="javascript:void(0);"></a>
							<a class="downD_a bga" title="置底" onclick="moveBottom(document.getElementById('detailFieldsRight'));" href="javascript:void(0);"></a>
						</div>
					</div>
					 -->
					<!-----------详情信息显示 End------------->
					#end
				</div>
			</div>
		</div>
		<!-----------分割线 wp_bk板块 End------------->
	</div>
	</div>
</form>
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
			/*
			alert(obj1.options.length);
			alert(j(obj1).find("option:selected").length);
			*/
			 var error = "false";
			 $(obj1).find("option:selected").each(function(){
			 	if($(this).text().indexOf("*") >-1){
			 		alert($(this).text().substring(1)+"为必填项,不可移除")
			 		error = "true";
			 		return fase;
			 	}
			 })
			 
			 if(error == "true"){
		 	 	return false;
		 	 }
			 
			 for(var i = obj1.options.length - 1 ; i >= 0 ; i--)
			 {
			 	
				 if(obj1.options[i].selected)
				 {
					var opt = new Option(obj1.options[i].text,obj1.options[i].value);
					//opt.selected = true;
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

</body>
</html>
