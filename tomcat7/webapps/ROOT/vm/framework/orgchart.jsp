<!DOCTYPE html>
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>tree</title>
<link type="text/css" rel="stylesheet" href="/js/orgchart/jquery.jOrgChart.css"/>
<link type="text/css" rel="stylesheet" href="/style/css/oameeting/tip-yellow/tip-yellow.css"  />
<link type="text/css" rel="stylesheet" href="/style/css/base_button.css"  />
<script type="text/javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js" ></script>
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
<script type="text/javascript" src="/js/orgchart/jquery.jOrgChart.js" ></script>
<script type="text/javascript" src="/js/ui/jquery-ui-1.8.18.custom.min.js"></script>
<script type="text/javascript" src="/js/oa/titletip/jquery.poshytip.js" charset="utf-8"></script>

<script type="text/javascript"> 
var num=0;
var yuand=0;

$(document).ready(function(){
	$("#navigation").jOrgChart();
	$(".jOrgChart .node").css("cursor","move");
	$(".jOrgChart").bind("mousewheel",function(){
	 	zoom(this);
	    return false;
	});
	$("body").click(function(){$("#main").css("display","none");});
	$(".jOrgChart .node").click(function(e){
	e.stopPropagation();
	});
   
	$("#main").click(function(e){ e.stopPropagation();});
	$("#main").blur(function(e){$("#main").css("display","none"); e.stopPropagation();});
	
	$(".jOrgChart").draggable();
	$("a[title]").poshytip();
});


function big(){
	var o=$(".jOrgChart")[0];
	var zoom = parseInt(o.style.zoom, 10) || 100;
	zoom = zoom +10;
	//zoom += event.wheelDelta / 2; //可适合修改
    if (zoom >= 100  && zoom <=250 ){
        o.style.zoom = zoom + '%';
    }
}

function small(){
	var o=$(".jOrgChart")[0];
	var zoom = parseInt(o.style.zoom, 10) || 100;
	  zoom = zoom -10;
	    //zoom += event.wheelDelta / 2; //可适合修改
	    if (zoom >= 100  && zoom <=250 )
	        o.style.zoom = zoom + '%';
}




function zoom(o) {	
    var zoom = parseInt(o.style.zoom, 10) || 100;
   if(event.wheelDelta>0){
	   zoom += 10;
   }else{
	   zoom = zoom-10;
   }
    //zoom += event.wheelDelta / 2; //可适合修改
    if (zoom >= 100  && zoom <=250 )
        o.style.zoom = zoom + '%';
    
   
}


function goFarmeList(str){
	$("#main").css("display","block");
	$("#main li").css("display","none");
	var liid="#main li[id^='user_"+str+"']";
	$(liid).css("display","block");
	$("#main")[0].focus();
}
</script>

</head>
 
<body >
<!--  -->
<div id="main"  tabindex="-1">
	<ul style="width:310px;  background:#999;">
		#foreach($user in $allEmp)
		<li id="user_$user.DepartmentCode" style="background:#FC3;width:300px; height:110px; float:left; ">
			<table width="296" >
			  <tr>
			    <td width="92" rowspan="5">
			    <img style="border:4px solid #E8E8E8;cursor:default; float:left; display:inline;" src="$!globals.checkEmployeePhoto('48',$!user.id)" width="92px" height="100px" />
			   	<!-- #if("$!user.photo"=="")
					<img style="cursor:default; float:left; display:inline;" src="/style/images/no_head.gif" width="92px" height="100px" onClick="uploadImage();"/>
	            #else
	             	<img style="border:4px solid #E8E8E8;cursor:default; float:left; display:inline;" src="/ReadFile.jpg?fileName=$!{user.id}_140.jpg&amp;tempFile=false&amp;type=PIC&amp;tableName=tblEmployee" width="92px" height="100px" onclick="uploadImage();"/>
	           	#end -->
			    </td>
			    <td width="36">姓名</td>
			    <td width="146">$!user.empFullName</td>
			  </tr>
			  <tr>
			    <td>职务</td>
			    <td>$!globals.getEnumerationItemsDisplay("duty",$!user.titleID)</td>
			  </tr>
			  <tr>
			    <td>生日</td>
			    <td>$!user.bornDate</td>
			  </tr>
			  <tr>
			    <td>电话</td>
			    <td>$!user.tel</td>
			  </tr>
			  <tr>
			    <td height="19">手机</td>
			    <td>$!user.mobile</td>
			  </tr>
			</table>
		</li>
		#end
	</ul>
</div>



<div id="buttondiv">
<input class="btn" type="button" value="+" onclick="big()" />
<input class="btn" type="button" value="-" onclick="small()" />
</div>
<div id="xuan">
#if("$!classcode"=="")
<ul id="navigation" style="display:none">		
		<li>
		
		<span ><a title="#if($!globals.getCompanyName('')=="")$text.get("com.framework.from")#else$!globals.getCompanyName('')#end" href="javascript:goFarmeList('')">#if($!globals.getCompanyName('')=="")$text.get("com.framework.from")#else$!globals.getCompanyName('')#end</a><font style="color:#ff0000" id="_1_top"></font></span>
			
			<ul>
				$result				
			</ul>
		</li>
</ul>
#else
   <ul id="navigation" style="display:none">		
				$result				
</ul>
#end
</div>
</body>
</html>

