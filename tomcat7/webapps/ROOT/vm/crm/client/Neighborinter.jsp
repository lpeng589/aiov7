
<html>
<head>
<meta name="renderer" content="webkit">
<base target="_self"></base>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>$text.get('tblNeighbourMain.setting')</title>
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css">
<script language="javascript" src="/js/jquery.js"></script>
<style>
.df{
	width:300px;
	height:147px;
	border:1px solid #57a7ff;
	background-color:#ffffff;
	margin-top: 5px;
	margin-left: 5px;
}
.df table{
	font-size:11px;
	border-collapse:collapse;
	margin-left: 10px;
	width: 340px;
}
</style>

<script>
function isInt(str){
	var pattern = new RegExp("^[-]{0,1}[0-9]*$");
	ret = str.search(pattern);
	return ret>-1;
}
</script>

<script type="text/javascript">
jQuery(document).ready(function(){
	jQuery("#keyhavings").click(function() {
 		jQuery("input[name='keyhaving']").each(function(i,check){
 			$(this).attr("checked",allCheck) ;
		});
 	});
});

function submitBefore() {
 	var orderbys=jQuery("input[name='orderby']");
 	for(var i=0;i<orderbys.length;i++){
		if(!isInt(orderbys[i].value)){
			alert("$text.get('common.validate.error.integer.taxis')");
			orderbys[i].focus();
			return false;
		}
	}
	
	var falg=false;
	jQuery("input[name='keyhaving']").each(function(i,check){
		if(check.checked==true){
			falg=true;
		}
	});
	if(falg==false){
		alert("$text.get('common.msg.selectone')");
		return false;
	}
	form.submit();
}
</script>
</head>

<body>
<form method="post" name="form" action="/crmUserFunctionAction.do">
<input type="hidden" name="type" value="addneighbor">
<input type="hidden" name="tableName" value="$tableName">
  <div class="df" style="overflow: hidden;width: 98%;height:200px;overflow-y: auto;">
  		<div style="margin:5px 0px 10px 20px;">邻居表显示：向上<input type="radio" name="tab_bottom" value="1" #if($!tabStatus==1) checked #end/> &nbsp;居中<input type="radio" name="tab_bottom" value="2" #if($!tabStatus==2) checked #end/> &nbsp;向下<input type="radio" name="tab_bottom" value="3" #if($!tabStatus==3) checked #end/></div>
        <table cellspacing="0">
				<tr><td><input type="checkbox" id="keyhavings" name="keyhavings"/></td><td>$text.get('common.lb.selectAll')</td><td colspan=4>$text.get('sort')</td></tr>
				#set($num=1)
				<tr>
				#foreach($row in $childTabHavingList)
						#set($isChecked = false)
						#set($isvalue = "")
						#if($childTabList.size()==0)
							#set($isChecked = true)
						#end
						#foreach($rowtab in $childTabList)
							#if("$globals.getTableDisplayName($row.tableName)"=="$globals.getTableDisplayName($globals.get($rowtab,0))")
								#set($isChecked = true)
								#set($isvalue = $globals.get($rowtab,1))
							#end
						#end
						<td style="width:20px;"><input type="checkbox" name="keyhaving" value="$row.tableName" #if($isChecked == true) checked #end/></td>
						<td style="width:70px;">$globals.getTableDisplayName($row.tableName)</td>
						<td><input name="${row.tableName}orderby" id="orderby" #if($isvalue=="") value="$num" #else value="$!isvalue" #end style="width: 40px;border:1px solid #ccc;"/></td>
						#if($num%2==0)
							</tr><tr>
						#end
						#set($num=$num+1)
				#end
			</table>
    </div>
</form>
</body>
</html>
