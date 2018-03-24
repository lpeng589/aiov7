<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("roleadmin.title.update")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css" />
<link type="text/css" rel="stylesheet" href="/style1/css/tab.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/validate.vjs"></script>
<script type="text/javascript" src="$globals.js("/js/editPage.vjs","",$text)"></script>
<script type="text/javascript">
#if("$!fromUser" == "")
putValidateItem("roleName","$text.get("role.lb.roleName")","any","",false,0,50);
putValidateItem("roleDesc","$text.get("role.lb.roleDesc")","any","",true,1,500 );
#end

function beforSubmit(form){
	//获取iframe中的值




	window.save=true;
	var moduleOperations="";
	
	var pindexs=document.getElementsByTagName("iframe");
	for(var i=0;i<pindexs.length;i++){
		var val=pindexs[i].getAttribute("value");
		var pindexNum=pindexs[i].getAttribute("name");
		var thepage = window.frames[pindexNum].document.myform;

		if(typeof(val)!="undefined" && typeof(thepage)!="undefined"){
			var obj=thepage.moduleOperations;
			if(obj!=null && typeof(obj)!="undefined"){ //可能下面一个权限都没有
				for(var k=0;k<obj.length;k++){		
					if(obj[k].checked){	
						moduleOperations+=obj[k].value+",";
					}	
				}
			}
		}
	}
	
	myform.moduleOperations.value=moduleOperations;
	
	if(!validate(form))return false;
	disableForm(form);
	return true;
}	

function confirmLeave(){
	if(confirm("$text.get("common.msg.IsSaveOperate")")){
		return true;
	}
	return false ;
}

function changeDefSys(moduleId,selfObj){
	var pindexs=document.getElementsByTagName("iframe");
	selectTag(selfObj);
		var pindex="#pindex_"+moduleId;

	if($(pindex).css("display") == "none"){
		for(var i=1;i<pindexs.length;i++){
			var val=pindexs[i].getAttribute("value");
			if(typeof(val)!="undefined" ){
				var pin="#"+pindexs[i].getAttribute("id");
				if( pin == pindex )
					$(pin).show();
				else
					$(pin).hide();	
			}	
		}
		
	}
}

function selectTag(selfObj){
	// 标签
	var tag = document.getElementById("tags").getElementsByTagName("li");
	var taglength = tag.length;
	for(i=0; i<taglength; i++){
		tag[i].className = "";
	}
	selfObj.parentNode.className = "selectTag";
}
function selectAllModule(obj){	
	var ck = obj.checked;	
	var pindexs=document.getElementsByTagName("iframe");
	for(var i=0;i<pindexs.length;i++){
		var val=pindexs[i].getAttribute("value");
		var pindexNum=pindexs[i].getAttribute("name");
		var thepage = window.frames[pindexNum].document.myform; 
		if(typeof(val)!="undefined" && typeof(thepage)!="undefined"){
			var obj=thepage.elements;
			if(obj!=null && typeof(obj)!="undefined" ){ //可能下面一个权限都没有
				for(var k=0;k<obj.length;k++){	
					if(obj[k].type=="checkbox"){
						obj[k].checked = ck;
					}
				}
			}						
		}
	}
}
</script>
<style type="text/css">
.HeadingTitle>em{font-weight:bold;color:#666;float:left;display:inline-block;}

.d-sub-wp{margin-left:20px;font-weight:normal;}
.d-sub-wp>li{float:left;display:inline-block;}
.only-name{margin-left:20px;}
.only-name>input{border:0;background:none;font-weight:bold;font-family:microsoft yahei;font-size:15px;color:#3876ac;height:25px;line-height:25px;}
.l-cbox{overflow:hidden;}
.l-cbox>.cbox{float:left;width:15px;margin:4px 2px 0 0;}
.l-cbox>i{float:left;display:inline-block;line-height:20px;}
#ERPSys{overflow:hidden;padding:15px;}
#ERPSys .l-cbox{float:left;display:inline-block;margin-right:10px;}
</style>
	</head>

	<body onLoad="showStatus();">
		<iframe name="formFrame" style="display:none"></iframe>
		<form method="post" scope="request" name="myform" action="/RoleAction.do" onSubmit="return beforSubmit(this);"target="formFrame">
			<input type="hidden" name="operation" value="$!operation" />
			<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/>
			<input type="hidden" name="id" value="$!roleId"/>
			<input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
			<input type="hidden" name="copy" value="$!copy"/>
			<input type="hidden" name="sourceRoleId" value="$!roleId"/>
			<input type="hidden" name="moduleOperations" value=""/>
			<input type="hidden" name="fromUser" value="$!fromUser"/>
			<input type="hidden" name="updatePage" value="$!updatePage"/>
			<div class="Heading">
				<div class="HeadingTitle" style="padding:0;">
					<em class="lf">$text.get("roleadmin.title.update")</em>
					
					#if("$!fromUser" == "")
						<div class="lf d-sub-wp">
							<li>
								<span>$text.get("role.lb.roleName")：</span>
								<input name="roleName" style="border:0px; border-bottom:1px solid #ccc;font-family:microsoft yahei;" type="text" value="$!result.roleName" maxlength="50">
							</li>
							<li>
								<span>$text.get("role.lb.roleDesc")：</span>
								<input name="roleDesc" type="text" style="border:0px; border-bottom:1px solid #ccc; width: 300px;font-family:microsoft yahei;" value="$!result.roleDesc" maxlength="50">
								<font style="margin-left:10px;" color="gray">(最多输入50个字)</font>
							</li>
						</div>
					#else
						<div class="lf only-name">
							<input name="roleName" readOnly type="text" value="$!result.empFullName" maxlength="50" />
						</div>	
					#end
				</div>
				<ul class="HeadingButton">
					#if($LoginBean.operationMap.get("/UserQueryAction.do").add())
					<li class="listRange_msg_button">
						<button type="submit" class="hBtns" >
							$text.get("common.lb.save")
						</button>
					</li>
					#end
					<li>
						#if("$!fromUser" == "")
						<span onClick="location.href='/RoleQueryAction.do?operation=$globals.getOP("OP_QUERY")&winCurIndex=$!winCurIndex '" class="hBtns">
							$text.get("common.lb.back")
						</span>
						#else
							#if("$!updatePage" == "true")
								<span onClick="parent.dealAsyn('close')" class="hBtns">
							$text.get("common.lb.close")
								</span>
							#else
								<span onClick="location.href='/UserQueryAction.do?winCurIndex=$!winCurIndex '" class="hBtns">
								$text.get("common.lb.back")
								</span>	
							#end	
						#end
					</li>
				</ul>
			</div>
		#if($globals.hasMainModule('1'))
			<div id="ERPSys">
				<label class="l-cbox" title='$text.get("role.lb.hiddenPrice")'>
					<input type="checkBox" class="cbox" value="1" #if($!result.hiddenField.indexOf("1")>=0)checked #end name="hidField" />
					<i>$text.get("role.lb.hiddenPrice")</i>
				</label>
				<label class="l-cbox" title='$text.get("role.lb.hiddenSalePrice")'>
					<input type="checkBox" class="cbox" value="2" #if($!result.hiddenField.indexOf("2")>=0)checked #end name="hidField" />
					<i>$text.get("role.lb.hiddenSalePrice")</i>	
				</label>
				<label class="l-cbox" title='$text.get("role.lb.hiddenCustomer")'>
					<input type="checkBox" class="cbox" value="3" #if($!result.hiddenField.indexOf("3")>=0)checked #end name="hidField" /i>  
					<i>$text.get("role.lb.hiddenCustomer")</i>	
				</label>
				<label class="l-cbox" title='$text.get("role.lb.hiddenSupplier")'>
					<input type="checkBox" class="cbox" value="4" #if($!result.hiddenField.indexOf("4")>=0)checked #end name="hidField" /> 
					<i>$text.get("role.lb.hiddenSupplier")</i>
				</label>
				<label class="l-cbox" title='$text.get("role.lb.hiddenCompany")'>
					<input type="checkBox" class="cbox" value="5" #if($!result.hiddenField.indexOf("5")>=0)checked #end name="hidField" /> 
					<i>$text.get("role.lb.hiddenCompany")</i>
				</label>
				#if($globals.getSysSetting("OrderAllowNegative")=="true")
				<label class="l-cbox" title='$text.get("allow.little.stocks")'>			
					<input type="checkBox" class="cbox" value="0" #if($rightType.get(1)== "1") checked #end name="hidField1" />
					<i>$text.get("allow.little.stocks")	</i>	
				</label>
				#end 
				#if($globals.getSysSetting("OverSalesorder")=="true")
				<label class="l-cbox" title='$text.get("allow.more.order.out")'>	
					<input type="checkBox"class="cbox" value="1" #if($rightType.get(2)== "1") checked #end name="hidField1" />
					<i>$text.get("allow.more.order.out")</i>	
			   	</label>
			    #end 
			    #if($globals.getSysSetting("OverBuyorder")=="true")	
			    <label class="l-cbox" title='$text.get("allow.more.order.in")'>		
					<input type="checkBox" class="cbox" value="2" #if($rightType.get(3)== "1") checked #end name="hidField1" />
					<i>$text.get("allow.more.order.in")</i>		
				</label>	
				#end 
				#if($globals.getSysSetting("UnderCostprice")=="true")
				<label class="l-cbox" title='$text.get("allow.little.cost.out")'>
					<input type="checkBox" class="cbox" value="3" #if($rightType.get(4)== "1") checked #end name="hidField1"/>
					<i>$text.get("allow.little.cost.out")</i>
				</label>
			    #end  
			    #if($globals.getSysSetting("SalesAllowNegative")=="true")
			    <label class="l-cbox" title='$text.get("allow.more.cost.out")'>
					<input type="checkBox" class="cbox" value="4" #if($rightType.get(5)== "1") checked #end name="hidField1"/>
					<i>$text.get("allow.more.cost.out")</i>	
				</label>
				#end 
				#if($globals.getSysSetting("allowCustomerCreditLimit")=="true")
				<label class="l-cbox" title='$text.get("allow.cstomer.credit.limit")'>
					<input type="checkBox" class="cbox" value="5" #if($rightType.get(6)== "1") checked #end name="hidField1"/>
					<i>$text.get("allow.cstomer.credit.limit")</i>	
				</label>		
				#end 
				#if($globals.getSysSetting("UnderLimitprice")=="true")
				<label class="l-cbox" title='$text.get("allow.little.sale.price")'>
					<input type="checkBox" class="cbox" value="6" #if($rightType.get(7)== "1") checked #end name="hidField1"/>
					<i>$text.get("allow.little.sale.price")</i>
				</label>
				#end  
				#if($globals.getSysSetting("WithOutSettleCys")=="true") 
				<label class="l-cbox" title='$text.get("allow.OutSettleCys")'>
					<input type="checkBox" class="cbox" value="7" #if($rightType.get(8)== "1") checked #end name="hidField1"/>
					<i>$text.get("allow.OutSettleCys")</i>
				</label>
				#end
				
			</div>
		#end

			<div id="con">
				<input id="selectAll" name="selectAll" type="checkbox" value="" mall=mall style="border:0px;width:16px;" onClick="selectAllModule(this)"/>
				$text.get("role.lb.allRight")
				<ul id="tags">
					 #set($num=0)
					 #foreach($module in $allModule) 
						<li  #if($num == 0) class="selectTag" #end>
							<a onClick="changeDefSys('$module.getId().trim()',this);" style="cursor: pointer;">
							$module.modelDisplay.get($globals.getLocale())</a>
						</li>
						#set($num=$num+1)
					 #end
				</ul>
				<div id="tagContent">
				
			 	#set($num=0)
				#foreach($module in $allModule)
					<iframe id="pindex_$module.getId().trim()" name="pindex_$module.getId().trim()" value="$module.getId().trim()"   
					    #if($num==0) style="display: block;" #else style="display: none" #end 
						    src="/RoleAction.do?operation=$globals.getOP('OP_MODULE_RIGHT_PREPARE')&keyId=$roleId&moduleId=$module.getId().trim()" 	
						width="100%"  height="370px" frameborder=no scrolling="no"></iframe>			
					#set($num=$num+1)
				 #end	
				</div>
			</div>
	</body>
</html>