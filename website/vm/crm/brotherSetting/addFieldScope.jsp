<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<title>demo</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="/style/css/brotherSetting.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<style type="text/css">
body{width:820px;}
</style>

<script type="text/javascript">
	function beforeSubmit(){
		if($("#readDeptIds").val()=="" && $("#readUserIds").val()=="" && $("#hideDeptIds").val()=="" && $("#hideUserIds").val()==""){
			alert("必须选择只读或隐藏对象.");
			return false;			
		}
		
		//循环select获取选中的值

		var fieldsName = "";
		$("#right option").each(function(){
			fieldsName += $(this).val()+",";
		})
		
		if(fieldsName == ""){
			alert("请选择控制字段");
			return false;
		}else{
			$("#fieldsName").val(fieldsName);
		}
		
		form.submit();
	}
</script>
</head>
<body>
<form action="/CRMBrotherFieldScopeAction.do" name="form" method="post">
<input type="hidden" name="operation" id="operation" value='$globals.getOP("OP_ADD")' />
<input type="hidden" name="readDeptIds" id="readDeptIds" value=""/>
<input type="hidden" name="readUserIds" id="readUserIds" value=""/>
<input type="hidden" name="hideDeptIds" id="hideDeptIds" value=""/>
<input type="hidden" name="hideUserIds" id="hideUserIds" value=""/>
<input type="hidden" name="fieldsName" id="fieldsName" value=""/>
<input type="hidden" name="tableName" id="tableName" value="$tableName"/>
	<div style="overflow:hidden;padding:5px 0 0 10px;">
		<div style="float:left;display:inline-block;line-height:26px;">当前位置：字段权限添加</div>
		<div class="btn btn-small" onclick="beforeSubmit()" style="float:right;">保存</div>
	</div>
	<div class="wp_dv" style="height:400px;overflow:auto;margin:5px 0 0 10px;">
		<!-----------分割线 wp_bk板块 Start------------->
		<div class="wp_bk">
			<div class="bk_t">
				<i>字段</i>
			</div>
			<div class="bk_c">
				<div class="ddv">
					<!-----------分割线 Select选择 Start------------->
					<div class="slt_wdv">
						<div class="bk_dv">
							<select class="left_slt" multiple name="left" id="left" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('left'), document.getElementById('right'))">
							   #foreach($fieldBean in $fieldList)
									#if($globals.canSelectField($fieldBean,$tableName,$childTableName) )
										#if("$fieldBean.tableBean.tableName" == "$tableName")
											<option title="$fieldBean.fieldName" value="$fieldBean.fieldName">$fieldBean.display.get("$globals.getLocale()")</option>
										#else
											<option title="child$fieldBean.fieldName" value="child$fieldBean.fieldName">明细--$fieldBean.display.get("$globals.getLocale()")</option>
										#end
									#end
								#end
							</select>
							<p class="note_p pRed">（已选字段）<p>
						</div>
						<div class="bk_dv bks">
							<a class="left_a bga" title="添加" onclick="moveOption(document.getElementById('left'),document.getElementById('right'))" href="javascript:void(0);"></a>
							<a class="right_a bga" title="移除" onclick="moveOption(document.getElementById('right'), document.getElementById('left'))" href="javascript:void(0);"></a>
						</div>
						<div class="bk_dv">
							<select class="right_slt" multiple name="right" id="right" size="8" style='width:100px;' ondblclick="moveOption(document.getElementById('right'), document.getElementById('left'))">
							</select>
							<p class="note_p pRed">（已选字段）<p>
						</div>
						<div class="bk_dv bks">
							<a class="upT_a bga" title="置顶" onclick="moveTop(document.getElementById('right'));" href="javascript:void(0);"></a>
							<a class="up_a bga" title="上移" onclick="moveUp(document.getElementById('right'));" href="javascript:void(0);"></a>
							<a class="down_a bga" title="下移" onclick="moveDown(document.getElementById('right'));" href="javascript:void(0);"></a>
							<a class="downD_a bga" title="置底" onclick="moveBottom(document.getElementById('right'));" href="javascript:void(0);"></a>
						</div>
					</div>
					<!-----------分割线 Select选择 End------------->
				</div>
			</div>
		</div>
		<!-----------分割线 wp_bk板块 End------------->

		<!-----------分割线 wp_bk板块 Start------------->
		<div class="wp_bk">
			<div class="bk_t">
				<i>只读对象</i>
			</div>
			<div class="bk_c">
				<div class="ddv">
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" name="readDeptName" id="readDeptName" multiple="multiple">
								
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('deptGroup','readDeptName','readDeptIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('readDeptName','readDeptIds')"></a>
						</div>
						<p class="note_p">（选择部门）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" id="readUserName" name="readUserName" multiple="multiple">
								
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('userGroup','readUserName','readUserIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('readUserName','readUserIds')"></a>
						</div>
						<p class="note_p">（选择个人）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->		
				</div>
			</div>
		</div>
		<!-----------分割线 wp_bk板块 End------------->
		
		<!-----------分割线 wp_bk板块 Start------------->
		<div class="wp_bk">
			<div class="bk_t">
				<i>隐藏对象</i>
			</div>
			<div class="bk_c">
				<div class="ddv">
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" name="hideDeptName" id="hideDeptName" multiple="multiple">
								
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('deptGroup','hideDeptName','hideDeptIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('hideDeptName','hideDeptIds')"></a>
						</div>
						<p class="note_p">（选择部门）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->
					<!-----------分割线 xjj_slt选择 Start------------->
					<div class="slt_woy">
						<div class="woy_lt">
							<select class="slt" size="5" id="hideUserName" name="hideUserName" multiple="multiple">
								
							</select>
						</div>
						<div class="woy_rt">
							<a class="add_a bga" title="添加" onClick="deptPop('userGroup','hideUserName','hideUserIds','1');"></a>
							<a class="del_a bga" title="删除" onClick="deleteOpation('hideUserName','hideUserIds')"></a>
						</div>
						<p class="note_p">（选择个人）</p>
					</div>
					<!-----------分割线 xjj_slt选择 Start------------->	
				</div>	
			</div>
		</div>
		<!-----------分割线 wp_bk板块 End------------->
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
			 for(var i = obj1.options.length - 1 ; i >= 0 ; i--)
			 {
				 if(obj1.options[i].selected)
				 {
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
</body>
</html>
