<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="/style/css/WorkingPlan.css" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"/>
<script language="javascript" charset="utf-8" src="/js/kindeditor-min.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="$globals.js("/js/fileUpload.vjs","",$text)"></script>
<script language="javascript" src="/js/validate.vjs"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script language="javascript" src="$globals.js("/js/workPlan.vjs","",$text)"></script>
<script language="javascript">
putValidateItem("title","$text.get("oa.subjects")","any","",false,0,200);
putValidateItem("beginDate","$text.get("oa.mail.time")","datetime","",false,0,19 );
putValidateItem("endDate","$text.get("oa.mail.time")","datetime","",false,0,19 );
var varLoginId;
var varPlanId;
var varId;
var varIsEmployee;
var varLinkaddress;
var varObj;
var varPlanType ;
var varDisplayName ;
function isNull(variable,message){
	if(variable=="" || variable==null){
		alert(message+" "+"$text.get('common.validate.error.null')");
		return false;
	}else{
		return true;
	}
}

function hello(){alert(3333333333);}

function beforSubmit(form){
	#if($planType=="day")
		bh = document.form.beginDateHour.value;	
		bm = document.form.beginDateMin.value;
		eh = document.form.endDateHour.value;
		em = document.form.endDateMin.value;
		
		document.form.beginDate.value='$strDate '+bh+":"+bm+":00";
		document.form.endDate.value='$strDate '+eh+":"+em+":00";
	#end
 
 	if(document.form.beginDate.value >=document.form.endDate.value){
 		alert("$text.get("scope.time.timeCompare")");
 		return false;
 	}
	var content= editor.html();
  	document.form.content.value = content ;
	if(!isNull(content,'$text.get("common.lb.content")'))	{
		return false;
	}
	var title = document.getElementById("title");
	if(typeof(top.jblockUI)!="undefined"){
		top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
	}
	if(!titleValid(title))return false;
	//if(!validate(form))return false;
	disableForm(form);
	return true;
}	

var editor  ;
KindEditor.ready(function(K) {
	editor = K.create('textarea[name="content"]',{
		uploadJson:'/UtilServlet?operation=uploadFile'
	});
});


function titleValid(title){
   var str = "\[,\`,\~,\!,\@,\#,\$,\^,\&,\*,\(,\),\=,\|,\{,\},\',\:,\;,\',\,,\\,\[,\],\.,\<,\>,\/,\?,\~,\！,\@,\#,\￥,\…,\…,\&,\*,\（,\）,\&,\;,\—,\|,\{,\},\【,\】,\‘,\；,\：,\”,\“,\',\。,\，,\、,\？,]";
   var iu, iuu; 
   var regArray = str.split(',');
   iuu=regArray.length;
   for(iu=1;iu<=iuu;iu++){   
      if(regArray[iu] !="") {
          if (title.value.indexOf(regArray[iu])!=-1){
             alert("主题不可以包含：" + regArray[iu]);
             title.focus();
             return false;
          }
      }
   }
   return true;              
}


function showOrHide(strId){
	var str = jQuery("#"+strId)
	if(str.css("display") == "block"){
		str.hide();
	}else{
		str.show();
	}
}


/*
	新客户弹出框
	popName:弹出框类型userGroup:职员
*/
function openClientPop(){
	var url ="/Accredit.do?inputType=checkbox&popname=CrmClickGroup";
	asyncbox.open({
		id :'clientPopId',url:url,title:'选择客户',width:755,height:450,
		btnsbar :jQuery.btn.OKCANCEL,
	    callback : function(action,opener){
	    	if(action == 'ok'){
	    		var str = opener.strData;
	    		var strArr = str.split("|");
	    		var retInfo = "";
	    		for(var i=0;i<strArr.length;i++){
	    			if("" != strArr[i].split(";")[0]){
		    			varLoginId = strArr[i].split(";")[1];
						varPlanId = '';
						varId = '5';
						varIsEmployee = '2';
						varLinkaddress = '/CRMClientAction.do?operation=5&type=detailNew&keyId=assID';
						varObj = 'ass$!{result.id}5';
						varDisplayName = '关联客户';
						exePopdiv(strArr[i]);
	    			}
	    		}
	    	}
	    }
　  });
}

/*双击回填字段*/
function fillData(datas){
	varLoginId = datas.split(";")[1];
	varPlanId = '';
	varId = '5';
	varIsEmployee = '2';
	varLinkaddress = '/CRMClientAction.do?operation=5&type=detailNew&keyId=assID';
	varObj = 'ass$!{result.id}5';
	varDisplayName = '关联客户';
	var strArr = datas.split(";")[0]+";"+datas.split(";")[1];
	exePopdiv(strArr);
	
	jQuery.close('clientPopId');
}

</script>

</head>
<body>
<iframe name="formFrame" style="display:none"></iframe>
<form name="form" method="post"  action="/OAWorkPlanAction.do"  onSubmit="return beforSubmit(this);" target="formFrame">
	#if($!result.id != "")
	<input type="hidden" name="operation" value="$globals.getOP("OP_UPDATE")" />
	#else
	<input type="hidden" name="operation" value="$globals.getOP("OP_ADD")" />
	#end
	<input type="hidden" name="noback" value="$!noback" />
	<input type="hidden" name="planType" value="$!planType" />
	<input type="hidden" name="id" value="$!result.id"/>
	<input type="hidden" name="keyId" value="$!keyId"/>
	<input type="hidden" name="strDate" value='$!strDate'/>
	<input type="hidden" name="employeeID" value='$!result.employeeID'/>
	<input type="hidden" name="departmentCode" value='$!result.departmentCode'/>
	<input type="hidden" name="winCurIndex" value='$!winCurIndex'/>
	<input type="hidden" name="calendar" value='$!calendar'/>
	<input type="hidden" name="copy" value='$!copy'/>
	<input type="hidden" name="score" value='$!score'/>
	<input type="hidden" name="planStatus" value='$!planStatus'/>
	<input type="hidden" name="attachFiles" value="$!result.attach"/>
	<input type="hidden" name="delFiles" value=""/>
	
	<div>
			
	
<div style="margin:5px;overflow-x:hidden;overflow-y:auto;"  id="listRange_id">
		<script type="text/javascript">
			var oDiv=document.getElementById("listRange_id");			
			var sHeight=document.documentElement.clientHeight-10;
			oDiv.style.height=sHeight+"px";
		</script>
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<td colspan="2" class="dateShow" height="40px;">
			<font style="font-size: 12px;"><b>
			&nbsp;
			#if("$!planType"=="event")
			$text.get("crm.event.plan")
			#else
			$text.get("oa.mydesk.workPlan")
			#end
			>> #if("$!result.id" == "") $text.get("common.lb.add")#else $text.get("common.lb.update") #end
			</b></font>
				<span style="float:right;">
						<input type="submit" class="bu_02"  value="$text.get("common.lb.save")" />
						#if($!result.planType=="event") 
							#if("$!result.id" == "")
							<input type="button" class="bu_02"  value="$text.get("common.lb.back")" onclick="window.location.href='/OAWorkPlanQueryAction.do?operation=4&opType=eventWorkList&winCurIndex=$!winCurIndex';"/>
							#else
							<input type="button" class="bu_02" value="$text.get("common.lb.back")" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&planType=$!result.planType&keyId=$!result.id&winCurIndex=$!winCurIndex';"/>
							#end
						#elseif("$copy"=="true") 
						<input type="button" class="bu_02" value="$text.get("common.lb.close")" onclick="window.close()"/>
						#else 
						<input type="button" class="bu_02" value="$text.get("common.lb.back")" onclick="window.location.href='/OAWorkPlanAction.do?operation=4&planType=$planType&userId=$result.employeeID&strDate=$strDate&score=$!score&planStatus=$!planStatus&winCurIndex=$!winCurIndex#if("$calendar"=="true")&opType=calendar#end';"/>
						#end
				</span>
			</td>
		</tr>
		<tr>
			<td class="WP_addC">
				#if($planType=="day")
				<input type="hidden"  name="beginDate" value="$!result.beginDate"/>
				<input type="hidden"  name="endDate" value="$!result.endDate"/>
				<div class="WP_title">
					<span><span style="width:70px;text-align:right">$text.get("oa.subjects")</span>
					<input type="text" name="title" id="title" style="width:280px" value="$!result.title"/><font color="red">*</font></span>
					<span><select name=grade style="margin-top:3px">
					#foreach($row in $globals.getEnumerationItems("planGrade"))
						<option value="$row.value" #if("$row.value"=="$result.grade") selected #end>$row.name</option>
					#end
					</select></span>
				</div>
				<div class="WP_title">	
					<span><span style="width:70px;text-align:right">$!strDate</span> &nbsp;
					<select name=beginDateHour style="margin-top:3px;width:40px">
						<option value="00" #if($result.beginDate.substring(11,13)=="00") selected #end>00</option>
						<option value="01" #if($result.beginDate.substring(11,13)=="01") selected #end >01</option>
						<option value="02" #if($result.beginDate.substring(11,13)=="02") selected #end >02</option>
						<option value="03" #if($result.beginDate.substring(11,13)=="03") selected #end >03</option>
						<option value="04" #if($result.beginDate.substring(11,13)=="04") selected #end >04</option>
						<option value="05" #if($result.beginDate.substring(11,13)=="05") selected #end >05</option>
						<option value="06" #if($result.beginDate.substring(11,13)=="06") selected #end >06</option>
						<option value="07" #if($result.beginDate.substring(11,13)=="07") selected #end >07</option>
						<option value="08" #if($result.beginDate.substring(11,13)=="08") selected #end >08</option>
						<option value="09" #if($result.beginDate.substring(11,13)=="09") selected #end >09</option>
						<option value="10" #if($result.beginDate.substring(11,13)=="10") selected #end >10</option>
						<option value="11" #if($result.beginDate.substring(11,13)=="11") selected #end >11</option>
						<option value="12" #if($result.beginDate.substring(11,13)=="12") selected #end >12</option>
						<option value="13" #if($result.beginDate.substring(11,13)=="13") selected #end >13</option>
						<option value="14" #if($result.beginDate.substring(11,13)=="14") selected #end >14</option>
						<option value="15" #if($result.beginDate.substring(11,13)=="15") selected #end >15</option>
						<option value="16" #if($result.beginDate.substring(11,13)=="16") selected #end >16</option>
						<option value="17" #if($result.beginDate.substring(11,13)=="17") selected #end >17</option>
						<option value="18" #if($result.beginDate.substring(11,13)=="18") selected #end >18</option>
						<option value="19" #if($result.beginDate.substring(11,13)=="19") selected #end >19</option>
						<option value="20" #if($result.beginDate.substring(11,13)=="20") selected #end >20</option>
						<option value="21" #if($result.beginDate.substring(11,13)=="21") selected #end >21</option>
						<option value="22" #if($result.beginDate.substring(11,13)=="22") selected #end >22</option>
						<option value="23" #if($result.beginDate.substring(11,13)=="23") selected #end >23</option>
					</select>&nbsp;$text.get("com.date.hour")
					<select name=beginDateMin style="margin-top:3px;width:40px">
						<option value="00" #if($result.beginDate.substring(14,16)=="00") selected #end>00</option>
						<option value="05" #if($result.beginDate.substring(14,16)=="05") selected #end >05</option>
						<option value="10" #if($result.beginDate.substring(14,16)=="10") selected #end >10</option>
						<option value="15" #if($result.beginDate.substring(14,16)=="15") selected #end >15</option>
						<option value="20" #if($result.beginDate.substring(14,16)=="20") selected #end >20</option>
						<option value="25" #if($result.beginDate.substring(14,16)=="25") selected #end >25</option>
						<option value="30" #if($result.beginDate.substring(14,16)=="30") selected #end >30</option>
						<option value="35" #if($result.beginDate.substring(14,16)=="35") selected #end >35</option>
						<option value="40" #if($result.beginDate.substring(14,16)=="40") selected #end >40</option>
						<option value="45" #if($result.beginDate.substring(14,16)=="45") selected #end >45</option>
						<option value="50" #if($result.beginDate.substring(14,16)=="50") selected #end >50</option>
						<option value="55" #if($result.beginDate.substring(14,16)=="55") selected #end >55</option>
					</select>&nbsp;$text.get("com.date.minute")
					$text.get("common.msg.until")
					<select name=endDateHour style="margin-top:3px;width:40px">
						<option value="00" #if($result.endDate.substring(11,13)=="00") selected #end>00</option>
						<option value="01" #if($result.endDate.substring(11,13)=="01") selected #end >01</option>
						<option value="02" #if($result.endDate.substring(11,13)=="02") selected #end >02</option>
						<option value="03" #if($result.endDate.substring(11,13)=="03") selected #end >03</option>
						<option value="04" #if($result.endDate.substring(11,13)=="04") selected #end >04</option>
						<option value="05" #if($result.endDate.substring(11,13)=="05") selected #end >05</option>
						<option value="06" #if($result.endDate.substring(11,13)=="06") selected #end >06</option>
						<option value="07" #if($result.endDate.substring(11,13)=="07") selected #end >07</option>
						<option value="08" #if($result.endDate.substring(11,13)=="08") selected #end >08</option>
						<option value="09" #if($result.endDate.substring(11,13)=="09") selected #end >09</option>
						<option value="10" #if($result.endDate.substring(11,13)=="10") selected #end >10</option>
						<option value="11" #if($result.endDate.substring(11,13)=="11") selected #end >11</option>
						<option value="12" #if($result.endDate.substring(11,13)=="12") selected #end >12</option>
						<option value="13" #if($result.endDate.substring(11,13)=="13") selected #end >13</option>
						<option value="14" #if($result.endDate.substring(11,13)=="14") selected #end >14</option>
						<option value="15" #if($result.endDate.substring(11,13)=="15") selected #end >15</option>
						<option value="16" #if($result.endDate.substring(11,13)=="16") selected #end >16</option>
						<option value="17" #if($result.endDate.substring(11,13)=="17") selected #end >17</option>
						<option value="18" #if($result.endDate.substring(11,13)=="18") selected #end >18</option>
						<option value="19" #if($result.endDate.substring(11,13)=="19") selected #end >19</option>
						<option value="20" #if($result.endDate.substring(11,13)=="20") selected #end >20</option>
						<option value="21" #if($result.endDate.substring(11,13)=="21") selected #end >21</option>
						<option value="22" #if($result.endDate.substring(11,13)=="22") selected #end >22</option>
						<option value="23" #if($result.endDate.substring(11,13)=="23") selected #end >23</option>
					</select>&nbsp;$text.get("com.date.hour")
					<select name=endDateMin style="margin-top:3px;width:40px">
						<option value="00" #if($result.endDate.substring(14,16)=="00") selected #end>00</option>
						<option value="05" #if($result.endDate.substring(14,16)=="05") selected #end >05</option>
						<option value="10" #if($result.endDate.substring(14,16)=="10") selected #end >10</option>
						<option value="15" #if($result.endDate.substring(14,16)=="15") selected #end >15</option>
						<option value="20" #if($result.endDate.substring(14,16)=="20") selected #end >20</option>
						<option value="25" #if($result.endDate.substring(14,16)=="25") selected #end >25</option>
						<option value="30" #if($result.endDate.substring(14,16)=="30") selected #end >30</option>
						<option value="35" #if($result.endDate.substring(14,16)=="35") selected #end >35</option>
						<option value="40" #if($result.endDate.substring(14,16)=="40") selected #end >40</option>
						<option value="45" #if($result.endDate.substring(14,16)=="45") selected #end >45</option>
						<option value="50" #if($result.endDate.substring(14,16)=="50") selected #end >50</option>
						<option value="55" #if($result.endDate.substring(14,16)=="55") selected #end >55</option>
					</select><sapn style="border:none; ">$text.get("com.date.minute")</sapn>
					</span>
				</div>
				#elseif($planType=="event")
				<div class="WP_title">
					<span><span style="width:70px;text-align:right">$text.get("oa.subjects")</span>
					<input type="text" name="title" style="width:265px" value="$!result.title"/><font color="red">*</font></span>
					<span><select name=grade style="margin-top:3px">
					#foreach($row in $globals.getEnumerationItems("planGrade"))
						<option value="$row.value" #if("$row.value"=="$result.grade") selected #end>$row.name</option>
					#end
					</select></span>
				</div>
				<div class="WP_title" >
					<span><span style="width:70px;text-align:right">$text.get("oa.workplan.time")</span>
					<input type="text"  name="beginDate" value="$!result.beginDate" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN',dateFmt:'yyyy-MM-dd HH:mm:ss'});"/><font color="red">*</font>
					$text.get("common.msg.until")
					<input type="text"  name="endDate" value="$!result.endDate" onKeyDown="if(event.keyCode==13){event.keyCode=9;}" onClick="WdatePicker({lang:'zh_CN',dateFmt:'yyyy-MM-dd HH:mm:ss'});"/><font color="red">*</font>		
					</span>
				</div>
				#else		
					<input type="hidden"  name="beginDate" value="$!result.beginDate"/>
					<input type="hidden"  name="endDate" value="$!result.endDate"/>	
					<input type="hidden" name="title" value="$!result.title"/>
					<input type="hidden" name="grade" value="$!result.grade"/>					
				#end
				<div><textarea name="content" style="width:100%;height:300px;visibility:hidden;">$!result.content</textarea></div>
				<div style="float:left;width:300px;margin-top: 5px;">$text.get("upload.lb.affixupload")：<input type="button" class="bu_02" onClick="openAttachUpload('/plan/')" value="$text.get('oa.common.accessories')"></input>
				<div id="files_preview" style="padding-bottom:20px">				  		  
			  	#foreach ($str in $globals.strSplit($!result.attach,';'))
					 <div  id ="$str" style ="height:18px; color:#ff0000;">
				  	 <a href="javascript:;" onclick="removeFile('$str');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;
				  	 $str<input type=hidden name="attachFile" value="$str"/></div>	
				#end
		    	</div>
				</div>
				
			</td>
			<td class="WP_addL">
			#foreach($row in $associate)
				#if("$row.name" != "联系人")
				<div class="WP_title">
					<span>$row.name</span>
						#if("$row.name" == "关联客户")
						<a  href="javascript:openClientPop()"><img src="style/plan/jh_icon.gif" /></a>
					#else
						<a href="javascript:openPop('$!LoginBean.id','','$row.popSelect','$row.name','$row.id','$row.isEmployee','$row.linkAddress','ass$!result.id$row.id')"><img src="style/plan/jh_icon.gif" /></a>
					#end
				</div>
				<ul id="ass$!result.id$row.id">
					#foreach($item in $assItem)
					#if($globals.get($item,1) == $row.id)
				<li id="${result.id}_${row.id}_$globals.get($item,2)">
					<a href="javascript:delAssoicate('$result.id','${row.id}','$globals.get($item,2)','true')"><img src="style/plan/del_icon.gif" /></a>
					<span onclick="openLink('$globals.get($item,2)','$row.linkAddress')">#if($globals.get($item,3).length() >15) $globals.get($item,3).substring(0,15) #else $globals.get($item,3) #end</span>
					#if("$row.isEmployee"=="1" && "$globals.get($item,2)"!="$!LoginBean.id")
						<a href="javascript:msgComm('$globals.get($item,2)','$globals.get($item,3)')"><img src="style/plan/msg_icon.gif" /></a>
					#end
					<input type="hidden" name="assoicate" value="${row.id}:$globals.get($item,2):$globals.get($item,3):0:0"/></li>			
					 #end
					 #end
				</ul>
				#end
			#end	
			</td>
		</tr>
	</table>
</div>
</div>
</form>
</body>
</html>
