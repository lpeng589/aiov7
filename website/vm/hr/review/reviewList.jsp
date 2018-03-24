<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get("hr.perform.review")</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/function.js"></script>
<script language="javascript" src="/js/date.vjs"></script>

<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script language="javascript">
function openInputDate(obj){
	WdatePicker({lang:'$globals.getLocale()'});
}

function addReview(){
	window.open("/UserFunctionQueryAction.do?tableName=HRReview&src=menu&operation=6&noback=true",
				"","width=690,height=400,top="+(screen.availHeight/4)+",left="+(screen.availWidth/5));
}

function deliverTo(varURL){
	window.open(varURL,"","width=690,height=400,top="+(screen.availHeight/4)+",left="+(screen.availWidth/5));
}
function onDBClick(designId,billId){
	AjaxRequest("/UtilServlet?operation=getFlowDesc&designId="+designId+"&billId="+billId) ;
	document.getElementById("flowDesc").innerHTML = response ;
}

function beforeSubmit(){
	form.submit() ;
}
</script>
</head>
<body onLoad="showStatus();">
<iframe name="formFrame" style="display:none"></iframe>
<form  method="post" name="form" action="/HRReviewAction.do">
 <input type="hidden" name="operation" value="4">
 <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value=""> 
<div class="Heading">
	<div class="HeadingIcon"><img src="/$globals.getStylePath()/images/Left/Icon_1.gif"></div>
	<div class="HeadingTitle">$text.get("hr.perform.review")</div>
	<ul class="HeadingButton">
						
	</ul>
</div>
<div id="listRange_id">
		<ul class="oalistRange_1">
		<li>
		<span>$text.get("hr.review.begin.period")：</span>
		<input name="beginTime" type="text" class="text" value="$!reviewSearchForm.beginTime" onClick="openInputDate(this);">
		</li>
		<li>
		<span>$text.get("hr.review.end.period")：</span>
		<input name="endTime" type="text" class="text" value="$!reviewSearchForm.endTime"  onClick="openInputDate(this);">
		</li>
		<li>
		<span>$text.get("hr.review.employee.name")：</span>
		<input name="empFullName" class="text" value="$!reviewSearchForm.empFullName"/>
		</li>
		<li>
			<input type="hidden" name="pageParam" value="" />
			<button name="Submits" type="submit" onClick="clearPageParam(document.form)" class="b2">$text.get("common.lb.query")</button>
		</li>
		#if($LoginBean.operationMap.get("/HRReviewAction.do").add() && $!addFlow)
		<li><button type="button" onClick="javascript:addReview();" class="b3">$text.get("mrp.lb.add")</button></li>
		#end
	</ul>
<div class="scroll_function_small_a" id="conter">
<script type="text/javascript">
var oDiv=document.getElementById("conter");
var sHeight=document.body.clientHeight-95;
oDiv.style.height=sHeight+"px";
</script>
		<table width="790" border="0" cellpadding="0" cellspacing="0" class="listRange_list_function" id="tblSort" name="table">
		<thead>
			<tr>
				<td width="30" class="oabbs_tc">
					<input type="checkbox" name="selAll" onClick="checkAll('keyId')">
				</td>
				<td width="125" class="oabbs_tl">$text.get("hr.review.begin.period")</td>
				<td width="120" class="oabbs_tc">$text.get("hr.review.end.period")</td>
			    <td width="105" class="oabbs_tc">$text.get("hr.review.employee.name")</td>
			    <td width="100" class="oabbs_tc">$text.get("workflow.lb.deptName")</td>
	     	    <td width="100" class="oabbs_tc">$text.get("hr.review.my.goal")</td>
		        <td width="100" class="oabbs_tc">$text.get("hr.review.competency")</td>
		        <td width="45" class="oabbs_tc">$text.get("note.lb.detail")</td>
		        <td width="65" class="oabbs_tc">$text.get("oa.lb.deliverTo")</td>
			</tr>
		</thead>
		<tbody>
		  #foreach ($review in $reviewList)
			<tr onMouseMove="setBackground(this,true);" onMouseOut="setBackground(this,false);" onclick="onDBClick('$!globals.get($review,7)','$globals.get($review,0)');">
				<td width="30" class="oabbs_tc"> 
					<input type="checkbox" name="keyId" value="$globals.get($review,0)">
				</td>
				<td class="oabbs_tc">$!globals.get($review,1)&nbsp;</td>	
				<td class="oabbs_tc">$!globals.get($review,2)&nbsp;</td>
			 	<td class="oabbs_tc">$!globals.get($review,3)&nbsp;</td>
			 	<td class="oabbs_tc">$!globals.get($review,4)&nbsp;</td> 
				<td class="oabbs_tc">
				#if($!globals.get($review,8)=="-1")
				$!globals.newFormatNumber($!globals.get($review,5),false,false,$!globals.getSysIntswitch(),"HRReview","MyGoal",true)&nbsp;</td>
				#end &nbsp;
				</td>
		        <td class="oabbs_tc">
		        #if($!globals.get($review,8)=="-1")
		        $!globals.newFormatNumber($!globals.get($review,6),false,false,$!globals.getSysIntswitch(),"HRReview","Competency",true)&nbsp;
		        #end &nbsp;
		        </td>
		        <td class="oabbs_tc"><a href="javascript:mdiwin('/CrmTabAction.do?operation=5&tableName=HRReview&keyId=$globals.get($review,0)&designId=$!globals.get($review,7)&public=public','$text.get("hr.perform.review")')";>$text.get("note.lb.detail")</a></td>
			    <td class="oabbs_tc">
			    	#if($!globals.get($review,11).indexOf(";$LoginBean.id;")!=-1)
			    		<a href="javascript:deliverTo('/HRReviewAction.do?keyId=$!globals.get($review,0)&currNodeId=$!globals.get($review,8)&nextNodeIds=$!globals.get($review,9)&department=$!globals.get($review,10)&designId=$!globals.get($review,7)&operation=82&noback=true')">$text.get("oa.lb.deliverTo")</a>
			    	#end
			    &nbsp;
			    </td>
			</tr>
		  #end
		  </tbody>
		</table>
		<div  id="flowDesc" align="left" style="margin-top: 10px;">
		$!flowDoc
		</div>
		<div>
		<table style="padding-bottom: 0pt; padding-left: 5.4pt; padding-right: 5.4pt; border-collapse: collapse; padding-top: 10pt; mso-table-layout-alt: fixed;">
	    <tbody>
	        <tr style="height: 19.75pt">
	            <td style="border-bottom: rgb(0,0,0) 0.5pt solid; border-left: rgb(255,255,255) 31.875pt; padding-bottom: 0pt; padding-left: 5.4pt; width: 216.8pt; padding-right: 5.4pt; border-top: rgb(255,255,255) 31.875pt; border-right: rgb(255,255,255) 31.875pt; padding-top: 0pt; mso-border-left-alt: 31.8750pt none rgb(255,255,255); mso-border-top-alt: 31.8750pt none rgb(255,255,255); mso-border-right-alt: 31.8750pt none rgb(255,255,255); mso-border-bottom-alt: 0.5000pt solid rgb(0,0,0)" valign="top" width="289">
	            <p style="margin-top: 0pt; margin-bottom: 0pt" class="p0"><span style="font-family: 'Times New Roman'; font-size: 10pt; font-weight: bold"><o:p></o:p></span></p>
	            </td>
	            <td style="border-bottom: rgb(0,0,0) 0.5pt solid; border-left: medium none; padding-bottom: 0pt; padding-left: 5.4pt; width: 280.6pt; padding-right: 5.4pt; border-top: rgb(255,255,255) 31.875pt; border-right: rgb(255,255,255) 31.875pt; padding-top: 0pt; mso-border-left-alt: none; mso-border-top-alt: 31.8750pt none rgb(255,255,255); mso-border-right-alt: 31.8750pt none rgb(255,255,255); mso-border-bottom-alt: 0.5000pt solid rgb(0,0,0)" valign="top" width="374">
	            <p style="margin-top: 0pt; margin-bottom: 0pt" class="p0"><span style="font-family: 'Times New Roman'; font-size: 10pt; font-weight: bold"><o:p></o:p></span></p>
	            </td>
	        </tr>
	        <tr style="height: 19.75pt">
	            <td style="border-bottom: rgb(0,0,0) 0.5pt solid; border-left: rgb(0,0,0) 0.5pt solid; padding-bottom: 0pt; padding-left: 5.4pt; width: 216.8pt; padding-right: 5.4pt; border-top: medium none; border-right: rgb(0,0,0) 0.5pt solid; padding-top: 0pt; mso-border-left-alt: 0.5000pt solid rgb(0,0,0); mso-border-top-alt: 0.5000pt solid rgb(0,0,0); mso-border-right-alt: 0.5000pt solid rgb(0,0,0); mso-border-bottom-alt: 0.5000pt solid rgb(0,0,0)" valign="top" width="289">
	            <p style="margin-top: 0pt; margin-bottom: 0pt" class="p0"><span style="font-family: 'Times New Roman'; font-size: 10pt; font-weight: bold; mso-spacerun: 'yes'">Employee&rsquo;s&nbsp;Role</span><span style="font-family: 'Times New Roman'; font-size: 10pt; font-weight: bold"><o:p></o:p></span></p>
	            </td>
	            <td style="border-bottom: rgb(0,0,0) 0.5pt solid; border-left: medium none; padding-bottom: 0pt; padding-left: 5.4pt; width: 280.6pt; padding-right: 5.4pt; border-top: medium none; border-right: rgb(0,0,0) 0.5pt solid; padding-top: 0pt; mso-border-left-alt: none; mso-border-top-alt: 0.5000pt solid rgb(0,0,0); mso-border-right-alt: 0.5000pt solid rgb(0,0,0); mso-border-bottom-alt: 0.5000pt solid rgb(0,0,0)" valign="top" width="374">
	            <p style="margin-top: 0pt; margin-bottom: 0pt" class="p0"><span style="font-family: 'Times New Roman'; font-size: 10pt; font-weight: bold; mso-spacerun: 'yes'">Supervisor&rsquo;s&nbsp;/&nbsp;Manager&rsquo;s&nbsp;Role</span><span style="font-family: 'Times New Roman'; font-size: 10pt; font-weight: bold"><o:p></o:p></span></p>
	            </td>
	        </tr>
	        <tr style="height: 21.45pt">
	            <td style="border-bottom: rgb(0,0,0) 0.5pt solid; border-left: rgb(0,0,0) 0.5pt solid; padding-bottom: 0pt; padding-left: 5.4pt; width: 216.8pt; padding-right: 5.4pt; border-top: medium none; border-right: rgb(0,0,0) 0.5pt solid; padding-top: 0pt; mso-border-left-alt: 0.5000pt solid rgb(0,0,0); mso-border-top-alt: 0.5000pt solid rgb(0,0,0); mso-border-right-alt: 0.5000pt solid rgb(0,0,0); mso-border-bottom-alt: 0.5000pt solid rgb(0,0,0)" valign="top" width="289">
	            <p style="margin-top: 0pt; text-indent: -24pt; margin-bottom: 0pt; margin-left: 24pt" class="p0"><span style="font-family: 'Wingdings'; font-size: 10pt; mso-spacerun: 'yes'">l&nbsp;</span><span style="font-family: 'Times New Roman'; font-size: 10pt; mso-spacerun: 'yes'">Evaluate&nbsp;own&nbsp;performance&nbsp;for&nbsp;review&nbsp;period&nbsp;and&nbsp;draft&nbsp;Section&nbsp;1&nbsp;and&nbsp;2.</span><span style="font-family: 'Times New Roman'; font-size: 10pt"><o:p></o:p></span></p>
	            <p style="margin-top: 0pt; text-indent: -24pt; margin-bottom: 0pt; margin-left: 24pt" class="p0"><span style="font-family: 'Wingdings'; font-size: 10pt; mso-spacerun: 'yes'">l&nbsp;</span><span style="font-family: 'Times New Roman'; font-size: 10pt; mso-spacerun: 'yes'">Identify&nbsp;training&nbsp;/&nbsp;development&nbsp;needs&nbsp;and&nbsp;draft&nbsp;Section&nbsp;3.</span><span style="font-family: 'Times New Roman'; font-size: 10pt"><o:p></o:p></span></p>
	            </td>
	            <td style="border-bottom: rgb(0,0,0) 0.5pt solid; border-left: medium none; padding-bottom: 0pt; padding-left: 5.4pt; width: 280.6pt; padding-right: 5.4pt; border-top: medium none; border-right: rgb(0,0,0) 0.5pt solid; padding-top: 0pt; mso-border-left-alt: none; mso-border-top-alt: 0.5000pt solid rgb(0,0,0); mso-border-right-alt: 0.5000pt solid rgb(0,0,0); mso-border-bottom-alt: 0.5000pt solid rgb(0,0,0)" valign="top" width="374">
	            <p style="margin-top: 0pt; text-indent: -24pt; margin-bottom: 0pt; margin-left: 24pt" class="p0"><span style="font-family: 'Wingdings'; font-size: 10pt; mso-spacerun: 'yes'">l&nbsp;</span><span style="font-family: 'Times New Roman'; font-size: 10pt; mso-spacerun: 'yes'">Assess&nbsp;objective&nbsp;achievement&nbsp;and&nbsp;competencies&nbsp;of&nbsp;the&nbsp;employee.</span><span style="font-family: 'Times New Roman'; font-size: 10pt"><o:p></o:p></span></p>
	            <p style="margin-top: 0pt; text-indent: -24pt; margin-bottom: 0pt; margin-left: 24pt" class="p0"><span style="font-family: 'Wingdings'; font-size: 10pt; mso-spacerun: 'yes'">l&nbsp;</span><span style="font-family: 'Times New Roman'; font-size: 10pt; mso-spacerun: 'yes'">Hold&nbsp;discussions&nbsp;to&nbsp;review&nbsp;performance&nbsp;results.</span><span style="font-family: 'Times New Roman'; font-size: 10pt"><o:p></o:p></span></p>
	            <p style="margin-top: 0pt; text-indent: -24pt; margin-bottom: 0pt; margin-left: 24pt" class="p0"><span style="font-family: 'Wingdings'; font-size: 10pt; mso-spacerun: 'yes'">l&nbsp;</span><span style="font-family: 'Times New Roman'; font-size: 10pt; mso-spacerun: 'yes'">Agree&nbsp;on&nbsp;future&nbsp;development&nbsp;plans&nbsp;</span><span style="font-family: 'Times New Roman'; font-size: 10pt"><o:p></o:p></span></p>
	            <p style="margin-top: 0pt; text-indent: -24pt; margin-bottom: 0pt; margin-left: 24pt" class="p0"><span style="font-family: 'Wingdings'; font-size: 10pt; mso-spacerun: 'yes'">l&nbsp;</span><span style="font-family: 'Times New Roman'; font-size: 10pt; mso-spacerun: 'yes'">Act&nbsp;on&nbsp;training&nbsp;and&nbsp;development&nbsp;needs.</span><span style="font-family: 'Times New Roman'; font-size: 10pt"><o:p></o:p></span></p>
	            <p style="margin-top: 0pt; text-indent: -24pt; margin-bottom: 0pt; margin-left: 24pt" class="p0"><span style="font-family: 'Wingdings'; font-size: 10pt; mso-spacerun: 'yes'">l&nbsp;</span><span style="font-family: 'Times New Roman'; font-size: 10pt; mso-spacerun: 'yes'">Commit&nbsp;to&nbsp;regular&nbsp;review,&nbsp;feedback&nbsp;and&nbsp;coaching.</span><span style="font-family: 'Times New Roman'; font-size: 10pt; font-weight: bold"><o:p></o:p></span></p>
	            </td>
	        </tr>
	    </tbody>
		</table>
		</div>
	</div>
	<div class="listRange_pagebar"> $!pageBar </div>
</div>
</form>
</body>
</html>
