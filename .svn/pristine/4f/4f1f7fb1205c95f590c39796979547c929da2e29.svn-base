<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>$text.get("iniSet.lb.title")</title>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/classFunction.css"/>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<style type="text/css">
.listRange_1{overflow:hidden;}
.title_div{margin: 10px 10px 10px 20px;}
.listRange_list_function tbody tr{height:30px;}
.listRange_pagebar{float: right;margin-right: 40px;}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/public_utils.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/k_listgrid.js"></script>
<script type="text/javascript" src="/js/dropdownselect.js"></script>
<script type="text/javascript">
	var hideObj;
	var hideField;
	function openDeptSelect(){
		var urls = "/Accredit.do?popname=deptGroup&inputType=radio";
		var titles = "请选择部门";
		asyncbox.open({
			id:'Popdiv',
			title:titles,
			url:urls,
			width:750,
			height:470,
			btnsbar :[{text:'清空',action:'remove'}].concat(jQuery.btn.OKCANCEL),
			callback : function(action,opener){
		　　　　　	if(action == 'ok'){
					var depts = opener.strData;
					popDeptData(depts);
		　　　　　	}
				if(action == 'remove'){
					$("#department").val('');
					$("#deptName").val('');
				}
			}
		});
	}
	
	function popDeptData(depts){
		if(depts == ""){
			return;
		}
		var dept = depts.split(";");
		$("#department").val(dept[0]);
		$("#deptName").val(dept[1]);
	}
	
	function fillData(datas){	
		popDeptData(datas);
		jQuery.close('Popdiv');
	}
	
	//表单提交
	function onsubmits(){
		form.submit();
	}
	
	//连接
	function showNext(accCode,accNumber,type,department){
		window.location.href="/IniAccQueryAction.do?accCode="+accCode+"&accNumber="+accNumber+"&type="+type+"&winCurIndex=$!winCurIndex&department="+department;
	}
	


	
	//修改期初,期初详情
	function operationIni(accCode,keyid,accNumber,isCalculate){
		if(isCalculate == "1"){
			alert("此科目是核算类科目，不允许在此做修改操作，修改上级科目时会影响此科目！");
			return false;
		}
		
		heights = document.documentElement.clientHeight-0;
			
		var title="期初设置";
		var urls = "/IniAccQueryAction.do?accCode="+accCode+"&accNumber="+accNumber+"&keyid="+keyid;
		#if($!NowPeriod==-1)
			#if($globals.getMOperation().update() || $loginBean.id=="1")
				urls += "&operation=7";
			#else
				urls += "&operation=5";
			#end
		#else
			urls += "&operation=5";
		#end
		
		openPop('IniDiv',title,urls,$(".Heading").width()-120,heights,true,heights==document.documentElement.clientHeight); 
		
	}
	
	#if($first)
	//试算平衡
	function account(){
	  	var ini=$ini;
	  	var debit=$debit;
	  	if(!ini){
	  		asyncbox.tips("$text.get("acc.account.ini")",'alert',1500);
	  		return false;
	  	}
	  	if(!debit){
	  		asyncbox.tips("$text.get("acc.account.debit")",'alert',1500);
	  		return false;
	  	}
	  	asyncbox.tips("$text.get("acc.account.level")",'alert',1500);
	}
	 
	//自动调平 
  	function autoAdjust(){
	  	var ini=$ini;
	  	var debit=$debit;
    	#if($globals.getMOperation().query())
			if(!ini||!debit){  	
		  		form.action="/IniAccQueryAction.do?autoAdjust=true&&winCurIndex=$!winCurIndex" ;
		  		form.submit() ;
		  	}else{
		  		asyncbox.tips("$text.get("acc.account.level")",'alert',1500);
		  	}
	  	#end
  	}
	#end
</script>
</head>
 
<body>
<form method="post" scope="request" id="form" name="form" action="/IniAccQueryAction.do">
<input type="hidden" id="operation" name="operation" value="4"/>
<input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="$!globals.getToken()"/> 
<input type="hidden" name="accCode" value="$!accCode"/>
<input type="hidden" name="winCurIndex" value="$!winCurIndex"/>
		<div class="Heading">
			<div class="HeadingTitle" style="padding:0;">
				$text.get("iniAcc.lb.title")
			</div>
			<ul class="HeadingButton">
				<li>
					<span type="submit" onClick="onsubmits();" class="hBtns">$text.get("common.lb.query")</span>
					#if($globals.getMOperation().update())
						#if($globals.getSysSetting("openDeptAcc")=="false" || ($globals.getSysSetting("openDeptAcc")=="true" && $!department.length()>0))
						<!-- <input type="button" class="bu_02" #if($!NowPeriod!=-1)disabled="true"#end onClick="return editPre();" value="$text.get("common.lb.edit")" /> -->
						#end
					#end
					#if($globals.getMOperation().query() && "$!accCode"!="")
						<span type="button" onClick="showNext('$!accCode','','back','$!department');" class="hBtns">$text.get("common.lb.back")</span>
					#end
					#if($first)
						#if($!NowPeriod==-1)
						<span type="button" onClick="account()"  class="hBtns">$text.get("acc.lb.account")</span>
						#if($globals.getSysSetting("openDeptAcc")=="false"||($globals.getSysSetting("openDeptAcc")=="true"&&$!department.length()>0))
						<span type="button" onClick="autoAdjust()" class="hBtns">$text.get("acc.lb.adjust")</span>
						#end
						#if(!$!department||$!department.length()==0)
						<span type="button" onClick="location.href='/SysAccAction.do?type=beginAcc'" class="hBtns">$text.get("sysAcc.lb.openAcc")</span>
						#end
						#end
					#end
					#if($LoginBean.operationMap.get("/ReportDataAction.do?reportNumber=IniReportAcc").query() || $!LoginBean.id=="1")
					<span type="button" onclick="javascript:mdiwin('/ReportDataAction.do?reportNumber=IniReportAcc','财务期初报表');" class="hBtns">财务期初报表</span>
					#end
				</li>
			</ul>
		</div>
		<div class="listRange_1" id="listid">
			<ul>
				#if($globals.getSysSetting("openDeptAcc")=="true")
				<li>
					<span>$text.get("oa.common.department")</span>
					<input type="hidden" name="department" id="department" value="$!department" />
					<div class="swa_c2">
						<input name="deptName" id="deptName" value="$!deptName" onDblClick="openDeptSelect('SelectFirstLevelDeptName','$text.get('mrp.lb.department')','deptName','department');"/>
						<b class="stBtn icon16" onClick="openDeptSelect('SelectFirstLevelDeptName','$text.get('mrp.lb.department')','deptName','department');"></b>
					</div>
				</li>
				#end
				<li>
					<span>$text.get("iniAcc.lb.accNumber")</span>
					<input name="accNumber" value="$!accNumber" id="accNumber" onkeydown="if(event.keyCode==13)onsubmits();" />
				</li>
				<li>
					<span>$text.get("common.lb.dimQuery")</span>
					<input name="keyword" id="keyword" type="text" value="$!keyword" onkeydown="if(event.keyCode==13)onsubmits();" />
				</li>
			</ul>
		</div>
		<div class="title_div">
			#if($globals.getMOperation().query())
				#if($parentName.length() > 0)<span>$text.get("common.msg.currstation")：$!parentName</span>#end
			#end
		</div>
		<div class="scroll_function_small_a" id="list_id">
			<script type="text/javascript">
			var oDiv=document.getElementById("list_id");
			var dHeight=document.documentElement.clientHeight;
			var sHeight=document.getElementById("listid").clientHeight;
			oDiv.style.height=dHeight-sHeight-100+"px";;
			</script>
			<table width="600" border="0" cellpadding="0" cellspacing="0"
				class="listRange_list_function" id="tblSort" name="table">
				<thead>
					<tr class="scrollColThead">
						<td width="45">No.</td>
						<td width="100">$text.get("iniAcc.lb.accNumber")</td>
						<td width="200">$text.get("iniAcc.lb.accName")</td>
						<td width="120">$globals.getFieldDisplay("tblIniAccDet.BeginAmount")</td>
						<td width="120">$globals.getFieldDisplay("tblIniAccDet.TotalDebit")</td>
						<td width="120">$globals.getFieldDisplay("tblIniAccDet.TotalLend")</td>
						<td width="120">$globals.getFieldDisplay("tblIniAccDet.TotalRemain")</td>
						
						#if($globals.getSysSetting("openDeptAcc")=="false"||($globals.getSysSetting("openDeptAcc")=="true"&&$!department.length()>0))
						<td width="80">
							$text.get("common.lb.print.Operation")
						</td>
						#end
						<td width="80">$text.get("common.lb.showNext")</td>
					</tr>
				</thead>
				<tbody>
					#foreach ($row in $result)
						 #if( $!globals.encodeHTMLLine($globals.get($row,2)).indexOf("6")!=0)
							<!--$globals.get($row,3)-->
							#if("$globals.get($row,3)" == "$text.get('com.acc.asset')")
								#set ($asset="$!globals.get($row,7)")
								#set ($iniAsset="$!globals.get($row,4)")
								#set ($debitAsset="$!globals.get($row,5)")
								#set ($creditAsset="$!globals.get($row,6)")
							#end
							#if("$globals.get($row,3)" == "$text.get('com.acc.liability')")
								#set ($liability="$!globals.get($row,7)")
								#set ($iniLiability="$!globals.get($row,4)")
								#set ($debitLiability="$!globals.get($row,5)")
								#set ($creditLiability="$!globals.get($row,6)")
							#end
							#if("$globals.get($row,3)" == "$text.get('com.acc.ownership')")
								#set ($ownership="$!globals.get($row,7)")
								#set ($iniOwnership="$!globals.get($row,4)")
								#set ($debitOwnership="$!globals.get($row,5)")
								#set ($creditOwnership="$!globals.get($row,6)")								
							#end
							#if("$globals.get($row,3)" == "$text.get('iniAcc.lb.Cost')")
								#set ($Cost="$!globals.get($row,7)")
								#set ($iniCost="$!globals.get($row,4)")
								#set ($debitCost="$!globals.get($row,5)")
								#set ($creditCost="$!globals.get($row,6)")
							#end
							#if("$globals.get($row,3)" == "$text.get('iniAcc.lb.InCommon')")
								#set ($InCommon="$!globals.get($row,7)")
							#end	
						#set($isPrepay = "false")
						#if($globals.getSysSetting("prerecvpaymarktoneedrecvpay")=="true" && ("$globals.get($row,2)"=="1123" || "$globals.get($row,2)"=="2203")) 
							#set($isPrepay = "true")
						#end					
					<tr 
						#if(($globals.getSysSetting("openDeptAcc") == "false" || ($globals.getSysSetting("openDeptAcc")=="true" && $!department.length()>0)) && $!isPrepay=="false")
							#if($globals.get($row,10)>0)
								 onDblClick="showNext('$globals.get($row,1)','$globals.get($row,2)','next','$!department')"
							#else
								 onDblClick="operationIni('$globals.get($row,1)','$globals.get($row,0)','$globals.get($row,2)','$globals.get($row,13)')"
							#end
						#end 
						#if($!isPrepay=="true")
							style="color:#aaa"
						#end
						onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);">
						<!-- <td align ="center" width="45" ><input type="checkbox" name="keyId" value="$globals.get($row,0)"></td> -->
						<td align ="center" width="45">$velocityCount</td>
						<td align="left" width="100" style="cursor: default;">$!globals.encodeHTMLLine($globals.get($row,2)) &nbsp;</td>
						<td align="left" width="200">
						#if($globals.getSysSetting("openDeptAcc")=="false"||($globals.getSysSetting("openDeptAcc")=="true"&&($!department.length()>0 ||$globals.get($row,10)>0)))
							<a href='javascript:void(0)' #if($!isPrepay=="false") #if($globals.get($row,10)>0) onclick="showNext('$globals.get($row,1)','$globals.get($row,2)','next','$!department');" #else onclick="operationIni('$globals.get($row,1)','$globals.get($row,0)','$globals.get($row,2)','$globals.get($row,13)')" #end #else style="color:#aaa;cursor: default;" #end>
							$!globals.encodeHTMLLine($globals.get($row,3)) &nbsp; #if($!isPrepay!="false") <img src="/style1/images/carefor/not.gif" style="cursor: pointer;" title="启用系统配置(预收预付记应收应付)不可进行修改预付预收！"/> #end</a>
						#else
							$!globals.encodeHTMLLine($globals.get($row,3))
						#end
						</td>
						<td width="120" class="right_td">$globals.get($row,4) &nbsp;</td>
						<td width="150" class="right_td">$globals.get($row,5) &nbsp;</td>
						<td width="150" class="right_td">$globals.get($row,6) &nbsp;</td>
						<td width="150" class="right_td">$globals.get($row,7) &nbsp;<input name="balance" type="hidden" value="$globals.get($row,7)" /><input name="curBalance" type="hidden" value="$globals.get($row,8)" /></td>	
														
						#if($globals.getSysSetting("openDeptAcc")=="false"||($globals.getSysSetting("openDeptAcc")=="true"&&$!department.length()>0))
							<td width="80" class="center_td">
							#if($!NowPeriod==-1 && ($globals.getMOperation().update() || $loginBean.id=="1"))	
								<a href="javascript:void(0)" #if($isPrepay == "true") style="color:#aaa;cursor: default;" #else  #if($globals.get($row,10)>0 && $globals.get($row,12)!=1) style="color:#C0C0C0;cursor:default;" onclick="showNext('$globals.get($row,1)','$globals.get($row,2)','next','$!department');" #else onclick="operationIni('$globals.get($row,1)','$globals.get($row,0)','$globals.get($row,2)','$globals.get($row,13)')" #end #end >$text.get("common.lb.update")</a>
							#else
								<a href="javascript:void(0)" #if($globals.get($row,10)>0 && $globals.get($row,12)!=1) onclick="showNext('$globals.get($row,1)','$globals.get($row,2)','next','$!department');" #else onclick="operationIni('$globals.get($row,1)','$globals.get($row,0)','$globals.get($row,2)','$globals.get($row,13)')" #end style="color:blue;">$text.get("common.lb.detail")</a>
							#end
							</td>
						#end
						<td width="80" class="center_td">
						#if($globals.getMOperation().query())
							#if($globals.get($row,10) > 0)
							<a href='javascript:void(0)' onclick="showNext('$globals.get($row,1)','$globals.get($row,2)','next','$!department');" style="color:blue;">$text.get("common.lb.showNext") &nbsp;</a>
							#else
							$text.get("common.lb.noChild")
							#end
						#else &nbsp; #end
						<input type="hidden" name="itemJdFlag" value="$globals.get($row,11)"/>
						</td>
					</tr>
					#end
					#end
					#if($!accCode.length()>=5)
					<tr>
						<td class="center_td" colspan="3"><strong>$text.get("common.lb.total")</strong></td>
						<td class="right_td">&nbsp; </td>
						<td class="right_td">&nbsp; </td>
						<td class="right_td">&nbsp; </td>
						<td class="right_td" id="balanceTotal">&nbsp; </td>
						
						#if($globals.getSysSetting("openDeptAcc")=="false"||($globals.getSysSetting("openDeptAcc")=="true"&&$!department.length()>0))
							<td class="right_td">&nbsp;</td>
						#end
						<td class="right_td">&nbsp;</td>
					</tr>
					<script language="javascript" type="text/javascript">
						//生成合计数




						var balances = document.getElementsByName("balance");
						var jdFlags=document.getElementsByName("itemJdFlag");
						var balanceTotal = 0;
						var balanceTotal_1=0;
						var balanceTotal_2=0;
						for(var i=0;i<balances.length;i++){
						    if(jdFlags[i].value=="1"){
							  balanceTotal_1+=parseFloat(balances[i].value.replaceAll(",",""));
							}else{
							  balanceTotal_2+=parseFloat(balances[i].value.replaceAll(",",""))-balanceTotal;
							}
						}
						#if("$parentJd"=="1")
						   balanceTotal=balanceTotal_1-balanceTotal_2;
						#elseif("$parentJd"=="2")
						   balanceTotal=balanceTotal_2-balanceTotal_1;
						#elseif("$parentJd"=="")
							balanceTotal=Math.abs(balanceTotal_1-balanceTotal_2);
						#end
						
						var balanceTotalO = document.getElementById("balanceTotal") ;
						if(balanceTotalO!=null){
							balanceTotalO.innerHTML="<strong>"+Number(balanceTotal).toFixed($globals.getSysSetting("DigitsAmount"))+"</strong>";
						}
					</script>
					#end
				</tbody>
			</table>
		</div>
		<div class="listRange_pagebar" style="position:relative">$!pageBar </div>
		</form>
	</body>
</html>