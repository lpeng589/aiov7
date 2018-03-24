<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link type="text/css" href="/js/skins/ZCMS/asyncbox.css" rel="stylesheet" />
<link type="text/css" href="/style/css/chooseInfo.css" rel="stylesheet" />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script src="/js/jquery.cookie.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript"> 
	var moduleId = "$LoginBean.getOperationMap().get("/EmployeeDepartmentAction.do").moduleId";
	jQuery(document).ready(function(){
		
		#if("$!operation" == "1")
			//添加
		#elseif("$!operation" == "5")
			jQuery("#form").find("input").attr("readonly","readonly");
			jQuery("#form").find("input[type=radio]").attr("disabled","disabled");
			jQuery("#form").find("select").attr("disabled","disabled");
			jQuery("#form").find("input").removeAttr("onClick").removeAttr("ondblclick");
			jQuery("#form").find("b").removeAttr("onClick");
			jQuery("#form").find("b").removeClass("icon16");
		#end
	});
	
	
	/* 弹出框选择部门，直接上司等 */
	var showvalues = "";
	var popnames = "";
	var hidevalues = "";
	var objectPop = null;
	/* 弹出框选择*/
	function selectPopups(object,popname,hidevalue,showvalue,name){
		var urls = "/Accredit.do?popname=" + popname + "&inputType=radio";
		if(hidevalue == "directBoss"){
			urls += "&condition=noSelf";
		}
		hidevalues=hidevalue;
		showvalues=showvalue;
		asyncbox.open({id : 'Popdiv',title:name,url:urls,width:750,height:430,btnsbar:[{text:'清 &nbsp; 空',action:'clear'}].concat(jQuery.btn.OKCANCEL),
			callback : function(action,opener){
	　　　　　	if(action == 'ok'){
					var returnValue = opener.strData;
					exePopdiv(returnValue);
	　　　　　	}else if(action == 'clear'){
					jQuery("#"+hidevalues).val('');
					jQuery("#"+showvalues).val('');
				}
	　　　	}
	　	});
	}
	
	/* 返回数据 */
	function exePopdiv(returnValue){
		if(typeof(returnValue)=="undefined") return ;
		var note = returnValue.split("#;#") ;
		jQuery("#"+hidevalues).val(note[0]);
		jQuery("#"+showvalues).val(note[1]);
	}
	
	function fillData(datas){
		exePopdiv(datas);
		parent.jQuery.close('Popdiv');
	}
	
	
	/* 展开,收缩 */
	function openMore(obj){
		var status = jQuery(obj).html();
		var objDiv= jQuery("div.more-dv");
		var uHeight = objDiv.find("ul").height();
		jQuery("div.more-dv").scrollTop(0);
		if(status == "更多信息"){
			objDiv.css("overflow-y","auto");
			objDiv.css("height","142px");
			$(obj).html("收缩");
		}else{
			objDiv.removeAttr("style");
			objDiv.stop().animate({height: '100px'}, "500");
			$(obj).html("更多信息");
		}
	}
	/* 时间选择*/
	function openInputDate(obj){
		WdatePicker({lang:'zh_CN'});
	}
	
	/* 选择仓库，往来单位，收款账户 */
	function selectPops(object,popname,hidevalue,showvalue,name){
		var displayName=encodeURI(name) ;
		var urlstr = '/UserFunctionAction.do?operation=22&popupWin=Popdiv&tableName=&selectName='+popname+"&MOID="+moduleId+"&MOOP=add&LinkType=@URL:&displayName="+displayName;
		if(linkStr != ""){
			urlstr+=encodeURI(linkStr);
		}
		if(hidevalue == "Account"){
			//收款账户进行处理，查询编号100的



	
			urlstr += "&tblAccTypeInfo.AccNumber=100";
		}
		asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:750,height:470})
		showvalues = showvalue;
		objectPop = object;
		hidevalues = hidevalue;
	}

	var linkStr = "";
	/* 输入数据敲回车取数据 */
	function ajaxSelects(object,popname,hidevalue,showvalue,name){
		var url="/UtilServlet?operation=Ajax&selectName="+popname+"&MOID="+moduleId+"&MOOP=query&selectField="+showvalue+"&selectValue="+encodeURIComponent($(object).val());
		 AjaxRequest(url) ;
		 if(response == "" || response == null){
		 	selectPops(object,popname,showvalue,hidevalue,name)
		 }else if(response.indexOf("@condition:") >= 0){
			response = revertTextCode(response) ;
			var array = response.split("@condition:");
			linkStr="&keySearch="+array[1]+"&url=@URL:";
			selectPops(object,popname,showvalue,hidevalue,name)
		 }else if(response.indexOf(";") >= 0){
		 	showvalues = showvalue;
			objectPop = object;
			hidevalues = hidevalue;
			exePopdiv(response);
		 }
		 linkStr = "";
		 return false;
	}
	
	//存放隐藏值，用于回填
	var hideObj;
	var objName;
	var hideEnumerName; 
	function UpdEnum(obj,enumerName,name){
		var enumerId = obj.getAttribute("enumerId");
		var urls = "/EnumerationAction.do?operation=7&type=crm&keyId="+enumerId;
		if(obj.value == "update"){
			asyncbox.open({
				id: 'EnumWinow',
				title : "选项数据管理",
				url : urls,
				width : 620,
				height : 400,
				btnsbar : jQuery.btn.OKCANCEL,
				callback : function(action,opener){
					if(action == 'ok'){
						opener.jQuery("#crmPopFlag").val("true");
						hideObj = obj;
						hideEnumerName = enumerName;
						objName = name;
						opener.beforSubmit(opener.document.form);
					}else{
						$(obj).children().first().attr("selected","selected");
						parent.jQuery.close('EnumWinow');
					}
					return false;
				}
			});		
		}
	}
	
	/* 保存返回 */
	function updateEnumer(type){
		if(hideEnumerName != undefined && hideEnumerName!=""){
			var enumerName = hideEnumerName;
			jQuery.ajax({
			   type: "POST",
			   url: "/EmployeeDepartmentAction.do?opType=reloadEnum&enumerName="+enumerName,
			   success: function(msg){
		 	       var str = "";
				   str +=msg;
				   str +="<option value='update'>编辑选项数据</option>";
				   jQuery("#"+objName).children().first().attr("selected","selected");
				   jQuery("#"+objName).children().remove()
				   jQuery("#"+objName).append(str);
			   }
			});
			parent.jQuery.close('EnumWinow');
			hideEnumerName = "";
		}
	}
	
	function sumits(){
		var empFullName = jQuery("#EmpFullName").val();
		var empNumber = jQuery("#EmpNumber").val();
		var departmentCode = jQuery("#DepartmentCode").val();
		if(empFullName == ""){
			asyncbox.tips('职员全称不能为空！','alert');
			jQuery("#EmpFullName").focus();
			return false;
		}
		if(!containSC(empFullName)){
			asyncbox.tips('职员全称不能存在特殊字符！','alert');
			jQuery("#EmpFullName").focus();
			return false;
		}
		if(empNumber == ""){
			asyncbox.tips('职员编号不能为空！','alert');
			jQuery("#EmpNumber").focus();
			return false;
		}
		if(departmentCode == ""){
			asyncbox.tips('所属部门不能为空！','alert');
			jQuery("#DepartmentCodeName").focus();
			return false;
		}
		if(jQuery("#mailSize").val()!='' && !isInt2(jQuery("#mailSize").val())){
			if(parseInt(jQuery("#mailSize").val())!=0){
				asyncbox.tips('邮件空间只能为正整数！','alert');
				jQuery("#mailSize").focus();
				return false;
			}
		}
		
		/**
		if(jQuery("#mobile").val()!='' && !isMobile(jQuery("#mobile").val())){
			asyncbox.tips('手机号码不正确！','alert');
			jQuery("#mobile").focus();
			return false;
		}
		if(jQuery("#tel").val()!='' && !isTel(jQuery("#tel").val())){
			asyncbox.tips('座机不正确！','alert');
			jQuery("#tel").focus();
			return false;
		}
		if(jQuery("#email").val()!='' && !isMail(jQuery("#email").val())){
			asyncbox.tips('邮件格式不正确！','alert');
			jQuery("#email").focus();
			return false;
		}*/
		form.submit();
	}
	
	function uploadImage(id){
		asyncbox.open({
	　　　	id : 'deskTopId',title : '$text.get("crm.deskTop.myHeadportrait")',
			url : '/MyDesktopAction.do?operation=$globals.getOP("OP_UPLOAD_PREPARE")&type=employee&empId='+id,
			cache : false,modal:true,width:540,height:400,
			callback : function(action,opener){			    
	　　  　 }
		});
}
</script>
</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form action="/EmployeeDepartmentAction.do" method="post" name="form" id="form" target="formFrame"> 
<input type="hidden" name="operation" id="operation" value="$!operation" />
<input type="hidden" name="tableName" id="tableName" value="tblEmployee" />
#if("$!operation"=="1")
	<input type="hidden" name="opType" id="opType" value="employee"/>
#else
	<input type="hidden" name="opType" id="opType" value="updateEmployee"/>
#end
<input type="hidden" id="id" name="id" value="$!empData.id"/>
<input type="hidden" id="workFlowNodeName" name="workFlowNodeName" value="finish"/>

<!-- 用户设置详情 Start -->
<div class="operateDetail pop-layer" id="addOperate">
	<div class="topD">
    	<a class="dPhoto" href="#">
    		#if("$!empData.photo"=="")
             	<img src="/style/images/item/defaultPhoto.jpg" #if("$!operation" != "5" && "$!operation" != "1") onclick="uploadImage('$!empData.id')" #else style="cursor: default;" #end/>
            #else
             	<img src="/ReadFile.jpg?fileName=$!{empData.id}_140.jpg&amp;tempFile=false&amp;type=PIC&amp;tableName=tblEmployee" #if("$!operation" != "5") onclick="uploadImage('$!empData.id')" #else style="cursor: default;" #end/>
           	#end
    	</a>
        <ul>
        	
            #set($count = 1)
            #foreach($row in $fieldInfos)
            	#if("$row.fieldName" != "id" && "$row.fieldName" != "workFlowNodeName")
            	#if($count==9)
            		</ul>
				    </div>
				    <div class="bottomD">
				    	<div class="more-dv">
				        <ul class="bottom-ul clear">
            	#end
            	#if("$row.inputType" == "0" || "$row.inputType" == "7")
	            	#if("$row.fieldType"=="13")
	            	#elseif("$row.fieldType"=="5")
	            	<li><em #if($row.isNull==1)style="color:#f13d3d;"#end>$row.display.get("$globals.getLocale()")</em>
	            	<span class="lf h-child-inp">
		                <input type="text" id="$row.fieldName" name="$row.fieldName" value="$!empData.get($row.fieldName)" onClick="WdatePicker({lang:'$globals.getLocale()'});"/>
		                <b class="dateBtn icon16" title="选择$row.display.get("$globals.getLocale()")"></b>
		            </span>
		            </li>
	            	#elseif("$row.fieldType"=="6")
	            	<li><em #if($row.isNull==1)style="color:#f13d3d;"#end>$row.display.get("$globals.getLocale()")</em>
	            	<span class="lf h-child-inp">
		                <input type="text" id="$row.fieldName" name="$row.fieldName" value="$!empData.get($row.fieldName)" onClick="WdatePicker({lang:'$globals.getLocale()',dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
		                <b class="dateBtn icon16" title="选择$row.display.get("$globals.getLocale()")"></b>
		            </span>
		            </li>
	            	#else
	            	<li><em #if($row.isNull==1)style="color:#f13d3d;"#end>$row.display.get("$globals.getLocale()")</em>
		                <input type="text" id="$row.fieldName" name="$row.fieldName" value="$!empData.get($row.fieldName)" />
		            </li>
		            #end
		        #set($count = $count+1)
	            #elseif("$row.inputType" == "1")
	            <li>
	            	<em #if($row.isNull==1)style="color:#f13d3d;"#end>$row.display.get("$globals.getLocale()")</em>
	                <select name="$row.fieldName" id="$row.fieldName" onchange="UpdEnum(this,'$row.refEnumerationName','$row.fieldName')" enumerId='$!enumerMap.get("$row.refEnumerationName")'>
	                	#foreach($erow in $globals.getEnumerationItems($row.refEnumerationName))
							<option title="$erow.name" value="$erow.value" #if("$!empData.get($row.fieldName)"=="$erow.value") selected #end>$erow.name</option>
						#end
						#if($globals.getEnumerationItems($row.refEnumerationName).size()==0)
							<option value=""></option>
						#end
	            		<option value="update">编辑选项数据</option>
	            	</select>
	            </li>
	            #set($count = $count+1)
	            #elseif("$row.inputType" == "2") 
	            <li>
	            	#foreach($srow in $row.getSelectBean().viewFields)#set($colName="")#set($totalFields=$totalFields+1)
						#if($!srow.display!="")
							#if($!srow.display.indexOf("@TABLENAME")==0)
								#set($index=$srow.display.indexOf(".")+1)#set($tableField=$tableName+"."+$srow.display.substring($index))
							#else
								#set($tableField=$srow.display)
							#end
							#set($colName="$srow.display")
						#else
							#set($tableField="$srow.fieldName")#set($colName="$srow.fieldName")
						#end
						#set($defV = "")
						#set($vals = "")
		            	<em #if($row.isNull==1)style="color:#f13d3d;"#end> $row.display.get("$globals.getLocale()") </em>
		               	<span class="lf h-child-inp">
		            		<input type="hidden" id="$row.fieldName" name="$row.fieldName" value="$!empData.get($row.fieldName)"/>
		            		#set($defV = "$globals.getTableField($srow.asName)")
		            		#set($vals = $!empData.get($srow.asName))
		            		#if($globals.getFieldBean($srow.asName).inputType==4)
		            			#set($vals = $!empData.get("LANGUAGEQUERY").get($vals).get("$globals.getLocale()"))
		            		#end
		            		<input type="text" name="$defV" id="$defV" value='$!vals' ondblclick="selectPops(this,'$row.inputValue','$row.fieldName','$defV','选择$row.display.get("$globals.getLocale()")')" onKeyDown="if(event.keyCode==13){if(this.value.length>0){ajaxSelects(this,'$row.inputValue','$row.fieldName','$defV','选择$row.display.get("$globals.getLocale()")');event.keyCode=9;}else{selectPops(this,'$row.inputValue','$row.fieldName','$defV','选择$row.display.get("$globals.getLocale()")');}}"/>
		            		<b class="stBtn icon16" title="选择$row.display.get("$globals.getLocale()")" onclick="selectPops(this,'$row.inputValue','$row.fieldName','$defV','选择$row.display.get("$globals.getLocale()")')"></b>
		            	</span>
					#end
	            </li>
	            #set($count = $count+1)
	            #else
	            	<input type="hidden" name="$row.fieldName" id="$row.fieldName" value="$!empData.get($row.fieldName)" />
	            #end
	            #end
            #end
        </ul>
        </div>
    </div>
</div>
<!-- 用户设置详情 Start -->
</form>
</body>
</html>
	<script type="text/javascript">  
		var oDiv2=document.getElementById("addOperate"); 
		var sHeight2=document.documentElement.clientHeight-50;
		oDiv2.style.height=sHeight2+"px";
		
		
	</script>

