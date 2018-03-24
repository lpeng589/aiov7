<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="/style/css/clientTransfer.css" />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css" />
<link type="text/css" rel="stylesheet" href="/style/css/brotherSetting.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript">

	$(function(){
		//删除方法
		$(".Del_Li a").live('click',function(){
			$(this).parent().parent().remove();
		});
		$(".viewList").width($(".viewItem").width());
		var showView = getParameter("moduleId"); 
		if(showView != ""){
			$("i.viewSelect").text($("span."+showView).text());
			$(".viewList").width($(".viewItem").width());
		}
		
		$(".viewItem").mouseover(function(){
			$(this).find(".viewList").show();
		}).mouseout(function(){
			$(this).find(".viewList").hide();
		});
	})
	
	/*
	添加映射值函数



	targetDiv:用于回填到DIV后面	
	*/
	function add(targetDiv){
		var erpHadFields = "";//存放erp已有字段
		var crmHadFields = "";//存放crm已有字段
		//循环已经有的字段
		$("#"+targetDiv+" ul").each(function(){
			erpHadFields += $(this).find("li:eq(0)").attr("id") +",";
			crmHadFields += $(this).find("li:eq(1)").attr("id") +",";
		})
		
		//选择拷贝隐藏域SELECT名称
		var copyERPSelectName = "erpTableBeanSelect";
		var copyCRMSelectName = "crmTableBeanSelect";
		if(targetDiv == "contactDiv"){
			//联系人用det的SELECT
			copyERPSelectName = "erpTableDetBeanSelect";
			copyCRMSelectName = "crmTableDetBeanSelect";
		}		
		
		//获取两个select
		var erpSelect = getFieldSelect(erpHadFields,copyERPSelectName,'erp');
		var crmSelect = getFieldSelect(crmHadFields,copyCRMSelectName,'crm');
		
		//封装UL
		var showUl = '<ul class="Content_U" name="add"><li class="Content_Li">';
		showUl +=erpSelect;
		showUl+=' </li><li class="Content_Li">';
		showUl +=crmSelect;		
		showUl+=' </li><li class="Del_Li"><a>删除</a></li></ul>';
		$("#"+targetDiv).append(showUl);
	}
	
	
	/*过滤已选的select方法
	str:已选字段



	copySelectName:隐藏的selectName
	selectType:是erp还是crm,用于提交时判断是否有重复值



	*/
	function getFieldSelect(str,copySelectName,selectType){
		str = ","+str;
		var newSelect = "<select class='Select_List' selectType='"+selectType+"'>"
		$("#"+copySelectName+" option").each(function(){
			if(str.indexOf(","+$(this).val()+",") == -1){
				newSelect +="<option value='"+$(this).val()+"'>"+$(this).text()+"</option>";
			}
		})
		newSelect +="</select>"
		return newSelect;
	}
	
	function subForm(){
	
		
		var flag = false;
		flag = checkSameField('clientDiv','erp');
		if(flag == true){
			return false;			
		}
		flag = checkSameField('clientDiv','crm');
		if(flag == true){
			return false;			
		}
		flag = checkSameField('contactDiv','erp');
		if(flag == true){
			return false;			
		}
		flag = checkSameField('contactDiv','crm');
		if(flag == true){
			return false;			
		}
	
		var transferFields ="";
		var erpHadFields = "";
		var crmHadFields =""
		$("#clientDiv ul[name!='add']").each(function(){
			erpHadFields = $(this).find("li:eq(0)").attr("id");
			crmHadFields = $(this).find("li:eq(1)").attr("id");	
			transferFields += erpHadFields+":"+crmHadFields+",";
		})
		$("#clientDiv ul[name='add']").each(function(){
			erpHadFields = $(this).find("select:eq(0)").val();
			crmHadFields = $(this).find("select:eq(1)").val();	
			transferFields += erpHadFields+":"+crmHadFields+",";
		})
		$("#contactDiv ul[name!='add']").each(function(){
			erpHadFields = "Contact_"+$(this).find("li:eq(0)").attr("id");
			crmHadFields = "Contact_"+$(this).find("li:eq(1)").attr("id");	
			transferFields += erpHadFields+":"+crmHadFields+",";
		})
		$("#contactDiv ul[name='add']").each(function(){
			erpHadFields = "Contact_"+$(this).find("select:eq(0)").val();
			crmHadFields = "Contact_"+$(this).find("select:eq(1)").val();	
			transferFields += erpHadFields+":"+crmHadFields+",";
		})
		$("#clientFieldsMapping").val(transferFields);
		form.submit();
	}
	
	
	/**
	*判断ERP，CRM SELECT选择是否重复
	*targetDiv :客户div or 联系人div
	*selectType : crm or erp
	*/
	function checkSameField(targetDiv,selectType){
		var tip = "客户资料";
		if(selectType == "erp"){
			tip = "潜在客户";
		}
		
		tip +="客户字段:";
		if(targetDiv == "contactDiv"){
			tip +="联系人字段:";
		}
	
	
		var v1 = "";
		var v2 = "";
		var group = $("#"+targetDiv+" select[selectType='"+selectType+"']");
		var len = group.length;
		for(var m=0;m<len;m++){
			v1 = group.get(m).value;
			for(var n=m+1;n<len;n++){
				v2 = group.get(n).value;
				if(v1 == v2){
					alert(tip+v1 +"重复");
					return true;//表示存在
				}
			}
		}
		return false;//表示不存在



	}
	//接收参数
	function getParameter(param)
	{
	     var query = window.location.search;
	     var iLen = param.length;
	     var iStart = query.indexOf(param);
	     if (iStart == -1)
	        return "";
	     iStart += iLen + 1;
	     var iEnd = query.indexOf("&", iStart);
	     if (iEnd == -1)
	     return query.substring(iStart); 
	     return query.substring(iStart, iEnd);
	}
	function getName()
	{
	  var name = getParameter("ID");
	  document.getElementById('name').innerHTML=name;
	}
	function changeModule(moduleId,obj){
		window.location.href="/CRMBrotherSettingAction.do?operation=$globals.getOP('OP_UPDATE_PREPARE')&type=fieldMapping&moduleId="+moduleId;
		
	}
</script>
</head>
<body >
	<form method="post" scope="request" name="form"
		action="/CRMBrotherSettingAction.do" class="mytable" >
		<input type="hidden" name="operation" id="operation" value="$globals.getOP('OP_UPDATE')" />
		<input type="hidden" name="type" id="type" value="fieldMapping" />
		<input type="hidden" name="clientFieldsMapping" id="clientFieldsMapping" value="$!clientFieldsMapping" />
		<input type="hidden" name="moduleId" id="moduleId" value="$!moduleId" />
		<!-- 存放主表与联系人表隐藏select开始 -->
		<select id="erpTableBeanSelect" style="display: none;">
			#foreach($erpBean in $potentialTableBean.fieldInfos)
				#if("$erpBean.inputType" != "1" && "$erpBean.inputType" != "3"&& "$erpBean.inputType" != "100")
				<option value="$erpBean.fieldName">$erpBean.fieldName ($erpBean.display.get("$globals.getLocale()"))</option>
				#end
			#end
		</select>
		<select id="erpTableDetBeanSelect" style="display: none;">
			#foreach($erpDetBean in $potentialTableBean.fieldInfos)
				#if("$erpDetBean.inputType" != "1" && "$erpDetBean.inputType" != "3"&& "$erpDetBean.inputType" != "100")
				<option value="$erpDetBean.fieldName">$erpDetBean.fieldName ($erpDetBean.display.get("$globals.getLocale()"))</option>
				#end
			#end
		</select>
		
		<select id="crmTableBeanSelect" style="display: none;">
			#foreach($crmBean in $clientInfoTableBean.fieldInfos)
				#if("$crmBean.inputType" != "1" && "$crmBean.inputType" != "3" && "$crmBean.inputType" != "100" && "$crmBean.fieldName" != "ClientNo" && "$crmBean.fieldName" != "Attachment")
					<option value="$crmBean.fieldName">$crmBean.fieldName ($crmBean.display.get("$globals.getLocale()"))</option>
				#end
			#end
		</select>
		<select id="crmTableDetBeanSelect" style="display: none;">
			#foreach($crmDetBean in $clientInfoDetTableBean.fieldInfos)
				#if("$crmDetBean.inputType" != "1" && "$crmDetBean.inputType" != "3" && "$crmDetBean.inputType" != "100" )
					<option value="$crmDetBean.fieldName">$crmDetBean.fieldName ($crmDetBean.display.get("$globals.getLocale()"))</option>
				#end
			#end
		</select>
		<!-- 存放主表与联系人表隐藏select结束 -->
		<div style="padding:5px 10px 0 10px;height:30px;">
			<div style="float:left;display:inline-block;line-height:26px;">当前位置:潜在客户映射字段</div>
			<div class="viewItem">
				<span><i class="viewSelect">默认模板</i><b></b></span>
				<ul class="viewList" style="display:none;">
					#foreach($module in $moduleList)
					<li onclick="changeModule('$globals.get($module,0)',this);">
						<span class='$globals.get($module,0)'>$globals.get($module,1)</span>
					</li>
					#end
				</ul>
			</div>
			<div class="btn btn-mini" onclick="subForm()" style="float:right;">保存</div>
		</div>
					
					<div id="cc" class="data" style="overflow:hidden;overflow-y:auto;width:100%;">
						<script type="text/javascript">
							var oDiv=document.getElementById("cc");
							var sHeight=document.documentElement.clientHeight-55;
							oDiv.style.height=sHeight+"px";
						</script>
						<!-- 映射字段开始 -->
						<div class="WrapDiv">
					  	<ul class="Title_U">
					    	<li class="Title_TLi">
					      	<span class="Title_TSpan">
					        	潜在客户表字段

					        </span>
					        <span class="Title_TSpan">
					        	客户资料表字段

					        </span>
					        <span class="Del_TSpan">
					        	操作
					        </span>
					      </li>
					    </ul>
					    #set($mainTableName = $tableName)
						#set($childTableName = $childTableName)
					    <div class="Content_List">
					    	<div class="AddList">
					      	<a onclick="add('clientDiv')">客户字段</a>
					      </div>
					    	<div class="Link_Div" id="clientDiv">
							#foreach($fileds in $!clientFieldsMapping.split(","))
								#if("$fileds"!="" && "$fileds"!="null" && $fileds.indexOf("Contact_") == -1)
						      	<ul class="Content_U" style="">
									#set($erpField = $globals.get($fileds.split(":"),0))
									#set($crmField = $globals.get($fileds.split(":"),1))
									<li id="$erpField" class="Content_Li">$erpField ($globals.getFieldBean("CRMPotentialClient",$erpField).display.get("$globals.getLocale()"))</li>
									<li id="$crmField" class="Content_Li">$crmField ($globals.getColName($crmField,$mainTableName,$childTableName))</li>
									<li class="Del_Li"><a>删除</a></li>
						        </ul>
						        #end
							#end
					      </div>
					      <!--分割线-->
					      <div class="AddList">
					      	<a onclick="add('contactDiv')">联系人字段</a>
					      </div>
					    	<div class="Link_Div" id="contactDiv">
					      	#foreach($fileds in $!clientFieldsMapping.split(","))
								#if("$fileds"!="" && "$fileds"!="null" && $fileds.indexOf("Contact_") > -1)
						      	<ul class="Content_U" style="">
									#set($erpField = $globals.get($fileds.split(":"),0))
									#set($crmField = $globals.get($fileds.split(":"),1))
									#set($erpField = $globals.replaceString("$erpField","Contact_",""))
									#set($crmField = $globals.replaceString("$crmField","Contact_",""))
									<li id="$erpField" class="Content_Li">$erpField ($globals.getFieldBean("CRMPotentialClient",$erpField).display.get("$globals.getLocale()"))</li>
									<li id="$crmField" class="Content_Li">$crmField ($globals.getColName($crmField,$mainTableName,$childTableName))</li>
									<li class="Del_Li"><a>删除</a></li>
						        </ul>
						        #end
							#end
					      </div>
					    </div>
					 	</div>
					 
					 	<!-- 映射字段结束-->
					</div>
				
	</form>
</body>
</html>
