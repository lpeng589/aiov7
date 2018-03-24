
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/style1/css/ListRange.css" type="text/css" />
<title>Insert title here</title>
<style type="text/css" >
.listRange_1_photoAndDoc_1{margin-left:0px;width:98%;}
</style>

#if("$!nodate"=="nodate")
<script type="text/javascript" >
window.onload=function(){
	parent.asyncbox.confirm("该数据可能被删除","警告",function(action){
		parent.jQuery.close('warmsetting');
	});
}
</script>
#end
</head>
<body>
<div class="listRange_1_photoAndDoc_1">
<span>问题：</span>

</div>
<div class="listRange_1_photoAndDoc_1">
<span>创建者：</span><span>$!globals.getEmpFullNameByUserId($qa.createBy)</span><span>创建时间：</span><span>$!qa.createTime</span>

</div>
<div class="listRange_1_photoAndDoc_1" style=" height:150px;overFlow-y:scroll;">
$!qa.ref_id
</div>
<div class="listRange_1_photoAndDoc_1">
<span>解答：</span>
</div>
<div class="listRange_1_photoAndDoc_1" style=" height:150px;overFlow-y:scroll;">
$!qa.answer
</div>
<div class="listRange_1_photoAndDoc_1" >
<span>附件：</span>

 						#if($!qa.attachment.indexOf(";") > 0)
							#foreach ($str in $globals.strSplit($!qa.attachment,';'))
								<img src="/$globals.getStylePath()/images/down.gif"/>
								<a	href='/ReadFile?tempFile=path&path=/crm/QA/&fileName=$!globals.encode($!str)'target="_blank"> $str</a> 
						    #end
						#else
							暂无
						#end
</div>

</body>
</html>